package com.panda.xalive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * 为了清除notification bar上为了保活而显示的notification，这个最好不要展现给用户
 */
public class CancelNoticeService extends Service {
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        startForeground(ForegroundService.NOTICE_ID,builder.build());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopForeground(true);
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(ForegroundService.NOTICE_ID);
                stopSelf();
            }
        }, 300);
    }
}
