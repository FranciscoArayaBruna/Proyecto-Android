package com.example.listafacil.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.listafacil.Controlador.NotificacionJobIntentService;
import com.example.listafacil.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class VistaPrincipal extends AppCompatActivity {

    private EditText edtNuevaTarea;
    private Button btnAgregarTarea;
    private ListView lstTareas;
    private ArrayList<String> listaTareas;
    private TareaAdapter adaptadorTareas;
    private Button btnIniciarSesion;

    private static final String PREFS_NAME = "MiPreferencia"; // Definición de la constante PREFS_NAME

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareasGuardadas();
    }

    private void iniciarServicioNotificaciones() {
        Intent serviceIntent = new Intent(this, NotificacionJobIntentService.class);
        NotificacionJobIntentService.enqueueWork(this, serviceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_principal);

        edtNuevaTarea = findViewById(R.id.edtNuevaTarea);
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea);
        lstTareas = findViewById(R.id.lstTareas);
        listaTareas = new ArrayList<>();
        adaptadorTareas = new TareaAdapter(this, listaTareas);
        lstTareas.setAdapter(adaptadorTareas);
        iniciarServicioNotificaciones();

        TextView txtUsername = findViewById(R.id.txtUsername);
        String username = getIntent().getStringExtra("USERNAME");
        txtUsername.setText(username);

        btnAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaTarea = edtNuevaTarea.getText().toString();

                if (!nuevaTarea.isEmpty()) {
                    listaTareas.add(nuevaTarea);
                    adaptadorTareas.notifyDataSetChanged();
                    edtNuevaTarea.setText("");
                    guardarTareas(); // Guarda las tareas
                }
            }
        });

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VistaPrincipal.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cargarTareasGuardadas() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String tareasGuardadas = prefs.getString("tareasGuardadas", null);

        if (tareasGuardadas != null) {
            try {
                JSONArray jsonArray = new JSONArray(tareasGuardadas);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String tarea = jsonArray.getString(i);
                    listaTareas.add(tarea);
                }
                adaptadorTareas.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarTareas() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray();

        for (String tarea : listaTareas) {
            jsonArray.put(tarea);
        }

        editor.putString("tareasGuardadas", jsonArray.toString());
        editor.apply();
    }


}
