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

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.text.TextUtils;
import android.view.InputDevice;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.pvr.PVRActivity;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TimeOutHelper;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;
import com.mstar.util.Utility;

public class ChannelListActivity extends MstarBaseActivity implements
        SearchView.OnQueryTextListener {
    private static final String TAG = "ChannelListActivity";

    private final int NORMAL_CHANNEL_LIST_MENU = 0;

    private final int OPERATOR_PROFILE_MENU = 1;

    private final int OP_CHANNEL_LIST_MENU = 2;

    private int mTvSystem = 0;

    private int mProgListType = 0;

    private int mServiceNum = 0;

    TvChannelManager mTvChannelManager = null;

    private SearchView mSearchView = null;

    private ListView mProgListView;

    /* mListItem stores the unsorted data. */
    private ArrayList<ProgramFavoriteObject> mListItem = new ArrayList<ProgramFavoriteObject>();

    private ArrayList<ProgramFavoriteObject> mDisplayedListItem = null;

    private ArrayList<ProgramInfo> mProgInfoList = new ArrayList<ProgramInfo>();

    private ChannelListAdapter mAdapter = null;

    private TimeOutHelper timeOutHelper;

    private int mState = NORMAL_CHANNEL_LIST_MENU;

    private OperatorProfileListAdapter mOPListAdapter = null;

    private TextView mTextTitle = null;

    private TextView mHintOP = null;

    private TextView mHintDelOP = null;

    private short mOPCount = 0;

    private AlertDialog mDeleteOpconfirmation = null;

    private TvAtscChannelManager mTvAtscChannelManager = null;

    private TvIsdbChannelManager mTvIsdbChannelManager = null;

    private OnDtvPlayerEventListener mDtvEventListener = null;

    private AlertDialog mSortingTypeSelectionDialog = null;

    private boolean mIsSupportSearchAndSort = (Utility.isDVBS() == true) ? true:false;

    private class DtvEventListener implements OnDtvPlayerEventListener {

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
            boolean ret = false;
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                Log.i(TAG, "onTsChange what:" + what);
                getListInfo();
                if (TvCiManager.getInstance().isOpMode()) {
                    switchChannelListState(OP_CHANNEL_LIST_MENU);
                } else {
                    switchChannelListState(NORMAL_CHANNEL_LIST_MENU);
                }
                ret = true;
            }
            return ret;
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
            Log.i(TAG, "get CI+ OP event EV_CI_OP_SERVICE_LIST");
            getListInfo();
            switchChannelListState(OP_CHANNEL_LIST_MENU);
            return true;
        }

        @Override
        public boolean onUiOPExitServiceList(int what) {
            Log.i(TAG, "get CI+ OP event EV_CI_OP_EXIT_SERVICE_LIST");
            getListInfo();
            switchChannelListState(NORMAL_CHANNEL_LIST_MENU);
            return true;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TimeOutHelper.getTimeOutMsg()) {
                // finish();
            }
        }
    };

    public class ChannelListAdapter extends BaseAdapter {
        ArrayList<ProgramFavoriteObject> mData = null;

        private Context mContext;

        public ChannelListAdapter(Context context, ArrayList<ProgramFavoriteObject> data) {
            mContext = context;
            mData = data;
        }

        public void setListData(ArrayList<ProgramFavoriteObject> data) {
            mData = data;
            notifyDataSetChanged();
        }

        public ArrayList<ProgramFavoriteObject> getListData() {
            return mData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int layout = R.layout.program_favorite_list_item;
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                layout = R.layout.program_favorite_list_item_atsc;
            }
            convertView = LayoutInflater.from(mContext).inflate(layout, null);
            TextView tmpText = (TextView) convertView
                    .findViewById(R.id.program_favorite_edit_number);
            tmpText.setText(mData.get(position).getChannelId());
            tmpText = (TextView) convertView.findViewById(R.id.program_favorite_edit_data);
            tmpText.setText(mData.get(position).getChannelName());
            ImageView sourceimage = (ImageView) convertView.findViewById(R.id.program_favorite_source_img);
            ImageView skipImage = (ImageView) convertView.findViewById(R.id.program_favorite_skip_img);
            ImageView sslimage = (ImageView) convertView.findViewById(R.id.program_favorite_ssl_img);
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                short serviceType = mData.get(position).getServiceType();
                int resId;
                switch(serviceType) {
                    case TvChannelManager.SERVICE_TYPE_ATV:
                        resId = R.drawable.list_menu_img_atv_foucus;
                        break;
                    case TvChannelManager.SERVICE_TYPE_RADIO:
                        resId = R.drawable.list_menu_img_radio_foucus;
                        break;
                    case TvChannelManager.SERVICE_TYPE_DTV:
                    default:
                        resId = R.drawable.list_menu_img_dtv_foucus;
                }
                sourceimage.setImageResource(resId);
                sourceimage.setVisibility(View.VISIBLE);
            } else {
                sourceimage.setVisibility(View.GONE);
            }
            if (mData.get(position).isSkipImg()) {
                skipImage.setVisibility(View.VISIBLE);
            } else {
                skipImage.setVisibility(View.GONE);
            }
            if (mData.get(position).isSslImg()) {
                sslimage.setVisibility(View.VISIBLE);
            } else {
                sslimage.setVisibility(View.GONE);
            }

            return convertView;
        }
    }

    private class ProgramFavoriteObject {
        private String channelId = null;

        private String channelName = null;

        private short serviceType;

        private boolean skipImg = false;

        private boolean sslImg = false;

        private int frequenry = 0;

        private int indexOfProgInfoList = 0;

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public short getServiceType() {
            return serviceType;
        }

        public void setServiceType(short type) {
            this.serviceType = type;
        }

        public boolean isSkipImg() {
            return skipImg;
        }

        public void setSkipImg(boolean skipImg) {
            this.skipImg = skipImg;
        }

        public boolean isSslImg() {
            return sslImg;
        }

        public void setSslImg(boolean sslImg) {
            this.sslImg = sslImg;
        }

        public void setFrequenry(int f) {
            frequenry = f;
        }

        public int getFrequenry() {
            return frequenry;
        }

        public void setProgInfoListIdx(int idx) {
            indexOfProgInfoList = idx;
        }

        public int getProgInfoListIdx() {
            return indexOfProgInfoList;
        }
    }

    private void getListInfo() {
        Log.d(TAG, "getListInfo(), mProgListType = " + mProgListType);

        mListItem.clear();
        if (null != mDisplayedListItem) {
            mDisplayedListItem = null;
        }
        mSearchView.setQuery("", false);
        mProgInfoList.clear();
        ProgramInfo pgi = null;
        int indexBase = 0;

        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();

        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            mTvIsdbChannelManager.genMixProgList(false);
            mServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        } else {
            if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    indexBase = 0;
                } else {
                    indexBase = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
                    if (0xFFFFFFFF == indexBase) {
                        indexBase = 0;
                    }
                }
                mServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);

            } else {
                indexBase = 0;
                mServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            }
        }
        Log.d(TAG, "indexBase:" + indexBase);
        Log.d(TAG, "mServiceNum:" + mServiceNum);
        for (int k = indexBase; k < mServiceNum; k++) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                pgi = mTvAtscChannelManager.getProgramInfo(k);
            } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                pgi = mTvIsdbChannelManager.getProgramInfo(k);
            } else {
                pgi = mTvChannelManager.getProgramInfoByIndex(k);

            }

            if (pgi != null) {
                if (Constant.SHOW_FAVORITE_LIST == mProgListType) {
                    // Show Favorite Programs Only
                    if ((pgi.isDelete == true) || (pgi.isVisible == false) || pgi.favorite == 0) {
                        continue;
                    } else {
                        ProgramFavoriteObject pfo = new ProgramFavoriteObject();
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                                pfo.setChannelId(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
                            } else {
                                String channum = mTvAtscChannelManager.getDispChannelNum(pgi);
                                String name = mTvAtscChannelManager.getDispChannelName(pgi);
                                pfo.setChannelId(channum);
                                pfo.setChannelName(name);
                            }
                        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                                pfo.setChannelId(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
                            } else {
                                String channum = pgi.majorNum + "." + pgi.minorNum;
                                pfo.setChannelId(channum);
                            }
                            pfo.setChannelName(pgi.serviceName);
                        } else {
                            if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                                pfo.setChannelId(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
                            } else {
                                pfo.setChannelId(String.valueOf(pgi.number));
                            }
                            pfo.setChannelName(pgi.serviceName);
                        }
                        pfo.setServiceType(pgi.serviceType);
                        pfo.setSkipImg(pgi.isSkip);
                        pfo.setSslImg(pgi.isScramble);
                        pfo.setFrequenry(pgi.frequency);
                        pfo.setProgInfoListIdx(mProgInfoList.size());

                        mListItem.add(pfo);
                        mProgInfoList.add(pgi);
                    }
                } else if (Constant.SHOW_PROGRAM_LIST == mProgListType) {
                    // Show All Programs
                    if ((pgi.isDelete == true) || (pgi.isVisible == false)) {
                        continue;
                    } else {
                        ProgramFavoriteObject pfo = new ProgramFavoriteObject();
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            String channum = mTvAtscChannelManager.getDispChannelNum(pgi);
                            String name = mTvAtscChannelManager.getDispChannelName(pgi);
                            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                                pfo.setChannelId(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
                            } else {
                                pfo.setChannelId(channum);
                            }
                            pfo.setChannelName(name);
                        } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                                pfo.setChannelId(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
                            } else {
                                String channum = pgi.majorNum + "." + pgi.minorNum;
                                pfo.setChannelId(channum);
                            }
                            pfo.setChannelName(pgi.serviceName);
                        } else {
                            if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                                pfo.setChannelId(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
                            } else {
                                pfo.setChannelId(String.valueOf(pgi.number));
                            }
                            pfo.setChannelName(pgi.serviceName);
                        }
                        pfo.setServiceType(pgi.serviceType);
                        pfo.setSkipImg(pgi.isSkip);
                        pfo.setSslImg(pgi.isScramble);
                        pfo.setFrequenry(pgi.frequency);
                        pfo.setProgInfoListIdx(mProgInfoList.size());

                        mListItem.add(pfo);
                        mProgInfoList.add(pgi);
                    }
                }
            }
        }

        mDisplayedListItem = new ArrayList<ProgramFavoriteObject>(mListItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        setContentView(R.layout.program_favorite_list);
        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            mProgListType = getIntent().getIntExtra("ListId", 0);
        }
        mSearchView = (SearchView) findViewById(R.id.program_favorite_search);
        mProgListView = (ListView) findViewById(R.id.program_favorite_list_view);
        mTextTitle = (TextView) findViewById(R.id.program_favorite_title);
        mHintOP = (TextView) findViewById(R.id.program_favorite_str_op);
        mHintDelOP = (TextView) findViewById(R.id.program_favorite_str_del_op);
        mOPListAdapter = new OperatorProfileListAdapter(this);
        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            mTvChannelManager = TvIsdbChannelManager.getInstance();
            mTvIsdbChannelManager = TvIsdbChannelManager.getInstance();
            Log.d(TAG, "oncreate===antennaType = " + mTvIsdbChannelManager.getAntennaType());
            mTvIsdbChannelManager.genMixProgList(false);
        } else {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                mTvAtscChannelManager = TvAtscChannelManager.getInstance();
            }
            mTvChannelManager = TvChannelManager.getInstance();
            refreshOPProfileList();
        }

        getListInfo();
        mAdapter = new ChannelListAdapter(this, mDisplayedListItem);
        if (TvCiManager.getInstance().isOpMode()) {
            Log.i(TAG, "E_OP_CHANNEL_LIST_MENU");
            switchChannelListState(OP_CHANNEL_LIST_MENU);
        } else {
            Log.i(TAG, "E_NORMAL_CHANNEL_LIST_MENU");
            switchChannelListState(NORMAL_CHANNEL_LIST_MENU);
        }

        int ttsSpeakId = R.string.str_channelList_program;
        if (Constant.SHOW_FAVORITE_LIST == mProgListType) {
            ttsSpeakId = R.string.str_channelList_favorite;
        }
        TvCommonManager.getInstance().speakTtsDelayed(
            getString(ttsSpeakId)
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);

        mProgListView.setDividerHeight(0);
        mProgListView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                int selItemIndex = (int) mProgListView.getSelectedItemId();
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                    Log.d(TAG, "selItemIndex" + selItemIndex);

                    if (selItemIndex >= mAdapter.getListData().size()) {
                        return false;
                    }
                    final ProgramInfo progInfo = mProgInfoList.get(mAdapter.getListData().get(selItemIndex).getProgInfoListIdx());
                    Log.d(TAG, "number" + progInfo.number);
                    if (true == isSameWithCurrentProgram(progInfo)) {
                        Log.d(TAG, "CH List :Select the same channel!!!");
                    } else {
                        if (progInfo.serviceType < TvChannelManager.SERVICE_TYPE_INVALID) {
                            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                                mTvAtscChannelManager.programSel(progInfo.majorNum, progInfo.minorNum);
                            } else {
                                if (TvChannelManager.SERVICE_TYPE_DTV == progInfo.serviceType) {
                                    Utility.channelSelect(ChannelListActivity.this, progInfo.number);
                                } else {
                                    mTvChannelManager.selectProgram(progInfo.number, progInfo.serviceType);
                                }
                            }
                        } else {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        timeOutHelper = new TimeOutHelper(handler, this);

        if (true == mIsSupportSearchAndSort) {
            mProgListView.setTextFilterEnabled(true);

            mSearchView.setIconifiedByDefault(false);
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setSubmitButtonEnabled(false);
            mSearchView.setIconifiedByDefault(false);

            mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                    } else {
                        mProgListView.requestFocus();
                    }
                }
            });

            //FIXME: use xml to set text color is better
            int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) mSearchView.findViewById(id);
            textView.setTextColor(Color.WHITE);
            textView.setHintTextColor(Color.WHITE);

            LinearLayout ll = (LinearLayout)findViewById(R.id.linearlayout_search_and_sort);
            ll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mDtvEventListener = new DtvEventListener();
        TvChannelManager.getInstance().registerOnDtvPlayerEventListener(mDtvEventListener);

        if (mState == OPERATOR_PROFILE_MENU) {
            switchChannelListState(NORMAL_CHANNEL_LIST_MENU);
        }
        timeOutHelper.start();
        timeOutHelper.init();
    };

    @Override
    protected void onPause() {
        TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(mDtvEventListener);
        mDtvEventListener = null;
        timeOutHelper.stop();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        timeOutHelper.reset();
        String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();

        // FIXME: this is the temporary solution
        if (sendCecKey(keyCode)) {
            Log.i(TAG, "sendCecKey success!");
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_CHANNEL_UP) {
            if (deviceName.equals("MStar Smart TV Keypad")) {
                mProgListView.setSelection(mProgListView.getSelectedItemPosition() == 0 ? 0
                        : mProgListView.getSelectedItemPosition() - 1);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
            if (deviceName.equals("MStar Smart TV Keypad")) {
                mProgListView.setSelection(mProgListView.getSelectedItemPosition() == (mProgListView
                        .getCount() - 1) ? (mProgListView.getCount() - 1) : (mProgListView
                        .getSelectedItemPosition() + 1));
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mProgListView.getSelectedItemPosition() == 0) {
                if ((true == mIsSupportSearchAndSort) && (mSearchView.getVisibility() == View.VISIBLE)) {
                    mSearchView.requestFocus();
                } else {
                    mProgListView.setSelection(mAdapter.getListData().size() - 1);
                }
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (!mProgListView.isFocused()) {
                if (0 < mAdapter.getListData().size()) {
                    mProgListView.requestFocus();
                    mProgListView.setSelection(0);
                }
            }
            if (mProgListView.getSelectedItemPosition() == mAdapter.getListData().size() - 1) {
                mProgListView.setSelection(0);
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_PROG_YELLOW) {
            if ((true == mIsSupportSearchAndSort) && (mState == NORMAL_CHANNEL_LIST_MENU)) {
                mSearchView.setQuery("", false);
                if (mSearchView.getVisibility() == View.VISIBLE) {
                    mSearchView.setVisibility(View.GONE);
                    mProgListView.requestFocus();
                } else {
                    mSearchView.setVisibility(View.VISIBLE);
                    mSearchView.requestFocus();
                }
                mAdapter.setListData(getFilterResult(mSearchView.getQuery().toString()));
                mProgListView.setSelection(getfocusIndex());
                return true;
            } else {
                Log.w(TAG, "find funciton is not supported");
            }
        }

        if (keyCode == KeyEvent.KEYCODE_PROG_GREEN) {
            if (mProgListView.isFocused()) {
                if ((true == mIsSupportSearchAndSort) && (mState == NORMAL_CHANNEL_LIST_MENU)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.str_channelList_sort);
                    builder.setSingleChoiceItems(R.array.str_sort_type, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                                if (item < TvDvbChannelManager.SORTING_TYPE_NUM) {
                                    TvDvbChannelManager.getInstance().setProgramSortByType(item);
                                    getListInfo();
                                    mAdapter.setListData(getFilterResult(mSearchView.getQuery().toString()));
                                    mProgListView.setSelection(getfocusIndex());
                                    mSortingTypeSelectionDialog.dismiss();
                                    mSortingTypeSelectionDialog = null;
                                }
                            }
                        }
                    );
                    mSortingTypeSelectionDialog = builder.create();
                    mSortingTypeSelectionDialog.show();

                    return true;
                } else {
                    Log.w(TAG, "sort funciton is not supported");
                }
            }
        }

        if (keyCode == KeyEvent.KEYCODE_PROG_BLUE) {
            // Focus is not on channel list currently, ignore key.
            if (!mProgListView.isFocused())
                return true;

            if (mState == OPERATOR_PROFILE_MENU) {
                displayDeleteOpconfirmation();
                mOPListAdapter.notifyDataSetChanged();
            }
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_PROG_RED) {
                if (!mProgListView.isFocused()) {
                    if (0 < mAdapter.getListData().size()) {
                        mProgListView.requestFocus();
                        mProgListView.setSelection(0);
                    }
                }
                boolean bOpMode = false;
                bOpMode = TvCiManager.getInstance().isOpMode();
                Log.i(TAG, "mState = " + mState);
                Log.i(TAG, "mOPCount = " + mOPCount);
                Log.i(TAG, "bOpMode = " + bOpMode);
                if (mState == NORMAL_CHANNEL_LIST_MENU
                        && (mOPCount != 0)
                        && (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV)
                        && (bOpMode == false)) {
                    Log.i(TAG, "MVK_RED: Enter E_OPERATOR_PROFILE_MENU");
                    switchChannelListState(OPERATOR_PROFILE_MENU);
                } else if (mState == OPERATOR_PROFILE_MENU) {
                    if (mOPCount != 0) {
                        OperatorProfileInfo selectItem = (OperatorProfileInfo) mOPListAdapter
                                .getItem(mProgListView.getSelectedItemPosition());
                        if (selectItem.getOPAcceisable() == true) {
                            Log.i(TAG, "MVK_RED: Enter E_OP_CHANNEL_LIST_MENU");
                            Log.i(TAG, "enterCiOperatorProfile!");
                            TvCiManager.getInstance().enterCiOperatorProfile(
                                    selectItem.getOPCacheResideIndex());
                        } else {
                            Log.i(TAG, "Forbid Entering OP Mode!");
                        }
                    }
                } else if (mState == OP_CHANNEL_LIST_MENU
                        && (bOpMode == true)) {
                    Log.i(TAG, "MVK_RED: Enter E_NORMAL_CHANNEL_LIST_MENU");
                    TvCiManager.getInstance().exitCiOperatorProfile();
                }
                return true;
        }

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(TvIntent.MAINMENU);
            intent.putExtra("currentPage", MainMenuActivity.PICTURE_PAGE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_TV_INPUT) {
            finish();
            return true;
        }
        if (keyCode == MKeyEvent.KEYCODE_LIST) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // FIXME: this is the temporary solution
    private boolean sendCecKey(int keyCode) {
        Log.d(TAG, "sendCecKey()");
        CecSetting setting = TvCecManager.getInstance().getCecConfiguration();
        int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (curInputSource == TvCommonManager.INPUT_SOURCE_HDMI
                || curInputSource == TvCommonManager.INPUT_SOURCE_HDMI2
                || curInputSource == TvCommonManager.INPUT_SOURCE_HDMI3
                || curInputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
            if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                if (TvCecManager.getInstance().sendCecKey(keyCode)) {
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
                if (TvCecManager.getInstance().sendCecKey(keyCode)) {
                    Log.d(TAG, "send Cec key,keyCode is " + keyCode
                            + ", tv don't handl the key");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.setListData(getFilterResult(newText));
        mProgListView.setSelection(getfocusIndex());
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    private ArrayList<ProgramFavoriteObject> getFilterResult(String newText) {
        ArrayList<ProgramFavoriteObject> result;
       if (TextUtils.isEmpty(newText)) {
            result = mDisplayedListItem;
        } else {
            ArrayList<ProgramFavoriteObject> filterResultsData = new ArrayList<ProgramFavoriteObject>();

            for (ProgramFavoriteObject pfo: mDisplayedListItem) {
                Pattern pattern = Pattern.compile(newText);
                Matcher matcher = pattern.matcher(pfo.channelName);
                if (matcher.find()) {
                    filterResultsData.add(pfo);
                }
            }
            result = filterResultsData;
        }

        return result;
    }

    private int getfocusIndex() {
        int focusIndex = 0;
        if (Constant.SHOW_PROGRAM_LIST == mProgListType) {
            ProgramInfo cpi = mTvChannelManager.getCurrentProgramInfo();
            for (ProgramFavoriteObject pfo: mAdapter.getListData()) {
                ProgramInfo pi = mProgInfoList.get(pfo.getProgInfoListIdx());
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if ((cpi.majorNum == pi.majorNum)
                        && (cpi.minorNum == pi.minorNum)
                        && (cpi.serviceType == pi.serviceType)) {
                        focusIndex = mAdapter.getListData().indexOf(pfo);
                        break;
                    }
                } else {
                    if (cpi.number == pi.number
                        && cpi.serviceType == pi.serviceType) {
                        focusIndex = mAdapter.getListData().indexOf(pfo);
                        break;
                    }
                }
            }
        }

        Log.v(TAG, "focusIndex = " + focusIndex);
        return focusIndex;
    }

    private void switchChannelListState(int state) {
        Log.d(TAG, "switchChannelListState(), state = " + state);
        if (state == NORMAL_CHANNEL_LIST_MENU) {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
                    if ((mOPCount != 0) && (currInputSource == TvCommonManager.INPUT_SOURCE_DTV))
                        mHintOP.setVisibility(TextView.VISIBLE);
                    else
                        mHintOP.setVisibility(TextView.GONE);
                    mHintDelOP.setVisibility(TextView.GONE);

                    if (Constant.SHOW_FAVORITE_LIST == mProgListType) {
                        mTextTitle.setText(R.string.str_channelList_favorite);
                    } else if (Constant.SHOW_PROGRAM_LIST == mProgListType) {
                        mTextTitle.setText(R.string.str_channelList_program);
                    }
                    mHintOP.setText(R.string.str_op_menu);
                    mAdapter.setListData(mDisplayedListItem);
                    mProgListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mProgListView.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            ttsSpeakFocusItem(pos);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    mProgListView.invalidate();
                    mProgListView.setSelection(getfocusIndex());
                }
            });
            mState = state;
        } else if (state == OPERATOR_PROFILE_MENU) {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    mHintOP.setVisibility(TextView.VISIBLE);
                    mHintDelOP.setVisibility(TextView.VISIBLE);

                    mTextTitle.setText(R.string.str_op_menu_title);
                    mHintOP.setText(R.string.str_enter_op);
                    refreshOPProfileList();
                    mProgListView.setAdapter(mOPListAdapter);
                    mOPListAdapter.notifyDataSetChanged();
                    mProgListView.invalidate();
                }
            });
            mState = state;
        } else if (state == OP_CHANNEL_LIST_MENU) {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    mHintOP.setVisibility(TextView.VISIBLE);
                    mHintDelOP.setVisibility(TextView.GONE);

                    mTextTitle.setText(R.string.str_op_channel_list_title);
                    mHintOP.setText(R.string.str_exit_op);
                    mAdapter.setListData(mDisplayedListItem);
                    mProgListView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mProgListView.invalidate();
                    mProgListView.setSelection(getfocusIndex());
                }
            });
            mState = state;
        } else {
            Log.v(TAG, "Set unkonwn state mState = " + state);
        }
    }

    private void displayDeleteOpconfirmation() {
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        final View layout = factory.inflate(R.layout.delete_op_confirmation_dialog, null);

        String dialogcontent = null;
        String opName = null;
        OperatorProfileInfo selectItem = (OperatorProfileInfo) mOPListAdapter
                .getItem(mProgListView.getSelectedItemPosition());

        opName = TvCiManager.getInstance().getOpProfileNameByIndex(
                selectItem.getOPCacheResideIndex());

        dialogcontent = getString(R.string.str_delete_op_msg) + opName + "] ?";

        mDeleteOpconfirmation = new AlertDialog.Builder(getApplicationContext())
                .setTitle(getString(R.string.str_delete_op_confirm))
                .setView(layout)
                .setMessage(dialogcontent)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setPositiveButton(getString(android.R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OperatorProfileInfo deleteItem = (OperatorProfileInfo) mOPListAdapter
                                        .getItem(mProgListView.getSelectedItemPosition());
                                TvCiManager.getInstance().deleteOpCacheByIndex(
                                        deleteItem.getOPCacheResideIndex());
                                mOPCount--;
                                Log.i(TAG, "mOPCount =" + mOPCount);
                                mOPListAdapter.remove(deleteItem);
                                if (mOPCount == 0) {
                                    switchChannelListState(NORMAL_CHANNEL_LIST_MENU);
                                } else {
                                    refreshOPProfileList();
                                }
                            }
                        })
                .setNegativeButton(getString(android.R.string.no), null)
                .create();
        mDeleteOpconfirmation.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        mDeleteOpconfirmation.show();
    }

    private boolean isSameWithCurrentProgram(ProgramInfo ProgInf) {
        boolean ret = false;
        ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if ((curProgInfo.majorNum == ProgInf.majorNum)
                && (curProgInfo.minorNum == ProgInf.minorNum)
                && (curProgInfo.serviceType == ProgInf.serviceType)) {
                ret = true;
            }
        } else {
            if ((curProgInfo.number == ProgInf.number)
                && (curProgInfo.serviceType == ProgInf.serviceType)) {
                ret = true;
            }
        }
        return ret;
    }

    private void refreshOPProfileList() {
        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        mOPCount = 0;
        if (mOPListAdapter != null) {
            mOPListAdapter.clear();
        } else {
            Log.e(TAG, "mOPListAdapter is null!!");
            return;
        }

        if (currInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            List<OperatorProfileInfo> mopdatalist = new ArrayList<OperatorProfileInfo>();

            short opCacheCount = TvCiManager.getInstance().getOpCacheCount();
            Log.i(TAG, "opCacheCount:" + opCacheCount);

            for (int i = 0; i < opCacheCount; i++) {
                short opsystype = TvCiManager.getInstance().getOpDtvSysTypeByIndex((short) i);
                Log.i(TAG, "opsystype:" + opsystype);
                if (opsystype != OperatorProfileInfo.EnumDeliverySysType.E_DELIVERY_SYS_NONE
                        .ordinal()) {
                    mOPCount++;
                    OperatorProfileInfo opInfo = new OperatorProfileInfo();
                    String opName = TvCiManager.getInstance().getOpProfileNameByIndex((short) i);
                    Log.i(TAG, "OpName:" + opName);
                    int dtvRouteIndex = TvChannelManager.DTV_ROUTE_INDEX_MAX_COUNT;
                    dtvRouteIndex = mTvChannelManager.getCurrentDtvRouteIndex();
                    TvTypeInfo tvinfo = TvCommonManager.getInstance().getTvInfo();

                    opInfo.setId(i);
                    opInfo.setOPCacheResideIndex((short) i);
                    opInfo.setOperatorProfileName(opName);
                    opInfo.setOPSysType(opsystype);

                    if (opsystype == OperatorProfileInfo.EnumDeliverySysType.E_DELIVERY_SYS_TDSD
                            .ordinal()) {
                        if ((TvChannelManager.TV_ROUTE_DVBT == tvinfo.routePath[dtvRouteIndex])
                                || (TvChannelManager.TV_ROUTE_DVBT2 == tvinfo.routePath[dtvRouteIndex])) {
                            opInfo.setOPAcceisable(true);
                        } else {
                            opInfo.setOPAcceisable(false);
                        }
                    } else if (opsystype == OperatorProfileInfo.EnumDeliverySysType.E_DELIVERY_SYS_CDSD
                            .ordinal()) {
                        if (TvChannelManager.TV_ROUTE_DVBC == tvinfo.routePath[dtvRouteIndex]) {
                            opInfo.setOPAcceisable(true);
                        } else {
                            opInfo.setOPAcceisable(false);
                        }
                    } else if (opsystype == OperatorProfileInfo.EnumDeliverySysType.E_DELIVERY_SYS_SDSD
                            .ordinal()) {
                        if ((TvChannelManager.TV_ROUTE_DVBS == tvinfo.routePath[dtvRouteIndex])
                                || (TvChannelManager.TV_ROUTE_DVBS2 == tvinfo.routePath[dtvRouteIndex])) {
                            opInfo.setOPAcceisable(true);
                        } else {
                            opInfo.setOPAcceisable(false);
                        }
                    } else {
                        opInfo.setOPAcceisable(false);
                    }

                    mopdatalist.add(opInfo);
                } else {
                    Log.w(TAG, "[Warning!] DTV Type is E_DELIVERY_SYS_NONE");
                }

            }
            mOPListAdapter.setItems(mopdatalist);
        }
    }

    private void ttsSpeakFocusItem(int position) {
        if (NORMAL_CHANNEL_LIST_MENU == mState) {
            if ((null != mDisplayedListItem) && (position < mDisplayedListItem.size())) {
                String str = mDisplayedListItem.get(position).getChannelId() + ", "+ Utility.getStrLimited(mDisplayedListItem.get(position).getChannelName(), Constant.TTS_CHANNEL_NAME_MAX_LENGTH);
                TvCommonManager.getInstance().speakTtsDelayed(
                    str
                    , TvCommonManager.TTS_QUEUE_FLUSH
                    , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
                    , TvCommonManager.TTS_DELAY_TIME_100MS);
            }
        }
    }
}
