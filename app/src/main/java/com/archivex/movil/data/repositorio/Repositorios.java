package com.archivex.movil.data.repositorio;

import com.archivex.movil.data.api.ApiConfig.Recurso;
import com.archivex.movil.data.mapper.AlmacenMapper;
import com.archivex.movil.data.mapper.CategoriaMapper;
import com.archivex.movil.data.mapper.CompraMapper;
import com.archivex.movil.data.mapper.MovimientoMapper;
import com.archivex.movil.data.mapper.ProductoMapper;
import com.archivex.movil.data.mapper.ProveedorMapper;
import com.archivex.movil.modelo.Almacen;
import com.archivex.movil.modelo.Categoria;
import com.archivex.movil.modelo.Compra;
import com.archivex.movil.modelo.Movimiento;
import com.archivex.movil.modelo.Producto;
import com.archivex.movil.modelo.Proveedor;

/**
 * Fábrica de repositorios (patrón Factory): las pantallas piden el repositorio por entidad
 * y no necesitan saber qué puerto ni qué mapper usa.
 */
public final class Repositorios {

    private Repositorios() {
    }

    public static UsuarioRepositorio usuarios() {
        return new UsuarioRepositorio();
    }

    public static Repositorio<Producto> productos() {
        return new Repositorio<>(Recurso.PRODUCTOS, new ProductoMapper());
    }

    public static Repositorio<Categoria> categorias() {
        return new Repositorio<>(Recurso.CATEGORIAS, new CategoriaMapper());
    }

    public static Repositorio<Almacen> almacenes() {
        return new Repositorio<>(Recurso.ALMACENES, new AlmacenMapper());
    }

    public static Repositorio<Proveedor> proveedores() {
        return new Repositorio<>(Recurso.PROVEEDORES, new ProveedorMapper());
    }

    public static Repositorio<Movimiento> movimientos() {
        return new Repositorio<>(Recurso.MOVIMIENTOS, new MovimientoMapper());
    }

    public static Repositorio<Compra> compras() {
        return new Repositorio<>(Recurso.COMPRAS, new CompraMapper());
    }
}
