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

package com.mstar.android.tv.tunersetup.tuning;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.inputservice.R;
import com.mstar.android.tv.tunersetup.TvIntent;
import com.mstar.android.tv.tunersetup.holder.ViewHolder;
import com.mstar.android.tv.util.Constant;
import com.mstar.android.tv.util.Utility;

public class ExitTuningInfoDialog extends Dialog {
    private static final String TAG = "ExitTuningInfoDialog";

    @SuppressWarnings("unused")
    private ViewHolder viewholder_exittune;

    private static int ATV_MIN_FREQ = 45200;

    private static int ATV_MAX_FREQ = 876250;

    private static int ATV_EVENTINTERVAL = 500 * 1000;// every 500ms to show

    protected TextView textview_cha_exittune_yes;

    protected TextView textview_cha_exittune_no;

    TvChannelManager mTvChannelManager = null;

    @SuppressWarnings("unused")
    private ViewHolder viewholder_channeltune;

    public ExitTuningInfoDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exittuninginfo_dialog);
        textview_cha_exittune_yes = (TextView) findViewById(R.id.textview_cha_exittune_yes);
        textview_cha_exittune_no = (TextView) findViewById(R.id.textview_cha_exittune_no);
        viewholder_exittune = new ViewHolder(ExitTuningInfoDialog.this);
        textview_cha_exittune_yes.requestFocus();
        registerListeners();
        mTvChannelManager = TvChannelManager.getInstance();
    }

    private void registerListeners() {
        textview_cha_exittune_yes.setOnClickListener(listener);
        textview_cha_exittune_no.setOnClickListener(listener);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ExitTuningActivityExit(false);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.textview_cha_exittune_yes:
                    ExitTuningActivityExit(true);
                    break;
                case R.id.textview_cha_exittune_no:
                    ExitTuningActivityExit(false);
                    break;
                default:
                    ExitTuningActivityExit(false);
                    break;
            }
        }
    };

    private void ExitTuningActivityExit(boolean flag) {
        Intent intent = new Intent();
        if (flag == true)// stop tuning
        {
            switch (mTvChannelManager.getTuningStatus()) {
                case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                    mTvChannelManager.stopAtvAutoTuning();
                    intent.setAction(TvIntent.ACTION_TUNER_SETUP);
                    intent.putExtra(Constant.UPDATE_TV_PROVIDER, true);
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        getContext().startActivity(intent);
                    }
                    this.dismiss();
                    break;
                case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                    mTvChannelManager.stopDtvScan();
                    // DVB and ISDB don't do this until ChannelTuning receives
                    // DTV_SCAN_STATUS_END event
                    if (Utility.isATSC()) {
                        if (mTvChannelManager.getUserScanType() == mTvChannelManager.TV_SCAN_ALL) {
                            boolean res = mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL,
                                    ATV_MIN_FREQ, ATV_MAX_FREQ);
                            if (res == false) {
                                Log.e(TAG, "startAtvAutoTuning fail!!!");
                            }
                        } else {
                            intent.setAction(TvIntent.ACTION_TUNER_SETUP);
                            intent.putExtra(Constant.UPDATE_TV_PROVIDER, true);
                            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                getContext().startActivity(intent);
                            }
                        }
                    }
                    this.dismiss();
                    break;
                default:
                    break;
            }
        } else
        // resume tuning
        {
            switch (mTvChannelManager.getTuningStatus()) {
                case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                    mTvChannelManager.resumeAtvAutoTuning();
                    this.dismiss();
                    break;
                case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                    mTvChannelManager.resumeDtvScan();
                    this.dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}
