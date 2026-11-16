package com.archivex.movil.ui.movimientos;

import android.os.Bundle;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Almacen;
import com.archivex.movil.modelo.Movimiento;
import com.archivex.movil.modelo.Producto;
import com.archivex.movil.ui.comun.BaseActivity;
import com.archivex.movil.ui.comun.SelectorLista;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.Formato;
import com.archivex.movil.util.Validador;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Registra un movimiento con POST /movimientos. El servidor aplica las mismas reglas del escritorio
 * (transacción, existencias suficientes, historial), así que el inventario queda consistente.
 */
public class MovimientoFormActivity extends BaseActivity {

    /** Opción de tipo con texto legible y el valor que espera la API. */
    private static class OpcionTipo {
        final String valor;

        OpcionTipo(String valor) {
            this.valor = valor;
        }

        @Override
        public String toString() {
            return Formato.capitalizar(valor);
        }
    }

    private TextInputLayout capaProducto, capaAlmacen, capaTipo, capaCantidad, capaMotivo;
    private SelectorLista<Producto> selectorProducto;
    private SelectorLista<Almacen> selectorAlmacen;
    private SelectorLista<OpcionTipo> selectorTipo;
    private MaterialButton botonGuardar;
    private int consultasPendientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!exigirSesion()) return;
        setContentView(R.layout.activity_movimiento_form);
        configurarBarra(getString(R.string.movimiento_nuevo), true);

        capaProducto = findViewById(R.id.capaProducto);
        capaAlmacen = findViewById(R.id.capaAlmacen);
        capaTipo = findViewById(R.id.capaTipo);
        capaCantidad = findViewById(R.id.capaCantidad);
        capaMotivo = findViewById(R.id.capaMotivo);
        botonGuardar = findViewById(R.id.botonGuardar);
        selectorProducto = new SelectorLista<>(findViewById(R.id.listaProducto));
        selectorAlmacen = new SelectorLista<>(findViewById(R.id.listaAlmacen));
        selectorTipo = new SelectorLista<>(findViewById(R.id.listaTipo));

        List<OpcionTipo> tipos = new ArrayList<>();
        for (String tipo : Movimiento.TIPOS) {
            tipos.add(new OpcionTipo(tipo));
        }
        selectorTipo.setOpciones(tipos);
        selectorTipo.seleccionar(tipos.get(0));
        findViewById(R.id.listaTipo).setOnFocusChangeListener((v, f) -> actualizarAyudaCantidad());
        ((android.widget.AutoCompleteTextView) findViewById(R.id.listaTipo))
                .setOnItemClickListener((padre, v, posicion, id) -> {
                    selectorTipo.seleccionar(tipos.get(posicion));
                    actualizarAyudaCantidad();
                });

        botonGuardar.setOnClickListener(v -> guardar());
        cargarListas();
    }

    private void actualizarAyudaCantidad() {
        OpcionTipo tipo = selectorTipo.getSeleccionado();
        boolean esAjuste = tipo != null && Movimiento.AJUSTE.equals(tipo.valor);
        capaCantidad.setHelperText(esAjuste ? getString(R.string.ayuda_ajuste) : null);
    }

    /** Consulta productos y almacenes en paralelo; el botón se habilita cuando llegan ambos. */
    private void cargarListas() {
        consultasPendientes = 2;
        cargando(true);
        Repositorios.productos().listar(new Resultado<List<Producto>>() {
            @Override public void onExito(List<Producto> datos) {
                List<Producto> activos = new ArrayList<>();
                for (Producto p : datos) {
                    if (p.estaActivo()) activos.add(p);
                }
                selectorProducto.setOpciones(activos);
                terminoConsulta();
            }

            @Override public void onError(String mensaje) {
                mostrarMensaje(mensaje);
                terminoConsulta();
            }
        });
        Repositorios.almacenes().listar(new Resultado<List<Almacen>>() {
            @Override public void onExito(List<Almacen> datos) {
                List<Almacen> activos = new ArrayList<>();
                for (Almacen a : datos) {
                    if ("activo".equalsIgnoreCase(a.getEstado())) activos.add(a);
                }
                selectorAlmacen.setOpciones(activos);
                terminoConsulta();
            }

            @Override public void onError(String mensaje) {
                mostrarMensaje(mensaje);
                terminoConsulta();
            }
        });
    }

    private void terminoConsulta() {
        consultasPendientes--;
        if (consultasPendientes == 0) {
            cargando(false);
        }
    }

    private void guardar() {
        Producto producto = selectorProducto.getSeleccionado();
        Almacen almacen = selectorAlmacen.getSeleccionado();
        OpcionTipo tipo = selectorTipo.getSeleccionado();
        Integer cantidad = Validador.aEntero(UiUtil.texto(capaCantidad));

        boolean valido = UiUtil.marcar(capaProducto, producto != null, "Selecciona un producto");
        valido &= UiUtil.marcar(capaAlmacen, almacen != null, "Selecciona un almacén");
        valido &= UiUtil.marcar(capaTipo, tipo != null, "Selecciona el tipo");
        valido &= UiUtil.marcar(capaCantidad, tipo != null && cantidad != null
                && Movimiento.esCantidadValida(tipo.valor, cantidad), getString(R.string.error_cantidad));
        if (!valido) {
            return;
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setIdProducto(producto.getIdProducto());
        movimiento.setIdAlmacen(almacen.getIdAlmacen());
        movimiento.setIdUsuario(sesion.getUsuario().getIdUsuario());
        movimiento.setTipoMovimiento(tipo.valor);
        movimiento.setCantidad(cantidad);
        String motivo = UiUtil.texto(capaMotivo);
        movimiento.setMotivo(motivo.isEmpty() ? null : motivo);

        cargando(true);
        Repositorios.movimientos().crear(movimiento, new Resultado<Movimiento>() {
            @Override
            public void onExito(Movimiento registrado) {
                cargando(false);
                finish();
            }

            @Override
            public void onError(String mensaje) {
                cargando(false);
                mostrarMensaje(mensaje);
            }
        });
    }

    private void cargando(boolean activo) {
        botonGuardar.setEnabled(!activo);
        UiUtil.visible(findViewById(R.id.progreso), activo);
    }
}
