package com.example.listafacil.Vista;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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

    private static final String PREFS_NAME = "MiPreferencia";
    private static final String CHANNEL_ID = "NotificacionChannel";
    private static final int NOTIFICATION_ID = 1;

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
                    guardarTareas();

                    // Verificar y mostrar notificación
                    if (hayTareasPendientes()) {
                        mostrarNotificacion();
                    }
                }
            }
        });

        // Agregamos la lógica para mostrar notificaciones cada 15 segundos
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Verificar y mostrar notificación
                if (hayTareasPendientes()) {
                    mostrarNotificacion();
                }
                // Programar el próximo chequeo después de 15 segundos
                handler.postDelayed(this, 15000);
            }
        }, 15000);

        // Resto del código...
    }

    private boolean hayTareasPendientes() {
        return !listaTareas.isEmpty();
    }

    private void mostrarNotificacion() {
        createNotificationChannel();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Tareas Pendientes")
                        .setContentText("Tienes tareas pendientes por hacer.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificacionChannel";
            String description = "Canal de notificación para tareas pendientes";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void cargarTareasGuardadas() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String tareasGuardadas = prefs.getString("tareasGuardadas", null);
        listaTareas.clear();
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
