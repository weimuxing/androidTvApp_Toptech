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

package com.mstar.tv.tvplayer.ui.settings;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.EnumTimeOnTimerSource;
import com.mstar.android.tvapi.common.vo.OnTimeTvDescriptor;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.SeekBarButton;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Utility;

public class SetTimeOnDialogActivity extends MstarBaseActivity {
    private String[] hours = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23"
    };

    private String[] minutes = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43",
            "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57",
            "58", "59"
    };

    private final int TIME_ON_SOURCE_DISABLED = EnumTimeOnTimerSource.EN_Time_OnTimer_Source_NUM.ordinal();

    private ComboButton comboBtnPowerOnSwitch;

    private ComboButton comboBtnHour;

    private ComboButton comboBtnMinute;

    private ComboButton comboBtnSource;

    private ComboButton comboBtnChannel;

    private SeekBarButton seekBarBtnVolume;

    private TvTimerManager tvTimerManager = null;

    private short preDtvChannel; // for restore previous dtv channel setting

    private short preAtvChannel; // for restore previous atv channel setting

    private final int[] sourceIndex = {
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_RGB.ordinal(), // VGA
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_ATV.ordinal(),// ATV
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_AV.ordinal(),// CVBS
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_AV2.ordinal(),// CVBS2
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_AV3.ordinal(),// CVBS3
            TIME_ON_SOURCE_DISABLED,// CVBS4
            TIME_ON_SOURCE_DISABLED,// CVBS5
            TIME_ON_SOURCE_DISABLED,// CVBS6
            TIME_ON_SOURCE_DISABLED,// CVBS7
            TIME_ON_SOURCE_DISABLED,// CVBS8
            TIME_ON_SOURCE_DISABLED,// CVBS_MAX
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_SVIDEO.ordinal(),// SVIDEO
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_SVIDEO2.ordinal(),// SVIDEO2
            TIME_ON_SOURCE_DISABLED,// SVIDEO3
            TIME_ON_SOURCE_DISABLED,// SVIDEO4
            TIME_ON_SOURCE_DISABLED,// SVIDEO_MAX
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_COMPONENT.ordinal(),// YPBPR1
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_COMPONENT2.ordinal(),// YPBPR2
            TIME_ON_SOURCE_DISABLED,// YPBPR3
            TIME_ON_SOURCE_DISABLED,// YPBPR_MAX
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_SCART.ordinal(),// SCART
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_SCART2.ordinal(),// SCART2
            TIME_ON_SOURCE_DISABLED,// SCART_MAX
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_HDMI.ordinal(),// HDMI1
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_HDMI2.ordinal(),// HDMI2
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_HDMI3.ordinal(),// HDMI3
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_HDMI4.ordinal(),// HDMI4
            TIME_ON_SOURCE_DISABLED,// HDMI_MAX
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_DTV.ordinal(), // DTV
            TIME_ON_SOURCE_DISABLED, // DVI
            TIME_ON_SOURCE_DISABLED, // DVI2
            TIME_ON_SOURCE_DISABLED, // DVI3
            TIME_ON_SOURCE_DISABLED, // DVI4
            TIME_ON_SOURCE_DISABLED, // DVI_MAX
            TIME_ON_SOURCE_DISABLED, // STORAGE
            TIME_ON_SOURCE_DISABLED, // KTV
            TIME_ON_SOURCE_DISABLED, // JPEG
            TIME_ON_SOURCE_DISABLED, // DTV2
            TIME_ON_SOURCE_DISABLED, // STORAGE2
            TIME_ON_SOURCE_DISABLED, // DIV3
            TIME_ON_SOURCE_DISABLED, // SCALER_OP
            TIME_ON_SOURCE_DISABLED, // RUV
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_RGB2.ordinal(), // VGA2
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_RGB3.ordinal()  // VGA3
    };

    private ArrayList<String> availableSource = new ArrayList<String>();

    private ArrayList<Integer> availableSourceIdx = new ArrayList<Integer>();

    private boolean enableOnTimer = false;

    private final static int SET_ON_TIME = 0x22;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == SET_ON_TIME) {
                tvTimerManager.setOnTimerEnable(true);
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_on);
        TVRootApp app = (TVRootApp) getApplication();
        tvTimerManager = TvTimerManager.getInstance();
        getCurrentAvailableSource(app);
        findViews();
        loadDataToUI();
    }

    private void getCurrentAvailableSource(TVRootApp app) {
        final int[] sourceList = TvCommonManager.getInstance().getSourceList();
        final String[] totalSource = getResources().getStringArray(R.array.str_arr_input_sources);
        availableSource.clear();

        if (sourceList != null) {
            for (int i = 0; i < sourceList.length && i < totalSource.length; i++) {
                if (sourceList[i] != 0) {
                    if (TIME_ON_SOURCE_DISABLED != sourceIndex[i]) {
                        String srcString = totalSource[i];
                        if ((TvCommonManager.INPUT_SOURCE_HDMI <= i)
                            && (TvCommonManager.INPUT_SOURCE_HDMI4 >= i)) {
                            srcString = Utility.getHdmiDispStr(i);
                        }
                        availableSource.add(srcString);
                        availableSourceIdx.add(sourceIndex[i]);
                    }
                }
            }
        }
    }

    private void updateSetTimeOnChannelInfo() {
        OnTimeTvDescriptor stEvent;
        stEvent = tvTimerManager.getOnTimeEvent();
        stEvent.enTVSrc = EnumTimeOnTimerSource.values()[availableSourceIdx.get(comboBtnSource
                .getIdx())];

        if (availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_DTV
                .ordinal()
                || availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_ATV
                        .ordinal()) {
            setChannelEnable(true);
            if (availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_DTV
                    .ordinal()) {
                comboBtnChannel.setIdx(preDtvChannel);
            } else if (availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_ATV
                    .ordinal()) {
                comboBtnChannel.setIdx(preAtvChannel);
            }
            stEvent.mChNo = 0xFFFF;
        } else {
            setChannelEnable(false);
            comboBtnChannel.setIdx(0X00);
        }
        tvTimerManager.setOnTimeEvent(stEvent);
        enableOnTimer = true;
    }

    private void findViews() {
        comboBtnPowerOnSwitch = new ComboButton(this, getResources().getStringArray(
                R.array.str_arr_time_switch), R.id.linearlayout_time_power_on_switch, 1, 2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                enableOnTimer = comboBtnPowerOnSwitch.getIdx() == 0 ? false : true;
                setBelowEnable(enableOnTimer);
                tvTimerManager.setOnTimerEnable(enableOnTimer);
                if (enableOnTimer) {
                    updateSetTimeOnChannelInfo();
                }
            }
        };
        comboBtnHour = new ComboButton(this, hours, R.id.linearlayout_time_power_on_hour, 1, 2,
                ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                Time dateTime;
                dateTime = tvTimerManager.getTvOnTimer();
                dateTime.hour = (byte) (comboBtnHour.getIdx());
                tvTimerManager.setTvOnTimer(dateTime);
                enableOnTimer = true;
                handler.sendEmptyMessageDelayed(SET_ON_TIME, 500);
                updateSetTimeOnChannelInfo();
            }
        };
        comboBtnMinute = new ComboButton(this, minutes, R.id.linearlayout_time_power_on_minute, 1,
                2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                Time dateTime;
                Time curDateTime;

                curDateTime = tvTimerManager.getCurrentTvTime();
                dateTime = tvTimerManager.getTvOnTimer();
                dateTime.minute = (byte) (comboBtnMinute.getIdx());
                tvTimerManager.setTvOnTimer(dateTime);
                enableOnTimer = true;
                updateSetTimeOnChannelInfo();
                if ((dateTime.toMillis(true) - curDateTime.toMillis(true)) > 1000 * 60) {
                    handler.sendEmptyMessageDelayed(SET_ON_TIME, 500);

                }
            }
        };
        comboBtnSource = new ComboButton(this,
                availableSource.toArray(new String[availableSource.size()]),
                R.id.linearlayout_time_power_on_source, 1, 2, ComboButton.DIRECT_SWITCH) {

            @Override
            public void doUpdate() {
                updateSetTimeOnChannelInfo();
            }
        };
        comboBtnChannel = new ComboButton(this, null, R.id.linearlayout_time_power_on_channel, 1,
                2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                if (availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_DTV
                        .ordinal()) {
                    if (comboBtnChannel.getIdx() <= 0X00) {
                        comboBtnChannel.setIdx(0X3E7);
                    }
                    if (comboBtnChannel.getIdx() > 0X3E7) {
                        comboBtnChannel.setIdx(0X00);
                    }
                    // keep the channel value in a variable so that even
                    // continuous swith source,
                    // the previous channel no could be keep.
                    preDtvChannel = comboBtnChannel.getIdx();
                } else if (availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_ATV
                        .ordinal()) {
                    if (comboBtnChannel.getIdx() <= 0X00) {
                        comboBtnChannel.setIdx(0XFF);
                    }
                    if (comboBtnChannel.getIdx() > 0XFF) {
                        comboBtnChannel.setIdx(0X00);
                    }
                    preAtvChannel = comboBtnChannel.getIdx();
                } else {
                    comboBtnChannel.setIdx(0X00);
                }
                OnTimeTvDescriptor stEvent;
                stEvent = tvTimerManager.getOnTimeEvent();
                // stEvent.mChNo = comboBtnChannel.getIdx();
                stEvent.mChNo = 0xFFFF;
                tvTimerManager.setOnTimeEvent(stEvent);
            }
        };
        comboBtnChannel.setVisibility(false);
        seekBarBtnVolume = new SeekBarButton(this, R.id.linearlayout_time_power_on_vol, 1, false) {
            @Override
            public void doUpdate() {
                OnTimeTvDescriptor stEvent;
                stEvent = tvTimerManager.getOnTimeEvent();
                stEvent.mVol = seekBarBtnVolume.getProgress();
                tvTimerManager.setOnTimeEvent(stEvent);
            }
        };
        seekBarBtnVolume.setVisibility(false);
        setOnFocusChangeListeners();
        comboBtnPowerOnSwitch.setFocused();
    }

    @Override
    protected void onResume() {
        LittleDownTimer.resumeMenu();
        super.onResume();
    }

    @Override
    public void onUserInteraction() {
        LittleDownTimer.resetMenu();
        super.onUserInteraction();
    }

    @Override
    protected void onPause() {
        tvTimerManager.setOnTimerEnable(enableOnTimer);
        LittleDownTimer.pauseMenu();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        tvTimerManager.setOnTimerEnable(enableOnTimer);
        Intent intent = new Intent(TvIntent.MAINMENU);
        intent.putExtra("currentPage", MainMenuActivity.TIME_PAGE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_PROG_RED) {
            // red key
            tvTimerManager.setOnTimerEnable(enableOnTimer);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadDataToUI() {
        enableOnTimer = tvTimerManager.isOnTimerEnable();

        if (enableOnTimer == false) {
            Time curTime = tvTimerManager.getCurrentTvTime();
            Time dateTime = null;
            dateTime = tvTimerManager.getTvOnTimer();
            dateTime.year = curTime.year;
            dateTime.month = curTime.month + 1;
            dateTime.monthDay = curTime.monthDay;
            dateTime.hour = curTime.hour;
            dateTime.minute = curTime.minute;
            comboBtnHour.setIdx(dateTime.hour);
            comboBtnMinute.setIdx(dateTime.minute);

            tvTimerManager.setTvOnTimer(dateTime);
        }

        comboBtnPowerOnSwitch.setIdx(enableOnTimer ? 1 : 0);
        comboBtnHour.setIdx(tvTimerManager.getTvOnTimer().hour);
        int minute = tvTimerManager.getTvOnTimer().minute;
        comboBtnMinute.setIdx(minute);
        comboBtnMinute.setTextInChild(2, minute + "");
        comboBtnSource.setIdx(availableSourceIdx.indexOf(tvTimerManager.getOnTimeEvent().enTVSrc
                .ordinal()));
        seekBarBtnVolume.setProgress(tvTimerManager.getOnTimeEvent().mVol);
        setBelowEnable(enableOnTimer);
    }

    private void setBelowEnable(boolean b) {
        boolean channleEnableFlag = b;
        comboBtnHour.setEnable(b);
        comboBtnMinute.setEnable(b);
        comboBtnSource.setEnable(b);
        if (channleEnableFlag
                && (availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_DTV
                        .ordinal() || availableSourceIdx.get(comboBtnSource.getIdx()) == EnumTimeOnTimerSource.EN_Time_OnTimer_Source_ATV
                        .ordinal())) {
        } else {
            channleEnableFlag = false;
        }
        comboBtnChannel.setEnable(channleEnableFlag);
        seekBarBtnVolume.setFocusable(b);
        int textColor = b ? getResources().getColor(R.color.enable_text_color) : getResources()
                .getColor(R.color.disable_text_color);
        int textChannelColor = channleEnableFlag ? getResources().getColor(
                R.color.enable_text_color) : getResources().getColor(R.color.disable_text_color);
        ((TextView) comboBtnHour.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) comboBtnHour.getLayout().getChildAt(2)).setTextColor(textColor);
        ((TextView) comboBtnMinute.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) comboBtnMinute.getLayout().getChildAt(2)).setTextColor(textColor);
        ((TextView) comboBtnSource.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) comboBtnSource.getLayout().getChildAt(2)).setTextColor(textColor);
        ((TextView) comboBtnChannel.getLayout().getChildAt(1)).setTextColor(textChannelColor);
        ((TextView) comboBtnChannel.getLayout().getChildAt(2)).setTextColor(textChannelColor);
        ((TextView) seekBarBtnVolume.getLayout().getChildAt(0)).setTextColor(textColor);
        ((TextView) seekBarBtnVolume.getLayout().getChildAt(1)).setTextColor(textColor);
    }

    private void setChannelEnable(boolean bool) {
        comboBtnChannel.setEnable(bool);
        int textColor = bool ? getResources().getColor(R.color.enable_text_color) : getResources()
                .getColor(R.color.disable_text_color);
        ((TextView) comboBtnChannel.getLayout().getChildAt(1)).setTextColor(textColor);
        ((TextView) comboBtnChannel.getLayout().getChildAt(2)).setTextColor(textColor);
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener seekBarBtnFocusListenser = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LinearLayout container = (LinearLayout) v;
                    container.getChildAt(2).setVisibility(View.VISIBLE);
                } else {
                    LinearLayout container = (LinearLayout) v;
                    container.getChildAt(2).setVisibility(View.GONE);
                }
            }
        };
        seekBarBtnVolume.setOnFocusChangeListener(seekBarBtnFocusListenser);
        OnFocusChangeListener focusListenser = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout container = (LinearLayout) v;
                if (hasFocus) {
                    Utility.ttsSepakLinearLayout(container);
                }
            }
        };
        LinearLayout ll;
        ll = (LinearLayout) findViewById(R.id.linearlayout_time_power_on_switch);
        ll.setOnFocusChangeListener(focusListenser);
        ll = (LinearLayout) findViewById(R.id.linearlayout_time_power_on_hour);
        ll.setOnFocusChangeListener(focusListenser);
        ll = (LinearLayout) findViewById(R.id.linearlayout_time_power_on_minute);
        ll.setOnFocusChangeListener(focusListenser);
        ll = (LinearLayout) findViewById(R.id.linearlayout_time_power_on_source);
        ll.setOnFocusChangeListener(focusListenser);
    }
}
