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

package com.mstar.tv.tvplayer.ui.dtv.epg;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.vo.EpgEventTimerInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.EpgEventInfo;
import com.mstar.tv.tvplayer.ui.dtv.epg.atsc.AtscEPGActivity;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;

public class RecordActivity extends MstarBaseActivity {
    private static final String TAG = "RecordActivity";

    private int mEventBaseTime = 0;

    private int mFocusIndex = 0;

    private int mEventChannelNum = 0;

    private int mServiceType = TvChannelManager.SERVICE_TYPE_DTV;

    private int mTvSystem = 0;

    // /EPG timer event type none
    private final int EPG_EVENT_NONE = 0;

    // /EPG timer event type remider
    private final int EPG_EVENT_REMIDER = 1;

    // /EPG timer event type recorder
    private final int EPG_EVENT_RECORDER = 2;

    // /EPG timer repeat mode auto
    // private final int EPG_REPEAT_AUTO = 0;
    // /EPG timer repeat mode once
    private final int EPG_REPEAT_ONCE = 0x81;

    // /EPG timer repeat mode daily
    private final int EPG_REPEAT_DAILY = 0x7F;

    // /EPG timer repeat mode weekly
    private final int EPG_REPEAT_WEEKLY = 0x82;

    /* for Record by Event */
    private final int EPG_EVENT_RECORDER_EVENT_ID = 0x83;

    private static EpgEventTimerInfo mEventTimerInfo = new EpgEventTimerInfo();

    private static int userProgramIndex = 0;

    private TextView mTextChannelValue = null;

    private TextView mTextStartMinuteValue = null;

    private TextView mTextStartHourValue = null;

    private TextView mTextStartMonthValue = null;

    private TextView mTextStartDateValue = null;

    private TextView mTextEndMinuteValue = null;

    private TextView mTextEndHourValue = null;

    private TextView mTextEndMonthValue = null;

    private TextView mTextEndDateValue = null;

    private TextView mTextModeValue = null;

    private TvTimerManager mTvTimerManager = null;

    private TvEpgManager mTvEpgManager = null;

    private TvChannelManager mTvChannelManager = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private String mStrMinute = null;

    private String mStrHour = null;

    private String mStrMonth = null;

    private String mStrDate = null;

    private final static int TIME_GAP_FROM_NOW_SEC = 60;

    private final static int TIME_BASE_DEFAULT_DURATION_SEC = 600;

    private Time mStartTime = new Time();

    private Time mEndTime = new Time();

    private boolean mIsEventBasedRecording = false;

    private AlertDialog mOverwriteTimerEventDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        initItems();

        ProgramInfo targetProgInfo = new ProgramInfo();

        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            mEventBaseTime = getIntent().getIntExtra("EventBaseTime", 0);
            mFocusIndex = getIntent().getIntExtra("FocusIndex", 0);
            mEventChannelNum = getIntent().getIntExtra("ChannelNum", 0);
            mIsEventBasedRecording = getIntent().getBooleanExtra(Constant.EVENT_BASED_RECORDING, false);
            Log.d(TAG, "======>>>REC mEventBaseTime " + mEventBaseTime);
            Log.d(TAG, "======>>>REC mFocusIndex " + mFocusIndex);
            Log.d(TAG, "======>>>REC mEventChannelNum " + mEventChannelNum);
        }

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mEventBaseTime = mEventBaseTime + mTvTimerManager.getClockOffset();
        }

        int programCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        Log.i(TAG, "programCount = " + programCount);
        ProgramInfo progInfo = new ProgramInfo();
        for (int i = 0; i < programCount; i++) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                progInfo = mTvAtscChannelManager.getCurrentProgramInfo();
            } else {
                progInfo = mTvChannelManager.getProgramInfoByIndex(i);
            }
            if (progInfo.number == mEventChannelNum) {
                mServiceType = progInfo.serviceType;
                targetProgInfo = progInfo;
                userProgramIndex = i;
                break;
            }
        }
        Log.d(TAG, "targetProgInfo.number" + targetProgInfo.number);

        // show service number and name
        String serviceNumberAndNameStr = null;
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            serviceNumberAndNameStr = "CH " + targetProgInfo.majorNum + "."
                    + targetProgInfo.minorNum + " " + targetProgInfo.serviceName;
        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            serviceNumberAndNameStr = "CH " + progInfo.majorNum + "-" + progInfo.minorNum + " " + progInfo.serviceName;
        } else {
            serviceNumberAndNameStr = "CH " + mEventChannelNum + " " + targetProgInfo.serviceName;
        }
        Log.i(TAG, "serviceNumberAndNameStr = " + serviceNumberAndNameStr);
        mTextChannelValue.setText(serviceNumberAndNameStr);

        mOverwriteTimerEventDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.str_epg_timer_conflict)
                .setMessage(R.string.str_epg_timer_prompt_delete_conflict_schedule)
                .setPositiveButton(R.string.str_epg_timer_delete_conflict_confirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAllConflictEvents(mEventTimerInfo);
                                dialog.dismiss();
                                addEpgEvent();
                            }
                        })
                .setNegativeButton(R.string.str_epg_timer_delete_conflict_cancel, null)
                .create();

        initialEpgTimerInfo();
        updateUI();
    }

    /**
     * To initial the class member
     */
    private void initItems() {
        mEventTimerInfo = new EpgEventTimerInfo();
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mTvChannelManager = TvChannelManager.getInstance();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        }
        mTvTimerManager = TvTimerManager.getInstance();
        mTvEpgManager = TvEpgManager.getInstance();

        // textview
        mTextChannelValue = (TextView) findViewById(R.id.record_service_name);
        mTextStartMinuteValue = (TextView) findViewById(R.id.record_start_time_minute);
        mTextStartHourValue = (TextView) findViewById(R.id.record_start_time_hour);
        mTextStartMonthValue = (TextView) findViewById(R.id.record_start_time_month);
        mTextStartDateValue = (TextView) findViewById(R.id.record_start_time_date);
        mTextEndMinuteValue = (TextView) findViewById(R.id.record_end_time_minute);
        mTextEndHourValue = (TextView) findViewById(R.id.record_end_time_hour);
        mTextEndMonthValue = (TextView) findViewById(R.id.record_end_time_month);
        mTextEndDateValue = (TextView) findViewById(R.id.record_end_time_date);
        mTextModeValue = (TextView) findViewById(R.id.reminder_mode_value);

        mStrMinute = getString(R.string.str_record_minute);
        mStrHour = getString(R.string.str_record_hour);
        mStrMonth = getString(R.string.str_record_month);
        mStrDate = getString(R.string.str_record_date);
    }

    /**
     * To initial the EpgTimerInfo for later set to the timer.
     */
    private void initialEpgTimerInfo() {
        EpgEventInfo curEpgEventInfo = new EpgEventInfo();
        Time baseTime = new Time();
        // get event info
        baseTime.set((mEventBaseTime * 1000L + 1));
        List<EpgEventInfo> eventInfoList = new ArrayList<EpgEventInfo>();
        eventInfoList.clear();
        eventInfoList = mTvEpgManager.getEventInfo((short) mServiceType, mEventChannelNum,
                baseTime, 1);
        int eventStartTimeInS = 0;
        int eventDurationInS = 0;

        // Reset mIsEventBasedRecording and recheck if it's from a event
        // If it's from a event, get the event info
        if (true == mIsEventBasedRecording) {
            mIsEventBasedRecording = false;
            if (null != eventInfoList) {
                curEpgEventInfo = eventInfoList.get(0);
                if (null != curEpgEventInfo) {
                    eventStartTimeInS = curEpgEventInfo.originalStartTime;
                    eventDurationInS = curEpgEventInfo.durationTime;
                    mIsEventBasedRecording = true;
                }
            }
        }

        // Initial EventTimerInfo for Recording
        mEventTimerInfo.enTimerType = EPG_EVENT_RECORDER;
        mEventTimerInfo.serviceType = mServiceType;
        mEventTimerInfo.serviceNumber = mEventChannelNum;

        // Initial left part of EventTimerInfo for two cases: Event-Based or Time-Based
        if (true == mIsEventBasedRecording) {
            // Event-Based Recording Timer
            Log.d(TAG, "To Event Based Recording");
            Time epgEventTime = new Time();
            epgEventTime.set((long) curEpgEventInfo.startTime * 1000);

            mEventTimerInfo.enRepeatMode = EPG_EVENT_RECORDER_EVENT_ID;
            mEventTimerInfo.eventID = curEpgEventInfo.eventId;
            mEventTimerInfo.startTime = curEpgEventInfo.startTime - mTvEpgManager.getEpgEventOffsetTime(epgEventTime, true);
            mEventTimerInfo.durationTime = curEpgEventInfo.durationTime;
        } else {
            // Time-Based Recording Timer
            /* record time should later current time */
            Log.d(TAG, "To Time Based Recording");
            baseTime = mTvTimerManager.getCurrentTvTime();
            baseTime.second = 0;
            final int curTimeGapInS = (int) (baseTime.toMillis(true) / 1000) + TIME_GAP_FROM_NOW_SEC;

            eventStartTimeInS = mEventBaseTime;
            eventDurationInS = TIME_BASE_DEFAULT_DURATION_SEC;
            mEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
            mEventTimerInfo.eventID = 0;
            final int eventEndTimeInS = eventStartTimeInS + eventDurationInS;
            if ((curTimeGapInS > eventStartTimeInS) && (curTimeGapInS < eventEndTimeInS)) {
                eventDurationInS = eventEndTimeInS - curTimeGapInS;
                eventStartTimeInS = curTimeGapInS;
            }

            mEventTimerInfo.startTime = eventStartTimeInS;
            mEventTimerInfo.durationTime = eventDurationInS;
            Log.d(TAG, "======>>>mEventTimerInfo.durationTime sec " + mEventTimerInfo.durationTime);

            // show event start time
            mStartTime.set(eventStartTimeInS * 1000L);
            mEndTime.set(eventEndTimeInS * 1000L);
        }
    }

    /**
     * To show/hide components according to the purpose: Event-based or Time-based Recording.
     */
    private void updateUI() {
        if (true == mIsEventBasedRecording) {
            ((TextView)findViewById(R.id.record_title_start_time)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.record_title_end_time)).setVisibility(View.INVISIBLE);
            ((LinearLayout) findViewById(R.id.reminder_mode_value_layout)).setVisibility(View.INVISIBLE);
            // Use the background without arrows
            mTextChannelValue.setBackgroundResource(R.drawable.select_item);
            mTextChannelValue.setText(R.string.str_reminder_record_event);
            mTextStartMinuteValue.setVisibility(View.INVISIBLE);
            mTextStartHourValue.setVisibility(View.INVISIBLE);
            mTextStartMonthValue.setVisibility(View.INVISIBLE);
            mTextStartDateValue.setVisibility(View.INVISIBLE);
            mTextEndMinuteValue.setVisibility(View.INVISIBLE);
            mTextEndHourValue.setVisibility(View.INVISIBLE);
            mTextEndMonthValue.setVisibility(View.INVISIBLE);
            mTextEndDateValue.setVisibility(View.INVISIBLE);
        } else {
            OnClickListener clickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    addEpgEvent();
                }
            };

            mTextChannelValue.setOnClickListener(clickListener);
            mTextStartMinuteValue.setOnClickListener(clickListener);
            mTextStartHourValue.setOnClickListener(clickListener);
            mTextEndMinuteValue.setOnClickListener(clickListener);
            mTextEndHourValue.setOnClickListener(clickListener);
            mTextModeValue.setOnClickListener(clickListener);

            // show repeat mode
            switch (mEventTimerInfo.enRepeatMode) {
                case EPG_REPEAT_DAILY:
                    mTextModeValue.setText(R.string.str_reminder_daily);
                    break;
                case EPG_REPEAT_WEEKLY:
                    mTextModeValue.setText(R.string.str_reminder_weekly);
                    break;
                case EPG_REPEAT_ONCE:
                default:
                    mTextModeValue.setText(R.string.str_reminder_once);
                    break;
            }

            mTextStartMinuteValue.setText(mStartTime.minute + mStrMinute);
            mTextStartHourValue.setText(mStartTime.hour + mStrHour);
            mTextStartMonthValue.setText(mStartTime.format("%m") + mStrMonth);
            mTextStartDateValue.setText(mStartTime.format("%d") + mStrDate);
            mTextEndMinuteValue.setText(mEndTime.minute + mStrMinute);
            mTextEndHourValue.setText(mEndTime.hour + mStrHour);
            mTextEndMonthValue.setText(mEndTime.format("%m") + mStrMonth);
            mTextEndDateValue.setText(mEndTime.format("%d") + mStrDate);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = null;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (mTextChannelValue.isFocused()) {
                    // If it's Event-based recording, channel is unchangable
                    if (false == mIsEventBasedRecording) {
                        changeRecordProgramNumber(true);
                    }
                } else if (mTextStartMinuteValue.isFocused()) {
                    changeRecordStartMin(true);
                } else if (mTextStartHourValue.isFocused()) {
                    changeRecordStartHour(true);
                } else if (mTextStartMonthValue.isFocused()) {
                    changeRecordStartMonth(true);
                } else if (mTextStartDateValue.isFocused()) {
                    changeRecordStartDate(true);
                } else if (mTextEndMinuteValue.isFocused()) {
                    changeRecordEndMin(true);
                } else if (mTextEndHourValue.isFocused()) {
                    changeRecordEndHour(true);
                } else if (mTextEndMonthValue.isFocused()) {
                    changeRecordEndMonth(true);
                } else if (mTextEndDateValue.isFocused()) {
                    changeRecordEndDate(true);
                } else if (((LinearLayout) findViewById(R.id.reminder_mode_value_layout)).isFocused()) {
                    changeRecordMode(true);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (mTextChannelValue.isFocused()) {
                    // If it's Event-based recording, channel is unchangable
                    if (false == mIsEventBasedRecording) {
                        changeRecordProgramNumber(false);
                    }
                } else if (mTextStartMinuteValue.isFocused()) {
                    changeRecordStartMin(false);
                } else if (mTextStartHourValue.isFocused()) {
                    changeRecordStartHour(false);
                } else if (mTextStartMonthValue.isFocused()) {
                    changeRecordStartMonth(false);
                } else if (mTextStartDateValue.isFocused()) {
                    changeRecordStartDate(false);
                } else if (mTextEndMinuteValue.isFocused()) {
                    changeRecordEndMin(false);
                } else if (mTextEndHourValue.isFocused()) {
                    changeRecordEndHour(false);
                } else if (mTextEndMonthValue.isFocused()) {
                    changeRecordEndMonth(false);
                } else if (mTextEndDateValue.isFocused()) {
                    changeRecordEndDate(false);
                } else if (((LinearLayout) findViewById(R.id.reminder_mode_value_layout)).isFocused()) {
                    changeRecordMode(false);
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                // case KeyEvent.KEYCODE_PROG_RED:
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    intent = new Intent(this, AtscEPGActivity.class);
                    intent.setClass(RecordActivity.this, AtscEPGActivity.class);
                } else {
                    intent = new Intent(this, EPGActivity.class);
                    intent.setClass(RecordActivity.this, EPGActivity.class);
                }
                intent.putExtra("FocusIndex", mFocusIndex);
                startActivity(intent);
                finish();
                return true;
            case KeyEvent.KEYCODE_ENTER:
                Log.d(TAG, "###addEpgEvent recorder KEYCODE_ENTER\n");
                addEpgEvent();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isEventsOverlapped(EpgEventTimerInfo eventTimerInfo1, EpgEventTimerInfo eventTimerInfo2) {
        if (((eventTimerInfo1.startTime <= eventTimerInfo2.startTime)
                && (eventTimerInfo2.startTime < (eventTimerInfo1.startTime + eventTimerInfo1.durationTime)))
                || ((eventTimerInfo2.startTime <= eventTimerInfo1.startTime)
                && (eventTimerInfo1.startTime < (eventTimerInfo2.startTime + eventTimerInfo2.durationTime)))
                ) {
            return true;
        }
        return false;
    }

    private boolean isEventConflict(EpgEventTimerInfo newEventTimerInfo) {
        int epgTimerEventCount = mTvTimerManager.getEpgTimerEventCount();
        EpgEventTimerInfo epgEventTimerInfo = null;

        for (int i = 0; i < epgTimerEventCount; i++) {
            epgEventTimerInfo = mTvTimerManager.getEpgTimerEventByIndex(i);
            if (true == isEventsOverlapped(newEventTimerInfo, epgEventTimerInfo)) {
                return true;
            }
        }
        return false;
    }

    private void deleteAllConflictEvents(EpgEventTimerInfo newEventTimerInfo) {
        int epgTimerEventCount = mTvTimerManager.getEpgTimerEventCount();
        EpgEventTimerInfo epgEventTimerInfo = null;

        for (int i = 0; i < epgTimerEventCount; i++) {
            epgEventTimerInfo = mTvTimerManager.getEpgTimerEventByIndex(i);
            if (true == isEventsOverlapped(newEventTimerInfo, epgEventTimerInfo)) {
                mTvTimerManager.delEpgEvent(i);
            }
        }
    }

    private void addEpgEvent() {
        String[] dispInfo = getResources().getStringArray(
                R.array.str_arr_epg_time_check_display_info);
        Toast toast = null;
        if (true == mIsEventBasedRecording) {
            // Event-Based Recording
            if (true == isEventConflict(mEventTimerInfo)) {
                if (null != mOverwriteTimerEventDialog) {
                    if (false == mOverwriteTimerEventDialog.isShowing()) {
                        mOverwriteTimerEventDialog.show();
                        return;
                    }
                }
            }
            int ret = mTvTimerManager.addEpgNewEvent(mEventTimerInfo);
            toast = Toast.makeText(RecordActivity.this, dispInfo[ret], Toast.LENGTH_SHORT);
        } else {
            // Time-Based Recording
            Time curTime = mTvTimerManager.getCurrentTvTime();
            Log.i(TAG, "curTime = " +curTime);
            if (curTime.after(mStartTime)) {
                toast = Toast.makeText(RecordActivity.this,
                        dispInfo[TvTimerManager.EPG_TIMECHECK_PAST], Toast.LENGTH_SHORT);
            } else if (mStartTime.after(mEndTime)) {
                toast = Toast.makeText(RecordActivity.this,
                        dispInfo[TvTimerManager.EPG_TIMECHECK_ENDTIME_BEFORE_START], Toast.LENGTH_SHORT);
            } else {
                /**
                 * Calculate the EPG Record startTime by user setting
                 * toMillis : Converts this time to milliseconds.
                 * The time is in UTC milliseconds since the epoch
                 *
                 * 1.timezoneOffsetSeconds => get timezone offset from startTime
                 * (including timezone and DST)
                 * 2.localTimeSeconds => calculate to localtime seconds
                 * 3.getEpgEventOffsetTime => this function can get
                 * epgOffset(including timezone and DST) from SN
                 * getEpgEventOffsetTime(startTime - timezoneOffset(without dst))
                 * 4.addEpgNewEvent : this function should set UTC time
                 * (no including timezone and DST)
                 */
                TimeZone tz = TimeZone.getDefault();
                int timezoneOffsetSeconds = tz.getOffset(mStartTime.toMillis(true))/1000;
                int localTimeSeconds = (int) (mStartTime.toMillis(true) / 1000) + timezoneOffsetSeconds;

                Time epgEventTime = new Time();
                epgEventTime.set((long)localTimeSeconds*1000 - tz.getRawOffset());

                int epgTimeOffsetSeconds = mTvEpgManager.getEpgEventOffsetTime(epgEventTime, true);
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    mEventTimerInfo.startTime = localTimeSeconds - tz.getRawOffset()/1000;
                } else {
                    mEventTimerInfo.startTime = localTimeSeconds - epgTimeOffsetSeconds;
                }
                mEventTimerInfo.durationTime = (int) (mEndTime.toMillis(true) / 1000)
                        - (int) (mStartTime.toMillis(true) / 1000);
                int ret = mTvTimerManager.addEpgNewEvent(mEventTimerInfo);
                toast = Toast.makeText(RecordActivity.this, dispInfo[ret], Toast.LENGTH_SHORT);
            }
        }

        if (null != toast) {
            toast.show();
        }

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            Intent intent = new Intent(RecordActivity.this, AtscEPGActivity.class);
            intent.setClass(RecordActivity.this, AtscEPGActivity.class);
            intent.putExtra("FocusIndex", mFocusIndex);
            startActivity(intent);
        } else {
            Intent intent = new Intent(RecordActivity.this, EPGActivity.class);
            intent.setClass(RecordActivity.this, EPGActivity.class);
            intent.putExtra("FocusIndex", mFocusIndex);
            startActivity(intent);
        }
        finish();
    }

    private void changeRecordMode(boolean isLeftKey) {
        if (isLeftKey) {
            switch (mEventTimerInfo.enRepeatMode) {
                case EPG_REPEAT_ONCE:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_DAILY;
                    break;

                case EPG_REPEAT_DAILY:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_WEEKLY;
                    break;

                case EPG_REPEAT_WEEKLY:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;

                default:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;
            }
        } else {
            switch (mEventTimerInfo.enRepeatMode) {
                case EPG_REPEAT_ONCE:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_WEEKLY;
                    break;

                case EPG_REPEAT_WEEKLY:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_DAILY;
                    break;

                case EPG_REPEAT_DAILY:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;

                default:
                    mEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;
            }
        }

        // show repeat mode
        switch (mEventTimerInfo.enRepeatMode) {
            case EPG_REPEAT_DAILY:
                mTextModeValue.setText(R.string.str_reminder_daily);
                break;
            case EPG_REPEAT_WEEKLY:
                mTextModeValue.setText(R.string.str_reminder_weekly);
                break;
            default:
            case EPG_REPEAT_ONCE:
                mTextModeValue.setText(R.string.str_reminder_once);
                break;
        }

        mTextModeValue.invalidate();
    }

    private void changeRecordProgramNumber(boolean isLeftKey) {
        ProgramInfo progInfo = new ProgramInfo();
        String tmpStr = null;

        if (isLeftKey) {
            if (userProgramIndex > 0) {
                userProgramIndex--;
            } else {
                userProgramIndex = mTvChannelManager
                        .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV) - 1;
            }
        } else {
            if (userProgramIndex < (mTvChannelManager
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV) - 1)) {
                userProgramIndex++;
            } else {
                userProgramIndex = 0;
            }
        }

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            progInfo = mTvAtscChannelManager.getProgramInfo(userProgramIndex);
            mEventTimerInfo.majorNumber = progInfo.majorNum;
            mEventTimerInfo.minorNumber = progInfo.minorNum;
            tmpStr = "CH " + progInfo.majorNum + "-" + progInfo.minorNum + " " + progInfo.serviceName;
        } else {
            progInfo = mTvChannelManager.getProgramInfoByIndex(userProgramIndex);
            tmpStr = "CH " + progInfo.number + " " + progInfo.serviceName;
        }
        mEventTimerInfo.serviceNumber = progInfo.number;
        mTextChannelValue.setText(tmpStr);
        Log.i(TAG, "tmpStr = " + tmpStr);
        mTextChannelValue.invalidate();
    }

    private void changeRecordStartMin(boolean isLeftKey) {
        int userTime = mStartTime.minute;

        if (isLeftKey) {
            if (userTime > 0) {
                userTime--;
            } else {
                userTime = 59;
            }
        } else {
            if (userTime < 59) {
                userTime++;
            } else {
                userTime = 0;
            }
        }

        mStartTime.minute = userTime;
        mTextStartMinuteValue.setText(userTime + mStrMinute);
        mTextStartMinuteValue.invalidate();
    }

    private void changeRecordStartMonth(boolean isLeftKey) {
        int userTime = mStartTime.month;

        if (isLeftKey) {
            userTime--;
        } else {
            userTime++;
        }

        mStartTime.month = userTime;
        mStartTime.normalize(true);
        mTextStartMonthValue.setText(mStartTime.format("%m") + mStrMonth);
        mTextStartMonthValue.invalidate();
    }

    private void changeRecordStartDate(boolean isLeftKey) {
        int userTime = mStartTime.monthDay;

        if (isLeftKey) {
            userTime--;
        } else {
            userTime++;
        }

        mStartTime.monthDay = userTime;
        mStartTime.normalize(true);
        mTextStartDateValue.setText(mStartTime.format("%d") + mStrDate);
        mTextStartDateValue.invalidate();
    }

    private void changeRecordStartHour(boolean isLeftKey) {
        int userTime = mStartTime.hour;

        if (isLeftKey) {
            if (userTime > 0) {
                userTime--;
            } else {
                userTime = 23;
            }
        } else {
            if (userTime < 23) {
                userTime++;
            } else {
                userTime = 0;
            }
        }

        mStartTime.hour = userTime;
        mTextStartHourValue.setText(userTime + mStrHour);
        mTextStartHourValue.invalidate();
    }

    private void changeRecordEndMin(boolean isLeftKey) {
        int userTime = mEndTime.minute;

        if (isLeftKey) {
            if (userTime > 0) {
                userTime--;
            } else {
                userTime = 59;
            }
        } else {
            if (userTime < 59) {
                userTime++;
            } else {
                userTime = 0;
            }
        }

        mEndTime.minute = userTime;
        mTextEndMinuteValue.setText(userTime + mStrMinute);
        mTextEndMinuteValue.invalidate();
    }

    private void changeRecordEndHour(boolean isLeftKey) {
        int userTime = mEndTime.hour;

        if (isLeftKey) {
            if (userTime > 0) {
                userTime--;
            } else {
                userTime = 23;
            }
        } else {
            if (userTime < 23) {
                userTime++;
            } else {
                userTime = 0;
            }
        }

        mEndTime.hour = userTime;
        mTextEndHourValue.setText(userTime + mStrHour);
        mTextEndHourValue.invalidate();
    }

    private void changeRecordEndMonth(boolean isLeftKey) {
        int userTime = mEndTime.month;

        if (isLeftKey) {
            userTime--;
        } else {
            userTime++;
        }

        mEndTime.month = userTime;
        mEndTime.normalize(true);
        mTextEndMonthValue.setText(mEndTime.format("%m") + mStrMonth);
        mTextEndMonthValue.invalidate();
    }

    private void changeRecordEndDate(boolean isLeftKey) {
        int userTime = mEndTime.monthDay;

        if (isLeftKey) {
            userTime--;
        } else {
            userTime++;
        }

        mEndTime.monthDay = userTime;
        mEndTime.normalize(true);
        mTextEndDateValue.setText(mEndTime.format("%d") + mStrDate);
        mTextEndDateValue.invalidate();
    }

}
