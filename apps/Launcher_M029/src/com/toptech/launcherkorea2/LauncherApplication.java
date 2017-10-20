package com.toptech.launcherkorea2;

import com.toptech.launcherkorea2.logic.MainService;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;

/**
 * 入口应用类
 * @author calvin
 * @date 2013-1-10
 */
public class LauncherApplication extends Application{
	
	public static Context context;
	// 设置两边的缩放系数
	public static float leftMargin = 0.07f;                     
	public static float topMargin = 0.05f;
	
	/*显示屏宽度和高度*/
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	
	public static int mDensityDpi = 0;
	public static int mDefaultDpi = 160;
	public static float mDpiRatio = 0;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		getScreenSize();
		
		if (!MainService.isrun) {
			Intent it = new Intent(this, MainService.class);
			this.startService(it);
		}
		
	}
	
	private void getScreenSize(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
	    screenWidth = displayMetrics.widthPixels;
	    screenHeight = displayMetrics.heightPixels;
		mDensityDpi = displayMetrics.densityDpi;
		mDpiRatio = ((float)mDefaultDpi)/(float)displayMetrics.densityDpi;
	}
	
	public static int getTopMargin() {
		return (int) (topMargin * screenHeight);
	}

	public static int getLeftMagin() {
		return (int) (leftMargin * screenWidth);
	}
}
