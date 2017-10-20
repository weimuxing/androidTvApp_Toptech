package mstar.tvsetting.factory.ui.pqsetting;

import com.mstar.android.tv.TvCommonManager;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.ui.designmenu.BaseActivity;
import mstar.tvsetting.factory.until.Constant;
import com.mstar.android.tv.TvFactoryManager;
import mstar.tvsetting.factory.until.Constant.PQ_DATA;

public class CTIActivity extends BaseActivity {
	private static final String TAG = "CTIActivity";
	
	private TextView textview_factory_pq_comb_83;
	private TextView textview_factory_pq_ctisetting_vip_post_cti;
	private TextView textview_factory_pq_ctisetting_vip_post_cti_coef;
	private TextView textview_factory_pq_ctisetting_precti;
	private TextView textview_factory_pq_ctisetting_ycdelay;
	private String[] arrVip_post_cti;
	private String[] arrPrecti;
	private String[] arrYcdelay;
	private int Comb_83 = 0;
	private int vip_post_cti =2 ;
	private int vip_post_cti_coef= 0;
	private int precti = 6;
	private int ycdelay =0;
	
	private MiniDialog.PqDataContent comb_83_data;
	private MiniDialog.PqDataContent vip_post_cti_data;
	private MiniDialog.PqDataContent vip_post_cti_coef_data;
	private MiniDialog.PqDataContent precti_data;
	private MiniDialog.PqDataContent ycdelay_data;
	
	private MiniDialog miniDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cti);

		initContentData();
		//PQ_IP_VIP_Post_CTI_COM_Main
		int postCtiCount = MiniDialog.getQmapTableNum(vip_post_cti_data);
		arrVip_post_cti = new String[postCtiCount];
		Log.d("bingo","postCtiCount==="+postCtiCount);
		for(int i=0;i<postCtiCount;i++)
			arrVip_post_cti[i]=MiniDialog.getQmapTableName(vip_post_cti_data, i);

		//PQ_IP_PreCTI_COM_Main
		int preCtiCount = MiniDialog.getQmapTableNum(precti_data);
		Log.d("bingo","preCtiCount==="+preCtiCount);
		arrPrecti = new String[preCtiCount];
		for(int i=0;i<preCtiCount;i++) {
			arrPrecti[i]=MiniDialog.getQmapTableName(precti_data,i);
			}

		int ycDelayCtiCount = MiniDialog.getQmapTableNum(ycdelay_data);
		arrYcdelay = new String[ycDelayCtiCount];
		for(int i=0;i<ycDelayCtiCount;i++)
			arrYcdelay[i]=MiniDialog.getQmapTableName(ycdelay_data,i);
		
		findView();
	}



	private void findView() {
		// TODO Auto-generated method stub
		textview_factory_pq_comb_83 = (TextView) findViewById(R.id.textview_factory_pq_comb_83val);
		textview_factory_pq_ctisetting_vip_post_cti = (TextView) findViewById(R.id.textview_factory_pq_ctisetting_vip_post_ctival);
		textview_factory_pq_ctisetting_vip_post_cti_coef = (TextView) findViewById(R.id.textview_factory_pq_ctisetting_vip_post_cti_coefval);
		textview_factory_pq_ctisetting_precti = (TextView) findViewById(R.id.textview_factory_pq_ctisetting_prectival);
		textview_factory_pq_ctisetting_ycdelay = (TextView) findViewById(R.id.textview_factory_pq_ctisetting_ycdelayval);
		initView();
	}

	private void initContentData() {
		comb_83_data = new MiniDialog.PqDataContent(MiniDialog.MODE_REGISTER, 0, 0);
		vip_post_cti_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
				Constant.MAIN_PQ_IP_VIP_Post_CTI_COM_Main);
		vip_post_cti_coef_data = new MiniDialog.PqDataContent(MiniDialog.MODE_REGISTER, 0, 0);
		precti_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
				Constant.MAIN_PQ_IP_PreCTI_COM_Main);
		ycdelay_data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
				Constant.MAIN_PQ_IP_YCdelay_COM_Main);
	}

	private void initView() {
//		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");
		
		Comb_83 = MiniDialog.getRegValueById(Constant.PQ_CTI_SETTING_COMB_83);
		vip_post_cti_coef = MiniDialog.getRegValueById(Constant.PQ_CTI_SETTING_POST_CTI_COEF);
		
		vip_post_cti = MiniDialog.getQmapCurrentTableIdx(vip_post_cti_data);
		precti = MiniDialog.getQmapCurrentTableIdx(precti_data);
		ycdelay = MiniDialog.getQmapCurrentTableIdx(ycdelay_data);
		
		Log.i(TAG, "Comb_83:"+Comb_83);
		Log.i(TAG, "vip_post_cti:"+vip_post_cti);
		Log.i(TAG, "vip_post_cti_coef:"+vip_post_cti_coef);
		Log.i(TAG, "precti:"+precti);
		Log.i(TAG, "ycdelay:"+ycdelay);
		
		textview_factory_pq_comb_83.setText("0x" + Integer.toHexString(Comb_83).toUpperCase());
		textview_factory_pq_ctisetting_vip_post_cti.setText(arrVip_post_cti[vip_post_cti]);
		textview_factory_pq_ctisetting_vip_post_cti_coef.setText("0x" + Integer.toHexString(vip_post_cti_coef).toUpperCase());
		textview_factory_pq_ctisetting_precti.setText(arrPrecti[precti]);
		textview_factory_pq_ctisetting_ycdelay.setText(arrYcdelay[ycdelay]);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		boolean bRet = true;
		int currentId = this.getCurrentFocus().getId();
		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
			String[] strings= null;
			int curValue = 0;
			String name = "";
			int type = 0;
			
			switch (currentId) {
			case R.id.linearlayout_factory_pq_ctisetting_comb_83:
				curValue = Comb_83;
				name = getString(R.string.str_textview_factory_pq_ctisetting_comb_83);
				type = Constant.PQ_CTI_SETTING_COMB_83;
				createDialog(null, curValue, name, currentId,type, comb_83_data);
				break;
			case R.id.linearlayout_factory_pq_ctisetting_vip_post_cti:
				strings = arrVip_post_cti;
				curValue = vip_post_cti;
				type = Constant.PQ_CTI_SETTING_POST_CTI;
				name = getResources().getString(R.string.str_textview_factory_pq_ctisetting_vip_post_cti);
				createDialog(strings, curValue, name, currentId,type, vip_post_cti_data);
				break;
			case R.id.linearlayout_factory_pq_ctisetting_vip_post_cti_coef:
				curValue = vip_post_cti_coef;
				type = Constant.PQ_CTI_SETTING_POST_CTI_COEF;
				name = getResources().getString(R.string.str_textview_factory_pq_ctisetting_vip_post_cti_coef);
				createDialog(null, curValue, name, currentId, type, vip_post_cti_coef_data);
				break;
			case R.id.linearlayout_factory_pq_ctisetting_precti:
				strings = arrPrecti;
				curValue = precti;
				type = Constant.PQ_CTI_SETTING_PRECTI;
				name = getResources().getString(R.string.str_textview_factory_pq_ctisetting_precti);
				createDialog(strings, curValue, name, currentId,type, precti_data);
				break;
			case R.id.linearlayout_factory_pq_ctisetting_ycdelay:
				strings = arrYcdelay;
				curValue = ycdelay;
				type = Constant.PQ_CTI_SETTING_YCDELAY;
				name = getResources().getString(R.string.str_textview_factory_pq_ctisetting_ycdelay);
				createDialog(strings, curValue, name, currentId,type, ycdelay_data);
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
	
	public void createDialog(final String[] strings,int curIndex,String name,final int currentID,int type,
			MiniDialog.PqDataContent pqdata){
		final MiniDialog miniDialog;
		if (pqdata.pqMode == MiniDialog.MODE_QMAP)
			miniDialog = MiniDialog.createQmapDialog(strings, curIndex, name, type, pqdata.pqIpIndex, pqdata.pqType);
		else if (pqdata.pqMode == MiniDialog.MODE_REGISTER)
			miniDialog = MiniDialog.createRegAdjustDialog(curIndex, name, type);
		else
			miniDialog = MiniDialog.createRegOnoffDialog(strings, curIndex, name, type);
			
		miniDialog.show(getFragmentManager(),"");
		
		miniDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				int values = 0;
				if (strings != null)
					values = miniDialog.currentIndex;
				else
					values = miniDialog.values;
					
				switch (currentID) {
				case R.id.linearlayout_factory_pq_ctisetting_comb_83:
					Comb_83 = values;
					break;
				case R.id.linearlayout_factory_pq_ctisetting_vip_post_cti:
					vip_post_cti = values;
					break;
				case R.id.linearlayout_factory_pq_ctisetting_vip_post_cti_coef:
					vip_post_cti_coef = values;
					break;
				case R.id.linearlayout_factory_pq_ctisetting_precti:
					precti = values;
					break;
				case R.id.linearlayout_factory_pq_ctisetting_ycdelay:
					ycdelay = values;
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
}
