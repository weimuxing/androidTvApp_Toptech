package mstar.tvsetting.factory.ui.designmenu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity{
	public static List<BaseActivity> activites = new ArrayList<BaseActivity>();;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activites.add(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		activites.remove(this);
		super.onDestroy();
	}

	public void setActivityVisible() {
		for (int i = activites.size() - 1; i >= 0; i--) {
			activites.get(i).setVisible(true);
		}
	}

	public void setActivityInvisible() {
		for (BaseActivity baseActivity : activites) {
			baseActivity.setVisible(false);
		}
	}
}
