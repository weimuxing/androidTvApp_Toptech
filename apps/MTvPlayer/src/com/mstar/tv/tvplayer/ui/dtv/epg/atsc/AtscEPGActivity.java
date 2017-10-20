//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tv.tvplayer.ui.dtv.epg.atsc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscEpgEventInfo;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.TimeOutHelper;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.dtv.epg.RecordActivity;
import com.mstar.tv.tvplayer.ui.dtv.epg.ScheduleListActivity;
import com.mstar.tv.tvplayer.ui.holder.EPGViewHolder;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;
import com.mstar.util.Utility;

public class AtscEPGActivity extends MstarBaseActivity {

    private static final String TAG = "AtscEPGActivity";

    private int mTvSystem = 0;

    private final static short EPG_UPDATE_LIST = 0x01;

    private final static short EPG_UPDATE_LIST_NOTIFY = 0x08;

    private final static short EPG_ITEM_COUNT_PER_PAGE = 9999;

    private final static int TIME_OUT_MSG = 51;

    private final int CHANNEL_NAME_MAX_LEN = 15;

    private ArrayList<EPGViewHolder> mEpgEventViewHolderList = new ArrayList<EPGViewHolder>();

    private ArrayList<EPGViewHolder> mEpgProgramViewHolderList = new ArrayList<EPGViewHolder>();

    private List<AtscEpgEventInfo> mEventInfoList = new ArrayList<AtscEpgEventInfo>();

    private ArrayList<ProgramInfo> mProgInfoList = new ArrayList<ProgramInfo>();

    private boolean mIsEpgChannel = true;

    private ListView mEpgListView;

    private AtscEPGAdapter mEventAdapter;

    private AtscEPGAdapter mProgramAdapter;

    private TextView mComDateNameTextView = null;

    private TvChannelManager mTvChannelManager = null;

    private TvAtscChannelManager mTvAtscChannelManager= null;

    private TvTimerManager mTvTimerManager= null;

    private TextView mTimeInfo = null;

    private TextView mContext = null;

    private TextView mTitle = null;

    private LinearLayout mInfoLay = null;

    private TextView mRatingTxt = null;

    private static int mUserProgramIndex = 0;

    private static int mUserEventIndex = 0;

    private static long mOffsetTimeInMs = 0;

    private boolean mInfoDisplay = false;

    private Time mNextEventBaseTime = null;

    private Time mNextProgramBaseTime = null;

    private int mTotalEventCount = 0;

    private int mTotalProgramCount = 0;

    private int mPrevConstructEventCount = 0;

    private int mPrevConstructProgramCount = 0;

    private TimeOutHelper mTimeOutHelper;

    private TvEpgManager mTvEpgManager = null;

    private MyHandler mMyTvHandler = null;

    private OnTvPlayerEventListener mTvPlayerEventListener = null;

    private ProgramInfo curProgInfo = null;

    private Intent intent = null;

    private boolean mIsPvrEnable = false;

    private ImageView mEpgTipsYellowImg = null;

    private ImageView mEpgTipsBlueImg = null;

    private ImageView mEpgTipsRedImg = null;

    private TextView mEpgTipsYellowTxt = null;

    private TextView mEpgTipsBlueTxt = null;

    private TextView mEpgTipsRedTxt = null;

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
            AtscEPGActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    refreshProgrameGuideListChannel();
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

    private void setEventData(String[] str_name, String[] str_info, boolean b_cc) {
        for (int i = 0; i < str_name.length; i++) {
            EPGViewHolder vh = new EPGViewHolder();
            vh.setChannelInfo(str_info[i]);
            vh.setChannelName(str_name[i]);
            vh.setCcInfo(b_cc);
            // vh.setInfo(info[i]);
            mEpgEventViewHolderList.add(vh);
        }
    }

    private void setProgramData(String[] str_name, String[] str_info, boolean b_cc) {
        for (int i = 0; i < str_name.length; i++) {
            EPGViewHolder vh = new EPGViewHolder();
            vh.setChannelInfo(str_info[i]);
            vh.setChannelName(str_name[i]);
            vh.setCcInfo(b_cc);
            // vh.setInfo(info[i]);
            mEpgProgramViewHolderList.add(vh);
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            super.handleMessage(msg);
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();

        setContentView(R.layout.atscprogramme_epg);
        mEpgListView = (ListView) findViewById(R.id.atscprogramme_epg_list_view);

        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            int focusIdx = getIntent().getIntExtra("FocusIndex", 0);
        }

        mEventAdapter = new AtscEPGAdapter(this, mEpgEventViewHolderList);
        mProgramAdapter = new AtscEPGAdapter(this, mEpgProgramViewHolderList);
        if (mIsEpgChannel == true) {
            mEpgListView.setAdapter(mEventAdapter);
        } else {
            mEpgListView.setAdapter(mProgramAdapter);
        }
        mEpgListView.setDividerHeight(0);
        mComDateNameTextView = (TextView) findViewById(R.id.atscprogramme_epg_context);
        mTitle = (TextView) findViewById(R.id.atscprogramme_epg_title);
        mTimeInfo = (TextView) findViewById(R.id.atscprogramme_epg_info_time);
        mContext = (TextView) findViewById(R.id.atscprogramme_epg_info_context);
        mInfoLay = (LinearLayout) findViewById(R.id.atscprogramme_epg_info_layout);
        mRatingTxt = (TextView) findViewById(R.id.atscprogramme_epg_rating);

        mInfoLay.setVisibility(View.INVISIBLE);

        curProgInfo = new ProgramInfo();
        TVRootApp app = (TVRootApp) getApplication();
        mIsPvrEnable = app.isPVREnable();
        if (!mIsPvrEnable) {
            Log.d(TAG, "PVR not enable");
            mEpgTipsYellowImg = (ImageView) findViewById(R.id.atscprogram_epg_tips_yellow_image);
            mEpgTipsBlueImg = (ImageView) findViewById(R.id.atscprogram_epg_tips_blue_image);
            mEpgTipsRedImg = (ImageView) findViewById(R.id.atscprogram_epg_tips_red_image);
            mEpgTipsYellowTxt = (TextView) findViewById(R.id.atscprogram_epg_tips_yellow_text);
            mEpgTipsBlueTxt = (TextView) findViewById(R.id.atscprogram_epg_tips_blue_text);
            mEpgTipsRedTxt = (TextView) findViewById(R.id.atscprogram_epg_tips_red_text);
            mEpgTipsYellowImg.setVisibility(View.GONE);
            mEpgTipsBlueImg.setVisibility(View.GONE);
            mEpgTipsRedImg.setVisibility(View.GONE);
            mEpgTipsYellowTxt.setVisibility(View.GONE);
            mEpgTipsBlueTxt.setVisibility(View.GONE);
            mEpgTipsRedTxt.setVisibility(View.GONE);
        }
        mTvChannelManager = TvChannelManager.getInstance();
        mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        mTvTimerManager = TvTimerManager.getInstance();
        mTvEpgManager = TvEpgManager.getInstance();
        resetAllNextBaseTime(true);
        mMyTvHandler = new MyHandler();

        // get cur time zone offset time
        Time curTime = mTvTimerManager.getCurrentTvTime();
        mOffsetTimeInMs = mTvEpgManager.getEpgEventOffsetTime(curTime, true) * 1000;
        Log.d(TAG, "mOffsetTimeInMs:"  + mOffsetTimeInMs);

        curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();
        int programCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        mUserProgramIndex = 0;
        mProgInfoList.clear();
        for (int i = 0, j = 0; i < programCount; i++) {
            ProgramInfo tmpProgInfo = mTvAtscChannelManager.getProgramInfo(i);
            if (tmpProgInfo.serviceType == TvChannelManager.SERVICE_TYPE_DTV) {
                if ((curProgInfo.majorNum == tmpProgInfo.majorNum)
                    && (curProgInfo.minorNum == tmpProgInfo.minorNum)) {
                    mUserProgramIndex = j;
                }
                mProgInfoList.add(tmpProgInfo);
                j++;
            }
        }
        Log.d(TAG, "mProgInfoList.size(): " + mProgInfoList.size());
        Log.d(TAG, "mUserProgramIndex: " + mUserProgramIndex);
        Log.d(TAG, "mIsEpgChannel: " + mIsEpgChannel);

        registerListener();

        if (mIsEpgChannel) {
            mTitle.setText(R.string.str_epg_title_channel);
            updateChannelNumber();

        } else {
            mTitle.setText(R.string.str_epg_title_time);
            updateDateTime(0);
        }

        // time out to kill this
        mTimeOutHelper = new TimeOutHelper(epgHandler, this);

        ProgramInfo progInfo = mTvAtscChannelManager.getCurrentProgramInfo();
        TvCommonManager.getInstance().stopTts();
        TvCommonManager.getInstance().speakTtsDelayed(
            mTitle.getText().toString() + ", " + mTvAtscChannelManager.getDispChannelNum(progInfo) + "," + mTvAtscChannelManager.getDispChannelName(progInfo)
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
        mEpgListView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ttsSpeakFocusItem(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private Handler epgHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Handler: " + msg.what);
            switch (msg.what) {
                case EPG_UPDATE_LIST_NOTIFY:
                    resetAllNextBaseTime(true);
                    constructmEventInfoList();
                    mEventAdapter.notifyDataSetChanged();
                    break;

                case EPG_UPDATE_LIST:
                    if (mIsEpgChannel == true) {
                        constructmEventInfoList();
                        mEventAdapter.notifyDataSetChanged();
                    } else {
                        constructProgramInfoList(null);
                        mProgramAdapter.notifyDataSetChanged();
                    }

                    mEpgListView.invalidate();
                    break;
                case TIME_OUT_MSG:
                    finish();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        mTimeOutHelper.start();
        mTimeOutHelper.init();
        mTvPlayerEventListener = new TvPlayerEventListener();
        mTvChannelManager.registerOnTvPlayerEventListener(mTvPlayerEventListener);
        if (mIsEpgChannel) {
            refreshProgrameGuideListChannel();
        } else {
            refreshProgrameGuideListTime(0);
        }
    };

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        mTimeOutHelper.stop();
        mTvChannelManager.unregisterOnTvPlayerEventListener(mTvPlayerEventListener);
        mTvPlayerEventListener = null;
        TvCommonManager.getInstance().stopTts();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mTimeOutHelper.reset();
        Log.d(TAG, "onKeyDown: " + event);
        final int selectedItemPosition = mEpgListView.getSelectedItemPosition();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mInfoLay.getVisibility() == View.VISIBLE) {
                    mInfoDisplay = false;
                    mContext.setText("");
                    mInfoLay.setVisibility(View.GONE);
                } else {
                    finish();
                }
                return true;

            case KeyEvent.KEYCODE_PROG_GREEN:
                if (mEpgListView.isFocused()) {
                    mInfoDisplay = !mInfoDisplay;
                    if (!mInfoDisplay) {
                        if (mInfoLay.getVisibility() == View.VISIBLE) {
                            mContext.setText("");
                            mInfoLay.setVisibility(View.GONE);
                        }
                    } else {
                        updateEpgProgramInfo(keyCode);
                        ttsSpeakFocusItem(mEpgListView.getSelectedItemPosition());
                    }
                }
                return true;

            case KeyEvent.KEYCODE_PROG_YELLOW:
                if (!mIsPvrEnable) {
                    Log.d(TAG, "PVR not enable");
                    break;
                }
                if (mInfoLay.getVisibility() == View.VISIBLE) {
                    mContext.setText("");
                    mInfoLay.setVisibility(View.GONE);
                }
                intent = new Intent(this, ScheduleListActivity.class);
                startActivity(intent);
                finish();
                return true;

            case KeyEvent.KEYCODE_PROG_RED:
                if (!mIsPvrEnable) {
                    Log.d(TAG, "PVR not enable");
                    break;
                }
                if (mInfoLay.getVisibility() == View.VISIBLE) {
                    mContext.setText("");
                    mInfoLay.setVisibility(View.GONE);
                }
                Log.d(TAG, "EpgChannel:" + mIsEpgChannel + "Position:" + selectedItemPosition);
                if (ListView.INVALID_POSITION == selectedItemPosition) {
                    return true;
                }
                if (mIsEpgChannel) {
                    intent = new Intent(this, RecordActivity.class);
                    intent.setClass(AtscEPGActivity.this, RecordActivity.class);
                    if (mEpgListView.getSelectedItemPosition() >= mEventInfoList.size()) {
                        return true;
                    }
                    AtscEpgEventInfo epgEventInfo = mEventInfoList.get(selectedItemPosition);
                    Log.d(TAG, "ChannelNum = " + curProgInfo.number);
                    Log.d(TAG, "EventBaseTime = " + mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime));
                    Log.d(TAG, "FocusIndex = " + selectedItemPosition);
                    intent.putExtra("ChannelNum", curProgInfo.number);
                    intent.putExtra("EventBaseTime", mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime));
                    intent.putExtra("FocusIndex", selectedItemPosition);
                    intent.putExtra(Constant.EVENT_BASED_RECORDING, mIsEpgChannel);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(this, RecordActivity.class);
                    intent.setClass(AtscEPGActivity.this, RecordActivity.class);
                    Log.d(TAG, "eventInfoList.size()" + mEventInfoList.size());
                    int channelNum = curProgInfo.number;
                    Log.d(TAG, "mIsEpgChannel != true, 1.channelNum = " + channelNum);
                    if ((mEpgProgramViewHolderList == null)
                            || mEpgProgramViewHolderList.size() <= selectedItemPosition) {
                        Log.d(TAG, "Get Channel Num error");
                        return true;
                    }
                    channelNum = mEpgProgramViewHolderList.get(selectedItemPosition).getChannelNum();
                    Log.d(TAG, "mIsEpgChannel != true, 2.channelNum = " + channelNum);
                    intent.putExtra("ChannelNum", channelNum);
                    /* use UTC for Record Activity */
                    if (mEventInfoList.size() == 0) {
                        Time curTime = mTvTimerManager.getCurrentTvTime();
                        intent.putExtra("EventBaseTime", (int) (curTime.toMillis(true) / 1000));
                        Log.d(TAG, "mIsEpgChannel != true, 1.EventBaseTime = " + (int) (curTime.toMillis(true) / 1000));
                    } else {
                        AtscEpgEventInfo epgEventInfo = new AtscEpgEventInfo();
                        epgEventInfo = mEventInfoList.get(mUserEventIndex);
                        intent.putExtra("EventBaseTime", epgEventInfo.startTime);
                        Log.d(TAG, "mIsEpgChannel != true, 2.EventBaseTime = " + epgEventInfo.startTime);
                    }
                    Log.d(TAG, "mIsEpgChannel != true, FocusIndex = " + selectedItemPosition);
                    intent.putExtra("FocusIndex", selectedItemPosition);
                    intent.putExtra(Constant.EVENT_BASED_RECORDING, mIsEpgChannel);
                    startActivity(intent);
                    finish();
                }
                return true;

            case KeyEvent.KEYCODE_PROG_BLUE:
                if (!mIsPvrEnable) {
                    Log.d(TAG, "PVR not enable");
                    break;
                }
                if (mInfoLay.getVisibility() == View.VISIBLE) {
                    mContext.setText("");
                    mInfoLay.setVisibility(View.GONE);
                }

                if (ListView.INVALID_POSITION == selectedItemPosition) {
                    return true;
                }

                intent = new Intent(TvIntent.ACTION_EPG_REMINDER);
                AtscEpgEventInfo epgEventInfo = null;

                if (true == mIsEpgChannel) {
                    if (0 <= selectedItemPosition && mEventInfoList.size() > selectedItemPosition) {
                        epgEventInfo = mEventInfoList.get(selectedItemPosition);
                    }
                } else {
                    if (mEventInfoList.size() > 0) {
                        epgEventInfo = mEventInfoList.get(0);
                    }
                }

                if (null != epgEventInfo) {
                    int clkOffset = mTvTimerManager.getClockOffset();
                    Log.d(TAG, "EventBaseTime = " + mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime) + clkOffset);
                    Log.d(TAG, "FocusIndex = " + selectedItemPosition);
                    intent.putExtra("EventBaseTime", mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime) + clkOffset);
                    intent.putExtra("FocusIndex", selectedItemPosition);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    finish();
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (!mComDateNameTextView.isFocused()) {
                    return true;
                }
                if (mInfoLay.getVisibility() == View.VISIBLE) {
                    mContext.setText("");
                    mInfoLay.setVisibility(View.GONE);
                }

                if (mComDateNameTextView.isFocused()) {
                    if (mIsEpgChannel) {
                        changeProgramNumber(true);
                    } else {
                        changeEventStartTime(true);
                    }
                } else {
                    mIsEpgChannel = !mIsEpgChannel;

                    if (mIsEpgChannel == true) {
                        mEpgListView.setAdapter(mEventAdapter);
                    } else {
                        mEpgListView.setAdapter(mProgramAdapter);
                    }
                    resetAllNextBaseTime(false);
                    if (mIsEpgChannel) {
                        mTitle.setText(R.string.str_epg_title_channel);
                        constructProgrameGuideListChannel();
                    } else {
                        mTitle.setText(R.string.str_epg_title_time);
                        constructProgrameGuideListTime(0);
                    }
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (!mComDateNameTextView.isFocused()) {
                    return true;
                }
                if (mInfoLay.getVisibility() == View.VISIBLE) {
                    mContext.setText("");
                    mInfoLay.setVisibility(View.GONE);
                }

                if (mComDateNameTextView.isFocused()) {
                    if (mIsEpgChannel) {
                        changeProgramNumber(false);
                    } else {
                        changeEventStartTime(false);
                    }

                } else {
                    mIsEpgChannel = !mIsEpgChannel;

                    if (mIsEpgChannel == true) {
                        mEpgListView.setAdapter(mEventAdapter);
                    } else {
                        mEpgListView.setAdapter(mProgramAdapter);
                    }
                    resetAllNextBaseTime(false);
                    if (mIsEpgChannel) {
                        mTitle.setText(R.string.str_epg_title_channel);
                        constructProgrameGuideListChannel();
                    } else {
                        mTitle.setText(R.string.str_epg_title_time);
                        constructProgrameGuideListTime(0);
                    }
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void registerListener() {
        mEpgListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                mTimeOutHelper.reset();
                // TODO Auto-generated method stub
                if (mIsEpgChannel == false) {
                    // go to the selected channel
                    ProgramInfo curProgInfo = new ProgramInfo();
                    TVRootApp app = (TVRootApp) getApplication();
                    if (0xffffffff == mEpgListView.getSelectedItemPosition()) {
                        return;
                    }

                    curProgInfo = mTvAtscChannelManager.getProgramInfo(mEpgListView.getSelectedItemPosition());

                    mTvAtscChannelManager.programSel(curProgInfo.majorNum, curProgInfo.minorNum);
                    resetAllNextBaseTime(true);
                }
            }
        });

        mEpgListView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mInfoDisplay) {
                        updateEpgProgramInfo(KeyEvent.KEYCODE_PROG_GREEN);
                    }
                    ttsSpeakFocusItem(mEpgListView.getSelectedItemPosition());
                } else {
                    if (mInfoLay.getVisibility() == View.VISIBLE) {
                        mContext.setText("");
                        mInfoLay.setVisibility(View.GONE);
                    }
                    TvCommonManager.getInstance().stopTts();
                }
            }
        });
        mEpgListView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                mTimeOutHelper.reset();
                if (mInfoDisplay) {
                    int curFocusIdx = mEpgListView.getSelectedItemPosition();
                    if (((keyCode == KeyEvent.KEYCODE_DPAD_UP) || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN))
                            && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (0xffffffff != curFocusIdx) {
                            updateEpgProgramInfo(keyCode);
                        }
                    }
                }
                return false;
            }
        });

        mTitle.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mInfoLay.getVisibility() == View.VISIBLE) {
                        mContext.setText("");
                        mInfoLay.setVisibility(View.GONE);
                    }
                } else {
                    if (mEpgEventViewHolderList.isEmpty()) {
                        if (mInfoLay.getVisibility() == View.VISIBLE) {
                            mContext.setText("");
                            mInfoLay.setVisibility(View.GONE);
                        }
                    } else {
                        mContext.setText(mEpgEventViewHolderList.get(0).getInfo());
                    }
                }
            }
        });
    }

    private void resetAllNextBaseTime(boolean initAll) {
        Log.d(TAG, "resetAllNextBaseTime, initAll: " + initAll);
        Log.d(TAG, "resetAllNextBaseTime, mIsEpgChannel: " + mIsEpgChannel);
        if (initAll == true) {
            mNextEventBaseTime = null;
            mPrevConstructEventCount = 0;
            mTotalEventCount = 0;
            mNextProgramBaseTime = null;
            mPrevConstructProgramCount = 0;
            mTotalProgramCount = 0;
        } else {
            if (mIsEpgChannel == true) {
                mNextEventBaseTime = null;
                mPrevConstructEventCount = 0;
                mTotalEventCount = 0;
            } else {
                mNextProgramBaseTime = null;
                mPrevConstructProgramCount = 0;
                mTotalProgramCount = 0;
            }
        }
    }

    private boolean constructmEventInfoList() {
        Log.d(TAG, "constructmEventInfoList()");
        ProgramInfo curProgInfo = new ProgramInfo();
        AtscEpgEventInfo epgEventInfo = new AtscEpgEventInfo();
        List<AtscEpgEventInfo> mEventInfoListTemp = new ArrayList<AtscEpgEventInfo>();

        if ((mPrevConstructEventCount >= mTotalEventCount) && (mTotalEventCount > 0)) {
            // construct finish
            return false;
        }
        curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();

        // initial
        if (mNextEventBaseTime == null) {
            mNextEventBaseTime = mTvTimerManager.getCurrentTvTime();

            mEpgEventViewHolderList.clear();
            mEventInfoList.clear();
            Log.d(TAG, "mNextEventBaseTime :" + mNextEventBaseTime);
            mTotalEventCount = mTvEpgManager.getAtscEventCount(curProgInfo.majorNum, curProgInfo.minorNum,
                (int)curProgInfo.serviceType, curProgInfo.progId, mNextEventBaseTime);

            mPrevConstructEventCount = 0;
        } else if (mTotalEventCount == 0) {
            mTotalEventCount = mTvEpgManager.getAtscEventCount(curProgInfo.majorNum, curProgInfo.minorNum,
                (int)curProgInfo.serviceType, curProgInfo.progId, mNextEventBaseTime);
            mPrevConstructEventCount = 0;
        }
        if (mTotalEventCount == 0) {
            return false;
        }

        mEventInfoListTemp.clear();
        mEventInfoListTemp = getEpgEvents();
        /* Clock offset = Timezone + DST */
        int clkOffset = mTvTimerManager.getClockOffset();
        GregorianCalendar calendar = new GregorianCalendar(0, 0, 0, 0, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        int tmpTime;
        boolean isDST = TvTimerManager.getInstance().getDaylightSavingState();
        if (mEventInfoListTemp != null) {
            for (int i = 0; i < mEventInfoListTemp.size(); i++) {
                mEventInfoList.add(mEventInfoListTemp.get(i));
                int startH;
                int startM;
                int endH;
                int endM;
                // push event start time -> end time & event name in list
                String[] eventNameStr = {
                    null
                };
                String[] mTimeInfoStr = {
                    null
                };

                epgEventInfo = mEventInfoListTemp.get(i);
                eventNameStr[0] = epgEventInfo.sName;

                tmpTime = mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime) + clkOffset;
                calendar.setTimeInMillis(tmpTime * 1000L);
                startH = calendar.get(Calendar.HOUR_OF_DAY);
                startM = calendar.get(Calendar.MINUTE);

                tmpTime = mTvTimerManager.convertGPSTime2UTC(epgEventInfo.endTime) + clkOffset;
                calendar.setTimeInMillis(tmpTime * 1000L);
                endH = calendar.get(Calendar.HOUR_OF_DAY);
                endM = calendar.get(Calendar.MINUTE);

                /* If you want to change the displaying format, you need to make sure TTS speaking is correct. */
                mTimeInfoStr[0] = String.format("%02d:%02d - %02d:%02d", startH, startM, endH, endM);

                setEventData(eventNameStr, mTimeInfoStr, epgEventInfo.bHasCCInfo);
            }
            if (mEventInfoListTemp.size() > 0) {
                // store current last event's end time
                epgEventInfo = mEventInfoListTemp.get(mEventInfoListTemp.size() - 1);
                tmpTime = mTvTimerManager.convertGPSTime2UTC(epgEventInfo.endTime);
                if (true == isDST) {
                    tmpTime += 3600;
                }
                mNextEventBaseTime.set((long) (tmpTime) * 1000);

                mPrevConstructEventCount += EPG_ITEM_COUNT_PER_PAGE;
            }
        }
        return true;
    }

    private boolean constructProgramInfoList(Time baseTime) {
        Log.d(TAG, "constructProgramInfoList()");
        int availableProgramCount = 0;
        ProgramInfo specificProgInfo = new ProgramInfo();
        AtscEpgEventInfo epgEventInfo = new AtscEpgEventInfo();
        List<AtscEpgEventInfo> mEventInfoListTemp = new ArrayList<AtscEpgEventInfo>();

        if ((mPrevConstructProgramCount >= mTotalProgramCount) && (mTotalProgramCount > 0)) {
            // construct finish
            return false;
        }

        // initial
        if (baseTime != null) {
            if (mNextProgramBaseTime == null) {
                mNextProgramBaseTime = new Time();
            }
            mNextProgramBaseTime.set(baseTime.toMillis(true) - mOffsetTimeInMs);
            mEpgProgramViewHolderList.clear();
            mTotalProgramCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            mPrevConstructProgramCount = 0;
        } else {
            if (mNextProgramBaseTime == null) {
                mNextProgramBaseTime = mTvTimerManager.getCurrentTvTime();
                mEpgProgramViewHolderList.clear();
                mTotalProgramCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
                mPrevConstructProgramCount = 0;
            } else if (mTotalProgramCount == 0) {
                mTotalProgramCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
                mPrevConstructProgramCount = 0;
            }
        }

        if (mTotalProgramCount == 0) {
            return false;
        }
        Log.d(TAG, "constructProgramInfoList(): mTotalProgramCount: " + mTotalProgramCount);

        for (int i = mPrevConstructProgramCount; i < mTotalProgramCount; i++) {
            specificProgInfo = mTvAtscChannelManager.getProgramInfo(i);

            if (i == (mTotalProgramCount - 1)) {
                mPrevConstructProgramCount = i + 1;
            }

            if (specificProgInfo == null) {
                continue;
            }

            if ((specificProgInfo.isDelete == true) || (specificProgInfo.isVisible == false)) {
                continue;
            }

            if (mEventInfoListTemp != null) {
                mEventInfoListTemp.clear();
                mEventInfoListTemp = getEpgEvents();

                epgEventInfo = mEventInfoListTemp.get(0);
            }
            if (epgEventInfo == null) {
                continue;
            }

            // push service name & event name in list,
            String[] eventNameStr = {
                null
            };
            String[] serviceInfoStr = {
                null
            };
            eventNameStr[0] = epgEventInfo.sName;
            serviceInfoStr[0] = specificProgInfo.number + "  " + specificProgInfo.serviceName;
            setProgramData(eventNameStr, serviceInfoStr, false);

            availableProgramCount++;
            if (availableProgramCount >= EPG_ITEM_COUNT_PER_PAGE) {
                mPrevConstructProgramCount = i + 1;
                break;
            }
        }

        return true;
    }

    private void updateChannelNumber() {
        ProgramInfo curProgInfo = new ProgramInfo();

        // get event info
        Time baseTime = mTvTimerManager.getCurrentTvTime();

        // show cur service number
        curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();

        String channum = mTvAtscChannelManager.getDispChannelNum(curProgInfo);
        String name = mTvAtscChannelManager.getDispChannelName(curProgInfo);
        if (name.length() > CHANNEL_NAME_MAX_LEN) {
            name = name.substring(0, CHANNEL_NAME_MAX_LEN - 3) + "...";
        }
        mComDateNameTextView.setText(channum + " " + name);
    }

    private void updateDateTime(long timeInSec) {
        // show date&time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Time stTime = getSTTime(timeInSec);
        Date curDate = new Date(stTime.toMillis(true));
        String dateStr = formatter.format(curDate);

        mComDateNameTextView.setText(dateStr);
    }

    private boolean constructProgrameGuideListChannel() {
        Log.d(TAG, "constructProgrameGuideListChannel()");

        updateChannelNumber();
        constructmEventInfoList();
        mEventAdapter.notifyDataSetChanged();
        mEpgListView.invalidate();
        return true;
    }

    private boolean constructProgrameGuideListTime(long timeInSec) {
        Log.d(TAG, "constructProgrameGuideListTime()");
        updateDateTime(timeInSec);
        constructProgramInfoList(getSTTime(timeInSec));
        mProgramAdapter.notifyDataSetChanged();
        mEpgListView.invalidate();
        return true;
    }

    public void changeProgramNumber(boolean isLeftKey) {
        TVRootApp app = (TVRootApp) getApplication();
        ProgramInfo progInfo = new ProgramInfo();
        // String tmpStr = null;

        int tmpindex = mUserProgramIndex;
        while (true) {
            if (isLeftKey) {
                if (mUserProgramIndex > 0) {
                    mUserProgramIndex--;
                } else {
                    mUserProgramIndex = mProgInfoList.size() - 1;
                }
                Log.d(TAG, "changeProgramNumber() mUserProgramIndex: " + mUserProgramIndex);

            } else {
                if (mUserProgramIndex < (mProgInfoList.size() - 1)) {
                    mUserProgramIndex++;
                } else {
                    mUserProgramIndex = 0;
                }
            }

            Log.d(TAG, "mUserProgramIndex:"  + mUserProgramIndex);

            progInfo = mProgInfoList.get(mUserProgramIndex);

            if (progInfo.isDelete || progInfo.isHide || progInfo.isSkip || !progInfo.isVisible) {
                if (tmpindex == mUserProgramIndex) {
                    break;
                } else {
                    continue;
                }

            } else {
                break;
            }
        }
        mTvAtscChannelManager.programSel(progInfo.majorNum, progInfo.minorNum);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        resetAllNextBaseTime(true);
        constructProgrameGuideListChannel();
    }

    public void changeEventStartTime(boolean isLeftKey) {
        AtscEpgEventInfo epgEventInfo = new AtscEpgEventInfo();

        if (mEventInfoList.size() == 0) {
            return;
        }
        if (isLeftKey) {
            if (mUserEventIndex > 0) {
                mUserEventIndex--;
            } else {
                mUserEventIndex = mEventInfoList.size() - 1;
            }
        } else {
            if (mUserEventIndex < (mEventInfoList.size() - 1)) {
                mUserEventIndex++;
            } else {
                mUserEventIndex = 0;
            }
        }

        epgEventInfo = mEventInfoList.get(mUserEventIndex);

        resetAllNextBaseTime(false);
        constructProgrameGuideListTime(epgEventInfo.startTime);
    }

    public boolean updateEpgProgramInfo(int keycode) {
        if (mIsEpgChannel) {
            ProgramInfo curProgInfo = new ProgramInfo();
            AtscEpgEventInfo epgEventInfo = new AtscEpgEventInfo();
            TVRootApp app = (TVRootApp) getApplication();
            int curFocusIdx = mEpgListView.getSelectedItemPosition();
            if (0xffffffff == curFocusIdx) {
                return true;
            }

            if (keycode == KeyEvent.KEYCODE_DPAD_UP) {
                if (0 != curFocusIdx) {
                    curFocusIdx--;
                } else {
                    return true;
                }
            } else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if ((curFocusIdx + 1) < mEventInfoList.size()) {
                    curFocusIdx++;
                } else {
                    return true;
                }
            }
            curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();
            if (curFocusIdx >= mEventInfoList.size()) {
                mContext.setText("");
                return true;
            }
            epgEventInfo = mEventInfoList.get(curFocusIdx);
            /* Clock offset = Timezone + DST */
            int clkOffset = mTvTimerManager.getClockOffset();
            GregorianCalendar calendar = new GregorianCalendar(0, 0, 0, 0, 0, 0);
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            int startTime;
            int endTime;
            int startYear;
            int startMonth;
            int startDay;
            int startHour;
            int startMinute;
            int endHour;
            int endMinute;

            startTime = mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime) + clkOffset;
            calendar.setTimeInMillis(startTime * 1000L);
            startYear = calendar.get(Calendar.YEAR);
            startMonth = calendar.get(Calendar.MONTH) + 1;
            startDay = calendar.get(Calendar.DAY_OF_MONTH);
            startHour = calendar.get(Calendar.HOUR_OF_DAY);
            startMinute = calendar.get(Calendar.MINUTE);

            endTime = mTvTimerManager.convertGPSTime2UTC(epgEventInfo.endTime) + clkOffset;
            calendar.setTimeInMillis(endTime * 1000L);
            endHour = calendar.get(Calendar.HOUR_OF_DAY);
            endMinute = calendar.get(Calendar.MINUTE);

            String mTimeInfoStr = String.format("%d/%02d/%02d %02d:%02d-%02d:%02d", startYear, startMonth, startDay, startHour, startMinute, endHour, endMinute);
            mTimeInfo.setText(mTimeInfoStr);

            mRatingTxt.setText(mTvAtscChannelManager.getCurrentRatingInformation());
            // show event description
            AtscEpgEventInfo epgEvInfoExted = mTvEpgManager.getEventExtendInfoByTime(
                curProgInfo.majorNum, curProgInfo.minorNum, (int)curProgInfo.serviceType,
                curProgInfo.progId, startTime);
            if (epgEvInfoExted == null) {
                Log.e(TAG, "AtscEpgEventInfo: no data");
                mContext.setText(R.string.str_epg_event_extend_no_info);
            } else {
                mContext.setText(epgEvInfoExted.sExtendedText);
            }
            mInfoLay.setVisibility(View.VISIBLE);
        } else {
            ProgramInfo curProgInfo = new ProgramInfo();
            AtscEpgEventInfo epgEventInfo = new AtscEpgEventInfo();
            TVRootApp app = (TVRootApp) getApplication();
            int curFocusIdx = 0;
            /*
             * if (LoadCurFocusChannelEventList() == false) { return true; }
             */

            curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();
            if (curFocusIdx >= mEventInfoList.size()) {
                return true;
            }
            epgEventInfo = mEventInfoList.get(curFocusIdx);

            // show event start time and end time
            Time startTime = new Time();
            // startTime.switchTimezone("Euro/London");
            startTime.set((long) epgEventInfo.startTime * 1000 - mOffsetTimeInMs); // s->ms
            Time endTime = new Time();
            // endTime.switchTimezone("Euro/London");
            endTime.set((long) epgEventInfo.endTime * 1000 - mOffsetTimeInMs); // s->ms
            String mTimeInfoStr = startTime.year + "/" + (startTime.month + 1) + "/"
                    + startTime.monthDay + " " + startTime.hour + ":" + startTime.minute + "-"
                    + endTime.hour + ":" + endTime.minute;
            mTimeInfo.setText(mTimeInfoStr);

            // show event description
            int utcTime = mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime);
            int offset = mTvTimerManager.getClockOffset();
            Time extTime = new Time();
            extTime.set((long) (utcTime + offset) * 1000);

            AtscEpgEventInfo epgEvInfoExted = mTvEpgManager.getEventExtendInfoByTime(
                curProgInfo.majorNum, curProgInfo.minorNum, (int)curProgInfo.serviceType,
                curProgInfo.progId, extTime);
            if (epgEvInfoExted == null) {
                Log.e(TAG, "AtscEpgEventInfo: no data");
                mContext.setText(R.string.str_epg_event_extend_no_info);
            } else {
                mContext.setText(epgEvInfoExted.sExtendedText);
            }

            mInfoLay.setVisibility(View.VISIBLE);
        }
        return false;
    }

    private List<AtscEpgEventInfo> getEpgEvents() {
        List<AtscEpgEventInfo> eventInfos = new ArrayList<AtscEpgEventInfo>();
        Time currTime;
        currTime = mTvTimerManager.getCurrentTvTime();
        ProgramInfo channelInfo = mTvAtscChannelManager.getCurrentProgramInfo();
        boolean canBegin = mTvEpgManager.beginToGetEventInformation(channelInfo.number,
                channelInfo.majorNum, channelInfo.minorNum, channelInfo.progId);
        Log.e(TAG, "the flag for begining to get epg information:" + canBegin + ",channelnum:"
                + channelInfo.majorNum + "-" + channelInfo.minorNum);
        if (canBegin) {
            int count = mTvEpgManager.getAtscEventCount(channelInfo.majorNum, channelInfo.minorNum,
                    channelInfo.number, channelInfo.progId, currTime);
            Log.e(TAG, "getEventCount:" + count);
            if (count > 0) {
                AtscEpgEventInfo info;
                info = mTvEpgManager.getFirstEventInformation(currTime);
                Log.e(TAG, "adjust the first event information is null:" + (info == null)
                        + ",epg event count:" + count);
                do {
                    if (info == null) {
                        Log.e(TAG, "AtscEpgEventInfo: no data");
                        break;
                    } else {
                        if (eventInfos.size() == 0) {
                            info.endTime = info.startTime + info.durationTime;
                            Log.e(TAG, "epg event, info.startTime: " + info.startTime);
                            Log.e(TAG, "epg event, info.endTime: " + info.endTime);
                            Log.e(TAG, "epg event, info.durationTime: " + info.durationTime);
                            eventInfos.add(info);
                        } else {
                            AtscEpgEventInfo lastInfo = eventInfos.get(eventInfos.size() - 1);
                            // to filter the repeat EPG event information.
                            Log.i(TAG, info.sName);
                            if (lastInfo.startTime != info.startTime) {
                                info.endTime = info.startTime + info.durationTime;
                                Log.e(TAG, "epg event, info.startTime: " + info.startTime);
                                Log.e(TAG, "epg event, info.endTime: " + info.endTime);
                                Log.e(TAG, "epg event, info.durationTime: " + info.durationTime);
                                eventInfos.add(info);
                            }
                        }
                    }

                    count--;
                    if (count <= 0) {
                        break;
                    }

                    info = mTvEpgManager.getNextEventInformation();
                } while (true);
            }
        }
        mTvEpgManager.endToGetEventInformation();

        return eventInfos;
    }

    private void refreshProgrameGuideListChannel() {
        resetAllNextBaseTime(true);
        constructmEventInfoList();
        mEventAdapter.notifyDataSetChanged();
        mEpgListView.invalidate();
        if (mInfoDisplay) {
            if (mEpgListView.isFocused()) {
                updateEpgProgramInfo(KeyEvent.KEYCODE_PROG_GREEN);
            }
        }
        mTimeOutHelper.reset();
    }

    private void refreshProgrameGuideListTime(long timeInSec) {
        resetAllNextBaseTime(true);
        constructProgramInfoList(getSTTime(timeInSec));
        mProgramAdapter.notifyDataSetChanged();
        mEpgListView.invalidate();
    }

    private Time getSTTime(long timeInSec) {
        // get st time
        Time stTime = new Time();
        if (timeInSec == 0) {
            // use default time
            stTime = mTvTimerManager.getCurrentTvTime();
        } else {
            // use specific time
            stTime.set(timeInSec * 1000);
        }
        return stTime;
    }

    public void ttsSpeakFocusItem(int position) {
        EPGViewHolder ev = mEpgEventViewHolderList.get(position);
        String str = ev.getChannelInfo() + ", "+ Utility.getStrLimited(ev.getChannelName(), Constant.TTS_EVENT_NAME_MAX_LENGTH);
        if (mInfoDisplay) {
            str = str + mContext.getText().toString();
        }
        TvCommonManager.getInstance().speakTtsDelayed(
            str
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
            , TvCommonManager.TTS_DELAY_TIME_100MS);
    }
}
