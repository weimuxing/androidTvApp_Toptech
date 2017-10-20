//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tv.TvTimerManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.os.SystemProperties;

public class TVRootApp extends Application {
    private final static String TAG = "TVRootApp";

    public final static int TEXT_STATUS_TTX = 1;

    public final static int TEXT_STATUS_MHEG5 = 2;

    public final static int TEXT_STATUS_HBBTV = 3;

    public final static int TEXT_STATUS_GINGA = 4;

    public final static int TEXT_STATUS_NONE = 5;

    public final static String REMOTE_UNKNOWN = "unknown";

    // for hot key
    public static boolean isSoundSettingDirty = false;

    public static boolean isVideoSettingDirty = false;

    public static boolean isPicModeSettingDirty = false;

    public static boolean isFactoryDirty = false;

    // for misc
    public static boolean isMiscSoundDirty = false;

    public static boolean isMiscPictureDirty = false;

    public static boolean isMiscS3DDirty = false;

    public static boolean isLoadDBOver = false;

    private static boolean isSourceDirty = false;

    private static boolean isSignalFormatSupported = false;

    private boolean isPVREnable = false;

    private boolean isTTXEnable = false;

    private boolean isCCEnable = false;

    private boolean isOADEnable = false;

    private boolean isLockEnable = false;

    private boolean isStickerEnable = false;

    private boolean isCECEnable = false;

    private boolean isRGBRangeEnable = false;

    private boolean isHOHEnable = false;

    private boolean isADEnable = false;

    private boolean isSpdifEnable = false;

    private boolean isOptionEnable = false;

    private boolean isArcEnable = false;
    
    private boolean isBTorESelect = false;
    
    private boolean isCusOnida = false;
    
    private boolean isNOTV = false;

    private boolean isNO_DTV = false;

    private String remoteName = null;

    private static boolean isGetBtSource = false;

    private static int BtSource = -1;

    private RootBroadCastReceiver rootBroadCastReceiver;

    private static Context mContext;

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        };
    };

    /**
     * start modefied by jachensy 2012-6-28
     */
    Runnable run = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isLoadDBOver = true;
            rootBroadCastReceiver = new RootBroadCastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH");
            registerReceiver(rootBroadCastReceiver, filter);
            initLittleDownCounter();
            Looper.loop();
        }
    };

    /**
     * end modefied by jachensy 2012-6-28
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initConfigData();
        /**
         * start modefied by jachensy 2012-6-28
         */
        Thread t = new Thread(run);
        t.start();
        TvTimerManager.getInstance().updateTimeZone();
        TvCommonManager.getInstance().disableTvosIr();
    }

    @Override
    public void onTerminate() {
        this.unregisterReceiver(rootBroadCastReceiver);
        super.onTerminate();
    }

    private void initConfigData() {
    	if (false)
    	{
	        Cursor snconfig = getContentResolver().query(
	                Uri.parse("content://mstar.tv.usersetting/snconfig"), null,
	                null, null, null);
	        if (snconfig != null) {
	            if (snconfig.moveToFirst()) {
	                isPVREnable = snconfig.getInt(snconfig
	                        .getColumnIndex("PVR_ENABLE")) == 1 ? true : false;
	                isTTXEnable = snconfig.getInt(snconfig
	                        .getColumnIndex("TTX_ENABLE")) == 1 ? true : false;
	            }
	            snconfig.close();
	        } else {
	            isPVREnable = true;
	            isTTXEnable = true;
	        }
        }
        else
        {
        	isBTorESelect = SystemProperties.getBoolean("mstar.BTorEqualizer.select", false);
			isPVREnable = SystemProperties.getBoolean("mstar.pvr.enable", false);
	        isTTXEnable = SystemProperties.getBoolean("mstar.ttx.enable", false);
	        isCCEnable = SystemProperties.getBoolean("mstar.cc.enable", false);
	        isOADEnable = SystemProperties.getBoolean("mstar.oad.enable", false);
	        isLockEnable = SystemProperties.getBoolean("mstar.lock.enable", false);
	        isStickerEnable = SystemProperties.getBoolean("mstar.sticker.enable", false);
	        isCECEnable = SystemProperties.getBoolean("mstar.cec.enable", false);
	        isRGBRangeEnable = SystemProperties.getBoolean("mstar.rgbRange.enable", false);
	        isHOHEnable = SystemProperties.getBoolean("mstar.hoh.enable", false);
	        isADEnable = SystemProperties.getBoolean("mstar.ad.enable", false);
	        isSpdifEnable = SystemProperties.getBoolean("mstar.spdif.enable", false);
	        isOptionEnable = SystemProperties.getBoolean("mstar.option.enable", false);
	        isArcEnable = SystemProperties.getBoolean("mstar.arc.enable", false);
	        remoteName = SystemProperties.get("mstar.toptech.remote", REMOTE_UNKNOWN);
	        isCusOnida = SystemProperties.getBoolean("mstar.cus.onida", false);
	        isNOTV = SystemProperties.getBoolean("persist.sys.no_tv", false);
               isNO_DTV = SystemProperties.getBoolean("persist.sys.no_dtv", false);
        }
    }

    public boolean isBTorESelect(){
    	return isBTorESelect;
    }
    
    public boolean isCusOnida(){
    	return isCusOnida;
    }
    public boolean isNOTV(){
    	return isNOTV;
    }
    
    public boolean isNO_DTV(){
    	return isNO_DTV;
    }
    
    public boolean isPVREnable() {
        return isPVREnable && (!isNO_DTV);
    }

    public boolean isTTXEnable() {
        return isTTXEnable;
    }

    public boolean isCCEnable() {
        return isCCEnable;
    }

    public boolean isOadEnable() {
        return isOADEnable;
    }

    public boolean isLockEnable() {
        return isLockEnable;
    }

    public boolean isStickerEnable() {
        return isStickerEnable;
    }

    public boolean isCECEnable() {
        return isCECEnable;
    }

    public boolean isRGBRangeEnable() {
        return isRGBRangeEnable;
    }

    public boolean isHOHEnable() {
        return isHOHEnable;
    }

    public boolean isADEnable() {
        return isADEnable;
    }

    public boolean isSpdifEnable() {
        return isSpdifEnable;
    }

    public boolean isOptionEnable() {
        return isOptionEnable;
    }

    public boolean isArcEnable() {
        return isArcEnable;
    }

    public String getRemote() {
        return remoteName;
    }

    public void initLanguageCfig() {
        int languageindex = TvCommonManager.getInstance().getOsdLanguage();
        Resources res = getResources();
        Configuration config = res.getConfiguration();
        if (languageindex == TvLanguage.ENGLISH) {
            config.locale = Locale.ENGLISH;
        } else {
            if (languageindex == TvLanguage.CHINESE) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else {
                config.locale = Locale.TAIWAN;
            }
        }
        DisplayMetrics dm = res.getDisplayMetrics();
        res.updateConfiguration(config, dm);
    }

    public static void setSourceDirty(boolean isDirty) {
        Log.v(TAG, "set source dirty to " + isDirty);
        isSourceDirty = isDirty;
    }

    public static boolean getSourceDirty() {
        return isSourceDirty;
    }

    private void initLittleDownCounter() {
        LittleDownTimer.getInstance().start();
        int value = TvCommonManager.getInstance().getOsdTimeoutInSecond();
        if (value < 1) {
            value = 5;
        }
        if (value > 30) {
            LittleDownTimer.stopMenu();
        } else {
            LittleDownTimer.setMenuStatus(value);
        }
    }

    // Ui setting root broadcast receiver.
    private class RootBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // mute
            if (action
                    .equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH")) {
            } else {
                Log.e(TAG, "Unknown Key Event!");
            }
        }
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setSignalFormatSupport(boolean status) {
        isSignalFormatSupported = status;
    }

    public static boolean getSignalFormatSupport() {
        return isSignalFormatSupported;
    }

    public static int getBtSource()
    {
		if (false == isGetBtSource)
		{
			int[] ret = TvCommonManager.getInstance().setTvosCommonCommand("GetBlueToothSource");
			BtSource = ret[0];
			isGetBtSource = true;
		}
		return BtSource;
    }

    public static boolean getChannelLockOrNot()
    {
        boolean mlock = false;
		int[] ret = TvCommonManager.getInstance().setTvosCommonCommand("queryChannelLockStatus");
		mlock = ret[0]==1?true:false;

		return mlock;
    }
}
