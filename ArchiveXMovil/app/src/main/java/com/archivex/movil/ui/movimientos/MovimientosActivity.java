package com.archivex.movil.ui.movimientos;

import android.content.Intent;

import com.archivex.movil.R;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Movimiento;
import com.archivex.movil.ui.comun.Dialogos;
import com.archivex.movil.ui.comun.ListaActivity;
import com.archivex.movil.ui.comun.ListaAdapter;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.FiltroTexto;
import com.archivex.movil.util.Formato;

import java.util.LinkedHashMap;
import java.util.Map;

/** Módulo de movimientos: historial de entradas, salidas, ajustes y devoluciones. */
public class MovimientosActivity extends ListaActivity<Movimiento> {

    private final Repositorio<Movimiento> repositorio = Repositorios.movimientos();

    @Override
    protected String titulo() {
        return getString(R.string.modulo_movimientos);
    }

    @Override
    protected Repositorio<Movimiento> repositorio() {
        return repositorio;
    }

    @Override
    protected ListaAdapter.Presentador<Movimiento> presentador() {
        return new ListaAdapter.Presentador<Movimiento>() {
            @Override public String titulo(Movimiento m) { return m.getNombreProducto(); }
            @Override public String subtitulo(Movimiento m) {
                return m.getSigno() + m.getCantidad() + " und · " + m.getNombreAlmacen();
            }
            @Override public String detalle(Movimiento m) {
                return Formato.unir(Formato.fecha(m.getFechaMovimiento()), m.getNombreUsuario());
            }
            @Override public String insignia(Movimiento m) { return Formato.capitalizar(m.getTipoMovimiento()); }
            @Override public int colorInsignia(Movimiento m) { return UiUtil.colorEstado(m.getTipoMovimiento()); }
        };
    }

    @Override
    protected boolean coincide(Movimiento m, String filtro) {
        return FiltroTexto.coincide(filtro, m.getNombreProducto(), m.getNombreAlmacen(),
                m.getTipoMovimiento(), m.getMotivo(), m.getNombreUsuario());
    }

    @Override
    protected boolean permiteCrear() {
        return true;
    }

    @Override
    protected void alCrear() {
        startActivity(new Intent(this, MovimientoFormActivity.class));
    }

    @Override
    protected void alTocar(Movimiento m) {
        Map<String, String> filas = new LinkedHashMap<>();
        filas.put("Tipo", Formato.capitalizar(m.getTipoMovimiento()));
        filas.put("Cantidad", m.getSigno() + m.getCantidad() + " unidades");
        filas.put("Almacén", m.getNombreAlmacen());
        filas.put("Registrado por", m.getNombreUsuario());
        filas.put("Fecha", Formato.fecha(m.getFechaMovimiento()));
        filas.put("Pedido de compra", m.vieneDeCompra() ? "N° " + m.getNumeroPedido() : null);
        filas.put("Motivo", m.getMotivo());
        Dialogos.detalle(this, m.getNombreProducto(), filas);
    }
}
