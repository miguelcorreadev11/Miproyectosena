// Paquete de controladores: validan datos de las vistas y delegan el acceso a MySQL en los modelos.
package controlador;

// JOptionPane muestra los avisos de validación y de error del inicio de sesión.
import javax.swing.JOptionPane;
// Modelo Usuario: consulta la tabla usuarios y calcula el hash de la contraseña.
import modelo.Usuario;

// Controlador de Login_Form: valida credenciales y conserva el usuario autenticado de la sesión.
public class Login_control {
    // Usuario autenticado; lo consultan Movimiento_control, Compra_control y Reportes_control
    // para guardar el id_usuario responsable en MySQL.
    private static Usuario usuarioSesion;

    // Devuelve el usuario de la sesión actual o null si nadie ha iniciado sesión.
    public static Usuario getUsuarioSesion() {
        return usuarioSesion;
    }

    // Borra el usuario de la sesión; lo invoca Home_Form al cerrar sesión.
    public static void cerrarSesion() {
        // Deja la sesión vacía para que los módulos exijan un nuevo inicio de sesión.
        usuarioSesion = null;
    }

    // Valida el formulario de login y devuelve el Usuario autenticado, o null si algo falla;
    // lo llama Login_Form.jButtonIngresarActionPerformed.
    public static Usuario validarLogin(String correo, String contrasena) {
        // El correo es obligatorio: se rechaza null o texto solo con espacios.
        if (correo == null || correo.trim().isEmpty()) {
            // Avisa que falta el correo.
            JOptionPane.showMessageDialog(null, "Ingrese su correo electrónico", "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el login sin consultar la base de datos.
            return null;
        }

        // La contraseña es obligatoria.
        if (contrasena == null || contrasena.trim().isEmpty()) {
            // Avisa que falta la contraseña.
            JOptionPane.showMessageDialog(null, "Ingrese su contraseña", "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el login.
            return null;
        }

        // Busca en la tabla usuarios el registro con ese correo (sin espacios al inicio o al final).
        Usuario usuario = Usuario.getUsuarioPorCorreo(correo.trim());

        // No existe un usuario con ese correo (o no hubo conexión con MySQL).
        if (usuario == null) {
            // Si fue un error de MySQL ya se informó; no se culpa al correo digitado.
            if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
                // Informa que el usuario no existe.
                JOptionPane.showMessageDialog(null, "El usuario no existe", "Error de inicio de sesión",
                        JOptionPane.ERROR_MESSAGE);
            }
            // Rechaza el inicio de sesión.
            return null;
        }

        // isEstado() es true solo si la columna estado vale 'activo'.
        if (!usuario.isEstado()) {
            // Informa que la cuenta está desactivada desde el módulo Usuarios.
            JOptionPane.showMessageDialog(null, "El usuario se encuentra inactivo", "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE);
            // Rechaza el inicio de sesión.
            return null;
        }

        // Calcula el SHA-256 de la contraseña escrita, igual que al registrar el usuario.
        String contrasenaEncriptada = Usuario.hashPassword(contrasena);
        // Compara el hash calculado con el guardado en usuarios.contrasena.
        if (!contrasenaEncriptada.equals(usuario.getContrasena())) {
            // Informa que la contraseña no coincide.
            JOptionPane.showMessageDialog(null, "La contraseña es incorrecta", "Error de inicio de sesión",
                    JOptionPane.ERROR_MESSAGE);
            // Rechaza el inicio de sesión.
            return null;
        }

        // Guarda el usuario autenticado como responsable de compras, movimientos y reportes.
        usuarioSesion = usuario;

        // Crea en historialusuarios la acción 'registro' de los usuarios que aún no la tienen.
        Usuario.sincronizarHistorialRegistros();
        // Devuelve el usuario para que Login_Form abra Home_Form.
        return usuario;
    }
}
