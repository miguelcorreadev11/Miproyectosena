package com.archivex.movil.ui.productos;

import android.os.Bundle;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Categoria;
import com.archivex.movil.modelo.Producto;
import com.archivex.movil.ui.comun.BaseActivity;
import com.archivex.movil.ui.comun.SelectorLista;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.Validador;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

/** Formulario para crear o editar un producto (POST /productos o PUT /productos/{id}). */
public class ProductoFormActivity extends BaseActivity {

    public static final String EXTRA_ID = "id_producto";

    private final Repositorio<Producto> productos = Repositorios.productos();
    private TextInputLayout capaNombre, capaDescripcion, capaCategoria, capaPrecioCompra, capaPrecioVenta, capaCantidad;
    private SwitchMaterial interruptorActivo;
    private MaterialButton botonGuardar;
    private SelectorLista<Categoria> selectorCategoria;
    private Producto producto;
    private int idProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!exigirSesion()) return;
        setContentView(R.layout.activity_producto_form);
        idProducto = getIntent().getIntExtra(EXTRA_ID, 0);
        configurarBarra(getString(idProducto > 0 ? R.string.producto_editar : R.string.producto_nuevo), true);

        capaNombre = findViewById(R.id.capaNombre);
        capaDescripcion = findViewById(R.id.capaDescripcion);
        capaCategoria = findViewById(R.id.capaCategoria);
        capaPrecioCompra = findViewById(R.id.capaPrecioCompra);
        capaPrecioVenta = findViewById(R.id.capaPrecioVenta);
        capaCantidad = findViewById(R.id.capaCantidad);
        interruptorActivo = findViewById(R.id.interruptorActivo);
        botonGuardar = findViewById(R.id.botonGuardar);
        selectorCategoria = new SelectorLista<>(findViewById(R.id.listaCategoria));
        botonGuardar.setOnClickListener(v -> guardar());

        if (idProducto > 0) {
            // La existencia de un producto creado solo cambia con movimientos (igual que en el escritorio).
            capaCantidad.setEnabled(false);
            capaCantidad.setHelperText(getString(R.string.cantidad_por_movimientos));
        }
        cargarCategorias();
    }

    private void cargarCategorias() {
        cargando(true);
        Repositorios.categorias().listar(new Resultado<List<Categoria>>() {
            @Override
            public void onExito(List<Categoria> categorias) {
                selectorCategoria.setOpciones(categorias);
                if (idProducto > 0) {
                    cargarProducto();
                } else {
                    cargando(false);
                }
            }

            @Override
            public void onError(String mensaje) {
                cargando(false);
                mostrarMensaje(mensaje);
            }
        });
    }

    private void cargarProducto() {
        productos.obtener(idProducto, new Resultado<Producto>() {
            @Override
            public void onExito(Producto datos) {
                cargando(false);
                producto = datos;
                UiUtil.ponerTexto(capaNombre, datos.getNombre());
                UiUtil.ponerTexto(capaDescripcion, datos.getDescripcion());
                UiUtil.ponerTexto(capaPrecioCompra, sinDecimalesInnecesarios(datos.getPrecioCompra()));
                UiUtil.ponerTexto(capaPrecioVenta, sinDecimalesInnecesarios(datos.getPrecioVenta()));
                UiUtil.ponerTexto(capaCantidad, String.valueOf(datos.getCantidad()));
                interruptorActivo.setChecked(datos.estaActivo());
                selectorCategoria.seleccionarPorId(datos.getIdCategoria());
            }

            @Override
            public void onError(String mensaje) {
                cargando(false);
                mostrarMensaje(mensaje);
            }
        });
    }

    private void guardar() {
        String obligatorio = getString(R.string.campo_obligatorio);
        String nombre = UiUtil.texto(capaNombre);
        String compra = UiUtil.texto(capaPrecioCompra);
        String venta = UiUtil.texto(capaPrecioVenta);
        String cantidad = UiUtil.texto(capaCantidad);
        Categoria categoria = selectorCategoria.getSeleccionado();

        boolean valido = UiUtil.marcar(capaNombre, !Validador.estaVacio(nombre), obligatorio);
        valido &= UiUtil.marcar(capaCategoria, categoria != null, "Selecciona una categoría");
        valido &= UiUtil.marcar(capaPrecioCompra, Validador.esDecimalNoNegativo(compra), "Escribe un precio válido");
        valido &= UiUtil.marcar(capaPrecioVenta, Validador.esDecimalNoNegativo(venta), "Escribe un precio válido");
        valido &= UiUtil.marcar(capaCantidad, Validador.esEnteroNoNegativo(cantidad), "Escribe una cantidad válida");
        if (valido && Validador.aDecimal(venta) < Validador.aDecimal(compra)) {
            valido = UiUtil.marcar(capaPrecioVenta, false, getString(R.string.error_precio_venta));
        }
        if (!valido) {
            return;
        }

        Producto datos = producto != null ? producto : new Producto();
        datos.setNombre(nombre);
        datos.setDescripcion(UiUtil.texto(capaDescripcion));
        datos.setIdCategoria(categoria.getIdCategoria());
        datos.setPrecioCompra(Validador.aDecimal(compra));
        datos.setPrecioVenta(Validador.aDecimal(venta));
        datos.setCantidad(Validador.aEntero(cantidad));
        datos.setEstado(interruptorActivo.isChecked() ? "activo" : "descontinuado");

        cargando(true);
        Resultado<Producto> alTerminar = new Resultado<Producto>() {
            @Override
            public void onExito(Producto guardado) {
                cargando(false);
                finish();
            }

            @Override
            public void onError(String mensaje) {
                cargando(false);
                mostrarMensaje(mensaje);
            }
        };
        if (idProducto > 0) {
            productos.actualizar(idProducto, datos, alTerminar);
        } else {
            productos.crear(datos, alTerminar);
        }
    }

    private void cargando(boolean activo) {
        botonGuardar.setEnabled(!activo);
        UiUtil.visible(findViewById(R.id.progreso), activo);
    }

    private static String sinDecimalesInnecesarios(double valor) {
        return valor == Math.rint(valor) ? String.valueOf((long) valor) : String.valueOf(valor);
    }
}
