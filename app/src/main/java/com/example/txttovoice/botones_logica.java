package com.example.txttovoice;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Map;

public class botones_logica extends MainActivity {
    private LinearLayout layoutPrincipal;
    private ImageButton botonCrear;
    private SharedPreferences sharedPreferences;
    private EditText editarTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botones_logica);

        layoutPrincipal = findViewById(R.id.layoutPrincipal);
        sharedPreferences = getSharedPreferences("Buttons", Context.MODE_PRIVATE);
        editarTexto = findViewById(R.id.buttonName);
        editarTexto.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)}); // Límite de 12 caracteres

        loadButtons();
        botonCrear = findViewById(R.id.botonCrear);
        botonCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonName = editarTexto.getText().toString();
                if (!buttonName.isEmpty()) {
                    createButton(buttonName);
                    editarTexto.setText(""); // Limpiar el EditText después de crear el botón
                } else {
                    Toast.makeText(botones_logica.this, "Por favor, ingresa un nombre para el botón", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createButton(String buttonName) {
        Button boton = new Button(this);
        boton.setText(buttonName);

        // Diseño básico para mantener limpieza
        boton.setBackgroundResource(R.drawable.custom_button_background);
        boton.setTextColor(getResources().getColor(android.R.color.white));
        boton.setTextSize(20);
        boton.setTypeface(Typeface.DEFAULT_BOLD);


        // Tamaño y espacio entre botones
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                150
        );
        params.setMargins(16, 8, 16, 8);
        boton.setLayoutParams(params);

        boton.setContentDescription("Frase: " + buttonName);

        layoutPrincipal.addView(boton);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(buttonName, buttonName);
        editor.apply();

        // Declarar final para usar dentro del listener
        final String finalButtonName = buttonName;

        boton.setOnLongClickListener(v -> {
            layoutPrincipal.removeView(boton);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(finalButtonName);
            edit.apply();

            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) vibrator.vibrate(200);

            Toast.makeText(getApplicationContext(), "Botón eliminado", Toast.LENGTH_SHORT).show();
            Voz1.speak("Botón eliminado", TextToSpeech.QUEUE_FLUSH, null);
            return true;
        });

        boton.setOnClickListener(v -> {
            String texto = boton.getText().toString();
            Voz1.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
        });
    }



    private void loadButtons() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String buttonName = entry.getKey();
            createButton(buttonName);
        }
    }
}
