//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.miscsetting.activity.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.util.Log;
import android.widget.LinearLayout;

import com.mstar.miscsetting.activity.SeekBarButton;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.miscsetting.R;
import com.mstar.miscsetting.activity.display.util.TvDispAreaUtil;
import com.mstar.miscsetting.activity.display.vo.ReproduceRate;

public class DisplayAreaActivity extends Activity {
    private static final String TAG = "DisplayAreaActivity";

    private static final int STEP = 1;

    private static final int MAXVALUE = 100;

    private static final int REPRODUCE_ADJUST_TOP = 1701;

    private static final int REPRODUCE_ADJUST_BUTTOM = 1702;

    private static final int REPRODUCE_ADJUST_LEFT = 1703;

    private static final int REPRODUCE_ADJUST_RIGHT = 1704;

    private TvDispAreaUtil mTvDispAreaUtil;

    protected SeekBarButton mSeekBarTop;

    protected SeekBarButton mSeekBarBottom;

    protected SeekBarButton mSeekBarLeft;

    protected SeekBarButton mSeekBarRight;

    protected LinearLayout mRestoreToDefault;

    private TvPictureManager mTvPictureManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvDispAreaUtil = new TvDispAreaUtil();
        mTvPictureManager = TvPictureManager.getInstance();
        setContentView(R.layout.displayarea);
        findViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent("miscPictureDirty");
        sendBroadcast(intent);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (getCurrentFocus().equals((Object)mRestoreToDefault)) {
                mTvDispAreaUtil.setReproduceRateDefault();
                refreshUI();
            }
        }
        return super.onKeyUp(keyCode, event);
    };

    private void findViews() {

        mSeekBarTop = new SeekBarButton(this, R.id.linearlayout_disp_top, STEP, false) {
            @Override
            public void doUpdate() {
                mTvDispAreaUtil.setReproduceRateTop(mSeekBarTop.getProgress());
            }
        };

        mSeekBarBottom = new SeekBarButton(this, R.id.linearlayout_disp_bottom, STEP, false) {
            @Override
            public void doUpdate() {
                mTvDispAreaUtil.setReproduceRateBottom(mSeekBarBottom.getProgress());
            }
        };

        mSeekBarLeft = new SeekBarButton(this, R.id.linearlayout_disp_left, STEP, false) {
            @Override
            public void doUpdate() {
                mTvDispAreaUtil.setReproduceRateLeft(mSeekBarLeft.getProgress());
            }
        };

        mSeekBarRight = new SeekBarButton(this, R.id.linearlayout_disp_right, STEP, false) {
            @Override
            public void doUpdate() {
                mTvDispAreaUtil.setReproduceRateRight(mSeekBarRight.getProgress());
            }
        };

        mRestoreToDefault = (LinearLayout)findViewById(R.id.linearlayout_disp_default);

        refreshUI();
    }

    private void updateSlider(int type) {
        int value = 0;
        ReproduceRate reproduceRate;
        SeekBarButton seekBar;

        reproduceRate =  mTvDispAreaUtil.getReproduceRate();
        switch (type) {
            case REPRODUCE_ADJUST_TOP :
                value = reproduceRate._reduceRate.topRate;
                seekBar = mSeekBarTop;
                mSeekBarTop.setProgress((short) value);
                break;
            case REPRODUCE_ADJUST_BUTTOM:
                value = reproduceRate._reduceRate.bottomRate;
                seekBar = mSeekBarBottom;
                break;
            case REPRODUCE_ADJUST_LEFT:
                value = reproduceRate._reduceRate.leftRate;
                seekBar = mSeekBarLeft;
                break;
            case REPRODUCE_ADJUST_RIGHT:
                value = reproduceRate._reduceRate.rightRate;
                seekBar = mSeekBarRight;
                break;
            default:
                return;
        }
        seekBar.setProgress((short) value);
    }

    private void refreshUI() {
        updateSlider(REPRODUCE_ADJUST_TOP);
        updateSlider(REPRODUCE_ADJUST_BUTTOM);
        updateSlider(REPRODUCE_ADJUST_LEFT);
        updateSlider(REPRODUCE_ADJUST_RIGHT);
    }
}
