package com.archivex.movil.ui.comun;

import android.content.Context;
import android.text.InputType;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.archivex.movil.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Formulario en un cuadro de diálogo, armado a partir de una lista de campos (patrón Builder).
 * Se reutiliza en los catálogos (categorías, almacenes, proveedores) y en la configuración del servidor.
 */
public class FormularioDialog {

    /** Regla extra de un campo. Devuelve el mensaje de error, o null si el valor es válido. */
    public interface Regla {
        String validar(String valor);
    }

    /** Se llama con los valores escritos cuando todos los campos son válidos. */
    public interface AlGuardar {
        void guardar(Map<String, String> valores, AlertDialog dialogo);
    }

    private static class Campo {
        final String clave;
        final String etiqueta;
        final int tipoEntrada;
        final boolean obligatorio;
        final String valorInicial;
        final Regla regla;
        TextInputLayout capa;

        Campo(String clave, String etiqueta, int tipoEntrada, boolean obligatorio, String valorInicial, Regla regla) {
            this.clave = clave;
            this.etiqueta = etiqueta;
            this.tipoEntrada = tipoEntrada;
            this.obligatorio = obligatorio;
            this.valorInicial = valorInicial;
            this.regla = regla;
        }
    }

    public static final int TEXTO = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
    public static final int CORREO = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    public static final int TELEFONO = InputType.TYPE_CLASS_PHONE;
    public static final int ENTERO = InputType.TYPE_CLASS_NUMBER;
    public static final int DIRECCION_WEB = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI;

    private final Context contexto;
    private final String titulo;
    private final List<Campo> campos = new ArrayList<>();
    private String ayuda;

    public FormularioDialog(Context contexto, String titulo) {
        this.contexto = contexto;
        this.titulo = titulo;
    }

    public FormularioDialog campo(String clave, String etiqueta, int tipoEntrada, boolean obligatorio,
                                  String valorInicial) {
        return campo(clave, etiqueta, tipoEntrada, obligatorio, valorInicial, null);
    }

    public FormularioDialog campo(String clave, String etiqueta, int tipoEntrada, boolean obligatorio,
                                  String valorInicial, Regla regla) {
        campos.add(new Campo(clave, etiqueta, tipoEntrada, obligatorio, valorInicial, regla));
        return this;
    }

    public FormularioDialog ayuda(String texto) {
        this.ayuda = texto;
        return this;
    }

    public void mostrar(AlGuardar alGuardar) {
        LinearLayout contenedor = new LinearLayout(contexto);
        contenedor.setOrientation(LinearLayout.VERTICAL);
        int margen = (int) (20 * contexto.getResources().getDisplayMetrics().density);
        contenedor.setPadding(margen, margen / 2, margen, 0);

        for (Campo campo : campos) {
            TextInputLayout capa = new TextInputLayout(contexto, null,
                    com.google.android.material.R.attr.textInputStyle);
            capa.setHint(campo.obligatorio ? campo.etiqueta + " *" : campo.etiqueta);
            TextInputEditText editor = new TextInputEditText(capa.getContext());
            editor.setInputType(campo.tipoEntrada);
            editor.setText(campo.valorInicial == null ? "" : campo.valorInicial);
            capa.addView(editor);
            LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            parametros.topMargin = margen / 2;
            contenedor.addView(capa, parametros);
            campo.capa = capa;
        }
        if (ayuda != null && !campos.isEmpty()) {
            campos.get(campos.size() - 1).capa.setHelperText(ayuda);
        }

        AlertDialog dialogo = new MaterialAlertDialogBuilder(contexto)
                .setTitle(titulo)
                .setView(contenedor)
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.guardar, null)
                .create();
        // El botón Guardar se configura después de mostrar el diálogo para que no se cierre si hay errores.
        dialogo.setOnShowListener(d -> dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            Map<String, String> valores = validar();
            if (valores != null) {
                alGuardar.guardar(valores, dialogo);
            }
        }));
        dialogo.show();
    }

    /** Valida todos los campos; devuelve los valores o null si alguno tiene error. */
    private Map<String, String> validar() {
        Map<String, String> valores = new LinkedHashMap<>();
        boolean todoValido = true;
        for (Campo campo : campos) {
            String valor = UiUtil.texto(campo.capa);
            String error = null;
            if (campo.obligatorio && valor.isEmpty()) {
                error = contexto.getString(R.string.campo_obligatorio);
            } else if (!valor.isEmpty() && campo.regla != null) {
                error = campo.regla.validar(valor);
            }
            campo.capa.setError(error);
            todoValido &= error == null;
            valores.put(campo.clave, valor.isEmpty() ? null : valor);
        }
        return todoValido ? valores : null;
    }
}
