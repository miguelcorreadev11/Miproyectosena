// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Reportes_control exporta la tabla a Excel y registra el reporte.
import controlador.Reportes_control;
// Usuarios_control valida los datos y llama a los métodos SQL de Usuario.
import controlador.Usuarios_control;
// ArrayList: usuarios cargados en la tabla.
import java.util.ArrayList;
// DefaultTableCellRenderer: base del renderizador de la columna Estado.
import javax.swing.table.DefaultTableCellRenderer;
// DefaultTableModel: modelo de filas de la tabla de usuarios.
import javax.swing.table.DefaultTableModel;
// Usuario: modelo de la tabla usuarios.
import modelo.Usuario;

// Ventana de administración de usuarios: alta, edición, cambio de contraseña, estado y eliminación.
public class Usuarios_Form extends javax.swing.JFrame {
    // Usuarios mostrados, en el mismo orden que las filas de la tabla.
    private ArrayList<Usuario> listaUsuarios;
    // Id del usuario elegido en la tabla; null si no hay selección.
    private Integer idUsuarioSeleccionado;

    // Índice de la columna Estado, que usa un renderizador propio.
    private static final int COLUMNA_ESTADO = 5;

    // Constructor: arma la tabla, llena el combo de estado y carga los usuarios de MySQL.
    public Usuarios_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Muestra el avatar genérico junto al formulario.
        RecursosApp.aplicarAvatarUsuario(jLabelAvatarUsuario);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Administrador de Usuarios");

        // Modelo de la tabla con las columnas de usuarios y sin filas iniciales.
        DefaultTableModel modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Apellido", "Correo", "Teléfono", "Estado", "Rol", "Fecha de creación"}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo a la tabla.
        jTableUsuarios.setModel(modeloTabla);
        // La columna Estado se pinta en negrita (activo) o atenuada (inactivo).
        jTableUsuarios.getColumnModel().getColumn(COLUMNA_ESTADO).setCellRenderer(new EstadoCellRenderer());

        // Opción del combo que se guarda como 'activo'.
        jComboBoxEstado.addItem("Activo");
        // Opción que se guarda como 'inactivo'.
        jComboBoxEstado.addItem("Inactivo");
        // Opción del combo de rol que se guarda como 'usuario'.
        jComboBoxRol.addItem("Usuario");
        // Opción que se guarda como 'administrador'.
        jComboBoxRol.addItem("Administrador");

        // Llena la tabla con los usuarios de la base de datos.
        cargarUsuarios();
        // Alto visible de la tabla (unas 8 filas).
        jTableUsuarios.setPreferredScrollableViewportSize(new java.awt.Dimension(760, 220));
        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableUsuarios, jScrollPaneUsuarios);
        // Carga el usuario en el formulario también cuando la fila cambia con el teclado o
        // arrastrando el mouse (mouseClicked no se dispara en esos casos).
        jTableUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTableUsuarios.getSelectedRow() >= 0) {
                mostrarUsuario(jTableUsuarios.getSelectedRow());
            }
        });
        // Cerrar con la X equivale a "Volver al Menú".
        RecursosApp.cerrarConBoton(this, jButtonVolver);
        // Ajusta la ventana al tamaño preferido de sus componentes.
        this.pack();
        // Tamaño inicial 1150x720 (antes el de pack(): 854x517), limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 1150, 720, 980, 600);
    }

    // initComponents() lo genera el GUI Builder de NetBeans a partir del .form;
    // no se edita a mano porque NetBeans lo regenera al guardar el diseño.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jPanelFormulario = new javax.swing.JPanel();
        jLabelAvatarUsuario = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jTextFieldNombre = new javax.swing.JTextField();
        jLabelApellido = new javax.swing.JLabel();
        jTextFieldApellido = new javax.swing.JTextField();
        jLabelCorreo = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jLabelTelefono = new javax.swing.JLabel();
        jTextFieldTelefono = new javax.swing.JTextField();
        jLabelContrasena = new javax.swing.JLabel();
        jPasswordFieldContrasena = new javax.swing.JPasswordField();
        jLabelEstado = new javax.swing.JLabel();
        jComboBoxEstado = new javax.swing.JComboBox<>();
        jLabelRol = new javax.swing.JLabel();
        jComboBoxRol = new javax.swing.JComboBox<>();
        jPanelBotones = new javax.swing.JPanel();
        jButtonInsertar = new javax.swing.JButton();
        jButtonActualizar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();
        jButtonExportarExcel = new javax.swing.JButton();
        jButtonVolver = new javax.swing.JButton();
        jScrollPaneUsuarios = new javax.swing.JScrollPane();
        jTableUsuarios = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));

        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Administrador de Usuarios");

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

        jPanelFormulario.setBackground(new java.awt.Color(11, 18, 32));

        jLabelAvatarUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabelNombre.setText("Nombre:");

        jLabelApellido.setText("Apellido:");

        jLabelCorreo.setText("Correo electrónico:");

        jLabelTelefono.setText("Teléfono:");

        jLabelContrasena.setText("Contraseña (vacío = no cambiar):");

        jLabelEstado.setText("Estado:");

        jLabelRol.setText("Rol:");

        javax.swing.GroupLayout jPanelFormularioLayout = new javax.swing.GroupLayout(jPanelFormulario);
        jPanelFormulario.setLayout(jPanelFormularioLayout);
        jPanelFormularioLayout.setHorizontalGroup(
            jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabelAvatarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNombre)
                    .addComponent(jLabelCorreo)
                    .addComponent(jLabelContrasena)
                    .addComponent(jLabelRol))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxRol, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelApellido)
                    .addComponent(jLabelTelefono)
                    .addComponent(jLabelEstado))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanelFormularioLayout.setVerticalGroup(
            jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabelAvatarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(jPanelFormularioLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombre)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelApellido)
                    .addComponent(jTextFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCorreo)
                    .addComponent(jTextFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelefono)
                    .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelContrasena)
                    .addComponent(jPasswordFieldContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelEstado)
                    .addComponent(jComboBoxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRol)
                    .addComponent(jComboBoxRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jButtonInsertar.setBackground(new java.awt.Color(237, 149, 62));
        jButtonInsertar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonInsertar.setText("Insertar");
        jButtonInsertar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInsertarActionPerformed(evt);
            }
        });

        jButtonActualizar.setBackground(new java.awt.Color(237, 149, 62));
        jButtonActualizar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizar.setText("Actualizar");
        jButtonActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarActionPerformed(evt);
            }
        });

        jButtonEliminar.setBackground(new java.awt.Color(153, 0, 0));
        jButtonEliminar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonEliminar.setText("Eliminar");
        jButtonEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEliminarActionPerformed(evt);
            }
        });

        jButtonLimpiar.setBackground(new java.awt.Color(15, 23, 42));
        jButtonLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLimpiar.setText("Limpiar");
        jButtonLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimpiarActionPerformed(evt);
            }
        });

        jButtonExportarExcel.setBackground(new java.awt.Color(15, 23, 42));
        jButtonExportarExcel.setForeground(new java.awt.Color(255, 255, 255));
        jButtonExportarExcel.setText("Exportar a Excel");
        jButtonExportarExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportarExcelActionPerformed(evt);
            }
        });

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
                .addContainerGap()
                .addComponent(jButtonInsertar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonExportarExcel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonVolver)
                .addContainerGap())
        );
        jPanelBotonesLayout.setVerticalGroup(
            jPanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonInsertar)
                    .addComponent(jButtonActualizar)
                    .addComponent(jButtonEliminar)
                    .addComponent(jButtonLimpiar)
                    .addComponent(jButtonExportarExcel)
                    .addComponent(jButtonVolver))
                .addContainerGap())
        );

        jTableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableUsuariosMouseClicked(evt);
            }
        });
        jScrollPaneUsuarios.setViewportView(jTableUsuarios);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
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

    // Botón Actualizar: guarda los cambios del usuario seleccionado.
    private void jButtonActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarActionPerformed
        // Actualiza datos y, si se escribió, la contraseña.
        actualizarUsuario();
    }//GEN-LAST:event_jButtonActualizarActionPerformed

    // Botón Eliminar: borra el usuario seleccionado (pide confirmación).
    private void jButtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEliminarActionPerformed
        // Elimina y recarga la tabla.
        eliminarUsuario();
    }//GEN-LAST:event_jButtonEliminarActionPerformed

    // Botón Limpiar: vacía el formulario.
    private void jButtonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarActionPerformed
        // Borra campos y selección.
        limpiarCampos();
    }//GEN-LAST:event_jButtonLimpiarActionPerformed

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

    // Clic en la tabla: carga el usuario de esa fila en el formulario.
    private void jTableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableUsuariosMouseClicked

        // Fila seleccionada; -1 si el clic fue fuera de las filas.
        int fila = jTableUsuarios.getSelectedRow();
        // Solo si hay una fila válida.
        if (fila >= 0) {
            // Copia sus datos a los campos.
            mostrarUsuario(fila);
        }
    }//GEN-LAST:event_jTableUsuariosMouseClicked

    // Botón Insertar: registra un usuario nuevo con los datos del formulario.
    private void jButtonInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInsertarActionPerformed
        // Valida y guarda el usuario.
        insertarUsuario();
    }//GEN-LAST:event_jButtonInsertarActionPerformed

    // Botón Exportar Excel: exporta la tabla y la registra como reporte 'Usuarios registrados'.
    private void jButtonExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportarExcelActionPerformed

        // Exporta la tabla tal como se ve.
        Reportes_control.exportarYRegistrar(jTableUsuarios, "Usuarios registrados");
    }//GEN-LAST:event_jButtonExportarExcelActionPerformed

    // Consulta los usuarios en MySQL y vuelve a llenar la tabla.
    private void cargarUsuarios() {
        // SELECT de todos los usuarios ordenados por id.
        listaUsuarios = Usuarios_control.listarUsuarios();

        // Modelo actual de la tabla.
        DefaultTableModel modelo = (DefaultTableModel) jTableUsuarios.getModel();
        // Borra las filas anteriores.
        modelo.setRowCount(0);

        // Agrega una fila por usuario.
        for (Usuario usuario : listaUsuarios) {
            // Valores en el orden de las columnas.
            modelo.addRow(new Object[]{
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreoElectronico(),
                usuario.getTelefono(),
                // Estado con símbolo para distinguirlo a simple vista.
                usuario.isEstado() ? "● Activo" : "○ Inactivo",
                // Rol leído de la columna rol: 'Usuario' o 'Administrador'.
                Usuario.textoRol(usuario.getRol()),
                usuario.getFechaCreacion()
            });
        }
    }

    // Copia a los campos el usuario de la posición indicada.
    private void mostrarUsuario(int posicion) {
        // Posición fuera de la lista.
        if (listaUsuarios == null || posicion < 0 || posicion >= listaUsuarios.size()) {
            // No hace nada.
            return;
        }

        // Usuario de esa fila.
        Usuario usuario = listaUsuarios.get(posicion);
        // Guarda su id para actualizar o eliminar.
        idUsuarioSeleccionado = usuario.getIdUsuario();

        // Nombre.
        jTextFieldNombre.setText(usuario.getNombre());
        // Apellido.
        jTextFieldApellido.setText(usuario.getApellido());
        // Correo.
        jTextFieldCorreo.setText(usuario.getCorreoElectronico());
        // Teléfono.
        jTextFieldTelefono.setText(usuario.getTelefono());
        // La contraseña nunca se muestra; se deja vacía (solo se cambia si se escribe una nueva).
        jPasswordFieldContrasena.setText("");
        // Selecciona Activo o Inactivo según el estado.
        jComboBoxEstado.setSelectedItem(usuario.isEstado() ? "Activo" : "Inactivo");
        // Selecciona Usuario o Administrador según su rol.
        jComboBoxRol.setSelectedItem(Usuario.textoRol(usuario.getRol()));

        // Mantiene resaltada la fila elegida.
        jTableUsuarios.setRowSelectionInterval(posicion, posicion);
    }

    // Crea un usuario con los datos del formulario.
    private void insertarUsuario() {
        // true si el combo dice 'Activo'.
        boolean estadoActivo = "Activo".equals(jComboBoxEstado.getSelectedItem());

        // Valida y ejecuta el INSERT; también registra 'registro' en historialusuarios.
        boolean creado = Usuarios_control.crearUsuario(
                jTextFieldNombre.getText(),
                jTextFieldApellido.getText(),
                jTextFieldCorreo.getText(),
                String.valueOf(jPasswordFieldContrasena.getPassword()),
                jTextFieldTelefono.getText(),
                estadoActivo,
                Usuario.rolDesdeTexto((String) jComboBoxRol.getSelectedItem()));

        // Si se creó.
        if (creado) {
            // Vacía el formulario.
            limpiarCampos();
            // Recarga la tabla para mostrar el nuevo usuario.
            cargarUsuarios();
        }
    }

    // Actualiza el usuario seleccionado y, si se escribió, su contraseña.
    private void actualizarUsuario() {
        // true si el combo dice 'Activo'.
        boolean estadoActivo = "Activo".equals(jComboBoxEstado.getSelectedItem());

        // UPDATE de datos, estado y rol; si no hay selección, el controlador lo avisa y devuelve false.
        boolean actualizado = Usuarios_control.actualizarUsuario(
                idUsuarioSeleccionado,
                jTextFieldNombre.getText(),
                jTextFieldApellido.getText(),
                jTextFieldCorreo.getText(),
                jTextFieldTelefono.getText(),
                estadoActivo,
                Usuario.rolDesdeTexto((String) jComboBoxRol.getSelectedItem()));

        // Contraseña nueva escrita (puede estar vacía).
        String nuevaContrasena = String.valueOf(jPasswordFieldContrasena.getPassword());
        // Si no se cambia la contraseña se considera correcto.
        boolean contrasenaActualizada = true;
        // Solo cambia la contraseña si los datos se guardaron y se escribió una.
        if (actualizado && !nuevaContrasena.isEmpty()) {
            // UPDATE de la contraseña con hash SHA-256 y registro en historialusuarios.
            contrasenaActualizada = Usuarios_control.actualizarContrasena(idUsuarioSeleccionado, nuevaContrasena);
        }

        // Si todo se guardó.
        if (actualizado && contrasenaActualizada) {
            // Vacía el formulario.
            limpiarCampos();
            // Recarga la tabla con los datos nuevos.
            cargarUsuarios();
        }
    }

    // Elimina el usuario seleccionado.
    private void eliminarUsuario() {
        // Pide confirmación y ejecuta el DELETE con su historial.
        boolean eliminado = Usuarios_control.eliminarUsuario(idUsuarioSeleccionado);
        // Si se eliminó.
        if (eliminado) {
            // Vacía el formulario.
            limpiarCampos();
            // Recarga la tabla.
            cargarUsuarios();
        }
    }

    // Deja el formulario vacío y sin usuario seleccionado.
    private void limpiarCampos() {
        // Sin selección.
        idUsuarioSeleccionado = null;
        // Vacía el nombre.
        jTextFieldNombre.setText("");
        // Vacía el apellido.
        jTextFieldApellido.setText("");
        // Vacía el correo.
        jTextFieldCorreo.setText("");
        // Vacía el teléfono.
        jTextFieldTelefono.setText("");
        // Vacía la contraseña.
        jPasswordFieldContrasena.setText("");
        // Vuelve a 'Activo'.
        jComboBoxEstado.setSelectedIndex(0);
        // Vuelve al rol 'Usuario'.
        jComboBoxRol.setSelectedIndex(0);
        // Quita la selección de la tabla.
        jTableUsuarios.clearSelection();
    }

    // Renderizador de la columna Estado: negrita para activos y texto atenuado para inactivos.
    private static class EstadoCellRenderer extends DefaultTableCellRenderer {
        // Swing lo llama para dibujar cada celda de la columna.
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable tabla, Object valor,
                boolean isSelected, boolean hasFocus, int fila, int columna) {
            // Obtiene la etiqueta por defecto con el texto de la celda.
            java.awt.Component componente = super.getTableCellRendererComponent(tabla, valor, isSelected, hasFocus, fila, columna);

            // Activo si el texto contiene 'Activo' y no 'Inactivo'.
            boolean activo = valor != null && valor.toString().contains("Activo")
                    && !valor.toString().contains("Inactivo");

            // Negrita para activos, normal para inactivos.
            componente.setFont(tabla.getFont().deriveFont(activo ? java.awt.Font.BOLD : java.awt.Font.PLAIN));
            // La fila seleccionada conserva el color de selección del tema.
            if (!isSelected) {
                // Texto blanco para activos y gris atenuado para inactivos.
                componente.setForeground(activo ? TemaColores.TEXTO : TemaColores.TEXTO_ATENUADO);
            }
            // Componente listo para dibujar.
            return componente;
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
            java.util.logging.Logger.getLogger(Usuarios_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Usuarios_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Usuarios_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Usuarios_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Usuarios_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonActualizar;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonExportarExcel;
    private javax.swing.JButton jButtonInsertar;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<String> jComboBoxEstado;
    private javax.swing.JComboBox<String> jComboBoxRol;
    private javax.swing.JLabel jLabelApellido;
    private javax.swing.JLabel jLabelAvatarUsuario;
    private javax.swing.JLabel jLabelContrasena;
    private javax.swing.JLabel jLabelCorreo;
    private javax.swing.JLabel jLabelEstado;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelRol;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelBotones;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JPasswordField jPasswordFieldContrasena;
    private javax.swing.JScrollPane jScrollPaneUsuarios;
    private javax.swing.JTable jTableUsuarios;
    private javax.swing.JTextField jTextFieldApellido;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTelefono;
    // End of variables declaration//GEN-END:variables
}
