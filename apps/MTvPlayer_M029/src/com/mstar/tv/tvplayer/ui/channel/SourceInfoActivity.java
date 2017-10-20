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

package com.mstar.tv.tvplayer.ui.channel;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.format.DateFormat;
import android.text.format.Time;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.DTVSpecificProgInfo;
import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfoQueryCriteria;
import com.mstar.android.tvapi.common.vo.VideoInfo;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEitInfo;
import com.mstar.android.tvapi.dtv.vo.DtvSubtitleInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscEpgEventInfo;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.SwitchPageHelper;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.pvr.PVRActivity;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant.SignalProgSyncStatus;
import com.mstar.util.Constant;
import com.mstar.util.TvEvent;
import com.mstar.util.Utility;
import android.os.SystemProperties;

public class SourceInfoActivity extends MstarBaseActivity {

    private static final String TAG = "SourceInfoActivity";

    private final int CMD_SET_CURRENT_TIME = 0x00;

    private final int CMD_UPDATE_SOURCE_INFO = 0X01;

    private final int CMD_SIGNAL_LOCK = 0x02;

    private final int CMD_SCREEN_SAVER = 0x03;

    private int mTvSystem = 0;

    private final int MAX_TIMES = 10;

    private final int mCecStatusOn = 1;

    private int mCount = 0;

    private final int MAX_VALUE_OF_ONE_DIGIT = 9;

    private final int MAX_VALUE_OF_TWO_DIGIT = 99;

    private final int MAX_VALUE_OF_THREE_DIGIT = 999;

    private final int MAX_VALUE_OF_FOUR_DIGIT = 9999;

    private int mCountry = TvCountry.OTHERS;

    /** Show the source info 6 seconds for all ATV and DTV standards */
    private static final int TIMEOUT_INTERVAL = 6000;

    private TvChannelManager mTvChannelManager = null;

    private int mInputSource = TvCommonManager.INPUT_SOURCE_NONE;

    private int mPreviousInputSource = TvCommonManager.INPUT_SOURCE_NONE;

    private boolean mIsPresentEvent = true;

    private VideoInfo mVideoInfo = null;

    private String mStr_video_info = null;

    private int mCurChannelNumber = 1;

    private ImageView mTvImageView = null;

    private TextView mTitle = null;

    private TextView mInfo = null;

    private ImageView mFirstTvNumberIcon = null;

    private ImageView mSecondTvNumberIcon = null;

    private ImageView mThirdTvNumberIcon = null;

    private ImageView mFourthTvNumberIcon = null;

    private ImageView mFifthTvNumberIcon = null;

    private ImageView mTvDotIcon = null;

    private ImageView mDigitMinorTvNumberIcon1 = null;

    private ImageView mDigitMinorTvNumberIcon2 = null;

    private ImageView mDigitMinorTvNumberIcon3 = null;

    private ImageView mDigitMinorTvNumberIcon4 = null;

    private ImageView mSslImage = null;

    private ImageView mChannelLogo = null;
    
	private ImageView mSource_info_program_imageshow = null;
	
	private ImageView mSource_info_program_flag = null;

    private TextView mSource_info_tvnumber;

    private TextView mSource_info_tvnumber_value;

    private TextView mSource_info_tvname;
    
    private TextView mSource_lock;
    private ImageView mSource_lock_img;

    private TextView mSource_info_teletext;

    private TextView mSource_info_program_name;

    private TextView mSource_info_Subtitle;

    private TextView mSource_info_mheg5;

    private TextView mSource_info_video_format;

    private TextView mSource_info_audio_format;

    private TextView mSource_info_program_type;

    private TextView mSource_info_program_period;

    private TextView mSource_info_description;

    private TextView mSource_info_resolution;

    private TextView mSource_info_language;

    private TextView mSource_info_imageformat;

    private TextView mSource_info_soundformat;

    private TextView mSource_info_audioformat;

    private TextView mSource_info_age;

    private TextView mSource_info_cc;

    private TextView mSource_epg_event_name;

    private TextView mSource_epg_event_rating;

    private String mStr;

    private TextView mCurrentTime = null;

    private TvEpgManager mTvEpgManager = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private TvCecManager mTvCecManager = null;

    private TvIsdbChannelManager mTvIsdbChannelManager = null;

    private TvTimerManager mTvTimerManager = null;

    protected TimerTask mTimerTask;

    // protected ST_Time curDateTime;
    public static boolean isDTVChannelNameReady = true;

    // private static boolean isATVProgramInfoReady = true;
    protected Timer mTimer;

    private Intent mIntent = null;

    private boolean mIsBackRoot = false;

    private DtvEitInfo mDtvEitInfo;

    private int[] mNumberResIds = {
            R.drawable.popup_img_number_0, R.drawable.popup_img_number_1,
            R.drawable.popup_img_number_2, R.drawable.popup_img_number_3,
            R.drawable.popup_img_number_4, R.drawable.popup_img_number_5,
            R.drawable.popup_img_number_6, R.drawable.popup_img_number_7,
            R.drawable.popup_img_number_8, R.drawable.popup_img_number_9
    };

    private String[] mServiceTypeDisplayString = {
            "", "DTV", "RADIO", "DATA", "UNITED_TV", "INVALID"
    };

    private String[] mVideoTypeDisplayString;

    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    private OnDtvPlayerEventListener mDtvPlayerEventListener = null;

    private OnTvPlayerEventListener mTvPlayerEventListener = null;

    // add by jerry
    int[] sourceList;
    int mHdmiNum;
    int mCvbsNum;
    int mYpbprNum;

    private TimeZone mTimeZone;
	private Calendar mTime;
	private String date;
	private String clock;

    private class TvPlayerEventListener implements OnTvPlayerEventListener {
        @Override
        public boolean onScreenSaverMode(int what, int arg1) {
            if ((TvCommonManager.INPUT_SOURCE_HDMI <= mInputSource)
                    && (TvCommonManager.INPUT_SOURCE_HDMI_MAX > mInputSource)) {
                if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == arg1) {
                    myHandler.sendEmptyMessage(CMD_SCREEN_SAVER);
                    return true;
                }
            }
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
            return false;
        }

        @Override
        public boolean onPvrNotifyUsbRemoved(int what, int arg1) {
            return false;
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
            Log.i(TAG, "onEpgUpdateList()");
            SourceInfoActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                        updateSourceInfo();
                    }
                }
            });
            return true;
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
            Bundle b = new Bundle();
            Message msg = myHandler.obtainMessage();
            msg.what = CMD_UPDATE_SOURCE_INFO;
            b.putInt("CmdIndex", TvEvent.ATV_PROGRAM_INFO_READY);
            myHandler.sendMessage(msg);
            return false;
        }
    }

    private class DtvPlayerEventListener implements OnDtvPlayerEventListener {

        @Override
        public boolean onDtvChannelNameReady(int what) {
            SourceInfoActivity.isDTVChannelNameReady = true;
            Bundle b = new Bundle();
            Message msg = myHandler.obtainMessage();
            msg.what = CMD_UPDATE_SOURCE_INFO;
            b.putInt("CmdIndex", TvEvent.DTV_CHANNELNAME_READY);
            myHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
            return false;
        }

        @Override
        public boolean onDtvProgramInfoReady(int what) {
            Bundle b = new Bundle();
            Message msg = myHandler.obtainMessage();
            msg.what = CMD_UPDATE_SOURCE_INFO;
            b.putInt("CmdIndex", TvEvent.DTV_PROGRAM_INFO_READY);
            msg.setData(b);
            myHandler.sendMessage(msg);
            return true;
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
            return false;
        }

        @Override
        public boolean onMheg5ReturnKey(int what, int arg1) {
            return false;
        }

        @Override
        public boolean onOadHandler(int what, int arg1, int arg2) {
            return false;
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
            return false;
        }

        @Override
        public boolean onOadTimeout(int what, int arg1) {
            return false;
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
            return false;
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

    protected Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case CMD_SET_CURRENT_TIME:                 
                   onTimeChanged();
                   mCurrentTime.setText(getString(R.string.str_time_time) + " : "+date+"  "+clock);
                   // Time curTime = mTvTimerManager.getCurrentTvTime();
                  //  mCurrentTime.setText(getString(R.string.str_time_time) + " : "
                 //           + curTime.format("%H:%M %Y/%m/%d %a"));
                    break;
                case CMD_UPDATE_SOURCE_INFO:
                    int CmdIndex = bundle.getInt("CmdIndex");
                    onMSrvMsgCmd(CmdIndex);
                    break;

                case CMD_SIGNAL_LOCK:
                case CMD_SCREEN_SAVER:
                    updateSourceInfo();
                    break;

                default:
                    break;
            }
        }
    };

    private void onMSrvMsgCmd(int index) {
        if (index == TvEvent.DTV_CHANNELNAME_READY) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                isDTVChannelNameReady = true;
            }
            mIsPresentEvent = true;
            updateChannelInfo();
        } else if (index == TvEvent.DTV_PROGRAM_INFO_READY) {
            updateSourceInfo();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Finish source info after TIMEOUT_INTERVAL seconds */
        setTimerInterval(TIMEOUT_INTERVAL);
        mVideoTypeDisplayString = getResources().getStringArray(R.array.str_arr_video_type);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mTvEpgManager = TvEpgManager.getInstance();
        mTvCecManager = TvCecManager.getInstance();
        mTvTimerManager = TvTimerManager.getInstance();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
            isDTVChannelNameReady = true;
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mTvIsdbChannelManager = TvIsdbChannelManager.getInstance();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_SOURCE_INFO_DISAPPEAR);
        filter.addAction(TvIntent.ACTION_SIGNAL_LOCK);
        filter.addAction(TvIntent.ACTION_TV_TIME_ZONE_CHANGE);
        registerReceiver(mReceiver, filter);
        mTvChannelManager = TvChannelManager.getInstance();
        mCountry = mTvChannelManager.getSystemCountryId();
        checkInputSourceAndInitView();
        setInvisible();

        sourceList = TvCommonManager.getInstance().getSourceList();
        mHdmiNum = 0;
        mCvbsNum = 0;
        mYpbprNum = 0;
        for (int i=0; i < sourceList.length; i++) {
            if (sourceList[i] == 0) continue;
        	if (i >= TvCommonManager.INPUT_SOURCE_HDMI && i < TvCommonManager.INPUT_SOURCE_HDMI_MAX) {
        		mHdmiNum += 1;
        	} else if (i >= TvCommonManager.INPUT_SOURCE_CVBS && i < TvCommonManager.INPUT_SOURCE_CVBS_MAX) {
        		mCvbsNum += 1;
        	} else if (i >= TvCommonManager.INPUT_SOURCE_YPBPR && i < TvCommonManager.INPUT_SOURCE_YPBPR_MAX) {
        		mYpbprNum += 1;
        	}
        }

        mTimeZone = TimeZone.getDefault();
		createTime(mTimeZone.getDisplayName());
		date =new SimpleDateFormat("yyyy-MM-dd").format(mTime.getTime());
		boolean is24HourFormat = DateFormat.is24HourFormat(this);
		clock =new SimpleDateFormat(is24HourFormat ? "HH:mm:ss" : "hh:mm:ss a").format(mTime.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // receive dtvlistener call back
        mCount = 0;

        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mDtvPlayerEventListener = new DtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mTvPlayerEventListener = new TvPlayerEventListener();
        TvChannelManager.getInstance().registerOnTvPlayerEventListener(mTvPlayerEventListener);

        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            try {
                if (mTimer == null)
                    mTimer = new Timer();
                if (mTimerTask == null)
                    mTimerTask = getTimerTask();
                if (mTimer != null && mTimerTask != null)
                    mTimer.schedule(mTimerTask, 10, 100);
            } catch (Exception e) {
            }
        }
        if (mInputSource == TvCommonManager.INPUT_SOURCE_HDMI
                || mInputSource == TvCommonManager.INPUT_SOURCE_HDMI2
                || mInputSource == TvCommonManager.INPUT_SOURCE_HDMI3
                || mInputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
            // this will call updateSourceInfo() delay.
            //myHandler.sendEmptyMessageDelayed(CMD_SIGNAL_LOCK, 1000);
            myHandler.sendEmptyMessage(CMD_SIGNAL_LOCK);
        } else {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                // this will call updateSourceInfo() delay.
                //myHandler.sendEmptyMessageDelayed(CMD_SIGNAL_LOCK, 1000);
                myHandler.sendEmptyMessage(CMD_SIGNAL_LOCK);
            } else {
                // this will call updateSourceInfo() delay.
                //myHandler.sendEmptyMessageDelayed(CMD_SIGNAL_LOCK, 300);
				myHandler.sendEmptyMessage(CMD_SIGNAL_LOCK);
            }
        }
        if (mInputSource == TvCommonManager.INPUT_SOURCE_YPBPR) {
            try {
                if (mTimer == null)
                    mTimer = new Timer();
                if (mTimerTask == null)
                    mTimerTask = getTimerTask();
                if (mTimer != null && mTimerTask != null)
                    mTimer.schedule(mTimerTask, 10, 100);
            } catch (Exception e) {
            }
        }

        // Notify the the password prompt for parental block is forced revealed if exists.
        forcePasswordPrompt(true);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mAtvPlayerEventListener = null;
        TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mDtvPlayerEventListener = null;
        TvChannelManager.getInstance().unregisterOnTvPlayerEventListener(mTvPlayerEventListener);
        mTvPlayerEventListener = null;

        try {
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
            mTimer.cancel();
            mTimer = null;
        } catch (Exception e) {
        }

        if (false == mIsBackRoot) {
            // Notify the the password prompt for parental block is not forced revealed anymore
            forcePasswordPrompt(false);
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void setInvisible() {
        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            mFirstTvNumberIcon.setVisibility(View.INVISIBLE);
            mSecondTvNumberIcon.setVisibility(View.INVISIBLE);
            mThirdTvNumberIcon.setVisibility(View.INVISIBLE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.INVISIBLE);
                mFifthTvNumberIcon.setVisibility(View.INVISIBLE);
                mTvDotIcon.setVisibility(View.INVISIBLE);
                mDigitMinorTvNumberIcon1.setVisibility(View.INVISIBLE);
                mDigitMinorTvNumberIcon2.setVisibility(View.INVISIBLE);
                mDigitMinorTvNumberIcon3.setVisibility(View.INVISIBLE);
                mDigitMinorTvNumberIcon4.setVisibility(View.INVISIBLE);
            }
            mCurrentTime.setVisibility(View.INVISIBLE);
            mSource_info_tvnumber.setVisibility(View.INVISIBLE);
            mSource_info_tvnumber_value.setVisibility(View.INVISIBLE);
            mSource_info_tvname.setVisibility(View.INVISIBLE);
        }
    }

    private void checkInputSourceAndInitView() {
        mPreviousInputSource = mInputSource;
        mInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();

        if (mInputSource == mPreviousInputSource) {
            return;
        }

        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            setContentView(R.layout.source_info_atv);
            mTvImageView = (ImageView) findViewById(R.id.source_info_imageView);
            mTitle = (TextView) findViewById(R.id.source_info_title);
            mFirstTvNumberIcon = (ImageView) findViewById(R.id.source_info_image1);
            mSecondTvNumberIcon = (ImageView) findViewById(R.id.source_info_image2);
            mThirdTvNumberIcon = (ImageView) findViewById(R.id.source_info_image3);
            mCurrentTime = (TextView) findViewById(R.id.source_info_tvtime);
            mSource_info_tvnumber = (TextView) findViewById(R.id.source_info_tvnumber);
            mSource_info_tvnumber_value = (TextView) findViewById(R.id.source_info_tvnumber_value);
            mSource_info_tvname = (TextView) findViewById(R.id.source_info_tvname);
            mSource_info_imageformat = (TextView) findViewById(R.id.source_info_imageformat);
            mSource_info_soundformat = (TextView) findViewById(R.id.source_info_soundformat);
            mSource_info_audioformat = (TextView) findViewById(R.id.source_info_audioformat);
            mSource_info_program_flag = (ImageView) findViewById(R.id.source_info_flag);
            
            mSource_lock =(TextView) findViewById(R.id.source_info_lock);
            mSource_lock_img =(ImageView) findViewById(R.id.source_info_lock_img);
            
            if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                mSource_epg_event_rating = (TextView) findViewById(R.id.source_info_epg_rating_atv);
                mSource_epg_event_rating.setVisibility(View.VISIBLE);
            }
            mTitle.setText(getResources().getString(R.string.source_info_ATV));
        } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            setContentView(R.layout.source_info_dtv);
            ((TextView) findViewById(R.id.source_info_description_title))
                    .setText(getString(R.string.str_dtv_source_info_description) + " : ");
            mTvImageView = (ImageView) findViewById(R.id.source_info_imageView);
            mTitle = (TextView) findViewById(R.id.source_info_title);
            mFirstTvNumberIcon = (ImageView) findViewById(R.id.source_info_image1);
            mSecondTvNumberIcon = (ImageView) findViewById(R.id.source_info_image2);
            mThirdTvNumberIcon = (ImageView) findViewById(R.id.source_info_image3);
            mFourthTvNumberIcon = (ImageView) findViewById(R.id.source_info_image4);
            mFifthTvNumberIcon = (ImageView) findViewById(R.id.source_info_image5);
            mTvDotIcon = (ImageView) findViewById(R.id.source_info_image_dot);
            mDigitMinorTvNumberIcon1 = (ImageView) findViewById(R.id.source_info_image_minor_1);
            mDigitMinorTvNumberIcon2 = (ImageView) findViewById(R.id.source_info_image_minor_2);
            mDigitMinorTvNumberIcon3 = (ImageView) findViewById(R.id.source_info_image_minor_3);
            mDigitMinorTvNumberIcon4 = (ImageView) findViewById(R.id.source_info_image_minor_4);
            mSource_info_program_imageshow = (ImageView) findViewById(R.id.source_info_program_imageshow);
            mSslImage = (ImageView) findViewById(R.id.source_info_ssl_image);
            mCurrentTime = (TextView) findViewById(R.id.source_info_tvtime);
            mSource_info_tvnumber = (TextView) findViewById(R.id.source_info_tvnumber);
            mSource_info_tvnumber_value = (TextView) findViewById(R.id.source_info_tvnumber_value);
            mSource_info_tvname = (TextView) findViewById(R.id.source_info_tvname);
            mSource_info_teletext = (TextView) findViewById(R.id.source_info_teletext);
            mSource_info_program_name = (TextView) findViewById(R.id.source_info_program_name);
            mSource_info_Subtitle = (TextView) findViewById(R.id.source_info_Subtitle);
            mSource_info_mheg5 = (TextView) findViewById(R.id.source_info_mheg5);
            mSource_info_video_format = (TextView) findViewById(R.id.source_info_video_format);
            mSource_info_audio_format = (TextView) findViewById(R.id.source_info_audio_format);
            mSource_info_program_type = (TextView) findViewById(R.id.source_info_program_type);
            mSource_info_program_period = (TextView) findViewById(R.id.source_info_program_period);
            mSource_info_description = (TextView) findViewById(R.id.source_info_description);
            mSource_info_resolution = (TextView) findViewById(R.id.source_info_resolution);
            mSource_info_language = (TextView) findViewById(R.id.source_info_language);
            mSource_info_age = (TextView) findViewById(R.id.source_info_age);
            mSource_info_cc = (TextView) findViewById(R.id.source_info_cc);
            mSource_epg_event_name = (TextView) findViewById(R.id.source_info_epg_name);
            mSource_epg_event_rating = (TextView) findViewById(R.id.source_info_epg_rating_dtv);
            mSource_info_program_flag = (ImageView) findViewById(R.id.source_info_flag);
            
            mSource_lock =(TextView) findViewById(R.id.source_info_lock);
            mSource_lock_img =(ImageView) findViewById(R.id.source_info_lock_img);
            
            if (TvCommonManager.TV_SYSTEM_ATSC != mTvSystem) {
                mSource_epg_event_name.setVisibility(View.GONE);
                mSource_epg_event_rating.setVisibility(View.GONE);
            } else {
                mSource_epg_event_rating.setVisibility(View.VISIBLE);
            }
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                mChannelLogo = (ImageView) findViewById(R.id.source_info_image_channel_logo);
            }
            mTitle.setText(getResources().getString(R.string.source_info_DTV));
            mStr_video_info = getResources().getString(R.string.str_tv_unknown);
        } else {
            setContentView(R.layout.source_info);
            mInfo = (TextView) findViewById(R.id.source_info_textview);
            mTvImageView = (ImageView) findViewById(R.id.source_info_imageView);
            mTitle = (TextView) findViewById(R.id.source_info_title);
        }
    }

    private void clearTvPartialSourceInfo() {
        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            mSource_info_imageformat.setText("");
            mSource_info_soundformat.setText("");
            mSource_info_audioformat.setText("");
            if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                mSource_epg_event_rating.setText("");
            }
        } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            mSource_info_teletext.setText("");
            mSource_info_program_name.setText("");
            mSource_info_Subtitle.setText("");
            mSource_info_mheg5.setText("");
            mSource_info_video_format.setText("");
            mSource_info_audio_format.setText("");
            mSource_info_program_type.setText("");
            mSource_info_program_period.setText("");
            mSource_info_description.setText("");
            mSource_info_resolution.setText("");
            mSource_info_language.setText("");
            mSource_info_age.setText("");
            mSource_info_cc.setText("");
            mSource_epg_event_name.setText("");
            mSource_epg_event_rating.setText("");
            if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
                mSource_epg_event_rating.setText("");
            }
        }
    }

    private String getCurProgrameName() {
        int pgNum = mTvChannelManager.getCurrentChannelNumber();
        int st = TvChannelManager.SERVICE_TYPE_DTV;
        if (Utility.isSupportATV()) {
            st = TvChannelManager.SERVICE_TYPE_ATV;
        }
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                st = TvChannelManager.SERVICE_TYPE_DTV;
            }
        } else {
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                st = TvChannelManager.SERVICE_TYPE_DTV;
            }
        }
        String pgName = mTvChannelManager.getProgramName(pgNum, st, 0x00);
        return pgName;
    }

    private String getCurProgrameName(short num) {
        int pgNum = mTvChannelManager.getCurrentChannelNumber();
        int st = -1;
        switch ((int) num) {
            case 0:
                st = TvChannelManager.SERVICE_TYPE_ATV;
                break;
            case 3:
                st = TvChannelManager.SERVICE_TYPE_DATA;
                break;
            case 1:
                st = TvChannelManager.SERVICE_TYPE_DTV;
                break;
            case 5:
                st = TvChannelManager.SERVICE_TYPE_INVALID;
                break;
            case 2:
                st = TvChannelManager.SERVICE_TYPE_RADIO;
                break;
            case 4:
                st = TvChannelManager.SERVICE_TYPE_UNITED_TV;
                break;
        }
        String pgName = mTvChannelManager.getProgramName(pgNum, st, 0x00);
        return pgName;
    }

    private void updateChannelInfo() {
        int videostandard = TvChannelManager.AVD_VIDEO_STANDARD_PAL_BGHI;
        checkInputSourceAndInitView();
        clearTvPartialSourceInfo();
        mCurChannelNumber = mTvChannelManager.getCurrentChannelNumber();

        if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            ProgramInfo CurrProg_Info = new ProgramInfo();
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                CurrProg_Info = mTvAtscChannelManager.getCurrentProgramInfo();
                majorMinorToIcon(CurrProg_Info.majorNum, CurrProg_Info.minorNum);
                String channum = mTvAtscChannelManager.getDispChannelNum(CurrProg_Info);
                String name = mTvAtscChannelManager.getDispChannelName(CurrProg_Info);
                mSource_info_tvname.setText(getString(R.string.str_textview_record_chaneel_name)
                        + " : " + name);
                mSource_info_tvnumber.setText(getString(R.string.str_cha_dtvmanualtuning_channelno)
                        + " : ");
                mSource_info_tvnumber_value.setText(channum);

                AtscEpgEventInfo pfEvtInfo = null;

                mCurChannelNumber = CurrProg_Info.number;
                mSource_info_Subtitle.setVisibility(View.GONE);
                mSource_info_teletext.setVisibility(View.GONE);
                mSource_info_mheg5.setVisibility(View.GONE);
                mSource_info_video_format.setVisibility(View.GONE);

                updateAudioInfo(false);

                updateServiceType(CurrProg_Info);

                mSource_info_resolution.setText(getString(R.string.str_dtv_source_info_resolution)
                        + " : " + mStr_video_info);

                Time currTime = mTvTimerManager.getCurrentTvTime();
                pfEvtInfo = mTvEpgManager.getAtscEventInfoByTime(CurrProg_Info.majorNum,
                        CurrProg_Info.minorNum, CurrProg_Info.number, CurrProg_Info.progId,
                        currTime);
                AtscEpgEventInfo epgEvInfoExted = mTvEpgManager.getEventExtendInfoByTime(
                        CurrProg_Info.majorNum, CurrProg_Info.minorNum,
                        (int) CurrProg_Info.serviceType, CurrProg_Info.progId, currTime);

                if (pfEvtInfo != null) {
                    mSource_epg_event_name.setText(getString(R.string.str_epg_event_name) + " : "
                            + pfEvtInfo.sName);
                }
                mSource_epg_event_rating.setText(getString(R.string.str_epg_event_rating) + " : "
                        + mTvAtscChannelManager.getCurrentRatingInformation());
                mSource_info_program_name.setVisibility(View.GONE);
                mSource_info_age.setVisibility(View.GONE);
                mSource_info_cc.setVisibility(View.GONE);
                if (epgEvInfoExted == null) {
                    mSource_info_description.setText("");
                } else {
                    mSource_info_description.setText(epgEvInfoExted.sExtendedText);
                }
            } else {
                ProgramInfoQueryCriteria cr = new ProgramInfoQueryCriteria();
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                    CurrProg_Info = mTvIsdbChannelManager.getCurrentProgramInfo();
                } else {
                    CurrProg_Info = TvDvbChannelManager.getInstance().getProgramInfo(cr,
                            TvDvbChannelManager.PROGRAM_INFO_TYPE_CURRENT);
                }
                DTVSpecificProgInfo dvtSpecProgInfo = null;

                if (CurrProg_Info != null) {
                    mCurChannelNumber = CurrProg_Info.number;
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                        mSource_info_tvname
                                .setText(getString(R.string.str_textview_record_chaneel_name)
                                        + " : " + CurrProg_Info.serviceName);
                        mSource_info_tvnumber
                                .setText(getString(R.string.str_cha_dtvmanualtuning_channelno)
                                        + " : ");
                        mSource_info_tvnumber_value.setText(String.valueOf(CurrProg_Info.majorNum)
                                + "." + String.valueOf(CurrProg_Info.minorNum));
                        majorMinorToIcon(CurrProg_Info.majorNum, CurrProg_Info.minorNum);

                        Bitmap channelBitmap = mTvChannelManager.getChannelLogoARGB();
                        if (null != channelBitmap) {
                            mChannelLogo.setImageBitmap(channelBitmap);
                            mChannelLogo.setVisibility(View.VISIBLE);
                        } else {
                            mChannelLogo.setVisibility(View.GONE);
                        }
                    } else {
                    	if(CurrProg_Info.favorite == 1 || CurrProg_Info.isSkip){
                    		if(CurrProg_Info.favorite == 1 && !CurrProg_Info.isSkip){
                    			mSource_info_program_flag.setImageResource(R.drawable.list_menu_img_favorite_focus);
                        		mSource_info_program_flag.setVisibility(View.VISIBLE);
                    		}else if(CurrProg_Info.favorite != 1 && CurrProg_Info.isSkip){
                    			mSource_info_program_flag.setImageResource(R.drawable.list_menu_img_skip_focus);
                        		mSource_info_program_flag.setVisibility(View.VISIBLE);
                    		}else if(CurrProg_Info.favorite == 1 && CurrProg_Info.isSkip){
                    			mSource_info_program_flag.setImageResource(R.drawable.list_menu_img_favorite_focusaskip);
                        		mSource_info_program_flag.setVisibility(View.VISIBLE);
                    		}
                    	}else
                    		mSource_info_program_flag.setVisibility(View.INVISIBLE);
						//modify by hz 20160726 to hide lock_img when close SystemLock
                    	if (CurrProg_Info.isLock&&TvParentalControlManager.getInstance().isSystemLock()) {
							mSource_lock_img.setVisibility(View.VISIBLE);
						}else {
							mSource_lock_img.setVisibility(View.GONE);
						}
                    		
                        mSource_info_tvname
                                .setText(getString(R.string.str_textview_record_chaneel_name)
                                        + " : " + getCurProgrameName((CurrProg_Info.serviceType)));
                        mSource_info_tvnumber
                                .setText(getString(R.string.str_cha_dtvmanualtuning_channelno)
                                        + " : ");
                        mSource_info_tvnumber_value.setText(String.valueOf(mCurChannelNumber));
                        numberToIcon(mCurChannelNumber);
                        if (CurrProg_Info.isScramble) {
                            mSslImage.setVisibility(View.VISIBLE);
                        } else {
                            mSslImage.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Log.v(TAG, "CurrProg_Info is NULL!!");
                    return;
                }

                try {
                    dvtSpecProgInfo = mTvChannelManager.getCurrentProgramSpecificInfo();
                    if (null == dvtSpecProgInfo) {
                        Log.v(TAG, "dvtSpecProgInfo is NULL!!");
                        return;
                    }
                    mDtvEitInfo = mTvEpgManager.getEitInfo(mIsPresentEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null != mDtvEitInfo) {
                    mSource_info_program_name.setText(mDtvEitInfo.eitCurrentEventPf.eventName);
                    mSource_info_age.setText(getString(R.string.str_dtv_source_info_age)
                            + ":"
                            + Utility.getParentalGuideAgeString(
                                    mDtvEitInfo.eitCurrentEventPf.parentalControl, mCountry));
                    if(!mDtvEitInfo.eitCurrentEventPf.eventName.isEmpty()){
	                    if(mIsPresentEvent)
	                    	mSource_info_program_imageshow.setImageResource(R.drawable.tune_right);
	                    else
	                    	mSource_info_program_imageshow.setImageResource(R.drawable.tune_left);
	                    mSource_info_program_imageshow.setVisibility(View.VISIBLE);
                    }else
                    	mSource_info_program_imageshow.setVisibility(View.GONE);
                    mSource_info_program_name.setVisibility(View.VISIBLE);
                } else {
                	mSource_info_program_imageshow.setVisibility(View.GONE);
                    mSource_info_program_name.setVisibility(View.GONE);
                }

                mSource_info_Subtitle.setText(getString(R.string.str_dtv_source_info_Subtitle)
                        + " : " + dvtSpecProgInfo.subtitleNum);

                if (true == dvtSpecProgInfo.ttxService) {
                    mSource_info_teletext.setText(getString(R.string.str_dtv_source_info_teletext));
                } else {
                    mStr = "";
                    mSource_info_teletext.setText(mStr);
                }
                if (true == dvtSpecProgInfo.mheg5Service) {
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                        mStr = getString(R.string.str_dtv_source_info_ginga);
                    } else {
                        mStr = getString(R.string.str_dtv_source_info_mheg5);
                    }
                } else {
                    mStr = "";
                }
                mSource_info_mheg5.setText(mStr);

                updateVideoType(dvtSpecProgInfo.videoType);
                updateAudioInfo(dvtSpecProgInfo.isAd);
                updateServiceType(CurrProg_Info);
                if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mTvChannelManager
                        .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT))
                        && (TvCountry.NEWZEALAND == mCountry)) {
                    if (null != mDtvEitInfo) {
                        String fmtStr = "";
                        final String TIME_FORMAT_NZDT = " NZDT";
                        final String TIME_FORMAT_NZST = " NZST";

                        if ((mDtvEitInfo.eitCurrentEventPf.stStartTime.year == mDtvEitInfo.eitCurrentEventPf.stEndTime.year)
                                && (mDtvEitInfo.eitCurrentEventPf.stStartTime.month == mDtvEitInfo.eitCurrentEventPf.stEndTime.month)
                                && (mDtvEitInfo.eitCurrentEventPf.stStartTime.monthDay == mDtvEitInfo.eitCurrentEventPf.stEndTime.monthDay)) {
                            /* test case: nz_si03_221, nz_si03_043 */
                            fmtStr = "HH:mm:ss";
                        } else {
                            /* test case: nz_si03_221, nz_si03_043 */
                            fmtStr = "EEE, d MMM yyyy HH:mm:ss";
                        }

                        String startTimeDispStr = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")
                                .format(new Date(mDtvEitInfo.eitCurrentEventPf.stStartTime
                                        .toMillis(false)));
                        String endTimeDispStr = new SimpleDateFormat(fmtStr).format(new Date(
                                mDtvEitInfo.eitCurrentEventPf.stEndTime.toMillis(false)));

                        if (mDtvEitInfo.eitCurrentEventPf.isStartTimeDayLightTime) {
                            startTimeDispStr += TIME_FORMAT_NZDT;
                        } else {
                            startTimeDispStr += TIME_FORMAT_NZST;
                        }
                        if (mDtvEitInfo.eitCurrentEventPf.isEndTimeDayLightTime) {
                            endTimeDispStr += TIME_FORMAT_NZDT;
                        } else {
                            endTimeDispStr += TIME_FORMAT_NZST;
                        }

                        mSource_info_program_period
                                .setText(getString(R.string.str_dtv_source_info_program_period)
                                        + " : "
                                        + startTimeDispStr
                                        + " - "
                                        + endTimeDispStr
                                        + " ("
                                        + getEpgEventDuration(mDtvEitInfo.eitCurrentEventPf.durationInSeconds)
                                        + ")");
                    } else {
                        mSource_info_program_period
                                .setText(getString(R.string.str_dtv_source_info_program_period)
                                        + " : ");
                    }
                } else {
                    if (null != mDtvEitInfo) {
                        mSource_info_program_period
                                .setText(getString(R.string.str_dtv_source_info_program_period)
                                        + " : "
                                        + (mDtvEitInfo.eitCurrentEventPf.durationInSeconds / 60)
                                        + "Min");
                    }
                }

                updateIsCcExist();

                if (null != mDtvEitInfo) {
                    mStr = "";
                    if (mDtvEitInfo.eitCurrentEventPf.shortEventText.length() > 0) {
                        mStr += mDtvEitInfo.eitCurrentEventPf.shortEventText;
                    }
                    if (mDtvEitInfo.eitCurrentEventPf.extendedEventText.length() > 0) {
                        mStr += mDtvEitInfo.eitCurrentEventPf.extendedEventText;
                    }
                    if (mDtvEitInfo.eitCurrentEventPf.extendedEventItem.length() > 0) {
                        mStr += mDtvEitInfo.eitCurrentEventPf.extendedEventItem;
                    }
                    String guidance = mTvEpgManager.getEventDescriptor(CurrProg_Info.serviceType,CurrProg_Info.number,
                        mDtvEitInfo.eitCurrentEventPf.stStartTime, TvEpgManager.EPG_GUIDANCE_DESCRIPTION);
                    if ((guidance != null) && (!guidance.isEmpty())) {
                        mStr += guidance;
                    }
                    mSource_info_description.setText(mStr);
                }

                if ((null != mVideoInfo) && (0 != mVideoInfo.vResolution)) {
                    mSource_info_resolution
                            .setText(getString(R.string.str_dtv_source_info_resolution) + " : "
                                    + mStr_video_info);
                } else {
                    mSource_info_resolution
                            .setText(getString(R.string.str_dtv_source_info_resolution) + " :"
                                    + getResources().getString(R.string.str_tv_unknown));
                }
            }
        } else if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            ProgramInfo pinfo = mTvChannelManager.getCurrentProgramInfo();
            int dispCh = mCurChannelNumber;
            if (pinfo == null) {
                return;
            }
            mCurChannelNumber = pinfo.number;
            if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                /* There is no need to display channel name if input source is ATV. */
                mSource_info_tvname.setText("");
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                String channum = mTvAtscChannelManager.getDispChannelNum(pinfo);
                String name = mTvAtscChannelManager.getDispChannelName(pinfo);
                mSource_info_tvname.setText(getString(R.string.str_textview_record_chaneel_name)
                        + " : " + name);
                mSource_epg_event_rating.setText(getString(R.string.str_epg_event_rating) + " : "
                        + mTvAtscChannelManager.getCurrentRatingInformation());
            } else {
                mSource_info_tvname.setText(getString(R.string.str_textview_record_chaneel_name)
                        + " : " + getCurProgrameName());
            }

        	if(pinfo.favorite == 1 || pinfo.isSkip){
        		if(pinfo.favorite == 1 && !pinfo.isSkip){
        			mSource_info_program_flag.setImageResource(R.drawable.list_menu_img_favorite_focus);
            		mSource_info_program_flag.setVisibility(View.VISIBLE);
        		}else if(pinfo.favorite != 1 && pinfo.isSkip){
        			mSource_info_program_flag.setImageResource(R.drawable.list_menu_img_skip_focus);
            		mSource_info_program_flag.setVisibility(View.VISIBLE);
        		}else if(pinfo.favorite == 1 && pinfo.isSkip){
        			mSource_info_program_flag.setImageResource(R.drawable.list_menu_img_favorite_focusaskip);
            		mSource_info_program_flag.setVisibility(View.VISIBLE);
        		}
        	}else
        		mSource_info_program_flag.setVisibility(View.INVISIBLE);
			//modify by hz 20160726 to hide lock_img when close SystemLock
        	if (pinfo.isLock&&TvParentalControlManager.getInstance().isSystemLock()) {
				mSource_lock_img.setVisibility(View.VISIBLE);
			}else {
				mSource_lock_img.setVisibility(View.GONE);
			}	
            
            dispCh = Utility.getATVDisplayChNum(mCurChannelNumber);
            mSource_info_tvnumber.setText(getString(R.string.str_cha_dtvmanualtuning_channelno)
                    + " : ");
            mSource_info_tvnumber_value.setText(String.valueOf(dispCh));
            numberToIcon(dispCh);

            videostandard = mTvChannelManager.getAvdVideoStandard();

            // get video standard
            switch (videostandard) {
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_BGHI:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_M:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_N:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_60:
                    mStr = "PAL";
                    break;
                case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_44:
                case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_M:
                    mStr = "NTSC";
                    break;
                case TvChannelManager.AVD_VIDEO_STANDARD_SECAM:
                    mStr = "SECAM";
                    break;
                default:
                    mStr = "";
            }
            mSource_info_imageformat.setText(getString(R.string.str_atv_source_imageformat) + " : "
                    + mStr);
            mStr = getATVSoundFormat();
            mSource_info_soundformat.setText(getString(R.string.str_atv_source_soundformat) + " : "
                    + mStr);
            switch (mTvChannelManager.getAtvAudioStandard()) {
                case TvChannelManager.ATV_AUDIO_STANDARD_BG:
                    mStr = "BG";
                    break;
                case TvChannelManager.ATV_AUDIO_STANDARD_DK:
                    mStr = "DK";
                    break;
                case TvChannelManager.ATV_AUDIO_STANDARD_I:
                    mStr = " I";
                    break;
                case TvChannelManager.ATV_AUDIO_STANDARD_L:
                    mStr = " L";
                    break;
                case TvChannelManager.ATV_AUDIO_STANDARD_M:
                    //mStr = " MN ";
                    if(TvChannelManager.AVD_VIDEO_STANDARD_PAL_BGHI==this.mTvChannelManager.getAvdVideoStandard())//hexing2017-0113 add for atv pal BG show M
					mStr = "BG";
					else if(TvChannelManager.AVD_VIDEO_STANDARD_PAL_N==this.mTvChannelManager.getAvdVideoStandard())//hexing2016-1130 add for PAL N show N
					mStr = " N ";
					else
                    mStr = " M ";
                    break;
                default:
                    mStr = "BG";
            }
            mSource_info_audioformat.setText(getString(R.string.str_atv_source_audioformat) + " : "
                    + mStr);

        }
    }

    private void updateAudioInfo(boolean isAD) {
        DtvAudioInfo audioInfo = new DtvAudioInfo();
        audioInfo = mTvChannelManager.getAudioInfo();
        if (audioInfo.audioInfos.length > 0) {
            if (audioInfo.audioInfos[audioInfo.currentAudioIndex] != null) {
                mStr = Utility.getAudioTypeString(audioInfo.audioInfos[audioInfo.currentAudioIndex].audioType, audioInfo.audioInfos[audioInfo.currentAudioIndex].aacProfileAndLevel);

                if (isAD) {
                    mStr += "  AD";
                }

                mSource_info_audio_format
                        .setText(getString(R.string.str_dtv_source_info_audio_format) + " : "
                                + mStr);
            }
            if (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC) {
                mSource_info_language.setText(getString(R.string.str_dtv_source_info_language)
                        + " : " + audioInfo.audioLangNum);
            }
        }
    }

    private void updateIsCcExist() {
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            if (mTvIsdbChannelManager.doesCcExist()) {
                mSource_info_cc.setText(getString(R.string.str_dtv_source_info_cc_exist));
            } else {
                mSource_info_cc.setText(getString(R.string.str_dtv_source_info_cc_noexist));
            }
        }
    }

    private void updateServiceType(ProgramInfo pi) {
        mStr = "";
        if (pi.serviceType < mServiceTypeDisplayString.length) {
            mStr = mServiceTypeDisplayString[pi.serviceType];
        }

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mSource_info_program_type
                    .getLayoutParams();
            mlp.setMargins(0, 0, 0, 0);
        }
        mSource_info_program_type.setText(getString(R.string.str_dtv_source_info_program_type)
                + " : " + mStr);
    }

    private void updateVideoType(int type) {
        if ((0 <= type) && (mVideoTypeDisplayString.length > type)) {
            mSource_info_video_format
                    .setText(getString(R.string.str_dtv_source_info_program_format) + " : "
                            + mVideoTypeDisplayString[type]);
        } else {
            Log.e(TAG, "updateVideoType(" + type + ") Out of Bounds array size = "
                    + mVideoTypeDisplayString.length);
        }
    }

    private String getATVSoundFormat() {
        String str = "";
        switch (TvCommonManager.getInstance().getATVMtsMode()) {
            case TvCommonManager.ATV_AUDIOMODE_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_A:
                str = getResources().getString(R.string.str_sound_format_dual_a);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_AB:
                str = getResources().getString(R.string.str_sound_format_dual_ab);
                break;
            case TvCommonManager.ATV_AUDIOMODE_DUAL_B:
                str = getResources().getString(R.string.str_sound_format_dual_b);
                break;
            case TvCommonManager.ATV_AUDIOMODE_FORCED_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_G_STEREO:
                str = getResources().getString(R.string.str_sound_format_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_HIDEV_MONO:
                str = getResources().getString(R.string.str_sound_format_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_K_STEREO:
                str = getResources().getString(R.string.str_sound_format_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_LEFT_LEFT:
                str = getResources().getString(R.string.str_sound_format_left_left);
                break;
            case TvCommonManager.ATV_AUDIOMODE_LEFT_RIGHT:
                str = getResources().getString(R.string.str_sound_format_left_right);
                break;
            case TvCommonManager.ATV_AUDIOMODE_MONO_SAP:
                str = getResources().getString(R.string.str_sound_format_mono_sap);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_A:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_a);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_AB:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_ab);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_DUAL_B:
                str = getResources().getString(R.string.str_sound_format_nicam_dual_b);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_MONO:
                str = getResources().getString(R.string.str_sound_format_nicam_mono);
                break;
            case TvCommonManager.ATV_AUDIOMODE_NICAM_STEREO:
                str = getResources().getString(R.string.str_sound_format_nicam_stereo);
                break;
            case TvCommonManager.ATV_AUDIOMODE_RIGHT_RIGHT:
                str = getResources().getString(R.string.str_sound_format_right_right);
                break;
            case TvCommonManager.ATV_AUDIOMODE_STEREO_SAP:
                str = getResources().getString(R.string.str_sound_format_stereo_sap);
                break;
            case TvCommonManager.ATV_AUDIOMODE_INVALID:
            default:
                str = getResources().getString(R.string.str_sound_format_unknown);
                break;
        }
        return str;
    }

    /**
     * Select source icon according to the input source
     */
    private void selectIconByInputSource() {
        selectVideoInfo();
        switch (mInputSource) {
            case TvCommonManager.INPUT_SOURCE_VGA:
                setSourceInfo(R.drawable.grid_menu_pc, getResources().getString(R.string.source_info_VGA));
                break;
            case TvCommonManager.INPUT_SOURCE_VGA2:
                setSourceInfo(R.drawable.grid_menu_pc, "VGA2");
                break;
            case TvCommonManager.INPUT_SOURCE_VGA3:
                setSourceInfo(R.drawable.grid_menu_pc, "VGA3");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS:
            	if(mCvbsNum > 1) {
            		setSourceInfo(R.drawable.grid_menu_avi, getResources().getString(R.string.source_info_AV1));
				} else {
					setSourceInfo(R.drawable.grid_menu_avi, getResources().getString(R.string.source_info_AV));
				}
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS2:
                setSourceInfo(R.drawable.grid_menu_avi, getResources().getString(R.string.source_info_AV2));
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS3:
                setSourceInfo(R.drawable.grid_menu_avi, "CVBS3");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS4:
                setSourceInfo(R.drawable.grid_menu_avi, "CVBS4");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS5:
                setSourceInfo(R.drawable.grid_menu_avi, "CVBS5");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS6:
                setSourceInfo(R.drawable.grid_menu_avi, "CVBS6");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS7:
                setSourceInfo(R.drawable.grid_menu_avi, "CVBS7");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS8:
                setSourceInfo(R.drawable.grid_menu_avi, "CVBS8");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO:
                setSourceInfo(R.drawable.grid_menu_sv, "SVIDEO");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO2:
                setSourceInfo(R.drawable.grid_menu_sv, "SVIDEO2");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO3:
                setSourceInfo(R.drawable.grid_menu_sv, "SVIDEO3");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO4:
                setSourceInfo(R.drawable.grid_menu_sv, "SVIDEO4");
                break;
            case TvCommonManager.INPUT_SOURCE_YPBPR:
            	if(mYpbprNum > 1) {
            		setSourceInfo(R.drawable.grid_menu_yuv, getResources().getString(R.string.str_input_source_ypbpr1));
				} else {
					setSourceInfo(R.drawable.grid_menu_yuv, getResources().getString(R.string.str_input_source_ypbpr));
				}
                break;
            case TvCommonManager.INPUT_SOURCE_YPBPR2:
                setSourceInfo(R.drawable.grid_menu_yuv, "YPBPR2");
                break;
            case TvCommonManager.INPUT_SOURCE_YPBPR3:
                setSourceInfo(R.drawable.grid_menu_yuv, "YPBPR3");
                break;
            case TvCommonManager.INPUT_SOURCE_SCART:
                setSourceInfo(R.drawable.grid_menu_scart, "SCART");
                break;
            case TvCommonManager.INPUT_SOURCE_SCART2:
                setSourceInfo(R.drawable.grid_menu_scart, "SCART2");
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI:
				if(mHdmiNum == 1)
				{
					if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
						setSourceInfo(R.drawable.grid_menu_hdmi, "HDMI");
					} else {
						setSourceInfo(R.drawable.grid_menu_pc, "DVI");
					}
				}
				else
				{
					if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
						setSourceInfo(R.drawable.grid_menu_hdmi, getResources().getString(R.string.source_info_HDMI1));
					} else {
						setSourceInfo(R.drawable.grid_menu_pc, "DVI1");
					}
				}
                
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI2:
                if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                    setSourceInfo(R.drawable.grid_menu_hdmi, getResources().getString(R.string.source_info_HDMI2));
                } else {
                    setSourceInfo(R.drawable.grid_menu_pc, "DVI2");
                }
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                    setSourceInfo(R.drawable.grid_menu_hdmi, "HDMI3");
                } else {
                    setSourceInfo(R.drawable.grid_menu_pc, "DVI3");
                }
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI4:
                if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                    setSourceInfo(R.drawable.grid_menu_hdmi, "HDMI4");
                } else {
                    setSourceInfo(R.drawable.grid_menu_pc, "DVI4");
                }
                break;
            case TvCommonManager.INPUT_SOURCE_ATV:
            case TvCommonManager.INPUT_SOURCE_DTV:
                updateChannelInfo();
                break;
            case TvCommonManager.INPUT_SOURCE_DVI:
                setSourceInfo(R.drawable.grid_menu_pc, "DVI");
                break;
            case TvCommonManager.INPUT_SOURCE_DVI2:
                setSourceInfo(R.drawable.grid_menu_pc, "DVI2");
                break;
            case TvCommonManager.INPUT_SOURCE_DVI3:
                setSourceInfo(R.drawable.grid_menu_pc, "DVI3");
                break;
            case TvCommonManager.INPUT_SOURCE_DVI4:
                setSourceInfo(R.drawable.grid_menu_pc, "DVI4");
                break;
            default:
                break;
        }
    }

    private void setSourceInfo(int resId, String strtitle) {
        mTvImageView.setImageResource(resId);
        mTitle.setText(strtitle);
        int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if(TvCommonManager.getInstance().isSignalStable(curInputSource))
        	mInfo.setText(mStr_video_info);
    }

    private void selectVideoInfo() {
        if (mVideoInfo.vResolution != 0) {
			// special, for only 72hz
			int s16FrameRateShow;
			if (mVideoInfo.frameRate > 711 && mVideoInfo.frameRate < 730)
				s16FrameRateShow = 72;
			else
            	s16FrameRateShow = (mVideoInfo.frameRate + 5) / 10;
            int scanType = VideoInfo.VIDEO_SCAN_TYPE_PROGRESSIVE;
            try {
                scanType = mVideoInfo.getVideoScanType();
            } catch (TvCommonException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            switch (mInputSource) {
                case TvCommonManager.INPUT_SOURCE_ATV:
                case TvCommonManager.INPUT_SOURCE_DTV:
                    if (scanType == VideoInfo.VIDEO_SCAN_TYPE_PROGRESSIVE) {
                        mStr_video_info = mVideoInfo.vResolution + "P";
                    } else {
                        mStr_video_info = mVideoInfo.vResolution + "I";
                    }
                    break;
                case TvCommonManager.INPUT_SOURCE_VGA:
                case TvCommonManager.INPUT_SOURCE_VGA2:
                case TvCommonManager.INPUT_SOURCE_VGA3:
                case TvCommonManager.INPUT_SOURCE_DVI:
                case TvCommonManager.INPUT_SOURCE_DVI2:
                case TvCommonManager.INPUT_SOURCE_DVI3:
                case TvCommonManager.INPUT_SOURCE_DVI4:
                    mStr_video_info = mVideoInfo.hResolution + "X" + mVideoInfo.vResolution + "@"
                            + s16FrameRateShow + "Hz";
                    break;
                case TvCommonManager.INPUT_SOURCE_HDMI:
                case TvCommonManager.INPUT_SOURCE_HDMI2:
                case TvCommonManager.INPUT_SOURCE_HDMI3:
                case TvCommonManager.INPUT_SOURCE_HDMI4:
                    if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                        if (scanType == VideoInfo.VIDEO_SCAN_TYPE_PROGRESSIVE) {
                            mStr_video_info = mVideoInfo.vResolution + "P";
                        } else {
                            mStr_video_info = mVideoInfo.vResolution + "I";
                        }
                        mStr_video_info += "@" + s16FrameRateShow + "Hz";
                    } else {
                        mStr_video_info = mVideoInfo.hResolution + "X" + mVideoInfo.vResolution
                                + "@" + s16FrameRateShow + "Hz";
                    }
                    break;
                case TvCommonManager.INPUT_SOURCE_YPBPR:
                case TvCommonManager.INPUT_SOURCE_YPBPR2:
                case TvCommonManager.INPUT_SOURCE_YPBPR3:
                    if (scanType == VideoInfo.VIDEO_SCAN_TYPE_PROGRESSIVE) {
                        mStr_video_info = mVideoInfo.vResolution + "P";
                    } else {
                        mStr_video_info = mVideoInfo.vResolution + "I";
                    }
                    mStr_video_info += "@" + s16FrameRateShow + "Hz";
                    break;
                case TvCommonManager.INPUT_SOURCE_CVBS:
                case TvCommonManager.INPUT_SOURCE_CVBS2:
                case TvCommonManager.INPUT_SOURCE_CVBS3:
                case TvCommonManager.INPUT_SOURCE_CVBS4:
                	int colour_system = -1;
                	try {
        				colour_system=TvManager.getInstance().getPlayerManager().getVideoStandard().ordinal();
        				//Log.i("wxy","----colour_system="+colour_system+"------");
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				Log.i("SourceInfoActivity","Get colour system fail");
        				e.printStackTrace();
        			}
                	if(colour_system <= 6){
                		if (scanType == VideoInfo.VIDEO_SCAN_TYPE_PROGRESSIVE) {
                            mStr_video_info = /*mVideoInfo.vResolution + "P/"+*/getResources().getStringArray(R.array.arr_cvbs_type)[colour_system];
                        } else {
                            mStr_video_info = /*mVideoInfo.vResolution + "I/"+*/getResources().getStringArray(R.array.arr_cvbs_type)[colour_system];
                        }
                	}else{
                		if(TvCommonManager.getInstance().isSignalStable(mInputSource))
                    		mInfo.setText("PAL");
                    	else
                    		mInfo.setText(" ");	
                	}
                	break;
                default:
                    if (scanType == VideoInfo.VIDEO_SCAN_TYPE_PROGRESSIVE) {
                        mStr_video_info = mVideoInfo.vResolution + "P";
                    } else {
                        mStr_video_info = mVideoInfo.vResolution + "I";
                    }
                    break;
            }
        }

        if (mStr_video_info == "X") {
            mStr_video_info = "";
        }
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if (mStr_video_info == null) {
                mStr_video_info = "";
            }
        }
    }

    public ArrayList<String> numberToPicture(int num) {
        ArrayList<String> strArray = new ArrayList<String>();
        String mStr = num + "";
        for (int i = 0; i < mStr.length(); i++) {
            char ch = mStr.charAt(i);
            strArray.add("" + ch);
        }
        return strArray;
    }

    public ArrayList<Integer> getResoulseID(ArrayList<String> mStr) {
        ArrayList<Integer> n = new ArrayList<Integer>();
        for (String string : mStr) {
            Integer resId = mNumberResIds[Integer.parseInt(string)];
            n.add(resId);
        }
        return n;
    }

    public void numberToIcon(int number) {
        ArrayList<Integer> n = getResoulseID(numberToPicture(number));
        if (number <= MAX_VALUE_OF_ONE_DIGIT) {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setVisibility(View.GONE);
            mThirdTvNumberIcon.setVisibility(View.GONE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.GONE);
            }
        } else if ((MAX_VALUE_OF_ONE_DIGIT < number) && (number <= MAX_VALUE_OF_TWO_DIGIT)) {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setImageResource(n.get(1));
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.GONE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.GONE);
            }
        } else if ((MAX_VALUE_OF_TWO_DIGIT < number) && (number <= MAX_VALUE_OF_THREE_DIGIT)) {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setImageResource(n.get(1));
            mThirdTvNumberIcon.setImageResource(n.get(2));
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.VISIBLE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.GONE);
            }
        } else {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setImageResource(n.get(1));
            mThirdTvNumberIcon.setImageResource(n.get(2));
            mFourthTvNumberIcon.setImageResource(n.get(3));
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.VISIBLE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.VISIBLE);
            }
        }
        if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            mFifthTvNumberIcon.setVisibility(View.GONE);
            mTvDotIcon.setVisibility(View.GONE);
            mDigitMinorTvNumberIcon1.setVisibility(View.GONE);
            mDigitMinorTvNumberIcon2.setVisibility(View.GONE);
            mDigitMinorTvNumberIcon3.setVisibility(View.GONE);
            mDigitMinorTvNumberIcon4.setVisibility(View.GONE);
        }
    }

    public void majorMinorToIcon(int majorNum, int minorNum) {
        ArrayList<Integer> n = getResoulseID(numberToPicture(majorNum));
        if (majorNum <= MAX_VALUE_OF_ONE_DIGIT) {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setVisibility(View.GONE);
            mThirdTvNumberIcon.setVisibility(View.GONE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.GONE);
                mFifthTvNumberIcon.setVisibility(View.GONE);
            }
        } else if ((MAX_VALUE_OF_ONE_DIGIT < majorNum) && (majorNum <= MAX_VALUE_OF_TWO_DIGIT)) {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setImageResource(n.get(1));
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.GONE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.GONE);
                mFifthTvNumberIcon.setVisibility(View.GONE);
            }
        } else if ((MAX_VALUE_OF_TWO_DIGIT < majorNum) && (majorNum <= MAX_VALUE_OF_THREE_DIGIT)) {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setImageResource(n.get(1));
            mThirdTvNumberIcon.setImageResource(n.get(2));
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.VISIBLE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.GONE);
                mFifthTvNumberIcon.setVisibility(View.GONE);
            }
        } else if ((MAX_VALUE_OF_THREE_DIGIT < majorNum) && (majorNum <= MAX_VALUE_OF_FOUR_DIGIT)) {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setImageResource(n.get(1));
            mThirdTvNumberIcon.setImageResource(n.get(2));
            mFourthTvNumberIcon.setImageResource(n.get(3));
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.VISIBLE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.VISIBLE);
                mFifthTvNumberIcon.setVisibility(View.GONE);
            }
        } else {
            mFirstTvNumberIcon.setImageResource(n.get(0));
            mSecondTvNumberIcon.setImageResource(n.get(1));
            mThirdTvNumberIcon.setImageResource(n.get(2));
            mFourthTvNumberIcon.setImageResource(n.get(3));
            mFifthTvNumberIcon.setImageResource(n.get(4));
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.VISIBLE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                mFourthTvNumberIcon.setVisibility(View.VISIBLE);
                mFifthTvNumberIcon.setVisibility(View.VISIBLE);
            }
        }

        /*
         * For ONE-PART channel, the minor number is defined as 0xFFFF.
         * In a case: MajorNum = 15, MinorNum = 65535(0xFFFF),
         * channel number will be displayed as "15" in channellist menu.
         * And we accept that user inputs "15" to tune to the channel,
         * it needs to convert minor channel number to "0xFFFF" before doing channel switching.
         */
        n = getResoulseID(numberToPicture(minorNum));
        if ((mTvDotIcon != null)
                && (mDigitMinorTvNumberIcon1 != null) && (mDigitMinorTvNumberIcon2 != null)
                && (mDigitMinorTvNumberIcon3 != null) && (mDigitMinorTvNumberIcon4 != null)) {

            if (minorNum == TvAtscChannelManager.ONEPARTCHANNEL_MINOR_NUM) {
                mTvDotIcon.setVisibility(View.GONE);
                mDigitMinorTvNumberIcon1.setVisibility(View.GONE);
                mDigitMinorTvNumberIcon2.setVisibility(View.GONE);
                mDigitMinorTvNumberIcon3.setVisibility(View.GONE);
                mDigitMinorTvNumberIcon4.setVisibility(View.GONE);
            } else {
                if (minorNum <= MAX_VALUE_OF_ONE_DIGIT) {
                    mTvDotIcon.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon1.setImageResource(n.get(0));
                    mDigitMinorTvNumberIcon1.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon2.setVisibility(View.GONE);
                    mDigitMinorTvNumberIcon3.setVisibility(View.GONE);
                    mDigitMinorTvNumberIcon4.setVisibility(View.GONE);
                } else if ((MAX_VALUE_OF_ONE_DIGIT < minorNum) && (minorNum <= MAX_VALUE_OF_TWO_DIGIT)) {
                    mTvDotIcon.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon1.setImageResource(n.get(0));
                    mDigitMinorTvNumberIcon1.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon2.setImageResource(n.get(1));
                    mDigitMinorTvNumberIcon2.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon3.setVisibility(View.GONE);
                    mDigitMinorTvNumberIcon4.setVisibility(View.GONE);
                } else if ((MAX_VALUE_OF_TWO_DIGIT < minorNum)
                        && (minorNum <= MAX_VALUE_OF_THREE_DIGIT)) {
                    mTvDotIcon.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon1.setImageResource(n.get(0));
                    mDigitMinorTvNumberIcon1.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon2.setImageResource(n.get(1));
                    mDigitMinorTvNumberIcon2.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon3.setImageResource(n.get(2));
                    mDigitMinorTvNumberIcon3.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon4.setVisibility(View.GONE);
                } else {
                    mTvDotIcon.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon1.setImageResource(n.get(0));
                    mDigitMinorTvNumberIcon1.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon2.setImageResource(n.get(1));
                    mDigitMinorTvNumberIcon2.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon3.setImageResource(n.get(2));
                    mDigitMinorTvNumberIcon3.setVisibility(View.VISIBLE);
                    mDigitMinorTvNumberIcon4.setImageResource(n.get(3));
                    mDigitMinorTvNumberIcon4.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private TimerTask getTimerTask() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                        || (mInputSource == TvCommonManager.INPUT_SOURCE_DTV)) {
                    myHandler.sendEmptyMessage(CMD_SET_CURRENT_TIME);
                }
                mCount++;
                if (mCount < MAX_TIMES && mCount % 2 == 0// the period is 2s
                        && mInputSource == TvCommonManager.INPUT_SOURCE_YPBPR) {
                    myHandler.sendEmptyMessage(CMD_SIGNAL_LOCK);
                }
            }
        };
        return mTimerTask;
    }

    public void updateSourceInfo() {
        checkInputSourceAndInitView();
        clearTvPartialSourceInfo();
        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            mThirdTvNumberIcon.setVisibility(View.VISIBLE);
            mCurrentTime.setVisibility(View.VISIBLE);
            mSource_info_tvnumber.setVisibility(View.VISIBLE);
            mSource_info_tvnumber_value.setVisibility(View.VISIBLE);
            mSource_info_tvname.setVisibility(View.VISIBLE);
        }
        mVideoInfo = TvPictureManager.getInstance().getVideoInfo();
        selectIconByInputSource();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mCount = 0;
        CecSetting setting = mTvCecManager.getCecConfiguration();
        switch (mInputSource) {
            case TvCommonManager.INPUT_SOURCE_HDMI:
            case TvCommonManager.INPUT_SOURCE_HDMI2:
            case TvCommonManager.INPUT_SOURCE_HDMI3:
            case TvCommonManager.INPUT_SOURCE_HDMI4: {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    break;
                }
                if ((setting.cecStatus == mCecStatusOn)
                        && (TvCommonManager.getInstance().isHdmiSignalMode() == true)) {
                    if (mTvCecManager.sendCecKey(keyCode)) {
                        Log.d(TAG, "onKeyDown return TRUE");
                        return true;
                    }
                }
                break;
            }
            case TvCommonManager.INPUT_SOURCE_VGA:
            case TvCommonManager.INPUT_SOURCE_VGA2:
            case TvCommonManager.INPUT_SOURCE_VGA3:
            case TvCommonManager.INPUT_SOURCE_CVBS:
            case TvCommonManager.INPUT_SOURCE_CVBS2:
            case TvCommonManager.INPUT_SOURCE_CVBS3:
            case TvCommonManager.INPUT_SOURCE_CVBS4:
            case TvCommonManager.INPUT_SOURCE_YPBPR:
            case TvCommonManager.INPUT_SOURCE_YPBPR2:
            case TvCommonManager.INPUT_SOURCE_YPBPR3:
            case TvCommonManager.INPUT_SOURCE_DTV:
            case TvCommonManager.INPUT_SOURCE_ATV: {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
                        || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                        || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
                    if (setting.cecStatus == mCecStatusOn) {
                        if (mTvCecManager.sendCecKey(keyCode)) {
                            Log.d(TAG, "onKeyDown return TRUE");
                            return true;
                        }
                    }
                }
                break;
            }
            default:
                break;
        }
        if (KeyEvent.KEYCODE_I == keyCode || KeyEvent.KEYCODE_INFO == keyCode){
        	finish();
        	return true;
        }else if (SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
            finish();
            return true;
        } else if (SwitchPageHelper.goToEpgPage(this, keyCode) == true) {
            finish();
            return true;
        } else if (SwitchPageHelper.goToPvrPage(this, keyCode) == true) {
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
            if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (isDTVChannelNameReady == true) {
                        isDTVChannelNameReady = false;
                        mTvChannelManager.programDown();
                        return true;
                    }
                } else {
                	if(TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV)<=1)
						return true;
                    mTvChannelManager.programDown();
                    updateChannelInfo();
                }

            } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                if (isDTVChannelNameReady == true) {
                    isDTVChannelNameReady = false;
                    Utility.channelDown(this);
                    return true;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_CHANNEL_UP) {

            if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (isDTVChannelNameReady == true) {
                        isDTVChannelNameReady = false;
                        mTvChannelManager.programUp();
                        return true;
                    }
                } else {
                	if(TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV)<=1)
						return true;
                    mTvChannelManager.programUp();
                    updateChannelInfo();
                }
            } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                if (isDTVChannelNameReady == true) {
                    isDTVChannelNameReady = false;
                    Utility.channelUp(this);
                    return true;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else if (keyCode == MKeyEvent.KEYCODE_CHANNEL_RETURN) {
            if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                    || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            	if(TvCommonManager.getInstance().getCurrentTvInputSource()==TvCommonManager.INPUT_SOURCE_ATV
						&& TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV)<=1)
						return true;
					if(TvCommonManager.getInstance().getCurrentTvInputSource()==TvCommonManager.INPUT_SOURCE_DTV
						&& TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV)<=1)
						return true;
                Utility.channelReturn(this);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateChannelInfo();
            } else {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            mIsBackRoot = true;
            finish();
            return true;
        } else if (keyCode >= KeyEvent.KEYCODE_1 && keyCode <= KeyEvent.KEYCODE_9) {
        	//add by pan for resolve the conflict between code dialog and sourceinfo
        	ProgramInfo CurrProg_Info = new ProgramInfo();
        	ProgramInfoQueryCriteria cr = new ProgramInfoQueryCriteria();
        	if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                CurrProg_Info = mTvAtscChannelManager.getCurrentProgramInfo();
        	}else if(mTvSystem == TvCommonManager.TV_SYSTEM_ISDB){
        		CurrProg_Info = mTvIsdbChannelManager.getCurrentProgramInfo();
        	}else{
        		CurrProg_Info = TvDvbChannelManager.getInstance().getProgramInfo(cr,
                        TvDvbChannelManager.PROGRAM_INFO_TYPE_CURRENT);
        	}
        	if(CurrProg_Info.isLock){
        		return true;
        	}
        	//add end
            Intent intentChannelControl = new Intent(this, ChannelControlActivity.class);
            intentChannelControl.putExtra("KeyCode", keyCode);
            this.startActivity(intentChannelControl);
            finish();
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) || (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
            if ((TvCommonManager.TV_SYSTEM_ATSC != mTvSystem) && (mInputSource == TvCommonManager.INPUT_SOURCE_DTV)) {
                if (true == isDTVChannelNameReady) {
                    mIsPresentEvent = (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) ? true:false;
                    updateChannelInfo();
                    return true;
                }
            }
        } else if ((keyCode == KeyEvent.KEYCODE_PAGE_UP) || (keyCode == KeyEvent.KEYCODE_PAGE_DOWN)) {
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                Utility.scrollPosition(mSource_info_description,
                    (keyCode == KeyEvent.KEYCODE_PAGE_UP) ? Utility.SCROLL_DIRECTION_UP:Utility.SCROLL_DIRECTION_DOWN);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Invoked by MstarBaseActivity in which send the TIMEOUT_INTERVAL delay.
     */
    @Override
    public void onTimeOut() {
        super.onTimeOut();
        mIsBackRoot = true;
        mIntent = new Intent(this, RootActivity.class);
        startActivity(mIntent);
        finish();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TvIntent.ACTION_SIGNAL_LOCK)) {
                //myHandler.sendEmptyMessageDelayed(CMD_SIGNAL_LOCK, 1000);// this
                myHandler.sendEmptyMessage(CMD_SIGNAL_LOCK);
            } else if (intent.getAction().equals(TvIntent.ACTION_SOURCE_INFO_DISAPPEAR)) {
                finish();
            } else if (intent.getAction().equals(TvIntent.ACTION_TV_TIME_ZONE_CHANGE)) {
                String timezoneChangeString = intent.getStringExtra("timezoneChangeString");
                Utility.changeTvTimeZone(context, timezoneChangeString);
            }
        }
    };

    private String getEpgEventDuration(int totalSeconds) {
        if (totalSeconds < 0) {
            return "";
        }

        String timeStr = "";

        String HOUR = "hour";
        String HOURS = "hours";
        String MINUTE = "minute";
        String MINUTES = "minutes";
        String SECOND = "second";
        String SECONDS = "seconds";

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            timeStr += String.format("%d %s ", hours, (hours < 1) ? HOUR : HOURS);
        }

        if (minutes > 0) {
            timeStr += String.format("%d %s ", minutes, (minutes < 1) ? MINUTE : MINUTES);
        }

        if (seconds > 0) {
            timeStr += String.format("%d %s", seconds, (seconds < 1) ? SECOND : SECONDS);
        }

        if (0 < timeStr.length()) {
            if (timeStr.charAt(timeStr.length() - 1) == ' ') {
                timeStr = timeStr.substring(0, timeStr.length() - 1);
            }
        }
        return timeStr;
    }

    private void forcePasswordPrompt(boolean bForceReveal) {
        Intent intent = new Intent(TvIntent.ACTION_FORCE_REVEAL_PASSWORD_PROMPT);
        if (true == bForceReveal) {
            // Notify the the password prompt for parental block is forced revealed if exists.
            intent.putExtra(Constant.IS_FORCE_REVEAL_PASSWORD_PROMPT, true);
        } else {
            // Notify the the password prompt for parental block is not forced revealed anymore
            intent.putExtra(Constant.IS_FORCE_REVEAL_PASSWORD_PROMPT, false);
        }
        sendBroadcastAsUser(intent, new UserHandle(UserHandle.USER_CURRENT));
    }


private void createTime(String timeZone) {
		if (timeZone != null) {
			mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			} else {

				mTime = Calendar.getInstance();

			}

	}
	private void onTimeChanged() {
		mTime.setTimeInMillis(System.currentTimeMillis());
		date =new SimpleDateFormat("yyyy-MM-dd").format(mTime.getTime());
		boolean is24HourFormat = DateFormat.is24HourFormat(this);
		clock =new SimpleDateFormat(is24HourFormat ? "HH:mm:ss" : "hh:mm:ss a").format(mTime.getTime());

	}
    
}
