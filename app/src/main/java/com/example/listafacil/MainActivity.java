package com.example.listafacil;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("MiPreferencia", MODE_PRIVATE);
        boolean seHaIniciadoAntes = prefs.getBoolean("seHaIniciadoAntes", false);

        if (!seHaIniciadoAntes) {
            // Si es la primera vez que se inicia, marca la aplicación como iniciada antes
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("seHaIniciadoAntes", true);
            editor.apply();
        } else {
            // Si no es la primera vez, inicia directamente VistaPrincipal y finaliza MainActivity
            Intent i = new Intent(getApplicationContext(), VistaPrincipal.class);
            startActivity(i);
            finish();
            return; // Asegúrate de que MainActivity no continúe su ejecución
        }

        Button btnContinuar = findViewById(R.id.btnContinuar);
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VistaPrincipal.class);
                startActivity(i);
            }
        });
    }
}
