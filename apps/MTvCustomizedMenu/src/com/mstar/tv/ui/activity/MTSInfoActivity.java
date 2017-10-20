//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2016 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.MKeyEvent;
import com.mstar.tv.R;
import com.mstar.tv.util.TimeOutHelper;

public class MTSInfoActivity extends Activity {

    private final String TAG = "MTSInfoActivity";

    private TextView mtsInfo = null;

    private ArrayList<String> audioInfo = new ArrayList<String>();

    private TimeOutHelper mTimeoutHelper;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == mTimeoutHelper.getTimeOutMsg()) {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.mtsinfo);
        mTimeoutHelper = new TimeOutHelper(mHandler, this);
        mtsInfo = (TextView) findViewById(R.id.mtsType);
        mtsInfo.setText(getSoundFormat());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimeoutHelper.setValue(3);
        mTimeoutHelper.start();
    };

    @Override
    protected void onPause() {
        super.onPause();
        mTimeoutHelper.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        mTimeoutHelper.reset();
        switch (keyCode) {
            case MKeyEvent.KEYCODE_MTS:
                TvCommonManager.getInstance().setToNextATVMtsMode();
                mtsInfo.setText(getSoundFormat());
                break;
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

    private String getSoundFormat() {
       String str = "";
        switch (TvCommonManager.getInstance().getATVMtsMode()) {
            case TvCommonManager.ATV_AUDIOMODE_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_A:
                str = getResources().getString(R.string.str_sound_format_dual_a);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_AB:
                str = getResources().getString(R.string.str_sound_format_dual_ab);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_B:
                str = getResources().getString(R.string.str_sound_format_dual_b);
                break;
            case TvCommonManager.ATV_AUDIOMODE_FORCED_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_G_STEREO:
                str = getResources().getString(R.string.str_sound_format_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_HIDEV_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_K_STEREO:
                str = getResources().getString(R.string.str_sound_format_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_LEFT_LEFT:
                str = getResources().getString(R.string.str_sound_format_left_left);
                break;
            case TvCommonManager.ATV_AUDIOMODE_LEFT_RIGHT:
                str = getResources().getString(R.string.str_sound_format_left_right);
                break;
            case TvCommonManager.ATV_AUDIOMODE_MONO_SAP:
                str = getResources().getString(R.string.str_sound_format_mono_sap);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_A:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_a);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_AB:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_ab);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_B:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_b);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_MONO:
                str = getResources().getString(R.string.str_sound_format_nicam_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_STEREO:
                str = getResources().getString(R.string.str_sound_format_nicam_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_RIGHT_RIGHT:
                str = getResources().getString(R.string.str_sound_format_right_right);
                break;
            case TvCommonManager.ATV_AUDIOMODE_STEREO_SAP:
                str = getResources().getString(R.string.str_sound_format_stereo_sap);
                break;
            case TvCommonManager.ATV_AUDIOMODE_INVALID:
            default:
                str = getResources().getString(R.string.str_sound_format_unknown);
                break;
        }
        return str;
    }
}
