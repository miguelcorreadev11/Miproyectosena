// Paquete de controladores del patrón MVC.
package controlador;

// ArrayList devuelve la lista de categorías a la vista.
import java.util.ArrayList;
// JOptionPane muestra validaciones, confirmaciones y resultados.
import javax.swing.JOptionPane;
// Modelo Categoria: ejecuta el CRUD sobre la tabla categorias.
import modelo.Categoria;

// Controlador de la pestaña Categorías de Catalogos_Form.
public class Categoria_control {
    // Valida y registra una categoría nueva; devuelve true si se insertó.
    public static boolean crearCategoria(String nombre, String descripcion, String estado) {
        // Si falta el nombre se cancela la operación.
        if (!validarCamposObligatorios(nombre)) {
            // Detiene el registro.
            return false;
        }

        // Crea el objeto con el nombre sin espacios sobrantes, la descripción y el estado del combo.
        Categoria categoria = new Categoria(nombre.trim(), descripcion, estado);

        // Ejecuta el INSERT en la tabla categorias.
        boolean creado = Categoria.insertCategoria(categoria);
        // Si el INSERT afectó una fila, confirma al usuario.
        if (creado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Categoría registrada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Catalogos_Form refresca la tabla solo si es true.
        return creado;
    }

    // Devuelve todas las categorías para llenar la JTable de Catalogos_Form.
    public static ArrayList<Categoria> listarCategorias() {
        // Delegación directa al SELECT del modelo.
        return Categoria.getCategorias();
    }

    // Valida y actualiza la categoría seleccionada en la tabla; devuelve true si se modificó.
    public static boolean actualizarCategoria(Integer idCategoria, String nombre, String descripcion,
            String estado) {
        // Sin id seleccionado no se sabe qué registro actualizar.
        if (idCategoria == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione una categoría de la tabla para actualizar",
                    "Categoría no seleccionada", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Revalida el nombre obligatorio.
        if (!validarCamposObligatorios(nombre)) {
            // Detiene la actualización.
            return false;
        }

        // Crea el objeto con el id seleccionado; la fecha de creación no se modifica (null).
        Categoria categoria = new Categoria(idCategoria, nombre.trim(), descripcion, estado, null);

        // Ejecuta el UPDATE por id_categoria.
        boolean actualizado = Categoria.updateCategoria(categoria);
        // Si se modificó la fila, confirma al usuario.
        if (actualizado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Categoría actualizada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        // El UPDATE no encontró la fila (fue eliminada o cambiada por otra sesión).
        // Si el false se debe a un error de MySQL, ese error ya se mostró: no se pide recargar ni redigitar.
        } else if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
            // Explica que el registro ya no coincide y cómo recargarlo.
            JOptionPane.showMessageDialog(null,
                    "No se pudo actualizar la categoría: el registro ya no coincide con el que se cargó "
                    + "en el formulario (puede haber sido modificado o eliminado). Cierre y vuelva a "
                    + "abrir el catálogo de categorías, seleccione la categoría nuevamente e intente de nuevo.",
                    "No se aplicaron cambios", JOptionPane.WARNING_MESSAGE);
        }
        // Devuelve el resultado para que la vista decida si refresca la tabla.
        return actualizado;
    }

    // Elimina la categoría seleccionada tras pedir confirmación; devuelve true si se borró.
    public static boolean eliminarCategoria(Integer idCategoria) {
        // Sin id seleccionado no se puede eliminar.
        if (idCategoria == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione una categoría de la tabla para eliminar",
                    "Categoría no seleccionada", JOptionPane.WARNING_MESSAGE);
            // Detiene la eliminación.
            return false;
        }

        // Pide confirmación Sí/No antes de borrar.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar la categoría seleccionada?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se elimina nada.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Cancela la eliminación.
            return false;
        }

        // DELETE por id; falla si hay productos con esa categoría (llave foránea RESTRICT).
        boolean eliminado = Categoria.deleteCategoria(idCategoria);
        // Si se borró la fila, confirma al usuario.
        if (eliminado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Categoría eliminada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Devuelve el resultado a Catalogos_Form.
        return eliminado;
    }

    // Comprueba que el nombre de la categoría no esté vacío.
    private static boolean validarCamposObligatorios(String nombre) {
        // Rechaza null o solo espacios.
        if (nombre == null || nombre.trim().isEmpty()) {
            // Avisa que falta el nombre.
            JOptionPane.showMessageDialog(null, "Ingrese el nombre de la categoría", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Indica validación fallida.
            return false;
        }
        // El nombre es válido.
        return true;
    }
}
