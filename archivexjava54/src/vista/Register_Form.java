// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Register_control valida los datos y registra el usuario en la tabla usuarios.
import controlador.Register_control;

// Ventana de registro de usuarios nuevos; al terminar vuelve a Login_Form.
public class Register_Form extends javax.swing.JFrame {
    // Constructor: arma la ventana y le aplica logo, tema y tamaño.
    public Register_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);

        // Centra el logo y el texto del título.
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // El texto queda centrado respecto al logo.
        jLabelTitulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        // El texto del título aparece debajo del logo.
        jLabelTitulo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Registro de usuario");
        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);

        // Tamaño inicial 560x700 (antes 450x600) y mínimo 500x640, limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 560, 700, 500, 640);
    }

    // initComponents() lo genera el GUI Builder de NetBeans a partir del .form;
    // no se edita a mano porque NetBeans lo regenera al guardar el diseño.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
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
        jLabelConfirmar = new javax.swing.JLabel();
        jPasswordFieldConfirmar = new javax.swing.JPasswordField();
        jLabelRol = new javax.swing.JLabel();
        jComboBoxRol = new javax.swing.JComboBox<>();
        jButtonRegistrar = new javax.swing.JButton();
        jLabelVolver = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));

        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("ArchiveX");

        javax.swing.GroupLayout jPanelTituloLayout = new javax.swing.GroupLayout(jPanelTitulo);
        jPanelTitulo.setLayout(jPanelTituloLayout);
        jPanelTituloLayout.setHorizontalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelTitulo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelTituloLayout.setVerticalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabelTitulo)
                .addGap(16, 16, 16))
        );

        jLabelNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelNombre.setText("Nombre:");

        jTextFieldNombre.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelApellido.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelApellido.setText("Apellido:");

        jTextFieldApellido.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelCorreo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCorreo.setText("Correo:");

        jTextFieldCorreo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelTelefono.setText("Teléfono:");

        jTextFieldTelefono.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelContrasena.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelContrasena.setText("Contraseña:");

        jPasswordFieldContrasena.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelConfirmar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelConfirmar.setText("Confirmar:");

        jPasswordFieldConfirmar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelRol.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelRol.setText("Rol:");

        jComboBoxRol.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jComboBoxRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Usuario", "Administrador" }));

        jButtonRegistrar.setBackground(new java.awt.Color(237, 149, 62));
        jButtonRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRegistrar.setText("Registrar");
        jButtonRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistrarActionPerformed(evt);
            }
        });

        jLabelVolver.setForeground(new java.awt.Color(237, 149, 62));
        jLabelVolver.setText("¿Ya tienes cuenta? Inicia sesión");
        jLabelVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelVolverMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNombre)
                    .addComponent(jLabelApellido)
                    .addComponent(jLabelCorreo)
                    .addComponent(jLabelTelefono)
                    .addComponent(jLabelContrasena)
                    .addComponent(jLabelConfirmar)
                    .addComponent(jLabelRol))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxRol, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonRegistrar)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelVolver)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombre)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelApellido)
                    .addComponent(jTextFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCorreo)
                    .addComponent(jTextFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTelefono)
                    .addComponent(jTextFieldTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelContrasena)
                    .addComponent(jPasswordFieldContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelConfirmar)
                    .addComponent(jPasswordFieldConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRol)
                    .addComponent(jComboBoxRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addComponent(jButtonRegistrar)
                .addGap(15, 15, 15)
                .addComponent(jLabelVolver)
                .addContainerGap(25, Short.MAX_VALUE))
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

    // Botón Registrar: envía los datos al controlador y, si se guardaron, vuelve al login.
    private void jButtonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistrarActionPerformed

        // Nombre escrito por el usuario.
        String nombre = jTextFieldNombre.getText();
        // Apellido.
        String apellido = jTextFieldApellido.getText();
        // Correo, que será el usuario para iniciar sesión.
        String correo = jTextFieldCorreo.getText();
        // Teléfono (opcional).
        String telefono = jTextFieldTelefono.getText();
        // Contraseña convertida a texto.
        String contrasena = String.valueOf(jPasswordFieldContrasena.getPassword());
        // Confirmación de la contraseña.
        String confirmar = String.valueOf(jPasswordFieldConfirmar.getPassword());
        // Rol elegido en el combo, convertido al valor del ENUM ('usuario' o 'administrador').
        String rol = modelo.Usuario.rolDesdeTexto((String) jComboBoxRol.getSelectedItem());

        // Valida campos obligatorios, formato de correo, coincidencia de contraseñas y correo duplicado;
        // guarda el usuario y su rol con Usuario.insertUsuario (que también escribe historialusuarios).
        boolean registrado = Register_control.registrarUsuario(nombre, apellido, correo, contrasena, confirmar, telefono,
                rol);

        // Usuario registrado.
        if (registrado) {
            // Vuelve a la ventana de login.
            vista.Login_Form loginForm = new vista.Login_Form();
            // Cerrar esa ventana con la X termina la aplicación.
            loginForm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            // Centra la nueva ventana en la pantalla.
            loginForm.setLocationRelativeTo(null);
            // Muestra la nueva ventana.
            loginForm.setVisible(true);
            // Cierra esta ventana y libera sus recursos.
            this.dispose();
        }
    }//GEN-LAST:event_jButtonRegistrarActionPerformed

    // Clic en 'Volver': regresa al login sin registrar.
    private void jLabelVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelVolverMouseClicked

        // Crea la ventana de login.
        vista.Login_Form loginForm = new vista.Login_Form();
        // Cerrar esa ventana con la X termina la aplicación.
        loginForm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        loginForm.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        loginForm.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jLabelVolverMouseClicked

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
            java.util.logging.Logger.getLogger(Register_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Register_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Register_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Register_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Register_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRegistrar;
    private javax.swing.JComboBox<String> jComboBoxRol;
    private javax.swing.JLabel jLabelApellido;
    private javax.swing.JLabel jLabelConfirmar;
    private javax.swing.JLabel jLabelContrasena;
    private javax.swing.JLabel jLabelCorreo;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelRol;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelVolver;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JPasswordField jPasswordFieldConfirmar;
    private javax.swing.JPasswordField jPasswordFieldContrasena;
    private javax.swing.JTextField jTextFieldApellido;
    private javax.swing.JTextField jTextFieldCorreo;
    private javax.swing.JTextField jTextFieldNombre;
    private javax.swing.JTextField jTextFieldTelefono;
    // End of variables declaration//GEN-END:variables
}
