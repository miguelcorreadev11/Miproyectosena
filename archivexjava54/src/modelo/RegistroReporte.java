// Paquete modelo: clases que representan tablas de MySQL y ejecutan su SQL con JDBC.
package modelo;

// Connection: conexión JDBC compartida que entrega config.conexión_BD.
import java.sql.Connection;
// PreparedStatement: sentencias SQL parametrizadas (evitan inyección SQL).
import java.sql.PreparedStatement;
// SQLException: errores reportados por MySQL o por el driver JDBC.
import java.sql.SQLException;
// Level.SEVERE marca la gravedad de los errores registrados.
import java.util.logging.Level;
// Logger: escribe las excepciones en la consola de NetBeans.
import java.util.logging.Logger;
// JOptionPane: cuadros de diálogo de error, aviso o confirmación.
import javax.swing.JOptionPane;

// Modelo de la tabla reportes: registra cada reporte exportado a archivo. Se llama RegistroReporte
// para no confundirse con reportes.Reporte, que contiene las consultas de los reportes.
public class RegistroReporte {
    // Valor 'inventario' del ENUM tipo_reporte.
    public static final String TIPO_INVENTARIO = "inventario";
    // Valor 'compras' del ENUM tipo_reporte.
    public static final String TIPO_COMPRAS = "compras";
    // Valor 'movimientos' del ENUM tipo_reporte.
    public static final String TIPO_MOVIMIENTOS = "movimientos";
    // Valor 'proveedores' del ENUM tipo_reporte.
    public static final String TIPO_PROVEEDORES = "proveedores";
    // Valor 'productos' del ENUM tipo_reporte.
    public static final String TIPO_PRODUCTOS = "productos";
    // Valor 'general' para reportes sin tipo propio en el ENUM.
    public static final String TIPO_GENERAL = "general";

    // Valor 'excel' del ENUM formato (ExportarExcel genera archivos .xls).
    public static final String FORMATO_EXCEL = "excel";
    // Valor 'generado' del ENUM estado: el archivo se creó.
    public static final String ESTADO_GENERADO = "generado";
    // Valor 'error' del ENUM estado: la exportación falló.
    public static final String ESTADO_ERROR = "error";

    // Columna id_reporte (llave primaria AUTO_INCREMENT).
    private Integer idReporte;
    // Columna titulo VARCHAR(150).
    private String titulo;
    // Columna tipo_reporte.
    private String tipoReporte;
    // Columna id_usuario (llave foránea fk_reportes_usuario, obligatoria).
    private Integer idUsuario;
    // Columna formato.
    private String formato;
    // Columna ruta_archivo VARCHAR(255).
    private String rutaArchivo;
    // Columna estado.
    private String estado;

    // Constructor vacío.
    public RegistroReporte() {
    }

    // Constructor para un registro nuevo; el id y fecha_generacion los asigna MySQL.
    public RegistroReporte(String titulo, String tipoReporte, Integer idUsuario, String formato,
            String rutaArchivo, String estado) {
        // Asigna el título del reporte exportado.
        this.titulo = titulo;
        // Asigna el tipo del ENUM tipo_reporte.
        this.tipoReporte = tipoReporte;
        // Asigna el id del usuario que exportó el reporte.
        this.idUsuario = idUsuario;
        // Asigna el formato del archivo ('excel').
        this.formato = formato;
        // Asigna la ruta absoluta del archivo exportado.
        this.rutaArchivo = rutaArchivo;
        // Asigna el estado de la exportación ('generado' o 'error').
        this.estado = estado;
    }

    // Devuelve el id del registro (columna id_reporte).
    public Integer getIdReporte() {
        return idReporte;
    }

    // Cambia el id del registro (columna id_reporte).
    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    // Devuelve el título del reporte exportado.
    public String getTitulo() {
        return titulo;
    }

    // Cambia el título del reporte exportado.
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Devuelve el tipo del ENUM tipo_reporte.
    public String getTipoReporte() {
        return tipoReporte;
    }

    // Cambia el tipo del ENUM tipo_reporte.
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    // Devuelve el id del usuario que exportó el reporte.
    public Integer getIdUsuario() {
        return idUsuario;
    }

    // Cambia el id del usuario que exportó el reporte.
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Devuelve el formato del archivo ('excel').
    public String getFormato() {
        return formato;
    }

    // Cambia el formato del archivo ('excel').
    public void setFormato(String formato) {
        this.formato = formato;
    }

    // Devuelve la ruta absoluta del archivo exportado.
    public String getRutaArchivo() {
        return rutaArchivo;
    }

    // Cambia la ruta absoluta del archivo exportado.
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // Devuelve el estado de la exportación ('generado' o 'error').
    public String getEstado() {
        return estado;
    }

    // Cambia el estado de la exportación ('generado' o 'error').
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // INSERT en la tabla reportes; id_almacen, id_categoria y el rango de fechas quedan en NULL
    // porque los reportes actuales no se filtran por esos datos.
    public static boolean insertReporte(RegistroReporte reporte) {
        // Obtiene la conexión compartida con MySQL desde config.conexión_BD.
        Connection con = config.conexión_BD.getConnection();
        // getConnection() devuelve null cuando MySQL no está disponible.
        if (con == null) {
            // Informa al usuario que no hay conexión con la base de datos.
            JOptionPane.showMessageDialog(null, config.conexión_BD.MENSAJE_SIN_CONEXION,
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            // Sin conexión no se registra.
            return false;
        }
        // Sentencia parametrizada con los datos de la exportación.
        String query = "INSERT INTO reportes (titulo, tipo_reporte, id_usuario, formato, ruta_archivo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        // Prepara la sentencia parametrizada; try-with-resources la cierra al terminar.
        try (PreparedStatement ps = con.prepareStatement(query)) {
            // Parámetro 1: título recortado a 150 caracteres.
            ps.setString(1, recortar(reporte.getTitulo(), 150));
            // Parámetro 2: tipo; 'general' si llega null.
            ps.setString(2, reporte.getTipoReporte() == null ? TIPO_GENERAL : reporte.getTipoReporte());
            // Parámetro 3: id del usuario de la sesión.
            ps.setInt(3, reporte.getIdUsuario());
            // Parámetro 4: formato; 'excel' si llega null.
            ps.setString(4, reporte.getFormato() == null ? FORMATO_EXCEL : reporte.getFormato());
            // Parámetro 5: ruta recortada a 255 caracteres, o NULL si falló la exportación.
            ps.setString(5, recortar(reporte.getRutaArchivo(), 255));
            // Parámetro 6: estado; 'generado' si llega null.
            ps.setString(6, reporte.getEstado() == null ? ESTADO_GENERADO : reporte.getEstado());

            // Ejecuta la sentencia y devuelve true si afectó al menos una fila.
            return ps.executeUpdate() != 0;
        // Captura errores de MySQL: restricciones, llaves foráneas, sintaxis o conexión perdida.
        } catch (SQLException ex) {
            // Marca el error para que el controlador no lo confunda con un dato inválido.
            config.conexión_BD.registrarError();
            // Registra la traza del error en la consola de NetBeans.
            Logger.getLogger(RegistroReporte.class.getName()).log(Level.SEVERE, null, ex);
            // Aviso (no error): el archivo existe aunque no quedó registrado en la tabla.
            JOptionPane.showMessageDialog(null, "El archivo se exportó, pero no se pudo registrar en la tabla "
                    + "reportes: " + ex.getMessage(), "Error de base de datos", JOptionPane.WARNING_MESSAGE);
            // No se registró.
            return false;
        }
    }

    // Recorta el texto al largo de la columna para evitar el error 'Data too long'.
    private static String recortar(String texto, int largoMaximo) {
        // Si es null o cabe en la columna, no se modifica.
        if (texto == null || texto.length() <= largoMaximo) {
            // Devuelve el texto original.
            return texto;
        }
        // Devuelve solo los primeros caracteres permitidos.
        return texto.substring(0, largoMaximo);
    }
}
