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

package com.mstar.tv.tvplayer.ui.dtv.epg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEitInfo;
import com.mstar.android.tvapi.dtv.vo.EpgEventInfo;
import com.mstar.android.tvapi.dtv.vo.EpgHdSimulcast;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.tv.tvplayer.ui.holder.EPGViewHolder;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TimeOutHelper;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.util.Constant;
import com.mstar.util.Utility;

import com.mstar.android.MKeyEvent;

public class EPGActivity extends MstarBaseActivity {
    private static final String TAG = "EPGActivity";

    private final static short EPG_UPDATE_LIST = 0x01;

    private final static short EPG_ITEM_COUNT_PER_PAGE = 14;

    private final static int TIME_OUT_MSG = 51;

    private final static int REFRESH_TIME_MS = 1000;

    private TvIsdbChannelManager mTvIsdbChannelManager = null;

    private String[] mParentalContentLockList;

    private String[] mGenreString;

    private int mTvSystem = 0;

    private ArrayList<EPGViewHolder> epgEventViewHolderList = new ArrayList<EPGViewHolder>();

    private ArrayList<EPGViewHolder> epgProgramViewHolderList = new ArrayList<EPGViewHolder>();

    private List<EpgEventInfo> eventInfoList = new ArrayList<EpgEventInfo>();

    private boolean isEpgChannel = false;

    private Intent intent = null;

    private ListView epgListView;

    private EPGAdapter eventAdapter;

    private EPGAdapter programAdapter;

    private TextView comDateNameTextView = null;

    private TextView mTimeinfo = null;

    private TextView context = null;

    private TextView mAge = null;

    private TextView mGenre = null;

    private TextView mContent = null;

    private TextView title = null;

    private LinearLayout info_lay = null;

    private LinearLayout mContentLinearLayout = null;

    private static int userProgramIndex = 0;

    private static int userEventIndex = 0;

    private boolean infodisplay = false;

    private Time nextEventBaseTime = null;

    private Time nextProgramBaseTime = null;

    private int totalEventCount = 0;

    private int totalProgramCount = 0;

    private int prevConstructEventCount = 0;

    private int prevConstructProgramCount = 0;

    private int mCountry = TvCountry.OTHERS;

    private Thread getEpgDataThread = null;

    private static boolean enableEpgDataThread = false;

    private TimeOutHelper timeOutHelper;

    private TvEpgManager mTvEpgManager = null;

    private TvChannelManager mTvChannelManager = null;

    private TvTimerManager mTvTimerManager = null;

    private ProgramInfo mCurProgInfo = null;

    private boolean mIsPvrEnable = false;

    private ImageView mDot1 = null;

    private ImageView mDot2 = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.programme_epg);
        epgListView = (ListView) findViewById(R.id.programme_epg_list_view);

        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            int focusIdx = getIntent().getIntExtra("FocusIndex", 0);
        }

        eventAdapter = new EPGAdapter(this, epgEventViewHolderList);
        programAdapter = new EPGAdapter(this, epgProgramViewHolderList);
        if (isEpgChannel == true) {
            epgListView.setAdapter(eventAdapter);
        } else {
            epgListView.setAdapter(programAdapter);
        }
        epgListView.setDividerHeight(0);
        comDateNameTextView = (TextView) findViewById(R.id.programme_epg_context);
        title = (TextView) findViewById(R.id.programme_epg_title);
        mTimeinfo = (TextView) findViewById(R.id.programme_epg_info_time);
        context = (TextView) findViewById(R.id.programme_epg_info_context);
        mAge = (TextView) findViewById(R.id.textview_epg_info_age);
        mGenre = (TextView) findViewById(R.id.textview_epg_info_genre);
        mContent = (TextView) findViewById(R.id.textview_epg_info_content);
        info_lay = (LinearLayout) findViewById(R.id.programme_epg_info_layout);
        mContentLinearLayout = (LinearLayout) findViewById(R.id.programme_epg_content_layout);
        info_lay.setVisibility(View.INVISIBLE);
        mParentalContentLockList = getResources().getStringArray(R.array.contentlock_list);
        mCountry = TvChannelManager.getInstance().getSystemCountryId();
        if (TvCountry.UK == mCountry) {
            mGenreString = getResources().getStringArray(R.array.str_arr_epg_genre_uk);
        } else if (TvCountry.NEWZEALAND == mCountry) {
            mGenreString = getResources().getStringArray(R.array.str_arr_epg_genre_nz);
        } else if (TvCountry.AUSTRALIA == mCountry) {
            mGenreString = getResources().getStringArray(R.array.str_arr_epg_genre_au);
        } else {
            mGenreString = getResources().getStringArray(R.array.str_arr_epg_genre_default);
        }
        TVRootApp app = (TVRootApp) getApplication();
        mIsPvrEnable = app.isPVREnable();
        if (!mIsPvrEnable) {
            ImageView redImage = (ImageView) findViewById(R.id.programme_epg_tips_red_image);
            redImage.setVisibility(View.GONE);
            TextView redText = (TextView) findViewById(R.id.programme_epg_tips_red_text);
            redText.setVisibility(View.GONE);
        }
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mCurProgInfo = new ProgramInfo();
        mTvChannelManager = TvChannelManager.getInstance();
        mTvEpgManager = TvEpgManager.getInstance();
        mTvTimerManager = TvTimerManager.getInstance();
        resetAllNextBaseTime(true);
        mTvEpgManager.enableEpgBarkerChannel();

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            mTvIsdbChannelManager = TvIsdbChannelManager.getInstance();
        }
        mCurProgInfo = mTvChannelManager.getCurrentProgramInfo();
        int dtvProgramCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        ProgramInfo tmpProgInfo = new ProgramInfo();
        userProgramIndex = 0;

        for (int i = 0; i < dtvProgramCount; i++) {
            tmpProgInfo = mTvChannelManager.getProgramInfoByIndex(i);
            if ((mCurProgInfo.serviceType == tmpProgInfo.serviceType)
                    && (mCurProgInfo.number == tmpProgInfo.number)) {
                userProgramIndex = i;
                programAdapter.setFocusItem(userProgramIndex);
                break;
            }
        }

        Log.i(TAG, "userProgramIndex:" + userProgramIndex);

        registerListener();

        if (isEpgChannel) {
            title.setText(R.string.str_epg_title_channel);
            constructProgrameGuideListChannel(mCurProgInfo);

        } else {
            title.setText(R.string.str_epg_title_time);
            constructEventInfoList(mCurProgInfo);
            constructProgrameGuideListTime(0);
        }

        enableEpgDataThread = true;
        getEpgDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (enableEpgDataThread) {
                    try {
                        Thread.sleep(REFRESH_TIME_MS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    epgHandler.sendEmptyMessageDelayed(EPG_UPDATE_LIST, 20);
                }
            }
        });

        if (getEpgDataThread != null) {
            getEpgDataThread.start();
        }

        // time out to kill this
        timeOutHelper = new TimeOutHelper(epgHandler, this);
    }

    private Handler epgHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case EPG_UPDATE_LIST:
                    if (isEpgChannel == true) {
                        if (false == constructEventInfoList(mCurProgInfo)) {
                            /* no data updated, no need to info adapter to refresh list */
                            break;
                        }
                        eventAdapter.notifyDataSetChanged();
                    } else {
                        if (false == constructProgramInfoList(null)) {
                            /* no data updated, no need to info adapter to refresh list */
                            break;
                        }
                        programAdapter.notifyDataSetChanged();
                    }

                    epgListView.invalidate();
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
        super.onResume();
        timeOutHelper.start();
        timeOutHelper.init();
    };

    @Override
    protected void onPause() {
        super.onPause();
        timeOutHelper.stop();
    }

    @Override
    protected void onDestroy() {
        mTvEpgManager.disableEpgBarkerChannel();
        enableEpgDataThread = false;
        getEpgDataThread.run();

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        timeOutHelper.reset();
        final int selectedItemPosition = epgListView.getSelectedItemPosition();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
			case MKeyEvent.KEYCODE_EPG:
                if (info_lay.getVisibility() == View.VISIBLE) {
                    infodisplay = false;
                    context.setText("");
                    info_lay.setVisibility(View.GONE);
                } else {
                    finish();
                }
                return true;

            case KeyEvent.KEYCODE_PROG_GREEN:
                if (epgListView.isFocused()) {
                    infodisplay = !infodisplay;
                    if (!infodisplay) {
                        if (info_lay.getVisibility() == View.VISIBLE) {
                            context.setText("");
                            info_lay.setVisibility(View.GONE);
                        }
                    } else {
                        UpdateEpgProgramInfo(keyCode);
                    }
                }
                return true;

            case KeyEvent.KEYCODE_PROG_YELLOW:
                if (info_lay.getVisibility() == View.VISIBLE) {
                    context.setText("");
                    info_lay.setVisibility(View.GONE);
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
                if (info_lay.getVisibility() == View.VISIBLE) {
                    context.setText("");
                    info_lay.setVisibility(View.GONE);
                }
                Log.d(TAG, "EpgChannel:" + isEpgChannel + "Position:" + selectedItemPosition);
                if (ListView.INVALID_POSITION == selectedItemPosition) {
                    return true;
                }
                if (isEpgChannel) {
                    intent = new Intent(this, RecordActivity.class);
                    intent.setClass(EPGActivity.this, RecordActivity.class);
                    if (epgListView.getSelectedItemPosition() >= eventInfoList.size()) {
                        return true;
                    }
                    EpgEventInfo epgEventInfo = eventInfoList.get(selectedItemPosition);
                    /* use UTC for Record Activity */
                    intent.putExtra("ChannelNum", mCurProgInfo.number);
                    intent.putExtra("EventBaseTime", epgEventInfo.originalStartTime);
                    intent.putExtra("FocusIndex", selectedItemPosition);
                    intent.putExtra(Constant.EVENT_BASED_RECORDING, isEpgChannel);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(this, RecordActivity.class);
                    intent.setClass(EPGActivity.this, RecordActivity.class);
                    Log.d(TAG, "eventInfoList.size()" + eventInfoList.size());
                    int channelNum = mCurProgInfo.number;
                    if ((epgProgramViewHolderList == null)
                            || epgProgramViewHolderList.size() <= selectedItemPosition) {
                        Log.d(TAG, "Get Channel Num error");
                        return true;
                    }
                    channelNum = epgProgramViewHolderList.get(selectedItemPosition).getChannelNum();
                    intent.putExtra("ChannelNum", channelNum);
                    /* use UTC for Record Activity */
                    if (eventInfoList.size() == 0) {
                        Time curTime = mTvTimerManager.getCurrentTvTime();
                        intent.putExtra("EventBaseTime", (int) (curTime.toMillis(true) / 1000));
                    } else {
                        EpgEventInfo epgEventInfo = new EpgEventInfo();
                        epgEventInfo = eventInfoList.get(userEventIndex);
                        intent.putExtra("EventBaseTime", epgEventInfo.originalStartTime);
                    }
                    intent.putExtra("FocusIndex", selectedItemPosition);
                    intent.putExtra(Constant.EVENT_BASED_RECORDING, isEpgChannel);
                    startActivity(intent);
                    finish();
                }
                return true;

            case KeyEvent.KEYCODE_PROG_BLUE:
                if (info_lay.getVisibility() == View.VISIBLE) {
                    context.setText("");
                    info_lay.setVisibility(View.GONE);
                }

                if (ListView.INVALID_POSITION == selectedItemPosition) {
                    return true;
                }

                intent = new Intent(TvIntent.ACTION_EPG_REMINDER);
                EpgEventInfo epgEventInfo = null;

                if (true == isEpgChannel) {
                    if (0 <= selectedItemPosition && eventInfoList.size() > selectedItemPosition) {
                        epgEventInfo = eventInfoList.get(selectedItemPosition);
                    }
                } else {
                    if (eventInfoList.size() > 0) {
                        epgEventInfo = eventInfoList.get(0);
                    }
                }

                if (null != epgEventInfo) {
                    intent.putExtra("EventBaseTime", epgEventInfo.startTime);
                    intent.putExtra("FocusIndex", selectedItemPosition);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    finish();
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (info_lay.getVisibility() == View.VISIBLE) {
                    context.setText("");
                    info_lay.setVisibility(View.GONE);
                }

                if (comDateNameTextView.isFocused()) {

                    if (isEpgChannel) {
                        changeProgramNumber(true);
                    } else {
                        changeEventStartTime(true);
                    }
                } else {
                    changeEPGListProgramList();
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (info_lay.getVisibility() == View.VISIBLE) {
                    context.setText("");
                    info_lay.setVisibility(View.GONE);
                }

                if (comDateNameTextView.isFocused()) {
                    if (isEpgChannel) {
                        changeProgramNumber(false);
                    } else {
                        changeEventStartTime(false);
                    }

                } else {
                    changeEPGListProgramList();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeEPGListProgramList() {
        if (mDot1 == null) {
            mDot1 = (ImageView) findViewById(R.id.programme_epg_bg_dot_1);
        }
        if (mDot2 == null) {
            mDot2 = (ImageView) findViewById(R.id.programme_epg_bg_dot_2);
        }

        isEpgChannel = !isEpgChannel;
        if (isEpgChannel == true) {
            epgListView.setAdapter(eventAdapter);
        } else {
            epgListView.setAdapter(programAdapter);
        }
        resetAllNextBaseTime(false);
        if (isEpgChannel) {
            mCurProgInfo = mTvChannelManager.getCurrentProgramInfo();
            title.setText(R.string.str_epg_title_channel);
            constructProgrameGuideListChannel(mCurProgInfo);
            if ((mDot1 != null) && (mDot2 != null)) {
                mDot1.setImageResource(R.drawable.common_img_pagepoint_enable);
                mDot2.setImageResource(R.drawable.common_img_pagepoint_disable);
            }
        } else {
            title.setText(R.string.str_epg_title_time);
            constructProgrameGuideListTime(0);
            if ((mDot1 != null) && (mDot2 != null)) {
                mDot1.setImageResource(R.drawable.common_img_pagepoint_disable);
                mDot2.setImageResource(R.drawable.common_img_pagepoint_enable);
            }
        }
    }

    private void registerListener() {
        epgListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                timeOutHelper.reset();

                if (isEpgChannel == false) {
                    // go to the selected channel
                    final int selectedItemPosition = epgListView.getSelectedItemPosition();
                    ProgramInfo curProgInfo = new ProgramInfo();
                    if (ListView.INVALID_POSITION == selectedItemPosition) {
                        return;
                    }
                    curProgInfo = mTvChannelManager.getProgramInfoByIndex(selectedItemPosition);
                    mTvChannelManager.selectProgram(curProgInfo.number, curProgInfo.serviceType);
                    resetAllNextBaseTime(true);
                }
            }
        });

        epgListView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (infodisplay) {
                        UpdateEpgProgramInfo(KeyEvent.KEYCODE_PROG_GREEN);
                    }
                } else {
                    if (info_lay.getVisibility() == View.VISIBLE) {
                        context.setText("");
                        info_lay.setVisibility(View.GONE);
                    }
                }
            }
        });
        epgListView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                timeOutHelper.reset();
				int curFocusIdx = epgListView.getSelectedItemPosition();
				
				//wxl add for switch channel 2016/3/14
				if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if (ListView.INVALID_POSITION != curFocusIdx) {
						ProgramInfo curProgInfo = mTvChannelManager.getProgramInfoByIndex(curFocusIdx);
	                    mTvChannelManager.selectProgram(curProgInfo.number, curProgInfo.serviceType);
	                    resetAllNextBaseTime(true);
                    }
                    //begin  wangxielin  20160326
					//int lastFocusIdx = programAdapter.getLastFocusItem();
					//programAdapter.setNoFocusItem(lastFocusIdx);
					programAdapter.setFocusItem(curFocusIdx);
                    programAdapter.notifyDataSetChanged();
					epgListView.invalidate();
					//end
                    return true;
				}
				
                if (infodisplay) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_UP:
                            case KeyEvent.KEYCODE_DPAD_DOWN:
                                if (ListView.INVALID_POSITION != curFocusIdx) {
                                    UpdateEpgProgramInfo(keyCode);
                                }
                                return false;
                            case KeyEvent.KEYCODE_PAGE_UP:
                                Utility.scrollPosition(context, Utility.SCROLL_DIRECTION_UP);
                                return true;
                            case KeyEvent.KEYCODE_PAGE_DOWN:
                                Utility.scrollPosition(context, Utility.SCROLL_DIRECTION_DOWN);
                                return true;
                            default:
                                return false;
                         }
                    }
                }
                return false;
            }
        });
        title.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                timeOutHelper.reset();
                TextView title = (TextView) view;
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    changeEPGListProgramList();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    changeEPGListProgramList();
                    return true;
                }
                return false;
            }
        });

        title.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (info_lay.getVisibility() == View.VISIBLE) {
                        context.setText("");
                        info_lay.setVisibility(View.GONE);
                    }
                } else {
                    if (epgEventViewHolderList.isEmpty()) {
                        if (info_lay.getVisibility() == View.VISIBLE) {
                            context.setText("");
                            info_lay.setVisibility(View.GONE);
                        }
                    } else {
                        context.setText(epgEventViewHolderList.get(0).getInfo());
                    }
                }
            }
        });
    }

    private void resetAllNextBaseTime(boolean initAll) {
        if (initAll == true) {
            nextEventBaseTime = null;
            prevConstructEventCount = 0;
            totalEventCount = 0;
            nextProgramBaseTime = null;
            prevConstructProgramCount = 0;
            totalProgramCount = 0;
        } else {
            if (isEpgChannel == true) {
                nextEventBaseTime = null;
                prevConstructEventCount = 0;
                totalEventCount = 0;
            } else {
                nextProgramBaseTime = null;
                prevConstructProgramCount = 0;
                totalProgramCount = 0;
            }
        }
    }

    private boolean constructEventInfoList(ProgramInfo curProgInfo) {
        EpgEventInfo epgEventInfo = new EpgEventInfo();
        List<EpgEventInfo> eventInfoListTemp = null;

        if ((prevConstructEventCount >= totalEventCount) && (totalEventCount > 0)) {
            return false;
        }
        Log.v(TAG, "\33[1;36m constructEventInfoList CH :" + curProgInfo.number + "\33[0;0m");

        // initial
        if (nextEventBaseTime == null) {
            nextEventBaseTime = mTvTimerManager.getCurrentTvTime();

            calculateEventBaseTime();
            epgEventViewHolderList.clear();
            eventInfoList.clear();

            totalEventCount = mTvEpgManager.getDvbEventCount(curProgInfo.serviceType,
                    curProgInfo.number, nextEventBaseTime.toMillis(false));
            prevConstructEventCount = 0;
        } else if (totalEventCount == 0) {
            calculateEventBaseTime();
            totalEventCount = mTvEpgManager.getDvbEventCount(curProgInfo.serviceType,
                    curProgInfo.number, nextEventBaseTime.toMillis(false));
            prevConstructEventCount = 0;
        }
        if (totalEventCount == 0) {
            return false;
        }

        eventInfoListTemp = mTvEpgManager.getEventInfo(curProgInfo.serviceType, curProgInfo.number,
                nextEventBaseTime, totalEventCount);

        Log.d(TAG, "nextEventBaseTime = " + nextEventBaseTime.format("%Y/%m/%d %H:%M"));
        Log.d(TAG, "totalEventCount = " + totalEventCount);

        if (eventInfoListTemp != null) {
            Log.d(TAG, "eventInfoListTemp.size() = " + eventInfoListTemp.size());
            for (int i = 0; i < eventInfoListTemp.size(); i++) {
                epgEventInfo = eventInfoListTemp.get(i);
                eventInfoList.add(eventInfoListTemp.get(i));

                // push event start time -> end time & event name in list
                String eventNameStr = "";
                String timeInfoStr = "";

                epgEventInfo = eventInfoListTemp.get(i);
                eventNameStr = epgEventInfo.name;

                Time startTime = new Time();
                startTime.set((long) epgEventInfo.originalStartTime * 1000);
                Time endTime = new Time();
                endTime.set((long) (epgEventInfo.originalStartTime + epgEventInfo.durationTime) * 1000);
                timeInfoStr = String.format("%02d", startTime.hour) + ":"
                        + String.format("%02d", startTime.minute) + "-"
                        + String.format("%02d", endTime.hour) + ":"
                        + String.format("%02d", endTime.minute);

                EPGViewHolder vh = new EPGViewHolder();
                vh.setChannelInfo(timeInfoStr);
                vh.setChannelName(eventNameStr);
                vh.setChannelNum(curProgInfo.number);
                epgEventViewHolderList.add(vh);

            }

            // store current last event's end time
            epgEventInfo = eventInfoListTemp.get(eventInfoListTemp.size() - 1);
        }
        nextEventBaseTime
                .set((long) (epgEventInfo.originalStartTime + epgEventInfo.durationTime) * 1000);
        Log.d(TAG, "update nextEventBaseTime = " + nextEventBaseTime.format("%Y/%m/%d %H:%M"));
        prevConstructEventCount += totalEventCount;
        return true;
    }

    private boolean constructProgramInfoList(Time baseTime) {
        int availableProgramCount = 0;
        EpgEventInfo eventInfoTemp = null;

        if ((prevConstructProgramCount >= totalProgramCount) && (totalProgramCount > 0)) {
            return false;
        }
        // initial
        if (baseTime != null) {
            if (nextProgramBaseTime == null) {
                nextProgramBaseTime = new Time();
            }
            nextProgramBaseTime.set(baseTime.toMillis(true));
            epgProgramViewHolderList.clear();
            totalProgramCount = mTvChannelManager
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            prevConstructProgramCount = 0;
        } else {
            if (nextProgramBaseTime == null) {
                nextProgramBaseTime = mTvTimerManager.getCurrentTvTime();
                nextProgramBaseTime.set(nextProgramBaseTime.toMillis(true));
                epgProgramViewHolderList.clear();
                totalProgramCount = mTvChannelManager
                        .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
                prevConstructProgramCount = 0;
            } else if (totalProgramCount == 0) {
                totalProgramCount = mTvChannelManager
                        .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
                prevConstructProgramCount = 0;
            }
        }

        if (totalProgramCount == 0) {
            return false;
        }

        for (int i = prevConstructProgramCount; i < totalProgramCount; i++) {
            ProgramInfo specificProgInfo = mTvChannelManager.getProgramInfoByIndex(i);

            if (i == (totalProgramCount - 1)) {
                prevConstructProgramCount = i + 1;
            }

            if (specificProgInfo == null) {
                continue;
            }

            if ((specificProgInfo.isDelete == true) || (specificProgInfo.isVisible == false)) {
                continue;
            }

            eventInfoTemp = TvEpgManager.getInstance().getDvbEventInfoByTime(
                    specificProgInfo.serviceType, specificProgInfo.number, nextProgramBaseTime);

            // push service name & event name in list,
            String eventNameStr = "";
            String serviceInfoStr = "";

            if ((null != eventInfoTemp)) {
                eventNameStr = eventInfoTemp.name;
            }

            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                serviceInfoStr = specificProgInfo.majorNum + "." + specificProgInfo.minorNum
                        + "  " + specificProgInfo.serviceName;
            } else {
                serviceInfoStr = specificProgInfo.number + "  " + specificProgInfo.serviceName;
            }

            /* set program data */
            EPGViewHolder vh = new EPGViewHolder();
            vh.setChannelInfo(serviceInfoStr);
            vh.setChannelName(eventNameStr);
            vh.setChannelNum(specificProgInfo.number);
            epgProgramViewHolderList.add(vh);

            availableProgramCount++;
            if (availableProgramCount >= EPG_ITEM_COUNT_PER_PAGE) {
                prevConstructProgramCount = i + 1;
                break;
            }
        }

        return true;
    }

    private boolean constructProgrameGuideListChannel(ProgramInfo curProgInfo) {
        // get event info
        Time baseTime = mTvTimerManager.getCurrentTvTime();
        baseTime.set(baseTime.toMillis(true));

        // show cur service number
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            comDateNameTextView.setText("CH " + curProgInfo.majorNum + "." + curProgInfo.minorNum);
        } else {
            comDateNameTextView.setText("CH " + curProgInfo.number);
        }
        Log.v(TAG, "constructProgrameGuideListChannel CH :" + curProgInfo.number);

        constructEventInfoList(curProgInfo);
        eventAdapter.notifyDataSetChanged();
        epgListView.invalidate();
        return true;
    }

    private boolean constructProgrameGuideListTime(long timeInSec) {
        // get st time
        Time stTime = new Time();
        if (timeInSec == 0) {
            // use default time
            stTime = mTvTimerManager.getCurrentTvTime();
        } else {
            // use specific time
            stTime.set(timeInSec * 1000);
        }

        Log.v(TAG, "constructProgrameGuideListTime(), time:" + stTime.format("%Y/%m/%d %H:%M"));
        // show date&time
        comDateNameTextView.setText(stTime.format("%Y/%m/%d %H:%M"));

        constructProgramInfoList(stTime);
        programAdapter.notifyDataSetChanged();
        epgListView.invalidate();
        return true;
    }

    public void changeProgramNumber(boolean isLeftKey) {
        ProgramInfo progInfo = new ProgramInfo();

        int tmpindex = userProgramIndex;
        while (true) {

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

            Log.i(TAG, "changeProgramNumber userProgramIndex : " + userProgramIndex);
            progInfo = mTvChannelManager.getProgramInfoByIndex(userProgramIndex);

            if (progInfo.isDelete || progInfo.isHide || progInfo.isSkip || !progInfo.isVisible) {
                if (tmpindex == userProgramIndex) {
                    break;
                } else {
                    continue;
                }

            } else {
                break;
            }
        }
        resetAllNextBaseTime(true);
        constructProgrameGuideListChannel(progInfo);
        mCurProgInfo = progInfo;
    }

    public void changeEventStartTime(boolean isLeftKey) {
        boolean initState = false;
        EpgEventInfo epgEventInfo = new EpgEventInfo();

        if (eventInfoList.size() == 0) {
            return;
        }
        if (isLeftKey) {
            if ((userEventIndex > 0) && (userEventIndex < eventInfoList.size())) {
                userEventIndex--;
            } else {
                epgEventInfo = eventInfoList.get(0);
                Time startTime = new Time();
                startTime.set((long) epgEventInfo.originalStartTime * 1000);
                Time curTime = mTvTimerManager.getCurrentTvTime();
                if (curTime.before(startTime)) {
                    initState = true;
                }
            }
        } else {
            if (userEventIndex < (eventInfoList.size() - 1)) {
                userEventIndex++;
            } else {
                return;
            }
        }

        resetAllNextBaseTime(false);
        if (true == initState) {
            constructProgrameGuideListTime(0);
        } else {
            epgEventInfo = eventInfoList.get(userEventIndex);
            Log.d(TAG, "epgEventInfo.originalStartTime = " + epgEventInfo.originalStartTime);
            constructProgrameGuideListTime(epgEventInfo.originalStartTime);
        }
    }

    private void UpdateEpgProgramInfo(int keycode) {
        if (isEpgChannel) {
            ProgramInfo curProgInfo = new ProgramInfo();
            EpgEventInfo epgEventInfo = new EpgEventInfo();
            int curFocusIdx = epgListView.getSelectedItemPosition();
            if (0xffffffff == curFocusIdx) {
                Log.e(TAG, "invalid curFocusIdx!");
                return;
            }

            if (keycode == KeyEvent.KEYCODE_DPAD_UP) {
                if (0 != curFocusIdx)
                    curFocusIdx--;
                else
                    return;
            } else if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
                curFocusIdx++;
            }
            curProgInfo = mTvChannelManager.getCurrentProgramInfo();
            if (curFocusIdx >= eventInfoList.size()) {
                Log.w(TAG, "curFocusIdx is larger than list size");
                return;
            }
            epgEventInfo = eventInfoList.get(curFocusIdx);
            // show event start time and end time
            Time startTime = new Time();
            startTime.set((long) epgEventInfo.originalStartTime * 1000);

            Time endTime = new Time();
            endTime.set((long) (epgEventInfo.originalStartTime + epgEventInfo.durationTime) * 1000);

            mTimeinfo.setText(startTime.format("%Y/%m/%d %H:%M") + "-" + endTime.format("%H:%M"));

            String castInfoStr = getSimulcastInfo(curProgInfo.serviceType, curProgInfo.number,
                    startTime);
            // show event description
            String eventDescription = mTvEpgManager.getEventDescriptor(curProgInfo.serviceType,
                    curProgInfo.number, startTime, TvEpgManager.EPG_DETAIL_DESCRIPTION);
            Log.d(TAG, "UpdateEpgProgramInfo(C castInfoStr): " + castInfoStr);
            Log.d(TAG, "UpdateEpgProgramInfo(C eventDescription): " + eventDescription);
			if(castInfoStr.equals("") && eventDescription==null)
            	context.setText("");
			else
            	context.setText(castInfoStr + eventDescription);
            mGenre.setText(getGenreString(epgEventInfo.genre));
            mAge.setText(Utility.getParentalGuideAgeString(epgEventInfo.parentalRating, mCountry));
            info_lay.setVisibility(View.VISIBLE);
            //mContentLinearLayout.setVisibility(View.GONE);
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                ProgramInfo curISDBProgInfo = null;
                DtvEitInfo dtvEitInfo = null;
                curISDBProgInfo = mTvIsdbChannelManager.getCurrentProgramInfo();
                if (null != curISDBProgInfo) {
                    PresentFollowingEventInfo pfEvtInfo = new PresentFollowingEventInfo();
                    try {
                        pfEvtInfo = mTvEpgManager.getEpgPresentFollowingEventInfo(curISDBProgInfo.serviceType,
                                curISDBProgInfo.number, true, TvEpgManager.EPG_DETAIL_DESCRIPTION);
                        if (null != pfEvtInfo) {
                            dtvEitInfo = mTvEpgManager.getEitInfo(true);
                        } else {
                            dtvEitInfo = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (null != dtvEitInfo) {
                        if ((dtvEitInfo.eitCurrentEventPf.parentalObjectiveContent >= 0) && (dtvEitInfo.eitCurrentEventPf.parentalObjectiveContent <= mParentalContentLockList.length)) {
                            mContent.setText(mParentalContentLockList[dtvEitInfo.eitCurrentEventPf.parentalObjectiveContent]);
                            mContentLinearLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } else {
            ProgramInfo curProgInfo = new ProgramInfo();
            EpgEventInfo epgEventInfo = new EpgEventInfo();
            int curFocusIdx = 0;

            curProgInfo = mTvChannelManager.getCurrentProgramInfo();
            if (curFocusIdx >= eventInfoList.size()) {
                Log.w(TAG, "curFocusIdx is larger than list size");
                return;
            }
            epgEventInfo = eventInfoList.get(curFocusIdx);

            // show event start time and end time
            Time startTime = new Time();
            startTime.set((long) epgEventInfo.originalStartTime * 1000);
            Time endTime = new Time();
            endTime.set((long) (epgEventInfo.originalStartTime + epgEventInfo.durationTime) * 1000);

            mTimeinfo.setText(startTime.format("%Y/%m/%d %H:%M") + "-" + endTime.format("%H:%M"));

            String castInfoStr = getSimulcastInfo(curProgInfo.serviceType, curProgInfo.number,
                    startTime);

            String eventDescription = mTvEpgManager.getEventDescriptor(curProgInfo.serviceType,
                    curProgInfo.number, startTime, TvEpgManager.EPG_SHORT_DESCRIPTION);
            Log.d(TAG, "UpdateEpgProgramInfo(T castInfoStr): " + castInfoStr);
            Log.d(TAG, "UpdateEpgProgramInfo(T eventDescription): " + eventDescription);
            context.setText(castInfoStr + eventDescription);

            info_lay.setVisibility(View.VISIBLE);
        }
    }

    private String getSimulcastInfo(short serviceType, int serviceNumber, Time baseTime) {
        String castInfoStr = "";
        List<EpgHdSimulcast> simulcastEventList = null;
        simulcastEventList = mTvEpgManager.getEventHdSimulcast(serviceType, serviceNumber,
                baseTime, (short) 0);
        if (simulcastEventList != null) {
            int size = simulcastEventList.size();
            Log.d(TAG, "getSimulcastInfo: size = " + size);
            for (int i = 0; i < size; i++) {
                String serviceName = simulcastEventList.get(i).serviceName;
                boolean isSimulcast = simulcastEventList.get(i).isSimulcast;
                boolean isResolvable = simulcastEventList.get(i).isHdEeventResolvable;
                EpgEventInfo eventInfo = simulcastEventList.get(i).stEventInfo;
                int eventStartTime = eventInfo.startTime;
                int duration = eventInfo.durationTime;
                String eventName = eventInfo.name;
                if (serviceName.isEmpty()) {
                    continue;
                }

                if (isSimulcast) {
                    castInfoStr += String.format("%s: %s\n",
                            getResources().getString(R.string.str_epg_event_simulcast_hd),
                            serviceName);
                } else {
                    castInfoStr += String.format("%s: %s\n",
                            getResources().getString(R.string.str_epg_event_broadcastlater_hd),
                            serviceName);
                }

                if (isResolvable) {
                    Time temp = new Time();
                    temp.set((long) eventStartTime * 1000);
                    int localStartTime = eventStartTime
                            + mTvEpgManager.getEpgEventOffsetTime(temp, true);
                    Time startTime = new Time();
                    startTime.set((long) localStartTime * 1000);
                    temp.set((long) (eventStartTime + duration) * 1000);
                    int localEndTime = eventStartTime + duration
                            + mTvEpgManager.getEpgEventOffsetTime(temp, false);
                    Time endTime = new Time();
                    endTime.set((long) localEndTime * 1000);
                    castInfoStr += String.format("%02d:%02d - %02d:%02d\n%s\n", startTime.hour,
                            startTime.minute, endTime.hour, endTime.minute, eventName);
                } else {
                    castInfoStr += getResources().getString(R.string.str_epg_event_unresolved);
                    castInfoStr += "\n";
                }
            }

            if (!castInfoStr.isEmpty()) {
                castInfoStr += "\n";
                Log.d(TAG, "getSimulcastInfo: castInfoStr = " + castInfoStr);
            }
        }
        return castInfoStr;
    }

    private String getGenreString(short genreType) {
        if ((genreType < 0) || (genreType > 15)) {
            genreType = 0;
        }
        return mGenreString[genreType];
    }

    private void calculateEventBaseTime() {
        /**
         * Calculate the eventBaseTime to get event count
         * toMillis : Converts this time to milliseconds.
         * The time is in UTC milliseconds since the epoch
         *
         * 1.timezoneOffsetMilliSeconds => get timezone offset from startTime
         * (including timezone and DST)
         * 2.streamOffsetMilliSeconds => get streaming offset from SN
         */
        TimeZone tz = TimeZone.getDefault();
        int timezoneOffsetMilliSeconds = tz.getOffset(nextEventBaseTime.toMillis(true));
        int streamOffsetMilliSeconds = mTvTimerManager.getClockOffset() * 1000;
        nextEventBaseTime.set(nextEventBaseTime.toMillis(true) + timezoneOffsetMilliSeconds - streamOffsetMilliSeconds);
    }
}
