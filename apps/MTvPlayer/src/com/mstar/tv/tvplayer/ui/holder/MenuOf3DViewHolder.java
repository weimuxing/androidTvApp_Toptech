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
import com.mstar.tv.tvplayer.ui.component.CycleScrollView;
import com.mstar.util.Utility;

public class MenuOf3DViewHolder {
    private static final String TAG = "MenuOf3DViewHolder";

    private static final int FRAME_PACKING_720P_VSIZE = 1470;

    private static final int UPDATE_UI_BUTTON = 9999;

    private static final int REFRESH_UI_DELAY_TIME = 300;

    private static final int SELF_DETECT_3DTO2D_RIGHT_NOW = 1;

    protected ComboButton mComboBtn3DSelfAdaptiveDetect;

    protected ComboButton mmComboBtn3DTo2DSelfAdaptiveDetect;

    protected ComboButton mComboBtn3DConversion;

    protected ComboButton mComboBtn3DTo2D;

    protected ComboButton mComboBtn3DDepth;

    protected ComboButton mComboBtn3DOffset;

    protected ComboButton mComboBtnLRViewSwitch;

    protected CycleScrollView mScrollView;

    protected Activity mActivity;

    protected TvS3DManager mTvS3DManager = null;

    private Toast mShowInvalid3DToast = null;

    private TvCommonManager mTvCommonManager;

    private boolean mThreeDDepthEnable;

    private boolean mIsHDREnabled = false;

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
        this.mActivity = act;
        mTvS3DManager = TvS3DManager.getInstance();
        mTvCommonManager = TvCommonManager.getInstance();
        mThreeDDepthEnable = mTvCommonManager
                .isSupportModule(TvCommonManager.MODULE_TV_CONFIG_THREED_DEPTH);
        mHandler = new MsgHandler();
    }

    public void findViews() {
        mScrollView = (CycleScrollView) mActivity.findViewById(R.id.cyclescrollview_3d_scroll_view);
        mComboBtn3DSelfAdaptiveDetect = new ComboButton(mActivity, mActivity.getResources()
                .getStringArray(R.array.str_arr_3d_self_adaptive_detecttriple_vals),
                R.id.linearlayout_3d_self_adaptive_detecttriple, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.setSelfAdaptiveDetectMode(getIdx());
                }

                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        mComboBtn3DConversion.setIdx(mTvS3DManager.getCurrent3dType());
                        updateUI(true);
                    }
                });
            }
        };
        mComboBtn3DSelfAdaptiveDetect.setItemEnable(
                TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_REALTIME, false);

        mmComboBtn3DTo2DSelfAdaptiveDetect = new ComboButton(mActivity, mActivity.getResources()
                .getStringArray(R.array.str_arr_3dto2d_self_adaptive_detecttriple_vals),
                R.id.linearlayout_3dto2d_self_adaptive_detecttriple, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();

                if (mTvS3DManager != null) {
                    if (TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SELF_ADAPTIVE_DETECT_RIGHT_NOW == getIdx()) {
                        final int DETECT_3DFORMAT_TIMES = 3;
                        for (int i = 0; i < DETECT_3DFORMAT_TIMES; i++) {
                            int type = mTvS3DManager.detect3DFormat(1);
                            if (TvS3DManager.THREE_DIMENSIONS_TYPE_NONE != type) {
                                mTvS3DManager.enable3DTo2D(type);
                                break;
                            }
                        }
                    } else {
                        mTvS3DManager.enable3DTo2D(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            mComboBtn3DConversion.setIdx(mTvS3DManager.getCurrent3dType());
                            updateUI(true);
                        }
                    });
                }
            }
        };

        mComboBtn3DConversion = new ComboButton(mActivity, mActivity.getResources().getStringArray(
                R.array.str_arr_3d_3dconversion_vals), R.id.linearlayout_3d_conversion, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    if ((getIdx() != TvS3DManager.THREE_DIMENSIONS_TYPE_NONE)
                            && (TvPictureManager.getInstance().getMWEDemoMode() != TvPictureManager.MWE_DEMO_MODE_OFF)) {
                        TvPictureManager.getInstance().setMWEDemoMode(
                                TvPictureManager.MWE_DEMO_MODE_OFF);
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(mActivity, R.string.str_3d_toast, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }

                    final boolean isValid = mTvS3DManager.enable3D(getIdx());
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            setmItemValueViewColor(Color.WHITE);
                            if (false == isValid) {
                                setmItemValueViewColor(Color.GRAY);
                                if (null != mShowInvalid3DToast) {
                                    mShowInvalid3DToast.cancel();
                                }
                                mShowInvalid3DToast = Toast.makeText(mActivity,
                                        R.string.str_invalid_3d_format, Toast.LENGTH_SHORT);
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

        mComboBtn3DTo2D = new ComboButton(mActivity, mActivity.getResources().getStringArray(
                R.array.str_arr_3d_3dto2d_vals), R.id.linearlayout_3d_3dto2d, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.enable3DTo2D(getIdx());
                }
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        if (getIdx() != TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) {
                            mComboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
                        }
                        updateUI(true);
                    }
                });
            }
        };

        mComboBtn3DDepth = new ComboButton(mActivity, mActivity.getResources().getStringArray(
                R.array.str_arr_3d_3ddepth_vals), R.id.linearlayout_3d_3ddepth, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.set3DGain(mComboBtn3DDepth.getIdx());
                }
            }
        };

        if (mThreeDDepthEnable == false) {
            mComboBtn3DDepth.setVisibility(false);
        }
        mComboBtn3DOffset = new ComboButton(mActivity, mActivity.getResources().getStringArray(
                R.array.str_arr_3d_3doffset_vals), R.id.linearlayout_3d_3doffset, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.set3DOffset(mComboBtn3DOffset.getIdx());
                }
            }
        };

        mComboBtnLRViewSwitch = new ComboButton(mActivity, mActivity.getResources().getStringArray(
                R.array.str_arr_3d_lrswitch_vals), R.id.linearlayout_3d_lrswitch, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (mTvS3DManager != null) {
                    mTvS3DManager.set3DLrViewSwitch(mComboBtnLRViewSwitch.getIdx());
                }
            }
        };
    }

    public void LoadDataToUI() {
        // Refine performance with query DB by content provider to reduce
        // startup time in 3D page.
        int inputSrcType = mTvCommonManager.getCurrentTvInputSource();
        Cursor cursor = this.mActivity
                .getApplicationContext()
                .getContentResolver()
                .query(Uri.parse("content://mstar.tv.usersetting/threedvideomode/inputsrc/"
                        + inputSrcType), null, null, null, null);
        if (cursor.moveToFirst()) {
            int threeDSelfdectect = cursor.getInt(cursor
                    .getColumnIndex("eThreeDVideoSelfAdaptiveDetect"));
            mComboBtn3DSelfAdaptiveDetect.setIdx(threeDSelfdectect);
        }
        cursor.close();
        final int current3DFormat = mTvS3DManager.getCurrent3dType();
        final boolean is3DTo2D = mTvS3DManager.get3dTo2dStatus();
        mmComboBtn3DTo2DSelfAdaptiveDetect
                .setIdx(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SELF_ADAPTIVE_DETECT_OFF);
        if (true == is3DTo2D) {
            mComboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
            mComboBtn3DTo2D.setIdx(current3DFormat);
        } else {
            mComboBtn3DConversion.setIdx(current3DFormat);
            mComboBtn3DTo2D.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
        }
        updateDepthAndOffset();
        mComboBtnLRViewSwitch.setIdx(mTvS3DManager.get3DLrViewSwitch());

        hideUnsupportItems();

        updateUI(true);

        // FIXME: need to remove delay timer
        mHandler.sendEmptyMessageDelayed(UPDATE_UI_BUTTON, REFRESH_UI_DELAY_TIME);
        Utility.setDefaultFocus((LinearLayout) mActivity.findViewById(R.id.linearlayout_3d_menu));
    }

    private void hideUnsupportItems() {
        /* disable not supported items */
        mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P,
                false);
        mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FIELD_ALTERNATIVE,
                false);
        mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW, false);
        mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_4K2K_VIDEO_SPLIT,
                false);
        mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_L, false);
        mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_R, false);

        /* disable not supported items */
        mComboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P, false);
        mComboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FIELD_ALTERNATIVE, false);
        mComboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_2DTO3D, false);
        mComboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW, false);
        mComboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_4K2K_VIDEO_SPLIT, false);
        mComboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_L, false);
        mComboBtn3DTo2D.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_R, false);
    }

    public void pageOnFocus() {
        TvCommonManager.getInstance().speakTtsDelayed(
                mActivity.getApplicationContext().getString(R.string.str_3d_3d),
                TvCommonManager.TTS_QUEUE_FLUSH, TvCommonManager.TTS_SPEAK_PRIORITY_HIGH,
                TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
        mScrollView.ttsSpeakFocusItem();
    }

    public void refreshUI(boolean isHDREnabled) {
        mIsHDREnabled = isHDREnabled;
        enableMenuItemForHDR(isHDREnabled);
        updateUI(true);
    }

    private void enableMenuItemForHDR(boolean isHDREnabled) {
        if (true == isHDREnabled) {
            mComboBtn3DSelfAdaptiveDetect.setEnable(false);
            mmComboBtn3DTo2DSelfAdaptiveDetect.setEnable(false);
            // mComboBtn3DConversion.setEnable(false);
            mComboBtn3DTo2D.setEnable(false);
            mComboBtn3DDepth.setEnable(false);
            mComboBtn3DOffset.setEnable(false);
            mComboBtnLRViewSwitch.setEnable(false);

            mComboBtn3DConversion.setEnable(true);
            int len = mComboBtn3DConversion.getItemLength();
            for (int i = 0; i < len; i++) {
                mComboBtn3DConversion.setItemEnable(i, false);
            }
            /* Only "None" and "2DTo3D" are supported, if HDR is enabled. */
            mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE, true);
            mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_2DTO3D, true);
        } else {
            int len = mComboBtn3DConversion.getItemLength();
            for (int i = 0; i < len; i++) {
                mComboBtn3DConversion.setItemEnable(i, true);
            }
        }
        hideUnsupportItems();
    }

    private void updateUI(boolean isUpdate3DUI) {
        boolean is3DOffestSupported = false;
        final int _3DSelfDetectMode = mTvS3DManager.getSelfAdaptiveDetectMode();
        final int current3DFormat = mTvS3DManager.getCurrent3dType();
        final boolean is2DTo3D = (current3DFormat == TvS3DManager.THREE_DIMENSIONS_TYPE_2DTO3D) ? true
                : false;
        final boolean is3DTo2D = mTvS3DManager.get3dTo2dStatus();
        final boolean is3DAutodetectSupported = mTvS3DManager
                .query3dCapability(TvS3DManager.THREE_DIMENSIONS_3D_ITEM_AUTODETECT);
        if (is2DTo3D) {
            /* 3Doffset is dependent on HW capability. */
            is3DOffestSupported = mTvS3DManager
                    .query3dCapability(TvS3DManager.THREE_DIMENSIONS_3D_ITEM_OFFSET);
        }
        final VideoInfo videoInfo = TvPictureManager.getInstance().getVideoInfo();
        Log.d(TAG, "3DSelfDetect: " + _3DSelfDetectMode);
        Log.d(TAG, "current3DFormat: " + current3DFormat);
        Log.d(TAG, "is2DTo3D: " + is2DTo3D);
        Log.d(TAG, "is3DTo2D: " + is3DTo2D);
        Log.d(TAG, "is3DOffestSupported:" + is3DOffestSupported);
        Log.d(TAG, "vResolution:" + videoInfo.vResolution);
        if (TvPictureManager.getInstance().getMWEDemoMode() == TvPictureManager.MWE_DEMO_MODE_OFF) {
            if (true == mIsHDREnabled) {
                if (is2DTo3D) {
                    mComboBtn3DDepth.setEnable(true);
                    mComboBtn3DOffset.setEnable(is3DOffestSupported);
                } else {
                    mComboBtn3DDepth.setEnable(false);
                    mComboBtn3DOffset.setEnable(false);
                }
            } else if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE
                    && current3DFormat == TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P
                    && videoInfo.vResolution == FRAME_PACKING_720P_VSIZE) {
                mComboBtn3DSelfAdaptiveDetect.setEnable(false);
                mComboBtn3DConversion.setEnable(false);
            } else if (_3DSelfDetectMode != TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_OFF) {
                /* for 3D self detection */
                updateSelfDetectBtn();
                mmComboBtn3DTo2DSelfAdaptiveDetect.setEnable(false);
                mComboBtn3DConversion.setEnable(false);
                mComboBtn3DTo2D.setEnable(false);
            } else if (current3DFormat != TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) {
                if (false == is3DTo2D) {
                    /* for 3D Conversion */
                    mComboBtn3DSelfAdaptiveDetect.setEnable(false);
                    mmComboBtn3DTo2DSelfAdaptiveDetect.setEnable(false);
                    mComboBtn3DConversion.setEnable(true);
                    mComboBtn3DTo2D.setEnable(false);
                } else {
                    /* for 3D-2D Conversion */
                    mComboBtn3DSelfAdaptiveDetect.setEnable(false);
                    mmComboBtn3DTo2DSelfAdaptiveDetect.setEnable(false);
                    mComboBtn3DConversion.setEnable(false);
                    mComboBtn3DTo2D.setEnable(true);
                }
            } else {
                updateSelfDetectBtn();
                mmComboBtn3DTo2DSelfAdaptiveDetect.setEnable(true);
                mComboBtn3DConversion.setEnable(true);
                mComboBtn3DTo2D.setEnable(true);
            }
            if (mmComboBtn3DTo2DSelfAdaptiveDetect.getIdx() == SELF_DETECT_3DTO2D_RIGHT_NOW) {
                mComboBtn3DSelfAdaptiveDetect.setEnable(false);
                mmComboBtn3DTo2DSelfAdaptiveDetect.setEnable(true);
                mComboBtn3DTo2D.setEnable(false);
                mComboBtn3DConversion.setEnable(false);
            }
            if (false == is3DAutodetectSupported) {
                /* Autodetect is dependent on HW capability */
                mComboBtn3DSelfAdaptiveDetect.setVisibility(false);
                mmComboBtn3DTo2DSelfAdaptiveDetect.setVisibility(false);
                mComboBtn3DOffset.setVisibility(false);
            }
            if ((current3DFormat == TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) || (true == is3DTo2D)) {
                update2DMode();
                /*
                 * Note: depth, offset and LR view switch are not allowed in 3D
                 * to 2D mode
                 */
            } else {
                if (true == isUpdate3DUI) {
                    update3DMode(is3DOffestSupported);
                }
            }
            updateDepthAndOffset();
        } else {
            Log.d(TAG, "disable all items in demo mode");
            mComboBtn3DSelfAdaptiveDetect.setEnable(false);
            mComboBtn3DConversion.setEnable(false);
            mComboBtn3DTo2D.setEnable(false);
            mComboBtn3DDepth.setEnable(false);
            mComboBtn3DOffset.setEnable(false);
            mComboBtnLRViewSwitch.setEnable(false);
        }
    }

    private void update2DMode() {
        mComboBtnLRViewSwitch.setEnable(false);
        mComboBtn3DDepth.setEnable(false);
        mComboBtn3DOffset.setEnable(false);
    }

    private void update3DMode(boolean is3DOffestSupported) {
        mComboBtnLRViewSwitch.setEnable(true);
        mComboBtn3DDepth.setEnable(true);
        mComboBtn3DOffset.setEnable(is3DOffestSupported);
    }

    private void updateSelfDetectBtn() {
        if (TvChannelManager.getInstance().isSignalStabled()) {
            mComboBtn3DSelfAdaptiveDetect.setEnable(true);
        } else {
            mComboBtn3DSelfAdaptiveDetect.setEnable(false);
        }
    }

    private void updateDepthAndOffset() {
        mComboBtn3DDepth.setIdx(mTvS3DManager.get3DGain());
        mComboBtn3DOffset.setIdx(mTvS3DManager.get3DOffset());
    }
}
