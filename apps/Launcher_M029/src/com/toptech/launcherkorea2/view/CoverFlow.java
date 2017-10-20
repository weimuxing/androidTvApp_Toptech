package com.toptech.launcherkorea2.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 重写gallery实现快捷方式中间选中放大效果
 * @author calvin
 * @date 2013-1-10
 */
public class CoverFlow extends Gallery {

	private final static String tag = "CoverFlow";
	private Camera mCamera = new Camera();
	private int mMaxRotationAngle = 0;// 50;//60;
	private int mMinRotationAngle = 0;
	private int mMaxZoom = -360;// -380;//-120;
	private int mCoveflowCenter;
	private boolean mAlphaMode = true;
	private boolean mCircleMode = false;
	
	public ImageView currentView;
	public ImageView rightSideView;
	public ImageView leftSideView;
	
	private AnimationSet manimationSet;

	public CoverFlow(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStaticTransformationsEnabled(true);
	}

	public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStaticTransformationsEnabled(true);
	}

	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	public boolean getCircleMode() {
		return mCircleMode;
	}

	public void setCircleMode(boolean isCircle) {
		mCircleMode = isCircle;
	}

	public boolean getAlphaMode() {
		return mAlphaMode;
	}

	public void setAlphaMode(boolean isAlpha) {
		mAlphaMode = isAlpha;
	}

	public int getMaxZoom() {
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}
	
	private int getRightRangOfView(View view){
		return  getWidth() - getPaddingLeft() - view.getWidth() / 2 - 40;
	}
	
	private int getLeftRangOfView(View view){
		return view.getWidth() / 2 + getPaddingLeft() + 40;
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}
	
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		
		final int childCenter = getCenterOfView(child);
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);
		
		mCamera.save();
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		
		if (childCenter == mCoveflowCenter) {
			
			//--------------------------------------------------------
			/*
			AnimationSet animationSet = new AnimationSet(true);
			
			if (manimationSet != null && manimationSet != animationSet) {
				ScaleAnimation scaleAnimation = new ScaleAnimation(2,
						0.5f, 2, 0.5f, Animation.RELATIVE_TO_SELF,
						0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(1000);
				manimationSet.addAnimation(scaleAnimation);
				manimationSet.setFillAfter(true);
				child.startAnimation(manimationSet);
			}
			
			ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1.5f,
					1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			scaleAnimation.setDuration(1000);
			animationSet.addAnimation(scaleAnimation);
			animationSet.setFillAfter(true);
			child.startAnimation(animationSet);
			manimationSet = animationSet;
			*/
			//---------------------------------------------------------
			mCamera.translate(0.0f, 0.0f, -320.0f);
			
		}else{
			mCamera.translate(0.0f, 0.0f, -180.0f);
		}
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		mCamera.restore();
		
		return true;
	}


	/**
	 * 这就是所谓的在大小的布局时,这一观点已经发生了改变。如果 你只是添加到视图层次,有人叫你旧的观念 价值观为0。
	 * 
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * @param oldw
	 *            Old width of this view.
	 * @param oldh
	 *            Old height of this view.
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/**
	 * 把图像位图的角度通过
	 * 
	 * @param imageView
	 *            ImageView the ImageView whose bitmap we want to rotate
	 * @param t
	 *            transformation
	 * @param rotationAngle
	 *            the Angle by which to rotate the Bitmap
	 */
	
	private void transformImageBitmap(ImageView child, Transformation t,int rotationAngle) {
		mCamera.save();
		
		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getLayoutParams().height;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);
		
		if(rotation > 0)
		mCamera.translate(0.0f, 0.0f, -200.0f);

		
		// 如视图的角度更少,放大
		if (rotation <= mMaxRotationAngle) {
			Log.d(tag, "++++++++++++++++++++++++++++=");
			float zoomAmount = (float) (mMaxZoom + (rotation * 1.1));
			mCamera.translate(0.0f, 0.0f, zoomAmount);
			if (mCircleMode) {
				if (rotation < 40)
					mCamera.translate(0.0f, -155, 0.0f);
				else
					mCamera.translate(0.0f, -(255 - rotation * 2.5f), 0.0f);
			}
			if (mAlphaMode) {
				if(rotation >= mMaxRotationAngle)
					((ImageView) (child)).setAlpha((int) (255 - mMaxRotationAngle * 2.2));
				else
					((ImageView) (child)).setAlpha((int) (255 - rotation * 2.5)); 
			}
		}
		
		
		mCamera.rotateY(rotationAngle);
		mCamera.getMatrix(imageMatrix);
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		
		mCamera.restore();
	}
	


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{   
		if(distanceX>550)distanceX=550;
		if(distanceX<-550)distanceX=-550;
		//distanceX = distanceX>600 ? 600 : distanceX;
		return super.onScroll(e1, e2, distanceX, distanceY);
		//return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		// TODO Auto-generated method stub
		//return super.onFling(e1, e2, velocityX, velocityY);
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		Log.d(tag, "onTouchEvent......");
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		Log.d(tag, "dispatchKeyEvent......");
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		Log.d(tag, "onSingleTapUp......");
		return super.onSingleTapUp(e);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		Log.d(tag, "onDown......");
		return super.onDown(e);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_ENTER:
				Log.d(tag, "ENTER......");
				return false;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	
}