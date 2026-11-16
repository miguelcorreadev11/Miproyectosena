package com.archivex.movil.modelo;

/** Compra o pedido a un proveedor (tabla compras). En el móvil solo se consulta. */
public class Compra implements Entidad {

    private int idCompra;
    private int numeroPedido;
    private int idProveedor;
    private String nombreProveedor;
    private int idAlmacen;
    private String nombreAlmacen;
    private String fechaPedido;
    private String fechaEntrega;
    private double valorTotal;
    private String estado;
    private String comentarios;

    @Override
    public int getId() {
        return idCompra;
    }

    public static final String PENDIENTE = "pendiente";
    public static final String ENTREGADO = "entregado";
    public static final String CANCELADO = "cancelado";

    public boolean estaEntregada() {
        return ENTREGADO.equalsIgnoreCase(estado);
    }

    public boolean estaPendiente() {
        return PENDIENTE.equalsIgnoreCase(estado);
    }

    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }
    public int getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(int numeroPedido) { this.numeroPedido = numeroPedido; }
    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }
    public int getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(int idAlmacen) { this.idAlmacen = idAlmacen; }
    public String getNombreAlmacen() { return nombreAlmacen; }
    public void setNombreAlmacen(String nombreAlmacen) { this.nombreAlmacen = nombreAlmacen; }
    public String getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(String fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
}
