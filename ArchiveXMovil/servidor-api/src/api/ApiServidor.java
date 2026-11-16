// Paquete api: lanzador único de todas las APIs REST de ArchiveX.
package api;

import java.io.IOException;

// Arranca en un solo proceso los siete servidores HTTP de ArchiveX para que la app móvil
// ArchiveX Móvil (o Postman) pueda consumirlos sin ejecutar cada clase por separado.
// En NetBeans: clic derecho sobre este archivo > Run File (Shift + F6).
public class ApiServidor {

    public static void main(String[] args) throws IOException {
        UsuarioApiServer.main(args);     // 8080 /usuarios  (incluye POST /usuarios/login)
        ProductoApiServer.main(args);    // 8081 /productos
        CategoriaApiServer.main(args);   // 8082 /categorias
        ProveedorApiServer.main(args);   // 8083 /proveedores
        AlmacenApiServer.main(args);     // 8084 /almacenes
        CompraApiServer.main(args);      // 8085 /compras   (solo lectura)
        MovimientoApiServer.main(args);  // 8086 /movimientos (GET y POST)
        System.out.println("ArchiveX: las 7 APIs están activas. Desde el emulador use http://10.0.2.2:<puerto>");
    }
}
