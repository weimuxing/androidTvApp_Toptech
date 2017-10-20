package com.toptech.factorytools;

import android.os.Bundle;

import android.os.Message;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class Factorytest extends Activity {
	private static String TAG = "Factorytest";
	private TextView imageView1;
	private TextView imageView2;
	private TextView imageView3;
	private TextView imageView4;
	private TextView imageView5;
	private TextView imageView6;
	private TextView imageView7;
	private TextView imageView8;
	private TextView btn_usb1;
	private TextView btn_usb2;
	private TextView btn_usb3;
	private TextView factorytest_text;
	private FrameLayout factorytestview1 = null;
	private LinearLayout factorytestview2 = null; // key test

	private BroadcastReceiver mPowerReceiver = null;
	public Message msg = null;
	private IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.factorytest);
		findViews();
		// getWifiStatus();
		mReceiver();
		msg = new Message();
		// ready key
		factorytestview1.setBackgroundColor(android.graphics.Color.BLACK);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		registerReceiver(mPowerReceiver, filter);
		testUSB();
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		Log.d(TAG, "***********************" + keycode);
		// TODO Auto-generated method stub
		testUSB();
		factorytestview1.setBackgroundColor(android.graphics.Color.BLACK);
		factorytestview2.setVisibility(View.VISIBLE);
		factorytest_text.setText(getString(R.string.str_factory_test_empty));
		switch (keycode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			bgdefalut();
			imageView5.setBackgroundColor(android.graphics.Color.YELLOW);
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			bgdefalut();
			imageView4.setBackgroundColor(android.graphics.Color.YELLOW);
			break;
		case KeyEvent.KEYCODE_TV_INPUT:
			bgdefalut();
			imageView7.setBackgroundColor(android.graphics.Color.YELLOW);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
			bgdefalut();
			imageView3.setBackgroundColor(android.graphics.Color.YELLOW);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_CHANNEL_UP:
			bgdefalut();
			imageView2.setBackgroundColor(android.graphics.Color.YELLOW);
			break;
		case KeyEvent.KEYCODE_M:
		case KeyEvent.KEYCODE_MENU:
			bgdefalut();
			imageView6.setBackgroundColor(android.graphics.Color.YELLOW);
			break;
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		case KeyEvent.KEYCODE_0:
			Intent intent = new Intent(Factorytest.this, MainService.class);
			intent.setAction("restart");
			startService(intent);
			finish();
			return true;
		default:
			bgdefalut();
			break;
		}

		// return super.onKeyDown(keycode, event);
		return true;
	}

	public void findViews() {
		imageView1 = (TextView) findViewById(R.id.btn1);
		imageView2 = (TextView) findViewById(R.id.btn2);
		imageView3 = (TextView) findViewById(R.id.btn3);
		imageView4 = (TextView) findViewById(R.id.btn4);
		imageView5 = (TextView) findViewById(R.id.btn5);
		imageView6 = (TextView) findViewById(R.id.btn6);
		imageView7 = (TextView) findViewById(R.id.btn7);
		imageView8 = (TextView) findViewById(R.id.btn8);
		factorytest_text = (TextView) findViewById(R.id.factorytest_text);
		factorytestview1 = (FrameLayout) findViewById(R.id.factorytestview1);
		factorytestview2 = (LinearLayout) findViewById(R.id.factorytestview2);
		btn_usb1 = (TextView) findViewById(R.id.btn_usb1);
		btn_usb2 = (TextView) findViewById(R.id.btn_usb2);
		btn_usb3 = (TextView) findViewById(R.id.btn_usb3);

	}

	private void mReceiver() {
		mPowerReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getAction().equals("com.toptech.factorytools.Factorytest.powerkey")) {
					bgdefalut();
					imageView1.setBackgroundColor(android.graphics.Color.YELLOW);
					Log.d(TAG, "20150115 POWERKEY");
				}
			}

		};
		filter = new IntentFilter();
		filter.addAction("com.toptech.factorytools.Factorytest.powerkey");
		registerReceiver(mPowerReceiver, filter);
	}

	private void bgdefalut() {
		imageView1.setBackgroundColor(android.graphics.Color.BLACK);
		imageView2.setBackgroundColor(android.graphics.Color.BLACK);
		imageView3.setBackgroundColor(android.graphics.Color.BLACK);
		imageView4.setBackgroundColor(android.graphics.Color.BLACK);
		imageView5.setBackgroundColor(android.graphics.Color.BLACK);
		imageView6.setBackgroundColor(android.graphics.Color.BLACK);
		imageView7.setBackgroundColor(android.graphics.Color.BLACK);
		imageView8.setBackgroundColor(android.graphics.Color.BLACK);
		factorytestview1.setBackgroundColor(android.graphics.Color.BLACK);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(mPowerReceiver);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void testUSB() {
		if (checkDisk("2-1:1.0")) // usb1
		{
			btn_usb1.setBackgroundColor(0xFF00FF00);
		} else {
			btn_usb1.setBackgroundColor(0xFFFF0000);
		}

		if (checkDisk("3-1:1.0")) // usb2
		{
			btn_usb2.setBackgroundColor(0xFF00FF00);
		} else {
			btn_usb2.setBackgroundColor(0xFFFF0000);
		}

		if (checkDisk("1-1:1.0")) // usb3 use for wifi
		{
			btn_usb3.setBackgroundColor(0xFF00FF00);
		} else {
			btn_usb3.setBackgroundColor(0xFFFF0000);
		}

	}

	public static Boolean checkDisk(String subName) {
		File Usbfile = new File("/sys/bus/usb/devices");
		File[] files = Usbfile.listFiles();
		if (files.length > 0) {
			for (File file : files) {
				if (file.isDirectory()) {
					if (file.canRead()) {
						if (file.getName().indexOf(subName) > -1 || file.getName().indexOf(subName.toUpperCase()) > -1) {
							Log.d("fujia", file.getAbsolutePath() + file.getName());
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
