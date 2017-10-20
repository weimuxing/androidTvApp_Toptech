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

package com.mstar.miscsetting.activity;

import java.lang.Thread;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.View;
import android.view.Gravity;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvPictureManager.OnVideoStatusEventListener;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvS3DManager.OnS3DCommonEventListener;
import com.mstar.android.tvapi.common.vo.HdrAttribute;
import com.mstar.android.tvapi.common.vo.VideoInfo;
import com.mstar.miscsetting.activity.SeekBarButton;
import com.mstar.miscsetting.R;
import com.mstar.util.Tools;

public class ThreeDimensionsSettingActivity extends BaseActivity {
    private static final String TAG = "ThreeDimensionsSettingActivity";

    private static final int STEP = 1;

    private static final int LOAD_DATA_TO_UI = 111;

    private static final int INVALID_3D = 222;

    private static final int INVALID_3D_TO_2D = 333;

    private static final int UPDATE_UI_BUTTON = 444;

    private final int VIDEO_STATUS_UPDATED = 1000;

    private final int S3D_UPDATE_3D_INFO = 1001;

    private static final int FRAME_PACKING_720P_VSIZE = 1470;

    protected ThreeDComboButton mComboBtn3DSelfAdaptiveDetect;

    protected ThreeDComboButton mmmComboBtn3DTo2DSelfAdaptiveDetect;

    protected ThreeDComboButton mComboBtn3DConversion;

    protected ThreeDComboButton mComboBtn3DTo2D;

    protected SeekBarButton mSeekBar3DDepth;

    protected SeekBarButton mSeekBar3DOffset;

    protected ThreeDComboButton mComboBtnLRViewSwitch;

    protected Thread mInitialThread = null;

    private ThreeDHandler mHandler = null;

    private Handler mthreadHandler = null;

    private TvS3DManager mTvS3DManager;

    private TvCommonManager mTvCommonManager;

    private Toast mShowInvalid3DToast;

    private boolean mThreeDDepthEnable;

    private Thread mThread3DSelfDetect = null;

    private Thread mThread3DConversion = null;

    private OnVideoStatusEventListener mVideoStatusEventListener = null;

    private S3DCommonEventListener mS3DCommonEventListener = null;

    private boolean mIsHDREnabled = false;

    private boolean mIsMenuInHdrMode = false;

    private class VideoStatusEventListener implements OnVideoStatusEventListener {
        @Override
        public boolean onVideoStatusEvent(int what, int arg1, int arg2, Object obj) {
            Log.d(TAG, "onVideoStatusEvent, arg2 = " + arg2);
            switch (what) {
                case TvPictureManager.TVPICTURE_EVENT_VIDEO_STATUS:
                    Message m = Message.obtain();
                    m.what = VIDEO_STATUS_UPDATED;
                    m.arg1 = arg1;
                    m.arg2 = arg2;
                    mHandler.sendMessage(m);
                    break;
            }
            return true;
        }
    }

    private class S3DCommonEventListener implements OnS3DCommonEventListener {
        @Override
        public boolean onEvent(int what, int arg1, int arg2, Object obj) {
            Log.d(TAG, "onEvent, arg1 = " + arg1);
            switch (what) {
                case TvS3DManager.THREED_COMMON_UPDATE_3D_INFO:
                    Message m = Message.obtain();
                    m.what = S3D_UPDATE_3D_INFO;
                    m.arg1 = arg1;
                    m.arg2 = arg2;
                    mHandler.sendMessage(m);
                    break;
            }
            return true;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mComboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
            mComboBtn3DTo2D.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
            mSeekBar3DDepth.setFocusable(false);
            mSeekBar3DOffset.setFocusable(false);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvS3DManager = TvS3DManager.getInstance();
        mTvCommonManager = TvCommonManager.getInstance();
        mThreeDDepthEnable = mTvCommonManager
                .isSupportModule(TvCommonManager.MODULE_TV_CONFIG_THREED_DEPTH);
        if (mInitialThread == null) {
            mInitialThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mInitialThread = null;
                    Looper.prepare();
                    mthreadHandler = new Handler();
                    Looper.loop();
                }
            });
            mInitialThread.start();
        }
        setContentView(R.layout.threedimensionssetting);
        IntentFilter filter = new IntentFilter("initThreeDimensionMode");
        registerReceiver(broadcastReceiver, filter);
        findViews();

        mVideoStatusEventListener = new VideoStatusEventListener();
        TvPictureManager.getInstance().registerOnVideoStatusEventListener(
                mVideoStatusEventListener);
        mS3DCommonEventListener = new S3DCommonEventListener();
        TvS3DManager.getInstance().registerOnS3DCommonEventListener(
                mS3DCommonEventListener);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout_3d_root);
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.x = 0;
        lp.y = 0;
        lp.width = ll.getLayoutParams().width;
        lp.height = ll.getLayoutParams().height;
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent("miscS3DDirty");
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        TvPictureManager.getInstance().unregisterOnVideoStatusEventListener(
                mVideoStatusEventListener);
        if (null != mS3DCommonEventListener) {
            TvS3DManager.getInstance().unregisterOnS3DCommonEventListener(
                    mS3DCommonEventListener);
            mS3DCommonEventListener = null;
        }
        super.onDestroy();
    }

    private Runnable run3DTo2D = new Runnable() {
        @Override
        public void run() {
            boolean isValid = false;
            final int is3DTo2DIdx = mComboBtn3DTo2D.getIdx();
            isValid = mTvS3DManager.enable3DTo2D(is3DTo2DIdx);
            if (TvS3DManager.THREE_DIMENSIONS_TYPE_NONE != is3DTo2DIdx) {
                if (false == isValid) {
                    mHandler.sendEmptyMessage(INVALID_3D_TO_2D);
                    Toast.makeText(ThreeDimensionsSettingActivity.this, "Invalid 3D to 2D!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            updateDepthAndOffset();
        }
    };

    private Runnable run3DConversion = new Runnable() {
        @Override
        public void run() {
            final int n3dConversionIdx = mComboBtn3DConversion.getIdx();
            boolean isValid = false;
            ThreeDimensionsSettingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mComboBtn3DConversion.setEnable(false);
                }
            });

            /**
             * set3dDisplayFormat may cost several seconds, log for the API
             * execution time.
             */
            isValid = mTvS3DManager.enable3D(n3dConversionIdx);
            Log.d(TAG, "n3dConversionIdx: " + n3dConversionIdx + " isValid: " + isValid);
            if (false == isValid) {
                mHandler.sendEmptyMessage(INVALID_3D);
            } else {
                mHandler.sendEmptyMessage(UPDATE_UI_BUTTON);
            }

            ThreeDimensionsSettingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mComboBtn3DConversion.setEnable(true);
                }
            });
        }
    };

    private Runnable runSet3DSelfDetect = new Runnable() {
        @Override
        public void run() {
            ThreeDimensionsSettingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mComboBtn3DSelfAdaptiveDetect.setEnable(false);
                }
            });

            mTvS3DManager.setSelfAdaptiveDetectMode(mComboBtn3DSelfAdaptiveDetect.getIdx());
            ThreeDimensionsSettingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mComboBtn3DConversion.setIdx(mTvS3DManager.getCurrent3dType());
                }
            });
            ThreeDimensionsSettingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mComboBtn3DSelfAdaptiveDetect.setEnable(true);
                }
            });
            mHandler.sendEmptyMessage(UPDATE_UI_BUTTON);
        }
    };

    private Runnable runSet3DTo2DSelfDetect = new Runnable() {
        @Override
        public void run() {
            if (TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SELF_ADAPTIVE_DETECT_RIGHT_NOW == mmmComboBtn3DTo2DSelfAdaptiveDetect
                    .getIdx()) {
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
            ThreeDimensionsSettingActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mComboBtn3DTo2D.setIdx(mTvS3DManager.getCurrent3dType());
                }
            });
            mHandler.sendEmptyMessage(UPDATE_UI_BUTTON);
        }
    };

    private void update2DMode() {
        mComboBtnLRViewSwitch.setFocusable(false);
        mSeekBar3DDepth.setFocusable(false);
        mSeekBar3DOffset.setFocusable(false);
    }

    private void update3DMode(boolean is3DOffestSupported) {
        mComboBtnLRViewSwitch.setFocusable(true);
        mSeekBar3DDepth.setFocusable(true);
        mSeekBar3DOffset.setFocusable(is3DOffestSupported);
    }

    private void findViews() {
        mComboBtn3DSelfAdaptiveDetect = new ThreeDComboButton(this, this.getResources()
                .getStringArray(R.array.str_arr_3d_detect_vals),
                R.id.linearlayout_3d_setting_selfdetect, false) {
            @Override
            public void doUpdate() {
                // index 0: 3D detect off
                if (mThread3DSelfDetect == null
                        || mThread3DSelfDetect.getState() == Thread.State.TERMINATED) {
                    final boolean is3dSelfDetect = (TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_OFF != mComboBtn3DSelfAdaptiveDetect
                            .getIdx()) ? true : false;
                    Log.d(TAG, "is3dSelfDetect: " + is3dSelfDetect);
                    mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(!is3dSelfDetect);
                    mComboBtn3DConversion.setFocusable(!is3dSelfDetect);
                    mComboBtn3DTo2D.setFocusable(!is3dSelfDetect);
                    mComboBtnLRViewSwitch.setFocusable(is3dSelfDetect);

                    mThread3DSelfDetect = new Thread(runSet3DSelfDetect);
                    mThread3DSelfDetect.start();
                    if (!is3dSelfDetect) {
                        mComboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
                    }
                } else {
                    Log.d(TAG, "Abort, another thread is already calling 3D SelfDetect");
                }
            }
        };
        String[] items = this.getResources().getStringArray(R.array.str_arr_3d_detect_vals);
        // the funtion of real time 3d works bad
        if (items.length > 3) {
            mComboBtn3DSelfAdaptiveDetect.setItemEnable(3, false);
        }
        mComboBtn3DSelfAdaptiveDetect.setIdx(mTvS3DManager.getSelfAdaptiveDetectMode());
        mmmComboBtn3DTo2DSelfAdaptiveDetect = new ThreeDComboButton(this, this.getResources()
                .getStringArray(R.array.str_arr_3dto2d_detect_vals),
                R.id.linearlayout_3d_setting_3dto2d_selfdetect, false) {
            @Override
            public void doUpdate() {
                // index 0: 3D detect off
                final boolean is3DTo2DSelfDetect = (TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SELF_ADAPTIVE_DETECT_OFF != mmmComboBtn3DTo2DSelfAdaptiveDetect
                        .getIdx()) ? true : false;
                Log.d(TAG, "is3DTo2DSelfDetect: " + is3DTo2DSelfDetect);
                mComboBtn3DSelfAdaptiveDetect.setFocusable(!is3DTo2DSelfDetect);
                mComboBtn3DConversion.setFocusable(!is3DTo2DSelfDetect);
                mComboBtn3DTo2D.setFocusable(!is3DTo2DSelfDetect);
                update2DMode();
                mthreadHandler.removeCallbacks(runSet3DTo2DSelfDetect);
                mthreadHandler.post(runSet3DTo2DSelfDetect);
                if (false == is3DTo2DSelfDetect) {
                    mComboBtn3DTo2D.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
                }
            }
        };
        mComboBtn3DConversion = new ThreeDComboButton(this, this.getResources().getStringArray(
                R.array.str_arr_3d_conversion_vals), R.id.linearlayout_3d_setting_3dconversion,
                false) {
            @Override
            public void doUpdate() {
                if (mThread3DConversion == null
                        || mThread3DConversion.getState() == Thread.State.TERMINATED) {
                    if (mShowInvalid3DToast != null) {
                        mShowInvalid3DToast.cancel();
                    }
                    boolean is3DConversion = (TvS3DManager.THREE_DIMENSIONS_TYPE_NONE == mComboBtn3DConversion
                            .getIdx()) ? false : true;
                    Log.d(TAG, "is3DConversion: " + is3DConversion);
                    if (false == mIsHDREnabled) {
                        mComboBtn3DSelfAdaptiveDetect.setFocusable(!is3DConversion);
                        mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(!is3DConversion);
                        mComboBtn3DTo2D.setFocusable(!is3DConversion);
                        mComboBtnLRViewSwitch.setFocusable(is3DConversion);
                    }
                    mComboBtn3DConversion.setTextViewIndicatorColor(Color.WHITE);
                    mThread3DConversion = new Thread(run3DConversion);
                    mThread3DConversion.start();
                } else {
                    Log.d(TAG, "Abort, another thread is already calling 3D Conversion");
                }
            }
        };
        mComboBtn3DTo2D = new ThreeDComboButton(this, this.getResources().getStringArray(
                R.array.str_arr_3d_3dto2d_vals), R.id.linearlayout_3d_setting_3dto2d, false) {
            @Override
            public void doUpdate() {
                final boolean is3DTo2D = (TvS3DManager.THREE_DIMENSIONS_TYPE_NONE != mComboBtn3DTo2D
                        .getIdx()) ? true : false;
                Log.d(TAG, "is3DTo2D: " + is3DTo2D);
                final boolean is3DConversion = (TvS3DManager.THREE_DIMENSIONS_TYPE_NONE != mComboBtn3DConversion
                        .getIdx()) ? true : false;
                if (is3DTo2D && is3DConversion) {
                    mComboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
                }
                mComboBtn3DSelfAdaptiveDetect.setFocusable(!is3DTo2D);
                mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(!is3DTo2D);
                mComboBtn3DConversion.setFocusable(!is3DTo2D);
                mComboBtn3DTo2D.setTextViewIndicatorColor(Color.WHITE);
                update2DMode();
                mthreadHandler.removeCallbacks(run3DTo2D);
                mthreadHandler.post(run3DTo2D);
            }
        };
        mSeekBar3DDepth = new SeekBarButton(this, R.id.linearlayout_3d_setting_3ddepth, STEP, false) {
            @Override
            public void doUpdate() {
                mTvS3DManager.set3DGain(mSeekBar3DDepth.getProgress());
            }
        };
        if (mThreeDDepthEnable == false) {
            mSeekBar3DDepth.setVisibility(false);
        }
        mSeekBar3DOffset = new SeekBarButton(this, R.id.linearlayout_3d_setting_3doffset, STEP,
                false) {
            @Override
            public void doUpdate() {
                mTvS3DManager.set3DOffset(mSeekBar3DOffset.getProgress());
            }
        };
        mComboBtnLRViewSwitch = new ThreeDComboButton(this, this.getResources().getStringArray(
                R.array.str_arr_lrswitch_vals), R.id.linearlayout_lrswitch, false) {
            @Override
            public void doUpdate() {
                mTvS3DManager.set3DLrViewSwitch(mComboBtnLRViewSwitch.getIdx());
            }
        };

        if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OPEN_HDR)) {
            HdrAttribute openHdrAtt = TvPictureManager.getInstance().getHdrAttributes(
                    TvPictureManager.HDR_OPEN_ATTRIBUTES, TvPictureManager.VIDEO_MAIN_WINDOW);
            if (true == openHdrAtt.isRunning) {
                mIsHDREnabled = true;
            }
        }
        if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_DOLBY_HDR)) {
            HdrAttribute dolbyHdrAtt = TvPictureManager.getInstance().getHdrAttributes(
                    TvPictureManager.HDR_DOLBY_ATTRIBUTES, TvPictureManager.VIDEO_MAIN_WINDOW);
            if (true == dolbyHdrAtt.isRunning) {
                mIsHDREnabled = true;
            }
        }

        LoadDataToUI();
        enableMenuItem(false);
        mHandler = new ThreeDHandler();
        mHandler.sendEmptyMessageDelayed(LOAD_DATA_TO_UI, 300);
    }

    class ThreeDHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if ((what == LOAD_DATA_TO_UI) || (what == S3D_UPDATE_3D_INFO)) {
                if (true == mIsHDREnabled) {
                    enableMenuItemForHDR(true);
                } else {
                    enableMenuItem(true);
                }
                LoadDataToUI();
            } else if (what == INVALID_3D) {
                update2DMode();
                // the color indicates this button is valid or not
                mComboBtn3DConversion.setTextViewIndicatorColor(Color.GRAY);
                if (null != mShowInvalid3DToast) {
                    mShowInvalid3DToast.cancel();
                }
                mShowInvalid3DToast = Toast.makeText(ThreeDimensionsSettingActivity.this,
                        "Invalid 3D Format!", Toast.LENGTH_SHORT);
                mShowInvalid3DToast.show();
                updateDepthAndOffset();
            } else if (what == INVALID_3D_TO_2D) {
                // this color indicates incalid
                mComboBtn3DTo2D.setTextViewIndicatorColor(Color.GRAY);
                updateDepthAndOffset();
            } else if (what == UPDATE_UI_BUTTON) {
                updateFocusableForBottom();
                updateDepthAndOffset();
            } else if (what == VIDEO_STATUS_UPDATED) {
                if (msg.arg2 == TvPictureManager.VIDEO_VIDEO_HDR_STOP) {
                    mIsHDREnabled = false;
                    enableMenuItemForHDR(false);
                } else {
                    mIsHDREnabled = true;
                    enableMenuItemForHDR(true);
                }
            }

            super.handleMessage(msg);
        }
    }

    public void LoadDataToUI() {
        mmmComboBtn3DTo2DSelfAdaptiveDetect
                .setIdx(TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SELF_ADAPTIVE_DETECT_OFF);
        final int current3DFormat = mTvS3DManager.getCurrent3dType();
        final boolean is3DTo2D = mTvS3DManager.get3dTo2dStatus();

        if (current3DFormat == TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_1080P ||
            current3DFormat == TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P ) {
            //Always set THREE_DIMENSIONS_TYPE_FRAME_PACKING_1080P
            mComboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_1080P);
        } else {
            mComboBtn3DConversion.setIdx(current3DFormat);
        }
        mComboBtn3DTo2D.setIdx(current3DFormat);

        if (true == is3DTo2D) {
            mComboBtn3DConversion.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
            mComboBtn3DTo2D.setIdx(current3DFormat);
        } else {
            mComboBtn3DTo2D.setIdx(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE);
        }
        mSeekBar3DDepth.setProgress((short) mTvS3DManager.get3DGain());
        mSeekBar3DOffset.setProgress((short) mTvS3DManager.get3DOffset());
        mComboBtnLRViewSwitch.setIdx(mTvS3DManager.get3DLrViewSwitch());

        hideUnsupportItems();

        if (isMWEOn()) { // support no 3d function for MWE
            mComboBtn3DSelfAdaptiveDetect.setFocusable(false);
            mComboBtn3DConversion.setFocusable(false);
            mComboBtn3DTo2D.setFocusable(false);
            mSeekBar3DDepth.setFocusable(false);
            mSeekBar3DOffset.setFocusable(false);
            mComboBtnLRViewSwitch.setFocusable(false);
        } else {
            updateFocusableForBottom();
        }
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

    private boolean isMWEOn() {
        return (TvPictureManager.getInstance().getMWEDemoMode() != TvPictureManager.MWE_DEMO_MODE_OFF);
    }

    private void updateFocusableForBottom() {
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
        if (true == mIsHDREnabled) {
            if (is2DTo3D) {
                mSeekBar3DDepth.setFocusable(true);
                mSeekBar3DOffset.setFocusable(is3DOffestSupported);
            } else {
                mSeekBar3DDepth.setEnable(false);
                mSeekBar3DOffset.setEnable(false);
            }
        } else {
            if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE
                    && current3DFormat == TvS3DManager.THREE_DIMENSIONS_TYPE_FRAME_PACKING_720P
                    && videoInfo.vResolution == FRAME_PACKING_720P_VSIZE) {
                mComboBtn3DSelfAdaptiveDetect.setFocusable(false);
                mComboBtn3DConversion.setFocusable(false);
            } else if (_3DSelfDetectMode != TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_OFF) {
                /* for 3D self detection */
                mComboBtn3DSelfAdaptiveDetect.setFocusable(true);
                mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(false);
                mComboBtn3DConversion.setFocusable(false);
                mComboBtn3DTo2D.setFocusable(false);
            } else if (current3DFormat != TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) {
                if (false == is3DTo2D) {
                    /* for 3D Conversion */
                    mComboBtn3DSelfAdaptiveDetect.setFocusable(false);
                    mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(false);
                    mComboBtn3DConversion.setFocusable(true);
                    mComboBtn3DTo2D.setFocusable(false);
                } else {
                    /* for 3D-2D Conversion */
                    mComboBtn3DSelfAdaptiveDetect.setFocusable(false);
                    mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(false);
                    mComboBtn3DConversion.setFocusable(false);
                    mComboBtn3DTo2D.setFocusable(true);
                    if (TvS3DManager.THREE_DIMENSIONS_VIDEO_3DTO2D_SELF_ADAPTIVE_DETECT_RIGHT_NOW == mmmComboBtn3DTo2DSelfAdaptiveDetect.getIdx()) {
                        mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(true);
                        mComboBtn3DTo2D.setFocusable(false);
                    }
                }
            } else {
                mComboBtn3DSelfAdaptiveDetect.setFocusable(true);
                mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(true);
                mComboBtn3DConversion.setFocusable(true);
                mComboBtn3DTo2D.setFocusable(true);
            }

            if (false == is3DAutodetectSupported) {
                /* Autodetect is dependent on HW capability */
                mComboBtn3DSelfAdaptiveDetect.setVisibility(false);
                mmmComboBtn3DTo2DSelfAdaptiveDetect.setVisibility(false);
                mSeekBar3DOffset.setVisibility(false);
            }
            if ((current3DFormat == TvS3DManager.THREE_DIMENSIONS_TYPE_NONE) || (true == is3DTo2D)) {
                update2DMode();
                /*
                 * Note: depth, offset and LR view switch are not allowed in 3D
                 * to 2D mode
                 */
            } else {
                update3DMode(is3DOffestSupported);
            }
        }
    }

    private void enableMenuItem(boolean en) {
        mComboBtn3DSelfAdaptiveDetect.setFocusable(en);
        mmmComboBtn3DTo2DSelfAdaptiveDetect.setFocusable(en);
        mComboBtn3DConversion.setFocusable(en);
        mComboBtn3DTo2D.setFocusable(en);
        mSeekBar3DDepth.setFocusable(en);
        mSeekBar3DOffset.setFocusable(en);
        mComboBtnLRViewSwitch.setFocusable(en);
    }

    private void enableMenuItemForHDR(boolean en) {
        Log.d(TAG, "enableMenuItemForHDR, en = " + en + ", mIsMenuInHdrMode = " + mIsMenuInHdrMode);
        if ((false == mIsMenuInHdrMode) && (false == en)) {
            return;
        }
        if (true == en) {
            mIsMenuInHdrMode = true;
            enableMenuItem(false);
            mComboBtn3DConversion.setFocusable(true);

            int len = mComboBtn3DConversion.getItemLength();
            for (int i = 0; i < len; i++) {
                mComboBtn3DConversion.setItemEnable(i, false);
            }
            /* Only "None" and "2DTo3D" are supported, if HDR is enabled. */
            mComboBtn3DConversion.setItemEnable(TvS3DManager.THREE_DIMENSIONS_TYPE_NONE,
                    true);
            mComboBtn3DConversion.setItemEnable(
                    TvS3DManager.THREE_DIMENSIONS_TYPE_2DTO3D, true);
        } else {
            mIsMenuInHdrMode = false;
            int len = mComboBtn3DConversion.getItemLength();
            for (int i = 0; i < len; i++) {
                mComboBtn3DConversion.setItemEnable(i, true);
            }
            enableMenuItem(true);
        }
        hideUnsupportItems();
        updateFocusableForBottom();
        updateDepthAndOffset();
    }

    private void updateDepthAndOffset() {
        ThreeDimensionsSettingActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                mSeekBar3DDepth.setProgress((short) mTvS3DManager.get3DGain());
                mSeekBar3DOffset.setProgress((short) mTvS3DManager.get3DOffset());
            }
        });
    }
}
