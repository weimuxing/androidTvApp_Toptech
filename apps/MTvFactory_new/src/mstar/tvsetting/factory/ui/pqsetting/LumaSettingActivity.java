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


public class LumaSettingActivity extends BaseActivity {

	private TextView textview_pq_lumasetting_preyoffset;
	private TextView textview_pq_lumasetting_preygain;
	private TextView textview_pq_lumasetting_cvbs_combcontrast;
	private TextView textview_pq_lumasetting_cvbs_combbrightness;
	private TextView textview_pq_lumasetting_cvbs_combsaturation;
	private int PreYoffset= 2;
	private int PreYGain = 3;
	private int Contrast = 128;
	private int Brightness = 128;
	private int Saturation = 128;
	private MiniDialog miniDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lumasetting);
		findView();
	}

	private void findView() {
		textview_pq_lumasetting_preyoffset = (TextView) findViewById(R.id.textview_factory_pq_lumasetting_pre_yoffsetval);
		textview_pq_lumasetting_preygain = (TextView) findViewById(R.id.textview_factory_pq_lumasetting_pre_ygainval);
		textview_pq_lumasetting_cvbs_combcontrast = (TextView) findViewById(R.id.str_textview_factory_pq_lumasetting_cvbs_combcontrastval);
		textview_pq_lumasetting_cvbs_combbrightness = (TextView) findViewById(R.id.textview_factory_pq_lumasetting_cvbs_combbrightnessval);
		textview_pq_lumasetting_cvbs_combsaturation = (TextView) findViewById(R.id.textview_factory_pq_lummasetting_cvbs_combsaturationval);
		initView();
	}
	
	private void initView(){
		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");
		
		PreYoffset = MiniDialog.getRegValueById(Constant.PQ_LUMA_SETTING_PRE_YOFFSET);
		PreYGain = MiniDialog.getRegValueById(Constant.PQ_LUMA_SETTING_PRE_YGAIN);
		Contrast = MiniDialog.getRegValueById(Constant.PQ_LUMA_SETTING_COMB_CONTRAST);
		Brightness = MiniDialog.getRegValueById(Constant.PQ_LUMA_SETTING_COMB_BRIGHTNESS);
		Saturation = MiniDialog.getRegValueById(Constant.PQ_LUMA_SETTING_COMB_SATURATION);
		
		textview_pq_lumasetting_preyoffset.setText("0x" + Integer.toHexString(PreYoffset).toUpperCase());
		textview_pq_lumasetting_preygain.setText("0x" + Integer.toHexString(PreYGain).toUpperCase());
		textview_pq_lumasetting_cvbs_combcontrast.setText("0x" + Integer.toHexString(Contrast).toUpperCase());
		textview_pq_lumasetting_cvbs_combbrightness.setText("0x" + Integer.toHexString(Brightness).toUpperCase());
		textview_pq_lumasetting_cvbs_combsaturation.setText("0x" + Integer.toHexString(Saturation).toUpperCase());
	}

	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		boolean bRet = true;
		int currentId = this.getCurrentFocus().getId();
		switch (KeyCode) {
		case KeyEvent.KEYCODE_ENTER:
			int tmp = 0;
			String  strName ="";
			String[] strings= null;
			int type = 0;
			switch (currentId) {
			case R.id.linearlayout_factory_pq_lummasetting_preyoffset:
				tmp = PreYoffset;
				strName = getString(R.string.str_textview_factory_pq_lumasetting_pre_yoffset);
				type = Constant.PQ_LUMA_SETTING_PRE_YOFFSET;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName,type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_lummasetting_ygain:
				tmp = PreYGain;
				strName = getString(R.string.str_textview_factory_pq_lumasetting_pre_ygain);
				type = Constant.PQ_LUMA_SETTING_PRE_YGAIN;
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName,type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_lumasetting_cvbs_comcontrast:
				tmp = Contrast;
				type = Constant.PQ_LUMA_SETTING_COMB_CONTRAST;
				strName = getString(R.string.str_textview_factory_pq_lumasetting_cvbs_comb_contrast);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName,type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_lumasetting_cvbs_combbrightness:
				tmp = Brightness;
				type = Constant.PQ_LUMA_SETTING_COMB_BRIGHTNESS;
				strName = getString(R.string.str_textview_factory_pq_lumasetting_cvbs_comb_brightness);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName,type);
				miniDialog.show(getFragmentManager(), "");
				createDialog(currentId);
				break;
			case R.id.linearlayout_factory_pq_lumasetting_cvbs_combsaturation:
				tmp = Saturation;
				type = Constant.PQ_LUMA_SETTING_COMB_SATURATION;
				strName = getString(R.string.str_textview_factory_pq_lumasetting_cvbs_comb_saturation);
				miniDialog = MiniDialog.createRegAdjustDialog(tmp, strName,type);
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
				case R.id.linearlayout_factory_pq_lummasetting_preyoffset:
					PreYoffset = miniDialog.currentIndex;
					break;
				case R.id.linearlayout_factory_pq_lummasetting_ygain:
					PreYGain = miniDialog.currentIndex;
					break;
				case R.id.linearlayout_factory_pq_lumasetting_cvbs_comcontrast:
					Contrast = values;
					break;
				case R.id.linearlayout_factory_pq_lumasetting_cvbs_combbrightness:
					Brightness = values;
					break;
				case R.id.linearlayout_factory_pq_lumasetting_cvbs_combsaturation:
					Saturation = values;
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
