package com.example.dell.mediaapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Dell on 22-Jan-18.
 */

public class MediaService extends Service {

    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.vi_mot_nguoi);
        mMediaPlayer.start();
        Toast.makeText(getApplicationContext(), "Play!", Toast.LENGTH_SHORT).show();
        // Chết sau khi kill app
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.pause();
        Toast.makeText(getApplicationContext(), "Stop!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        public MediaService getService(){
            return MediaService.this;
        }
    }
}
