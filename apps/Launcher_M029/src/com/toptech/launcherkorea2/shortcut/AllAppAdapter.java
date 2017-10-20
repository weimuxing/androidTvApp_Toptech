package com.toptech.launcherkorea2.shortcut;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.dock.PackageInformation;

/**
 * 
 * @author calvin
 * @date 2013-1-10
 */
public class AllAppAdapter extends BaseAdapter{
	
	private final static String tag = "AllAppAdapter";
	private Context mContext;
	private List<PackageInformation> allAPKList;
	private List<PackageInformation> selectedAPKList;
	private static HashMap<String, PackageInformation> apkMap = null;
	
	private LayoutInflater inflater;
	private int resource;
	private String[] viewKey;
	private int[] viewId;
	private ViewHolder viewHolder;
	
	private class ViewHolder {
		public ImageView icon;
		public CheckBox ckbox;
		public TextView name;
	}
	
	public AllAppAdapter(Context mContext,int resource, String[] from,int[] to,
			List<PackageInformation> allAPKList,
			List<PackageInformation> selectedAPKList) {
		this.mContext = mContext;
		this.resource = resource;
		this.allAPKList = allAPKList;
		this.selectedAPKList = selectedAPKList;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewKey = new String[from.length];
		viewId = new int[to.length];
		System.arraycopy(from, 0, viewKey, 0, from.length);
		System.arraycopy(to, 0, viewId, 0, to.length);
		
		apkMap = new HashMap<String,PackageInformation>();
		ListToHashMap();
	}
	

	public void ListToHashMap(){
		for(PackageInformation p :selectedAPKList){
			apkMap.put(p.getPackageName(), p);
		}
	}
	

	public int getCount() {
		return allAPKList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position < 0 || allAPKList.size() <= 0){
			return new LinearLayout(mContext);
		}
		
			
			convertView = inflater.inflate(resource, null);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(viewId[0]);
			viewHolder.ckbox = (CheckBox)convertView.findViewById(viewId[1]);
			viewHolder.name = (TextView)convertView.findViewById(viewId[2]);
			convertView.setTag(viewHolder);
		//}
		
		PackageInformation appInfo = allAPKList.get(position);
		if(appInfo != null){
			viewHolder.icon.setImageDrawable(appInfo.getIcon());
			viewHolder.name.setText(appInfo.getAppName());
			viewHolder.ckbox.setChecked(apkMap.containsKey(appInfo.getPackageName()));
			viewHolder.ckbox.setOnCheckedChangeListener(new AppCkListener(appInfo));
		}
		
		return convertView;
	}
	
	private class AppCkListener implements OnCheckedChangeListener{
		
		private PackageInformation p;
		
		public AppCkListener(PackageInformation p){
			this.p = p;
		}
		@Override
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {	
			if(isChecked){
				if(!apkMap.containsKey(p.getPackageName())){
					Log.d(tag, "add packageInfomation");
					apkMap.put(p.getPackageName(), p);
					notifyDataSetChanged();
				}
			}else{
				if(apkMap.size() == 1){
					Toast.makeText(mContext, mContext.getString(R.string.min_apk_num), Toast.LENGTH_LONG).show();
					((CheckBox)buttonView).setChecked(true);
					return;
				}
				if(apkMap.containsKey(p.getPackageName())){
					Log.d(tag, "del packageInfomation");
					apkMap.remove(p.getPackageName());
					notifyDataSetChanged();
				}
			}
		}
		
	}
	
	
	public List<PackageInformation> getApkList(){
		selectedAPKList.clear();
		for(String pkg : apkMap.keySet()){
			selectedAPKList.add(apkMap.get(pkg));
		}
		return selectedAPKList;
	}

	
}