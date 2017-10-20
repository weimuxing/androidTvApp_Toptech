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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager.DvbcScanParam;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbMuxInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbTargetRegionInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbRegionChannelListInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbCountryInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbPrimaryRegionInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbSecondaryRegionInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbTeritaryRegionInfo;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.holder.ViewHolder;
import com.mstar.tv.tvplayer.ui.tuning.dvb.TargetRegionListDialog;
import com.mstar.tv.tvplayer.ui.tuning.dvb.RegionChannelListIfnoDialog;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Utility;
import com.mstar.util.Constant;
import com.mstar.android.tv.TvTimerManager;

import java.util.ArrayList;
import android.os.SystemProperties;

@SuppressLint("ShowToast")
public class ChannelTuning extends MstarBaseActivity {
    /** Called when the activity is first created. */

	private BroadcastReceiver mReceiver = null;
	private BroadcastReceiver mReceiver1 = null;

    private static final String TAG = "ChannelTuning";

    private int mTvSystem = 0;

    private static final int MSG_FINISH_ATV_AUTOTUNING = 0x80000000;

	private static int ATV_MIN_FREQ = 44250;
	private static int ATV_MAX_FREQ = 877250;

    private static int ATV_MIN_FREQ_AIR = 44250;

    private static int ATV_MAX_FREQ_AIR = 877250;

    private static int ATV_MIN_FREQ_CABLE = 44250;

    private static int ATV_MAX_FREQ_CABLE = 804250;

    private static int ATV_EVENTINTERVAL = 500 * 1000;// every 500ms to show

    private static int DTV_VHF_MAX_FREQ = 300000;

    private static final int TUNING_EXIT_FLAG = 0x11;

    private static int exitTuningProcess = 0;
    private final int INVALID_SYMBOL = 0;

    private final int INVALID_FREQUENCY = 0;

    private final int INVALID_NETWORKID = 0;

    private static int dtvServiceCount = 0;

    private static int atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_AIR;

    private boolean isDtvAutoUpdateScan = false;

    private ViewHolder viewholder_channeltune;

    private TvCommonManager mTvCommonManager = null;
    
    private TvTimerManager mTvTimerManager = null;

    private Time scanStartTime = new Time();

    private boolean isMhlOpen = false;

    private int scanPercent = -1;

    private int mCurrentRoute = TvChannelManager.TV_ROUTE_NONE;

    private TvChannelManager mTvChannelManager = null;

    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    private OnDtvPlayerEventListener mDtvPlayerEventListener = null;

    private Handler mAtvUiEventHandler = null;

    private Handler mDtvUiEventHandler = null;

    private DvbTargetRegionInfo mTargetRegionInfo = null;

    private int mTargetRegionCountryIndex = 0;

    private int mTargetRegionPrimaryIndex = 0;

    private int mTargetRegionSecondaryIndex = 0;

    private int mTargetRegionTertiaryIndex = 0;

    private String mTargetRegionCountryCode = null;

    private short mTargetRegionPrimaryCode = 0;

    private short mTargetRegionSecondaryCode = 0;

    private int mTargetRegionTertiaryCode = 0;
    
    private String forbidInstallApk;

	private static int mOldCurrentTvInputSource = 0;	
	public static int getmOldCurrentTvInputSource(){		
		return mOldCurrentTvInputSource;	
	}

    private class AtvPlayerEventListener implements OnAtvPlayerEventListener {

        @Override
        public boolean onAtvAutoTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvUiEventHandler.obtainMessage(what, extra);
            mAtvUiEventHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onAtvManualTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvUiEventHandler.obtainMessage(what, extra);
            mAtvUiEventHandler.sendMessage(msg);
            return true;
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
        public boolean onAtvProgramInfoReady(int what) {
            return false;
        }
    }

    private class DtvPlayerEventListener implements OnDtvPlayerEventListener {

        @Override
        public boolean onDtvChannelNameReady(int what) {
            return false;
        }

        @Override
        public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
            Message msg = mDtvUiEventHandler.obtainMessage(what, extra);
            mDtvUiEventHandler.sendMessage(msg);
            return true;
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
            return false;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SystemProperties.getBoolean("persist.sys.no_dtv", false))
        	 setContentView(R.layout.channeltuning_atv);
        else setContentView(R.layout.channeltuning);	
	//	SystemProperties.set("mstar.sys.ChannelTuning", "true");
		SystemProperties.set("persist.sys.power2off","true");
		forbidInstallApk = SystemProperties.get("mstar.toptech.forbidapk", "0");
		mReceiver1 = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals("com.toptech.disable.ChannelTuning")) {
					Toast.makeText(ChannelTuning.this,ChannelTuning.this.getResources().getString(R.string.tuning_no_powoff), Toast.LENGTH_SHORT).show();
				}
				
			}
		};
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction("com.toptech.disable.ChannelTuning");
		registerReceiver(mReceiver1, filter1);

		
		mOldCurrentTvInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE)) {
                    Log.i(TAG, "Receive ACTION_CIPLUS_TUNER_UNAVAIABLE...");
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE);
        registerReceiver(mReceiver, filter);

        mAtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            	//Log.d(TAG, "MSG = " + msg);
            	if (msg.what == MSG_FINISH_ATV_AUTOTUNING)
					finish_atv_autoscan();
            	else
                	updateAtvTuningScanInfo((AtvEventScan)msg.obj);
            }
        };

        mDtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateDtvTuningScanInfo((DtvEventScan) msg.obj);
            }
        };

        viewholder_channeltune = new ViewHolder(ChannelTuning.this);
        viewholder_channeltune.findViewForChannelTuning();
        mTvCommonManager = TvCommonManager.getInstance();
        mTvChannelManager = TvChannelManager.getInstance();
        mTvTimerManager = TvTimerManager.getInstance();

        dtvServiceCount = 0;
        scanStartTime.setToNow();
        DtvEventScan extra;
        exitTuningProcess = 0;
        mTvTimerManager.delAllEpgEvent();
        mTvSystem = mTvCommonManager.getCurrentTvSystem();
		//zb20160114 add
		TvTimerManager.getInstance().setSleepTimeMode(0);
		//end
		
		//add by pan for hide the DATA item in the DTMB system
		//DTMB system has not the DATA program
        LinearLayout mLinearlayoyt = (LinearLayout)findViewById(R.id.linearlayout_cha_dataprogram);
        if(mTvSystem == TvCommonManager.TV_SYSTEM_DTMB){
        	mLinearlayoyt.setVisibility(View.GONE);
        }
        //add end
        
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            viewholder_channeltune.linear_cha_radioprogram.setVisibility(View.INVISIBLE);
            viewholder_channeltune.linear_cha_dataprogram.setVisibility(View.INVISIBLE);
            viewholder_channeltune.text_cha_tuningprogress_rf.setText(getResources().getString(
                    R.string.str_cha_tuningprogress_vhf));



            	viewholder_channeltune.text_cha_tuningprogress_rf.setText(getResources().getString(
                        R.string.str_cha_tuningprogress_uhf));
            
        } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            viewholder_channeltune.linear_cha_radioprogram.setVisibility(View.GONE);
            viewholder_channeltune.linear_cha_dataprogram.setVisibility(View.GONE);
            viewholder_channeltune.lineaR_cha_tvprogram.setVisibility(View.GONE);
            viewholder_channeltune.lineaR_cha_dtvprogram.setVisibility(View.GONE);
            viewholder_channeltune.linear_cha_airdtv.setVisibility(View.VISIBLE);
            viewholder_channeltune.linear_cha_airtv.setVisibility(View.VISIBLE);
            viewholder_channeltune.linear_cha_cabletv.setVisibility(View.VISIBLE);
        }
        if(SystemProperties.getBoolean("persist.sys.no_dtv",false))
        {
        	viewholder_channeltune.lineaR_cha_dtvprogram.setVisibility(View.INVISIBLE);
        	viewholder_channeltune.linear_cha_radioprogram.setVisibility(View.INVISIBLE);
        	viewholder_channeltune.linear_cha_dataprogram.setVisibility(View.INVISIBLE);
        }
        if (false == Utility.isSupportATV()) {
            viewholder_channeltune.lineaR_cha_tvprogram.setVisibility(View.GONE);
        }
        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            isDtvAutoUpdateScan = getIntent().getBooleanExtra("DtvAutoUpdateScan", false);
        }

        if (isDtvAutoUpdateScan) {
            viewholder_channeltune.text_cha_tuningprogress_type.setText("DTV");
            Log.e(TAG, "switchMSrvDtvRouteCmd 1");
            int m_nServiceNum = mTvChannelManager
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            TvChannelManager.DvbcScanParam sp = mTvChannelManager.new DvbcScanParam();
            int dvbcRouteIndex = mTvChannelManager
                    .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBC);
            if (dvbcRouteIndex < 0) {
                Log.e(TAG, "get route index error");
                return;
            }
            mTvChannelManager.switchMSrvDtvRouteCmd(dvbcRouteIndex);
            mTvChannelManager.dvbcgetScanParam(sp);

            if (m_nServiceNum > 0) {
                DvbMuxInfo dmi = mTvChannelManager.getCurrentMuxInfo();
                if (dmi != null) {
                    sp.u32NITFrequency = dmi.frequency;
                    sp.CAB_Type = dmi.modulationMode;
                    sp.u16SymbolRate = (short) dmi.symbRate;
                    Log.e(TAG, "dmi.u32NITFrequencye: " + sp.u32NITFrequency);
                    Log.e(TAG, "dmi.CAB_Type: " + sp.CAB_Type);
                    Log.e(TAG, "dmi.u16SymbolRate: " + sp.u16SymbolRate);
                    mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_DTV);
                    mTvChannelManager.setDvbcScanParam(sp.u16SymbolRate, sp.CAB_Type,
                            sp.u32NITFrequency, 0, (short) 0x0000);
                    mTvChannelManager.startQuickScan();
                } else {
                    Log.e(TAG, "getCurrentMuxInfo error");
                    return;
                }
            } else {
                Log.e(TAG, "m_nServiceNum = 0");
                return;
            }
        } else if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL
                || mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV) {
            if (TvChannelManager.TV_ROUTE_DVBC == mCurrentRoute) {
                viewholder_channeltune.text_cha_tuningprogress_type.setText("CADTV");
            } else {
                viewholder_channeltune.text_cha_tuningprogress_type.setText("DTV");
            }
            TvTypeInfo tvinfo = mTvCommonManager.getTvInfo();
            int currentRouteIndex = mTvChannelManager.getCurrentDtvRouteIndex();
            mCurrentRoute = tvinfo.routePath[currentRouteIndex];
            int dtmbRouteIndex = mTvChannelManager
                    .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);
            if (TvChannelManager.TV_ROUTE_DVBC == mCurrentRoute) {
                viewholder_channeltune.text_cha_tuningprogress_rf.setText(getResources().getString(
                        R.string.str_cha_tuningprogress_freq));
                viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.GONE);
                int scanType = getIntent().getIntExtra("scanType", TvChannelManager.DVBC_FULL_SCAN);
                int cableOperator = getIntent().getIntExtra("cableOperator",
                        TvChannelManager.CABLE_OPERATOR_OTHER);
                int symbol = getIntent().getIntExtra("symbol", INVALID_SYMBOL);
                int modulation = getIntent().getIntExtra("modulation",
                        DvbcScanParam.DVBC_CAB_TYPE_INVALID);
                int frequency = getIntent().getIntExtra("frequency", INVALID_FREQUENCY);
                int networkId = getIntent().getIntExtra("networkId", INVALID_NETWORKID);
                if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
                    mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                }
                TvChannelManager.getInstance().setCableOperator(cableOperator, false);
                mTvChannelManager.setDvbcScanParam((short) symbol, modulation, frequency,
                        0, (short) networkId);
                switch (scanType) {
                    case TvChannelManager.DVBC_FULL_SCAN:
                        mTvChannelManager.startDtvFullScan();
                        break;
                    case TvChannelManager.DVBC_QUICK_SCAN:
                    case TvChannelManager.DVBC_NETWORK_SCAN:
                        //mask by jerry 20160602
                        //mTvChannelManager.startDtvAutoScan();
                        mTvChannelManager.startQuickScan();
                        break;
                    default:
                        break;
                }
            } else if ((TvChannelManager.TV_ROUTE_DVBT == mCurrentRoute)
                    || TvChannelManager.TV_ROUTE_DVBT2 == mCurrentRoute) {
                mTvChannelManager.switchMSrvDtvRouteCmd(currentRouteIndex);
                mTvChannelManager.startDtvAutoScan();
            } else if ((TvChannelManager.TV_ROUTE_DVBS == mCurrentRoute)
                    || TvChannelManager.TV_ROUTE_DVBS2 == mCurrentRoute) {
                viewholder_channeltune.text_cha_tuningprogress_rf.setText(getResources().getString(
                        R.string.str_cha_tuningprogress_freq));
                viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.GONE);
                mTvChannelManager.switchMSrvDtvRouteCmd(currentRouteIndex);
                // mTvChannelManager.startDtvFullScan();
                mTvChannelManager.startDtvAutoScan();
            } else {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                        TvAtscChannelManager.getInstance().deleteAllMainList();
                    } else {
                        TvAtscChannelManager.getInstance().deleteDtvMainList();
                    }
                } else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                    TvIsdbChannelManager.getInstance().setAntennaType(
                            TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
                } else {
                    mTvChannelManager.switchMSrvDtvRouteCmd(dtmbRouteIndex);
                }
                mTvChannelManager.startDtvAutoScan();
            }
        } else {
            viewholder_channeltune.text_cha_tuningprogress_type.setText("ATV");
            String str = "0%49.25";
            if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                TvIsdbChannelManager.getInstance().setAntennaType(
                        TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
                viewholder_channeltune.text_cha_tuningprogress_type.setText("AIR");
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                TvAtscChannelManager.getInstance().deleteAtvMainList();
            }  else if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
            	if(forbidInstallApk.equals("1")){
            		atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_CABLE;
                    TvChannelManager.getInstance().setNTSCAntennaType(
                                                    TvChannelManager.ATV_ANTENNA_TYPE_CABLE);
                    viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
            	}else {
            		atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_AIR;
                    TvChannelManager.getInstance().setNTSCAntennaType(
                                                    TvChannelManager.ATV_ANTENNA_TYPE_AIR);
                    viewholder_channeltune.text_cha_tuningprogress_type.setText("AIR");
				}
            }
            viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.GONE);
            viewholder_channeltune.text_cha_tuningprogress_val.setText(str);
            changeLayoutATV();
        }
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
   	 	new Thread(new Runnable() {
			
   	 		@Override
   	 		public void run() {
   	 			mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
   	 		}
   	 	}).start();
    	super.onStart();
    }

    @Override
    protected void onResume() {
        isMhlOpen = TvMhlManager.getInstance().getAutoSwitch();
        if (isMhlOpen)
            TvMhlManager.getInstance().setAutoSwitch(false);
        viewholder_channeltune.linear_cha_mainlinear.setVisibility(View.VISIBLE);

        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(mAtvPlayerEventListener);

        mDtvPlayerEventListener = new DtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnDtvPlayerEventListener(mDtvPlayerEventListener);

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isMhlOpen) {
            TvMhlManager.getInstance().setAutoSwitch(true);
        }
        if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE) {
            return;
        }

        if (scanPercent <= 100) {
            channetuningActivityLeave();
            pauseChannelTuning();
        }

        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mAtvPlayerEventListener = null;

        TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(mDtvPlayerEventListener);
        mDtvPlayerEventListener = null;
		SystemProperties.set("mstar.sys.ChannelTuning", "false");
        try {
        	unregisterReceiver(mReceiver1);
		} catch (Exception e) {
			e.printStackTrace();
		}
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
		SystemProperties.set("persist.sys.power2off","false");
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                {
                    Log.i(TAG,"onkey:" + keyCode + " tuningStat:" + mTvChannelManager.getTuningStatus());
                    Time curTime = new Time();
                    curTime.setToNow();
                    if (curTime.after(scanStartTime))
                    {
                        if ((curTime.toMillis(true) - scanStartTime.toMillis(true)) < 3500)
                        {
                            Toast toast = Toast.makeText(ChannelTuning.this,getResources().getString(R.string.str_channeltuning_stop),  Toast.LENGTH_SHORT);
                            toast.show();
                            return false;
                        }
                        //add by wxy
                        if(viewholder_channeltune.linear_cha_mainlinear.getVisibility() != View.VISIBLE)
                        {
                            return false;
                        }
                    //add end
                    }
					exitTuningProcess = TUNING_EXIT_FLAG;
                    channetuningActivityLeave();
                    viewholder_channeltune.linear_cha_mainlinear.setVisibility(View.GONE);
                    
                    ExitTuningInfoDialog exitTuning;

					Log.d("wym", "getUserScanType()="+mTvChannelManager.getUserScanType());
					if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ATV)//add by wym
                    	exitTuning = new ExitTuningInfoDialog(this, R.style.DialogExit, TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING);
                    else if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV)
                    	exitTuning = new ExitTuningInfoDialog(this, R.style.DialogExit, TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING);
                    else
                    	exitTuning = new ExitTuningInfoDialog(this, R.style.DialogExit, TUNING_EXIT_FLAG);

                    exitTuning.setOnDismissListener(new OnDismissListener()
                    {
                        @Override
                        public void onDismiss(DialogInterface dialog)
                        {
                            Log.i(TAG,"dialog 1 tuningStatus:" + mTvChannelManager.getTuningStatus());
                           // exitTuningProcess = 0;
                            if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_NONE)
                            {
                                // FIXME: need verifying other system (ISDB/ATSC/DTMB...)
                                // DVB-T doesn't do this until ChannelTuning receives SCAN END event
                                // DVB-T may receive SET REGION or other status after stop scan, so don't finish ChannelTuning immediately
                                if (TvChannelManager.TV_ROUTE_DVBT != mCurrentRoute) {
                                    finish();// if leave tuning this page should hide
                                }
                            }
                            else
                            {
                                viewholder_channeltune.linear_cha_mainlinear.setVisibility(View.VISIBLE);
                                if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_AUTO_TUNING)
                                {
                                    scanStartTime.setToNow();   //add by wxy
                                    if ((mTvSystem == TvCommonManager.TV_SYSTEM_DTMB)||(!TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)))//add by wym
                                    {
										viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.GONE);
                						viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.GONE);
									}
									else
									{
	                                    if(mTvChannelManager.getNTSCAntennaType() == TvChannelManager.ATV_ANTENNA_TYPE_AIR)
	                                        viewholder_channeltune.text_cha_tuningprogress_type.setText("AIR");
	                                    else if(mTvChannelManager.getNTSCAntennaType() == TvChannelManager.ATV_ANTENNA_TYPE_CABLE)
	                                        viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
	                                    else 
	                                    	{
											if((SystemProperties.getBoolean("persist.sys.no_dtv",false))&&TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE))//hexing2015-1214 add for cable show error
												viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
											else
	                                    	viewholder_channeltune.text_cha_tuningprogress_type.setText("ATV");
	                                    	}
									}
                                }
                            }
                        }
                    });
                    exitTuning.show();
                }
                break;
            case KeyEvent.KEYCODE_MENU:
                {
                Time curTime = new Time();
                curTime.setToNow();
                if (curTime.after(scanStartTime)) {
                    if ((curTime.toMillis(true) - scanStartTime.toMillis(true)) < 2000) {
                        Toast toast = Toast.makeText(ChannelTuning.this,
                        		getResources().getString(R.string.str_channeltuning_stop), 1);
                        toast.show();
                        return false;
                    }
                    //add by wxy
                    if(viewholder_channeltune.linear_cha_mainlinear.getVisibility() != View.VISIBLE)
                    {
                        return false;
                    }
                    //add end
                }
                channetuningActivityLeave();
                viewholder_channeltune.linear_cha_mainlinear.setVisibility(View.GONE);
                exitTuningProcess = 0;
                ExitTuningInfoDialog exitTuning = new ExitTuningInfoDialog(this, R.style.Dialog);
                exitTuning.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_NONE) {
                            // FIXME: need verifying other system
                            // (ISDB/ATSC/DTMB...)
                            // DVB-T doesn't do this until ChannelTuning
                            // receives SCAN END event
                            // DVB-T may receive SET REGION or other status
                            // after stop scan, so don't finish ChannelTuning
                            // immediately
                            if ((TvChannelManager.TV_ROUTE_DVBT != mCurrentRoute)
                                    && (TvChannelManager.TV_ROUTE_DVBT2 != mCurrentRoute)) {
                                finish();// if leave tuning this page should
                                         // hide
                            }
                        } else {
                            viewholder_channeltune.linear_cha_mainlinear
                                    .setVisibility(View.VISIBLE);
                            if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_AUTO_TUNING)
                            {
                                scanStartTime.setToNow();   //add by wxy
                                if(TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE))// 0 means E_ATV_SYSTEM_TYPE_NTSC_ENABLE
                                {
                                    if(mTvChannelManager.getNTSCAntennaType() == TvChannelManager.ATV_ANTENNA_TYPE_AIR){
                                        atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_AIR;
                                        viewholder_channeltune.text_cha_tuningprogress_type.setText("AIR");
                                    }
                                    else{// if(mTvChannelManager.getNTSCAntennaType() == TvChannelManager.ATV_ANTENNA_TYPE_CABLE){
                                        atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_CABLE;
                                        viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
                                    }
                               /*     //add by wxy
                                    else
                                    {
									if((SystemProperties.getBoolean("persist.sys.no_dtv",false))&&TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE))//hexing2015-1214 add for cable show error
                                        viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
									else
										viewholder_channeltune.text_cha_tuningprogress_type.setText("ATV");
                                    }
                                    //add end */
                                }
                                else
                                {
                                	viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.INVISIBLE);
									viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.INVISIBLE);
									viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.INVISIBLE);
                                    viewholder_channeltune.text_cha_tuningprogress_type.setText("ATV");
                                }
                            }
                        }
                    }
                });
                exitTuning.show();
            }
                break;
            case KeyEvent.KEYCODE_TV_INPUT:
            case MKeyEvent.KEYCODE_ASPECT_RATIO:
            case KeyEvent.KEYCODE_BROWSER:
                return true;
            case KeyEvent.KEYCODE_POWER:
            	AlertDialog dialog = new AlertDialog.Builder(this).setMessage(R.string.str_power_off_confirm).setPositiveButton(R.string.str_pvr_recording_poweroff_positive_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						AutoTuningActivity.this.sendBroadcast(new Intent("com.toptech.tv.hotkey.POWER_OFF"));
		                SystemProperties.set("persist.sys.power2off","false");
		                SendKey(KeyEvent.KEYCODE_POWER);
		                ChannelTuning.this.finish();
					}
				}).setNegativeButton(R.string.str_pvr_recording_poweroff_nagative_text, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).create();
            	dialog.show();
            	return true;
            default:
                break;
        }
        return true;
        //return super.onKeyDown(keyCode, event);
    }

    private void channetuningActivityLeave() {
        switch (mTvChannelManager.getTuningStatus()) {
            case TvChannelManager.TUNING_STATUS_ATV_AUTO_TUNING:
                mTvChannelManager.pauseAtvAutoTuning();
                break;
            case TvChannelManager.TUNING_STATUS_DTV_AUTO_TUNING:
            case TvChannelManager.TUNING_STATUS_DTV_FULL_TUNING:
                mTvChannelManager.pauseDtvScan();
                break;
            default:
                break;
        }
    }

    private void channetuningActivityExit() {
        Log.e(TAG, "channetuningActivityExit");
        Intent intent = new Intent(TvIntent.MAINMENU);
        intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    private void channetuningActivityExitAtv() {
        Log.e(TAG, "channetuningActivityExitAtv");
        Intent intent = new Intent(TvIntent.MAINMENU);
        if (intent.resolveActivity(getPackageManager()) != null) {
            if(TvParentalControlManager.getInstance().isSystemLock()){
            	Intent intentRoot = new Intent(this, RootActivity.class);
                intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
				startActivity(intentRoot);
            	startActivity(intent);
            	
            }else{
            	startActivity(intent);
            }
        }
    }
    private void pauseChannelTuning() {
        switch (mTvChannelManager.getTuningStatus()) {
            case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                mTvChannelManager.stopAtvAutoTuning();
                if (SystemProperties.getInt("mstar.str.suspending", 0) == 0) {
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
                break;
            case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                mTvChannelManager.stopDtvScan();
                if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                    boolean res = mTvChannelManager.stopAtvAutoTuning();
                    if (res == false) {
                        Log.e(TAG, "atvSetAutoTuningStart Error!!!");
                    }
                } else if (SystemProperties.getInt("mstar.str.suspending", 0) == 0) {
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
                break;
            default:
                break;
        }
    }

	private void finish_atv_autoscan()
    {
		
        mTvChannelManager.stopAtvAutoTuning();
        //Log.i(TAG, " step change " + atvScanType + " persent:" + percent + " frequencyKHz:" + frequencyKHz);
        
		if ((mTvSystem == TvCommonManager.TV_SYSTEM_DTMB)||(!TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)))//add by wym
		{
			if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
				if (dtvServiceCount > 0) {
				    if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
				    {
				        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
				    }
				    mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
				            TvChannelManager.FIRST_SERVICE_DEFAULT);
				}
				else
				{
				    if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_ATV) {
				        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
				    }
				    mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
				            TvChannelManager.FIRST_SERVICE_DEFAULT);
				}
            } else {
            //zb20150106 add
            	if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV)
            	{
            	dtvServiceCount= mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
				if (dtvServiceCount > 0) {
                    if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
                    {
                        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                    }
                    mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
            	}
                else //end
                {
                if (mTvCommonManager.getCurrentTvInputSource()
                        != TvCommonManager.INPUT_SOURCE_ATV) {
                    mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                }
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
            }
            channetuningActivityExit();
		}
        else
        {Log.d("wym", "finish atvScanType="+atvScanType);
        Log.d("wym", "finish getNTSCAntennaType()="+mTvChannelManager.getNTSCAntennaType());
        	if(atvScanType == TvChannelManager.ATV_ANTENNA_TYPE_AIR)
            {
                atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_CABLE;
                mTvChannelManager.setNTSCAntennaType(atvScanType);
                changeLayoutATV_Cable();
                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ_CABLE,ATV_MAX_FREQ_CABLE);
            }
            else //if ((percent == 100 && bIsScaningEnable == false) || (percent > 100) || (frequencyKHz > ATV_MAX_FREQ_CABLE))
            {
            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB && TvIsdbChannelManager.getInstance().getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR)
            {
                TvIsdbChannelManager.getInstance().genMixProgList(false);
                TvIsdbChannelManager.getInstance().setAntennaType(TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE);
                viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
                changeLayoutATV();
                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ_AIR, ATV_MAX_FREQ_AIR);
            }
            else
            {
                if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                    if (dtvServiceCount > 0) {
                        if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
                        {
                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                        }
                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                    else
                    {
                        if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_ATV) {
                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                        }
                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                } else {
                //zb20150106 add
                	if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV)
                	{
                	dtvServiceCount= mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
					if (dtvServiceCount > 0) {
                        if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
                        {
                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                        }
                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                	}
                    else //end
                    {
                    if (mTvCommonManager.getCurrentTvInputSource()
                            != TvCommonManager.INPUT_SOURCE_ATV) {
                        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                    }
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                }
                channetuningActivityExit();
            }
            }
        }
    }
    private void updateAtvTuningScanInfo(AtvEventScan extra) {
    	
        String str = new String();
        int percent = extra.percent;
        int frequencyKHz = extra.frequencyKHz;
        int scannedChannelNum = extra.scannedChannelNum;
        int curScannedChannel = extra.curScannedChannel;
        boolean bIsScaningEnable = extra.bIsScaningEnable;

        scanPercent = percent;
        Log.i(TAG, "updateAtvTuningScanInfo().percent:-->"+percent);
        str = "" + scannedChannelNum;
        viewholder_channeltune.text_cha_tvprogram_val.setText(str);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
            if (TvIsdbChannelManager.getInstance().getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE) {
                viewholder_channeltune.text_cha_cabletv_scanned_channels_val.setText(str);
            } else if (TvIsdbChannelManager.getInstance().getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR) {
                viewholder_channeltune.text_cha_airtv_scanned_channels_val.setText(str);
            } else {
                viewholder_channeltune.text_cha_airtv_scanned_channels_val.setText(str);
            }
        }

        str = "" + curScannedChannel;
        viewholder_channeltune.text_cha_tuningprogress_num.setText(str);

        String sFreq = " " + (frequencyKHz / 1000) + "." + (frequencyKHz % 1000) / 10
                    + "MHz";
        if (TvCommonManager.getInstance().isSupportModule(
                    TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
            if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                if (atvScanType == TvChannelManager.ATV_ANTENNA_TYPE_AIR)
                    str = "" + percent + '%' + sFreq;
                else
                    str = "" + percent + '%' + sFreq;
            }
            else if (atvScanType == TvChannelManager.ATV_ANTENNA_TYPE_AIR)
                str = "" + percent + '%' + sFreq;
            else
                str = "" + percent + '%' + sFreq;
        } else {
            str = "" + percent + '%' + sFreq;
        }
        
        viewholder_channeltune.text_cha_tuningprogress_val.setText(str);
        viewholder_channeltune.progressbar_cha_tuneprogress.setProgress(percent);
        Log.d("wym", "tuning: "+mTvChannelManager.getUserScanType()
                    +","+TvCommonManager.getInstance().isSupportModule(
                        TvCommonManager.MODULE_ATV_NTSC_ENABLE)
                    +","+atvScanType);
        //if ((percent == 100 && bIsScaningEnable == false) || (percent > 100) || (frequencyKHz > ATV_MAX_FREQ_AIR))
        if ((percent == 100 && bIsScaningEnable == false) || (percent > 100))
        {
        	//boolean mNewMachanism = true;
        	//if (mNewMachanism)
        	if (mTvSystem == TvCommonManager.TV_SYSTEM_DTMB)
        	{
        		Message myMsg = mAtvUiEventHandler.obtainMessage();
        		myMsg.what = MSG_FINISH_ATV_AUTOTUNING;
				mAtvUiEventHandler.sendMessageDelayed(myMsg, 500);
        	}
        	else
        	{
            mTvChannelManager.stopAtvAutoTuning();
            //Log.i(TAG, " step change " + atvScanType + " persent:" + percent + " frequencyKHz:" + frequencyKHz);

			if ((mTvSystem == TvCommonManager.TV_SYSTEM_DTMB)||(!TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)))//add by wym
			{
				if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
					if (dtvServiceCount > 0) {
					    if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
					    {
					        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
					    }
					    mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
					            TvChannelManager.FIRST_SERVICE_DEFAULT);
					}
					else
					{
					    if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_ATV) {
					        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
					    }
					    mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
					            TvChannelManager.FIRST_SERVICE_DEFAULT);
					}
                } else {
                //zb20150106 add
                	if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV)
                	{
                	dtvServiceCount= mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
					if (dtvServiceCount > 0) {
                        if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
                        {
                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                        }
                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                	}
                    else //end
                    {
                    if (mTvCommonManager.getCurrentTvInputSource()
                            != TvCommonManager.INPUT_SOURCE_ATV) {
                        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                    }
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                }
	            channetuningActivityExit();
			}
            else
            {
            	if(atvScanType == TvChannelManager.ATV_ANTENNA_TYPE_AIR)
	            {
	                atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_CABLE;
	                mTvChannelManager.setNTSCAntennaType(TvChannelManager.ATV_ANTENNA_TYPE_CABLE);
	                changeLayoutATV_Cable();
	                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ_CABLE,ATV_MAX_FREQ_CABLE);
	            }
	            else //if ((percent == 100 && bIsScaningEnable == false) || (percent > 100) || (frequencyKHz > ATV_MAX_FREQ_CABLE))
	            {
	            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB && TvIsdbChannelManager.getInstance().getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR)
	            {
	                TvIsdbChannelManager.getInstance().genMixProgList(false);
	                TvIsdbChannelManager.getInstance().setAntennaType(TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE);
	                viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
	                changeLayoutATV();
	                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ_AIR, ATV_MAX_FREQ_AIR);
	            }
	            else
	            {
	                if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
	                    if (dtvServiceCount > 0) {
	                        if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
	                        {
	                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
	                        }
	                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
	                                TvChannelManager.FIRST_SERVICE_DEFAULT);
	                    }
	                    else
	                    {
	                        if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_ATV) {
	                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
	                        }
	                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
	                                TvChannelManager.FIRST_SERVICE_DEFAULT);
	                    }
	                } else {
	                //zb20150106 add
	                	if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV)
	                	{
	                	dtvServiceCount= mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
						if (dtvServiceCount > 0) {
	                        if (mTvCommonManager.getCurrentTvInputSource()!= TvCommonManager.INPUT_SOURCE_DTV)
	                        {
	                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
	                        }
	                        mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
	                                TvChannelManager.FIRST_SERVICE_DEFAULT);
	                    }
	                	}
	                    else //end
	                    {
	                    if (mTvCommonManager.getCurrentTvInputSource()
	                            != TvCommonManager.INPUT_SOURCE_ATV) {
	                        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
	                    }
	                    mTvChannelManager.changeToFirstService(
	                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
	                            TvChannelManager.FIRST_SERVICE_DEFAULT);
	                    }
	                }
	                channetuningActivityExit();
	            }
	            }
	        }
	        }
        }
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.INVISIBLE);
        }
    }

    private void updateDtvTuningScanInfo(DtvEventScan extra) {
        int scan_status = extra.scanStatus;
        int dtv = extra.dtvSrvCount;
        int radio = extra.radioSrvCount;
        int data = extra.dataSrvCount;
        int boolcurrFrequency = extra.currFrequency;

        Log.e(TAG, "updateDtvTuningScanInfo(), scan_status =  " + scan_status);

        if (TvChannelManager.DTV_SCAN_STATUS_AUTOTUNING_PROGRESS == scan_status) {
            String str;
            int percent = extra.scanPercentageNum;
            int currRFCh = extra.currRFCh;
            int currFrequency = extra.currFrequency;
            boolean isVHF = extra.isVHF;
            String rfSignalName = extra.rfSignalName;
            int resId;
            scanPercent = percent;

            str = "" + (dtv + radio + data);
            viewholder_channeltune.text_cha_airdtv_scanned_channels_val.setText(str);

            str = "" + dtv;
            viewholder_channeltune.text_cha_dtvprogram_val.setText(str);
            str = "" + radio;
            viewholder_channeltune.text_cha_radioprogram_val.setText(str);
            str = "" + data;
            viewholder_channeltune.text_cha_dataprogram_val.setText(str);
            str = "" + percent + '%';
            if (TvChannelManager.TV_ROUTE_DVBC == mCurrentRoute) {
                str = str + "  " + (currFrequency / 1000) + "MHz";
            } 
            viewholder_channeltune.text_cha_tuningprogress_val.setText(str);

            if ((TvChannelManager.TV_ROUTE_DVBS == mCurrentRoute)
                    || (TvChannelManager.TV_ROUTE_DVBS2 == mCurrentRoute)) {
                str = "" + currFrequency;
            } else if (TvChannelManager.TV_ROUTE_DVBC == mCurrentRoute) {
                //str = "" + (currFrequency / 1000) + "MHz";
                str = "";
            } else {
                if (((TvChannelManager.TV_ROUTE_DVBT == mCurrentRoute)
                        || (TvChannelManager.TV_ROUTE_DVBT2 == mCurrentRoute) || (TvChannelManager.TV_ROUTE_DTMB == mCurrentRoute))
                        && (!rfSignalName.isEmpty())) {
                    str = "" + rfSignalName;
                } else {
                    str = "" + currRFCh;
                }
            }

            Log.d(TAG, "updateDtvTuningScanInfo(), " + str + ", " + percent + "%");
            /*****
            if (isVHF) {
               	Log.d("pan_vhf","vhf");
                resId = R.string.str_cha_tuningprogress_vhf;
            } else {
            	Log.d("pan_vhf","uhf");
                resId = R.string.str_cha_tuningprogress_uhf;
            }
            ******/
            if(boolcurrFrequency < 473000 ){ //Bingo 20160624 modify
               	Log.d("pan_vhf","vhf");
                resId = R.string.str_cha_tuningprogress_vhf;
            } else {
            	Log.d("pan_vhf","uhf");
                resId = R.string.str_cha_tuningprogress_uhf;
            }
            viewholder_channeltune.text_cha_tuningprogress_rf.setText(getResources().getString(
                    resId));
        if(!str.equals("255"))//zb20141211 add
            viewholder_channeltune.text_cha_tuningprogress_num.setText(str);
            viewholder_channeltune.progressbar_cha_tuneprogress.setProgress(percent);
        } else if (scan_status == TvChannelManager.DTV_SCAN_STATUS_END) {
			if(exitTuningProcess == TUNING_EXIT_FLAG)
            {
                Log.i(TAG, "exit Dtv Tuning");
				if(mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV)//hexing2016-0120 add for serching dtv exit no singnal
				{
				mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
						TvChannelManager.FIRST_SERVICE_DEFAULT);
				 
				}
				else if(mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL)
				{
					if(TvCommonManager.getInstance().getCurrentTvInputSource()== TvCommonManager.INPUT_SOURCE_DTV)
                        	mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                	TvChannelManager.FIRST_SERVICE_DEFAULT);
					else
						mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                            	TvChannelManager.FIRST_SERVICE_DEFAULT);
				}
				channetuningActivityExit();
            }
            else if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                if (View.VISIBLE != viewholder_channeltune.linear_cha_mainlinear.getVisibility()) {
                    viewholder_channeltune.linear_cha_mainlinear.setVisibility(View.VISIBLE);
                }
                dtvServiceCount = dtv + radio + data;
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    TvAtscChannelManager.getInstance().deleteAtvMainList();
                }

                viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.GONE);
                viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.GONE);
                viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.GONE);
                viewholder_channeltune.text_cha_tuningprogress_type.setText("ATV");

                if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                    TvIsdbChannelManager.getInstance().genMixProgList(false);
                    TvIsdbChannelManager.getInstance().setAntennaType(
                            TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
                    viewholder_channeltune.text_cha_tuningprogress_type.setText("AIR");
                }
                if ((mTvSystem == TvCommonManager.TV_SYSTEM_DTMB)||(!TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)))
                {
					changeLayoutATV();
                }
                else
                {
                	if(forbidInstallApk.equals("1"))
                	{
                		 mTvChannelManager.setNTSCAntennaType(TvChannelManager.ATV_ANTENNA_TYPE_CABLE);
     	                changeLayoutATV_Cable();
     	                atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_CABLE;
                	}else {
                		 mTvChannelManager.setNTSCAntennaType(TvChannelManager.ATV_ANTENNA_TYPE_AIR);
     	                changeLayoutATV_Air();
     	                atvScanType = TvChannelManager.ATV_ANTENNA_TYPE_AIR;
					}
                }
                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
            } else if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV) {
                if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                    TvIsdbChannelManager.getInstance().genMixProgList(false);
                }
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                if (isDtvAutoUpdateScan) {
                    finish();
                } else {
                    channetuningActivityExit();
                }
            }
        } else if (TvChannelManager.DTV_SCAN_STATUS_SET_REGION == scan_status) {
            mTargetRegionInfo = TvDvbChannelManager.getInstance().getRegionInfo();
            Log.d(TAG, "display country dialog...");
            ArrayList<String> countryNameList = getCountryNameList();
            TargetRegionListDialog countryDialog = new TargetRegionListDialog(this,
                    Constant.TARGET_REGION_COUNTRY_LEVEL, countryNameList) {
                @Override
                public void onShow() {
                    if (View.VISIBLE == viewholder_channeltune.linear_cha_mainlinear
                            .getVisibility()) {
                        View view = ChannelTuning.this.findViewById(android.R.id.content);
                        if (null != view) {
                            view.animate()
                                    .alpha(0f)
                                    .setDuration(
                                            ChannelTuning.this.getResources().getInteger(
                                                    android.R.integer.config_shortAnimTime));
                        }
                    }
                }

                @Override
                public void onDismiss() {
                    super.onDismiss();
                    showNextTargetRegionDialog(Constant.TARGET_REGION_COUNTRY_LEVEL,
                            getSelectIndex());
                }
            };
            countryDialog.show();
        } else if (TvChannelManager.STATUS_SET_REGIONAL_CHANNELLIST == scan_status) {
            DvbRegionChannelListInfo mRegionChannelListInfo = TvDvbChannelManager.getInstance()
                    .getRegionChannellistInfo();
            Log.d(TAG, "display region channel list info dialog, count = "
                    + mRegionChannelListInfo.count);
            if (0 < mRegionChannelListInfo.count) {
                new RegionChannelListIfnoDialog(this, mRegionChannelListInfo).show();
            }
        }
    }

    private void changeLayoutATV() {
        if ((true != Utility.isATSC()) && (true != Utility.isISDB())) {

            viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.INVISIBLE);
            viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.INVISIBLE);
            viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.INVISIBLE);
        }
    }
    private void changeLayoutATV_Air() {
        if ((true != Utility.isATSC())
            && (true != Utility.isISDB())) {
            viewholder_channeltune.text_cha_tuningprogress_type.setText("AIR");
            viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.INVISIBLE);
            viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.INVISIBLE);
            viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.INVISIBLE);
        }
    }

    private void changeLayoutATV_Cable() {
        if ((true != Utility.isATSC())
            && (true != Utility.isISDB())) {
            viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
            viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.INVISIBLE);
            viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.INVISIBLE);
            viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.INVISIBLE);
        }
    }
    private ArrayList<String> getCountryNameList() {
        ArrayList<String> countryNameList = new ArrayList<String>();
        for (DvbCountryInfo countryInfo : mTargetRegionInfo.countryInfos) {
            if (countryInfo != null) {
                String countryCode = new String(countryInfo.countryCode);
                countryNameList.add(countryCode);
            }
        }
        return countryNameList;
    }

    private ArrayList<String> getPrimaryNameList() {
        ArrayList<String> primaryNameList = new ArrayList<String>();
        DvbCountryInfo countryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex];
        for (DvbPrimaryRegionInfo primaryInfo : countryInfo.primaryRegionInfos) {
            if (primaryInfo != null) {
                primaryNameList.add(primaryInfo.name);
            }
        }
        return primaryNameList;
    }

    private ArrayList<String> getSecondaryNameList() {
        ArrayList<String> secondaryNameList = new ArrayList<String>();
        DvbPrimaryRegionInfo primaryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex].primaryRegionInfos[mTargetRegionPrimaryIndex];
        for (DvbSecondaryRegionInfo secondaryInfo : primaryInfo.secondaryRegionInfos) {
            if (secondaryInfo != null) {
                secondaryNameList.add(secondaryInfo.regionName);
            }
        }
        return secondaryNameList;
    }

    private ArrayList<String> getTertiaryNameList() {
        ArrayList<String> tertiaryNameList = new ArrayList<String>();
        DvbSecondaryRegionInfo secondaryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex].primaryRegionInfos[mTargetRegionPrimaryIndex].secondaryRegionInfos[mTargetRegionSecondaryIndex];
        for (DvbTeritaryRegionInfo tertiaryInfo : secondaryInfo.tertiaryRegionInfos) {
            if (tertiaryInfo != null) {
                tertiaryNameList.add(tertiaryInfo.regionName);
            }
        }
        return tertiaryNameList;
    }

    public void showNextTargetRegionDialog(int currentLevel, int currentSelectIndex) {
        Log.d(TAG, "showNextTargetRegionDialog: currentLevel = " + currentLevel
                + ", currentSelectIndex = " + currentSelectIndex);
        if (Constant.TARGET_REGION_COUNTRY_LEVEL == currentLevel) {
            mTargetRegionCountryIndex = currentSelectIndex;
            DvbCountryInfo countryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex];
            mTargetRegionCountryCode = new String(countryInfo.countryCode);
            if (countryInfo.primaryRegionInfos.length > 0) {
                Log.d(TAG, "display primary dialog...");
                ArrayList<String> primaryNameList = getPrimaryNameList();
                TargetRegionListDialog primaryDialog = new TargetRegionListDialog(this,
                        Constant.TARGET_REGION_PRIMARY_LEVEL, primaryNameList) {
                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        showNextTargetRegionDialog(Constant.TARGET_REGION_PRIMARY_LEVEL,
                                getSelectIndex());
                    }
                };
                primaryDialog.show();
                return;
            }
        } else if (Constant.TARGET_REGION_PRIMARY_LEVEL == currentLevel) {
            mTargetRegionPrimaryIndex = currentSelectIndex;
            DvbPrimaryRegionInfo primaryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex].primaryRegionInfos[mTargetRegionPrimaryIndex];
            mTargetRegionPrimaryCode = primaryInfo.code;
            if (primaryInfo.secondaryRegionInfos.length > 0) {
                Log.d(TAG, "display secondary dialog...");
                ArrayList<String> secondaryNameList = getSecondaryNameList();
                TargetRegionListDialog secondaryDialog = new TargetRegionListDialog(this,
                        Constant.TARGET_REGION_SECONDARY_LEVEL, secondaryNameList) {
                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        showNextTargetRegionDialog(Constant.TARGET_REGION_SECONDARY_LEVEL,
                                getSelectIndex());
                    }
                };
                secondaryDialog.show();
                return;
            }
        } else if (Constant.TARGET_REGION_SECONDARY_LEVEL == currentLevel) {
            mTargetRegionSecondaryIndex = currentSelectIndex;
            DvbSecondaryRegionInfo secondaryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex].primaryRegionInfos[mTargetRegionPrimaryIndex].secondaryRegionInfos[mTargetRegionSecondaryIndex];
            mTargetRegionSecondaryCode = secondaryInfo.code;
            if (secondaryInfo.tertiaryRegionInfos.length > 0) {
                Log.d(TAG, "display tertiary dialog...");
                ArrayList<String> tertiaryNameList = getTertiaryNameList();
                TargetRegionListDialog tertiaryDialog = new TargetRegionListDialog(this,
                        Constant.TARGET_REGION_TERTIARY_LEVEL, tertiaryNameList) {
                    @Override
                    public void onDismiss() {
                        super.onDismiss();
                        showNextTargetRegionDialog(Constant.TARGET_REGION_TERTIARY_LEVEL,
                                getSelectIndex());
                    }
                };
                tertiaryDialog.show();
                return;
            }
        } else if (Constant.TARGET_REGION_TERTIARY_LEVEL == currentLevel) {
            mTargetRegionTertiaryIndex = currentSelectIndex;
            mTargetRegionTertiaryCode = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex].primaryRegionInfos[mTargetRegionPrimaryIndex].secondaryRegionInfos[mTargetRegionSecondaryIndex].tertiaryRegionInfos[mTargetRegionTertiaryIndex].code;
        }

        if (null == mTargetRegionCountryCode) {
            mTargetRegionCountryCode = "";
        }
        TvDvbChannelManager.getInstance().setRegionInfo(mTargetRegionCountryCode,
                mTargetRegionPrimaryCode, mTargetRegionSecondaryCode, mTargetRegionTertiaryCode);
        if (TvChannelManager.TV_SCAN_ALL == mTvChannelManager.getUserScanType()) {
            if (View.VISIBLE == viewholder_channeltune.linear_cha_mainlinear.getVisibility()) {
                View view = this.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate()
                            .alpha(1f)
                            .setDuration(
                                    this.getResources().getInteger(
                                            android.R.integer.config_shortAnimTime));
                }
            }
        }
    }
    
    // add by jerry 20160524
    private void SendKey(final int keycode){
    	new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keycode);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }.start();
    }
    // end
}
