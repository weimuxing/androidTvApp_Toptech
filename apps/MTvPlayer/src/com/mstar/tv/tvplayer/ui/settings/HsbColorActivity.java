//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.tvplayer.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mstar.android.tv.TvPictureManager;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.SeekBarButton;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;

public class HsbColorActivity extends MstarBaseActivity {
    private static final String TAG = "HsbColorActivity";

    private final int MODE_OFF = 0;

    private final int MODE_ON = 1;

    private TvPictureManager mTvPictureManager = null;

    private String[] mColorDef = null;

    private int[] mIniColorSetting;

    private SeekBarButton[] mSeekBtnList = null;

    private LinearLayout mHsbOnOff;

    private ComboButton mBtnMode;

    private ComboButton mBtnType;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
                Intent intent = new Intent(HsbColorActivity.this, RootActivity.class);
                startActivity(intent);
                finish();
            }
        };
    };

    private OnClickListener mResetOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ResetOnClickListener!!");
                mHsbOnOff.requestFocus();
                mTvPictureManager.resetAdvanceColor();
                mBtnMode.setIdx((mTvPictureManager.getAdvanceColorEnable() == true) ? MODE_ON:MODE_OFF);
                mBtnType.setIdx(TvPictureManager.ADVANCE_COLOR_HSB_HUE);
                loadColorData();
            }
        };

    private OnFocusChangeListener mSeekBarBtnFocusListenser = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                LinearLayout container = (LinearLayout) v;
                container.getChildAt(2).setVisibility(View.VISIBLE);
            } else {
                LinearLayout container = (LinearLayout) v;
                container.getChildAt(2).setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hsb_color);
        mTvPictureManager = TvPictureManager.getInstance();
        LittleDownTimer.setHandler(mHandler);
        mColorDef = getResources().getStringArray(R.array.str_arr_hsb_color_vals);
        mIniColorSetting = mTvPictureManager.getAdvanceColorIniSetting();
        mBtnMode = new ComboButton(this, getResources().getStringArray(
                        R.array.str_arr_hsb_mode_vals),
                R.id.linearlayout_pic_hsb_on_off, 1, 2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                boolean isEnable = (MODE_OFF == getIdx()) ? false : true;
                mTvPictureManager.setAdvanceColorEnable(isEnable);
                updateItemStatus(isEnable);
            }
        };
        mBtnMode.setIdx((mTvPictureManager.getAdvanceColorEnable() == true) ? MODE_ON:MODE_OFF);
        mHsbOnOff = (LinearLayout)findViewById(R.id.linearlayout_pic_hsb_on_off);
        mBtnType = new ComboButton(this, getResources().getStringArray(
                        R.array.str_arr_hsb_type_vals),
                R.id.linearlayout_pic_hsb_type, 1, 2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                loadColorData();
            }
        };

        LinearLayout rootLinearLayout = (LinearLayout)findViewById(R.id.linearlayout_pic_hsb);
        mSeekBtnList = new SeekBarButton[mColorDef.length];
        LinearLayout lastLl = null;
        int counter = 2;
        for(int colorId = 0; colorId < mColorDef.length; colorId++) {
            if (true == isSupportColorId(colorId)) {
                View v = LayoutInflater.from(HsbColorActivity.this).inflate(R.layout.hsb_color_item, null);
                LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearlayout_pic_hsb_item);
                ll.setId(View.generateViewId());
                lastLl = ll;
                mSeekBtnList[colorId] = newSeekButton(ll, colorId);
                TextView tmpText = (TextView) v.findViewById(R.id.hsb_color_item_title);
                tmpText.setText(mColorDef[colorId]);
                rootLinearLayout.addView(v, counter);
                counter++ ;
            } else {
                mSeekBtnList[colorId] = null;
            }
        }

        if (null != lastLl) {
            lastLl.setNextFocusForwardId(R.id.linearlayout_pic_hsb_reset);
        }

        LinearLayout resetView = (LinearLayout)findViewById(R.id.linearlayout_pic_hsb_reset);
        resetView.setOnClickListener(mResetOnClickListener);

        loadColorData();
    }

    @Override
    protected void onResume() {
        LittleDownTimer.resumeMenu();
        super.onResume();
    }

    @Override
    public void onUserInteraction() {
        LittleDownTimer.resetMenu();
        super.onUserInteraction();
    }

    @Override
    protected void onPause() {
        LittleDownTimer.pauseMenu();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TvIntent.MAINMENU);
        intent.putExtra("currentPage", MainMenuActivity.PICTURE_PAGE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        finish();
        super.onBackPressed();
    }

    private boolean isSupportColorId(final int colorId) {
        if (null != mIniColorSetting) {
            return (mIniColorSetting[colorId] == 0) ? false : true;
        }
        return true;
    }

    private SeekBarButton newSeekButton(final LinearLayout ll, final int colorId) {
        SeekBarButton btn = new SeekBarButton(this, ll, 1, false) {
            final int cId = colorId;
            @Override
            public void doUpdate() {
                mTvPictureManager.setAdvanceColor(getProgress(), cId, mBtnType.getIdx());
            }
        };
        btn.setOnFocusChangeListener(mSeekBarBtnFocusListenser);
        return btn;
    }

    private void updateItemStatus(boolean isEnable) {
        for(int colorId = 0; colorId < mColorDef.length; colorId++) {
            if (null != mSeekBtnList[colorId]) {
                mSeekBtnList[colorId].setEnable(isEnable);
                mSeekBtnList[colorId].setFocusable(isEnable);
            }
        }
        mBtnType.setEnable(isEnable);
        mBtnType.setFocusable(isEnable);
    }

    private void loadColorData() {
        final int[] currentColorData = mTvPictureManager.getAdvanceColor(mBtnType.getIdx());
        if (null != currentColorData) {
            for(int colorId = 0; colorId < mColorDef.length; colorId++) {
                if (null != mSeekBtnList[colorId]) {
                    mSeekBtnList[colorId].setProgress((short)(currentColorData[colorId]));
                }
            }
        }
        updateItemStatus((MODE_OFF == mBtnMode.getIdx()) ? false : true);
    }
}
