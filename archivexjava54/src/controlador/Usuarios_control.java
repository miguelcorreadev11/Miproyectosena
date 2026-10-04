// Paquete de controladores del patrón MVC.
package controlador;

// ArrayList devuelve la lista de usuarios a la vista.
import java.util.ArrayList;
// JOptionPane muestra validaciones, confirmaciones y resultados.
import javax.swing.JOptionPane;
// Modelo Usuario: CRUD de usuarios y registro automático en historialusuarios.
import modelo.Usuario;

// Controlador de Usuarios_Form (administración de usuarios del sistema).
public class Usuarios_control {
    // Devuelve todos los usuarios para la JTable de Usuarios_Form.
    public static ArrayList<Usuario> listarUsuarios() {
        // SELECT de la tabla usuarios ordenado por id.
        return Usuario.getUsuarios();
    }

    // Versión sin rol (compatibilidad): crea el usuario con rol 'usuario'.
    public static boolean crearUsuario(String nombre, String apellido, String correo, String contrasena,
            String telefono, boolean estadoActivo) {
        // Delega en la versión con rol.
        return crearUsuario(nombre, apellido, correo, contrasena, telefono, estadoActivo, Usuario.ROL_USUARIO);
    }

    // Crea un usuario desde el administrador; permite elegir estado (activo/inactivo) y rol.
    public static boolean crearUsuario(String nombre, String apellido, String correo, String contrasena,
            String telefono, boolean estadoActivo, String rol) {
        // Nombre, apellido y correo obligatorios, y correo con formato válido.
        if (!validarCamposObligatorios(nombre, apellido, correo)) {
            // Detiene la creación.
            return false;
        }

        // La contraseña es obligatoria al crear un usuario.
        if (contrasena == null || contrasena.trim().isEmpty()) {
            // Avisa que falta la contraseña.
            JOptionPane.showMessageDialog(null, "Ingrese una contraseña para el nuevo usuario",
                    "Campo obligatorio", JOptionPane.WARNING_MESSAGE);
            // Detiene la creación.
            return false;
        }

        // Exige una contraseña de mínimo 4 caracteres.
        if (contrasena.length() < 4) {
            // Avisa que la contraseña es demasiado corta.
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 4 caracteres",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Detiene la creación.
            return false;
        }

        // El correo es UNIQUE en usuarios; se valida antes del INSERT.
        if (Usuario.existeCorreo(correo.trim())) {
            // Avisa que el correo ya está registrado.
            JOptionPane.showMessageDialog(null, "Ya existe un usuario registrado con ese correo",
                    "Correo duplicado", JOptionPane.WARNING_MESSAGE);
            // Detiene la creación.
            return false;
        }
        // Si no se pudo verificar el correo por un error de MySQL (ya informado), no se intenta guardar.
        if (config.conexión_BD.huboErrorEnUltimaOperacion()) {
            return false;
        }

        // Calcula el SHA-256 de la contraseña.
        String contrasenaEncriptada = Usuario.hashPassword(contrasena);
        // Crea el usuario (queda 'activo' por el constructor); un teléfono null se guarda como vacío.
        Usuario nuevoUsuario = new Usuario(nombre.trim(), apellido.trim(), correo.trim(), contrasenaEncriptada,
                telefono == null ? "" : telefono.trim());
        // Rol elegido en el combo; un valor desconocido se guarda como 'usuario'.
        nuevoUsuario.setRol(normalizarRol(rol));

        // Inserta el usuario y su acción 'registro' en historialusuarios.
        boolean creado = Usuario.insertUsuario(nuevoUsuario);

        // Si se creó y se pidió estado inactivo, se aplica con un UPDATE posterior.
        if (creado && !estadoActivo) {
            // Recupera el usuario recién creado (con su id) buscando por correo.
            Usuario usuarioCreado = Usuario.getUsuarioPorCorreo(correo.trim());
            // Solo continúa si se encontró el registro.
            if (usuarioCreado != null) {
                // Cambia el estado a 'inactivo'.
                usuarioCreado.setEstado(false);
                // El UPDATE deja además la acción 'desactivacion' en historialusuarios.
                Usuario.updateUsuario(usuarioCreado);
            }
        }

        // Si se creó, confirma al usuario.
        if (creado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Usuario creado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Usuarios_Form refresca la tabla si es true.
        return creado;
    }

    // Versión sin rol (compatibilidad): actualiza los datos y conserva el rol actual del usuario.
    public static boolean actualizarUsuario(Integer idUsuario, String nombre, String apellido, String correo,
            String telefono, boolean estadoActivo) {
        // null indica al modelo que no cambie el rol.
        return actualizarUsuario(idUsuario, nombre, apellido, correo, telefono, estadoActivo, null);
    }

    // Actualiza datos, estado y rol del usuario seleccionado; la contraseña se cambia con otro método.
    public static boolean actualizarUsuario(Integer idUsuario, String nombre, String apellido, String correo,
            String telefono, boolean estadoActivo, String rol) {
        // Sin id seleccionado no se sabe qué registro actualizar.
        if (idUsuario == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un usuario de la tabla para actualizar",
                    "Usuario no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Revalida nombre, apellido y correo.
        if (!validarCamposObligatorios(nombre, apellido, correo)) {
            // Detiene la actualización.
            return false;
        }

        // Carga el usuario actual desde MySQL para conservar contraseña y fecha de creación.
        Usuario usuarioExistente = Usuario.getUsuarioPorId(idUsuario);
        // Si ya no existe (fue eliminado), no se puede actualizar.
        if (usuarioExistente == null) {
            // Si fue un error de MySQL ya se informó; no se afirma que el usuario fue eliminado.
            if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
                // Avisa que el usuario ya no existe.
                JOptionPane.showMessageDialog(null, "El usuario seleccionado ya no existe",
                        "Usuario no encontrado", JOptionPane.WARNING_MESSAGE);
            }
            // Detiene la actualización.
            return false;
        }

        // Busca si otro usuario ya tiene el correo nuevo.
        Usuario usuarioConEseCorreo = Usuario.getUsuarioPorCorreo(correo.trim());
        // Sin poder verificar el correo (error de MySQL ya informado) no se intenta actualizar.
        if (config.conexión_BD.huboErrorEnUltimaOperacion()) {
            return false;
        }
        // Un mismo correo solo se permite si pertenece al usuario que se edita.
        if (usuarioConEseCorreo != null && !usuarioConEseCorreo.getIdUsuario().equals(idUsuario)) {
            // Avisa del correo duplicado.
            JOptionPane.showMessageDialog(null, "Ya existe otro usuario registrado con ese correo",
                    "Correo duplicado", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Asigna el nombre nuevo sin espacios sobrantes.
        usuarioExistente.setNombre(nombre.trim());
        // Asigna el apellido nuevo.
        usuarioExistente.setApellido(apellido.trim());
        // Asigna el correo nuevo.
        usuarioExistente.setCorreoElectronico(correo.trim());
        // Asigna el teléfono nuevo (vacío si llega null).
        usuarioExistente.setTelefono(telefono == null ? "" : telefono.trim());
        // Convierte el booleano del combo en 'activo' o 'inactivo'.
        usuarioExistente.setEstado(estadoActivo);
        // Solo cambia el rol si se envió uno; con null se conserva el que tenía.
        if (rol != null) {
            // Guarda 'usuario' o 'administrador'.
            usuarioExistente.setRol(normalizarRol(rol));
        }

        // UPDATE en una transacción que también registra el cambio en historialusuarios.
        boolean actualizado = Usuario.updateUsuario(usuarioExistente);
        // Si se actualizó, confirma al usuario.
        if (actualizado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        // El UPDATE no encontró la fila o falló.
        // Si el false se debe a un error de MySQL, ese error ya se mostró: no se pide recargar ni redigitar.
        } else if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
            // Explica que el registro ya no coincide y cómo recargarlo.
            JOptionPane.showMessageDialog(null,
                    "No se pudo actualizar el usuario: el registro ya no coincide con el que se cargó "
                    + "en el formulario (puede haber sido modificado o eliminado). Cierre y vuelva a "
                    + "abrir el administrador de usuarios, seleccione el usuario nuevamente e intente de nuevo.",
                    "No se aplicaron cambios", JOptionPane.WARNING_MESSAGE);
        }
        // Devuelve el resultado a Usuarios_Form.
        return actualizado;
    }

    // Cambia solo la contraseña del usuario seleccionado.
    public static boolean actualizarContrasena(Integer idUsuario, String nuevaContrasena) {
        // Sin id seleccionado no se sabe a quién cambiarle la contraseña.
        if (idUsuario == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un usuario de la tabla para actualizar",
                    "Usuario no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene el cambio.
            return false;
        }

        // La nueva contraseña es obligatoria.
        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            // Avisa que falta la nueva contraseña.
            JOptionPane.showMessageDialog(null, "Ingrese la nueva contraseña", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Detiene el cambio.
            return false;
        }

        // Exige un mínimo de 4 caracteres.
        if (nuevaContrasena.length() < 4) {
            // Avisa que la contraseña es demasiado corta.
            JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 4 caracteres",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Detiene el cambio.
            return false;
        }

        // Calcula el SHA-256 de la nueva contraseña.
        String contrasenaEncriptada = Usuario.hashPassword(nuevaContrasena);
        // UPDATE de usuarios.contrasena y registro 'cambio_contrasena' en historialusuarios.
        boolean actualizada = Usuario.updateContrasena(idUsuario, contrasenaEncriptada);

        // Si se actualizó, confirma al usuario.
        if (actualizada) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Contraseña actualizada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        // El UPDATE no encontró la fila o falló.
        // Si el false se debe a un error de MySQL, ese error ya se mostró: no se pide recargar ni redigitar.
        } else if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
            // Explica que el registro ya no coincide y cómo recargarlo.
            JOptionPane.showMessageDialog(null,
                    "No se pudo actualizar la contraseña: el usuario ya no coincide con el que se cargó "
                    + "en el formulario (puede haber sido modificado o eliminado). Cierre y vuelva a "
                    + "abrir el administrador de usuarios, seleccione el usuario nuevamente e intente de nuevo.",
                    "No se aplicaron cambios", JOptionPane.WARNING_MESSAGE);
        }
        // Devuelve el resultado a Usuarios_Form.
        return actualizada;
    }

    // Elimina el usuario seleccionado tras pedir confirmación.
    public static boolean eliminarUsuario(Integer idUsuario) {
        // Sin id seleccionado no se puede eliminar.
        if (idUsuario == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un usuario de la tabla para eliminar",
                    "Usuario no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la eliminación.
            return false;
        }

        // Pide confirmación Sí/No antes de borrar.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar el usuario seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se elimina nada.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Cancela la eliminación.
            return false;
        }

        // Borra su historial y luego el usuario; falla si tiene movimientos o reportes asociados.
        boolean eliminado = Usuario.deleteUsuario(idUsuario);
        // Si se borró, confirma al usuario.
        if (eliminado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Devuelve el resultado a Usuarios_Form.
        return eliminado;
    }

    // Devuelve 'administrador' solo si se pidió ese rol; cualquier otro valor queda como 'usuario'.
    public static String normalizarRol(String rol) {
        // Compara sin distinguir mayúsculas con el valor del ENUM.
        return Usuario.ROL_ADMINISTRADOR.equalsIgnoreCase(rol == null ? "" : rol.trim())
                ? Usuario.ROL_ADMINISTRADOR : Usuario.ROL_USUARIO;
    }

    // Comprueba nombre, apellido y correo del formulario de usuarios.
    private static boolean validarCamposObligatorios(String nombre, String apellido, String correo) {
        // El nombre no puede ser null ni solo espacios.
        if (nombre == null || nombre.trim().isEmpty()) {
            // Avisa que falta el nombre.
            JOptionPane.showMessageDialog(null, "Ingrese el nombre del usuario", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // El apellido no puede ser null ni solo espacios.
        if (apellido == null || apellido.trim().isEmpty()) {
            // Avisa que falta el apellido.
            JOptionPane.showMessageDialog(null, "Ingrese el apellido del usuario", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // El correo no puede ser null ni solo espacios.
        if (correo == null || correo.trim().isEmpty()) {
            // Avisa que falta el correo.
            JOptionPane.showMessageDialog(null, "Ingrese el correo electrónico", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // El correo debe cumplir el formato texto@dominio.ext.
        if (!correo.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            // Avisa que el correo no es válido.
            JOptionPane.showMessageDialog(null, "El correo electrónico no es válido", "Dato inválido",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // Datos válidos.
        return true;
    }
}
