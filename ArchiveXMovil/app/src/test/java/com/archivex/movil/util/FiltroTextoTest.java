package com.archivex.movil.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Pruebas de la búsqueda de los listados. */
public class FiltroTextoTest {

    @Test
    public void ignoraMayusculasYTildes() {
        assertTrue(FiltroTexto.coincide("cafe", "Café molido"));
        assertTrue(FiltroTexto.coincide("TELEVISOR", "Televisor Smart 50\""));
        assertTrue(FiltroTexto.coincide("almacen", "Almacén Central"));
    }

    @Test
    public void buscaEnVariosCampos() {
        assertTrue(FiltroTexto.coincide("tecno", "Iphone 18", null, "Tecnología"));
        assertFalse(FiltroTexto.coincide("pony", "Iphone 18", "Tecnología"));
    }

    @Test
    public void filtroVacioMuestraTodo() {
        assertTrue(FiltroTexto.coincide("", "lo que sea"));
        assertTrue(FiltroTexto.coincide(null, "lo que sea"));
        assertEquals("nino", FiltroTexto.normalizar("  Niño "));
    }
}
