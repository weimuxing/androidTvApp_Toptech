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

package com.mstar.tv.tvplayer.ui.holder;

import java.lang.Thread;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Time;

import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCaManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvOadManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.common.vo.DbSetting;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.ca.CaActivity;
import com.mstar.tv.tvplayer.ui.channel.ProgramListViewActivity;
import com.mstar.tv.tvplayer.ui.channel.LNBSettingActivity;
import com.mstar.tv.tvplayer.ui.component.CycleScrollView;
import com.mstar.tv.tvplayer.ui.dtv.CimmiActivity;
import com.mstar.tv.tvplayer.ui.dtv.epg.EPGActivity;
import com.mstar.tv.tvplayer.ui.dtv.epg.atsc.AtscEPGActivity;
import com.mstar.tv.tvplayer.ui.dtv.ewbs.EWBSInfoActivity;
import com.mstar.tv.tvplayer.ui.component.PasswordCheckDialog;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.MenuConstants;
import com.mstar.tv.tvplayer.ui.SwitchPageHelper;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.tuning.SignalInfoActivity;
import com.mstar.tv.tvplayer.ui.tuning.OadScan;
import com.mstar.util.Constant;
import com.mstar.util.Tools;
import com.mstar.util.Utility;

public class ChannelViewHolder {
    private static final String TAG = "ChannelViewHolder";

    private final int SWITCH_OFF = 0;

    private final int SWITCH_ON = 1;

    private final int VIEW_PROMPT_SWITCH_OFF = 0;

    private final int VIEW_PROMPT_SWITCH_ON = 1;

    private final int OAD_UPDATE_TIME_NOW = 24;

    private final int BANDWIDTH_7_MHZ_INDEX = 0;

    private final int BANDWIDTH_8_MHZ_INDEX = 1;

    private final int UHF_7_MHZ_DISABLE = 0;

    private final int UHF_7_MHZ_ENABLE = 1;

    protected TextView text_cha_software_update_oad_val;

    protected TextView text_cha_time_to_search_for_ssu_val;

    private ComboButton mComboButtonAntenna = null;

    private ComboButton mComboButtonBandwidthSwitch = null;

    private ComboButton mComboButtonViewPrompt = null;

    private ComboButton mComboButtonUhf7MhzOption = null;

    private ComboButton mComboButtonStandbyScan = null;

    private Thread mThreadRouteChange = null;

    private ProgressDialog mProgressDialog = null;

    protected LinearLayout linear_cha_epg;

    protected LinearLayout linear_cha_subcode;

    protected LinearLayout linear_cha_autotuning;

    protected LinearLayout linear_cha_dtvmanualtuning;

    protected LinearLayout linear_cha_atvmanualtuning;

    protected LinearLayout linear_cha_dvbs_lnbsetting;

    protected LinearLayout linear_cha_programedit;

    protected LinearLayout linear_cha_ciinformation;

    protected LinearLayout linear_cha_cainformation;

    protected LinearLayout linear_cha_signalinfo;

    protected LinearLayout linear_cha_ewbsinfo;

    protected LinearLayout linear_cha_softwareupdateoad;

    protected LinearLayout linear_cha_oadtime;

    protected LinearLayout linear_cha_oadscan;

    protected CycleScrollView mScrollView;

    private TvS3DManager tvS3DManager = null;

    private final Activity mChannelActivity;

    private final Intent mIntent = new Intent();

    private int focusedid = 0x00000000;

    private String[] mSoftwareUpdateOadTable;

    private int mTvSystem = 0;

    private int mAtscAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_AIR;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private int mSoftwareUpdateOadIndex = SWITCH_OFF;

    private int oadTime = 0;

    private PasswordCheckDialog mPasswordLock = null;

    public ChannelViewHolder(Activity activity) {
        this.mChannelActivity = activity;
        tvS3DManager = TvS3DManager.getInstance();
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        }
    }

    public void findViews() {
        /* Create Set Antenna Type ComboButton according to the given TV System */
        mScrollView = (CycleScrollView) mChannelActivity
                .findViewById(R.id.cyclescrollview_channel_scroll_view);
        createAntennaSwitch();
        createBandwidthSwitch();
        createViewPromptSwitch();
        text_cha_software_update_oad_val = (TextView) mChannelActivity
                .findViewById(R.id.textview_cha_software_update_oad_val);
        text_cha_time_to_search_for_ssu_val = (TextView) mChannelActivity
                .findViewById(R.id.textview_cha_time_to_search_for_ssu_val);
        linear_cha_epg = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_epg);
        linear_cha_subcode = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_subcode);
        linear_cha_autotuning = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_autotuning);
        linear_cha_dtvmanualtuning = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_dtvmanualtuning);
        linear_cha_atvmanualtuning = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_atvmanualtuning);
        linear_cha_dvbs_lnbsetting = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_dvbs_lnbsetting);
        linear_cha_programedit = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_programedit);
        linear_cha_ciinformation = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_ciinformation);
        linear_cha_cainformation = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_cainformation);
        linear_cha_signalinfo = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_signalinfo);
        linear_cha_ewbsinfo = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_emergencyInfo);
        linear_cha_softwareupdateoad = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_software_update_oad);
        linear_cha_oadtime = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_oadtime);
        linear_cha_oadscan = (LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_oadscan);
        if (false == Utility.isSupportATV()) {
            linear_cha_atvmanualtuning.setVisibility(View.GONE);
        }
        if (false == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OAD)) {
            linear_cha_oadscan.setVisibility(View.GONE);
            linear_cha_oadtime.setVisibility(View.GONE);
            linear_cha_softwareupdateoad.setVisibility(View.GONE);
        }

        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
            linear_cha_ciinformation.setVisibility(View.GONE);
            linear_cha_cainformation.setVisibility(View.GONE);
            linear_cha_softwareupdateoad.setVisibility(View.GONE);
            linear_cha_oadtime.setVisibility(View.GONE);
            linear_cha_oadscan.setVisibility(View.GONE);
            mAtscAntennaType = mTvAtscChannelManager.getDtvAntennaType();
        } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            linear_cha_ciinformation.setVisibility(View.GONE);
            linear_cha_cainformation.setVisibility(View.GONE);
            if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OAD)) {
                linear_cha_softwareupdateoad.setVisibility(View.VISIBLE);
                linear_cha_oadtime.setVisibility(View.GONE);
                linear_cha_oadscan.setVisibility(View.VISIBLE);
            }
        } else {
            linear_cha_ciinformation.setVisibility(View.VISIBLE);
            linear_cha_cainformation.setVisibility(View.VISIBLE);
            if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OAD)) {
                linear_cha_softwareupdateoad.setVisibility(View.VISIBLE);
                linear_cha_oadtime.setVisibility(View.GONE);
                linear_cha_oadscan.setVisibility(View.VISIBLE);
            }
        }

        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();

        if (true == Tools.isEwbsSupport()) {
            linear_cha_ewbsinfo.setVisibility(LinearLayout.VISIBLE);
        }

        if (currInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            enableSingleItemOrNot(linear_cha_epg, true);
            enableSingleItemOrNot(linear_cha_subcode, true);
            enableSingleItemOrNot(linear_cha_dtvmanualtuning, true);
            enableSingleItemOrNot(linear_cha_ciinformation, true);
            enableSingleItemOrNot(linear_cha_cainformation, true);

            if (0 >= TvChannelManager.getInstance().getProgramCount(
                    TvChannelManager.PROGRAM_COUNT_DTV)) {
                enableSingleItemOrNot(linear_cha_signalinfo, false);
            }
            int curRouteIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
            TvTypeInfo info = TvCommonManager.getInstance().getTvInfo();
            if ((TvChannelManager.TV_ROUTE_DVBS == info.routePath[curRouteIndex])
                    || TvChannelManager.TV_ROUTE_DVBS2 == info.routePath[curRouteIndex]) {
                linear_cha_dvbs_lnbsetting.setVisibility(View.VISIBLE);
                enableSingleItemOrNot(linear_cha_dvbs_lnbsetting, true);
            }
        } else {
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
                en = false;
            }
            enableSingleItemOrNot(linear_cha_epg, false);
            enableSingleItemOrNot(linear_cha_dtvmanualtuning, en);

            enableSingleItemOrNot(linear_cha_ciinformation, false);
            enableSingleItemOrNot(linear_cha_cainformation, false);
            enableSingleItemOrNot(linear_cha_signalinfo, false);
            enableCompositeItemOrNot(linear_cha_softwareupdateoad, false, (short) 2);
            enableCompositeItemOrNot(linear_cha_oadtime, false, (short) 2);
            enableSingleItemOrNot(linear_cha_oadscan, false);
            if ((false == Utility.isATSC()) && (false == Utility.isISDB())) {
                mComboButtonAntenna.setEnable(false);
            }
        }
        setOnClickLisenters();
        setOnFocusChangeListeners();
        setOnTouchListeners();

        mSoftwareUpdateOadTable = mChannelActivity.getResources().getStringArray(
                R.array.str_arr_software_update_oad_value);
        mSoftwareUpdateOadIndex = TvOadManager.getInstance().getSoftwareUpdateState() == 0 ? SWITCH_OFF
                : SWITCH_ON;
        text_cha_software_update_oad_val.setText(mSoftwareUpdateOadTable[mSoftwareUpdateOadIndex]);
        oadTime = TvOadManager.getInstance().getOadTime();
        if (oadTime < OAD_UPDATE_TIME_NOW) {
            text_cha_time_to_search_for_ssu_val.setText(oadTime + ":00");
        } else {
            oadTime = OAD_UPDATE_TIME_NOW;
            text_cha_time_to_search_for_ssu_val.setText("Now");
        }

        // Initiate Password Check Dialog
        int viewResId;
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            viewResId = R.layout.password_check_dialog_6_digits;
        } else {
            viewResId = R.layout.password_check_dialog_4_digits;
        }

        mPasswordLock = new PasswordCheckDialog(mChannelActivity, viewResId) {
            @Override
            public String onCheckPassword() {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    return MenuConstants.getSharedPreferencesValue(mChannelActivity, MenuConstants.VCHIPPASSWORD, MenuConstants.VCHIPPASSWORD_DEFAULTVALUE);
                } else {
                    return String.format("%04d", TvParentalControlManager.getInstance().getParentalPassword());
                }
            }

            @Override
            public void onPassWordCorrect() {
                mToast.cancel();
                mToast = Toast.makeText(mChannelActivity, mChannelActivity.getResources().getString(R.string.str_check_password_pass) , Toast.LENGTH_SHORT);
                mToast.show();
                dismiss();

                if (mIntent.getAction() != null && mIntent.getAction().length() > 0) {
                    if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                        mChannelActivity.startActivity(mIntent);
                        mChannelActivity.finish();
                    }
                }
            }

            @Override
            public void onKeyDown(int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_MENU == keyCode) {
                    cancel();
                }
            }

            @Override
            public void onShow() {
                LittleDownTimer.pauseMenu();
                View view = mChannelActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate().alpha(0f)
                            .setDuration(mChannelActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onCancel() {
                View view = mChannelActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate().alpha(1f)
                            .setDuration(mChannelActivity.getResources().getInteger(android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onDismiss() {
                LittleDownTimer.resetMenu();
                LittleDownTimer.resumeMenu();
            }
        };

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

        mComboButtonStandbyScan = new ComboButton(mChannelActivity, mChannelActivity
                .getResources().getStringArray(R.array.str_arr_active_standby_scan_option),
                R.id.linearlayout_cha_active_standby_scan, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                DbSetting dbSetting = new DbSetting();
                dbSetting.setIntData(getIdx());
                TvCommonManager.getInstance().setDatabaseSetting(dbSetting);
            }
        };
        mComboButtonStandbyScan.setVisibility(Utility.isDVBT());
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
        if (null != mComboButtonViewPrompt) {
            mComboButtonViewPrompt.setIdx((TvOadManager.getInstance().getOadViewerPrompt() == false) ? VIEW_PROMPT_SWITCH_OFF
                : VIEW_PROMPT_SWITCH_ON);
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

        mComboButtonStandbyScan.setVisibility(Utility.isDVBT());
        if (true == Utility.isDVBT()) {
            DbSetting dbSetting = TvCommonManager.getInstance().getDatabaseSetting(TvCommonManager.DATABASE_SETTING_STANDBY_SCAN);
            if (null != dbSetting) {
                mComboButtonStandbyScan.setIdx(dbSetting.getIntData());
            }
        }

        Utility.setDefaultFocus((LinearLayout) mChannelActivity.findViewById(R.id.linearlayout_channel));
    }

    public void pageOnFocus() {
        TvCommonManager.getInstance().speakTtsDelayed(
            mChannelActivity.getApplicationContext().getString(R.string.str_cha_channel)
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
        mScrollView.ttsSpeakFocusItem();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int currentid = -1;
        if (mChannelActivity.getCurrentFocus() != null) {
            currentid = mChannelActivity.getCurrentFocus().getId();
        }
        if (focusedid != currentid) {
            MainMenuActivity.selectedstatusforChannel = 0x00000000;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_cha_software_update_oad:
                        if (MainMenuActivity.selectedstatusforChannel == 0x00000010) {
                            if (mSoftwareUpdateOadIndex == SWITCH_OFF) {
                                mSoftwareUpdateOadIndex = SWITCH_ON;
                                TvOadManager.getInstance().setOadOn();
                            } else {
                                mSoftwareUpdateOadIndex = SWITCH_OFF;
                                TvOadManager.getInstance().setOadOff();
                            }
                            TvOadManager.getInstance().setSoftwareUpdateState(
                                    mSoftwareUpdateOadIndex);
                            text_cha_software_update_oad_val
                                    .setText(mSoftwareUpdateOadTable[mSoftwareUpdateOadIndex]);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_cha_oadtime:
                        if (MainMenuActivity.selectedstatusforChannel == 0x00000100) {
                            if (oadTime < OAD_UPDATE_TIME_NOW) {
                                oadTime++;
                            } else {
                                oadTime = 0;
                            }
                            TvOadManager.getInstance().setOadTime(oadTime);
                            if (oadTime == OAD_UPDATE_TIME_NOW) {
                                text_cha_time_to_search_for_ssu_val.setText("Now");
                            } else {
                                text_cha_time_to_search_for_ssu_val.setText(oadTime + ":00");
                            }
                            if (mSoftwareUpdateOadIndex == SWITCH_ON) {
                                if (oadTime != OAD_UPDATE_TIME_NOW) {
                                    Time curTime = TvTimerManager.getInstance().getCurrentTvTime();
                                    int curMinutes = curTime.hour * 60 + curTime.minute;
                                    int targetMinutes = oadTime * 60;
                                    if (targetMinutes > curMinutes) {
                                        TvOadManager.getInstance().setOadScanTime(
                                                60 * (targetMinutes - curMinutes));
                                    } else if (targetMinutes < curMinutes) {
                                        TvOadManager.getInstance().setOadScanTime(
                                                60 * (1440 + targetMinutes - curMinutes));
                                    } else {
                                        TvOadManager.getInstance().setOadScanTime(10);
                                    }
                                } else {
                                    AlertDialog.Builder build = new AlertDialog.Builder(
                                            mChannelActivity);
                                    build.setMessage(R.string.str_oad_msg_scan_confirm);
                                    build.setPositiveButton(R.string.str_oad_yes,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                        int which) {
                                                    TvOadManager.getInstance().resetOad();
                                                    mIntent.setClass(mChannelActivity, OadScan.class);
                                                    mChannelActivity.startActivity(mIntent);
                                                    mChannelActivity.finish();
                                                }
                                            });
                                    build.setNegativeButton(R.string.str_oad_no,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                        int which) {
                                                    mChannelActivity.finish();
                                                }
                                            });
                                    build.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            // TODO Auto-generated method stub
                                            mChannelActivity.finish();
                                        }
                                    });
                                    build.create().show();
                                }
                            }
                        }
                        focusedid = currentid;
                        break;

                    case R.id.linearlayout_cha_epg:
                    case R.id.linearlayout_cha_subcode:
                    case R.id.linearlayout_cha_autotuning:
                    case R.id.linearlayout_cha_dtvmanualtuning:
                    case R.id.linearlayout_cha_atvmanualtuning:
                    case R.id.linearlayout_cha_programedit:
                    case R.id.linearlayout_cha_ciinformation:
                    case R.id.linearlayout_cha_cainformation:
                    case R.id.linearlayout_cha_signalinfo:
                    case R.id.linearlayout_cha_emergencyInfo:
                    case R.id.linearlayout_cha_oadscan:
                        focusedid = currentid;
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_cha_software_update_oad:
                        if (MainMenuActivity.selectedstatusforChannel == 0x00000010) {
                            if (mSoftwareUpdateOadIndex == SWITCH_OFF) {
                                mSoftwareUpdateOadIndex = SWITCH_ON;
                                TvOadManager.getInstance().setOadOn();
                            } else {
                                mSoftwareUpdateOadIndex = SWITCH_OFF;
                                TvOadManager.getInstance().setOadOff();
                            }
                            TvOadManager.getInstance().setSoftwareUpdateState(
                                    mSoftwareUpdateOadIndex);
                            text_cha_software_update_oad_val
                                    .setText(mSoftwareUpdateOadTable[mSoftwareUpdateOadIndex]);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_cha_oadtime:
                        if (MainMenuActivity.selectedstatusforChannel == 0x00000100) {
                            if (oadTime > 0) {
                                oadTime--;
                            } else {
                                oadTime = OAD_UPDATE_TIME_NOW;
                            }
                            TvOadManager.getInstance().setOadTime(oadTime);
                            if (oadTime == OAD_UPDATE_TIME_NOW) {
                                text_cha_time_to_search_for_ssu_val.setText("Now");
                            } else {
                                text_cha_time_to_search_for_ssu_val.setText(oadTime + ":00");
                            }
                            if (mSoftwareUpdateOadIndex == SWITCH_ON) {
                                if (oadTime != OAD_UPDATE_TIME_NOW) {
                                    Time curTime = TvTimerManager.getInstance().getCurrentTvTime();
                                    int curMinutes = curTime.hour * 60 + curTime.minute;
                                    int targetMinutes = oadTime * 60;
                                    if (targetMinutes > curMinutes) {
                                        TvOadManager.getInstance().setOadScanTime(
                                                60 * (targetMinutes - curMinutes));
                                    } else if (targetMinutes < curMinutes) {
                                        TvOadManager.getInstance().setOadScanTime(
                                                60 * (1440 + targetMinutes - curMinutes));
                                    } else {
                                        TvOadManager.getInstance().setOadScanTime(10);
                                    }
                                } else {
                                    AlertDialog.Builder build = new AlertDialog.Builder(
                                            mChannelActivity);
                                    build.setMessage(R.string.str_oad_msg_scan_confirm);
                                    build.setPositiveButton(R.string.str_oad_yes,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                        int which) {
                                                    TvOadManager.getInstance().resetOad();
                                                    mIntent.setClass(mChannelActivity, OadScan.class);
                                                    mChannelActivity.startActivity(mIntent);
                                                    mChannelActivity.finish();
                                                }
                                            });
                                    build.setNegativeButton(R.string.str_oad_no,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                        int which) {
                                                    mChannelActivity.finish();
                                                }
                                            });
                                    build.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            mChannelActivity.finish();
                                        }
                                    });
                                    build.create().show();
                                }
                            }
                        }
                        focusedid = currentid;
                        break;

                    case R.id.linearlayout_cha_epg:
                    case R.id.linearlayout_cha_subcode:
                    case R.id.linearlayout_cha_autotuning:
                    case R.id.linearlayout_cha_dtvmanualtuning:
                    case R.id.linearlayout_cha_atvmanualtuning:
                    case R.id.linearlayout_cha_programedit:
                    case R.id.linearlayout_cha_ciinformation:
                    case R.id.linearlayout_cha_cainformation:
                    case R.id.linearlayout_cha_signalinfo:
                    case R.id.linearlayout_cha_emergencyInfo:
                    case R.id.linearlayout_cha_oadscan:
                        focusedid = currentid;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void closeDialogs() {
        if (null != mPasswordLock) {
            mPasswordLock.dismiss();
        }
    }

    private void setOnClickLisenters() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentid = mChannelActivity.getCurrentFocus().getId();
                if (focusedid != currentid)
                    MainMenuActivity.selectedstatusforChannel = 0x00000000;
                switch (currentid) {
                    case R.id.linearlayout_cha_epg:
                        if (SwitchPageHelper.specificSourceIsInUse(mChannelActivity, TvCommonManager.INPUT_SOURCE_DTV)) {
                           if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                               mIntent.setClass(mChannelActivity, AtscEPGActivity.class);
                           } else {
                               mIntent.setClass(mChannelActivity, EPGActivity.class);
                           }
                           mChannelActivity.startActivity(mIntent);
                           mChannelActivity.finish();
                        }
                        break;
                    case R.id.linearlayout_cha_subcode:
                        if ((SwitchPageHelper.specificSourceIsInUse(mChannelActivity, TvCommonManager.INPUT_SOURCE_DTV))
                                    || (SwitchPageHelper.specificSourceIsInUse(mChannelActivity, TvCommonManager.INPUT_SOURCE_ATV))) {
                            Intent intent = new Intent(TvIntent.CHANNEL_LIST);
                            intent.putExtra("ListId", Constant.SHOW_FAVORITE_LIST);
                            if (intent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                                mChannelActivity.startActivity(intent);
                            }
                        }
                        break;
                    case R.id.linearlayout_cha_autotuning:
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

                        mIntent.setAction(TvIntent.ACTION_AUTOTUNING_OPTION);
                        if (getSystemLock()) {
                            mPasswordLock.show();
                        } else {
                            if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                                mChannelActivity.startActivity(mIntent);
                                mChannelActivity.finish();
                            }
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
                        if (true == getSystemLock()) {
                            mPasswordLock.show();
                        } else {
                            if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                                mChannelActivity.startActivity(mIntent);
                                mChannelActivity.finish();
                            }
                        }
                        break;
                    case R.id.linearlayout_cha_atvmanualtuning:
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
                        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
                            mIntent.setAction(TvIntent.ACTION_NTSC_ATV_MANUAL_TUNING);
                        } else if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)) {
                            mIntent.setAction(TvIntent.ACTION_PAL_ATV_MANUAL_TUNING);
                        } else {
                            Log.e(TAG, "no available acvitity for ATV manual tuning!");
                            break;
                        }
                        if (getSystemLock()) {
                            mPasswordLock.show();
                        } else {
                            if (mIntent.resolveActivity(mChannelActivity.getPackageManager()) != null) {
                                mChannelActivity.startActivity(mIntent);
                                mChannelActivity.finish();
                            }
                        }
                        break;
                    case R.id.linearlayout_cha_programedit:
                        mIntent.setClass(mChannelActivity, ProgramListViewActivity.class);
                        mChannelActivity.startActivity(mIntent);
                        mChannelActivity.finish();
                        break;
                    case R.id.linearlayout_cha_signalinfo:
                        mIntent.setClass(mChannelActivity, SignalInfoActivity.class);
                        mChannelActivity.startActivity(mIntent);
                        mChannelActivity.finish();
                        break;
                    case R.id.linearlayout_cha_emergencyInfo:
                        mIntent.setClass(mChannelActivity, EWBSInfoActivity.class);
                        mChannelActivity.startActivity(mIntent);
                        mChannelActivity.finish();
                        break;
                    case R.id.linearlayout_cha_ciinformation: {
                        if (TvCiManager.getInstance() != null
                                && TvCommonManager.getInstance() != null) {
                            int currInputSource = TvCommonManager.getInstance()
                                    .getCurrentTvInputSource();
                            if (currInputSource != TvCommonManager.INPUT_SOURCE_DTV) {
                                break;
                            }

                            int status = TvCiManager.CARD_STATE_NO;
                            status = TvCiManager.getInstance().getCiCardState();

                            if (status == TvCiManager.CARD_STATE_READY) {
                                TvCiManager.getInstance().enterMenu();
                                mIntent.setClass(mChannelActivity, CimmiActivity.class);
                                mChannelActivity.startActivity(mIntent);
                                mChannelActivity.finish();
                                break;
                            }
                        }

                    }
                        break;
                    case R.id.linearlayout_cha_cainformation:
                        if (TvCommonManager.getInstance() != null) {
                            int currInputSource = TvCommonManager.getInstance()
                                    .getCurrentTvInputSource();
                            if (currInputSource != TvCommonManager.INPUT_SOURCE_DTV) {
                                break;
                            }
                            if (TvCaManager.getInstance().CaGetVer() == 0) {
                                break;
                            }
                            mIntent.setClass(mChannelActivity, CaActivity.class);
                            mChannelActivity.startActivity(mIntent);
                            mChannelActivity.finish();
                        }
                        break;
                    case R.id.linearlayout_cha_software_update_oad:
                        linear_cha_softwareupdateoad.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_cha_softwareupdateoad.getChildAt(3).setVisibility(View.VISIBLE);
                        MainMenuActivity.selectedstatusforChannel = 0x00000010;
                        focusedid = R.id.linearlayout_cha_software_update_oad;
                        break;
                    case R.id.linearlayout_cha_oadtime:
                        linear_cha_oadtime.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_cha_oadtime.getChildAt(3).setVisibility(View.VISIBLE);
                        MainMenuActivity.selectedstatusforChannel = 0x00000100;
                        focusedid = R.id.linearlayout_cha_oadtime;
                        break;
                    case R.id.linearlayout_cha_oadscan:
                        if (mSoftwareUpdateOadIndex == SWITCH_ON) {
                            mIntent.setClass(mChannelActivity, OadScan.class);
                            mChannelActivity.startActivity(mIntent);
                            mChannelActivity.finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        linear_cha_epg.setOnClickListener(listener);
        linear_cha_subcode.setOnClickListener(listener);
        linear_cha_autotuning.setOnClickListener(listener);
        linear_cha_dtvmanualtuning.setOnClickListener(listener);
        linear_cha_atvmanualtuning.setOnClickListener(listener);
        linear_cha_dvbs_lnbsetting.setOnClickListener(listener);
        linear_cha_programedit.setOnClickListener(listener);
        linear_cha_ciinformation.setOnClickListener(listener);
        linear_cha_cainformation.setOnClickListener(listener);
        linear_cha_signalinfo.setOnClickListener(listener);
        linear_cha_ewbsinfo.setOnClickListener(listener);
        linear_cha_softwareupdateoad.setOnClickListener(listener);
        linear_cha_oadtime.setOnClickListener(listener);
        linear_cha_oadscan.setOnClickListener(listener);
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener FocuschangesListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout container = (LinearLayout) v;
                container.getChildAt(0).setVisibility(View.GONE);
                container.getChildAt(3).setVisibility(View.GONE);
                MainMenuActivity.selectedstatusforChannel = 0x00000000;
            }
        };
        linear_cha_softwareupdateoad.setOnFocusChangeListener(FocuschangesListener);
        linear_cha_oadtime.setOnFocusChangeListener(FocuschangesListener);
    }

    private void setOnTouchListeners() {
        setMyOntouchListener(R.id.linearlayout_cha_software_update_oad, 0x00000010,
                linear_cha_softwareupdateoad);
        setMyOntouchListener(R.id.linearlayout_cha_oadtime, 0x00000100, linear_cha_oadtime);
    }

    private void setMyOntouchListener(final int resID, final int status, LinearLayout lay) {

        lay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP) {
                    v.requestFocus();
                    MainMenuActivity.selectedstatusforChannel = status;
                    focusedid = resID;
                    onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                }
                return true;
            }
        });
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

    private boolean getSystemLock() {
        return TvParentalControlManager.getInstance().isSystemLock();
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
        String[] mRouteTableName = Utility.getRouteTableName();

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
                int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
                if (TvCommonManager.INPUT_SOURCE_DTV == currInputSource) {
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

    /*
     * Create ComboButton for switching view prompt
     */
    private void createViewPromptSwitch() {
        int curRouteIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
        TvTypeInfo info = TvCommonManager.getInstance().getTvInfo();
        int country = TvChannelManager.getInstance().getSystemCountryId();

        if ((TvCountry.AUSTRALIA == country)
            && ((TvChannelManager.TV_ROUTE_DVBT == info.routePath[curRouteIndex]) || (TvChannelManager.TV_ROUTE_DVBT2 == info.routePath[curRouteIndex]))
            ) {
            mComboButtonViewPrompt = new ComboButton(mChannelActivity, mChannelActivity
                    .getResources().getStringArray(R.array.str_arr_oad_view_prompt_value),
                    R.id.linearlayout_cha_oad_view_prompt, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
                @Override
                public void doUpdate() {
                    TvOadManager.getInstance().setOadViewerPrompt(
                            (mComboButtonViewPrompt.getIdx() == VIEW_PROMPT_SWITCH_OFF) ? false : true);
                }
            };
            int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            if (TvCommonManager.INPUT_SOURCE_DTV == currInputSource) {
                mComboButtonViewPrompt.setEnable(true);
            } else {
                mComboButtonViewPrompt.setEnable(false);
            }
        } else {
            ((LinearLayout) mChannelActivity
                .findViewById(R.id.linearlayout_cha_oad_view_prompt))
                .setVisibility(View.GONE);
        }
    }
}
