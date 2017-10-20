package com.jrm.localmm.ui.video;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mstar.android.tv.TvAudioManager;
import com.jrm.localmm.R;
import java.util.Arrays;

public class VideoVideoSettingDialog extends Dialog {
	private static final String TAG = "VideoVideoSettingDialog";
	private VideoPlayerActivity mContext;
	private VideoPlaySettingDialog mVideoPlaySettingDialog;
	private MyAdapter mAdapter;
	private ListView mVideoVideoModeSettingList;
	private int viewId = 1;
	private ProgressBar[] mBar;
	public static int[] pictureModeSettingName = { R.string.video_mode_120hz, R.string.video_mode_500Hz,
		R.string.video_mode_1_5KHz,R.string.video_mode_5KHz,R.string.video_mode_10KHz};

	public VideoVideoSettingDialog(VideoPlayerActivity context, VideoPlaySettingDialog videoPlaySettingDialog) {
		super(context, R.style.dialog);
		mContext = context;
		mVideoPlaySettingDialog = videoPlaySettingDialog;
		//viewId = context.getVideoPlayHolder().getViewId();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.dismiss();
		mVideoPlaySettingDialog.show();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.video_video_setting_dialog);
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
			width = (int) (display.getWidth() * 0.3);
			height = (int) (display.getHeight() * 0.54);
		}
		w.setLayout(width, height);
		w.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams wl = w.getAttributes();
		w.setAttributes(wl);
		w.setBackgroundDrawableResource(android.R.color.transparent);
		initView();
		getVideoModNum();
	}

	private void initView() {
		mBar = new ProgressBar[5];
		mVideoVideoModeSettingList = (ListView) findViewById(R.id.videoVideoModeList);
		mVideoVideoModeSettingList.setMinimumHeight(300);
		mAdapter = new MyAdapter(mContext, pictureModeSettingName, this);
		mVideoVideoModeSettingList.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (TvAudioManager.getInstance().getAudioSoundMode() != 0) {
				Log.d(TAG, "DPAD_LEFT  Not supported in this mode");
				return true;
			}
			event.startTracking();
			View view = mVideoVideoModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.leftImageView.isShown()) {
				num_hz[holder.pos]--;
				if(num_hz[holder.pos]<0)
					num_hz[holder.pos] = 0;
				holder.showproBar.setProgress(num_hz[holder.pos]);
				holder.settingOptionTextView.setText(Integer.toString(num_hz[holder.pos]));
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (TvAudioManager.getInstance().getAudioSoundMode() != 0) {
				Log.d(TAG, "DPAD_right  Not supported in this mode");
				return true;
			}
			event.startTracking();
			View view = mVideoVideoModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.leftImageView.isShown()) {
				num_hz[holder.pos]++;
				if(num_hz[holder.pos]>100)
					num_hz[holder.pos] = 100;
				holder.showproBar.setProgress(num_hz[holder.pos]);
				holder.settingOptionTextView.setText(Integer.toString(num_hz[holder.pos]));
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			Log.d(TAG, "num_hz []"+Arrays.toString(num_hz));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (TvAudioManager.getInstance().getAudioSoundMode() != 0) {
				Log.d(TAG, "DPAD_LEFT up Not supported in this mode");
				return true;
			}
			View view = mVideoVideoModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (event.isTracking() && !event.isCanceled()) {
				setVideoModChange(holder.pos, num_hz[holder.pos]);
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (TvAudioManager.getInstance().getAudioSoundMode() != 0) {
				Log.d(TAG, "DPAD_right up Not supported in this mode");
				return true;
			}
			View view = mVideoVideoModeSettingList.getSelectedView();
			ViewHolder holder = (ViewHolder) view.getTag();
			if (event.isTracking() && !event.isCanceled()) {
				setVideoModChange(holder.pos, num_hz[holder.pos]);
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void setVideoModChange(int pos,int n) {
		switch (pos) {
		case 0:
			TvAudioManager.getInstance().setEqBand120(num_hz [0]);
			break;
		case 1:
			TvAudioManager.getInstance().setEqBand500(num_hz [1]);
			break;
		case 2:
			TvAudioManager.getInstance().setEqBand1500(num_hz [2]);
			break;
		case 3:
			TvAudioManager.getInstance().setEqBand5k(num_hz [3]);
			break;
		case 4:
			TvAudioManager.getInstance().setEqBand10k(num_hz [4]);
			break;
		}
		
	}
	private int[] num_hz =new int[5] ;
	private int[] getVideoModNum() {
		num_hz [0] = (int) TvAudioManager.getInstance().getEqBand120();
		num_hz [1] = (short) TvAudioManager.getInstance().getEqBand500();
		num_hz [2] = (short) TvAudioManager.getInstance().getEqBand1500();
		num_hz [3] = (short) TvAudioManager.getInstance().getEqBand5k();
		num_hz [4] = (short) TvAudioManager.getInstance().getEqBand10k();
		return num_hz;
	}

	static class ViewHolder {
		TextView settingNameTextView;
		ImageView leftImageView;
		TextView settingOptionTextView;
		ImageView rightImageView;
		ProgressBar showproBar;
		int pos;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private ViewHolder holder;
		private int[] settingData;
		private Context context;
		public MyAdapter(Context context, int[] settingData, VideoVideoSettingDialog VideoVideoSettingDialog) {
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
				convertView = mInflater.inflate(R.layout.video_mode_setting_list_item, null);
				holder = new ViewHolder();
				holder.settingNameTextView = (TextView) convertView.findViewById(R.id.settingNameTextView);
				holder.showproBar = (ProgressBar) convertView.findViewById(R.id.showProgressBar);
				holder.leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
				holder.settingOptionTextView = (TextView) convertView.findViewById(R.id.settingOptionTextView);
				holder.rightImageView = (ImageView) convertView.findViewById(R.id.rightImageView);
				holder.pos = position;
				if (TvAudioManager.getInstance().getAudioSoundMode() != 0) {
					holder.leftImageView.setVisibility(View.INVISIBLE);
					holder.rightImageView.setVisibility(View.INVISIBLE);
				}
			} else {
				holder.settingNameTextView = (TextView) convertView.findViewById(R.id.settingNameTextView);
				holder.showproBar = (ProgressBar) convertView.findViewById(R.id.showProgressBar);
				holder.leftImageView = (ImageView) convertView.findViewById(R.id.leftImageView);
				holder.settingOptionTextView = (TextView) convertView.findViewById(R.id.settingOptionTextView);
				holder.rightImageView = (ImageView) convertView.findViewById(R.id.rightImageView);
				holder.pos = position;
			}
			holder.showproBar.setProgress(num_hz[holder.pos]);
			mBar[position] = holder.showproBar;
			holder.settingNameTextView.setText(context.getString(settingData[position]));
			holder.settingOptionTextView.setText(Integer.toString(num_hz[holder.pos]));
			convertView.setTag(holder);
			return convertView;
		}

	}
}
