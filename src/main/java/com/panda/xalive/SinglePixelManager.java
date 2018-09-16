package com.panda.xalive;


import android.app.Activity;

import java.lang.ref.WeakReference;

public class SinglePixelManager {
    private static SinglePixelManager manager;
    private WeakReference<Activity> mActivityRef;

    private SinglePixelManager() {

    }

    public static SinglePixelManager instance(){
        if(manager == null){
            manager = new SinglePixelManager();
        }
        return manager;
    }

    public void setSingleActivity(Activity mActivity){
        mActivityRef = new WeakReference<>(mActivity);
    }

    public void finishActivity(){
        if(mActivityRef != null){
            Activity mActivity = mActivityRef.get();
            if(mActivity != null){
                mActivity.finish();
            }
        }
    }
}
