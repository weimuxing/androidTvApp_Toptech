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

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Context;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.ContentObserver;
import android.media.tv.TvContract;
import android.media.tv.TvInputManager;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputHardwareInfo;
import android.media.tv.TvTrackInfo;
import android.media.tv.TvContentRating;
import android.provider.Settings;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.os.RemoteException;
import android.os.AsyncTask;
import android.text.format.Time;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.Log;
import android.view.accessibility.CaptioningManager;
import android.media.PlaybackParams;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCommonManager.OnDialogEventListener;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager.OnChannelInfoEventListener;
import com.mstar.android.tv.TvChannelManager.OnEpgEventListener;
import com.mstar.android.tv.TvChannelManager.OnCiEventListener;
import com.mstar.android.tv.TvChannelManager.OnEmergencyAlertEventListener;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvCiManager.OnCiStatusChangeEventListener;
import com.mstar.android.tv.TvCiManager.OnCiAsyncCmdNotifyListener;
import com.mstar.android.tv.TvCiManager.OnUiEventListener;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.MwAtscEasInfo;
import com.mstar.android.tv.inputservice.data.WindowControl;
import com.mstar.android.tv.inputservice.data.WindowData;
import com.mstar.android.tv.inputservice.data.WindowData.TvInputEventListener;
import com.mstar.android.tv.util.Utility;
import com.mstar.android.tv.util.Constant;
import com.mstar.android.tv.util.TunerUtility;
import com.mstar.android.tv.util.BroadcastType;
import com.mstar.android.tv.util.TifProgramInfo;
import com.mstar.android.tv.util.ChannelUpdateTask;
import com.mstar.android.tv.tunersetup.TvIntent;
import com.mstar.android.tv.inputservice.R;
import com.mstar.android.tv.util.PVRControlUnit;
import com.mstar.android.tv.dtv.TeletextActivity;
import com.mstar.android.MKeyEvent;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * TunerInputService
 */
public class TunerInputService extends BaseTvInputService {
    private static final String TAG = "TunerInputService";

    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    @Override
    public Session onCreateSession(String inputId) {
        Log.d(TAG, "onCreateSession: inputId = " + inputId);
        if (WindowControl.acquireWindow(inputId)) {
            return new TunerSessionImpl(this, inputId);
        } else {
            return null;
        }
    }

    @Override
    public TvInputInfo onHardwareAdded(TvInputHardwareInfo hardwareInfo) {
        if (hardwareInfo.getType() != TvInputHardwareInfo.TV_INPUT_TYPE_TUNER) {
            return null;
        }
        Log.d(TAG, "onHardwareAdded: deviceId = " + hardwareInfo.getDeviceId());
        ComponentName name = new ComponentName(this, TunerInputService.class);
        ResolveInfo info = getResolveInfo(name.getClassName());
        TvInputInfo inputInfo = null;
        if (info != null) {
            try {
                inputInfo = TvInputInfo.createTvInputInfo(this, info, hardwareInfo, null, null);
            } catch (XmlPullParserException | IOException e) {
            }
        }
        return inputInfo;
    }

    static class TunerSessionImpl extends SessionImpl {
        private final static int EPG_UPDATE_PERIOD_MS = 10000;
        private final static int EPG_UPDATE_DELAY_MS = 3000;
        private final static int SIGNAL_LOCK_DELAY_MS = 3000;
        private final static int PROGRAM_UPDATE_DELAY_MS = 1000;
        private final static int EAS_DISPLAY_TIME_DEFAULT = 10;
        private final static int EAS_DISPLAY_TIME_CHECK = 15;
        private final static int EAS_DISPLAY_TIME_EXTEND = 30;
        private TvCommonManager mTvCommonManager;
        private TvChannelManager mTvChannelManager;
        private TvCiManager mTvCiManager;
        private int mCurrentSource;
        private OnChannelInfoEventListener mChannelInfoEventListener = null;
        private OnEpgEventListener mEpgEventListener = null;
        private OnCiEventListener mCiEventListener = null;
        private OnEmergencyAlertEventListener mEmergencyAlertEventListener = null;
        private OnDialogEventListener mDialogEventListener = null;
        private OnCiStatusChangeEventListener mCiStatusChangeEventListener = null;
        private OnCiAsyncCmdNotifyListener mCiAsyncCmdNotifyListener = null;
        private OnUiEventListener mCiUiEventListener = null;
        private BroadcastReceiver mReceiver = null;
        private TvProgramInfo mTvProgramInfo = null;
        private EasData mEasData = null;
        private TuneTask mTuneTask;
        private EpgUpdateTask mEpgUpdateTask;
        private ChannelUpdateTask mChannelUpdateTask;
        private boolean mHandleOnKeyDown = false;
        private CaptioningManager mCaptioningManager;
        private PVRControlUnit mPVRCtrlUnit;

        private Runnable mSignalLockRunnable = new Runnable() {
            @Override
            public void run() {
                notifyTracksInfo();
                insertCurrentProgramInfoToTvProvider();
                checkRatingBlocked();
            }
        };
        private Runnable mEasDisplayTextRunnable = new Runnable() {
            @Override
            public void run() {
                int remainTime = mEasData.getRemainTime();
                if (DEBUG) Log.d(TAG, "mEasDisplayTextRunnable: remainTime = " + remainTime);
                if (remainTime > 0) {
                    mOverlayView.updateEmergencyAlertTextView(mEasData.getEasText());
                    mEasData.setRemainTime(--remainTime);
                    mHandler.postDelayed(this, 1000);
                } else {
                    mOverlayView.dismissEmergencyAlertTextView();
                    mEasData.setEasText("");
                    if (mEasData.isInDetailChannel()) {
                        int majorNumber = mEasData.getOriginalMajorNumber();
                        int minorNumber = mEasData.getOriginalMinorNumber();
                        Log.d(TAG, "mEasDisplayTextRunnable: tune to original channel " + majorNumber + "-" +
                                minorNumber);
                        mBroadcastType.easTune(majorNumber, minorNumber);
                        mEasData.setIsInDetailChannel(false);
                    }
                    notifyEasTextShown(false);
                }
            }
        };

        TunerSessionImpl(Context context, String inputId) {
            super(context, inputId);
            mCaptioningManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
            mTvCommonManager = TvCommonManager.getInstance();
            mTvChannelManager = TvChannelManager.getInstance();
            mTvCiManager = TvCiManager.getInstance();
            mCurrentSource = mTvCommonManager.getCurrentTvInputSource();
            mChannelInfoEventListener = new ChannelInfoEventListener();
            mEpgEventListener = new EpgEventListener();
            mCiEventListener = new CiEventListener();
            mEmergencyAlertEventListener = new EmergencyAlertEventListener();
            mCiStatusChangeEventListener = new CiStatusChangeEventListener();
            mCiAsyncCmdNotifyListener = new CiAsyncCmdNotifyListener();
            mCiUiEventListener = new CiUiEventListener();
            mDialogEventListener = new DialogEventListener();
            mTvChannelManager.registerOnChannelInfoEventListener(mChannelInfoEventListener);
            mTvChannelManager.registerOnEpgEventListener(mEpgEventListener);
            mTvChannelManager.registerOnCiEventListener(mCiEventListener);
            mTvChannelManager.registerOnEmergencyAlertEventListener(mEmergencyAlertEventListener);
            mTvCiManager.registerOnCiStatusChangeEventListener(mCiStatusChangeEventListener);
            mTvCiManager.registerOnCiAsyncCmdNotifyListener(mCiAsyncCmdNotifyListener);
            mTvCiManager.registerOnUiEventListener(mCiUiEventListener);
            mTvCommonManager.registerOnDialogEventListener(mDialogEventListener);
            mPVRCtrlUnit = PVRControlUnit.getInstance(context);
            if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                if (mPVRCtrlUnit.start()) {
                    if (DEBUG) Log.d(TAG, "start success");
                    notifyTimeShiftStatusChanged(TvInputManager.TIME_SHIFT_STATUS_AVAILABLE);
                } else {
                    if (DEBUG) Log.d(TAG, "start fail");
                    notifyTimeShiftStatusChanged(TvInputManager.TIME_SHIFT_STATUS_UNAVAILABLE);
                }
            } else {
                notifyTimeShiftStatusChanged(TvInputManager.TIME_SHIFT_STATUS_UNSUPPORTED);
            }
            registerReceiver();
            mTvProgramInfo = new TvProgramInfo();
            mEasData = new EasData();
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case Constant.CMD_UPDATE_PROGRAM:
                            notifyTracksInfo();
                            insertCurrentProgramInfoToTvProvider();
                            checkRatingBlocked();
                            break;
                        case Constant.CMD_SIGNAL_LOCK:
                            signalLockHandler();
                            removeCallbacks(mSignalLockRunnable);
                            postDelayed(mSignalLockRunnable, SIGNAL_LOCK_DELAY_MS);
                            break;
                        case Constant.CMD_SIGNAL_UNLOCK:
                            signalUnlockHandler();
                            break;
                        case Constant.CMD_SCREEN_SAVER:
                            if (WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_MAIN) {
                                final int status = msg.arg1;
                                if (mScreenSaverMode != status) {
                                    screenSaverHandler(status);
                                    if (WindowControl.getWindowData(mInputId).getSource() == TvCommonManager.INPUT_SOURCE_ATV) {
                                        checkRatingBlocked();
                                    }
                                }
                            }
                            break;
                        case Constant.CMD_UPDATE_EPG:
                            insertCurrentProgramInfoToTvProvider();
                            handleEpgUpdate();
                            break;
                        case Constant.CMD_UPDATE_RATING:
                            insertCurrentProgramInfoToTvProvider();
                            checkRatingBlocked();
                            notifyRatingChange();
                            break;
                        case Constant.CMD_HANDLE_EMERGENCY_ALERT:
                            emergencyAlertHandler(msg.arg1);
                            break;
                        default:
                            break;
                    }
                }
            };
        }

        @Override
        public void onRelease() {
            mHandler.removeCallbacks(mSignalLockRunnable);
            mHandler.removeCallbacks(mEasDisplayTextRunnable);
            mHandler.removeMessages(Constant.CMD_UPDATE_EPG);
            mTvChannelManager.unregisterOnChannelInfoEventListener(mChannelInfoEventListener);
            mTvChannelManager.unregisterOnEpgEventListener(mEpgEventListener);
            mTvChannelManager.unregisterOnCiEventListener(mCiEventListener);
            mTvChannelManager.unregisterOnEmergencyAlertEventListener(mEmergencyAlertEventListener);
            mTvCiManager.unregisterOnCiStatusChangeEventListener(mCiStatusChangeEventListener);
            mTvCiManager.unregisterOnCiAsyncCmdNotifyListener(mCiAsyncCmdNotifyListener);
            mTvCiManager.unregisterOnUiEventListener(mCiUiEventListener);
            mTvCommonManager.unregisterOnDialogEventListener(mDialogEventListener);
            mChannelInfoEventListener = null;
            mEpgEventListener = null;
            mCiEventListener = null;
            mEmergencyAlertEventListener = null;
            mCiStatusChangeEventListener = null;
            mCiAsyncCmdNotifyListener = null;
            mCiUiEventListener = null;
            mDialogEventListener = null;
            mContext.unregisterReceiver(mReceiver);
            if (mTuneTask != null) {
                mTuneTask.cancel(true);
                mTuneTask = null;
            }
            if (mEpgUpdateTask != null) {
                mEpgUpdateTask.cancel(true);
                mEpgUpdateTask = null;
            }
            if (mChannelUpdateTask != null) {
                mChannelUpdateTask.cancel(true);
                mChannelUpdateTask = null;
            }
            if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                mPVRCtrlUnit.stop();
                mPVRCtrlUnit = null;
            }
            super.onRelease();
        }

        @Override
        public boolean onTune(Uri channelUri) {
            Log.d(TAG, "onTune: channelUri = " + channelUri +
                    ", mWidth = " + mWidth +
                    ", mHeight = " + mHeight);
            if (ContentUris.parseId(channelUri) < 0 ||
                    channelUri.equals(mTvProgramInfo.getChannelUri())) {
                if (DEBUG) Log.d(TAG, "onTune: ContentUris.parseId(channelUri) < 0 || channelUri.equals(mTvProgramInfo.getChannelUri()), return");
                return false;
            }
            return tune(0, 0, mWidth, mHeight, mDeviceId, channelUri);
        }

        @Override
        public void onSetCaptionEnabled(boolean enabled) {
            if (DEBUG) Log.d(TAG, "onSetCaptionEnabled(" + enabled + ")");
            if (enabled) {
                String fontFamily = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ACCESSIBILITY_CAPTIONING_TYPEFACE);
                mBroadcastType.setCaptionEnabled(mCaptioningManager, fontFamily);
            } else {
                mBroadcastType.setCaptionEnabled(false, 0xFF);
                notifyTrackSelected(TvTrackInfo.TYPE_SUBTITLE, null);
            }
        }

        @Override
        public void onUnblockContent(TvContentRating unblockedRating) {
            Log.d(TAG, "onUnblockContent");
            notifyContentAllowed();
        }

        @Override
        public boolean onSelectTrack(int type, String trackId) {
            Log.d(TAG, "onSelectTrack: type = " + type + ", trackId = " + trackId);
            int index = TunerUtility.parseIndexFromTrackId(trackId);
            switch (type) {
                case TvTrackInfo.TYPE_AUDIO:
                    if (index >= 0) {
                        mTvChannelManager.switchAudioTrack(index);
                        notifyTrackSelected(TvTrackInfo.TYPE_AUDIO, trackId);
                    }
                    break;
                case TvTrackInfo.TYPE_SUBTITLE:
                    if (index >= 0) {
                        mBroadcastType.setCaptionEnabled(true, index);
                        notifyTrackSelected(TvTrackInfo.TYPE_SUBTITLE, trackId);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }

        protected void addAudioTrackInfo() {
            if (WindowControl.getWindowData(mInputId).getSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                List<TvTrackInfo> track = TunerUtility.getDtvAudioTrackInfo(mContext);
                if (track != null) {
                    mTrackInfoList.addAll(track);
                }
            }
        }

        protected void addSubtitleTrackInfo() {
           if (WindowControl.getWindowData(mInputId).getSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                List<TvTrackInfo> track = mBroadcastType.getDtvSubtitleTrackInfo();
                if (track != null) {
                    mTrackInfoList.addAll(track);
                }
           }
        }

        public void selectDefaultAudioTracks() {
           if (WindowControl.getWindowData(mInputId).getSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                int currentAudioIndex = TunerUtility.getCurrentAudioTrackIndex();
                if (currentAudioIndex >= 0) {
                    notifyTrackSelected(TvTrackInfo.TYPE_AUDIO,
                            Utility.buildTrackId(TvTrackInfo.TYPE_AUDIO, currentAudioIndex));
                }
            }
        }

        public void selectDefaultSubtitleTracks() {
            if (WindowControl.getWindowData(mInputId).getSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                int currentSubtitleIndex = mBroadcastType.getCurrentSubtitleTrackIndex();
                if (currentSubtitleIndex >= 0) {
                    notifyTrackSelected(TvTrackInfo.TYPE_SUBTITLE,
                            Utility.buildTrackId(TvTrackInfo.TYPE_SUBTITLE, currentSubtitleIndex));
                }
            }
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_MAIN &&
                    WindowControl.getWindowData(mInputId).getSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                if (mBroadcastType.sendMheg5Key(keyCode)) {
                    if (DEBUG) Log.d(TAG, "onKeyDown: sendMhegKey success!");
                    mHandleOnKeyDown = true;
                    return true;
                } else if (mBroadcastType.sendHbbtvKey(keyCode)) {
                    if (DEBUG) Log.d(TAG, "onKeyDown: sendHbbtvKey success!");
                    mHandleOnKeyDown = true;
                    return true;
                } else if (mBroadcastType.sendGingaKey(keyCode, event)) {
                    if (DEBUG) Log.d(TAG, "onKeyDown: sendGingaKey success!");
                    mHandleOnKeyDown = true;
                    return true;
                }
                switch (keyCode) {
                    case MKeyEvent.KEYCODE_MSTAR_CLOCK: {
                        if ((TvChannelManager.getInstance().hasTeletextClockSignal())
                                && (specificSourceIsInUse(TvCommonManager.INPUT_SOURCE_DTV)
                                        || specificSourceIsInUse(TvCommonManager.INPUT_SOURCE_ATV)
                                        || currentInputSourceIs("CVBS")
                                        || currentInputSourceIs("SVIDEO")
                                        || currentInputSourceIs("SCART"))) {
                            Intent intent = new Intent(mContext, TeletextActivity.class);
                            intent.putExtra("TTX_MODE_CLOCK", true);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            List<ResolveInfo> resolveInfo = mContext.getPackageManager().queryIntentActivities(intent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            if (resolveInfo.size() > 0) {
                                mContext.startActivity(intent);
                            }
                            return true;
                        }
                        break;
                    }
                    case MKeyEvent.KEYCODE_TTX: {
                        boolean bIsTtxChannel = TvChannelManager.getInstance().isTtxChannel();
                        // check if hasTeletextSignal
                        if (bIsTtxChannel
                                && (specificSourceIsInUse(TvCommonManager.INPUT_SOURCE_DTV)
                                        || specificSourceIsInUse(TvCommonManager.INPUT_SOURCE_ATV)
                                        || currentInputSourceIs("CVBS")
                                        || currentInputSourceIs("SVIDEO") || currentInputSourceIs("SCART"))) {
                            Intent intent = new Intent(mContext, TeletextActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            List<ResolveInfo> resolveInfo = mContext.getPackageManager().queryIntentActivities(intent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            if (resolveInfo.size() > 0) {
                                mContext.startActivity(intent);
                            }
                            return true;
                        }
                        break;
                    }
                }
            }
            mHandleOnKeyDown = super.onKeyDown(keyCode, event);
            return mHandleOnKeyDown;
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (mHandleOnKeyDown == true) {
                mHandleOnKeyDown = false;
                return true;
            }
            return super.onKeyUp(keyCode, event);
        }

        private boolean specificSourceIsInUse(int source) {
            int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            if (currInputSource != source) {
                return false;
            } else {
                return true;
            }
        }

        private boolean currentInputSourceIs(final String source) {
            int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            if (source.equals("CVBS")) {
                if (currInputSource >= TvCommonManager.INPUT_SOURCE_CVBS
                        && currInputSource <= TvCommonManager.INPUT_SOURCE_CVBS_MAX)
                    return true;
            } else if (source.equals("SVIDEO")) {
                if (currInputSource >= TvCommonManager.INPUT_SOURCE_SVIDEO
                        && currInputSource <= TvCommonManager.INPUT_SOURCE_SVIDEO_MAX)
                    return true;
            } else if (source.equals("SCART")) {
                if (currInputSource >= TvCommonManager.INPUT_SOURCE_SCART
                        && currInputSource <= TvCommonManager.INPUT_SOURCE_SCART_MAX)
                    return true;
            }
            return false;
        }

        @Override
        public boolean tune(int left, int top, int width, int height, int source, Uri channelUri) {
            if (mEpgUpdateTask != null) {
                mEpgUpdateTask.cancel(true);
                mEpgUpdateTask = null;
            }
            if (mTuneTask != null) {
                mTuneTask.cancel(true);
                mTuneTask = null;
            }
            mTvProgramInfo.reset();
            mTvProgramInfo.setChannelUri(channelUri);
            mScreenSaverMode = Constant.SCREENSAVER_DEFAULT_MODE;
            WindowData windowData = new WindowData.Builder()
                    .setInputId(mInputId)
                    .setSource(TvCommonManager.INPUT_SOURCE_NONE)
                    .setLeft(left).setTop(top)
                    .setWidth(width).setHeight(height).setListener(
                    new TvInputEventListener() {
                        public void onEvent() {
                            setSurfaceToHardware();
                        }
                    }).build();
            mTuneTask = new TuneTask(channelUri, windowData);
            mTuneTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return true;
        }

        private void registerReceiver() {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(TvInputManager.ACTION_PARENTAL_CONTROLS_ENABLED_CHANGED)) {
                        Log.d(TAG, "Receive ACTION_PARENTAL_CONTROLS_ENABLED_CHANGED");
                        if (mTvInputManager.isParentalControlsEnabled()) {
                            checkRatingBlocked();
                        } else {
                            notifyContentAllowed();
                        }
                    } else if (intent.getAction().equals(TvInputManager.ACTION_BLOCKED_RATINGS_CHANGED)) {
                        Log.d(TAG, "Receive ACTION_BLOCKED_RATINGS_CHANGED");
                        checkRatingBlocked();
                    } else if (intent.getAction().equals(TvIntent.ACTION_CHANNEL_HAS_UPDATED)) {
                        Log.d(TAG, "Receive ACTION_CHANNEL_HAS_UPDATED");
                        mTvProgramInfo.reset();
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(TvInputManager.ACTION_BLOCKED_RATINGS_CHANGED);
            filter.addAction(TvInputManager.ACTION_PARENTAL_CONTROLS_ENABLED_CHANGED);
            filter.addAction(TvIntent.ACTION_CHANNEL_HAS_UPDATED);
            mContext.registerReceiver(mReceiver, filter);
        }

        private void checkRatingBlocked() {
            if (mTvInputManager.isParentalControlsEnabled()) {
                if (mTvProgramInfo.getCurrentTifProgramInfo() != null &&
                        mTvProgramInfo.getCurrentTifProgramInfo().contentRatings != null) {
                    TvContentRating[] ratings = mTvProgramInfo.getCurrentTifProgramInfo().contentRatings;
                    for (int i = 0; i < ratings.length; i++) {
                        if (mTvInputManager.isRatingBlocked(ratings[i])) {
                            Log.d(TAG, "checkRatingBlocked: parental control is enabled and current rating is blocked (" +
                                    ratings[i].flattenToString() + ")");
                            notifyContentBlocked(ratings[i]);
                            return;
                        }
                    }
                    notifyContentAllowed();
                } else {
                    notifyContentAllowed();
                }
            }
        }

        private void insertCurrentProgramInfoToTvProvider() {
            ContentResolver resolver = mContext.getContentResolver();
            if (mTvProgramInfo.getChannelUri() == null) {
                 Log.e(TAG, "insertCurrentProgramInfoToTvProvider: channelUri == null, return");
                 return;
            }
            mTvProgramInfo.setCurrentTifProgramInfo(mBroadcastType.getCurrentTifProgramInfo(
                    mContext, ContentUris.parseId(mTvProgramInfo.getChannelUri())));
            if (mTvProgramInfo.getCurrentTifProgramInfo().isEmpty() ||
                    !mTvProgramInfo.getCurrentTifProgramInfo().isValid()) {
                Log.e(TAG, "insertCurrentProgramInfoToTvProvider: currentTifProgramInfo is empty or invalid, return");
                return;
            }
            Bundle extras = new Bundle();
            extras.putBoolean(Constant.SESSION_EVENT_CLOSED_CAPTION_STATUS, mTvProgramInfo.getCurrentTifProgramInfo().ccService);
            notifySessionEvent(Constant.SESSION_EVENT_CLOSED_CAPTION, extras);
            ContentValues values = TunerUtility.putProgramInfoToContentValues(mTvProgramInfo.getCurrentTifProgramInfo());
            Uri currentProgramUri =
                    TvContract.buildProgramsUriForChannel(mTvProgramInfo.getChannelUri(),
                    mTvProgramInfo.getCurrentTifProgramInfo().startTime,
                    mTvProgramInfo.getCurrentTifProgramInfo().endTime);
            String[] projection = { TvContract.Programs._ID };
            Cursor cursor = resolver.query(currentProgramUri, projection, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    Log.d(TAG, "insertCurrentProgramInfoToTvProvider: update uri " + currentProgramUri);
                    resolver.update(TvContract.buildProgramUri(cursor.getLong(0)), values, null, null);
                } else {
                    Log.d(TAG, "insertCurrentProgramInfoToTvProvider: insert channelId = " + mTvProgramInfo.getCurrentTifProgramInfo().channelId +
                            ", startTime = " + mTvProgramInfo.getCurrentTifProgramInfo().startTime +
                            ", endTime = " + mTvProgramInfo.getCurrentTifProgramInfo().endTime);
                    mTvProgramInfo.setCurrentProgramUri(resolver.insert(TvContract.Programs.CONTENT_URI, values));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        private void handleEpgUpdate() {
            long currentTime = TvTimerManager.getInstance().getCurrentTvTime().toMillis(false);
            if ((mEpgUpdateTask != null && mEpgUpdateTask.getStatus() != AsyncTask.Status.FINISHED) ||
                    currentTime - mTvProgramInfo.getLastUpdateEpgRunMs() < EPG_UPDATE_PERIOD_MS) {
                if (DEBUG) Log.d(TAG, "sendEmptyMessageDelayed for CMD_UPDATE_EPG, return");
                mHandler.sendEmptyMessageDelayed(Constant.CMD_UPDATE_EPG, EPG_UPDATE_DELAY_MS);
                return;
            }
            if (DEBUG) Log.d(TAG, "start EpgUpdateTask");
            mEpgUpdateTask = null;
            mEpgUpdateTask = new EpgUpdateTask(mContext, mInputId, mTvProgramInfo);
            mEpgUpdateTask.execute();
        }

        private void notifyRatingChange() {
            if (mTvProgramInfo.getCurrentTifProgramInfo() != null &&
                    mTvProgramInfo.getCurrentTifProgramInfo().contentRatings != null) {
                TvContentRating[] contentRatings = mTvProgramInfo.getCurrentTifProgramInfo().contentRatings;
                final String DELIMITER = ",";
                StringBuilder ratings = new StringBuilder(contentRatings[0].flattenToString());
                for (int i = 1; i < contentRatings.length; ++i) {
                    ratings.append(DELIMITER);
                    ratings.append(contentRatings[i].flattenToString());
                }
                Bundle extras = new Bundle();
                extras.putString(Constant.SESSION_EVENT_KEY_RATING, ratings.toString());
                notifySessionEvent(Constant.SESSION_EVENT_RATING_CHANGE, extras);
            }
        }

        private void emergencyAlertHandler(int type) {
            Log.d(TAG, "emergencyAlertHandler: type = " + type);
            MwAtscEasInfo easInfo = mBroadcastType.getEasInfo();
            if (easInfo == null) {
                return;
            }
            StringBuffer sb = new StringBuffer();
            String text;
            switch (type) {
                case TvAtscChannelManager.EAS_TEXT_ONLY:
                    if (mEasData.isInDetailChannel()) {
                        int majorNumber = mEasData.getOriginalMajorNumber();
                        int minorNumber = mEasData.getOriginalMinorNumber();
                        Log.d(TAG, "emergencyAlertHandler: tune to original channel " + majorNumber + "-" +
                                minorNumber);
                        mBroadcastType.easTune(majorNumber, minorNumber);
                        mEasData.setIsInDetailChannel(false);
                    }
                    mEasData.setRemainTime(EAS_DISPLAY_TIME_DEFAULT);
                    mHandler.removeCallbacks(mEasDisplayTextRunnable);
                    mHandler.post(mEasDisplayTextRunnable);
                    notifyEasTextShown(true);
                    for (int i = 0; i < easInfo.au8AlertText.length; i++) {
                        if (easInfo.au8AlertText[i] > 0) {
                            sb.append((char) easInfo.au8AlertText[i]);
                        }
                    }
                    if (easInfo.au8AlertText != null && easInfo.au8AlertText[0] != 0) {
                        text = sb.toString();
                    } else {
                        text = "";
                    }
                    mEasData.setEasText(text);
                    mOverlayView.updateEmergencyAlertTextView(text);
                    break;
                case TvAtscChannelManager.EAS_DETAIL_CHANNEL:
                    if (mEasData.isInDetailChannel()) {
                        Log.d(TAG, "emergencyAlertHandler: now is in detail channel");
                    } else {
                        ProgramInfo progInfo = mBroadcastType.getCurrentProgramInfo();
                        mEasData.setOriginalMajorNumber(progInfo.majorNum);
                        mEasData.setOriginalMinorNumber(progInfo.minorNum);
                        Log.d(TAG, "emergencyAlertHandler: tune to detail channel " + easInfo.u16DetailsMajorNum + "-" +
                                easInfo.u16DetailsMinorNum);
                        mBroadcastType.easTune(easInfo.u16DetailsMajorNum, easInfo.u16DetailsMinorNum);
                        mEasData.setIsInDetailChannel(true);
                    }
                    int remainTime = easInfo.u8AlertTimeRemain;
                    if (remainTime < EAS_DISPLAY_TIME_CHECK) {
                        remainTime += EAS_DISPLAY_TIME_EXTEND;
                    }
                    mEasData.setRemainTime(remainTime);
                    mHandler.removeCallbacks(mEasDisplayTextRunnable);
                    mHandler.post(mEasDisplayTextRunnable);
                    notifyEasTextShown(true);
                    for (int i = 0; i < easInfo.au8AlertText.length; i++) {
                        if (easInfo.au8AlertText[i] > 0) {
                            sb.append((char) easInfo.au8AlertText[i]);
                        }
                    }
                    if (easInfo.au8AlertText != null && easInfo.au8AlertText[0] != 0) {
                        text = sb.toString();
                    } else {
                        text = "";
                    }
                    mEasData.setEasText(text);
                    mOverlayView.updateEmergencyAlertTextView(text);
                    break;
                case TvAtscChannelManager.EAS_STOP_TEXT_SCORLL:
                    if (mOverlayView != null) {
                        mOverlayView.stopScrollEmergencyAlertTextView();
                    }
                    break;
                case TvAtscChannelManager.EAS_UPDATE_TIME_REMAINING:
                    if (DEBUG) Log.d(TAG, "emergencyAlertHandler: update remain time = " + easInfo.u8AlertTimeRemain);
                    mEasData.setRemainTime(easInfo.u8AlertTimeRemain);
                    mHandler.removeCallbacks(mEasDisplayTextRunnable);
                    mHandler.post(mEasDisplayTextRunnable);
                    break;
            }
        }

        private void notifyEasTextShown(boolean isShown) {
            Bundle extras = new Bundle();
            extras.putBoolean(Constant.SESSION_EVENT_KEY_IS_EAS_TEXT_SHOWN, isShown);
            notifySessionEvent(Constant.SESSION_EVENT_EAS_TEXT_SHOWN, extras);
        }

        @Override
        public void onTimeShiftPause() {
            if (DEBUG) Log.d(TAG, "onTimeShiftPause");
            if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                mPVRCtrlUnit.pause();
            }
        }

        @Override
        public void onTimeShiftResume() {
            if (DEBUG) Log.d(TAG, "onTimeShiftResume");
            if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                mPVRCtrlUnit.play();
            }
        }

        @Override
        public void onTimeShiftSeekTo(long timeMs) {
            if (DEBUG)Log.d(TAG, "onTimeShiftSeekTo, timeMs=" + timeMs);
            if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                mPVRCtrlUnit.seekTo(timeMs);
            }
        }

        @Override
        public void onTimeShiftSetPlaybackParams(PlaybackParams params) {
            if (DEBUG) Log.d(TAG, "onTimeShiftSetPlaybackParams");
            if (null != params) {
                if (DEBUG) Log.d(TAG, "onTimeShiftSetPlaybackParams, params.getSpeed()" + (float)params.getSpeed());
                if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                    mPVRCtrlUnit.setPlaybackSpeed((float)params.getSpeed());
                }
            }
        }

        @Override
        public long onTimeShiftGetStartPosition() {
            if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                long sp = mPVRCtrlUnit.getStartPosition();
                if (DEBUG) Log.d(TAG, "onTimeShiftGetStartPosition:" + sp);
                return sp;
            }
            return TvInputManager.TIME_SHIFT_INVALID_TIME;
        }

        @Override
        public long onTimeShiftGetCurrentPosition() {
            if (mPVRCtrlUnit != null && mPVRCtrlUnit.isPVRsupported()) {
                long cp = mPVRCtrlUnit.getCurrentPosition();
                if (DEBUG) Log.d(TAG, "onTimeShiftGetCurrentPosition:" + cp);
                return cp;
            }
            return TvInputManager.TIME_SHIFT_INVALID_TIME;
        }

        private class TuneTask extends AsyncTask<Void, Void, Void> {
            private Uri mChannelUri;
            private WindowData mWindowData;

            public TuneTask(Uri channelUri, WindowData windowData) {
                mChannelUri = channelUri;
                mWindowData = windowData;
            }

            @Override
            protected Void doInBackground(Void... arg) {
                if (isCancelled()) {
                    if (DEBUG) Log.d(TAG, "TuneTask: TuneTask has been canceled!!!");
                    return null;
                }
                String[] projection = { TvContract.Channels.COLUMN_TYPE,
                        TvContract.Channels.COLUMN_DISPLAY_NUMBER,
                        TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA };
                String channelType = TvContract.Channels.TYPE_OTHER;
                String displayNumber = null;
                byte[] bytes = null;
                Cursor cursor = mContext.getContentResolver().query(mChannelUri, projection, null, null, null);
                try {
                    if (cursor != null && cursor.moveToNext()) {
                        channelType = cursor.getString(cursor.getColumnIndex(TvContract.Channels.COLUMN_TYPE));
                        displayNumber = cursor.getString(cursor.getColumnIndex(TvContract.Channels.COLUMN_DISPLAY_NUMBER));
                        bytes = cursor.getBlob(cursor.getColumnIndex(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA));
                    } else {
                        Log.e(TAG, "TuneTask: query cursor fail!!!");
                        return null;
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING);
                int serviceType = TvChannelManager.SERVICE_TYPE_INVALID;
                int[] values = Utility.byteArrayToInts(bytes);
                if (values != null && values.length > 1) {
                    serviceType = values[1];
                }
                int source = serviceType == TvChannelManager.SERVICE_TYPE_ATV ?
                        TvCommonManager.INPUT_SOURCE_ATV : TvCommonManager.INPUT_SOURCE_DTV;
                if (isCancelled()) {
                    if (DEBUG) Log.d(TAG, "TuneTask: TuneTask has been canceled!!!");
                    return null;
                }
                mWindowData.setSource(source);
                if (WindowControl.setWindowData(mWindowData)) {
                    boolean isSub = WindowControl.getWindowType(mInputId) == WindowControl.WINDOW_TYPE_SUB ? true : false;
                    if (isSub) {
                        Log.d(TAG, "TuneTask: unregisterListener");
                        unregisterListener();
                    }
                    if (WindowControl.run()) {
                        if (DEBUG) {
                            Log.d(TAG, "TuneTask: tune source = " + source + ", displayNumber = " + displayNumber +
                                    ", serviceType = " + serviceType);
                        }
                        if (isCancelled()) {
                            if (DEBUG) Log.d(TAG, "TuneTask: TuneTask has been canceled!!!");
                            return null;
                        }
                        if (mCurrentSource == source &&
                                mBroadcastType.isSameAsCurrentChannel(displayNumber, serviceType)) {
                            Log.d(TAG, "TuneTask: not tune");
                        } else {
                            if (isSub) {
                                TvPipPopManager.getInstance().setPipDisplayFocusWindow(TvPipPopManager.E_SUB_WINDOW);
                            }
                            mCurrentSource = source;
                            BroadcastType.getInstance().tune(
                                    source,
                                    displayNumber,
                                    serviceType);
                            if (isSub) {
                                TvPipPopManager.getInstance().setPipDisplayFocusWindow(TvPipPopManager.E_MAIN_WINDOW);
                            }
                        }
                    }
                }
                notifyVideoAvailable();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mTuneTask = null;
                if (mCurrentSource == TvCommonManager.INPUT_SOURCE_ATV) {
                    TunerUtility.updateCurrentAtvChannelInfo(mContext, mChannelUri);
                }
            }
        }

        private static class EpgUpdateTask extends AsyncTask<Void, Void, Void> {
            WeakReference<Context> mContext;
            String mInputId;
            WeakReference<TvProgramInfo> mTvProgramInfo;

            public EpgUpdateTask(Context context, String inputId, TvProgramInfo tvProgramInfo) {
                mContext = new WeakReference<>(context);
                mInputId = inputId;
                mTvProgramInfo= new WeakReference<>(tvProgramInfo);
            }

            @Override
            protected Void doInBackground(Void... params) {
                insertEpgInfoToTvProvider();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (mTvProgramInfo != null) {
                    mTvProgramInfo.get().setLastUpdateEpgRunMs();
                }
            }

            private void insertEpgInfoToTvProvider() {
                if (isCancelled()) {
                    if (DEBUG) Log.d(TAG, "insertEpgInfoToTvProvider: EpgUpdateTask has been canceled!!!");
                    return;
                }
                ContentResolver resolver = mContext.get().getContentResolver();
                Uri uri = TvContract.buildChannelsUriForInput(mInputId);
                String[] projection = { TvContract.Channels._ID,
                        TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA };
                Cursor cursor = resolver.query(uri, projection, null, null, null);
                try {
                    while (!isCancelled() && cursor != null && cursor.moveToNext()) {
                        long channelId = cursor.getLong(cursor.getColumnIndex(TvContract.Channels._ID));
                        byte[] bytes = cursor.getBlob(cursor.getColumnIndex(
                                TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA));
                        int[] values = Utility.byteArrayToInts(bytes);
                        if (values == null || values.length == 0) {
                            continue;
                        }
                        int indexOfProgramInfoList = values[0];
                        ProgramInfo programInfo = BroadcastType.getInstance().
                                getProgramInfoByIndex(indexOfProgramInfoList);
                        ArrayList<TifProgramInfo> tifProgInfoList = BroadcastType.getInstance().
                                buildTifProgramInfoList(mContext.get(), programInfo, channelId);
                        if (tifProgInfoList != null && tifProgInfoList.size() > 0) {
                            List<ContentValues> contentList = new ArrayList<ContentValues>();
                            long currentTime = TvTimerManager.getInstance().getCurrentTvTime().toMillis(false);
                            for (TifProgramInfo tifProgramInfo : tifProgInfoList) {
                                if (tifProgramInfo.startTime > currentTime || tifProgramInfo.endTime < currentTime) {
                                    contentList.add(TunerUtility.putProgramInfoToContentValues(tifProgramInfo));
                                }
                            }
                            TunerUtility.deleteProgramsByChannelExceptCurrentProgram(mContext.get(), TvContract.buildChannelUri(channelId), currentTime);
                            resolver.bulkInsert(TvContract.Programs.CONTENT_URI, contentList.toArray(new ContentValues[contentList.size()]));
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }

        private class TvProgramInfo {
            private Uri mChannelUri;
            private Uri mCurrentProgramUri;
            private TifProgramInfo mCurrentTifProgramInfo;
            private long mLastUpdateEpgRunMs;

            public TvProgramInfo() {
                mChannelUri = null;
                mCurrentProgramUri = null;
                mCurrentTifProgramInfo = null;
                mLastUpdateEpgRunMs = 0;
            }

            public void reset() {
                mChannelUri = null;
                mCurrentProgramUri = null;
                mCurrentTifProgramInfo = null;
                mLastUpdateEpgRunMs = 0;
            }

            public Uri getChannelUri() {
                return mChannelUri;
            }

            public void setChannelUri(Uri channelUri) {
                mChannelUri = channelUri;
            }

            public Uri getCurrentProgramUri() {
                return mCurrentProgramUri;
            }

            public void setCurrentProgramUri(Uri currentProgramUri) {
                mCurrentProgramUri = currentProgramUri;
            }

            public TifProgramInfo getCurrentTifProgramInfo() {
                return mCurrentTifProgramInfo;
            }

            public void setCurrentTifProgramInfo(TifProgramInfo tifProgramInfo) {
                mCurrentTifProgramInfo = tifProgramInfo;
            }

            public long getLastUpdateEpgRunMs() {
                return mLastUpdateEpgRunMs;
            }

            public void setLastUpdateEpgRunMs() {
                mLastUpdateEpgRunMs = TvTimerManager.getInstance().getCurrentTvTime().toMillis(false);
                if (DEBUG) Log.d(TAG, "set mLastUpdateEpgRunMs = " + mLastUpdateEpgRunMs);
            }
        }

        private class EasData {
            private boolean mIsInDetailChannel = false;
            private int mRemainTime = 0;
            private int mOriginalMajorNumber = -1;
            private int mOriginalMinorNumber = -1;
            private String mText = "";

            public boolean isInDetailChannel() {
                return mIsInDetailChannel;
            }

            public void setIsInDetailChannel(boolean b) {
                mIsInDetailChannel = b;
            }

            public int getRemainTime() {
                return mRemainTime;
            }

            public void setRemainTime(int remainTime) {
                mRemainTime = remainTime;
            }

            public int getOriginalMajorNumber() {
                return mOriginalMajorNumber;
            }

            public void setOriginalMajorNumber(int majorNumber) {
                mOriginalMajorNumber = majorNumber;
            }

            public int getOriginalMinorNumber() {
                return mOriginalMinorNumber;
            }

            public void setOriginalMinorNumber(int minorNumber) {
                mOriginalMinorNumber = minorNumber;
            }

            public String getEasText() {
                return mText;
            }

            public void setEasText(String text) {
                mText = text;
            }
        }

        private class ChannelInfoEventListener implements OnChannelInfoEventListener {
            @Override
            public boolean onChannelInfoEvent(int what, int arg1, int arg2, Object obj) {
                if (what == TvChannelManager.TVPLAYER_DTV_CHANNEL_NAME_READY) {
                    if (DEBUG) Log.d(TAG, "onChannelInfoEvent: TVPLAYER_DTV_CHANNEL_NAME_READY");
                    mHandler.removeMessages(Constant.CMD_UPDATE_PROGRAM);
                    mHandler.sendEmptyMessage(Constant.CMD_UPDATE_PROGRAM);
                    return true;
                } else if (what == TvChannelManager.TVPLAYER_TS_CHANGE) {
                    if (DEBUG) Log.d(TAG, "onChannelInfoEvent: TVPLAYER_TS_CHANGE");
                    if (mEpgUpdateTask != null) {
                        mEpgUpdateTask.cancel(true);
                        mEpgUpdateTask = null;
                    }
                    if (mChannelUpdateTask != null) {
                        mChannelUpdateTask.cancel(true);
                        mChannelUpdateTask = null;
                    }
                    mChannelUpdateTask = new ChannelUpdateTask(mContext, mInputId, false);
                    mChannelUpdateTask.execute();
                    return true;
                }
                return false;
            }

            @Override
            public boolean onAtvProgramInfoReady(int what, int arg1, int arg2, Object obj) {
                if (what == TvChannelManager.TVPLAYER_PROGRAM_INFO_READY) {
                    if (DEBUG) Log.d(TAG, "onAtvProgramInfoReady: TVPLAYER_PROGRAM_INFO_READY");
                    mHandler.removeMessages(Constant.CMD_UPDATE_PROGRAM);
                    mHandler.sendEmptyMessage(Constant.CMD_UPDATE_PROGRAM);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onDtvProgramInfoReady(int what, int arg1, int arg2, Object obj) {
                if (what == TvChannelManager.TVPLAYER_PROGRAM_INFO_READY) {
                    if (DEBUG) Log.d(TAG, "onDtvProgramInfoReady: TVPLAYER_PROGRAM_INFO_READY");
                    mHandler.removeMessages(Constant.CMD_UPDATE_PROGRAM);
                    mHandler.sendEmptyMessage(Constant.CMD_UPDATE_PROGRAM);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onTvProgramInfoReady(int what, int arg1, int arg2, Object obj) {
                return false;
            }
        }

        private class EpgEventListener implements OnEpgEventListener {
            @Override
            public boolean onEpgEvent(int what, int arg1, int arg2, Object obj) {
                if (what == TvChannelManager.TVPLAYER_EPG_UPDATE ||
                        what == TvChannelManager.TVPLAYER_EPG_UPDATE_LIST) {
                    if (DEBUG) Log.d(TAG, "onEpgEvent: what = " + what);
                    mHandler.removeMessages(Constant.CMD_UPDATE_EPG);
                    mHandler.sendEmptyMessage(Constant.CMD_UPDATE_EPG);
                    return true;
                }
                return false;
            }
        }

        private class DialogEventListener implements OnDialogEventListener {
            @Override
            public boolean onDialogEvent(int what, int arg1, int arg2, Object obj) {
                if (what == TvCommonManager.TV_ATSC_POPUP_DIALOG) {
                    if (DEBUG) Log.d(TAG, "onDialogEvent: TV_ATSC_POPUP_DIALOG");
                    mHandler.removeMessages(Constant.CMD_UPDATE_RATING);
                    if (mTvProgramInfo.getChannelUri() == null) {
                        Log.d(TAG, "onDialogEvent: channelUri == null, sendEmptyMessageDelayed");
                        mHandler.sendEmptyMessageDelayed(Constant.CMD_UPDATE_RATING, PROGRAM_UPDATE_DELAY_MS);
                    } else {
                        mHandler.sendEmptyMessage(Constant.CMD_UPDATE_RATING);
                    }
                    return true;
                }
                return false;
            }
        }

        private class CiEventListener implements OnCiEventListener {
            @Override
            public boolean onCiEvent(int what, int arg1, int arg2, Object obj){
                if (what == TvChannelManager.TVPLAYER_CI_UI_OP_SERVICE_LIST ||
                        what == TvChannelManager.TVPLAYER_CI_UI_OP_EXIT_SERVICE_LIST) {
                    if (DEBUG) Log.d(TAG, "OnCiEvent: "+what);
                    if (mEpgUpdateTask != null) {
                        mEpgUpdateTask.cancel(true);
                        mEpgUpdateTask = null;
                    }
                    if (mChannelUpdateTask != null) {
                        mChannelUpdateTask.cancel(true);
                        mChannelUpdateTask = null;
                    }
                    mChannelUpdateTask = new ChannelUpdateTask(mContext, mInputId, false);
                    mChannelUpdateTask.execute();
                    return true;
                } else if (what == TvChannelManager.TVPLAYER_CI_UI_OP_REFRESH_QUERY) {
                    TvCiManager.getInstance().ciClearOPSearchSuspended();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(mContext.getString(R.string.str_ciplus_op_confirmation_title));
                    builder.setMessage(mContext.getString(R.string.str_ciplus_op_refresh));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            TvCiManager.getInstance().sendCiOpSearchStart(false);
                        }
                    });
                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Do nothing.
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    alert.show();
                }
                return false;
            }
        }

        private class CiStatusChangeEventListener implements OnCiStatusChangeEventListener {
            @Override
            public boolean onCiStatusChanged(int what, int arg1, int arg2) {
                if (DEBUG) Log.i(TAG, "onCiStatusChanged(), what:" + what);
                switch (what) {
                    case TvCiManager.TVCI_STATUS_CHANGE_TUNER_OCCUPIED: {
                        switch (arg1) {
                            case TvCiManager.CI_NOTIFY_CU_IS_PROGRESS: {
                                Toast toast = Toast.makeText(mContext,
                                        R.string.str_cam_upgrade_alarm, Toast.LENGTH_SHORT);
                                toast.show();
                                break;
                            }
                            case TvCiManager.CI_NOTIFY_OP_IS_TUNING: {
                                Toast toast = Toast.makeText(mContext,
                                        R.string.str_op_tuning_alarm, Toast.LENGTH_SHORT);
                                toast.show();
                                break;
                            }
                            default: {
                                if (DEBUG) Log.d(TAG, "Unknown CI occupied status, arg1 = " + arg1);
                                break;
                            }
                        }
                        Intent intent = new Intent("com.mstar.tv.tvplayer.ui.intent.action.CI_PLUS_TUNER_UNAVAIABLE");
                        intent.putExtra("TUNER_AVAILABLE", false);
                        mContext.sendBroadcast(intent);
                        break;
                    }
                }
                return true;
            }
        }

        private class CiAsyncCmdNotifyListener implements OnCiAsyncCmdNotifyListener {
            @Override
            public boolean onCiCmdNotify(int what, int arg1, int arg2) {
                switch (what) {
                    case TvCiManager.TVCI_ASYNC_COMMAND_NOTIFY: {
                        switch (arg1) {
                            case TvCiManager.CI_ASYNC_CMD_SWITCH_ROUTE: {
                                if (TvCommonManager.getInstance().processTvAsyncCommand() == false) {
                                    if (DEBUG) Log.e(TAG, "processTvAsyncCommand return false...");
                                }
                                break;
                            }
                            case TvCiManager.CI_ASYNC_CMD_SWITCH_CI_SLOT: {
                                if (DEBUG) Log.i(TAG, "Reveive CI_ASYNC_CMD_SWITCH_CI_SLOT");
                                break;
                            }
                            default: {
                                if (DEBUG) Log.d(TAG, "Unknown CI async command notify, arg1 = " + arg1);
                                break;
                            }
                        }
                        break;
                    }
                }
                return true;
            }
        }

        private class CiUiEventListener implements OnUiEventListener {
            @Override
            public boolean onUiEvent(int what){
                if (what == TvCiManager.TVCI_UI_DATA_READY) {
                    if (DEBUG) Log.d(TAG, "onUiEvent: TvCiManager.TVCI_UI_DATA_READY");
                    Intent intent = new Intent("com.mstar.tv.intent.action.CIINFO");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    List<ResolveInfo> resolveInfo = mContext.getPackageManager().queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);
                    if (resolveInfo.size() > 0) {
                        mContext.startActivity(intent);
                    }
                    return true;
                } else if (what == TvCiManager.TVCI_UI_CARD_INSERTED) {
                    if (DEBUG) Log.d(TAG, "onUiEvent: TvCiManager.TVCI_UI_CARD_INSERTED");
                    Toast toast = Toast.makeText(mContext,
                            R.string.str_cimmi_hint_ci_inserted, Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                } else if (what == TvCiManager.TVCI_UI_CARD_REMOVED) {
                    if (DEBUG) Log.d(TAG, "onUiEvent: TvCiManager.TVCI_UI_CARD_REMOVED");
                    Toast toast = Toast.makeText(mContext,
                            R.string.str_cimmi_hint_ci_removed, Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                return false;
            }
        }

        private class EmergencyAlertEventListener implements OnEmergencyAlertEventListener {
            @Override
            public boolean onEmergencyAlertEvent(int what, int arg1, int arg2, Object obj) {
                if (what == TvChannelManager.TVPLAYER_EMERGENCY_ALERT) {
                    if (DEBUG) Log.d(TAG, "onDialogEvent: TVPLAYER_EMERGENCY_ALERT");
                    Message m = Message.obtain();
                    m.what = Constant.CMD_HANDLE_EMERGENCY_ALERT;
                    m.arg1 = arg1;
                    mHandler.sendMessage(m);
                    return true;
                }
                return false;
            }
        }
    }
}
