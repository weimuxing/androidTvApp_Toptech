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

package com.mstar.tv.tvplayer.ui.holder;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.media.AudioManager;
import android.content.Context;

import com.mstar.tv.tvplayer.ui.R;

import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.MyButton;
import com.mstar.tv.tvplayer.ui.component.SeekBarButton;
import com.mstar.tv.tvplayer.ui.settings.EqualizerDialogActivity;
import com.mstar.tv.tvplayer.ui.settings.SeperateHearActivity;
import com.mstar.util.Tools;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumSoundEffectType;
import com.mstar.tv.tvplayer.ui.TVRootApp;

public class SoundViewHolder {
    private static final String TAG = "SoundViewHolder";

    protected static final int STEP = 1;

	private int SOUNDMODE;

    protected ComboButton comboBtnSoundmode;

    protected SeekBarButton seekBarBtnBass;

    protected SeekBarButton seekBarBtnTreble;

    protected MyButton btnEqualizer;

    protected SeekBarButton seekBarBtnBalance;

    protected ComboButton comboBtnAVC;

    protected ComboButton comboBtnSurround;

    private CecSetting cecSetting;
    
    protected ComboButton comboBtnSrs;

    protected ComboButton comboBtnSpdifoutput;

    // protected ComboButton comboBtnSeparatehear;
    protected MyButton btnSeperateHear;

    protected Activity activity;

    protected TvAudioManager tvAudioManager = null;
    protected AudioManager mAudiomanager = null;

    protected ComboButton comboBtnAd;

    protected ComboButton comboBtnHOH;

    protected ComboButton comboBtnHDByPass;
    
    private TextView sound_musicmode=null;

    public SoundViewHolder(Activity act) {
        this.activity = act;
        tvAudioManager = TvAudioManager.getInstance();
         mAudiomanager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
    }

    //add by wxy
    public static void sendKeyEvent(final int KeyCode) {
        new Thread() {     
             public void run() {
                 try {
                     Instrumentation inst = new Instrumentation();
                     inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                     e.printStackTrace();
                 }
              }
   
         }.start();
  }
 //add end
    
    public void findViews() {
    	TVRootApp tvapp = (TVRootApp)activity.getApplication();
    	sound_musicmode = (TextView) activity.findViewById(R.id.sound_musicmode);
    	if (tvapp.isCusOnida()&&sound_musicmode!=null) {
    		sound_musicmode.setText(activity.getResources().getString(R.string.str_sound_music_mode));
		}
        seekBarBtnBass = new SeekBarButton(activity, R.id.linearlayout_sound_bass, STEP, true) {
            @Override
            public void doUpdate() {
                // TODO Auto-generated method stub
                if (tvAudioManager != null) {
                    tvAudioManager.setBass(seekBarBtnBass.getProgress());
                }
            }
        };
        seekBarBtnTreble = new SeekBarButton(activity, R.id.linearlayout_sound_treble, STEP, true) {
            @Override
            public void doUpdate() {
                // TODO Auto-generated method stub
                if (tvAudioManager != null) {
                    tvAudioManager.setTreble(seekBarBtnTreble.getProgress());
                }
            }
        };
        btnEqualizer = new MyButton(activity, R.id.linearlayout_sound_equalizer);
        btnEqualizer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EqualizerDialogActivity.class);
                activity.startActivity(intent);
            }
        });
        comboBtnSoundmode = new ComboButton(activity, activity.getResources().getStringArray(
               getApp().isCusOnida()? R.array.str_arr_sound_soundmode_vals_onida:R.array.str_arr_sound_soundmode_vals), R.id.linearlayout_sound_soundmode, 1, 2,
                true) {
            @Override
            public void doUpdate() {
                SetFocusableOrNotForUserMode();
                if (tvAudioManager != null) {
                    tvAudioManager.setAudioSoundMode(comboBtnSoundmode.getIdx());
                }
                freshDataToUIWhenSoundModChange();
            }
        };
        SetFocusableOrNotForUserMode();
        seekBarBtnBalance = new SeekBarButton(activity, R.id.linearlayout_sound_balance, STEP, true) {
            @Override
            public void doUpdate() {
                // TODO Auto-generated method stub
                if (tvAudioManager != null) {
                    tvAudioManager.setBalance(seekBarBtnBalance.getProgress());
                    seekBarBtnBalance.setProgress_Balance(seekBarBtnBalance.getProgress());
                }
            }
        };
        comboBtnAVC = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_avc, 1, 2, true) {
            @Override
            public void doUpdate() {
                // TODO Auto-generated method stub
                if (tvAudioManager != null) {
			//modify for M029 require.lxk 20150531
			if(TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV){
	                    tvAudioManager.setAvcMode(comboBtnAVC.getIdx() == 0 ? false : true);						}
			else{
					//modify by hz to update Auto Volume in DTV for mantis:0020301
					if(getApp().isCusOnida())
	                    tvAudioManager.setAvcMode(false);
					else
						tvAudioManager.setAvcMode(comboBtnAVC.getIdx() == 0 ? false : true);
					//end hz
				}
			//end
                    //tvAudioManager.setAvcMode(comboBtnAVC.getIdx() == 0 ? false : true);
                }
            }
        };

        comboBtnAd = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_ad, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setADEnable(comboBtnAd.getIdx() == 0 ? false : true);
                    tvAudioManager.setADAbsoluteVolume(50);
                }
            }
        };
        comboBtnHOH = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_hoh, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setHOHStatus(comboBtnHOH.getIdx() == 0 ? false : true);
                }
            }
        };
        comboBtnHDByPass = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_avc_vals), R.id.linearlayout_sound_hdbypass, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager
                            .setHDMITx_HDByPass(comboBtnHDByPass.getIdx() == 0 ? false : true);
                }
            }
        };

        if (Tools.isBox()) {
            comboBtnAd.setVisibility(false);
            comboBtnHOH.setVisibility(false);
        }
        comboBtnSurround = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_surround_vals), R.id.linearlayout_sound_surround, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (tvAudioManager != null) {
                    tvAudioManager.setAudioSurroundMode(comboBtnSurround.getIdx());
                }
            }
        };
        SetFocusableSurroundInARC();
        SetFocusableOrNotForSoundPage();

        comboBtnSrs = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_surround_vals), R.id.linearlayout_sound_srs, 1, 2, true) {
            @Override
            public void doUpdate() {
                // TODO Auto-generated method stub
                if (tvAudioManager != null) {
                    tvAudioManager.enableSRS((comboBtnSrs.getIdx() != 0));
                }
            }
        };
        comboBtnSpdifoutput = new ComboButton(activity, activity.getResources().getStringArray(
                R.array.str_arr_sound_spdifoutput_vals), R.id.linearlayout_sound_spdifoutput, 1, 2,
                true) {
            @Override
            public void doUpdate() {
                // TODO Auto-generated method stub
                tvAudioManager.setAudioSpdifOutMode(comboBtnSpdifoutput.getIdx());
            }
        };
        btnSeperateHear = new MyButton(activity, R.id.linearlayout_sound_separatehear);
        btnSeperateHear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // lyp 2014.12.2
                myScreenSave(activity);
                //Intent intent = new Intent(activity, SeperateHearActivity.class);
                //activity.startActivity(intent);
                // activity.finish();
            }
        });
        if (!tvapp.isCusOnida()) {
        	if(!tvapp.isBTorESelect()){
            	seekBarBtnBass.setVisibility(true);
            	seekBarBtnTreble.setVisibility(true);
            	btnEqualizer.setVisibility(View.GONE);
            }else{
            	seekBarBtnBass.setVisibility(false);
            	seekBarBtnTreble.setVisibility(false);
            	btnEqualizer.setVisibility(View.VISIBLE);
            }
		}
        if (!tvapp.isSpdifEnable())
        	comboBtnSpdifoutput.setVisibility(View.GONE);
        setOnClickListeners();
        //setOnFocusChangeListeners();
        //comboBtnSoundmode.setFocused();//skye
    }

    private void setOnClickListeners() {
        OnClickListener BtnOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
            	sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
            }
        };
        comboBtnSoundmode.setOnClickListener(BtnOnClickListener);
        comboBtnAVC.setOnClickListener(BtnOnClickListener);
        comboBtnAd.setOnClickListener(BtnOnClickListener);
        comboBtnHOH.setOnClickListener(BtnOnClickListener);
        comboBtnHDByPass.setOnClickListener(BtnOnClickListener);
        comboBtnSurround.setOnClickListener(BtnOnClickListener);
        comboBtnSrs.setOnClickListener(BtnOnClickListener);
        comboBtnSpdifoutput.setOnClickListener(BtnOnClickListener);
       
        seekBarBtnBass.setOnClickListener(BtnOnClickListener);
        seekBarBtnTreble.setOnClickListener(BtnOnClickListener);
        seekBarBtnBalance.setOnClickListener(BtnOnClickListener);
    }

  private void freshDataToUIWhenSoundModChange() {
        if (tvAudioManager != null) {
            seekBarBtnBass.setProgress((short) tvAudioManager.getBass());
            seekBarBtnTreble.setProgress((short) tvAudioManager.getTreble());
        }
    }

    public void LoadDataToUI() {
		SOUNDMODE = tvAudioManager.getAudioSoundMode();
        if (tvAudioManager != null) {
            // Refine performance with query DB by content provider to reduce
            // startup time in sound page.
            Cursor cursor = this.activity
                    .getApplicationContext()
                    .getContentResolver()
                    .query(Uri.parse("content://mstar.tv.usersetting/soundsetting"), null, null,
                            null, null);
			int soundMode = 0;
            if (cursor.moveToFirst()) {
                soundMode = cursor.getInt(cursor.getColumnIndex("SoundMode"));
                comboBtnSoundmode.setIdx(soundMode);
                seekBarBtnBalance.setProgress_Balance((short) cursor.getInt(cursor
                        .getColumnIndex("Balance")));
                if(TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV){
                    int AVC = cursor.getInt(cursor.getColumnIndex("bEnableAVC"));
                    comboBtnAVC.setIdx(AVC == 1 ? 1 : 0);
                    //tvAudioManager.setAvcMode(AVC == 1 ? true : false);
                	}
                else{
						//modify by hz to update Auto Volume in DTV for mantis:0020301
						if(getApp().isCusOnida()) {
						//	try {
						//		TvManager.getInstance().getAudioManager().enableBasicSoundEffect(EnumSoundEffectType.E_AVC, false);
						//	} catch (TvCommonException e) {
								// TODO Auto-generated catch block
						//		e.printStackTrace();
						//	}
                			comboBtnAVC.setIdx(0);
						} else {
							comboBtnAVC.setIdx(cursor.getInt(cursor.getColumnIndex("bEnableAVC")) == 1 ? 1 : 0);
						}
						//end hz
					}
                comboBtnAd.setIdx(cursor.getInt(cursor.getColumnIndex("bEnableAD")) == 1 ? 1 : 0);
                comboBtnSurround.setIdx(cursor.getInt(cursor.getColumnIndex("Surround")));
                comboBtnSrs
                        .setIdx(cursor.getInt(cursor.getColumnIndex("SurroundSoundMode")) != 0 ? 1
                                : 0);
            }
            cursor.close();
            

            cursor = this.activity
                    .getApplicationContext()
                    .getContentResolver()
                    .query(Uri
                            .parse("content://mstar.tv.usersetting/soundmodesetting/" + soundMode),
                            null, null, null, null);

            if (cursor.moveToFirst()) {
                seekBarBtnBass.setProgress((short) cursor.getInt(cursor.getColumnIndex("Bass")));
                seekBarBtnTreble
                        .setProgress((short) cursor.getInt(cursor.getColumnIndex("Treble")));
            }
            cursor.close();
            

            comboBtnHOH.setIdx(tvAudioManager.getHOHStatus() ? 1 : 0);
            Log.i(TAG,
                    "*****Spidif Mode is :" + tvAudioManager.getAudioSpdifOutMode() + "*****");
            comboBtnSpdifoutput.setIdx(tvAudioManager.getAudioSpdifOutMode());

            comboBtnHDByPass.setIdx(tvAudioManager.getHDMITx_HDByPass() ? 1 : 0);

            if (!tvAudioManager.isSupportHDMITx_HDByPassMode()) {
                comboBtnHDByPass.setVisibility(false);
            }

        }
        SetFocusableOrNotForUserMode();
        SetFocusableSurroundInARC();
        SetFocusableOrNotForSoundPage();
    }

    private OnTouchListener canTouchListener= new OnTouchListener() {
		
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			return false;
		}
	};
	  private OnTouchListener notTouchListener= new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
		};
    private void SetFocusableOrNotForUserMode() {
    	SetFocusableForLocked();
        if (getApp().isCusOnida()?comboBtnSoundmode.getIdx() != 0 : comboBtnSoundmode.getIdx() != 4) {
        	
            seekBarBtnBass.setEnable(false);
            seekBarBtnBass.setFocusable(false);
            seekBarBtnTreble.setEnable(false);
            seekBarBtnTreble.setFocusable(false);
        	btnEqualizer.setEnable(false);
        	btnEqualizer.setFocusable(false);
        	seekBarBtnBass.getLayout().getChildAt(1).setOnTouchListener(notTouchListener);
        	seekBarBtnTreble.getLayout().getChildAt(1).setOnTouchListener(notTouchListener);
        } else {
        	
    		if(!getApp().getChannelLockOrNot() && TvCecManager.getInstance().getCecConfiguration().arcStatus == 0)
    		{
	    		seekBarBtnBass.setEnable(true);
	            seekBarBtnBass.setFocusable(true);
	            seekBarBtnTreble.setEnable(true);
	            seekBarBtnTreble.setFocusable(true);
	        	btnEqualizer.setEnable(true);
	        	btnEqualizer.setFocusable(true);
    		}
			
          
        	seekBarBtnBass.getLayout().getChildAt(1).setOnTouchListener(canTouchListener);
        	seekBarBtnTreble.getLayout().getChildAt(1).setOnTouchListener(canTouchListener);

        }

    }

    //modify for sound page for M029.lxk 20150309
    private void SetFocusableOrNotForSoundPage() {
    	if (!getApp().isCusOnida()) {
			return;
		}
        int curis = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (curis == TvCommonManager.INPUT_SOURCE_ATV) {
            comboBtnAVC.setEnable(true);
            comboBtnAVC.setFocusable(true);
        } else {
            comboBtnAVC.setEnable(false);
            comboBtnAVC.setFocusable(false);
        }
    }

    
    //modify for surroundsound when ARC enabled.lxk 20150107
	public void SetFocusableSurroundInARC() {
		if (!getApp().isCusOnida()) {
			return;
		}
		cecSetting = TvCecManager.getInstance().getCecConfiguration();
		SOUNDMODE = tvAudioManager.getAudioSoundMode();
		Log.d(TAG,"cecSetting.arcStatus:" + cecSetting.arcStatus);
		if (cecSetting.arcStatus == 1) {
			comboBtnSurround.setEnable(false);
			comboBtnSurround.setFocusable(false);
			comboBtnSoundmode.setEnable(false);
			comboBtnSoundmode.setFocusable(false);
			seekBarBtnBass.setEnable(false);
			seekBarBtnBass.setFocusable(false);
			seekBarBtnTreble.setEnable(false);
			seekBarBtnTreble.setFocusable(false);
		} else {
			comboBtnSurround.setEnable(true);
			comboBtnSurround.setFocusable(true);
			comboBtnSoundmode.setEnable(true);
			comboBtnSoundmode.setFocusable(true);
			if(comboBtnSoundmode.getIdx() == 0 && !getApp().getChannelLockOrNot())
			{
				seekBarBtnBass.setEnable(true);
				seekBarBtnBass.setFocusable(true);
				seekBarBtnTreble.setEnable(true);
				seekBarBtnTreble.setFocusable(true);
			}
		}
		SetFocusableForLocked();
	}

	private void myScreenSave(Activity from){
		/*if (SystemProperties.getInt("persist.sys.istvmute",0)==1) {
			Toast.makeText(from, "TV System Mute,Music Mode Not Allowed", Toast.LENGTH_LONG).show();
			return;
		}*/
		if (mAudiomanager.isMasterMute()) {
			Toast.makeText(from, "TV System Mute,Music Mode Not Allowed", Toast.LENGTH_LONG).show();
               return;
		}
		String str  = (String) activity.getResources().getString(R.string.sound_screen_save);
		Toast toast=Toast.makeText(from, str, Toast.LENGTH_LONG); 
		toast.show();
		handler.post(new Runnable() {				
			@Override
			public void run() {	
				Message msg  = new Message();
				msg.what = 10086;
				handler.sendMessageDelayed(msg, 6000);
			}
		}); 
	}
	
    private  Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 10086){	
				Intent intent = new Intent(activity,SeperateHearActivity.class);
				activity.startActivity(intent);				
			}
		}	
    }; 

    private void SetFocusableForLocked() {
        boolean mlocked = getApp().getChannelLockOrNot();
        if (mlocked) {
            comboBtnSoundmode.setEnable(false);
			comboBtnSoundmode.setFocusable(false);

			seekBarBtnBass.setEnable(false);
			seekBarBtnBass.setFocusable(false);
			seekBarBtnTreble.setEnable(false);
			seekBarBtnTreble.setFocusable(false);
        }else{
        	comboBtnSoundmode.setEnable(true);
			comboBtnSoundmode.setFocusable(true);
			
			if(comboBtnSoundmode.getIdx() == 0 && TvCecManager.getInstance().getCecConfiguration().arcStatus == 0)
			{
				seekBarBtnBass.setEnable(true);
				seekBarBtnBass.setFocusable(true);
				seekBarBtnTreble.setEnable(true);
				seekBarBtnTreble.setFocusable(true);
			}
			//add by hz for mantis:0020913
			if(TvCecManager.getInstance().getCecConfiguration().arcStatus ==1){
				comboBtnSoundmode.setEnable(false);
				comboBtnSoundmode.setFocusable(false);
			}
			//end
		}
    }
    private TVRootApp getApp()
    {
        return (TVRootApp) activity.getApplication();
    }
/*----------------------end---------------------*/
}
