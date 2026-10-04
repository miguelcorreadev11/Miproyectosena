// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Inventario_control valida y ejecuta las operaciones de inventarioporalmacen.
import controlador.Inventario_control;
// Producto_control valida y ejecuta las operaciones de la tabla productos.
import controlador.Producto_control;
// BigDecimal: precios de compra y venta.
import java.math.BigDecimal;
// ArrayList: registros cargados en tablas y combos.
import java.util.ArrayList;
// DefaultTableModel: modelo de filas de las dos tablas.
import javax.swing.table.DefaultTableModel;
// Almacen: elementos del combo de almacén del inventario.
import modelo.Almacen;
// Categoria: elementos del combo de categoría del producto.
import modelo.Categoria;
// InventarioPorAlmacen: modelo de la tabla inventarioporalmacen.
import modelo.InventarioPorAlmacen;
// Producto: modelo de la tabla productos.
import modelo.Producto;

// Ventana con dos pestañas: productos (con imagen) e inventario por almacén; cada una
// permite guardar, actualizar, eliminar y limpiar usando su controlador.
public class Productos_Inventario_Form extends javax.swing.JFrame {
    // Productos mostrados, en el mismo orden que las filas de la tabla.
    private ArrayList<Producto> listaProductos;
    // Id del producto elegido; null si no hay selección.
    private Integer idProductoSeleccionado;

    // Nombre del archivo de imagen del producto que se guardará en productos.imagen.
    private String imagenProductoActual;

    // Registros de inventario mostrados, en el mismo orden que las filas.
    private ArrayList<InventarioPorAlmacen> listaInventario;
    // Id del registro de inventario elegido; null si no hay selección.
    private Integer idInventarioSeleccionado;

    // Constructor: arma las tablas, llena combos y carga productos e inventario de MySQL.
    public Productos_Inventario_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Productos e Inventario");

        // Pestañas con el fondo del tema.
        jTabbedPaneProductosInventario.setBackground(TemaColores.FONDO_PRINCIPAL);
        // Texto de las pestañas en blanco.
        jTabbedPaneProductosInventario.setForeground(TemaColores.TEXTO);
        // Fuente base del tema.
        jTabbedPaneProductosInventario.setFont(TemaColores.FUENTE_BASE);

        // Modelo de la tabla de productos con sus columnas y sin filas iniciales.
        DefaultTableModel modeloProductos = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Categoría", "P. Compra", "P. Venta", "Cantidad", "Estado"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo a la tabla.
        jTableProductos.setModel(modeloProductos);
        // Opción 'activo' del ENUM estado.
        jComboBoxEstadoProducto.addItem("activo");
        // Opción 'descontinuado' del ENUM estado.
        jComboBoxEstadoProducto.addItem("descontinuado");
        // Llena el combo de categorías.
        cargarCategoriasProducto();
        // Llena la tabla de productos.
        cargarProductos();
        // Tamaño visible de la tabla.
        jTableProductos.setPreferredScrollableViewportSize(new java.awt.Dimension(760, 200));

        // Modelo de la tabla de inventario con sus columnas y sin filas iniciales.
        DefaultTableModel modeloInventario = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Producto", "Almacén", "Cant. Disponible", "Cant. Reservada",
                    "Última actualización"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo a la tabla.
        jTableInventario.setModel(modeloInventario);
        // Llena el combo de productos del inventario.
        cargarProductosInventario();
        // Llena el combo de almacenes del inventario.
        cargarAlmacenesInventario();
        // Llena la tabla de inventario.
        cargarInventario();
        // Tamaño visible de la tabla.
        jTableInventario.setPreferredScrollableViewportSize(new java.awt.Dimension(700, 200));

        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableProductos, jScrollPaneProductos);
        // Borde de la vista previa de la imagen.
        jLabelImagenProducto.setBorder(javax.swing.BorderFactory.createLineBorder(TemaColores.BORDE, 1));
        // Muestra sin_imagen.png mientras no haya producto elegido.
        mostrarImagenProducto();
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableInventario, jScrollPaneInventario);

        // Carga el registro en el formulario también cuando la fila cambia con el teclado o
        // arrastrando el mouse (mouseClicked no se dispara en esos casos).
        jTableProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTableProductos.getSelectedRow() >= 0) {
                cargarProductoSeleccionado();
            }
        });
        jTableInventario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTableInventario.getSelectedRow() >= 0) {
                cargarInventarioSeleccionado();
            }
        });
        // Cerrar con la X equivale a "Volver al Menú".
        RecursosApp.cerrarConBoton(this, jButtonVolver);

        // Ajusta la ventana al tamaño preferido de sus componentes.
        this.pack();
        // Tamaño inicial 1180x740 (antes el de pack(): 815x605), limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 1180, 740, 1000, 640);
    }

    // initComponents() lo genera el GUI Builder de NetBeans a partir del .form;
    // no se edita a mano porque NetBeans lo regenera al guardar el diseño.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jTabbedPaneProductosInventario = new javax.swing.JTabbedPane();
        jPanelProductos = new javax.swing.JPanel();
        jPanelFormularioProducto = new javax.swing.JPanel();
        jLabelPrecioCompraProducto = new javax.swing.JLabel();
        jTextFieldPrecioCompraProducto = new javax.swing.JTextField();
        jLabelNombreProducto = new javax.swing.JLabel();
        jTextFieldNombreProducto = new javax.swing.JTextField();
        jLabelPrecioVentaProducto = new javax.swing.JLabel();
        jTextFieldPrecioVentaProducto = new javax.swing.JTextField();
        jLabelDescripcionProducto = new javax.swing.JLabel();
        jTextFieldDescripcionProducto = new javax.swing.JTextField();
        jLabelCantidadProducto = new javax.swing.JLabel();
        jTextFieldCantidadProducto = new javax.swing.JTextField();
        jLabelCategoriaProducto = new javax.swing.JLabel();
        jComboBoxCategoriaProducto = new javax.swing.JComboBox<>();
        jLabelEstadoProducto = new javax.swing.JLabel();
        jComboBoxEstadoProducto = new javax.swing.JComboBox<>();
        jLabelImagenProducto = new javax.swing.JLabel();
        jButtonSeleccionarImagenProducto = new javax.swing.JButton();
        jButtonQuitarImagenProducto = new javax.swing.JButton();
        jPanelBotonesProducto = new javax.swing.JPanel();
        jButtonGuardarProducto = new javax.swing.JButton();
        jButtonActualizarProducto = new javax.swing.JButton();
        jButtonEliminarProducto = new javax.swing.JButton();
        jButtonLimpiarProducto = new javax.swing.JButton();
        jScrollPaneProductos = new javax.swing.JScrollPane();
        jTableProductos = new javax.swing.JTable();
        jPanelInventario = new javax.swing.JPanel();
        jPanelFormularioInventario = new javax.swing.JPanel();
        jLabelProductoInventario = new javax.swing.JLabel();
        jComboBoxProductoInventario = new javax.swing.JComboBox<>();
        jLabelAlmacenInventario = new javax.swing.JLabel();
        jComboBoxAlmacenInventario = new javax.swing.JComboBox<>();
        jLabelCantidadDisponibleInventario = new javax.swing.JLabel();
        jTextFieldCantidadDisponibleInventario = new javax.swing.JTextField();
        jLabelCantidadReservadaInventario = new javax.swing.JLabel();
        jTextFieldCantidadReservadaInventario = new javax.swing.JTextField();
        jPanelBotonesInventario = new javax.swing.JPanel();
        jButtonRegistrarInventario = new javax.swing.JButton();
        jButtonActualizarInventario = new javax.swing.JButton();
        jButtonEliminarInventario = new javax.swing.JButton();
        jButtonLimpiarInventario = new javax.swing.JButton();
        jScrollPaneInventario = new javax.swing.JScrollPane();
        jTableInventario = new javax.swing.JTable();
        jPanelBotones = new javax.swing.JPanel();
        jButtonVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));

        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Productos e Inventario");

        javax.swing.GroupLayout jPanelTituloLayout = new javax.swing.GroupLayout(jPanelTitulo);
        jPanelTitulo.setLayout(jPanelTituloLayout);
        jPanelTituloLayout.setHorizontalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabelTitulo)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanelTituloLayout.setVerticalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabelTitulo)
                .addGap(14, 14, 14))
        );

        jPanelProductos.setBackground(new java.awt.Color(11, 18, 32));

        jLabelPrecioCompraProducto.setText("Precio compra:");

        jLabelNombreProducto.setText("Nombre:");

        jLabelPrecioVentaProducto.setText("Precio venta:");

        jLabelDescripcionProducto.setText("Descripción:");

        jLabelCantidadProducto.setText("Cantidad:");

        jLabelCategoriaProducto.setText("Categoría:");

        jLabelEstadoProducto.setText("Estado:");

        jLabelImagenProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jButtonSeleccionarImagenProducto.setBackground(new java.awt.Color(15, 23, 42));
        jButtonSeleccionarImagenProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSeleccionarImagenProducto.setText("Elegir imagen");
        jButtonSeleccionarImagenProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSeleccionarImagenProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSeleccionarImagenProductoActionPerformed(evt);
            }
        });

        jButtonQuitarImagenProducto.setBackground(new java.awt.Color(15, 23, 42));
        jButtonQuitarImagenProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonQuitarImagenProducto.setText("Quitar imagen");
        jButtonQuitarImagenProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonQuitarImagenProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitarImagenProductoActionPerformed(evt);
            }
        });

        jPanelFormularioProducto.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelFormularioProductoLayout = new javax.swing.GroupLayout(jPanelFormularioProducto);
        jPanelFormularioProducto.setLayout(jPanelFormularioProductoLayout);
        jPanelFormularioProductoLayout.setHorizontalGroup(
            jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioProductoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNombreProducto)
                    .addComponent(jLabelDescripcionProducto)
                    .addComponent(jLabelCategoriaProducto))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCategoriaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelPrecioCompraProducto)
                    .addComponent(jLabelPrecioVentaProducto)
                    .addComponent(jLabelCantidadProducto)
                    .addComponent(jLabelEstadoProducto))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldPrecioCompraProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPrecioVentaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxEstadoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelImagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSeleccionarImagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonQuitarImagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanelFormularioProductoLayout.setVerticalGroup(
            jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioProductoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombreProducto)
                    .addComponent(jTextFieldNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrecioCompraProducto)
                    .addComponent(jTextFieldPrecioCompraProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDescripcionProducto)
                    .addComponent(jTextFieldDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrecioVentaProducto)
                    .addComponent(jTextFieldPrecioVentaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCategoriaProducto)
                    .addComponent(jComboBoxCategoriaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCantidadProducto)
                    .addComponent(jTextFieldCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEstadoProducto)
                    .addComponent(jComboBoxEstadoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
            .addGroup(jPanelFormularioProductoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabelImagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jButtonSeleccionarImagenProducto)
                .addGap(4, 4, 4)
                .addComponent(jButtonQuitarImagenProducto)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jButtonGuardarProducto.setBackground(new java.awt.Color(237, 149, 62));
        jButtonGuardarProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarProducto.setText("Guardar");
        jButtonGuardarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarProductoActionPerformed(evt);
            }
        });

        jButtonActualizarProducto.setBackground(new java.awt.Color(237, 149, 62));
        jButtonActualizarProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizarProducto.setText("Actualizar");
        jButtonActualizarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarProductoActionPerformed(evt);
            }
        });

        jButtonEliminarProducto.setBackground(new java.awt.Color(153, 0, 0));
        jButtonEliminarProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarProducto.setText("Eliminar");
        jButtonEliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarProductoActionPerformed(evt);
            }
        });

        jButtonLimpiarProducto.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarProducto.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarProducto.setText("Limpiar");
        jButtonLimpiarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarProductoActionPerformed(evt);
            }
        });

        jPanelBotonesProducto.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelBotonesProductoLayout = new javax.swing.GroupLayout(jPanelBotonesProducto);
        jPanelBotonesProducto.setLayout(jPanelBotonesProductoLayout);
        jPanelBotonesProductoLayout.setHorizontalGroup(
            jPanelBotonesProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonGuardarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonActualizarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminarProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpiarProducto)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBotonesProductoLayout.setVerticalGroup(
            jPanelBotonesProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotonesProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardarProducto)
                    .addComponent(jButtonActualizarProducto)
                    .addComponent(jButtonEliminarProducto)
                    .addComponent(jButtonLimpiarProducto))
                .addContainerGap())
        );

        jTableProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableProductosMouseClicked(evt);
            }
        });
        jScrollPaneProductos.setViewportView(jTableProductos);

        javax.swing.GroupLayout jPanelProductosLayout = new javax.swing.GroupLayout(jPanelProductos);
        jPanelProductos.setLayout(jPanelProductosLayout);
        jPanelProductosLayout.setHorizontalGroup(
            jPanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFormularioProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBotonesProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelProductosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelProductosLayout.setVerticalGroup(
            jPanelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProductosLayout.createSequentialGroup()
                .addComponent(jPanelFormularioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelBotonesProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneProductos, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneProductosInventario.addTab("Productos", jPanelProductos);

        jPanelInventario.setBackground(new java.awt.Color(11, 18, 32));

        jLabelProductoInventario.setText("Producto:");

        jLabelAlmacenInventario.setText("Almacén:");

        jLabelCantidadDisponibleInventario.setText("Cantidad disponible:");

        jLabelCantidadReservadaInventario.setText("Cantidad reservada:");

        jPanelFormularioInventario.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelFormularioInventarioLayout = new javax.swing.GroupLayout(jPanelFormularioInventario);
        jPanelFormularioInventario.setLayout(jPanelFormularioInventarioLayout);
        jPanelFormularioInventarioLayout.setHorizontalGroup(
            jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioInventarioLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelProductoInventario)
                    .addComponent(jLabelAlmacenInventario)
                    .addComponent(jLabelCantidadDisponibleInventario)
                    .addComponent(jLabelCantidadReservadaInventario))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxProductoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxAlmacenInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCantidadDisponibleInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCantidadReservadaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanelFormularioInventarioLayout.setVerticalGroup(
            jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioInventarioLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelProductoInventario)
                    .addComponent(jComboBoxProductoInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAlmacenInventario)
                    .addComponent(jComboBoxAlmacenInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCantidadDisponibleInventario)
                    .addComponent(jTextFieldCantidadDisponibleInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCantidadReservadaInventario)
                    .addComponent(jTextFieldCantidadReservadaInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jButtonRegistrarInventario.setBackground(new java.awt.Color(237, 149, 62));
        jButtonRegistrarInventario.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRegistrarInventario.setText("Registrar");
        jButtonRegistrarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonRegistrarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistrarInventarioActionPerformed(evt);
            }
        });

        jButtonActualizarInventario.setBackground(new java.awt.Color(237, 149, 62));
        jButtonActualizarInventario.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizarInventario.setText("Actualizar");
        jButtonActualizarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarInventarioActionPerformed(evt);
            }
        });

        jButtonEliminarInventario.setBackground(new java.awt.Color(153, 0, 0));
        jButtonEliminarInventario.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarInventario.setText("Eliminar");
        jButtonEliminarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarInventarioActionPerformed(evt);
            }
        });

        jButtonLimpiarInventario.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarInventario.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarInventario.setText("Limpiar");
        jButtonLimpiarInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarInventarioActionPerformed(evt);
            }
        });

        jPanelBotonesInventario.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelBotonesInventarioLayout = new javax.swing.GroupLayout(jPanelBotonesInventario);
        jPanelBotonesInventario.setLayout(jPanelBotonesInventarioLayout);
        jPanelBotonesInventarioLayout.setHorizontalGroup(
            jPanelBotonesInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonRegistrarInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonActualizarInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminarInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpiarInventario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBotonesInventarioLayout.setVerticalGroup(
            jPanelBotonesInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotonesInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRegistrarInventario)
                    .addComponent(jButtonActualizarInventario)
                    .addComponent(jButtonEliminarInventario)
                    .addComponent(jButtonLimpiarInventario))
                .addContainerGap())
        );

        jTableInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableInventarioMouseClicked(evt);
            }
        });
        jScrollPaneInventario.setViewportView(jTableInventario);

        javax.swing.GroupLayout jPanelInventarioLayout = new javax.swing.GroupLayout(jPanelInventario);
        jPanelInventario.setLayout(jPanelInventarioLayout);
        jPanelInventarioLayout.setHorizontalGroup(
            jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFormularioInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBotonesInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelInventarioLayout.setVerticalGroup(
            jPanelInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInventarioLayout.createSequentialGroup()
                .addComponent(jPanelFormularioInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelBotonesInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneInventario, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneProductosInventario.addTab("Inventario", jPanelInventario);

        jButtonVolver.setBackground(new java.awt.Color(15, 23, 42));
        jButtonVolver.setForeground(new java.awt.Color(255, 255, 255));
        jButtonVolver.setText("Volver al Menú");
        jButtonVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVolverActionPerformed(evt);
            }
        });

        jPanelBotones.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelBotonesLayout = new javax.swing.GroupLayout(jPanelBotones);
        jPanelBotones.setLayout(jPanelBotonesLayout);
        jPanelBotonesLayout.setHorizontalGroup(
            jPanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonVolver)
                .addContainerGap())
        );
        jPanelBotonesLayout.setVerticalGroup(
            jPanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonVolver)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneProductosInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jTabbedPaneProductosInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

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

    // Botón Guardar: registra un producto nuevo.
    private void jButtonGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarProductoActionPerformed
        // Valida los datos y ejecuta el INSERT.
        guardarProducto();
    }//GEN-LAST:event_jButtonGuardarProductoActionPerformed

    // Botón Actualizar: guarda los cambios del producto seleccionado.
    private void jButtonActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarProductoActionPerformed
        // Valida los datos y ejecuta el UPDATE.
        actualizarProducto();
    }//GEN-LAST:event_jButtonActualizarProductoActionPerformed

    // Botón Eliminar: borra el producto seleccionado.
    private void jButtonEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarProductoActionPerformed
        // Pide confirmación y ejecuta el DELETE.
        eliminarProducto();
    }//GEN-LAST:event_jButtonEliminarProductoActionPerformed

    // Botón Limpiar: vacía el formulario del producto.
    private void jButtonLimpiarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarProductoActionPerformed
        // Borra campos y selección.
        limpiarCamposProducto();
    }//GEN-LAST:event_jButtonLimpiarProductoActionPerformed

    // Clic en la tabla: carga el producto de esa fila en el formulario.
    private void jTableProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProductosMouseClicked
        // Copia los datos de la fila a los campos.
        cargarProductoSeleccionado();
    }//GEN-LAST:event_jTableProductosMouseClicked

    // Botón Seleccionar imagen: elige un archivo y lo copia a la carpeta de imágenes.
    private void jButtonSeleccionarImagenProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSeleccionarImagenProductoActionPerformed
        // Abre el selector y copia la imagen.
        seleccionarImagenProducto();
    }//GEN-LAST:event_jButtonSeleccionarImagenProductoActionPerformed

    // Botón Quitar imagen: el producto quedará sin imagen al guardar.
    private void jButtonQuitarImagenProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQuitarImagenProductoActionPerformed
        // Se guardará NULL en productos.imagen.
        imagenProductoActual = null;
        // Muestra la imagen de respaldo.
        mostrarImagenProducto();
    }//GEN-LAST:event_jButtonQuitarImagenProductoActionPerformed

    // Elige una imagen, la copia al proyecto y guarda su nombre para el producto.
    private void seleccionarImagenProducto() {
        // Diálogo de selección de archivo.
        java.io.File archivo = ImagenProducto.seleccionarArchivo(this);
        // El usuario canceló.
        if (archivo == null) {
            // No cambia la imagen.
            return;
        }
        // La copia puede fallar si el archivo no es una imagen.
        try {
            // Copia la imagen y guarda el nombre asignado.
            imagenProductoActual = ImagenProducto.copiarImagen(archivo);
            // Muestra la vista previa.
            mostrarImagenProducto();
        // Archivo inválido o error de copia.
        } catch (java.io.IOException ex) {
            // Informa el motivo.
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen: " + ex.getMessage(),
                    "Imagen inválida", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    // Muestra la imagen actual en la vista previa (170x112 px).
    private void mostrarImagenProducto() {
        // Usa sin_imagen.png si no hay imagen o no se encuentra.
        jLabelImagenProducto.setIcon(ImagenProducto.obtenerIcono(imagenProductoActual, 170, 112));
    }

    // Llena el combo de categorías con la tabla categorias.
    private void cargarCategoriasProducto() {
        // Vacía el combo.
        jComboBoxCategoriaProducto.removeAllItems();
        // SELECT de las categorías.
        ArrayList<Categoria> categorias = Categoria.getCategorias();
        // Recorre las categorías.
        for (Categoria categoria : categorias) {
            // Agrega cada categoría.
            jComboBoxCategoriaProducto.addItem(categoria);
        }
    }

    // Consulta los productos en MySQL y vuelve a llenar la tabla.
    private void cargarProductos() {
        // SELECT de los productos de la tabla productos.
        listaProductos = Producto_control.listarProductos();

        // Modelo actual de la tabla.
        DefaultTableModel modelo = (DefaultTableModel) jTableProductos.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);

        // Agrega una fila por cada producto.
        for (Producto producto : listaProductos) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getNombreCategoria(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta(),
                producto.getCantidad(),
                producto.getEstado()
            });
        }
    }

    // Copia al formulario el producto de la fila seleccionada.
    private void cargarProductoSeleccionado() {
        // Fila seleccionada; -1 si no hay.
        int fila = jTableProductos.getSelectedRow();
        // Sin selección válida.
        if (fila < 0 || listaProductos == null || fila >= listaProductos.size()) {
            // No hace nada.
            return;
        }

        // Registro de esa fila.
        Producto producto = listaProductos.get(fila);
        // Guarda su id para actualizar o eliminar.
        idProductoSeleccionado = producto.getIdProducto();

        // Muestra el nombre.
        jTextFieldNombreProducto.setText(producto.getNombre());
        // Muestra la descripción.
        jTextFieldDescripcionProducto.setText(producto.getDescripcion());
        // Muestra el precio de compra.
        jTextFieldPrecioCompraProducto.setText(String.valueOf(producto.getPrecioCompra()));
        // Muestra el precio de venta.
        jTextFieldPrecioVentaProducto.setText(String.valueOf(producto.getPrecioVenta()));
        // Muestra la cantidad total.
        jTextFieldCantidadProducto.setText(String.valueOf(producto.getCantidad()));

        // Selecciona la categoría del producto en el combo.
        seleccionarCategoriaEnCombo(producto.getIdCategoria());
        // Selecciona su estado en el combo.
        jComboBoxEstadoProducto.setSelectedItem(producto.getEstado());
        // Guarda el nombre de su imagen.
        imagenProductoActual = producto.getImagen();
        // Muestra la imagen del producto.
        mostrarImagenProducto();
    }

    // Busca la categoría por id en el combo y la selecciona.
    private void seleccionarCategoriaEnCombo(Integer idCategoria) {
        // Recorre las categorías del combo.
        for (int i = 0; i < jComboBoxCategoriaProducto.getItemCount(); i++) {
            // Categoría en esa posición.
            Categoria categoria = jComboBoxCategoriaProducto.getItemAt(i);
            // Coincide el id.
            if (categoria.getIdCategoria().equals(idCategoria)) {
                // La selecciona.
                jComboBoxCategoriaProducto.setSelectedIndex(i);
                // Termina la búsqueda.
                return;
            }
        }
    }

    // Registra un producto con los datos del formulario.
    private void guardarProducto() {
        // Captura precios o cantidades no numéricos.
        try {
            // Categoría elegida.
            Categoria categoria = (Categoria) jComboBoxCategoriaProducto.getSelectedItem();
            // Id de la categoría o null si no hay ninguna.
            Integer idCategoria = categoria == null ? null : categoria.getIdCategoria();

            // Valida los datos y ejecuta el INSERT en productos; los errores los informa el controlador.
            boolean creado = Producto_control.crearProducto(
                    jTextFieldNombreProducto.getText(),
                    jTextFieldDescripcionProducto.getText(),
                    idCategoria,
                    // Precio de compra como BigDecimal.
                    leerBigDecimal(jTextFieldPrecioCompraProducto.getText()),
                    // Precio de venta como BigDecimal.
                    leerBigDecimal(jTextFieldPrecioVentaProducto.getText()),
                    // Cantidad inicial; 0 si está vacía.
                    leerEntero(jTextFieldCantidadProducto.getText(), 0),
                    (String) jComboBoxEstadoProducto.getSelectedItem(),
                    // Nombre del archivo de imagen o null.
                    imagenProductoActual);

            // Si se guardó.
            if (creado) {
                // Vacía el formulario.
                limpiarCamposProducto();
                // Recarga la tabla con los datos actuales.
                cargarProductos();
                // Actualiza el combo de productos de la pestaña de inventario.
                cargarProductosInventario();
            }
        // Un número escrito con letras o formato inválido.
        } catch (NumberFormatException ex) {
            // Avisa que el dato numérico no es válido.
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique que los precios y cantidades sean numéricos",
                    "Dato inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    // Guarda los cambios del producto seleccionado.
    private void actualizarProducto() {
        // Captura precios o cantidades no numéricos.
        try {
            // Categoría elegida.
            Categoria categoria = (Categoria) jComboBoxCategoriaProducto.getSelectedItem();
            // Id de la categoría o null si no hay ninguna.
            Integer idCategoria = categoria == null ? null : categoria.getIdCategoria();

            // Valida y ejecuta el UPDATE en productos (incluye la cantidad escrita); sin selección,
            // el controlador lo avisa.
            boolean actualizado = Producto_control.actualizarProducto(
                    idProductoSeleccionado,
                    jTextFieldNombreProducto.getText(),
                    jTextFieldDescripcionProducto.getText(),
                    idCategoria,
                    leerBigDecimal(jTextFieldPrecioCompraProducto.getText()),
                    leerBigDecimal(jTextFieldPrecioVentaProducto.getText()),
                    leerEntero(jTextFieldCantidadProducto.getText(), 0),
                    (String) jComboBoxEstadoProducto.getSelectedItem(),
                    imagenProductoActual);

            // Si se actualizó.
            if (actualizado) {
                // Vacía el formulario.
                limpiarCamposProducto();
                // Recarga la tabla con los datos actuales.
                cargarProductos();
                // Actualiza el combo de productos de la pestaña de inventario.
                cargarProductosInventario();
            }
        // Un número escrito con letras o formato inválido.
        } catch (NumberFormatException ex) {
            // Avisa que el dato numérico no es válido.
            javax.swing.JOptionPane.showMessageDialog(this, "Verifique que los precios y cantidades sean numéricos",
                    "Dato inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    // Elimina el producto seleccionado.
    private void eliminarProducto() {
        // Pide confirmación y ejecuta el DELETE en productos; sin selección, lo avisa.
        boolean eliminado = Producto_control.eliminarProducto(idProductoSeleccionado);
        // Si se eliminó.
        if (eliminado) {
            // Vacía el formulario.
            limpiarCamposProducto();
            // Recarga la tabla con los datos actuales.
            cargarProductos();
            // Actualiza el combo de productos de la pestaña de inventario.
            cargarProductosInventario();
        }
    }

    // Deja el formulario vacío y sin registro seleccionado.
    private void limpiarCamposProducto() {
        // Sin selección.
        idProductoSeleccionado = null;
        // Vacía el nombre.
        jTextFieldNombreProducto.setText("");
        // Vacía la descripción.
        jTextFieldDescripcionProducto.setText("");
        // Vacía el precio de compra.
        jTextFieldPrecioCompraProducto.setText("");
        // Vacía el precio de venta.
        jTextFieldPrecioVentaProducto.setText("");
        // Vacía la cantidad.
        jTextFieldCantidadProducto.setText("");
        // Solo si hay categorías.
        if (jComboBoxCategoriaProducto.getItemCount() > 0) {
            // Selecciona la primera.
            jComboBoxCategoriaProducto.setSelectedIndex(0);
        }
        // Vuelve al primer estado (activo).
        jComboBoxEstadoProducto.setSelectedIndex(0);
        // Quita la selección de la tabla.
        jTableProductos.clearSelection();
        // Sin imagen.
        imagenProductoActual = null;
        // Muestra la imagen de respaldo.
        mostrarImagenProducto();
    }

    // Convierte el texto a BigDecimal aceptando coma o punto decimal.
    private BigDecimal leerBigDecimal(String texto) {
        // Campo vacío.
        if (texto == null || texto.trim().isEmpty()) {
            // Los precios son obligatorios.
            throw new NumberFormatException("valor vacío");
        }
        // Reemplaza la coma por punto y convierte.
        return new BigDecimal(texto.trim().replace(",", "."));
    }

    // Convierte el texto a entero; usa el valor por defecto si está vacío.
    private Integer leerEntero(String texto, int valorPorDefecto) {
        // Campo vacío.
        if (texto == null || texto.trim().isEmpty()) {
            // Valor por defecto.
            return valorPorDefecto;
        }
        // Lanza NumberFormatException si no es un entero.
        return Integer.parseInt(texto.trim());
    }

    // Botón Registrar: crea un registro de inventario nuevo.
    private void jButtonRegistrarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistrarInventarioActionPerformed
        // Valida los datos y ejecuta el INSERT.
        registrarInventario();
    }//GEN-LAST:event_jButtonRegistrarInventarioActionPerformed

    // Botón Actualizar: guarda los cambios del registro de inventario seleccionado.
    private void jButtonActualizarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarInventarioActionPerformed
        // Valida los datos y ejecuta el UPDATE.
        actualizarInventario();
    }//GEN-LAST:event_jButtonActualizarInventarioActionPerformed

    // Botón Eliminar: borra el registro de inventario seleccionado.
    private void jButtonEliminarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarInventarioActionPerformed
        // Pide confirmación y ejecuta el DELETE.
        eliminarInventario();
    }//GEN-LAST:event_jButtonEliminarInventarioActionPerformed

    // Botón Limpiar: vacía el formulario de inventario.
    private void jButtonLimpiarInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarInventarioActionPerformed
        // Borra campos y selección.
        limpiarCamposInventario();
    }//GEN-LAST:event_jButtonLimpiarInventarioActionPerformed

    // Clic en la tabla: carga el registro de inventario de esa fila en el formulario.
    private void jTableInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableInventarioMouseClicked
        // Copia los datos de la fila a los campos.
        cargarInventarioSeleccionado();
    }//GEN-LAST:event_jTableInventarioMouseClicked

    // Llena el combo de productos de la pestaña de inventario.
    private void cargarProductosInventario() {
        // Producto elegido en el formulario de inventario: se conserva al recargar la lista, porque
        // guardar o editar un producto en la otra pestaña no debe cambiar lo que el usuario ya eligió.
        Producto elegido = (Producto) jComboBoxProductoInventario.getSelectedItem();
        // Vacía el combo.
        jComboBoxProductoInventario.removeAllItems();
        // SELECT de los productos.
        ArrayList<Producto> productos = Producto.getProductos();
        // Recorre los productos.
        for (Producto producto : productos) {
            // Agrega cada producto.
            jComboBoxProductoInventario.addItem(producto);
        }
        // Vuelve a seleccionar el producto que estaba elegido (si todavía existe).
        if (elegido != null) {
            seleccionarProductoEnCombo(elegido.getIdProducto());
        }
    }

    // Llena el combo de almacenes de la pestaña de inventario.
    private void cargarAlmacenesInventario() {
        // Vacía el combo.
        jComboBoxAlmacenInventario.removeAllItems();
        // SELECT de los almacenes.
        ArrayList<Almacen> almacenes = Almacen.getAlmacenes();
        // Recorre los almacenes.
        for (Almacen almacen : almacenes) {
            // Agrega cada almacén.
            jComboBoxAlmacenInventario.addItem(almacen);
        }
    }

    // Consulta los registros de inventario en MySQL y vuelve a llenar la tabla.
    private void cargarInventario() {
        // SELECT de los registros de inventario de la tabla inventarioporalmacen.
        listaInventario = Inventario_control.listarInventario();

        // Modelo actual de la tabla.
        DefaultTableModel modelo = (DefaultTableModel) jTableInventario.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);

        // Agrega una fila por cada registro de inventario.
        for (InventarioPorAlmacen inventario : listaInventario) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                inventario.getIdInventario(),
                inventario.getNombreProducto(),
                inventario.getNombreAlmacen(),
                inventario.getCantidadDisponible(),
                inventario.getCantidadReservada(),
                inventario.getFechaDeActualizacion()
            });
        }
    }

    // Copia al formulario el registro de inventario de la fila seleccionada.
    private void cargarInventarioSeleccionado() {
        // Fila seleccionada; -1 si no hay.
        int fila = jTableInventario.getSelectedRow();
        // Sin selección válida.
        if (fila < 0 || listaInventario == null || fila >= listaInventario.size()) {
            // No hace nada.
            return;
        }

        // Registro de esa fila.
        InventarioPorAlmacen inventario = listaInventario.get(fila);
        // Guarda su id para actualizar o eliminar.
        idInventarioSeleccionado = inventario.getIdInventario();

        // Selecciona el producto del registro.
        seleccionarProductoEnCombo(inventario.getIdProducto());
        // Selecciona el almacén del registro.
        seleccionarAlmacenEnCombo(inventario.getIdAlmacen());
        // Muestra la cantidad disponible.
        jTextFieldCantidadDisponibleInventario.setText(String.valueOf(inventario.getCantidadDisponible()));
        // Muestra la cantidad reservada.
        jTextFieldCantidadReservadaInventario.setText(String.valueOf(inventario.getCantidadReservada()));
    }

    // Busca el producto por id en el combo y lo selecciona.
    private void seleccionarProductoEnCombo(Integer idProducto) {
        // Recorre los productos del combo.
        for (int i = 0; i < jComboBoxProductoInventario.getItemCount(); i++) {
            // Producto en esa posición.
            Producto producto = jComboBoxProductoInventario.getItemAt(i);
            // Coincide el id.
            if (producto.getIdProducto().equals(idProducto)) {
                // Lo selecciona.
                jComboBoxProductoInventario.setSelectedIndex(i);
                // Termina la búsqueda.
                return;
            }
        }
    }

    // Busca el almacén por id en el combo y lo selecciona.
    private void seleccionarAlmacenEnCombo(Integer idAlmacen) {
        // Recorre los almacenes del combo.
        for (int i = 0; i < jComboBoxAlmacenInventario.getItemCount(); i++) {
            // Almacén en esa posición.
            Almacen almacen = jComboBoxAlmacenInventario.getItemAt(i);
            // Coincide el id.
            if (almacen.getIdAlmacen().equals(idAlmacen)) {
                // Lo selecciona.
                jComboBoxAlmacenInventario.setSelectedIndex(i);
                // Termina la búsqueda.
                return;
            }
        }
    }

    // Registra un registro de inventario con los datos del formulario.
    private void registrarInventario() {
        // Captura cantidades no numéricas.
        try {
            // Producto elegido.
            Producto producto = (Producto) jComboBoxProductoInventario.getSelectedItem();
            // Almacén elegido.
            Almacen almacen = (Almacen) jComboBoxAlmacenInventario.getSelectedItem();

            // INSERT directo en inventarioporalmacen (no genera movimiento ni historial; para eso
            // está Movimientos_Form).
            boolean registrado = Inventario_control.registrarInventario(
                    // Id del producto o null.
                    producto == null ? null : producto.getIdProducto(),
                    // Id del almacén o null.
                    almacen == null ? null : almacen.getIdAlmacen(),
                    // Cantidad disponible (obligatoria).
                    leerEnteroInventario(jTextFieldCantidadDisponibleInventario.getText()),
                    // Cantidad reservada (obligatoria).
                    leerEnteroInventario(jTextFieldCantidadReservadaInventario.getText()));

            // Si se guardó.
            if (registrado) {
                // Vacía el formulario.
                limpiarCamposInventario();
                // Recarga la tabla con los datos actuales.
                cargarInventario();
            }
        // Un número escrito con letras o formato inválido.
        } catch (NumberFormatException ex) {
            // Avisa que el dato numérico no es válido.
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese cantidades numéricas válidas",
                    "Dato inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    // Guarda los cambios del registro de inventario seleccionado.
    private void actualizarInventario() {
        // Captura cantidades no numéricas.
        try {
            // Producto elegido.
            Producto producto = (Producto) jComboBoxProductoInventario.getSelectedItem();
            // Almacén elegido.
            Almacen almacen = (Almacen) jComboBoxAlmacenInventario.getSelectedItem();

            // UPDATE directo del registro de inventario; sin selección, el controlador lo avisa.
            boolean actualizado = Inventario_control.actualizarInventario(
                    idInventarioSeleccionado,
                    producto == null ? null : producto.getIdProducto(),
                    almacen == null ? null : almacen.getIdAlmacen(),
                    leerEnteroInventario(jTextFieldCantidadDisponibleInventario.getText()),
                    leerEnteroInventario(jTextFieldCantidadReservadaInventario.getText()));

            // Si se actualizó.
            if (actualizado) {
                // Vacía el formulario.
                limpiarCamposInventario();
                // Recarga la tabla con los datos actuales.
                cargarInventario();
            }
        // Un número escrito con letras o formato inválido.
        } catch (NumberFormatException ex) {
            // Avisa que el dato numérico no es válido.
            javax.swing.JOptionPane.showMessageDialog(this, "Ingrese cantidades numéricas válidas",
                    "Dato inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    // Elimina el registro de inventario seleccionado.
    private void eliminarInventario() {
        // Pide confirmación y ejecuta el DELETE en inventarioporalmacen; sin selección, lo avisa.
        boolean eliminado = Inventario_control.eliminarInventario(idInventarioSeleccionado);
        // Si se eliminó.
        if (eliminado) {
            // Vacía el formulario.
            limpiarCamposInventario();
            // Recarga la tabla con los datos actuales.
            cargarInventario();
        }
    }

    // Deja el formulario vacío y sin registro seleccionado.
    private void limpiarCamposInventario() {
        // Sin selección.
        idInventarioSeleccionado = null;
        // Solo si hay productos.
        if (jComboBoxProductoInventario.getItemCount() > 0) {
            // Selecciona el primero.
            jComboBoxProductoInventario.setSelectedIndex(0);
        }
        // Solo si hay almacenes.
        if (jComboBoxAlmacenInventario.getItemCount() > 0) {
            // Selecciona el primero.
            jComboBoxAlmacenInventario.setSelectedIndex(0);
        }
        // Vacía la cantidad disponible.
        jTextFieldCantidadDisponibleInventario.setText("");
        // Vacía la cantidad reservada.
        jTextFieldCantidadReservadaInventario.setText("");
        // Quita la selección de la tabla.
        jTableInventario.clearSelection();
    }

    // Convierte una cantidad de inventario a entero; vacía no se permite.
    private Integer leerEnteroInventario(String texto) {
        // Campo vacío.
        if (texto == null || texto.trim().isEmpty()) {
            // Las cantidades son obligatorias.
            throw new NumberFormatException("valor vacío");
        }
        // Lanza NumberFormatException si no es un entero.
        return Integer.parseInt(texto.trim());
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
            java.util.logging.Logger.getLogger(Productos_Inventario_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Productos_Inventario_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Productos_Inventario_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Productos_Inventario_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Productos_Inventario_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonActualizarInventario;
    private javax.swing.JButton jButtonActualizarProducto;
    private javax.swing.JButton jButtonEliminarInventario;
    private javax.swing.JButton jButtonEliminarProducto;
    private javax.swing.JButton jButtonGuardarProducto;
    private javax.swing.JButton jButtonLimpiarInventario;
    private javax.swing.JButton jButtonLimpiarProducto;
    private javax.swing.JButton jButtonQuitarImagenProducto;
    private javax.swing.JButton jButtonRegistrarInventario;
    private javax.swing.JButton jButtonSeleccionarImagenProducto;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<Almacen> jComboBoxAlmacenInventario;
    private javax.swing.JComboBox<Categoria> jComboBoxCategoriaProducto;
    private javax.swing.JComboBox<String> jComboBoxEstadoProducto;
    private javax.swing.JComboBox<Producto> jComboBoxProductoInventario;
    private javax.swing.JLabel jLabelAlmacenInventario;
    private javax.swing.JLabel jLabelCantidadDisponibleInventario;
    private javax.swing.JLabel jLabelCantidadProducto;
    private javax.swing.JLabel jLabelCantidadReservadaInventario;
    private javax.swing.JLabel jLabelCategoriaProducto;
    private javax.swing.JLabel jLabelDescripcionProducto;
    private javax.swing.JLabel jLabelEstadoProducto;
    private javax.swing.JLabel jLabelImagenProducto;
    private javax.swing.JLabel jLabelNombreProducto;
    private javax.swing.JLabel jLabelPrecioCompraProducto;
    private javax.swing.JLabel jLabelPrecioVentaProducto;
    private javax.swing.JLabel jLabelProductoInventario;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelBotones;
    private javax.swing.JPanel jPanelBotonesInventario;
    private javax.swing.JPanel jPanelBotonesProducto;
    private javax.swing.JPanel jPanelFormularioInventario;
    private javax.swing.JPanel jPanelFormularioProducto;
    private javax.swing.JPanel jPanelInventario;
    private javax.swing.JPanel jPanelProductos;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPaneInventario;
    private javax.swing.JScrollPane jScrollPaneProductos;
    private javax.swing.JTabbedPane jTabbedPaneProductosInventario;
    private javax.swing.JTable jTableInventario;
    private javax.swing.JTable jTableProductos;
    private javax.swing.JTextField jTextFieldCantidadDisponibleInventario;
    private javax.swing.JTextField jTextFieldCantidadProducto;
    private javax.swing.JTextField jTextFieldCantidadReservadaInventario;
    private javax.swing.JTextField jTextFieldDescripcionProducto;
    private javax.swing.JTextField jTextFieldNombreProducto;
    private javax.swing.JTextField jTextFieldPrecioCompraProducto;
    private javax.swing.JTextField jTextFieldPrecioVentaProducto;
    // End of variables declaration//GEN-END:variables
}
