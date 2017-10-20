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

package com.mstar.tv.ui.activity.ca;

import com.mstar.tv.R;
import com.mstar.tv.ui.activity.LittleDownTimer;
import com.mstar.tv.ui.activity.ca.ippv.IppvProgramInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class CaInformationActivity extends Activity {
    private String OperatorId;

    private String CURRNT_OPERATOR_ID = "OperatorId";

    private String TAG = "CaInformationActivity";

    protected LinearLayout linearlayout_ca_info_entitle;

    protected LinearLayout linearlayout_ca_info_detitle;

    protected LinearLayout linearlayout_ca_info_purse;

    protected LinearLayout linearlayout_ca_card_info;

    protected LinearLayout linearlayout_ca_ippv_program;

    private Intent intent = new Intent();

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
                finish();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_card_information);
        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
            Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
        }
        LittleDownTimer.setHandler(handler);
        findViews();
        setOnClickLisenters();
    }

    @Override
    protected void onResume() {
        Log.d("TvApp", "CaInformationActivity onResume");
        LittleDownTimer.resumeMenu();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("TvApp", "CaInformationActivity onPause");
        LittleDownTimer.pauseMenu();
        super.onPause();
    }

    @Override
    public void onUserInteraction() {
        Log.d("TvApp", "CaInformationActivity onUserInteraction");
        LittleDownTimer.resetMenu();
        super.onUserInteraction();
    }

    private void findViews() {
        linearlayout_ca_info_entitle = (LinearLayout) findViewById(R.id.linearlayout_ca_info_entitle);
        linearlayout_ca_info_detitle = (LinearLayout) findViewById(R.id.linearlayout_ca_info_detitle);
        linearlayout_ca_info_purse = (LinearLayout) findViewById(R.id.linearlayout_ca_info_purse);
        linearlayout_ca_card_info = (LinearLayout) findViewById(R.id.linearlayout_ca_card_info);
        linearlayout_ca_ippv_program = (LinearLayout) findViewById(R.id.linearlayout_ca_ippv_program);
    }

    private void setOnClickLisenters() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.linearlayout_ca_info_entitle:
                        intent.setClass(CaInformationActivity.this, CaEntitleInfoActivity.class);
                        intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.linearlayout_ca_info_detitle:
                        intent.setClass(CaInformationActivity.this, CaDetitleInfoActivity.class);
                        intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.linearlayout_ca_info_purse:
                        intent.setClass(CaInformationActivity.this, CaPurseInfoActivity.class);
                        intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.linearlayout_ca_ippv_program:
                        intent.setClass(CaInformationActivity.this, IppvProgramInfo.class);
                        intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.linearlayout_ca_card_info:
                        intent.setClass(CaInformationActivity.this, CaCardInfoActivity.class);
                        intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
        linearlayout_ca_info_entitle.setOnClickListener(listener);
        linearlayout_ca_info_detitle.setOnClickListener(listener);
        linearlayout_ca_info_purse.setOnClickListener(listener);
        linearlayout_ca_ippv_program.setOnClickListener(listener);
        linearlayout_ca_card_info.setOnClickListener(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                intent.setClass(CaInformationActivity.this, CaOperatorListActivity.class);
                if ((getIntent() != null) && (getIntent().getExtras() != null)) {
                    OperatorId = getIntent().getStringExtra(CURRNT_OPERATOR_ID);
                    Log.d(TAG, "get String extra,OperatorId=" + OperatorId);
                }
                intent.putExtra(CURRNT_OPERATOR_ID, OperatorId);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
