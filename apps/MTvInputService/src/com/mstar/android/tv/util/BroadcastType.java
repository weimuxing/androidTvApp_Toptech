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
import android.media.tv.TvContentRating;
import android.media.tv.TvTrackInfo;
import android.view.KeyEvent;
import android.view.accessibility.CaptioningManager;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.dtv.vo.MwAtscEasInfo;
import com.mstar.android.tv.util.BroadcastTypeAtsc;
import com.mstar.android.tv.util.BroadcastTypeIsdb;
import com.mstar.android.tv.util.BroadcastTypeDvb;
import com.mstar.android.tv.util.Constant.ScreenSaverMode;
import com.mstar.android.tv.util.Constant.SignalProgSyncStatus;
import com.mstar.android.tv.inputservice.R;

import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

public abstract class BroadcastType {
    protected static final String SYSTEM_RATING_DOMAIN = "com.android.tv";

    protected static BroadcastType sInstance = null;

    protected TvEpgManager mTvEpgManager = TvEpgManager.getInstance();

    public static BroadcastType getInstance() {
        if (sInstance == null) {
            synchronized (BroadcastType.class) {
                if (sInstance == null) {
                    if (Utility.isATSC()) {
                        sInstance = new BroadcastTypeAtsc();
                    } else if (Utility.isISDB()) {
                        sInstance = new BroadcastTypeIsdb();
                    } else {
                        sInstance = new BroadcastTypeDvb();
                    }
                }
            }
        }
        return sInstance;
    }

    /**
     * Tune to a given channel.
     *
     * @param inputSource The input source. The value is one of the following:
     * <ul>
     * <li>{@link TvCommonManager#INPUT_SOURCE_ATV}
     * <li>{@link TvCommonManager#INPUT_SOURCE_DTV}
     * </ul>
     * @param displayNumber The channel number defined as {@link TvContract#Channels#COLUMN_DISPLAY_NUMBER}.
     * @param serviceType The channel service type. The value is one of the following:
     * <ul>
     * <li>{@link TvChannelManager#SERVICE_TYPE_ATV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_DTV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_RADIO}
     * <li>{@link TvChannelManager#SERVICE_TYPE_DATA}
     * <li>{@link TvChannelManager#SERVICE_TYPE_UNITED_TV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_INVALID}
     * </ul>
     * @return {@code true} the tuning was successful, {@code false} otherwise.
     */
     public abstract boolean tune(int inputSource, String displayNumber, int serviceType);

    /**
     * Build Supernova program info list.
     *
     * @param inputId The input ID of the TV input.
     * @return The list of TifChannelInfo objects.
     */
    public abstract ArrayList<TifChannelInfo> buildTifChannelInfoList(String inputId);

    /**
     * Get current TIF program info.
     *
     * @param context The context of the application.
     * @param channelId The channel ID that provides this program.
     * @return TifProgramInfo object.
     */
    public abstract TifProgramInfo getCurrentTifProgramInfo(Context context, long channelId);

    /**
     * Get program(channel) info by index.
     *
     * @param index The index of the program info in program list.
     * @return The ProgramInfo object.
     */
    public abstract ProgramInfo getProgramInfoByIndex(int index);

    /**
     * Get current program(channel) info.
     *
     * @return The ProgramInfo object of current channel.
     */
    public abstract ProgramInfo getCurrentProgramInfo();

    /**
     * Build TIF program info list for the target channel.
     *
     * @param context The context of the application.
     * @param programInfo The target program(channel) info.
     * @param channelId The channel ID that provides this program.
     * @return The list of TifProgramInfo objects.
     */
    public abstract ArrayList<TifProgramInfo> buildTifProgramInfoList(Context context, ProgramInfo programInfo, long channelId);

    /**
     * Get the DTV subtitle track information.
     *
     * @return The {@link TvTrackInfo} instance list for the subtitle information.
     */
    public abstract List<TvTrackInfo> getDtvSubtitleTrackInfo();

    /**
     * Get the current subtitle track index.
     *
     * @return The current subtitle track index.
     */
    public abstract int getCurrentSubtitleTrackIndex();

    public abstract void setCaptionEnabled(CaptioningManager captioningManager, String fontFamily);

    /**
     * Enables or disables the caption.
     *
     * @param enabled {@code true} to enable, {@code false} to disable.
     * @param settings The closed caption settings.
     */
    public abstract void setCaptionEnabled(boolean enabled, Object settings);

    /**
     * Get the screen saver string.
     *
     * @param The context of the application.
     * @param status The Supernova defined status. The value is one of the following:
     * <ul>
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_INVALID_SERVICE}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_NO_CI_MODULE}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_CI_PLUS_AUTHENTICATION}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_SCRAMBLED_PROGRAM}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_CH_BLOCK}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_PARENTAL_BLOCK}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_AUDIO_ONLY}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_DATA_ONLY}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_COMMON_VIDEO}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_UNSUPPORTED_FORMAT}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_INVALID_PMT}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_NO_CHANNEL}
     * <li>{@link Constant#ScreenSaverMode#DTV_SS_CA_NOTIFY}
     * <li>{@link Constant#AtscAtvScreenSaverMode#ATV_SS_NORMAL}
     * <li>{@link Constant#AtscAtvScreenSaverMode#ATV_SS_NO_CHANNEL}
     * <li>{@link Constant#SignalProgSyncStatus#NOSYNC}
     * <li>{@link Constant#SignalProgSyncStatus#STABLE_SUPPORT_MODE}
     * <li>{@link Constant#SignalProgSyncStatus#STABLE_UN_SUPPORT_MODE}
     * <li>{@link Constant#SignalProgSyncStatus#UNSTABLE}
     * <li>{@link Constant#SignalProgSyncStatus#AUTO_ADJUST}
     * </ul>
     * @param currentInputSource The current input source.
     * @return The screen saver string.
     */
    public String getScreenSaverString(Context context, int status, int currentInputSource) {
        int resId = 0;
        if (currentInputSource == TvCommonManager.INPUT_SOURCE_DTV ||
                currentInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            switch (status) {
                case ScreenSaverMode.DTV_SS_INVALID_SERVICE:
                    resId = R.string.str_invalid_service;
                    break;
                case ScreenSaverMode.DTV_SS_NO_CI_MODULE:
                    resId = R.string.str_no_ci_module;
                    break;
                case ScreenSaverMode.DTV_SS_CI_PLUS_AUTHENTICATION:
                    resId = R.string.str_ci_plus_authentication;
                    break;
                case ScreenSaverMode.DTV_SS_SCRAMBLED_PROGRAM:
                    resId = R.string.str_scrambled_program;
                    break;
                case ScreenSaverMode.DTV_SS_AUDIO_ONLY:
                    resId = R.string.str_audio_only;
                    break;
                case ScreenSaverMode.DTV_SS_DATA_ONLY:
                    resId = R.string.str_data_only;
                    break;
                case ScreenSaverMode.DTV_SS_UNSUPPORTED_FORMAT:
                    resId = R.string.str_unsupported;
                    break;
                case ScreenSaverMode.DTV_SS_INVALID_PMT:
                    resId = R.string.str_invalid_pmt;
                    break;
                default:
                    break;
            }
        } else if (currentInputSource == TvCommonManager.INPUT_SOURCE_VGA ||
                currentInputSource == TvCommonManager.INPUT_SOURCE_VGA2 ||
                currentInputSource == TvCommonManager.INPUT_SOURCE_VGA3) {
            switch (status) {
                case SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE:
                case SignalProgSyncStatus.UNSTABLE:
                    resId = R.string.str_unsupported;
                    break;
                case SignalProgSyncStatus.AUTO_ADJUST:
                    resId = R.string.str_auto_adjust;
                    break;
                default:
                    break;
            }
        } else if (currentInputSource == TvCommonManager.INPUT_SOURCE_HDMI ||
                currentInputSource == TvCommonManager.INPUT_SOURCE_HDMI2 ||
                currentInputSource == TvCommonManager.INPUT_SOURCE_HDMI3 ||
                currentInputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
            switch (status) {
                case SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE:
                case SignalProgSyncStatus.UNSTABLE:
                    resId = R.string.str_unsupported;
                    break;
                default:
                    break;
            }
        }
        if (resId == 0) {
            return "";
        } else {
            return context.getResources().getString(resId);
        }
    }

    /**
     * Send the key code and check if it is handled by CEC.
     *
     * @param keyCode The value in event.getKeyCode().
     * @param currentInputSource The current input source.
     * @return {@code true} if the key code is handled, {@code false} otherwise.
     */
    public boolean sendCecKey(int keyCode, int currentInputSource) {
        // TODO: Implement it after TIF CEC ready
        return false;
    }

    /**
     * Send the key code and check if it is handled by HbbTV.
     *
     * @param keyCode The value in event.getKeyCode().
     * @return {@code true} if the key code is handled, {@code false} otherwise.
     */
    public boolean sendHbbtvKey(int keyCode) {
        return false;
    }

    /**
     * Send the key code and check if it is handled by MHEG-5.
     *
     * @param keyCode The value in event.getKeyCode().
     * @return {@code true} if the key code is handled, {@code false} otherwise.
     */
    public boolean sendMheg5Key(int keyCode) {
        return false;
    }

    /**
     * Send the key code and check if it is handled by ginga.
     *
     * @param keyCode The value in event.getKeyCode().
     * @param event Description of the key event.
     * @return {@code true} if the key code is handled, {@code false} otherwise.
     */
    public boolean sendGingaKey(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * Check if the channel is the same as current channel.
     *
     * @param displayNumber The channel number defined as {@link TvContract#Channels#COLUMN_DISPLAY_NUMBER}.
     * @param serviceType The channel service type. The value is one of the following:
     * <ul>
     * <li>{@link TvChannelManager#SERVICE_TYPE_ATV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_DTV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_RADIO}
     * <li>{@link TvChannelManager#SERVICE_TYPE_DATA}
     * <li>{@link TvChannelManager#SERVICE_TYPE_UNITED_TV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_INVALID}
     * </ul>
     * @return {@code true} if the channel is the same as current channel, {@code false} otherwise.
     */
    public abstract boolean isSameAsCurrentChannel(String displayNumber, int serviceType);

    /**
     * Get the digital audio format.
     *
     * @param context The context of the application.
     * @param audioType Audio type. The value is one of the following:
     * <ul>
     * <li>{@link TvAudioManager#AUDIO_TYPE_MPEG}
     * <li>{@link TvAudioManager#AUDIO_TYPE_Dolby_D}
     * <li>{@link TvAudioManager#AUDIO_TYPE_AAC}
     * <li>{@link TvAudioManager#AUDIO_TYPE_AC3P}
     * <li>{@link TvAudioManager#AUDIO_TYPE_DRA1}
     * </ul>
     * @param aacLevel The aac level. The value is one of the following:
     * <ul>
     * <li>{@link TvAudioManager#AAC_LEVEL1_BRAZIL}
     * <li>{@link TvAudioManager#AAC_LEVEL2_BRAZIL}
     * <li>{@link TvAudioManager#AAC_LEVEL4_BRAZIL}
     * <li>{@link TvAudioManager#AAC_LEVEL5_BRAZIL}
     * <li>{@link TvAudioManager#HE_AAC_LEVEL2_BRAZIL}
     * <li>{@link TvAudioManager#HE_AAC_LEVEL3_BRAZIL}
     * <li>{@link TvAudioManager#HE_AAC_LEVEL4_BRAZIL}
     * <li>{@link TvAudioManager#HE_AAC_LEVEL5_BRAZIL}
     * <li>{@link TvAudioManager#AAC_LEVEL1}
     * <li>{@link TvAudioManager#AAC_LEVEL2}
     * <li>{@link TvAudioManager#AAC_LEVEL4}
     * <li>{@link TvAudioManager#AAC_LEVEL5}
     * <li>{@link TvAudioManager.HE_AAC_LEVEL2}
     * <li>{@link TvAudioManager.HE_AAC_LEVEL3}
     * <li>{@link TvAudioManager.HE_AAC_LEVEL4}
     * <li>{@link TvAudioManager.HE_AAC_LEVEL5}
     * </ul>
     * @return The audio format.
     */
    public String getAudioFormat(Context context, int audioType, int aacLevel) {
        int resId = 0;
        switch (audioType) {
            case TvAudioManager.AUDIO_TYPE_MPEG:
                resId = R.string.audio_type_mpeg;
                break;
            case TvAudioManager.AUDIO_TYPE_Dolby_D:
                resId = R.string.audio_type_dolby_d;
                break;
            case TvAudioManager.AUDIO_TYPE_AAC:
                resId = R.string.audio_type_aac;
                switch (aacLevel) {
                    case TvAudioManager.AAC_LEVEL1:
                        resId = R.string.aac_level1;
                        break;
                    case TvAudioManager.AAC_LEVEL2:
                        resId = R.string.aac_level2;
                        break;
                    case TvAudioManager.AAC_LEVEL4:
                        resId = R.string.aac_level4;
                        break;
                    case TvAudioManager.AAC_LEVEL5:
                        resId = R.string.aac_level5;
                        break;
                    case TvAudioManager.HE_AAC_LEVEL2:
                        resId = R.string.he_aac_level2;
                        break;
                    case TvAudioManager.HE_AAC_LEVEL3:
                        resId = R.string.he_aac_level3;
                        break;
                    case TvAudioManager.HE_AAC_LEVEL4:
                        resId = R.string.he_aac_level4;
                        break;
                    case TvAudioManager.HE_AAC_LEVEL5:
                        resId = R.string.he_aac_level5;
                        break;
                    default:
                        break;
                }
                break;
            case TvAudioManager.AUDIO_TYPE_AC3P:
                resId = R.string.audio_type_ac3p;
                break;
            case TvAudioManager.AUDIO_TYPE_DRA1:
                resId = R.string.audio_type_dra1;
                break;
            default:
                return new String("");
        }
        return context.getResources().getString(resId);
    }

    /**
     * Get the EAS info.
     *
     * @return The MwAtscEasInfo object.
     */
    public MwAtscEasInfo getEasInfo() {
        return null;
    }

     /**
     * Tune to the specific channel in EAS process (only for ATSC).
     *
     * @param majorNumber Major number of the channel.
     * @param minorNumber Minor number of the channel.
     */
    public void easTune(int majorNumber, int minorNumber) {
    }
}
