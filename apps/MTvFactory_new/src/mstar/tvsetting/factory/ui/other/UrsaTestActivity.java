
package mstar.tvsetting.factory.ui.other;

import mstar.factorymenu.ui.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UrsaTestActivity extends Activity {
	private TextView result;

	private Button runAuto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ursa_test);
		findViews();
		registerListener();
	}

	private void registerListener() {
		runAuto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				result.setText("OK");
			}
		});

	}

	private void findViews() {
		result = (TextView) findViewById(R.id.ursa_test_result);
		runAuto = (Button) findViewById(R.id.ursa_test_run_button);

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			onBackPressed();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

}
