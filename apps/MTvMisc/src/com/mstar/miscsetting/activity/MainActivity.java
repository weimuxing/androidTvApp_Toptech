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

package com.mstar.miscsetting.activity;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvPipPopManager;
import com.mstar.miscsetting.broadcast.HotKeyBroadCastReceiver;
import com.mstar.miscsetting.constants.PipContants;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog.MISCMODE;
import com.mstar.miscsetting.dialog.PipSubWindowSwitchVolDialog.SWITCH_VOL;
import com.mstar.miscsetting.hoilder.MainMenuHolder;
import com.mstar.miscsetting.service.PipService;
import com.mstar.miscsetting.R;
import com.mstar.util.Tools;

public class MainActivity extends BaseActivity {
    public static MainMenuHolder mMainMenuHolder = null;

    private static final String TAG = "MainActivity";

    // chnage the DecorView's position and size according to content
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout_main);
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.x = 0;
        lp.y = 0;
        lp.width = ll.getLayoutParams().width;
        lp.height = ll.getLayoutParams().height;
        lp.gravity = Gravity.RIGHT | Gravity.TOP;
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Hidden title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        // Initialization MainMenuHolder
        mMainMenuHolder = new MainMenuHolder(this, R.style.mainMenu, "MENU");
        // Initialization control
        mMainMenuHolder.findView();
        // Initialization animation
        mMainMenuHolder.initAnimation();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mstar.tv.ui.tvend");
        registerReceiver(mReceiver, filter);
        Log.i(TAG, "MainActivity.onCreate-->PIPorPOP Start completed........................"
                + mMainMenuHolder);
        updateAppRunningStatus();
    }

    @SuppressWarnings("deprecation")
    private void updateAppRunningStatus() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(2);
        if (taskList.size() > 1) {
            ComponentName cn = taskList.get(1).topActivity;
            String packageName = cn.getPackageName();
            Log.i(TAG, "***top activity package=" + packageName);
            Log.i(TAG, "***top activity class=" + cn.getClassName());
            MainMenuHolder.isHome = false;
            MainMenuHolder.isMM = false;
            MainMenuHolder.isBrowser = false;
            if (packageName.equals("com.android.mslauncher")) {
                MainMenuHolder.isHome = true;
            } else if (packageName.equals("com.android.browser")
                    && !Tools.isBrowserVideoPlaying()) {
                MainMenuHolder.isBrowser = true;
            } else if (packageName.equals("com.jrm.localmm")
                    && !cn.getClassName().contains("VideoPlayerActivity")) {
                MainMenuHolder.isMM = true;
            } else if (packageName.equals("com.jrm.localmm")
                    && cn.getClassName().contains("VideoPlayerActivity")) {
                MainMenuHolder.isMMPlay = true;
            }
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.mstar.tv.ui.tvend")) {
                if (MainMenuHolder.miscmode == MISCMODE.POP_MODE) {
                    TvPipPopManager.getInstance().disablePOP(TvPipPopManager.E_MAIN_WINDOW);
                    finish();
                }
                Log.i(TAG, "receive [com.mstar.tv.ui.tvend]!");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (TvPipPopManager.getInstance().getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_OFF) {
            PipService.volStatus = SWITCH_VOL.BT_HEADSET_MAIN;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mMainMenuHolder.pipOrPopDialog != null
                && mMainMenuHolder.pipOrPopDialog.pipTVMenuDialog != null) {
            mMainMenuHolder.pipOrPopDialog.pipTVMenuDialog.redrawPipInputSourceDialog();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "keyResponse: " + !mMainMenuHolder.keyResponse);
        Log.i(TAG, "keyCode: " + keyCode);
        Log.i(TAG, "event.getAction(): " + event.getAction());
        if (!mMainMenuHolder.keyResponse) {
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return mMainMenuHolder.processLeftOrRightKey(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            return mMainMenuHolder.processUpOrDownKey(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
