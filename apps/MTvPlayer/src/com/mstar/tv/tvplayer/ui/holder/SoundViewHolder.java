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

package com.mstar.tv.tvplayer.ui.holder;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.CycleScrollView;
import com.mstar.tv.tvplayer.ui.component.MyButton;
import com.mstar.tv.tvplayer.ui.component.SeekBarButton;
import com.mstar.tv.tvplayer.ui.settings.EqualizerDialogActivity;
import com.mstar.tv.tvplayer.ui.settings.SeperateHearActivity;
import com.mstar.util.Tools;
import com.mstar.util.Utility;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;

public class SoundViewHolder {
    private static final String TAG = "SoundViewHolder";

    protected static final int STEP = 1;

    protected ComboButton mComboBtnSoundmode;

    protected SeekBarButton mSeekBarBtnBass;

    protected SeekBarButton mSeekBarBtnTreble;

    protected SeekBarButton mSeekBarBtnAdVolume;

    protected MyButton mBtnEqualizer;

    protected SeekBarButton mSeekBarBtnBalance;

    protected ComboButton mComboBtnAvc;

    protected ComboButton mComboBtnSurround;

    protected ComboButton mComboBtnSrs;

    protected ComboButton mComboBtnSpdifoutput;

    // protected ComboButton comboBtnSeparatehear;
    protected MyButton mBtnSeperateHear;

    protected Activity activity;

    protected TvAudioManager tvAudioManager = null;

    protected ComboButton mComboBtnVd;

    protected ComboButton mComboBtnAd;

    protected ComboButton mComboBtnAdMixerBalance;

    protected ComboButton mComboBtnHoh;

    protected ComboButton mComboBtnHdByPass;

    protected CycleScrollView mScrollView;

    private final int AD_SWITCH_RANK = 16;

    public SoundViewHolder(Activity act) {
        this.activity = act;
        tvAudioManager = TvAudioManager.getInstance();
    }

    public void findViews() {
        mScrollView = (CycleScrollView) activity
                .findViewById(R.id.cyclescrollview_sound_scroll_view);
        mSeekBarBtnBass = new SeekBarButton(activity, R.id.linearlayout_sound_bass, STEP, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setBass(mSeekBarBtnBass.getProgress());
                }
            }
        };
        mSeekBarBtnTreble = new SeekBarButton(activity, R.id.linearlayout_sound_treble, STEP, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setTreble(mSeekBarBtnTreble.getProgress());
                }
            }
        };
        mComboBtnSoundmode = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_soundmode_vals), R.id.linearlayout_sound_soundmode, 1, 2,
                true) {
            @Override
            public void doUpdate() {
                SetFocusableOrNotForUserMode();
                if (tvAudioManager != null) {
                    tvAudioManager.setAudioSoundMode(mComboBtnSoundmode.getIdx());
                }
                freshDataToUIWhenSoundModChange();
            }
        };
        SetFocusableOrNotForUserMode();
        mBtnEqualizer = new MyButton(activity, R.id.linearlayout_sound_equalizer);
        mBtnEqualizer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EqualizerDialogActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
        mSeekBarBtnBalance = new SeekBarButton(activity, R.id.linearlayout_sound_balance, STEP, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setBalance(mSeekBarBtnBalance.getProgress());
                }
            }
        };
        mComboBtnAvc = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_avc, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setAvcMode(mComboBtnAvc.getIdx() == 0 ? false : true);
                }
            }
        };
        mComboBtnVd = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_vd_vals), R.id.linearlayout_sound_vd, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setVDEnable(mComboBtnVd.getIdx() == 0 ? false : true);
                }
            }
        };
        mComboBtnAd = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_ad, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setADEnable(mComboBtnAd.getIdx() == 0 ? false : true);
                    SetFocusableOrNotForAdVolume();
                    SetFocusableOrNotForAdMixerBalance();
                }
            }
        };
        mSeekBarBtnAdVolume = new SeekBarButton(activity, R.id.linearlayout_sound_ad_volume, STEP,
                true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setADAbsoluteVolume(mSeekBarBtnAdVolume.getProgress());
                }
            }
        };
        mComboBtnAdMixerBalance = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_ad_mixerbalance_vals), R.id.linearlayout_sound_ad_mixerbalance, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setSNDAC3PInfo(TvAudioManager.AUDIO_AC3P_INFOTYPE_MIXER_BALANCE, mComboBtnAdMixerBalance.getIdx()*AD_SWITCH_RANK, 0);
                }
            }
        };

        mComboBtnHoh = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_hoh, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setHOHStatus(mComboBtnHoh.getIdx() == 0 ? false : true);
                }
            }
        };
        mComboBtnHdByPass = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_hdbypass, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager
                            .setHDMITx_HDByPass(mComboBtnHdByPass.getIdx() == 0 ? false : true);
                }
            }
        };

        mComboBtnSurround = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_surround_vals), R.id.linearlayout_sound_surround, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setAudioSurroundMode(mComboBtnSurround.getIdx());
                }
            }
        };

        mComboBtnSrs = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_surround_vals), R.id.linearlayout_sound_srs, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.enableSRS((mComboBtnSrs.getIdx() != 0));
                }
            }
        };
        mComboBtnSpdifoutput = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_spdifoutput_vals), R.id.linearlayout_sound_spdifoutput, 1, 2,
                true) {
            @Override
            public void doUpdate() {
                tvAudioManager.setAudioSpdifOutMode(mComboBtnSpdifoutput.getIdx());
            }
        };
        mBtnSeperateHear = new MyButton(activity, R.id.linearlayout_sound_separatehear);
        mBtnSeperateHear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SeperateHearActivity.class);
                activity.startActivity(intent);
                // activity.finish();
            }
        });
        if (Tools.isBox()) {
            mComboBtnAdMixerBalance.setVisibility(false);
            mBtnSeperateHear.setVisibility(View.GONE);
        }
        if (false == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_AD_SWITCH)) {
            mComboBtnAd.setVisibility(false);
            mSeekBarBtnAdVolume.setVisibility(false);
            mComboBtnAdMixerBalance.setVisibility(false);
        }
        if (Utility.isATSC()) {
            mComboBtnAd.setVisibility(false);
            mSeekBarBtnAdVolume.setVisibility(false);
            mComboBtnAdMixerBalance.setVisibility(false);
        } else {
            mComboBtnVd.setVisibility(false);
        }
        setOnClickListeners();
        setOnFocusChangeListeners();
        mComboBtnSoundmode.setFocused();
    }

    private void setOnClickListeners() {
        OnClickListener seekBarBtnOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    LinearLayout container = (LinearLayout) v;
                    container.getChildAt(2).setVisibility(View.VISIBLE);
                } else {
                    LinearLayout container = (LinearLayout) v;
                    container.getChildAt(2).setVisibility(View.GONE);
                }
            }
        };
        mSeekBarBtnBass.setOnClickListener(seekBarBtnOnClickListener);
        mSeekBarBtnTreble.setOnClickListener(seekBarBtnOnClickListener);
        mSeekBarBtnBalance.setOnClickListener(seekBarBtnOnClickListener);
        mSeekBarBtnAdVolume.setOnClickListener(seekBarBtnOnClickListener);
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener seekBarBtnFocusListenser = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout container = (LinearLayout) v;
                container.getChildAt(2).setVisibility(View.GONE);
            }
        };
        mSeekBarBtnBass.setOnFocusChangeListener(seekBarBtnFocusListenser);
        mSeekBarBtnTreble.setOnFocusChangeListener(seekBarBtnFocusListenser);
        mSeekBarBtnBalance.setOnFocusChangeListener(seekBarBtnFocusListenser);
        mSeekBarBtnAdVolume.setOnFocusChangeListener(seekBarBtnFocusListenser);
    }

    private void freshDataToUIWhenSoundModChange() {
        if (tvAudioManager != null) {
            mSeekBarBtnBass.setProgress((short) tvAudioManager.getBass());
            mSeekBarBtnTreble.setProgress((short) tvAudioManager.getTreble());
        }
    }

    public void LoadDataToUI() {
        if (tvAudioManager != null) {
            mComboBtnSoundmode.setIdx(tvAudioManager.getAudioSoundMode());
            mComboBtnAvc.setIdx(true == tvAudioManager.getAvcMode() ? 1 : 0);
            if (Utility.isATSC()) {
                mComboBtnVd.setIdx(true == tvAudioManager.getVDEnable() ? 1 : 0);
            }
            mComboBtnAd.setIdx(true == tvAudioManager.getADEnable() ? 1 : 0);
            mComboBtnAdMixerBalance.setIdx(tvAudioManager.getSNDAC3PInfo(TvAudioManager.AUDIO_AC3P_INFOTYPE_MIXER_BALANCE)/AD_SWITCH_RANK);
            mComboBtnSurround.setIdx(tvAudioManager.getAudioSurroundMode());
            mComboBtnSrs.setIdx(true == tvAudioManager.isSRSEnable() ? 1 : 0);
            mComboBtnHoh.setIdx(true == tvAudioManager.getHOHStatus() ? 1 : 0);
            mComboBtnSpdifoutput.setIdx(tvAudioManager.getAudioSpdifOutMode());
            mComboBtnHdByPass.setIdx(true == tvAudioManager.getHDMITx_HDByPass() ? 1 : 0);

            mSeekBarBtnBalance.setProgress((short) tvAudioManager.getBalance());
            mSeekBarBtnAdVolume.setProgress((short) tvAudioManager.getADAbsoluteVolume());
            mSeekBarBtnBass.setProgress((short) tvAudioManager.getBass());
            mSeekBarBtnTreble.setProgress((short) tvAudioManager.getTreble());
            if (!tvAudioManager.isSupportHDMITx_HDByPassMode()) {
                mComboBtnHdByPass.setVisibility(false);
            }
        }
        SetFocusableOrNotForAdVolume();
        SetFocusableOrNotForAdMixerBalance();
        SetFocusableOrNotForUserMode();
    }

    public void pageOnFocus() {
        TvCommonManager.getInstance().speakTtsDelayed(
            activity.getApplicationContext().getString(R.string.str_sound_soundmode)
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
        mScrollView.ttsSpeakFocusItem();
    }

    private void SetFocusableOrNotForUserMode() {
        if (TvAudioManager.SOUND_MODE_USER == mComboBtnSoundmode.getIdx()) {
            mSeekBarBtnBass.setEnable(true);
            mSeekBarBtnBass.setFocusable(true);
            mSeekBarBtnTreble.setEnable(true);
            mSeekBarBtnTreble.setFocusable(true);
        } else {
            mSeekBarBtnBass.setEnable(false);
            mSeekBarBtnBass.setFocusable(false);
            mSeekBarBtnTreble.setEnable(false);
            mSeekBarBtnTreble.setFocusable(false);
        }
    }

    private void SetFocusableOrNotForAdVolume() {
        if (TvAudioManager.ON_OFF_TYPE_OFF == mComboBtnAd.getIdx()) {
            mSeekBarBtnAdVolume.setEnable(false);
            mSeekBarBtnAdVolume.setFocusable(false);
        } else {
            mSeekBarBtnAdVolume.setEnable(true);
            mSeekBarBtnAdVolume.setFocusable(true);
        }
    }

    private void SetFocusableOrNotForAdMixerBalance() {
        if (TvAudioManager.ON_OFF_TYPE_OFF == mComboBtnAd.getIdx()) {
            mComboBtnAdMixerBalance.setEnable(false);
            mComboBtnAdMixerBalance.setFocusable(false);
        } else {
            mComboBtnAdMixerBalance.setEnable(true);
            mComboBtnAdMixerBalance.setFocusable(true);
        }
    }
}
