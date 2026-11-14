package com.archivex.movil.ui.productos;

import android.content.Intent;

import com.archivex.movil.R;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Producto;
import com.archivex.movil.ui.comun.ListaActivity;
import com.archivex.movil.ui.comun.ListaAdapter;
import com.archivex.movil.util.FiltroTexto;
import com.archivex.movil.util.Formato;

/** Módulo de productos e inventario: listado con búsqueda, alertas de existencias y acceso al detalle. */
public class ProductosActivity extends ListaActivity<Producto> {

    private final Repositorio<Producto> repositorio = Repositorios.productos();

    @Override
    protected String titulo() {
        return getString(R.string.modulo_productos);
    }

    @Override
    protected Repositorio<Producto> repositorio() {
        return repositorio;
    }

    @Override
    protected ListaAdapter.Presentador<Producto> presentador() {
        return new ListaAdapter.Presentador<Producto>() {
            @Override public String titulo(Producto p) { return p.getNombre(); }
            @Override public String subtitulo(Producto p) {
                return Formato.unir(p.getNombreCategoria(), Formato.moneda(p.getPrecioVenta()));
            }
            @Override public String detalle(Producto p) { return "Existencia: " + p.getCantidad() + " und"; }
            @Override public String insignia(Producto p) { return etiquetaExistencia(p); }
            @Override public int colorInsignia(Producto p) { return colorExistencia(p); }
        };
    }

    /** Agotado, stock bajo, descontinuado o disponible: la alerta más importante primero. */
    static String etiquetaExistencia(Producto p) {
        if (!p.estaActivo()) return "Descontinuado";
        if (p.estaAgotado()) return "Agotado";
        if (p.tieneStockBajo()) return "Stock bajo";
        return "Disponible";
    }

    static int colorExistencia(Producto p) {
        if (!p.estaActivo() || p.estaAgotado()) return R.color.peligro;
        if (p.tieneStockBajo()) return R.color.alerta;
        return R.color.exito;
    }

    @Override
    protected boolean coincide(Producto p, String filtro) {
        return FiltroTexto.coincide(filtro, p.getNombre(), p.getDescripcion(), p.getNombreCategoria());
    }

    @Override
    protected boolean permiteCrear() {
        return true;
    }

    @Override
    protected void alCrear() {
        startActivity(new Intent(this, ProductoFormActivity.class));
    }

    @Override
    protected void alTocar(Producto producto) {
        Intent intent = new Intent(this, ProductoDetalleActivity.class);
        intent.putExtra(ProductoDetalleActivity.EXTRA_ID, producto.getIdProducto());
        startActivity(intent);
    }
}
