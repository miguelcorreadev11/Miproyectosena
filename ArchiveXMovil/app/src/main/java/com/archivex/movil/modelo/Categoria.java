package com.archivex.movil.modelo;

/** Categoría de productos (tabla categorias). */
public class Categoria implements Entidad {

    private int idCategoria;
    private String nombre;
    private String descripcion;
    private String estado = "activo";

    @Override
    public int getId() {
        return idCategoria;
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return nombre;
    }
}
