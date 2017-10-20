package com.toptech.factorytools;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.toptech.factorytools.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;

import com.mstar.android.tv.TvTimerManager;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.util.Log;

import com.mstar.android.tv.TvPictureManager;
public class AgingActivity extends Activity {
	private ImageView image;
	private int cureentInputSource;
	private int currentBacklight;
	private SharedPreferences sp;
	private Editor editor;
	private static final String TAG = "AgingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image = (ImageView) findViewById(R.id.iv_aging);
		TvAudioManager.getInstance().enableAudioMute(TvAudioManager.AUDIO_MUTE_ALL);
		currentBacklight = TvPictureManager.getInstance().getBacklight();
		sp = getSharedPreferences("sp",MODE_PRIVATE);
		editor = sp.edit();
		if(sp.getInt("currentBacklight", -1) == -1)
		{
			editor.putInt("currentBacklight",currentBacklight);
			editor.commit();
		}else{
			currentBacklight = sp.getInt("currentBacklight", -1);
		}
		TvPictureManager.getInstance().setBacklight(100);
		cureentInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
		image.setBackgroundResource(R.anim.anim);
		AnimationDrawable anim = (AnimationDrawable) image.getBackground();
		anim.start();
		TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
		TvCommonManager.getInstance().setTvosCommonCommand("StartLedRedToGreen");
		SystemProperties.set("persist.sys.aging", "true");
		try {
			TvManager.getInstance().setEnvironment("env_aging_Running", "1");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.toptech.close.AgingActivity.from.phonewindowmanagwer");
		registerReceiver(agingRecivier, filter);
	}
	
	BroadcastReceiver agingRecivier = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String action = arg1.getAction();
			if (action.equals("com.toptech.close.AgingActivity.from.phonewindowmanagwer")) {
				SendKey(KeyEvent.KEYCODE_MENU);
			}
		}
	};
	
	 private void SendKey(final int keycode){
	    	new Thread() {
	            public void run() {
	                try {
	                    Instrumentation inst = new Instrumentation();
	                    inst.sendKeyDownUpSync(keycode);
	                    
	                } catch (Exception e) {
	                    Log.e("zsr", e.toString());
	                }
	            }
	        }.start();
	    }

	@Override
	protected void onPause() {
		super.onPause();
	}

     @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d(TAG, "dispatchKeyEvent, event.getKeyCode() = " + event.getKeyCode());
		if (event.getKeyCode() == KeyEvent.KEYCODE_M ||
				event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			TvCommonManager.getInstance().setTvosCommonCommand("StopLedRedToGreen");
			SystemProperties.set("persist.sys.aging", "false");
			TvPictureManager.getInstance().setBacklight(currentBacklight);
			editor.putInt("currentBacklight",-1);
			editor.commit();
			try {
				TvManager.getInstance().setEnvironment("env_aging_Running", "0");
			} catch (Exception e) {
			}
			if(cureentInputSource == TvCommonManager.INPUT_SOURCE_STORAGE){
				int supportSourceList[] = TvCommonManager.getInstance().getSourceList();
				cureentInputSource = supportSourceList[0];
			}
			ComponentName componentName=null;
			if(SystemProperties.getBoolean("mstar.aries.osd", false)){
				componentName = new ComponentName(
						"com.toptech.tvmenu",
						"com.toptech.tvmenu.TVActivity");
			}else{
				componentName = new ComponentName(
						"com.mstar.tv.tvplayer.ui",
						"com.mstar.tv.tvplayer.ui.RootActivity");
			}
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setComponent(componentName);
				intent.putExtra("inputSrc", cureentInputSource);
				intent.putExtra("isPowerOn", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				AgingActivity.this.startActivity(intent);
				SystemProperties.set("mstar.launcher.1stinit", "true");
				PackageManager pm = getPackageManager();
				ComponentName name = new ComponentName(this, AgingActivity.class);
				pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
						PackageManager.DONT_KILL_APP);
				finish();
		
		}
		return true;
	}
     
       @Override
    public void onDestroy() {
        super.onDestroy();
		TvAudioManager.getInstance().disableAudioMute(TvAudioManager.AUDIO_MUTE_ALL);
        unregisterReceiver(agingRecivier);
       }  
}
