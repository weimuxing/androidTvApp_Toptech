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

import java.util.ArrayList;

import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureDeviceType;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureSource;
import com.mstar.android.tvapi.common.vo.EnumScreenMuteType;
import com.mstar.android.tvapi.common.vo.PanelProperty;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.miscsetting.activity.MainActivity;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog.MISCMODE;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog.OSDTHREEDMODE;
import com.mstar.miscsetting.dialog.PipSubWindowSwitchVolDialog.SWITCH_VOL;
import com.mstar.miscsetting.hoilder.MainMenuHolder;
import com.mstar.miscsetting.service.PipService;
import com.mstar.miscsetting.R;
import com.mstar.util.Tools;
import com.mstar.util.Utility;
import com.mstar.android.MDisplay;
import com.mstar.miscsetting.activity.display.util.TvDispAreaUtil;
import com.mstar.miscsetting.activity.display.vo.ResolutionVO;

import android.content.DialogInterface;
import android.graphics.Point;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import com.mstar.miscsetting.activity.display.vo.ResolutionVO;
import com.mstar.util.Tools;

public class PipSubWindowInputSourceDialog extends Dialog {
    public enum PIP_MODE_ENABLED {
        // / Pip mode
        PIP_ENABLED,
        // / Pop mode
        POP_ENABLED,
        // / dual mode
        DUAL_ENABLED,
        // / mode num
        NUM_EANBLED
    }

    private static final String TAG = "PipSubWindowInputSourceDialog";

    private Context mActivity;

    private ArrayList<PipSubWindowInputSourceItem> data = new ArrayList<PipSubWindowInputSourceItem>();

    private ListView inputSourceListView;

    private PipSubWindowInputSourceAdapter adapter = null;

    private TvPipPopManager tvPipPopManager;

    private int[] mIntArrayList;

    private int mIntArrayListSize;

    private int position = -1;

    public static int subInputSource = TvCommonManager.INPUT_SOURCE_ATV;

    public static int mainInputSrc = TvCommonManager.INPUT_SOURCE_NUM;

    public static VideoWindowType mainWin = null;

    public static VideoWindowType subWin = null;

    private static TvPictureManager pm = null;

    private static PanelProperty pp = null;

    public static PIP_MODE_ENABLED pipModeEnabled = PIP_MODE_ENABLED.NUM_EANBLED;

    private static TvCommonManager mTvCommonManager = null;

    private String[] mTvSourceName;

    public PipSubWindowInputSourceDialog(Context context, int style,
            int[] intArrayList) {
        super(context, style);
        mActivity = context;
        tvPipPopManager = TvPipPopManager.getInstance();
        mIntArrayList = intArrayList;
    }

    private Handler handler = new Handler();
    private Runnable runCheckSubSource = new Runnable() {

        @Override
        public void run() {
            PipMenuDialog4Scene.checkSubSource();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(), subInputSource = " + subInputSource);
        setContentView(R.layout.subwindow_inputsource_list);
        mTvSourceName = mActivity.getResources().getStringArray(
                R.array.str_arr_input_source_vals);
        mainWin = new VideoWindowType();
        subWin = new VideoWindowType();
        pm = TvPictureManager.getInstance();
        pp = pm.getPanelWidthHeight();
        mTvCommonManager = TvCommonManager.getInstance();

        mainWin.x = subWin.x = 0;
        mainWin.y = subWin.y = 0;
        mainWin.width = subWin.width = pp.width;
        mainWin.height = subWin.height = pp.height;
        Window w = getWindow();

        // Get the default display the data
        Display display = w.getWindowManager().getDefaultDisplay();

        // The window's title is empty
        w.setTitle(null);

        // Definition of window width and height
        Point point = new Point();
        display.getSize(point);
        int width = (int) (point.x * 0.2);
        int height = (int) (point.y * 0.9);

        // Settings window properties
        w.setLayout(width, height);

        w.setGravity(Gravity.CENTER_HORIZONTAL);

        // Settings window properties
        WindowManager.LayoutParams wl = w.getAttributes();
        wl.x = 100;
        w.setAttributes(wl);
        mainInputSrc = MainMenuHolder.tvCommonManager.getCurrentTvInputSource();
        Init();
    }

    public void Init() {
        inputSourceListView = (ListView) findViewById(R.id.ListView_subwindow_inputsource);
        for (int i = 0; i < mIntArrayList.length; i++) {
            setItemText(mIntArrayList[i]);
        }
        adapter = new PipSubWindowInputSourceAdapter(this, data);
        inputSourceListView.setAdapter(adapter);
        inputSourceListView.setDividerHeight(0);
        setListener();
    }

    private void freshUI() {
        mIntArrayList = tvPipPopManager
                .getSubWindowSourceList(ChoosePipOrPopDialog.miscMode == MISCMODE.PIP_MODE);
        data.clear();
        position = -1;
        for (int i = 0; i < mIntArrayList.length; i++) {
            setItemText(mIntArrayList[i]);
        }
        adapter.notifyDataSetChanged();
    }

    public void setItemText(int oridinal) {
        position++;
        PipSubWindowInputSourceItem inputSrcItem = new PipSubWindowInputSourceItem();
        if (TvCommonManager.INPUT_SOURCE_VGA == oridinal) {
            inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
        } else if (TvCommonManager.INPUT_SOURCE_VGA2 == oridinal) {
            inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
        } else if (TvCommonManager.INPUT_SOURCE_VGA3 == oridinal) {
            inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
        } else if (TvCommonManager.INPUT_SOURCE_ATV == oridinal) {
            if (mainInputSrc != TvCommonManager.INPUT_SOURCE_DTV)
                inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
            else {
                return;
            }
        } else if (TvCommonManager.INPUT_SOURCE_CVBS == oridinal) {
            inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
        } else if (TvCommonManager.INPUT_SOURCE_YPBPR == oridinal) {
            inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
        } else if (TvCommonManager.INPUT_SOURCE_HDMI == oridinal) {
            inputSrcItem.setInputSourceName(Tools.getHdmiDispStr(TvCommonManager.INPUT_SOURCE_HDMI));
        } else if (TvCommonManager.INPUT_SOURCE_HDMI2 == oridinal) {
            inputSrcItem.setInputSourceName(Tools.getHdmiDispStr(TvCommonManager.INPUT_SOURCE_HDMI2));
        } else if (TvCommonManager.INPUT_SOURCE_HDMI3 == oridinal) {
            inputSrcItem.setInputSourceName(Tools.getHdmiDispStr(TvCommonManager.INPUT_SOURCE_HDMI3));
        } else if (TvCommonManager.INPUT_SOURCE_HDMI4 == oridinal) {
            inputSrcItem.setInputSourceName(Tools.getHdmiDispStr(TvCommonManager.INPUT_SOURCE_HDMI4));
        } else if (TvCommonManager.INPUT_SOURCE_DTV == oridinal) {
            if ((ChoosePipOrPopDialog.miscMode == MISCMODE.PIP_MODE)
                    && tvPipPopManager.checkPipSupport(mainInputSrc,
                            TvCommonManager.INPUT_SOURCE_DTV)
                    || (ChoosePipOrPopDialog.miscMode != MISCMODE.PIP_MODE)
                    && tvPipPopManager.checkPopSupport(mainInputSrc,
                            TvCommonManager.INPUT_SOURCE_DTV))
                inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
            else {
                return;
            }
        } else if (TvCommonManager.INPUT_SOURCE_DTV2 == oridinal) {
            if ((ChoosePipOrPopDialog.miscMode == MISCMODE.PIP_MODE)
                    && tvPipPopManager.checkPipSupport(mainInputSrc,
                            TvCommonManager.INPUT_SOURCE_DTV2)
                    || (ChoosePipOrPopDialog.miscMode != MISCMODE.PIP_MODE)
                    && tvPipPopManager.checkPopSupport(mainInputSrc,
                            TvCommonManager.INPUT_SOURCE_DTV2))
                inputSrcItem.setInputSourceName(mTvSourceName[oridinal]);
            else {
                return;
            }
        } else {
            return;
        }
        inputSrcItem.setPositon(position);
        data.add(inputSrcItem);
    }

    private void setListener() {
        inputSourceListView
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int position, long id) {
                    }
                });
        inputSourceListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                    int position, long arg3) {
                int currentSubInputSource = mTvCommonManager.getCurrentTvSubInputSource();
                int subsrc = mIntArrayList[data.get(position).getPositon()];
                int pipReturn;
                int popReturn;
                boolean dualViewReturn;
                boolean needMuteScreen = false;
                subInputSource = subsrc;

                Log.i(TAG,
                        "mainInputSource = " + mainInputSrc);
                Log.i(TAG,
                        "subInputSource = " + subInputSource);
                Log.i(TAG,
                        "currentSubInputSource = " + currentSubInputSource);

                if (subInputSource != TvCommonManager.INPUT_SOURCE_NONE) {
                    // no change
                    if (subInputSource == currentSubInputSource
                            && Utility.getCurrentPipMode() == ChoosePipOrPopDialog.miscMode.ordinal()
                            && mainInputSrc != TvCommonManager.INPUT_SOURCE_STORAGE
                            && tvPipPopManager.isPipModeEnabled()) {
                        Log.d(TAG, "PIP no change just return, don't open PIP");
                        return;
                    }
                    switch (ChoosePipOrPopDialog.miscMode) {
                    case PIP_MODE:
                        if (pipModeEnabled == PIP_MODE_ENABLED.POP_ENABLED
                                || pipModeEnabled == PIP_MODE_ENABLED.DUAL_ENABLED) {
                            TvCommonManager.getInstance().setVideoMute(true,
                                        TvCommonManager.SCREEN_MUTE_BLACK, 0,
                                        mainInputSrc);
                            needMuteScreen = true;
                        }

                        if (pipModeEnabled == PIP_MODE_ENABLED.POP_ENABLED) {
                            tvPipPopManager.disablePOP(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        if (pipModeEnabled == PIP_MODE_ENABLED.DUAL_ENABLED) {
                            tvPipPopManager.disable3dDualView();
                        }

                        Log.i(TAG,"x:" + PipMenuDialog4Scene.dispWin.x);
                        Log.i(TAG,"y:" + PipMenuDialog4Scene.dispWin.y);
                        Log.i(TAG,"w:" + PipMenuDialog4Scene.dispWin.width);
                        Log.i(TAG,"h:" + PipMenuDialog4Scene.dispWin.height);
                        pipReturn = tvPipPopManager.enablePipTv(mainInputSrc,
                                subInputSource, PipMenuDialog4Scene.dispWin);
                        if (true == needMuteScreen) {
                            TvCommonManager.getInstance().setVideoMute(false,
                                        TvCommonManager.SCREEN_MUTE_BLACK, 0,
                                        mainInputSrc);
                        }
                        if (pipReturn != TvPipPopManager.E_PIP_NOT_SUPPORT) {
                            swithVol2Sub();
                            tvPipPopManager.setPipOnFlag(true);
                            pipModeEnabled = PIP_MODE_ENABLED.PIP_ENABLED;
                            MainActivity.mMainMenuHolder.closeMHL();
                        } else {
                            pipErrPrompt();
                            return;
                        }
                        break;
                    case POP_MODE:
                        if (pipModeEnabled == PIP_MODE_ENABLED.PIP_ENABLED
                                || pipModeEnabled == PIP_MODE_ENABLED.DUAL_ENABLED) {
                            TvCommonManager.getInstance().setVideoMute(true,
                                        TvCommonManager.SCREEN_MUTE_BLACK, 0,
                                        mainInputSrc);
                            needMuteScreen = true;
                        }
                        if (pipModeEnabled == PIP_MODE_ENABLED.PIP_ENABLED) {
                            tvPipPopManager.disablePIP(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        if (pipModeEnabled == PIP_MODE_ENABLED.DUAL_ENABLED) {
                            tvPipPopManager.disable3dDualView();
                        }
                        if (needMuteScreen) {
                            TvCommonManager.getInstance().setVideoMute(true,
                                        TvCommonManager.SCREEN_MUTE_BLACK, 0,
                                        mainInputSrc);
                        }
                        popReturn = tvPipPopManager.enablePopTv(mainInputSrc,
                                subInputSource);
                        if (true == needMuteScreen) {
                            TvCommonManager.getInstance().setVideoMute(false,
                                        TvCommonManager.SCREEN_MUTE_BLACK, 0,
                                        mainInputSrc);
                        }
                        if (popReturn != TvPipPopManager.E_PIP_NOT_SUPPORT) {
                            pipModeEnabled = PIP_MODE_ENABLED.POP_ENABLED;
                            MainActivity.mMainMenuHolder.closeMHL();
                            swithVol2Sub();
                        } else {
                            popErrPrompt();
                            return;
                        }
                        break;
                    case DUAL_MODE:
                        if (pipModeEnabled == PIP_MODE_ENABLED.PIP_ENABLED
                                || pipModeEnabled == PIP_MODE_ENABLED.POP_ENABLED) {
                            TvCommonManager.getInstance().setVideoMute(true,
                                        TvCommonManager.SCREEN_MUTE_BLACK, 0,
                                        mainInputSrc);
                            needMuteScreen = true;
                        }
                        if (pipModeEnabled == PIP_MODE_ENABLED.PIP_ENABLED) {
                            tvPipPopManager.disablePIP(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        if (pipModeEnabled == PIP_MODE_ENABLED.POP_ENABLED) {
                            tvPipPopManager.disablePOP(TvPipPopManager.E_MAIN_WINDOW);
                        }
                        dualViewReturn = tvPipPopManager.enable3dDualView(
                                mainInputSrc, subInputSource, mainWin, subWin);
                        if (true == needMuteScreen) {
                            TvCommonManager.getInstance().setVideoMute(false,
                                        TvCommonManager.SCREEN_MUTE_BLACK, 0,
                                        mainInputSrc);
                        }
                        if (dualViewReturn == true) {
                            pipModeEnabled = PIP_MODE_ENABLED.DUAL_ENABLED;
                            MainActivity.mMainMenuHolder.closeMHL();
                            swithVol2Sub();
                        } else {
                            dualErrPrompt();
                            return;
                        }
                        break;
                    default:
                        break;
                    }
                    MainMenuHolder.setMiscMode(ChoosePipOrPopDialog.miscMode);
                    handler.removeCallbacks(runCheckSubSource);
                    /* the interface that get the stable state of sub source is
                     * clumsy ,so delay to call
                     */
                    handler.postDelayed(runCheckSubSource, 800);
                }
            }
        });
    }

    private void swithVol2Sub() {
        if (PipService.volStatus != SWITCH_VOL.BT_HEADSET_SUB) {
            TvAudioManager
                        .getInstance()
                        .setAudioCaptureSource(
                                TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                                TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
            Tools.toastShow(R.string.switch_sub, mActivity);
            PipService.volStatus = SWITCH_VOL.BT_HEADSET_SUB;
        }
    }

    private void pipErrPrompt() {
        new AlertDialog.Builder(mActivity)
                .setTitle(R.string.str_pip_err_title)
                .setMessage(R.string.str_pip_err_msg)
                .setPositiveButton(R.string.str_ok_label,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            }
                        }).show();
    }

    private void popErrPrompt() {
        new AlertDialog.Builder(mActivity)
                .setTitle(R.string.str_pop_err_title)
                .setMessage(R.string.str_pop_err_msg)
                .setPositiveButton(R.string.str_ok_label,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            }
                        }).show();
    }

    private void dualErrPrompt() {
        new AlertDialog.Builder(mActivity)
                .setTitle(R.string.str_dual_err_title)
                .setMessage(R.string.str_dual_err_msg)
                .setPositiveButton(R.string.str_ok_label,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            }
                        }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.i(TAG, "keyCode:" + keyCode);

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mIntArrayList = null;
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        freshUI();
        super.onWindowFocusChanged(hasFocus);
    }
}
