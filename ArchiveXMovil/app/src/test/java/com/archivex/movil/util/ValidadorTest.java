package com.archivex.movil.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/** Pruebas de las reglas de validación de formularios. */
public class ValidadorTest {

    @Test
    public void correoValido() {
        assertTrue(Validador.esCorreoValido("mdch2077@gmail.com"));
        assertTrue(Validador.esCorreoValido("  torres123@gmail.com  "));
    }

    @Test
    public void correoInvalido() {
        assertFalse(Validador.esCorreoValido(null));
        assertFalse(Validador.esCorreoValido(""));
        assertFalse(Validador.esCorreoValido("miguel"));
        assertFalse(Validador.esCorreoValido("miguel@correo"));
        assertFalse(Validador.esCorreoValido("mi guel@correo.com"));
    }

    @Test
    public void contrasenaConLongitudMinimaDelEscritorio() {
        assertFalse(Validador.esContrasenaValida("123"));
        assertTrue(Validador.esContrasenaValida("1234"));
        assertFalse(Validador.esContrasenaValida(null));
    }

    @Test
    public void telefonoEsOpcionalPeroDebeSerValido() {
        assertTrue(Validador.esTelefonoValido(""));
        assertTrue(Validador.esTelefonoValido("3115861318"));
        assertTrue(Validador.esTelefonoValido("+57 311 586 1318"));
        assertFalse(Validador.esTelefonoValido("12ab"));
        assertFalse(Validador.esTelefonoValido("123"));
    }

    @Test
    public void numerosEnterosYDecimales() {
        assertTrue(Validador.esEnteroNoNegativo("0"));
        assertFalse(Validador.esEnteroNoNegativo("-1"));
        assertFalse(Validador.esEnteroPositivo("0"));
        assertTrue(Validador.esEnteroPositivo("15"));
        assertFalse(Validador.esEnteroPositivo("1.5"));
        assertTrue(Validador.esDecimalNoNegativo("3500"));
        assertTrue(Validador.esDecimalNoNegativo("3500,50"));
        assertFalse(Validador.esDecimalNoNegativo("-2"));
        assertFalse(Validador.esDecimalNoNegativo("abc"));
        assertEquals(3500.5, Validador.aDecimal("3500,5"), 0.0001);
        assertNull(Validador.aEntero(" "));
    }

    @Test
    public void direccionDelServidor() {
        assertTrue(Validador.esHostValido("10.0.2.2"));
        assertTrue(Validador.esHostValido("192.168.1.35"));
        assertTrue(Validador.esHostValido("localhost"));
        assertTrue(Validador.esHostValido("laptop-54r4tnac"));
        assertFalse(Validador.esHostValido("256.1.1.1"));
        assertFalse(Validador.esHostValido("10.0.2"));
        assertFalse(Validador.esHostValido("http://10.0.2.2"));
        assertFalse(Validador.esHostValido(""));
    }
}
