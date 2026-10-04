// Paquete de controladores del patrón MVC.
package controlador;

// ArrayList transporta los resultados hacia Busqueda_Productos_Form.
import java.util.ArrayList;
// Categoria se usa para el combo de filtro por categoría.
import modelo.Categoria;
// InventarioPorAlmacen aporta las existencias del producto por almacén.
import modelo.InventarioPorAlmacen;
// Producto contiene la consulta de búsqueda con filtros.
import modelo.Producto;

// Controlador de Busqueda_Productos_Form: traduce los filtros de la vista a la consulta del modelo.
public class BusquedaProductos_control {
    // Texto de la opción que significa 'sin filtro' en los combos de la vista.
    public static final String FILTRO_TODOS = "Todos";

    // Opciones del combo de estado; 'activo' y 'descontinuado' son los valores del ENUM de productos.estado.
    public static String[] getFiltrosEstado() {
        // Arreglo con la opción 'Todos' seguida de los estados posibles.
        return new String[]{FILTRO_TODOS, "activo", "descontinuado"};
    }

    // Categorías para el combo de filtro de la búsqueda.
    public static ArrayList<Categoria> listarCategorias() {
        // Consulta la tabla categorias mediante el modelo.
        return Categoria.getCategorias();
    }

    // Busca productos por texto libre, categoría (null = todas) y estado ('Todos' = sin filtro).
    public static ArrayList<Producto> buscar(String texto, Categoria categoria, String estado) {
        // Convierte la categoría en su id y 'Todos' en null antes de llamar a
        // Producto.buscarProductos, que arma el SELECT parametrizado.
        return Producto.buscarProductos(texto,
                categoria == null ? null : categoria.getIdCategoria(),
                FILTRO_TODOS.equals(estado) ? null : estado);
    }

    // Existencias del producto en cada almacén para la tabla lateral del detalle.
    public static ArrayList<InventarioPorAlmacen> existenciasPorAlmacen(Integer idProducto) {
        // Lista resultado; vacía si no hay producto seleccionado.
        ArrayList<InventarioPorAlmacen> existencias = new ArrayList<>();
        // Sin producto no hay existencias que mostrar.
        if (idProducto == null) {
            // Devuelve la lista vacía.
            return existencias;
        }
        // Recorre todo el inventario con nombres de producto y almacén (JOIN hecho por el modelo).
        for (InventarioPorAlmacen inventario : InventarioPorAlmacen.getInventarioDetallado()) {
            // Conserva solo los registros del producto seleccionado.
            if (idProducto.equals(inventario.getIdProducto())) {
                // Agrega el registro de ese almacén.
                existencias.add(inventario);
            }
        }
        // Devuelve las existencias filtradas.
        return existencias;
    }
}
