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

package mstar.tvsetting.factory.until;

public class Constant {
    // short
    public static final short CDCA_RC_OK = 0x00;
    public static final short CDCA_RC_UNKNOWN = 0x01;
    public static final short CDCA_RC_POINTER_INVALID = 0x02;
    public static final short CDCA_RC_CARD_INVALID = 0x03;
    public static final short CDCA_RC_PIN_INVALID = 0x04;
    public static final short CDCA_RC_DATASPACE_SMALL = 0x06;
    public static final short CDCA_RC_CARD_PAIROTHER = 0x07;
    public static final short CDCA_RC_DATA_NOT_FIND = 0x08;
    public static final short CDCA_RC_PROG_STATUS_INVALID = 0x09;
    public static final short CDCA_RC_CARD_NO_ROOM = 0x0A;
    public static final short CDCA_RC_WORKTIME_INVALID = 0x0B;
    public static final short CDCA_RC_IPPV_CANNTDEL = 0x0C;
    public static final short CDCA_RC_CARD_NOPAIR = 0x0D;
    public static final short CDCA_RC_WATCHRATING_INVALID = 0x0E;
    public static final short CDCA_RC_CARD_NOTSUPPORT = 0x0F;
    public static final short CDCA_RC_DATA_ERROR = 0x10;
    public static final short CDCA_RC_FEEDTIME_NOT_ARRIVE = 0x11;
    public static final short CDCA_Detitle_All_Read = 0x00;
    public static final short CDCA_Detitle_Received = 0x01;
    public static final short CDCA_Detitle_Space_Small = 0x02;
    public static final short CDCA_Detitle_Ignore = 0x03;
    // boolean
    public static boolean lockKey = true;
    // String
    public static final String OPMODE = "OP_MODE";
    public static final String TUNER_AVAIABLE = "TUNER_AVAILABLE";
    public static final String LNBOPTION_PAGETYPE = "LNBOPTION_PAGETYPE";
    public static final String LNBOPTION_EDITOR_PAGETYPE = "LNBOPTION_EDITOR_PAGETYPE";
    public static final String LNBOPTION_EDITOR_ACTIONTYPE = "LNBOPTION_EDITOR_ACTIONTYPE";
    public static final String LNBOPTION_EDITOR_INDEX = "LNBOPTION_EDITOR_INDEX";
    public static final String LNBOPTION_MOTOR_ACTIONTYPE = "LNBOPTION_MOTOR_ACTIONTYPE";
    public static final String LNBMOTOR_EDITOR_DISEQC_VERSION = "DISEQC_VERSION";
    public static final String LNBMOTOR_EDITOR_DISEQC_1_2 = "1.2";
    public static final String LNBMOTOR_EDITOR_DISEQC_1_3 = "1.3";
    public static final String LNBOPTION_MOTOR_FOCUS = "LNBOPTION_MOTOR_FOCUS";
    public static final String PREFERENCES_TV_SETTING = "TvSetting";
    public static final String PREFERENCES_IS_AUTOSCAN_LAUNCHED = "autoTuningLaunchedBefore";
    public static final String TV_EVENT_LISTENER_READY = "TVEventListenerReady";
    public static final String PARENTAL_CONTROL_MENU_PERMITTED = "isParentalControlMenuPermitted";
    public static final String PREFERENCES_INPUT_SOURCE = "INPUT_SOURCE";
    public static final String PREFERENCES_PREVIOUS_INPUT_SOURCE = "PREVIOUS_INPUT_SOURCE";
    public static final String EPG_INDEX_OF_EDITING_EVENT = "INDEX_OF_EDITING_EVENT";
    public static final String PREFERENCES_IS_LOCATION_SELECTED = "locationSelected";
    public static final String EVENT_BASED_RECORDING = "EVENT_BASED_RECORDING_TIMER";
    public static final String IS_FORCE_REVEAL_PASSWORD_PROMPT = "IS_FORCE_REVEAL_PASSWORD_PROMPT";

    /* PVR Create Mode */
    public static final String PVR_CREATE_MODE = "PVR_CREATE_MODE";
    /* PVR Create Mode playback filename */
    public static final String PVR_FILENAME = "PVR_FILENAME";
	//add by wxy
	public static final String PVR_CREATE_FROM = "PVR_CREATE_FROM";
	public static final int PVR_CREATE_FROM_EPG = 1;
	//add end

    public static final String PREFERENCES_DVBC_OPERATOR = "DVBC_OPERATOR";
    public static final String SAVE_SETTING_SELECT = "save_setting_select";
    public static final String TUNING_COUNTRY = "tuningCountry";
    public static final String AUTO_TUNING_OPTION_FROM_SUBPAGE = "fromSubpage";

    // float
    public static final float CCKEY_TEXTSIZE = 40.0f;
    public static final float CCKEY_ALPHA = 0.6f;
    // int
    public static final int TV_SCREENSAVER_NOSIGNAL = 80;
    public static final int ROOTACTIVITY_OAD_DOWNLOAD_TIMEOUT = 600;
    public static final int ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT = 601;
    public static final int ROOTACTIVITY_TVPROMINFOREADY_MESSAGE = 700;
    public static final int ROOTACTIVITY_RESUME_MESSAGE = 800;
    public static final int ROOTACTIVITY_CREATE_MESSAGE = 900;
    public static final int ROOTACTIVITY_CANCEL_DIALOG = 8000;
    public static final int ROOTACTIVITY_DISPLAY_EMERGENCY_SYSTEM = 8001;
    public static final int CEC_STATUS_ON = 1;
    public static final int CHANNEL_LOCK_RESULT_CODE = 100;
    public static final int LNBOPTION_PAGETYPE_INVALID = -1;
    public static final int LNBOPTION_PAGETYPE_SATELLITE = 0;
    public static final int LNBOPTION_PAGETYPE_TRANSPONDER = 1;
    public static final int LNBOPTION_PAGETYPE_FREQUENCIES = 2;
    public static final int LNBOPTION_PAGETYPE_MOTOR = 3;
    public static final int LNBOPTION_PAGETYPE_SINGLECABLE = 4;
    public static final int LNBOPTION_EDITOR_ACTION_INVALID = -1;
    public static final int LNBOPTION_EDITOR_ACTION_ADD = 0;
    public static final int LNBOPTION_EDITOR_ACTION_EDIT = 1;
    public static final int LNBOPTION_MOTOR_NONE = 0;
    public static final int LNBOPTION_MOTOR_1_2 = 1;
    public static final int LNBOPTION_MOTOR_1_3 = 2;
    public static final int LNBOPTION_MOTOR_ACTION_INVALID = -1;
    public static final int LNBOPTION_MOTOR_ACTION_POSITION = 0;
    public static final int LNBOPTION_MOTOR_ACTION_LIMIT = 1;
    public static final int LNBOPTION_MOTOR_ACTION_LOCATION = 2;
    public static final int TARGET_REGION_COUNTRY_LEVEL = 0;
    public static final int TARGET_REGION_PRIMARY_LEVEL = 1;
    public static final int TARGET_REGION_SECONDARY_LEVEL = 2;
    public static final int TARGET_REGION_TERTIARY_LEVEL = 3;

    public static final int PQ_ADJUST_GAMMA = 201;
    public static final int PQ_ADJUST_DLC = 202;
    public static final int PQ_SELECT_IHC = 203;
    public static final int PQ_COLOR_SETTING_ICC_R = 204;
    public static final int PQ_COLOR_SETTING_ICC_G = 205;
    public static final int PQ_COLOR_SETTING_ICC_B = 206;
    public static final int PQ_COLOR_SETTING_ICC_C = 207;
    public static final int PQ_COLOR_SETTING_ICC_M = 208;
    public static final int PQ_COLOR_SETTING_ICC_Y = 209;
    public static final int PQ_COLOR_SETTING_ICC_F = 210;
    public static final int PQ_COLOR_SETTING_ICC_GY = 211;
    public static final int PQ_COLOR_SETTING_ICC_BY = 212;
    public static final int PQ_COLOR_SETTING_IBC_R = 213;
    public static final int PQ_COLOR_SETTING_IBC_G = 214;
    public static final int PQ_COLOR_SETTING_IBC_B = 215;
    public static final int PQ_COLOR_SETTING_IBC_C = 216;
    public static final int PQ_COLOR_SETTING_IBC_M = 217;
    public static final int PQ_COLOR_SETTING_IBC_Y = 218;
    public static final int PQ_COLOR_SETTING_IBC_F = 219;
    public static final int PQ_COLOR_SETTING_IBC_GY = 220;
    public static final int PQ_COLOR_SETTING_IBC_BY = 221;
    public static final int PQ_COLOR_SETTING_IHC_R = 222;
    public static final int PQ_COLOR_SETTING_IHC_G = 223;
    public static final int PQ_COLOR_SETTING_IHC_B = 224;
    public static final int PQ_COLOR_SETTING_IHC_C = 225;
    public static final int PQ_COLOR_SETTING_IHC_M = 226;
    public static final int PQ_COLOR_SETTING_IHC_Y = 227;
    public static final int PQ_COLOR_SETTING_IHC_F = 228;
    public static final int PQ_COLOR_SETTING_IHC_GY = 229;
    public static final int PQ_COLOR_SETTING_IHC_BY = 230;
    public static final int PQ_LUMA_SETTING_PRE_YOFFSET = 231;
    public static final int PQ_LUMA_SETTING_PRE_YGAIN = 232;
    public static final int PQ_LUMA_SETTING_COMB_CONTRAST = 233;
    public static final int PQ_LUMA_SETTING_COMB_BRIGHTNESS = 234;
    public static final int PQ_LUMA_SETTING_COMB_SATURATION = 235;
    public static final int PQ_PEAK_SETTING_PEAK_BAND= 236;
    public static final int PQ_PEAK_SETTING_PEAK_PCORING = 237;
    public static final int PQ_PEAK_SETTING_PEAK_PCRING_AD_C = 238;
    public static final int PQ_PEAK_SETTING_PEAK_GAIN = 239;
    public static final int PQ_PEAK_SETTING_PEAK_GAIN_AD_C = 240;
    public static final int PQ_PEAK_SETTING_HLPF = 241;
    public static final int PQ_PEAK_SETTING_SRAM1 = 242;
    public static final int PQ_PEAK_SETTING_SRAM2 = 243;
    public static final int PQ_NR_SETTING_NR_ENABLE = 244;
    public static final int PQ_NR_SETTING_DNR_Y = 245;
    public static final int PQ_NR_SETTING_DNR_C = 246;
    public static final int PQ_NR_SETTING_DNR_Y_COLOR_DEP = 247;
    public static final int PQ_NR_SETTING_SPF_SNR_MR = 248;
    public static final int PQ_NR_SETTING_SPF_SNR= 249;
    public static final int PQ_CTI_SETTING_COMB_83 = 250;
    public static final int PQ_CTI_SETTING_POST_CTI = 251;
    public static final int PQ_CTI_SETTING_POST_CTI_COEF = 252;
    public static final int PQ_CTI_SETTING_PRECTI = 253;
    public static final int PQ_CTI_SETTING_YCDELAY = 254;
    public static final int PQ_SELECT_ICC = 255;
    public static final int PQ_PEAK_SETTING_PEAK= 256;
    public static final int PQ_COLOR_SETTING_ICC_ENABLE = 257;
    public static final int PQ_COLOR_SETTING_IBC_ENABLE = 258;
    public static final int PQ_COLOR_SETTING_IHC_ENABLE = 259;
    public static final int PQ_ADJUST_VIP = 260;
	public static final int PQ_SELECT_IBC = 261;
	public static final int PQ_COLOR_SETTING_ICC_FLESH = 262;
	public static final int PQ_COLOR_SETTING_ICC_REDDISH = 263;
	public static final int PQ_COLOR_SETTING_ICC_LIP = 264;
	public static final int PQ_COLOR_SETTING_ICC_HAIR = 265;
	public static final int PQ_COLOR_SETTING_ICC_MEAT = 266;
	public static final int PQ_COLOR_SETTING_ICC_DARK = 267;
	public static final int PQ_COLOR_SETTING_IBC_FLESH = 268;
    public static final int PQ_COLOR_SETTING_IBC_REDDISH = 269;
    public static final int PQ_COLOR_SETTING_IBC_LIP = 270;
    public static final int PQ_COLOR_SETTING_IBC_HAIR = 271;
    public static final int PQ_COLOR_SETTING_IBC_MEAT = 272;
    public static final int PQ_COLOR_SETTING_IBC_DARK = 273;
	public static final int PQ_COLOR_SETTING_IHC_FLESH = 274;
    public static final int PQ_COLOR_SETTING_IHC_REDDISH = 275;
    public static final int PQ_COLOR_SETTING_IHC_LIP = 276;
    public static final int PQ_COLOR_SETTING_IHC_HAIR = 277;
    public static final int PQ_COLOR_SETTING_IHC_MEAT = 278;
    public static final int PQ_COLOR_SETTING_IHC_DARK = 279;


	

    /*
     * Set extra int ListId = SHOW_FAVORITE_LIST To show only favorite programs
     * @see com.mstar.tv.tvplayer.ui.channel.ChannelListActivity
     */
    public final static int SHOW_FAVORITE_LIST = 1;
    /*
     * Set extra int ListId = SHOW_PROGRAM_LIST To show all programs
     * @see com.mstar.tv.tvplayer.ui.channel.ChannelListActivity
     */
    public final static int SHOW_PROGRAM_LIST = 2;

    /*PVR PVR_CREATE_MODE extra int contant, NONE*/
    public static final int PVR_NONE = 0;
    /*PVR PVR_CREATE_MODE extra int contant, stop playback*/
    public static final int PVR_PLAYBACK_STOP = 1;
    /*PVR PVR_CREATE_MODE extra int contant, playback*/
    public static final int PVR_PLAYBACK_START = 2;
    /*PVR PVR_CREATE_MODE extra int contant, playback from browser*/
    public static final int PVR_PLAYBACK_FROM_BROWSER = 3;
    /*PVR PVR_CREATE_MODE extra int contant, (always)timeshift playback pause*/
    public static final int PVR_PLAYBACK_PAUSE = 4;
    /*PVR PVR_CREATE_MODE extra int contant, always timeshift jump forward*/
    public static final int PVR_PLAYBACK_PREVIOUS = 5;
    /*PVR PVR_CREATE_MODE extra int contant, always timeshift fastbackward*/
    public static final int PVR_PLAYBACK_REWIND = 6;
    /*PVR PVR_CREATE_MODE extra int contant, stop record*/
    public static final int PVR_RECORD_STOP = 10;
    /*PVR PVR_CREATE_MODE extra int contant, schedule record or one touch record*/
    public static final int PVR_RECORD_START = 11;
    public static  int SAVE_CURRENT_PAGE = 0;
    /* OAD download file */
    public static final String OAD_UPGRADE_FILE = "/cache/update_signed.zip";

    // class
    public class SignalProgSyncStatus {

        public static final int NOSYNC = 0x00;

        public static final int STABLE_SUPPORT_MODE = 0x01;

        public static final int STABLE_UN_SUPPORT_MODE = 0x02;

        public static final int UNSTABLE = 0x03;

        public static final int AUTO_ADJUST =0x04;
    };

    public class ScreenSaverMode {

        public static final int DTV_SS_INVALID_SERVICE = 0x00;

        public static final int DTV_SS_NO_CI_MODULE = 0x01;

        public static final int DTV_SS_CI_PLUS_AUTHENTICATION = 0x02;

        public static final int DTV_SS_SCRAMBLED_PROGRAM = 0x03;

        public static final int DTV_SS_CH_BLOCK = 0x04;

        public static final int DTV_SS_PARENTAL_BLOCK = 0x05;

        public static final int DTV_SS_AUDIO_ONLY = 0x06;

        public static final int DTV_SS_DATA_ONLY = 0x07;

        public static final int DTV_SS_COMMON_VIDEO = 0x08;

        public static final int DTV_SS_UNSUPPORTED_FORMAT = 0x09;

        public static final int DTV_SS_INVALID_PMT = 0x0A;

        public static final int DTV_SS_NO_CHANNEL = 0x0B;

        public static final int DTV_SS_CA_NOTIFY = 0x0C;

        public static final int DTV_SS_MAX = 0x0D;
    };

    public class AtscAtvScreenSaverMode {

        public static final int ATV_SS_NORMAL = 0x00;

        public static final int ATV_SS_NO_CHANNEL = 0x01;

        public static final int ATV_SS_MAX = 0x02;
    };

	public class PQ_DATA {
	public static final int  GAMMA_Data = 0;
	public static final int  DLC_Data = 1;		
	public static final int  VIP_Data = 2;		
	
	//pq select
	public static final int  IHC_Data = 3;		
	public static final int  ICC_Data = 4;		
	public static final int  IBC_Data = 5;		

	//color setting/ICC
	public static final int  ICC_ENABLE_Data = 6;
	public static final int  ICC_R_Data = 7; 	
	public static final int  ICC_G_Data = 8; 	
	public static final int  ICC_B_Data = 9; 	
	public static final int  ICC_C_Data = 10; 	
	public static final int  ICC_M_Data = 11; 	
	public static final int  ICC_Y_Data = 12; 	
	public static final int  ICC_F_Data = 13; 	
	public static final int  ICC_GY_Data = 14;	
	public static final int  ICC_BY_Data = 15;	
	public static final int  ICC_Flesh_Data = 16;
	public static final int  ICC_Reddish_Data = 17;
	public static final int  ICC_Lip_Data = 18;
	public static final int  ICC_Hair_Data = 19;
	public static final int  ICC_Meat_Data = 20;
	public static final int  ICC_Dark_Data = 21;

	
	//color setting/IBC
	public static final int  IBC_ENABLE_Data = 22;
	public static final int  IBC_R_Data = 23; 	
	public static final int  IBC_G_Data = 24; 	
	public static final int  IBC_B_Data = 25; 	
	public static final int  IBC_C_Data = 26; 	
	public static final int  IBC_M_Data = 27; 	
	public static final int  IBC_Y_Data = 28; 	
	public static final int  IBC_F_Data = 29; 	
	public static final int  IBC_GY_Data = 30;	
	public static final int  IBC_BY_Data = 31;	
	public static final int  IBC_Flesh_Data = 32;
	public static final int  IBC_Reddish_Data = 33;
	public static final int  IBC_Lip_Data = 34;
	public static final int  IBC_Hair_Data = 35;
	public static final int  IBC_Meat_Data = 36;
	public static final int  IBC_Dark_Data = 37;

	//color setting/IHC
	public static final int  IHC_ENABLE_Data = 38;
	public static final int  IHC_R_Data = 39; 	
	public static final int  IHC_G_Data = 40; 	
	public static final int  IHC_B_Data = 41; 	
	public static final int  IHC_C_Data = 42; 	
	public static final int  IHC_M_Data = 43; 	
	public static final int  IHC_Y_Data = 44; 	
	public static final int  IHC_F_Data = 45; 	
	public static final int  IHC_GY_Data = 46;	
	public static final int  IHC_BY_Data = 47;	
	public static final int  IHC_Flesh_Data = 48;
	public static final int  IHC_Reddish_Data = 49;
	public static final int  IHC_Lip_Data = 50;
	public static final int  IHC_Hair_Data = 51;
	public static final int  IHC_Meat_Data = 52;
	public static final int  IHC_Dark_Data = 53;

	//luma settings 
	public static final int  PRE_YOFFSET_Data = 54;
	public static final int  PRE_YGAIN_Data = 55; 
	public static final int  COMB_CONTRAST_Data = 56;
	public static final int  COMB_BRIGHTNESS_Data = 57;
	public static final int  COMB_SATURATION_Data = 58;

	//peaking settings
	public static final int  PEAKING_Data = 59;		
	public static final int  VIP_PEAKING_BAND_Data = 60;
	public static final int  VIP_PEAKING_PCORING_Data = 61;
	public static final int  VIP_PEAKING_PCORING_AD_C_Data = 62;
	public static final int  VIP_PEAKING_GAIN_Data = 63;
	public static final int  VIP_PEAKING_GAIN_AD_C_Data = 64;
	public static final int  VIP_HLPF_Data = 65;
	public static final int  SRAM1_Data = 66;
	public static final int  SRAM2_Data = 67;

	//nr settings
	public static final int  NR_ENABLE_Data = 68;
	public static final int  DNR_Y_Data = 69;
	public static final int  DNR_C_Data = 70;
	public static final int  DNR_Y_COLOR_DEP_Data = 71;
	public static final int  SPF_SNR_MR_Data = 72;
	public static final int  SPF_SNR_Data = 73;

	//cti
	public static final int  COMB_83_Data = 74;
	public static final int  VIP_POST_CTI_Data = 75;
	public static final int  VIP_POST_CTI_COEF_Data = 76;
	public static final int  PRECTI_Data = 77;
	public static final int  YCDELAY_Data = 78;

	//vip 
	public static final int  VIP_ON_Data = 79;
	public static final int  VIP_OFF_Data = 80;
    };

    public static final int  MAIN_PQ_IP_AFEC_COM_Main = 0;
    public static final int  MAIN_PQ_IP_Comb_COM_Main = 1;
    public static final int  MAIN_PQ_IP_SECAM_COM_Main = 2;
    public static final int  MAIN_PQ_IP_VD_Sampling_no_comm_COM_Main = 3;
    public static final int  MAIN_PQ_IP_ADC_Sampling_COM_Main = 4;
    public static final int  MAIN_PQ_IP_SCinit_COM_Main = 5;
    public static final int  MAIN_PQ_IP_YCdelay_COM_Main = 6;
    public static final int  MAIN_PQ_IP_PreFilter_COM_Main = 7;
    public static final int  MAIN_PQ_IP_PreFilter_Dither_COM_Main = 8;
    public static final int  MAIN_PQ_IP_HSD_Sampling_COM_Main = 9;
    public static final int  MAIN_PQ_IP_HSD_Y_COM_Main = 10;
    public static final int  MAIN_PQ_IP_HSD_C_COM_Main = 11;
    public static final int  MAIN_PQ_IP_444To422_COM_Main = 12;
    public static final int  MAIN_PQ_IP_VSD_COM_Main = 13;
    public static final int  MAIN_PQ_IP_HVSD_Dither_COM_Main = 14;
    public static final int  MAIN_PQ_IP_10to8_Dither_COM_Main = 15;
    public static final int  MAIN_PQ_IP_MemFormat_COM_Main = 16;
    public static final int  MAIN_PQ_IP_PreSNR_COM_Main = 17;
    public static final int  MAIN_PQ_IP_PreSNR_Patch_COM_Main = 18;
    public static final int  MAIN_PQ_IP_DNR_COM_Main = 19;
    public static final int  MAIN_PQ_IP_DNR_Motion_COM_Main = 20;
    public static final int  MAIN_PQ_IP_DNR_Y_COM_Main = 21;
    public static final int  MAIN_PQ_IP_DNR_Y_COLOR_DEP_COM_Main = 22;
    public static final int  MAIN_PQ_IP_SRAM_COLOR_INDEX_COM_Main = 23;
    public static final int  MAIN_PQ_IP_DNR_Y_LUMA_ADAPTIVE_COM_Main = 24;
    public static final int  MAIN_PQ_IP_DNR_POSTTUNE_COM_Main = 25;
    public static final int  MAIN_PQ_IP_DNR_C_COM_Main = 26;
    public static final int  MAIN_PQ_IP_DNR_sticky_solver_COM_Main = 27;
    public static final int  MAIN_PQ_IP_PNR_COM_Main = 28;
    public static final int  MAIN_PQ_IP_PNR_Y_COM_Main = 29;
    public static final int  MAIN_PQ_IP_PNR_C_COM_Main = 30;
    public static final int  MAIN_PQ_IP_PostCCS_COM_Main = 31;
    public static final int  MAIN_PQ_IP_PostCCS_NP_COM_Main = 32;
    public static final int  MAIN_PQ_IP_DHD_COM_Main = 33;
    public static final int  MAIN_PQ_IP_420CUP_COM_Main = 34;
    public static final int  MAIN_PQ_IP_BWS_COM_Main = 35;
    public static final int  MAIN_PQ_IP_MADi_COM_Main = 36;
    public static final int  MAIN_PQ_IP_MADi_Motion_COM_Main = 37;
    public static final int  MAIN_PQ_IP_MADi_ADP3x3_COM_Main = 38;
    public static final int  MAIN_PQ_IP_MADi_MORPHO_COM_Main = 39;
    public static final int  MAIN_PQ_IP_MADi_DFK_COM_Main = 40;
    public static final int  MAIN_PQ_IP_MADi_SST_COM_Main = 41;
    public static final int  MAIN_PQ_IP_MADi_EODiW_COM_Main = 42;
    public static final int  MAIN_PQ_IP_MADi_Force_COM_Main = 43;
    public static final int  MAIN_PQ_IP_EODi_COM_Main = 44;
    public static final int  MAIN_PQ_IP_Film_COM_Main = 45;
    public static final int  MAIN_PQ_IP_Film32_COM_Main = 46;
    public static final int  MAIN_PQ_IP_Film22_COM_Main = 47;
    public static final int  MAIN_PQ_IP_Film_any_COM_Main = 48;
    public static final int  MAIN_PQ_IP_UCNR_COM_Main = 49;
    public static final int  MAIN_PQ_IP_UCDi_COM_Main = 50;
    public static final int  MAIN_PQ_IP_UC_CTL_COM_Main = 51;
    public static final int  MAIN_PQ_IP_DIPF_COM_Main = 52;
	public static final int  MAIN_PQ_IP_VCLPF_COM_Main = 53;
	public static final int  MAIN_PQ_IP_SPF_COM_Main = 54;
	public static final int  MAIN_PQ_IP_Spike_NR_COM_Main = 55;
	public static final int  MAIN_PQ_IP_SPF_DBK_COM_Main = 56;
	public static final int  MAIN_PQ_IP_SPF_DBK_MR_COM_Main = 57;
	public static final int  MAIN_PQ_IP_SPF_DBK_BKN_COM_Main = 58;
	public static final int  MAIN_PQ_IP_SPF_SNR_COM_Main = 59;
	public static final int  MAIN_PQ_IP_SPF_SNR_MR_COM_Main = 60;
	public static final int  MAIN_PQ_IP_SPF_MR_LPF_COM_Main = 61;
	public static final int  MAIN_PQ_IP_SPF_NMR_Y_COM_Main = 62;
	public static final int  MAIN_PQ_IP_SPF_NMR_Y_MR_COM_Main = 63;
	public static final int  MAIN_PQ_IP_DMS_COM_Main = 64;
	public static final int  MAIN_PQ_IP_DMS_H_COM_Main = 65;
	public static final int  MAIN_PQ_IP_VSP_Y_COM_Main = 66;
	public static final int  MAIN_PQ_IP_VSP_C_COM_Main = 67;
	public static final int  MAIN_PQ_IP_VSP_CoRing_COM_Main = 68;
	public static final int  MAIN_PQ_IP_VSP_Dither_COM_Main = 69;
	public static final int  MAIN_PQ_IP_VSP_PreVBound_COM_Main = 70;
	public static final int  MAIN_PQ_IP_422To444_COM_Main = 71;
	public static final int  MAIN_PQ_IP_PreCTI_COM_Main = 72;
	public static final int  MAIN_PQ_IP_HSP_Y_COM_Main = 73;
	public static final int  MAIN_PQ_IP_HSP_C_COM_Main = 74;
	public static final int  MAIN_PQ_IP_HSP_CoRing_COM_Main = 75;
	public static final int  MAIN_PQ_IP_HSP_Dither_COM_Main = 76;
	public static final int  MAIN_PQ_IP_HnonLinear_COM_Main = 77;
	public static final int  MAIN_PQ_IP_SRAM1_COM_Main = 78;
	public static final int  MAIN_PQ_IP_SRAM2_COM_Main = 79;
	public static final int  MAIN_PQ_IP_SRAM3_COM_Main = 80;
	public static final int  MAIN_PQ_IP_SRAM4_COM_Main = 81;
	public static final int  MAIN_PQ_IP_C_SRAM1_COM_Main = 82;
	public static final int  MAIN_PQ_IP_C_SRAM2_COM_Main = 83;
	public static final int  MAIN_PQ_IP_C_SRAM3_COM_Main = 84;
	public static final int  MAIN_PQ_IP_C_SRAM4_COM_Main = 85;
	public static final int  MAIN_PQ_IP_VIP_COM_Main = 86;
	public static final int  MAIN_PQ_IP_VIP_pseudo_COM_Main = 87;
	public static final int  MAIN_PQ_IP_VIP_Post_YC_delay_COM_Main = 88;
	public static final int  MAIN_PQ_IP_VIP_HNMR_Y_COM_Main = 89;
	public static final int  MAIN_PQ_IP_VIP_HNMR_ad_C_COM_Main = 90;
	public static final int  MAIN_PQ_IP_VIP_HNMR_ad_C_gain_COM_Main = 91;
	public static final int  MAIN_PQ_IP_VIP_HNMR_C_win1_COM_Main = 92;
	public static final int  MAIN_PQ_IP_VIP_Pre_Yoffset_COM_Main = 93;
	public static final int  MAIN_PQ_IP_VIP_Pre_Ygain_COM_Main = 94;
	public static final int  MAIN_PQ_IP_VIP_Pre_Ygain_dither_COM_Main = 95;
	public static final int  MAIN_PQ_IP_VIP_HLPF_COM_Main = 96;
	public static final int  MAIN_PQ_IP_VIP_HLPF_dither_COM_Main = 97;
	public static final int  MAIN_PQ_IP_VIP_VNMR_COM_Main = 98;
	public static final int  MAIN_PQ_IP_VIP_VNMR_dither_COM_Main = 99;
	public static final int  MAIN_PQ_IP_VIP_VLPF_coef1_COM_Main = 100;
	public static final int  MAIN_PQ_IP_VIP_VLPF_coef2_COM_Main = 101;
	public static final int  MAIN_PQ_IP_VIP_VLPF_dither_COM_Main = 102;
	public static final int  MAIN_PQ_IP_VIP_LDE_COM_Main = 103;
	public static final int  MAIN_PQ_IP_VIP_LDE_setting_COM_Main = 104;
	public static final int  MAIN_PQ_IP_VIP_Peaking_COM_Main = 105;
	public static final int  MAIN_PQ_IP_VIP_Peaking_band_COM_Main = 106;
	public static final int  MAIN_PQ_IP_VIP_Peaking_Pcoring_COM_Main = 107;
	public static final int  MAIN_PQ_IP_VIP_Peaking_Pcoring_ad_C_COM_Main = 108;
	public static final int  MAIN_PQ_IP_VIP_Peaking_gain_COM_Main = 109;
	public static final int  MAIN_PQ_IP_VIP_Peaking_gain_ad_C_COM_Main = 110;
	public static final int  MAIN_PQ_IP_VIP_Post_SNR_COM_Main = 111;
	public static final int  MAIN_PQ_IP_VIP_Post_CTI_COM_Main = 112;
	public static final int  MAIN_PQ_IP_VIP_Post_CTI_coef_COM_Main = 113;
	public static final int  MAIN_PQ_IP_VIP_Post_CTI_gray_COM_Main = 114;
	public static final int  MAIN_PQ_IP_VIP_FCC_full_range_COM_Main = 115;
	public static final int  MAIN_PQ_IP_VIP_FCC_bdry_dist_COM_Main = 116;
	public static final int  MAIN_PQ_IP_VIP_FCC_T1_COM_Main = 117;
	public static final int  MAIN_PQ_IP_VIP_FCC_T2_COM_Main = 118;
	public static final int  MAIN_PQ_IP_VIP_FCC_T3_COM_Main = 119;
	public static final int  MAIN_PQ_IP_VIP_FCC_T4_COM_Main = 120;
	public static final int  MAIN_PQ_IP_VIP_FCC_T5_COM_Main = 121;
	public static final int  MAIN_PQ_IP_VIP_FCC_T6_COM_Main = 122;
	public static final int  MAIN_PQ_IP_VIP_FCC_T7_COM_Main = 123;
	public static final int  MAIN_PQ_IP_VIP_FCC_T8_COM_Main = 124;
	public static final int  MAIN_PQ_IP_VIP_FCC_T9_COM_Main = 125;
	public static final int  MAIN_PQ_IP_VIP_IHC_COM_Main = 126;
	public static final int  MAIN_PQ_IP_VIP_IHC_Ymode_COM_Main = 127;
	public static final int  MAIN_PQ_IP_VIP_IHC_dither_COM_Main = 128;
	public static final int  MAIN_PQ_IP_VIP_IHC_CRD_SRAM_COM_Main = 129;
	public static final int  MAIN_PQ_IP_VIP_IHC_SETTING_COM_Main = 130;
	public static final int  MAIN_PQ_IP_VIP_ICC_COM_Main = 131;
	public static final int  MAIN_PQ_IP_VIP_ICC_Ymode_COM_Main = 132;
	public static final int  MAIN_PQ_IP_VIP_ICC_dither_COM_Main = 133;
	public static final int  MAIN_PQ_IP_VIP_ICC_CRD_SRAM_COM_Main = 134;
	public static final int  MAIN_PQ_IP_VIP_ICC_SETTING_COM_Main = 135;
	public static final int  MAIN_PQ_IP_VIP_Ymode_Yvalue_ALL_COM_Main = 136;
	public static final int  MAIN_PQ_IP_VIP_Ymode_Yvalue_SETTING_COM_Main = 137;
	public static final int  MAIN_PQ_IP_VIP_IBC_COM_Main = 138;
	public static final int  MAIN_PQ_IP_VIP_IBC_dither_COM_Main = 139;
	public static final int  MAIN_PQ_IP_VIP_IBC_SETTING_COM_Main = 140;
	public static final int  MAIN_PQ_IP_VIP_DLC_COM_Main = 141;
	public static final int  MAIN_PQ_IP_VIP_DLC_ad_C_COM_Main = 142;
	public static final int  MAIN_PQ_IP_VIP_DLC_dither_COM_Main = 143;
	public static final int  MAIN_PQ_IP_VIP_DLC_His_range_COM_Main = 144;
	public static final int  MAIN_PQ_IP_VIP_DLC_His_rangeH_COM_Main = 145;
	public static final int  MAIN_PQ_IP_VIP_DLC_His_rangeV_COM_Main = 146;
	public static final int  MAIN_PQ_IP_VIP_DLC_PC_COM_Main = 147;
	public static final int  MAIN_PQ_IP_VIP_BWLE_COM_Main = 148;
	public static final int  MAIN_PQ_IP_VIP_BLE_COM_Main = 149;
	public static final int  MAIN_PQ_IP_VIP_WLE_COM_Main = 150;
	public static final int  MAIN_PQ_IP_VIP_BWLE_dither_COM_Main = 151;
	public static final int  MAIN_PQ_IP_VIP_UVC_COM_Main = 152;
	public static final int  MAIN_PQ_IP_VIP_Post_Yoffset_COM_Main = 153;
	public static final int  MAIN_PQ_IP_VIP_Post_Ygain_COM_Main = 154;
	public static final int  MAIN_PQ_IP_VIP_Hcoring_Y_COM_Main = 155;
	public static final int  MAIN_PQ_IP_VIP_Hcoring_C_COM_Main = 156;
	public static final int  MAIN_PQ_IP_VIP_Hcoring_dither_COM_Main = 157;
	public static final int  MAIN_PQ_IP_VIP_YCbCr_Clip_COM_Main = 158;
	public static final int  MAIN_PQ_IP_SwDriver_COM_Main = 159;
	public static final int  MAIN_PQ_IP_3x3_COM_Main = 160;
	public static final int  MAIN_PQ_IP_RGB_Offset_COM_Main = 161;
	public static final int  MAIN_PQ_IP_RGB_Clip_COM_Main = 162;
	public static final int  MAIN_PQ_IP_HBC_COM_Main = 163;
	public static final int  MAIN_PQ_IP_Pre_CON_BRI_COM_Main = 164;
	public static final int  MAIN_PQ_IP_Blue_Stretch_COM_Main = 165;
	public static final int  MAIN_PQ_IP_Blue_Stretch_dither_COM_Main = 166;
	public static final int  MAIN_PQ_IP_Gamma_COM_Main = 167;
	public static final int  MAIN_PQ_IP_Gamma_dither_COM_Main = 168;
	public static final int  MAIN_PQ_IP_Post_CON_BRI_COM_Main = 169;
	public static final int  MAIN_PQ_IP_Clone_main_no_comm_COM_Main = 170;
	public static final int  MAIN_PQ_IP_Clone_sub_no_comm_COM_Main = 171;
	public static final int  MAIN_PQ_IP_MWE_diff_no_comm_COM_Main = 172;
	public static final int  MAIN_PQ_IP_3DSettingForLBL_no_comm_COM_Main = 173;
	public static final int  MAIN_PQ_IP_SettingFor2LineMode_no_comm_COM_Main = 174;
	public static final int  MAIN_PQ_IP_HDR_Settings_COM_Main = 175;
    public static final int  MAIN_PQ_IP_NUM = 176;

    public static final int  COLOR_PQ_IP_VIP_FCC_T1_Main_Color = 0;
    public static final int  COLOR_PQ_IP_VIP_FCC_T2_Main_Color = 1;
    public static final int  COLOR_PQ_IP_VIP_FCC_T3_Main_Color = 2;
    public static final int  COLOR_PQ_IP_VIP_FCC_T4_Main_Color = 3;
    public static final int  COLOR_PQ_IP_VIP_FCC_T5_Main_Color = 4;
    public static final int  COLOR_PQ_IP_VIP_FCC_T6_Main_Color = 5;
    public static final int  COLOR_PQ_IP_VIP_FCC_T7_Main_Color = 6;
    public static final int  COLOR_PQ_IP_VIP_FCC_T8_Main_Color = 7;
    public static final int  COLOR_PQ_IP_VIP_FCC_T9_Main_Color = 8;
    public static final int  COLOR_PQ_IP_VIP_IHC_Main_Color = 9;
    public static final int  COLOR_PQ_IP_VIP_IHC_Ymode_Main_Color = 10;
    public static final int  COLOR_PQ_IP_VIP_IHC_SETTING_Main_Color = 11;
    public static final int  COLOR_PQ_IP_VIP_ICC_Main_Color = 12;
    public static final int  COLOR_PQ_IP_VIP_ICC_Ymode_Main_Color = 13;
    public static final int  COLOR_PQ_IP_VIP_ICC_SETTING_Main_Color = 14;
    public static final int  COLOR_PQ_IP_VIP_Ymode_Yvalue_ALL_Main_Color = 15;
    public static final int  COLOR_PQ_IP_VIP_Ymode_Yvalue_SETTING_Main_Color = 16;
    public static final int  COLOR_PQ_IP_VIP_IBC_Main_Color = 17;
    public static final int  COLOR_PQ_IP_VIP_IBC_SETTING_Main_Color = 18;
    public static final int  COLOR_PQ_IP_VIP_Post_Cgain_Main_Color = 19;
    public static final int  COLOR_PQ_IP_VIP_Peaking_band_Main_Color = 20;
    public static final int  COLOR_PQ_IP_VIP_Peaking_Pcoring_Main_Color = 21;
    public static final int  COLOR_PQ_IP_NUM = 22;

	public static final int PQ_TABLE_TYPE_BIG = 0;
    public static final int PQ_TABLE_TYPE_LITTLE = 1;
}
