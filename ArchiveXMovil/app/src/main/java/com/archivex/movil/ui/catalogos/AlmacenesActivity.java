package com.archivex.movil.ui.catalogos;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorio;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Almacen;
import com.archivex.movil.ui.comun.FormularioDialog;
import com.archivex.movil.ui.comun.ListaActivity;
import com.archivex.movil.ui.comun.ListaAdapter;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.FiltroTexto;
import com.archivex.movil.util.Formato;
import com.archivex.movil.util.Validador;

/** CRUD de almacenes: tocar = editar, mantener presionado = eliminar. */
public class AlmacenesActivity extends ListaActivity<Almacen> {

    private final Repositorio<Almacen> repositorio = Repositorios.almacenes();

    @Override
    protected String titulo() {
        return getString(R.string.almacenes);
    }

    @Override
    protected Repositorio<Almacen> repositorio() {
        return repositorio;
    }

    @Override
    protected ListaAdapter.Presentador<Almacen> presentador() {
        return new ListaAdapter.Presentador<Almacen>() {
            @Override public String titulo(Almacen a) { return a.getNombre(); }
            @Override public String subtitulo(Almacen a) { return Formato.unir(a.getUbicacion(), a.getCiudad()); }
            @Override public String detalle(Almacen a) {
                return a.getCapacidadMaxima() == null ? "Capacidad sin definir"
                        : "Capacidad máxima: " + a.getCapacidadMaxima() + " und";
            }
            @Override public String insignia(Almacen a) { return Formato.capitalizar(a.getEstado()); }
            @Override public int colorInsignia(Almacen a) { return UiUtil.colorEstado(a.getEstado()); }
        };
    }

    @Override
    protected boolean coincide(Almacen a, String filtro) {
        return FiltroTexto.coincide(filtro, a.getNombre(), a.getUbicacion(), a.getCiudad());
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
    protected void alTocar(Almacen almacen) {
        abrirFormulario(almacen);
    }

    @Override
    protected void alMantenerPresionado(Almacen almacen) {
        confirmarEliminar(almacen, almacen.getNombre());
    }

    private void abrirFormulario(Almacen existente) {
        boolean nuevo = existente == null;
        String capacidad = nuevo || existente.getCapacidadMaxima() == null ? null
                : String.valueOf(existente.getCapacidadMaxima());
        new FormularioDialog(this, nuevo ? "Nuevo almacén" : "Editar almacén")
                .campo("nombre", getString(R.string.nombre), FormularioDialog.TEXTO, true,
                        nuevo ? null : existente.getNombre())
                .campo("ubicacion", "Ubicación", FormularioDialog.TEXTO, true,
                        nuevo ? null : existente.getUbicacion())
                .campo("ciudad", "Ciudad", FormularioDialog.TEXTO, false,
                        nuevo ? null : existente.getCiudad())
                .campo("capacidad", "Capacidad máxima", FormularioDialog.ENTERO, false, capacidad,
                        valor -> Validador.esEnteroPositivo(valor) ? null : "Debe ser un número mayor que cero")
                .mostrar((valores, dialogo) -> {
                    Almacen almacen = nuevo ? new Almacen() : existente;
                    almacen.setNombre(valores.get("nombre"));
                    almacen.setUbicacion(valores.get("ubicacion"));
                    almacen.setCiudad(valores.get("ciudad"));
                    almacen.setCapacidadMaxima(Validador.aEntero(valores.get("capacidad")));
                    Resultado<Almacen> alTerminar = new Resultado<Almacen>() {
                        @Override public void onExito(Almacen datos) {
                            dialogo.dismiss();
                            mostrarMensaje(R.string.guardado);
                            cargar();
                        }

                        @Override public void onError(String mensaje) {
                            mostrarMensaje(mensaje);
                        }
                    };
                    if (nuevo) repositorio.crear(almacen, alTerminar);
                    else repositorio.actualizar(almacen.getId(), almacen, alTerminar);
                });
    }
}
