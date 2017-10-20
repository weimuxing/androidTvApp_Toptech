
package com.mstar.tv.tvplayer.ui.dtv.epg;

import java.util.ArrayList;

import com.mstar.tv.tvplayer.ui.holder.EPGViewHolder;
import com.mstar.tv.tvplayer.ui.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.graphics.Color;

public class EPGAdapter extends BaseAdapter {
    ArrayList<EPGViewHolder> mData = null;

    Activity act = null;

    public EPGAdapter(Activity activity, ArrayList<EPGViewHolder> vh) {
        mData = vh;
        act = activity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

	//begin wangxielin 2010326
	private int foucusItem = -1;
	public void setFocusItem(int focus){
		foucusItem = focus;
	}

	public int getLastFocusItem(){
		return foucusItem;
	}
	//end

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(act).inflate(R.layout.programme_epg_item, null);
        }
        TextView channelName = (TextView) convertView
                .findViewById(R.id.programme_epg_list_view_item_text_view_title);
        TextView channelInfo = (TextView) convertView
                .findViewById(R.id.programme_epg_list_view_item_text_view);
        channelName.setText(mData.get(position).getChannelName());
        channelInfo.setText(mData.get(position).getChannelInfo());
		//begin wangxielin 2010326
		if(position == foucusItem){
        	channelName.setTextColor(Color.BLUE);
        	channelInfo.setTextColor(Color.BLUE);
        } else {
			channelName.setTextColor(Color.WHITE);
        	channelInfo.setTextColor(Color.WHITE);
        }
		//end
        return convertView;
    }
}
