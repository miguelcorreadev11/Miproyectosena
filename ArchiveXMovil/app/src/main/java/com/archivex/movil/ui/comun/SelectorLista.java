package com.archivex.movil.ui.comun;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.archivex.movil.modelo.Entidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista desplegable de Material Design que recuerda el objeto elegido (no solo su texto).
 * Se usa para escoger categoría, producto, almacén y tipo de movimiento.
 */
public class SelectorLista<T> {

    private final AutoCompleteTextView vista;
    private final List<T> opciones = new ArrayList<>();
    private T seleccionado;

    public SelectorLista(AutoCompleteTextView vista) {
        this.vista = vista;
        vista.setOnItemClickListener((padre, v, posicion, id) -> seleccionado = opciones.get(posicion));
    }

    public void setOpciones(List<T> nuevas) {
        opciones.clear();
        opciones.addAll(nuevas);
        vista.setAdapter(new ArrayAdapter<>(vista.getContext(),
                android.R.layout.simple_list_item_1, opciones));
    }

    public T getSeleccionado() {
        return seleccionado;
    }

    public void seleccionar(T opcion) {
        seleccionado = opcion;
        vista.setText(opcion == null ? "" : opcion.toString(), false);
    }

    /** Selecciona la opción cuyo id coincide (solo para entidades). */
    public void seleccionarPorId(int id) {
        for (T opcion : opciones) {
            if (opcion instanceof Entidad && ((Entidad) opcion).getId() == id) {
                seleccionar(opcion);
                return;
            }
        }
    }
}
