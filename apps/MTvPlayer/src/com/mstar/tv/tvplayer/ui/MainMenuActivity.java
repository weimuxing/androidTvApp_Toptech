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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvS3DManager.OnS3DCommonEventListener;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvPictureManager.OnVideoStatusEventListener;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tvapi.common.vo.HdrAttribute;
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

public class MainMenuActivity extends MstarBaseActivity implements OnGestureListener {
    private static final String TAG = "MainMenuActivity";

    private int mTvSystem = 0;

    private GestureDetector detector;

    protected ViewFlipper viewFlipper = null;

    protected LayoutInflater lf;

    protected static boolean hasAdd;

    public static int selectedstatusforChannel = 0x00000000;

    protected int currentPage = PICTURE_PAGE;

    public final static int PICTURE_PAGE = 0;

    public final static int SOUND_PAGE = 1;

    public final static int CHANNEL_PAGE = 2;

    public final static int SETTING_PAGE = 3;

    public final static int TIME_PAGE = 4;

    public final static int DEMO_PAGE = 5;

    public final static int S3D_PAGE = 6;

    public final static int OPTION_PAGE = 7;

    public final static int LOCK_PAGE = 8;

    private int mMaxPageIdx = 7;

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

    private LinearLayout curLinearLayout;

    protected static int[] curFocusedViewIds;

    final static int LANGUAGE_CHANGE_MSG = 1080;

    private final int RESUME_MESSAGE = 7758521;

    private boolean needRestartMainMenu = false;

    private boolean onCreatFlag = false;

    protected static boolean bMainMenuFocused = false;

    private int mOptionSelectStatus = 0x00000000;

    private int mParentalControlSelectStatus = 0x00000000;

    private int mSelectedStatusInSetting = 0x00000000;

    private boolean mIsAnimationEnd = true;

    private final int SWIPE_LEFT_ONE_PAGE = -1;

    private final int SWIPE_RIGHT_ONE_PAGE = 1;

    private final int VIDEO_STATUS_UPDATED = 0;

    private final int S3D_UPDATE_3D_INFO = 1;

    private boolean mIsHDREnabled = false;

    private OnVideoStatusEventListener mVideoStatusEventListeners = null;

    private S3DCommonEventListener mS3DCommonEventListener = null;

    public static MainMenuActivity getInstance() {
        return mainMenuActivity;
    }

    private class VideoStatusEventListener implements OnVideoStatusEventListener {
        @Override
        public boolean onVideoStatusEvent(int what, int arg1, int arg2, Object obj) {
            Log.d(TAG, "onVideoStatusEvent, arg2 = " + arg2);
            switch (what) {
                case TvPictureManager.TVPICTURE_EVENT_VIDEO_STATUS:
                    Message m = Message.obtain();
                    m.what = VIDEO_STATUS_UPDATED;
                    m.arg1 = arg1;
                    m.arg2 = arg2;
                    mVidoeEvantHandler.sendMessage(m);
                break;
            }
            return true;
        }
    }

    private class S3DCommonEventListener implements OnS3DCommonEventListener {
        @Override
        public boolean onEvent(int what, int arg1, int arg2, Object obj) {
            Log.d(TAG, "onEvent, arg1 = " + arg1);
            switch (what) {
                case TvS3DManager.THREED_COMMON_UPDATE_3D_INFO:
                    Message m = Message.obtain();
                    m.what = S3D_UPDATE_3D_INFO;
                    m.arg1 = arg1;
                    m.arg2 = arg2;
                    mVidoeEvantHandler.sendMessage(m);
                    break;
            }
            return true;
        }
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

    protected Handler mVidoeEvantHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == VIDEO_STATUS_UPDATED) {
                if (msg.arg2 == TvPictureManager.VIDEO_VIDEO_HDR_STOP) {
                    mIsHDREnabled = false;
                } else {
                    mIsHDREnabled = true;
                }
                if (menuOf3DViewHolder != null) {
                    menuOf3DViewHolder.refreshUI(mIsHDREnabled);
                }
            } else if (msg.what == S3D_UPDATE_3D_INFO) {
                if (menuOf3DViewHolder != null) {
                    menuOf3DViewHolder.refreshUI(mIsHDREnabled);
                }
            }
        };
    };

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (timeViewHolder != null) {
                timeViewHolder.toHandleMsg(msg);
            }
            if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
                Intent intent = new Intent(MainMenuActivity.this, RootActivity.class);
                startActivity(intent);
            }
            if (msg.what == LittleDownTimer.SELECT_RETURN_MSG) {
                if (!(getCurrentFocus() instanceof LinearLayout))
                    return;
                curLinearLayout = (LinearLayout) getCurrentFocus();
                if (curLinearLayout != null) {
                    if (selectedstatusforChannel != 0x00000000
                            && mSelectedStatusInSetting != 0x00000000
                            && mOptionSelectStatus != 0x00000000
                            && mParentalControlSelectStatus != 0x00000000) {
                        selectedstatusforChannel = 0x00000000;
                        mSelectedStatusInSetting = 0x00000000;
                        mOptionSelectStatus = 0x00000000;
                        mParentalControlSelectStatus = 0x00000000;
                    }
                    curLinearLayout.clearFocus();
                    curLinearLayout.requestFocus();
                    curLinearLayout.setSelected(false);
                }
            }
            if (msg.what == LANGUAGE_CHANGE_MSG) {
                restoreCurFocus();
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
            }
            if (msg.what == RESUME_MESSAGE) {
                {
                    if ((getIntent() != null) && (getIntent().getExtras() != null)) {
                        currentPage = getIntent().getIntExtra("currentPage", PICTURE_PAGE);
                        keyInjection(getIntent().getIntExtra("currentKeyCode", 0));
                    }
                    if (onCreatFlag) {
                        addCurrentView(currentPage);
                        initUIComponent(currentPage);
                        selectedstatusforChannel = 0x00000000;
                        mSelectedStatusInSetting = 0x00000000;
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
                    }
                }
                if (onCreatFlag) {
                    loadFocusDataFromSys();
                    LittleDownTimer.setHandler(handler);
                    onCreatFlag = false;
                }
            }
        };
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        mTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
            mMaxPageIdx = 7; // Only 7 Pages for ATSC System
        } else {
            mMaxPageIdx = 8; // Additional Page for Parental Control
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

        if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OPEN_HDR)) {
            HdrAttribute openHdrAtt = TvPictureManager.getInstance().getHdrAttributes(TvPictureManager.HDR_OPEN_ATTRIBUTES
                , TvPictureManager.VIDEO_MAIN_WINDOW);
            if (true == openHdrAtt.isRunning) {
                mIsHDREnabled = true;
            }
        }
        if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_DOLBY_HDR)) {
            HdrAttribute dolbyHdrAtt = TvPictureManager.getInstance().getHdrAttributes(TvPictureManager.HDR_DOLBY_ATTRIBUTES
                , TvPictureManager.VIDEO_MAIN_WINDOW);
            if (true == dolbyHdrAtt.isRunning) {
                mIsHDREnabled = true;
            }
        }
        mVideoStatusEventListeners = new VideoStatusEventListener();
        TvPictureManager.getInstance().registerOnVideoStatusEventListener(mVideoStatusEventListeners);
        mS3DCommonEventListener = new S3DCommonEventListener();
        TvS3DManager.getInstance().registerOnS3DCommonEventListener(mS3DCommonEventListener);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LinearLayout ll = (LinearLayout) findViewById(R.id.MainMenu);
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.x = 0;
        lp.y = 0;
        lp.width = ll.getLayoutParams().width;
        lp.height = ll.getLayoutParams().height;
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (TvCecManager.getInstance().getCecConfiguration().cecStatus == Constant.CEC_STATUS_ON) {
            if (Tools.isBox()) {
                TvCecManager.getInstance().enableDeviceMenu();
            } else {
                TvCecManager.getInstance().disableDeviceMenu();
            }
        }
        if (RootActivity.mExitDialog != null) {
            if (RootActivity.mExitDialog.isShowing()) {
                RootActivity.mExitDialog.dismiss();
            }
        }
        bMainMenuFocused = true;

        lf = LayoutInflater.from(MainMenuActivity.this);
        handler.sendEmptyMessage(RESUME_MESSAGE);
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
            case 0:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.picture, null), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.sound, null), 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.channel, null), 2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    View viewSetting = lf.inflate(R.layout.setting, null);
                    TvCommonManager tvCommonManager = TvCommonManager.getInstance();
                    LinearLayout linearlayoutSetting = (LinearLayout) viewSetting
                            .findViewById(R.id.linearlayout_setting);
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
                    viewFlipper.addView(viewSetting, 3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.time, null), 4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.demo, null), 5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.menu_of_3d, null), 6);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.option, null), 7);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    viewFlipper.addView(lf.inflate(R.layout.menu_parentalcontrol, null), 8);
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


    @Override
    protected void onPause() {
        try {
            LittleDownTimer.pauseMenu();
            LittleDownTimer.pauseItem();
            bMainMenuFocused = false;
            SharedPreferences settings = this.getSharedPreferences(Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
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
        if (Tools.isBox()) {
            if (TvCecManager.getInstance().getCecConfiguration().cecStatus == Constant.CEC_STATUS_ON) {
                TvCecManager.getInstance().disableDeviceMenu();
            }
        }
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

    private boolean isSourceInTv() {
        int curis = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (curis == TvCommonManager.INPUT_SOURCE_ATV || curis == TvCommonManager.INPUT_SOURCE_DTV) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag;
        SharedPreferences settings;

        // If MapKeyPadToIR returns true,the previous keycode has been changed
        // to responding
        // android d_pad keys,just return.
        if (MapKeyPadToIR(keyCode, event))
            return true;

        if (selectedstatusforChannel == 0x00000000 && mSelectedStatusInSetting == 0x00000000
                && mOptionSelectStatus == 0x00000000 && mParentalControlSelectStatus == 0x00000000) {
            Intent intent;
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    Intent intentRoot = new Intent(this, RootActivity.class);
                    startActivity(intentRoot);
                    break;
                case KeyEvent.KEYCODE_TV_INPUT:
                    needRestartMainMenu = true;
                    Intent intentSource = new Intent(
                            "com.mstar.tvsetting.switchinputsource.intent.action.PictrueChangeActivity");
                    startActivity(intentSource);
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (swipePage(SWIPE_LEFT_ONE_PAGE)) {
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (swipePage(SWIPE_RIGHT_ONE_PAGE)) {
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_M:
                    settings = this.getSharedPreferences(Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
                    flag = settings.getBoolean("_3Dflag", false);
                    if (flag) {
                        intent = new Intent(this, MainMenu3DActivity.class);
                        startActivity(intent);
                    }
                    return true;
            }
        }
        if (currentPage == CHANNEL_PAGE) {
            menuOfChannelViewHolder.onKeyDown(keyCode, event);
        }
        if (currentPage == SETTING_PAGE) {
            menuOfSettingViewHolder.onKeyDown(keyCode, event);
        }
        if (currentPage == DEMO_PAGE) {
            menuOfDemoViewHolder.onKeyDown(keyCode, event);
        }
        if (currentPage == OPTION_PAGE) {
            menuOfOptionViewHolder.onKeyDown(keyCode, event);
        }
        if (currentPage == LOCK_PAGE) {
            menuOfParentalControlViewHolder.onKeyDown(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
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

    protected void initUIComponent(int page) {
        viewFlipper.setDisplayedChild(page);
        TvCommonManager.getInstance().stopTts();
        switch (page) {
            case PICTURE_PAGE:
                if (pictureViewHolder == null) {
                    pictureViewHolder = new PictureViewHolder(MainMenuActivity.this);
                    pictureViewHolder.findViews();
                    pictureViewHolder.LoadDataToUI();
                    TvCommonManager.getInstance().speakTtsDelayed(
                        getString(R.string.str_mainmenu_menu)
                        , TvCommonManager.TTS_QUEUE_FLUSH
                        , TvCommonManager.TTS_SPEAK_PRIORITY_HIGH
                        , TvCommonManager.TTS_DELAY_TIME_NO_DELAY);
                }
                pictureViewHolder.pageOnFocus();
                break;
            case SOUND_PAGE:
                if (soundViewHolder == null) {
                    soundViewHolder = new SoundViewHolder(MainMenuActivity.this);
                    soundViewHolder.findViews();
                    soundViewHolder.LoadDataToUI();
                }
                soundViewHolder.pageOnFocus();
                break;
            case S3D_PAGE:
                if (menuOf3DViewHolder == null) {
                    menuOf3DViewHolder = new MenuOf3DViewHolder(MainMenuActivity.this);
                    menuOf3DViewHolder.findViews();
                    menuOf3DViewHolder.LoadDataToUI();
                    if (true == mIsHDREnabled) {
                        menuOf3DViewHolder.refreshUI(mIsHDREnabled);
                    }
                }
                menuOf3DViewHolder.pageOnFocus();
                break;
            case TIME_PAGE:
                if (timeViewHolder == null) {
                    timeViewHolder = new TimeViewHolder(MainMenuActivity.this, handler);
                    timeViewHolder.findViews();
                    timeViewHolder.loadDataToUI();
                }
                timeViewHolder.loadDataToMyBtnOffTime();
                timeViewHolder.loadDataToMyBtnScheduledTime();
                timeViewHolder.pageOnFocus();
                break;
            case CHANNEL_PAGE:
                if (menuOfChannelViewHolder == null) {
                    menuOfChannelViewHolder = new ChannelViewHolder(MainMenuActivity.this);
                    menuOfChannelViewHolder.findViews();
                }
                menuOfChannelViewHolder.updateUi();
                menuOfChannelViewHolder.pageOnFocus();
                break;
            case SETTING_PAGE:
                if (menuOfSettingViewHolder == null) {
                    menuOfSettingViewHolder = new SettingViewHolder(MainMenuActivity.this, handler);
                    menuOfSettingViewHolder.findViews();
                }
                menuOfSettingViewHolder.pageOnFocus();
                break;
            case DEMO_PAGE:
                if (menuOfDemoViewHolder == null) {
                    menuOfDemoViewHolder = new DemoViewHolder(MainMenuActivity.this);
                    menuOfDemoViewHolder.findViews();
                }
                menuOfDemoViewHolder.pageOnFocus();
                break;
            case OPTION_PAGE:
                if (menuOfOptionViewHolder == null) {
                    menuOfOptionViewHolder = new OptionViewHolder(MainMenuActivity.this);
                    menuOfOptionViewHolder.findViews();
                    menuOfOptionViewHolder.loadDataToUi();
                }
                menuOfOptionViewHolder.pageOnFocus();
                break;
            case LOCK_PAGE:
                if (menuOfParentalControlViewHolder == null) {
                    menuOfParentalControlViewHolder = new ParentalControlViewHolder(
                            MainMenuActivity.this);
                    menuOfParentalControlViewHolder.findViews();
                }
                boolean bIsParentalControlPermitted = false;
                if ((getIntent() != null) && (getIntent().getExtras() != null)) {
                    bIsParentalControlPermitted = getIntent().getBooleanExtra(Constant.PARENTAL_CONTROL_MENU_PERMITTED, false);
                }
                menuOfParentalControlViewHolder.setControlPermitted(bIsParentalControlPermitted);
                menuOfParentalControlViewHolder.updateUi();
                break;
        }
        initCurFocus();
    }

    @Override
    public void onDestroy() {
        if (timeViewHolder != null) {
            timeViewHolder.endUIClock();
        }
        if (needRestartMainMenu) {
            needRestartMainMenu = false;
        }

        TvPictureManager.getInstance().unregisterOnVideoStatusEventListener(mVideoStatusEventListeners);
        if (null != mS3DCommonEventListener) {
            TvS3DManager.getInstance().unregisterOnS3DCommonEventListener(
                    mS3DCommonEventListener);
            mS3DCommonEventListener = null;
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
        SharedPreferences.Editor editor = getSharedPreferences(Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE).edit();
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
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
        for (int i = 0; i < curFocusedViewIds.length; i++) {
            curFocusedViewIds[i] = sharedPreferences.getInt("page" + i, 0);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() < -100) {
            swipePage(SWIPE_LEFT_ONE_PAGE);
            return true;
        } else if (e1.getX() - e2.getX() > 100) {
            swipePage(SWIPE_RIGHT_ONE_PAGE);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
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

    private boolean swipePage(int pageOffset) {
        if (mIsAnimationEnd == false) {
            return false;
        }

        // Count page number since the index is zero-based
        final int nPages = mMaxPageIdx + 1;

        // Calculate the remainder to move to next (pageOffset) pages
        // This makes pageOffset between  ( -nPages + 1)  to (nPages - 1)
        pageOffset %= nPages;

        // No actually page move
        if (0 == pageOffset) {
            return false;
        }

        addOtherView();

        recordCurFocusViewId();
        SharedPreferences settings = this.getSharedPreferences(Constant.PREFERENCES_TV_SETTING,
                Context.MODE_PRIVATE);
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
                this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.right_out));
            } else {
                // Go right
                animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
                this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.left_out));
            }
            animationFadeIn.setAnimationListener(mAnimationListener);
            this.viewFlipper.setInAnimation(animationFadeIn);

            initUIComponent(currentPage);
        }
        return true;
    }
}
