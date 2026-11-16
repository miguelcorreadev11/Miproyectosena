package com.archivex.movil.modelo;

/** Almacén o bodega (tabla almacenes). */
public class Almacen implements Entidad {

    private int idAlmacen;
    private String nombre;
    private String ubicacion;
    private String ciudad;
    private Integer capacidadMaxima;
    private String estado = "activo";

    @Override
    public int getId() {
        return idAlmacen;
    }

    public int getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(int idAlmacen) { this.idAlmacen = idAlmacen; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nombre;
    }
}
