//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2012 - 2015 MStar Semiconductor, Inc. All rights reserved.
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
package com.jrm.localmm.business.adapter;

import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.io.IOException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jrm.localmm.business.data.BaseData;
import com.jrm.localmm.R;
import com.jrm.localmm.util.Constants;
import com.jrm.localmm.ui.main.FileBrowserActivity;
import com.jrm.localmm.ui.main.MediaThumbnail;

/**
 *
 * Provide adapter to gridview.
 *
 * @date 2015-01-01
 *
 * @version 1.0.0 *
 *
 * @author andrew.wang
 *
 */
public class GridAdapter extends BaseAdapter {

    private MediaThumbnail mtb;

    private final static String TAG="GridAdapter";

    private LayoutInflater mInflater;

    private ArrayList<BaseData> gridOnePage = new ArrayList<BaseData>();

    private LruCache<String,Drawable>mMemoryCache;

    private Resources resourceGrid;

    private static int thumbnailVideoOrMusic=-100;

    private Handler handler;

    private int itemHeight = 0;

    private int itemWidth = 0;

    public AsyncLoadImageTask imagePosition2Task[] = new AsyncLoadImageTask[Constants.GRID_MODE_DISPLAY_NUM+1];

    // just use to tag those videos which have no thumbnails
    public  int isFinish[] =new int[Constants.GRID_MODE_DISPLAY_NUM+1];

    public  int mTypeOfPosition[] = new int[Constants.GRID_MODE_DISPLAY_NUM+1];

    public  ImageView ivHasCancelTask[]=new ImageView[Constants.GRID_MODE_DISPLAY_NUM+1];

    private final static int maxPictureSize = 5242880;
    public GridAdapter(Context context,ArrayList<BaseData>gridOnePage,Handler handler) {
        this.mInflater = LayoutInflater.from(context);
        this.gridOnePage=gridOnePage;
        this.handler=handler;
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/8;
        mMemoryCache = new LruCache<String,Drawable>(cacheSize){
           @Override
           protected int sizeOf(String key,Drawable drawable){
              BitmapDrawable bd = (BitmapDrawable)drawable;
              Bitmap bitmap = bd.getBitmap();
              return bitmap.getByteCount();
           }
        };
    }
    public int getCount() {
        //return mData.size();
        if (gridOnePage==null) return 0;
        return gridOnePage.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gridview_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setOnHoverListener(new GridViewOnHoverListener());
            convertView.setTag(holder);
        } else {
                holder = (ViewHolder) convertView.getTag();
        }
        Drawable drawable =null;
        String url =gridOnePage.get(position).getPath();
        int flag4HasCacheOrNot =0;
        if (gridOnePage.get(position).getType() == Constants.FILE_TYPE_VIDEO) {
            Drawable tmpDrawable = hasDrawableInCache(url);
            if (tmpDrawable!=null) {
                holder.img.setImageDrawable(tmpDrawable);
                isFinish[position]=Constants.FINISHED;
                flag4HasCacheOrNot=1;
            } else {
                drawable = FileBrowserActivity.fileTypeDrawable.get(3);
                holder.img.setImageDrawable(drawable);
                isFinish[position]=Constants.UNFINISHED;
            }
            mTypeOfPosition[position] = Constants.GRID_POSITION_IS_VIDEO;
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_FILE) {
            imagePosition2Task[position]=null;
            drawable = FileBrowserActivity.fileTypeDrawable.get(0);
            holder.img.setImageDrawable(drawable);
            isFinish[position]=Constants.FINISHED;
            mTypeOfPosition[position] = Constants.GRID_POSITION_IS_OTHERS;
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_PICTURE) {
            Drawable tmpDrawable = hasDrawableInCache(url);
            if (tmpDrawable!=null) {
                holder.img.setImageDrawable(tmpDrawable);
                flag4HasCacheOrNot=1;
                isFinish[position]=Constants.FINISHED;
            } else {
                drawable = FileBrowserActivity.fileTypeDrawable.get(1);
                holder.img.setImageDrawable(drawable);
                isFinish[position]=Constants.UNFINISHED;
            }
            mTypeOfPosition[position] = Constants.GRID_POSITION_IS_PICTURE;
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_SONG) {
            Drawable tmpDrawable = hasDrawableInCache(url);
            if (tmpDrawable!=null) {
                holder.img.setImageDrawable(tmpDrawable);
                flag4HasCacheOrNot=1;
                isFinish[position]=Constants.FINISHED;
            } else {
                drawable = FileBrowserActivity.fileTypeDrawable.get(2);
                holder.img.setImageDrawable(drawable);
                isFinish[position]=Constants.UNFINISHED;
            }
            mTypeOfPosition[position] = Constants.GRID_POSITION_IS_MUSIC;
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_DIR) {
            imagePosition2Task[position]=null;
            drawable = FileBrowserActivity.fileTypeDrawable.get(4);
            holder.img.setImageDrawable(drawable);
            isFinish[position]=Constants.FINISHED;
            mTypeOfPosition[position] = Constants.GRID_POSITION_IS_OTHERS;
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_RETURN) {
            imagePosition2Task[position]=null;
            drawable = FileBrowserActivity.fileTypeDrawable.get(5);
            holder.img.setImageDrawable(drawable);
            isFinish[position]=Constants.FINISHED;
            mTypeOfPosition[position] = Constants.GRID_POSITION_IS_OTHERS;
        }
        holder.title.setText((String) gridOnePage.get(position).getName());
        if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_VIDEO && 0==flag4HasCacheOrNot) {
            if (cancelPotentialLoad(url, position)) {
                Drawable tmpDrawable = hasDrawableInCache(url);
                if (tmpDrawable!=null) {
                    holder.img.setImageDrawable(tmpDrawable);
                } else {
                    try {
                        AsyncLoadImageTask task = new AsyncLoadImageTask(holder.img,Constants.GRID_POSITION_IS_VIDEO);
                        imagePosition2Task[position]=task;
                        ivHasCancelTask[position]=holder.img;
                        task.execute(position);
                    } catch (Exception e) {   Log.e("error", e.toString()); }
                }

            }

        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_SONG && 0==flag4HasCacheOrNot) {
            if (cancelPotentialLoad(url, position)) {
                Drawable tmpDrawable = hasDrawableInCache(url);
                if (tmpDrawable!=null) {
                    holder.img.setImageDrawable(tmpDrawable);
                } else {
                    try {
                        AsyncLoadImageTask task = new AsyncLoadImageTask(holder.img,Constants.GRID_POSITION_IS_MUSIC);
                        imagePosition2Task[position]=task;
                        task.execute(position);
                    } catch (Exception e) {   Log.e("error", e.toString());  }
                }
            }
        } else if (gridOnePage.get(position).getType()==Constants.FILE_TYPE_PICTURE && 0==flag4HasCacheOrNot) {
            String tmpPath = gridOnePage.get(position).getPath();
            File tmpFile = new File(tmpPath);
            long tmpLength = tmpFile.length();
            if (tmpLength<=maxPictureSize && cancelPotentialLoad(url, position) ) {
                Drawable tmpDrawable = hasDrawableInCache(url);
                if (tmpDrawable!=null) {
                    holder.img.setImageDrawable(tmpDrawable);
                } else {
                    try {
                         AsyncLoadImageTask task = new AsyncLoadImageTask(holder.img,Constants.GRID_POSITION_IS_PICTURE);
                         imagePosition2Task[position]=task;
                         task.execute(position);
                    } catch (Exception e) {   Log.e("error", e.toString());  }
                }
            }
        }
        return convertView;
    }
    public AsyncLoadImageTask createTaskObject(ImageView imageview,int fileType){
           AsyncLoadImageTask task = new AsyncLoadImageTask(imageview,fileType);
           return task;
    }
    public final class ViewHolder {
        public ImageView img;
        public TextView title;
        //public TextView info;
    }
    private Drawable hasDrawableInCache(String key){
        return mMemoryCache.get(key);
    }
    private Drawable getBitmapFromUrl(String url,int filetype){
        Bitmap bitmap = null;
        Bitmap bitmapResult = null;
        mtb= new MediaThumbnail();
        File file = new File(url);
        if (Constants.GRID_POSITION_IS_VIDEO == filetype) {
            bitmap = mtb.createVideoThumbnail(file.getAbsolutePath());
        } else if (Constants.GRID_POSITION_IS_MUSIC == filetype) {
            bitmap = mtb.createAlbumThumbnail(file.getAbsolutePath());
        } else if (Constants.GRID_POSITION_IS_PICTURE == filetype) {
            //bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            bitmap = decodeSampledBitmapFromFilePath(file.getAbsolutePath(), 380, 380) ;
        }
        bitmapResult=ThumbnailUtils.extractThumbnail(bitmap, 380, 380);
        if (bitmap != bitmapResult) {
            bitmap.recycle();
        }
        if (bitmapResult == null) return null;
        Drawable drawable=new BitmapDrawable(bitmapResult);
        return drawable;
    }
    public class AsyncLoadImageTask extends AsyncTask<Integer, Void, Drawable>{
        private String url = null;
        private final WeakReference<ImageView> imageViewReference;
        private ImageView iv;
        private int taskPos;
        // video is 0 and music is 1
        private int fileType;
        public AsyncLoadImageTask(ImageView imageview,int fileType) {
            super();
            // TODO Auto-generated constructor stub
            this.iv=imageview;
            this.fileType=fileType;
            imageViewReference = new WeakReference<ImageView>(imageview);
        }
        @Override
        protected Drawable doInBackground(Integer... params) {
            Log.i(TAG, "doInBackground");
            // TODO Auto-generated method stub
            Drawable drawable = null;
            if (isCancelled())return null;
            if (params[0]>=gridOnePage.size()) {
                this.cancel(true);
            } else {
                this.taskPos =   params[0];
                this.url = gridOnePage.get(params[0]).getPath();
                drawable = getBitmapFromUrl(this.url,this.fileType);
                if (drawable!=null) {
                    mMemoryCache.put(this.url,drawable);
                }
            }
            return drawable;
        }
        @Override
        protected void onPostExecute(Drawable resultDrawable) {
            if (isCancelled()) resultDrawable = null;
            if (imageViewReference != null) {
                ImageView imageview = imageViewReference.get();
                AsyncLoadImageTask loadImageTask =imagePosition2Task[this.taskPos] ;
                if (this == loadImageTask && resultDrawable!=null && iv!=null) {
                    iv.setImageDrawable(resultDrawable);
                }
            }
            isFinish[this.taskPos]=Constants.FINISHED;
            super.onPostExecute(resultDrawable);
        }
        protected void onPreExecute () {
            Log.i(TAG, "onPreExecute");
        }
    }
    private boolean cancelPotentialLoad(String url,int position){
        AsyncLoadImageTask loadImageTask = imagePosition2Task[position];

        if (loadImageTask != null) {
            String bitmapUrl = loadImageTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                loadImageTask.cancel(true);
            } else {
                // the same url has been loaded
                return false;
            }
        }
        return true;
    }
    public class GridViewOnHoverListener implements OnHoverListener {
        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            if (0==itemHeight) {
                itemHeight = v.getHeight();
            }
            if(0==itemWidth ){
                itemWidth = v.getWidth();
            }
            switch (what) {
            case MotionEvent.ACTION_HOVER_ENTER:
                 int x = (int) v.getX()/itemWidth;
                 int y = (int) v.getY() / itemHeight;
                 Bundle bundle = new Bundle();
                 int pos=y*Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM+x;
                 bundle.putInt(Constants.ADAPTER_POSITION, pos);
                 Message msg = new Message();
                 msg.what = Constants.UPDATE_LISTVIEW_FOCUS;
                 msg.setData(bundle);
                 handler.sendMessage(msg);
                 break;

            }
            return false;
        }
    }

    private static int calculateRatioSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while ((halfHeight/inSampleSize)>reqHeight && (halfWidth/inSampleSize)>reqWidth) {
                   inSampleSize *= 2;
            }

        }
        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromFilePath(String filePath,
            int reqWidth, int reqHeight) {
        FileInputStream fis =null;
        try {
            fis= new FileInputStream(filePath);
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
        }
        FileDescriptor fd =null;
        if (fis == null) return null;
        try {
            fd= fis.getFD();
        } catch(IOException e){
            e.printStackTrace();
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bMap = null;
        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fd,null,options);
            options.inSampleSize = calculateRatioSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            bMap = BitmapFactory.decodeFileDescriptor(fd,null,options);
        } catch (OutOfMemoryError e) {
            Log.i(TAG,"OutOfMemoryError");
            options.inSampleSize+=1;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            bMap = BitmapFactory.decodeFileDescriptor(fd,null,options);
        }
        return bMap;
        //return createScaleBitmap(bMap, reqWidth, reqHeight);
    }
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) {
            src.recycle();
        }
        return dst;
    }
}