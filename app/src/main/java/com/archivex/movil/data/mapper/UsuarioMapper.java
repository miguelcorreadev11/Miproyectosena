package com.archivex.movil.data.mapper;

import com.archivex.movil.modelo.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import static com.archivex.movil.data.mapper.JsonMapper.poner;
import static com.archivex.movil.data.mapper.JsonMapper.texto;

public class UsuarioMapper implements JsonMapper<Usuario> {

    @Override
    public Usuario desdeJson(JSONObject json) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(json.optInt("idUsuario"));
        usuario.setNombre(texto(json, "nombre"));
        usuario.setApellido(texto(json, "apellido"));
        usuario.setCorreoElectronico(texto(json, "correoElectronico"));
        usuario.setTelefono(texto(json, "telefono"));
        usuario.setEstado(texto(json, "estado"));
        usuario.setRol(texto(json, "rol"));
        usuario.setFechaCreacion(texto(json, "fechaCreacion"));
        return usuario;
    }

    @Override
    public JSONObject aJson(Usuario usuario) throws JSONException {
        JSONObject json = new JSONObject();
        poner(json, "nombre", usuario.getNombre());
        poner(json, "apellido", usuario.getApellido());
        poner(json, "correoElectronico", usuario.getCorreoElectronico());
        poner(json, "telefono", usuario.getTelefono() == null ? "" : usuario.getTelefono());
        poner(json, "estado", usuario.getEstado());
        poner(json, "rol", usuario.getRol());
        return json;
    }

    /** Cuerpo de POST /usuarios: los datos del usuario más la contraseña en texto (la API calcula el hash). */
    public JSONObject aJsonRegistro(Usuario usuario, String contrasena) throws JSONException {
        JSONObject json = aJson(usuario);
        json.put("contrasena", contrasena);
        return json;
    }

    /** Cuerpo de POST /usuarios/login. */
    public static JSONObject credenciales(String correo, String contrasena) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("correoElectronico", correo.trim());
        json.put("contrasena", contrasena);
        return json;
    }
}
