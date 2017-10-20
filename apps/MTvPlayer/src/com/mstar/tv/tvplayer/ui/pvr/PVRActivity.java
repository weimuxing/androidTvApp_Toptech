//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.tvplayer.ui.pvr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.SwitchPageHelper;
import com.mstar.tv.tvplayer.ui.TVRootApp;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StatFs;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import android.graphics.Color;

import com.mstar.android.MKeyEvent;
import com.mstar.android.storage.MStorageManager;
import com.mstar.android.tv.TvCcManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.PvrManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvCcManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.DtvSubtitleInfo;
import com.mstar.android.tvapi.dtv.vo.EpgEventInfo;
import com.mstar.tv.tvplayer.ui.component.PasswordCheckDialog;
import com.mstar.tv.tvplayer.ui.dtv.AudioLanguageActivity;
import com.mstar.tv.tvplayer.ui.dtv.SubtitleLanguageActivity;
import com.mstar.tv.tvplayer.ui.pvr.USBDiskSelecter.usbListener;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;
import com.mstar.util.Utility;

public class PVRActivity extends MstarBaseActivity {
    public static boolean isPVRActivityActive = false;

    public enum PVR_MODE {
        // /pvr mode none
        E_PVR_MODE_NONE,
        // /pvr mode record
        E_PVR_MODE_RECORD,
        // /pvr mode playback or (record + playback)
        E_PVR_MODE_PLAYBACK,
        // /pvr mode time shift
        E_PVR_MODE_TIME_SHIFT,
        // /pvr mode always time shift
        E_PVR_MODE_ALWAYS_TIME_SHIFT,
        // /pvr mode file browser
        E_PVR_MODE_FILE_BROWSER,
        // /pvr mode short
        E_PVR_MODE_SHORT,
    }

    public enum PVR_AB_LOOP_STATUS {
        // /pvr ab loop not set
        E_PVR_AB_LOOP_STATUS_NONE,
        // /pvr ab loop set a position
        E_PVR_AB_LOOP_STATUS_A,
        // /pvr ab loop set b position
        E_PVR_AB_LOOP_STATUS_AB,
    }

    public static final int FREQUENCY_NOT_RECORDING = -1;

    public static final int TIMESHIFT_PLAYBACK_DELAY_SECS = 5;

    private static final int INVALID_TIME = 0xFFFFFFFF;

    private final int PLAYBACK_TIME_UPDATED = 1001;

    private final int RECORDTIME_TIME_UPDATED = 1002;

    private PVR_AB_LOOP_STATUS setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE;

    private ImageView mImagePlaybackStatusImage = null;

    private int pvrABLoopStartTime = INVALID_TIME;

    private int pvrABLoopEndTime = INVALID_TIME;

    private PVR_MODE curPvrMode = PVR_MODE.E_PVR_MODE_NONE;

    private final int savingProgress = 0;

    private final int timeChoose = 1;

    private final int TIME_SHIFE_OVERWRITE_COUNTDOWN_SECS = 30;

    private final int SECONDS_PER_MINUTE = 60;

    /* Currently tv service only support one person. */
    private final int AVAILABLE_PERSON_INDEX = 0;

    private Handler handler = new Handler();

    private boolean isMenuHide = false;

    private boolean mBackToBrowser = false;

    private boolean mIsRecordExceptionHappened = false;

    private boolean mUpdateTotalTime = true;

    private int mCurrentSpeed = TvPvrManager.PVR_PLAYBACK_SPEED_INVALID;

    private LinearLayout mLinearOverWrite = null;

    private RelativeLayout mRelativeRootView = null;

    private RelativeLayout mRelativeRecordingView = null;

    private ImageButton recorder = null;

    private ImageButton play = null;

    private ImageButton stop = null;

    private ImageButton pause = null;

    private ImageButton rev = null;

    private ImageButton ff = null;

    private ImageButton slow = null;

    private ImageButton time = null;

    private ImageButton backward = null;

    private ImageButton forward = null;

    private TextView mTextPause = null;

    private TextView mTextServiceName = null;

    private TextView mTextEventName = null;

    private TextView mTextStartTime = null;

    private TextView mTextTotalRecordTime = null;

    private TextView mTextUsbLabel = null;

    private TextView mTextUsbPercentage = null;

    private TextView mTextTImeShiftOverWrite = null;

    private TextView mTextPlaybackStatusText = null;

    private Activity activity = null;

    private String recordDiskPath = null;

    private String recordDiskLable = null;

    private USBDiskSelecter usbSelecter = null;

    private AnimatorSet menuShowAnimation = null;

    private AnimatorSet menuHideAnimation = null;

    private AnimatorSet recordIconAnimation = null;

    private AlertDialog timeChooser = null;

    private ProgressDialog savingProgressDialog = null;

    private TextProgressBar RPProgress = null;

    private ProgressBar usbFreeSpace = null;

    private Button resetJump2Timebtn = null;

    private static Dialog stopRecordDialog = null;

    private KeyEvent tPreviousEvent;

    private static final String TAG = "PVRActivity";

    private TextView textViewPlay = null;

    private RelativeLayout mProcessLayout = null;

    private ProgressBar progress_loopab = null;

    private int A_progress = 0;

    private android.widget.RelativeLayout.LayoutParams lp4LoopAB;

    private int mCreateMode = Constant.PVR_NONE;

    private int mPauseTimeInSec = 0;

    private int mCurrentTime = 0;

    private int mTotalTime = 0;

    private TextView playSpeed;

    private int subtitlePosLive = -1;

    private short audioLangPosLive = -1;

    private TvChannelManager mTvChannelManager = null;

    private TvEpgManager mTvEpgManager = null;

    private TvPvrManager mPvrManager = null;

    private TvTimerManager mTvTimerManager = null;

    private TvPlayerEventListener mTvPlayerEventListener = null;

    private PasswordCheckDialog mPasswordLock = null;

    private boolean mIsToPromptPassword = false;

    private Toast mCcKeyToast;

    private static final String NO_DISK = "NO_DISK";

    private static final String CHOOSE_DISK = "CHOOSE_DISK";

    private final String FAT = "FAT";

    private final String NTFS = "NTFS";

    private PowerManager mPowerManager = null;

    private void findView() {
        mRelativeRootView = (RelativeLayout) findViewById(R.id.pvrrootmenu);
        mRelativeRecordingView = (RelativeLayout) findViewById(R.id.pvrisrecording);
        mImagePlaybackStatusImage = (ImageView) findViewById(R.id.pvrrecordimage);
        mTextPlaybackStatusText = (TextView) findViewById(R.id.pvrrecordtext);
        mLinearOverWrite = (LinearLayout) findViewById(R.id.linearTimeShiftOverWrite);
        recorder = (ImageButton) findViewById(R.id.player_recorder);
        play = (ImageButton) findViewById(R.id.player_play);
        stop = (ImageButton) findViewById(R.id.player_stop);
        pause = (ImageButton) findViewById(R.id.player_pause);
        rev = (ImageButton) findViewById(R.id.player_rev);
        ff = (ImageButton) findViewById(R.id.player_ff);
        slow = (ImageButton) findViewById(R.id.player_slow);
        time = (ImageButton) findViewById(R.id.player_time);
        backward = (ImageButton) findViewById(R.id.player_backward);
        forward = (ImageButton) findViewById(R.id.player_forward);
        mTextPause = (TextView) findViewById(R.id.str_player_pause);
        mTextServiceName = (TextView) findViewById(R.id.textView1);
        mTextEventName = (TextView) findViewById(R.id.textView2);
        mTextStartTime = (TextView) findViewById(R.id.start_time);
        mTextStartTime.setText("00:00:00");
        mTextTotalRecordTime = (TextView) findViewById(R.id.record_time);
        mTextTotalRecordTime.setText("00:00:00");
        mTextTImeShiftOverWrite = (TextView) findViewById(R.id.textViewOverWriteTime);
        mTextTImeShiftOverWrite.setText("00:00:00");
        playSpeed = (TextView) findViewById(R.id.play_speed);
        playSpeed.setVisibility(View.GONE);
        mTextUsbLabel = (TextView) findViewById(R.id.usbLabelName);
        mTextUsbPercentage = (TextView) findViewById(R.id.usbFreeSpacePercent);
        RPProgress = (TextProgressBar) findViewById(R.id.play_record_progress);
        usbFreeSpace = (ProgressBar) findViewById(R.id.usbFreeSpace);
        usbFreeSpace.setMax(100);
        textViewPlay = (TextView) findViewById(R.id.text_view_player_play);
        progress_loopab = (ProgressBar) findViewById(R.id.progressbar_loopab);
        mProcessLayout = (RelativeLayout) findViewById(R.id.pvrprocess_layout);
        lp4LoopAB = new android.widget.RelativeLayout.LayoutParams(0, dip2px(5));
        lp4LoopAB.topMargin = 5;
        savingProgressDialog = new ProgressDialog(this);

        recorder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyPlay();
            }
        });
        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyStop();
            }
        });
        pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyPause();
            }
        });
        rev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyRev();
            }
        });
        ff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyFF();
            }
        });
        slow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeySlowMotion();
            }
        });
        time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyGoToTime();
            }
        });
        backward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onKeyBackward();
            }
        });
        forward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyForward();
            }
        });
    }

    private void settingServiceEventName() {
        ProgramInfo curProgInfo = null;
        EpgEventInfo epgEventInfo = new EpgEventInfo();
        String serviceNumberAndNameStr = null;
        if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB) {
            curProgInfo = TvIsdbChannelManager.getInstance().getCurrentProgramInfo();
            serviceNumberAndNameStr = "CH" + curProgInfo.majorNum + "." + curProgInfo.minorNum
                    + " " + curProgInfo.serviceName;
        } else {
            curProgInfo = mTvChannelManager.getCurrentProgramInfo();
            serviceNumberAndNameStr = "CH" + curProgInfo.number + " " + curProgInfo.serviceName;
        }
        mTextServiceName.setText(serviceNumberAndNameStr);
        epgEventInfo = mTvEpgManager.getDvbEventInfoByTime(curProgInfo.serviceType,
                curProgInfo.number, mTvTimerManager.getCurrentTvTime());
        if ((epgEventInfo != null) && (epgEventInfo.name != null)) {
            mTextEventName.setText(epgEventInfo.name);
        } else {
            mTextEventName.setText("");
        }
    }

    private void saveChooseDiskSettings(boolean flag, String path, String label) {
        SharedPreferences sp = getSharedPreferences(Constant.SAVE_SETTING_SELECT, MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean("IS_ALREADY_CHOOSE_DISK", flag);
        editor.putString("DISK_PATH", path);
        editor.putString("DISK_LABEL", label);
        editor.commit();
    }

    private boolean isSupportedFileSystem(boolean isToastError) {
        int startPos = 6;
        if (VERSION.SDK_INT > TvCommonManager.API_LEVEL_LOLLIPOP_MR1) {
            // FIXME: Use storage manager to get file system type
            startPos = 11;
        }
        if (false == recordDiskLable.regionMatches(startPos, FAT, 0, 3)) {
            if (true == isToastError) {
                toastOnUiThread(R.string.str_pvr_unsurpt_flsystem);
            }
            return false;
        }
        return true;
    }

    private void settingUSB() {
        usbSelecter = new USBDiskSelecter(activity) {
            @Override
            public void onItemChosen(int position, String diskLabel, String diskPath) {
                boolean isRecordSuccess = false;
                recordDiskPath = diskPath;
                recordDiskLable = diskLabel;
                mTextUsbLabel.setText(diskLabel);
                Log.d(TAG, "current Selected Disk = " + recordDiskPath);
                Log.e(TAG, "current selected DiskLabel=" + recordDiskLable);
                if (false == isSupportedFileSystem(true)) {
                    Log.d(TAG, "Finish()! File system not support, maybe it is the NTFS.");
                    finish();
                    return;
                }
                saveChooseDiskSettings(true, diskPath, diskLabel);
                if (Constant.PVR_PLAYBACK_PAUSE == mCreateMode) {
                    isRecordSuccess = doPVRTimeShift(mPvrManager.isRecording());
                } else {
                    isRecordSuccess = doPVRRecord(mPvrManager.isRecording());
                }
                if (false == isRecordSuccess) {
                    mIsRecordExceptionHappened = true;
                    Log.d(TAG, "Finish()! Failed in starting record");
                    finish();
                    return;
                }
            }
        };
        usbSelecter.setUSBListener(new usbListener() {

            @Override
            public void onUSBUnmounted(String diskPath) {
            }

            @Override
            public void onUSBMounted(String diskPath) {
            }

            @Override
            public void onUSBEject(String diskPath) {
                Log.d(TAG, "onUSBEject");
                if (recordDiskPath == null || !recordDiskPath.equals(diskPath))
                    return;
                stopPlaybacking();
                stopRecord();
                mIsRecordExceptionHappened = true;
                Log.d(TAG, "Finish()! USB ejected!");
                finish();
            }
        });
    }

    private void settingPasswardDialog() {
        mPasswordLock = new PasswordCheckDialog(this, R.layout.password_check_dialog_4_digits) {
            @Override
            public String onCheckPassword() {
                return String.format("%04d", TvParentalControlManager.getInstance()
                        .getParentalPassword());
            }

            @Override
            public void onPassWordCorrect() {
                Toast.makeText(activity,
                        activity.getResources().getString(R.string.str_check_password_pass),
                        Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPvrManager.unlockPlayback();
                    }
                }).start();
                dismiss();
            }

            @Override
            public void onKeyDown(int keyCode, KeyEvent keyEvent) {
                Log.i(TAG, "PasswordCheckDialog.onKeyDown(" + keyCode + "," + keyEvent + ")");
                if (KeyEvent.KEYCODE_ENTER != keyCode) {
                    activity.onKeyDown(keyCode, keyEvent);
                }
            }

            @Override
            public void onBackPressed() {
                Toast.makeText(activity,
                        activity.getResources().getString(R.string.str_check_password_invalid),
                        Toast.LENGTH_SHORT).show();
                activity.onBackPressed();
            }
        };
    }

    private void toastErrorCode(int errorCode) {
        String[] dispInfo = getResources().getStringArray(R.array.str_arr_pvr_error_code);
        try {
            Toast.makeText(this, dispInfo[errorCode], Toast.LENGTH_SHORT).show();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private boolean playfile(String pvrFileName) {
        if ((null == pvrFileName) || (true == pvrFileName.isEmpty())) {
            Log.e(TAG, "===========>>>> pvrFileName is NULL !!!!!");
            return false;
        }

        final String pvrFileLcn = "CH " + mPvrManager.getFileLcn(0);
        final String pvrFileServiceName = mPvrManager.getFileServiceName(pvrFileName);
        final String pvrFileEventName = mPvrManager.getFileEventName(pvrFileName);
        final String pvrFileServiceNumberAndNameStr = pvrFileLcn + " " + pvrFileServiceName;
        final int recordedFileDuration = mPvrManager.getRecordedFileDurationTime(pvrFileName);
        int resumePlayTime = mPvrManager.getPvrFileResumePointInSec(pvrFileName,
                AVAILABLE_PERSON_INDEX);
        mTextServiceName.setText(pvrFileServiceNumberAndNameStr);
        mTextEventName.setText(pvrFileEventName);
        Log.d(TAG, "PlayName    :" + pvrFileName);
        Log.d(TAG, "Lcn         :" + pvrFileLcn);
        Log.d(TAG, "ServiceName :" + pvrFileServiceName);
        Log.d(TAG, "EventName   :" + pvrFileEventName);
        stopPlaybacking();
        /* UPC Spec: resume play behavior sync from Supernova CL:452060 */
        if ((resumePlayTime + SECONDS_PER_MINUTE) > recordedFileDuration) {
            resumePlayTime = 0;
        }
        int playbackStatus = mPvrManager.startPvrPlaybackInSec(pvrFileName, resumePlayTime);
        Log.d(TAG, "==========>>> playbackStatus = " + playbackStatus + "; resumePlayTime ="
                + resumePlayTime);
        if (TvPvrManager.PVR_STATUS_SUCCESS != playbackStatus) {
            toastErrorCode(playbackStatus);
            return false;
        }
        if ((false == mPvrManager.isRecording())
                || (false == pvrFileName.equals(mPvrManager.getCurRecordingFileName()))) {
            Log.d(TAG, "Play recording file");
            mUpdateTotalTime = false;
            mTotalTime = recordedFileDuration;
        }
        /*
         * to avoid request focus on stop or other image button , give the
         * request to invisible image button when the first focus is on the stop
         * button, when press the enter in pvr brower page, it will perform
         * button onclick
         */
        recorder.setEnabled(false);
        recorder.requestFocus();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pvr_menu);
        alwaysTimeout = true;
        activity = this;
        isPVRActivityActive = true;
        mTvChannelManager = TvChannelManager.getInstance();
        mTvTimerManager = TvTimerManager.getInstance();
        mTvEpgManager = TvEpgManager.getInstance();
        mPvrManager = TvPvrManager.getInstance();
        pvrABLoopStartTime = INVALID_TIME;
        pvrABLoopEndTime = INVALID_TIME;
        setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE;
        findView();
        settingUSB();
        settingPasswardDialog();
        mTvPlayerEventListener = new TvPlayerEventListener();
        TvChannelManager.getInstance().registerOnTvPlayerEventListener(mTvPlayerEventListener);
        createAnimation();
        final boolean isAlwaysTimeShiftRecording = mPvrManager.isAlwaysTimeShiftRecording();
        if (getIntent().getExtras() == null) {
            Log.d(TAG, "Finish()! Extras() is null");
            finish();
            return;
        }
        mPowerManager = (PowerManager)activity.getSystemService(Context.POWER_SERVICE);
        mCreateMode = getIntent().getExtras().getInt(Constant.PVR_CREATE_MODE);
        mBackToBrowser = (Constant.PVR_PLAYBACK_FROM_BROWSER == mCreateMode);
        Log.d(TAG, "mCreateMode:" + Integer.toHexString(mCreateMode));
        Log.d(TAG, "BrowserCall:" + mBackToBrowser);

        if (isAlwaysTimeShiftRecording) {
            findViewById(R.id.player_recorder_icon).setVisibility(View.GONE);
            curPvrMode = PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT;
            recordDiskPath = mPvrManager.getPvrMountPath();
            recordDiskLable = getUsbLabelByPath(new String(recordDiskPath));
            settingServiceEventName();
            if (Constant.PVR_PLAYBACK_PAUSE == mCreateMode) {
                onKeyPause();
            } else if (Constant.PVR_PLAYBACK_PREVIOUS == mCreateMode) {
                onKeyBackward();
            } else if (Constant.PVR_PLAYBACK_REWIND == mCreateMode) {
                onKeyRev();
            } else {
                Log.d(TAG, "Finish()! Unhandled case");
                finish();
                return;
            }
            new usbInfoUpdate().start();
            return;
        }

        if ((Constant.PVR_PLAYBACK_START == mCreateMode)
                || (Constant.PVR_PLAYBACK_FROM_BROWSER == mCreateMode)) {
            final String pvrFileName = getIntent().getExtras().getString(Constant.PVR_FILENAME);
            findViewById(R.id.player_recorder_icon).setVisibility(View.GONE);
            findViewById(R.id.usbInfoLayout).setVisibility(View.GONE);
            curPvrMode = PVR_MODE.E_PVR_MODE_PLAYBACK;
            createAnimation();
            setPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_1X);
            if (false == playfile(pvrFileName)) {
                Log.d(TAG, "Finish()! can not play  PVR file");
                finish();
            }
        } else if (Constant.PVR_PLAYBACK_PAUSE == mCreateMode) {
            findViewById(R.id.player_recorder_icon).setVisibility(View.GONE);
            curPvrMode = PVR_MODE.E_PVR_MODE_TIME_SHIFT;
            settingServiceEventName();
            if (false == onKeyRecord()) {
                mIsRecordExceptionHappened = true;
                Log.d(TAG, "Finish()! onKeyRecord() = false 11");
                finish();
            }
        } else if (Constant.PVR_RECORD_START == mCreateMode) {
            curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
            settingServiceEventName();
            if (false == onKeyRecord()) {
                mIsRecordExceptionHappened = true;
                Log.d(TAG, "Finish()! onKeyRecord() = false 22");
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        onKeyStop();
    }

    @Override
    public void onTimeOut() {
        super.onTimeOut();
        menuHide();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        /* When another activtiy stop record or playback */
        if ((false == mPvrManager.isRecording()) && (false == mPvrManager.isPlaybacking())) {
            /*
             * Prevent goto browser Page, after close record or playbacking by
             * other apps
             */
            mBackToBrowser = false;
            Log.d(TAG, "Finish()! Prevent goto browser Page");
            finish();
            return;
        }
        if (true == mPvrManager.isRecording()) {
            if (TvCommonManager.INPUT_SOURCE_DTV != TvCommonManager.getInstance()
                    .getCurrentTvInputSource()) {
                TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                TvChannelManager.getInstance().changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                TvPictureManager.getInstance().setDynamicBackLightThreadSleep(false);
            }
        }

        activity.setVisible(true);
        // Use for press "index" key in PVR recording state then back to PVR
        isGoingToBeClosed(false);
        showPasswordLock(mIsToPromptPassword);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        showPasswordLock(false);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /* must store the new intent unless getIntent() will return the old one */
        setIntent(intent);
        if (getIntent().getExtras() != null) {
            int createMode = getIntent().getExtras().getInt(Constant.PVR_CREATE_MODE);
            if (Constant.PVR_PLAYBACK_STOP == createMode) {
                stopPlaybacking();
                Log.d(TAG, "Finish()! Stop playback!");
                finish();
                return;
            }
            if (Constant.PVR_RECORD_STOP == createMode) {
                saveAndExit();
            }
        }
    }

    private void stopRecord() {
        unFreezeImage(true);
        if (PVR_MODE.E_PVR_MODE_TIME_SHIFT == curPvrMode) {
            mPvrManager.stopTimeShiftRecord();
        } else if (PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT == curPvrMode) {
            mPvrManager.stopAlwaysTimeShiftRecord();
        } else {
            mPvrManager.stopRecord();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
         * The behavior is same as Pure SN. Mheg5 is supported if PVR is in
         * recording mode and the menu is in hidden mode.
         */
        if ((isMenuHide) && (PVR_MODE.E_PVR_MODE_RECORD == curPvrMode)) {
            if (sendMheg5Key(keyCode)) {
                Log.i(TAG, "onKeyDown:sendMhegKey success!");
                return true;
            }
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
                Utility.channelUp(activity);
                break;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                Utility.channelDown(activity);
                break;
            case MKeyEvent.KEYCODE_CHANNEL_RETURN:
                Utility.channelReturn(activity);
                break;

            case MKeyEvent.KEYCODE_LIST: {
                Intent intent = new Intent(TvIntent.CHANNEL_LIST);
                intent.putExtra("ListId", Constant.SHOW_PROGRAM_LIST);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    activity.setVisible(false);
                    startActivity(intent);
                }
                return true;
            }

            case MKeyEvent.KEYCODE_MSTAR_INDEX: {
                /* TimeShift not support index key. */
                if ((PVR_MODE.E_PVR_MODE_TIME_SHIFT == curPvrMode)
                        || (PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT == curPvrMode)) {
                    return false;
                }
                mBackToBrowser = true;
                Log.d(TAG, "Finish()! index key pressed!");
                finish();
                return true;
            }

            case MKeyEvent.KEYCODE_MTS: {
                try {
                    DtvAudioInfo audioInfo = new DtvAudioInfo();
                    audioInfo = TvChannelManager.getInstance().getAudioInfo_ex();
                    if (audioInfo != null)
                        audioLangPosLive = audioInfo.currentAudioIndex;
                    /* add by owen.qin to avoid finish by BaseActivity's onPause */
                    isGoingToBeClosed(false);
                    Intent intent = new Intent(PVRActivity.this, AudioLanguageActivity.class);
                    PVRActivity.this.startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case MKeyEvent.KEYCODE_SUBTITLE: {
                try {
                    isGoingToBeClosed(false);
                    SharedPreferences settings = getSharedPreferences(
                            Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
                    subtitlePosLive = settings.getInt("subtitlePos", -1);

                    Intent intent = new Intent(PVRActivity.this, SubtitleLanguageActivity.class);
                    PVRActivity.this.startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case MKeyEvent.KEYCODE_MSTAR_UPDATE:
            case KeyEvent.KEYCODE_DVR:
            case KeyEvent.KEYCODE_MEDIA_RECORD:
                /* SN PVR turnkey spec: record key mapping to UI show Hide */
                if (isMenuHide == true) {
                    menuShow();
                } else {
                    menuHide();
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                pause.performClick();
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                play.performClick();
                return true;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                ff.performClick();
                return true;
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                rev.performClick();
                return true;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                forward.performClick();
                return true;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                backward.performClick();
                return true;
            case KeyEvent.KEYCODE_MEDIA_STOP:
                stop.performClick();
                return true;
            case MKeyEvent.KEYCODE_CC:
                if ((TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB)
                        || (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC)) {
                    if (mCcKeyToast == null) {
                        mCcKeyToast = new Toast(this);
                        mCcKeyToast.setGravity(Gravity.CENTER, 0, 0);
                    }
                    TextView tv = new TextView(this);
                    tv.setTextSize(Constant.CCKEY_TEXTSIZE);
                    tv.setTextColor(Color.WHITE);
                    tv.setAlpha(Constant.CCKEY_ALPHA);
                    mCcKeyToast.setView(tv);
                    mCcKeyToast.setDuration(Toast.LENGTH_SHORT);
                    int caption = 0;
                    int resId = 0;
                    if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
                        caption = TvCcManager.getInstance().getNextAtscCcMode();
                        resId = R.array.str_arr_setting_cc_mode_vals;
                    } else {
                        caption = TvCcManager.getInstance().getNextIsdbCcMode();
                        resId = R.array.str_arr_option_caption;
                    }
                    tv.setText(getResources().getString(R.string.str_option_caption) + " "
                            + getResources().getStringArray(resId)[caption]);
                    mCcKeyToast.show();
                }
                return true;
        }

        if (keyCode != KeyEvent.KEYCODE_BACK) {
            menuShow();
        }
        /*
         * The behavior is same as Pure SN. TTX is supported if PVR is in
         * recording mode and the menu is in hidden mode.
         */
        if ((isMenuHide) && (PVR_MODE.E_PVR_MODE_RECORD == curPvrMode)) {
            if (true == SwitchPageHelper.goToTeletextPage(this, keyCode)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showSavingProgressDialog() {
        savingProgressDialog.setMessage(getResources().getString(R.string.str_pvr_program_saving));
        savingProgressDialog.setIndeterminate(false);
        savingProgressDialog.setCancelable(false);
        savingProgressDialog.show();
    }

    private void showTimeChooseDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.pvr_menu_dialog,
                (ViewGroup) findViewById(R.id.pvr_dialog));

        resetJump2Timebtn = (Button) layout.findViewById(R.id.ResetJ2TBtn);
        resetJump2Timebtn.setOnClickListener(J2TButtonListener);

        timeChooser = new AlertDialog.Builder(activity)
                .setTitle(R.string.str_player_time)
                .setView(layout)
                .setCancelable(false)
                .setPositiveButton(R.string.str_root_alert_dialog_confirm,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                                int timeInSecond = ((TimeChooser) timeChooser
                                        .findViewById(R.id.pvr_menu_dialog_hours)).getValue()
                                        * 3600
                                        + ((TimeChooser) timeChooser
                                                .findViewById(R.id.pvr_menu_dialog_minutes))
                                                .getValue()
                                        * 60
                                        + ((TimeChooser) timeChooser
                                                .findViewById(R.id.pvr_menu_dialog_seconds))
                                                .getValue();
                                Log.e(TAG, "=============>>> CURRENT JUMP TIME = " + timeInSecond);
                                mPvrManager.jumpPlaybackTime(timeInSecond);
                            }
                        })
                .setNegativeButton(R.string.str_root_alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                                timeChooser = null;
                            }
                        }).show();
    }

    private OnClickListener J2TButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText hours, minutes, seconds;
            hours = (TimeChooser) timeChooser.findViewById(R.id.pvr_menu_dialog_hours);
            minutes = (TimeChooser) timeChooser.findViewById(R.id.pvr_menu_dialog_minutes);
            seconds = (TimeChooser) timeChooser.findViewById(R.id.pvr_menu_dialog_seconds);
            hours.setText("");
            minutes.setText("");
            seconds.setText("");
        }
    };

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        stopPlaybacking();
        if (false == mPvrManager.getIsBootByRecord()) {
            if (false == mPowerManager.isScreenOn()) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        if (true == mIsRecordExceptionHappened) {
            cancelValidEpgTimerEvent();
            mIsRecordExceptionHappened = false;
        }
        checkToStandbySystem();
        isPVRActivityActive = false;
        super.onDestroy();
        usbSelecter.dismiss();
        if (null != mTvPlayerEventListener) {
            TvChannelManager.getInstance()
                    .unregisterOnTvPlayerEventListener(mTvPlayerEventListener);
            mTvPlayerEventListener = null;
        }
        if (true == mBackToBrowser) {
            /*
             * Prevent press home key, still go back to Browser (prevent no DTV
             * resource)
             */
            if (TvCommonManager.INPUT_SOURCE_DTV == TvCommonManager.getInstance()
                    .getCurrentTvInputSource()) {
                Intent intent = new Intent(TvIntent.ACTION_PVR_BROWSER);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        }
    }

    public void setBarStatusOfStartRecord() {
        recorder.setEnabled(false);
        play.setEnabled(true);
        stop.setEnabled(true);
        pause.setEnabled(true);
        rev.setEnabled(false);
        ff.setEnabled(false);
        slow.setEnabled(false);
        time.setEnabled(false);
        backward.setEnabled(false);
        forward.setEnabled(false);
        recorder.setFocusable(false);
        play.setFocusable(true);
        stop.setFocusable(true);
        pause.setFocusable(true);
        rev.setFocusable(false);
        ff.setFocusable(false);
        slow.setFocusable(false);
        time.setFocusable(false);
        backward.setFocusable(false);
        forward.setFocusable(false);
        mImagePlaybackStatusImage.setImageResource(R.drawable.idle_img_press_ststus_pvr);
        mRelativeRecordingView.setVisibility(View.GONE);
        mTextPlaybackStatusText.setText(R.string.str_pvr_is_recording);
        mTextPlaybackStatusText.setVisibility(View.VISIBLE);
    }

    public void setBarStatusOfRecordToPause() {
        recorder.setEnabled(false);
        play.setEnabled(true);
        stop.setEnabled(true);
        pause.setEnabled(false);
        rev.setEnabled(false);
        ff.setEnabled(false);
        slow.setEnabled(false);
        time.setEnabled(false);
        backward.setEnabled(false);
        forward.setEnabled(false);
        recorder.setFocusable(false);
        play.setFocusable(true);
        stop.setFocusable(true);
        pause.setFocusable(false);
        rev.setFocusable(false);
        ff.setFocusable(false);
        slow.setFocusable(false);
        time.setFocusable(false);
        backward.setFocusable(false);
        forward.setFocusable(false);
    }

    public void setBarStatusOfPlayToPause() {
        recorder.setEnabled(false);
        play.setEnabled(true);
        stop.setEnabled(true);
        pause.setEnabled(true);
        rev.setEnabled(true);
        ff.setEnabled(true);
        slow.setEnabled(true);
        time.setEnabled(false);
        backward.setEnabled(false);
        forward.setEnabled(false);
        recorder.setFocusable(false);
        play.setFocusable(true);
        stop.setFocusable(true);
        pause.setFocusable(true);
        rev.setFocusable(true);
        ff.setFocusable(true);
        slow.setFocusable(true);
        time.setFocusable(false);
        backward.setFocusable(false);
        forward.setFocusable(false);
    }

    public void setBarStatusOfPlaybackAll() {
        recorder.setEnabled(false);
        play.setEnabled(true);
        stop.setEnabled(true);
        pause.setEnabled(true);
        rev.setEnabled(true);
        ff.setEnabled(true);
        slow.setEnabled(true);
        time.setEnabled(true);
        backward.setEnabled(true);
        forward.setEnabled(true);
        recorder.setFocusable(false);
        play.setFocusable(true);
        stop.setFocusable(true);
        pause.setFocusable(true);
        rev.setFocusable(true);
        ff.setFocusable(true);
        slow.setFocusable(true);
        time.setFocusable(true);
        backward.setFocusable(true);
        forward.setFocusable(true);
        if (isMenuHide == false) {
            mRelativeRecordingView.setVisibility(View.GONE);
            mTextPlaybackStatusText.setVisibility(View.GONE);
        }
    }

    private boolean getChooseDiskSettings() {
        SharedPreferences sp = getSharedPreferences(Constant.SAVE_SETTING_SELECT, MODE_PRIVATE);
        return sp.getBoolean("IS_ALREADY_CHOOSE_DISK", false);

    }

    private String getChooseDiskLable() {
        SharedPreferences sp = getSharedPreferences(Constant.SAVE_SETTING_SELECT, MODE_PRIVATE);
        return sp.getString("DISK_LABEL", "unknown");
    }

    private String getChooseDiskPath() {
        SharedPreferences sp = getSharedPreferences(Constant.SAVE_SETTING_SELECT, MODE_PRIVATE);
        return sp.getString("DISK_PATH", "unknown");
    }

    private boolean isFileExisted(String path) {
        if (path == null || "".equals(path)) {
            return false;
        } else {
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static class DirectoryFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String filename) {
            if (new File(dir, filename).isDirectory()) {
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean isFATDisk(String diskPath) {
        Log.e(TAG, "isFATDisk diskPath:" + diskPath);
        String label = getUsbLabelByPath(new String(diskPath));
        Log.e(TAG, "isFATDisk label:" + label);
        if (label != null && label.contains(FAT)) {
            return true;
        }
        return false;
    }

    private String getFirstUseableDiskAtParentDir(String parent) {
        File file = new File(parent);
        if (file.isDirectory()) {
            FilenameFilter filter = new DirectoryFilter();
            File[] list = file.listFiles(filter);
            for (File tmp : list) {
                if ((isFileExisted(tmp.getAbsolutePath() + "/_MSTPVR/") || USBBroadcastReceiver
                        .isDiskExisted(new String(tmp.getAbsolutePath())))
                        && (isFATDisk(tmp.getAbsolutePath()))) {
                    return tmp.getAbsolutePath();
                }
            }
            return null;
        } else {
            return null;
        }
    }

    private String getBestDiskPath() {
        if (getChooseDiskSettings()) {
            String path = getChooseDiskPath();
            if (isFileExisted(path + "/_MSTPVR")
                    || USBBroadcastReceiver.isDiskExisted(new String(path))) {
                return path;
            } else {
                String parent = "/mnt/usb/";
                String firstDisk = getFirstUseableDiskAtParentDir(parent);
                if (firstDisk == null) {
                    return NO_DISK;
                } else {
                    return firstDisk;
                }
            }
        } else {
            return CHOOSE_DISK;
        }
    }

    /**
     * @param path like /mnt/usb/sda1
     */
    private String getUsbLabelByPath(String diskPath) {

        MStorageManager storageManager = MStorageManager.getInstance(this);
        String[] volumes = storageManager.getVolumePaths();
        int usbDriverCount = 0;
        ArrayList<String> usbDriverLabel = new ArrayList<String>();
        ArrayList<String> usbDriverPath = new ArrayList<String>();
        usbDriverLabel.clear();
        usbDriverPath.clear();
        if (volumes == null) {
            return null;
        }

        File file = new File("proc/mounts");
        if (!file.exists() || file.isDirectory()) {
            file = null;
        }

        for (int i = 0; i < volumes.length; ++i) {
            String state = storageManager.getVolumeState(volumes[i]);
            if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                continue;
            }
            String path = volumes[i];
            String[] pathPartition = path.split("/");
            String label = pathPartition[pathPartition.length - 1];
            String volumeLabel = storageManager.getVolumeLabel(path);
            if (volumeLabel != null) {
                // get rid of the long space in the Label word
                String[] tempVolumeLabel = volumeLabel.split(" ");
                volumeLabel = "";
                for (int j = 0; j < tempVolumeLabel.length; j++) {
                    if (j != tempVolumeLabel.length - 1) {
                        volumeLabel += tempVolumeLabel[j] + " ";
                        continue;
                    }
                    volumeLabel += tempVolumeLabel[j];
                }
            }
            label += ": " + getFileSystem(file, path) + "\n" + volumeLabel;
            usbDriverLabel.add(usbDriverCount, label);
            usbDriverPath.add(usbDriverCount, path);
            usbDriverCount++;
        }

        if (diskPath.startsWith("/")) {
            diskPath = diskPath.substring(1);
        }
        if (diskPath.endsWith("/")) {
            diskPath = diskPath.substring(0, diskPath.length() - 1);
        }
        for (int i = 0; i < usbDriverPath.size(); i++) {
            if (usbDriverPath.get(i).contains(diskPath)) {
                return usbDriverLabel.get(i);
            }
        }
        return null;
    }

    private String getFileSystem(File file, String path) {
        if (file == null) {
            return "";
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                String[] info = line.split(" ");
                if (info[1].equals(path)) {
                    if (info[2].equals("ntfs3g"))
                        return NTFS;
                    if (info[2].equals("vfat"))
                        return FAT;
                    else
                        return info[2];
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return "";
    }

    private void checkToStandbySystem() {
        final boolean isBootedByRecord = mPvrManager.getIsBootByRecord();
        Log.d(TAG, "check standby boot by record:" + isBootedByRecord);
        if (true == isBootedByRecord) {
            mPvrManager.setIsBootByRecord(false);
            if (false == mPowerManager.isScreenOn()) {
                TvCommonManager.getInstance().standbySystem("pvr");
            }
        }
    }

    private String getAvaliableDiskForStandBy() {
        String parent = "/mnt/usb/";
        String firstDisk = getFirstUseableDiskAtParentDir(parent);
        return firstDisk;

    }

    private void cancelValidEpgTimerEvent() {
        if (null != mTvTimerManager) {
            Time curTime = mTvTimerManager.getCurrentTvTime();
            mTvTimerManager.cancelEpgTimerEvent((int) ((curTime.toMillis(true) / 1000) + 10 + 3),
                    false);
        }
    }

    private boolean onKeyRecord() {
        boolean ret = false;
        final boolean isRecording = mPvrManager.isRecording();
        final boolean isPlaybacking = mPvrManager.isPlaybacking();
        recordDiskPath = mPvrManager.getPvrMountPath();
        Log.d(TAG, "isRecording =" + isRecording);
        Log.d(TAG, "isPlaybacking =" + isPlaybacking);
        Log.d(TAG, "getMountPath=" + recordDiskPath);
        if (true == isRecording) {
            if (true == isPlaybacking) {
                return true;
            }
            recordDiskLable = getUsbLabelByPath(new String(recordDiskPath));
            mTextUsbLabel.setText(recordDiskLable);
            Log.d(TAG, "recordDiskLable=" + recordDiskLable);
            if (Constant.PVR_PLAYBACK_PAUSE == mCreateMode) {
                ret = doPVRTimeShift(isRecording);
            } else {
                ret = doPVRRecord(isRecording);
            }
        } else {
            if (usbSelecter.getDriverCount() <= 0) {
                Toast.makeText(activity, R.string.str_pvr_insert_usb, Toast.LENGTH_SHORT).show();
                return false;
            }
            String diskPath = getBestDiskPath();
            Log.e(TAG, "getBestDiskPath:" + diskPath);
            if (true == NO_DISK.equals(diskPath)) {
                Toast.makeText(activity, R.string.str_pvr_insert_usb, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (true == CHOOSE_DISK.equals(diskPath)) {
                Log.e(TAG, "choose disk");
                if (mPvrManager.getIsBootByRecord()) {
                    diskPath = getAvaliableDiskForStandBy();
                    Log.e(TAG, "getAvaliableDiskForStandBy=" + diskPath);
                    if (null == diskPath) {
                        return false;
                    }
                } else {
                    usbSelecter.start();
                    return true;
                }

            }
            String diskLabel = getUsbLabelByPath(new String(diskPath));
            recordDiskPath = diskPath;
            recordDiskLable = diskLabel;
            mTextUsbLabel.setText(diskLabel);
            if (Constant.PVR_PLAYBACK_PAUSE == mCreateMode) {
                ret = doPVRTimeShift(isRecording);
            } else {
                if (false == isSupportedFileSystem(true)) {
                    return false;
                }
                ret = doPVRRecord(isRecording);
            }
        }
        return ret;
    }

    public void onKeyPlay() {
        if (curPvrMode == PVR_MODE.E_PVR_MODE_NONE)
            return;
        play.requestFocus();
        if (mPvrManager.isTimeShiftRecording()) {
            if (mPvrManager.getPvrPlaybackSpeed() == TvPvrManager.PVR_PLAYBACK_SPEED_INVALID) {
                int errorCode = TvPvrManager.PVR_STATUS_SUCCESS;
                if (mPvrManager.isAlwaysTimeShiftPlaybackPaused()) {
                    errorCode = mPvrManager.startAlwaysTimeShiftPlayback();
                } else {
                    errorCode = mPvrManager.startPvrTimeShiftPlayback();
                }
                if (TvPvrManager.PVR_STATUS_SUCCESS != errorCode) {
                    toastErrorCode(errorCode);
                    Log.e(TAG, "=========>>>> start(Always)TimeShiftPlayback ErrorCode: "
                            + errorCode);
                    return;
                }
            } else {
                switch (mPvrManager.getPvrPlaybackSpeed()) {
                    case TvPvrManager.PVR_PLAYBACK_SPEED_0X:
                        mPvrManager.resumePlayback();
                        break;
                    case TvPvrManager.PVR_PLAYBACK_SPEED_1X:
                        OnClick_ABLoop();
                        break;
                    default:
                        mPvrManager.setPvrPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_1X);
                        break;
                }
            }
        } else if (curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK) {
            switch (mPvrManager.getPvrPlaybackSpeed()) {
                case TvPvrManager.PVR_PLAYBACK_SPEED_0X:
                    mPvrManager.resumePlayback();
                    break;
                case TvPvrManager.PVR_PLAYBACK_SPEED_1X:
                    OnClick_ABLoop();
                    break;
                default:
                    mPvrManager.setPvrPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_1X);
                    break;
            }
        } else if (curPvrMode == PVR_MODE.E_PVR_MODE_RECORD) {
            final String strFileName = mPvrManager.getCurRecordingFileName();
            int errorCode = mPvrManager.startPvrPlayback(strFileName);
            if (TvPvrManager.PVR_STATUS_SUCCESS != errorCode) {
                toastErrorCode(errorCode);
                Log.e(TAG, "=========>>>> startPvrPlayback ErrorCode: " + errorCode);
                return;
            }
            mPvrManager.jumpPlaybackTime(mPauseTimeInSec);
            /*
             * Don't unmunt audio. MW_DTV_AVMonitor::DoVideoSync will unmute
             * video and audio after signal stable mantis :0793483
             */
            unFreezeImage(false);
            mPauseTimeInSec = 0;
            mPvrManager.assignThumbnailFileInfoHandler(strFileName);
            curPvrMode = PVR_MODE.E_PVR_MODE_PLAYBACK;
            setPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_1X);
        }
        setBarStatusOfPlaybackAll();
        return;
    }

    public void onKeyStop() {
        Log.d(TAG, "onKeyStop curPvrMode:" + curPvrMode);
        if (true == isMenuHide) {
            menuShow();
            return;
        }

        if (true == stopPlaybackLoop()) {
            return;
        }

        if ((true == mBackToBrowser) || (Constant.PVR_PLAYBACK_START == mCreateMode)) {
            stopPlaybacking();
            Log.d(TAG, "Finish()! onKeyStop(), Stop playback....");
            finish();
            return;
        }
        if (PVR_MODE.E_PVR_MODE_PLAYBACK == curPvrMode) {
            stopPlaybacking();
            if (false == mPvrManager.isRecording()) {
                Log.d(TAG, "Finish()! it is not in Recording mode");
                finish();
            }
        } else if (PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT == curPvrMode) {
            stopPlaybacking();
            mPvrManager.pausePvrAlwaysTimeShiftPlayback(false);
            Log.d(TAG, "Finish()! paush always time shift recording...");
            finish();
        } else if (PVR_MODE.E_PVR_MODE_TIME_SHIFT == curPvrMode) {
            stopPlaybacking();
            mPvrManager.stopTimeShiftRecord();
            Log.d(TAG, "Finish()! stop time shift recording...");
            finish();
        } else if (PVR_MODE.E_PVR_MODE_RECORD == curPvrMode) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.str_root_alert_dialog_title)
                    .setMessage(R.string.str_pvr_exit_confirm)
                    .setPositiveButton(R.string.str_root_alert_dialog_confirm,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();
                                    saveAndExit();
                                }
                            })
                    .setNegativeButton(R.string.str_root_alert_dialog_cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.dismiss();
                                }
                            }).show();
        } else {
            Log.d(TAG, "Finish()! onKeyStop(), unhandled case");
            finish();
        }
    }

    private void freezeImage(boolean bMuteAudio) {
        if (true == TvPictureManager.getInstance().isImageFreezed()) {
            return;
        }
        TvPictureManager.getInstance().freezeImage();
        if (bMuteAudio) {
            TvAudioManager.getInstance().enableAudioMute(TvAudioManager.AUDIO_MUTE_BYBLOCK);
        }
    }

    private void unFreezeImage(boolean bUnMuteAudio) {
        if (false == TvPictureManager.getInstance().isImageFreezed()) {
            return;
        }
        TvPictureManager.getInstance().unFreezeImage();
        if (bUnMuteAudio) {
            TvAudioManager.getInstance().disableAudioMute(TvAudioManager.AUDIO_MUTE_BYBLOCK);
        }
    }

    private void setStepInIcon(boolean isStepIn) {
        if (isStepIn) {
            mTextPause.setText(R.string.str_player_stepin);
            pause.setImageResource(R.drawable.player_stepin);
        } else {
            mTextPause.setText(R.string.str_player_pause);
            pause.setImageResource(R.drawable.player_pause);
        }
    }

    public void onKeyPause() {
        Log.e(TAG, "onKeyPause=========>>>> curPvrMode is :" + curPvrMode);
        if (curPvrMode == PVR_MODE.E_PVR_MODE_NONE)
            return;
        if ((curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
                || (curPvrMode == PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT)) {
            if (false == mPvrManager.isPlaybacking()) {
                // freeze img and setup start time
                if (curPvrMode == PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT) {
                    mPvrManager.pausePvrAlwaysTimeShiftPlayback(true);
                } else {
                    mPvrManager.startPvrTimeShiftPlayback();
                    mPvrManager.pausePlayback();
                }
                setBarStatusOfRecordToPause();
            } else {
                mPvrManager.stepInPlayback(); // stop or pause
                setBarStatusOfPlayToPause();
            }
        } else if (curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK) {
            Log.e(TAG, "=========>>>> stepInPlayback !!!!");
            mPvrManager.stepInPlayback(); // stop or pause
            setBarStatusOfPlayToPause();
        } else if (curPvrMode == PVR_MODE.E_PVR_MODE_RECORD) {
            mPauseTimeInSec = mPvrManager.getCurRecordTimeInSecond();
            freezeImage(true);
            setBarStatusOfRecordToPause();
        }
        pause.requestFocus();
        return;
    }

    private boolean isFastForwardPlaying() {
        final int speed = mPvrManager.getPvrPlaybackSpeed();
        if (speed >= TvPvrManager.PVR_PLAYBACK_SPEED_FF_2X
                && speed <= TvPvrManager.PVR_PLAYBACK_SPEED_FF_32X) {
            return true;
        }
        return false;
    }

    private boolean isFastBackPlaying() {
        final int speed = mCurrentSpeed;
        if (speed >= TvPvrManager.PVR_PLAYBACK_SPEED_FB_32X
                && speed <= TvPvrManager.PVR_PLAYBACK_SPEED_FB_1X) {
            return true;
        }
        return false;
    }

    // TODO: FIX ME don't use hard code speed.
    private void updateSpeedIcon(int speed) {
        mCurrentSpeed = speed;
        setStepInIcon(TvPvrManager.PVR_PLAYBACK_SPEED_STEP_IN == speed);
        switch (speed) {
            case TvPvrManager.PVR_PLAYBACK_SPEED_1X:
                playSpeed.setVisibility(View.GONE);
                playSpeed.setText("");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_play_focus);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FF_2X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("2X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_2x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FF_4X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("4X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_4x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FF_8X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("8X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_8x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FF_16X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("16X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_16x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FF_32X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("32X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_32x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_STEP_IN:
                playSpeed.setVisibility(View.GONE);
                playSpeed.setText("");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_pause_focus);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_SF_32X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("Slow/32");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_1_32x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_SF_16X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("Slow/16");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_1_16x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_SF_8X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("Slow/8");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_1_8x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_SF_4X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("Slow/4");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_1_4x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_SF_2X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("Slow/2");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_ff_focus_1_2x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FB_2X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("-2X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_rev_focus_2x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FB_4X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("-4X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_rev_focus_4x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FB_8X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("-8X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_rev_focus_8x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FB_16X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("-16X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_rev_focus_16x);
                break;
            case TvPvrManager.PVR_PLAYBACK_SPEED_FB_32X:
                playSpeed.setVisibility(View.VISIBLE);
                playSpeed.setText("-32X");
                mImagePlaybackStatusImage.setImageResource(R.drawable.player_rev_focus_32x);
                break;
            default:
                playSpeed.setVisibility(View.GONE);
                playSpeed.setText("");
                break;
        }
    }

    void setPlaybackSpeed(int speed) {
        mPvrManager.setPvrPlaybackSpeed(speed);
        updateSpeedIcon(speed);
    }

    public void onKeyRev() {
        Log.d(TAG, "onKeyRev");
        final boolean isPlaybacking = mPvrManager.isPlaybacking();
        if ((false == isPlaybacking) && (PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT == curPvrMode)) {
            mPvrManager.pausePvrAlwaysTimeShiftPlayback(true);
            if (mPvrManager.startAlwaysTimeShiftPlayback() != TvPvrManager.PVR_STATUS_SUCCESS) {
                Log.d(TAG, "Finish()! onKeyRev()....");
                finish();
                return;
            }
            setPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_FB_2X);
            setBarStatusOfPlaybackAll();
            return;
        }

        if (true == isPlaybacking) {
            mPvrManager.doPlaybackFastBackward();
            setBarStatusOfPlaybackAll();
        }
    }

    public void onKeyFF() {
        Log.d(TAG, "onKeyFF");
        if (true == mPvrManager.isPlaybacking()) {
            mPvrManager.doPlaybackFastForward();
            setBarStatusOfPlaybackAll();
        }
    }

    public void onKeySlowMotion() {
        if (mPvrManager.isPlaybacking()) {
            int curPlayBackSpeed = mPvrManager.getPvrPlaybackSpeed();
            int speed = TvPvrManager.PVR_PLAYBACK_SPEED_1X;
            switch (curPlayBackSpeed) {
                case TvPvrManager.PVR_PLAYBACK_SPEED_STEP_IN:
                case TvPvrManager.PVR_PLAYBACK_SPEED_SF_32X:
                    speed = TvPvrManager.PVR_PLAYBACK_SPEED_SF_2X;
                    break;
                case TvPvrManager.PVR_PLAYBACK_SPEED_SF_16X:
                    speed = TvPvrManager.PVR_PLAYBACK_SPEED_SF_32X;
                    break;
                case TvPvrManager.PVR_PLAYBACK_SPEED_SF_8X:
                    speed = TvPvrManager.PVR_PLAYBACK_SPEED_SF_16X;
                    break;
                case TvPvrManager.PVR_PLAYBACK_SPEED_SF_4X:
                    speed = TvPvrManager.PVR_PLAYBACK_SPEED_SF_8X;
                    break;
                case TvPvrManager.PVR_PLAYBACK_SPEED_SF_2X:
                    speed = TvPvrManager.PVR_PLAYBACK_SPEED_SF_4X;
                    break;
                default:
                    speed = TvPvrManager.PVR_PLAYBACK_SPEED_SF_2X;
                    break;
            }
            setPlaybackSpeed(speed);
            setBarStatusOfPlaybackAll();
        }
    }

    public void onKeyGoToTime() {
        if (mPvrManager.isPlaybacking()) {
            showTimeChooseDialog();
        }
    }

    public void onKeyBackward() {
        // for ATshift
        final boolean isPlaybacking = mPvrManager.isPlaybacking();
        if ((false == isPlaybacking) && (PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT == curPvrMode)) {
            mPvrManager.pausePvrAlwaysTimeShiftPlayback(true);
            if (mPvrManager.startAlwaysTimeShiftPlayback() != TvPvrManager.PVR_STATUS_SUCCESS) {
                Log.d(TAG, "Finish()! onKeyBackward()....");
                finish();
                return;
            }
            mPvrManager.doPlaybackJumpBackward();
            setBarStatusOfPlaybackAll();
            return;
        }
        if (mPvrManager.isPlaybacking() && !mPvrManager.isPlaybackPaused()
                && mPvrManager.getPvrPlaybackSpeed() != TvPvrManager.PVR_PLAYBACK_SPEED_STEP_IN) {
            mPvrManager.doPlaybackJumpBackward();
            setBarStatusOfPlaybackAll();
        }
        return;
    }

    public void onKeyForward() {
        if (mPvrManager.isPlaybacking() && !mPvrManager.isPlaybackPaused()
                && mPvrManager.getPvrPlaybackSpeed() != TvPvrManager.PVR_PLAYBACK_SPEED_STEP_IN) {
            mPvrManager.doPlaybackJumpForward();
            setBarStatusOfPlaybackAll();
        }
    }

    private void OnClick_ABLoop() {
        if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE) {
            pvrABLoopStartTime = mPvrManager.getCurPlaybackTimeInSecond();
            setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A;
            textViewPlay.setText(getString(R.string.str_player_play) + " A");
            A_progress = RPProgress.getProgress();
            int x = RPProgress.getWidth() * A_progress
                    / (RPProgress.getMax() == 0 ? 1 : RPProgress.getMax());
            lp4LoopAB.leftMargin = x;
            progress_loopab.setMax(0);
            progress_loopab.setProgress(0);
            progress_loopab.setLayoutParams(lp4LoopAB);
            progress_loopab.setVisibility(View.VISIBLE);
            mProcessLayout.bringChildToFront(progress_loopab);
        } else if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A) {
            pvrABLoopEndTime = mPvrManager.getCurPlaybackTimeInSecond();
            Log.e(TAG, "b-a=" + (pvrABLoopEndTime - pvrABLoopStartTime));
            if (pvrABLoopEndTime - pvrABLoopStartTime <= 2) {
                Toast.makeText(this, R.string.str_pvr_abloop_too_short, Toast.LENGTH_SHORT).show();
                return;
            }
            mPvrManager.startPlaybackLoop(pvrABLoopStartTime, pvrABLoopEndTime);
            setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_AB;

            textViewPlay.setText(getString(R.string.str_player_play) + " A-B");
            lp4LoopAB.width = (RPProgress.getProgress() - A_progress) * RPProgress.getWidth()
                    / (RPProgress.getMax() == 0 ? 1 : RPProgress.getMax());
            progress_loopab.setLayoutParams(lp4LoopAB);
            progress_loopab.setMax(pvrABLoopEndTime - pvrABLoopStartTime);
            mProcessLayout.bringChildToFront(RPProgress);
        } else {
            stopPlaybackLoop();
        }
    }

    private int dip2px(int dipValue) {
        float scale = getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }

    private boolean doPVRRecord(boolean isRecording) {
        if (!isRecording) {
            if (recordDiskPath == null) {
                Log.e(TAG, "USB Disk Path is NULL !!!");
                return false;
            }
            Log.d(TAG, "USB Disk Path = " + recordDiskPath);
            Log.d(TAG, "USB Disk Label = " + recordDiskLable);
            if (false == isSupportedFileSystem(true)) {
                return false;
            }
            mPvrManager.setPvrParams(recordDiskPath, (short) 2);
            final int status = mPvrManager.startPvrRecord();
            Log.d(TAG, "status=" + status);
            if (TvPvrManager.PVR_STATUS_SUCCESS != status) {
                toastErrorCode(status);
                return false;
            }
            final String strFileName = mPvrManager.getCurRecordingFileName();
            Log.d(TAG, "===========>>>> doPVRRecord: current recording fileName = " + strFileName);
            SwitchPageHelper.sLastRecordedFileName = strFileName;
        }
        createIconAnimation();
        setBarStatusOfStartRecord();
        curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
        new usbInfoUpdate().start();
        return true;
    }

    private boolean doPVRTimeShift(boolean isRecording) {
        if (!isRecording) {
            if (recordDiskPath == null) {
                Log.e(TAG, "doPVRTimeShift USB Disk Path is NULL !!!");
                return false;
            }
            if (false == isSupportedFileSystem(true)) {
                return false;
            }
            mPvrManager.setPvrParams(recordDiskPath, (short) 2);
            final int status = mPvrManager.startPvrTimeShiftRecord();
            Log.d(TAG, "status=" + status);
            if (TvPvrManager.PVR_STATUS_SUCCESS != status) {
                toastErrorCode(status);
                return false;
            }
        }
        createIconAnimation();
        curPvrMode = PVR_MODE.E_PVR_MODE_TIME_SHIFT;
        setBarStatusOfRecordToPause();
        new usbInfoUpdate().start();
        return true;
    }

    private void resumeSubtitle() {
        DtvSubtitleInfo subtitleInfo;

        subtitleInfo = mTvChannelManager.getSubtitleInfoForUiDisp();
        if (subtitleInfo != null) {
            if (subtitlePosLive != -1 && subtitlePosLive <= subtitleInfo.subtitleServiceNumber
                    && subtitlePosLive != subtitleInfo.currentSubtitleIndex + 1) {
                mTvChannelManager.closeSubtitle();
                mTvChannelManager.openSubtitle((subtitlePosLive - 1));
                SharedPreferences settings = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                        Context.MODE_PRIVATE);
                Editor editor = settings.edit();
                editor.putInt("subtitlePos", subtitlePosLive);
                editor.commit();
                subtitlePosLive = -1;
            }
        }
    }

    private void resumeCloseCaption() {
        if (TvCommonManager.getInstance().isSupportModule(
                TvCommonManager.MODULE_ATSC_CC_ENABLE)
                || TvCommonManager.getInstance().isSupportModule(
                        TvCommonManager.MODULE_NTSC_CC_ENABLE)) {
            // ATSC TV System using [DTV]MODULE_ATSC_CC_ENABLE +
            // [ATV]MODULE_NTSC_CC_ENABLE
            int closedCaptionMode = TvCcManager.getInstance().getNextClosedCaptionMode();
            TvCcManager.getInstance().setClosedCaptionMode(closedCaptionMode);
            TvCcManager.getInstance().stopCc();
            if (TvCcManager.CLOSED_CAPTION_OFF != closedCaptionMode) {
                TvCcManager.getInstance().startCc();
            }
        }
    }

    private void resumeLang() {
        DtvAudioInfo audioInfo = new DtvAudioInfo();
        audioInfo = mTvChannelManager.getAudioInfo_ex();
        if (audioInfo != null) {
            if (audioLangPosLive != -1 && audioInfo.audioLangNum > audioLangPosLive
                    && audioInfo.currentAudioIndex != audioLangPosLive) {
                mTvChannelManager.switchAudioTrack(audioLangPosLive);
                audioLangPosLive = -1;
            }
        }
    }

    private void saveAndExit() {
        showSavingProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                stopRecord();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (null != savingProgressDialog) {
                            savingProgressDialog.dismiss();
                        }
                        Log.d(TAG, "Finish()! saveAndExit()....");
                        finish();
                    }
                });
            }
        }).start();
    }

    private boolean stopPlaybackLoop() {
        boolean exitFromLoop = false;
        if (PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE != setPvrABLoop) {
            exitFromLoop = true;
            if (PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_AB == setPvrABLoop) {
                mPvrManager.stopPlaybackLoop();
            }
            pvrABLoopStartTime = pvrABLoopEndTime = INVALID_TIME;
            setPvrABLoop = PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_NONE;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewPlay.setText(getString(R.string.str_player_play));
                    lp4LoopAB.width = 0;
                    progress_loopab.setMax(0);
                    progress_loopab.setVisibility(View.GONE);
                }
            });
        }
        return exitFromLoop;
    }

    private void stopPlaybacking() {
        if (false == mPvrManager.isPlaybacking()) {
            return;
        }

        stopPlaybackLoop();

        if (PVR_MODE.E_PVR_MODE_TIME_SHIFT == curPvrMode) {
            mPvrManager.stopTimeShiftPlayback();
        } else if (PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT == curPvrMode) {
            mPvrManager.pausePvrAlwaysTimeShiftPlayback(false);
            mPvrManager.stopAlwaysTimeShiftPlayback();
        } else {
            final String curPlaybackingFileName = mPvrManager.getCurPlaybackingFileName();
            mPvrManager.setPvrFileResumePoint(curPlaybackingFileName, AVAILABLE_PERSON_INDEX);
            mCurrentTime = 0;
            resumeLang();
            if (TvCommonManager.getInstance().getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ATSC) {
                resumeSubtitle();
            } else {
                resumeCloseCaption();
            }
            mPvrManager.stopPlayback();
            final boolean isRecording = mPvrManager.isRecording();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateSpeedIcon(TvPvrManager.PVR_PLAYBACK_SPEED_INVALID);
                    if (true == isRecording) {
                        curPvrMode = PVR_MODE.E_PVR_MODE_RECORD;
                        setBarStatusOfStartRecord();
                    }
                }
            });
        }
    }

    private void updateUSBInfo() {
        try {
            StatFs sf = new StatFs(recordDiskPath);
            final int percent = (int) (100 - ((sf.getFreeBlocksLong() * 100) / sf
                    .getBlockCountLong()));
            mTextUsbPercentage.setText(percent + "%");
            usbFreeSpace.setProgress(percent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createIconAnimation() {
        if (null != recordIconAnimation) {
            return;
        }
        mRelativeRecordingView.setVisibility(View.VISIBLE);
        recordIconAnimation = new AnimatorSet();
        ObjectAnimator IconfadeOutAlphaAnim = ObjectAnimator.ofFloat(
                mRelativeRecordingView.findViewById(R.id.pvrrecordimage), "alpha", 1f, 0f);
        IconfadeOutAlphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        IconfadeOutAlphaAnim.setDuration(2000);
        IconfadeOutAlphaAnim.setRepeatCount(Animation.INFINITE);
        IconfadeOutAlphaAnim.setRepeatMode(Animation.RESTART);
        IconfadeOutAlphaAnim.addListener(new AnimatorListener() {
            private int count = 0;

            private TextView text = (TextView) mRelativeRecordingView
                    .findViewById(R.id.pvrrecordtext);

            private String strName = getResources().getString(R.string.str_pvr_is_recording);

            @Override
            public void onAnimationStart(Animator animation) {
                text.setText(strName + ".");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                String str = strName;
                for (int i = 0; i < count + 1; i++) {
                    str += ".";
                }
                text.setText(str);
                count = (count + 1) % 8;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        recordIconAnimation.play(IconfadeOutAlphaAnim);
        recordIconAnimation.start();
    }

    private void createAnimation() {
        if ((null != menuShowAnimation) && (null != menuHideAnimation)) {
            return;
        }
        int height = mRelativeRootView.getHeight() + mRelativeRootView.getPaddingBottom();
        ObjectAnimator fadeInAlphaAnim = ObjectAnimator.ofFloat(mRelativeRootView, "alpha", 0f, 1f);
        fadeInAlphaAnim.setInterpolator(new DecelerateInterpolator());
        fadeInAlphaAnim.setDuration(300);
        ObjectAnimator fadeOutAlphaAnim = ObjectAnimator
                .ofFloat(mRelativeRootView, "alpha", 1f, 0f);
        fadeOutAlphaAnim.setInterpolator(new DecelerateInterpolator());
        fadeOutAlphaAnim.setDuration(300);
        ObjectAnimator moveUpAnim = ObjectAnimator.ofFloat(mRelativeRootView, "translationY",
                height, 0);
        moveUpAnim.setInterpolator(new DecelerateInterpolator());
        moveUpAnim.setDuration(300);
        ObjectAnimator moveDownAnim = ObjectAnimator.ofFloat(mRelativeRootView, "translationY", 0,
                height);
        moveDownAnim.setInterpolator(new DecelerateInterpolator());
        moveDownAnim.setDuration(300);
        menuShowAnimation = new AnimatorSet();
        menuHideAnimation = new AnimatorSet();
        menuShowAnimation.play(moveUpAnim).with(fadeInAlphaAnim);
        menuHideAnimation.play(moveDownAnim).with(fadeOutAlphaAnim);
        menuShowAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isMenuHide = false;
                mRelativeRootView.setVisibility(View.VISIBLE);
                mRelativeRootView.requestFocus();
            }
        });
        menuHideAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRelativeRootView.setVisibility(View.GONE);
                isMenuHide = true;
            }
        });
    }

    private void menuShow() {
        Log.d(TAG, "=============>>>> menuShow = " + isMenuHide);
        if (isMenuHide) {
            menuHideAnimation.end();
            menuShowAnimation.start();
            if (mPvrManager.isPlaybacking()) {
                mRelativeRecordingView.setVisibility(View.GONE);
                mTextPlaybackStatusText.setVisibility(View.GONE);
            } else {
                mImagePlaybackStatusImage.setImageResource(R.drawable.idle_img_press_ststus_pvr);
                mRelativeRecordingView.setVisibility(View.GONE);
                mTextPlaybackStatusText.setText(R.string.str_pvr_is_recording);
                mTextPlaybackStatusText.setVisibility(View.GONE);
            }
        }
    }

    private void menuHide() {
        Log.d(TAG, "=============>>>> menuHide = " + !isMenuHide);
        if (!isMenuHide) {
            menuShowAnimation.end();
            menuHideAnimation.start();
            if (mPvrManager.isPlaybacking()) {
                mRelativeRecordingView.setVisibility(View.VISIBLE);
                mTextPlaybackStatusText.setVisibility(View.GONE);
            } else {
                mImagePlaybackStatusImage.setImageResource(R.drawable.idle_img_press_ststus_pvr);
                mRelativeRecordingView.setVisibility(View.VISIBLE);
                mTextPlaybackStatusText.setText(R.string.str_pvr_is_recording);
                mTextPlaybackStatusText.setVisibility(View.VISIBLE);
            }
        }
    }

    /*
     * update mCurrentTime or mTotalTime before using. TODO: move
     * mCurrentTime/mTotalTime to input arguments
     */
    private void updateProgress(final int reason) {
        final boolean isPlaybacking = mPvrManager.isPlaybacking();
        final int currentTime = isPlaybacking ? mCurrentTime : 0;
        final int total = mTotalTime;
        Log.d(TAG, "reason: " + reason + ", current: " + mCurrentTime + "/  total: " + mTotalTime);
        mTextTotalRecordTime.setText(getTimeString(total));
        if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_AB) {
            RPProgress.setMax(total);
            final int currentlooptime = currentTime - pvrABLoopStartTime;
            RPProgress.setTextProgress(getTimeString(currentTime), 0);
            if (currentlooptime > progress_loopab.getMax()) {
                progress_loopab.setMax(currentlooptime);
            }
            progress_loopab.setProgress(currentlooptime);
        } else if (setPvrABLoop == PVR_AB_LOOP_STATUS.E_PVR_AB_LOOP_STATUS_A) {
            if (true == isNeedToSetProgressMax(reason)) {
                RPProgress.setMax(total);
                RPProgress.setTextProgress(getTimeString(currentTime), currentTime);
                lp4LoopAB.width = (RPProgress.getProgress() - A_progress) * RPProgress.getWidth()
                        / (RPProgress.getMax() == 0 ? 1 : RPProgress.getMax());
                progress_loopab.setLayoutParams(lp4LoopAB);
            }
        } else {
            if (true == isNeedToSetProgressMax(reason)) {
                RPProgress.setMax(total);
                RPProgress.setTextProgress(getTimeString(currentTime), currentTime);
            }
        }
        if (isFastBackPlaying() && (currentTime <= 0)) {
            // back to normal when FB to begin.
            playSpeed.setVisibility(View.GONE);
            playSpeed.setText("");
            mPvrManager.setPvrPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_1X);
        }
    }

    private boolean isNeedToSetProgressMax(final int reason) {
        boolean isPauseMode = false;
        if (mTextPause.getText() .equals(getResources().getString(R.string.str_player_pause))) {
            isPauseMode = true;
        }
        if ((RECORDTIME_TIME_UPDATED == reason) && (true == isPauseMode)
                && (mPvrManager.isPlaybacking() && mPvrManager.isRecording())) {
            /*
             * FIXME: This is a patch solution. For the case: PVR playback + PVR
             * recording at the same time The max value of progress bar is no
             * need to update; it is updated when next time playback time update
             * notification received. This is not a good solution, but the
             * modifications can solve mantis issue 1043928 and 1043911.
             */
            Log.d(TAG, "No need to update progress bar");
            return false;
        } else {
            return true;
        }
    }

    private Handler progressBarHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (PLAYBACK_TIME_UPDATED == msg.what) {
                mCurrentTime = msg.arg1;
                updateProgress(PLAYBACK_TIME_UPDATED);
            } else if ((RECORDTIME_TIME_UPDATED == msg.what)) {
                mTotalTime = msg.arg1;
                updateProgress(RECORDTIME_TIME_UPDATED);
            }
        };
    };

    private class usbInfoUpdate extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while ((mPvrManager.isPlaybacking() || mPvrManager.isRecording()) && !isFinishing()) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            updateUSBInfo();
                        }
                    });
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getTimeString(int seconds) {
        String hour = "00";
        String minute = "00";
        String second = "00";
        if (seconds % 60 < 10)
            second = "0" + seconds % 60;
        else
            second = "" + seconds % 60;

        int offset = seconds / 60;
        if (offset % 60 < 10)
            minute = "0" + offset % 60;
        else
            minute = "" + offset % 60;

        offset = seconds / 3600;
        if (offset < 10)
            hour = "0" + offset;
        else
            hour = "" + offset;
        return hour + ":" + minute + ":" + second;
    }

    private void showPasswordLock(boolean bShow) {
        if (true == bShow) {
            mPasswordLock.show();
        } else {
            mPasswordLock.dismiss();
        }
    }

    private void toastOnUiThread(final int resId) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(activity, resId, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private boolean sendMheg5Key(int keyCode) {
        if (mTvChannelManager.isMheg5Running()) {
            return mTvChannelManager.sendMheg5Key(keyCode);
        } else {
            Log.i(TAG, "isMheg5Running return fali!");
        }
        return false;
    }

    private class TvPlayerEventListener implements OnTvPlayerEventListener {

        @Override
        public boolean onScreenSaverMode(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onHbbtvUiEvent(int what, HbbtvEventInfo eventInfo) {
            return false;
        }

        @Override
        public boolean onPopupDialog(int what, int arg1, int arg2) {
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackTime(int what, int arg1) {
            Message m = Message.obtain();
            m.what = PLAYBACK_TIME_UPDATED;
            m.arg1 = arg1;
            progressBarHandler.sendMessage(m);
            return true;
        }

        @Override
        public boolean onPvrNotifyPlaybackSpeedChange(int what) {
            Log.d(TAG, "onPvrNotifyPlaybackSpeedChange");
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    updateSpeedIcon(mPvrManager.getPvrPlaybackSpeed());
                }
            });
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordTime(int what, int arg1) {
            /*
             * When play not recording file don't update total time base on
             * record time
             */
            if (false == mUpdateTotalTime) {
                return true;
            }
            Message m = Message.obtain();
            m.what = RECORDTIME_TIME_UPDATED;
            m.arg1 = arg1;
            progressBarHandler.sendMessage(m);
            return true;
        }

        @Override
        public boolean onPvrNotifyRecordSize(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyRecordStop(int what) {
            Log.d(TAG, "onPvrNotifyRecordStop");
            if (TvCiManager.getInstance().getCardState().ordinal() == TvCiManager.CARD_STATE_NO) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Remove CI card! Finish()! onPvrNotifyRecordStop()....");
                        finish();
                    }
                });
            }
            return false;
        }

        @Override
        public boolean onPvrNotifyPlaybackBegin(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesBefore(int what, int arg1) {
            Log.d(TAG, "onPvrNotifyTimeShiftOverwritesBefore arg1:" + arg1);
            final int timeShiftOverWriteBefore = arg1;
            if (TIME_SHIFE_OVERWRITE_COUNTDOWN_SECS < timeShiftOverWriteBefore) {
                return true;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (0 == timeShiftOverWriteBefore) {
                        mLinearOverWrite.setVisibility(View.GONE);
                    } else {
                        mTextTImeShiftOverWrite.setText(getTimeString(timeShiftOverWriteBefore));
                        mLinearOverWrite.setVisibility(View.VISIBLE);
                    }
                }
            });
            return true;
        }

        @Override
        public boolean onPvrNotifyTimeShiftOverwritesAfter(int what, int arg1) {
            Log.d(TAG, "onPvrNotifyTimeShiftOverwritesAfter arg1:" + arg1);
            final int timeShiftMin = arg1;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLinearOverWrite.setVisibility(View.GONE);
                    mTextStartTime.setText(getTimeString(timeShiftMin));
                    RPProgress.setMin(timeShiftMin);
                }
            });

            return false;
        }

        @Override
        public boolean onPvrNotifyOverRun(int what) {
            toastOnUiThread(R.string.str_disk_too_slow);
            return true;
        }

        @Override
        public boolean onPvrNotifyUsbRemoved(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onPvrNotifyCiPlusProtection(int what) {
            toastOnUiThread(R.string.str_pvr_ciplus_copy_protection);
            stopPlaybacking();
            if (curPvrMode == PVR_MODE.E_PVR_MODE_ALWAYS_TIME_SHIFT) {
                mPvrManager.pausePvrAlwaysTimeShiftPlayback(false);
            } else if (curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT) {
                mPvrManager.stopTimeShiftRecord();
            } else if (curPvrMode == PVR_MODE.E_PVR_MODE_RECORD) {
                stopRecord();
                return true;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Finish()! onPvrNotifyCiPlusProtection()....");
                    finish();
                }
            });
            return true;
        }

        @Override
        public boolean onPvrNotifyPlaybackStop(int what) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    /*
                     * PVR SPEC: Timeshift playback file end, jump to
                     * TIMESHIFT_PLAYBACK_DELAY_SECS secs before end of file and
                     * back to normal play. exp: FF/Jump to End, total time 100
                     * secs, jump to 95 secs exp: FF/Jump to End, total time 3
                     * secs, jump to 0 secs (if total time less then
                     * TIMESHIFT_PLAYBACK_DELAY_SECS)
                     */
                    final boolean isTimeShiftRecording = mPvrManager.isTimeShiftRecording();
                    if (true == isTimeShiftRecording) {
                        final String curPlaybackingFileName = mPvrManager
                                .getCurPlaybackingFileName();
                        final int total = mPvrManager
                                .getRecordedFileDurationTime(curPlaybackingFileName);
                        final int jumpToTimeInSeconds = ((total - TIMESHIFT_PLAYBACK_DELAY_SECS) > 0) ? (total - TIMESHIFT_PLAYBACK_DELAY_SECS)
                                : 0;
                        playSpeed.setVisibility(View.GONE);
                        playSpeed.setText("");
                        mPvrManager.setPvrPlaybackSpeed(TvPvrManager.PVR_PLAYBACK_SPEED_1X);
                        mPvrManager.jumpPlaybackTime(jumpToTimeInSeconds);
                    } else {
                        if (mPvrManager.isPlaybacking()) {
                            menuShow();
                            stopPlaybackLoop();
                            onKeyStop();
                        }
                    }
                }
            });
            return true;
        }

        @Override
        public boolean onPvrNotifyParentalControl(int what, int arg1) {
            final boolean isLock = (arg1 == 1);
            mIsToPromptPassword = isLock;
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    showPasswordLock(isLock);
                }
            });
            return true;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int what) {
            return false;
        }

        @Override
        public boolean onPvrNotifyCiPlusRetentionLimitUpdate(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onTvProgramInfoReady(int what) {
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
            return false;
        }

        @Override
        public boolean onDtvChannelInfoUpdate(int what, int info, int arg2) {
            return false;
        }
    }
}
