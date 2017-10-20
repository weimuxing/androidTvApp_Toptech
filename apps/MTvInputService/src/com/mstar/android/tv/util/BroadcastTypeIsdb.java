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
import android.media.tv.TvContract;
import android.media.tv.TvContentRating;
import android.media.tv.TvTrackInfo;
import android.view.KeyEvent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.CaptioningManager;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvCcManager;
import com.mstar.android.tv.TvGingaManager;
import com.mstar.android.tvapi.dtv.vo.DtvEitInfo;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.AudioInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.common.vo.DTVSpecificProgInfo;
import com.mstar.android.tv.inputservice.R;

import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

public class BroadcastTypeIsdb extends BroadcastType {
    private static final String TAG = "BroadcastTypeIsdb";

    private static final String RATING_SYSTEM_ISDB = "ISDB";

    private TvIsdbChannelManager mTvIsdbChannelManager = TvIsdbChannelManager.getInstance();

    @Override
    public boolean tune(int inputSource, String displayNumber, int serviceType) {
        final String DELIMITER = ".";
        String[] strs = displayNumber.split(DELIMITER);
        if (strs.length == 0 || strs.length > 2) {
            return false;
        }
        try {
            if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                int majorNumber = Integer.parseInt(strs[0]);
                int minorNumber = 0;
                if (strs.length == 2) {
                    minorNumber = Integer.parseInt(strs[1]);
                }
                int number = majorNumber << 8 + minorNumber;
                return mTvIsdbChannelManager.selectProgram(
                        number,
                        serviceType);
            } else if (inputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                int number = Integer.parseInt(strs[0]);
                return mTvIsdbChannelManager.selectProgram(
                        Utility.getATVRealChNum(number),
                        TvChannelManager.SERVICE_TYPE_ATV);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<TifChannelInfo> buildTifChannelInfoList(String inputId) {
        mTvIsdbChannelManager.genMixProgList(false);
        int serviceNum = mTvIsdbChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        ArrayList<TifChannelInfo> tifChannelInfoList = new ArrayList<TifChannelInfo>();
        for (int i = 0; i < serviceNum; i++) {
            ProgramInfo pgi = mTvIsdbChannelManager.getProgramInfo(i);
            if (pgi == null || !pgi.isVisible) {
                continue;
            }
            TifChannelInfo info = new TifChannelInfo();
            info.inputId = inputId;
            info.serviceType = TunerUtility.getServiceType(pgi.serviceType);
            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                info.type = TvContract.Channels.TYPE_OTHER;
                info.displayNumber = Integer.toString(pgi.number+1);
                info.serviceId = pgi.number+2000;
            } else {
                info.type = TunerUtility.getDtvChannelType();
                info.displayNumber = pgi.majorNum + "." + pgi.minorNum;
                info.serviceId = pgi.serviceId;
            }
            info.transportStreamId = pgi.transportStreamId;
            info.displayName = pgi.serviceName;
            info.isLocked = pgi.isLock;
            int[] temp = {i, (int) pgi.serviceType};
            info.internalProviderData = Utility.intsToByteArray(temp);
            tifChannelInfoList.add(info);
        }
        return tifChannelInfoList;
    }

    @Override
    public TifProgramInfo getCurrentTifProgramInfo(Context context, long channelId) {
        TifProgramInfo tifProgramInfo = new TifProgramInfo();
        if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            ProgramInfo programInfo = mTvIsdbChannelManager.getCurrentProgramInfo();
            PresentFollowingEventInfo presentEventInfo = null;
            DtvEitInfo dtvEitInfo = null;
            DTVSpecificProgInfo dtvSpecProgInfo = null;
            try {
                presentEventInfo = mTvEpgManager.getEpgPresentFollowingEventInfo(programInfo.serviceType,
                        programInfo.number, true, TvEpgManager.EPG_DETAIL_DESCRIPTION);
                if (presentEventInfo != null) {
                   dtvEitInfo = mTvEpgManager.getEitInfo(true);
                } else {
                    Log.d(TAG, "presentEventInfo == null, return");
                    return tifProgramInfo;
                }
                dtvSpecProgInfo = mTvIsdbChannelManager.getCurrentProgramSpecificInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (presentEventInfo != null) {
                tifProgramInfo.title = presentEventInfo.eventInfo.name;
                tifProgramInfo.startTime = (long) presentEventInfo.eventInfo.originalStartTime * 1000;
                tifProgramInfo.endTime = (long) (presentEventInfo.eventInfo.originalStartTime +
                        presentEventInfo.eventInfo.durationTime) * 1000;
            }
            if (dtvEitInfo != null) {
                tifProgramInfo.shortDescription = dtvEitInfo.eitCurrentEventPf.shortEventText;
                tifProgramInfo.longDescription = dtvEitInfo.eitCurrentEventPf.extendedEventText;
                tifProgramInfo.broadcastGenres = getBroadcastGenres(
                        context,
                        (int) dtvEitInfo.eitCurrentEventPf.contentNibbleLevel1,
                        (int) dtvEitInfo.eitCurrentEventPf.contentNibbleLevel2);
                tifProgramInfo.contentRatings = createRatings((int) dtvEitInfo.eitCurrentEventPf.parentalControl);
            }
            if (dtvSpecProgInfo != null) {
                tifProgramInfo.videoWidth = dtvSpecProgInfo.width;
                tifProgramInfo.videoHeight = dtvSpecProgInfo.height;
            }
            DtvAudioInfo audioInfo = mTvIsdbChannelManager.getAudioInfo();
            if (audioInfo != null && audioInfo.audioLangNum > 0) {
                AudioInfo currentAudioInfo = audioInfo.audioInfos[audioInfo.currentAudioIndex];
                tifProgramInfo.audioLanguage = String.valueOf(currentAudioInfo.isoLangInfo.isoLangInfo);
            }
            tifProgramInfo.channelId = channelId;
        }
        return tifProgramInfo;
    }

    @Override
    public ProgramInfo getProgramInfoByIndex(int index) {
        return mTvIsdbChannelManager.getProgramInfoByIndex(index);
    }

    @Override
    public ProgramInfo getCurrentProgramInfo() {
        return mTvIsdbChannelManager.getCurrentProgramInfo();
    }

    @Override
    public ArrayList<TifProgramInfo> buildTifProgramInfoList(Context context, ProgramInfo programInfo, long channelId) {
        return null;
    }

    @Override
    public List<TvTrackInfo> getDtvSubtitleTrackInfo() {
        return null;
    }

    @Override
    public int getCurrentSubtitleTrackIndex() {
        return -1;
    }

    @Override
    public void setCaptionEnabled(CaptioningManager captioningManager, String fontFamily) {
        // Do nothing.
    }

    @Override
    public void setCaptionEnabled( boolean enabled, Object settings) {
        TvCcManager.getInstance().stopCc();
        if (enabled) {
            TvCcManager.getInstance().startCc();
        }
    }

    @Override
    public boolean sendGingaKey(int keyCode, KeyEvent event) {
        final boolean isDown = (event.getAction() == KeyEvent.ACTION_DOWN);
        if (TvGingaManager.getInstance().isGingaRunning()) {
            if (isDown) {
                if (TvGingaManager.getInstance().processKey(keyCode, true)) {
                    return true;
                }
            } else {
                if (TvGingaManager.getInstance().processKey(keyCode, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isSameAsCurrentChannel(String displayNumber, int serviceType) {
        return false;
    }

    @Override
    public String getAudioFormat(Context context, int audioType, int aacLevel) {
        String audioFormat = super.getAudioFormat(context, audioType, aacLevel);
        if (audioFormat.isEmpty() &&
                audioType == TvAudioManager.AUDIO_TYPE_AAC) {
            int resId = 0;
            switch (aacLevel) {
                case TvAudioManager.AAC_LEVEL1_BRAZIL:
                    resId = R.string.aac_level1;
                    break;
                case TvAudioManager.AAC_LEVEL2_BRAZIL:
                    resId = R.string.aac_level2;
                    break;
                case TvAudioManager.AAC_LEVEL4_BRAZIL:
                    resId = R.string.aac_level4;
                    break;
                case TvAudioManager.AAC_LEVEL5_BRAZIL:
                    resId = R.string.aac_level5;
                    break;
                case TvAudioManager.HE_AAC_LEVEL2_BRAZIL:
                    resId = R.string.he_aac_level2;
                    break;
                case TvAudioManager.HE_AAC_LEVEL3_BRAZIL:
                    resId = R.string.he_aac_level3;
                    break;
                case TvAudioManager.HE_AAC_LEVEL4_BRAZIL:
                    resId = R.string.he_aac_level4;
                    break;
                case TvAudioManager.HE_AAC_LEVEL5_BRAZIL:
                    resId = R.string.he_aac_level5;
                    break;
                default:
                    return audioFormat;
            }
            audioFormat = context.getResources().getString(resId);
        }
        return audioFormat;
    }

    private TvContentRating[] createRatings(int originalRating) {
        Log.d(TAG, "createRatings: originalRating = " + originalRating);
        TvContentRating[] canonicalRatings = new TvContentRating[1];
        if (originalRating > 3 && originalRating < 21) {
            canonicalRatings[0] = TvContentRating.createRating(SYSTEM_RATING_DOMAIN, RATING_SYSTEM_ISDB, String.format("ISDB_%d", originalRating), (String[]) null);
        }
        return canonicalRatings[0] != null ? canonicalRatings : null;
    }

    private String[] getBroadcastGenres(Context context, int nibbleLevel1, int nibbleLevel2) {
        Log.d(TAG, "getBroadcastGenres: nibbleLevel1 = " + nibbleLevel1 + ", nibbleLevel2 = " + nibbleLevel2);
        String[] genreArray = null;
        String[] broadcastGenres = new String[1];
        switch (nibbleLevel1) {
            case 0x0:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_news_report_isdb);
                break;
            case 0x1:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_sports_isdb);
                break;
            case 0x2:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_information_tabloid_show_isdb);
                break;
            case 0x3:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_dramas_isdb);
                break;
            case 0x4:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_music_isdb);
                break;
            case 0x5:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_variety_isdb);
                break;
            case 0x6:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_movies_isdb);
                break;
            case 0x7:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_animation_special_effects_isdb);
                break;
            case 0x8:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_documentary_culture_isdb);
                break;
            case 0x9:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_theatre_public_performance_isdb);
                break;
            case 0xA:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_hobby_education_isdb);
                break;
            case 0xB:
                genreArray = context.getResources().getStringArray(R.array.str_arr_genre_welfare_isdb);
                break;
            default:
                break;
        }
        if (genreArray != null && nibbleLevel2 < genreArray.length) {
            broadcastGenres[0] = genreArray[nibbleLevel2];
        }
        return TextUtils.isEmpty(broadcastGenres[0]) ? null : broadcastGenres;
    }
}
