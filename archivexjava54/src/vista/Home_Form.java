// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Menú principal: abre cada módulo y permite cerrar la sesión; cada módulo regresa aquí con 'Volver'.
public class Home_Form extends javax.swing.JFrame {
    // Constructor: arma el menú y le aplica logo, tema y tamaño.
    public Home_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();

        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Menú Principal");
        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Botones del menú principal más grandes que el resto: son la navegación de todo el sistema.
        for (javax.swing.JButton boton : new javax.swing.JButton[]{jButtonCatalogos, jButtonProductosInventario,
            jButtonUsuarios, jButtonReportes, jButtonCompras, jButtonMovimientos, jButtonBusquedaProductos}) {
            boton.setFont(TemaColores.FUENTE_BOTON.deriveFont(17f));
            boton.setMargin(new java.awt.Insets(12, 28, 12, 28));
        }
        // Tamaño inicial 1100x720 (antes 900x640); el mínimo evita que los botones y la imagen se encimen.
        RecursosApp.ajustarVentana(this, 1100, 720, 900, 600);
    }

    // initComponents() lo genera el GUI Builder de NetBeans a partir del .form;
    // no se edita a mano porque NetBeans lo regenera al guardar el diseño.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jButtonCerrarSesion = new javax.swing.JButton();
        jPanelMenu = new javax.swing.JPanel();
        jButtonCatalogos = new javax.swing.JButton();
        jButtonProductosInventario = new javax.swing.JButton();
        jButtonUsuarios = new javax.swing.JButton();
        jButtonReportes = new javax.swing.JButton();
        jButtonCompras = new javax.swing.JButton();
        jButtonMovimientos = new javax.swing.JButton();
        jButtonBusquedaProductos = new javax.swing.JButton();
        jPanelFondoInicio = new vista.PanelImagenFondo();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));

        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("ArchiveX - Menú Principal");

        jButtonCerrarSesion.setBackground(new java.awt.Color(237, 149, 62));
        jButtonCerrarSesion.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCerrarSesion.setText("Cerrar sesión");
        jButtonCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTituloLayout = new javax.swing.GroupLayout(jPanelTitulo);
        jPanelTitulo.setLayout(jPanelTituloLayout);
        jPanelTituloLayout.setHorizontalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabelTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCerrarSesion)
                .addGap(20, 20, 20))
        );
        jPanelTituloLayout.setVerticalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTitulo)
                    .addComponent(jButtonCerrarSesion))
                .addGap(14, 14, 14))
        );

        jPanelMenu.setBackground(new java.awt.Color(15, 23, 42));

        jButtonCatalogos.setBackground(new java.awt.Color(11, 18, 32));
        jButtonCatalogos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonCatalogos.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCatalogos.setText("Catálogos");
        jButtonCatalogos.setToolTipText("Categorías, Almacenes y Proveedores");
        jButtonCatalogos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCatalogos.setMargin(new java.awt.Insets(8, 20, 8, 20));
        jButtonCatalogos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCatalogosActionPerformed(evt);
            }
        });

        jButtonProductosInventario.setBackground(new java.awt.Color(11, 18, 32));
        jButtonProductosInventario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonProductosInventario.setForeground(new java.awt.Color(255, 255, 255));
        jButtonProductosInventario.setText("Productos e Inventario");
        jButtonProductosInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonProductosInventario.setMargin(new java.awt.Insets(8, 20, 8, 20));
        jButtonProductosInventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProductosInventarioActionPerformed(evt);
            }
        });

        jButtonUsuarios.setBackground(new java.awt.Color(11, 18, 32));
        jButtonUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        jButtonUsuarios.setText("Usuarios");
        jButtonUsuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonUsuarios.setMargin(new java.awt.Insets(8, 20, 8, 20));
        jButtonUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUsuariosActionPerformed(evt);
            }
        });

        jButtonReportes.setBackground(new java.awt.Color(11, 18, 32));
        jButtonReportes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonReportes.setForeground(new java.awt.Color(255, 255, 255));
        jButtonReportes.setText("Reportes");
        jButtonReportes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonReportes.setMargin(new java.awt.Insets(8, 20, 8, 20));
        jButtonReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReportesActionPerformed(evt);
            }
        });

        jButtonCompras.setBackground(new java.awt.Color(11, 18, 32));
        jButtonCompras.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonCompras.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCompras.setText("Compras");
        jButtonCompras.setToolTipText("Pedidos a proveedores y entrada al inventario");
        jButtonCompras.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonCompras.setMargin(new java.awt.Insets(8, 20, 8, 20));
        jButtonCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonComprasActionPerformed(evt);
            }
        });

        jButtonMovimientos.setBackground(new java.awt.Color(11, 18, 32));
        jButtonMovimientos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonMovimientos.setForeground(new java.awt.Color(255, 255, 255));
        jButtonMovimientos.setText("Movimientos");
        jButtonMovimientos.setToolTipText("Entradas, salidas, ajustes y devoluciones");
        jButtonMovimientos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonMovimientos.setMargin(new java.awt.Insets(8, 20, 8, 20));
        jButtonMovimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMovimientosActionPerformed(evt);
            }
        });

        jButtonBusquedaProductos.setBackground(new java.awt.Color(11, 18, 32));
        jButtonBusquedaProductos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButtonBusquedaProductos.setForeground(new java.awt.Color(255, 255, 255));
        jButtonBusquedaProductos.setText("Buscar productos");
        jButtonBusquedaProductos.setToolTipText("Búsqueda de productos con imagen");
        jButtonBusquedaProductos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonBusquedaProductos.setMargin(new java.awt.Insets(8, 20, 8, 20));
        jButtonBusquedaProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBusquedaProductosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonCatalogos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonProductosInventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonUsuarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonReportes)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButtonCompras)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonMovimientos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonBusquedaProductos)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCatalogos)
                    .addComponent(jButtonProductosInventario)
                    .addComponent(jButtonUsuarios)
                    .addComponent(jButtonReportes))
                .addGap(8, 8, 8)
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCompras)
                    .addComponent(jButtonMovimientos)
                    .addComponent(jButtonBusquedaProductos))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanelFondoInicioLayout = new javax.swing.GroupLayout(jPanelFondoInicio);
        jPanelFondoInicio.setLayout(jPanelFondoInicioLayout);
        jPanelFondoInicioLayout.setHorizontalGroup(
            jPanelFondoInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );
        jPanelFondoInicioLayout.setVerticalGroup(
            jPanelFondoInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelFondoInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelFondoInicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    // Botón Cerrar sesión: pide confirmación, borra el usuario de la sesión y vuelve al login.
    private void jButtonCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCerrarSesionActionPerformed

        // Pregunta Sí/No antes de cerrar la sesión.
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cerrar sesión?", "Cerrar sesión",
                javax.swing.JOptionPane.YES_NO_OPTION);

        // Solo si el usuario confirmó.
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            // Olvida el usuario guardado en Login_control; los módulos dejan de tener usuario responsable.
            controlador.Login_control.cerrarSesion();
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
        }
    }//GEN-LAST:event_jButtonCerrarSesionActionPerformed

    // Botón Catálogos: abre categorías, almacenes y proveedores.
    private void jButtonCatalogosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCatalogosActionPerformed

        // Crea la ventana de catálogos.
        vista.Catalogos_Form catalogosForm = new vista.Catalogos_Form();
        // Cerrar esa ventana con la X solo la cierra a ella.
        catalogosForm.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        catalogosForm.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        catalogosForm.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonCatalogosActionPerformed

    // Botón Productos e Inventario: abre la gestión de productos y existencias por almacén.
    private void jButtonProductosInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProductosInventarioActionPerformed

        // Crea la ventana de productos e inventario.
        vista.Productos_Inventario_Form productosInventarioForm = new vista.Productos_Inventario_Form();
        // Cerrar esa ventana con la X solo la cierra a ella.
        productosInventarioForm.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        productosInventarioForm.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        productosInventarioForm.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonProductosInventarioActionPerformed

    // Botón Usuarios: abre la administración de usuarios.
    private void jButtonUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUsuariosActionPerformed

        // Crea la ventana de usuarios.
        vista.Usuarios_Form usuariosForm = new vista.Usuarios_Form();
        // Cerrar esa ventana con la X solo la cierra a ella.
        usuariosForm.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        usuariosForm.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        usuariosForm.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonUsuariosActionPerformed

    // Botón Reportes: abre la generación y exportación de reportes.
    private void jButtonReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReportesActionPerformed

        // Crea la ventana de reportes.
        vista.Reportes_Form reportesForm = new vista.Reportes_Form();
        // Cerrar esa ventana con la X solo la cierra a ella.
        reportesForm.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        reportesForm.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        reportesForm.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonReportesActionPerformed

    // Botón Compras: abre el registro y entrega de pedidos a proveedores.
    private void jButtonComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonComprasActionPerformed

        // Crea la ventana de compras.
        vista.Compras_Form ventana = new vista.Compras_Form();
        // Cerrar esa ventana con la X solo la cierra a ella.
        ventana.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        ventana.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        ventana.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonComprasActionPerformed

    // Botón Movimientos: abre las entradas, salidas, ajustes y devoluciones de inventario.
    private void jButtonMovimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMovimientosActionPerformed

        // Crea la ventana de movimientos.
        vista.Movimientos_Form ventana = new vista.Movimientos_Form();
        // Cerrar esa ventana con la X solo la cierra a ella.
        ventana.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        ventana.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        ventana.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonMovimientosActionPerformed

    // Botón Búsqueda: abre la búsqueda de productos por filtros.
    private void jButtonBusquedaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBusquedaProductosActionPerformed

        // Crea la ventana de búsqueda.
        vista.Busqueda_Productos_Form ventana = new vista.Busqueda_Productos_Form();
        // Cerrar esa ventana con la X solo la cierra a ella.
        ventana.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        // Centra la nueva ventana en la pantalla.
        ventana.setLocationRelativeTo(null);
        // Muestra la nueva ventana.
        ventana.setVisible(true);
        // Cierra esta ventana y libera sus recursos.
        this.dispose();
    }//GEN-LAST:event_jButtonBusquedaProductosActionPerformed

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
            java.util.logging.Logger.getLogger(Home_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Home_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Home_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Home_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Home_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBusquedaProductos;
    private javax.swing.JButton jButtonCatalogos;
    private javax.swing.JButton jButtonCerrarSesion;
    private javax.swing.JButton jButtonCompras;
    private javax.swing.JButton jButtonMovimientos;
    private javax.swing.JButton jButtonProductosInventario;
    private javax.swing.JButton jButtonReportes;
    private javax.swing.JButton jButtonUsuarios;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private vista.PanelImagenFondo jPanelFondoInicio;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelTitulo;
    // End of variables declaration//GEN-END:variables
}
