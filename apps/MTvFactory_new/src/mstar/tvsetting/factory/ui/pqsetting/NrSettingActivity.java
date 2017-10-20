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
import com.mstar.android.tv.TvFactoryManager;
import mstar.tvsetting.factory.until.Constant.PQ_DATA;

public class NrSettingActivity extends BaseActivity {
	private TextView textview_factory_pq_nrsetting_enable;
	private TextView textview_factory_pq_nrsetting_dnr_y;
	private TextView textview_factory_pq_nrsetting_dnr_c;
	private TextView textview_factory_pq_nrsetting_dnr_y_color_dep;
	private TextView textview_factory_pq_nrsetting_spf_snr_mr;
	private TextView textview_factory_pq_nrsetting_spf_snr;
	private String[] arrNrenable;
	private String[] arrDnr_y;
	private String[] arrDnr_c;
	private String[] arrDnr_y_color_dep;
	private String[] arrSpf_snr_mr;
	private String[] arrSpf_snr;
	private int Nrenable = 0;
	private int Dnr_y = 24;
	private int Dnr_c = 0;
	private int Dnr_y_color_dep = 0;
	private int Spf_snr_mr = 0;
	private int Spf_snr = 0;

	private MiniDialog.PqDataContent nrEnableData;
	private MiniDialog.PqDataContent dnr_y_data;
	private MiniDialog.PqDataContent dnr_c_data;
	private MiniDialog.PqDataContent dnr_y_color_dep_data;
	private MiniDialog.PqDataContent spf_snr_mr_data;
	private MiniDialog.PqDataContent spf_snr_data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nrsetting);

		nrEnableData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG, Constant.MAIN_PQ_IP_DNR_COM_Main);
		dnr_y_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG, Constant.MAIN_PQ_IP_DNR_Y_COM_Main);
		dnr_c_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG, Constant.MAIN_PQ_IP_DNR_C_COM_Main);
		dnr_y_color_dep_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG, Constant.MAIN_PQ_IP_DNR_Y_COLOR_DEP_COM_Main);
		spf_snr_mr_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG, Constant.MAIN_PQ_IP_SPF_SNR_MR_COM_Main);
		spf_snr_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG, Constant.MAIN_PQ_IP_SPF_SNR_COM_Main);

		int nrEnableCount = MiniDialog.getQmapTableNum(nrEnableData);//PQ_IP_DNR_Main
		arrNrenable = new String[nrEnableCount];
		for(int i=0;i<nrEnableCount;i++)
		  arrNrenable[i] = MiniDialog.getQmapTableName(nrEnableData,i);

		int ndr_YCount = MiniDialog.getQmapTableNum(dnr_y_data);//PQ_IP_DNR_Y_Main
		arrDnr_y = new String[ndr_YCount];
		for(int i=0;i<ndr_YCount;i++)
		  arrDnr_y[i] = MiniDialog.getQmapTableName(dnr_y_data, i);

		int dnr_CCount = MiniDialog.getQmapTableNum(dnr_y_data);//PQ_IP_DNR_C_Main
		arrDnr_c = new String[dnr_CCount];
		for(int i=0;i<dnr_CCount;i++)
		  arrDnr_c[i] = MiniDialog.getQmapTableName(dnr_y_data,i);

		int dnr_Y_color_depCount = MiniDialog.getQmapTableNum(dnr_y_color_dep_data); //PQ_IP_DNR_Y_COLOR_DEP_Main
		arrDnr_y_color_dep = new String[dnr_Y_color_depCount];
		for(int i=0;i<dnr_Y_color_depCount;i++)
		  arrDnr_y_color_dep[i] = MiniDialog.getQmapTableName(dnr_y_color_dep_data,i);

		int spf_snr_mrCount = MiniDialog.getQmapTableNum(spf_snr_mr_data); //PQ_IP_SPF_SNR_MR_Main
		arrSpf_snr_mr = new String[spf_snr_mrCount];
		for(int i=0;i<spf_snr_mrCount;i++)
		  arrSpf_snr_mr[i] = MiniDialog.getQmapTableName(spf_snr_mr_data,i);

		int spf_snrCount = MiniDialog.getQmapTableNum(spf_snr_data); //PQ_IP_SPF_SNR_Main
		arrSpf_snr = new String[spf_snrCount];
		for(int i=0;i<spf_snrCount;i++)
		  arrSpf_snr[i] = MiniDialog.getQmapTableName(spf_snr_data,i);
		  
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		textview_factory_pq_nrsetting_enable = (TextView) findViewById(R.id.textview_factory_pq_nrsetting_enableval);
		textview_factory_pq_nrsetting_dnr_y = (TextView) findViewById(R.id.textview_factory_pq_nrsetting_dnr_yval);
		textview_factory_pq_nrsetting_dnr_c = (TextView) findViewById(R.id.textview_factory_pq_nrsetting_dnr_cval);
		textview_factory_pq_nrsetting_dnr_y_color_dep = (TextView) findViewById(R.id.textview_factory_pq_nrsetting_dnr_y_color_depval);
		textview_factory_pq_nrsetting_spf_snr_mr = (TextView) findViewById(R.id.textview_factory_pq_nrsetting_spf_snr_mrval);
		textview_factory_pq_nrsetting_spf_snr = (TextView) findViewById(R.id.textview_factory_pq_nrsetting_spf_snrval);
		initView();
	}

	private void initView() {
//		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");
		
		Nrenable = MiniDialog.getQmapCurrentTableIdx(nrEnableData.pqMode, nrEnableData.pqType, nrEnableData.pqIpIndex);
		Dnr_y = MiniDialog.getQmapCurrentTableIdx(dnr_y_data.pqMode, dnr_y_data.pqType, dnr_y_data.pqIpIndex);
		Dnr_c = MiniDialog.getQmapCurrentTableIdx(dnr_c_data.pqMode, dnr_c_data.pqType, dnr_c_data.pqIpIndex);
		Dnr_y_color_dep = MiniDialog.getQmapCurrentTableIdx(dnr_y_color_dep_data.pqMode, dnr_y_color_dep_data.pqType, dnr_y_color_dep_data.pqIpIndex);
		Spf_snr_mr = MiniDialog.getQmapCurrentTableIdx(spf_snr_mr_data.pqMode, spf_snr_mr_data.pqType, spf_snr_mr_data.pqIpIndex);
		Spf_snr = MiniDialog.getQmapCurrentTableIdx(spf_snr_data.pqMode, spf_snr_data.pqType, spf_snr_data.pqIpIndex);

		textview_factory_pq_nrsetting_enable.setText(arrNrenable[Nrenable]);
		textview_factory_pq_nrsetting_dnr_y.setText(arrDnr_y[Dnr_y]);
		textview_factory_pq_nrsetting_dnr_c.setText(arrDnr_c[Dnr_c]);
		textview_factory_pq_nrsetting_dnr_y_color_dep.setText(arrDnr_y_color_dep[Dnr_y_color_dep]);
		textview_factory_pq_nrsetting_spf_snr_mr.setText(arrSpf_snr_mr[Spf_snr_mr]);
		textview_factory_pq_nrsetting_spf_snr.setText(arrSpf_snr[Spf_snr]);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentId = this.getCurrentFocus().getId();
		switch (keyCode) {
		case KeyEvent .KEYCODE_ENTER:
			String[] strings = null;
			int curIndex = 0;
			String name = "";
			int type = 0;
			switch (currentId) {
			case R.id.linearlayout_factory_pq_nrsetting_enable:
				strings = arrNrenable;
				curIndex = Nrenable;
				type = Constant.PQ_NR_SETTING_NR_ENABLE;
				name = getResources().getString(R.string.str_textview_factory_pq_nrsetting_enable);
				createDialog(strings, curIndex, name, currentId,type,
						nrEnableData.pqMode, nrEnableData.pqIpIndex, nrEnableData.pqType);
				break;
			case R.id.linearlayout_factory_pq_nrsetting_dnr_y:
				strings = arrDnr_y;
				type = Constant.PQ_NR_SETTING_DNR_Y;
				curIndex = Dnr_y;
				name = getResources().getString(R.string.str_textview_factory_pq_nrsetting_dnr_y);
				createDialog(strings, curIndex, name, currentId,type,
						dnr_y_data.pqMode, dnr_y_data.pqIpIndex, dnr_y_data.pqType);
				break;
			case R.id.linearlayout_factory_pq_nrsetting_dnr_c:
				strings = arrDnr_c;
				curIndex = Dnr_c;
				type = Constant.PQ_NR_SETTING_DNR_C;
				name = getResources().getString(R.string.str_textview_factory_pq_nrsetting_dnr_c);
				createDialog(strings, curIndex, name, currentId,type,
						dnr_c_data.pqMode, dnr_c_data.pqIpIndex, dnr_c_data.pqType);
				break;
			case R.id.linearlayout_factory_pq_nrsetting_dnr_y_color_dep:
				strings = arrDnr_y_color_dep;
				curIndex = Dnr_y_color_dep;
				type = Constant.PQ_NR_SETTING_DNR_Y_COLOR_DEP;
				name = getResources().getString(R.string.str_textview_factory_pq_nrsetting_dnr_y_color_dep);
				createDialog(strings, curIndex, name, currentId,type,
						dnr_y_color_dep_data.pqMode, dnr_y_color_dep_data.pqIpIndex, dnr_y_color_dep_data.pqType);
				break;
			case R.id.linearlayout_factory_pq_nrsetting_spf_snr_mr:
				strings = arrSpf_snr_mr;
				curIndex = Spf_snr_mr;
				type = Constant.PQ_NR_SETTING_SPF_SNR_MR;
				name = getResources().getString(R.string.str_textview_factory_pq_nrsetting_spf_snr_mr);
				createDialog(strings, curIndex, name, currentId,type,
						spf_snr_mr_data.pqMode, spf_snr_mr_data.pqIpIndex, spf_snr_mr_data.pqType);
				break;
			case R.id.linearlayout_factory_pq_nrsetting_spf_snr:
				strings = arrSpf_snr;
				curIndex = Spf_snr;
				type = Constant.PQ_NR_SETTING_SPF_SNR;
				name = getResources().getString(R.string.str_textview_factory_pq_nrsetting_spf_snr);
				createDialog(strings, curIndex, name, currentId,type,
						spf_snr_data.pqMode, spf_snr_data.pqIpIndex, spf_snr_data.pqType);
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
	public void createDialog(String[] strings,int curIndex,String name,final int currentID,int type,
							 int mode, int ipIndex, int pqtype){
		final MiniDialog miniDialog;
		if (mode == MiniDialog.MODE_QMAP)
			miniDialog = MiniDialog.createQmapDialog(strings, curIndex, name, type, ipIndex, pqtype);
		else
			miniDialog = MiniDialog.createRegAdjustDialog(curIndex, name, type);
		miniDialog.show(getFragmentManager(),"");
		
		miniDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				int values = miniDialog.currentIndex;
				switch (currentID) {
				case R.id.linearlayout_factory_pq_nrsetting_enable:
					Nrenable = values;
					break;
				case R.id.linearlayout_factory_pq_nrsetting_dnr_y:
					Dnr_y = values;
					break;
				case R.id.linearlayout_factory_pq_nrsetting_dnr_c:
					Dnr_c = values;
					break;
				case R.id.linearlayout_factory_pq_nrsetting_dnr_y_color_dep:
					Dnr_y_color_dep = values;
					break;
				case R.id.linearlayout_factory_pq_nrsetting_spf_snr_mr:
					Spf_snr_mr = values;
					break;
				case R.id.linearlayout_factory_pq_nrsetting_spf_snr:
					Spf_snr = values;
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
