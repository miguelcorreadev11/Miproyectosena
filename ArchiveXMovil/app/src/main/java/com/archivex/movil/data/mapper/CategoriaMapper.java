package com.archivex.movil.data.mapper;

import com.archivex.movil.modelo.Categoria;

import org.json.JSONException;
import org.json.JSONObject;

import static com.archivex.movil.data.mapper.JsonMapper.poner;
import static com.archivex.movil.data.mapper.JsonMapper.texto;

public class CategoriaMapper implements JsonMapper<Categoria> {

    @Override
    public Categoria desdeJson(JSONObject json) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(json.optInt("idCategoria"));
        categoria.setNombre(texto(json, "nombre"));
        categoria.setDescripcion(texto(json, "descripcion"));
        categoria.setEstado(texto(json, "estado"));
        return categoria;
    }

    @Override
    public JSONObject aJson(Categoria categoria) throws JSONException {
        JSONObject json = new JSONObject();
        poner(json, "nombre", categoria.getNombre());
        poner(json, "descripcion", categoria.getDescripcion());
        poner(json, "estado", categoria.getEstado());
        return json;
    }
}
