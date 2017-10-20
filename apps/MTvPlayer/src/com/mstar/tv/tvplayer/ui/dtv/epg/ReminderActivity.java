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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.vo.EpgEventTimerInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.EpgEventInfo;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscEpgEventInfo;
import com.mstar.tv.tvplayer.ui.dtv.epg.atsc.AtscEPGActivity;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;

public class ReminderActivity extends MstarBaseActivity {

    private static final String TAG = "ReminderActivity";

    private int eventBaseTime = 0;

    private int eventIdx = 0;

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

    private static EpgEventTimerInfo eventTimerInfo = new EpgEventTimerInfo();

    private List<EpgEventInfo> eventInfoList = new ArrayList<EpgEventInfo>();

    private List<AtscEpgEventInfo> mEventInfoList = new ArrayList<AtscEpgEventInfo>();

    private static int userProgramIndex = 0;

    private TextView channelValue = null;

    private TextView minuteValue = null;

    private TextView hourValue = null;

    private TextView monthValue = null;

    private TextView dateValue = null;

    private TextView modeValue = null;

    private LinearLayout channelLayout = null;

    private LinearLayout minuteLayout = null;

    private LinearLayout hourLayout = null;

    private LinearLayout monthLayout = null;

    private LinearLayout dateLayout = null;

    private LinearLayout modeLayout = null;

    private int offsetTimeInS = 0;

    private TvChannelManager mTvChannelManager = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private TvTimerManager mTvTimerManager = null;

    private TvEpgManager mTvEpgManager = null;

    private Time mTime = new Time();

    private static final int INVALID_EPG_EVENT_INDEX = -1;

    private int mTotalEventCount = 0;

    private int mPrevConstructEventCount = 0;

    private Time mNextEventBaseTime = null;

    private final static short EPG_ITEM_COUNT_PER_PAGE = 9999;

    int clkOffset = 0;

    /*
     * mIndexEditingEvent indicates current in edit mode when it's a
     * positive integer or zero (e.g. valid index), otherwise it's now in new reminder mode
     */
    private int mIndexEditingEvent = INVALID_EPG_EVENT_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mTvChannelManager = TvChannelManager.getInstance();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        }
        mTvTimerManager = TvTimerManager.getInstance();
        mTvEpgManager = TvEpgManager.getInstance();
        ProgramInfo curProgInfo = new ProgramInfo();
        // TODO add reminder event
        eventTimerInfo.enTimerType = EPG_EVENT_REMIDER; // TODO, we should store it!!
        eventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE; // TODO, we should store it!!
        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            // TODO: use string constant
            eventBaseTime = getIntent().getIntExtra("EventBaseTime", 0);
            eventIdx = getIntent().getIntExtra("FocusIndex", 0);
            // Set to -1 if not exist, for following logic know it's in new reminder mode or edit mode
            mIndexEditingEvent = getIntent().getIntExtra(Constant.EPG_INDEX_OF_EDITING_EVENT, INVALID_EPG_EVENT_INDEX);
            Log.d(TAG, "Edit Event Index = " + mIndexEditingEvent);
            if (0 <= mIndexEditingEvent) {
                EpgEventTimerInfo evTimerInfo  = mTvTimerManager.getEpgTimerEventByIndex(mIndexEditingEvent);
                eventTimerInfo.enTimerType = evTimerInfo.enTimerType;
                eventTimerInfo.enRepeatMode = evTimerInfo.enRepeatMode;
            }
        }

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();
        } else {
            curProgInfo = mTvChannelManager.getCurrentProgramInfo();
        }
        int programCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        Log.i(TAG, "programCount = " + programCount);
        for (int i = 0; i < programCount; i++) {
            ProgramInfo progInfo = new ProgramInfo();
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                progInfo = mTvAtscChannelManager.getProgramInfo(i);
            } else {
                progInfo = mTvChannelManager.getProgramInfoByIndex(i);
            }
            if ((progInfo.number == curProgInfo.number)
                    && (progInfo.serviceType == curProgInfo.serviceType)) {
                userProgramIndex = i;
                break;
            }
        }
        Log.i(TAG, "userProgramIndex = " + userProgramIndex);

        channelValue = (TextView) findViewById(R.id.reminder_channel_value);
        minuteValue = (TextView) findViewById(R.id.reminder_minute_value);
        hourValue = (TextView) findViewById(R.id.reminder_hour_value);
        monthValue = (TextView) findViewById(R.id.reminder_month_value);
        dateValue = (TextView) findViewById(R.id.reminder_date_value);
        modeValue = (TextView) findViewById(R.id.reminder_mode_value);
        channelLayout = (LinearLayout) findViewById(R.id.reminder_channel_layout);
        minuteLayout = (LinearLayout) findViewById(R.id.reminder_minute_layout);
        hourLayout = (LinearLayout) findViewById(R.id.reminder_hour_layout);
        monthLayout = (LinearLayout) findViewById(R.id.reminder_month_layout);
        dateLayout = (LinearLayout) findViewById(R.id.reminder_date_layout);
        modeLayout = (LinearLayout) findViewById(R.id.reminder_mode_layout);
        channelLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);

        // show service number and name
        String serviceNumberAndNameStr = "";
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            serviceNumberAndNameStr = curProgInfo.majorNum + "-" + curProgInfo.minorNum + " " + curProgInfo.serviceName;
        } else {
            serviceNumberAndNameStr = curProgInfo.number + " " + curProgInfo.serviceName;
        }
        Log.i(TAG, "serviceNumberAndNameStr = " + serviceNumberAndNameStr);
        channelValue.setText(serviceNumberAndNameStr);

        // get event info
        Time baseTime = new Time();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            baseTime.set(((eventBaseTime - offsetTimeInS) * 1000L + 1));
            Log.i(TAG, "baseTime = " + baseTime);
            Log.i(TAG, "getCurrentChannelNumber = " + mTvAtscChannelManager.getCurrentChannelNumber());
            constructmEventInfoList();
            AtscEpgEventInfo epgEventInfo;
            epgEventInfo = mEventInfoList.get(eventIdx);
            eventTimerInfo.startTime = mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime) + mTvTimerManager.getClockOffset();
            eventTimerInfo.durationTime = epgEventInfo.durationTime;
            eventTimerInfo.majorNumber = curProgInfo.majorNum;
            eventTimerInfo.minorNumber = curProgInfo.minorNum;
        } else {
            EpgEventInfo epgEventInfo = new EpgEventInfo();
            offsetTimeInS = mTvEpgManager.getEpgEventOffsetTime(baseTime, true);
            /*Use long to prevent multiplication overflow*/
            baseTime.set(((eventBaseTime - offsetTimeInS) * 1000L + 1));
            eventInfoList.clear();
            eventInfoList = mTvEpgManager.getEventInfo(curProgInfo.serviceType,
                    curProgInfo.number, baseTime, 1);
            if (eventInfoList != null) {
                epgEventInfo = eventInfoList.get(0);
            }
            eventTimerInfo.startTime = epgEventInfo.startTime - offsetTimeInS;
            eventTimerInfo.durationTime = epgEventInfo.durationTime;
        }
        Log.i(TAG, "offsetTimeInS = " + offsetTimeInS);
        Log.i(TAG, "curProgInfo.serviceType = " + curProgInfo.serviceType);
        Log.i(TAG, "curProgInfo.number = " + curProgInfo.number);
        Log.i(TAG, "eventIdx = + " + eventIdx);
        Log.i(TAG, "curProgInfo.majorNum = " + curProgInfo.majorNum);
        Log.i(TAG, "curProgInfo.minorNum = " + curProgInfo.minorNum);
        eventTimerInfo.serviceType = curProgInfo.serviceType;
        eventTimerInfo.serviceNumber = curProgInfo.number;
        eventTimerInfo.eventID = eventIdx;

        if (0 > mIndexEditingEvent) {
            // Not a valid (zero based) index, Now in New Reminder mode
            Log.i(TAG, "(long) eventTimerInfo.startTime* 1000 = " + (long) eventTimerInfo.startTime * 1000);
            mTime.set((long) eventTimerInfo.startTime * 1000); // s->ms
        } else {
            // In Edit Mode
            mTime.set(baseTime); // s->ms
        }
        // set the second to 0 for starting event when the given minute is coming
        mTime.second = 0;

        // show event start time
        Time startTime = new Time();
        startTime.set((long) eventTimerInfo.startTime * 1000); // s->ms
        // TODO: remove unit or put unit in layout
        minuteValue.setText(mTime.format("%M") + getString(R.string.str_record_minute));
        hourValue.setText(mTime.format("%H") + getString(R.string.str_record_hour));
        monthValue.setText(mTime.format("%m") + getString(R.string.str_record_month));
        dateValue.setText(mTime.format("%d") + getString(R.string.str_record_date));

        // show repeat mode
        switch (eventTimerInfo.enRepeatMode) {
            case EPG_REPEAT_ONCE:
                modeValue.setText(R.string.str_reminder_once);
                break;

            case EPG_REPEAT_DAILY:
                modeValue.setText(R.string.str_reminder_daily);
                break;

            case EPG_REPEAT_WEEKLY:
                modeValue.setText(R.string.str_reminder_weekly);
                break;

            default:
                modeValue.setText(R.string.str_reminder_once);
                break;

        }

        OnClickListener clickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] dispInfo = getResources().getStringArray(R.array.str_arr_epg_time_check_display_info);
                if (0 <= mIndexEditingEvent) {
                    // In Edit Mode, delete odd event reminder first
                    mTvTimerManager.delEpgEvent(mIndexEditingEvent);
                }
                eventTimerInfo.startTime = (int) (mTime.toMillis(true) / 1000);
                int ret = mTvTimerManager.addEpgNewEvent(eventTimerInfo);

                try {
                    Toast toast = Toast.makeText(ReminderActivity.this,
                        dispInfo[ret], Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    Intent intent = new Intent(ReminderActivity.this, AtscEPGActivity.class);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(TvIntent.ACTION_EPG_ACTIVITY);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                finish();
            }
        };

        channelValue.setOnClickListener(clickListener);
        minuteValue.setOnClickListener(clickListener);
        hourValue.setOnClickListener(clickListener);
        modeValue.setOnClickListener(clickListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = null;
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                changeFocusDown();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                changeFocusUp();
                return true;
            case KeyEvent.KEYCODE_ENTER:
                String[] dispInfo = getResources().getStringArray(R.array.str_arr_epg_time_check_display_info);

                if (0 <= mIndexEditingEvent) {
                    // In Edit Mode, delete odd event reminder first
                    mTvTimerManager.delEpgEvent(mIndexEditingEvent);
                }
                eventTimerInfo.startTime = (int) (mTime.toMillis(true) / 1000);
                int ret = mTvTimerManager.addEpgNewEvent(eventTimerInfo);

                try {
                    Toast toast = Toast.makeText(ReminderActivity.this,
                        dispInfo[ret], Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    intent = new Intent(ReminderActivity.this, AtscEPGActivity.class);
                    intent.putExtra("FocusIndex", eventIdx);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(TvIntent.ACTION_EPG_ACTIVITY);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                finish();
                return true;

            case KeyEvent.KEYCODE_BACK:
                // case KeyEvent.KEYCODE_PROG_BLUE:
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    intent = new Intent(ReminderActivity.this, AtscEPGActivity.class);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(TvIntent.ACTION_EPG_ACTIVITY);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                finish();
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (channelValue.isFocused()) {
                    changeReminderProgramNumber(true);
                } else if (minuteValue.isFocused()) {
                    changeReminderMin(true);
                } else if (hourValue.isFocused()) {
                    changeReminderHour(true);
                } else if (monthValue.isFocused()) {
                    changeReminderMonth(true);
                } else if (dateValue.isFocused()) {
                    changeReminderDate(true);
                } else if (modeValue.isFocused()) {
                    changeReminderMode(true);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (channelValue.isFocused()) {
                    changeReminderProgramNumber(false);
                } else if (minuteValue.isFocused()) {
                    changeReminderMin(false);
                } else if (hourValue.isFocused()) {
                    changeReminderHour(false);
                } else if (monthValue.isFocused()) {
                    changeReminderMonth(false);
                } else if (dateValue.isFocused()) {
                    changeReminderDate(false);
                } else if (modeValue.isFocused()) {
                    changeReminderMode(false);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clearFocus() {
        channelValue.clearFocus();
        minuteValue.clearFocus();
        hourValue.clearFocus();
        monthValue.clearFocus();
        dateValue.clearFocus();
        modeValue.clearFocus();
        channelLayout.setBackgroundResource(R.drawable.alpha_img);
        minuteLayout.setBackgroundResource(R.drawable.alpha_img);
        hourLayout.setBackgroundResource(R.drawable.alpha_img);
        monthLayout.setBackgroundResource(R.drawable.alpha_img);
        dateLayout.setBackgroundResource(R.drawable.alpha_img);
        modeLayout.setBackgroundResource(R.drawable.alpha_img);
    }

    public void changeFocusUp() {
        if (channelValue.isFocused()) {
            clearFocus();
            modeValue.requestFocus();
            modeLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);

        } else if (minuteValue.isFocused()) {
            clearFocus();
            channelValue.requestFocus();
            channelLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);
        } else if (hourValue.isFocused()) {
            clearFocus();
            minuteValue.requestFocus();
            minuteLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);
        } else if (monthValue.isFocused()) {
            clearFocus();
            hourValue.requestFocus();
            hourLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);
        } else if (dateValue.isFocused()) {
            clearFocus();
            monthValue.requestFocus();
            monthLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);
        } else if (modeValue.isFocused()) {
            clearFocus();
            dateValue.requestFocus();
            dateLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);

        }
    }

    public void changeFocusDown() {
        if (channelValue.isFocused()) {
            clearFocus();
            minuteValue.requestFocus();
            minuteLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);

        }

        else if (minuteValue.isFocused()) {
            clearFocus();
            hourValue.requestFocus();
            hourLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);
        }

        else if (hourValue.isFocused()) {
            clearFocus();
            monthValue.requestFocus();
            monthLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);

        } else if (monthValue.isFocused()) {
            clearFocus();
            dateValue.requestFocus();
            dateLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);

        } else if (dateValue.isFocused()) {
            clearFocus();
            modeValue.requestFocus();
            modeLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);
        }

        else if (modeValue.isFocused()) {
            clearFocus();
            channelValue.requestFocus();
            channelLayout.setBackgroundResource(R.drawable.programme_epg_img_focus);

        }

    }

    public void changeReminderMode(boolean isLeftKey) {

        if (isLeftKey) {
            switch (eventTimerInfo.enRepeatMode) {
                case EPG_REPEAT_ONCE:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_DAILY;
                    break;

                case EPG_REPEAT_DAILY:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_WEEKLY;
                    break;

                case EPG_REPEAT_WEEKLY:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;

                default:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;
            }
        } else {
            switch (eventTimerInfo.enRepeatMode) {
                case EPG_REPEAT_ONCE:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_WEEKLY;
                    break;
                case EPG_REPEAT_WEEKLY:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_DAILY;
                    break;
                case EPG_REPEAT_DAILY:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;
                default:
                    eventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
                    break;
            }
        }

        // show repeat mode
        switch (eventTimerInfo.enRepeatMode) {
            case EPG_REPEAT_ONCE:
                modeValue.setText(R.string.str_reminder_once);
                break;
            case EPG_REPEAT_DAILY:
                modeValue.setText(R.string.str_reminder_daily);
                break;
            case EPG_REPEAT_WEEKLY:
                modeValue.setText(R.string.str_reminder_weekly);
                break;
            default:
                modeValue.setText(R.string.str_reminder_once);
                break;
        }
        modeValue.invalidate();
    }

    public void changeReminderProgramNumber(boolean isLeftKey) {
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
            eventTimerInfo.majorNumber = progInfo.majorNum;
            eventTimerInfo.minorNumber = progInfo.minorNum;
            tmpStr = progInfo.majorNum + "-" + progInfo.minorNum + " " + progInfo.serviceName;
        } else {
            progInfo = mTvChannelManager.getProgramInfoByIndex(userProgramIndex);
            tmpStr = progInfo.number + " " + progInfo.serviceName;
        }
        eventTimerInfo.serviceNumber = progInfo.number;
        channelValue.setText(tmpStr);
        channelValue.invalidate();
    }

    public void changeReminderMin(boolean bDecrease) {
        if (true == bDecrease) {
            mTime.minute -= 1;
        } else {
            mTime.minute += 1;
        }

        mTime.normalize(true);
        // TODO: remove unit or put unit in layout
        minuteValue.setText(mTime.format("%M") + getString(R.string.str_record_minute));
        minuteValue.invalidate();
    }

    public void changeReminderHour(boolean bDecrease) {
        if (true == bDecrease) {
            mTime.hour -= 1;
        } else {
            mTime.hour += 1;
        }

        mTime.normalize(true);
        // TODO: remove unit or put unit in layout
        hourValue.setText(mTime.format("%H") + getString(R.string.str_record_hour));
        hourValue.invalidate();
    }

    public void changeReminderMonth(boolean bDecrease) {
        if (true == bDecrease) {
            mTime.month -= 1;
        } else {
            mTime.month += 1;
        }

        mTime.normalize(true);
        // TODO: remove unit or put unit in layout
        monthValue.setText(mTime.format("%m") + getString(R.string.str_record_month));
        monthValue.invalidate();
    }

    public void changeReminderDate(boolean bDecrease) {
        if (true == bDecrease) {
            mTime.monthDay -= 1;
        } else {
            mTime.monthDay += 1;
        }

        mTime.normalize(true);
        // TODO: remove unit or put unit in layout
        dateValue.setText(mTime.format("%d") + getString(R.string.str_record_date));
        dateValue.invalidate();
    }

    private boolean constructmEventInfoList() {
        Log.d(TAG, "constructmEventInfoList()");
        ProgramInfo constructProgInfo = new ProgramInfo();
        AtscEpgEventInfo epgEventInfo = new AtscEpgEventInfo();
        List<AtscEpgEventInfo> mEventInfoListTemp = new ArrayList<AtscEpgEventInfo>();

        if ((mPrevConstructEventCount >= mTotalEventCount) && (mTotalEventCount > 0)) {
            // construct finish
            return false;
        }
        constructProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();

        // initial
        if (mNextEventBaseTime == null) {
            mNextEventBaseTime = mTvTimerManager.getCurrentTvTime();

            mEventInfoList.clear();

            Log.d(TAG, "mNextEventBaseTime :" + mNextEventBaseTime);
            mTotalEventCount = mTvEpgManager.getAtscEventCount(constructProgInfo.majorNum, constructProgInfo.minorNum,
                (int)constructProgInfo.serviceType, constructProgInfo.progId, mNextEventBaseTime);

            mPrevConstructEventCount = 0;
        } else if (mTotalEventCount == 0) {
            mTotalEventCount = mTvEpgManager.getAtscEventCount(constructProgInfo.majorNum, constructProgInfo.minorNum,
                (int)constructProgInfo.serviceType, constructProgInfo.progId, mNextEventBaseTime);
            mPrevConstructEventCount = 0;
        }
        if (mTotalEventCount == 0) {
            return false;
        }

        mEventInfoListTemp.clear();
        mEventInfoListTemp = getEpgEvents();
        /* Clock offset = Timezone + DST */
        clkOffset = mTvTimerManager.getClockOffset();
        GregorianCalendar calendar = new GregorianCalendar(0, 0, 0, 0, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        int tmpTime;
        boolean isDST = TvTimerManager.getInstance().getDaylightSavingState();
        if (mEventInfoListTemp != null) {
            for (int i = 0; i < mEventInfoListTemp.size(); i++) {
                mEventInfoList.add(mEventInfoListTemp.get(i));

                // push event start time -> end time & event name in list
                String[] eventNameStr = {
                    null
                };
                String[] mTimeInfoStr = {
                    null
                };

                epgEventInfo = mEventInfoListTemp.get(i);
                eventNameStr[0] = epgEventInfo.sName;

                Time startTime = new Time();
                startTime.set(mTvTimerManager.convertGPSTime2UTC(epgEventInfo.startTime) + clkOffset);
                Time endTime = new Time();
                endTime.set(mTvTimerManager.convertGPSTime2UTC(epgEventInfo.endTime) + clkOffset);
                mTimeInfoStr[0] = startTime.hour + ":" + startTime.minute + "-" + endTime.hour + ":"
                        + endTime.minute;
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
                // currTime.set(currTime.toMillis(true) + (long) offset * 1000);
                Log.e(TAG, "getFirstEventInformation, currTime: " + currTime);
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
}
