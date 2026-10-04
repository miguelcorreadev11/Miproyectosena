// Paquete de controladores del patrón MVC.
package controlador;

// ArrayList devuelve los registros de inventario a la vista.
import java.util.ArrayList;
// JOptionPane muestra validaciones, confirmaciones y resultados.
import javax.swing.JOptionPane;
// Modelo InventarioPorAlmacen: CRUD de la tabla inventarioporalmacen.
import modelo.InventarioPorAlmacen;

// Controlador de la pestaña Inventario de Productos_Inventario_Form; edita cantidades directamente
// (sin movimiento ni historial, a diferencia de Movimiento_control).
public class Inventario_control {
    // Valida y crea el registro de inventario de un producto en un almacén.
    public static boolean registrarInventario(Integer idProducto, Integer idAlmacen,
            Integer cantidadDisponible, Integer cantidadReservada) {
        // Exige producto, almacén y cantidades no negativas.
        if (!validarCampos(idProducto, idAlmacen, cantidadDisponible, cantidadReservada)) {
            // Detiene el registro.
            return false;
        }

        // El par (id_producto, id_almacen) es UNIQUE en la tabla; se valida antes del INSERT.
        if (InventarioPorAlmacen.existeProductoAlmacen(idProducto, idAlmacen)) {
            // Avisa que ese producto ya tiene inventario en ese almacén.
            JOptionPane.showMessageDialog(null,
                    "Ya existe un registro de inventario para ese producto en ese almacén",
                    "Registro duplicado", JOptionPane.WARNING_MESSAGE);
            // Detiene el registro.
            return false;
        }
        // Si la verificación del duplicado falló por MySQL (error ya informado), no se intenta el INSERT.
        if (config.conexión_BD.huboErrorEnUltimaOperacion()) {
            return false;
        }

        // Crea el registro nuevo con las cantidades del formulario.
        InventarioPorAlmacen inventario = new InventarioPorAlmacen(idProducto, idAlmacen,
                cantidadDisponible, cantidadReservada);

        // Ejecuta el INSERT en inventarioporalmacen.
        boolean registrado = InventarioPorAlmacen.insertInventario(inventario);
        // Si se insertó, confirma al usuario.
        if (registrado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Inventario registrado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // La vista refresca la tabla si es true.
        return registrado;
    }

    // Devuelve el inventario con nombres de producto y almacén para la JTable.
    public static ArrayList<InventarioPorAlmacen> listarInventario() {
        // SELECT con JOIN a productos y almacenes.
        return InventarioPorAlmacen.getInventarioDetallado();
    }

    // Valida y actualiza el registro de inventario seleccionado.
    public static boolean actualizarInventario(Integer idInventario, Integer idProducto, Integer idAlmacen,
            Integer cantidadDisponible, Integer cantidadReservada) {
        // Sin id seleccionado no se sabe qué registro actualizar.
        if (idInventario == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla para actualizar",
                    "Registro no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Revalida producto, almacén y cantidades.
        if (!validarCampos(idProducto, idAlmacen, cantidadDisponible, cantidadReservada)) {
            // Detiene la actualización.
            return false;
        }

        // Busca si ya existe un registro para el par producto-almacén elegido.
        InventarioPorAlmacen existente = InventarioPorAlmacen.getInventarioPorProductoYAlmacen(idProducto,
                idAlmacen);
        // Si la consulta falló por MySQL (error ya informado), no se intenta el UPDATE.
        if (config.conexión_BD.huboErrorEnUltimaOperacion()) {
            return false;
        }
        // Si existe y es otro registro distinto al editado, se violaría el índice UNIQUE.
        if (existente != null && !existente.getIdInventario().equals(idInventario)) {
            // Avisa del duplicado.
            JOptionPane.showMessageDialog(null,
                    "Ya existe otro registro de inventario para ese producto en ese almacén",
                    "Registro duplicado", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Crea el objeto con el id seleccionado; la fecha la actualiza MySQL (ON UPDATE CURRENT_TIMESTAMP).
        InventarioPorAlmacen inventario = new InventarioPorAlmacen(idInventario, idProducto, idAlmacen,
                cantidadDisponible, cantidadReservada, null);

        // Ejecuta el UPDATE por id_inventario.
        boolean actualizado = InventarioPorAlmacen.updateInventario(inventario);
        // Si se modificó, confirma al usuario.
        if (actualizado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Inventario actualizado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Devuelve el resultado a la vista.
        return actualizado;
    }

    // Elimina el registro de inventario seleccionado tras pedir confirmación.
    public static boolean eliminarInventario(Integer idInventario) {
        // Sin id seleccionado no se puede eliminar.
        if (idInventario == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla para eliminar",
                    "Registro no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la eliminación.
            return false;
        }

        // Pide confirmación Sí/No antes de borrar.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar el registro de inventario seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se elimina nada.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Cancela la eliminación.
            return false;
        }

        // Ejecuta el DELETE por id_inventario.
        boolean eliminado = InventarioPorAlmacen.deleteInventario(idInventario);
        // Si se borró, confirma al usuario.
        if (eliminado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Registro de inventario eliminado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Devuelve el resultado a la vista.
        return eliminado;
    }

    // Comprueba los datos del formulario de inventario.
    private static boolean validarCampos(Integer idProducto, Integer idAlmacen, Integer cantidadDisponible,
            Integer cantidadReservada) {
        // Debe haber un producto seleccionado en el combo.
        if (idProducto == null) {
            // Avisa que falta el producto.
            JOptionPane.showMessageDialog(null, "Seleccione un producto", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // Debe haber un almacén seleccionado en el combo.
        if (idAlmacen == null) {
            // Avisa que falta el almacén.
            JOptionPane.showMessageDialog(null, "Seleccione un almacén", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // La cantidad disponible es obligatoria y no puede ser negativa (columna UNSIGNED).
        if (cantidadDisponible == null || cantidadDisponible < 0) {
            // Avisa que la cantidad disponible no es válida.
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad disponible válida", "Dato inválido",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // La cantidad reservada es obligatoria y no puede ser negativa (columna UNSIGNED).
        if (cantidadReservada == null || cantidadReservada < 0) {
            // Avisa que la cantidad reservada no es válida.
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad reservada válida", "Dato inválido",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // Datos válidos.
        return true;
    }
}
