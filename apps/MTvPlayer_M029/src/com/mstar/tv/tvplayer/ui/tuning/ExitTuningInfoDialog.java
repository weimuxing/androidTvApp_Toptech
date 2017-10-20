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

package com.mstar.tv.tvplayer.ui.tuning;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.holder.ViewHolder;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import android.os.SystemClock;
public class ExitTuningInfoDialog extends Dialog {
    /** Called when the activity is first created. */
    @SuppressWarnings("unused")
    private ViewHolder viewholder_exittune;

    private int mTvSystem = 0;
    private int mExitFlag = 0;
    private static int ATV_MIN_FREQ = 45200;

    private static int ATV_MAX_FREQ = 876250;

    private static int ATV_EVENTINTERVAL = 500 * 1000;// every 500ms to show

    protected TextView text_cha_exittuning_info;
    protected TextView textview_cha_exittune_yes;

    protected TextView textview_cha_exittune_no;

    TvChannelManager mTvChannelManager = null;

    @SuppressWarnings("unused")
    private ViewHolder viewholder_channeltune;

    public ExitTuningInfoDialog(Context context, int theme) {
        super(context, theme);
        mExitFlag = 0;
        viewholder_channeltune = new ViewHolder((ChannelTuning)context);
        Log.i("lxk","exit tuning flag:" + mExitFlag);
    }

    public ExitTuningInfoDialog(Context context, int theme ,int exitflag) {
        super(context, theme);
        mExitFlag = exitflag;
        viewholder_channeltune = new ViewHolder((ChannelTuning)context);
        Log.i("lxk","1 exit tuning flag:" + mExitFlag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        setContentView(R.layout.exittuninginfo_dialog);
        Window exitWindow = this.getWindow();
   	    Display display = exitWindow.getWindowManager().getDefaultDisplay();
   	    int width = display.getWidth();
		int height = display.getHeight();
		width = (int) (display.getWidth() * 0.42);
		height = (int) (display.getHeight() * 0.338);
		exitWindow.setLayout(width, height);
		exitWindow.setGravity(Gravity.CENTER);
        
        textview_cha_exittune_yes = (TextView) findViewById(R.id.textview_cha_exittune_yes);
        textview_cha_exittune_no = (TextView) findViewById(R.id.textview_cha_exittune_no);
        text_cha_exittuning_info = (TextView) findViewById(R.id.textview_cha_exittuning_info);
        viewholder_exittune = new ViewHolder(ExitTuningInfoDialog.this);
        textview_cha_exittune_yes.requestFocus();
        registerListeners();
        mTvChannelManager = TvChannelManager.getInstance();
        if(mExitFlag == 0x11)
            text_cha_exittuning_info.setText(getContext().getResources().getString(R.string.str_cha_exittuning_info));
        else if(mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING)
            text_cha_exittuning_info.setText(getContext().getResources().getString(R.string.str_cha_exitDTVtuning_info));
        else if(mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING)
        {
            if(TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)){
                if(mTvChannelManager.getNTSCAntennaType() == TvChannelManager.ATV_ANTENNA_TYPE_AIR)
                    text_cha_exittuning_info.setText(getContext().getResources().getString(R.string.str_cha_exitAIRtuning_info));
                else
                    text_cha_exittuning_info.setText(getContext().getResources().getString(R.string.str_cha_exitCABLEtuning_info));
            }            
            else {
                text_cha_exittuning_info.setText(getContext().getResources().getString(R.string.str_cha_exitATVtuning_info));
            }
        }
    }

    private void registerListeners() {
        textview_cha_exittune_yes.setOnClickListener(listener);
        textview_cha_exittune_no.setOnClickListener(listener);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ExitTuningActivityExit(false);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.textview_cha_exittune_yes:
                    ExitTuningActivityExit(true);
                    break;
                case R.id.textview_cha_exittune_no:
                    ExitTuningActivityExit(false);
                    break;
                default:
                    ExitTuningActivityExit(false);
                    break;
            }
        }
    };

    private void ExitTuningActivityExit(boolean flag) {
        Intent intent = new Intent();
        if (flag == true)// stop tuning
        {
            switch (mTvChannelManager.getTuningStatus()) {
                case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                    mTvChannelManager.stopAtvAutoTuning();
                    int iDTVporgarmCount = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
                    if(mExitFlag == 0x11)
                    {
                        Log.i("lxk","stop tuning    DTV:" + iDTVporgarmCount);
                        if(ChannelTuning.getmOldCurrentTvInputSource()== TvCommonManager.INPUT_SOURCE_DTV)
                            mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                    TvChannelManager.FIRST_SERVICE_DEFAULT);
                        else
                            mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                    TvChannelManager.FIRST_SERVICE_DEFAULT);
                        intent.setAction(TvIntent.MAINMENU);
                        intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                        getContext().startActivity(intent);
                        this.dismiss();
                        break;
                    }
                    if((TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE))
                        && mTvChannelManager.getNTSCAntennaType() == TvChannelManager.ATV_ANTENNA_TYPE_AIR)
                    {
                        Log.i("lxk","start atv tuning next step");
                        mTvChannelManager.setNTSCAntennaType(TvChannelManager.ATV_ANTENNA_TYPE_CABLE);//0 means cable antenna type.
                        boolean res = mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL,ATV_MIN_FREQ, ATV_MAX_FREQ);
                        if (res == false) {
                            Log.e("TuningService", "atvSetAutoTuningStart Error!!!");
                        }
                    }
                    else
                    {
                        Log.i("lxk","stop atv tuning step");
                        mTvChannelManager.changeToFirstService(
                                TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                        intent.setAction(TvIntent.MAINMENU);
                        intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                            getContext().startActivity(intent);
                        }
                    }
                    this.dismiss();
                    break;
                case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                    mTvChannelManager.stopDtvScan();
                    if(mExitFlag == 0x11)
                    {
                        Log.i("lxk","stop tuning in DTV");
						
						long start_time = SystemClock.elapsedRealtime();
						long cur_time;
						while (true)
						{
							short commendResult[] = null;
							try {
								commendResult = TvManager.getInstance().setTvosCommonCommand("istuning");
							} catch (TvCommonException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (commendResult[0] == 0 )
								break;
							cur_time = SystemClock.elapsedRealtime();
							Log.i("lxk","wait stop tuning    DTV:"+(cur_time - start_time));

							if ((cur_time - start_time) > 10*1000)//timeout
							{
								Log.i("lxk","wait stop tuning DTV timeout:"+(cur_time - start_time));
								break;
							}
						}
						if(TvCommonManager.getInstance().getCurrentTvInputSource()== TvCommonManager.INPUT_SOURCE_DTV)
                        	mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                	TvChannelManager.FIRST_SERVICE_DEFAULT);
						else
							mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                	TvChannelManager.FIRST_SERVICE_DEFAULT);

                       // viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.GONE);
                		//viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.GONE);
                		
                        intent.setAction(TvIntent.MAINMENU);
                        intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                        getContext().startActivity(intent);
                        this.dismiss();
                        break;
                    }
                    // DVB-T doesn't do this until ChannelTuning receives DTV_SCAN_STATUS_END event
                    // Because DVB-T may receive SET REGION or other status after stop scan
                    // DVB-C/ISDB/DTMB also receive DTV_SCAN_STATUS_END after stopDtvScan
                    // But ATSC seems not
                    int dvbtRouteIndex = mTvChannelManager
                            .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT);
                    if (mTvChannelManager.getCurrentDtvRouteIndex() != dvbtRouteIndex) {
                        if (mTvChannelManager.getUserScanType() == mTvChannelManager.TV_SCAN_ALL) {
                            boolean res = mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL,
                                    ATV_MIN_FREQ, ATV_MAX_FREQ);
                            if (res == false) {
                                Log.e("TuningService", "atvSetAutoTuningStart Error!!!");
                            }
                        } else {
                            if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB) {
                                TvIsdbChannelManager.getInstance().genMixProgList(false);
                            }
                            mTvChannelManager.changeToFirstService(
                                    TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                    TvChannelManager.FIRST_SERVICE_DEFAULT);
                            intent.setAction(TvIntent.MAINMENU);
                            intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                getContext().startActivity(intent);
                            }
                        }
                    }
                    this.dismiss();
                    break;
                default:
                    break;
            }
        } else
        // resume tuning
        {
            switch (mTvChannelManager.getTuningStatus()) {
                case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                    mTvChannelManager.resumeAtvAutoTuning();
                    this.dismiss();
                    break;
                case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                    mTvChannelManager.resumeDtvScan();
                    this.dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}
