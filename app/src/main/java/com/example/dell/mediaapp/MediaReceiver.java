package com.example.dell.mediaapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

/**
 * Created by Dell on 22-Jan-18.
 */

public class MediaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Đối số để nhận dữ liệu là key đã khai báo khi gửi và trả về một value đã định nghĩa
        String receiveLetter = intent.getStringExtra("key");
        Toast.makeText(context , receiveLetter, Toast.LENGTH_LONG).show();



    }

}
