package com.mstar.miscsetting.hoilder;

import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MenuViewHolder {

    public static RelativeLayout menuBgRelativeLayout;

    public static ImageView menuFoucsImage;
    public static ImageView upImage;
    public static ImageView rightImage;
    public static ImageView downImage;
    public static ImageView leftImage;

    public static TextView upText;
    public static TextView rightText;
    public static TextView downText;
    public static TextView leftText;

    public static Animation bigToSmall     = null;
    public static Animation fadeIn         = null;
    public static Animation fadeOut        = null;
    public static Animation currentToUp    = null;
    public static Animation currentToRight = null;
    public static Animation currentToDown  = null;
    public static Animation currentToLeft  = null;
    public static Animation rotateFadeIn   = null;
    public static LayoutAnimationController layoutFadeIn  = null;
    public static LayoutAnimationController layoutFadeOut = null;

    public static GridView menu2GridView;
}
