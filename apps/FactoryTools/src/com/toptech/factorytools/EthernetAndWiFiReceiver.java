package com.toptech.factorytools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class EthernetAndWiFiReceiver extends BroadcastReceiver {
	private static final String WIFI_SERVICE = "wifi";
	public static final String TYPE = "type";
	public static final int CONNECT_NOT_TYPE = 0;
	public static final int CONNECT_ETHERNET_TYPE = 1;
	public static final int CONNECT_WIFI_TYPE = 2;
	private int mType = CONNECT_NOT_TYPE;	// null
	private WifiManager wifiManager = null;
	private Context mContext = null;

	@Override
	public void onReceive(Context context, Intent intente) {
		// TODO Auto-generated method stub
		Intent intent = null;
		intent = new Intent(context, FactoryShow.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
		mContext = context;
		wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
		Bundle bundle = intente.getExtras();
		mType = bundle.getInt(TYPE, 0);
		switch (mType) {
		case CONNECT_NOT_TYPE:

			break;
		case CONNECT_ETHERNET_TYPE:
			if (wifiManager.isWifiEnabled() == false) {
				wifiManager.setWifiEnabled(true);
				wifiManager.startScan();
				intent.putExtra(TYPE, CONNECT_ETHERNET_TYPE);
//				try {
//					Thread.sleep(600);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				mContext.startActivity(intent);
			} else {
				intent.putExtra(TYPE, CONNECT_ETHERNET_TYPE);
				mContext.startActivity(intent);
			}
			break;
		case CONNECT_WIFI_TYPE:
			intent.putExtra(TYPE, CONNECT_WIFI_TYPE);
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}

}
