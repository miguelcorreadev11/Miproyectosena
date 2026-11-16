package com.archivex.movil.ui.catalogos;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.archivex.movil.R;
import com.archivex.movil.ui.comun.BaseActivity;
import com.archivex.movil.ui.comun.ModuloAdapter;
import com.archivex.movil.ui.comun.ModuloAdapter.Modulo;

import java.util.Arrays;

/** Submenú de catálogos (igual que Catalogos_Form del escritorio, con sus tres pestañas). */
public class CatalogosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!exigirSesion()) return;
        setContentView(R.layout.activity_catalogos);
        configurarBarra(getString(R.string.modulo_catalogos), true);

        RecyclerView lista = findViewById(R.id.listaModulos);
        lista.setLayoutManager(new GridLayoutManager(this, 2));
        lista.setAdapter(new ModuloAdapter(Arrays.asList(
                new Modulo(R.drawable.ic_etiqueta, R.string.categorias, R.string.desc_catalogos,
                        () -> startActivity(new Intent(this, CategoriasActivity.class))),
                new Modulo(R.drawable.ic_almacen, R.string.almacenes, R.string.desc_catalogos,
                        () -> startActivity(new Intent(this, AlmacenesActivity.class))),
                new Modulo(R.drawable.ic_proveedor, R.string.proveedores, R.string.desc_catalogos,
                        () -> startActivity(new Intent(this, ProveedoresActivity.class))))));
    }
}
