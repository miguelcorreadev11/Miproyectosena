package com.archivex.movil.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.data.repositorio.UsuarioRepositorio;
import com.archivex.movil.modelo.Usuario;
import com.archivex.movil.ui.comun.BaseActivity;
import com.archivex.movil.ui.comun.FormularioDialog;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.ui.menu.MenuActivity;
import com.archivex.movil.util.Validador;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

/** Módulo de acceso: inicio de sesión contra POST /usuarios/login. */
public class LoginActivity extends BaseActivity {

    private TextInputLayout capaCorreo;
    private TextInputLayout capaContrasena;
    private MaterialButton botonIngresar;
    private TextView textoServidor;
    private final UsuarioRepositorio usuarios = Repositorios.usuarios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (sesion.haySesion()) {
            abrirMenu();
            return;
        }
        setContentView(R.layout.activity_login);
        capaCorreo = findViewById(R.id.capaCorreo);
        capaContrasena = findViewById(R.id.capaContrasena);
        botonIngresar = findViewById(R.id.botonIngresar);
        textoServidor = findViewById(R.id.textoServidor);

        botonIngresar.setOnClickListener(v -> ingresar());
        findViewById(R.id.botonRegistro).setOnClickListener(v ->
                startActivity(new Intent(this, RegistroActivity.class)));
        textoServidor.setOnClickListener(v -> configurarServidor());
        actualizarTextoServidor();
    }

    private void ingresar() {
        String correo = UiUtil.texto(capaCorreo);
        String contrasena = UiUtil.texto(capaContrasena);
        boolean valido = UiUtil.marcar(capaCorreo, Validador.esCorreoValido(correo), "Escribe un correo válido");
        valido &= UiUtil.marcar(capaContrasena, !Validador.estaVacio(contrasena), getString(R.string.campo_obligatorio));
        if (!valido) {
            return;
        }
        cargando(true);
        usuarios.iniciarSesion(correo, contrasena, new Resultado<Usuario>() {
            @Override
            public void onExito(Usuario usuario) {
                cargando(false);
                sesion.iniciarSesion(usuario);
                abrirMenu();
            }

            @Override
            public void onError(String mensaje) {
                cargando(false);
                mostrarMensaje(mensaje);
            }
        });
    }

    /** Permite cambiar la IP del servidor sin recompilar la aplicación. */
    private void configurarServidor() {
        new FormularioDialog(this, getString(R.string.servidor_titulo))
                .campo("host", "IP o nombre del servidor", FormularioDialog.DIRECCION_WEB, true, sesion.getHost(),
                        valor -> Validador.esHostValido(valor) ? null : getString(R.string.servidor_invalido))
                .ayuda(getString(R.string.servidor_ayuda))
                .mostrar((valores, dialogo) -> {
                    sesion.guardarHost(valores.get("host"));
                    actualizarTextoServidor();
                    dialogo.dismiss();
                });
    }

    private void actualizarTextoServidor() {
        textoServidor.setText(getString(R.string.configurar_servidor, sesion.getHost()));
    }

    private void cargando(boolean activo) {
        botonIngresar.setEnabled(!activo);
        UiUtil.visible(findViewById(R.id.progreso), activo);
    }

    private void abrirMenu() {
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}
