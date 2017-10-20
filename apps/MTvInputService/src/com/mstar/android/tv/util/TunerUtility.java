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
import android.content.ContentValues;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.media.tv.TvTrackInfo;
import android.media.tv.TvContentRating;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvParentalControlManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tv.inputservice.R;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.MissingResourceException;
import java.io.OutputStream;
import java.io.IOException;

public class TunerUtility {
    private static final String TAG = "TunerUtility";

    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    private static final Map<String, String> ISO639_2_B_TO_ISO639_2_T;
    static {
        ISO639_2_B_TO_ISO639_2_T = new HashMap<>();
        ISO639_2_B_TO_ISO639_2_T.put("alb", "sqi");
        ISO639_2_B_TO_ISO639_2_T.put("arm", "hye");
        ISO639_2_B_TO_ISO639_2_T.put("baq", "eus");
        ISO639_2_B_TO_ISO639_2_T.put("tib", "bod");
        ISO639_2_B_TO_ISO639_2_T.put("bur", "mya");
        ISO639_2_B_TO_ISO639_2_T.put("cze", "ces");
        ISO639_2_B_TO_ISO639_2_T.put("chi", "zho");
        ISO639_2_B_TO_ISO639_2_T.put("wel", "cym");
        ISO639_2_B_TO_ISO639_2_T.put("ger", "deu");
        ISO639_2_B_TO_ISO639_2_T.put("dut", "nld");
        ISO639_2_B_TO_ISO639_2_T.put("gre", "ell");
        ISO639_2_B_TO_ISO639_2_T.put("per", "fas");
        ISO639_2_B_TO_ISO639_2_T.put("fre", "fra");
        ISO639_2_B_TO_ISO639_2_T.put("geo", "kat");
        ISO639_2_B_TO_ISO639_2_T.put("ice", "isl");
        ISO639_2_B_TO_ISO639_2_T.put("mac", "mkd");
        ISO639_2_B_TO_ISO639_2_T.put("mao", "mri");
        ISO639_2_B_TO_ISO639_2_T.put("may", "msa");
        ISO639_2_B_TO_ISO639_2_T.put("rum", "ron");
        ISO639_2_B_TO_ISO639_2_T.put("slo", "slk");
        ISO639_2_B_TO_ISO639_2_T.put("tib", "bod");
    }

    /**
     * Parse the track index from the track ID.
     *
     * @param trackId The ID of the track.
     * @return The index of the track.
     */
    public static int parseIndexFromTrackId(String trackId) {
        int index = -1;
        try {
            if (trackId != null) {
                index = Integer.parseInt(trackId.substring(4));
                Log.d(TAG, "trackId = " + trackId + ", index = " + index);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return index;
    }

    /**
     * Get the DTV audio track information.
     *
     * @param context The context of the application.
     * @return The {@link TvTrackInfo} instance list for the audio information.
     */
    public static List<TvTrackInfo> getDtvAudioTrackInfo(Context context) {
        DtvAudioInfo audioInfo = TvChannelManager.getInstance().getAudioInfo();
        if (audioInfo == null) {
            return null;
        }
        List<TvTrackInfo> trackInfoList = new ArrayList<TvTrackInfo>();
        for (int i = 0; i < audioInfo.audioLangNum; i++) {
            String originalLang;
            if (audioInfo.audioInfos[i].isoLangInfo.isoLangInfo[2] == 0) {
                originalLang = String.valueOf(audioInfo.audioInfos[i].isoLangInfo.isoLangInfo, 0, 2);
            } else {
                originalLang = String.valueOf(audioInfo.audioInfos[i].isoLangInfo.isoLangInfo, 0, 3);
            }
            String lang = verifyLanguageCode(originalLang);
            Log.d(TAG, "getDtvAudioTrackInfo: lang = " + lang);
            int channelCount = 0;
            switch (audioInfo.audioInfos[i].isoLangInfo.audioMode) {
                case TvAudioManager.DTV_SOUND_MODE_STEREO:
                    channelCount = 2;
                    break;
                case TvAudioManager.DTV_SOUND_MODE_LEFT:
                case TvAudioManager.DTV_SOUND_MODE_RIGHT:
                    channelCount = 1;
                    break;
                default:
                    break;
            }
            String audioFormat = BroadcastType.getInstance().getAudioFormat(
                    context,
                    audioInfo.audioInfos[i].audioType,
                    audioInfo.audioInfos[i].aacProfileAndLevel);
            Bundle extra = new Bundle();
            extra.putString(Constant.TRACK_INFO_KEY_AUDIO_FORMAT, audioFormat);
            if (DEBUG) {
                Log.d(TAG, "getDtvAudioTrackInfo: audioInfo: " + lang + ", " + channelCount + ", " + audioFormat);
            }
            TvTrackInfo audioTrack = new TvTrackInfo.Builder(TvTrackInfo.TYPE_AUDIO,
                    Utility.buildTrackId(TvTrackInfo.TYPE_AUDIO, i))
                    .setLanguage(lang)
                    .setAudioChannelCount(channelCount)
                    .setExtra(extra)
                    .build();
            trackInfoList.add(audioTrack);
        }
        return trackInfoList;
    }

    /**
     * Get the current audio track index.
     *
     * @return The current audio track index.
     */
    public static int getCurrentAudioTrackIndex() {
        if (TvChannelManager.getInstance().getAudioInfo() != null) {
            return (int) (TvChannelManager.getInstance().getAudioInfo().currentAudioIndex);
        }
        return -1;
    }

    /**
     * Get the ATV audio track information.
     *
     * @param The context of the application.
     * @return The {@link TvTrackInfo} instance for the audio information.
     */
    public static TvTrackInfo getAtvAudioTrackInfo(Context context) {
        int mode = TvCommonManager.getInstance().getATVMtsMode();
        String [] soundFormatArray = context.getResources().getStringArray(R.array.str_arr_atv_sound_format);
        if (mode >= soundFormatArray.length) {
            mode = TvCommonManager.ATV_AUDIOMODE_INVALID;
        }
        String soundFormat = soundFormatArray[mode];
        if (DEBUG) {
            Log.d(TAG, "getAtvAudioTrackInfo: soundFormat: " + soundFormat);
        }
        Bundle extra = new Bundle();
        extra.putString(Constant.TRACK_INFO_KEY_AUDIO_FORMAT, soundFormat);
        TvTrackInfo audioTrack = new TvTrackInfo.Builder(TvTrackInfo.TYPE_AUDIO,
                Utility.buildTrackId(TvTrackInfo.TYPE_AUDIO, 0))
                .setLanguage(null)
                .setAudioChannelCount(1)
                .setExtra(extra)
                .build();
        return audioTrack;
    }

    /**
     * Get the DTV channel type.
     *
     * @return The string of channel type. The value is one of the following:
     * <ul>
     * <li>{@link TvContract#Channels#TYPE_OTHER}
     * <li>{@link TvContract#Channels#TYPE_DVB_T}
     * <li>{@link TvContract#Channels#TYPE_DVB_T2}
     * <li>{@link TvContract#Channels#TYPE_DVB_C}
     * <li>{@link TvContract#Channels#TYPE_DVB_S}
     * <li>{@link TvContract#Channels#TYPE_DVB_S2}
     * <li>{@link TvContract#Channels#TYPE_ATSC_T}
     * <li>{@link TvContract#Channels#TYPE_ATSC_C}
     * <li>{@link TvContract#Channels#TYPE_ISDB_T}
     * <li>{@link TvContract#Channels#TYPE_ISDB_C}
     * <li>{@link TvContract#Channels#TYPE_DTMB}
     * </ul>
     */
    public static String getDtvChannelType() {
        String type = TvContract.Channels.TYPE_OTHER;
        TvTypeInfo tvInfo = TvCommonManager.getInstance().getTvInfo();
        int currentRouteIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
        int currentRoute = tvInfo.routePath[currentRouteIndex];
        switch (currentRoute) {
            case TvChannelManager.TV_ROUTE_DVBT:
                type = TvContract.Channels.TYPE_DVB_T;
                break;
            case TvChannelManager.TV_ROUTE_DVBC:
                type = TvContract.Channels.TYPE_DVB_C;
                break;
            case TvChannelManager.TV_ROUTE_DVBS:
                type = TvContract.Channels.TYPE_DVB_S;
                break;
            case TvChannelManager.TV_ROUTE_ATSC:
                if (TvAtscChannelManager.getInstance()
                        .getDtvAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                    type = TvContract.Channels.TYPE_ATSC_T;
                } else if (TvAtscChannelManager.getInstance()
                        .getDtvAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_CABLE) {
                    type = TvContract.Channels.TYPE_ATSC_C;
                } else {
                    type = TvContract.Channels.TYPE_OTHER;
                }
                break;
            case TvChannelManager.TV_ROUTE_ISDB:
                if (TvIsdbChannelManager.getInstance()
                        .getAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_AIR) {
                    type = TvContract.Channels.TYPE_ISDB_T;
                } else if (TvIsdbChannelManager.getInstance()
                        .getAntennaType() == TvChannelManager.DTV_ANTENNA_TYPE_CABLE) {
                    type = TvContract.Channels.TYPE_ISDB_C;
                } else {
                    type = TvContract.Channels.TYPE_OTHER;
                }
                break;
            case TvChannelManager.TV_ROUTE_DVBT2:
                type = TvContract.Channels.TYPE_DVB_T2;
                break;
            case TvChannelManager.TV_ROUTE_DVBS2:
                type = TvContract.Channels.TYPE_DVB_S2;
                break;
            case TvChannelManager.TV_ROUTE_DTMB:
                type = TvContract.Channels.TYPE_DTMB;
                break;
            default:
                type = TvContract.Channels.TYPE_OTHER;
                break;
        }
        return type;
    }

    /**
     * Get the service type of the channel.
     *
     * @param originalServiceType The Supernova service type. The value is on of the following:
     * <ul>
     * <li>{@link TvChannelManager#SERVICE_TYPE_ATV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_DTV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_RADIO}
     * <li>{@link TvChannelManager#SERVICE_TYPE_DATA}
     * <li>{@link TvChannelManager#SERVICE_TYPE_UNITED_TV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_INVALID}
     * </ul>
     * @return The string of service type. The value is one of the following:
     * <ul>
     * <li>{@link TvContract#Channels#SERVICE_TYPE_OTHER}
     * <li>{@link TvContract#Channels#SERVICE_TYPE_AUDIO_VIDEO}
     * <li>{@link TvContract#Channels#SERVICE_TYPE_AUDIO}
     * </ul>
     */
    public static String getServiceType(int originalServiceType) {
        String serviceType = TvContract.Channels.SERVICE_TYPE_OTHER;
        switch (originalServiceType) {
            case TvChannelManager.SERVICE_TYPE_ATV:
                serviceType = TvContract.Channels.SERVICE_TYPE_AUDIO_VIDEO;
                break;
            case TvChannelManager.SERVICE_TYPE_DTV:
                serviceType = TvContract.Channels.SERVICE_TYPE_AUDIO_VIDEO;
                break;
            case TvChannelManager.SERVICE_TYPE_RADIO:
                serviceType = TvContract.Channels.SERVICE_TYPE_AUDIO;
                break;
            case TvChannelManager.SERVICE_TYPE_DATA:
            case TvChannelManager.SERVICE_TYPE_UNITED_TV:
            case TvChannelManager.SERVICE_TYPE_INVALID:
            default:
                serviceType = TvContract.Channels.SERVICE_TYPE_OTHER;
                break;
        }
        return serviceType;
    }

    /**
     * Get the Supernova service type of the DTV channel.
     *
     * @param serviceType The service type of the channel. The value is one of the following:
     * <ul>
     * <li>{@link TvContract#Channels#SERVICE_TYPE_OTHER}
     * <li>{@link TvContract#Channels#SERVICE_TYPE_AUDIO_VIDEO}
     * <li>{@link TvContract#Channels#SERVICE_TYPE_AUDIO}
     * </ul>
     * @return The string of service type. The value is one of the following:
     * <ul>
     * <li>{@link TvChannelManager#SERVICE_TYPE_DTV}
     * <li>{@link TvChannelManager#SERVICE_TYPE_RADIO}
     * <li>{@link TvChannelManager#SERVICE_TYPE_DATA}
     * </ul>
     */
    public static int getSnServiceTypeForDtv(String serviceType) {
        int snServiceType = TvChannelManager.SERVICE_TYPE_INVALID;
        if (serviceType.equals(TvContract.Channels.SERVICE_TYPE_AUDIO_VIDEO)) {
            snServiceType = TvChannelManager.SERVICE_TYPE_DTV;
        } else if (serviceType.equals(TvContract.Channels.SERVICE_TYPE_AUDIO)) {
            snServiceType = TvChannelManager.SERVICE_TYPE_RADIO;
        } else {
            // TODO
            snServiceType = TvChannelManager.SERVICE_TYPE_DATA;
        }
        return snServiceType;
    }

    /**
     * Put the TIF channel info to ContentValues object.
     *
     * @param tifChannelInfo The TIF channel info.
     * @return The ContentValues object with channel info.
     */
    public static ContentValues putChannelInfoToContentValues(TifChannelInfo tifChannelInfo) {
        ContentValues values = new ContentValues();
        values.put(TvContract.Channels.COLUMN_INPUT_ID, tifChannelInfo.inputId);
        values.put(TvContract.Channels.COLUMN_TYPE, tifChannelInfo.type);
        values.put(TvContract.Channels.COLUMN_SERVICE_TYPE, tifChannelInfo.serviceType);
        values.put(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID, tifChannelInfo.originalNetworkId);
        values.put(TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID, tifChannelInfo.transportStreamId);
        values.put(TvContract.Channels.COLUMN_SERVICE_ID, tifChannelInfo.serviceId);
        values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, tifChannelInfo.displayNumber);
        values.put(TvContract.Channels.COLUMN_DISPLAY_NAME, tifChannelInfo.displayName);
        values.put(TvContract.Channels.COLUMN_LOCKED, tifChannelInfo.isLocked);
        if (tifChannelInfo.internalProviderData != null) {
            values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA, tifChannelInfo.internalProviderData);
        }
        return values;
    }

   /**
     * Put the TIF program info to ContentValues object.
     *
     * @param tifProgramInfo The TIF program info.
     * @return The ContentValues object with program info.
     */
    public static ContentValues putProgramInfoToContentValues(TifProgramInfo tifProgramInfo) {
        ContentValues values = new ContentValues();
        values.put(TvContract.Programs.COLUMN_CHANNEL_ID, tifProgramInfo.channelId);
        values.put(TvContract.Programs.COLUMN_TITLE, tifProgramInfo.title);
        values.put(TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS, tifProgramInfo.startTime);
        values.put(TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS, tifProgramInfo.endTime);
        if (tifProgramInfo.broadcastGenres != null &&
                tifProgramInfo.broadcastGenres.length > 0) {
            values.put(TvContract.Programs.COLUMN_BROADCAST_GENRE, genresToString(tifProgramInfo.broadcastGenres));
        }
        if (tifProgramInfo.canonicalGenres != null &&
                tifProgramInfo.canonicalGenres.length > 0) {
            values.put(TvContract.Programs.COLUMN_CANONICAL_GENRE, genresToString(tifProgramInfo.canonicalGenres));
        }
        values.put(TvContract.Programs.COLUMN_SHORT_DESCRIPTION, tifProgramInfo.shortDescription);
        values.put(TvContract.Programs.COLUMN_LONG_DESCRIPTION, tifProgramInfo.longDescription);
        values.put(TvContract.Programs.COLUMN_VIDEO_WIDTH, tifProgramInfo.videoWidth);
        values.put(TvContract.Programs.COLUMN_VIDEO_HEIGHT, tifProgramInfo.videoHeight);
        values.put(TvContract.Programs.COLUMN_AUDIO_LANGUAGE, tifProgramInfo.audioLanguage);
        if (tifProgramInfo.contentRatings != null &&
                tifProgramInfo.contentRatings.length > 0) {
            values.put(TvContract.Programs.COLUMN_CONTENT_RATING,
                    contentRatingsToString(tifProgramInfo.contentRatings));
        }
        return values;
    }

    /**
     * Put the A/V TIF program info to ContentValues object.
     *
     * @param tifProgramInfo The TIF program info.
     * @return The ContentValues object with A/V info.
     */
    public static ContentValues putAvProgramInfoToContentValues(TifProgramInfo tifProgramInfo) {
        ContentValues values = new ContentValues();
        values.put(TvContract.Programs.COLUMN_VIDEO_WIDTH, tifProgramInfo.videoWidth);
        values.put(TvContract.Programs.COLUMN_VIDEO_HEIGHT, tifProgramInfo.videoHeight);
        values.put(TvContract.Programs.COLUMN_AUDIO_LANGUAGE, tifProgramInfo.audioLanguage);
        return values;
    }

    /**
     * Get the ISO audio type in String.
     *
     * @param context The context of the application.
     * @param isoAudioType The ISO audio type. 0x01: clean effects; 0x02: hearing impaired; 0x03: visual impaired.
     * @return The ISO audio type in String.
     */
    public static String getIsoAudioTypeString(Context context, int isoAudioType) {
        String[] audioTypeIsoString = context.getResources().getStringArray(R.array.str_arr_setting_audio_type_iso_text);
        if (isoAudioType < audioTypeIsoString.length) {
            return audioTypeIsoString[isoAudioType];
        }
        return new String("");
    }

    /**
     * Check if subtitle is of Hard-of-Hearing(HOH) type
     *
     * @param subtitleType The Subtitle type. The value of HOH type is one of the following:
     * <ul>
     * <li>{@link DtvType#EnumSubtitlingType#E_SUBTITLING_TYPE_HH_NO}
     * <li>{@link DtvType#EnumSubtitlingType#E_SUBTITLING_TYPE_HH_4X3}
     * <li>{@link DtvType#EnumSubtitlingType#E_SUBTITLING_TYPE_HH_16X9}
     * <li>{@link DtvType#EnumSubtitlingType#E_SUBTITLING_TYPE_HH_221X100}
     * <li>{@link DtvType#EnumSubtitlingType#E_SUBTITLING_TYPE_HH_HD}
     * </ul>
     * @return {@code true} if the subtitle type is HOH, {@code false} otherwise.
     */
    public static boolean isHohSubtitle(int subtitleType) {
        if (subtitleType == 0x20 || subtitleType == 0x21 ||
            subtitleType == 0x22 || subtitleType == 0x23 ||
            subtitleType == 0x24) {
            return true;
        }
        return false;
    }

    /**
     * Delete all programs of the given channel URI.
     *
     * @param context The context of the application.
     * @param channelUri The URI of the channel to delete programs for.
     */
    public static void deleteProgramsByChannel(Context context, Uri channelUri) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = { TvContract.Channels._ID };
        Cursor cursor = resolver.query(channelUri, projection, null, null, null);
        try {
            while (cursor != null && cursor.moveToNext()) {
                long channelId = cursor.getLong(0);
                Uri programUri = TvContract.buildProgramsUriForChannel(channelId);
                resolver.delete(programUri, null, null);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Delete all programs of the given channel URI except current program.
     *
     * @param context The context of the application.
     * @param channelUri The URI of the channel to delete programs for.
     * @param currentTime Curren time.
     */
    public static void deleteProgramsByChannelExceptCurrentProgram(Context context, Uri channelUri, long currentTime) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = { TvContract.Channels._ID };
        Cursor cursor = resolver.query(channelUri, projection, null, null, null);
        try {
            while (cursor != null && cursor.moveToNext()) {
                long channelId = cursor.getLong(0);
                Uri programUri = TvContract.buildProgramsUriForChannel(channelId);
                String where = TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS+">? OR "+TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS+"<?";
                String currentTimeStr = Long.toString(currentTime, 10);
                String[] selectionArgs = {currentTimeStr, currentTimeStr};
                resolver.delete(programUri, where, selectionArgs);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Update current program of the given channel URI.
     *
     * @param context The context of the application.
     * @param channelUri The URI of the channel to delete programs for.
     * @param currentTime Curren time.
     * @param tifProgramInfo The TIF program info.
     */
    public static boolean updateCurrentProgramByChannel(Context context, Uri channelUri, long currentTime, TifProgramInfo tifProgramInfo) {
        boolean ret = false;
        ContentResolver resolver = context.getContentResolver();
        String[] projection = { TvContract.Channels._ID };
        Cursor cursor = resolver.query(channelUri, projection, null, null, null);
        try {
            if (cursor != null && cursor.moveToNext()) {
                long channelId = cursor.getLong(0);
                Uri programUri = TvContract.buildProgramsUriForChannel(channelId);
                ContentValues values = TunerUtility.putProgramInfoToContentValues(tifProgramInfo);
                String where = TvContract.Programs.COLUMN_START_TIME_UTC_MILLIS+"<=? AND "+TvContract.Programs.COLUMN_END_TIME_UTC_MILLIS+">=?";
                String currentTimeStr = Long.toString(currentTime, 10);
                String[] selectionArgs = {currentTimeStr, currentTimeStr};
                if (resolver.update(programUri, values, where, selectionArgs) > 0) {
                    ret = true;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ret;
    }

    /**
     * Verify if the language code is encoded by either ISO 639-1 or ISO 639-2/T and convert if not.
     *
     * @param lang The original language code.
     * @return The ISO 639-1 or ISO 639-2/T language code or null if the conversion fails.
     */
    public static String verifyLanguageCode(String lang) {
        try {
            if (lang.length() == 2) {
                final String[] iso639_1 = Locale.getISOLanguages();
                for (String s : iso639_1) {
                    if (lang.equalsIgnoreCase(s)) {
                        return s;
                    }
                }
            } else {
                return new Locale(lang).getISO3Language();
            }
        } catch (MissingResourceException exception) {
            Log.e(TAG, "MissingResourceException");
            return convertLanguageCode(lang);
        }
        return null;
    }

    /**
     * Write the channel logo data to the content row pointed by the specified channel ID.
     *
     * @param context The context of the application.
     * @param channelId The ID of the channel whose logo is pointed to.
     * @param logo The channel logo data with Bitmap type.
     */
    public static void writeChannelLogo(Context context, long channelId, Bitmap logo) {
        Uri channelLogoUri = TvContract.buildChannelLogoUri(channelId);
        try {
            AssetFileDescriptor fd =
                    context.getContentResolver().openAssetFileDescriptor(channelLogoUri, "w");
            OutputStream os = fd.createOutputStream();
            logo.compress(Bitmap.CompressFormat.PNG, 100, os);
            Log.d(TAG, "writeChannelLogo: write channel logo to " + channelLogoUri);
        } catch (IOException e) {
            Log.e(TAG, "writeChannelLogo: exception happened!!");
        }
    }

    /**
     * Update the current ATV channel info.
     *
     * @param context The context of the application.
     * @param channelUri The URI of the channel to update.
     */
    public static void updateCurrentAtvChannelInfo(Context context, Uri channelUri) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = { TvContract.Channels.COLUMN_TYPE};
        Cursor cursor = resolver.query(channelUri, projection, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String originalType = cursor.getString(0);
                String type = getAtvChannelType();
                if (originalType!= null && !originalType.equals(type)) {
                    ContentValues value = new ContentValues();
                    value.put(TvContract.Channels.COLUMN_TYPE, type);
                    if (DEBUG) Log.d(TAG, "updateCurrentAtvChannelInfo: update COLUMN_TYPE to " + type);
                    resolver.update(channelUri, value, null, null);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static String genresToString(String[] genres) {
        if (genres == null || genres.length == 0) {
            return null;
        }
        final String DELIMITER = ",";
        StringBuilder commaSeparatedGenres = new StringBuilder(genres[0]);
        for (int i = 1; i < genres.length; ++i) {
            commaSeparatedGenres.append(DELIMITER);
            commaSeparatedGenres.append(genres[i]);
        }
        return commaSeparatedGenres.toString();
    }

    private static String contentRatingsToString(TvContentRating[] ratings) {
        if (ratings == null || ratings.length == 0) {
            return null;
        }
        final String DELIMITER = ",";
        StringBuilder commaSeparatedRatings = new StringBuilder(ratings[0].flattenToString());
        for (int i = 1; i < ratings.length; ++i) {
            commaSeparatedRatings.append(DELIMITER);
            commaSeparatedRatings.append(ratings[i].flattenToString());
        }
        return commaSeparatedRatings.toString();
    }

    private static String convertLanguageCode(String lang) {
        String convertedLang = ISO639_2_B_TO_ISO639_2_T.get(lang.toLowerCase());
        if (convertedLang == null &&
                lang.equalsIgnoreCase("esl")) {
            convertedLang = "spa";
        }
        return convertedLang;
    }

    private static String getAtvChannelType() {
        String type;
        switch (TvChannelManager.getInstance().getAvdVideoStandard()) {
            case TvChannelManager.AVD_VIDEO_STANDARD_PAL_BGHI:
            case TvChannelManager.AVD_VIDEO_STANDARD_PAL_M:
            case TvChannelManager.AVD_VIDEO_STANDARD_PAL_N:
            case TvChannelManager.AVD_VIDEO_STANDARD_PAL_60:
                type = TvContract.Channels.TYPE_PAL;
                break;
            case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_44:
            case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_M:
                type = TvContract.Channels.TYPE_NTSC;
                break;
            case TvChannelManager.AVD_VIDEO_STANDARD_SECAM:
                type = TvContract.Channels.TYPE_SECAM;
                break;
            default:
                type = TvContract.Channels.TYPE_OTHER;
                break;
        }
        return type;
    }
}
