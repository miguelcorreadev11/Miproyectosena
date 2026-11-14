package com.archivex.movil.data.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Error devuelto por la API (código HTTP 4xx o 5xx) o por la conexión.
 * Guarda el código para que la interfaz pueda mostrar un mensaje adecuado.
 */
public class ApiException extends Exception {

    /** Código usado cuando no hubo respuesta del servidor. */
    public static final int SIN_CONEXION = -1;

    private final int codigo;

    public ApiException(int codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public boolean esSinConexion() {
        return codigo == SIN_CONEXION;
    }

    /**
     * La API de ArchiveX responde los errores como {"error":"mensaje"}.
     * Si el cuerpo no tiene ese formato, se devuelve un texto genérico según el código.
     */
    public static String extraerMensaje(int codigo, String cuerpo) {
        if (cuerpo != null && !cuerpo.trim().isEmpty()) {
            try {
                JSONObject json = new JSONObject(cuerpo);
                String error = json.optString("error", "");
                if (!error.isEmpty()) {
                    return error;
                }
            } catch (JSONException ignorada) {
                // El cuerpo no era JSON: se usa el mensaje genérico.
            }
        }
        return mensajeGenerico(codigo);
    }

    static String mensajeGenerico(int codigo) {
        switch (codigo) {
            case 400: return "Los datos enviados no son válidos";
            case 401: return "Credenciales incorrectas";
            case 403: return "Acceso denegado";
            case 404: return "El registro no existe";
            case 409: return "La operación entra en conflicto con los datos actuales";
            case 503: return "El servidor no tiene conexión con la base de datos";
            default: return "Error del servidor (código " + codigo + ")";
        }
    }
}
