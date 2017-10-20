package com.toptech.factorytools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class UpdateKeyReceiver extends BroadcastReceiver {
	public static final String TYPE = "type";
	public static final int CONNECT_NOT_TYPE = 0;
	public static final int CONNECT_ETHERNET_MAC_TYPE = 3;
	public static final int CONNECT_HDCP_KEY_TYPE = 4;
	public static final int CONNECT_CI_KEY_TYPE = 7;
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
		Bundle bundle = intente.getExtras();
		mType = bundle.getInt(TYPE, 0);
		switch (mType) {
		case CONNECT_NOT_TYPE:

			break;
		case CONNECT_ETHERNET_MAC_TYPE:		
				intent.putExtra(TYPE, CONNECT_ETHERNET_MAC_TYPE);
				mContext.startActivity(intent);
			break;
		case CONNECT_HDCP_KEY_TYPE:
			intent.putExtra(TYPE, CONNECT_HDCP_KEY_TYPE);
			mContext.startActivity(intent);
			break;
		case CONNECT_CI_KEY_TYPE:
			intent.putExtra(TYPE, CONNECT_CI_KEY_TYPE);
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}

}
