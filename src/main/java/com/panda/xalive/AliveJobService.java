package com.panda.xalive;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.io.File;

/**
 * JobScheduler执行的后台服务
 */
public class AliveJobService extends JobService {
    private static final int MESSAGE_ID_TASK = 0x01;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(!Utils.isAPPALive(getApplicationContext())){
                Intent intent = new Intent(getApplicationContext(), CounterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // 通知系统任务执行结束
                jobFinished((JobParameters) msg.obj, false );
            }

            Utils.writeFile(new File(getFilesDir(), "jobs"), "this is a test info for nowwwwwwwwwwwwwwwww" + System.currentTimeMillis() + "\n\r", true);
            return true;
        }
    });

    @Override
    public boolean onStartJob(JobParameters params) {
        Message msg = Message.obtain(mHandler, MESSAGE_ID_TASK, params);
        mHandler.sendMessage(msg);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mHandler.removeMessages(MESSAGE_ID_TASK);
        return false;
    }
}
