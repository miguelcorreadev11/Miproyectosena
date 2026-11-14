package com.archivex.movil.data.api;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Cliente HTTP único de la aplicación (patrón Singleton).
 * Ejecuta las peticiones en un hilo de fondo y entrega el resultado en el hilo principal,
 * porque Android no permite operaciones de red en el hilo de la interfaz.
 */
public final class ApiClient {

    /** Trabajo que se ejecuta en segundo plano y puede fallar. */
    public interface Tarea<T> {
        T ejecutar() throws Exception;
    }

    private static ApiClient instancia;

    private final ExecutorService hilos = Executors.newFixedThreadPool(3);
    private final Handler hiloPrincipal = new Handler(Looper.getMainLooper());

    private ApiClient() {
    }

    public static synchronized ApiClient getInstancia() {
        if (instancia == null) {
            instancia = new ApiClient();
        }
        return instancia;
    }

    /** Ejecuta la tarea en segundo plano y avisa el resultado al callback en el hilo principal. */
    public <T> void enSegundoPlano(Tarea<T> tarea, Resultado<T> callback) {
        hilos.execute(() -> {
            try {
                T datos = tarea.ejecutar();
                hiloPrincipal.post(() -> callback.onExito(datos));
            } catch (Exception e) {
                String mensaje = traducirError(e);
                hiloPrincipal.post(() -> callback.onError(mensaje));
            }
        });
    }

    public String get(String url) throws ApiException {
        return enviar("GET", url, null);
    }

    public String post(String url, String json) throws ApiException {
        return enviar("POST", url, json);
    }

    public String put(String url, String json) throws ApiException {
        return enviar("PUT", url, json);
    }

    public String delete(String url) throws ApiException {
        return enviar("DELETE", url, null);
    }

    /** Envía la petición HTTP y devuelve el cuerpo de la respuesta, o lanza ApiException si falla. */
    private String enviar(String metodo, String url, String cuerpoJson) throws ApiException {
        HttpURLConnection conexion = null;
        try {
            conexion = (HttpURLConnection) new URL(url).openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setConnectTimeout(ApiConfig.TIEMPO_ESPERA_MS);
            conexion.setReadTimeout(ApiConfig.TIEMPO_ESPERA_MS);
            conexion.setRequestProperty("Accept", "application/json");
            if (cuerpoJson != null) {
                byte[] bytes = cuerpoJson.getBytes(StandardCharsets.UTF_8);
                conexion.setDoOutput(true);
                conexion.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conexion.setFixedLengthStreamingMode(bytes.length);
                try (OutputStream salida = conexion.getOutputStream()) {
                    salida.write(bytes);
                }
            }
            int codigo = conexion.getResponseCode();
            String respuesta = leer(codigo >= 400 ? conexion.getErrorStream() : conexion.getInputStream());
            if (codigo >= 400) {
                throw new ApiException(codigo, ApiException.extraerMensaje(codigo, respuesta));
            }
            return respuesta;
        } catch (IOException e) {
            throw new ApiException(ApiException.SIN_CONEXION,
                    "No se pudo conectar con el servidor " + ApiConfig.getHost()
                            + ". Verifique que ApiServidor esté en ejecución.");
        } finally {
            if (conexion != null) {
                conexion.disconnect();
            }
        }
    }

    private static String leer(InputStream entrada) throws IOException {
        if (entrada == null) {
            return "";
        }
        try (InputStream in = entrada) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] bloque = new byte[2048];
            int leidos;
            while ((leidos = in.read(bloque)) != -1) {
                buffer.write(bloque, 0, leidos);
            }
            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    static String traducirError(Exception e) {
        if (e instanceof ApiException) {
            return e.getMessage();
        }
        if (e instanceof JSONException) {
            return "La respuesta del servidor no tiene el formato esperado";
        }
        return "Ocurrió un error inesperado: " + e.getMessage();
    }
}
