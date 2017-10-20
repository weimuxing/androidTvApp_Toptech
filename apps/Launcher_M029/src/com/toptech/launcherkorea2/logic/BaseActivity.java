package com.toptech.launcherkorea2.logic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

/**
 * 窗口基类
 * @author calvin
 *
 */
public abstract class BaseActivity extends Activity{

	private final static String TAG = "BaseActivity";
	public abstract void init();
    public abstract void refresh(Object ... param);
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().setSystemUiVisibility(4);
		MainService.addActivity(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MainService.removeActivity(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//Log.d(TAG, "onKeyDown.......");
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		//Log.d(TAG, "onKeyUp.......");
		
		return super.onKeyUp(keyCode, event);
	}
	
	
}
