package com.mstar.tv.tvplayer.ui;

import java.util.List;
import java.util.Map;



import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.SystemProperties;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapter extends BaseAdapter{

	private class ViewHolder {
		public ImageView icon;
		public TextView name;
		public ImageView focus_icon;
	}
	
	private Context context;
	private Resources res;
	private List<Map<String,Object>> list;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder;
	private int focusPos=-1;
//	private TypedArray focusIcon=null;
//	private TypedArray nonFocusIcon=null;
	
	private int[] nonFocusIcon={
		R.drawable.picture_icon_disable,
		R.drawable.sound_icon_disable,
		R.drawable.channel_icon_disable,
		R.drawable.setting_icon_disable,
		R.drawable.time_icon_disable,
		R.drawable.lock_icon_disable,
	};
	private int[] focusIcon={
		R.drawable.picture_icon_focus,
		R.drawable.sound_icon_focus,
		R.drawable.channel_icon_focus,
		R.drawable.setting_icon_focus,
		R.drawable.time_icon_focus,
		R.drawable.lock_icon_focus,
	};
	
	private int[] nonFocusIcon_no_tv={
			R.drawable.picture_icon_disable,
			R.drawable.sound_icon_disable,
			R.drawable.setting_icon_disable,
			R.drawable.time_icon_disable,
			R.drawable.lock_icon_disable,
		};
		private int[] focusIcon_no_tv={
			R.drawable.picture_icon_focus,
			R.drawable.sound_icon_focus,
			R.drawable.setting_icon_focus,
			R.drawable.time_icon_focus,
			R.drawable.lock_icon_focus,
		};
		
	
	public ListViewAdapter(Context context, List<Map<String,Object>> list){
		this.context = context;
		this.list = list;
		res = context.getResources();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		int page =((Activity) context).getIntent().getIntExtra("currentPage", 0);
		if(page!=0){
			focusPos=-1;
		}else{
		focusPos=0;
		}
//		nonFocusIcon=context.getResources().obtainTypedArray(R.array.more_setting_listview_icon);
//		focusIcon=context.getResources().obtainTypedArray(R.array.more_setting_listview_icon_focus);
	}
	
	public void updateData(List<Map<String,Object>> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(list == null || position >= list.size()){
			return null;
		}
		
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			if (SystemProperties.getBoolean("persist.sys.no_tv", false)) {
				convertView = inflater.inflate(R.layout.listview_item_no_tv_lock, null);
			}
			else if (!MainMenuActivity.isSourceInTv()) {
				convertView = inflater.inflate(R.layout.listview_item_no_tv, null);
			} else {
				convertView = inflater.inflate(R.layout.listview_item, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.lv_img);
			viewHolder.name = (TextView) convertView.findViewById(R.id.lv_text);
			viewHolder.focus_icon = (ImageView) convertView
					.findViewById(R.id.lv_focus_img);
			convertView.setTag(viewHolder);
		}
		
		Map<String, Object> map = list.get(position);
		if(map != null){
			int resId ;
			
//			if(Boolean.parseBoolean(map.get("focus").toString()))
//			{
//				resId=res.getIdentifier(map.get("focus_icon").toString(),"drawable",context.getPackageName());
//				viewHolder.icon.setBackgroundResource(resId);
//			}
//			else
//			{
//				resId= res.getIdentifier(map.get("icon").toString(),"drawable",context.getPackageName());
//				viewHolder.icon.setBackgroundResource(resId);
//			}
			viewHolder.name.setText(map.get("text").toString());
			if (!MainMenuActivity.isSourceInTv()) {
				if(position==focusPos)
				{
					if( focusIcon_no_tv.length <= position )
				        position = 0;
					viewHolder.icon.setBackgroundResource(focusIcon_no_tv[position]);
					viewHolder.name.setTextColor(0xFF3197FF);
					//convertView.setBackgroundColor(0xFF00008B);
					convertView.setBackgroundResource(R.drawable.listitem_blue_bg);
				}
				else {
					if( nonFocusIcon_no_tv.length <= position )
				        position = 0;
					viewHolder.icon.setBackgroundResource(nonFocusIcon_no_tv[position]);
					viewHolder.name.setTextColor(0xFFFFFFFF);
					convertView.setBackgroundColor(Color.TRANSPARENT);
				}
			}else{
				if(position==focusPos){
					if( focusIcon.length <= position)
				        position = 0;
					viewHolder.icon.setBackgroundResource(focusIcon[position]);
					viewHolder.name.setTextColor(0xFF3197FF);
					//convertView.setBackgroundColor(0xFF00008B);
					convertView.setBackgroundResource(R.drawable.listitem_blue_bg);
				}
				else {
					if( nonFocusIcon.length <= position )
				        position = 0;
					viewHolder.icon.setBackgroundResource(nonFocusIcon[position]);
					viewHolder.name.setTextColor(0xFFFFFFFF);
					convertView.setBackgroundColor(Color.TRANSPARENT);
				}

			}
			
		}
		/*if(Boolean.parseBoolean(map.get("selected").toString()))
		{
			convertView.setBackgroundColor(0xFF444444);
		}
		else {
			convertView.setBackgroundColor(0);
		}
		if(Boolean.parseBoolean(map.get("focus_img_show").toString()))
		{
			viewHolder.focus_icon.setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.focus_icon.setVisibility(View.INVISIBLE);
		}*/
		//convertView.setOnHoverListener(new AppOnHoverListener(position));
		return convertView;
	}
	
	private class AppOnHoverListener implements OnHoverListener{
		private int pos;
		public AppOnHoverListener(int pos){
			this.pos = pos;
		}
		@Override
		public boolean onHover(View v, MotionEvent event) {
			Map<String,Object> map = list.get(pos);
			if(map == null){
				return false;
			}
			int what = event.getAction();  
	        switch(what){  
	         case MotionEvent.ACTION_HOVER_ENTER: 
	        	 //int resId_press = res.getIdentifier(map.get("icon_press").toString(),"drawable",context.getPackageName());
	        	 v.setBackgroundColor(Color.argb(24, 0, 165, 240));
	             break;  
	         case MotionEvent.ACTION_HOVER_EXIT:  
	        	 //int resId = res.getIdentifier(map.get("icon").toString(),"drawable",context.getPackageName());
	        	 v.setBackgroundColor(Color.TRANSPARENT);
	             break;  
	        }  
			return false;
		}
		
	}
	public void setFocusPosition(int position)
	{
		focusPos=position;
		notifyDataSetChanged();  
	}
}
