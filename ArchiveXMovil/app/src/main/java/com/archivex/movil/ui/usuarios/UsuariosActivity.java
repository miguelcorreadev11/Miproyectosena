package com.archivex.movil.ui.usuarios;

import com.archivex.movil.R;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Usuario;
import com.archivex.movil.ui.comun.Dialogos;
import com.archivex.movil.ui.comun.ListaActivity;
import com.archivex.movil.ui.comun.ListaAdapter;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.FiltroTexto;
import com.archivex.movil.util.Formato;

import java.util.LinkedHashMap;
import java.util.Map;

/** Consulta de usuarios registrados (la API nunca devuelve contraseñas). */
public class UsuariosActivity extends ListaActivity<Usuario> {

    private final Repositorio<Usuario> repositorio = Repositorios.usuarios();

    @Override
    protected String titulo() {
        return getString(R.string.modulo_usuarios);
    }

    @Override
    protected Repositorio<Usuario> repositorio() {
        return repositorio;
    }

    @Override
    protected ListaAdapter.Presentador<Usuario> presentador() {
        return new ListaAdapter.Presentador<Usuario>() {
            @Override public String titulo(Usuario u) { return u.getNombreCompleto(); }
            @Override public String subtitulo(Usuario u) { return u.getCorreoElectronico(); }
            @Override public String detalle(Usuario u) { return Formato.unir(u.getRolTexto(), u.getTelefono()); }
            @Override public String insignia(Usuario u) { return Formato.capitalizar(u.getEstado()); }
            @Override public int colorInsignia(Usuario u) { return UiUtil.colorEstado(u.getEstado()); }
        };
    }

    @Override
    protected boolean coincide(Usuario u, String filtro) {
        return FiltroTexto.coincide(filtro, u.getNombreCompleto(), u.getCorreoElectronico(), u.getRolTexto());
    }

    @Override
    protected void alTocar(Usuario u) {
        Map<String, String> filas = new LinkedHashMap<>();
        filas.put("Correo", u.getCorreoElectronico());
        filas.put("Teléfono", u.getTelefono());
        filas.put("Rol", u.getRolTexto());
        filas.put("Estado", Formato.capitalizar(u.getEstado()));
        filas.put("Registrado", Formato.fecha(u.getFechaCreacion()));
        Dialogos.detalle(this, u.getNombreCompleto(), filas);
    }
}
