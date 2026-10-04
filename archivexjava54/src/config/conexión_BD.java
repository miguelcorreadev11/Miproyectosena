// Paquete de configuración: contiene la única clase de conexión JDBC que usan todos los modelos.
package config;

// Connection representa la sesión abierta con MySQL que comparten modelo.* y reportes.Reporte.
import java.sql.Connection;
// DriverManager crea la conexión a partir de la URL JDBC de MySQL Connector/J 8.3.0.
import java.sql.DriverManager;
// SQLException se captura al validar si la conexión compartida sigue abierta.
import java.sql.SQLException;

// Punto central de acceso a MySQL: todos los modelos llaman a conexión_BD.getConnection()
// y reutilizan la misma conexión estática en lugar de abrir una por consulta.
public class conexión_BD {
    // Nombre de la base de datos del proyecto (script sql/archivex.sql).
    private static String dbname = "archivex";
    // Usuario de MySQL con el que se autentica la aplicación.
    private static String username = "root";
    // Contraseña del usuario de MySQL; debe coincidir con la configurada en el servidor local.
    private static String password = "12540316";

    // Mensaje que los modelos muestran en un JOptionPane cuando getConnection() devuelve null,
    // es decir, cuando MySQL está apagado, la BD no existe o las credenciales fallan.
    public static final String MENSAJE_SIN_CONEXION =
            "No se pudo establecer conexión con la base de datos 'archivex'.\n"
            + "Verifique que el servidor MySQL esté encendido, que la base de datos exista "
            + "y que el usuario/contraseña configurados en config.conexión_BD sean correctos.";

       // Conexión compartida por toda la aplicación; null hasta el primer getConnection() o tras un fallo.
       static Connection con=null;

    // true si la última operación con MySQL falló (sin conexión o SQLException) y el error ya se informó;
    // así un false/null del modelo no se confunde con "registro no encontrado" o con un dato inválido.
    private static boolean errorEnUltimaOperacion = false;

    // Lo llaman los catch de los modelos cuando MySQL rechaza o no puede ejecutar una sentencia.
    public static void registrarError() {
        errorEnUltimaOperacion = true;
    }

    // Lo consultan los controladores antes de mostrar avisos como "no existe" o "ya no coincide".
    public static boolean huboErrorEnUltimaOperacion() {
        return errorEnUltimaOperacion;
    }

    // Devuelve la conexión compartida, reabriéndola si está cerrada o dejó de responder.
    public static Connection getConnection()
    {
        // Cada operación de un modelo empieza aquí: se descarta el error de la operación anterior.
        errorEnUltimaOperacion = false;
        // Si ya existe una conexión, se comprueba antes de reutilizarla.
        if (con != null) {
            try {
                // isClosed() e isValid(2) confirman en máximo 2 segundos que MySQL sigue respondiendo.
                if (!con.isClosed() && con.isValid(2)) {
                    // La conexión sigue viva: se devuelve sin abrir otra.
                    return con;
                }
            // Si la verificación falla, se ignora y se continúa para abrir una conexión nueva.
            } catch (SQLException e) {
            }
        }

        // Abre una conexión nueva con las credenciales configuradas arriba.
        return getConnection(dbname, username, password);
    }

    // Crea la conexión física con MySQL; devuelve null si no es posible conectarse.
    private static Connection getConnection(String db_name,String user_name,String password)
    {
        try
        {
            // Carga el driver de MySQL Connector/J (librería Mysql_connector_8.3.0 del proyecto).
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Conecta a localhost:3306 con usuario y contraseña; useSSL=false y allowPublicKeyRetrieval
            // evitan errores de MySQL 8 local y serverTimezone=UTC fija la zona horaria de la sesión JDBC.
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db_name
                    +"?user="+user_name+"&password="+password
                    +"&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
            // Traza en la consola de NetBeans que la conexión se abrió correctamente.
            System.out.println("connected");
        }
        // Cualquier fallo (driver ausente, MySQL apagado, credenciales) deja la conexión en null.
        catch(Exception e)
        {
            // Marca la conexión como inexistente para que los modelos muestren MENSAJE_SIN_CONEXION.
            con = null;
            // El fallo de conexión cuenta como error de la operación en curso.
            errorEnUltimaOperacion = true;
            // Escribe el motivo del fallo en la salida de errores para diagnosticar la conexión.
            System.err.println("No fue posible conectar a la base de datos '" + db_name + "': " + e.getMessage());
            // Imprime la traza completa de la excepción en la consola.
            e.printStackTrace();
        }

        // Devuelve la conexión creada o null si no se pudo abrir.
        return con;
    }

}
