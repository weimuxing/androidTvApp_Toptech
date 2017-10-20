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

package mstar.tvsetting.factory.ui.designmenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.System;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.FactoryDB;
import mstar.tvsetting.factory.desk.FactoryDeskImpl;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.other.CISettingViewHolder;
import mstar.tvsetting.factory.ui.other.IPEnableMappingViewHolder;
import mstar.tvsetting.factory.ui.other.OtherOptionViewHolder;
import mstar.tvsetting.factory.ui.other.UrsaInfoViewHolder;
import mstar.tvsetting.factory.ui.panel.PanelInfoViewHolder;
import mstar.tvsetting.factory.ui.picture.ADCAdjustViewHolder;
import mstar.tvsetting.factory.ui.picture.NonLinearAdjustViewHolder;
import mstar.tvsetting.factory.ui.picture.NonStandardAdjustViewHolder;
import mstar.tvsetting.factory.ui.picture.OverScanAdjustViewHolder;
import mstar.tvsetting.factory.ui.picture.PEQAdjustViewHolder;
import mstar.tvsetting.factory.ui.picture.PictureModeAdjustViewHolder;
import mstar.tvsetting.factory.ui.picture.PictureViewHolder;
import mstar.tvsetting.factory.ui.picture.SSCAdjustViewHolder;
import mstar.tvsetting.factory.ui.picture.TestPatternViewHolder;
import mstar.tvsetting.factory.ui.picture.WBAdjustViewHolder;
import mstar.tvsetting.factory.ui.reset.ResetViewHolder;
import mstar.tvsetting.factory.ui.sound.FactorySoundViewHolder;
import mstar.tvsetting.factory.ui.sound.SoundModeViewHolder;
import mstar.tvsetting.factory.ui.sound.SoundNonLinearAdjustViewHolder;
import mstar.tvsetting.factory.ui.sound.SoundViewHolder;
import mstar.tvsetting.factory.ui.upgrade.UpgradeViewHolder;
import mstar.tvsetting.factory.ui.user.UserViewHolder;
import mstar.tvsetting.factory.until.Constant;

public class DesignMenuActivity extends BaseActivity {
    private static final String TAG = "DesignMenuActivity";

	private ViewFlipper viewFlipper;

	private IFactoryDesk factoryDesk;

	private TvFactoryManager tvFactoryManager;

	private TvCommonManager mTvCommonManager;

	private DesignMenuViewHolder designViewHolder;

	private PictureViewHolder pictureViewHolder;

	private ResetViewHolder resetViewHolder;

	private PanelInfoViewHolder panelViewHolder;

	private UserViewHolder userViewHolder;

	private OtherOptionViewHolder otheroptionViewHolder;

	private UpgradeViewHolder upgradeViewHolder;

	private SoundViewHolder soundViewHolder;

	private PictureModeAdjustViewHolder pictureModeAdjustViewHolder;

	private NonLinearAdjustViewHolder nonLinearAdjustViewHolder;

	private ADCAdjustViewHolder adcAdjustViewHolder;

	private WBAdjustViewHolder wbAdjustViewHolder;
	
	private PQAdjustViewHolder pqAdjustViewHolder;

	private OverScanAdjustViewHolder OverScanAdjustViewHolder;

	private NonStandardAdjustViewHolder nonStandardAdjustViewHolder;

	private SSCAdjustViewHolder sscAdjustViewHolder;

	private PEQAdjustViewHolder peqAdjustViewHolder;

	private TestPatternViewHolder testPatternViewHolder;
	
	private SoundNonLinearAdjustViewHolder soundNonLinearAdjustViewHolder;

	private SoundModeViewHolder soundModeViewHolder;

	private FactorySoundViewHolder soundAdjustViewHolder;

	private UrsaInfoViewHolder ursaInfoViewHolder;

	private IPEnableMappingViewHolder ipEnableMappingViewHolder;

	private CISettingViewHolder ciSettingViewHolder;
	
	public final int MAIN_PAGE = 0;

	public final int PICTURE_PAGE = 1;

	public final int SOUND_PAGE = 2;

	public final int PANEL_INFO_PAGE = 3;

	public final int USER_PAGE = 4;

	public final int UPGRADE_PAGE = 5;

	public final int OTHER_OPTION_PAGE = 6;

	public final int PICTURE_MODE_PAGE = 7;

	public final int NON_LINEAR_PAGE = 8;

	public final int ADC_ADJUST_PAGE = 9;

	public final int WHITE_BALANCE_PAGE = 10;

	public final int OVERSCAN_PAGE = 11;

	public final int NON_STANDARD_ADJUST_PAGE = 12;

	public final int SSC_ADJUST_PAGE = 13;

	public final int PEQ_ADJUST_PAGE = 14;
	
	public final int SOUND_NONLINEAR_PAGE = 15;

	public final int SOUND_MODE_PAGE = 16;

	public final int SOUND_ADJUST_PAGE = 17;

	public final int URSA_INFO_PAGE = 18;

	public final int IP_ENABLE_MAPPING_PAGE = 19;

	public final int CI_SETTING_PAGE = 20;

	public final int TEST_PATTERN_PAGE = 21;
	
	public final int PQ_ADJUST_PAGE = 22;
	// VIEWINDEX TO BUTTON
	private final int BUTTON_POSITION_MAIN_PAGE = 0;

	private final int BUTTON_POSITION_PICTURE_PAGE = 0;

	private final int BUTTON_POSITION_SOUND_PAGE = 1;

	private final int BUTTON_POSITION_PANEL_INFO_PAGE = 2;

	private final int BUTTON_POSITION_USER_PAGE = 3;

	private final int BUTTON_POSITION_UPGRADE_PAGE = 4;

	private final int BUTTON_POSITION_OTHER_OPTION_PAGE = 5;

	private final int BUTTON_POSITION_PICTURE_MODE_PAGE = 0;

	private final int BUTTON_POSITION_NON_LINEAR_PAGE = 1;

	private final int BUTTON_POSITION_ADC_ADJUST_PAGE = 2;

	private final int BUTTON_POSITION_WHITE_BALANCE_PAGE = 3;

	private final int BUTTON_POSITION_OVERSCAN_PAGE = 4;

	private final int BUTTON_POSITION_NON_STANDARD_OPTIONS_PAGE = 5;

	private final int BUTTON_POSITION_SSC_ADJUST_PAGE = 6;
	
	private final int BUTTON_POSITION_PQ_ADJUST_PAGE = 13;
	
	private final int BUTTON_POSITION_SOUND_NONLINEAR_PAGE = 0;

	private final int BUTTON_POSITION_SOUND_MODE_PAGE = 1;

	private final int BUTTON_POSITION_SOUND_ADJUST_PAGE = 2;

	private final int BUTTON_POSITION_URSA_INFO_PAGE = 3;

	private final int BUTTON_POSITION_IP_ENABLE_MAPPING = 4;

	private final int BUTTON_POSITION_PEQ_ADJUST_PAGE = 5;

	private final int BUTTON_POSITION_TEST_PATTERN_PAGE = 7;

	private final int BUTTON_POSITION_CI_SETTING_PAGE = 12;
	
	private boolean isM029;

	private boolean isFirst = true;

	private int currentPage = MAIN_PAGE;

	public int nonlineartype = 0;// 0- for volume only, 1 - non volume

	private static UsbManager usbman;

	public int scrollx;// this must little than zero

	private LinearLayout linearlayout_factory_menu;

	private LinearLayout linearlayout_other_optioon;

	private LinearLayout linearlayout_sound_menu;

	private LinearLayout linearlayout_picture_menu;

	private LinearLayout focusedLayout = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		viewFlipper = (ViewFlipper) findViewById(R.id.factory_view_flipper);
		usbman = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
		factoryDesk = FactoryDeskImpl.getInstance(this);
		FactoryDB.getInstance(this).openDB();
		factoryDesk.loadEssentialDataFromDB();
		designViewHolder = new DesignMenuViewHolder(DesignMenuActivity.this);
		designViewHolder.findView();
		registerListeners();
		isM029 = SystemProperties.getBoolean("mstar.cus.onida", false);
		isFirst = false;
		scrollx = 0;
		viewFlipper.scrollBy(scrollx, 0);

		linearlayout_factory_menu = (LinearLayout) findViewById(R.id.linearlayout_factory_menu);
		linearlayout_other_optioon = (LinearLayout) findViewById(R.id.linearlayout_other_optioon);
		linearlayout_sound_menu = (LinearLayout) findViewById(R.id.linearlayout_sound_menu);
		linearlayout_picture_menu = (LinearLayout) findViewById(R.id.linearlayout_picture_menu);

		focusedLayout = (LinearLayout) linearlayout_factory_menu.getChildAt(0);
	}
	
	public boolean isM029() {
		return isM029;
	}

	@Override
	protected void onResume() {
		if (!isFirst) {
			FactoryDB.getInstance(this).openDB();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (factoryDesk != null) {
			FactoryDB.getInstance(this).closeDB();
		}
		// Intent intent = new Intent("factoryDirty");
		// sendBroadcast(intent);
		super.onPause();
	}

	private void registerListeners() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.linearlayout_factory_picture:
					currentPage = PICTURE_PAGE;
					viewFlipper.setDisplayedChild(PICTURE_PAGE);
					pictureViewHolder = new PictureViewHolder(DesignMenuActivity.this, factoryDesk);
					pictureViewHolder.findView();
					pictureViewHolder.onCreate();
					break;
				case R.id.linearlayout_factory_sound:
					currentPage = SOUND_PAGE;
					viewFlipper.setDisplayedChild(SOUND_PAGE);
					soundViewHolder = new SoundViewHolder(DesignMenuActivity.this, factoryDesk);
					soundViewHolder.findView();
					break;
				case R.id.linearlayout_factory_panel_Info:
					currentPage = PANEL_INFO_PAGE;
					viewFlipper.setDisplayedChild(PANEL_INFO_PAGE);
					panelViewHolder = new PanelInfoViewHolder(DesignMenuActivity.this, factoryDesk);
					panelViewHolder.findView();
					panelViewHolder.onCreate();
					break;
				case R.id.linearlayout_factory_User:
					currentPage = USER_PAGE;
					viewFlipper.setDisplayedChild(USER_PAGE);
					userViewHolder = new UserViewHolder(DesignMenuActivity.this, factoryDesk);
					userViewHolder.findView();
					userViewHolder.onCreate();
					break;
				case R.id.linearlayout_factory_Upgrade:
					currentPage = UPGRADE_PAGE;
					viewFlipper.setDisplayedChild(UPGRADE_PAGE);
					upgradeViewHolder = new UpgradeViewHolder(DesignMenuActivity.this, factoryDesk);
					upgradeViewHolder.findView();
					upgradeViewHolder.onCreate();
					break;
				// move to sound page
				case R.id.linearlayout_factory_Other:
					currentPage = OTHER_OPTION_PAGE;
					viewFlipper.setDisplayedChild(OTHER_OPTION_PAGE);
					otheroptionViewHolder = new OtherOptionViewHolder(DesignMenuActivity.this, factoryDesk);
					otheroptionViewHolder.findView();
					otheroptionViewHolder.onCreate();
					break;
				case R.id.linearlayout_factory_Reset:
					new AlertDialog.Builder(DesignMenuActivity.this)
							.setTitle(getResources().getString(R.string.str_reset_dialog_title))
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton(getResources().getString(R.string.str_ok),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											PackageManager pm = getPackageManager();
											ComponentName name = new ComponentName("com.mstar.android.setupwizard",
													"com.mstar.android.setupwizard.DefaultActivity");
											pm.setComponentEnabledSetting(name,
													PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
													PackageManager.DONT_KILL_APP);

											Settings.Global.putInt(getContentResolver(),
													Settings.Global.DEVICE_PROVISIONED, 0);
											Settings.Secure.putInt(getContentResolver(), "user_setup_complete", 0);

											SharedPreferences settings = getSharedPreferences(
													Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
											SharedPreferences.Editor editor1 = settings.edit();
											editor1.putBoolean(Constant.PREFERENCES_IS_LOCATION_SELECTED, false);
											editor1.commit();
											SharedPreferences.Editor editor2 = settings.edit();
											editor2.putBoolean(Constant.PREFERENCES_IS_AUTOSCAN_LAUNCHED, false);
											editor2.commit();

											if (SystemProperties.getBoolean("mstar.reset.clear.tv_data", true)) {
												FactoryRestory();
											}
											FactoryDeskImpl.getInstance(DesignMenuActivity.this).restoreToDefault();
											String defaultLocale = SystemProperties.get("ro.product.locale", "en-US");
											SystemProperties.set("persist.sys.locale", defaultLocale);
											if(SystemProperties.getBoolean("persist.sys.showonidaview", false)){
												SystemProperties.set("persist.sys.showonidaview", "false");
											}
										}
									})
							.setNegativeButton(getResources().getString(R.string.cancel),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface arg0, int arg1) {
											arg0.dismiss();
										}
									})
							.show();
					break;
				default:
					break;
				}

				if (null != getCurrentFocus()) {
					View currentView = viewFlipper.getCurrentView();
					View focusForwad = currentView.focusSearch(View.FOCUS_FORWARD);
					focusForwad.requestFocus();
				}
			}
		};

		designViewHolder.linear_factory_picture.setOnClickListener(listener);
		designViewHolder.linear_factory_sound.setOnClickListener(listener);
		designViewHolder.linear_factory_panel_info.setOnClickListener(listener);
		designViewHolder.linear_factory_user.setOnClickListener(listener);
		designViewHolder.linear_factory_upgrade.setOnClickListener(listener);
		designViewHolder.linear_factory_other_option.setOnClickListener(listener);
		designViewHolder.linear_factory_reset.setOnClickListener(listener);
	}

	void restoreToDefault() {
		File dir = new File("/tvdatabase/Database");
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
		// close DB before recovery db
		FactoryDB.getInstance(this).closeDB();
		File fileFactory = new File(dir, "factory.db");
		loadDbFile(R.raw.factory, fileFactory);
		File fileUserSetting = new File(dir, "user_setting.db");
		loadDbFile(R.raw.user_setting, fileUserSetting);
		// reopen the db after recovery db
		FactoryDB.getInstance(this).openDB();
		factoryDesk.loadEssentialDataFromDB();
	}

	private void loadDbFile(int rawId, File file) {
		InputStream dbInputStream = this.getResources().openRawResource(rawId);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = dbInputStream.read(bytes)) > 0) {
				fos.write(bytes, 0, length);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				dbInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void FactoryRestory() {
		tvFactoryManager = TvFactoryManager.getInstance();
		mTvCommonManager = TvCommonManager.getInstance();
		if (null != tvFactoryManager && null != mTvCommonManager) {
			if (tvFactoryManager.restoreToDefault() == true) {
				int currInputSource = mTvCommonManager.getCurrentTvInputSource();
				if (currInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
					// tvS3DManager.setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
					if ((mTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ISDB)
							&& (mTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ATSC)) {
						// Because restore to factory default value,reset
						// routeIndex to 0
						TvDvbChannelManager.getInstance().setDtvAntennaType(0);
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				/***** Ended by gerard.jiang 2013/04/28 *****/
			} else {
				Log.e(TAG, "restoreToDefault failed!");
			}
		}
	}// add end

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = false;

		if (keyCode == KeyEvent.KEYCODE_PROG_RED) {
			if (viewFlipper.getVisibility() == View.VISIBLE)
				viewFlipper.setVisibility(View.INVISIBLE);
			else
				viewFlipper.setVisibility(View.VISIBLE);
			return true;
		} else if (viewFlipper.getVisibility() != View.VISIBLE) {
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_PROG_GREEN) {
			float density = (float) (SystemProperties.getInt("ro.sf.lcd_density", 0) / 160.0);
			// 50dp 100dp
			if (scrollx == 0)
				scrollx = -100;// -Math.round(50*density);
			else if (scrollx == -100)
				scrollx = -200;// -Math.round(100*density);
			else
				scrollx = 0;
			viewFlipper.scrollTo(Math.round(scrollx * density), 0);
			return true;
		}

		currentPage = viewFlipper.getDisplayedChild();
		int page = currentPage;
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
			if (currentPage == MAIN_PAGE) {
				finish();
			} else if (currentPage == URSA_INFO_PAGE || currentPage == IP_ENABLE_MAPPING_PAGE
					|| currentPage == CI_SETTING_PAGE) {
				gotoPage(6);
				returnRoot(page);
			} else if (currentPage == SOUND_ADJUST_PAGE || currentPage == SOUND_NONLINEAR_PAGE
					|| currentPage == SOUND_MODE_PAGE || currentPage == PEQ_ADJUST_PAGE) {
				gotoPage(2);
				returnRoot(page);
			} else if (currentPage == PICTURE_PAGE || currentPage == SOUND_PAGE || currentPage == PANEL_INFO_PAGE
					|| currentPage == USER_PAGE || currentPage == UPGRADE_PAGE || currentPage == OTHER_OPTION_PAGE) {
				returnRoot(page);
			} else {
				gotoPage(1);
				returnRoot(page);
			}
			return true;
		}

		switch (currentPage) {
		case MAIN_PAGE:
			View view = this.getCurrentFocus();
			if (null != view) {
				int currentid = view.getId();
			}
			break;
		case PICTURE_PAGE:
			bRet = pictureViewHolder.onKeyDown(keyCode, event);
			break;
		case SOUND_PAGE:
			bRet = soundViewHolder.onKeyDown(keyCode, event);
			break;
		case PANEL_INFO_PAGE:
			bRet = panelViewHolder.onKeyDown(keyCode, event);
			break;
		case USER_PAGE:
			bRet = userViewHolder.onKeyDown(keyCode, event);
			break;
		case UPGRADE_PAGE:
			bRet = upgradeViewHolder.onKeyDown(keyCode, event);
			break;
		case OTHER_OPTION_PAGE:
			bRet = otheroptionViewHolder.onKeyDown(keyCode, event);
			break;
		case PICTURE_MODE_PAGE:
			bRet = pictureModeAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case NON_LINEAR_PAGE:
			bRet = nonLinearAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case ADC_ADJUST_PAGE:
			bRet = adcAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case WHITE_BALANCE_PAGE:
			bRet = wbAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case OVERSCAN_PAGE:
			bRet = OverScanAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case SSC_ADJUST_PAGE:
			bRet = sscAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case PEQ_ADJUST_PAGE:
			bRet = peqAdjustViewHolder.onKeyDown(keyCode, event);
			break;
			
		case PQ_ADJUST_PAGE:
			bRet = pqAdjustViewHolder.onKeyDown(keyCode, event);
			break;
			
		case SOUND_NONLINEAR_PAGE:
			bRet = soundNonLinearAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case SOUND_MODE_PAGE:
			bRet = soundModeViewHolder.onKeyDown(keyCode, event);
			break;
		case SOUND_ADJUST_PAGE:
			bRet = soundAdjustViewHolder.onKeyDown(keyCode, event);
			break;
		case URSA_INFO_PAGE:
			bRet = ursaInfoViewHolder.onKeyDown(keyCode, event);
			break;
		case IP_ENABLE_MAPPING_PAGE:
			bRet = ipEnableMappingViewHolder.onKeyDown(keyCode, event);
			break;
		case CI_SETTING_PAGE:
			bRet = ciSettingViewHolder.onKeyDown(keyCode, event);
			break;
		default:
			break;
		}
		if (bRet == false) {
			bRet = super.onKeyDown(keyCode, event);
		}
		return bRet;
	}

	public void returnRoot(int pageIdx) {
		switch (pageIdx) {
		case URSA_INFO_PAGE:
		case IP_ENABLE_MAPPING_PAGE:
		case CI_SETTING_PAGE:
			focusedLayout = (LinearLayout) linearlayout_other_optioon.getChildAt(pageToBtnPosOther(pageIdx));
			break;
		case SOUND_NONLINEAR_PAGE:
		case SOUND_ADJUST_PAGE:
		case SOUND_MODE_PAGE:
		case PEQ_ADJUST_PAGE:
			focusedLayout = (LinearLayout) linearlayout_sound_menu.getChildAt(pageToBtnPosSound(pageIdx));
			break;
		case PICTURE_PAGE:
		case SOUND_PAGE:
		case PANEL_INFO_PAGE:
		case USER_PAGE:
		case UPGRADE_PAGE:
		case OTHER_OPTION_PAGE:
			currentPage = MAIN_PAGE;
			viewFlipper.setDisplayedChild(currentPage);
			focusedLayout = (LinearLayout) linearlayout_factory_menu.getChildAt(pageToBtnPosMain(pageIdx));
			break;
		default:
			focusedLayout = (LinearLayout) linearlayout_picture_menu.getChildAt(pageToBtnPosPicture(pageIdx));
			break;
		}
		focusedLayout.setFocusable(true);
		focusedLayout.requestFocus();
		focusedLayout.setFocusableInTouchMode(true);
	}

	public void gotoPage(int pageIdx) {
		currentPage = pageIdx;
		viewFlipper.setDisplayedChild(pageIdx);
		initPage(pageIdx);
	}

	public static boolean CheckUsbIsExist() {
		boolean ret = false;
		if (usbman.getDeviceList().isEmpty() == false) {
			ret = true;
		}
		return ret;
	}

	private int pageToBtnPosMain(int pageIdx) {
		switch (pageIdx) {
		case MAIN_PAGE:
			return BUTTON_POSITION_MAIN_PAGE;
		case PICTURE_PAGE:
			return BUTTON_POSITION_PICTURE_PAGE;
		case SOUND_PAGE:
			return BUTTON_POSITION_SOUND_PAGE;
		case PANEL_INFO_PAGE:
			return BUTTON_POSITION_PANEL_INFO_PAGE;
		case USER_PAGE:
			return BUTTON_POSITION_USER_PAGE;
		case UPGRADE_PAGE:
			return BUTTON_POSITION_UPGRADE_PAGE;
		case OTHER_OPTION_PAGE:
			return BUTTON_POSITION_OTHER_OPTION_PAGE;
		default:
			return BUTTON_POSITION_MAIN_PAGE;
		}

	}

	private int pageToBtnPosPicture(int pageIdx) {
		switch (pageIdx) {
		case PICTURE_MODE_PAGE:
			return BUTTON_POSITION_PICTURE_MODE_PAGE;
		case NON_LINEAR_PAGE:
			return BUTTON_POSITION_NON_LINEAR_PAGE;
		case ADC_ADJUST_PAGE:
			return BUTTON_POSITION_ADC_ADJUST_PAGE;
		case WHITE_BALANCE_PAGE:
			return BUTTON_POSITION_WHITE_BALANCE_PAGE;
		case OVERSCAN_PAGE:
			return BUTTON_POSITION_OVERSCAN_PAGE;
		case NON_STANDARD_ADJUST_PAGE:
			return BUTTON_POSITION_NON_STANDARD_OPTIONS_PAGE;
		case SSC_ADJUST_PAGE:
			return BUTTON_POSITION_SSC_ADJUST_PAGE;
		case TEST_PATTERN_PAGE:
			return BUTTON_POSITION_TEST_PATTERN_PAGE;
		case PQ_ADJUST_PAGE:
			return BUTTON_POSITION_PQ_ADJUST_PAGE;
		default:
			return BUTTON_POSITION_MAIN_PAGE;
		}
	}

	private int pageToBtnPosSound(int pageIdx) {
		switch (pageIdx) {
		case SOUND_ADJUST_PAGE:
			return BUTTON_POSITION_SOUND_ADJUST_PAGE;
		case SOUND_NONLINEAR_PAGE:
			return BUTTON_POSITION_SOUND_NONLINEAR_PAGE;
		case SOUND_MODE_PAGE:
			return BUTTON_POSITION_SOUND_MODE_PAGE;
		case PEQ_ADJUST_PAGE:
			return BUTTON_POSITION_PEQ_ADJUST_PAGE;
		default:
			return BUTTON_POSITION_MAIN_PAGE;
		}
	}

	private int pageToBtnPosOther(int pageIdx) {
		switch (pageIdx) {
		case URSA_INFO_PAGE:
			return BUTTON_POSITION_URSA_INFO_PAGE;
		case IP_ENABLE_MAPPING_PAGE:
			return BUTTON_POSITION_IP_ENABLE_MAPPING;
		case CI_SETTING_PAGE:
			return BUTTON_POSITION_CI_SETTING_PAGE;
		default:
			return BUTTON_POSITION_MAIN_PAGE;
		}
	}

	private void initPage(int pageIdx) {
		switch (pageIdx) {
		case MAIN_PAGE:
			break;
		case PICTURE_PAGE:
			pictureViewHolder = new PictureViewHolder(DesignMenuActivity.this, factoryDesk);
			pictureViewHolder.findView();
			pictureViewHolder.onCreate();
			break;
		case SOUND_PAGE:
			soundViewHolder = new SoundViewHolder(DesignMenuActivity.this, factoryDesk);
			soundViewHolder.findView();
			break;
		case PANEL_INFO_PAGE:
			panelViewHolder = new PanelInfoViewHolder(DesignMenuActivity.this, factoryDesk);
			panelViewHolder.findView();
			break;
		case USER_PAGE:
			userViewHolder = new UserViewHolder(DesignMenuActivity.this, factoryDesk);
			userViewHolder.findView();
			userViewHolder.onCreate();
			break;
		case UPGRADE_PAGE:
			upgradeViewHolder = new UpgradeViewHolder(DesignMenuActivity.this, factoryDesk);
			upgradeViewHolder.findView();
			upgradeViewHolder.onCreate();
			break;
		case OTHER_OPTION_PAGE:
			otheroptionViewHolder = new OtherOptionViewHolder(DesignMenuActivity.this, factoryDesk);
			otheroptionViewHolder.findView();
			otheroptionViewHolder.onCreate();
			break;
		case PICTURE_MODE_PAGE:
			pictureModeAdjustViewHolder = new PictureModeAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			pictureModeAdjustViewHolder.findView();
			pictureModeAdjustViewHolder.onCreate();
			break;
		case NON_LINEAR_PAGE:
			nonLinearAdjustViewHolder = new NonLinearAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			nonLinearAdjustViewHolder.findView();
			nonLinearAdjustViewHolder.onCreate();
			break;
		case ADC_ADJUST_PAGE:
			adcAdjustViewHolder = new ADCAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			adcAdjustViewHolder.findView();
			adcAdjustViewHolder.onCreate();
			break;
		case WHITE_BALANCE_PAGE:
			wbAdjustViewHolder = new WBAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			wbAdjustViewHolder.findView();
			wbAdjustViewHolder.onCreate();
			break;
		case OVERSCAN_PAGE:
			OverScanAdjustViewHolder = new OverScanAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			OverScanAdjustViewHolder.findView();
			OverScanAdjustViewHolder.onCreate();
			break;
		case NON_STANDARD_ADJUST_PAGE:
			nonStandardAdjustViewHolder = new NonStandardAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			break;
		case SSC_ADJUST_PAGE:
			sscAdjustViewHolder = new SSCAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			sscAdjustViewHolder.findView();
			sscAdjustViewHolder.onCreate();
			break;
		case PEQ_ADJUST_PAGE:
			peqAdjustViewHolder = new PEQAdjustViewHolder(DesignMenuActivity.this);
			peqAdjustViewHolder.findView();
			peqAdjustViewHolder.onCreate();
			break;
		case TEST_PATTERN_PAGE:
			testPatternViewHolder = new TestPatternViewHolder(DesignMenuActivity.this);
			testPatternViewHolder.findView();
			testPatternViewHolder.onCreate();
			break;
		case PQ_ADJUST_PAGE:
			pqAdjustViewHolder = new PQAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			pqAdjustViewHolder.findView();
			pqAdjustViewHolder.onCreate();
			break;
		case SOUND_NONLINEAR_PAGE:
			soundNonLinearAdjustViewHolder = new SoundNonLinearAdjustViewHolder(DesignMenuActivity.this, factoryDesk);
			soundNonLinearAdjustViewHolder.findView();
			soundNonLinearAdjustViewHolder.onCreate();
			break;
		case SOUND_MODE_PAGE:
			soundModeViewHolder = new SoundModeViewHolder(DesignMenuActivity.this, factoryDesk);
			soundModeViewHolder.findView();
			soundModeViewHolder.onCreate();
			break;
		case SOUND_ADJUST_PAGE:
			soundAdjustViewHolder = new FactorySoundViewHolder(DesignMenuActivity.this, factoryDesk);
			soundAdjustViewHolder.findView();
			soundAdjustViewHolder.onCreate();
			break;
		case URSA_INFO_PAGE:
			ursaInfoViewHolder = new UrsaInfoViewHolder(DesignMenuActivity.this);
			ursaInfoViewHolder.findView();
			ursaInfoViewHolder.onCreate();
			break;
		case IP_ENABLE_MAPPING_PAGE:
			ipEnableMappingViewHolder = new IPEnableMappingViewHolder(DesignMenuActivity.this, factoryDesk);
			ipEnableMappingViewHolder.onCreate();
			break;
		case CI_SETTING_PAGE:
			ciSettingViewHolder = new CISettingViewHolder(DesignMenuActivity.this, factoryDesk);
			ciSettingViewHolder.findView();
			ciSettingViewHolder.onCreate();
			break;
		default:
			break;
		}
	}
}
