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

package com.mstar.miscsetting.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.TvPlayer;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureDeviceType;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureSource;
import com.mstar.miscsetting.dialog.PipMenuDialog4Scene;
import com.mstar.miscsetting.R;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog.MISCMODE;
import com.mstar.miscsetting.dialog.PipSubWindowInputSourceDialog;
import com.mstar.miscsetting.dialog.PipSubWindowInputSourceDialog.PIP_MODE_ENABLED;
import com.mstar.miscsetting.dialog.PipSubWindowSwitchVolDialog.SWITCH_VOL;
import com.mstar.miscsetting.hoilder.MainMenuHolder;
import com.mstar.util.Tools;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.vo.PanelProperty;
import android.util.DisplayMetrics;

/**
 *
 */
public class PipService extends Service implements Callback {
    private static final String TAG = "PipService";

    private TvPlayer mtvplayer = null;

    private WindowManager PIPwm = null;

    public static View PIPView;

    private SurfaceHolder mSurfaceHolder = null;

    public static SWITCH_VOL volStatus;

    private BroadcastReceiver strBcRev;

    @Override
    public void onCreate() {
        super.onCreate();
        strBcRev = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (PIPView != null) {
                        PIPwm.removeView(PIPView);
                        PIPView = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
        registerReceiver(strBcRev, filter );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand()");

        boolean isTifEnable = SystemProperties.getBoolean("mstar.tif.enable", false);
        if (isTifEnable) {
            return super.onStartCommand(intent, flags, startId);
        }

        if (mtvplayer == null) {
            mtvplayer = TvManager.getInstance().getPlayerManager();
        }

        if (intent != null) {
            String cmd = new String();
            cmd = intent.getStringExtra("cmd");
            if ("showpip".equals(cmd)) {
                showPip(intent);
            } else if ("removepip".equals(cmd)) {
                try {
                    synchronized (this) {
                            PIPwm.removeView(PIPView);
                            PipMenuDialog4Scene.enableMoveScreenThread = false;
                            PIPwm = null;
                            PIPView = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("visible".equals(cmd)) {
                if (PIPView != null) {
                    SurfaceView PIPSurface = (SurfaceView) (PIPView.findViewById(R.id.PIPSurface));
                    PIPSurface.setVisibility(View.VISIBLE);
                }
            } else if ("invisible".equals(cmd)) {
                if (PIPView != null) {
                    SurfaceView PIPSurface = (SurfaceView) (PIPView.findViewById(R.id.PIPSurface));
                    PIPSurface.setVisibility(View.GONE);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent arg) {

        return null;
    }

    private void showPip(Intent arg) {
        Log.i(TAG, "showPip()");
        Bundle bundle = arg.getBundleExtra("pipdata");

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;

        params.x = bundle.getInt("x");
        params.y = bundle.getInt("y");
        params.width = bundle.getInt("width");
        params.height = bundle.getInt("height");
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        if (PIPwm == null) {
            PIPwm = (WindowManager) getApplicationContext().getSystemService(
                    Context.WINDOW_SERVICE);
        }
        try {
            if (PIPView == null) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PIPView = inflater.inflate(R.layout.pipsurface, null);
                SurfaceView PIPSurface = (SurfaceView) (PIPView.findViewById(R.id.PIPSurface));
                mSurfaceHolder = PIPSurface.getHolder();
                mSurfaceHolder.addCallback(this);
                PIPwm.addView(PIPView, params);
            } else {
                PIPwm.updateViewLayout(PIPView, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i(TAG, "surfaceCreated()");
        try {
            mtvplayer.setDisplay(mSurfaceHolder, TvPlayer.TVPLAYER_WINDOW_TYPE_SUBDEV);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        try {
            if (mtvplayer.destoryDisplay(mSurfaceHolder) != true) {
                Log.e(TAG, "destoryDisplay failed");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(strBcRev);
        super.onDestroy();
    }
}
