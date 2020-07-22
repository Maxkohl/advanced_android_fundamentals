package com.example.clippingexample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ClippedView extends View {
    public ClippedView(Context context) {
        super(context);
    }

    public ClippedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClippedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ClippedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                       int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
