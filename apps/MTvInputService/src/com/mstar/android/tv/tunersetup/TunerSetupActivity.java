//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.android.tv.tunersetup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import android.media.tv.TvInputManager;
import android.media.tv.TvInputInfo;
import android.util.Log;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.tunersetup.holder.ChannelViewHolder;
import com.mstar.android.tv.tunersetup.LittleDownTimer;
import com.mstar.android.tv.util.Constant;
import com.mstar.android.tv.util.ChannelUpdateTask;
import com.mstar.android.tv.inputservice.R;

/**
 * TunerSetupActivity
 */
public class TunerSetupActivity extends Activity {
    private static final String TAG = "TunerSetupActivity";

    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    public static final int CHANNEL_PAGE = 0;  // TODO: not used now, check if need

    public static final int TOTAL_PAGE_NUM = 1;

    private static final int RESUME_TASK_MSG = 7758521;

    private int mCurrentPage = CHANNEL_PAGE;  // TODO: not used now, check if need

    private ViewFlipper mViewFlipper = null;

    private LayoutInflater mLayoutInflater;

    private ChannelViewHolder mChannelViewHolder;

    private boolean mIsOnCreateCalled = false;

    private ChannelSetupTask mChannelSetupTask;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LittleDownTimer.TIME_OUT_MSG:
                    finish();
                    break;
                case LittleDownTimer.SELECT_RETURN_MSG:
                    if (!(getCurrentFocus() instanceof LinearLayout)) {
                        return;
                    }
                    LinearLayout currentLinearLayout = (LinearLayout) getCurrentFocus();
                    if (currentLinearLayout != null) {
                        currentLinearLayout.clearFocus();
                        currentLinearLayout.requestFocus();
                        currentLinearLayout.setSelected(false);
                    }
                    break;
                case RESUME_TASK_MSG:
                    LittleDownTimer.resumeMenu();
                    LittleDownTimer.resumeItem();
                    if (mIsOnCreateCalled) {
                        LittleDownTimer.setHandler(handler);
                        addAllViews();
                        initializeUiComponent(CHANNEL_PAGE);
                        mIsOnCreateCalled = false;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        mIsOnCreateCalled = true;
        mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper_main_menu);
        if (getIntent().getBooleanExtra(Constant.UPDATE_TV_PROVIDER, false)) {
            handleUpdateChannels();
        }
        LittleDownTimer.getInstance().start();
        LittleDownTimer.setMenuStatus(5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayoutInflater = LayoutInflater.from(TunerSetupActivity.this);
        handler.sendEmptyMessage(RESUME_TASK_MSG);
    }

    @Override
    protected void onPause() {
        try {
            LittleDownTimer.pauseMenu();
            LittleDownTimer.pauseItem();
        } catch (Exception e) {
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LittleDownTimer.destory();
        if (mChannelSetupTask != null) {
            mChannelSetupTask.cancel(true);
            mChannelSetupTask = null;
        }
        super.onDestroy();
    }

    @Override
    public void onUserInteraction() {
        LittleDownTimer.resetMenu();
        LittleDownTimer.resetItem();
        super.onUserInteraction();
    }

    private void addAllViews() {
        if (mViewFlipper.getChildCount() >= TOTAL_PAGE_NUM) {
            Log.d(TAG, "addAllViews: ViewFlipper child count " + mViewFlipper.getChildCount() +
                    " >= TOTAL_PAGE_NUM " + TOTAL_PAGE_NUM + ", return");
            return;
        }
        mViewFlipper.removeAllViews();
        for (int i = 0; i < TOTAL_PAGE_NUM; i++) {
            addView(i);
        }
    }

    private void addView(int page) {
        switch (page) {
            case CHANNEL_PAGE:
                try {
                    mViewFlipper.addView(mLayoutInflater.inflate(R.layout.channel, null), page);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void initializeUiComponent(int page) {
        mViewFlipper.setDisplayedChild(0);
        switch (page) {
            case CHANNEL_PAGE:
                if (mChannelViewHolder == null) {
                    mChannelViewHolder = new ChannelViewHolder(TunerSetupActivity.this);
                    mChannelViewHolder.findViews();
                }
                mChannelViewHolder.updateUi();
                break;
            default:
                break;
        }
        initializeFocus();
    }

    private void initializeFocus() {
        View view = findViewById(R.id.linearlayout_cha_antennatype);
        if (view != null) {
            view.requestFocus();
        }
    }

    private void handleUpdateChannels() {
        TvInputInfo currentInputInfo = null;
        TvInputManager manager = (TvInputManager) getSystemService(Context.TV_INPUT_SERVICE);
        for (TvInputInfo info : manager.getTvInputList()) {
            if (info.getType() == TvInputInfo.TYPE_TUNER &&
                    info.getServiceInfo().packageName.equals(getPackageName())) {
                currentInputInfo = info;
                break;
            }
        }
        if (currentInputInfo == null) {
            Log.e(TAG, "insertChannelsToTvProvider: currentInputInfo == null, return");
            return;
        }
        if (mChannelSetupTask != null) {
            mChannelSetupTask.cancel(true);
            mChannelSetupTask = null;
        }
        mChannelSetupTask = new ChannelSetupTask(this, currentInputInfo.getId(), true);
        mChannelSetupTask.execute();
    }

    private class ChannelSetupTask extends ChannelUpdateTask {
        public ChannelSetupTask(Context context, String inputId, boolean isScan) {
            super(context, inputId, isScan);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "ChannelSetupTask: set source to storage");
            TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
        }
    }
}
