package com.toptech.launcherkorea2.shortcut;

import java.util.List;
import java.util.Random;

import android.R.integer;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.dock.PackageInformation;

/**
 * ��ݷ�ʽ������
 * @author calvin
 * @date 2013-1-10
 */
public class ShortCutAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<PackageInformation> selectedApkList;
	private int selectItem;

	private LayoutInflater inflater;
	private int resource;
	private String[] viewKey;
	private int[] viewId;
	private ViewHolder viewHolder;
	
	private class ViewHolder {
		public ImageView icon;
	}
	private static int[] bgimg={R.drawable.bg_00,//skye 201503
		R.drawable.bg_01,
		R.drawable.bg_02,
		R.drawable.bg_03,
		R.drawable.bg_04,
		R.drawable.bg_05,
		R.drawable.bg_06,
		R.drawable.bg_07,
		R.drawable.bg_08,
		R.drawable.bg_09,
		};
	public static int inputSize = 0;
	
	public ShortCutAdapter(Context mContext,int resource, String[] from, int[] to,
			List<PackageInformation> selectedApkList) {
		this.mContext = mContext;
		this.resource = resource;
		int i = 0;
		inputSize = selectedApkList.size();
		while(selectedApkList.size()<10){
			//selectedApkList.add((PackageInformation) defIcon.get(i));
			PackageInformation pI  = new PackageInformation();
	        pI.setIcon(new BitmapDrawable(BitmapFactory.decodeResource(mContext.getResources(), bgimg[i])));
			selectedApkList.add(pI);
			i=i+1;
			if(selectedApkList.size()>=10){
				i=0;
				break;
			}
		}
		this.selectedApkList = selectedApkList;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewKey = new String[from.length];
		viewId = new int[to.length];
		//��from�����0λ�ÿ�ʼ������ݵ�viewKey,����viewKey��0λ�÷��룬һֱ��from��from.length����
		System.arraycopy(from, 0, viewKey, 0, from.length);
		System.arraycopy(to, 0, viewId, 0, to.length);
		
	}

	public int getCount() {
		return selectedApkList.size();
		//return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;

	}
	public void setSelectItem(int selectItem) {  
        if (this.selectItem != selectItem) {                  
            this.selectItem = selectItem;  
        	notifyDataSetChanged();                 
        }
    }
	public int getSelectedItem()
	{
		return selectItem;
	}
	public void setOnDataChanged(List<PackageInformation> selectedApkList){
		int i =0;
		while(selectedApkList.size()<10){
			PackageInformation pI  = new PackageInformation();
	        pI.setIcon(new BitmapDrawable(BitmapFactory.decodeResource(mContext.getResources(), bgimg[i])));
			selectedApkList.add(pI);
			i=i+1;
			if(selectedApkList.size()>=10){
				i=0;
				break;
			}
		}
		this.selectedApkList = selectedApkList;
		notifyDataSetChanged();
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position < 0 || selectedApkList.size() <= 0){
			
			return new LinearLayout(mContext);
		}
		
		/*
		if (convertView != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {//���Ϊ�����µ���ListView�Ĳ����ļ��������ļ��Զ���
		
			convertView = inflater.inflate(resource, null);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView.findViewById(viewId[0]);
			convertView.setTag(viewHolder);
		}
		
		//PackageInformation appInfo = allApkList.get(position%allApkList.size());
		PackageInformation appInfo = allApkList.get(position);
		if(appInfo != null){
			viewHolder.icon.setImageDrawable(appInfo.getIcon());
			if(position == selectItem){
				viewHolder.icon.setBackgroundResource(R.drawable.select_bg);
			}
		}
		*/
		viewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.input_list_item, null);
			holder = new viewHolder();
			holder.defIcon = (ImageView) convertView
					.findViewById(R.id.def_icon);
			holder.inputIcon = (ImageView) convertView
					.findViewById(R.id.input_icon);
			if(inputSize>position){
			PackageInformation appInfo = selectedApkList.get(position);
			if (appInfo != null) {
				holder.inputIcon.setImageDrawable(appInfo.getIcon());
			}
			}
			holder.defIcon.setImageResource(bgimg[position]); 
         	if(position == selectItem){
				holder.defIcon.setBackgroundResource(R.drawable.select_bg);
			}
			convertView.setTag(holder);
		}else{
			holder =(viewHolder) convertView.getTag();
		}
		/*ImageView iv_icon = new ImageView(mContext);
		iv_icon.setLayoutParams(new AppGallery.LayoutParams(90, 90));
		iv_icon.setPadding(15, 0, 15, 0);
		iv_icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
		PackageInformation appInfo = selectedApkList.get(position);
		if(appInfo != null){
			iv_icon.setImageDrawable(appInfo.getIcon());
			if(position == selectItem){
				iv_icon.setBackgroundResource(R.drawable.select_bg);
			}
		}
		return iv_icon;*/
		
		return convertView;
	}
	static class viewHolder{
		ImageView defIcon ;
		ImageView inputIcon ;
	}
}
