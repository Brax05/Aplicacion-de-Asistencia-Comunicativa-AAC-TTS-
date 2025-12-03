package com.example.txttovoice;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageButton Boton, Stop;
    EditText Texto;
    TextToSpeech Voz1;
    boolean enPausa = false;
    Spinner spinnerIdiomas;

    String[] idiomas = {"Español", "Inglés", "Japonés"};
    Locale[] locales = {new Locale("es", "ES"), Locale.US, Locale.JAPANESE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Voz1 = new TextToSpeech(this, estado -> {
            if (estado != TextToSpeech.ERROR) {
                Voz1.setLanguage(locales[0]); // Idioma por defecto
            }
        });

        // Inicializar elementos de UI
        Boton = findViewById(R.id.microphoneButton);
        Texto = findViewById(R.id.Texto);
        Stop = findViewById(R.id.Stop);
        spinnerIdiomas = findViewById(R.id.spinnerIdiomas);

        // Configurar Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, idiomas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdiomas.setAdapter(adapter);
        spinnerIdiomas.setSelection(0); // opción por defecto

        spinnerIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Voz1.setLanguage(locales[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Evento para el botón que habla
        Boton.setOnClickListener(v -> {
            String TextoString = Texto.getText().toString();
            Voz1.speak(TextoString, TextToSpeech.QUEUE_FLUSH, null);
        });

        // Evento para detener la síntesis
        Stop.setOnClickListener(v -> {
            if (!enPausa) {
                Voz1.stop();
            }
        });
    }
}
