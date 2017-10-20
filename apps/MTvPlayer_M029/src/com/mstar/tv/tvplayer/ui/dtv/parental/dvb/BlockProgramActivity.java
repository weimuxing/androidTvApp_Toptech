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
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.view.View.OnKeyListener;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.TimeOutHelper;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;
import com.mstar.util.Utility;
import android.os.SystemProperties;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.tv.tvplayer.ui.RootActivity;
public class BlockProgramActivity extends MstarBaseActivity {

    private static final String TAG = "BlockProgramActivity";
    private ListView proListView;
    private TextView block_button;
    private static int selectId = -1;

    private ArrayList<ProgramFavoriteObject> pfos = new ArrayList<ProgramFavoriteObject>();

    private ArrayList<ProgramInfo> progInfoList = new ArrayList<ProgramInfo>();

    private ChannelListAdapter adapter = null;

    private boolean[] mIsChannelLocked = null;

    private TvChannelManager mTvChannelManager = null;
	private int nServiceNum = 0;
	private TimeOutHelper timeOutHelper;
	private final static int TIME_OUT_MSG = 51;

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
            if(position == focusItem)
            {
                tmpText.setTextColor(0xFF0000FF);
            }
            tmpText = (TextView) convertView.findViewById(R.id.program_parentalcontrol_data);
            tmpText.setText(mData.get(position).getChannelName());
            if(position == focusItem)
            {
                tmpText.setTextColor(0xFF0000FF);
            }
            ImageView lock = (ImageView) convertView.findViewById(R.id.is_pclocked);
            if (mIsChannelLocked[position]) {
                lock.setVisibility(View.VISIBLE);
            } else {
                lock.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        private int focusItem=-1;
        public void setFocusItem(int focus)
        {
            focusItem=focus;
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
        
        int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        int curTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        if (curTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            TvIsdbChannelManager.getInstance().genMixProgList(false);
            nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        } else {
           // if (currInputSource != TvCommonManager.INPUT_SOURCE_DTV) {
           //     nServiceNum = 0;
           // } else {
                nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
           // }
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
                    	if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) //zb20160309 add
	                    	pfo.setChannelId(String.valueOf(pgi.number + 1));
	                    else
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

    private int getfocusIndex() {
        int focusIndex = 0;
        ProgramInfo cpi = mTvChannelManager.getCurrentProgramInfo();
        for (ProgramInfo pi: progInfoList) {
            if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
                if ((cpi.majorNum == pi.majorNum)
                    && (cpi.minorNum == pi.minorNum)
                    && (cpi.serviceType == pi.serviceType)) {
                    focusIndex = progInfoList.indexOf(pi);
                    break;
                }
            } else {
                if (cpi.number == pi.number) {
                    focusIndex = progInfoList.indexOf(pi);
                    break;
                }
            }
        }
        Log.d(TAG,"--getfocusIndex:" + focusIndex);
        return focusIndex;
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_program);
        proListView = (ListView) findViewById(R.id.block_program_list);
        TextView title = (TextView) findViewById(R.id.block_program_title);
        title.setText(R.string.block_program_title);
        block_button = (TextView) findViewById(R.id.block_button);
        mTvChannelManager = TvChannelManager.getInstance();
        getListInfo();
        adapter = new ChannelListAdapter(this, pfos, mIsChannelLocked);
        proListView.setAdapter(adapter);
        proListView.setDividerHeight(0);
        proListView.setSelection(getfocusIndex());
        adapter.setFocusItem(getfocusIndex());
        adapter.notifyDataSetChanged();
        selectId = proListView.getSelectedItemPosition();
        //add the inner class for channel changing in lock list
        proListView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) 
            {
            	timeOutHelper.reset();
                //Log.d(TAG, "onKey:" + keyCode);
                int select = proListView.getSelectedItemPosition();
                if (keyCode == KeyEvent.KEYCODE_ENTER && (keyEvent.getAction() == KeyEvent.ACTION_UP)) 
                {
                    //Log.d(TAG, "select:" + select);
                    if (select >= progInfoList.size()) {
                        return false;
                    }
                    adapter.setFocusItem(select);
                    adapter.notifyDataSetChanged();
                    ProgramInfo progInf = progInfoList.get(select);
                    ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
                    if ((curProgInfo.number == progInf.number)
                        && (curProgInfo.serviceType == progInf.serviceType)) 
                    {
                        //Log.d(TAG, "CH List :Select the same channel!!!");
                    } 
                    else 
                    {
                        if (progInf.serviceType < TvChannelManager.SERVICE_TYPE_INVALID) 
                        {
                            //if (TvChannelManager.SERVICE_TYPE_DTV == progInf.serviceType) {
                            if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                                Utility.channelSelectedDialog(BlockProgramActivity.this, progInf.number ,progInf.serviceType);
                                //Utility.channelSelect(BlockProgramActivity.this, progInf.number);
                            } 
                            else
                            {
                                mTvChannelManager.selectProgram(progInf.number, progInf.serviceType);
                            }
                        }
                    }
                    return true;
                }
                else 
                {
                    return false;
                }
            }
        });
        //end
        proListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			   	selectId= position;
				 ProgramInfo progInf = progInfoList.get(position);
                 ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
                 if ((curProgInfo.number == progInf.number)
                     && (curProgInfo.serviceType == progInf.serviceType)) 
                 {
                     //Log.d(TAG, "CH List :Select the same channel!!!");
                 } 
                 else 
                 {
                     if (progInf.serviceType < TvChannelManager.SERVICE_TYPE_INVALID) 
                     {
                         //if (TvChannelManager.SERVICE_TYPE_DTV == progInf.serviceType) {
                         if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                             Utility.channelSelectedDialog(BlockProgramActivity.this, progInf.number ,progInf.serviceType);
                             //Utility.channelSelect(BlockProgramActivity.this, progInf.number);
                         } 
                         else
                         {
                             mTvChannelManager.selectProgram(progInf.number, progInf.serviceType);
                         }
                     }
                 }
                 adapter.setFocusItem(position);
                 adapter.notifyDataSetChanged();
			}
		});
        block_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			 if (nServiceNum>0) {
		    	if(selectId == -1) selectId = proListView.getSelectedItemPosition();
		    	if(selectId == -1) return ;
	            ProgramInfo progInf = progInfoList.get(selectId);
	            boolean islock = progInf.isLock;
	            islock = !islock;
	            progInf.isLock = islock;
	            ProgramInfo curProgInf = mTvChannelManager.getCurrentProgramInfo();
	            if((!islock)&&(curProgInf.number == progInf.number)&&(curProgInf.serviceType == progInf.serviceType)){
	            	TvParentalControlManager.getInstance().unlockChannel();
	            }
	            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_LOCK, progInf.number,
	                    progInf.serviceType, 0x00, islock);
	            mIsChannelLocked[selectId] = islock;
	            adapter.notifyDataSetChanged();
			 }
			}
		});
        timeOutHelper = new TimeOutHelper(epgHandler, this);
    }

    private Handler epgHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
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
		// TODO Auto-generated method stub
		super.onResume();
        timeOutHelper.start();
        timeOutHelper.init();
	}
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	timeOutHelper.stop();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	timeOutHelper.reset();
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
       
        if (keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_BACK) {
            //zb20150213 add
            SystemProperties.set("persist.sys.isinparentmenu","true");
			//end
            Intent intent = new Intent(TvIntent.MAINMENU);
            intent.putExtra("currentPage", MainMenuActivity.LOCK_PAGE);
            intent.putExtra(Constant.PARENTAL_CONTROL_MENU_PERMITTED, true);
            if (intent.resolveActivity(BlockProgramActivity.this.getPackageManager()) != null) {
                startActivity(intent);
            }
            finish();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_PROG_GREEN && nServiceNum>0) {
            int select = proListView.getSelectedItemPosition();
            ProgramInfo progInf = progInfoList.get(select);
            boolean islock = progInf.isLock;
            islock = !islock;
            progInf.isLock = islock;
            ProgramInfo curProgInf = mTvChannelManager.getCurrentProgramInfo();
			/*delete by hz 20170829 for mantis :34491
            //add by wxy
            if((!islock)&&(curProgInf.number == progInf.number)&&(curProgInf.serviceType == progInf.serviceType)){
            	TvParentalControlManager.getInstance().unlockChannel();
            }
            //add end
            */
            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_LOCK, progInf.number,
                    progInf.serviceType, 0x00, islock);
            mIsChannelLocked[select] = islock;
            adapter.notifyDataSetChanged();
        }
        return super.onKeyDown(keyCode, event);
    }

	/**
     * Invoked by MstarBaseActivity in which send the 10s delay.
     */
    @Override
    public void onTimeOut() {
//        super.onTimeOut();
//        Intent mIntent = new Intent(this, RootActivity.class);
//        startActivity(mIntent);
//        finish();
    }
}
