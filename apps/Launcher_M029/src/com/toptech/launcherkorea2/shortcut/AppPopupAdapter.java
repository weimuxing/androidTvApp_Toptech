package com.toptech.launcherkorea2.shortcut;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
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
 * @author calvin
 * @date 2013-1-10
 */
public class AppPopupAdapter extends BaseAdapter{
	
	private final static String TAG = "AppPopupAdapter";
	private Context mContext;
	private List<PackageInformation> _allAPKList;
	private List<PackageInformation> _selectedAPKList;
	private HashMap<String, PackageInformation> apkMap = null;
	
	private LayoutInflater inflater;
	private int resource;
	private String[] viewKey;
	private int[] viewId;
	private ViewHolder viewHolder;
	private Toast  mytoast = null;
	
	private class ViewHolder {
		public ImageView icon;
		public CheckBox ckbox;
		public TextView name;
	}
	
	public AppPopupAdapter(Context mContext,int resource, String[] from,int[] to,
			List<PackageInformation> allAPKList,
			List<PackageInformation> selectedAPKList) {
		this.mContext = mContext;
		this.resource = resource;
		_allAPKList = allAPKList;
		_selectedAPKList = selectedAPKList;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewKey = new String[from.length];
		viewId = new int[to.length];
		//从from对象的0位置开始拷贝数据到viewKey,并从viewKey的0位置放入，一直到from的from.length长度
		System.arraycopy(from, 0, viewKey, 0, from.length);
		System.arraycopy(to, 0, viewId, 0, to.length);
		
		apkMap = new HashMap<String,PackageInformation>();
		ListToHashMap();
	}
	
	//把list集合转换成HashMap集合
	public void ListToHashMap(){
		apkMap.clear();
		if(_selectedAPKList == null)
		{
			return;
		}
		for(PackageInformation p :_selectedAPKList){
                    if(p.getPackageName() != null)
                        apkMap.put(p.getPackageName(), p);
		}
	}
	

	public int getCount() {
		return _allAPKList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	
	public boolean onEnterKeyListener(int position)
	{
		PackageInformation p = _allAPKList.get(position);
		if(!apkMap.containsKey(p.getPackageName())){
			if(apkMap.size()>=10){
			    if (mytoast == null)
				mytoast = Toast.makeText(mContext, "10 is Maximum", Toast.LENGTH_LONG);
				mytoast.show();
				return true;
			}
			apkMap.put(p.getPackageName(), p);
			notifyDataSetChanged();
		}
		else{
			apkMap.remove(p.getPackageName());
			notifyDataSetChanged();
		}
		
		return true;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position < 0 || _allAPKList.size() <= 0){
			return new LinearLayout(mContext);
		}
		
			
			convertView = inflater.inflate(resource, null);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(viewId[0]);
			viewHolder.ckbox = (CheckBox)convertView.findViewById(viewId[1]);
			viewHolder.name = (TextView)convertView.findViewById(viewId[2]);
			convertView.setTag(viewHolder);
		//}
		
		PackageInformation appInfo = _allAPKList.get(position);
		if(appInfo != null){
			viewHolder.icon.setImageDrawable(appInfo.getIcon());
			viewHolder.name.setText(appInfo.getAppName());
			viewHolder.ckbox.setChecked(apkMap.containsKey(appInfo.getPackageName()));
			viewHolder.ckbox.setOnCheckedChangeListener(new AppCkListener(appInfo));
		}
		
		return convertView;
	}
	
	/**
	 * 该方法用于对apk的全选或反选
	 * @param apklist
	 */
	public void setSelectedApk(List<PackageInformation> apklist)
	{
		_selectedAPKList = apklist;
		ListToHashMap();
		notifyDataSetChanged();
	}
	
	private class AppCkListener implements OnCheckedChangeListener{
		
		private PackageInformation p;
		private Toast  mytoast;
		
		public AppCkListener(PackageInformation p){
			this.p = p;
			this.mytoast = null;
		}
		@Override
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {	
			if(isChecked){//选中
				if(apkMap.size()>=10){
					//buttonView.setClickable(false);
				//	buttonView.setEnabled(false);
					buttonView.setChecked(false);
					
					if (mytoast == null)
					mytoast = Toast.makeText(mContext, "10 is Maximum", Toast.LENGTH_LONG);
					mytoast.show();
					return ;
				}else{
				if(!apkMap.containsKey(p.getPackageName())){
					Log.d(TAG, "Add package Infomation");
					apkMap.put(p.getPackageName(), p);
					notifyDataSetChanged();
				}
				}
			}else{
				/*
				if(apkMap.size() == 1){
					Toast.makeText(mContext, mContext.getString(R.string.min_apk_num), Toast.LENGTH_LONG).show();
					((CheckBox)buttonView).setChecked(true);
					return;
				}
				*/
				if(apkMap.containsKey(p.getPackageName())){
					Log.d(TAG, "Del package Infomation");
					apkMap.remove(p.getPackageName());
					notifyDataSetChanged();
				}
			}
		}
		
	}
	

	public List<PackageInformation> getApkList(){
		if(_selectedAPKList == null)
		{
			return null;
		}
		_selectedAPKList.clear();
		for(String pkg : apkMap.keySet()){
			if (apkMap.get(pkg).getAppName()!=null) {
				_selectedAPKList.add(apkMap.get(pkg));
				}
		}
		ShortCutAdapter.inputSize = _selectedAPKList.size() ;
		return _selectedAPKList;
	}
}