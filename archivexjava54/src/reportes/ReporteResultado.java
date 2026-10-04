// Paquete de reportes: consultas de solo lectura y su resultado listo para una JTable.
package reportes;

// List guarda las filas devueltas por la consulta del reporte.
import java.util.List;

// Objeto de transferencia que Reporte llena y Reportes_Form muestra en su JTable.
public class ReporteResultado {
    // Encabezados de columna que usará el DefaultTableModel de Reportes_Form.
    private final String[] columnas;
    // Filas del reporte; cada Object[] corresponde a un registro del ResultSet.
    private final List<Object[]> filas;

    // Recibe columnas y filas ya consultadas en Reporte.ejecutarConsulta().
    public ReporteResultado(String[] columnas, List<Object[]> filas) {
        // Guarda los nombres de columna del reporte.
        this.columnas = columnas;
        // Guarda la lista de filas del reporte.
        this.filas = filas;
    }

    // Devuelve los encabezados; Reportes_Form los usa para crear el modelo de la tabla.
    public String[] getColumnas() {
        return columnas;
    }

    // Devuelve las filas; Reportes_Form las agrega una por una al modelo de la tabla.
    public List<Object[]> getFilas() {
        return filas;
    }

    // Cantidad de filas del reporte; 0 si la lista es null.
    public int getCantidadFilas() {
        // Evita NullPointerException cuando no se cargaron filas.
        return filas == null ? 0 : filas.size();
    }
}
