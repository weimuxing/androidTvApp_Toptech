package com.toptech.launcherkorea2.dock;

import android.app.Activity;
import java.util.ArrayList;

import com.toptech.launcherkorea2.R;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.view.LayoutInflater;

/**
 * @author calvin
 * @date 2013-1-10
 */
public class AppGridViewAdpter extends BaseAdapter {
	
	private final static String  tag = "AppGridViewAdpter";
	// private Context mContext = null;
	private Activity mActivity = null;
	private ArrayList<PackageInformation> mList = null;
	private final int EDGE_PADDING = 2;
	private int mWidth = 80;
	private int mHeight = 80;
	// private int mTextSize = 15;
	public int mTextColor = Color.WHITE;
	private LayoutInflater flater = null;

	public AppGridViewAdpter(Activity activity,ArrayList<PackageInformation> list) {

		// mContext = context;
		mActivity = activity;
		mList = list;
		flater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * void setTextSize(int size) { mTextSize = size; }
	 */

	void setTextColor(int color) {
		mTextColor = color;
	}

	void setWidthAndHeight(int width, int height) {
		mWidth = width;
		mHeight = height;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		if (mList != null)
			return mList.size();

		return 0;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mList != null)
			return mList.get(position);

		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if ((mList != null) && (position >= mList.size())){
			return null;
		}
		
		LinearLayout layout = null;
		if (convertView != null) {
			layout = (LinearLayout) convertView;
			//return layout;
		}

		if(mList != null){
			layout = (LinearLayout) flater.inflate(R.layout.grid_view_item, null);
	
			ImageView image = (ImageView) layout.findViewById(R.id.item_imageview);
			image.setImageDrawable(mList.get(position).getIcon());
	
			TextView text = (TextView) layout.findViewById(R.id.item_textview);
			text.setText(mList.get(position).getAppName());
		}
		return layout;
	}

}
