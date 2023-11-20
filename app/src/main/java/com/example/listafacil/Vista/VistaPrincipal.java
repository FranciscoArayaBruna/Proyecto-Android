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

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareasGuardadas();
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

        // Recuperar el nombre de usuario de la información de inicio de sesión
        TextView txtUsername = findViewById(R.id.txtUsername);

        // Configurar el nombre de usuario en el TextView
        txtUsername.setText(obtenerNombreDeUsuario());

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
                // Aquí debes agregar el código para iniciar sesión
                // Puedes abrir la actividad de inicio de sesión o realizar las acciones necesarias
                // dependiendo de tu lógica de inicio de sesión.
                // Por ejemplo, podrías abrir una nueva actividad de inicio de sesión.
                Intent intent = new Intent(VistaPrincipal.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private String obtenerNombreDeUsuario() {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", ""); // "userEmail" debe coincidir con la clave utilizada durante el inicio de sesión

        // Extraer la parte del correo electrónico antes del "@"
        int atIndex = userEmail.indexOf("@");
        if (atIndex != -1) {
            return userEmail.substring(0, atIndex);
        } else {
            return userEmail; // Si no hay "@" en el correo, devuelve el correo completo
        }
    }

    private void cargarTareasGuardadas() { // Carga las tareas guardadas al iniciar
        SharedPreferences prefs = getSharedPreferences("MiPreferencia", MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences("MiPreferencia", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray();

        for (String tarea : listaTareas) {
            jsonArray.put(tarea);
        }

        editor.putString("tareasGuardadas", jsonArray.toString());
        editor.apply();
    }
}
