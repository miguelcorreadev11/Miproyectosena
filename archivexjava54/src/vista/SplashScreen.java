// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Dimension: tamaño de la ventana del splash.
import java.awt.Dimension;
// Image: imagen original y escalada.
import java.awt.Image;
// URL: ubicación de la imagen dentro del classpath.
import java.net.URL;
// ImageIcon carga la imagen y la muestra en el JLabel.
import javax.swing.ImageIcon;
// JLabel que contiene la imagen.
import javax.swing.JLabel;
// JWindow: ventana sin bordes ni barra de título.
import javax.swing.JWindow;
// SwingConstants: alineación de la imagen.
import javax.swing.SwingConstants;

// Pantalla de bienvenida sin bordes; archivexjava54.main la muestra 1,8 s y luego abre el login.
public class SplashScreen extends JWindow {
    // Ruta de la imagen dentro de src/imagenes.
    private static final String RUTA_IMAGEN = "/imagenes/ax_inicio.png";

    // Ancho original de ax_inicio.png, usado para conservar la proporción.
    private static final int ANCHO_ORIGINAL = 1677;
    // Alto original de ax_inicio.png.
    private static final int ALTO_ORIGINAL = 938;

    // Ancho fijo del splash en pantalla.
    private static final int ANCHO_SPLASH = 640;
    // Alto calculado con la proporción de la imagen para no deformarla (358 px).
    private static final int ALTO_SPLASH = Math.round(
            ANCHO_SPLASH * ((float) ALTO_ORIGINAL / ANCHO_ORIGINAL));

    // Arma la ventana con la imagen escalada y la centra en pantalla.
    public SplashScreen() {
        // Etiqueta que contendrá la imagen.
        JLabel labelImagen = new JLabel();
        // Centra la imagen horizontalmente.
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);

        // Busca la imagen en el classpath.
        URL recurso = getClass().getResource(RUTA_IMAGEN);
        // Solo si el recurso existe.
        if (recurso != null) {
            // Carga la imagen original.
            Image imagenOriginal = new ImageIcon(recurso).getImage();
            // Escala la imagen al tamaño del splash con suavizado.
            Image imagenEscalada = imagenOriginal.getScaledInstance(
                    ANCHO_SPLASH, ALTO_SPLASH, Image.SCALE_SMOOTH);
            // Muestra la imagen escalada.
            labelImagen.setIcon(new ImageIcon(imagenEscalada));
        }

        // Agrega la etiqueta a la ventana.
        getContentPane().add(labelImagen);
        // Fija el tamaño de la ventana.
        setSize(new Dimension(ANCHO_SPLASH, ALTO_SPLASH));
        // Centra la ventana en la pantalla.
        setLocationRelativeTo(null);
    }
}
