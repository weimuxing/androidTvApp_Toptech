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

package com.mstar.tv.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;

/**
 * This class implements the activity to support teletext capability.
 *
 */
public class TeletextActivity extends Activity {

    private final static String TAG = "TeletextActivity";

    public static final int CHANGE_TTX_STATUS = 38;

    private TvChannelManager mTvChannelManager;

    private OnDtvPlayerEventListener mDtvPlayerEventListener = null;

    private class DtvPlayerEventListener implements OnDtvPlayerEventListener {

        @Override
        public boolean onDtvChannelNameReady(int what) {
            return true;
        }

        @Override
        public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
            return false;
        }

        @Override
        public boolean onDtvProgramInfoReady(int what) {
            return true;
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
            Log.i(TAG,"TTX status = " + arg1);
            if (false == arg1) {
                finish();
            }
            return true;
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

    private Runnable killself = new Runnable() {

        @Override
        public void run() {
            mTvChannelManager.closeTeletext();
            // after close teletext, should direct activity to TV
            finish();
        }
    };

    protected Handler myHandler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CHANGE_TTX_STATUS) {
                Bundle b = msg.getData();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        mTvChannelManager = TvChannelManager.getInstance();
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getBooleanExtra("TTX_MODE_CLOCK", false)) {
                if (mTvChannelManager.openTeletext(TvChannelManager.TTX_MODE_CLOCK) == false) {
                    Log.e(TAG, "open teletext false");
                } else {
                    myHandler.postDelayed(killself, 5000);
                }
            } else {
                if (mTvChannelManager.openTeletext(TvChannelManager.TTX_MODE_NORMAL) == false) {
                    Log.e(TAG, "open teletext false");
                }
            }
        } else {
            if (mTvChannelManager.openTeletext(TvChannelManager.TTX_MODE_NORMAL) == false) {
                Log.e(TAG, "open teletext false");
            }
        }
        mDtvPlayerEventListener = new DtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnDtvPlayerEventListener(mDtvPlayerEventListener);
    }

    @Override
    protected void onDestroy() {
        myHandler.removeCallbacks(killself);
        TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mDtvPlayerEventListener = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mTvChannelManager.closeTeletext();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_0);
                return true;
            case KeyEvent.KEYCODE_1:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_1);
                return true;
            case KeyEvent.KEYCODE_2:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_2);
                return true;
            case KeyEvent.KEYCODE_3:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_3);
                return true;
            case KeyEvent.KEYCODE_4:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_4);
                return true;
            case KeyEvent.KEYCODE_5:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_5);
                return true;
            case KeyEvent.KEYCODE_6:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_6);
                return true;
            case KeyEvent.KEYCODE_7:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_7);
                return true;
            case KeyEvent.KEYCODE_8:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_8);
                return true;
            case KeyEvent.KEYCODE_9:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_DIGIT_9);
                return true;
            case KeyEvent.KEYCODE_PAGE_UP:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_PAGE_UP);
                return true;
            case KeyEvent.KEYCODE_PAGE_DOWN:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_PAGE_DOWN);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_PAGE_LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_PAGE_RIGHT);
                return true;
            case MKeyEvent.KEYCODE_TTX:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_NORMAL_MODE_NEXT_PHASE);
                if (!mTvChannelManager.isTeletextDisplayed()) {
                    finish();
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                mTvChannelManager.closeTeletext();
                finish();
                return true;
            case KeyEvent.KEYCODE_PROG_BLUE:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_CYAN);
                return true;
            case KeyEvent.KEYCODE_PROG_YELLOW:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_YELLOW);
                return true;
            case KeyEvent.KEYCODE_PROG_GREEN:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_GREEN);
                return true;
            case KeyEvent.KEYCODE_PROG_RED:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_RED);
                return true;
            case MKeyEvent.KEYCODE_TV_SETTING:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_MIX);
                return true;
            case MKeyEvent.KEYCODE_MSTAR_UPDATE:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_UPDATE);
                return true;
            case MKeyEvent.KEYCODE_MSTAR_SIZE:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_SIZE);
                return true;
            case MKeyEvent.KEYCODE_MSTAR_INDEX:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_INDEX);
                return true;
            case MKeyEvent.KEYCODE_MSTAR_HOLD:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_HOLD);
                return true;
            case MKeyEvent.KEYCODE_MSTAR_REVEAL:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_REVEAL);
                return true;
            case MKeyEvent.KEYCODE_LIST:
                mTvChannelManager.sendTeletextCommand(TvChannelManager.TTX_COMMAND_LIST);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
