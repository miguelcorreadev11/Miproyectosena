package com.archivex.movil.ui.catalogos;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Proveedor;
import com.archivex.movil.ui.comun.FormularioDialog;
import com.archivex.movil.ui.comun.ListaActivity;
import com.archivex.movil.ui.comun.ListaAdapter;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.FiltroTexto;
import com.archivex.movil.util.Formato;
import com.archivex.movil.util.Validador;

/** CRUD de proveedores: tocar = editar, mantener presionado = eliminar. */
public class ProveedoresActivity extends ListaActivity<Proveedor> {

    private final Repositorio<Proveedor> repositorio = Repositorios.proveedores();

    @Override
    protected String titulo() {
        return getString(R.string.proveedores);
    }

    @Override
    protected Repositorio<Proveedor> repositorio() {
        return repositorio;
    }

    @Override
    protected ListaAdapter.Presentador<Proveedor> presentador() {
        return new ListaAdapter.Presentador<Proveedor>() {
            @Override public String titulo(Proveedor p) { return p.getNombre(); }
            @Override public String subtitulo(Proveedor p) { return Formato.unir(p.getCorreo(), p.getTelefono()); }
            @Override public String detalle(Proveedor p) { return Formato.unir(p.getDireccion(), p.getCiudad(), p.getPais()); }
            @Override public String insignia(Proveedor p) { return Formato.capitalizar(p.getEstado()); }
            @Override public int colorInsignia(Proveedor p) { return UiUtil.colorEstado(p.getEstado()); }
        };
    }

    @Override
    protected boolean coincide(Proveedor p, String filtro) {
        return FiltroTexto.coincide(filtro, p.getNombre(), p.getCorreo(), p.getCiudad(), p.getPais());
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
    protected void alTocar(Proveedor proveedor) {
        abrirFormulario(proveedor);
    }

    @Override
    protected void alMantenerPresionado(Proveedor proveedor) {
        confirmarEliminar(proveedor, proveedor.getNombre());
    }

    private void abrirFormulario(Proveedor existente) {
        boolean nuevo = existente == null;
        new FormularioDialog(this, nuevo ? "Nuevo proveedor" : "Editar proveedor")
                .campo("nombre", getString(R.string.nombre), FormularioDialog.TEXTO, true,
                        nuevo ? null : existente.getNombre())
                .campo("correo", getString(R.string.correo), FormularioDialog.CORREO, false,
                        nuevo ? null : existente.getCorreo(),
                        valor -> Validador.esCorreoValido(valor) ? null : "Correo no válido")
                .campo("telefono", "Teléfono", FormularioDialog.TELEFONO, false,
                        nuevo ? null : existente.getTelefono(),
                        valor -> Validador.esTelefonoValido(valor) ? null : "Teléfono no válido")
                .campo("direccion", "Dirección", FormularioDialog.TEXTO, false,
                        nuevo ? null : existente.getDireccion())
                .campo("ciudad", "Ciudad", FormularioDialog.TEXTO, false,
                        nuevo ? null : existente.getCiudad())
                .campo("pais", "País", FormularioDialog.TEXTO, false,
                        nuevo ? null : existente.getPais())
                .mostrar((valores, dialogo) -> {
                    Proveedor proveedor = nuevo ? new Proveedor() : existente;
                    proveedor.setNombre(valores.get("nombre"));
                    proveedor.setCorreo(valores.get("correo"));
                    proveedor.setTelefono(valores.get("telefono"));
                    proveedor.setDireccion(valores.get("direccion"));
                    proveedor.setCiudad(valores.get("ciudad"));
                    proveedor.setPais(valores.get("pais"));
                    Resultado<Proveedor> alTerminar = new Resultado<Proveedor>() {
                        @Override public void onExito(Proveedor datos) {
                            dialogo.dismiss();
                            mostrarMensaje(R.string.guardado);
                            cargar();
                        }

                        @Override public void onError(String mensaje) {
                            mostrarMensaje(mensaje);
                        }
                    };
                    if (nuevo) repositorio.crear(proveedor, alTerminar);
                    else repositorio.actualizar(proveedor.getId(), proveedor, alTerminar);
                });
    }
}
