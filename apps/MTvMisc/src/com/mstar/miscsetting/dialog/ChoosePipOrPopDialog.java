//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.miscsetting.dialog;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.miscsetting.hoilder.MainMenuHolder;
import com.mstar.miscsetting.broadcast.HotKeyBroadCastReceiver;
import com.mstar.miscsetting.R;
import com.mstar.util.Tools;

public class ChoosePipOrPopDialog extends Dialog {
    private static final String TAG = "ChoosePipOrPopDialog";

    private final String FEATURE_LIVE_TV = "android.software.live_tv";

    public PipMenuDialog4Scene pipTVMenuDialog = null;

    TextView text_pip;

    TextView text_pop;

    TextView text_dual;

    private boolean mainWinIsIn3dMode = false;

    private boolean mainWinIs4k2kMode = false;

    private Handler handler;

    private boolean isGettingData = true;

    public enum MISCMODE {
        // / Pip mode
        PIP_MODE,
        // / Pop mode
        POP_MODE,
        // / dual mode
        DUAL_MODE,
        // / mode num
        MODE_NUM
    }

    public enum OSDTHREEDMODE {
        DISPLAYMODE_NORMAL, // 0
        DISPLAYMODE_LEFTRIGHT, // 1
        DISPLAYMODE_TOPBOTTOM, // 2
        DISPLAYMODE_TOPBOTTOM_LA, // 3
        DISPLAYMODE_NORMAL_LA, // 4
        DISPLAYMODE_TOP_LA, // 5
        DISPLAYMODE_BOTTOM_LA, // 6
        DISPLAYMODE_LEFT_ONLY, // 7
        DISPLAYMODE_RIGHT_ONLY, // 8
        DISPLAYMODE_TOP_ONLY, // 9
        DISPLAYMODE_BOTTOM_ONLY, // 10
        DISPLAYMODE_LEFTRIGHT_FR, // 11
        DISPLAYMODE_NORMAL_FR, // 12
        DISPLAYMODE_MAX // 13
    }

    public static MISCMODE miscMode;

    // Width and Height must same as content size in choosepiporpop.xml
    private static final int mWidthDip = 167;

    private static final int mHeightDip = 33;

    public ChoosePipOrPopDialog(Context context, int style) {
        super(context, style);
        handler = new Handler();
    }

    // chnage the DecorView's position and size according to content
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        float scale = getContext().getResources().getDisplayMetrics().density;

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.RIGHT | Gravity.TOP;
        lp.x = 0;
        lp.y = 0;
        lp.width = Math.round(mWidthDip * scale);
        lp.height = Math.round(mHeightDip * scale);
        getWindow().getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hidden title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        miscMode = MISCMODE.PIP_MODE;
        setContentView(R.layout.choosepiporpop);

        findView();
        text_pip.requestFocus();
        if (false == isSupportPip()) {
            grayOut(text_pip);
        }
        if (false == isSupportPopAndDual()) {
            grayOut(text_pop);
            grayOut(text_dual);
        }
        registerListeners();
    }

    private void findView() {
        text_pip = (TextView) findViewById(R.id.textview_pip);
        text_pop = (TextView) findViewById(R.id.textview_pop);
        text_dual = (TextView) findViewById(R.id.textview_dual);
        isGettingData = true;
        // those thing below block the ui thread, so put them in a new thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (TvS3DManager.getInstance() != null) {
                    int current3dType = TvS3DManager.getInstance().getCurrent3dType();
                    if ((current3dType > TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_ALTERNATIVE)
                        || (current3dType == TvS3DManager.THREE_DIMENSIONS_TYPE_NONE)) {
                        mainWinIsIn3dMode = false;
                    } else {
                        mainWinIsIn3dMode = true;
                    }
                }
                mainWinIs4k2kMode = TvPictureManager.getInstance().is4K2KMode(true);
                final boolean isDualMM = SystemProperties.getBoolean("mstar.localmm.dual-playing", false);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isDualMM || mainWinIs4k2kMode || mainWinIsIn3dMode) {
                            String ToastMsg = "Not support PIP in ";
                            if (isDualMM) {
                                ToastMsg += "dual playing";
                            } else if (mainWinIsIn3dMode) {
                                ToastMsg += "3D";
                            } else {
                                ToastMsg += "4k2k";
                            }
                            Toast.makeText(getContext(), ToastMsg, Toast.LENGTH_LONG).show();
                            grayOut(text_pip);
                            grayOut(text_pop);
                            grayOut(text_dual);
                        } else if (false == isSupportPopAndDual()) {
                            grayOut(text_pop);
                            grayOut(text_dual);
                        }
                    }
                });
                isGettingData = false;
            }
        }).start();
    }

    private void grayOut(final TextView grayView) {
        if (grayView != null) {
            grayView.setEnabled(false);
            grayView.setFocusable(false);
            grayView.setTextColor(Color.GRAY);
        }
    }

    private boolean isSupportPip() {
        if (VERSION.SDK_INT >= TvCommonManager.API_LEVEL_LOLLIPOP_MR1) {
            PackageManager pm = getContext().getPackageManager();
            if (pm.hasSystemFeature(FEATURE_LIVE_TV)) {
                return false;
            }
        }
        boolean isTifEnable = SystemProperties.getBoolean("mstar.tif.enable", false);
        if (isTifEnable) {
            return false;
        }
        return true;
    }

    private boolean isSupportPopAndDual() {
        if (VERSION.SDK_INT >= TvCommonManager.API_LEVEL_LOLLIPOP_MR1) {
            PackageManager pm = getContext().getPackageManager();
            if (pm.hasSystemFeature(FEATURE_LIVE_TV)) {
                return false;
            }
        }
        boolean isTifEnable = SystemProperties.getBoolean("mstar.tif.enable", false);
        MainMenuHolder.mMainInputSrc = MainMenuHolder.tvCommonManager.getCurrentTvInputSource();
        Log.d(TAG, "mMainInputSrc = " + MainMenuHolder.mMainInputSrc);
        Log.d(TAG, "isHome = " + MainMenuHolder.isHome
                + ", isMM = " + MainMenuHolder.isMM
                + ", isBrowser = " + MainMenuHolder.isBrowser
                + ", isTifEnable = " + isTifEnable
                + ", isMMPlay = " + MainMenuHolder.isMMPlay
                + ", isBrowserVideoPlaying = " + Tools.isBrowserVideoPlaying());
        if (MainMenuHolder.isHome || MainMenuHolder.isMM || (MainMenuHolder.isBrowser && !Tools.isBrowserVideoPlaying()) || isTifEnable) {
            return false;
        } else if (MainMenuHolder.isMMPlay
                || (MainMenuHolder.mMainInputSrc < TvCommonManager.INPUT_SOURCE_STORAGE)
                ||(TvCommonManager.INPUT_SOURCE_VGA2 == MainMenuHolder.mMainInputSrc)
                ||(TvCommonManager.INPUT_SOURCE_VGA3 == MainMenuHolder.mMainInputSrc)
                || Tools.isBrowserVideoPlaying()) {
            return true;
        }
        return false;
    }

    private void registerListeners() {
        text_pip.setOnClickListener(listener);
        text_pop.setOnClickListener(listener);
        text_dual.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isGettingData)
                return;
            switch (view.getId()) {
                case R.id.textview_pip:
                    miscMode = MISCMODE.PIP_MODE;
                    break;
                case R.id.textview_pop:
                    miscMode = MISCMODE.POP_MODE;
                    break;
                case R.id.textview_dual:
                    miscMode = MISCMODE.DUAL_MODE;
                    break;
                default:
                    break;
            }
            enterPipOrPopSkin();
            dismiss();
        }
    };

    void enterPipOrPopSkin() {
        pipTVMenuDialog = new PipMenuDialog4Scene(getContext(), R.style.mainMenu);
        pipTVMenuDialog.show();
    }
}
