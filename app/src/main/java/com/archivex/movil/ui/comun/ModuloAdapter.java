package com.archivex.movil.ui.comun;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.archivex.movil.R;

import java.util.List;

/** Tarjetas de acceso a módulos, usadas por el menú principal y el submenú de catálogos. */
public class ModuloAdapter extends RecyclerView.Adapter<ModuloAdapter.Tarjeta> {

    /** Un acceso del menú: ícono, título, descripción y acción al tocarlo. */
    public static class Modulo {
        final int icono;
        final int titulo;
        final int descripcion;
        final Runnable accion;

        public Modulo(int icono, int titulo, int descripcion, Runnable accion) {
            this.icono = icono;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.accion = accion;
        }
    }

    private final List<Modulo> modulos;

    public ModuloAdapter(List<Modulo> modulos) {
        this.modulos = modulos;
    }

    @NonNull
    @Override
    public Tarjeta onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {
        View vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_modulo, padre, false);
        return new Tarjeta(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull Tarjeta tarjeta, int posicion) {
        Modulo modulo = modulos.get(posicion);
        tarjeta.icono.setImageResource(modulo.icono);
        tarjeta.titulo.setText(modulo.titulo);
        tarjeta.descripcion.setText(modulo.descripcion);
        tarjeta.itemView.setOnClickListener(v -> modulo.accion.run());
    }

    @Override
    public int getItemCount() {
        return modulos.size();
    }

    static class Tarjeta extends RecyclerView.ViewHolder {
        final ImageView icono;
        final TextView titulo;
        final TextView descripcion;

        Tarjeta(View vista) {
            super(vista);
            icono = vista.findViewById(R.id.iconoModulo);
            titulo = vista.findViewById(R.id.tituloModulo);
            descripcion = vista.findViewById(R.id.descripcionModulo);
        }
    }
}
