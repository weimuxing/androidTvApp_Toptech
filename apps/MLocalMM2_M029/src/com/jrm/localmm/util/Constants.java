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
package com.jrm.localmm.util;

import com.jrm.localmm.R;
import android.graphics.Point;

public class Constants {
    /** file list focus position */
    public static final int POSITION_0 = 0;
    /** file list focus position */
    public static final int POSITION_9 = 9;
    /** list mode one page most display nine data **/
    public static final int LIST_MODE_DISPLAY_NUM = 9;

    public static final int GRID_MODE_DISPLAY_NUM = 9;

    public static final int GRID_MODE_ONE_ROW_DISPLAY_NUM = 5;

    /**
     * file browsing interface display the current file types including
     * pictures, video, music
     */
    public static final int OPTION_STATE_ALL = 0x01;
    /**
     * file browsing interface display the current file types only pictures and
     * folder
     */
    public static final int OPTION_STATE_PICTURE = 0x02;
    /**
     * file browsing interface display the current file types only music and
     * folder
     */
    public static final int OPTION_STATE_SONG = 0x03;
    /**
     * file browsing interface display the current file types only video and
     * folder
     */
    public static final int OPTION_STATE_VIDEO = 0x04;
    /** flip over instruction: through the remote page up */
    public static final int KEYCODE_PAGE_UP = 0x5;
    /** flip over instruction: through the remote downward turn the page */
    public static final int KEYCODE_PAGE_DOWN = 0x6;
    /** flip over instruction: through the Touch events page up */
    public static final int TOUCH_PAGE_UP = 0x7;
    /** flip over instruction: through the Touch events downward turn the page */
    public static final int TOUCH_PAGE_DOWN = 0x8;
    /** file browsing main interface */
    public static final int BROWSER_TOP_DATA = 0x01;
    /** file browsing interface data from local usb disk equipment */
    public static final int BROWSER_LOCAL_DATA = 0x02;
    /** file browsing interface data from network samba equipment */
    public static final int BROWSER_SAMBA_DATA = 0x03;
    /** file browsing interface data from network dlna equipment */
    public static final int BROWSER_DLNA_DATA = 0x04;
    /** refresh local data */
    public static final int UPDATE_TOP_DATA = 0x05;
    /** refresh local data */
    public static final int UPDATE_LOCAL_DATA = 0x06;
    /** refresh dlna data */
    public static final int UPDATE_DLNA_DATA = 0x07;
    /** refresh samba data */
    public static final int UPDATE_ALL_SAMBA_DATA = 0x08;
    /** refresh samba data */
    public static final int UPDATE_SAMBA_DATA = 0x58;
    public static final int SAMBA_SCAN_COMPLETED = 0x59;
    /** switching media type refresh UI */
    public static final int UPDATE_MEDIA_TYPE = 0x09;
    /** anomaly hints */
    public static final int UPDATE_EXCEPTION_INFO = 0xa;
    /** refresh loading progress information */
    public static final int UPDATE_PROGRESS_INFO = 0xb;
    /** disk loading/remove **/
    public static final int UPDATE_DISK_DEVICE = 0xc;
    /** network cannot use */
    public static final int NETWORK_EXCEPTION = 0xd;
    /** network status changes for not use */
    public static final int NETWORK_UNCONNECTED = 0xf;
    /** without the support of media formats */
    public static final int UNSUPPORT_FORMAT = 0x10;
    /** display loading hints */
    public static final int PROGRESS_TEXT_SHOW = 0x11;
    /** hidden loading hints */
    public static final int PROGRESS_TEXT_HIDE = 0x12;
    /** the mouse moves listview focus update */
    public static final int UPDATE_LISTVIEW_FOCUS = 0x13;
    /** the mouse operate page up */
    public static final int MOUSE_PAGE_UP = 0x14;
    /** the mouse operate downward turn the page */
    public static final int MOUSE_PAGE_DOWN = 0x15;
    
    //zb20150613 add
    public static final int REFRESH_DATA=0x16;
    //end
    
    /** file categories: general file */
    public static final int FILE_TYPE_FILE = 0x1;
    /** file categories: picture */
    public static final int FILE_TYPE_PICTURE = 0x2;
    /** file categories: music */
    public static final int FILE_TYPE_SONG = 0x3;
    /** file categories: video */
    public static final int FILE_TYPE_VIDEO = 0x4;
    /** file categories: dir */
    public static final int FILE_TYPE_DIR = 0x5;
    /** file categories: return */
    public static final int FILE_TYPE_RETURN = 0x6;
    /** file categories: mstarplaylist */
    public static final int FILE_TYPE_MPLAYLIST = 0x7;
    /** file categories: install */
    public static final int FILE_TYPE_INSTALL = 0x8;
    /** to return to dlna root page */
    public static final String RETURN_TOP = "top";
    /** returns to the local root page */
    public static final String RETURN_LOCAL = "local";
    /** to return to dlna root page */
    public static final String RETURN_DLNA = "dlna";
    /** returns to the samba root page */
    public static final String RETURN_SAMBA = "samba";
    /** currently browsing the content page */
    public static final String BUNDLE_PAGE = "current_page";
    /** currently browsing files list of total page */
    public static final String BUNDLE_TPAGE = "total_page";
    /** currently browsing files list focus position */
    public static final String BUNDLE_INDEX = "current_index";
    public static final String BUNDLE_INDEX_KEY = "com.jrm.index";
    public static final String BUNDLE_BASEDATA_KEY = "player_basedata";
    public static final String BUNDLE_FILETYPE_KEY = "player_filetype";
    public static final String BUNDLE_FILEURL_KEY = "player_fileurl";
    public static final String BUNDLE_LIST_KEY = "com.jrm.arraylist";
    public static final String SOURCE_FROM = "sourceFrom";
    public static final String ADAPTER_POSITION = "adapter.position";
    public static final String DB_NAME = "videoplay.db";
    public static final String VIDEO_PLAY_LIST_TABLE_NAME = "videoplaylist";
    // resources from the samba
    public static final int SOURCE_FROM_SAMBA = 0x011;
    /** resources from local disk */
    public static final int SOURCE_FROM_LOCAL = 0x12;
    /** media scanning abnormal state */
    public static final int FAILED_BASE = 0x00;
    /** Ping far end equipment failure: */
    public static final int FAILED_TIME_OUT = FAILED_BASE + 1;
    /** samba landing when password mistake */
    public static final int FAILED_WRONG_PASSWD = FAILED_TIME_OUT + 1;
    /** landing samba failure */
    public static final int FAILED_LOGIN_FAILED = FAILED_WRONG_PASSWD + 1;
    /** landing samba failure */
    public static final int MOUNT_FAILED = FAILED_LOGIN_FAILED + 1;
    /** landing samba other abnormal */
    public static final int FAILED_LOGIN_OTHER_FAILED = MOUNT_FAILED + 1;
    // AB function of the switch
    public static boolean abFlag = false;
    // A function of the switch
    public static boolean aFlag = false;
    public static boolean bSupportDivx = false;
    public static boolean bSupportPhotoScale = true;

    public static final int THREE_D_MODE_2D_TO_3D = 2;
    public static final int THREE_D_MODE_AUTO = 1;
    public static final int THREE_D_MODE_CHECK_BOARD = 7;
    public static final int THREE_D_MODE_FRAME_SEQUENTIAL = 8;
    public static final int THREE_D_MODE_LEFT_RIGHT = 0;
    public static final int THREE_D_MODE_LINE_ALTERNATIVE = 5;
    public static final int THREE_D_MODE_NONE = 0;
    public static final int THREE_D_MODE_PIXEL_ALTERNATIVE = 6;
    public static final int THREE_D_MODE_RIGHT_LEFT = 1;
    public static final int THREE_D_MODE_SIDE_BY_SIDE = 3;
    public static final int THREE_D_MODE_TOP_BOTTOM = 4;
    // Play subtitles
    // public static int[] subTitleSettingOptText = {
    // R.string.subtitle_type_value, R.string.subtitle_out_path_value,
    // R.string.subtitle_in_no_value, R.string.subtitle_language_value,
    // R.string.subtitle_font_value, R.string.subtitle_font_size_value,
    // R.string.subtitle_color_value, R.string.subtitle_frame_value,
    // R.string.subtitle_area_value, R.string.subtitle_adjust_value
    // };
    // Caption title
    public static int[] subTitleSettingName = { R.string.subtitle_type,
            R.string.subtitle_out_path, R.string.subtitle_in_no,
            R.string.subtitle_language, R.string.subtitle_font,
            R.string.subtitle_font_size, R.string.subtitle_color,
            R.string.subtitle_frame, R.string.subtitle_area,
            R.string.subtitle_adjust };
    // Choose played
    public static final int CHOOSE_TIME = 18;
    public static final int SEEK_TIME = 19;
    public static final int OPEN_DUAL_SEEK_TIME = 20;
    public static final int SET_LEFT_VIDEO_SIZE = 21;
    public static final int HANDLE_MESSAGE_PLAYER_EXIT = 22;
    public static final int HANDLE_MESSAGE_CHECK_AB = 23;
    public static final int HANDLE_MESSAGE_SKIP_BREAKPOINT = 24;
    public static final int OSD_3D_UI = 25;
    public static final int ShowController = 40;
    public static final int DualAudioOn = 41;
    public static final int SHOW_SUBTITLE_DIALOG = 124;
    public static final int SHOW_SUBTITLE_LIST_DIALOG = 125;
    public static final int CHECK_IS_SUPPORTED = 126;

    // Multi Thumbnail
    public static final int SetThumbnailBorderViewOnFocus = 201;
    public static final int ShowThumbnailBorderView = SetThumbnailBorderViewOnFocus + 1;
    public static final int SeekWithHideThumbnailBorderView = SetThumbnailBorderViewOnFocus + 2;
    public static final int seekThumbnailPos = SetThumbnailBorderViewOnFocus + 3;
    public static final int OpenThumbnailPlayer = SetThumbnailBorderViewOnFocus + 4;
    public static final int HideThumbnailBorderView = SetThumbnailBorderViewOnFocus + 5;
    public static final int EnargeThumbnail = SetThumbnailBorderViewOnFocus + 6;
    public static final int FowrardThumbnail = SetThumbnailBorderViewOnFocus + 7;
    public static final int PrepareMediaPlayer = SetThumbnailBorderViewOnFocus + 8;
    public static final int RefreshThumbnailSeekViewPosition= SetThumbnailBorderViewOnFocus + 9;
    public static final int REQUEST_RENDER_THUMBNAIL = SetThumbnailBorderViewOnFocus + 10;
    public static final int INIT_THUMBNAIL_PLAYER = SetThumbnailBorderViewOnFocus + 11;


    // VideoPlayerActivity
    public static final int RefreshCurrentPositionStatusUI = 300;

    // Hidden video article control
    public static final int HIDE_PLAYER_CONTROL = 13;
    public static final String MPO = "mpo";
    public static final String GIF = "gif";
    public static boolean isExit = true;
    public static final int START_MEDIA_PLAYER = 100;
    public static Point sceenResolution= new Point(0,0);
    public static boolean bSupportDualDecode = true;
    public static boolean bPhotoSeamlessEnable = false;
    public static boolean bReleasingPlayer = false;
    public static final boolean bStreamLessEnable = false;
    public static final boolean bUseAndroidStandardMediaPlayerTrackAPI = false;

    //listview or gridview mode value,ps:do not use enum for efficiency
    public static final int LISTVIEW_MODE = 0 ;
    public static final int GRIDVIEW_MODE = 1 ;

    //first enter the localMM's view Mode
    public static final int LISTVIEW_MODE_FIRST = 0 ;
    public static final int GRIDVIEW_MODE_FIRST = 1 ;


    // used on GridAdapter
    public static final int UNFINISHED = -1;
    public static final int FINISHED = 1;
    public static final int GRID_POSITION_IS_VIDEO = 0;
    public static final int GRID_POSITION_IS_MUSIC = 1;
    public static final int GRID_POSITION_IS_PICTURE = 2;
    public static final int GRID_POSITION_IS_OTHERS = -1;

    // used on localDataBrowser to TAG if any task has been canceled
    public static final int GRID_TASK_NOT_CANCELED = 0;
    public static final int GRID_TASK_CANCELED = 1;

    public static boolean ExitMM=true;
    public static String UsbName=null;
}
