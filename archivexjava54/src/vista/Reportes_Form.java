// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Reportes_control genera los reportes y registra las exportaciones en la tabla reportes.
import controlador.Reportes_control;
// DefaultTableModel: modelo de la JTable armado con las columnas de cada reporte.
import javax.swing.table.DefaultTableModel;
// ReporteResultado: columnas y filas devueltas por reportes.Reporte.
import reportes.ReporteResultado;

// Ventana de reportes: muestra el reporte elegido en una tabla y lo exporta a Excel.
public class Reportes_Form extends javax.swing.JFrame {
    // Tipo del último reporte mostrado; se usa como título al exportar (null si la tabla está vacía).
    private String tipoGenerado;

    // Constructor: arma la ventana, carga los tipos de reporte y deja la tabla vacía.
    public Reportes_Form() {
        // Crea y organiza los componentes diseñados en el archivo .form.
        initComponents();
        // Pone el logo de ArchiveX como ícono de la ventana y junto al título.
        RecursosApp.aplicarLogo(this, jLabelTitulo);
        // Texto de la barra de título.
        this.setTitle("ArchiveX - Reportes");

        // Llena el combo con los tipos de reporte disponibles.
        cargarTiposDeReporte();
        // Empieza con la tabla vacía.
        limpiarTabla();

        // Aplica los colores y fuentes del tema oscuro a los componentes del panel.
        TemaColores.aplicarTema(jPanel1);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableReportes, jScrollPaneReportes);
        // Cerrar con la X equivale a "Volver al Menú".
        RecursosApp.cerrarConBoton(this, jButtonVolver);
        // Ajusta la ventana al tamaño preferido de sus componentes.
        this.pack();
        // Tamaño inicial 1100x700 (antes el de pack(): 784x491), limitado al área útil de la pantalla.
        RecursosApp.ajustarVentana(this, 1100, 700, 860, 560);
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
        jLabelTipoReporte = new javax.swing.JLabel();
        jComboBoxTipoReporte = new javax.swing.JComboBox<>();
        jPanelBotones = new javax.swing.JPanel();
        jButtonGenerar = new javax.swing.JButton();
        jButtonActualizar = new javax.swing.JButton();
        jButtonLimpiar = new javax.swing.JButton();
        jButtonExportarExcel = new javax.swing.JButton();
        jButtonVolver = new javax.swing.JButton();
        jScrollPaneReportes = new javax.swing.JScrollPane();
        jTableReportes = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(11, 18, 32));

        jPanelTitulo.setBackground(new java.awt.Color(15, 23, 42));

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Reportes");

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

        jLabelTipoReporte.setText("Tipo de reporte:");

        jPanelFormulario.setBackground(new java.awt.Color(11, 18, 32));

        javax.swing.GroupLayout jPanelFormularioLayout = new javax.swing.GroupLayout(jPanelFormulario);
        jPanelFormulario.setLayout(jPanelFormularioLayout);
        jPanelFormularioLayout.setHorizontalGroup(
            jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabelTipoReporte)
                .addGap(10, 10, 10)
                .addComponent(jComboBoxTipoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelFormularioLayout.setVerticalGroup(
            jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFormularioLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTipoReporte)
                    .addComponent(jComboBoxTipoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jButtonGenerar.setBackground(new java.awt.Color(237, 149, 62));
        jButtonGenerar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGenerar.setText("Generar");
        jButtonGenerar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerarActionPerformed(evt);
            }
        });

        jButtonActualizar.setBackground(new java.awt.Color(15, 23, 42));
        jButtonActualizar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonActualizar.setText("Actualizar");
        jButtonActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonActualizarActionPerformed(evt);
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
                .addComponent(jButtonGenerar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonActualizar)
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
                    .addComponent(jButtonGenerar)
                    .addComponent(jButtonActualizar)
                    .addComponent(jButtonLimpiar)
                    .addComponent(jButtonExportarExcel)
                    .addComponent(jButtonVolver))
                .addContainerGap())
        );

        jScrollPaneReportes.setViewportView(jTableReportes);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneReportes, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanelBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPaneReportes, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
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

    // Botón Generar: consulta el reporte seleccionado y lo muestra.
    private void jButtonGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerarActionPerformed
        // Ejecuta la consulta del tipo elegido.
        generarReporteSeleccionado();
    }//GEN-LAST:event_jButtonGenerarActionPerformed

    // Botón Actualizar: vuelve a consultar para ver los datos más recientes.
    private void jButtonActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonActualizarActionPerformed
        // Repite la consulta del tipo elegido.
        generarReporteSeleccionado();
    }//GEN-LAST:event_jButtonActualizarActionPerformed

    // Botón Limpiar: vacía la tabla.
    private void jButtonLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimpiarActionPerformed
        // Quita columnas y filas.
        limpiarTabla();
    }//GEN-LAST:event_jButtonLimpiarActionPerformed

    // Botón Exportar Excel: guarda la tabla en un .xls y lo registra en la tabla reportes.
    private void jButtonExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportarExcelActionPerformed

        // Sin columnas no hay reporte generado.
        if (jTableReportes.getColumnCount() == 0) {
            // Avisa que primero debe generarse un reporte.
            javax.swing.JOptionPane.showMessageDialog(this, "Genere un reporte antes de exportarlo",
                    "Nada para exportar", javax.swing.JOptionPane.WARNING_MESSAGE);
            // No exporta nada.
            return;
        }

        // Exporta con ExportarExcel y guarda título, tipo, ruta y estado con el usuario de la sesión.
        Reportes_control.exportarYRegistrar(jTableReportes, tipoGenerado);
    }//GEN-LAST:event_jButtonExportarExcelActionPerformed

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

    // Carga en el combo los nombres de reporte definidos en Reportes_control.
    private void cargarTiposDeReporte() {
        // Quita los elementos del diseño.
        jComboBoxTipoReporte.removeAllItems();
        // Recorre los tipos disponibles.
        for (String tipo : Reportes_control.getTiposDeReporte()) {
            // Agrega cada tipo al combo.
            jComboBoxTipoReporte.addItem(tipo);
        }
    }

    // Consulta el reporte del combo y reemplaza el modelo de la tabla con sus columnas y filas.
    private void generarReporteSeleccionado() {
        // Tipo elegido en el combo.
        String tipoSeleccionado = (String) jComboBoxTipoReporte.getSelectedItem();
        // Combo vacío.
        if (tipoSeleccionado == null) {
            // No hay nada que consultar.
            return;
        }

        // Ejecuta las consultas SQL del reporte; devuelve null si hubo error (ya informado).
        ReporteResultado resultado = Reportes_control.generarReporte(tipoSeleccionado);
        // Error o sin conexión.
        if (resultado == null) {
            // La tabla conserva lo que tenía.
            return;
        }

        // Modelo nuevo con las columnas del reporte y sin filas.
        DefaultTableModel modelo = new DefaultTableModel(resultado.getColumnas(), 0) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Recorre las filas del resultado.
        for (Object[] fila : resultado.getFilas()) {
            // Agrega cada fila al modelo.
            modelo.addRow(fila);
        }
        // Muestra el reporte en la tabla.
        jTableReportes.setModel(modelo);
        // Aplica el estilo oscuro a la tabla y a su scroll.
        TemaColores.aplicarEstiloTabla(jTableReportes, jScrollPaneReportes);
        // Recuerda el tipo mostrado para usarlo al exportar.
        tipoGenerado = tipoSeleccionado;
    }

    // Deja la tabla sin columnas ni filas.
    private void limpiarTabla() {
        // Modelo vacío.
        DefaultTableModel modelo = new DefaultTableModel(new Object[][]{}, new String[]{}) {
            // Las celdas son de solo lectura; los datos se editan en los campos del formulario.
            @Override
            public boolean isCellEditable(int row, int column) {
                // Ninguna celda es editable.
                return false;
            }
        };
        // Asigna el modelo vacío a la tabla.
        jTableReportes.setModel(modelo);
        // Ya no hay reporte que exportar.
        tipoGenerado = null;
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
            java.util.logging.Logger.getLogger(Reportes_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // No se pudo crear el Look and Feel.
        } catch (InstantiationException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Reportes_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // Sin permiso para instanciar el Look and Feel.
        } catch (IllegalAccessException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Reportes_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        // El sistema operativo no soporta Nimbus.
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            // Registra el error y la ventana se abre con el aspecto por defecto.
            java.util.logging.Logger.getLogger(Reportes_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Crea la ventana en el hilo de eventos de Swing (EDT).
        java.awt.EventQueue.invokeLater(new Runnable() {
            // Código que ejecuta el hilo de eventos.
            public void run() {
                // Crea y muestra la ventana.
                new Reportes_Form().setVisible(true);
            }
        });
    }

    // Componentes Swing declarados por NetBeans según el .form (bloque protegido, no editar).
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonActualizar;
    private javax.swing.JButton jButtonExportarExcel;
    private javax.swing.JButton jButtonGenerar;
    private javax.swing.JButton jButtonLimpiar;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JComboBox<String> jComboBoxTipoReporte;
    private javax.swing.JLabel jLabelTipoReporte;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelBotones;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPaneReportes;
    private javax.swing.JTable jTableReportes;
    // End of variables declaration//GEN-END:variables
}
