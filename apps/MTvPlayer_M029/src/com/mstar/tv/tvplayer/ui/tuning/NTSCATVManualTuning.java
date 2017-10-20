//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.tvplayer.ui.tuning;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.widget.Toast;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.view.View.OnFocusChangeListener;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.holder.ViewHolder;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Utility;
import android.widget.Toast;

public class NTSCATVManualTuning extends MstarBaseActivity {

    private static final String TAG = "NTSCATVManualTuning";

    private static final int USER_CHANGE_CHANNEL_TIMEOUT = 2000;

    private boolean mDirectChangeCh = false;

    private int mTvSystem = 0;

    private int mMinCh = 0;

    private int mMaxCh = 0;

    /** The variable mCurChannelNumber acquired from Supernova library
     * stands for the internal channel number of the current channel
     * If one'd like to get the surface number for show in OSD/MENU/INFO/...,etc,
     * it can be acquired by calling surfaceNumber = Utility.getATVDisplayChNum(mCurChannelNumber);
     */
    private int mCurChannelNumber = 0;

    /** The variable mNewChannelNumber stands for the internal channel number prepared for next tuning,
     * This variabl keep the mCurChannelNumber stays unchanged when channel tuning get failed.
     * Set mCurChannelNumber = mNewChannelNumber after the tuning is successful to update the UI.
     */
    private int mNewChannelNumber = 0;

    /** The variable mBufferInputNumber only used when user input number directly
     * this is a surface number and only for display purpose
     * Get the internal channel number by calling internalNumber = Utility.getATVRealChNum(mBufferInputNumber)
     */
    private int mBufferInputNumber = 0;

    private ViewHolder mViewholder;

    private TvChannelManager mTvChannelManager = null;
    
    private boolean tcflag = true;

    private Handler mDirectChangeChTimeout = new Handler();

    private Runnable mDirectChangeChTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
        	
            mDirectChangeCh = false;
            if (doManualTune(mNewChannelNumber)) {
                mCurChannelNumber = mNewChannelNumber;
            }
          //end by pan
            if(true == tcflag &&  1 == mBufferInputNumber){
            	mCurChannelNumber = 2;
            	Toast.makeText(getApplicationContext(),"无线下，最小频道号为2",
            		     Toast.LENGTH_SHORT).show();
            }
            updateAtvManualtuningComponents();
        }
    };

    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    private class AtvPlayerEventListener implements OnAtvPlayerEventListener {
        @Override
        public boolean onAtvAutoTuningScanInfo(int what, AtvEventScan extra) {
            return false;
        }

        @Override
        public boolean onAtvManualTuningScanInfo(int what, AtvEventScan extra) {
            runOnUiThread(new Runnable() {
                public void run() {
                    updateAtvManualtuningfreq();
                }
            });
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
            runOnUiThread(new Runnable() {
                public void run() {
                    updateAtvManualtuningfreq();
                }
            });
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        setContentView(R.layout.ntscatvmanualtuning);
        mViewholder = new ViewHolder(NTSCATVManualTuning.this);
        mViewholder.findViewForNTSCATVManualTuning();

        TVRootApp app = (TVRootApp) getApplication();
        mTvChannelManager = TvChannelManager.getInstance();
        if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
        }

        mCurChannelNumber = mTvChannelManager.getCurrentChannelNumber();
        mMinCh = mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MIN_CHANNEL, 0, 0);
        mMaxCh = mTvChannelManager.getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MAX_CHANNEL, 0, 0);
        if(1 == mMinCh){
        	tcflag = false;
        }else if(2 == mMinCh){
        	tcflag = true;
        }
        Log.d(TAG, "mMinCh: " + mMinCh);
        Log.d(TAG, "mMaxCh: " + mMaxCh);
        Log.d(TAG, "mCurChannelNumber: " + mCurChannelNumber);

        if (mCurChannelNumber == 0) {
            mCurChannelNumber = mMinCh;
        }
        mNewChannelNumber = mCurChannelNumber;
        doManualTune(mNewChannelNumber);
        updateAtvManualtuningComponents();

        TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {

            @Override
            public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                Log.d(TAG, "onKeyInfo");
                return false;
            }

            @Override
            public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                Log.d(TAG, "onAutoSwitch");
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                finish();

                return false;
            }
        });

        mViewholder.linear_cha_ntscatvmanualtuning_channelnum
            .setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mDirectChangeCh = false;
                    mDirectChangeChTimeout.removeCallbacks(mDirectChangeChTimeoutRunnable);
                }
            }
        });
    }

    public boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
        String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
        Log.d(TAG, "deviceName" + deviceName);
        if (!deviceName.equals("MStar Smart TV Keypad"))
            return false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                return true;
            default:
                return false;
        }
    }

    private void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // If MapKeyPadToIR returns true,the previous keycode has been changed
        // to responding
        // android d_pad keys,just return.
        if (MapKeyPadToIR(keyCode, event))
            return true;
        Intent intent = new Intent();
        int currentid = getCurrentFocus().getId();
        int nCurrentFrequency = mTvChannelManager.getAtvCurrentFrequency();
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_cha_ntscatvmanualtuning_frequency:
                        mTvChannelManager.startAtvManualTuning(3 * 1000, nCurrentFrequency,
                                TvChannelManager.ATV_MANUAL_TUNE_MODE_FINE_TUNE_UP);
                        updateAtvManualtuningfreq();
                        break;
                    case R.id.linearlayout_cha_ntscatvmanualtuning_channelnum:
                        if ((mCurChannelNumber + 1) > mMaxCh) {
                            mNewChannelNumber = mMinCh;
                        } else {
                            mNewChannelNumber = (mCurChannelNumber + 1);
                        }
                        if (doManualTune(mNewChannelNumber)) {
                            mCurChannelNumber = mNewChannelNumber;
                        }
                        updateAtvManualtuningComponents();
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_cha_ntscatvmanualtuning_frequency:
                        mTvChannelManager.startAtvManualTuning(3 * 1000, nCurrentFrequency,
                                TvChannelManager.ATV_MANUAL_TUNE_MODE_FINE_TUNE_DOWN);
                        updateAtvManualtuningfreq();
                        break;
                    case R.id.linearlayout_cha_ntscatvmanualtuning_channelnum:
                        if ((mCurChannelNumber - 1) < mMinCh) {
                            mNewChannelNumber = mMaxCh;
                        } else {
                            mNewChannelNumber = (mCurChannelNumber - 1);
                        }
                        if (doManualTune(mNewChannelNumber)) {
                            mCurChannelNumber = mNewChannelNumber;
                        }
                        updateAtvManualtuningComponents();
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                intent.setAction(TvIntent.MAINMENU);
                intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                finish();
                break;
            case KeyEvent.KEYCODE_PROG_RED:
                mTvChannelManager.saveAtvProgram(mCurChannelNumber);
                intent.setAction(TvIntent.MAINMENU);
                intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                finish();
                break;
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
                if (currentid == R.id.linearlayout_cha_ntscatvmanualtuning_channelnum) {
                    int currNumLen = 0;

                    if (mDirectChangeCh == false) {
                        if (KeyEvent.KEYCODE_0 == keyCode) {
                            // just ignore input when first input is 0.
                            break;
                        }
                        mDirectChangeCh = true;
                        mBufferInputNumber = 0;
                    }
                    currNumLen = getIntLen(mBufferInputNumber);

                    if (currNumLen >= getIntLen(mMaxCh)) {
                        if (KeyEvent.KEYCODE_0 == keyCode) {
                            // just ignore input when first input is 0.
                            break;
                        }
                        mBufferInputNumber = 0;
                    }

                    mBufferInputNumber = mBufferInputNumber * 10
                                + (keyCode - KeyEvent.KEYCODE_0);
                    //end by pan
                    if(9<mBufferInputNumber){
                    Log.d("pan_num","mBufferInputNumber3: " + mBufferInputNumber);
                    if (mBufferInputNumber > mMaxCh) {
                        mBufferInputNumber = mMaxCh;
                    }
                    if (mBufferInputNumber < mMinCh) {
                        mBufferInputNumber = mMinCh;
                        Log.d("pan_num","mBufferInputNumber_min: " + mBufferInputNumber);
                     }
                    }

                    mViewholder.text_cha_ntscatvmanualtuning_channelnum_val.setText(Integer.toString(mBufferInputNumber));

                    mNewChannelNumber = Utility.getATVRealChNum(mBufferInputNumber);
                    mDirectChangeChTimeout.removeCallbacks(mDirectChangeChTimeoutRunnable);
                    mDirectChangeChTimeout.postDelayed(mDirectChangeChTimeoutRunnable, USER_CHANGE_CHANNEL_TIMEOUT);

                    if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                        mTvChannelManager.stopAtvManualTuning();
                    }
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private int getIntLen(int num) {
        int numLen = 1;

        while(num > 9) {
            numLen++;
            num/=10;
        }
        return numLen;
    }

    private void updateAtvManualtuningfreq() {
        String str_val;
        int freqKhz = mTvChannelManager.getAtvCurrentFrequency();
        int minteger = freqKhz / 1000;
        int mfraction = (freqKhz % 1000) / 10; // 0.25M not

        str_val = Integer.toString(minteger) + "." + Integer.toString(mfraction);
        mViewholder.text_cha_ntscatvmanualtuning_freqency_val.setText(str_val);
    }

    private void updateAtvManualtuningComponents() {
        String str_val;
        // 0.250M
        str_val = Integer.toString(Utility.getATVDisplayChNum(mCurChannelNumber));
        mViewholder.text_cha_ntscatvmanualtuning_channelnum_val.setText(str_val);
        updateAtvManualtuningfreq();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(mAtvPlayerEventListener);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(mAtvPlayerEventListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    /**
     * doManualTune, one can update the channel number displayed on OSD after the function executed successfully
     * @param newChannelNumber, the internal channer number going to be set
     * @return boolean, true:tunned success, false: failed
     */
    private boolean doManualTune(int newChannelNumber) {
        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            if (TvIsdbChannelManager.CHANNEL_SET_FAIL != TvIsdbChannelManager.getInstance().setChannel(newChannelNumber, true)) {
                return true;
            } else {
                return false;
            }
        } else {
            int ret = mTvChannelManager.getAtvProgramInfo(TvChannelManager.GET_PROGRAMINFO_DIRECT_TUNED, newChannelNumber);
            if (0 < ret) {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    TvAtscChannelManager.getInstance().programSel(newChannelNumber,0);
                } else {
                    mTvChannelManager.selectProgram(newChannelNumber,
                        TvChannelManager.SERVICE_TYPE_ATV);
                }
            } else {
                mTvChannelManager.startATSCAtvManualTuning(newChannelNumber, 0);
            }
        }
        return true;
    }
}
