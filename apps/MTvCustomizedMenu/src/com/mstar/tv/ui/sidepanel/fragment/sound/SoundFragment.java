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
import com.mstar.android.tv.TvCommonManager;
import com.mstar.tv.util.Tools;

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
public class SoundFragment extends SideFragment {
    private final static String TAG = "SoundFragment";

    private static Handler mHandler = new Handler();

    // Sound mode related items
    private SoundModeItem mSoundModeItem;

    private LowPitchItem mLowPitchItem;

    private HighPitchItem mHighPitchItem;

    // String arrays
    private String mStrSoundModes[];

    private String mStrSPDIFOutput[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrSoundModes = getActivity().getResources()
                .getStringArray(R.array.str_arr_sound_sound_mode_vals);
        mStrSPDIFOutput = getActivity().getResources()
                .getStringArray(R.array.str_arr_sound_SPDIF_output_vals);
    }

    /**
     * Shows the sound mode setting fragment.
     */
    public class SoundModeItem extends ActionItem {
        public final String DIALOG_TAG = SoundModeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSoundMode;

        public SoundModeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.sound_mode));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mSoundMode = TvAudioManager.getInstance().getAudioSoundMode();
                        setDescription(mStrSoundModes[mSoundMode]);
                        if (mSoundMode == TvAudioManager.SOUND_MODE_USER) {
                            SetFocusableOrNotForUserMode(true);
                        } else {
                            SetFocusableOrNotForUserMode(false);
                        }
                    }
                }
            });

        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SoundModeFragment(mSoundMode));
        }
    }

    /**
     * Shows the low pitch setting fragment.
     */
    public static class LowPitchItem extends ActionItem {
        public final static String DIALOG_TAG = LowPitchItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mLowPitch;

        public LowPitchItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.low_pitch));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mLowPitch = TvAudioManager.getInstance().getBass();
                        setDescription(Integer.toString(mLowPitch));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.low_pitch), 0, 100, mLowPitch,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvAudioManager.getInstance() != null) {
                                TvAudioManager.getInstance().setBass(value);
                                mLowPitch = value;
                                setDescription(Integer.toString(mLowPitch));
                            }

                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the high pitch setting fragment.
     */
    public static class HighPitchItem extends ActionItem {
        public final static String DIALOG_TAG = HighPitchItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mHighPitch;

        public HighPitchItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.high_pitch));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mHighPitch = TvAudioManager.getInstance().getTreble();
                        setDescription(Integer.toString(mHighPitch));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.high_pitch), 0, 100, mHighPitch,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            if (TvAudioManager.getInstance() != null) {
                                TvAudioManager.getInstance().setTreble(value);
                                mHighPitch = value;
                                setDescription(Integer.toString(mHighPitch));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the Equalizer setting fragment.
     */
    public static class EqualizerActionItem extends ActionItem {
        public final static String DIALOG_TAG = EqualizerActionItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public EqualizerActionItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.equalizer));
            mMainActivity = mainActivity;
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new EqualizerFragment());
        }
    }

    /**
     * Shows the balance setting fragment.
     */
    public static class BalanceItem extends ActionItem {
        public final static String DIALOG_TAG = BalanceItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mBalance;

        public BalanceItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.balance));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mBalance = TvAudioManager.getInstance().getBalance();
                        setDescription(Integer.toString(mBalance));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.balance), 0, 100, mBalance,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            mBalance = value;
                            if (TvAudioManager.getInstance() != null) {
                                TvAudioManager.getInstance().setBalance(mBalance);
                                setDescription(Integer.toString(mBalance));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the AVC setting fragment.
     */
    public static class AVCItem extends SwitchItem {
        public final static String DIALOG_TAG = AVCItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public AVCItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.AVC));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() == null) {
                        return;
                    }
                    // Check if AVC is enabled and then set the ui
                    if (TvAudioManager.getInstance().getAvcMode()) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvAudioManager.getInstance() == null) {
                return;
            }
            // Enable/Disable AVC
            if (isChecked()) {
                TvAudioManager.getInstance().setAvcMode(true);
            } else {
                TvAudioManager.getInstance().setAvcMode(false);
            }
        }
    }

    /**
     * Shows the AD setting fragment.
     */
    public static class ADItem extends SwitchItem {
        public final static String DIALOG_TAG = ADItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public ADItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.AD));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() == null) {
                        return;
                    }
                    // Check if AD is enabled and then set the ui
                    if (TvAudioManager.getInstance().getADEnable()) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvAudioManager.getInstance() == null) {
                return;
            }
            // Enable/Disable AD
            if (isChecked()) {
                TvAudioManager.getInstance().setADEnable(true);
            } else {
                TvAudioManager.getInstance().setADEnable(false);
            }
        }
    }

    /**
     * Shows the AD volume setting fragment.
     */
    public static class ADVolumeItem extends ActionItem {
        public final static String DIALOG_TAG = ADVolumeItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mADVolume;

        public ADVolumeItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.AD_volume));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mADVolume = TvAudioManager.getInstance().getADAbsoluteVolume();
                        setDescription(Integer.toString(mADVolume));
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            SliderDialogFragment dialog = new SliderDialogFragment(
                    mMainActivity.getString(R.string.AD_volume), 0, 100, mADVolume,
                    new SliderDialogFragment.ResultListener() {
                        @Override
                        public void done(int value) {
                            mADVolume = value;
                            if (TvAudioManager.getInstance() != null) {
                                TvAudioManager.getInstance().setADAbsoluteVolume(mADVolume);
                                setDescription(Integer.toString(mADVolume));
                            }
                        }
                    });
            dialog.show(mMainActivity.getFragmentManager(), DIALOG_TAG);
        }
    }

    /**
     * Shows the VD setting fragment.
     */
    public static class VDItem extends SwitchItem {
        public final static String DIALOG_TAG = VDItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public VDItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.video_description));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() == null) {
                        return;
                    }
                    // Check if VD is enabled and then set the ui
                    if (TvAudioManager.getInstance().getVDEnable()) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvAudioManager.getInstance() == null) {
                return;
            }
            // Enable/Disable VD
            if (isChecked()) {
                TvAudioManager.getInstance().setVDEnable(true);
            } else {
                TvAudioManager.getInstance().setVDEnable(false);
            }
        }
    }

    /**
     * Shows the Auto HOH setting fragment.
     */
    public static class Auto_HOHItem extends SwitchItem {
        public final static String DIALOG_TAG = Auto_HOHItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public Auto_HOHItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.auto_HOH));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() == null) {
                        return;
                    }
                    // Check if Auto HOH is enabled and then set the ui
                    if (TvAudioManager.getInstance().getHOHStatus()) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvAudioManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Auto HOH
            if (isChecked()) {
                TvAudioManager.getInstance().setHOHStatus(true);
            } else {
                TvAudioManager.getInstance().setHOHStatus(false);
            }
        }
    }

    /**
     * Shows the HD Bypass setting fragment.
     */
    public static class HD_BypassItem extends SwitchItem {
        public final static String DIALOG_TAG = HD_BypassItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public HD_BypassItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.HD_bypass));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() == null) {
                        return;
                    }
                    // Check if HD Bypass is enabled and then set the ui
                    if (TvAudioManager.getInstance().getHDMITx_HDByPass()) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });

        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvAudioManager.getInstance() == null) {
                return;
            }
            // Enable/Disable HD Bypass
            if (isChecked()) {
                TvAudioManager.getInstance().setHDMITx_HDByPass(true);
            } else {
                TvAudioManager.getInstance().setHDMITx_HDByPass(false);
            }
        }
    }

    /**
     * Shows the surround setting fragment.
     */
    public static class SurroundItem extends SwitchItem {
        public final static String DIALOG_TAG = SurroundItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SurroundItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.surround));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() == null) {
                        return;
                    }
                    // Check if Surround is enabled and then set the ui
                    int ret_val = TvAudioManager.getInstance().getAudioSurroundMode();
                    if (ret_val != 0) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvAudioManager.getInstance() == null) {
                return;
            }
            // Enable/Disable Surround
            if (isChecked()) {
                TvAudioManager.getInstance().setAudioSurroundMode(1);
            } else {
                TvAudioManager.getInstance().setAudioSurroundMode(0);
            }
        }
    }

    /**
     * Shows the SRS setting fragment.
     */
    public static class SRSItem extends SwitchItem {
        public final static String DIALOG_TAG = SRSItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        public SRSItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.SRS));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() == null) {
                        return;
                    }
                    // Check if SRS is enabled and then set the ui
                    if (TvAudioManager.getInstance().isSRSEnable()) {
                        setChecked(true);
                    } else {
                        setChecked(false);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            super.onSelected();
            if (TvAudioManager.getInstance() == null) {
                return;
            }
            // Enable/Disable SRS
            if (isChecked()) {
                TvAudioManager.getInstance().enableSRS(true);
            } else {
                TvAudioManager.getInstance().enableSRS(false);
            }
        }
    }

    /**
     * Shows the SPDIF output setting fragment.
     */
    public class SPDIFOutputItem extends ActionItem {
        public final String DIALOG_TAG = SPDIFOutputItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mSPDIFOutput;

        public SPDIFOutputItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.SPDIF_output));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (TvAudioManager.getInstance() != null) {
                        mSPDIFOutput = TvAudioManager.getInstance().getAudioSpdifOutMode();
                        setDescription(mStrSPDIFOutput[mSPDIFOutput]);
                    }
                }
            });
        }

        @Override
        public void onSelected() {
            mMainActivity.getSideFragmentManager().show(new SPDIFOutputFragment(mSPDIFOutput));
        }
    }

    /**
     * Shows the Alone setting fragment.
     */
    public class AloneItem extends ActionItem {
        public final String DIALOG_TAG = AloneItem.class.getSimpleName();

        private final MainActivity mMainActivity;

        private int mAlone;

        public AloneItem(MainActivity mainActivity) {
            super(mainActivity.getString(R.string.alone));
            mMainActivity = mainActivity;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Do nothing.
                }
            });
        }

        @Override
        public void onSelected() {
            Intent intent = new Intent(mMainActivity, SeperateHearActivity.class);
            mMainActivity.startActivity(intent);
            mMainActivity.finish();
        }
    }

    @Override
    protected String getTitle() {
        return getResources().getString(R.string.str_menu_sound);
    }

    @Override
    protected List<Item> getItemList() {
        mLowPitchItem = new LowPitchItem((MainActivity) getActivity());
        mHighPitchItem = new HighPitchItem((MainActivity) getActivity());
        mSoundModeItem = new SoundModeItem((MainActivity) getActivity());
        List<Item> items = new ArrayList<>();
        items.add(mSoundModeItem);
        items.add(mLowPitchItem);
        items.add(mHighPitchItem);
        items.add(new EqualizerActionItem((MainActivity) getActivity()));
        items.add(new BalanceItem((MainActivity) getActivity()));
        items.add(new AVCItem((MainActivity) getActivity()));

        if (Tools.isBox()) {
            // Hide the AD & ADVolume
        } else {

            if (true == TvCommonManager.getInstance()
                    .isSupportModule(TvCommonManager.MODULE_AD_SWITCH)) {
                items.add(new ADItem((MainActivity) getActivity()));
                items.add(new ADVolumeItem((MainActivity) getActivity()));
            }
        }

        if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
            items.add(new VDItem((MainActivity) getActivity()));
        }

        items.add(new Auto_HOHItem((MainActivity) getActivity()));

        if (TvAudioManager.getInstance().isSupportHDMITx_HDByPassMode()) {
            items.add(new HD_BypassItem((MainActivity) getActivity()));
        }

        items.add(new SurroundItem((MainActivity) getActivity()));
        items.add(new SRSItem((MainActivity) getActivity()));
        items.add(new SPDIFOutputItem((MainActivity) getActivity()));
        items.add(new AloneItem((MainActivity) getActivity()));
        return items;
    }

    private void SetFocusableOrNotForUserMode(boolean isUserMode) {
        if (isUserMode) {
            mLowPitchItem.setEnabled(true);
            mHighPitchItem.setEnabled(true);
        } else {
            mLowPitchItem.setEnabled(false);
            mHighPitchItem.setEnabled(false);
        }
    }
}
