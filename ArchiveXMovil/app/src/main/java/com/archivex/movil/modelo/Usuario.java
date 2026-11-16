package com.archivex.movil.modelo;

/** Usuario de ArchiveX (tabla usuarios). La contraseña nunca se guarda en el teléfono. */
public class Usuario implements Entidad {

    public static final String ROL_USUARIO = "usuario";
    public static final String ROL_ADMINISTRADOR = "administrador";
    public static final String ESTADO_ACTIVO = "activo";

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String telefono;
    private String estado = ESTADO_ACTIVO;
    private String rol = ROL_USUARIO;
    private String fechaCreacion;

    @Override
    public int getId() {
        return idUsuario;
    }

    public String getNombreCompleto() {
        String completo = (nombre == null ? "" : nombre) + " " + (apellido == null ? "" : apellido);
        return completo.trim();
    }

    public boolean esAdministrador() {
        return ROL_ADMINISTRADOR.equalsIgnoreCase(rol);
    }

    public boolean estaActivo() {
        return ESTADO_ACTIVO.equalsIgnoreCase(estado);
    }

    /** Texto del rol como en el escritorio: "Administrador" o "Usuario". */
    public String getRolTexto() {
        return esAdministrador() ? "Administrador" : "Usuario";
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
