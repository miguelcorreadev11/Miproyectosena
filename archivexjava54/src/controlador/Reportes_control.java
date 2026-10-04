// Paquete de controladores del patrón MVC.
package controlador;

// File representa el archivo .xls que devuelve ExportarExcel.
import java.io.File;
// IOException se captura si falla la escritura del archivo Excel.
import java.io.IOException;
// JOptionPane muestra errores de exportación y avisos de registro.
import javax.swing.JOptionPane;
// JTable es la tabla de la vista que se exporta a Excel.
import javax.swing.JTable;
// Modelo RegistroReporte: guarda cada exportación en la tabla reportes.
import modelo.RegistroReporte;
// Usuario identifica a quién se atribuye el reporte (id_usuario).
import modelo.Usuario;
// Reporte contiene las consultas SELECT de cada tipo de reporte.
import reportes.Reporte;
// ReporteResultado transporta columnas y filas hacia Reportes_Form.
import reportes.ReporteResultado;

// Controlador de Reportes_Form y de la exportación a Excel de Usuarios_Form.
public class Reportes_control {
    // Texto del combo para el reporte de productos por categoría.
    public static final String PRODUCTOS_POR_CATEGORIA = "Productos por categoría";
    // Texto del combo para el reporte de inventario por almacén.
    public static final String INVENTARIO_POR_ALMACEN = "Inventario por almacén";
    // Texto del combo para el listado de categorías.
    public static final String CATEGORIAS = "Categorías registradas";
    // Texto del combo para el listado de proveedores.
    public static final String PROVEEDORES = "Proveedores registrados";
    // Texto del combo para el listado de almacenes.
    public static final String ALMACENES = "Almacenes registrados";
    // Texto del combo para el reporte de compras.
    public static final String COMPRAS = "Compras a proveedores";
    // Texto del combo para el reporte de movimientos de inventario.
    public static final String MOVIMIENTOS = "Movimientos de inventario";
    // Texto del combo para el historial de usuarios.
    public static final String HISTORIAL_USUARIOS = "Historial de usuarios";
    // Texto del combo para el listado de la tabla reportes.
    public static final String REPORTES_GENERADOS = "Reportes generados";

    // Opciones del combo jComboBoxTipoReporte en el orden en que se muestran.
    public static String[] getTiposDeReporte() {
        // Arreglo que Reportes_Form recorre para llenar el combo.
        return new String[]{
            PRODUCTOS_POR_CATEGORIA,
            INVENTARIO_POR_ALMACEN,
            CATEGORIAS,
            PROVEEDORES,
            ALMACENES,
            COMPRAS,
            MOVIMIENTOS,
            HISTORIAL_USUARIOS,
            REPORTES_GENERADOS
        };
    }

    // Ejecuta la consulta del tipo elegido; devuelve null si el tipo es null o desconocido.
    public static ReporteResultado generarReporte(String tipoReporte) {
        // Sin tipo seleccionado no hay reporte que generar.
        if (tipoReporte == null) {
            // Reportes_Form no cambia la tabla si recibe null.
            return null;
        }

        // Elige la consulta de reportes.Reporte según el texto del combo.
        switch (tipoReporte) {
            // Productos con su categoría.
            case PRODUCTOS_POR_CATEGORIA:
                // SELECT productos + categorias.
                return Reporte.reporteProductosPorCategoria();
            // Cantidades por almacén.
            case INVENTARIO_POR_ALMACEN:
                // SELECT inventarioporalmacen + productos + almacenes.
                return Reporte.reporteInventarioPorAlmacen();
            // Listado de categorías.
            case CATEGORIAS:
                // SELECT categorias.
                return Reporte.reporteCategorias();
            // Listado de proveedores.
            case PROVEEDORES:
                // SELECT proveedores.
                return Reporte.reporteProveedores();
            // Listado de almacenes.
            case ALMACENES:
                // SELECT almacenes.
                return Reporte.reporteAlmacenes();
            // Compras con proveedor y almacén.
            case COMPRAS:
                // SELECT compras + proveedores + almacenes.
                return Reporte.reporteCompras();
            // Movimientos de inventario.
            case MOVIMIENTOS:
                // SELECT movimientoinventario + productos + almacenes + usuarios + compras.
                return Reporte.reporteMovimientos();
            // Historial de cambios de usuarios.
            case HISTORIAL_USUARIOS:
                // SELECT historialusuarios + usuarios.
                return Reporte.reporteHistorialUsuarios();
            // Reportes exportados previamente.
            case REPORTES_GENERADOS:
                // SELECT reportes + usuarios.
                return Reporte.reporteRegistroReportes();
            // Tipo no reconocido.
            default:
                // La vista no modifica la tabla.
                return null;
        }
    }

    // Traduce el texto del combo al valor del ENUM reportes.tipo_reporte de MySQL.
    public static String tipoParaBaseDeDatos(String tipoReporte) {
        // Sin tipo se clasifica como 'general'.
        if (tipoReporte == null) {
            // Valor por defecto válido del ENUM.
            return RegistroReporte.TIPO_GENERAL;
        }
        // Relaciona cada opción de la vista con su tipo en la base de datos.
        switch (tipoReporte) {
            // Productos por categoría se registra como 'productos'.
            case PRODUCTOS_POR_CATEGORIA:
                // Valor 'productos' del ENUM.
                return RegistroReporte.TIPO_PRODUCTOS;
            // Inventario por almacén se registra como 'inventario'.
            case INVENTARIO_POR_ALMACEN:
                // Valor 'inventario' del ENUM.
                return RegistroReporte.TIPO_INVENTARIO;
            // Proveedores se registra como 'proveedores'.
            case PROVEEDORES:
                // Valor 'proveedores' del ENUM.
                return RegistroReporte.TIPO_PROVEEDORES;
            // Compras se registra como 'compras'.
            case COMPRAS:
                // Valor 'compras' del ENUM.
                return RegistroReporte.TIPO_COMPRAS;
            // Movimientos se registra como 'movimientos'.
            case MOVIMIENTOS:
                // Valor 'movimientos' del ENUM.
                return RegistroReporte.TIPO_MOVIMIENTOS;
            // Categorías, almacenes, historial, usuarios y otros no tienen tipo propio en el ENUM.
            default:
                // Se registran como 'general'.
                return RegistroReporte.TIPO_GENERAL;
        }
    }

    // Exporta la JTable a Excel con ExportarExcel y registra el resultado en la tabla reportes;
    // devuelve true si el archivo quedó guardado.
    public static boolean exportarYRegistrar(JTable tabla, String titulo) {
        // Archivo creado; null si el usuario canceló el diálogo de guardar.
        File archivo;
        try {
            // Abre el JFileChooser, escribe el .xls con Apache POI y lo abre con el programa predeterminado.
            archivo = new ExportarExcel().exportarExcel(tabla);
        // Error al escribir el archivo o fallo inesperado de POI.
        } catch (IOException | RuntimeException ex) {
            // Informa el error de exportación.
            JOptionPane.showMessageDialog(null, "No se pudo exportar el archivo: " + ex.getMessage(),
                    "Error al exportar", JOptionPane.ERROR_MESSAGE);
            // Registra el intento fallido con estado 'error' y sin ruta de archivo.
            registrarReporte(titulo, null, RegistroReporte.ESTADO_ERROR);
            // La exportación no se completó.
            return false;
        }
        // El usuario canceló el diálogo: no se crea archivo ni registro.
        if (archivo == null) {
            // No hubo exportación.
            return false;
        }
        // Registra la exportación exitosa con la ruta absoluta del archivo y estado 'generado'.
        registrarReporte(titulo, archivo.getAbsolutePath(), RegistroReporte.ESTADO_GENERADO);
        // El archivo quedó guardado.
        return true;
    }

    // Inserta el registro en la tabla reportes con el usuario de la sesión.
    private static void registrarReporte(String titulo, String ruta, String estado) {
        // Obtiene el usuario autenticado desde Login_control.
        Usuario usuario = Login_control.getUsuarioSesion();
        // reportes.id_usuario es NOT NULL: sin sesión no se puede registrar.
        if (usuario == null) {
            // Avisa que el archivo se generó pero no quedó registrado.
            JOptionPane.showMessageDialog(null, "El reporte no se registró en la base de datos porque no hay "
                    + "una sesión iniciada.", "Reporte sin registrar", JOptionPane.WARNING_MESSAGE);
            // Termina sin registrar.
            return;
        }
        // Crea el registro con título, tipo del ENUM, usuario, formato 'excel', ruta y estado.
        RegistroReporte registro = new RegistroReporte(titulo, tipoParaBaseDeDatos(titulo),
                usuario.getIdUsuario(), RegistroReporte.FORMATO_EXCEL, ruta, estado);
        // Ejecuta el INSERT en la tabla reportes.
        RegistroReporte.insertReporte(registro);
    }
}
