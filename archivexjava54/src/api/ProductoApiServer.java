// Paquete api: servidor HTTP mínimo sobre la tabla productos, con el mismo patrón de UsuarioApiServer.
package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.math.BigDecimal;
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

// API REST básica sobre la tabla productos, para probar GET/POST/PUT/DELETE desde Postman.
public class ProductoApiServer {

    // Reconoce /productos/{id}; el grupo 1 captura el id numérico.
    private static final Pattern RUTA_CON_ID = Pattern.compile("^/productos/(\\d+)/?$");

    // Arranca el HttpServer en el puerto 8081 y registra el único contexto /productos.
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/productos", new ProductoHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("API de productos escuchando en http://localhost:8081/productos");
    }

    // Reparte cada request según el método HTTP y si la ruta trae o no un id.
    private static class ProductoHandler implements HttpHandler {
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
                        default: responder(exchange, 405, error("Método no permitido en /productos/{id}"));
                    }
                } else if (path.equals("/productos") || path.equals("/productos/")) {
                    switch (method) {
                        case "GET": handleGetAll(exchange); break;
                        case "POST": handlePost(exchange); break;
                        default: responder(exchange, 405, error("Método no permitido en /productos"));
                    }
                } else {
                    responder(exchange, 404, error("Ruta no encontrada"));
                }
            } catch (Exception e) {
                // Cualquier fallo inesperado (JSON mal formado, número inválido, etc.) termina en 500.
                e.printStackTrace();
                responder(exchange, 500, error("Error interno: " + e.getMessage()));
            }
        }
    }

    // ---- Handlers ----

    // Lista todos los productos con el nombre de su categoría (igual que getProductosConCategoria).
    private static void handleGetAll(HttpExchange exchange) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT p.id_producto, p.nombre, p.descripcion, p.id_categoria, p.precio_compra, "
                + "p.precio_venta, p.cantidad, p.estado, p.fecha_creacion, p.imagen, c.nombre AS nombre_categoria "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "ORDER BY p.id_producto";
        StringBuilder json = new StringBuilder("[");
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            boolean primero = true;
            while (rs.next()) {
                if (!primero) json.append(",");
                json.append(productoAJson(rs));
                primero = false;
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
            return;
        }
        json.append("]");
        responder(exchange, 200, json.toString());
    }

    // Un producto por id, con el nombre de su categoría; 404 si no existe.
    private static void handleGetOne(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        String query = "SELECT p.id_producto, p.nombre, p.descripcion, p.id_categoria, p.precio_compra, "
                + "p.precio_venta, p.cantidad, p.estado, p.fecha_creacion, p.imagen, c.nombre AS nombre_categoria "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    responder(exchange, 404, error("No existe un producto con id " + id));
                    return;
                }
                responder(exchange, 200, productoAJson(rs));
            }
        } catch (SQLException e) {
            responder(exchange, 500, error("Error de MySQL: " + e.getMessage()));
        }
    }

    // Crea un producto nuevo; nombre, idCategoria, precioCompra, precioVenta y cantidad son obligatorios.
    private static void handlePost(HttpExchange exchange) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        String nombre = datos.get("nombre");
        if (vacio(nombre) || vacio(datos.get("idCategoria")) || vacio(datos.get("precioCompra"))
                || vacio(datos.get("precioVenta")) || vacio(datos.get("cantidad"))) {
            responder(exchange, 400, error("nombre, idCategoria, precioCompra, precioVenta y cantidad "
                    + "son obligatorios"));
            return;
        }

        int idCategoria;
        BigDecimal precioCompra;
        BigDecimal precioVenta;
        int cantidad;
        try {
            // Convierte los campos numéricos; un formato inválido cae al catch de abajo.
            idCategoria = Integer.parseInt(datos.get("idCategoria"));
            precioCompra = new BigDecimal(datos.get("precioCompra"));
            precioVenta = new BigDecimal(datos.get("precioVenta"));
            cantidad = Integer.parseInt(datos.get("cantidad"));
        } catch (NumberFormatException e) {
            responder(exchange, 400, error("idCategoria y cantidad deben ser enteros; precioCompra y "
                    + "precioVenta deben ser números"));
            return;
        }

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "INSERT INTO productos (nombre, descripcion, id_categoria, precio_compra, "
                + "precio_venta, cantidad, estado, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, datos.get("descripcion"));
            ps.setInt(3, idCategoria);
            ps.setBigDecimal(4, precioCompra);
            ps.setBigDecimal(5, precioVenta);
            ps.setInt(6, cantidad);
            ps.setString(7, datos.getOrDefault("estado", "activo"));
            ps.setString(8, datos.get("imagen"));
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                claves.next();
                responder(exchange, 201, obtenerProductoJson(con, claves.getInt(1)));
            }
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo crear (revisa que la categoría exista): " + e.getMessage()));
        }
    }

    // Actualiza un producto existente; 404 si el id no existe.
    private static void handlePut(HttpExchange exchange, int id) throws IOException {
        Map<String, String> datos = parseJsonObject(leerCuerpo(exchange));

        int idCategoria;
        BigDecimal precioCompra;
        BigDecimal precioVenta;
        int cantidad;
        try {
            idCategoria = Integer.parseInt(datos.get("idCategoria"));
            precioCompra = new BigDecimal(datos.get("precioCompra"));
            precioVenta = new BigDecimal(datos.get("precioVenta"));
            cantidad = Integer.parseInt(datos.get("cantidad"));
        } catch (NumberFormatException e) {
            responder(exchange, 400, error("idCategoria y cantidad deben ser enteros; precioCompra y "
                    + "precioVenta deben ser números"));
            return;
        }

        Connection con = obtenerConexion(exchange);
        if (con == null) return;

        String query = "UPDATE productos SET nombre = ?, descripcion = ?, id_categoria = ?, precio_compra = ?, "
                + "precio_venta = ?, cantidad = ?, estado = ?, imagen = ? WHERE id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, datos.get("nombre"));
            ps.setString(2, datos.get("descripcion"));
            ps.setInt(3, idCategoria);
            ps.setBigDecimal(4, precioCompra);
            ps.setBigDecimal(5, precioVenta);
            ps.setInt(6, cantidad);
            ps.setString(7, datos.getOrDefault("estado", "activo"));
            ps.setString(8, datos.get("imagen"));
            ps.setInt(9, id);
            if (ps.executeUpdate() == 0) {
                responder(exchange, 404, error("No existe un producto con id " + id));
                return;
            }
            responder(exchange, 200, obtenerProductoJson(con, id));
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo actualizar: " + e.getMessage()));
        }
    }

    // Elimina un producto; 409 si tiene inventario, compras o movimientos asociados.
    private static void handleDelete(HttpExchange exchange, int id) throws IOException {
        Connection con = obtenerConexion(exchange);
        if (con == null) return;
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM productos WHERE id_producto = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                responder(exchange, 404, error("No existe un producto con id " + id));
                return;
            }
            exchange.sendResponseHeaders(204, -1);
        } catch (SQLException e) {
            responder(exchange, 409, error("No se pudo eliminar (probablemente tiene inventario, compras o "
                    + "movimientos asociados): " + e.getMessage()));
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

    // Relee un producto recién creado o actualizado para devolverlo completo en la respuesta.
    private static String obtenerProductoJson(Connection con, int id) throws SQLException {
        String query = "SELECT p.id_producto, p.nombre, p.descripcion, p.id_categoria, p.precio_compra, "
                + "p.precio_venta, p.cantidad, p.estado, p.fecha_creacion, p.imagen, c.nombre AS nombre_categoria "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "WHERE p.id_producto = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return productoAJson(rs);
            }
        }
    }

    // Convierte la fila actual del ResultSet (con JOIN a categorias) en un objeto JSON.
    private static String productoAJson(ResultSet rs) throws SQLException {
        return "{"
                + "\"idProducto\":" + rs.getInt("id_producto") + ","
                + "\"nombre\":" + jsonString(rs.getString("nombre")) + ","
                + "\"descripcion\":" + jsonString(rs.getString("descripcion")) + ","
                + "\"idCategoria\":" + rs.getInt("id_categoria") + ","
                + "\"nombreCategoria\":" + jsonString(rs.getString("nombre_categoria")) + ","
                + "\"precioCompra\":" + rs.getBigDecimal("precio_compra") + ","
                + "\"precioVenta\":" + rs.getBigDecimal("precio_venta") + ","
                + "\"cantidad\":" + rs.getInt("cantidad") + ","
                + "\"estado\":" + jsonString(rs.getString("estado")) + ","
                + "\"imagen\":" + jsonString(rs.getString("imagen")) + ","
                + "\"fechaCreacion\":" + jsonTimestamp(rs.getTimestamp("fecha_creacion"))
                + "}";
    }
}
