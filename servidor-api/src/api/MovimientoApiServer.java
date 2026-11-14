// Paquete api: servidor HTTP de movimientos: consulta (GET) y registro (POST) con las reglas del modelo.
package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ApiUtil.* trae las funciones de JSON y HTTP compartidas con los demás *ApiServer.
import static api.ApiUtil.*;

// API de movimientos de inventario. GET consulta; POST registra un movimiento llamando a
// modelo.MovimientoInventario.registrarMovimientoApi, que usa la misma transacción del escritorio:
// bloquea la fila de inventarioporalmacen, recalcula productos.cantidad y escribe historialinventario.
// Así el inventario no queda inconsistente. No tiene PUT/DELETE: un movimiento no se edita ni se borra.
public class MovimientoApiServer {

    // Reconoce /movimientos/{id}; el grupo 1 captura el id numérico.
    private static final Pattern RUTA_CON_ID = Pattern.compile("^/movimientos/(\\d+)/?$");

    // Arranca el HttpServer en el puerto 8086 y registra el único contexto /movimientos.
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8086), 0);
        server.createContext("/movimientos", new MovimientoHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("API de movimientos escuchando en http://localhost:8086/movimientos");
    }

    // Reparte cada request: solo GET, con o sin id.
    private static class MovimientoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                Matcher m = RUTA_CON_ID.matcher(path);

                boolean rutaBase = path.equals("/movimientos") || path.equals("/movimientos/");
                if ("POST".equals(method) && rutaBase) {
                    handlePost(exchange);
                } else if (!"GET".equals(method)) {
                    responder(exchange, 405, error("Use GET para consultar o POST en /movimientos para registrar"));
                } else if (m.matches()) {
                    handleGetOne(exchange, Integer.parseInt(m.group(1)));
                } else if (path.equals("/movimientos") || path.equals("/movimientos/")) {
                    handleGetAll(exchange);
                } else {
                    responder(exchange, 404, error("Ruta no encontrada"));
                }
            } catch (Exception e) {
                // Cualquier fallo inesperado termina en 500 en vez de colgar la petición.
                e.printStackTrace();
                responder(exchange, 500, error("Error interno: " + e.getMessage()));
            }
        }
    }

    // ---- Handlers ----

    // Lista los movimientos con nombres de producto, almacén y usuario, del más reciente al más antiguo.
    private static void handleGetAll(HttpExchange exchange) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = baseQuery() + " ORDER BY m.fecha_movimiento DESC, m.id_movimiento DESC";
        StringBuilder json = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",");
                json.append(movimientoAJson(rs));
                primero = false;
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
            return;
        }
        json.append("]");
        responder(exchange, 200, json.toString());
    }

    // Un movimiento por id, con los mismos nombres del JOIN; 404 si no existe.
    private static void handleGetOne(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = baseQuery() + " AND m.id_movimiento = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    responder(exchange, 404, error("No existe un movimiento con id " + id));
                    return;
                }
                responder(exchange, 200, movimientoAJson(rs));
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
        }
    }

    // Registra un movimiento: idProducto, idAlmacen, idUsuario, tipoMovimiento y cantidad son obligatorios.
    private static void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));
        if (vacio(datos.get("idProducto")) || vacio(datos.get("idAlmacen")) || vacio(datos.get("idUsuario"))
                || vacio(datos.get("tipoMovimiento")) || vacio(datos.get("cantidad"))) {
            responder(exchange, 400, error("idProducto, idAlmacen, idUsuario, tipoMovimiento y cantidad "
                    + "son obligatorios"));
            return;
        }
        int idProducto, idAlmacen, idUsuario, cantidad;
        try {
            idProducto = Integer.parseInt(datos.get("idProducto"));
            idAlmacen = Integer.parseInt(datos.get("idAlmacen"));
            idUsuario = Integer.parseInt(datos.get("idUsuario"));
            cantidad = Integer.parseInt(datos.get("cantidad"));
        } catch (NumberFormatException e) {
            responder(exchange, 400, error("idProducto, idAlmacen, idUsuario y cantidad deben ser enteros"));
            return;
        }
        String tipo = datos.get("tipoMovimiento").trim().toLowerCase();
        if (!java.util.Arrays.asList(modelo.MovimientoInventario.getTiposMovimiento()).contains(tipo)) {
            responder(exchange, 400, error("tipoMovimiento debe ser entrada, salida, ajuste o devolucion"));
            return;
        }
        boolean esAjuste = modelo.MovimientoInventario.TIPO_AJUSTE.equals(tipo);
        if (cantidad < 0 || (!esAjuste && cantidad == 0)) {
            responder(exchange, 400, error("La cantidad debe ser mayor que cero (en ajuste, cero o más)"));
            return;
        }
        String motivo = datos.get("motivo");
        String rechazo = modelo.MovimientoInventario.registrarMovimientoApi(idProducto, idAlmacen, idUsuario,
                tipo, cantidad, vacio(motivo) ? "Registrado desde ArchiveX Móvil" : motivo);
        if (rechazo != null) {
            responder(exchange, 409, error("No se pudo registrar el movimiento: " + rechazo));
            return;
        }
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = baseQuery() + " AND m.id_producto = ? AND m.id_almacen = ? ORDER BY m.id_movimiento DESC LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, idProducto);
            ps.setInt(2, idAlmacen);
            try (ResultSet rs = ps.executeQuery()) {
                responder(exchange, 201, rs.next() ? movimientoAJson(rs) : "{}");
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
        }
    }

    // ---- Utilidades propias de este recurso ----

    // Conexión compartida con MySQL; responde 503 y devuelve null si el servidor no está disponible.
    private static Connection obtenerConexion(HttpExchange exchange) throws IOException {
        Connection con = config.conexión_BD.getConnection();
        if (con == null) {
            responder(exchange, 503, error(config.conexión_BD.MENSAJE_SIN_CONEXION));
        }
        return con;
    }

    // SELECT con los JOIN necesarios para los nombres; 'WHERE 1 = 1' deja agregar filtros con AND.
    private static String baseQuery() {
        return "SELECT m.id_movimiento, m.id_producto, m.id_almacen, m.id_usuario, m.tipo_movimiento, "
                + "m.cantidad, m.id_compra, m.motivo, m.fecha_movimiento, p.nombre AS nombre_producto, "
                + "a.nombre AS nombre_almacen, CONCAT(u.nombre, ' ', u.apellido) AS nombre_usuario, "
                + "c.numero_pedido FROM movimientoinventario m "
                + "INNER JOIN productos p ON m.id_producto = p.id_producto "
                + "INNER JOIN almacenes a ON m.id_almacen = a.id_almacen "
                + "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario "
                + "LEFT JOIN compras c ON m.id_compra = c.id_compra WHERE 1 = 1";
    }

    // Convierte la fila actual del ResultSet (con los JOIN de baseQuery) en un objeto JSON.
    private static String movimientoAJson(ResultSet rs) throws SQLException {
        // id_compra y numero_pedido son opcionales (LEFT JOIN); getObject(..., Integer.class) da null.
        Integer idCompra = rs.getObject("id_compra", Integer.class);
        Integer numeroPedido = rs.getObject("numero_pedido", Integer.class);
        return "{"
                + "\"idMovimiento\":" + rs.getInt("id_movimiento") + ","
                + "\"idProducto\":" + rs.getInt("id_producto") + ","
                + "\"nombreProducto\":" + jsonString(rs.getString("nombre_producto")) + ","
                + "\"idAlmacen\":" + rs.getInt("id_almacen") + ","
                + "\"nombreAlmacen\":" + jsonString(rs.getString("nombre_almacen")) + ","
                + "\"idUsuario\":" + rs.getInt("id_usuario") + ","
                + "\"nombreUsuario\":" + jsonString(rs.getString("nombre_usuario")) + ","
                + "\"tipoMovimiento\":" + jsonString(rs.getString("tipo_movimiento")) + ","
                + "\"cantidad\":" + rs.getInt("cantidad") + ","
                + "\"idCompra\":" + (idCompra == null ? "null" : idCompra) + ","
                + "\"numeroPedido\":" + (numeroPedido == null ? "null" : numeroPedido) + ","
                + "\"motivo\":" + jsonString(rs.getString("motivo")) + ","
                + "\"fechaMovimiento\":" + jsonTimestamp(rs.getTimestamp("fecha_movimiento"))
                + "}";
    }
}
