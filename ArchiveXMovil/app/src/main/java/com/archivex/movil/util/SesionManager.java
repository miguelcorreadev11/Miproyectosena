package com.archivex.movil.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.archivex.movil.data.api.ApiConfig;
import com.archivex.movil.modelo.Usuario;

/**
 * Guarda la sesión del usuario y la dirección del servidor en SharedPreferences (patrón Singleton).
 * Nunca se guarda la contraseña.
 */
public final class SesionManager {

    private static final String ARCHIVO = "archivex_sesion";
    private static final String CLAVE_ID = "id_usuario";
    private static final String CLAVE_NOMBRE = "nombre";
    private static final String CLAVE_APELLIDO = "apellido";
    private static final String CLAVE_CORREO = "correo";
    private static final String CLAVE_ROL = "rol";
    private static final String CLAVE_HOST = "host_servidor";

    private static SesionManager instancia;

    private final SharedPreferences preferencias;

    private SesionManager(Context contexto) {
        preferencias = contexto.getApplicationContext().getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        ApiConfig.setHost(preferencias.getString(CLAVE_HOST, ApiConfig.HOST_POR_DEFECTO));
    }

    public static synchronized SesionManager getInstancia(Context contexto) {
        if (instancia == null) {
            instancia = new SesionManager(contexto);
        }
        return instancia;
    }

    public void iniciarSesion(Usuario usuario) {
        preferencias.edit()
                .putInt(CLAVE_ID, usuario.getIdUsuario())
                .putString(CLAVE_NOMBRE, usuario.getNombre())
                .putString(CLAVE_APELLIDO, usuario.getApellido())
                .putString(CLAVE_CORREO, usuario.getCorreoElectronico())
                .putString(CLAVE_ROL, usuario.getRol())
                .apply();
    }

    public boolean haySesion() {
        return preferencias.getInt(CLAVE_ID, 0) > 0;
    }

    /** Usuario de la sesión actual, o null si nadie ha iniciado sesión. */
    public Usuario getUsuario() {
        if (!haySesion()) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(preferencias.getInt(CLAVE_ID, 0));
        usuario.setNombre(preferencias.getString(CLAVE_NOMBRE, ""));
        usuario.setApellido(preferencias.getString(CLAVE_APELLIDO, ""));
        usuario.setCorreoElectronico(preferencias.getString(CLAVE_CORREO, ""));
        usuario.setRol(preferencias.getString(CLAVE_ROL, Usuario.ROL_USUARIO));
        return usuario;
    }

    public void cerrarSesion() {
        preferencias.edit()
                .remove(CLAVE_ID).remove(CLAVE_NOMBRE).remove(CLAVE_APELLIDO)
                .remove(CLAVE_CORREO).remove(CLAVE_ROL)
                .apply();
    }

    public String getHost() {
        return ApiConfig.getHost();
    }

    /** Cambia y guarda la dirección del servidor. Devuelve false si no es válida. */
    public boolean guardarHost(String host) {
        if (!ApiConfig.setHost(host)) {
            return false;
        }
        preferencias.edit().putString(CLAVE_HOST, ApiConfig.getHost()).apply();
        return true;
    }
}
