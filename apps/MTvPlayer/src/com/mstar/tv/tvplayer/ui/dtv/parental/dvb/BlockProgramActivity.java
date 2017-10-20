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

package com.mstar.tv.tvplayer.ui.dtv.parental.dvb;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class BlockProgramActivity extends MstarBaseActivity {

    private ListView proListView;

    private ArrayList<ProgramFavoriteObject> pfos = new ArrayList<ProgramFavoriteObject>();

    private ArrayList<ProgramInfo> progInfoList = new ArrayList<ProgramInfo>();

    private ChannelListAdapter adapter = null;

    private boolean[] mIsChannelLocked = null;

    private TvChannelManager mTvChannelManager = null;

    public class ChannelListAdapter extends BaseAdapter {
        ArrayList<ProgramFavoriteObject> mData = null;

        private Context mContext;

        private boolean[] mIsChannelLocked = null;

        public ChannelListAdapter(Context context, ArrayList<ProgramFavoriteObject> data,
                boolean[] isChannelLock) {
            mContext = context;
            mData = data;
            mIsChannelLocked = isChannelLock;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.block_program_item, null);
            TextView tmpText = (TextView) convertView
                    .findViewById(R.id.program_parentalcontrol_number);
            tmpText.setText(mData.get(position).getChannelId());
            tmpText = (TextView) convertView.findViewById(R.id.program_parentalcontrol_data);
            tmpText.setText(mData.get(position).getChannelName());
            ImageView lock = (ImageView) convertView.findViewById(R.id.is_pclocked);
            if (mIsChannelLocked[position]) {
                lock.setVisibility(View.VISIBLE);
            } else {
                lock.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private class ProgramFavoriteObject {
        private String channelId = null;

        private String channelName = null;

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
    }

    private void getListInfo() {
        ProgramInfo pgi = null;
        int indexBase = 0;
        int nServiceNum = 0;
        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        int curTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        if (curTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            TvIsdbChannelManager.getInstance().genMixProgList(false);
            nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        } else {
            if (currInputSource != TvCommonManager.INPUT_SOURCE_DTV) {
                nServiceNum = 0;
            } else {
                nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            }
        }
        mIsChannelLocked  = new boolean[nServiceNum];
        int nDtvChannelFiltered = 0;
        for (int k = indexBase; k < nServiceNum; k++) {
            if (curTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                pgi = TvAtscChannelManager.getInstance().getProgramInfo(k);
            } else if (curTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                pgi = TvIsdbChannelManager.getInstance().getProgramInfo(k);
            } else {
                pgi = mTvChannelManager.getProgramInfoByIndex(k);
            }
            if (pgi != null) {
                // Filter to delete and hide programs
                if ((false == pgi.isDelete) && (true == pgi.isVisible)) {
                    ProgramFavoriteObject pfo = new ProgramFavoriteObject();
                    if (curTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                        if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                            pfo.setChannelId(String.valueOf(pgi.number + 1));
                        } else {
                            String channum = pgi.majorNum + "." + pgi.minorNum;
                            pfo.setChannelId(channum);
                        }
                    } else {
                        pfo.setChannelId(String.valueOf(pgi.number));
                    }
                    pfo.setChannelName(pgi.serviceName);
                    mIsChannelLocked[nDtvChannelFiltered++] = pgi.isLock;
                    pfos.add(pfo);
                    progInfoList.add(pgi);
                }
            }
        }
        mIsChannelLocked = Arrays.copyOfRange(mIsChannelLocked, 0, nDtvChannelFiltered);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_program);
        proListView = (ListView) findViewById(R.id.block_program_list);
        TextView title = (TextView) findViewById(R.id.block_program_title);
        title.setText(R.string.block_program_title);
        mTvChannelManager = TvChannelManager.getInstance();
        getListInfo();
        adapter = new ChannelListAdapter(this, pfos, mIsChannelLocked);
        proListView.setAdapter(adapter);
        proListView.setDividerHeight(0);
        proListView.setSelection(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (proListView.getSelectedItemPosition() == 0) {
                proListView.setSelection(pfos.size() - 1);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (proListView.getSelectedItemPosition() == pfos.size() - 1) {
                proListView.setSelection(0);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(TvIntent.MAINMENU);
            intent.putExtra("currentPage", MainMenuActivity.LOCK_PAGE);
            intent.putExtra(Constant.PARENTAL_CONTROL_MENU_PERMITTED, true);
            if (intent.resolveActivity(BlockProgramActivity.this.getPackageManager()) != null) {
                startActivity(intent);
            }
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_PROG_GREEN) {
            int select = proListView.getSelectedItemPosition();
            ProgramInfo progInf = progInfoList.get(select);
            boolean islock = progInf.isLock;
            islock = !islock;
            progInf.isLock = islock;

            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_LOCK, progInf.number,
                    progInf.serviceType, 0x00, islock);
            mIsChannelLocked[select] = islock;
            adapter.notifyDataSetChanged();
        }
        return super.onKeyDown(keyCode, event);
    }

}
