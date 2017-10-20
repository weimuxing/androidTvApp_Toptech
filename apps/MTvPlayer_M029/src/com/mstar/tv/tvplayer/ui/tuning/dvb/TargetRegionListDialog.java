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

package com.mstar.tv.tvplayer.ui.tuning.dvb;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.KeyEvent;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.tuning.ChannelTuning;
import com.mstar.util.Constant;

import java.util.ArrayList;

public class TargetRegionListDialog {
    private final static String TAG = "TargetRegionListDialog";

    private Activity mActivity = null;

    private Dialog mDialog = null;

    private ListView mListView = null;

    private TextView mTextTitle = null;

    private TargetRegionListAdapter mAdapter = null;

    private ArrayList<String> mRegionNameList = new ArrayList<String>();

    private int mCurrentLevel = 0;

    private int mSelectIndex = 0;

    public TargetRegionListDialog(Activity activity, int level, ArrayList<String> regionNameList) {
        mActivity = activity;
        mDialog = new Dialog(mActivity, R.style.dialog);
        mDialog.setContentView(R.layout.target_region_list);
        mCurrentLevel = level;
        mRegionNameList = regionNameList;
        initItems(level);
        setListeners();
    }

    private void initItems(int level) {
        mListView = (ListView) mDialog.findViewById(R.id.target_region_list_listview);
        mTextTitle = (TextView) mDialog.findViewById(R.id.target_region_list_title);
        mAdapter = new TargetRegionListAdapter(mActivity, mRegionNameList);
        switch (level) {
            case Constant.TARGET_REGION_COUNTRY_LEVEL:
                mTextTitle.setText(R.string.str_target_region_country);
                break;
            case Constant.TARGET_REGION_PRIMARY_LEVEL:
                mTextTitle.setText(R.string.str_target_region_primary);
                break;
            case Constant.TARGET_REGION_SECONDARY_LEVEL:
                mTextTitle.setText(R.string.str_target_region_secondary);
                break;
            case Constant.TARGET_REGION_TERTIARY_LEVEL:
                mTextTitle.setText(R.string.str_target_region_tertiary);
                break;
        }
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setDividerHeight(0);
        mListView.setSelection(0);
    }

    public int getSelectIndex() {
        return mSelectIndex;
    }

    public void show() {
        if (false == mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public boolean isShowing() {
        return mDialog.isShowing();
    }

    public void onShow() {
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void onDismiss() {
        View view = mDialog.findViewById(R.id.linearlayout_target_region_list);
        if (null != view) {
            view.animate()
                    .alpha(0f)
                    .setDuration(
                            mActivity.getResources().getInteger(
                                    android.R.integer.config_shortAnimTime));
        }
    }

    private void setListeners() {
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TargetRegionListDialog.this.onShow();
            }
        });

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                TargetRegionListDialog.this.onDismiss();
            }
        });

        mDialog.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent keyEvent) {
                int selItemIndex = (int) mListView.getSelectedItemId();
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (selItemIndex >= mRegionNameList.size()) {
                        return false;
                    }
                    Log.d(TAG, "user select " + selItemIndex);
                    mSelectIndex = selItemIndex;
                    dismiss();
                    return true;
                } else if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
                        && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public class TargetRegionListAdapter extends BaseAdapter {
        ArrayList<String> mData = null;

        private Context mContext;

        public TargetRegionListAdapter(Context context, ArrayList<String> data) {
            mContext = context;
            mData = data;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.target_region_list_item,
                    null);
            TextView tmpText = (TextView) convertView.findViewById(R.id.target_region_name);
            tmpText.setText(mData.get(position));
            return convertView;
        }
    }
}
