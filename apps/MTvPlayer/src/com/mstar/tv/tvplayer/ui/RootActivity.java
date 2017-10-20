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

package com.mstar.tv.tvplayer.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManagerNative;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.view.Gravity;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.os.Looper;
import android.content.ContentValues;
import java.util.Locale;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvCaManager;
import com.mstar.android.tv.TvCcManager;
import com.mstar.android.tv.TvCecManager.OnCecCtrlEventListener;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager.OnInputSourceEventListener;
import com.mstar.android.tv.TvChannelManager.OnSignalEventListener;
import com.mstar.android.tv.TvChannelManager.OnEwsEventListener;
import com.mstar.android.tv.TvCiManager.OnCiStatusChangeEventListener;
import com.mstar.android.tv.TvCiManager.OnCiAsyncCmdNotifyListener;
import com.mstar.android.tv.TvCiManager.OnUiEventListener;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCommonManager.OnResourceEventListener;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvGingaManager;
import com.mstar.android.tv.TvHbbTVManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tv.TvOadManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvTimerManager.OnEpgTimerEventListener;
import com.mstar.android.tv.TvTimerManager.OnOadTimerEventListener;
import com.mstar.android.tv.TvTimerManager.OnPvrTimerEventListener;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.handler.OnTvCommonEventHandler;
import com.mstar.android.tv.widget.TvView;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.dtv.common.CaManager.OnCaEventListener;
import com.mstar.android.tvapi.dtv.common.CaManager;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.vo.MwAtscEasInfo;
import com.mstar.tv.tvplayer.ui.channel.ChannelControlActivity;
import com.mstar.tv.tvplayer.ui.component.PasswordCheckDialog;
import com.mstar.tv.tvplayer.ui.dtv.CimmiActivity;
import com.mstar.tv.tvplayer.ui.dtv.eas.atsc.EmergencyAlertDialog;
import com.mstar.tv.tvplayer.ui.dtv.oad.OadDownload;
import com.mstar.tv.tvplayer.ui.holder.CaViewHolder;
import com.mstar.tv.tvplayer.ui.MenuConstants;
import com.mstar.tv.tvplayer.ui.pvr.PVRActivity;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.pvr.USBDiskSelecter;
import com.mstar.tv.tvplayer.ui.tuning.AutoTuneOptionActivity;
import com.mstar.tv.tvplayer.ui.tuning.dvb.DvbsDTVAutoTuneOptionActivity;
import com.mstar.tv.tvplayer.ui.tuning.OadScan;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarUIActivity;
import com.mstar.util.Constant.ScreenSaverMode;
import com.mstar.util.Constant.AtscAtvScreenSaverMode;
import com.mstar.util.Constant.SignalProgSyncStatus;
import com.mstar.util.Constant;
import com.mstar.util.Tools;
import com.mstar.util.TvEvent;
import com.mstar.util.Utility;
import com.mstar.android.MIntent;

public class RootActivity extends MstarUIActivity {
    private static final String TAG = "RootActivity";

    // Auto shutdown time (Same behavior with PureSN) 10Mins
    private static final int NO_SIGNAL_SHUTDOWN_TIME = 10 * 60 * 1000;

    private static final int IMAGE_VIEW_ANIMATION_TIME = 500;

    private static final int SCREENSAVER_DEFAULT_STATUS = -1;

    /* OAD confirm dialog timeout, 60 seconds */
    private final int OAD_DOWNLOAD_CONFIRM_TIMEOUT = 60 * 1000;

    private final int CEC_INFO_DISPLAY_TIMEOUT = 1000;

    private final int TTS_DELAY_TIME_700MS = 700;

    private final int ATSC_CLOSE_DIALOG_DELAY_TIME_200MS = 200;

    /*
     * The delay time is used to prevent speaker noise.
     * User may hold the volume key, the volume updated notification will be received repeatedly
     * and then the speaker plays the "bobo" noise. The solution is to add delay time to ignore
     * the repeated/unnecessary TTS playing.
     */
    private final int TTS_VOLUME_UPDATE_DELAY_MS = 100;

    public boolean bCmd_TvApkExit = false;

    private TvView tvView = null;

    private boolean mIsSignalLock = true;

    private static boolean mIsActive = false;

    private static boolean mIsBackKeyPressed = false;

    private volatile int mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;

    private static boolean mIsScreenSaverShown = false;

    private static boolean mIsMsrvStarted = false;

    private static String TV_APK_END = "com.mstar.tv.ui.tvend";

    private final String INTENT_FALCON_BOOT_DONE = "android.intent.action.FALCON_BOOT_DONE";

    private TvCommonManager mTvCommonManager = null;

    private TvPictureManager mTvPictureManager = null;

    private TvS3DManager mTvS3DManager = null;

    private TvChannelManager mTvChannelManager = null;

    private TvMhlManager mTvMhlManager = null;

    private TvTimerManager mTvTimerManager = null;

    private TvCecManager mTvCecManager = null;

    private static boolean isFirstPowerOn = true;

    private static int systemAutoTime = 0;

    private boolean mIsExiting = false;

    private final int mCecStatusOn = 1;

    private Handler mAutoShutdownHandler = new Handler();

    private Handler mImageViewAnimationHandler = new Handler();

    private boolean mIsEasShow = false;

    private int mEasRemainTime = 0;

    private final int CH_NUM_INVALID = -1;

    private int mEasPreProgramMajor = CH_NUM_INVALID;

    private int mEasPreProgramMinor = CH_NUM_INVALID;

    private final int EAS_DISPLAY_TIME_DEFAULT = 10;

    private final int EAS_DISPLAY_TIME_CHECK = 15;

    private final int EAS_DISPLAY_TIME_EXTEND = 30;

    private int mPreviousInputSource = mTvCommonManager.INPUT_SOURCE_NONE;

    public int caCurNotifyIdx;

    public int caCurEvent;

    private int msgFromActivity = 1;

    protected static AlertDialog mExitDialog;

    private ProgressDialog mCecInfoDialog = null;

    private boolean mIsCecDialogCanceled = false;

    // now close 3D function, when open, it
    private boolean _3Dflag = false;

    // shall be deleted
    private static RootActivity rootActivity = null;

    private CaViewHolder caViewHolder;

    private boolean startPvr = false;

    private static boolean isReboot = false;

    private boolean mIskeyLocked = false;

    private PowerManager mPowerManager = null;

    private AlertDialog mCiPlusOPRefreshDialog = null;

    private BroadcastReceiver mReceiver = null;

    private ResourceEventListener mResourceEventListener = null;

    private OnDtvPlayerEventListener mDtvPlayerEventListener = null;

    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    private OnTvPlayerEventListener mTvPlayerEventListener = null;

    private OnTvCommonEventHandler mTvCommonEventHandler = null;

    private OnEpgTimerEventListener mEpgTimerEventListener = null;

    private OnPvrTimerEventListener mPvrTimerEventListener = null;

    private OnCaEventListener mCaEventListener = null;

    private OnUiEventListener mUiEventListener = null;

    private OnCiStatusChangeEventListener mCiStatusChangeEventListener = null;

    private OnCiAsyncCmdNotifyListener mCiCmdNotifyListener = null;

    private OnCecCtrlEventListener mCecCtrlEventListener = null;

    private OnOadTimerEventListener mOadTimerEventListener = null;

    private OnInputSourceEventListener mInputSourceEventListener = null;

    private OnEwsEventListener mEwsEventListener = null;

    private AlertDialog mOadDownloadConfirmDialog = null;

    private Toast mCcKeyToast;

    private TextView mSignalStatusView = null;

    private TextView mSignalStatusView4Pop = null;

    private TextView mFreezeView = null;

    private boolean mIsPowerOn = false;

    private boolean mIsToPromptPassword = false;

    private int mTvSystem = 0;

    private PasswordCheckDialog mPasswordLock = null;

    private PasswordCheckDialog mNitPasswordLock = null;

    private boolean isOnStart = false;

    private boolean isOnStop = false;

    private boolean mIsForceRevealPwdPrompt = false;

    private int mNitAutoScanType = TvCommonManager.NIT_UPDATE_NONE;

    private String mStrParentalLockPromptMessage = "";

    private USBDiskSelecter mUsbSelector = null;

    private LinearLayout mLinearlayoutNoSignalImageView = null;

    private ImageView mArrowImageView = null;

    private Animation mTranslate;

    private boolean mIsInputBlocked = false;

    private final int INPUT_SOURCE_BLOCKED_ENABLED = 0;

    private final int INPUT_SOURCE_BLOCKED_DISABLED = 1;

    private final int MASK_ACTIVITY_PVR_FULL_BROWSER = (1 << 1);

    private int mActivityStatus = 0;

    private BroadcastReceiver mVolumeReceiver = null;

    private String mTtsNoSignalViewStr = "";

    private Runnable mTtsSpeakRunnable = null;

    private Runnable mEasDisplayTextRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "mEasDisplayTextRunnable Runnable :" + mEasRemainTime);
            if (mEasRemainTime > 0) {
                mEasRemainTime--;
                handler.postDelayed(this, 1000);
            } else {
                Log.i(TAG, "mEasPreProgramMajor:" + mEasPreProgramMajor + " mEasPreProgramMinor:"
                        + mEasPreProgramMinor);
                if ((mEasPreProgramMajor + mEasPreProgramMinor) > 0) {
                    Log.i(TAG, "switch to per channel:" + mEasPreProgramMajor + "."
                            + mEasPreProgramMinor);
                    TvAtscChannelManager.getInstance().programSel(mEasPreProgramMajor,
                            mEasPreProgramMinor);
                }
                EmergencyAlertDialog.create(getApplicationContext()).dismiss();
                mIsEasShow = false;
            }
        }
    };

    private class ResourceEventListener implements OnResourceEventListener {
        @Override
        public boolean onResourceEvent(int what, int arg1, int arg2, Object obj) {
            Log.d(TAG, "onResourceEvent what: " + what);

            if (what == TvCommonManager.TV_RESOURCE_CONFLICT_POPUP_DIALOG) {
                mHandler.sendEmptyMessage(Constant.ROOTACTIVITY_DISPLAY_RESOURCE_CONFLICT_DIALOG);
            }
            return true;
        }
    }

    private OnSignalEventListener mSignalEventListener = null;

    private class SignalEventListener implements OnSignalEventListener {
        @Override
        public boolean onAtvSignalEvent(int what, int arg1, int arg2, Object obj) {
            if (what == TvChannelManager.TVPLAYER_SIGNAL_LOCK) {
                Log.d(TAG, "ATV - SIGNAL_LOCK");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_LOCK;
                signalLockHandler.sendMessage(m);
                return true;
            } else if (what == TvChannelManager.TVPLAYER_SIGNAL_UNLOCK) {
                Log.d(TAG, "ATV - SIGNAL_UNLOCK");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_UNLOCK;
                signalLockHandler.sendMessage(m);
                return true;
            } else if (what == TvChannelManager.TVPLAYER_SIGNAL_UNSTABLE) {
                Log.d(TAG, "ATV - SIGNAL_UNSTABLE");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_UNSTABLE;
                signalLockHandler.sendMessage(m);
                return true;
            }
            return false;
        }

        @Override
        public boolean onDtvSignalEvent(int what, int arg1, int arg2, Object obj) {
            if (what == TvChannelManager.TVPLAYER_SIGNAL_LOCK) {
                Log.d(TAG, "DTV - SIGNAL_LOCK");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_LOCK;
                signalLockHandler.sendMessage(m);
                return true;
            } else if (what == TvChannelManager.TVPLAYER_SIGNAL_UNLOCK) {
                Log.d(TAG, "DTV - SIGNAL_UNLOCK");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_UNLOCK;
                signalLockHandler.sendMessage(m);
                return true;
            } else if (what == TvChannelManager.TVPLAYER_SIGNAL_UNSTABLE) {
                Log.d(TAG, "DTV - SIGNAL_UNSTABLE");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_UNSTABLE;
                signalLockHandler.sendMessage(m);
            }
            return false;
        }

        @Override
        public boolean onTvSignalEvent(int what, int arg1, int arg2, Object obj) {
            if (TvCommonManager.INPUT_SOURCE_STORAGE == mTvCommonManager.getCurrentTvInputSource()) {
                Log.d(TAG, "onTvSignalEvent(), ignored!");
                return true;
            }
            if (what == TvChannelManager.TVPLAYER_SIGNAL_LOCK) {
                Log.d(TAG, "TV - SIGNAL_LOCK");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_LOCK;
                signalLockHandler.sendMessage(m);
                return true;
            } else if (what == TvChannelManager.TVPLAYER_SIGNAL_UNLOCK) {
                Log.d(TAG, "TV - SIGNAL_UNLOCK");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_UNLOCK;
                signalLockHandler.sendMessage(m);
                return true;
            } else if (what == TvChannelManager.TVPLAYER_SIGNAL_UNSTABLE) {
                Log.d(TAG, "TV - SIGNAL_UNSTABLE");
                Message m = Message.obtain();
                m.arg1 = TvEvent.SIGNAL_UNSTABLE;
                signalLockHandler.sendMessage(m);
                return true;
            }
            return false;
        }
    }

    private class DtvPlayerEventListener implements OnDtvPlayerEventListener {
        @Override
        public boolean onDtvChannelNameReady(int what) {
            return false;
        }

        @Override
        public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
            return false;
        }

        @Override
        public boolean onDtvProgramInfoReady(int what) {
            return false;
        }

        @Override
        public boolean onCiLoadCredentialFail(int what) {
            return false;
        }

        @Override
        public boolean onEpgTimerSimulcast(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onHbbtvStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onMheg5StatusMode(int what, int arg1) {
            if (TvChannelManager.MHEG5_STATUS_NON != mTvChannelManager.getMheg5KeyRegisterStatus()) {
                RootActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(TAG, "call onMheg5StatusMode");
                        showNoSignalView(false);
                    }
                });
                return true;
            }
            return false;
        }

        @Override
        public boolean onMheg5ReturnKey(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadHandler(int what, int arg1, int arg2) {
            Log.d(TAG, "onOadHandler(), what = " + what + ", arg1 = " + arg1);
            if ((arg1 == TvOadManager.OAD_UI_TYPE_DOWNLOAD_CONFIRM)
                    || (arg1 == TvOadManager.OAD_UI_TYPE_CH_CHANGE)) {
                RootActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        oadDownloadConfirm();
                    }
                });
            } else if (arg1 == TvOadManager.OAD_DOWNLOAD_FINISH) {
                mTvCommonManager.updateSystem(Constant.OAD_UPGRADE_FILE);
            }
            return true;
        }

        @Override
        public boolean onOadDownload(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onDtvAutoUpdateScan(int what) {
            return false;
        }

        @Override
        public boolean onTsChange(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogLossSignal(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogNewMultiplex(int what) {
            return false;
        }

        @Override
        public boolean onPopupScanDialogFrequencyChange(int what) {
            return false;
        }

        @Override
        public boolean onRctPresence(int what) {
            return false;
        }

        @Override
        public boolean onChangeTtxStatus(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onDtvPriComponentMissing(int what) {
            return false;
        }

        @Override
        public boolean onAudioModeChange(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onMheg5EventHandler(int what, int arg1) {
            Log.d(TAG, "Mheg5EventHandler, what = " + what);
            if (what == TvEvent.MHEG5_EVENT_HANDLER) {
                RootActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Utility.startSourceInfo(RootActivity.this);
                    }
                });
            }
            return true;
        }

        @Override
        public boolean onOadTimeout(int what, int arg1) {
            mHandler.sendEmptyMessage(Constant.ROOTACTIVITY_OAD_DOWNLOAD_TIMEOUT);
            return true;
        }

        @Override
        public boolean onGingaStatusMode(int what, boolean arg1) {
            return false;
        }

        @Override
        public boolean onSignalLock(int what) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            return false;
        }

        @Override
        public boolean onUiOPRefreshQuery(int what) {
            Log.i(TAG, "get CI+ OP event EV_CI_OP_REFRESH_QUERY");
            TvCiManager.getInstance().ciClearOPSearchSuspended();
            displayOpRefreshconfirmation();
            return true;
        }

        @Override
        public boolean onUiOPServiceList(int what) {
            return false;
        }

        @Override
        public boolean onUiOPExitServiceList(int what) {
            return false;
        }
    }

    private class AtvPlayerEventListener implements OnAtvPlayerEventListener {

        @Override
        public boolean onAtvAutoTuningScanInfo(int what, AtvEventScan extra) {
            return false;
        }

        @Override
        public boolean onAtvManualTuningScanInfo(int what, AtvEventScan extra) {
            return false;
        }

        @Override
        public boolean onSignalLock(int what) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            return false;
        }

        @Override
        public boolean onAtvProgramInfoReady(int what) {
            return false;
        }
    }

    private class TvPlayerEventListener implements OnTvPlayerEventListener {

        @Override
        public boolean onScreenSaverMode(int what, int arg1) {
            Message m = Message.obtain();
            m.arg1 = arg1;
            screenSaverHandler.sendMessage(m);
            return true;
        }

        @Override
        public boolean onHbbtvUiEvent(int what, HbbtvEventInfo eventInfo) {
            return false;
        }

        @Override
        public boolean onPopupDialog(int what, int arg1, int arg2) {
            Log.d(TAG, "onPopupDialog(" + arg1 + "," + arg2 + ")");
            boolean lockstatus = TvParentalControlManager.getInstance().isSystemLock();
            if (TvCommonManager.POPUP_DIALOG_SHOW == arg1) {
                mIsToPromptPassword = true;
                /**
                 * The password prompt can show if and only if
                 * mIsToPromptPassword equals true and one of the belowing
                 * conditions holds 1. There is no any UI in the foreground 2.
                 * The Source Info(Banner) is lauched and currently(or going to
                 * be) in foreground. 3. the systemlock is false(open)
                 */
                if ((true == mIsActive) || (true == mIsForceRevealPwdPrompt)
                        || (false == lockstatus)) {
                    RootActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPasswordLock(true);
                        }
                    });
                }
            } else if (TvCommonManager.POPUP_DIALOG_HIDE == arg1) {
                mIsToPromptPassword = false;
                RootActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPasswordLock(false);
                    }
                });
            }
            return true;
        }

        @Override
        public boolean onPvrNotifyPlaybackTime(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackSpeedChange(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordTime(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordSize(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordStop(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackStop(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackBegin(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesBefore(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesAfter(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyOverRun(int what) {
            RootActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast toast = Toast.makeText(RootActivity.this, R.string.str_disk_too_slow,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            return true;
        }

        @Override
        public boolean onPvrNotifyUsbRemoved(int what, int arg1) {
            RootActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast toast = Toast.makeText(RootActivity.this, R.string.str_pvr_usb_removed,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            if (PVRActivity.isPVRActivityActive) {
                Intent intent = new Intent(TvIntent.ACTION_PVR_ACTIVITY);
                intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_RECORD_STOP);
                if (intent.resolveActivity(RootActivity.this.getPackageManager()) != null) {
                    RootActivity.this.startActivity(intent);
                }
            } else {
                final TvPvrManager pvr = TvPvrManager.getInstance();
                if (pvr != null) {
                    pvr.stopPvr();
                    if (pvr.getIsBootByRecord()) {
                        pvr.setIsBootByRecord(false);
                        mTvCommonManager.standbySystem("pvr");
                    }
                }
            }
            return true;
        }

        @Override
        public boolean onPvrNotifyCiPlusProtection(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyParentalControl(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int what) {
            final boolean result = ((TvPvrManager.getInstance().startAlwaysTimeShiftRecord() == TvPvrManager.PVR_STATUS_SUCCESS));
            RootActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (false == result) {
                        Toast toast = Toast.makeText(RootActivity.this,
                                R.string.str_pvr_alwaystimeshift_record_fail, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
            Log.d(TAG, "onPvrNotifyAlwaysTimeShiftProgramReady result:" + result);
            return result;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int what) {
            Log.d(TAG, "onPvrNotifyAlwaysTimeShiftProgramNotReady");
            if (TvPvrManager.getInstance().isAlwaysTimeShiftRecording()) {
                if (TvPvrManager.getInstance().isPlaybacking()) {
                    return false;
                } else if (TvPvrManager.getInstance().isAlwaysTimeShiftPlaybackPaused()) {
                    TvPvrManager.getInstance().pausePvrAlwaysTimeShiftPlayback(false);
                }
                TvPvrManager.getInstance().stopAlwaysTimeShiftRecord();
            }
            return true;
        }

        @Override
        public boolean onPvrNotifyCiPlusRetentionLimitUpdate(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onTvProgramInfoReady(int what) {
            Message msg = mHandler.obtainMessage();
            msg.what = Constant.ROOTACTIVITY_TVPROMINFOREADY_MESSAGE;
            mHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onSignalLock(int what) {
            return false;
        }

        @Override
        public boolean onSignalUnLock(int what) {
            return false;
        }

        @Override
        public boolean onEpgUpdateList(int what, int arg1) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePip(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisablePop(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableDualView(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean on4k2kHDMIDisableTravelingMode(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onDtvPsipTsUpdate(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onEmerencyAlert(int what, int arg1, int arg2) {

            if (TvCommonManager.getInstance().getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ATSC) {
                return false;
            }

            Log.e(TAG, "onEmerencyAlert() arg1:" + arg1 + ",  arg2:" + arg2);

            MwAtscEasInfo easInfo = null;
            String text = "";

            easInfo = TvAtscChannelManager.getInstance().getEASInProgress();
            Log.i(TAG, "easInfo:" + easInfo.u16DetailsMajorNum + "." + easInfo.u16DetailsMinorNum);

            if (easInfo == null) {
                return false;
            }

            int type = arg1;
            Log.i(TAG, "EAS currentType is" + type);

            switch (type) {
                case TvAtscChannelManager.EAS_TEXT_ONLY:
                    mEasRemainTime = EAS_DISPLAY_TIME_DEFAULT;
                    handler.removeCallbacks(mEasDisplayTextRunnable);
                    handler.post(mEasDisplayTextRunnable);
                    mIsEasShow = true;
                    StringBuffer sb = new StringBuffer();
                    for (int j = 0; j < easInfo.au8AlertText.length; j++) {
                        if (easInfo.au8AlertText[j] > 0) {
                            sb.append((char) easInfo.au8AlertText[j]);
                        }
                    }

                    if (easInfo.au8AlertText != null && easInfo.au8AlertText[0] != 0) {
                        text += sb.toString();
                    } else {
                        text = "";
                    }
                    EmergencyAlertDialog.create(getApplicationContext()).show(text);
                    if ((mEasPreProgramMajor + mEasPreProgramMinor) > 0) {
                        TvAtscChannelManager.getInstance().programSel(mEasPreProgramMajor,
                                mEasPreProgramMinor);
                        mEasPreProgramMajor = CH_NUM_INVALID;
                        mEasPreProgramMinor = CH_NUM_INVALID;
                    }
                    break;
                case TvAtscChannelManager.EAS_DETAIL_CHANNEL:
                    mEasRemainTime = easInfo.u8AlertTimeRemain;
                    Log.i(TAG, "remainTime" + mEasRemainTime);
                    if (mEasRemainTime < EAS_DISPLAY_TIME_CHECK) {
                        mEasRemainTime += EAS_DISPLAY_TIME_EXTEND;
                    }
                    Log.i(TAG, "remainTime:" + mEasRemainTime);
                    handler.removeCallbacks(mEasDisplayTextRunnable);
                    handler.post(mEasDisplayTextRunnable);
                    mIsEasShow = true;
                    StringBuffer sb2 = new StringBuffer();
                    for (int j = 0; j < easInfo.au8AlertText.length; j++) {
                        if (easInfo.au8AlertText[j] > 0) {
                            sb2.append((char) easInfo.au8AlertText[j]);
                        }
                    }

                    if (easInfo.au8AlertText != null && easInfo.au8AlertText[0] != 0) {
                        text += sb2.toString();
                    } else {
                        text = "";
                    }
                    Log.i(TAG, "Text:" + text);

                    EmergencyAlertDialog.create(getApplicationContext()).show(text);
                    ProgramInfo pInfo = TvAtscChannelManager.getInstance().getCurrentProgramInfo();
                    if ((easInfo.u16DetailsMajorNum == pInfo.majorNum)
                            && (easInfo.u16DetailsMinorNum == pInfo.minorNum)) {
                        Log.i(TAG, "same channel ,needn't tune to channel");
                        return true;
                    }
                    Log.i(TAG, "switch channel to " + easInfo.u16DetailsMajorNum + "."
                            + easInfo.u16DetailsMinorNum);
                    TvAtscChannelManager.getInstance().programSel(easInfo.u16DetailsMajorNum,
                            easInfo.u16DetailsMinorNum);
                    mEasPreProgramMajor = pInfo.majorNum;
                    mEasPreProgramMinor = pInfo.minorNum;
                    break;
                case TvAtscChannelManager.EAS_STOP_TEXT_SCORLL:
                    EmergencyAlertDialog.create(getApplicationContext()).stopScroll();
                    break;
                case TvAtscChannelManager.EAS_UPDATE_TIME_REMAINING:
                    mEasRemainTime = easInfo.u8AlertTimeRemain;
                    Log.i(TAG, "remainTime:" + mEasRemainTime);
                    handler.removeCallbacks(mEasDisplayTextRunnable);
                    handler.post(mEasDisplayTextRunnable);
                    break;
            }
            return true;
        }

        @Override
        public boolean onDtvChannelInfoUpdate(int what, int info, int arg2) {
            return false;
        }
    }

    private class TvCommonEventHandler implements OnTvCommonEventHandler {
        @Override
        public boolean onTvCommonEvent(int what, int arg1, int arg2, Object obj) {
            Log.i(TAG, "onTvCommonEvent(), what:" + what + ", arg1:" + arg1);
            switch (what) {
                case TvCommonManager.EV_DTV_AUTO_UPDATE_SCAN:
                    mNitAutoScanType = arg1;
                    if ((arg1 == TvCommonManager.NIT_UPDATE_MUX_ADD)
                            || (arg1 == TvCommonManager.NIT_UPDATE_FREQ_CHANGE)
                            || (arg1 == TvCommonManager.NIT_UPDATE_MUX_REMOVE)) {
                        RootActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                nitAutoUpdateScanConfirm();
                            }
                        });
                        return true;
                    }
                    break;
                case TvCommonManager.EV_PVR_NOTIFY_PLAYBACK_BEGIN:
                    TvPvrManager.getInstance().setPvrPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_1X);
                    return true;
                default:
                    break;
            }
            return false;
        }
    }

    private class EpgTimerEventListener implements OnEpgTimerEventListener {
        @Override
        public boolean onEpgTimerEvent(int what, int arg1, int arg2) {
            Log.i(TAG, "onEpgTimerEvent(), what:" + what);
            switch (what) {
                case TvTimerManager.TVTIMER_EPG_TIME_UP: {
                    if (mIsActive == false) {
                        Intent intent = new Intent(TvIntent.ACTION_START_ROOTACTIVITY);
                        startActivity(intent);
                    }
                }
                    break;
                case TvTimerManager.TVTIMER_EPG_TIMER_RECORD_START: {
                    Intent intent = new Intent(TvIntent.ACTION_PVR_ACTIVITY);
                    intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_RECORD_START);
                    if (intent.resolveActivity(RootActivity.this.getPackageManager()) != null) {
                        RootActivity.this.startActivity(intent);
                    }
                }
                    break;
                default: {
                }
                    break;
            }
            return true;
        }
    }

    private class PvrTimerEventListener implements OnPvrTimerEventListener {
        @Override
        public boolean onPvrTimerEvent(int what, int arg1, int arg2) {
            Log.i(TAG, "onPvrTimerEvent(), what:" + what);
            switch (what) {
                case TvTimerManager.TVTIMER_PVR_NOTIFY_RECORD_STOP: {
                    Intent intent = new Intent(TvIntent.ACTION_PVR_ACTIVITY);
                    intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_RECORD_STOP);
                    if (intent.resolveActivity(RootActivity.this.getPackageManager()) != null) {
                        RootActivity.this.startActivity(intent);
                    }
                }
                    break;
                default: {
                }
                    break;
            }
            return true;
        }
    }

    private class OadTimerEventListener implements OnOadTimerEventListener {
        @Override
        public boolean onOadTimerEvent(int what) {
            Log.i(TAG, "onOadTimerEvent(), what:" + what);
            switch (what) {
                case TvTimerManager.TVTIMER_OAD_TIME_SCAN: {
                    oadScanConfirm();
                }
                    break;
                default: {
                }
                    break;
            }
            return true;
        }
    }

    private class InputSourceEventListener implements OnInputSourceEventListener {
        @Override
        public boolean onInputSourceLockEvent(int what, int arg1, int arg2, Object obj) {
            Log.d(TAG, "onInputSourceLock arg1 = " + arg1 + " ,arg2 = " + arg2);
            boolean bUpdate = false;
            if (INPUT_SOURCE_BLOCKED_ENABLED == arg1) {
                if (false == mIsInputBlocked) {
                    bUpdate = true;
                    mIsInputBlocked = true;
                }
            } else {
                if (true == mIsInputBlocked) {
                    bUpdate = true;
                    mIsInputBlocked = false;
                }
            }
            if (true == bUpdate) {
                new Thread() {
                    public void run() {
                        int curInputSource = TvCommonManager.getInstance()
                                .getCurrentTvInputSource();
                        Message m = Message.obtain();
                        m.arg1 = mScreenSaverStatus;
                        m.arg2 = curInputSource;
                        screenSaverHandler.sendMessage(m);
                    };
                }.start();
            }
            return true;
        }
    }

    private class EwsEventListener implements OnEwsEventListener {
        @Override
        public boolean onEwsEvent(int what, int arg1, int arg2, Object obj) {
            /*
             * EWS sends two kinds of event to AN:
             * TVPLAYER_DISPLAY_EMERGENCY_SYSTEM: AN needs to start the EWS UI
             * TVPLAYER_CLOSE_EMERGENCY_SYSTEM: AN needs to close the EWS UI
             */
            Log.d(TAG, "onEwsEvent what = " + what + ", arg1 = " + arg1 + " ,arg2 = " + arg2);
            if (TvChannelManager.TVPLAYER_DISPLAY_EMERGENCY_SYSTEM == what) {
                Intent intent = new Intent(TvIntent.ACTION_EWS_FINISH_ACTIVITY);
                LocalBroadcastManager.getInstance(RootActivity.this).sendBroadcastSync(intent);
                mHandler.sendEmptyMessage(Constant.ROOTACTIVITY_DISPLAY_EMERGENCY_SYSTEM);
            }
            return true;
        }
    }

    BroadcastReceiver mLocalBroadcastReveiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            Log.d(TAG, "onReceive(), action = " + intent.getAction());
            if ((intent.getAction().equals(TvIntent.ACTION_PVR_ENTER_FULL_BROWSER))
                    || (intent.getAction().equals(TvIntent.ACTION_PVR_LEAVE_FULL_BROWSER))) {
                RootActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Message msgSaver = Message.obtain();
                        if (intent.getAction().equals(TvIntent.ACTION_PVR_ENTER_FULL_BROWSER)) {
                            mActivityStatus = mActivityStatus | MASK_ACTIVITY_PVR_FULL_BROWSER;
                            msgSaver.arg1 = mScreenSaverStatus;
                        } else {
                            mActivityStatus = mActivityStatus & (~MASK_ACTIVITY_PVR_FULL_BROWSER);
                            /*
                             * set status to default and wait for new video
                             * playing result to update it
                             */
                            msgSaver.arg1 = SCREENSAVER_DEFAULT_STATUS;
                        }
                        Log.i(TAG,
                                "LocalBroadcastReveiver, mActivityStatus = "
                                        + String.format("0x%X", mActivityStatus));
                        /*
                         * refresh screen saver status
                         */
                        screenSaverHandler.sendMessage(msgSaver);
                    }
                });
            }
        }
    };

    public static RootActivity getInstance() {
        return rootActivity;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case Constant.ROOTACTIVITY_OAD_DOWNLOAD_TIMEOUT:
                    handleOadDownloadTimeout();
                    break;
                case Constant.ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT:
                    handleOadDownloadUiTimeout();
                    break;
                case Constant.ROOTACTIVITY_RESUME_MESSAGE:
                    /*
                     * Modified by gerard.jiang for "0386249" in 2013/04/28. Add
                     * reboot flag When system is suspending (isScreenOn == false),
                     * do not start No Signal Activity. by Desmond Pu 2013/12/18
                     */
                    if (_3Dflag == false && !isReboot && (mPowerManager.isScreenOn()))
                    /***** Ended by gerard.jiang 2013/04/28 *****/
                    {
                        updateTvSourceSignal();
                    }
                    // Notfiy event queue to start sending pending event
                    try {
                        mTvCommonManager.setTvosCommonCommand(Constant.TV_EVENT_LISTENER_READY);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.ROOTACTIVITY_CREATE_MESSAGE:
                    executePreviousTask(getIntent());

                    SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("_3Dflag", _3Dflag);
                    editor.commit();
                    checkSystemAutoTime();
                    break;
                case Constant.ROOTACTIVITY_TVPROMINFOREADY_MESSAGE:
                    int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
                    if ((TvCommonManager.INPUT_SOURCE_CVBS <= curInputSource)
                            && (TvCommonManager.INPUT_SOURCE_CVBS_MAX > curInputSource)
                            || (TvCommonManager.INPUT_SOURCE_YPBPR <= curInputSource)
                            && (TvCommonManager.INPUT_SOURCE_YPBPR_MAX > curInputSource)
                            || (TvCommonManager.INPUT_SOURCE_HDMI <= curInputSource)
                            && (TvCommonManager.INPUT_SOURCE_HDMI_MAX > curInputSource)) {
                        Utility.startSourceInfo(RootActivity.this);
                    }
                    break;
                case Constant.ROOTACTIVITY_CANCEL_DIALOG:
                    if (null != mCecInfoDialog) {
                        if (false == mIsCecDialogCanceled) {
                            mHandler.sendEmptyMessageDelayed(Constant.ROOTACTIVITY_CANCEL_DIALOG,
                                    CEC_INFO_DISPLAY_TIMEOUT);
                        } else {
                            mCecInfoDialog.dismiss();
                            mCecInfoDialog = null;
                            mIsCecDialogCanceled = false;
                        }
                    }
                    break;
                case Constant.ROOTACTIVITY_DISPLAY_EMERGENCY_SYSTEM:
                    Intent intent = new Intent(TvIntent.ACTION_EWS_ACTIVITY);
                    RootActivity.this.startActivity(intent);
                    break;
                case Constant.ROOTACTIVITY_TTS_SPEAK_NO_SIGNAL_STATUS:
                    ttsSpeakSignalStatusView();
                    break;
                case Constant.ROOTACTIVITY_DELAY_TO_CLOSE_DIALOG:
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        closeDialogs();
                    }
                    break;
                case Constant.ROOTACTIVITY_DISPLAY_RESOURCE_CONFLICT_DIALOG:
                    gotoResourceConflictActivity();
                    break;
                default:
                    break;
            }
        };
    };

    public boolean isBackKeyPressed() {
        return mIsBackKeyPressed;
    }

    private void gotoPVRRecordingForStandBy() {
        Intent i = new Intent(this, PVRActivity.class);
        startActivity(i);
    }

    private void gotoResourceConflictActivity() {
        Intent i = new Intent(this, ResourceConflictActivity.class);
        startActivity(i);
        this.finish();
    }

    Handler standbyHandler = new Handler() {

        public void handleMessage(Message msg) {
            gotoPVRRecordingForStandBy();
        };
    };

    // ---------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        setContentView(R.layout.root);
        rootActivity = this;
        _3Dflag = false;
        super.onCreate(savedInstanceState);
        mTvCommonManager = TvCommonManager.getInstance();
        mTvPictureManager = TvPictureManager.getInstance();
        mTvS3DManager = TvS3DManager.getInstance();
        mTvChannelManager = TvChannelManager.getInstance();
        // tvAudioManager =TvAudioManager.getInstance();
        mTvCecManager = TvCecManager.getInstance();
        mTvSystem = mTvCommonManager.getCurrentTvSystem();
        mSignalStatusView = (TextView) findViewById(R.id.SiganlStatusText);
        mSignalStatusView4Pop = (TextView) findViewById(R.id.SiganlStatusText4Pop);
        mFreezeView = (TextView) findViewById(R.id.FreezeText);
        mSignalStatusView.setText(R.string.str_no_signal);
        mSignalStatusView4Pop.setText(R.string.str_no_signal);
        mFreezeView.setText("Freeze On");
        mFreezeView.setBackgroundColor(android.graphics.Color.BLACK);
        mLinearlayoutNoSignalImageView = (LinearLayout) findViewById(R.id.linearlayout_nosignal_imageview);
        mArrowImageView = (ImageView) findViewById(R.id.arrow_imageview);
        mTranslate = AnimationUtils.loadAnimation(this, R.anim.translate_anim);
        mIsPowerOn = getIntent().getBooleanExtra("isPowerOn", false);
        createSurfaceView();
        mIsMsrvStarted = true;
        caViewHolder = new CaViewHolder(this);
        mHandler.sendEmptyMessage(Constant.ROOTACTIVITY_CREATE_MESSAGE);
        mTvMhlManager = TvMhlManager.getInstance();
        mTvTimerManager = TvTimerManager.getInstance();
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);

        switch (mTvSystem) {
            case TvCommonManager.TV_SYSTEM_DVBT:
            case TvCommonManager.TV_SYSTEM_DVBC:
            case TvCommonManager.TV_SYSTEM_DVBS:
            case TvCommonManager.TV_SYSTEM_DVBT2:
            case TvCommonManager.TV_SYSTEM_DVBS2:
                mStrParentalLockPromptMessage = getResources().getString(
                        R.string.str_rating_blocked);
                break;
            default:
                mStrParentalLockPromptMessage = getResources().getString(
                        R.string.str_parental_block);
                break;
        }

        // Initiate Password Check Dialog
        int viewResId;
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            viewResId = R.layout.password_check_dialog_6_digits;
        } else {
            viewResId = R.layout.password_check_dialog_4_digits;
        }

        mPasswordLock = new PasswordCheckDialog(rootActivity, viewResId) {
            @Override
            public String onCheckPassword() {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    return MenuConstants.getSharedPreferencesValue(rootActivity,
                            MenuConstants.VCHIPPASSWORD, MenuConstants.VCHIPPASSWORD_DEFAULTVALUE);
                } else {
                    return String.format("%04d", TvParentalControlManager.getInstance()
                            .getParentalPassword());
                }
            }

            @Override
            public void onPassWordWrong() {
                if (null != mToast) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(rootActivity,
                        rootActivity.getResources().getString(R.string.str_check_password_wrong),
                        Toast.LENGTH_SHORT);
                mToast.show();

                try {
                    mTvChannelManager.sendMheg5IcsCommand(TvChannelManager.M5_ICS_PIN_ERROR,
                            TvChannelManager.M5_ICS_FREE_STATUS);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPassWordCorrect() {
                mToast.cancel();
                mToast = Toast.makeText(rootActivity,
                        rootActivity.getResources().getString(R.string.str_check_password_pass),
                        Toast.LENGTH_SHORT);
                mToast.show();

                if (true == Utility.isSupportInputSourceLock()) {
                    if (TvCommonManager.getInstance().getInputSourceLock(
                            TvCommonManager.getInstance().getCurrentTvInputSource())) {
                        TvCommonManager.getInstance().setInputSourceLock(false,
                                TvCommonManager.getInstance().getCurrentTvInputSource());
                    }
                }
                if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                    // Unlock Parental Lock
                    TvAtscChannelManager.getInstance().setBlockSysLockModeTemporarily(false);
                    // Unlock Channel Lock
                } else {
                    TvParentalControlManager.getInstance().unlockChannel();
                }
                try {
                    mTvChannelManager.sendMheg5IcsCommand(TvChannelManager.M5_ICS_PIN_RIGHT,
                            TvChannelManager.M5_ICS_FREE_STATUS);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                dismiss();
            }

            @Override
            public void onKeyDown(int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_ENTER != keyCode) {
                    RootActivity.this.onKeyDown(keyCode, keyEvent);
                }
            }

            @Override
            public void onBackPressed() {
                mToast.cancel();
                mToast = Toast.makeText(rootActivity,
                        rootActivity.getResources().getString(R.string.str_check_password_invalid),
                        Toast.LENGTH_SHORT);
                mToast.show();
            }
        };

        // updateTvSourceSignal();
        if (caViewHolder.isCaEnable()) {
            caViewHolder.emailNotify(CaManager._current_email_type);
            caViewHolder.detitleNotify(CaManager._current_detitle_type);
        }
        boolean isPVRStandby = getIntent().getBooleanExtra("isPVRStandby", false);
        Log.e(TAG, "onCreate isPVRStandby:" + isPVRStandby);
        if (isPVRStandby) {
            standbyHandler.sendEmptyMessageDelayed(100, 5000);
        }

        /* for support input source change intent send from source hot key */
        if (getIntent() != null && getIntent().getAction() != null) {
            int isdbAntennaType = getIntent().getIntExtra("inputAntennaType",
                    TvIsdbChannelManager.DTV_ANTENNA_TYPE_NONE);
            if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_NONE != isdbAntennaType) {
                if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                    startISDBAntennaTypeChange(isdbAntennaType);
                }
            }

            if (getIntent().hasExtra("inputSrc")) {
                /*
                 * clear screen saver status after changing input source and
                 * hide NoSignalView before change input source.
                 */
                mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
                showNoSignalView(false);
                int inputsource = getIntent().getIntExtra("inputSrc",
                        Utility.getDefaultInputSource());
                Log.i(TAG, "onCreate:inputsource = " + inputsource);
                startSourceChange(inputsource);
            }
        }

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(TvIntent.ACTION_EXIT_TV_APK)) {
                    tvApkExitHandler();
                } else if (intent.getAction().equals(MIntent.ACTION_FINISH_TV_PLAYER)) {
                    /*
                     * Force finish when receive ACTION_FINISH_TV_PLAYER
                     * broadcast intent. Since there may exist some situation
                     * that cause the Activity life-cycle not correct. (Use
                     * case: start TvPlayer and press back key as soon as
                     * possible, and then press ok to finish TvPlayer) The
                     * activity may not enter stop/destroy state when finish.
                     * This issue is cause from framework and is hard to fix. So
                     * we make a workaround solution here. When finishing, the
                     * TvView will be destroyed, so we can send a
                     * ACTION_FINISH_TV_PLAYER broadcast from TvView when
                     * surfaceDestroyed. And then we can execute
                     * onStop/onDestroy flow manually in TvPlayer when receive
                     * this broadcast.
                     */
                    if (!isOnStop) {
                        onStop();
                        onDestroy();
                        System.exit(0);
                    }
                } else if (intent.getAction().equals(TvIntent.ACTION_SETUP_LOCATION_WIZARD_DONE)) {
                    checkSetupWizardStatus();
                } else if (intent.getAction().equals(TvIntent.ACTION_TV_TIME_ZONE_CHANGE)) {
                    String timezoneChangeString = intent.getStringExtra("timezoneChangeString");
                    Utility.changeTvTimeZone(context, timezoneChangeString);
                } else if (intent.getAction().equals("ENTRY_LMM")) {
                    isOnStop = true;
                } else if (intent.getAction().equals(TvIntent.ACTION_FORCE_REVEAL_PASSWORD_PROMPT)) {
                    // When received this broadcast, the Password prompt should
                    // force revealed if it exists.
                    mIsForceRevealPwdPrompt = intent.getBooleanExtra(
                            Constant.IS_FORCE_REVEAL_PASSWORD_PROMPT, false);
                    if (true == mIsForceRevealPwdPrompt) {
                        showPasswordLock(mIsToPromptPassword);
                    } else {
                        if (false == mIsActive) {
                            showPasswordLock(false);
                        }
                    }
                } else if (intent.getAction().equals("com.mstar.android.mcast.START_DEMO")) {
                    isOnStop = true;
                } else if (intent.getAction().equals(INTENT_FALCON_BOOT_DONE)) {
                    Utility.initLittleDownCounter();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_EXIT_TV_APK);
        filter.addAction(MIntent.ACTION_FINISH_TV_PLAYER);
        filter.addAction(TvIntent.ACTION_SETUP_LOCATION_WIZARD_DONE);
        filter.addAction(TvIntent.ACTION_TV_TIME_ZONE_CHANGE);
        // FIXME: Do NOT hard-coding
        filter.addAction("ENTRY_LMM");
        filter.addAction(TvIntent.ACTION_FORCE_REVEAL_PASSWORD_PROMPT);
        filter.addAction("com.mstar.android.mcast.START_DEMO");
        filter.addAction(INTENT_FALCON_BOOT_DONE);
        registerReceiver(mReceiver, filter);

        filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_PVR_ENTER_FULL_BROWSER);
        filter.addAction(TvIntent.ACTION_PVR_LEAVE_FULL_BROWSER);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReveiver, filter);

        LayoutInflater factory = LayoutInflater.from(this);
        final View layout = factory.inflate(R.layout.ciplus_oprefresh_confirmation_dialog, null);

        mCiPlusOPRefreshDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.str_ciplus_op_confirmation_title))
                .setView(layout)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setPositiveButton(getString(android.R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TvCiManager.getInstance().sendCiOpSearchStart(false);
                                updateScreenSaver();
                            }
                        })
                .setNegativeButton(getString(android.R.string.no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateScreenSaver();
                            }
                        }).setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        updateScreenSaver();
                    }
                }).create();

        mExitDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.str_root_alert_dialog_title)
                .setMessage(R.string.str_root_alert_dialog_message)
                .setPositiveButton(R.string.str_root_alert_dialog_confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LittleDownTimer.destory();
                                mIsBackKeyPressed = false;
                                dialog.dismiss();
                                mIsExiting = true;
                                finish();
                            }
                        })
                .setNegativeButton(R.string.str_root_alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mIsActive = true;
                                dialog.dismiss();
                                mIsBackKeyPressed = false;
                                bCmd_TvApkExit = false;
                                updateScreenSaver();
                                caViewHolder.sendCaNotifyMsg(caCurEvent, caCurNotifyIdx,
                                        msgFromActivity);
                                mIskeyLocked = false;
                            }
                        }).setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mIskeyLocked = false;
                        mIsActive = true;
                        dialog.dismiss();
                        mIsBackKeyPressed = false;
                        bCmd_TvApkExit = false;
                        updateScreenSaver();
                        caViewHolder.sendCaNotifyMsg(caCurEvent, caCurNotifyIdx, msgFromActivity);

                        // ***add by allen.sun 2013-5-27
                        // Adaptation different resolutions in STB
                        if (Tools.isBox()) {
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Intent pipIntent = new Intent("com.mstar.pipservice");
                                    pipIntent.putExtra("cmd", "visible");
                                    pipIntent.setComponent(new ComponentName(
                                            "com.mstar.miscsetting",
                                            "com.mstar.miscsetting.service.PipService"));
                                    RootActivity.this.startService(pipIntent);
                                }
                            }.start();
                        }
                        // ***and end
                    }

                }).create();

        TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {
            Intent intent;

            @Override
            public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                Log.d(TAG, "onKeyInfo");
                return false;
            }

            @Override
            public boolean onAutoSwitch(int arg0, final int arg1, int arg2) {
                Log.d(TAG, "onAutoSwitch");
                intent = new Intent(TvIntent.ACTION_START_ROOTACTIVITY);
                intent.putExtra("task_tag", "input_source_changed");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mTvS3DManager
                                .setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                        TvCommonManager.getInstance().setInputSource(arg1);
                        startActivity(intent);
                    }
                }).start();
                return false;
            }
        });

        mDtvPlayerEventListener = new DtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mTvPlayerEventListener = new TvPlayerEventListener();
        TvChannelManager.getInstance().registerOnTvPlayerEventListener(mTvPlayerEventListener);
        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mTvCommonEventHandler = new TvCommonEventHandler();
        TvCommonManager.getInstance().registerOnTvCommonEventHandler(mTvCommonEventHandler);
        mEpgTimerEventListener = new EpgTimerEventListener();
        TvTimerManager.getInstance().registerOnEpgTimerEventListener(mEpgTimerEventListener);
        mPvrTimerEventListener = new PvrTimerEventListener();
        TvTimerManager.getInstance().registerOnPvrTimerEventListener(mPvrTimerEventListener);
        mCaEventListener = caViewHolder.getCaEventListener();
        TvCaManager.getInstance().registerOnCaEventListener(mCaEventListener);
        mUiEventListener = new UiEventListener();
        TvCiManager.getInstance().registerOnUiEventListener(mUiEventListener);
        mCiStatusChangeEventListener = new CiStatusChangeEventListener();
        TvCiManager.getInstance().registerOnCiStatusChangeEventListener(
                mCiStatusChangeEventListener);
        mCiCmdNotifyListener = new CiCmdNotifyListener();
        TvCiManager.getInstance().registerOnCiAsyncCmdNotifyListener(mCiCmdNotifyListener);
        mOadTimerEventListener = new OadTimerEventListener();
        TvTimerManager.getInstance().registerOnOadTimerEventListener(mOadTimerEventListener);
        mInputSourceEventListener = new InputSourceEventListener();
        TvChannelManager.getInstance()
                .registerOnInputSourceEventListener(mInputSourceEventListener);
        mSignalEventListener = new SignalEventListener();
        TvChannelManager.getInstance().registerOnSignalEventListener(mSignalEventListener);
        mEwsEventListener = new EwsEventListener();
        TvChannelManager.getInstance().registerOnEwsEventListener(mEwsEventListener);
        mResourceEventListener = new ResourceEventListener();
        TvCommonManager.getInstance()
                .registerOnResourceEventListener(mResourceEventListener);
        TVRootApp app = (TVRootApp) rootActivity.getApplication();
        if (true == app.isPVREnable()) {
            mUsbSelector = new USBDiskSelecter(rootActivity) {
                @Override
                public void onItemChosen(int position, String diskLabel, String diskPath) {
                }
            };
            mUsbSelector.setUSBListener(new USBDiskSelecter.usbListener() {
                @Override
                public void onUSBUnmounted(String diskPath) {
                    Log.d(TAG, "\033[31m onUSBUnmounted diskPath = " + diskPath + "\033[0m");
                }

                @Override
                public void onUSBMounted(String diskPath) {
                    Log.d(TAG, "\033[31m onUSBMounted diskPath = " + diskPath + "\033[0m");
                    setAlwaysTimeShiftPath();
                }

                @Override
                public void onUSBEject(String diskPath) {
                    Log.d(TAG, "\033[31m onUSBEject diskPath = " + diskPath + "\033[0m");
                    if (false == PVRActivity.isPVRActivityActive) {
                        RootActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(RootActivity.this,
                                        R.string.str_pvr_usb_removed, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        if (TvPvrManager.getInstance().isAlwaysTimeShiftRecording()) {
                            Log.d(TAG, "\033[31m stopAlwaysTimeShiftRecord \033[0m");
                            TvPvrManager.getInstance().stopAlwaysTimeShiftRecord();
                        }
                    }
                }
            });

            setAlwaysTimeShiftPath();
        }
    }

    private void setAlwaysTimeShiftPath() {
        TVRootApp app = (TVRootApp) rootActivity.getApplication();
        if (true == app.isPVREnable()) {
            if ((false == TvPvrManager.getInstance().isAlwaysTimeShiftRecording())
                    && (false == TvPvrManager.getInstance().isTimeShiftRecording())
                    && (false == TvPvrManager.getInstance().isRecording())) {
                if (null != mUsbSelector) {
                    String diskPath = mUsbSelector.getBestDiskPath();
                    if ((USBDiskSelecter.NO_DISK != diskPath)
                            && (USBDiskSelecter.CHOOSE_DISK != diskPath)) {
                        // FIXME: DO NOT hardcoding: param 2 stands for FAT
                        File recordPath = new File(diskPath);
                        if (recordPath.exists() && recordPath.isDirectory()) {
                            Log.d(TAG, "\033[31m setAlwaysTimeShiftPath diskPath = " + diskPath
                                    + " \033[0m");
                            TvPvrManager.getInstance().setPvrParams(diskPath, (short) 2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConfigurationChanged(), newConfig:" + newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
        isOnStart = true;
        mCecCtrlEventListener = new CecCtrlEventListener();
        TvCecManager.getInstance().registerOnCecCtrlEventListener(mCecCtrlEventListener);

        // Select Location for the first launch
        SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
        if (false == settings.getBoolean(Constant.PREFERENCES_IS_LOCATION_SELECTED, false)) {
            SharedPreferences.Editor editor = settings.edit();
            Log.d(TAG, "Launch TV First Time , Do AutoScan");
            if (TvCommonManager.TV_SYSTEM_ATSC != mTvSystem
                    && TvCommonManager.TV_SYSTEM_ISDB != mTvSystem) {
                if (TvCiManager.getInstance().isOpMode() == false) {
                    startSetupLocationWizard();
                    return;
                }
            }
        }

        checkSetupWizardStatus();

        mTtsSpeakRunnable = new Runnable() {
            @Override
            public void run() {
                AudioManager audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (0 < currentVolume) {
                    String str = getString(R.string.volume) + " " + String.valueOf(currentVolume);
                    mTvCommonManager.speakTtsDelayed(
                        str
                        , TvCommonManager.TTS_QUEUE_FLUSH
                        , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
                        , TTS_VOLUME_UPDATE_DELAY_MS);
                }
            }
        };

        mVolumeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.media.VOLUME_CHANGED_ACTION".equals(action)) {
                    int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                    if (streamType != AudioManager.STREAM_MUSIC) {
                        return;
                    }
                    mHandler.removeCallbacks(mTtsSpeakRunnable);
                    mHandler.postDelayed(mTtsSpeakRunnable, TTS_VOLUME_UPDATE_DELAY_MS);
                } else if ("android.media.STREAM_MUTE_CHANGED_ACTION".equals(action)) {
                    int streamType = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1);
                    if (streamType != AudioManager.STREAM_MUSIC) {
                        return;
                    }
                    final boolean muted = intent.getBooleanExtra("android.media.EXTRA_STREAM_VOLUME_MUTED", false);
                    if (false == muted) {
                        mHandler.removeCallbacks(mTtsSpeakRunnable);
                        mHandler.postDelayed(mTtsSpeakRunnable, TTS_VOLUME_UPDATE_DELAY_MS);
                    }
                }
            }
        };
        final IntentFilter audFilter = new IntentFilter();
        audFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        audFilter.addAction("android.media.STREAM_MUTE_CHANGED_ACTION");
        registerReceiver(mVolumeReceiver, audFilter);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");

        // Reset flag of Force Revealing Password Prompt
        mIsForceRevealPwdPrompt = false;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTvPictureManager.is4K2KMode(true)) {
                    sendBroadcast(new Intent("enter.4k2k"));
                }
            }
        }, 1000);
        super.onResume();

        Log.d(TAG, "onResume() mScreenSaverStatus = " + mScreenSaverStatus);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mHandler.removeMessages(Constant.ROOTACTIVITY_DELAY_TO_CLOSE_DIALOG);
        }
        showPasswordLock(mIsInputBlocked || mIsToPromptPassword);

        if (!mIsSignalLock) {
            setAutoShutdownTime();
        }

        // get previous inputsource from preferences
        SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_INPUT_SOURCE,
                Context.MODE_PRIVATE);
        mPreviousInputSource = settings.getInt(Constant.PREFERENCES_PREVIOUS_INPUT_SOURCE,
                Utility.getDefaultInputSource());
        Log.d(TAG, "get previous input source from preference:" + mPreviousInputSource);

        mIskeyLocked = false;
        tvView.setBackgroundColor(Color.TRANSPARENT);
        mIsBackKeyPressed = false;
        mHandler.sendEmptyMessage(Constant.ROOTACTIVITY_RESUME_MESSAGE);

        mFreezeView
                .setVisibility((mTvPictureManager.isImageFreezed() == false) ? TextView.INVISIBLE
                        : TextView.VISIBLE);

        if (startPvr) {
            startPvr = false;
            Intent intent = new Intent(TvIntent.ACTION_PVR_ACTIVITY);
            intent.putExtra(Constant.PVR_CREATE_MODE, Constant.PVR_RECORD_START);
            if (intent.resolveActivity(RootActivity.this.getPackageManager()) != null) {
                RootActivity.this.startActivity(intent);
            }
        }
    }

    private void checkSetupWizardStatus() {
        // Start Autotuning if TvPlayer Never Launched
        SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
        if (false == settings.getBoolean(Constant.PREFERENCES_IS_AUTOSCAN_LAUNCHED, false)) {
            SharedPreferences.Editor editor = settings.edit();
            Log.d(TAG, "Launch TV First Time , Do AutoScan");
            if (TvCommonManager.TV_SYSTEM_ATSC != mTvSystem
                    && TvCommonManager.TV_SYSTEM_ISDB != mTvSystem) {
                if (TvCiManager.getInstance().isOpMode() == false) {
                    startSetupWizard();
                } else {
                    Log.d(TAG,
                            getResources().getString(R.string.str_op_forbid_channel_tuning_content));
                }
            } else {
                startSetupWizard();
            }
        } else {
            Log.d(TAG, "Not Launch TV First Time, No Needed To Do AutoScan");
        }
    }

    private void startSetupWizard() {
        Log.d(TAG, "startAutoTuning()");
        Intent intentSetupWizard = new Intent(TvIntent.ACTION_SETUP_WIZARD);
        if (intentSetupWizard.resolveActivity(getPackageManager()) != null) {
            startActivity(intentSetupWizard);
        }
    }

    private void startSetupLocationWizard() {
        Log.d(TAG, "startSetupLocationWizard()");
        Intent intent = new Intent(TvIntent.ACTION_SETUP_LOCATION_WIZARD);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void checkSystemAutoTime() {
        try {
            systemAutoTime = Settings.System
                    .getInt(getContentResolver(), Settings.Global.AUTO_TIME);
        } catch (SettingNotFoundException e) {
            systemAutoTime = 0;
        }

        if (systemAutoTime > 0) {
            int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                Settings.System.putInt(getContentResolver(), Settings.Global.AUTO_TIME, 0);
            }
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mHandler.removeMessages(Constant.ROOTACTIVITY_DELAY_TO_CLOSE_DIALOG);
        }
        isOnStop = true;
        isOnStart = false;
        sendBroadcast(new Intent(TV_APK_END));

        if (mCiPlusOPRefreshDialog != null && mCiPlusOPRefreshDialog.isShowing()) {
            mCiPlusOPRefreshDialog.dismiss();
        }

        if (mExitDialog != null) {
            mExitDialog.dismiss();
        }
        TvCecManager.getInstance().unregisterOnCecCtrlEventListener(mCecCtrlEventListener);
        mCecCtrlEventListener = null;

        TVRootApp app = (TVRootApp) rootActivity.getApplication();
        if (true == app.isPVREnable()) {
            if (TvPvrManager.getInstance().isAlwaysTimeShiftRecording()) {
                TvPvrManager.getInstance().stopAlwaysTimeShiftRecord();
            }
        }

        if ("kernel_mode".equals(SystemProperties.get("mstar.str.enable"))
                && !(mPowerManager.isScreenOn())) {
            /*
             * In kernel mode STR case, kernel driver resume will restore the
             * last input source, do not switch to storage here for speeding up
             * TV showing time when resuming.
             */
        } else {
            // switch input source to storage for releasing TV relative resrouce
            if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE) {
                Log.i(TAG, "onStop(): Switch input source to storage...");
                if (false == app.isPVREnable()
                        || false == TvPvrManager.getInstance().getIsBootByRecord()) {
                    TvCommonManager.getInstance().setInputSource(
                            TvCommonManager.INPUT_SOURCE_STORAGE);
                }
            }
        }

        /*
         * Constant.ROOTACTIVITY_RESUME_MESSAGE is not handled during STR power
         * off procedure. Jut to remove "no_change_source" extra to fix
         * mantis:1006626
         */
        getIntent().removeExtra("no_change_source");

        if (null != mVolumeReceiver) {
            unregisterReceiver(mVolumeReceiver);
            mVolumeReceiver = null;
        }
        mTtsSpeakRunnable = null;

        super.onStop();
    }

    private void updateTvSourceSignal() {
        int curInputSource = TvCommonManager.INPUT_SOURCE_NONE;
        curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        Log.i(TAG, "curInputSource is :" + curInputSource);
        boolean noChangeSource = getIntent().getBooleanExtra("no_change_source", false);
        getIntent().removeExtra("no_change_source");
        Log.d(TAG, "mIsMsrvStarted:" + mIsMsrvStarted);
        Log.d(TAG, "mIsPowerOn:" + mIsPowerOn);
        Log.d(TAG, "noChangeSource:" + noChangeSource);
        if (mIsMsrvStarted == true) {
            /**
             * If current inputsource is storage, it means apk resume from mm.
             * We need to change inputsource to previous tv inputsource.
             */
            if ((TvCommonManager.INPUT_SOURCE_STORAGE == curInputSource)
                    || (TvCommonManager.INPUT_SOURCE_NONE == curInputSource)) {
                if (!noChangeSource && !mIsPowerOn) {
                    new Thread(new Runnable() {
                        public void run() {
                            Log.d(TAG, "Change InputSource to previous :" + mPreviousInputSource);
                            TvCommonManager.getInstance().setInputSource(mPreviousInputSource);
                            TVRootApp.setSourceDirty(true);
                            if (mPreviousInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                                TvChannelManager.getInstance().changeToFirstService(
                                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                            } else if (mPreviousInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                                TvChannelManager.getInstance().changeToFirstService(
                                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                            }
                        }
                    }).start();
                    TvPictureManager.getInstance().setDynamicBackLightThreadSleep(false);
                }
            } else {
                /*
                 * The flow add here is for the purpose of acquiring tv resource
                 * control from reousrce manager. It will notify resource
                 * manager TvPlayer own the TV resource as this activity
                 * startup.
                 */
                if (mIsPowerOn == true) {
                    Log.d(TAG, "The source change to the previous power source :" + curInputSource);
                    // acquire resource control from resource manager
                    TvCommonManager.getInstance().setInputSource(curInputSource);
                }
                if (isFirstPowerOn == false) {
                    if (curInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                        startThread(true);
                    } else if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                        startThread(false);
                    }
                }
                isFirstPowerOn = false; // move here for first power on
            }
            int swMode = mTvChannelManager.getAtvChannelSwitchMode();
            if (swMode == TvChannelManager.CHANNEL_SWITCH_MODE_FREEZESCREEN) {
                mTvChannelManager.setChannelChangeFreezeMode(true);
            } else {
                mTvChannelManager.setChannelChangeFreezeMode(false);
            }
            mIsMsrvStarted = false;
        }
        curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        mIsActive = true;
        updateScreenSaver();
        if (caViewHolder.isCaEnable()) {
            caViewHolder.sendCaNotifyMsg(caCurEvent, caCurNotifyIdx, msgFromActivity);
        } else {
            caViewHolder.cancelAllCANotify();
        }
        if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_DTV);
        } else {
            mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_ATV);
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        if (0 != SystemProperties.getInt("mstar.str.suspending", 0)) {
            Log.i(TAG, "System is suspending");
            showNoSignalView(false);
        }

        if (null != mCecInfoDialog) {
            mHandler.removeMessages(Constant.ROOTACTIVITY_CANCEL_DIALOG);
            mCecInfoDialog.dismiss();
        }
        mCecInfoDialog = null;
        mIsCecDialogCanceled = false;

        mIsPowerOn = false;
        if (getIntent() != null) {
            getIntent().removeExtra("isPowerOn");
        }

        if (mCcKeyToast != null) {
            mCcKeyToast.cancel();
        }
        if (true == mIsExiting) {
            Log.i(TAG, "Exiting, prepare to change souce");
            mIsExiting = false;
        }
        mIsActive = false;
        TVRootApp.setSourceDirty(false);

        int curInputSource = TvCommonManager.INPUT_SOURCE_NONE;
        curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();

        /**
         * If user switch between tv and launch too fast, the currentInputSource
         * had not changed from storage to last input source. Don't need to
         * update mPreviousInputSource
         */
        if ((TvCommonManager.INPUT_SOURCE_STORAGE != curInputSource)
                && (TvCommonManager.INPUT_SOURCE_NONE != curInputSource)) {
            mPreviousInputSource = curInputSource;
            Log.d(TAG, "Save previous inputsource :" + mPreviousInputSource);
            SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_INPUT_SOURCE,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Constant.PREFERENCES_PREVIOUS_INPUT_SOURCE, mPreviousInputSource);
            editor.commit();
        }

        if (mExitDialog != null) {
            mExitDialog.dismiss();
        }
        mAutoShutdownHandler.removeCallbacks(mShutdownTask);

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            /**
             * FIXME: This is a temporary solution, finding the right solution is necessary.
             *
             * After fixing mantis 1182532, Jacky reports a new issue, the password dialog flashes in sometime.
             * Use cause:
             *  1. In ATSC, block unrated TV is set as On
             *  2. After switching to a new channel, RootActivity shows the dialog after reveiving screen update with DTV_SS_PARENTAL_BLOCK
             *     => This is normal behavior, DTV_SS_PARENTAL_BLOCK is the default status before channel ratting is detected.
             *  3. And then SourceInfoActvivity will be started
             *  4. RootActivity enters the onPause state and close password dialog
             *  5. SourceInfoActvivity shows and broadcasts a new notification to RootActivity to check parental status, password dialog will be displayed again
             *  6. Users see the password dialog flashed in a short time
             *
             * To solve the problem, sending a delay message here to close dialog. If SourceInfoActvivity is started after the message sent,
             * the message will be removed when handling the broadcasts and the password dialog displays correctly.
             *
             * mantis:1182532
             */
            mHandler.removeMessages(Constant.ROOTACTIVITY_DELAY_TO_CLOSE_DIALOG);
            mHandler.sendEmptyMessageDelayed(Constant.ROOTACTIVITY_DELAY_TO_CLOSE_DIALOG, ATSC_CLOSE_DIALOG_DELAY_TIME_200MS);
        } else {
            closeDialogs();
        }

        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart()");
        isFirstPowerOn = true;
        mIsMsrvStarted = true;
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");

        if (mCiPlusOPRefreshDialog != null && mCiPlusOPRefreshDialog.isShowing()) {
            mCiPlusOPRefreshDialog.dismiss();
        }

        if (mExitDialog != null && mExitDialog.isShowing()) {
            mExitDialog.dismiss();
        }
        if (systemAutoTime > 0) {
            int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                Settings.System.putInt(getContentResolver(), Settings.Global.AUTO_TIME,
                        systemAutoTime);
            }
        }

        TvPictureManager.getInstance().setDynamicBackLightThreadSleep(true);
        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mAtvPlayerEventListener = null;
        TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mDtvPlayerEventListener = null;
        TvChannelManager.getInstance().unregisterOnTvPlayerEventListener(mTvPlayerEventListener);
        mTvPlayerEventListener = null;
        TvCommonManager.getInstance().unregisterOnTvCommonEventHandler(mTvCommonEventHandler);
        mTvCommonEventHandler = null;
        TvTimerManager.getInstance().unregisterOnEpgTimerEventListener(mEpgTimerEventListener);
        mEpgTimerEventListener = null;
        TvTimerManager.getInstance().unregisterOnPvrTimerEventListener(mPvrTimerEventListener);
        mPvrTimerEventListener = null;
        TvCaManager.getInstance().unregisterOnCaEventListener(mCaEventListener);
        mCaEventListener = null;
        TvCiManager.getInstance().unregisterOnUiEventListener(mUiEventListener);
        mUiEventListener = null;
        TvCiManager.getInstance().unregisterOnCiStatusChangeEventListener(
                mCiStatusChangeEventListener);
        mCiStatusChangeEventListener = null;
        TvCiManager.getInstance().unregisterOnCiAsyncCmdNotifyListener(mCiCmdNotifyListener);
        mCiCmdNotifyListener = null;
        TvTimerManager.getInstance().unregisterOnOadTimerEventListener(mOadTimerEventListener);
        mOadTimerEventListener = null;
        TvChannelManager.getInstance().unregisterOnInputSourceEventListener(
                mInputSourceEventListener);
        mInputSourceEventListener = null;
        TvChannelManager.getInstance().unregisterOnSignalEventListener(mSignalEventListener);
        mSignalEventListener = null;
        TvChannelManager.getInstance().unregisterOnEwsEventListener(mEwsEventListener);
        mEwsEventListener = null;
        TvCommonManager.getInstance().unregisterOnResourceEventListener(
                mResourceEventListener);
        mResourceEventListener = null;

        unregisterReceiver(mReceiver);
        mIsActive = false;
        TvCaManager.getInstance().setCurrentEvent(caCurEvent);
        TvCaManager.getInstance().setCurrentMsgType(caCurNotifyIdx);
        TVRootApp.setSourceDirty(false);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        executePreviousTask(intent);
        setIntent(intent);
        boolean isPVRStandby = intent.getBooleanExtra("isPVRStandby", false);
        Log.e(TAG, "onNewIntent isPVRStandby=" + isPVRStandby);
        if (isPVRStandby) {
            standbyHandler.sendEmptyMessageDelayed(100, 5000);
        }

        /*
         * In case RootActivity under onStop() stage, we have to handle input
         * source change intent here
         */
        if (getIntent() != null && getIntent().getAction() != null) {
            int isdbAntennaType = getIntent().getIntExtra("inputAntennaType",
                    TvIsdbChannelManager.DTV_ANTENNA_TYPE_NONE);
            if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_NONE != isdbAntennaType) {
                if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem)
                    startISDBAntennaTypeChange(isdbAntennaType);
            }

            if (getIntent().hasExtra("inputSrc")) {
                /*
                 * clear screen saver status after changing input source and
                 * hide NoSignalView before change input source.
                 */
                mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
                mIsInputBlocked = false;
                showNoSignalView(false);
                int inputsource = getIntent().getIntExtra("inputSrc",
                        Utility.getDefaultInputSource());
                Log.i(TAG, "onNewIntent:inputsource = " + inputsource);
                startSourceChange(inputsource);
            }
        }
    }

    private void executePreviousTask(final Intent paramIntent) {
        if (paramIntent != null) {
            String taskTag = paramIntent.getStringExtra("task_tag");
            if ("input_source_changed".equals(taskTag)) {
                TVRootApp.setSourceDirty(true);
            }
        }
    }

    /**
     * handle the up, down, return and 0-9 key
     *
     * @param keyCode
     * @return
     */
    public boolean onChannelChange(int keyCode) {
        if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV
                || TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            if (keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                Utility.channelDown(this);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_CHANNEL_UP
                    || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                Utility.channelUp(this);
                return true;
            } else if (keyCode == MKeyEvent.KEYCODE_CHANNEL_RETURN) {
                mTvCommonManager.speakTtsDelayed(
                    getString(R.string.str_channel_return)
                    , TvCommonManager.TTS_QUEUE_FLUSH
                    , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
                    , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
                Utility.channelReturn(this);
                return true;
            }
            switch (keyCode) {
                case KeyEvent.KEYCODE_0:
                case KeyEvent.KEYCODE_1:
                case KeyEvent.KEYCODE_2:
                case KeyEvent.KEYCODE_3:
                case KeyEvent.KEYCODE_4:
                case KeyEvent.KEYCODE_5:
                case KeyEvent.KEYCODE_6:
                case KeyEvent.KEYCODE_7:
                case KeyEvent.KEYCODE_8:
                case KeyEvent.KEYCODE_9:
                case MKeyEvent.KEYCODE_SUBTITLE:
                    Intent intentChannelControl = new Intent(this, ChannelControlActivity.class);
                    intentChannelControl.putExtra("KeyCode", keyCode);
                    this.startActivity(intentChannelControl);
                    return true;
            }
        }
        return false;
    }

    static boolean canRepeatKey = true;

    static int preKeyCode = KeyEvent.KEYCODE_UNKNOWN;

    Handler mRepeatHandler = new Handler();

    Runnable mRepeatRun = new Runnable() {
        public void run() {
            canRepeatKey = true;
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (sendGingaKey(keyCode, event)) {
            Log.i(TAG, "onKeyUp:sendGingaKey success!");
            return true;
        }

        if (mTvMhlManager.CbusStatus() == true && mTvMhlManager.IsMhlPortInUse() == true) {
            if (mTvMhlManager.IRKeyProcess(keyCode, true) == true) {
                SystemClock.sleep(140);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setAutoShutdownTime();

        if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
            if (mIsEasShow == true) {
                Log.w(TAG, "onKeyDown: user input blocked in showing EAS inforations");
                return true;
            }
        }

        final boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
        if (down && (keyCode == KeyEvent.KEYCODE_M || keyCode == KeyEvent.KEYCODE_MENU)) {

            if (!canRepeatKey && !(preKeyCode == keyCode)) {
                preKeyCode = keyCode;
            } else {
                preKeyCode = keyCode;
                mRepeatHandler.removeCallbacks(mRepeatRun);
                canRepeatKey = false;
                mRepeatHandler.postDelayed(mRepeatRun, 2000);
            }
        }

        // arrange CEC key
        if (sendCecKey(keyCode)) {
            Log.i(TAG, "onKeyDown:sendCecKey success!");
            return true;
        }

        // arrange Mheg5 key
        if (sendMheg5Key(keyCode)) {
            Log.i(TAG, "onKeyDown:sendMhegKey success!");
            return true;
        }

        // arrange HbbTV key
        if (sendHbbTVKey(keyCode)) {
            Log.i(TAG, "onKeyDown:sendHbbTVKey success!");
            return true;
        }

        // arrange Ginga key
        if (sendGingaKey(keyCode, event)) {
            Log.i(TAG, "onKeyDown:sendGingaKey success!");
            return true;
        }

        if (mTvMhlManager.CbusStatus() == true && mTvMhlManager.IsMhlPortInUse() == true) {
            if (mTvMhlManager.IRKeyProcess(keyCode, false) == true) {
                SystemClock.sleep(140);
                return true;
            }
        }

        if (!Constant.lockKey && keyCode != KeyEvent.KEYCODE_BACK) {
            return true;
        }
        if (_3Dflag) {
        }
        Log.d(TAG, " onKeyDown  keyCode = " + keyCode);
        if (mTvPictureManager.isImageFreezed() == true) {
            mTvPictureManager.unFreezeImage();
            mFreezeView.setVisibility(TextView.INVISIBLE);
            mTvCommonManager.speakTtsDelayed(
                getString(R.string.str_unfreeze)
                , TvCommonManager.TTS_QUEUE_FLUSH
                , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
                , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
            if (MKeyEvent.KEYCODE_FREEZE == keyCode) {
                return true;
            }
        }
        switch (keyCode) {
            case MKeyEvent.KEYCODE_CC:
                boolean bIsCcSupported = false;
                if (true == Utility.isATSC()) {
                    if (TvCommonManager.getInstance().isSupportModule(
                            TvCommonManager.MODULE_ATSC_CC_ENABLE)
                            || TvCommonManager.getInstance().isSupportModule(
                                    TvCommonManager.MODULE_NTSC_CC_ENABLE)) {
                        // ATSC TV System using [DTV]MODULE_ATSC_CC_ENABLE +
                        // [ATV]MODULE_NTSC_CC_ENABLE
                        bIsCcSupported = true;
                    }
                } else if (true == Utility.isISDB()) {
                    if (TvCommonManager.getInstance().isSupportModule(
                            TvCommonManager.MODULE_ISDB_CC_ENABLE)
                            || TvCommonManager.getInstance().isSupportModule(
                                    TvCommonManager.MODULE_NTSC_CC_ENABLE)) {
                        // ISDB TV System using [DTV]MODULE_ISDB_CC_ENABLE +
                        // [ATV]MODULE_NTSC_CC_ENABLE
                        bIsCcSupported = true;
                    }
                } else {
                    if (TvCommonManager.getInstance().isSupportModule(
                            TvCommonManager.MODULE_NTSC_CC_ENABLE)) {
                        // ASIA CC = [ATV]MODULE_NTSC_CC_ENABLE
                        bIsCcSupported = true;
                    }
                }

                if (true == bIsCcSupported) {
                    if (mCcKeyToast == null) {
                        mCcKeyToast = new Toast(this);
                        mCcKeyToast.setGravity(Gravity.CENTER, 0, 0);
                    }
                    TextView tv = new TextView(RootActivity.this);
                    tv.setTextSize(Constant.CCKEY_TEXTSIZE);
                    tv.setTextColor(Color.WHITE);
                    tv.setAlpha(Constant.CCKEY_ALPHA);
                    mCcKeyToast.setView(tv);
                    mCcKeyToast.setDuration(Toast.LENGTH_SHORT);

                    int closedCaptionMode = TvCcManager.getInstance().getNextClosedCaptionMode();
                    TvCcManager.getInstance().setClosedCaptionMode(closedCaptionMode);
                    TvCcManager.getInstance().stopCc();
                    if (TvCcManager.CLOSED_CAPTION_OFF != closedCaptionMode) {
                        TvCcManager.getInstance().startCc();
                    }
                    String[] closedCaptionStrings = getResources().getStringArray(
                            R.array.str_arr_option_closed_caption);
                    if (0 <= closedCaptionMode && closedCaptionStrings.length > closedCaptionMode) {
                        String str = getResources().getString(R.string.str_option_caption) + " "
                                + closedCaptionStrings[closedCaptionMode];
                        tv.setText(str);
                        TvCommonManager.getInstance().stopTts();
                        TvCommonManager.getInstance().speakTtsDelayed(
                            str
                            , TvCommonManager.TTS_QUEUE_FLUSH
                            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
                            , TvCommonManager.TTS_DELAY_TIME_100MS);
                    }
                    mCcKeyToast.show();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
                if (deviceName.equals("MStar Smart TV IR Receiver")
                        || deviceName.equals("MStar Smart TV Keypad")) {
                    AudioManager audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    /*
                     * Adjust the volume in on key down since it is more
                     * responsive to the user.
                     */
                    if (audiomanager != null) {
                        int flags = AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_VIBRATE;
                        audiomanager.adjustVolume(
                                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ? AudioManager.ADJUST_RAISE
                                        : AudioManager.ADJUST_LOWER, flags);
                    }
                } else {
                    Log.d(TAG, "deviceName is:" + deviceName);
                }
                break;
            case KeyEvent.KEYCODE_PROG_YELLOW:
            case KeyEvent.KEYCODE_PROG_BLUE:
            case KeyEvent.KEYCODE_PROG_GREEN:
            case KeyEvent.KEYCODE_PROG_RED:
            case KeyEvent.KEYCODE_ENTER:
                if (caViewHolder.isCaEnable()) {
                    Intent intent = new Intent();
                    intent.setClass(this, EmailListActivity.class);
                    RootActivity.this.startActivity(intent);
                    break;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if (mIsBackKeyPressed == false) {
                    mIsActive = false;
                    mIsBackKeyPressed = true;
                    if (!mIskeyLocked) {
                        mIskeyLocked = true;
                        showExitDialog();
                    }
                }
                return true;
            case MKeyEvent.KEYCODE_FREEZE:
                if (mTvPictureManager.isImageFreezed() == false) {
                    mTvPictureManager.freezeImage();
                    mFreezeView.setVisibility(TextView.VISIBLE);
                    mTvCommonManager.speakTtsDelayed(
                        getString(R.string.str_freeze)
                        , TvCommonManager.TTS_QUEUE_FLUSH
                        , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
                        , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
                }
                return true;
            case KeyEvent.KEYCODE_M:
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        if (!mIskeyLocked && SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
            mIskeyLocked = true;
            Log.d(TAG, "onKeyDown->goToMenuPage  keyCode = " + keyCode);
            return true;
        } else if (SwitchPageHelper.goToSourceInfo(this, keyCode) == true) {
            /**
             * Keep the the password prompt show if exists
             */
            mIsForceRevealPwdPrompt = true;
            return true;
        } else if (SwitchPageHelper.goToEpgPage(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToPvrPage(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToPvrBrowserPage(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToSubtitleLangPage(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToAudioLangPage(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToProgrameListInfo(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToFavorateListInfo(this, keyCode) == true) {
            return true;
        } else if (SwitchPageHelper.goToTeletextPage(this, keyCode) == true) {
            return true;
        } else if (onChannelChange(keyCode) == true) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createSurfaceView() {
        tvView = (TvView) findViewById(R.id.tranplentview);

        Log.d(TAG, "mIsPowerOn = " + mIsPowerOn);
        String taskTag = getIntent().getStringExtra("task_tag");
        // true means don't set window size which will cause black screen
        tvView.openView(mIsPowerOn || "input_source_changed".equals(taskTag));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case Constant.CHANNEL_LOCK_RESULT_CODE:
                if (data.getExtras().getBoolean("result")) {
                    mSignalStatusView.setVisibility(TextView.INVISIBLE);
                    mSignalStatusView4Pop.setVisibility(TextView.INVISIBLE);
                }
                break;
            default:
                Log.w(TAG, "onActivityResult resultCode not match");
                break;
        }
    }

    /*
     * update NoSignalView Text String by Screen Saver Status and input source.
     */
    private Handler signalLockHandler = new Handler() {
        public void handleMessage(Message msg) {
            int lockStatus = msg.arg1;
            if (TvEvent.SIGNAL_LOCK == lockStatus) {
                mIsSignalLock = true;
            } else if (TvEvent.SIGNAL_UNLOCK == lockStatus) {
                mIsSignalLock = false;
            } else if (TvEvent.SIGNAL_UNSTABLE == lockStatus) {
                mIsSignalLock = false;
            }
            Log.d(TAG, "signalLockHandler, mIsInputBlocked = " + mIsInputBlocked);
            if (true == mIsInputBlocked) {
                mIsScreenSaverShown = true;
                mSignalStatusView.setText(R.string.str_input_block);
                // mIsToPromptPassword = true;
                if (true == mIsActive) {
                    showPasswordLock(true);
                }
                startNoSignal();
                return;
            }
            int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            mIsToPromptPassword = false;
            TVRootApp.setSignalFormatSupport(true);

            Log.d(TAG, "signalLockHandler, current InputSource = " + curInputSource);
            Log.d(TAG, "signalLockHandler, lockStatus = " + lockStatus);
            Log.d(TAG, "signalLockHandler, mIsSignalLock = " + mIsSignalLock);
            Log.d(TAG, "signalLockHandler, mScreenSaverStatus = " + mScreenSaverStatus);

            if (TvEvent.SIGNAL_UNSTABLE == lockStatus) {
                /*
                 * Signal is in an unstable status, reset status.
                 */
                mTtsNoSignalViewStr = "";
                mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
                mSignalStatusView.setText(R.string.str_no_signal);
                mIsScreenSaverShown = false;
            } else if (mIsSignalLock) {
                /*
                 * send broadcast to those who need to know signal lock status.
                 * ex: SourceInfoActivity will get this intent when it is alive,
                 * and update source info content.
                 */
                sendBroadcast(new Intent(TvIntent.ACTION_SIGNAL_LOCK));
                mAutoShutdownHandler.removeCallbacks(mShutdownTask);

                switch (curInputSource) {
                    case TvCommonManager.INPUT_SOURCE_DTV: {
                        /* show SourceInfo before updating NoSignalView */
                        Utility.startSourceInfo(RootActivity.this);
                        /* update NoSignalView string by screen saver status */
                        if (ScreenSaverMode.DTV_SS_INVALID_SERVICE == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_invalid_service);
                        } else if (ScreenSaverMode.DTV_SS_NO_CI_MODULE == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_no_ci_module);
                        } else if (ScreenSaverMode.DTV_SS_CI_PLUS_AUTHENTICATION == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_ci_plus_authentication);
                        } else if (ScreenSaverMode.DTV_SS_SCRAMBLED_PROGRAM == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_scrambled_program);
                        } else if (ScreenSaverMode.DTV_SS_UNSUPPORTED_FORMAT == mScreenSaverStatus) {
                            /*
                             * FIXME: atsc screen saver status is separate by
                             * flag in supernova enum of screen saver should be
                             * sync between each tv system.
                             */
                            if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
                                mSignalStatusView.setText(R.string.str_no_channel_banner);
                            } else {
                                TVRootApp.setSignalFormatSupport(false);
                                mSignalStatusView.setText(R.string.str_unsupported);
                            }
                        } else if (ScreenSaverMode.DTV_SS_CH_BLOCK == mScreenSaverStatus) {
                            mIsToPromptPassword = true;
                            mSignalStatusView.setText(R.string.str_channel_block);
                        } else if (ScreenSaverMode.DTV_SS_PARENTAL_BLOCK == mScreenSaverStatus) {
                            mIsToPromptPassword = true;
                            mSignalStatusView.setText(mStrParentalLockPromptMessage);
                        } else if (ScreenSaverMode.DTV_SS_AUDIO_ONLY == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_audio_only);
                        } else if (ScreenSaverMode.DTV_SS_DATA_ONLY == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_data_only);
                        } else if (ScreenSaverMode.DTV_SS_COMMON_VIDEO == mScreenSaverStatus) {
                            // Reset NoSignalView string to default string : No
                            // Signal
                            mIsToPromptPassword = false;
                            showPasswordLock(false);
                            mSignalStatusView.setText(R.string.str_no_signal);
                        } else if (ScreenSaverMode.DTV_SS_INVALID_PMT == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_invalid_pmt);
                        } else if (SCREENSAVER_DEFAULT_STATUS == mScreenSaverStatus) {
                            /*
                             * Status fall into a default value case
                             * (SCREENSAVER_DEFAULT_STATUS) skip updating
                             * NoSignalView, it will be updated when
                             * ScreenSaverStatus be updated.
                             */
                            if (TvPvrManager.getInstance().isPlaybacking()) {
                                /*
                                 * There is not EV_SCREEN_SAVER_MODE update
                                 * notification in PVR playing.
                                 */
                                mIsScreenSaverShown = false;
                                showNoSignalView(false);
                                break;
                            }
                            Log.i(TAG,
                                    "Default ScreenSaver status, wait screenSaverHandler updating NoSignalView.");
                            break;
                        } else {
                            Log.w(TAG, "Current Screen Saver Status is unrecognized");
                            Log.w(TAG, "status: " + mScreenSaverStatus);
                            break;
                        }
                        Log.d(TAG, "screen saver status is " + mScreenSaverStatus);
                        /*
                         * update NoSignalView Visibility by signal lock flag
                         * and screen saver status
                         */
                        if (ScreenSaverMode.DTV_SS_COMMON_VIDEO != mScreenSaverStatus) {
                            mIsScreenSaverShown = true;
                            showNoSignalView(true);
                        } else {
                            mIsScreenSaverShown = false;
                            showNoSignalView(false);
                        }
                        break;
                    }
                    case TvCommonManager.INPUT_SOURCE_HDMI:
                    case TvCommonManager.INPUT_SOURCE_HDMI2:
                    case TvCommonManager.INPUT_SOURCE_HDMI3:
                    case TvCommonManager.INPUT_SOURCE_HDMI4:
                        if ((SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == mScreenSaverStatus)
                                || (SignalProgSyncStatus.UNSTABLE == mScreenSaverStatus)) {
                            TVRootApp.setSignalFormatSupport(false);
                            mSignalStatusView.setText(R.string.str_unsupported);
                            mIsScreenSaverShown = true;
                            showNoSignalView(true);
                        } else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == mScreenSaverStatus) {
                            mIsScreenSaverShown = false;
                            showNoSignalView(false);
                        } else if (SCREENSAVER_DEFAULT_STATUS == mScreenSaverStatus) {
                            mIsScreenSaverShown = false;
                            showNoSignalView(false);
                        }
                        Utility.startSourceInfo(RootActivity.this);
                        break;
                    case TvCommonManager.INPUT_SOURCE_VGA:
                    case TvCommonManager.INPUT_SOURCE_VGA2:
                    case TvCommonManager.INPUT_SOURCE_VGA3:
                        if ((SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == mScreenSaverStatus)
                                || (SignalProgSyncStatus.UNSTABLE == mScreenSaverStatus)) {
                            TVRootApp.setSignalFormatSupport(false);
                            mSignalStatusView.setText(R.string.str_unsupported);
                            mIsScreenSaverShown = true;
                            showNoSignalView(true);
                        } else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == mScreenSaverStatus) {
                            mIsScreenSaverShown = false;
                            showNoSignalView(false);
                            Utility.startSourceInfo(RootActivity.this);
                        } else if (SignalProgSyncStatus.AUTO_ADJUST == mScreenSaverStatus) {
                            mSignalStatusView.setText(R.string.str_auto_adjust);
                            mIsScreenSaverShown = true;
                            showNoSignalView(true);
                            signalLockHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mIsScreenSaverShown = false;
                                    showNoSignalView(false);
                                    Utility.startSourceInfo(RootActivity.this);
                                }
                            }, 3000);
                        }
                        break;
                    case TvCommonManager.INPUT_SOURCE_CVBS:
                    case TvCommonManager.INPUT_SOURCE_CVBS2:
                    case TvCommonManager.INPUT_SOURCE_CVBS3:
                    case TvCommonManager.INPUT_SOURCE_CVBS4:
                        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                            boolean isSystemLocked = false;
                            isSystemLocked = TvAtscChannelManager.getInstance()
                                    .getCurrentVChipBlockStatus();
                            if (true == isSystemLocked) {
                                mSignalStatusView.setText(mStrParentalLockPromptMessage);
                                mIsToPromptPassword = true;
                                mIsScreenSaverShown = true;
                                showNoSignalView(true);
                                Utility.startSourceInfo(RootActivity.this);
                            } else {
                                mIsToPromptPassword = false;
                                mIsScreenSaverShown = false;
                                showNoSignalView(false);
                                Utility.startSourceInfo(RootActivity.this);
                            }
                        } else {
                            mIsScreenSaverShown = false;
                            showNoSignalView(false);
                            Utility.startSourceInfo(RootActivity.this);
                        }
                        break;
                    case TvCommonManager.INPUT_SOURCE_YPBPR:
                    case TvCommonManager.INPUT_SOURCE_YPBPR2:
                    case TvCommonManager.INPUT_SOURCE_YPBPR3:
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                        Utility.startSourceInfo(RootActivity.this);
                        break;
                    case TvCommonManager.INPUT_SOURCE_STORAGE:
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                        break;
                    default:
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                        Utility.startSourceInfo(RootActivity.this);
                        break;
                }
            } else {
                // signal unlock case
                switch (curInputSource) {
                    case TvCommonManager.INPUT_SOURCE_ATV:
                        // atv would not show nosignal text even signal unlock.
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                        mIsSignalLock = true;
                        mAutoShutdownHandler.removeCallbacks(mShutdownTask);
                        return;
                    default:
                        showPasswordLock(false);
                        mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
                        mSignalStatusView.setText(R.string.str_no_signal);
                        Log.d(TAG, "unlock show nosignal");
                        mIsScreenSaverShown = true;
                        showNoSignalView(true);
                        setAutoShutdownTime();
                }
            }
        };
    };

    /**
     * Used to handle scrren saver mode, decide if we need to show NoSignal
     * Text. Each inputsource will have itself situation. Status can be
     * referenced in two enum: EnumScreenMode, EnumSignalProgSyncStatus.
     */
    private Handler screenSaverHandler = new Handler() {
        public void handleMessage(Message msg) {
            int curInputSource;
            int status = msg.arg1;

            if (status != mScreenSaverStatus) {
                mScreenSaverStatus = status;
                Log.d(TAG, "screenSaverHandler, mScreenSaverStatus = " + status + " (updated!)");
            }

            // FIXME: should add new method to check main,sub,current input
            // source
            if (TvPipPopManager.getInstance().isPipModeEnabled()) {
                curInputSource = TvCommonManager.getInstance().getCurrentTvMainInputSource();
            } else {
                curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
            }
            Log.d(TAG, "screenSaverHandler, curInputSource = " + curInputSource);
            Log.d(TAG, "screenSaverHandler, mScreenSaverStatus = " + status);
            Log.d(TAG, "screenSaverHandler, mIsInputBlocked = " + mIsInputBlocked);
            Log.d(TAG, "screenSaverHandler, mIsSignalLock = " + mIsSignalLock);

            if (true == mIsInputBlocked) {
                mIsScreenSaverShown = true;
                mSignalStatusView.setText(R.string.str_input_block);
                // mIsToPromptPassword = true;
                if (true == mIsActive) {
                    showPasswordLock(true);
                }
                startNoSignal();
                return;
            }
            // FIXME: check consistency between program lock status and activity
            // visibility.
            // FIXME: activity should receive a event from tvservice to know
            // program lock status changed.
            if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                if (false == TvAtscChannelManager.getInstance().getCurrentVChipBlockStatus()
                        && false == mTvChannelManager.getCurrentProgramInfo().isLock) {
                    mIsToPromptPassword = false;
                    showPasswordLock(false);
                }
            }
            TVRootApp.setSignalFormatSupport(true);
            switch (curInputSource) {
                case TvCommonManager.INPUT_SOURCE_ATV:
                case TvCommonManager.INPUT_SOURCE_DTV:
                    if (TvCommonManager.INPUT_SOURCE_ATV == curInputSource) {
                        if (Utility.isATSC()) {
                            if (AtscAtvScreenSaverMode.ATV_SS_NORMAL == status) {
                                mIsScreenSaverShown = false;
                                mSignalStatusView.setText(R.string.str_no_signal);
                                mIsToPromptPassword = false;
                                showPasswordLock(false);
                                break;
                            } else if (AtscAtvScreenSaverMode.ATV_SS_NO_CHANNEL == status) {
                                mIsScreenSaverShown = true;
                                mSignalStatusView.setText(R.string.str_no_channel_banner);
                                break;
                            }
                        }
                    }
                    /* update NoSignalView string by screen saver status */
                    if (ScreenSaverMode.DTV_SS_INVALID_SERVICE == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_invalid_service);
                    } else if (ScreenSaverMode.DTV_SS_NO_CI_MODULE == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_no_ci_module);
                    } else if (ScreenSaverMode.DTV_SS_CI_PLUS_AUTHENTICATION == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_ci_plus_authentication);
                    } else if (ScreenSaverMode.DTV_SS_SCRAMBLED_PROGRAM == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_scrambled_program);
                    } else if (ScreenSaverMode.DTV_SS_UNSUPPORTED_FORMAT == status) {
                        /*
                         * FIXME: atsc screen saver status is separate by flag
                         * in supernova enum of screen saver should be sync
                         * between each tv system.
                         */
                        mIsScreenSaverShown = true;
                        if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
                            mSignalStatusView.setText(R.string.str_no_channel_banner);
                        } else {
                            TVRootApp.setSignalFormatSupport(false);
                            mSignalStatusView.setText(R.string.str_unsupported);
                        }
                    } else if (ScreenSaverMode.DTV_SS_CH_BLOCK == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_channel_block);
                        mIsToPromptPassword = true;
                        if ((true == mIsActive) || (true == mIsForceRevealPwdPrompt)) {
                            showPasswordLock(true);
                        }
                    } else if (ScreenSaverMode.DTV_SS_PARENTAL_BLOCK == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(mStrParentalLockPromptMessage);
                        mIsToPromptPassword = true;
                        if (true == mIsActive) {
                            showPasswordLock(true);
                        }
                    } else if (ScreenSaverMode.DTV_SS_AUDIO_ONLY == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_audio_only);
                    } else if (ScreenSaverMode.DTV_SS_DATA_ONLY == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_data_only);
                    } else if (ScreenSaverMode.DTV_SS_COMMON_VIDEO == status) {
                        // Reset NoSignalView string to default string : No
                        // Signal
                        mIsScreenSaverShown = false;
                        mSignalStatusView.setText(R.string.str_no_signal);
                        mIsToPromptPassword = false;
                        showPasswordLock(false);
                    } else if (ScreenSaverMode.DTV_SS_INVALID_PMT == status) {
                        mIsScreenSaverShown = true;
                        mSignalStatusView.setText(R.string.str_invalid_pmt);
                    } else if (SCREENSAVER_DEFAULT_STATUS == mScreenSaverStatus) {
                        /*
                         * Status fall into a default value case
                         * (SCREENSAVER_DEFAULT_STATUS) skip updating
                         * NoSignalView, it will be updated when
                         * ScreenSaverStatus be updated.
                         *
                         * This is the special case to handle
                         * SCREENSAVER_DEFAULT_STATUS here, mScreenSaverStatus may
                         * be set as SCREENSAVER_DEFAULT_STATUS in mLocalBroadcastReveiver.
                         */
                        mIsScreenSaverShown = false;
                        /*
                         * Reset NoSignalView string to default string : No
                         * Signal
                         */
                        mSignalStatusView.setText(R.string.str_no_signal);
                    }
                    break;
                case TvCommonManager.INPUT_SOURCE_VGA:
                case TvCommonManager.INPUT_SOURCE_VGA2:
                case TvCommonManager.INPUT_SOURCE_VGA3:
                    if (SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == status) {
                        TVRootApp.setSignalFormatSupport(false);
                        mSignalStatusView.setText(R.string.str_unsupported);
                        mIsScreenSaverShown = true;
                        showNoSignalView(true);
                    } else if (SignalProgSyncStatus.AUTO_ADJUST == status) {
                        mSignalStatusView.setText(R.string.str_auto_adjust);
                        mIsScreenSaverShown = true;
                        showNoSignalView(true);
                        screenSaverHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mIsScreenSaverShown = false;
                                showNoSignalView(false);
                                Utility.startSourceInfo(RootActivity.this);
                            }
                        }, 3000);
                    } else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == status) {
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                        Utility.startSourceInfo(RootActivity.this);
                    }
                    break;
                case TvCommonManager.INPUT_SOURCE_CVBS:
                case TvCommonManager.INPUT_SOURCE_CVBS2:
                case TvCommonManager.INPUT_SOURCE_CVBS3:
                case TvCommonManager.INPUT_SOURCE_CVBS4:
                    if (ScreenSaverMode.DTV_SS_PARENTAL_BLOCK == mScreenSaverStatus) {
                        mSignalStatusView.setText(mStrParentalLockPromptMessage);
                        if (true == mIsActive) {
                            showPasswordLock(true);
                        }
                        mIsScreenSaverShown = true;
                        showNoSignalView(true);
                    } else if (ScreenSaverMode.DTV_SS_COMMON_VIDEO == mScreenSaverStatus) {
                        mSignalStatusView.setText(R.string.str_no_signal);
                        showPasswordLock(false);
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                    }
                    break;
                case TvCommonManager.INPUT_SOURCE_YPBPR:
                case TvCommonManager.INPUT_SOURCE_YPBPR2:
                case TvCommonManager.INPUT_SOURCE_YPBPR3:
                    if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == status) {
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                    } else if (SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == status) {
                        mSignalStatusView.setText(R.string.str_unsupported);
                        mIsScreenSaverShown = true;
                        showNoSignalView(true);
                    }
                    break;
                case TvCommonManager.INPUT_SOURCE_HDMI:
                case TvCommonManager.INPUT_SOURCE_HDMI2:
                case TvCommonManager.INPUT_SOURCE_HDMI3:
                case TvCommonManager.INPUT_SOURCE_HDMI4:
                    if ((SignalProgSyncStatus.UNSTABLE == status)
                            || (SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == status)) {
                        mSignalStatusView.setText(R.string.str_unsupported);
                        mIsScreenSaverShown = true;
                        showNoSignalView(true);
                    } else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == status) {
                        mIsScreenSaverShown = false;
                        showNoSignalView(false);
                    }
                    break;
                default:
                    break;
            }

            startNoSignal();
        };
    };

    private void tvApkExitHandler() {
        mIsActive = false;
        bCmd_TvApkExit = true;
        mIsBackKeyPressed = true;
        showExitDialog();
        return;
    }

    private class CecCtrlEventListener implements OnCecCtrlEventListener {
        @Override
        public boolean onCecCtrlEvent(int what, int arg1, int arg2) {
            switch (what) {
                case TvCecManager.TVCEC_STANDBY: {
                    Log.i(TAG, "EV_CEC_STANDBY");
                    TvCommonManager.getInstance().standbySystem("cec");
                }
                    break;
                case TvCecManager.TVCEC_SET_MENU_LANGUAGE: {
                    Log.i(TAG, "EV_CEC_SET_MENU_LANGUAGE");
                    try {
                        IActivityManager am = ActivityManagerNative.getDefault();
                        Configuration config = am.getConfiguration();
                        config.locale = TvLanguage.getLocale(arg1, config.locale);

                        // indicate this isn't some passing default - the user
                        // wants
                        // this remembered
                        config.userSetLocale = true;
                        am.updateConfiguration(config);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                    break;
                case TvCecManager.TVCEC_SOURCE_SWITCH: {
                    Log.i(TAG, "EV_CEC_SOURCE_SWITCH");
                    startSourceChange(arg1);
                }
                    break;
                case TvCecManager.TVCEC_SEL_DIGITAL_SERVICE_DVB: {
                    Log.i(TAG, "EV_CEC_SEL_DIGITAL_SERVICE_DVB");
                    Log.i(TAG, "msg.arg1: " + arg1 + " msg.arg2: " + arg2);
                    mTvChannelManager.selectProgram(arg1, TvChannelManager.SERVICE_TYPE_DTV);
                }
                    break;
                case TvCecManager.TVCEC_UPDATE_EDID: {
                    if (null == mCecInfoDialog) {
                        if (TvCecManager.CEC_DIALOG_SHOW == arg1) {
                            mCecInfoDialog = ProgressDialog.show(RootActivity.this, "",
                                    getString(R.string.str_updating_edid), true, false);
                            mIsCecDialogCanceled = false;
                            mHandler.sendEmptyMessageDelayed(Constant.ROOTACTIVITY_CANCEL_DIALOG,
                                    CEC_INFO_DISPLAY_TIMEOUT);
                        }
                    } else {
                        if (TvCecManager.CEC_DIALOG_HIDE == arg1) {
                            mIsCecDialogCanceled = true;
                        }
                    }
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

    protected class UiEventListener implements OnUiEventListener {
        @Override
        public boolean onUiEvent(int what) {
            switch (what) {
                case TvCiManager.TVCI_UI_DATA_READY: {
                    if (TvCommonManager.INPUT_SOURCE_STORAGE != mTvCommonManager
                            .getCurrentTvInputSource()) {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        String foreGroundActivity = am.getRunningTasks(1).get(0).topActivity
                                .getClassName();
                        if (foreGroundActivity.equals(CimmiActivity.class.getName()) == false) {
                            Intent intent = new Intent(RootActivity.this, CimmiActivity.class);
                            RootActivity.this.startActivity(intent);
                        }
                    }
                }
                    break;
                case TvCiManager.TVCI_UI_CARD_INSERTED: {
                    Toast toast = Toast.makeText(RootActivity.this,
                            R.string.str_cimmi_hint_ci_inserted, Toast.LENGTH_SHORT);
                    toast.show();
                }
                    break;
                case TvCiManager.TVCI_UI_CARD_REMOVED: {
                    Toast toast = Toast.makeText(RootActivity.this,
                            R.string.str_cimmi_hint_ci_removed, Toast.LENGTH_SHORT);
                    toast.show();
                    if (mScreenSaverStatus == ScreenSaverMode.DTV_SS_SCRAMBLED_PROGRAM) {
                        mScreenSaverStatus = ScreenSaverMode.DTV_SS_NO_CI_MODULE;
                        sendBroadcast(new Intent(TvIntent.ACTION_REDRAW_NOSIGNAL));
                    }
                }
                    break;
                default: {
                }
                    break;
            }
            return true;
        }
    }

    private class CiStatusChangeEventListener implements OnCiStatusChangeEventListener {
        @Override
        public boolean onCiStatusChanged(int what, int arg1, int arg2) {
            Log.i(TAG, "onCiStatusChanged(), what:" + what);
            switch (what) {
                case TvCiManager.TVCI_STATUS_CHANGE_TUNER_OCCUPIED: {
                    switch (arg1) {
                        case TvCiManager.CI_NOTIFY_CU_IS_PROGRESS: {
                            openNotifyMessage(getString(R.string.str_cam_upgrade_alarm));
                        }
                            break;
                        case TvCiManager.CI_NOTIFY_OP_IS_TUNING: {
                            openNotifyMessage(getString(R.string.str_op_tuning_alarm));
                        }
                            break;
                        default: {
                            Log.d(TAG, "Unknown CI occupied status, arg1 = " + arg1);
                        }
                            break;
                    }
                    Log.i(TAG, "sendBroadcast CIPLUS_TUNER_UNAVAIABLE intent: status = " + arg1);
                    Intent intent = new Intent(TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE);
                    intent.putExtra(Constant.TUNER_AVAIABLE, false);
                    sendBroadcast(intent);
                }
                    break;
                default: {
                }
                    break;

            }
            return true;
        }
    }

    private class CiCmdNotifyListener implements OnCiAsyncCmdNotifyListener {
        @Override
        public boolean onCiCmdNotify(int what, int arg1, int arg2) {
            Log.i(TAG, "onCiCmdNotify(), what:" + what);

            ProgressDialog ciNotifyCmdDialog = new ProgressDialog(RootActivity.this);
            ciNotifyCmdDialog.setMessage(RootActivity.this.getResources().getString(
                    R.string.str_cha_please_wait));
            ciNotifyCmdDialog.setIndeterminate(false);
            ciNotifyCmdDialog.setCancelable(false);

            switch (what) {
                case TvCiManager.TVCI_ASYNC_COMMAND_NOTIFY: {
                    LittleDownTimer.pauseItem();
                    LittleDownTimer.pauseMenu();
                    switch (arg1) {
                        case TvCiManager.CI_ASYNC_CMD_SWITCH_ROUTE: {
                            Log.i(TAG, "Reveive CI_ASYNC_CMD_SWITCH_ROUTE");
                            ciNotifyCmdDialog.setTitle(RootActivity.this.getResources().getString(
                                    R.string.str_ci_async_cmd_switch_route));
                            ciNotifyCmdDialog.show();

                            if (TvCommonManager.getInstance().processTvAsyncCommand() == false) {
                                Log.e(TAG, "processTvAsyncCommand return false...");
                            }

                            /* Prepare resuming to menu */
                            if (null != ciNotifyCmdDialog) {
                                ciNotifyCmdDialog.dismiss();
                            }
                        }
                            break;
                        case TvCiManager.CI_ASYNC_CMD_SWITCH_CI_SLOT: {
                            Log.i(TAG, "Reveive CI_ASYNC_CMD_SWITCH_CI_SLOT");
                        }
                            break;
                        default: {
                            Log.d(TAG, "Unknown CI async command notify, arg1 = " + arg1);
                        }
                            break;
                    }
                    LittleDownTimer.resumeItem();
                    LittleDownTimer.resumeMenu();
                }
                    break;
                default: {
                }
                    break;

            }
            return true;
        }
    }

    Thread t_atv = null;

    Thread t_dtv = null;

    private void startThread(boolean type) {
        if (type) {
            // atv
            if (t_atv == null) {
                t_atv = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mTvChannelManager.changeToFirstService(
                                TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                        t_atv = null;
                    }
                });
                t_atv.start();
            }
        } else {
            if (t_dtv == null) {
                // dtv
                t_dtv = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mTvChannelManager.changeToFirstService(
                                TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                        t_dtv = null;
                    }
                });
                t_dtv.start();
            }
        }
    }

    private void startNoSignal() {
        if (mIsSignalLock) {
            mAutoShutdownHandler.removeCallbacks(mShutdownTask);

            if (mIsScreenSaverShown) {
                showNoSignalView(true);
            } else {
                showNoSignalView(false);
            }
        } else {
            setAutoShutdownTime();
            showNoSignalView(true);
        }
    }

    private void startISDBAntennaTypeChange(final int inputAntennaType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int currentTestAntennaType = TvIsdbChannelManager.getInstance().getAntennaType();
                if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                    if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR == inputAntennaType) {
                        if (TvIsdbChannelManager.getInstance().getAntennaType() != TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR) {
                            TvIsdbChannelManager.getInstance().setAntennaType(
                                    TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
                        }
                    } else if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE == inputAntennaType) {
                        TvIsdbChannelManager.getInstance().setAntennaType(
                                TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE);
                        TvCommonManager.getInstance().setInputSource(
                                TvCommonManager.INPUT_SOURCE_ATV);
                        TvChannelManager.getInstance().changeToFirstService(
                                TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                TvChannelManager.FIRST_SERVICE_AC_DC_BOOT);
                    }
                }

            }
        }).start();
    }

    private void startSourceChange(final int inputsource) {
        Log.d(TAG, "startSourceChange, inputsource = " + inputsource);
        if (TvCommonManager.getInstance().getCurrentTvInputSource() != inputsource) {
            mIsInputBlocked = false;
            mIsToPromptPassword = false;
            mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
            mSignalStatusView.setText(R.string.str_no_signal);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                 * SetInputSource is to change source of main window, Set
                 * DisplayFocusWindow to main window to make sure the follow
                 * steps is executing on main window
                 */
                if (TvPipPopManager.getInstance() != null) {
                    if (TvPipPopManager.getInstance().isPipModeEnabled() == true
                            && TvPipPopManager.getInstance().getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_PIP) {
                        TvPipPopManager.getInstance().setPipDisplayFocusWindow(
                                TvPipPopManager.E_MAIN_WINDOW);
                    }
                }

                TvCommonManager.getInstance().setInputSource(inputsource);
                if (inputsource == TvCommonManager.INPUT_SOURCE_ATV) {
                    if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB) {
                        TvIsdbChannelManager.getInstance().genMixProgList(false);
                    }
                    int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
                    if (curChannelNumber > 0xFF) {
                        curChannelNumber = 0;
                    }
                    if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                        TvAtscChannelManager.getInstance().programSel(curChannelNumber,
                                TvAtscChannelManager.ONEPARTCHANNEL_MINOR_NUM);
                    } else {
                        TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
                    }
                } else if (inputsource == TvCommonManager.INPUT_SOURCE_DTV) {
                    if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB) {
                        TvIsdbChannelManager.getInstance().setAntennaType(
                                TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
                    }
                    TvChannelManager.getInstance().playDtvCurrentProgram();
                }
            }
        }).start();
    }

    /**
     * get back key status in root activity
     *
     * @return boolean , true is back key pressed.
     */
    public static boolean getBackKeyStatus() {
        return mIsBackKeyPressed;
    }

    /**
     * get root activity status
     *
     * @return boolean , true means root activity is active now.
     */
    public static boolean getActiveStatus() {
        return mIsActive;
    }

    /**
     * Add API when system is reboot.
     *
     * @author gerard.jiang
     * @serialData 2013/04/28
     * @param flag
     */
    public static void setRebootFlag(boolean flag) {
        isReboot = flag;
    }

    /**
     * Show the exit dialog.
     *
     * @author gerard.jiang
     * @param aDialog
     */
    private void showExitDialog() {
        // Adaptation different resolutions in STB
        if (Tools.isBox()) {
            Log.i(TAG, "start com.mstar.pipservice");
            Intent pipIntent = new Intent("com.mstar.pipservice");
            pipIntent.putExtra("cmd", "invisible");
            pipIntent.setComponent(new ComponentName("com.mstar.miscsetting",
                    "com.mstar.miscsetting.service.PipService"));
            RootActivity.this.startService(pipIntent);
        }
        mExitDialog.setOwnerActivity(rootActivity);
        mExitDialog.show();
    }

    private void displayOpRefreshconfirmation() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                mCiPlusOPRefreshDialog.show();
            }
        });
    }

    private void showNoSignalView(boolean isShown) {
        if (TvChannelManager.MHEG5_STATUS_NON != mTvChannelManager.getMheg5KeyRegisterStatus()) {
            if (TvCommonManager.INPUT_SOURCE_DTV == TvCommonManager.getInstance()
                    .getCurrentTvInputSource()) {
                isShown = false;
            }
        }

        if (0 != mActivityStatus) {
            Log.i(TAG,
                    "No need to show no signal view, mActivityStatus = "
                            + String.format("0x%X", mActivityStatus));
            isShown = false;
        }

        mLinearlayoutNoSignalImageView.setVisibility(ImageView.INVISIBLE);
        mSignalStatusView.setVisibility(TextView.INVISIBLE);
        mSignalStatusView4Pop.setVisibility(TextView.INVISIBLE);
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(ImageView.INVISIBLE);
        if (isShown) {
            mHandler.removeMessages(Constant.ROOTACTIVITY_TTS_SPEAK_NO_SIGNAL_STATUS);
            mHandler.sendEmptyMessageDelayed(Constant.ROOTACTIVITY_TTS_SPEAK_NO_SIGNAL_STATUS, TTS_DELAY_TIME_700MS);
            Log.d(TAG, "showNoSignalView(), isShown = " + isShown);
            if (TvPipPopManager.getInstance().getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP) {
                mSignalStatusView4Pop.setText(mSignalStatusView.getText());
                mSignalStatusView4Pop.setVisibility(TextView.VISIBLE);
            } else if (mSignalStatusView.getText()
                    .equals(getResources().getString(R.string.str_no_signal))) {
                /*
                 * Due to the electric start, the system monitoring view whether
                 * it is drawn,if after the start of the "no signal", first show
                 * static pictures,waiting for bootanimation to finish after
                 * begin to picture of animation.
                 */
                Log.d(TAG, "Set no signal view to VISIBLE");
                mLinearlayoutNoSignalImageView.setVisibility(ImageView.VISIBLE);
                if (mIsPowerOn) {
                    /*
                     * This process is used to "monitor" service.bootanim.exit,
                     * if equal to "1", bootanimation has already been finished,
                     * the pictures can start animation.
                     */
                    mImageViewAnimationHandler.removeCallbacks(mAnimationTask);
                    mImageViewAnimationHandler.postDelayed(mAnimationTask,
                            IMAGE_VIEW_ANIMATION_TIME);
                } else {
                    if (null != mTranslate) {
                        mArrowImageView.startAnimation(mTranslate);
                    }
                }
            } else {
                mSignalStatusView.setVisibility(TextView.VISIBLE);
            }
        } else {
            mTtsNoSignalViewStr = "";
        }
    }

    private Runnable mAnimationTask = new Runnable() {
        @Override
        public void run() {
            if ("1".equals(SystemProperties.get("service.bootanim.exit", "0"))) {
                if (ImageView.VISIBLE == mLinearlayoutNoSignalImageView.getVisibility()) {
                    mArrowImageView.startAnimation(mTranslate);
                }
            } else {
                mImageViewAnimationHandler.postDelayed(mAnimationTask, IMAGE_VIEW_ANIMATION_TIME);
            }
        }
    };

    private boolean sendCecKey(int keyCode) {
        CecSetting setting = mTvCecManager.getCecConfiguration();
        int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (setting.cecStatus == mCecStatusOn) {
            if (curInputSource == TvCommonManager.INPUT_SOURCE_HDMI
                    || curInputSource == TvCommonManager.INPUT_SOURCE_HDMI2
                    || curInputSource == TvCommonManager.INPUT_SOURCE_HDMI3
                    || curInputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
                if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                    if (mTvCecManager.sendCecKey(keyCode)) {
                        Log.d(TAG, "send Cec key,keyCode is " + keyCode
                                + ", tv don't handl the key");
                        return true;
                    }
                }
            } else if (curInputSource == TvCommonManager.INPUT_SOURCE_VGA
                    || curInputSource == TvCommonManager.INPUT_SOURCE_VGA2
                    || curInputSource == TvCommonManager.INPUT_SOURCE_VGA3
                    || curInputSource == TvCommonManager.INPUT_SOURCE_DTV
                    || curInputSource == TvCommonManager.INPUT_SOURCE_ATV
                    || curInputSource == TvCommonManager.INPUT_SOURCE_CVBS
                    || curInputSource == TvCommonManager.INPUT_SOURCE_CVBS2
                    || curInputSource == TvCommonManager.INPUT_SOURCE_CVBS3
                    || curInputSource == TvCommonManager.INPUT_SOURCE_CVBS4
                    || curInputSource == TvCommonManager.INPUT_SOURCE_YPBPR
                    || curInputSource == TvCommonManager.INPUT_SOURCE_YPBPR2
                    || curInputSource == TvCommonManager.INPUT_SOURCE_YPBPR3) {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
                        || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                        || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
                    if (mTvCecManager.sendCecKey(keyCode)) {
                        Log.d(TAG, "send Cec key,keyCode is " + keyCode
                                + ", tv don't handl the key");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean sendGingaKey(int keyCode, KeyEvent event) {
        final boolean down = event.getAction() == KeyEvent.ACTION_DOWN;

        if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            if (TvGingaManager.getInstance().isGingaRunning()) {
                if (down) {
                    if (TvGingaManager.getInstance().processKey(keyCode, true)) {
                        Log.i(TAG, "onKeyDown GingaStatusMode:processKey");
                        return true;
                    }
                } else {
                    if (TvGingaManager.getInstance().processKey(keyCode, false)) {
                        Log.i(TAG, "onKeyUp GingaStatusMode:processKey");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean sendHbbTVKey(int keyCode) {
        if (TvCommonManager.getInstance().getCurrentTvSystem() <= TvCommonManager.TV_SYSTEM_DTMB
                && TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            if (TvHbbTVManager.getInstance().isHbbTVEnabled()) {
                if (TvHbbTVManager.getInstance().hbbtvKeyHandler(keyCode)) {
                    Log.i(TAG, "onKeyDown HbbTV:sendHbbTVKey");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean sendMheg5Key(int keyCode) {
        if (mTvChannelManager.isMheg5Running()) {
            return mTvChannelManager.sendMheg5Key(keyCode);
        } else {
            Log.i(TAG, "isMheg5Running return fali!");
        }
        return false;
    }

    private void updateScreenSaver() {
        /*
         * FIXME: This function is called only when tv first boot up. FIXME:
         * because unlock event maybe post before tvapk is ready. FIXME: so we
         * use this function to draw nosignal when first boot up.
         */
        Boolean isPowerOn = getIntent() != null ? getIntent().getBooleanExtra("isPowerOn", false)
                : false;
        if (!isPowerOn) {
            Log.d(TAG, "only use updateScreenSaver to update NoSignalTextView when first boot up");
            return;
        }

        /**
         * if apk is not exiting or need to update screen saver, send screen
         * saver status or signal lock status to handler for updating screen.
         */
        int curInputSource = mTvCommonManager.getCurrentTvInputSource();
        int curSubInputSource = mTvCommonManager.getCurrentTvSubInputSource();

        mIsSignalLock = mTvCommonManager.isSignalStable(curInputSource);

        Log.d(TAG, "updateScreenSaver(), curInputSource = " + curInputSource
                + ", curSubInputSource = " + curSubInputSource);
        if (true == checkChannelBlockStatus()) {
            return;
        }

        if (!mIsSignalLock && !bCmd_TvApkExit) {
            boolean bSubPopSignalLock = false;
            if (TvPipPopManager.getInstance() != null) {
                if (TvPipPopManager.getInstance().isPipModeEnabled()) {
                    if (TvPipPopManager.getInstance().getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP) {
                        if (mTvCommonManager.isSignalStable(curSubInputSource)) {
                            Log.d(TAG, "set bSubPopSignalLock to True!");
                            bSubPopSignalLock = true;
                        }
                    }
                }
            }
            /**
             * Send Signal Unlock to signalLock Handler if sub inputsource is
             * signal stabled and inputsource is not changing now.
             */
            if (!bSubPopSignalLock && !TVRootApp.getSourceDirty()) {
                Log.d(TAG, "rise a SIGNAL-UNLOCK notification!");
                Message msgLock = Message.obtain();
                msgLock.arg1 = TvEvent.SIGNAL_UNLOCK;
                signalLockHandler.sendMessage(msgLock);
            }
        }
        /**
         * update screen saver status to screenSaverHandler if screen saver need
         * to show and apk is not exiting.
         */
        if (mIsScreenSaverShown && !bCmd_TvApkExit && !mIsBackKeyPressed) {
            Message msgSaver = Message.obtain();
            msgSaver.arg1 = mScreenSaverStatus;
            Log.d(TAG, "update screen saver source :" + curInputSource + " status :"
                    + msgSaver.arg1);
            screenSaverHandler.sendMessage(msgSaver);
        }
    }

    private boolean checkChannelBlockStatus() {
        boolean bBlocking = false;
        Message msgSaver_lock = null;

        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
            if (true == TvAtscChannelManager.getInstance().getCurrentVChipBlockStatus()) {
                msgSaver_lock = Message.obtain();
                msgSaver_lock.arg1 = ScreenSaverMode.DTV_SS_PARENTAL_BLOCK;
                screenSaverHandler.sendMessage(msgSaver_lock);
                bBlocking = true;
            } else if (true == mTvChannelManager.getCurrentProgramInfo().isLock) {
                msgSaver_lock = Message.obtain();
                msgSaver_lock.arg1 = ScreenSaverMode.DTV_SS_CH_BLOCK;
                screenSaverHandler.sendMessage(msgSaver_lock);
                bBlocking = true;
            }
        }
        return bBlocking;
    }

    private Runnable mShutdownTask = new Runnable() {
        @Override
        public void run() {
            if (TvFactoryManager.getInstance().isNoSignalAutoShutdownEnable()) {
                Log.i(TAG, "=================NO SIGNAL STANDBY===============");
                int inputSource = mTvCommonManager.getCurrentTvInputSource();
                if (inputSource == TvCommonManager.INPUT_SOURCE_VGA
                        || inputSource == TvCommonManager.INPUT_SOURCE_VGA2
                        || inputSource == TvCommonManager.INPUT_SOURCE_VGA3
                        || inputSource == TvCommonManager.INPUT_SOURCE_HDMI
                        || inputSource == TvCommonManager.INPUT_SOURCE_HDMI2
                        || inputSource == TvCommonManager.INPUT_SOURCE_HDMI3
                        || inputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
                    mTvCommonManager.setDpmsWakeUpEnable(true);
                }
                mTvCommonManager.standbySystem("standby");
            } else {
                setAutoShutdownTime();
            }
        }
    };

    private void startOadDownload() {
        mHandler.removeMessages(Constant.ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT);
        TvOadManager.getInstance().startOad();
        Intent intent = new Intent(RootActivity.this, OadDownload.class);
        RootActivity.this.startActivity(intent);
    }

    private void stopOadDownload() {
        mHandler.removeMessages(Constant.ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT);
        TvOadManager.getInstance().stopOad(true);
        TvOadManager.getInstance().resetOad();
    }

    private void oadDownloadConfirm() {
        AlertDialog.Builder build = new AlertDialog.Builder(RootActivity.this);
        int country = TvChannelManager.getInstance().getSystemCountryId();
        if (country == TvCountry.AUSTRALIA || country == TvCountry.NEWZEALAND) {
            build.setMessage(R.string.str_oad_msg_dowload_prompt_nz);
        } else {
            build.setMessage(this.getString(R.string.str_oad_msg_download_found) + "\n"
                    + this.getString(R.string.str_oad_msg_dowload_prompt));
        }
        build.setPositiveButton(R.string.str_oad_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startOadDownload();
            }
        });
        build.setNegativeButton(R.string.str_oad_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopOadDownload();
            }
        });
        build.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stopOadDownload();
            }
        });
        build.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mOadDownloadConfirmDialog = null;
            }
        });
        mOadDownloadConfirmDialog = build.create();
        mOadDownloadConfirmDialog.show();
        mHandler.sendEmptyMessageDelayed(Constant.ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT,
                OAD_DOWNLOAD_CONFIRM_TIMEOUT);
    }

    private void oadScanConfirm() {
        AlertDialog.Builder build = new AlertDialog.Builder(RootActivity.this);
        build.setMessage(R.string.str_oad_msg_scan_confirm);
        build.setPositiveButton(R.string.str_oad_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TvOadManager.getInstance().resetOad();
                Intent intent = new Intent(RootActivity.this, OadScan.class);
                RootActivity.this.startActivity(intent);
            }
        });
        build.setNegativeButton(R.string.str_oad_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        build.create().show();
    }

    private void showPasswordLock(boolean bShow) {
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mHandler.removeMessages(Constant.ROOTACTIVITY_DELAY_TO_CLOSE_DIALOG);
        }
        if (true == bShow) {
            if (null != mNitPasswordLock) {
                mNitPasswordLock.dismiss();
            }
            mPasswordLock.show();
        } else {
            mPasswordLock.dismiss();
        }
    }

    private void openNotifyMessage(final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(RootActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void closeDialogs() {
        if (false == mIsForceRevealPwdPrompt) {
            showPasswordLock(false);
        }
    }

    private void nitAutoUpdateScanConfirm() {
        int resId = R.string.str_nit_msg_multiplexer_add_confirm;

        if (mNitAutoScanType == TvCommonManager.NIT_UPDATE_MUX_ADD) {
            resId = R.string.str_nit_msg_multiplexer_add_confirm;
        } else if (mNitAutoScanType == TvCommonManager.NIT_UPDATE_FREQ_CHANGE) {
            resId = R.string.str_nit_msg_frequency_change_confirm;
        } else if (mNitAutoScanType == TvCommonManager.NIT_UPDATE_MUX_REMOVE) {
            resId = R.string.str_nit_msg_multiplexer_remove_confirm;
        }
        AlertDialog.Builder build = new AlertDialog.Builder(RootActivity.this);
        build.setMessage(resId);
        build.setPositiveButton(R.string.str_nit_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TvParentalControlManager.getInstance().isSystemLock()) {
                    mPasswordLock.dismiss();
                    PasswordCheckDialog mNitPasswordLock = new PasswordCheckDialog(rootActivity,
                            R.layout.password_check_dialog_4_digits) {
                        @Override
                        public String onCheckPassword() {
                            return String.format("%04d", TvParentalControlManager.getInstance()
                                    .getParentalPassword());
                        }

                        @Override
                        public void onPassWordCorrect() {
                            dismiss();
                            Toast.makeText(RootActivity.this, R.string.str_check_password_pass,
                                    Toast.LENGTH_SHORT).show();
                            startAutoTuning();
                        }

                        @Override
                        public void onBackPressed() {
                            dismiss();
                            showPasswordLock(mIsToPromptPassword);
                        }
                    };
                    mNitPasswordLock.show();
                } else {
                    startAutoTuning();
                }
            }
        });
        build.setNegativeButton(R.string.str_nit_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        build.create().show();
    }

    private void startAutoTuning() {
        Intent intent = new Intent();
        intent.setAction(TvIntent.ACTION_AUTOTUNING_OPTION);
        if (intent.resolveActivity(RootActivity.this.getPackageManager()) != null) {
            RootActivity.this.startActivity(intent);
        }
    }

    private void setAutoShutdownTime() {
        if (true == ActivityManager.isUserAMonkey()) {
            Log.i(TAG, "monkey is running, no need to start auto shutdown timer!");
        } else {
            int inputSource = mTvCommonManager.getCurrentTvInputSource();
            mAutoShutdownHandler.removeCallbacks(mShutdownTask);
            if (TvCommonManager.INPUT_SOURCE_VGA != inputSource
                    && TvCommonManager.INPUT_SOURCE_VGA2 != inputSource
                    && TvCommonManager.INPUT_SOURCE_VGA3 != inputSource
                    && TvCommonManager.INPUT_SOURCE_STORAGE  != inputSource
                    && TvCommonManager.INPUT_SOURCE_STORAGE2 != inputSource) {
                mAutoShutdownHandler.postDelayed(mShutdownTask, NO_SIGNAL_SHUTDOWN_TIME);
            }
        }
    }

    /**
     * The function handles the APK's displaying message timeout.
     */
    private void handleOadDownloadUiTimeout() {
        if (null != mOadDownloadConfirmDialog) {
            mOadDownloadConfirmDialog.dismiss();
            mOadDownloadConfirmDialog = null;
        }
        mHandler.removeMessages(Constant.ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT);
        int country = TvChannelManager.getInstance().getSystemCountryId();
        if (TvCountry.NEWZEALAND == country) {
            startOadDownload();
        } else if (TvCountry.AUSTRALIA == country) {
            if (true == TvOadManager.getInstance().getOadViewerPrompt()) {
                stopOadDownload();
            } else {
                startOadDownload();
            }
        } else {
            TvOadManager.getInstance().stopOad(false);
        }
    }

    /**
     * The function handles the lower layer triggered timeout notification
     */
    private void handleOadDownloadTimeout() {
        if (null != mOadDownloadConfirmDialog) {
            mOadDownloadConfirmDialog.dismiss();
            mOadDownloadConfirmDialog = null;
            mHandler.removeMessages(Constant.ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT);
            TvOadManager.getInstance().stopOad(false);
        }
    }

    private void ttsSpeakSignalStatusView() {
        if ((mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) && (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV)) {
            if (false == isOnStart) {
                return;
            }
            if ((ImageView.VISIBLE == mLinearlayoutNoSignalImageView.getVisibility())
                    || (TextView.VISIBLE == mSignalStatusView.getVisibility())
                    || (TextView.VISIBLE == mSignalStatusView4Pop.getVisibility())) {
                String str = mSignalStatusView.getText().toString();
                if (!mTtsNoSignalViewStr.equals(str)) {
                    mTtsNoSignalViewStr = new String(str);
                    TvCommonManager.getInstance().speakTtsDelayed(
                        mTtsNoSignalViewStr.toLowerCase()
                        , TvCommonManager.TTS_QUEUE_ADD
                        , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
                        , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
                }
            }
        }
    }
}
