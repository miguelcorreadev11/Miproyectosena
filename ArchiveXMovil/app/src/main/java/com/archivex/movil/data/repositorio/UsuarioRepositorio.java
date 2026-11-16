package com.archivex.movil.data.repositorio;

import com.archivex.movil.data.api.ApiConfig;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.mapper.UsuarioMapper;
import com.archivex.movil.modelo.Usuario;

import org.json.JSONObject;

/** Repositorio de usuarios con las operaciones propias de autenticación. */
public class UsuarioRepositorio extends Repositorio<Usuario> {

    private final UsuarioMapper usuarioMapper;

    public UsuarioRepositorio() {
        this(new UsuarioMapper());
    }

    private UsuarioRepositorio(UsuarioMapper mapper) {
        super(ApiConfig.Recurso.USUARIOS, mapper);
        this.usuarioMapper = mapper;
    }

    /** POST /usuarios/login: devuelve el usuario si el correo y la contraseña coinciden. */
    public void iniciarSesion(String correo, String contrasena, Resultado<Usuario> callback) {
        cliente.enSegundoPlano(() -> {
            String cuerpo = UsuarioMapper.credenciales(correo, contrasena).toString();
            String respuesta = cliente.post(ApiConfig.url(recurso, "login"), cuerpo);
            return usuarioMapper.desdeJson(new JSONObject(respuesta));
        }, callback);
    }

    /** POST /usuarios: crea la cuenta con rol "usuario" y estado "activo". */
    public void registrar(Usuario usuario, String contrasena, Resultado<Usuario> callback) {
        cliente.enSegundoPlano(() -> {
            String cuerpo = usuarioMapper.aJsonRegistro(usuario, contrasena).toString();
            String respuesta = cliente.post(ApiConfig.url(recurso), cuerpo);
            return usuarioMapper.desdeJson(new JSONObject(respuesta));
        }, callback);
    }
}
