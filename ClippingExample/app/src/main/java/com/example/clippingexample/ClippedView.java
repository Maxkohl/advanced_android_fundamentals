package com.example.clippingexample;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ClippedView extends View {
    private Paint mPaint;
    private Path mPath;

    private int mClipRectRight =
            (int) getResources().getDimension(R.dimen.clipRectRight);
    private int mClipRectBottom =
            (int) getResources().getDimension(R.dimen.clipRectBottom);
    private int mClipRectTop =
            (int) getResources().getDimension(R.dimen.clipRectTop);
    private int mClipRectLeft =
            (int) getResources().getDimension(R.dimen.clipRectLeft);
    private int mRectInset =
            (int) getResources().getDimension(R.dimen.rectInset);
    private int mSmallRectOffset =
            (int) getResources().getDimension(R.dimen.smallRectOffset);

    private int mCircleRadius =
            (int) getResources().getDimension(R.dimen.circleRadius);

    private int mTextOffset =
            (int) getResources().getDimension(R.dimen.textOffset);
    private int mTextSize =
            (int) getResources().getDimension(R.dimen.textSize);

    private int mColumnOne = mRectInset;
    private int mColumnnTwo = mColumnOne + mRectInset + mClipRectRight;

    private int mRowOne = mRectInset;
    private int mRowTwo = mRowOne + mRectInset + mClipRectBottom;
    private int mRowThree = mRowTwo + mRectInset + mClipRectBottom;
    private int mRowFour = mRowThree + mRectInset + mClipRectBottom;
    private int mTextRow = mRowFour + (int) (1.5 * mClipRectBottom);

    private final RectF mRectF;

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
