package com.archivex.movil.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/** Pruebas de los formatos de presentación. */
public class FormatoTest {

    @Test
    public void monedaEnPesosColombianos() {
        assertEquals("$ 2.000.000", Formato.moneda(2000000));
        assertEquals("$ 3.500", Formato.moneda(3500));
        assertEquals("$ 0", Formato.moneda(0));
        assertEquals("$ 1.001", Formato.moneda(1000.6));
    }

    @Test
    public void fechaDeLaApi() {
        assertEquals("16/09/2026 15:31", Formato.fecha("2026-09-16 15:31:51.0"));
        assertEquals("", Formato.fecha(null));
        assertEquals("ayer", Formato.fecha("ayer"));
    }

    @Test
    public void capitalizarEstados() {
        assertEquals("Activo", Formato.capitalizar("activo"));
        assertEquals("Devolución", Formato.capitalizar("devolucion"));
        assertEquals("", Formato.capitalizar(null));
    }

    @Test
    public void unirOmiteVacios() {
        assertEquals("Bogotá · Colombia", Formato.unir("Bogotá", null, " ", "Colombia"));
        assertEquals("", Formato.unir());
    }
}
