package com.example.surfaceviewexample;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;

public class GameView extends SurfaceView implements Runnable {

    private Context mContext;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);

    }

    @Override
    public void run() {

    }

    public void init(Context context) {
        mContext = context;
    }
}
