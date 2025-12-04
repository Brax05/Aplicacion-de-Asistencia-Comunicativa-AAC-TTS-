package com.example.txttovoice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CarpetaActivity extends AppCompatActivity {

    private EditText editFrase;
    private ImageButton btnAgregarFrase;
    private RecyclerView recyclerFrases;
    private TextView titulo;
    private TextToSpeech Voz1;
    private List<String> frases;
    private String carpetaSeleccionada;
    private FrasesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpetas);

        recyclerFrases = findViewById(R.id.recyclerFrases);
        editFrase = findViewById(R.id.editFrase);
        btnAgregarFrase = findViewById(R.id.btnAgregarFrase);
        titulo = findViewById(R.id.tituloCarpeta);

        carpetaSeleccionada = getIntent().getStringExtra("carpeta");
        titulo.setText(carpetaSeleccionada);

        Voz1 = new TextToSpeech(this, estado -> {
            if (estado != TextToSpeech.ERROR)
                Voz1.setLanguage(new Locale("es"));
        });

        // Cargar frases desde SharedPreferences en la lista base
        frases = new ArrayList<>(getPhrasesFromFolder(carpetaSeleccionada));

        adapter = new FrasesAdapter(
                frases,
                frase -> onFraseClicked(frase),
                frase -> {},
                position -> {
                    // 1) Actualizar la lista base
                    frases.remove(position);
                    // 2) Actualizar el adapter
                    adapter.removeItem(position);
                    // 3) Guardar cambios
                    savePhrases();
                },
                this
        );

        recyclerFrases.setLayoutManager(new LinearLayoutManager(this));
        recyclerFrases.setAdapter(adapter);

        btnAgregarFrase.setOnClickListener(v -> {
            String nuevaFrase = editFrase.getText().toString().trim();
            Vibrator vib = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vib != null) vib.vibrate(80);

            if (!nuevaFrase.isEmpty() && !frases.contains(nuevaFrase)) {
                // 1) Actualizar la lista base
                frases.add(nuevaFrase);
                // 2) Actualizar el adapter
                adapter.addItem(nuevaFrase);
                // 3) Guardar cambios
                savePhrases();
                editFrase.setText("");
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                // 1) Actualizar la lista base
                if (from >= 0 && to >= 0 && from < frases.size() && to < frases.size()) {
                    String tmp = frases.get(from);
                    frases.set(from, frases.get(to));
                    frases.set(to, tmp);
                }

                // 2) Actualizar el adapter
                adapter.swapItems(from, to);
                // 3) Guardar cambios
                savePhrases();
                return true;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}
        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerFrases);
    }

    private void onFraseClicked(String frase) {
        Voz1.speak(frase, TextToSpeech.QUEUE_FLUSH, null);

        SharedPreferences prefs = getSharedPreferences("HistorialFrases", MODE_PRIVATE);
        Set<String> historial = prefs.getStringSet("historial", new HashSet<>());
        // copiar para evitar problemas con el Set devuelto
        Set<String> nuevoHistorial = new HashSet<>(historial);
        nuevoHistorial.add(frase);
        prefs.edit().putStringSet("historial", nuevoHistorial).apply();

        SharedPreferences ultima = getSharedPreferences("UltimaFrase", MODE_PRIVATE);
        ultima.edit().putString("frase", frase).apply();
    }

    private Set<String> getPhrasesFromFolder(String folderName) {
        SharedPreferences prefs = getSharedPreferences("Carpetas", MODE_PRIVATE);
        return prefs.getStringSet(folderName, new HashSet<>());
    }

    private void savePhrases() {
        SharedPreferences prefs = getSharedPreferences("Carpetas", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(carpetaSeleccionada, new HashSet<>(frases));
        editor.apply();
    }
}
