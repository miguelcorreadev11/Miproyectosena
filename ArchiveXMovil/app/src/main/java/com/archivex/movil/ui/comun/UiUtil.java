package com.archivex.movil.ui.comun;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.archivex.movil.R;
import com.google.android.material.textfield.TextInputLayout;

/** Funciones de apoyo para la interfaz, compartidas por todas las pantallas. */
public final class UiUtil {

    private UiUtil() {
    }

    /** Texto escrito en un campo (sin espacios al inicio y al final). */
    public static String texto(TextInputLayout capa) {
        if (capa.getEditText() == null || capa.getEditText().getText() == null) {
            return "";
        }
        return capa.getEditText().getText().toString().trim();
    }

    public static void ponerTexto(TextInputLayout capa, String valor) {
        if (capa.getEditText() != null) {
            capa.getEditText().setText(valor == null ? "" : valor);
        }
    }

    /** Muestra u oculta el mensaje de error de un campo. Devuelve true si el campo es válido. */
    public static boolean marcar(TextInputLayout capa, boolean valido, String mensajeError) {
        capa.setError(valido ? null : mensajeError);
        return valido;
    }

    /** Pinta una etiqueta de estado con el color indicado (recurso de color). */
    public static void pintarInsignia(TextView insignia, String texto, int colorRes) {
        if (texto == null || texto.isEmpty()) {
            insignia.setVisibility(View.GONE);
            return;
        }
        insignia.setVisibility(View.VISIBLE);
        insignia.setText(texto);
        int color = ContextCompat.getColor(insignia.getContext(), colorRes);
        ViewCompat.setBackgroundTintList(insignia, ColorStateList.valueOf(color));
    }

    /** Color según el estado guardado en la base de datos. */
    public static int colorEstado(String estado) {
        if (estado == null) return R.color.texto_atenuado;
        switch (estado.toLowerCase()) {
            case "activo":
            case "entregado":
            case "entrada":
            case "devolucion":
                return R.color.exito;
            case "pendiente":
            case "ajuste":
                return R.color.alerta;
            case "inactivo":
            case "cancelado":
            case "descontinuado":
            case "suspendido":
            case "salida":
                return R.color.peligro;
            default:
                return R.color.info;
        }
    }

    public static void visible(View vista, boolean mostrar) {
        vista.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    public static String texto(Context contexto, int recurso, Object... argumentos) {
        return contexto.getString(recurso, argumentos);
    }
}
