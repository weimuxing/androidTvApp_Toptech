package mstar.tvsetting.factory.ui.pqsetting;


import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.mstar.android.tv.TvFactoryManager;

import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.designmenu.BaseActivity;
import mstar.tvsetting.factory.until.Constant;


public class PQColorQmapActivity extends BaseActivity {
    static public String TAG = "PQColorQmapActivity";

	private ListView listview_pq_ip;
	private TextView title = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qmap);

		title = (TextView)findViewById(R.id.title);
		title.setText(R.string.str_textview_factory_pq_qmap);

		listview_pq_ip = (ListView) findViewById(R.id.textview_factory_pq_list);
		listview_pq_ip.setAdapter(new PqColorQmapAdapter(this, true));
		listview_pq_ip.setItemsCanFocus(true);
		listview_pq_ip.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Adapter adpater = parent.getAdapter();
				PqViewHolder ph = (PqViewHolder)adpater.getItem(position);
				Log.d(TAG, "onItemClick " + position);
				createDialog(ph.getTableNames(), ph.getCurrentIndex(), ph.getIpName(),
						-1, ph.getIpIndex(), Constant.PQ_TABLE_TYPE_LITTLE);
			}
		});
		listview_pq_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d(TAG, "onItemSelected " + position);
				Log.d(TAG, "onItemSelected " + view);
				//view.requestFocus();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Log.d(TAG, "onNothingSelected " + parent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		listview_pq_ip.invalidate();
		listview_pq_ip.requestFocus();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "keycode is " + keyCode);
		Log.d(TAG, "focus is " + getCurrentFocus().toString());
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_MENU) {
			finish();
			return true;
		}
		return false;
	}

	public void createDialog(String[] strings,int curIndex,String name,
							  int type, int ipIndex, int pqType){
		final MiniDialog miniDialog = MiniDialog.createQmapDialog(strings, curIndex, name, type, ipIndex, pqType);
		miniDialog.show(getFragmentManager(),"");

		miniDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				PqColorQmapAdapter pca = (PqColorQmapAdapter)listview_pq_ip.getAdapter();
				pca.reloadData();
				setActivityVisible();
			}
		});
		miniDialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub
				setActivityInvisible();
			}
		});
	}
}
