//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package com.mstar.tvsetting.hotkey;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumSleepTimeState;

import com.mstar.tvsetting.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemProperties;
import android.os.SystemClock;

public class TVCounterActivity extends Activity {
    public static String TAG = "TVCounterActivity";

    public RootReceiver rootReceiver;

    private LinearLayout linearLayoutPopupOnTime;

    private TextView textViewOnTimeSecond;

    private LinearLayout linearLayoutPopupOffTime;

    private TextView textViewOffTimeSecond;

    private LinearLayout linearLayoutReadySwitch;

    private LinearLayout linearLayoutReadyOff;

    private LinearLayout linearLayoutPopupSystemCLKChange;

	//zb20160106 add
	private Thread countDownThread=null;
	private int inputSource=0;
	private int tvCounterTimeCounter = 0;
	private Thread countDownDetectThread = null;
	private boolean tvCounterHasDestory = false;
	private final int CLOSE_TVCOUNTER_MENU = 0x5555;
	MsgHandler tvCounterHandler = null;
	private boolean isKeyDown=false;
	//end
    private class RootReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action
                    .equals("com.android.server.tv.TIME_EVENT_POWER_DOWNTIME")) {
                TvTimerManager.getInstance().setOffTimerEnable(false);
                TvTimerManager.getInstance().setSleepTimeMode(
                        TvTimerManager.SLEEP_TIME_OFF);
                linearLayoutPopupOffTime.setVisibility(View.GONE);
                finish();
            }
            if (action
                    .equals("com.android.server.tv.TIME_EVENT_LAST_MINUTE_UPDATE")) {
                int remainSeconds = intent.getIntExtra("LeftTime", -1);
                linearLayoutPopupOffTime.setVisibility(View.VISIBLE);
                textViewOffTimeSecond.setText("" + remainSeconds);

				if(remainSeconds==1)
					SystemProperties.set("mstar.sys.countdown.pwroff", "true");
				
                if (remainSeconds == -1) {
                    Log.e(TAG, "get left time error!");
                    textViewOffTimeSecond.setText("ERROR");
                }
            }
            if (action
                    .equals("com.android.server.tv.TIME_EVENT_SYSTEM_CLOCK_CHANGE")) {
                Log.i(TAG, "close countdown window");
                // when system clock has be changed, cancel all.
                TvTimerManager.getInstance().setOffTimerEnable(false);
                TvTimerManager.getInstance().setSleepTimeMode(
                        TvTimerManager.SLEEP_TIME_OFF);
                linearLayoutPopupOffTime.setVisibility(View.GONE);
                linearLayoutPopupSystemCLKChange.setVisibility(View.VISIBLE);
                BroadcastRev.setTVCounterActivityStarted(false);
            }
			//zb20160106 add
			if (action
                    .equals("com.android.server.tv.TIME_EVENT_LAST_MINUTE_WARN")) {
                Log.d(TAG, "tvcounter get, time is"+SystemClock.currentTimeMicro());
                linearLayoutPopupOffTime.setVisibility(View.GONE);
                tvCounterTimeCounter = 0;
            }
			if (action
                    .equals("com.android.server.tv.TIME_EVENT_DESTROY_COUNT_DOWN")) {
                if (linearLayoutPopupOffTime.getVisibility() == View.VISIBLE) {
					if(BroadcastRev.getSleepTimeFlag())
					{
			            TvTimerManager.getInstance().setOffTimerEnable(false);
			            TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
						BroadcastRev.setSleepTimeFlag(false);
					}
					else
						BroadcastRev.setNoSignalAutoSleepFlag(false);
        		}
    			BroadcastRev.setNoSignalAutoSleepFlag(false);
                linearLayoutPopupOffTime.setVisibility(View.GONE);
                finish();
            }
			//end
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_counter);
        linearLayoutPopupOnTime = (LinearLayout) findViewById(R.id.linear_layout_popup_ontime);
        textViewOnTimeSecond = (TextView) findViewById(R.id.text_view_second);
        linearLayoutPopupOffTime = (LinearLayout) findViewById(R.id.linear_layout_popup_offtime);
        linearLayoutPopupSystemCLKChange = (LinearLayout) findViewById(R.id.linear_layout_popup_system_clkchange);

        textViewOffTimeSecond = (TextView) findViewById(R.id.text_view_offsecond);
        linearLayoutReadySwitch = (LinearLayout) findViewById(R.id.linear_layout_popup_ready_switch);
        linearLayoutReadyOff = (LinearLayout) findViewById(R.id.linear_layout_popup_ready_off);
        rootReceiver = new RootReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.server.tv.TIME_EVENT_DESTROY_COUNT_DOWN");
        filter.addAction("com.android.server.tv.TIME_EVENT_LAST_MINUTE_WARN");
        filter.addAction("com.android.server.tv.TIME_EVENT_LAST_MINUTE_UPDATE");
        filter.addAction("com.android.server.tv.TIME_EVENT_POWER_DOWNTIME");
        // ADD FOR SUPPORT SYSTEM CLOCK CHANGE
        filter.addAction("com.android.server.tv.TIME_EVENT_SYSTEM_CLOCK_CHANGE");
        this.registerReceiver(rootReceiver, filter);

		//zb20160106 add
		SystemProperties.set("mstar.sys.countdown", "true");
		//fch20170808 add  mantis:0034024
		SystemProperties.set("persist.sys.power2off", "true");
		//end fch
		tvCounterTimeCounter = 0;
        tvCounterHasDestory = false;
        tvCounterHandler = new MsgHandler();
        countDownDetectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!tvCounterHasDestory) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                    	if(BroadcastRev.getNoSignalAutoSleepFlag())
                    	{
							if(TvManager.getInstance().getPlayerManager().isSignalStable())
							{
								tvCounterHandler.sendEmptyMessageDelayed(
		                                CLOSE_TVCOUNTER_MENU, 0);
								isKeyDown=true;
							}
                    	}
					} catch (TvCommonException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        });

        if (countDownDetectThread != null) {
            countDownDetectThread.start();
        }
        
        isKeyDown=false;
        inputSource=TvCommonManager.getInstance().getCurrentTvInputSource();
        countDownThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				int count=60;
                while(count>0&&BroadcastRev.getNoSignalAutoSleepFlag())
                {
                	if(isKeyDown)
                	{
                		isKeyDown=false;
                		count=0;
                		break;
                	}
                	Intent intent = new Intent("com.android.server.tv.TIME_EVENT_LAST_MINUTE_UPDATE");
                    intent.putExtra("LeftTime", count);
                    sendBroadcast(intent);
                	count--;
                	try
                	{
                		Thread.sleep(1000);
                	}
            		catch (Exception e) {
						// TODO: handle exception
					}
                	if(count<=0)
                	{
                		finish();
                		if (inputSource == TvCommonManager.INPUT_SOURCE_VGA
                                || inputSource == TvCommonManager.INPUT_SOURCE_HDMI
                                || inputSource == TvCommonManager.INPUT_SOURCE_HDMI2
                                || inputSource == TvCommonManager.INPUT_SOURCE_HDMI3
                                || inputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
                            TvCommonManager.getInstance().setDpmsWakeUpEnable(true);
                        }
						SystemProperties.set("mstar.sys.countdown.pwroff", "true");
						SystemProperties.set("mstar.sys.countdown", "false");
						if (SystemProperties.getBoolean("mstar.sys.recording", false)) {
						    SystemProperties.set("mstar.sys.recording", "false");
                            TvCommonManager.getInstance().standbySystem("pvr");
						} else {
						TvCommonManager.getInstance().standbySystem("standby");
						}
                	}
                }
				
			}
		});
        if(countDownThread !=null)
        	countDownThread.start();
        //end
    }

    @Override
    protected void onResume()
    {
        //zb20160106 add
		//SystemProperties.set("mstar.sys.countdown", "true");
		super.onResume();
    }

    @Override
    public void onUserInteraction() {
    	//SystemProperties.set("mstar.sys.countdown", "false");
    	isKeyDown=true;
        if (linearLayoutPopupOffTime.getVisibility() == View.VISIBLE) {
			if(BroadcastRev.getSleepTimeFlag())
			{
	            TvTimerManager.getInstance().setOffTimerEnable(false);
	            TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
				BroadcastRev.setSleepTimeFlag(false);
			}
			else
				BroadcastRev.setNoSignalAutoSleepFlag(false);
            //Toast Hint = new Toast(this);
           // Toast.makeText(this, "User disable off timer!", Toast.LENGTH_LONG)
            //        .show();
        }
        if (linearLayoutPopupOnTime.getVisibility() == View.VISIBLE) {
            TvTimerManager.getInstance().setOnTimerEnable(false);
        }

        super.onUserInteraction();
        finish();
    }

    @Override
    protected void onPause()
    {
        //zb20160106 add
    	//SystemProperties.set("mstar.sys.countdown", "false");
		super.onPause();
    }

    @Override
    protected void onDestroy() {
    	//zb20160106 add
    	// move to on pause
    	SystemProperties.set("mstar.sys.countdown", "false");
		//fch20170808 add  mantis:0034024
		SystemProperties.set("persist.sys.power2off", "false");
		//end fch
    	tvCounterHasDestory = true;
		if(BroadcastRev.getSleepTimeFlag())
		{
            TvTimerManager.getInstance().setOffTimerEnable(false);
            TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
			BroadcastRev.setSleepTimeFlag(false);
		}
		else
			BroadcastRev.setNoSignalAutoSleepFlag(false);
		//end
        BroadcastRev.setTVCounterActivityStarted(false);
        this.unregisterReceiver(rootReceiver);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
		isKeyDown=true;
		if(BroadcastRev.getSleepTimeFlag())
		{
            TvTimerManager.getInstance().setOffTimerEnable(false);
            TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
			BroadcastRev.setSleepTimeFlag(false);
		}
		else
			BroadcastRev.setNoSignalAutoSleepFlag(false);
        finish();
        return true;
    }
	 class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == CLOSE_TVCOUNTER_MENU) {
                finish();
            }

            super.handleMessage(msg);
        }
    }
}
