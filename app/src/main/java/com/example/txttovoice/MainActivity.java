package com.example.txttovoice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.os.Vibrator;

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
            Vibrator vib = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (vib != null) vib.vibrate(80);
            if (!enPausa) {
                Voz1.stop();
            }
        });
        ImageButton btnRepetir = findViewById(R.id.btnRepetir);
        btnRepetir.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UltimaFrase", MODE_PRIVATE);
            String ultima = prefs.getString("frase", "");
            if (!ultima.isEmpty()) {
                Voz1.speak(ultima, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        SeekBar tonoBar = findViewById(R.id.seekBarPitch);
        SeekBar velBar = findViewById(R.id.seekBarSpeed);

        final float[] tono = {1.0f};
        final float[] vel = {1.0f};

        tonoBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tono[0] = (progress / 50f);
                if (tono[0] < 0.5f) tono[0] = 0.5f;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        velBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vel[0] = (progress / 50f);
                if (vel[0] < 0.5f) vel[0] = 0.5f;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

// Y en el evento hablar:
        Boton.setOnClickListener(v -> {
            String TextoString = Texto.getText().toString();
            Voz1.setPitch(tono[0]);
            Voz1.setSpeechRate(vel[0]);
            Voz1.speak(TextoString, TextToSpeech.QUEUE_FLUSH, null);

            // Guardar última frase
            SharedPreferences prefs = getSharedPreferences("UltimaFrase", MODE_PRIVATE);
            prefs.edit().putString("frase", TextoString).apply();
        });


    }
}
