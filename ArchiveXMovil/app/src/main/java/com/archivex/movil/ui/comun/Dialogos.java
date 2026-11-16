package com.archivex.movil.ui.comun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archivex.movil.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Map;

/** Cuadros de diálogo reutilizables. */
public final class Dialogos {

    private Dialogos() {
    }

    public static void confirmar(Context contexto, String mensaje, Runnable alAceptar) {
        new MaterialAlertDialogBuilder(contexto)
                .setMessage(mensaje)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.aceptar, (d, w) -> alAceptar.run())
                .show();
    }

    /** Muestra un registro como filas etiqueta / valor (para módulos de solo consulta). */
    public static void detalle(Context contexto, String titulo, Map<String, String> filas) {
        LinearLayout contenedor = new LinearLayout(contexto);
        contenedor.setOrientation(LinearLayout.VERTICAL);
        int margen = (int) (20 * contexto.getResources().getDisplayMetrics().density);
        contenedor.setPadding(margen, margen / 2, margen, 0);
        for (Map.Entry<String, String> fila : filas.entrySet()) {
            agregarFila(contenedor, fila.getKey(), fila.getValue());
        }
        new MaterialAlertDialogBuilder(contexto)
                .setTitle(titulo)
                .setView(contenedor)
                .setPositiveButton(R.string.aceptar, null)
                .show();
    }

    /** Agrega una fila etiqueta / valor a un contenedor; se omite si el valor está vacío. */
    public static void agregarFila(LinearLayout contenedor, String etiqueta, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return;
        }
        View fila = LayoutInflater.from(contenedor.getContext())
                .inflate(R.layout.fila_detalle, contenedor, false);
        ((TextView) fila.findViewById(R.id.textoEtiqueta)).setText(etiqueta);
        ((TextView) fila.findViewById(R.id.textoValor)).setText(valor);
        contenedor.addView(fila);
    }
}
