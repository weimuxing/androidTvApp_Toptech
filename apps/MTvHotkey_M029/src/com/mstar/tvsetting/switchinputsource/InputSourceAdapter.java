package com.mstar.tvsetting.switchinputsource;

import java.util.ArrayList;

import com.mstar.tvsetting.R;
import com.mstar.tvsetting.hotkey.InputSourceItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InputSourceAdapter extends BaseAdapter
{
	private Context con;
	private ArrayList<InputSourceItem> mData;
	private boolean switchflag = true;
	private int focusPos=0;

	public InputSourceAdapter(Context context, ArrayList<InputSourceItem> data,
	        boolean flag)
	{
		mData = data;
		con = context;
		switchflag = flag;
	}

	@Override
	public int getCount()
	{
		return mData.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mData.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent)
	{
		viewHolder holder;
		if(view==null){
			view = LayoutInflater.from(con).inflate(
			        R.layout.input_source_list_view_item, null);
			holder = new viewHolder();
			holder.inputSourceName = (TextView) view
			        .findViewById(R.id.input_source_data);
					holder.inputSourceImage =(ImageView) view
					        .findViewById(R.id.input_source_img);
					view.setTag(holder);
		}else{
			holder =(viewHolder) view.getTag();
		}
		InputSourceItem inputSourceItem = mData.get(index);
		holder.inputSourceName.setText(inputSourceItem.getInputSourceName());
		if (switchflag)
		{
			/*if (inputSourceItem.isSignalFlag())
			{*/
				if (inputSourceItem.getTypeFlag() == 3)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_vga);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_vga);
					}
				}
				else if (inputSourceItem.getTypeFlag() == 0)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_tv);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_tv);
					}
				}
				else if (inputSourceItem.getTypeFlag() == 1)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_av);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_av);
					}
				}
				else if (inputSourceItem.getTypeFlag() == 2)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_ypbpr);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_ypbpr);
					}
				}
				else if (inputSourceItem.getTypeFlag() == 4)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_hdmi);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_hdmi);
					}
				}
				else if (inputSourceItem.getTypeFlag() == 5)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_ypbpr);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_ypbpr);
					}
				}
				else if (inputSourceItem.getTypeFlag() == 6)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_bluetooh);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.list_menu_img_bluetooh);
					}
				}
				else if (inputSourceItem.getTypeFlag() == 7)
				{
					if (inputSourceItem.isSelectFlag())
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.dmp_media_type_usb_icon_colour);
					}
					else
					{
						holder.inputSourceImage
						        .setImageResource(R.drawable.dmp_media_type_usb_icon_colour);
					}
				}
			//}
			
		}
		else
		{
			if (inputSourceItem.getTypeFlag() == 0)
			{
				holder.inputSourceImage
				        .setImageResource(R.drawable.list_menu_img_notvsignal_disable);
			}
			else if (inputSourceItem.getTypeFlag() == 1)
			{
				holder.inputSourceImage
				        .setImageResource(R.drawable.list_menu_img_notvline_disable);
			}
		}
		if(focusPos==index){
			view.setBackgroundResource(R.drawable.picture_mode_img_focus);
		}else{
			view.setBackgroundColor(Color.TRANSPARENT);

		}
		return view;
	}
	
	public void setFocusPosition(int position)
	{
		focusPos=position;
		notifyDataSetChanged();
	}
	static class viewHolder{
		TextView inputSourceName ;
		ImageView inputSourceImage ;
	}
}