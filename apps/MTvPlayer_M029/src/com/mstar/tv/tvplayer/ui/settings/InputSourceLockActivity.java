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

package com.mstar.tv.tvplayer.ui.settings;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;

public class InputSourceLockActivity extends MstarBaseActivity {
    private static final String TAG = "InputSourceLockActivity";

    private final int FUNCTION_DISABLED = 0;

    private ListView mListView;

    private InputSourceListAdapter mAdapter = null;

    private ArrayList<InputSourceObject> mPfos = new ArrayList<InputSourceObject>();

    private boolean[] mIsChannelLocked = null;

    /*
     * Only the support source that can be added in the list.
     * Note: If you try to set source lock to a unsupport source,
     *       SN will rise a Fatal Assertion.
     */
    private final int[] mInputAdjustaleSource = new int[]{
        TvCommonManager.INPUT_SOURCE_VGA,
        TvCommonManager.INPUT_SOURCE_ATV,
        TvCommonManager.INPUT_SOURCE_CVBS,
        TvCommonManager.INPUT_SOURCE_CVBS2,
        TvCommonManager.INPUT_SOURCE_CVBS3,
        TvCommonManager.INPUT_SOURCE_CVBS4,
        TvCommonManager.INPUT_SOURCE_CVBS5,
        TvCommonManager.INPUT_SOURCE_CVBS6,
        TvCommonManager.INPUT_SOURCE_CVBS7,
        TvCommonManager.INPUT_SOURCE_CVBS8,
        TvCommonManager.INPUT_SOURCE_SVIDEO,
        TvCommonManager.INPUT_SOURCE_SVIDEO2,
        TvCommonManager.INPUT_SOURCE_SVIDEO3,
        TvCommonManager.INPUT_SOURCE_SVIDEO4,
        TvCommonManager.INPUT_SOURCE_YPBPR,
        TvCommonManager.INPUT_SOURCE_YPBPR2,
        TvCommonManager.INPUT_SOURCE_YPBPR3,
        TvCommonManager.INPUT_SOURCE_SCART,
        TvCommonManager.INPUT_SOURCE_SCART2,
        TvCommonManager.INPUT_SOURCE_HDMI,
        TvCommonManager.INPUT_SOURCE_HDMI2,
        TvCommonManager.INPUT_SOURCE_HDMI3,
        TvCommonManager.INPUT_SOURCE_HDMI4,
        TvCommonManager.INPUT_SOURCE_DTV,
        TvCommonManager.INPUT_SOURCE_DVI,
        TvCommonManager.INPUT_SOURCE_DVI2,
        TvCommonManager.INPUT_SOURCE_DVI3,
        TvCommonManager.INPUT_SOURCE_DVI4,
        TvCommonManager.INPUT_SOURCE_VGA2,
        TvCommonManager.INPUT_SOURCE_VGA3,
    };

    private String[] mLiteralInputSources = null;

    public class InputSourceListAdapter extends BaseAdapter {
        ArrayList<InputSourceObject> mData = null;

        private Context mContext;

        private boolean[] isInputSourcelocked = null;

        public InputSourceListAdapter(Context context, ArrayList<InputSourceObject> data,
                boolean[] isInputSourcelock) {
            mContext = context;
            mData = data;
            isInputSourcelocked = isInputSourcelock;
        }

        public void updateLockStatus(boolean[] status) {
            isInputSourcelocked = status;
            notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.input_source_lock_item, null);
            TextView tmpText = (TextView) convertView.findViewById(R.id.input_source_lock_source_id);
            tmpText.setText(mData.get(position).getInputSourceName());
            ImageView lock = (ImageView) convertView.findViewById(R.id.input_source_lock_icon);
            if (isInputSourcelocked[position]) {
                lock.setVisibility(View.VISIBLE);
            } else {
                lock.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private class InputSourceObject {
        private int inputSourceId = -1;

        private String inputSourceName = "";

        public int getInputSourceId() {
            return inputSourceId;
        }

        public void setInputSourceId(int inputSourceId) {
            this.inputSourceId = inputSourceId;
        }

        public String getInputSourceName() {
            return inputSourceName;
        }

        public void setInputSourceName(String inputSourceName) {
            this.inputSourceName = inputSourceName;
        }
    }

    private void getListInfo() {
        int[] sourceList = TvCommonManager.getInstance().getSourceList();
        if (null == sourceList) {
            Log.e(TAG, "No input source avaiable.");
            return;
        }
        int inputSource = -1;
        int counter = 0;
        boolean[] tempIsChannelLocked = new boolean[mInputAdjustaleSource.length];

        for (int i = 0; i < mInputAdjustaleSource.length; i++) {
            inputSource = mInputAdjustaleSource[i];
            if ((inputSource < sourceList.length) && (FUNCTION_DISABLED != sourceList[inputSource])) {
                InputSourceObject pfo = new InputSourceObject();
                pfo.setInputSourceId(inputSource);
                pfo.setInputSourceName(mLiteralInputSources[inputSource]);
                mPfos.add(pfo);
                tempIsChannelLocked[counter] = TvCommonManager.getInstance().getInputSourceLock(inputSource);
                Log.d(TAG, "tempIsChannelLocked["+counter+"] = " + tempIsChannelLocked[counter]);
                counter++;
            }
        }
        mIsChannelLocked = Arrays.copyOf(tempIsChannelLocked, counter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_source_lock);

        mListView = (ListView) findViewById(R.id.input_source_lock_list);
        mLiteralInputSources =  getResources().getStringArray(R.array.str_arr_input_sources);
        getListInfo();
        mAdapter = new InputSourceListAdapter(this, mPfos, mIsChannelLocked);
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
        mListView.setSelection(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mListView.getSelectedItemPosition() == 0) {
                mListView.setSelection(mPfos.size() - 1);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mListView.getSelectedItemPosition() == mPfos.size() - 1) {
                mListView.setSelection(0);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(TvIntent.MAINMENU);
            intent.putExtra("currentPage", MainMenuActivity.SETTING_PAGE);
            if (intent.resolveActivity(InputSourceLockActivity.this.getPackageManager()) != null) {
                startActivity(intent);
            }
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_PROG_RED) {
            int select = mListView.getSelectedItemPosition();
            InputSourceObject progInf = mPfos.get(select);
            if (TvCommonManager.getInstance().setInputSourceLock(!mIsChannelLocked[select], progInf.getInputSourceId())) {
                mIsChannelLocked[select] = !mIsChannelLocked[select];
                Log.d(TAG, "mIsChannelLocked["+select+"] = " + mIsChannelLocked[select]);
                mAdapter.updateLockStatus(mIsChannelLocked);
            }
        } else if (keyCode == KeyEvent.KEYCODE_PROG_GREEN) {
            TvCommonManager.getInstance().resetInputSourceLock();
            int[] sourceList = TvCommonManager.getInstance().getSourceList();
            if (null == sourceList) {
                Log.e(TAG, "No input source avaiable.");
            } else {
                for (int i = 0; i < mIsChannelLocked.length; i++) {
                    int inputSource = mInputAdjustaleSource[i];
                    if(FUNCTION_DISABLED != sourceList[inputSource]) {
                        mIsChannelLocked[i] = TvCommonManager.getInstance().getInputSourceLock(inputSource);
                    }
                }
                mAdapter.updateLockStatus(mIsChannelLocked);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
