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

package com.mstar.tv.tvplayer.ui.channel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.MKeyEvent;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.SwitchPageHelper;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.pvr.PVRActivity;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Utility;
import com.mstar.tv.tvplayer.ui.RootActivity;

public class ChannelControlActivity extends MstarBaseActivity {

    private final static String TAG = "ChannelControlActivity";

    private int mTvSystem = 0;

    private final int WAIT_EXPIRE_TIME = 3000;

    private final int DIRECT_TUNE_MESSAGE = 1;

    private final int DISAPPEAR_KEY_100_MWSSAGE = 2;

    private final int HIGH_BYTE_16BIT = 0x0000FF00;

    private final int LOW_BYTE_16BIT = 0x000000FF;

    private final int FINISH_DELAY_FOR_IDLE = 2 * 1000;

    private final int FINISH_DELAY_FOR_PROG_FOUND = 3 * 1000;

    private final int INVALID_CHANNEL_NUM = -1;

    private int mInputDigitMajor = 0;

    private int mInputDigitMinor = 0;

    private int mChannelNumberInput = 0;

    private int mChannelNumberMinorNum = 0;

    private int mAtvChannelCount = 0;

    private int mDtvChannelCount = 0;

    private int mPreChannelNumber = INVALID_CHANNEL_NUM;

    private int mPreChannelNumberMinorNum = INVALID_CHANNEL_NUM;

    private int mCurRFScanNumber = 0;

    private boolean mInputKeySlash = false;

    private boolean mIsDotExist = false;

    private boolean mDirectTuneEnd = false;

    private TextView mTvNumberIcon_100; 
    
    private TextView mTvNumberIcon_10;
    
    private TextView mTvNumberIcon_1;
    
    private ImageView mTvNumberIcon1;

    private ImageView mTvNumberIcon2;

    private ImageView mTvNumberIcon3;

    private ImageView mTvNumberIcon4;

    private ImageView mTvNumberIcon5;

    private ImageView mTvMinorNumberIcon1;

    private ImageView mTvMinorNumberIcon2;

    private ImageView mTvMinorNumberIcon3;

    private ImageView mTvMinorNumberIcon4;

    private ImageView mTvNumberIconDot;

    private TextView mChannelScan;

    private TextView mChannelNum;

    private int mInputSource = TvCommonManager.INPUT_SOURCE_DTV;

    private TvChannelManager mTvChannelManager = null;

    private TvCommonManager mTvCommonManager = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private TvIsdbChannelManager mTvIsdbChannelManager = null;

    private OnDtvPlayerEventListener mDtvPlayerEventListener = null;

    // FIXME: remove if use new method instead of handler
    private Handler mDtvUiEventHandler = null;

    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    // FIXME: remove if use new method instead of handler
    private Handler mAtvUiEventHandler = null;

    private Context mThisContext = this;

    private ArrayList<ProgramInfo> mProgramNumbers = new ArrayList<ProgramInfo>();

    private final static int[] mNumberResIds = { R.drawable.popup_img_number_0,
            R.drawable.popup_img_number_1, R.drawable.popup_img_number_2,
            R.drawable.popup_img_number_3, R.drawable.popup_img_number_4,
            R.drawable.popup_img_number_5, R.drawable.popup_img_number_6,
            R.drawable.popup_img_number_7, R.drawable.popup_img_number_8,
            R.drawable.popup_img_number_9 };

    private Handler finishHandler = new Handler();

    private Runnable finishTask = new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
            intent.putExtra("info_key", true);
            mThisContext.startActivity(intent);
            ChannelControlActivity.this.finish();
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            //add by jerry 20160816
            case DISAPPEAR_KEY_100_MWSSAGE:
            {
				RootActivity.key_100_Count=0;
				RootActivity.key_100_Flag = true;
            	mTvNumberIcon_1.setVisibility(View.GONE);
    			mTvNumberIcon_10.setVisibility(View.GONE);
    			mTvNumberIcon_100.setVisibility(View.GONE);
    			mTvNumberIcon1.setVisibility(View.GONE);
    			mTvNumberIcon2.setVisibility(View.GONE);
    			mTvNumberIcon3.setVisibility(View.GONE);
            }
            //add end
            break;
            case DIRECT_TUNE_MESSAGE:
                RootActivity.key_100_Flag = true;
            	RootActivity.key_100_Count =0;
            	mHandler.removeMessages(DISAPPEAR_KEY_100_MWSSAGE);
            	mHandler.sendEmptyMessageDelayed(DISAPPEAR_KEY_100_MWSSAGE, 0);
                if (isProgramExist()) {
                    if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            doProgramSelForAtscSystem(mChannelNumberInput,
                                    mChannelNumberMinorNum);
                        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            if (mIsDotExist) {
                                mTvCommonManager
                                        .setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                                mInputSource = TvCommonManager.INPUT_SOURCE_DTV;
                                mTvChannelManager.selectProgram(
                                        mChannelNumberInput,
                                        TvChannelManager.SERVICE_TYPE_DTV);
                            } else {
                                mTvChannelManager.selectProgram(
                                        (Utility.getATVRealChNum(mChannelNumberInput)),
                                        TvChannelManager.SERVICE_TYPE_ATV);
                            }
                        } else {
                            mTvChannelManager.selectProgram(
                                    Utility.getATVRealChNum(mChannelNumberInput),
                                    TvChannelManager.SERVICE_TYPE_ATV);
                        }
                    } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                        if (true == Utility.isForegroundRecording() || (true == TvPvrManager.getInstance().isPlaybacking())) {
                            Utility.channelSelect(ChannelControlActivity.this,mChannelNumberInput);
                        }
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            doProgramSelForAtscSystem(mChannelNumberInput,
                                    mChannelNumberMinorNum);
                        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            if ((!mIsDotExist)
                                && (true == Utility.isSupportATV())) {
                                mTvCommonManager
                                        .setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                                mInputSource = TvCommonManager.INPUT_SOURCE_ATV;
                                mTvChannelManager.selectProgram(
                                        (Utility.getATVRealChNum(mChannelNumberInput)),
                                        TvChannelManager.SERVICE_TYPE_ATV);
                            } else {
                                mTvChannelManager.selectProgram(
                                        mChannelNumberInput,
                                        TvChannelManager.SERVICE_TYPE_DTV);
                            }
                        } else {
                            mTvChannelManager.selectProgram(
                                    mChannelNumberInput,
                                    TvChannelManager.SERVICE_TYPE_DTV);
                        }
                    }
                } else {
                    // if Channel is not exit, Try Scan this RF
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                        int RFNum = 0;
                        if (mTvIsdbChannelManager.getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR) {
                            RFNum = (mChannelNumberInput & HIGH_BYTE_16BIT) >> 8;
                        } else {
                            RFNum = mChannelNumberInput;
                        }
                        // if Channel is not exit, Try Scan this RF
                        mCurRFScanNumber = RFNum;
                        if (mIsDotExist) {
                            dtvDirectTune(RFNum);
                        } else {
                            atvDirectTune(RFNum);
                        }
                    } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        if (true == mTvAtscChannelManager.isRFNumberValid(mChannelNumberInput)) {
                            dtvDirectTune(mChannelNumberInput);
                            break;
                        }
                    }
                }
                mChannelNumberInput = 0;
                mInputDigitMajor = 0;
                mInputDigitMinor = 0;
                mInputKeySlash = false;
                mChannelNumberMinorNum = 0;

                if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_NONE) {
                    Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
                    intent.putExtra("info_key", true);
                    mThisContext.startActivity(intent);
                    finish();
                }
                break;
            }
        }
    };

    private void dtvDirectTune(int RF) {
        Log.d(TAG, "dtvDirectTune, RF: " + RF);
        mChannelNum.setText(R.string.str_channelcontrol_channels);
        mChannelScan.setVisibility(View.VISIBLE);
        mChannelNum.setVisibility(View.VISIBLE);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mTvChannelManager.setDtvManualScanByRF(RF);
            mTvChannelManager.startDtvManualScan();
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            resetAtscDtvPreChannel();
            getAtscDtvPreChannel();
            if (TvCommonManager.INPUT_SOURCE_DTV != mTvCommonManager.getCurrentTvInputSource()) {
                mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                mInputSource = TvCommonManager.INPUT_SOURCE_DTV;
            }
            mTvChannelManager.setDtvManualScanByRF(RF);
            mTvChannelManager.changeDtvManualScanRF(RF);
            mTvChannelManager.startDtvManualScan();
        }
    }

    private void atvDirectTune(int RF) {
        if (false == Utility.isSupportATV()) {
            return;
        }
        Log.d(TAG, "atvDirectTune, RF: " + RF);
        mChannelNum.setText(R.string.str_channelcontrol_channels);
        mChannelScan.setVisibility(View.VISIBLE);
        mChannelNum.setVisibility(View.VISIBLE);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mPreChannelNumber = mTvChannelManager.getCurrentChannelNumber();
            if (TvCommonManager.INPUT_SOURCE_ATV != mTvCommonManager.getCurrentTvInputSource()) {
                mTvCommonManager
                        .setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                mInputSource = TvCommonManager.INPUT_SOURCE_ATV;
            }
            // FIXME ATSC/ISDB are using the same API
            mTvChannelManager.startATSCAtvManualTuning(RF, 0);
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if (TvCommonManager.INPUT_SOURCE_ATV != mTvCommonManager.getCurrentTvInputSource()) {
                mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                mInputSource = TvCommonManager.INPUT_SOURCE_ATV;
            }

            if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
                resetAtscDtvPreChannel();
                mTvChannelManager.startATSCAtvManualTuning(RF, 0);
            } else {
                //FIXME: need to implement ATV PAL + ATSC
                Log.e(TAG, "ATV PAL + ATSC is not supported!");
                finishHandler.postDelayed(finishTask, FINISH_DELAY_FOR_IDLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channelcontrol);

        mTvChannelManager = TvChannelManager.getInstance();
        mTvCommonManager = TvCommonManager.getInstance();
        mTvSystem = mTvCommonManager.getCurrentTvSystem();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        }
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mTvIsdbChannelManager = TvIsdbChannelManager.getInstance();
        }

        mTvNumberIcon_100 = (TextView) findViewById(R.id.channel_control_100);
        mTvNumberIcon_10 = (TextView) findViewById(R.id.channel_control_10);
        mTvNumberIcon_1 = (TextView) findViewById(R.id.channel_control_1);
        mTvNumberIcon1 = (ImageView) findViewById(R.id.channel_control_num1);
        mTvNumberIcon2 = (ImageView) findViewById(R.id.channel_control_num2);
        mTvNumberIcon3 = (ImageView) findViewById(R.id.channel_control_num3);
        mTvNumberIcon4 = (ImageView) findViewById(R.id.channel_control_num4);
        mTvNumberIcon5 = (ImageView) findViewById(R.id.channel_control_num5);
        mTvNumberIconDot = (ImageView) findViewById(R.id.channel_control_dot);
        mTvMinorNumberIcon1 = (ImageView) findViewById(R.id.channel_control_minor_num1);
        mTvMinorNumberIcon2 = (ImageView) findViewById(R.id.channel_control_minor_num2);
        mTvMinorNumberIcon3 = (ImageView) findViewById(R.id.channel_control_minor_num3);
        mTvMinorNumberIcon4 = (ImageView) findViewById(R.id.channel_control_minor_num4);
        mChannelScan = (TextView) findViewById(R.id.channel_control_scan);
        mChannelNum = (TextView) findViewById(R.id.channel_control_channels);

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC
                || mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mTvNumberIcon1.setVisibility(View.GONE);
            mTvNumberIcon2.setVisibility(View.GONE);
            mTvNumberIcon3.setVisibility(View.GONE);
            mTvNumberIcon4.setVisibility(View.GONE);
            mTvNumberIcon5.setVisibility(View.GONE);
            mTvNumberIconDot.setVisibility(View.GONE);
            mTvMinorNumberIcon1.setVisibility(View.GONE);
            mTvMinorNumberIcon2.setVisibility(View.GONE);
            mTvMinorNumberIcon3.setVisibility(View.GONE);
            mTvMinorNumberIcon4.setVisibility(View.GONE);
        } else {
            mTvNumberIconDot.setVisibility(View.GONE);
            mTvNumberIcon4.setVisibility(View.GONE);
            mTvNumberIcon5.setVisibility(View.GONE);
            mTvMinorNumberIcon1.setVisibility(View.GONE);
            mTvMinorNumberIcon2.setVisibility(View.GONE);
            mTvMinorNumberIcon3.setVisibility(View.GONE);
            mTvMinorNumberIcon4.setVisibility(View.GONE);
        }

        mChannelScan.setVisibility(View.GONE);
        mChannelNum.setVisibility(View.GONE);

        mInputSource = mTvCommonManager.getCurrentTvInputSource();
        int channel_key = getIntent()
                .getIntExtra("KeyCode", KeyEvent.KEYCODE_1);
        getProgList();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            if (mTvIsdbChannelManager.getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR) {
                isdbTenKeyCode(channel_key);
            } else {
                tenKeyCode(channel_key);
            }
        } else {
            tenKeyCode(channel_key);
        }
        // FIXME: remove if use new method instead of handler
        mDtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateDtvTuningScanInfo((DtvEventScan) msg.obj);
            }
        };
        // FIXME: remove if use new method instead of handler
        mAtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateAtvTuningScanInfo((AtvEventScan) msg.obj);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDtvPlayerEventListener = new DtvEventListener();
        mAtvPlayerEventListener = new AtvPlayerEventListener();
        mTvChannelManager
                .registerOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mTvChannelManager
                .registerOnAtvPlayerEventListener(mAtvPlayerEventListener);
    }

    @Override
    protected void onPause() {
        mHandler.removeMessages(DIRECT_TUNE_MESSAGE);
        stopTvDirectTune();
        mTvChannelManager
                .unregisterOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mTvChannelManager
                .unregisterOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mDtvPlayerEventListener = null;
        mAtvPlayerEventListener = null;
        super.onPause();
        RootActivity.key_100_Flag =true;
 		RootActivity.key_100_Count=0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RootActivity.key_100_Flag =true;
 		RootActivity.key_100_Count=0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //add by wxy for init
    	if(keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		RootActivity.key_100_Flag =true;
     		RootActivity.key_100_Count=0;
    	}
    	//add end
    	
        if (SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
            finish();
            return true;
        } else if (SwitchPageHelper.goToEpgPage(this, keyCode) == true) {
            finish();
            return true;
        } else if (SwitchPageHelper.goToPvrPage(this, keyCode) == true) {
            finish();
            return true;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_9:
            case KeyEvent.KEYCODE_TV_100:
            	if(keyCode == KeyEvent.KEYCODE_TV_100)
            		RootActivity.key_100_Flag = false;

                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (true == isBolckingInput()) {
                        break;
                    }
                    if (mInputKeySlash == false) {
                        if (mInputDigitMajor == 5) {
                            mInputDigitMajor = 0;
                            mChannelNumberInput = 0;
                            mInputDigitMinor = 0;
                            mChannelNumberMinorNum = 0;
                            mInputKeySlash = false;
                        }
                    } else {
                        if (mInputDigitMinor == 4) {
                            mInputDigitMajor = 0;
                            mChannelNumberInput = 0;
                            mInputDigitMinor = 0;
                            mChannelNumberMinorNum = 0;
                            mInputKeySlash = false;
                        }
                    }
                    atscTenKeyCode(keyCode);
                } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                    if (true == isBolckingInput()) {
                        break;
                    }
                    if (mTvIsdbChannelManager.getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR) {
                        isdbTenKeyCode(keyCode);
                    } else {
                        tenKeyCode(keyCode);
                    }
                } else {
                    tenKeyCode(keyCode);
                }
                return true;
            case MKeyEvent.KEYCODE_MSTAR_BALANCE:
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                    if (true == isBolckingInput()) {
                        break;
                    }
                    if (mTvIsdbChannelManager.getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR) {
                        isdbTenKeyCode(keyCode);
                        return true;
                    }
                }
                else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (true == isBolckingInput()) {
                        break;
                    }
                    if ((mInputDigitMajor == 0) || (mChannelNumberInput == 0)) {
                        return true;
                    }
                    mInputKeySlash = true;
                    atscTenKeyCode(keyCode);
                    return true;
                }
                break;
            //jerry add 20160816
    		case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            	if(mChannelNumberInput>0)
            	{
                    mHandler.removeMessages(DISAPPEAR_KEY_100_MWSSAGE);
    	        	mHandler.removeMessages(DIRECT_TUNE_MESSAGE);
    	        	mHandler.sendEmptyMessageDelayed(DISAPPEAR_KEY_100_MWSSAGE, 0);
    	            mHandler.sendEmptyMessageDelayed(DIRECT_TUNE_MESSAGE, 0);
            	}
            	RootActivity.key_100_Flag = true;
                return true;
    		//end
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean tenKeyCode(int keyCode) {
        if(keyCode == KeyEvent.KEYCODE_TV_100)
    	{
    		mChannelNumberInput = 0;
    		RootActivity.key_100_Count++;
    		mInputDigitMajor = 0;
    		mHandler.removeMessages(DIRECT_TUNE_MESSAGE);
    		mTvNumberIcon1.setVisibility(View.GONE);
    		mTvNumberIcon2.setVisibility(View.GONE);
    		mTvNumberIcon3.setVisibility(View.GONE);
    		switch(RootActivity.key_100_Count)
    		{
    		case 1:
    			mTvNumberIcon_100.setVisibility(View.VISIBLE);
    			mTvNumberIcon_10.setVisibility(View.GONE);
    			mTvNumberIcon_1.setVisibility(View.GONE);
    			break;
    		case 2:
    			mTvNumberIcon_100.setVisibility(View.VISIBLE);
    			mTvNumberIcon_10.setVisibility(View.VISIBLE);
    			mTvNumberIcon_1.setVisibility(View.GONE);
    			break;
    		case 3:
    			mTvNumberIcon_100.setVisibility(View.VISIBLE);
    			mTvNumberIcon_10.setVisibility(View.VISIBLE);
    			mTvNumberIcon_1.setVisibility(View.VISIBLE);
    			break;
    		default:
    			mTvNumberIcon_100.setVisibility(View.VISIBLE);
    			mTvNumberIcon_10.setVisibility(View.GONE);
    			mTvNumberIcon_1.setVisibility(View.GONE);
    			RootActivity.key_100_Count=1;
    			break;
    		}
    		mHandler.removeMessages(DISAPPEAR_KEY_100_MWSSAGE);
    		mHandler.sendEmptyMessageDelayed(DISAPPEAR_KEY_100_MWSSAGE, WAIT_EXPIRE_TIME);
    		return true;
    	}

        mInputDigitMajor++;

        if(RootActivity.key_100_Flag)
    	{
    		if (mInputDigitMajor > 3) {
                mTvNumberIcon1.setVisibility(View.INVISIBLE);
                mTvNumberIcon2.setVisibility(View.INVISIBLE);
                mTvNumberIcon3.setVisibility(View.INVISIBLE);
                mInputDigitMajor = 1;
            }

            int inputnum = keyCode - KeyEvent.KEYCODE_0;
            ArrayList<Integer> n = getResoulseID(numberToPicture(inputnum));

            if (mInputDigitMajor == 1) {
                mChannelNumberInput = inputnum;
                mTvNumberIcon1.setImageResource(n.get(0));
                mTvNumberIcon1.setVisibility(View.VISIBLE);
            } else if (mInputDigitMajor == 2) {
                mChannelNumberInput = mChannelNumberInput * 10 + inputnum;
                mTvNumberIcon2.setImageResource(n.get(0));
                mTvNumberIcon2.setVisibility(View.VISIBLE);
            } else if (mInputDigitMajor == 3) {
                mChannelNumberInput = mChannelNumberInput * 10 + inputnum;
                mTvNumberIcon3.setImageResource(n.get(0));
                mTvNumberIcon3.setVisibility(View.VISIBLE);
            } else {
                return true;
            }
    	}
    	else{ //add by wxy
            if(mInputDigitMajor>RootActivity.key_100_Count)
            {
                mInputDigitMajor = RootActivity.key_100_Count;
            }
            int inputnum = keyCode - KeyEvent.KEYCODE_0;
            ArrayList<Integer> n = getResoulseID(numberToPicture(inputnum));

            if (mInputDigitMajor == 1) {
                mChannelNumberInput = inputnum;
                if(RootActivity.key_100_Count==1)
                {
                mTvNumberIcon1.setImageResource(mNumberResIds[inputnum]);
                mTvNumberIcon_100.setVisibility(View.GONE);
                mTvNumberIcon1.setVisibility(View.VISIBLE);
                }
                else if(RootActivity.key_100_Count==2)
                {
                mTvNumberIcon_100.setVisibility(View.GONE);
                mTvNumberIcon1.setImageResource(mNumberResIds[inputnum]);
                mTvNumberIcon1.setVisibility(View.VISIBLE);
                mTvNumberIcon_10.setVisibility(View.VISIBLE);
                }
                else if(RootActivity.key_100_Count==3)
                {
                mTvNumberIcon_100.setVisibility(View.GONE);
                mTvNumberIcon1.setImageResource(mNumberResIds[inputnum]);
                mTvNumberIcon1.setVisibility(View.VISIBLE);
                mTvNumberIcon_10.setVisibility(View.VISIBLE);
                mTvNumberIcon_1.setVisibility(View.VISIBLE);
                }
            } else if (mInputDigitMajor == 2) {
                mChannelNumberInput = mChannelNumberInput * 10 + inputnum;
                if(mChannelNumberInput >99)
                mChannelNumberInput = mChannelNumberInput%100;
                if(RootActivity.key_100_Count==2)
                {
                mTvNumberIcon_100.setVisibility(View.GONE);
                mTvNumberIcon_10.setVisibility(View.GONE);
                mTvNumberIcon2.setImageResource(mNumberResIds[inputnum]);
                mTvNumberIcon2.setVisibility(View.VISIBLE);
                mTvNumberIcon1.setImageResource(mNumberResIds[(mChannelNumberInput/10)]);
                mTvNumberIcon1.setVisibility(View.VISIBLE);
                }
                else if(RootActivity.key_100_Count==3)
                {
                	mTvNumberIcon_10.setVisibility(View.GONE);
                	mTvNumberIcon_100.setVisibility(View.GONE);
                	mTvNumberIcon2.setImageResource(mNumberResIds[inputnum]);
                	mTvNumberIcon1.setImageResource(mNumberResIds[(mChannelNumberInput/10)]);
                    mTvNumberIcon1.setVisibility(View.VISIBLE);
                    mTvNumberIcon2.setVisibility(View.VISIBLE);
                    mTvNumberIcon_1.setVisibility(View.VISIBLE);
                }
            } else if (mInputDigitMajor == 3) {
                mChannelNumberInput = mChannelNumberInput * 10 + inputnum;
                if(mChannelNumberInput >999)
                {
                mChannelNumberInput = mChannelNumberInput%1000;
                }
                mTvNumberIcon_100.setVisibility(View.GONE);
                mTvNumberIcon_10.setVisibility(View.GONE);
                mTvNumberIcon_1.setVisibility(View.GONE);
                mTvNumberIcon3.setImageResource(n.get(0));
                mTvNumberIcon2.setImageResource(mNumberResIds[((mChannelNumberInput%100)/10)]);
                mTvNumberIcon1.setImageResource(mNumberResIds[(mChannelNumberInput/100)]);
                mTvNumberIcon3.setVisibility(View.VISIBLE);
                mTvNumberIcon2.setVisibility(View.VISIBLE);
                mTvNumberIcon1.setVisibility(View.VISIBLE);
            } else {
                return true;
            }
    	}

        Log.i(TAG, "mChannelNumberInput = " + mChannelNumberInput);
        mHandler.removeMessages(DIRECT_TUNE_MESSAGE);
        mHandler.sendEmptyMessageDelayed(DIRECT_TUNE_MESSAGE, WAIT_EXPIRE_TIME);

        return true;
    }

    private boolean atscTenKeyCode(int keyCode) {
        int inputnum = 0;
        ArrayList<Integer> n = null;

        if (keyCode == MKeyEvent.KEYCODE_MSTAR_BALANCE) {
            mTvNumberIconDot.setVisibility(View.VISIBLE);
            mHandler.removeMessages(DIRECT_TUNE_MESSAGE);
            mHandler.sendEmptyMessageDelayed(DIRECT_TUNE_MESSAGE,
                    WAIT_EXPIRE_TIME);
            return true;
        }
        if (mInputKeySlash == true) {
            if (!((mInputDigitMinor == 1) && (mChannelNumberMinorNum == 0))) {
                mInputDigitMinor++;
            }
        } else {
            mTvNumberIconDot.setVisibility(View.GONE);
            if (!((mInputDigitMajor == 1) && (mChannelNumberInput == 0))) {
                mInputDigitMajor++;
            }
        }

        mTvNumberIcon1.setVisibility(View.GONE);
        mTvNumberIcon2.setVisibility(View.GONE);
        mTvNumberIcon3.setVisibility(View.GONE);
        mTvNumberIcon4.setVisibility(View.GONE);
        mTvNumberIcon5.setVisibility(View.GONE);
        mTvMinorNumberIcon1.setVisibility(View.GONE);
        mTvMinorNumberIcon2.setVisibility(View.GONE);
        mTvMinorNumberIcon3.setVisibility(View.GONE);
        mTvMinorNumberIcon4.setVisibility(View.GONE);

        if (mInputKeySlash == false) {
            inputnum = keyCode - KeyEvent.KEYCODE_0;
            mChannelNumberInput = mChannelNumberInput * 10 + inputnum;
        } else {
            inputnum = keyCode - KeyEvent.KEYCODE_0;
            mChannelNumberMinorNum = mChannelNumberMinorNum * 10 + inputnum;
        }

        n = getResoulseID(numberToPicture(mChannelNumberInput));
        if (n.size() == 1) {
            mTvNumberIcon1.setImageResource(n.get(0));
            mTvNumberIcon1.setVisibility(View.VISIBLE);
        } else if (n.size() == 2) {
            mTvNumberIcon1.setImageResource(n.get(0));
            mTvNumberIcon1.setVisibility(View.VISIBLE);
            mTvNumberIcon2.setImageResource(n.get(1));
            mTvNumberIcon2.setVisibility(View.VISIBLE);
        } else if (n.size() == 3) {
            mTvNumberIcon1.setImageResource(n.get(0));
            mTvNumberIcon1.setVisibility(View.VISIBLE);
            mTvNumberIcon2.setImageResource(n.get(1));
            mTvNumberIcon2.setVisibility(View.VISIBLE);
            mTvNumberIcon3.setImageResource(n.get(2));
            mTvNumberIcon3.setVisibility(View.VISIBLE);
        } else if (n.size() == 4) {
            mTvNumberIcon1.setImageResource(n.get(0));
            mTvNumberIcon1.setVisibility(View.VISIBLE);
            mTvNumberIcon2.setImageResource(n.get(1));
            mTvNumberIcon2.setVisibility(View.VISIBLE);
            mTvNumberIcon3.setImageResource(n.get(2));
            mTvNumberIcon3.setVisibility(View.VISIBLE);
            mTvNumberIcon4.setImageResource(n.get(3));
            mTvNumberIcon4.setVisibility(View.VISIBLE);
        } else if (n.size() == 5) {
            mTvNumberIcon1.setImageResource(n.get(0));
            mTvNumberIcon1.setVisibility(View.VISIBLE);
            mTvNumberIcon2.setImageResource(n.get(1));
            mTvNumberIcon2.setVisibility(View.VISIBLE);
            mTvNumberIcon3.setImageResource(n.get(2));
            mTvNumberIcon3.setVisibility(View.VISIBLE);
            mTvNumberIcon4.setImageResource(n.get(3));
            mTvNumberIcon4.setVisibility(View.VISIBLE);
            mTvNumberIcon5.setImageResource(n.get(4));
            mTvNumberIcon5.setVisibility(View.VISIBLE);
        }
        if (mInputKeySlash == true) {
            n = getResoulseID(numberToPicture(mChannelNumberMinorNum));
            if (n.size() == 1) {
                mTvMinorNumberIcon1.setImageResource(n.get(0));
                mTvMinorNumberIcon1.setVisibility(View.VISIBLE);
            } else if (n.size() == 2) {
                mTvMinorNumberIcon1.setImageResource(n.get(0));
                mTvMinorNumberIcon1.setVisibility(View.VISIBLE);
                mTvMinorNumberIcon2.setImageResource(n.get(1));
                mTvMinorNumberIcon2.setVisibility(View.VISIBLE);
            } else if (n.size() == 3) {
                mTvMinorNumberIcon1.setImageResource(n.get(0));
                mTvMinorNumberIcon1.setVisibility(View.VISIBLE);
                mTvMinorNumberIcon2.setImageResource(n.get(1));
                mTvMinorNumberIcon2.setVisibility(View.VISIBLE);
                mTvMinorNumberIcon3.setImageResource(n.get(2));
                mTvMinorNumberIcon3.setVisibility(View.VISIBLE);
            } else if (n.size() == 4) {
                mTvMinorNumberIcon1.setImageResource(n.get(0));
                mTvMinorNumberIcon1.setVisibility(View.VISIBLE);
                mTvMinorNumberIcon2.setImageResource(n.get(1));
                mTvMinorNumberIcon2.setVisibility(View.VISIBLE);
                mTvMinorNumberIcon3.setImageResource(n.get(2));
                mTvMinorNumberIcon3.setVisibility(View.VISIBLE);
                mTvMinorNumberIcon4.setImageResource(n.get(3));
                mTvMinorNumberIcon4.setVisibility(View.VISIBLE);
            }
        }

        mHandler.removeMessages(DIRECT_TUNE_MESSAGE);
        mHandler.sendEmptyMessageDelayed(DIRECT_TUNE_MESSAGE, WAIT_EXPIRE_TIME);

        return true;
    }

    private boolean isdbTenKeyCode(int keyCode) {
        if (keyCode == MKeyEvent.KEYCODE_MSTAR_BALANCE) {
            if (mIsDotExist == true) {
                return true;
            } else {
                mIsDotExist = true;
            }
            mTvNumberIconDot.setVisibility(View.VISIBLE);
        } else {
            int inputNumber = keyCode - KeyEvent.KEYCODE_0;
            ArrayList<Integer> n = null;

            /*
             * isdb ch num includes major num and minor num,
             * ex. original ch no = 1281, transforms to hex = 0x501, 5 is major
             * num and 1 is minor num;
             * displayed ch no is 5.1
             */
            if (mIsDotExist == false) {
                mInputDigitMajor++;
            } else {
                mInputDigitMinor++;
            }
            /*
             * Layout looks like this: Icon1 Icon2 dot Icon3 Icon4 Icon5
             * If current input is first major num or first minor num,
             * then update Icon2 for first major num or Icon3 for first minor
             * num;
             * if current input is second major num or second minor num,
             * then update both Icon1 and Icon2 for major num or Icon4 for
             * second minor num
             */
            if ((mInputDigitMajor == 1 && mIsDotExist == false)
                    || (mInputDigitMinor == 1 && mIsDotExist == true)) {
                n = getResoulseID(numberToPicture(inputNumber));
                if (mIsDotExist == false) {
                    if (inputNumber == 0) {
                        mInputDigitMajor = 0;
                    }
                    mChannelNumberInput = inputNumber << 8;
                    mTvNumberIcon2.setImageResource(n.get(0));
                    mTvNumberIcon2.setVisibility(View.VISIBLE);
                } else {
                    mChannelNumberInput = mChannelNumberInput & HIGH_BYTE_16BIT;
                    mChannelNumberInput += inputNumber;
                    mTvMinorNumberIcon1.setImageResource(n.get(0));
                    mTvMinorNumberIcon1.setVisibility(View.VISIBLE);
                }
            } else if ((mInputDigitMajor == 2 && mIsDotExist == false)
                    || (mInputDigitMinor == 2 && mIsDotExist == true)) {
                if (mIsDotExist == false) {
                    mChannelNumberInput = mChannelNumberInput * 10
                            + (inputNumber << 8);
                    n = getResoulseID(numberToPicture(mChannelNumberInput >> 8));
                    mTvNumberIcon1.setImageResource(n.get(0));
                    mTvNumberIcon1.setVisibility(View.VISIBLE);
                    mTvNumberIcon2.setImageResource(n.get(1));
                    mTvNumberIcon2.setVisibility(View.VISIBLE);
                } else {
                    mChannelNumberInput += (mChannelNumberInput & LOW_BYTE_16BIT)
                            * 9 + inputNumber;
                    n = getResoulseID(numberToPicture(inputNumber));
                    mTvMinorNumberIcon2.setImageResource(n.get(0));
                    mTvMinorNumberIcon2.setVisibility(View.VISIBLE);
                }
            }
        }
        mHandler.removeMessages(DIRECT_TUNE_MESSAGE);
        mHandler.sendEmptyMessageDelayed(DIRECT_TUNE_MESSAGE, WAIT_EXPIRE_TIME);
        return true;
    }

    private ArrayList<String> numberToPicture(int num) {
        ArrayList<String> strArray = new ArrayList<String>();
        String str = num + "";
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            strArray.add("" + ch);
        }
        return strArray;
    }

    private ArrayList<Integer> getResoulseID(ArrayList<String> str) {
        ArrayList<Integer> n = new ArrayList<Integer>();
        for (String string : str) {
            Integer resId = mNumberResIds[Integer.parseInt(string)];
            n.add(resId);
        }
        return n;
    }

    private void getProgList() {
        ProgramInfo programInfo = null;
        mAtvChannelCount = 0;
        mDtvChannelCount = 0;

        int m_nServiceNum = mTvChannelManager
                .getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        for (int k = 0; k < m_nServiceNum; k++) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                programInfo = mTvAtscChannelManager.getProgramInfo(k);
            } else {
                programInfo = mTvChannelManager.getProgramInfoByIndex(k);
            }
            if (programInfo != null) {
                if (programInfo.isDelete == true) {
                    continue;
                } else {
                    if (programInfo.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                        mAtvChannelCount++;
                    } else {
                        mDtvChannelCount++;
                    }
                    mProgramNumbers.add(programInfo);
                }
            }
        }
    }

    private boolean isProgramExist() {
        boolean programIsExist = false;

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            for (ProgramInfo pi : mProgramNumbers) {
                if ((mChannelNumberInput == pi.majorNum)
                        && (mChannelNumberMinorNum == pi.minorNum)) {
                    programIsExist = true;
                    break;
                }
                /*
                 * For ONE-PART channel, the minor number is defined as 0xFFFF.
                 * In a case: MajorNum = 15, MinorNum = 65535(0xFFFF), channel
                 * number will be displayed as "15" in channellist menu. User
                 * inputs "15" to tune to the channel.
                 */
                if ((mChannelNumberInput == pi.majorNum)
                        && (TvAtscChannelManager.ONEPARTCHANNEL_MINOR_NUM == pi.minorNum)) {
                    programIsExist = true;
                    break;
                }

                /*
                 * Special case, the behavior is same as pure SN's UI.
                 * User case:
                 *  1. Channel list contents the ATV channel: CH5
                 *  2. Current input source is DTV and the channel is: 5-8 (DTV channel)
                 *  3. User input: 5-20, "5-20" is not existed in channel list
                 *  4. Check the major number 5 as the ATV channel, CH5 is existed
                 *  5. isProgramExist() returns "TRUE", Direct Tuning will not be performed
                 *  6. Do program selection to "5-20"
                 *  7. SN tune to channel "5-8" (the channel is kept in DTV 5-8 not the ATV CH5)
                 */
                if ((mChannelNumberInput == pi.majorNum) && (0 == pi.minorNum)) {
                    programIsExist = true;
                    break;
                }
            }
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            boolean isMajorSame = false;
            int channelMajorSame = 0;

            for (ProgramInfo pi : mProgramNumbers) {
                int channelNumber = pi.number;
                int channelMajor = (channelNumber & HIGH_BYTE_16BIT) >> 8;
                int channelInputMajor = (mChannelNumberInput & HIGH_BYTE_16BIT) >> 8;

                if (!mIsDotExist) {
                    // check if ATV channell avaliable
                    if (channelNumber == (Utility.getATVRealChNum(mChannelNumberInput))) {
                        programIsExist = true;
                        break;
                    } else if (channelNumber == (getATVChannelNumFromDTV(mChannelNumberInput)) - 1) {
                        programIsExist = true;
                        mChannelNumberInput = (getATVChannelNumFromDTV(mChannelNumberInput));
                        break;
                    }
                } else {
                    // check if DTV channell avaliable or the most close to the Major number one
                    if (channelNumber == mChannelNumberInput) {
                        programIsExist = true;
                        break;
                    } else if (channelMajor == channelInputMajor) {
                        if (isMajorSame == false) {
                            channelMajorSame = channelNumber;
                            isMajorSame = true;
                        }
                    }
                }
            }
            if (programIsExist == false) {
                /*
                 * For ISDB only!!
                 * If the channel number(major and minor) is not existed, find the first matched major to do channel switching.
                 * For example, the channel list is:
                 *    channel 1: 11-1  (11: Major number , 1:Minor number)
                 *    channel 2: 11-2  (11: Major number , 2:Minor number)
                 *
                 * User input:
                 *    case 1: input 11-  -> switch to 11-1 (find first 11-x)
                 *    case 2: input 11-5 -> switch to 11-1 (find first 11-x)
                 *    case 3: input 11-2 -> switch to 11-2 (11-2 is existed)
                 *    case 4: input 11-1 -> switch to 11-1 (11-1 is existed)
                 */
                if (isMajorSame == true) {
                    mChannelNumberInput = channelMajorSame;
                    programIsExist = true;
                }
            }
        } else {
            /*
             * For internal TV model(China mainland), ATV channel number start
             * from 0.
             * And for external TV model, ATV channel number start from 1.
             * Here, we do program exist check by checking input index number
             * with program count size inside mProgramNumbers array list.
             */
            int curInputSrc = mTvCommonManager.getCurrentTvInputSource();
            for (ProgramInfo pi : mProgramNumbers) {
                if (TvChannelManager.SERVICE_TYPE_ATV == pi.serviceType) {
                    if (TvCommonManager.INPUT_SOURCE_ATV == curInputSrc) {
                        if (mChannelNumberInput == Utility.getATVDisplayChNum(pi.number)) {
                            programIsExist = true;
                            break;
                        }
                    }
                } else {
                    if ((TvCommonManager.INPUT_SOURCE_DTV == curInputSrc)) {
                        if (mChannelNumberInput == pi.number) {
                            programIsExist = true;
                            break;
                        }
                    }
                }
            }
        }
        return programIsExist;
    }

    private boolean isOnePartChannelExist(int majorNumber, int minorNumber) {
        boolean isExist = false;

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            for (ProgramInfo pi : mProgramNumbers) {
                if ((majorNumber == pi.majorNum)
                        && (minorNumber == pi.minorNum)) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }

    private void doProgramSelForAtscSystem(int majorNumber, int minorNumber) {
        if ((mInputDigitMinor == 0) && (mChannelNumberMinorNum == 0)) {
            /*
             * For ONE-PART channel, the minor number is defined as 0xFFFF.
             * In a case: MajorNum = 15, MinorNum = 65535(0xFFFF),
             * channel number will be displayed as "15" in channellist menu.
             * If user inputs "15" to tune to the channel,
             * it needs to convert minor channel number to "0xFFFF" before doing channel switching.
             */
            if (isOnePartChannelExist(majorNumber,
                    TvAtscChannelManager.ONEPARTCHANNEL_MINOR_NUM)) {
                minorNumber = TvAtscChannelManager.ONEPARTCHANNEL_MINOR_NUM;
            }
        }
        mTvAtscChannelManager.programSel(majorNumber, minorNumber);
    }

    private int getATVChannelNumFromDTV(int mchnum) {
        int ret = 0;
        if (mchnum < (LOW_BYTE_16BIT + 1)) {
            ret = 0;
        } else if (mchnum < (LOW_BYTE_16BIT + 1) * 10) {
            ret = mchnum >> 8;
        } else {
            ret = (mchnum / ((LOW_BYTE_16BIT + 1) * 10))
                    + (mchnum - (mchnum / ((LOW_BYTE_16BIT + 1) * 10))) >> 8;
        }

        return ret;
    }

    private void updateAtvTuningScanInfo(AtvEventScan extra) {
        Log.i(TAG, "[percent]:" + extra.percent);
        Log.i(TAG, "[frequencyKHz]:" + extra.frequencyKHz);
        Log.i(TAG, "[scannedChannelNum]:" + extra.scannedChannelNum);
        Log.i(TAG, "[curScannedChannel]:" + extra.curScannedChannel);

        if ((mTvSystem != TvCommonManager.TV_SYSTEM_ISDB)
            && (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC)) {
            return;
        }

        mTvChannelManager.stopAtvManualTuning();
        if (extra.scannedChannelNum > 0) {
            mChannelNum.setText(R.string.str_channelcontrol_done);
            mDirectTuneEnd = true;
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                mTvIsdbChannelManager.genMixProgList(false);
                mTvIsdbChannelManager.setChannel(mCurRFScanNumber - 1, true);
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                doProgramSelForAtscSystem(mChannelNumberInput, 0);
            }
        } else {
            mChannelNum.setText(R.string.str_channelcontrol_notfound);
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                if (mPreChannelNumber > mTvChannelManager.getProgramCtrl(
                        TvChannelManager.ATV_PROG_CTRL_GET_MAX_CHANNEL, 0, 0)) {
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                            TvChannelManager.FIRST_SERVICE_MENU_SCAN);
                } else {
                    mTvChannelManager.selectProgram(mPreChannelNumber,
                            TvChannelManager.SERVICE_TYPE_ATV);
                }
            }
        }
        finishHandler.postDelayed(finishTask, FINISH_DELAY_FOR_IDLE);
    }

    private void updateDtvTuningScanInfo(DtvEventScan extra) {
        if (extra.scanStatus == TvChannelManager.DTV_SCAN_STATUS_END) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                finishHandler.removeCallbacks(finishTask);

                int delay = FINISH_DELAY_FOR_IDLE;
                mChannelScan.setText(R.string.str_channelcontrol_done);
                mDirectTuneEnd = true;
                if ((extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount) > 0) {
                    mChannelNum.setText(String.valueOf(" "
                            + (extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount)));
                    delay = FINISH_DELAY_FOR_PROG_FOUND;
                } else {
                    mChannelNum.setText(R.string.str_channelcontrol_notfound);
                }
                finishHandler.postDelayed(finishTask, delay);
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                mTvChannelManager.stopDtvScan();
                finishHandler.removeCallbacks(finishTask);
                if ((extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount) > 0) {
                    resetAtscDtvPreChannel();
                    mTvAtscChannelManager.changeDtvToManualFirstService(mChannelNumberInput);
                    mChannelScan.setText(R.string.str_channelcontrol_done);
                    mDirectTuneEnd = true;
                    mChannelNum.setText(String.valueOf(" "
                            + (extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount)));
                    finishHandler.postDelayed(finishTask, FINISH_DELAY_FOR_PROG_FOUND);
                } else {
                    if (false == Utility.isSupportATV()) {
                        mChannelNum.setText(R.string.str_channelcontrol_notfound);
                        finishHandler.postDelayed(finishTask, FINISH_DELAY_FOR_IDLE);
                    } else {
                        resetAtscDtvPreChannel();
                        atvDirectTune(mChannelNumberInput);
                    }
                }
            }
        }
    }

    private class AtvPlayerEventListener implements OnAtvPlayerEventListener {
        // FIXME: remove if use new method instead of handler
        @Override
        public boolean onAtvAutoTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvUiEventHandler.obtainMessage(what, extra);
            mAtvUiEventHandler.sendMessage(msg);
            return true;
        }

        // FIXME: remove if use new method instead of handler
        @Override
        public boolean onAtvManualTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvUiEventHandler.obtainMessage(what, extra);
            mAtvUiEventHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onSignalLock(int what) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            return false;
        }

        @Override
        public boolean onAtvProgramInfoReady(int what) {
            return false;
        }
    }

    private class DtvEventListener implements OnDtvPlayerEventListener {

        @Override
        public boolean onDtvChannelNameReady(int what) {
            return false;
        }

        // FIXME: remove if use new method instead of handler
        @Override
        public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
            Message msg = mDtvUiEventHandler.obtainMessage(what, extra);
            mDtvUiEventHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onDtvProgramInfoReady(int what) {
            return false;
        }

        @Override
        public boolean onCiLoadCredentialFail(int what) {
            return false;
        }

        @Override
        public boolean onEpgTimerSimulcast(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onHbbtvStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onMheg5StatusMode(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onMheg5ReturnKey(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadHandler(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onOadDownload(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onDtvAutoUpdateScan(int what) {
            return false;
        }

        @Override
        public boolean onTsChange(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogLossSignal(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogNewMultiplex(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogFrequencyChange(int what) {
            return false;
        }

        @Override
        public boolean onRctPresence(int what) {
            return false;
        }

        @Override
        public boolean onChangeTtxStatus(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onDtvPriComponentMissing(int what) {
            return false;
        }

        @Override
        public boolean onAudioModeChange(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onMheg5EventHandler(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadTimeout(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onGingaStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onSignalLock(int what) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            return false;
        }

        @Override
        public boolean onUiOPRefreshQuery(int what) {
            return false;
        }

        @Override
        public boolean onUiOPServiceList(int what) {
            return false;
        }

        @Override
        public boolean onUiOPExitServiceList(int what) {
            return false;
        }
    }

    private void stopTvDirectTune() {
        finishHandler.removeCallbacks(finishTask);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_DTV_MANUAL_TUNING) {
                mTvChannelManager.stopDtvScan();
            }
            if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_AUTO_TUNING) {
                mTvChannelManager.stopAtvManualTuning();
            }
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_DTV_MANUAL_TUNING) {
                mTvChannelManager.stopDtvScan();
                if ((INVALID_CHANNEL_NUM != mPreChannelNumber) && (INVALID_CHANNEL_NUM != mPreChannelNumberMinorNum)) {
                    doProgramSelForAtscSystem(mPreChannelNumber, mPreChannelNumberMinorNum);
                } else {
                    mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
            }
            if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_AUTO_TUNING) {
                mTvChannelManager.stopAtvManualTuning();
                mTvChannelManager.changeToFirstService(
                    TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                    TvChannelManager.FIRST_SERVICE_DEFAULT);
            }
        }
    }

    private boolean isBolckingInput() {
        int tuningStatus = mTvChannelManager.getTuningStatus();
        if (TvChannelManager.TUNING_STATUS_NONE != tuningStatus) {
            Log.d(TAG, "key input ignored! tuning status = " + tuningStatus);
            return true;
        } else if (true == mDirectTuneEnd) {
            return true;
        }
        return false;
    }

    private void getAtscDtvPreChannel() {
        if (0 < mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV)) {
            ProgramInfo progInfo = mTvAtscChannelManager.getCurrentProgramInfo();
            mPreChannelNumber = progInfo.majorNum;
            mPreChannelNumberMinorNum = progInfo.minorNum;
        }
    }

    private void resetAtscDtvPreChannel() {
        mPreChannelNumber = INVALID_CHANNEL_NUM;
        mPreChannelNumberMinorNum = INVALID_CHANNEL_NUM;
    }
}
