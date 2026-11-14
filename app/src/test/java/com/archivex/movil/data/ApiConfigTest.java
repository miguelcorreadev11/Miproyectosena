package com.archivex.movil.data;

import com.archivex.movil.data.api.ApiConfig;
import com.archivex.movil.data.api.ApiConfig.Recurso;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Las URL deben coincidir con los puertos y rutas de la API del escritorio. */
public class ApiConfigTest {

    @After
    public void restaurarHost() {
        ApiConfig.setHost(ApiConfig.HOST_POR_DEFECTO);
    }

    @Test
    public void urlsPorDefectoApuntanAlComputadorDesdeElEmulador() {
        assertEquals("http://10.0.2.2:8080/usuarios", ApiConfig.url(Recurso.USUARIOS));
        assertEquals("http://10.0.2.2:8081/productos/3", ApiConfig.url(Recurso.PRODUCTOS, 3));
        assertEquals("http://10.0.2.2:8080/usuarios/login", ApiConfig.url(Recurso.USUARIOS, "login"));
        assertEquals("http://10.0.2.2:8086/movimientos", ApiConfig.url(Recurso.MOVIMIENTOS));
    }

    @Test
    public void puertosDeCadaRecurso() {
        assertEquals(8082, Recurso.CATEGORIAS.getPuerto());
        assertEquals(8083, Recurso.PROVEEDORES.getPuerto());
        assertEquals(8084, Recurso.ALMACENES.getPuerto());
        assertEquals(8085, Recurso.COMPRAS.getPuerto());
    }

    @Test
    public void cambiarHostSoloSiEsValido() {
        assertTrue(ApiConfig.setHost(" 192.168.1.35 "));
        assertEquals("http://192.168.1.35:8084/almacenes", ApiConfig.url(Recurso.ALMACENES));
        assertFalse(ApiConfig.setHost("no es una ip"));
        assertEquals("192.168.1.35", ApiConfig.getHost());
    }
}
