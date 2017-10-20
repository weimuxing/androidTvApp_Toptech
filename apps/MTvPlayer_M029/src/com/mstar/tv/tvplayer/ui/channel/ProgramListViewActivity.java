//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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
import java.util.Locale;
import java.lang.Integer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.mstar.android.MKeyEvent;
import com.mstar.android.tvapi.common.vo.EnumServiceType;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.TimeOutHelper;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Utility;

import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tvapi.atv.AtvManager;
import com.mstar.android.tvapi.atv.vo.EnumSetProgramCtrl;
import com.mstar.android.tvapi.common.exception.TvCommonException;

public class ProgramListViewActivity extends MstarBaseActivity {

    private static final String TAG = "ProgramListViewActivity";

    private int mTvSystem = 0;

    private ListView proListView;

    private ArrayList<ProgramListViewItemObject> plvios = new ArrayList<ProgramListViewItemObject>();

    private ProgramListViewItemObject plvioTmp = new ProgramListViewItemObject();

    private ArrayList<ProgramInfo> progInfoList = new ArrayList<ProgramInfo>();

    private ProgramEditAdapter mProgramEditAdapter = null;

    private EditText input = null;

	private TextView programEdit_title = null;

    private boolean moveFlag = false;

    private boolean moveble = false;

    private int moveKeyCount = 0;

    private int position;

    private int pageSize = 10;

    private int currutPage = 1;

    private int m_u32Source = 0;

    private int m_u32Target = 0;

    private int m_nServiceNum = 0;

    private int mDtvDelOrHideNum = 0;

	private int AtvNum = 0;

	private int isMoving = 0;

    private ImageView ImgSkip;

    //add by wym 20150803
    private boolean mDisableMoveFunctionInDtv = false;
    private boolean mDisableMoveFunctionInAtv = false;

    // Remove Edit key cause we have only 4 colored keys in K3.
    private ImageView ImgEdit;

    private ImageView ImgFavorite;

    private ImageView ImgDelete;

    private ImageView ImgMove;

    private ImageView ImgLock;

    // Remove Edit key cause we have only 4 colored keys in K3.
    private TextView textEdit;

    private TextView textFavorite;

    private TextView textDelete;

    private TextView textMove;

    private TextView textSkip;

    private TextView textLock;

    private TimeOutHelper timeOutHelper;

    private TvChannelManager mTvChannelManager = null;

    private TvCommonManager mTvCommonManager = null;
    
    private TvAtscChannelManager mTvAtscChannelManager = null;

    private OnDtvPlayerEventListener mDtvEventListener = null;

    private boolean del_dialog_flag = false;//add by wxy

    private int selectId = 0;

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
                ProgramListViewActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        RefreshContent();
                    }
                });
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
            return false;
        }

        @Override
        public boolean onUiOPExitServiceList(int what) {
            return false;
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == TimeOutHelper.getTimeOutMsg()) {
                finish();
            }
        }
    };

    /**
     * @param keyCode
     * @param selItem
     * @return
     */
    boolean checkChmoveble(int keyCode, int selItem) {
        ProgramInfo cur = null;
        ProgramInfo next = null;
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (selItem >= (progInfoList.size() - 1)) {
                return false;
            }
            cur = progInfoList.get(selItem);
            next = progInfoList.get(selItem + 1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (selItem == 0) {
                return false;
            }
            cur = progInfoList.get(selItem);
            next = progInfoList.get(selItem - 1);
        }
        if (cur.serviceType == next.serviceType) {
            return true;
        } else {
            return false;
        }
    }

    private android.view.View.OnClickListener channelManagerClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			timeOutHelper.start();
            timeOutHelper.init();
			switch (view.getId()) {
            case R.id.program_edit_str_edit:
                channelEdit();
                break;

            case R.id.program_edit_str_skip:
                channelSkip();
                break;

            case R.id.program_edit_str_delete:
                if((!moveFlag) && plvios.size() != 0)showDialog(ProgramListViewActivity.this);
                break;

            case R.id.program_edit_str_favorite:
                channelFavorite();
                break;
            case R.id.program_edit_str_move:
				channelMove();
                break;
            default:
                break;
        }

		}
    };

    private Boolean channelMove(){
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            return true;
        }
        /**add to disable move operation in ATV.lxk 20141210***//*
        int selItem = (int) proListView.getSelectedItemId();
        ProgramInfo ProgInfo = progInfoList.get(selItem);
        //Log.i(TAG,"selItem:" + selItem + " with ProgInfo.serviceType:" + ProgInfo.serviceType);
        if (ProgInfo.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
            return true;
        }
        end*/
        if (plvios.size() == 0) // || ImgMove.getVisibility() == View.GONE)
            return true;
        moveFlag = !moveFlag;
        setMoveTip(moveFlag);
        position = (int) proListView.getSelectedItemId();
        if (position >= plvios.size()) {
            return false;
        }
        swapObject(plvioTmp, plvios.get(position));
        if (moveFlag) {
            m_u32Source = position;
        } else {
            m_u32Target = position;
            Log.i(TAG,"source:" + m_u32Source + " vs target:" + m_u32Target);
            Log.i(TAG,"mDtvDelOrHideNum:" + mDtvDelOrHideNum);
            if (m_u32Source != m_u32Target) {
                /* Since DTV index will be recompute to real index under layer but ATV, so we have to consider the DTV programs that be deleted and hidden DTV when move ATV channels.
                   E.g.
                   DTV1 DTV2 DTV3 ATV1 ATV2 ATV3
                   delete DTV2   =>DTV1 (DTV2) DTV3 ATV1 ATV2 ATV3
                   Parentheses () is invisible but actually exists
                   so, move ATV1 to ATV2,moveProgram() parameters should be 3 4,not 2 3
                 */
                if (TvChannelManager.SERVICE_TYPE_DTV == progInfoList.get(m_u32Source).serviceType) {
                    mTvChannelManager.moveProgram(m_u32Source, m_u32Target);
                } else {
                    mTvChannelManager.moveProgram(m_u32Source + mDtvDelOrHideNum, m_u32Target + mDtvDelOrHideNum);
                }
                RefreshContent();
            }
            if (progInfoList.size() > 0) {
                if (m_u32Target >= progInfoList.size()) {
                    return false;
                }
                ProgramInfo ProgInf = progInfoList.get(m_u32Target);
                mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
                if (ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_DTV) {
                    mTvChannelManager.playDtvCurrentProgram();
                }
                mProgramEditAdapter.setFocusItem(m_u32Target);
                mProgramEditAdapter.notifyDataSetChanged();
            }
        }
        return true;

    }

    private Boolean channelFavorite(){
    	 //int selItemIndex = (int) proListView.getSelectedItemId();
    	 int selItemIndex = selectId;
        if (selItemIndex >= progInfoList.size()) {
            return false;
        }
        ProgramInfo ProgInf = progInfoList.get(selItemIndex);
        short bfav = ProgInf.favorite;

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            if (bfav == 0) {
                bfav = 1;
                mTvChannelManager.addProgramToFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                    ProgInf.majorNum, ProgInf.minorNum, ProgInf.progId);
            } else {
                bfav = 0;
                mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                    ProgInf.majorNum, ProgInf.minorNum, ProgInf.progId);
            }
        } else {
            if (bfav == 0) {
                bfav = 1;
                mTvChannelManager.addProgramToFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                        ProgInf.number, ProgInf.serviceType, 0x00);
            } else {
                bfav = 0;
                mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                        ProgInf.number, ProgInf.serviceType, 0x00);
            }
        }
        ProgInf.favorite = bfav;
        if (selItemIndex >= plvios.size()) {
            return false;
        }
        if (bfav != 0) {
        	(plvios.get(selItemIndex)).setSkipImg(false);
            (plvios.get(selItemIndex)).setFavoriteImg(true);
        } else {
        	(plvios.get(selItemIndex)).setSkipImg(false);
            (plvios.get(selItemIndex)).setFavoriteImg(false);
        }
        position = (int) proListView.getSelectedItemId();
        // swapObject(plvioTmp, plvios.get(position));
        //add by wxy
        if(ProgInf.isSkip)
        {
            ProgInf.isSkip = false;
            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.number,
                    ProgInf.serviceType, 0x00, false);
        }
        //add end

        mProgramEditAdapter.notifyDataSetChanged();
        proListView.invalidate();
        return true;

    }

    private Boolean channelSkip(){
//    	int selItemIndex = (int) proListView.getSelectedItemId();
    	int selItemIndex = selectId;
        if (selItemIndex >= progInfoList.size()) {
            return false;
        }
        ProgramInfo ProgInf = progInfoList.get(selItemIndex);
        boolean bSkip = ProgInf.isSkip;
        bSkip = !bSkip;
        ProgInf.isSkip = bSkip;
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.majorNum,
                ProgInf.minorNum, ProgInf.progId, bSkip);
        } else {
            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.number,
                ProgInf.serviceType, 0x00, bSkip);
        }
        if (selItemIndex >= plvios.size()) {
            return false;
        }
        if (bSkip) {
        	plvios.get(selItemIndex).setFavoriteImg(false);
            plvios.get(selItemIndex).setSkipImg(true);
        } else {
        	plvios.get(selItemIndex).setFavoriteImg(false);
            plvios.get(selItemIndex).setSkipImg(false);
        }
        // swapObject(plvioTmp, plvios.get(position));

        //add by wxy
        if(ProgInf.favorite==1)
        {
            ProgInf.favorite = 0;
            mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                    ProgInf.number, ProgInf.serviceType, 0x00);
            //add end
        }
        mProgramEditAdapter.notifyDataSetChanged();
        proListView.invalidate();
        return true;


    }

    private Boolean channelEdit(){
        //int selItemIndex = (int) proListView.getSelectedItemId();
        int selItemIndex = selectId;
        if (selItemIndex >= progInfoList.size()) {
            return false;
        }
        ProgramInfo ProgInf = progInfoList.get(selItemIndex);

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            //ISDB not support program edit both ATV & DTV
            return false;
        }

//        if (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC) {
//            //Log.i(TAG,"selItemIndex:" + selItemIndex + " with ProgInf.serviceType:" + ProgInf.serviceType);
//            if (ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_DTV
//		||ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_RADIO
//		||ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_DATA) {
//                return false;
//            }
//        }
        // Add : will not do onPause
        timeOutHelper.stop();
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.program_dialog_edit_text, null);
        new AlertDialog.Builder(this)
                .setTitle(R.string.str_program_edit_dialog_input)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(textEntryView)
                .setPositiveButton(R.string.str_program_edit_dialog_ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //int selItemIndex = (int) proListView.getSelectedItemId();
                    	int selItemIndex = selectId;
                        timeOutHelper.start();
                        timeOutHelper.init();
                        if (selItemIndex >= progInfoList.size()) {
                            return;
                        }
                        if (selItemIndex >= plvios.size()) {
                            return;
                        }
                        ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                        input = (EditText) textEntryView.findViewById(R.id.program_edit_text);
                        String Tvmame = input.getText().toString();
                        String finalName = splitString(Tvmame, 27);// sn:MAX_STATION_NAME=30
                        //add by wxy
                        if("".equals(finalName))
                        {
                        	Toast.makeText(ProgramListViewActivity.this, R.string.edit_program_toast,Toast.LENGTH_SHORT).show();
                        }
                        //add end
                        else
                        {
                        (plvios.get(selItemIndex)).setTvName(finalName);
                        mProgramEditAdapter.notifyDataSetChanged();
                        proListView.invalidate();
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                            mTvAtscChannelManager.setProgramName(ProgInf.majorNum, ProgInf.minorNum, finalName);
                        } else {
                            mTvChannelManager.setProgramName(ProgInf.number, ProgInf.serviceType, finalName);
                        }
                        }
                    }
                })
                .setNegativeButton(R.string.str_program_edit_dialog_cancel,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                timeOutHelper.start();
                                timeOutHelper.init();
                            }
                        }).show();// show this for atv program
        return true;

    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        if (getApp().isCusOnida()) {
        	setContentView(R.layout.program_list_view_onida);
		}else
			setContentView(R.layout.program_list_view);
        ImgSkip = (ImageView) findViewById(R.id.program_edit_img_skip);
        ImgEdit = (ImageView) findViewById(R.id.program_edit_img_edit);
        ImgFavorite = (ImageView) findViewById(R.id.program_edit_img_favorite);
        ImgDelete = (ImageView) findViewById(R.id.program_edit_img_delete);
        ImgMove = (ImageView) findViewById(R.id.program_edit_img_move);
        ImgLock = (ImageView) findViewById(R.id.program_edit_img_lock);
		programEdit_title=(TextView) findViewById(R.id.program_edit_title);
        textEdit = (TextView) findViewById(R.id.program_edit_str_edit);
        textFavorite = (TextView) findViewById(R.id.program_edit_str_favorite);
        textDelete = (TextView) findViewById(R.id.program_edit_str_delete);
        textMove = (TextView) findViewById(R.id.program_edit_str_move);
        textSkip = (TextView) findViewById(R.id.program_edit_str_skip);
        textLock = (TextView) findViewById(R.id.program_edit_str_lock);
        proListView = (ListView) findViewById(R.id.program_edit_list_view);
        mTvCommonManager = TvCommonManager.getInstance();
        if(mTvCommonManager.isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)){
        	mDisableMoveFunctionInAtv = true;
        	ImgMove.setVisibility(View.GONE);
            textMove.setVisibility(View.GONE);
        }else if(mTvCommonManager.isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)){
        	mDisableMoveFunctionInAtv = false;
        	ImgMove.setVisibility(View.VISIBLE);
            textMove.setVisibility(View.VISIBLE);
        }
        proListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				selectId = pos;
                if (selectId >= progInfoList.size()) {
                    return ;
                }
                ProgramInfo ProgInf = progInfoList.get(selectId);
                ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if ((curProgInfo.majorNum == ProgInf.majorNum)
                        && (curProgInfo.minorNum == ProgInf.minorNum)
                        && (curProgInfo.serviceType == ProgInf.serviceType)) {
                        Log.d(TAG, "ProList:Select the same channel!!!");
                    } else {
                        mTvAtscChannelManager.programSel(ProgInf.majorNum, ProgInf.minorNum);
                    }
                } else {
                    if ((curProgInfo.number == ProgInf.number)
                        && (curProgInfo.serviceType == ProgInf.serviceType)) {
                        Log.d(TAG, "ProList:Select the same channel!!!");
                    } else {
                        mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
                      //zb20141029 add
                        mProgramEditAdapter.setFocusItem(selectId);
                        mProgramEditAdapter.notifyDataSetChanged();
                        //end
                    }
                }
			}
		});
        textEdit.setOnClickListener(channelManagerClick);
        textDelete.setOnClickListener(channelManagerClick);
        textFavorite.setOnClickListener(channelManagerClick);
        textSkip.setOnClickListener(channelManagerClick);
        textMove.setOnClickListener(channelManagerClick);

        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            ImgMove.setVisibility(View.GONE);
            textMove.setVisibility(View.GONE);
        } else {
            ImgLock.setVisibility(View.GONE);
            textLock.setVisibility(View.GONE);
        }

        //zb20141009 add
        if(Locale.getDefault().getLanguage().equals("zh"))
        	ImgFavorite.setBackgroundResource(R.drawable.chinese_fav);
        else
        	ImgFavorite.setBackgroundResource(R.drawable.english_fav);
        //end    //removing the image icon for M029 OSD channelmanager.lxk 20150504
        mTvChannelManager = TvChannelManager.getInstance();
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvAtscChannelManager = TvAtscChannelManager.getInstance();
        }
        getProgList();
        mProgramEditAdapter = new ProgramEditAdapter(this, plvios);
        proListView.setAdapter(mProgramEditAdapter);
        proListView.setDividerHeight(0);
        proListView.setSelection(getfocusIndex());
        //add for marked display of the current channel.lxk 20141209
        mProgramEditAdapter.setFocusItem(getfocusIndex());
        mProgramEditAdapter.notifyDataSetChanged();
        //end
        //add by hz to Hide Move fuction when only one  ATV/DTV program
		if((isOnlyOneDTV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_DTV)||
			(isOnlyOneATV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_ATV)){
			ImgMove.setVisibility(View.GONE);
	        textMove.setVisibility(View.GONE);
			mDisableMoveFunctionInDtv=true;
			mDisableMoveFunctionInAtv=true;
		}
		//end
        if (!progInfoList.isEmpty()) {
            int selItemIndex = (int) proListView.getSelectedItemId();
            ProgramInfo ProgInf = progInfoList.get(selItemIndex);
            /*
             * Edit by gerard.jiang for "0380586" in 2013/04/18 Change ATV to
             * DTV.
             */
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                ImgEdit.setVisibility(View.VISIBLE);
                textEdit.setVisibility(View.VISIBLE);
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                //ISDB not support program edit both ATV & DTV
                ImgEdit.setVisibility(View.GONE);
                textEdit.setVisibility(View.GONE);
            } else {
                if (ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                    ImgEdit.setVisibility(View.VISIBLE);
                    textEdit.setVisibility(View.VISIBLE);
                    if (mDisableMoveFunctionInAtv)
                    {
	                    ImgMove.setVisibility(View.GONE);
	                    textMove.setVisibility(View.GONE);
                    }
                    else
                    {
					    ImgMove.setVisibility(View.VISIBLE);
	                    textMove.setVisibility(View.VISIBLE);
                    }
                    //end
                } else {
                    ImgEdit.setVisibility(View.VISIBLE);
                    textEdit.setVisibility(View.VISIBLE);
                   /* if (mDisableMoveFunctionInDtv)
                    {
	                    ImgMove.setVisibility(View.GONE);
	                    textMove.setVisibility(View.GONE);
                    }
                    else
                    {
					    ImgMove.setVisibility(View.VISIBLE);
	                    textMove.setVisibility(View.VISIBLE);
                    }*/
                }
            }
        }
        proListView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                Log.i(TAG,"onkey:" + keyCode);
                timeOutHelper.reset();
                // KEYCODE_PROG_RED use for KEYCODE_DEL
                // KEYCODE_PROG_GREEN use for KEYCODE_E
                // KEYCODE_PROG_YELLOW use for KEYCODE_M
                // KEYCODE_PROG_BLUE use for KEYCODE_S
                int selItemIndex = (int) proListView.getSelectedItemId();
                /** start modified by jachensy.chen 2012-6-27 */
                if (((keyCode == KeyEvent.KEYCODE_DPAD_UP) && !moveFlag && (keyEvent.getAction() == KeyEvent.ACTION_UP))
                        || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && !moveFlag && (keyEvent
                                .getAction() == KeyEvent.ACTION_UP))) {
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    /*
                     * Edit by gerard.jiang for "0380586" in 2013/04/18 Change
                     * ATV to DTV.
                     */
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        ImgEdit.setVisibility(View.VISIBLE);
                        textEdit.setVisibility(View.VISIBLE);
                    } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                        //ISDB not support program edit both ATV & DTV
                        ImgEdit.setVisibility(View.GONE);
                        textEdit.setVisibility(View.GONE);
                    } else {
                        if (ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                            ImgEdit.setVisibility(View.VISIBLE);
                            textEdit.setVisibility(View.VISIBLE);
							if ( AtvNum <2 ) {
								mDisableMoveFunctionInAtv = true;
							}
                            if (mDisableMoveFunctionInAtv)
		                    {
			                    ImgMove.setVisibility(View.GONE);
			                    textMove.setVisibility(View.GONE);
		                    }
		                    else
		                    {
							    ImgMove.setVisibility(View.VISIBLE);
			                    textMove.setVisibility(View.VISIBLE);
		                    }
                            
                        } else {
                            ImgEdit.setVisibility(View.VISIBLE);
                            textEdit.setVisibility(View.VISIBLE);
                            
                           /* if (mDisableMoveFunctionInDtv)
		                    {
			                    ImgMove.setVisibility(View.GONE);
			                    textMove.setVisibility(View.GONE);
		                    }
		                    else
		                    {
							    ImgMove.setVisibility(View.VISIBLE);
			                    textMove.setVisibility(View.VISIBLE);
		                    }*/
                        }
                    }
                }
                /** end modified by jachensy.chen 2012-6-27 */
                if (((keyCode == KeyEvent.KEYCODE_DPAD_UP) && moveFlag && (keyEvent.getAction() == KeyEvent.ACTION_DOWN))
                        || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && moveFlag && (keyEvent
                                .getAction() == KeyEvent.ACTION_DOWN))) {
                    if (checkChmoveble(keyCode, selItemIndex)) {
                        moveble = true;
                    } else {
                        moveble = false;
                        return true;
                    }
                }
                if (((keyCode == KeyEvent.KEYCODE_DPAD_UP) && moveFlag && (keyEvent.getAction() == KeyEvent.ACTION_UP))
                        || (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && moveble && (keyEvent
                                .getAction() == KeyEvent.ACTION_UP))) {
                    if (moveFlag) {
                        if ((position >= plvios.size()) || (selItemIndex >= plvios.size())) {
                            return false;
                        }
                        swapObject(plvios.get(position), plvios.get(selItemIndex));
                        swapObject(plvios.get(selItemIndex), plvioTmp);
                        position = selItemIndex;
						setCurProgramFocus(keyCode);
                        //mProgramEditAdapter.setFocusItem(getfocusItem());
                        mProgramEditAdapter.notifyDataSetChanged();
                        proListView.invalidate();
                        return true;
                    } else {
                        return true;
                    }
                }
                if (moveFlag) {
                    Log.i(TAG,"moveFalg true, return");
                    /*modify for pressing enter key to end moving.lxk 20150109*/
                    if(keyCode == KeyEvent.KEYCODE_ENTER)
                    {
                        moveFlag = false;
                        setMoveTip(moveFlag);
                        position = (int) proListView.getSelectedItemId();
                        if (position >= plvios.size()) {
                            return false;
                        }
                        swapObject(plvioTmp, plvios.get(position));
                        m_u32Target = position;
                        if (m_u32Source != m_u32Target) {
                            /* Since DTV index will be recompute to real index under layer but ATV, so we have to consider the DTV programs that be deleted and hidden DTV when move ATV channels.
                            E.g.
                            DTV1 DTV2 DTV3 ATV1 ATV2 ATV3
                            delete DTV2   =>DTV1 (DTV2) DTV3 ATV1 ATV2 ATV3
                            Parentheses () is invisible but actually exists
                            so, move ATV1 to ATV2,moveProgram() parameters should be 3 4,not 2 3
                            */
                            if (TvChannelManager.SERVICE_TYPE_DTV == progInfoList.get(m_u32Source).serviceType) {
                                mTvChannelManager.moveProgram(m_u32Source, m_u32Target);
                            } else {
                                mTvChannelManager.moveProgram(m_u32Source + mDtvDelOrHideNum, m_u32Target + mDtvDelOrHideNum);
                            }
							setCurProgramFocus(keyCode);
                            RefreshContent();
                        }
                        if (progInfoList.size() > 0) {
                            if (m_u32Target >= progInfoList.size()) {
                                return false;
                            }
                            ProgramInfo ProgInf = progInfoList.get(m_u32Target);
                            mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
                            if (ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_DTV) {
                                mTvChannelManager.playDtvCurrentProgram();
                            }
                            mProgramEditAdapter.setFocusItem(m_u32Target);
							setCurProgramFocus(keyCode);
                            mProgramEditAdapter.notifyDataSetChanged();
                        }
                    }
                    /*end*/
                    return false;
                }
                // by wangshangwen 20160712
                
                if ((keyCode == MKeyEvent.KEYCODE_FAVORITE || keyCode == MKeyEvent.KEYCODE_TV_SUBCODE)
                        && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    short bfav = ProgInf.favorite;
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        if (bfav == 0) {
                            bfav = 1;
                            mTvChannelManager.addProgramToFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                ProgInf.majorNum, ProgInf.minorNum, ProgInf.progId);
                        } else {
                            bfav = 0;
                            mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                ProgInf.majorNum, ProgInf.minorNum, ProgInf.progId);
                        }
                    } else {
                        if (bfav == 0) {
                            bfav = 1;
                            mTvChannelManager.addProgramToFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                    ProgInf.number, ProgInf.serviceType, 0x00);
                        } else {
                            bfav = 0;
                            mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
                                    ProgInf.number, ProgInf.serviceType, 0x00);
                        }
                    }
                    ProgInf.favorite = bfav;
                    if (selItemIndex >= plvios.size()) {
                        return false;
                    }
                    if (bfav != 0) {
                    	(plvios.get(selItemIndex)).setSkipImg(false);
                        (plvios.get(selItemIndex)).setFavoriteImg(true);
                    } else {
                    	(plvios.get(selItemIndex)).setSkipImg(false);
                        (plvios.get(selItemIndex)).setFavoriteImg(false);
                    }
                    position = (int) proListView.getSelectedItemId();
                    // swapObject(plvioTmp, plvios.get(position));
                    //add by wxy
                    if(ProgInf.isSkip)
                    {
	                    ProgInf.isSkip = false;
	                    mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.number,
	                            ProgInf.serviceType, 0x00, false);
                    }
                    //add end

                    mProgramEditAdapter.notifyDataSetChanged();
                    proListView.invalidate();
                    return true;
                } else if ((keyCode == KeyEvent.KEYCODE_L && (keyEvent.getAction() == KeyEvent.ACTION_UP))
                        || (keyCode == MKeyEvent.KEYCODE_MSTAR_HOLD && (keyEvent.getAction() == KeyEvent.ACTION_UP))) {
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    if (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    boolean block = ProgInf.isLock;
                    block = !block;
                    ProgInf.isLock = block;
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_LOCK, ProgInf.majorNum,
                            ProgInf.minorNum, ProgInf.progId, block);
                    } else {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_LOCK, ProgInf.number,
                            ProgInf.serviceType, 0x00, block);
                    }
                    if (block) {
                        plvios.get(selItemIndex).setLockImg(true);
                    } else {
                        plvios.get(selItemIndex).setLockImg(false);
                    }
                    // swapObject(plvioTmp, plvios.get(position));
                    mProgramEditAdapter.notifyDataSetChanged();
                    proListView.invalidate();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_PROG_BLUE
                        && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    boolean bSkip = ProgInf.isSkip;
                    bSkip = !bSkip;
                    ProgInf.isSkip = bSkip;
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.majorNum,
                            ProgInf.minorNum, ProgInf.progId, bSkip);
                    } else {
                        mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_SKIP, ProgInf.number,
                            ProgInf.serviceType, 0x00, bSkip);
                    }
                    if (selItemIndex >= plvios.size()) {
                        return false;
                    }
                    if (bSkip) {
                    	plvios.get(selItemIndex).setFavoriteImg(false);
                        plvios.get(selItemIndex).setSkipImg(true);
                    } else {
                    	plvios.get(selItemIndex).setFavoriteImg(false);
                        plvios.get(selItemIndex).setSkipImg(false);
                    }
                    // swapObject(plvioTmp, plvios.get(position));

                    //add by wxy
                    if(ProgInf.favorite==1)
                    {
	                    ProgInf.favorite = 0;
	                    mTvChannelManager.deleteProgramFromFavorite(TvChannelManager.PROGRAM_FAVORITE_ID_1,
	                            ProgInf.number, ProgInf.serviceType, 0x00);
	                    //add end
                    }
                    mProgramEditAdapter.notifyDataSetChanged();
                    proListView.invalidate();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER
                        && (keyEvent.getAction() == KeyEvent.ACTION_UP)) {
                	selectId = selItemIndex ;
                    if (selItemIndex >= progInfoList.size()) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                    ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
                    if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                        if ((curProgInfo.majorNum == ProgInf.majorNum)
                            && (curProgInfo.minorNum == ProgInf.minorNum)
                            && (curProgInfo.serviceType == ProgInf.serviceType)) {
                            Log.d(TAG, "ProList:Select the same channel!!!");
                        } else {
                            mTvAtscChannelManager.programSel(ProgInf.majorNum, ProgInf.minorNum);
                        }
                    } else {
                        if ((curProgInfo.number == ProgInf.number)
                            && (curProgInfo.serviceType == ProgInf.serviceType)) {
                            Log.d(TAG, "ProList:Select the same channel!!!");
                        } else {
                            mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
							RefreshContent();//add by hz
                          //zb20141029 add
                            mProgramEditAdapter.setFocusItem(selItemIndex);
                            mProgramEditAdapter.notifyDataSetChanged();
                            //end
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        timeOutHelper = new TimeOutHelper(handler, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
		//add by hz to Hide Move fuction when only one  ATV/DTV program
		if((isOnlyOneDTV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_DTV)||
			(isOnlyOneATV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_ATV)){
			ImgMove.setVisibility(View.GONE);
	        textMove.setVisibility(View.GONE);
			mDisableMoveFunctionInDtv=true;
			mDisableMoveFunctionInAtv=true;
		}
		//end
		 if (getApp().isCusOnida()) {
        	programEdit_title.setText(R.string.str_cha_channelmanager);
		}
        selectId = getfocusIndex();
        timeOutHelper.start();
        timeOutHelper.init();
        mDtvEventListener = new DtvEventListener();
        mTvChannelManager.registerOnDtvPlayerEventListener(mDtvEventListener);
    };

    @Override
    protected void onPause() {
        mTvChannelManager.unregisterOnDtvPlayerEventListener(mDtvEventListener);
        mDtvEventListener = null;
        timeOutHelper.stop();
        super.onPause();
    }

	//add by hz to count ATV/DTV
	private boolean isOnlyOneATV_Prog(){
		int ATV_NUM=0;
		for(int i=0;i<plvios.size();i++){
			if(plvios.get(i).getServiceType() == TvChannelManager.SERVICE_TYPE_ATV)
				ATV_NUM++;
		}
		if(ATV_NUM<=1)
			return true;
		return false;
	}
	private boolean isOnlyOneDTV_Prog(){
		int DTV_NUM=0;
		for(int i=0;i<plvios.size();i++){
			if(plvios.get(i).getServiceType() == TvChannelManager.SERVICE_TYPE_DTV)
				DTV_NUM++;
		}
		if(DTV_NUM<=1)
			return true;
		return false;
	}
	//end

    //add by wxy

    private boolean showDialog(Context context)
    {
    	LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(
                R.layout.exittuninginfo_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
       // builder.setIcon(R.drawable.icon);
        builder.setTitle(getString(R.string.str_delete_program_title));
        //builder.setView(textEntryView);
        builder.setPositiveButton(getString(R.string.str_stop_record_dialog_confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	del_dialog_flag = delete_program();
                    	
                    }
                });
        builder.setNegativeButton(getString(R.string.str_stop_record_dialog_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	del_dialog_flag = false;
                    }
                });
        builder.show();
        return del_dialog_flag;
    }



    protected boolean delete_program()
    {
        //int selItemIndex = (int) proListView.getSelectedItemId();
    	//add by wxy
        int selItemIndex = (int) proListView.getSelectedItemId();
        //add end
        Log.d(TAG,"selItemIndex:" + selItemIndex);
        if (selItemIndex >= progInfoList.size()) {
            return false;
        }
        ProgramInfo selProgInfo = progInfoList.get(selItemIndex);
        ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
        ProgramInfo selProg = null;
        Log.d(TAG,"selNo.:" + selProgInfo.number + " vs curNo.:" + curProgInfo.number);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_DELETE, selProgInfo.majorNum,
                selProgInfo.minorNum, selProgInfo.progId, true);
        } else {
            mTvChannelManager.setProgramAttribute(TvChannelManager.PROGRAM_ATTRIBUTE_DELETE, selProgInfo.number,
                selProgInfo.serviceType, 0x00, true);
        }
        //int iProgcount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        //int iATVcount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
        //int iDTVcount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        //Log.d(TAG,"iProgcount:" + iProgcount + " iATVcount:" + iATVcount + " iDTVcount:" + iDTVcount);
        if ((curProgInfo.number == selProgInfo.number) && (curProgInfo.serviceType == selProgInfo.serviceType))
        {
            if (EnumServiceType.E_SERVICETYPE_ATV.ordinal() == curProgInfo.serviceType)
            {
                if(selItemIndex<progInfoList.size()-1)//not last prog
                {
                    selProg = progInfoList.get(selItemIndex );
                    Log.d(TAG,"mux ----1----selProg num:" + selProg.number);
                    mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                    proListView.setSelection(selItemIndex);
                    mProgramEditAdapter.setFocusItem(selItemIndex);
                }
                else if(selItemIndex>=1)//last prog and not first in list
                {
                    selProg = progInfoList.get(selItemIndex - 1);
                    Log.d(TAG,"mux ----selProg num:" + selProg.number + " selProg num:" + selProg.serviceType);
			if(curProgInfo.serviceType == selProg.serviceType)
                    {
                        mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                        proListView.setSelection(selItemIndex - 1);
                        mProgramEditAdapter.setFocusItem(selItemIndex - 1);
                    }
			else
                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        	TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
                else//last and first in list, only one prog now
                {
                    Log.d(TAG,"----3----");
                    mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
            }
            else if ((EnumServiceType.E_SERVICETYPE_DTV.ordinal() == curProgInfo.serviceType)
                ||(EnumServiceType.E_SERVICETYPE_RADIO.ordinal() == curProgInfo.serviceType)
                ||(EnumServiceType.E_SERVICETYPE_DATA.ordinal() == curProgInfo.serviceType))
            {
                if(selItemIndex<progInfoList.size()-1)
                {
                    selProg = progInfoList.get(selItemIndex + 1);//remove the plus 1,as the channel number has been rearranged already.lxk 20150205
                    Log.d(TAG,"--1--selProg num:" + selProg.number + " selProg service:" + selProg.serviceType);
			if(EnumServiceType.E_SERVICETYPE_ATV.ordinal()  == selProg.serviceType)
                    {
                        if(selItemIndex > 0)
                        {
                            selProg = progInfoList.get(selItemIndex - 1);
                            Log.d(TAG,"--2--selProg num:" + selProg.number + " selProg num:" + selProg.serviceType);
                            mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                            proListView.setSelection(selItemIndex - 1);
                            mProgramEditAdapter.setFocusItem(selItemIndex - 1);
                        }
                        else
                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        	TvChannelManager.FIRST_SERVICE_DEFAULT);
                        Log.d(TAG,"--2--selProg num:" + selProg.number + " selProg ser:" + selProg.serviceType);
                    }
                    else
                    {
                        selProg = progInfoList.get(selItemIndex + 1);
                        Log.d(TAG,"--selProg current num:" + selProg.number + " selProg service:" + selProg.serviceType);
                        mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                        proListView.setSelection(selItemIndex);
                        mProgramEditAdapter.setFocusItem(selItemIndex);
                    }
                }
                else if(selItemIndex > 0)
                {
                    selProg = progInfoList.get(selItemIndex - 1);
                    Log.d(TAG,"--3--selProg num:" + selProg.number + " selProg num:" + selProg.serviceType);
                    mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                    proListView.setSelection(selItemIndex - 1);
                    mProgramEditAdapter.setFocusItem(selItemIndex - 1);
                }
                else
                	{
                    mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
					
                	}
            }
        }
		else
		{
			if ((curProgInfo.serviceType == selProgInfo.serviceType)
					&&(curProgInfo.serviceType == EnumServiceType.E_SERVICETYPE_ATV.ordinal())){
				//20160721,add by hz for mantis:0019573
				if (EnumServiceType.E_SERVICETYPE_ATV.ordinal() == curProgInfo.serviceType)
            	{
                	if(selItemIndex<progInfoList.size()-1)//not last prog
                	{	
                    	selProg = progInfoList.get(selItemIndex );
                    	Log.d(TAG,"mux 1----1----selProg num:" + selProg.number);
                        //RefreshContent();
                    	//mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                    	mProgramEditAdapter.notifyDataSetChanged();
                        RefreshContent();
                    	proListView.setSelection(selItemIndex);
            			if(curProgInfo.number > selProgInfo.number)
            			{
            				mTvChannelManager.selectProgram(curProgInfo.number-1, selProg.serviceType);
            			}
                    	mProgramEditAdapter.setFocusItem(selItemIndex);
                	}
                	else if(selItemIndex>0)//last prog and not first in list
                	{
                    	selProg = progInfoList.get(selItemIndex - 1);
                    	Log.d(TAG,"mux 2----selProg num:" + selProg.number + " selProg num:" + selProg.serviceType);
						if(curProgInfo.serviceType == selProg.serviceType)
                    	{
                        	//mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                        	proListView.setSelection(selItemIndex - 1);
                        	//mProgramEditAdapter.setFocusItem(selItemIndex - 1);
                    	}
						else
                        	mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        		TvChannelManager.FIRST_SERVICE_DEFAULT);
                	}
                	else//last and first in list, only one prog now
                	{
                	
                    	Log.d(TAG,"----3----");
                    	mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        	TvChannelManager.FIRST_SERVICE_DEFAULT);
                	}
				}
				//end hz 20160721
			}
			else if ((EnumServiceType.E_SERVICETYPE_DTV.ordinal() == curProgInfo.serviceType)//hexing20160615 add for del dtv last channel set selelct dtv
                ||(EnumServiceType.E_SERVICETYPE_RADIO.ordinal() == curProgInfo.serviceType)
                ||(EnumServiceType.E_SERVICETYPE_DATA.ordinal() == curProgInfo.serviceType))
            	{
                if(selItemIndex<progInfoList.size()-1)
                {
                    selProg = progInfoList.get(selItemIndex + 1);//remove the plus 1,as the channel number has been rearranged already.lxk 20150205
                    Log.d(TAG,"--b1--selProg num:" + selProg.number + " selProg service:" + selProg.serviceType);
			if(EnumServiceType.E_SERVICETYPE_ATV.ordinal()  == selProg.serviceType)
                    {
                        if(selItemIndex > 0)
                        {
                            selProg = progInfoList.get(selItemIndex - 1);
                            Log.d(TAG,"--b2--selProg num:" + selProg.number + " selProg num:" + selProg.serviceType);
                           // mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                            proListView.setSelection(selItemIndex - 1);
                           // mProgramEditAdapter.setFocusItem(selItemIndex - 1);
                        }
                        else
                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        	TvChannelManager.FIRST_SERVICE_DEFAULT);
                        Log.d(TAG,"--b2--selProg num:" + selProg.number + " selProg ser:" + selProg.serviceType);
                    }
                    else
                    {
                        selProg = progInfoList.get(selItemIndex);
                        Log.d(TAG,"--bselProg current num:" + selProg.number + " selProg service:" + selProg.serviceType);
                        // no need to chagne program since not delete current prog...
                        //mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                        proListView.setSelection(selItemIndex);
						//proListView.setSelection(selItemIndex);
                       // mProgramEditAdapter.setFocusItem(selItemIndex);
                    }
                }
                else if(selItemIndex > 0)
                {
                    selProg = progInfoList.get(selItemIndex - 1);
                    Log.d(TAG,"--3--selProg num:" + selProg.number + " selProg num:" + selProg.serviceType);
                   // mTvChannelManager.selectProgram(selProg.number, selProg.serviceType);
                    proListView.setSelection(selItemIndex - 1);
                   // mProgramEditAdapter.setFocusItem(selItemIndex - 1);
                }
                else
                	{
                    mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                    Log.d(TAG,"--4--selProg num:" + selProg.number + " selProg num:" + selProg.serviceType);
					
                	}
            }

			if ((selItemIndex >= 0) && (selItemIndex < mProgramEditAdapter.getFocusItem()))
			mProgramEditAdapter.setFocusItem(mProgramEditAdapter.getFocusItem() - 1);
		}

        mProgramEditAdapter.notifyDataSetChanged();
        RefreshContent();
		//add by hz to Hide Move fuction when only one  ATV/DTV program
		if((isOnlyOneDTV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_DTV)||
			(isOnlyOneATV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_ATV)){
			ImgMove.setVisibility(View.GONE);
	        textMove.setVisibility(View.GONE);
			mDisableMoveFunctionInDtv=true;
			mDisableMoveFunctionInAtv=true;
		}
		//end
        Log.d(TAG,"Now,selItemIndex:" + (int) proListView.getSelectedItemId());
        if (!progInfoList.isEmpty() && (proListView.getSelectedItemId() <= progInfoList.size())) {
            if (proListView.getSelectedItemId() == progInfoList.size()) {		 
                int lastSelItemIndex = progInfoList.size() - 1;
                selItemIndex = lastSelItemIndex;
		  Log.d(TAG,"mux Now,selItemIndex:" + (int) proListView.getSelectedItemId());
            }
        }
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        timeOutHelper.reset();
        // KEYCODE_PROG_RED use for KEYCODE_DEL
        // KEYCODE_PROG_GREEN use for KEYCODE_E
        // KEYCODE_PROG_YELLOW use for KEYCODE_M
        Log.i(TAG,"onkeyDown:" + keyCode);
		//add by hz to Hide Move fuction when only one program
		if((isOnlyOneDTV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_DTV)||
			(isOnlyOneATV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_ATV)){
			ImgMove.setVisibility(View.GONE);
	        textMove.setVisibility(View.GONE);
			mDisableMoveFunctionInDtv=true;
			mDisableMoveFunctionInAtv=true;
			
		}
		//end
	if(keyCode == KeyEvent.KEYCODE_1){
		Log.i(TAG,"mux keycode 1");
        	ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
        	ProgramInfo selProg = null;
        	Log.d(TAG," vs curNo.:" + curProgInfo.number+";select:"+proListView.getSelectedItemPosition()+"item = "+mProgramEditAdapter.getItemId(proListView.getSelectedItemPosition()));
	}
		
        if (keyCode == KeyEvent.KEYCODE_PROG_RED && (!moveFlag) && plvios.size() != 0) {
        	return showDialog(this);
        } else if (keyCode == KeyEvent.KEYCODE_PROG_YELLOW) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC /*|| TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)*/) {//zb20150702 modify
                return true;
            }

			ProgramInfo selItemInfo = null;
			if(proListView.getAdapter().getCount()>0)
				selItemInfo=progInfoList.get((int)proListView.getSelectedItemId());
			else
				return true;

            if ((true == mDisableMoveFunctionInAtv)
            	&& (selItemInfo.serviceType == TvChannelManager.SERVICE_TYPE_ATV))
            {
            	return true;
            }

            if ((true == mDisableMoveFunctionInDtv)
            	&& (selItemInfo.serviceType != TvChannelManager.SERVICE_TYPE_ATV))
            {
            	return true;
            }
            
            /**add to disable move operation in ATV.lxk 20141210***//*
            int selItem = (int) proListView.getSelectedItemId();
            ProgramInfo ProgInfo = progInfoList.get(selItem);
            //Log.i(TAG,"selItem:" + selItem + " with ProgInfo.serviceType:" + ProgInfo.serviceType);
            if (ProgInfo.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                return true;
            }
            end*/
            if (plvios.size() == 0) // || ImgMove.getVisibility() == View.GONE)
                return true;
            moveFlag = !moveFlag;
            setMoveTip(moveFlag);
            position = (int) proListView.getSelectedItemId();
			if(moveFlag)
				isMoving = position;
            if (position >= plvios.size()) {
                return false;
            }
            swapObject(plvioTmp, plvios.get(position));
            if (moveFlag) {
                m_u32Source = position;
            } else {
                m_u32Target = position;
                Log.i(TAG,"source:" + m_u32Source + " vs target:" + m_u32Target);
                Log.i(TAG,"mDtvDelOrHideNum:" + mDtvDelOrHideNum);
                if (m_u32Source != m_u32Target) {
                    /* Since DTV index will be recompute to real index under layer but ATV, so we have to consider the DTV programs that be deleted and hidden DTV when move ATV channels.
                       E.g.
                       DTV1 DTV2 DTV3 ATV1 ATV2 ATV3
                       delete DTV2   =>DTV1 (DTV2) DTV3 ATV1 ATV2 ATV3
                       Parentheses () is invisible but actually exists
                       so, move ATV1 to ATV2,moveProgram() parameters should be 3 4,not 2 3
                     */
                    if (TvChannelManager.SERVICE_TYPE_DTV == progInfoList.get(m_u32Source).serviceType) {
                        mTvChannelManager.moveProgram(m_u32Source, m_u32Target);
                    } else {
                        mTvChannelManager.moveProgram(m_u32Source + mDtvDelOrHideNum, m_u32Target + mDtvDelOrHideNum);
                    }
                    //RefreshContent();
                    plvios.clear();
        			progInfoList.clear();
        			getProgList();
					mProgramEditAdapter.setFocusItem(getCurrentProggramIndex());
        			mProgramEditAdapter.notifyDataSetChanged();
        			proListView.invalidate();
                }
//       LCP DEL @20160812 for retain current channel
                /*if (progInfoList.size() > 0) {
                    if (m_u32Target >= progInfoList.size()) {
                        return false;
                    }
                    ProgramInfo ProgInf = progInfoList.get(m_u32Target);
                    mTvChannelManager.selectProgram(ProgInf.number, ProgInf.serviceType);
                    if (ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_DTV) {
                        mTvChannelManager.playDtvCurrentProgram();
                    }
                    mProgramEditAdapter.setFocusItem(m_u32Target);
                    mProgramEditAdapter.notifyDataSetChanged();
                }*/
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (currutPage >= 2) {
                moveKeyCount = 0;
                moveFlag = false;
                setMoveTip(moveFlag);
                currutPage--;
                proListView.setSelection((currutPage - 1) * pageSize);
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (currutPage < ((plvios.size() - 1) / pageSize) + 1) {
                moveKeyCount = 0;
                moveFlag = false;
                setMoveTip(moveFlag);
                currutPage++;
                proListView.setSelection((currutPage - 1) * pageSize);
                return true;
            }
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (proListView.getSelectedItemPosition() == 0) {
                proListView.setSelection(plvios.size() - 1);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (proListView.getSelectedItemPosition() == plvios.size() - 1) {
                proListView.setSelection(0);
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent intentRoot = new Intent(this, RootActivity.class);
            startActivity(intentRoot);
            finish();
        }
        else if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(TvIntent.MAINMENU);
            intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
            startActivity(intent);
            finish();
        }
        // edit atv program when press "KEYCODE_PROG_GREEN" key,not support dtv
        else if (keyCode == KeyEvent.KEYCODE_PROG_GREEN)// KeyEvent.KEYCODE_PROG_GREEN
                                                        // to edit
        {
            int selItemIndex = (int) proListView.getSelectedItemId();
            if (selItemIndex >= progInfoList.size()) {
                return false;
            }
            ProgramInfo ProgInf = progInfoList.get(selItemIndex);

            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                //ISDB not support program edit both ATV & DTV
                return false;
            }

            if (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC) {
                //Log.i(TAG,"selItemIndex:" + selItemIndex + " with ProgInf.serviceType:" + ProgInf.serviceType);
//                if (ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_DTV
//			||ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_RADIO
//			||ProgInf.serviceType == TvChannelManager.SERVICE_TYPE_DATA) {
//                    return false;
//                }
            }
            // Add : will not do onPause
            timeOutHelper.stop();
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.program_dialog_edit_text, null);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.str_program_edit_dialog_input)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(textEntryView)
                    .setPositiveButton(R.string.str_program_edit_dialog_ok, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            int selItemIndex = (int) proListView.getSelectedItemId();
                            timeOutHelper.start();
                            timeOutHelper.init();
                            if (selItemIndex >= progInfoList.size()) {
                                return;
                            }
                            if (selItemIndex >= plvios.size()) {
                                return;
                            }
                            ProgramInfo ProgInf = progInfoList.get(selItemIndex);
                            input = (EditText) textEntryView.findViewById(R.id.program_edit_text);
                            String Tvmame = input.getText().toString();
                            String finalName = splitString(Tvmame, 27);// sn:MAX_STATION_NAME=30
                            //add by wxy
                            if("".equals(finalName))
                            {
                            	Toast.makeText(ProgramListViewActivity.this, R.string.edit_program_toast,Toast.LENGTH_SHORT).show();
                            }
                            //add end
                            else
                            {
                            (plvios.get(selItemIndex)).setTvName(finalName);
                            mProgramEditAdapter.notifyDataSetChanged();
                            proListView.invalidate();
                            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                                mTvAtscChannelManager.setProgramName(ProgInf.majorNum, ProgInf.minorNum, finalName);
                            } else {
                                mTvChannelManager.setProgramName(ProgInf.number, ProgInf.serviceType, finalName);
                            }
                            }
                        }
                    })
                    .setNegativeButton(R.string.str_program_edit_dialog_cancel,
                            new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    timeOutHelper.start();
                                    timeOutHelper.init();
                                }
                            }).show();// show this for atv program
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public String splitString(String str, int len) {
        if (str == null) {
            return "";
        }
        int k = 0;
        String new_str = "";
        for (int i = 0; i < str.length(); i++) {
            byte[] b = (str.charAt(i) + "").getBytes();
            k = k + b.length;
            if (k > len) {
                break;
            }
            new_str = new_str + str.charAt(i);
        }
        return new_str;
    }

    private void swapObject(ProgramListViewItemObject obj1, ProgramListViewItemObject obj2) {
        obj1.setTvName(obj2.getTvName());
        obj1.setTvNumber(obj2.getTvNumber());
        obj1.setFavoriteImg(obj2.isFavoriteImg());
        obj1.setSkipImg(obj2.isSkipImg());
        obj1.setSslImg(obj2.isSslImg());
		obj1.setLockImg(obj2.isLockImg());
        obj1.setServiceType(obj2.getServiceType());
    }

    private void RefreshContent() {
        plvios.clear();
        progInfoList.clear();
        getProgList();
        mProgramEditAdapter.notifyDataSetChanged();
        proListView.invalidate();
		//add by hz to Hide Move fuction when only one  ATV/DTV program
		if((isOnlyOneDTV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_DTV)||
			(isOnlyOneATV_Prog()&&mTvCommonManager.getCurrentTvInputSource()==mTvCommonManager.INPUT_SOURCE_ATV)){
			ImgMove.setVisibility(View.GONE);
	        textMove.setVisibility(View.GONE);
			mDisableMoveFunctionInDtv=true;
			mDisableMoveFunctionInAtv=true;
		}
		else if(mTvCommonManager.isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)){
			ImgMove.setVisibility(View.VISIBLE);
	        textMove.setVisibility(View.VISIBLE);
			mDisableMoveFunctionInDtv=false;
			mDisableMoveFunctionInAtv=false;
		}
		//end
		
    }

    private void addOneListViewItem(ProgramInfo pgi) {
        boolean flag = false;
        if (pgi != null) {
            ProgramListViewItemObject plvio = new ProgramListViewItemObject();
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                String channum = mTvAtscChannelManager.getDispChannelNum(pgi);
                String name = mTvAtscChannelManager.getDispChannelName(pgi);
                plvio.setTvName(name);
            } else {
                plvio.setTvName(pgi.serviceName);
            }
            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                plvio.setTvNumber(String.valueOf(Utility.getATVDisplayChNum(pgi.number)));
            } else {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    String channum = mTvAtscChannelManager.getDispChannelNum(pgi);
                    plvio.setTvNumber(channum);
                } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                    String channum = pgi.majorNum + "." + pgi.minorNum;
                    plvio.setTvNumber(channum);
                } else {
                    plvio.setTvNumber(String.valueOf(pgi.number));
                }
            }
            flag = false;
            if (pgi.favorite != 0) {
                flag = true;
            }
            plvio.setFavoriteImg(flag);
            flag = pgi.isSkip;
            plvio.setSkipImg(flag);
			//add by hz 20160726 to hide lock_img when close SystemLock
			if(TvParentalControlManager.getInstance().isSystemLock()){
				flag = pgi.isLock;
            	plvio.setLockImg(flag);
			}
			//end
            flag = pgi.isScramble;
            plvio.setSslImg(flag);
            plvio.setServiceType(pgi.serviceType);
            plvios.add(plvio);
        }
    }

    private int getfocusIndex() {
        int focusIndex = 0;
        ProgramInfo cpi = mTvChannelManager.getCurrentProgramInfo();
        for (ProgramInfo pi: progInfoList) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                if ((cpi.majorNum == pi.majorNum)
                    && (cpi.minorNum == pi.minorNum)
                    && (cpi.serviceType == pi.serviceType)) {
                    focusIndex = progInfoList.indexOf(pi);
                    break;
                }
            } else {
                if (cpi.number == pi.number && cpi.serviceType == pi.serviceType) {
                    focusIndex = progInfoList.indexOf(pi);
                    break;
                }
            }
        }
        return focusIndex;
    }
    
    private int getfocusItem() {
        int focusIndex = 0;
        ProgramInfo cpi = mTvChannelManager.getCurrentProgramInfo();
        //Log.d(TAG,"cpi.number =  " + cpi.number);
        for (ProgramListViewItemObject pi: plvios) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                if ((String.valueOf(cpi.majorNum) == pi.getTvNumber())
                    && (String.valueOf(cpi.minorNum) == pi.getTvName())
                    && (cpi.serviceType == pi.getServiceType())) {
                    focusIndex = plvios.indexOf(pi);
                    break;
                }
            } else {
            	//modify by hz to correct error of proggramInfo after moving progs
            	if(cpi.serviceType == mTvChannelManager.SERVICE_TYPE_ATV){
                	if (String.valueOf(cpi.number+1) == pi.getTvNumber() && cpi.serviceType == pi.getServiceType()) {
						focusIndex = plvios.indexOf(pi);
                    	break;
                	}
            	}
				else{
					if (String.valueOf(cpi.number) == pi.getTvNumber() && cpi.serviceType == pi.getServiceType()) {
						focusIndex = plvios.indexOf(pi);
                    	break;
                	}
				}
				//end hz
            }
        }
        //Log.d(TAG,"focusIndex =  " + focusIndex);
        return focusIndex;
    }

    private void getProgList() {
        ProgramInfo pgi = null;
        m_nServiceNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
		AtvNum = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
        mDtvDelOrHideNum = 0;
        for (int k = 0; k < m_nServiceNum; k++) {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                pgi = mTvAtscChannelManager.getProgramInfo(k);
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                pgi = TvIsdbChannelManager.getInstance().getProgramInfo(k);
            } else {
                pgi = mTvChannelManager.getProgramInfoByIndex(k);
            }
            if (pgi != null) {
                if ((pgi.isDelete == true) || (pgi.isVisible == false)) {
                    if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_DTV) {
                        mDtvDelOrHideNum += 1;
                    }
                    continue;
                } else {
                    progInfoList.add(pgi);
                    addOneListViewItem(pgi);
                }
            }
        }
    }

    private void setMoveTip(boolean b) {
        int selItemIndex = (int) proListView.getSelectedItemId();
        ProgramInfo ProgInf = progInfoList.get(selItemIndex);
        if (b) {
            ImgDelete.setVisibility(View.INVISIBLE);
            ImgEdit.setVisibility(View.INVISIBLE);
            ImgFavorite.setVisibility(View.INVISIBLE);
            ImgMove.setVisibility(View.VISIBLE);
            ImgSkip.setVisibility(View.INVISIBLE);
            textDelete.setVisibility(View.INVISIBLE);
            textFavorite.setVisibility(View.INVISIBLE);
            textEdit.setVisibility(View.INVISIBLE);
            textMove.setVisibility(View.VISIBLE);
            textSkip.setVisibility(View.INVISIBLE);
        } else {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                ImgDelete.setVisibility(View.VISIBLE);
                ImgEdit.setVisibility(View.GONE);
                ImgFavorite.setVisibility(View.VISIBLE);
                ImgMove.setVisibility(View.VISIBLE);
                ImgSkip.setVisibility(View.VISIBLE);
                textDelete.setVisibility(View.VISIBLE);
                textFavorite.setVisibility(View.VISIBLE);
                textEdit.setVisibility(View.GONE);
                textMove.setVisibility(View.VISIBLE);
                textSkip.setVisibility(View.VISIBLE);
            } else {
                ImgDelete.setVisibility(View.VISIBLE);
//                if (ProgInf.serviceType != TvChannelManager.SERVICE_TYPE_ATV) {
//                    ImgEdit.setVisibility(View.GONE);
//                    textEdit.setVisibility(View.GONE);
//                } else {
                    ImgEdit.setVisibility(View.VISIBLE);
                    textEdit.setVisibility(View.VISIBLE);
//                }
                ImgFavorite.setVisibility(View.VISIBLE);
                ImgMove.setVisibility(View.VISIBLE);
                ImgSkip.setVisibility(View.VISIBLE);
                textDelete.setVisibility(View.VISIBLE);
                textFavorite.setVisibility(View.VISIBLE);
                textMove.setVisibility(View.VISIBLE);
                textSkip.setVisibility(View.VISIBLE);
            }
        }
    }
	/*
	private int getRealDtvProgCunt(){
               ProgramInfo progInfo;
               int sumDtv = this.mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
               int numDtv = 0;
               for (int i = 0; i < sumDtv; i++) {
                       progInfo = this.mTvChannelManager.getProgramInfoByIndex(i);
                       if (!(progInfo == null || progInfo.isDelete || !progInfo.isVisible)) {
                               numDtv++;
                       }
               }
               return numDtv;
       }
       private int getCurrentProgFocus(){
               ProgramInfo progInfo;
               ProgramInfo curProgInfo = mTvChannelManager.getCurrentProgramInfo();
               int totalProgs = this.mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
               for (int i = 0; i < totalProgs; i++) {
                       progInfo = this.mTvChannelManager.getProgramInfoByIndex(i);
                       if (!(progInfo == null || progInfo.isDelete || !progInfo.isVisible)) {
                               if(progInfo.serviceName.equals(curProgInfo.serviceName)
                                       && progInfo.serviceType == curProgInfo.serviceType)
                                       return i;
                       }
               }
               return 0;
       }
	*/
       // add by chenwenlong 20170310
       private int getCurrentProggramIndex() {
               ProgramInfo curProgInfo;
               ProgramListViewItemObject progInfo;
               String number;
               if(mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                       curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();
                       number = mTvAtscChannelManager.getDispChannelNum(curProgInfo);
               }
               else if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                       curProgInfo = TvIsdbChannelManager.getInstance().getCurrentProgramInfo();
                       if(curProgInfo.serviceType == TvChannelManager.SERVICE_TYPE_ATV)
                               number = String.valueOf(Utility.getATVDisplayChNum(curProgInfo.number)+ "."+0);
                       else
                               number = curProgInfo.majorNum+"."+curProgInfo.minorNum;
               }
               else {
                       curProgInfo = this.mTvChannelManager.getCurrentProgramInfo();
                       if(mTvSystem == TvCommonManager.TV_SYSTEM_DVBT
                                       && curProgInfo.serviceType == TvChannelManager.SERVICE_TYPE_ATV){
                               number = String.valueOf(Utility.getATVDisplayChNum(curProgInfo.number));
                       }else{
                               number = String.valueOf(curProgInfo.number);
                       }
               }
               for (int i = 0; i < plvios.size(); i++) {
                       progInfo = (ProgramListViewItemObject) plvios.get(i);
                       if(progInfo.getServiceType() == curProgInfo.serviceType
                                       && progInfo.getTvNumber().equals(number)) {
                               Log.d(TAG, "i --->>> " + i);
                               return i;
                       }
               }
               Log.d(TAG,"return 0");
               return 0;
       }
       // end add
       
       
       private void setCurProgramFocus(int keyCode){
               //ProgramInfo programInfo = mTvChannelManager.getCurrentProgramInfo();
               ProgramEditAdapter mAdapter = (ProgramEditAdapter)proListView.getAdapter();
               if((isMoving == 0 && keyCode == KeyEvent.KEYCODE_DPAD_UP)
               || (isMoving == (progInfoList.size()-1) && keyCode == KeyEvent.KEYCODE_DPAD_DOWN)){
                       return;
               }else{
                       if(isMoving == mAdapter.getFocusItem()){
                               if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                                       isMoving--;
                                       mAdapter.setFocusItem(mAdapter.getFocusItem()-1);
                               }else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                                       isMoving++;
                                       mAdapter.setFocusItem(mAdapter.getFocusItem()+1);
                               }
                       }else{
                               if(isMoving > 0 && (isMoving-1) == mAdapter.getFocusItem()
                               && keyCode == KeyEvent.KEYCODE_DPAD_UP){
                                       mAdapter.setFocusItem(isMoving);
                                       isMoving--;
                               }else if(isMoving< progInfoList.size()-1 && (isMoving+1) == mAdapter.getFocusItem()
                                       && keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                                       mAdapter.setFocusItem(isMoving);
                                       isMoving++;
                               }else{
                                       if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                                               isMoving--;
                                       }else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                                               isMoving++;
                                       }
                               }
                       }
               }
       }
    private TVRootApp getApp()
    {
        return (TVRootApp) this.getApplication();
    }
}
