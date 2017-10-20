//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.android.providers.tv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.mstar.android.tv.TvCiManager;

public class TvUserSettingProvider extends ContentProvider {
    private static final String TAG = "TvUserSettingProvider";

    private SQLiteDatabase userSettingDB;

    private UserSQLiteDatabase tvUserSettingDB;

    private HandlerThread userThread = new HandlerThread("com.mstar.android.tv.usersetting.handler");

    private Handler userHandler = null;

    private Runnable syncRun = new Runnable() {
        class StreamGobbler extends Thread {
            InputStream is;

            String type;

            public StreamGobbler(InputStream is, String type) {
                this.is = is;
                this.type = type;
            }

            public void run() {
                try {
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        if (type.equals("Error")) {
                            Log.e(TAG, "Error   :" + line);
                        } else {
                            Log.e(TAG, "Debug:" + line);
                        }
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            try {
                Process proc = Runtime.getRuntime().exec("sync");
                StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "Error");
                StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "Output");
                errorGobbler.start();
                outputGobbler.start();
                proc.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static final String AUTHORITY = "mstar.tv.usersetting";

    private static final UriMatcher s_urlMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int dbRefreshCNT = 30;

    private static final int URL_3DInfo = 0;

    private static final int URL_3DSetting = 5;

    private static final int URL_BlockSysSetting = 10;

    private static final int URL_CECSetting = 15;

    private static final int URL_ChinaDVBCSetting = 20;

    private static final int URL_CISetting = 25;

    private static final int URL_CISetting_ID = 26;

    private static final int URL_DB_VERSION = 30;

    private static final int URL_DvbtPresetting = 35;

    private static final int URL_EpgTimer = 40;

    private static final int URL_EpgTimer_ID = 41;

    private static final int URL_FavTypeName = 45;

    private static final int URL_FavTypeName_ID = 46;

    private static final int URL_InputSource_Type = 50;

    private static final int URL_InputSource_Type_ID = 51;

    private static final int URL_IsdbSysSetting = 55;

    private static final int URL_IsdbUserSetting = 60;

    private static final int URL_DvbUserSetting = 61;

    private static final int URL_MediumSetting = 65;

    private static final int URL_MfcMode = 70;

    private static final int URL_Nit_TSInfo = 75;

    private static final int URL_Nit_TSInfo_ID = 76;

    private static final int URL_NitInfo = 80;

    private static final int URL_NitInfo_ID = 81;

    private static final int URL_NRMode = 85;

    private static final int URL_NRMode_ID = 86;

    private static final int URL_OADInfo = 90;

    private static final int URL_OADInfo_UntDescriptor = 95;

    private static final int URL_OADInfo_UntDescriptor_ID = 96;

    private static final int URL_OADWakeUpInfo = 100;

    private static final int URL_PicMode_Setting = 105;

    private static final int URL_PicMode_Setting_ID = 106;

    private static final int URL_PipSetting = 110;

    private static final int URL_SNConfig = 111;

    private static final int URL_SoundModeSetting = 115;

    private static final int URL_SoundModeSetting_ID = 116;

    private static final int URL_SoundSetting = 120;

    private static final int URL_SubtitleSetting = 125;

    private static final int URL_SystemSetting = 130;

    private static final int URL_ThreeDVideoMode = 135;

    private static final int URL_ThreeDVideoMode_ID = 136;

    private static final int URL_ThreeDVideoRouterSetting = 140;

    private static final int URL_ThreeDVideoRouterSetting_ID = 141;

    private static final int URL_TimeSetting = 145;

    private static final int URL_UserColorTemp = 150;

    private static final int URL_UserColorTempEx = 155;

    private static final int URL_UserColorTempEx_ID = 156;

    private static final int URL_UserLocationSetting = 160;

    private static final int URL_UserMMSetting = 165;

    private static final int URL_UserOverScanMode = 170;

    private static final int URL_UserOverScanMode_ID = 171;

    private static final int URL_UserPCModeSetting = 175;

    private static final int URL_UserPCModeSetting_ID = 176;

    private static final int URL_VideoSetting = 180;

    private static final int URL_VideoSetting_ID = 181;

    private static final int URL_VChipMpaaItem_ID = 185;

    private static final int URL_VChipSetting_ID = 190;

    private static final int URL_MiscSetting_ID = 195;

    private static final int URL_VChipRatingInfo_ID = 200;

    private static final int URL_Regin5DimensionInfo_ID = 205;

    private static final int URL_AbbRatingText_ID = 210;

    private static final int URL_RR5RatingPair_ID = 215;

    private static final int URL_CCAdvancedSetting_ID = 220;

    private static final int URL_CCSetting_ID = 225;

    private static final int URL_AndroidConfig_ID = 230;

    private static final int URL_BootSetting = 235;

    private String _3DInfo = "tbl_3DInfo";

    private String _3DSetting = "tbl_3DSetting";

    private String BlockSysSetting = "tbl_BlockSysSetting";

    private String CECSetting = "tbl_CECSetting";

    private String ChinaDVBCSetting = "tbl_ChinaDVBCSetting";

    private String CISetting = "tbl_CISetting";

    private String DB_VERSION = "tbl_DB_VERSION";

    private String DvbtPresetting = "tbl_DvbtPresetting";

    private String EpgTimer = "tbl_EpgTimer";

    private String FavTypeName = "tbl_FavTypeName";

    private String InputSource_Type = "tbl_InputSource_Type";

    private String IsdbSysSetting = "tbl_IsdbSysSetting";

    private String IsdbUserSetting = "tbl_IsdbUserSetting";

    private String DvbUserSetting = "tbl_DvbUserSetting";

    private String MediumSetting = "tbl_MediumSetting";

    private String MfcMode = "tbl_MfcMode";

    private String Nit_TSInfo = "tbl_Nit_TSInfo";

    private String NitInfo = "tbl_NitInfo";

    private String NRMode = "tbl_NRMode";

    private String OADInfo = "tbl_OADInfo";

    private String OADInfo_UntDescriptor = "tbl_OADInfo_UntDescriptor";

    private String OADWakeUpInfo = "tbl_OADWakeUpInfo";

    private String PicMode_Setting = "tbl_PicMode_Setting";

    private String PipSetting = "tbl_PipSetting";

    private String SNConfig = "tbl_SNConfig";

    private String SoundModeSetting = "tbl_SoundModeSetting";

    private String SoundSetting = "tbl_SoundSetting";

    private String SubtitleSetting = "tbl_SubtitleSetting";

    private String SystemSetting = "tbl_SystemSetting";

    private String ThreeDVideoMode = "tbl_ThreeDVideoMode";

    private String ThreeDVideoRouterSetting = "tbl_ThreeDVideoRouterSetting";

    private String TimeSetting = "tbl_TimeSetting";

    private String UserColorTemp = "tbl_UserColorTemp";

    private String UserColorTempEx = "tbl_UserColorTempEx";

    private String UserLocationSetting = "tbl_UserLocationSetting";

    private String UserMMSetting = "tbl_UserMMSetting";

    private String UserOverScanMode = "tbl_UserOverScanMode";

    private String UserPCModeSetting = "tbl_UserPCModeSetting";

    private String VideoSetting = "tbl_VideoSetting";

    private String VChipMpaaItem = "tbl_VChipMpaaItem";

    private String VChipSetting = "tbl_VChipSetting";

    private String MiscSetting = "tbl_MiscSetting";

    private String VChipRatingInfo = "tbl_VChipRatingInfo";

    private String Regin5DimensionInfo = "tbl_Regin5DimensionInfo";

    private String AbbRatingText = "tbl_AbbRatingText";

    private String RR5RatingPair = "tbl_RR5RatingPair";

    private String CCSetting = "tbl_CCSetting";

    private String CCAdvancedSetting = "tbl_CCAdvancedSetting";

    private String AndroidConfig = "tbl_AndroidConfig";

    private String BootSetting = "tbl_BootSetting";

    static {
        s_urlMatcher.addURI(AUTHORITY, "3dinfo", URL_3DInfo);
        s_urlMatcher.addURI(AUTHORITY, "3dsetting", URL_3DSetting);
        s_urlMatcher.addURI(AUTHORITY, "blocksyssetting", URL_BlockSysSetting);
        s_urlMatcher.addURI(AUTHORITY, "cecsetting", URL_CECSetting);
        s_urlMatcher.addURI(AUTHORITY, "chinadvbcsetting", URL_ChinaDVBCSetting);
        s_urlMatcher.addURI(AUTHORITY, "cisetting", URL_CISetting);
        s_urlMatcher.addURI(AUTHORITY, "cisetting/#", URL_CISetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "db_version", URL_DB_VERSION);
        s_urlMatcher.addURI(AUTHORITY, "dvbtpresetting", URL_DvbtPresetting);
        s_urlMatcher.addURI(AUTHORITY, "epgtimer", URL_EpgTimer);
        s_urlMatcher.addURI(AUTHORITY, "epgtimer/#", URL_EpgTimer_ID);
        s_urlMatcher.addURI(AUTHORITY, "favtypename", URL_FavTypeName);
        s_urlMatcher.addURI(AUTHORITY, "favtypename/typeid/#", URL_FavTypeName_ID);
        s_urlMatcher.addURI(AUTHORITY, "inputsource_type", URL_InputSource_Type);
        s_urlMatcher.addURI(AUTHORITY, "inputsource_type/#", URL_InputSource_Type_ID);
        s_urlMatcher.addURI(AUTHORITY, "isdbsyssetting", URL_IsdbSysSetting);
        s_urlMatcher.addURI(AUTHORITY, "isdbusersetting", URL_IsdbUserSetting);
        s_urlMatcher.addURI(AUTHORITY, "dvbusersetting", URL_DvbUserSetting);
        s_urlMatcher.addURI(AUTHORITY, "mediumsetting", URL_MediumSetting);
        s_urlMatcher.addURI(AUTHORITY, "mfcmode", URL_MfcMode);
        s_urlMatcher.addURI(AUTHORITY, "nit_tsinfo", URL_Nit_TSInfo);
        s_urlMatcher.addURI(AUTHORITY, "nit_tsinfo/nit_id/#/id/#", URL_Nit_TSInfo_ID);
        s_urlMatcher.addURI(AUTHORITY, "nitinfo", URL_NitInfo);
        s_urlMatcher.addURI(AUTHORITY, "nitinfo/#", URL_NitInfo_ID);
        s_urlMatcher.addURI(AUTHORITY, "nrmode", URL_NRMode);
        s_urlMatcher.addURI(AUTHORITY, "nrmode/nrmode/#/inputsrc/#", URL_NRMode_ID);
        s_urlMatcher.addURI(AUTHORITY, "oadinfo", URL_OADInfo);
        s_urlMatcher.addURI(AUTHORITY, "oadinfo_untdescriptor", URL_OADInfo_UntDescriptor);
        s_urlMatcher.addURI(AUTHORITY, "oadinfo_untdescriptor/#", URL_OADInfo_UntDescriptor_ID);
        s_urlMatcher.addURI(AUTHORITY, "oadwakeupinfo", URL_OADWakeUpInfo);
        s_urlMatcher.addURI(AUTHORITY, "picmode_setting", URL_PicMode_Setting);
        s_urlMatcher.addURI(AUTHORITY, "picmode_setting/inputsrc/#/picmode/#",
                URL_PicMode_Setting_ID);
        s_urlMatcher.addURI(AUTHORITY, "pipsetting", URL_PipSetting);
        s_urlMatcher.addURI(AUTHORITY, "snconfig", URL_SNConfig);
        s_urlMatcher.addURI(AUTHORITY, "soundmodesetting", URL_SoundModeSetting);
        s_urlMatcher.addURI(AUTHORITY, "soundmodesetting/#", URL_SoundModeSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "soundsetting", URL_SoundSetting);
        s_urlMatcher.addURI(AUTHORITY, "subtitlesetting", URL_SubtitleSetting);
        s_urlMatcher.addURI(AUTHORITY, "systemsetting", URL_SystemSetting);
        s_urlMatcher.addURI(AUTHORITY, "threedvideomode", URL_ThreeDVideoMode);
        s_urlMatcher.addURI(AUTHORITY, "threedvideomode/inputsrc/#", URL_ThreeDVideoMode_ID);
        s_urlMatcher.addURI(AUTHORITY, "threedvideoroutersetting", URL_ThreeDVideoRouterSetting);
        s_urlMatcher.addURI(AUTHORITY, "threedvideoroutersetting/e3dtype/#",
                URL_ThreeDVideoRouterSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "timesetting", URL_TimeSetting);
        s_urlMatcher.addURI(AUTHORITY, "usercolortemp", URL_UserColorTemp);
        s_urlMatcher.addURI(AUTHORITY, "usercolortempex", URL_UserColorTempEx);
        s_urlMatcher.addURI(AUTHORITY, "usercolortempex/#", URL_UserColorTempEx_ID);
        s_urlMatcher.addURI(AUTHORITY, "userlocationsetting", URL_UserLocationSetting);
        s_urlMatcher.addURI(AUTHORITY, "usermmsetting", URL_UserMMSetting);
        s_urlMatcher.addURI(AUTHORITY, "useroverscanmode", URL_UserOverScanMode);
        s_urlMatcher.addURI(AUTHORITY, "useroverscanmode/inputsrc/#", URL_UserOverScanMode_ID);
        s_urlMatcher.addURI(AUTHORITY, "userpcmodesetting", URL_UserPCModeSetting);
        s_urlMatcher.addURI(AUTHORITY, "userpcmodesetting/#", URL_UserPCModeSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "videosetting", URL_VideoSetting);
        s_urlMatcher.addURI(AUTHORITY, "videosetting/inputsrc/#", URL_VideoSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "vchipmpaaitem", URL_VChipMpaaItem_ID);
        s_urlMatcher.addURI(AUTHORITY, "vchipsetting", URL_VChipSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "miscsetting", URL_MiscSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "vchipratinginfo", URL_VChipRatingInfo_ID);
        s_urlMatcher.addURI(AUTHORITY, "regin5dimensioninfo", URL_Regin5DimensionInfo_ID);
        s_urlMatcher.addURI(AUTHORITY, "abbratingtext/#/#", URL_AbbRatingText_ID);
        s_urlMatcher.addURI(AUTHORITY, "rr5ratingpair/#", URL_RR5RatingPair_ID);
        s_urlMatcher.addURI(AUTHORITY, "ccsetting", URL_CCSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "ccadvancedsetting/#", URL_CCAdvancedSetting_ID);
        s_urlMatcher.addURI(AUTHORITY, "androidconfig", URL_AndroidConfig_ID);
        s_urlMatcher.addURI(AUTHORITY, "bootsetting", URL_BootSetting);
    }

    @Override
    public boolean onCreate() {
        openDB();
        userThread.start();
        userHandler = new Handler(userThread.getLooper());
        tvUserSettingDB = new UserSQLiteDatabase(userSettingDB);
        return (userSettingDB == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        if (tvUserSettingDB.isUriDirty(uri)) {
            tvUserSettingDB.flush();
        }

        int match = s_urlMatcher.match(uri);
        switch (match) {
            case URL_3DInfo:
                qb.setTables(_3DInfo);
                break;
            case URL_3DSetting:
                qb.setTables(_3DSetting);
                break;
            case URL_BlockSysSetting:
                qb.setTables(BlockSysSetting);
                break;
            case URL_CECSetting:
                qb.setTables(CECSetting);
                break;
            case URL_ChinaDVBCSetting:
                qb.setTables(ChinaDVBCSetting);
                break;
            case URL_CISetting:
                qb.setTables(CISetting);
                break;
            case URL_CISetting_ID:
                qb.setTables(CISetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_DB_VERSION:
                qb.setTables(DB_VERSION);
                break;
            case URL_DvbtPresetting:
                qb.setTables(DvbtPresetting);
                break;
            case URL_EpgTimer:
                qb.setTables(EpgTimer);
                break;
            case URL_EpgTimer_ID:
                qb.setTables(EpgTimer);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_FavTypeName:
                qb.setTables(FavTypeName);
                break;
            case URL_FavTypeName_ID:
                qb.setTables(FavTypeName);
                qb.appendWhere("TypeId = " + uri.getLastPathSegment());
                break;
            case URL_InputSource_Type:
                qb.setTables(InputSource_Type);
                break;
            case URL_InputSource_Type_ID:
                qb.setTables(InputSource_Type);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_IsdbSysSetting:
                qb.setTables(IsdbSysSetting);
                break;
            case URL_IsdbUserSetting:
                qb.setTables(IsdbUserSetting);
                break;
            case URL_DvbUserSetting:
                qb.setTables(DvbUserSetting);
                break;
            case URL_MediumSetting:
                qb.setTables(MediumSetting);
                break;
            case URL_MfcMode:
                qb.setTables(MfcMode);
                break;
            case URL_Nit_TSInfo:
                qb.setTables(Nit_TSInfo);
                break;
            case URL_Nit_TSInfo_ID:
                qb.setTables(Nit_TSInfo);
                qb.appendWhere("_id = " + uri.getLastPathSegment() + " and _NIT_id = "
                        + uri.getPathSegments().get(2));
                break;
            case URL_NitInfo:
                qb.setTables(NitInfo);
                break;
            case URL_NitInfo_ID:
                qb.setTables(NitInfo);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_NRMode:
                qb.setTables(NRMode);
                break;
            case URL_NRMode_ID:
                qb.setTables(NRMode);
                qb.appendWhere("NRMode = " + uri.getPathSegments().get(2) + " and InputSrcType = "
                        + uri.getLastPathSegment());
                break;
            case URL_OADInfo:
                qb.setTables(OADInfo);
                break;
            case URL_OADInfo_UntDescriptor:
                qb.setTables(OADInfo_UntDescriptor);
                break;
            case URL_OADInfo_UntDescriptor_ID:
                qb.setTables(OADInfo_UntDescriptor);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_OADWakeUpInfo:
                qb.setTables(OADWakeUpInfo);
                break;
            case URL_PicMode_Setting:
                qb.setTables(PicMode_Setting);
                break;
            case URL_PicMode_Setting_ID:
                qb.setTables(PicMode_Setting);
                qb.appendWhere("InputSrcType = " + uri.getPathSegments().get(2)
                        + " and PictureModeType = " + uri.getLastPathSegment());
                break;
            case URL_PipSetting:
                qb.setTables(PipSetting);
                break;
            case URL_SNConfig:
                qb.setTables(SNConfig);
                break;
            case URL_SoundModeSetting:
                qb.setTables(SoundModeSetting);
                break;
            case URL_SoundModeSetting_ID:
                qb.setTables(SoundModeSetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_SoundSetting:
                qb.setTables(SoundSetting);
                break;
            case URL_SubtitleSetting:
                qb.setTables(SubtitleSetting);
                break;
            case URL_SystemSetting:
                /*
                 * For supporting block power-off when CI is occuiped tuner for
                 * upgrading, we will query isCiOccupiedTuner() here, it will
                 * implicit update bCiOccupiedTuner flag within
                 * content://mstar.tv.usersetting/systemsetting.
                 */
                if (projection != null) {
                    for (String str : projection) {
                        if (str.equals("bCiOccupiedTuner")) {
                            boolean bCiOccupiedTuner = TvCiManager.getInstance().isCiOccupiedTuner(
                                    false);
                            Log.i(TAG, "bCiOccupiedTuner = " + bCiOccupiedTuner);
                        }
                    }
                }
                qb.setTables(SystemSetting);
                break;
            case URL_ThreeDVideoMode:
                qb.setTables(ThreeDVideoMode);
                break;
            case URL_ThreeDVideoMode_ID:
                qb.setTables(ThreeDVideoMode);
                qb.appendWhere("InputSrcType = " + uri.getLastPathSegment());
                break;
            case URL_ThreeDVideoRouterSetting:
                qb.setTables(ThreeDVideoRouterSetting);
                break;
            case URL_ThreeDVideoRouterSetting_ID:
                qb.setTables(ThreeDVideoRouterSetting);
                qb.appendWhere("e3DType = " + uri.getLastPathSegment());
                break;
            case URL_TimeSetting:
                qb.setTables(TimeSetting);
                break;
            case URL_UserColorTemp:
                qb.setTables(UserColorTemp);
                break;
            case URL_UserColorTempEx:
                qb.setTables(UserColorTempEx);
                break;
            case URL_UserColorTempEx_ID:
                qb.setTables(UserColorTempEx);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_UserLocationSetting:
                qb.setTables(UserLocationSetting);
                break;
            case URL_UserMMSetting:
                qb.setTables(UserMMSetting);
                break;
            case URL_UserOverScanMode:
                qb.setTables(UserOverScanMode);
                break;
            case URL_UserOverScanMode_ID:
                qb.setTables(UserOverScanMode);
                qb.appendWhere("InputSrcType = " + uri.getLastPathSegment());
                break;
            case URL_UserPCModeSetting:
                qb.setTables(UserPCModeSetting);
                break;
            case URL_UserPCModeSetting_ID:
                qb.setTables(UserPCModeSetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_VideoSetting:
                qb.setTables(VideoSetting);
                break;
            case URL_VideoSetting_ID:
                qb.setTables(VideoSetting);
                qb.appendWhere("InputSrcType = " + uri.getLastPathSegment());
                break;
            case URL_VChipMpaaItem_ID:
                qb.setTables(VChipMpaaItem);
                break;
            case URL_VChipSetting_ID:
                qb.setTables(VChipSetting);
                break;
            case URL_MiscSetting_ID:
                qb.setTables(MiscSetting);
                break;
            case URL_VChipRatingInfo_ID:
                qb.setTables(VChipRatingInfo);
                break;
            case URL_Regin5DimensionInfo_ID:
                qb.setTables(Regin5DimensionInfo);
                break;
            case URL_AbbRatingText_ID:
                qb.setTables(AbbRatingText);
                qb.appendWhere("_id >= " + uri.getPathSegments().get(1) + " and _id <"
                        + uri.getLastPathSegment());
                break;
            case URL_RR5RatingPair_ID:
                qb.setTables(RR5RatingPair);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_CCSetting_ID:
                qb.setTables(CCSetting);
                break;
            case URL_CCAdvancedSetting_ID:
                qb.setTables(CCAdvancedSetting);
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;
            case URL_AndroidConfig_ID:
                qb.setTables(AndroidConfig);
                break;
            case URL_BootSetting:
                qb.setTables(BootSetting);
                break;
        }
        Cursor c = qb.query(userSettingDB, projection, selection, selectionArgs, null, null,
                sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        return -1;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        int count = 0;
        int match = s_urlMatcher.match(uri);
        switch (match) {
            case URL_3DInfo:
                count = tvUserSettingDB.update(uri, _3DInfo, values, where, whereArgs);
                break;
            case URL_3DSetting:
                count = tvUserSettingDB.update(uri, _3DSetting, values, where, whereArgs);
                break;
            case URL_BlockSysSetting:
                count = tvUserSettingDB.update(uri, BlockSysSetting, values, where, whereArgs);
                break;
            case URL_CECSetting:
                count = tvUserSettingDB.update(uri, CECSetting, values, where, whereArgs);
                break;
            case URL_ChinaDVBCSetting:
                count = tvUserSettingDB.update(uri, ChinaDVBCSetting, values, where, whereArgs);
                break;
            case URL_CISetting:
                count = tvUserSettingDB.update(uri, CISetting, values, where, whereArgs);
                break;
            case URL_CISetting_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, CISetting, values, "_id=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            case URL_DB_VERSION:
                count = tvUserSettingDB.update(uri, DB_VERSION, values, where, whereArgs);
                break;
            case URL_DvbtPresetting:
                count = tvUserSettingDB.update(uri, DvbtPresetting, values, where, whereArgs);
                break;
            case URL_EpgTimer:
                count = tvUserSettingDB.update(uri, EpgTimer, values, where, whereArgs);
                break;
            case URL_EpgTimer_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, EpgTimer, values, "_id=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            case URL_FavTypeName:
                count = tvUserSettingDB.update(uri, FavTypeName, values, where, whereArgs);
                break;
            case URL_FavTypeName_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, FavTypeName, values, "TypeId=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            case URL_InputSource_Type:
                count = tvUserSettingDB.update(uri, InputSource_Type, values, where, whereArgs);
                break;
            case URL_InputSource_Type_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, InputSource_Type, values, "_id=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_IsdbSysSetting:
                count = tvUserSettingDB.update(uri, IsdbSysSetting, values, where, whereArgs);
                break;
            case URL_IsdbUserSetting:
                count = tvUserSettingDB.update(uri, IsdbUserSetting, values, where, whereArgs);
                break;
            case URL_DvbUserSetting:
                count = tvUserSettingDB.update(uri, DvbUserSetting, values, where, whereArgs);
                break;
            case URL_MediumSetting:
                count = tvUserSettingDB.update(uri, MediumSetting, values, where, whereArgs);
                break;
            case URL_MfcMode:
                count = tvUserSettingDB.update(uri, MfcMode, values, where, whereArgs);
                break;
            case URL_Nit_TSInfo:
                count = tvUserSettingDB.update(uri, Nit_TSInfo, values, where, whereArgs);
                break;
            case URL_Nit_TSInfo_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, Nit_TSInfo, values, "_id=? and _NIT_id=?",
                        new String[] {
                                uri.getLastPathSegment(), uri.getPathSegments().get(2)
                        });
                break;
            case URL_NitInfo:
                count = tvUserSettingDB.update(uri, NitInfo, values, where, whereArgs);
                break;
            case URL_NitInfo_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, NitInfo, values, "_id=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            case URL_NRMode:
                count = tvUserSettingDB.update(uri, NRMode, values, where, whereArgs);
                break;
            case URL_NRMode_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, NRMode, values, "NRMode=? and InputSrcType=?",
                        new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            case URL_OADInfo:
                count = tvUserSettingDB.update(uri, OADInfo, values, where, whereArgs);
                break;
            case URL_OADInfo_UntDescriptor:
                count = tvUserSettingDB
                        .update(uri, OADInfo_UntDescriptor, values, where, whereArgs);
                break;
            case URL_OADInfo_UntDescriptor_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, OADInfo_UntDescriptor, values, "_id=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_OADWakeUpInfo:
                count = tvUserSettingDB.update(uri, OADWakeUpInfo, values, where, whereArgs);
                break;
            case URL_PicMode_Setting:
                count = tvUserSettingDB.update(uri, PicMode_Setting, values, where, whereArgs);
                break;
            case URL_PicMode_Setting_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, PicMode_Setting, values,
                        "InputSrcType=? and PictureModeType=?", new String[] {
                                uri.getPathSegments().get(2), uri.getLastPathSegment()
                        });
                break;
            case URL_PipSetting:
                count = tvUserSettingDB.update(uri, PipSetting, values, where, whereArgs);
                break;
            case URL_SNConfig:
                count = tvUserSettingDB.update(uri, SNConfig, values, where, whereArgs);
                break;
            case URL_SoundModeSetting:
                count = tvUserSettingDB.update(uri, SoundModeSetting, values, where, whereArgs);
                break;
            case URL_SoundModeSetting_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, SoundModeSetting, values, "_id=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_SoundSetting:
                count = tvUserSettingDB.update(uri, SoundSetting, values, where, whereArgs);
                break;
            case URL_SubtitleSetting:
                count = tvUserSettingDB.update(uri, SubtitleSetting, values, where, whereArgs);
                break;
            case URL_SystemSetting:
                count = tvUserSettingDB.update(uri, SystemSetting, values, where, whereArgs);
                break;
            case URL_ThreeDVideoMode:
                count = tvUserSettingDB.update(uri, ThreeDVideoMode, values, where, whereArgs);
                break;
            case URL_ThreeDVideoMode_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, ThreeDVideoMode, values, "InputSrcType=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_ThreeDVideoRouterSetting:
                count = tvUserSettingDB.update(uri, ThreeDVideoRouterSetting, values, where,
                        whereArgs);
                break;
            case URL_ThreeDVideoRouterSetting_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, ThreeDVideoRouterSetting, values, "e3DType=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_TimeSetting:
                count = tvUserSettingDB.update(uri, TimeSetting, values, where, whereArgs);
                break;
            case URL_UserColorTemp:
                count = tvUserSettingDB.update(uri, UserColorTemp, values, where, whereArgs);
                break;
            case URL_UserColorTempEx:
                count = tvUserSettingDB.update(uri, UserColorTempEx, values, where, whereArgs);
                break;
            case URL_UserColorTempEx_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, UserColorTempEx, values, "_id=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            case URL_UserLocationSetting:
                count = tvUserSettingDB.update(uri, UserLocationSetting, values, where, whereArgs);
                break;
            case URL_UserMMSetting:
                count = tvUserSettingDB.update(uri, UserMMSetting, values, where, whereArgs);
                break;
            case URL_UserOverScanMode:
                count = tvUserSettingDB.update(uri, UserOverScanMode, values, where, whereArgs);
                break;
            case URL_UserOverScanMode_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, UserOverScanMode, values, "InputSrcType=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_UserPCModeSetting:
                count = tvUserSettingDB.update(uri, UserPCModeSetting, values, where, whereArgs);
                break;
            case URL_UserPCModeSetting_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, UserPCModeSetting, values, "_id=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_VideoSetting:
                count = tvUserSettingDB.update(uri, VideoSetting, values, where, whereArgs);
                break;
            case URL_VideoSetting_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, VideoSetting, values, "InputSrcType=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_VChipMpaaItem_ID:
                count = tvUserSettingDB.update(uri, VChipMpaaItem, values, where, whereArgs);
                break;
            case URL_VChipSetting_ID:
                count = tvUserSettingDB.update(uri, VChipSetting, values, where, whereArgs);
                break;
            case URL_MiscSetting_ID:
                count = tvUserSettingDB.update(uri, MiscSetting, values, where, whereArgs);
                break;
            case URL_VChipRatingInfo_ID:
                count = tvUserSettingDB.update(uri, VChipRatingInfo, values, where, whereArgs);
                break;
            case URL_RR5RatingPair_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, RR5RatingPair, values, "_id=?", new String[] {
                    uri.getLastPathSegment()
                });
                break;
            case URL_CCSetting_ID:
                count = tvUserSettingDB.update(uri, CCSetting, values, where, whereArgs);
                break;
            case URL_CCAdvancedSetting_ID:
                if (where != null || whereArgs != null) {
                    throw new UnsupportedOperationException("Cannot update URL " + uri
                            + " with a where clause");
                }
                count = tvUserSettingDB.update(uri, CCAdvancedSetting, values, "_id=?",
                        new String[] {
                            uri.getLastPathSegment()
                        });
                break;
            case URL_AndroidConfig_ID:
                count = tvUserSettingDB.update(uri, AndroidConfig, values, where, whereArgs);
                break;
            case URL_BootSetting:
                count = tvUserSettingDB.update(uri, BootSetting, values, where, whereArgs);
                break;
        }
        return count;
    }

    private void openDB() {
        int retry = 0;
        if ((userSettingDB == null) || (!userSettingDB.isOpen())) {
            Log.e(TAG, "open db start");
            while (retry < 20) {
                try {
                    userSettingDB = SQLiteDatabase.openDatabase(
                            "/tvdatabase/Database/user_setting.db", null,
                            SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
                } catch (SQLiteException e) {
                    Log.e(TAG, "open db fail,retry ...");
                    retry++;
                    userSettingDB = null;
                    if (retry >= 20) {
                        Log.e(TAG, "open db fail,Please check sw bug!");
                    }
                } finally {
                    if (userSettingDB != null) {
                        Log.e(TAG, "open db success");
                        retry = 20;
                    }
                }
            }
        }
    }

    private void closeDB() {
        userSettingDB.endTransaction();
        userSettingDB.close();
    }

    @Override
    public void shutdown() {
        Log.e(TAG, "now shutdown");
        super.shutdown();
        userThread.quit();
        closeDB();
    }

    private class UserSQLiteDatabase {
        private final boolean isQueueReady = true; // not ready now, so no usage

        private SQLiteDatabase userDB = null;

        private keptData preURI = new keptData();

        private keptData curURI = new keptData();

        private long ret = -1;

        private int syncCount = 0; // will write db every 10 times

        private Runnable queueRun = new Runnable() { // queue the current data

            @Override
            public void run() {
                synchronized (this) {
                    syncCount = 0;
                    if (curURI.uri == null)
                        return;
                    doDataUpdate(curURI.uri, curURI.table, curURI.values, curURI.whereClause,
                            curURI.whereArgs);
                    curURI.setData(null, null, null, null, null);
                }
            }
        };

        private class keptData {
            public Uri uri = null;

            public String table = null;

            public ContentValues values = null;

            public String whereClause = null;

            public String[] whereArgs = null;

            public void setData(Uri uri, String table, ContentValues values, String whereClause,
                    String[] whereArgs) {
                this.uri = uri;
                this.table = table;
                this.values = values;
                this.whereClause = whereClause;
                this.whereArgs = whereArgs;
            }
        }

        UserSQLiteDatabase(SQLiteDatabase db) {
            userDB = db;
        }

        public boolean isUriDirty(final Uri uri) {
            synchronized (this) {
                if (curURI.uri != null) {
                    return curURI.uri.equals(uri);
                } else {
                    return false;
                }
            }
        }

        public void flush() {
            userHandler.removeCallbacks(queueRun);
            synchronized (this) {
                syncCount = 0;
                if (curURI.uri == null)
                    return;
                doDataUpdate(curURI.uri, curURI.table, curURI.values, curURI.whereClause,
                        curURI.whereArgs);
                curURI.setData(null, null, null, null, null);
            }
        }

        private boolean equalsKey(ContentValues value1, ContentValues value2) {
            boolean result = false;
            String s1 = value1.toString();
            String s2 = value2.toString();

            String[] c1 = s1.split(" ");
            String[] c2 = s2.split(" ");
            if (c1.length != c2.length) {
                return result;
            } else {
                for (int i = 0; i < c1.length; i++) {
                    String k1 = c1[i].split("=")[0];
                    String k2 = c2[i].split("=")[0];
                    if (!k1.equals(k2)) {
                        return result;
                    }
                }
                result = true;
                return result;
            }
        }

        private boolean isNeedQueue(String table) {
            if (isQueueReady) {
                return table.equals("tbl_PicMode_Setting") || table.equals("tbl_SoundSetting")
                        || table.equals("tbl_SoundModeSetting");
            } else {
                return false;
            }
        }

        public int update(final Uri uri, final String table, final ContentValues values,
                final String whereClause, final String[] whereArgs) {
            if (isNeedQueue(table)) {
                synchronized (this) {
                    preURI.uri = curURI.uri;
                    preURI.table = curURI.table;
                    preURI.values = curURI.values;
                    preURI.whereArgs = curURI.whereArgs;
                    preURI.whereClause = curURI.whereClause;
                    curURI.setData(uri, table, values, whereClause, whereArgs);
                    if (!(curURI.uri.equals(preURI.uri) && equalsKey(curURI.values, preURI.values))
                            || syncCount == 10) {
                        if (preURI.uri != null) { // update the previous data
                            userHandler.removeCallbacks(queueRun);
                            doDataUpdate(preURI.uri, preURI.table, preURI.values,
                                    preURI.whereClause, preURI.whereArgs);
                        }
                        syncCount = 0;
                        userHandler.postDelayed(queueRun, 300);
                    } else { // reset post timer
                        syncCount++;
                        userHandler.removeCallbacks(queueRun);
                        userHandler.postDelayed(queueRun, 300);
                    }
                }
            } else {
                // if (userDB.update(table, values, whereClause, whereArgs) > 0)
                // getContext().getContentResolver().notifyChange(uri, null);
                doDataUpdate(uri, table, values, whereClause, whereArgs);
            }
            return 1;
        }

        private void doDataUpdate(Uri uri, String table, ContentValues values, String whereClause,
                String[] whereArgs) {
            Log.e(TAG, "updating " + uri.toString() + ", value = " + values);
            try {
                ret = userDB.update(table, values, whereClause, whereArgs);
            } catch (SQLException e) {
                ret = -1;
            }
            if (ret < 0) {
                UserSQLiteThread userSQLiteThread = new UserSQLiteThread(userDB, uri, table,
                        values, whereClause, whereArgs);
                userSQLiteThread.start();
            } else {
                getContext().getContentResolver().notifyChange(uri, null);
                userHandler.removeCallbacks(syncRun);
                userHandler.post(syncRun);
            }
        }
    }

    private class UserSQLiteThread extends Thread {
        long ret = -1;

        int redetect = 0;

        SQLiteDatabase userDB = null;

        Uri userURI = null;

        String userTable = null;

        ContentValues userValues = null;

        String userWhereClause = null;

        String[] userWhereArgs = null;

        public UserSQLiteThread(SQLiteDatabase db, Uri uri, String table, ContentValues values,
                String whereClause, String[] whereArgs) {
            userDB = db;
            userURI = uri;
            userTable = table;
            userValues = values;
            userWhereClause = whereClause;
            userWhereArgs = whereArgs;
        }

        public void run() {
            while (ret < 0 && redetect < dbRefreshCNT) {
                try {
                    Thread.sleep(50);
                    ret = userDB.update(userTable, userValues, userWhereClause, userWhereArgs);
                    redetect++;
                } catch (InterruptedException e) {
                } catch (SQLException e) {
                    ret = -1;
                }
            }
            if (ret < 0) {
                Log.e(TAG, "update usersetting db fail !!!");
            } else {
                getContext().getContentResolver().notifyChange(userURI, null);
                userHandler.removeCallbacks(syncRun);
                userHandler.post(syncRun);
            }
        }
    }
}
