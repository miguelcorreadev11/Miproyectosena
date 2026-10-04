// Paquete de controladores del patrón MVC.
package controlador;

// BigDecimal representa los precios exactos (columnas DECIMAL(10,2)).
import java.math.BigDecimal;
// ArrayList devuelve la lista de productos a la vista.
import java.util.ArrayList;
// JOptionPane muestra validaciones, confirmaciones y resultados.
import javax.swing.JOptionPane;
// Categoria se usa para verificar que la categoría elegida exista en MySQL.
import modelo.Categoria;
// Modelo Producto: CRUD de la tabla productos, incluida la columna imagen.
import modelo.Producto;

// Controlador de la pestaña Productos de Productos_Inventario_Form.
public class Producto_control {
    // Valida y registra un producto; 'imagen' es el nombre de archivo que devolvió
    // ImagenProducto.copiarImagen o null si el producto no tiene imagen.
    public static boolean crearProducto(String nombre, String descripcion,
            Integer idCategoria, BigDecimal precioCompra, BigDecimal precioVenta, Integer cantidad,
            String estado, String imagen) {
        // Nombre, categoría y precios son obligatorios.
        if (!validarCamposObligatorios(nombre, idCategoria, precioCompra, precioVenta)) {
            // Detiene el registro.
            return false;
        }

        // Evita violar la llave foránea fk_productos_categoria con una categoría inexistente.
        if (!Categoria.existeCategoria(idCategoria)) {
            // Si la consulta falló por MySQL, ese error ya se informó y la categoría no es el problema.
            if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
                // Avisa que la categoría no existe.
                JOptionPane.showMessageDialog(null, "La categoría seleccionada no existe",
                        "Categoría inválida", JOptionPane.WARNING_MESSAGE);
            }
            // Detiene el registro.
            return false;
        }

        // Crea el producto con los datos del formulario y el nombre sin espacios sobrantes.
        Producto producto = new Producto(nombre.trim(), descripcion, idCategoria,
                precioCompra, precioVenta, cantidad, estado);
        // Asigna el nombre del archivo de imagen que se guardará en productos.imagen.
        producto.setImagen(imagen);

        // Ejecuta el INSERT en la tabla productos.
        boolean creado = Producto.insertProducto(producto);
        // Si se insertó, confirma al usuario.
        if (creado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Producto registrado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // La vista refresca tablas y combos solo si es true.
        return creado;
    }

    // Devuelve los productos con el nombre de su categoría para la JTable de productos.
    public static ArrayList<Producto> listarProductos() {
        // SELECT con INNER JOIN a categorias.
        return Producto.getProductosConCategoria();
    }

    // Valida y actualiza el producto seleccionado, incluida su imagen.
    public static boolean actualizarProducto(Integer idProducto, String nombre,
            String descripcion, Integer idCategoria, BigDecimal precioCompra, BigDecimal precioVenta,
            Integer cantidad, String estado, String imagen) {
        // Sin id seleccionado no se sabe qué registro actualizar.
        if (idProducto == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla para actualizar",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la actualización.
            return false;
        }

        // Revalida los campos obligatorios.
        if (!validarCamposObligatorios(nombre, idCategoria, precioCompra, precioVenta)) {
            // Detiene la actualización.
            return false;
        }

        // Verifica que la categoría siga existiendo.
        if (!Categoria.existeCategoria(idCategoria)) {
            // Si la consulta falló por MySQL, ese error ya se informó y la categoría no es el problema.
            if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
                // Avisa que la categoría no existe.
                JOptionPane.showMessageDialog(null, "La categoría seleccionada no existe",
                        "Categoría inválida", JOptionPane.WARNING_MESSAGE);
            }
            // Detiene la actualización.
            return false;
        }

        // Crea el objeto con el id seleccionado; la fecha de creación no se modifica (null).
        Producto producto = new Producto(idProducto, nombre.trim(), descripcion,
                idCategoria, precioCompra, precioVenta, cantidad, estado, null);
        // Conserva, cambia o quita (null) la imagen según lo elegido en el formulario.
        producto.setImagen(imagen);

        // Ejecuta el UPDATE por id_producto.
        boolean actualizado = Producto.updateProducto(producto);
        // Si se modificó la fila, confirma al usuario.
        if (actualizado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Producto actualizado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        // El UPDATE no encontró la fila (fue eliminada o cambiada por otra sesión).
        // Si el false se debe a un error de MySQL, ese error ya se mostró: no se pide recargar ni redigitar.
        } else if (!config.conexión_BD.huboErrorEnUltimaOperacion()) {
            // Explica que el registro ya no coincide y cómo recargarlo.
            JOptionPane.showMessageDialog(null,
                    "No se pudo actualizar el producto: el registro ya no coincide con el que se cargó "
                    + "en el formulario (puede haber sido modificado o eliminado). Cierre y vuelva a "
                    + "abrir el módulo de productos, seleccione el producto nuevamente e intente de nuevo.",
                    "No se aplicaron cambios", JOptionPane.WARNING_MESSAGE);
        }
        // Devuelve el resultado a la vista.
        return actualizado;
    }

    // Elimina el producto seleccionado tras pedir confirmación.
    public static boolean eliminarProducto(Integer idProducto) {
        // Sin id seleccionado no se puede eliminar.
        if (idProducto == null) {
            // Pide seleccionar una fila de la tabla.
            JOptionPane.showMessageDialog(null, "Seleccione un producto de la tabla para eliminar",
                    "Producto no seleccionado", JOptionPane.WARNING_MESSAGE);
            // Detiene la eliminación.
            return false;
        }

        // Pide confirmación Sí/No antes de borrar.
        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de eliminar el producto seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        // Si el usuario no eligió Sí, no se elimina nada.
        if (confirmacion != JOptionPane.YES_OPTION) {
            // Cancela la eliminación.
            return false;
        }

        // DELETE por id; falla si el producto tiene inventario, compras o movimientos (llaves RESTRICT).
        boolean eliminado = Producto.deleteProducto(idProducto);
        // Si se borró, confirma al usuario.
        if (eliminado) {
            // Mensaje de éxito.
            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        // Devuelve el resultado a la vista.
        return eliminado;
    }

    // Comprueba los campos obligatorios del producto.
    private static boolean validarCamposObligatorios(String nombre, Integer idCategoria,
            BigDecimal precioCompra, BigDecimal precioVenta) {
        // El nombre no puede ser null ni solo espacios.
        if (nombre == null || nombre.trim().isEmpty()) {
            // Avisa que falta el nombre.
            JOptionPane.showMessageDialog(null, "Ingrese el nombre del producto", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // Debe haber una categoría seleccionada en el combo.
        if (idCategoria == null) {
            // Avisa que falta la categoría.
            JOptionPane.showMessageDialog(null, "Seleccione una categoría", "Campo obligatorio",
                    JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // La vista envía null cuando un precio no se pudo convertir a número.
        if (precioCompra == null || precioVenta == null) {
            // Avisa que los precios deben ser numéricos.
            JOptionPane.showMessageDialog(null, "Los precios de compra y venta deben ser numéricos",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            // Validación fallida.
            return false;
        }
        // Datos válidos.
        return true;
    }
}
