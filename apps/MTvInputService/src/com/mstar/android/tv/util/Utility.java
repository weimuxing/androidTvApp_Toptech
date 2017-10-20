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

package com.mstar.android.tv.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.tv.TvTrackInfo;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.SystemProperties;
import android.util.Log;

import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.tunersetup.TunerSetupActivity;
import com.mstar.android.tv.inputservice.R;

import java.util.Arrays;

public class Utility {
    private static final String TAG = "Utility";

    private static final String PREFERENCES_TITLE = "TVINPUTSERVICE_PREFERENCES";

    private static int sTvSystem = -1;

    private static int[] sRouteTable = null;

    private static String[] sRouteTableName = null;

    public static int getCurrentTvSystem() {
        if (sTvSystem < 0) {
            sTvSystem = TvCommonManager.getInstance().getCurrentTvSystem();
        }
        return sTvSystem;
    }

    public static boolean isATSC() {
        if (sTvSystem > 0) {
            return TvCommonManager.TV_SYSTEM_ATSC == sTvSystem;
        }
        return TvCommonManager.TV_SYSTEM_ATSC == getCurrentTvSystem();
    }

    public static boolean isISDB() {
        if (sTvSystem > 0) {
            return TvCommonManager.TV_SYSTEM_ISDB == sTvSystem;
        }
        return TvCommonManager.TV_SYSTEM_ISDB == getCurrentTvSystem();
    }

    public static int[] getRouteTable() {
        if (null == sRouteTable) {
            switch (getCurrentTvSystem()) {
                case TvCommonManager.TV_SYSTEM_DVBT:
                case TvCommonManager.TV_SYSTEM_DVBC:
                case TvCommonManager.TV_SYSTEM_DVBS:
                case TvCommonManager.TV_SYSTEM_DVBT2:
                case TvCommonManager.TV_SYSTEM_DVBS2:
                case TvCommonManager.TV_SYSTEM_DTMB:
                case TvCommonManager.TV_SYSTEM_ISDB:
                    TvTypeInfo tvinfo = TvCommonManager.getInstance().getTvInfo();
                    sRouteTable = Arrays.copyOf(tvinfo.routePath, tvinfo.routePath.length);
                    break;
                case TvCommonManager.TV_SYSTEM_ATSC:
                    sRouteTable = new int[] {TvChannelManager.DTV_ANTENNA_TYPE_NONE, TvChannelManager.DTV_ANTENNA_TYPE_AIR, TvChannelManager.DTV_ANTENNA_TYPE_CABLE};
                    break;
                default:
                    break;
            }
        }
        return sRouteTable;
    }

    public static String[] getRouteTableName(Context context) {
        if (null == sRouteTableName) {
            int[] routeTable = getRouteTable();

            if (0 < routeTable.length) {
                sRouteTableName = new String[routeTable.length];
                /* Fill the literal representation of tv route */
                for (int i = 0; i < sRouteTableName.length; ++i ) {
                    sRouteTableName[i] = tvRouteToString(context, routeTable[i]);
                }
            } else {
                sRouteTableName = new String[] { tvRouteToString(context, -1) };
            }
        }
        return sRouteTableName;
    }

    public static void setDefaultFocus(ViewGroup viewGroup) {
        if (null != viewGroup) {
            View focusedView = (View) viewGroup.getFocusedChild();
            View view = null;
            boolean hasFocus = false;

            if (null != focusedView) {
                for (int index = 0; index < viewGroup.getChildCount(); index++) {
                    view = viewGroup.getChildAt(index);
                    if (null != view) {
                        if (view.getId() == focusedView.getId()) {
                            hasFocus = true;
                            break;
                        }
                    }
                }
            }

            if (false == hasFocus) {
                for (int index = 0; index < viewGroup.getChildCount(); index++) {
                    view = viewGroup.getChildAt(index);
                    if (null != view) {
                        if (true == view.isFocusable()) {
                            view.requestFocus();
                            break;
                        }
                    }
                }
            }
        }
    }

    public static int getATVRealChNum(int chNo) {
        int num = chNo;
        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)) {
            /*
             * In Pal system, ATV channel number saving in db is start from 0
             * but display number is start from 1 (NTSC system has no this problem)
             */
            num -= 1;
        } else if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
            if (isISDB()) {
                num -= 1;
            }
        }
        return num;
    }

    public static int getATVDisplayChNum(int chNo) {
        int num = chNo;
        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE)) {
            /*
             * In Pal system, ATV channel number saving in db is start from 0
             * but display number is start from 1 (NTSC system has no this
             * problem)
             */
            num += 1;
        } else if (TvCommonManager.getInstance().isSupportModule(
                TvCommonManager.MODULE_ATV_NTSC_ENABLE)) {
            if (isISDB()) {
                num += 1;
            }
        }

        return num;
    }

    public static boolean isSupportATV() {
        if ((false == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_NTSC_ENABLE) &&
                false == TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_ATV_PAL_ENABLE))) {
            return false;
        }
        return true;
    }

    public static int getSharedPreferencesValue(Context context, String item, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_TITLE,
                Context.MODE_PRIVATE);
        int sharePreValue = sharedPreferences.getInt(item, defaultValue);
        return sharePreValue;
    }

    public static void setSharedPreferencesValue(Context context, String item, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_TITLE,
                Context.MODE_PRIVATE);
        Editor edit = sharedPreferences.edit();
        edit.putInt(item, value);
        edit.commit();
    }

    public static byte[] intsToByteArray(int[] values) {
        if (values == null || values.length == 0) {
            return null;
        }
        byte[] bytes = new byte[values.length << 2];
        int index = 0;
        for (int value : values) {
            bytes[index + 3] = (byte) (value & 0xFF);
            bytes[index + 2] = (byte) ((value >> 8) & 0xFF);
            bytes[index + 1] = (byte) ((value >> 16) & 0xFF);
            bytes[index + 0] = (byte) ((value >> 24) & 0xFF);
            index += 4;
        }
        return bytes;
    }

    public static int[] byteArrayToInts(byte[] bytes) {
        if (bytes == null || bytes.length < 4) {
            return null;
        }
        int count = bytes.length >> 2;
        int [] values = new int[count];
        for (int i = 0; i < count; i++) {
            int startIndex = i << 2;
            if (startIndex + 3 >= bytes.length) {
                return values;
            }
            values[i] = (bytes[startIndex + 3] & 0xFF) + ((bytes[startIndex + 2] & 0xFF) << 8) + ((bytes[startIndex + 1] & 0xFF) << 16) + ((bytes[startIndex + 0] & 0xFF) << 24);
        }
        return values;
    }

    public static boolean isBox() {
        return SystemProperties.getBoolean("mstar.build.for.stb", false);
    }

    private static String tvRouteToString(Context context, int tvRoute) {
        switch (getCurrentTvSystem()) {
            case TvCommonManager.TV_SYSTEM_DVBT:
            case TvCommonManager.TV_SYSTEM_DVBC:
            case TvCommonManager.TV_SYSTEM_DVBS:
            case TvCommonManager.TV_SYSTEM_DVBT2:
            case TvCommonManager.TV_SYSTEM_DVBS2:
            case TvCommonManager.TV_SYSTEM_DTMB:
                switch (tvRoute) {
                    case TvChannelManager.TV_ROUTE_NONE:
                        return context.getResources().getString(R.string.str_cha_tv_route_none);
                    case TvChannelManager.TV_ROUTE_DVBT:
                        return context.getResources().getString(R.string.str_cha_tv_route_dvbt);
                    case TvChannelManager.TV_ROUTE_DVBC:
                        return context.getResources().getString(R.string.str_cha_tv_route_dvbc);
                    case TvChannelManager.TV_ROUTE_DVBS:
                        return context.getResources().getString(R.string.str_cha_tv_route_dvbs);
                    case TvChannelManager.TV_ROUTE_DVBT2:
                        return context.getResources().getString(R.string.str_cha_tv_route_dvbt2);
                    case TvChannelManager.TV_ROUTE_DVBS2:
                        return context.getResources().getString(R.string.str_cha_tv_route_dvbs2);
                    case TvChannelManager.TV_ROUTE_DTMB:
                        return context.getResources().getString(R.string.str_cha_tv_route_dtmb);
                    default:
                        return context.getResources().getString(R.string.str_cha_tv_route_unknown);
                }
            case TvCommonManager.TV_SYSTEM_ATSC:
                switch (tvRoute) {
                    case TvChannelManager.DTV_ANTENNA_TYPE_NONE:
                        return context.getResources().getString(R.string.str_cha_antannatype_none);
                    case TvChannelManager.DTV_ANTENNA_TYPE_AIR:
                        return context.getResources().getString(R.string.str_cha_antannatype_air);
                    case TvChannelManager.DTV_ANTENNA_TYPE_CABLE:
                        return context.getResources().getString(R.string.str_cha_antannatype_cable);
                    default:
                        return context.getResources().getString(R.string.str_cha_tv_route_unknown);
                }
            default:
                return context.getResources().getString(R.string.str_cha_tv_route_unknown);
        }
    }

    /**
     * Build the track ID.
     *
     * @param type The type of the track. The value is one of the following:
     * <ul>
     * <li>{@link TvTrackInfo#TYPE_AUDIO}
     * <li>{@link TvTrackInfo#TYPE_VIDEO}
     * <li>{@link TvTrackInfo#TYPE_SUBTITLE}
     * </ul>
     * @param index The index of the track.
     * @return The track ID.
     */
    public static String buildTrackId(int type, int index) {
        String trackId = null;
        switch (type) {
            case TvTrackInfo.TYPE_VIDEO:
                trackId = String.format("VID-%d", index);
                break;
            case TvTrackInfo.TYPE_AUDIO:
                trackId = String.format("AID-%d", index);
                break;
            case TvTrackInfo.TYPE_SUBTITLE:
                trackId = String.format("SID-%d", index);
                break;
            default:
                break;
        }
        return trackId;
    }
}
