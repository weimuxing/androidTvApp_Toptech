package com.toptech.factorytools;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;

public class MainService extends IntentService {
	private final static String RESTART = "restart";
	private ComponentName componentName ;
	private Intent mIntent;
	
	public MainService() {
		super("com.toptech.factorytools.MainService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mIntent = new Intent();
		componentName = new ComponentName(AllTestName.PACKNAME, AllTestName.ClASSNAME);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mIntent.setComponent(componentName);
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if(action.equals(RESTART)){
			mIntent.putExtra("type", 0);
			startActivity(mIntent);
		}
		//all operation of the time-comsuming should write in there. 
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
