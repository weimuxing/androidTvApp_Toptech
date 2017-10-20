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
package com.jrm.localmm.ui.video;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.jrm.localmm.R;
import com.jrm.localmm.business.video.VideoPlayView;
import com.jrm.localmm.util.Constants;
import com.jrm.localmm.util.Tools;
import com.mstar.android.MDisplay;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tvapi.common.ThreeDimensionManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.Enum3dType;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.EnumScreenMuteType;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideo3DTo2D;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureSource;
import com.mstar.android.tvapi.common.PipManager;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureDeviceType;
import com.mstar.android.tvapi.common.vo.EnumPipModes;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tvapi.common.CecManager;
import com.mstar.android.tvapi.common.vo.CecSetting;

public class ThreeDimentionControl {
    private static final String TAG = "ThreeDimentionControl";
    private static final int mAutoDetect3DFromatTimes = 3;
    private static ThreeDimentionControl mThreeDimentionControl;
    protected VideoPlayView mVideoPlayView;
    // MVC sources flag
    private boolean mVideoSourceIsMvc = false;
    private boolean isThreeDMode = false;
    // 3D mode
    private EnumThreeDVideoDisplayFormat mThreeDMode = EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO;
    private EnumThreeDVideo3DTo2D mThreeD3DTo2D;
    private CecManager mCecManager;
    private final static int THREED_3DTO2D_NO = 0;
    private final static int THREED_3DTO2D_YES = 1;
    // 3D to 2D
    private int mThreeD3DTo2DIndex = THREED_3DTO2D_NO;
    // Sources of 3d type
    private Enum3dType mVideoSource3DType = Enum3dType.EN_3D_NONE;
    private Handler mHandler;
    private Context mContext;
    // Automatic identification number 3d
    private int detectNum = 0;
    private int detectTopBottomNum = 0;
    private int detectSideBySideNum = 0;

    public static ThreeDimentionControl getInstance() {
        if (mThreeDimentionControl == null) {
            mThreeDimentionControl = new ThreeDimentionControl();
        }
        return mThreeDimentionControl;
    }

    public void setMediaPlayer(VideoPlayView videoPlayView) {
        mVideoPlayView = videoPlayView;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * Detection is MVC sources
     */
    public void checkMvcSource() {
        if (mVideoPlayView.isMVCSource()) {
            mVideoSourceIsMvc = true;
            Log.i(TAG, "******is MVC source");
            if (!Tools.unSupportTVApi()) {
                mThreeDMode = EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO;
            }
        } else {
            Log.i(TAG, "******is not MVC source");
            mVideoSourceIsMvc = false;
        }
    }

    /**
     * 3D automatic identification
     */
    public void ThreeDInit() {
        detectNum = 0;
        detectTopBottomNum = 0;
        detectSideBySideNum = 0;
        if(Tools.isBox()){
            setOSD3DMode();
            return;
        }
        Log.i(TAG, "ThreeDInit -> mThreeDMode =" + mThreeDMode);
        if (!isVideoSourceMvc()) {
            if (Tools.unSupportTVApi()) {
                return;
            }
            if (isThreeDVideoSelfAdaptiveDetectOn()) {
                settingThreeDMode(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO);
            }

            /*
            if (isThreeDAuto() ) {
                try {
                    // Detection sources 3D type
                    mVideoSource3DType = mThreeDimensionManager.detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW);
                    Log.i(TAG, "ThreeDInit -> mVideoSource3DType =" + mVideoSource3DType);
                    if (mVideoSource3DType == Enum3dType.EN_3D_NONE) {
                        mHandler.sendEmptyMessageDelayed(VideoPlayerActivity.INIT_THREED, 1000);
                    } else {
                        // Screen blank screen
                        setVideoMute(true, 1500);
                        Log.i(TAG, "enable3d start:" + (new Date()).getTime());
                        // Set up 3D model
                        mThreeDimensionManager.enable3d(mVideoSource3DType);
                        isThreeDMode = true;
                        Log.i(TAG, "enable3d end:" + (new Date()).getTime());
                        // Open blank screen
                        setVideoMute(false, 0);
                    }
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
            } else {
                if (isThreeDVideoSelfAdaptiveDetectOn()) {
                    settingThreeDMode(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO);
                } else {
                    settingThreeDMode(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE);
                }

            }
            */
        }
        if (!Tools.unSupportTVApi()) {
            Log.i(TAG, "ThreeDInit -> mThreeDMode=" + mThreeDMode);
        }
    }

    // isThreeDVideoSelfAdaptiveDetectOn is a function to get MiscSetting 3D AutoDetect Switch Status.
    // MiscSetting 3D AutoDetect Switch Value: 0: detect off, 1: right now, 2: when source changed
    private boolean isThreeDVideoSelfAdaptiveDetectOn() {
        if (Tools.unSupportTVApi()) {
            return false;
        }
        int selfDetectMode = TvS3DManager.getInstance().getSelfAdaptiveDetectMode();
        Log.i(TAG, "isThreeDVideoSelfAdaptiveDetectOn");
        if ((selfDetectMode == TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_RIGHT_NOW)
            || (selfDetectMode == TvS3DManager.THREE_DIMENSIONS_VIDEO_SELF_ADAPTIVE_DETECT_WHEN_SOURCE_CHANGE)) {
            return true;
        } else {
            return false;
        }
    }

    public void refreshThreeDMode() {
        Log.i(TAG, "refreshThreeDMode -> mThreeDMode =" + mThreeDMode);
        if (mVideoPlayView.isMVCSource()) {
            mVideoSourceIsMvc = true;
        } else {
            if (Tools.unSupportTVApi()) {
                return;
            }
            mThreeDMode = Tools.getCurrent3dFormat();
            mVideoSourceIsMvc = false;
            if (isThreeDAuto()) {
                ThreeDimensionManager threeDimensionManager = TvManager.getInstance().getThreeDimensionManager();
                Enum3dType videoSource3DTypetemp = Enum3dType.EN_3D_NONE;
                videoSource3DTypetemp = getCurrent3DFormate();
                Log.i(TAG, "refreshThreeDMode -> videoSource3DTypetemp =" + videoSource3DTypetemp);
                if (videoSource3DTypetemp == Enum3dType.EN_3D_NONE) {
                    try {
                        mVideoSource3DType = threeDimensionManager.detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW);
                    } catch (TvCommonException e) {
                        e.printStackTrace();
                    }
                } else {
                    mVideoSource3DType = videoSource3DTypetemp;
                }
                Log.i(TAG, "refreshThreeDMode -> mVideoSource3DType=" + mVideoSource3DType);
                if (mVideoSource3DType == Enum3dType.EN_3D_NONE || mVideoSource3DType == null) {
                    if (++detectNum < 10) {
                        mHandler.sendEmptyMessageDelayed(VideoPlayerActivity.INIT_THREED, 1000);
                    }
                } else {
                    setVideoMute(true, 1500);
                    Log.i(TAG, "enable3d start:" + (new Date()).getTime());
                    try {
                        threeDimensionManager.enable3d(mVideoSource3DType);
                        isThreeDMode = true;
                    } catch (TvCommonException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "enable3d end:" + (new Date()).getTime());
                    setVideoMute(false, 0);
                }
            } else {
                if (mThreeDMode != null && mThreeDMode != EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                    //Tools.setDisplayFormat(mThreeDMode);
                    if (mThreeD3DTo2DIndex == THREED_3DTO2D_YES) {
                        setting3DTo2D(mThreeD3DTo2DIndex);
                    }
                } else if (mThreeDMode == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                    if (isThreeDVideoSelfAdaptiveDetectOn()) {
                        settingThreeDMode(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO);
                    }
                }
            }
        }
    }

    /**
     * Screen open and cover screen
     *
     * @param isMute
     *            cover/open
     * @param time
     */
    public void setVideoMute(boolean isMute, int time) {
        Log.i(TAG, "*********setVideoMute********" + isMute);
        if (Tools.unSupportTVApi()) {
            return;
        }
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(isMute,EnumScreenMuteType.E_BLACK, time,
                        EnumInputSource.E_INPUT_SOURCE_STORAGE);
            }
            // TvManager.setVideoMute(isMute, EnumScreenMuteType.E_BLACK, time,
            // EnumInputSource.E_INPUT_SOURCE_STORAGE);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public boolean isVideoSourceMvc() {
        return mVideoSourceIsMvc;
    }

    public void set3DFormate(Enum3dType enum3DFormate){
        try {
            ThreeDimensionManager threeDimensionManager = TvManager.getInstance().getThreeDimensionManager();
            threeDimensionManager.enable3d(enum3DFormate);
        } catch (TvCommonException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public Enum3dType getCurrent3DFormate() {
        Enum3dType currentFormate = Enum3dType.EN_3D_NONE;
        try {
            // currentFormate = mThreeDimensionManager.detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW);
            currentFormate = TvManager.getInstance().getThreeDimensionManager().getCurrent3dFormat();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return currentFormate;
    }

    public boolean isThreeDAuto() {
        if (mThreeDMode == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO) {
            return true;
        } else {
            return false;
        }
    }

    public void getThreeDMode() {
        if (!Tools.unSupportTVApi()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mThreeDMode = Tools.getCurrent3dFormat();
                }
            }).start();
        }
    }

    /**
     * set up 3D model
     *
     * @param threeDModeIndex
     *            3D model index
     */
    public void settingThreeDMode(EnumThreeDVideoDisplayFormat threeDMode) {
        if (Tools.unSupportTVApi()) {
            return;
        }
        Log.i(TAG, "settingThreeDMode->mThreeDMode=" + threeDMode);
        mThreeDMode = threeDMode;
        if (mThreeDMode == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO) {
            // Log.i(TAG, "TvS3DManager autoDetect3DFormat " + mAutoDetect3DFromatTimes + " times");
            // TvS3DManager.getInstance().autoDetect3DFormat(mAutoDetect3DFromatTimes);
            autoDetect3DFormat(mAutoDetect3DFromatTimes);
            // Tools.setDisplayFormat(mThreeDMode);
            if (!isVideoSourceMvc()) {
                Enum3dType videoSource3DTypetemp = getCurrent3DFormate();
                mVideoSource3DType = videoSource3DTypetemp;
                Log.i(TAG, "settingThreeDMode -> mThreeDMode=" + mThreeDMode);
                Log.i(TAG, "settingThreeDMode -> mVideoSource3DType=" + mVideoSource3DType);
            }
        } else {
            if (mThreeDMode != null && mThreeDMode != EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE)
                Tools.setDisplayFormat(mThreeDMode);
        }
        Log.i(TAG, "settingThreeDMode -> mThreeDMode=" + mThreeDMode);
    }


    public boolean autoDetect3DFormat(int autoDetect3DFormatTimes) {
        Log.i(TAG, "autoDetect3DFormat autoDetect3DFormatTimes:" + autoDetect3DFormatTimes);

        boolean result = false;
        Enum3dType threedType = null;
        Enum3dType type = Enum3dType.EN_3D_NONE;
        for (int i = 0; i < autoDetect3DFormatTimes; i++) {
            if (mContext == null) {
                return false;
            }
            if (!Tools.isCurrentInputSourceStorage()) {
                return false;
            }

            try {
                threedType = TvManager.getInstance().getThreeDimensionManager().getCurrent3dFormat();
                if (Enum3dType.EN_3D_NONE != threedType) {
                    Log.i(TAG, "Don't need autoDetect3dFormat, because current 3D mode is " + threedType);
                    return true;
                }

                Log.i(TAG, "detect3dFormat times: " + i);
                type = TvManager.getInstance().getThreeDimensionManager().
                        detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW, 1);
                Log.i(TAG, "detect3dFormat type:" + type);
                if (type != Enum3dType.EN_3D_NONE) {
                    result = TvManager.getInstance().getThreeDimensionManager().enable3d(type);
                    break;
                }
            } catch (TvCommonException e) {
                Log.i(TAG, "TvCommonException:" + e);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * * 3D sources turn 2D to watch
     *
     * @param threeD3DTo2DIndex
     *            3D turn 2D parameter index
     */
    public void setting3DTo2D(int threeD3DTo2DIndex) {
        if (Tools.unSupportTVApi()) {
            return;
        }
        mThreeD3DTo2DIndex = threeD3DTo2DIndex;
        Log.i(TAG, "setting3DTo2D -> mThreeD3DTo2DIndex=" + mThreeD3DTo2DIndex);
        switch (mThreeD3DTo2DIndex) {
        case THREED_3DTO2D_NO:
            Log.i(TAG, "setting3DTo2D 0000000 -> mThreeDMode=" + mThreeDMode);
            settingThreeDMode(mThreeDMode);
            break;
        case THREED_3DTO2D_YES:
            Log.i(TAG, "setting3DTo2D 1111111 -> mThreeDMode=" + mThreeDMode);
            switch (mThreeDMode) {
            case E_ThreeD_Video_DISPLAYFORMAT_AUTO:
                mThreeD3DTo2D = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_AUTO;
                break;
            case E_ThreeD_Video_DISPLAYFORMAT_SIDE_BY_SIDE:
                mThreeD3DTo2D = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_SIDE_BY_SIDE;
                break;
            case E_ThreeD_Video_DISPLAYFORMAT_TOP_BOTTOM:
                mThreeD3DTo2D = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_TOP_BOTTOM;
                break;
            case E_ThreeD_Video_DISPLAYFORMAT_LINE_ALTERNATIVE:
                mThreeD3DTo2D = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_LINE_ALTERNATIVE;
                break;
            case E_ThreeD_Video_DISPLAYFORMAT_COUNT: // vertical stripe
                mThreeD3DTo2D = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_COUNT;
                break;
            case E_ThreeD_Video_DISPLAYFORMAT_FRAME_PACKING:
                mThreeD3DTo2D = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_FRAME_PACKING;
                break;
            default:
                break;
            }
            Log.i(TAG, "setting3DTo2D 1111111 -> mThreeD3DTo2D= "
                    + mThreeD3DTo2D);
            // 3D to 2D
            Tools.set3DTo2D(mThreeD3DTo2D);
            break;
        default:
            break;
        }
    }

    public boolean isThreeDMode() {
        if (Tools.unSupportTVApi()) {
            return false;
        }
        return isThreeDMode
               || isVideoSourceMvc()
               || (Tools.getCurrent3dFormat() != EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE);
    }

    public void setOSD3DMode(){
        if (Tools.unSupportTVApi()) {
            return;
        }
        try {
            ThreeDimensionManager threeDimensionManager = TvManager.getInstance().getThreeDimensionManager();
            mVideoSource3DType = threeDimensionManager.detect3dFormat(EnumScalerWindow.E_MAIN_WINDOW);
            Log.i(TAG, "ThreeDInit -> mVideoSource3DType =" + mVideoSource3DType);

            if(mVideoSource3DType == Enum3dType.EN_3D_NONE){
                if (++detectNum < 10) {
                    mHandler.sendEmptyMessageDelayed(Constants.OSD_3D_UI, 1000);
                }
            }else if(mVideoSource3DType == Enum3dType.EN_3D_TOP_BOTTOM){
                if(detectTopBottomNum<1){
                    detectTopBottomNum++;
                    Log.i(TAG, "detectTopBottomNum = " + detectTopBottomNum);
                } else {
                    MDisplay.set3DDisplayMode(2);
                }
            }else if(mVideoSource3DType == Enum3dType.EN_3D_SIDE_BY_SIDE_HALF){
                if (detectSideBySideNum<1) {
                    detectSideBySideNum++;
                    Log.i(TAG, "detectSideBySideNum = " + detectSideBySideNum);
                } else {
                    MDisplay.set3DDisplayMode(1);
                }
            }

        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public void init3DMode() {
        if (Tools.unSupportTVApi()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Boot the input from TV switch to AP
                // s3dSkin = TvApiManager
                // .getTvS3DManager(VideoPlayerActivity.this);
                TvManager mTvManager = TvManager.getInstance();
                ThreeDimensionManager threeDimensionManager = TvManager.getInstance().getThreeDimensionManager();
                try {
                    if (mTvManager != null) {
                        PipManager mPipManager = mTvManager.getPipManager();
                        TvPipPopManager mTvPipPopManager = TvPipPopManager.getInstance();
                        EnumPipModes mode = mPipManager.getPipMode();
                        Log.i(TAG, "******mode:****" + mode);
                        if (mode == EnumPipModes.E_PIP_MODE_PIP) {
                            swithVol2Main();
                            mPipManager.disablePip(EnumScalerWindow.E_MAIN_WINDOW);
                            mTvPipPopManager.disablePip();
                            mTvPipPopManager.setPipOnFlag(false);
                        } else if (mode == EnumPipModes.E_PIP_MODE_POP) {
                            swithVol2Main();
                            Enum3dType formatType = Enum3dType.EN_3D_TYPE_NUM;
                            formatType = threeDimensionManager.getCurrent3dFormat();
                            if (formatType == Enum3dType.EN_3D_DUALVIEW) {
                                threeDimensionManager.disable3dDualView();
                            } else {
                                mPipManager.disablePop(EnumScalerWindow.E_MAIN_WINDOW);
                                mTvPipPopManager.disablePop();
                            }
                        } else {
                            Enum3dType formatType = Enum3dType.EN_3D_TYPE_NUM;
                            formatType = threeDimensionManager.getCurrent3dFormat();
                            Log.i(TAG, "******formatType:****" + formatType);
                            if (formatType == Enum3dType.EN_3D_DUALVIEW) {
                                threeDimensionManager.disable3dDualView();
                                mTvPipPopManager.disable3dDualView();
                            }
                        }
                    }
                } catch (TvCommonException e1) {

                    e1.printStackTrace();
                }
            }
        }).start();
    }

    public void disable3dDualView() {
        if (Tools.unSupportTVApi()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreeDimensionManager threeDimensionManager = TvManager.getInstance().getThreeDimensionManager();
                if (threeDimensionManager != null) {
                    try {
                        Enum3dType formatType = Enum3dType.EN_3D_TYPE_NUM;
                        formatType = threeDimensionManager.getCurrent3dFormat();
                        if (formatType == Enum3dType.EN_3D_DUALVIEW) {
                            Log.i(TAG, "disable3dDualView EN_3D_DUALVIEW");
                            threeDimensionManager.disable3dDualView();
                        }
                    } catch (TvCommonException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void swithVol2Main() {
        if (Tools.unSupportTVApi()) {
            return;
        }
        try {
            TvManager.getInstance().getAudioManager().setAudioCaptureSource(
                            EnumAuidoCaptureDeviceType.E_CAPTURE_DEVICE_TYPE_DEVICE1,
                            EnumAuidoCaptureSource.E_CAPTURE_MAIN_SOUND);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public boolean isPIPMode() {
        if (Tools.unSupportTVApi()) {
            return false;
        }
        try {
            TvManager mTvManager = TvManager.getInstance();
            if (mTvManager != null) {
                PipManager mPipManager = mTvManager.getPipManager();
                EnumPipModes mode;
                mode = mPipManager.getPipMode();
                Log.i(TAG, "******mode:****" + mode);
                if (mode == EnumPipModes.E_PIP_MODE_PIP || mode == EnumPipModes.E_PIP_MODE_POP) {
                    return true;
                } else {
                    ThreeDimensionManager threeDimensionManager = TvManager.getInstance().getThreeDimensionManager();
                    if (threeDimensionManager.getCurrent3dFormat() == Enum3dType.EN_3D_DUALVIEW) {
                        return true;
                    }
                }
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendCecKey(int keyCode) {
        Log.d(TAG, "send Cec key,keyCode is " + keyCode);
        if (Tools.unSupportTVApi()) {
            return false;
        }
        try {
            if (mCecManager == null) {
                mCecManager = TvManager.getInstance().getCecManager();
            }
            CecSetting setting = mCecManager.getCecConfiguration();
            if (setting.cecStatus == 1) {
                if (mCecManager.sendCecKey(keyCode)) {
                    Log.d(TAG, "send Cec key,keyCode is " + keyCode + ", localmm don't handl the key");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
