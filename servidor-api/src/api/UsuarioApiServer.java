// Paquete api: servidor HTTP mínimo, separado del Swing y de modelo.Usuario/controlador.Usuarios_control
// (esas clases usan JOptionPane, que bloquearía cada petición esperando un clic en pantalla).
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

// API REST básica sobre la tabla usuarios, para probar GET/POST/PUT/DELETE desde Postman.
public class UsuarioApiServer {

    // Reconoce /usuarios/{id}; el grupo 1 captura el id numérico.
    private static final Pattern RUTA_CON_ID = Pattern.compile("^/usuarios/(\\d+)/?$");

    // Arranca el HttpServer en el puerto 8080 y registra el único contexto /usuarios.
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/usuarios", new UsuarioHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("API de usuarios escuchando en http://localhost:8080/usuarios");
    }

    // Reparte cada request según el método HTTP y si la ruta trae o no un id.
    private static class UsuarioHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                Matcher m = RUTA_CON_ID.matcher(path);

                // POST /usuarios/login: inicio de sesión para la app móvil ArchiveX Móvil.
                if (path.equals("/usuarios/login") || path.equals("/usuarios/login/")) {
                    if ("POST".equals(method)) {
                        handleLogin(exchange);
                    } else {
                        responder(exchange, 405, error("Use POST en /usuarios/login"));
                    }
                } else if (m.matches()) {
                    int id = Integer.parseInt(m.group(1));
                    switch (method) {
                        case "GET": handleGetOne(exchange, id); break;
                        case "PUT": handlePut(exchange, id); break;
                        case "DELETE": handleDelete(exchange, id); break;
                        default: responder(exchange, 405, error("Método no permitido en /usuarios/{id}"));
                    }
                } else if (path.equals("/usuarios") || path.equals("/usuarios/")) {
                    switch (method) {
                        case "GET": handleGetAll(exchange); break;
                        case "POST": handlePost(exchange); break;
                        default: responder(exchange, 405, error("Método no permitido en /usuarios"));
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


    // Valida correo y contraseña igual que controlador.Login_control (usuario activo + hash SHA-256).
    // Responde 200 con el usuario (sin contraseña), 401 si las credenciales no coinciden y 403 si está inactivo.
    private static void handleLogin(HttpExchange exchange) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));
        String correo = datos.get("correoElectronico");
        String contrasena = datos.get("contrasena");
        if (vacio(correo) || vacio(contrasena)) {
            responder(exchange, 400, error("correoElectronico y contrasena son obligatorios"));
            return;
        }
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT id_usuario, nombre, apellido, correo_electronico, telefono, estado, rol, "
                + "fecha_creacion, contrasena FROM usuarios WHERE correo_electronico = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, correo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    responder(exchange, 401, error("Correo o contraseña incorrectos"));
                    return;
                }
                if (!"activo".equalsIgnoreCase(rs.getString("estado"))) {
                    responder(exchange, 403, error("El usuario se encuentra inactivo"));
                    return;
                }
                String hash = modelo.Usuario.hashPassword(contrasena);
                if (!hash.equals(rs.getString("contrasena"))) {
                    responder(exchange, 401, error("Correo o contraseña incorrectos"));
                    return;
                }
                responder(exchange, 200, usuarioAJson(rs));
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
        }
    }

    // Lista todos los usuarios ordenados por id.
    private static void handleGetAll(HttpExchange exchange) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT id_usuario, nombre, apellido, correo_electronico, telefono, estado, rol, "
                + "fecha_creacion FROM usuarios ORDER BY id_usuario";
        StringBuilder json = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",");
                json.append(usuarioAJson(rs));
                primero = false;
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
            return;
        }
        json.append("]");
        responder(exchange, 200, json.toString());
    }

    // Un usuario por id; 404 si no existe.
    private static void handleGetOne(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT id_usuario, nombre, apellido, correo_electronico, telefono, estado, rol, "
                + "fecha_creacion FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    responder(exchange, 404, error("No existe un usuario con id " + id));
                    return;
                }
                responder(exchange, 200, usuarioAJson(rs));
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
        }
    }

    // Crea un usuario nuevo; nombre, apellido, correoElectronico y contrasena son obligatorios.
    private static void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        String nombre = datos.get("nombre");
        String apellido = datos.get("apellido");
        String correo = datos.get("correoElectronico");
        String contrasena = datos.get("contrasena");
        if (vacio(nombre) || vacio(apellido) || vacio(correo) || vacio(contrasena)) {
            responder(exchange, 400, error("nombre, apellido, correoElectronico y contrasena son obligatorios"));
            return;
        }

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "INSERT INTO usuarios (nombre, apellido, correo_electronico, contrasena, telefono, "
                + "estado, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, correo);
            ps.setString(4, contrasena);
            ps.setString(5, datos.getOrDefault("telefono", ""));
            ps.setString(6, datos.getOrDefault("estado", "activo"));
            ps.setString(7, datos.getOrDefault("rol", "usuario"));
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                int idGenerado = claves.getInt(1);
                responder(exchange, 201, obtenerUsuarioJson(con, idGenerado));
            }
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo crear (revisa si el correo ya existe): " + e.getMessage()));
        }
    }

    // Actualiza un usuario existente; 404 si el id no existe.
    private static void handlePut(HttpExchange exchange, int id) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, correo_electronico = ?, telefono = ?, "
                + "estado = ?, rol = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, datos.get("nombre"));
            ps.setString(2, datos.get("apellido"));
            ps.setString(3, datos.get("correoElectronico"));
            ps.setString(4, datos.getOrDefault("telefono", ""));
            ps.setString(5, datos.getOrDefault("estado", "activo"));
            ps.setString(6, datos.getOrDefault("rol", "usuario"));
            ps.setInt(7, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                responder(exchange, 404, error("No existe un usuario con id " + id));
                return;
            }
            responder(exchange, 200, obtenerUsuarioJson(con, id));
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo actualizar: " + e.getMessage()));
        }
    }

    // Elimina un usuario; 409 si tiene historial, movimientos, compras o reportes asociados.
    private static void handleDelete(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios WHERE id_usuario = ?")) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                responder(exchange, 404, error("No existe un usuario con id " + id));
                return;
            }
            exchange.sendResponseHeaders(204, -1);
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo eliminar (probablemente tiene historial, movimientos, "
                    + "compras o reportes asociados): " + e.getMessage()));
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

    // Relee un usuario recién creado o actualizado para devolverlo completo en la respuesta.
    private static String obtenerUsuarioJson(Connection con, int id) throws SQLException {
        String query = "SELECT id_usuario, nombre, apellido, correo_electronico, telefono, estado, rol, "
                + "fecha_creacion FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return usuarioAJson(rs);
            }
        }
    }

    // Convierte la fila actual del ResultSet en un objeto JSON.
    private static String usuarioAJson(ResultSet rs) throws SQLException {
        return "{"
                + "\"idUsuario\":" + rs.getInt("id_usuario") + ","
                + "\"nombre\":" + jsonString(rs.getString("nombre")) + ","
                + "\"apellido\":" + jsonString(rs.getString("apellido")) + ","
                + "\"correoElectronico\":" + jsonString(rs.getString("correo_electronico")) + ","
                + "\"telefono\":" + jsonString(rs.getString("telefono")) + ","
                + "\"estado\":" + jsonString(rs.getString("estado")) + ","
                + "\"rol\":" + jsonString(rs.getString("rol")) + ","
                + "\"fechaCreacion\":" + jsonTimestamp(rs.getTimestamp("fecha_creacion"))
                + "}";
    }
}
