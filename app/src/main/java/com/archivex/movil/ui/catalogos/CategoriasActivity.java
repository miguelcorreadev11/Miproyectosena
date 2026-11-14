package com.archivex.movil.ui.catalogos;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Categoria;
import com.archivex.movil.ui.comun.FormularioDialog;
import com.archivex.movil.ui.comun.ListaActivity;
import com.archivex.movil.ui.comun.ListaAdapter;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.FiltroTexto;
import com.archivex.movil.util.Formato;

/** CRUD de categorías: tocar = editar, mantener presionado = eliminar. */
public class CategoriasActivity extends ListaActivity<Categoria> {

    private final Repositorio<Categoria> repositorio = Repositorios.categorias();

    @Override
    protected String titulo() {
        return getString(R.string.categorias);
    }

    @Override
    protected Repositorio<Categoria> repositorio() {
        return repositorio;
    }

    @Override
    protected ListaAdapter.Presentador<Categoria> presentador() {
        return new ListaAdapter.Presentador<Categoria>() {
            @Override public String titulo(Categoria c) { return c.getNombre(); }
            @Override public String subtitulo(Categoria c) { return c.getDescripcion(); }
            @Override public String detalle(Categoria c) { return "Código " + c.getIdCategoria(); }
            @Override public String insignia(Categoria c) { return Formato.capitalizar(c.getEstado()); }
            @Override public int colorInsignia(Categoria c) { return UiUtil.colorEstado(c.getEstado()); }
        };
    }

    @Override
    protected boolean coincide(Categoria c, String filtro) {
        return FiltroTexto.coincide(filtro, c.getNombre(), c.getDescripcion());
    }

    @Override
    protected boolean permiteCrear() {
        return true;
    }

    @Override
    protected void alCrear() {
        abrirFormulario(null);
    }

    @Override
    protected void alTocar(Categoria categoria) {
        abrirFormulario(categoria);
    }

    @Override
    protected void alMantenerPresionado(Categoria categoria) {
        confirmarEliminar(categoria, categoria.getNombre());
    }

    private void abrirFormulario(Categoria existente) {
        boolean nueva = existente == null;
        new FormularioDialog(this, nueva ? "Nueva categoría" : "Editar categoría")
                .campo("nombre", getString(R.string.nombre), FormularioDialog.TEXTO, true,
                        nueva ? null : existente.getNombre())
                .campo("descripcion", getString(R.string.descripcion), FormularioDialog.TEXTO, false,
                        nueva ? null : existente.getDescripcion())
                .mostrar((valores, dialogo) -> {
                    Categoria categoria = nueva ? new Categoria() : existente;
                    categoria.setNombre(valores.get("nombre"));
                    categoria.setDescripcion(valores.get("descripcion"));
                    Resultado<Categoria> alTerminar = new Resultado<Categoria>() {
                        @Override public void onExito(Categoria datos) {
                            dialogo.dismiss();
                            mostrarMensaje(R.string.guardado);
                            cargar();
                        }

                        @Override public void onError(String mensaje) {
                            mostrarMensaje(mensaje);
                        }
                    };
                    if (nueva) repositorio.crear(categoria, alTerminar);
                    else repositorio.actualizar(categoria.getId(), categoria, alTerminar);
                });
    }
}
