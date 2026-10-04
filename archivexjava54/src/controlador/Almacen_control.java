// Paquete de controladores del patrón MVC.
package controlador;

// ArrayList devuelve la lista de almacenes a la vista.
import java.util.ArrayList;
// JOptionPane muestra validaciones, confirmaciones y resultados.
import javax.swing.JOptionPane;
// Modelo Almacen: ejecuta el CRUD sobre la tabla almacenes.
import modelo.Almacen;

// Controlador de la pestaña Almacenes de Catalogos_Form.
public class Almacen_control {
    // Valida y registra un almacén nuevo; capacidadMaxima llega null si el campo quedó vacío.
    public static boolean crearAlmacen(String nombre, String ubicacion, String ciudad,
            Integer capacidadMaxima, String estado) {
        // Nombre y ubicación son obligatorios (columnas NOT NULL).
        if (!validarCamposObligatorios(nombre, ubicacion)) {
            // Detiene el registro.
            return false;
        }

        // Crea el almacén con nombre y ubicación sin espacios sobrantes.
        Almacen almacen = new Almacen(nombre.trim(), ubicacion.trim(), ciudad, capacidadMaxima, estado);

        // Ejecuta el INSERT en la tabla almacenes.
        boolean creado = Almacen.insertAlmacen(almacen);
        // Si se insertó, confirma al usuario.
        if (creado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Almacén registrado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Catalogos_Form refresca la tabla solo si es true.
        return creado;
    }

    // Devuelve los almacenes para llenar la JTable de Catalogos_Form.
    public static ArrayList<Almacen> listarAlmacenes() {
        // Delegación directa al SELECT del modelo.
        return Almacen.getAlmacenes();
    }

    // Valida y actualiza el almacén seleccionado en la tabla.
    public static boolean actualizarAlmacen(Integer idAlmacen, String nombre, String ubicacion,
            String ciudad, Integer capacidadMaxima, String estado) {
        // Sin id seleccionado no se sabe qué registro actualizar.
        if (idAlmacen == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un almacén de la tabla para actualizar",
                    "Almacén no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Revalida nombre y ubicación.
        if (!validarCamposObligatorios(nombre, ubicacion)) {
            // Detiene la actualización.
            return false;
        }

        // Crea el objeto con el id seleccionado; la fecha de creación no se modifica (null).
        Almacen almacen = new Almacen(idAlmacen, nombre.trim(), ubicacion.trim(), ciudad,
                capacidadMaxima, estado, null);

        // Ejecuta el UPDATE por id_almacen.
        boolean actualizado = Almacen.updateAlmacen(almacen);
        // Si se modificó la fila, confirma al usuario.
        if (actualizado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Almacén actualizado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        // El UPDATE no encontró la fila (fue eliminada o cambiada por otra sesión).
        // Si el false se debe a un error de MySQL, ese error ya se mostró: no se pide recargar ni redigitar.
        } else if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
            // Explica que el registro ya no coincide y cómo recargarlo.
            JOptionPane.showMessageDialog(null,
                    "No se pudo actualizar el almacén: el registro ya no coincide con el que se cargó "
                    + "en el formulario (puede haber sido modificado o eliminado). Cierre y vuelva a "
                    + "abrir el catálogo de almacenes, seleccione el almacén nuevamente e intente de nuevo.",
                    "No se aplicaron cambios", JOptionPane.WARNING_MESSAGE);
        }
        // Devuelve el resultado a Catalogos_Form.
        return actualizado;
    }

    // Elimina el almacén seleccionado tras pedir confirmación.
    public static boolean eliminarAlmacen(Integer idAlmacen) {
        // Sin id seleccionado no se puede eliminar.
        if (idAlmacen == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un almacén de la tabla para eliminar",
                    "Almacén no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la eliminación.
            return false;
        }

        // Pide confirmación Sí/No antes de borrar.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar el almacén seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se elimina nada.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Cancela la eliminación.
            return false;
        }

        // DELETE por id; falla si el almacén tiene inventario, compras o movimientos (llaves RESTRICT).
        boolean eliminado = Almacen.deleteAlmacen(idAlmacen);
        // Si se borró la fila, confirma al usuario.
        if (eliminado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Almacén eliminado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Devuelve el resultado a Catalogos_Form.
        return eliminado;
    }

    // Comprueba los campos obligatorios del almacén.
    private static boolean validarCamposObligatorios(String nombre, String ubicacion) {
        // El nombre no puede ser null ni solo espacios.
        if (nombre == null || nombre.trim().isEmpty()) {
            // Avisa que falta el nombre.
            JOptionPane.showMessageDialog(null, "Ingrese el nombre del almacén", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // La ubicación no puede ser null ni solo espacios.
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            // Avisa que falta la ubicación.
            JOptionPane.showMessageDialog(null, "Ingrese la ubicación del almacén", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // Los campos obligatorios son válidos.
        return true;
    }
}
