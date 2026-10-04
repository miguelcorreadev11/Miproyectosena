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

// Modelo de la tabla proveedores; lo usan Catálogos (CRUD) y Compras (proveedor del pedido).
public class Proveedor {
    // Columna id_proveedor (llave primaria AUTO_INCREMENT).
    private Integer idProveedor;
    // Columna nombre (obligatoria).
    private String nombre;
    // Columna correo (opcional).
    private String correo;
    // Columna telefono VARCHAR(20); se maneja como texto.
    private String telefono;
    // Columna direccion (opcional).
    private String direccion;
    // Columna ciudad (opcional).
    private String ciudad;
    // Columna pais (opcional).
    private String pais;
    // Columna estado: 'activo', 'inactivo' o 'suspendido'.
    private String estado;
    // Columna fecha_registro (la asigna MySQL).
    private Timestamp fechaRegistro;

    // Constructor vacío.
    public Proveedor() {
    }

    // Constructor completo, usado por mapResultSet al leer una fila.
    public Proveedor(Integer idProveedor, String nombre, String correo, String telefono, String direccion,
            String ciudad, String pais, String estado, Timestamp fechaRegistro) {
        // Asigna el id del proveedor (columna id_proveedor).
        this.idProveedor = idProveedor;
        // Asigna el nombre o razón social del proveedor.
        this.nombre = nombre;
        // Asigna el correo de contacto.
        this.correo = correo;
        // Asigna el teléfono (texto; puede incluir +57 o paréntesis).
        this.telefono = telefono;
        // Asigna la dirección.
        this.direccion = direccion;
        // Asigna la ciudad.
        this.ciudad = ciudad;
        // Asigna el país.
        this.pais = pais;
        // Asigna el estado ('activo', 'inactivo' o 'suspendido').
        this.estado = estado;
        // Asigna la fecha de registro asignada por MySQL.
        this.fechaRegistro = fechaRegistro;
    }

    // Constructor para un proveedor nuevo (sin id ni fecha, que genera MySQL).
    public Proveedor(String nombre, String correo, String telefono, String direccion, String ciudad,
            String pais, String estado) {
        // Asigna el nombre o razón social del proveedor.
        this.nombre = nombre;
        // Asigna el correo de contacto.
        this.correo = correo;
        // Asigna el teléfono (texto; puede incluir +57 o paréntesis).
        this.telefono = telefono;
        // Asigna la dirección.
        this.direccion = direccion;
        // Asigna la ciudad.
        this.ciudad = ciudad;
        // Asigna el país.
        this.pais = pais;
        // Asigna el estado ('activo', 'inactivo' o 'suspendido').
        this.estado = estado;
    }

    // Devuelve el id del proveedor (columna id_proveedor).
    public Integer getIdProveedor() {
        return idProveedor;
    }

    // Cambia el id del proveedor (columna id_proveedor).
    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    // Devuelve el nombre o razón social del proveedor.
    public String getNombre() {
        return nombre;
    }

    // Cambia el nombre o razón social del proveedor.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve el correo de contacto.
    public String getCorreo() {
        return correo;
    }

    // Cambia el correo de contacto.
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Devuelve el teléfono (texto; puede incluir +57 o paréntesis).
    public String getTelefono() {
        return telefono;
    }

    // Cambia el teléfono (texto; puede incluir +57 o paréntesis).
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Devuelve la dirección.
    public String getDireccion() {
        return direccion;
    }

    // Cambia la dirección.
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Devuelve la ciudad.
    public String getCiudad() {
        return ciudad;
    }

    // Cambia la ciudad.
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // Devuelve el país.
    public String getPais() {
        return pais;
    }

    // Cambia el país.
    public void setPais(String pais) {
        this.pais = pais;
    }

    // Devuelve el estado ('activo', 'inactivo' o 'suspendido').
    public String getEstado() {
        return estado;
    }

    // Cambia el estado ('activo', 'inactivo' o 'suspendido').
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Devuelve la fecha de registro asignada por MySQL.
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    // Cambia la fecha de registro asignada por MySQL.
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Texto que muestra jComboBoxProveedor en Compras_Form.
    @Override
    public String toString() {
        // Muestra solo el nombre del proveedor.
        return nombre;
    }

    // INSERT de un proveedor nuevo; devuelve true si se creó la fila.
    public static boolean insertProveedor(Proveedor proveedor) {
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
        // Sentencia parametrizada con las siete columnas editables.
        String query = "INSERT INTO proveedores (nombre, correo, telefono, direccion, ciudad, pais, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, proveedor.getNombre());
            // Parámetro 2: correo (puede ser null).
            ps.setString(2, proveedor.getCorreo());
            // Parámetro 3: teléfono (null si se dejó vacío).
            ps.setString(3, proveedor.getTelefono());
            // Parámetro 4: dirección.
            ps.setString(4, proveedor.getDireccion());
            // Parámetro 5: ciudad.
            ps.setString(5, proveedor.getCiudad());
            // Parámetro 6: país.
            ps.setString(6, proveedor.getPais());
            // Parámetro 7: estado; 'activo' por defecto si llega null.
            ps.setString(7, proveedor.getEstado() == null ? "activo" : proveedor.getEstado());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Proveedor.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL.
            JOptionPane.showMessageDialog(null, "Error al registrar el proveedor: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se insertó.
            return false;
        }
    }

    // SELECT de todos los proveedores ordenados por nombre.
    public static ArrayList<Proveedor> getProveedores() {
        // Lista resultado.
        ArrayList<Proveedor> lista = new ArrayList<>();
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
        String query = "SELECT id_proveedor, nombre, correo, telefono, direccion, ciudad, pais, estado, "
                + "fecha_registro FROM proveedores ORDER BY nombre";

        // Prepara y ejecuta el SELECT; try-with-resources cierra sentencia y ResultSet (la conexión sigue abierta).
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Recorre cada fila devuelta por el SELECT.
            while (rs.next()) {
                // Convierte la fila en Proveedor y la agrega.
                lista.add(mapResultSet(rs));
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Proveedor.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar proveedores: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT de un proveedor por id; null si no existe o hay error.
    public static Proveedor getProveedorPorId(int idProveedor) {
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
        String query = "SELECT id_proveedor, nombre, correo, telefono, direccion, ciudad, pais, estado, "
                + "fecha_registro FROM proveedores WHERE id_proveedor = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del proveedor.
            ps.setInt(1, idProveedor);
            // Ejecuta la consulta; el ResultSet se cierra automáticamente al salir del bloque.
            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta encontró el registro buscado.
                if (rs.next()) {
                    // Convierte la fila encontrada en Proveedor.
                    return mapResultSet(rs);
                }
            }
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Proveedor.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar el proveedor: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No se encontró el proveedor.
        return null;
    }

    // true si el proveedor existe.
    public static boolean existeProveedor(int idProveedor) {
        // Reutiliza la consulta por id.
        return getProveedorPorId(idProveedor) != null;
    }

    // UPDATE de los datos del proveedor por id; true si modificó la fila.
    public static boolean updateProveedor(Proveedor proveedor) {
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
        // Sentencia parametrizada filtrada por id_proveedor.
        String query = "UPDATE proveedores SET nombre = ?, correo = ?, telefono = ?, direccion = ?, "
                + "ciudad = ?, pais = ?, estado = ? WHERE id_proveedor = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: nombre.
            ps.setString(1, proveedor.getNombre());
            // Parámetro 2: correo.
            ps.setString(2, proveedor.getCorreo());
            // Parámetro 3: teléfono.
            ps.setString(3, proveedor.getTelefono());
            // Parámetro 4: dirección.
            ps.setString(4, proveedor.getDireccion());
            // Parámetro 5: ciudad.
            ps.setString(5, proveedor.getCiudad());
            // Parámetro 6: país.
            ps.setString(6, proveedor.getPais());
            // Parámetro 7: estado.
            ps.setString(7, proveedor.getEstado());
            // Parámetro 8: id de la fila a modificar.
            ps.setInt(8, proveedor.getIdProveedor());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Proveedor.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL.
            JOptionPane.showMessageDialog(null, "Error al actualizar el proveedor: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se actualizó.
            return false;
        }
    }

    // DELETE por id; MySQL lo rechaza si el proveedor tiene compras (fk_compras_proveedor).
    public static boolean deleteProveedor(int idProveedor) {
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
        String query = "DELETE FROM proveedores WHERE id_proveedor = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id a eliminar.
            ps.setInt(1, idProveedor);
            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Proveedor.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error; normalmente por compras asociadas.
            JOptionPane.showMessageDialog(null, "Error al eliminar el proveedor: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // No se eliminó.
            return false;
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Proveedor.
    private static Proveedor mapResultSet(ResultSet rs) throws SQLException {
        // Lee cada columna por su nombre y llama al constructor completo.
        return new Proveedor(
                rs.getInt("id_proveedor"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("direccion"),
                rs.getString("ciudad"),
                rs.getString("pais"),
                rs.getString("estado"),
                rs.getTimestamp("fecha_registro")
        );
    }
}
