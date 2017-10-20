package mstar.tvsetting.factory.ui.pqsetting;

import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.designmenu.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.mstar.android.tv.TvCommonManager;

public class PQColorSettingActivity extends BaseActivity implements OnClickListener {
	private LinearLayout linearlayout_pq_colorsetting_icc;
	private LinearLayout linearlayout_pq_colorsetting_ibc;
	private LinearLayout linearlayout_pq_colorsetting_ihc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.colorsetting);
		findView();
	}

	private void findView() {
		linearlayout_pq_colorsetting_icc = (LinearLayout) findViewById(R.id.linearlayout_factory_pq_colorsetting_icc);
		linearlayout_pq_colorsetting_ibc = (LinearLayout) findViewById(R.id.linearlayout_factory_pq_colorsetting_ibc);
		linearlayout_pq_colorsetting_ihc = (LinearLayout) findViewById(R.id.linearlayout_factory_pq_colorsetting_ihc);
		linearlayout_pq_colorsetting_icc.setOnClickListener(this);
		linearlayout_pq_colorsetting_ibc.setOnClickListener(this);
		linearlayout_pq_colorsetting_ihc.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.linearlayout_factory_pq_colorsetting_icc:
			this.startActivity(new Intent(PQColorSettingActivity.this,
					ICCActivity.class));
			break;
		case R.id.linearlayout_factory_pq_colorsetting_ibc:
			this.startActivity(new Intent(PQColorSettingActivity.this,
					IBCActivity.class));
			break;
		case R.id.linearlayout_factory_pq_colorsetting_ihc:
			this.startActivity(new Intent(PQColorSettingActivity.this,
					IHCActivity.class));
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		switch (KeyCode) {
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(KeyCode, event);
	}

}
