package com.jrm.localmm.ui.music;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jrm.localmm.R;
import com.mstar.android.tv.TvAudioManager;

public class MusicSettingDialog extends Dialog {
	private static final String TAG = "MusicSettingDialog";
	private boolean Debug=true;
	private MusicPlayerActivity mContext;
	private LinearLayout music_model_LinearLayout;
	private LinearLayout music_surround_model_LinearLayout;
	private LinearLayout music_spdif_model_LinearLayout;
	private LinearLayout music_eq;
	private LinearLayout music_balance_LinearLayout;
	private TextView  music_Smode;
	private TextView  music_surround_val;
	private TextView music_spdif_val;
	private TextView music_balance_val;
	private TextView  music_eq_name;
	private int SmodeItem;
	private int SurroundModeItem;
	private int SpdifModeItem;
	private int Balance=0;
	private MusicEQSettingDialog mMusicEQSettingDialog;
	
	private String[] MusicModeArray;
	private String[] SurroundModeArray;
	private String[] SpdifModeArray;
	
	
	public MusicSettingDialog(MusicPlayerActivity context) {
		super(context, R.style.dialog);
		mContext = context;
		//mMusicPlayerActivity = videoPlaySettingDialog;
	}
	 private ArrayList<Integer> mKeyQueue = new ArrayList<Integer>();

	    final static int KEYQUEUE_SIZE = 4;

	    private final static String GOODKEYCODES = String
	            .valueOf(KeyEvent.KEYCODE_2)
	            + String.valueOf(KeyEvent.KEYCODE_0)
	            + String.valueOf(KeyEvent.KEYCODE_0)
	            + String.valueOf(KeyEvent.KEYCODE_8);
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//mMusicPlayerActivity.show();
		this.dismiss();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.music_setting_dialog);
		Window w = getWindow();
		Display display = w.getWindowManager().getDefaultDisplay();
		w.setTitle(null);
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Log.i(TAG, "metrics.density:" + metrics.density);
		int width = display.getWidth();
		int height = display.getHeight();
		if (metrics.density == 1.5) {
			width = (int) (display.getWidth() * 0.20);
			height = (int) (display.getHeight() * 0.30);
		} else if (metrics.density == 2.0) {
			width = (int) (display.getWidth() * 0.51);
			height = (int) (display.getHeight() * 0.50);
		}
		w.setLayout(width, height);
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams wl = w.getAttributes();
		w.setAttributes(wl);
		w.setBackgroundDrawableResource(android.R.color.transparent);
		initView();
		music_model_LinearLayout.requestFocus();
		MusicModeArray =mContext.getResources().getStringArray(R.array.tv_sound_mode_types);
		SmodeItem = TvAudioManager.getInstance().getAudioSoundMode();
		music_Smode.setText(MusicModeArray[SmodeItem]);
		if(SmodeItem!=0){
			music_eq.setFocusable(false);
			//music_eq.setClickable(false);
			music_eq_name.setTextColor(Color.GRAY);
		}else{
			music_eq.setFocusable(true);
			//music_eq.setClickable(true);
			music_eq_name.setTextColor(Color.WHITE);
		}
		SurroundModeArray =mContext.getResources().getStringArray(R.array.surround_onoff);
		SurroundModeItem = TvAudioManager.getInstance().getAudioSurroundMode();
		music_surround_val.setText(SurroundModeArray[SurroundModeItem]);
		SpdifModeArray =mContext.getResources().getStringArray(R.array.spdif_model);
		SpdifModeItem = TvAudioManager.getInstance().getAudioSpdifOutMode();
		music_spdif_val.setText(SpdifModeArray[SpdifModeItem]);
		//if(Debug) Log.d(TAG, "music_spdif_output now ="+SpdifModeItem);
		Balance=TvAudioManager.getInstance().getBalance();
		//if(music_balance_val==null)Log.d(TAG, "music_balance_val now =null");
		music_balance_val.setText(Balance-50+"");
		
	}

	private void initView() {
		music_model_LinearLayout =(LinearLayout)findViewById(R.id.music_eq_model);
		music_Smode = (TextView)findViewById(R.id.music_eq_model_val);
		music_surround_model_LinearLayout =(LinearLayout)findViewById(R.id.music_surround_model);
		music_surround_val = (TextView)findViewById(R.id.music_surround_model_val);
		music_spdif_model_LinearLayout =(LinearLayout)findViewById(R.id.music_spdif_model);
		music_spdif_val = (TextView)findViewById(R.id.music_spdif_model_val);
		music_eq = (LinearLayout)findViewById(R.id.music_eq);
		music_eq_name= (TextView) findViewById(R.id.music_eq_name);
		music_balance_LinearLayout=(LinearLayout)findViewById(R.id.music_balance_model);
		music_balance_val=(TextView)findViewById(R.id.music_balance_val);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_DPAD_UP) {
                if (music_model_LinearLayout.isFocused()) {
                    Log.d("lxk","music_eq.isFocusable()"+music_eq.isFocusable());
                    if(music_eq.isFocusable())
                        music_eq.requestFocus();
                    else
                        music_balance_LinearLayout.requestFocus();
                    return true;
                }
            }
            if (keyCode==KeyEvent.KEYCODE_DPAD_DOWN) {
                if (music_balance_LinearLayout.isFocused()) {
                    if(music_eq.isFocusable()){
                        music_eq.requestFocus();
                    }else{
                        music_model_LinearLayout.requestFocus();
                    }
                    return true;
                }
            }
		if (keyCode==KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (music_model_LinearLayout.isFocused()) {
				SmodeItem++;
				if (SmodeItem>MusicModeArray.length-1) SmodeItem=0;
				music_Smode.setText(MusicModeArray[SmodeItem]);
				TvAudioManager.getInstance().setAudioSoundMode(SmodeItem);
				Log.d("skk", "music_Smode now ="+SmodeItem);
				if(SmodeItem!=0){
					music_eq.setFocusable(false);
					//music_eq.setClickable(false);
					music_eq_name.setTextColor(Color.GRAY);
				}else{
					music_eq.setFocusable(true);
					//music_eq.setClickable(true);
					music_eq_name.setTextColor(Color.WHITE);
				}
				if(Debug)Log.d(TAG, "music_Smode now ="+TvAudioManager.getInstance().getAudioSoundMode());
			}
			if (music_surround_model_LinearLayout.isFocused()) {
				SurroundModeItem++;
				if (SurroundModeItem==2) SurroundModeItem=0;
				TvAudioManager.getInstance().setAudioSurroundMode(SurroundModeItem);
				music_surround_val.setText(SurroundModeArray[SurroundModeItem]);
			}
			if (music_spdif_model_LinearLayout.isFocused()) {
				SpdifModeItem++;
				 if (SpdifModeItem>SpdifModeArray.length-1) SpdifModeItem=0;
				 TvAudioManager.getInstance().setAudioSpdifOutMode(SpdifModeItem);
			     music_spdif_val.setText(SpdifModeArray[SpdifModeItem]);	
			}
			if (music_balance_LinearLayout.isFocused()) {
				Balance++;
				 if (Balance>100) Balance=100;
				 TvAudioManager.getInstance().setBalance(Balance);
				 music_balance_val.setText(Balance-50+"");	
			}
			
		}
		if (keyCode==KeyEvent.KEYCODE_DPAD_LEFT) {
			if (music_model_LinearLayout.isFocused()) {
			SmodeItem--;
			if (SmodeItem<0) SmodeItem=MusicModeArray.length-1;
			music_Smode.setText(MusicModeArray[SmodeItem]);
			TvAudioManager.getInstance().setAudioSoundMode(SmodeItem);
			if(SmodeItem!=0){
				music_eq.setFocusable(false);
				//music_eq.setClickable(false);
				music_eq_name.setTextColor(Color.GRAY);
			}else{
				music_eq.setFocusable(true);
				//music_eq.setClickable(true);
				music_eq_name.setTextColor(Color.WHITE);
			}
			if(Debug) Log.d(TAG, "music_Smode now ="+TvAudioManager.getInstance().getAudioSoundMode());
			}
			if (music_surround_model_LinearLayout.isFocused()) {
				SurroundModeItem--;
				if (SurroundModeItem==-1) SurroundModeItem=1;
				TvAudioManager.getInstance().setAudioSurroundMode(SurroundModeItem);
				music_surround_val.setText(SurroundModeArray[SurroundModeItem]);
			 if(Debug)	Log.d(TAG, "SurroundMode() now ="+TvAudioManager.getInstance().getAudioSurroundMode());
			}
			if (music_spdif_model_LinearLayout.isFocused()) {
				SpdifModeItem--;
				 if (SpdifModeItem<0) SpdifModeItem=SpdifModeArray.length-1;
				 TvAudioManager.getInstance().setAudioSpdifOutMode(SpdifModeItem);
			     music_spdif_val.setText(SpdifModeArray[SpdifModeItem]);	
			}
			if (music_balance_LinearLayout.isFocused()) {
				Balance--;
				 if (Balance<0) Balance=0;
				 TvAudioManager.getInstance().setBalance(Balance);
				 music_balance_val.setText(Balance-50+"");	
			}
		}
		if (keyCode==KeyEvent.KEYCODE_ENTER&&music_eq.isFocused()) {
			mMusicEQSettingDialog = new MusicEQSettingDialog(mContext,MusicSettingDialog.this);
			mMusicEQSettingDialog.show();
			this.dismiss();
		}
		
		mKeyQueue.add(keyCode);
        if (mKeyQueue.size() == KEYQUEUE_SIZE) {
            String keystr = intArrayListToString(mKeyQueue);
            if (keystr.equals(GOODKEYCODES)) {
                Intent intent;

                mKeyQueue.clear();
                intent = new Intent("mstar.tvsetting.factory.intent.action.MainmenuActivity");
                mContext.startActivity(intent);
                this.dismiss();
                return true;
            }
        }
		
		return super.onKeyDown(keyCode, event);
	}
	private String intArrayListToString(ArrayList<Integer> al) 
    {
        String str = "";
        for (int i = 0; i < al.size(); ++i) 
        {
            str += al.get(i).toString();
        }
        return str;
    }
	
}
