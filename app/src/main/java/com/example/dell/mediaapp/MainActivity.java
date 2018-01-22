package com.example.dell.mediaapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextViewTitle, mTextViewRunTime, mTextViewTotalTime;
    private ImageButton mBtnPrevious, mBtnPlay, mBtnNext;
    private EditText mEditText;
    private Button mBtnTimeOff;
    private SeekBar mSeekBar;
    private MediaPlayer mMediaPlayer;
    private List<Song> arraySong;
    private int mPosition = 0;
    private ServiceConnection mServiceConnection;
    private boolean isConnected;
    private MediaService mMediaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        addSong();
        launchMediaPlayer();
        connectionService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Hủy kết nối khi app destroy
        unbindService(mServiceConnection);
    }

    private void connectionService() {
        Intent intent = new Intent(this, MediaService.class);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                // Lấy đối tượng binder, kết nối
                MediaService.MyBinder myBinder = (MediaService.MyBinder) service;
                mMediaService = myBinder.getService();
                isConnected = true;
                Log.i("Check connect", "a" + isConnected);
            }


            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isConnected = false;
                mMediaService = null;
            }
        };
        bindService(intent , mServiceConnection, BIND_AUTO_CREATE);

    }



    /**
     * Khởi chạy MediaPlayer
     */
    private void launchMediaPlayer() {
        mMediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(mPosition).getmAudioResourceID());
        mTextViewTitle.setText(arraySong.get(mPosition).getmSongTitle());
    }

    private void findView(){ //Tìm viewID và xử lí button
        mTextViewTitle =  (TextView) findViewById(R.id.text_name);
        mTextViewRunTime = (TextView) findViewById(R.id.text_runtime);
        mTextViewTotalTime = (TextView) findViewById(R.id.text_totaltime);
        mBtnPrevious = (ImageButton) findViewById(R.id.image_btn_previous);
        mBtnPlay = (ImageButton) findViewById(R.id.image_btn_play);
        mBtnNext = (ImageButton) findViewById(R.id.image_btn_next);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mEditText = (EditText) findViewById(R.id.editText);
        mBtnTimeOff = (Button) findViewById(R.id.btn_time_off) ;

        mBtnPlay.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnPrevious.setOnClickListener(this);
        mBtnTimeOff.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Kéo seekbar , cập nhập giá trị liên tục (tua theo con trỏ)
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Lấy khoảnh khắc vừa chạm , cập nhập
                mMediaPlayer.seekTo(seekBar.getProgress());

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Khi kéo seekbar và buông ra , lấy giá trị từ điểm cuối
                mMediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private void addSong(){
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Vì Một Người", R.raw.vi_mot_nguoi));
        arraySong.add(new Song("Giữ Em Đi", R.raw.giu_em_di));
        arraySong.add(new Song("Hẹn Một Mai", R.raw.hen_mot_mai));
        arraySong.add(new Song("Yêu Em Rất Nhiều", R.raw.yeu_em_rat_nhieu));
    }

    private void updateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

                //Hiển thị Time hiện tại của bài hát
                mTextViewRunTime.setText(simpleDateFormat.format(mMediaPlayer.getCurrentPosition()));

                //Chạy thanh Seekbar
                mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());

                //Kiểm tra bài nhạc đã hết chưa , nếu hết chuyển bài
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        handlerButtonNext();
                    }

                });

                handler.postDelayed(this, 500); //0.5s
            }
        },100);

    }

    private void setTimeOff() {
        int minutes = Integer.parseInt(mEditText.getText().toString());

        //Ẩn bàn phím sau khi nhập dữ liệu
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (minutes > 0) {
            if (mMediaPlayer.isPlaying()) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMediaPlayer.pause();
                    }
                }, minutes * 60 * 1000);
                Toast.makeText(this, "Successful !", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Please start a song !!! ", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void setTotalTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        mTextViewTotalTime.setText(simpleDateFormat.format(mMediaPlayer.getDuration()));

        //Gán max Seekbar =mMediaPlayer.getDuration() để có được thời gian 2 thanh giống nhau
        mSeekBar.setMax(mMediaPlayer.getDuration());
    }

    private void createNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);

        Intent intent = new Intent(this, MainActivity.class);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//        //Hiển thị Time hiện tại của bài hát
//        mTextViewRunTime.setText(simpleDateFormat.format(mMediaPlayer.getCurrentPosition()));
//
//        //Chạy thanh Seekbar
//        mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1 ,
                intent, 0);
        //Nội dung của notification
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle(arraySong.get(mPosition).getmSongTitle());
        builder.setContentText("Chạm để có tùy chọn khác");
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(1,builder.build());
    }
    private void handlerButtonPrevious() {
        mPosition--;
        if (mPosition < 0){
            mPosition = arraySong.size();
            mPosition--;
        }
        // Nếu bài hát đang chạy , stop và chuyển sang bài mới
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        //Khởi chạy Media và cập nhập tên , bài hát
        launchMediaPlayer();
        mMediaPlayer.start();
        mBtnPlay.setImageResource(R.drawable.ic_pause);

        // Khi play hiện Total time của bài hát
        setTotalTime();
        createNotification();
    }

    private void handlerButtonPlay() {
        Intent intent = new Intent();
        intent.setAction("com.intent.Mybroadcast");
        //key, value
        intent.putExtra("key" , "You're listening to music");
        sendBroadcast(intent);

        createNotification();

        //Kiểm tra nó đang phát hay không , nếu đang hát -> dừng
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            mBtnPlay.setImageResource(R.drawable.ic_play);
        }
        // Ngược lại , đang ngừng -> phát nhạc
        else {
            mMediaPlayer.start();
            mBtnPlay.setImageResource(R.drawable.ic_pause);
        }
        // Khi play hiện Total time của bài hát
        setTotalTime();
        updateTimeSong();
    }

    private void handlerButtonNext() {
        mPosition++;
        if(mPosition > arraySong.size() -1){
            // Nếu vị trí bài hát lớn hơn mảng , quay về vị trí đầu tiên
            mPosition = 0;
        }
        // Nếu bài hát đang chạy , stop và chuyển sang bài mới
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        //Khởi chạy Media và cập nhập tên , bài hát
        launchMediaPlayer();
        mMediaPlayer.start();
        mBtnPlay.setImageResource(R.drawable.ic_pause);

        // Khi play hiện Total time của bài hát
        setTotalTime();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_btn_play:
                handlerButtonPlay();
                break;
            case R.id.image_btn_previous:
                handlerButtonPrevious();
                break;
            case R.id.image_btn_next:
                handlerButtonNext();
                break;
            case R.id.btn_time_off:
                setTimeOff();
                break;

        }
    }
}
