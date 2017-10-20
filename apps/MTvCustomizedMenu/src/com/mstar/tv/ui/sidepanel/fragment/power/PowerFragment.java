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

package com.mstar.tv.ui.sidepanel.fragment.power;

import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.os.Handler;
import android.os.Bundle;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;

import java.util.ArrayList;
import java.util.List;

import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.CheckBoxItem;
import com.mstar.tv.ui.sidepanel.item.CompoundButtonItem;
import com.mstar.tv.ui.sidepanel.item.DividerItem;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.RadioButtonItem;
import com.mstar.tv.ui.sidepanel.item.SubMenuItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.tv.ui.dialog.SliderDialogFragment;
import com.mstar.android.tv.TvFactoryManager;

/**
 * Shows the power setting
 */
public class PowerFragment extends SideFragment {
    private final static String TAG = "PowerFragment";

    private final int TYPE_POWER_MUSIC = 0;

    private final int TYPE_POWER_LOGO = 1;

    private Handler mHandler = new Handler();

    private String[] mPowerMusics;

    private String[] mPowerLogos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPowerMusics = getActivity().getResources()
                .getStringArray(R.array.str_arr_power_powermusic);
        mPowerLogos = getActivity().getResources().getStringArray(R.array.str_arr_power_powerlogo);
    }

    /**
     * Shows the power music setting
     */
    public class PowerMusicItem extends ActionItem {
        public final String DIALOG_TAG = PowerMusicItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public PowerMusicItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_power_powermusic));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setDescription(getPowerText(TYPE_POWER_MUSIC));
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new PowerMusicFragment());
        }
    }

    /**
     * Shows the power logo setting
     */
    public class PowerLogoItem extends ActionItem {
        public final String DIALOG_TAG = PowerLogoItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public PowerLogoItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_power_powerlogo));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setDescription(getPowerText(TYPE_POWER_LOGO));
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new PowerLogoFragment());
        }
    }

    /**
     * Shows the power volume setting
     */
    public class PowerVolumeItem extends ActionItem {
        public final String DIALOG_TAG = PowerVolumeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mPowerVolume;

        private String mDescription;

        public PowerVolumeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.str_power_powervolume));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvFactoryManager.getInstance() != null) {
                        mPowerVolume = TvFactoryManager.getInstance()
                                .getEnvironmentPowerOnMusicVolume();
                        setDescription(Integer.toString(mPowerVolume));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.str_power_powervolume), 0, 100, mPowerVolume,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            mPowerVolume = value;
                            Log.d(TAG, "power volume :" + mPowerVolume);
                            if (TvFactoryManager.getInstance() != null) {
                                TvFactoryManager.getInstance().setEnvironmentPowerOnMusicVolume(
                                        mPowerVolume);
                                setDescription(Integer.toString(mPowerVolume));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_power_title);
    }

    @Override
    protected List<Item> getItemList() {
        List<Item> items = new ArrayList<>();
        items.add(new PowerMusicItem((MainActivity) getActivity()));
        items.add(new PowerLogoItem((MainActivity) getActivity()));
        items.add(new PowerVolumeItem((MainActivity) getActivity()));
        return items;
    }

    private String getPowerText(int type) {
        int index;
        String strText = "";
        switch (type) {
            case TYPE_POWER_MUSIC:
                index = TvFactoryManager.getInstance().getPowerOnMusicMode();
                strText = mPowerMusics[index];
                break;
            case TYPE_POWER_LOGO:
                index = TvFactoryManager.getInstance().getPowerOnLogoMode();
                strText = mPowerLogos[index];
                break;
            default:
                break;
        }
        return strText;
    }
}
