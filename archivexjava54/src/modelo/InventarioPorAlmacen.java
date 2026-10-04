// Paquete modelo: clases que representan tablas de MySQL y ejecutan su SQL con JDBC.
package modelo;

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

// Modelo de la tabla inventarioporalmacen: existencias de cada producto en cada almacén.
// El par (id_producto, id_almacen) es único; MovimientoInventario también actualiza esta tabla.
public class InventarioPorAlmacen {
    // Columna id_inventario (llave primaria AUTO_INCREMENT).
    private Integer idInventario;
    // Columna id_producto.
    private Integer idProducto;
    // Columna id_almacen.
    private Integer idAlmacen;
    // Columna cantidad_disponible (UNSIGNED).
    private Integer cantidadDisponible;
    // Columna cantidad_reservada (UNSIGNED).
    private Integer cantidadReservada;
    // Columna fecha_de_actualizacion (MySQL la actualiza con ON UPDATE CURRENT_TIMESTAMP).
    private Timestamp fechaDeActualizacion;

    // Campo auxiliar: nombre del producto, llenado solo en getInventarioDetallado().
    private String nombreProducto;
    // Campo auxiliar: nombre del almacén, llenado solo en getInventarioDetallado().
    private String nombreAlmacen;

    // Constructor vacío.
    public InventarioPorAlmacen() {
    }

    // Constructor completo, usado por mapResultSet al leer una fila.
    public InventarioPorAlmacen(Integer idInventario, Integer idProducto, Integer idAlmacen,
            Integer cantidadDisponible, Integer cantidadReservada, Timestamp fechaDeActualizacion) {
        // Asigna el id del registro de inventario (columna id_inventario).
        this.idInventario = idInventario;
        // Asigna el id del producto (llave foránea a productos).
        this.idProducto = idProducto;
        // Asigna el id del almacén (llave foránea a almacenes).
        this.idAlmacen = idAlmacen;
        // Asigna las unidades disponibles en el almacén.
        this.cantidadDisponible = cantidadDisponible;
        // Asigna las unidades reservadas en el almacén.
        this.cantidadReservada = cantidadReservada;
        // Asigna la fecha de la última actualización del registro.
        this.fechaDeActualizacion = fechaDeActualizacion;
    }

    // Constructor para un registro nuevo (sin id ni fecha).
    public InventarioPorAlmacen(Integer idProducto, Integer idAlmacen, Integer cantidadDisponible,
            Integer cantidadReservada) {
        // Asigna el id del producto (llave foránea a productos).
        this.idProducto = idProducto;
        // Asigna el id del almacén (llave foránea a almacenes).
        this.idAlmacen = idAlmacen;
        // Asigna las unidades disponibles en el almacén.
        this.cantidadDisponible = cantidadDisponible;
        // Asigna las unidades reservadas en el almacén.
        this.cantidadReservada = cantidadReservada;
    }

    // Devuelve el id del registro de inventario (columna id_inventario).
    public Integer getIdInventario() {
        return idInventario;
    }

    // Cambia el id del registro de inventario (columna id_inventario).
    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    // Devuelve el id del producto (llave foránea a productos).
    public Integer getIdProducto() {
        return idProducto;
    }

    // Cambia el id del producto (llave foránea a productos).
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    // Devuelve el id del almacén (llave foránea a almacenes).
    public Integer getIdAlmacen() {
        return idAlmacen;
    }

    // Cambia el id del almacén (llave foránea a almacenes).
    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    // Devuelve las unidades disponibles en el almacén.
    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    // Cambia las unidades disponibles en el almacén.
    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    // Devuelve las unidades reservadas en el almacén.
    public Integer getCantidadReservada() {
        return cantidadReservada;
    }

    // Cambia las unidades reservadas en el almacén.
    public void setCantidadReservada(Integer cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }

    // Devuelve la fecha de la última actualización del registro.
    public Timestamp getFechaDeActualizacion() {
        return fechaDeActualizacion;
    }

    // Cambia la fecha de la última actualización del registro.
    public void setFechaDeActualizacion(Timestamp fechaDeActualizacion) {
        this.fechaDeActualizacion = fechaDeActualizacion;
    }

    // Devuelve el nombre del producto (dato auxiliar del JOIN).
    public String getNombreProducto() {
        return nombreProducto;
    }

    // Cambia el nombre del producto (dato auxiliar del JOIN).
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    // Devuelve el nombre del almacén (dato auxiliar del JOIN).
    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    // Cambia el nombre del almacén (dato auxiliar del JOIN).
    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    // INSERT del inventario de un producto en un almacén; true si se creó la fila.
    public static boolean insertInventario(InventarioPorAlmacen inventario) {
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
        // Sentencia parametrizada: producto, almacén y cantidades.
        String query = "INSERT INTO inventarioporalmacen (id_producto, id_almacen, cantidad_disponible, "
                + "cantidad_reservada) VALUES (?, ?, ?, ?)";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del producto.
            ps.setInt(1, inventario.getIdProducto());
            // Parámetro 2: id del almacén.
            ps.setInt(2, inventario.getIdAlmacen());
            // Parámetro 3: cantidad disponible.
            ps.setInt(3, inventario.getCantidadDisponible());
            // Parámetro 4: cantidad reservada.
            ps.setInt(4, inventario.getCantidadReservada());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(InventarioPorAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al registrar el inventario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        }
    }

    // SELECT de todos los registros de inventario (solo ids, sin nombres).
    public static ArrayList<InventarioPorAlmacen> getInventarios() {
        // Lista resultado.
        ArrayList<InventarioPorAlmacen> lista = new ArrayList<>();
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
        // Consulta ordenada por id_inventario.
        String query = "SELECT id_inventario, id_producto, id_almacen, cantidad_disponible, "
                + "cantidad_reservada, fecha_de_actualizacion FROM inventarioporalmacen ORDER BY id_inventario";

        // Prepara y ejecuta el SELECT; try-with-resources cierra sentencia y ResultSet (la conexión sigue abierta).
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Recorre cada fila devuelta por el SELECT.
            while (rs.next()) {
                // Convierte la fila y la agrega.
                lista.add(mapResultSet(rs));
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(InventarioPorAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al consultar el inventario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT del inventario con nombres de producto y almacén, para las JTable de las vistas.
    public static ArrayList<InventarioPorAlmacen> getInventarioDetallado() {
        // Lista resultado.
        ArrayList<InventarioPorAlmacen> lista = new ArrayList<>();
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
        // INNER JOIN con productos y almacenes para mostrar nombres en lugar de ids.
        String query = "SELECT i.id_inventario, i.id_producto, i.id_almacen, i.cantidad_disponible, "
                + "i.cantidad_reservada, i.fecha_de_actualizacion, p.nombre AS nombre_producto, "
                + "a.nombre AS nombre_almacen "
                + "FROM inventarioporalmacen i "
                + "INNER JOIN productos p ON i.id_producto = p.id_producto "
                + "INNER JOIN almacenes a ON i.id_almacen = a.id_almacen "
                + "ORDER BY i.id_inventario";

        // Prepara y ejecuta el SELECT; try-with-resources cierra sentencia y ResultSet (la conexión sigue abierta).
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Recorre cada fila devuelta por el SELECT.
            while (rs.next()) {
                // Convierte las columnas propias de la tabla.
                InventarioPorAlmacen inventario = mapResultSet(rs);
                // Agrega el nombre del producto obtenido del JOIN.
                inventario.setNombreProducto(rs.getString("nombre_producto"));
                // Agrega el nombre del almacén obtenido del JOIN.
                inventario.setNombreAlmacen(rs.getString("nombre_almacen"));
                // Agrega el registro completo a la lista.
                lista.add(inventario);
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(InventarioPorAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al consultar el inventario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT de un registro de inventario por id; null si no existe.
    public static InventarioPorAlmacen getInventarioPorId(int idInventario) {
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
        String query = "SELECT id_inventario, id_producto, id_almacen, cantidad_disponible, "
                + "cantidad_reservada, fecha_de_actualizacion FROM inventarioporalmacen WHERE id_inventario = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del inventario.
            ps.setInt(1, idInventario);
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
            Logger.getLogger(InventarioPorAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al consultar el inventario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No se encontró el registro.
        return null;
    }

    // Busca el registro del par producto-almacén; null si ese producto aún no tiene inventario allí.
    public static InventarioPorAlmacen getInventarioPorProductoYAlmacen(int idProducto, int idAlmacen) {
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
        // Consulta por el índice único uk_producto_almacen.
        String query = "SELECT id_inventario, id_producto, id_almacen, cantidad_disponible, "
                + "cantidad_reservada, fecha_de_actualizacion FROM inventarioporalmacen "
                + "WHERE id_producto = ? AND id_almacen = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del producto.
            ps.setInt(1, idProducto);
            // Parámetro 2: id del almacén.
            ps.setInt(2, idAlmacen);
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
            Logger.getLogger(InventarioPorAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al consultar el inventario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No existe inventario para ese par.
        return null;
    }

    // true si ya hay inventario para ese producto en ese almacén.
    public static boolean existeProductoAlmacen(int idProducto, int idAlmacen) {
        // Reutiliza la búsqueda por producto y almacén.
        return getInventarioPorProductoYAlmacen(idProducto, idAlmacen) != null;
    }

    // UPDATE directo de producto, almacén y cantidades por id (sin movimiento ni historial).
    public static boolean updateInventario(InventarioPorAlmacen inventario) {
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
        // Sentencia parametrizada filtrada por id_inventario.
        String query = "UPDATE inventarioporalmacen SET id_producto = ?, id_almacen = ?, "
                + "cantidad_disponible = ?, cantidad_reservada = ? WHERE id_inventario = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del producto.
            ps.setInt(1, inventario.getIdProducto());
            // Parámetro 2: id del almacén.
            ps.setInt(2, inventario.getIdAlmacen());
            // Parámetro 3: cantidad disponible.
            ps.setInt(3, inventario.getCantidadDisponible());
            // Parámetro 4: cantidad reservada.
            ps.setInt(4, inventario.getCantidadReservada());
            // Parámetro 5: id de la fila a modificar.
            ps.setInt(5, inventario.getIdInventario());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(InventarioPorAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al actualizar el inventario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        }
    }

    // DELETE del registro de inventario por id.
    public static boolean deleteInventario(int idInventario) {
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
        String query = "DELETE FROM inventarioporalmacen WHERE id_inventario = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id a eliminar.
            ps.setInt(1, idInventario);
            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(InventarioPorAlmacen.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario el mensaje de error devuelto por MySQL.
            JOptionPane.showMessageDialog(null, "Error al eliminar el inventario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        }
    }

    // Convierte la fila actual del ResultSet en un objeto InventarioPorAlmacen.
    private static InventarioPorAlmacen mapResultSet(ResultSet rs) throws SQLException {
        // Lee cada columna por su nombre y llama al constructor completo.
        return new InventarioPorAlmacen(
                rs.getInt("id_inventario"),
                rs.getInt("id_producto"),
                rs.getInt("id_almacen"),
                rs.getInt("cantidad_disponible"),
                rs.getInt("cantidad_reservada"),
                rs.getTimestamp("fecha_de_actualizacion")
        );
    }
}
