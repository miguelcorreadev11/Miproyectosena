// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Almacen_control valida y ejecuta las operaciones de la tabla almacenes.
import controlador.Almacen_control;
// Categoria_control valida y ejecuta las operaciones de la tabla categorias.
import controlador.Categoria_control;
// Proveedor_control valida y ejecuta las operaciones de la tabla proveedores.
import controlador.Proveedor_control;
// ArrayList: registros cargados en cada tabla.
import java.util.ArrayList;
// DefaultTableModel: modelo de filas de las tres tablas.
import javax.swing.table.DefaultTableModel;
// Almacen: modelo de la tabla almacenes.
import modelo.Almacen;
// Categoria: modelo de la tabla categorias.
import modelo.Categoria;
// Proveedor: modelo de la tabla proveedores.
import modelo.Proveedor;

// Ventana de catálogos con tres pestañas (categorías, almacenes y proveedores); cada una
// permite guardar, actualizar, eliminar y limpiar usando su controlador.
public class Catalogos_Form extends javax.swing.JFrame {
    // Categorías mostradas, en el mismo orden que las filas de la tabla.
    private ArrayList<Categoria> listaCategorias;
    // Id de la categoría elegida; null si no hay selección.
    private Integer idCategoriaSeleccionada;

    // Almacenes mostrados, en el mismo orden que las filas.
    private ArrayList<Almacen> listaAlmacenes;
    // Id del almacén elegido; null si no hay selección.
    private Integer idAlmacenSeleccionado;

    // Proveedores mostrados, en el mismo orden que las filas.
    private ArrayList<Proveedor> listaProveedores;
    // Id del proveedor elegido; null si no hay selección.
    private Integer idProveedorSeleccionado;

    // Constructor: arma las tres tablas, llena los combos de estado y carga los datos de MySQL.
    public Catalogos_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Catálogos");

        // Pestañas con el fondo del tema.
        jTabbedPaneCatalogos.setBackground(TemaColores.FONDO_PRINCIPAL);
        // Texto de las pestañas en blanco.
        jTabbedPaneCatalogos.setForeground(TemaColores.TEXTO);
        // Fuente base del tema.
        jTabbedPaneCatalogos.setFont(TemaColores.FUENTE_BASE);

        // Modelo de la tabla de categorias con sus columnas y sin filas iniciales.
        DefaultTableModel modeloCategorias = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Descripción", "Estado"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo a la tabla.
        jTableCategorias.setModel(modeloCategorias);
        // Opción 'activo' del ENUM estado.
        jComboBoxEstadoCategoria.addItem("activo");
        // Opción 'inactivo' del ENUM estado.
        jComboBoxEstadoCategoria.addItem("inactivo");
        // Llena la tabla de categorías.
        cargarCategorias();
        // Tamaño visible de la tabla.
        jTableCategorias.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 200));

        // Modelo de la tabla de almacenes con sus columnas y sin filas iniciales.
        DefaultTableModel modeloAlmacenes = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Ubicación", "Ciudad", "Capacidad Máx.", "Estado"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo a la tabla.
        jTableAlmacenes.setModel(modeloAlmacenes);
        // Opción 'activo' del ENUM estado.
        jComboBoxEstadoAlmacen.addItem("activo");
        // Opción 'inactivo' del ENUM estado.
        jComboBoxEstadoAlmacen.addItem("inactivo");
        // Llena la tabla de almacenes.
        cargarAlmacenes();
        // Tamaño visible de la tabla.
        jTableAlmacenes.setPreferredScrollableViewportSize(new java.awt.Dimension(600, 200));

        // Modelo de la tabla de proveedores con sus columnas y sin filas iniciales.
        DefaultTableModel modeloProveedores = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Correo", "Teléfono", "Dirección", "Ciudad", "País", "Estado"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo a la tabla.
        jTableProveedores.setModel(modeloProveedores);
        // Opción 'activo' del ENUM estado.
        jComboBoxEstadoProveedor.addItem("activo");
        // Opción 'inactivo' del ENUM estado.
        jComboBoxEstadoProveedor.addItem("inactivo");
        // Llena la tabla de proveedores.
        cargarProveedores();
        // Tamaño visible de la tabla.
        jTableProveedores.setPreferredScrollableViewportSize(new java.awt.Dimension(700, 200));

        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableCategorias, jScrollPaneCategorias);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableAlmacenes, jScrollPaneAlmacenes);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableProveedores, jScrollPaneProveedores);

        // Carga el registro en el formulario también cuando la fila cambia con el teclado o
        // arrastrando el mouse (mouseClicked no se dispara en esos casos y el formulario quedaba
        // mostrando otro registro distinto al resaltado en la tabla).
        jTableCategorias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTableCategorias.getSelectedRow() >= 0) {
                cargarCategoriaSeleccionada();
            }
        });
        jTableAlmacenes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTableAlmacenes.getSelectedRow() >= 0) {
                cargarAlmacenSeleccionado();
            }
        });
        jTableProveedores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTableProveedores.getSelectedRow() >= 0) {
                cargarProveedorSeleccionado();
            }
        });
        // Cerrar con la X equivale a "Volver al Menú".
        RecursosApp.cerrarConBoton(this, jButtonVolver);

        // Ajusta la ventana al tamaño preferido de sus componentes.
        this.pack();
        // Tamaño inicial 1100x720 (antes el de pack(): 755x619), limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 1100, 720, 900, 620);
    }

    // initComponents() lo genera el GUI Builder de NetBeans a partir del .form;
    // no se edita a mano porque NetBeans lo regenera al guardar el diseño.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jTabbedPaneCatalogos = new javax.swing.JTabbedPane();
        jPanelCategorias = new javax.swing.JPanel();
        jPanelFormularioCategoria = new javax.swing.JPanel();
        jLabelNombreCategoria = new javax.swing.JLabel();
        jTextFieldNombreCategoria = new javax.swing.JTextField();
        jLabelDescripcionCategoria = new javax.swing.JLabel();
        jTextFieldDescripcionCategoria = new javax.swing.JTextField();
        jLabelEstadoCategoria = new javax.swing.JLabel();
        jComboBoxEstadoCategoria = new javax.swing.JComboBox<>();
        jPanelBotonesCategoria = new javax.swing.JPanel();
        jButtonGuardarCategoria = new javax.swing.JButton();
        jButtonActualizarCategoria = new javax.swing.JButton();
        jButtonEliminarCategoria = new javax.swing.JButton();
        jButtonLimpiarCategoria = new javax.swing.JButton();
        jScrollPaneCategorias = new javax.swing.JScrollPane();
        jTableCategorias = new javax.swing.JTable();
        jPanelAlmacenes = new javax.swing.JPanel();
        jPanelFormularioAlmacen = new javax.swing.JPanel();
        jLabelNombreAlmacen = new javax.swing.JLabel();
        jTextFieldNombreAlmacen = new javax.swing.JTextField();
        jLabelUbicacionAlmacen = new javax.swing.JLabel();
        jTextFieldUbicacionAlmacen = new javax.swing.JTextField();
        jLabelCiudadAlmacen = new javax.swing.JLabel();
        jTextFieldCiudadAlmacen = new javax.swing.JTextField();
        jLabelCapacidadMaximaAlmacen = new javax.swing.JLabel();
        jTextFieldCapacidadMaximaAlmacen = new javax.swing.JTextField();
        jLabelEstadoAlmacen = new javax.swing.JLabel();
        jComboBoxEstadoAlmacen = new javax.swing.JComboBox<>();
        jPanelBotonesAlmacen = new javax.swing.JPanel();
        jButtonGuardarAlmacen = new javax.swing.JButton();
        jButtonActualizarAlmacen = new javax.swing.JButton();
        jButtonEliminarAlmacen = new javax.swing.JButton();
        jButtonLimpiarAlmacen = new javax.swing.JButton();
        jScrollPaneAlmacenes = new javax.swing.JScrollPane();
        jTableAlmacenes = new javax.swing.JTable();
        jPanelProveedores = new javax.swing.JPanel();
        jPanelFormularioProveedor = new javax.swing.JPanel();
        jLabelNombreProveedor = new javax.swing.JLabel();
        jTextFieldNombreProveedor = new javax.swing.JTextField();
        jLabelCorreoProveedor = new javax.swing.JLabel();
        jTextFieldCorreoProveedor = new javax.swing.JTextField();
        jLabelTelefonoProveedor = new javax.swing.JLabel();
        jTextFieldTelefonoProveedor = new javax.swing.JTextField();
        jLabelDireccionProveedor = new javax.swing.JLabel();
        jTextFieldDireccionProveedor = new javax.swing.JTextField();
        jLabelCiudadProveedor = new javax.swing.JLabel();
        jTextFieldCiudadProveedor = new javax.swing.JTextField();
        jLabelPaisProveedor = new javax.swing.JLabel();
        jTextFieldPaisProveedor = new javax.swing.JTextField();
        jLabelEstadoProveedor = new javax.swing.JLabel();
        jComboBoxEstadoProveedor = new javax.swing.JComboBox<>();
        jPanelBotonesProveedor = new javax.swing.JPanel();
        jButtonGuardarProveedor = new javax.swing.JButton();
        jButtonActualizarProveedor = new javax.swing.JButton();
        jButtonEliminarProveedor = new javax.swing.JButton();
        jButtonLimpiarProveedor = new javax.swing.JButton();
        jScrollPaneProveedores = new javax.swing.JScrollPane();
        jTableProveedores = new javax.swing.JTable();
        jPanelBotones = new javax.swing.JPanel();
        jButtonVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));

        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Catálogos");

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

        jPanelCategorias.setBackground(new java.awt.Color(11, 18, 32));

        jLabelNombreCategoria.setText("Nombre:");

        jLabelDescripcionCategoria.setText("Descripción:");

        jLabelEstadoCategoria.setText("Estado:");

        jPanelFormularioCategoria.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelFormularioCategoriaLayout = new javax.swing.GroupLayout(jPanelFormularioCategoria);
        jPanelFormularioCategoria.setLayout(jPanelFormularioCategoriaLayout);
        jPanelFormularioCategoriaLayout.setHorizontalGroup(
            jPanelFormularioCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioCategoriaLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelFormularioCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNombreCategoria)
                    .addComponent(jLabelDescripcionCategoria)
                    .addComponent(jLabelEstadoCategoria))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDescripcionCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxEstadoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanelFormularioCategoriaLayout.setVerticalGroup(
            jPanelFormularioCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioCategoriaLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelFormularioCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombreCategoria)
                    .addComponent(jTextFieldNombreCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDescripcionCategoria)
                    .addComponent(jTextFieldDescripcionCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEstadoCategoria)
                    .addComponent(jComboBoxEstadoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jButtonGuardarCategoria.setBackground(new java.awt.Color(237, 149, 62));
        jButtonGuardarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarCategoria.setText("Guardar");
        jButtonGuardarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGuardarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarCategoriaActionPerformed(evt);
            }
        });

        jButtonActualizarCategoria.setBackground(new java.awt.Color(237, 149, 62));
        jButtonActualizarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizarCategoria.setText("Actualizar");
        jButtonActualizarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarCategoriaActionPerformed(evt);
            }
        });

        jButtonEliminarCategoria.setBackground(new java.awt.Color(153, 0, 0));
        jButtonEliminarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarCategoria.setText("Eliminar");
        jButtonEliminarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarCategoriaActionPerformed(evt);
            }
        });

        jButtonLimpiarCategoria.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarCategoria.setText("Limpiar");
        jButtonLimpiarCategoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarCategoriaActionPerformed(evt);
            }
        });

        jPanelBotonesCategoria.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelBotonesCategoriaLayout = new javax.swing.GroupLayout(jPanelBotonesCategoria);
        jPanelBotonesCategoria.setLayout(jPanelBotonesCategoriaLayout);
        jPanelBotonesCategoriaLayout.setHorizontalGroup(
            jPanelBotonesCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesCategoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonGuardarCategoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonActualizarCategoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminarCategoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpiarCategoria)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBotonesCategoriaLayout.setVerticalGroup(
            jPanelBotonesCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesCategoriaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotonesCategoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardarCategoria)
                    .addComponent(jButtonActualizarCategoria)
                    .addComponent(jButtonEliminarCategoria)
                    .addComponent(jButtonLimpiarCategoria))
                .addContainerGap())
        );

        jTableCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCategoriasMouseClicked(evt);
            }
        });
        jScrollPaneCategorias.setViewportView(jTableCategorias);

        javax.swing.GroupLayout jPanelCategoriasLayout = new javax.swing.GroupLayout(jPanelCategorias);
        jPanelCategorias.setLayout(jPanelCategoriasLayout);
        jPanelCategoriasLayout.setHorizontalGroup(
            jPanelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFormularioCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBotonesCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelCategoriasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelCategoriasLayout.setVerticalGroup(
            jPanelCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCategoriasLayout.createSequentialGroup()
                .addComponent(jPanelFormularioCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelBotonesCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneCatalogos.addTab("Categorías", jPanelCategorias);

        jPanelAlmacenes.setBackground(new java.awt.Color(11, 18, 32));

        jLabelNombreAlmacen.setText("Nombre:");

        jLabelUbicacionAlmacen.setText("Ubicación:");

        jLabelCiudadAlmacen.setText("Ciudad:");

        jLabelCapacidadMaximaAlmacen.setText("Capacidad máxima:");

        jLabelEstadoAlmacen.setText("Estado:");

        jPanelFormularioAlmacen.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelFormularioAlmacenLayout = new javax.swing.GroupLayout(jPanelFormularioAlmacen);
        jPanelFormularioAlmacen.setLayout(jPanelFormularioAlmacenLayout);
        jPanelFormularioAlmacenLayout.setHorizontalGroup(
            jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioAlmacenLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNombreAlmacen)
                    .addComponent(jLabelUbicacionAlmacen)
                    .addComponent(jLabelCiudadAlmacen)
                    .addComponent(jLabelCapacidadMaximaAlmacen)
                    .addComponent(jLabelEstadoAlmacen))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNombreAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldUbicacionAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCiudadAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCapacidadMaximaAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxEstadoAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanelFormularioAlmacenLayout.setVerticalGroup(
            jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioAlmacenLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombreAlmacen)
                    .addComponent(jTextFieldNombreAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUbicacionAlmacen)
                    .addComponent(jTextFieldUbicacionAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCiudadAlmacen)
                    .addComponent(jTextFieldCiudadAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCapacidadMaximaAlmacen)
                    .addComponent(jTextFieldCapacidadMaximaAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEstadoAlmacen)
                    .addComponent(jComboBoxEstadoAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jButtonGuardarAlmacen.setBackground(new java.awt.Color(237, 149, 62));
        jButtonGuardarAlmacen.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarAlmacen.setText("Guardar");
        jButtonGuardarAlmacen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGuardarAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarAlmacenActionPerformed(evt);
            }
        });

        jButtonActualizarAlmacen.setBackground(new java.awt.Color(237, 149, 62));
        jButtonActualizarAlmacen.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizarAlmacen.setText("Actualizar");
        jButtonActualizarAlmacen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizarAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarAlmacenActionPerformed(evt);
            }
        });

        jButtonEliminarAlmacen.setBackground(new java.awt.Color(153, 0, 0));
        jButtonEliminarAlmacen.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarAlmacen.setText("Eliminar");
        jButtonEliminarAlmacen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminarAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarAlmacenActionPerformed(evt);
            }
        });

        jButtonLimpiarAlmacen.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarAlmacen.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarAlmacen.setText("Limpiar");
        jButtonLimpiarAlmacen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarAlmacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarAlmacenActionPerformed(evt);
            }
        });

        jPanelBotonesAlmacen.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelBotonesAlmacenLayout = new javax.swing.GroupLayout(jPanelBotonesAlmacen);
        jPanelBotonesAlmacen.setLayout(jPanelBotonesAlmacenLayout);
        jPanelBotonesAlmacenLayout.setHorizontalGroup(
            jPanelBotonesAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesAlmacenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonGuardarAlmacen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonActualizarAlmacen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminarAlmacen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpiarAlmacen)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBotonesAlmacenLayout.setVerticalGroup(
            jPanelBotonesAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesAlmacenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotonesAlmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardarAlmacen)
                    .addComponent(jButtonActualizarAlmacen)
                    .addComponent(jButtonEliminarAlmacen)
                    .addComponent(jButtonLimpiarAlmacen))
                .addContainerGap())
        );

        jTableAlmacenes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAlmacenesMouseClicked(evt);
            }
        });
        jScrollPaneAlmacenes.setViewportView(jTableAlmacenes);

        javax.swing.GroupLayout jPanelAlmacenesLayout = new javax.swing.GroupLayout(jPanelAlmacenes);
        jPanelAlmacenes.setLayout(jPanelAlmacenesLayout);
        jPanelAlmacenesLayout.setHorizontalGroup(
            jPanelAlmacenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFormularioAlmacen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBotonesAlmacen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelAlmacenesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneAlmacenes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelAlmacenesLayout.setVerticalGroup(
            jPanelAlmacenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAlmacenesLayout.createSequentialGroup()
                .addComponent(jPanelFormularioAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelBotonesAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneAlmacenes, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneCatalogos.addTab("Almacenes", jPanelAlmacenes);

        jPanelProveedores.setBackground(new java.awt.Color(11, 18, 32));

        jLabelNombreProveedor.setText("Nombre:");

        jLabelCorreoProveedor.setText("Correo:");

        jLabelTelefonoProveedor.setText("Teléfono:");

        jLabelDireccionProveedor.setText("Dirección:");

        jLabelCiudadProveedor.setText("Ciudad:");

        jLabelPaisProveedor.setText("País:");

        jLabelEstadoProveedor.setText("Estado:");

        jPanelFormularioProveedor.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelFormularioProveedorLayout = new javax.swing.GroupLayout(jPanelFormularioProveedor);
        jPanelFormularioProveedor.setLayout(jPanelFormularioProveedorLayout);
        jPanelFormularioProveedorLayout.setHorizontalGroup(
            jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioProveedorLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNombreProveedor)
                    .addComponent(jLabelCorreoProveedor)
                    .addComponent(jLabelTelefonoProveedor)
                    .addComponent(jLabelDireccionProveedor))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCorreoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCiudadProveedor)
                    .addComponent(jLabelPaisProveedor)
                    .addComponent(jLabelEstadoProveedor))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCiudadProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPaisProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxEstadoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanelFormularioProveedorLayout.setVerticalGroup(
            jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioProveedorLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombreProveedor)
                    .addComponent(jTextFieldNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCiudadProveedor)
                    .addComponent(jTextFieldCiudadProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCorreoProveedor)
                    .addComponent(jTextFieldCorreoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPaisProveedor)
                    .addComponent(jTextFieldPaisProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTelefonoProveedor)
                    .addComponent(jTextFieldTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEstadoProveedor)
                    .addComponent(jComboBoxEstadoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDireccionProveedor)
                    .addComponent(jTextFieldDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jButtonGuardarProveedor.setBackground(new java.awt.Color(237, 149, 62));
        jButtonGuardarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardarProveedor.setText("Guardar");
        jButtonGuardarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGuardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGuardarProveedorActionPerformed(evt);
            }
        });

        jButtonActualizarProveedor.setBackground(new java.awt.Color(237, 149, 62));
        jButtonActualizarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizarProveedor.setText("Actualizar");
        jButtonActualizarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarProveedorActionPerformed(evt);
            }
        });

        jButtonEliminarProveedor.setBackground(new java.awt.Color(153, 0, 0));
        jButtonEliminarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminarProveedor.setText("Eliminar");
        jButtonEliminarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarProveedorActionPerformed(evt);
            }
        });

        jButtonLimpiarProveedor.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiarProveedor.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiarProveedor.setText("Limpiar");
        jButtonLimpiarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarProveedorActionPerformed(evt);
            }
        });

        jPanelBotonesProveedor.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelBotonesProveedorLayout = new javax.swing.GroupLayout(jPanelBotonesProveedor);
        jPanelBotonesProveedor.setLayout(jPanelBotonesProveedorLayout);
        jPanelBotonesProveedorLayout.setHorizontalGroup(
            jPanelBotonesProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesProveedorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonGuardarProveedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonActualizarProveedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminarProveedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpiarProveedor)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelBotonesProveedorLayout.setVerticalGroup(
            jPanelBotonesProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesProveedorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotonesProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonGuardarProveedor)
                    .addComponent(jButtonActualizarProveedor)
                    .addComponent(jButtonEliminarProveedor)
                    .addComponent(jButtonLimpiarProveedor))
                .addContainerGap())
        );

        jTableProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableProveedoresMouseClicked(evt);
            }
        });
        jScrollPaneProveedores.setViewportView(jTableProveedores);

        javax.swing.GroupLayout jPanelProveedoresLayout = new javax.swing.GroupLayout(jPanelProveedores);
        jPanelProveedores.setLayout(jPanelProveedoresLayout);
        jPanelProveedoresLayout.setHorizontalGroup(
            jPanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFormularioProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBotonesProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelProveedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelProveedoresLayout.setVerticalGroup(
            jPanelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProveedoresLayout.createSequentialGroup()
                .addComponent(jPanelFormularioProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelBotonesProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPaneCatalogos.addTab("Proveedores", jPanelProveedores);

        jPanelBotones.setBackground(new java.awt.Color(11, 18, 32));

        jButtonVolver.setBackground(new java.awt.Color(15, 23, 42));
        jButtonVolver.setForeground(new java.awt.Color(255, 255, 255));
        jButtonVolver.setText("Volver al Menú");
        jButtonVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVolverActionPerformed(evt);
            }
        });

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
                .addComponent(jTabbedPaneCatalogos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jTabbedPaneCatalogos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    // Botón Guardar: registra una categoría nueva.
    private void jButtonGuardarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarCategoriaActionPerformed
        // Valida los datos y ejecuta el INSERT.
        guardarCategoria();
    }//GEN-LAST:event_jButtonGuardarCategoriaActionPerformed

    // Botón Actualizar: guarda los cambios de la categoría seleccionada.
    private void jButtonActualizarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarCategoriaActionPerformed
        // Valida los datos y ejecuta el UPDATE.
        actualizarCategoria();
    }//GEN-LAST:event_jButtonActualizarCategoriaActionPerformed

    // Botón Eliminar: borra la categoría seleccionada.
    private void jButtonEliminarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarCategoriaActionPerformed
        // Pide confirmación y ejecuta el DELETE.
        eliminarCategoria();
    }//GEN-LAST:event_jButtonEliminarCategoriaActionPerformed

    // Botón Limpiar: vacía el formulario de la categoría.
    private void jButtonLimpiarCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarCategoriaActionPerformed
        // Borra campos y selección.
        limpiarCamposCategoria();
    }//GEN-LAST:event_jButtonLimpiarCategoriaActionPerformed

    // Clic en la tabla: carga la categoría de esa fila en el formulario.
    private void jTableCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCategoriasMouseClicked
        // Copia los datos de la fila a los campos.
        cargarCategoriaSeleccionada();
    }//GEN-LAST:event_jTableCategoriasMouseClicked

    // Consulta las categorías en MySQL y vuelve a llenar la tabla.
    private void cargarCategorias() {
        // SELECT de las categorías de la tabla categorias.
        listaCategorias = Categoria_control.listarCategorias();

        // Modelo actual de la tabla.
        DefaultTableModel modelo = (DefaultTableModel) jTableCategorias.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);

        // Agrega una fila por cada categoría.
        for (Categoria categoria : listaCategorias) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                categoria.getIdCategoria(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado()
            });
        }
    }

    // Copia al formulario la categoría de la fila seleccionada.
    private void cargarCategoriaSeleccionada() {
        // Fila seleccionada; -1 si no hay.
        int fila = jTableCategorias.getSelectedRow();
        // Sin selección válida.
        if (fila < 0 || listaCategorias == null || fila >= listaCategorias.size()) {
            // No hace nada.
            return;
        }

        // Registro de esa fila.
        Categoria categoria = listaCategorias.get(fila);
        // Guarda su id para actualizar o eliminar.
        idCategoriaSeleccionada = categoria.getIdCategoria();

        // Muestra el nombre.
        jTextFieldNombreCategoria.setText(categoria.getNombre());
        // Muestra la descripción.
        jTextFieldDescripcionCategoria.setText(categoria.getDescripcion());
        // Selecciona su estado en el combo.
        jComboBoxEstadoCategoria.setSelectedItem(categoria.getEstado());
    }

    // Registra una categoría con los datos del formulario.
    private void guardarCategoria() {
        // Valida los datos y ejecuta el INSERT en categorias; los errores los informa el controlador.
        boolean creado = Categoria_control.crearCategoria(
                jTextFieldNombreCategoria.getText(),
                jTextFieldDescripcionCategoria.getText(),
                (String) jComboBoxEstadoCategoria.getSelectedItem());

        // Si se guardó.
        if (creado) {
            // Vacía el formulario.
            limpiarCamposCategoria();
            // Recarga la tabla con los datos actuales.
            cargarCategorias();
        }
    }

    // Guarda los cambios de la categoría seleccionada.
    private void actualizarCategoria() {
        // Valida y ejecuta el UPDATE en categorias; sin selección, el controlador lo avisa.
        boolean actualizado = Categoria_control.actualizarCategoria(
                idCategoriaSeleccionada,
                jTextFieldNombreCategoria.getText(),
                jTextFieldDescripcionCategoria.getText(),
                (String) jComboBoxEstadoCategoria.getSelectedItem());

        // Si se actualizó.
        if (actualizado) {
            // Vacía el formulario.
            limpiarCamposCategoria();
            // Recarga la tabla con los datos actuales.
            cargarCategorias();
        }
    }

    // Elimina la categoría seleccionada.
    private void eliminarCategoria() {
        // Pide confirmación y ejecuta el DELETE en categorias; sin selección, lo avisa.
        boolean eliminado = Categoria_control.eliminarCategoria(idCategoriaSeleccionada);
        // Si se eliminó.
        if (eliminado) {
            // Vacía el formulario.
            limpiarCamposCategoria();
            // Recarga la tabla con los datos actuales.
            cargarCategorias();
        }
    }

    // Deja el formulario vacío y sin registro seleccionado.
    private void limpiarCamposCategoria() {
        // Sin selección.
        idCategoriaSeleccionada = null;
        // Vacía el nombre.
        jTextFieldNombreCategoria.setText("");
        // Vacía la descripción.
        jTextFieldDescripcionCategoria.setText("");
        // Vuelve al primer estado (activo).
        jComboBoxEstadoCategoria.setSelectedIndex(0);
        // Quita la selección de la tabla.
        jTableCategorias.clearSelection();
    }

    // Botón Guardar: registra un almacén nuevo.
    private void jButtonGuardarAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarAlmacenActionPerformed
        // Valida los datos y ejecuta el INSERT.
        guardarAlmacen();
    }//GEN-LAST:event_jButtonGuardarAlmacenActionPerformed

    // Botón Actualizar: guarda los cambios del almacén seleccionado.
    private void jButtonActualizarAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarAlmacenActionPerformed
        // Valida los datos y ejecuta el UPDATE.
        actualizarAlmacen();
    }//GEN-LAST:event_jButtonActualizarAlmacenActionPerformed

    // Botón Eliminar: borra el almacén seleccionado.
    private void jButtonEliminarAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarAlmacenActionPerformed
        // Pide confirmación y ejecuta el DELETE.
        eliminarAlmacen();
    }//GEN-LAST:event_jButtonEliminarAlmacenActionPerformed

    // Botón Limpiar: vacía el formulario del almacén.
    private void jButtonLimpiarAlmacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarAlmacenActionPerformed
        // Borra campos y selección.
        limpiarCamposAlmacen();
    }//GEN-LAST:event_jButtonLimpiarAlmacenActionPerformed

    // Clic en la tabla: carga el almacén de esa fila en el formulario.
    private void jTableAlmacenesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAlmacenesMouseClicked
        // Copia los datos de la fila a los campos.
        cargarAlmacenSeleccionado();
    }//GEN-LAST:event_jTableAlmacenesMouseClicked

    // Consulta los almacenes en MySQL y vuelve a llenar la tabla.
    private void cargarAlmacenes() {
        // SELECT de los almacenes de la tabla almacenes.
        listaAlmacenes = Almacen_control.listarAlmacenes();

        // Modelo actual de la tabla.
        DefaultTableModel modelo = (DefaultTableModel) jTableAlmacenes.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);

        // Agrega una fila por cada almacén.
        for (Almacen almacen : listaAlmacenes) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                almacen.getIdAlmacen(),
                almacen.getNombre(),
                almacen.getUbicacion(),
                almacen.getCiudad(),
                almacen.getCapacidadMaxima(),
                almacen.getEstado()
            });
        }
    }

    // Copia al formulario el almacén de la fila seleccionada.
    private void cargarAlmacenSeleccionado() {
        // Fila seleccionada; -1 si no hay.
        int fila = jTableAlmacenes.getSelectedRow();
        // Sin selección válida.
        if (fila < 0 || listaAlmacenes == null || fila >= listaAlmacenes.size()) {
            // No hace nada.
            return;
        }

        // Registro de esa fila.
        Almacen almacen = listaAlmacenes.get(fila);
        // Guarda su id para actualizar o eliminar.
        idAlmacenSeleccionado = almacen.getIdAlmacen();

        // Muestra el nombre.
        jTextFieldNombreAlmacen.setText(almacen.getNombre());
        // Muestra la ubicación.
        jTextFieldUbicacionAlmacen.setText(almacen.getUbicacion());
        // Muestra la ciudad.
        jTextFieldCiudadAlmacen.setText(almacen.getCiudad());
        // Capacidad vacía si el almacén no la tiene registrada.
        jTextFieldCapacidadMaximaAlmacen.setText(
                almacen.getCapacidadMaxima() == null ? "" : String.valueOf(almacen.getCapacidadMaxima()));
        // Selecciona su estado en el combo.
        jComboBoxEstadoAlmacen.setSelectedItem(almacen.getEstado());
    }

    // Registra un almacén con los datos del formulario.
    private void guardarAlmacen() {
        // Captura la capacidad no numérica que lanza leerCapacidad.
        try {
            // Valida los datos y ejecuta el INSERT en almacenes; los errores los informa el controlador.
            boolean creado = Almacen_control.crearAlmacen(
                    jTextFieldNombreAlmacen.getText(),
                    jTextFieldUbicacionAlmacen.getText(),
                    jTextFieldCiudadAlmacen.getText(),
                    // Capacidad convertida a entero o null si está vacía.
                    leerCapacidad(jTextFieldCapacidadMaximaAlmacen.getText()),
                    (String) jComboBoxEstadoAlmacen.getSelectedItem());

            // Si se guardó.
            if (creado) {
                // Vacía el formulario.
                limpiarCamposAlmacen();
                // Recarga la tabla con los datos actuales.
                cargarAlmacenes();
            }
        // Un número escrito con letras o formato inválido.
        } catch (NumberFormatException ex) {
            // Avisa que el dato numérico no es válido.
            javax.swing.JOptionPane.showMessageDialog(this, "La capacidad máxima debe ser un número entero",
                    "Dato inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    // Guarda los cambios del almacén seleccionado.
    private void actualizarAlmacen() {
        // Captura la capacidad no numérica que lanza leerCapacidad.
        try {
            // Valida y ejecuta el UPDATE en almacenes; sin selección, el controlador lo avisa.
            boolean actualizado = Almacen_control.actualizarAlmacen(
                    idAlmacenSeleccionado,
                    jTextFieldNombreAlmacen.getText(),
                    jTextFieldUbicacionAlmacen.getText(),
                    jTextFieldCiudadAlmacen.getText(),
                    leerCapacidad(jTextFieldCapacidadMaximaAlmacen.getText()),
                    (String) jComboBoxEstadoAlmacen.getSelectedItem());

            // Si se actualizó.
            if (actualizado) {
                // Vacía el formulario.
                limpiarCamposAlmacen();
                // Recarga la tabla con los datos actuales.
                cargarAlmacenes();
            }
        // Un número escrito con letras o formato inválido.
        } catch (NumberFormatException ex) {
            // Avisa que el dato numérico no es válido.
            javax.swing.JOptionPane.showMessageDialog(this, "La capacidad máxima debe ser un número entero",
                    "Dato inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    // Elimina el almacén seleccionado.
    private void eliminarAlmacen() {
        // Pide confirmación y ejecuta el DELETE en almacenes; sin selección, lo avisa.
        boolean eliminado = Almacen_control.eliminarAlmacen(idAlmacenSeleccionado);
        // Si se eliminó.
        if (eliminado) {
            // Vacía el formulario.
            limpiarCamposAlmacen();
            // Recarga la tabla con los datos actuales.
            cargarAlmacenes();
        }
    }

    // Deja el formulario vacío y sin registro seleccionado.
    private void limpiarCamposAlmacen() {
        // Sin selección.
        idAlmacenSeleccionado = null;
        // Vacía el nombre.
        jTextFieldNombreAlmacen.setText("");
        // Vacía la ubicación.
        jTextFieldUbicacionAlmacen.setText("");
        // Vacía la ciudad.
        jTextFieldCiudadAlmacen.setText("");
        // Vacía la capacidad máxima.
        jTextFieldCapacidadMaximaAlmacen.setText("");
        // Vuelve al primer estado (activo).
        jComboBoxEstadoAlmacen.setSelectedIndex(0);
        // Quita la selección de la tabla.
        jTableAlmacenes.clearSelection();
    }

    // Convierte la capacidad a entero; null si está vacía (columna opcional).
    private Integer leerCapacidad(String texto) {
        // Campo vacío.
        if (texto == null || texto.trim().isEmpty()) {
            // Sin capacidad.
            return null;
        }
        // Lanza NumberFormatException si el texto no es un entero.
        return Integer.parseInt(texto.trim());
    }

    // Botón Guardar: registra un proveedor nuevo.
    private void jButtonGuardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarProveedorActionPerformed
        // Valida los datos y ejecuta el INSERT.
        guardarProveedor();
    }//GEN-LAST:event_jButtonGuardarProveedorActionPerformed

    // Botón Actualizar: guarda los cambios del proveedor seleccionado.
    private void jButtonActualizarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarProveedorActionPerformed
        // Valida los datos y ejecuta el UPDATE.
        actualizarProveedor();
    }//GEN-LAST:event_jButtonActualizarProveedorActionPerformed

    // Botón Eliminar: borra el proveedor seleccionado.
    private void jButtonEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarProveedorActionPerformed
        // Pide confirmación y ejecuta el DELETE.
        eliminarProveedor();
    }//GEN-LAST:event_jButtonEliminarProveedorActionPerformed

    // Botón Limpiar: vacía el formulario del proveedor.
    private void jButtonLimpiarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarProveedorActionPerformed
        // Borra campos y selección.
        limpiarCamposProveedor();
    }//GEN-LAST:event_jButtonLimpiarProveedorActionPerformed

    // Clic en la tabla: carga el proveedor de esa fila en el formulario.
    private void jTableProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableProveedoresMouseClicked
        // Copia los datos de la fila a los campos.
        cargarProveedorSeleccionado();
    }//GEN-LAST:event_jTableProveedoresMouseClicked

    // Consulta los proveedores en MySQL y vuelve a llenar la tabla.
    private void cargarProveedores() {
        // SELECT de los proveedores de la tabla proveedores.
        listaProveedores = Proveedor_control.listarProveedores();

        // Modelo actual de la tabla.
        DefaultTableModel modelo = (DefaultTableModel) jTableProveedores.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);

        // Agrega una fila por cada proveedor.
        for (Proveedor proveedor : listaProveedores) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                proveedor.getIdProveedor(),
                proveedor.getNombre(),
                proveedor.getCorreo(),
                proveedor.getTelefono(),
                proveedor.getDireccion(),
                proveedor.getCiudad(),
                proveedor.getPais(),
                proveedor.getEstado()
            });
        }
    }

    // Copia al formulario el proveedor de la fila seleccionada.
    private void cargarProveedorSeleccionado() {
        // Fila seleccionada; -1 si no hay.
        int fila = jTableProveedores.getSelectedRow();
        // Sin selección válida.
        if (fila < 0 || listaProveedores == null || fila >= listaProveedores.size()) {
            // No hace nada.
            return;
        }

        // Registro de esa fila.
        Proveedor proveedor = listaProveedores.get(fila);
        // Guarda su id para actualizar o eliminar.
        idProveedorSeleccionado = proveedor.getIdProveedor();

        // Muestra el nombre.
        jTextFieldNombreProveedor.setText(proveedor.getNombre());
        // Muestra el correo.
        jTextFieldCorreoProveedor.setText(proveedor.getCorreo());
        // Teléfono vacío si el proveedor no lo tiene.
        jTextFieldTelefonoProveedor.setText(proveedor.getTelefono() == null ? "" : proveedor.getTelefono());
        // Muestra la dirección.
        jTextFieldDireccionProveedor.setText(proveedor.getDireccion());
        // Muestra la ciudad.
        jTextFieldCiudadProveedor.setText(proveedor.getCiudad());
        // Muestra el país.
        jTextFieldPaisProveedor.setText(proveedor.getPais());
        // Selecciona su estado en el combo.
        jComboBoxEstadoProveedor.setSelectedItem(proveedor.getEstado());
    }

    // Registra un proveedor con los datos del formulario.
    private void guardarProveedor() {
        // Valida los datos y ejecuta el INSERT en proveedores; los errores los informa el controlador.
        boolean creado = Proveedor_control.crearProveedor(
                jTextFieldNombreProveedor.getText(),
                jTextFieldCorreoProveedor.getText(),
                jTextFieldTelefonoProveedor.getText(),
                jTextFieldDireccionProveedor.getText(),
                jTextFieldCiudadProveedor.getText(),
                jTextFieldPaisProveedor.getText(),
                (String) jComboBoxEstadoProveedor.getSelectedItem());

        // Si se guardó.
        if (creado) {
            // Vacía el formulario.
            limpiarCamposProveedor();
            // Recarga la tabla con los datos actuales.
            cargarProveedores();
        }
    }

    // Guarda los cambios del proveedor seleccionado.
    private void actualizarProveedor() {
        // Valida y ejecuta el UPDATE en proveedores; sin selección, el controlador lo avisa.
        boolean actualizado = Proveedor_control.actualizarProveedor(
                idProveedorSeleccionado,
                jTextFieldNombreProveedor.getText(),
                jTextFieldCorreoProveedor.getText(),
                jTextFieldTelefonoProveedor.getText(),
                jTextFieldDireccionProveedor.getText(),
                jTextFieldCiudadProveedor.getText(),
                jTextFieldPaisProveedor.getText(),
                (String) jComboBoxEstadoProveedor.getSelectedItem());

        // Si se actualizó.
        if (actualizado) {
            // Vacía el formulario.
            limpiarCamposProveedor();
            // Recarga la tabla con los datos actuales.
            cargarProveedores();
        }
    }

    // Elimina el proveedor seleccionado.
    private void eliminarProveedor() {
        // Pide confirmación y ejecuta el DELETE en proveedores; sin selección, lo avisa.
        boolean eliminado = Proveedor_control.eliminarProveedor(idProveedorSeleccionado);
        // Si se eliminó.
        if (eliminado) {
            // Vacía el formulario.
            limpiarCamposProveedor();
            // Recarga la tabla con los datos actuales.
            cargarProveedores();
        }
    }

    // Deja el formulario vacío y sin registro seleccionado.
    private void limpiarCamposProveedor() {
        // Sin selección.
        idProveedorSeleccionado = null;
        // Vacía el nombre.
        jTextFieldNombreProveedor.setText("");
        // Vacía el correo.
        jTextFieldCorreoProveedor.setText("");
        // Vacía el teléfono.
        jTextFieldTelefonoProveedor.setText("");
        // Vacía la dirección.
        jTextFieldDireccionProveedor.setText("");
        // Vacía la ciudad.
        jTextFieldCiudadProveedor.setText("");
        // Vacía el país.
        jTextFieldPaisProveedor.setText("");
        // Vuelve al primer estado (activo).
        jComboBoxEstadoProveedor.setSelectedIndex(0);
        // Quita la selección de la tabla.
        jTableProveedores.clearSelection();
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
            java.util.logging.Logger.getLogger(Catalogos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Catalogos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Catalogos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Catalogos_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Catalogos_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonActualizarAlmacen;
    private javax.swing.JButton jButtonActualizarCategoria;
    private javax.swing.JButton jButtonActualizarProveedor;
    private javax.swing.JButton jButtonEliminarAlmacen;
    private javax.swing.JButton jButtonEliminarCategoria;
    private javax.swing.JButton jButtonEliminarProveedor;
    private javax.swing.JButton jButtonGuardarAlmacen;
    private javax.swing.JButton jButtonGuardarCategoria;
    private javax.swing.JButton jButtonGuardarProveedor;
    private javax.swing.JButton jButtonLimpiarAlmacen;
    private javax.swing.JButton jButtonLimpiarCategoria;
    private javax.swing.JButton jButtonLimpiarProveedor;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<String> jComboBoxEstadoAlmacen;
    private javax.swing.JComboBox<String> jComboBoxEstadoCategoria;
    private javax.swing.JComboBox<String> jComboBoxEstadoProveedor;
    private javax.swing.JLabel jLabelCapacidadMaximaAlmacen;
    private javax.swing.JLabel jLabelCiudadAlmacen;
    private javax.swing.JLabel jLabelCiudadProveedor;
    private javax.swing.JLabel jLabelCorreoProveedor;
    private javax.swing.JLabel jLabelDescripcionCategoria;
    private javax.swing.JLabel jLabelDireccionProveedor;
    private javax.swing.JLabel jLabelEstadoAlmacen;
    private javax.swing.JLabel jLabelEstadoCategoria;
    private javax.swing.JLabel jLabelEstadoProveedor;
    private javax.swing.JLabel jLabelNombreAlmacen;
    private javax.swing.JLabel jLabelNombreCategoria;
    private javax.swing.JLabel jLabelNombreProveedor;
    private javax.swing.JLabel jLabelPaisProveedor;
    private javax.swing.JLabel jLabelTelefonoProveedor;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUbicacionAlmacen;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelAlmacenes;
    private javax.swing.JPanel jPanelBotones;
    private javax.swing.JPanel jPanelBotonesAlmacen;
    private javax.swing.JPanel jPanelBotonesCategoria;
    private javax.swing.JPanel jPanelBotonesProveedor;
    private javax.swing.JPanel jPanelCategorias;
    private javax.swing.JPanel jPanelFormularioAlmacen;
    private javax.swing.JPanel jPanelFormularioCategoria;
    private javax.swing.JPanel jPanelFormularioProveedor;
    private javax.swing.JPanel jPanelProveedores;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPaneAlmacenes;
    private javax.swing.JScrollPane jScrollPaneCategorias;
    private javax.swing.JScrollPane jScrollPaneProveedores;
    private javax.swing.JTabbedPane jTabbedPaneCatalogos;
    private javax.swing.JTable jTableAlmacenes;
    private javax.swing.JTable jTableCategorias;
    private javax.swing.JTable jTableProveedores;
    private javax.swing.JTextField jTextFieldCapacidadMaximaAlmacen;
    private javax.swing.JTextField jTextFieldCiudadAlmacen;
    private javax.swing.JTextField jTextFieldCiudadProveedor;
    private javax.swing.JTextField jTextFieldCorreoProveedor;
    private javax.swing.JTextField jTextFieldDescripcionCategoria;
    private javax.swing.JTextField jTextFieldDireccionProveedor;
    private javax.swing.JTextField jTextFieldNombreAlmacen;
    private javax.swing.JTextField jTextFieldNombreCategoria;
    private javax.swing.JTextField jTextFieldNombreProveedor;
    private javax.swing.JTextField jTextFieldPaisProveedor;
    private javax.swing.JTextField jTextFieldTelefonoProveedor;
    private javax.swing.JTextField jTextFieldUbicacionAlmacen;
    // End of variables declaration//GEN-END:variables
}
