package com.toptech.launcherkorea2.adapter;

import com.toptech.launcherkorea2.Launcher;
import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.utils.Funs;
import com.toptech.launcherkorea2.utils.Utils;
import com.toptech.launcherkorea2.view.CoverFlow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 主菜单适配器
 * @author calvin
 * @date 2013-1-10
 */
public class ImageAdapter extends BaseAdapter{
	
	private Context mContext;
	private Integer[] mImageIds;
	private int selectItem;
	private TypedArray names = null;
	private AnimationSet manimationSet;
	private AlphaAnimation alphAnim;
	
	private LayoutInflater inflater;
	private int mResource;
	private String[] viewKey;
	private int[] viewId;
	private ViewHolder viewHolder;
	private int focusPos=-1;
	private int mWidth,mHeight;
	private class ViewHolder {
		public ImageView icon;
		public TextView name;
	}
	private TextView mTextView;
	public ImageAdapter(Context context,int resource, 
			String[] from, int[] to,Integer[] mImageIds,TypedArray names,TextView textview) {
		mContext = context;
		mResource = resource;
		mTextView = textview;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewKey = new String[from.length];
		viewId = new int[to.length];
		//从from对象的0位置开始拷贝数据到viewKey,并从viewKey的0位置放入，一直到from的from.length长度
		System.arraycopy(from, 0, viewKey, 0, from.length);
		System.arraycopy(to, 0, viewId, 0, to.length);
		
		this.mImageIds = mImageIds;
		this.names = names;
		mWidth = context.getResources().getDimensionPixelSize(R.dimen.x140);
		mHeight = context.getResources().getDimensionPixelSize(R.dimen.y80);
		//names = mContext.getResources().obtainTypedArray(R.array.appnames);
		//alphAnim = new AlphaAnimation(0.1f,1.0f);
		//alphAnim.setDuration(3000);
	}
	
	public void setImageIds(Integer[] mImageIds){
		this.mImageIds = mImageIds;
	}
	
	public void setNames(TypedArray names){
		this.names = names;
	}
	
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
    
	public int getIdLength(){
		return mImageIds.length;
	}
	
	public void setSelectItem(int selectItem) {  
        if (this.selectItem != selectItem) {                  
            this.selectItem = selectItem;  
        	notifyDataSetChanged();                 
        } 
    }  
    
	private int mCount;
	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {//如果为空重新导入ListView的布局文件，布局文件自定义
			convertView = inflater.inflate(resource, null);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(viewId[0]);
			viewHolder.name = (TextView) convertView.findViewById(viewId[1]);
			convertView.setTag(viewHolder);
		}
		
		Bitmap bitmap = createReflectedImages(mContext,mImageIds[position%mImageIds.length]);
		viewHolder.icon.setImageBitmap(bitmap);
		
		if(selectItem == position){ 
			viewHolder.name.setText(names.getString(position%mImageIds.length));
		}
		return convertView;
		*/
		
		ImageView i = null;
		//if(selectItem == position){ 
		if(focusPos==position){
			i = Utils.createReflectedImages(mContext,Launcher.mImageIds[position%mImageIds.length],names.getString(position%mImageIds.length));
		//	mTextView.setText(names.getString(position%mImageIds.length));
			
			i.setLayoutParams(new CoverFlow.LayoutParams(mWidth,mHeight));//skye 2015
			i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			i.setPadding(0, 0, 0, 0);
			Log.d("zsr", "position, convertView, parent: " + mContext);
		}else{
			i = Utils.createReflectedImages(mContext,mImageIds[position%mImageIds.length],null/*names.getString(position%mImageIds.length)*/);
			
			//i.setLayoutParams(new CoverFlow.LayoutParams(200, 150));
		//}else{
			//i = createReflectedImages(mContext,mImageIds[position%mImageIds.length],null);
			
		//}
			
			mCount++;
			
			
		i.setLayoutParams(new CoverFlow.LayoutParams(mWidth,mHeight));//skye 2015
		i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		i.setPadding(0, 12, 0, 0);
		//i.setBackgroundColor(Color.BLUE);
		}
		// 设置的抗锯齿
		BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
		drawable.setAntiAlias(true);
		//i.setOnHoverListener(new AppOnHoverListener(parent,position));
		if(i != null){
			i.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					Log.d("zsr", "v, hasFocus: " + hasFocus);
				}
			});
		}
		
		return i;
		
		
	}
	
	
	
	private class AppOnHoverListener implements OnHoverListener{
		private ViewGroup parent;
		private int pos;
		public AppOnHoverListener(ViewGroup parent,int pos){
			this.parent = parent;
			this.pos = pos;
		}
		@Override
		public boolean onHover(View v, MotionEvent event) {
			//AnimationSet animationSet = new AnimationSet(true);
			int what = event.getAction();
			switch (what) {
				case MotionEvent.ACTION_HOVER_ENTER: // 鼠标进入view
					View selectView = parent.getChildAt(selectItem);
					if(selectView != null){
						selectView.startAnimation(alphAnim);
						
					}
					Log.d("zsr", "v, event: " );
					/*
					if(pos != selectItem){
						View selectView = parent.getChildAt(selectItem);
						if(selectView != null){
							ScaleAnimation scalesmall = new ScaleAnimation(1, 1f,
									1, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
									Animation.RELATIVE_TO_SELF, 0.5f);
							scalesmall.setDuration(1000);
							animationSet.addAnimation(scalesmall);
							animationSet.setFillAfter(true);
							selectView.startAnimation(animationSet);
							manimationSet = animationSet;
						}
						
						ScaleAnimation scalelarge = new ScaleAnimation(1, 1.1f,
								1, 1.1f, Animation.RELATIVE_TO_SELF, 0.9f,
								Animation.RELATIVE_TO_SELF, 0.9f);
						scalelarge.setDuration(1000);
						animationSet.addAnimation(scalelarge);
						animationSet.setFillAfter(true);
						v.startAnimation(animationSet);
						manimationSet = animationSet;
					}
					*/
				break;
				case MotionEvent.ACTION_HOVER_EXIT: // 鼠标离开view
					
					/*
					if(pos == selectItem){
						//do nothing
					}else{
						ScaleAnimation scalesmall = new ScaleAnimation(1, 1f,
								1, 1f, Animation.RELATIVE_TO_SELF, 0.9f,
								Animation.RELATIVE_TO_SELF, 0.9f);
						scalesmall.setDuration(1000);
						animationSet.addAnimation(scalesmall);
						animationSet.setFillAfter(true);
						v.startAnimation(animationSet);
						manimationSet = animationSet;
					}
					*/
				break;
			}
			return false;
		}
		
	}
	

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
	
	public void setCurFocus(int position)
	{
		focusPos=position;
	}
}

