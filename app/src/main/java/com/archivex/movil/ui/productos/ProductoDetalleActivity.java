package com.archivex.movil.ui.productos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Producto;
import com.archivex.movil.ui.comun.BaseActivity;
import com.archivex.movil.ui.comun.Dialogos;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.Formato;

import java.util.Locale;

/** Detalle de un producto con sus precios, margen y existencias. Permite editarlo o eliminarlo. */
public class ProductoDetalleActivity extends BaseActivity {

    public static final String EXTRA_ID = "id_producto";

    private final Repositorio<Producto> repositorio = Repositorios.productos();
    private int idProducto;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!exigirSesion()) return;
        setContentView(R.layout.activity_detalle);
        configurarBarra(getString(R.string.producto), true);
        idProducto = getIntent().getIntExtra(EXTRA_ID, 0);

        findViewById(R.id.botonEditar).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductoFormActivity.class);
            intent.putExtra(ProductoFormActivity.EXTRA_ID, idProducto);
            startActivity(intent);
        });
        findViewById(R.id.botonEliminar).setOnClickListener(v -> eliminar());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idProducto > 0) {
            cargar();
        }
    }

    private void cargar() {
        UiUtil.visible(findViewById(R.id.progreso), true);
        repositorio.obtener(idProducto, new Resultado<Producto>() {
            @Override
            public void onExito(Producto datos) {
                UiUtil.visible(findViewById(R.id.progreso), false);
                producto = datos;
                mostrar(datos);
            }

            @Override
            public void onError(String mensaje) {
                UiUtil.visible(findViewById(R.id.progreso), false);
                mostrarMensaje(mensaje);
            }
        });
    }

    private void mostrar(Producto p) {
        ((TextView) findViewById(R.id.textoEncabezado)).setText(p.getNombre());
        UiUtil.pintarInsignia(findViewById(R.id.textoInsignia),
                ProductosActivity.etiquetaExistencia(p), ProductosActivity.colorExistencia(p));
        LinearLayout filas = findViewById(R.id.contenedorFilas);
        filas.removeAllViews();
        Dialogos.agregarFila(filas, "Código", String.valueOf(p.getIdProducto()));
        Dialogos.agregarFila(filas, getString(R.string.descripcion), p.getDescripcion());
        Dialogos.agregarFila(filas, getString(R.string.categoria), p.getNombreCategoria());
        Dialogos.agregarFila(filas, getString(R.string.precio_compra), Formato.moneda(p.getPrecioCompra()));
        Dialogos.agregarFila(filas, getString(R.string.precio_venta), Formato.moneda(p.getPrecioVenta()));
        Dialogos.agregarFila(filas, "Ganancia por unidad", Formato.moneda(p.getGananciaUnitaria())
                + String.format(Locale.ROOT, " (%.1f %%)", p.getMargenPorcentaje()));
        Dialogos.agregarFila(filas, "Existencia total", p.getCantidad() + " unidades");
        Dialogos.agregarFila(filas, "Valor del inventario", Formato.moneda(p.getValorInventario()));
        Dialogos.agregarFila(filas, "Estado", Formato.capitalizar(p.getEstado()));
        Dialogos.agregarFila(filas, "Creado", Formato.fecha(p.getFechaCreacion()));
    }

    private void eliminar() {
        if (producto == null) return;
        Dialogos.confirmar(this, getString(R.string.confirmar_eliminar, producto.getNombre()), () ->
                repositorio.eliminar(idProducto, new Resultado<Boolean>() {
                    @Override
                    public void onExito(Boolean datos) {
                        finish();
                    }

                    @Override
                    public void onError(String mensaje) {
                        mostrarMensaje(mensaje);
                    }
                }));
    }
}
