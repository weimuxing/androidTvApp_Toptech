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

package com.mstar.tv.ui.sidepanel.fragment.hdmi;

import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.hardware.hdmi.HdmiTvClient;
import android.view.KeyEvent;
import android.content.Context;
import android.content.Intent;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;

import java.util.ArrayList;
import java.util.List;

import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvCommonManager;

import com.mstar.android.MIntent;

/**
 * Shows the Hdmi setting
 */
public class HdmiFragment extends SideFragment {
    private final static String TAG = "HdmiFragment";

    private Handler mHandler = new Handler();

    private TvMhlManager mTvMhlManager = null;

    private String mStrColorRange[];

    private String[] mEdidVersions;
    // [Menu State]
    static final int MENU_STATE_ACTIVATED = 0;
    static final int MENU_STATE_DEACTIVATED = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrColorRange = getActivity().getResources().getStringArray(
                R.array.str_arr_misc_colorrangetype);
        mEdidVersions = getActivity().getResources().getStringArray(
                R.array.str_arr_hdmi_edid_version);
    }

    /**
     * Open the HDMI CEC device menu
     */
    public class DeviceMenuItem extends ActionItem {
        public final String DIALOG_TAG = DeviceMenuItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public DeviceMenuItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_hdmi_device_menu));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            Intent intent = new Intent(MIntent.ACTION_MENU_REQUEST);
            intent.putExtra("MenuRequestType", MENU_STATE_ACTIVATED);
            mMainActivity.sendBroadcast(intent);
        }
    }

    /**
     * Shows the HDMI CEC device list
     */
    public class DeviceListItem extends ActionItem {
        public final String DIALOG_TAG = DeviceListItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public DeviceListItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_hdmi_device_list));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new DeviceListFragment());
        }
    }

    /**
     * Shows the Hdmi mhl auto switch setting
     */
    public class MhlAutoSwitchItem extends SwitchItem {
        public final String DIALOG_TAG = MhlAutoSwitchItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public MhlAutoSwitchItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_hdmi_mhl_auto_switch));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mTvMhlManager != null) {
                        boolean bIsAutoSwitch = mTvMhlManager.getAutoSwitch();
                        Log.d(TAG, "bIsAutoSwitch :" + bIsAutoSwitch);
                        if (bIsAutoSwitch) {
                            setChecked(true);
                        } else {
                            setChecked(false);
                        }
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            mTvMhlManager.setAutoSwitch(isChecked());
        }
    }

    /**
     * Set the Hdmi mhl edid version
     */
    public class HdmiEdidVersionItem extends ActionItem {
        public final String DIALOG_TAG = HdmiEdidVersionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public HdmiEdidVersionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_hdmi_edid_version));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setDescription(getEdidVersionText());
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new EdidVersionFragment());
        }
    }

    /**
     * Shows the Color Range setting fragment.
     */
    public class ColorRangeItem extends ActionItem {
        public final String DIALOG_TAG = ColorRangeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public ColorRangeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_hdmi_colorrange));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvPictureManager.getInstance() != null) {
                        /** Color range mode 0-255\16-235\auto for HDMI */
                        int ColorRangeMode = TvPictureManager.getInstance().getColorRange();
                        Log.e(TAG, "ColorRangeMode :" + ColorRangeMode);
                        if ((ColorRangeMode < 0) || (ColorRangeMode >= mStrColorRange.length)) {
                            ColorRangeMode = 0;
                            Log.d(TAG, "re-assign ColorRangeMode: " + ColorRangeMode);
                        }
                        setDescription(mStrColorRange[ColorRangeMode]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new ColorRangeFragment());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mTvMhlManager == null) {
            mTvMhlManager = TvMhlManager.getInstance();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_hdmi_title);
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();

        items.add(new DeviceMenuItem((MainActivity) getActivity()));
        items.add(new DeviceListItem((MainActivity) getActivity()));
        items.add(new MhlAutoSwitchItem((MainActivity) getActivity()));

        if ((TvCommonManager.getInstance().getCurrentTvInputSource() >= TvCommonManager.INPUT_SOURCE_HDMI)
                && (TvCommonManager.getInstance().getCurrentTvInputSource() < TvCommonManager.INPUT_SOURCE_HDMI_MAX)) {
            items.add(new ColorRangeItem((MainActivity) getActivity()));
            items.add(new HdmiEdidVersionItem((MainActivity) getActivity()));
        }

        return items;
    }

    private String getEdidVersionText() {
        int idx;
        String strText = "";

        int inputSource = TvCommonManager.getInstance().getCurrentTvInputSource();

        idx = TvCommonManager.getInstance().getHdmiEdidVersionBySource(inputSource);
        if (idx <= mEdidVersions.length) {
            strText = mEdidVersions[idx];
        }

        return strText;
    }
}
