package com.archivex.movil.modelo;

/** Producto del inventario (tabla productos). */
public class Producto implements Entidad {

    /** Existencia desde la cual se considera que hay pocas unidades. */
    public static final int STOCK_MINIMO = 10;

    private int idProducto;
    private String nombre;
    private String descripcion;
    private int idCategoria;
    private String nombreCategoria;
    private double precioCompra;
    private double precioVenta;
    private int cantidad;
    private String estado = "activo";
    private String imagen;
    private String fechaCreacion;

    @Override
    public int getId() {
        return idProducto;
    }

    public boolean tieneStockBajo() {
        return cantidad <= STOCK_MINIMO;
    }

    public boolean estaAgotado() {
        return cantidad == 0;
    }

    public boolean estaActivo() {
        return "activo".equalsIgnoreCase(estado);
    }

    /** Ganancia por unidad: precio de venta menos precio de compra. */
    public double getGananciaUnitaria() {
        return precioVenta - precioCompra;
    }

    /** Margen de ganancia sobre el precio de venta, en porcentaje (0 si el precio de venta es 0). */
    public double getMargenPorcentaje() {
        if (precioVenta <= 0) {
            return 0;
        }
        return (getGananciaUnitaria() / precioVenta) * 100.0;
    }

    /** Valor del inventario de este producto a precio de compra. */
    public double getValorInventario() {
        return precioCompra * cantidad;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return nombre;
    }
}
