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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.RemoteException;
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
import android.os.SystemProperties;
import android.content.ContentValues;

import java.util.ArrayList;

import android.database.SQLException;
import android.media.AudioManager;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvHbbTVManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.common.vo.EnumCecDeviceLa;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.MyButton;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.MenuConstants;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.component.PasswordCheckDialog;
import com.mstar.tv.tvplayer.ui.pvr.PVROptionActivity;
import com.mstar.tv.tvplayer.ui.settings.PowerSettingActivity;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.util.Constant;
import com.mstar.util.Utility;
import android.os.SystemProperties;

import com.mstar.tv.tvplayer.ui.TVRootApp;

import android.media.AudioManager;
public class SettingViewHolder {
    // 16*16 switchmode...
    private static final String TAG = "SettingViewHolder";

    private static final int SWITCH_OFF = 0;

    private static final int SWITCH_ON = 1;

    private static final int HBBTV_OFF = 0;

    private static final int HBBTV_ON = 1;
    protected TextView text_set_language_val;

    protected TextView text_set_audio_language1_val;

    protected TextView text_set_audio_language2_val;

    protected TextView text_set_subtitle_language1_val;

    protected TextView text_set_subtitle_language2_val;

    protected TextView text_set_audio_language1_name;

    protected TextView text_set_audio_language2_name;

    protected TextView text_set_subtitle_language1_name;

    protected TextView text_set_subtitle_language2_name;

    protected TextView text_set_menutime_val;

    protected TextView text_set_switchmode_val;

    protected TextView text_set_autosourceident_val;

    protected TextView text_set_sourcepreview_val;

    protected TextView text_set_autosourceswit_val;

    protected TextView text_set_colorrange_val;

    protected TextView text_set_mhlswitch_val;

    protected TextView text_set_moviemode_val;

    protected TextView text_set_bluescreenswitch_val;

    protected TextView text_set_hdmiarc_val;

    protected TextView text_set_hdmicec_val;

    protected TextView text_set_standyon_val;

    // add by wym 20151229
    protected TextView text_set_boot2launcher_val;
    protected TextView text_set_bootsource_val;
    // end
    protected TextView pvr_file_system_title;

    protected LinearLayout linear_set_pvrfs;

    protected LinearLayout linear_set_hdmicec;

    protected LinearLayout linear_set_source;
        
    protected LinearLayout linear_set_cec_list;

    protected LinearLayout linear_set_standyon;

    protected LinearLayout linear_set_powersetting;

    protected LinearLayout linear_hbbtv_onoff;

    protected LinearLayout linear_location_code;

    protected LinearLayout linear_set_restoretodefault;

    protected LinearLayout linear_set_language;

    protected LinearLayout linear_set_audio_language1;

    protected LinearLayout linear_set_audio_language2;

    protected LinearLayout linear_set_subtitle_language1;

    protected LinearLayout linear_set_subtitle_language2;
    
    protected LinearLayout linearlayout_set_subtitle_switch;

    protected LinearLayout linear_set_menutime;

    protected LinearLayout linear_set_switchmode;

    protected LinearLayout linear_set_autosourceident;

    protected LinearLayout linear_set_sourcepreview;

    protected LinearLayout linear_set_autosourceswit;

    protected LinearLayout linear_set_colorrange;

    protected LinearLayout linear_set_mhlswitch;

    protected LinearLayout linear_set_moviemode;

    protected LinearLayout linear_set_bluescreenswitch;

    protected LinearLayout linear_set_hdmiarc;

    protected LinearLayout linear_set_hdmiedidversion;

    protected LinearLayout linear_setting;
	
	protected LinearLayout linearlayout_set_scrolling_display;
	
	protected TextView textview_set_scrolling_display_val;

    // add by wym 20151229
    protected LinearLayout linear_set_boot2launcher;

    protected LinearLayout linear_set_bootsource;
    // end

    private Handler mHandler = new Handler();

    private TvS3DManager tvS3DManager = null;

    private TvCecManager mTvCecManager = null;

    private TvPictureManager tvPictureManager = null;

    private TvFactoryManager tvFactoryManager = null;

    private CecSetting cecSetting;

    protected TvChannelManager mTvChannelManager;

    private TvCommonManager mTvCommonManager = null;

    private int mTvSystem = 0;

    private TvPipPopManager tvPipPopManager = null;

    private TvMhlManager tvMhlManager = null;

    private Activity settingActivity;

    private Intent intent = new Intent();

    private PasswordCheckDialog mPasswordLock = null;

    private PasswordCheckDialog mPasswordCheck = null;

    private ProgressDialog mProgressDialog = null;

    private Thread mThreadEdidChange = null;

    private int languageindex = 0;

    private int mAudiolanguage1index = 0;

    private int mAudiolanguage2index = 0;

    private int mSubtitlelanguage1index = 0;

    private int mSubtitlelanguage2index = 0;

    private int menutimeindex = 0;

    private int switchmodeindex = 0;

    private int sourceidentindex = SWITCH_OFF;

    private int sourcepreviewindex = SWITCH_OFF;

    private int sourceswitindex = SWITCH_OFF;

    private int colorrangeindex = 0;

    private int mhlswitchindex = SWITCH_OFF;

    private int moviemodeindex = SWITCH_OFF;

    private int bluescreenindex = SWITCH_OFF;

    private int hdmiarcindex = SWITCH_OFF;

    private int hdmicecindex = SWITCH_OFF;

    private int standyonindex = SWITCH_ON;//SWITCH_OFF;

    private int bootindex = SWITCH_OFF;
    private int bootsourceindex = 0;

    private int focusedid = 0x00000000;

    private int DeviceListCount = 0;

    private CecSetting hdmicecstatus = null;

    private String[] mAudioLanguageType;

    private String[] mSubtitleLanguageType;

    private String[] menutimetype;

    private String[] switchmodetype;

    private String[] sourceidenttype;

    private String[] sourcepreviewtype;

    private String[] sourceswittype;

    private String[] mhlswitchtype;

    private String[] moviemodetype;

    private String[] bluescreentype;

    private String[] hdmidarctype;

    private String[] hdmicectype;
	
	private String[] scrollingtype;

    private String[] standyontype;

    private String[] colorrangetype;

    private Handler handler;

    private String[] mLanguageNameArray;
    private String[] mCecListName;

    private EnumCecDeviceLa[] mCECDeviceList;

    private ComboButton mComboSubtitleSwitch = null;

    private ComboButton mComboHdmiEdidVersion = null;

    private ComboButton mComboHbbTVOnOff = null;

    private MyButton mButtonInputSourceLock;

    private final int[] mAudioLanguageEnumValueArray = new int[] {
            TvLanguage.CZECH,
            TvLanguage.DANISH,
            TvLanguage.GERMAN,
            TvLanguage.ENGLISH,
            TvLanguage.SPANISH,
            TvLanguage.GREEK,
            TvLanguage.FRENCH,
            TvLanguage.CROATIAN,
            TvLanguage.ITALIAN,
            TvLanguage.HUNGARIAN,
            TvLanguage.DUTCH,
            TvLanguage.NORWEGIAN,
            TvLanguage.POLISH,
            TvLanguage.PORTUGUESE,
            TvLanguage.RUSSIAN,
            TvLanguage.ROMANIAN,
            TvLanguage.SLOVENIAN,
            TvLanguage.SERBIAN,
            TvLanguage.FINNISH,
            TvLanguage.SWEDISH,
            TvLanguage.BULGARIAN,
            TvLanguage.SLOVAK,
            TvLanguage.CHINESE,
            TvLanguage.GAELIC,
            TvLanguage.WELSH,
            TvLanguage.IRISH,
            TvLanguage.ARABIC,
            TvLanguage.LATVIAN,
            TvLanguage.HEBREW,
            TvLanguage.TURKISH,
            TvLanguage.ESTONIAN,
            TvLanguage.GALLEGAN,
            TvLanguage.BASQUE,
            TvLanguage.CATALAN,
            TvLanguage.ICELANDIC,
            TvLanguage.LETZEBURGESCH,
            TvLanguage.LITHUANIAN,
            TvLanguage.UKRAINIAN,
            TvLanguage.BYELORUSSIAN,
            TvLanguage.MAORI,
            TvLanguage.MANDARIN,
            TvLanguage.CANTONESE,
            TvLanguage.KOREAN,
            TvLanguage.JAPANESE,
            TvLanguage.HINDI,
            TvLanguage.THAI,
            TvLanguage.VIETNAMESE,
            TvLanguage.MALAY,
            TvLanguage.QAB,
            TvLanguage.QAC,
    };

    private final int[] mSubtitleLanguageEnumValueArray = new int[] {
            TvLanguage.CZECH,
            TvLanguage.DANISH,
            TvLanguage.GERMAN,
            TvLanguage.ENGLISH,
            TvLanguage.SPANISH,
            TvLanguage.GREEK,
            TvLanguage.FRENCH,
            TvLanguage.CROATIAN,
            TvLanguage.ITALIAN,
            TvLanguage.HUNGARIAN,
            TvLanguage.DUTCH,
            TvLanguage.NORWEGIAN,
            TvLanguage.POLISH,
            TvLanguage.PORTUGUESE,
            TvLanguage.RUSSIAN,
            TvLanguage.ROMANIAN,
            TvLanguage.SLOVENIAN,
            TvLanguage.SERBIAN,
            TvLanguage.FINNISH,
            TvLanguage.SWEDISH,
            TvLanguage.BULGARIAN,
            TvLanguage.SLOVAK,
            TvLanguage.CHINESE,
            TvLanguage.GAELIC,
            TvLanguage.WELSH,
            TvLanguage.IRISH,
            TvLanguage.ARABIC,
            TvLanguage.LATVIAN,
            TvLanguage.HEBREW,
            TvLanguage.TURKISH,
            TvLanguage.ESTONIAN,
            TvLanguage.GALLEGAN,
            TvLanguage.BASQUE,
            TvLanguage.CATALAN,
            TvLanguage.ICELANDIC,
            TvLanguage.LETZEBURGESCH,
            TvLanguage.LITHUANIAN,
            TvLanguage.UKRAINIAN,
            TvLanguage.BYELORUSSIAN,
            TvLanguage.MAORI,
            TvLanguage.MANDARIN,
            TvLanguage.CANTONESE,
            TvLanguage.KOREAN,
            TvLanguage.JAPANESE,
            TvLanguage.HINDI,
            TvLanguage.THAI,
            TvLanguage.VIETNAMESE,
            TvLanguage.MALAY,
            TvLanguage.MULTIPLE,
    };

    public SettingViewHolder(Activity activity, Handler handler) {
        this.settingActivity = activity;
        mTvCommonManager = TvCommonManager.getInstance();
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        tvS3DManager = TvS3DManager.getInstance();
        mTvCecManager = TvCecManager.getInstance();
        tvPictureManager = TvPictureManager.getInstance();
        tvFactoryManager = TvFactoryManager.getInstance();
        cecSetting = mTvCecManager.getCecConfiguration();
        tvPipPopManager = TvPipPopManager.getInstance();
        tvMhlManager = TvMhlManager.getInstance();
        mTvChannelManager = TvChannelManager.getInstance();
        this.handler = handler;
        mCECDeviceList = new EnumCecDeviceLa[EnumCecDeviceLa.values().length];
    }

    public void findViews() {
        int bootsource = 0;
        mLanguageNameArray = new String[] {
                settingActivity.getResources().getString(R.string.str_set_language_czech),
                settingActivity.getResources().getString(R.string.str_set_language_danish),
                settingActivity.getResources().getString(R.string.str_set_language_german),
                settingActivity.getResources().getString(R.string.str_set_language_english),
                settingActivity.getResources().getString(R.string.str_set_language_spanish),
                settingActivity.getResources().getString(R.string.str_set_language_greek),
                settingActivity.getResources().getString(R.string.str_set_language_french),
                settingActivity.getResources().getString(R.string.str_set_language_croatian),
                settingActivity.getResources().getString(R.string.str_set_language_italian),
                settingActivity.getResources().getString(R.string.str_set_language_hungarian),
                settingActivity.getResources().getString(R.string.str_set_language_dutch),
                settingActivity.getResources().getString(R.string.str_set_language_norwegian),
                settingActivity.getResources().getString(R.string.str_set_language_polish),
                settingActivity.getResources().getString(R.string.str_set_language_portuguese),
                settingActivity.getResources().getString(R.string.str_set_language_russian),
                settingActivity.getResources().getString(R.string.str_set_language_romanian),
                settingActivity.getResources().getString(R.string.str_set_language_slovenian),
                settingActivity.getResources().getString(R.string.str_set_language_serbian),
                settingActivity.getResources().getString(R.string.str_set_language_finnish),
                settingActivity.getResources().getString(R.string.str_set_language_swedish),
                settingActivity.getResources().getString(R.string.str_set_language_bulgarian),
                settingActivity.getResources().getString(R.string.str_set_language_slovak),
                settingActivity.getResources().getString(R.string.str_set_language_chinese),
                settingActivity.getResources().getString(R.string.str_set_language_gaelic),
                settingActivity.getResources().getString(R.string.str_set_language_welsh),
                settingActivity.getResources().getString(R.string.str_set_language_irish),
                settingActivity.getResources().getString(R.string.str_set_language_arabic),
                settingActivity.getResources().getString(R.string.str_set_language_latvian),
                settingActivity.getResources().getString(R.string.str_set_language_hebrew),
                settingActivity.getResources().getString(R.string.str_set_language_turkish),
                settingActivity.getResources().getString(R.string.str_set_language_estonian),
                settingActivity.getResources().getString(R.string.str_set_language_gallegan),
                settingActivity.getResources().getString(R.string.str_set_language_basque),
                settingActivity.getResources().getString(R.string.str_set_language_catalan),
                settingActivity.getResources().getString(R.string.str_set_language_icelandic),
                settingActivity.getResources().getString(R.string.str_set_language_letzeburgesch),
                settingActivity.getResources().getString(R.string.str_set_language_lithuanian),
                settingActivity.getResources().getString(R.string.str_set_language_ukrainian),
                settingActivity.getResources().getString(R.string.str_set_language_byelorussian),
                settingActivity.getResources().getString(R.string.str_set_language_maori),
                settingActivity.getResources().getString(R.string.str_set_language_mandarin),
                settingActivity.getResources().getString(R.string.str_set_language_cantonese),
                settingActivity.getResources().getString(R.string.str_set_language_korean),
                settingActivity.getResources().getString(R.string.str_set_language_japanese),
                settingActivity.getResources().getString(R.string.str_set_language_hindi),
        };
        
        pvr_file_system_title = (TextView)settingActivity.findViewById(R.id.pvr_file_system_title);
        text_set_language_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_language_val);
        text_set_audio_language1_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_audio_language1_val);
        text_set_audio_language2_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_audio_language2_val);
        text_set_subtitle_language1_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_subtitle_language1_val);
        text_set_subtitle_language2_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_subtitle_language2_val);
        text_set_menutime_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_menutime_val);
        text_set_switchmode_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_switchmode_val);
        text_set_autosourceident_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_autosourceident_val);
        text_set_sourcepreview_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_sourcepreview_val);
        text_set_autosourceswit_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_autosourceswit_val);
        text_set_colorrange_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_colorrange_val);
        text_set_moviemode_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_moviemode_val);
        text_set_mhlswitch_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_mhlswitch_val);
        text_set_bluescreenswitch_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_bluescreenswitch_val);
        text_set_hdmiarc_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_hdmiarc_val);
        text_set_hdmicec_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_hdmicec_val);
        text_set_standyon_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_standyon_val);
        // add by wym 20151229
        text_set_boot2launcher_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_bootoption_val);
        text_set_bootsource_val = (TextView) settingActivity
                .findViewById(R.id.textview_set_bootsource_val);
        // end
        linear_set_pvrfs = (LinearLayout) settingActivity.findViewById(R.id.linearlayout_set_pvrfs);
        TVRootApp app = (TVRootApp) settingActivity.getApplication();
        if (app.isPVREnable() == false) {
            linear_set_pvrfs.setVisibility(View.GONE);
        }
        //add by wxy
        if(mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV)
        {
        	linear_set_pvrfs.setFocusable(false);
        	linear_set_pvrfs.setEnabled(false);
        	pvr_file_system_title.setTextColor(Color.GRAY);
        	linear_set_pvrfs.setVisibility(View.GONE);
        }
        else
        {
        	linear_set_pvrfs.setFocusable(true);
        	linear_set_pvrfs.setEnabled(true);
        	pvr_file_system_title.setTextColor(Color.WHITE);
        	linear_set_pvrfs.setVisibility(View.VISIBLE);

        }
		
		linearlayout_set_scrolling_display = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_scrolling_display);
		textview_set_scrolling_display_val=(TextView) settingActivity
                .findViewById(R.id.textview_set_scrolling_display_val);
        //add end
        linear_set_source = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_source);
        if (app.isCusOnida()) {
        	linear_set_source.setVisibility(View.VISIBLE);
        	linear_set_source.setFocusable(true);
        	linear_set_source.setEnabled(true);
		} else {
			linear_set_source.setVisibility(View.GONE);
        	linear_set_source.setFocusable(false);
        	linear_set_source.setEnabled(false);
		}
        linear_set_hdmicec = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_hdmicec);
        if (app.isCECEnable() == false) {
            linear_set_hdmicec.setVisibility(View.GONE);
        }
        linear_set_standyon = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_standyon);
        if (app.isCECEnable() == false) {
            linear_set_standyon.setVisibility(View.GONE);
        }
        linear_set_cec_list = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_cec_list);
        if (app.isCECEnable() == false) {
            linear_set_cec_list.setVisibility(View.GONE);
        }
        linear_set_powersetting = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_powersetting);
        //add by wxy
        linear_set_powersetting.setVisibility(View.GONE);
        linear_set_powersetting.setFocusable(false);
        linear_set_powersetting.setEnabled(false);
        //add end
        linear_set_language = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_language);
        linear_set_audio_language1 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_audio_language1);
        linear_set_audio_language2 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_audio_language2);
        linear_set_subtitle_language1 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_subtitle_language1);
        linear_set_subtitle_language2 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_subtitle_language2);
        linearlayout_set_subtitle_switch = (LinearLayout) settingActivity
        .findViewById(R.id.linearlayout_set_subtitle_switch);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            linear_set_subtitle_language1.setVisibility(View.GONE);
            linear_set_subtitle_language2.setVisibility(View.GONE);
        }
        //modify by wxy
        	linearlayout_set_subtitle_switch.setVisibility(View.GONE);
            linearlayout_set_subtitle_switch.setVisibility(View.GONE);
        //modify end
        linear_set_menutime = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_menutime);
        linear_set_switchmode = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_switchmode);
        linear_set_autosourceident = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_autosourceident);
        linear_set_sourcepreview = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_sourcepreview);
        linear_set_autosourceswit = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_autosourceswit);
        linear_set_colorrange = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_colorrange);
        if (app.isRGBRangeEnable() == false) {
            linear_set_colorrange.setVisibility(View.GONE);
        }
        linear_set_moviemode = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_moviemode);
        //add by wxy
        linear_set_moviemode.setVisibility(View.GONE);
        linear_set_moviemode.setFocusable(false);
        linear_set_moviemode.setEnabled(false);
        //add end
        linear_set_bluescreenswitch = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_bluescreenswitch);
        linear_set_mhlswitch = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_mhlswitch);
        linear_set_hdmiarc = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_hdmiarc);
        if ((app.isCECEnable() == false)||(app.isArcEnable() == false)) {
            linear_set_hdmiarc.setVisibility(View.GONE);
        }
        linear_hbbtv_onoff = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_hbbtv_switch);
        linear_location_code = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_location_code);
        linear_set_restoretodefault = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_restoretodefault);
        if (app.isCusOnida()) {
        	linear_set_standyon.setVisibility(View.GONE);
        	linear_set_restoretodefault.setVisibility(View.GONE);
		}
        linear_setting = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_setting);
		linear_set_hdmiedidversion = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_hdmi_edid_version);
        linear_setting = (LinearLayout) settingActivity.findViewById(R.id.linearlayout_setting);
        // add by wym 20151229
        linear_set_boot2launcher = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_bootoption);
        linear_set_bootsource = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_bootsource);
        // end
        mAudioLanguageType = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_audio_language);
        mSubtitleLanguageType = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_subtitle_language);
        menutimetype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_menutimetype);
        switchmodetype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_switchmodetype);
        sourceidenttype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_sourceidenttype);
        sourcepreviewtype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_sourcepreviewtype);
        sourceswittype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_sourceswittype);
        mhlswitchtype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_mhlautoswitch_onoff);
        moviemodetype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_moviemodetype);
        bluescreentype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_bluescreenswitchtype);
        hdmidarctype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_hdmiarctype);
        hdmicectype = settingActivity.getResources()
                .getStringArray(R.array.str_arr_set_hdmicectype);
        standyontype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_standyontype);
        colorrangetype = settingActivity.getResources().getStringArray(
                R.array.str_arr_set_colorrangetype);
        mLiteralInputSources =  settingActivity.getResources()
                .getStringArray(R.array.str_arr_input_sources);
				
				
		 scrollingtype=settingActivity.getResources()         
                .getStringArray(R.array.str_arr_set_hdmicectype);
        if (SystemProperties.getBoolean("persist.sys.showonidaview", false)) {
        	textview_set_scrolling_display_val.setText(scrollingtype[1]);
		}else{
        textview_set_scrolling_display_val.setText(scrollingtype[0]);
		}

         if (SystemProperties.getBoolean("persist.sys.onidaview_onoff", true)&&app.isCusOnida() ==true) {
        	 if(!linearlayout_set_scrolling_display.isShown()){
        		 linearlayout_set_scrolling_display.setVisibility(View.VISIBLE);
        	 }
        }else{
            linearlayout_set_scrolling_display.setVisibility(View.GONE);
			RootActivity.showonidaview(false);
        }		
				
				
        //linear_set_menutime.requestFocus();
        //linear_set_menutime.requestFocus();//skye
//        linear_set_restoretodefault.setVisibility(View.GONE);
        linear_set_language.setVisibility(View.GONE);

        linear_set_bluescreenswitch.setVisibility(View.GONE);
        //move down by jerry 20160330
        setOnClickLisenter();
       // setOnFocusChangeListeners();
        setOnTouchListeners();
        
        Configuration config = settingActivity.getResources().getConfiguration();
        if (config.locale.toString().equals("en_US")) {
            languageindex = 0;
        } else {
            languageindex = 1;
        }
        mAudiolanguage1index = getAudioLanguage(true);
        mAudiolanguage2index = getAudioLanguage(false);
        mSubtitlelanguage1index = getSubtitleLanguage(true);
        mSubtitlelanguage2index = getSubtitleLanguage(false);
        mComboSubtitleSwitch = new ComboButton(settingActivity, settingActivity.getResources()
                .getStringArray(R.array.str_arr_subtitle_switch),
                R.id.linearlayout_set_subtitle_switch, 1, 2, true) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (SWITCH_ON == getIdx()) {
                    mTvCommonManager.setSubtitleEnable(true);
                } else {
                    mTvCommonManager.setSubtitleEnable(false);
                }
            }
        };

        if (mTvCommonManager.isSubtitleEnable()) {
            mComboSubtitleSwitch.setIdx(SWITCH_ON);
        } else {
            mComboSubtitleSwitch.setIdx(SWITCH_OFF);
        }
		
        if(SystemProperties.getBoolean("persist.sys.no_dtv",false))
		{
       	 mComboSubtitleSwitch.setVisibility(View.GONE);
		}

        mComboHbbTVOnOff = new ComboButton(settingActivity, settingActivity.getResources()
                .getStringArray(R.array.str_arr_hbbtv_switch), R.id.linearlayout_set_hbbtv_switch,
                1, 2, true) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                if (HBBTV_ON == getIdx()) {
                    TvHbbTVManager.getInstance().setHbbTVOnOff(true);
                } else {
                    TvHbbTVManager.getInstance().setHbbTVOnOff(false);
                }
            }
        };

        if (TvHbbTVManager.getInstance().getHbbTVOnOff()) {
            mComboHbbTVOnOff.setIdx(HBBTV_ON);
        } else {
            mComboHbbTVOnOff.setIdx(HBBTV_OFF);
        }
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_HBBTV)) {
            linear_hbbtv_onoff.setVisibility(View.VISIBLE);
        } else {
            linear_hbbtv_onoff.setVisibility(View.GONE);
        }

        int currInputSource = mTvCommonManager.getCurrentTvInputSource();
        if ((currInputSource >= TvCommonManager.INPUT_SOURCE_HDMI)
                && (currInputSource < TvCommonManager.INPUT_SOURCE_HDMI_MAX)) {
            createEdidSwitch(currInputSource);
        } else {
            linear_set_hdmiedidversion.setVisibility(View.GONE);
        }

        if (TvCountry.INDONESIA == TvChannelManager.getInstance().getSystemCountryId()) {
            if (Utility.isDVBT()) {
                /*
                 * The location code setting is for EWS and only enabled in
                 * DVBT/DVBT2.
                 */
                linear_location_code.setVisibility(View.VISIBLE);
            }
        }

        // Refine performance with query DB by content provider to reduce
        // startup time in setting page.
        
        
        try {
        	Cursor cursor = this.settingActivity
                    .getApplicationContext()
                    .getContentResolver()
                    .query(Uri.parse("content://mstar.tv.usersetting/systemsetting"), null, null, null,
                            null);
            if (cursor.moveToFirst()) {
                switchmodeindex = cursor.getInt(cursor.getColumnIndex("bATVChSwitchFreeze"));
                colorrangeindex = cursor.getInt(cursor.getColumnIndex("u8ColorRangeMode"));
                sourceidentindex = cursor.getInt(cursor.getColumnIndex("bSourceDetectEnable"));
                sourcepreviewindex = cursor.getInt(cursor.getColumnIndex("bSourcePreview"));
                sourceswitindex = cursor.getInt(cursor.getColumnIndex("bAutoSourceSwitch"));
                bootsource = cursor.getInt(cursor.getColumnIndex("enSelInputSourceType"));;
            }
            cursor.close();
		} catch (Exception e) {
			Log.d(TAG, "cursor error: " +e.toString());
			// TODO: handle exception
		}

        menutimeindex = mTvCommonManager.getOsdDuration();
        moviemodeindex = tvPictureManager.getFilm();
        bluescreenindex = mTvCommonManager.getBlueScreenMode() ? SWITCH_ON : SWITCH_OFF;
        hdmicecstatus = mTvCecManager.getCecConfiguration();
        hdmicecindex = hdmicecstatus.cecStatus;
        standyonindex = hdmicecstatus.autoStandby;
        hdmiarcindex = cecSetting.arcStatus;
        //bootsourceindex = queryDbValue();
        bootindex = SystemProperties.getInt("persist.tvboot.firstshowtv", 1);
        if (bootindex > 0) bootindex = 0;
        else  bootindex = 1;
        if (tvMhlManager.getAutoSwitch()) {
            mhlswitchindex = SWITCH_ON;
        } else {
            mhlswitchindex = SWITCH_OFF;
        }

        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
            enableChoose(linear_set_autosourceswit, sourceidentindex == SWITCH_OFF ? false : true);
        }
        enableChoose(linear_set_standyon, hdmicecindex == SWITCH_OFF ? false : true);
        enableChoose(linear_set_hdmiarc, hdmicecindex == SWITCH_OFF ? false : true);
        enableChooseTitle(linear_set_cec_list, hdmicecindex == SWITCH_OFF ? false : true);
        text_set_language_val.setText(mSubtitleLanguageType[languageindex]);
        text_set_audio_language1_val.setText(mAudioLanguageType[mAudiolanguage1index]);
        text_set_audio_language2_val.setText(mAudioLanguageType[mAudiolanguage2index]);
        text_set_subtitle_language1_val.setText(mSubtitleLanguageType[mSubtitlelanguage1index]);
        text_set_subtitle_language2_val.setText(mSubtitleLanguageType[mSubtitlelanguage2index]);
        text_set_menutime_val.setText(menutimetype[menutimeindex]);
        text_set_switchmode_val.setText(switchmodetype[switchmodeindex]);
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
            text_set_autosourceident_val.setText(sourceidenttype[sourceidentindex]);
            text_set_autosourceswit_val.setText(sourceswittype[sourceswitindex]);
        }
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_PREVIEW_MODE)) {
            text_set_sourcepreview_val.setText(sourcepreviewtype[sourcepreviewindex]);
        }
        text_set_colorrange_val.setText(colorrangetype[colorrangeindex]);
        text_set_mhlswitch_val.setText(mhlswitchtype[mhlswitchindex]);
        text_set_moviemode_val.setText(moviemodetype[moviemodeindex]);
        text_set_bluescreenswitch_val.setText(bluescreentype[bluescreenindex]);
        text_set_hdmicec_val.setText(hdmicectype[hdmicecindex]);
        text_set_standyon_val.setText(standyontype[standyonindex]);
        text_set_hdmiarc_val.setText(hdmidarctype[hdmiarcindex]);
        text_set_boot2launcher_val.setText(hdmidarctype[bootindex]);
        TextView text_color_range_name = (TextView) linear_set_colorrange.getChildAt(1);
        TextView text_color_range_val = (TextView) linear_set_colorrange.getChildAt(2);
        TextView text_color_switchmode_name = (TextView) linear_set_switchmode.getChildAt(1);
        TextView text_color_switchmode_val = (TextView) linear_set_switchmode.getChildAt(2);
        text_set_audio_language1_name = (TextView) linear_set_audio_language1.getChildAt(1);
        text_set_audio_language1_val = (TextView) linear_set_audio_language1.getChildAt(2);
        text_set_audio_language2_name = (TextView) linear_set_audio_language2.getChildAt(1);
        text_set_audio_language2_val = (TextView) linear_set_audio_language2.getChildAt(2);
        text_set_subtitle_language1_name = (TextView) linear_set_subtitle_language1.getChildAt(1);
        text_set_subtitle_language1_val = (TextView) linear_set_subtitle_language1.getChildAt(2);
        text_set_subtitle_language2_name = (TextView) linear_set_subtitle_language2.getChildAt(1);
        text_set_subtitle_language2_val = (TextView) linear_set_subtitle_language2.getChildAt(2);
        if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_HDMI
                || mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_HDMI2
                || mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_HDMI3
                || mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_HDMI4) {
            linear_set_colorrange.setFocusable(true);
            linear_set_colorrange.setEnabled(true);
            text_color_range_name.setTextColor(Color.WHITE);
            text_color_range_val.setTextColor(Color.WHITE);
        } else {
            linear_set_colorrange.setFocusable(false);
            linear_set_colorrange.setEnabled(false);
            text_color_range_name.setTextColor(Color.GRAY);
            text_color_range_val.setTextColor(Color.GRAY);
        }
        if ((mTvCommonManager.getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC)) {
            if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV) {
                linear_set_switchmode.setFocusable(true);
                linear_set_switchmode.setEnabled(true);
                text_color_switchmode_name.setTextColor(Color.WHITE);
                text_color_switchmode_val.setTextColor(Color.WHITE);
            } else {
                linear_set_switchmode.setFocusable(false);
                linear_set_switchmode.setEnabled(false);
                text_color_switchmode_name.setTextColor(Color.GRAY);
                text_color_switchmode_val.setTextColor(Color.GRAY);
                /* set black screen mode always */
                text_set_switchmode_val.setText(switchmodetype[0]);
            }
        } else {
            if ((mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV)
                    || (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV)) {
                linear_set_switchmode.setFocusable(true);
                linear_set_switchmode.setEnabled(true);
                text_color_switchmode_name.setTextColor(Color.WHITE);
                text_color_switchmode_val.setTextColor(Color.WHITE);
            } else {
                linear_set_switchmode.setFocusable(false);
                linear_set_switchmode.setEnabled(false);
                text_color_switchmode_name.setTextColor(Color.GRAY);
                text_color_switchmode_val.setTextColor(Color.GRAY);
                text_set_switchmode_val.setText(switchmodetype[0]); // set black
            }
        }

        // Only the DTV needs language Settings
        if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            linear_set_audio_language1.setFocusable(true);
            linear_set_audio_language1.setEnabled(true);
            linear_set_audio_language2.setFocusable(true);
            linear_set_audio_language2.setEnabled(true);
            linear_set_subtitle_language1.setFocusable(true);
            linear_set_subtitle_language1.setEnabled(true);
            linear_set_subtitle_language2.setFocusable(true);
            linear_set_subtitle_language2.setEnabled(true);
            mComboSubtitleSwitch.setEnable(true);
            text_set_audio_language1_name.setTextColor(Color.WHITE);
            text_set_audio_language1_val.setTextColor(Color.WHITE);
            text_set_audio_language2_name.setTextColor(Color.WHITE);
            text_set_audio_language2_val.setTextColor(Color.WHITE);
            text_set_subtitle_language1_name.setTextColor(Color.WHITE);
            text_set_subtitle_language1_val.setTextColor(Color.WHITE);
            text_set_subtitle_language2_name.setTextColor(Color.WHITE);
            text_set_subtitle_language2_val.setTextColor(Color.WHITE);
        } else {
            linear_set_audio_language1.setFocusable(false);
            linear_set_audio_language1.setEnabled(false);
            linear_set_audio_language2.setFocusable(false);
            linear_set_audio_language2.setEnabled(false);
            linear_set_subtitle_language1.setFocusable(false);
            linear_set_subtitle_language1.setEnabled(false);
            linear_set_subtitle_language2.setFocusable(false);
            linear_set_subtitle_language2.setEnabled(false);
            mComboSubtitleSwitch.setEnable(false);
            text_set_audio_language1_name.setTextColor(Color.GRAY);
            text_set_audio_language1_val.setTextColor(Color.GRAY);
            text_set_audio_language2_name.setTextColor(Color.GRAY);
            text_set_audio_language2_val.setTextColor(Color.GRAY);
            text_set_subtitle_language1_name.setTextColor(Color.GRAY);
            text_set_subtitle_language1_val.setTextColor(Color.GRAY);
            text_set_subtitle_language2_name.setTextColor(Color.GRAY);
            text_set_subtitle_language2_val.setTextColor(Color.GRAY);
        }

        // Initiate Password Check Dialog
        int viewResId;
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            viewResId = R.layout.password_check_dialog_6_digits;
        } else {
            viewResId = R.layout.password_check_dialog_4_digits;
        }

        mPasswordLock = new PasswordCheckDialog(settingActivity, viewResId) {
            @Override
            public String onCheckPassword() {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    return MenuConstants.getSharedPreferencesValue(settingActivity,
                            MenuConstants.VCHIPPASSWORD, MenuConstants.VCHIPPASSWORD_DEFAULTVALUE);
                } else {
                    return String.format("%04d", TvParentalControlManager.getInstance()
                            .getParentalPassword());
                }
            }

            @Override
            public void onPassWordCorrect() {
                mToast.cancel();
                mToast = Toast.makeText(settingActivity,
                        settingActivity.getResources().getString(R.string.str_check_password_pass),
                        Toast.LENGTH_SHORT);
                mToast.show();
                dismiss();

                /*mantis:11510,rearranging lock flow to match test team`s require.lxk 20150227*/
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(settingActivity);
                dialogBuilder.setMessage(settingActivity.getResources().getString(R.string.str_root_alert_dialog_message_restore_to_default));
                dialogBuilder.setPositiveButton(settingActivity.getResources().getString(R.string.str_root_alert_dialog_confirm),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainMenuActivity.getInstance().setSettingSelectStatus(0x10000000);
                            focusedid = R.id.linearlayout_set_restoretodefault;
                            restoreToDefault();
                        }
                    });
                dialogBuilder.setNegativeButton(settingActivity.getResources().getString(R.string.str_root_alert_dialog_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                dialogBuilder.create().show();
                //restoreToDefault();
                /*end*/
                
            }

            @Override
            public void onKeyDown(int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_MENU == keyCode) {
                    cancel();
                }
            }

            @Override
            public void onShow() {
                LittleDownTimer.pauseMenu();
                View view = settingActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate()
                            .alpha(0f)
                            .setDuration(
                                    settingActivity.getResources().getInteger(
                                            android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onCancel() {
                View view = settingActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate()
                            .alpha(1f)
                            .setDuration(
                                    settingActivity.getResources().getInteger(
                                            android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onDismiss() {
                LittleDownTimer.resetMenu();
                LittleDownTimer.resumeMenu();
            }
        };

        if (true == Utility.isSupportInputSourceLock()) {
            mButtonInputSourceLock = new MyButton(settingActivity,
                    R.id.linearlayout_set_inputsourceblock) {
                @Override
                public void doUpdate() {
                    mPasswordCheck.show();
                }
            };
            mButtonInputSourceLock.setVisibility(View.VISIBLE);
            mButtonInputSourceLock.setEnable(true);
        }

        mPasswordCheck = new PasswordCheckDialog(settingActivity, viewResId) {
            @Override
            public String onCheckPassword() {
                if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                    return MenuConstants.getSharedPreferencesValue(settingActivity,
                            MenuConstants.VCHIPPASSWORD, MenuConstants.VCHIPPASSWORD_DEFAULTVALUE);
                } else {
                    return String.format("%04d", TvParentalControlManager.getInstance()
                            .getParentalPassword());
                }
            }

            @Override
            public void onPassWordCorrect() {
                mToast.cancel();
                mToast = Toast.makeText(settingActivity,
                        settingActivity.getResources().getString(R.string.str_check_password_pass),
                        Toast.LENGTH_SHORT);
                mToast.show();
                dismiss();

                Intent intent = new Intent(TvIntent.ACTION_INPUT_SOURCE_BLOCK);
                if (intent.resolveActivity(settingActivity.getPackageManager()) != null) {
                    settingActivity.startActivity(intent);
                }
            }

            @Override
            public void onKeyDown(int keyCode, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_MENU == keyCode) {
                    cancel();
                }
            }

            @Override
            public void onShow() {
                LittleDownTimer.pauseMenu();
                View view = settingActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate()
                            .alpha(0f)
                            .setDuration(
                                    settingActivity.getResources().getInteger(
                                            android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onCancel() {
                View view = settingActivity.findViewById(android.R.id.content);
                if (null != view) {
                    view.animate()
                            .alpha(1f)
                            .setDuration(
                                    settingActivity.getResources().getInteger(
                                            android.R.integer.config_shortAnimTime));
                }
            }

            @Override
            public void onDismiss() {
                LittleDownTimer.resetMenu();
                LittleDownTimer.resumeMenu();
            }
        };

        getSrcListInfo();
        setSrcIndex(bootsource);// here, bootsourceindex is the source not index.....

        text_set_bootsource_val.setText(getCurSrcInfo().getInputSourceName());
        
        //Utility.setDefaultFocus(linear_setting);
    }

    protected void enableChoose(LinearLayout linearLayout, boolean bool) {
        linearLayout.setEnabled(bool);
        linearLayout.setFocusable(bool);

        int color = Color.WHITE;
        if (!bool) {
            color = Color.GRAY;
        }

        ((TextView) linearLayout.getChildAt(1)).setTextColor(color);
        ((TextView) linearLayout.getChildAt(2)).setTextColor(color);
    }

    protected void enableChooseTitle(LinearLayout linearLayout, boolean bool) {
        linearLayout.setEnabled(bool);
        linearLayout.setFocusable(bool);

        int color = Color.WHITE;
        if (!bool) {
            color = Color.GRAY;
        }

        ((TextView) linearLayout.getChildAt(0)).setTextColor(color);
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        int currentid = settingActivity.getCurrentFocus().getId();
        if (focusedid != currentid) {
            MainMenuActivity.getInstance().setSettingSelectStatus(0x00000000);
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_set_audio_language1:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002)*/ {
                            if (mAudiolanguage1index == (mAudioLanguageType.length - 1))
                                mAudiolanguage1index = 0;
                            else
                                mAudiolanguage1index++;
                            if (setAudioLanguage(true, mAudiolanguage1index) == true) {
                                text_set_audio_language1_val
                                        .setText(mAudioLanguageType[mAudiolanguage1index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_audio_language2:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003)*/ {
                            if (mAudiolanguage2index == (mAudioLanguageType.length - 1))
                                mAudiolanguage2index = 0;
                            else
                                mAudiolanguage2index++;
                            if (setAudioLanguage(false, mAudiolanguage2index) == true) {
                                text_set_audio_language2_val
                                        .setText(mAudioLanguageType[mAudiolanguage2index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language1:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002)*/ {
                            if (mSubtitlelanguage1index == (mSubtitleLanguageType.length - 1))
                                mSubtitlelanguage1index = 0;
                            else
                                mSubtitlelanguage1index++;
                            if (setSubtitleLanguage(true, mSubtitlelanguage1index) == true) {
                                text_set_subtitle_language1_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage1index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language2:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003)*/ {
                            if (mSubtitlelanguage2index == (mSubtitleLanguageType.length - 1))
                                mSubtitlelanguage2index = 0;
                            else
                                mSubtitlelanguage2index++;
                            if (setSubtitleLanguage(false, mSubtitlelanguage2index) == true) {
                                text_set_subtitle_language2_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage2index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_source:
                        focusedid = R.id.linearlayout_source;
                        Intent intentSource = new Intent(
                            "com.mstar.tvsetting.switchinputsource.intent.action.PictrueChangeActivity");
                        settingActivity.startActivity(intentSource);
                        break;

                    case R.id.linearlayout_set_menutime:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000010)*/ {
                           // boolean bEnable = true;
                            int second = 10;
                            if (menutimeindex == 5)
                                menutimeindex = 0;
                            else
                                menutimeindex++;
                            text_set_menutime_val.setText(menutimetype[menutimeindex]);
                            mTvCommonManager.setOsdDuration(menutimeindex);
                            second = mTvCommonManager.getOsdTimeoutInSecond();
                            if (menutimeindex == 5) {
                                LittleDownTimer.stopMenu();
                            } else {
                                LittleDownTimer.setMenuStatus(second);
                            }
                        }
                        focusedid = currentid;
                        break;
						
					 case R.id.linearlayout_set_scrolling_display:
                        focusedid = R.id.linearlayout_set_scrolling_display;
                        if (SystemProperties.getBoolean("persist.sys.showonidaview", false)) {
                        	RootActivity.showonidaview(false);
                        	textview_set_scrolling_display_val.setText(scrollingtype[0]);
                        	//RootActivity.isshowonidaview=false;
                        	SystemProperties.set("persist.sys.showonidaview","false");
						}else{
                        RootActivity.showonidaview(true);
                        textview_set_scrolling_display_val.setText(scrollingtype[1]);
                        //RootActivity.isshowonidaview=true;
                        SystemProperties.set("persist.sys.showonidaview","true");
						}
                        focusedid = currentid;
                        break;
						
                    case R.id.linearlayout_set_switchmode:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000100)*/ {
                            if (switchmodeindex == 1)
                                switchmodeindex = 0;
                            else
                                switchmodeindex++;
                            text_set_switchmode_val.setText(switchmodetype[switchmodeindex]);
                            mTvChannelManager.setAtvChannelSwitchMode(switchmodeindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceident:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001000)*/ {
                            if (sourceidentindex == SWITCH_ON) {
                                sourceidentindex = SWITCH_OFF;
                            } else {
                                sourceidentindex = SWITCH_ON;
                            }
                            text_set_autosourceident_val.setText(sourceidenttype[sourceidentindex]);
                            TvCommonManager.getInstance().setSourceIdentState(sourceidentindex);
                            enableChoose(linear_set_autosourceswit,
                                    sourceidentindex == SWITCH_OFF ? false : true);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_sourcepreview:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001011)*/ {
                            if (sourcepreviewindex == SWITCH_ON) {
                                sourcepreviewindex = SWITCH_OFF;
                            } else {
                                sourcepreviewindex = SWITCH_ON;
                            }

                            text_set_sourcepreview_val
                                    .setText(sourcepreviewtype[sourcepreviewindex]);
                            TvCommonManager.getInstance().setSourcePreviewState(sourcepreviewindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceswit:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001001)*/ {
                            if (sourceswitindex == SWITCH_ON) {
                                sourceswitindex = SWITCH_OFF;
                            } else {
                                sourceswitindex = SWITCH_ON;
                            }
                            text_set_autosourceswit_val.setText(sourceswittype[sourceswitindex]);
                            TvCommonManager.getInstance().setSourceSwitchState(sourceswitindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_colorrange:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010000)*/ {
                            if (colorrangeindex == 2)
                                colorrangeindex = 0;
                            else
                                colorrangeindex++;
                            text_set_colorrange_val.setText(colorrangetype[colorrangeindex]);
                            tvPictureManager.setColorRange((byte) colorrangeindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_mhlswitch:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010001)*/ {
                            if (mhlswitchindex == SWITCH_ON) {
                                mhlswitchindex = SWITCH_OFF;
                            } else {
                                mhlswitchindex = SWITCH_ON;
                            }
                            text_set_mhlswitch_val.setText(mhlswitchtype[mhlswitchindex]);
                            if (mhlswitchindex == SWITCH_OFF) {
                                tvMhlManager.setAutoSwitch(false);
                            } else {
                                tvMhlManager.setAutoSwitch(true);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_moviemode:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00100000)*/ {
                            if (moviemodeindex == SWITCH_ON) {
                                moviemodeindex = SWITCH_OFF;
                            } else {
                                moviemodeindex = SWITCH_ON;
                            }
                            text_set_moviemode_val.setText(moviemodetype[moviemodeindex]);
                            tvPictureManager.setFilm(moviemodeindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bluescreenswitch:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000000)*/ {
                            if (bluescreenindex == SWITCH_ON) {
                                bluescreenindex = SWITCH_OFF;
                            } else {
                                bluescreenindex = SWITCH_ON;
                            }
                            text_set_bluescreenswitch_val.setText(bluescreentype[bluescreenindex]);
                            mTvCommonManager.setBlueScreenMode(bluescreenindex == SWITCH_ON ? true
                                    : false);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmicec:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000001)*/ {
                            hdmicecstatus = mTvCecManager.getCecConfiguration();
                            if (hdmicecindex == SWITCH_ON) {
                                hdmicecindex = SWITCH_OFF;
                                //add for trun ARC off with CEC.lxk 20150107
                                hdmiarcindex = SWITCH_OFF;
                                hdmicecstatus.arcStatus = (short) hdmiarcindex;
                                text_set_hdmiarc_val.setText(hdmidarctype[hdmiarcindex]);
                                //end
                            } else {
                                hdmicecindex = SWITCH_ON;
                            }
                            hdmicecstatus.cecStatus = (short) hdmicecindex;
                            text_set_hdmicec_val.setText(hdmicectype[hdmicecindex]);
                            mTvCecManager.setCecConfiguration(hdmicecstatus);
                            enableChoose(linear_set_standyon, hdmicecindex == SWITCH_OFF ? false
                                    : true);
                            enableChoose(linear_set_hdmiarc, hdmicecindex == SWITCH_OFF ? false
                                    : true);
                            enableChooseTitle(linear_set_cec_list,
                                    hdmicecindex == SWITCH_OFF ? false : true);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_standyon:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000011)*/ {
                            hdmicecstatus = mTvCecManager.getCecConfiguration();
                            if (standyonindex == SWITCH_ON) {
                                standyonindex = SWITCH_OFF;
                            } else {
                                standyonindex = SWITCH_ON;
                            }
                            hdmicecstatus.autoStandby = (short) standyonindex;
                            text_set_standyon_val.setText(standyontype[standyonindex]);
                            mTvCecManager.setCecConfiguration(hdmicecstatus);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bootoption:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000110)*/ {
                            if (bootindex == SWITCH_ON) {
                                bootindex = SWITCH_OFF;
                            } else {
                                bootindex = SWITCH_ON;
                            }
                            if (bootindex == SWITCH_ON)
                            SystemProperties.set("persist.tvboot.firstshowtv", "0");
                            else
                            SystemProperties.set("persist.tvboot.firstshowtv", "1");
                            text_set_boot2launcher_val.setText(standyontype[bootindex]);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bootsource:
                        Log.d("wym", "status"+MainMenuActivity.getInstance().getSettingSelectStatus());
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000110) */{
                            InputSourceObject srcinfo = set2next_SrcInfo();
                            text_set_bootsource_val.setText(srcinfo.getInputSourceName());
                            updateDbValue(srcinfo.getInputSourceId());
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmiarc:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000101)*/ {
                        	cecSetting = mTvCecManager.getCecConfiguration();
                        	hdmiarcindex = cecSetting.arcStatus;
                            if (hdmiarcindex == SWITCH_ON) {
                                hdmiarcindex = SWITCH_OFF;
                            } else {
                                hdmiarcindex = SWITCH_ON;
                                /*release mut status when making ARC enable.lxk 20150126*/
                                if(SystemProperties.getInt("persist.sys.istvmute",0)==1)
                                {
                                    AudioManager mAudioManager = (AudioManager)this.settingActivity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                                    mAudioManager.setMasterMute(false);
                                }
                                /*end*/
                            }
                            text_set_hdmiarc_val.setText(hdmidarctype[hdmiarcindex]);
                            setHdmiArcMode(hdmiarcindex);
                        }
                        focusedid = currentid;
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_set_language:
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_audio_language1:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002) */{
                            if (mAudiolanguage1index == 0)
                                mAudiolanguage1index = (mAudioLanguageType.length - 1);
                            else
                                mAudiolanguage1index--;
                            if (setAudioLanguage(true, mAudiolanguage1index) == true) {
                                text_set_audio_language1_val
                                        .setText(mAudioLanguageType[mAudiolanguage1index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_audio_language2:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003)*/ {
                            if (mAudiolanguage2index == 0)
                                mAudiolanguage2index = (mAudioLanguageType.length - 1);
                            else
                                mAudiolanguage2index--;
                            if (setAudioLanguage(false, mAudiolanguage2index) == true) {
                                text_set_audio_language2_val
                                        .setText(mAudioLanguageType[mAudiolanguage2index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language1:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002)*/ {
                            if (mSubtitlelanguage1index == 0)
                                mSubtitlelanguage1index = (mSubtitleLanguageType.length - 1);
                            else
                                mSubtitlelanguage1index--;
                            if (setSubtitleLanguage(true, mSubtitlelanguage1index) == true) {
                                text_set_subtitle_language1_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage1index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language2:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003)*/ {
                            if (mSubtitlelanguage2index == 0)
                                mSubtitlelanguage2index = (mSubtitleLanguageType.length - 1);
                            else
                                mSubtitlelanguage2index--;
                            if (setSubtitleLanguage(false, mSubtitlelanguage2index) == true) {
                                text_set_subtitle_language2_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage2index]);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_menutime:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000010)*/ {
                            if (menutimeindex == 0)
                                menutimeindex = 5;
                            else
                                menutimeindex--;
                            text_set_menutime_val.setText(menutimetype[menutimeindex]);
                            mTvCommonManager.setOsdDuration(menutimeindex);
                            if (menutimeindex == 5) {
                                LittleDownTimer.stopMenu();
                            } else {
                                LittleDownTimer.setMenuStatus(mTvCommonManager
                                        .getOsdTimeoutInSecond());
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_switchmode:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000100)*/ {
                            if (switchmodeindex == 0)
                                switchmodeindex = 1;
                            else
                                switchmodeindex--;
                            text_set_switchmode_val.setText(switchmodetype[switchmodeindex]);
                            mTvChannelManager.setAtvChannelSwitchMode(switchmodeindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceident:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001000)*/ {
                            if (sourceidentindex == SWITCH_OFF) {
                                sourceidentindex = SWITCH_ON;
                            } else {
                                sourceidentindex = SWITCH_OFF;
                            }
                            text_set_autosourceident_val.setText(sourceidenttype[sourceidentindex]);
                            TvCommonManager.getInstance().setSourceIdentState(sourceidentindex);
                            enableChoose(linear_set_autosourceswit,
                                    sourceidentindex == SWITCH_OFF ? false : true);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_sourcepreview:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001011)*/ {
                            if (sourcepreviewindex == SWITCH_OFF) {
                                sourcepreviewindex = SWITCH_ON;
                            } else {
                                sourcepreviewindex = SWITCH_OFF;
                            }
                            text_set_sourcepreview_val
                                    .setText(sourcepreviewtype[sourcepreviewindex]);
                            TvCommonManager.getInstance().setSourcePreviewState(sourcepreviewindex);
                        }
                        focusedid = currentid;
                        break;
                        			
			 case R.id.linearlayout_set_scrolling_display:
                        focusedid = R.id.linearlayout_set_scrolling_display;
                        if (SystemProperties.getBoolean("persist.sys.showonidaview", false)) {
                        	RootActivity.showonidaview(false);
                        	textview_set_scrolling_display_val.setText(scrollingtype[0]);
                        	//RootActivity.isshowonidaview=false;
                        	SystemProperties.set("persist.sys.showonidaview","false");
						}else{
                        RootActivity.showonidaview(true);
                        textview_set_scrolling_display_val.setText(scrollingtype[1]);
                        //RootActivity.isshowonidaview=true;
                        SystemProperties.set("persist.sys.showonidaview","true");
						}
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceswit:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001001)*/ {
                            if (sourceswitindex == SWITCH_OFF) {
                                sourceswitindex = SWITCH_ON;
                            } else {
                                sourceswitindex = SWITCH_OFF;
                            }
                            text_set_autosourceswit_val.setText(sourceswittype[sourceswitindex]);
                            TvCommonManager.getInstance().setSourceSwitchState(sourceswitindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_colorrange:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010000)*/ {
                            if (colorrangeindex == 0)
                                colorrangeindex = 2;
                            else
                                colorrangeindex--;
                            text_set_colorrange_val.setText(colorrangetype[colorrangeindex]);
                            tvPictureManager.setColorRange((byte) colorrangeindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_mhlswitch:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010001)*/ {
                            if (mhlswitchindex == SWITCH_OFF) {
                                mhlswitchindex = SWITCH_ON;
                            } else {
                                mhlswitchindex = SWITCH_OFF;
                            }
                            text_set_mhlswitch_val.setText(mhlswitchtype[mhlswitchindex]);
                            if (mhlswitchindex == SWITCH_OFF) {
                                tvMhlManager.setAutoSwitch(false);
                            } else {
                                tvMhlManager.setAutoSwitch(true);
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_moviemode:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00100000)*/ {
                            if (moviemodeindex == SWITCH_OFF) {
                                moviemodeindex = SWITCH_ON;
                            } else {
                                moviemodeindex = SWITCH_OFF;
                            }
                            text_set_moviemode_val.setText(moviemodetype[moviemodeindex]);
                            tvPictureManager.setFilm(moviemodeindex);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bluescreenswitch:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000000)*/ {
                            if (bluescreenindex == SWITCH_OFF) {
                                bluescreenindex = SWITCH_ON;
                            } else {
                                bluescreenindex = SWITCH_OFF;
                            }
                            text_set_bluescreenswitch_val.setText(bluescreentype[bluescreenindex]);
                            mTvCommonManager.setBlueScreenMode(bluescreenindex == SWITCH_ON ? true
                                    : false);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmicec:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000001)*/ {
                            hdmicecstatus = mTvCecManager.getCecConfiguration();
                            if (hdmicecindex == SWITCH_OFF) {
                                hdmicecindex = SWITCH_ON;
                            } else {
                                hdmicecindex = SWITCH_OFF;
                                //add for trun ARC off with CEC.lxk 20150107
                                hdmiarcindex = SWITCH_OFF;
                                hdmicecstatus.arcStatus = (short) hdmiarcindex;
                                text_set_hdmiarc_val.setText(hdmidarctype[hdmiarcindex]);
                                //end
                            }
                            hdmicecstatus.cecStatus = (short) hdmicecindex;
                            text_set_hdmicec_val.setText(hdmicectype[hdmicecindex]);
                            mTvCecManager.setCecConfiguration(hdmicecstatus);
                            enableChoose(linear_set_standyon, hdmicecindex == SWITCH_OFF ? false
                                    : true);
                            enableChoose(linear_set_hdmiarc, hdmicecindex == SWITCH_OFF ? false
                                    : true);
                            enableChooseTitle(linear_set_cec_list,
                                    hdmicecindex == SWITCH_OFF ? false : true);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bootoption:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000110)*/ {
                            if (bootindex == SWITCH_ON) {
                                bootindex = SWITCH_OFF;
                            } else {
                                bootindex = SWITCH_ON;
                            }
                            if (bootindex == SWITCH_ON)
                            SystemProperties.set("persist.tvboot.firstshowtv", "0");
                            else
                            SystemProperties.set("persist.tvboot.firstshowtv", "1");
                            text_set_boot2launcher_val.setText(standyontype[bootindex]);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_standyon:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000011)*/ {
                            hdmicecstatus = mTvCecManager.getCecConfiguration();
                            if (standyonindex == SWITCH_OFF) {
                                standyonindex = SWITCH_ON;
                            } else {
                                standyonindex = SWITCH_OFF;
                            }
                            hdmicecstatus.autoStandby = (short) standyonindex;
                            text_set_standyon_val.setText(standyontype[standyonindex]);
                            mTvCecManager.setCecConfiguration(hdmicecstatus);
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bootsource:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000110)*/ {
                            InputSourceObject srcinfo = set2prev_SrcInfo();
                            text_set_bootsource_val.setText(srcinfo.getInputSourceName());
                            updateDbValue(srcinfo.getInputSourceId());
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmiarc:
                        /*if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000101)*/ {
                            cecSetting = mTvCecManager.getCecConfiguration();
                            hdmiarcindex = cecSetting.arcStatus;
                            if (hdmiarcindex == SWITCH_OFF) {
                                hdmiarcindex = SWITCH_ON;
                            } else {
                                hdmiarcindex = SWITCH_OFF;
                            }
                            text_set_hdmiarc_val.setText(hdmidarctype[hdmiarcindex]);
                            setHdmiArcMode(hdmiarcindex);
                        }
                        focusedid = currentid;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    public void closeDialogs() {
        if (null != mPasswordLock) {
            mPasswordLock.dismiss();
        }
        if (null != mPasswordCheck) {
            mPasswordCheck.dismiss();
        }
    }

    private void setOnClickLisenter() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View view) {
            	Log.d(TAG,"setting onClick--------*");
                int currentid = settingActivity.getCurrentFocus().getId();
                if (focusedid != currentid)
                    MainMenuActivity.getInstance().setSettingSelectStatus(0x00000000);
                switch (view.getId()) {
                    case R.id.linearlayout_set_pvrfs:
                        intent.setClass(settingActivity, PVROptionActivity.class);
                        settingActivity.startActivity(intent);
                        break;
                    case R.id.linearlayout_set_powersetting:
                        intent.setClass(settingActivity, PowerSettingActivity.class);
                        settingActivity.startActivity(intent);
                        break;
                    case R.id.linearlayout_set_audio_language1:
                        focusedid = R.id.linearlayout_set_audio_language1;
                        break;
                    case R.id.linearlayout_set_audio_language2:
                        focusedid = R.id.linearlayout_set_audio_language2;
                        break;
                    case R.id.linearlayout_set_subtitle_language1:
                        focusedid = R.id.linearlayout_set_subtitle_language1;
                        break;
                    case R.id.linearlayout_set_subtitle_language2:
                        focusedid = R.id.linearlayout_set_subtitle_language2;
                        break;
                    case R.id.linearlayout_set_menutime:
                        focusedid = R.id.linearlayout_set_menutime;
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                        break;
					case R.id.linearlayout_set_scrolling_display:
                        focusedid = R.id.linearlayout_set_scrolling_display;
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                        break;
                    case R.id.linearlayout_set_switchmode:
                        focusedid = R.id.linearlayout_set_switchmode;
                        break;
                    case R.id.linearlayout_set_hdmiarc:
                        focusedid = R.id.linearlayout_set_hdmiarc;
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                        break;
                    case R.id.linearlayout_set_bootoption:
                        focusedid = R.id.linearlayout_set_bootoption;
                        break;
                    case R.id.linearlayout_set_bootsource:
                        focusedid = R.id.linearlayout_set_bootsource;
                        break;
                    case R.id.linearlayout_set_autosourceident:
                        focusedid = R.id.linearlayout_set_autosourceident;
                        break;
                    case R.id.linearlayout_set_sourcepreview:
                        focusedid = R.id.linearlayout_set_sourcepreview;
                        break;
                    case R.id.linearlayout_set_autosourceswit:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00001001);
                        focusedid = R.id.linearlayout_set_autosourceswit;
                        break;
                    case R.id.linearlayout_set_colorrange:
                        focusedid = R.id.linearlayout_set_colorrange;
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                        break;
                    case R.id.linearlayout_set_mhlswitch:
                        focusedid = R.id.linearlayout_set_mhlswitch;
                        break;
                    case R.id.linearlayout_set_moviemode:
                        focusedid = R.id.linearlayout_set_moviemode;
                        break;
                    case R.id.linearlayout_set_bluescreenswitch:
                        focusedid = R.id.linearlayout_set_bluescreenswitch;
                        break;
                    case R.id.linearlayout_set_hdmicec:
                        focusedid = R.id.linearlayout_set_hdmicec;
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                        break;
                    case R.id.linearlayout_source:
                        focusedid = R.id.linearlayout_source;
                        Intent intentSource = new Intent(
                            "com.mstar.tvsetting.switchinputsource.intent.action.PictrueChangeActivity");
                        settingActivity.startActivity(intentSource);
                        break;
                    case R.id.linearlayout_set_standyon:
                        focusedid = R.id.linearlayout_set_standyon;
                        break;
                    case R.id.linearlayout_set_cec_list:
                        focusedid = R.id.linearlayout_set_cec_list;
                        getCecDeviceList();
                        AlertDialog.Builder builder = new AlertDialog.Builder(settingActivity);
                        builder.setTitle(R.string.str_set_hdmicec_devicelist)
                                .setItems(mCecListName, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        final EnumCecDeviceLa cecdevice = mCECDeviceList[which];
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mTvCecManager
                                                        .routingChangeInDeviceListSetting(cecdevice
                                                                .ordinal());
                                            }
                                        }, 1000);
                                        settingActivity.finish();
                                    }
                                	})
                                	.setOnKeyListener(new OnKeyListener() {
                                		@Override  
                                        public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                                				if (keyCode == KeyEvent.KEYCODE_MENU
                                						&& event.getRepeatCount() == 0
                                						&& event.getAction() == KeyEvent.ACTION_DOWN) { 
                                					dialog.dismiss();
                                					return true;  
                                				}  
                                				return false;  
                                       }  
                                   })
                                   .show();
                        break;
                    case R.id.linearlayout_location_code:
                        Utility.showLocationCodeInputDialog(settingActivity);
                        break;
                    case R.id.linearlayout_set_restoretodefault:
                    	//lg modification
                    	 if (TvParentalControlManager.getInstance().isSystemLock()) {
                             mPasswordLock.show();
                             return;
                         }/* else {
                             restoreToDefault();
                         }*/
                    	 //end
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(settingActivity);
                        dialogBuilder.setMessage(settingActivity.getResources().getString(
                                R.string.str_root_alert_dialog_message_restore_to_default));
                        dialogBuilder.setPositiveButton(
                                settingActivity.getResources().getString(
                                        R.string.str_root_alert_dialog_confirm),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainMenuActivity.getInstance().setSettingSelectStatus(
                                                0x10000000);
                                        focusedid = R.id.linearlayout_set_restoretodefault;
                                        restoreToDefault();
                                    }
                                });
                        dialogBuilder.setNegativeButton(
                                settingActivity.getResources().getString(
                                        R.string.str_root_alert_dialog_cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        dialogBuilder.create().show();
                        break;
                    default:
                        break;
                }
            }
        };
		linearlayout_set_scrolling_display.setOnClickListener(listener);
        linear_set_source.setOnClickListener(listener);
        linear_set_pvrfs.setOnClickListener(listener);
        linear_set_hdmicec.setOnClickListener(listener);
        linear_set_standyon.setOnClickListener(listener);
        linear_set_cec_list.setOnClickListener(listener);
        linear_set_powersetting.setOnClickListener(listener);
        linear_set_restoretodefault.setOnClickListener(listener);
        linear_set_audio_language1.setOnClickListener(listener);
        linear_set_audio_language2.setOnClickListener(listener);
        linear_set_subtitle_language1.setOnClickListener(listener);
        linear_set_subtitle_language2.setOnClickListener(listener);
        linear_set_menutime.setOnClickListener(listener);
        linear_set_switchmode.setOnClickListener(listener);
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
            linear_set_autosourceident.setOnClickListener(listener);
            linear_set_autosourceswit.setOnClickListener(listener);
        }
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_PREVIEW_MODE)) {
            linear_set_sourcepreview.setOnClickListener(listener);
        }
        linear_set_colorrange.setOnClickListener(listener);
        linear_set_mhlswitch.setOnClickListener(listener);
        linear_set_moviemode.setOnClickListener(listener);
        linear_set_bluescreenswitch.setOnClickListener(listener);
        linear_set_hdmiarc.setOnClickListener(listener);
        linear_location_code.setOnClickListener(listener);
        linear_set_boot2launcher.setOnClickListener(listener);
        linear_set_bootsource.setOnClickListener(listener);
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener FocuschangesListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout container = (LinearLayout) v;
                if (hasFocus)
                {
					container.getChildAt(0).setVisibility(View.VISIBLE);
	                container.getChildAt(3).setVisibility(View.VISIBLE);
                }
                else
                {
	                container.getChildAt(0).setVisibility(View.GONE);
	                container.getChildAt(3).setVisibility(View.GONE);
                }
                MainMenuActivity.getInstance().setSettingSelectStatus(0x00000000);
            }
        };
		linearlayout_set_scrolling_display.setOnFocusChangeListener(FocuschangesListener);
        linear_set_hdmicec.setOnFocusChangeListener(FocuschangesListener);
        linear_set_standyon.setOnFocusChangeListener(FocuschangesListener);
        linear_set_powersetting.setOnFocusChangeListener(FocuschangesListener);
        linear_set_audio_language1.setOnFocusChangeListener(FocuschangesListener);
        linear_set_audio_language2.setOnFocusChangeListener(FocuschangesListener);
        linear_set_subtitle_language1.setOnFocusChangeListener(FocuschangesListener);
        linear_set_subtitle_language2.setOnFocusChangeListener(FocuschangesListener);
        linear_set_menutime.setOnFocusChangeListener(FocuschangesListener);
        linear_set_switchmode.setOnFocusChangeListener(FocuschangesListener);
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
            linear_set_autosourceident.setOnFocusChangeListener(FocuschangesListener);
            linear_set_autosourceswit.setOnFocusChangeListener(FocuschangesListener);
        }
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_PREVIEW_MODE)) {
            linear_set_sourcepreview.setOnFocusChangeListener(FocuschangesListener);
        }
        linear_set_colorrange.setOnFocusChangeListener(FocuschangesListener);
        linear_set_mhlswitch.setOnFocusChangeListener(FocuschangesListener);
        linear_set_moviemode.setOnFocusChangeListener(FocuschangesListener);
        linear_set_bluescreenswitch.setOnFocusChangeListener(FocuschangesListener);
        linear_set_hdmiarc.setOnFocusChangeListener(FocuschangesListener);
        linear_set_boot2launcher.setOnFocusChangeListener(FocuschangesListener);
        linear_set_bootsource.setOnFocusChangeListener(FocuschangesListener);
    }

    private void setOnTouchListeners() {
        setMyOntouchListener(R.id.linearlayout_set_audio_language1, 0x00000002,
                linear_set_audio_language1);
        setMyOntouchListener(R.id.linearlayout_set_audio_language2, 0x00000003,
                linear_set_audio_language2);
        setMyOntouchListener(R.id.linearlayout_set_subtitle_language1, 0x00000004,
                linear_set_subtitle_language1);
        setMyOntouchListener(R.id.linearlayout_set_subtitle_language2, 0x00000005,
                linear_set_subtitle_language2);
        setMyOntouchListener(R.id.linearlayout_set_menutime, 0x00000010, linear_set_menutime);
        setMyOntouchListener(R.id.linearlayout_set_switchmode, 0x00000100, linear_set_switchmode);
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
            setMyOntouchListener(R.id.linearlayout_set_autosourceident, 0x00001000,
                    linear_set_autosourceident);
            setMyOntouchListener(R.id.linearlayout_set_autosourceswit, 0x00001001,
                    linear_set_autosourceswit);
        }
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_PREVIEW_MODE)) {
            setMyOntouchListener(R.id.linearlayout_set_sourcepreview, 0x00001011,
                    linear_set_sourcepreview);
        }
        setMyOntouchListener(R.id.linearlayout_set_colorrange, 0x00010000, linear_set_colorrange);
        setMyOntouchListener(R.id.linearlayout_set_mhlswitch, 0x00010001, linear_set_mhlswitch);
        setMyOntouchListener(R.id.linearlayout_set_moviemode, 0x00100000, linear_set_moviemode);
        setMyOntouchListener(R.id.linearlayout_set_bluescreenswitch, 0x01000000,
                linear_set_bluescreenswitch);
        setMyOntouchListener(R.id.linearlayout_set_hdmicec, 0x01000001, linear_set_hdmicec);
        setMyOntouchListener(R.id.linearlayout_set_standyon, 0x01000011, linear_set_standyon);
        setMyOntouchListener(R.id.linearlayout_set_hdmiarc, 0x00000101, linear_set_hdmiarc);
        setMyOntouchListener(R.id.linearlayout_set_bootoption, 0x00000110, linear_set_boot2launcher);
        setMyOntouchListener(R.id.linearlayout_set_bootsource, 0x00000110, linear_set_bootsource);
        setMyOntouchListener(R.id.linearlayout_set_scrolling_display, 0x01000001, linearlayout_set_scrolling_display);
    }

    private void setMyOntouchListener(final int resID, final int status, LinearLayout lay) {
        lay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    v.requestFocus();
                    MainMenuActivity.getInstance().setSettingSelectStatus(status);
                    focusedid = resID;
                    onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                }
                return true;
            }
        });
    }

    private void setHdmiArcMode(int type) {
        Log.i(TAG, "----HDMI ARC MODE----:" + type);
        cecSetting.arcStatus = (short) type;
        mTvCecManager.setCecConfiguration(cecSetting);
    }

    private boolean setAudioLanguage(boolean isPrimary, int selIdx) {
        int langId = 0;

        if (mAudioLanguageEnumValueArray.length != mAudioLanguageType.length) {
            throw new IllegalAccessError("Different length array!");
        }

        if ((selIdx < 0) || (selIdx > (mAudioLanguageType.length - 1))) {
            return false;
        }

        langId = mAudioLanguageEnumValueArray[selIdx];
        Log.i(TAG, "setAudioLanguage(), selIdx = " + selIdx + ", id = " + langId + ", isPrimary = "
                + isPrimary);

        if (isPrimary) {
            mTvChannelManager.setAudioLanguageDefaultValue(langId);
        } else {
            mTvChannelManager.setAudioLanguageSecondaryValue(langId);
        }
        return true;
    }

    private int getAudioLanguage(boolean isPrimary) {
        int langId = 0;

        if (isPrimary) {
            langId = mTvChannelManager.getAudioLanguageDefaultValue();
        } else {
            langId = mTvChannelManager.getAudioLanguageSecondaryValue();
        }
        Log.i(TAG, "getAudioLanguage(), id = " + langId + ", isPrimary = " + isPrimary);

        for (int index = 0; index < mAudioLanguageEnumValueArray.length; index++) {
            if (langId == mAudioLanguageEnumValueArray[index]) {
                Log.i(TAG, "audio language:" + mAudioLanguageEnumValueArray[index]);
                return index;
            }
        }

        return 0;
    }

    private boolean setSubtitleLanguage(boolean isPrimary, int selIdx) {
        int langId = 0;
        if ((selIdx < 0) || (selIdx > (mSubtitleLanguageType.length - 1))) {
            return false;
        }

        if (mSubtitleLanguageEnumValueArray.length != mSubtitleLanguageType.length) {
            throw new IllegalAccessError("Different length array!");
        }

        langId = mSubtitleLanguageEnumValueArray[selIdx];
        Log.i(TAG, "setSubtitleLanguage(), selIdx = " + selIdx + ", id = " + langId
                + ", isPrimary = " + isPrimary);

        if (isPrimary) {
            mTvCommonManager.setSubtitlePrimaryLanguage(langId);
        } else {
            mTvCommonManager.setSubtitleSecondaryLanguage(langId);
        }
        return true;
    }

    private int getSubtitleLanguage(boolean isPrimary) {
        int langId = 0;
        if (isPrimary) {
            langId = mTvCommonManager.getSubtitlePrimaryLanguage();
        } else {
            langId = mTvCommonManager.getSubtitleSecondaryLanguage();
        }
        Log.i(TAG, "getSubtitleLanguage(), id = " + langId + ", isPrimary = " + isPrimary);

        for (int index = 0; index < mSubtitleLanguageEnumValueArray.length; index++) {
            if (langId == mSubtitleLanguageEnumValueArray[index]) {
                Log.i(TAG, "subtitle language:" + mSubtitleLanguageEnumValueArray[index]);
                return index;
            }
        }
        return 0;
    }

    private void getCecDeviceList() {

        String[] DeviceName = new String[EnumCecDeviceLa.E_UNREGISTERED.ordinal()];
        // init DeviceListCount before use it
        DeviceListCount = 0;
        // Init the Device List for each LA
        for (int i = 0; i < EnumCecDeviceLa.E_UNREGISTERED.ordinal(); i++) {
            mCECDeviceList[i] = EnumCecDeviceLa.E_TV;
        }

        for (int i = EnumCecDeviceLa.E_TV.ordinal(); i < EnumCecDeviceLa.E_UNREGISTERED.ordinal(); i++) {

            if (!mTvCecManager.getDeviceName(i).isEmpty()) {

                DeviceName[DeviceListCount] = mTvCecManager.getDeviceName(i);

                mCECDeviceList[DeviceListCount++] = EnumCecDeviceLa.values()[i];
                Log.i(TAG, "LA index:" + i + "Device name:" + mTvCecManager.getDeviceName(i));
            } else {
                Log.i(TAG, "LA index:" + i + " Device is not avaliable in this LA(logical address)");
            }
        }

        mCecListName = new String[DeviceListCount];

        for (int i = 0; i < DeviceListCount; i++) {
            mCecListName[i] = DeviceName[i];
        }

        if (DeviceListCount > 0) {
            Log.i(TAG, "There is/are " + DeviceListCount + " CEC device(s)");
        }
    }

    public void setFocusId(int id) {
        focusedid = id;
    }

    private void restoreToDefault() {
        if (true == ActivityManager.isUserAMonkey()) {
            Log.i(TAG, "mounkey is running, ignore to restore to default!");
            return;
        }
        if (null != tvFactoryManager && null != mTvCommonManager) {
			//zb20160108 add
			int old_passwd = TvParentalControlManager.getInstance().getParentalPassword();
			boolean lockstatus = TvParentalControlManager.getInstance().isSystemLock();
			int currentRate = TvParentalControlManager.getInstance().getParentalControlRating();
			//end
			//modify by hz 20160726,userOSDRestoreToDefault()-->restoreToDefault()
            if (tvFactoryManager.userRestoreToDefault() == true) {
				TvParentalControlManager.getInstance().setParentalPassword(old_passwd);
				TvParentalControlManager.getInstance().setSystemLock(lockstatus);
				TvParentalControlManager.getInstance().setParentalControlRating(currentRate);
                int currInputSource = mTvCommonManager.getCurrentTvInputSource();
			
                if (currInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                    tvS3DManager
                            .setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
                    if ((mTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ISDB)
                            && (mTvCommonManager.getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ATSC)) {
                        // Because restore to factory default value,reset
                        // routeIndex to 0
                        TvDvbChannelManager.getInstance().setDtvAntennaType(0);
                    }
                }

                // Reset First Startup Input Source
                SharedPreferences preferencesSettings = settingActivity.getSharedPreferences(
                        Constant.PREFERENCES_INPUT_SOURCE, Context.MODE_PRIVATE);
                preferencesSettings.edit().remove(Constant.PREFERENCES_PREVIOUS_INPUT_SOURCE)
                        .commit();

                preferencesSettings = settingActivity.getSharedPreferences(
                        Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
                // Reset Setup Wizard
                preferencesSettings.edit().remove(Constant.PREFERENCES_IS_AUTOSCAN_LAUNCHED)
                        .commit();
                // Reset Setup Location Wizard
                preferencesSettings.edit().remove(Constant.PREFERENCES_IS_LOCATION_SELECTED)
                        .commit();
                preferencesSettings.edit().commit();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mTvCommonManager.rebootSystem("reboot");
                /*
                 * Added by gerard.jiang for "0386249" in 2013/04/28. Add reboot
                 * flag
                 */
                RootActivity.setRebootFlag(true);
                /***** Ended by gerard.jiang 2013/04/28 *****/
            } else {
                Log.e(TAG, "restoreToDefault failed!");
            }
        }
    }

    private void getSupportHdmiEdidVersionList() {
        if (TvCommonManager.getInstance() != null) {
            boolean bSupportHdmiEdidVersion;
            int[] supportHdmiEdidVersionList = TvCommonManager.getInstance()
                    .getHdmiEdidVersionList();
            for (int i = 0; i < supportHdmiEdidVersionList.length; i++) {
                if (supportHdmiEdidVersionList[i] != TvCommonManager.EDID_VERSION_UNSUPPORT) {
                    bSupportHdmiEdidVersion = true;
                } else {
                    bSupportHdmiEdidVersion = false;
                }
                mComboHdmiEdidVersion.setItemEnable(i, bSupportHdmiEdidVersion);
            }
        }
    }

    /*
     * Create ComboButton for switching HDMI EDID version
     */
    private void createEdidSwitch(final int inputSource) {
        mProgressDialog = new ProgressDialog(settingActivity);
        mProgressDialog.setTitle(settingActivity.getResources().getString(
                R.string.str_hdmi_edid_version_change));
        mProgressDialog.setMessage(settingActivity.getResources().getString(
                R.string.str_hdmi_edid_version_wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);

        mComboHdmiEdidVersion = new ComboButton(settingActivity, settingActivity.getResources()
                .getStringArray(R.array.str_arr_hdmi_edid_version),
                R.id.linearlayout_hdmi_edid_version, 1, 2, ComboButton.NEED_SELECTED_BEFORE_SWITCH) {
            @Override
            public void doUpdate() {
                super.doUpdate();

                LittleDownTimer.pauseItem();
                LittleDownTimer.pauseMenu();
                mProgressDialog.show();

                if (mThreadEdidChange == null
                        || mThreadEdidChange.getState() == Thread.State.TERMINATED) {
                    mThreadEdidChange = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "new thread starting change edid version !");
                            if (TvCommonManager.getInstance().setHdmiEdidVersionBySource(
                                    inputSource, getIdx()) != true) {
                                Log.e(TAG, "setHdmiEdidVersion failed!");
                            }

                            /* Prepare resuming to menu */
                            if (null != mProgressDialog) {
                                mProgressDialog.dismiss();
                            }

                            LittleDownTimer.resetItem();
                            LittleDownTimer.resetMenu();
                            LittleDownTimer.resumeItem();
                            LittleDownTimer.resumeMenu();
                        }
                    });
                    mThreadEdidChange.start();
                } else {
                    Log.d(TAG,
                            "Abort, another thread is already starting changing edid. the combobutton is not switchable until it finish");
                }
            }
        };

        getSupportHdmiEdidVersionList();
        mComboHdmiEdidVersion.setIdx(mTvCommonManager.getHdmiEdidVersionBySource(inputSource));
    }

    private final int[] mInputAdjustaleSource = new int[]{
        TvCommonManager.INPUT_SOURCE_VGA,
        TvCommonManager.INPUT_SOURCE_ATV,
        TvCommonManager.INPUT_SOURCE_CVBS,
        TvCommonManager.INPUT_SOURCE_CVBS2,
        TvCommonManager.INPUT_SOURCE_CVBS3,
        TvCommonManager.INPUT_SOURCE_CVBS4,
        TvCommonManager.INPUT_SOURCE_CVBS5,
        TvCommonManager.INPUT_SOURCE_CVBS6,
        TvCommonManager.INPUT_SOURCE_CVBS7,
        TvCommonManager.INPUT_SOURCE_CVBS8,
        TvCommonManager.INPUT_SOURCE_SVIDEO,
        TvCommonManager.INPUT_SOURCE_SVIDEO2,
        TvCommonManager.INPUT_SOURCE_SVIDEO3,
        TvCommonManager.INPUT_SOURCE_SVIDEO4,
        TvCommonManager.INPUT_SOURCE_YPBPR,
        TvCommonManager.INPUT_SOURCE_YPBPR2,
        TvCommonManager.INPUT_SOURCE_YPBPR3,
        TvCommonManager.INPUT_SOURCE_SCART,
        TvCommonManager.INPUT_SOURCE_SCART2,
        TvCommonManager.INPUT_SOURCE_HDMI,
        TvCommonManager.INPUT_SOURCE_HDMI2,
        TvCommonManager.INPUT_SOURCE_HDMI3,
        TvCommonManager.INPUT_SOURCE_HDMI4,
        TvCommonManager.INPUT_SOURCE_DTV,
        TvCommonManager.INPUT_SOURCE_DVI,
        TvCommonManager.INPUT_SOURCE_DVI2,
        TvCommonManager.INPUT_SOURCE_DVI3,
        TvCommonManager.INPUT_SOURCE_DVI4,
        TvCommonManager.INPUT_SOURCE_VGA2,
        TvCommonManager.INPUT_SOURCE_VGA3,
    };
    ArrayList<InputSourceObject> mPfos = new ArrayList<InputSourceObject>();
    private String[] mLiteralInputSources = null;
    private class InputSourceObject {
        private int inputSourceId = -1;

        private String inputSourceName = "";

        public int getInputSourceId() {
            return inputSourceId;
        }

        public void setInputSourceId(int inputSourceId) {
            this.inputSourceId = inputSourceId;
        }

        public String getInputSourceName() {
            return inputSourceName;
        }

        public void setInputSourceName(String inputSourceName) {
            this.inputSourceName = inputSourceName;
        }
    }
    private void getSrcListInfo() {
        int[] sourceList = TvCommonManager.getInstance().getSourceList();
        if (null == sourceList) {
            Log.e(TAG, "No input source avaiable.");
            return;
        }
        int inputSource = -1;
        int counter = 0;
        //boolean[] tempIsChannelLocked = new boolean[mInputAdjustaleSource.length];

        if (mPfos.size() != 0)
            mPfos.clear();
        InputSourceObject firstprof = new InputSourceObject();
        firstprof.setInputSourceId(TvCommonManager.INPUT_SOURCE_NUM);
        firstprof.setInputSourceName(new String("Last Source"));
        mPfos.add(firstprof);
        for (int i = 0; i < mInputAdjustaleSource.length; i++) {
            inputSource = mInputAdjustaleSource[i];
            if ((inputSource < sourceList.length) && (0 != sourceList[inputSource])) {
                InputSourceObject pfo = new InputSourceObject();
                pfo.setInputSourceId(inputSource);
                pfo.setInputSourceName(mLiteralInputSources[inputSource]);
                mPfos.add(pfo);
                //tempIsChannelLocked[counter] = TvCommonManager.getInstance().getInputSourceLock(inputSource);
                //Log.d(TAG, "tempIsChannelLocked["+counter+"] = " + tempIsChannelLocked[counter]);
                //counter++;
            }
        }
    }
    private InputSourceObject set2next_SrcInfo() {
        bootsourceindex++;
        if (bootsourceindex >= mPfos.size())
            bootsourceindex = 0;
        return mPfos.get(bootsourceindex);
    }
    private InputSourceObject set2prev_SrcInfo() {
        if (bootsourceindex > 0)
            bootsourceindex -= 1;
        else
            bootsourceindex = mPfos.size() - 1;
        return mPfos.get(bootsourceindex);
    }
    private InputSourceObject getCurSrcInfo() {
        if (bootsourceindex < 0)
            bootsourceindex = 0;
        else if (bootsourceindex >= mPfos.size())
            bootsourceindex = 0;
        return mPfos.get(bootsourceindex);
    }
    private void setSrcIndex(int srcinfo) {
        int i;

        bootsourceindex = 0;

        for (i = 0; i < mPfos.size(); i++)
        {
            if (srcinfo == mPfos.get(i).getInputSourceId())
                bootsourceindex = i;
        }
    }
    public int queryDbValue() {
        int lastsource = -1;
        Cursor cursor = this.settingActivity
                .getApplicationContext()
                .getContentResolver()
                .query(Uri.parse("content://mstar.tv.usersetting/systemsetting"),
                       null, null, null, null);
        if (cursor.moveToFirst()) {
            lastsource = cursor.getInt(cursor.getColumnIndex("enSelInputSourceType"));
        }
        cursor.close();
        return lastsource;
    }
    public void updateDbValue(int val) {
        ContentValues vals = new ContentValues();
        vals.put("enSelInputSourceType", val);

        try {
            this.settingActivity
                .getApplicationContext()
                .getContentResolver()
                .update(Uri.parse("content://mstar.tv.usersetting/systemsetting"),
                      vals, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showRestoreConfirmDialog()
    {
		 //lg modification
         if (TvParentalControlManager.getInstance().isSystemLock()) {
         	mPasswordLock.show();
            return;
    	 }
    	//end
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(settingActivity);
            dialogBuilder.setMessage(settingActivity.getResources().getString(
                    R.string.str_root_alert_dialog_message_restore_to_default));
        dialogBuilder.setPositiveButton(
                settingActivity.getResources().getString(
                        R.string.str_root_alert_dialog_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainMenuActivity.getInstance().setSettingSelectStatus(
                                0x10000000);
                        focusedid = R.id.linearlayout_set_restoretodefault;
                        restoreToDefault();
                    }
                });
        dialogBuilder.setNegativeButton(
                settingActivity.getResources().getString(
                        R.string.str_root_alert_dialog_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialogBuilder.create().show();
    }
}
