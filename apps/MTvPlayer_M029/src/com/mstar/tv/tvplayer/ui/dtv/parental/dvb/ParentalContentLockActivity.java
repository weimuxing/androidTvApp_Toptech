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

package com.mstar.tv.tvplayer.ui.dtv.parental.dvb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;

public class ParentalContentLockActivity extends MstarBaseActivity {
    private static final String TAG = "ParentalContentLockActivity";

    private TextView mTitle;

    private ListView mContentLockList;

    private String[] mContentLockListValue;

    private boolean[] mRating;

    private ContentLockListAdapter adapter;

    private class ContentLockListAdapter extends BaseAdapter {
        private String[] mContent = null;

        private Context mContext;

        private boolean[] mRating;

        public ContentLockListAdapter(Context context, String[] data, boolean[] rate) {
            mContext = context;
            mContent = data;
            mRating = rate;
        }

        @Override
        public int getCount() {
            return mContent.length;
        }

        @Override
        public Object getItem(int arg0) {
            return mContent[arg0];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.contentlock_item, null);
            }
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contentlock_item, null);
            TextView txtContentLockContent = (TextView) convertView.findViewById(R.id.contentlock_content);
            txtContentLockContent.setText(mContent[position]);
            ImageView imgLock = (ImageView) convertView.findViewById(R.id.ifchoice);
            if (false == mRating[position]) {
                imgLock.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parental_contentlock);
        mContentLockListValue = getResources().getStringArray(R.array.contentlock_list);
        mTitle = (TextView) findViewById(R.id.parental_contentlock_title);
        mTitle.setText(getResources().getString(R.string.str_parental_contentlock));
        mContentLockList = (ListView) findViewById(R.id.parental_contentlock_list);
        int rate = TvParentalControlManager.getInstance().getParentalObjectiveContent();
        mRating = new boolean[mContentLockListValue.length];
        for (int i = 0; i < mContentLockListValue.length; i++) {
            mRating[i] = false;
        }
        mRating[rate] = true;
        adapter = new ContentLockListAdapter(this, mContentLockListValue, mRating);
        mContentLockList.setAdapter(adapter);
        setListener();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mContentLockList.getSelectedItemPosition() == 0) {
                mContentLockList.setSelection(mContentLockListValue.length - 1);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mContentLockList.getSelectedItemPosition() == mContentLockListValue.length - 1) {
                mContentLockList.setSelection(0);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(TvIntent.MAINMENU);
            intent.putExtra("currentPage", MainMenuActivity.LOCK_PAGE);
            intent.putExtra(Constant.PARENTAL_CONTROL_MENU_PERMITTED, true);
            if (intent.resolveActivity(ParentalContentLockActivity.this.getPackageManager()) != null) {
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setListener() {
        mContentLockList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int oldRate = TvParentalControlManager.getInstance().getParentalObjectiveContent();
                TvParentalControlManager.getInstance().setParentalObjectiveContent(arg2);
                mRating[oldRate] = false;
                mRating[arg2] = true;
                adapter.notifyDataSetChanged();
            }
        });
    }
}
