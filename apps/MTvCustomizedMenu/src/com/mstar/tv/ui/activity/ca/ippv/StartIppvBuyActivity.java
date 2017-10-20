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

package com.mstar.tv.ui.activity.ca.ippv;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mstar.android.tvapi.dtv.vo.CaStartIPPVBuyDlgInfo;
import com.mstar.tv.R;
import android.util.Log;
import com.mstar.android.tv.TvCaManager;
import com.mstar.android.tvapi.dtv.common.CaManager;
import com.mstar.android.tvapi.dtv.common.CaManager.OnCaEventListener;
import com.mstar.android.tvapi.dtv.vo.CaLockService;
import com.mstar.android.tvapi.dtv.vo.CaStartIPPVBuyDlgInfo;

public class StartIppvBuyActivity extends Activity {
    private static final String TAG = "StartIppvBuyActivity";
    private CaStartIPPVBuyDlgInfo StIppvInfo;

    private int pricestate;

    private OnCaEventListener mCaEventListener = null;

    class CaEventListener implements OnCaEventListener {
        @Override
        public boolean onStartIppvBuyDlg(CaManager mgr, int what, int arg1, int arg2,CaStartIPPVBuyDlgInfo arg3) {
            return true;
        }

        @Override
        public boolean onHideIPPVDlg(CaManager mgr, int what, int arg1, int arg2) {
            StartIppvBuyActivity.this.finish();
            return true;
        }

        @Override
        public boolean onEmailNotifyIcon(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onShowOSDMessage(CaManager mgr, int what, int arg1, int arg2, String arg3) {
            return true;
        }

        @Override
        public boolean onHideOSDMessage(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onRequestFeeding(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }


        @Override
        public boolean onShowBuyMessage(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onShowFingerMessage(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onShowProgressStrip(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onActionRequest(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onEntitleChanged(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onDetitleReceived(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onLockService(CaManager mgr, int what, int arg1, int arg2, CaLockService arg3) {
            return true;
        }

        @Override
        public boolean onUNLockService(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }

        @Override
        public boolean onOtaState(CaManager mgr, int what, int arg1, int arg2) {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ippv_buy_dialog);

        StIppvInfo = (CaStartIPPVBuyDlgInfo) getIntent().getSerializableExtra("StIppvInfo");
        TextView ippv_buy_dialog_segment = (TextView) findViewById(R.id.ippv_buy_dialog_segment);
        TextView ippv_buy_dialog_tvsid = (TextView) findViewById(R.id.ippv_buy_dialog_tvsid);
        TextView ippv_buy_dialog_productid = (TextView) findViewById(R.id.ippv_buy_dialog_productid);
        TextView ippv_buy_dialog_slotid = (TextView) findViewById(R.id.ippv_buy_dialog_slotid);
        TextView ippv_buy_dialog_expireddate = (TextView) findViewById(R.id.ippv_buy_dialog_expireddate);
        final TextView ippv_buy_dialog_price = (TextView) findViewById(R.id.ippv_buy_dialog_price);
        Button confirm_button = (Button) findViewById(R.id.ippv_buy_dialog_confirm);
        Button cancel_button = (Button) findViewById(R.id.ippv_buy_dialog_cancel);
        confirm_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StartIppvBuyActivity.this, IppvPinActivity.class);
                Bundle bundle = new Bundle();
                if (pricestate == 1) {
                    bundle.putShort("price", StIppvInfo.m_Price[1].m_wPrice);
                    bundle.putShort("pricecode", StIppvInfo.m_Price[1].m_byPriceCode);
                } else if (pricestate == 0) {
                    bundle.putShort("price", StIppvInfo.m_Price[0].m_wPrice);
                    bundle.putShort("pricecode", StIppvInfo.m_Price[0].m_byPriceCode);
                } else {
                    return;
                }
                bundle.putShort("ecmpid", StIppvInfo.getwEcmPid());
                bundle.putShort("messagetype", StIppvInfo.getWyMessageType());
                intent.putExtras(bundle);
                StartIppvBuyActivity.this.startActivity(intent);
                finish();

            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        switch (StIppvInfo.wyMessageType) {
            case 0:
                ippv_buy_dialog_segment.setText(getResources().getString(
                        R.string.ippv_freeviewed_segment));
                ippv_buy_dialog_expireddate.setText(getResources().getString(
                        R.string.ippv_payviewed_expireddate)
                        + dateConvert(StIppvInfo.wExpiredDate));
                break;
            case 1:
                ippv_buy_dialog_segment.setText(getResources().getString(
                        R.string.ippv_payviewed_segment));
                ippv_buy_dialog_expireddate.setText(getResources().getString(
                        R.string.ippv_payviewed_expireddate)
                        + dateConvert(StIppvInfo.wExpiredDate));
                break;
            case 2:
                ippv_buy_dialog_segment.setText(getResources().getString(
                        R.string.ippt_payviewed_segment));
                ippv_buy_dialog_expireddate.setText(getResources().getString(
                        R.string.ippt_interval_min)
                        + StIppvInfo.wExpiredDate
                        + getResources().getString(R.string.str_work_time_minute_text));
                break;

        }

        ippv_buy_dialog_tvsid.setText(getResources().getString(R.string.ippv_tvsid)
                + StIppvInfo.wTvsID);
        ippv_buy_dialog_productid.setText(getResources().getString(
                R.string.ippv_payviewed_productid)
                + StIppvInfo.dwProductID);
        ippv_buy_dialog_slotid.setText(getResources().getString(R.string.ippv_payviewed_slotid)
                + StIppvInfo.wySlotID);

        int pricecode = StIppvInfo.m_Price[1].m_byPriceCode;
        if (pricecode == 1) {
            pricestate = 1;
            ippv_buy_dialog_price.setText(getResources().getString(R.string.ippv_payviewed_cantape)
                    + StIppvInfo.m_Price[1].m_wPrice);
        } else if (pricecode == 0) {
            pricestate = 0;
            ippv_buy_dialog_price.setText(getResources().getString(
                    R.string.ippv_payviewed_cannottape)
                    + StIppvInfo.m_Price[0].m_wPrice);
        }

        ippv_buy_dialog_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ippv_buy_dialog_price.setBackgroundResource(R.drawable.programme_epg_img_focus);
                } else {
                    ippv_buy_dialog_price.setBackgroundResource(Color.TRANSPARENT);
                }
            }
        });

        ippv_buy_dialog_price.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    if (pricestate == 1) {
                        ippv_buy_dialog_price.setText(getResources().getString(
                                R.string.ippv_payviewed_cannottape)
                                + StIppvInfo.m_Price[0].getM_wPrice());
                        pricestate = 0;
                    } else if (pricestate == 0) {
                        ippv_buy_dialog_price.setText(getResources().getString(
                                R.string.ippv_payviewed_cantape)
                                + StIppvInfo.m_Price[1].getM_wPrice());
                        pricestate = 1;
                    }
                }
                return false;
            }
        });
        ippv_buy_dialog_price.requestFocus();

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        mCaEventListener = new CaEventListener();
        TvCaManager.getInstance().registerOnCaEventListener(mCaEventListener);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        TvCaManager.getInstance().unregisterOnCaEventListener(mCaEventListener);
        mCaEventListener = null;
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent arg1) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, arg1);
    }

    private String dateConvert(short dayNum) {
        String endDate = "";
        Calendar ca = Calendar.getInstance();
        ca.set(2000, Calendar.JANUARY, 1);
        ca.add(Calendar.DATE, dayNum);
        Format s = new SimpleDateFormat("yyyy-MM-dd");
        endDate = s.format(ca.getTime());

        return endDate;
    }
}
