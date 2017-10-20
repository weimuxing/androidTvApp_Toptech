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


public class ICCActivity extends BaseActivity {

	private TextView textview_pq_colorsetting_icc_enable;
	private TextView textview_pq_colorsetting_icc_r;
	private TextView textview_pq_colorsetting_icc_g;
	private TextView textview_pq_colorsetting_icc_b;
	private TextView textview_pq_colorsetting_icc_c;
	private TextView textview_pq_colorsetting_icc_m;
	private TextView textview_pq_colorsetting_icc_y;
	private TextView textview_pq_colorsetting_icc_f;
	private TextView textview_pq_colorsetting_icc_gy;
	private TextView textview_pq_colorsetting_icc_by;
	private TextView textview_pq_colorsetting_icc_flesh;
	private TextView textview_pq_colorsetting_icc_reddish;
	private TextView textview_pq_colorsetting_icc_lip;
	private TextView textview_pq_colorsetting_icc_hair;
	private TextView textview_pq_colorsetting_icc_meat;
	private TextView textview_pq_colorsetting_icc_dark;
	private String[] arr_iccEnable;
	private int iccEnable = 1;
	private int iccRVal = 4;
	private int iccGVal = 10;
	private int iccBVal = 9;
	private int iccCVal = 6;
	private int iccMVal = 6;
	private int iccYVal = 3;
	private int iccFVal = 5;
	private int iccGYVal = 4;
	private int iccBYVal = 4;
	private int iccFleshVal = 6;
	private int iccReddishVal = 6;
	private int iccLipVal = 3;
	private int iccHairVal = 5;
	private int iccMeatVal = 4;
	private int iccDarkVal = 4;
	private MiniDialog miniDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.icc);
//		arr_iccEnable = getResources().getStringArray(R.array.str_arr_pq_color_setting_icc_enable);
		findView();
	}

	private void findView() {
		textview_pq_colorsetting_icc_enable = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_enableval);
		textview_pq_colorsetting_icc_r = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_rval);
		textview_pq_colorsetting_icc_g = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_gval);
		textview_pq_colorsetting_icc_b = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_bval);
		textview_pq_colorsetting_icc_c = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_cval);
		textview_pq_colorsetting_icc_m = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_mval);
		textview_pq_colorsetting_icc_y = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_yval);
		textview_pq_colorsetting_icc_f = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_fval);
		textview_pq_colorsetting_icc_gy = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_gyval);
		textview_pq_colorsetting_icc_by = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_byval);
		textview_pq_colorsetting_icc_flesh = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_fleshval);
		textview_pq_colorsetting_icc_reddish = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_reddishval);
		textview_pq_colorsetting_icc_lip = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_lipval);
		textview_pq_colorsetting_icc_hair = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_hairval);
		textview_pq_colorsetting_icc_meat = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_meatval);
		textview_pq_colorsetting_icc_dark = (TextView) findViewById(R.id.textview_factory_pq_colorsetting_icc_darkval);
		initView();
	}

	private void initView() {
		arr_iccEnable = new String[2];
		arr_iccEnable[0]="OFF";
		arr_iccEnable[1]="ON";
//		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");
		if (MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_ENABLE) != 0)
			iccEnable = 1;
		else
			iccEnable = 0;
		
		iccRVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_R);//retval[PQ_DATA.ICC_R_Data] & 0x0F;
		iccGVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_G);//retval[PQ_DATA.ICC_G_Data] & 0x0F;
		iccBVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_B);//retval[PQ_DATA.ICC_B_Data] & 0x0F;
		iccCVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_C);//retval[PQ_DATA.ICC_C_Data] & 0x0F;
		iccMVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_M);//retval[PQ_DATA.ICC_M_Data] & 0x0F;
		iccYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_Y);//retval[PQ_DATA.ICC_Y_Data] & 0x0F;
		iccFVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_F);//retval[PQ_DATA.ICC_F_Data] & 0x0F;
		iccGYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_GY);//retval[PQ_DATA.ICC_GY_Data] & 0x0F;
		iccBYVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_BY);//retval[PQ_DATA.ICC_BY_Data] & 0x0F;
		iccFleshVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_FLESH);//retval[PQ_DATA.ICC_Flesh_Data] & 0x0F;
		iccReddishVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_REDDISH);//retval[PQ_DATA.ICC_Reddish_Data] & 0x0F;
		iccLipVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_LIP);//retval[PQ_DATA.ICC_Lip_Data] & 0x0F;
		iccHairVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_HAIR);//retval[PQ_DATA.ICC_Hair_Data] & 0x0F;
		iccMeatVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_MEAT);//retval[PQ_DATA.ICC_Meat_Data] & 0x0F;
		iccDarkVal = MiniDialog.getRegValueById(Constant.PQ_COLOR_SETTING_ICC_DARK);//retval[PQ_DATA.ICC_Dark_Data] & 0x0F;

		textview_pq_colorsetting_icc_enable.setText(arr_iccEnable[iccEnable]);
		textview_pq_colorsetting_icc_r.setText(toHexString(iccRVal));
		textview_pq_colorsetting_icc_g.setText(toHexString(iccGVal));
		textview_pq_colorsetting_icc_b.setText(toHexString(iccBVal));
		textview_pq_colorsetting_icc_c.setText(toHexString(iccCVal));
		textview_pq_colorsetting_icc_m.setText(toHexString(iccMVal));
		textview_pq_colorsetting_icc_y.setText(toHexString(iccYVal));
		textview_pq_colorsetting_icc_f.setText(toHexString(iccFVal));
		textview_pq_colorsetting_icc_gy.setText(toHexString(iccGYVal));
		textview_pq_colorsetting_icc_by.setText(toHexString(iccBYVal));
		textview_pq_colorsetting_icc_flesh.setText(toHexString(iccFleshVal));
		textview_pq_colorsetting_icc_reddish.setText(toHexString(iccReddishVal));
		textview_pq_colorsetting_icc_lip.setText(toHexString(iccLipVal));
		textview_pq_colorsetting_icc_hair.setText(toHexString(iccHairVal));
		textview_pq_colorsetting_icc_meat.setText(toHexString(iccMeatVal));
		textview_pq_colorsetting_icc_dark.setText(toHexString(iccDarkVal));
		
	}

	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		boolean bRet = true;
		int currentId = this.getCurrentFocus().getId();
		switch (KeyCode) {
		case KeyEvent.KEYCODE_ENTER:
			int tmp = 0;
			String  strName ="";
			int type =0;
			String[] strings = null;
			switch (currentId) {
			case R.id.linearlayout_factory_pq_colorsetting_icc_enable:
				tmp = iccEnable;
				strings =arr_iccEnable ;
				strName = getResources().getString(R.string.str_textview_factory_pq_colorsetting_icc_enable);
				type = Constant.PQ_COLOR_SETTING_ICC_ENABLE;
				miniDialog = MiniDialog.createRegOnoffDialog(strings, tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_r:
				tmp = iccRVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_r);
				type = Constant.PQ_COLOR_SETTING_ICC_R;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_g:
				tmp = iccGVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_g);
				type = Constant.PQ_COLOR_SETTING_ICC_G;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_b:
				tmp = iccBVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_b);
				type = Constant.PQ_COLOR_SETTING_ICC_B;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_c:
				tmp = iccCVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_c);
				type = Constant.PQ_COLOR_SETTING_ICC_C;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_m:
				tmp = iccMVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_m);
				type = Constant.PQ_COLOR_SETTING_ICC_M;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_y:
				tmp = iccYVal;
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_y);
				type = Constant.PQ_COLOR_SETTING_ICC_Y;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_f:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_f);
				type = Constant.PQ_COLOR_SETTING_ICC_F;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				tmp = iccFVal;
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_gy:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_gy);
				type = Constant.PQ_COLOR_SETTING_ICC_GY;
				tmp = iccGYVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_by:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_by);
				type = Constant.PQ_COLOR_SETTING_ICC_BY;
				tmp = iccBYVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_flesh:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_flesh);
				type = Constant.PQ_COLOR_SETTING_ICC_FLESH;
				tmp = iccFleshVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_reddish:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_reddish);
				type = Constant.PQ_COLOR_SETTING_ICC_REDDISH;
				tmp = iccReddishVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_lip:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_lip);
				type = Constant.PQ_COLOR_SETTING_ICC_LIP;
				tmp = iccLipVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_hair:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_hair);
				type = Constant.PQ_COLOR_SETTING_ICC_HAIR;
				tmp = iccHairVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_meat:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_meat);
				type = Constant.PQ_COLOR_SETTING_ICC_MEAT;
				tmp = iccMeatVal;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName, type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_colorsetting_icc_dark:
				strName = getString(R.string.str_textview_factory_pq_colorsetting_icc_dark);
				type = Constant.PQ_COLOR_SETTING_ICC_DARK;
				tmp = iccDarkVal;
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
				case R.id.linearlayout_factory_pq_colorsetting_icc_enable:
					iccEnable = miniDialog.currentIndex;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_r:
					iccRVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_g:
					iccGVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_b:
					iccBVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_c:
					iccCVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_m:
					iccMVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_y:
					iccYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_f:
					iccFVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_gy:
					iccGYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_by:
					iccBYVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_flesh:
					iccFleshVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_reddish:
					iccReddishVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_lip:
					iccLipVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_hair:
					iccHairVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_meat:
					iccMeatVal = values;
					break;
				case R.id.linearlayout_factory_pq_colorsetting_icc_dark:
					iccDarkVal = values;
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
			setActivityInvisible();
			}
		});
	}

	String toHexString(int value) {
		StringBuilder ret = new StringBuilder("");

		if (value < 0) {
			value = -value;
			ret.append("-");
		}

		ret.append("0x").append(Integer.toHexString(value).toUpperCase());

		return ret.toString();
	}

}
