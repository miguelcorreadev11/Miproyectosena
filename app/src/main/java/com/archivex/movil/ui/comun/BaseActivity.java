package com.archivex.movil.ui.comun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.archivex.movil.R;
import com.archivex.movil.ui.login.LoginActivity;
import com.archivex.movil.util.SesionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

/**
 * Comportamiento común de todas las pantallas: barra superior, mensajes y control de sesión.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected SesionManager sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sesion = SesionManager.getInstancia(this);
    }

    /** Muestra el título en la barra superior y, si se pide, la flecha para volver. */
    protected void configurarBarra(String titulo, boolean conRegreso) {
        MaterialToolbar barra = findViewById(R.id.barraSuperior);
        if (barra == null) {
            return;
        }
        barra.setTitle(titulo);
        if (conRegreso) {
            barra.setNavigationIcon(R.drawable.ic_atras);
            barra.setNavigationContentDescription(R.string.volver);
            barra.setNavigationOnClickListener(v -> finish());
        }
    }

    /** Si no hay sesión, vuelve al inicio de sesión y devuelve false. */
    protected boolean exigirSesion() {
        if (sesion.haySesion()) {
            return true;
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return false;
    }

    protected void mostrarMensaje(String mensaje) {
        View raiz = findViewById(android.R.id.content);
        Snackbar.make(raiz, mensaje, Snackbar.LENGTH_LONG).show();
    }

    protected void mostrarMensaje(int recursoTexto) {
        mostrarMensaje(getString(recursoTexto));
    }
}
