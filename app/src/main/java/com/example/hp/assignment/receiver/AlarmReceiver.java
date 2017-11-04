package com.example.hp.assignment.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.hp.assignment.R;
import com.example.hp.assignment.activity.Constant;
import com.example.hp.assignment.database.DatabaseEvenImpl;
import com.example.hp.assignment.model.Even;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 11/4/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationCompat.Builder builder;
    private List<Even> evens=new ArrayList<>();
    private DatabaseEvenImpl databaseEven;
    @Override
    public void onReceive(Context context, Intent intent) {
        databaseEven=new DatabaseEvenImpl(context);
        evens.addAll(databaseEven.getAllData());
        builder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Constant.NOTIFICATION)
                .setContentText("dgdh")
                .setWhen(System.currentTimeMillis());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
