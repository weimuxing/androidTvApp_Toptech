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

package com.mstar.android.tv.inputservice.data;

import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.util.ArrayMap;

import java.util.*;

import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tvapi.common.vo.VideoWindowType;

public class WindowControl {
    private static final String TAG = "WindowControl";

    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    public static final int WINDOW_TYPE_MAIN = 0;

    public static final int WINDOW_TYPE_SUB = 1;

    public static final int WINDOW_TYPE_NONE = -1;

    private static final int EMPTY_WINDOW = 0;

    private static final int SINGLE_WINDOW_FULLSCREEN = 1;

    private static final int SINGLE_WINDOW_IN_PICTURE = 2;

    private static final int DUAL_WINDOW = 3;

    private static final int MAX_WINDOW_NUMBER = 2;

    private static final int FULLSCREEN_WIDTH = 1920;

    private static final int FULLSCREEN_HEIGHT = 1080;

    private static final int DATA_NOT_FOUND = -1;

    private static final Object sLock = new Object();

    private static final ArrayList<WindowData> mWindowList = new ArrayList<>();

    private static int sFlag = EMPTY_WINDOW;

    public static boolean acquireWindow(String inputId) {
        synchronized (sLock) {
            if (mWindowList.size() >= MAX_WINDOW_NUMBER) {
                Log.e(TAG, "acquireWindow: reach max number of supported windows.");
                debugLog();
                return false;
            }
            int index = queryIndexbyId(inputId);
            if (index != DATA_NOT_FOUND) {
                Log.e(TAG, "acquireWindow: " + inputId + " is already in the List[" + index + "]\n");
                return false;
            }
            Log.w(TAG, "AquireWindow From " + inputId);
            mWindowList.add(new WindowData.Builder().setInputId(inputId).build());
            return true;
        }
    }

    public static int getWindowType(String inputId) {
        synchronized (sLock) {
            int index = queryIndexbyId(inputId);
            if (index != DATA_NOT_FOUND) {
                return mWindowList.get(index).getWindowType();
            } else {
                return WINDOW_TYPE_NONE;
            }
        }
    }

    public static WindowData getWindowData(String inputId) {
        synchronized (sLock) {
            int index = queryIndexbyId(inputId);
            if (index != DATA_NOT_FOUND) {
                return mWindowList.get(index);
            } else {
                return null;
            }
        }
    }

    public static boolean setWindowData(WindowData windowData) {
        synchronized (sLock) {
            Log.d(TAG, "setWindowData: " + windowData);
            int index = queryIndexbyId(windowData.getInputId());
            if (index != DATA_NOT_FOUND) {
                //Update List windowData value.
                mWindowList.set(index,windowData);
                return update();
            } else {
                return false;
            }
        }
    }

    private static int queryIndexbyId(String Id) {
        for (WindowData wd : mWindowList) {
            if (wd.getInputId().compareTo(Id) == 0) {
                return mWindowList.indexOf(wd);
            }
        }
        return DATA_NOT_FOUND;
    }

    private static void updateSource() {
        for (WindowData wd : mWindowList) {
            if (wd.getWindowType() == WINDOW_TYPE_SUB &&
                wd.getSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                wd.setSource(TvCommonManager.INPUT_SOURCE_DTV2);
            } else if (wd.getWindowType() == WINDOW_TYPE_MAIN &&
                wd.getSource() == TvCommonManager.INPUT_SOURCE_DTV2) {
                wd.setSource(TvCommonManager.INPUT_SOURCE_DTV);
            }
        }
    }

    private static boolean isValidWindowData(int index) {
        if (mWindowList.get(index).getLeft() < 0 ||
            mWindowList.get(index).getTop() < 0 ||
            mWindowList.get(index).getWidth() <= 0 ||
            mWindowList.get(index).getHeight() <= 0 ||
            mWindowList.get(index).getWindowType() < TvCommonManager.INPUT_SOURCE_VGA ||
            mWindowList.get(index).getWindowType() >= TvCommonManager.INPUT_SOURCE_NUM) {
            return false;
        }
        return true;
    }
    private static void setDataToFullScreen(int index) {
        mWindowList.get(index).setLeft(0);
        mWindowList.get(index).setTop(0);
        mWindowList.get(index).setWidth(FULLSCREEN_WIDTH);
        mWindowList.get(index).setHeight(FULLSCREEN_HEIGHT);
    }

    private VideoWindowType creatVideoWindowType(int x, int y, int w, int h){
        VideoWindowType v = new VideoWindowType();
        v.x = x;
        v.y = y;
        v.width = w;
        v.height = h;
        return v;
    }

    private static void debugLog() {
        for (WindowData wd : mWindowList) {
                Log.w(TAG, wd.toString());
        }
    }
    private static VideoWindowType creatVideoWindowType(int windowType){
        int index = queryIndexbyType(windowType);
        if (index == DATA_NOT_FOUND) {
            return null;
        }
        VideoWindowType v = new VideoWindowType();
        v.x = mWindowList.get(index).getLeft();
        v.y = mWindowList.get(index).getTop();
        v.width = mWindowList.get(index).getWidth();
        v.height = mWindowList.get(index).getHeight();
        return v;
    }
    private static VideoWindowType creatDummyVideoWindow(){
        VideoWindowType v = new VideoWindowType();
        v.x = 0;
        v.y = 0;
        v.width = 1;
        v.height = 1;
        return v;
    }

    private static int querySourcebyType(int windowType) {
        int index = queryIndexbyType(windowType);
        if (index == DATA_NOT_FOUND) {
            return DATA_NOT_FOUND;
        }
        return mWindowList.get(index).getSource();
    }

    private static int queryIndexbyType(int windowType) {
        for (WindowData wd : mWindowList) {
            if (wd.getWindowType() == windowType) {
                return mWindowList.indexOf(wd);
            }
        }
        return DATA_NOT_FOUND;
    }

    private static boolean update() {
        synchronized (sLock) {
            //1) Determine Single/Dual Window
            //2) Determine main and sub by window size
            //TODO: Query system to get FullScreen width and height.
            boolean res = false;
            int AreaFullScreen = FULLSCREEN_WIDTH * FULLSCREEN_HEIGHT;
            if (mWindowList.size() == 2) {
                //DUAL_WINDOW case
                sFlag = DUAL_WINDOW;
                //Determine which input ID set to main or sub
                //based on the size of view & pos of x.
                Collections.sort(mWindowList);
                int type = 0;
                for (WindowData wd : mWindowList) {
                    wd.setWindowType(type++);
                }
                res = true;
            } else if (mWindowList.size() == 1) {
                //Determine if the current window is
                //1) SINGLE_WINDOW_FULLSCREEN
                //2) SINGLE_WINDOW_IN_PICTURE

                //Force invalid WindowData to full screen setting.
                if (!isValidWindowData(0)) {
                    setDataToFullScreen(0);
                }
                int AreaZero = mWindowList.get(0).getWidth() * mWindowList.get(0).getHeight();
                if (AreaZero == AreaFullScreen) {
                    sFlag = SINGLE_WINDOW_FULLSCREEN;
                    mWindowList.get(0).setWindowType(WINDOW_TYPE_MAIN);
                    res = true;
                } else if (AreaZero < AreaFullScreen) {
                    sFlag = SINGLE_WINDOW_IN_PICTURE;
                    mWindowList.get(0).setWindowType(WINDOW_TYPE_SUB);
                    res = true;
                }
            } else if (mWindowList.size() == 0) {
                sFlag = EMPTY_WINDOW;
                res = true;
            }
            //Handle TVOS requirement: If sub source is DTV, reset it to DTV2
            if (sFlag == DUAL_WINDOW || sFlag == SINGLE_WINDOW_IN_PICTURE) {
                updateSource();
            }
            Log.w(TAG, "After update() sFlag: " + sFlag);
            return res;
        }
    }

    public static boolean run(){
        synchronized (sLock) {
            //Print WindowData before run.
            debugLog();
            //If any illegal value is in WindowData,
            //return run comment without doing anything.
            for (int i = 0; i < mWindowList.size(); i++) {
                if(!isValidWindowData(i)) {
                    return true;
                }
            }
            boolean res = false;
            int pipResult = TvPipPopManager.E_PIP_NOT_SUPPORT;
            //If enablePipTv is called, we create a dummy video window(0,0,0,0)
            //and let tv_input to handle main/sub position and size adjustment.
            switch (sFlag) {
                case DUAL_WINDOW:
                    pipResult = TvPipPopManager.getInstance().enablePipTv(
                            querySourcebyType(WINDOW_TYPE_MAIN),
                            querySourcebyType(WINDOW_TYPE_SUB),
                            creatDummyVideoWindow());
                    break;
                case SINGLE_WINDOW_IN_PICTURE:
                    pipResult = TvPipPopManager.getInstance().enablePipTv(
                            TvCommonManager.INPUT_SOURCE_STORAGE,
                            querySourcebyType(WINDOW_TYPE_SUB),
                            creatDummyVideoWindow());
                    break;
                case SINGLE_WINDOW_FULLSCREEN:
                    TvCommonManager.getInstance().setInputSource(querySourcebyType(WINDOW_TYPE_MAIN));
                    res = true;
                    break;
                case EMPTY_WINDOW:
                    TvCommonManager.getInstance().setInputSource(TvCommonManager.INPUT_SOURCE_STORAGE);
                    res = true;
                    break;
                default:
                    break;
            }
            if (pipResult == TvPipPopManager.E_PIP_SUCCESS ||
                pipResult == TvPipPopManager.E_PIP_PIP_MODE_OPENED) {
                res = true;
            }
            //After supernova input source is set,
            //Trigger TvInputService to pass surface to tv_input and
            //create pipe with HWComposer
            if (res == true) {
                for (WindowData wd : mWindowList) {
                    if( wd.getEventListener() != null) {
                        wd.getEventListener().onEvent();
                    }
                }
            }
            return res;
        }
    }

    public static void releaseWindow(String inputId) {
        synchronized (sLock) {
            Log.w(TAG, "Release Input Service: " + inputId);
            int index = queryIndexbyId(inputId);
            if (index == DATA_NOT_FOUND) {
                Log.e(TAG, "No " + inputId + " existed in list.");
                return;
            }
            if (sFlag == DUAL_WINDOW) {
                if (mWindowList.get(index).getWindowType() == WINDOW_TYPE_SUB) {
                    //1) if sub is released,
                    //   disable Pip and set source to main in update()/run().
                    TvPipPopManager.getInstance().disablePIP(TvPipPopManager.E_MAIN_WINDOW);
                } else {
                    //2) If main is released,
                    //   keep pip enable state and sub's pos & size.
                    //   let update()/run() to set main to storage
                }
            } else if (sFlag == SINGLE_WINDOW_IN_PICTURE) {
                //sub is released and no window left,
                //disable Pip and set main source (storage) in update()/run().
                TvPipPopManager.getInstance().disablePIP(TvPipPopManager.E_MAIN_WINDOW);
            }
            mWindowList.remove(index);
            update();
            run();
            //TODO:Update tv_input
        }
    }
}
