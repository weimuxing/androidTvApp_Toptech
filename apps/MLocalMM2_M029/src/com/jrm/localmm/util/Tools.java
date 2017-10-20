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
package com.jrm.localmm.util;

import java.io.File;

import java.lang.reflect.Method;
import java.math.BigInteger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemProperties;
import android.util.Log;

import com.jrm.localmm.R;
import com.jrm.localmm.ui.MediaContainerApplication;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.EnumScreenMuteType;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;
import com.mstar.android.tvapi.common.PictureManager;
import com.mstar.android.tvapi.common.AudioManager;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideo3DTo2D;
import com.mstar.android.tvapi.common.vo.Enum3dType;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideo3DTo2D;
import com.mstar.android.tvapi.common.vo.EnumScalerWindow;
import com.mstar.android.tvapi.common.vo.VideoWindowType;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureDeviceType;
import com.mstar.android.tvapi.common.vo.EnumAuidoCaptureSource;
import com.mstar.android.tvapi.common.vo.EnumAudioProcessorType;
import com.mstar.android.tvapi.common.vo.EnumAudioVolumeSourceType;

/**
 * Project name: LocalMM class name: Tools class description: tool class
 *
 * @author luoyong
 * @date 2011-07-27
 */
public class Tools {
    private static final String FILE_SIZE_B = "B";
    private static final String FILE_SIZE_KB = "KB";
    private static final String FILE_SIZE_MB = "MB";
    private static final String FILE_SIZE_GB = "GB";
    private static final String FILE_SIZE_TB = "TB";
    private static final String FILE_SIZE_NA = "N/A";
    private static final String TAG = "Tools";
    public static int CURPOS = 0;
    //skye 20160810
	public static final int ITEM_SUBTITLE_SETTING = 0;
	public static final int ITEM_BREAKPOINT_PLAY_SETTING =1;
	public static final int ITEM_TRACK_SETTING = 2;
	public static final int ITEM_PICTURE_MODE_SETTING = 3;
	public static final int ITEM_VOIDE_MODE_SETTING = 4;
	public static final int ITEM_COLOR_TEMPERATURE = 5;
	public static final int ITEM_IMAGE_NOISEREDUCTION=6;
	public static final int ITEM_THUMBNAIL_SETTING = 7;
	public static final int ITEM_ROTATE_SETTING = 8;
	public static final int ITEM_GoldenLeftEyeMode=9;
	public static final int ITEM_3D_SETTING = 10;
    /**
     * judgment file exists.
     *
     * @param path
     *            file path.
     * @return when the parameters specified file exists return true, otherwise
     *         returns false.
     */
    public static boolean isFileExist(String path) {
        return isFileExist(new File(path));
    }

    /**
     * judgment file exists.
     *
     * @param file
     *            {@link File}.
     * @return when the parameters specified file exists return true, otherwise
     *         returns false.
     */
    public static boolean isFileExist(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    /**
     * will millisecond number formatting 00:00:00.
     *
     * @param duration
     *            time value to seconds for the unit.
     * @return format for 00:00:00 forms of string or '-- : -- : -- ".
     */
    public static String formatDuration(long duration) {
        long time = duration / 1000;
        if (time <= 0) {
            return "--:--:--";
        }
        long min = time / 60 % 60;
        long hour = time / 60 / 60;
        long second = time % 60;
        return String.format("%02d:%02d:%02d", hour, min, second);
    }

    public static String formatISODuration(long duration) {
        long time = duration / 90000;
        if (time <= 0) {
            return "--:--:--";
        }
        long min = time / 60 % 60;
        long hour = time / 3600;
        long second = time % 60;
        return String.format("%02d:%02d:%02d", hour, min, second);
    }

    /**
     * will millisecond number formatting 00:00.
     *
     * @ param duration time value to seconds for the unit. @ return format for
     * 00:00 forms of string.
     */
    public static String formatDuration(int duration) {
        int time = duration / 1000;
        if (time == 0 && duration > 0) {
            time = 1;
        }

        int second = time % 60;
        int min = time / 60;
        long hour = 0;
        if (min > 60) {
            hour =  time / 3600;
            min = time / 60 % 60;
        }
        if (hour > 0)
            return String.format("%02d:%02d:%02d", hour, min, second);
        else
            return String.format("%02d:%02d", min, second);
    }

    /**
     * will file size into human form, the biggest said 1023 TB.
     *
     * @ param size file size. @ return file size minimum unit for B string.
     */
    public static String formatSize(BigInteger size) {
        // Less than
        if (size.compareTo(BigInteger.valueOf(1024)) == -1) {
            return (size.toString() + FILE_SIZE_B);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024)) == -1) {
            return (size.divide(BigInteger.valueOf(1024)).toString() + FILE_SIZE_KB);
        } else if (size.compareTo(BigInteger.valueOf(1024 * 1024 * 1024)) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024)).toString() + FILE_SIZE_MB);
        } else if (size.compareTo(BigInteger
                .valueOf(1024 * 1024 * 1024 * 1024L)) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024))
                    .toString() + FILE_SIZE_GB);
        } else if (size.compareTo(BigInteger
                .valueOf(1024 * 1024 * 1024 * 1024L).multiply(
                        BigInteger.valueOf(1024))) == -1) {
            return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024 * 1024L))
                    .toString() + FILE_SIZE_TB);
        }
        return FILE_SIZE_NA;
    }

    /**
     * judgment parameter the specified file type is media files.
     *
     * @ param type said file types of string. @ return if it is media file
     * returns true on, otherwise returns false.
     */
    public static boolean isMediaFile(final int type) {
        if (Constants.FILE_TYPE_PICTURE == type
                || Constants.FILE_TYPE_SONG == type
                || Constants.FILE_TYPE_VIDEO == type) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return If the network can be used to return to true, otherwise returns
     *         false.
     */
    public static boolean isNetWorkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                // Get the network connection management object
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni == null) {
                    return false;
                }
                boolean isConnected = ni.isConnected();
                boolean isStateConnected = (ni.getState() == NetworkInfo.State.CONNECTED);
                Log.i(TAG, "isNetWorkConnected isConnected:" + isConnected + " isStateConnected:" + isStateConnected);
                if (isConnected && isStateConnected) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "network exception." + e.getMessage());
        }
        return false;
    }

    /**
     * @return The current usb disk equipment loading position.
     */
    public static String getUSBMountedPath() {
        return Environment.getExternalStorageDirectory().getParent();
    }

    // Play Settings option value
    private static String[] playSettingOptTextOne = null;
    private static String[] playSettingOptTextTwo = null;

    /**
     * @return Initialization play Settings related option value
     */
    public static String[] initPlaySettingOpt(Context context, int id) {
        if(playSettingOptTextOne == null) {
            playSettingOptTextOne = context.getResources().getStringArray(
                    R.array.play_setting_opt);
            if (isThumbnailModeOn()) {
                playSettingOptTextOne[Tools.ITEM_THUMBNAIL_SETTING] = context.getString(R.string.play_setting_0_value_1);
            } else {
                playSettingOptTextOne[Tools.ITEM_THUMBNAIL_SETTING] = context.getString(R.string.play_setting_0_value_2);
            }
        }
        if (playSettingOptTextTwo == null) {
            playSettingOptTextTwo = context.getResources().getStringArray(
                    R.array.play_setting_opt);
        }
        if (id == 1) {
            playSettingOptTextOne[Tools.ITEM_GoldenLeftEyeMode] = context.getString(R.string.play_setting_0_value_2);
            return playSettingOptTextOne;
        } else {
            playSettingOptTextTwo[Tools.ITEM_GoldenLeftEyeMode] = context.getString(R.string.play_setting_0_value_2);
            return playSettingOptTextTwo;
        }
    }

    /**
     * @param index
     *            initPlaySettingOpt
     */
    public static String getPlaySettingOpt(int index, int id) {
        if (id == 1) {
            if (index >= 0 && index < 10) {
                if (playSettingOptTextOne != null) {
                    return playSettingOptTextOne[index];
                }
                return null;
            }
        } else {
            if (index >= 0 && index < 10) {
                if (playSettingOptTextTwo != null) {
                    return playSettingOptTextTwo[index];
                }
                return null;
            }
        }
        return null;
    }

    /**
     * @ param index subscript index value. @ param value to set to value.
     */
    public static void setPlaySettingOpt(int index, final String value, int id) {
        if (id == 1) {
            if (index >= 0 && index < 11) {
                if (playSettingOptTextOne != null) {
                	//Log.d("skk", " setPlaySettingOpt     index=  "+index);
                    playSettingOptTextOne[index] = value;
                }
            }
        } else {
            if (index >= 0 && index < 11) {
                if (playSettingOptTextTwo != null) {
                    playSettingOptTextTwo[index] = value;
                }
            }
        }
        if (index == 2) {
            if (playSettingOptTextOne != null) {
                playSettingOptTextOne[index] = value;
            }
            if (playSettingOptTextTwo != null) {
                playSettingOptTextTwo[index] = value;
            }
        }
    }

    /**
     * Exit video player when eliminate all Settings option value.
     */
    public static void destroyAllSettingOpt() {
        playSettingOptTextOne = null;
        playSettingOptTextTwo = null;
    }

    /**
     * Stop media scanning.
     */
    public static void stopMediascanner(Context context) {
        Intent intent = new Intent();
        intent.setAction("action_media_scanner_stop");
        context.sendBroadcast(intent);
        Log.d("Tools", "stopMediascanner");
    }

    /**
     * Start media scanning.
     */
    public static void startMediascanner(Context context) {
        Intent intent = new Intent();
        intent.setAction("action_media_scanner_start");
        context.sendBroadcast(intent);
        Log.d("Tools", "startMediascanner");
    }

    /**
     * The size of file is whether larger than the specified size.
     *
     * @param path
     *            absolute path of file.
     * @param size
     *            the specified size of file.
     * @return true if the file is larger than size, otherwise false.
     */
    public static boolean isLargeFile(final String path, final long size) {
        File file = new File(path);
        // file does not exist
        if (!isFileExist(file)) {
            Log.d("Tools", "file does not exist");
            return true;
        }
        long length = file.length();
        Log.d("Tools", "size of file : " + length);
        // file bigger than size
        if (length > size) {
            return true;
        }
        return false;
    }

    /**
     * Screen open and cover screen
     *
     * @param isMute
     *            cover/open
     * @param time
     */
    public static void setVideoMute(boolean isMute, int time) {
        if (unSupportTVApi()) {
            return;
        }
        Log.i("Tools", "*********setVideoMute********isMute:" + isMute + " time:" + time);
        try {
            if (TvManager.getInstance() != null) {
                TvManager.getInstance().setVideoMute(isMute,
                        EnumScreenMuteType.E_BLACK, time,
                        EnumInputSource.E_INPUT_SOURCE_STORAGE);
            }
            // TvManager.setVideoMute(isMute, EnumScreenMuteType.E_BLACK, time,
            // EnumInputSource.E_INPUT_SOURCE_STORAGE);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    // Static class will capture large memory
    //private static TvCommonManager appSkin = null;
    //private static EnumInputSource inputSource = null;

    /*
     * Complete TV and storage of between source switching
     */
    public static void changeSource(final boolean isEnter) {
        if (unSupportTVApi()) {
            return;
        }
        Runnable localRunnable = new Runnable() {
            @Override
            public void run() {
                TvCommonManager appSkin = TvCommonManager.getInstance();
                EnumInputSource inputSource = null;

                // boot will input from TV switch to AP
                if (appSkin != null) {
                    // switching source to storage
                    if (isEnter) {
                        inputSource = appSkin.getCurrentInputSource();
                        if (inputSource != EnumInputSource.E_INPUT_SOURCE_STORAGE) {
                            appSkin.setInputSource(EnumInputSource.E_INPUT_SOURCE_STORAGE);
                        }
                    } else {// switching source to the TV
                        if (inputSource != EnumInputSource.E_INPUT_SOURCE_STORAGE) {
                            appSkin.setInputSource(inputSource);
                        }
                    }
                }
            }
        };

        // Switching source more time-consuming, so start thread operation
        new Thread(localRunnable).start();

    }

    public static boolean isCurrentInputSourceStorage() {
        if (unSupportTVApi()) {
            return false;
        } else {
            TvCommonManager appSkin = TvCommonManager.getInstance();
            EnumInputSource inputSource = null;

            if (appSkin != null) {
                inputSource = appSkin.getCurrentInputSource();
                Log.i(TAG, "isCurrentInputSourceStorage inputSource:" + inputSource);
                if (inputSource == EnumInputSource.E_INPUT_SOURCE_STORAGE) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean is3DTVPlugedIn() {
        try {
             PictureManager PM = TvManager.getInstance().getPictureManager();
             if (PM.is3DTVPlugedIn()) {
                 Log.i(TAG, "is3DTVPlugedIn");
                 return true;
             }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "!is3DTVPlugedIn");
        return false;
    }

    // Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
    public static void setDisplayFormat(EnumThreeDVideoDisplayFormat threeDVideoDisplayFormat) {
        Log.i(TAG,"setDisplayFormat threeDVideoDisplayFormat:" + threeDVideoDisplayFormat);
        TvS3DManager.getInstance().setDisplayFormat(threeDVideoDisplayFormat);
        /* Because SN would set desk-display-mode, so AN APK don't need do this again.
        int formatID = threeDVideoDisplayFormat.ordinal();
        String format = "";
        if (formatID == 0) {
            format = "0";
        } else {
            format = "2";
        }
        String hardname = getHardwareName();
        if (hardname != null && hardname.equals("monaco")) {
            SystemProperties.set("mstar.desk-display-mode", format);
        }
        */
    }

    // Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
//    public static EnumThreeDVideoDisplayFormat getDisplayFormat() {
//        EnumThreeDVideoDisplayFormat threeDVideoDisplayFormat = EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE;
//        threeDVideoDisplayFormat = TvS3DManager.getInstance().getDisplayFormat();
//        Log.i(TAG, "getDisplayFormat threeDVideoDisplayFormat:" + threeDVideoDisplayFormat);
//        return threeDVideoDisplayFormat;
//    }

    // Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
    public static void set3DTo2D(EnumThreeDVideo3DTo2D displayMode) {
        Log.d(TAG, "set3DTo2D(), paras displayMode = " + displayMode);
        TvS3DManager.getInstance().set3DTo2D(displayMode);
    }

    public static boolean hasSet3dFormat() {
        if (Tools.unSupportTVApi()) {
            return false;
        }
        if (getCurrent3dFormat() == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
            return false;
        } else {
            return true;
        }
    }

    // Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
    public static EnumThreeDVideoDisplayFormat getCurrent3dFormat() {
            EnumThreeDVideoDisplayFormat result = EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE;
            Enum3dType formatType = Enum3dType.EN_3D_NONE;
            try {
                formatType = TvManager.getInstance().getThreeDimensionManager().getCurrent3dFormat();
                Log.i(TAG, "getCurrent3dFormat formatType:" + formatType);
            } catch (TvCommonException e) {
                e.printStackTrace();
                return result;
            }
            if (formatType == Enum3dType.EN_3D_NONE) {
                // / 3D Off mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE;
            } else if (formatType == Enum3dType.EN_3D_TOP_BOTTOM) {
                // / 3D Top Bottom mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_TOP_BOTTOM;
            } else if (formatType == Enum3dType.EN_3D_SIDE_BY_SIDE_HALF) {
                // / 3D Side By Side mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_SIDE_BY_SIDE;
            } else if (formatType == Enum3dType.EN_3D_CHECK_BORAD) {
                // / 3D Check Board mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_CHECK_BOARD;
            } else if (formatType == Enum3dType.EN_3D_FRAME_ALTERNATIVE) {
                // / 3D Frame Alternative
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_FRAME_ALTERNATIVE;
            } else if (formatType == Enum3dType.EN_3D_2DTO3D) {
                // / 3D 2Dto3D mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_2DTO3D;
            }  else if (formatType == Enum3dType.EN_3D_LINE_ALTERNATIVE) {
                // / 3D Line Alternative mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_LINE_ALTERNATIVE;
            } else if (formatType == Enum3dType.EN_3D_FRAME_PACKING_1080P) {
                // / 3D Frame Packing mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_FRAME_PACKING;
            } else if (formatType == Enum3dType.EN_3D_FRAME_PACKING_720P) {
                // / 3D Frame Packing mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_FRAME_PACKING;
            } else if (formatType == Enum3dType.EN_3D_PIXEL_ALTERNATIVE) {
                // / 3D Pixel Alternative mode
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_PIXEL_ALTERNATIVE;
            } else {
                result =  EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO;
            }

            return result;
    }

    // Remember: Every time before you call this TV function, should judge Tools.unSupportTVApi.
    public static EnumThreeDVideo3DTo2D getCurrent3dFormatOnSTB2DTV() {
        EnumThreeDVideo3DTo2D result = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_NONE;
            Enum3dType formatType = Enum3dType.EN_3D_NONE;
            try {
                formatType = TvManager.getInstance().getThreeDimensionManager().getCurrent3dFormat();
                Log.i(TAG, "getCurrent3dFormatOnSTB2DTV formatType:" + formatType);
            } catch (TvCommonException e) {
                e.printStackTrace();
                return result;
            }
            if (formatType == Enum3dType.EN_3D_NONE) {
                // / 3Dto2D Off mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_NONE;
            } else if (formatType == Enum3dType.EN_3D_TOP_BOTTOM) {
                // / 3Dto2D Top Bottom mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_TOP_BOTTOM;
            } else if (formatType == Enum3dType.EN_3D_SIDE_BY_SIDE_HALF) {
                // / 3Dto2D Side By Side mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_SIDE_BY_SIDE;
            } else if (formatType == Enum3dType.EN_3D_FRAME_PACKING_1080P) {
                // / 3Dto2D Frame Packing mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_FRAME_PACKING;
            } else if (formatType == Enum3dType.EN_3D_FRAME_PACKING_720P) {
                // / 3Dto2D Frame Packing mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_FRAME_PACKING;
            } else if (formatType == Enum3dType.EN_3D_FRAME_ALTERNATIVE) {
                // / 3Dto2D Frame Alternative mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_FRAME_ALTERNATIVE;
            } else if (formatType == Enum3dType.EN_3D_LINE_ALTERNATIVE) {
                // / 3Dto2D Line Alternative mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_LINE_ALTERNATIVE;
            } else if (formatType == Enum3dType.EN_3D_PIXEL_ALTERNATIVE) {
                // / 3Dto2D Pixel Alternative mode
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_PIXEL_ALTERNATIVE;
            } else {
                result =  EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_NONE;
            }

            return result;

    }

    private static final String MSTAR_PRODUCT_CHARACTERISTICS = "mstar.product.characteristics";
    private static final String MSTAR_PRODUCT_STB = "stb";
    private static String mProduct = null;

    public static boolean isBox() {
        if (mProduct == null) {
            Class<?> systemProperties = null;
            Method method = null;
            try {
                systemProperties = Class.forName("android.os.SystemProperties");
                method = systemProperties.getMethod("get", String.class,
                        String.class);
                mProduct = (String) method.invoke(null,
                        MSTAR_PRODUCT_CHARACTERISTICS, "");
            } catch (Exception e) {
                return false;
            }
        }
        // Log.d("Tools", "mstar.product.characteristics is " + mProduct);
        if (MSTAR_PRODUCT_STB.equals(mProduct)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean showRootDir() {
        String value = SystemProperties.get("persist.mstar.localmm.rootdir", null);
        if (value != null && value.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public static boolean isMadisonPlatform() {
        String name = getHardwareName();
        if (name != null && name.equals("madison")) {
            return true;
        }
        return false;
    }

    public static  String getHardwareName() {
        String name = MediaContainerApplication.getInstance().getHardwareName();
        if(name == null) {
            name = parseHardwareName();
            MediaContainerApplication.getInstance().setHardwareName(name);
        }
        return name;
    }

    private static String parseHardwareName() {
        String str = null;
        try {
             FileReader reader = new FileReader("/proc/cpuinfo");
             BufferedReader br = new BufferedReader(reader);
             while ((str = br.readLine()) != null) {
                 if (str.indexOf("Hardware") >= 0) {
                     break;
                 }
             }
             if (str != null) {
                 String s[] = str.split(":" , 2);
                 str = s[1].trim().toLowerCase();
             }
             br.close();
             reader.close();
        } catch (Exception e) {
             e.printStackTrace();
        }
        return str;
    }

    private static String getConfigName(String file, String index) {
        String str = null;
        String gConfigName = null;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null) {
                if (str.indexOf(index) >= 0) {
                    break;
                }
            }
            if (str != null) {
                int begin = str.indexOf("\"") + 1;
                int end = str.lastIndexOf("\"");
                if ((begin > 0) && (end > 0)) {
                    gConfigName = str.substring(begin,end);
                    str = null;
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
            return null;
        }
        Log.i(TAG, "gConfigName:" + gConfigName + "; file: " + file + "; index: " + index);
        return gConfigName;
    }

    private static int[] parsePanelOsdSize(String index) {
        String str = null;
        String wPanelWidth = null;
        String wPanelHeight = null;
        int[] config = {0,0};
        try {
            FileReader reader = new FileReader(getConfigName(getConfigName("/config/sys.ini", "gModelName"), "m_pPanelName"));
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null) {
                if (str.indexOf(index + "Width") >= 0) {
                    Log.i(TAG, index + "Width:" + str);
                    int begin = str.indexOf("=") + 1;
                    int end = str.lastIndexOf(";");
                    if ((begin > 0) && (end > 0)) {
                        wPanelWidth = str.substring(begin, end).trim();
                        Log.i(TAG, index +"Width:" + wPanelWidth);
                        config[0] = Integer.parseInt(wPanelWidth);
                    }
                } else if (str.indexOf(index + "Height") >= 0) {
                    Log.i(TAG, index + "Height:" + str);
                    int begin = str.indexOf("=") + 1;
                    int end = str.lastIndexOf(";");
                    if ((begin > 0) && (end > 0)) {
                        wPanelHeight = str.substring(begin, end).trim();
                        Log.i(TAG, index + "Height:" + wPanelHeight);
                        config[1] = Integer.parseInt(wPanelHeight);
                    }
                }
            }
            br.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return config;
        }
        return config;
    }

    public static int[] getPanelSize() {
        int[] config = MediaContainerApplication.getInstance().getPanelSize();
        if(config[0] == 0 || config[1] == 0) {
            config = parsePanelOsdSize("m_wPanel");
            MediaContainerApplication.getInstance().setPanelSize(config);
        }
        return config;
    }

    public static int[] getOsdSize() {
        int[] config = MediaContainerApplication.getInstance().getOsdSize();
        if(config[0] == 0 || config[1] == 0) {
            config = parsePanelOsdSize("osd");
            MediaContainerApplication.getInstance().setOsdSize(config);
        }
        return config;
    }

    private static long parseMem(String index) {
        String str = null;
        long mTotal = 0;
        try {
             FileReader reader = new FileReader("/proc/meminfo");
             BufferedReader br = new BufferedReader(reader);
             while ((str = br.readLine()) != null) {
                 if (str.indexOf(index) >= 0) {
                     break;
                 }
             }
             if (str != null) {
                 int begin = str.indexOf(':');
                 int end = str.indexOf('k');
                 str = str.substring(begin + 1, end).trim();
                 mTotal = Integer.parseInt(str);
             }
             br.close();
             reader.close();
        } catch (Exception e) {
             e.printStackTrace();
        }
        Log.i(TAG, "parse " + index + " : " + mTotal + " kB");
        return mTotal;
    }

    public static long getTotalMem() {
        long total = MediaContainerApplication.getInstance().getTotalMem();
        if(total == -1) {
            total = parseMem("MemTotal");
            MediaContainerApplication.getInstance().setTotalMem(total);
        }
        return total;
    }

    public static long getFreeMem() {
        return parseMem("MemFree");
    }
    public static String getFileName(String sPath) {
        int pos = sPath.lastIndexOf("/");
        String name = sPath.substring(pos+1,sPath.length());
        return name;
    }

    public static String parseUri(Uri intent) {
        if (intent != null) {
            return intent.getPath();
        }
        return null;
    }

    public static boolean isFloatVideoPlayModeOn() {
        int status = SystemProperties.getInt("mstar.floatvideoplaymode", 0);
        if (1 == status) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCurrPlatformSupportThumbnailMode() {
        String platform = getHardwareName();
        if ("napoli".equals(platform)
             || "monaco".equals(platform)) {
            // Currently muji don't support dual mm, so comment out it.
            // || "muji".equals(platform)) {
            return true;
        } else { // Madison & Clippers don't support Multi Thumbnail Mode.
            return false;
        }
    }

    public static boolean isThumbnailModeOn() {
        if (!isCurrPlatformSupportThumbnailMode()) {
            return false;
        }
        int status = SystemProperties.getInt("mstar.thumbnailmode", 0);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isThumbnailMode_SW_On() {
        int status = SystemProperties.getInt("mstar.thumbnail.swmode", 0);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static int getThumbnailNumber() {
        return SystemProperties.getInt("mstar.thumbnail.number", 5);
    }

    public static void setThumbnailMode(String value) {
        if (!isCurrPlatformSupportThumbnailMode()) {
            return;
        }
        SystemProperties.set("mstar.thumbnailmode", value);
    }

    public static boolean showThumbnailTimeStamp() {
        int status = SystemProperties.getInt("mstar.thumbnail.showtime", 0);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean thumbnailSeekBarEnable() {
        int status = SystemProperties.getInt("mstar.thumbnail.seekbar.enable", 1);
        if (status == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPlatformSupportRotate() {
        String platform = getHardwareName();
        if ("napoli".equals(platform)) {
            return true;
        }
        return isRotateModeOn();
    }

    public static boolean isRotateModeOn() {
        boolean status = SystemProperties.getBoolean("mstar.video.rotate", false);
        return  status;
    }

    public static boolean isRotateDisplayAspectRatioModeOn() {
        boolean status = SystemProperties.getBoolean("mstar.video.rotate.aspectratio", false);
        return status;
    }

    public static void setRotateMode(String value) {
        SystemProperties.set("mstar.video.rotate", value);
        if ("0".equalsIgnoreCase(value)) {
            SystemProperties.set("mstar.video.rotate.degrees", value);
        }
    }

    public static int getRotateDegrees() {
        return SystemProperties.getInt("mstar.video.rotate.degrees", 0);
    }

    public static boolean isVideoSWDisplayModeOn() {
        // Check if we're running on Android 5.0 or higher
        // public static final int LOLLIPOP = 21;
        if (Build.VERSION.SDK_INT < 21) {
            return true;
        }
        boolean status = SystemProperties.getBoolean("mstar.video.sw.display", false);
        return  status;
    }

    public static boolean isMstarSTB() {
        String value = SystemProperties.get("mstar.build.for.stb", null);
        if (value != null && value.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public static boolean unSupportTVApi() {
		//Bingo 20160531 add
		boolean isAriesOsd = SystemProperties.getBoolean("mstar.aries.osd", false);
		if (isAriesOsd) {
			return true;
		}
		//end
        String property = SystemProperties.get("mstar.build.mstartv", null);
        if (property != null) {
            if (property.equalsIgnoreCase("ddi")) {
                return true;
            } else if (property.equalsIgnoreCase("mi") && !getHardwareName().equals("clippers")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMstarTV_MI() {
        String property = SystemProperties.get("mstar.build.mstartv", null);
        if (property == null || !property.equalsIgnoreCase("mi")) {
            Log.i(TAG, "-------- unSupportTVApi false");
            return false;
        }
        Log.i(TAG, "-------- unSupportTVApi true");
        return true;
    }

   private static int curLangID = 1;
   public static void setCurrLangID(int nID) {
        curLangID = nID;
   }

   public static int getCurrLangID() {
        return curLangID;
   }

    public static void setSystemProperty(String key , String value) {
        try {
            Class clz = Class.forName("android.os.SystemProperties");
            Method set = clz.getDeclaredMethod("set", String.class,String.class);
            set.invoke(clz, key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSystemProperty(String key , String defValue) {
        try {
            Class clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getDeclaredMethod("get", String.class,String.class);
            return String.valueOf(get.invoke(clz,key,defValue));
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public static void setStreamlessModeOn(boolean streamlessOn) {
        if (streamlessOn) {
            SystemProperties.set("mstar.seamlessplay", "1");
        } else {
            SystemProperties.set("mstar.seamlessplay", "0");
        }
    }

    public static boolean isStreamlessModeOn() {
        int seamstatus = SystemProperties.getInt("mstar.seamlessplay", 0);
        if (seamstatus == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            if (!f1.exists()) {
                return;
            }
            File f2 = new File(dtFile);
            if (!f2.exists()){
                f2.createNewFile();
            }
            FileInputStream in = new FileInputStream(f1);
            FileOutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch(FileNotFoundException ex) {
            Log.d(TAG,"the file cannot be found");
        } catch(IOException e){
            Log.i(TAG,"---IOException---");
        }
    }

    public static boolean getResumePlayState(Uri uri) {
        if (SystemProperties.getInt("mstar.bootinfo", 1) == 0) {
            if (SystemProperties.getInt("mstar.backstat", 0) == 1) {
                String lPath = SystemProperties.get("mstar.path", "");
                String FN = getFileName(uri.getPath());
                Log.i("andrew", "the file name is:" + FN);
                if (lPath.equals(FN)) {
                    return true;
                } else {
                    SystemProperties.set("mstar.path", FN);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            SystemProperties.set("mstar.bootinfo", "0");
            return false;
        }
    }

    public static void setResumePlayState(int state) {
        String sState = state + "";
        SystemProperties.set("mstar.backstat", sState);
    }

    public static void setVideoWindowVisable(boolean isMainWindow, boolean visable) {
        if (unSupportTVApi()) {
            return;
        }
        try {
            PictureManager picManager = TvManager.getInstance().getPictureManager();
            if (isMainWindow) {
                picManager.selectWindow(EnumScalerWindow.E_MAIN_WINDOW);
            } else {
                picManager.selectWindow(EnumScalerWindow.E_SUB_WINDOW);
            }
            if (visable) {
                picManager.setWindowVisible();
            } else {
                picManager.setWindowInvisible();
            }
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public static void setHpBtMainSource(boolean isMainSource) {
        // change earphone and bluetooth to main source or sub source
        if (!unSupportTVApi()) {
            try{
                Log.v(TAG, "setHpBtMainSource isMainSource:"+isMainSource);
                AudioManager audioManager = TvManager.getInstance().getAudioManager();
                if(isMainSource) {
                    audioManager.setAudioCaptureSource(EnumAuidoCaptureDeviceType.E_CAPTURE_DEVICE_TYPE_DEVICE1,
                            EnumAuidoCaptureSource.E_CAPTURE_MAIN_SOUND);
                    audioManager.setOutputSourceInfo((EnumAudioVolumeSourceType.E_VOL_SOURCE_HP_OUT).ordinal(),
                            (EnumAuidoCaptureSource.E_CAPTURE_MAIN_SOUND).ordinal());
                } else {
                    audioManager.setAudioCaptureSource(EnumAuidoCaptureDeviceType.E_CAPTURE_DEVICE_TYPE_DEVICE1,
                            EnumAuidoCaptureSource.E_CAPTURE_SUB_SOUND);
                    audioManager.setOutputSourceInfo((EnumAudioVolumeSourceType.E_VOL_SOURCE_HP_OUT).ordinal(),
                            (EnumAuidoCaptureSource.E_CAPTURE_SUB_SOUND).ordinal());
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setSpeakerMainSource(boolean isMainSource) {
        // change speaker to main source or sub source, not use now
        if (!unSupportTVApi()) {
            try{
                Log.v(TAG, "setSpeakerMainSource isMainSource:"+isMainSource);
                AudioManager audioManager = TvManager.getInstance().getAudioManager();
                if(isMainSource) {
                    audioManager.setOutputSourceInfo((EnumAudioVolumeSourceType.E_VOL_SOURCE_SPEAKER_OUT).ordinal(),
                            (EnumAuidoCaptureSource.E_CAPTURE_MAIN_SOUND).ordinal());
                } else {
                    audioManager.setOutputSourceInfo((EnumAudioVolumeSourceType.E_VOL_SOURCE_SPEAKER_OUT).ordinal(),
                            (EnumAuidoCaptureSource.E_CAPTURE_SUB_SOUND).ordinal());
                }
            } catch (TvCommonException e) {
                e.printStackTrace();
            }
        }
    }

    public static String fixPath(String path) {
        // lollipop5.1 corrsponding to 22, don't need to handle the sign of '#' and '%' after lollipop5.1
        // mantis:0893065,0870320
        if(Build.VERSION.SDK_INT < 22) {
            if (path.indexOf("%") != -1) {
                path = path.replaceAll("%", "%25");
            }
            if (path.indexOf("#") != -1) {
                path = path.replaceAll("#", "%23");
            }
        }
        return path;
    }
}
