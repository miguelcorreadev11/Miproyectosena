// Paquete api: servidor HTTP mínimo sobre la tabla almacenes, con el mismo patrón de UsuarioApiServer.
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
import java.sql.Statement;
import java.sql.Types;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ApiUtil.* trae las funciones de JSON y HTTP compartidas con los demás *ApiServer.
import static api.ApiUtil.*;

// API REST básica sobre la tabla almacenes, para probar GET/POST/PUT/DELETE desde Postman.
public class AlmacenApiServer {

    // Reconoce /almacenes/{id}; el grupo 1 captura el id numérico.
    private static final Pattern RUTA_CON_ID = Pattern.compile("^/almacenes/(\\d+)/?$");

    // Arranca el HttpServer en el puerto 8084 y registra el único contexto /almacenes.
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8084), 0);
        server.createContext("/almacenes", new AlmacenHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("API de almacenes escuchando en http://localhost:8084/almacenes");
    }

    // Reparte cada request según el método HTTP y si la ruta trae o no un id.
    private static class AlmacenHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                Matcher m = RUTA_CON_ID.matcher(path);

                if (m.matches()) {
                    int id = Integer.parseInt(m.group(1));
                    switch (method) {
                        case "GET": handleGetOne(exchange, id); break;
                        case "PUT": handlePut(exchange, id); break;
                        case "DELETE": handleDelete(exchange, id); break;
                        default: responder(exchange, 405, error("Método no permitido en /almacenes/{id}"));
                    }
                } else if (path.equals("/almacenes") || path.equals("/almacenes/")) {
                    switch (method) {
                        case "GET": handleGetAll(exchange); break;
                        case "POST": handlePost(exchange); break;
                        default: responder(exchange, 405, error("Método no permitido en /almacenes"));
                    }
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

    // Lista todos los almacenes ordenados por nombre.
    private static void handleGetAll(HttpExchange exchange) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT id_almacen, nombre, ubicacion, ciudad, capacidad_maxima, estado, "
                + "fecha_creacion FROM almacenes ORDER BY nombre";
        StringBuilder json = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",");
                json.append(almacenAJson(rs));
                primero = false;
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
            return;
        }
        json.append("]");
        responder(exchange, 200, json.toString());
    }

    // Un almacén por id; 404 si no existe.
    private static void handleGetOne(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT id_almacen, nombre, ubicacion, ciudad, capacidad_maxima, estado, "
                + "fecha_creacion FROM almacenes WHERE id_almacen = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    responder(exchange, 404, error("No existe un almacén con id " + id));
                    return;
                }
                responder(exchange, 200, almacenAJson(rs));
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
        }
    }

    // Crea un almacén nuevo; nombre y ubicación son obligatorios.
    private static void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        if (vacio(datos.get("nombre")) || vacio(datos.get("ubicacion"))) {
            responder(exchange, 400, error("nombre y ubicacion son obligatorios"));
            return;
        }

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "INSERT INTO almacenes (nombre, ubicacion, ciudad, capacidad_maxima, estado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, datos.get("nombre"));
            ps.setString(2, datos.get("ubicacion"));
            ps.setString(3, datos.get("ciudad"));
            // La capacidad es opcional; sin ella se guarda NULL.
            asignarCapacidad(ps, 4, datos.get("capacidadMaxima"));
            ps.setString(5, datos.getOrDefault("estado", "activo"));
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                responder(exchange, 201, obtenerAlmacenJson(con, claves.getInt(1)));
            }
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo crear: " + e.getMessage()));
        }
    }

    // Actualiza un almacén existente; 404 si el id no existe.
    private static void handlePut(HttpExchange exchange, int id) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "UPDATE almacenes SET nombre = ?, ubicacion = ?, ciudad = ?, capacidad_maxima = ?, "
                + "estado = ? WHERE id_almacen = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, datos.get("nombre"));
            ps.setString(2, datos.get("ubicacion"));
            ps.setString(3, datos.get("ciudad"));
            asignarCapacidad(ps, 4, datos.get("capacidadMaxima"));
            ps.setString(5, datos.getOrDefault("estado", "activo"));
            ps.setInt(6, id);
            if (ps.executeUpdate() == 0) {
                responder(exchange, 404, error("No existe un almacén con id " + id));
                return;
            }
            responder(exchange, 200, obtenerAlmacenJson(con, id));
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo actualizar: " + e.getMessage()));
        }
    }

    // Elimina un almacén; 409 si tiene inventario, compras o movimientos asociados.
    private static void handleDelete(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM almacenes WHERE id_almacen = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                responder(exchange, 404, error("No existe un almacén con id " + id));
                return;
            }
            exchange.sendResponseHeaders(204, -1);
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo eliminar (probablemente tiene inventario o movimientos "
                    + "asociados): " + e.getMessage()));
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

    // Pone el entero recibido o NULL si el campo no vino o no es un número (capacidad es opcional).
    private static void asignarCapacidad(PreparedStatement ps, int posicion, String valor) throws SQLException {
        try {
            ps.setInt(posicion, Integer.parseInt(valor));
        } catch (NumberFormatException e) {
            ps.setNull(posicion, Types.INTEGER);
        }
    }

    // Relee un almacén recién creado o actualizado para devolverlo completo en la respuesta.
    private static String obtenerAlmacenJson(Connection con, int id) throws SQLException {
        String query = "SELECT id_almacen, nombre, ubicacion, ciudad, capacidad_maxima, estado, "
                + "fecha_creacion FROM almacenes WHERE id_almacen = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return almacenAJson(rs);
            }
        }
    }

    // Convierte la fila actual del ResultSet en un objeto JSON.
    private static String almacenAJson(ResultSet rs) throws SQLException {
        // getInt devuelve 0 cuando la columna es NULL; wasNull() distingue 'sin capacidad' de un 0 real.
        int capacidad = rs.getInt("capacidad_maxima");
        String capacidadJson = rs.wasNull() ? "null" : String.valueOf(capacidad);
        return "{"
                + "\"idAlmacen\":" + rs.getInt("id_almacen") + ","
                + "\"nombre\":" + jsonString(rs.getString("nombre")) + ","
                + "\"ubicacion\":" + jsonString(rs.getString("ubicacion")) + ","
                + "\"ciudad\":" + jsonString(rs.getString("ciudad")) + ","
                + "\"capacidadMaxima\":" + capacidadJson + ","
                + "\"estado\":" + jsonString(rs.getString("estado")) + ","
                + "\"fechaCreacion\":" + jsonTimestamp(rs.getTimestamp("fecha_creacion"))
                + "}";
    }
}
