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

package com.mstar.tv.tvplayer.ui;

import java.lang.Thread;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;
import com.mstar.util.Tools;
import com.mstar.util.Utility;

public class SetupWizardActivity extends MstarBaseActivity {

    private static final String TAG = "SetupWizardActivity";

    private ComboButton mComboButtonAntenna = null;

    private ProgressDialog mProgressDialog;

    private Thread mThreadRouteChange = null;

    private Button btnStartTuning = null;

    private Button btnExit = null;

    private int mTvSystem = 0;

    private LinearLayout mLinearLayoutAntennatype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_wizard);
        mTvSystem = Utility.getCurrentTvSystem();
        initItem();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
        if (false == settings.getBoolean(Constant.PREFERENCES_IS_AUTOSCAN_LAUNCHED, false)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(Constant.PREFERENCES_IS_AUTOSCAN_LAUNCHED, true).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGoingToBeClosed(false);
        this.findViewById(android.R.id.content).setVisibility(View.VISIBLE);
    };

    @Override
    protected void onPause() {
        super.onResume();
        this.findViewById(android.R.id.content).setVisibility(View.GONE);
    };

    private void initItem() {
        btnStartTuning = (Button) findViewById(R.id.btn_auto_scan_positive);
        btnStartTuning.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(TvIntent.ACTION_AUTOTUNING_OPTION);
                if (intent.resolveActivity(SetupWizardActivity.this.getPackageManager()) != null) {
                    SetupWizardActivity.this.startActivity(intent);
                    SetupWizardActivity.this.finish();
                }
            }
        });
        btnExit = (Button) findViewById(R.id.btn_auto_scan_negative);
        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
            createAntennaSwitch();
            mComboButtonAntenna.setIdx(TvAtscChannelManager.getInstance().getDtvAntennaType());
            mLinearLayoutAntennatype.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Utility.ttsSepakLinearLayout(mLinearLayoutAntennatype);
                    }
                }
            });
            OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        Button b = (Button)v;
                        TvCommonManager.getInstance().speakTtsDelayed(
                            b.getText().toString()
                            , TvCommonManager.TTS_QUEUE_FLUSH
                            , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
                            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
                    }
                }
            };
            btnStartTuning.setOnFocusChangeListener(focusChangeListener);
            btnExit.setOnFocusChangeListener(focusChangeListener);
        } else if (TvCommonManager.TV_SYSTEM_ISDB != mTvSystem) {
            createAntennaSwitch();
            mComboButtonAntenna.setIdx(TvChannelManager.getInstance().getCurrentDtvRouteIndex());
        }
    }

    /*
     * Create ComboButton for switching DTVRoute (Antenna Type) Enumerate each
     * DTV System here and do corresponding behavior Add new cases if needed
     */
    private void createAntennaSwitch() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getResources().getString(R.string.str_cha_changing_antenna_type));
        mProgressDialog.setMessage(getResources().getString(R.string.str_cha_please_wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        int[] mRouteTableValue = Utility.getRouteTable();
        String[] mRouteTableName = Utility.getRouteTableName();

        for (int i = 0; i < mRouteTableValue.length; ++i) {
            Log.d(TAG, "!!!! route[" + i + "] = " + mRouteTableValue[i]);
        }

        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            if (null != findViewById(R.id.linearlayout_cha_antennatype)) {
                ((LinearLayout) findViewById(R.id.linearlayout_cha_antennatype))
                        .setVisibility(View.GONE);
            }
        } else {
            mLinearLayoutAntennatype = (LinearLayout) findViewById(R.id.linearlayout_cha_antennatype);
            mComboButtonAntenna = new ComboButton(SetupWizardActivity.this, mRouteTableName,
                    R.id.linearlayout_cha_antennatype, 1, 2, ComboButton.DIRECT_SWITCH) {
                @Override
                public void doUpdate() {
                    super.doUpdate();

                    LittleDownTimer.pauseItem();
                    LittleDownTimer.pauseMenu();
                    mProgressDialog.show();

                    if (mThreadRouteChange == null
                            || mThreadRouteChange.getState() == Thread.State.TERMINATED) {
                        /*
                         * Change TV Route may be time consuming, use another
                         * thread to avoid ANR
                         */
                        mThreadRouteChange = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "New Thread Starting Change TV Route !");

                                /*
                                 * Retrieve the index of ComboButton, which is
                                 * same as the routeTable (mRouteTableValue)
                                 */
                                setRoute(getIdx());

                                /* Prepare resuming to menu */
                                if (null != mProgressDialog) {
                                    mProgressDialog.dismiss();
                                }
                                LittleDownTimer.resetItem();
                                LittleDownTimer.resetMenu();
                                LittleDownTimer.resumeItem();
                                LittleDownTimer.resumeMenu();
                            }
                        });

                        mThreadRouteChange.start();
                    } else {
                        Log.d(TAG,
                                "Abort, another thread is already starting changing tv route. the combobutton is not switchable until it finish");
                    }
                }
            };

            /* if isBox, the comboButton is shown but not controlable */
            if (Tools.isBox()) {
                mComboButtonAntenna.setEnable(false);
            }
        }

        /* Hide the invalid TV Route */
        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
            for (int i = 0; i < mRouteTableValue.length; ++i) {
                if (TvChannelManager.DTV_ANTENNA_TYPE_NONE == mRouteTableValue[i]) {
                    mComboButtonAntenna.setItemEnable(i, false);
                }
            }
        } else if (TvCommonManager.TV_SYSTEM_ISDB != mTvSystem) {
            for (int i = 0; i < mRouteTableValue.length; ++i) {
                if (TvChannelManager.TV_ROUTE_NONE == mRouteTableValue[i]) {
                    mComboButtonAntenna.setItemEnable(i, false);
                }
            }
        }
    }

    private void setRoute(int nDtvRoute) {
        switch (mTvSystem) {
            case TvCommonManager.TV_SYSTEM_DVBT:
            case TvCommonManager.TV_SYSTEM_DVBC:
            case TvCommonManager.TV_SYSTEM_DVBS:
            case TvCommonManager.TV_SYSTEM_DVBT2:
            case TvCommonManager.TV_SYSTEM_DVBS2:
            case TvCommonManager.TV_SYSTEM_DTMB:
                TvDvbChannelManager.getInstance().setDtvAntennaType(nDtvRoute);
                break;
            case TvCommonManager.TV_SYSTEM_ATSC:
                TvAtscChannelManager.getInstance().setDtvAntennaType(nDtvRoute);
                TvChannelManager.getInstance().changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ALL,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                break;
            case TvCommonManager.TV_SYSTEM_ISDB:
                break;
            default:
                break;
        }
    }
}
