package com.mark.stylish;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class StylishService extends Service {
    private Timer mTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String description = "ShouKaYo";
            NotificationChannel channel = new NotificationChannel("com.mark.stylish",
                    "Stylish", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        mTimer = new Timer();
        Log.i("Mark", "Success");
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    private TimerTask mTimerTask = new TimerTask() {
        int i = 0;
        @Override
        public void run() {
            NotificationCompat.Builder builder = new NotificationCompat
                    .Builder(MyApp.getContext(), "com.mark.stylish")
                    .setSmallIcon(R.drawable.icons_24px_notification)
                    .setContentTitle("Stylish")
                    .setContentText("Ran~" + i)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApp.getContext());
            notificationManager.notify(i, builder.build());
            i++;
        }
    };
}
