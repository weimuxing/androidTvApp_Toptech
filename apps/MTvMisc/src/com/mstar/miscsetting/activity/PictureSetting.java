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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.View;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.miscsetting.R;

public class PictureSetting extends BaseActivity {
    private static final int STEP = 1;

    protected ComboButton comboBtnPictureMode;

    protected SeekBarButton seekBarBrightness;

    protected SeekBarButton seekBarContrast;

    protected SeekBarButton seekBarBacklight;

    protected SeekBarButton seekBarSharpness;

    protected SeekBarButton seekBarHue;

    protected SeekBarButton seekBarSaturation;

    private TvPictureManager tvPictureManager = null;

    private TvCommonManager mTvCommonManager = null;

    private class UpdateUIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            freshDataToUIWhenPicModChange();
            freshUIPictureMode();
            super.handleMessage(msg);
        }
    }

    private UpdateUIHandler updateUIHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvPictureManager = TvPictureManager.getInstance();
        mTvCommonManager = TvCommonManager.getInstance();
        updateUIHandler = new UpdateUIHandler();
        setContentView(R.layout.picturesetting);
        findViews();

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout_picture_root);
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
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent("miscPictureDirty");
        sendBroadcast(intent);
    }

    public void findViews() {
        comboBtnPictureMode = new ComboButton(this, this.getResources().getStringArray(
                R.array.str_arr_pic_mode_vals), R.id.linearlayout_pic_mode, false) {
            @Override
            public void doUpdate() {
                tvPictureManager.setPictureMode(comboBtnPictureMode.getIdx());
                delayToUpdateUi();
            }
        };
        int inputSrc = mTvCommonManager.getCurrentTvInputSource();
        if ((inputSrc == TvCommonManager.INPUT_SOURCE_VGA)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA2)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA3)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI2)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI3)
                || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI4)) {
            /*
             * PICTURE_MODE_AUTO and PICTURE_MODE_PC are only
             * supported in HDMI and VGA source.
             */
            comboBtnPictureMode.setItemEnable(
                    TvPictureManager.PICTURE_MODE_AUTO, true);
            comboBtnPictureMode.setItemEnable(
                    TvPictureManager.PICTURE_MODE_PC, true);
        } else {
            comboBtnPictureMode.setItemEnable(
                    TvPictureManager.PICTURE_MODE_AUTO, false);
            comboBtnPictureMode.setItemEnable(
                    TvPictureManager.PICTURE_MODE_PC, false);
        }
        comboBtnPictureMode.setIdx(tvPictureManager.getPictureMode());

        seekBarBrightness = new SeekBarButton(this, R.id.linearlayout_pic_brightness, STEP, false) {
            @Override
            public void doUpdate() {
                tvPictureManager.setVideoItem(TvPictureManager.PICTURE_BRIGHTNESS,
                        seekBarBrightness.getProgress());
            }
        };
        seekBarBrightness.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_BRIGHTNESS));

        seekBarContrast = new SeekBarButton(this, R.id.linearlayout_pic_contrast, STEP, false) {
            @Override
            public void doUpdate() {
                tvPictureManager.setVideoItem(TvPictureManager.PICTURE_CONTRAST,
                        seekBarContrast.getProgress());
            }
        };
        seekBarContrast.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_CONTRAST));

        seekBarHue = new SeekBarButton(this, R.id.linearlayout_pic_hue, STEP, false) {
            @Override
            public void doUpdate() {
                tvPictureManager.setVideoItem(TvPictureManager.PICTURE_HUE,
                        seekBarHue.getProgress());
            }
        };
        seekBarHue.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_HUE));

        seekBarSharpness = new SeekBarButton(this, R.id.linearlayout_pic_sharpness, STEP, false) {
            @Override
            public void doUpdate() {
                tvPictureManager.setVideoItem(TvPictureManager.PICTURE_SHARPNESS,
                        seekBarSharpness.getProgress());
            }
        };
        seekBarSharpness.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_SHARPNESS));

        seekBarSaturation = new SeekBarButton(this, R.id.linearlayout_pic_saturation, STEP, false) {
            @Override
            public void doUpdate() {
                tvPictureManager.setVideoItem(TvPictureManager.PICTURE_SATURATION,
                        seekBarSaturation.getProgress());
            }
        };
        seekBarSaturation.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_SATURATION));

        seekBarBacklight = new SeekBarButton(this, R.id.linearlayout_pic_backlight, STEP, false) {
            @Override
            public void doUpdate() {
                tvPictureManager.setBacklight(seekBarBacklight.getProgress());
            }
        };
        seekBarBacklight.setProgress((short) tvPictureManager.getBacklight());

        comboBtnPictureMode.setFocused();

        freshUIPictureMode();
    }

    private void delayToUpdateUi() {
        new Thread(new Runnable() {
            // need sometimes to setPictureMode
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                updateUIHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void freshDataToUIWhenPicModChange() {
        seekBarSharpness.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_SHARPNESS));
        seekBarBrightness.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_BRIGHTNESS));
        seekBarHue.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_HUE));
        seekBarContrast.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_CONTRAST));
        seekBarSaturation.setProgress((short) tvPictureManager
                .getVideoItem(TvPictureManager.PICTURE_SATURATION));
        seekBarBacklight.setProgress((short) tvPictureManager.getBacklight());
    }

    private void freshUIPictureMode() {
        final boolean enable = (tvPictureManager.getPictureMode() == TvPictureManager.PICTURE_MODE_USER);
        seekBarSharpness.setFocusable(enable);
        seekBarBrightness.setFocusable(enable);
        seekBarHue.setFocusable(enable);
        seekBarContrast.setFocusable(enable);
        seekBarSaturation.setFocusable(enable);
        seekBarBacklight.setFocusable(enable);
    }
}
