package com.archivex.movil.data.repositorio;

import com.archivex.movil.data.api.ApiClient;
import com.archivex.movil.data.api.ApiConfig;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.mapper.JsonMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Acceso a un recurso REST de ArchiveX (patrón Repository).
 * La interfaz solo pide "listar", "crear", etc., sin saber de HTTP ni de JSON.
 * Una sola clase genérica sirve para todas las entidades: cambia el recurso y el mapper.
 */
public class Repositorio<T> {

    protected final ApiConfig.Recurso recurso;
    protected final JsonMapper<T> mapper;
    protected final ApiClient cliente;

    public Repositorio(ApiConfig.Recurso recurso, JsonMapper<T> mapper) {
        this(recurso, mapper, ApiClient.getInstancia());
    }

    Repositorio(ApiConfig.Recurso recurso, JsonMapper<T> mapper, ApiClient cliente) {
        this.recurso = recurso;
        this.mapper = mapper;
        this.cliente = cliente;
    }

    /** GET /recurso */
    public void listar(Resultado<List<T>> callback) {
        cliente.enSegundoPlano(
                () -> mapper.lista(new JSONArray(cliente.get(ApiConfig.url(recurso)))),
                callback);
    }

    /** GET /recurso/{id} */
    public void obtener(int id, Resultado<T> callback) {
        cliente.enSegundoPlano(
                () -> mapper.desdeJson(new JSONObject(cliente.get(ApiConfig.url(recurso, id)))),
                callback);
    }

    /** POST /recurso; devuelve el registro creado con su id. */
    public void crear(T objeto, Resultado<T> callback) {
        cliente.enSegundoPlano(() -> {
            String respuesta = cliente.post(ApiConfig.url(recurso), mapper.aJson(objeto).toString());
            return mapper.desdeJson(new JSONObject(respuesta));
        }, callback);
    }

    /** PUT /recurso/{id}; devuelve el registro actualizado. */
    public void actualizar(int id, T objeto, Resultado<T> callback) {
        cliente.enSegundoPlano(() -> {
            String respuesta = cliente.put(ApiConfig.url(recurso, id), mapper.aJson(objeto).toString());
            return mapper.desdeJson(new JSONObject(respuesta));
        }, callback);
    }

    /** DELETE /recurso/{id} */
    public void eliminar(int id, Resultado<Boolean> callback) {
        cliente.enSegundoPlano(() -> {
            cliente.delete(ApiConfig.url(recurso, id));
            return Boolean.TRUE;
        }, callback);
    }
}
