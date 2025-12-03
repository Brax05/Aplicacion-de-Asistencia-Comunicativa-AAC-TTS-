package com.example.txttovoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    Button txtLargo;
    Button txtCorto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        txtLargo = findViewById(R.id.txtLargo);
        txtLargo.setBackgroundResource(R.drawable.custom_button_background);

        txtCorto = findViewById(R.id.txtCorto);
        txtCorto.setBackgroundResource(R.drawable.custom_button_background);

        txtLargo.setOnClickListener(v -> {
            Intent intentos = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intentos);
        });

        txtCorto.setOnClickListener(v -> {
            Intent intentos = new Intent(MainActivity2.this, CarpetasActivity.class);
            startActivity(intentos);
        });
    }
}
