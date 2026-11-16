package com.archivex.movil.data.mapper;

import com.archivex.movil.modelo.Almacen;

import org.json.JSONException;
import org.json.JSONObject;

import static com.archivex.movil.data.mapper.JsonMapper.enteroONulo;
import static com.archivex.movil.data.mapper.JsonMapper.poner;
import static com.archivex.movil.data.mapper.JsonMapper.texto;

public class AlmacenMapper implements JsonMapper<Almacen> {

    @Override
    public Almacen desdeJson(JSONObject json) {
        Almacen almacen = new Almacen();
        almacen.setIdAlmacen(json.optInt("idAlmacen"));
        almacen.setNombre(texto(json, "nombre"));
        almacen.setUbicacion(texto(json, "ubicacion"));
        almacen.setCiudad(texto(json, "ciudad"));
        almacen.setCapacidadMaxima(enteroONulo(json, "capacidadMaxima"));
        almacen.setEstado(texto(json, "estado"));
        return almacen;
    }

    @Override
    public JSONObject aJson(Almacen almacen) throws JSONException {
        JSONObject json = new JSONObject();
        poner(json, "nombre", almacen.getNombre());
        poner(json, "ubicacion", almacen.getUbicacion());
        poner(json, "ciudad", almacen.getCiudad());
        poner(json, "capacidadMaxima", almacen.getCapacidadMaxima());
        poner(json, "estado", almacen.getEstado());
        return json;
    }
}
