package com.archivex.movil.data.mapper;

import com.archivex.movil.modelo.Producto;

import org.json.JSONException;
import org.json.JSONObject;

import static com.archivex.movil.data.mapper.JsonMapper.poner;
import static com.archivex.movil.data.mapper.JsonMapper.texto;

public class ProductoMapper implements JsonMapper<Producto> {

    @Override
    public Producto desdeJson(JSONObject json) {
        Producto producto = new Producto();
        producto.setIdProducto(json.optInt("idProducto"));
        producto.setNombre(texto(json, "nombre"));
        producto.setDescripcion(texto(json, "descripcion"));
        producto.setIdCategoria(json.optInt("idCategoria"));
        producto.setNombreCategoria(texto(json, "nombreCategoria"));
        producto.setPrecioCompra(json.optDouble("precioCompra", 0));
        producto.setPrecioVenta(json.optDouble("precioVenta", 0));
        producto.setCantidad(json.optInt("cantidad"));
        producto.setEstado(texto(json, "estado"));
        producto.setImagen(texto(json, "imagen"));
        producto.setFechaCreacion(texto(json, "fechaCreacion"));
        return producto;
    }

    @Override
    public JSONObject aJson(Producto producto) throws JSONException {
        JSONObject json = new JSONObject();
        poner(json, "nombre", producto.getNombre());
        poner(json, "descripcion", producto.getDescripcion());
        json.put("idCategoria", producto.getIdCategoria());
        json.put("precioCompra", producto.getPrecioCompra());
        json.put("precioVenta", producto.getPrecioVenta());
        json.put("cantidad", producto.getCantidad());
        poner(json, "estado", producto.getEstado());
        poner(json, "imagen", producto.getImagen());
        return json;
    }
}
