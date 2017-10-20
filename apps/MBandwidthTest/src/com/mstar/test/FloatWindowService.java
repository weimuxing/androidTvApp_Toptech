package com.mstar.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnShowListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class FloatWindowService extends Service {

    private static View floatMenuView;
    private WindowManager wm;
    private LayoutParams menuPanelParams;
    private LayoutParams launcherPanelParams;
    private DisplayMetrics dm;
    private ViewPager mPager;
    private RadioButton leftMenuItem[];
    private List<View> listViews;
    private MyPagerAdapter mpAdapter = null;
    private LayoutInflater mInflater;
    private LauncherLayout applicationView, tvView, settingView, myView;
    private Dialog connectingDialog;
    private Button btn_start, btn_test1, btn_test2, btn_test3, btn_finish, btn_startTV;
    private LinearLayout startPanel, testPanel;
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private static int count = 0;
    private ScaleAnimation scaleAnimation;
    private ImageView scaleImageView;

    private Runnable automator = new Runnable() {
        @Override
        public void run() {
            if (count>=4)
                count = 0;
            leftMenuItem[count++].setChecked(true);
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        wm = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);

        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        menuPanelParams = new LayoutParams();
        menuPanelParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        menuPanelParams.format = PixelFormat.RGBA_8888;
        menuPanelParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        menuPanelParams.width = LayoutParams.WRAP_CONTENT;
        menuPanelParams.height = LayoutParams.WRAP_CONTENT;
        menuPanelParams.gravity = Gravity.LEFT;
        menuPanelParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        launcherPanelParams = new LayoutParams();
        launcherPanelParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
        launcherPanelParams.format = PixelFormat.RGBA_8888;
        launcherPanelParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                | LayoutParams.FLAG_NOT_FOCUSABLE;
        launcherPanelParams.width = LayoutParams.WRAP_CONTENT;
        launcherPanelParams.height = LayoutParams.WRAP_CONTENT;
        launcherPanelParams.gravity = Gravity.RIGHT;
        launcherPanelParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

        mInflater = LayoutInflater.from(this);
        floatMenuView = mInflater.inflate(R.layout.floatmenu, null);

        connectingDialog = createConnectingDialog();

        wm.addView(floatMenuView, menuPanelParams);
        initView();
    }

    private Bitmap getBitmapFromAssets(String file) {
        try {
            AssetManager am = getAssets();
            InputStream is = am.open(file);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initView() {
        listViews = new ArrayList<View>();
        mpAdapter = new MyPagerAdapter(listViews);

        scaleImageView = (ImageView)floatMenuView.findViewById(R.id.scaleImageView);
        scaleImageView.setImageBitmap(getBitmapFromAssets("image/1080p.jpg"));
        scaleAnimation = new ScaleAnimation(0.2f, 1f, 0.2f, 1f,
                     Animation.RELATIVE_TO_SELF, 0.5f,
                     Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(5000);
        scaleAnimation.setRepeatCount(-1);

        tvView = new LauncherLayout(this.getBaseContext(), mInflater.inflate(R.layout.layout_tv, null));
        listViews.add(tvView);

        applicationView = new LauncherLayout(this.getBaseContext(), mInflater.inflate(R.layout.layout_application, null));
        listViews.add(applicationView);

        myView = new LauncherLayout(this.getBaseContext(), mInflater.inflate(R.layout.layout_my, null));
        listViews.add(myView);

        settingView = new LauncherLayout(this.getBaseContext(), mInflater.inflate(R.layout.layout_setting, null));
        listViews.add(settingView);

        startPanel = (LinearLayout) floatMenuView.findViewById(R.id.start_panel);
        testPanel = (LinearLayout) floatMenuView.findViewById(R.id.test_panel);

        mPager = (ViewPager) floatMenuView.findViewById(R.id.apps_panel);
        mPager.setOffscreenPageLimit(0);
        mPager.setAdapter(mpAdapter);
        mPager.setCurrentItem(0);

        leftMenuItem = new RadioButton[4];
        leftMenuItem[0] = (RadioButton)floatMenuView.findViewById(R.id.menu_tv);
        leftMenuItem[1] = (RadioButton)floatMenuView.findViewById(R.id.menu_application);
        leftMenuItem[2] = (RadioButton)floatMenuView.findViewById(R.id.menu_my);
        leftMenuItem[3] = (RadioButton)floatMenuView.findViewById(R.id.menu_setting);

        btn_startTV = (Button)floatMenuView.findViewById(R.id.btn_start_tv);
        btn_test1 = (Button)floatMenuView.findViewById(R.id.btn_test1);
        btn_test2 = (Button)floatMenuView.findViewById(R.id.btn_test2);
        btn_test3 = (Button)floatMenuView.findViewById(R.id.btn_test3);
        btn_finish = (Button)floatMenuView.findViewById(R.id.btn_finish);

        btn_startTV.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                startTVPlayer();
            }
        });

        btn_test1.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    isRunning = true;
                    automator.run();
                }
                else {
                    isRunning = false;
                    handler.removeCallbacks(automator);
                }
            }
        });

        btn_test2.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                connectingDialog.show();
            }
        });

        btn_test3.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if (scaleImageView.getVisibility() == View.GONE) {
                    scaleImageView.setVisibility(View.VISIBLE);
                    scaleImageView.setAnimation(scaleAnimation);
                    scaleAnimation.startNow();
                }
                else {
                    scaleAnimation.cancel();
                    scaleImageView.setAnimation(null);
                    scaleImageView.setVisibility(View.GONE);
                }
            }
        });

        btn_finish.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        leftMenuItem[0].setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    mPager.setCurrentItem(0);
                    tvView.findViewById(R.id.item0).requestFocus();
                }
            }
        });
        leftMenuItem[1].setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    mPager.setCurrentItem(1);
                    applicationView.findViewById(R.id.item0).requestFocus();
                }
            }
        });
        leftMenuItem[2].setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    mPager.setCurrentItem(2);
                    myView.findViewById(R.id.item0).requestFocus();
                }
            }
        });
        leftMenuItem[3].setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                if (isChecked) {
                    mPager.setCurrentItem(3);
                    settingView.findViewById(R.id.item0).requestFocus();
                }
            }
        });

        btn_start = (Button) floatMenuView.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                startPanel.setVisibility(View.GONE);
                testPanel.setVisibility(View.VISIBLE);
            }
        });
    }

    public void startTVPlayer() {
        Intent intent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("isPowerOn", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        final PackageManager packageManager = getApplicationContext().getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            startActivity(intent);
        }
    }

    public class MyPagerAdapter extends PagerAdapter {
            public List<View> mListViews;

            public MyPagerAdapter(List<View> mListViews) {
                    this.mListViews = mListViews;
            }

            @Override
            public void destroyItem(View arg0, int arg1, Object arg2) {
                    ((ViewPager) arg0).removeView(mListViews.get(arg1));
            }

            @Override
            public void finishUpdate(View arg0) {
            }

            @Override
            public int getCount() {
                    return mListViews.size();
            }

            @Override
            public Object instantiateItem(View arg0, int arg1) {
                    ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
                    return mListViews.get(arg1);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == (arg1);
            }

            @Override
            public void restoreState(Parcelable arg0, ClassLoader arg1) {
            }

            @Override
            public Parcelable saveState() {
                    return null;
            }

            @Override
            public void startUpdate(View arg0) {
            }
    }

    protected Dialog createConnectingDialog() {
        final AnimationDrawable connectingAnimation = (AnimationDrawable)getResources().getDrawable(R.drawable.connecting);
        connectingAnimation.setOneShot(false);

        Dialog dialog =  new Dialog(getApplicationContext(), R.style.transparentDialog);
        dialog.setContentView(R.layout.connecting_dialog);
        dialog.setCancelable(true);
        dialog.setOnShowListener(new OnShowListener(){

            @Override
            public void onShow(DialogInterface arg0) {
                connectingAnimation.start();
            }

        });
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        ImageView connectingImageView = (ImageView) dialog.findViewById(R.id.imgViewConnecting);
        connectingImageView.setImageDrawable(connectingAnimation);
        return dialog;
    }

    @Override
    public void onDestroy() {
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (floatMenuView != null) {
            wm.removeView(floatMenuView);
        }
    }
}