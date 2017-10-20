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

package com.mstar.tv.tvplayer.ui.holder;

import android.app.Activity;
import android.graphics.Color;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.vo.VideoInfo;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.util.Utility;

public class MenuOf3DViewHolder {
    private static final String TAG = "MenuOf3DViewHolder";

    private static final int FRAME_PACKING_720P_VSIZE = 1470;

    private static final int UPDATE_UI_BUTTON = 9999;

    private static final int REFRESH_UI_DELAY_TIME = 300;

    private static final int SELF_DETECT_3DTO2D_RIGHT_NOW = 1;

    protected ComboButton comboBtn3DSelfAdaptiveDetect;

    protected ComboButton comboBtn3dTo2dSelfAdaptiveDetect;

    protected ComboButton comboBtn3DConversion;

    protected ComboButton comboBtn3DTo2D;

    protected ComboButton comboBtn3DDepth;

    protected ComboButton comboBtn3DOffset;

    protected ComboButton comboBtn3DOutputAspect;

    protected ComboButton comboBtnLRViewSwitch;

    protected Activity activity;

    protected TvS3DManager mTvS3DManager = null;

    private Toast mShowInvalid3DToast = null;

    private TvCommonManager mTvCommonManager;

    private boolean mThreeDDepthEnable;

    private MsgHandler mHandler = null;
    class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == UPDATE_UI_BUTTON) {
                updateUI(true);
            }
            super.handleMessage(msg);
        }
    }

    public MenuOf3DViewHolder(Activity act) {
        this.activity = act;
        mTvS3DManager = TvS3DManager.getInstance();
        mTvCommonManager = TvCommonManager.getInstance();
        mThreeDDepthEnable = mTvCommonManager.isSupportModule(TvCommonManager.MODULE_TV_CONFIG_THREED_DEPTH);
        mHandler = new MsgHandler();
    }

    public void findViews() {
        comboBtn3DSelfAdaptiveDetect = new ComboButton(activity, activity.getResources()
                .getStringArray(R.array.str_arr_3d_self_adaptive_detecttriple_vals),
                R.id.linearlayout_3d_self_adaptive_detecttriple, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.setSelfAdaptiveDetectMode(getIdx());
                }

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        comboBtn3DConversion.setIdx(mTvS3DManager.get3dDisplayFormat());
                        updateUI(true);
                    }
                });
            }
        };
        comboBtn3DSelfAdaptiveDetect.setItemEnable(TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_REALTIME, false);

        comboBtn3dTo2dSelfAdaptiveDetect = new ComboButton(activity, activity.getResources()
                .getStringArray(R.array.str_arr_3dto2d_self_adaptive_detecttriple_vals),
                R.id.linearlayout_3dto2d_self_adaptive_detecttriple, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();

                if (mTvS3DManager != null) {
                    mTvS3DManager.set3DTo2DDisplayMode((getIdx() != 0) ? TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_AUTO
                            : TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_NONE);

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            comboBtn3DConversion.setIdx(mTvS3DManager.get3dDisplayFormat());
                            updateUI(true);
                        }
                    });
                }
            }
        };

        comboBtn3DConversion = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_3d_3dconversion_vals), R.id.linearlayout_3d_conversion, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    if ((getIdx() != TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE)
                            && (TvPictureManager.getInstance().getMWEDemoMode() != TvPictureManager.MWE_DEMO_MODE_OFF)) {
                        TvPictureManager.getInstance().setMWEDemoMode(TvPictureManager.MWE_DEMO_MODE_OFF);
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(activity, R.string.str_3d_toast, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    final boolean isValid = mTvS3DManager.set3dDisplayFormat(getIdx());
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            setmItemValueViewColor(Color.WHITE);
                            if (false == isValid) {
                                setmItemValueViewColor(Color.GRAY);
                                if (null == mShowInvalid3DToast) {
                                    mShowInvalid3DToast = Toast.makeText(activity,
                                            R.string.str_invalid_3d_format, Toast.LENGTH_SHORT);
                                }
                                mShowInvalid3DToast.cancel();
                                mShowInvalid3DToast.show();
                                updateUI(false);
                            } else {
                                updateUI(true);
                            }
                        }
                    });
                }
            }
        };

        comboBtn3DTo2D = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_3d_3dto2d_vals), R.id.linearlayout_3d_3dto2d, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.set3DTo2DDisplayMode(getIdx());
                }
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (getIdx() != TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_NONE) {
                            comboBtn3DConversion.setIdx(0); // 0 for off
                        }
                        updateUI(true);
                    }
                });
            }
        };

        comboBtn3DDepth = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_3d_3ddepth_vals), R.id.linearlayout_3d_3ddepth, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.set3DDepthMode(comboBtn3DDepth.getIdx());
                }
            }
        };

        if (mThreeDDepthEnable == false) {
            comboBtn3DDepth.setVisibility(false);
        }
        comboBtn3DOffset = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_3d_3doffset_vals), R.id.linearlayout_3d_3doffset, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.set3DOffsetMode(comboBtn3DOffset.getIdx());
                }
            }
        };
        comboBtn3DOutputAspect = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_3d_3doutputaspect_vals), R.id.linearlayout_3d_3doutputaspect, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager
                            .setThreeDVideoOutputAspectMode(comboBtn3DOutputAspect.getIdx());
                }
            }
        };
        comboBtn3DOutputAspect.setVisibility(false);
        comboBtnLRViewSwitch = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_3d_lrswitch_vals), R.id.linearlayout_3d_lrswitch, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager
                            .setThreeDVideoLrViewSwitch(comboBtnLRViewSwitch.getIdx());
                }
            }
        };
    }

    public void LoadDataToUI() {
        //Refine performance with query DB by content provider to reduce startup time in 3D page.
        int inputSrcType = mTvCommonManager.getCurrentTvInputSource();

        Cursor cursorHdmi = this.activity.getApplicationContext().getContentResolver().query(
            Uri.parse("content://mstar.tv.usersetting/threedvideomode/inputsrc/"
                        + TvCommonManager.INPUT_SOURCE_HDMI), null, null, null, null);
        int threeDSelfdetectHdmi = 0;
        if (cursorHdmi.moveToFirst()) {
            threeDSelfdetectHdmi = cursorHdmi.getInt(cursorHdmi.getColumnIndex("eThreeDVideoSelfAdaptiveDetect"));
        }
        cursorHdmi.close();

        Cursor cursor = this.activity.getApplicationContext().getContentResolver().query(
            Uri.parse("content://mstar.tv.usersetting/threedvideomode/inputsrc/"
                        + inputSrcType), null, null, null, null);
        if (cursor.moveToFirst()) {
            int threeDSelfdectect = cursor.getInt(cursor.getColumnIndex("eThreeDVideoSelfAdaptiveDetect"));
            comboBtn3DSelfAdaptiveDetect.setIdx(threeDSelfdectect);
            if (threeDSelfdetectHdmi == TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_OFF) {
                comboBtn3DConversion.setIdx(cursor.getInt(cursor.getColumnIndex("eThreeDVideoDisplayFormat")));
            } else {
                comboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO);
            }
            comboBtn3DTo2D.setIdx(cursor.getInt(cursor.getColumnIndex("eThreeDVideo3DTo2D")));
            comboBtn3DDepth.setIdx(cursor.getInt(cursor.getColumnIndex("eThreeDVideo3DDepth")));
            comboBtn3DOffset.setIdx(cursor.getInt(cursor.getColumnIndex("eThreeDVideo3DOffset")));
            comboBtn3DOutputAspect.setIdx(cursor.getInt(cursor.getColumnIndex("eThreeDVideo3DOutputAspect")));
        }
        cursor.close();
        comboBtn3dTo2dSelfAdaptiveDetect.setIdx(0);
        /*  Remove the 3D conversion, 3D-2D "automatic",
         *  3D converted automatically by the self-test instead,
         *  3D-2D are automatically transferred to the 3D-2D self-test.
        */
        comboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO, false);
        comboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_AUTO, false);
        comboBtnLRViewSwitch.setIdx(mTvS3DManager.getThreeDVideoLrViewSwitch());
        updateUI(true);
        // FIXME: need to remove delay timer
        mHandler.sendEmptyMessageDelayed(UPDATE_UI_BUTTON, REFRESH_UI_DELAY_TIME);
        Utility.setDefaultFocus((LinearLayout) activity.findViewById(R.id.linearlayout_3d_menu));
    }

    public void updateUI(boolean isUpdate3DUI) {
        final int selfDetect = mTvS3DManager.getSelfAdaptiveDetectMode();
        final int displayFormat3d = mTvS3DManager.get3dDisplayFormat();
        final int displayFormat3dTo2d = mTvS3DManager.get3DTo2DDisplayMode();
        final boolean is3dTo2dStatus = mTvS3DManager.get3dTo2dStatus();
        final boolean is2dTo3d = (TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_2DTO3D == displayFormat3d) ? true
                : false;
        final int current3dType = mTvS3DManager.getCurrent3dType();
        final VideoInfo videoInfo = TvPictureManager.getInstance().getVideoInfo();

        Log.d(TAG, "selfDetect: " + selfDetect);
        Log.d(TAG, "displayFormat3d: " + displayFormat3d);
        Log.d(TAG, "displayFormat3dTo2d: " + displayFormat3dTo2d);
        Log.d(TAG, "is2dTo3d: " + is2dTo3d);
        Log.d(TAG, "current3dType: " + current3dType);
        Log.d(TAG, "vResolution:" + videoInfo.vResolution);
        Log.d(TAG, "is3dTo2dStatus:" + is3dTo2dStatus);
        if (TvPictureManager.getInstance().getMWEDemoMode() == TvPictureManager.MWE_DEMO_MODE_OFF) {
            if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE
                    && current3dType == TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P
                    && videoInfo.vResolution == FRAME_PACKING_720P_VSIZE) {
                comboBtn3DSelfAdaptiveDetect.setEnable(false);
                comboBtn3DConversion.setEnable(false);
            } else if (selfDetect != TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_OFF) {
                updateSelfDetectBtn();
                comboBtn3dTo2dSelfAdaptiveDetect.setEnable(false);
                comboBtn3DConversion.setEnable(false);
                comboBtn3DTo2D.setEnable(false);
            } else if (displayFormat3d != TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE) {
                comboBtn3DSelfAdaptiveDetect.setEnable(false);
                comboBtn3dTo2dSelfAdaptiveDetect.setEnable(false);
                comboBtn3DConversion.setEnable(true);
                comboBtn3DTo2D.setEnable(false);
            } else if (displayFormat3dTo2d != TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_NONE) {
                if (displayFormat3dTo2d != TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_AUTO) {
                    comboBtn3dTo2dSelfAdaptiveDetect.setEnable(false);
                    comboBtn3DTo2D.setEnable(true);
                } else {
                    comboBtn3dTo2dSelfAdaptiveDetect.setEnable(true);
                    comboBtn3DTo2D.setEnable(false);
                }
                comboBtn3DSelfAdaptiveDetect.setEnable(false);
                comboBtn3DConversion.setEnable(false);
            } else {
                updateSelfDetectBtn();
                comboBtn3dTo2dSelfAdaptiveDetect.setEnable(true);
                comboBtn3DConversion.setEnable(true);
                comboBtn3DTo2D.setEnable(true);
            }
            if (comboBtn3dTo2dSelfAdaptiveDetect.getIdx() == SELF_DETECT_3DTO2D_RIGHT_NOW) {
                comboBtn3DSelfAdaptiveDetect.setEnable(false);
                comboBtn3dTo2dSelfAdaptiveDetect.setEnable(true);
                comboBtn3DTo2D.setEnable(false);
                comboBtn3DConversion.setEnable(false);
            }
            if (displayFormat3d == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE
                    && current3dType == TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) {
                update2dMode();
            } else if ((displayFormat3d == TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_AUTO)
                    && (current3dType == TvS3DManager.THREE_DIMENSIONS_TYPE_NONE)) {
                update2dMode();
            } else if (true == is3dTo2dStatus) {
                update2dMode();
            } else {
                if (true == isUpdate3DUI) {
                    update3dMode(is2dTo3d);
                }
            }
        } else {
            Log.d(TAG, "disable all items in demo mode");
            comboBtn3DSelfAdaptiveDetect.setEnable(false);
            comboBtn3DConversion.setEnable(false);
            comboBtn3DTo2D.setEnable(false);
            comboBtn3DDepth.setEnable(false);
            comboBtn3DOffset.setEnable(false);
            comboBtn3DOutputAspect.setEnable(false);
            comboBtnLRViewSwitch.setEnable(false);
        }
    }

    private void update2dMode() {
        comboBtnLRViewSwitch.setEnable(false);
        comboBtn3DDepth.setEnable(false);
        comboBtn3DOffset.setEnable(false);
    }

    private void update3dMode(boolean is2dTo3D) {
        comboBtnLRViewSwitch.setEnable(true);
        comboBtn3DDepth.setEnable(true);
        comboBtn3DOffset.setEnable(is2dTo3D);
    }

    private void updateSelfDetectBtn() {
        if (TvChannelManager.getInstance().isSignalStabled()) {
            comboBtn3DSelfAdaptiveDetect.setEnable(true);
        } else {
            comboBtn3DSelfAdaptiveDetect.setEnable(false);
        }
    }
}
