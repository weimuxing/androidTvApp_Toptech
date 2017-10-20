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
import com.mstar.tv.tvplayer.ui.component.CycleScrollView;
import com.mstar.tv.tvplayer.ui.component.MyButton;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.TvIntent;
import com.mstar.tv.tvplayer.ui.LittleDownTimer;
import com.mstar.tv.tvplayer.ui.MainMenuActivity;
import com.mstar.tv.tvplayer.ui.MenuConstants;
import com.mstar.tv.tvplayer.ui.RootActivity;
import com.mstar.tv.tvplayer.ui.SwitchPageHelper;
import com.mstar.tv.tvplayer.ui.component.PasswordCheckDialog;
import com.mstar.tv.tvplayer.ui.dtv.AudioLanguageActivity;
import com.mstar.tv.tvplayer.ui.dtv.MTSInfoActivity;
import com.mstar.tv.tvplayer.ui.dtv.SubtitleLanguageActivity;
import com.mstar.tv.tvplayer.ui.dtv.epg.atsc.AtscEPGActivity;
import com.mstar.tv.tvplayer.ui.pvr.PVROptionActivity;
import com.mstar.tv.tvplayer.ui.settings.PowerSettingActivity;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.util.Constant;
import com.mstar.util.Utility;

public class SettingViewHolder {
    // 16*16 switchmode...
    private static final String TAG = "SettingViewHolder";

    private static final int SWITCH_OFF = 0;

    private static final int SWITCH_ON = 1;

    private static final int HBBTV_OFF = 0;

    private static final int HBBTV_ON = 1;

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

    protected LinearLayout linear_set_pvrfs;

    protected LinearLayout linear_set_hdmicec;

    protected LinearLayout linear_set_cec_list;

    protected LinearLayout linear_set_standyon;

    protected LinearLayout linear_set_mts;

    protected LinearLayout linear_set_subtitle;

    protected LinearLayout linear_set_powersetting;

    protected LinearLayout linear_hbbtv_onoff;

    protected LinearLayout linear_location_code;

    protected LinearLayout linear_set_restoretodefault;

    protected LinearLayout linear_set_audio_language1;

    protected LinearLayout linear_set_audio_language2;

    protected LinearLayout linear_set_subtitle_language1;

    protected LinearLayout linear_set_subtitle_language2;

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

    protected CycleScrollView mScrollView;

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

    private int standyonindex = SWITCH_OFF;

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

    private String[] standyontype;

    private String[] colorrangetype;

    private Handler handler;

    private String[] mCecListName;

    private EnumCecDeviceLa[] mCECDeviceList;

    private ComboButton mComboSubtitleSwitch = null;

    private ComboButton mComboHdmiEdidVersion = null;

    private ComboButton mComboHbbTVOnOff = null;

    private MyButton mButtonInputSourceLock;

    private int mHdmiEdidAutoSwitchIndex = 0;

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
            TvLanguage.TAMIL,
            TvLanguage.QAA,
            TvLanguage.QAB,
            TvLanguage.QAC,
            TvLanguage.INDONESIAN,
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
            TvLanguage.TAMIL,
            TvLanguage.MULTIPLE,
            TvLanguage.INDONESIAN,
    };

    public SettingViewHolder(Activity activity, Handler handler) {
        this.settingActivity = activity;
        mTvCommonManager = TvCommonManager.getInstance();
        mTvSystem = mTvCommonManager.getCurrentTvSystem();
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
        mScrollView = (CycleScrollView) settingActivity
                .findViewById(R.id.cyclescrollview_setting_scroll_view);
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
        linear_set_pvrfs = (LinearLayout) settingActivity.findViewById(R.id.linearlayout_set_pvrfs);
        TVRootApp app = (TVRootApp) settingActivity.getApplication();
        if (app.isPVREnable() == false) {
            linear_set_pvrfs.setVisibility(View.GONE);
        }
        linear_set_hdmicec = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_hdmicec);
        linear_set_standyon = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_standyon);
        linear_set_cec_list = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_cec_list);
        linear_set_mts = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_mts);
        linear_set_subtitle = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_subtitle);
        linear_set_powersetting = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_powersetting);
        linear_set_audio_language1 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_audio_language1);
        linear_set_audio_language2 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_audio_language2);
        linear_set_subtitle_language1 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_subtitle_language1);
        linear_set_subtitle_language2 = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_subtitle_language2);
        if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
            linear_set_subtitle_language1.setVisibility(View.GONE);
            linear_set_subtitle_language2.setVisibility(View.GONE);
        }
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
        linear_set_moviemode = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_moviemode);
        linear_set_bluescreenswitch = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_bluescreenswitch);
        linear_set_mhlswitch = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_mhlswitch);
        linear_set_hdmiarc = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_hdmiarc);
        linear_hbbtv_onoff = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_hbbtv_switch);
        linear_location_code = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_location_code);
        linear_set_restoretodefault = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_set_restoretodefault);
        linear_set_hdmiedidversion = (LinearLayout) settingActivity
                .findViewById(R.id.linearlayout_hdmi_edid_version);
        linear_setting = (LinearLayout) settingActivity.findViewById(R.id.linearlayout_setting);
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
        linear_set_menutime.requestFocus();
        linear_set_bluescreenswitch.setVisibility(View.GONE);
        setOnClickLisenter();
        setOnFocusChangeListeners();
        setOnTouchListeners();
        Configuration config = settingActivity.getResources().getConfiguration();
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
        }
        cursor.close();

        menutimeindex = mTvCommonManager.getOsdDuration();
        moviemodeindex = tvPictureManager.getFilm();
        bluescreenindex = mTvCommonManager.getBlueScreenMode() ? SWITCH_ON : SWITCH_OFF;
        hdmicecstatus = mTvCecManager.getCecConfiguration();
        hdmicecindex = hdmicecstatus.cecStatus;
        standyonindex = hdmicecstatus.autoStandby;
        hdmiarcindex = cecSetting.arcStatus;
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
                /* set black */
                text_set_switchmode_val.setText(switchmodetype[0]);
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

                restoreToDefault();
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
        Utility.setDefaultFocus(linear_setting);
    }

    public void pageOnFocus() {
        mTvCommonManager.speakTtsDelayed(
            settingActivity.getApplicationContext().getString(R.string.str_set_setting)
            , TvCommonManager.TTS_QUEUE_FLUSH
            , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
            , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
        mScrollView.ttsSpeakFocusItem();
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
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002) {
                            if (mAudiolanguage1index == (mAudioLanguageType.length - 1))
                                mAudiolanguage1index = 0;
                            else
                                mAudiolanguage1index++;
                            if (setAudioLanguage(true, mAudiolanguage1index) == true) {
                                text_set_audio_language1_val
                                        .setText(mAudioLanguageType[mAudiolanguage1index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_audio_language2:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003) {
                            if (mAudiolanguage2index == (mAudioLanguageType.length - 1))
                                mAudiolanguage2index = 0;
                            else
                                mAudiolanguage2index++;
                            if (setAudioLanguage(false, mAudiolanguage2index) == true) {
                                text_set_audio_language2_val
                                        .setText(mAudioLanguageType[mAudiolanguage2index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language1:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002) {
                            if (mSubtitlelanguage1index == (mSubtitleLanguageType.length - 1))
                                mSubtitlelanguage1index = 0;
                            else
                                mSubtitlelanguage1index++;
                            if (setSubtitleLanguage(true, mSubtitlelanguage1index) == true) {
                                text_set_subtitle_language1_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage1index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language2:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003) {
                            if (mSubtitlelanguage2index == (mSubtitleLanguageType.length - 1))
                                mSubtitlelanguage2index = 0;
                            else
                                mSubtitlelanguage2index++;
                            if (setSubtitleLanguage(false, mSubtitlelanguage2index) == true) {
                                text_set_subtitle_language2_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage2index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;

                    case R.id.linearlayout_set_menutime:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000010) {
                            boolean bEnable = true;
                            int second = 10;
                            if (menutimeindex == 5)
                                menutimeindex = 0;
                            else
                                menutimeindex++;
                            text_set_menutime_val.setText(menutimetype[menutimeindex]);
                            ttsSpeakComboButtonVolue();
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
                    case R.id.linearlayout_set_switchmode:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000100) {
                            if (switchmodeindex == 1)
                                switchmodeindex = 0;
                            else
                                switchmodeindex++;
                            text_set_switchmode_val.setText(switchmodetype[switchmodeindex]);
                            mTvChannelManager.setAtvChannelSwitchMode(switchmodeindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceident:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001000) {
                            if (sourceidentindex == SWITCH_ON) {
                                sourceidentindex = SWITCH_OFF;
                            } else {
                                sourceidentindex = SWITCH_ON;
                            }
                            text_set_autosourceident_val.setText(sourceidenttype[sourceidentindex]);
                            mTvCommonManager.setSourceIdentState(sourceidentindex);
                            enableChoose(linear_set_autosourceswit,
                                    sourceidentindex == SWITCH_OFF ? false : true);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_sourcepreview:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001011) {
                            if (sourcepreviewindex == SWITCH_ON) {
                                sourcepreviewindex = SWITCH_OFF;
                            } else {
                                sourcepreviewindex = SWITCH_ON;
                            }
                            text_set_sourcepreview_val
                                    .setText(sourcepreviewtype[sourcepreviewindex]);
                            mTvCommonManager.setSourcePreviewState(sourcepreviewindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceswit:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001001) {
                            if (sourceswitindex == SWITCH_ON) {
                                sourceswitindex = SWITCH_OFF;
                            } else {
                                sourceswitindex = SWITCH_ON;
                            }
                            text_set_autosourceswit_val.setText(sourceswittype[sourceswitindex]);
                            mTvCommonManager.setSourceSwitchState(sourceswitindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_colorrange:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010000) {
                            if (colorrangeindex == 2)
                                colorrangeindex = 0;
                            else
                                colorrangeindex++;
                            text_set_colorrange_val.setText(colorrangetype[colorrangeindex]);
                            tvPictureManager.setColorRange((byte) colorrangeindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_mhlswitch:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010001) {
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
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_moviemode:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00100000) {
                            if (moviemodeindex == SWITCH_ON) {
                                moviemodeindex = SWITCH_OFF;
                            } else {
                                moviemodeindex = SWITCH_ON;
                            }
                            text_set_moviemode_val.setText(moviemodetype[moviemodeindex]);
                            tvPictureManager.setFilm(moviemodeindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bluescreenswitch:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000000) {
                            if (bluescreenindex == SWITCH_ON) {
                                bluescreenindex = SWITCH_OFF;
                            } else {
                                bluescreenindex = SWITCH_ON;
                            }
                            text_set_bluescreenswitch_val.setText(bluescreentype[bluescreenindex]);
                            mTvCommonManager.setBlueScreenMode(bluescreenindex == SWITCH_ON ? true
                                    : false);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmicec:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000001) {
                            if (hdmicecindex == SWITCH_ON) {
                                hdmicecindex = SWITCH_OFF;
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
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_standyon:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000011) {
                            if (standyonindex == SWITCH_ON) {
                                standyonindex = SWITCH_OFF;
                            } else {
                                standyonindex = SWITCH_ON;
                            }
                            hdmicecstatus.autoStandby = (short) standyonindex;
                            text_set_standyon_val.setText(standyontype[standyonindex]);
                            mTvCecManager.setCecConfiguration(hdmicecstatus);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmiarc:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000101) {
                            if (hdmiarcindex == SWITCH_ON) {
                                hdmiarcindex = SWITCH_OFF;
                            } else {
                                hdmiarcindex = SWITCH_ON;
                            }
                            text_set_hdmiarc_val.setText(hdmidarctype[hdmiarcindex]);
                            setHdmiArcMode(hdmiarcindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_set_audio_language1:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002) {
                            if (mAudiolanguage1index == 0)
                                mAudiolanguage1index = (mAudioLanguageType.length - 1);
                            else
                                mAudiolanguage1index--;
                            if (setAudioLanguage(true, mAudiolanguage1index) == true) {
                                text_set_audio_language1_val
                                        .setText(mAudioLanguageType[mAudiolanguage1index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_audio_language2:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003) {
                            if (mAudiolanguage2index == 0)
                                mAudiolanguage2index = (mAudioLanguageType.length - 1);
                            else
                                mAudiolanguage2index--;
                            if (setAudioLanguage(false, mAudiolanguage2index) == true) {
                                text_set_audio_language2_val
                                        .setText(mAudioLanguageType[mAudiolanguage2index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language1:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000002) {
                            if (mSubtitlelanguage1index == 0)
                                mSubtitlelanguage1index = (mSubtitleLanguageType.length - 1);
                            else
                                mSubtitlelanguage1index--;
                            if (setSubtitleLanguage(true, mSubtitlelanguage1index) == true) {
                                text_set_subtitle_language1_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage1index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_subtitle_language2:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000003) {
                            if (mSubtitlelanguage2index == 0)
                                mSubtitlelanguage2index = (mSubtitleLanguageType.length - 1);
                            else
                                mSubtitlelanguage2index--;
                            if (setSubtitleLanguage(false, mSubtitlelanguage2index) == true) {
                                text_set_subtitle_language2_val
                                        .setText(mSubtitleLanguageType[mSubtitlelanguage2index]);
                                ttsSpeakComboButtonVolue();
                            }
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_menutime:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000010) {
                            if (menutimeindex == 0)
                                menutimeindex = 5;
                            else
                                menutimeindex--;
                            text_set_menutime_val.setText(menutimetype[menutimeindex]);
                            ttsSpeakComboButtonVolue();
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
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000100) {
                            if (switchmodeindex == 0)
                                switchmodeindex = 1;
                            else
                                switchmodeindex--;
                            text_set_switchmode_val.setText(switchmodetype[switchmodeindex]);
                            mTvChannelManager.setAtvChannelSwitchMode(switchmodeindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceident:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001000) {
                            if (sourceidentindex == SWITCH_OFF) {
                                sourceidentindex = SWITCH_ON;
                            } else {
                                sourceidentindex = SWITCH_OFF;
                            }
                            text_set_autosourceident_val.setText(sourceidenttype[sourceidentindex]);
                            mTvCommonManager.setSourceIdentState(sourceidentindex);
                            enableChoose(linear_set_autosourceswit,
                                    sourceidentindex == SWITCH_OFF ? false : true);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_sourcepreview:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001011) {
                            if (sourcepreviewindex == SWITCH_OFF) {
                                sourcepreviewindex = SWITCH_ON;
                            } else {
                                sourcepreviewindex = SWITCH_OFF;
                            }
                            text_set_sourcepreview_val
                                    .setText(sourcepreviewtype[sourcepreviewindex]);
                            mTvCommonManager.setSourcePreviewState(sourcepreviewindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_autosourceswit:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00001001) {
                            if (sourceswitindex == SWITCH_OFF) {
                                sourceswitindex = SWITCH_ON;
                            } else {
                                sourceswitindex = SWITCH_OFF;
                            }
                            text_set_autosourceswit_val.setText(sourceswittype[sourceswitindex]);
                            mTvCommonManager.setSourceSwitchState(sourceswitindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_colorrange:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010000) {
                            if (colorrangeindex == 0)
                                colorrangeindex = 2;
                            else
                                colorrangeindex--;
                            text_set_colorrange_val.setText(colorrangetype[colorrangeindex]);
                            tvPictureManager.setColorRange((byte) colorrangeindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_mhlswitch:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00010001) {
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
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_moviemode:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00100000) {
                            if (moviemodeindex == SWITCH_OFF) {
                                moviemodeindex = SWITCH_ON;
                            } else {
                                moviemodeindex = SWITCH_OFF;
                            }
                            text_set_moviemode_val.setText(moviemodetype[moviemodeindex]);
                            tvPictureManager.setFilm(moviemodeindex);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_bluescreenswitch:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000000) {
                            if (bluescreenindex == SWITCH_OFF) {
                                bluescreenindex = SWITCH_ON;
                            } else {
                                bluescreenindex = SWITCH_OFF;
                            }
                            text_set_bluescreenswitch_val.setText(bluescreentype[bluescreenindex]);
                            mTvCommonManager.setBlueScreenMode(bluescreenindex == SWITCH_ON ? true
                                    : false);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmicec:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000001) {
                            if (hdmicecindex == SWITCH_OFF) {
                                hdmicecindex = SWITCH_ON;
                            } else {
                                hdmicecindex = SWITCH_OFF;
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
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_standyon:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x01000011) {
                            if (standyonindex == SWITCH_OFF) {
                                standyonindex = SWITCH_ON;
                            } else {
                                standyonindex = SWITCH_OFF;
                            }
                            hdmicecstatus.autoStandby = (short) standyonindex;
                            text_set_standyon_val.setText(standyontype[standyonindex]);
                            mTvCecManager.setCecConfiguration(hdmicecstatus);
                            ttsSpeakComboButtonVolue();
                        }
                        focusedid = currentid;
                        break;
                    case R.id.linearlayout_set_hdmiarc:
                        if (MainMenuActivity.getInstance().getSettingSelectStatus() == 0x00000101) {
                            if (hdmiarcindex == SWITCH_OFF) {
                                hdmiarcindex = SWITCH_ON;
                            } else {
                                hdmiarcindex = SWITCH_OFF;
                            }
                            text_set_hdmiarc_val.setText(hdmidarctype[hdmiarcindex]);
                            setHdmiArcMode(hdmiarcindex);
                            ttsSpeakComboButtonVolue();
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
                int currentid = settingActivity.getCurrentFocus().getId();
                if (focusedid != currentid)
                    MainMenuActivity.getInstance().setSettingSelectStatus(0x00000000);
                switch (view.getId()) {
                    case R.id.linearlayout_set_pvrfs:
                        intent.setClass(settingActivity, PVROptionActivity.class);
                        settingActivity.startActivity(intent);
                        break;

                    case R.id.linearlayout_set_mts:
                        if (SwitchPageHelper.specificSourceIsInUse(settingActivity, TvCommonManager.INPUT_SOURCE_DTV)) {
                            intent.setClass(settingActivity, AudioLanguageActivity.class);
                            settingActivity.startActivity(intent);
                        }
                        if (SwitchPageHelper.specificSourceIsInUse(settingActivity, TvCommonManager.INPUT_SOURCE_ATV)) {
                            intent.setClass(settingActivity, MTSInfoActivity.class);
                            settingActivity.startActivity(intent);
                        }
                        break;

                    case R.id.linearlayout_set_subtitle:
                       if (TvCommonManager.getInstance().getCurrentTvSystem() != TvCommonManager.TV_SYSTEM_ATSC) {
                           if (SwitchPageHelper.specificSourceIsInUse(settingActivity, TvCommonManager.INPUT_SOURCE_DTV)) {
                               Intent intent = new Intent(settingActivity, SubtitleLanguageActivity.class);
                               settingActivity.startActivity(intent);
                            } else if (SwitchPageHelper.specificSourceIsInUse(settingActivity, TvCommonManager.INPUT_SOURCE_ATV)) {
                                if (TvChannelManager.getInstance().isTeletextSubtitleChannel()) {
                                    if (TvChannelManager.getInstance().isTeletextDisplayed()) {
                                        TvChannelManager.getInstance()
                                            .sendTeletextCommand(TvChannelManager.TTX_COMMAND_SUBTITLE_NAVIGATION);
                                    } else {
                                        TvChannelManager.getInstance()
                                            .openTeletext(TvChannelManager.TTX_MODE_SUBTITLE_NAVIGATION);
                                    }
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(settingActivity);
                                    dialog.setTitle(R.string.str_root_alert_dialog_title)
                                        .setMessage(R.string.str_dtv_source_info_no_subtitle)
                                        .setPositiveButton(R.string.str_root_alert_dialog_confirm,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int arg1) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                }
                            }
                        }
                        break;
                    case R.id.linearlayout_set_powersetting:
                        intent.setClass(settingActivity, PowerSettingActivity.class);
                        settingActivity.startActivity(intent);
                        break;
                    case R.id.linearlayout_set_audio_language1:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00000002);
                        focusedid = R.id.linearlayout_set_audio_language1;
                        linear_set_audio_language1.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_audio_language1.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_audio_language2:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00000003);
                        focusedid = R.id.linearlayout_set_audio_language2;
                        linear_set_audio_language2.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_audio_language2.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_subtitle_language1:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00000002);
                        focusedid = R.id.linearlayout_set_subtitle_language1;
                        linear_set_subtitle_language1.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_subtitle_language1.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_subtitle_language2:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00000003);
                        focusedid = R.id.linearlayout_set_subtitle_language2;
                        linear_set_subtitle_language2.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_subtitle_language2.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_menutime:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00000010);
                        focusedid = R.id.linearlayout_set_menutime;
                        linear_set_menutime.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_menutime.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_switchmode:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00000100);
                        focusedid = R.id.linearlayout_set_switchmode;
                        linear_set_switchmode.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_switchmode.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_hdmiarc:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00000101);
                        focusedid = R.id.linearlayout_set_hdmiarc;
                        linear_set_hdmiarc.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_hdmiarc.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_autosourceident:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00001000);
                        focusedid = R.id.linearlayout_set_autosourceident;
                        linear_set_autosourceident.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_autosourceident.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_sourcepreview:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00001011);
                        focusedid = R.id.linearlayout_set_sourcepreview;
                        linear_set_sourcepreview.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_sourcepreview.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_autosourceswit:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00001001);
                        focusedid = R.id.linearlayout_set_autosourceswit;
                        linear_set_autosourceswit.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_autosourceswit.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_colorrange:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00010000);
                        focusedid = R.id.linearlayout_set_colorrange;
                        linear_set_colorrange.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_colorrange.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_mhlswitch:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00010001);
                        focusedid = R.id.linearlayout_set_mhlswitch;
                        linear_set_mhlswitch.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_mhlswitch.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_moviemode:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x00100000);
                        focusedid = R.id.linearlayout_set_moviemode;
                        linear_set_moviemode.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_moviemode.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_bluescreenswitch:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x01000000);
                        focusedid = R.id.linearlayout_set_bluescreenswitch;
                        linear_set_bluescreenswitch.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_bluescreenswitch.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_hdmicec:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x01000001);
                        focusedid = R.id.linearlayout_set_hdmicec;
                        linear_set_hdmicec.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_hdmicec.getChildAt(3).setVisibility(View.VISIBLE);
                        break;
                    case R.id.linearlayout_set_standyon:
                        MainMenuActivity.getInstance().setSettingSelectStatus(0x01000011);
                        focusedid = R.id.linearlayout_set_standyon;
                        linear_set_standyon.getChildAt(0).setVisibility(View.VISIBLE);
                        linear_set_standyon.getChildAt(3).setVisibility(View.VISIBLE);
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
                                }).show();
                        break;
                    case R.id.linearlayout_location_code:
                        Utility.showLocationCodeInputDialog(settingActivity);
                        break;
                    case R.id.linearlayout_set_restoretodefault:
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
                                        if (TvParentalControlManager.getInstance().isSystemLock()) {
                                            mPasswordLock.show();
                                        } else {
                                            restoreToDefault();
                                        }
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
        linear_set_pvrfs.setOnClickListener(listener);
        linear_set_hdmicec.setOnClickListener(listener);
        linear_set_standyon.setOnClickListener(listener);
        linear_set_cec_list.setOnClickListener(listener);
        linear_set_mts.setOnClickListener(listener);
        linear_set_subtitle.setOnClickListener(listener);
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
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener FocuschangesListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout container = (LinearLayout) v;
                container.getChildAt(0).setVisibility(View.GONE);
                container.getChildAt(3).setVisibility(View.GONE);
                MainMenuActivity.getInstance().setSettingSelectStatus(0x00000000);
            }
        };
        linear_set_hdmicec.setOnFocusChangeListener(FocuschangesListener);
        linear_set_standyon.setOnFocusChangeListener(FocuschangesListener);
        // linear_set_powersetting.setOnFocusChangeListener(FocuschangesListener);
        // linear_set_restoretodefault.setOnFocusChangeListener(FocuschangesListener);
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
        Log.i(TAG, "setHdmiArcMode, type = " + type);
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
            if (tvFactoryManager.restoreToDefault() == true) {
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
                preferencesSettings.edit().clear().commit();

                preferencesSettings = settingActivity.getSharedPreferences(
                        Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
                // Reset Setup Wizard & Location Wizard
                preferencesSettings.edit().clear().commit();

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

        String[] resList = settingActivity.getResources().getStringArray(R.array.str_arr_hdmi_edid_version);
        String[] edidItemList = new String[resList.length + 1];
        System.arraycopy(resList, 0, edidItemList, 0, resList.length);
        mHdmiEdidAutoSwitchIndex = resList.length;
        edidItemList[mHdmiEdidAutoSwitchIndex] = settingActivity.getResources().getString(R.string.str_edid_auto_switch);

        mComboHdmiEdidVersion = new ComboButton(settingActivity, edidItemList,
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
                            int idx = getIdx();
                            if (idx == mHdmiEdidAutoSwitchIndex) {
                                if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_EDID_AUTO_SWITCH)) {
                                    mTvCommonManager.setHdmiEdidAutoSwitch(true);
                                }
                            } else {
                                if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_EDID_AUTO_SWITCH)) {
                                    mTvCommonManager.setHdmiEdidAutoSwitch(false);
                                }
                                if (mTvCommonManager.setHdmiEdidVersionBySource(
                                        inputSource, idx) != true) {
                                    Log.e(TAG, "setHdmiEdidVersion failed!");
                                }
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

        /* Get the supported HDMI EDIE version list */
        boolean bSupportHdmiEdidVersion;
        int focusIndex = mTvCommonManager.getHdmiEdidVersionBySource(inputSource);
        int[] supportHdmiEdidVersionList = mTvCommonManager
                .getHdmiEdidVersionList();
        for (int i = 0; (i < supportHdmiEdidVersionList.length) && (i < resList.length); i++) {
            Log.d(TAG, "supportHdmiEdidVersionList["+i+"] = " + supportHdmiEdidVersionList[i]);
            if (supportHdmiEdidVersionList[i] != TvCommonManager.EDID_VERSION_UNSUPPORT) {
                bSupportHdmiEdidVersion = true;
            } else {
                bSupportHdmiEdidVersion = false;
            }
            mComboHdmiEdidVersion.setItemEnable(i, bSupportHdmiEdidVersion);
        }
        if (mTvCommonManager.isSupportModule(TvCommonManager.MODULE_EDID_AUTO_SWITCH)) {
            mComboHdmiEdidVersion.setItemEnable(mHdmiEdidAutoSwitchIndex, true);
            if (mTvCommonManager.getHdmiEdidAutoSwitch()) {
                focusIndex = mHdmiEdidAutoSwitchIndex;
            }
        } else {
            mComboHdmiEdidVersion.setItemEnable(mHdmiEdidAutoSwitchIndex, false);
        }
        mComboHdmiEdidVersion.setIdx(focusIndex);
    }

    private void ttsSpeakComboButtonVolue() {
        int currentid = settingActivity.getCurrentFocus().getId();
        LinearLayout ll = (LinearLayout) settingActivity.findViewById(currentid);
        if (ll instanceof LinearLayout) {
            final int count = ll.getChildCount();
            if (4 == count) {
                final View textView = ll.getChildAt(2);
                if (textView instanceof TextView) {
                    String str = ((TextView) textView).getText().toString();
                    if (!str.isEmpty()) {
                        TvCommonManager.getInstance().speakTtsDelayed(
                            str
                            , TvCommonManager.TTS_QUEUE_FLUSH
                            , TvCommonManager.TTS_SPEAK_PRIORITY_NORMAL
                            , TvCommonManager.TTS_DELAY_TIME_100MS);
                    }
                }
            }
        }
    }
}
