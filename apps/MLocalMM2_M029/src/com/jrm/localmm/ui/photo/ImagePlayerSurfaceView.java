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

package com.jrm.localmm.ui.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.InflateException;
import android.widget.Toast;
import android.util.Log;
import android.net.Uri;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jrm.localmm.util.Constants;
import com.jrm.localmm.R;
import com.jrm.localmm.util.ToastFactory;
import com.jrm.localmm.business.photo.GifDecoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;
import android.os.SystemProperties;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jrm.localmm.util.Tools;
import com.mstar.android.media.MMediaPlayer;

public class ImagePlayerSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private static final String TAG = "ImagePlayerSurfaceView";
    private static final int MEDIA_PLAYER_STATE_IDLE = 0;
    private static final int MEDIA_PLAYER_STATE_PREPARING = MEDIA_PLAYER_STATE_IDLE + 1;
    private static final int MEDIA_PLAYER_STATE_PREPARED = MEDIA_PLAYER_STATE_IDLE + 2;
    private static final int MEDIA_PLAYER_STATE_STARTED = MEDIA_PLAYER_STATE_IDLE + 3;
    private static final int MEDIA_PLAYER_STATE_ERROR = MEDIA_PLAYER_STATE_IDLE + 4;
    private int mCurrentMediaPlayerState = MEDIA_PLAYER_STATE_IDLE;
    private Bitmap bitmap;
    private InputStream is;
    private boolean isStop = false;
    private int delta = 1;
    private ImagePlayerActivity mImagePlayerActivity;
    private GifDecoder gDecoder;
    private MMediaPlayer mMMediaPlayer=null;
    private boolean mNextFilePrepared = false;
    private boolean mPrevFilePrepared = false;
    private String sPath = "";
    private SurfaceHolder sfholder=null;
    private int mSurfaceWidth = 1920;
    private int mSurfaceHeight = 1080;
    private int mPanelWidth;
    private int mPanelHeight;
    private float imgDecodedWidth;
    private float imgDecodedHeight;
    private int dstWidthAfterScale;
    private int dstHeightAfterScale;
    private int cropStartX = 0;
    private int cropStartY = 0;
    private Thread updateTimer;
    private android.graphics.Rect dst = new android.graphics.Rect();
    private Thread mStartImagePlayerThread;

    public ImagePlayerSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        getContext();
    }

    public ImagePlayerSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        getHolder().addCallback(this);
    }

    public ImagePlayerSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        mSurfaceHeight = height;
        mSurfaceWidth = width;
        if (SystemProperties.getInt("mstar.4k2k.photo", 0) != 1) {
            if ((mPanelHeight > mSurfaceHeight) || (mPanelWidth > mSurfaceWidth)) {
                mSurfaceHeight = mPanelHeight;
                mSurfaceWidth = mPanelWidth;
            }
            if (mSurfaceWidth < 1920) {
                setSurfaceSize();
            }
        }

        Log.i(TAG, "surfaceChanged--mSurfaceWidth:"+mSurfaceWidth+"---mSurfaceHeight:"+mSurfaceHeight);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        sfholder = holder;
        if (SystemProperties.getInt("mstar.4k2k.photo", 0) != 1) {
            adjustSurfaceSize();
        }

        Log.i(TAG, "surfaceCreated--mSurfaceWidth:"+mSurfaceWidth+"---mSurfaceHeight:"+mSurfaceHeight);
        if(!sPath.equals(""))
            openImagePlayer();
        if (bitmap != null) {
            drawImage();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "********surfaceDestroyed******");
        /* Because SN would set desk-display-mode, so AN APK don't need do this again.
        if (SystemProperties.getInt("mstar.desk-display-mode", 0) != 0) {
            SystemProperties.set("mstar.desk-display-mode", "0");
        }
        */
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        stopPlayback(true);
    }

    public void setImagePath(String imagePath,ImagePlayerActivity ppa) {
        sPath = imagePath;
        mImagePlayerActivity = ppa;
        Log.i(TAG,"the path is:"+imagePath);
    }

    public int getPlayTimeEachFrame(){
        if(gDecoder != null){
            return Math.max(100, gDecoder.nextDelay()) / delta;
        }
        return 0;
    }

    public int getPlayTime() {
        int allDelay = 0;
        if(gDecoder != null) {
            int n = gDecoder.getFrameCount();
            for(int i = 0; i < n; i++) {
                allDelay += Math.max(100, gDecoder.getDelay(i));
            }
        }
        Log.i(TAG,"getPlayTime: allDelay = " + allDelay);
        return allDelay;
    }

    /*public int getPlayTime() {
        if(gDecoder != null){
            return gDecoder.getFrameCount() * getPlayTimeEachFrame();
        }
        return 0;
    }*/

    public int getFrameCount(){
        return gDecoder.getFrameCount();
    }

    private void frameDelay() {
        try {
            Thread.sleep(getPlayTimeEachFrame());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setStart(GifDecoder.IGifCallBack mGifCallBack) {
        if (updateTimer != null && updateTimer.isAlive()) {
            isStop = true;
            frameDelay();
        }
        gDecoder.setOnGifListener(mGifCallBack);
        updateTimer = new Thread(new Runnable() {
            @Override
            public void run() {
                frameDelay(); // the first frame delay
                while (true) {
                    if (isStop) {
                        return;
                    }
                    drawImage();
                    frameDelay();
                }
            }
        });
        isStop = false;
        updateTimer.start();
    }

    public void resetMediaPlayer() {
        Log.i(TAG, "resetMediaPlayer mMMediaPlayer:" + mMMediaPlayer + " mCurrentMediaPlayerState:" + mCurrentMediaPlayerState);
            if (mMMediaPlayer != null) {
                mMMediaPlayer.reset();
                mMMediaPlayer = null;
            }
    }

    public void stopPlayback(boolean bActivityExit) {
        Log.i(TAG, "------stopPlayback ------- bActivityExit:" + bActivityExit);
        isStop = true;
        if (mCurrentMediaPlayerState == MEDIA_PLAYER_STATE_IDLE || Constants.bReleasingPlayer) {
            return;
        }
        synchronized(this) {
            if (bActivityExit) {
                // When abnormal stop play.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "mMMediaPlayer:" + mMMediaPlayer + " mCurrentMediaPlayerState:" + mCurrentMediaPlayerState);
                        if (mMMediaPlayer != null ) {
                            Constants.bReleasingPlayer = true;
                            while (mCurrentMediaPlayerState < MEDIA_PLAYER_STATE_PREPARED) {
                                try {
                                    Log.w(TAG,"player state is not prepared,wait 0.6 second!");
                                    Thread.sleep(600);
                                } catch (InterruptedException e) {
                                 Log.e(TAG,"thread interrupt exception");
                                }
                            }
                            try {
                                 if ((mMMediaPlayer !=null) && mMMediaPlayer.isPlaying()) {
                                     Log.i(TAG, "*****stop start*****");
                                     mMMediaPlayer.stop();
                                     Log.i(TAG, "*****release start*****");
                                     mMMediaPlayer.release();
                                     Log.i(TAG, "*****release end*****");
                                     mMMediaPlayer = null;
                                 }
                                 Constants.bReleasingPlayer = false;
                            } catch (IllegalStateException ex) {
                                 Log.e(TAG, "IllegalStateException");
                            }
                        }
                    }
                }).start();
            } else {
                // call before play next.
                if (mMMediaPlayer != null ) {
                    Log.i(TAG, "mMMediaPlayer.stop()");
                    mMMediaPlayer.stop();
                    Log.i(TAG, "mMMediaPlayer.release()");
                    mMMediaPlayer.release();
                    Log.i(TAG, "mMMediaPlayer= null");
                    mMMediaPlayer = null;
                    mCurrentMediaPlayerState = MEDIA_PLAYER_STATE_IDLE;
                }
            }
        }
    }

    public void setStop() {
        isStop = true;
    }

    public boolean startNextVideo(String sPath, ImagePlayerActivity ppa) {
        Log.i(TAG, "startNextVideo  sPath:"+sPath+"  mImagePlayerActivity:"+mImagePlayerActivity+ " mCurrentMediaPlayerState:" + mCurrentMediaPlayerState);
        if (mCurrentMediaPlayerState == MEDIA_PLAYER_STATE_PREPARING) {
            return false;
        }
        setImagePath(sPath,ppa);
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        drawImage();
        stopPlayback(false);
        mImagePlayerActivity.startShowProgress();
        openImagePlayer();
        return true;
    }

    private void setDstImageSize(float fAngle,float fRatio) {
        Log.i(TAG, "setDstImageSize -------------- begin " + "fAngle:" + fAngle + " fRatio:" + fRatio);
        double radian = fAngle * Math.PI / 180;
        int dstWidth = (int)((int)imgDecodedWidth * fRatio);
        int dstHeight = (int)((int)imgDecodedHeight * fRatio);
        dstWidthAfterScale = (int)(dstWidth * Math.abs(Math.cos(radian)) +
                dstHeight * Math.abs(Math.sin(radian))) - 1;
        dstHeightAfterScale = (int)(dstHeight * Math.abs(Math.cos(radian)) +
                dstWidth * Math.abs(Math.sin(radian))) - 1;
        if (dstHeightAfterScale>mSurfaceHeight) {
            cropStartY = (int)(dstHeightAfterScale-mSurfaceHeight)/2;
        } else {
            cropStartY = 0;
        }
        if (dstWidthAfterScale>mSurfaceWidth) {
            cropStartX = (int)(dstWidthAfterScale-mSurfaceWidth)/2;
        } else {
            cropStartX = 0;
        }
        Log.i(TAG, "after setDstImageSize cropStartY:" + cropStartY + " cropStartX:" + cropStartX);
        Log.i(TAG, "After setDstImageSize dstWidthAfterScale:"+dstWidthAfterScale+" dstHeightAfterScale:"+dstHeightAfterScale);
    }

    protected void rotateImage(float fAngle,float fRatio) {
        if (mMMediaPlayer != null) {
            if ((fRatio == 1.0f) && (fAngle == 90 || fAngle == 270 || fAngle == -90 || fAngle == -270)) {
                int tmpWidth =  (int)(imgDecodedWidth + 1.5);
                if (tmpWidth > mSurfaceWidth) {
                    fRatio = Math.min((float)mSurfaceHeight/(float)imgDecodedWidth,(float)mSurfaceWidth/(float)imgDecodedHeight);
                }
            }
            if ((dstWidthAfterScale > 3840) && (dstHeightAfterScale > 2160)) {
                // Tools.setVideoMute(true, 50);
                mMMediaPlayer.ImageRotateAndScale(fAngle, fRatio,fRatio,true);
                // Tools.setVideoMute(false, 0);
            } else {
                // Tools.setVideoMute(true, 50);
                mMMediaPlayer.ImageRotateAndScale(fAngle, fRatio,fRatio,false);
                setDstImageSize(fAngle, fRatio);
                // Tools.setVideoMute(false, 0);
            }
        }
    }

    protected void scaleImage(float fAngle,float fRatio){
        if(mMMediaPlayer != null){
            // Tools.setVideoMute(true, 50);
            mMMediaPlayer.ImageRotateAndScale(fAngle, fRatio,fRatio,true);
            // Tools.setVideoMute(false, 0);
            setDstImageSize(fAngle, fRatio);
        }
    }

    // if panRight > 0 means move Right direction
    // if panRight < 0 means move Left direction
    // if panDown > 0 means move Down direction
    // if panDown < 0 means move Up direction
    public void moveDirection(int panRight,int panDown) {
        Log.i(TAG, "------- moveDirection pRight:" + panRight + " pDown:" + panDown);
        if ((mSurfaceWidth > dstWidthAfterScale) && (mSurfaceHeight >
                dstHeightAfterScale)) {
            return;
        }
        if (panRight != 0){
            cropStartX += panRight;
            if (cropStartX < 0){
                cropStartX = 0;
               // return;
            } else if ((cropStartX+mSurfaceWidth) > dstWidthAfterScale) {
                cropStartX = dstWidthAfterScale - mSurfaceWidth - 1;
               // return;
            }
        }
        if (panDown != 0) {
            cropStartY += panDown;
            if (cropStartY < 0){
                cropStartY = 0;
               // return;
            } else if ((cropStartY+mSurfaceHeight) > dstHeightAfterScale) {
                cropStartY = dstHeightAfterScale - mSurfaceHeight - 1;
              //  return;
            }
        }
        if(mMMediaPlayer != null) {
            Log.i(TAG, "dstWidthAfterScale:" + dstWidthAfterScale + " dstHeightAfterScale:" + dstHeightAfterScale);
            Log.i(TAG, "cropStartX:" + cropStartX + " cropStartY:" + cropStartY);
            int cropWidth = Math.min(mSurfaceWidth, dstWidthAfterScale);
            int cropHeight = Math.min(mSurfaceHeight,dstHeightAfterScale);
            Log.i(TAG, "cropWidth:" + cropWidth);
            Log.i(TAG, "cropHeight:" + cropHeight);
            // ImageCropRect API's Parameter should follow:
            // 0 <= cropStartX < dstWidthAfterScale
            // 0 <= cropStartY < dstHeightAfterScale
            // (cropStartX + cropWidth) <= dstWidthAfterScale
            // (cropStartY + cropHeight) <= dstHeightAfterScale

            if (cropStartX < dstWidthAfterScale && cropStartY < dstHeightAfterScale &&
                    cropStartX + cropWidth <= dstWidthAfterScale && cropStartY + cropHeight <= dstHeightAfterScale) {
                Log.i(TAG, "ImageCropRect parameter is valid");
                boolean bImagePanSuccess = mMMediaPlayer.ImageCropRect(cropStartX, cropStartY, cropWidth, cropHeight);
                showToast(bImagePanSuccess ? getResources().getString(R.string.photo_pan_success_toast) : getResources().getString(R.string.photo_pan_failed_toast), Gravity.CENTER, Toast.LENGTH_SHORT);
            } else {
                Log.i(TAG, "ImageCropRect parameter is not valid");
                showToast(getResources().getString(R.string.photo_pan_parameter_invalid), Gravity.CENTER, Toast.LENGTH_SHORT);
            }
        }
    }

    private void startImagePlayer() {
        Log.i(TAG, "mMMediaPlayer.setDisplay()");
        if(!mMMediaPlayer.isPlaying())
        	return ;
        mMMediaPlayer.setDisplay(sfholder);
        mStartImagePlayerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "mMMediaPlayer.start()");
                    mCurrentMediaPlayerState = MEDIA_PLAYER_STATE_PREPARING;
                    mMMediaPlayer.start();
                    mCurrentMediaPlayerState = MEDIA_PLAYER_STATE_PREPARED;
                    mImagePlayerActivity.stopShowingProgress();
                    mImagePlayerActivity.hideControlDelay();
                    mImagePlayerActivity.startPPT_Player();
                    if (Constants.bPhotoSeamlessEnable) {
                        prepareNextPhoto(1);
                        prepareNextPhoto(-1);
                    }
                } catch (Exception e){
                    mImagePlayerActivity.stopShowingProgress();
                    /*Toast toast = ToastFactory.getToast(mImagePlayerActivity,
                            getResources().getString(R.string.photo_out_of_memory_toast), Gravity.CENTER);
                    toast.show();*/
                    if (mImagePlayerActivity != null) {
                        mImagePlayerActivity.finish();
                    }
                }
            }
        });
        mStartImagePlayerThread.start();
    }

    public Thread getImagePlayerThread() {
        return mStartImagePlayerThread;
    }

    private void closeFileInputStream(final Closeable c) {
       if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (Throwable t) {
        }
    }

    protected void prepareNextPhoto(int delta) {
        BitmapFactory.Options  options = new BitmapFactory.Options();
        String imgPath = mImagePlayerActivity.getNextPhotoPath(delta);
        if (imgPath == null)
            return;
        FileInputStream nextFileStream = null;
        boolean bSuccess = false;
        try {
            nextFileStream = new FileInputStream(imgPath);
            FileDescriptor fd  = nextFileStream.getFD();
            if (fd == null) {
                closeFileInputStream(nextFileStream);
                return;
            }
            options.inPurgeable = false;
            options.inInputShareable = true;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd, null, options);
        }  catch (Exception e) {
            e.printStackTrace();
            bSuccess = false;
            Log.i(TAG,"prepare next photo failed!");
            return;
        } finally {
            closeFileInputStream(nextFileStream);
            nextFileStream = null;
        }
        int sampleSize = 1;
        int imgWidth = options.outWidth;
        int imgHeight = options.outHeight;
        Log.i(TAG,"the decoded next photo w:"+imgWidth+" ;h:"+imgHeight);
        float initialSize = Math.max((float)imgWidth/(float)mSurfaceWidth,(float)imgHeight/(float)mSurfaceHeight);
        float scaleFactor = 1.0f;
        if ((initialSize == 1.0f) || (initialSize == 2.0f) || (initialSize == 4.0f) || (initialSize == 8.0f)) {
            scaleFactor = 1.0f;
            sampleSize = (int)initialSize;
        } else if(initialSize < 1.0f) {
            scaleFactor = 1.0f;
            sampleSize = 1;
        } else {
            if (initialSize < 2.0f) {
                 sampleSize = 1;
            } else if (initialSize < 4.0f) {
                 sampleSize = 2;
            } else if (initialSize < 8.0f) {
                 sampleSize = 4;
            } else {
                 sampleSize = 8;
            }
            scaleFactor = sampleSize / initialSize;
        }
        MMediaPlayer.InitParameter  initParameter = mMMediaPlayer.new InitParameter();
        initParameter.degrees = 0;
        initParameter.scaleX = scaleFactor;
        initParameter.scaleY = scaleFactor;
        initParameter.cropX = 0;
        initParameter.cropY = 0;
        initParameter.cropWidth = 0;
        initParameter.cropHeight = 0;
        int index = (delta < 0) ? 0 : 1;
        try {
          bSuccess = mMMediaPlayer.ImageDecodeNext(imgPath,sampleSize,mSurfaceWidth,mSurfaceHeight,initParameter,index);
        } catch (Exception e) {
          Log.i(TAG,"image decode next exception");
          bSuccess = false;
        }
        Log.i(TAG,"decode success:"+bSuccess);
        if (delta < 0)
            mPrevFilePrepared = bSuccess;
        else
            mNextFilePrepared = bSuccess;
    }

    public boolean showNextPhoto(final int delta) {
        boolean bSuccess = false;
        if (delta < 0)
            bSuccess = mPrevFilePrepared;
        else
            bSuccess = mNextFilePrepared;
        if (mMMediaPlayer != null && bSuccess) {
            int index = (delta < 0) ? 0 : 1;
            mMMediaPlayer.ImageShowNext(index);
            mNextFilePrepared = false;
            mPrevFilePrepared = false;
            new Thread(new Runnable() {
            @Override
            public void run() {
               mImagePlayerActivity.setCurrentPos(delta);
               prepareNextPhoto(1);
               prepareNextPhoto(-1);
            }}).start();
            //return true;
        } else {
            String sMessage = "The photo is decoding,please try again later...";
            showToast(sMessage,Gravity.CENTER, Toast.LENGTH_SHORT);
        }
        return true;
    }

    protected void openImagePlayer(){
        try {
            mCurrentMediaPlayerState = MEDIA_PLAYER_STATE_IDLE;
            resetMediaPlayer();
            mMMediaPlayer = new MMediaPlayer();
            mMMediaPlayer.reset();
            Log.i(TAG,"the photo path is:"+sPath);
            Uri mUri= Uri.parse(sPath);
            Log.i(TAG, "mMMediaPlayer.setDataSource:"+mUri);
            mMMediaPlayer.setOnErrorListener(mErrorListener);
            mMMediaPlayer.setDataSource(this.getContext(), mUri);
            mMMediaPlayer.setOnPreparedListener(new MMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // The process of getting the width or height of photos is just like video
                    // flow as the image flow
                    int imgOriginalWidth = mp.getVideoWidth();
                    int imgOriginalHeight = mp.getVideoHeight();
                    Log.i(TAG, "imgOriginalWidth:" + imgOriginalWidth + " imgOriginalHeight:" + imgOriginalHeight);
                    if (SystemProperties.getInt("mstar.4k2k.photo", 0) == 1) {
                        if ((imgOriginalWidth >= 3840) && (imgOriginalHeight >= 2160)) {
                            mSurfaceWidth = 3840;
                            mSurfaceHeight = 2160;
                            Log.i(TAG, "mSurfaceWidth:" + mSurfaceWidth + " mSurfaceHeight:" + mSurfaceHeight);
                        } else {
                            adjustSurfaceSize();
                        }
                    }
                    // Currently media player do not support image size >= 1920*8 * 1080*8
                    if (imgOriginalWidth * imgOriginalHeight >= 1920 * 8 * 1080 * 8) {
                        Log.i(TAG, "Currently media player do not support image size >= 1920*8 * 1080*8");
                        Toast toast = ToastFactory.getToast(mImagePlayerActivity,
                                getResources().getString(R.string.can_not_decode),
                                Gravity.CENTER);
                        toast.show();
                        resetMediaPlayer();
                        if (mImagePlayerActivity.getPhotoFileListSize() <= 1) {
                            mImagePlayerActivity.finish();
                        } else {
                            mImagePlayerActivity.moveNextOrPrevious(1);
                        }
                        return;
                    }
                    int sampleSize = 1;
                    float initialSize = Math.max((float)imgOriginalWidth/(float)mSurfaceWidth,(float)imgOriginalHeight/(float)mSurfaceHeight);
                    float scaleFactor = 1.0f;
                    /*if ((initialSize == 1.0f) || (initialSize == 2.0f) || (initialSize == 4.0f) || (initialSize == 8.0f)) {
                        scaleFactor = 1.0f;
                        sampleSize = (int)initialSize;
                    } else if(initialSize < 1.0f) {
                        scaleFactor = 1.0f;
                        sampleSize = 1;
                    } else {
                        if (initialSize < 2.0f) {
                            sampleSize = 1;
                        } else if (initialSize < 4.0f) {
                            sampleSize = 2;
                        } else if (initialSize < 8.0f) {
                            sampleSize = 4;
                        } else {
                            sampleSize = 8;
                        }
                        scaleFactor = sampleSize / initialSize;
                    }*/
                    //scale pic
                    final float scaleBase = 1.0f;
                    scaleFactor = scaleBase/initialSize;
                    if (scaleFactor > 4.0f) {
                    	scaleFactor=4.0f;
					}
                    //end
                    if(sPath.endsWith(".MPO") || sPath.endsWith(".mpo")) {
                        if (imgOriginalWidth * imgOriginalHeight > 2000 * 2000) {
                            if (sampleSize < 8) {
                                sampleSize *= 2;
                            }
                            scaleFactor = sampleSize / initialSize;
                        }
                    }

                    imgDecodedWidth = (int)(imgOriginalWidth/sampleSize * scaleFactor);
                    imgDecodedHeight = (int)(imgOriginalHeight/sampleSize * scaleFactor);
                    dstWidthAfterScale = (int)imgDecodedWidth;
                    dstHeightAfterScale = (int)imgDecodedHeight;
                    MMediaPlayer.InitParameter  initParameter = mMMediaPlayer.new InitParameter();
                    initParameter.degrees = 0;
                    initParameter.scaleX = scaleFactor;
                    initParameter.scaleY = scaleFactor;
                    initParameter.cropX = 0;
                    initParameter.cropY = 0;
                    initParameter.cropWidth = 0;
                    initParameter.cropHeight = 0;
                    Log.i(TAG, "imgDecodedWidth:" + imgDecodedWidth + " imgDecodedHeight:" + imgDecodedHeight + " sampleSize:" + sampleSize + " scaleFactor:" + initParameter.scaleX);
                    mMMediaPlayer.SetImageSampleSize(sampleSize, mSurfaceWidth, mSurfaceHeight, initParameter);
                    initParameter = null;
                    startImagePlayer();
                } });
            Log.i(TAG, "mMMediaPlayer.prepareAsync()");
            mMMediaPlayer.prepareAsync();
        } catch (Exception e){
            Log.i(TAG, "Exception:" + e);
            mImagePlayerActivity.showTipDialog(mImagePlayerActivity.getResources().getString(R.string.file_not_support));
            /*String strMessage = "The file is not supported!";
            try {
                Toast toast = ToastFactory.getToast(mImagePlayerActivity,
                        strMessage, Gravity.CENTER);
                toast.show();
            } catch (InflateException inflateException) {
                Log.i(TAG, "InflateException:" + inflateException);
            }
            mImagePlayerActivity.moveNextOrPrevious(1);*/
            // mImagePlayerActivity.finish();
        }
    }

    private MMediaPlayer.OnErrorListener mErrorListener = new MMediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.e(TAG, "Error: " + framework_err + "," + impl_err);
            mCurrentMediaPlayerState = MEDIA_PLAYER_STATE_ERROR;
            /* If an error handler has been supplied, use it and finish. */
            mImagePlayerActivity.onError(mp,framework_err,impl_err);
            return true;
        }
    };

    public boolean decodeBitmapFromNet(String path, ImagePlayerActivity player){
        if (bitmap!= null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (gDecoder == null) {
        } else {
            gDecoder.reset();
            //gDecoder.resetFrame();
        }
        gDecoder = new GifDecoder();
        try {
            is = new URL(path).openStream();
            if (gDecoder != null) {
                gDecoder.read(is);
                if (gDecoder.err()) {
                    bitmap = player.decodeBitmap(path);
                } else {
                    bitmap = gDecoder.getImage();// first
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawImage();
        return true;
    }

    public boolean setSrc(String path,ImagePlayerActivity player) {
        // isStop = false;
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (gDecoder == null){
        } else {
            gDecoder.reset();
        }
        gDecoder = new GifDecoder();
        try {
            is = new FileInputStream(path);
            if (gDecoder != null) {
                gDecoder.read(is);
                if (gDecoder.err()) {
                    bitmap = player.decodeBitmap(path);
                } else {
                    bitmap = gDecoder.getImage();// first
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        drawImage();
        return true;
    }

    protected void drawImage() {
        ImagePlayerSurfaceView.this.postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
         if (bitmap != null && canvas != null) {
             int srcWidth = bitmap.getWidth();
             int srcHeight = bitmap.getHeight();
             // Some GIF photo's size is Larger than Screen Size, So need to be scaled to adapt to ScreenSize..
             if (srcWidth > this.getWidth() && srcHeight > this.getHeight() && this.getWidth() > 0 && this.getHeight() > 0) {
                 float widthScale = (float) this.getHeight() / srcWidth;
                 float heightScale = (float) this.getHeight() / srcHeight;
                 int width, height;
                 if (widthScale > heightScale) {
                     width = Math.round(bitmap.getWidth() * heightScale);
                     height = Math.round(bitmap.getHeight() * heightScale);
                 } else {
                     width = Math.round(bitmap.getWidth() * widthScale);
                     height = Math.round(bitmap.getHeight() * widthScale);
                 }
                 bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
             }
            Paint paint = new Paint();
            android.graphics.Rect src = new android.graphics.Rect();
            src.left = 0;
            src.top = 0;
            src.bottom = bitmap.getHeight();
            src.right = bitmap.getWidth();
            dst.left = 0;
            dst.top = 0;
            dst.bottom = this.getHeight();
            dst.right = this.getWidth();
            if(!bitmap.isRecycled()){
                paint.setColor(Color.BLACK);
                canvas.drawRect(dst, paint);
                center(src,dst);
                canvas.drawBitmap(bitmap, src, dst, paint);
                if (gDecoder.getFrameCount() > 0) {
                    bitmap = gDecoder.nextBitmap();
                }
            }
        }else {
            if (canvas != null) {
                dst.left = 0;
                dst.top = 0;
                dst.bottom = this.getHeight();
                dst.right = this.getWidth();
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAlpha(0);
                canvas.drawRect(dst, paint);
            }
        }

    }


    protected void center(android.graphics.Rect src,android.graphics.Rect dst) {
        //bmp = resizeDownIfTooBig(bmp, true);
        float height = bitmap.getHeight();
        float width = bitmap.getWidth();
        float deltaX = 0, deltaY = 0;
        int viewHeight = getHeight();
        if (height <= viewHeight) {
            deltaY = (viewHeight - height) / 2 - src.top;
        }  else if (src.top > 0) {
            deltaY = -src.top;
        } else if (src.bottom < viewHeight) {
            deltaY = getHeight() - src.bottom;
        }
        int viewWidth = getWidth();
        if (width <= viewWidth) {
            deltaX = (viewWidth - width) / 2 - src.left;
        } else if (src.left > 0) {
            deltaX = -src.left;
        } else if (src.right < viewWidth) {
            deltaX = viewWidth - src.right;
        }
        dst.top = src.top + (int)deltaY;
        dst.left = src.left + (int)deltaX;
        dst.bottom = bitmap.getHeight() + (int)deltaY;
        dst.right = bitmap.getWidth() + (int)deltaX;
    }

    protected void cleanView(int width, int height) {
        Canvas canvas = getHolder().lockCanvas();
        if (bitmap != null && canvas != null) {
            Paint paint = new Paint();
            android.graphics.Rect src = new android.graphics.Rect();
            paint.setColor(Color.BLACK);
            src.left = 0;
            src.top = 0;
            src.bottom = height;
            src.right = width;
            canvas.drawRect(src, paint);
            //canvas.save(1);
        }
        if (canvas != null) {
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    protected void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    protected boolean updateView() {
        if (this.bitmap != null) {
            invalidate();
            return true;
        } else {
            return false;
        }
    }

    private void setSurfaceSize() {
        int[] config = Tools.getOsdSize();
        mSurfaceWidth = config[0];
        mSurfaceHeight = config[1];
    }

    private void showToast(final String text, int gravity, int duration) {
        Toast toast = ToastFactory.getToast(mImagePlayerActivity, text, gravity);
        toast.show();
    }

    private void adjustSurfaceSize() {
        int[] config = Tools.getPanelSize();
        mPanelWidth = config[0];
        mPanelHeight = config[1];
        if (mPanelWidth != 0 && mPanelHeight != 0) {
            Log.i(TAG, "getPanelConfig true");
        } else {
            Log.i(TAG, "getPanelConfig false");
        }
        if ((mPanelHeight > mSurfaceHeight) || (mPanelWidth > mSurfaceWidth)) {
            mSurfaceHeight = mPanelHeight;
            mSurfaceWidth = mPanelWidth;
        }
    }
}
