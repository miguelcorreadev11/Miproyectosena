package com.archivex.movil.util;

import java.text.Normalizer;
import java.util.Locale;

/** Búsqueda sin distinguir mayúsculas ni tildes ("cafe" encuentra "Café"). */
public final class FiltroTexto {

    private FiltroTexto() {
    }

    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase(Locale.ROOT).trim();
    }

    /** true si el filtro está vacío o aparece en alguno de los campos. */
    public static boolean coincide(String filtro, String... campos) {
        String buscado = normalizar(filtro);
        if (buscado.isEmpty()) {
            return true;
        }
        for (String campo : campos) {
            if (normalizar(campo).contains(buscado)) {
                return true;
            }
        }
        return false;
    }
}
