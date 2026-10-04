// Paquete modelo: clases que representan tablas de MySQL y ejecutan su SQL con JDBC.
package modelo;

// BigInteger convierte los bytes del hash SHA-256 en texto hexadecimal.
import java.math.BigInteger;
// MessageDigest calcula el hash SHA-256 de las contraseñas.
import java.security.MessageDigest;
// Connection: conexión JDBC compartida que entrega config.conexión_BD.
import java.sql.Connection;
// PreparedStatement: sentencias SQL parametrizadas (evitan inyección SQL).
import java.sql.PreparedStatement;
// ResultSet: filas devueltas por los SELECT.
import java.sql.ResultSet;
// SQLException: errores reportados por MySQL o por el driver JDBC.
import java.sql.SQLException;
// Statement.RETURN_GENERATED_KEYS permite leer el id AUTO_INCREMENT insertado.
import java.sql.Statement;
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

// Modelo de la tabla usuarios. Cada registro, actualización, cambio de contraseña o de estado
// queda guardado en historialusuarios dentro de la misma transacción.
public class Usuario {
    // Columna id_usuario (llave primaria AUTO_INCREMENT).
    private Integer idUsuario;
    // Columna nombre.
    private String nombre;
    // Columna apellido.
    private String apellido;
    // Columna correo_electronico (única, índice uk_usuarios_correo).
    private String correoElectronico;
    // Columna contrasena: hash SHA-256 de 64 caracteres, nunca texto plano.
    private String contrasena;
    // Columna telefono (opcional).
    private String telefono;
    // Columna fecha_creacion (la asigna MySQL).
    private Timestamp fechaCreacion;

    // Columna estado: texto del ENUM 'activo' o 'inactivo'.
    private String estado;
    // Columna rol: ENUM 'usuario' o 'administrador' (se agrega con sql/agregar_rol_usuarios.sql).
    private String rol;

    // Valor 'usuario' del ENUM rol: rol por defecto de toda cuenta nueva.
    public static final String ROL_USUARIO = "usuario";
    // Valor 'administrador' del ENUM rol.
    public static final String ROL_ADMINISTRADOR = "administrador";

    // Acción 'registro' del ENUM historialusuarios.accion: alta de un usuario.
    public static final String ACCION_REGISTRO = "registro";
    // Acción 'actualizacion': cambió el nombre o varios datos a la vez.
    public static final String ACCION_ACTUALIZACION = "actualizacion";
    // Acción 'cambio_correo': solo cambió el correo.
    public static final String ACCION_CAMBIO_CORREO = "cambio_correo";
    // Acción 'cambio_telefono': solo cambió el teléfono.
    public static final String ACCION_CAMBIO_TELEFONO = "cambio_telefono";
    // Acción 'cambio_contrasena': se cambió la contraseña.
    public static final String ACCION_CAMBIO_CONTRASENA = "cambio_contrasena";
    // Acción 'activacion': el usuario pasó a activo.
    public static final String ACCION_ACTIVACION = "activacion";
    // Acción 'desactivacion': el usuario pasó a inactivo.
    public static final String ACCION_DESACTIVACION = "desactivacion";

    // Constructor vacío.
    public Usuario() {
    }

    // Constructor completo, usado por mapResultSet al leer una fila.
    public Usuario(Integer idUsuario, String nombre, String apellido, String correoElectronico,
            String contrasena, String telefono, Timestamp fechaCreacion, String estado) {
        // Asigna el id del usuario (columna id_usuario).
        this.idUsuario = idUsuario;
        // Asigna el nombre del usuario.
        this.nombre = nombre;
        // Asigna el apellido del usuario.
        this.apellido = apellido;
        // Asigna el correo, que también es el usuario de inicio de sesión.
        this.correoElectronico = correoElectronico;
        // Asigna el hash SHA-256 de la contraseña.
        this.contrasena = contrasena;
        // Asigna el teléfono del usuario.
        this.telefono = telefono;
        // Asigna la fecha de creación asignada por MySQL.
        this.fechaCreacion = fechaCreacion;
        // Asigna el estado leído de la tabla.
        this.estado = estado;
    }

    // Constructor para registrar un usuario nuevo (sin id ni fecha).
    public Usuario(String nombre, String apellido, String correoElectronico, String contrasena, String telefono) {
        // Asigna el nombre del usuario.
        this.nombre = nombre;
        // Asigna el apellido del usuario.
        this.apellido = apellido;
        // Asigna el correo, que también es el usuario de inicio de sesión.
        this.correoElectronico = correoElectronico;
        // Asigna el hash SHA-256 de la contraseña.
        this.contrasena = contrasena;
        // Asigna el teléfono del usuario.
        this.telefono = telefono;
        // Todo usuario nuevo empieza con estado 'activo'.
        this.estado = "activo";
        // Si no se indica otro, el usuario nuevo queda con rol 'usuario'.
        this.rol = ROL_USUARIO;
    }

    // Devuelve el id del usuario (columna id_usuario).
    public Integer getIdUsuario() {
        return idUsuario;
    }

    // Cambia el id del usuario (columna id_usuario).
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Devuelve el nombre del usuario.
    public String getNombre() {
        return nombre;
    }

    // Cambia el nombre del usuario.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve el apellido del usuario.
    public String getApellido() {
        return apellido;
    }

    // Cambia el apellido del usuario.
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // Devuelve el correo, que también es el usuario de inicio de sesión.
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    // Cambia el correo, que también es el usuario de inicio de sesión.
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    // Devuelve el hash SHA-256 de la contraseña.
    public String getContrasena() {
        return contrasena;
    }

    // Cambia el hash SHA-256 de la contraseña.
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    // Devuelve el teléfono del usuario.
    public String getTelefono() {
        return telefono;
    }

    // Cambia el teléfono del usuario.
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Devuelve la fecha de creación asignada por MySQL.
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    // Cambia la fecha de creación asignada por MySQL.
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Devuelve el texto del ENUM estado ('activo' o 'inactivo').
    public String getEstado() {
        return estado;
    }

    // Cambia el estado usando el texto del ENUM.
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Versión booleana del estado; Login_control la usa para rechazar usuarios inactivos.
    public boolean isEstado() {
        // true solo si el estado es 'activo' (sin distinguir mayúsculas).
        return "activo".equalsIgnoreCase(estado);
    }

    // Cambia el estado a partir del combo Activo/Inactivo de Usuarios_Form.
    public void setEstado(boolean estadoActivo) {
        // true se guarda como 'activo' y false como 'inactivo'.
        this.estado = estadoActivo ? "activo" : "inactivo";
    }

    // Devuelve el rol del usuario ('usuario' o 'administrador').
    public String getRol() {
        return rol;
    }

    // Cambia el rol del usuario; Register_Form y Usuarios_Form lo eligen en un combo.
    public void setRol(String rol) {
        this.rol = rol;
    }

    // Texto del rol para mostrar en tablas y combos: 'Usuario' o 'Administrador'.
    public static String textoRol(String rol) {
        // Cualquier valor distinto de 'administrador' (incluido null) se muestra como Usuario.
        return ROL_ADMINISTRADOR.equalsIgnoreCase(rol) ? "Administrador" : "Usuario";
    }

    // Convierte el texto del combo ('Usuario'/'Administrador') al valor del ENUM rol.
    public static String rolDesdeTexto(String texto) {
        // Solo 'Administrador' da permisos de administrador; lo demás queda como 'usuario'.
        return "Administrador".equalsIgnoreCase(texto) ? ROL_ADMINISTRADOR : ROL_USUARIO;
    }

    // Texto del usuario: nombre y apellido si existe apellido.
    @Override
    public String toString() {
        // Arma el nombre completo.
        return apellido == null || apellido.isEmpty() ? nombre : nombre + " " + apellido;
    }

    // Calcula el SHA-256 de la contraseña en 64 caracteres hexadecimales; lo usan login y registro.
    public static String hashPassword(String textoPlano) {
        try {
            // Obtiene el algoritmo SHA-256 del JDK.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Calcula el hash de los bytes UTF-8 del texto.
            byte[] hashBytes = digest.digest(textoPlano.getBytes("UTF-8"));
            // Convierte el hash a hexadecimal con ceros a la izquierda (64 caracteres).
            return String.format("%064x", new BigInteger(1, hashBytes));
        // SHA-256 y UTF-8 siempre existen en Java 8; este caso no debería ocurrir.
        } catch (Exception ex) {
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Si fallara, devolvería el texto sin cifrar (comportamiento original conservado).
            return textoPlano;
        }
    }

    // INSERT del usuario y de su acción 'registro' en historialusuarios, en una misma transacción.
    public static boolean insertUsuario(Usuario usuario) {
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
        // Sentencia parametrizada con los datos del usuario.
        String query = "INSERT INTO usuarios (nombre, apellido, correo_electronico, contrasena, telefono, estado, rol) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            // Inicia una transacción manual: nada se confirma hasta con.commit().
            con.setAutoCommit(false);
            // Id AUTO_INCREMENT del usuario creado.
            int idGenerado;
            // RETURN_GENERATED_KEYS permite leer el id generado para el historial.
            try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                // Parámetro 1: nombre.
                ps.setString(1, usuario.getNombre());
                // Parámetro 2: apellido.
                ps.setString(2, usuario.getApellido());
                // Parámetro 3: correo.
                ps.setString(3, usuario.getCorreoElectronico());
                // Parámetro 4: hash de la contraseña.
                ps.setString(4, usuario.getContrasena());
                // Parámetro 5: teléfono.
                ps.setString(5, usuario.getTelefono());
                // Parámetro 6: estado.
                ps.setString(6, usuario.getEstado());
                // Parámetro 7: rol elegido; 'usuario' si llega vacío.
                ps.setString(7, usuario.getRol() == null ? ROL_USUARIO : usuario.getRol());
                // Inserta el usuario sin confirmar todavía.
                ps.executeUpdate();
                // Lee la llave generada; el ResultSet se cierra al terminar.
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    // Si MySQL no devolvió el id, no se puede registrar el historial.
                    if (!claves.next()) {
                        // Provoca el ROLLBACK.
                        throw new SQLException("no se obtuvo el id del usuario registrado");
                    }
                    // Id del usuario recién creado.
                    idGenerado = claves.getInt(1);
                }
            }
            // Registra la acción 'registro' con los datos nuevos y sin datos anteriores.
            registrarHistorial(con, idGenerado, null, usuario, ACCION_REGISTRO);
            // Confirma en MySQL todas las operaciones de la transacción.
            con.commit();
            // Guarda el id generado en el objeto recibido.
            usuario.setIdUsuario(idGenerado);
            // Usuario registrado.
            return true;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // ROLLBACK: descarta todo lo hecho en la transacción fallida.
            MovimientoInventario.deshacer(con);
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL (por ejemplo, correo duplicado).
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        // Se ejecuta siempre, con éxito o con error.
        } finally {
            // Devuelve la conexión compartida al modo autocommit para los demás módulos.
            MovimientoInventario.restaurarAutoCommit(con);
        }
    }

    // SELECT de todos los usuarios ordenados por id, para Usuarios_Form.
    public static ArrayList<Usuario> getUsuarios() {
        // Lista resultado.
        ArrayList<Usuario> lista = new ArrayList<>();
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
        // Columnas que necesita mapResultSet.
        String query = "SELECT id_usuario, nombre, apellido, correo_electronico, contrasena, telefono, "
                + "fecha_creacion, estado, rol FROM usuarios ORDER BY id_usuario";

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
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar usuarios: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // Devuelve la lista (vacía si no hubo conexión o datos).
        return lista;
    }

    // SELECT de un usuario por correo (login y validación de duplicados); null si no existe.
    public static Usuario getUsuarioPorCorreo(String correo) {
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
        // Consulta por el índice único del correo.
        String query = "SELECT id_usuario, nombre, apellido, correo_electronico, contrasena, telefono, "
                + "fecha_creacion, estado, rol FROM usuarios WHERE correo_electronico = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: correo.
            ps.setString(1, correo);
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
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar el usuario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No existe un usuario con ese correo.
        return null;
    }

    // SELECT de un usuario por id; null si no existe.
    public static Usuario getUsuarioPorId(int idUsuario) {
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
        String query = "SELECT id_usuario, nombre, apellido, correo_electronico, contrasena, telefono, "
                + "fecha_creacion, estado, rol FROM usuarios WHERE id_usuario = ?";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del usuario.
            ps.setInt(1, idUsuario);
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
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de consulta.
            JOptionPane.showMessageDialog(null, "Error al consultar el usuario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
        // No se encontró el usuario.
        return null;
    }

    // true si ya existe un usuario con ese correo.
    public static boolean existeCorreo(String correo) {
        // Reutiliza la consulta por correo.
        return getUsuarioPorCorreo(correo) != null;
    }

    // UPDATE de nombre, apellido, correo, teléfono y estado (no toca la contraseña). Si algo cambió,
    // guarda los valores anteriores y nuevos en historialusuarios en la misma transacción.
    public static boolean updateUsuario(Usuario usuario) {
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
        // Sentencia parametrizada filtrada por id_usuario.
        String query = "UPDATE usuarios SET nombre = ?, apellido = ?, correo_electronico = ?, telefono = ?, "
                + "estado = ?, rol = ? WHERE id_usuario = ?";
        try {
            // Inicia una transacción manual: nada se confirma hasta con.commit().
            con.setAutoCommit(false);

            // Datos actuales del usuario para compararlos con los nuevos.
            Usuario anterior = null;
            // FOR UPDATE bloquea la fila mientras se actualiza y se registra el historial.
            try (PreparedStatement ps = con.prepareStatement("SELECT id_usuario, nombre, apellido, "
                    + "correo_electronico, contrasena, telefono, fecha_creacion, estado, rol FROM usuarios "
                    + "WHERE id_usuario = ? FOR UPDATE")) {
                // Parámetro 1: id del usuario.
                ps.setInt(1, usuario.getIdUsuario());
                // Ejecuta la consulta con bloqueo.
                try (ResultSet rs = ps.executeQuery()) {
                    // Si el usuario existe.
                    if (rs.next()) {
                        // Guarda los valores anteriores.
                        anterior = mapResultSet(rs);
                    }
                }
            }
            // El usuario fue eliminado por otra sesión.
            if (anterior == null) {
                // Libera el bloqueo.
                con.rollback();
                // Usuarios_control informa que el registro ya no coincide.
                return false;
            }
            // Filas afectadas por el UPDATE.
            int filas;
            // Prepara el UPDATE; try-with-resources lo cierra.
            try (PreparedStatement ps = con.prepareStatement(query)) {
                // Parámetro 1: nombre.
                ps.setString(1, usuario.getNombre());
                // Parámetro 2: apellido.
                ps.setString(2, usuario.getApellido());
                // Parámetro 3: correo.
                ps.setString(3, usuario.getCorreoElectronico());
                // Parámetro 4: teléfono.
                ps.setString(4, usuario.getTelefono());
                // Parámetro 5: estado.
                ps.setString(5, usuario.getEstado());
                // Parámetro 6: rol; si no se cambió, conserva el que tenía el usuario.
                ps.setString(6, usuario.getRol() == null ? anterior.getRol() : usuario.getRol());
                // Parámetro 7: id de la fila a modificar.
                ps.setInt(7, usuario.getIdUsuario());
                // Ejecuta el UPDATE dentro de la transacción.
                filas = ps.executeUpdate();
            }
            // Decide qué acción registrar según los campos que cambiaron.
            String accion = determinarAccion(anterior, usuario);
            // Si no cambió nada, no se crea una fila vacía en el historial.
            if (accion != null) {
                // Guarda la acción con los valores anteriores y nuevos.
                registrarHistorial(con, usuario.getIdUsuario(), anterior, usuario, accion);
            }
            // Confirma el cambio y el historial a la vez.
            con.commit();
            // true si se actualizó la fila.
            return filas != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // ROLLBACK: descarta todo lo hecho en la transacción fallida.
            MovimientoInventario.deshacer(con);
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL (por ejemplo, correo duplicado).
            JOptionPane.showMessageDialog(null, "Error al actualizar el usuario: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        // Se ejecuta siempre, con éxito o con error.
        } finally {
            // Devuelve la conexión compartida al modo autocommit para los demás módulos.
            MovimientoInventario.restaurarAutoCommit(con);
        }
    }

    // UPDATE solo de la contraseña (recibe el hash) y registro 'cambio_contrasena' en historialusuarios.
    public static boolean updateContrasena(int idUsuario, String contrasenaEncriptada) {
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
        // Sentencia parametrizada filtrada por id_usuario.
        String query = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
        try {
            // Inicia una transacción manual: nada se confirma hasta con.commit().
            con.setAutoCommit(false);
            // Filas afectadas.
            int filas;
            // Prepara el UPDATE; try-with-resources lo cierra.
            try (PreparedStatement ps = con.prepareStatement(query)) {
                // Parámetro 1: hash de la nueva contraseña.
                ps.setString(1, contrasenaEncriptada);
                // Parámetro 2: id del usuario.
                ps.setInt(2, idUsuario);
                // Ejecuta el UPDATE dentro de la transacción.
                filas = ps.executeUpdate();
            }
            // El usuario ya no existe.
            if (filas == 0) {
                // Deshace la transacción vacía.
                con.rollback();
                // Usuarios_control informa que el registro ya no coincide.
                return false;
            }
            // Registra la acción sin datos personales (la contraseña nunca se guarda en el historial).
            registrarHistorial(con, idUsuario, null, null, ACCION_CAMBIO_CONTRASENA);
            // Confirma en MySQL todas las operaciones de la transacción.
            con.commit();
            // Contraseña actualizada.
            return true;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // ROLLBACK: descarta todo lo hecho en la transacción fallida.
            MovimientoInventario.deshacer(con);
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error de MySQL.
            JOptionPane.showMessageDialog(null, "Error al actualizar la contraseña: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        // Se ejecuta siempre, con éxito o con error.
        } finally {
            // Devuelve la conexión compartida al modo autocommit para los demás módulos.
            MovimientoInventario.restaurarAutoCommit(con);
        }
    }

    // DELETE del usuario; la llave foránea de historialusuarios impide borrar un usuario con historial,
    // así que primero se borra su historial en la misma transacción.
    public static boolean deleteUsuario(int idUsuario) {
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
        try {
            // Inicia una transacción manual: nada se confirma hasta con.commit().
            con.setAutoCommit(false);
            // Borra primero el historial del usuario.
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM historialusuarios WHERE id_usuario = ?")) {
                // Parámetro 1: id del usuario.
                ps.setInt(1, idUsuario);
                // Ejecuta dentro de la transacción.
                ps.executeUpdate();
            }
            // Filas eliminadas de usuarios.
            int filas;
            // Borra el usuario.
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios WHERE id_usuario = ?")) {
                // Parámetro 1: id del usuario.
                ps.setInt(1, idUsuario);
                // Falla si el usuario tiene movimientos, historial de inventario o reportes (llaves RESTRICT).
                filas = ps.executeUpdate();
            }
            // Confirma ambos borrados.
            con.commit();
            // true si se eliminó el usuario.
            return filas != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // ROLLBACK: si el usuario no se pudo borrar, su historial también se conserva.
            MovimientoInventario.deshacer(con);
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Informa el error y sugiere desactivar el usuario en lugar de eliminarlo.
            JOptionPane.showMessageDialog(null, "Error al eliminar el usuario: " + ex.getMessage()
                    + ". Si el usuario tiene movimientos o compras registradas, desactívelo en lugar de eliminarlo.",
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            // La operación falló; el controlador recibe este resultado.
            return false;
        // Se ejecuta siempre, con éxito o con error.
        } finally {
            // Devuelve la conexión compartida al modo autocommit para los demás módulos.
            MovimientoInventario.restaurarAutoCommit(con);
        }
    }

    // Crea la acción 'registro' de los usuarios que aún no la tienen (por ejemplo, los creados antes
    // de existir el historial) con su fecha de creación. Login_control la ejecuta al iniciar sesión.
    public static int sincronizarHistorialRegistros() {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Sin conexión no se sincroniza nada.
            return 0;
        }
        // INSERT ... SELECT: copia los datos actuales de cada usuario sin registro; NOT EXISTS evita
        // duplicar filas si se ejecuta varias veces.
        String query = "INSERT INTO historialusuarios (id_usuario, nombre_nuevo, apellido_nuevo, correo_nuevo, "
                + "telefono_nuevo, estado_nuevo, accion, fecha_accion) "
                + "SELECT u.id_usuario, u.nombre, u.apellido, u.correo_electronico, u.telefono, u.estado, ?, "
                + "u.fecha_creacion FROM usuarios u WHERE NOT EXISTS (SELECT 1 FROM historialusuarios h "
                + "WHERE h.id_usuario = u.id_usuario AND h.accion = ?)";
        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: acción 'registro' que se inserta.
            ps.setString(1, ACCION_REGISTRO);
            // Parámetro 2: acción 'registro' que se busca en NOT EXISTS.
            ps.setString(2, ACCION_REGISTRO);
            // Devuelve cuántos usuarios se agregaron al historial.
            return ps.executeUpdate();
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            // Ante un error no se agrega nada; el login continúa normalmente.
            return 0;
        }
    }

    // Decide la acción del historial según lo que cambió; null si no cambió nada.
    private static String determinarAccion(Usuario anterior, Usuario nuevo) {
        // Cambió el nombre o el apellido.
        boolean cambioNombre = !igual(anterior.getNombre(), nuevo.getNombre())
                || !igual(anterior.getApellido(), nuevo.getApellido());
        // Cambió el correo.
        boolean cambioCorreo = !igual(anterior.getCorreoElectronico(), nuevo.getCorreoElectronico());
        // Cambió el teléfono.
        boolean cambioTelefono = !igual(anterior.getTelefono(), nuevo.getTelefono());
        // Cambió el estado.
        boolean cambioEstado = !igual(anterior.getEstado(), nuevo.getEstado());
        // Cambió el rol (historialusuarios no tiene columnas de rol: se registra como 'actualizacion').
        boolean cambioRol = nuevo.getRol() != null && !igual(anterior.getRol(), nuevo.getRol());

        // Cuenta cuántos grupos de datos cambiaron.
        int cambios = (cambioNombre ? 1 : 0) + (cambioCorreo ? 1 : 0) + (cambioTelefono ? 1 : 0)
                + (cambioEstado ? 1 : 0) + (cambioRol ? 1 : 0);
        // Nada cambió.
        if (cambios == 0) {
            // No se registra historial.
            return null;
        }
        // Varios cambios a la vez, cambio de nombre o cambio de rol se registran como 'actualizacion'.
        if (cambios > 1 || cambioNombre || cambioRol) {
            // Acción general.
            return ACCION_ACTUALIZACION;
        }
        // Solo cambió el correo.
        if (cambioCorreo) {
            // Acción 'cambio_correo'.
            return ACCION_CAMBIO_CORREO;
        }
        // Solo cambió el teléfono.
        if (cambioTelefono) {
            // Acción 'cambio_telefono'.
            return ACCION_CAMBIO_TELEFONO;
        }
        // Solo cambió el estado: 'activacion' o 'desactivacion' según el nuevo valor.
        return nuevo.isEstado() ? ACCION_ACTIVACION : ACCION_DESACTIVACION;
    }

    // Compara textos tratando null y vacío como iguales y sin espacios sobrantes.
    private static boolean igual(String a, String b) {
        // Normaliza el primer valor.
        String x = a == null ? "" : a.trim();
        // Normaliza el segundo valor.
        String y = b == null ? "" : b.trim();
        // true si son iguales.
        return x.equals(y);
    }

    // INSERT en historialusuarios con la conexión y transacción recibidas; la fecha la pone MySQL
    // con el DEFAULT CURRENT_TIMESTAMP de fecha_accion. Los datos que faltan quedan en NULL.
    private static void registrarHistorial(Connection con, int idUsuario, Usuario anterior, Usuario nuevo,
            String accion) throws SQLException {
        // Sentencia con los pares anterior/nuevo de cada dato y la acción.
        String query = "INSERT INTO historialusuarios (id_usuario, nombre_anterior, nombre_nuevo, "
                + "apellido_anterior, apellido_nuevo, correo_anterior, correo_nuevo, telefono_anterior, "
                + "telefono_nuevo, estado_anterior, estado_nuevo, accion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Prepara la sentencia; try-with-resources la cierra.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: id del usuario afectado.
            ps.setInt(1, idUsuario);
            // Parámetro 2: nombre anterior o NULL.
            ps.setString(2, anterior == null ? null : anterior.getNombre());
            // Parámetro 3: nombre nuevo o NULL.
            ps.setString(3, nuevo == null ? null : nuevo.getNombre());
            // Parámetro 4: apellido anterior o NULL.
            ps.setString(4, anterior == null ? null : anterior.getApellido());
            // Parámetro 5: apellido nuevo o NULL.
            ps.setString(5, nuevo == null ? null : nuevo.getApellido());
            // Parámetro 6: correo anterior o NULL.
            ps.setString(6, anterior == null ? null : anterior.getCorreoElectronico());
            // Parámetro 7: correo nuevo o NULL.
            ps.setString(7, nuevo == null ? null : nuevo.getCorreoElectronico());
            // Parámetro 8: teléfono anterior o NULL.
            ps.setString(8, anterior == null ? null : anterior.getTelefono());
            // Parámetro 9: teléfono nuevo o NULL.
            ps.setString(9, nuevo == null ? null : nuevo.getTelefono());
            // Parámetro 10: estado anterior o NULL.
            ps.setString(10, anterior == null ? null : anterior.getEstado());
            // Parámetro 11: estado nuevo o NULL.
            ps.setString(11, nuevo == null ? null : nuevo.getEstado());
            // Parámetro 12: acción del ENUM.
            ps.setString(12, accion);
            // Ejecuta dentro de la transacción del llamador.
            ps.executeUpdate();
        }
    }

    // Convierte la fila actual del ResultSet en un objeto Usuario.
    private static Usuario mapResultSet(ResultSet rs) throws SQLException {
        // Lee cada columna por su nombre y llama al constructor completo.
        Usuario usuario = new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("correo_electronico"),
                rs.getString("contrasena"),
                rs.getString("telefono"),
                rs.getTimestamp("fecha_creacion"),
                rs.getString("estado")
        );
        // Agrega el rol leído de la columna rol.
        usuario.setRol(rs.getString("rol"));
        // Devuelve el usuario con todos sus datos.
        return usuario;
    }
}
