package com.mstar.miscsetting.dialog;

import java.util.ArrayList;

import com.mstar.miscsetting.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SwitchVolAdapter  extends BaseAdapter{

    private ArrayList<SwitchVolItem> mData;

    private PipSubWindowSwitchVolDialog dialog;

    private boolean switchflag = true;

    public SwitchVolAdapter(
            PipSubWindowSwitchVolDialog pipSubWindowSwitchVolDialog,
            ArrayList<SwitchVolItem> data) {
        mData = data;
        dialog = pipSubWindowSwitchVolDialog;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        view = LayoutInflater.from(dialog.getContext()).inflate(
                R.layout.subwindow_inputsource_list_item, null);
        SwitchVolItem switchVolItem = mData.get(index);
        TextView inputSourceName = (TextView) view
                .findViewById(R.id.input_source_data);
        inputSourceName.setText(switchVolItem.getVolName());
        return view;
    }

}
