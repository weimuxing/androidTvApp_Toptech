package com.mstar.tv.tvplayer.ui.widget;

import android.widget.ImageView;
import android.graphics.Matrix;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import com.mstar.tv.tvplayer.ui.R;
import android.content.res.Resources;
import android.content.res.TypedArray;


public class MirrorImageView extends ImageView
{
	boolean mirror = true;

	public void setMirror(boolean bsetMirror)
	{
		mirror  = bsetMirror;
	}

	public MirrorImageView(Context context) {
        super(context);
    }

    public MirrorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MirrorImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.mirrorImage, defStyle, 0);
        mirror = a.getBoolean(R.styleable.mirrorImage_mirror, true);
    }
    
	@Override
	public void onDraw(Canvas canvas)
	{
		Matrix mMatrix;

		if (mirror)
		{
			mMatrix = getImageMatrix();

			mMatrix.postScale(-1, 1);
			mMatrix.postTranslate((float)getMeasuredWidth(), 0);
			setImageMatrix(mMatrix);
		}
		super.onDraw(canvas);
	}
}