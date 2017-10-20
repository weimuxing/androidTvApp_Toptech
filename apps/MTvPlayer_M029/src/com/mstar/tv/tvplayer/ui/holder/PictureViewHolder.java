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
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.tv.tvplayer.ui.TVRootApp;
import com.mstar.tv.tvplayer.ui.R;
import com.mstar.tv.tvplayer.ui.component.ComboButton;
import com.mstar.tv.tvplayer.ui.component.MyButton;
import com.mstar.tv.tvplayer.ui.component.SeekBarButton;
import com.mstar.tv.tvplayer.ui.settings.PCImageModeDialogActivity;
import com.mstar.android.tvapi.common.vo.ColorTemperatureExData;
import com.mstar.android.tvapi.common.vo.EnumVideoArcType;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.util.Tools;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.tv.tvplayer.ui.TVRootApp;

public class PictureViewHolder {
    private static final int INVALID_PICTURE_MODE = 111;

    private static final int VALID_PICTURE_MODE = 222;

    private static final int XVYCC_NORMAL_MAIN = 0;

    private static final int XVYCC_ON_XVYCC_MAIN = 1;

    private static final int XVYCC_ON_SRGB_MAIN = 2;

    private int mArcType = TvPictureManager.VIDEO_ARC_MAX;

    protected static final int STEP = 1;

    protected static final String TAG = "PictureViewHolder";

    private int mPictureMode = TvPictureManager.PICTURE_MODE_NORMAL;

    protected ProgressDialog progressDialog;

    protected ComboButton comboBtnPictureMode;

    protected SeekBarButton seekBtnContrast;

    protected SeekBarButton seekBtnBrightness;

    protected SeekBarButton seekBtnSharpness;

    protected SeekBarButton seekBtnSaturation;

    protected SeekBarButton seekBtnHue;

    protected SeekBarButton seekBtnBacklight;

    protected ComboButton comboBtnColorTemperature;

    protected SeekBarButton seekBtnColorRed;

    protected SeekBarButton seekBtnColorGreen;

    protected SeekBarButton seekBtnColorBlue;

    protected ComboButton comboBtnZoomMode;

    protected ComboButton comboBtnImageNoiseReduction;

    protected ComboButton comboBtnMPEGNoiseReduction;

    protected ComboButton comboBtnxvYCC;

    protected MyButton myButtonPcImageMode;

    protected LinearLayout layoutMenu;

    protected Activity activity;

    private MyHandler myHandler = null;

    private short OFF = 0;

    private short ON = 1;
	private boolean isSignalStable = false; //zb20160309 add
	private boolean isSupportZoom = false;
	
    public PictureViewHolder(Activity act) {
        this.activity = act;
    }

    public void findViews() {
        layoutMenu = (LinearLayout) activity
                .findViewById(R.id.linearlayout_pic_menu);
        myHandler = new MyHandler();
        seekBtnContrast = new SeekBarButton(activity,
                R.id.linearlayout_pic_contrast, STEP, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setVideoItem(
                            TvPictureManager.PICTURE_CONTRAST,
                            (int) seekBtnContrast.getProgress());
                }
            }
        };
        seekBtnBrightness = new SeekBarButton(activity,
                R.id.linearlayout_pic_brightness, STEP, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setVideoItem(
                            TvPictureManager.PICTURE_BRIGHTNESS,
                            (int) seekBtnBrightness.getProgress());
                }
            }
        };
        seekBtnSharpness = new SeekBarButton(activity,
                R.id.linearlayout_pic_sharpness, STEP, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setVideoItem(
                            TvPictureManager.PICTURE_SHARPNESS,
                            (int) seekBtnSharpness.getProgress());
                }
            }
        };
        seekBtnHue = new SeekBarButton(activity, R.id.linearlayout_pic_hue,
                STEP, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setVideoItem(
                            TvPictureManager.PICTURE_HUE,
                            (int) seekBtnHue.getProgress());
                }
            }
        };
        seekBtnSaturation = new SeekBarButton(activity,
                R.id.linearlayout_pic_saturation, STEP, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setVideoItem(
                            TvPictureManager.PICTURE_SATURATION,
                            (int) seekBtnSaturation.getProgress());
                }
            }
        };
        seekBtnBacklight = new SeekBarButton(activity,
                R.id.linearlayout_pic_backlight, STEP, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setBacklight(
                            seekBtnBacklight.getProgress());
                }
            }
        };
        comboBtnPictureMode = new ComboButton(activity, activity.getResources()
                .getStringArray(getApp().isCusOnida() ? R.array.str_arr_pic_picturemode_vals_onida:R.array.str_arr_pic_picturemode_vals),
                R.id.linearlayout_pic_picturemode, 1, 2,
                ComboButton.NEED_SELECTED_BEFORE_SWITCH, ComboButton.FLAG_RUN_IN_NEW_THREAD) {
            @Override
            public void doUpdate() {
                super.doUpdate();
                int specifyPicMode = TvPictureManager.PICTURE_MODE_NORMAL;
                int prePicMode = TvPictureManager.getInstance().getPictureMode();
                if (getIdx() != TvPictureManager.PICTURE_MODE_USER) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            SetFocusableForUserMode();
                        }
                    });
                }

                if (TvPictureManager.getInstance() != null) {
                    specifyPicMode = comboBtnPictureMode.getIdx();
                    TvPictureManager.getInstance().setPictureMode(
                            comboBtnPictureMode.getIdx());
                     //do bee
                if(SystemProperties.getBoolean("ro.onida.enablebee", false)){
                    if ((prePicMode ==TvPictureManager.PICTURE_MODE_SPORTS ||prePicMode ==TvPictureManager.PICTURE_MODE_VIVID) 
                    		&&comboBtnPictureMode.getIdx() == TvPictureManager.PICTURE_MODE_NATURAL/*BEE*/||
                    prePicMode == TvPictureManager.PICTURE_MODE_NATURAL) {
			// bee 
                    activity.sendBroadcast(new Intent("com.android.toptech.bee"));
			     } 
                     }
                    mArcType = TvPictureManager.getInstance().getVideoArcType();
                    if (mArcType >= TvPictureManager.VIDEO_ARC_MAX) {
                        return;
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            getSupportArcList();
                            comboBtnZoomMode.setIdx(mArcType);
                        }
                    });

                    if (mPictureMode == TvPictureManager.PICTURE_MODE_GAME
                            || mPictureMode == TvPictureManager.PICTURE_MODE_AUTO
                            || mPictureMode == TvPictureManager.PICTURE_MODE_PC
                            || specifyPicMode == TvPictureManager.PICTURE_MODE_GAME
                            || specifyPicMode == TvPictureManager.PICTURE_MODE_AUTO
                            || specifyPicMode == TvPictureManager.PICTURE_MODE_PC) {
                        TvPictureManager.getInstance()
                                .setVideoArcType(mArcType);
                    }
                    mPictureMode = specifyPicMode;
                }
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        SetFocusableForUserMode();
                        freshDataToUIWhenPicModChange();
                    }
                });
            }
        };

        seekBtnColorRed = new SeekBarButton(activity,
                R.id.linearlayout_pic_colortemperature_red, STEP, true) {
            @Override
            public void doUpdate() {
                ColorTemperatureExData colorTemperatureUpdateData = TvPictureManager
                        .getInstance().getColorTempratureEx();
                colorTemperatureUpdateData.redGain = seekBtnColorRed
                        .getProgressInt();
                TvPictureManager.getInstance().setColorTempratureEx(
                        colorTemperatureUpdateData);
            }
        };

        seekBtnColorGreen = new SeekBarButton(activity,
                R.id.linearlayout_pic_colortemperature_green, STEP, true) {
            @Override
            public void doUpdate() {
                ColorTemperatureExData colorTemperatureUpdateData = TvPictureManager
                        .getInstance().getColorTempratureEx();
                colorTemperatureUpdateData.greenGain = seekBtnColorGreen
                        .getProgressInt();
                TvPictureManager.getInstance().setColorTempratureEx(
                        colorTemperatureUpdateData);
            }
        };

        seekBtnColorBlue = new SeekBarButton(activity,
                R.id.linearlayout_pic_colortemperature_blue, STEP, true) {
            @Override
            public void doUpdate() {
                ColorTemperatureExData colorTemperatureUpdateData = TvPictureManager
                        .getInstance().getColorTempratureEx();
                colorTemperatureUpdateData.blueGain = seekBtnColorBlue
                        .getProgressInt();
                TvPictureManager.getInstance().setColorTempratureEx(
                        colorTemperatureUpdateData);
            }
        };

        comboBtnColorTemperature = new ComboButton(activity, activity
                .getResources().getStringArray(
                        R.array.str_arr_pic_colortemperature_vals),
                R.id.linearlayout_pic_colortemperature, 1, 2, true) {
            @Override
            public void doUpdate() {
                SetFocusableForColorTmpUserMode();
                Log.i(TAG,
                        "ColorTemperature:IDX = "
                                + comboBtnColorTemperature.getIdx());

                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setColorTempratureIdx(
                            comboBtnColorTemperature.getIdx());
                }
                freshDataToUIWhenColorTmpChange();
            }
        };
        comboBtnColorTemperature.setItemEnable(3, false);

        comboBtnZoomMode = new ComboButton(activity, activity.getResources()
                .getStringArray(R.array.str_arr_pic_zoommode_vals),
                R.id.linearlayout_pic_zoommode, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setVideoArcType(
                            comboBtnZoomMode.getIdx());
                }
				SetFocusableForUserMode();//add by hz 20160831 for mantis:0020139
            }
        };
        comboBtnZoomMode.setCallBack(new ComboButton.CallBack(){
            //@Override
            public boolean isAdjEnabled() {
                boolean isSigStable = false;
                boolean isSupportedZoom = false;
                try {
                isSigStable = TvManager.getInstance().getPlayerManager().isSignalStable();
                isSupportedZoom = TvManager.getInstance().getPictureManager().isSupportedZoom();
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }

                if (!isSigStable || !isSupportedZoom) return false;

                return true;
            }});
        comboBtnImageNoiseReduction = new ComboButton(activity, activity
                .getResources().getStringArray(
                        R.array.str_arr_pic_imgnoisereduction_vals),
                R.id.linearlayout_pic_imgnoisereduction, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setNoiseReduction(
                            comboBtnImageNoiseReduction.getIdx());
                }
            }
        };
		TVRootApp app = (TVRootApp) activity.getApplication();
		if(app.isCusOnida()==true){
			comboBtnImageNoiseReduction.setItemEnable(4, false);
		}
        comboBtnMPEGNoiseReduction = new ComboButton(activity, activity
                .getResources().getStringArray(
                        R.array.str_arr_pic_mpegnoisereduction_vals),
                R.id.linearlayout_pic_mpegnoisereduction, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    TvPictureManager.getInstance().setMpegNoiseReduction(
                            comboBtnMPEGNoiseReduction.getIdx());
                }
            }
        };
        comboBtnxvYCC = new ComboButton(activity, activity.getResources()
                .getStringArray(R.array.str_arr_xvycc_vals),
                R.id.linearlayout_pic_xvycc, 1, 2, true) {
            @Override
            public void doUpdate() {
                if (TvPictureManager.getInstance() != null) {
                    if (comboBtnxvYCC.getIdx() == 0) {
                        TvPictureManager.getInstance().setxvYCCEnable(false,
                                XVYCC_ON_SRGB_MAIN);
                    } else {
                        TvPictureManager.getInstance().setxvYCCEnable(true,
                                XVYCC_NORMAL_MAIN);
                    }
                }
            }
        };

        myButtonPcImageMode = new MyButton(activity,
                R.id.linearlayout_pic_pcimagemode);
        myButtonPcImageMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,
                        PCImageModeDialogActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        LinearLayout pccontainer = (LinearLayout) activity
                .findViewById(R.id.linearlayout_pic_pcimagemode);
        TextView pctextview = (TextView) (pccontainer.getChildAt(0));
        if (TvCommonManager.getInstance() != null) {
            boolean needToGrayItem = true;
            int inputSrc = TvCommonManager.getInstance()
                    .getCurrentTvInputSource();

            if ((inputSrc == TvCommonManager.INPUT_SOURCE_VGA)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA2)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA3)) {
                if ((TvCommonManager.getInstance().isSignalStable(inputSrc))
                       /* && (true == TVRootApp.getSignalFormatSupport())*/) {
                    needToGrayItem = false;
                }
            }
            if (true == needToGrayItem) {
            	
                pccontainer.setFocusable(false);
                if (getApp().isCusOnida())                    
                    pccontainer.setVisibility(View.GONE);
                else
                	pctextview.setTextColor(Color.GRAY);
            } else {
            	
                pccontainer.setFocusable(true);
                if (getApp().isCusOnida()) 
                	pccontainer.setVisibility(View.VISIBLE);
                else            
                	pctextview.setTextColor(Color.WHITE);
            }
        }
        if (true == Tools.isBox()) {
            comboBtnImageNoiseReduction.setVisibility(View.GONE);
            comboBtnMPEGNoiseReduction.setVisibility(View.GONE);
        }
        if (true == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_HDMITX)) {
            comboBtnImageNoiseReduction.setVisibility(View.GONE);
            comboBtnMPEGNoiseReduction.setVisibility(View.GONE);
        }
        initPicArcSupportTypes();
       // setOnFocusChangeListeners();
        setOnClickListeners();
        //comboBtnPictureMode.setFocused();
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == INVALID_PICTURE_MODE) {
                comboBtnPictureMode.setItemEnable(
                        TvPictureManager.PICTURE_MODE_AUTO, false);
                comboBtnPictureMode.setItemEnable(
                        TvPictureManager.PICTURE_MODE_PC, false);
            } else if (what == VALID_PICTURE_MODE) {
                comboBtnPictureMode.setItemEnable(
                        TvPictureManager.PICTURE_MODE_AUTO, true);
                comboBtnPictureMode.setItemEnable(
                        TvPictureManager.PICTURE_MODE_PC, true);
            }
            //modfiy for M029 picmode.lxk 20150310
            if(getApp().isCusOnida())
            {
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_DYNAMIC, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_GAME, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_AUTO, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_PC, false);
                if(!SystemProperties.getBoolean("ro.onida.enablebee", false))
                 comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_NATURAL, false);
            }else {
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_GAME, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_AUTO, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_PC, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_VIVID, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_NATURAL, false);
                comboBtnPictureMode.setItemEnable(TvPictureManager.PICTURE_MODE_SPORTS, false);
			}
            //end
            super.handleMessage(msg);
        }
    }

    private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(activity.getResources().getString(
                    R.string.str_pic_pcimagemode_waiting));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
        }
        return progressDialog;
    }

    private void setOnClickListeners() {
    	/* // close by jerry 20160411, we don't need this */
        OnClickListener BtnOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
               sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
            }
        };
        seekBtnContrast.setOnClickListener(BtnOnClickListener);
        seekBtnBrightness.setOnClickListener(BtnOnClickListener);
        seekBtnSharpness.setOnClickListener(BtnOnClickListener);
        seekBtnSaturation.setOnClickListener(BtnOnClickListener);
        seekBtnHue.setOnClickListener(BtnOnClickListener);
        seekBtnBacklight.setOnClickListener(BtnOnClickListener);
        seekBtnColorRed.setOnClickListener(BtnOnClickListener);
        seekBtnColorGreen.setOnClickListener(BtnOnClickListener);
        seekBtnColorBlue.setOnClickListener(BtnOnClickListener);
        
        comboBtnPictureMode.setOnClickListener(BtnOnClickListener);
        comboBtnColorTemperature.setOnClickListener(BtnOnClickListener);
        comboBtnZoomMode.setOnClickListener(BtnOnClickListener);
        comboBtnImageNoiseReduction.setOnClickListener(BtnOnClickListener);
    }
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

    public void LoadDataToUI() {
        if (TvPictureManager.getInstance() != null) {
            mPictureMode = TvPictureManager.getInstance().getPictureMode();
            comboBtnPictureMode.setIdx(mPictureMode);
            int inputSrcType = TvCommonManager.getInstance()
                    .getCurrentTvInputSource();
            ColorTemperatureExData colorTemperatureExData = TvPictureManager
                    .getInstance().getColorTempratureEx();

            // Refine performance with query DB by content provider to reduce
            // startup time in picture page.
            Cursor cursor = this.activity
                    .getApplicationContext()
                    .getContentResolver()
                    .query(Uri.parse("content://mstar.tv.usersetting/picmode_setting/inputsrc/"
                            + inputSrcType + "/picmode/" + mPictureMode), null,
                            null, null, null);
            if (cursor.moveToFirst()) {
                seekBtnContrast.setProgress((short) cursor.getInt(cursor
                        .getColumnIndex("u8Contrast")));
                seekBtnBrightness.setProgress((short) cursor.getInt(cursor
                        .getColumnIndex("u8Brightness")));
                seekBtnSharpness.setProgress((short) cursor.getInt(cursor
                        .getColumnIndex("u8Sharpness")));
                seekBtnHue.setProgress((short) cursor.getInt(cursor
                        .getColumnIndex("u8Hue")));
                seekBtnSaturation.setProgress((short) cursor.getInt(cursor
                        .getColumnIndex("u8Saturation")));
                seekBtnBacklight.setProgress((short) cursor.getInt(cursor
                        .getColumnIndex("u8Backlight")));
            }
            cursor.close();

            seekBtnColorRed.setProgressInt(colorTemperatureExData.redGain);
            seekBtnColorGreen.setProgressInt(colorTemperatureExData.greenGain);
            seekBtnColorBlue.setProgressInt(colorTemperatureExData.blueGain);
            boolean enable = SystemProperties.getBoolean("mstar.cus.onida", false);
            short colorTempIdx = getColorTemperatureSetting();
            comboBtnColorTemperature.setIdx(colorTempIdx);

            cursor = this.activity
                    .getApplicationContext()
                    .getContentResolver()
                    .query(Uri.parse("content://mstar.tv.usersetting/nrmode/nrmode/"
                            +(enable ? colorTempIdx : 1) + "/inputsrc/" + inputSrcType),
                            null, null, null, null);
            int eNr = 0;
            int eMpegNr = 0;
            if (cursor.moveToFirst()) {
                eNr = cursor.getInt(cursor.getColumnIndex("eNR"));
                eMpegNr = cursor.getInt(cursor.getColumnIndex("eMPEG_NR"));
            }
            cursor.close();
            comboBtnImageNoiseReduction.setIdx(eNr);
            comboBtnMPEGNoiseReduction.setIdx(eMpegNr);

            //add by fch 20170721
            int zoomIndex = TvPictureManager.getInstance().getVideoArcType();
            if(TvCommonManager.INPUT_SOURCE_HDMI <= TvCommonManager.getInstance().getCurrentTvInputSource()
					&& TvCommonManager.INPUT_SOURCE_HDMI_MAX>= TvCommonManager.getInstance().getCurrentTvInputSource()){
            	if(zoomIndex == EnumVideoArcType.E_Panorama.ordinal() || zoomIndex == EnumVideoArcType.E_DEFAULT.ordinal()){
            		zoomIndex = EnumVideoArcType.E_AUTO.ordinal();
            		TvPictureManager.getInstance().setVideoArcType(zoomIndex);
            	}
            }
            if((TvCommonManager.INPUT_SOURCE_CVBS <= TvCommonManager.getInstance().getCurrentTvInputSource()
					&& TvCommonManager.INPUT_SOURCE_CVBS_MAX>= TvCommonManager.getInstance().getCurrentTvInputSource()) 
            		|| (TvCommonManager.INPUT_SOURCE_YPBPR <= TvCommonManager.getInstance().getCurrentTvInputSource()
        					&& TvCommonManager.INPUT_SOURCE_YPBPR_MAX>= TvCommonManager.getInstance().getCurrentTvInputSource()) 
            		|| (TvCommonManager.INPUT_SOURCE_ATV == TvCommonManager.getInstance().getCurrentTvInputSource())){
            	if(zoomIndex == EnumVideoArcType.E_Panorama.ordinal() || zoomIndex == EnumVideoArcType.E_DEFAULT.ordinal() 
            			|| zoomIndex == EnumVideoArcType.E_JustScan.ordinal()){
            		zoomIndex = EnumVideoArcType.E_AUTO.ordinal();
            		TvPictureManager.getInstance().setVideoArcType(zoomIndex);
            	}
            }
            //end fch
            comboBtnZoomMode.setIdx(TvPictureManager.getInstance()
                    .getVideoArcType());

            comboBtnxvYCC.setIdx(TvPictureManager.getInstance()
                    .getxvYCCEnable() == true ? ON : OFF);
        }
	  freshSignalStable();
        SetFocusableForColorTmpUserMode();
    }

    public void freshSignalStable(){
        try {
            isSignalStable = TvManager.getInstance().getPlayerManager().isSignalStable();
            isSupportZoom = TvManager.getInstance().getPictureManager().isSupportedZoom();
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        SetFocusableForUserMode();
        }

    private short getColorTemperatureSetting() {
        short colorTempIdx = 0;
        boolean enable = SystemProperties.getBoolean("mstar.cus.onida", false);
        if (TvPictureManager.getInstance() != null) {
            int inputSrcType = TvCommonManager.getInstance()
                    .getCurrentTvInputSource();
            int pictureMode = TvPictureManager.getInstance().getPictureMode();
           // pictureMode = 1;
            Cursor cursor = this.activity
                    .getApplicationContext()
                    .getContentResolver()
                    .query(Uri.parse("content://mstar.tv.usersetting/picmode_setting/inputsrc/"
                            + inputSrcType + "/picmode/" + (enable ? pictureMode : 1)), null,
                            null, null, "PictureModeType");
            if (cursor.moveToFirst()) {
                colorTempIdx = (short) cursor.getInt(cursor
                        .getColumnIndex("eColorTemp"));
            }
            cursor.close();
        }

        return colorTempIdx;
    }

    private void updateBtnColorTemperature() {
        comboBtnColorTemperature.setIdx(getColorTemperatureSetting());
        freshDataToUIWhenColorTmpChange();
        SetFocusableForColorTmpUserMode();
    }

    private void freshDataToUIWhenPicModChange() {
        if (TvPictureManager.getInstance() != null) {
            seekBtnContrast.setProgress((short) TvPictureManager.getInstance()
                    .getVideoItem(TvPictureManager.PICTURE_CONTRAST));
            seekBtnBrightness.setProgress((short) TvPictureManager
                    .getInstance().getVideoItem(
                            TvPictureManager.PICTURE_BRIGHTNESS));
            seekBtnSharpness.setProgress((short) TvPictureManager.getInstance()
                    .getVideoItem(TvPictureManager.PICTURE_SHARPNESS));
            seekBtnHue.setProgress((short) TvPictureManager.getInstance()
                    .getVideoItem(TvPictureManager.PICTURE_HUE));
            seekBtnSaturation.setProgress((short) TvPictureManager
                    .getInstance().getVideoItem(
                            TvPictureManager.PICTURE_SATURATION));
            seekBtnBacklight.setProgress((short) TvPictureManager.getInstance()
                    .getBacklight());
            comboBtnImageNoiseReduction.setIdx(TvPictureManager.getInstance()
                    .getNoiseReduction());
            comboBtnMPEGNoiseReduction.setIdx(TvPictureManager.getInstance()
                    .getMpegNoiseReduction());
            comboBtnZoomMode.setIdx(TvPictureManager.getInstance()
                    .getVideoArcType());
            comboBtnxvYCC.setIdx(TvPictureManager.getInstance()
                    .getxvYCCEnable() == true ? ON : OFF);
            updateBtnColorTemperature();
        }
    }

    private void getSupportArcList() {
        if (TvPictureManager.getInstance() != null) {
            boolean[] supportArcListTypes = TvPictureManager.getInstance()
                    .getAspectRationList();
            if (getApp().isCusOnida()) {
        		supportArcListTypes[9]=false;//Skye rm p2p 
				}
            for (int i = supportArcListTypes.length - 1; i >= 0; i--) {
                comboBtnZoomMode.setItemEnable(i, supportArcListTypes[i]);
            }
        }
    }

    private void initPicArcSupportTypes() {
        if (TvPictureManager.getInstance() != null
                && TvCommonManager.getInstance() != null) {
            int inputSrc = TvCommonManager.getInstance()
                    .getCurrentTvInputSource();
            if ((inputSrc == TvCommonManager.INPUT_SOURCE_VGA)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA2)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_VGA3)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI2)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI3)
                    || (inputSrc == TvCommonManager.INPUT_SOURCE_HDMI4)) {
                myHandler.sendEmptyMessage(VALID_PICTURE_MODE);
            } else {
                myHandler.sendEmptyMessage(INVALID_PICTURE_MODE);
            }
            getSupportArcList();
        }
    }

    private void freshDataToUIWhenColorTmpChange() {
        if (TvPictureManager.getInstance() != null) {
            ColorTemperatureExData colorTemperatureExData = TvPictureManager
                    .getInstance().getColorTempratureEx();
            TvPictureManager.getInstance().setColorTempratureEx(
                    colorTemperatureExData);
            seekBtnColorRed.setProgressInt(colorTemperatureExData.redGain);
            seekBtnColorGreen.setProgressInt(colorTemperatureExData.greenGain);
            seekBtnColorBlue.setProgressInt(colorTemperatureExData.blueGain);
            /*
            comboBtnImageNoiseReduction.setIdx(TvPictureManager.getInstance()
                    .getNoiseReduction());
            comboBtnMPEGNoiseReduction.setIdx(TvPictureManager.getInstance()
                    .getMpegNoiseReduction());
            */
        }
    }

    private void SetFocusableForUserMode() {
        if (comboBtnPictureMode.getIdx() != TvPictureManager.PICTURE_MODE_USER) {
            seekBtnContrast.setEnable(false);
            seekBtnBrightness.setEnable(false);
            seekBtnSharpness.setEnable(false);
            seekBtnHue.setEnable(false);
            seekBtnSaturation.setEnable(false);
            seekBtnBacklight.setEnable(false);
            seekBtnContrast.setFocusable(false);
            seekBtnBrightness.setFocusable(false);
            seekBtnSharpness.setFocusable(false);
            seekBtnHue.setFocusable(false);
            seekBtnSaturation.setFocusable(false);
            seekBtnBacklight.setFocusable(false);
        } else {
            seekBtnContrast.setEnable(true);
            seekBtnBrightness.setEnable(true);
            seekBtnBacklight.setEnable(true);
            seekBtnContrast.setFocusable(true);
            seekBtnBrightness.setFocusable(true);
			seekBtnBacklight.setFocusable(true);
			
            if (isHueModeEnable())
            {
	            seekBtnHue.setEnable(true);
				seekBtnHue.setFocusable(true);
            }
            else
            {
	            seekBtnHue.setFocusable(false);
	            seekBtnHue.setEnable(false);
            }
			if(TvCommonManager.getInstance().getCurrentTvInputSource()== TvCommonManager.INPUT_SOURCE_VGA
				//add by hz 20160831 for mantis:0020139
				||(TvCommonManager.INPUT_SOURCE_HDMI <= TvCommonManager.getInstance().getCurrentTvInputSource()
					&& TvCommonManager.INPUT_SOURCE_HDMI_MAX>= TvCommonManager.getInstance().getCurrentTvInputSource()
					&& (TvPictureManager.getInstance().getVideoArcType()==TvPictureManager.getInstance().VIDEO_ARC_DOTBYDOT 
					|| TvPictureManager.getInstance().getVideoArcType()==TvPictureManager.getInstance().VIDEO_ARC_JUSTSCAN)))
				//end hz
			{
				seekBtnSharpness.setEnable(false);
	            seekBtnSaturation.setEnable(false);
	            seekBtnSharpness.setFocusable(false);
	            seekBtnSaturation.setFocusable(false);
			}
			else
			{
				seekBtnSharpness.setEnable(true);
	            seekBtnSaturation.setEnable(true);
	            seekBtnSharpness.setFocusable(true);
	            seekBtnSaturation.setFocusable(true);
			}   
        }
		if(isSignalStable&&isSupportZoom)
		{
			comboBtnZoomMode.setEnable(true);
			comboBtnZoomMode.setFocusable(true);
		}
		else
		{
			comboBtnZoomMode.setEnable(false);
			comboBtnZoomMode.setFocusable(false);
		}
		//Bingo 20160602 add
		if(TvCommonManager.getInstance().getCurrentTvInputSource()== TvCommonManager.INPUT_SOURCE_VGA)
		{
			comboBtnImageNoiseReduction.setEnable(false);
			comboBtnImageNoiseReduction.setFocusable(false);
		}
		else
		{
			comboBtnImageNoiseReduction.setEnable(true);
			comboBtnImageNoiseReduction.setFocusable(true);
		}
		//end
		SetFocusableForLocked();
    }

    private void SetFocusableForColorTmpUserMode() {
        if (comboBtnColorTemperature.getIdx() != TvPictureManager.COLOR_TEMP_USER1) {
            seekBtnColorRed.setEnable(false);
            seekBtnColorGreen.setEnable(false);
            seekBtnColorBlue.setEnable(false);
            seekBtnColorRed.setFocusable(false);
            seekBtnColorGreen.setFocusable(false);
            seekBtnColorBlue.setFocusable(false);
        } else {
            seekBtnColorRed.setEnable(true);
            seekBtnColorGreen.setEnable(true);
            seekBtnColorBlue.setEnable(true);
            seekBtnColorRed.setFocusable(true);
            seekBtnColorGreen.setFocusable(true);
            seekBtnColorBlue.setFocusable(true);
        }
        SetFocusableForLocked();
    }

    private boolean isHueModeEnable()
    {
        int tvInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
        if (tvInputSource == TvCommonManager.INPUT_SOURCE_ATV
            ||((tvInputSource >=  TvCommonManager.INPUT_SOURCE_CVBS)
                &&(tvInputSource <  TvCommonManager.INPUT_SOURCE_CVBS_MAX)))
        {
            if (TvChannelManager.AVD_VIDEO_STANDARD_NTSC_M
                 == TvChannelManager.getInstance().getAvdVideoStandard()
                 ||TvChannelManager.AVD_VIDEO_STANDARD_NTSC_44 //add by hz 20170810 for mantis :0034039	
                 == TvChannelManager.getInstance().getAvdVideoStandard())
                 return true;
        }

        return false;
    }

    private void SetFocusableForLocked() {
        boolean mlocked = getApp().getChannelLockOrNot();
        if (mlocked) {
            comboBtnZoomMode.setEnable(false);
			comboBtnZoomMode.setFocusable(false);

			comboBtnPictureMode.setEnable(false);
			comboBtnPictureMode.setFocusable(false);
			seekBtnContrast.setEnable(false);
			seekBtnContrast.setFocusable(false);
			seekBtnBrightness.setEnable(false);
			seekBtnBrightness.setFocusable(false);
			seekBtnSaturation.setEnable(false);
			seekBtnSaturation.setFocusable(false);
			seekBtnHue.setEnable(false);
			seekBtnHue.setFocusable(false);
			seekBtnSharpness.setEnable(false);
			seekBtnSharpness.setFocusable(false);
        }
    }
    private TVRootApp getApp()
    {
        return (TVRootApp) activity.getApplication();
    }
}
