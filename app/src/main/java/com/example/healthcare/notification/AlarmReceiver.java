package com.example.healthcare.notification;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.healthcare.HomeScreen;
import com.example.healthcare.MedicationDashBoard;
import com.example.healthcare.R;

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, HomeScreen.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeScreen.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent= stackBuilder.getPendingIntent(100,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyme")
                .setSmallIcon(R.drawable.headericon)
                .setContentTitle("Medication Reminder")
                .setContentText("it time to take your medication! get a glass of water")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200,builder.build());

    }
}
