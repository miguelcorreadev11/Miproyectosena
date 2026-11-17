// Paquete api: servidor HTTP mínimo sobre la tabla proveedores, con el mismo patrón de UsuarioApiServer.
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ApiUtil.* trae las funciones de JSON y HTTP compartidas con los demás *ApiServer.
import static api.ApiUtil.*;

// API REST básica sobre la tabla proveedores, para probar GET/POST/PUT/DELETE desde Postman.
public class ProveedorApiServer {

    // Reconoce /proveedores/{id}; el grupo 1 captura el id numérico.
    private static final Pattern RUTA_CON_ID = Pattern.compile("^/proveedores/(\\d+)/?$");

    // Arranca el HttpServer en el puerto 8083 y registra el único contexto /proveedores.
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8083), 0);
        server.createContext("/proveedores", new ProveedorHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("API de proveedores escuchando en http://localhost:8083/proveedores");
    }

    // Reparte cada request según el método HTTP y si la ruta trae o no un id.
    private static class ProveedorHandler implements HttpHandler {
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
                        default: responder(exchange, 405, error("Método no permitido en /proveedores/{id}"));
                    }
                } else if (path.equals("/proveedores") || path.equals("/proveedores/")) {
                    switch (method) {
                        case "GET": handleGetAll(exchange); break;
                        case "POST": handlePost(exchange); break;
                        default: responder(exchange, 405, error("Método no permitido en /proveedores"));
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

    // Lista todos los proveedores ordenados por nombre.
    private static void handleGetAll(HttpExchange exchange) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT id_proveedor, nombre, correo, telefono, direccion, ciudad, pais, estado, "
                + "fecha_registro FROM proveedores ORDER BY nombre";
        StringBuilder json = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",");
                json.append(proveedorAJson(rs));
                primero = false;
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
            return;
        }
        json.append("]");
        responder(exchange, 200, json.toString());
    }

    // Un proveedor por id; 404 si no existe.
    private static void handleGetOne(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT id_proveedor, nombre, correo, telefono, direccion, ciudad, pais, estado, "
                + "fecha_registro FROM proveedores WHERE id_proveedor = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    responder(exchange, 404, error("No existe un proveedor con id " + id));
                    return;
                }
                responder(exchange, 200, proveedorAJson(rs));
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
        }
    }

    // Crea un proveedor nuevo; el nombre es obligatorio.
    private static void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        if (vacio(datos.get("nombre"))) {
            responder(exchange, 400, error("nombre es obligatorio"));
            return;
        }

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "INSERT INTO proveedores (nombre, correo, telefono, direccion, ciudad, pais, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, datos.get("nombre"));
            ps.setString(2, datos.get("correo"));
            ps.setString(3, datos.get("telefono"));
            ps.setString(4, datos.get("direccion"));
            ps.setString(5, datos.get("ciudad"));
            ps.setString(6, datos.get("pais"));
            ps.setString(7, datos.getOrDefault("estado", "activo"));
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                responder(exchange, 201, obtenerProveedorJson(con, claves.getInt(1)));
            }
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo crear: " + e.getMessage()));
        }
    }

    // Actualiza un proveedor existente; 404 si el id no existe.
    private static void handlePut(HttpExchange exchange, int id) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "UPDATE proveedores SET nombre = ?, correo = ?, telefono = ?, direccion = ?, "
                + "ciudad = ?, pais = ?, estado = ? WHERE id_proveedor = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, datos.get("nombre"));
            ps.setString(2, datos.get("correo"));
            ps.setString(3, datos.get("telefono"));
            ps.setString(4, datos.get("direccion"));
            ps.setString(5, datos.get("ciudad"));
            ps.setString(6, datos.get("pais"));
            ps.setString(7, datos.getOrDefault("estado", "activo"));
            ps.setInt(8, id);
            if (ps.executeUpdate() == 0) {
                responder(exchange, 404, error("No existe un proveedor con id " + id));
                return;
            }
            responder(exchange, 200, obtenerProveedorJson(con, id));
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo actualizar: " + e.getMessage()));
        }
    }

    // Elimina un proveedor; 409 si todavía tiene compras asociadas.
    private static void handleDelete(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM proveedores WHERE id_proveedor = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                responder(exchange, 404, error("No existe un proveedor con id " + id));
                return;
            }
            exchange.sendResponseHeaders(204, -1);
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo eliminar (probablemente tiene compras asociadas): "
                    + e.getMessage()));
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

    // Relee un proveedor recién creado o actualizado para devolverlo completo en la respuesta.
    private static String obtenerProveedorJson(Connection con, int id) throws SQLException {
        String query = "SELECT id_proveedor, nombre, correo, telefono, direccion, ciudad, pais, estado, "
                + "fecha_registro FROM proveedores WHERE id_proveedor = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return proveedorAJson(rs);
            }
        }
    }

    // Convierte la fila actual del ResultSet en un objeto JSON.
    private static String proveedorAJson(ResultSet rs) throws SQLException {
        return "{"
                + "\"idProveedor\":" + rs.getInt("id_proveedor") + ","
                + "\"nombre\":" + jsonString(rs.getString("nombre")) + ","
                + "\"correo\":" + jsonString(rs.getString("correo")) + ","
                + "\"telefono\":" + jsonString(rs.getString("telefono")) + ","
                + "\"direccion\":" + jsonString(rs.getString("direccion")) + ","
                + "\"ciudad\":" + jsonString(rs.getString("ciudad")) + ","
                + "\"pais\":" + jsonString(rs.getString("pais")) + ","
                + "\"estado\":" + jsonString(rs.getString("estado")) + ","
                + "\"fechaRegistro\":" + jsonTimestamp(rs.getTimestamp("fecha_registro"))
                + "}";
    }
}
