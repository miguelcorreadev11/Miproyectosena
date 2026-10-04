// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Login_control valida las credenciales contra la tabla usuarios.
import controlador.Login_control;
// Usuario: registro devuelto cuando el inicio de sesión es correcto.
import modelo.Usuario;

// Ventana de inicio de sesión; SplashScreen la abre al arrancar y lleva a Home_Form o Register_Form.
public class Login_Form extends javax.swing.JFrame {
    // Constructor: arma la ventana y le aplica logo, tema y tamaño.
    public Login_Form() {
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
        this.setTitle("ArchiveX - Iniciar sesión");
        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);

        // Enter en los campos equivale a pulsar "Ingresar".
        getRootPane().setDefaultButton(jButtonIngresar);
        // Tamaño inicial 520x640 (antes 450x600) y mínimo 460x560, limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 520, 640, 460, 560);
    }

    // initComponents() lo genera el GUI Builder de NetBeans a partir del .form;
    // no se edita a mano porque NetBeans lo regenera al guardar el diseño.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelCorreo = new javax.swing.JLabel();
        jTextFieldCorreo = new javax.swing.JTextField();
        jLabelContrasena = new javax.swing.JLabel();
        jPasswordFieldContrasena = new javax.swing.JPasswordField();
        jButtonIngresar = new javax.swing.JButton();
        jLabelRegistro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));

        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
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
                .addGap(18, 18, 18)
                .addComponent(jLabelTitulo)
                .addGap(18, 18, 18))
        );

        jLabelCorreo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCorreo.setText("Correo:");

        jTextFieldCorreo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelContrasena.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelContrasena.setText("Contraseña:");

        jPasswordFieldContrasena.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jButtonIngresar.setBackground(new java.awt.Color(237, 149, 62));
        jButtonIngresar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonIngresar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonIngresar.setText("Ingresar");
        jButtonIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIngresarActionPerformed(evt);
            }
        });

        jLabelRegistro.setForeground(new java.awt.Color(237, 149, 62));
        jLabelRegistro.setText("¿No tienes cuenta? Regístrate aquí");
        jLabelRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelRegistroMouseClicked(evt);
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
                    .addComponent(jLabelCorreo)
                    .addComponent(jLabelContrasena))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordFieldContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonIngresar)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelRegistro)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCorreo)
                    .addComponent(jTextFieldCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelContrasena)
                    .addComponent(jPasswordFieldContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(jButtonIngresar)
                .addGap(15, 15, 15)
                .addComponent(jLabelRegistro)
                .addContainerGap(30, Short.MAX_VALUE))
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

    // Botón Ingresar: valida el correo y la contraseña y, si son correctos, abre el menú principal.
    private void jButtonIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIngresarActionPerformed

        // Correo escrito por el usuario.
        String correo = jTextFieldCorreo.getText();
        // Contraseña del JPasswordField convertida a texto; el controlador calcula su hash SHA-256.
        String contrasena = String.valueOf(jPasswordFieldContrasena.getPassword());

        // Devuelve el usuario si las credenciales son válidas y está activo; si no, muestra el error y devuelve null.
        Usuario usuario = Login_control.validarLogin(correo, contrasena);

        // Inicio de sesión correcto (Login_control ya guardó el usuario de la sesión).
        if (usuario != null) {
            // Crea el menú principal.
            vista.Home_Form homeForm = new vista.Home_Form();
            // Cerrar esa ventana con la X termina la aplicación.
            homeForm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            // Centra la nueva ventana en la pantalla.
            homeForm.setLocationRelativeTo(null);
            // Muestra la nueva ventana.
            homeForm.setVisible(true);
            // Cierra esta ventana y libera sus recursos.
            this.dispose();
        }
    }//GEN-LAST:event_jButtonIngresarActionPerformed

    // Clic en el texto de registro: abre Register_Form y cierra el login.
    private void jLabelRegistroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelRegistroMouseClicked

        // Crea la ventana de registro.
        vista.Register_Form registerForm = new vista.Register_Form();
        // Cerrar esa ventana con la X termina la aplicación.
        registerForm.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        registerForm.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        registerForm.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jLabelRegistroMouseClicked

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
            java.util.logging.Logger.getLogger(Login_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Login_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Login_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Login_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Login_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonIngresar;
    private javax.swing.JLabel jLabelContrasena;
    private javax.swing.JLabel jLabelCorreo;
    private javax.swing.JLabel jLabelRegistro;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JPasswordField jPasswordFieldContrasena;
    private javax.swing.JTextField jTextFieldCorreo;
    // End of variables declaration//GEN-END:variables
}
