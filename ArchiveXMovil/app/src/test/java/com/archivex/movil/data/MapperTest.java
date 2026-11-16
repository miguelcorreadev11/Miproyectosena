package com.archivex.movil.data;

import com.archivex.movil.data.api.ApiException;
import com.archivex.movil.data.mapper.AlmacenMapper;
import com.archivex.movil.data.mapper.MovimientoMapper;
import com.archivex.movil.data.mapper.ProductoMapper;
import com.archivex.movil.data.mapper.UsuarioMapper;
import com.archivex.movil.modelo.Almacen;
import com.archivex.movil.modelo.Movimiento;
import com.archivex.movil.modelo.Producto;
import com.archivex.movil.modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/** Conversión entre JSON de la API y objetos del modelo (usa respuestas reales de la API de ArchiveX). */
public class MapperTest {

    private static final String PRODUCTO_JSON = "{\"idProducto\":1,\"nombre\":\"Iphone 18\","
            + "\"descripcion\":\"Telefono inteligente\",\"idCategoria\":2,\"nombreCategoria\":\"Tecnología\","
            + "\"precioCompra\":1000000.00,\"precioVenta\":2000000.00,\"cantidad\":10,\"estado\":\"activo\","
            + "\"imagen\":null,\"fechaCreacion\":\"2026-09-16 15:31:51.0\"}";

    @Test
    public void productoDesdeJson() throws Exception {
        Producto p = new ProductoMapper().desdeJson(new JSONObject(PRODUCTO_JSON));
        assertEquals(1, p.getIdProducto());
        assertEquals("Iphone 18", p.getNombre());
        assertEquals("Tecnología", p.getNombreCategoria());
        assertEquals(2000000, p.getPrecioVenta(), 0.01);
        assertEquals(10, p.getCantidad());
        assertNull("JSON null debe quedar como null, no como \"null\"", p.getImagen());
    }

    @Test
    public void productoAJsonTieneLosCamposObligatoriosDeLaApi() throws Exception {
        Producto p = new ProductoMapper().desdeJson(new JSONObject(PRODUCTO_JSON));
        JSONObject json = new ProductoMapper().aJson(p);
        assertEquals("Iphone 18", json.getString("nombre"));
        assertEquals(2, json.getInt("idCategoria"));
        assertEquals(1000000, json.getDouble("precioCompra"), 0.01);
        assertEquals(10, json.getInt("cantidad"));
        assertFalse("El id va en la URL, no en el cuerpo", json.has("idProducto"));
    }

    @Test
    public void listaDeProductos() throws Exception {
        List<Producto> lista = new ProductoMapper().lista(new JSONArray("[" + PRODUCTO_JSON + "," + PRODUCTO_JSON + "]"));
        assertEquals(2, lista.size());
    }

    @Test
    public void almacenConCapacidadNula() throws Exception {
        Almacen a = new AlmacenMapper().desdeJson(new JSONObject(
                "{\"idAlmacen\":3,\"nombre\":\"Bodega Norte\",\"ubicacion\":\"Calle 10\",\"ciudad\":null,"
                        + "\"capacidadMaxima\":null,\"estado\":\"activo\"}"));
        assertNull(a.getCapacidadMaxima());
        assertNull(a.getCiudad());
        assertTrue(new AlmacenMapper().aJson(a).isNull("capacidadMaxima"));
    }

    @Test
    public void usuarioSinContrasenaYCuerpoDeLogin() throws Exception {
        Usuario u = new UsuarioMapper().desdeJson(new JSONObject(
                "{\"idUsuario\":1,\"nombre\":\"Miguel\",\"apellido\":\"Correa\",\"correoElectronico\":"
                        + "\"mdch2077@gmail.com\",\"telefono\":\"3115861318\",\"estado\":\"activo\",\"rol\":\"usuario\"}"));
        assertEquals("Miguel Correa", u.getNombreCompleto());
        assertFalse(new UsuarioMapper().aJson(u).has("contrasena"));
        JSONObject login = UsuarioMapper.credenciales(" mdch2077@gmail.com ", "1234");
        assertEquals("mdch2077@gmail.com", login.getString("correoElectronico"));
        assertEquals("1234", login.getString("contrasena"));
    }

    @Test
    public void movimientoParaRegistrar() throws Exception {
        Movimiento m = new Movimiento();
        m.setIdProducto(1);
        m.setIdAlmacen(3);
        m.setIdUsuario(1);
        m.setTipoMovimiento(Movimiento.SALIDA);
        m.setCantidad(2);
        JSONObject json = new MovimientoMapper().aJson(m);
        assertEquals("salida", json.getString("tipoMovimiento"));
        assertEquals(2, json.getInt("cantidad"));
        assertTrue(json.isNull("motivo"));
    }

    @Test
    public void mensajeDeErrorDeLaApi() {
        assertEquals("Correo o contraseña incorrectos",
                ApiException.extraerMensaje(401, "{\"error\":\"Correo o contraseña incorrectos\"}"));
        assertEquals("El registro no existe", ApiException.extraerMensaje(404, "<html>"));
        assertEquals("Error del servidor (código 500)", ApiException.extraerMensaje(500, ""));
    }
}
