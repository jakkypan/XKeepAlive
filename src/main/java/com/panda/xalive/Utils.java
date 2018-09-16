package com.panda.xalive;

import android.app.ActivityManager;
import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Utils {
    public static boolean isAPPALive(Context mContext){
        boolean isAPPRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList){
            if(mContext.getPackageName().equals(appInfo.processName)){
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }

    public static void writeFile(File file, String data, boolean isAppend) {
        FileWriter fw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, isAppend);
            fw.write(data);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
