package com.example.customfancontroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DialView extends View {
    private float mWidth;                   // Custom view width.
    private float mHeight;                  // Custom view height.
    private Paint mTextPaint;               // For text in the view.
    private Paint mDialPaint;               // For dial circle in the view.
    private float mRadius;                  // Radius of the circle.
    private int mActiveSelection;           // The active selection.
    // String buffer for dial labels and float for ComputeXY result.
    private final StringBuffer mTempLabel = new StringBuffer(8);
    private final float[] mTempResult = new float[2];

    private int mFanOnColor;
    private int mFanOffColor;

    private int mSelectionCount = 1;

    public DialView(Context context) {
        super(context);
        init(null);
    }

    public DialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);
        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Initialize current selection.
        mActiveSelection = 0;

        mFanOnColor = Color.CYAN;
        mFanOffColor = Color.GRAY;
        mDialPaint.setColor(mFanOffColor);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.DialView,
                    0, 0);
            // Set the fan on and fan off colors from the attribute values.
            mFanOnColor = typedArray.getColor(R.styleable.DialView_fanOnColor,
                    mFanOnColor);
            mFanOffColor = typedArray.getColor(R.styleable.DialView_fanOffColor,
                    mFanOffColor);
            mSelectionCount =
                    typedArray.getInt(R.styleable.DialView_selectionIndicators,
                            mSelectionCount);
            typedArray.recycle();
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mActiveSelection = (mActiveSelection + 1) % mSelectionCount;
                if (mActiveSelection >= 1) {
                    mDialPaint.setColor(mFanOnColor);
                } else {
                    mDialPaint.setColor(mFanOffColor);
                }
                invalidate();
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mRadius = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
    }

    private float[] computeXYForPosition(final int pos, final float radius , boolean isLabel) {
        float[] result = mTempResult;
        Double startAngle;
        Double angle;
        if (mSelectionCount > 4) {
            startAngle = Math.PI * (3 / 2d);
            angle= startAngle + (pos * (Math.PI / mSelectionCount));
            result[0] = (float) (radius * Math.cos(angle * 2))
                    + (mWidth / 2);
            result[1] = (float) (radius * Math.sin(angle * 2))
                    + (mHeight / 2);
            if((angle > Math.toRadians(360)) && isLabel) {
                result[1] += 20;
            }
        } else {
            startAngle = Math.PI * (9 / 8d);
            angle= startAngle + (pos * (Math.PI / mSelectionCount));
            result[0] = (float) (radius * Math.cos(angle))
                    + (mWidth / 2);
            result[1] = (float) (radius * Math.sin(angle))
                    + (mHeight / 2);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint);
        final float labelRadius = mRadius + 20;
        StringBuffer label = mTempLabel;
        for (int i = 0; i < mSelectionCount; i++) {
            //... For text labels:
            float[] xyData = computeXYForPosition(i, labelRadius, true);
            float x = xyData[0];
            float y = xyData[1];
            label.setLength(0);
            label.append(i);
            canvas.drawText(label, 0, label.length(), x, y, mTextPaint);
        }

        final float markerRadius = mRadius - 35;
        float[] xyData = computeXYForPosition(mActiveSelection, markerRadius, false);
        float x = xyData[0];
        float y = xyData[1];
        canvas.drawCircle(x, y, 20, mTextPaint);
    }

    public void setSelectionCount(int count) {
        this.mSelectionCount = count;
        this.mActiveSelection = 0;
        mDialPaint.setColor(mFanOffColor);
        invalidate();
    }
}
