package com.toptech.launcherkorea2.dock;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

import android.util.Log;
import android.widget.GridView;
import android.os.Handler;
import android.os.Message;
import android.content.ComponentName;
import android.widget.AdapterView;
import android.view.View;
import android.media.AudioManager;

import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.utils.Funs;

import android.content.ActivityNotFoundException;
import android.widget.Toast;

/**
 * @author calvin
 * @date 2013-1-10
 */
public class ApplicationActivity extends Activity {
	
	private final Context context = ApplicationActivity.this;
	private GridView mGridView = null;
	private final static int UPDATA_UI = 0;
	private ArrayList<PackageInformation> mApkInformation = new ArrayList<PackageInformation>();
	private int mPosition = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_activity);

		mGridView = (GridView)findViewById(R.id.app_grid);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				if ((mApkInformation != null) && (arg2 < mApkInformation.size())){
					startApplication(arg2);
				}
				mPosition = arg2;
			}

		});

		mGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		new Thread(getApkInfoRunnable).start();
	}
	
	private Runnable getApkInfoRunnable = new Runnable() {
		public void run() {
			mApkInformation = Funs.getAllApks(context);
			
			Message msg = new Message();
			msg.what = UPDATA_UI;
			msg.arg1 = mApkInformation.size();
			mHandler.sendMessage(msg);
		}
	};

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATA_UI: 
				AppGridViewAdpter adapter = new AppGridViewAdpter(ApplicationActivity.this, mApkInformation);
				mGridView.setAdapter(adapter);
				mGridView.setSelection(mPosition);
				break;
			}
		}
	};

	private void startApplication(int position) {
		try {
			Log.d("Application", "startApplication");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			ComponentName componentName = new ComponentName(mApkInformation
					.get(position).getPackageName(), mApkInformation.get(
					position).getActivityName());
			intent.setComponent(componentName);
			startActivity(intent);
			//this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, R.string.activity_not_found,
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (SecurityException e) {
			Toast.makeText(this, R.string.activity_not_found,
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	protected void onResume() {
		super.onResume();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	protected void onRestart() {
		super.onRestart();
		new Thread(getApkInfoRunnable).start();
	}
}
