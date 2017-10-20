package mstar.tvsetting.factory.ui.pqsetting;

import com.mstar.android.tv.TvCommonManager;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.designmenu.BaseActivity;
import mstar.tvsetting.factory.until.Constant;
import mstar.tvsetting.factory.until.Constant.PQ_DATA;


public class IHCActivity extends BaseActivity {

	private TextView textview_pq_colorsetting_ihc_enable;
	private TextView textview_pq_colorsetting_ihc_r;
	private TextView textview_pq_colorsetting_ihc_g;
	private TextView textview_pq_colorsetting_ihc_b;
	private TextView textview_pq_colorsetting_ihc_c;
	private TextView textview_pq_colorsetting_ihc_m;
	private TextView textview_pq_colorsetting_ihc_y;
	private TextView textview_pq_colorsetting_ihc_f;
	private TextView textview_pq_colorsetting_ihc_gy;
	private TextView textview_pq_colorsetting_ihc_by;
	private TextView textview_pq_colorsetting_ihc_flesh;
	private TextView textview_pq_colorsetting_ihc_reddish;
	private TextView textview_pq_colorsetting_ihc_lip;
	private TextView textview_pq_colorsetting_ihc_hair;
	private TextView textview_pq_colorsetting_ihc_meat;
	private TextView textview_pq_colorsetting_ihc_dark;
	private String[] arr_ihcEnable;
	private int  ihcEnableVal = 1;
	private int ihcRVal = 4;
	private int ihcGVal = 10;
	private int ihcBVal = 9;
	private int ihcCVal = 6;
	private int ihcMVal = 6;
	private int ihcYVal = 3;
	private int ihcFVal = 5;
	private int ihcGYVal = 4;
	private int ihcBYVal = 4;
	private int ihcFleshVal = 6;
	private int ihcReddishVal = 6;
	private int ihcLipVal = 3;
	private int ihcHairVal = 5;
	private int ihcMeatVal = 4;
	private int ihcDarkVal = 4;
	private MiniDialog miniDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ihc);
//		arr_ihcEnable = getResources().getStringArray(R.array.str_arr_pq_color_setting_ihc_enable);
		findView();
	}

	private void findView() {

		textview_pq_colorsetting_ihc_enable = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_enableval);
		textview_pq_colorsetting_ihc_r = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_rval);
		textview_pq_colorsetting_ihc_g = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_gval);
		textview_pq_colorsetting_ihc_b = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_bval);
		textview_pq_colorsetting_ihc_c = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_cval);
		textview_pq_colorsetting_ihc_m = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_mval);
		textview_pq_colorsetting_ihc_y = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_yval);
		textview_pq_colorsetting_ihc_f = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_fval);
		textview_pq_colorsetting_ihc_gy = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_gyval);
		textview_pq_colorsetting_ihc_by = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_byval);
        textview_pq_colorsetting_ihc_flesh = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_fleshval);
		textview_pq_colorsetting_ihc_reddish = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_reddishval);
		textview_pq_colorsetting_ihc_lip = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_lipval);
		textview_pq_colorsetting_ihc_hair = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_hairval);
		textview_pq_colorsetting_ihc_meat = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_meatval);
		textview_pq_colorsetting_ihc_dark = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_ihc_darkval);
		initView();
	}

	private void initView() {
		arr_ihcEnable = new String[2];
		arr_ihcEnable[0]="OFF";
		arr_ihcEnable[1]="ON";
//		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");
//
//		ihcEnableVal = retval[25];
		if (MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_ENABLE) != 0)
			ihcEnableVal = 1;
		else
			ihcEnableVal = 0;

		ihcRVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_R);//retval[PQ_DATA.IHC_R_Data] & 0x0F;
		ihcGVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_G);//retval[PQ_DATA.IHC_G_Data] & 0x0F;
		ihcBVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_B);//retval[PQ_DATA.IHC_B_Data] & 0x0F;
		ihcCVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_C);//retval[PQ_DATA.IHC_C_Data] & 0x0F;
		ihcMVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_M);//retval[PQ_DATA.IHC_M_Data] & 0x0F;
		ihcYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_Y);//retval[PQ_DATA.IHC_Y_Data] & 0x0F;
		ihcFVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_F);//retval[PQ_DATA.IHC_F_Data] & 0x0F;
		ihcGYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_GY);//retval[PQ_DATA.IHC_GY_Data] & 0x0F;
		ihcBYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_BY);//retval[PQ_DATA.IHC_BY_Data] & 0x0F;
		ihcFleshVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_FLESH);//retval[PQ_DATA.IHC_Flesh_Data] & 0x0F;
		ihcReddishVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_REDDISH);//retval[PQ_DATA.IHC_Reddish_Data] & 0x0F;
		ihcLipVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_LIP);//retval[PQ_DATA.IHC_Lip_Data] & 0x0F;
		ihcHairVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_HAIR);//retval[PQ_DATA.IHC_Hair_Data] & 0x0F;
		ihcMeatVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_MEAT);//retval[PQ_DATA.IHC_Meat_Data] & 0x0F;
		ihcDarkVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_IHC_DARK);//retval[PQ_DATA.IHC_Dark_Data] & 0x0F;

		textview_pq_colorsetting_ihc_enable.setText(arr_ihcEnable[ihcEnableVal]);
		textview_pq_colorsetting_ihc_r.setText("0x"	+ Integer.toHexString(ihcRVal).toUpperCase());
		textview_pq_colorsetting_ihc_g.setText("0x"	+ Integer.toHexString(ihcGVal).toUpperCase());
		textview_pq_colorsetting_ihc_b.setText("0x"	+ Integer.toHexString(ihcBVal).toUpperCase());
		textview_pq_colorsetting_ihc_c.setText("0x"	+ Integer.toHexString(ihcCVal).toUpperCase());
		textview_pq_colorsetting_ihc_m.setText("0x"	+ Integer.toHexString(ihcMVal).toUpperCase());
		textview_pq_colorsetting_ihc_y.setText("0x"	+ Integer.toHexString(ihcYVal).toUpperCase());
		textview_pq_colorsetting_ihc_f.setText("0x"	+ Integer.toHexString(ihcFVal).toUpperCase());
		textview_pq_colorsetting_ihc_gy.setText("0x" + Integer.toHexString(ihcGYVal).toUpperCase());
		textview_pq_colorsetting_ihc_by.setText("0x" + Integer.toHexString(ihcBYVal).toUpperCase());
		textview_pq_colorsetting_ihc_flesh.setText("0x"	+ Integer.toHexString(ihcFleshVal).toUpperCase());
		textview_pq_colorsetting_ihc_reddish.setText("0x"	+ Integer.toHexString(ihcReddishVal).toUpperCase());
		textview_pq_colorsetting_ihc_lip.setText("0x"	+ Integer.toHexString(ihcLipVal).toUpperCase());
		textview_pq_colorsetting_ihc_hair.setText("0x"	+ Integer.toHexString(ihcHairVal).toUpperCase());
		textview_pq_colorsetting_ihc_meat.setText("0x" + Integer.toHexString(ihcMeatVal).toUpperCase());
		textview_pq_colorsetting_ihc_dark.setText("0x" + Integer.toHexString(ihcDarkVal).toUpperCase());
		
	}

	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		boolean bRet = true;
		int currentId = this.getCurrentFocus().getId();
		switch (KeyCode) {
		case KeyEvent.KEYCODE_ENTER:
			int tmp = 0;
			String  strName ="";
			int type = 0;
			switch (currentId) {
			case R.id.linearlayout_factory_pq_colorsetting_ihc_enable:
				strName = getResources().getString(R.string.str_textview_factory_pq_colorsetting_ihc_enable);
				type = Constant.PQ_COLOR_SETTING_IHC_ENABLE;
				miniDialog = MiniDialog.createRegOnoffDialog(arr_ihcEnable, ihcEnableVal, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_r:
				tmp = ihcRVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_r);
				type = Constant.PQ_COLOR_SETTING_IHC_R;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_g:
				tmp = ihcGVal;
				type = Constant.PQ_COLOR_SETTING_IHC_G;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_g);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_b:
				tmp = ihcBVal;
				type = Constant.PQ_COLOR_SETTING_IHC_B;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_b);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_c:
				tmp = ihcCVal;
				type = Constant.PQ_COLOR_SETTING_IHC_C;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_c);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_m:
				tmp = ihcMVal;
				type = Constant.PQ_COLOR_SETTING_IHC_M;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_m);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_y:
				tmp = ihcYVal;
				type = Constant.PQ_COLOR_SETTING_IHC_Y;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_y);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_f:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_f);
				tmp = ihcFVal;
				type = Constant.PQ_COLOR_SETTING_IHC_F;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_gy:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_gy);
				tmp = ihcGYVal;
				type = Constant.PQ_COLOR_SETTING_IHC_GY;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_by:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_by);
				tmp = ihcBYVal;
				type = Constant.PQ_COLOR_SETTING_IHC_BY;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
		    	case R.id.linearlayout_factory_pq_colorsetting_ihc_flesh:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_flesh);
				type = Constant.PQ_COLOR_SETTING_IHC_FLESH;
				tmp = ihcFleshVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_reddish:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_reddish);
				type = Constant.PQ_COLOR_SETTING_IHC_REDDISH;
				tmp = ihcReddishVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_lip:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_lip);
				type = Constant.PQ_COLOR_SETTING_IHC_LIP;
				tmp = ihcLipVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_hair:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_hair);
				type = Constant.PQ_COLOR_SETTING_IHC_HAIR;
				tmp = ihcHairVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_meat:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_meat);
				type = Constant.PQ_COLOR_SETTING_IHC_MEAT;
				tmp = ihcMeatVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_ihc_dark:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_ihc_dark);
				type = Constant.PQ_COLOR_SETTING_IHC_DARK;
				tmp = ihcDarkVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
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
				case R.id.linearlayout_factory_pq_colorsetting_ihc_enable:
					ihcEnableVal = miniDialog.currentIndex;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_r:
					ihcRVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_g:
					ihcGVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_b:
					ihcBVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_c:
					ihcCVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_m:
					ihcMVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_y:
					ihcYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_f:
					ihcFVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_gy:
					ihcGYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_by:
					ihcBYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_flesh:
					ihcFleshVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_reddish:
					ihcReddishVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_lip:
					ihcLipVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_hair:
					ihcHairVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_meat:
					ihcMeatVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_ihc_dark:
					ihcDarkVal = values;
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