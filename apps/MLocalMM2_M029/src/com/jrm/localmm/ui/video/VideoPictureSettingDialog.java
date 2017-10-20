package com.jrm.localmm.ui.video;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mstar.android.tv.TvPictureManager;
import com.jrm.localmm.R;

public class VideoPictureSettingDialog extends Dialog {
	private static final String TAG = "VideoPictureSettingDialog";
	private boolean debug=true;
	private VideoPlayerActivity mContext;
	private VideoPlaySettingDialog mVideoPlaySettingDialog;
	private MyAdapter mAdapter;
	private ListView mVideoPictureModeSettingList;
	private SeekBar[] mBar;
	private int[] PicModNum =new int[6] ;
	public static int[] pictureModeSettingName = {R.string.picture_mode_brightness, R.string.picture_mode_contrast, 
		R.string.picture_mode_color, R.string.picture_mode_hue,  R.string.picture_mode_sharpness,
		R.string.picture_mode_backlight };

	public VideoPictureSettingDialog(VideoPlayerActivity context, VideoPlaySettingDialog videoPlaySettingDialog) {
		super(context, R.style.dialog);
		mContext = context;
		mVideoPlaySettingDialog = videoPlaySettingDialog;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.dismiss();
		mVideoPlaySettingDialog.show();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.video_picture_setting_dialog);
		Window w = getWindow();
		Display display = w.getWindowManager().getDefaultDisplay();
		w.setTitle(null);
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Log.i(TAG, "metrics.density:" + metrics.density);
		int width = display.getWidth();
		int height = display.getHeight();
		if (metrics.density == 1.5) {
			width = (int) (display.getWidth() * 0.20);
			height = (int) (display.getHeight() * 0.30);
		} else if (metrics.density == 2.0) {
			width = (int) (display.getWidth() * 0.56);
			height = (int) (display.getHeight() * 0.55);
		}
		w.setLayout(width, height);
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams wl = w.getAttributes();
		w.setAttributes(wl);
		w.setBackgroundDrawableResource(android.R.color.transparent);
		initView();
		getPicModNum();
			}

	private void initView() {
		mBar = new SeekBar[6];
		mVideoPictureModeSettingList = (ListView) findViewById(R.id.videoPictureModeList);
		mVideoPictureModeSettingList.setMinimumHeight(300);
		mAdapter = new MyAdapter(mContext, pictureModeSettingName, this);
		mVideoPictureModeSettingList.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			event.startTracking();
			Log.d("sch", "onKeyDown KEYCODE_DPAD_LEFT");
			View view = mVideoPictureModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (TvPictureManager.getInstance().getPictureModeIdx().ordinal() != 3||holder.pos==3){
				if(debug)Log.d(TAG, "DPAD_LEFT  is'not pic Normal");
				return true; 
				}
			if (holder.leftImageView.isShown()) {
				PicModNum[holder.pos]--;
				if(PicModNum[holder.pos]<0)
					PicModNum[holder.pos] = 0;
				holder.showproBar.setProgress(PicModNum[holder.pos]);
				holder.settingOptionTextView.setText(Integer.toString(PicModNum[holder.pos]));
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			event.startTracking();
			View view = mVideoPictureModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (TvPictureManager.getInstance().getPictureModeIdx().ordinal() != 3||holder.pos==3){
				if(debug)Log.d(TAG, "DPAD_RIGHT  is'not pic Normal");
				return true; 
				}
			
			if (holder.leftImageView.isShown()) {
				PicModNum[holder.pos]++;
				if(PicModNum[holder.pos]>100)
					PicModNum[holder.pos] = 100;
				holder.showproBar.setProgress(PicModNum[holder.pos]);
				holder.settingOptionTextView.setText(Integer.toString(PicModNum[holder.pos]));
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			View view = mVideoPictureModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (TvPictureManager.getInstance().getPictureModeIdx().ordinal() != 3||holder.pos==3){
			if(debug)Log.d(TAG, "DPAD_LEFT UP  is'not pic Normal");
				return true; 
				}
			
			if (event.isTracking() && !event.isCanceled()) {
				setPicModChange(holder.pos, PicModNum[holder.pos]);
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			View view = mVideoPictureModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (TvPictureManager.getInstance().getPictureModeIdx().ordinal() != 3||holder.pos==3){
				if(debug)Log.d(TAG, "DPAD_RIGHT UP  is'not pic Normal");
				return true; 
				}
			if(debug)Log.d("sch", "onKeyUp KEYCODE_DPAD_RIGHT");
			
			if (event.isTracking() && !event.isCanceled()) {
				setPicModChange(holder.pos, PicModNum[holder.pos]);
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void setPicModChange(int pos, int m) {
		switch (pos) {
		case 0:
			TvPictureManager.getInstance().setVideoItem(TvPictureManager.PICTURE_BRIGHTNESS, PicModNum[0]);
			break;
		case 1:
			TvPictureManager.getInstance().setVideoItem(TvPictureManager.PICTURE_CONTRAST, PicModNum[1]);
			break;
		case 2:
			TvPictureManager.getInstance().setVideoItem(TvPictureManager.PICTURE_SATURATION, PicModNum[2]);
			break;
		case 3:
			TvPictureManager.getInstance().setVideoItem(TvPictureManager.PICTURE_HUE, PicModNum[3]);
			break;
		case 4:
			TvPictureManager.getInstance().setVideoItem(TvPictureManager.PICTURE_SHARPNESS, PicModNum[4]);
			break;
		case 5:
			//TvPictureManager.getInstance().setVideoItem(TvPictureManager.PICTURE_BACKLIGHT, num);
			TvPictureManager.getInstance().setBacklight(PicModNum[5]);
			break;
		}
	}

	private int[] getPicModNum() {
		PicModNum[0] = (short) TvPictureManager.getInstance().getVideoItem(TvPictureManager.PICTURE_BRIGHTNESS);
		PicModNum[1] = (short) TvPictureManager.getInstance().getVideoItem(TvPictureManager.PICTURE_CONTRAST);
		PicModNum[2] = (short) TvPictureManager.getInstance().getVideoItem(TvPictureManager.PICTURE_SATURATION);
		PicModNum[3] = (short) TvPictureManager.getInstance().getVideoItem(TvPictureManager.PICTURE_HUE);
		PicModNum[4] = (short) TvPictureManager.getInstance().getVideoItem(TvPictureManager.PICTURE_SHARPNESS);
		PicModNum[5] = (short) TvPictureManager.getInstance().getBacklight();
		return PicModNum;
	}

	static class ViewHolder {
		TextView settingNameTextView;
		ImageView leftImageView;
		TextView settingOptionTextView;
		ImageView rightImageView;
		SeekBar showproBar;
		int pos;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ViewHolder holder;
		private int[] settingData;
		private Context context;
		public MyAdapter(Context context, int[] settingData, VideoPictureSettingDialog videoPictureSettingDialog) {
			try {
				mInflater = LayoutInflater.from(context);
				this.context = context;
				this.settingData = settingData;
			} catch (Exception e) {
			}
		}

		public int getCount() {
			return settingData.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.picture_mode_setting_list_item, null);
				holder = new ViewHolder();
				holder.settingNameTextView = (TextView) convertView.findViewById(R.id.settingNameTextView);
				holder.showproBar = (SeekBar) convertView.findViewById(R.id.showProgressBar);
				holder.leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
				holder.settingOptionTextView = (TextView) convertView.findViewById(R.id.settingOptionTextView);
				holder.rightImageView = (ImageView) convertView.findViewById(R.id.rightImageView);
				holder.pos = position;
				convertView.setTag(holder);
				if(debug)Log.d(TAG, "getPictureModeIdx().ordinal()=="+TvPictureManager.getInstance().getPictureModeIdx().ordinal());
				if (TvPictureManager.getInstance().getPictureModeIdx().ordinal() != 3) {
					holder.leftImageView.setVisibility(View.INVISIBLE);
					holder.rightImageView.setVisibility(View.INVISIBLE);
					holder.showproBar.setEnabled(false);
				}else{
					if(holder.pos==3){
						holder.showproBar.setEnabled(false);
						holder.settingNameTextView.setTextColor(Color.GRAY);
						holder.settingOptionTextView.setTextColor(Color.GRAY);
					}
					else holder.showproBar.setEnabled(true);
				}
				holder.showproBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					private ViewHolder holder = null;
					private View view = null;
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
							setPicModChange(holder.pos, PicModNum[holder.pos]);
					}
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						
					}
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {

							view= (View) seekBar.getParent();
							holder = (ViewHolder) ((View)seekBar.getParent()).getTag();
							PicModNum[holder.pos] = progress;
							holder.settingOptionTextView.setText(Integer.toString(PicModNum[holder.pos]));
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.showproBar.setProgress(PicModNum[holder.pos]);
			mBar[position] = holder.showproBar;
			/*mBar[position].setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					Log.d("skk","***************20151221******setOnTouchListener****"+mBar[holder.pos].getProgress());
					return false;
				}
	
			});*/
			
			holder.settingNameTextView.setText(context.getString(settingData[position]));
			holder.settingOptionTextView.setText(Integer.toString(PicModNum[holder.pos]));
			//convertView.setTag(holder);
			return convertView;
		}

	}
}
