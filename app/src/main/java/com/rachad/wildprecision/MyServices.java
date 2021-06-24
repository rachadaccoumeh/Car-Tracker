package com.rachad.wildprecision;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class MyServices extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Alarmset();
        kill_me();
        createNotificationChaneel();
        Intent intent1 = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
        Notification notification = new NotificationCompat.Builder(this, "ChannelId1")
                .setContentTitle(getString(R.string.app_name))
                .setContentText("")
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent).build();
        startForeground(450, notification);
        kill_me();
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    private void createNotificationChaneel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ChannelId1", "Foreground Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    void Alarmset() {
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MyReceiverAlarmManger.class);
        intent.setAction("com.rachad.alarm");
        // intent.putExtra("MyMessage", "hello from alarm");
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

     /*   am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);*/
//setInexactRepeating
        //setRepeatingworking
        //hl right now ELAPSED_REALTIME_WAKEUP
        /*am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 20*//*<- time by second*//* * 1000, pi);*/

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30/*<- time by second */ * 1000, pi);
        else
            am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 30/*<- time by second*/ * 1000, pi);
    }

    public void kill_me() {
        stopForeground(true);
    }
}
