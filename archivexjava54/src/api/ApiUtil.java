// Paquete api: utilidades comunes a todos los *ApiServer (evita repetir el mismo código en cada uno).
package api;

import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Funciones de JSON y HTTP reutilizadas por UsuarioApiServer, ProductoApiServer, etc.
public class ApiUtil {

    // Capta "clave":"texto" o "clave":123 (número, true/false/null sin comillas).
    private static final Pattern PAR_JSON = Pattern.compile(
            "\"(\\w+)\"\\s*:\\s*(?:\"((?:\\\\.|[^\"\\\\])*)\"|([^,}\\s][^,}]*))");

    // Parser mínimo: solo objetos planos {"clave":valor, ...}, alcanza para los campos de este proyecto.
    public static Map<String, String> parseJsonObject(String json) {
        Map<String, String> resultado = new LinkedHashMap<>();
        if (json == null) return resultado;
        Matcher m = PAR_JSON.matcher(json);
        while (m.find()) {
            // group(2): valor entre comillas; group(3): número o literal sin comillas.
            String valor = m.group(2) != null
                    ? m.group(2).replace("\\\"", "\"").replace("\\\\", "\\")
                    : m.group(3).trim();
            resultado.put(m.group(1), valor);
        }
        return resultado;
    }

    // Arma el JSON de un mensaje de error: {"error":"..."}.
    public static String error(String mensaje) {
        return "{\"error\":" + jsonString(mensaje) + "}";
    }

    // Escapa comillas, barras y saltos de línea para incluir el texto en un JSON válido.
    public static String jsonString(String valor) {
        if (valor == null) return "null";
        return "\"" + valor.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }

    // Igual que jsonString, pero para columnas DATETIME: un valor null queda como JSON null (sin comillas).
    public static String jsonTimestamp(Timestamp valor) {
        return valor == null ? "null" : jsonString(valor.toString());
    }

    // true si el texto es null o solo espacios (campo obligatorio no enviado).
    public static boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Lee el cuerpo completo del request (el JSON que mandó Postman) como texto UTF-8.
    public static String leerCuerpo(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] tmp = new byte[1024];
            int leidos;
            while ((leidos = is.read(tmp)) != -1) {
                buffer.write(tmp, 0, leidos);
            }
            return buffer.toString("UTF-8");
        }
    }

    // Escribe el código de estado y el cuerpo JSON de la respuesta HTTP.
    public static void responder(HttpExchange exchange, int codigo, String cuerpoJson) throws IOException {
        byte[] bytes = cuerpoJson.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
