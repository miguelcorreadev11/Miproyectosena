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

// Modelo de la tabla almacenes; lo usan Catálogos, Inventario, Compras y Movimientos.
public class Almacen {
    // Columna id_almacen (llave primaria AUTO_INCREMENT).
    private Integer idAlmacen;
    // Columna nombre (única, índice uk_almacenes_nombre).
    private String nombre;
    // Columna ubicacion (obligatoria).
    private String ubicacion;
    // Columna ciudad (opcional).
    private String ciudad;
    // Columna capacidad_maxima (opcional, UNSIGNED); null si no se indicó.
    private Integer capacidadMaxima;
    // Columna estado: 'activo' o 'inactivo'.
    private String estado;
    // Columna fecha_creacion (la asigna MySQL).
    private Timestamp fechaCreacion;

    // Constructor vacío.
    public Almacen() {
    }

    // Constructor completo, usado por mapResultSet al leer una fila.
    public Almacen(Integer idAlmacen, String nombre, String ubicacion, String ciudad,
            Integer capacidadMaxima, String estado, Timestamp fechaCreacion) {
        // Asigna el id.
        this.idAlmacen = idAlmacen;
        // Asigna el nombre.
        this.nombre = nombre;
        // Asigna la ubicación.
        this.ubicacion = ubicacion;
        // Asigna la ciudad.
        this.ciudad = ciudad;
        // Asigna la capacidad máxima.
        this.capacidadMaxima = capacidadMaxima;
        // Asigna el estado.
        this.estado = estado;
        // Asigna la fecha de creación.
        this.fechaCreacion = fechaCreacion;
    }

    // Constructor para un almacén nuevo (sin id ni fecha, que genera MySQL).
    public Almacen(String nombre, String ubicacion, String ciudad, Integer capacidadMaxima, String estado) {
        // Asigna el nombre.
        this.nombre = nombre;
        // Asigna la ubicación.
        this.ubicacion = ubicacion;
        // Asigna la ciudad.
        this.ciudad = ciudad;
        // Asigna la capacidad máxima (puede ser null).
        this.capacidadMaxima = capacidadMaxima;
        // Asigna el estado elegido en el combo.
        this.estado = estado;
    }

    // Devuelve el id del almacén.
    public Integer getIdAlmacen() {
        return idAlmacen;
    }

    // Cambia el id del almacén.
    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    // Devuelve el nombre del almacén.
    public String getNombre() {
        return nombre;
    }

    // Cambia el nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve la ubicación.
    public String getUbicacion() {
        return ubicacion;
    }

    // Cambia la ubicación.
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    // Devuelve la ciudad.
    public String getCiudad() {
        return ciudad;
    }

    // Cambia la ciudad.
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // Devuelve la capacidad máxima o null.
    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    // Cambia la capacidad máxima.
    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
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

    // Texto que muestran los JComboBox de almacenes (Inventario, Compras, Movimientos).
    @Override
    public String toString() {
        // Muestra solo el nombre del almacén.
        return nombre;
    }

    // INSERT de un almacén nuevo; devuelve true si se creó la fila.
    public static boolean insertAlmacen(Almacen almacen) {
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
        // Sentencia parametrizada con las cinco columnas editables.
        String query = "INSERT INTO almacenes (nombre, ubicacion, ciudad, capacidad_maxima, estado) "
                + "VALUES (?, ?, ?, ?, ?)";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, almacen.getNombre());
            // Parámetro 2: ubicación.
            ps.setString(2, almacen.getUbicacion());
            // Parámetro 3: ciudad (puede ser null).
            ps.setString(3, almacen.getCiudad());
            // La capacidad es opcional.
            if (almacen.getCapacidadMaxima() != null) {
                // Parámetro 4: capacidad indicada.
                ps.setInt(4, almacen.getCapacidadMaxima());
            } else {
                // Parámetro 4: NULL cuando no se indicó capacidad.
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            // Parámetro 5: estado; 'activo' por defecto si llega null.
            ps.setString(5, almacen.getEstado() == null ? "activo" : almacen.getEstado());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL (por ejemplo, nombre duplicado o capacidad negativa).
            JOptionPane.showMessageDialog(null, "Error al registrar el almacén: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se insertó.
            return false;
        }
    }

    // SELECT de todos los almacenes ordenados por nombre.
    public static ArrayList<Almacen> getAlmacenes() {
        // Lista resultado.
        ArrayList<Almacen> lista = new ArrayList<>();
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
        String query = "SELECT id_almacen, nombre, ubicacion, ciudad, capacidad_maxima, estado, "
                + "fecha_creacion FROM almacenes ORDER BY nombre";

        // Prepara y ejecuta el SELECT; try-with-resources cierra sentencia y ResultSet (la conexión sigue abierta).
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Recorre cada fila devuelta por el SELECT.
            while (rs.next()) {
                // Convierte la fila en Almacen y la agrega.
                lista.add(mapResultSet(rs));
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar almacenes: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT de un almacén por id; null si no existe o hay error.
    public static Almacen getAlmacenPorId(int idAlmacen) {
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
        String query = "SELECT id_almacen, nombre, ubicacion, ciudad, capacidad_maxima, estado, "
                + "fecha_creacion FROM almacenes WHERE id_almacen = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del almacén.
            ps.setInt(1, idAlmacen);
            // Ejecuta la consulta; el ResultSet se cierra automáticamente al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta encontró el registro buscado.
                if (rs.next()) {
                    // Convierte la fila encontrada en Almacen.
                    return mapResultSet(rs);
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar el almacén: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No se encontró el almacén.
        return null;
    }

    // true si el almacén existe.
    public static boolean existeAlmacen(int idAlmacen) {
        // Reutiliza la consulta por id.
        return getAlmacenPorId(idAlmacen) != null;
    }

    // UPDATE de los datos del almacén por id; true si modificó la fila.
    public static boolean updateAlmacen(Almacen almacen) {
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
        // Sentencia parametrizada filtrada por id_almacen.
        String query = "UPDATE almacenes SET nombre = ?, ubicacion = ?, ciudad = ?, "
                + "capacidad_maxima = ?, estado = ? WHERE id_almacen = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, almacen.getNombre());
            // Parámetro 2: ubicación.
            ps.setString(2, almacen.getUbicacion());
            // Parámetro 3: ciudad.
            ps.setString(3, almacen.getCiudad());
            // La capacidad es opcional.
            if (almacen.getCapacidadMaxima() != null) {
                // Parámetro 4: capacidad indicada.
                ps.setInt(4, almacen.getCapacidadMaxima());
            } else {
                // Parámetro 4: NULL cuando se borró la capacidad.
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            // Parámetro 5: estado.
            ps.setString(5, almacen.getEstado());
            // Parámetro 6: id de la fila a modificar.
            ps.setInt(6, almacen.getIdAlmacen());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL.
            JOptionPane.showMessageDialog(null, "Error al actualizar el almacén: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se actualizó.
            return false;
        }
    }

    // DELETE por id; MySQL lo rechaza si hay inventario, compras o movimientos de este almacén.
    public static boolean deleteAlmacen(int idAlmacen) {
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
        String query = "DELETE FROM almacenes WHERE id_almacen = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id a eliminar.
            ps.setInt(1, idAlmacen);
            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Almacen.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error; normalmente por registros relacionados (llaves foráneas RESTRICT).
            JOptionPane.showMessageDialog(null, "Error al eliminar el almacén: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se eliminó.
            return false;
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Almacen.
    private static Almacen mapResultSet(ResultSet rs) throws SQLException {
        // getInt devuelve 0 cuando la columna es NULL.
        int capacidad = rs.getInt("capacidad_maxima");
        // wasNull() distingue un NULL real de un 0 para conservar 'sin capacidad'.
        Integer capacidadMaxima = rs.wasNull() ? null : capacidad;
        // Lee el resto de columnas por nombre y llama al constructor completo.
        return new Almacen(
                rs.getInt("id_almacen"),
                rs.getString("nombre"),
                rs.getString("ubicacion"),
                rs.getString("ciudad"),
                capacidadMaxima,
                rs.getString("estado"),
                rs.getTimestamp("fecha_creacion")
        );
    }
}
