package com.archivex.movil.data.mapper;

import com.archivex.movil.modelo.Compra;

import org.json.JSONException;
import org.json.JSONObject;

import static com.archivex.movil.data.mapper.JsonMapper.texto;

public class CompraMapper implements JsonMapper<Compra> {

    @Override
    public Compra desdeJson(JSONObject json) {
        Compra compra = new Compra();
        compra.setIdCompra(json.optInt("idCompra"));
        compra.setNumeroPedido(json.optInt("numeroPedido"));
        compra.setIdProveedor(json.optInt("idProveedor"));
        compra.setNombreProveedor(texto(json, "nombreProveedor"));
        compra.setIdAlmacen(json.optInt("idAlmacen"));
        compra.setNombreAlmacen(texto(json, "nombreAlmacen"));
        compra.setFechaPedido(texto(json, "fechaPedido"));
        compra.setFechaEntrega(texto(json, "fechaEntrega"));
        compra.setValorTotal(json.optDouble("valorTotal", 0));
        compra.setEstado(texto(json, "estado"));
        compra.setComentarios(texto(json, "comentarios"));
        return compra;
    }

    /** La API de compras es de solo lectura; este método existe para cumplir la interfaz. */
    @Override
    public JSONObject aJson(Compra compra) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("numeroPedido", compra.getNumeroPedido());
        json.put("idProveedor", compra.getIdProveedor());
        json.put("idAlmacen", compra.getIdAlmacen());
        return json;
    }
}
