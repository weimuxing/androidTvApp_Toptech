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

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.CycleScrollView;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.util.Utility;
import com.mstar.util.Tools;

public class DemoViewHolder {
    private final static String TAG = "DemoViewHolder";

    private final int COMBO_BUTTON_VALUE_OFF = 0;

    private final int COMBO_BUTTON_VALUE_ON = 1;

    private final int MSG_UPDATE_MWE = 1000;

    public Activity demoActivity;

    protected CycleScrollView mScrollView;

    protected ComboButton comboBtnMwe;

    protected ComboButton comboBtnDbc;

    protected ComboButton comboBtnDlc;

    protected ComboButton comboBtnDcc;

    protected ComboButton comboBtnNr;

    protected ComboButton comboBtnUClear;

    /* this mapping is reference to str_arr_mwe_text in string.xml */
    private final static int IDX_DEMO_MODE_SQUARE = 6;

    public DemoViewHolder(Activity activity) {
        demoActivity = activity;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_MWE) {
                if (getSignalStatus()) {
                    TvS3DManager tvS3DManager = TvS3DManager.getInstance();
                    tvS3DManager.get3dDisplayFormat();
                    int idx = comboBtnMwe.getIdx();
                    if ((tvS3DManager.get3dDisplayFormat() != TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE)
                            && (idx != TvPictureManager.MWE_DEMO_MODE_OFF)) {
                        tvS3DManager
                                .set3dDisplayFormat(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                        Toast toast = Toast.makeText(demoActivity,
                                R.string.str_demo_toast, 5);
                        toast.show();
                    }
                    TvPictureManager.getInstance().setMWEDemoMode(mappingIdxToMweType(idx));
                } else {
                    offMWE();
                }
            }
        };
    };

    public void findViews() {
        mScrollView = (CycleScrollView) demoActivity
                .findViewById(R.id.cyclescrollview_demo_scroll_view);

        comboBtnMwe = new ComboButton(demoActivity, demoActivity.getResources()
                .getStringArray(R.array.str_arr_mwe_text),
                R.id.linearlayout_demo_mwe, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                    mHandler.sendEmptyMessage(MSG_UPDATE_MWE);
            }
        };
        comboBtnMwe.setIdx(mappingMweTypeToIdx(TvPictureManager.getInstance().getMWEDemoMode()));

        comboBtnDbc = new ComboButton(demoActivity, demoActivity.getResources()
                .getStringArray(R.array.str_arr_demo_onoff),
                R.id.linearlayout_demo_dbc, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (COMBO_BUTTON_VALUE_OFF == comboBtnDbc.getIdx()) {
                    TvPictureManager.getInstance().disableDbc();
                } else {
                    TvPictureManager.getInstance().enableDbc();
                }
            }
        };
        comboBtnDbc.setIdx((true == TvPictureManager.getInstance().isDbcEnabled()) ? COMBO_BUTTON_VALUE_ON : COMBO_BUTTON_VALUE_OFF);

        comboBtnDlc = new ComboButton(demoActivity, demoActivity.getResources()
                .getStringArray(R.array.str_arr_demo_onoff),
                R.id.linearlayout_demo_dlc, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (COMBO_BUTTON_VALUE_OFF == comboBtnDlc.getIdx()) {
                    TvPictureManager.getInstance().disableDlc();
                } else {
                    TvPictureManager.getInstance().enableDlc();
                }
            }
        };
        comboBtnDlc.setIdx((true == TvPictureManager.getInstance().isDlcEnabled()) ? COMBO_BUTTON_VALUE_ON : COMBO_BUTTON_VALUE_OFF);

        comboBtnDcc = new ComboButton(demoActivity, demoActivity.getResources()
                .getStringArray(R.array.str_arr_demo_onoff),
                R.id.linearlayout_demo_dcc, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (COMBO_BUTTON_VALUE_OFF == comboBtnDcc.getIdx()) {
                    TvPictureManager.getInstance().disableDcc();
                } else {
                    TvPictureManager.getInstance().enableDcc();
                }
            }
        };
        comboBtnDcc.setIdx((true == TvPictureManager.getInstance().isDccEnabled()) ? COMBO_BUTTON_VALUE_ON : COMBO_BUTTON_VALUE_OFF);

        comboBtnNr = new ComboButton(demoActivity, demoActivity.getResources()
                .getStringArray(R.array.str_arr_pic_imgnoisereduction_vals),
                R.id.linearlayout_demo_nr, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                    TvPictureManager.getInstance().setNoiseReduction(comboBtnNr.getIdx());
            }
        };
        comboBtnNr.setIdx(TvPictureManager.getInstance().getNoiseReduction());

        comboBtnUClear = new ComboButton(demoActivity, demoActivity.getResources()
                .getStringArray(R.array.str_arr_demo_onoff),
                R.id.linearlayout_demo_uclear, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (COMBO_BUTTON_VALUE_OFF == comboBtnUClear.getIdx()) {
                    TvPictureManager.getInstance().setUClearStatus(false);
                } else {
                    TvPictureManager.getInstance().setUClearStatus(true);
                }
            }
        };
        comboBtnUClear.setIdx((true == TvPictureManager.getInstance().isUClearOn()) ? COMBO_BUTTON_VALUE_ON : COMBO_BUTTON_VALUE_OFF);

        if (true == Tools.isBox()) {
            /* MWE, DBC, DLC, DCC and Ultra Clear are currently not supoorted in Box project. */
            comboBtnMwe.setVisibility(View.GONE);
            comboBtnDbc.setVisibility(View.GONE);
            comboBtnDlc.setVisibility(View.GONE);
            comboBtnDcc.setVisibility(View.GONE);
            comboBtnUClear.setVisibility(View.GONE);
        }

        Utility.setDefaultFocus((LinearLayout) demoActivity.findViewById(R.id.linearlayout_demo));
    }

    public void pageOnFocus() {
        TvCommonManager.getInstance().speakTtsDelayed(
            demoActivity.getApplicationContext().getString(R.string.str_demo_demo)
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
        mScrollView.ttsSpeakFocusItem();
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
    }

    private int mappingIdxToMweType(int mweIdx) {
        /* this mapping is reference to str_arr_mwe_text in string.xml */
        switch (mweIdx) {
            case IDX_DEMO_MODE_SQUARE :
                return TvPictureManager.MWE_DEMO_MODE_SQUAREMOVE;
            default :
                return mweIdx;
        }
    }

    private int mappingMweTypeToIdx(int mweType) {
        /* this mapping is reference to str_arr_mwe_text in string.xml */
        switch (mweType) {
            case TvPictureManager.MWE_DEMO_MODE_SQUAREMOVE :
                return IDX_DEMO_MODE_SQUARE;
            default :
                return mweType;
        }
    }

    private boolean getSignalStatus() {
        int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        int signalStatus = TvChannelManager.getInstance().getSignalStatus(curInputSource);
        if (TvChannelManager.TVPLAYER_SIGNAL_LOCK == signalStatus) {
            return true;
        } else {
            return false;
        }
    }

    private void offMWE() {
        TvPictureManager.getInstance().setMWEDemoMode(mappingIdxToMweType(TvPictureManager.MWE_DEMO_MODE_OFF));
        Toast toast = Toast.makeText(demoActivity,
                R.string.str_unsupported, Toast.LENGTH_SHORT);
        toast.show();

        comboBtnMwe.setIdx(mappingIdxToMweType(TvPictureManager.MWE_DEMO_MODE_OFF));
    }
}
