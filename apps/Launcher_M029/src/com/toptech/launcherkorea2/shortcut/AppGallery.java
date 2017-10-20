package com.toptech.launcherkorea2.shortcut;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class AppGallery extends Gallery
{
	private final String TAG = "AppGallery";
	
	public AppGallery(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	public AppGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public AppGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t)
	{
		// TODO Auto-generated method stub
		return super.getChildStaticTransformation(child, t);
	}

	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		Log.d(TAG, "onFling------------------");
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		Log.d(TAG, "onScroll------------------");
		return false;
	}
	
	public interface IOnItemClickListener {  
	    public void onItemClick(int position);  
	}  
	
	private IOnItemClickListener mListener;
	
	public void setOnItemClickListener(IOnItemClickListener listener) {  
	    mListener = listener;  
	}  
	
	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		Log.d(TAG, "onSingleTapUp------------------");
		try {  
	        Field f = AppGallery.class.getSuperclass().getDeclaredField("mDownTouchPosition");  
	        f.setAccessible(true);  
	        int position = f.getInt(this);  
	        Log.i(TAG, "mDownTouchPosition = " + position);  
	        if(null != mListener && position >= 0) {  
	            mListener.onItemClick(position);  
	        }  
	    } catch (SecurityException e1) {  
	        e1.printStackTrace();  
	    } catch (NoSuchFieldException e1) {  
	        e1.printStackTrace();  
	    } catch (IllegalArgumentException e2) {  
	        e2.printStackTrace();  
	    } catch (IllegalAccessException e3) {  
	        e3.printStackTrace();  
	    }   
	    return false;  
		//return super.onSingleTapUp(e);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		Log.d(TAG, "dispatchKeyEvent------------------");
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		Log.d(TAG, "onTouchEvent------------------");
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e)
	{
		Log.d(TAG, "onDown------------------");
		return super.onDown(e);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
