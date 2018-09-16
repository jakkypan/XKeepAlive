package com.panda.xalive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements Handler.Callback {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Point p = new Point();
        wm.getDefaultDisplay().getSize(p);

        Log.e("111", p.x + "   " + p.y);

        setContentView(R.layout.activity_main);
        handler = new Handler(this);
        handler.sendEmptyMessageDelayed(0,5000);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Intent intent = new Intent(MainActivity.this, CounterActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
