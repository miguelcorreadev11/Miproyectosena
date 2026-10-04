// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Compra_control valida y ejecuta registro, entrega y cancelación de compras.
import controlador.Compra_control;
// BigDecimal: total de la compra.
import java.math.BigDecimal;
// SimpleDateFormat da formato a las fechas de la tabla de compras.
import java.text.SimpleDateFormat;
// LocalDate: fecha de hoy sugerida como fecha del pedido.
import java.time.LocalDate;
// ArrayList: líneas del pedido nuevo y compras consultadas.
import java.util.ArrayList;
// DefaultTableModel: modelo de filas de las tres tablas.
import javax.swing.table.DefaultTableModel;
// Almacen: elementos del combo de almacén destino.
import modelo.Almacen;
// Compra: cabecera de las compras registradas.
import modelo.Compra;
// DetalleCompra: líneas de producto de cada compra.
import modelo.DetalleCompra;
// Producto: elementos del combo de producto.
import modelo.Producto;
// Proveedor: elementos del combo de proveedor.
import modelo.Proveedor;

// Ventana de compras: arma un pedido con varias líneas, lo guarda y permite entregarlo
// (genera entradas de inventario) o cancelarlo. Se conecta con Busqueda_Productos_Form.
public class Compras_Form extends javax.swing.JFrame {
    // Líneas del pedido que se está armando, aún no guardadas en MySQL.
    private final ArrayList<DetalleCompra> detallesNuevaCompra = new ArrayList<>();

    // Compras mostradas, en el mismo orden que las filas de jTableCompras.
    private ArrayList<Compra> listaCompras = new ArrayList<>();

    // true mientras se llenan los combos, para ignorar los eventos que disparan.
    private boolean cargando;
    // Id del producto cuyo precio se mostró por última vez en "Precio unitario".
    private Integer idProductoMostrado;
    // Ventana de búsqueda abierta desde esta compra (una sola a la vez).
    private Busqueda_Productos_Form busquedaAbierta;

    // Formato de fecha y hora de las columnas de fecha.
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // Constructor: arma las tablas, llena combos y filtros y carga las compras registradas.
    public Compras_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Compras");

        // Pestañas con el fondo del tema.
        jTabbedPaneCompras.setBackground(TemaColores.FONDO_PRINCIPAL);
        // Texto de las pestañas en blanco.
        jTabbedPaneCompras.setForeground(TemaColores.TEXTO);
        // Fuente base del tema.
        jTabbedPaneCompras.setFont(TemaColores.FUENTE_BASE);

        // Tabla de líneas del pedido nuevo.
        jTableDetalle.setModel(crearModeloTabla("Producto", "Cantidad", "Precio unitario", "Subtotal"));
        // Tabla de compras registradas.
        jTableCompras.setModel(crearModeloTabla("ID", "N° pedido", "Proveedor", "Almacén", "Fecha pedido",
                "Fecha entrega", "Total", "Estado"));
        // Tabla con el detalle de la compra seleccionada.
        jTableDetalleRegistrada.setModel(crearModeloTabla("Producto", "Cantidad", "Precio unitario", "Subtotal"));
        // Alto visible de la tabla de líneas.
        jTableDetalle.setPreferredScrollableViewportSize(new java.awt.Dimension(760, 180));
        // Alto visible de la tabla de compras.
        jTableCompras.setPreferredScrollableViewportSize(new java.awt.Dimension(760, 220));
        // Alto visible del detalle registrado.
        jTableDetalleRegistrada.setPreferredScrollableViewportSize(new java.awt.Dimension(760, 120));
        // Solo se puede seleccionar una compra a la vez.
        jTableCompras.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Al cambiar la selección se muestra el detalle de esa compra.
        jTableCompras.getSelectionModel().addListSelectionListener(e -> {
            // Ignora los eventos intermedios mientras se arrastra la selección.
            if (!e.getValueIsAdjusting()) {
                // Consulta y muestra las líneas de la compra.
                mostrarDetalleCompraSeleccionada();
            }
        });

        // Evita que los combos disparen consultas mientras se llenan.
        cargando = true;
        // Opciones del filtro: 'Todos' y los estados del ENUM.
        for (String estado : Compra_control.getFiltrosEstado()) {
            // Agrega cada opción.
            jComboBoxFiltroEstado.addItem(estado);
        }
        // Llena proveedores, almacenes y productos activos.
        cargarCombos();
        // A partir de aquí los combos responden normalmente.
        cargando = false;
        // Sugiere número de pedido y fecha, y deja el detalle vacío.
        prepararNuevaCompra();
        // Carga las compras registradas.
        cargarCompras();

        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableDetalle, jScrollPaneDetalle);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableCompras, jScrollPaneCompras);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableDetalleRegistrada, jScrollPaneDetalleRegistrada);
        // Total en naranja y negrita para destacarlo.
        jLabelTotal.setFont(TemaColores.FUENTE_BASE.deriveFont(java.awt.Font.BOLD, 19f));
        // Color de acento del tema.
        jLabelTotal.setForeground(TemaColores.ACENTO);
        // Ayuda del formato de fecha atenuada.
        jLabelFormatoFecha.setForeground(TemaColores.TEXTO_ATENUADO);
        // Título del detalle seleccionado en negrita.
        jLabelDetalleSeleccion.setFont(TemaColores.FUENTE_BASE.deriveFont(java.awt.Font.BOLD));
        // Centra la imagen del producto.
        jLabelImagenProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // Borde de la vista previa de la imagen.
        jLabelImagenProducto.setBorder(javax.swing.BorderFactory.createLineBorder(TemaColores.BORDE, 1));

        // Cerrar con la X equivale a "Volver al Menú".
        RecursosApp.cerrarConBoton(this, jButtonVolver);
        // Tamaño inicial 1200x760 (antes 1040x700) y mínimo 1000x660, limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 1200, 760, 1000, 660);
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
        jTabbedPaneCompras = new javax.swing.JTabbedPane();
        jPanelNuevaCompra = new javax.swing.JPanel();
        jPanelDatosCompra = new javax.swing.JPanel();
        jLabelNumeroPedido = new javax.swing.JLabel();
        jTextFieldNumeroPedido = new javax.swing.JTextField();
        jLabelProveedor = new javax.swing.JLabel();
        jComboBoxProveedor = new javax.swing.JComboBox<>();
        jLabelFechaPedido = new javax.swing.JLabel();
        jTextFieldFechaPedido = new javax.swing.JTextField();
        jLabelAlmacen = new javax.swing.JLabel();
        jComboBoxAlmacen = new javax.swing.JComboBox<>();
        jLabelFechaEntrega = new javax.swing.JLabel();
        jTextFieldFechaEntrega = new javax.swing.JTextField();
        jLabelComentarios = new javax.swing.JLabel();
        jTextFieldComentarios = new javax.swing.JTextField();
        jLabelFormatoFecha = new javax.swing.JLabel();
        jPanelDetalleCompra = new javax.swing.JPanel();
        jPanelAgregarProducto = new javax.swing.JPanel();
        jLabelImagenProducto = new javax.swing.JLabel();
        jLabelProducto = new javax.swing.JLabel();
        jComboBoxProducto = new javax.swing.JComboBox<>();
        jButtonBuscarProducto = new javax.swing.JButton();
        jLabelCantidad = new javax.swing.JLabel();
        jTextFieldCantidad = new javax.swing.JTextField();
        jLabelPrecioUnitario = new javax.swing.JLabel();
        jTextFieldPrecioUnitario = new javax.swing.JTextField();
        jButtonAgregarProducto = new javax.swing.JButton();
        jScrollPaneDetalle = new javax.swing.JScrollPane();
        jTableDetalle = new javax.swing.JTable();
        jPanelAccionesDetalle = new javax.swing.JPanel();
        jPanelBotonesDetalle = new javax.swing.JPanel();
        jButtonQuitarProducto = new javax.swing.JButton();
        jButtonLimpiarCompra = new javax.swing.JButton();
        jPanelGuardarCompra = new javax.swing.JPanel();
        jLabelTotal = new javax.swing.JLabel();
        jButtonGuardarCompra = new javax.swing.JButton();
        jPanelComprasRegistradas = new javax.swing.JPanel();
        jPanelFiltroCompras = new javax.swing.JPanel();
        jLabelFiltroEstado = new javax.swing.JLabel();
        jComboBoxFiltroEstado = new javax.swing.JComboBox<>();
        jButtonActualizarCompras = new javax.swing.JButton();
        jButtonMarcarEntregada = new javax.swing.JButton();
        jButtonCancelarCompra = new javax.swing.JButton();
        jScrollPaneCompras = new javax.swing.JScrollPane();
        jTableCompras = new javax.swing.JTable();
        jPanelDetalleRegistrada = new javax.swing.JPanel();
        jLabelDetalleSeleccion = new javax.swing.JLabel();
        jScrollPaneDetalleRegistrada = new javax.swing.JScrollPane();
        jTableDetalleRegistrada = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));
        jPanelTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));
        jPanelTitulo.setLayout(new java.awt.BorderLayout());
        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Compras");

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
        jPanelNuevaCompra.setBackground(new java.awt.Color(11, 18, 32));
        jPanelNuevaCompra.setLayout(new java.awt.BorderLayout());
        jPanelDatosCompra.setBackground(new java.awt.Color(11, 18, 32));
        jPanelDatosCompra.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 10, 6, 10));
        jPanelDatosCompra.setLayout(new java.awt.GridBagLayout());
        jLabelNumeroPedido.setText("N° pedido:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jLabelNumeroPedido, gridBagConstraints);
        jTextFieldNumeroPedido.setColumns(10);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jTextFieldNumeroPedido, gridBagConstraints);
        jLabelProveedor.setText("Proveedor:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelDatosCompra.add(jLabelProveedor, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jComboBoxProveedor, gridBagConstraints);
        jLabelFechaPedido.setText("Fecha pedido:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jLabelFechaPedido, gridBagConstraints);
        jTextFieldFechaPedido.setColumns(10);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jTextFieldFechaPedido, gridBagConstraints);
        jLabelAlmacen.setText("Almacén destino:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelDatosCompra.add(jLabelAlmacen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jComboBoxAlmacen, gridBagConstraints);
        jLabelFechaEntrega.setText("Fecha entrega:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jLabelFechaEntrega, gridBagConstraints);
        jTextFieldFechaEntrega.setColumns(10);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jTextFieldFechaEntrega, gridBagConstraints);
        jLabelComentarios.setText("Comentarios:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelDatosCompra.add(jLabelComentarios, gridBagConstraints);
        jTextFieldComentarios.setColumns(30);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jTextFieldComentarios, gridBagConstraints);
        jLabelFormatoFecha.setText("Fechas en formato AAAA-MM-DD. La fecha de entrega es opcional; al marcar la compra como entregada se registra la fecha real.");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelDatosCompra.add(jLabelFormatoFecha, gridBagConstraints);

        jPanelNuevaCompra.add(jPanelDatosCompra, java.awt.BorderLayout.NORTH);
        jPanelDetalleCompra.setBackground(new java.awt.Color(11, 18, 32));
        jPanelDetalleCompra.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 4, 10));
        jPanelDetalleCompra.setLayout(new java.awt.BorderLayout());
        jPanelAgregarProducto.setBackground(new java.awt.Color(11, 18, 32));
        jPanelAgregarProducto.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 0, 8, 0));
        jPanelAgregarProducto.setLayout(new java.awt.GridBagLayout());
        jLabelImagenProducto.setPreferredSize(new java.awt.Dimension(72, 72));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 12);
        jPanelAgregarProducto.add(jLabelImagenProducto, gridBagConstraints);
        jLabelProducto.setText("Producto:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelAgregarProducto.add(jLabelProducto, gridBagConstraints);
        jComboBoxProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProductoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelAgregarProducto.add(jComboBoxProducto, gridBagConstraints);
        jButtonBuscarProducto.setBackground(new java.awt.Color(15, 23, 42));
        jButtonBuscarProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonBuscarProducto.setText("Buscar producto...");
        jButtonBuscarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarProductoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelAgregarProducto.add(jButtonBuscarProducto, gridBagConstraints);
        jLabelCantidad.setText("Cantidad:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelAgregarProducto.add(jLabelCantidad, gridBagConstraints);
        jTextFieldCantidad.setColumns(6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelAgregarProducto.add(jTextFieldCantidad, gridBagConstraints);
        jLabelPrecioUnitario.setText("Precio unitario:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelAgregarProducto.add(jLabelPrecioUnitario, gridBagConstraints);
        jTextFieldPrecioUnitario.setColumns(10);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelAgregarProducto.add(jTextFieldPrecioUnitario, gridBagConstraints);
        jButtonAgregarProducto.setBackground(new java.awt.Color(237, 149, 62));
        jButtonAgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregarProducto.setText("Agregar al detalle");
        jButtonAgregarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarProductoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelAgregarProducto.add(jButtonAgregarProducto, gridBagConstraints);

        jPanelDetalleCompra.add(jPanelAgregarProducto, java.awt.BorderLayout.NORTH);

        jScrollPaneDetalle.setViewportView(jTableDetalle);

        jPanelDetalleCompra.add(jScrollPaneDetalle, java.awt.BorderLayout.CENTER);
        jPanelAccionesDetalle.setBackground(new java.awt.Color(11, 18, 32));
        jPanelAccionesDetalle.setLayout(new java.awt.BorderLayout());
        jPanelBotonesDetalle.setBackground(new java.awt.Color(11, 18, 32));
        jPanelBotonesDetalle.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 10));
        jButtonQuitarProducto.setBackground(new java.awt.Color(153, 0, 0));
        jButtonQuitarProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonQuitarProducto.setText("Quitar producto");
        jButtonQuitarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonQuitarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarProductoActionPerformed(evt);
            }
        });

        jPanelBotonesDetalle.add(jButtonQuitarProducto);
        jButtonLimpiarCompra.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarCompra.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarCompra.setText("Limpiar");
        jButtonLimpiarCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarCompraActionPerformed(evt);
            }
        });

        jPanelBotonesDetalle.add(jButtonLimpiarCompra);

        jPanelAccionesDetalle.add(jPanelBotonesDetalle, java.awt.BorderLayout.WEST);
        jPanelGuardarCompra.setBackground(new java.awt.Color(11, 18, 32));
        jPanelGuardarCompra.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 10));
        jLabelTotal.setText("Total: $ 0.00");

        jPanelGuardarCompra.add(jLabelTotal);
        jButtonGuardarCompra.setBackground(new java.awt.Color(237, 149, 62));
        jButtonGuardarCompra.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarCompra.setText("Guardar compra");
        jButtonGuardarCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGuardarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarCompraActionPerformed(evt);
            }
        });

        jPanelGuardarCompra.add(jButtonGuardarCompra);

        jPanelAccionesDetalle.add(jPanelGuardarCompra, java.awt.BorderLayout.EAST);

        jPanelDetalleCompra.add(jPanelAccionesDetalle, java.awt.BorderLayout.SOUTH);

        jPanelNuevaCompra.add(jPanelDetalleCompra, java.awt.BorderLayout.CENTER);

        jTabbedPaneCompras.addTab("Nueva compra", jPanelNuevaCompra);
        jPanelComprasRegistradas.setBackground(new java.awt.Color(11, 18, 32));
        jPanelComprasRegistradas.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10));
        jPanelComprasRegistradas.setLayout(new java.awt.BorderLayout());
        jPanelFiltroCompras.setBackground(new java.awt.Color(11, 18, 32));
        jPanelFiltroCompras.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 6, 0, 6));
        jPanelFiltroCompras.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 10));
        jLabelFiltroEstado.setText("Estado:");

        jPanelFiltroCompras.add(jLabelFiltroEstado);
        jComboBoxFiltroEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFiltroEstadoActionPerformed(evt);
            }
        });

        jPanelFiltroCompras.add(jComboBoxFiltroEstado);
        jButtonActualizarCompras.setBackground(new java.awt.Color(15, 23, 42));
        jButtonActualizarCompras.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizarCompras.setText("Actualizar");
        jButtonActualizarCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizarCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarComprasActionPerformed(evt);
            }
        });

        jPanelFiltroCompras.add(jButtonActualizarCompras);
        jButtonMarcarEntregada.setBackground(new java.awt.Color(237, 149, 62));
        jButtonMarcarEntregada.setForeground(new java.awt.Color(255, 255, 255));
        jButtonMarcarEntregada.setText("Marcar como entregada");
        jButtonMarcarEntregada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonMarcarEntregada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarcarEntregadaActionPerformed(evt);
            }
        });

        jPanelFiltroCompras.add(jButtonMarcarEntregada);
        jButtonCancelarCompra.setBackground(new java.awt.Color(153, 0, 0));
        jButtonCancelarCompra.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelarCompra.setText("Cancelar compra");
        jButtonCancelarCompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCancelarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarCompraActionPerformed(evt);
            }
        });

        jPanelFiltroCompras.add(jButtonCancelarCompra);

        jPanelComprasRegistradas.add(jPanelFiltroCompras, java.awt.BorderLayout.NORTH);

        jScrollPaneCompras.setViewportView(jTableCompras);

        jPanelComprasRegistradas.add(jScrollPaneCompras, java.awt.BorderLayout.CENTER);
        jPanelDetalleRegistrada.setBackground(new java.awt.Color(11, 18, 32));
        jPanelDetalleRegistrada.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 10, 0));
        jPanelDetalleRegistrada.setLayout(new java.awt.BorderLayout());
        jLabelDetalleSeleccion.setText("Seleccione una compra para ver su detalle");

        jPanelDetalleRegistrada.add(jLabelDetalleSeleccion, java.awt.BorderLayout.NORTH);

        jScrollPaneDetalleRegistrada.setViewportView(jTableDetalleRegistrada);

        jPanelDetalleRegistrada.add(jScrollPaneDetalleRegistrada, java.awt.BorderLayout.CENTER);

        jPanelComprasRegistradas.add(jPanelDetalleRegistrada, java.awt.BorderLayout.SOUTH);

        jTabbedPaneCompras.addTab("Compras registradas", jPanelComprasRegistradas);

        jPanel1.add(jTabbedPaneCompras, java.awt.BorderLayout.CENTER);

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
        // Cierra también la búsqueda de productos abierta desde esta compra.
        if (busquedaAbierta != null) {
            busquedaAbierta.dispose();
        }
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonVolverActionPerformed

    // Cambio de producto: muestra su imagen y su precio de compra.
    private void jComboBoxProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxProductoActionPerformed
        // JComboBox dispara este evento aunque se vuelva a elegir el mismo producto; en ese caso
        // no se reemplaza el precio unitario que el usuario ya digitó.
        Producto producto = (Producto) jComboBoxProducto.getSelectedItem();
        if (producto != null && producto.getIdProducto().equals(idProductoMostrado)) {
            return;
        }
        // Actualiza imagen y precio sugerido.
        mostrarProductoSeleccionado();
    }//GEN-LAST:event_jComboBoxProductoActionPerformed

    // Botón Buscar producto: abre la búsqueda enlazada a esta compra.
    private void jButtonBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarProductoActionPerformed
        // Si la búsqueda ya está abierta, solo se trae al frente (no se abren ventanas duplicadas).
        if (busquedaAbierta != null && busquedaAbierta.isDisplayable()) {
            busquedaAbierta.toFront();
            return;
        }
        // La búsqueda recibe esta ventana para devolver el producto elegido.
        Busqueda_Productos_Form busqueda = new Busqueda_Productos_Form(this);
        // Centra la búsqueda sobre la ventana de compras.
        busqueda.setLocationRelativeTo(this);
        // Muestra la búsqueda; esta ventana sigue abierta.
        busqueda.setVisible(true);
        // Recuerda la ventana para no abrir otra mientras siga abierta.
        busquedaAbierta = busqueda;
    }//GEN-LAST:event_jButtonBuscarProductoActionPerformed

    // Botón Agregar: agrega el producto al detalle del pedido.
    private void jButtonAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarProductoActionPerformed
        // Valida cantidad y precio y agrega la línea.
        agregarLineaDetalle();
    }//GEN-LAST:event_jButtonAgregarProductoActionPerformed

    // Botón Quitar: elimina la línea seleccionada del detalle.
    private void jButtonQuitarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarProductoActionPerformed
        // Quita la línea.
        quitarLineaDetalle();
    }//GEN-LAST:event_jButtonQuitarProductoActionPerformed

    // Botón Limpiar: descarta el pedido que se estaba armando.
    private void jButtonLimpiarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarCompraActionPerformed
        // Reinicia el formulario.
        prepararNuevaCompra();
    }//GEN-LAST:event_jButtonLimpiarCompraActionPerformed

    // Botón Guardar: registra la compra y sus líneas en MySQL.
    private void jButtonGuardarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarCompraActionPerformed
        // Valida y guarda en una transacción.
        guardarCompra();
    }//GEN-LAST:event_jButtonGuardarCompraActionPerformed

    // Cambio del filtro de estado: recarga la lista de compras.
    private void jComboBoxFiltroEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFiltroEstadoActionPerformed
        // No consulta mientras se llenan los combos.
        if (!cargando) {
            // Consulta con el estado elegido.
            cargarCompras();
        }
    }//GEN-LAST:event_jComboBoxFiltroEstadoActionPerformed

    // Botón Actualizar: vuelve a consultar las compras.
    private void jButtonActualizarComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarComprasActionPerformed
        // Recarga la tabla.
        cargarCompras();
    }//GEN-LAST:event_jButtonActualizarComprasActionPerformed

    // Botón Marcar entregada: suma las líneas al inventario del almacén destino y registra
    // movimientos 'entrada'; solo funciona con compras pendientes.
    private void jButtonMarcarEntregadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarcarEntregadaActionPerformed
        // Si se entregó, recarga la tabla para mostrar el nuevo estado.
        if (Compra_control.marcarEntregada(compraSeleccionada())) {
            // Recarga la tabla.
            cargarCompras();
        }
    }//GEN-LAST:event_jButtonMarcarEntregadaActionPerformed

    // Botón Cancelar compra: anula una compra pendiente sin tocar el inventario.
    private void jButtonCancelarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarCompraActionPerformed
        // Si se canceló, recarga la tabla.
        if (Compra_control.cancelarCompra(compraSeleccionada())) {
            // Recarga la tabla.
            cargarCompras();
        }
    }//GEN-LAST:event_jButtonCancelarCompraActionPerformed

    // Crea un modelo de solo lectura con las columnas indicadas.
    private DefaultTableModel crearModeloTabla(String... columnas) {
        // Modelo sin filas.
        return new DefaultTableModel(new Object[][]{}, columnas) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
    }

    // Llena los combos solo con proveedores, almacenes y productos activos.
    private void cargarCombos() {
        // Vacía proveedores.
        jComboBoxProveedor.removeAllItems();
        // Proveedores activos.
        for (Proveedor proveedor : Compra_control.listarProveedoresActivos()) {
            // Agrega cada proveedor.
            jComboBoxProveedor.addItem(proveedor);
        }
        // Vacía almacenes.
        jComboBoxAlmacen.removeAllItems();
        // Almacenes activos.
        for (Almacen almacen : Compra_control.listarAlmacenesActivos()) {
            // Agrega cada almacén.
            jComboBoxAlmacen.addItem(almacen);
        }
        // Vacía productos.
        jComboBoxProducto.removeAllItems();
        // Productos activos.
        for (Producto producto : Compra_control.listarProductosActivos()) {
            // Agrega cada producto.
            jComboBoxProducto.addItem(producto);
        }
    }

    // Deja el formulario listo para una compra nueva.
    private void prepararNuevaCompra() {
        // Sugiere MAX(numero_pedido) + 1.
        jTextFieldNumeroPedido.setText(String.valueOf(Compra_control.siguienteNumeroPedido()));
        // Fecha de hoy en formato AAAA-MM-DD.
        jTextFieldFechaPedido.setText(LocalDate.now().toString());
        // Fecha de entrega vacía (opcional).
        jTextFieldFechaEntrega.setText("");
        // Comentarios vacíos.
        jTextFieldComentarios.setText("");
        // Cantidad vacía.
        jTextFieldCantidad.setText("");
        // Descarta las líneas del pedido anterior.
        detallesNuevaCompra.clear();
        // Vacía la tabla de líneas y el total.
        refrescarDetalle();
        // Si hay proveedores.
        if (jComboBoxProveedor.getItemCount() > 0) {
            // Selecciona el primero.
            jComboBoxProveedor.setSelectedIndex(0);
        }
        // Si hay almacenes.
        if (jComboBoxAlmacen.getItemCount() > 0) {
            // Selecciona el primero.
            jComboBoxAlmacen.setSelectedIndex(0);
        }
        // Muestra imagen y precio del producto actual.
        mostrarProductoSeleccionado();
    }

    // Muestra la imagen del producto elegido y sugiere su precio de compra.
    private void mostrarProductoSeleccionado() {
        // Los combos se están llenando.
        if (cargando) {
            // No actualiza todavía.
            return;
        }
        // Producto elegido.
        Producto producto = (Producto) jComboBoxProducto.getSelectedItem();
        // Recuerda qué producto se mostró para no pisar después un precio digitado a mano.
        idProductoMostrado = producto == null ? null : producto.getIdProducto();
        // Imagen de 68x68 px con ImagenProducto (sin_imagen.png si no tiene).
        jLabelImagenProducto.setIcon(ImagenProducto.obtenerIcono(producto == null ? null : producto.getImagen(),
                68, 68));
        // Si el producto tiene precio de compra.
        if (producto != null && producto.getPrecioCompra() != null) {
            // Lo sugiere como precio unitario.
            jTextFieldPrecioUnitario.setText(producto.getPrecioCompra().toPlainString());
        // Sin precio registrado.
        } else {
            // Deja el precio vacío.
            jTextFieldPrecioUnitario.setText("");
        }
    }

    // La llama Busqueda_Productos_Form al elegir un producto: lo selecciona en el combo y deja
    // la cantidad en 1 lista para editar.
    public void agregarProductoDesdeBusqueda(Producto producto) {
        // Sin producto.
        if (producto == null) {
            // No hace nada.
            return;
        }
        // Busca el producto en el combo (solo contiene productos activos).
        for (int i = 0; i < jComboBoxProducto.getItemCount(); i++) {
            // Compara por id del producto.
            if (jComboBoxProducto.getItemAt(i).getIdProducto().equals(producto.getIdProducto())) {
                // Muestra la pestaña de nueva compra.
                jTabbedPaneCompras.setSelectedIndex(0);
                // Selecciona el producto.
                jComboBoxProducto.setSelectedIndex(i);
                // Muestra su imagen y precio.
                mostrarProductoSeleccionado();
                // Cantidad inicial 1.
                jTextFieldCantidad.setText("1");
                // Pone el cursor en la cantidad.
                jTextFieldCantidad.requestFocusInWindow();
                // Selecciona el texto para reemplazarlo al escribir.
                jTextFieldCantidad.selectAll();
                // Producto encontrado.
                return;
            }
        }
        // El producto está inactivo y no aparece en el combo.
        javax.swing.JOptionPane.showMessageDialog(this, "El producto \"" + producto.getNombre()
                + "\" no está activo y no se puede agregar a una compra", "Producto no disponible",
                javax.swing.JOptionPane.WARNING_MESSAGE);
    }

    // Agrega una línea al pedido; si el producto ya está con el mismo precio, suma la cantidad.
    private void agregarLineaDetalle() {
        // Valida cantidad y precio y crea la línea; null si algo es inválido (ya informado).
        DetalleCompra linea = Compra_control.crearLineaDetalle((Producto) jComboBoxProducto.getSelectedItem(),
                jTextFieldCantidad.getText(), jTextFieldPrecioUnitario.getText());
        // Datos inválidos.
        if (linea == null) {
            // No agrega nada.
            return;
        }
        // Recorre las líneas existentes.
        for (DetalleCompra existente : detallesNuevaCompra) {
            // El producto ya está en el pedido.
            if (existente.getIdProducto().equals(linea.getIdProducto())) {
                // Con otro precio no se combinan las líneas.
                if (existente.getPrecioUnitario().compareTo(linea.getPrecioUnitario()) != 0) {
                    // Avisa que debe quitar la línea anterior.
                    javax.swing.JOptionPane.showMessageDialog(this, "El producto ya está en el detalle con otro "
                            + "precio. Quite la línea anterior si desea cambiar el precio.", "Producto repetido",
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    // No agrega nada.
                    return;
                }
                // Mismo precio: suma la cantidad a la línea existente.
                existente.setCantidad(existente.getCantidad() + linea.getCantidad());
                // Actualiza tabla y total.
                refrescarDetalle();
                // Vacía la cantidad.
                jTextFieldCantidad.setText("");
                // Termina.
                return;
            }
        }
        // Producto nuevo en el pedido: agrega la línea.
        detallesNuevaCompra.add(linea);
        // Actualiza tabla y total.
        refrescarDetalle();
        // Vacía la cantidad.
        jTextFieldCantidad.setText("");
    }

    // Quita del pedido la línea seleccionada.
    private void quitarLineaDetalle() {
        // Fila elegida en la tabla de líneas.
        int fila = jTableDetalle.getSelectedRow();
        // Sin selección válida.
        if (fila < 0 || fila >= detallesNuevaCompra.size()) {
            // Pide seleccionar una línea.
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione una línea del detalle para quitarla",
                    "Línea no seleccionada", javax.swing.JOptionPane.WARNING_MESSAGE);
            // No quita nada.
            return;
        }
        // Quita la línea de la lista.
        detallesNuevaCompra.remove(fila);
        // Actualiza tabla y total.
        refrescarDetalle();
    }

    // Vuelve a dibujar las líneas del pedido y recalcula el total.
    private void refrescarDetalle() {
        // Modelo de la tabla de líneas.
        DefaultTableModel modelo = (DefaultTableModel) jTableDetalle.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);
        // Agrega una fila por línea.
        for (DetalleCompra detalle : detallesNuevaCompra) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                detalle.getNombreProducto(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
            });
        }
        // Suma de los subtotales.
        BigDecimal total = Compra_control.calcularTotal(detallesNuevaCompra);
        // Total con separador de miles y 2 decimales.
        jLabelTotal.setText("Total: $ " + String.format("%,.2f", total));
    }

    // Envía el pedido al controlador; si se guardó, limpia el formulario y recarga la lista.
    private void guardarCompra() {
        // Valida número, fechas, proveedor, almacén y líneas; inserta compras y detallecompra
        // en una transacción. Devuelve el id o null.
        Integer idCompra = Compra_control.registrarCompra(
                jTextFieldNumeroPedido.getText(),
                (Proveedor) jComboBoxProveedor.getSelectedItem(),
                (Almacen) jComboBoxAlmacen.getSelectedItem(),
                jTextFieldFechaPedido.getText(),
                jTextFieldFechaEntrega.getText(),
                jTextFieldComentarios.getText(),
                detallesNuevaCompra);
        // Si se guardó.
        if (idCompra != null) {
            // Deja el formulario listo para otra compra.
            prepararNuevaCompra();
            // Muestra la compra nueva en la lista.
            cargarCompras();
        }
    }

    // Consulta las compras con el filtro de estado y llena la tabla.
    private void cargarCompras() {
        // SELECT con JOIN a proveedores y almacenes.
        listaCompras = Compra_control.listarCompras((String) jComboBoxFiltroEstado.getSelectedItem());
        // Modelo de la tabla de compras.
        DefaultTableModel modelo = (DefaultTableModel) jTableCompras.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);
        // Agrega una fila por compra.
        for (Compra compra : listaCompras) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                compra.getIdCompra(),
                compra.getNumeroPedido(),
                compra.getNombreProveedor(),
                compra.getNombreAlmacen(),
                // Fecha del pedido formateada o vacía.
                compra.getFechaPedido() == null ? "" : formatoFecha.format(compra.getFechaPedido()),
                // Fecha de entrega formateada o vacía.
                compra.getFechaEntrega() == null ? "" : formatoFecha.format(compra.getFechaEntrega()),
                compra.getValorTotal(),
                compra.getEstado()
            });
        }
        // Limpia o actualiza el detalle según la selección.
        mostrarDetalleCompraSeleccionada();
    }

    // Compra de la fila seleccionada; null si no hay selección.
    private Compra compraSeleccionada() {
        // Fila elegida.
        int fila = jTableCompras.getSelectedRow();
        // Sin selección válida.
        if (fila < 0 || fila >= listaCompras.size()) {
            // No hay compra.
            return null;
        }
        // Compra de esa fila.
        return listaCompras.get(fila);
    }

    // Muestra las líneas guardadas de la compra seleccionada.
    private void mostrarDetalleCompraSeleccionada() {
        // Modelo del detalle registrado.
        DefaultTableModel modelo = (DefaultTableModel) jTableDetalleRegistrada.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);
        // Compra elegida.
        Compra compra = compraSeleccionada();
        // Sin selección.
        if (compra == null) {
            // Indica que se debe elegir una compra.
            jLabelDetalleSeleccion.setText("Seleccione una compra para ver su detalle");
            // Termina.
            return;
        }
        // Consulta el detalle en detallecompra.
        for (DetalleCompra detalle : Compra_control.listarDetalle(compra.getIdCompra())) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                detalle.getNombreProducto(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
            });
        }
        // Agrega los comentarios si existen.
        String comentarios = compra.getComentarios() == null ? "" : "  |  " + compra.getComentarios();
        // Título con número de pedido, estado y comentarios.
        jLabelDetalleSeleccion.setText("Detalle del pedido N° " + compra.getNumeroPedido() + " ("
                + compra.getEstado() + ")" + comentarios);
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
            java.util.logging.Logger.getLogger(Compras_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Compras_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Compras_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Compras_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Compras_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonActualizarCompras;
    private javax.swing.JButton jButtonAgregarProducto;
    private javax.swing.JButton jButtonBuscarProducto;
    private javax.swing.JButton jButtonCancelarCompra;
    private javax.swing.JButton jButtonGuardarCompra;
    private javax.swing.JButton jButtonLimpiarCompra;
    private javax.swing.JButton jButtonMarcarEntregada;
    private javax.swing.JButton jButtonQuitarProducto;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<Almacen> jComboBoxAlmacen;
    private javax.swing.JComboBox<String> jComboBoxFiltroEstado;
    private javax.swing.JComboBox<Producto> jComboBoxProducto;
    private javax.swing.JComboBox<Proveedor> jComboBoxProveedor;
    private javax.swing.JLabel jLabelAlmacen;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelComentarios;
    private javax.swing.JLabel jLabelDetalleSeleccion;
    private javax.swing.JLabel jLabelFechaEntrega;
    private javax.swing.JLabel jLabelFechaPedido;
    private javax.swing.JLabel jLabelFiltroEstado;
    private javax.swing.JLabel jLabelFormatoFecha;
    private javax.swing.JLabel jLabelImagenProducto;
    private javax.swing.JLabel jLabelNumeroPedido;
    private javax.swing.JLabel jLabelPrecioUnitario;
    private javax.swing.JLabel jLabelProducto;
    private javax.swing.JLabel jLabelProveedor;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAccionesDetalle;
    private javax.swing.JPanel jPanelAgregarProducto;
    private javax.swing.JPanel jPanelBotonesDetalle;
    private javax.swing.JPanel jPanelComprasRegistradas;
    private javax.swing.JPanel jPanelDatosCompra;
    private javax.swing.JPanel jPanelDetalleCompra;
    private javax.swing.JPanel jPanelDetalleRegistrada;
    private javax.swing.JPanel jPanelFiltroCompras;
    private javax.swing.JPanel jPanelGuardarCompra;
    private javax.swing.JPanel jPanelNuevaCompra;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPaneCompras;
    private javax.swing.JScrollPane jScrollPaneDetalle;
    private javax.swing.JScrollPane jScrollPaneDetalleRegistrada;
    private javax.swing.JTabbedPane jTabbedPaneCompras;
    private javax.swing.JTable jTableCompras;
    private javax.swing.JTable jTableDetalle;
    private javax.swing.JTable jTableDetalleRegistrada;
    private javax.swing.JTextField jTextFieldCantidad;
    private javax.swing.JTextField jTextFieldComentarios;
    private javax.swing.JTextField jTextFieldFechaEntrega;
    private javax.swing.JTextField jTextFieldFechaPedido;
    private javax.swing.JTextField jTextFieldNumeroPedido;
    private javax.swing.JTextField jTextFieldPrecioUnitario;
    // End of variables declaration//GEN-END:variables
}
