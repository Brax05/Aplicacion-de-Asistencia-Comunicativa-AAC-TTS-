package com.example.txttovoice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CarpetasActivity extends AppCompatActivity {

    private EditText editCarpeta;
    private Button btnNuevaCarpeta;
    private RecyclerView recyclerCarpetas;
    private CarpetasAdapter adapter;
    private ImageView basureroView;
    private List<String> carpetas;
    private Spinner spinnerColor;

    private final int[] colorResIds = {
            R.color.carpeta_color_azul,
            R.color.carpeta_color_verde,
            R.color.carpeta_color_amarillo,
            R.color.carpeta_color_naranja,
            R.color.carpeta_color_morado
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_carpetas);

        editCarpeta = findViewById(R.id.editCarpeta);
        btnNuevaCarpeta = findViewById(R.id.btnNuevaCarpeta);
        spinnerColor = findViewById(R.id.spinnerColor);
        recyclerCarpetas = findViewById(R.id.recyclerCarpetas);

        ArrayAdapter<CharSequence> colorAdapter =
                ArrayAdapter.createFromResource(this, R.array.carpeta_colores, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(colorAdapter);

        carpetas = new ArrayList<>(getFolders());

        adapter = new CarpetasAdapter(carpetas,
                carpeta -> onCarpetaClicked(carpeta),
                carpeta -> {},
                position -> {
                    adapter.removeItem(position);
                    saveCarpetas();
                },
                this // <-- IMPORTANTE para favoritos y ordenamiento
        );


        recyclerCarpetas.setLayoutManager(new LinearLayoutManager(this));
        recyclerCarpetas.setAdapter(adapter);

        btnNuevaCarpeta.setOnClickListener(v -> {
            String nombre = editCarpeta.getText().toString().trim();
            int colorIndex = spinnerColor.getSelectedItemPosition();
            int color = getResources().getColor(colorResIds[colorIndex]);
            if (!nombre.isEmpty() && !carpetas.contains(nombre)) {
                adapter.addItem(nombre);
                saveCarpetas();

                SharedPreferences prefs = getSharedPreferences("CarpetasColores", MODE_PRIVATE);
                prefs.edit().putInt(nombre, color).apply();

                editCarpeta.setText("");
            }
        });
    }

    private void onCarpetaClicked(String carpeta) {
        Intent intent = new Intent(this, CarpetaActivity.class);
        intent.putExtra("carpeta", carpeta);
        startActivity(intent);
    }

    private Set<String> getFolders() {
        SharedPreferences prefs = getSharedPreferences("Carpetas", Context.MODE_PRIVATE);
        return prefs.getAll().keySet();
    }

    private void saveCarpetas() {
        SharedPreferences prefs = getSharedPreferences("Carpetas", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (String carpeta : carpetas) {
            Set<String> frases = prefs.getStringSet(carpeta, new HashSet<>());
            editor.putStringSet(carpeta, frases);
        }
        Set<String> currentKeys = prefs.getAll().keySet();
        for (String key : currentKeys) {
            if (!carpetas.contains(key)) editor.remove(key);
        }
        editor.apply();
    }
}
