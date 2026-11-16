package com.archivex.movil.modelo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** Reglas de negocio de las entidades. */
public class ModeloTest {

    private Producto producto(int cantidad, double compra, double venta, String estado) {
        Producto p = new Producto();
        p.setCantidad(cantidad);
        p.setPrecioCompra(compra);
        p.setPrecioVenta(venta);
        p.setEstado(estado);
        return p;
    }

    @Test
    public void alertasDeExistencia() {
        assertTrue(producto(10, 0, 0, "activo").tieneStockBajo());
        assertFalse(producto(11, 0, 0, "activo").tieneStockBajo());
        assertTrue(producto(0, 0, 0, "activo").estaAgotado());
        assertFalse(producto(5, 0, 0, "descontinuado").estaActivo());
    }

    @Test
    public void gananciaMargenYValorDeInventario() {
        Producto iphone = producto(10, 1000000, 2000000, "activo");
        assertEquals(1000000, iphone.getGananciaUnitaria(), 0.001);
        assertEquals(50.0, iphone.getMargenPorcentaje(), 0.001);
        assertEquals(10000000, iphone.getValorInventario(), 0.001);
        assertEquals(0, producto(1, 10, 0, "activo").getMargenPorcentaje(), 0.001);
    }

    @Test
    public void tiposYCantidadesDeMovimiento() {
        assertTrue(Movimiento.esTipoValido("entrada"));
        assertTrue(Movimiento.esTipoValido("devolucion"));
        assertFalse(Movimiento.esTipoValido("robo"));
        assertTrue(Movimiento.esCantidadValida(Movimiento.AJUSTE, 0));
        assertFalse(Movimiento.esCantidadValida(Movimiento.SALIDA, 0));
        assertFalse(Movimiento.esCantidadValida(Movimiento.ENTRADA, -3));
    }

    @Test
    public void signoDelMovimiento() {
        Movimiento m = new Movimiento();
        m.setTipoMovimiento(Movimiento.SALIDA);
        assertEquals("-", m.getSigno());
        m.setTipoMovimiento(Movimiento.AJUSTE);
        assertEquals("=", m.getSigno());
        m.setTipoMovimiento(Movimiento.DEVOLUCION);
        assertEquals("+", m.getSigno());
    }

    @Test
    public void rolesYEstadosDeUsuario() {
        Usuario u = new Usuario();
        u.setNombre("Miguel");
        u.setApellido("Correa");
        assertEquals("Miguel Correa", u.getNombreCompleto());
        assertEquals("Usuario", u.getRolTexto());
        assertTrue(u.estaActivo());
        u.setRol("administrador");
        assertTrue(u.esAdministrador());
        u.setEstado("inactivo");
        assertFalse(u.estaActivo());
    }

    @Test
    public void estadoDeCompra() {
        Compra c = new Compra();
        c.setEstado("entregado");
        assertTrue(c.estaEntregada());
        c.setEstado("pendiente");
        assertTrue(c.estaPendiente());
        assertFalse(c.estaEntregada());
    }
}
