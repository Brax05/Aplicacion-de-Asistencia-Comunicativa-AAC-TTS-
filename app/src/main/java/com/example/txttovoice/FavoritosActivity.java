package com.example.txttovoice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FavoritosActivity extends AppCompatActivity {

    private TextToSpeech voz;
    private List<String> listaFavoritos;
    private FrasesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        RecyclerView recycler = findViewById(R.id.recyclerFavoritos);

        SharedPreferences prefs = getSharedPreferences("FavoritoFrases", MODE_PRIVATE);
        Set<String> favoritos = prefs.getStringSet("favoritos", new HashSet<>());
        listaFavoritos = new ArrayList<>(favoritos);

        voz = new TextToSpeech(this, estado -> {
            if (estado != TextToSpeech.ERROR) {
                voz.setLanguage(new Locale("es"));
            }
        });

        adapter = new FrasesAdapter(
                listaFavoritos,
                frase -> voz.speak(frase, TextToSpeech.QUEUE_FLUSH, null),
                frase -> Toast.makeText(this, "Long press: " + frase, Toast.LENGTH_SHORT).show(),
                position -> {
                    String fraseEliminada = listaFavoritos.get(position);
                    listaFavoritos.remove(position);
                    SharedPreferences.Editor editor = prefs.edit();
                    Set<String> nuevosFavoritos = new HashSet<>(listaFavoritos);
                    editor.putStringSet("favoritos", nuevosFavoritos);
                    editor.apply();
                    adapter.removeItem(position);
                    Toast.makeText(this, "Frase eliminada de favoritos", Toast.LENGTH_SHORT).show();
                },
                this // <= contexto (Activity)
        );

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voz != null) {
            voz.stop();
            voz.shutdown();
        }
    }
}
