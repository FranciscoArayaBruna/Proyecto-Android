package com.example.listafacil.Controlador;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

public class NotificacionService extends IntentService {

    private static final String CHANNEL_ID = "NotificacionChannel";
    private static final int NOTIFICATION_ID = 1;

    public NotificacionService() {
        super("NotificacionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            // Manejar la lógica de notificación aquí
            manejarNotificaciones();
        }
    }

    private void manejarNotificaciones() {
        // Esperar 24 horas (86400000 milisegundos)
        long delayMillis = 86400000;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Verificar si hay tareas pendientes (debes implementar esta lógica)
                if (hayTareasPendientes()) {
                    // Mostrar la notificación
                    mostrarNotificacion();
                }
            }
        }, delayMillis);
    }

    private boolean hayTareasPendientes() {
        // Aquí debes implementar la lógica para verificar si hay tareas pendientes
        // Puedes consultar tus datos locales y determinar si hay tareas que necesitan notificación.
        // Devuelve true si hay tareas pendientes, false de lo contrario.
        return false; // Este es un marcador de posición, debes implementarlo según tus necesidades.
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
}

