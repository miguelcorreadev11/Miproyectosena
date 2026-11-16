package com.archivex.movil.ui.comun;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.modelo.Entidad;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de listado reutilizable (patrón Template Method).
 * Resuelve la carga desde la API, la búsqueda, el "deslizar para actualizar" y los estados
 * vacío y de error. Cada módulo solo implementa los métodos abstractos.
 */
public abstract class ListaActivity<T extends Entidad> extends BaseActivity {

    private final List<T> todos = new ArrayList<>();
    private ListaAdapter<T> adaptador;
    private SwipeRefreshLayout deslizar;
    private EditText campoBusqueda;
    private TextView textoTotal;
    private LinearLayout panelEstado;
    private TextView textoEstado;
    private Button botonReintentar;

    // ---- Métodos que define cada módulo ----

    protected abstract String titulo();

    protected abstract Repositorio<T> repositorio();

    protected abstract ListaAdapter.Presentador<T> presentador();

    /** true si el registro coincide con el texto buscado. */
    protected abstract boolean coincide(T item, String filtro);

    protected boolean permiteCrear() {
        return false;
    }

    protected void alCrear() {
    }

    protected void alTocar(T item) {
    }

    protected void alMantenerPresionado(T item) {
    }

    // ---- Ciclo de vida ----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!exigirSesion()) return;
        setContentView(R.layout.activity_lista);
        configurarBarra(titulo(), true);

        deslizar = findViewById(R.id.deslizarActualizar);
        campoBusqueda = findViewById(R.id.campoBusqueda);
        textoTotal = findViewById(R.id.textoTotal);
        panelEstado = findViewById(R.id.panelEstado);
        textoEstado = findViewById(R.id.textoEstado);
        botonReintentar = findViewById(R.id.botonReintentar);

        adaptador = new ListaAdapter<>(presentador());
        adaptador.setAlTocar(this::alTocar);
        adaptador.setAlMantenerPresionado(this::alMantenerPresionado);
        RecyclerView lista = findViewById(R.id.listaRegistros);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adaptador);

        deslizar.setColorSchemeResources(R.color.acento);
        deslizar.setOnRefreshListener(this::cargar);
        botonReintentar.setOnClickListener(v -> cargar());
        campoBusqueda.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { aplicarFiltro(); }
        });

        ExtendedFloatingActionButton botonNuevo = findViewById(R.id.botonNuevo);
        UiUtil.visible(botonNuevo, permiteCrear());
        botonNuevo.setOnClickListener(v -> alCrear());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adaptador != null) {
            cargar();
        }
    }

    /** Vuelve a consultar la API. Los módulos lo llaman después de crear, editar o eliminar. */
    protected void cargar() {
        deslizar.setRefreshing(true);
        repositorio().listar(new Resultado<List<T>>() {
            @Override
            public void onExito(List<T> datos) {
                deslizar.setRefreshing(false);
                todos.clear();
                todos.addAll(datos);
                aplicarFiltro();
            }

            @Override
            public void onError(String mensaje) {
                deslizar.setRefreshing(false);
                todos.clear();
                adaptador.setItems(todos);
                textoTotal.setText("");
                mostrarEstado(mensaje, true);
            }
        });
    }

    private void aplicarFiltro() {
        String filtro = campoBusqueda.getText() == null ? "" : campoBusqueda.getText().toString();
        List<T> visibles = new ArrayList<>();
        for (T item : todos) {
            if (coincide(item, filtro)) {
                visibles.add(item);
            }
        }
        adaptador.setItems(visibles);
        textoTotal.setText(getString(R.string.total_registros, visibles.size()));
        if (todos.isEmpty()) {
            mostrarEstado(getString(R.string.sin_registros), false);
        } else if (visibles.isEmpty()) {
            mostrarEstado(getString(R.string.sin_resultados), false);
        } else {
            UiUtil.visible(panelEstado, false);
        }
    }

    private void mostrarEstado(String mensaje, boolean conReintento) {
        textoEstado.setText(mensaje);
        UiUtil.visible(botonReintentar, conReintento);
        UiUtil.visible(panelEstado, true);
    }

    /** Pide confirmación y elimina el registro en la API. */
    protected void confirmarEliminar(T item, String nombre) {
        Dialogos.confirmar(this, getString(R.string.confirmar_eliminar, nombre), () ->
                repositorio().eliminar(item.getId(), new Resultado<Boolean>() {
                    @Override
                    public void onExito(Boolean datos) {
                        mostrarMensaje(R.string.eliminado);
                        cargar();
                    }

                    @Override
                    public void onError(String mensaje) {
                        mostrarMensaje(mensaje);
                    }
                }));
    }
}
