package com.mstar.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FpsImageView extends ImageView {
    private long mStartTime = -1;
    private int mCounter;
    private int mFps;

    private final Paint mPaint;

    public FpsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(48f);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mStartTime == -1) {
            mStartTime = SystemClock.elapsedRealtime();
            mCounter = 0;
        }

        long now = SystemClock.elapsedRealtime();
        long delay = now - mStartTime;

        super.draw(canvas);

        canvas.drawText(mFps + " fps", 20, 120, mPaint);

        if (delay > 1000l) {
            mStartTime = now;
            mFps = mCounter;
            mCounter = 0;
        }

        mCounter++;
    }
}
