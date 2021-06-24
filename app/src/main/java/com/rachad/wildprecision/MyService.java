package com.rachad.wildprecision;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class MyService extends Service {
  //  static MediaPlayer mediaPlayer;
    static Ringtone ringtone;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra("stop", false)) {
            try {
                //  mediaPlayer.stop();
                ringtone.stop();
            } catch (Exception ignored) {
            }
        }
        createNotificationChaneel();
        Intent intent1 = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Safe State is false.click to stop alarm")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent).build();
        startForeground(456, notification);
        //mediaPlayer = MediaPlayer.create(this, R.raw.warning);
        // mediaPlayer.start();
        //mediaPlayer.setLooping(true);

       // mediaPlayer.setVolume(10.0f,10.0f);
        Uri uri = Uri.parse("android.resource://com.rachad.wildprecision/"+"/raw/warning");
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        ringtone.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(true);
            ringtone.setVolume(10.0f);
        }


        if (intent.getBooleanExtra("stop", false)) {
            stop();
        } else {
            SharedPreferences pref = getSharedPreferences("Wildprecisiontrack", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("q", false);
            editor.putString("password", "null");
            editor.putBoolean("working", true);
            editor.apply();
        }


        return START_STICKY;
    }


    private void createNotificationChaneel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ChannelId1", "Foreground Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        try {
            //  mediaPlayer.stop();
            ringtone.stop();
        } catch (Exception ignored) {
        }
        stopForeground(true);
        super.onDestroy();
    }

    void stop() {
        try {
            //mediaPlayer.stop();
            ringtone.stop();
        } catch (Exception ignored) {
        }
        stopForeground(true);
        stopSelf();
    }

}
