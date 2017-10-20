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

package com.mstar.android.tv.tunersetup.holder;

import java.lang.Thread;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.media.tv.TvInputManager;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;

import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tv.tunersetup.component.ComboButton;
import com.mstar.android.tv.inputservice.R;
import com.mstar.android.tv.tunersetup.LittleDownTimer;
import com.mstar.android.tv.tunersetup.TvIntent;
import com.mstar.android.tv.util.Utility;

public class ChannelViewHolder {
    private static final String TAG = "ChannelViewHolder";

    private final int BANDWIDTH_7_MHZ_INDEX = 0;

    private final int BANDWIDTH_8_MHZ_INDEX = 1;

    private final int UHF_7_MHZ_DISABLE = 0;

    private final int UHF_7_MHZ_ENABLE = 1;

    private ComboButton mComboButtonAntenna = null;

    private ComboButton mComboButtonBandwidthSwitch = null;

    private ComboButton mComboButtonUhf7MhzOption = null;

    private Thread mThreadRouteChange = null;

    private ProgressDialog mProgressDialog = null;

    protected LinearLayout linear_cha_autotuning;

    protected LinearLayout linear_cha_dtvmanualtuning;

    protected LinearLayout linear_cha_atvmanualtuning;

    protected LinearLayout linear_cha_dvbs_lnbsetting;

    private TvS3DManager tvS3DManager = null;

    private final Activity mChannelActivity;

    private final Intent mIntent = new Intent();

    private int mTvSystem = 0;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    public ChannelViewHolder(Activity activity) {
        mChannelActivity = activity;
        tvS3DManager = TvS3DManager.getInstance();
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        }
    }

    public void findViews() {
        /* Create Set Antenna Type ComboButton according to the given TV System */
        createAntennaSwitch();
        createBandwidthSwitch();
        linear_cha_autotuning = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_autotuning);
        linear_cha_dtvmanualtuning = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_dtvmanualtuning);
        linear_cha_atvmanualtuning = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_atvmanualtuning);
        linear_cha_dvbs_lnbsetting = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_dvbs_lnbsetting);
        if (false == Utility.isSupportATV()) {
            linear_cha_atvmanualtuning.setVisibility(View.GONE);
        }
        boolean en = false;
        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
            en = true;
        } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR == TvIsdbChannelManager.getInstance().getAntennaType()) {
                /* DTV_ANTENNA_TYPE_AIR */
                en = true;
            } else {
                /* DTV_ANTENNA_TYPE_CABLE */
                en = false;
            }
        } else {
            /* TV system: DVBC, DVBT, DVBS, DTMB */
            en = true;
        }
        enableSingleItemOrNot(linear_cha_dtvmanualtuning, en);
        int curRouteIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
        TvTypeInfo info = TvCommonManager.getInstance().getTvInfo();
        if ((TvChannelManager.TV_ROUTE_DVBS == info.routePath[curRouteIndex])
                || TvChannelManager.TV_ROUTE_DVBS2 == info.routePath[curRouteIndex]) {
            linear_cha_dvbs_lnbsetting.setVisibility(View.VISIBLE);
            enableSingleItemOrNot(linear_cha_dvbs_lnbsetting, true);
        }
        setOnClickLisenters();

        /** The 7 MHz Over UHF option is avaliable when DVB-T2 is avaliable */
        if (TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT2) >= 0) {
            mComboButtonUhf7MhzOption = new ComboButton(mChannelActivity, mChannelActivity
                    .getResources().getStringArray(R.array.str_arr_uhf_7mhz_option),
                    R.id.linearlayout_cha_bandwidth_uhf_7mhz, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
                @Override
                public void doUpdate() {
                    if (getIdx() == UHF_7_MHZ_ENABLE) {
                        TvChannelManager.getInstance().setUhf7MhzEnabled(true);
                    } else {
                        TvChannelManager.getInstance().setUhf7MhzEnabled(false);
                    }
                }
            };
        }
    }

    public void updateUi() {
        switch (mTvSystem) {
            case TvCommonManager.TV_SYSTEM_DVBT:
            case TvCommonManager.TV_SYSTEM_DVBC:
            case TvCommonManager.TV_SYSTEM_DVBS:
            case TvCommonManager.TV_SYSTEM_DVBT2:
            case TvCommonManager.TV_SYSTEM_DVBS2:
            case TvCommonManager.TV_SYSTEM_DTMB:
                if (null != mComboButtonAntenna) {
                    mComboButtonAntenna.setIdx(TvChannelManager.getInstance().getCurrentDtvRouteIndex());
                }
                break;
            case TvCommonManager.TV_SYSTEM_ATSC:
                if (null != mComboButtonAntenna) {
                    mComboButtonAntenna.setIdx(mTvAtscChannelManager.getDtvAntennaType());
                }
                break;
            case TvCommonManager.TV_SYSTEM_ISDB:
                // ISDB has no setAntennaType Button
                break;
            default:
                break;
        }
        if (null != mComboButtonBandwidthSwitch) {
            int bandwidth = TvChannelManager.getInstance().getTuningBandwidth();
            if (TvChannelManager.RF_CHANNEL_BANDWIDTH_7_MHZ == bandwidth) {
                mComboButtonBandwidthSwitch.setIdx(BANDWIDTH_7_MHZ_INDEX);
            } else if (TvChannelManager.RF_CHANNEL_BANDWIDTH_8_MHZ == bandwidth) {
                mComboButtonBandwidthSwitch.setIdx(BANDWIDTH_8_MHZ_INDEX);
            }
        }
        if (null != mComboButtonUhf7MhzOption) {
            if (true == TvChannelManager.getInstance().getUhf7MhzEnabled()) {
                mComboButtonUhf7MhzOption.setIdx(UHF_7_MHZ_ENABLE);
            } else {
                mComboButtonUhf7MhzOption.setIdx(UHF_7_MHZ_DISABLE);
            }
            boolean showUhf7MhzOption = false;
            /** The Option only show for Sweden and Denmark and under DVB-T2 */
            if (TvCommonManager.getInstance().getCurrentTvInputSource()
                    == TvCommonManager.INPUT_SOURCE_DTV) {
                if (TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT2)
                        == TvChannelManager.getInstance().getCurrentDtvRouteIndex()) {
                    int country = TvChannelManager.getInstance().getSystemCountryId();
                    if ((TvCountry.SWEDEN == country) || (TvCountry.DENMARK == country)) {
                        showUhf7MhzOption = true;
                    }
                }
            }
            mComboButtonUhf7MhzOption.setVisibility(showUhf7MhzOption);
        }
        Utility.setDefaultFocus((LinearLayout) mChannelActivity.findViewById(R.id.linearlayout_channel));
    }

    private void setOnClickLisenters() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentid = mChannelActivity.getCurrentFocus().getId();
                switch (currentid) {
                    case R.id.linearlayout_cha_autotuning:
                        if ((mTvSystem != TvCommonManager.TV_SYSTEM_ATSC)
                                && (mTvSystem != TvCommonManager.TV_SYSTEM_ISDB)) {
                            if (TvCiManager.getInstance().isOpMode()) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mChannelActivity, R.string.str_op_forbid_channel_tuning_content, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }

                        mIntent.setAction(TvIntent.ACTION_AUTOTUNING_OPTION);
                        if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                            mChannelActivity.startActivity(mIntent);
                            mChannelActivity.finish();
                        }
                        break;
                    case R.id.linearlayout_cha_dvbs_lnbsetting:
                        mIntent.setAction(TvIntent.ACTION_LNBSETTING);
                        if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                            mChannelActivity.startActivity(mIntent);
                        }
                        mChannelActivity.finish();
                        break;
                    case R.id.linearlayout_cha_dtvmanualtuning:
                        if ((mTvSystem != TvCommonManager.TV_SYSTEM_ATSC)
                                && (mTvSystem != TvCommonManager.TV_SYSTEM_ISDB)) {
                            Log.d(TAG, "isOpMode: " + TvCiManager.getInstance().isOpMode());
                            if (TvCiManager.getInstance().isOpMode()) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mChannelActivity, R.string.str_op_forbid_channel_tuning_content, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }

                        mIntent.setAction(TvIntent.ACTION_DTV_MANUAL_TUNING);
                        if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                            mChannelActivity.startActivity(mIntent);
                            mChannelActivity.finish();
                        }
                        break;
                    case R.id.linearlayout_cha_atvmanualtuning:
                        if ((mTvSystem >= TvCommonManager.TV_SYSTEM_DVBT)
                                && (mTvSystem <= TvCommonManager.TV_SYSTEM_DTMB)) {
                            if (TvCiManager.getInstance().isOpMode()) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    public void run() {
                                        Toast.makeText(mChannelActivity, R.string.str_op_forbid_channel_tuning_content, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }
                        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
                            mIntent.setAction(TvIntent.ACTION_NTSC_ATV_MANUAL_TUNING);
                        } else if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)) {
                            mIntent.setAction(TvIntent.ACTION_PAL_ATV_MANUAL_TUNING);
                        } else {
                            break;
                        }
                        if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                            mChannelActivity.startActivity(mIntent);
                            mChannelActivity.finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        linear_cha_autotuning.setOnClickListener(listener);
        linear_cha_dtvmanualtuning.setOnClickListener(listener);
        linear_cha_atvmanualtuning.setOnClickListener(listener);
        linear_cha_dvbs_lnbsetting.setOnClickListener(listener);
    }

    void enableSingleItemOrNot(LinearLayout linearLayout, boolean isEnable) {
        if (!isEnable) {
            ((TextView) (linearLayout.getChildAt(0))).setTextColor(Color.GRAY);
            linearLayout.setEnabled(false);
            linearLayout.setFocusable(false);
        } else {
            ((TextView) (linearLayout.getChildAt(0))).setTextColor(Color.WHITE);
            linearLayout.setEnabled(true);
            linearLayout.setFocusable(true);
        }
    }

    void enableCompositeItemOrNot(LinearLayout linearLayout, boolean isEnable, short Count) {
        if (!isEnable) {
            for (short i = 1; i <= Count; i++) {
                ((TextView) (linearLayout.getChildAt(i))).setTextColor(Color.GRAY);
            }
            linearLayout.setEnabled(false);
            linearLayout.setFocusable(false);
        } else {
            for (short i = 1; i <= Count; i++) {
                ((TextView) (linearLayout.getChildAt(i))).setTextColor(Color.WHITE);
            }
            linearLayout.setEnabled(true);
            linearLayout.setFocusable(true);
        }
    }

    /*
     * Create ComboButton for switching DTVRoute (Antenna Type)
     * Enumerate each DTV System here and do corresponding behavior
     * Add new cases if needed
     */
    private void createAntennaSwitch() {
        mProgressDialog = new ProgressDialog(mChannelActivity);
        mProgressDialog.setTitle(mChannelActivity.getResources().getString(R.string.str_cha_changing_antenna_type));
        mProgressDialog.setMessage(mChannelActivity.getResources().getString(R.string.str_cha_please_wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        int[] mRouteTableValue = Utility.getRouteTable();
        String[] mRouteTableName = Utility.getRouteTableName(mChannelActivity);

        for(int i = 0; i < mRouteTableValue.length; ++i) {
            Log.d(TAG, "!!!! route[" + i + "] = " + mRouteTableValue[i]);
        }

        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            if (null != mChannelActivity.findViewById(R.id.linearlayout_cha_antennatype)) {
                ((LinearLayout)mChannelActivity.findViewById(R.id.linearlayout_cha_antennatype)).setVisibility(View.GONE);
            }
        } else {
            mComboButtonAntenna = new ComboButton(mChannelActivity, mRouteTableName, R.id.linearlayout_cha_antennatype,
                    1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
                @Override
                public void doUpdate() {
                    super.doUpdate();

                    LittleDownTimer.pauseItem();
                    LittleDownTimer.pauseMenu();
                    mProgressDialog.show();

                    if (mThreadRouteChange == null || mThreadRouteChange.getState() == Thread.State.TERMINATED ) {
                        /* Change TV Route may be time consuming, use another thread to avoid ANR */
                        mThreadRouteChange = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "New Thread Starting Change TV Route !");

                                /* Retrieve the index of ComboButton, which is same as the routeTable (mRouteTableValue) */
                                setRoute(getIdx());

                                /* Prepare resuming to menu */
                                if (null != mProgressDialog) {
                                    mProgressDialog.dismiss();
                                }
                                mChannelActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        ChannelViewHolder.this.updateUi();
                                    }
                                });
                                LittleDownTimer.resetItem();
                                LittleDownTimer.resetMenu();
                                LittleDownTimer.resumeItem();
                                LittleDownTimer.resumeMenu();
                            }
                        });

                        mThreadRouteChange.start();
                    } else {
                        Log.d(TAG, "Abort, another thread is already starting changing tv route. the combobutton is not switchable until it finish");
                    }
                }
            };

            /* if isBox, the comboButton is shown but not controlable */
            if (Utility.isBox()) {
                mComboButtonAntenna.setEnable(false);
            }
        }

        /* Hide the invalid TV Route */
        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
            for (int i = 0; i < mRouteTableValue.length; ++i ) {
                if (TvChannelManager.DTV_ANTENNA_TYPE_NONE == mRouteTableValue[i]) {
                    mComboButtonAntenna.setItemEnable(i, false);
                }
            }
        } else if (TvCommonManager.TV_SYSTEM_ISDB != mTvSystem) {
            for (int i = 0; i < mRouteTableValue.length; ++i ) {
                if (TvChannelManager.TV_ROUTE_NONE == mRouteTableValue[i]) {
                    mComboButtonAntenna.setItemEnable(i, false);
                }
            }
        }
    }

    private void setRoute(final int nDtvRouteIndex) {
        switch (mTvSystem) {
            case TvCommonManager.TV_SYSTEM_DVBT:
            case TvCommonManager.TV_SYSTEM_DVBC:
            case TvCommonManager.TV_SYSTEM_DVBS:
            case TvCommonManager.TV_SYSTEM_DVBT2:
            case TvCommonManager.TV_SYSTEM_DVBS2:
            case TvCommonManager.TV_SYSTEM_DTMB:
                TvDvbChannelManager.getInstance().setDtvAntennaType(nDtvRouteIndex);
                tvS3DManager.setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);

                this.mChannelActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        TvTypeInfo info = TvCommonManager.getInstance().getTvInfo();
                        if ((TvChannelManager.TV_ROUTE_DVBS == info.routePath[nDtvRouteIndex])
                                || TvChannelManager.TV_ROUTE_DVBS2 == info.routePath[nDtvRouteIndex]) {
                            linear_cha_dvbs_lnbsetting.setVisibility(View.VISIBLE);
                            enableSingleItemOrNot(linear_cha_dvbs_lnbsetting, true);
                        } else {
                            linear_cha_dvbs_lnbsetting.setVisibility(View.GONE);
                            enableSingleItemOrNot(linear_cha_dvbs_lnbsetting, false);
                        }
                    }
                });
                break;
            case TvCommonManager.TV_SYSTEM_ATSC:
                TvAtscChannelManager.getInstance().setDtvAntennaType(nDtvRouteIndex);
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

    /*
     * Create ComboButton for switching tuning bandwidth
     */
    private void createBandwidthSwitch() {
        int curRouteIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
        TvTypeInfo info = TvCommonManager.getInstance().getTvInfo();
        if (TvChannelManager.TV_ROUTE_DVBT == info.routePath[curRouteIndex]
                || TvChannelManager.TV_ROUTE_DVBT2 == info.routePath[curRouteIndex]) {
            int country = TvChannelManager.getInstance().getSystemCountryId();
            if (TvCountry.NORWAY == country || TvCountry.AUSTRALIA == country
                    || TvCountry.FRANCE == country) {
                mComboButtonBandwidthSwitch = new ComboButton(mChannelActivity, mChannelActivity
                        .getResources().getStringArray(R.array.str_arr_bandwidth_option),
                        R.id.linearlayout_cha_bandwidth_switch, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
                    @Override
                    public void doUpdate() {
                        int index = mComboButtonBandwidthSwitch.getIdx();
                        if (BANDWIDTH_7_MHZ_INDEX == index) {
                            TvChannelManager.getInstance().setTuningBandwidth(
                                    TvChannelManager.RF_CHANNEL_BANDWIDTH_7_MHZ);
                        } else if (BANDWIDTH_8_MHZ_INDEX == index) {
                            TvChannelManager.getInstance().setTuningBandwidth(
                                    TvChannelManager.RF_CHANNEL_BANDWIDTH_8_MHZ);
                        }
                    }
                };
                if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                    mComboButtonBandwidthSwitch.setEnable(true);
                } else {
                    mComboButtonBandwidthSwitch.setEnable(false);
                }
            } else {
                if (null != mChannelActivity.findViewById(R.id.linearlayout_cha_bandwidth_switch)) {
                    ((LinearLayout) mChannelActivity
                            .findViewById(R.id.linearlayout_cha_bandwidth_switch))
                            .setVisibility(View.GONE);
                }
            }
        } else {
            if (null != mChannelActivity.findViewById(R.id.linearlayout_cha_bandwidth_switch)) {
                ((LinearLayout) mChannelActivity
                        .findViewById(R.id.linearlayout_cha_bandwidth_switch))
                        .setVisibility(View.GONE);
            }
        }
    }
}
