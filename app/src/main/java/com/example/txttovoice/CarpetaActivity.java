package com.example.txttovoice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ImageView basureroView;
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
        TextView titulo = findViewById(R.id.tituloCarpeta);

        carpetaSeleccionada = getIntent().getStringExtra("carpeta");
        titulo.setText(carpetaSeleccionada);

        Voz1 = new TextToSpeech(this, estado -> {
            if (estado != TextToSpeech.ERROR)
                Voz1.setLanguage(new Locale("es"));
        });

        frases = new ArrayList<>(getPhrasesFromFolder(carpetaSeleccionada));
        adapter = new FrasesAdapter(frases,
                frase -> onFraseClicked(frase),
                frase -> {},
                position -> {
                    adapter.removeItem(position);
                    savePhrases();
                });

        recyclerFrases.setLayoutManager(new LinearLayoutManager(this));
        recyclerFrases.setAdapter(adapter);

        btnAgregarFrase.setOnClickListener(v -> {
            String nuevaFrase = editFrase.getText().toString().trim();
            if (!nuevaFrase.isEmpty() && !frases.contains(nuevaFrase)) {
                adapter.addItem(nuevaFrase);
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
                adapter.swapItems(from, to);
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
