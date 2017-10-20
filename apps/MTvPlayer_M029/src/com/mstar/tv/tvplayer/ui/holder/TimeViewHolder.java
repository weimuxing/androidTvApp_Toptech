//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.tvplayer.ui.holder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.text.format.Time;
import android.util.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.vo.StandardTime;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.MyButton;
import com.mstar.tv.tvplayer.ui.settings.SetTimeOffDialogActivity;
import com.mstar.tv.tvplayer.ui.settings.SetTimeOnDialogActivity;

public class TimeViewHolder {
    private static final String TAG = "TimeViewHolder";

    protected boolean isClockExisted = false;

    protected Timer timer = new Timer();

    protected ComboButton comboBtnDate;

    protected ComboButton comboBtnCurrentTime;

    protected MyButton myBtnOffTime;

    protected MyButton myBtnScheduledTime;

    protected ComboButton comboBtnSleepTime;

    private TvTimerManager tvTimerManager = null;

    protected Activity activity;

    protected Handler handler;

    private static final int UPDATE_CURRENT_TIME = 10086;

	// add by wsw
	private TimeZone mTimeZone;
	private Calendar mTime;
	private String date;
	private String clock;
	// for update time

    public TimeViewHolder(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
        tvTimerManager = TvTimerManager.getInstance();
		// add by wsw
		mTimeZone = TimeZone.getDefault();
    }

	//add by wsw
	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
	@Override
		public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
			final String timeZone = intent.getStringExtra("time-zone");
			Log.d("ppan","intent right :" + timeZone);
			}
		}
	};
	/*

    public void toHandleMsg(Message msg) {
        switch (msg.what) {
            case UPDATE_CURRENT_TIME:
                Time curTime = tvTimerManager.getCurrentTvTime();
                comboBtnDate.setTextInChild(2, curTime.format("%Y/%m/%d"));
                comboBtnCurrentTime.setTextInChild(2, curTime.format("%H:%M:%S"));
                break;
            default:
                break;
        }
    }
    */
   //add by wsw
   public void toHandleMsg(Message msg) {
   	switch (msg.what) {
   		case UPDATE_CURRENT_TIME:
			onTimeChanged();
			comboBtnDate.setTextInChild(2, date);
			comboBtnCurrentTime.setTextInChild(2, clock);
			break;
		default:
			break;
		}
   } 

    public void findViews() {
        myBtnOffTime = new MyButton(activity, R.id.linearlayout_time_offtime);
        myBtnScheduledTime = new MyButton(activity, R.id.linearlayout_time_schedule);
        myBtnOffTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (true == ActivityManager.isUserAMonkey()) {
                    Log.i(TAG, "monkey is running, ignore to launch SetTimeOffDialogActivity!");
                } else {
                    Intent intent = new Intent(activity, SetTimeOffDialogActivity.class);
                    activity.startActivity(intent);
                }
                activity.finish();
            }
        });
        myBtnScheduledTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(activity, SetTimeOnDialogActivity.class);
                activity.startActivity(intent);
				//setOnFocusChangeListeners();
                activity.finish();
            }
        });
        comboBtnDate = new ComboButton(activity, null, R.id.linearlayout_time_date, 1, 2, false);
        comboBtnCurrentTime = new ComboButton(activity, null, R.id.linearlayout_time_currenttime,
                1, 2, false);
        comboBtnSleepTime = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sleep_mode_vals), R.id.linearlayout_time_sleep, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (true == ActivityManager.isUserAMonkey()) {
                    Log.i(TAG, "monkey is running, ignore to set sleep time!");
                } else {
                    tvTimerManager.setSleepTimeMode(comboBtnSleepTime.getIdx());
                }
            }
        };

        setOnClickListeners();
        myBtnOffTime.setFocused();
        comboBtnDate.setEnable(false);
        comboBtnCurrentTime.setEnable(false);
    }
    
    private void setOnClickListeners() {
        OnClickListener BtnOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
            	sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
            }
        };
        comboBtnSleepTime.setOnClickListener(BtnOnClickListener);
    }
    public static void sendKeyEvent(final int KeyCode) {
        new Thread() {     
             public void run() {
                 try {
                     Instrumentation inst = new Instrumentation();
                     inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                     e.printStackTrace();
                 }
              }
   
         }.start();
  }

    public void loadDataToUI() {
		mTimeZone = TimeZone.getDefault();
		createTime(mTimeZone.getDisplayName());
		date =new SimpleDateFormat("yyyy-MM-dd").format(mTime.getTime());
		clock =new SimpleDateFormat("HH:mm:ss").format(mTime.getTime());

        loadDataToMyBtnOffTime();
        loadDataToMyBtnScheduledTime();
        if (!isClockExisted) {
            isClockExisted = true;
            //Time curTime = tvTimerManager.getCurrentTvTime();
            //comboBtnDate.setTextInChild(2, curTime.format("%Y/%m/%d"));
            //comboBtnCurrentTime.setTextInChild(2, curTime.format("%H:%M:%S"));
			comboBtnDate.setTextInChild(2, date);
			comboBtnCurrentTime.setTextInChild(2, clock);

			timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(UPDATE_CURRENT_TIME);
                }
            }, 1000, 1000);
        }
		registerReceiver();
		comboBtnSleepTime.setIdx(tvTimerManager.getSleepTimeMode());
    }

    public void endUIClock() {
        timer.cancel();
    }

    public void loadDataToMyBtnOffTime() {
        Time dateTime;
        if(tvTimerManager.getSleepTimeMode() !=0){
        	comboBtnSleepTime.setValue(tvTimerManager.getSleepTimeRemainMins()+" "+activity.getResources().getText(R.string.str_sleeptime_minute));
        }else
			comboBtnSleepTime.setIdx(tvTimerManager.getSleepTimeMode());
        if (tvTimerManager.isOffTimerEnable()) {
            dateTime = tvTimerManager.getTvOffTimer();
            myBtnOffTime.setTextInChild(2, "" + dateTime.hour + ":" + dateTime.minute);
        } else {
            myBtnOffTime.setTextInChild(2,
                    activity.getResources().getStringArray(R.array.str_arr_time_switch)[0]);
        }
    }

    public void loadDataToMyBtnScheduledTime() {
        Time dateTime;
        if (tvTimerManager.isOnTimerEnable()) {
            dateTime = tvTimerManager.getTvOnTimer();
            myBtnScheduledTime.setTextInChild(2, "" + dateTime.hour + ":" + dateTime.minute);
        } else {
            myBtnScheduledTime.setTextInChild(2,
                    activity.getResources().getStringArray(R.array.str_arr_time_switch)[0]);
        }
    }

    //add by wsw for getting time 
	private void registerReceiver() {
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		activity.registerReceiver(mIntentReceiver, filter, null, handler);
		}
	
	public void unregisterReceiver() {
		activity.unregisterReceiver(mIntentReceiver);
	}
	private void createTime(String timeZone) {
		if (timeZone != null) {
			mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			} else {

				mTime = Calendar.getInstance();

			}

	}
	private void onTimeChanged() {
		mTime.setTimeInMillis(System.currentTimeMillis());
		date =new SimpleDateFormat("yyyy-MM-dd").format(mTime.getTime());
		clock =new SimpleDateFormat("HH:mm:ss").format(mTime.getTime());

	}
	//add end
}
