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

package com.mstar.miscsetting.dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.view.SurfaceView;

import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.vo.EnumScreenMuteType;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.android.tvapi.common.vo.PanelProperty;
import com.mstar.miscsetting.R;
import com.mstar.miscsetting.activity.MainActivity;
import com.mstar.miscsetting.activity.display.util.TvDispAreaUtil;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog.MISCMODE;
import com.mstar.miscsetting.dialog.PipSubWindowInputSourceDialog.PIP_MODE_ENABLED;
import com.mstar.miscsetting.hoilder.GridViewAdapter;
import com.mstar.miscsetting.hoilder.MainMenuHolder;
import com.mstar.miscsetting.hoilder.MenuViewHolder;
import com.mstar.util.Tools;
import com.mstar.miscsetting.service.PipService;
import com.mstar.android.tvapi.common.vo.MuteType.EnumMuteType;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.MIntent;

public class PipMenuDialog4Scene extends Dialog {
    private static final String TAG = "PipMenuDialog4Scene";

    private static final int COMMAND_SEND_MSG = 0x10;

    private static final int COMMAND_KILL_DIALOG = 0x20;

    private static final short SUBINPUTSOURCE_SEND_MSG = 0x30;

    private static Context mActivity;

    private int earPhoneVolume = 0;

    TvPipPopManager tvPipPopManager;

    private Runnable runSetPipDisplayFocusWindow = new Runnable() {
        @Override
        public void run() {
            tvPipPopManager
                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
        }
    };

    private Thread mthreadSetPipDisplayFocusWindow = new Thread(
            runSetPipDisplayFocusWindow);

    TvCommonManager tvCommonManager;

    TvAudioManager tvAudioManager;

    private TvChannelManager tvChannelManager;

    protected static String[] menu4_name = new String[7];

    protected static String[] menu4_name2 = new String[9];

    protected static String[] menu4_name3 = new String[8];

    public PipSubWindowInputSourceDialog pipMenu3Dialog = null;

    /* switch vol dialog */
    public PipSubWindowSwitchVolDialog pipVolDialog = null;

    public static VideoWindowType dispWin = null;

    private static int screenPosition = 0;

    private static boolean isMove = false;

    public static boolean enableMoveScreenThread = false;

    private Thread getMoveScreenThread = null;

    int[] subSrcArrayList;

    private static boolean isFullScreenButtonEnable = false;

    private static TvPictureManager pm = null;

    private static PanelProperty pp = null;

    /* Width and Height must same as content size in menu2.xml */
    private static final int mWidthDip = 337;

    private static final int mHeightDip = 195;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (COMMAND_SEND_MSG == msg.what) {
                if ((tvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE)
                        || (tvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE2)) {
                    Intent source_switch_from_storage = new Intent(
                            "source.switch.from.storage");
                    mActivity.sendBroadcast(source_switch_from_storage);
                }
            } else if (COMMAND_KILL_DIALOG == msg.what) {
                MenuViewHolder.menuBgRelativeLayout
                        .setVisibility(View.INVISIBLE);
                findViewById(R.id.pipmenudialog).setVisibility(View.INVISIBLE);
                TvAudioManager.getInstance().enableAudioMute(TvAudioManager.AUDIO_MUTE_BYUSER);
                TvAudioManager.getInstance().enableAudioMute(TvAudioManager.AUDIO_MUTE_USER_HP);

                MainActivity.mMainMenuHolder.disablePipPop(TvPipPopManager.E_SUB_WINDOW);
                Intent intent = new Intent(MIntent.ACTION_START_TV_PLAYER);
                mActivity.startActivity(intent);
                TvAudioManager.getInstance().disableAudioMute(TvAudioManager.AUDIO_MUTE_BYUSER);
                TvAudioManager.getInstance().disableAudioMute(TvAudioManager.AUDIO_MUTE_USER_HP);
                // dismiss(); don't dismiss itself,otherwise it won't run the
                // code below.
                MainActivity.mMainMenuHolder.finishMainActivity();
            } else if (SUBINPUTSOURCE_SEND_MSG == msg.what) {
                SurfaceView PIPSurface = (SurfaceView) (PipService.PIPView
                        .findViewById(R.id.PIPSurface));
                PIPSurface.setVisibility(View.GONE);
                handler.sendEmptyMessageDelayed(0, 10);

            } else if (0 == msg.what) {
                screenPosition++;
                getDispWinPosition();
                dispWin.width = 680;
                dispWin.height = 540;

                pm = TvPictureManager.getInstance();
                    pp = pm.getPanelWidthHeight();
                SurfaceView PIPSurface = (SurfaceView) (PipService.PIPView
                        .findViewById(R.id.PIPSurface));
                PIPSurface.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(1, 0);
                tvPipPopManager.setPipSubwindow(dispWin);
            }
        };
    };

    public PipMenuDialog4Scene(Context activity, int style) {
        super(activity, style);
        mActivity = activity;
        tvPipPopManager = TvPipPopManager.getInstance();
        tvCommonManager = TvCommonManager.getInstance();
        tvAudioManager = TvAudioManager.getInstance();
        tvChannelManager = TvChannelManager.getInstance();
    }

    // chnage the DecorView's position and size according to content
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        float scale = getContext().getResources().getDisplayMetrics().density;
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int display_width = point.x;
        int display_height = point.y;
        // 4/5 screen
        float display_scale = 0.8f;

        lp.gravity = Gravity.RIGHT | Gravity.TOP;
        // down scale the screen and calculate x/y
        lp.x = Math.round((display_width - display_width * display_scale) / 2.0f);
        lp.y = Math.round((display_height - display_height * display_scale) / 2.0f);
        lp.width = Math.round(mWidthDip * scale);
        lp.height = Math.round(mHeightDip * scale);
        getWindow().getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        // Hidden title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu2);
        earPhoneVolume = tvAudioManager.getEarPhoneVolume();
        // Definition of dispWin with enablePipTV
        dispWin = new VideoWindowType();
        if (tvPipPopManager.getIsPipOn() == false) {
            screenPosition = 0;
        }
        getDispWinPosition();
        dispWin.width = 680;
        dispWin.height = 540;

        // Initialization control
        findView();
        // Initialization Data
        initData();
        // For control and monitor
        registerListener();
        enableMoveScreenThread = true;
        getMoveScreenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (enableMoveScreenThread) {
                    try {
                        Thread.sleep(500);
                        if (isMove) {
                            isMove = false;
                            handler.sendEmptyMessageDelayed(
                                    SUBINPUTSOURCE_SEND_MSG, 0);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (getMoveScreenThread != null) {
            getMoveScreenThread.start();
        }
    }

    /**
     * Initialization control
     */
    private void findView() {
        MenuViewHolder.menu2GridView = (GridView) findViewById(R.id.menu2_gridview);
    }

    /**
     * Initialization Data
     */
    private void initData() {
        isFullScreenButtonEnable = false;
        menu4_name = new String[] {
                mActivity.getResources().getString(R.string.source),
                mActivity.getResources().getString(R.string.channelBig),
                mActivity.getResources().getString(R.string.channelSmall),
                mActivity.getResources().getString(R.string.volumeBig),
                mActivity.getResources().getString(R.string.volumeSmall),
                mActivity.getResources().getString(R.string.exit) };
        menu4_name2 = new String[] {
                mActivity.getResources().getString(R.string.source),
                mActivity.getResources().getString(R.string.channelBig),
                mActivity.getResources().getString(R.string.channelSmall),
                mActivity.getResources().getString(R.string.volumeBig),
                mActivity.getResources().getString(R.string.full_screen),
                mActivity.getResources().getString(R.string.volumeSmall),
                mActivity.getResources().getString(R.string.moveScreen),
                mActivity.getResources().getString(R.string.exit) };
        menu4_name3 = new String[] {
                mActivity.getResources().getString(R.string.source),
                mActivity.getResources().getString(R.string.channelBig),
                mActivity.getResources().getString(R.string.channelSmall),
                mActivity.getResources().getString(R.string.volumeBig),
                mActivity.getResources().getString(R.string.full_screen),
                mActivity.getResources().getString(R.string.volumeSmall),
                mActivity.getResources().getString(R.string.exit) };
        checkSubSource();
    }

    /**
     * For control and monitor
     */
    public void registerListener() {

        // Application classification listening
        MenuViewHolder.menu2GridView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {

                        String menu4Name = (String) parent
                                .getItemAtPosition(position);
                        if (!isFullScreenButtonEnable) {
                            menu4Name = menu4_name[position];
                        }
                        boolean isPip = true;
                        Log.i(TAG, "position:" + position);
                        Log.i(TAG, "menu4Name:" + menu4Name);

                        if (ChoosePipOrPopDialog.miscMode == MISCMODE.PIP_MODE) {
                            isPip = true;
                        } else {
                            isPip = false;
                        }
                        if (mActivity.getResources().getString(R.string.source)
                                .equals(menu4Name)) {

                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
                            int[] subSrcArrayList = tvPipPopManager
                                    .getSubWindowSourceList(isPip);

                            pipMenu3Dialog = new PipSubWindowInputSourceDialog(
                                    mActivity, R.style.mainMenu,
                                    subSrcArrayList);
                            pipMenu3Dialog
                                    .setOnDismissListener(new OnDismissListener() {
                                        @Override
                                        public void onDismiss(
                                                DialogInterface arg0) {
                                        }
                                    });
                            pipMenu3Dialog.show();
                        }

                        if (mActivity.getResources()
                                .getString(R.string.switch_vol)
                                .equals(menu4Name)) {
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);

                            // TODO Demo
                            int[] length = new int[] { 0, 1, 2, 3 };

                            pipVolDialog = new PipSubWindowSwitchVolDialog(
                                    mActivity, R.style.mainMenu, length);

                            pipVolDialog
                                    .setOnDismissListener(new OnDismissListener() {

                                        @Override
                                        public void onDismiss(
                                                DialogInterface arg0) {
                                            checkSubSource();
                                        }

                                    });

                            pipVolDialog.show();

                        }

                        if (mActivity.getResources().getString(R.string.exit)
                                .equals(menu4Name)) {
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
                            dismiss();
                        }
                        if (mActivity.getResources()
                                .getString(R.string.channelBig)
                                .equals(menu4Name)) {
                            if (tvPipPopManager.isPipModeEnabled() == false)
                                return;
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_SUB_WINDOW);
                            TvChannelManager.getInstance().programUp();
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        if (mActivity.getResources()
                                .getString(R.string.channelSmall)
                                .equals(menu4Name)) {
                            if (tvPipPopManager.isPipModeEnabled() == false)
                                return;
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_SUB_WINDOW);
                            TvChannelManager.getInstance().programDown();
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        if (mActivity.getResources()
                                .getString(R.string.volumeBig)
                                .equals(menu4Name)) {
                            if (tvPipPopManager.isPipModeEnabled() == false)
                                return;
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_SUB_WINDOW);
                            earPhoneVolume = tvAudioManager.getEarPhoneVolume();
                            if (earPhoneVolume < 100) {
                                earPhoneVolume++;
                            }
                            tvAudioManager.setEarPhoneVolume(earPhoneVolume);
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        if (mActivity.getResources()
                                .getString(R.string.volumeSmall)
                                .equals(menu4Name)) {
                            if (tvPipPopManager.isPipModeEnabled() == false)
                                return;
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_SUB_WINDOW);
                            earPhoneVolume = tvAudioManager.getEarPhoneVolume();
                            if (earPhoneVolume > 0) {
                                earPhoneVolume--;
                            }
                            tvAudioManager.setEarPhoneVolume(earPhoneVolume);
                            tvPipPopManager
                                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        if (mActivity.getResources()
                                .getString(R.string.moveScreen)
                                .equals(menu4Name)) {
                            if (tvPipPopManager.isPipModeEnabled() == false)
                                return;
                            isMove = true;
                        }
                        if (mActivity.getResources()
                                .getString(R.string.full_screen)
                                .equals(menu4Name)) {
                            handler.sendEmptyMessageDelayed(COMMAND_SEND_MSG, 0);
                            handler.sendEmptyMessageDelayed(
                                    COMMAND_KILL_DIALOG, 0);
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "keyCode: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mthreadSetPipDisplayFocusWindow.start();
            dismiss();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            int position = MenuViewHolder.menu2GridView
                    .getSelectedItemPosition();
            String menu4Name = (String) MenuViewHolder.menu2GridView
                    .getSelectedItem();
            if (mActivity.getResources().getString(R.string.volumeBig)
                    .equals(menu4Name)) {
                if (tvPipPopManager.isPipModeEnabled() == true) {
                    tvPipPopManager
                            .setPipDisplayFocusWindow(TvPipPopManager.E_SUB_WINDOW);
                    earPhoneVolume = tvAudioManager.getEarPhoneVolume();
                    if (earPhoneVolume < 100) {
                        earPhoneVolume++;
                    }
                    Log.i(TAG, "earPhoneVolume:" + earPhoneVolume);
                    tvAudioManager.setEarPhoneVolume(earPhoneVolume);
                }

            } else if (mActivity.getResources().getString(R.string.volumeSmall)
                    .equals(menu4Name)) {
                if (tvPipPopManager.isPipModeEnabled() == true) {
                    tvPipPopManager
                            .setPipDisplayFocusWindow(TvPipPopManager.E_SUB_WINDOW);
                    earPhoneVolume = tvAudioManager.getEarPhoneVolume();
                    if (earPhoneVolume > 0) {
                        earPhoneVolume--;
                    }
                    Log.i(TAG, "earPhoneVolume:" + earPhoneVolume);
                    tvAudioManager.setEarPhoneVolume(earPhoneVolume);
                }
            }
            tvPipPopManager
                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void redrawPipInputSourceDialog() {
        if (pipMenu3Dialog == null) {
            return;
        }
        if (pipMenu3Dialog.isShowing()) {
            pipMenu3Dialog.dismiss();
            pipMenu3Dialog = null;
            boolean isPip = true;
            if (ChoosePipOrPopDialog.miscMode == MISCMODE.PIP_MODE) {
                isPip = true;
            } else {
                isPip = false;
            }
            tvPipPopManager
                    .setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
            subSrcArrayList = tvPipPopManager.getSubWindowSourceList(isPip);
            pipMenu3Dialog = new PipSubWindowInputSourceDialog(mActivity,
                    R.style.mainMenu, subSrcArrayList);
            pipMenu3Dialog.show();
        }
    }

    public static void checkSubSource() {
        Log.d(TAG,
                "checkSubSource PipSubWindowInputSourceDialog.pipModeEnabled"
                        + PipSubWindowInputSourceDialog.pipModeEnabled
                        + "@PipSubWindowInputSourceDialog.subInputSource"
                        + PipSubWindowInputSourceDialog.subInputSource);
        if (PipSubWindowInputSourceDialog.pipModeEnabled != PIP_MODE_ENABLED.NUM_EANBLED) {
            if (PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_ATV
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_VGA
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_VGA2
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_VGA3
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_CVBS
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_YPBPR
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_YPBPR2
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_YPBPR3
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_HDMI
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_HDMI2
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_HDMI3
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_HDMI4
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_DTV
                    || PipSubWindowInputSourceDialog.subInputSource == TvCommonManager.INPUT_SOURCE_DTV2) {
                // enable the button
                isFullScreenButtonEnable = true;
            } else {
                // disable the button
                isFullScreenButtonEnable = false;
            }
        }
        if (!isFullScreenButtonEnable) {
            MenuViewHolder.menu2GridView.setAdapter(new GridViewAdapter(
                    mActivity, menu4_name));
        } else {
            if (ChoosePipOrPopDialog.miscMode == MISCMODE.PIP_MODE) {
                MenuViewHolder.menu2GridView.setAdapter(new GridViewAdapter(
                        mActivity, menu4_name2));
            } else {
                MenuViewHolder.menu2GridView.setAdapter(new GridViewAdapter(
                        mActivity, menu4_name3));
            }
        }
    }

    private void getDispWinPosition() {
        Log.d(TAG, "getDispWinPosition(), screenPosition = " + screenPosition);
        int position = screenPosition % 3;
        if (position == 0) {
            dispWin.x = 0;
            dispWin.y = 540;
        }
        if (position == 1) {
            dispWin.x = 0;
            dispWin.y = 0;
        }
        if (position == 2) {
            dispWin.x = 1240;
            dispWin.y = 540;
        }
    }
}
