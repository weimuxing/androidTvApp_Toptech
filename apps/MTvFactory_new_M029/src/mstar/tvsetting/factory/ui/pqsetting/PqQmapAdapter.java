package mstar.tvsetting.factory.ui.pqsetting;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.collect.Lists;
import com.mstar.android.tv.TvFactoryManager;
import java.util.ArrayList;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.until.Constant;

public class PqQmapAdapter extends BaseAdapter {
    private ArrayList<PqViewHolder> mData = null;
    private boolean bShowAllIps = true;
    private boolean bPartialLoad = true;

    Activity act = null;

    ArrayList<Integer> qmapIps = null;

    public PqQmapAdapter(Activity activity, boolean initNow) {
        act = activity;

        if (!bShowAllIps)
            qmapIps = Lists.newArrayList(1, 10, 20, 50, 60, 100, 200, 500, 300);
        mData = new ArrayList<PqViewHolder>();
        mData.clear();
        if (initNow) {
            int ipNums = TvFactoryManager.getInstance().getQmapIpNum();

            if (bShowAllIps) {
                for (int i = 0; i < ipNums; i++) {
                    PqViewHolder pvh = new PqViewHolder(Constant.PQ_TABLE_TYPE_BIG, i);
                    if (bPartialLoad && i < 20) {
                        pvh.init();
                    }
                    mData.add(pvh);
                }
            } else {
                int displayCount = qmapIps.size();
                int ipidx = 0;
                for (int i = 0; i < displayCount; i++) {
                    ipidx = qmapIps.get(i);

                    if (ipidx >= ipNums) continue;

                    PqViewHolder pvh = new PqViewHolder(Constant.PQ_TABLE_TYPE_BIG, ipidx);
                    pvh.init();
                    mData.add(pvh);
                }
            }
        }
    }

    public void reloadData() {
        // TODO Auto-generated method stub
        if (mData == null)
            mData = new ArrayList<PqViewHolder>();
        mData.clear();
        int ipNums = TvFactoryManager.getInstance().getQmapIpNum();
        if (bShowAllIps) {
            for (int i = 0; i < ipNums; i++) {
                PqViewHolder pvh = new PqViewHolder(Constant.PQ_TABLE_TYPE_BIG, i);
                if (bPartialLoad && i < 20) {
                    pvh.init();
                }
                mData.add(pvh);
            }
        } else {
            int displayCount = qmapIps.size();
            int ipidx = 0;
            for (int i = 0; i < displayCount; i++) {
                ipidx = qmapIps.get(i);

                if (ipidx >= ipNums) continue;

                PqViewHolder pvh = new PqViewHolder(Constant.PQ_TABLE_TYPE_BIG, ipidx);
                pvh.init();
                mData.add(pvh);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (bPartialLoad && !mData.get(position).bLoaded) {
            mData.get(position).init();
        }
        return mData.get(position);
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
            convertView = LayoutInflater.from(act).inflate(R.layout.pq_item, null);
            HoldView tagView = new HoldView();
            tagView.pqIpName = (TextView) convertView
                    .findViewById(R.id.pq_item_display_text);
            tagView.pqIpValue = (TextView) convertView
                    .findViewById(R.id.pq_item_value_text);
            convertView.setTag(tagView);
        }

        HoldView tagView = (HoldView)convertView.getTag();
        if (bPartialLoad && !mData.get(position).bLoaded) {
            mData.get(position).init();
        }
        tagView.pqIpName.setText(mData.get(position).getIpName());
        tagView.pqIpValue.setText(mData.get(position).getCurrentTableName());
        //begin wangxielin 2010326
        if(position == foucusItem){
            tagView.pqIpName.setTextColor(Color.CYAN);
            tagView.pqIpValue.setTextColor(Color.CYAN);
        } else {
            tagView.pqIpName.setTextColor(Color.WHITE);
            tagView.pqIpValue.setTextColor(Color.WHITE);
        }
        //end
        return convertView;
    }

    private class HoldView {
        TextView pqIpName;
        TextView pqIpValue;
    }
}