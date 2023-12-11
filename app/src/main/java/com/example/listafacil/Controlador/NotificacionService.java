package com.example.listafacil.Controlador;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class NotificacionService extends Service {

    private static final String CHANNEL_ID = "NotificacionChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            manejarNotificaciones(intent.getBooleanExtra("hayTareasPendientes", false));
        }
        return START_NOT_STICKY;
    }

    private void manejarNotificaciones(boolean hayTareasPendientes) {
        long delayMillis = 10000; // Notificación cada 10 segundos

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hayTareasPendientes) {
                    mostrarNotificacion();
                    // Luego de mostrar la notificación, programamos la próxima
                    manejarNotificaciones(true);
                }
            }
        }, delayMillis);
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
