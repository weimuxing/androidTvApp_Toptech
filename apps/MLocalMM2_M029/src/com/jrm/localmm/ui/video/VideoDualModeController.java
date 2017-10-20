//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2012 - 2015 MStar Semiconductor, Inc. All rights reserved.
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.Metadata;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import com.jrm.localmm.R;
import com.jrm.localmm.business.data.BaseData;
import com.jrm.localmm.business.video.BreakPointRecord;
import com.jrm.localmm.business.video.MediaError;
import com.jrm.localmm.business.video.VideoPlayView;
import com.jrm.localmm.ui.MediaContainerApplication;
import com.jrm.localmm.util.Constants;
import com.jrm.localmm.util.ToastFactory;
import com.jrm.localmm.util.Tools;
import com.mstar.android.MDisplay;
import com.mstar.android.media.AudioTrackInfo;
import com.mstar.android.media.MMediaPlayer;
import com.mstar.android.media.VideoCodecInfo;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tvapi.common.ThreeDimensionManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.Enum3dType;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideo3DTo2D;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureSource;
import com.mstar.android.tvapi.common.vo.EnumAudioProcessorType;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureDeviceType;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.jrm.localmm.util.Bluray;
import android.os.Parcel;
import com.mstar.android.storage.*;
import android.content.ComponentName;
import com.mstar.android.MKeyEvent;


/**
 *
 * Provide controller to dualMode.
 *
 * @date 2015-04
 *
 * @author andrew.wang
 *
 */
    public class VideoDualModeController{

        private final static String TAG= "VideoDualModeController";

        private VideoPlayerActivity context;

        private VideoPlayerViewHolder videoPlayerHolder;

        private Resources resources;

        public VideoDualModeController(VideoPlayerActivity context ,VideoPlayerViewHolder videoPlayerHolder,
                Resources resources) {
            this.context = context;
            this.videoPlayerHolder =videoPlayerHolder;
            this.resources=resources;
        }
    public void switchDualMode() {
        // When switch Dual Mode, should know current video's 3D mode,
        // so add getDisplayFormat to get video's display format.
        context.getDisplayFormat(context, 1);
        String hwName = Tools.getHardwareName();
        if (!videoPlayerHolder.getDualVideoMode() && context.isThreeDMode()) {
            // show toast
            context.showToastTip(resources.getString(R.string.can_not_open_dual));
        } else if (ThreeDimentionControl.getInstance().isPIPMode()) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_pip));
        } else if (!videoPlayerHolder.getDualVideoMode()
                    && context.isVideoSize_4K(1)
                    && !"kano".equals(hwName)) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_4kVideo));
        } else if (context.isCodecTypeHEVC()
                    && !"monaco".equals(hwName)
                    && !"kano".equals(hwName)) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_HEVC_Video));
        } else if (Tools.isThumbnailModeOn()) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_Thumbnail_Video));
        } else if (Tools.isMadisonPlatform() || "clippers".equals(hwName)) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_this_platform));
        } else if (Tools.isRotateModeOn()) {
            context.showToastTip(resources.getString(R.string.can_not_open_dual_rotate_mode));
        } else{
            if (!videoPlayerHolder.getDualVideoMode()) {
                videoPlayerHolder.showVideoFocus(false);
                videoPlayerHolder.currentDualMode = videoPlayerHolder.DUAL_MODE_LEFT_RIGHT;
                videoPlayerHolder.switchFocusedView();
                context.showVideoListDialog();
            } else {
                int time = 0;
                if (videoPlayerHolder.getPlayerView(1) != null) {
                    time = videoPlayerHolder.getPlayerView(1).getCurrentPosition();
                }
                //videoPlayerHolder.isDualVideoClosed = true;
                if (!videoPlayerHolder.isSeekable(1)
                        || context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1] != null) {
                    context.localPauseFromDualSwitch(1, false);
                    if (videoPlayerHolder.getViewId() == 2) {
                        switchDualFocus();
                    }
                    Tools.setHpBtMainSource(true); // all audio device siwtch to main source
                    videoPlayerHolder.openOrCloseDualDecode(false);
                    context.localResumeFromDualSwitch(false);
                    if (context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1] != null ) {
                        if (context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1].isABplaying()) {
                            Constants.abFlag = true;
                            if (context.videoPlayAbDialog[videoPlayerHolder.getViewId() - 1].aFlag) {
                                Constants.aFlag = true;
                            }
                        }
                    }
                } else {
                    videoPlayerHolder.setSeekVar(2, true);
                    if (videoPlayerHolder.getViewId() == 2) {
                        switchDualFocus();
                    }
                    Tools.setHpBtMainSource(true); // all audio device siwtch to main source
                    videoPlayerHolder.openOrCloseDualDecode(false);
                    //seekBarListener.onProgressChanged(videoPlayerHolder.videoSeekBar, time, true);
                    context.isPlaying = videoPlayerHolder.getPlayerView().isPlaying();
                }

                if (Tools.isVideoSWDisplayModeOn()) {
                    // After close dual decode, if video display not by hardware, should set video display aspect ratio
                    // to adapt to the video(S/W) size.
                    if (!context.getVideoPlayHolder().getPlayerView(1).isVideoDisplayByHardware()) {
                        context.setVideoDisplayRotate90(1);
                    }
                }
                videoPlayerHolder.getPlayerView().setDualAudioOn(false);
            }
        }
    }
    public void switchDualFocus() {
        videoPlayerHolder.switchFocusedView();
        //getVideoPlayHolder().setAudioProcessor(getVideoPlayHolder().getViewId());
        String videoName = null;
        int duration = 0;
        int pos = 0;
        int speed = 1;
        int id = videoPlayerHolder.getViewId() - 1;
        if (context.isPrepared()) {
            videoName = context.mVideoPlayList.get(context.video_position[id]).getName();
            duration = (int) videoPlayerHolder.getPlayerView().getDuration();
            pos = context.video_position[id] + 1;
            speed = videoPlayerHolder.getPlayerView().getPlayMode();
            if (speed == 0)
                speed = 1;
        }
        videoPlayerHolder.refreshControlInfo(videoName, speed, pos, context.mVideoPlayList.size(), duration);

        if (context.videoPlayAbDialog[id] != null) {
            Message msg = new Message();
            msg.what = 2;
            msg.arg1 = 0;
            if (context.videoPlayAbDialog[id].bFlag && context.videoPlayAbDialog[id].sharedata != null) {
                msg.arg1 = 1;
            }
            context.videoPlayAbDialog[id].mHandler.sendMessage(msg);
        } else {
            videoPlayerHolder.bt_playA.setVisibility(View.INVISIBLE);
            videoPlayerHolder.bt_playB.setVisibility(View.INVISIBLE);
        }

        context.checkABCycle(videoPlayerHolder.getViewId());
        if (context.mChooseTimePlayDialog != null) {
            context.mChooseTimePlayDialog.setVariable(!videoPlayerHolder.isSeekable(videoPlayerHolder.getViewId()));
        }
        context.videoHandler.removeMessages(context.SEEK_POS);
        context.videoHandler.sendEmptyMessageDelayed(context.SEEK_POS, 1000);
    }
}


