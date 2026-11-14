package com.archivex.movil.data.api;

import com.archivex.movil.util.Validador;

/**
 * Configuración de acceso a la API REST de ArchiveX.
 * Cada recurso se publica en un puerto distinto del servidor (ver api.ApiServidor en el proyecto de escritorio).
 * Desde el emulador de Android, la dirección 10.0.2.2 apunta al localhost del computador.
 */
public final class ApiConfig {

    /** Dirección por defecto: el computador anfitrión visto desde el emulador. */
    public static final String HOST_POR_DEFECTO = "10.0.2.2";

    /** Tiempo máximo de conexión y de lectura, en milisegundos. */
    public static final int TIEMPO_ESPERA_MS = 8000;

    /** Recursos REST de ArchiveX con su puerto y su ruta. */
    public enum Recurso {
        USUARIOS(8080, "usuarios"),
        PRODUCTOS(8081, "productos"),
        CATEGORIAS(8082, "categorias"),
        PROVEEDORES(8083, "proveedores"),
        ALMACENES(8084, "almacenes"),
        COMPRAS(8085, "compras"),
        MOVIMIENTOS(8086, "movimientos");

        private final int puerto;
        private final String ruta;

        Recurso(int puerto, String ruta) {
            this.puerto = puerto;
            this.ruta = ruta;
        }

        public int getPuerto() {
            return puerto;
        }

        public String getRuta() {
            return ruta;
        }
    }

    private static String host = HOST_POR_DEFECTO;

    private ApiConfig() {
    }

    public static synchronized String getHost() {
        return host;
    }

    /**
     * Cambia el servidor (por ejemplo, la IP del computador en la red Wi-Fi para un teléfono real).
     *
     * @return true si la dirección es válida y se aplicó.
     */
    public static synchronized boolean setHost(String nuevoHost) {
        if (!Validador.esHostValido(nuevoHost)) {
            return false;
        }
        host = nuevoHost.trim();
        return true;
    }

    /** URL de la colección, por ejemplo http://10.0.2.2:8081/productos */
    public static String url(Recurso recurso) {
        return "http://" + getHost() + ":" + recurso.getPuerto() + "/" + recurso.getRuta();
    }

    /** URL de un registro, por ejemplo http://10.0.2.2:8081/productos/3 */
    public static String url(Recurso recurso, int id) {
        return url(recurso) + "/" + id;
    }

    /** URL de una acción, por ejemplo http://10.0.2.2:8080/usuarios/login */
    public static String url(Recurso recurso, String accion) {
        return url(recurso) + "/" + accion;
    }
}
