package com.archivex.movil.modelo;

/** Movimiento de inventario (tabla movimientoinventario). */
public class Movimiento implements Entidad {

    public static final String ENTRADA = "entrada";
    public static final String SALIDA = "salida";
    public static final String AJUSTE = "ajuste";
    public static final String DEVOLUCION = "devolucion";

    /** Tipos en el mismo orden del ENUM de MySQL. */
    public static final String[] TIPOS = {ENTRADA, SALIDA, AJUSTE, DEVOLUCION};

    private int idMovimiento;
    private int idProducto;
    private String nombreProducto;
    private int idAlmacen;
    private String nombreAlmacen;
    private int idUsuario;
    private String nombreUsuario;
    private String tipoMovimiento;
    private int cantidad;
    private Integer numeroPedido;
    private String motivo;
    private String fechaMovimiento;

    @Override
    public int getId() {
        return idMovimiento;
    }

    public static boolean esTipoValido(String tipo) {
        for (String valido : TIPOS) {
            if (valido.equals(tipo)) {
                return true;
            }
        }
        return false;
    }

    /** En un ajuste la cantidad es la existencia contada (puede ser 0); en los demás debe ser mayor que 0. */
    public static boolean esCantidadValida(String tipo, int cantidad) {
        return AJUSTE.equals(tipo) ? cantidad >= 0 : cantidad > 0;
    }

    /** Signo con que se muestra la cantidad: + para entradas y devoluciones, - para salidas, = para ajustes. */
    public String getSigno() {
        if (SALIDA.equals(tipoMovimiento)) return "-";
        if (AJUSTE.equals(tipoMovimiento)) return "=";
        return "+";
    }

    public boolean vieneDeCompra() {
        return numeroPedido != null;
    }

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public int getIdAlmacen() { return idAlmacen; }
    public void setIdAlmacen(int idAlmacen) { this.idAlmacen = idAlmacen; }
    public String getNombreAlmacen() { return nombreAlmacen; }
    public void setNombreAlmacen(String nombreAlmacen) { this.nombreAlmacen = nombreAlmacen; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Integer getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(Integer numeroPedido) { this.numeroPedido = numeroPedido; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(String fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
}
