// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// BusquedaProductos_control arma la búsqueda con filtros y consulta existencias.
import controlador.BusquedaProductos_control;
// BorderLayout: distribución imagen arriba y datos abajo en cada tarjeta.
import java.awt.BorderLayout;
// Cursor: cursor de mano sobre las tarjetas.
import java.awt.Cursor;
// Font: estilos en negrita de títulos y nombres.
import java.awt.Font;
// GridLayout: filas de texto dentro de cada tarjeta.
import java.awt.GridLayout;
// MouseAdapter: escucha de clics sobre las tarjetas.
import java.awt.event.MouseAdapter;
// MouseEvent: información del clic (cantidad de clics).
import java.awt.event.MouseEvent;
// ArrayList: productos encontrados.
import java.util.ArrayList;
// BorderFactory crea los bordes de tarjetas y paneles.
import javax.swing.BorderFactory;
// JLabel: textos e imagen de cada tarjeta.
import javax.swing.JLabel;
// JPanel: tarjeta de cada producto.
import javax.swing.JPanel;
// SwingConstants: alineación de las imágenes.
import javax.swing.SwingConstants;
// DefaultTableModel: modelo de la tabla de existencias por almacén.
import javax.swing.table.DefaultTableModel;
// Categoria: elementos del combo de categoría.
import modelo.Categoria;
// InventarioPorAlmacen: filas de existencias del producto elegido.
import modelo.InventarioPorAlmacen;
// Producto: resultados de la búsqueda.
import modelo.Producto;

// Búsqueda visual de productos en tarjetas con imagen; muestra el detalle y las existencias
// por almacén. Abierta desde Compras_Form, el doble clic agrega el producto a esa compra.
public class Busqueda_Productos_Form extends javax.swing.JFrame {
    // Compra que abrió la búsqueda; null si se abrió desde el menú principal.
    private final Compras_Form compraDestino;

    // Producto que se muestra en el panel de detalle.
    private Producto productoSeleccionado;
    // Tarjeta resaltada con borde naranja.
    private JPanel tarjetaSeleccionada;

    // true mientras se llenan los combos, para ignorar los eventos que disparan.
    private boolean cargando;

    // Constructor usado por Home_Form: búsqueda sin compra asociada.
    public Busqueda_Productos_Form() {
        // Delega en el constructor con compra.
        this(null);
    }

    // Constructor usado por Compras_Form: guarda la compra destino y arma la ventana.
    public Busqueda_Productos_Form(Compras_Form compraDestino) {
        // Compra a la que se enviará el producto elegido.
        this.compraDestino = compraDestino;
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Búsqueda de productos");
        // Abierta desde Compras: el botón solo cierra la búsqueda.
        if (compraDestino != null) {
            // Cambia el texto del botón a 'Cerrar'.
            jButtonVolver.setText("Cerrar");
        }

        // Modelo de la tabla de existencias con sus columnas.
        jTableExistencias.setModel(new DefaultTableModel(new Object[][]{},
                new String[]{"Almacén", "Disponible", "Reservada"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        });
        // Tamaño visible de la tabla de existencias.
        jTableExistencias.setPreferredScrollableViewportSize(new java.awt.Dimension(340, 100));
        // Desplazamiento de 16 px por paso de la rueda del ratón.
        jScrollPaneResultados.getVerticalScrollBar().setUnitIncrement(16);
        // Fondo oscuro del área de tarjetas.
        jScrollPaneResultados.getViewport().setBackground(TemaColores.FONDO_PRINCIPAL);
        // Borde del área de tarjetas.
        jScrollPaneResultados.setBorder(BorderFactory.createLineBorder(TemaColores.BORDE, 1));

        // Evita que los combos lancen búsquedas mientras se llenan.
        cargando = true;
        // Primera opción del combo de categoría: 'Todos'.
        jComboBoxCategoria.addItem(BusquedaProductos_control.FILTRO_TODOS);
        // Categorías de la tabla categorias.
        for (Categoria categoria : BusquedaProductos_control.listarCategorias()) {
            // Agrega cada categoría.
            jComboBoxCategoria.addItem(categoria);
        }
        // Opciones del filtro de estado del producto.
        for (String estado : BusquedaProductos_control.getFiltrosEstado()) {
            // Agrega cada opción.
            jComboBoxEstado.addItem(estado);
        }
        // A partir de aquí los combos responden normalmente.
        cargando = false;

        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableExistencias, jScrollPaneExistencias);
        // Contador de resultados atenuado.
        jLabelResultados.setForeground(TemaColores.TEXTO_ATENUADO);
        // Nombre del producto en el detalle en negrita.
        jLabelNombreDetalle.setFont(TemaColores.FUENTE_BASE.deriveFont(Font.BOLD, 19f));
        // Precio de venta en naranja.
        jLabelPrecioVentaDetalle.setForeground(TemaColores.ACENTO);
        // Título de existencias en negrita.
        jLabelExistenciasTitulo.setFont(TemaColores.FUENTE_BASE.deriveFont(Font.BOLD));
        // Centra la imagen del detalle.
        jLabelImagenDetalle.setHorizontalAlignment(SwingConstants.CENTER);
        // Borde de la imagen del detalle.
        jLabelImagenDetalle.setBorder(BorderFactory.createLineBorder(TemaColores.BORDE, 1));

        // Panel de detalle vacío al inicio.
        mostrarDetalle(null);
        // Muestra todos los productos.
        buscarProductos();

        // Al cambiar el alto del área de resultados se recalcula el relleno de la cuadrícula.
        jScrollPaneResultados.getViewport().addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                completarCuadricula();
            }
        });
        // Cerrar con la X equivale al botón "Volver al Menú" / "Cerrar".
        RecursosApp.cerrarConBoton(this, jButtonVolver);
        // Tamaño inicial 1280x780 (antes 1180x720) y mínimo 1080x660, limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 1280, 780, 1080, 660);
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
        jPanelFiltrosBusqueda = new javax.swing.JPanel();
        jLabelBuscar = new javax.swing.JLabel();
        jTextFieldBuscar = new javax.swing.JTextField();
        jLabelCategoria = new javax.swing.JLabel();
        jComboBoxCategoria = new javax.swing.JComboBox<>();
        jLabelEstado = new javax.swing.JLabel();
        jComboBoxEstado = new javax.swing.JComboBox<>();
        jButtonBuscar = new javax.swing.JButton();
        jButtonLimpiarBusqueda = new javax.swing.JButton();
        jLabelResultados = new javax.swing.JLabel();
        jScrollPaneResultados = new javax.swing.JScrollPane();
        jPanelResultados = new javax.swing.JPanel();
        jPanelDetalleProducto = new javax.swing.JPanel();
        jLabelImagenDetalle = new javax.swing.JLabel();
        jLabelNombreDetalle = new javax.swing.JLabel();
        jLabelCategoriaDetalle = new javax.swing.JLabel();
        jLabelEstadoDetalle = new javax.swing.JLabel();
        jLabelPrecioCompraDetalle = new javax.swing.JLabel();
        jLabelPrecioVentaDetalle = new javax.swing.JLabel();
        jLabelCantidadDetalle = new javax.swing.JLabel();
        jLabelDescripcionDetalle = new javax.swing.JLabel();
        jLabelExistenciasTitulo = new javax.swing.JLabel();
        jScrollPaneExistencias = new javax.swing.JScrollPane();
        jTableExistencias = new javax.swing.JTable();
        jButtonAgregarACompra = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));
        jPanelTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 14, 20));
        jPanelTitulo.setLayout(new java.awt.BorderLayout());
        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Búsqueda de productos");

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
        jPanelContenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanelContenido.setLayout(new java.awt.BorderLayout());
        jPanelFiltrosBusqueda.setBackground(new java.awt.Color(11, 18, 32));
        jPanelFiltrosBusqueda.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 8, 0));
        jPanelFiltrosBusqueda.setLayout(new java.awt.GridBagLayout());
        jLabelBuscar.setText("Buscar:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosBusqueda.add(jLabelBuscar, gridBagConstraints);
        jTextFieldBuscar.setColumns(24);
        jTextFieldBuscar.setToolTipText("Nombre, descripción o categoría");
        jTextFieldBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldBuscarKeyReleased(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosBusqueda.add(jTextFieldBuscar, gridBagConstraints);
        jLabelCategoria.setText("Categoría:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFiltrosBusqueda.add(jLabelCategoria, gridBagConstraints);
        jComboBoxCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCategoriaActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosBusqueda.add(jComboBoxCategoria, gridBagConstraints);
        jLabelEstado.setText("Estado:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFiltrosBusqueda.add(jLabelEstado, gridBagConstraints);
        jComboBoxEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxEstadoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosBusqueda.add(jComboBoxEstado, gridBagConstraints);
        jButtonBuscar.setBackground(new java.awt.Color(237, 149, 62));
        jButtonBuscar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonBuscar.setText("Buscar");
        jButtonBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBuscarActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 4, 6);
        jPanelFiltrosBusqueda.add(jButtonBuscar, gridBagConstraints);
        jButtonLimpiarBusqueda.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarBusqueda.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarBusqueda.setText("Limpiar");
        jButtonLimpiarBusqueda.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarBusquedaActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosBusqueda.add(jButtonLimpiarBusqueda, gridBagConstraints);
        jLabelResultados.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        jPanelFiltrosBusqueda.add(jLabelResultados, gridBagConstraints);

        jPanelContenido.add(jPanelFiltrosBusqueda, java.awt.BorderLayout.NORTH);
        jPanelResultados.setBackground(new java.awt.Color(11, 18, 32));
        jPanelResultados.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        jPanelResultados.setLayout(new java.awt.GridLayout(0, 3, 12, 12));

        jScrollPaneResultados.setViewportView(jPanelResultados);

        jPanelContenido.add(jScrollPaneResultados, java.awt.BorderLayout.CENTER);
        jPanelDetalleProducto.setBackground(new java.awt.Color(15, 23, 42));
        jPanelDetalleProducto.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 14, 14, 14));
        jPanelDetalleProducto.setPreferredSize(new java.awt.Dimension(380, 500));
        jPanelDetalleProducto.setLayout(new java.awt.GridBagLayout());
        jLabelImagenDetalle.setPreferredSize(new java.awt.Dimension(156, 156));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 10, 6);
        jPanelDetalleProducto.add(jLabelImagenDetalle, gridBagConstraints);
        jLabelNombreDetalle.setText("Seleccione un producto");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelNombreDetalle, gridBagConstraints);
        jLabelCategoriaDetalle.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelCategoriaDetalle, gridBagConstraints);
        jLabelEstadoDetalle.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelEstadoDetalle, gridBagConstraints);
        jLabelPrecioCompraDetalle.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelPrecioCompraDetalle, gridBagConstraints);
        jLabelPrecioVentaDetalle.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelPrecioVentaDetalle, gridBagConstraints);
        jLabelCantidadDetalle.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelCantidadDetalle, gridBagConstraints);
        jLabelDescripcionDetalle.setText(" ");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelDescripcionDetalle, gridBagConstraints);
        jLabelExistenciasTitulo.setText("Existencias por almacén:");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 3, 0);
        jPanelDetalleProducto.add(jLabelExistenciasTitulo, gridBagConstraints);

        jScrollPaneExistencias.setViewportView(jTableExistencias);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanelDetalleProducto.add(jScrollPaneExistencias, gridBagConstraints);
        jButtonAgregarACompra.setBackground(new java.awt.Color(237, 149, 62));
        jButtonAgregarACompra.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAgregarACompra.setText("Agregar a compra");
        jButtonAgregarACompra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAgregarACompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAgregarACompraActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 3, 0);
        jPanelDetalleProducto.add(jButtonAgregarACompra, gridBagConstraints);

        jPanelContenido.add(jPanelDetalleProducto, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanelContenido, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Botón Volver/Cerrar: cierra la búsqueda o regresa al menú principal.
    private void jButtonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVolverActionPerformed

        // Si se abrió desde Compras.
        if (compraDestino != null) {
            // Solo cierra la búsqueda; la compra sigue abierta.
            this.dispose();
            // No abre el menú.
            return;
        }

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

    // Búsqueda mientras se escribe: filtra en cada tecla soltada.
    private void jTextFieldBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBuscarKeyReleased
        // Consulta con el texto actual.
        buscarProductos();
    }//GEN-LAST:event_jTextFieldBuscarKeyReleased

    // Cambio de categoría: repite la búsqueda.
    private void jComboBoxCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCategoriaActionPerformed
        // No busca mientras se llenan los combos.
        if (!cargando) {
            // Consulta con la categoría elegida.
            buscarProductos();
        }
    }//GEN-LAST:event_jComboBoxCategoriaActionPerformed

    // Cambio de estado: repite la búsqueda.
    private void jComboBoxEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxEstadoActionPerformed
        // No busca mientras se llenan los combos.
        if (!cargando) {
            // Consulta con el estado elegido.
            buscarProductos();
        }
    }//GEN-LAST:event_jComboBoxEstadoActionPerformed

    // Botón Buscar: repite la búsqueda.
    private void jButtonBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBuscarActionPerformed
        // Consulta con los filtros actuales.
        buscarProductos();
    }//GEN-LAST:event_jButtonBuscarActionPerformed

    // Botón Limpiar: quita los filtros y muestra todos los productos.
    private void jButtonLimpiarBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarBusquedaActionPerformed
        // Evita una búsqueda por cada combo reiniciado.
        cargando = true;
        // Vacía el texto.
        jTextFieldBuscar.setText("");
        // Categoría 'Todos'.
        jComboBoxCategoria.setSelectedIndex(0);
        // Estado 'Todos'.
        jComboBoxEstado.setSelectedIndex(0);
        // Reactiva los eventos de los combos.
        cargando = false;
        // Una sola búsqueda con los filtros limpios.
        buscarProductos();
    }//GEN-LAST:event_jButtonLimpiarBusquedaActionPerformed

    // Botón Agregar a compra: envía el producto elegido a Compras_Form.
    private void jButtonAgregarACompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAgregarACompraActionPerformed
        // Valida y envía el producto.
        agregarACompra();
    }//GEN-LAST:event_jButtonAgregarACompraActionPerformed

    // Consulta los productos con los filtros y vuelve a dibujar las tarjetas.
    private void buscarProductos() {
        // Categoría elegida o el texto 'Todos'.
        Object categoria = jComboBoxCategoria.getSelectedItem();
        // SELECT con LIKE por nombre, descripción o categoría; 'Todos' se envía como null.
        ArrayList<Producto> productos = BusquedaProductos_control.buscar(jTextFieldBuscar.getText(),
                categoria instanceof Categoria ? (Categoria) categoria : null,
                (String) jComboBoxEstado.getSelectedItem());

        // Quita las tarjetas anteriores.
        jPanelResultados.removeAll();
        // Ninguna tarjeta resaltada por ahora.
        tarjetaSeleccionada = null;
        // Id del producto que estaba en el detalle, para conservarlo si sigue en los resultados.
        Integer idAnterior = productoSeleccionado == null ? null : productoSeleccionado.getIdProducto();
        // Producto anterior si aparece en la nueva búsqueda.
        Producto sigueVisible = null;
        // Crea una tarjeta por producto.
        for (Producto producto : productos) {
            // Tarjeta con imagen y datos.
            JPanel tarjeta = crearTarjeta(producto);
            // Agrega la tarjeta al panel.
            jPanelResultados.add(tarjeta);
            // Es el producto que estaba seleccionado.
            if (producto.getIdProducto().equals(idAnterior)) {
                // Se mantiene en el detalle.
                sigueVisible = producto;
                // Se marca como tarjeta seleccionada.
                tarjetaSeleccionada = tarjeta;
                // Borde naranja de selección.
                tarjeta.setBorder(bordeTarjeta(true));
            }
        }

        // Rellena la cuadrícula para que pocas tarjetas no se estiren a todo el ancho ni a todo el alto.
        completarCuadricula();
        // Muestra el producto que sigue visible o deja el detalle vacío.
        mostrarDetalle(sigueVisible);

        // Texto con la cantidad de resultados y, en modo compra, la ayuda del doble clic.
        jLabelResultados.setText(productos.isEmpty() ? "No se encontraron productos con esos filtros"
                : productos.size() + (productos.size() == 1 ? " producto encontrado" : " productos encontrados")
                + (compraDestino != null ? "  -  doble clic en una tarjeta para agregarla a la compra" : ""));
        // Recalcula la distribución de las tarjetas.
        jPanelResultados.revalidate();
        // Vuelve a pintar el panel.
        jPanelResultados.repaint();
    }

    // Agrega celdas vacías hasta completar las filas que caben en el área visible: el GridLayout
    // reparte todo el alto entre las filas y, con pocos resultados, las tarjetas quedaban estiradas.
    private void completarCuadricula() {
        // Quita el relleno anterior y cuenta las tarjetas reales.
        for (java.awt.Component celda : jPanelResultados.getComponents()) {
            if (Boolean.TRUE.equals(((JPanel) celda).getClientProperty("relleno"))) {
                jPanelResultados.remove(celda);
            }
        }
        int cantidadTarjetas = jPanelResultados.getComponentCount();
        // Columnas de la cuadrícula definidas en el .form (GridLayout de 3 columnas).
        int columnas = 3;
        // Alto de una tarjeta más el espacio vertical entre filas.
        int altoFila = (cantidadTarjetas > 0 ? jPanelResultados.getComponent(0).getPreferredSize().height : 250) + 12;
        // Filas que caben en el área visible (2 mientras la ventana todavía no se muestra).
        int altoVisible = jScrollPaneResultados.getViewport().getHeight();
        int filasVisibles = altoVisible > 0 ? Math.max(1, altoVisible / altoFila) : 2;
        // Filas necesarias para las tarjetas reales.
        int filasTarjetas = (cantidadTarjetas + columnas - 1) / columnas;
        int celdas = Math.max(filasTarjetas, filasVisibles) * columnas;
        for (int i = cantidadTarjetas; i < celdas; i++) {
            // Panel vacío y transparente que ocupa un espacio de la cuadrícula.
            JPanel vacio = new JPanel();
            vacio.setOpaque(false);
            vacio.putClientProperty("relleno", Boolean.TRUE);
            jPanelResultados.add(vacio);
        }
        jPanelResultados.revalidate();
    }

    // Crea la tarjeta de un producto: imagen, nombre, categoría, precio y existencia.
    private JPanel crearTarjeta(final Producto producto) {
        // Panel con 6 px de separación entre imagen y datos.
        final JPanel tarjeta = new JPanel(new BorderLayout(0, 6));
        // Fondo de tarjeta del tema.
        tarjeta.setBackground(TemaColores.FONDO_TARJETA);
        // Borde normal.
        tarjeta.setBorder(bordeTarjeta(false));
        // Cursor de mano para indicar que se puede hacer clic.
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Imagen de 130x130 px (sin_imagen.png si no tiene).
        JLabel imagen = new JLabel(ImagenProducto.obtenerIcono(producto.getImagen(), 130, 130));
        // Centra la imagen.
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        // Espacio fijo para la imagen.
        imagen.setPreferredSize(new java.awt.Dimension(140, 140));
        // Imagen en la parte superior.
        tarjeta.add(imagen, BorderLayout.NORTH);

        // Panel de textos en una columna.
        JPanel datos = new JPanel(new GridLayout(0, 1, 0, 2));
        // Transparente para mostrar el fondo de la tarjeta.
        datos.setOpaque(false);
        // Nombre del producto en blanco.
        JLabel nombre = crearEtiqueta(producto.getNombre(), TemaColores.TEXTO);
        // Nombre en negrita de 14 puntos.
        nombre.setFont(TemaColores.FUENTE_BASE.deriveFont(Font.BOLD, 16f));
        // Muestra el nombre completo al pasar el ratón si se recorta.
        nombre.setToolTipText(producto.getNombre());
        // Agrega el nombre.
        datos.add(nombre);
        // Categoría en gris atenuado.
        datos.add(crearEtiqueta(producto.getNombreCategoria(), TemaColores.TEXTO_ATENUADO));
        // Precio de venta en naranja.
        datos.add(crearEtiqueta("Precio venta: $ " + formatearPrecio(producto.getPrecioVenta()), TemaColores.ACENTO));
        // Existencia total y estado.
        datos.add(crearEtiqueta("Existencia: " + producto.getCantidad() + "  |  " + producto.getEstado(),
                TemaColores.TEXTO_SECUNDARIO));
        // Textos debajo de la imagen.
        tarjeta.add(datos, BorderLayout.CENTER);

        // Ancho fijo de 240 px por tarjeta.
        tarjeta.setPreferredSize(new java.awt.Dimension(240, tarjeta.getPreferredSize().height));

        // Escucha los clics sobre la tarjeta.
        tarjeta.addMouseListener(new MouseAdapter() {
            // Se ejecuta en cada clic.
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Un clic: selecciona la tarjeta y muestra el detalle.
                seleccionarTarjeta(tarjeta, producto);
                // Doble clic.
                if (evt.getClickCount() == 2) {
                    // Envía el producto a la compra.
                    agregarACompra();
                }
            }
        });
        // Tarjeta lista.
        return tarjeta;
    }

    // Crea una etiqueta con el color indicado y la fuente base.
    private JLabel crearEtiqueta(String texto, java.awt.Color color) {
        // Texto vacío si llega null.
        JLabel etiqueta = new JLabel(texto == null ? "" : texto);
        // Color del texto.
        etiqueta.setForeground(color);
        // Fuente base.
        etiqueta.setFont(TemaColores.FUENTE_BASE);
        // Etiqueta lista.
        return etiqueta;
    }

    // Borde de tarjeta: naranja de 2 px si está seleccionada, gris de 1 px si no.
    private javax.swing.border.Border bordeTarjeta(boolean seleccionada) {
        // Borde exterior de color más margen interno.
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(seleccionada ? TemaColores.ACENTO : TemaColores.BORDE,
                        seleccionada ? 2 : 1),
                BorderFactory.createEmptyBorder(8, 10, 10, 10));
    }

    // Resalta la tarjeta elegida, quita el resaltado anterior y muestra el detalle.
    private void seleccionarTarjeta(JPanel tarjeta, Producto producto) {
        // Si había otra tarjeta resaltada.
        if (tarjetaSeleccionada != null) {
            // Le devuelve el borde normal.
            tarjetaSeleccionada.setBorder(bordeTarjeta(false));
        }
        // Guarda la nueva tarjeta seleccionada.
        tarjetaSeleccionada = tarjeta;
        // Borde naranja.
        tarjeta.setBorder(bordeTarjeta(true));
        // Muestra el detalle del producto.
        mostrarDetalle(producto);
    }

    // Llena el panel de detalle y la tabla de existencias; con null lo deja vacío.
    private void mostrarDetalle(Producto producto) {
        // Producto que se agregaría a la compra.
        productoSeleccionado = producto;
        // Modelo de existencias.
        DefaultTableModel modelo = (DefaultTableModel) jTableExistencias.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);
        // Imagen grande de 150x150 px (cabe junto a la tabla de existencias en pantallas de 768 px).
        jLabelImagenDetalle.setIcon(ImagenProducto.obtenerIcono(producto == null ? null : producto.getImagen(),
                150, 150));
        // Sin producto elegido.
        if (producto == null) {
            // Indica que se debe elegir un producto.
            jLabelNombreDetalle.setText("Seleccione un producto");
            // Vacía la categoría (espacio para conservar la altura).
            jLabelCategoriaDetalle.setText(" ");
            // Vacía el estado.
            jLabelEstadoDetalle.setText(" ");
            // Vacía el precio de compra.
            jLabelPrecioCompraDetalle.setText(" ");
            // Vacía el precio de venta.
            jLabelPrecioVentaDetalle.setText(" ");
            // Vacía la cantidad.
            jLabelCantidadDetalle.setText(" ");
            // Vacía la descripción.
            jLabelDescripcionDetalle.setText(" ");
            // No se puede agregar nada a la compra.
            jButtonAgregarACompra.setEnabled(false);
            // Termina.
            return;
        }
        // Nombre del producto.
        jLabelNombreDetalle.setText(producto.getNombre());
        // Nombre completo al pasar el ratón.
        jLabelNombreDetalle.setToolTipText(producto.getNombre());
        // Categoría.
        jLabelCategoriaDetalle.setText("Categoría: " + producto.getNombreCategoria());
        // Estado (activo o descontinuado).
        jLabelEstadoDetalle.setText("Estado: " + producto.getEstado());
        // Precio de compra.
        jLabelPrecioCompraDetalle.setText("Precio compra: $ " + formatearPrecio(producto.getPrecioCompra()));
        // Precio de venta.
        jLabelPrecioVentaDetalle.setText("Precio venta: $ " + formatearPrecio(producto.getPrecioVenta()));
        // Cantidad total en productos.cantidad.
        jLabelCantidadDetalle.setText("Cantidad total: " + producto.getCantidad());
        // Texto por defecto si no tiene descripción.
        String descripcion = producto.getDescripcion() == null || producto.getDescripcion().trim().isEmpty()
                ? "Sin descripción" : producto.getDescripcion();
        // HTML con ancho fijo para que la descripción salte de línea; se escapa para no romper el HTML.
        jLabelDescripcionDetalle.setText("<html><body style='width:250px'>" + escaparHtml(descripcion)
                + "</body></html>");
        // Existencias del producto en cada almacén (tabla inventarioporalmacen).
        for (InventarioPorAlmacen inventario : BusquedaProductos_control.existenciasPorAlmacen(
                producto.getIdProducto())) {
            // Almacén, disponible y reservada.
            modelo.addRow(new Object[]{
                inventario.getNombreAlmacen(),
                inventario.getCantidadDisponible(),
                inventario.getCantidadReservada()
            });
        }
        // Solo los productos activos se pueden agregar a una compra.
        jButtonAgregarACompra.setEnabled("activo".equals(producto.getEstado()));
    }

    // Envía el producto elegido a la compra de origen o abre una compra nueva.
    private void agregarACompra() {
        // Sin producto elegido.
        if (productoSeleccionado == null) {
            // Pide seleccionar un producto.
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un producto", "Producto no seleccionado",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            // No envía nada.
            return;
        }
        // Producto descontinuado.
        if (!"activo".equals(productoSeleccionado.getEstado())) {
            // Avisa que no se puede comprar.
            javax.swing.JOptionPane.showMessageDialog(this, "El producto está descontinuado y no se puede comprar",
                    "Producto no disponible", javax.swing.JOptionPane.WARNING_MESSAGE);
            // No envía nada.
            return;
        }
        // Compra que abrió la búsqueda.
        Compras_Form compras = compraDestino;
        // Si no hay compra o ya se cerró.
        if (compras == null || !compras.isDisplayable()) {
            // Abre una ventana de compras nueva.
            compras = new Compras_Form();
            // Cerrar esa ventana con la X solo la cierra a ella.
            compras.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            // Centra la nueva ventana en la pantalla.
            compras.setLocationRelativeTo(null);
            // Muestra la nueva ventana.
            compras.setVisible(true);
        }
        // Selecciona el producto en la compra con cantidad 1.
        compras.agregarProductoDesdeBusqueda(productoSeleccionado);
        // Pone la compra al frente.
        compras.toFront();
        // Cierra la búsqueda.
        this.dispose();
    }

    // Precio con separador de miles y 2 decimales; 0.00 si no hay precio.
    private String formatearPrecio(java.math.BigDecimal valor) {
        // Formatea el valor.
        return valor == null ? "0.00" : String.format("%,.2f", valor);
    }

    // Escapa &, < y > para mostrar el texto dentro del HTML de la etiqueta.
    private String escaparHtml(String texto) {
        // Reemplaza los caracteres especiales.
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
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
            java.util.logging.Logger.getLogger(Busqueda_Productos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Busqueda_Productos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Busqueda_Productos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Busqueda_Productos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Busqueda_Productos_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregarACompra;
    private javax.swing.JButton jButtonBuscar;
    private javax.swing.JButton jButtonLimpiarBusqueda;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<Object> jComboBoxCategoria;
    private javax.swing.JComboBox<String> jComboBoxEstado;
    private javax.swing.JLabel jLabelBuscar;
    private javax.swing.JLabel jLabelCantidadDetalle;
    private javax.swing.JLabel jLabelCategoria;
    private javax.swing.JLabel jLabelCategoriaDetalle;
    private javax.swing.JLabel jLabelDescripcionDetalle;
    private javax.swing.JLabel jLabelEstado;
    private javax.swing.JLabel jLabelEstadoDetalle;
    private javax.swing.JLabel jLabelExistenciasTitulo;
    private javax.swing.JLabel jLabelImagenDetalle;
    private javax.swing.JLabel jLabelNombreDetalle;
    private javax.swing.JLabel jLabelPrecioCompraDetalle;
    private javax.swing.JLabel jLabelPrecioVentaDetalle;
    private javax.swing.JLabel jLabelResultados;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelContenido;
    private javax.swing.JPanel jPanelDetalleProducto;
    private javax.swing.JPanel jPanelFiltrosBusqueda;
    private javax.swing.JPanel jPanelResultados;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPaneExistencias;
    private javax.swing.JScrollPane jScrollPaneResultados;
    private javax.swing.JTable jTableExistencias;
    private javax.swing.JTextField jTextFieldBuscar;
    // End of variables declaration//GEN-END:variables
}
