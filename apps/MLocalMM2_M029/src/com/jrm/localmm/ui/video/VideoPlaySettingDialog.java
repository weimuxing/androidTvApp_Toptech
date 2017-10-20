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
package com.jrm.localmm.ui.video;

import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;
import android.os.Message;
import android.os.SystemProperties;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.jrm.localmm.R;
import com.jrm.localmm.business.adapter.VideoPlaySettingListAdapter;
import com.jrm.localmm.business.video.BreakPointRecord;
import com.jrm.localmm.ui.common.GoldenLeftEyeActivity;
import com.jrm.localmm.util.Constants;
import com.jrm.localmm.util.Tools;
import com.mstar.android.media.SubtitleTrackInfo;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;
import com.mstar.android.tvapi.common.vo.EnumPictureMode;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideo3DTo2D;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.media.MMediaPlayer;
/**
 * @author
 * @since 1.0
 * @date 2012-2-16
 */
public class VideoPlaySettingDialog extends Dialog {
    private final static String TAG = "VideoPlaySettingDialog";
    private final static int mAutoDetect3DFromatTimes = 3;
    private final static int SHOW_TOAST = 0;
    private VideoPlayerActivity context;
    private ListView playSettingListView;
    private VideoPlaySettingListAdapter adapter;
    // private String videoPath;
    private PlaySettingAudioTrackDialog playSettingAudioTrackDialog;
    private ThreeDSettingDialog threeDSettingDialog;
    private VideoRotateSettingDialog mRotateDegreesSettingDialog;
    //skye 20160810 add for onida
    private VideoPictureSettingDialog mPictureDegreesSettingDialog;
    private VideoMusicSettingDialog mVideoMusicSettingDialog;
    
    private int viewId = 1;
    private long mLastTimeMillis = 0;
    private boolean mIsSTB = false;
    private boolean dectectSucess = false;
    private SubtitleManager mSubtitleManager ;
    private MMediaPlayer mMMediaPlayer ;
    private String[] PictureMode;
	private String[] VideoMode;
	private String[] ColorTem;
	private String[] NoiseReduuction;
	private enum MChangFlag{
		LEFT,RIGHT,REFRESH
	}
	private int pictureItemId =0;
	private int videoItId = 0;
	private int colortemItemId =0;
	private int noisereduuctionItemId =0;
    public static boolean videosettingflag = true;
    // for subtitle store
    private final int SUBTITLE_CLOSE = 0;
    private final int SUBTITLE_INNER = 1;
    private final int SUBTITLE_EXTERNAL = 2;
    private int subtitleItemId = SUBTITLE_INNER;
    private boolean EnableBEE = false;

    public VideoPlaySettingDialog(VideoPlayerActivity context) {
        super(context);
        this.context = context;
    }

    public VideoPlaySettingDialog(VideoPlayerActivity context, int theme,
            String videoPath, MMediaPlayer mMMediaPlayer) {
        super(context, theme);
        this.context = context;
        // this.videoPath = videoPath;
        this.mMMediaPlayer = mMMediaPlayer ;
        mSubtitleManager = SubtitleManager.getInstance() ;
        viewId = context.getVideoPlayHolder().getViewId();
        adapter = new VideoPlaySettingListAdapter(context, VideoPlayerViewHolder.playSettingName,
                Tools.initPlaySettingOpt(context, viewId),VideoPlaySettingDialog.this);
        //Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
        //for subtitle store
        subtitleItemId = Settings.System.getInt(context.getContentResolver(), "subtitle_item", SUBTITLE_INNER);
        initSubtitle(subtitleItemId);
        
        if(context.getBreakPointFlag()) {
            Tools.setPlaySettingOpt(Tools.ITEM_BREAKPOINT_PLAY_SETTING, context.getString(R.string.play_setting_0_value_1), viewId);
        }else{
            Tools.setPlaySettingOpt(Tools.ITEM_BREAKPOINT_PLAY_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
        }
        EnableBEE = SystemProperties.getBoolean("ro.onida.enablebee", false);
        PictureMode = context.getResources().getStringArray(R.array.picture_setting_opt);
        pictureItemId = TvPictureManager.getInstance().getPictureModeIdx().ordinal();
		
        VideoMode = context.getResources().getStringArray(R.array.video_setting_opt);
        videoItId = TvAudioManager.getInstance().getAudioSoundMode();
        
        ColorTem = context.getResources().getStringArray(R.array.video_color_temperature);
        colortemItemId=TvPictureManager.getInstance().getColorTempratureIdx();
        
        NoiseReduuction = context.getResources().getStringArray(R.array.noise_reduuction);
        noisereduuctionItemId =  TvPictureManager.getInstance().getNoiseReduction();
       // Log.d(TAG, "viewId="+viewId+"   noisereduuctionItemId = "+noisereduuctionItemId+"    colortemItemId="+colortemItemId+"  pictureItemId="+pictureItemId);
        
        if(pictureItemId>=PictureMode.length)
        	pictureItemId = PictureMode.length -1;
        if(videoItId >= VideoMode.length)
        	videoItId = VideoMode.length - 1;
        if (colortemItemId>=ColorTem.length) {
        	colortemItemId=ColorTem.length-1;
		}
        if (noisereduuctionItemId>=NoiseReduuction.length) {
        	noisereduuctionItemId=NoiseReduuction.length-1;
		}
        //Log.d(TAG, "noisereduuctionItemId 123 = "+noisereduuctionItemId+"NoiseReduuction.length="+NoiseReduuction.length);
        Tools.setPlaySettingOpt(Tools.ITEM_PICTURE_MODE_SETTING, PictureMode[pictureItemId], viewId);
       // Tools.setPlaySettingOpt(Tools.ITEM_VOIDE_MODE_SETTING, VideoMode[videoItId], viewId);
        Tools.setPlaySettingOpt(Tools.ITEM_COLOR_TEMPERATURE, ColorTem[colortemItemId], viewId);
        Tools.setPlaySettingOpt(Tools.ITEM_IMAGE_NOISEREDUCTION, NoiseReduuction[noisereduuctionItemId], viewId);
       // Log.d(TAG, "201608  "+PictureMode[pictureItemId]+"  "+ColorTem[colortemItemId]+"   "+NoiseReduuction[noisereduuctionItemId]);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_TOAST:
                    showToastTip((String)msg.obj);
                    Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING, context.getString(R.string.play_setting_0_value_2),viewId);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "****onCreate*******");
        setContentView(R.layout.video_play_setting_dialog);
        // Instantiation new window
        Window w = getWindow();
        // Get the default display data
        Display display = w.getWindowManager().getDefaultDisplay();
        playSettingListView = (ListView) this.findViewById(R.id.videoPlaySettingListView);
        playSettingListView.setDivider(null);
        mIsSTB = Tools.isMstarSTB();

        setListeners();
        w.setTitle(null);
        int width = (int) (display.getWidth() * 0.56);
        int height = (int) (display.getHeight() * 0.60);
        w.setLayout(width, height);
        w.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams wl = w.getAttributes();
        w.setAttributes(wl);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "****onStart*******");
        if (Tools.isThumbnailModeOn()) {
            Tools.setPlaySettingOpt(Tools.ITEM_THUMBNAIL_SETTING,context.getString(R.string.play_setting_0_value_1), viewId);
        } else {
            Tools.setPlaySettingOpt(Tools.ITEM_THUMBNAIL_SETTING,context.getString(R.string.play_setting_0_value_2), viewId);
        }
        if (Tools.isRotateModeOn()) {
            Tools.setPlaySettingOpt(Tools.ITEM_ROTATE_SETTING,context.getString(R.string.play_setting_0_value_1), viewId);
        } else {
            Tools.setPlaySettingOpt(Tools.ITEM_ROTATE_SETTING,context.getString(R.string.play_setting_0_value_2), viewId);
        }
//        refresh3DSwitchStatus();
    }

    @Override
    public void dismiss() {
        Log.d(TAG, "****dismiss*******");
        if (threeDSettingDialog != null) {
            threeDSettingDialog.dismiss();
        }
        if (playSettingAudioTrackDialog != null) {
            playSettingAudioTrackDialog.dismiss();
        }
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        Log.d(TAG, "****show*******");
        playSettingListView.setAdapter(adapter);
    }


    /* These code is Extraneous, so comment out it.
     * When user change 3D switch(3D ON or OFF) in VideoPlaySettingDialog,
     * lmm don't necessary to reflesh3DState.
     *
    private boolean mReflesh3DStateFlag = false;
    private Runnable mReflesh3DStateRun = new  Runnable (){
        public void run() {
            context.displayFormat = Tools.getCurrent3dFormat();
            if (context.displayFormat == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                Tools.setPlaySettingOpt(0, context.getString(R.string.play_setting_0_value_2), viewId);
            }
            mReflesh3DStateFlag = true;
        }
        };

    private void reflesh3DState() {
        mReflesh3DStateFlag = false;
        new Thread(mReflesh3DStateRun).start();
        int count = 100;
        while(count-- > 0 && !mReflesh3DStateFlag) {
            android.os.SystemClock.sleep(1);
        }
    }
    */

    private void  refresh3DSwitchStatus() {
        if (!Tools.unSupportTVApi()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        context.displayFormat = Tools.getCurrent3dFormat();
                        if (context.displayFormat == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                            Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
                        }  else {
                            String opt = Tools.getPlaySettingOpt(Tools.ITEM_3D_SETTING, viewId);
                            if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
                                Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING, context.getString(R.string.play_setting_0_value_1), viewId);
                            }
                        }
                    }
                }).start();
        }
    }

    public void handleLeftClick(int pos){
//        if (pos == 0) {
//            change3DState();
//        } else 
    	if (pos == Tools.ITEM_SUBTITLE_SETTING) {
            changeSubtitleStateRight();
        } else if (pos == Tools.ITEM_BREAKPOINT_PLAY_SETTING) {
            changeBreakPointFlag();
        }  else if (pos == Tools.ITEM_THUMBNAIL_SETTING) {
            changeThumbnailMode();
        } else if (pos == Tools.ITEM_ROTATE_SETTING) {
            changeRotateMode();
        } else if (pos == Tools.ITEM_GoldenLeftEyeMode) {
            changdeGoldenLeftEyeMode();
        }else if (pos == Tools.ITEM_PICTURE_MODE_SETTING) {
        	changePictureMode(MChangFlag.LEFT);
		}else if (pos == Tools.ITEM_COLOR_TEMPERATURE) {
			changeColorTem(MChangFlag.LEFT);
		}else if (pos == Tools.ITEM_IMAGE_NOISEREDUCTION) {
			changeNoiseReduction(MChangFlag.LEFT);
		}
        adapter.notifyDataSetChanged();
    }

    private void changdeGoldenLeftEyeMode() {
        String opt = Tools.getPlaySettingOpt(Tools.ITEM_GoldenLeftEyeMode, viewId);
        if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            if(context.getVideoPlayHolder().getDualVideoMode()){
                Toast.makeText(context, context.getString(R.string.dual_mode_not_support_golden_left_eye), 0).show();
                return;
            }
            if(Tools.isMstarSTB()){
                Toast.makeText(context, context.getString(R.string.stb_not_support_golden_left_eye), 0).show();
                return;
            }
            Tools.setPlaySettingOpt(Tools.ITEM_GoldenLeftEyeMode, context.getString(R.string.play_setting_0_value_1), viewId);
            //open golden left eye
            Intent intent = new Intent(context, GoldenLeftEyeActivity.class);
            intent.putExtra("viewId", viewId);
            context.startActivity(intent);
            this.dismiss();
        } else if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_GoldenLeftEyeMode, context.getString(R.string.play_setting_0_value_2), viewId);
            //close golden left eye
            Intent intent = new Intent("com.jrm.localmm.ui.common.GoldenLeftEyeActivity.exit");
            context.sendBroadcast(intent);
        }
    }

    public void handleMidClick(int position) {
        Tools.getPlaySettingOpt(Tools.ITEM_3D_SETTING, viewId);
        String opt2 = Tools.getPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, viewId);
        String opt6 = Tools.getPlaySettingOpt(Tools.ITEM_ROTATE_SETTING, viewId);
//        if (position == 0 && context.getString(R.string.play_setting_0_value_1).equals(opt)) {
//            dismiss();
//            threeDSettingDialog = new ThreeDSettingDialog(context,VideoPlaySettingDialog.this);
//            threeDSettingDialog.show();
//        } else 
        	if (position == Tools.ITEM_SUBTITLE_SETTING && !context.getString(R.string.play_setting_0_value_2).equals(opt2)) {
            dismiss();
            Message msg = new Message();
            Bundle mBundle = new Bundle();
            mBundle.putString("index", opt2);
            msg.setData(mBundle);
            msg.what = Constants.SHOW_SUBTITLE_DIALOG;
            context.getVideoHandler().sendMessage(msg);
        } else 
//        	if (position == 0 && context.getString(R.string.video_3D_3dto2d).equals(opt)) {
//            dismiss();
//            threeDSettingDialog = new ThreeDSettingDialog(context,VideoPlaySettingDialog.this);
//            threeDSettingDialog.show();
//        }  else 
        	if (position == Tools.ITEM_ROTATE_SETTING && context.getString(R.string.play_setting_0_value_1).equals(opt6)) {
            dismiss();
            mRotateDegreesSettingDialog = new VideoRotateSettingDialog(context, VideoPlaySettingDialog.this);
            mRotateDegreesSettingDialog.show();
        }
    }

    public void handleRightClick(int pos) {
//        if (pos == 0){
//            change3DState();
//        } else 
        	if (pos == Tools.ITEM_SUBTITLE_SETTING) {
            changeSubtitleStateRight();
        } else if (pos == Tools.ITEM_BREAKPOINT_PLAY_SETTING) {
            changeBreakPointFlag();
        }  else if (pos == Tools.ITEM_THUMBNAIL_SETTING) {
            changeThumbnailMode();
        } else if (pos == Tools.ITEM_ROTATE_SETTING) {
            changeRotateMode();
        } else if (pos == Tools.ITEM_GoldenLeftEyeMode) {
            changdeGoldenLeftEyeMode();
        }else if (pos == Tools.ITEM_PICTURE_MODE_SETTING) {
        	changePictureMode(MChangFlag.RIGHT);
		}else if (pos == Tools.ITEM_COLOR_TEMPERATURE) {
			changeColorTem(MChangFlag.RIGHT);
		}else if (pos == Tools.ITEM_IMAGE_NOISEREDUCTION) {
			changeNoiseReduction(MChangFlag.RIGHT);
		}
        adapter.notifyDataSetChanged();
    }

    private void setListeners() {
        playSettingListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String opt6 = Tools.getPlaySettingOpt(Tools.ITEM_GoldenLeftEyeMode, viewId);
                Log.d(TAG, "setListeners()  position="+position);
                if (position == Tools.ITEM_BREAKPOINT_PLAY_SETTING){
                    changeBreakPointFlag();
                    adapter.notifyDataSetChanged();
                } else if (position == Tools.ITEM_TRACK_SETTING) {
                    dismiss();
                    playSettingAudioTrackDialog = new PlaySettingAudioTrackDialog(
                            context, R.style.dialog, VideoPlaySettingDialog.this);
                    playSettingAudioTrackDialog.show();
                } /*else if (position == Tools.ITEM_THUMBNAIL_SETTING) {
                    changeThumbnailMode();
                    adapter.notifyDataSetChanged();
                }  else if (position == Tools.ITEM_ROTATE_SETTING && context.getString(R.string.play_setting_0_value_1).equals(opt6)) {
                    dismiss();
                    mRotateDegreesSettingDialog = new VideoRotateSettingDialog(context, VideoPlaySettingDialog.this);
                    mRotateDegreesSettingDialog.show();
                }else if (position == Tools.ITEM_GoldenLeftEyeMode){
                    changdeGoldenLeftEyeMode();
                    adapter.notifyDataSetChanged();
                }*/else if(position == Tools.ITEM_PICTURE_MODE_SETTING){
                	dismiss();
                	mPictureDegreesSettingDialog = new VideoPictureSettingDialog(context, VideoPlaySettingDialog.this);
                	mPictureDegreesSettingDialog.show();
                } else if(position == Tools.ITEM_VOIDE_MODE_SETTING){
                	dismiss();
                	//mVideoDegreesSettingDialog = new VideoVideoSettingDialog(context, VideoPlaySettingDialog.this);
                	//mVideoDegreesSettingDialog.show();
                	mVideoMusicSettingDialog = new VideoMusicSettingDialog(context,VideoPlaySettingDialog.this);
                	mVideoMusicSettingDialog.show();
                }
            }
        });
        playSettingListView.setOnKeyListener(onkeyListenter);
    }

    private View.OnKeyListener onkeyListenter = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_UP: {
                int position = playSettingListView.getSelectedItemPosition();
                switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    Log.e(TAG, "****KEYCODE_DPAD_LEFT*******position:" + position);
                    switch (position) {
//                    case 0:
//                        change3DState();
//                        break;
                    case Tools.ITEM_SUBTITLE_SETTING:
                        changeSubtitleStateLeft();
                        break;
                    case Tools.ITEM_BREAKPOINT_PLAY_SETTING:
                        changeBreakPointFlag();
                        break;
                    case Tools.ITEM_THUMBNAIL_SETTING:
                        changeThumbnailMode();
                        break;
                    case Tools.ITEM_ROTATE_SETTING:
                        changeRotateMode();
                        break;
                    case Tools.ITEM_GoldenLeftEyeMode:
                        changdeGoldenLeftEyeMode();
                        break;
                    case Tools.ITEM_PICTURE_MODE_SETTING:
                    	changePictureMode(MChangFlag.LEFT);
                    	break;
                    case Tools.ITEM_VOIDE_MODE_SETTING:
                    	//changeVideoMode(MChangFlag.LEFT);
                    	break;
                    case Tools.ITEM_COLOR_TEMPERATURE:
                    	changeColorTem(MChangFlag.LEFT);
                    	break;
                    case Tools.ITEM_IMAGE_NOISEREDUCTION:
                    	changeNoiseReduction(MChangFlag.LEFT);
                    	break;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    Log.e(TAG, "****KEYCODE_DPAD_RIGHT*******position:" + position);
                    switch (position) {
//                    case 0:
//                        change3DState();
//                        break;
                    case Tools.ITEM_SUBTITLE_SETTING:
                        changeSubtitleStateRight();
                        break;
                    case Tools.ITEM_BREAKPOINT_PLAY_SETTING:
                        changeBreakPointFlag();
                        break;
                    case Tools.ITEM_THUMBNAIL_SETTING:
                        changeThumbnailMode();
                        break;
                    case Tools.ITEM_ROTATE_SETTING:
                        changeRotateMode();
                        break;
                    case Tools.ITEM_GoldenLeftEyeMode:
                        changdeGoldenLeftEyeMode();
                        break;
                    case Tools.ITEM_PICTURE_MODE_SETTING:
                    	changePictureMode(MChangFlag.RIGHT);
                    	break;
                    case Tools.ITEM_VOIDE_MODE_SETTING:
                    	//changeVideoMode(MChangFlag.LEFT);
                    	break;
                    case Tools.ITEM_COLOR_TEMPERATURE:
                    	changeColorTem(MChangFlag.RIGHT);
                    	break;
                    case Tools.ITEM_IMAGE_NOISEREDUCTION:
                    	changeNoiseReduction(MChangFlag.RIGHT);
                    	break;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                 case KeyEvent.KEYCODE_DPAD_CENTER:
                 case KeyEvent.KEYCODE_ENTER:
                    handleMidClick(position);
                    break;
                }
            }
            }
            return false;
        }
    };

    private void change3DState() {
        if (Tools.unSupportTVApi()) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis-mLastTimeMillis < 2000) {
            mLastTimeMillis = currentTimeMillis;
            showToastTip(context.getResources().getString(R.string.video_3D_switch_info));
            return;
        }
        mLastTimeMillis = currentTimeMillis;
        final TvS3DManager s3dSkin = TvS3DManager.getInstance();
        if (s3dSkin != null) {
            String opt = Tools.getPlaySettingOpt(Tools.ITEM_3D_SETTING, viewId);
            if (mIsSTB) {
                TvPipPopManager mTvPipPopManager = TvPipPopManager.getInstance();
                if (mTvPipPopManager.isPipModeEnabled()) {
                    showToastTip(context.getResources().getString(R.string.can_not_open_3D_pip));
                    return;
                }
                if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
                    Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING,context.getString(R.string.video_3D_3dto2d), viewId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            EnumThreeDVideo3DTo2D twoDformat = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_SIDE_BY_SIDE;
                            Log.i(TAG, "set3DTo2D twoDformat:" + twoDformat);
                            s3dSkin.set3DTo2D(twoDformat);
                        }
                    }).start();
                } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
                    if (!context.getVideoPlayHolder().getDualVideoMode()) {
                        if (Tools.is3DTVPlugedIn()) {
                            Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING,context.getString(R.string.play_setting_0_value_1),viewId);
                        } else {
                            Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING,context.getString(R.string.video_3D_3dto2d), viewId);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (!Tools.is3DTVPlugedIn()) {
                                    if (s3dSkin != null) {
                                        EnumThreeDVideo3DTo2D twoDformat = EnumThreeDVideo3DTo2D.E_ThreeD_Video_3DTO2D_SIDE_BY_SIDE;
                                        Log.i(TAG, "set3DTo2D twoDformat:" + twoDformat);
                                        s3dSkin.set3DTo2D(twoDformat);
                                    }
                                } else {
                                    context.displayFormat = Tools.getCurrent3dFormat();
                                    if (context.displayFormat == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                                        context.displayFormat = EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_SIDE_BY_SIDE;
                                    }
                                    Log.i(TAG, "setDisplayFormat displayFormat:" + context.displayFormat);
                                    s3dSkin.setDisplayFormat(context.displayFormat);
                                }
                            }
                        }).start();
                    } else {
                        showToastTip(context.getResources().getString(R.string.can_not_open_3D));
                    }
                } else if (context.getString(R.string.video_3D_3dto2d).equals(opt)) {
                    Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
                   new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "setDisplayFormat E_ThreeD_Video_DISPLAYFORMAT_NONE");
                            s3dSkin.set3DTo2DDisplayMode(0);
                        }
                   }).start();
                }
            } else {
                if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
                    Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        s3dSkin.setDisplayFormat(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE);
                        }
                    }).start();
                } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
                    if (!context.getVideoPlayHolder().getDualVideoMode()) {
                        Tools.setPlaySettingOpt(Tools.ITEM_3D_SETTING, context.getString(R.string.play_setting_0_value_1),viewId);
                        context.displayFormat = Tools.getCurrent3dFormat();
                        Log.i(TAG, "context.displayFormat:" + context.displayFormat);
                        if (this.context.displayFormat == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE) {
                            this.context.displayFormat = EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (context.displayFormat == EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_AUTO) {
                                    Log.i(TAG, "TvS3DManager autoDetect3DFormat " + mAutoDetect3DFromatTimes + " times");
                                    // dectectSucess = s3dSkin.autoDetect3DFormat(mAutoDetect3DFromatTimes);
                                    dectectSucess = ThreeDimentionControl.getInstance().autoDetect3DFormat(mAutoDetect3DFromatTimes);
                                    Log.i(TAG, "TvS3DManager autoDetect3D result: " + dectectSucess);
                                    if(!dectectSucess) {
                                        Message msg = handler.obtainMessage(SHOW_TOAST);
                                        msg.obj = context.getResources().getString(R.string.video_3D_auto_detect_info);
                                        handler.sendMessage(msg);
                                    }
                                } else {
                                    s3dSkin.setDisplayFormat(context.displayFormat);
                                }
                            }
                        }).start();
                    }else{
                        showToastTip(context.getResources().getString(R.string.can_not_open_3D));
                    }
                }
            }
        }
    }

    private void changeSubtitleStateRight() {
        String opt = Tools.getPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, viewId);
        if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.subtitle_setting_0_value_1), viewId);
            mSubtitleManager.setSubtitleDataSource(mMMediaPlayer, null) ;
            onInnerSubtitleTrack(true);
            Settings.System.putInt(context.getContentResolver(),"subtitle_item", SUBTITLE_INNER);
        } else if (context.getString(R.string.subtitle_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.subtitle_setting_0_value_2), viewId);
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer) ;
            Settings.System.putInt(context.getContentResolver(),"subtitle_item", SUBTITLE_EXTERNAL);
        } else if (context.getString(R.string.subtitle_setting_0_value_2).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer) ;
            Settings.System.putInt(context.getContentResolver(),"subtitle_item", SUBTITLE_CLOSE);
        }
    }

    private void changeSubtitleStateLeft() {
        String opt = Tools.getPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, viewId);
        if (context.getString(R.string.subtitle_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer) ;
            Settings.System.putInt(context.getContentResolver(),"subtitle_item", SUBTITLE_CLOSE);
        } else if (context.getString(R.string.subtitle_setting_0_value_2).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.subtitle_setting_0_value_1), viewId);
            mSubtitleManager.setSubtitleDataSource(mMMediaPlayer, null) ;
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer) ;
            Settings.System.putInt(context.getContentResolver(),"subtitle_item", SUBTITLE_INNER);
            onInnerSubtitleTrack(true);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.subtitle_setting_0_value_2), viewId);
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer) ;
            Settings.System.putInt(context.getContentResolver(),"subtitle_item", SUBTITLE_EXTERNAL);
        }
    }

    private void onInnerSubtitleTrack(boolean right) {
        SubtitleTrackInfo subtitleAllTrackInfo = null ;
        if(((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().isInPlaybackState()){
            subtitleAllTrackInfo = SubtitleManager.getInstance().getAllSubtitleTrackInfo(
                    ((VideoPlayerActivity) context).getVideoPlayHolder().getPlayerView().getMMediaPlayer());
        }
        if (subtitleAllTrackInfo != null) {
            // Display the film contains all the subtitles number
            SubtitleManager.mVideoSubtitleNo = subtitleAllTrackInfo.getAllInternalSubtitleCount();
            if (SubtitleManager.mVideoSubtitleNo > 0) {
                mSubtitleManager.onSubtitleTrack(mMMediaPlayer);
                mSubtitleManager.setSubtitleTrack(mMMediaPlayer, 0);
                if (right) {
                    SubtitleManager.setSubtitleSettingOpt(2, context.getString(R.string.subtitle_2_value_2) + "1", viewId);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_MEDIA_PLAY == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PAUSE == keyCode ||
                KeyEvent.KEYCODE_MEDIA_NEXT == keyCode ||
                KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode){
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void changeBreakPointFlag(){
        String opt = Tools.getPlaySettingOpt(Tools.ITEM_BREAKPOINT_PLAY_SETTING, viewId);
        if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_BREAKPOINT_PLAY_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
            BreakPointRecord.setBreakPointFlag(context, false);
            context.setBreakPointFlag(false);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_BREAKPOINT_PLAY_SETTING, context.getString(R.string.play_setting_0_value_1), viewId);
            BreakPointRecord.setBreakPointFlag(context, true);
            context.setBreakPointFlag(true);
        }
    }

    private void changeThumbnailMode() {
        Log.i(TAG, "---------changeThumbnailMode----------- ");
        if (!Tools.isCurrPlatformSupportThumbnailMode()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_this_platform));
            return;
        }
        if (context.isVideoSize_4K(1)) {
            showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_4kVideo));
            return;
        }
        if (context.isThreeDMode()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_3DMode));
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis-mLastTimeMillis < 2000) {
            mLastTimeMillis = currentTimeMillis;
            showToastTip(context.getResources().getString(R.string.video_thumbnail_switch_info));
            return;
        }
        mLastTimeMillis = currentTimeMillis;
        String opt = Tools.getPlaySettingOpt(Tools.ITEM_THUMBNAIL_SETTING, viewId);
        if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setPlaySettingOpt(Tools.ITEM_THUMBNAIL_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
            Tools.setThumbnailMode("0");
            context.getThumbnailController().releaseThumbnailView(true);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            if (context.getVideoPlayHolder().getDualVideoMode()) {
                showToastTip(context.getResources().getString(R.string.can_not_open_thumbnail_dual_Video));
            } else {
                Tools.setPlaySettingOpt(Tools.ITEM_THUMBNAIL_SETTING,context.getString(R.string.play_setting_0_value_1), viewId);
                Tools.setThumbnailMode("1");
                context.getThumbnailController().initThumbnailView();
            }
        }
    }

    private void changeRotateMode() {
        Log.i(TAG, "-----changeRotateMode------");
//        if (!Tools.isRotateModeOn()) {
//            showToastTip(context.getResources().getString(R.string.can_not_open_rotate));
//            return;
//        }

        if (!Tools.isPlatformSupportRotate()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_rotate));
            return;
        }

        if (context.getVideoPlayHolder().getDualVideoMode()) {
            showToastTip(context.getResources().getString(R.string.can_not_open_rotate_dual_mode));
            return;
        }
        String opt = Tools.getPlaySettingOpt(Tools.ITEM_ROTATE_SETTING, viewId);
        if (context.getString(R.string.play_setting_0_value_1).equals(opt)) {
            Tools.setRotateMode("0");
            Tools.setPlaySettingOpt(Tools.ITEM_ROTATE_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
            context.setVideoDisplayFullScreen(context.getVideoPlayHolder().getViewId());
            context.imageRotate(context.getVideoPlayHolder().getViewId(), 0);
        } else if (context.getString(R.string.play_setting_0_value_2).equals(opt)) {
            Tools.setRotateMode("1");
            Tools.setPlaySettingOpt(Tools.ITEM_ROTATE_SETTING, context.getString(R.string.play_setting_0_value_1), viewId);
        }
    }

    private void changePictureMode(MChangFlag flag){
    	pictureItemId  =TvPictureManager.getInstance().getPictureModeIdx().ordinal();
    	//Log.i(TAG, "-----changePictureMode------before--PictureModeID="+pictureItemId);
    	int prePicMode =pictureItemId;
    	if(flag == MChangFlag.LEFT){
                   if(EnableBEE){
        		do {
            		pictureItemId--;
    			} while (pictureItemId == 0 || pictureItemId == 4|| pictureItemId == 5 || pictureItemId == 6/*|| 
    					pictureItemId == 8*/);
                    }else{
                            do {
            		pictureItemId--;
    			} while (pictureItemId == 0 || pictureItemId == 4|| pictureItemId == 5 || pictureItemId == 6|| 
    					pictureItemId == 8);
                    }
    	if(pictureItemId < 0){
    			pictureItemId = PictureMode.length-1;
    		}
    	}else if(flag == MChangFlag.RIGHT){
    		if (pictureItemId < (PictureMode.length-1)) {
                         if(EnableBEE){
                	 do {
                		pictureItemId++;
    				  } while (pictureItemId == 0 || pictureItemId == 4|| pictureItemId == 5 || pictureItemId == 6/*|| 
    						pictureItemId == 8*/);
                           }else{
                                    do {
                		pictureItemId++;
    				     } while (pictureItemId == 0 || pictureItemId == 4|| pictureItemId == 5 || pictureItemId == 6|| 
    						pictureItemId == 8);
                            }
            } else {
            	pictureItemId = 1;
            }
        	if(pictureItemId >= PictureMode.length){
        		pictureItemId = 1;
    		}
    	}
    	Tools.setPlaySettingOpt(Tools.ITEM_PICTURE_MODE_SETTING, PictureMode[pictureItemId], viewId);
    	TvPictureManager.getInstance().setPictureModeIdx(EnumPictureMode.values()[pictureItemId]);
             changeColorTem(MChangFlag.REFRESH);
    	Log.i(TAG, "-----changePictureMode------now ID= "+TvPictureManager.getInstance().getPictureModeIdx().ordinal()+" "+PictureMode[pictureItemId]);
        //do bee 
        if(EnableBEE){
          if ((prePicMode ==TvPictureManager.PICTURE_MODE_SPORTS ||prePicMode ==TvPictureManager.PICTURE_MODE_VIVID)&& pictureItemId == TvPictureManager.PICTURE_MODE_NATURAL/*BEE*/
            ||prePicMode == TvPictureManager.PICTURE_MODE_NATURAL) {      
            // bee                  
            context.sendBroadcast(new Intent("com.android.toptech.bee"));  
            }
         }
    }
    private void changeColorTem(MChangFlag flag){
    	colortemItemId=TvPictureManager.getInstance().getColorTempratureIdx();
    	//Log.i(TAG, "---changeColorTem---before--colortemItemId=="+colortemItemId);
    	if(flag == MChangFlag.LEFT){
    		colortemItemId--;
			if(colortemItemId < 0)
				colortemItemId = ColorTem.length-1;
    	}else if(flag == MChangFlag.RIGHT){
    		colortemItemId++;
    		if(colortemItemId >= ColorTem.length)
    			colortemItemId = 0;
    	}
    	Tools.setPlaySettingOpt(Tools.ITEM_COLOR_TEMPERATURE, ColorTem[colortemItemId], viewId);
    	TvPictureManager.getInstance().setColorTemprature(colortemItemId);
    	//Log.i(TAG, "-----colortemItemId---now--ID-=="+TvPictureManager.getInstance().getColorTempratureIdx()+"  "+ColorTem[colortemItemId]);
    }
    private void changeNoiseReduction(MChangFlag flag){
    	noisereduuctionItemId=TvPictureManager.getInstance().getNoiseReduction();
    	//Log.i(TAG, "---changeNoiseReduction----before--noisereduuctionItemId=="+noisereduuctionItemId);
    	if(flag == MChangFlag.LEFT){
    		noisereduuctionItemId--;
			if(noisereduuctionItemId < 0)
				noisereduuctionItemId = NoiseReduuction.length-1;
    	}else if(flag == MChangFlag.RIGHT){
    		noisereduuctionItemId++;
    		if(noisereduuctionItemId >= NoiseReduuction.length)
    			noisereduuctionItemId = 0;
    	}
    	Tools.setPlaySettingOpt(Tools.ITEM_IMAGE_NOISEREDUCTION, NoiseReduuction[noisereduuctionItemId], viewId);
    	TvPictureManager.getInstance().setNoiseReduction(noisereduuctionItemId);
    	//Log.i(TAG, "-----noisereduuctionItemId---now---=="+TvPictureManager.getInstance().getNoiseReduction()+"  "+NoiseReduuction[noisereduuctionItemId]);
    }
    private void showToastTip(String strMessage) {
        Toast toast = Toast.makeText(context, strMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    private void initSubtitle(int item){
    	switch (item) {
		case SUBTITLE_CLOSE:
			 Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.play_setting_0_value_2), viewId);
             mSubtitleManager.offSubtitleTrack(mMMediaPlayer) ;
			break;
		case SUBTITLE_INNER:
			Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.subtitle_setting_0_value_1), viewId);
            mSubtitleManager.setSubtitleDataSource(mMMediaPlayer, null) ;
            onInnerSubtitleTrack(true);
			break;
		case SUBTITLE_EXTERNAL:
			Tools.setPlaySettingOpt(Tools.ITEM_SUBTITLE_SETTING, context.getString(R.string.subtitle_setting_0_value_2), viewId);
            mSubtitleManager.offSubtitleTrack(mMMediaPlayer) ;
			break;
		default:
			break;
		}
    }
}
