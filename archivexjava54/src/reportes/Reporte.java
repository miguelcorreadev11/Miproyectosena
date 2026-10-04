// Paquete de reportes: agrupa consultas que combinan varias tablas y no pertenecen a un solo modelo.
package reportes;

// Connection se obtiene de config.conexión_BD, la conexión compartida del proyecto.
import java.sql.Connection;
// PreparedStatement ejecuta cada SELECT de reporte.
import java.sql.PreparedStatement;
// ResultSet recorre las filas devueltas por MySQL.
import java.sql.ResultSet;
// SQLException se captura para informar errores de consulta sin cerrar la ventana.
import java.sql.SQLException;
// ArrayList acumula las filas del reporte.
import java.util.ArrayList;
// List es el tipo que recibe ReporteResultado.
import java.util.List;
// Level indica la gravedad al registrar errores en el Logger.
import java.util.logging.Level;
// Logger registra en consola las excepciones SQL.
import java.util.logging.Logger;
// JOptionPane muestra al usuario los errores de conexión o de consulta.
import javax.swing.JOptionPane;

// Consultas SELECT de los reportes que Reportes_control ofrece en el combo de Reportes_Form.
public class Reporte {
    // Constructor privado: la clase solo expone métodos estáticos y no se instancia.
    private Reporte() {
    }

    // Reporte 'Productos por categoría': une productos con categorias por id_categoria.
    public static ReporteResultado reporteProductosPorCategoria() {
        // Encabezados en el mismo orden que las columnas del SELECT.
        String[] columnas = {"Producto", "Categoría", "P. Compra", "P. Venta", "Estado"};
        // Trae nombre, categoría, precios y estado de cada producto ordenados por categoría y nombre.
        String query = "SELECT p.nombre, c.nombre AS categoria, p.precio_compra, "
                + "p.precio_venta, p.estado "
                + "FROM productos p INNER JOIN categorias c ON p.id_categoria = c.id_categoria "
                + "ORDER BY c.nombre, p.nombre";

        // Ejecuta la consulta y devuelve el ReporteResultado para la JTable.
        return ejecutarConsulta(columnas, query, "productos por categoría");
    }

    // Reporte 'Inventario por almacén': cantidades de inventarioporalmacen con nombres de producto y almacén.
    public static ReporteResultado reporteInventarioPorAlmacen() {
        // Encabezados que verá el usuario en la JTable de Reportes_Form.
        String[] columnas = {"Producto", "Almacén", "Cant. Disponible", "Cant. Reservada", "Última actualización"};
        // Une inventarioporalmacen con productos y almacenes para mostrar nombres en lugar de ids.
        String query = "SELECT p.nombre AS producto, a.nombre AS almacen, i.cantidad_disponible, "
                + "i.cantidad_reservada, i.fecha_de_actualizacion "
                + "FROM inventarioporalmacen i "
                + "INNER JOIN productos p ON i.id_producto = p.id_producto "
                + "INNER JOIN almacenes a ON i.id_almacen = a.id_almacen "
                + "ORDER BY a.nombre, p.nombre";

        // Ejecuta la consulta del inventario por almacén.
        return ejecutarConsulta(columnas, query, "inventario por almacén");
    }

    // Reporte 'Categorías registradas': lista la tabla categorias.
    public static ReporteResultado reporteCategorias() {
        // Encabezados de la tabla de categorías.
        String[] columnas = {"Nombre", "Descripción", "Estado"};
        // Consulta nombre, descripción y estado de todas las categorías ordenadas por nombre.
        String query = "SELECT nombre, descripcion, estado FROM categorias ORDER BY nombre";

        // Ejecuta la consulta de categorías.
        return ejecutarConsulta(columnas, query, "categorías");
    }

    // Reporte 'Proveedores registrados': datos de contacto de la tabla proveedores.
    public static ReporteResultado reporteProveedores() {
        // Encabezados de la tabla de proveedores.
        String[] columnas = {"Nombre", "Correo", "Teléfono", "Ciudad", "País", "Estado"};
        // Consulta los datos de contacto y el estado de cada proveedor.
        String query = "SELECT nombre, correo, telefono, ciudad, pais, estado "
                + "FROM proveedores ORDER BY nombre";

        // Ejecuta la consulta de proveedores.
        return ejecutarConsulta(columnas, query, "proveedores");
    }

    // Reporte 'Almacenes registrados': lista la tabla almacenes.
    public static ReporteResultado reporteAlmacenes() {
        // Encabezados de la tabla de almacenes.
        String[] columnas = {"Nombre", "Ubicación", "Ciudad", "Capacidad máxima", "Estado"};
        // Consulta ubicación, ciudad, capacidad y estado de cada almacén.
        String query = "SELECT nombre, ubicacion, ciudad, capacidad_maxima, estado "
                + "FROM almacenes ORDER BY nombre";

        // Ejecuta la consulta de almacenes.
        return ejecutarConsulta(columnas, query, "almacenes");
    }

    // Reporte 'Compras a proveedores': compras con proveedor, almacén y número de líneas del detalle.
    public static ReporteResultado reporteCompras() {
        // Encabezados del reporte de compras.
        String[] columnas = {"N° pedido", "Proveedor", "Almacén", "Fecha pedido", "Fecha entrega",
            "Productos", "Valor total", "Estado"};
        // Une compras con proveedores y almacenes; la subconsulta cuenta las filas de detallecompra
        // de cada compra. Ordena de la compra más reciente a la más antigua.
        String query = "SELECT c.numero_pedido, p.nombre AS proveedor, a.nombre AS almacen, c.fecha_pedido, "
                + "c.fecha_entrega, (SELECT COUNT(*) FROM detallecompra d WHERE d.id_compra = c.id_compra), "
                + "c.valor_total, c.estado "
                + "FROM compras c "
                + "INNER JOIN proveedores p ON c.id_proveedor = p.id_proveedor "
                + "INNER JOIN almacenes a ON c.id_almacen = a.id_almacen "
                + "ORDER BY c.fecha_pedido DESC";

        // Ejecuta la consulta de compras.
        return ejecutarConsulta(columnas, query, "compras");
    }

    // Reporte 'Movimientos de inventario': movimientos con producto, almacén, usuario y pedido.
    public static ReporteResultado reporteMovimientos() {
        // Encabezados del reporte de movimientos.
        String[] columnas = {"Fecha", "Producto", "Almacén", "Tipo", "Cantidad", "N° pedido", "Usuario", "Motivo"};
        // Une movimientoinventario con productos, almacenes y usuarios; LEFT JOIN a compras porque
        // id_compra solo existe en las entradas generadas al entregar una compra.
        String query = "SELECT m.fecha_movimiento, p.nombre AS producto, a.nombre AS almacen, "
                + "m.tipo_movimiento, m.cantidad, c.numero_pedido, "
                + "CONCAT(u.nombre, ' ', u.apellido) AS usuario, m.motivo "
                + "FROM movimientoinventario m "
                + "INNER JOIN productos p ON m.id_producto = p.id_producto "
                + "INNER JOIN almacenes a ON m.id_almacen = a.id_almacen "
                + "INNER JOIN usuarios u ON m.id_usuario = u.id_usuario "
                + "LEFT JOIN compras c ON m.id_compra = c.id_compra "
                + "ORDER BY m.fecha_movimiento DESC";

        // Ejecuta la consulta de movimientos.
        return ejecutarConsulta(columnas, query, "movimientos");
    }

    // Reporte 'Historial de usuarios': acciones registradas por modelo.Usuario en historialusuarios.
    public static ReporteResultado reporteHistorialUsuarios() {
        // Encabezados con los valores anteriores y nuevos de cada cambio.
        String[] columnas = {"Fecha", "Usuario", "Acción", "Nombre anterior", "Nombre nuevo",
            "Correo anterior", "Correo nuevo", "Teléfono anterior", "Teléfono nuevo", "Estado anterior",
            "Estado nuevo"};
        // Une historialusuarios con usuarios; COALESCE y TRIM arman el nombre completo anterior
        // y nuevo aunque alguno de los campos sea NULL.
        String query = "SELECT h.fecha_accion, CONCAT(u.nombre, ' ', u.apellido) AS usuario, h.accion, "
                + "TRIM(CONCAT(COALESCE(h.nombre_anterior, ''), ' ', COALESCE(h.apellido_anterior, ''))), "
                + "TRIM(CONCAT(COALESCE(h.nombre_nuevo, ''), ' ', COALESCE(h.apellido_nuevo, ''))), "
                + "h.correo_anterior, h.correo_nuevo, h.telefono_anterior, h.telefono_nuevo, "
                + "h.estado_anterior, h.estado_nuevo "
                + "FROM historialusuarios h INNER JOIN usuarios u ON h.id_usuario = u.id_usuario "
                + "ORDER BY h.fecha_accion DESC, h.id_historial_usuario DESC";

        // Ejecuta la consulta del historial de usuarios.
        return ejecutarConsulta(columnas, query, "historial de usuarios");
    }

    // Reporte 'Reportes generados': registros de la tabla reportes que crea modelo.RegistroReporte.
    public static ReporteResultado reporteRegistroReportes() {
        // Encabezados del listado de reportes exportados.
        String[] columnas = {"Fecha", "Título", "Tipo", "Usuario", "Formato", "Estado", "Archivo"};
        // Une reportes con usuarios para mostrar quién exportó cada archivo, del más reciente al más antiguo.
        String query = "SELECT r.fecha_generacion, r.titulo, r.tipo_reporte, "
                + "CONCAT(u.nombre, ' ', u.apellido) AS usuario, r.formato, r.estado, r.ruta_archivo "
                + "FROM reportes r INNER JOIN usuarios u ON r.id_usuario = u.id_usuario "
                + "ORDER BY r.fecha_generacion DESC, r.id_reporte DESC";

        // Ejecuta la consulta de reportes generados.
        return ejecutarConsulta(columnas, query, "reportes generados");
    }

    // Ejecuta un SELECT de reporte y convierte cada fila del ResultSet en un Object[].
    private static ReporteResultado ejecutarConsulta(String[] columnas, String query, String nombreReporte) {
        // Lista vacía que se devuelve también si falla la conexión o la consulta.
        List<Object[]> filas = new ArrayList<>();
        // Obtiene la conexión compartida de config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // Sin conexión con MySQL no se puede generar el reporte.
        if (con == null) {
            // Informa al usuario que MySQL no está disponible.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Devuelve un resultado vacío para que la vista no reciba null.
            return new ReporteResultado(columnas, filas);
        }

        // try-with-resources cierra PreparedStatement y ResultSet; la conexión compartida queda abierta.
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Número de columnas esperadas según los encabezados del reporte.
            int cantidadColumnas = columnas.length;
            // Recorre cada fila devuelta por MySQL.
            while (rs.next()) {
                // Arreglo que representa una fila de la JTable.
                Object[] fila = new Object[cantidadColumnas];
                // Copia cada columna por posición (1-based en JDBC).
                for (int i = 0; i < cantidadColumnas; i++) {
                    // getObject conserva el tipo original (texto, número, fecha) de la columna.
                    fila[i] = rs.getObject(i + 1);

                    // Connector/J 8 devuelve DATETIME como LocalDateTime; se detecta para darle formato legible.
                    if (fila[i] instanceof java.time.LocalDateTime) {
                        // Convierte la fecha a 'AAAA-MM-DD HH:MM:SS' en lugar del formato ISO con 'T'.
                        fila[i] = ((java.time.LocalDateTime) fila[i]).format(
                                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                }
                // Agrega la fila al resultado.
                filas.add(fila);
            }
        // Error de SQL (tabla o columna inexistente, conexión perdida): se registra y se informa.
        } catch (SQLException ex) {
            // Registra la traza en la consola de NetBeans.
            Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, ex);
            // Muestra al usuario qué reporte falló y el mensaje de MySQL.
            JOptionPane.showMessageDialog(null, "Error al generar el reporte de " + nombreReporte + ": "
                    + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }

        // Devuelve columnas y filas para que Reportes_Form las muestre.
        return new ReporteResultado(columnas, filas);
    }
}
