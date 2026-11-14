package com.archivex.movil.data.mapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Convierte entre objetos del modelo y JSON de la API (patrón Strategy: cada entidad tiene su mapper).
 */
public interface JsonMapper<T> {

    T desdeJson(JSONObject json) throws JSONException;

    JSONObject aJson(T objeto) throws JSONException;

    default List<T> lista(JSONArray arreglo) throws JSONException {
        List<T> resultado = new ArrayList<>();
        for (int i = 0; i < arreglo.length(); i++) {
            resultado.add(desdeJson(arreglo.getJSONObject(i)));
        }
        return resultado;
    }

    // ---- Utilidades para leer valores que pueden venir como null en el JSON ----

    /** Texto o null (optString devuelve "null" cuando el valor es JSON null). */
    static String texto(JSONObject json, String clave) {
        return json.isNull(clave) ? null : json.optString(clave, null);
    }

    static Integer enteroONulo(JSONObject json, String clave) {
        return json.isNull(clave) ? null : json.optInt(clave);
    }

    /** Escribe el valor o JSON null si es null. */
    static void poner(JSONObject json, String clave, Object valor) throws JSONException {
        json.put(clave, valor == null ? JSONObject.NULL : valor);
    }
}
