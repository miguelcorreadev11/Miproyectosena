package com.archivex.movil.data.mapper;

import com.archivex.movil.modelo.Movimiento;

import org.json.JSONException;
import org.json.JSONObject;

import static com.archivex.movil.data.mapper.JsonMapper.enteroONulo;
import static com.archivex.movil.data.mapper.JsonMapper.poner;
import static com.archivex.movil.data.mapper.JsonMapper.texto;

public class MovimientoMapper implements JsonMapper<Movimiento> {

    @Override
    public Movimiento desdeJson(JSONObject json) {
        Movimiento movimiento = new Movimiento();
        movimiento.setIdMovimiento(json.optInt("idMovimiento"));
        movimiento.setIdProducto(json.optInt("idProducto"));
        movimiento.setNombreProducto(texto(json, "nombreProducto"));
        movimiento.setIdAlmacen(json.optInt("idAlmacen"));
        movimiento.setNombreAlmacen(texto(json, "nombreAlmacen"));
        movimiento.setIdUsuario(json.optInt("idUsuario"));
        movimiento.setNombreUsuario(texto(json, "nombreUsuario"));
        movimiento.setTipoMovimiento(texto(json, "tipoMovimiento"));
        movimiento.setCantidad(json.optInt("cantidad"));
        movimiento.setNumeroPedido(enteroONulo(json, "numeroPedido"));
        movimiento.setMotivo(texto(json, "motivo"));
        movimiento.setFechaMovimiento(texto(json, "fechaMovimiento"));
        return movimiento;
    }

    /** Cuerpo de POST /movimientos. */
    @Override
    public JSONObject aJson(Movimiento movimiento) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("idProducto", movimiento.getIdProducto());
        json.put("idAlmacen", movimiento.getIdAlmacen());
        json.put("idUsuario", movimiento.getIdUsuario());
        poner(json, "tipoMovimiento", movimiento.getTipoMovimiento());
        json.put("cantidad", movimiento.getCantidad());
        poner(json, "motivo", movimiento.getMotivo());
        return json;
    }
}
