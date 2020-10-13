package com.example.noteflow;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title=intent.getStringExtra(AddNoteActivity.TITLE);
        String desc=intent.getStringExtra(AddNoteActivity.DESC);
        String time=intent.getStringExtra(AddNoteActivity.TIME);
        String date=intent.getStringExtra(AddNoteActivity.DATE);
        int best=intent.getIntExtra(AddNoteActivity.BEST,1);
       makeNotification(context,title,desc,time,date,best);
    }

    private void makeNotification(Context context,String title,String desc,String time,String date,int best) {
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification=new NotificationCompat.Builder(context,"Note");
        notification.setSmallIcon(R.mipmap.ic_launcher).setContentText(desc).setContentTitle(title);
        notificationManager.notify("1",1,notification.build());
    }
}
