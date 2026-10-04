// Paquete de controladores del patrón MVC.
package controlador;

// ArrayList devuelve la lista de proveedores a la vista.
import java.util.ArrayList;
// JOptionPane muestra validaciones, confirmaciones y resultados.
import javax.swing.JOptionPane;
// Modelo Proveedor: ejecuta el CRUD sobre la tabla proveedores.
import modelo.Proveedor;

// Controlador de la pestaña Proveedores de Catalogos_Form.
public class Proveedor_control {
    // Valida y registra un proveedor nuevo; devuelve true si se insertó.
    public static boolean crearProveedor(String nombre, String correo, String telefono, String direccion,
            String ciudad, String pais, String estado) {
        // Nombre obligatorio y correo con formato válido si se escribió.
        if (!validarCamposObligatorios(nombre, correo)) {
            // Detiene el registro.
            return false;
        }

        // Crea el proveedor limpiando espacios del nombre y del correo, y normalizando el teléfono.
        Proveedor proveedor = new Proveedor(nombre.trim(), correo == null ? null : correo.trim(),
                normalizarTelefono(telefono), direccion, ciudad, pais, estado);

        // Ejecuta el INSERT en la tabla proveedores.
        boolean creado = Proveedor.insertProveedor(proveedor);
        // Si se insertó, confirma al usuario.
        if (creado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Proveedor registrado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Catalogos_Form refresca la tabla solo si es true.
        return creado;
    }

    // Devuelve los proveedores para la JTable de Catalogos_Form.
    public static ArrayList<Proveedor> listarProveedores() {
        // Delegación directa al SELECT del modelo.
        return Proveedor.getProveedores();
    }

    // Valida y actualiza el proveedor seleccionado en la tabla.
    public static boolean actualizarProveedor(Integer idProveedor, String nombre, String correo,
            String telefono, String direccion, String ciudad, String pais, String estado) {
        // Sin id seleccionado no se sabe qué registro actualizar.
        if (idProveedor == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un proveedor de la tabla para actualizar",
                    "Proveedor no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Revalida nombre y correo.
        if (!validarCamposObligatorios(nombre, correo)) {
            // Detiene la actualización.
            return false;
        }

        // Crea el objeto con el id seleccionado; la fecha de registro no se modifica (null).
        Proveedor proveedor = new Proveedor(idProveedor, nombre.trim(), correo == null ? null : correo.trim(),
                normalizarTelefono(telefono), direccion, ciudad, pais, estado, null);

        // Ejecuta el UPDATE por id_proveedor.
        boolean actualizado = Proveedor.updateProveedor(proveedor);
        // Si se modificó la fila, confirma al usuario.
        if (actualizado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Proveedor actualizado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        // El UPDATE no encontró la fila (fue eliminada o cambiada por otra sesión).
        // Si el false se debe a un error de MySQL, ese error ya se mostró: no se pide recargar ni redigitar.
        } else if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
            // Explica que el registro ya no coincide y cómo recargarlo.
            JOptionPane.showMessageDialog(null,
                    "No se pudo actualizar el proveedor: el registro ya no coincide con el que se cargó "
                    + "en el formulario (puede haber sido modificado o eliminado). Cierre y vuelva a "
                    + "abrir el catálogo de proveedores, seleccione el proveedor nuevamente e intente de nuevo.",
                    "No se aplicaron cambios", JOptionPane.WARNING_MESSAGE);
        }
        // Devuelve el resultado a Catalogos_Form.
        return actualizado;
    }

    // Elimina el proveedor seleccionado tras pedir confirmación.
    public static boolean eliminarProveedor(Integer idProveedor) {
        // Sin id seleccionado no se puede eliminar.
        if (idProveedor == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un proveedor de la tabla para eliminar",
                    "Proveedor no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la eliminación.
            return false;
        }

        // Pide confirmación Sí/No antes de borrar.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar el proveedor seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se elimina nada.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Cancela la eliminación.
            return false;
        }

        // DELETE por id; falla si el proveedor tiene compras registradas (llave foránea RESTRICT).
        boolean eliminado = Proveedor.deleteProveedor(idProveedor);
        // Si se borró la fila, confirma al usuario.
        if (eliminado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Devuelve el resultado a Catalogos_Form.
        return eliminado;
    }

    // Convierte un teléfono vacío en null para no guardar cadenas vacías en proveedores.telefono.
    private static String normalizarTelefono(String telefono) {
        // Campo vacío o solo espacios.
        if (telefono == null || telefono.trim().isEmpty()) {
            // Se guardará NULL en la base de datos.
            return null;
        }
        // Devuelve el teléfono sin espacios sobrantes.
        return telefono.trim();
    }

    // Comprueba el nombre obligatorio y el formato opcional del correo.
    private static boolean validarCamposObligatorios(String nombre, String correo) {
        // El nombre no puede ser null ni solo espacios.
        if (nombre == null || nombre.trim().isEmpty()) {
            // Avisa que falta el nombre.
            JOptionPane.showMessageDialog(null, "Ingrese el nombre del proveedor", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // El correo es opcional, pero si se escribió debe cumplir el formato texto@dominio.ext.
        if (correo != null && !correo.trim().isEmpty()
                && !correo.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
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
