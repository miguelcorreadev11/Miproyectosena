package com.archivex.movil.util;

import java.text.NumberFormat;
import java.util.Locale;

/** Formatos de presentación usados en todas las pantallas. */
public final class Formato {

    private static final Locale COLOMBIA = new Locale.Builder().setLanguage("es").setRegion("CO").build();

    private Formato() {
    }

    /** 2000000 → "$ 2.000.000" (pesos colombianos, sin decimales). */
    public static String moneda(double valor) {
        NumberFormat numero = NumberFormat.getIntegerInstance(COLOMBIA);
        numero.setGroupingUsed(true);
        return "$ " + numero.format(Math.round(valor));
    }

    /** "2026-09-16 15:31:51.0" → "16/09/2026 15:31". Si el texto no tiene ese formato, se devuelve igual. */
    public static String fecha(String fechaApi) {
        if (fechaApi == null || fechaApi.length() < 16) {
            return fechaApi == null ? "" : fechaApi;
        }
        String anio = fechaApi.substring(0, 4);
        String mes = fechaApi.substring(5, 7);
        String dia = fechaApi.substring(8, 10);
        String hora = fechaApi.substring(11, 16);
        return dia + "/" + mes + "/" + anio + " " + hora;
    }

    /** "activo" → "Activo"; "devolucion" → "Devolución". */
    public static String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "";
        }
        if ("devolucion".equalsIgnoreCase(texto)) {
            return "Devolución";
        }
        return texto.substring(0, 1).toUpperCase(COLOMBIA) + texto.substring(1).toLowerCase(COLOMBIA);
    }

    /** Une partes no vacías con " · ", por ejemplo "Bogotá · Colombia". */
    public static String unir(String... partes) {
        StringBuilder texto = new StringBuilder();
        for (String parte : partes) {
            if (parte == null || parte.trim().isEmpty()) {
                continue;
            }
            if (texto.length() > 0) {
                texto.append(" · ");
            }
            texto.append(parte.trim());
        }
        return texto.toString();
    }
}
