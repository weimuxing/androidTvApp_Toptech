package com.toptech.launcherkorea2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class WaterTextView extends TextView{
	public WaterTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("NewApi")
	public WaterTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setSingleLine(true);
		setEllipsize(TruncateAt.MARQUEE);
		setMarqueeRepeatLimit(-1);//marquee_forever
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	public WaterTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
}
