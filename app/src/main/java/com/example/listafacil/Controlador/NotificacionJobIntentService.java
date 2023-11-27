package com.example.listafacil.Controlador;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

public class NotificacionJobIntentService extends JobIntentService {

    private static final int JOB_ID = 1000;
    private static final String CHANNEL_ID = "NotificacionChannel";
    private static final int NOTIFICATION_ID = 1;
    // Método estático para encolar el trabajo
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificacionJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // Lógica para manejar las notificaciones
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
        // Implementa la lógica para verificar si hay tareas pendientes
        // Devuelve true si hay tareas pendientes, false de lo contrario.
        return false; // Este es un marcador de posición, debes implementarlo según tus necesidades.
    }

    private void mostrarNotificacion() {
        // Crea el canal de notificación
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
        // Crea el canal de notificación (solo si es necesario)
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

