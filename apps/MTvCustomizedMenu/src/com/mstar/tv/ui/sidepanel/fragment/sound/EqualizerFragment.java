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

package com.mstar.tv.ui.sidepanel.fragment.sound;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import com.mstar.android.tv.TvAudioManager;

import com.mstar.tv.MainActivity;
import com.mstar.tv.R;
import com.mstar.tv.ui.dialog.SliderDialogFragment;
import com.mstar.tv.ui.sidepanel.item.Item;
import com.mstar.tv.ui.sidepanel.item.ActionItem;
import com.mstar.tv.ui.sidepanel.item.SwitchItem;
import com.mstar.tv.ui.sidepanel.fragment.SideFragment;
import com.mstar.tv.ui.activity.SeperateHearActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows version and optional license information.
 */
public class EqualizerFragment extends SideFragment {
    private final static String TAG = "EqualizerFragment";

    private static Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Shows the EQ120Hz setting fragment.
     */
    public static class EQ120HzItem extends ActionItem {
        public final static String DIALOG_TAG = EQ120HzItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mEQ120Hz;

        public EQ120HzItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.eq_120Hz));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mEQ120Hz = TvAudioManager.getInstance().getEqBand120();
                        setDescription(Integer.toString(mEQ120Hz));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.eq_120Hz), 0, 100, mEQ120Hz,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvAudioManager.getInstance() != null) {
                                mEQ120Hz = value;
                                TvAudioManager.getInstance().setEqBand120(mEQ120Hz);
                                setDescription(Integer.toString(mEQ120Hz));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the EQ500Hz setting fragment.
     */
    public static class EQ500HzItem extends ActionItem {
        public final static String DIALOG_TAG = EQ500HzItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mEQ500Hz;

        public EQ500HzItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.eq_500Hz));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mEQ500Hz = TvAudioManager.getInstance().getEqBand500();
                        setDescription(Integer.toString(mEQ500Hz));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.eq_500Hz), 0, 100, mEQ500Hz,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvAudioManager.getInstance() != null) {
                                mEQ500Hz = value;
                                TvAudioManager.getInstance().setEqBand500(mEQ500Hz);
                                setDescription(Integer.toString(mEQ500Hz));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the EQ1_5KHz setting fragment.
     */
    public static class EQ1_5KHzItem extends ActionItem {
        public final static String DIALOG_TAG = EQ1_5KHzItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mEQ1_5KHz;

        public EQ1_5KHzItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.eq_1_5KHz));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mEQ1_5KHz = TvAudioManager.getInstance().getEqBand1500();
                        setDescription(Integer.toString(mEQ1_5KHz));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.eq_1_5KHz), 0, 100, mEQ1_5KHz,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvAudioManager.getInstance() != null) {
                                mEQ1_5KHz = value;
                                TvAudioManager.getInstance().setEqBand1500(mEQ1_5KHz);
                                setDescription(Integer.toString(mEQ1_5KHz));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the EQ5KHz setting fragment.
     */
    public static class EQ5KHzItem extends ActionItem {
        public final static String DIALOG_TAG = EQ5KHzItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mEQ5KHz;

        public EQ5KHzItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.eq_5KHz));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mEQ5KHz = TvAudioManager.getInstance().getEqBand5k();
                        setDescription(Integer.toString(mEQ5KHz));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.eq_5KHz), 0, 100, mEQ5KHz,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvAudioManager.getInstance() != null) {
                                mEQ5KHz = value;
                                TvAudioManager.getInstance().setEqBand5k(mEQ5KHz);
                                setDescription(Integer.toString(mEQ5KHz));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the EQ10KHz setting fragment.
     */
    public static class EQ10KHzItem extends ActionItem {
        public final static String DIALOG_TAG = EQ10KHzItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mEQ10KHz;

        public EQ10KHzItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.eq_10KHz));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mEQ10KHz = TvAudioManager.getInstance().getEqBand10k();
                        setDescription(Integer.toString(mEQ10KHz));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.eq_10KHz), 0, 100, mEQ10KHz,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvAudioManager.getInstance() != null) {
                                mEQ10KHz = value;
                                TvAudioManager.getInstance().setEqBand10k(mEQ10KHz);
                                setDescription(Integer.toString(mEQ10KHz));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.equalizer);
    }

    @Override
    protected List<Item> getItemList() {

        List<Item> items = new ArrayList<>();
        items.add(new EQ120HzItem((MainActivity) getActivity()));
        items.add(new EQ500HzItem((MainActivity) getActivity()));
        items.add(new EQ1_5KHzItem((MainActivity) getActivity()));
        items.add(new EQ5KHzItem((MainActivity) getActivity()));
        items.add(new EQ10KHzItem((MainActivity) getActivity()));

        return items;
    }
}
