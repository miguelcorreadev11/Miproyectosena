// Paquete api: servidor HTTP de solo lectura sobre compras (ver nota de alcance al final del archivo).
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ApiUtil.* trae las funciones de JSON y HTTP compartidas con los demás *ApiServer.
import static api.ApiUtil.*;

// API de solo lectura (GET) sobre la tabla compras, para probar consultas desde Postman.
// No tiene POST/PUT/DELETE: crear o entregar una compra es una transacción que también escribe
// detallecompra, inventarioporalmacen, productos.cantidad e historialinventario (modelo.Compra y
// modelo.MovimientoInventario); reimplementar esa lógica con SQL suelto rompería la consistencia
// del inventario real, así que para este recurso solo se exponen consultas seguras.
public class CompraApiServer {

    // Reconoce /compras/{id}; el grupo 1 captura el id numérico.
    private static final Pattern RUTA_CON_ID = Pattern.compile("^/compras/(\\d+)/?$");

    // Arranca el HttpServer en el puerto 8085 y registra el único contexto /compras.
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8085), 0);
        server.createContext("/compras", new CompraHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("API de compras (solo lectura) escuchando en http://localhost:8085/compras");
    }

    // Reparte cada request: solo GET, con o sin id.
    private static class CompraHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                Matcher m = RUTA_CON_ID.matcher(path);

                if (!"GET".equals(method)) {
                    responder(exchange, 405, error("Esta API es de solo lectura: solo admite GET"));
                } else if (m.matches()) {
                    handleGetOne(exchange, Integer.parseInt(m.group(1)));
                } else if (path.equals("/compras") || path.equals("/compras/")) {
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

    // Lista las compras con nombre de proveedor y de almacén, de la más reciente a la más antigua.
    private static void handleGetAll(HttpExchange exchange) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT c.id_compra, c.numero_pedido, c.id_proveedor, c.id_almacen, c.fecha_pedido, "
                + "c.fecha_entrega, c.valor_total, c.estado, c.comentarios, p.nombre AS nombre_proveedor, "
                + "a.nombre AS nombre_almacen FROM compras c "
                + "INNER JOIN proveedores p ON c.id_proveedor = p.id_proveedor "
                + "INNER JOIN almacenes a ON c.id_almacen = a.id_almacen "
                + "ORDER BY c.fecha_pedido DESC, c.id_compra DESC";
        StringBuilder json = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",");
                json.append(compraAJson(rs));
                primero = false;
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
            return;
        }
        json.append("]");
        responder(exchange, 200, json.toString());
    }

    // Una compra por id, con nombre de proveedor y de almacén; 404 si no existe.
    private static void handleGetOne(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT c.id_compra, c.numero_pedido, c.id_proveedor, c.id_almacen, c.fecha_pedido, "
                + "c.fecha_entrega, c.valor_total, c.estado, c.comentarios, p.nombre AS nombre_proveedor, "
                + "a.nombre AS nombre_almacen FROM compras c "
                + "INNER JOIN proveedores p ON c.id_proveedor = p.id_proveedor "
                + "INNER JOIN almacenes a ON c.id_almacen = a.id_almacen WHERE c.id_compra = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    responder(exchange, 404, error("No existe una compra con id " + id));
                    return;
                }
                responder(exchange, 200, compraAJson(rs));
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

    // Convierte la fila actual del ResultSet (con JOIN a proveedores y almacenes) en un objeto JSON.
    private static String compraAJson(ResultSet rs) throws SQLException {
        return "{"
                + "\"idCompra\":" + rs.getInt("id_compra") + ","
                + "\"numeroPedido\":" + rs.getInt("numero_pedido") + ","
                + "\"idProveedor\":" + rs.getInt("id_proveedor") + ","
                + "\"nombreProveedor\":" + jsonString(rs.getString("nombre_proveedor")) + ","
                + "\"idAlmacen\":" + rs.getInt("id_almacen") + ","
                + "\"nombreAlmacen\":" + jsonString(rs.getString("nombre_almacen")) + ","
                + "\"fechaPedido\":" + jsonTimestamp(rs.getTimestamp("fecha_pedido")) + ","
                + "\"fechaEntrega\":" + jsonTimestamp(rs.getTimestamp("fecha_entrega")) + ","
                + "\"valorTotal\":" + rs.getBigDecimal("valor_total") + ","
                + "\"estado\":" + jsonString(rs.getString("estado")) + ","
                + "\"comentarios\":" + jsonString(rs.getString("comentarios"))
                + "}";
    }
}
