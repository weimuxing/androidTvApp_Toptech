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
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.tv.TvContract;
import android.media.tv.TvContentRating;
import android.media.tv.TvTrackInfo;
import android.text.format.Time;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.CaptioningManager;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tv.TvCcManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.VideoInfo;
import com.mstar.android.tvapi.common.vo.CaptionOptionSetting;
import com.mstar.android.tvapi.common.vo.CCSetting;
import com.mstar.android.tvapi.common.vo.DTVSpecificProgInfo;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.AudioInfo;
import com.mstar.android.tvapi.dtv.vo.MwAtscEasInfo;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscEpgEventInfo;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscEpgRating;
import com.mstar.android.tv.util.Constant.ScreenSaverMode;
import com.mstar.android.tv.util.Constant.AtscAtvScreenSaverMode;
import com.mstar.android.tv.inputservice.R;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

public class BroadcastTypeAtsc extends BroadcastType {
    private static final String TAG = "BroadcastTypeAtsc";

    private static final String RATING_SYSTEM_US_TV = "US_TV";

    private static final String RATING_SYSTEM_US_MV = "US_MV";

    private static final String RATING_SYSTEM_CA_TV_EN = "CA_TV_EN";

    private static final String RATING_SYSTEM_CA_TV_FR = "CA_TV_FR";

    private static final String RATING_US_TV_NONE = "US_TV_NONE";

    private static final String RATING_US_TV_Y = "US_TV_Y";

    private static final String RATING_US_TV_Y7 = "US_TV_Y7";

    private static final String RATING_US_TV_G = "US_TV_G";

    private static final String RATING_US_TV_PG = "US_TV_PG";

    private static final String RATING_US_TV_14 = "US_TV_14";

    private static final String RATING_US_TV_MA = "US_TV_MA";

    private static final String RATING_US_MV_G = "US_MV_G";

    private static final String RATING_US_MV_PG = "US_MV_PG";

    private static final String RATING_US_MV_PG13 = "US_MV_PG13";

    private static final String RATING_US_MV_R = "US_MV_R";

    private static final String RATING_US_MV_NC17 = "US_MV_NC17";

    private static final String RATING_US_MV_X = "US_MV_X";

    private static final String RATING_US_MV_NR = "US_MV_NR";

    private static final String RATING_CA_TV_EN_EXEMPT = "CA_TV_EN_EXEMPT";

    private static final String RATING_CA_TV_EN_C = "CA_TV_EN_C";

    private static final String RATING_CA_TV_EN_C8 = "CA_TV_EN_C8";

    private static final String RATING_CA_TV_EN_G = "CA_TV_EN_G";

    private static final String RATING_CA_TV_EN_PG = "CA_TV_EN_PG";

    private static final String RATING_CA_TV_EN_14 = "CA_TV_EN_14";

    private static final String RATING_CA_TV_EN_18 = "CA_TV_EN_18";

    private static final String RATING_CA_TV_FR_E = "CA_TV_FR_E";

    private static final String RATING_CA_TV_FR_G = "CA_TV_FR_G";

    private static final String RATING_CA_TV_FR_8 = "CA_TV_FR_8";

    private static final String RATING_CA_TV_FR_13 = "CA_TV_FR_13";

    private static final String RATING_CA_TV_FR_16 = "CA_TV_FR_16";

    private static final String RATING_CA_TV_FR_18 = "CA_TV_FR_18";

    private static final String SUBRATING_US_TV_D = "US_TV_D";

    private static final String SUBRATING_US_TV_L = "US_TV_L";

    private static final String SUBRATING_US_TV_S = "US_TV_S";

    private static final String SUBRATING_US_TV_V = "US_TV_V";

    private static final String SUBRATING_US_TV_FV = "US_TV_FV";

    private TvAtscChannelManager mTvAtscChannelManager = TvAtscChannelManager.getInstance();

    public static final TvContentRating UNRATED = TvContentRating.createRating("null", "null", "null", null);

    @Override
    public boolean tune(int inputSource, String displayNumber, int serviceType) {
        final String DELIMITER = "-";
        String[] strs = displayNumber.split(DELIMITER);
        if (strs.length == 0 || strs.length > 2 ||
                (inputSource != TvCommonManager.INPUT_SOURCE_DTV &&
                inputSource != TvCommonManager.INPUT_SOURCE_ATV) ) {
            return false;
        }
        try {
            int majorNumber = Integer.parseInt(strs[0]);
            int minorNumber = strs.length == 2 ? Integer.parseInt(strs[1]) : 65535;
            return mTvAtscChannelManager.programSel(majorNumber, minorNumber);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<TifChannelInfo> buildTifChannelInfoList(String inputId) {
        int serviceNum = mTvAtscChannelManager.getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
        ArrayList<TifChannelInfo> tifChannelInfoList = new ArrayList<TifChannelInfo>();
        for (int i = 0; i < serviceNum; i++) {
            ProgramInfo pgi = mTvAtscChannelManager.getProgramInfo(i);
            if (pgi == null || !pgi.isVisible || pgi.isHide) {
                continue;
            }
            TifChannelInfo info = new TifChannelInfo();
            info.inputId = inputId;
            info.serviceType = TunerUtility.getServiceType(pgi.serviceType);
            if (pgi.serviceType == TvChannelManager.SERVICE_TYPE_ATV) {
                info.type = TvContract.Channels.TYPE_OTHER;
            } else {
                info.type = TunerUtility.getDtvChannelType();
            }
            info.transportStreamId = pgi.transportStreamId;  // TODO: tvos does not send transportStreamId for ATSC
            info.serviceId = pgi.number;
            info.displayNumber = mTvAtscChannelManager.getDispChannelNum(pgi);
            info.displayName = mTvAtscChannelManager.getDispChannelName(pgi);
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
        List<TvContentRating> canonicalRatings = new ArrayList<TvContentRating>();
        if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            ProgramInfo programInfo = mTvAtscChannelManager.getCurrentProgramInfo();
            tifProgramInfo.channelId = channelId;
            Time baseTime = TvTimerManager.getInstance().getCurrentTvTime();
            AtscEpgEventInfo eventInfo = mTvEpgManager.getAtscEventInfoByTime(programInfo.majorNum,
                    programInfo.minorNum, programInfo.number, programInfo.progId, baseTime);
            if (eventInfo != null) {
                tifProgramInfo.title = eventInfo.sName;
                tifProgramInfo.startTime = (long) TvTimerManager.getInstance().convertGPSTime2UTC(eventInfo.startTime) * 1000;
                tifProgramInfo.endTime = tifProgramInfo.startTime + eventInfo.durationTime * 1000;
                TvContentRating[] contentRatings = createRatingFromAtscEpgEventInfo(eventInfo);
                if (contentRatings != null) {
                    Collections.addAll(canonicalRatings, contentRatings);
                }
            }
            AtscEpgEventInfo epgEventExtedInfo = mTvEpgManager.getEventExtendInfoByTime(
                    programInfo.majorNum, programInfo.minorNum,
                    (int) programInfo.serviceType, programInfo.progId, baseTime);
            if (epgEventExtedInfo != null) {
                tifProgramInfo.shortDescription = epgEventExtedInfo.sExtendedText;
            }
            VideoInfo videoInfo = TvPictureManager.getInstance().getVideoInfo();
            tifProgramInfo.videoWidth = videoInfo.hResolution;
            tifProgramInfo.videoHeight = videoInfo.vResolution;
            DtvAudioInfo audioInfo = mTvAtscChannelManager.getAudioInfo();
            if (audioInfo != null && audioInfo.audioLangNum > 0) {
                AudioInfo currentAudioInfo = audioInfo.audioInfos[audioInfo.currentAudioIndex];
                tifProgramInfo.audioLanguage = String.valueOf(currentAudioInfo.isoLangInfo.isoLangInfo);
            }
        } else if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV) {
            tifProgramInfo.channelId = channelId;
            VideoInfo videoInfo = TvPictureManager.getInstance().getVideoInfo();
            tifProgramInfo.videoWidth = videoInfo.hResolution;
            tifProgramInfo.videoHeight = videoInfo.vResolution;
        }
        TvContentRating[] contentRatings = createRatingFromString(mTvAtscChannelManager.getCurrentRatingInformation());
        if (contentRatings != null) {
            Collections.addAll(canonicalRatings, contentRatings);
        }
        if (canonicalRatings.size() > 0) {
            tifProgramInfo.contentRatings = canonicalRatings.toArray(new TvContentRating[canonicalRatings.size()]);
        }
        DTVSpecificProgInfo dtvSpecProgInfo = mTvAtscChannelManager.getCurrentProgramSpecificInfo();
        tifProgramInfo.ccService = dtvSpecProgInfo.ccService;
        return tifProgramInfo;
    }

    @Override
    public ProgramInfo getProgramInfoByIndex(int index) {
        return mTvAtscChannelManager.getProgramInfo(index);
    }

    @Override
    public ProgramInfo getCurrentProgramInfo() {
        return mTvAtscChannelManager.getCurrentProgramInfo();
    }

    @Override
    public ArrayList<TifProgramInfo> buildTifProgramInfoList(Context context, ProgramInfo programInfo, long channelId) {
        ArrayList<TifProgramInfo> tifProgramInfoList = new ArrayList<TifProgramInfo>();
        List<AtscEpgEventInfo> eventInfoList = buildEventInfoList(programInfo);
        boolean isCurrentChannel = isCurrentChannel(programInfo);
        if (eventInfoList == null) {
            //Log.d(TAG, "buildTifProgramInfoList: eventInfoList == null");
            return tifProgramInfoList;
        }
        long currentTimeInMillis = TvTimerManager.getInstance().getCurrentTvTime().toMillis(false);
        for (AtscEpgEventInfo eventInfo : eventInfoList) {
            TifProgramInfo tifProgramInfo = new TifProgramInfo();
            tifProgramInfo.channelId = channelId;
            tifProgramInfo.title = eventInfo.sName;
            tifProgramInfo.startTime = (long) TvTimerManager.getInstance().convertGPSTime2UTC(eventInfo.startTime) * 1000;
            tifProgramInfo.endTime = (long) TvTimerManager.getInstance().convertGPSTime2UTC(eventInfo.endTime) * 1000;
            Time startTime = new Time();
            startTime.set(tifProgramInfo.startTime);
            AtscEpgEventInfo epgEventExtedInfo = mTvEpgManager.getEventExtendInfoByTime(
                    programInfo.majorNum, programInfo.minorNum, (int)programInfo.serviceType,
                    programInfo.progId, startTime);
            if (epgEventExtedInfo == null) {
                tifProgramInfo.shortDescription = context.getResources().getString(R.string.str_epg_event_extend_no_info);
            } else {
                tifProgramInfo.shortDescription = epgEventExtedInfo.sExtendedText;
            }
            if (isCurrentChannel &&
                    currentTimeInMillis >= tifProgramInfo.startTime &&
                    currentTimeInMillis <= tifProgramInfo.endTime) {
                tifProgramInfo.contentRatings = createRatingFromString(mTvAtscChannelManager.getCurrentRatingInformation());
            } else {
                tifProgramInfo.contentRatings = createRatingFromAtscEpgRating(eventInfo.stRating);
            }
            tifProgramInfoList.add(tifProgramInfo);
        }
        return tifProgramInfoList;
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
        if (captioningManager.isEnabled()) {
            TvCcManager.getInstance().setClosedCaptionMode(TvCcManager.CLOSED_CAPTION_ON);
            TvCcManager.getInstance().stopCc();
            CaptioningManager.CaptionStyle cs = captioningManager.getUserStyle();
            CaptionOptionSetting ccOption = new CaptionOptionSetting(
                    TvCcManager.CC_MODE_CUSTOM,
                    getCcColor(cs.foregroundColor),
                    getCcColor(cs.backgroundColor),
                    getCcOpacity(cs.foregroundColor),
                    getCcOpacity(cs.backgroundColor),
                    getCcFontSize(captioningManager.getFontScale()),
                    getCcFontSytle(fontFamily),
                    getCcEdgeType(cs.edgeType),
                    getCcColor(cs.edgeColor),
                    TvCcManager.CC_ITALIC_OFF,
                    TvCcManager.CC_UNDERLINE_OFF
                    );
            TvCcManager.getInstance().setAdvancedSetting(ccOption, 1);
            CCSetting ccSetting = TvCcManager.getInstance().getCCSetting();
            ccSetting.advancedMode = 1;
            TvCcManager.getInstance().setCCSetting(ccSetting);
            TvCcManager.getInstance().startCc();
        } else {
            TvCcManager.getInstance().setClosedCaptionMode(TvCcManager.CLOSED_CAPTION_OFF);
            TvCcManager.getInstance().stopCc();
        }
    }

    @Override
    public void setCaptionEnabled(boolean enabled, Object settings) {
        // Do nothing.
    }

    private short getCcFontSytle(String fontFamily) {
        if (fontFamily == null) {
            return TvCcManager.CC_FONT_STYLE_DEFAULT;
        } else if (fontFamily.equals("CC0_TiresiasScreenfont")) {
            return TvCcManager.CC_FONT_STYLE_DEFAULT;
        } else if (fontFamily.equals("CC1_Gentium")) {
            return TvCcManager.CC_FONT_STYLE_STYLE1;
        } else if (fontFamily.equals("CC2_Bitstream_Vera_Serif")) {
            return TvCcManager.CC_FONT_STYLE_STYLE2;
        } else if (fontFamily.equals("CC3_Bitstream_Vera_Sans_Mono")) {
            return TvCcManager.CC_FONT_STYLE_STYLE3;
        } else if (fontFamily.equals("CC4_Bitstream_Vera_Sans")) {
            return TvCcManager.CC_FONT_STYLE_STYLE4;
        } else if (fontFamily.equals("CC5_Minya_Nouvelle")) {
            return TvCcManager.CC_FONT_STYLE_STYLE5;
        } else if (fontFamily.equals("CC6_MarketingScript")) {
            return TvCcManager.CC_FONT_STYLE_STYLE6;
        } else if (fontFamily.equals("CC7_Engebrechtre_Expand")) {
            return TvCcManager.CC_FONT_STYLE_STYLE7;
        }
        return TvCcManager.CC_FONT_STYLE_DEFAULT;
    }

    private short getCcEdgeType(int edgeType) {
        switch (edgeType) {
            case CaptioningManager.CaptionStyle.EDGE_TYPE_DEPRESSED:
                return TvCcManager.CC_EDGE_STYLE_DEPRESSED;
            case CaptioningManager.CaptionStyle.EDGE_TYPE_DROP_SHADOW:
                return TvCcManager.CC_EDGE_STYLE_RIGHT_SHADOW;
            case CaptioningManager.CaptionStyle.EDGE_TYPE_NONE:
                return TvCcManager.CC_EDGE_STYLE_NONE;
            case CaptioningManager.CaptionStyle.EDGE_TYPE_OUTLINE:
                return TvCcManager.CC_EDGE_STYLE_UNIFORM;
            case CaptioningManager.CaptionStyle.EDGE_TYPE_RAISED:
                return TvCcManager.CC_EDGE_STYLE_RAISED;
            case CaptioningManager.CaptionStyle.EDGE_TYPE_UNSPECIFIED:
            default:
                return TvCcManager.CC_EDGE_STYLE_NONE;
        }
    }

    private short getCcFontSize(float fontScale) {
        if (fontScale == 0.5) {
            return TvCcManager.CC_FONT_SIZE_SMALL;
        } else if (fontScale == 1.5) {
            return TvCcManager.CC_FONT_SIZE_LARGE;
        } else {
            return TvCcManager.CC_FONT_SIZE_NORMAL;
        }
    }

    private short getCcOpacity(int argb) {
        int alpha = (int)(argb >>> 24);
        if (alpha == 0x80) {
            return TvCcManager.CC_OPACITY_TRANSLUCENT;
        } else if (alpha == 0x01) {
            return TvCcManager.CC_OPACITY_TRANSPARENT;
        }
        return TvCcManager.CC_OPACITY_DEFAULT;
    }

    private short getCcColor(int argb) {
        int rgb = argb | 0xff000000;
        switch (rgb) {
            case Color.BLACK:
                return TvCcManager.CC_COLOR_BLACK;
            case Color.RED:
                return TvCcManager.CC_COLOR_RED;
            case Color.GREEN:
                return TvCcManager.CC_COLOR_GREEN;
            case Color.YELLOW:
                return TvCcManager.CC_COLOR_YELLOW;
            case Color.BLUE:
                return TvCcManager.CC_COLOR_BLUE;
            case Color.MAGENTA:
                return TvCcManager.CC_COLOR_MAGENTA;
            case Color.CYAN:
                return TvCcManager.CC_COLOR_CYAN;
            case Color.WHITE:
            default:
                return TvCcManager.CC_COLOR_WHITE;
        }
    }

    @Override
    public String getScreenSaverString(Context context, int status, int currentInputSource) {
        int resId = 0;
        if ((currentInputSource == TvCommonManager.INPUT_SOURCE_ATV &&
                        status == AtscAtvScreenSaverMode.ATV_SS_NO_CHANNEL) ||
                (currentInputSource == TvCommonManager.INPUT_SOURCE_DTV &&
                        status == ScreenSaverMode.DTV_SS_UNSUPPORTED_FORMAT)) {
            resId = R.string.str_no_channel_banner;
        } else {
            return super.getScreenSaverString(context, status, currentInputSource);
        }
        if (resId == 0) {
            return "";
        } else {
            return context.getResources().getString(resId);
        }
    }

    @Override
    public boolean isSameAsCurrentChannel(String displayNumber, int serviceType) {
        final String DELIMITER = "-";
        String[] strs = displayNumber.split(DELIMITER);
        if (strs.length == 0 || strs.length > 2 ||
                (serviceType != TvChannelManager.SERVICE_TYPE_ATV && strs.length == 1)) {
            return false;
        }
        try {
            int majorNumber = Integer.parseInt(strs[0]);
            int minorNumber = strs.length == 2 ? Integer.parseInt(strs[1]) : 0;
            ProgramInfo curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();
            if ((curProgInfo.majorNum == majorNumber)
                    && (curProgInfo.minorNum == minorNumber)
                    && (curProgInfo.serviceType == serviceType)) {
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public MwAtscEasInfo getEasInfo() {
        return mTvAtscChannelManager.getEASInProgress();
    }

    @Override
    public void easTune(int majorNumber, int minorNumber) {
        mTvAtscChannelManager.programSel(majorNumber, minorNumber);
    }

    private List<AtscEpgEventInfo> buildEventInfoList(ProgramInfo programInfo) {
        List<AtscEpgEventInfo> eventInfoList = null;
        Time baseTime = TvTimerManager.getInstance().getCurrentTvTime();
        boolean isBegin = mTvEpgManager.beginToGetEventInformation(programInfo.number,
                programInfo.majorNum, programInfo.minorNum, programInfo.progId);
        if (isBegin) {
            int totalEventCount = mTvEpgManager.getAtscEventCount(programInfo.majorNum,
                    programInfo.minorNum, (int)programInfo.number, programInfo.progId, baseTime);
            Log.e(TAG, "buildEventInfoList: totalEventCount:" + totalEventCount);
            if (totalEventCount > 0) {
                eventInfoList = new ArrayList<AtscEpgEventInfo>();
                AtscEpgEventInfo info = mTvEpgManager.getFirstEventInformation(baseTime);
                do {
                    if (info == null) {
                        break;
                    } else {
                        if (eventInfoList.size() == 0) {
                            info.endTime = info.startTime + info.durationTime;
                            eventInfoList.add(info);
                        } else {
                            AtscEpgEventInfo lastInfo = eventInfoList.get(eventInfoList.size() - 1);
                            if (lastInfo.startTime != info.startTime) {
                                info.endTime = info.startTime + info.durationTime;
                                eventInfoList.add(info);
                            }
                        }
                    }
                    totalEventCount--;
                    if (totalEventCount <= 0) {
                        break;
                    }
                    info = mTvEpgManager.getNextEventInformation();
                } while (true);
            }
        }
        mTvEpgManager.endToGetEventInformation();
        return eventInfoList;
    }

    private TvContentRating[] createRatingFromString(String ratingInfo) {
        final String NO_RATING = "No Rating";
        List<TvContentRating> canonicalRatings = new ArrayList<TvContentRating>();
        if (ratingInfo == null || ratingInfo.equals(NO_RATING)) {
            canonicalRatings.add(UNRATED);
        } else {
            final String DELIMITER = "/";
            final String US_TV_RATING_SYSTEM = "TV-";
            final String US_MOVIE_RATING_SYSTEM = "MPAA-";
            final String CANADA_TV_EN_RATING_SYSTEM = "CE-";
            final String CANADA_TV_FR_RATING_SYSTEM = "CANADA FRE-";
            String ratings[] = ratingInfo.split(DELIMITER);
            for (int i = 0; i < ratings.length; i++) {
                final String rating = ratings[i];
                String mainRating;
                int index = 0;
                if (rating.startsWith(US_TV_RATING_SYSTEM)) {
                    final String RATING_NONE = "NONE";
                    final String RATING_Y = "Y";
                    final String RATING_Y7 = "Y7";
                    final String RATING_G = "G";
                    final String RATING_PG = "PG";
                    final String RATING_14 = "14";
                    final String RATING_MA = "MA";
                    index += US_TV_RATING_SYSTEM.length();
                    if (rating.substring(index).contains(RATING_NONE)) {
                        mainRating = RATING_US_TV_NONE;
                        index += RATING_NONE.length();
                    } else if (rating.substring(index).contains(RATING_Y7)) {
                        mainRating = RATING_US_TV_Y7;
                        index += RATING_Y7.length();
                    } else if (rating.substring(index).contains(RATING_PG)) {
                        mainRating = RATING_US_TV_PG;
                        index += RATING_PG.length();
                    } else if (rating.substring(index).contains(RATING_14)) {
                        mainRating = RATING_US_TV_14;
                        index += RATING_14.length();
                    } else if (rating.substring(index).contains(RATING_MA)) {
                        mainRating = RATING_US_TV_MA;
                        index += RATING_MA.length();
                    } else if (rating.substring(index).contains(RATING_Y)) {
                        mainRating = RATING_US_TV_Y;
                        index += RATING_Y.length();
                    } else if (rating.substring(index).contains(RATING_G)) {
                        mainRating = RATING_US_TV_G;
                        index += RATING_G.length();
                    } else {
                        continue;
                    }
                    final String SUBRATING_D = "D";
                    final String SUBRATING_L = "L";
                    final String SUBRATING_S = "S";
                    final String SUBRATING_V = "V";
                    final String SUBRATING_FV = "-FV";
                    List<String> subRatings = new ArrayList<String>();
                    if (rating.substring(index).contains(SUBRATING_D)) {
                        subRatings.add(SUBRATING_US_TV_D);
                    } if (rating.substring(index).contains(SUBRATING_L)) {
                        subRatings.add(SUBRATING_US_TV_L);
                    } if (rating.substring(index).contains(SUBRATING_S)) {
                        subRatings.add(SUBRATING_US_TV_S);
                    } if (rating.substring(index).contains(SUBRATING_V)) {
                        subRatings.add(SUBRATING_US_TV_V);
                    } if (rating.substring(index).contains(SUBRATING_FV)) {
                        subRatings.add(SUBRATING_US_TV_FV);
                    }
                    canonicalRatings.add(TvContentRating.createRating(
                            SYSTEM_RATING_DOMAIN,
                            RATING_SYSTEM_US_TV, mainRating,
                            subRatings.toArray(new String[subRatings.size()])));
                } else if (rating.startsWith(US_MOVIE_RATING_SYSTEM)) {
                    final String RATING_G = "G";
                    final String RATING_PG = "PG";
                    final String RATING_PG13 = "PG13";
                    final String RATING_R = "R";
                    final String RATING_NC17 = "NC17";
                    final String RATING_X = "X";
                    final String RATING_NR = "NR";
                    index += US_MOVIE_RATING_SYSTEM.length();
                    if (rating.substring(index).equals(RATING_G)) {
                        mainRating = RATING_US_MV_G;
                    } else if (rating.substring(index).equals(RATING_PG)) {
                        mainRating = RATING_US_MV_PG;
                    } else if (rating.substring(index).equals(RATING_PG13)) {
                        mainRating = RATING_US_MV_PG13;
                    } else if (rating.substring(index).equals(RATING_R)) {
                        mainRating = RATING_US_MV_R;
                    } else if (rating.substring(index).equals(RATING_NC17)) {
                        mainRating = RATING_US_MV_NC17;
                    } else if (rating.substring(index).equals(RATING_X)) {
                        mainRating = RATING_US_MV_X;
                    } else if (rating.substring(index).equals(RATING_NR)) {
                        mainRating = RATING_US_MV_NR;
                    } else {
                        continue;
                    }
                    canonicalRatings.add(TvContentRating.createRating(
                            SYSTEM_RATING_DOMAIN,
                            RATING_SYSTEM_US_MV, mainRating,
                            (String[]) null));
                } else if (rating.startsWith(CANADA_TV_EN_RATING_SYSTEM)) {
                    final String RATING_E = "E";
                    final String RATING_C = "C";
                    final String RATING_C8 = "C8+";
                    final String RATING_G = "G";
                    final String RATING_PG = "PG";
                    final String RATING_14 = "14+";
                    final String RATING_18 = "18+";
	             index += CANADA_TV_EN_RATING_SYSTEM.length();
                    if (rating.substring(index).equals(RATING_E)) {
                        mainRating = RATING_CA_TV_EN_EXEMPT;
                    } else if (rating.substring(index).equals(RATING_C)) {
                        mainRating = RATING_CA_TV_EN_C;
                    } else if (rating.substring(index).equals(RATING_C8)) {
                        mainRating = RATING_CA_TV_EN_C8;
                    } else if (rating.substring(index).equals(RATING_G)) {
                        mainRating = RATING_CA_TV_EN_G;
                    } else if (rating.substring(index).equals(RATING_PG)) {
                        mainRating = RATING_CA_TV_EN_PG;
                    } else if (rating.substring(index).equals(RATING_14)) {
                        mainRating = RATING_CA_TV_EN_14;
                    } else if (rating.substring(index).equals(RATING_18)) {
                        mainRating = RATING_CA_TV_EN_18;
                    } else {
                        continue;
                    }
                    canonicalRatings.add(TvContentRating.createRating(
                            SYSTEM_RATING_DOMAIN,
                            RATING_SYSTEM_CA_TV_EN, mainRating,
                            (String[]) null));
                } else if (rating.startsWith(CANADA_TV_FR_RATING_SYSTEM)) {
                    final String RATING_E = "E";
                    final String RATING_G = "G";
                    final String RATING_8 = "8 ans+";
                    final String RATING_13 = "13 ans+";
                    final String RATING_16 = "16 ans+";
                    final String RATING_18 = "18 ans+";
	             index += CANADA_TV_FR_RATING_SYSTEM.length();
                    if (rating.substring(index).equals(RATING_E)) {
                        mainRating = RATING_CA_TV_FR_E;
                    } else if (rating.substring(index).equals(RATING_G)) {
                        mainRating = RATING_CA_TV_FR_G;
                    } else if (rating.substring(index).equals(RATING_8)) {
                        mainRating = RATING_CA_TV_FR_8;
                    } else if (rating.substring(index).equals(RATING_13)) {
                        mainRating = RATING_CA_TV_FR_13;
                    } else if (rating.substring(index).equals(RATING_16)) {
                        mainRating = RATING_CA_TV_FR_16;
                    } else if (rating.substring(index).equals(RATING_18)) {
                        mainRating = RATING_CA_TV_FR_18;
                    } else {
                        continue;
                    }
                    canonicalRatings.add(TvContentRating.createRating(
                            SYSTEM_RATING_DOMAIN,
                            RATING_SYSTEM_CA_TV_FR, mainRating,
                            (String[]) null));
                }
            }
        }
        return canonicalRatings.size() > 0 ? canonicalRatings.toArray(new TvContentRating[canonicalRatings.size()]) : null;
    }

    private TvContentRating[] createRatingFromAtscEpgEventInfo(AtscEpgEventInfo epgEventInfo) {
        List<TvContentRating> canonicalRatings = new ArrayList<TvContentRating>();
        if (epgEventInfo.noDimension > 0) {
            String ratingSystem = epgEventInfo.sRegion5Name;
            for (int i = 0; i < epgEventInfo.regin5Dimensions.length; i++) {
                List<String> subRatings = new ArrayList<String>();
                for (int j = 0; j < epgEventInfo.regin5Dimensions[i].valuesDefined; j++) {
                    if (!TextUtils.isEmpty(epgEventInfo.regin5Dimensions[i].abbRatingText[j])) {
                        subRatings.add(epgEventInfo.regin5Dimensions[i].abbRatingText[j]);
                    }
                }
                if (subRatings.size() > 0) {
                    canonicalRatings.add(TvContentRating.createRating(
                            "com.mstar.android.tv.rrt5rating",
                            ratingSystem, epgEventInfo.regin5Dimensions[i].dimensionName,
                            subRatings.toArray(new String[subRatings.size()])));
                }
            }
        }
        return canonicalRatings.size() > 0 ? canonicalRatings.toArray(new TvContentRating[canonicalRatings.size()]) : null;
    }

    private TvContentRating[] createRatingFromAtscEpgRating(AtscEpgRating epgRating) {
        List<TvContentRating> canonicalRatings = new ArrayList<TvContentRating>();
        if (epgRating.ratingRxIsOK == 1) {
            boolean isValid = true;
            String mainRating = "";
            List<String> subRatings = new ArrayList<String>();
            switch (epgRating.tvRatingForEntire) {
                case 0:
                    if (epgRating.tvRatingForChild == 1) {
                        mainRating = RATING_US_TV_Y;
                    } else if (epgRating.tvRatingForChild == 2) {
                        mainRating = RATING_US_TV_Y7;
                    } else {
                        isValid = false;
                    }
                    break;
                case 1:
                    mainRating = RATING_US_TV_NONE;
                    break;
                case 2:
                    mainRating = RATING_US_TV_G;
                    break;
                case 3:
                    mainRating = RATING_US_TV_PG;
                    break;
                case 4:
                    mainRating = RATING_US_TV_14;
                    break;
                case 5:
                    mainRating = RATING_US_TV_MA;
                    break;
                default:
                    isValid = false;
                    break;
            }

            if (epgRating.fantasyViolence == 1) {
                if (mainRating.equals(RATING_US_TV_Y7)) {
                    subRatings.add(SUBRATING_US_TV_FV);
                } else {
                    isValid = false;
                }
            }
            if (epgRating.dialog == 1) {
                if (mainRating.equals(RATING_US_TV_PG) ||
                        mainRating.equals(RATING_US_TV_14)) {
                    subRatings.add(SUBRATING_US_TV_D);
                }
                else {
                    isValid = false;
                }
            }
            if (epgRating.language == 1) {
                if (mainRating.equals(RATING_US_TV_PG) ||
                        mainRating.equals(RATING_US_TV_14) ||
                        mainRating.equals(RATING_US_TV_MA)) {
                    subRatings.add(SUBRATING_US_TV_L);
                } else {
                    isValid = false;
                }
            }
            if (epgRating.sexualContent == 1) {
                if (mainRating.equals(RATING_US_TV_PG) ||
                        mainRating.equals(RATING_US_TV_14) ||
                        mainRating.equals(RATING_US_TV_MA)) {
                    subRatings.add(SUBRATING_US_TV_S);
                } else {
                    isValid = false;
                }
            }
            if (epgRating.violence == 1) {
                if (mainRating.equals(RATING_US_TV_PG) ||
                        mainRating.equals(RATING_US_TV_14) ||
                        mainRating.equals(RATING_US_TV_MA)) {
                    subRatings.add(SUBRATING_US_TV_V);
                } else {
                    isValid = false;
                }
            }
            if (isValid && !TextUtils.isEmpty(mainRating)) {
                canonicalRatings.add(TvContentRating.createRating(
                        SYSTEM_RATING_DOMAIN,
                        RATING_SYSTEM_US_TV, mainRating,
                        subRatings.toArray(new String[subRatings.size()])));
            }
        }
        if (epgRating.mpaaFlag == 1) {
            String mainRating = "";
            switch (epgRating.mpaaRatingD2) {
                case 2:
                    mainRating = RATING_US_MV_G;
                    break;
                case 3:
                    mainRating = RATING_US_MV_PG;
                    break;
                case 4:
                    mainRating = RATING_US_MV_PG13;
                    break;
                case 5:
                    mainRating = RATING_US_MV_R;
                    break;
                case 6:
                    mainRating = RATING_US_MV_NC17;
                    break;
                case 7:
                    mainRating = RATING_US_MV_X;
                    break;
                case 8:
                    mainRating = RATING_US_MV_NR;
                    break;
                default:
                    break;
            }
            if (!TextUtils.isEmpty(mainRating)) {
                canonicalRatings.add(TvContentRating.createRating(
                        SYSTEM_RATING_DOMAIN,
                        RATING_SYSTEM_US_MV, mainRating,
                        (String[]) null));
            }
        }
        if (epgRating.caEngFlag == 1) {
            String mainRating = "";
            switch (epgRating.caEngRatingD0) {
                case 0:
                    mainRating = RATING_CA_TV_EN_EXEMPT;
                    break;
                case 1:
                    mainRating = RATING_CA_TV_EN_C;
                    break;
                case 2:
                    mainRating = RATING_CA_TV_EN_C8;
                    break;
                case 3:
                    mainRating = RATING_CA_TV_EN_G;
                    break;
                case 4:
                    mainRating = RATING_CA_TV_EN_PG;
                    break;
                case 5:
                    mainRating = RATING_CA_TV_EN_14;
                    break;
                case 6:
                    mainRating = RATING_CA_TV_EN_18;
                    break;
                default:
                    break;
            }
            if (!TextUtils.isEmpty(mainRating)) {
                canonicalRatings.add(TvContentRating.createRating(
                        SYSTEM_RATING_DOMAIN,
                        RATING_SYSTEM_CA_TV_EN, mainRating,
                        (String[]) null));
            }
        }
        if (epgRating.caFreFlag == 1) {
            String mainRating = "";
            switch (epgRating.caFreRatingD1) {
                case 0:
                    mainRating = RATING_CA_TV_FR_E;
                    break;
                case 1:
                    mainRating = RATING_CA_TV_FR_G;
                    break;
                case 2:
                    mainRating = RATING_CA_TV_FR_8;
                    break;
                case 3:
                    mainRating = RATING_CA_TV_FR_13;
                    break;
                case 4:
                    mainRating = RATING_CA_TV_FR_16;
                    break;
                case 5:
                    mainRating = RATING_CA_TV_FR_18;
                    break;
                default:
                    break;
            }
            if (!TextUtils.isEmpty(mainRating)) {
                canonicalRatings.add(TvContentRating.createRating(
                        SYSTEM_RATING_DOMAIN,
                        RATING_SYSTEM_CA_TV_FR, mainRating,
                        (String[]) null));
            }
        }
        return canonicalRatings.size() > 0 ? canonicalRatings.toArray(new TvContentRating[canonicalRatings.size()]) : null;
    }

    private boolean isCurrentChannel(ProgramInfo programInfo) {
        ProgramInfo curProgInfo = mTvAtscChannelManager.getCurrentProgramInfo();
        if ((curProgInfo.majorNum == programInfo.majorNum)
                && (curProgInfo.minorNum == programInfo.minorNum)
                && (curProgInfo.serviceType == programInfo.serviceType)) {
            return true;
        } else {
            return false;
        }
    }
}
