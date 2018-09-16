package com.panda.xalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 解锁屏系统广播
 */
public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private ScreenStateListener mStateReceiverListener;

    public ScreenBroadcastReceiver(ScreenStateListener mStateReceiverListener) {
        this.mStateReceiverListener = mStateReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (mStateReceiverListener == null) {
            return;
        }
        if (Intent.ACTION_SCREEN_ON.equals(action)) {         // 开屏
            mStateReceiverListener.onScreenOn();
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {  // 锁屏
            mStateReceiverListener.onScreenOff();
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            mStateReceiverListener.onUserPresent();
        }
    }

    public interface ScreenStateListener {
        void onScreenOn();

        void onScreenOff();

        void onUserPresent();
    }
}
