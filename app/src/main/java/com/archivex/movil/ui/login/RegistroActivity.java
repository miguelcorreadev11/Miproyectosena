package com.archivex.movil.ui.login;

import android.os.Bundle;
import android.widget.Toast;

import com.archivex.movil.R;
import com.archivex.movil.data.api.Resultado;
import com.archivex.movil.data.repositorio.Repositorios;
import com.archivex.movil.modelo.Usuario;
import com.archivex.movil.ui.comun.BaseActivity;
import com.archivex.movil.ui.comun.UiUtil;
import com.archivex.movil.util.Validador;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

/** Registro de usuario: mismas reglas que Register_control del escritorio, guardado con POST /usuarios. */
public class RegistroActivity extends BaseActivity {

    private TextInputLayout capaNombre, capaApellido, capaCorreo, capaTelefono, capaContrasena, capaConfirmar;
    private MaterialButton botonRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        configurarBarra(getString(R.string.registro_titulo), true);
        capaNombre = findViewById(R.id.capaNombre);
        capaApellido = findViewById(R.id.capaApellido);
        capaCorreo = findViewById(R.id.capaCorreo);
        capaTelefono = findViewById(R.id.capaTelefono);
        capaContrasena = findViewById(R.id.capaContrasena);
        capaConfirmar = findViewById(R.id.capaConfirmar);
        botonRegistrar = findViewById(R.id.botonRegistrar);
        botonRegistrar.setOnClickListener(v -> registrar());
    }

    private void registrar() {
        String obligatorio = getString(R.string.campo_obligatorio);
        String nombre = UiUtil.texto(capaNombre);
        String apellido = UiUtil.texto(capaApellido);
        String correo = UiUtil.texto(capaCorreo);
        String telefono = UiUtil.texto(capaTelefono);
        String contrasena = UiUtil.texto(capaContrasena);
        String confirmar = UiUtil.texto(capaConfirmar);

        boolean valido = UiUtil.marcar(capaNombre, !Validador.estaVacio(nombre), obligatorio);
        valido &= UiUtil.marcar(capaApellido, !Validador.estaVacio(apellido), obligatorio);
        valido &= UiUtil.marcar(capaCorreo, Validador.esCorreoValido(correo), "El correo electrónico no es válido");
        valido &= UiUtil.marcar(capaTelefono, Validador.esTelefonoValido(telefono), "Teléfono no válido");
        valido &= UiUtil.marcar(capaContrasena, Validador.esContrasenaValida(contrasena),
                "La contraseña debe tener al menos " + Validador.LONGITUD_MINIMA_CONTRASENA + " caracteres");
        valido &= UiUtil.marcar(capaConfirmar, contrasena.equals(confirmar), "Las contraseñas no coinciden");
        if (!valido) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreoElectronico(correo);
        usuario.setTelefono(telefono);
        usuario.setRol(Usuario.ROL_USUARIO);
        usuario.setEstado(Usuario.ESTADO_ACTIVO);

        botonRegistrar.setEnabled(false);
        UiUtil.visible(findViewById(R.id.progreso), true);
        Repositorios.usuarios().registrar(usuario, contrasena, new Resultado<Usuario>() {
            @Override
            public void onExito(Usuario creado) {
                Toast.makeText(RegistroActivity.this, R.string.registro_exitoso, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String mensaje) {
                botonRegistrar.setEnabled(true);
                UiUtil.visible(findViewById(R.id.progreso), false);
                mostrarMensaje(mensaje);
            }
        });
    }
}
