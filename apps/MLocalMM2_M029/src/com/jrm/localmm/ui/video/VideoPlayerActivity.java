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
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.Metadata;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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
import com.mstar.android.media.SubtitleTrackInfo;
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
import android.provider.Settings;

import com.mstar.android.storage.*;
import android.content.ComponentName;
import com.mstar.android.MKeyEvent;

//add by jerry 20160506
import android.media.AudioManager;
import com.mstar.android.tv.TvCecManager.OnCecCtrlEventListener;
import com.mstar.android.tv.TvCecManager;
//end

public class VideoPlayerActivity extends VideoActivityBase {
    private static final String TAG = "VideoPlayerActivity";
    public static final String ACTION_CHANGE_SOURCE = "source.switch.from.storage";
    private static final String LOCAL_MEDIA_SERVICE = "LOCAL_MEDIA_SERVICE";

    private VideoPlaySettingDialog videoPlaySettingDialogOne;
    private VideoPlaySettingDialog videoPlaySettingDialogTwo;
    public VideoPlayABDialog[] videoPlayAbDialog = new VideoPlayABDialog[2];
    private PlaySettingSubtitleDialog playSettingSubtitleDialogOne;
    private PlaySettingSubtitleDialog playSettingSubtitleDialogTwo;
    // choose time Dialog
    public ChooseTimePlayDialog mChooseTimePlayDialog;
    // Video buffer progress bar
    private ProgressDialog progressDialog;
    // Video detailed information
    private DetailInfoDialog mDetailInfoDialog;
    // Video list information
    private VideoListDialog mVideoListDialog;
    private VideoStartTimeDialog mVideoStartTimeDialog;
    private AlertDialog breakPointDialog;
    private OnCecCtrlEventListener mCecCtrlEventListener = null;

    // Video control bar to be automatic hidden time
    private static final int DEFAULT_TIMEOUT = 15 * 1000;

    private int playSpeed = 1;

    public static final int SEEK_POS = 14;

    private int mCurrentVideoPlayerIndex = 0;

    public static final int INIT_THREED = 16;
    public static final int THREED_INIT = 17;
    private static final int IO_ERROR = 9000;
    protected static final int DO_NEXT_OR_PRE_VIDEO = 26;

    private long lastEventTime = 0;
    /*****************ISO FILE SUPPORTED*********************/
    private MStorageManager stm = null ;
    private StringBuilder mountedIsoPath = null;
    private int nBlurayISOTitleNumber = 0;
    private int nBlurayISOTitleIndex = -1;
    private ISOEvnetListener isoEvnetListener = null;
    /*******************************************************/
    private VideoPlayerViewHolder videoPlayerHolder;

    public VideoPlayerViewHolder getVideoPlayHolder() {
        return videoPlayerHolder;
    }

    // can seek?
    private boolean isCanSeek = false;

    // Video is prepared
    private boolean isOnePrepared = false;
    private boolean isTwoPrepared = false;

    // From the VideoActivity selected to play video index,
    // through the index can obtain video detailed information
    protected int[] video_position = new int[2];

    protected int currentViewPosition = 0;

    // The current video broadcast to the position
    private int currentPlayerPosition = 0;

    private int duration = 0;

    // Screen Highlighted lock
    private PowerManager.WakeLock wakelock = null;

    // The current folder data sources
    protected List<BaseData> mVideoPlayList = new ArrayList<BaseData>();

    // The screen resolution width and height
    public static int mScreenResolutionWidth;
    public static int mScreenResolutionHeight;

    // Video article control whether display
    private boolean isControllerShow = true;

    // Video is in play
    public boolean isPlaying = true;

    // Players play source files
    private int sourceFrom;

    private static int seekTimes = 0;

    private int mSeekPosition = 0;

    public EnumThreeDVideoDisplayFormat displayFormat;
    public ThreeDimentionControl mThreeDimentionControl;

    public static final int DETECT_3D_SOURCE_TIMEOUT = 5000;

    private boolean playerIsCreated = true;

    private boolean isAudioSupportOne = true;
    private boolean isVideoSupportOne = true;
    private boolean isAudioSupportTwo = true;
    private boolean isVideoSupportTwo = true;
    private boolean isVideoNone = false;

    private long[] startPreTime = new long[2];
    private long[] endPreTime = new long[2];
    private long[] startRenderingTime = new long[2];
    private long mFirstTimeMillis = 0;
    private long mSecondTimeMillis = 0;

    private float[] oldAnims = null;

    private IWindowManager wm;

    public int breakPoint;
    public boolean isBreakPointPlay = false;

    public boolean bDualAudioOn = false;

    private static boolean isNeedBreakPointPlay = false;

    private boolean isResourceLost = false;

    private int mVideoWidth;
    private int mVideoHeight;

    private String mCurrentVideoPlayPath;

    private LocalMediaController mLocalMediaController = null;
    private ThumbnailController mThumbnailController = null;
    private DivxController divxController = null;

    private String mNetUrlFrom = null;
    private Uri mNetVideoUri;
    private Map<String, String> mHeaders = null;

    private  VideoDualModeController videoDualModeController;

	//zb20160112 add
	private int mAudioTrackCount = 0;
    private PlaySettingAudioTrackDialog mPlaySettingAudioTrackDialog = null;
    private int numberAudioTrack = 0;
	//end
	private ImageView im_play_pause ,im_play_kuaijin,im_play_kuaitui;
    private TextView im_video_player_FF_FR_val;
    
    private VideoPlaySettingDialog videosetting;
    
    private int currentSpeedtop;
    private final int SUBTITLE_INNER = 1;
	
    public void setBreakPointFlag(boolean flag) {
        isNeedBreakPointPlay = flag;
    }

    public boolean getBreakPointFlag() {
        return isNeedBreakPointPlay;
    }

    public class errorStruct {
        protected String strMessage = "";

        // The default mode with error display
        protected boolean showStateWithError = true;

        public errorStruct() {
            super();
            showStateWithError = true;
            strMessage = "";
        }
    }

    public Handler getVideoHandler() {
        return videoHandler;
    }

    public void checkVideoSizeIsSupported() {
        if ("napoli".equalsIgnoreCase(Tools.getHardwareName()) ||
                "muji".equalsIgnoreCase(Tools.getHardwareName())) {
            if (videoPlayerHolder.getDualVideoMode()) {
                if (isVideoSize_4K(videoPlayerHolder.getViewId())) {
                    exitPlayer();
                    showToastTip(getResources().getString(R.string.can_not_open_dual_4kVideo));
                    VideoPlayerActivity.this.finish();
                }
            }
        }
    }

    public void checkVideoCodecIsSupported() {
        if ("napoli".equalsIgnoreCase(Tools.getHardwareName())) {
            if (videoPlayerHolder.getDualVideoMode()) {
                if (isVideoCodec_HEVC(videoPlayerHolder.getViewId())) {
                    exitPlayer();
                    showToastTip(getResources().getString(R.string.can_not_open_dual_HEVC_Video));
                    VideoPlayerActivity.this.finish();
                }
            }
        }
    }

    public boolean isVideoSize_4K(int viewId) {
        if (videoPlayerHolder.getPlayerView(viewId) != null) {
            if (videoPlayerHolder.getPlayerView(viewId).is4kVideo()) {
                Log.i(TAG, "viewId:" + viewId + "is4KVideo");
                return true;
            }
        }
        return false;
    }

    public boolean isVideoCodec_HEVC(int viewId) {
        if (videoPlayerHolder.getPlayerView(viewId) != null) {
            try {
                VideoCodecInfo info = videoPlayerHolder.getPlayerView(viewId).getVideoInfo();
                Log.i(TAG, "info.getCodecType():" + info.getCodecType() + " viewId:" + viewId);
                return "HEVC".equalsIgnoreCase(info.getCodecType());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    public boolean isTwo4kSupported() {
        boolean oneIs4k = false;
        boolean twoIs4k = false;
        String hwName = Tools.getHardwareName();
        if(videoPlayerHolder.getDualVideoMode()) {
            if (!"kano".equals(hwName)) {
                if (videoPlayerHolder.getPlayerView(1) != null) {
                    if (videoPlayerHolder.getPlayerView(1).is4kVideo()) {
                        Log.i(TAG, "Player1 is 4k video.");
                        oneIs4k = true;
                    }
                }
                if (videoPlayerHolder.getPlayerView(2) != null) {
                    if (videoPlayerHolder.getPlayerView(2).is4kVideo()) {
                        Log.i(TAG, "player2 is 4k video.");
                        twoIs4k = true;
                    }
                }
            }
        }
        return !oneIs4k || !twoIs4k;
    }

    // Video processing related handler
    public Handler videoHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            int id = videoPlayerHolder.getViewId();
            switch (msg.what) {
                case Constants.HIDE_PLAYER_CONTROL:
                    hideController();
                    break;
                case DO_NEXT_OR_PRE_VIDEO:
                    doMoveToNextOrPrevios(msg.arg1, msg.arg2);
                    break;
                case SEEK_POS:
                    if (videoPlayerHolder.getPlayerView() != null) {
                        if (videoPlayerHolder.getPlayerView().isInPlaybackState()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String time = null;
                                    int total;
                                    Log.d(TAG,"before getCurrentPosition, currentPlayerPosition = " + currentPlayerPosition);
                                    currentPlayerPosition = videoPlayerHolder.getPlayerView().getCurrentPosition();
                                    time = Tools.formatDuration(currentPlayerPosition);
                                    if (nBlurayISOTitleIndex > -1) {
                                        total = (int) Bluray.getTitleDuration(nBlurayISOTitleIndex)/45;
                                        // int realTime = (int)Bluray.tellTime()/90;
                                        Tools.CURPOS += 500;
                                        if (Tools.CURPOS > total) {
                                            Tools.CURPOS = total;
                                        }

                                        currentPlayerPosition = Tools.CURPOS;
                                        Log.d(TAG,"CURPOS=" + currentPlayerPosition);
                                    }
                                    if (mLocalMediaController != null) {
                                        mLocalMediaController.seekToPosition(currentPlayerPosition);
                                    }

                                    Message msg = videoHandler.obtainMessage(Constants.RefreshCurrentPositionStatusUI);
                                    msg.obj = time;
                                    videoHandler.sendMessage(msg);
                                }
                            }).start();
                        }
                    }
                    break;
                case Constants.RefreshCurrentPositionStatusUI:
                    Log.i(TAG, "RefreshCurrentPositionStatusUI msg.obj:" + (String)msg.obj);
                    videoPlayerHolder.videoSeekBar.setProgress(currentPlayerPosition);
                    videoPlayerHolder.current_time_video.setText((String)msg.obj);
                    if (mChooseTimePlayDialog != null) {
                        mChooseTimePlayDialog.getVideoTimeCurrentPositionTextView().setText((String)msg.obj);
                    }
                    videoHandler.sendEmptyMessageDelayed(SEEK_POS, 1000);
                    break;
                case Constants.SHOW_SUBTITLE_DIALOG:
                    // set subtitle Dialog
                    Bundle mBundle = msg.getData();
                    String index = mBundle.getString("index");
                    Log.i(TAG, "******************" + index);
                    if (id == 1) {
                        playSettingSubtitleDialogOne = new PlaySettingSubtitleDialog(
                                VideoPlayerActivity.this, R.style.dialog, mVideoPlayList.get(
                                        video_position[videoPlayerHolder.getViewId() - 1])
                                        .getPath(), videoPlaySettingDialogOne, VideoPlayerActivity.this.getString(
                                        R.string.subtitle_setting_0_value_1).equals(index));
                    } else {
                        playSettingSubtitleDialogTwo = new PlaySettingSubtitleDialog(
                                VideoPlayerActivity.this, R.style.dialog, mVideoPlayList.get(
                                        video_position[videoPlayerHolder.getViewId() - 1])
                                        .getPath(),videoPlaySettingDialogTwo, VideoPlayerActivity.this.getString(
                                        R.string.subtitle_setting_0_value_1).equals(index));
                    }
                    getPlaySettingSubtitleDialog(id).requestWindowFeature(Window.FEATURE_NO_TITLE);
                    getPlaySettingSubtitleDialog(id).show();
                    hideController();
                    break;
                case Constants.SHOW_SUBTITLE_LIST_DIALOG:
                    // subtitle list Dialog
                    SubtitleListDialog subtitleListDialog = new SubtitleListDialog(
                            VideoPlayerActivity.this, R.style.dialog, mVideoPlayList.get(
                                    video_position[videoPlayerHolder.getViewId() - 1]).getPath(),
                            getPlaySettingSubtitleDialog(id),
                            getPlaySettingSubtitleDialog(id).getAdapter());
                    subtitleListDialog.show();
                    break;
                case INIT_THREED:
                    // 3D settings
                    Thread localThread = new Thread(VideoPlayerActivity.this.mRunnable);
                    localThread.start();
                    break;
                case THREED_INIT:
                    if (Tools.unSupportTVApi()) {
                        return;
                    }
                    if (!videoPlayerHolder.getDualVideoMode()) {
                        try {
                            ThreeDInit();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constants.CHOOSE_TIME:
                    seekBarListener.onProgressChanged(videoPlayerHolder.videoSeekBar, msg.arg1,
                            true);
                    videoPlayerHolder.setVideoTimeSelect(false);
                    break;
                case Constants.SEEK_TIME:
                    // localPause(true);
                    if (VideoPlayerViewHolder.state == VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH) {
                        videoPlayerHolder.getPlayerView(1).seekTo(mSeekPosition);
                        localResumeFromDualSwitch(false);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
                        videoPlayerHolder.setVideoPlaySelect(false, videoPlayerHolder.getPlayerView().isPlaying());
                    } else {
                        videoPlayerHolder.getPlayerView().seekTo(mSeekPosition);
                        // isPlaying = true;
                        videoPlayerHolder.setVideoPlaySelect(true, videoPlayerHolder.getPlayerView().isPlaying());
                        videoHandler.removeMessages(SEEK_POS);
                        videoHandler.sendEmptyMessageDelayed(SEEK_POS, 1000);
                    }
                    setBreakPoint();
                    seekTimes = 0;
                    break;
                case Constants.SET_LEFT_VIDEO_SIZE:
                    videoPlayerHolder.setVideoWindow();
                    break;
                case Constants.HANDLE_MESSAGE_PLAYER_EXIT:
                    VideoPlayerActivity.this.finish();
                    break;
                case Constants.HANDLE_MESSAGE_CHECK_AB:
                    int vId = videoPlayerHolder.getViewId();
                    if (vId == 1) {
                        vId = 2;
                    } else {
                        vId = 1;
                    }
                    if (videoPlayerHolder.getPlayerView(vId) != null) {
                        if (videoPlayerHolder.getPlayerView(vId).isPlaying()) {
                            int position = videoPlayerHolder.getPlayerView(vId)
                                    .getCurrentPosition();
                            if (position >= videoPlayAbDialog[vId - 1].postionB) {
                                videoPlayerHolder.getPlayerView(vId).pause();
                                videoPlayerHolder.getPlayerView(vId).seekTo(
                                        videoPlayAbDialog[vId - 1].postionA);
                                videoPlayerHolder.getPlayerView(vId).start();
                            }
                            videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_CHECK_AB,
                                    1000);
                        }
                    }
                    break;
                case VideoPlayerViewHolder.MSG_VOICEBAR_GONE:
                    videoPlayerHolder.setVoiceLayoutVisibility(false);
                    hideControlDelay();
                    break;
                case Constants.HANDLE_MESSAGE_SKIP_BREAKPOINT:
                    if (breakPointDialog != null && breakPointDialog.isShowing() && !isFinishing()) {
                        breakPointDialog.dismiss();
                    }
                    break;
                case Constants.OSD_3D_UI:
                    if (mThreeDimentionControl != null && !isFinishing()) {
                        // add by avery.yan for fix pr:0400066
                        Thread myThread = new Thread(VideoPlayerActivity.this.mRunnable43D);
                        myThread.start();
                        // end add by avery.yan fof fix pr:0400066
                        // mThreeDimentionControl.setOSD3DMode();
                    }
                    break;
                case Constants.ShowThumbnailBorderView:
                    getThumbnailController().showThumbnailBorderView();
                    break;
                case Constants.SetThumbnailBorderViewOnFocus:
                    getThumbnailController().showThumbnailBorderView();
                    break;
                case Constants.seekThumbnailPos:
                    Log.i(TAG, "msg.arg1:" + msg.arg1 + " mCurrentThumbnailFocusPosition:" + getThumbnailController().getCurrentThumbnailFocusPosition());
                    if (Tools.isThumbnailModeOn()) {
                        int getViewId = videoPlayerHolder.getViewId() - 1;
                        if (videoPlayAbDialog[getViewId] != null) {
                            if (videoPlayAbDialog[getViewId].bFlag && videoPlayAbDialog[getViewId].sharedata != null) {
                                if (msg.arg1 > videoPlayAbDialog[getViewId].sharedata.getInt(VideoPlayABDialog.B_POSITION, 0)) {
                                    msg.arg1 = videoPlayAbDialog[getViewId].sharedata.getInt(VideoPlayABDialog.B_POSITION, 0);
                                } else if (msg.arg1 < videoPlayAbDialog[getViewId].sharedata.getInt(VideoPlayABDialog.A_POSITION, 0)) {
                                    msg.arg1 = videoPlayAbDialog[getViewId].sharedata.getInt(VideoPlayABDialog.A_POSITION, 0);
                                }
                            }
                        }

                        if (msg.arg1 < 0) {
                            msg.arg1 = 0;
                        }

                        // Set video's duration - 10 seconds as Multi Thumbnail 's last position.
                        if (msg.arg1 > videoPlayerHolder.getPlayerView().getDuration()) {
                            msg.arg1 = videoPlayerHolder.getPlayerView().getDuration() - 10000;
                        }

                        if (msg.arg1 == videoPlayerHolder.getPlayerView().getDuration() - 10000) {
                            return;
                        }

                        getThumbnailController().setCurrentThumbnailFocusPosition(msg.arg1);
                        getThumbnailController().getThumbnailFrame(msg.arg1, Tools.getThumbnailNumber(), 2000);

                        // msg.arg2 = 1 means fastForward/slowForward
                        if (msg.arg2 == 1) {
                            Message message = videoHandler.obtainMessage(Constants.seekThumbnailPos);
                            message.arg1 = getThumbnailController().getCurrentThumbnailFocusPosition() + getThumbnailController().getThumbnailPlaySpeed() * 10000;
                            message.arg2 = 1;
                            videoHandler.sendMessageDelayed(message, 3000);
                        }
                    }
                    break;
                case Constants.SeekWithHideThumbnailBorderView:
                    getThumbnailController().setThumbnailPlaySpeed(1);
                    videoHandler.removeMessages(Constants.seekThumbnailPos);
                    videoPlayerHolder.setPlaySpeed(getThumbnailController().getThumbnailPlaySpeed());
                    mSeekPosition = msg.arg1;
                    Log.i(TAG, "mSeekPosition:" + mSeekPosition);
                    videoHandler.removeMessages(Constants.SEEK_TIME);
                    videoHandler.sendEmptyMessage(Constants.SEEK_TIME);
                    break;
                case Constants.OpenThumbnailPlayer:
                    Log.i(TAG, "videoHandler Constants.OpenThumbnailPlayer");
                    if (Tools.isThumbnailModeOn()) {
                        getThumbnailController().getThumbnailViewHolder().initGLSurfaceView(mCurrentVideoPlayPath);
                    }
                    break;
                case Constants.HideThumbnailBorderView:
                    getThumbnailController().hideThumbnailBorderView();
                    break;
                case Constants.PrepareMediaPlayer:
                    getThumbnailController().getThumbnailViewHolder().getVideoGLSurfaceView().prepareMediaPlayer();
                    break;
                case Constants.ShowController:
                    if (!isControllerShow) {
                        showController();
                        hideControlDelay();
                    }
                    break;
                case Constants.DualAudioOn:
                    videoPlayerHolder.getPlayerView().setDualAudioOn(true);
                    break;
                case Constants.INIT_THUMBNAIL_PLAYER:
                    if (getThumbnailController().getThumbnailViewHolder().getVideoGLSurfaceView() != null) {
                        getThumbnailController().getThumbnailViewHolder().getVideoGLSurfaceView().initThumbnailPlayer(mCurrentVideoPlayPath);
                    }
                    break;
                case Constants.CHECK_IS_SUPPORTED:
                    if (!isTwo4kSupported()) {
                        Log.i(TAG, "Does not support the two 4k video!");
                        exitPlayer();
                        showToastTip(getResources().getString(R.string.video_no_support_two_4k));
                        VideoPlayerActivity.this.finish();
                        return;
                    }
                    break;
                default:
                    break;
            }
        };
    };

    // play control Handler
    private Handler toBePlayedHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int id = videoPlayerHolder.getViewId();
            if (id == 2 && videoPlayerHolder.getPlayerView(id).getMMediaPlayer() == null) {
                localPauseFromDualSwitch(1, false);
                videoPlayerHolder.openOrCloseDualDecode(true);
                localResumeFromDualSwitch(false);
            }

            if (isVideoNone) {
                videoPlayerHolder.showDefaultPhoto(false);
                isVideoNone = false;
            }
            Bundle data = msg.getData();
            videoPlayerHolder.reset();
            video_position[id - 1] = data.getInt("index", 0);
            if (video_position[id - 1] < 0 || video_position[id - 1] > mVideoPlayList.size())
                video_position[id - 1] = 0;
            mCurrentVideoPlayerIndex = video_position[id - 1];
            String videoName = mVideoPlayList.get(video_position[id - 1]).getName();
            InitVideoPlayer(mVideoPlayList.get(video_position[id - 1]).getPath(), id);
            videoPlayerHolder.setVideoName(videoName);
            videoPlayerHolder.setVideoListText(video_position[id - 1] + 1, mVideoPlayList.size());
            showController();
            if (id ==2) {
                Tools.setHpBtMainSource(false); // change earphone and bluetooth to sub source
                if (mChooseTimePlayDialog != null) {
                    mChooseTimePlayDialog.setVariable(!videoPlayerHolder.isSeekable(id));
                }
            }
            hideControlDelay();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThreeDimentionControl.getInstance().init3DMode();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        showProgressDialog(R.string.buffering);
        String video_path;
        String videoName;
        /*
         * video_position = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY,
         * 0); mVideoPlayList = getIntent().getParcelableArrayListExtra(
         * Constants.BUNDLE_LIST_KEY);
         */
        video_position[0] = getIntent().getIntExtra(Constants.BUNDLE_INDEX_KEY, 0);
        // mVideoPlayList =
        // getIntent().getParcelableArrayListExtra(Constants.BUNDLE_LIST_KEY);
        String IntentPath = Tools.parseUri(getIntent().getData());
        Bundle bundle = getIntent().getBundleExtra("headers");
        mNetUrlFrom = getIntent().getStringExtra("URL_FROM");
        mNetVideoUri = getIntent().getData();
        if (bundle != null) {
            Set<String> keySet = bundle.keySet();
            Iterator<String> iter = keySet.iterator();
            mHeaders = new HashMap<String, String>();
            while(iter.hasNext())
            {
                String key = iter.next();
                mHeaders.put(key, bundle.getString(key));
            }
        }
        Log.i(TAG, "mNetUrlFrom:" + mNetUrlFrom + " mNetVideoUri:" + mNetVideoUri);
        if (IntentPath != null) {
            BaseData bd = new BaseData();
            bd.setPath(IntentPath);
            bd.setName(Tools.getFileName(IntentPath));
            video_position[0] = 0;
            bd.setSize("1024");
            mVideoPlayList.add(bd);
        } else {
            mVideoPlayList = MediaContainerApplication.getInstance().getMediaData(
                Constants.FILE_TYPE_VIDEO);
        }

        if (mVideoPlayList.size() == 0) {
            Log.i(TAG, "mVideoPlayList.size() == 0 !!!");
            this.finish();
            return;
        }

        video_path = mVideoPlayList.get(video_position[0]).getPath();
        videoName = mVideoPlayList.get(video_position[0]).getName();
        sourceFrom = getIntent().getIntExtra(Constants.SOURCE_FROM, 0);
        Display currDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        currDisplay.getRealSize(point);
        mScreenResolutionWidth = point.x;
        mScreenResolutionHeight = point.y;
        Constants.sceenResolution = point;
        Log.i(TAG, "mScreenResolutionWidth:" + mScreenResolutionWidth + " mScreenResolutionHeight:" + mScreenResolutionHeight);
        setContentView(R.layout.video_player_frame2);
        InitView();
        ControlButtonListener listener = new ControlButtonListener();
        videoPlayerHolder.setOnClickListener(listener);
        initObject();
        Tools.CURPOS = 0;
        isNeedBreakPointPlay = BreakPointRecord.getBreakPointFlag(this);

        mCurrentVideoPlayerIndex = video_position[0];

        InitVideoPlayer(video_path, 1);
        if (nBlurayISOTitleIndex >= 0 && nBlurayISOTitleNumber > 0) {
            videoPlayerHolder.setVideoName(videoName + " Bluray ISO tile " + (nBlurayISOTitleIndex + 1) +" of " + nBlurayISOTitleNumber);
        }else {
            videoPlayerHolder.setVideoName(videoName);
        }
        //videoPlayerHolder.setVideoName(videoName);
        videoPlayerHolder.setVideoListText(video_position[videoPlayerHolder.getViewId() - 1] + 1,
                mVideoPlayList.size());
        acquireWakeLock();
        hideControlDelay();
        // register shutdown broadcast
        IntentFilter ittfile = new IntentFilter();
        ittfile.addAction(Intent.ACTION_SHUTDOWN);
        ittfile.addAction(ACTION_CHANGE_SOURCE);
        this.registerReceiver(shutDownReceiver, ittfile);
        if (!Tools.unSupportTVApi()) {
            Tools.changeSource(true);
        }
        wm = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));

        Thread breakPointThread = new Thread(VideoPlayerActivity.this.breakPointRunnable);
        breakPointThread.start();
        if (!Tools.unSupportTVApi()) {
            TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {
                @Override
                public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onKeyInfo");
                    return false;
                }

                @Override
                public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onAutoSwitch arg0:" + arg0 + " arg1:" + " arg2:" + arg2);
                    exitPlayer();
                    //TvCommonManager.getInstance().setInputSource(EnumInputSource.E_INPUT_SOURCE_HDMI3);
                    TvCommonManager.getInstance().setInputSource(arg1);
                    Intent intent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
                    // ComponentName componentName = new ComponentName("mstar.tvsetting.ui",
                    //        "mstar.tvsetting.ui.RootActivity");
                    // Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    // intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    intent.putExtra("task_tag", "input_source_changed");
                    VideoPlayerActivity.this.startActivity(intent);

                    finish();
                    return false;
                }
            });
        }

        if (mNetUrlFrom != null && LOCAL_MEDIA_SERVICE.equalsIgnoreCase(mNetUrlFrom)) {
            mLocalMediaController = new LocalMediaController(this);
        }

        mThumbnailController = new ThumbnailController(this, videoPlayerHolder.getThumbnailViewHolder());
        if(Constants.bSupportDivx) {
            divxController = new DivxController(this, videoPlayerHolder);
        }
        if (Tools.isFloatVideoPlayModeOn()) {
            FloatVideoController.getInstance().setVideoPlayList(Constants.DB_NAME, Constants.VIDEO_PLAY_LIST_TABLE_NAME,
                    mVideoPlayList, mCurrentVideoPlayerIndex);

        }
        videoDualModeController=new VideoDualModeController(VideoPlayerActivity.this,getVideoPlayHolder(),getResources());
        im_play_pause = (ImageView)findViewById(R.id.im_video_player_pause);
        im_play_kuaijin = (ImageView)findViewById(R.id.im_video_player_kuaijin);
        im_play_kuaitui = (ImageView)findViewById(R.id.im_video_player_kuaitui);
        im_video_player_FF_FR_val = (TextView)findViewById(R.id.im_video_player_FF_FR_val);
    }


    @Override
    public void finish() {
        //send a broadcast when finish , pip/pop will use it
        //mantis-0692032
        sendBroadcast(new Intent("com.jrm.localmm.VideoPlayerActivity.FINISH_SELF"));
        super.finish();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "***************onDestroy********");
        // mantis-0636831:Others module(such as MtvMisc.apk),
        // need exec in different way by the property when localmm dual playing.
        Tools.setSystemProperty("mstar.localmm.dual-playing","false");
        if (Tools.isThumbnailModeOn()) {
            getThumbnailController().releaseThumbnailView(false);
        }
        if (Tools.isRotateModeOn()) {
            Tools.setRotateMode("0");
        }
        if (wakelock != null) {
            wakelock.release();
        }
        // avoid the player send Exception to activity after destroyed
        if(videoPlayerHolder != null) {
            if (videoPlayerHolder.mVideoPlayViewOne != null) {
                videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
            }
            if (videoPlayerHolder.mVideoPlayViewTwo != null) {
                videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
            }
            Tools.setVideoWindowVisable(true, true);
        }
        BreakPointRecord.closeDB();
        dismissProgressDialog();
        try {
            unregisterReceiver(netDisconnectReceiver);
            unregisterReceiver(diskChangeReceiver);
            unregisterReceiver(shutDownReceiver);
            unregisterReceiver(homeKeyEventBroadCastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Constants.subTitleSettingOptText = defaultSubArray;
                Tools.destroyAllSettingOpt();
                SubtitleManager.destroySubtitleSettingOpt() ;
                AudioTrackManager.destroyAudioTrackOption() ;
                // videoPlayerHolder.mVideoPlayView.stopPlayback();
                SubtitleManager.mVideoSubtitleNo = 0;
                Constants.abFlag = false;
            }
        }).start();

        if (mLocalMediaController != null) {
            mLocalMediaController.exitMediaPlayer();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "***************onPause********");
        if (Tools.isThumbnailModeOn()) {
            getThumbnailController().hideThumbnailBorderView();
            getThumbnailController().getThumbnailViewHolder().getVideoGLSurfaceView().releaseThumbnailPlayer(false);
        }
        hideController();
        // Began to disk scan
        new Thread(new Runnable() {
            @Override
            public void run() {
                startMediascanner();
                if (wm != null) {
                    try {
                        wm.setAnimationScales(oldAnims);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "***************onStop****finish****");
        if (Tools.isThumbnailModeOn()) {
            getThumbnailController().hideThumbnailBorderView();
            getThumbnailController().getThumbnailViewHolder().getVideoGLSurfaceView().releaseThumbnailPlayer(false);
        }
        if (videoPlayerHolder.mVideoPlayViewOne != null) {
            videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
            videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
        }
        if (videoPlayerHolder.mVideoPlayViewTwo != null) {
            videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
            videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
        }
        TvCecManager.getInstance().unregisterOnCecCtrlEventListener(mCecCtrlEventListener);
        mCecCtrlEventListener = null;
        ThreeDimentionControl.getInstance().disable3dDualView();
        VideoPlayerActivity.this.finish();
        MDisplay.set3DDisplayMode(0);
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "***************onStart********");
        super.onStart();
        mCecCtrlEventListener = new CecCtrlEventListener();
        TvCecManager.getInstance().registerOnCecCtrlEventListener(mCecCtrlEventListener);
    }
    @Override
    protected void onResume() {
        Log.i(TAG, "***************onResume********");
        // Stop disk scan
        stopMediascanner();
        // Tools.changeSource(true);
        // init3DMode();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        // Registered disk change broadcast receiver
        registerReceiver(diskChangeReceiver, filter);
        IntentFilter networkFilter = new IntentFilter("com.mstar.localmm.network.disconnect");
        registerReceiver(netDisconnectReceiver, networkFilter);
        registerReceiver(homeKeyEventBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        // Initialization subtitles options value
        SubtitleManager.initSubtitleSettingOpt(VideoPlayerActivity.this, 1);
        SubtitleManager.initSubtitleSettingOpt(VideoPlayerActivity.this, 2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (wm != null) {
                    try {
                        // Close window animation,to avoid screen flicker while
                        // changing video size or position.
                        oldAnims = wm.getAnimationScales();
                        wm.setAnimationScale(0, 0.0f);
                        wm.setAnimationScale(1, 0.0f);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        super.onResume();
    }

    public List<BaseData> getVideoPlayList() {
        return mVideoPlayList;
    }

    private void initObject() {
        if (!Tools.unSupportTVApi()) {
            mThreeDimentionControl = ThreeDimentionControl.getInstance();
            mThreeDimentionControl.setMediaPlayer(videoPlayerHolder.getPlayerView());
            mThreeDimentionControl.setHandler(videoHandler);
            mThreeDimentionControl.setContext(this);
        }
    }

    private boolean isISOFile(String videoPlayPath){
        if (videoPlayPath.toLowerCase().endsWith(".iso")) {
            return true;
        }

        if (videoPlayPath.toLowerCase().endsWith(".bdmv")) {
            return true;
        }

        return false;
    }

    private void showISOTip() {
        // showToastTip(getResources().getString(R.string.iso_tip));
        String strMessage = getResources().getString(R.string.video_iso_tip);
        Toast toast = ToastFactory.getToast(VideoPlayerActivity.this, strMessage, Gravity.CENTER);
        // toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void playISOFile(String ISOFilePath, int viewId) {
        if (!Bluray.isSupport(this)) {
            String msg = getResources().getString(
                            R.string.video_media_error_video_unsupport);
            showErrorDialog(msg , viewId , true);
            return;
        }

        if (ISOFilePath != null) {
            if (stm == null) {
                stm = MStorageManager.getInstance(this);
            }
            String mountPath = null;
            if (isoEvnetListener == null) {
                    isoEvnetListener = new ISOEvnetListener();
                    isoEvnetListener.name = "ISOEvnetListener1";
            }
            if (ISOFilePath.toLowerCase().endsWith(".iso")) {
               mountPath = stm.getISOFileMountPath(ISOFilePath);
               if (mountPath == null) {
                   if (mountedIsoPath != null) {
                       Bluray.deinit();
                       stm.unmountISO(mountedIsoPath.toString(), true);
                       mountedIsoPath = null;
                   }
                   mountedIsoPath = new StringBuilder(ISOFilePath);
                   if (stm.mountISO(ISOFilePath, isoEvnetListener)) {
                       mountPath = stm.getISOFileMountPath(ISOFilePath);
                       Log.i(TAG,"the last mount path is:"+mountPath);
                   }
               }
            } else {
               int pathindex = ISOFilePath.lastIndexOf('/');
               if (pathindex > 0) {
                   char []newPath = new char[pathindex];
                   ISOFilePath.getChars(0, pathindex, newPath, 0);
                   pathindex = (new String(newPath)).lastIndexOf('/');
                   if (pathindex > 0) {
                      mountPath = new String(newPath, 0, pathindex);
                   }
               }
            }
            if (nBlurayISOTitleIndex <= 0) {
                if (Bluray.init(mountPath, "")) {
                    Bluray.setEnable3D(true);
                    nBlurayISOTitleNumber = Bluray.getTitles();
                    if (nBlurayISOTitleNumber > 0) {
                        nBlurayISOTitleIndex = 0;
                        Log.i(TAG, "nBlurayISOTitleNumber " + nBlurayISOTitleNumber);
                        long nduration = 0;
                        for (int i = 0; i < nBlurayISOTitleNumber; i++) {
                             long titDur = Bluray.getTitleDuration(i);
                             if (titDur > 180000) {
                                 nBlurayISOTitleIndex = i;
                                 break;
                             }
                             Log.i(TAG,"the title "+i+" duration is:"+titDur);
                        }
                    }
                } else {
                    if (mountedIsoPath != null) {
                       Bluray.deinit();
                       stm.unmountISO(mountedIsoPath.toString(), true);
                       mountedIsoPath = null;
                    }
                    String eMessage = "The libblurary.so is needed which is protected by CopyRight!";
                    showErrorDialog(eMessage,viewId);
                    return;
                }
            }
            String ISOPath = "BlurayISO://" + Long.toHexString(Bluray.getNativePointer()) + "index" + nBlurayISOTitleIndex;
            videoPlayerHolder.getPlayerView(viewId).setVideoPath(ISOPath,viewId);
            String videoName = mVideoPlayList.get(video_position[0]).getName();
            videoPlayerHolder.setVideoName(videoName + " Bluray ISO title " + (nBlurayISOTitleIndex + 1) +" of " + nBlurayISOTitleNumber);
        }
    }


    private class ISOEvnetListener extends OnISOEvnetListener {
        public String name = TAG;

        /* onISOStateChange()*/
        @Override
        public void onISOStateChange(String filename, int state) {
            if (state == OnISOEvnetListener.MOUNTED) {
                Log.i(name,"ISO Mounted:"+filename);
            } else if (state == OnISOEvnetListener.UNMOUNTED) {
                Log.i(name,"ISO Unmounted:"+filename);
            } else {
                Log.i(name,"Unknown state:"+state);
            }
        }
    }

    /**
     * Initialization view
     */
    private void InitView() {
        videoPlayerHolder = new VideoPlayerViewHolder(VideoPlayerActivity.this, mScreenResolutionWidth,
                mScreenResolutionHeight);
        videoPlayerHolder.setActivity(VideoPlayerActivity.this, videoHandler);
        videoPlayerHolder.videoSeekBar.setOnSeekBarChangeListener(seekBarListener);
        // Video control article
        videoPlayerHolder.playControlLayout = (LinearLayout) findViewById(R.id.video_suspension_layout);
        videoPlayerHolder.playControlLayout.setOnKeyListener(controlOnKeyListener);
    }

    private OnKeyListener controlOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

            return false;
        }
    };

    /**
     *
     */
    private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int id = videoPlayerHolder.getViewId();
            // The progress of the progress bar is changed
            if (fromUser) {
                Log.i(TAG, "onProgressChanged fromUser:" + fromUser);
                if (videoPlayAbDialog[id - 1] != null) {
                    // Switch open and B point set
                    if (videoPlayAbDialog[id - 1].bFlag
                            && videoPlayAbDialog[id - 1].sharedata != null) {
                        if (progress <= videoPlayAbDialog[id - 1].sharedata.getInt(
                                VideoPlayABDialog.B_POSITION, 0)
                                && progress >= videoPlayAbDialog[id - 1].sharedata.getInt(
                                        VideoPlayABDialog.A_POSITION, 0)) {
                            videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                    .isPlaying());
                            mSeekPosition = progress;
                            videoHandler.removeMessages(Constants.SEEK_TIME);
                            if (++seekTimes == 1) {
                                videoHandler.sendEmptyMessage(Constants.SEEK_TIME);
                            } else {
                                videoHandler.sendEmptyMessageDelayed(Constants.SEEK_TIME, 1000);
                            }
                            VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
                        }
                    } else if (videoPlayAbDialog[id - 1].aFlag) {
                        if (progress > videoPlayAbDialog[id - 1].sharedata.getInt(
                                VideoPlayABDialog.A_POSITION, 0)) {
                            videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                    .isPlaying());
                            mSeekPosition = progress;
                            videoHandler.removeMessages(Constants.SEEK_TIME);
                            if (++seekTimes == 1) {
                                videoHandler.sendEmptyMessage(Constants.SEEK_TIME);
                            } else {
                                videoHandler.sendEmptyMessageDelayed(Constants.SEEK_TIME, 1000);
                            }
                            VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
                        }
                        else {
                            showToastTip(getResources().getString(R.string.choose_time_invalid));
                        }
                    } else {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        mSeekPosition = progress;
                        videoHandler.removeMessages(Constants.SEEK_TIME);
                        if (++seekTimes == 1) {
                            videoHandler.sendEmptyMessage(Constants.SEEK_TIME);
                        } else {
                            videoHandler.sendEmptyMessageDelayed(Constants.SEEK_TIME, 1000);
                        }
                        hideControlDelay();
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
                    }
                } else if (videoPlayerHolder.isSeekable(id)) {
                    videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView().isPlaying());
                    cancleDelayHide();

                    mSeekPosition = progress;
                    videoHandler.removeMessages(Constants.SEEK_TIME);

                    if (++seekTimes == 1) {
                        videoHandler.sendEmptyMessage(Constants.SEEK_TIME);
                        hideControlDelay();
                        return;
                    } else {
                        videoHandler.sendEmptyMessageDelayed(Constants.SEEK_TIME, 1000);
                    }
                    hideControlDelay();
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
                } else {
                    showToastTip(getResources().getString(R.string.choose_time_failed));
                }
            } else {
                if (videoPlayAbDialog[id - 1] != null) {
                    if (videoPlayAbDialog[id - 1].bFlag && videoPlayAbDialog[id - 1].sharedata != null) {
                        if (progress >= videoPlayAbDialog[id - 1].sharedata.getInt(VideoPlayABDialog.B_POSITION, 0)
                                || (playSpeed < 0 && progress < videoPlayAbDialog[id - 1].sharedata
                                        .getInt(VideoPlayABDialog.A_POSITION, 0))) {
                            // localPause(true);
                            videoPlayerHolder.videoSeekBar.setProgress(videoPlayAbDialog[id - 1].sharedata.getInt(
                                            VideoPlayABDialog.A_POSITION, 0));
                            videoPlayerHolder.getPlayerView().seekTo(videoPlayAbDialog[id - 1].sharedata.getInt(
                                            VideoPlayABDialog.A_POSITION, 0));
                            // localResume(true);
                            videoPlayerHolder.getPlayerView().setPlayMode(1);
                            // isPlaying = true;
                            // videoPlayerHolder.setVideoPlaySelect(true, isPlaying);
                        }
                    } else if (videoPlayAbDialog[id - 1].aFlag
                            && videoPlayAbDialog[id - 1].sharedata != null) {
                        if (playSpeed < 0 && progress < videoPlayAbDialog[id - 1].sharedata.getInt(VideoPlayABDialog.A_POSITION, 0)) {
                            videoPlayerHolder.videoSeekBar.setProgress(videoPlayAbDialog[id - 1].sharedata.getInt(VideoPlayABDialog.A_POSITION, 0));
                            videoPlayerHolder.getPlayerView().seekTo( videoPlayAbDialog[id - 1].sharedata.getInt(VideoPlayABDialog.A_POSITION, 0));
                            videoPlayerHolder.getPlayerView().setPlayMode(1);
                            isPlaying = true;
                            videoPlayerHolder.setVideoPlaySelect(true, isPlaying);
                        }
                    }
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(videoPlayerHolder.getPlayerView().getDuration()==0){
                showToastTip(VideoPlayerActivity.this.getString(R.string.choose_time_failed));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isControllerShow) {
                showController();
                hideControlDelay();
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "*****onKeyUp keyCode******" + keyCode);
        boolean bRet = false;
        switch (keyCode) {
			//zb20151228 add
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			playSpeed = videoPlayerHolder.getPlayerView().getPlayMode();
			if (playSpeed == 1) {
	               if (isPlaying) {
	                 localPause(true);
	                }else{				
					if (!isPlaying) 
	                    localResume(true); 
	                }
			}else {
               localResumeFromSpeed(true);
                }
			bRet = true;
            break;
			case KeyEvent.KEYCODE_INFO:
                cancleDelayHide();
                while (!isCanSeek && videoPlayerHolder.state != videoPlayerHolder.OPTION_STATE_INFO)
                    videoPlayerHolder.processLeftKey(keyCode, event);
				if (isPrepared()) {
					showVideoInfoDialog();
				}
				bRet = true;
            break;
			case MKeyEvent.KEYCODE_MTS:
        	{
        		mAudioTrackCount = getAudioTrackCount();
        		changeAudioTrack(1);
        	}
        	bRet = true;
        	break;
        	case MKeyEvent.KEYCODE_SUBTITLE:
        	{
        		if (isPrepared()) {
                    videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                            .isPlaying());
                    cancleDelayHide();
                    if (nBlurayISOTitleIndex < 0) {
                        showSettingDialog();
                    } else {
                        showISOTip();
                    }
                    hideControlDelay();
                    videoPlayerHolder.setVideoSettingSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_SETTING;
                }
        	}
        	bRet = true;
        	break;
			//end
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                isPlaying = videoPlayerHolder.getPlayerView().isPlaying();
                if (!isPlaying) {
                    localResume(true);
                } else {
                    playSpeed = videoPlayerHolder.getPlayerView().getPlayMode();
                    if (playSpeed != 1) {
                        // localPause(true);
                        // localResume(true);
                        localResumeFromSpeed(true);
                    }
                }
                bRet = true;
                break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                isPlaying = videoPlayerHolder.getPlayerView().isPlaying();
                if (isPlaying) {
                    localPause(true);
                }
                bRet = true;
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                if (isPrepared()) {
					videoPlayerHolder.setAllUnSelect(true);
					videoPlayerHolder.setVideoNextSelect(true);
                    moveToNextOrPrevious(1, videoPlayerHolder.getViewId());
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                if (isPrepared()) {
					videoPlayerHolder.setAllUnSelect(true);
					videoPlayerHolder.setVideoPreSelect(true);
                    moveToNextOrPrevious(-1, videoPlayerHolder.getViewId());
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                slowForward();
                videoPlayerHolder.setRewindorForwardSelect(false);
                break;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                fastForward();
                videoPlayerHolder.setRewindorForwardSelect(true);
                break;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                if (Constants.bSupportDivx) {
                    if(divxController != null) {
                        divxController.keyStopProcess(event, videoHandler);
                    }
                 } else {
                     if (videoPlayerHolder.isVoiceLayoutShow) {
                         videoHandler.sendEmptyMessage(VideoPlayerViewHolder.MSG_VOICEBAR_GONE);
                     } else if (videoPlayerHolder.isDualLayoutShow) {
                         videoPlayerHolder.setDualModeLayoutVisibility(false);
                     } else {
                         videoHandler.removeMessages(SEEK_POS);
                         if (videoPlayerHolder.mVideoPlayViewOne != null) {
                             if (Constants.bSupportDivx) {
                                 Tools.setResumePlayState(1);
                             }
                             if (isOnePrepared && mVideoPlayList.size() > 0) {
                                  BreakPointRecord.addData(mVideoPlayList.get(video_position[0]).getPath(), videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition(), mVideoPlayList.get(video_position[0]).getSize(), this);
                             }
                             videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
                             videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                         }
                         if (videoPlayerHolder.mVideoPlayViewTwo != null) {
                              if (Constants.bSupportDivx) {
                                  Tools.setResumePlayState(1);
                              }
                              if (isTwoPrepared && mVideoPlayList.size() > 0) {
                                  BreakPointRecord.addData(mVideoPlayList.get(video_position[1]).getPath(), videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition(), mVideoPlayList.get(video_position[1]).getSize(), this);
                              }
                              videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
                              videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
                         }
                          // this.finish();
                         videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
                    }
                    bRet = true;
                }
                break;

            case KeyEvent.KEYCODE_BACK:
                if (Tools.isThumbnailModeOn()) {
                    Tools.setThumbnailMode("0");
                    getThumbnailController().releaseThumbnailView(true);
                }
                if (videoPlayerHolder.isVoiceLayoutShow) {
                    videoHandler.sendEmptyMessage(VideoPlayerViewHolder.MSG_VOICEBAR_GONE);
                } else if (videoPlayerHolder.isDualLayoutShow) {
                    videoPlayerHolder.setDualModeLayoutVisibility(false);
                } else {
                    videoHandler.removeMessages(SEEK_POS);
                    if (isControllerShow) {
                        hideController();
                    }
                    if (videoPlayerHolder.mVideoPlayViewOne != null) {
                        if (Constants.bSupportDivx) {
                            Tools.setResumePlayState(1);
                        }
                        if (isOnePrepared && mVideoPlayList.size() > 0) {
                            BreakPointRecord.addData(
                                    mVideoPlayList.get(video_position[0]).getPath(),
                                    videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition(),
                                    mVideoPlayList.get(video_position[0]).getSize(), this);
                        }
                        videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
                        videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                    }
                    if (videoPlayerHolder.mVideoPlayViewTwo != null) {
                        if (Constants.bSupportDivx) {
                            Tools.setResumePlayState(1);
                        }
                        if (isTwoPrepared && mVideoPlayList.size() > 0) {
                            BreakPointRecord.addData(
                                    mVideoPlayList.get(video_position[1]).getPath(),
                                    videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition(),
                                    mVideoPlayList.get(video_position[1]).getSize(), this);
                        }
                        videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
                        videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
                    }
                    // this.finish();
                    videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
                }
                bRet = true;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!isControllerShow)
                    return super.onKeyUp(keyCode, event);
                if (Tools.isThumbnailModeOn() && getThumbnailController().getThumbnailOnFocus()) {
                    return super.onKeyUp(keyCode, event);
                }
				
				if (videoPlayerHolder.isVoiceLayoutShow)//hexing2016-0531 add for hide voice bar
				{
				videoHandler.sendEmptyMessage(VideoPlayerViewHolder.MSG_VOICEBAR_GONE);
				}
                cancleDelayHide();
                if (!isCanSeek) {
                    videoPlayerHolder.processLeftKey(keyCode, event);
                } else {
                    rewind();
                }
                hideControlDelay();
                bRet = true;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (!isControllerShow) {
                }
                if (Tools.isThumbnailModeOn() && getThumbnailController().getThumbnailOnFocus()) {
                    return super.onKeyUp(keyCode, event);
                }
				
				if (videoPlayerHolder.isVoiceLayoutShow)//hexing2016-0531 add for hide voice bar
				{
				videoHandler.sendEmptyMessage(VideoPlayerViewHolder.MSG_VOICEBAR_GONE);
				}
				
                cancleDelayHide();
                if (!isCanSeek) {
                    videoPlayerHolder.processRightKey(keyCode, event);
                } else {
                    wind();
                }
                hideControlDelay();
                bRet = true;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (videoPlayerHolder.isDualLayoutShow) {
                    videoPlayerHolder.processOkKey(keyCode, event);
                    hideControlDelay();
                } else {
                    registerListeners();
                }
                bRet = true;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (videoPlayerHolder.isDualLayoutShow) {
                    cancleDelayHide();
                    videoPlayerHolder.processUpKey(keyCode, event);
                }
                if (videoPlayerHolder.isVoiceLayoutShow) {
                    cancleDelayHide();
                    videoPlayerHolder.addVoice(true);
                }
                bRet = true;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (videoPlayerHolder.isDualLayoutShow) {
                    cancleDelayHide();
                    videoPlayerHolder.processDownKey(keyCode, event);
                }
                if (videoPlayerHolder.isVoiceLayoutShow) {
                    cancleDelayHide();
                    videoPlayerHolder.addVoice(false);
                }
                bRet = true;
                break;
            case KeyEvent.KEYCODE_MENU:
                if (Tools.isThumbnailModeOn()) {
                    if (getThumbnailController().getThumbnailPlaySpeed() != 1) {
                        getThumbnailController().setThumbnailPlaySpeed(1);
                        videoPlayerHolder.setPlaySpeed(getThumbnailController().getThumbnailPlaySpeed());
                        videoHandler.removeMessages(Constants.seekThumbnailPos);
                    }
                }
                if (!isControllerShow) {
                    showController();
                    hideControlDelay();
                } else {
                	if(videosetting.videosettingflag){
						
					if (videoPlayerHolder.isVoiceLayoutShow)//hexing2016-0531 add for hide voice bar
					{
					videoHandler.sendEmptyMessage(VideoPlayerViewHolder.MSG_VOICEBAR_GONE);
					}
						
                    hideController();
                	}
                	videosetting.videosettingflag = true;
                }
                bRet = true;
                break;
            case KeyEvent.KEYCODE_0:
                int index = videoPlayerHolder.getViewId() - 1;
                if (startRenderingTime[index] != 0) {
                    startPreTime[index] = videoPlayerHolder.getPlayerView(index + 1).getStartTime();
                    mVideoStartTimeDialog = new VideoStartTimeDialog(this, R.style.dialog, endPreTime[index]
                            - startPreTime[index], startRenderingTime[index] - endPreTime[index],
                            mVideoPlayList.get(video_position[index]).getName());
                    mVideoStartTimeDialog.show();
                }
                break;
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_5:
                if (Tools.isThumbnailModeOn()) {
                    getThumbnailController().dispatchKeyEvent(keyCode - 7);
                }
                break;
            default:
                bRet = false;
                break;
        }
        if (bRet) {
            return true;
        } else {
            if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
                return true;
            }
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "---------- onKeyDown -------- keyCode:" + keyCode);

        if (Tools.isThumbnailModeOn()) {
            getThumbnailController().respondThumbnailOnKeyDown(keyCode, event);
        }

        /*
         * Key code constant: Volume Up key. Adjusts the speaker volume up.
         * Key code constant: Volume Down key.Adjusts the speaker volume down.
         */
        if (KeyEvent.KEYCODE_VOLUME_UP == keyCode
            || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode
            || KeyEvent.KEYCODE_VOLUME_MUTE == keyCode) {
            if (ThreeDimentionControl.getInstance().sendCecKey(keyCode) == true) {
                return true;
            }
        }

        if (keyCode == MKeyEvent.KEYCODE_MSTAR_SUBCODE) {//goto key
            if (isPrepared()) 
            {
                videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                           .isPlaying());
                VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_TIME;
                cancleDelayHide();
                showVideoTimeSetDialog();
                hideControlDelay();
            }
            return true;
        }

        /*
         * if (KeyEvent.KEYCODE_ASPECT_RATIO == keyCode) { return true; }
         */
        if (MKeyEvent.KEYCODE_ASPECT_RATIO == keyCode) {
            Log.i(TAG, "video width:" + mVideoWidth + ", video height:" + mVideoHeight);
            //aspect ratio will be rejected when the video size is more than 1920 * 1200
            if (Tools.getHardwareName() == "clippers" && (mVideoWidth > 1920 || mVideoHeight > 1200)) {
                Toast.makeText(this, getResources().getString(R.string.can_not_support_aspect_ratio), Toast.LENGTH_SHORT).show();
                return true;
            }
        if ((!videoPlayerHolder.getDualVideoMode() && !isVideoSupportOne) ||
            (videoPlayerHolder.getDualVideoMode() && !isVideoSupportOne && !isVideoSupportTwo)) {
                Toast.makeText(this, getResources().getString(R.string.can_not_support_aspect_ratio), Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        if (KeyEvent.KEYCODE_MEDIA_PLAY == keyCode || KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_NEXT == keyCode
                || KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode
                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
            return true;
        }
        if (!isControllerShow) {
            if (KeyEvent.KEYCODE_ENTER != keyCode && KeyEvent.KEYCODE_DPAD_CENTER != keyCode
            /*
             * && KeyEvent.KEYCODE_VOLUME_DOWN != keyCode &&
             * KeyEvent.KEYCODE_VOLUME_UP != keyCode
             */)
                return super.onKeyDown(keyCode, event);
        }
        if (KeyEvent.KEYCODE_ENTER == keyCode
                || KeyEvent.KEYCODE_DPAD_CENTER == keyCode
                || KeyEvent.KEYCODE_BACK == keyCode
                || KeyEvent.KEYCODE_DPAD_LEFT == keyCode
                || KeyEvent.KEYCODE_DPAD_RIGHT == keyCode
                || KeyEvent.KEYCODE_DPAD_DOWN == keyCode
                || KeyEvent.KEYCODE_DPAD_UP == keyCode
                || KeyEvent.KEYCODE_MENU == keyCode
                || KeyEvent.KEYCODE_MEDIA_STOP == keyCode
        /*
         * || KeyEvent.KEYCODE_VOLUME_DOWN == keyCode ||
         * KeyEvent.KEYCODE_VOLUME_UP == keyCode
         */) {
            return true;
        } else {
            if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    public boolean isPrepared() {
        if (videoPlayerHolder.getViewId() == 1) {
            return isOnePrepared;
        } else {
            return isTwoPrepared;
        }
    }

    /**
     * Response OK button
     */
    private void registerListeners() {
        if (isControllerShow) {
            Log.i(TAG,"Click VideoPlayerViewHolder.state:"+VideoPlayerViewHolder.state);
            switch (VideoPlayerViewHolder.state) {
                case VideoPlayerViewHolder.OPTION_STATE_PRE:
                    if (isPrepared()) {
                        videoPlayerHolder.bt_videoPre
                                .setBackgroundResource(R.drawable.player_icon_previous_focus);
                        cancleDelayHide();
                        moveToNextOrPrevious(-1, videoPlayerHolder.getViewId());
                        hideControlDelay();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_REWIND:
                    if (isPrepared()) {
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex < 0) {
                            slowForward();
                        } else {
                            showISOTip();
                        }
                        hideControlDelay();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_PLAY:
                    if (isPrepared()) {
                        cancleDelayHide();
                        if (videoPlayerHolder.getPlayerView().isPlaying()) {
                            playSpeed = videoPlayerHolder.getPlayerView().getPlayMode();
                            if (playSpeed != 1) {
                                // localPause(true);
                                // localResume(true);
                                localResumeFromSpeed(true);
                            } else {
                                localPause(true);
                            }
                        } else {
                            localResume(true);
                        }
                        hideControlDelay();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_WIND:
                    if (isPrepared()) {
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex < 0) {
                            fastForward();
                        } else {
                            showISOTip();
                        }
                        hideControlDelay();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_NEXT:
                    if (isPrepared()) {
                        cancleDelayHide();
                        moveToNextOrPrevious(1, videoPlayerHolder.getViewId());
                        hideControlDelay();
                        videoPlayerHolder.bt_videoNext
                                .setBackgroundResource(R.drawable.player_icon_next_focus);
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_TIME:
                    if (isPrepared()) {
                        cancleDelayHide();
                        showVideoTimeSetDialog();
                        hideControlDelay();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_LIST:
                    cancleDelayHide();
                    showVideoListDialog();
                    hideControlDelay();
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_INFO:
                    if (isPrepared()) {
                        cancleDelayHide();
                        showVideoInfoDialog();
                        hideControlDelay();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_SETTING:
                    if (isPrepared()) {
                        // Setup Menu
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex < 0) {
                            showSettingDialog();
                        } else {
                            showISOTip();
                        }
                        hideControlDelay();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_PLAYAB:
                    if (isPrepared()) {
                        // AB repeat play menu
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex < 0) {
                            showPlayABDialog();
                        } else {
                            showISOTip();
                        }
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_VOICE:
                    if (isPrepared()) {
                        if (videoPlayerHolder.isVoiceLayoutShow) {
                            videoPlayerHolder.setVoiceLayoutVisibility(false);
                            hideControlDelay();
                            break;
                        }
                        cancleDelayHide();
                        videoPlayerHolder.updateVoiceBar();
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH:
                    cancleDelayHide();
                    // switchDualMode();
                    videoDualModeController.switchDualMode();
                    videoPlayerHolder.setVideoDualSwitchSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH;
                    hideControlDelay();
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_DUAL_FOCUS:
                    if (videoPlayerHolder.getDualVideoMode()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getViewId() == 1 ?  videoPlayerHolder.getPlayerView(2).isPlaying() : videoPlayerHolder.getPlayerView(1).isPlaying());
                        cancleDelayHide();
                        // switchDualFocus();
                        videoDualModeController.switchDualFocus();
                        hideControlDelay();
                        String time = Tools.formatDuration(videoPlayerHolder.getPlayerView().getCurrentPosition());
                        videoPlayerHolder.current_time_video.setText(time);
                    }
                    break;
                case VideoPlayerViewHolder.OPTION_STATE_DUAL_MODE:
                    if (videoPlayerHolder.getDualVideoMode()) {
                        videoPlayerHolder.changeDualMode();
                    }
                    break;
            }
        } else {
            showController();
            hideControlDelay();
        }
    }

    /**
     * Response the mouse to click the button
     */
    class ControlButtonListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isControllerShow)
                return;
            switch (v.getId()) {
                case R.id.video_previous:
                    if (isPrepared()) {
                        videoHandler.removeMessages(Constants.SEEK_TIME);
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        moveToNextOrPrevious(-1, videoPlayerHolder.getViewId());
                        hideControlDelay();
                        videoPlayerHolder.setVideoPreSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PRE;
                        videoPlayerHolder.bt_videoPre
                                .setBackgroundResource(R.drawable.player_icon_previous_focus);
                    }
                    break;
                case R.id.video_rewind:
                    if (isPrepared()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex < 0) {
                            slowForward();
                        } else {
                            showISOTip();
                        }
                        hideControlDelay();
                        videoPlayerHolder.setVideoRewindSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_REWIND;
                    }
                    break;
                case R.id.video_play:
                    if (isPrepared()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView().isPlaying());
                        cancleDelayHide();
                        if (videoPlayerHolder.getPlayerView().isPlaying()) {
                            playSpeed = videoPlayerHolder.getPlayerView().getPlayMode();
                            if (playSpeed != 1) {
                                // localPause(true);
                                // localResume(true);
                                localResumeFromSpeed(true);
                            } else {
                                localPause(true);
                            }
                        } else {
                            localResume(true);
                        }
                        hideControlDelay();
                        videoPlayerHolder.setVideoPlaySelect(true, videoPlayerHolder
                                .getPlayerView().isPlaying());
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
                    }
                    break;
                case R.id.video_wind:
                    if (isPrepared()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex<0){
                            fastForward();
                        } else {
                            showISOTip();
                        }
                        hideControlDelay();
                        videoPlayerHolder.setVideoWindSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_WIND;
                    }
                    break;
                case R.id.video_next:
                    if (isPrepared()) {
                        videoHandler.removeMessages(Constants.SEEK_TIME);
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        moveToNextOrPrevious(1, videoPlayerHolder.getViewId());
                        hideControlDelay();
                        videoPlayerHolder.setVideoNextSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_NEXT;
                        videoPlayerHolder.bt_videoNext
                                .setBackgroundResource(R.drawable.player_icon_next_focus);
                    }
                    break;
                case R.id.video_time:
                    if (isPrepared()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_TIME;
                        cancleDelayHide();
                        showVideoTimeSetDialog();
                        hideControlDelay();
                    }
                    break;
                case R.id.video_list:
                    videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView().isPlaying());
                    cancleDelayHide();
                    showVideoListDialog();
                    hideControlDelay();
                    videoPlayerHolder.setVideoListSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_LIST;
                    break;
                case R.id.video_info:
                    if (isPrepared()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        showVideoInfoDialog();
                        hideControlDelay();
                        videoPlayerHolder.setVideoInforSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_INFO;
                    }
                    break;
                case R.id.video_setting:
                    if (isPrepared()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex < 0) {
                            showSettingDialog();
                        } else {
                            showISOTip();
                        }
                        hideControlDelay();
                        videoPlayerHolder.setVideoSettingSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_SETTING;
                    }
                    break;
                case R.id.play_icon_ab:
                    if (isPrepared()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        if (nBlurayISOTitleIndex < 0) {
                            showPlayABDialog();
                        } else {
                            showISOTip();
                        }
                        // hideControlDelay();
                        videoPlayerHolder.setVideoPlayABSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAYAB;
                    }
                    break;
                case R.id.play_icon_voice:
                    if (isPrepared()) {
                        if (videoPlayerHolder.isVoiceLayoutShow) {
                            videoPlayerHolder.setVoiceLayoutVisibility(false);
                            hideControlDelay();
                            break;
                        }
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                .isPlaying());
                        cancleDelayHide();
                        videoPlayerHolder.updateVoiceBar();
                        // hideControlDelay();
                        videoPlayerHolder.setVideoPlayVoiceSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_VOICE;
                    }
                    break;
                case R.id.play_icon_dual_switch:
                    if (!videoPlayerHolder.getDualVideoMode() && !isOnePrepared) {
                        break;
                    }
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH;
                    videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView().isPlaying());
                    cancleDelayHide();

                    // switchDualMode();
                    videoDualModeController.switchDualMode();
                    hideControlDelay();
                    videoPlayerHolder.setVideoDualSwitchSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH;
                    break;
                case R.id.play_icon_dual_focus_switch:
                    if (videoPlayerHolder.getDualVideoMode()) {
                        videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getViewId() == 1 ?  videoPlayerHolder.getPlayerView(2).isPlaying() : videoPlayerHolder.getPlayerView(1).isPlaying());
                        cancleDelayHide();
                        // switchDualFocus();
                        videoDualModeController.switchDualFocus();
                        hideControlDelay();
                        videoPlayerHolder.setVideoDualFocusSwitchSelect(true);
                        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_DUAL_FOCUS;
                        String time = Tools.formatDuration(videoPlayerHolder.getPlayerView().getCurrentPosition());
                        videoPlayerHolder.current_time_video.setText(time);
                    }
                    break;
                case R.id.play_icon_dual_mode_switch:
                    if (videoPlayerHolder.getDualVideoMode()) {
                        if (videoPlayerHolder.isDualLayoutShow) {
                            videoPlayerHolder.setDualModeLayoutVisibility(false);
                        } else {
                            videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView()
                                    .isPlaying());
                            cancleDelayHide();
                            videoPlayerHolder.changeDualMode();
                            // videoPlayerHolder.setVideoDualModeSelect(true);
                            VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_DUAL_MODE;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *
     */
    public void hideControlDelay() {
        videoHandler.removeMessages(Constants.HIDE_PLAYER_CONTROL);
        videoHandler.sendEmptyMessageDelayed(Constants.HIDE_PLAYER_CONTROL, DEFAULT_TIMEOUT);
    }

    /**
     *
     */
    public void cancleDelayHide() {
        videoHandler.removeMessages(Constants.HIDE_PLAYER_CONTROL);
    }

    /**
     *
     */
    private void hideController() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        if (videoPlayerHolder.playControlLayout != null) {
            videoPlayerHolder.playControlLayout.setVisibility(View.INVISIBLE);
            isControllerShow = false;
            videoPlayerHolder.showVideoFocus(isControllerShow);
        } else
            System.err.println("playControlLayout is null ptr!!");
    }

    /**
     * Display video information dialog menu
     */
    private void showVideoInfoDialog() {
        String time = videoPlayerHolder.total_time_video.getText().toString();
        String acType = videoPlayerHolder.getPlayerView().getAudioCodecType();
        VideoCodecInfo vcInfo = videoPlayerHolder.getPlayerView().getVideoInfo();
        if (vcInfo != null) {
            String vcType = vcInfo.getCodecType();
            mDetailInfoDialog = new DetailInfoDialog(this, R.style.dialog,
                    video_position[videoPlayerHolder.getViewId() - 1], time, acType, vcType);
            mDetailInfoDialog.show();
        }
    }

    /**
     * Dismiss video information dialog menu
     */
    private void dismissVideoInfoDialog() {
        if (mDetailInfoDialog != null) {
            mDetailInfoDialog.dismiss();
            mDetailInfoDialog = null;
        }
    }

    /**
     *
     */
    private void acquireWakeLock() {
        if (wakelock == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass()
                    .getCanonicalName());
            wakelock.acquire();
        }
    }

    /**
     * display the play time Settings dialog box
     */
    private void showVideoTimeSetDialog() {
        videoPlayerHolder.setVideoTimeSelect(true);
        // variation in timing Dialog
        if (mChooseTimePlayDialog == null) {
            mChooseTimePlayDialog = new ChooseTimePlayDialog(VideoPlayerActivity.this,
                    R.style.choose_time_dialog);
            mChooseTimePlayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window mWindow = mChooseTimePlayDialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
           // lp.x = 150;
            lp.y = 230;
            Log.i(TAG, "showVideoTimeSetDialog lp.x:" + lp.x + " lp.y:" + lp.y);
            mChooseTimePlayDialog.getWindow().setAttributes(lp);
            mChooseTimePlayDialog.setVariable(
                    !videoPlayerHolder.isSeekable(videoPlayerHolder.getViewId()), videoHandler);
        }
        mChooseTimePlayDialog.show();
        duration = (int) videoPlayerHolder.getPlayerView().getDuration();
        Log.i(TAG, "getDuration()" + duration);
        String tTime = Tools.formatDuration(duration);
        mChooseTimePlayDialog.getVideoTimeDurationTextView().setText(tTime);
        String cTime = Tools.formatDuration(currentPlayerPosition);
        mChooseTimePlayDialog.getVideoTimeCurrentPositionTextView().setText(cTime);
        if (tTime.equals(getResources().getString(R.string.default_time))) {
            mChooseTimePlayDialog.setVariable(true);
        }
    }

    /**
     * Dismiss the play time Settings dialog box
     */
    private void dismissVideoTimeSetDialog() {
        if (mChooseTimePlayDialog != null) {
            mChooseTimePlayDialog.dismiss();
            mChooseTimePlayDialog = null;
        }
    }

    /**
     * Display video playlist dialog box
     */
    public void showVideoListDialog() {
        if (mVideoListDialog !=null && mVideoListDialog.isShowing()) {
            dismissVideoListDialog();
            return;
        }
        currentViewPosition = video_position[videoPlayerHolder.getViewId() - 1];
        mVideoListDialog = new VideoListDialog(this);
        mVideoListDialog.show();
        mVideoListDialog.setHandler(toBePlayedHandler);
        mVideoListDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                if (VideoPlayerViewHolder.state == VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH) {
                    // switchDualFocus();
                    videoDualModeController.switchDualFocus();
                }
                mVideoListDialog = null;
            }
        });
    }

    /**
     * Dismiss video playlist dialog box
     */
    private void dismissVideoListDialog() {
        if (mVideoListDialog != null) {
            mVideoListDialog.dismiss();
            mVideoListDialog = null;
        }
    }

    @Override
    public int getCurrentViewPosition() {
        return currentViewPosition;
    }


    private void doMoveToNextOrPrevios(int alpha, int viewId){
		
		im_play_pause.setVisibility(View.INVISIBLE);
		im_play_kuaijin.setVisibility(View.INVISIBLE);
		im_play_kuaitui.setVisibility(View.INVISIBLE);
		im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);

        if (Tools.isThumbnailModeOn()) {
            getThumbnailController().releaseThumbnailView(true);
            Tools.setThumbnailMode("0");
        }

        if(videoPlayerHolder.getDualVideoMode() && Tools.isStreamlessModeOn()) {
            Log.i(TAG, "moveToNextOrPrevious: Seamless play in dual mode is unsupported!");
            showToastTip(getResources().getString(R.string.video_no_support_seamless_dualscreen));
        }
        if (viewId == 1) {
            isOnePrepared = false;
        } else {
            isTwoPrepared = false;
        }
        if (isVideoNone) {
            videoPlayerHolder.showDefaultPhoto(false);
            isVideoNone = false;
        }
         if (nBlurayISOTitleIndex>-1 && nBlurayISOTitleNumber > (nBlurayISOTitleIndex + alpha) && (nBlurayISOTitleIndex + alpha) > -1) {
            nBlurayISOTitleIndex += alpha;
            Tools.CURPOS= 0;
            Log.v(TAG, "Play title " + nBlurayISOTitleIndex + " alpha " + alpha);
            InitVideoPlayer(mVideoPlayList.get(video_position[viewId - 1]).getPath(), viewId);
            return;
        }

        if (mVideoPlayList.size() == 0) {
            return;
        }

        nBlurayISOTitleIndex = -1;
        if (videoPlayerHolder.mVideoPlayViewOne.getDuration()
                - videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition() > 2000) {
            BreakPointRecord.addData(mVideoPlayList.get(video_position[viewId - 1]).getPath(),
                    videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition(),
                    mVideoPlayList.get(video_position[viewId - 1]).getSize(), this);
        } else {
            BreakPointRecord.addData(mVideoPlayList.get(video_position[viewId - 1]).getPath(), 0,
                    mVideoPlayList.get(video_position[viewId - 1]).getSize(), this);
        }
        // videoPlayerHolder.getPlayerView(viewId).stopPlayback();
        video_position[viewId - 1] = video_position[viewId - 1] + alpha;
        Log.i(TAG, "moveToNextOrPrevious()  video_position: " + video_position + " " + viewId + " "
                + videoPlayerHolder.getViewId());
        //
        if (video_position[viewId - 1] <= -1) {
            video_position[viewId - 1] = mVideoPlayList.size() - 1;
        } else if (video_position[viewId - 1] >= mVideoPlayList.size()) {
            video_position[viewId - 1] = 0;
        }
        mCurrentVideoPlayerIndex = video_position[viewId - 1];
        if (viewId == videoPlayerHolder.getViewId()) {
            videoPlayerHolder.reset();
            String video_name = mVideoPlayList.get(video_position[viewId - 1]).getName();
            videoPlayerHolder.setVideoName(video_name);
            videoPlayerHolder
                    .setVideoListText(video_position[viewId - 1] + 1, mVideoPlayList.size());
        }
        cancleDelayHide();
        InitVideoPlayer(mVideoPlayList.get(video_position[viewId - 1]).getPath(), viewId);
        /*if (videoPlayerHolder.getDualVideoMode()) {
            int vid = videoPlayerHolder.getViewId();
            if (viewId == vid) {
                videoPlayerHolder.setAudioProcessor(vid);
            }
        }*/
    }

    /**
     * @param alpha
     */
    private void moveToNextOrPrevious(int alpha, int viewId) {
        String opt = Tools.getPlaySettingOpt(6, viewId);
        if (getString(R.string.play_setting_0_value_1).equals(opt)) {
            //close golden left eye
            Intent intent = new Intent("com.jrm.localmm.ui.common.GoldenLeftEyeActivity.exit");
            sendBroadcast(intent);
        }

        if ("monaco".equalsIgnoreCase(Tools.getHardwareName()) && !Tools.unSupportTVApi()) {
            final int id = viewId ;
            final int al = alpha ;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "current 3D formate is :"+mThreeDimentionControl.getCurrent3DFormate() );
                    if(mThreeDimentionControl.getCurrent3DFormate() != Enum3dType.EN_3D_NONE){
                        Log.d(TAG, "enable3D with EN_3d_NONE before play next video!!!") ;
                        long startTime = System.currentTimeMillis() ;
                        mThreeDimentionControl.set3DFormate(Enum3dType.EN_3D_NONE) ;
                        long endTime = System.currentTimeMillis() ;
                        Log.d(TAG, "close 3D cost time: "+(endTime - startTime) +" ms !") ;
                    }
                    videoHandler.obtainMessage(DO_NEXT_OR_PRE_VIDEO, al, id).sendToTarget();
                }
            }).start();
        }else{
            doMoveToNextOrPrevios(alpha, viewId);
        }
    }

    private void initTimeVar(int viewId) {
        startPreTime[viewId - 1] = 0;
        endPreTime[viewId - 1] = 0;
        startRenderingTime[viewId - 1] = 0;
    }

    private void InitVideoPlayer(String videoPlayPath, int viewId) {
        if (mNetVideoUri != null) {
            videoPlayerHolder.getPlayerView(viewId).setNetVideoUri(mNetVideoUri, mHeaders);
        }
        isResourceLost = false;
        if (viewId == 1) {
            isOnePrepared = false;
        } else {
            isTwoPrepared = false;
        }
        videoPlayerHolder.setSeekVar(viewId, true);
        initTimeVar(viewId);
        // Setting error of the monitor
        if (videoPlayerHolder.getPlayerView(viewId) != null) {
            if (!Tools.isStreamlessModeOn()) {
                videoPlayerHolder.getPlayerView(viewId).stopPlayback();
            }

            videoPlayerHolder.getPlayerView(viewId).setPlayerCallbackListener(myPlayerCallback);
        }

        if (nBlurayISOTitleIndex == -1 && mountedIsoPath != null) {
            Bluray.deinit();
            stm.unmountISO(mountedIsoPath.toString(), true);
            mountedIsoPath = null;
        }
        Log.i(TAG, "*******videoPlayPath*****" + videoPlayPath + "SDK_INT: " + Build.VERSION.SDK_INT);
        videoPlayPath = Tools.fixPath(videoPlayPath);

        if (isNeedBreakPointPlay) {
            breakPoint = BreakPointRecord.getData(mVideoPlayList.get(video_position[viewId - 1])
                    .getPath(), mVideoPlayList.get(video_position[viewId - 1]).getSize(),
                    VideoPlayerActivity.this);
        } else {
            breakPoint = 0;
            isBreakPointPlay = false;
        }
        Log.i(TAG, "********breakPoint********" + breakPoint);
        if (viewId == 1) {
            mCurrentVideoPlayPath = videoPlayPath;
        }
        if (isISOFile(videoPlayPath)) {
            playISOFile(videoPlayPath, viewId);
        } else {
            if (breakPoint > 0) {
                breakPointPlay(viewId, breakPoint, videoPlayPath);
            } else {
                if (videoPlayPath != null) {
                    videoPlayerHolder.getPlayerView(viewId).setVideoPath(videoPlayPath, viewId);
                    // startPreTime[viewId - 1] =
                    // videoPlayerHolder.getPlayerView(viewId).getStartTime();
                    // System.currentTimeMillis();
                }
            }
        }
        // Note:Just passing handler to videoplayview,which has not any side effect.
        //if (Tools.isThumbnailModeOn()) {
            videoPlayerHolder.getPlayerView(viewId).setHandler(videoHandler);
        //}

        /*
         * displayFormat = s3dSkin.getDisplayFormat(); set3DMode(true);
         */
        PlaySettingSubtitleDialog.subtitlePosition = 0;
        if (viewId == 1) {
            isAudioSupportOne = true;
            isVideoSupportOne = true;
            if (videoPlaySettingDialogOne != null) {
                videoPlaySettingDialogOne.dismiss();
                videoPlaySettingDialogOne = null;
                SubtitleManager.setSubtitleSettingOpt(3, getResources()
                        .getString(R.string.subtitle_3_value_1), viewId);
            }
            if (playSettingSubtitleDialogOne != null) {
                playSettingSubtitleDialogOne.dismiss();
            }
        } else {
            isAudioSupportTwo = true;
            isVideoSupportTwo = true;
            if (videoPlaySettingDialogTwo != null) {
                videoPlaySettingDialogTwo.dismiss();
                videoPlaySettingDialogTwo = null;
                SubtitleManager.setSubtitleSettingOpt(3, getResources()
                        .getString(R.string.subtitle_3_value_1), viewId);
            }
            if (playSettingSubtitleDialogTwo != null) {
                playSettingSubtitleDialogTwo.dismiss();
            }
        }

        if (mDetailInfoDialog != null) {
            mDetailInfoDialog.dismiss();
        }
        if (mChooseTimePlayDialog != null) {
            mChooseTimePlayDialog.clearChooseList();
            mChooseTimePlayDialog.dismiss();
        }
        if (videoPlayAbDialog[viewId - 1] != null) {
            videoPlayAbDialog[viewId - 1].mHandler.sendEmptyMessage(1);
            videoPlayAbDialog[viewId - 1] = null;
        }
        if (mVideoListDialog != null) {
            mVideoListDialog.setSelection(video_position[videoPlayerHolder.getViewId() - 1]);
        }
        dismissVideoStartTimeDialog();
    }

    /**
     * Display buffer progress
     *
     * @param id
     */
    private void showProgressDialog(int id) {
        if (!isFinishing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(id));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }

    /**
     *
     */
    public void dismissProgressDialog() {
    	
    	int subtitleItemId = Settings.System.getInt(getContentResolver(),
				"subtitle_item", SUBTITLE_INNER);
		if(subtitleItemId==SUBTITLE_INNER)
		{
			SubtitleTrackInfo subtitleAllTrackInfo = null ;
			if (getVideoPlayHolder().getPlayerView().isInPlaybackState()) {
				subtitleAllTrackInfo = SubtitleManager.getInstance().getAllSubtitleTrackInfo(videoPlayerHolder.getPlayerView().getMMediaPlayer());
			}
			if (subtitleAllTrackInfo != null) {
	            // Display the film contains all the subtitles number
	            SubtitleManager.mVideoSubtitleNo = subtitleAllTrackInfo.getAllInternalSubtitleCount();
	            if (SubtitleManager.mVideoSubtitleNo > 0) {
	                SubtitleManager.getInstance().onSubtitleTrack(videoPlayerHolder.getPlayerView().getMMediaPlayer());
	                SubtitleManager.getInstance().setSubtitleTrack(videoPlayerHolder.getPlayerView().getMMediaPlayer(), 0);
	                SubtitleManager.setSubtitleSettingOpt(2, getResources().getString(R.string.subtitle_2_value_2) + "1", 1);
	                Settings.System.putInt(getContentResolver(),"subtitle_item", SUBTITLE_INNER);
	            }
	        }
		}
		else{
			SubtitleManager.getInstance().offSubtitleTrack(videoPlayerHolder.getPlayerView().getMMediaPlayer());
		}
    	
        if (progressDialog != null && progressDialog.isShowing()) {
            Log.d(TAG, "dismissProgressDialog");
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * FF 10s
     */
    private void rewind() {
        cancleDelayHide();
        int currentTime = videoPlayerHolder.videoSeekBar.getProgress();
        if (currentTime >= 0) {
            currentTime = currentTime - 10000;
            videoPlayerHolder.getPlayerView().seekTo(currentTime);
        }
        hideControlDelay();
    }

    /**
     * FB 10s
     */
    private void wind() {
        cancleDelayHide();
        int currentTime = videoPlayerHolder.videoSeekBar.getProgress();
        if (currentTime <= videoPlayerHolder.getPlayerView().getDuration()) {
            currentTime = currentTime + 10000;
            videoPlayerHolder.getPlayerView().seekTo(currentTime);
        }
        hideControlDelay();
    }

    /**
     * FF
     */
    private void fastForward() {
        if (Tools.isThumbnailModeOn()) {
            getThumbnailController().thumbnailFastForward();
            getVideoPlayHolder().setPlaySpeed(getThumbnailController().getThumbnailPlaySpeed());
            return;
        }

        if (!isPlaying) {
            videoPlayerHolder.getPlayerView().setVoice(false);
            localResume(false);
        }
        playSpeed = videoPlayerHolder.getPlayerView().getPlayMode();
        Log.d(TAG, "get play mode ---" + playSpeed);
        int currentSpeed = 1 * 2;
        if (playSpeed < 64 && playSpeed > 0) {
            currentSpeed = playSpeed * 2;
        }
        if(playSpeed == 32){
        	playSpeed = 1;
        	currentSpeed = 1;
        }
        playSpeed = currentSpeed;
        videoPlayerHolder.getPlayerView().setPlayMode(currentSpeed);
        // Set the current approaching speed display string
        videoPlayerHolder.setPlaySpeed(currentSpeed);
		if(currentSpeed>1)
		{
	        im_play_kuaijin.setVisibility(View.VISIBLE);
	        im_video_player_FF_FR_val.setVisibility(View.VISIBLE);
		}
		else
		{
			im_play_kuaijin.setVisibility(View.INVISIBLE);
	        im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
		}
        im_video_player_FF_FR_val.setText("X"+currentSpeed);
        im_play_kuaitui.setVisibility(View.INVISIBLE);
        im_play_pause.setVisibility(View.INVISIBLE);
        //end
    }

    /**
     * FB
     */
    private void slowForward() {
        if (Tools.isThumbnailModeOn()) {
            getThumbnailController().thumbnailSlowForward();
            getVideoPlayHolder().setPlaySpeed(getThumbnailController().getThumbnailPlaySpeed());
            return;
        }

        if (!isPlaying) {
            videoPlayerHolder.getPlayerView().setVoice(false);
            localResume(false);
        }
        playSpeed = videoPlayerHolder.getPlayerView().getPlayMode();
        int currentSpeed = 1 * (-2);
        if (playSpeed < 64 && playSpeed < 0) {
            currentSpeed = playSpeed * 2;
        }
        if(playSpeed == -32){
        	playSpeed = 1;
        	currentSpeed = 1;
        }
        playSpeed = currentSpeed;
        videoPlayerHolder.getPlayerView().setPlayMode(currentSpeed);
        videoPlayerHolder.setPlaySpeed(currentSpeed);
		if(currentSpeed<-1)
		{
	        im_play_kuaitui.setVisibility(View.VISIBLE);
	        im_video_player_FF_FR_val.setVisibility(View.VISIBLE);
		}
		else
		{
	        im_play_kuaitui.setVisibility(View.INVISIBLE);
	        im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
		}
		currentSpeedtop=currentSpeed*(-1);
        im_video_player_FF_FR_val.setText("X"+currentSpeedtop);
        im_play_kuaijin.setVisibility(View.INVISIBLE);
        im_play_pause.setVisibility(View.INVISIBLE);
    }

    /**
     * Local broadcast suspended
     */
    protected void localPause(boolean bSelect) {
        // cancleDelayHide();
        videoPlayerHolder.getPlayerView().setVoice(false);
        videoPlayerHolder.getPlayerView().setPlayMode(1);
        videoPlayerHolder.getPlayerView().pause();
        videoPlayerHolder.getPlayerView().setVoice(true);
        isPlaying = false;
        videoPlayerHolder.setVideoPlaySelect(bSelect, isPlaying);
        // hideControlDelay();
        im_play_pause.setVisibility(View.VISIBLE);
        im_play_kuaijin.setVisibility(View.INVISIBLE);
        im_play_kuaitui.setVisibility(View.INVISIBLE);
        im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
    }

    /**
     * Local broadcast suspended
     */
    protected void localPauseFromDualSwitch(int viewId, boolean bSelect) {
        if (null != videoPlayerHolder.getPlayerView(viewId)) {
            videoPlayerHolder.getPlayerView(viewId).setVoice(false);
            videoPlayerHolder.getPlayerView(viewId).setPlayMode(1);
            videoPlayerHolder.getPlayerView(viewId).pause();
            videoPlayerHolder.getPlayerView(viewId).setVoice(true);
        }
    }

    /**
     * Local broadcast recovery
     */
    protected void localResume(boolean bSelect) {
        // cancleDelayHide();
        // videoPlayerHolder.getPlayerView().setPlayMode(1);
        videoPlayerHolder.getPlayerView().start();
        isPlaying = true;
        playSpeed = 1;
        videoPlayerHolder.setPlaySpeed(1);
        videoPlayerHolder.setVideoPlaySelect(bSelect, isPlaying);
        videoHandler.removeMessages(SEEK_POS);
        videoHandler.sendEmptyMessageDelayed(SEEK_POS, 1000);
        im_play_pause.setVisibility(View.INVISIBLE);
        im_play_kuaijin.setVisibility(View.INVISIBLE);
        im_play_kuaitui.setVisibility(View.INVISIBLE);
        im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
        // hideControlDelay();
    }

    /**
     * Local broadcast recovery
     */
    protected void localResumeFromDualSwitch(boolean bSelect) {
        // Tools.setVideoMute(false, 0);
        videoPlayerHolder.getPlayerView(1).start();
        videoHandler.removeMessages(SEEK_POS);
        videoHandler.sendEmptyMessageDelayed(SEEK_POS, 1000);
    }

    /**
     * Local broadcast recovery
     */
    protected void localResumeFromSpeed(boolean bSelect) {
        // cancleDelayHide();
        videoPlayerHolder.getPlayerView().setVoice(true);
        videoPlayerHolder.getPlayerView().setPlayMode(1);
        isPlaying = true;
        playSpeed = 1;
        videoPlayerHolder.setPlaySpeed(1);
        videoPlayerHolder.setVideoPlaySelect(bSelect, isPlaying);
        videoHandler.removeMessages(SEEK_POS);
        videoHandler.sendEmptyMessage(SEEK_POS);
        im_play_pause.setVisibility(View.INVISIBLE);
        im_play_kuaijin.setVisibility(View.INVISIBLE);
        im_play_kuaitui.setVisibility(View.INVISIBLE);
        im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
        // hideControlDelay();
    }

    /**
     * Show Controller
     */
    public void showController() {
        if (videoPlayerHolder.playControlLayout != null) {
            videoPlayerHolder.playControlLayout.setVisibility(View.VISIBLE);
            isControllerShow = true;
            videoPlayerHolder.showVideoFocus(isControllerShow);
        } else {
            Log.i(TAG, "playControlLayout is null");
        }
    }

    /**
     * Show Setting Dialog
     */
    private void showSettingDialog() {
        int id = videoPlayerHolder.getViewId();
        if (!Constants.bUseAndroidStandardMediaPlayerTrackAPI) {
            Log.d(TAG, " audioTrack : " + AudioTrackManager.getInstance().getAudioTrackInfo(videoPlayerHolder.getPlayerView().getMMediaPlayer(), false));
        }
        videoPlayerHolder.setVideoSettingSelect(true);
        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_SETTING;
        // init audio tack
        AudioTrackManager.initAudioTackSettingOpt(VideoPlayerActivity.this, id);
        int audioTrackId = AudioTrackManager.getInstance().getCurrentAudioTrackId(
                videoPlayerHolder.getPlayerView().getMMediaPlayer()
                );
        AudioTrackManager.setAudioTackSettingOpt(this, 0, getString(R.string.audio_track_setting_0_value_1)
                + (audioTrackId + 1), id);
        if (id == 1) {
            // init setting dialog
            if (videoPlaySettingDialogOne == null) {
                videoPlaySettingDialogOne = new VideoPlaySettingDialog(VideoPlayerActivity.this,
                        R.style.dialog, mVideoPlayList.get(video_position[id - 1]).getPath(),
                        videoPlayerHolder.getPlayerView().getMMediaPlayer());
                videoPlaySettingDialogOne.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            // show setting dialog
            videoPlaySettingDialogOne.show();
        } else {
            // init setting dialog
            if (videoPlaySettingDialogTwo == null) {
                videoPlaySettingDialogTwo = new VideoPlaySettingDialog(VideoPlayerActivity.this,
                        R.style.dialog, mVideoPlayList.get(video_position[id - 1]).getPath(),
                        videoPlayerHolder.getPlayerView().getMMediaPlayer());
                videoPlaySettingDialogTwo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            // show setting dialog
            videoPlaySettingDialogTwo.show();
        }
    }

    /**
     * Dismiss Setting Dialog
     */
    private void dismissSettingDialog() {
        if (videoPlaySettingDialogOne != null) {
            videoPlaySettingDialogOne.dismiss();
            videoPlaySettingDialogOne = null;
        }
        if (videoPlaySettingDialogTwo != null) {
            videoPlaySettingDialogTwo.dismiss();
            videoPlaySettingDialogTwo = null;
        }
    }

    /**
     * Show aired with AB Dialog
     */
    public void showPlayABDialog() {
        int id = videoPlayerHolder.getViewId();
        videoPlayerHolder.setVideoPlayABSelect(true);
        VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAYAB;
        if (!videoPlayerHolder.isSeekable(id)) {
            showToastTip(getResources().getString(R.string.video_media_infor_no_index_ab));
            return;
        }
        if (videoPlayAbDialog[id - 1] == null) {
            videoPlayAbDialog[id - 1] = new VideoPlayABDialog(VideoPlayerActivity.this,
                    R.style.dialog, videoPlayerHolder);
            videoPlayAbDialog[id - 1].requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        videoPlayAbDialog[id - 1].show();
    }

    /**
     * Dismiss aired with AB Dialog
     */
    private void dismissPlayAbDialog() {
        if (videoPlayAbDialog[0] != null) {
            videoPlayAbDialog[0].dismiss();
            videoPlayAbDialog[0] = null;
        }

        if (videoPlayAbDialog[1] != null) {
            videoPlayAbDialog[1].dismiss();
            videoPlayAbDialog[1] = null;
        }
    }

    /**
     * Dismiss aired with VideoStartTimeDialog
     */
    private void dismissVideoStartTimeDialog() {
        if (mVideoStartTimeDialog != null) {
            if (mVideoStartTimeDialog.isShowing()) {
                mVideoStartTimeDialog.dismiss();
                mVideoStartTimeDialog = null;
            }
        }
    }

    //catch a NullPointerException in getVideoInfo() .
    public boolean isCodecTypeHEVC() {
        try {
            VideoCodecInfo info = videoPlayerHolder.getPlayerView().getVideoInfo();
            return "HEVC".equalsIgnoreCase(info.getCodecType());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void checkABCycle(int viewId) {
        videoHandler.removeMessages(Constants.HANDLE_MESSAGE_CHECK_AB);
        int id = 0;
        if (viewId == 1) {
            id = 1;
        } else {
            id = 0;
        }
        if (videoPlayAbDialog[id] != null) {
            if (videoPlayAbDialog[id].bFlag && videoPlayAbDialog[id].sharedata != null) {
                videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_CHECK_AB, 1000);
            }
        }
    }

    // show Tip
    public void showToastTip(String strMessage) {
        Toast toast = ToastFactory.getToast(VideoPlayerActivity.this, strMessage, Gravity.CENTER);
        // toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        // ToastFactory.getToast(VideoPlayerActivity.this, strMessage,
        // Gravity.CENTER).show();
        if (!isVideoSupportOne && !isAudioSupportOne) {
            if (mVideoPlayList.size() <= 1) {
                videoHandler.removeMessages(SEEK_POS);
                if (videoPlayerHolder.mVideoPlayViewOne != null) {
                    videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
                    videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                }
                if (videoPlayerHolder.mVideoPlayViewTwo != null) {
                    videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
                    videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
                }
                // this.finish();
                videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
            } else {
//                moveToNextOrPrevious(1, 1);
            }
        }
        if (!isVideoSupportTwo && !isAudioSupportTwo) {
            if (mVideoPlayList.size() <= 1) {
                videoHandler.removeMessages(SEEK_POS);
                if (videoPlayerHolder.mVideoPlayViewOne != null) {
                    videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
                    videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                }
                if (videoPlayerHolder.mVideoPlayViewTwo != null) {
                    videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
                    videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
                }
                // this.finish();
                videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
            } else {
//                moveToNextOrPrevious(1, 2);
            }
        }
    }

    /**
     * show onError callback info.
     *
     * @param strMessage toast info
     * @param viewId from witch player
     */
    private void showErrorToast(String strMessage, int viewId) {
        if (isResourceLost) {
            if (viewId == 1) {
                if (videoPlayerHolder.mVideoPlayViewOne != null) {
                    videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
                    videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                    finish();
                }
            } else {
                if (videoPlayerHolder.mVideoPlayViewTwo != null) {
                    videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
                    videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH;
                    videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView().isPlaying());
                    cancleDelayHide();
                    // switchDualMode();
                    videoDualModeController.switchDualMode();
                    hideControlDelay();
                    videoPlayerHolder.setVideoDualSwitchSelect(true);
                    VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_DUAL_SWITCH;
                }
            }
            return;
        }
        String svd = getResources().getString(R.string.video_media_error_video_unsupport);
        if (strMessage.equals(getResources().getString(R.string.video_media_error_server_died)) ||
            strMessage.equals(svd)) {
            VideoPlayerActivity.this.finish();
        } else {
            if (Constants.bSupportDivx) {
                videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
                videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
            } else {
                if (mVideoPlayList.size() > 1) {
                    String sName = mVideoPlayList.get(video_position[viewId - 1]).getName();
                    String showtip = sName + " "+ strMessage + ",\n" + getResources().getString(R.string.video_media_play_next);
                    showErrorDialog(showtip,viewId);
                } else {
                    Toast toast = ToastFactory.getToast(VideoPlayerActivity.this, strMessage, Gravity.CENTER);
                    toast.show();
                    VideoPlayerActivity.this.finish();
                }
            }
        }
    }

    private void showErrorDialog(final String strMessage, final int viewId) {
        showErrorDialog(strMessage , viewId , false);
    }

    // Pop up display an error dialog box
    private void showErrorDialog(final String strMessage, final int viewId , final boolean isoSupport) {
        // Prevent activity died when the popup menu
        if (!isFinishing()) {
            new AlertDialog.Builder(VideoPlayerActivity.this)
                    .setTitle(getResources().getString(R.string.show_info))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(strMessage)
                    .setPositiveButton(getResources().getString(R.string.exit_ok),
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dismissProgressDialog();
                                    if (isoSupport || strMessage.equals(getResources().getString(
                                            R.string.video_media_error_server_died))) {
                                        VideoPlayerActivity.this.finish();
                                    } else {
                                        moveToNextOrPrevious(1, viewId);
                                    }
                                }
                            })
                    .setNegativeButton(getResources().getString(R.string.exit_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (viewId == 2) {
                                        // switchDualMode();
                                        videoDualModeController.switchDualMode();
                                    } else {
                                        if (videoPlayerHolder.getDualVideoMode()) {
                                            moveToNextOrPrevious(-1, viewId);
                                        } else {
                                            VideoPlayerActivity.this.finish();
                                        }
                                    }
                            }
                        }).setCancelable(false).show();
        }
    }

    // Unknown error handling
    private errorStruct processErrorUnknown(MediaPlayer mp, int what, int extra) {
        errorStruct retStruct = new errorStruct();
        int strID = R.string.video_media_error_unknown;
        if (Constants.bSupportDivx) {
            if(extra == -5007 || extra == -5008) {
                retStruct.showStateWithError = false;
                if(divxController != null) {
                    divxController.errorMsgProcess(extra);
                }
            }
        }
        switch (extra) {
            case MediaError.ERROR_MALFORMED:
                strID = R.string.video_media_error_malformed;
                break;
            case MediaError.ERROR_IO:
                strID = R.string.video_media_error_io;
                break;
            case MediaError.ERROR_UNSUPPORTED:
                strID = R.string.video_media_error_unsupported;
                break;
            case MediaError.ERROR_FILE_FORMAT_UNSUPPORT:
                strID = R.string.video_media_error_format_unsupport;
                break;
            case MediaError.ERROR_NOT_CONNECTED:
                strID = R.string.video_media_error_not_connected;
                break;
            case MediaError.ERROR_AUDIO_UNSUPPORT:
                strID = R.string.video_media_error_audio_unsupport;
                retStruct.showStateWithError = false;
                break;
            case MediaError.ERROR_VIDEO_UNSUPPORT:
                strID = R.string.video_media_error_video_unsupport;
                break;
            case MediaError.ERROR_DRM_NO_LICENSE:
                strID = R.string.video_media_error_no_license;
                break;
            case MediaError.ERROR_DRM_LICENSE_EXPIRED:
                strID = R.string.video_media_error_license_expired;
                break;
            case MMediaPlayer.MEDIA_ERROR_VIDEO_RESOURCE_LOST:
                isResourceLost = true;
                break;
            default:
                //usb storage off
                strID = R.string.video_media_other_error_unknown;
                break;
        }
        retStruct.strMessage = getResources().getString(strID);
        return retStruct;
    }

    public VideoPlayView.playerCallback myPlayerCallback = new VideoPlayView.playerCallback() {
        @Override
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err, int viewId) {
            if (mLocalMediaController != null) {
                mLocalMediaController.onError(framework_err, impl_err);
            }
            String strMessage = "";
            switch (framework_err) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    strMessage = getResources().getString(R.string.video_media_error_server_died);
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    if (nBlurayISOTitleNumber > (nBlurayISOTitleIndex + 1) && (nBlurayISOTitleIndex + 1) > 0) {
                        nBlurayISOTitleIndex += 1;
                        Log.v(TAG, "Video error, play next title " + nBlurayISOTitleIndex + " alpha " + 1);
                        InitVideoPlayer(mVideoPlayList.get(video_position[videoPlayerHolder.getViewId() - 1]).getPath(), videoPlayerHolder.getViewId());
                        return true;
                    } else {
                        errorStruct retStruct = processErrorUnknown(mp, framework_err, impl_err);
                        strMessage = retStruct.strMessage;
                        if (!retStruct.showStateWithError) {
                            return true;
                        }
                    }
                    break;
                case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    strMessage = getResources().getString(R.string.video_media_error_not_valid);
                    break;
                case IO_ERROR:
                    strMessage = getResources().getString(R.string.open_file_fail);
                    break;
                default:
                    strMessage = getResources().getString(R.string.video_media_other_error_unknown);
                    break;
            }
            Log.i(TAG,"player onError---start to stop playerback.");
            videoPlayerHolder.getPlayerView(viewId).stopPlayback();
            showErrorToast(strMessage, viewId);
            return true;
        }

        @Override
        public void onCompletion(MediaPlayer mp, int viewId) {
            Log.i(TAG, "----------- onCompletion ------------");
            if (mLocalMediaController != null) {
                mLocalMediaController.onCompletion();
            }

            if (Constants.bSupportDivx) {
                videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
                videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_PLAYER_EXIT, 300);
            } else {
                if (videoPlayAbDialog[viewId - 1] != null) {
                    if (videoPlayAbDialog[viewId - 1].bFlag
                            && videoPlayAbDialog[viewId - 1].sharedata != null) {
                        videoPlayerHolder.videoSeekBar
                                .setProgress(videoPlayAbDialog[viewId - 1].sharedata.getInt(
                                        VideoPlayABDialog.A_POSITION, 0));
                        videoPlayerHolder.getPlayerView().seekTo(
                                videoPlayAbDialog[viewId - 1].sharedata.getInt(
                                        VideoPlayABDialog.A_POSITION, 0));
                        videoPlayerHolder.getPlayerView().setPlayMode(1);
                        localResume(true);
                        return;
                    }
                }
                videoPlayerHolder.setSubtitleTextViewVisible(false);
                moveToNextOrPrevious(1, viewId);
                showController();
                hideControlDelay();
            }
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra, int viewId) {
            Log.i(TAG, "*******onInfo******" + what + " getVideoWidth:" + mp.getVideoWidth() + " getVideoHeight:" + mp.getVideoHeight());

            if (mLocalMediaController != null) {
                mLocalMediaController.onInfo(what, extra);
            }

            switch (what) {
                case MMediaPlayer.MEDIA_INFO_SUBTITLE_UPDATA:
                    if (extra == 1) {
                        String str = SubtitleManager.getInstance()
                                .getSubtitleData(videoPlayerHolder.getPlayerView(viewId).getMMediaPlayer());
                        Log.i(TAG, "*******setSubTitleText******" + str + " " + viewId);
                        if (str.length() >= 1) {
                            /* match the pattern ASS tags */
                            Pattern pattern = Pattern.compile("\\{\\\\[a-z]{1,}[0-9]\\}");
                            Matcher matcher = pattern.matcher(str);
                            String newStr = matcher.replaceAll("");
                            videoPlayerHolder.setSubTitleText(newStr, viewId);
                        }
                        return true;
                    }
                    if (extra == 0) {
                        // Hide Subtitles
                        videoPlayerHolder.setSubTitleText("", viewId);
                        return true;
                    }
                    break;
                case MMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    Log.i(TAG, "*******MEDIA_INFO_NOT_SEEKABLE******");
                    videoPlayerHolder.setSeekVar(viewId, false);
                    String strMessage = getResources().getString(
                            R.string.video_media_infor_no_index);
                    if (videoPlayAbDialog[viewId - 1] != null) {
                        if (videoPlayAbDialog[viewId - 1].aFlag) {
                            strMessage = getResources().getString(
                                    R.string.video_media_infor_no_index_ab);
                            videoPlayAbDialog[viewId - 1].mHandler.sendEmptyMessage(what);
                        }
                    }
                    playSpeed = 1;
                    videoPlayerHolder.setPlaySpeed(1);
                    showToastTip(strMessage);
                    im_play_pause.setVisibility(View.INVISIBLE);
                    im_play_kuaijin.setVisibility(View.INVISIBLE);
                    im_play_kuaitui.setVisibility(View.INVISIBLE);
                    im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
                    return true;
                case MMediaPlayer.MEDIA_INFO_AUDIO_UNSUPPORT:
                    Log.i(TAG, "MEDIA_INFO_AUDIO_UNSUPPORT");
                    if (viewId == 1) {
                        isAudioSupportOne = false;
                    } else {
                        isAudioSupportTwo = false;
                    }
                    showToastTip(getResources().getString(
                            R.string.video_media_error_audio_unsupport));
                    break;
                case MMediaPlayer.MEDIA_INFO_VIDEO_UNSUPPORT:
                    Log.i(TAG, "MEDIA_INFO_VIDEO_UNSUPPORT");
                    if (viewId == 1) {
                        isVideoSupportOne = false;
                        isOnePrepared = true;
                    } else {
                        isVideoSupportTwo = false;
                        isTwoPrepared = true;
                    }

                    videoHandler.sendEmptyMessageDelayed(SEEK_POS, 1000);

                    showToastTip(getResources().getString(
                            R.string.video_media_error_video_unsupport));
                    if (!videoPlayerHolder.getDualVideoMode()) {
                        videoPlayerHolder.showDefaultPhoto(true);
                        isVideoNone = true;
                    }
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Log.i(TAG, "*********MEDIA_INFO_RENDERING_START************");
                    startRenderingTime[viewId - 1] = System.currentTimeMillis();
                    int index = viewId - 1;
                    startPreTime[index] = videoPlayerHolder.getPlayerView(index + 1).getStartTime();
                    /*Log.i(TAG,
                            ">>>init video>>>>>>video name : "
                                    + mVideoPlayList.get(video_position[index]).getName());
                    Log.i(TAG, ">>>init video>>>>>>buffer time : "
                            + (endPreTime[index] - startPreTime[index]) + "ms");
                    Log.i(TAG, ">>>init video>>>>>>firstFrame time : "
                            + (startRenderingTime[index] - endPreTime[index]) + "ms");*/
                    // http://hcgit:8080/#/c/16581/
                    // Revert CL:c615f67f99338972875790f089530b79720d7a7e
                    // initPlayer(viewId);
                    break;
                case MMediaPlayer.MEDIA_INFO_TRICK_PLAY_COMPLETE:
                    // Trick play complete notify MEDIA_INFO_TRICK_PLAY_COMPLETE = 1006
                    playSpeed = 1;
                    videoPlayerHolder.setPlaySpeed(1);
                    im_play_pause.setVisibility(View.INVISIBLE);
                    im_play_kuaijin.setVisibility(View.INVISIBLE);
                    im_play_kuaitui.setVisibility(View.INVISIBLE);
                    im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
                    break;
                default:
                    Log.i(TAG, "Play onInfo::: default onInfo!");
                    break;
            }
            return false;
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        }

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height, int viewId) {
            Log.i(TAG, "onVideoSizeChanged width:" + width + " height:" + height + " viewId:" + viewId);
            if (width == 0 || height == 0) {
                if (viewId == 1) {
                    isVideoSupportOne = false;
                    isOnePrepared = true;
                } else {
                    isVideoSupportTwo = false;
                    isTwoPrepared = true;
                }

                videoHandler.sendEmptyMessageDelayed(SEEK_POS, 1000);

                showToastTip(getResources().getString(
                                                R.string.video_media_error_video_unsupport));
                if (!videoPlayerHolder.getDualVideoMode()) {
                    videoPlayerHolder.showDefaultPhoto(true);
                    isVideoNone = true;
                }
            }
            if (mLocalMediaController != null) {
                mLocalMediaController.onVideoSizeChanged(width, height);
            }
        }

        @Override
        public void onPrepared(MediaPlayer mp, int viewId) {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            Log.i(TAG, "onPrepared mVideoWidth:" + mVideoWidth + " mVideoHeight:" + mVideoHeight);
            endPreTime[viewId - 1] = System.currentTimeMillis();
            if (viewId == 1) {
                isOnePrepared = true;
            } else {
                isTwoPrepared = true;
            }

            if (mLocalMediaController != null) {
                mLocalMediaController.onPrepared();
            }

            // Set subtitles data
            videoPlayerHolder.setSubTitleText("", viewId);
            videoPlayerHolder.setSubtitleTextViewVisible(true);
            SurfaceHolder sh = videoPlayerHolder.getImageSubtitleSurfaceView(viewId).getHolder();

            SubtitleManager.getInstance().setSubtitleDisplay(
                    videoPlayerHolder.getPlayerView(viewId).getMMediaPlayer(), sh);
            //videoPlayerHolder.getPlayerView(viewId).setSubtitleDisplay(sh);
            if (Constants.bSupportDivx) {
                if(divxController != null) {
                    divxController.preparedProcess(viewId);
                }
            } else {
                if (isBreakPointPlay) {
                    videoPlayerHolder.getPlayerView(viewId).seekTo(breakPoint);
                }

                videoPlayerHolder.getPlayerView(viewId).start();
                initPlayer(viewId);
                dismissProgressDialog();
                checkABCycle(viewId);
            }
        }

        @Override
        public void onSeekComplete(MediaPlayer mp, int viewId) {
            playSpeed = 1;
            videoPlayerHolder.setPlaySpeed(1);
            im_play_pause.setVisibility(View.INVISIBLE);
            im_play_kuaijin.setVisibility(View.INVISIBLE);
            im_play_kuaitui.setVisibility(View.INVISIBLE);
            im_video_player_FF_FR_val.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onCloseMusic() {
        }

        @Override
        public void onUpdateSubtitle(String sub) {
        }
    };

    private void breakPointPlay(final int viewId, final int time, final String path) {
        isBreakPointPlay = false;
        videoHandler.sendEmptyMessageDelayed(Constants.HANDLE_MESSAGE_SKIP_BREAKPOINT, 5000);
        breakPointDialog = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.video_breakpoint_play))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(getResources().getString(R.string.exit_ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                videoHandler
                                        .removeMessages(Constants.HANDLE_MESSAGE_SKIP_BREAKPOINT);
                                isBreakPointPlay = true;
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.exit_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                videoHandler
                                        .removeMessages(Constants.HANDLE_MESSAGE_SKIP_BREAKPOINT);
                                isBreakPointPlay = false;
                            }
                        }).setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {
                        videoHandler.removeMessages(Constants.HANDLE_MESSAGE_SKIP_BREAKPOINT);
                        isBreakPointPlay = false;
                    }
                }).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        if (path != null) {
                            videoPlayerHolder.getPlayerView(viewId).setVideoPath(path, viewId);
                            // startPreTime[viewId - 1] =
                            // videoPlayerHolder.getPlayerView(viewId).getStartTime();
                            // System.currentTimeMillis();
                        }
                    }
                }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
                        if (KeyEvent.KEYCODE_MEDIA_PLAY == arg1
                                || KeyEvent.KEYCODE_MEDIA_PAUSE == arg1
                                || KeyEvent.KEYCODE_MEDIA_NEXT == arg1
                                || KeyEvent.KEYCODE_MEDIA_PREVIOUS == arg1) {
                            return true;
                        }
                        return false;
                    }
                }).show();

    }

    public void initPlayer(int viewId) {
        Log.i(TAG, "initPlayer viewId:" + viewId);
        dismissProgressDialog();

        videoPlayerHolder.getPlayerView().setVoice(-1);
        if (viewId == 1) {
            isOnePrepared = true;
        } else {
            isTwoPrepared = true;
        }
        String time="";
        if (nBlurayISOTitleIndex>-1) {
            duration = (int) Bluray.getTitleDuration(nBlurayISOTitleIndex)/45;
            // time = (int)Tools.formatISODuration(duration)/90.0;
        } else {
            duration = (int) videoPlayerHolder.getPlayerView(viewId).getDuration();
            // time = Tools.formatDuration(duration);
        }
        time = Tools.formatDuration(duration);
        Log.i(TAG, "initPlayer getDuration()" + time);
        if (mLocalMediaController != null) {
            mLocalMediaController.setDuration(duration);
        }

        // String time = Tools.formatDuration(duration);
        if (viewId == videoPlayerHolder.getViewId()) {
            videoPlayerHolder.total_time_video.setText(time);
            videoPlayerHolder.videoSeekBar.setMax(duration);
            if (viewId == 1) {
                if (playSettingSubtitleDialogOne != null) {
                    SubtitleManager.setSubtitleSettingOpt(1, getString(R.string.subtitle_1_value_1), viewId);
                }
            } else {
                if (playSettingSubtitleDialogTwo != null) {
                    SubtitleManager.setSubtitleSettingOpt(1, getString(R.string.subtitle_1_value_1), viewId);
                }
            }
            videoPlayerHolder.setPlaySpeed(1);
            videoHandler.sendEmptyMessageDelayed(THREED_INIT, 1000);
            playSpeed = 1;
            videoPlayerHolder.setAllUnSelect(videoPlayerHolder.getPlayerView().isPlaying());
            videoPlayerHolder.setVideoPlaySelect(true, videoPlayerHolder.getPlayerView().isPlaying());
            VideoPlayerViewHolder.state = VideoPlayerViewHolder.OPTION_STATE_PLAY;
            hideControlDelay();
            videoHandler.sendEmptyMessage(SEEK_POS);
        }
        isSeekable(viewId);
        // is3D(viewId); // comment out it for it is useless.
    }

    public boolean isSeekable(int viewId) {
        MMediaPlayer player = videoPlayerHolder.getPlayerView().getMMediaPlayer();
        Log.i(TAG, "***player***" + (player == null));
        if (player == null)
            return false;
        Metadata data = player.getMetadata(true, true);
        Log.i(TAG, "***data***" + (data != null));
        if (data != null) {
            Log.i(TAG, "***SEEK_AVAILABLE***" + data.has(Metadata.SEEK_AVAILABLE));
            if (data.has(Metadata.SEEK_AVAILABLE)) {
                videoPlayerHolder.setSeekVar(viewId, data.getBoolean(Metadata.SEEK_AVAILABLE));
                Log.i(TAG, "*****SEEK_AVAILABLE******" + videoPlayerHolder.isSeekable(viewId));
            }
            try {
                Class clz = Class.forName("android.media.Metadata");
                Method get = clz.getDeclaredMethod("recycleParcel");
                // http://hcgit:8080/#/q/bac5dbcffd9773356a969254d9c8733a419df8e4
                data.recycleParcel();
                Log.i(TAG, "recycleParcel");
            } catch (Exception e) {
                Log.i(TAG, "Can't find android.media.Metadata API recycleParcel !");
                e.printStackTrace();
            }

        }
        return videoPlayerHolder.isSeekable(viewId);
    }

    public void stopMediascanner() {
        Intent intent = new Intent();
        intent.setAction("action_media_scanner_stop");
        VideoPlayerActivity.this.sendBroadcast(intent);
        Log.i(TAG, "stopMediascanner");
    }

    public void startMediascanner() {
        Intent intent = new Intent();
        intent.setAction("action_media_scanner_start");
        VideoPlayerActivity.this.sendBroadcast(intent);
        Log.i(TAG, "startMediascanner");
    }

    private void exitPlayer() {
        videoHandler.removeMessages(SEEK_POS);
        setBreakPoint();
        if (videoPlayerHolder.mVideoPlayViewOne != null) {
            videoPlayerHolder.mVideoPlayViewOne.stopPlayer();
            videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
        }
        if (videoPlayerHolder.mVideoPlayViewTwo != null) {
            videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
            videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
        }
    }

    public void setBreakPoint() {
        if (mVideoPlayList.size() == 0) {
            return;
        }
        if (videoPlayerHolder.mVideoPlayViewOne != null) {
            if (isOnePrepared && videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition() > 0) {
                BreakPointRecord.addData(mVideoPlayList.get(video_position[0]).getPath(),
                        videoPlayerHolder.mVideoPlayViewOne.getCurrentPosition(), mVideoPlayList
                                .get(video_position[0]).getSize(), this);
            }
        }
        if (videoPlayerHolder.mVideoPlayViewTwo != null) {
            if (isTwoPrepared && videoPlayerHolder.mVideoPlayViewTwo.getCurrentPosition() > 0) {
                BreakPointRecord.addData(mVideoPlayList.get(video_position[1]).getPath(),
                        videoPlayerHolder.mVideoPlayViewTwo.getCurrentPosition(), mVideoPlayList
                                .get(video_position[1]).getSize(), this);
            }
        }
    }

    private Runnable breakPointRunnable = new Runnable() {
        @Override
        public void run() {
            while (!VideoPlayerActivity.this.isFinishing()) {
                try {
                    if (BreakPointRecord.getBreakPointFlag(VideoPlayerActivity.this)) {
                        Log.i(TAG, ">>>>>record break point>>>>>>");
                        setBreakPoint();
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    };

    // To receive off the radio exit play interface(Or switching source)
    BroadcastReceiver shutDownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "*******BroadcastReceiver**********" + intent.getAction());
            exitPlayer();
            if ((intent.getExtras() != null) && intent.getExtras().getInt("epgPara") == 1) {
                if (!Tools.unSupportTVApi()) {
                    try {
                        TvManager.getInstance().getTimerManager().execEpgTimerAction();
                    } catch (TvCommonException e) {
                        e.printStackTrace();
                    }
                }
            }
            finish();
        }
    };

    // When the network disconnection radio treatment
    BroadcastReceiver netDisconnectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (sourceFrom == Constants.SOURCE_FROM_SAMBA) {
                // videoPlayerHolder.mVideoPlayView.stopPlayer();
                Log.i(TAG, "netDisconnectReceiver: " + intent.getAction());
                Toast toast = Toast.makeText(VideoPlayerActivity.this, R.string.net_disconnect,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                // Avoid activity is lost player anomaly to upper send anomaly
                // Lead to error when showErrorDialog
                exitPlayer();
                VideoPlayerActivity.this.finish();
            }
        }
    };

    // Disk change monitoring
    private BroadcastReceiver diskChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Disk remove
            if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
                Log.i(TAG, "DiskChangeReceiver: " + action);
                String devicePath = intent.getData().getPath();
                Log.i(TAG, "DiskChangeReceiver: " + devicePath + " "
                        + mVideoPlayList.get(0).getPath());

                if (mVideoPlayList.get(0).getPath().contains(devicePath)) {
                    videoHandler.removeMessages(SEEK_POS);
                    // Avoid activity is lost player anomaly to upper send
                    // anomaly Lead to error when showErrorDialog
                    exitPlayer();
                    Toast toast = Toast.makeText(VideoPlayerActivity.this, R.string.disk_eject,
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    VideoPlayerActivity.this.finish();
                }
            }
        }
    };

    private BroadcastReceiver homeKeyEventBroadCastReceiver = new BroadcastReceiver() {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";//home key
        static final String SYSTEM_RECENT_APPS = "recentapps";//long home key
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        Log.i(TAG, "SYSTEM_HOME_KEY");
                        dismissVideoTimeSetDialog();
                        dismissVideoListDialog();
                        dismissVideoInfoDialog();
                        dismissSettingDialog();
                        dismissPlayAbDialog();
                        dismissVideoStartTimeDialog();
                        videoPlayerHolder.setSubtitleTextViewVisible(false);
                        if (breakPointDialog != null && breakPointDialog.isShowing()) {
                            breakPointDialog.dismiss();
                            breakPointDialog = null;
                        }
                        if (videoPlayerHolder.mVideoPlayViewOne != null) {
                            //videoPlayerHolder.mVideoPlayViewOne.setSubtitleDisplay(null);
                            SubtitleManager.getInstance().setSubtitleDisplay(
                                    videoPlayerHolder.getPlayerView().getMMediaPlayer(), null);
                            videoPlayerHolder.mVideoPlayViewOne.stopPlayback();
                            videoPlayerHolder.mVideoPlayViewOne.setPlayerCallbackListener(null);
                        }
                        if (videoPlayerHolder.mVideoPlayViewTwo != null) {
                            //videoPlayerHolder.mVideoPlayViewTwo.setSubtitleDisplay(null);
                            SubtitleManager.getInstance().setSubtitleDisplay(
                                    videoPlayerHolder.getPlayerView().getMMediaPlayer(), null);
                            videoPlayerHolder.mVideoPlayViewTwo.stopPlayer();
                            videoPlayerHolder.mVideoPlayViewTwo.setPlayerCallbackListener(null);
                        }
                        if (Tools.isThumbnailModeOn()) {
                            Tools.setThumbnailMode("0");
                            getThumbnailController().releaseThumbnailView(true);
                        }
                        VideoPlayerActivity.this.finish();
                        // android.os.Process.killProcess(Process.myPid());
                        if (Tools.isFloatVideoPlayModeOn()) {
                            FloatVideoController.getInstance().showFloatVideoWindow();
                            FloatVideoController.getInstance().getVideoListItem(Constants.DB_NAME, Constants.VIDEO_PLAY_LIST_TABLE_NAME);
                        }
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        // long home key
                    }
                }
            }
        }
    };

    protected void ThreeDInit() throws RemoteException {
        if (Tools.unSupportTVApi()) {
            return;
        }
        mThreeDimentionControl.checkMvcSource();
        if (mThreeDimentionControl.isVideoSourceMvc()) {
            // mVideoZoomControl.setVideoZoomMode(1);
        } else {
            // When play pre or next, remember 3D mode.
            mThreeDimentionControl.getThreeDMode();

            videoHandler.removeMessages(INIT_THREED);
            videoHandler.sendEmptyMessageDelayed(INIT_THREED, DETECT_3D_SOURCE_TIMEOUT);

        }
    }

    // add by avery.yan for fix pr:0400066
    private Runnable mRunnable43D = new Runnable() {
        @Override
        public void run() {
            try {
                mThreeDimentionControl.setOSD3DMode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // end add by avery.yan for fix pr:0400066
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (videoPlayerHolder.getPlayerView().isInPlaybackState()) {
                try {
                    mThreeDimentionControl.ThreeDInit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public PlaySettingSubtitleDialog getPlaySettingSubtitleDialog(int id) {
        if (id == 1) {
            return playSettingSubtitleDialogOne;
        } else {
            return playSettingSubtitleDialogTwo;
        }
    }

    /*
    private boolean doForward() {
        boolean flag = false;
        mSecondTimeMillis = System.currentTimeMillis();
        if (mSecondTimeMillis - mFirstTimeMillis < 600) {
            flag = true;
            Toast toast = ToastFactory.getToast(VideoPlayerActivity.this,
                    getResources().getString(R.string.video_media_do_forward_info), Gravity.CENTER);
            toast.show();
        }

        mFirstTimeMillis = System.currentTimeMillis();

        return flag;
    }
    */

    public String getCurrentVideoPlayPath() {
        return mCurrentVideoPlayPath;
    }

    public ThumbnailController getThumbnailController() {
        return mThumbnailController;
    }

    public boolean isThreeDMode() {
        if (!Tools.unSupportTVApi() && mThreeDimentionControl != null) {
            return (mThreeDimentionControl.isThreeDMode() || this.getString(R.string.play_setting_0_value_1).equals(Tools.getPlaySettingOpt(0, 1)));
        } else {
            return false;
        }
    }

    public Parcel getDivxPlusInfo(int Key){
        if (videoPlayerHolder.getPlayerView().isInPlaybackState()) {
            MMediaPlayer player = videoPlayerHolder.getPlayerView().getMMediaPlayer();
            return  player.getParcelParameter(Key);
        }
        return null;
    }

    public static void getDisplayFormat(final VideoPlayerActivity context, final int viewId) {
        if (!Tools.unSupportTVApi()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        context.displayFormat = Tools.getCurrent3dFormat();
                        if (context.displayFormat == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                            Tools.setPlaySettingOpt(0, context.getString(R.string.play_setting_0_value_2), viewId);
                        }
                    }
                }).start();
        }
    }

    public boolean imageRotate(int viewId, int degrees) {
        return getVideoPlayHolder().getPlayerView(viewId).imageRotate(degrees);
    }

    /* This function is for initialize video display aspect ratio before mediaplayer initialize.*/
    public void setVideoDisplayRotate90(int viewId) {
        float ratio = (float)mScreenResolutionHeight/mScreenResolutionWidth;
        FrameLayout.LayoutParams params = null;
        int videoHeight = mScreenResolutionHeight;
        int videoWidth = mScreenResolutionHeight * mScreenResolutionHeight / mScreenResolutionWidth;
        Log.i(TAG, "--- setVideoDisplayRotate90--- mScreenResolutionWidth:" + mScreenResolutionWidth + " screenHeight:"
                + mScreenResolutionHeight + " ratio:" + ratio + " videoWidth:" + videoWidth);
        params = new FrameLayout.LayoutParams(videoWidth , videoHeight, Gravity.CENTER);
        videoPlayerHolder.getLinearLayout(viewId).setLayoutParams(params);
    }

    /* If Rotate 0/180 or close Rotate, should reset VideoDisplayAspectRatio.*/
    public void setVideoDisplayFullScreen(int viewId) {
        Log.i(TAG, "---setVideoDisplayFullScreen-- viewId:" + viewId);
        FrameLayout.LayoutParams params = null;
        params = new FrameLayout.LayoutParams(mScreenResolutionWidth , mScreenResolutionHeight, Gravity.CENTER);
        videoPlayerHolder.getLinearLayout(viewId).setLayoutParams(params);
    }
	//zb20160112 add
	public int getAudioTrackCount() {
        MMediaPlayer player = videoPlayerHolder.getPlayerView().getMMediaPlayer();
        Log.i(TAG, "getAudioTrackCount: (player == null) = " + (player == null));
        if (player == null)
            return 1;
        Metadata data = player.getMetadata(true, true);
        Log.i(TAG, "getAudioTrackCount: (data != null) = " + (data != null));
        if (data != null) {
            int totalTrackNum = 1;
            if (data.has(Metadata.TOTAL_TRACK_NUM)) {
                totalTrackNum = data.getInt(Metadata.TOTAL_TRACK_NUM);
                Log.i(TAG, "getAudioTrackCount: totalTrackNum = " + totalTrackNum);
                return totalTrackNum;
            }
        }
        return 1;
    }
	public void changeAudioTrack(int step)
    {
    	if (step == 1 || step == -1)
    	{
    		Log.d(TAG, "Track number: " + mAudioTrackCount);
            if (mAudioTrackCount < 2) {
            	showAudioTrackToast(numberAudioTrack);
                return;
            }
            numberAudioTrack = (numberAudioTrack + step) % mAudioTrackCount;
            if (numberAudioTrack<0) {
                numberAudioTrack = mAudioTrackCount - 1;
            }
           setAudioTrack(numberAudioTrack);
           showAudioTrackToast(numberAudioTrack);
    	}
    }
	public void setAudioTrack(int track) {
        videoPlayerHolder.getPlayerView().setAudioTrack(track);
    }
	private void showAudioTrackToast(int audioTrack)
	{
		Toast toast = Toast.makeText(this, 
				getResources().getString(R.string.audio_track_setting_0_value_1)
				+ " " + (getCurrentAudioTrackId() + 1), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 100);
		toast.show();
	}
    public int getCurrentAudioTrackId() {
        MMediaPlayer player = videoPlayerHolder.getPlayerView().getMMediaPlayer();
        if (player == null)
            return 1;
        Metadata data = player.getMetadata(true, true);
        if (data != null) {
            int CurrentTrackId = 0;
            int count = 0;
            if (data.has(Metadata.TOTAL_TRACK_NUM)) {
                count = data.getInt(Metadata.TOTAL_TRACK_NUM);
            }
            if (data.has(Metadata.CURRENT_AUDIO_TRACK_ID)) {
                CurrentTrackId = data.getInt(Metadata.CURRENT_AUDIO_TRACK_ID);
                if (count != 0) {
                    return CurrentTrackId % count;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

	//end
	private class CecCtrlEventListener implements OnCecCtrlEventListener {
        @Override
        public boolean onCecCtrlEvent(int what, int arg1, int arg2) {
            switch (what) {
                case TvCecManager.TVCEC_STANDBY: {
                    Log.i(TAG, "EV_CEC_STANDBY");
                    TvCommonManager.getInstance().standbySystem("cec");
                }
                    break;
                case TvCecManager.TVCEC_ARC_VOLUME_VALUE:{
                           //sendBroadcast(new Intent("VolumePanel_cec_SYN.show"));
                    AudioManager audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    //if(SystemProperties.getInt("persist.sys.istvmute",0)==1)
                    {
                        audiomanager.setMasterVolume(arg1, 1);
                    }
                }
                    break;
                case TvCecManager.TVCEC_ARC_MUTE_STATUS:
                {
                    Log.i(TAG, "EV_CEC_ARC_MUTE_STATUS:"+arg1);
                    AudioManager audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audiomanager.setMasterMute((arg1==1)?true:false);
                }
                    break;
                case TvCecManager.TVCEC_ARC_STATUS_REPORT:
                {
                    Log.i(TAG, "TVCEC_ARC_STATUS_REPORT:"+arg1);
                    AudioManager audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (audiomanager.isMasterMute())
                    audiomanager.setMasterMute(true, (arg1==1)?0:(AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_VIBRATE));
                }
                    break;
                default: {
                    Log.i(TAG, "Unknown message type " + what);
                }
                    break;
            }
            return true;
        }
    }
}
