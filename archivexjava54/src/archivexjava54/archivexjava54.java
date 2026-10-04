// Paquete de la clase principal configurada en project.properties (main.class).
package archivexjava54;

// Timer de Swing: ejecuta una acción en el hilo de eventos después de un retardo, sin bloquearlo.
import javax.swing.Timer;
// Ventana de inicio de sesión que se abre al terminar el splash.
import vista.Login_Form;
// Ventana sin bordes que muestra la imagen de bienvenida ax_inicio.png.
import vista.SplashScreen;

// Clase de arranque de ArchiveX: muestra el splash y luego abre el login.
public class archivexjava54 {
    // Punto de entrada del programa que ejecuta NetBeans al presionar Run.
    public static void main(String[] args) {
        // Toda la creación de ventanas Swing se programa en el hilo de eventos (EDT).
        java.awt.EventQueue.invokeLater(() -> {
            // Fuente más legible en los cuadros de diálogo (JOptionPane) de todo el sistema.
            vista.TemaColores.aplicarFuentesGlobales();
            // Crea el splash con la imagen escalada que prepara SplashScreen.
            SplashScreen splash = new SplashScreen();
            // Muestra el splash centrado en pantalla.
            splash.setVisible(true);

            // Programa una única acción a los 1800 ms para cerrar el splash y continuar el flujo.
            Timer timer = new Timer(1800, e -> {
                // Cierra y libera la ventana del splash.
                splash.dispose();
                // Abre Login_Form, primera ventana interactiva del sistema.
                new Login_Form().setVisible(true);
            });
            // El Timer se ejecuta una sola vez.
            timer.setRepeats(false);
            // Inicia la cuenta regresiva del Timer.
            timer.start();
        });
    }
}
