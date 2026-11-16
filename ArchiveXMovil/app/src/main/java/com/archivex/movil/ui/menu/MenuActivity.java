package com.archivex.movil.ui.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.archivex.movil.R;
import com.archivex.movil.modelo.Usuario;
import com.archivex.movil.ui.catalogos.CatalogosActivity;
import com.archivex.movil.ui.comun.BaseActivity;
import com.archivex.movil.ui.comun.Dialogos;
import com.archivex.movil.ui.comun.ModuloAdapter;
import com.archivex.movil.ui.comun.ModuloAdapter.Modulo;
import com.archivex.movil.ui.compras.ComprasActivity;
import com.archivex.movil.ui.login.LoginActivity;
import com.archivex.movil.ui.movimientos.MovimientosActivity;
import com.archivex.movil.ui.productos.ProductosActivity;
import com.archivex.movil.ui.usuarios.UsuariosActivity;

import java.util.Arrays;
import java.util.List;

/** Menú principal: punto de partida de todos los módulos (mapa de navegación). */
public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!exigirSesion()) return;
        setContentView(R.layout.activity_menu);

        Usuario usuario = sesion.getUsuario();
        ((TextView) findViewById(R.id.textoSaludo)).setText(getString(R.string.saludo, usuario.getNombre()));
        ((TextView) findViewById(R.id.textoRol)).setText(usuario.getRolTexto() + " · " + usuario.getCorreoElectronico());

        List<Modulo> modulos = Arrays.asList(
                new Modulo(R.drawable.ic_lista, R.string.modulo_productos, R.string.desc_productos,
                        () -> abrir(ProductosActivity.class)),
                new Modulo(R.drawable.ic_etiqueta, R.string.modulo_catalogos, R.string.desc_catalogos,
                        () -> abrir(CatalogosActivity.class)),
                new Modulo(R.drawable.ic_movimiento, R.string.modulo_movimientos, R.string.desc_movimientos,
                        () -> abrir(MovimientosActivity.class)),
                new Modulo(R.drawable.ic_compra, R.string.modulo_compras, R.string.desc_compras,
                        () -> abrir(ComprasActivity.class)),
                new Modulo(R.drawable.ic_usuario, R.string.modulo_usuarios, R.string.desc_usuarios,
                        () -> abrir(UsuariosActivity.class)),
                new Modulo(R.drawable.ic_salir, R.string.cerrar_sesion, R.string.confirmar_cerrar_sesion,
                        this::cerrarSesion));

        RecyclerView lista = findViewById(R.id.listaModulos);
        lista.setLayoutManager(new GridLayoutManager(this, 2));
        lista.setAdapter(new ModuloAdapter(modulos));
    }

    private void abrir(Class<?> pantalla) {
        startActivity(new Intent(this, pantalla));
    }

    private void cerrarSesion() {
        Dialogos.confirmar(this, getString(R.string.confirmar_cerrar_sesion), () -> {
            sesion.cerrarSesion();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
