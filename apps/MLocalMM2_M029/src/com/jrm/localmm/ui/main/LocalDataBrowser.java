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

package com.jrm.localmm.ui.main;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Gravity;
import android.widget.Toast;

import com.jrm.localmm.R;
import com.jrm.localmm.business.data.BaseData;
import com.jrm.localmm.ui.MediaContainerApplication;
import com.jrm.localmm.ui.music.MusicPlayerActivity;
import com.jrm.localmm.ui.photo.ImagePlayerActivity;
import com.jrm.localmm.ui.video.VideoPlayerActivity;
import com.jrm.localmm.ui.video.NetVideoPlayActivity;
import com.jrm.localmm.util.Constants;
import com.jrm.localmm.business.adapter.GridAdapter;


public class LocalDataBrowser {

    private static final String TAG = "LocalDataBrowser";

    private Activity activity;

    private Handler handler;

    private LocalDataManager localDataManager;

    protected List<BaseData> data;

    private int activityType;

    // Focus the current position
    protected int focusPosition;

    // Click confirm focus position media types
    private int mediaType;

    public static int hasCancelVideoTaskStateInLocalData=Constants.GRID_TASK_NOT_CANCELED;
    private RefreshUIListener refreshUIListener = new RefreshUIListener() {

        @Override
        public void onFinish(List<BaseData> list, int currentPage,
                int totalPage, int position) {
            focusPosition = position;
            if (data != null) {
                // data clear.
                data.clear();
            }
            data.addAll(list);

            // send refresh UI event.
            Message msg = handler.obtainMessage();
            msg.what = Constants.UPDATE_LOCAL_DATA;

            Bundle bundle = new Bundle();
            bundle.putInt(Constants.BUNDLE_PAGE, currentPage);
            bundle.putInt(Constants.BUNDLE_TPAGE, totalPage);
            bundle.putInt(Constants.BUNDLE_INDEX, position);
            msg.setData(bundle);
            handler.sendMessage(msg);

        }

        @Override
        public void onOneItemAdd(List<BaseData> list, int currentPage,
                int totalPage, int position) {
        }

        @Override
        public void onScanCompleted() {
        }

        @Override
        public void onFailed(int code) {
            Message message = handler.obtainMessage();
            // Refresh disk equipment
            if (code == Constants.UPDATE_DISK_DEVICE) {
                message.what = Constants.UPDATE_PROGRESS_INFO;
                message.arg2 = Constants.PROGRESS_TEXT_SHOW;
                message.arg1 = R.string.loading_usb_device;
            }

            handler.sendMessage(message);
        }
    };

    public LocalDataBrowser(Activity activity, Handler handler,
            List<BaseData> data) {
        this.activity = activity;
        this.handler = handler;
        this.data = data;
    }

    /**
     * into the folder for data.
     *
     * @param type
     *            media types
     */
    protected void browser(int index, int type) {
        this.activityType = type;
        if (localDataManager == null) {
            localDataManager = new LocalDataManager(activity, refreshUIListener);
        }
        localDataManager.browser(index, type);
    }

    /**
     * complete turn the page operation
     */
    protected void refresh(int operateType) {

        // finish turn the page, 0 for flip over mark
        if (operateType == Constants.KEYCODE_PAGE_UP
                || operateType == Constants.TOUCH_PAGE_UP) {
            // page up
            localDataManager.getCurrentPage(-1, 0);

        } else if (operateType == Constants.KEYCODE_PAGE_DOWN
                || operateType == Constants.TOUCH_PAGE_DOWN) {
            // page down
            localDataManager.getCurrentPage(1, 0);

            // filter files
        } else if (operateType == Constants.OPTION_STATE_ALL
                || operateType == Constants.OPTION_STATE_SONG
                || operateType == Constants.OPTION_STATE_VIDEO
                || operateType == Constants.OPTION_STATE_PICTURE) {
            this.activityType = operateType;
            Log.d(TAG, "LocalDataBrowser activityType:" + activityType);

            localDataManager.getCurrentPage(0, operateType);

        }

    }
	protected void refresh(String name){
		localDataManager.findFocus(name);
	}

	protected void refreshDir(String currentDirectory){
		localDataManager.startScan(currentDirectory);
	}
	
    /**
     * receive to disk loading or remove radio again when the interface shown on
     * disk data.
     *
     * @param path
     *            disk mount the absolute path.
     */
    protected void updateUSBDevice(final String path) {
        localDataManager.showUSBDevice(path);
    }
    public int getLocalDataBrowserState(){
        if (null == localDataManager)
            return -1;
        int tmpState = localDataManager.getLocalDataBrowserState();
        return tmpState;
    }
    protected void switchViewMode(){
          localDataManager.switchViewMode=1;
          localDataManager.onFinish();
    }
    /**
     * According to the parameter start designated player.
     *
     * @param type
     *            media types.
     * @param position
     *            focus position.
     */
    protected void startPlayer(final int type, final int position) {
        Log.i(TAG, "*****startPlayer****" + position);
        if (Constants.FILE_TYPE_MPLAYLIST == type) {
            BaseData bd = localDataManager.getCommonFile(position);
            Log.v("LocalDataBrowser", "play playlist file:"+bd.getPath());
            Intent intent = null;
            intent = new Intent(activity, NetVideoPlayActivity.class);
            intent.putExtra(Constants.BUNDLE_FILETYPE_KEY, type);
            intent.putExtra(Constants.BUNDLE_BASEDATA_KEY, bd);
            activity.startActivity(intent);
            return;
        }
        boolean hasMedia = false;
        // check whether has the specified type of media
        hasMedia = MediaContainerApplication.getInstance().hasMedia(type);
        if (hasMedia) {
            // The current click position
            int index = 0;
            if (activityType != type
                    && activityType == Constants.OPTION_STATE_ALL) {
                index = localDataManager.getMediaFile(-type, position);
            } else {
                index = localDataManager.getMediaFile(type, position);
            }
            Log.d(TAG, "startPlayer, index : " + index);

            // intent for start activity
            Intent intent = null;
            // Start picture player
            if (Constants.FILE_TYPE_PICTURE == type) {
                if (!Constants.bReleasingPlayer) {
                    intent = new Intent(activity, ImagePlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                } else {
                    String sMessage = activity.getString(R.string.busy_tip);
                    Toast toast = Toast.makeText(activity, sMessage, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                // Start music player
            } else if (Constants.FILE_TYPE_SONG == type) {
                intent = new Intent(activity, MusicPlayerActivity.class);



                // Start video player
            } else if (Constants.FILE_TYPE_VIDEO == type) {
                if (Constants.GRIDVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
                    new Thread(new Runnable() {
                       @Override
                        public void run() {
                            for (int i=0;i<=Constants.GRID_MODE_DISPLAY_NUM;i++) {
                                if (FileBrowserActivity.gridAdapter.imagePosition2Task[i]!=null
                                    &&FileBrowserActivity.gridAdapter.isFinish[i]!=Constants.FINISHED) {
                                    FileBrowserActivity.gridAdapter.imagePosition2Task[i].cancel(true);
                                    hasCancelVideoTaskStateInLocalData=Constants.GRID_TASK_CANCELED;
                                }
                            }
                        }
                    }).start();
                }
                //modify because of mantis 0893708
                if (!Constants.bReleasingPlayer) {
                    intent = new Intent(activity, VideoPlayerActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                } else {
                    String sMessage = activity.getString(R.string.busy_tip);
                    Toast toast = Toast.makeText(activity, sMessage, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }

            }
            // the current broadcast media index
            intent.putExtra(Constants.BUNDLE_INDEX_KEY, index);

            // boot media player
            if(Constants.FILE_TYPE_PICTURE == type){
                if(Constants.isExit){
                    activity.startActivityForResult(intent,0);
                }else{
                    focusPosition = position;
                    handler.sendEmptyMessageDelayed(Constants.START_MEDIA_PLAYER, 500);
                }
                return;
            }
            activity.startActivityForResult(intent,0);
        } else {
            Log.d(TAG, "Does not has specified type : " + type + " of media.");
        }
    }

    public void startPlayer(){
        startPlayer(mediaType, focusPosition);
    }

    /**
     * processing key events.
     *
     * @ param keyCode
     *                key value.
     * @ param position
     *                response button ListView focus position.
     * @ return key event handling complete returning true,otherwise returns false.
     */
    protected boolean processKeyDown(int keyCode, int position) {
		//huzhou modify
        if (keyCode == KeyEvent.KEYCODE_ENTER||keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
            // ocus in the first "return", click on the previous page
            if (position == Constants.POSITION_0&&keyCode == KeyEvent.KEYCODE_ENTER) {
                String description = data.get(0).getDescription();
                // return TOP page
                if (Constants.RETURN_TOP.equals(description)) {
                    handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);

                    // previous directory level directory
                } else if (Constants.RETURN_LOCAL.equals(description)) {
                    Message message = handler.obtainMessage();
                    message.what = Constants.UPDATE_PROGRESS_INFO;
                    message.arg1 = R.string.loading_local_resource;
                    message.arg2 = Constants.PROGRESS_TEXT_SHOW;
                    handler.sendMessage(message);

                    browser(0, activityType);
                }

            } else if(position>0) {
                BaseData di = null;
                mediaType = 0;

                if (position < data.size()) {
                    di = data.get(position);
                    mediaType = di.getType();
                } else {
                	Log.e(TAG, "position out of the size!!, positon : " + position);
                }
                Log.d(TAG, "processKeyDown, positon : " + position+" , file type: "+mediaType);
                // media files on click confirm key activate a player
                if(Constants.FILE_TYPE_INSTALL == mediaType){
        			Intent intent = new Intent();
        		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		    intent.setDataAndType(Uri.fromFile(new File(di.getPath())), 
        		    		"application/vnd.android.package-archive"); 
        		    intent.setAction(android.content.Intent.ACTION_VIEW); 
        		    activity.startActivity(intent);
                    
                }else if (Constants.FILE_TYPE_PICTURE == mediaType
                        || Constants.FILE_TYPE_SONG == mediaType
                        || Constants.FILE_TYPE_VIDEO == mediaType
                        || Constants.FILE_TYPE_MPLAYLIST == mediaType) {
                    // start player
                    startPlayer(mediaType, position);

                    // catalog click confirm button to enter catalog level
                    // directory level directory
                } else if (Constants.FILE_TYPE_DIR == mediaType) {
                    Message msg = handler.obtainMessage();
                    msg.what = Constants.UPDATE_PROGRESS_INFO;
                    msg.arg1 = R.string.loading_local_resource;
                    msg.arg2 = Constants.PROGRESS_TEXT_SHOW;
                    handler.sendMessage(msg);
                    browser(position, activityType);

                    // Not media file and folder is not when
                } else {
                    Message message = handler.obtainMessage();
                    message.what = Constants.UPDATE_EXCEPTION_INFO;
                    message.arg1 = Constants.UNSUPPORT_FORMAT;
                    handler.sendMessage(message);
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (Constants.LISTVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
                if (focusPosition == Constants.POSITION_0) {
                    // page up
                    refresh(Constants.KEYCODE_PAGE_UP);
                } else {
                    focusPosition = position;
                }
                return true;

            } else if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                if (focusPosition<Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM && focusPosition>=0) {
                     // page up
                    refresh(Constants.KEYCODE_PAGE_UP);
                } else {
                    focusPosition = position;
                }
                return true;

            }


        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (Constants.LISTVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
                if (focusPosition == Constants.POSITION_9) {
                    // page down
                    refresh(Constants.KEYCODE_PAGE_DOWN);
                } else {
                    focusPosition = position;
                }
                return true;
            } else if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                int lastRowStartPosition =Constants.GRID_MODE_DISPLAY_NUM-Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM+1;
                if (focusPosition<=Constants.GRID_MODE_DISPLAY_NUM && focusPosition>=lastRowStartPosition) {
                    // page down
                    refresh(Constants.KEYCODE_PAGE_DOWN);
                } else {
                    focusPosition = position;
                }
                return true;
            }
        }
        return false;
    }
}
