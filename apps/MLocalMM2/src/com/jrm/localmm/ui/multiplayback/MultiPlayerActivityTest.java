package com.jrm.localmm.ui.multiplayback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.jrm.localmm.R;
import com.jrm.localmm.ui.video.VideoPlayerActivity;
import com.jrm.localmm.util.Constants;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.jrm.localmm.util.Tools;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.content.SharedPreferences.Editor;
import com.jrm.localmm.business.video.MultiVideoPlayView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.net.Uri;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;


import com.mstar.android.media.MMediaPlayer;
import com.mstar.android.media.VideoCodecInfo;
import android.media.Metadata;
import android.os.SystemProperties;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * Mutil video play main activity
 * @author jason
 */
public class MultiPlayerActivityTest extends Activity{
    private static final String TAG0 = "MultiPlayerActivityTest";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        Log.i(TAG0,"SubtitleSurfaceView 1111");
        SubtitleSurfaceView mSubtitleSurfaceView = new SubtitleSurfaceView(this);
        decor.addView(mSubtitleSurfaceView,COVER_SCREEN_PARAMS);
        Log.i(TAG0,"SubtitleSurfaceView 2222");
    }

    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
        new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);

    public class SubtitleSurfaceView extends SurfaceView {

        private static final String TAG = "SubtitleSurfaceView";
        private SurfaceHolder mSurfaceHolder = null;
        private Context mContext = null;
        public SubtitleSurfaceView(Context context) {
            super(context);
            mContext = context;
            initVideoView();
        }

        public SubtitleSurfaceView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
            mContext = context;
            initVideoView();
        }

        public SubtitleSurfaceView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            mContext = context;
            initVideoView();
        }

        private void initVideoView() {
            getHolder().addCallback(mSHCallback);
            //getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format, int w,int h) {
                mSurfaceHolder = holder;
                Log.i(TAG, "SubtitleSurfaceView surfaceChanged:" + w + "," + h);
            }

            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "SubtitleSurfaceView surfaceCreated");
                mSurfaceHolder = holder;
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                mSurfaceHolder = null;
                Log.i(TAG, "SubtitleSurfaceView surfaceDestroyed");
            }
        };

    }

}
