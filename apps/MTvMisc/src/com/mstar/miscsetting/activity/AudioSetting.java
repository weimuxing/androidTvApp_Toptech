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

package com.mstar.miscsetting.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.View;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tvapi.common.vo.EnumSoundMode;
import com.mstar.android.tvapi.common.vo.EnumSpdifType;
import com.mstar.miscsetting.R;

public class AudioSetting extends BaseActivity {
    private final String TAG = "AudioSetting";

    private final int STEP = 1;

    private static int currentVolume = 0;

    private final int UPDATE_UI_ON_MUTE_CHANGED = 1000;

    private BroadcastReceiver mVolumeReceiver = null;

    private AudioManager audiomanager;

    protected ComboButton comboBtnAudioMode;

    protected ComboButton comboBtnSPIDFMode;

    protected SeekBarButton seekBarVolume;

    protected SeekBarButton seekBarBass;

    protected SeekBarButton seekBarTreble;

    protected SeekBarButton seekBarBalance;

    int volumeType = AudioManager.STREAM_SYSTEM;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_UI_ON_MUTE_CHANGED) {
                currentVolume = audiomanager.getStreamVolume(volumeType);
                seekBarVolume.setProgress((short) currentVolume);
            }
        }
    };

    private ContentObserver mVolumeObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mHandler.sendEmptyMessage(UPDATE_UI_ON_MUTE_CHANGED);
        }
    };

    private class UpdateUIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            seekBarBass.setProgress((short) tvAudioManager.getBass());
            seekBarTreble.setProgress((short) tvAudioManager.getTreble());
            freshUISoundMode();
            super.handleMessage(msg);
        }
    }

    private UpdateUIHandler updateUIHandler = null;

    private TvAudioManager tvAudioManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvAudioManager = TvAudioManager.getInstance();
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (VERSION.SDK_INT >= TvCommonManager.API_LEVEL_M) {
            volumeType = AudioManager.STREAM_MUSIC;
        }
        currentVolume = audiomanager.getStreamVolume(volumeType);
        updateUIHandler = new UpdateUIHandler();
        setContentView(R.layout.audiosetting);
        findViews();

        if (VERSION.SDK_INT >= TvCommonManager.API_LEVEL_M) {
            mVolumeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if ("android.media.STREAM_MUTE_CHANGED_ACTION".equals(action)) {
                        int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                        if (streamType != AudioManager.STREAM_MUSIC) {
                            return;
                        }
                        mHandler.sendEmptyMessage(UPDATE_UI_ON_MUTE_CHANGED);
                    }
                }
            };
            final IntentFilter audFilter = new IntentFilter();
            audFilter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
            registerReceiver(mVolumeReceiver, audFilter);
        } else {
            /* Redefine VOLUME_MASTER_MUTE to prevent AN N building code fail. */
            final String VOLUME_MASTER_MUTE = "volume_master_mute";
            Uri uri = Settings.System.getUriFor(VOLUME_MASTER_MUTE);
            getContentResolver().registerContentObserver(uri, true, mVolumeObserver);
        }
    }

    @Override
    public void onDestroy() {
        if (VERSION.SDK_INT >= TvCommonManager.API_LEVEL_M) {
            if (null != mVolumeReceiver) {
                unregisterReceiver(mVolumeReceiver);
                mVolumeReceiver = null;
            }
        } else {
            getContentResolver().unregisterContentObserver(mVolumeObserver);
        }
        super.onDestroy();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout_audio_root);
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.x = 0;
        lp.y = 0;
        lp.width = ll.getLayoutParams().width;
        lp.height = ll.getLayoutParams().height;
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            View view = getCurrentFocus();
            if (tvAudioManager.getAudioSoundMode() != TvAudioManager.SOUND_MODE_USER) {
                if (view != null) {
                    if (view.getId() == R.id.linearlayout_audio_mode) {
                        seekBarBalance.requestFocus();
                        return true;
                    }
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            View view = getCurrentFocus();
            if (tvAudioManager.getAudioSoundMode() != TvAudioManager.SOUND_MODE_USER) {
                if (view != null) {
                    if (view.getId() == R.id.linearlayout_audio_balance) {
                        comboBtnAudioMode.requestFocus();
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        seekBarBalance.textViewProgress.setText("" + (tvAudioManager.getBalance()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent("miscSoundDirty");
        sendBroadcast(intent);
    }

    public void findViews() {
        seekBarVolume = new SeekBarButton(this, R.id.linearlayout_audio_volume, STEP, false) {
            @Override
            public void doUpdate() {
                if (audiomanager != null) {
                    int vol = seekBarVolume.getProgress();
                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
                    currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    Log.d(TAG, "currentVolume = " + currentVolume);
                }
            }

            @Override
            public void increaseProgress() {
                refreshUIVolume();
                super.increaseProgress();
            }

            @Override
            public void decreaseProgress() {
                refreshUIVolume();
                super.decreaseProgress();
            }
        };
        seekBarVolume.setProgress((short) currentVolume);

        comboBtnAudioMode = new ComboButton(this, this.getResources().getStringArray(
                R.array.str_arr_audio_audiomode_vals), R.id.linearlayout_audio_mode, false) {
            @Override
            public void doUpdate() {
                tvAudioManager.setAudioSoundMode(comboBtnAudioMode.getIdx());
                freshDataToUIWhenSoundModChange();
            }
        };
        comboBtnAudioMode.setIdx(tvAudioManager.getAudioSoundMode());

        seekBarBass = new SeekBarButton(this, R.id.linearlayout_audio_bass, STEP, false) {
            @Override
            public void doUpdate() {
                tvAudioManager.setBass(seekBarBass.getProgress());
            }
        };
        seekBarBass.setProgress((short) tvAudioManager.getBass());

        seekBarTreble = new SeekBarButton(this, R.id.linearlayout_audio_treble, STEP, false) {
            @Override
            public void doUpdate() {
                tvAudioManager.setTreble(seekBarTreble.getProgress());
            }
        };
        seekBarTreble.setProgress((short) tvAudioManager.getTreble());

        seekBarBalance = new SeekBarButton(this, R.id.linearlayout_audio_balance, STEP, false) {
            @Override
            public void doUpdate() {
                seekBarBalance.textViewProgress.setText("" + (seekBarBalance.getProgress()));
                tvAudioManager.setBalance(seekBarBalance.getProgress());
            }
        };
        seekBarBalance.setProgress((short) tvAudioManager.getBalance());

        comboBtnSPIDFMode = new ComboButton(this, this.getResources().getStringArray(
                R.array.str_arr_spdif_mode_vals), R.id.linearlayout_audio_spdif_mode, false) {
            @Override
            public void doUpdate() {
                tvAudioManager.setAudioSpdifOutMode(comboBtnSPIDFMode.getIdx());
            }
        };
        comboBtnSPIDFMode.setIdx(tvAudioManager.getAudioSpdifOutMode());
        seekBarVolume.setFocused();

        freshUISoundMode();
    }

    private void freshDataToUIWhenSoundModChange() {
        new Thread(new Runnable() {
            // need sometimes to setSoundMode
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateUIHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void freshUISoundMode() {
        final boolean enable = (tvAudioManager.getAudioSoundMode() == TvAudioManager.SOUND_MODE_USER);
        seekBarBass.setFocusable(enable);
        seekBarTreble.setFocusable(enable);
    }

    private void refreshUIVolume() {
        if (true == audiomanager.isMasterMute()) {
            audiomanager.setMasterMute(false, AudioManager.FLAG_SHOW_UI);
            currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
            seekBarVolume.setProgress((short) currentVolume);
        }
    }
}
