package com.archivex.movil.ui.comun;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archivex.movil.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador genérico para todos los listados (patrón Adapter).
 * Cada módulo solo indica, mediante un Presentador, qué texto mostrar de cada registro.
 */
public class ListaAdapter<T> extends RecyclerView.Adapter<ListaAdapter.Fila> {

    /** Decide qué se muestra de cada registro en la fila. */
    public interface Presentador<T> {
        String titulo(T item);

        String subtitulo(T item);

        String detalle(T item);

        /** Texto de la etiqueta de estado (o null para ocultarla). */
        String insignia(T item);

        /** Recurso de color de la etiqueta. */
        int colorInsignia(T item);
    }

    public interface AccionItem<T> {
        void ejecutar(T item);
    }

    private final List<T> items = new ArrayList<>();
    private final Presentador<T> presentador;
    private AccionItem<T> alTocar;
    private AccionItem<T> alMantener;

    public ListaAdapter(Presentador<T> presentador) {
        this.presentador = presentador;
    }

    public void setAlTocar(AccionItem<T> accion) {
        this.alTocar = accion;
    }

    public void setAlMantenerPresionado(AccionItem<T> accion) {
        this.alMantener = accion;
    }

    public void setItems(List<T> nuevos) {
        items.clear();
        items.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fila onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_registro, padre, false);
        return new Fila(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Fila fila, int posicion) {
        T item = items.get(posicion);
        fila.titulo.setText(presentador.titulo(item));
        mostrarSiHayTexto(fila.subtitulo, presentador.subtitulo(item));
        mostrarSiHayTexto(fila.detalle, presentador.detalle(item));
        UiUtil.pintarInsignia(fila.insignia, presentador.insignia(item), presentador.colorInsignia(item));
        fila.itemView.setOnClickListener(v -> {
            if (alTocar != null) alTocar.ejecutar(item);
        });
        fila.itemView.setOnLongClickListener(v -> {
            if (alMantener == null) return false;
            alMantener.ejecutar(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static void mostrarSiHayTexto(TextView vista, String texto) {
        boolean hay = texto != null && !texto.isEmpty();
        vista.setVisibility(hay ? View.VISIBLE : View.GONE);
        vista.setText(hay ? texto : "");
    }

    static class Fila extends RecyclerView.ViewHolder {
        final TextView titulo;
        final TextView subtitulo;
        final TextView detalle;
        final TextView insignia;

        Fila(View vista) {
            super(vista);
            titulo = vista.findViewById(R.id.textoTitulo);
            subtitulo = vista.findViewById(R.id.textoSubtitulo);
            detalle = vista.findViewById(R.id.textoDetalle);
            insignia = vista.findViewById(R.id.textoInsignia);
        }
    }
}
