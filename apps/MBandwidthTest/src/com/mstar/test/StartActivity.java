package com.mstar.test;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("StartActivity", "onCreate");
        if(!isServiceRunning())
        {
            Intent serviceIntent = new Intent(StartActivity.this, FloatWindowService.class);
            startService(serviceIntent);
            Log.d("StartActivity", "start service!");
        }
        finish();
    }

    private boolean isServiceRunning() {
        Log.d("StartActivity", "check isServiceRunning!");
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for(RunningServiceInfo service:manager.getRunningServices(Integer.MAX_VALUE)) {
            if(service.service.getClassName().contains("FloatWindowService"))
                return true;
        }
        return false;
    }
}
