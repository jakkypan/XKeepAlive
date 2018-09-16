package com.panda.xalive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 【前台Service保活】：这个在系统资源紧张时，照样会将其杀死。
 *
 *
 * 通过发起一个notification使得整个service变成前台service
 *
 */
public class ForegroundService extends Service {
    public static final int NOTICE_ID = 1000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 对API大于18而言，startForeground()方法是必须弹出一个可见通知的
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("KeepAppAlive");
        builder.setContentText("DaemonService is runing...");
        startForeground(NOTICE_ID, builder.build());
        // 去掉通知栏，这个也不想用户看到的，但是这个不会改变oom_adj的值
        Intent intent = new Intent(this, CancelNoticeService.class);
        startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 系统会尝试重新创建这个Service
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        NotificationManager mManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mManager.cancel(NOTICE_ID);
        Intent intent = new Intent(getApplicationContext(), ForegroundService.class);
        startService(intent);
        super.onDestroy();
    }
}
