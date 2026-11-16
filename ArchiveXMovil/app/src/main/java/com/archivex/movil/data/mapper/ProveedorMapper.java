package com.archivex.movil.data.mapper;

import com.archivex.movil.modelo.Proveedor;

import org.json.JSONException;
import org.json.JSONObject;

import static com.archivex.movil.data.mapper.JsonMapper.poner;
import static com.archivex.movil.data.mapper.JsonMapper.texto;

public class ProveedorMapper implements JsonMapper<Proveedor> {

    @Override
    public Proveedor desdeJson(JSONObject json) {
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(json.optInt("idProveedor"));
        proveedor.setNombre(texto(json, "nombre"));
        proveedor.setCorreo(texto(json, "correo"));
        proveedor.setTelefono(texto(json, "telefono"));
        proveedor.setDireccion(texto(json, "direccion"));
        proveedor.setCiudad(texto(json, "ciudad"));
        proveedor.setPais(texto(json, "pais"));
        proveedor.setEstado(texto(json, "estado"));
        return proveedor;
    }

    @Override
    public JSONObject aJson(Proveedor proveedor) throws JSONException {
        JSONObject json = new JSONObject();
        poner(json, "nombre", proveedor.getNombre());
        poner(json, "correo", proveedor.getCorreo());
        poner(json, "telefono", proveedor.getTelefono());
        poner(json, "direccion", proveedor.getDireccion());
        poner(json, "ciudad", proveedor.getCiudad());
        poner(json, "pais", proveedor.getPais());
        poner(json, "estado", proveedor.getEstado());
        return json;
    }
}
