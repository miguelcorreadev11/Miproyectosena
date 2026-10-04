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

// Modelo de la tabla categorias; lo usan Categoria_control, Producto_control y los combos de productos.
public class Categoria {
    // Columna id_categoria (llave primaria AUTO_INCREMENT).
    private Integer idCategoria;
    // Columna nombre (única en la tabla, índice uk_categorias_nombre).
    private String nombre;
    // Columna descripcion (opcional).
    private String descripcion;
    // Columna estado: 'activo' o 'inactivo'.
    private String estado;
    // Columna fecha_creacion (la asigna MySQL con CURRENT_TIMESTAMP).
    private Timestamp fechaCreacion;

    // Constructor vacío.
    public Categoria() {
    }

    // Constructor completo, usado por mapResultSet al leer una fila de la tabla.
    public Categoria(Integer idCategoria, String nombre, String descripcion, String estado, Timestamp fechaCreacion) {
        // Asigna el id.
        this.idCategoria = idCategoria;
        // Asigna el nombre.
        this.nombre = nombre;
        // Asigna la descripción.
        this.descripcion = descripcion;
        // Asigna el estado.
        this.estado = estado;
        // Asigna la fecha de creación.
        this.fechaCreacion = fechaCreacion;
    }

    // Constructor para una categoría nueva (sin id ni fecha, que genera MySQL).
    public Categoria(String nombre, String descripcion, String estado) {
        // Asigna el nombre.
        this.nombre = nombre;
        // Asigna la descripción.
        this.descripcion = descripcion;
        // Asigna el estado elegido en el combo.
        this.estado = estado;
    }

    // Devuelve el id de la categoría.
    public Integer getIdCategoria() {
        return idCategoria;
    }

    // Cambia el id de la categoría.
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    // Devuelve el nombre de la categoría.
    public String getNombre() {
        return nombre;
    }

    // Cambia el nombre de la categoría.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve la descripción.
    public String getDescripcion() {
        return descripcion;
    }

    // Cambia la descripción.
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Devuelve el estado ('activo' o 'inactivo').
    public String getEstado() {
        return estado;
    }

    // Cambia el estado.
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Devuelve la fecha de creación.
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    // Cambia la fecha de creación.
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Texto que muestran los JComboBox de categorías (Productos, Búsqueda).
    @Override
    public String toString() {
        // Muestra solo el nombre de la categoría.
        return nombre;
    }

    // INSERT de una categoría nueva; devuelve true si se creó la fila.
    public static boolean insertCategoria(Categoria categoria) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no se puede insertar.
            return false;
        }
        // Sentencia parametrizada: nombre, descripción y estado.
        String query = "INSERT INTO categorias (nombre, descripcion, estado) VALUES (?, ?, ?)";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, categoria.getNombre());
            // Parámetro 2: descripción (puede ser null).
            ps.setString(2, categoria.getDescripcion());
            // Parámetro 3: estado; 'activo' por defecto si llega null.
            ps.setString(3, categoria.getEstado() == null ? "activo" : categoria.getEstado());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL (por ejemplo, nombre duplicado).
            JOptionPane.showMessageDialog(null, "Error al registrar la categoría: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se insertó.
            return false;
        }
    }

    // SELECT de todas las categorías ordenadas por nombre.
    public static ArrayList<Categoria> getCategorias() {
        // Lista resultado.
        ArrayList<Categoria> lista = new ArrayList<>();
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Devuelve la lista vacía.
            return lista;
        }
        // Columnas que necesita mapResultSet.
        String query = "SELECT id_categoria, nombre, descripcion, estado, fecha_creacion "
                + "FROM categorias ORDER BY nombre";

        // Prepara y ejecuta el SELECT; try-with-resources cierra sentencia y ResultSet (la conexión sigue abierta).
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Recorre cada fila devuelta por el SELECT.
            while (rs.next()) {
                // Convierte la fila en Categoria y la agrega.
                lista.add(mapResultSet(rs));
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar categorías: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT de una categoría por id; null si no existe o hay error.
    public static Categoria getCategoriaPorId(int idCategoria) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no hay resultado.
            return null;
        }
        // Consulta filtrada por la llave primaria.
        String query = "SELECT id_categoria, nombre, descripcion, estado, fecha_creacion "
                + "FROM categorias WHERE id_categoria = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id de la categoría.
            ps.setInt(1, idCategoria);
            // Ejecuta la consulta; el ResultSet se cierra automáticamente al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta encontró el registro buscado.
                if (rs.next()) {
                    // Convierte la fila encontrada en Categoria.
                    return mapResultSet(rs);
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar la categoría: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No se encontró la categoría.
        return null;
    }

    // true si la categoría existe; Producto_control lo usa antes de guardar un producto.
    public static boolean existeCategoria(int idCategoria) {
        // Reutiliza la consulta por id.
        return getCategoriaPorId(idCategoria) != null;
    }

    // UPDATE de nombre, descripción y estado por id; true si modificó la fila.
    public static boolean updateCategoria(Categoria categoria) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no se puede actualizar.
            return false;
        }
        // Sentencia parametrizada filtrada por id_categoria.
        String query = "UPDATE categorias SET nombre = ?, descripcion = ?, estado = ? WHERE id_categoria = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, categoria.getNombre());
            // Parámetro 2: descripción.
            ps.setString(2, categoria.getDescripcion());
            // Parámetro 3: estado.
            ps.setString(3, categoria.getEstado());
            // Parámetro 4: id de la fila a modificar.
            ps.setInt(4, categoria.getIdCategoria());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL (por ejemplo, nombre duplicado).
            JOptionPane.showMessageDialog(null, "Error al actualizar la categoría: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se actualizó.
            return false;
        }
    }

    // DELETE por id; MySQL lo rechaza si hay productos con esa categoría (fk_productos_categoria).
    public static boolean deleteCategoria(int idCategoria) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no se puede eliminar.
            return false;
        }
        // Sentencia de borrado por llave primaria.
        String query = "DELETE FROM categorias WHERE id_categoria = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id a eliminar.
            ps.setInt(1, idCategoria);
            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error; normalmente la categoría tiene productos asociados.
            JOptionPane.showMessageDialog(null, "Error al eliminar la categoría: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se eliminó.
            return false;
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Categoria.
    private static Categoria mapResultSet(ResultSet rs) throws SQLException {
        // Lee cada columna por su nombre y llama al constructor completo.
        return new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("estado"),
                rs.getTimestamp("fecha_creacion")
        );
    }
}
