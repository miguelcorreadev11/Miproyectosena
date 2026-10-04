// Paquete de controladores del patrón MVC.
package controlador;

// Desktop abre el archivo generado con la aplicación asociada (Excel u otra).
import java.awt.Desktop;
// File representa el archivo .xls de destino.
import java.io.File;
// FileOutputStream escribe el libro de Excel en disco.
import java.io.FileOutputStream;
// IOException se propaga a Reportes_control si falla la escritura.
import java.io.IOException;
// JFileChooser permite elegir dónde guardar el archivo.
import javax.swing.JFileChooser;
// JTable es la tabla de la vista cuyo contenido se exporta.
import javax.swing.JTable;
// Filtro del diálogo para mostrar solo archivos .xls.
import javax.swing.filechooser.FileNameExtensionFilter;
// HSSFWorkbook crea libros de Excel 97-2003 (.xls) con Apache POI 4.0.1.
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
// Importación sin uso en el código; proviene de la versión original de la clase.
import org.apache.poi.sl.usermodel.Sheet;
// Cell representa una celda de la hoja.
import org.apache.poi.ss.usermodel.Cell;
// Row representa una fila de la hoja.
import org.apache.poi.ss.usermodel.Row;
// Workbook es la interfaz común del libro de Excel.
import org.apache.poi.ss.usermodel.Workbook;

// Utilidad que exporta cualquier JTable a un archivo Excel; la usa Reportes_control.
public class ExportarExcel {
     // Exporta la tabla y devuelve el archivo creado, o null si el usuario cancela.
     public File exportarExcel(JTable t) throws IOException {
        // Diálogo estándar de Swing para elegir la ruta del archivo.
        JFileChooser chooser = new JFileChooser();
        // Filtro que muestra solo archivos con extensión .xls.
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
        // Aplica el filtro al diálogo.
        chooser.setFileFilter(filter);
        // Título de la ventana de guardado.
        chooser.setDialogTitle("Guardar archivo");
        // Oculta la opción 'Todos los archivos' para forzar el formato .xls.
        chooser.setAcceptAllFileFilterUsed(false);
        // Continúa solo si el usuario confirmó el guardado.
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            // Ruta escrita o seleccionada por el usuario.
            String ruta = chooser.getSelectedFile().toString();

            // Agrega '.xls' solo si el usuario no la escribió, para evitar 'archivo.xls.xls'.
            if (!ruta.toLowerCase().endsWith(".xls")) {
                // Completa la extensión.
                ruta = ruta.concat(".xls");
            }
            try {
                // Archivo de destino.
                File archivoXLS = new File(ruta);
                // Si ya existe un archivo con ese nombre, se reemplaza.
                if (archivoXLS.exists()) {
                    // Borra la versión anterior.
                    archivoXLS.delete();
                }
                // Crea el archivo vacío en disco.
                archivoXLS.createNewFile();
                // try-with-resources cierra el libro de POI aunque ocurra un error.
                try (Workbook libro = new HSSFWorkbook()) {
                     // Crea la hoja donde se copiará la tabla.
                     org.apache.poi.ss.usermodel.Sheet hoja = libro.createSheet("Mi hoja de trabajo 1");
                    // Oculta la cuadrícula de la hoja.
                    hoja.setDisplayGridlines(false);

                    // La primera fila (índice 0) contiene los encabezados.
                    Row encabezado = hoja.createRow(0);
                    // Recorre las columnas de la JTable.
                    for (int c = 0; c < t.getColumnCount(); c++) {
                        // Escribe el nombre de la columna como texto.
                        encabezado.createCell(c).setCellValue(t.getColumnName(c));
                    }

                    // Los datos empiezan en la fila 1, debajo del encabezado.
                    int filaInicio = 1;
                    // Recorre cada fila de la JTable.
                    for (int f = 0; f < t.getRowCount(); f++) {
                        // Crea la fila correspondiente en la hoja.
                        Row fila = hoja.createRow(filaInicio);
                        // Avanza a la siguiente fila de la hoja.
                        filaInicio++;
                        // Recorre cada columna de la fila.
                        for (int c = 0; c < t.getColumnCount(); c++) {
                            // Crea la celda de destino.
                            Cell celda = fila.createCell(c);
                            // Los Double se guardan como número.
                            if (t.getValueAt(f, c) instanceof Double) {
                                // Convierte el valor a double y lo escribe como número.
                                celda.setCellValue(Double.parseDouble(t.getValueAt(f, c).toString()));
                            // Los Float también se guardan como número.
                            } else if (t.getValueAt(f, c) instanceof Float) {
                                // Convierte el Float a double sin pasar por texto.
                                celda.setCellValue(((Float) t.getValueAt(f, c)).doubleValue());
                            // Los valores null (por ejemplo, fechas NULL de MySQL) se dejan vacíos.
                            } else if (t.getValueAt(f, c) == null) {
                                // Escribe una celda vacía en lugar del texto 'null'.
                                celda.setCellValue("");
                            // Cualquier otro tipo (texto, BigDecimal, fechas) se guarda como texto.
                            } else {
                                // Convierte el valor con String.valueOf y lo escribe como texto.
                                celda.setCellValue(String.valueOf(t.getValueAt(f, c)));
                            }
                        }
                    }

                    // try-with-resources cierra el flujo aunque falle la escritura (evita dejar el archivo bloqueado).
                    try (FileOutputStream archivo = new FileOutputStream(archivoXLS)) {
                        // Escribe el libro completo en el archivo.
                        libro.write(archivo);
                    }
                }
                // Intenta abrir el archivo con la aplicación predeterminada del sistema.
                abrirArchivo(archivoXLS);
                // Devuelve el archivo para que Reportes_control registre su ruta en la tabla reportes.
                return archivoXLS;
            // Los errores de escritura o conversión se propagan al llamador.
            } catch (IOException | NumberFormatException e) {
                // Relanza la excepción para que Reportes_control la informe.
                throw e;
            }
        }
        // El usuario canceló el diálogo.
        return null;
    }

    // Abre el archivo exportado; si el sistema no lo permite, solo informa dónde quedó guardado.
    private void abrirArchivo(File archivo) {
        try {
            // Desktop no está disponible en todos los entornos.
            if (Desktop.isDesktopSupported()) {
                // Abre el .xls con Excel u otra aplicación asociada.
                Desktop.getDesktop().open(archivo);
            }
        // No hay aplicación asociada o el sistema bloqueó la apertura.
        } catch (IOException | RuntimeException ex) {
            // Informa la ruta donde quedó el archivo.
            javax.swing.JOptionPane.showMessageDialog(null, "El archivo se guardó en " + archivo.getAbsolutePath()
                    + "\npero no se pudo abrir automáticamente.", "Archivo exportado",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
