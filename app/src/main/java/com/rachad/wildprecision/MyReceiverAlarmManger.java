package com.rachad.wildprecision;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MyReceiverAlarmManger extends BroadcastReceiver {
    DatabaseReference myRef;

    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        SharedPreferences pref = context.getSharedPreferences("Wildprecisiontrack", 0);
        String email, password;
        email = pref.getString("email", "null");
        password = pref.getString("password", "null");
        myRef = database.getReference("account/email_password/" + email + "_" + password);
        myRef.child("SafeState").get().addOnCompleteListener(task -> {
            try {
                if (task.isSuccessful()) {
                    if (!Boolean.parseBoolean(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getValue()).toString())) {
                        if (pref.getBoolean("q", false) && !pref.getBoolean("working", false)) {
                            Intent intent1 = new Intent(context, MyService.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(intent1);
                            } else {
                                context.startService(intent1);
                            }
                        }

                    } else {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("working", false);
                        editor.apply();
                    }
                }
            } catch (Exception ignored) {
            }
        });

        Intent intent1 = new Intent(context, MyServices.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent1);
        } else {
            context.startService(intent1);
        }


        /*Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2 * 1000);*/
    }

   /* private void notify(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, LoginActivity.class), 0);
        Notification notification =
                new NotificationCompat.Builder(context, "ChannelId2")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Safe State is false"))
                        .setContentText("Safe State is false")
                        .setAutoCancel(true)
                        .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.warning))
                        .setContentIntent(contentIntent).build();
        notificationManager.notify(147, notification);
        SharedPreferences pref = context.getSharedPreferences("Wildprecisiontrack", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("q", false);
        editor.putString("password", "null");
        editor.apply();
    }

    private void createNotificationChaneel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ChannelId2", "Foreground Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        notify(context);
    }*/
}
