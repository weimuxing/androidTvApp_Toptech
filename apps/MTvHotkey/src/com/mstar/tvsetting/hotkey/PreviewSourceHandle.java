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

package com.mstar.tvsetting.hotkey;

import java.util.ArrayList;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.tvsetting.switchinputsource.InputSourceGalleryAdapter;
import com.mstar.android.tv.TvPipPopManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

@SuppressWarnings("deprecation")
public class PreviewSourceHandle {
    public static final String TAG = "PreviewSourceHandle";

    private static final long DELAY_TIME = 300;

    private static final int TRAVELING_FRAMERATE = 30;

    private static final int DIP_CHIP_ID = 5;

    protected android.hardware.Camera.Parameters mParameters;

    private Handler mSourcePreviewHandler = null;

    private InputSourceGalleryAdapter mInputSourceGalleryAdapter = null;

    protected android.hardware.Camera mCameraDevice;

    protected int mPosition;

    private ArrayList<InputSourceItem> mGalleryItemList = null;

    protected static SurfaceHolder mSurfaceHolder;

    private int mCurrentTvInputSource = TvCommonManager.INPUT_SOURCE_NONE;

    private TvPipPopManager mTvPipPopManager = null;

    private int mSurceIndex = TvCommonManager.INPUT_SOURCE_NONE;

    public PreviewSourceHandle(ArrayList<InputSourceItem> galleryItemList, int curTvInputSrc, InputSourceGalleryAdapter inputSourceGalleryAdapter) {
        mGalleryItemList = galleryItemList;
        mCurrentTvInputSource = curTvInputSrc;
        mInputSourceGalleryAdapter = inputSourceGalleryAdapter;
        mTvPipPopManager = TvPipPopManager.getInstance();
        HandlerThread handlerThread = new HandlerThread("HandlerThread");
        handlerThread.setDaemon(true);
        handlerThread.start();
        mSourcePreviewHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    stopPreview();
                    mCameraDevice = android.hardware.Camera.open(DIP_CHIP_ID);
                    mParameters = mCameraDevice.getParameters();
                    mParameters.set(MCamera.Parameters.KEY_TRAVELING_RES,
                            MCamera.Parameters.E_TRAVELING_RES_640_480);
                    mParameters.set(MCamera.Parameters.KEY_TRAVELING_MODE,
                            MCamera.Parameters.E_TRAVELING_2ND_VIDEO_PROGRESSIVE_AUTO);
                    mParameters.set(MCamera.Parameters.KEY_MAIN_INPUT_SOURCE,
                            mCurrentTvInputSource);
                    mParameters.set(MCamera.Parameters.KEY_SUB_INPUT_SOURCE, mSurceIndex);
                    mParameters.set(MCamera.Parameters.KEY_TRAVELING_MEM_FORMAT,
                            MCamera.Parameters.E_TRAVELING_MEM_FORMAT_YUV422_YUYV);
                    mParameters.set(MCamera.Parameters.KEY_TRAVELING_FRAMERATE,
                            TRAVELING_FRAMERATE);
                    Log.d(TAG, "mCameraDevice = " + mCameraDevice);
                    Log.d(TAG, "traveling-res = " + MCamera.Parameters.E_TRAVELING_RES_640_480);
                    Log.d(TAG, "traveling-mode = " +
                            MCamera.Parameters.E_TRAVELING_2ND_VIDEO_PROGRESSIVE_AUTO);
                    Log.d(TAG, "main-input-source = " + mCurrentTvInputSource);
                    Log.d(TAG, "sub-input-source = " + mSurceIndex);
                    Log.d(TAG, "traveling-mem-format = " +
                            MCamera.Parameters.E_TRAVELING_MEM_FORMAT_YUV422_YUYV);
                    Log.d(TAG, "traveling-frame-rate = " + TRAVELING_FRAMERATE);
                    mCameraDevice.setParameters(mParameters);
                    Log.d(TAG, "setParameters done");
                    mCameraDevice.setPreviewDisplay(mSurfaceHolder);
                    mCameraDevice.setPreviewCallback(mInputSourceGalleryAdapter);
                    mCameraDevice.startPreview();
                    mInputSourceGalleryAdapter.initPreviewFrameCallbackFilterFlags();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private int getPreviewPosition() {
        return mPosition;
    }

    private void setPreviewPosition(int position) {
        mPosition = position;
    }

    public void openPreview(int position, SurfaceHolder surfaceHolder) {
        mSourcePreviewHandler.removeCallbacksAndMessages(null);
        int currentFocusPositionSourceIndex = mGalleryItemList.get(position).getPositon();
        if (true == checkoutIsSupportPreview(currentFocusPositionSourceIndex)) {
            setPreviewPosition(position);
            mSurfaceHolder = surfaceHolder;
            mSourcePreviewHandler.sendEmptyMessageDelayed(0,
                    DELAY_TIME);
        }
    }

    private boolean checkoutIsSupportPreview(int currentFocusPosition) {
        if (currentFocusPosition == TvCommonManager.INPUT_SOURCE_DTV) {
            mSurceIndex = TvCommonManager.INPUT_SOURCE_DTV2;
        } else {
            mSurceIndex = currentFocusPosition;
        }
        Log.d(TAG, "mCurrentTvInputSource = " + mCurrentTvInputSource +
                ", mSurceIndex = " + mSurceIndex);
        if ((mCurrentTvInputSource != currentFocusPosition)
                && (TvCommonManager.INPUT_SOURCE_STORAGE != currentFocusPosition)
                && mTvPipPopManager.checkTravelingModeSupport(mCurrentTvInputSource,
                        mSurceIndex)) {
            Log.d(TAG, "checkoutIsSupportPreview return true");
            return true;
        }
        Log.d(TAG, "checkoutIsSupportPreview return false");
        return false;
    }

    public void takePreviewLastOneFrame() {
        mInputSourceGalleryAdapter.takeLastOneFrame(getPreviewPosition());
    }

    public void stopPreview() {
        if (null != mCameraDevice) {
            mCameraDevice.stopPreview();
        }
    }

    public void releasePreview() {
        if (null != mCameraDevice) {
            mCameraDevice.setPreviewCallback(null);
            mCameraDevice.release();
            mCameraDevice = null;
        }
    }
}
