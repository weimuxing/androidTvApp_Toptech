package com.mstar.test;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;


public class ScaleAnimEffect {
    private long duration;
    private float fromXScale;
    private float fromYScale;
    private float toXScale;
    private float toYScale;

    public Animation alphaAnimation(float fromAlpha, float toAlpha, long duration, long startOffset) {
        AlphaAnimation localAlphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        localAlphaAnimation.setDuration(duration);
        localAlphaAnimation.setStartOffset(startOffset);
        localAlphaAnimation.setInterpolator(new AccelerateInterpolator());
        return localAlphaAnimation;
    }

    public Animation createAnimation() {
        ScaleAnimation localScaleAnimation = new ScaleAnimation(this.fromXScale, this.toXScale, this.fromYScale, this.toYScale, 1, 0.5F, 1, 0.5F);
        localScaleAnimation.setFillAfter(true);
        localScaleAnimation.setInterpolator(new AccelerateInterpolator());
        localScaleAnimation.setDuration(this.duration);
        return localScaleAnimation;
    }

    public void setAttributs(float fromXScale, float toXScale, float fromYScale, float toYScale, long duration) {
        this.fromXScale = fromXScale;
        this.fromYScale = fromYScale;
        this.toXScale = toXScale;
        this.toYScale = toYScale;
        this.duration = duration;
    }
}