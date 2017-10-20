//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

import com.mstar.android.tv.TvCommonManager;

import java.util.Locale;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.holder.ViewHolder;
import com.mstar.tvframework.MstarBaseActivity;

public class PALATVManualTuning extends MstarBaseActivity {
    /** Called when the activity is first created. */
    private static final String TAG = "PALATVManualTuning";

    private static final int USER_CHANGE_CHANNEL_TIMEOUT = 1000;

    private ViewHolder mViewholder;

    private int mColorSystemIndex = 0;

    private int mSoundSystemIndex = 0;
    
    private int mStoreNum = 0;
    
    private int mStoreToNum = 0;

    private String mSoundSystem[] = { "BG", "DK", "I", "L", "M" };

    private String mColorSystem[] = { "PAL", "NTSC", "SECAM", "NTSC_44", "PAL_M", "PAL_N", "PAL_60",
            "NO_STAND", "AUTO" };

    private TvChannelManager mTvChannelManager = null;

    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    private Handler mAtvUiEventHandler = null;

    private int mTvSystem = 0;

    private int mChannelMax = 0;

    private int mChannelMin = 0;

    private int mCurChannelNumber = 0;

	private TextView ManualTuningTitle=null;

    //private ComboButton mComboStoreChannelNum = null;

    private BroadcastReceiver mReceiver = null;

    private Handler mDirectChangeChTimeout = new Handler();

    private LinearLayout mLinearLayoutSearchDirection = null;

    private LinearLayout mLinearLayoutSearchFineTune = null;
    
    private LinearLayout palatvmanualtuning_channelnum = null;
    private LinearLayout palatvmanualtuning_colorsystem = null;
    private LinearLayout palatvmanualtuning_soundsystem = null;
    private TextView save = null;
    private LinearLayout mLinearLayoutStoreChannelNum = null;

    private int mInputFrequency = -1;
    private int mInputNum = 0;

    /** For showing one arrow both left and right side */
    private static final int DIRECTION_NONE = 0;

    /** For showing 2 arrows in the left side, no arrow in the right side */
    private static final int DIRECTION_LEFT = 1;

    /** For showing no arrows in the left side, 2 arrow in the right side */
    private static final int DIRECTION_RIGHT = 2;

    /** Align Supernova */
    private static final int STORE_CHANNEL_MIN = 1;

    private static final int STORE_CHANNEL_MAX = 200;

    private Runnable mDirectChangeChTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            setIsdbAtvChannel(mCurChannelNumber - 1);
            updateAtvManualtuningComponents();
            mCurChannelNumber = -1;
        }
    };

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palatvmanualtuning);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        mTvChannelManager = TvChannelManager.getInstance();
        mLinearLayoutStoreChannelNum = (LinearLayout) findViewById(R.id.linearlayout_cha_palatvmanualtuning_store_channelnum);
        palatvmanualtuning_channelnum = (LinearLayout) findViewById(R.id.linearlayout_cha_palatvmanualtuning_channelnum);
        palatvmanualtuning_colorsystem = (LinearLayout) findViewById(R.id.linearlayout_cha_palatvmanualtuning_colorsystem);
        palatvmanualtuning_soundsystem = (LinearLayout) findViewById(R.id.linearlayout_cha_palatvmanualtuning_soundsystem);
		ManualTuningTitle=(TextView)findViewById(R.id.textview_cha_palatvmanualtuning);
        save = (TextView) findViewById(R.id.manual_tuning_save);
        //if (getApp().isCusOnida()) {
        //	save.setVisibility(View.GONE);	
		//}
        
        save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mStoreNum = Integer.parseInt(mViewholder.text_cha_atvmanualtuning_store_channelno_val.getText().toString());
				boolean isSave = mTvChannelManager.saveAtvProgram(mStoreNum - 1);
                updateAtvManualtuningComponents();
                mCurChannelNumber = 0;// -1;
                if(isSave){
                	Toast.makeText(getApp(), R.string.str_program_saved, Toast.LENGTH_SHORT).show();
                }
			}
		});
        /*mComboStoreChannelNum = new ComboButton(this, null, R.id.linearlayout_cha_palatvmanualtuning_store_channelnum,
                1, 2, ComboButton.DIRECT_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (getIdx() > STORE_CHANNEL_MAX) {
                    setIdx(STORE_CHANNEL_MIN);
                } else if (getIdx() < STORE_CHANNEL_MIN) {
                    setIdx(STORE_CHANNEL_MAX);
                }
            }
        };*/

        mViewholder = new ViewHolder(PALATVManualTuning.this);
        mViewholder.findViewForPALAtvManualTuning();
        // registerListeners();

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

        if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_MENU_SCAN);
            } else {
                TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                mTvChannelManager.changeToFirstService(TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
            }
        }

        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            TvIsdbChannelManager.getInstance().genMixProgList(false);
            // FIXME: use constant instead of enum
            mChannelMax = TvChannelManager.getInstance().getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MAX_CHANNEL, 0, 0);
            mChannelMin = TvChannelManager.getInstance().getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MIN_CHANNEL, 0, 0);
            Log.d(TAG, "mChannelMax: " + mChannelMax);
            Log.d(TAG, "mChannelMin: " + mChannelMin);
        }else{
            mChannelMax = TvChannelManager.getInstance().getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MAX_CHANNEL, 0, 0);
            mChannelMin = TvChannelManager.getInstance().getProgramCtrl(TvChannelManager.ATV_PROG_CTRL_GET_MIN_CHANNEL, 0, 0);
            mCurChannelNumber = -1;
            mStoreToNum = TvChannelManager.getInstance().getCurrentChannelNumber();
            Log.d(TAG, "mChannelMax: " + mChannelMax);
            Log.d(TAG, "mChannelMin: " + mChannelMin);
            Log.d(TAG, "mCurChannelNumber: " + mCurChannelNumber);
        }

        updateAtvManualtuningComponents();

        TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {

            @Override
            public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                Log.d(TAG, "onKeyInfo");
                return false;
            }

            @Override
            public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                Log.d(TAG, "onAutoSwitch");
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                finish();

                return false;
            }
        });

        mAtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateAtvTuningScanInfo((AtvEventScan)msg.obj);
            }
        };

        mLinearLayoutSearchDirection = (LinearLayout) findViewById(R.id.linearlayout_cha_palatvmanualtuning_starttuning);
        mLinearLayoutSearchDirection.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                LinearLayout container = (LinearLayout) view;
                int nCurrentFrequency = mTvChannelManager.getAtvCurrentFrequency();
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                        if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_LEFT) {
                            mTvChannelManager.stopAtvManualTuning();
							//add by hz 20170720  for mantis:0031649
							updateTuneIcon((LinearLayout) view, DIRECTION_NONE);
							return true;
							//end hz
                        }

                        if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_RIGHT) {
                        	TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
                            mTvChannelManager.startAtvManualTuning(5 * 1000, nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_SEARCH_UP);
                        }
                        updateTuneIcon((LinearLayout) view, DIRECTION_RIGHT);
                        return true;
                    } else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                        if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_RIGHT) {
                            mTvChannelManager.stopAtvManualTuning();
							//add by hz 20170720  for mantis:0031649
							updateTuneIcon((LinearLayout) view, DIRECTION_NONE);
							return true;
							//end hz
                        }

                        if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_LEFT) {
                        	TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
                            mTvChannelManager.startAtvManualTuning(5 * 1000, nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_SEARCH_DOWN);
                        }
                        updateTuneIcon((LinearLayout) view, DIRECTION_LEFT);
                        return true;
                    } else if (KeyEvent.KEYCODE_ENTER == keyCode) {
                        if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                            mTvChannelManager.stopAtvManualTuning();
                        }
                        updateTuneIcon((LinearLayout) view, DIRECTION_NONE);
                        return true;
                    }
                }
                return false;
            }
        });

        mLinearLayoutSearchFineTune = (LinearLayout) findViewById(R.id.linearlayout_cha_palatvmanualtuning_fine_tune);
        mLinearLayoutSearchFineTune.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                LinearLayout container = (LinearLayout) view;
                int nCurrentFrequency = mTvChannelManager.getAtvCurrentFrequency();
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                        if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_NONE) {
                            mTvChannelManager.startAtvManualTuning(3 * 1000, nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_FINE_TUNE_UP);
                            updateAtvManualtuningfreq();
                        }
                       // updateTuneIcon((LinearLayout) view, DIRECTION_RIGHT);
                        return true;
                    } else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                        if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_NONE) {
                            mTvChannelManager.startAtvManualTuning(3 * 1000, nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_FINE_TUNE_DOWN);
                            updateAtvManualtuningfreq();
                        }
                       // updateTuneIcon((LinearLayout) view, DIRECTION_LEFT);
                        return true;
                    }
                } else if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    updateTuneIcon((LinearLayout) view, DIRECTION_NONE);
                    return true;
                }
                return false;
            }
        });

        OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout container = (LinearLayout) v;
                if (false == hasFocus) {
                	if (container == mLinearLayoutSearchDirection) {
	                    if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
	                        mTvChannelManager.stopAtvManualTuning();
	                    }
                     updateTuneIcon((LinearLayout) v, DIRECTION_NONE);
                	}
                }else {
                	mInputNum = 0;
                    mInputFrequency = -1;
                    updateAtvManualtuningfreq();
				}
            }
        };
      //  mLinearLayoutStoreChannelNum.setOnFocusChangeListener(onFocusChangeListener);
        mLinearLayoutSearchDirection.setOnFocusChangeListener(onFocusChangeListener);
        mLinearLayoutSearchFineTune.setOnFocusChangeListener(onFocusChangeListener);
        //20160602
        OnFocusChangeListener FocuschangesListener1 = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout container = (LinearLayout) v;
                if (hasFocus)
                {
					container.getChildAt(0).setVisibility(View.VISIBLE);
	                container.getChildAt(4).setVisibility(View.VISIBLE);
                }
                else
                {
	                container.getChildAt(0).setVisibility(View.GONE);
	                container.getChildAt(4).setVisibility(View.GONE);
                }
            }
        };
      //  palatvmanualtuning_channelnum.setOnFocusChangeListener(FocuschangesListener1);
      //  palatvmanualtuning_colorsystem.setOnFocusChangeListener(FocuschangesListener1);
      //  palatvmanualtuning_soundsystem.setOnFocusChangeListener(FocuschangesListener1);
        
        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
            //mComboStoreChannelNum.setVisibility(false);
            mViewholder.linear_cha_palatvmanualtuning_colorsystem.setVisibility(View.GONE);
            mViewholder.text_cha_palatvmanualtuning_colorsystem_val.setVisibility(View.GONE);
            mViewholder.linear_cha_palatvmanualtuning_soundsystem.setVisibility(View.GONE);
            mViewholder.text_cha_palatvmanualtuning_soundsystem_val.setVisibility(View.GONE);

        }
    }

    private void updateTuneIcon(LinearLayout container, int nDirection) {
        switch (nDirection) {
            case DIRECTION_LEFT:
                container.getChildAt(0).setBackgroundResource(R.drawable.tune_left_double);
                container.getChildAt(0).setVisibility(View.VISIBLE);
                container.getChildAt(2).setVisibility(View.INVISIBLE);
                break;
            case DIRECTION_RIGHT:
                container.getChildAt(2).setBackgroundResource(R.drawable.tune_right_double);
                container.getChildAt(0).setVisibility(View.INVISIBLE);
                container.getChildAt(2).setVisibility(View.VISIBLE);
                break;
            case DIRECTION_NONE:
            default:
                container.getChildAt(0).setBackgroundResource(R.drawable.tune_left);
                container.getChildAt(2).setBackgroundResource(R.drawable.tune_right);
                container.getChildAt(0).setVisibility(View.VISIBLE);
                container.getChildAt(2).setVisibility(View.VISIBLE);
                break;
        }
    }
    @Override
    public void onResume() {
    TVRootApp app = (TVRootApp) getApplication();
        super.onResume();
		if(app.isCusOnida()){
			ManualTuningTitle.setText(R.string.str_cha_manualtuning);
		}
        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(mAtvPlayerEventListener);
    }

    public boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
        String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
        Log.d(TAG, "deviceName" + deviceName);
        if (!deviceName.equals("MStar Smart TV Keypad"))
            return false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                return true;
            default:
                return false;
        }

    }

    private void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // If MapKeyPadToIR returns true,the previous keycode has been changed
        // to responding
        // android d_pad keys,just return.
        if (MapKeyPadToIR(keyCode, event))
            return true;
        Intent intent = new Intent();
        int currentid = getCurrentFocus().getId();
        int maxv = mColorSystem.length;
        int maxs = mSoundSystem.length;
        int nCurrentFrequency = mTvChannelManager.getAtvCurrentFrequency();
        int curChannelNumber = mTvChannelManager.getCurrentChannelNumber();

        switch (keyCode) {
            case KeyEvent.KEYCODE_PROG_RED:
            	mStoreNum = Integer.parseInt(mViewholder.text_cha_atvmanualtuning_store_channelno_val.getText().toString());
                boolean isSave = mTvChannelManager.saveAtvProgram(mStoreNum - 1);
                updateAtvManualtuningComponents();
                mCurChannelNumber = 0;// -1;
                if(isSave){
                	Toast.makeText(getApp(), R.string.str_program_saved, Toast.LENGTH_SHORT).show();	
                }
                break;
            case KeyEvent.KEYCODE_ENTER:
            	Log.d("skk", "key enter ");
            	if (currentid == R.id.linearlayout_cha_palatvmanualtuning_store_channelnum) {
                    //mStoreToNum -= 1;
                    mStoreNum = Integer.parseInt(mViewholder.text_cha_atvmanualtuning_store_channelno_val.getText().toString());
                    boolean hasSave = mTvChannelManager.saveAtvProgram(mStoreNum - 1);
                    updateAtvManualtuningComponents();
                    mCurChannelNumber = 0;// -1;
                    if(hasSave){
                    	Toast.makeText(getApp(), R.string.str_program_saved, Toast.LENGTH_SHORT).show();
                    }
				}
            	break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                case R.id.linearlayout_cha_palatvmanualtuning_store_channelnum:
                    mStoreToNum ++ ;
                    if (mStoreToNum > STORE_CHANNEL_MAX) {
                        mStoreToNum = STORE_CHANNEL_MIN;
                    } 
                    mViewholder.text_cha_atvmanualtuning_store_channelno_val.setText(String.valueOf(mStoreToNum));
                    break;
                    case R.id.linearlayout_cha_palatvmanualtuning_channelnum:
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            setIsdbAtvChannel(curChannelNumber + 1);
                        } else {
                            int pc = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
                            if (pc > 0) {
                                mTvChannelManager.programUp();
                            }
                        }
                        updateAtvManualtuningComponents();
                        break;
                    case R.id.linearlayout_cha_palatvmanualtuning_colorsystem:
                    	while (true)//change by wym
                    	{
                        	mColorSystemIndex = (mColorSystemIndex + 1) % (maxv);

						    if (mTvSystem == TvCommonManager.TV_SYSTEM_DTMB)
						    {
						    	boolean bInvalid = true;
                        		if (mColorSystem[mColorSystemIndex].equals("NTSC_44"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_M"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_N"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_60"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("NO_STAND"))
                        			bInvalid = false;
                        		if (bInvalid)
                        		    break;
                        	}
							else if (mTvSystem == TvCommonManager.TV_SYSTEM_DVBT)
						    {
						    	boolean bInvalid = true;
                        		if (mColorSystem[mColorSystemIndex].equals("NTSC_44"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_M"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_N"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_60"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("NO_STAND"))
                        			bInvalid = false;
                        		if (bInvalid)
                        		    break;
                        	}
                        	else
                        		break;
                        }
                        mTvChannelManager
                                .setAtvVideoStandard(mColorSystemIndex);
                        mViewholder.text_cha_palatvmanualtuning_colorsystem_val
                                .setText(mColorSystem[mColorSystemIndex]);
                        break;
                    case R.id.linearlayout_cha_palatvmanualtuning_soundsystem:
                        mSoundSystemIndex = (mSoundSystemIndex + 1) % (maxs);
                        if (getApp().isCusOnida()&&(mSoundSystemIndex>=3)) {
                        	mSoundSystemIndex = 0;
						}
                        mTvChannelManager.setAtvAudioStandard(mSoundSystemIndex);
                        mViewholder.text_cha_palatvmanualtuning_soundsystem_val
                                .setText(mSoundSystem[mSoundSystemIndex]);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
	                case R.id.linearlayout_cha_palatvmanualtuning_store_channelnum:
	                    mStoreToNum -- ;
	                    if (mStoreToNum < STORE_CHANNEL_MIN) {
	                        mStoreToNum = STORE_CHANNEL_MAX;
	                    } 
	                    mViewholder.text_cha_atvmanualtuning_store_channelno_val.setText(String.valueOf(mStoreToNum));
	                    break;
                    case R.id.linearlayout_cha_palatvmanualtuning_channelnum:
                        if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                            setIsdbAtvChannel(curChannelNumber - 1);
                        } else {
                            int pc = mTvChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
                            if (pc > 0) {
                                mTvChannelManager.programDown();
                            }
                        }
                        updateAtvManualtuningComponents();
                        break;
                    case R.id.linearlayout_cha_palatvmanualtuning_colorsystem:
                        while (true)//change by wym
                    	{
                        	mColorSystemIndex = (mColorSystemIndex + maxv - 1) % (maxv);

						    if (mTvSystem == TvCommonManager.TV_SYSTEM_DTMB)
						    {//in dtmb, suport pal ntsc secam
						    	boolean bInvalid = true;
                        		if (mColorSystem[mColorSystemIndex].equals("NTSC_44"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_M"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_N"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_60"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("NO_STAND"))
                        			bInvalid = false;
                        		if (bInvalid)
                        		    break;
                        	}
							else if (mTvSystem == TvCommonManager.TV_SYSTEM_DVBT)
						    {
						    	boolean bInvalid = true;
                        		if (mColorSystem[mColorSystemIndex].equals("NTSC_44"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_M"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_N"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("PAL_60"))
                        			bInvalid = false;
                        		else if (mColorSystem[mColorSystemIndex].equals("NO_STAND"))
                        			bInvalid = false;
                        		if (bInvalid)
                        		    break;
                        	}
                        	else
                        		break;
                        }
                        mTvChannelManager
                                .setAtvVideoStandard(mColorSystemIndex);
                        mViewholder.text_cha_palatvmanualtuning_colorsystem_val
                                .setText(mColorSystem[mColorSystemIndex]);
                        break;
                    case R.id.linearlayout_cha_palatvmanualtuning_soundsystem:
                        mSoundSystemIndex = (mSoundSystemIndex + maxs - 1) % (maxs);
                        if (getApp().isCusOnida()&&(mSoundSystemIndex>=3)) {
                        	mSoundSystemIndex = 2;
						}
                        mTvChannelManager.setAtvAudioStandard(mSoundSystemIndex);
                        mViewholder.text_cha_palatvmanualtuning_soundsystem_val
                                .setText(mSoundSystem[mSoundSystemIndex]);
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    mTvChannelManager.stopAtvManualTuning();
                }
                intent.setAction(TvIntent.MAINMENU);
                intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                finish();
                break;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_9:
                if (currentid == R.id.linearlayout_cha_palatvmanualtuning_channelnum) {
                     if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB) {
                        if (mCurChannelNumber == -1) {
                            // it's the first input number and it equals to 0, do nothing
                            if (KeyEvent.KEYCODE_0 == keyCode) {
                                break;
                            }
                            mCurChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                        } else if (mCurChannelNumber >= 10) {
                            mCurChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                        } else {
                            mCurChannelNumber = mCurChannelNumber * 10 + (keyCode - KeyEvent.KEYCODE_0);
                        }
                        mViewholder.text_cha_palatvmanualtuning_channelnum_val
                                .setText(String.valueOf(mCurChannelNumber));
                        mDirectChangeChTimeout.removeCallbacks(mDirectChangeChTimeoutRunnable);
                        mDirectChangeChTimeout.postDelayed(mDirectChangeChTimeoutRunnable
                                , USER_CHANGE_CHANNEL_TIMEOUT);
                    }
                }else if (currentid == R.id.linearlayout_cha_palatvmanualtuning_store_channelnum) {
                	int currNumLen = 0;
                    if (mCurChannelNumber == -1) {
                        // it's the first input number and it equals to 0, do nothing
                        if (KeyEvent.KEYCODE_0 == keyCode) {
                            break;
                        }
                        mCurChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                    }
                    currNumLen = getIntLen(mCurChannelNumber);
                    if (currNumLen >= getIntLen(mChannelMax)) {
                        if (KeyEvent.KEYCODE_0 == keyCode) {
                            // just ignore input when first input is 0.
                            break;
                        }
                        mCurChannelNumber = 0;
                    }
                    mCurChannelNumber = mCurChannelNumber * 10
                                + (keyCode - KeyEvent.KEYCODE_0);
                    //Log.d(TAG,"---number:" + mCurChannelNumber);
                    if (mCurChannelNumber > mChannelMax) {
                        mCurChannelNumber = keyCode - KeyEvent.KEYCODE_0;
                    }
                    mStoreToNum = mCurChannelNumber;
                    mViewholder.text_cha_atvmanualtuning_store_channelno_val.setText(String.valueOf(mStoreToNum));
				}
                else if (currentid == R.id.linearlayout_cha_palatvmanualtuning_starttuning) {
                    if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)) {
                        //mChannelFrequency
                        processManualTuningFreq(keyCode);
                    }
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    private int getIntLen(int num) {
        int numLen = 1;

        while(num > 9) {
            numLen++;
            num/=10;
        }
        return numLen;
    }

    /*
     * private void registerListeners() { } private OnClickListener listener =
     * new OnClickListener() { Intent intent = new Intent();
     * @Override public void onClick(View view) { // TODO Auto-generated method
     * stub switch (view.getId()) { } } };
     */
    private void updateAtvManualtuningfreq() {
        String str_val;
        if (mInputNum > 0) {
            int mtmp = mInputFrequency;
            switch (mInputNum) {
                case 1: mtmp *= 10000;break;
                case 2: mtmp *= 1000;break;
                case 3: mtmp *= 100;break;
                case 4: mtmp *= 10;break;
                default:
                    break;
            }
            str_val = String.format("%.2f", mtmp/100.0);
        } else {
            int freqKhz = mTvChannelManager.getAtvCurrentFrequency();
            str_val = String.format("%.2f", freqKhz/1000.0);
        }
        mViewholder.text_cha_palatvmanualtuning_freqency_val.setText(str_val);
    }

    private void updateAtvManualtuningComponents() {
        String str_val;

        int curChannelNum = mTvChannelManager.getCurrentChannelNumber() + 1;

        if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
             ((LinearLayout)findViewById(R.id.linearlayout_cha_palatvmanualtuning_soundsystem)).setVisibility(LinearLayout.GONE);
        }

        //mComboStoreChannelNum.setIdx(curChannelNum);
        // 0.250M
        str_val = Integer.toString(curChannelNum);
        mColorSystemIndex = mTvChannelManager.getAvdVideoStandard()
                % (mColorSystem.length);
        if (getApp().isCusOnida()&&(mColorSystemIndex>=3 && mColorSystemIndex != 8)) {//M029 no other type
        	mColorSystemIndex = 8;
            mTvChannelManager.setAtvVideoStandard(mColorSystemIndex);
		}
        mSoundSystemIndex = mTvChannelManager.getAtvAudioStandard()
                % mSoundSystem.length;
        if (getApp().isCusOnida()&&(mSoundSystemIndex>=3)) {
        	mSoundSystemIndex = 0;
		}
        mViewholder.text_cha_atvmanualtuning_store_channelno_val.setText(str_val);
        mViewholder.text_cha_palatvmanualtuning_channelnum_val.setText(str_val);
        mViewholder.text_cha_palatvmanualtuning_colorsystem_val
                .setText(mColorSystem[mColorSystemIndex]);
        mViewholder.text_cha_palatvmanualtuning_soundsystem_val
                .setText(mSoundSystem[mSoundSystemIndex]);
        updateAtvManualtuningfreq();
    }

    @Override
    protected void onPause() {
        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(mAtvPlayerEventListener);
        mAtvPlayerEventListener = null;

        if (mTvChannelManager.getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
            mTvChannelManager.stopAtvManualTuning();
            updateTuneIcon(mLinearLayoutSearchDirection, DIRECTION_NONE);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mDirectChangeChTimeout.removeCallbacks(mDirectChangeChTimeoutRunnable);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void updateAtvTuningScanInfo(AtvEventScan extra) {
        String str_val;
        int frequency = extra.frequencyKHz;
        int percent = extra.percent;
        int minteger = frequency / 1000;
        int mfraction = (frequency % 1000) / 10;
        str_val = Integer.toString(minteger) + "." + Integer.toString(mfraction);
        mViewholder.text_cha_palatvmanualtuning_freqency_val.setText(str_val);

        if (percent >= 100) {
            updateTuneIcon(mLinearLayoutSearchDirection, DIRECTION_NONE);
            mTvChannelManager.stopAtvManualTuning();
            updateAtvManualtuningComponents();
        }
    }

    private void setIsdbAtvChannel(int channelNumber) {
        if (channelNumber > mChannelMax) {
            channelNumber = mChannelMin;
        } else if (channelNumber < mChannelMin) {
            channelNumber = mChannelMax;
        }
        TvIsdbChannelManager.getInstance().setChannel(channelNumber, true);
    }

    private void processManualTuningFreq(int keyCode)
    {
        int val = keyCode - KeyEvent.KEYCODE_0;

        //valid...
        if (val >=0 && val <= 9) {
            if (mInputNum >= 5) {
                mInputNum = 0;
                mInputFrequency = 0;
            }
            if (mInputFrequency < 0) mInputFrequency = 0;
            mInputFrequency = mInputFrequency*10 + val;
            mInputNum++;
            updateAtvManualtuningfreq();

            if (mInputNum >= 5) {
                //start tuning...
                if (mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_NONE) {
                	TvTimerManager.getInstance().setSleepTimeMode(TvTimerManager.SLEEP_TIME_OFF);
                    mTvChannelManager.startAtvManualTuning(5 * 1000, mInputFrequency*10,
                    TvChannelManager.ATV_MANUAL_TUNE_MODE_FINE_TUNE_ONE_FREQ);
                    Locale locale = getResources().getConfiguration().locale;
                    String language = locale.getLanguage();
                    if (language.endsWith("zh")){
                    	mTvChannelManager.setAtvAudioStandard(TvChannelManager.ATV_AUDIO_STANDARD_DK);
                    	mTvChannelManager.setAtvVideoStandard(TvChannelManager.AVD_VIDEO_STANDARD_AUTO);
                    	mViewholder.text_cha_palatvmanualtuning_soundsystem_val
                        .setText(mSoundSystem[1]);//dk
                    	mViewholder.text_cha_palatvmanualtuning_colorsystem_val
                        .setText(mColorSystem[8]);
                    }
                }
                //reset
                mInputNum = 0;
                mInputFrequency = 0;
            }
        }
    }
    private TVRootApp getApp()
    {
        return (TVRootApp) this.getApplication();
    }
}
