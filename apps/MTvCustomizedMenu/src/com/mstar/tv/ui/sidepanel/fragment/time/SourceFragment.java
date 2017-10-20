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

package com.mstar.tv.ui.sidepanel.fragment.time;

import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.os.Handler;
import android.text.format.Time;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.RadioButtonItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.vo.EnumTimeOnTimerSource;
import com.mstar.android.tvapi.common.vo.OnTimeTvDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows the Source setting
 */
public class SourceFragment extends SideFragment {
    private final static String TAG = "SourceFragment";

    private Handler mHandler = new Handler();

    private String[] mSources;

    private int mSource;

    private Time mDateTime;

    private final int TIME_ON_SOURCE_DISABLED = EnumTimeOnTimerSource.EN_Time_OnTimer_Source_NUM
            .ordinal();

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
            EnumTimeOnTimerSource.EN_Time_OnTimer_Source_RGB3.ordinal()
    // VGA3
    };

    private ArrayList<String> availableSource = new ArrayList<String>();

    private ArrayList<Integer> availableSourceIdx = new ArrayList<Integer>();

    private final String[] totalSource = {
            "VGA", "ATV", "CVBS", "CVBS2", "CVBS3", "CVBS4", "CVBS5", "CVBS6", "CVBS7", "CVBS8",
            "CVBS_MAX", "SVIDEO", "SVIDEO2", "SVIDEO3", "SVIDEO4", "SVIDEO_MAX", "YPBPR1",
            "YPBPR2", "YPBPR3", "YPBPR_MAX", "SCART", "SCART2", "SCART_MAX", "HDMI1", "HDMI2",
            "HDMI3", "HDMI4", "HDMI_MAX", "DTV", "DVI", "DVI2", "DVI3", "DVI4", "DVI_MAX",
            "STORAGE", "KTV", "JPEG", "DTV2", "STORAGE2", "DIV3", "SCALER_OP", "RUV", "VGA2",
            "VGA3"
    };

    public SourceFragment(Time mTime) {
        if (mTime != null) {
            mDateTime = mTime;
        } else {
            mDateTime = TvTimerManager.getInstance().getCurrentTvTime();
        }
    }

    /**
     * Set Source.
     */
    public class SourceItem extends RadioButtonItem {
        public final String DIALOG_TAG = SourceItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SourceItem(MainActivity mainActivity, int index) {
            super(mSources[index]);
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            setChecked(true);
            mMainActivity.getSideFragmentManager().popSideFragment();

            OnTimeTvDescriptor stEvent = TvTimerManager.getInstance().getOnTimeEvent();
            stEvent.enTVSrc = EnumTimeOnTimerSource.values()[availableSourceIdx
                    .get(getSelectedPosition())];
            TvTimerManager.getInstance().setOnTimeEvent(stEvent);
            TvTimerManager.getInstance().setTvOnTimer(mDateTime);
            TvTimerManager.getInstance().setOnTimerEnable(true);
        }
    }

    private void getCurrentAvailableSource() {
        int[] sourceList = TvCommonManager.getInstance().getSourceList();

        // Clear the available source list
        availableSource.clear();
        availableSourceIdx.clear();

        if (sourceList != null) {
            for (int i = 0; i < sourceList.length && i < totalSource.length; i++) {
                if (sourceList[i] != 0) {
                    if (TIME_ON_SOURCE_DISABLED != sourceIndex[i]) {
                        availableSource.add(totalSource[i]);
                        availableSourceIdx.add(sourceIndex[i]);
                    }
                }
            }
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_time_input_source);
    }

    @Override
    protected List<Item> getItemList() {
        getCurrentAvailableSource();
        mSources = availableSource.toArray(new String[availableSource.size()]);
        mSource = availableSourceIdx.indexOf(TvTimerManager.getInstance().getOnTimeEvent().enTVSrc
                .ordinal());

        Log.d(TAG, "enTVSrc:" + TvTimerManager.getInstance().getOnTimeEvent().enTVSrc + " mSource:"
                + mSource);

        List<Item> items = new ArrayList<>();
        for (int i = 0; i < mSources.length; i++) {
            SourceItem sourceItem = new SourceItem((MainActivity) getActivity(), i);
            items.add(sourceItem);
            if (mSource == i) {
                sourceItem.setChecked(true);
                setSelectedPosition(i);
            }
        }
        return items;
    }
}
