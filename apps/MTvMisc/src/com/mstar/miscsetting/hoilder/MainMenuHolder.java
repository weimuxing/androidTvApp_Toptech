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

package com.mstar.miscsetting.hoilder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mstar.android.MDisplay;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureDeviceType;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureSource;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.miscsetting.activity.MainActivity;
import com.mstar.miscsetting.constants.PipContants;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog.MISCMODE;
import com.mstar.miscsetting.dialog.ChoosePipOrPopDialog.OSDTHREEDMODE;
import com.mstar.miscsetting.dialog.OtherSettingDialog;
import com.mstar.miscsetting.dialog.DisplaySettingDialog;
import com.mstar.miscsetting.dialog.PipSubWindowInputSourceDialog;
import com.mstar.miscsetting.dialog.PipSubWindowInputSourceDialog.PIP_MODE_ENABLED;
import com.mstar.miscsetting.dialog.PipSubWindowSwitchVolDialog.SWITCH_VOL;
import com.mstar.miscsetting.R;
import com.mstar.miscsetting.service.PipService;
import com.mstar.util.Tools;

public class MainMenuHolder {

    private static final String TAG = "MainMenuHolder";

    private Activity mContext;

    /* Page that was passed to jump the logo */
    public static String mFlag;

    public static MISCMODE miscmode = MISCMODE.MODE_NUM;

    /* Button is response */
    public boolean keyResponse = false;

    /* Judge whether to move up, down, left and right */
    private boolean turnUp = true;

    private boolean turnRight = true;

    private boolean turnDown = true;

    private boolean turnLeft = true;

    public static boolean isHome = false;

    /* To the up and down or so mobile distance */
    private int moveUpDistance = 0;

    private int moveRightDistance = 0;

    private int moveDownDistance = 0;

    private int moveLeftDistance = 0;

    /* fix Issue 351872, disable pop & dual wile started from MM or Browser */
    public static boolean isMM = false;

    public static boolean isBrowser = false;

    public static boolean isMMPlay = false;

    private static TvPipPopManager tvPipPopManager = null;

    public static TvCommonManager tvCommonManager = null;

    public static int mMainInputSrc = TvCommonManager.INPUT_SOURCE_NONE;

    public static VideoWindowType dispWin = null;

    public static VideoWindowType mainWin = null;

    public static VideoWindowType subWin = null;

    public ChoosePipOrPopDialog pipOrPopDialog = null;

    public OtherSettingDialog otherSettingDialog = null;

    public DisplaySettingDialog mDisplaySettingDialog = null;

    private Runnable runDisablePip = new Runnable() {

        @Override
        public void run() {
            tvPipPopManager.disablePIP(TvPipPopManager.E_MAIN_WINDOW);
            tvPipPopManager.setPipOnFlag(false);
            switchVol2Main();
        }
    };

    private Thread mthreadDisablePip = new Thread(runDisablePip);

    private Runnable runDisablePop = new Runnable() {

        @Override
        public void run() {
            tvPipPopManager.disablePOP(TvPipPopManager.E_MAIN_WINDOW);
            switchVol2Main();
        }
    };

    private Thread mthreadDisablePop = new Thread(runDisablePop);

    private Runnable runDisableDualView = new Runnable() {

        @Override
        public void run() {
            tvPipPopManager.disable3dDualView();
            MDisplay.set3DDisplayMode(OSDTHREEDMODE.DISPLAYMODE_NORMAL.ordinal());
            switchVol2Main();
        }
    };

    private Thread mthreadDisableDualView = new Thread(runDisableDualView);

    SharedPreferences sp;

    public MainMenuHolder(Activity context, int style, String flag) {
        mContext = context;
        mFlag = flag;
        tvPipPopManager = TvPipPopManager.getInstance();
        tvCommonManager = TvCommonManager.getInstance();
        sp = mContext.getSharedPreferences(TAG, 0);
    }

    /**
     * Initialization control
     */
    public void findView() {

        MenuViewHolder.menuBgRelativeLayout = (RelativeLayout) mContext
                .findViewById(R.id.menuBgRelativeLayout);
        MenuViewHolder.menuFoucsImage = (ImageView) mContext.findViewById(R.id.menuFocusImage);
        MenuViewHolder.rightImage = (ImageView) mContext.findViewById(R.id.rightImage);
        MenuViewHolder.leftImage = (ImageView) mContext.findViewById(R.id.leftImage);
        MenuViewHolder.upImage = (ImageView) mContext.findViewById(R.id.upImage);
        MenuViewHolder.downImage = (ImageView) mContext.findViewById(R.id.downImage);
        MenuViewHolder.upText = (TextView) mContext.findViewById(R.id.upText);
        MenuViewHolder.rightText = (TextView) mContext.findViewById(R.id.rightText);
        MenuViewHolder.leftText = (TextView) mContext.findViewById(R.id.leftText);
        MenuViewHolder.downText = (TextView) mContext.findViewById(R.id.downText);
        Log.i(TAG, "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Log.i(TAG, "findView-->mFlag:" + mFlag);
        initMenuData();
        MenuViewHolder.bigToSmall = AnimationUtils.loadAnimation(mContext, R.anim.big_to_small);
        MenuViewHolder.fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        MenuViewHolder.fadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        MenuViewHolder.currentToUp = AnimationUtils.loadAnimation(mContext, R.anim.current_to_up);
        MenuViewHolder.rotateFadeIn = AnimationUtils.loadAnimation(mContext,
                R.anim.since_rotate_and_fade_in);
        MenuViewHolder.layoutFadeIn = AnimationUtils.loadLayoutAnimation(mContext,
                R.anim.layout_fade_in);
        MenuViewHolder.layoutFadeOut = AnimationUtils.loadLayoutAnimation(mContext,
                R.anim.layout_fade_out);
    }

    public static void setMiscMode(MISCMODE miscMode) {
        miscmode = miscMode;
    }

    public static MISCMODE getMiscMode() {
        return miscmode;
    }

    /**
     * Initialization menu
     */
    private void initMenuData() {

        if ("MENU".equals(mFlag) || "PIP_MENU".equals(mFlag)) {
            initStairMenu();
        }
        if ("PIP_VVOIP".equals(mFlag)) {

            MenuViewHolder.leftText
                    .setText(mContext.getResources().getString(R.string.volumeSmall));
            MenuViewHolder.rightText.setText(mContext.getResources().getString(R.string.volumeBig));
            MenuViewHolder.upText.setText(mContext.getResources()
                    .getString(R.string.fullScreenCall));
            MenuViewHolder.downText.setText(mContext.getResources().getString(R.string.exit));
        }
    }

    /**
     * Initialization level 1 menu
     */
    private void initStairMenu() {
        MenuViewHolder.leftText.setText(mContext.getResources().getString(R.string.exit));
        if (tvCommonManager.isSupportModule(TvCommonManager.MODULE_PIP)) {
            MenuViewHolder.upText.setText(mContext.getResources().getString(R.string.pipOrpop));
        }

        MenuViewHolder.rightText.setText(mContext.getResources().getString(R.string.other));

        if (Tools.isBox()) {
            MenuViewHolder.downText.setText(mContext.getResources().getString(R.string.display));
        }
    }

    /**
     * Initialization animation
     */
    public void initAnimation() {
        keyResponse = false;
        MenuViewHolder.menuBgRelativeLayout.startAnimation(MenuViewHolder.rotateFadeIn);
        MenuViewHolder.rotateFadeIn.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setAllVisible();
                keyResponse = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Picture menu cursor flashing Handler
     */
    Handler menuFoucsImageFlickerHandler = new Handler() {

        int i = 0;

        @Override
        public void handleMessage(final Message msg) {

            /* Deal with the disc focus animation */
            if (msg.what == 0) {
                i++;
                MenuViewHolder.menuFoucsImage.startAnimation(MenuViewHolder.fadeIn);
                MenuViewHolder.fadeIn.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (i > 1) {
                            keyResponse = true;
                        } else {
                            Message msg = menuFoucsImageFlickerHandler.obtainMessage();
                            msg.what = 0;
                            menuFoucsImageFlickerHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                });
            }

        }
    };

    /**
     * Up animation
     */
    protected void currentToUpAnimation() {
        if (moveUpDistance == 0) {
            moveUpDistance = (MenuViewHolder.upImage.getTop() - MenuViewHolder.downImage
                    .getBottom())
                    / 2
                    - (MenuViewHolder.upText.getBottom() - MenuViewHolder.menuFoucsImage.getTop())
                    / 2;
        }

        TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, moveUpDistance);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        anim.setFillBefore(true);
        keyResponse = false;
        MenuViewHolder.menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                keyResponse = true;
                MenuViewHolder.upText.startAnimation(MenuViewHolder.bigToSmall);
                MenuViewHolder.bigToSmall.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                pipOrPopDialog = new ChoosePipOrPopDialog(mContext, R.style.mainMenu);
                pipOrPopDialog.show();
                Log.i(TAG, "enter choose pip or pop");
            }

        });

    }

    /**
     * Right animation
     */
    protected void currentToRightAnimation() {
        if (moveRightDistance == 0) {
            moveRightDistance = (MenuViewHolder.rightImage.getRight() - MenuViewHolder.leftImage
                    .getLeft())
                    / 2
                    - (MenuViewHolder.rightText.getRight() - MenuViewHolder.menuFoucsImage
                            .getRight()) / 2;
        }

        TranslateAnimation anim = new TranslateAnimation(0.0f, moveRightDistance, 0.0f, 0.0f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        anim.setFillBefore(true);
        // anim.setFillAfter(true);
        keyResponse = false;
        MenuViewHolder.menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                keyResponse = true;
                MenuViewHolder.rightText.startAnimation(MenuViewHolder.bigToSmall);
                MenuViewHolder.bigToSmall.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }
                });
                otherSettingDialog = new OtherSettingDialog(mContext, R.style.mainMenu);
                otherSettingDialog.show();
            }
        });
    }

    /**
     * Down animation
     */
    protected void currentToDownAnimation() {
        if (moveDownDistance == 0) {
            moveDownDistance = (MenuViewHolder.downImage.getBottom() - MenuViewHolder.upImage
                    .getTop())
                    / 2
                    - (MenuViewHolder.downText.getTop() - MenuViewHolder.menuFoucsImage.getBottom())
                    / 2;
        }

        TranslateAnimation anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, moveDownDistance);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        anim.setFillBefore(true);
        keyResponse = false;
        MenuViewHolder.menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                keyResponse = true;
                MenuViewHolder.downText.startAnimation(MenuViewHolder.bigToSmall);
                MenuViewHolder.bigToSmall.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }
                });

                mDisplaySettingDialog = new DisplaySettingDialog(mContext, R.style.mainMenu);
                mDisplaySettingDialog.show();

            }
        });
    }

    private void switchVol2Main() {
        if (PipService.volStatus != SWITCH_VOL.BT_HEADSET_MAIN) {
            final int speakerVolume = TvAudioManager.getInstance().getSpeakerVolume();
            Log.i(TAG, "reset earphone volumn as speaker:" + speakerVolume);
            TvAudioManager.getInstance().setEarPhoneVolume(speakerVolume, false);
            TvAudioManager.getInstance().setAudioCaptureSource(
                    TvAudioManager.AUDIO_CAPTURE_DEVICE_TYPE_DEVICE1,
                    TvAudioManager.AUDIO_CAPTURE_SOURCE_MAIN_SOUND);
            PipService.volStatus = SWITCH_VOL.BT_HEADSET_MAIN;
        }
    }

    public void disablePipPop(int scaleWindow) {
        if (tvPipPopManager != null) {
            PipSubWindowInputSourceDialog.pipModeEnabled = PIP_MODE_ENABLED.NUM_EANBLED;
            resetMHL();
            switch (miscmode) {
                case PIP_MODE:
                    switchVol2Main();
                    tvPipPopManager.disablePIP(scaleWindow);
                    tvPipPopManager.setPipOnFlag(false);
                    break;
                case POP_MODE:
                    switchVol2Main();
                    tvPipPopManager.disablePOP(scaleWindow);
                    break;
                case DUAL_MODE:
                    switchVol2Main();
                    tvPipPopManager.disable3dDualView();
                    MDisplay.set3DDisplayMode(OSDTHREEDMODE.DISPLAYMODE_NORMAL.ordinal());
                    break;
                default:
                    break;

            }
        }
    }

    public void finishMainActivity() {
        if (mContext != null)
            mContext.finish();
    }

    /**
     * Left animation
     */
    protected void currentToLeftAnimation() {
        if (moveLeftDistance == 0) {
            moveLeftDistance = (MenuViewHolder.leftImage.getLeft() - MenuViewHolder.rightImage
                    .getRight())
                    / 2
                    - (MenuViewHolder.leftText.getLeft() - MenuViewHolder.menuFoucsImage.getLeft())
                    / 2;
        }

        TranslateAnimation anim = new TranslateAnimation(0.0f, moveLeftDistance, 0.0f, 0.0f);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        anim.setFillBefore(true);
        keyResponse = false;
        MenuViewHolder.menuFoucsImage.startAnimation(anim);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                keyResponse = true;
                MenuViewHolder.leftText.startAnimation(MenuViewHolder.bigToSmall);
                MenuViewHolder.bigToSmall.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }
                });

                if (tvPipPopManager != null) {
                    PipSubWindowInputSourceDialog.pipModeEnabled = PIP_MODE_ENABLED.NUM_EANBLED;
                    PipSubWindowInputSourceDialog.subInputSource = TvCommonManager.INPUT_SOURCE_NONE;
                    if (tvPipPopManager.isPipModeEnabled()) {
                        resetMHL();
                        switch (miscmode) {
                            case PIP_MODE:
                                mthreadDisablePip.start();
                                Tools.toastShow(R.string.switch_main, mContext);
                                break;
                            case POP_MODE:
                                mthreadDisablePop.start();
                                Tools.toastShow(R.string.switch_main, mContext);
                                break;
                            case DUAL_MODE:
                                mthreadDisableDualView.start();
                                Tools.toastShow(R.string.switch_main, mContext);
                                break;
                            default:
                                break;

                        }
                    }
                }

                mContext.finish();
            }
        });
    }

    /**
     * Set the visibility of components
     */
    protected void setAllVisible() {
        Log.i(TAG, "mFlag:" + mFlag);

        MenuViewHolder.menuFoucsImage.setVisibility(View.VISIBLE);

        if ("MENU".equals(mFlag) || "PIP_MENU".equals(mFlag) || ("PIP_VVOIP".equals(mFlag))) {

            MenuViewHolder.rightText.setVisibility(View.VISIBLE);
            MenuViewHolder.leftText.setVisibility(View.VISIBLE);
            MenuViewHolder.upText.setVisibility(View.VISIBLE);

            MenuViewHolder.rightImage.setVisibility(View.VISIBLE);
            MenuViewHolder.leftImage.setVisibility(View.VISIBLE);
            if (tvCommonManager.isSupportModule(TvCommonManager.MODULE_PIP)) {
                MenuViewHolder.upImage.setVisibility(View.VISIBLE);
            }
        }

        if (Tools.isBox()) {
            MenuViewHolder.downText.setVisibility(View.VISIBLE);
            MenuViewHolder.downImage.setVisibility(View.VISIBLE);
        } else {
            MenuViewHolder.downText.setVisibility(View.GONE);
            MenuViewHolder.downImage.setVisibility(View.GONE);
        }
    }

    /**
     * Press the "left" or to "right" key
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean processLeftOrRightKey(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (turnLeft) {
                currentToLeftAnimation();
            }
        } else {
            if (turnRight) {
                currentToRightAnimation();
            }
        }

        return true;
    }

    /**
     * Press the "up" or to "down" key
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean processUpOrDownKey(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (turnUp) {
                if (tvCommonManager.isSupportModule(TvCommonManager.MODULE_PIP)) {
                    currentToUpAnimation();
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (turnDown) {
                if (Tools.isBox()) {
                    currentToDownAnimation();
                }
            }
        }
        return true;
    }

    /* the two functions below help to avoid auto switch of mhl. */
    public void closeMHL() {
        if (TvMhlManager.getInstance().getAutoSwitch()) {
            TvMhlManager.getInstance().setAutoSwitch(false);
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("needTriggerMHL", true);
            edit.commit();
        }
    }

    public void resetMHL() {
        if (sp.getBoolean("needTriggerMHL", false)) {
            TvMhlManager.getInstance().setAutoSwitch(true);
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("needTriggerMHL", false);
            edit.commit();
        }
    }
}
