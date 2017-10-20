package com.toptech.launcherkorea2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RemoveReceiver extends BroadcastReceiver	 {

	@Override
	public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")){
			String s=intent.getDataString();
			Intent i =new Intent();
			i.setAction("removed_package");
			i.putExtra("pn", s);
			context.sendBroadcast(i);
		}
	}
}
