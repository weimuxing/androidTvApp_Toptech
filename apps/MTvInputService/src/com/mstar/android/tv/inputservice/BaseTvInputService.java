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

package com.mstar.android.tv.inputservice;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.media.tv.TvInputManager;
import android.media.tv.TvInputManager.Hardware;
import android.media.tv.TvInputManager.HardwareCallback;
import android.media.tv.TvInputService;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputHardwareInfo;
import android.media.tv.TvContentRating;
import android.media.tv.TvStreamConfig;
import android.media.tv.TvContract;
import android.media.tv.TvTrackInfo;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.os.AsyncTask;
import android.os.SystemProperties;
import android.view.Surface;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.util.Log;

import com.mstar.android.tvapi.common.vo.VideoInfo;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager.OnScreenSaverEventListener;
import com.mstar.android.tv.TvChannelManager.OnSignalEventListener;
import com.mstar.android.tv.inputservice.data.WindowControl;
import com.mstar.android.tv.inputservice.data.WindowData;
import com.mstar.android.tv.inputservice.data.WindowData.TvInputEventListener;
import com.mstar.android.tv.inputservice.ui.OverlayView;
import com.mstar.android.tv.util.Constant;
import com.mstar.android.tv.util.BroadcastType;
import com.mstar.android.tv.util.Utility;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.TvManager;

import org.xmlpull.v1.XmlPullParserException;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * BaseTvInputService
 */
public class BaseTvInputService extends TvInputService {
    private static final String TAG = "BaseTvInputService";

    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    @Override
    public Session onCreateSession(String inputId) {
        Log.d(TAG, "onCreateSession: inputId = " + inputId);
        if (WindowControl.acquireWindow(inputId)) {
            return new SessionImpl(this, inputId);
        } else {
            return null;
        }
    }

    @Override
    public String onHardwareRemoved(TvInputHardwareInfo hardwareInfo) {
        return null;
    }

    @Override
    public TvInputInfo onHdmiDeviceAdded(HdmiDeviceInfo deviceInfo) {
        return null;
    }

    @Override
    public String onHdmiDeviceRemoved(HdmiDeviceInfo deviceInfo) {
        return null;
    }

    public ResolveInfo getResolveInfo(String serviceName) {
        ResolveInfo info = null;
        PackageManager pm = getPackageManager();
        List<ResolveInfo> services = pm.queryIntentServices(new Intent(
                TvInputService.SERVICE_INTERFACE), PackageManager.GET_SERVICES
                | PackageManager.GET_META_DATA);
        for (ResolveInfo ri : services) {
            ServiceInfo si = ri.serviceInfo;
            if (!android.Manifest.permission.BIND_TV_INPUT.equals(si.permission)) {
                continue;
            }
            if (si.name.equals(serviceName)) {
                info = ri;
                break;
            }
        }
        return info;
    }

    public static class SessionImpl extends Session {
        private final String TV_INPUT_HDMI1_PREFIX = "com.mstar.android.tv.inputservice/.Hdmi1InputService/HW";
        private final String TV_INPUT_HDMI2_PREFIX = "com.mstar.android.tv.inputservice/.Hdmi2InputService/HW";
        private final String TV_INPUT_HDMI3_PREFIX = "com.mstar.android.tv.inputservice/.Hdmi3InputService/HW";
        private final String TV_INPUT_HDMI4_PREFIX = "com.mstar.android.tv.inputservice/.Hdmi4InputService/HW";

        protected Context mContext;

        protected TvInputManager mTvInputManager;

        protected String mInputId;

        protected int mDeviceId = TvCommonManager.INPUT_SOURCE_NONE;

        protected int mScreenSaverMode = Constant.SCREENSAVER_DEFAULT_MODE;

        protected int mWidth = 0;

        protected int mHeight = 0;

        protected TvCommonManager mTvCommonManager;

        protected TvChannelManager mTvChannelManager;

        protected OverlayView mOverlayView;

        protected BroadcastType mBroadcastType = null;

        private static final int SIGNAL_UNLOCK_EVENT_DELAY = 2000;

        private static final int AUTO_ADJUST_SHOW_TIME = 3000;

        private Surface mSurface = null;

        private Hardware mHardware = null;

        private List<TvStreamConfig> mStreamConfigList = new ArrayList<TvStreamConfig>();

        private HardwareCallbackImpl mCallback = new HardwareCallbackImpl();

        private OnScreenSaverEventListener mScreenSaverEventListener = null;

        private OnSignalEventListener mSignalEventListener = null;

        private TuneTask mTuneTask;

        protected List<TvTrackInfo> mTrackInfoList = new ArrayList<TvTrackInfo>();

        private Runnable mScreenSaverRunnable = new Runnable() {
            @Override
            public void run() {
                mOverlayView.dismissScreenSaverTextView();
            }
        };

        private boolean isOverlayNeedtoBeUpdated = false;

        SessionImpl(Context context, String inputId) {
            super(context);
            setOverlayViewEnabled(true);
            mContext = context;
            mInputId = inputId;
            mTvInputManager = (TvInputManager) context.getSystemService(Context.TV_INPUT_SERVICE);
            final String DELIMITER = "/";
            final String PREFIX_HARDWARE_DEVICE = "HW";
            String[] strs = inputId.split(DELIMITER);
            if (strs.length > 0) {
                String lastSegment = strs[strs.length - 1];
                if (lastSegment.startsWith(PREFIX_HARDWARE_DEVICE)) {
                    try {
                        mDeviceId = Integer.parseInt(lastSegment.substring(PREFIX_HARDWARE_DEVICE.length()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            mTvCommonManager = TvCommonManager.getInstance();
            mTvChannelManager = TvChannelManager.getInstance();
            mBroadcastType = BroadcastType.getInstance();
            mScreenSaverEventListener = new ScreenSaverEventListener();
            mSignalEventListener = new SignalEventListener();
            mTvChannelManager.registerOnScreenSaverEventListener(mScreenSaverEventListener);
            mTvChannelManager.registerOnSignalEventListener(mSignalEventListener);
            mTvCommonManager.setTvosCommonCommand(Constant.TV_EVENT_LISTENER_READY);
            SystemProperties.set("mstar.tif.enable", "true");

            TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {
                @Override
                public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                    return false;
                }

                @Override
                public boolean onAutoSwitch(int arg0, final int arg1, int arg2) {
                    Log.d(TAG, "onAutoSwitch(), arg1 = " + arg1);
                    Intent live_tv_intent = new Intent();
                    live_tv_intent.setComponent(new ComponentName("com.android.tv",
                                    "com.android.tv.MainActivity"));
                    if (isIntentSupported(live_tv_intent)) {
                        Uri channelUri = null;
                        switch (arg1) {
                            case TvCommonManager.INPUT_SOURCE_HDMI:
                                channelUri = buildChannelUriForPassthroughInput(TV_INPUT_HDMI1_PREFIX + arg1);
                                break;
                            case TvCommonManager.INPUT_SOURCE_HDMI2:
                                channelUri = buildChannelUriForPassthroughInput(TV_INPUT_HDMI2_PREFIX + arg1);
                                break;
                            case TvCommonManager.INPUT_SOURCE_HDMI3:
                                channelUri = buildChannelUriForPassthroughInput(TV_INPUT_HDMI3_PREFIX + arg1);
                                break;
                            case TvCommonManager.INPUT_SOURCE_HDMI4:
                                channelUri = buildChannelUriForPassthroughInput(TV_INPUT_HDMI4_PREFIX + arg1);
                                break;
                        }
                        if (channelUri != null) {
                            live_tv_intent.setAction(Intent.ACTION_VIEW);
                            live_tv_intent.setData(channelUri);
                            live_tv_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            mContext.startActivity(live_tv_intent);
                        }
                    }
                    return true;
                }
            });
        }

        private boolean isIntentSupported(Intent intent) {
            boolean ret = false;
            List<ResolveInfo> resolveInfo = mContext.getApplicationContext().getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfo.size() > 0) {
                ret = true;
            }
            return ret;
        }

         /**
         * Build a special channel URI intended to be used with pass-through inputs. (e.g. HDMI)
         *
         * @param inputId The ID of the pass-through input to build a channels URI for.
         * @see TvInputInfo#isPassthroughInput()
         */
        private final Uri buildChannelUriForPassthroughInput(String inputId) {
            return new Uri.Builder().scheme("content").authority("android.media.tv")
                    .appendPath("passthrough").appendPath(inputId).build();
        }

        @Override
        public void onRelease() {
            mHandler.removeMessages(Constant.CMD_SIGNAL_UNLOCK);
            mHandler.removeCallbacks(mScreenSaverRunnable);
            if (mHardware != null) {
                mTvInputManager.releaseTvInputHardware(mDeviceId, mHardware);
            }
            unregisterListener();
            if (mTuneTask != null) {
                mTuneTask.cancel(true);
                mTuneTask = null;
            }
            WindowControl.releaseWindow(mInputId);
            SystemProperties.set("mstar.tif.enable", "false");
        }

        @Override
        public void onSetMain(boolean isMain) {
        }

        @Override
        public boolean onSetSurface(Surface surface) {
            if (surface != null) {
                if (mSurface != null && mSurface.equals(surface)) {
                    if (DEBUG) Log.d(TAG, "onSetSurface: surface is equals to the previous one");
                } else {
                    mSurface = surface;
                    if (DEBUG) Log.d(TAG, "onSetSurface: new surface");
                    if (mDeviceId == TvCommonManager.INPUT_SOURCE_NONE) {
                        Log.e(TAG, "onSetSurface: mDeviceId == INPUT_SOURCE_NONE, return false");
                        return false;
                    }
                    if (mHardware != null) {
                        mTvInputManager.releaseTvInputHardware(mDeviceId, mHardware);
                    }
                    mHardware = mTvInputManager.acquireTvInputHardware(mDeviceId, mCallback,
                            mTvInputManager.getTvInputInfo(mInputId));
                    if (mHardware == null) {
                        return false;
                    }
                    if (mCallback.mConfigList.isEmpty()) {
                        if (DEBUG) Log.d(TAG, "onSetSurface: no stream config, wait until callback notify");
                        synchronized (mCallback.mLock) {
                            try {
                                mCallback.mLock.wait(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (!mCallback.mConfigList.isEmpty()) {
                        mStreamConfigList.addAll(mCallback.mConfigList);
                    }
                }
            } else {
                mSurface = null;
                if (mHardware == null) {
                    return false;
                }
                mHardware.setSurface(null, null);
            }
            return true;
        }

        @Override
        public void onSurfaceChanged(int format, int width, int height) {
            if (DEBUG) Log.d(TAG, "onSurfaceChanged: width = " + width + ",  height = " + height);
            mWidth = width;
            mHeight = height;
        }

        @Override
        public void onOverlayViewSizeChanged(int width, int height) {
            if (DEBUG) Log.d(TAG, "onOverlayViewSizeChanged: width = " + width + ",  height = " + height);
            mWidth = width;
            mHeight = height;
        }

        @Override
        public void onSetStreamVolume(float volume) {
            if (mHardware == null) {
                mHardware = mTvInputManager.acquireTvInputHardware(mDeviceId, mCallback,
                        mTvInputManager.getTvInputInfo(mInputId));
            }
            if (mHardware != null) {
                if (DEBUG) Log.d(TAG, "onSetStreamVolume: call mHardware.setStreamVolume, volume = " + volume);
                mHardware.setStreamVolume(volume);
            }
        }

        @Override
        public boolean onTune(Uri channelUri) {
            Log.d(TAG, "onTune: channelUri = " + channelUri +
                    ", mWidth = " + mWidth +
                    ", mHeight = " + mHeight);
            return tune(0, 0, mWidth, mHeight, mDeviceId, channelUri);
        }

        @Override
        public void onSetCaptionEnabled(boolean enabled) {
        }

        @Override
        public void onUnblockContent(TvContentRating unblockedRating) {
        }

        @Override
        public boolean onSelectTrack(int type, String trackId) {
            return false;
        }

        @Override
        public void onAppPrivateCommand(String action, Bundle data) {
        }

        @Override
        public View onCreateOverlayView() {
            if (DEBUG) Log.d(TAG, "onCreateOverlayView");
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    LAYOUT_INFLATER_SERVICE);
            mOverlayView = (OverlayView) inflater.inflate(R.layout.tv_overlay, null);
            updateOverlayView();
            return mOverlayView;
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_MAIN &&
                    mBroadcastType.sendCecKey(keyCode, WindowControl.getWindowData(mInputId).getSource())) {
                if (DEBUG) Log.d(TAG, "onKeyDown: sendCecKey success!");
                return true;
            }
            return false;
        }

        @Override
        public boolean onKeyLongPress(int keyCode, KeyEvent event) {
            return false;
        }

        @Override
        public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
            return false;
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return false;
        }

        @Override
        public boolean onTrackballEvent(MotionEvent event) {
            return false;
        }

        @Override
        public boolean onGenericMotionEvent(MotionEvent event) {
            return false;
        }

        protected Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Constant.CMD_SIGNAL_LOCK:
                        if (WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_MAIN) {
                            signalLockHandler();
                            notifyTracksInfo();
                        }
                        break;
                    case Constant.CMD_SIGNAL_UNLOCK:
                        if (WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_MAIN) {
                            signalUnlockHandler();
                        }
                        break;
                    case Constant.CMD_SCREEN_SAVER:
                        if (WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_MAIN) {
                            final int status = msg.arg1;
                            screenSaverHandler(status);
                        }
                        break;
                    default:
                        break;
                }
            }
        };

        public boolean tune(int left, int top, int width, int height, int source, Uri channelUri) {
            mScreenSaverMode = Constant.SCREENSAVER_DEFAULT_MODE;
            WindowData windowData = new WindowData.Builder()
                    .setInputId(mInputId).setSource(source)
                    .setLeft(left).setTop(top)
                    .setWidth(width).setHeight(height).setListener(
                    new TvInputEventListener() {
                        public void onEvent() {
                            setSurfaceToHardware();
                        }
                    }).build();
            if (mTuneTask != null) {
                mTuneTask.cancel(true);
                mTuneTask = null;
            }
            mTuneTask = new TuneTask(windowData);
            mTuneTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return true;
        }

        protected void setSurfaceToHardware() {
            if (mHardware == null || mSurface == null) {
                return;
            }
            int streamId = WindowControl.getWindowType(mInputId);
            TvStreamConfig config = null;
            if (mStreamConfigList != null && mStreamConfigList.size() > 0) {
                for (TvStreamConfig item : mStreamConfigList) {
                    if (item.getStreamId() == streamId) {
                        if (DEBUG) Log.d(TAG, "setSurfaceToHardware: find stream config for id " + streamId);
                        config = item;
                        break;
                    }
                }
            }
            if (config == null) {
                config = new TvStreamConfig.Builder().streamId(streamId)
                        .type(TvStreamConfig.STREAM_TYPE_INDEPENDENT_VIDEO_SOURCE)
                        .maxWidth(1920).maxHeight(1080).generation(1).build();
            }
            mHardware.setSurface(mSurface, config);
        }

        protected void signalLockHandler() {
            Log.d(TAG, "signalLockHandler: mInputId = " + mInputId);
            showScreenSaverIfNeeded();
        }

        protected void updateOverlayView() {
            if (isOverlayNeedtoBeUpdated == true && mOverlayView != null) {
                mScreenSaverMode = Constant.SCREENSAVER_DEFAULT_MODE;
                Bundle extras = new Bundle();
                extras.putString(Constant.SESSION_EVENT_KEY_SCREEN_SAVER_TEXT, mContext.getResources()
                        .getString(R.string.str_no_signal).toLowerCase());
                notifySessionEvent(Constant.SESSION_EVENT_SCREEN_SAVER, extras);
                mOverlayView.updateScreenSaverTextView(
                        mContext.getResources().getString(R.string.str_no_signal));
                isOverlayNeedtoBeUpdated = false;
            }
        }

        protected void signalUnlockHandler() {
            isOverlayNeedtoBeUpdated = true;
            updateOverlayView();
        }

        protected void screenSaverHandler(int status) {
            if (mScreenSaverMode != status) {
                mScreenSaverMode = status;
            }
            showScreenSaverIfNeeded();
        }

        protected void unregisterListener() {
            if (DEBUG) Log.d(TAG, "unregisterListener");
            if (mScreenSaverEventListener != null) {
                mTvChannelManager.unregisterOnScreenSaverEventListener(mScreenSaverEventListener);
                mScreenSaverEventListener = null;
            }
            if (mSignalEventListener != null) {
                mTvChannelManager.unregisterOnSignalEventListener(mSignalEventListener);
                mSignalEventListener = null;
            }
        }

        protected void notifyTracksInfo() {
            buildTrackInfoList();
            if (mTrackInfoList.size() > 0) {
                notifyTracksChanged(mTrackInfoList);
                selectDefaultVideoTracks();
                selectDefaultAudioTracks();
                selectDefaultSubtitleTracks();
            }
        }

        public boolean isTracksContain(int type) {
            for (TvTrackInfo track : mTrackInfoList) {
                if (track.getType() == type) {
                    return true;
                }
            }
            return false;
        }

        public void selectDefaultVideoTracks() {
            if (isTracksContain(TvTrackInfo.TYPE_VIDEO)) {
                notifyTrackSelected(TvTrackInfo.TYPE_VIDEO,
                        Utility.buildTrackId(TvTrackInfo.TYPE_VIDEO, 0));
            }
        }

        public void selectDefaultAudioTracks() {
            //Do nothing in the BaseTvInputService
        }

        public void selectDefaultSubtitleTracks() {
            //Do nothing in the BaseTvInputService
        }

        private void buildTrackInfoList() {
            mTrackInfoList.clear();
            addVideoTrackInfo();
            addAudioTrackInfo();
            addSubtitleTrackInfo();
        }

        void addVideoTrackInfo() {
            TvTrackInfo track = getVideoTrackInfo(WindowControl.getWindowData(mInputId).getSource());
            if (track != null) {
                mTrackInfoList.add(track);
            }
        }
        void addAudioTrackInfo() {
            //Do nothing in the BaseTvInputService
        }
        void addSubtitleTrackInfo() {
            //Do nothing in the BaseTvInputService
        }

        /**
         * Get the video track information.
         *
         * @param inputSource The input source. The value is one of the following:
         * <ul>
         * <li>{@link TvCommonManager#INPUT_SOURCE_ATV}
         * <li>{@link TvCommonManager#INPUT_SOURCE_DTV}
         * </ul>
         * @return The {@link TvTrackInfo} instance for the video information.
         */
        private static TvTrackInfo getVideoTrackInfo(int inputSource) {
            VideoInfo videoInfo = TvPictureManager.getInstance().getVideoInfo();
            if (videoInfo == null) {
                return null;
            }
            TvTrackInfo videoTrack = null;
            if (DEBUG) {
                Log.d(TAG, "videoInfo: " + videoInfo.hResolution + ", " +
                        videoInfo.vResolution + ", " +
                        videoInfo.frameRate);
            }
            videoTrack = new TvTrackInfo.Builder(TvTrackInfo.TYPE_VIDEO,
                    Utility.buildTrackId(TvTrackInfo.TYPE_VIDEO, 0))
                            .setVideoWidth(videoInfo.hResolution)
                            .setVideoHeight(videoInfo.vResolution)
                            .setVideoFrameRate(videoInfo.frameRate)
                            .build();
            return videoTrack;
        }

        private void showScreenSaverIfNeeded() {
            if (mOverlayView == null) {
                Log.d(TAG, "showScreenSaverIfNeeded: mOverlayView == null, return");
                return;
            }
            String screenSaverString = mBroadcastType.getScreenSaverString(mContext,
                    mScreenSaverMode,
                    WindowControl.getWindowData(mInputId).getSource());
            mOverlayView.updateScreenSaverTextView(screenSaverString);
            Bundle extras = new Bundle();
            extras.putString(Constant.SESSION_EVENT_KEY_SCREEN_SAVER_TEXT, screenSaverString.toLowerCase());
            notifySessionEvent(Constant.SESSION_EVENT_SCREEN_SAVER, extras);
            if (screenSaverString.equals(mContext.getResources().getString(R.string.str_auto_adjust))) {
                mHandler.postDelayed(mScreenSaverRunnable, AUTO_ADJUST_SHOW_TIME);
            }
        }

        private class TuneTask extends AsyncTask<Void, Void, Void> {
            private WindowData mWindowData;

            public TuneTask(WindowData windowData) {
                mWindowData = windowData;
            }

            @Override
            protected Void doInBackground(Void... arg) {
                if (isCancelled()) {
                    if (DEBUG) Log.d(TAG, "TuneTask: TuneTask has been canceled!!!");
                    return null;
                }
                notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING);
                if (WindowControl.setWindowData(mWindowData)) {
                    if (WindowControl.getWindowType(mInputId) != WindowControl.WINDOW_TYPE_MAIN) {
                        Log.d(TAG, "TuneTask: unregisterListener");
                        unregisterListener();
                    }
                    WindowControl.run();
                }
                notifyVideoAvailable();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mTuneTask = null;
            }
        }

        private static class HardwareCallbackImpl extends HardwareCallback {
            protected Object mLock = new Object();
            protected List<TvStreamConfig> mConfigList = new ArrayList<TvStreamConfig>();

            @Override
            public void onReleased() {
                mConfigList.clear();
            }

            @Override
            public void onStreamConfigChanged(TvStreamConfig[] configs) {
                synchronized (mLock) {
                    if (DEBUG) Log.d(TAG, "HardwareCallbackImpl::onStreamConfigChanged");
                    mConfigList.clear();
                    for (int i = 0; i < configs.length; i++) {
                        mConfigList.add(configs[i]);
                    }
                    mLock.notify();
                }
            }
        }

        private class ScreenSaverEventListener implements OnScreenSaverEventListener {
            @Override
            public boolean onScreenSaverEvent(int what, int arg1, int arg2, Object obj) {
                if (what == TvChannelManager.TVPLAYER_SCREEN_SAVER_MODE) {
                    if (DEBUG) Log.d(TAG, "onScreenSaverEvent: TVPLAYER_SCREEN_SAVER_MODE(arg1 = " + arg1 + ", arg2 = " + arg2 + ")");
                    Message m = Message.obtain();
                    m.what = Constant.CMD_SCREEN_SAVER;
                    m.arg1 = arg1;
                    mHandler.sendMessage(m);
                    return true;
                }
                return false;
            }
        }

        private class SignalEventListener implements OnSignalEventListener {
            @Override
            public boolean onAtvSignalEvent(int what, int arg1, int arg2, Object obj) {
                return processSignalEvent(what, arg1, arg2, obj);
            }

            @Override
            public boolean onDtvSignalEvent(int what, int arg1, int arg2, Object obj) {
                return processSignalEvent(what, arg1, arg2, obj);
            }

            @Override
            public boolean onTvSignalEvent(int what, int arg1, int arg2, Object obj) {
                return processSignalEvent(what, arg1, arg2, obj);
            }

            private boolean processSignalEvent(int what, int arg1, int arg2, Object obj) {
                mHandler.removeMessages(Constant.CMD_SIGNAL_UNLOCK);
                if (what == TvChannelManager.TVPLAYER_SIGNAL_LOCK) {
                    if (DEBUG) Log.d(TAG, "processSignalEvent: TVPLAYER_SIGNAL_LOCK(arg1 = " + arg1 + ", arg2 = " + arg2 + ")");
                    mHandler.sendEmptyMessage(Constant.CMD_SIGNAL_LOCK);
                    return true;
                } else if (what == TvChannelManager.TVPLAYER_SIGNAL_UNLOCK) {
                    if (DEBUG) Log.d(TAG, "processSignalEvent: TVPLAYER_SIGNAL_UNLOCK(arg1 = " + arg1 + ", arg2 = " + arg2 + ")");
                    mHandler.sendEmptyMessage(Constant.CMD_SIGNAL_UNLOCK);
                    return true;
                }
                return false;
            }
        }
    }
}
