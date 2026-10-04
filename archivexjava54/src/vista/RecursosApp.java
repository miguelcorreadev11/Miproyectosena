// Paquete vista: ventanas Swing (JFrame con .form de NetBeans) y utilidades visuales.
package vista;

// Dimension, GraphicsEnvironment y Rectangle: tamaño de la ventana según el área útil de la pantalla.
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
// Image: imágenes base cargadas desde el classpath.
import java.awt.Image;
// Eventos para cerrar la ventana con la X.
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
// URL: ubicación del recurso dentro de src/imagenes (o del .jar).
import java.net.URL;
// ArrayList para la lista de íconos de ventana.
import java.util.ArrayList;
// List: tipo que recibe JFrame.setIconImages.
import java.util.List;
// ImageIcon carga y envuelve las imágenes para Swing.
import javax.swing.ImageIcon;
// JButton: botón "Volver" que se ejecuta al cerrar la ventana con la X.
import javax.swing.JButton;
// JFrame: ventana a la que se le pone el ícono.
import javax.swing.JFrame;
// JLabel: etiqueta de título o avatar que recibe la imagen.
import javax.swing.JLabel;
// SwingConstants: alineaciones del texto y la imagen.
import javax.swing.SwingConstants;

// Recursos gráficos compartidos (logo, avatar y fondo del menú); clase de utilidades sin estado.
public final class RecursosApp {
    // Constructor privado: la clase no se instancia.
    private RecursosApp() {
    }

    // Ruta del logo dentro del classpath (src/imagenes/ax-logo.png).
    private static final String RUTA_LOGO = "/imagenes/ax-logo.png";
    // Ruta del avatar genérico de usuario.
    private static final String RUTA_AVATAR_USUARIO = "/imagenes/masculino_ax.png";
    // Ruta de la imagen de fondo del menú y del splash.
    private static final String RUTA_FONDO_INICIO = "/imagenes/ax_inicio.png";

    // Tamaño en píxeles del logo junto al título de cada ventana.
    private static final int TAMANO_LOGO_TITULO = 36;

    // Tamaño en píxeles del avatar de usuario (88 + 1 px de borde por lado = 90 px reservados en el .form).
    private static final int TAMANO_AVATAR_USUARIO = 88;

    // Tamaños del ícono de ventana para que Windows elija el más nítido.
    private static final int[] TAMANOS_ICONO_VENTANA = {16, 20, 24, 32, 48, 64, 128};

    // Logo original cargado una sola vez al usar la clase.
    private static final Image IMAGEN_LOGO_BASE = cargarImagenBase(RUTA_LOGO);
    // Avatar original cargado una sola vez.
    private static final Image IMAGEN_AVATAR_BASE = cargarImagenBase(RUTA_AVATAR_USUARIO);

    // Fondo del menú sin escalar; PanelImagenFondo lo ajusta al tamaño del panel.
    private static final Image IMAGEN_FONDO_INICIO_BASE = cargarImagenBase(RUTA_FONDO_INICIO);

    // Logo ya escalado para los títulos de las ventanas.
    public static final ImageIcon ICONO_LOGO_TITULO = crearIconoEscalado(IMAGEN_LOGO_BASE, TAMANO_LOGO_TITULO);

    // Avatar ya escalado.
    public static final ImageIcon ICONO_AVATAR_USUARIO = crearIconoEscalado(IMAGEN_AVATAR_BASE, TAMANO_AVATAR_USUARIO);

    // Logo en varios tamaños para el ícono de las ventanas.
    private static final List<Image> ICONOS_VENTANA = crearIconosVentana();

    // Carga una imagen del classpath; null si el recurso no existe.
    private static Image cargarImagenBase(String ruta) {
        // Busca el recurso relativo a la raíz del classpath (build/classes o el .jar).
        URL recurso = RecursosApp.class.getResource(ruta);
        // El archivo no está en el proyecto compilado.
        if (recurso == null) {
            // Sin imagen.
            return null;
        }
        // Carga la imagen de forma síncrona con ImageIcon.
        return new ImageIcon(recurso).getImage();
    }

    // Escala la imagen a un cuadrado del tamaño indicado; null si no hay imagen base.
    private static ImageIcon crearIconoEscalado(Image imagenBase, int tamano) {
        // Sin imagen base no hay nada que escalar.
        if (imagenBase == null) {
            // Sin ícono.
            return null;
        }
        // Escala con suavizado.
        Image escalada = imagenBase.getScaledInstance(tamano, tamano, Image.SCALE_SMOOTH);
        // Envuelve la imagen escalada en un ImageIcon.
        return new ImageIcon(escalada);
    }

    // Genera el logo en cada tamaño de TAMANOS_ICONO_VENTANA.
    private static List<Image> crearIconosVentana() {
        // Lista resultado.
        List<Image> iconos = new ArrayList<Image>();
        // Sin logo, la lista queda vacía y la ventana conserva el ícono de Java.
        if (IMAGEN_LOGO_BASE == null) {
            // Devuelve la lista vacía.
            return iconos;
        }
        // Recorre los tamaños definidos.
        for (int tamano : TAMANOS_ICONO_VENTANA) {
            // Agrega el logo escalado a ese tamaño.
            iconos.add(IMAGEN_LOGO_BASE.getScaledInstance(tamano, tamano, Image.SCALE_SMOOTH));
        }
        // Devuelve los íconos.
        return iconos;
    }

    // Imagen de fondo del menú en resolución original, o null si no está disponible.
    public static Image obtenerImagenFondoInicio() {
        // La usa PanelImagenFondo.paintComponent.
        return IMAGEN_FONDO_INICIO_BASE;
    }

    // Pone el logo como ícono de la ventana y junto al texto del título; lo llaman todos los formularios.
    public static void aplicarLogo(JFrame ventana, JLabel labelTitulo) {
        // Solo si se cargó el logo.
        if (!ICONOS_VENTANA.isEmpty()) {
            // Ícono en la barra de título y en la barra de tareas.
            ventana.setIconImages(ICONOS_VENTANA);
        }
        // Solo si hay etiqueta de título y logo disponible.
        if (labelTitulo != null && ICONO_LOGO_TITULO != null) {
            // Coloca el logo en la etiqueta del título.
            labelTitulo.setIcon(ICONO_LOGO_TITULO);
            // Separación de 10 px entre logo y texto.
            labelTitulo.setIconTextGap(10);
            // El texto queda a la derecha del logo.
            labelTitulo.setHorizontalTextPosition(SwingConstants.RIGHT);
            // El conjunto se alinea a la izquierda.
            labelTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        }
    }

    // Muestra el avatar genérico en la etiqueta indicada, con borde del tema.
    public static void aplicarAvatarUsuario(JLabel labelAvatar) {
        // Sin etiqueta no hay nada que hacer.
        if (labelAvatar == null) {
            // Termina sin cambios.
            return;
        }
        // Solo si el avatar se cargó.
        if (ICONO_AVATAR_USUARIO != null) {
            // Coloca el avatar.
            labelAvatar.setIcon(ICONO_AVATAR_USUARIO);
        }
        // Centra horizontalmente.
        labelAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        // Centra verticalmente.
        labelAvatar.setVerticalAlignment(SwingConstants.CENTER);
        // Borde de 1 px con el color del tema.
        labelAvatar.setBorder(javax.swing.BorderFactory.createLineBorder(TemaColores.BORDE, 1));
    }

    // Fija el tamaño inicial y mínimo de la ventana sin superar el área útil de la pantalla
    // (descontando la barra de tareas) y sin quedar por debajo del tamaño preferido del contenido.
    public static void ajustarVentana(JFrame ventana, int ancho, int alto, int anchoMinimo, int altoMinimo) {
        // Área disponible del monitor principal.
        Rectangle area = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        // Tamaño que necesitan los componentes según el layout del .form.
        Dimension preferido = ventana.getPreferredSize();
        // Tamaño mínimo para que el usuario no pueda encoger la ventana hasta cortar componentes.
        ventana.setMinimumSize(new Dimension(Math.min(anchoMinimo, area.width), Math.min(altoMinimo, area.height)));
        // Tamaño inicial: el indicado o el preferido si es mayor, siempre dentro de la pantalla.
        ventana.setSize(Math.min(Math.max(ancho, preferido.width), area.width),
                Math.min(Math.max(alto, preferido.height), area.height));
        // Centra la ventana.
        ventana.setLocationRelativeTo(null);
    }

    // Hace que cerrar la ventana con la X ejecute la acción del botón indicado (por ejemplo,
    // "Volver al Menú"); así la aplicación no termina sin regresar al menú principal.
    public static void cerrarConBoton(JFrame ventana, final JButton boton) {
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (ActionListener oyente : boton.getActionListeners()) {
                    oyente.actionPerformed(new ActionEvent(boton, ActionEvent.ACTION_PERFORMED,
                            boton.getActionCommand()));
                }
            }
        });
    }
}
