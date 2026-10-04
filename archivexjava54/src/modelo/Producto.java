// Paquete modelo: clases que representan tablas de MySQL y ejecutan su SQL con JDBC.
package modelo;

// BigDecimal: valores monetarios exactos (columnas DECIMAL).
import java.math.BigDecimal;
// Connection: conexión JDBC compartida que entrega config.conexión_BD.
import java.sql.Connection;
// PreparedStatement: sentencias SQL parametrizadas (evitan inyección SQL).
import java.sql.PreparedStatement;
// ResultSet: filas devueltas por los SELECT.
import java.sql.ResultSet;
// SQLException: errores reportados por MySQL o por el driver JDBC.
import java.sql.SQLException;
// Timestamp: representa las columnas DATETIME de la tabla.
import java.sql.Timestamp;
// ArrayList: listas de registros devueltas a los controladores.
import java.util.ArrayList;
// Level.SEVERE marca la gravedad de los errores registrados.
import java.util.logging.Level;
// Logger: escribe las excepciones en la consola de NetBeans.
import java.util.logging.Logger;
// JOptionPane: cuadros de diálogo de error, aviso o confirmación.
import javax.swing.JOptionPane;

// Modelo de la tabla productos; lo usan Productos, Búsqueda, Compras, Movimientos y Reportes.
// La columna imagen guarda solo el nombre del archivo que carga vista.ImagenProducto.
public class Producto {
    // Columna id_producto (llave primaria AUTO_INCREMENT).
    private Integer idProducto;
    // Columna nombre (obligatoria).
    private String nombre;
    // Columna descripcion (opcional).
    private String descripcion;
    // Columna id_categoria (llave foránea fk_productos_categoria).
    private Integer idCategoria;
    // Columna precio_compra DECIMAL(10,2).
    private BigDecimal precioCompra;
    // Columna precio_venta DECIMAL(10,2).
    private BigDecimal precioVenta;
    // Columna cantidad UNSIGNED: existencia total del producto.
    private Integer cantidad;
    // Columna estado: 'activo' o 'descontinuado'.
    private String estado;
    // Columna fecha_creacion (la asigna MySQL).
    private Timestamp fechaCreacion;

    // Columna imagen: nombre del archivo (por ejemplo, Televisor.png) o null.
    private String imagen;

    // Campo auxiliar: nombre de la categoría, llenado solo en las consultas con JOIN.
    private String nombreCategoria;

    // Constructor vacío.
    public Producto() {
    }

    // Constructor completo, usado por mapResultSet al leer una fila.
    public Producto(Integer idProducto, String nombre, String descripcion,
            Integer idCategoria, BigDecimal precioCompra, BigDecimal precioVenta, Integer cantidad,
            String estado, Timestamp fechaCreacion) {
        // Asigna el id del producto (columna id_producto).
        this.idProducto = idProducto;
        // Asigna el nombre del producto.
        this.nombre = nombre;
        // Asigna la descripción del producto.
        this.descripcion = descripcion;
        // Asigna el id de la categoría (llave foránea a categorias).
        this.idCategoria = idCategoria;
        // Asigna el precio de compra.
        this.precioCompra = precioCompra;
        // Asigna el precio de venta.
        this.precioVenta = precioVenta;
        // Asigna la cantidad total del producto (suma que mantienen los movimientos).
        this.cantidad = cantidad;
        // Asigna el estado ('activo' o 'descontinuado').
        this.estado = estado;
        // Asigna la fecha de creación asignada por MySQL.
        this.fechaCreacion = fechaCreacion;
    }

    // Constructor para un producto nuevo (sin id ni fecha, que genera MySQL).
    public Producto(String nombre, String descripcion, Integer idCategoria,
            BigDecimal precioCompra, BigDecimal precioVenta, Integer cantidad, String estado) {
        // Asigna el nombre del producto.
        this.nombre = nombre;
        // Asigna la descripción del producto.
        this.descripcion = descripcion;
        // Asigna el id de la categoría (llave foránea a categorias).
        this.idCategoria = idCategoria;
        // Asigna el precio de compra.
        this.precioCompra = precioCompra;
        // Asigna el precio de venta.
        this.precioVenta = precioVenta;
        // Asigna la cantidad total del producto (suma que mantienen los movimientos).
        this.cantidad = cantidad;
        // Asigna el estado ('activo' o 'descontinuado').
        this.estado = estado;
    }

    // Devuelve el id del producto (columna id_producto).
    public Integer getIdProducto() {
        return idProducto;
    }

    // Cambia el id del producto (columna id_producto).
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    // Devuelve el nombre del producto.
    public String getNombre() {
        return nombre;
    }

    // Cambia el nombre del producto.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve la descripción del producto.
    public String getDescripcion() {
        return descripcion;
    }

    // Cambia la descripción del producto.
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Devuelve el id de la categoría (llave foránea a categorias).
    public Integer getIdCategoria() {
        return idCategoria;
    }

    // Cambia el id de la categoría (llave foránea a categorias).
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    // Devuelve el precio de compra.
    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    // Cambia el precio de compra.
    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    // Devuelve el precio de venta.
    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    // Cambia el precio de venta.
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    // Devuelve la cantidad total del producto (suma que mantienen los movimientos).
    public Integer getCantidad() {
        return cantidad;
    }

    // Cambia la cantidad total del producto (suma que mantienen los movimientos).
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    // Devuelve el estado ('activo' o 'descontinuado').
    public String getEstado() {
        return estado;
    }

    // Cambia el estado ('activo' o 'descontinuado').
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Devuelve la fecha de creación asignada por MySQL.
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    // Cambia la fecha de creación asignada por MySQL.
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Devuelve el nombre del archivo de imagen del producto (null si no tiene).
    public String getImagen() {
        return imagen;
    }

    // Cambia el nombre del archivo de imagen del producto (null si no tiene).
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    // Devuelve el nombre de la categoría (dato auxiliar del JOIN).
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    // Cambia el nombre de la categoría (dato auxiliar del JOIN).
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    // Texto que muestran los JComboBox de productos (Inventario, Compras, Movimientos).
    @Override
    public String toString() {
        // Muestra solo el nombre del producto.
        return nombre;
    }

    // INSERT de un producto nuevo, incluida su imagen; true si se creó la fila.
    public static boolean insertProducto(Producto producto) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión la operación termina aquí.
            return false;
        }
        // Sentencia parametrizada con las ocho columnas editables.
        String query = "INSERT INTO productos (nombre, descripcion, id_categoria, "
                + "precio_compra, precio_venta, cantidad, estado, imagen) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, producto.getNombre());
            // Parámetro 2: descripción.
            ps.setString(2, producto.getDescripcion());
            // Parámetro 3: id de la categoría.
            ps.setInt(3, producto.getIdCategoria());
            // Parámetro 4: precio de compra exacto.
            ps.setBigDecimal(4, producto.getPrecioCompra());
            // Parámetro 5: precio de venta exacto.
            ps.setBigDecimal(5, producto.getPrecioVenta());
            // Parámetro 6: cantidad inicial.
            ps.setInt(6, producto.getCantidad());
            // Parámetro 7: estado; 'activo' por defecto si llega null.
            ps.setString(7, producto.getEstado() == null ? "activo" : producto.getEstado());
            // Parámetro 8: nombre del archivo de imagen o NULL.
            ps.setString(8, producto.getImagen());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al registrar el producto: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        }
    }

    // SELECT de todos los productos (sin nombre de categoría), para los combos de Inventario y Movimientos.
    public static ArrayList<Producto> getProductos() {
        // Lista resultado.
        ArrayList<Producto> lista = new ArrayList<>();
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Devuelve la lista (vacía si no hubo conexión o datos).
            return lista;
        }
        // Consulta ordenada por id_producto.
        String query = "SELECT id_producto, nombre, descripcion, id_categoria, precio_compra, "
                + "precio_venta, cantidad, estado, fecha_creacion, imagen "
                + "FROM productos ORDER BY id_producto";

        // Prepara y ejecuta el SELECT; try-with-resources cierra sentencia y ResultSet (la conexión sigue abierta).
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Recorre cada fila devuelta por el SELECT.
            while (rs.next()) {
                // Convierte la fila en Producto y la agrega.
                lista.add(mapResultSet(rs));
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al consultar productos: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT de productos con el nombre de su categoría, para la JTable de productos y Compras.
    public static ArrayList<Producto> getProductosConCategoria() {
        // Lista resultado.
        ArrayList<Producto> lista = new ArrayList<>();
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Devuelve la lista (vacía si no hubo conexión o datos).
            return lista;
        }
        // INNER JOIN con categorias para traer el nombre de la categoría.
        String query = "SELECT p.id_producto, p.nombre, p.descripcion, p.id_categoria, "
                + "p.precio_compra, p.precio_venta, p.cantidad, "
                + "p.estado, p.fecha_creacion, p.imagen, c.nombre AS nombre_categoria "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "ORDER BY p.id_producto";

        // Prepara y ejecuta el SELECT; try-with-resources cierra sentencia y ResultSet (la conexión sigue abierta).
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Recorre cada fila devuelta por el SELECT.
            while (rs.next()) {
                // Convierte las columnas propias del producto.
                Producto producto = mapResultSet(rs);
                // Agrega el nombre de la categoría obtenido del JOIN.
                producto.setNombreCategoria(rs.getString("nombre_categoria"));
                // Agrega el producto a la lista.
                lista.add(producto);
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al consultar productos: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT de un producto por id; null si no existe o hay error.
    public static Producto getProductoPorId(int idProducto) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión la operación termina aquí.
            return null;
        }
        // Consulta filtrada por la llave primaria.
        String query = "SELECT id_producto, nombre, descripcion, id_categoria, precio_compra, "
                + "precio_venta, cantidad, estado, fecha_creacion, imagen "
                + "FROM productos WHERE id_producto = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del producto.
            ps.setInt(1, idProducto);
            // Ejecuta la consulta; el ResultSet se cierra automáticamente al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta encontró el registro buscado.
                if (rs.next()) {
                    // Convierte la fila encontrada.
                    return mapResultSet(rs);
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al consultar el producto: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No se encontró el producto.
        return null;
    }

    // UPDATE de todos los datos editables del producto por id, incluida la imagen.
    public static boolean updateProducto(Producto producto) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión la operación termina aquí.
            return false;
        }
        // Sentencia parametrizada filtrada por id_producto.
        String query = "UPDATE productos SET nombre = ?, descripcion = ?, id_categoria = ?, "
                + "precio_compra = ?, precio_venta = ?, cantidad = ?, "
                + "estado = ?, imagen = ? WHERE id_producto = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, producto.getNombre());
            // Parámetro 2: descripción.
            ps.setString(2, producto.getDescripcion());
            // Parámetro 3: id de la categoría.
            ps.setInt(3, producto.getIdCategoria());
            // Parámetro 4: precio de compra.
            ps.setBigDecimal(4, producto.getPrecioCompra());
            // Parámetro 5: precio de venta.
            ps.setBigDecimal(5, producto.getPrecioVenta());
            // Parámetro 6: cantidad (sobrescribe la cantidad total con el valor del formulario).
            ps.setInt(6, producto.getCantidad());
            // Parámetro 7: estado.
            ps.setString(7, producto.getEstado());
            // Parámetro 8: nombre de la imagen o NULL si se quitó.
            ps.setString(8, producto.getImagen());
            // Parámetro 9: id de la fila a modificar.
            ps.setInt(9, producto.getIdProducto());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        }
    }

    // DELETE por id; MySQL lo rechaza si el producto tiene inventario, compras o movimientos.
    public static boolean deleteProducto(int idProducto) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión la operación termina aquí.
            return false;
        }
        // Sentencia de borrado por llave primaria.
        String query = "DELETE FROM productos WHERE id_producto = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id a eliminar.
            ps.setInt(1, idProducto);
            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error y sugiere la causa más frecuente (registros relacionados).
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto: " + ex.getMessage()
                    + ". Verifique que no tenga movimientos o compras asociadas.",
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        }
    }

    // Búsqueda con filtros opcionales para Busqueda_Productos_Form; los filtros null o vacíos
    // se omiten y todos los valores viajan como parámetros del PreparedStatement.
    public static ArrayList<Producto> buscarProductos(String texto, Integer idCategoria, String estado) {
        // Lista resultado.
        ArrayList<Producto> lista = new ArrayList<>();
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Devuelve la lista (vacía si no hubo conexión o datos).
            return lista;
        }
        // SELECT base con JOIN a categorias; 'WHERE 1 = 1' permite añadir cada filtro con AND.
        StringBuilder query = new StringBuilder("SELECT p.id_producto, p.nombre, p.descripcion, "
                + "p.id_categoria, p.precio_compra, p.precio_venta, p.cantidad, p.estado, "
                + "p.fecha_creacion, p.imagen, c.nombre AS nombre_categoria "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria WHERE 1 = 1");
        // Valores de los signos ? en el orden en que se agregan los filtros.
        ArrayList<Object> parametros = new ArrayList<>();
        // Filtro de texto libre, solo si se escribió algo.
        if (texto != null && !texto.trim().isEmpty()) {
            // Busca el texto en el nombre, la descripción o la categoría.
            query.append(" AND (p.nombre LIKE ? OR p.descripcion LIKE ? OR c.nombre LIKE ?)");
            // Patrón LIKE que encuentra el texto en cualquier posición.
            String patron = "%" + texto.trim() + "%";
            // Valor para p.nombre LIKE ?.
            parametros.add(patron);
            // Valor para p.descripcion LIKE ?.
            parametros.add(patron);
            // Valor para c.nombre LIKE ?.
            parametros.add(patron);
        }
        // Filtro por categoría, solo si se eligió una.
        if (idCategoria != null) {
            // Agrega la condición por id_categoria.
            query.append(" AND p.id_categoria = ?");
            // Valor del id de categoría.
            parametros.add(idCategoria);
        }
        // Filtro por estado, solo si no es 'Todos'.
        if (estado != null && !estado.trim().isEmpty()) {
            // Agrega la condición por estado.
            query.append(" AND p.estado = ?");
            // Valor del estado ('activo' o 'descontinuado').
            parametros.add(estado);
        }
        // Ordena los resultados alfabéticamente por nombre.
        query.append(" ORDER BY p.nombre");

        // Prepara la consulta armada; try-with-resources cierra la sentencia.
        try (PreparedStatement ps = con.prepareStatement(query.toString())) {
            // Asigna los parámetros acumulados en el mismo orden de los signos ?.
            for (int i = 0; i < parametros.size(); i++) {
                // setObject elige el tipo JDBC según el valor (Integer o String).
                ps.setObject(i + 1, parametros.get(i));
            }
            // Ejecuta la búsqueda; el ResultSet se cierra al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Recorre cada fila devuelta por el SELECT.
                while (rs.next()) {
                    // Convierte las columnas propias del producto.
                    Producto producto = mapResultSet(rs);
                    // Agrega el nombre de la categoría.
                    producto.setNombreCategoria(rs.getString("nombre_categoria"));
                    // Agrega el producto a los resultados.
                    lista.add(producto);
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al buscar productos: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // Convierte la fila actual del ResultSet en un objeto Producto.
    private static Producto mapResultSet(ResultSet rs) throws SQLException {
        // Crea el producto con las columnas principales leídas por nombre.
        Producto producto = new Producto(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getInt("id_categoria"),
                rs.getBigDecimal("precio_compra"),
                rs.getBigDecimal("precio_venta"),
                rs.getInt("cantidad"),
                rs.getString("estado"),
                rs.getTimestamp("fecha_creacion")
        );
        // Agrega el nombre del archivo de imagen (puede ser NULL).
        producto.setImagen(rs.getString("imagen"));
        // Devuelve el producto construido.
        return producto;
    }
}
