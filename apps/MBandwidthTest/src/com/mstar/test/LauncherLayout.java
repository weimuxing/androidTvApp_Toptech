package com.mstar.test;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LauncherLayout extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener {

    private ScaleAnimEffect animEffect;
    private ImageView[] shadowBackgrounds = new ImageView[4];
    private FrameLayout[] frameLayoutViews = new FrameLayout[4];
    private ImageView[] imageViews = new ImageView[4];

    public LauncherLayout(Context paramContext, View mainView) {
        super(paramContext);
        this.animEffect = new ScaleAnimEffect();

        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.BOTTOM);
        setOrientation(LinearLayout.VERTICAL);
        addView(mainView);
        initView();
    }

    private void showLooseFocusAinimation(int paramInt) {
        this.animEffect.setAttributs(1.105F, 1.0F, 1.105F, 1.0F, 100L);
        this.imageViews[paramInt].startAnimation(this.animEffect.createAnimation());
        this.shadowBackgrounds[paramInt].setVisibility(View.GONE);
    }

    private void showOnFocusAnimation(final int paramInt) {
        this.frameLayoutViews[paramInt].bringToFront();
        this.animEffect.setAttributs(1.0F, 1.105F, 1.0F, 1.105F, 100L);
        Animation localAnimation = this.animEffect.createAnimation();
        localAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                shadowBackgrounds[paramInt].startAnimation(animEffect.alphaAnimation(0.0F, 1.0F, 150L, 0L));
                shadowBackgrounds[paramInt].setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }
        });
        this.imageViews[paramInt].startAnimation(localAnimation);
    }

    private OnClickListener onClickListener;

    public void destroy() {
    }

    public void initListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void initListener() {
    }

    public void initView() {
        int[] arrayOfInt = new int[7];
        arrayOfInt[0] = R.drawable.blue_no_shadow;
        arrayOfInt[1] = R.drawable.dark_no_shadow;
        arrayOfInt[2] = R.drawable.green_no_shadow;
        arrayOfInt[3] = R.drawable.orange_no_shadow;
        arrayOfInt[4] = R.drawable.pink_no_shadow;
        arrayOfInt[5] = R.drawable.red_no_shadow;
        arrayOfInt[6] = R.drawable.yellow_no_shadow;

        this.frameLayoutViews[0] = ((FrameLayout) findViewById(R.id.layout_item0));
        this.frameLayoutViews[1] = ((FrameLayout) findViewById(R.id.layout_item1));
        this.frameLayoutViews[2] = ((FrameLayout) findViewById(R.id.layout_item2));
        this.frameLayoutViews[3] = ((FrameLayout) findViewById(R.id.layout_item3));

        this.shadowBackgrounds[0] = ((ImageView) findViewById(R.id.item0_shadow));
        this.shadowBackgrounds[1] = ((ImageView) findViewById(R.id.item1_shadow));
        this.shadowBackgrounds[2] = ((ImageView) findViewById(R.id.item2_shadow));
        this.shadowBackgrounds[3] = ((ImageView) findViewById(R.id.item3_shadow));

        this.imageViews[0] = ((ImageView) findViewById(R.id.item0));
        this.imageViews[1] = ((ImageView) findViewById(R.id.item1));
        this.imageViews[2] = ((ImageView) findViewById(R.id.item2));
        this.imageViews[3] = ((ImageView) findViewById(R.id.item3));

        for (int i = 0; i < 4; i++) {
            this.shadowBackgrounds[i].setVisibility(View.GONE);
            imageViews[i].setOnFocusChangeListener(this);
            imageViews[i].setOnClickListener(this);
        }
    }

    public void onClick(View paramView) {
        if (onClickListener != null) {
            onClickListener.onClick(paramView);
        }
    }

    private OnFocusChangeListener onFocusChangeListener;

    public void initListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public void onFocusChange(View paramView, boolean paramBoolean) {
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(paramView, paramBoolean);
        }
        int i = -1;
        switch (paramView.getId()) {
        case R.id.item0:
            i = 0;
            break;
        case R.id.item1:
            i = 1;
            break;
        case R.id.item2:
            i = 2;
            break;
        case R.id.item3:
            i = 3;
            break;
        }
        if (paramBoolean) {
            showOnFocusAnimation(i);
        } else {
            showLooseFocusAinimation(i);
        }
    }

    public void updateData() {
    }
}