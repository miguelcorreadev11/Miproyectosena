package com.archivex.movil.ui.compras;

import com.archivex.movil.R;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Compra;
import com.archivex.movil.ui.comun.Dialogos;
import com.archivex.movil.ui.comun.ListaActivity;
import com.archivex.movil.ui.comun.ListaAdapter;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.FiltroTexto;
import com.archivex.movil.util.Formato;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Consulta de compras. Crear y entregar pedidos se hace en el escritorio, porque es una transacción
 * que también mueve el inventario; la API de compras es de solo lectura.
 */
public class ComprasActivity extends ListaActivity<Compra> {

    private final Repositorio<Compra> repositorio = Repositorios.compras();

    @Override
    protected String titulo() {
        return getString(R.string.modulo_compras);
    }

    @Override
    protected Repositorio<Compra> repositorio() {
        return repositorio;
    }

    @Override
    protected ListaAdapter.Presentador<Compra> presentador() {
        return new ListaAdapter.Presentador<Compra>() {
            @Override public String titulo(Compra c) { return "Pedido N° " + c.getNumeroPedido(); }
            @Override public String subtitulo(Compra c) {
                return Formato.unir(c.getNombreProveedor(), Formato.moneda(c.getValorTotal()));
            }
            @Override public String detalle(Compra c) {
                return Formato.unir(c.getNombreAlmacen(), Formato.fecha(c.getFechaPedido()));
            }
            @Override public String insignia(Compra c) { return Formato.capitalizar(c.getEstado()); }
            @Override public int colorInsignia(Compra c) { return UiUtil.colorEstado(c.getEstado()); }
        };
    }

    @Override
    protected boolean coincide(Compra c, String filtro) {
        return FiltroTexto.coincide(filtro, String.valueOf(c.getNumeroPedido()), c.getNombreProveedor(),
                c.getNombreAlmacen(), c.getEstado());
    }

    @Override
    protected void alTocar(Compra c) {
        Map<String, String> filas = new LinkedHashMap<>();
        filas.put("Proveedor", c.getNombreProveedor());
        filas.put("Almacén de destino", c.getNombreAlmacen());
        filas.put("Valor total", Formato.moneda(c.getValorTotal()));
        filas.put("Estado", Formato.capitalizar(c.getEstado()));
        filas.put("Fecha del pedido", Formato.fecha(c.getFechaPedido()));
        filas.put("Fecha de entrega", Formato.fecha(c.getFechaEntrega()));
        filas.put("Comentarios", c.getComentarios());
        Dialogos.detalle(this, "Pedido N° " + c.getNumeroPedido(), filas);
    }
}
