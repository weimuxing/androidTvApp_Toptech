package com.toptech.launcherkorea2.utils;

import java.io.InputStream;

import com.toptech.launcherkorea2.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.widget.ImageView;

/**
 * @author calvin
 * @date 2013-1-10
 */
public class Utils {
	

	/* public static Drawable getIcon(Context context,int code,int ap){
		 Drawable drawable = null;
		 switch(code){
			 case 0:
			 case 1:
			 case 2:
			 case 3:
			 case 4:
				 drawable = context.getResources().getDrawable(R.drawable.ic_0d);
				 break;
			 case 5:
				 drawable = context.getResources().getDrawable(R.drawable.ic_5d);
				 break;
			 case 6:
			 case 7:
				 drawable = context.getResources().getDrawable(R.drawable.ic_6d);
				 break;
			 case 8:
			 case 9:
				 drawable = context.getResources().getDrawable(R.drawable.ic_8d);
				 break;
			 case 10:
				 drawable = context.getResources().getDrawable(R.drawable.ic_10d);
				 break;
			 case 11:
			 case 12:
			 case 13:
				 drawable = context.getResources().getDrawable(R.drawable.ic_13d);
				 break;
			 case 14:
				 drawable = context.getResources().getDrawable(R.drawable.ic_14d);
				 break;
			 case 15:
				 drawable = context.getResources().getDrawable(R.drawable.ic_15d);
				 break;
			 case 16:
			 case 17:
				 drawable = context.getResources().getDrawable(R.drawable.ic_17d);
				 break;
			 case 18:
			 case 19:
				 if(ap == 0){
					 drawable = context.getResources().getDrawable(R.drawable.ic_19d);
				 }else{
					 drawable = context.getResources().getDrawable(R.drawable.ic_19n);
				 }
				 break;
			 case 20:
				 drawable = context.getResources().getDrawable(R.drawable.ic_20d);
				 break;
			 case 21:
			 case 22:
			 case 23:
				 drawable = context.getResources().getDrawable(R.drawable.ic_23d);
				 break;
			 case 24:
			 case 25:
			 case 26:
				 drawable = context.getResources().getDrawable(R.drawable.ic_26d);
				 break;
			 case 27:
				 if(ap == 0){
					 drawable = context.getResources().getDrawable(R.drawable.ic_27d);
				 }else{
					 drawable = context.getResources().getDrawable(R.drawable.ic_27n);
				 }
				 break;
			 case 28:
			 case 29:
			 case 30:
			 case 31:
				 if(ap == 0){
					 drawable = context.getResources().getDrawable(R.drawable.ic_31d);
				 }else{
					 drawable = context.getResources().getDrawable(R.drawable.ic_31n);
				 }
				 break;
			 case 32:
			 case 33:
				 if(ap == 0){
					 drawable = context.getResources().getDrawable(R.drawable.ic_33d); 
				 }else{
					 drawable = context.getResources().getDrawable(R.drawable.ic_33n);
				 }
				 break;
			 case 34:
			 case 35:
			 case 36:
			 case 37:
				 if(ap == 0){
					 drawable = context.getResources().getDrawable(R.drawable.ic_37d);
				 }else{
					 drawable = context.getResources().getDrawable(R.drawable.ic_37n);
				 }
				 break;
			 case 38:
			 case 39:
				 if(ap == 0){
					 drawable = context.getResources().getDrawable(R.drawable.ic_39d);
				 }else{
					 drawable = context.getResources().getDrawable(R.drawable.ic_39n);
				 }
				 break;
			 case 40:
			 case 41:
			 case 42:
			 case 43:
			 case 44:
			 case 45:
			 case 46:
			 case 47:
			 case 3200:
				 drawable = context.getResources().getDrawable(R.drawable.ic_3200d);
				 break;
			 default:
				 drawable = context.getResources().getDrawable(R.drawable.ic_3200d);
				
		 }
		 return drawable;
	 }*/
	 
	/**
	 * @param x
	 * @return
	 */
	/*public static String format(Context context, int x) {
		TypedArray months = context.getResources().obtainTypedArray(R.array.months_of_year);
		String s = "";
		s = months.getString(x-1);
		
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		
		return s;
	}*/
	

	/*public static String getFormDay(Context context, String day){
		TypedArray weeks = context.getResources().obtainTypedArray(R.array.days_of_weeks);
		if(day.equals("Sun")){
			
			return weeks.getString(0);
		}else if(day.equals("Mon")){
			
			return weeks.getString(1);
		}else if(day.equals("Tue")){
			
			return weeks.getString(2);
		}else if(day.equals("Wed")){
			
			return weeks.getString(3);
		}else if(day.equals("Thu")){
			
			return weeks.getString(4);
		}else if(day.equals("Fri")){
			
			return weeks.getString(5);
		}else if(day.equals("Sat")){
			
			return weeks.getString(6);
		}
		return "";	
	}
	
	public static String getWeatherCondition(Context context,int code)
	{
		TypedArray weatherCondition=context.getResources().obtainTypedArray(R.array.weather_condition);
		if(code<48)
			return weatherCondition.getString(code);
		
		return "" ;
	}*/
	
	/**
	 * @return
	 */
	/*public static boolean is24Hour(Context context) {
        return DateFormat.is24HourFormat(context);
    }
	*/

    public static Bitmap GeneratorBitmap(Context context,Bitmap icon){
    	
    	int w = icon.getWidth();
    	int h = icon.getHeight();

    	//int iconSize=(int)context.getResources().getDimension(android.R.dimen.app_icon_size);
    	Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
    	Canvas canvas=new Canvas(bitmap);
    	

    	Paint iconPaint=new Paint();
    	iconPaint.setDither(true);//防抖动
    	iconPaint.setFilterBitmap(true);//用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
    	Rect src=new Rect(0, 0, w, h);
    	Rect dst=new Rect(0, 0, w, h);
    	canvas.drawBitmap(icon, src, dst, iconPaint);
    	
    	/*
    	//提示圈边框
    	Paint paintL= new Paint();
    	paintL.setAntiAlias(true);
    	paintL.setColor(Color.WHITE); 
    	canvas.drawCircle(w-12,10,8, paintL);
    	
    	
    	//提示圈背景
    	Paint paintS= new Paint();
    	paintS.setAntiAlias(true);
    	paintS.setColor(Color.RED); 
    	canvas.drawCircle(w-12,10,6, paintS);
    	
    	
    	//启用抗锯齿和使用设备的文本字距
    	Paint countPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
    	countPaint.setColor(Color.WHITE);
    	countPaint.setTextSize(9);
    	countPaint.setTextAlign(Align.CENTER);
    	countPaint.setTypeface(Typeface.DEFAULT_BOLD);
    	canvas.drawText(String.valueOf(count),w-12,12, countPaint); 
    	*/
    	
    	Drawable drawable = context.getResources().getDrawable(R.drawable.red_close);
    	BitmapDrawable bd = (BitmapDrawable)drawable;
    	Bitmap bm = bd.getBitmap();
    	canvas.drawBitmap(bm, w-25, -8, null);
    	
    	return bitmap;
    }
    
    /**
     * 根据id获取一个图片
     * @param res
     * @param resId
     * @return
     */
    public static Bitmap getResIcon(Context context,int resId){
        //得到Resources对象  
        Resources r = context.getResources(); 
        //以数据流的方式读取资源  
        InputStream is = r.openRawResource(resId);  
        BitmapDrawable  bmpDraw = new BitmapDrawable(is);  
        Bitmap bmp = bmpDraw.getBitmap();  
    	return bmp;
    }
    
    /**
	 * 判断网络状态是否可用
	 * @return true:网络可用; false:网络不可用
	 */
	public static boolean isConnectInternet(Context mContext) {
		ConnectivityManager conManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}
	
	/**
	 * 图片实现倒影效果
	 * @param mContext 上下文
	 * @param imageId  图片资源id
	 * @param name  图片下方文字
	 * @return 返回一个包含改图片的ImageView组件
	 */
	public static ImageView createReflectedImages(Context mContext,int imageId,String name) {
		
		Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), imageId);
		
		final int reflectionGap = 0 ;
		
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		//创建倒影图像
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);
		
		//创建（原图+倒影）图像
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);
		//创建（原图+倒影）画布
		Canvas canvas = new Canvas(bitmapWithReflection);
		//在画布上画原图片
		canvas.drawBitmap(originalImage, 0, 0, null);
		//在画布上画倒影图像
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		
		//----------------------在倒影上画一模糊层-------------------
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage
				.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.MIRROR);

		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		//-----------------------------------------------------------------
		  /* skye heap */
		            originalImage.recycle();
		            reflectionImage.recycle();
		//---------------------------------------------
		//remove by zsr 2017/5/18,because it cannnot control the size 
		if(name != null){
			Paint deafaultPaint = new Paint();
			Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
			deafaultPaint.setTypeface(font);
			deafaultPaint.setColor(Color.WHITE);
			deafaultPaint.setTextAlign(Paint.Align.CENTER);
			deafaultPaint.setTextSize(mContext.getResources().getDimensionPixelOffset(R.dimen.x14));
			deafaultPaint.setAntiAlias(true);
			canvas.drawText(name, width/2, height + reflectionGap+height/4, deafaultPaint);
		}
		
		//--------------------------------------------
		
		
		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(bitmapWithReflection);

		return imageView;
		
		//return bitmapWithReflection;
		
	}

	
}
