package com.toptech.factorytools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.io.File;
import java.lang.reflect.Method;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.listener.OnEventListener;
import com.mstar.android.tvapi.common.vo.EnumMedium;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvCiManager;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.drm.DrmStore.RightsStatus;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.INetworkManagementService;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.InputDevice;

import com.toptech.factorytools.Factorytest;
import android.content.ComponentName;

public class FactoryShow extends Activity {
	private static final String TAG = FactoryShow.class.getSimpleName();
	private static final String WIFI_SERVICE = "wifi";
	public static final String TYPE = "type";
	public static final int CONNECT_NOT_TYPE = 0;
	public static final int CONNECT_ETHERNET_TYPE = 1;
	public static final int CONNECT_WIFI_TYPE = 2;
	public static final int CONNECT_ETHERNET_MAC_TYPE = 3;
	public static final int CONNECT_HDCP_KEY_TYPE = 4;
	public static final int CONNECT_INPUT_SOURCE_TYPE = 5;
	//public static final int CONNECT_HDCP2_KEY_TYPE = 6;
	public static final int CONNECT_CI_KEY_TYPE = 7;
	private int mType = CONNECT_NOT_TYPE;	// null
	private WifiManager wifiManager = null;
	private LinearLayout mBackGround = null;
	private TextView mTestView = null;
	private ProgressBar mPBar = null;
	private StorageManager mStorageManager =null;
	private TvFactoryManager tvFactoryManager;
	private TvCommonManager mTvCommonManager;
	private TvManager mTvManager;
	private int testResult=0;
	private int backCmd=0;
	private boolean isNeedFeedback=true;
	private String tmpResult="";
	private int AdcValue=0xFF;
	private String cmdLength="";
	private String hdcpkeyName="";
	private String mackeyName="";
	private String cikeyName="";
	private int irKeycode=0xFF;
	private int keypadKeycode=0xFF;
	private int sourceIndex=0;
	private StringBuilder sBuffer;
	private StringBuilder ciBuffer;
	private int hdcpkeyNameLength=0;
	private int cikeyNameLength=0;
	private StringBuilder macStrBuffer;
	private StringBuilder SW_Version;

	private int mackeyNameLength=0;
	private int SWversionNameLength=0;
	
	private TvAtscChannelManager mTvAtscChannelManager = null;
	TvChannelManager mTvChannelManager = null;
	private int[][] srcCmdTbl={
			{TvCommonManager.INPUT_SOURCE_DTV,0x81},
			{TvCommonManager.INPUT_SOURCE_ATV,0x84},
			{TvCommonManager.INPUT_SOURCE_VGA,0x90},
			{TvCommonManager.INPUT_SOURCE_CVBS,0x85},
			{TvCommonManager.INPUT_SOURCE_CVBS2,0x86},
			{TvCommonManager.INPUT_SOURCE_CVBS3,0x87},
			{TvCommonManager.INPUT_SOURCE_YPBPR,0x89},
			{TvCommonManager.INPUT_SOURCE_YPBPR2,0x8A},
			{TvCommonManager.INPUT_SOURCE_YPBPR3,0x8B},
			{TvCommonManager.INPUT_SOURCE_HDMI,0x8C},
			{TvCommonManager.INPUT_SOURCE_HDMI2,0x8D},
			{TvCommonManager.INPUT_SOURCE_HDMI3,0x8E},
			{TvCommonManager.INPUT_SOURCE_HDMI4,0x8F},
			{TvCommonManager.INPUT_SOURCE_SCART,0x93},
			{TvCommonManager.INPUT_SOURCE_SCART2,0x94},
			{TvCommonManager.INPUT_SOURCE_SVIDEO,0x88},
	};
	
	
	private boolean isHideCIInfo = true;
	private OnEventListener mOnEventListener = new OnEventListener() {
		
		@Override
		public boolean onEvent(Message msg) {
			// TODO Auto-generated method stub
			Log.d(TAG, "Message: " + msg.what + " Message.arg1: " + msg.arg1+" "+msg.arg2);
			mBackGround.setBackgroundColor(Color.TRANSPARENT);
			mTestView.setText("");
			if (msg.what == 0x20001023)
			{
				isNeedFeedback=true;
				isHideCIInfo = true;
				switch (msg.arg1) {
				case AllTestName.MESSAGE_INPUT_SOURCE_DVBS:
					//int curTvSystem = TvCommonManager.getInstance().getCurrentTvSystem(); // add by wjc 20161027
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV)
					{
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
						
					}
					TvDvbChannelManager.getInstance().setDtvAntennaType(2);
					TvChannelManager.getInstance().changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_DTV:
//					mTestView.setText("Current input source: DTV");
					int mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem(); // add by wjc 20161027
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV)
					{
					 	if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
							Log.d(TAG, "mTvSystem == TvCommonManager.TV_SYSTEM_ISDB ATV_ANTENNA_TYPE_AIR");
							TvChannelManager.getInstance().setNTSCAntennaType(TvChannelManager.ATV_ANTENNA_TYPE_AIR);
						}
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
						
						// add by wjc 20161027 start
						if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
							Log.d(TAG, "mTvSystem == TvCommonManager.TV_SYSTEM_ATSC");
				            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
				            mTvAtscChannelManager.setDtvAntennaType(1); //1->air      2->cable
							Log.d(TAG, "mTvAtscChannelManager.getDtvAntennaType() = " + mTvAtscChannelManager.getDtvAntennaType());
							int mServiceNum = mTvAtscChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
							for (int i = 0; i < mServiceNum; i++) {
								ProgramInfo pgi =  mTvAtscChannelManager.getProgramInfo(i);
								if(pgi.serviceType == TvAtscChannelManager.SERVICE_TYPE_DTV)
								{
									mTvAtscChannelManager.programSel(pgi.majorNum, pgi.minorNum);
									break;
								}
								
							}
//							mTvAtscChannelManager.changeToFirstService(
//			                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ALL,
//			                        TvChannelManager.FIRST_SERVICE_DEFAULT);
							Log.d(TAG, "TvChannelManager.getInstance().getCurrentChannelNumber() = " + TvChannelManager.getInstance().getCurrentChannelNumber());
				        } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
							Log.d(TAG, "mTvSystem == TvCommonManager.TV_SYSTEM_ISDB");
							TvChannelManager.getInstance().changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
	                                TvChannelManager.FIRST_SERVICE_DEFAULT);
						}
						else {
							Log.d(TAG, "TvChannelManager.getInstance().getCurrentChannelNumber() = " + TvChannelManager.getInstance().getCurrentChannelNumber());
							TvChannelManager.getInstance().selectProgram(TvChannelManager.getInstance().getCurrentChannelNumber(),
									TvChannelManager.SERVICE_TYPE_DTV);
						}
						// wjc 20161027 end
					}
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_ATV:
//					mTestView.setText("Current input source: ATV");
					// add by wjc 20161027 start 
					mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
						Log.d(TAG, "setInputSource = ATV");
						if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
							mTvAtscChannelManager.setDtvAntennaType(2); //1->air      2->cable
							//TvChannelManager.getInstance().setNTSCAntennaType(0); //1->air      0->cable
							TvChannelManager.getInstance().changeToFirstService(
			                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ALL,
			                        TvChannelManager.FIRST_SERVICE_DEFAULT);
							int mServiceNum = mTvAtscChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
							for (int i = 0; i < mServiceNum; i++) {
								ProgramInfo pgi =  mTvAtscChannelManager.getProgramInfo(i);
								if(pgi.serviceType == TvAtscChannelManager.SERVICE_TYPE_ATV)
								{
									mTvAtscChannelManager.programSel(pgi.majorNum, pgi.minorNum);
									break;
								}
							}
						}
						else {
							
							int channel = TvChannelManager.getInstance().getCurrentChannelNumber();
							
							if(TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_NTSC_CC_ENABLE))
							{
								TvChannelManager.getInstance().setNTSCAntennaType(0); //1->air      0->cable
								TvChannelManager.getInstance().changeToFirstService(
		                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
		                            TvChannelManager.FIRST_SERVICE_DEFAULT);
																
								
							}
							
							if ((channel < 0) || (channel > 255)) {
								channel = 0;
							}
							TvChannelManager.getInstance().setAtvChannel(channel);
						}
					}
					// wjc 20161027 end
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_ATV_CABLE:
//					mTestView.setText("Current input source: ATV");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
					if(TvChannelManager.getInstance().getNTSCAntennaType() != EnumMedium.MEDIUM_CABLE.ordinal())
					{
						TvChannelManager.getInstance().setNTSCAntennaType(EnumMedium.MEDIUM_CABLE.ordinal());
					}
					int cable_ch=0;
					/*String strCableCh="";
					try {
						strCableCh=TvManager.getInstance().getEnvironment("atv_cable_ch");
					} catch (NumberFormatException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (TvCommonException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if(strCableCh != null)
					{
						cable_ch=Integer.parseInt(strCableCh)-1;
					}
					else*/
					{
						cable_ch= TvChannelManager.getInstance().getCurrentChannelNumber();
					}
					if ((cable_ch < 0) || (cable_ch > 255)) {
						cable_ch = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(cable_ch);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_ATV_AIR:
//					mTestView.setText("Current input source: ATV");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
					if(TvChannelManager.getInstance().getNTSCAntennaType() != EnumMedium.MEDIUM_AIR.ordinal())
					{
						TvChannelManager.getInstance().setNTSCAntennaType(EnumMedium.MEDIUM_AIR.ordinal());
					}
					int air_ch=0;
//					/*String strAirCh="";
//					try {
//						strAirCh=TvManager.getInstance().getEnvironment("atv_air_ch");
//					} catch (NumberFormatException e2) {
//						// TODO Auto-generated catch block
//						e2.printStackTrace();
//					} catch (TvCommonException e2) {
//						// TODO Auto-generated catch block
//						e2.printStackTrace();
//					}
//					if(strAirCh != null)
//						air_ch=Integer.parseInt(strAirCh)-1;
//					else*/
					{
						air_ch= TvChannelManager.getInstance().getCurrentChannelNumber();
					}
					if ((air_ch < 0) || (air_ch > 255)) {
						air_ch = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(air_ch);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_AV1:
//					mTestView.setText("Current input source: AV1");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_CVBS)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_CVBS);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_AV2:
//					mTestView.setText("Current input source: AV2");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_CVBS2)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_CVBS2);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_YUPBR:
//					mTestView.setText("Current input source: YUPBR");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_YPBPR)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_YPBPR);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_YUPBR2:
//					mTestView.setText("Current input source: YUPBR2");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_YPBPR2)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_YPBPR2);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_HDMI1:
//					mTestView.setText("Current input source: HDMI1");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_HDMI)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_HDMI2:
//					mTestView.setText("Current input source: HDMI2");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_HDMI2)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI2);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_HDMI3:
//					mTestView.setText("Current input source: HDMI3");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_HDMI3)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI3);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_HDMI4:
//					mTestView.setText("Current input source: HDMI4");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_HDMI4)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_HDMI4);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_VGA:
//					mTestView.setText("Current input source: VGA");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_VGA)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_VGA);
					testResult=1;
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_USB0_1:
//					mTestView.setText("Current input source:USB0/1");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_USB2:
//					mTestView.setText("Current input source:USB2");
					if(TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE2)
						TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE2);
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_SCART1:
//					mTestView.setText("Current input source: SCART1");
//					TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_SCART);
					break;
				case AllTestName.MESSAGE_INPUT_SOURCE_SCART2:
//					mTestView.setText("Current input source: SCART2");
//					TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_SCART2);
					break;
				case AllTestName.MESSAGE_KEYPED_TEST:
					/*short commendResult2[] = null;
                    try {
                        commendResult2 = TvManager.getInstance().setTvosCommonCommand("readKeypadAdcValue");
                    } catch (TvCommonException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                   if(commendResult2.length>0)
                   {
//                    Log.d("zhongbin","result-===="+commendResult2[0]);
                	   AdcValue=commendResult2[0];
                	   testResult=1;
                   }*/
                   if(keypadKeycode != 0xFF)
                   	{
						AdcValue=keypadKeycode;
                	   testResult=1;
                   }
                   else
                   {
                	   AdcValue=0xFF;
                	   testResult=0;
                   }
					break;
				case AllTestName.MESSAGE_FACTORY_RESTORE:
					FactoryRestory();
					break;
				case AllTestName.MESSAGE_CI_INFO:
					isHideCIInfo = false;
					int status = TvCiManager.CARD_STATE_NO;
				     status = TvCiManager.getInstance().getCiCardState();
					 if(status == TvCiManager.CARD_STATE_READY){
					 	/*
					 		mTestView.setText("CI INFO OK");
							mBackGround.setBackgroundColor(Color.GREEN);
							mTestView.setVisibility(View.VISIBLE);
							*/
						 FactoryShow.this.sendBroadcast(new Intent("com.mstar.tv.tvplayer.ui.intent.action.SHOW_CI_INFO"));
						 testResult = 0X81;
					 }else{

						 mTestView.setText("CI INFO NG");
						 mBackGround.setBackgroundColor(Color.RED);
						 mTestView.setVisibility(View.VISIBLE);
						 testResult=0X80;
					 }
                     Log.d(TAG,"test CI info");
					break;
				//case AllTestName.MESSAGE_UPDATA_MAC_KEY:
				case AllTestName.MESSAGE_UPDATA_MAC_KEY_BY_USB:	
					  short commendResult[] = null;
						try {
							commendResult = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_MAC_DIRECT);
						} catch (TvCommonException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						testResult=commendResult[0];
//						Log.d("zhongbin","mac key==="+testResult);
						if(commendResult[0] == 0){
	                    	testResult=1;
	                        Handler handler = new Handler(Looper.getMainLooper());
	                        handler.post(new Runnable() {
	                            public void run() {
//	                                Toast.makeText(getBaseContext(), "update HDCP key Success !", Toast.LENGTH_SHORT).show();
	                            	try {
	                            		mackeyName=IniFileReadWrite.getProfileString("/license/macandci.ini",
												"mac",
												"macdisplay",
												"Please update mac");
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									mTestView.setText("MAC:"+mackeyName);
									mBackGround.setBackgroundColor(Color.GREEN);
									mTestView.setVisibility(View.VISIBLE);
									
									macStrBuffer = new StringBuilder();
									byte[] by = mackeyName.getBytes();
									for (int i = 0; i < by.length; i++) {
										macStrBuffer.append(Integer.toHexString(by[i]));
									}
									mackeyNameLength=by.length;
									
									if(isNeedFeedback)
										sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,AllTestName.MESSAGE_UPDATA_MAC_KEY_BY_USB,testResult);
								}
	                        });
						}else{
							testResult=2;
							mackeyNameLength=0;
							macStrBuffer = new StringBuilder();
							macStrBuffer.append("");
	                    	
							mTestView.setText("Notice!update MAC key Fail !");
							mBackGround.setBackgroundColor(Color.RED);
							mTestView.setVisibility(View.VISIBLE);
							
							if(isNeedFeedback)
								sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,AllTestName.MESSAGE_UPDATA_MAC_KEY_BY_USB,testResult);
						}
					break;
				case AllTestName.MESSAGE_UPDATA_CI_PLUS_KEY:
					short commendResult2[] = null;
                    try {
                    	commendResult2 = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_CIKEY_DIRECT);
                    } catch (TvCommonException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(commendResult2[0] == 0){
                    	testResult=1;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
//                                Toast.makeText(getBaseContext(), "update HDCP key Success !", Toast.LENGTH_SHORT).show();
                            	try {
									cikeyName=IniFileReadWrite.getProfileString("/license/macandci.ini",
											"ci",
											"cidisplay",
											"Please update hdcp");
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								mTestView.setText("CIKEY:"+cikeyName);
								mBackGround.setBackgroundColor(Color.GREEN);
								mTestView.setVisibility(View.VISIBLE);
								
								ciBuffer = new StringBuilder();
								byte[] by = cikeyName.getBytes();
								for (int i = 0; i < by.length; i++) {
									ciBuffer.append(Integer.toHexString(by[i]));
								}
								cikeyNameLength=by.length;
								
								SharedPreferences sp = getSharedPreferences("SP", Activity.MODE_WORLD_READABLE|Activity.MODE_WORLD_WRITEABLE);
								Editor editer = sp.edit();
								int num = sp.getInt("update", 0);
								num ++;
								editer.putInt("update",num);
								editer.commit();
								if(isNeedFeedback)
									sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,AllTestName.MESSAGE_UPDATA_CI_PLUS_KEY,testResult);
							}
                        });
                    }else{
                    	testResult=2;
                    	cikeyNameLength=0;
                    	ciBuffer = new StringBuilder();
                    	ciBuffer.append("");
						
						mTestView.setText("Notice!update CI key Fail !");
						mBackGround.setBackgroundColor(Color.RED);
						mTestView.setVisibility(View.VISIBLE);
						
						if(isNeedFeedback)
							sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,AllTestName.MESSAGE_UPDATA_CI_PLUS_KEY,testResult);
                    }
					break;
				
				case AllTestName.MESSAGE_UPDATA_HDCP_KEY:
					short commendResult1[] = null;
                    try {
                        commendResult1 = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_HDCPKEY_DIRECT);
                    } catch (TvCommonException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
//                    Log.d("zhongbin","update HDCP key:" + commendResult1[0]);
                    if(commendResult1[0] == 0){
                    	testResult=1;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
//                                Toast.makeText(getBaseContext(), "update HDCP key Success !", Toast.LENGTH_SHORT).show();
                            	try {
									hdcpkeyName=IniFileReadWrite.getProfileString("/license/macandci.ini",
											"hdcp",
											"hdcpdisplay",
											"Please update hdcp");
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								mTestView.setText("HDCPKEY:"+hdcpkeyName);
								mBackGround.setBackgroundColor(Color.GREEN);
								mTestView.setVisibility(View.VISIBLE);
								
								sBuffer = new StringBuilder();
								byte[] by = hdcpkeyName.getBytes();
								for (int i = 0; i < by.length; i++) {
									sBuffer.append(Integer.toHexString(by[i]));
								}
								hdcpkeyNameLength=by.length;
								
								SharedPreferences sp = getSharedPreferences("SP", Activity.MODE_WORLD_READABLE|Activity.MODE_WORLD_WRITEABLE);
								Editor editer = sp.edit();
								int num = sp.getInt("update", 0);
								num ++;
								editer.putInt("update",num);
								editer.commit();
								if(isNeedFeedback)
									sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,AllTestName.MESSAGE_UPDATA_HDCP_KEY,testResult);
							}
                        });
                    }else{
                    	testResult=2;
                    	hdcpkeyNameLength=0;
                    	sBuffer = new StringBuilder();
                    	sBuffer.append("");
						
						mTestView.setText("Notice!update HDCP key Fail !");
						mBackGround.setBackgroundColor(Color.RED);
						mTestView.setVisibility(View.VISIBLE);
						
						if(isNeedFeedback)
							sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,AllTestName.MESSAGE_UPDATA_HDCP_KEY,testResult);
                    }
					break;
				case AllTestName.MESSAGE_ETHERNET_TEST:
					getEthStatus();
					break;
				case AllTestName.MESSAGE_WIFI_TEST:
					if (wifiManager.isWifiEnabled() == false) {
						wifiManager.setWifiEnabled(true);
						wifiManager.startScan();

						msg.what = 3721;
						try {
							//handler.sendEmptyMessage(msg.what);
							Thread.sleep(100);

				        } catch (InterruptedException e) {
				            // TODO Auto-generated catch block
				            e.printStackTrace();
				        }
						List<ScanResult> scanResults = wifiManager.getScanResults();
						if (scanResults.size() > 0) {
							mPBar.setVisibility(View.INVISIBLE);
							mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
							mTestView.setText(getString(R.string.str_factory_test_wifi));
							mTestView.setVisibility(View.VISIBLE);
							testResult=1;
						} else {
							mPBar.setVisibility(View.INVISIBLE);
							mBackGround.setBackgroundColor(android.graphics.Color.RED);
							mTestView.setText(getString(R.string.str_factory_test_wifi_ng));
							mTestView.setVisibility(View.VISIBLE);
							testResult=0;
						}
					} else {
						mPBar.setVisibility(View.VISIBLE);
						msg.what = 3721;
						try {
							//handler.sendEmptyMessage(msg.what);

							Thread.sleep(100);
				        } catch (InterruptedException e) {
				            // TODO Auto-generated catch block
				            e.printStackTrace();
				        }
						List<ScanResult> scanResults = wifiManager.getScanResults();
						if (scanResults.size() > 0) {
							mPBar.setVisibility(View.INVISIBLE);
							mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
							mTestView.setText(getString(R.string.str_factory_test_wifi));
							mTestView.setVisibility(View.VISIBLE);
							testResult=1;
						} else {
							mPBar.setVisibility(View.INVISIBLE);
							mBackGround.setBackgroundColor(android.graphics.Color.RED);
							mTestView.setText(getString(R.string.str_factory_test_wifi_ng));
							mTestView.setVisibility(View.VISIBLE);
							testResult=0;
						}
					}
					break;
				case AllTestName.MESSAGE_LNB_13V:
						{
					//Log.d("hexing","MESSAGE_LNB_13V");
					TvDvbChannelManager.getInstance().setDishLNBPowerMode(2);//13V
					//Log.d("hexing","setDishLNBPowerMode(2)" + TvDvbChannelManager.getInstance().setDishLNBPowerMode(2));
					mTestView.setText("Set LNB 13V");
					mBackGround.setBackgroundColor(Color.GREEN);
					mTestView.setVisibility(View.VISIBLE);
					testResult=2;
					//Log.d("hexing","aaaaaaaaaaaaaaaaaaaaaaaaasetDishLNBPowerMode(2) 13v");
					}
					break;
				case AllTestName.MESSAGE_LNB_18V:
						{
					//Log.d("hexing","MESSAGE_LNB_18V");
					TvDvbChannelManager.getInstance().setDishLNBPowerMode(3);//18V
					//Log.d("hexing","setDishLNBPowerMode(3)" + TvDvbChannelManager.getInstance().setDishLNBPowerMode(3));
					mTestView.setText("Set LNB 18V");
					mBackGround.setBackgroundColor(Color.GREEN);
					mTestView.setVisibility(View.VISIBLE);
					testResult=2;
					//Log.d("hexing","aaaaaaaaaaaaaaaaaaaaasetDishLNBPowerMode(2) 18v");
					}
					break;
				case AllTestName.MESSAGE_LNB_OFF:
						{
					//Log.d("hexing","MESSAGE_LNB_OFF");
					TvDvbChannelManager.getInstance().setDishLNBPowerMode(0);//0V
					//Log.d("hexing","setDishLNBPowerMode(0)" + TvDvbChannelManager.getInstance().setDishLNBPowerMode(0));
					mTestView.setText("Set LNB OFF");
					mBackGround.setBackgroundColor(Color.GREEN);
					mTestView.setVisibility(View.VISIBLE);
					testResult=2;
					//Log.d("hexing","aaaaaaaaaaaaaaaaaaaaaaaaaaasetDishLNBPowerMode(2) off");
					}
					break;
				case AllTestName.MESSAGE_USB_TEST_USB1:	
					mTestView.setText("USB1");
					if(Factorytest.checkDisk("2-1:1.0"))
					{
						mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
						testResult=1;
					}
					else 
					{
						mBackGround.setBackgroundColor(android.graphics.Color.RED);
						testResult=2;
					}
					break;
				case AllTestName.MESSAGE_USB_TEST_USB2:
					mTestView.setText("USB2");
					if (Factorytest.checkDisk("3-1:1.0")) 
					{
						mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
						testResult=1;
					}
					else 
					{
						mBackGround.setBackgroundColor(android.graphics.Color.RED);
						testResult=2;
					}
					break;
				case AllTestName.MESSAGE_USB_TEST_USB3://used for wifi
					mTestView.setText("USB3");
					if (Factorytest.checkDisk("1-1:1.0")) 
					{
						mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
						testResult=1;
					}
					else 
					{
						mBackGround.setBackgroundColor(android.graphics.Color.RED);
						testResult=2;
					}
					break;
				case AllTestName.MESSAGE_USB_TEST_USB4:
					break;
				case AllTestName.MESSAGE_SDcard_TEST:
					mTestView.setText("SDcard");
					if (isExternalStorageMounted()) 
					{
						mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
						testResult=1;
					}
					else 
					{
						mBackGround.setBackgroundColor(android.graphics.Color.RED);
						testResult=2;
					}
					break;
				case AllTestName.MESSAGE_SW_Version:
					
					Log.d(TAG, "Look SW_Version!");
					testResult=1;
//					 Handler handler = new Handler(Looper.getMainLooper());
//		             handler.post(new Runnable() {
//		                 public void run() {
		                	Long timestamp;  
		             		SimpleDateFormat df; 
		            		String date = new String();
                 
                 			timestamp = Long.parseLong(SystemProperties.get("ro.build.date.utc", "1420008801"))*1000;
                 			df = new SimpleDateFormat("EEE, dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                 			df.setTimeZone(TimeZone.getTimeZone("GMT+08:00")); 
                 			date =df.format(new java.util.Date(timestamp));
		                 			
                 			Log.d(TAG, " timestamp: " + timestamp + " date: " + date);	
							
                 			SW_Version = new StringBuilder();
                 			byte[] by = date.getBytes();
							for (int i = 0; i < by.length; i++) {
								SW_Version.append(Integer.toHexString(by[i]));
							}
							SWversionNameLength=by.length;
                 					                 
		                 	mTestView.setText("SW_Version: " + date);
							mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
							mTestView.setVisibility(View.VISIBLE);
//		                 }
//		             });

					break;
				case AllTestName.MESSAGE_UPGRADE_SW:
					break;
				case AllTestName.MESSAGE_IR_TEST:
					if(irKeycode!=0xFF)
					{
						testResult=0x81;
					}
					else
						testResult=0x80;
					irKeycode=0xFF;
					break;
				case AllTestName.MESSAGE_FEEDBACK:
					isNeedFeedback=false;
					break;
				default:
					isHideCIInfo = false;
					mTestView.setText("Current input source: NONE");
					mBackGround.setBackgroundColor(android.graphics.Color.RED);
					break;
				}
				//add by chenwenlong 20170705 for hide CI info UI
				if (isHideCIInfo) {
					FactoryShow.this.sendBroadcast(new Intent("com.mstar.tv.tvplayer.ui.intent.action.HIDE_CI_INFO"));
					isHideCIInfo = false;
				}
				// add end
				if(isNeedFeedback && msg.arg1 != AllTestName.MESSAGE_UPDATA_HDCP_KEY 
						&& msg.arg1 != AllTestName.MESSAGE_UPDATA_MAC_KEY_BY_USB
						&& msg.arg1 != AllTestName.MESSAGE_UPDATA_CI_PLUS_KEY)
					sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,msg.arg1,testResult);
			}
			else if(msg.what == 0x20001024)
			{
				//if(msg.arg1 == AllTestName.MESSAGE_UPDATA_MAC_KEY)
				{
					isNeedFeedback=true;
					
					if(msg.arg1==0x868201)
					{
						try {
							mackeyName=IniFileReadWrite.getProfileString("/license/macandci.ini",
									"mac",
									"macdisplay",
									"Please update mac");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						mTestView.setText("MAC2:"+mackeyName);
						mBackGround.setBackgroundColor(Color.GREEN);
						mTestView.setVisibility(View.VISIBLE);
					}
					else
					{
						mTestView.setText("update MAC key failed !");
						mBackGround.setBackgroundColor(Color.RED);
						mTestView.setVisibility(View.VISIBLE);
					}
					if(isNeedFeedback)
						sendFeedbackToPc(AllTestName.TEST_MODE_MAC,msg.arg1,testResult);
				}
			}
			return false;
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 3721) {
				WifiManager wifiManager = getWifiManager();
				List<ScanResult> scanResults = wifiManager.getScanResults();
				if (scanResults.size() > 0) {
					mPBar.setVisibility(View.INVISIBLE);
					mBackGround.setBackgroundColor(android.graphics.Color.GREEN);
					mTestView.setText(getString(R.string.str_factory_test_wifi));
					mTestView.setVisibility(View.VISIBLE);
					testResult=1;
				} else {
					mPBar.setVisibility(View.INVISIBLE);
					mBackGround.setBackgroundColor(android.graphics.Color.RED);
					mTestView.setText(getString(R.string.str_factory_test_wifi_ng));
					mTestView.setVisibility(View.VISIBLE);
					testResult=0;
				}
			}
		}
	};

	BroadcastReceiver powerReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			keypadKeycode = 115;
			Log.d("lcp", "PowerKeycode: " + Integer.toString(keypadKeycode));
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.factory_show);
		mBackGround = (LinearLayout) findViewById(R.id.background);
		mTestView = (TextView) findViewById(R.id.factorytest_text);
		mPBar = (ProgressBar) findViewById(R.id.progressBar);
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		mTvManager = TvManager.getInstance();
		mTvManager.setOnEventListener(mOnEventListener);
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.toptech.factorytools.FactoryShow.poweroffBroadcast");
		registerReceiver(powerReceiver, filter);
		Bundle bundle = getIntent().getExtras();
		Message msg = handler.obtainMessage();
		mType = bundle.getInt(TYPE, 0);
		mStorageManager = StorageManager.from(this);
		Log.d("lcp", "<<<<<<<Enter FactoryShow>>>>>>>");
		
		int curInputSrc=TvCommonManager.getInstance().getCurrentTvInputSource();
		for( int i=0;i<srcCmdTbl.length;i++)
		{
			if(curInputSrc==srcCmdTbl[i][0])
			{
				isNeedFeedback=true;
				testResult=1;
				sourceIndex=i;
				
				if(isNeedFeedback)
				{
					new Thread(new Runnable() {
						@Override
						public void run() {
							sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,0x81,testResult);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS,0x820000|(srcCmdTbl[FactoryShow.this.sourceIndex][1]<<8),testResult);
						}
					}).start();
					
					break;
				}
			}
		}
		
		Log.d("lcp", "<<<<<<<mType = "+ mType +">>>>>>>");
		switch (mType) {
		case CONNECT_NOT_TYPE:

			break;
		case CONNECT_ETHERNET_TYPE:
			getEthStatus();
			break;
		case CONNECT_WIFI_TYPE:
			if (wifiManager.isWifiEnabled() == false) {
				wifiManager.setWifiEnabled(true);
				wifiManager.startScan();
				mPBar.setVisibility(View.VISIBLE);
				msg.what = 3721;
				handler.sendEmptyMessageDelayed(msg.what, 5000);
			} else {
				mPBar.setVisibility(View.VISIBLE);
				msg.what = 3721;
				handler.sendEmptyMessageDelayed(msg.what, 100);
			}
			break;
		case CONNECT_ETHERNET_MAC_TYPE:
			 short commendResult[] = null;
				try {
					commendResult = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_MAC_DIRECT);
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("LCP","update MAC key:" + commendResult[0]);
				if(commendResult[0] == 0){
                 Handler handler = new Handler(Looper.getMainLooper());
                 handler.post(new Runnable() {
                     public void run() {
//                         Toast.makeText(getBaseContext(), "update HDCP key Success !", Toast.LENGTH_SHORT).show();
                     	try {
                     		mackeyName=IniFileReadWrite.getProfileString("/license/macandci.ini",
										"mac",
										"macdisplay",
										"Please update mac");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                     		
							mTestView.setText("MAC:"+mackeyName);
							mBackGround.setBackgroundColor(Color.GREEN);
							mTestView.setVisibility(View.VISIBLE);
						}
                 });
				}else{
					testResult=2;
					mackeyNameLength=0;
					macStrBuffer = new StringBuilder();
					macStrBuffer.append("");
             	
					mTestView.setText("notice! update MAC key fail!");
					mBackGround.setBackgroundColor(Color.RED);
					mTestView.setVisibility(View.VISIBLE);
					
				}
			break;
			
		case CONNECT_HDCP_KEY_TYPE:
			short commendResult1[] = null;
            try {
                commendResult1 = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_HDCPKEY_DIRECT);
            } catch (TvCommonException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("LCP","update HDCP key:" + commendResult1[0]);
            if(commendResult1[0] == 0){

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
//                        Toast.makeText(getBaseContext(), "update HDCP key Success !", Toast.LENGTH_SHORT).show();
                    	try {
							hdcpkeyName=IniFileReadWrite.getProfileString("/license/macandci.ini",
									"hdcp",
									"hdcpdisplay",
									"Please update hdcp");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						mTestView.setText("HDCPKEY:"+hdcpkeyName);
						mBackGround.setBackgroundColor(Color.GREEN);
						mTestView.setVisibility(View.VISIBLE);
					}
                });
            }else{

				
				mTestView.setText("Notice!update HDCP key Fail !");
				mBackGround.setBackgroundColor(Color.RED);
				mTestView.setVisibility(View.VISIBLE);

            }
			break;
		case CONNECT_CI_KEY_TYPE:
			short commendResult2[] = null;
            try {
				Log.d("LCP","1040");
                commendResult2 = TvManager.getInstance().setTvosCommonCommand(TvManager.TVOS_COMMON_CMD_LOAD_CIKEY_DIRECT);
            } catch (TvCommonException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("LCP","update CI key:" + commendResult2[0]);
            if(commendResult2[0] == 0){

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                    	try {
							cikeyName=IniFileReadWrite.getProfileString("/license/macandci.ini",
									"ci",
									"cidisplay",
									"Please update ci");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						mTestView.setText("CIKEY:"+cikeyName);
						mBackGround.setBackgroundColor(Color.GREEN);
						mTestView.setVisibility(View.VISIBLE);
					}
                });
            }else{
				mTestView.setText("Notice!update CI key Fail !");
				mBackGround.setBackgroundColor(Color.RED);
				mTestView.setVisibility(View.VISIBLE);

            }
			break;
		default:
			break;
		}
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		handler.removeMessages(3721);
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
		
		 Log.d("lcp", "deviceName: " + deviceName + " keyCode: " + Integer.toString(keyCode));
		
        if (deviceName.equals("MStar Smart TV Keypad")) {
        	
        	switch (keyCode) {
        	case 166:keypadKeycode = 10;break;
        	case 167:keypadKeycode = 25;break;
        	case 82:keypadKeycode = 40;break;
        	case 24:keypadKeycode = 55;break;
        	case 25:keypadKeycode = 70;break;
        	case 178:keypadKeycode = 85;break;
        	case 66:keypadKeycode = 100;break;
        	case 26:keypadKeycode = 115;break;
        	}
        	Log.d("lcp", "keypadKeycode: " + Integer.toString(keypadKeycode));
            return true;
        }
		else
			irKeycode=keyCode;
        
       
        
		switch (keyCode) {
		case KeyEvent.KEYCODE_FACTORY_WIFI:
			finish();
			return true;
		case KeyEvent.KEYCODE_FACTORY_ETHERNET:
			finish();
			return true;
		case KeyEvent.KEYCODE_FACTORY_MAC:
			finish();
			return true;
		case KeyEvent.KEYCODE_FACTORY_CI_KEY:
		case KeyEvent.KEYCODE_FACTORY_HDCP_KEY:
			finish();
			return true;
		case KeyEvent.KEYCODE_FACTORY_IR_TEST:
			//sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS, AllTestName.MESSAGE_IR_TEST, 81);
//			finish();
			return true;
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		
//		sendFeedbackToPc(AllTestName.TEST_MODE_OTHERS, AllTestName.MESSAGE_IR_TEST, 80);
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(powerReceiver);
		super.onDestroy();
	}

	/********************************************************************************************************/
	
	public WifiManager getWifiManager() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		return wifiManager;
	}

	private void getEthStatus() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

		if (netInfo == null) {
			mTestView.setText(getString(R.string.str_factory_test_eth_ng));
			Log.d(TAG, "++++++++ETHERNET=null+++++++");
			mBackGround.setBackgroundColor(android.graphics.Color.RED);
			mTestView.setVisibility(View.VISIBLE);
			testResult=0;
		} else {
			if (netInfo.getTypeName().equals("ETHERNET") || netInfo.getTypeName().equals("Ethernet")) {
				mBackGround.setBackgroundColor(Color.GREEN);
				mTestView.setText(getString(R.string.str_factory_test_eth));
				mTestView.setVisibility(View.VISIBLE);
				testResult=1;
			} else {
				mTestView.setText(getString(R.string.str_factory_test_eth_ng));
				Log.d(TAG, "++++++++ETHERNET=null+++++++");
				mBackGround.setBackgroundColor(android.graphics.Color.RED);
				mTestView.setVisibility(View.VISIBLE);
				testResult=0;
			}
		}
	}
	
    public static String getEthernetMacAddress() {
        String macAddr = "00:00:00:00:00:00";

        try {
            final String interfaceName = SystemProperties.get("ethernet.interface", "eth0");
            IBinder b = ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE);
            INetworkManagementService service =
                    INetworkManagementService.Stub.asInterface(b);
            macAddr = service.getInterfaceConfig(interfaceName).getHardwareAddress();
        } catch (RemoteException ex) {
            Log.w(TAG, "Exception getting ethernet config: " + ex);
        }

        return macAddr;
    }
    
    public void FactoryRestory()
    {
    	tvFactoryManager = TvFactoryManager.getInstance();
    	mTvCommonManager = TvCommonManager.getInstance();
        if (null != tvFactoryManager
                && null != mTvCommonManager) {
            if (tvFactoryManager.restoreToDefault() == true ) {
                int currInputSource = mTvCommonManager.getCurrentTvInputSource();
                if (currInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                    //tvS3DManager.setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                    if ((mTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ISDB)
                            && (mTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ATSC)) {
                        // Because restore to factory default value,reset routeIndex to 0
                        TvDvbChannelManager.getInstance().setDtvAntennaType(0);
                    }
                }

                // Reset First Startup Input Source
                SharedPreferences preferencesSettings = getSharedPreferences(AllTestName.PREFERENCES_INPUT_SOURCE, Context.MODE_PRIVATE);
                preferencesSettings.edit().remove(AllTestName.PREFERENCES_PREVIOUS_INPUT_SOURCE).commit();

                // Reset Setup Wizard
                preferencesSettings = getSharedPreferences(AllTestName.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
                preferencesSettings.edit().remove(AllTestName.PREFERENCES_IS_AUTOSCAN_LAUNCHED).commit();
                testResult=1;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        		try {
        			TvManager.getInstance().setEnvironment("db_table", "0");
        			TvManager.getInstance().setEnvironment("top_auto_pwr_on", "1");
        		} catch (TvCommonException e) {
        			e.printStackTrace();
        		}

        		mTvCommonManager.rebootSystem("reboot");
                /***** Ended by gerard.jiang 2013/04/28 *****/
            } else {
                Log.e(TAG, "restoreToDefault failed!");
            }
        }
    }//add end

    private void sendFeedbackToPc(int testMode, final int cmd, final int result)
	{
    	int sleeptime=0;
    	boolean needSleep=false;
    	
    	if(result>=0 && result<10)
    	{
    		tmpResult="0"+result;
    	}
    	else
    		tmpResult=""+Integer.toHexString(result);
    	
    	switch(testMode)
    	{
    		case AllTestName.TEST_MODE_OTHERS:
    			switch(cmd)
    	    	{
    			case AllTestName.MESSAGE_INPUT_SOURCE_DTV:
				case AllTestName.MESSAGE_INPUT_SOURCE_DVBS:
    			case AllTestName.MESSAGE_INPUT_SOURCE_ATV:
    			case AllTestName.MESSAGE_INPUT_SOURCE_ATV_CABLE:
    			case AllTestName.MESSAGE_INPUT_SOURCE_ATV_AIR:
    			case AllTestName.MESSAGE_INPUT_SOURCE_AV1:
    			case AllTestName.MESSAGE_INPUT_SOURCE_AV2:
    			case AllTestName.MESSAGE_INPUT_SOURCE_YUPBR:
    			case AllTestName.MESSAGE_INPUT_SOURCE_YUPBR2:
    			case AllTestName.MESSAGE_INPUT_SOURCE_HDMI1:
    			case AllTestName.MESSAGE_INPUT_SOURCE_HDMI2:
    			case AllTestName.MESSAGE_INPUT_SOURCE_HDMI3:
    			case AllTestName.MESSAGE_INPUT_SOURCE_HDMI4:
    			case AllTestName.MESSAGE_INPUT_SOURCE_VGA:
    			case AllTestName.MESSAGE_INPUT_SOURCE_USB0_1:
    			case AllTestName.MESSAGE_INPUT_SOURCE_USB2:
    			case AllTestName.MESSAGE_INPUT_SOURCE_SCART1:
    			case AllTestName.MESSAGE_INPUT_SOURCE_SCART2:
    			case AllTestName.MESSAGE_LNB_13V:
    			case AllTestName.MESSAGE_LNB_18V:
    			case AllTestName.MESSAGE_LNB_OFF:
    			case AllTestName.MESSAGE_USB_TEST_USB1:
    			case AllTestName.MESSAGE_USB_TEST_USB2:
    			case AllTestName.MESSAGE_USB_TEST_USB3:
    			case AllTestName.MESSAGE_USB_TEST_USB4:
    				cmdLength="05";
    				backCmd=(cmd>>8)&0xFFFF;
    				break;
    			case AllTestName.MESSAGE_UPDATA_HDCP_KEY:
    			{
    				int tmpLength=0x05+hdcpkeyNameLength;//3bytes head+ hdcpkeyNameLength+ 2bytes crc
    				if(tmpLength<=0x0F)
    					cmdLength="0"+Integer.toHexString(tmpLength);
    				else
    					cmdLength=Integer.toHexString(tmpLength); 
    				backCmd=(cmd>>8)&0xFFFF;
    			}
    				break;
    			case AllTestName.MESSAGE_UPDATA_CI_PLUS_KEY:
    			{
    				int tmpLength=0x05+cikeyNameLength;//3bytes head+ cikeyNameLength+ 2bytes crc
    				if(tmpLength<=0x0F)
    					cmdLength="0"+Integer.toHexString(tmpLength);
    				else
    					cmdLength=Integer.toHexString(tmpLength); 
    				backCmd=(cmd>>8)&0xFFFF;
    			}
    				break;
    			case AllTestName.MESSAGE_UPDATA_MAC_KEY_BY_USB:
    			{
    				int tmpLength=0x05+mackeyNameLength;//3bytes head+ mackeyNameLength + 2bytes crc
    				if(tmpLength<=0x0F)
    					cmdLength="0"+Integer.toHexString(tmpLength);
    				else
    					cmdLength=Integer.toHexString(tmpLength); 
    				backCmd=(cmd>>8)&0xFFFF;
    			}
    				break;
    			case AllTestName.MESSAGE_SW_Version:
    			{
    				int tmpLength=0x04 + SWversionNameLength;//3bytes head+ mackeyNameLength + 2bytes crc
    				if(tmpLength<=0x0F)
    					cmdLength="0"+Integer.toHexString(tmpLength);
    				else
    					cmdLength=Integer.toHexString(tmpLength); 
    				backCmd=(cmd>>16)&0xFF;
    				
    				Log.d(TAG, "SW_Version: " + SW_Version  + " SWNameLength: " + SW_Version.length()  + " cmdLength: " + cmdLength);
    			}
    				break;	
    			case AllTestName.MESSAGE_KEYPED_TEST:
    				cmdLength="05";
    				backCmd=(((cmd>>8)&0xFF00)|AdcValue);
    				break;
    			case AllTestName.MESSAGE_CI_INFO:
    			case AllTestName.MESSAGE_FACTORY_RESTORE:
    			case AllTestName.MESSAGE_ETHERNET_TEST:
    			case AllTestName.MESSAGE_WIFI_TEST:
    			case AllTestName.MESSAGE_UPGRADE_SW:
    			case AllTestName.MESSAGE_IR_TEST:
    			case AllTestName.MESSAGE_FEEDBACK:
    			case AllTestName.MESSAGE_SDcard_TEST:
    				cmdLength="04";
    				backCmd=(cmd>>16)&0xFF;
    				break;
    			case AllTestName.MESSAGE_TEST_VOLTAGE:
    				cmdLength="03";
    				backCmd=cmd;
    				tmpResult="";
    				break;
    	    	}
    			break;
    		case AllTestName.TEST_MODE_MAC:
    			backCmd=cmd;
    			cmdLength="05";
    			tmpResult="";
    			break;
    			
    			
    	}
    	
    	switch(cmd)
    	{
    	case AllTestName.MESSAGE_INPUT_SOURCE_DTV:
		case AllTestName.MESSAGE_INPUT_SOURCE_DVBS:
    	case AllTestName.MESSAGE_INPUT_SOURCE_YUPBR:
		case AllTestName.MESSAGE_INPUT_SOURCE_YUPBR2:
		case AllTestName.MESSAGE_INPUT_SOURCE_HDMI1:
		case AllTestName.MESSAGE_INPUT_SOURCE_HDMI2:
		case AllTestName.MESSAGE_INPUT_SOURCE_HDMI3:
		case AllTestName.MESSAGE_INPUT_SOURCE_HDMI4:
		case AllTestName.MESSAGE_INPUT_SOURCE_ATV:
		case AllTestName.MESSAGE_INPUT_SOURCE_ATV_CABLE:
		case AllTestName.MESSAGE_INPUT_SOURCE_ATV_AIR:
			sleeptime=1500;
			needSleep=true;
			break;
		
		case AllTestName.MESSAGE_INPUT_SOURCE_AV1:
		case AllTestName.MESSAGE_INPUT_SOURCE_AV2:
		case AllTestName.MESSAGE_INPUT_SOURCE_VGA:
			sleeptime=1000;
			needSleep=true;
			break;
    	}
    	
//    	Log.d("zhongbin","backCmd==="+backCmd+"  cmdLength=="+cmdLength);
    	
    	if(needSleep)
    	{
    		try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	new Thread(new Runnable() {
			int num =0;
			@Override
			public void run() {
				while(FactoryShow.this.isNeedFeedback && num <5)
		    	{
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(cmd == AllTestName.MESSAGE_UPDATA_HDCP_KEY)
						TvCommonManager.getInstance().setTvosCommonCommand("AAA75A"+cmdLength+Integer.toHexString(backCmd)+tmpResult+sBuffer);
					else if(cmd == AllTestName.MESSAGE_UPDATA_CI_PLUS_KEY)
						TvCommonManager.getInstance().setTvosCommonCommand("AAA75A"+cmdLength+Integer.toHexString(backCmd)+tmpResult+ciBuffer);
					else if(cmd == AllTestName.MESSAGE_UPDATA_MAC_KEY_BY_USB)
						TvCommonManager.getInstance().setTvosCommonCommand("AAA75A"+cmdLength+Integer.toHexString(backCmd)+tmpResult+macStrBuffer);
					else if(cmd == AllTestName.MESSAGE_SW_Version)
						TvCommonManager.getInstance().setTvosCommonCommand("AAA75A"+cmdLength+Integer.toHexString(backCmd)+tmpResult+SW_Version);
					else
						TvCommonManager.getInstance().setTvosCommonCommand("AAA75A"+cmdLength+Integer.toHexString(backCmd)+tmpResult);
					num ++;
		    	}
				irKeycode=0xFF;
				keypadKeycode=0xFF;
			}
		}).start();
    	
		
	}
    
    private boolean ExistSDCard() {
  	  if (android.os.Environment.getExternalStorageState().equals(
  	    android.os.Environment.MEDIA_MOUNTED)) {
  		Log.d(TAG,"SDCard Exist!");
  	   return true;
  	  } else
  		Log.d(TAG,"SDCard Not Exist!");
  	   return false;
  	 }
    
    public String getStorageState(String path) {
        try {
            StorageManager sm = (StorageManager) getSystemService(STORAGE_SERVICE);
            Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", new Class[] {String.class});
            String state = (String) getVolumeStateMethod.invoke(sm, path);
            return state;
        } catch (Exception e) {
            Log.e(TAG, "getStorageState() failed", e);
        }
        return null;
    }
    
    
    private boolean isExternalStorageMounted() {  
        final StorageVolume[] volumes = mStorageManager.getVolumeList(); 
        final String Path = "/mnt/other_sdcard";
        for (StorageVolume v : volumes) {
        	
        	Log.d(TAG,"ExternalStoragePath: " + v.getPath());
        	if(Path.equals(v.getPath()))
            if (v.isRemovable()) {  
                if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getStorageState(v.getPathFile()))){
                	Log.d(TAG,"ExternalStorage Exist!");
                	return true;
                }
                      
            }  
        }  
        return false;  
    }  
}
		
