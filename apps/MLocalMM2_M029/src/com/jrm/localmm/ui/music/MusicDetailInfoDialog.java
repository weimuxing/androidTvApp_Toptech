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
package com.jrm.localmm.ui.music;

import android.R.color;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.jrm.localmm.R;
import com.jrm.localmm.business.data.BaseData;
import com.jrm.localmm.util.Tools;
import android.util.DisplayMetrics;
import com.jrm.localmm.util.Constants;
/**
 *
 * Custom audio file detailed information Dialog.
 */
public class MusicDetailInfoDialog extends Dialog {
    private static final String TAG = "MusicDetailInfoDialog";
    private Activity activity;
    // Dialog box in of each component
    private TextView fileName;
    private TextView filePath;
    private TextView artistName;
    private TextView fileFormat;
    private TextView duration;
    private BaseData musicFile;
	private boolean flag=true;
    private int FLAG_DISMISS=1;
    private int num=15;

    /**
     *
     * Structure function
     */
    public MusicDetailInfoDialog(Activity activity, int theme, BaseData data) {
        super(activity, theme);
        this.activity = activity;
        this.musicFile = data;
    }
	//add by fangcuihong,20160720
	   @Override
    public void show() {
    	// TODO Auto-generated method stub
    	super.show();
    	mThread.start();
    }
    
    @Override
    public void dismiss() {
    	// TODO Auto-generated method stub
    	super.dismiss();
    	flag=false;
    }
    
    private Thread mThread=new Thread(){
    	@Override
    	public void run() {
    		super.run();
    		while(flag){
    			try{
    				Thread.sleep(1000);
    				num-=1;
    				if(num<=0){
    					flag=false;
    					Message msg=mHandler.obtainMessage();
    					msg.what=FLAG_DISMISS;
    					mHandler.sendMessage(msg);
    				}
    			}catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
    		}
    	};
    };
    
    private Handler mHandler=new Handler(){
    	@Override
    	public void handleMessage(android.os.Message msg) {
    		super.handleMessage(msg);
    		if(msg.what==FLAG_DISMISS){
    			dismiss();
    		}
    	};
    };
	//end
	
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
	//add by fangcuihong,20160720
		if(keyCode==KeyEvent.KEYCODE_INFO||keyCode==KeyEvent.KEYCODE_ENTER ||keyCode==KeyEvent.KEYCODE_DPAD_LEFT
				||keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
			flag=false;
			this.cancel();
		}else{
			num=15;
		}
		//end
        return super.onKeyUp(keyCode, event);
    }

    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //   return true;
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_info);
        // Settings dialog related attributes, such as: the background color,
        // the width and height, the location of display, etc
        Window w = getWindow();
        w.setTitle(null);
        Display display = w.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        Log.d(TAG, "x : " + point.x + " y : " + point.y);
        int width = (int) (point.x * 0.4);
        int height = (int) (point.y * 0.5) ;
        WindowManager.LayoutParams wl = w.getAttributes();
        if (activity != null) {
            DisplayMetrics metrics = activity.getResources()
                    .getDisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.i(TAG, "metrics.density:" + metrics.density);
            if (metrics.density == 1.5) {
                wl.screenBrightness = 1.0f;
                height = (int) (point.y * 0.6);
            }
        }
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        w.setAttributes(wl);
        // w.setBackgroundDrawableResource(color.transparent);
        // initial control parts
        findViews();
        // display music information
        showMusicInfo();
    }

    /*
     *
     * Initialization Dialog of components
     */
    private void findViews() {
        fileName = (TextView) findViewById(R.id.file_name);
        artistName = (TextView) findViewById(R.id.artist_name);
        fileFormat = (TextView) findViewById(R.id.file_format);
        duration = (TextView) findViewById(R.id.duration);
        filePath = (TextView) findViewById(R.id.file_path);
    }

    /*
     *
     * Access to current are broadcast video detailed information
     */
    private void showMusicInfo() {
        if (musicFile != null) {
            String path = musicFile.getPath();
            // The name of the song set
            if (musicFile.getName() != null) {
                fileName.setText(musicFile.getName());
            } else {
                fileName.setText(path.substring(path.lastIndexOf("/") + 1,
                        path.length()));
            }
            // Artist Settings
            if (musicFile.getArtist() != null) {
                artistName.setText(musicFile.getArtist());
            } else {
                artistName.setText(activity.getResources().getString(
                        R.string.unknown));
            }
            // song format Settings
            fileFormat.setText(path.substring(path.lastIndexOf(".") + 1,
                    path.length()));
            // total when long Settings
            if (musicFile.getDuration2() != null) {
                duration.setText(musicFile.getDuration2());
            } else {
                duration.setText(Tools
                        .formatDuration(MusicPlayerActivity.countTime));
            }
            // Song path Settings
            //20150312 
            String text=musicFile.getPath();
            if(text.contains("/storage/emulated/0"))
        	{
        		text=text.replace("/storage/emulated/0", "/Internal Storage");
        	}
        	else if(text.contains("/mnt/usb"))
        	{
        		String[] tmp=text.split("/");
        		tmp[3]=Constants.UsbName;
        		text="";
        		for(int i=3;i<tmp.length;i++)
        		{
        			text+="/";
        			text+=tmp[i];
        		}
        	}
            filePath.setText(text);
			//end
        }
    }
}
