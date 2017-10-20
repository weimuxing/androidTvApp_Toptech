// <MStar Software>
// ******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software")
// are
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
// Software and any modification/derivatives thereof.
// No right, ownership, or interest to MStar Software and any
// modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
// supplied together with third party's software and the use of MStar
// Software may require additional licenses from third parties.
// Therefore, you hereby agree it is your sole responsibility to separately
// obtain any and all third party right and license necessary for your use of
// such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
// MStar's confidential information and you agree to keep MStar's
// confidential information in strictest confidence and not disclose to any
// third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
// kind. Any warranties are hereby expressly disclaimed by MStar, including
// without limitation, any warranties of merchantability, non-infringement of
// intellectual property rights, fitness for a particular purpose, error free
// and in conformity with any international standard. You agree to waive any
// claim against MStar for any loss, damage, cost or expense that you may
// incur related to your use of MStar Software.
// In no event shall MStar be liable for any direct, indirect, incidental or
// consequential damages, including without limitation, lost of profit or
// revenues, lost or damage of data, and unauthorized system use.
// You agree that this Section 4 shall still apply without being affected
// even if MStar Software has been modified by MStar in accordance with your
// request or instruction for your use, except otherwise agreed by both
// parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
// services in relation with MStar Software to you for your use of
// MStar Software in conjunction with your or your customer's product
// ("Services").
// You understand and agree that, except otherwise agreed by both parties in
// writing, Services are provided on an "AS IS" basis and the warranty
// disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
// or otherwise:
// (a) conferring any license or right to use MStar name, trademark, service
// mark, symbol or any other identification;
// (b) obligating MStar or any of its affiliates to furnish any person,
// including without limitation, you and your customers, any assistance
// of any kind whatsoever, or any information; or
// (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
// of Taiwan, R.O.C., excluding its conflict of law rules.
// Any and all dispute arising out hereof or related hereto shall be finally
// settled by arbitration referred to the Chinese Arbitration Association,
// Taipei in accordance with the ROC Arbitration Law and the Arbitration
// Rules of the Association by three (3) arbitrators appointed in accordance
// with the said Rules.
// The place of arbitration shall be in Taipei, Taiwan and the language shall
// be English.
// The arbitration award shall be final and binding to both parties.
//
// ******************************************************************************
// <MStar Software>

package com.mstar.tv.tvplayer.ui;

import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.holder.ChannelViewHolder;
import com.mstar.tv.tvplayer.ui.holder.DemoViewHolder;
import com.mstar.tv.tvplayer.ui.holder.MenuOf3DViewHolder;
import com.mstar.tv.tvplayer.ui.holder.PictureViewHolder;
import com.mstar.tv.tvplayer.ui.holder.SettingViewHolder;
import com.mstar.tv.tvplayer.ui.holder.SoundViewHolder;
import com.mstar.tv.tvplayer.ui.holder.TimeViewHolder;
import com.mstar.tv.tvplayer.ui.holder.OptionViewHolder;
import com.mstar.tv.tvplayer.ui.holder.ParentalControlViewHolder;
import com.mstar.tvframework.MstarBaseActivity;
import com.mstar.util.Constant;
import com.mstar.util.Tools;

import android.content.ComponentName;

import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import android.os.SystemProperties;

public class MainMenuActivity extends MstarBaseActivity implements OnGestureListener {
	private List<Map<String, Object>> listItem;
	public ListView listView = null;
	private ListViewAdapter linkAdapter;
	private static final String TAG = "MainMenuActivity";

	private int mTvSystem = 0;

	private GestureDetector detector;

	protected ViewFlipper viewFlipper = null;

	protected LayoutInflater lf;

	private TVRootApp  app = null;

	protected static boolean hasAdd;

	public static int selectedstatusforChannel = 0x00000000;

	public static int selectedsStatusForDemo = 0x00000000;

	protected int currentPage = PICTURE_PAGE;

	public final static int PICTURE_PAGE = 0;

	public final static int SOUND_PAGE = 1;

	public final static int CHANNEL_PAGE = 2;

	public final static int SETTING_PAGE = 3;

	public final static int TIME_PAGE = 4;

	public final static int LOCK_PAGE = 5;

    public final static int OPTION_PAGE = 6;
    
	public final static int S3D_PAGE = 7;

	public final static int DEMO_PAGE = 8;

	private int mMaxPageIdx = 5;

	protected PictureViewHolder pictureViewHolder;

	protected SoundViewHolder soundViewHolder;

	protected MenuOf3DViewHolder menuOf3DViewHolder;

	protected TimeViewHolder timeViewHolder;

	protected ChannelViewHolder menuOfChannelViewHolder;

	protected SettingViewHolder menuOfSettingViewHolder;

	protected DemoViewHolder menuOfDemoViewHolder;

	protected OptionViewHolder menuOfOptionViewHolder;

	protected ParentalControlViewHolder menuOfParentalControlViewHolder;

	protected LinearLayout MainMenu_Surface;

	private static boolean NeedSaveBitmap = true;

	public static Bitmap currentBitmapImg = null;

	public static KeyEvent currentKeyEvent = null;

	private static MainMenuActivity mainMenuActivity = null;

	// To remember focus
	private LinearLayout curLinearLayout;

	protected static int[] curFocusedViewIds;

	final static int LANGUAGE_CHANGE_MSG = 1080;

	private MainMenuPauseReceiver mainmenupausereceiver;

	private boolean needRestartMainMenu = false;

	private boolean onCreatFlag = false;

	protected static boolean bMainMenuFocused = false;

	private int mOptionSelectStatus = 0x00000000;

	private int mParentalControlSelectStatus = 0x00000000;

	private boolean mIsPwdCorrect = false;
	private int mSelectedStatusInSetting = 0x00000000;

	// zb20141008 add
	private final static boolean is3DFlag = false;
	// end

	// add by jerry 20160304
	// cache the view, speed up....
	protected View picture_page_view = null;
	protected View sound_page_view = null;
	protected View channel_page_view = null;
	protected View setting_page_view = null;
	protected View time_page_view = null;
	protected View demo_page_view = null;
	protected View s3d_page_view = null;
	protected View option_page_view = null;
	protected View lock_page_view = null;
	protected int saved_page = -1;
	// end

	private final static int FLAG_INIT_CURRENT_PAGE = 7758521;
	private final static int FLAG_INIT_OTHER_PAGE = 1990;

	// moving the entrance of factory menu for customized.lxk 20141226
	private ArrayList<Integer> mKeyQueue = new ArrayList<Integer>();

	final static int KEYQUEUE_SIZE = 4;

	private final static String GOODKEYCODES = String.valueOf(KeyEvent.KEYCODE_2) + String.valueOf(KeyEvent.KEYCODE_0)
			+ String.valueOf(KeyEvent.KEYCODE_0) + String.valueOf(KeyEvent.KEYCODE_8);
	private final static String GOODKEYCODES2 = String.valueOf(KeyEvent.KEYCODE_1) + String.valueOf(KeyEvent.KEYCODE_2)
			+ String.valueOf(KeyEvent.KEYCODE_0) + String.valueOf(KeyEvent.KEYCODE_1);
	
	private final static String FACTORYEYCODES = String.valueOf(KeyEvent.KEYCODE_1)
			+ String.valueOf(KeyEvent.KEYCODE_9) + String.valueOf(KeyEvent.KEYCODE_7)
			+ String.valueOf(KeyEvent.KEYCODE_9);
	// end
	// zb20150120 add
	private final static String RESETTVKEYCODES = String.valueOf(KeyEvent.KEYCODE_6)
			+ String.valueOf(KeyEvent.KEYCODE_9) + String.valueOf(KeyEvent.KEYCODE_6)
			+ String.valueOf(KeyEvent.KEYCODE_9);
	private final static String AGINGKEYCODES = String.valueOf(KeyEvent.KEYCODE_8) + String.valueOf(KeyEvent.KEYCODE_0)
			+ String.valueOf(KeyEvent.KEYCODE_8) + String.valueOf(KeyEvent.KEYCODE_0);
	// end

	private boolean mIsAnimationEnd = true;

	private final int SWIPE_LEFT_ONE_PAGE = -1;

	private final int SWIPE_RIGHT_ONE_PAGE = 1;
	public boolean mIsControlPermitted = false;

	public static MainMenuActivity getInstance() {
		return mainMenuActivity;
	}

	AnimationListener mAnimationListener = new Animation.AnimationListener() {
		public void onAnimationStart(Animation animation) {
			mIsAnimationEnd = false;
		}

		public void onAnimationRepeat(Animation animation) {
			mIsAnimationEnd = false;
		}

		public void onAnimationEnd(Animation animation) {
			mIsAnimationEnd = true;
		}
	};

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			if (pictureViewHolder != null) {
				// pictureViewHolder.toHandleMsg(msg);
			}
			if (timeViewHolder != null) {
				timeViewHolder.toHandleMsg(msg);
			}
			if (msg.what == FLAG_INIT_OTHER_PAGE) {
				addOtherView();
			}
			else if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
				setExitTypeResult("TimeOut");
				Intent intent = new Intent(MainMenuActivity.this, RootActivity.class);
				startActivity(intent);
			}
			else if (msg.what == LittleDownTimer.SELECT_RETURN_MSG) {
				if (!(getCurrentFocus() instanceof LinearLayout))
					return;
				curLinearLayout = (LinearLayout) getCurrentFocus();
				if (curLinearLayout != null) {
					if (selectedstatusforChannel != 0x00000000
					    && mSelectedStatusInSetting != 0x00000000
						&& selectedsStatusForDemo != 0x00000000
						&& mOptionSelectStatus != 0x00000000
						&& mParentalControlSelectStatus != 0x00000000)
					{
						selectedstatusforChannel = 0x00000000;
						mSelectedStatusInSetting = 0x00000000;
						selectedsStatusForDemo = 0x00000000;
						mOptionSelectStatus = 0x00000000;
						mParentalControlSelectStatus = 0x00000000;
					}
					// curLinearLayout.clearFocus();
					curLinearLayout.requestFocus();
					// curLinearLayout.setSelected(false);
				}
			}
			else if (msg.what == LANGUAGE_CHANGE_MSG) {
				restoreCurFocus();
				// recordCurFocusViewId();
				// saveFocusDataToSys();
				setContentView(R.layout.main_menu);
				viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper_main_menu);
				pictureViewHolder = null;
				soundViewHolder = null;
				menuOf3DViewHolder = null;
				timeViewHolder = null;
				menuOfChannelViewHolder = null;
				menuOfSettingViewHolder = null;
				menuOfDemoViewHolder = null;
				menuOfOptionViewHolder = null;
				menuOfParentalControlViewHolder = null;
				initUIComponent(currentPage);
				LittleDownTimer.resetItem();
				setLanguageItemSelected();
			}
			else if (msg.what == FLAG_INIT_CURRENT_PAGE) {
				{
					if ((getIntent() != null) && (getIntent().getExtras() != null)) {
						int pos = PICTURE_PAGE;
						currentPage = getIntent().getIntExtra("currentPage", PICTURE_PAGE);
						//keyInjection(getIntent().getIntExtra("currentKeyCode", 0));
						pos = currentPage;
						
						if (!isSourceInTv()) {
							if (currentPage == SETTING_PAGE)
								pos = CHANNEL_PAGE;
							else if (currentPage == TIME_PAGE)
								pos = SETTING_PAGE;
							else if (currentPage == LOCK_PAGE) {
								pos = TIME_PAGE;
							}
						} else {
							if (currentPage == LOCK_PAGE)
								pos = DEMO_PAGE;
						}
						
						listView.setSelection(pos);
					}
					if (onCreatFlag) {
						addCurrentView(currentPage);
						//listView.requestFocus();
						initUIComponent(currentPage);
						selectedstatusforChannel = 0x00000000;
						mSelectedStatusInSetting = 0x00000000;
						selectedsStatusForDemo = 0x00000000;
						mOptionSelectStatus = 0x00000000;
						mParentalControlSelectStatus = 0x00000000;
					}
					LittleDownTimer.resumeMenu();
					LittleDownTimer.resumeItem();
					currentBitmapImg = null;
					currentKeyEvent = null;
					if (currentPage == TIME_PAGE) {
						timeViewHolder.loadDataToMyBtnOffTime();
						timeViewHolder.loadDataToMyBtnScheduledTime();
					}else if(currentPage == LOCK_PAGE){
						menuOfParentalControlViewHolder.updateUi();
						Log.d(TAG,"updateUi");
					}
					//freshUI();
				}
				if (onCreatFlag) {
					// new timer service
					loadFocusDataFromSys();
					LittleDownTimer.setHandler(handler);
					mainmenupausereceiver = new MainMenuPauseReceiver();
					IntentFilter filter1 = new IntentFilter();
					filter1.addAction("mstar.tvsetting.ui.pausemainmenu");
					filter1.addAction("mstar.tvsetting.ui.finishmainmenu");
					registerReceiver(mainmenupausereceiver, filter1);
//					 if (getSharedPreferences("TvSetting",0).getBoolean("_3Dflag", false)) { // get
//						 3DopenflagMainMenu_Surface.setBackgroundDrawable(getResources().getDrawable(R.drawable.picture_mode_img_3d_bg));
//						 RootActivity.my3DHandler.sendEmptyMessage(RootActivity._3DAction.justshow);
//					 }
					onCreatFlag = false;
				}
				// zb20150314 add
				if (!hasAdd) {
					handler.sendEmptyMessageDelayed(FLAG_INIT_OTHER_PAGE, 100);
					// addOtherView();
				}
				// end
			}
		};
	};
	private boolean mainLR = true;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "--------------> startActivity->MainMenuActivity begin " + System.currentTimeMillis());
		super.onCreate(savedInstanceState);
		app = (TVRootApp)getApplication();
		// zb20141008 modify
		if (is3DFlag)
			setContentView(R.layout.main_menu3d);
		else if (app.isCusOnida()) {
			setContentView(R.layout.main_onida);
			}
		else
			setContentView(R.layout.main);
		// end
		mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
		if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
			mMaxPageIdx = 4; // Only 7 Pages for ATSC System
		} else if (app.isLockEnable() == false) {
			mMaxPageIdx = 4; 
		} else {
			mMaxPageIdx = 5; // Additional Page for Parental Control
		}
		if(currentPage != PICTURE_PAGE){
			mIsControlPermitted = true;
			leftORright = true;
			setParentalControlSelectStatus(0x00000001);
		}
		curFocusedViewIds = new int[mMaxPageIdx + 1];
		detector = new GestureDetector(this);
		mainMenuActivity = this;
		onCreatFlag = true;
		hasAdd = false;
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper_main_menu);
		MainMenu_Surface = (LinearLayout) findViewById(R.id.MainMenu);
		Log.i(TAG, "========== Display TV Menu ==========");
		Log.i(TAG, "========== Display TV Menu ========== " + System.currentTimeMillis());
		// box platform
		if (Tools.isBox()) {
			// make sure the default antenna type is DVB-C for box
			new ConfigAntennaTypeAsyncTask().execute();
		}
		listView = (ListView) findViewById(R.id.setting_lv);
		initListItem();

	}

	private void initListItem() {
		listItem = new ArrayList<Map<String, Object>>();
		TypedArray lv_text;
		if (isSourceInTv()) {
			lv_text = getResources().obtainTypedArray(R.array.more_setting_listview_text);
		}else if (app.isNOTV() || app.isCusOnida()) {
			lv_text = getResources().obtainTypedArray(R.array.more_setting_listview_nochannelnolock_text);
		} else {
			lv_text = getResources().obtainTypedArray(R.array.more_setting_listview_nochannel_text);
		}
		/*
		 * TypedArray lv_icon = getResources().obtainTypedArray(
		 * R.array.more_setting_listview_icon);
		 */
		// TypedArray
		// lv_selected_icon=getResources().obtainTypedArray(R.array.more_setting_listview_icon_focus);
		for (int i = 0; i < lv_text.length(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			// map.put("icon", lv_icon.getText(i));
			map.put("text", lv_text.getText(i));
			// map.put("focus_icon", lv_selected_icon.getText(i));
			/*
			 * if(i==0)
			 * map.put("focus", false);
			 * else {
			 * map.put("focus", false);
			 * }
			 */
			// map.put("selected", false);
			// map.put("focus_img_show", false);
			listItem.add(map);
		}
		linkAdapter = new ListViewAdapter(this, listItem);
		listView.setAdapter(linkAdapter);
		//listView.requestFocus();

		listView.setOnItemClickListener(lvOnItemClickListener);
		listView.setOnItemSelectedListener(lvOnItemSelectedListener);// skye
	}

	private OnItemClickListener lvOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			linkAdapter.setFocusPosition(position);
			if (!isSourceInTv()) {
			    if (position > SOUND_PAGE) position += 1;
				currentPage = position;
				/*
				if (position == CHANNEL_PAGE)
					position = SETTING_PAGE;
				else if (position == SETTING_PAGE)
					position = TIME_PAGE;
				else if (position == TIME_PAGE) {
					position = LOCK_PAGE;
					currentPage = position;
				}
				*/
				initUIComponent(position);
			} else {
			    /*
				if (position == DEMO_PAGE)
					position = LOCK_PAGE;
				*/
				initUIComponent(position);
				currentPage = position;
			}
		}
	};
	private OnItemSelectedListener lvOnItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
			// TODO Auto-generated method stub
			linkAdapter.setFocusPosition(position);
			if (!isSourceInTv()) {
			    if (position > SOUND_PAGE) position += 1;
				currentPage = position;
				/*
				if (position == CHANNEL_PAGE)
					position = SETTING_PAGE;
				else if (position == SETTING_PAGE)
					position = TIME_PAGE;
				else if (position == TIME_PAGE) {
					position = LOCK_PAGE;
					currentPage = position;
				}
				*/
				initUIComponent(position);
				currentPage = position;
			} else {
				//if (position == DEMO_PAGE)
				//	position = LOCK_PAGE;
				initUIComponent(position);
				currentPage = position;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();
		//add by pan for menu focus
		Log.d(TAG,"onresume");
		View view = mainMenuActivity.findViewById(android.R.id.content);
        if (null != view) {
            view.animate().alpha(1f)
                    .setDuration(/*mainMenuActivity.getResources().getInteger(android.R.integer.config_shortAnimTime)*/50);
        }
          //add end
		freshUI();
		if (TvCecManager.getInstance().getCecConfiguration().cecStatus == Constant.CEC_STATUS_ON) {
			TvCecManager.getInstance().enableDeviceMenu();
		}
		// Charles
		if (RootActivity.mExitDialog != null) {
			if (RootActivity.mExitDialog.isShowing()) {
				RootActivity.mExitDialog.dismiss();
			}
		}
		bMainMenuFocused = true;

		saved_page = -1;
		
		mKeyQueue.clear();
		lf = LayoutInflater.from(MainMenuActivity.this);
		handler.sendEmptyMessage(FLAG_INIT_CURRENT_PAGE);
	}

	protected void addCurrentView(int pageId) {
		if (viewFlipper.getChildCount() > mMaxPageIdx) {
			return;
		}
		viewFlipper.removeAllViews();
		for (int i = 0; i <= pageId; i++) {
			addView(i);
		}
	}

	protected void addView(int id) {
		switch (id) {
		case PICTURE_PAGE:
			try {
			    if (picture_page_view == null)
			        picture_page_view = lf.inflate(R.layout.picture, null);
				viewFlipper.addView(picture_page_view, PICTURE_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case SOUND_PAGE:
			try {
			    if (sound_page_view == null)
			        sound_page_view = lf.inflate(R.layout.sound, null);
				viewFlipper.addView(sound_page_view, SOUND_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case CHANNEL_PAGE:
			try {
			    if (channel_page_view == null)
			        channel_page_view = lf.inflate(R.layout.channel, null);
				viewFlipper.addView(channel_page_view, CHANNEL_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case SETTING_PAGE:
			try {
			    if (setting_page_view == null)
			        setting_page_view = lf.inflate(R.layout.setting, null);
				View viewSetting = setting_page_view;
				TvCommonManager tvCommonManager = TvCommonManager.getInstance();
				LinearLayout linearlayoutSetting = (LinearLayout) viewSetting.findViewById(R.id.linearlayout_setting);
				if (!tvCommonManager.isSupportModule(TvCommonManager.MODULE_PREVIEW_MODE)) {
					LinearLayout linearlayoutSetSourcepreview = (LinearLayout) viewSetting
							.findViewById(R.id.linearlayout_set_sourcepreview);
					linearlayoutSetting.removeView(linearlayoutSetSourcepreview);
				}
				if (!tvCommonManager.isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
					LinearLayout linearlayoutSetAutosourceident = (LinearLayout) viewSetting
							.findViewById(R.id.linearlayout_set_autosourceident);
					linearlayoutSetting.removeView(linearlayoutSetAutosourceident);
					LinearLayout linearlayoutSetAutosourceswit = (LinearLayout) viewSetting
							.findViewById(R.id.linearlayout_set_autosourceswit);
					linearlayoutSetting.removeView(linearlayoutSetAutosourceswit);
				}
				viewFlipper.addView(viewSetting, SETTING_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case TIME_PAGE:
			try {
			    if (time_page_view == null)
			        time_page_view = lf.inflate(R.layout.time, null);
				viewFlipper.addView(time_page_view, TIME_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case DEMO_PAGE:
			try {
			    if (demo_page_view == null)
			        demo_page_view = lf.inflate(R.layout.demo, null);
				viewFlipper.addView(demo_page_view, DEMO_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case S3D_PAGE:
			try {
			    if (s3d_page_view == null)
			        s3d_page_view = lf.inflate(R.layout.menu_of_3d, null);
				viewFlipper.addView(s3d_page_view, S3D_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case OPTION_PAGE:
			try {
			    if (option_page_view == null)
			        option_page_view = lf.inflate(R.layout.option, null);
				viewFlipper.addView(option_page_view, OPTION_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case LOCK_PAGE:
			try {
			    if (lock_page_view == null)
			        lock_page_view = lf.inflate(R.layout.menu_parentalcontrol, null);
				viewFlipper.addView(lock_page_view, LOCK_PAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	protected void addOtherView() {
		if (true == hasAdd) {
			return;
		}
		hasAdd = true;
		for (int i = viewFlipper.getChildCount(); i <= mMaxPageIdx; i++) {
			addView(i);
		}
	}

	// TODO: remove this function if it's Useless
	private void freshUI() {
		if (pictureViewHolder != null) {
			pictureViewHolder.LoadDataToUI();
		}
		if (soundViewHolder != null) {
			soundViewHolder.LoadDataToUI();
		}
		if (menuOf3DViewHolder != null) {
			menuOf3DViewHolder.LoadDataToUI();
		}
		if (timeViewHolder != null) {
			timeViewHolder.loadDataToUI();
		}
		if (menuOfChannelViewHolder != null) {
			menuOfChannelViewHolder.findViews();
		}
		if (menuOfSettingViewHolder != null) {
			menuOfSettingViewHolder.findViews();
		}
		if (menuOfDemoViewHolder != null) {
			menuOfDemoViewHolder.findViews();
		}
		if (menuOfOptionViewHolder != null) {
			menuOfOptionViewHolder.findViews();
		}
		if (menuOfParentalControlViewHolder != null) {
			menuOfParentalControlViewHolder.updateUi();
		}
	}

	@Override
	protected void onPause() {
		try {
			LittleDownTimer.pauseMenu();
			LittleDownTimer.pauseItem();
			bMainMenuFocused = false;
			SharedPreferences settings = this.getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
					Context.MODE_PRIVATE);
			boolean flag = settings.getBoolean("_3Dflag", false);
			if (flag) {
				boolean drawDone = false;
				Canvas myCanvas = getBitmapCanvas();
				if (myCanvas != null) {
					MainMenu_Surface.draw(myCanvas);
					drawDone = true;
				}
				if (NeedSaveBitmap && drawDone) {
					try {
						String picName = "";
						int systemLanguage = TvCommonManager.getInstance().getOsdLanguage();
						if (systemLanguage == TvLanguage.ENGLISH) {
							picName = String.format("mainmenu_eng_pic_%d", currentPage);
						} else if (systemLanguage == TvLanguage.CHINESE) {
							picName = String.format("mainmenu_chn_pic_%d", currentPage);
						} else if (systemLanguage == TvLanguage.ACHINESE) {
							picName = String.format("mainmenu_tw_pic_%d", currentPage);
						}
						saveToBitmap(currentBitmapImg, picName);
						Log.v(TAG, "drawBitmap saveToBitmap");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			recordCurFocusViewId();
			saveFocusDataToSys();
		} catch (Exception e) {
		}

		if (null != menuOfSettingViewHolder) {
			menuOfSettingViewHolder.closeDialogs();
		}
		if (null != menuOfChannelViewHolder) {
			menuOfChannelViewHolder.closeDialogs();
		}
		if (null != menuOfOptionViewHolder) {
			menuOfOptionViewHolder.closeDialogs();
		}
		if (null != menuOfParentalControlViewHolder) {
			menuOfParentalControlViewHolder.setControlPermitted(false);
			menuOfParentalControlViewHolder.closeDialogs();
		}
		// selectedparental = 0x00000000;
		// disable by jerry 20160113
		//if (TvCecManager.getInstance().getCecConfiguration().cecStatus == Constant.CEC_STATUS_ON) {
		//	TvCecManager.getInstance().disableDeviceMenu();
		//}
		super.onPause();
	}

	@Override
	public void onUserInteraction() {
		LittleDownTimer.resetMenu();
		LittleDownTimer.resetItem();
		super.onUserInteraction();
	}

	private void keyInjection(final int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				|| keyCode == KeyEvent.KEYCODE_ENTER) { //Bingo 20160630 modify for mantis bug: 0017312
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

	public static boolean isSourceInTv() {
		int curis = TvCommonManager.getInstance().getCurrentTvInputSource();
		if (curis == TvCommonManager.INPUT_SOURCE_ATV || curis == TvCommonManager.INPUT_SOURCE_DTV) {
			return true;
		} else {
			return false;
		}
	}

    // true  in the viewpage
    // false in the listview
	private boolean leftORright = false;// skye 201504

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag;
		SharedPreferences settings;

		// If MapKeyPadToIR returns true,the previous keycode has been changed
		// to responding
		// android d_pad keys,just return.
		if (MapKeyPadToIR(keyCode, event))
			return true;

		Log.d(TAG, " ---onkeydown:" + keyCode + " with leftORright:" + leftORright);
		/*
		 * if (selectedstatusforChannel == 0x00000000 &&
		 * mSelectedStatusInSetting == 0x00000000
		 * && selectedsStatusForDemo == 0x00000000 && mOptionSelectStatus ==
		 * 0x00000000
		 * && mParentalControlSelectStatus ==
		 * 0x00000000&&mSelectedStatusInSetting==0x00000000) {
		 */
		Intent intent2;
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			if (leftORright) {
				int sselect;
				leftORright = false;
				listView.setFocusable(true);
				listView.requestFocus();
				sselect = Constant.SAVE_CURRENT_PAGE;
				if (!isSourceInTv() && sselect > CHANNEL_PAGE)
					sselect -= 1;
				listView.setSelection(sselect);
			} else {
				setExitTypeResult("ManualExit");
				Intent intentRoot = new Intent(this, RootActivity.class);
				startActivity(intentRoot);
			}
			return true;
		case KeyEvent.KEYCODE_TV_INPUT:
//			needRestartMainMenu = true;
//			Intent intentSource = new Intent(
//					"com.mstar.tvsetting.switchinputsource.intent.action.PictrueChangeActivity");
//			startActivity(intentSource);
//			Log.d(TAG,"Show PictrueChangeActivity!");
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			// if (swipePage(SWIPE_LEFT_ONE_PAGE)) {
			// return true;
			// }
			// break;
			if (leftORright) {
				if (currentPage == SETTING_PAGE) {
					View view = getCurrentFocus();
					if (view.getId() == R.id.linearlayout_source)
						break;
					else
						menuOfSettingViewHolder.onKeyDown(keyCode, event);
					break;
				}
			}
			return true;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			// if (swipePage(SWIPE_RIGHT_ONE_PAGE)) {
			// return true;
			// }
			// break;
			if (leftORright) {
				if (currentPage == SETTING_PAGE) {
					View view = getCurrentFocus();
					if (view.getId() == R.id.linearlayout_source)
						break;
					else
						menuOfSettingViewHolder.onKeyDown(keyCode, event);
					break;
				}
			} else {
				View view = getCurrentFocus();
				if (view.getId() == listView.getId())
					viewFlipper.getCurrentView().requestFocus();
				Constant.SAVE_CURRENT_PAGE = currentPage;
				leftORright = true;
				listView.setFocusable(false);
			}
			return true;
		case KeyEvent.KEYCODE_M:
			settings = this.getSharedPreferences(Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
			flag = settings.getBoolean("_3Dflag", false);
			if (flag) {
				intent2 = new Intent(this, MainMenu3DActivity.class);
				startActivity(intent2);
				// finish();
				// overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if ((currentPage == 0) && (leftORright == false) && !isSourceInTv()){
				if (app.isNOTV()) 
					listView.setSelection(3);
				else if (app.isCusOnida())
					listView.setSelection(3);
				else
					listView.setSelection(4);
			}
			if ((currentPage == 0) && (leftORright == false) && isSourceInTv())
				listView.setSelection(5);// skye 20150410
			View view = getCurrentFocus();
			if (view != null) {
				if (view.getId() == R.id.linearlayout_time_offtime) {
					if (findViewById(R.id.linearlayout_time_sleep) != null) {
						findViewById(R.id.linearlayout_time_sleep).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_demo_mwe) {
					if (findViewById(R.id.linearlayout_demo_uclear) != null) {
						findViewById(R.id.linearlayout_demo_uclear).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_3d_self_adaptive_detecttriple) {
					if (findViewById(R.id.linearlayout_3d_lrswitch) != null) {
						findViewById(R.id.linearlayout_3d_lrswitch).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_set_menutime) {
					if (findViewById(R.id.linearlayout_source).isFocusable() == true
							&&findViewById(R.id.linearlayout_source).getVisibility() == View.VISIBLE) {
						findViewById(R.id.linearlayout_source).requestFocus();
					}
					else if (findViewById(R.id.linearlayout_set_restoretodefault).isEnabled() == true) {
						findViewById(R.id.linearlayout_set_restoretodefault).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_source) {
					if ((findViewById(R.id.linearlayout_set_restoretodefault).getVisibility() == View.VISIBLE)
							&&(findViewById(R.id.linearlayout_set_restoretodefault).isFocusable() == true)) {
						findViewById(R.id.linearlayout_set_restoretodefault).requestFocus();
					}else if ((findViewById(R.id.linearlayout_hdmi_edid_version).getVisibility() == View.VISIBLE)
							&&(findViewById(R.id.linearlayout_hdmi_edid_version).isFocusable() == true)) {
						findViewById(R.id.linearlayout_hdmi_edid_version).requestFocus();
					}else if ((findViewById(R.id.linearlayout_set_cec_list).getVisibility() == View.VISIBLE)
							&&(findViewById(R.id.linearlayout_set_cec_list).isFocusable() == true)) {
						findViewById(R.id.linearlayout_set_cec_list).requestFocus();
					}else if ((findViewById(R.id.linearlayout_set_hdmicec).getVisibility() == View.VISIBLE)
							&&(findViewById(R.id.linearlayout_set_hdmicec).isFocusable() == true)) {
						findViewById(R.id.linearlayout_set_hdmicec).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_sound_soundmode) {
					if(findViewById(R.id.linearlayout_sound_equalizer).isFocusable()) {
						findViewById(R.id.linearlayout_sound_equalizer).requestFocus();
						return true;
					}else if (findViewById(R.id.linearlayout_sound_separatehear).isFocusable()) {
						findViewById(R.id.linearlayout_sound_separatehear).requestFocus();
						return true;
					} else if (findViewById(R.id.linearlayout_sound_spdifoutput).hasFocusable() != false) {
						findViewById(R.id.linearlayout_sound_spdifoutput).requestFocus();
						return true;
					}else if (findViewById(R.id.linearlayout_sound_avc).hasFocusable() != false) {
						findViewById(R.id.linearlayout_sound_avc).requestFocus();
						return true;
					}
				}
				else if (view.getId() == R.id.linearlayout_pic_picturemode) {
					if (findViewById(R.id.linearlayout_pic_imgnoisereduction).isEnabled() == false) {
						if (findViewById(R.id.linearlayout_pic_zoommode).isFocusable() == false)
							findViewById(R.id.linearlayout_pic_colortemperature).requestFocus();
						else
							findViewById(R.id.linearlayout_pic_zoommode).requestFocus();
						return true;
					}
				}
				// for channel page
				else if ((view.getId() == R.id.linearlayout_cha_antennatype)
				        || (view.getId() == R.id.linearlayout_cha_ntsc_antennatype)) {
                    if (findViewById(R.id.linearlayout_cha_signalinfo).hasFocusable() != false) {
						findViewById(R.id.linearlayout_cha_signalinfo).requestFocus();
						return true;
					} else if (findViewById(R.id.linearlayout_cha_programedit).hasFocusable() != false) {
						findViewById(R.id.linearlayout_cha_programedit).requestFocus();
						return true;
					}
				}
				else if (view.getId() == R.id.linearlayout_cha_autotuning) {
				    int curis = TvCommonManager.getInstance().getCurrentTvInputSource();
				    if(!findViewById(R.id.linearlayout_cha_antennatype).hasFocusable()
				    		&& !findViewById(R.id.linearlayout_cha_ntsc_antennatype).hasFocusable()){
						if (findViewById(R.id.linearlayout_cha_signalinfo).isFocusable()) {
							findViewById(R.id.linearlayout_cha_signalinfo).requestFocus();
							return true;
						}else if (findViewById(R.id.linearlayout_cha_favoritelist).getVisibility() == View.VISIBLE
							&&(findViewById(R.id.linearlayout_cha_favoritelist).isFocusable() == true)) {
							findViewById(R.id.linearlayout_cha_favoritelist).requestFocus();
							return true;
						}else if (findViewById(R.id.linearlayout_cha_programedit).hasFocusable() != false) {
							findViewById(R.id.linearlayout_cha_programedit).requestFocus();
							return true;
						}
						
				    }else{
					    if (curis == TvCommonManager.INPUT_SOURCE_DTV) {
	                        int currounte = TvChannelManager.getInstance().getCurrentDtvRoute();
	                        if(findViewById(R.id.linearlayout_cha_antennatype).hasFocusable()){
	//	                        if (currounte == TvChannelManager.DTV_ROUTE_DVBC) {
		                            findViewById(R.id.linearlayout_cha_antennatype).requestFocus();
		                            return true;
	//	                        }
	                        }
					    } else if (curis == TvCommonManager.INPUT_SOURCE_ATV) {
					        boolean isntsc = TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE);
	                        if (isntsc && findViewById(R.id.linearlayout_cha_ntsc_antennatype).hasFocusable()) {
	                            findViewById(R.id.linearlayout_cha_ntsc_antennatype).requestFocus();
	                            return true;
	                        }
					    }
				    }
				}
				
			}
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			// if ((currentPage==8)&&(leftORright == false))
			// listView.setSelection(0);//skye 20150410
			View view = getCurrentFocus();
			if (view != null) {
				if (view.getId() == R.id.linearlayout_time_sleep) {
					if (findViewById(R.id.linearlayout_time_offtime) != null) {
						findViewById(R.id.linearlayout_time_offtime).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_demo_uclear) {
					if (findViewById(R.id.linearlayout_demo_mwe) != null) {
						findViewById(R.id.linearlayout_demo_mwe).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_3d_lrswitch) {
					if (findViewById(R.id.linearlayout_3d_self_adaptive_detecttriple) != null) {
						findViewById(R.id.linearlayout_3d_self_adaptive_detecttriple).requestFocus();
					}
					return true;
				}
				//Device List
				else if (view.getId() == R.id.linearlayout_set_cec_list) { 
					if (findViewById(R.id.linearlayout_hdmi_edid_version).isFocusable()&&
							findViewById(R.id.linearlayout_hdmi_edid_version).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_hdmi_edid_version).requestFocus();
					}else if (findViewById(R.id.linearlayout_set_restoretodefault).isFocusable()
							&&findViewById(R.id.linearlayout_set_restoretodefault).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_restoretodefault).requestFocus();
					}else if (findViewById(R.id.linearlayout_source).isFocusable()
							&&findViewById(R.id.linearlayout_source).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_source).requestFocus();
					}else if (findViewById(R.id.linearlayout_set_menutime).isFocusable()
							&&findViewById(R.id.linearlayout_set_menutime).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_menutime).requestFocus();
					}
					return true;
				}
				//CEC
				else if (view.getId() == R.id.linearlayout_set_hdmicec) {
					if (findViewById(R.id.linearlayout_set_hdmiarc).isFocusable()&&
							findViewById(R.id.linearlayout_set_hdmiarc).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_hdmiarc).requestFocus();
					}else if (findViewById(R.id.linearlayout_set_cec_list).isFocusable()&&
							findViewById(R.id.linearlayout_set_cec_list).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_cec_list).requestFocus();
					}else if (findViewById(R.id.linearlayout_hdmi_edid_version).isFocusable()&&
							findViewById(R.id.linearlayout_hdmi_edid_version).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_hdmi_edid_version).requestFocus();
					}else if (findViewById(R.id.linearlayout_set_restoretodefault).isFocusable()
							&&findViewById(R.id.linearlayout_set_restoretodefault).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_restoretodefault).requestFocus();
					}else if (findViewById(R.id.linearlayout_source).isFocusable()
							&&findViewById(R.id.linearlayout_source).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_source).requestFocus();
					}else if (findViewById(R.id.linearlayout_set_menutime).isFocusable()
							&&findViewById(R.id.linearlayout_set_menutime).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_menutime).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_hdmi_edid_version) {
					if (findViewById(R.id.linearlayout_set_restoretodefault).isFocusable()
							&&findViewById(R.id.linearlayout_set_restoretodefault).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_restoretodefault).requestFocus();
					}else if(findViewById(R.id.linearlayout_source).isFocusable()
							&&findViewById(R.id.linearlayout_source).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_source).requestFocus();
					}else if (findViewById(R.id.linearlayout_set_menutime).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_menutime).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_set_restoretodefault) {
					if (findViewById(R.id.linearlayout_source).isFocusable()
							&&findViewById(R.id.linearlayout_source).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_source).requestFocus();
					}else if (findViewById(R.id.linearlayout_set_menutime).getVisibility()==View.VISIBLE) {
						findViewById(R.id.linearlayout_set_menutime).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_sound_separatehear) {
					if (findViewById(R.id.linearlayout_sound_equalizer).isFocusable()) {
						findViewById(R.id.linearlayout_sound_equalizer).requestFocus();
					}else if (findViewById(R.id.linearlayout_sound_soundmode).isFocusable()) {
						findViewById(R.id.linearlayout_sound_soundmode).requestFocus();
					}else if (findViewById(R.id.linearlayout_sound_balance).isFocusable()) {
						findViewById(R.id.linearlayout_sound_balance).requestFocus();
					}
					return true;
				}
				else if (view.getId() ==R.id.linearlayout_sound_equalizer) {
					if (findViewById(R.id.linearlayout_sound_soundmode).isFocusable()) {
						findViewById(R.id.linearlayout_sound_soundmode).requestFocus();
					}else if (findViewById(R.id.linearlayout_sound_balance).isFocusable()) {
						findViewById(R.id.linearlayout_sound_balance).requestFocus();
					}
					return true;
				}
				else if (view.getId() == R.id.linearlayout_pic_colortemperature) {
					if (findViewById(R.id.linearlayout_pic_imgnoisereduction).isEnabled() == false) {
						if (findViewById(R.id.linearlayout_pic_zoommode).isFocusable() == false) {
							findViewById(R.id.linearlayout_pic_picturemode).requestFocus();
							return true;
						}
					}
				}
				else if (view.getId() == R.id.linearlayout_pic_zoommode) {
					if (findViewById(R.id.linearlayout_pic_imgnoisereduction).isEnabled() == false) {
						findViewById(R.id.linearlayout_pic_picturemode).requestFocus();
						return true;
					}
				}
				//spdif
				else if (view.getId() == R.id.linearlayout_sound_spdifoutput) {
					if (findViewById(R.id.linearlayout_sound_soundmode).hasFocusable() != false) {
						findViewById(R.id.linearlayout_sound_soundmode).requestFocus();
						return true;
					}
				}
				//srs
				else if (view.getId() == R.id.linearlayout_sound_surround) {
					if ((!app.isSpdifEnable()) && findViewById(R.id.linearlayout_sound_soundmode).hasFocusable() 
							&& !findViewById(R.id.linearlayout_sound_separatehear).hasFocusable()
							&& !findViewById(R.id.linearlayout_sound_equalizer).hasFocusable()!= false) {
						Log.d("08.25", "---->");
						findViewById(R.id.linearlayout_sound_soundmode).requestFocus();
						return true;
					}
				}
				// for channel page
				else if (view.getId() == R.id.linearlayout_cha_signalinfo) {
				    int curis = TvCommonManager.getInstance().getCurrentTvInputSource();
				    if (curis == TvCommonManager.INPUT_SOURCE_DTV) {
                        int currounte = TvChannelManager.getInstance().getCurrentDtvRoute();
                       // if (currounte == TvChannelManager.DTV_ROUTE_DVBC || currounte == TvChannelManager.DTV_ROUTE_DVBT) {
                            findViewById(R.id.linearlayout_cha_antennatype).requestFocus();
                            return true;
                      //  }
				    } else if (curis == TvCommonManager.INPUT_SOURCE_ATV) {
				        boolean isntsc = TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE);
                        if (isntsc) {
                            findViewById(R.id.linearlayout_cha_ntsc_antennatype).requestFocus();
                            return true;
                        }
				    }
					findViewById(R.id.linearlayout_cha_autotuning).requestFocus();
					return true;
				}
				else if (view.getId() == R.id.linearlayout_cha_programedit) {
					if (findViewById(R.id.linearlayout_cha_favoritelist).getVisibility()==View.VISIBLE
							&&findViewById(R.id.linearlayout_cha_favoritelist).isFocusable()) {
						findViewById(R.id.linearlayout_cha_favoritelist).requestFocus();
						return true;
					}
					if (findViewById(R.id.linearlayout_cha_signalinfo).hasFocusable() == false) {
						int curis = TvCommonManager.getInstance().getCurrentTvInputSource();
						if(!findViewById(R.id.linearlayout_cha_antennatype).hasFocusable()
				    		&& !findViewById(R.id.linearlayout_cha_ntsc_antennatype).hasFocusable()){
							findViewById(R.id.linearlayout_cha_autotuning).requestFocus();
							return true;
						}
						else
					    if (curis == TvCommonManager.INPUT_SOURCE_DTV) {
	                        int currounte = TvChannelManager.getInstance().getCurrentDtvRoute();
//	                        if (currounte == TvChannelManager.DTV_ROUTE_DVBC || currounte == TvChannelManager.DTV_ROUTE_DVBT) {
                            findViewById(R.id.linearlayout_cha_antennatype).requestFocus();
                            return true;
//	                        }
				    } else if (curis == TvCommonManager.INPUT_SOURCE_ATV) {
				        boolean isntsc = TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE);
                        if (isntsc) {
                            findViewById(R.id.linearlayout_cha_ntsc_antennatype).requestFocus();
                            return true;
                        }
					    }
					}
				}else if (view.getId() == R.id.linearlayout_cha_favoritelist) {
					if (findViewById(R.id.linearlayout_cha_autotuning).getVisibility()==View.VISIBLE
							&&findViewById(R.id.linearlayout_cha_autotuning).isFocusable()) {
						findViewById(R.id.linearlayout_cha_autotuning).requestFocus();
						return true;
					}
				}
				
			}
			if (app.isNOTV()&&(currentPage == TIME_PAGE) && (leftORright == false))
				listView.setSelection(0);
			else if ((currentPage == LOCK_PAGE) && (leftORright == false))
				listView.setSelection(0);// skye 20150410
			else if (app.isCusOnida()&&(currentPage == TIME_PAGE) && (leftORright == false))
				listView.setSelection(0);
		}

		// modify for factory entrance.lxk 20141226
		mKeyQueue.add(keyCode);
		if (mKeyQueue.size() == KEYQUEUE_SIZE) {
			String keystr = intArrayListToString(mKeyQueue);
			if (keystr.equals(GOODKEYCODES)||keystr.equals(GOODKEYCODES2)) {
				Intent intent;

				mKeyQueue.clear();
				intent = new Intent("mstar.tvsetting.factory.intent.action.MainmenuActivity");
				startActivity(intent);
				finish();
				return true;
			} else if (keystr.equals(FACTORYEYCODES)) {
				Intent intent;

				mKeyQueue.clear();
				intent = new Intent("mstar.tvsetting.factory.intent.action.FactorymenuActivity");
				startActivity(intent);
				finish();
				return true;
			}
			// zb20150120 add
			else if (keystr.equals(RESETTVKEYCODES)) {
				TvFactoryManager.getInstance().restoreToDefault();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					TvManager.getInstance().setEnvironment("db_table", "0");
					// TvManager.getInstance().setEnvironment("top_auto_pwr_on",
					// "1");
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TvCommonManager.getInstance().rebootSystem("reboot");
				RootActivity.setRebootFlag(true);
				// sendBroadcast(new
				// Intent("android.intent.action.MASTER_CLEAR"));
				finish();
				return true;
			} else if (keystr.equals(AGINGKEYCODES)) {
				ComponentName componentName = new ComponentName("com.toptech.factorytools",
						"com.toptech.factorytools.AgingActivity");
				Intent intent1 = new Intent(Intent.ACTION_MAIN);
				intent1.setComponent(componentName);
				intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(intent1);
				finish();
				return true;
			}
			// end
			else {
				mKeyQueue.remove(0);
			}
		}
		// end
		return super.onKeyDown(keyCode, event);
	}

	private String intArrayListToString(ArrayList<Integer> al) {
		String str = "";
		for (int i = 0; i < al.size(); ++i) {
			str += al.get(i).toString();
		}
		return str;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// ignore any key up event from MStar Smart TV Keypad
		String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
		if (deviceName.equals("MStar Smart TV Keypad")) {
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	public boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
		String deviceName = InputDevice.getDevice(event.getDeviceId()).getName();
		Log.d(TAG, "deviceName:" + deviceName);

		if (!deviceName.equals("MStar Smart TV Keypad"))
			return false;
		// lyp 2014.10.11 add
		if (KeyEvent.KEYCODE_TV_INPUT == keyCode) {
			keyInjection(KeyEvent.KEYCODE_ENTER);
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_CHANNEL_UP:
			keyInjection(KeyEvent.KEYCODE_DPAD_UP);
			return true;
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
			keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
			return true;
		default:
			return false;
		}

	}

	private void setExitTypeResult(String result) {
		// RootActivity.MENU_EXIT_TYPE = result;
	}

	protected void initUIComponent(int page) {
		viewFlipper.setDisplayedChild(page);
		Log.d("wym", "initUIComponent "+page);
		if (saved_page == page && saved_page != -1)
		{
			if(!mIsControlPermitted)
            return;
		}
		saved_page = page;
		
		switch (page) {
		case PICTURE_PAGE:
			if (pictureViewHolder == null) {
				pictureViewHolder = new PictureViewHolder(MainMenuActivity.this);
				pictureViewHolder.findViews();
				pictureViewHolder.LoadDataToUI();
			}else
			pictureViewHolder.freshSignalStable();
			break;
		case SOUND_PAGE:
			if (soundViewHolder == null) {
				soundViewHolder = new SoundViewHolder(MainMenuActivity.this);
				soundViewHolder.findViews();
				soundViewHolder.LoadDataToUI();
			}
			if (soundViewHolder != null) {
				soundViewHolder.SetFocusableSurroundInARC();
			}
			break;
		case S3D_PAGE:
			if (menuOf3DViewHolder == null) {
				menuOf3DViewHolder = new MenuOf3DViewHolder(MainMenuActivity.this);
				menuOf3DViewHolder.findViews();
				menuOf3DViewHolder.LoadDataToUI();
			}
			menuOf3DViewHolder.updateUI(false);// for some cases support no 3d
			// function
			break;
		case TIME_PAGE:
			if (timeViewHolder == null) {
				timeViewHolder = new TimeViewHolder(MainMenuActivity.this, handler);
				timeViewHolder.findViews();
				timeViewHolder.loadDataToUI();
			}
			timeViewHolder.loadDataToMyBtnOffTime();
			timeViewHolder.loadDataToMyBtnScheduledTime();
			break;
		case CHANNEL_PAGE:
			if (menuOfChannelViewHolder == null) {
				menuOfChannelViewHolder = new ChannelViewHolder(MainMenuActivity.this);
				menuOfChannelViewHolder.findViews();
			}
			menuOfChannelViewHolder.updateUi();
			break;
		case SETTING_PAGE:
			if (menuOfSettingViewHolder == null) {
				menuOfSettingViewHolder = new SettingViewHolder(MainMenuActivity.this, handler);
				menuOfSettingViewHolder.findViews();
			}
			break;
		case DEMO_PAGE:
			if (menuOfDemoViewHolder == null) {
				menuOfDemoViewHolder = new DemoViewHolder(MainMenuActivity.this);
				menuOfDemoViewHolder.findViews();
			}
			break;
		case OPTION_PAGE:
			if (menuOfOptionViewHolder == null) {
				menuOfOptionViewHolder = new OptionViewHolder(MainMenuActivity.this);
				menuOfOptionViewHolder.findViews();
				menuOfOptionViewHolder.loadDataToUi();
			}
			break;
		case LOCK_PAGE:
			if (menuOfParentalControlViewHolder == null) {
				menuOfParentalControlViewHolder = new ParentalControlViewHolder(MainMenuActivity.this);
				menuOfParentalControlViewHolder.setControlPermitted(mIsControlPermitted);
				menuOfParentalControlViewHolder.findViews();
			}
			boolean bIsParentalControlPermitted = false;
			/*
			 * if ((getIntent() != null) && (getIntent().getExtras() != null)) {
			 * bIsParentalControlPermitted =
			 * getIntent().getBooleanExtra(Constant
			 * .PARENTAL_CONTROL_MENU_PERMITTED, false);
			 * }
			 */
			// zb20150213 add
			bIsParentalControlPermitted = SystemProperties.getBoolean("persist.sys.isinparentmenu", false);
			SystemProperties.set("persist.sys.isinparentmenu", "false");
			// end
			menuOfParentalControlViewHolder.setControlPermitted(mIsControlPermitted);
			menuOfParentalControlViewHolder.updateUi();
			break;
		}
		// initCurFocus();
	}

	protected void onStop() {
		Log.d(TAG,"onStop");
        if (null != menuOfSettingViewHolder) {
			menuOfSettingViewHolder.closeDialogs();
		}
		if (null != menuOfChannelViewHolder) {
			menuOfChannelViewHolder.closeDialogs();
		}
		if (null != menuOfOptionViewHolder) {
			menuOfOptionViewHolder.closeDialogs();
		}
		if (null != menuOfParentalControlViewHolder) {
			menuOfParentalControlViewHolder.setControlPermitted(false);
			menuOfParentalControlViewHolder.closeDialogs();
		}
		super.onStop();
	};
	@Override
	public void onDestroy() {
		if (timeViewHolder != null) {
			timeViewHolder.endUIClock();
			timeViewHolder.unregisterReceiver();
		}
		unregisterReceiver(mainmenupausereceiver);
		// if (this.getSharedPreferences("TvSetting", 0).getBoolean("_3Dflag",
		// false)
		// && RootActivity.MENU_EXIT_TYPE.equals(""))
		// RootActivity.my3DHandler.sendEmptyMessageDelayed(RootActivity._3DAction.hide,
		// 300);
		if (needRestartMainMenu) {
			setExitTypeResult("Restart");
			needRestartMainMenu = false;
		}
		super.onDestroy();
	}

	private void saveToBitmap(Bitmap bitmap, String bitName) throws IOException {
		FileOutputStream fOut = openFileOutput(bitName + ".png", Activity.MODE_PRIVATE);
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Canvas getBitmapCanvas() {
		final int cl = MainMenu_Surface.getLeft();
		final int ct = MainMenu_Surface.getTop();
		final int cr = MainMenu_Surface.getRight();
		final int cb = MainMenu_Surface.getBottom();
		if (currentBitmapImg == null) {
			final int width = cr - cl;
			final int height = cb - ct;
			if (width <= 0 || height <= 0) {
				return null;
			}
			try {
				currentBitmapImg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			} catch (OutOfMemoryError e) {
				return null;
			}
		}
		Canvas myCanvas = new Canvas(currentBitmapImg);
		return myCanvas;
	}

	private void recordCurFocusViewId() {
		View view = getCurrentFocus();
		if (view != null) {
			curFocusedViewIds[currentPage] = view.getId();
		}
	}

	private void saveFocusDataToSys() {
		SharedPreferences.Editor editor = getSharedPreferences(Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE)
				.edit();
		int i = 0;
		for (int id : curFocusedViewIds) {
			editor.putInt("page" + i, id);
			editor.commit();
			i++;
		}
	}

	private void initCurFocus() {
		if (curFocusedViewIds[currentPage] != 0) {
			View view = findViewById(curFocusedViewIds[currentPage]);
			if (view != null) {
				view.requestFocus();
			}
		}
	}

	private void restoreCurFocus() {
		curFocusedViewIds[PICTURE_PAGE] = R.id.linearlayout_pic_picturemode;
		curFocusedViewIds[SOUND_PAGE] = R.id.linearlayout_sound_soundmode;
		curFocusedViewIds[CHANNEL_PAGE] = R.id.linearlayout_cha_antennatype;
		curFocusedViewIds[SETTING_PAGE] = R.id.linearlayout_set_audio_language1;
		curFocusedViewIds[TIME_PAGE] = R.id.linearlayout_time_offtime;
		curFocusedViewIds[DEMO_PAGE] = R.id.linearlayout_demo_mwe;
		curFocusedViewIds[S3D_PAGE] = R.id.linearlayout_3d_3dto2d;
		curFocusedViewIds[OPTION_PAGE] = R.id.linearlayout_set_caption;
		if (TvCommonManager.TV_SYSTEM_ATSC != mTvSystem) {
			curFocusedViewIds[LOCK_PAGE] = R.id.linearlayout_lock_system;
		}
	}

	private void loadFocusDataFromSys() {
		SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
				Context.MODE_PRIVATE);
		for (int i = 0; i < curFocusedViewIds.length; i++) {
			curFocusedViewIds[i] = sharedPreferences.getInt("page" + i, 0);
		}
	}

	private void setLanguageItemSelected() {
		if (currentPage != SETTING_PAGE)
			return;
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout_set_language);
		mSelectedStatusInSetting = 0x00000001;
		menuOfSettingViewHolder.setFocusId(R.id.linearlayout_set_language);
		ImageView iv1 = (ImageView) ll.getChildAt(0);
		ImageView iv2 = (ImageView) ll.getChildAt(3);
		iv1.setVisibility(View.VISIBLE);
		iv2.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		/*
		 * if (e1.getX() - e2.getX() < -100) {
		 * swipePage(SWIPE_LEFT_ONE_PAGE);
		 * return true;
		 * } else if (e1.getX() - e2.getX() > 100) {
		 * swipePage(SWIPE_RIGHT_ONE_PAGE);
		 * return true;
		 * }
		 */
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	public int getOptionSelectStatus() {
		return mOptionSelectStatus;
	}

	public void setOptionSelectStatus(int status) {
		mOptionSelectStatus = status;
	}

	public int getParentalControlSelectStatus() {
		return mParentalControlSelectStatus;
	}

	public void setParentalControlSelectStatus(int status) {
		mParentalControlSelectStatus = status;
	}

	public int getSettingSelectStatus() {
		return mSelectedStatusInSetting;
	}

	public void setSettingSelectStatus(int status) {
		mSelectedStatusInSetting = status;
	}

	private class MainMenuPauseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// mute
			if (action.equals("mstar.tvsetting.ui.pausemainmenu")) {
				Log.i(TAG, "--------------recieved-------------");
				// finish();
				return;
				// from.finish();
			}else if (action.equals("mstar.tvsetting.ui.finishmainmenu")) {
				 finish();
				return;
			}
		}
	}

	// for box only
	private class ConfigAntennaTypeAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int currentRouteIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
			int dvbcRouteIndex = TvChannelManager.getInstance()
					.getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBC);
			Log.d(TAG, "ConfigAntennaTypeAsyncTask, antenna " + currentRouteIndex);
			if (currentRouteIndex != dvbcRouteIndex) {
				TvS3DManager.getInstance().setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
				TvChannelManager.getInstance().switchMSrvDtvRouteCmd(dvbcRouteIndex);
			}

			return null;
		}
	}

	private boolean swipePage(int pageOffset) {
		if (mIsAnimationEnd == false) {
			return false;
		}

		// Count page number since the index is zero-based
		final int nPages = mMaxPageIdx + 1;

		// Calculate the remainder to move to next (pageOffset) pages
		// This makes pageOffset between ( -nPages + 1) to (nPages - 1)
		pageOffset %= nPages;

		// No actually page move
		if (0 == pageOffset) {
			return false;
		}

		addOtherView();

		recordCurFocusViewId();
		SharedPreferences settings = this.getSharedPreferences(Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
		boolean flag = settings.getBoolean("_3Dflag", false);

		if (true == flag) {
			Intent intent = new Intent(TvIntent.ACTION_START_ROOTACTIVITY);
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			}
		} else {
			// Calculate the page number to be shown
			currentPage = (currentPage + nPages + pageOffset) % nPages;
			// Skip pages here
			if (currentPage == CHANNEL_PAGE) {
				if (!isSourceInTv()) {
					if (pageOffset < 0) {
						currentPage--;
					} else {
						currentPage++;
					}
				}
			}

			// Calculate again for some skipped pages
			currentPage = ((currentPage % nPages) + nPages) % nPages;

			// Setup Swipe Animation
			Animation animationFadeIn;
			if (pageOffset < 0) {
				// Go left
				animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.right_in);
				this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
			} else {
				// Go right
				animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
				this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
			}
			animationFadeIn.setAnimationListener(mAnimationListener);
			this.viewFlipper.setInAnimation(animationFadeIn);

			initUIComponent(currentPage);
		}
		return true;
	}
	
	//add by pan for mainmenu focus
	@Override
	public void finish() {
		// TODO Auto-generated method stub		
		Log.d(TAG,"finish");
		View view = mainMenuActivity.findViewById(android.R.id.content);
          if (null != view) {
            view.animate().alpha(0f)
                    .setDuration(/*mainMenuActivity.getResources().getInteger(android.R.integer.config_shortAnimTime)*/50);
          }
	}
	//add end
}
