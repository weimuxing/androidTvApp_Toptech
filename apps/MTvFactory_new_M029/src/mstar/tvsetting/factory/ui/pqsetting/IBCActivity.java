package mstar.tvsetting.factory.ui.pqsetting;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.designmenu.BaseActivity;
import mstar.tvsetting.factory.until.Constant;

public class IBCActivity extends BaseActivity {

	private TextView textview_pq_colorsetting_ibc_enable;
	private TextView textview_pq_colorsetting_ibc_r;
	private TextView textview_pq_colorsetting_ibc_g;
	private TextView textview_pq_colorsetting_ibc_b;
	private TextView textview_pq_colorsetting_ibc_c;
	private TextView textview_pq_colorsetting_ibc_m;
	private TextView textview_pq_colorsetting_ibc_y;
	private TextView textview_pq_colorsetting_ibc_f;
	private TextView textview_pq_colorsetting_ibc_gy;
	private TextView textview_pq_colorsetting_ibc_by;
	private TextView textview_pq_colorsetting_ibc_flesh;
	private TextView textview_pq_colorsetting_ibc_reddish;
	private TextView textview_pq_colorsetting_ibc_lip;
	private TextView textview_pq_colorsetting_ibc_hair;
	private TextView textview_pq_colorsetting_ibc_meat;
	private TextView textview_pq_colorsetting_ibc_dark;
	private String[] arr_ibcEnable;
	private int ibcEnableVal = 1;
	private int ibcRVal = 4;
	private int ibcGVal = 10;
	private int ibcBVal = 9;
	private int ibcCVal = 6;
	private int ibcMVal = 6;
	private int ibcYVal = 3;
	private int ibcFVal = 5;
	private int ibcGYVal = 4;
	private int ibcBYVal = 4;
	private int ibcFleshVal = 6;
	private int ibcReddishVal = 6;
	private int ibcLipVal = 3;
	private int ibcHairVal = 5;
	private int ibcMeatVal = 4;
	private int ibcDarkVal = 4;
	private MiniDialog miniDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ibc);
//		arr_ibcEnable = getResources().getStringArray(R.array.str_arr_pq_color_setting_ibc_enable);
		findView();
	}

	private void findView() {
		textview_pq_colorsetting_ibc_enable = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_enableval);
		textview_pq_colorsetting_ibc_r = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_rval);
		textview_pq_colorsetting_ibc_g = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_gval);
		textview_pq_colorsetting_ibc_b = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_bval);
		textview_pq_colorsetting_ibc_c = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_cval);
		textview_pq_colorsetting_ibc_m = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_mval);
		textview_pq_colorsetting_ibc_y = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_yval);
		textview_pq_colorsetting_ibc_f = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_fval);
		textview_pq_colorsetting_ibc_gy = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_gyval);
		textview_pq_colorsetting_ibc_by = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_byval);
		textview_pq_colorsetting_ibc_flesh = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_fleshval);
		textview_pq_colorsetting_ibc_reddish = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_reddishval);
		textview_pq_colorsetting_ibc_lip = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_lipval);
		textview_pq_colorsetting_ibc_hair = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_hairval);
		textview_pq_colorsetting_ibc_meat = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_meatval);
		textview_pq_colorsetting_ibc_dark = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ibc_darkval);
		initView();
	}

	private void initView() {
		arr_ibcEnable = new String[2];
		arr_ibcEnable[0]="OFF";
		arr_ibcEnable[1]="ON";
//		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");

		if (MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_ENABLE) != 0)
			ibcEnableVal = 1;
		else
			ibcEnableVal = 0;

		ibcRVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_R);
		ibcGVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_G);
		ibcBVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_B);
		ibcCVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_C);
		ibcMVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_M);
		ibcYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_Y);
		ibcFVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_F);
		ibcGYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_GY);
		ibcBYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_BY);
		ibcFleshVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_FLESH);
		ibcReddishVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_REDDISH);
		ibcLipVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_LIP);
		ibcHairVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_HAIR);
		ibcMeatVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_MEAT);
		ibcDarkVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IBC_DARK);

		textview_pq_colorsetting_ibc_enable.setText(arr_ibcEnable[ibcEnableVal]);
		textview_pq_colorsetting_ibc_r.setText("0x"	+ Integer.toHexString(ibcRVal).toUpperCase());
		textview_pq_colorsetting_ibc_g.setText("0x"	+ Integer.toHexString(ibcGVal).toUpperCase());
		textview_pq_colorsetting_ibc_b.setText("0x"	+ Integer.toHexString(ibcBVal).toUpperCase());
		textview_pq_colorsetting_ibc_c.setText("0x"	+ Integer.toHexString(ibcCVal).toUpperCase());
		textview_pq_colorsetting_ibc_m.setText("0x"+ Integer.toHexString(ibcMVal).toUpperCase());
		textview_pq_colorsetting_ibc_y.setText("0x"	+ Integer.toHexString(ibcYVal).toUpperCase());
		textview_pq_colorsetting_ibc_f.setText("0x"	+ Integer.toHexString(ibcFVal).toUpperCase());
		textview_pq_colorsetting_ibc_gy.setText("0x" + Integer.toHexString(ibcGYVal).toUpperCase());
		textview_pq_colorsetting_ibc_by.setText("0x" + Integer.toHexString(ibcBYVal).toUpperCase());
		textview_pq_colorsetting_ibc_flesh.setText("0x"	+ Integer.toHexString(ibcFleshVal).toUpperCase());
		textview_pq_colorsetting_ibc_reddish.setText("0x"	+ Integer.toHexString(ibcReddishVal).toUpperCase());
		textview_pq_colorsetting_ibc_lip.setText("0x"	+ Integer.toHexString(ibcLipVal).toUpperCase());
		textview_pq_colorsetting_ibc_hair.setText("0x"	+ Integer.toHexString(ibcHairVal).toUpperCase());
		textview_pq_colorsetting_ibc_meat.setText("0x" + Integer.toHexString(ibcMeatVal).toUpperCase());
		textview_pq_colorsetting_ibc_dark.setText("0x" + Integer.toHexString(ibcDarkVal).toUpperCase());
	}

	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		boolean bRet = true;
		int currentId = this.getCurrentFocus().getId();
		switch (KeyCode) {
		case KeyEvent.KEYCODE_ENTER:
			int tmp = 0;
			String  strName ="";
			int itemId = 0;
			switch (currentId) {
			case R.id.linearlayout_factory_pq_colorsetting_ibc_enable:
				strName = getResources().getString(R.string.str_textview_factory_pq_colorsetting_ibc_enable);
				itemId = Constant.PQ_COLOR_SETTING_IBC_ENABLE;
				miniDialog = MiniDialog.createRegOnoffDialog(arr_ibcEnable, ibcEnableVal, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_r:
				tmp = ibcRVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_r);
				itemId = Constant.PQ_COLOR_SETTING_IBC_R;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_g:
				tmp = ibcGVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_g);
				itemId = Constant.PQ_COLOR_SETTING_IBC_G;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_b:
				tmp = ibcBVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_b);
				itemId = Constant.PQ_COLOR_SETTING_IBC_B;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_c:
				tmp = ibcCVal;
				itemId = Constant.PQ_COLOR_SETTING_IBC_C;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_c);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_m:
				tmp = ibcMVal;
				itemId = Constant.PQ_COLOR_SETTING_IBC_M;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_m);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_y:
				tmp = ibcYVal;
				itemId = Constant.PQ_COLOR_SETTING_IBC_Y;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_y);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_f:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_f);
				tmp = ibcFVal;
				itemId = Constant.PQ_COLOR_SETTING_IBC_F;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_gy:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_gy);
				tmp = ibcGYVal;
				itemId = Constant.PQ_COLOR_SETTING_IBC_GY;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_by:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_by);
				tmp = ibcBYVal;
				itemId = Constant.PQ_COLOR_SETTING_IBC_BY;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ibc_flesh:
			strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_flesh);
			itemId = Constant.PQ_COLOR_SETTING_IBC_FLESH;
			tmp = ibcFleshVal;
			miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
			miniDialog.show(getFragmentManager(), "");
			createDialog(currentId);
			break;
		case R.id.linearlayout_factory_pq_colorsetting_ibc_reddish:
			strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_reddish);
			itemId = Constant.PQ_COLOR_SETTING_IBC_REDDISH;
			tmp = ibcReddishVal;
			miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
			miniDialog.show(getFragmentManager(), "");
			createDialog(currentId);
			break;
		case R.id.linearlayout_factory_pq_colorsetting_ibc_lip:
			strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_lip);
			itemId = Constant.PQ_COLOR_SETTING_IBC_LIP;
			tmp = ibcLipVal;
			miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
			miniDialog.show(getFragmentManager(), "");
			createDialog(currentId);
			break;
		case R.id.linearlayout_factory_pq_colorsetting_ibc_hair:
			strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_hair);
			itemId = Constant.PQ_COLOR_SETTING_IBC_HAIR;
			tmp = ibcHairVal;
			miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
			miniDialog.show(getFragmentManager(), "");
			createDialog(currentId);
			break;
		case R.id.linearlayout_factory_pq_colorsetting_ibc_meat:
			strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_meat);
			itemId = Constant.PQ_COLOR_SETTING_IBC_MEAT;
			tmp = ibcMeatVal;
			miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
			miniDialog.show(getFragmentManager(), "");
			createDialog(currentId);
			break;
		case R.id.linearlayout_factory_pq_colorsetting_ibc_dark:
			strName = getString(R.string.str_textview_factory_pq_colorsetting_ibc_dark);
			itemId = Constant.PQ_COLOR_SETTING_IBC_DARK;
			tmp = ibcDarkVal;
			miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, itemId);
			miniDialog.show(getFragmentManager(), "");
			createDialog(currentId);
			break;
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			finish();

		default:
			bRet = false;
			break;
		}
		return bRet;
	}
	private void createDialog(final int currentID) {
		miniDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				int values = miniDialog.values;
				switch (currentID) {
				case R.id.linearlayout_factory_pq_colorsetting_ibc_enable:
					ibcEnableVal = miniDialog.currentIndex;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_r:
					ibcRVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_g:
					ibcGVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_b:
					ibcBVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_c:
					ibcCVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_m:
					ibcMVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_y:
					ibcYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_f:
					ibcFVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_gy:
					ibcGYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_by:
					ibcBYVal = values;
				break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_flesh:
					ibcFleshVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_reddish:
					ibcReddishVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_lip:
					ibcLipVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_hair:
					ibcHairVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_meat:
					ibcMeatVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ibc_dark:
					ibcDarkVal = values;
					break;
				default:
					break;
				}
				setActivityVisible();
				initView();
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