// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Graphics: contexto de dibujo que entrega Swing.
import java.awt.Graphics;
// Graphics2D permite ajustar la calidad del escalado.
import java.awt.Graphics2D;
// Image: imagen de fondo que se dibuja.
import java.awt.Image;
// RenderingHints: opciones de interpolación y calidad.
import java.awt.RenderingHints;

// JPanel que dibuja ax_inicio.png ocupando todo el panel; se usa en Home_Form (jPanelFondoInicio).
public class PanelImagenFondo extends javax.swing.JPanel {
    // Constructor usado por el código generado de Home_Form.
    public PanelImagenFondo() {
        // El panel pinta todo su fondo.
        setOpaque(true);
        // Color de respaldo si la imagen no está disponible.
        setBackground(TemaColores.FONDO_PRINCIPAL);
    }

    // Redibuja el panel con la imagen escalada cada vez que cambia de tamaño.
    @Override
    protected void paintComponent(Graphics g) {
        // Pinta primero el fondo por defecto del panel.
        super.paintComponent(g);

        // Obtiene la imagen original de RecursosApp.
        Image imagenFondo = RecursosApp.obtenerImagenFondoInicio();
        // Si no se encontró la imagen, queda solo el color de fondo.
        if (imagenFondo == null) {
            // Termina sin dibujar la imagen.
            return;
        }

        // Copia del contexto para no alterar el Graphics original de Swing.
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Interpolación bilineal para un escalado suave.
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            // Prioriza la calidad sobre la velocidad.
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            // Dibuja la imagen estirada al ancho y alto actuales del panel.
            g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        // Se ejecuta siempre para liberar el contexto copiado.
        } finally {
            // Libera los recursos del contexto.
            g2d.dispose();
        }
    }
}
