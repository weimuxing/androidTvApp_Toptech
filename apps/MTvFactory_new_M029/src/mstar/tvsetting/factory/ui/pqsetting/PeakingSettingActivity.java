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
import android.util.Log;
import com.mstar.android.tv.TvFactoryManager;
import mstar.tvsetting.factory.until.Constant.PQ_DATA;


public class PeakingSettingActivity extends BaseActivity {

	private TextView textview_pq_peakingsetting_peaking;
	private TextView textview_pq_peakingsetting_band;
	private TextView textview_pq_peakingsetting_pcoring;
	private TextView textview_pq_peakingsetting_pcoring_ad_c;
	private TextView textview_pq_peakingsetting_gain;
	private TextView textview_pq_peakingsetting_gain_ad_c;
	private TextView textview_pq_peakingsetting_hlpf;
	private TextView textview_pq_peakingsetting_sram1;
	private TextView textview_pq_peakingsetting_sram2;
	private String[] arrPeaking;
	private String[] arrPeaking_band;
	private String[] arrPeaking_pcoring;
	
	private String[] arrPeaking_hlpf;
	private String[] arrSram1;
	private String[] arrSram2;
	private String[] arrPeaking_Gain_ad_c;
	private String[] arrPeaking_Pcoring_ad_c;
	private String[] arrPeakingGain;
	private int peaking = 0;
	private int band = 27;
	private int pcoring = 3;
	private int gain = 3;
	private int hlpf = 7;
	private int gain_ad_c = 2;
	private int pcoring_ad_c = 2;
	private int sram1 = 3;
	private int sram2 = 3;
    private TvFactoryManager mTvFactoryManager = null;

	private MiniDialog.PqDataContent peakingData;
	private MiniDialog.PqDataContent bandData;
	private MiniDialog.PqDataContent pCoringData;
	private MiniDialog.PqDataContent pCoringAdCData;
	private MiniDialog.PqDataContent gainData;
	private MiniDialog.PqDataContent gainAdCData;
	private MiniDialog.PqDataContent hlpfData;
	private MiniDialog.PqDataContent sram1Data;
	private MiniDialog.PqDataContent sram2Data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peakingsetting);

		peakingData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_VIP_Peaking_COM_Main);
		bandData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_VIP_Peaking_band_COM_Main);
		pCoringData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_VIP_Peaking_Pcoring_COM_Main);
		pCoringAdCData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_VIP_Peaking_Pcoring_ad_C_COM_Main);
		gainData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_VIP_Peaking_gain_COM_Main);
		gainAdCData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_VIP_Peaking_gain_ad_C_COM_Main);
		hlpfData = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_VIP_HLPF_COM_Main);
		sram1Data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_SRAM1_COM_Main);
		sram2Data = new MiniDialog.PqDataContent(MiniDialog.MODE_QMAP, MiniDialog.PQTYPE_BIG,
						Constant.MAIN_PQ_IP_SRAM2_COM_Main);

		mTvFactoryManager = TvFactoryManager.getInstance();
		int peakingCount = MiniDialog.getQmapTableNum(peakingData);//PQ_IP_VIP_Peaking_COM_Main
		arrPeaking = new String[peakingCount];
		for(int i=0;i<peakingCount;i++) {
			arrPeaking[i] = MiniDialog.getQmapTableName(peakingData,i);
		}

		int peakingBandCount = MiniDialog.getQmapTableNum(bandData);//PQ_IP_VIP_Peaking_band_COM_Main
		arrPeaking_band = new String[peakingBandCount];
		for(int i=0;i<peakingBandCount;i++) {
			arrPeaking_band[i] = MiniDialog.getQmapTableName(bandData,i);
		}

		int peakingPcoringCount = MiniDialog.getQmapTableNum(pCoringData);//PQ_IP_VIP_Peaking_Pcoring_COM_Main
		arrPeaking_pcoring = new String[peakingPcoringCount];
		for(int i=0;i<peakingPcoringCount;i++) {
			arrPeaking_pcoring[i] = MiniDialog.getQmapTableName(pCoringData,i);
		}

		int peakingGainCount = MiniDialog.getQmapTableNum(gainData);//PQ_IP_VIP_Peaking_gain_COM_Main
		arrPeakingGain = new String[peakingGainCount];
		for(int i=0;i<peakingGainCount;i++) {
			arrPeakingGain[i] = MiniDialog.getQmapTableName(gainData,i);
		}
		
		int peakingGain_ad_cCount = MiniDialog.getQmapTableNum(gainAdCData);//PQ_IP_VIP_Peaking_gain_ad_C_COM_Main
		arrPeaking_Gain_ad_c = new String[peakingGain_ad_cCount];
		for(int i=0;i<peakingGain_ad_cCount;i++) {
			arrPeaking_Gain_ad_c[i] = MiniDialog.getQmapTableName(gainAdCData,i);
		}

		int peakingPcoring_ad_cCount = MiniDialog.getQmapTableNum(pCoringAdCData);//PQ_IP_VIP_Peaking_Pcoring_ad_C_COM_Main
		arrPeaking_Pcoring_ad_c = new String[peakingPcoring_ad_cCount];
		for(int i=0;i<peakingPcoring_ad_cCount;i++) {
			arrPeaking_Pcoring_ad_c[i] = MiniDialog.getQmapTableName(pCoringAdCData,i);
		}

		int peakingHlpfCount = MiniDialog.getQmapTableNum(hlpfData);//PQ_IP_VIP_HLPF_COM_Main
		arrPeaking_hlpf = new String[peakingHlpfCount];
		for(int i=0;i<peakingHlpfCount;i++) {
			arrPeaking_hlpf[i] = MiniDialog.getQmapTableName(hlpfData,i);
		}

		int peakingSram1Count = MiniDialog.getQmapTableNum(sram1Data);//PQ_IP_SRAM1_COM_Main
		arrSram1 = new String[peakingSram1Count];
		for(int i=0;i<peakingSram1Count;i++) {
			arrSram1[i] = MiniDialog.getQmapTableName(sram1Data,i);
		}

		int peakingSram2Count = MiniDialog.getQmapTableNum(sram2Data);//PQ_IP_SRAM2_COM_Main
		arrSram2 = new String[peakingSram2Count];
		for(int i=0;i<peakingSram2Count;i++) {
			arrSram2[i] = MiniDialog.getQmapTableName(sram2Data,i);
		}
		
		findView();
	}

	private void findView() {
		textview_pq_peakingsetting_peaking = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_peakingval);
		textview_pq_peakingsetting_band = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_peakingbandval);
		textview_pq_peakingsetting_pcoring = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_pcoringval);
		textview_pq_peakingsetting_pcoring_ad_c = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_pcoring_ad_cval);
		textview_pq_peakingsetting_gain = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_Gainval);
		textview_pq_peakingsetting_gain_ad_c = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_Gain_ad_Cval);
		textview_pq_peakingsetting_hlpf = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_hlpfval);
		textview_pq_peakingsetting_sram1 = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_sram1val);
		textview_pq_peakingsetting_sram2 = (TextView) findViewById(
				R.id.textview_factory_pq_peakingsetting_sram2val);
		initView();
	}

	private void initView() {
//		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");
		
		peaking = MiniDialog.getQmapCurrentTableIdx(peakingData);
		band = MiniDialog.getQmapCurrentTableIdx(bandData);
		pcoring = MiniDialog.getQmapCurrentTableIdx(pCoringData);
		pcoring_ad_c = MiniDialog.getQmapCurrentTableIdx(pCoringAdCData);
		gain = MiniDialog.getQmapCurrentTableIdx(gainData);
		gain_ad_c = MiniDialog.getQmapCurrentTableIdx(gainAdCData);
		hlpf = MiniDialog.getQmapCurrentTableIdx(hlpfData);
		sram1 = MiniDialog.getQmapCurrentTableIdx(sram1Data);
		sram2 = MiniDialog.getQmapCurrentTableIdx(sram2Data);

		if (band >= arrPeaking_band.length) {
			band = 0;
			if (arrPeaking_band.length <= 0) {
				arrPeaking_band = new String[1];
				arrPeaking_band[0] = "dummy";
			}
		}
		if (pcoring >= arrPeaking_Pcoring_ad_c.length) {
			pcoring = 0;
			if (arrPeaking_Pcoring_ad_c.length <= 0) {
				arrPeaking_Pcoring_ad_c = new String[1];
				arrPeaking_Pcoring_ad_c[0] = "dummy";
			}
		}
		textview_pq_peakingsetting_peaking.setText(arrPeaking[peaking]);
		textview_pq_peakingsetting_band.setText(arrPeaking_band[band]);
		textview_pq_peakingsetting_pcoring.setText(arrPeaking_pcoring[pcoring]);
		textview_pq_peakingsetting_pcoring_ad_c.setText(arrPeaking_Pcoring_ad_c[pcoring_ad_c]);
		textview_pq_peakingsetting_gain.setText(arrPeakingGain[gain]);
		textview_pq_peakingsetting_gain_ad_c.setText(arrPeaking_Gain_ad_c[gain_ad_c]);
		textview_pq_peakingsetting_hlpf.setText(arrPeaking_hlpf[hlpf]);
		textview_pq_peakingsetting_sram1.setText(arrSram1[sram1]);
		textview_pq_peakingsetting_sram2.setText(arrSram2[sram2]);
	}

	@Override
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		boolean bRet = true;
		int currentId = this.getCurrentFocus().getId();

		switch (KeyCode) {
		case KeyEvent.KEYCODE_ENTER:
			String[] strings = null;
			int curIndex = 0;
			String name = "";
			int type = 0;
			switch (currentId) {
			case R.id.linearlayout_factory_pq_peakingsetting_peaking:
				strings = arrPeaking;
				curIndex = peaking;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_peaking);
				type = Constant.PQ_PEAK_SETTING_PEAK;

				createDialog(strings, curIndex, name, currentId,type,
						peakingData.pqMode, peakingData.pqIpIndex, peakingData.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_peakingband:
				strings = arrPeaking_band;
				curIndex = band;
				type = Constant.PQ_PEAK_SETTING_PEAK_BAND;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_peaking_band);

				createDialog(strings, curIndex, name, currentId,type,
						bandData.pqMode, bandData.pqIpIndex, bandData.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_pcoring:
				strings = arrPeaking_pcoring;
				curIndex = pcoring;
				type = Constant.PQ_PEAK_SETTING_PEAK_PCORING;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_peaking_pcoring);

				createDialog(strings, curIndex, name, currentId,type,
						pCoringData.pqMode, pCoringData.pqIpIndex, pCoringData.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_pcoring_ad_c:
				strings = arrPeaking_Pcoring_ad_c;
				curIndex = pcoring_ad_c;
				type = Constant.PQ_PEAK_SETTING_PEAK_PCRING_AD_C;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_peaking_pcoring_ad_c);

				createDialog(strings, curIndex, name, currentId,type,
						pCoringAdCData.pqMode, pCoringAdCData.pqIpIndex, pCoringAdCData.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_Gain:
				strings = arrPeakingGain;
				curIndex = gain;
				type = Constant.PQ_PEAK_SETTING_PEAK_GAIN;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_peaking_gain);

				createDialog(strings, curIndex, name, currentId,type,
						gainData.pqMode, gainData.pqIpIndex, gainData.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_Gain_ad_C:
				strings = arrPeaking_Gain_ad_c;
				curIndex = gain_ad_c;
				type = Constant.PQ_PEAK_SETTING_PEAK_GAIN_AD_C;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_peaking_gain_ad_c);

				createDialog(strings, curIndex, name, currentId,type,
						gainAdCData.pqMode, gainAdCData.pqIpIndex, gainAdCData.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_hlpf:
				strings = arrPeaking_hlpf ;
				curIndex = hlpf;
				type = Constant.PQ_PEAK_SETTING_HLPF;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_vip_hlpf);

				createDialog(strings, curIndex, name, currentId,type,
						hlpfData.pqMode, hlpfData.pqIpIndex, hlpfData.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_sram1:
				strings = arrSram1;
				curIndex = sram1;
				type = Constant.PQ_PEAK_SETTING_SRAM1;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_sram1);

				createDialog(strings, curIndex, name, currentId,type,
						sram1Data.pqMode, sram1Data.pqIpIndex, sram1Data.pqType);
				break;
			case R.id.linearlayout_factory_pq_peakingsetting_sram2:
				strings = arrSram2;
				curIndex = sram2;
				type = Constant.PQ_PEAK_SETTING_SRAM2;
				name = getResources().getString(R.string.str_textview_factory_pq_peakingsetting_vip_sram2);

				createDialog(strings, curIndex, name, currentId,type,
						sram2Data.pqMode, sram2Data.pqIpIndex, sram2Data.pqType);
				break;
			default:
				bRet = false;
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
							 int mode, int ipIndex, int pqtype){
		final MiniDialog miniDialog;
		if (mode == MiniDialog.MODE_QMAP)
			miniDialog = MiniDialog.createQmapDialog(strings, curIndex, name, type, ipIndex, pqtype);
		else if (mode == MiniDialog.MODE_REGISTER)
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
				case R.id.linearlayout_factory_pq_peakingsetting_peaking:
					peaking = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_peakingband:
					band = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_pcoring:
					pcoring = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_pcoring_ad_c:
					pcoring_ad_c = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_Gain:
					gain = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_Gain_ad_C:
					gain_ad_c = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_hlpf:
					hlpf = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_sram1:
					sram1 = values;
					break;
				case R.id.linearlayout_factory_pq_peakingsetting_sram2:
					sram2 = values;
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
