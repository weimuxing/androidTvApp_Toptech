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

package com.mstar.tv.tvplayer.ui.dtv.ewbs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mstar.tv.tvplayer.ui.R;

import java.util.ArrayList;

public class EWBSNameSelectingDialog extends Dialog {
    private final String TAG = "EWBSNameSelectingDialog";

    private Context mContext = null;

    private TextView mTitle = null;

    private ListView mListView = null;

    private ListMenuAdapter mAdapter = null;

    private String mTitleStr;

    private ArrayList<AreaCodeObject> mListItem = new ArrayList<AreaCodeObject>();

    private int mEwbsInfoApiReturnCode = 0;

    private OnAreaCodeSelectedListener mOnAreaCodeSelectedListener = null;

    public EWBSNameSelectingDialog(Context context, int style, String titleStr, ArrayList<AreaCodeObject> objList) {
        super(context, style);
        mContext = context;
        mTitleStr = new String(titleStr);
        mListItem = new ArrayList<AreaCodeObject>(objList);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listmenu_dialog);
        mTitle = (TextView) findViewById(R.id.listmenu_dlg_title);
        mListView = (ListView) findViewById(R.id.listmenu_dlg_listview);
        mTitle.setText(mTitleStr);
        mAdapter = new ListMenuAdapter(mContext, mListItem);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Log.d(TAG, "onItemClick pos = " + pos);
                if (null != mOnAreaCodeSelectedListener) {
                    final AreaCodeObject item = mListItem.get(pos);
                    mOnAreaCodeSelectedListener.onSelected(item.getAreaCode());
                }
            }
        });

        mListView.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.d(TAG, "onItemSelected pos = " + pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void registerOnAreaCodeSelectedListener(OnAreaCodeSelectedListener listener) {
        mOnAreaCodeSelectedListener = listener;
    }

    public interface OnAreaCodeSelectedListener {
        void onSelected(int code);
    }

    public class ListMenuAdapter extends BaseAdapter {
        ArrayList<AreaCodeObject> mData = null;

        private Context mContext;

        public ListMenuAdapter(Context context, ArrayList<AreaCodeObject> data) {
            mContext = context;
            mData = data;
        }

        public ArrayList<AreaCodeObject> getListData() {
            return mData;
        }

        public void setListData(ArrayList<AreaCodeObject> data) {
            mData = data;
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
            int layout = R.layout.listmenu_dialog_item;
            convertView = LayoutInflater.from(mContext).inflate(layout, null);
            TextView txtView = (TextView) convertView
                    .findViewById(R.id.listmenu_dlg_item);
            txtView.setText(mData.get(position).getAreaName());
            return convertView;
        }
    }
}
