// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Compra_control entrega la lista de almacenes activos.
import controlador.Compra_control;
// Login_control entrega el usuario de la sesión (responsable del movimiento).
import controlador.Login_control;
// Movimiento_control valida y registra los movimientos de inventario.
import controlador.Movimiento_control;
// SimpleDateFormat da formato a la fecha mostrada en la tabla.
import java.text.SimpleDateFormat;
// ArrayList: productos, almacenes y movimientos consultados.
import java.util.ArrayList;
// DefaultTableModel: modelo de filas del historial de movimientos.
import javax.swing.table.DefaultTableModel;
// Almacen: elementos de los combos de almacén.
import modelo.Almacen;
// MovimientoInventario: constantes de tipo y filas del historial.
import modelo.MovimientoInventario;
// Producto: elementos de los combos de producto.
import modelo.Producto;
// Usuario: usuario de la sesión mostrado como responsable.
import modelo.Usuario;

// Ventana de movimientos manuales (entrada, salida, ajuste, devolución) y consulta del historial;
// la fecha y el responsable se asignan automáticamente al guardar.
public class Movimientos_Form extends javax.swing.JFrame {
    // true mientras se llenan los combos, para ignorar los eventos que disparan.
    private boolean cargando;

    // Formato de fecha y hora de la columna Fecha.
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // Constructor: arma la tabla, llena los combos y muestra el responsable y el historial.
    public Movimientos_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Movimientos de inventario");

        // Modelo de la tabla con las columnas del historial y sin filas iniciales.
        DefaultTableModel modelo = new DefaultTableModel(new Object[][]{},
                new String[]{"ID", "Fecha", "Producto", "Almacén", "Tipo", "Cantidad", "Pedido", "Usuario",
                    "Motivo"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo a la tabla.
        jTableMovimientos.setModel(modelo);
        // Tamaño visible de la tabla.
        jTableMovimientos.setPreferredScrollableViewportSize(new java.awt.Dimension(900, 260));

        // Evita que los combos consulten la existencia mientras se llenan.
        cargando = true;
        // Llena los combos de producto, almacén, tipo y filtros.
        cargarCombos();
        // A partir de aquí los combos responden normalmente.
        cargando = false;
        // Deja el formulario vacío y muestra la existencia del primer producto y almacén.
        limpiarFormulario();
        // Carga el historial sin filtros.
        cargarMovimientos();

        // Usuario que inició sesión; null si se abrió la ventana sin login.
        Usuario usuario = Login_control.getUsuarioSesion();
        // Muestra quién quedará como responsable y que la fecha se asigna al guardar.
        jLabelResponsable.setText("Responsable: " + (usuario == null ? "sin sesión iniciada"
                : usuario.getNombre() + " " + usuario.getApellido())
                + "   |   Fecha y hora: se registran automáticamente al guardar");

        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableMovimientos, jScrollPaneMovimientos);
        // Existencia en naranja y negrita para destacarla.
        jLabelExistencia.setFont(TemaColores.FUENTE_BASE.deriveFont(java.awt.Font.BOLD, 18f));
        // Color de acento del tema.
        jLabelExistencia.setForeground(TemaColores.ACENTO);
        // Texto de ayuda atenuado.
        jLabelAyudaCantidad.setForeground(TemaColores.TEXTO_ATENUADO);
        // Texto del responsable atenuado.
        jLabelResponsable.setForeground(TemaColores.TEXTO_ATENUADO);
        // Título de la sección de consulta en negrita.
        jLabelConsultaTitulo.setFont(TemaColores.FUENTE_BASE.deriveFont(java.awt.Font.BOLD, 17f));

        // Cerrar con la X equivale a "Volver al Menú".
        RecursosApp.cerrarConBoton(this, jButtonVolver);
        // Tamaño inicial 1240x760 (antes 1080x700) y mínimo 1040x660, limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 1240, 760, 1040, 660);
    }

    // initComponents() lo genera el GUI Builder de NetBeans a partir del .form;
    // no se edita a mano porque NetBeans lo regenera al guardar el diseño.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jButtonVolver = new javax.swing.JButton();
        jPanelContenido = new javax.swing.JPanel();
        jPanelFormularioMovimiento = new javax.swing.JPanel();
        jLabelProducto = new javax.swing.JLabel();
        jComboBoxProducto = new javax.swing.JComboBox<>();
        jLabelAlmacen = new javax.swing.JLabel();
        jComboBoxAlmacen = new javax.swing.JComboBox<>();
        jLabelExistenciaTitulo = new javax.swing.JLabel();
        jLabelExistencia = new javax.swing.JLabel();
        jLabelTipo = new javax.swing.JLabel();
        jComboBoxTipo = new javax.swing.JComboBox<>();
        jLabelCantidad = new javax.swing.JLabel();
        jTextFieldCantidad = new javax.swing.JTextField();
        jLabelAyudaCantidad = new javax.swing.JLabel();
        jLabelMotivo = new javax.swing.JLabel();
        jTextFieldMotivo = new javax.swing.JTextField();
        jPanelBotonesMovimiento = new javax.swing.JPanel();
        jButtonRegistrarMovimiento = new javax.swing.JButton();
        jButtonLimpiarMovimiento = new javax.swing.JButton();
        jLabelResponsable = new javax.swing.JLabel();
        jPanelConsulta = new javax.swing.JPanel();
        jPanelFiltrosMovimiento = new javax.swing.JPanel();
        jLabelConsultaTitulo = new javax.swing.JLabel();
        jLabelFiltroProducto = new javax.swing.JLabel();
        jComboBoxFiltroProducto = new javax.swing.JComboBox<>();
        jLabelFiltroAlmacen = new javax.swing.JLabel();
        jComboBoxFiltroAlmacen = new javax.swing.JComboBox<>();
        jLabelFiltroTipo = new javax.swing.JLabel();
        jComboBoxFiltroTipo = new javax.swing.JComboBox<>();
        jButtonBuscarMovimientos = new javax.swing.JButton();
        jButtonQuitarFiltros = new javax.swing.JButton();
        jScrollPaneMovimientos = new javax.swing.JScrollPane();
        jTableMovimientos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));
        jPanelTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));
        jPanelTitulo.setLayout(new java.awt.BorderLayout());
        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Movimientos de inventario");

        jPanelTitulo.add(jLabelTitulo, java.awt.BorderLayout.WEST);
        jButtonVolver.setBackground(new java.awt.Color(15, 23, 42));
        jButtonVolver.setForeground(new java.awt.Color(255, 255, 255));
        jButtonVolver.setText("Volver al Menú");
        jButtonVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVolverActionPerformed(evt);
            }
        });

        jPanelTitulo.add(jButtonVolver, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanelTitulo, java.awt.BorderLayout.NORTH);
        jPanelContenido.setBackground(new java.awt.Color(11, 18, 32));
        jPanelContenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        jPanelContenido.setLayout(new java.awt.BorderLayout());
        jPanelFormularioMovimiento.setBackground(new java.awt.Color(11, 18, 32));
        jPanelFormularioMovimiento.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 4, 0));
        jPanelFormularioMovimiento.setLayout(new java.awt.GridBagLayout());
        jLabelProducto.setText("Producto:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jLabelProducto, gridBagConstraints);
        jComboBoxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProductoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jComboBoxProducto, gridBagConstraints);
        jLabelAlmacen.setText("Almacén:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFormularioMovimiento.add(jLabelAlmacen, gridBagConstraints);
        jComboBoxAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAlmacenActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jComboBoxAlmacen, gridBagConstraints);
        jLabelExistenciaTitulo.setText("Existencia actual:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFormularioMovimiento.add(jLabelExistenciaTitulo, gridBagConstraints);
        jLabelExistencia.setText("-");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jLabelExistencia, gridBagConstraints);
        jLabelTipo.setText("Tipo:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jLabelTipo, gridBagConstraints);
        jComboBoxTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTipoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jComboBoxTipo, gridBagConstraints);
        jLabelCantidad.setText("Cantidad:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFormularioMovimiento.add(jLabelCantidad, gridBagConstraints);
        jTextFieldCantidad.setColumns(8);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jTextFieldCantidad, gridBagConstraints);
        jLabelAyudaCantidad.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFormularioMovimiento.add(jLabelAyudaCantidad, gridBagConstraints);
        jLabelMotivo.setText("Motivo:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jLabelMotivo, gridBagConstraints);
        jTextFieldMotivo.setColumns(30);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jTextFieldMotivo, gridBagConstraints);
        jPanelBotonesMovimiento.setBackground(new java.awt.Color(11, 18, 32));
        jPanelBotonesMovimiento.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 6));
        jButtonRegistrarMovimiento.setBackground(new java.awt.Color(237, 149, 62));
        jButtonRegistrarMovimiento.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRegistrarMovimiento.setText("Registrar movimiento");
        jButtonRegistrarMovimiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonRegistrarMovimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistrarMovimientoActionPerformed(evt);
            }
        });

        jPanelBotonesMovimiento.add(jButtonRegistrarMovimiento);
        jButtonLimpiarMovimiento.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarMovimiento.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarMovimiento.setText("Limpiar");
        jButtonLimpiarMovimiento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarMovimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarMovimientoActionPerformed(evt);
            }
        });

        jPanelBotonesMovimiento.add(jButtonLimpiarMovimiento);
        jLabelResponsable.setText("Responsable: -");

        jPanelBotonesMovimiento.add(jLabelResponsable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFormularioMovimiento.add(jPanelBotonesMovimiento, gridBagConstraints);

        jPanelContenido.add(jPanelFormularioMovimiento, java.awt.BorderLayout.NORTH);
        jPanelConsulta.setBackground(new java.awt.Color(11, 18, 32));
        jPanelConsulta.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        jPanelConsulta.setLayout(new java.awt.BorderLayout());
        jPanelFiltrosMovimiento.setBackground(new java.awt.Color(11, 18, 32));
        jPanelFiltrosMovimiento.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 6, 0));
        jPanelFiltrosMovimiento.setLayout(new java.awt.GridBagLayout());
        jLabelConsultaTitulo.setText("Consultar movimientos");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosMovimiento.add(jLabelConsultaTitulo, gridBagConstraints);
        jLabelFiltroProducto.setText("Producto:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosMovimiento.add(jLabelFiltroProducto, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosMovimiento.add(jComboBoxFiltroProducto, gridBagConstraints);
        jLabelFiltroAlmacen.setText("Almacén:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFiltrosMovimiento.add(jLabelFiltroAlmacen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosMovimiento.add(jComboBoxFiltroAlmacen, gridBagConstraints);
        jLabelFiltroTipo.setText("Tipo:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFiltrosMovimiento.add(jLabelFiltroTipo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosMovimiento.add(jComboBoxFiltroTipo, gridBagConstraints);
        jButtonBuscarMovimientos.setBackground(new java.awt.Color(237, 149, 62));
        jButtonBuscarMovimientos.setForeground(new java.awt.Color(255, 255, 255));
        jButtonBuscarMovimientos.setText("Buscar");
        jButtonBuscarMovimientos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonBuscarMovimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarMovimientosActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFiltrosMovimiento.add(jButtonBuscarMovimientos, gridBagConstraints);
        jButtonQuitarFiltros.setBackground(new java.awt.Color(15, 23, 42));
        jButtonQuitarFiltros.setForeground(new java.awt.Color(255, 255, 255));
        jButtonQuitarFiltros.setText("Quitar filtros");
        jButtonQuitarFiltros.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonQuitarFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarFiltrosActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosMovimiento.add(jButtonQuitarFiltros, gridBagConstraints);

        jPanelConsulta.add(jPanelFiltrosMovimiento, java.awt.BorderLayout.NORTH);

        jScrollPaneMovimientos.setViewportView(jTableMovimientos);

        jPanelConsulta.add(jScrollPaneMovimientos, java.awt.BorderLayout.CENTER);

        jPanelContenido.add(jPanelConsulta, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanelContenido, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Botón Volver: regresa al menú principal.
    private void jButtonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVolverActionPerformed

        // Crea de nuevo el menú principal para regresar a él.
        vista.Home_Form homeForm = new vista.Home_Form();
        // Cerrar esa ventana con la X termina la aplicación.
        homeForm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        homeForm.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        homeForm.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonVolverActionPerformed

    // Cambio de producto: muestra su existencia en el almacén elegido.
    private void jComboBoxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxProductoActionPerformed
        // Consulta la existencia actual.
        actualizarExistencia();
    }//GEN-LAST:event_jComboBoxProductoActionPerformed

    // Cambio de almacén: muestra la existencia del producto en ese almacén.
    private void jComboBoxAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAlmacenActionPerformed
        // Consulta la existencia actual.
        actualizarExistencia();
    }//GEN-LAST:event_jComboBoxAlmacenActionPerformed

    // Cambio de tipo: actualiza el texto de ayuda de la cantidad.
    private void jComboBoxTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTipoActionPerformed
        // Explica qué significa la cantidad en el tipo elegido.
        actualizarSegunTipo();
    }//GEN-LAST:event_jComboBoxTipoActionPerformed

    // Botón Registrar: guarda el movimiento en MySQL.
    private void jButtonRegistrarMovimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistrarMovimientoActionPerformed
        // Valida y registra el movimiento.
        registrarMovimiento();
    }//GEN-LAST:event_jButtonRegistrarMovimientoActionPerformed

    // Botón Limpiar: vacía cantidad y motivo.
    private void jButtonLimpiarMovimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarMovimientoActionPerformed
        // Restablece el formulario.
        limpiarFormulario();
    }//GEN-LAST:event_jButtonLimpiarMovimientoActionPerformed

    // Botón Buscar: aplica los filtros de la sección de consulta.
    private void jButtonBuscarMovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarMovimientosActionPerformed
        // Consulta el historial con los filtros elegidos.
        cargarMovimientos();
    }//GEN-LAST:event_jButtonBuscarMovimientosActionPerformed

    // Botón Quitar filtros: vuelve a 'Todos' y recarga el historial.
    private void jButtonQuitarFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarFiltrosActionPerformed
        // Filtro de producto en 'Todos'.
        jComboBoxFiltroProducto.setSelectedIndex(0);
        // Filtro de almacén en 'Todos'.
        jComboBoxFiltroAlmacen.setSelectedIndex(0);
        // Filtro de tipo en 'Todos'.
        jComboBoxFiltroTipo.setSelectedIndex(0);
        // Recarga el historial completo.
        cargarMovimientos();
    }//GEN-LAST:event_jButtonQuitarFiltrosActionPerformed

    // Llena los combos del formulario y de los filtros con datos de MySQL.
    private void cargarCombos() {
        // Productos de la tabla productos.
        ArrayList<Producto> productos = Producto.getProductos();
        // Solo los almacenes activos pueden recibir movimientos.
        ArrayList<Almacen> almacenes = Compra_control.listarAlmacenesActivos();

        // Vacía el combo de producto del formulario.
        jComboBoxProducto.removeAllItems();
        // Vacía el filtro de producto.
        jComboBoxFiltroProducto.removeAllItems();
        // Primera opción del filtro: 'Todos'.
        jComboBoxFiltroProducto.addItem(Movimiento_control.FILTRO_TODOS);
        // Recorre los productos.
        for (Producto producto : productos) {
            // Agrega el producto al formulario.
            jComboBoxProducto.addItem(producto);
            // Agrega el producto al filtro.
            jComboBoxFiltroProducto.addItem(producto);
        }

        // Vacía el combo de almacén del formulario.
        jComboBoxAlmacen.removeAllItems();
        // Vacía el filtro de almacén.
        jComboBoxFiltroAlmacen.removeAllItems();
        // Primera opción del filtro: 'Todos'.
        jComboBoxFiltroAlmacen.addItem(Movimiento_control.FILTRO_TODOS);
        // Recorre los almacenes activos.
        for (Almacen almacen : almacenes) {
            // Agrega el almacén al formulario.
            jComboBoxAlmacen.addItem(almacen);
        }

        // El filtro incluye también los almacenes inactivos para consultar su historial.
        for (Almacen almacen : Almacen.getAlmacenes()) {
            // Agrega el almacén al filtro.
            jComboBoxFiltroAlmacen.addItem(almacen);
        }

        // Vacía el combo de tipo.
        jComboBoxTipo.removeAllItems();
        // Tipos del ENUM tipo_movimiento.
        for (String tipo : Movimiento_control.getTiposMovimiento()) {
            // Agrega cada tipo.
            jComboBoxTipo.addItem(tipo);
        }
        // Vacía el filtro de tipo.
        jComboBoxFiltroTipo.removeAllItems();
        // 'Todos' más los tipos del ENUM.
        for (String tipo : Movimiento_control.getFiltrosTipo()) {
            // Agrega cada opción.
            jComboBoxFiltroTipo.addItem(tipo);
        }
    }

    // Vacía cantidad y motivo y vuelve al primer tipo.
    private void limpiarFormulario() {
        // Vacía la cantidad.
        jTextFieldCantidad.setText("");
        // Vacía el motivo.
        jTextFieldMotivo.setText("");
        // Solo si el combo ya tiene tipos.
        if (jComboBoxTipo.getItemCount() > 0) {
            // Selecciona el primer tipo (entrada).
            jComboBoxTipo.setSelectedIndex(0);
        }
        // Actualiza el texto de ayuda.
        actualizarSegunTipo();
        // Actualiza la existencia mostrada.
        actualizarExistencia();
    }

    // Muestra la existencia del producto en el almacén elegido (tabla inventarioporalmacen).
    private void actualizarExistencia() {
        // Los combos se están llenando.
        if (cargando) {
            // No consulta todavía.
            return;
        }
        // Producto elegido.
        Producto producto = (Producto) jComboBoxProducto.getSelectedItem();
        // Almacén elegido.
        Almacen almacen = (Almacen) jComboBoxAlmacen.getSelectedItem();
        // Falta alguno de los dos.
        if (producto == null || almacen == null) {
            // No hay existencia que mostrar.
            jLabelExistencia.setText("-");
            // Termina.
            return;
        }
        // Consulta y muestra la existencia actual.
        jLabelExistencia.setText(String.valueOf(Movimiento_control.existenciaActual(producto, almacen)));
    }

    // Cambia el texto de ayuda según el tipo: en 'ajuste' la cantidad es la existencia contada.
    private void actualizarSegunTipo() {
        // Los combos se están llenando.
        if (cargando) {
            // No actualiza todavía.
            return;
        }
        // Tipo elegido.
        String tipo = (String) jComboBoxTipo.getSelectedItem();
        // Entrada.
        if (MovimientoInventario.TIPO_ENTRADA.equals(tipo)) {
            // Ayuda para entradas.
            jLabelAyudaCantidad.setText("Unidades que ingresan al almacén (motivo opcional)");
        // Salida.
        } else if (MovimientoInventario.TIPO_SALIDA.equals(tipo)) {
            // Ayuda para salidas.
            jLabelAyudaCantidad.setText("Unidades que salen del almacén");
        // Ajuste.
        } else if (MovimientoInventario.TIPO_AJUSTE.equals(tipo)) {
            // Ayuda para ajustes.
            jLabelAyudaCantidad.setText("Nueva existencia contada físicamente");
        // Devolución.
        } else if (MovimientoInventario.TIPO_DEVOLUCION.equals(tipo)) {
            // Ayuda para devoluciones.
            jLabelAyudaCantidad.setText("Unidades devueltas al proveedor (salen del almacén)");
        // Sin tipo elegido.
        } else {
            // Deja un espacio para conservar la altura de la etiqueta.
            jLabelAyudaCantidad.setText(" ");
        }
    }

    // Envía el movimiento al controlador y, si se guardó, refresca existencia e historial.
    private void registrarMovimiento() {
        // Valida cantidad, sesión y existencia, y registra el movimiento en una transacción
        // (inventario, productos.cantidad, movimientoinventario e historialinventario).
        boolean registrado = Movimiento_control.registrarMovimiento(
                (Producto) jComboBoxProducto.getSelectedItem(),
                (Almacen) jComboBoxAlmacen.getSelectedItem(),
                (String) jComboBoxTipo.getSelectedItem(),
                jTextFieldCantidad.getText(),
                jTextFieldMotivo.getText());
        // Si se registró.
        if (registrado) {
            // Vacía la cantidad.
            jTextFieldCantidad.setText("");
            // Vacía el motivo.
            jTextFieldMotivo.setText("");
            // Muestra la nueva existencia.
            actualizarExistencia();
            // Agrega el movimiento al historial visible.
            cargarMovimientos();
        }
    }

    // Consulta los movimientos con los filtros y llena la tabla.
    private void cargarMovimientos() {
        // Filtro de producto: un Producto o el texto 'Todos'.
        Object producto = jComboBoxFiltroProducto.getSelectedItem();
        // Filtro de almacén: un Almacen o el texto 'Todos'.
        Object almacen = jComboBoxFiltroAlmacen.getSelectedItem();
        // Consulta con JOIN; 'Todos' se envía como null para no filtrar.
        ArrayList<MovimientoInventario> movimientos = Movimiento_control.buscarMovimientos(
                producto instanceof Producto ? (Producto) producto : null,
                almacen instanceof Almacen ? (Almacen) almacen : null,
                (String) jComboBoxFiltroTipo.getSelectedItem());
        // Modelo actual de la tabla.
        DefaultTableModel modelo = (DefaultTableModel) jTableMovimientos.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);
        // Agrega una fila por movimiento.
        for (MovimientoInventario movimiento : movimientos) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                movimiento.getIdMovimiento(),
                // Fecha con formato 'yyyy-MM-dd HH:mm' o vacía.
                movimiento.getFechaMovimiento() == null ? "" : formatoFecha.format(movimiento.getFechaMovimiento()),
                movimiento.getNombreProducto(),
                movimiento.getNombreAlmacen(),
                movimiento.getTipoMovimiento(),
                movimiento.getCantidad(),
                // Número de pedido solo en entradas generadas por una compra.
                movimiento.getNumeroPedido() == null ? "" : movimiento.getNumeroPedido(),
                movimiento.getNombreUsuario(),
                // Motivo o vacío.
                movimiento.getMotivo() == null ? "" : movimiento.getMotivo()
            });
        }
    }

    // Punto de entrada generado por NetBeans para abrir solo esta ventana (Run File).
    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            // Busca el aspecto visual Nimbus entre los instalados en Java.
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                // Si Nimbus está disponible.
                if ("Nimbus".equals(info.getName())) {
                    // Lo aplica a todas las ventanas Swing.
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        // La clase del Look and Feel no existe en esta versión de Java.
        } catch (ClassNotFoundException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Movimientos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Movimientos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Movimientos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Movimientos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Movimientos_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBuscarMovimientos;
    private javax.swing.JButton jButtonLimpiarMovimiento;
    private javax.swing.JButton jButtonQuitarFiltros;
    private javax.swing.JButton jButtonRegistrarMovimiento;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<Almacen> jComboBoxAlmacen;
    private javax.swing.JComboBox<Object> jComboBoxFiltroAlmacen;
    private javax.swing.JComboBox<Object> jComboBoxFiltroProducto;
    private javax.swing.JComboBox<String> jComboBoxFiltroTipo;
    private javax.swing.JComboBox<Producto> jComboBoxProducto;
    private javax.swing.JComboBox<String> jComboBoxTipo;
    private javax.swing.JLabel jLabelAlmacen;
    private javax.swing.JLabel jLabelAyudaCantidad;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelConsultaTitulo;
    private javax.swing.JLabel jLabelExistencia;
    private javax.swing.JLabel jLabelExistenciaTitulo;
    private javax.swing.JLabel jLabelFiltroAlmacen;
    private javax.swing.JLabel jLabelFiltroProducto;
    private javax.swing.JLabel jLabelFiltroTipo;
    private javax.swing.JLabel jLabelMotivo;
    private javax.swing.JLabel jLabelProducto;
    private javax.swing.JLabel jLabelResponsable;
    private javax.swing.JLabel jLabelTipo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelBotonesMovimiento;
    private javax.swing.JPanel jPanelConsulta;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelFiltrosMovimiento;
    private javax.swing.JPanel jPanelFormularioMovimiento;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPaneMovimientos;
    private javax.swing.JTable jTableMovimientos;
    private javax.swing.JTextField jTextFieldCantidad;
    private javax.swing.JTextField jTextFieldMotivo;
    // End of variables declaration//GEN-END:variables
}
