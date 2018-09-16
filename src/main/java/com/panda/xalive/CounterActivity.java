package com.panda.xalive;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class CounterActivity extends AppCompatActivity {
    TextView tv;
    Button bn;
    boolean isRunning = false;
    Timer timer;

    ScreenBroadcastReceiver mScreenReceiver;
    JobScheduler mJobScheduler;
    static final int JOB_ID = 10000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(params);
        tv = new TextView(this);
        tv.setText("0");
        tv.setTextSize(20);
        tv.setGravity(Gravity.CENTER);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.weight = 1;
        root.addView(tv, params);
        bn = new Button(this);
        bn.setText("开始");
        bn.setBackgroundColor(Color.GREEN);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    stop();
                } else {
                    start();
                }
            }
        });
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        root.addView(bn, params);
        setContentView(root);

        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    private void start() {
        isRunning = true;
        startJobScheduler();
        startScreenBroadcast();
        startForegroundService();
        startMusicService();
        TimerTask mTask = new TimerTask() {
            @Override
            public void run() {
                // 更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(String.valueOf(Integer.valueOf(tv.getText().toString()) + 1) + "");
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(mTask,1000,1000);
    }

    private void stop() {
        isRunning = false;
        stopJobScheduler();
        stopForegroundService();
        stopMusicService();
        stopScreenBroadcast();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        tv.setText("0");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isRunning){
                Toast.makeText(CounterActivity.this,"正在计数",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        stop();
        stopJobScheduler();
        stopScreenBroadcast();
        stopForegroundService();
        stopMusicService();
        super.onDestroy();
    }

    private void startForegroundService() {
        Intent intent = new Intent(CounterActivity.this, ForegroundService.class);
        startService(intent);
    }

    private void stopForegroundService() {
        Intent intent = new Intent(CounterActivity.this, ForegroundService.class);
        stopService(intent);
    }

    private void startMusicService() {
        Intent intent = new Intent(CounterActivity.this, MusicService.class);
        startService(intent);
    }

    private void stopMusicService() {
        Intent intent = new Intent(CounterActivity.this, MusicService.class);
        stopService(intent);
    }

    private void startScreenBroadcast() {
        mScreenReceiver = new ScreenBroadcastReceiver(new ScreenBroadcastReceiver.ScreenStateListener() {

            @Override
            public void onScreenOn() {
                SinglePixelManager.instance().finishActivity();
            }

            @Override
            public void onScreenOff() {
                Intent intent = new Intent(CounterActivity.this, SinglePixelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onUserPresent() {

            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mScreenReceiver, filter);
    }

    private void stopScreenBroadcast() {
        unregisterReceiver(mScreenReceiver);
    }

    private void startJobScheduler() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,new ComponentName(this, AliveJobService.class));
            builder.setPeriodic(3000);
            builder.setPersisted(true);
//            builder.setRequiresCharging(true);
            JobInfo info = builder.build();
            mJobScheduler.schedule(info);
        }
    }

    private void stopJobScheduler() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler.cancel(JOB_ID);
        }
    }
}
