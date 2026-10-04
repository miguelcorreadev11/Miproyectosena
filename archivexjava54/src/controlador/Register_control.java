// Paquete de controladores del patrón MVC.
package controlador;

// JOptionPane muestra los avisos de validación del registro.
import javax.swing.JOptionPane;
// Modelo Usuario: verifica correos duplicados, cifra la contraseña e inserta el usuario.
import modelo.Usuario;

// Controlador de Register_Form: valida el formulario de registro público y crea el usuario.
public class Register_control {
    // Versión sin rol (compatibilidad): registra la cuenta con rol 'usuario'.
    public static boolean registrarUsuario(String nombre, String apellido, String correo, String contrasena,
            String confirmarContrasena, String telefono) {
        // Delega en la versión con rol.
        return registrarUsuario(nombre, apellido, correo, contrasena, confirmarContrasena, telefono,
                Usuario.ROL_USUARIO);
    }

    // Recibe los textos y el rol del formulario; devuelve true si el usuario quedó guardado en MySQL.
    public static boolean registrarUsuario(String nombre, String apellido, String correo, String contrasena,
            String confirmarContrasena, String telefono, String rol) {
        // El nombre es obligatorio.
        if (nombre == null || nombre.trim().isEmpty()) {
            // Avisa que falta el nombre.
            JOptionPane.showMessageDialog(null, "Ingrese el nombre", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // El apellido es obligatorio.
        if (apellido == null || apellido.trim().isEmpty()) {
            // Avisa que falta el apellido.
            JOptionPane.showMessageDialog(null, "Ingrese el apellido", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // El correo es obligatorio.
        if (correo == null || correo.trim().isEmpty()) {
            // Avisa que falta el correo.
            JOptionPane.showMessageDialog(null, "Ingrese el correo electrónico", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // Valida el formato texto@dominio.ext con una expresión regular.
        if (!correo.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            // Avisa que el correo no tiene un formato válido.
            JOptionPane.showMessageDialog(null, "El correo electrónico no es válido", "Dato inválido",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // La contraseña es obligatoria.
        if (contrasena == null || contrasena.trim().isEmpty()) {
            // Avisa que falta la contraseña.
            JOptionPane.showMessageDialog(null, "Ingrese la contraseña", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // Exige una contraseña de mínimo 4 caracteres.
        if (contrasena.length() < 4) {
            // Avisa que la contraseña es demasiado corta.
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 4 caracteres",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // La confirmación debe coincidir exactamente con la contraseña.
        if (!contrasena.equals(confirmarContrasena)) {
            // Avisa que las contraseñas no coinciden.
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden", "Dato inválido",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }

        // Consulta si el correo ya existe (usuarios.correo_electronico tiene índice UNIQUE).
        if (Usuario.existeCorreo(correo.trim())) {
            // Avisa que el correo ya está registrado.
            JOptionPane.showMessageDialog(null, "Ya existe un usuario registrado con ese correo",
                    "Correo duplicado", JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }
        // Si no se pudo verificar el correo por un error de MySQL (ya informado), no se intenta guardar.
        if (config.conexión_BD.huboErrorEnUltimaOperacion()) {
            return false;
        }

        // Calcula el SHA-256 de la contraseña; en MySQL nunca se guarda el texto plano.
        String contrasenaEncriptada = Usuario.hashPassword(contrasena);
        // Crea el Usuario nuevo con estado 'activo'; un teléfono null se guarda como texto vacío.
        Usuario usuario = new Usuario(nombre.trim(), apellido.trim(), correo.trim(), contrasenaEncriptada,
                telefono == null ? "" : telefono.trim());
        // Rol elegido en Register_Form ('usuario' o 'administrador').
        usuario.setRol(Usuarios_control.normalizarRol(rol));

        // Inserta el usuario y su acción 'registro' en historialusuarios dentro de una transacción.
        boolean registrado = Usuario.insertUsuario(usuario);

        // Si se guardó, confirma al usuario.
        if (registrado) {
            // Muestra el mensaje de registro exitoso.
            JOptionPane.showMessageDialog(null, "Usuario registrado correctamente", "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        // Register_Form vuelve al login solo si el resultado es true.
        return registrado;
    }
}
