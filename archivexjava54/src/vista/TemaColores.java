// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Color: colores de la paleta oscura de ArchiveX.
import java.awt.Color;
// Component: tipo base de cada hijo que se recorre al aplicar el tema.
import java.awt.Component;
// Container: panel cuyos componentes se recorren recursivamente.
import java.awt.Container;
// Cursor: identifica las etiquetas que funcionan como enlace (cursor de mano).
import java.awt.Cursor;
// Font: tipografía de etiquetas, campos y tablas.
import java.awt.Font;
// Insets: margen interno de los botones.
import java.awt.Insets;
// Eventos para recalcular el ancho de columnas cuando cambian los datos o el tamaño de la tabla.
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
// BorderFactory crea los bordes de los campos y tablas.
import javax.swing.BorderFactory;
// JButton recibe fuente y margen más grandes.
import javax.swing.JButton;
// JComboBox recibe colores del tema.
import javax.swing.JComboBox;
// JLabel recibe color de texto y fuente del tema.
import javax.swing.JLabel;
// JPasswordField se estiliza igual que los campos de texto.
import javax.swing.JPasswordField;
// JScrollPane: contenedor de las tablas al que se le cambia fondo y borde.
import javax.swing.JScrollPane;
// JTable recibe los colores del tema.
import javax.swing.JTable;
// JTextField recibe fondo, color, fuente y borde del tema.
import javax.swing.JTextField;
// JViewport: área visible de la tabla dentro del JScrollPane.
import javax.swing.JViewport;
// SwingUtilities: agrupa en una sola tarea los recálculos de ancho de columnas.
import javax.swing.SwingUtilities;
// UIManager: fuentes globales de los cuadros de diálogo.
import javax.swing.UIManager;
// BasicArrowButton: flechas de combos y barras de desplazamiento (no se les cambia el margen).
import javax.swing.plaf.basic.BasicArrowButton;
// FontUIResource: fuente registrada en UIManager.
import javax.swing.plaf.FontUIResource;
// JTableHeader: encabezado de columnas de las tablas.
import javax.swing.table.JTableHeader;
// Clases para medir el contenido de las columnas.
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

// Paleta y tipografía de ArchiveX con métodos que la aplican a las ventanas (clase de utilidades).
public final class TemaColores {
    // Constructor privado: la clase no se instancia.
    private TemaColores() {
    }

    // Fondo principal oscuro de las ventanas (#0B1220).
    public static final Color FONDO_PRINCIPAL = new Color(0x0B, 0x12, 0x20);
    // Fondo secundario para encabezados de tabla (#111827).
    public static final Color FONDO_SECUNDARIO = new Color(0x11, 0x18, 0x27);
    // Fondo de tarjetas y filas de tabla (#0F172A).
    public static final Color FONDO_TARJETA = new Color(0x0F, 0x17, 0x2A);
    // Color de acento naranja para selección y datos destacados (#ED953E).
    public static final Color ACENTO = new Color(0xED, 0x95, 0x3E);
    // Variante oscura del acento (#C9732F).
    public static final Color ACENTO_OSCURO = new Color(0xC9, 0x73, 0x2F);
    // Color de texto principal (blanco).
    public static final Color TEXTO = Color.WHITE;
    // Texto secundario gris claro (#E5E7EB).
    public static final Color TEXTO_SECUNDARIO = new Color(0xE5, 0xE7, 0xEB);
    // Texto atenuado para ayudas e indicaciones (#94A3B8).
    public static final Color TEXTO_ATENUADO = new Color(0x94, 0xA3, 0xB8);
    // Color de bordes de campos y tablas (#334155).
    public static final Color BORDE = new Color(0x33, 0x41, 0x55);
    // Fondo de campos de texto y combos (igual al fondo principal).
    public static final Color CAMPO_FONDO = FONDO_PRINCIPAL;

    // Nombre de la fuente usada en toda la interfaz.
    public static final String NOMBRE_FUENTE = "Segoe UI";
    // Fuente base de 15 puntos para etiquetas, campos y tablas (antes 13: textos más legibles).
    public static final Font FUENTE_BASE = new Font(NOMBRE_FUENTE, Font.PLAIN, 15);
    // Fuente de título de 22 puntos en negrita.
    public static final Font FUENTE_TITULO = new Font(NOMBRE_FUENTE, Font.BOLD, 22);
    // Fuente de los botones: negrita de 15 puntos.
    public static final Font FUENTE_BOTON = new Font(NOMBRE_FUENTE, Font.BOLD, 15);
    // Margen interno de los botones para que sean más fáciles de pulsar.
    private static final Insets MARGEN_BOTON = new Insets(8, 18, 8, 18);
    // Alto de cada fila de las tablas.
    private static final int ALTO_FILA_TABLA = 30;
    // Ancho máximo que se le da a una columna al ajustarla a su contenido.
    private static final int ANCHO_MAXIMO_COLUMNA = 360;
    // Filas que se miden para calcular el ancho de una columna (evita recorrer tablas enormes).
    private static final int FILAS_MEDIDAS = 300;

    // Aplica la fuente base a los cuadros de diálogo (JOptionPane) de toda la aplicación.
    public static void aplicarFuentesGlobales() {
        // Texto de los mensajes.
        UIManager.put("OptionPane.messageFont", new FontUIResource(FUENTE_BASE));
        // Botones Aceptar / Sí / No.
        UIManager.put("OptionPane.buttonFont", new FontUIResource(FUENTE_BOTON));
        // Ayudas emergentes.
        UIManager.put("ToolTip.font", new FontUIResource(FUENTE_BASE));
    }

    // Conserva la fuente del diseñador cuando es más grande que la indicada (títulos); si no, usa la indicada.
    private static Font fuenteMinima(Font actual, Font minima) {
        return actual != null && actual.getSize() > minima.getSize() ? actual : minima;
    }

    // Recorre el contenedor y aplica el tema a etiquetas, campos y combos (no a botones ni tablas).
    public static void aplicarTema(Container contenedor) {
        // Recorre los componentes hijos directos.
        for (Component hijo : contenedor.getComponents()) {
            // Etiquetas: texto blanco y fuente base.
            if (hijo instanceof JLabel) {
                // Color del texto; las etiquetas-enlace (cursor de mano) conservan el color de acento.
                hijo.setForeground(hijo.getCursor().getType() == Cursor.HAND_CURSOR ? ACENTO : TEXTO);
                // Fuente base, sin reducir los títulos que el diseñador definió más grandes.
                hijo.setFont(fuenteMinima(hijo.getFont(), FUENTE_BASE));
            // Botones (excepto las flechas de combos y barras de desplazamiento).
            } else if (hijo instanceof JButton && !(hijo instanceof BasicArrowButton)) {
                // Fuente más grande y legible.
                hijo.setFont(fuenteMinima(hijo.getFont(), FUENTE_BOTON));
                // Margen interno: botones más altos y anchos.
                ((JButton) hijo).setMargin(MARGEN_BOTON);
            // Campos de contraseña (se revisan antes porque JPasswordField hereda de JTextField).
            } else if (hijo instanceof JPasswordField) {
                // Aplica el estilo de campo.
                estilizarCampo((JPasswordField) hijo);
            // Campos de texto.
            } else if (hijo instanceof JTextField) {
                // Aplica el estilo de campo.
                estilizarCampo((JTextField) hijo);
            // Combos de cualquier tipo.
            } else if (hijo instanceof JComboBox<?>) {
                // Conversión al tipo JComboBox.
                JComboBox<?> combo = (JComboBox<?>) hijo;
                // Fondo del combo.
                combo.setBackground(CAMPO_FONDO);
                // Color del texto del combo.
                combo.setForeground(TEXTO);
                // Fuente base.
                combo.setFont(FUENTE_BASE);
            }

            // Si el hijo contiene otros componentes (paneles, pestañas), se recorre también;
            // las partes internas de un JComboBox no se modifican.
            if (hijo instanceof Container && !(hijo instanceof JComboBox<?>)) {
                // Llamada recursiva sobre el contenedor hijo.
                aplicarTema((Container) hijo);
            }
        }
    }

    // Aplica colores, fuente y borde del tema a un campo de texto.
    private static void estilizarCampo(JTextField campo) {
        // Fondo oscuro del campo.
        campo.setBackground(CAMPO_FONDO);
        // Texto blanco.
        campo.setForeground(TEXTO);
        // Cursor de escritura blanco.
        campo.setCaretColor(TEXTO);
        // Fuente base.
        campo.setFont(FUENTE_BASE);
        // Borde de 1 px con margen interno de 6 y 8 px: campos más altos y cómodos.
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    // Aplica el estilo oscuro a una tabla y, si se pasa, a su JScrollPane.
    public static void aplicarEstiloTabla(JTable tabla, JScrollPane scroll) {
        // Fondo de las filas.
        tabla.setBackground(FONDO_TARJETA);
        // Texto de las filas.
        tabla.setForeground(TEXTO);
        // Color de las líneas de la cuadrícula.
        tabla.setGridColor(BORDE);
        // Fuente base.
        tabla.setFont(FUENTE_BASE);
        // Alto de cada fila en píxeles.
        tabla.setRowHeight(ALTO_FILA_TABLA);
        // Fondo naranja de la fila seleccionada.
        tabla.setSelectionBackground(ACENTO);
        // Texto oscuro sobre la fila seleccionada.
        tabla.setSelectionForeground(FONDO_PRINCIPAL);

        // Encabezado de columnas de la tabla.
        JTableHeader encabezado = tabla.getTableHeader();
        // Fondo del encabezado.
        encabezado.setBackground(FONDO_SECUNDARIO);
        // Texto del encabezado.
        encabezado.setForeground(TEXTO);
        // Fuente base en negrita para el encabezado.
        encabezado.setFont(FUENTE_BASE.deriveFont(Font.BOLD));

        // El scroll es opcional.
        if (scroll != null) {
            // Fondo del área vacía debajo de las filas.
            scroll.getViewport().setBackground(FONDO_TARJETA);
            // Borde del contenedor de la tabla.
            scroll.setBorder(BorderFactory.createLineBorder(BORDE, 1));
        }

        // Ajusta el ancho de las columnas ahora y cada vez que cambien los datos o el tamaño.
        instalarAjusteColumnas(tabla);
    }

    // Registra (una sola vez por tabla) los oyentes que recalculan el ancho de las columnas.
    private static void instalarAjusteColumnas(final JTable tabla) {
        // Evita registrar los oyentes dos veces (Reportes_Form vuelve a aplicar el estilo).
        if (tabla.getClientProperty("archivex.ajusteColumnas") != null) {
            programarAjusteColumnas(tabla);
            return;
        }
        tabla.putClientProperty("archivex.ajusteColumnas", Boolean.TRUE);
        // Cambios en las filas (cargar, limpiar o recargar la tabla).
        final javax.swing.event.TableModelListener oyenteDatos = e -> programarAjusteColumnas(tabla);
        tabla.getModel().addTableModelListener(oyenteDatos);
        // Reportes_Form reemplaza el modelo completo al generar otro reporte.
        tabla.addPropertyChangeListener("model", e -> {
            if (e.getOldValue() instanceof TableModel) {
                ((TableModel) e.getOldValue()).removeTableModelListener(oyenteDatos);
            }
            if (e.getNewValue() instanceof TableModel) {
                ((TableModel) e.getNewValue()).addTableModelListener(oyenteDatos);
            }
            programarAjusteColumnas(tabla);
        });
        // Cambio de tamaño de la ventana: decide si hace falta desplazamiento horizontal.
        instalarOyenteViewport(tabla);
        tabla.addHierarchyListener(e -> instalarOyenteViewport(tabla));
        programarAjusteColumnas(tabla);
    }

    // Recalcula las columnas cuando cambia el tamaño del área visible de la tabla.
    private static void instalarOyenteViewport(final JTable tabla) {
        if (!(tabla.getParent() instanceof JViewport)) {
            return;
        }
        JViewport area = (JViewport) tabla.getParent();
        if (area.getClientProperty("archivex.ajusteColumnas") != null) {
            return;
        }
        area.putClientProperty("archivex.ajusteColumnas", Boolean.TRUE);
        area.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                programarAjusteColumnas(tabla);
            }
        });
    }

    // Agrupa varios cambios seguidos (por ejemplo, cada addRow) en un único recálculo.
    private static void programarAjusteColumnas(final JTable tabla) {
        if (tabla.getClientProperty("archivex.ajustePendiente") != null) {
            return;
        }
        tabla.putClientProperty("archivex.ajustePendiente", Boolean.TRUE);
        SwingUtilities.invokeLater(() -> {
            tabla.putClientProperty("archivex.ajustePendiente", null);
            ajustarColumnas(tabla);
        });
    }

    // Da a cada columna el ancho de su encabezado y contenido; si no caben en el área visible,
    // la tabla usa desplazamiento horizontal en lugar de cortar los textos.
    public static void ajustarColumnas(JTable tabla) {
        int total = 0;
        int filas = Math.min(tabla.getRowCount(), FILAS_MEDIDAS);
        for (int c = 0; c < tabla.getColumnCount(); c++) {
            TableColumn columna = tabla.getColumnModel().getColumn(c);
            TableCellRenderer renderEncabezado = columna.getHeaderRenderer() != null
                    ? columna.getHeaderRenderer() : tabla.getTableHeader().getDefaultRenderer();
            int ancho = renderEncabezado.getTableCellRendererComponent(tabla, columna.getHeaderValue(),
                    false, false, -1, c).getPreferredSize().width;
            for (int f = 0; f < filas; f++) {
                Component celda = tabla.prepareRenderer(tabla.getCellRenderer(f, c), f, c);
                ancho = Math.max(ancho, celda.getPreferredSize().width);
            }
            // Margen para que el texto no quede pegado al borde.
            ancho = Math.min(ancho + 18, ANCHO_MAXIMO_COLUMNA);
            columna.setPreferredWidth(ancho);
            // Con desplazamiento horizontal la tabla usa el ancho real de cada columna.
            columna.setWidth(ancho);
            total += ancho;
        }
        int visible = tabla.getParent() instanceof JViewport ? tabla.getParent().getWidth() : 0;
        // Si todas caben, se reparten el ancho disponible; si no, aparece la barra horizontal.
        tabla.setAutoResizeMode(visible > 0 && total > visible
                ? JTable.AUTO_RESIZE_OFF : JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        tabla.revalidate();
    }
}
