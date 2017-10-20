package mstar.tvsetting.factory.ui.designmenu;

import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.pqsetting.CTIActivity;
import mstar.tvsetting.factory.ui.pqsetting.LumaSettingActivity;
import mstar.tvsetting.factory.ui.pqsetting.MiniDialog;
import mstar.tvsetting.factory.ui.pqsetting.NrSettingActivity;
import mstar.tvsetting.factory.ui.pqsetting.PQColorQmapActivity;
import mstar.tvsetting.factory.ui.pqsetting.PQColorSettingActivity;
import mstar.tvsetting.factory.ui.pqsetting.QmapActivity;
import mstar.tvsetting.factory.ui.pqsetting.PeakingSettingActivity;
import mstar.tvsetting.factory.until.Constant;
import mstar.tvsetting.factory.until.Tools;
import com.mstar.android.tv.TvFactoryManager;
import mstar.tvsetting.factory.until.Constant.PQ_DATA;


public class PQAdjustViewHolder {
	private LinearLayout linearlayout_pq_source;
	private DesignMenuActivity mPQActivity;
	private IFactoryDesk factoryManager;
	private LinearLayout linearlayout_pq_colorqmap;
	private LinearLayout linearlayout_pq_qmap;
	private LinearLayout linearlayout_pq_colorsetting;
	private LinearLayout linearlayout_pq_lumaseting;
	private LinearLayout linearlayout_pq_peakingsetting;
	private LinearLayout linearlayout_pq_nrsetting;
	private LinearLayout linearlayout_pq_cti;
	private LinearLayout linearlayout_pq_reload;
	private TextView textview_pq_source_val;
	private TextView textview_pq_vip_val;
	private TextView textview_pq_gamma_val;
	private TextView textview_pq_dlc_val;
	private String[] mArrayVip;
	private String[] mGammaVal = null;
	private String[] mDlcVal = null;
	private int mGAMMAIndex = 0;
	private int mDLCIndex = 0;
	private int mVipVal=0;
	private String TAG =PQAdjustViewHolder.class.getSimpleName();
	private static final int FUNCTION_DISABLED = 0;
	private static final int TYPE_AV = 0;
	private static final int TYPE_HDMI = 1;
	private TvCommonManager mTvCommonmanager = TvCommonManager.getInstance();
	private int[] sourceType;
	private int currentSourceindex = 0;
	private int[] SourceListFlag = new int[TvCommonManager.INPUT_SOURCE_NUM];
	int currentType=0;
	private static String[] sourcearrayPQ;
	private int sourceindexPQ = 0;
	
	private TvCommonManager mTvCommonManager = TvCommonManager.getInstance();
	private boolean mIsSignalLock = true;
	
	private static final int[] mSourceListInvisible = { TvCommonManager.INPUT_SOURCE_STORAGE,
			TvCommonManager.INPUT_SOURCE_KTV, TvCommonManager.INPUT_SOURCE_JPEG, TvCommonManager.INPUT_SOURCE_DTV2,
			TvCommonManager.INPUT_SOURCE_STORAGE2, TvCommonManager.INPUT_SOURCE_DIV3,
			TvCommonManager.INPUT_SOURCE_SCALER_OP, TvCommonManager.INPUT_SOURCE_RUV };
	
	public PQAdjustViewHolder(DesignMenuActivity designMenuActivity1, IFactoryDesk factoryManager) {
		mPQActivity = designMenuActivity1;
		this.factoryManager = factoryManager;
	}

	public void findView() {
		linearlayout_pq_source = (LinearLayout) mPQActivity.findViewById(R.id.linearlayout_factory_pq_source);
		linearlayout_pq_colorsetting = (LinearLayout) mPQActivity
				.findViewById(R.id.linearlayout_factory_pq_colorsetting);
		linearlayout_pq_colorqmap = (LinearLayout) mPQActivity.findViewById(R.id.linearlayout_factory_pq_pqselect);
		linearlayout_pq_qmap = (LinearLayout) mPQActivity.findViewById(R.id.linearlayout_factory_pq_qmap);
		linearlayout_pq_lumaseting = (LinearLayout) mPQActivity.findViewById(R.id.linearlayout_factory_pq_lumasetting);
		linearlayout_pq_peakingsetting = (LinearLayout) mPQActivity
				.findViewById(R.id.linearlayout_factory_pq_peakingsetting);
		linearlayout_pq_nrsetting = (LinearLayout) mPQActivity.findViewById(R.id.linearlayout_factory_pq_nrsetting);
		linearlayout_pq_cti = (LinearLayout) mPQActivity.findViewById(R.id.linearlayout_factory_pq_cti);
		linearlayout_pq_reload = (LinearLayout) mPQActivity.findViewById(R.id.linearlayout_factory_pq_reload);
		textview_pq_source_val = (TextView) mPQActivity.findViewById(R.id.textview_factory_pq_source_val);
		textview_pq_gamma_val = (TextView) mPQActivity.findViewById(R.id.textview_factory_pq_gamma_val);
		textview_pq_dlc_val = (TextView) mPQActivity.findViewById(R.id.textview_factory_pq_dlc_val);
		textview_pq_vip_val = (TextView) mPQActivity.findViewById(R.id.textview_factory_pq_vip_val);
		
		initValue();
	}

	private void initValue() {
		sourcearrayPQ=mPQActivity.getResources().getStringArray(R.array.str_arr_pq_vals);
		mGammaVal = mPQActivity.getResources().getStringArray(R.array.str_arr_pq_gamma_type);
		//mDlcVal = mPQActivity.getResources().getStringArray(R.array.str_arr_pq_dlc_type);
		int[] dlcCureCount=TvCommonManager.getInstance().setTvosCommonCommand("GetDLCCurveCount");
		
		mDlcVal = new String[dlcCureCount[0]];
		for(int i=0;i<dlcCureCount[0];i++)
			mDlcVal[i] = i+"";

		/*for(int i=0;i<dlcCureCount.length;i++){
		Log.i(TAG, "dlcCureCount["+i+"]:"+dlcCureCount[i]);
		}*/
		
		int vipCount = TvFactoryManager.getInstance().getQmapTableNum(Constant.MAIN_PQ_IP_VIP_COM_Main);//PQ_IP_VIP_Main
		//Log.i(TAG, "vipCount:"+vipCount);
		mArrayVip = new String[vipCount];
		for(int i=0;i<vipCount;i++) {
			mArrayVip[i] = TvFactoryManager.getInstance().getQmapTableName(Constant.MAIN_PQ_IP_VIP_COM_Main,i);
			//Log.i(TAG, "mArrayVip["+i+"]:"+mArrayVip[i]);
		}
			
		
		//mArrayVip = mPQActivity.getResources().getStringArray(R.array.str_arr_pq_vip_type);
		
		sourceindexPQ = TvCommonManager.getInstance().getCurrentInputSource().ordinal();
		
		getSupportSourcelist();

		String[] com = null;
		int tableNum = TvFactoryManager.getInstance().getQmapTableNum(1);
					
		int[] retval = TvCommonManager.getInstance().setTvosCommonCommand("GetPQData");
		mGAMMAIndex = retval[PQ_DATA.GAMMA_Data];
		mDLCIndex = retval[PQ_DATA.DLC_Data];
		/*for(int i=0;i<=PQ_DATA.DLC_Data;i++){
			Log.i(TAG, "retval["+i+"]:"+retval[i]);
		}*/
		mVipVal = TvFactoryManager.getInstance().getQmapCurrentTableIdx(Constant.MAIN_PQ_IP_VIP_COM_Main);
		
		textview_pq_source_val.setText(sourcearrayPQ[sourceindexPQ]);
		textview_pq_gamma_val.setText(mGammaVal[mGAMMAIndex]);
		textview_pq_dlc_val.setText(mDlcVal[mDLCIndex]);
		if(mVipVal<0 || mVipVal>=mArrayVip.length){
			mVipVal=0;
		}
		textview_pq_vip_val.setText(mArrayVip[mVipVal]);

		
	}

	private void getSupportSourcelist() {
		int[] sourceList;
		int j = 0;
		sourceList = mTvCommonmanager.getSourceList();
		if (sourceList != null) {
			int[] count = new int[2];
			for (int i = 0; i < count.length; i++) {
				count[i] = 0;
			}
			for (int i = 0; (i < SourceListFlag.length) && (i < sourceList.length); i++) {
				SourceListFlag[i] = sourceList[i];
				if (SourceListFlag[i] > 0) {
					j++;
					switch (i) {
					case TvCommonManager.INPUT_SOURCE_CVBS:
					case TvCommonManager.INPUT_SOURCE_CVBS2:
					case TvCommonManager.INPUT_SOURCE_CVBS3:
					case TvCommonManager.INPUT_SOURCE_CVBS4:
					case TvCommonManager.INPUT_SOURCE_CVBS5:
					case TvCommonManager.INPUT_SOURCE_CVBS6:
					case TvCommonManager.INPUT_SOURCE_CVBS7:
					case TvCommonManager.INPUT_SOURCE_CVBS8:
						count[TYPE_AV] += 1;
						break;
					case TvCommonManager.INPUT_SOURCE_HDMI:
					case TvCommonManager.INPUT_SOURCE_HDMI2:
					case TvCommonManager.INPUT_SOURCE_HDMI3:
					case TvCommonManager.INPUT_SOURCE_HDMI4:
						count[TYPE_HDMI] += 1;
						break;
					}
				}
			}
			sourcearrayPQ[TvCommonManager.INPUT_SOURCE_CVBS] = count[TYPE_AV] == 1
					? mPQActivity.getResources().getString(R.string.picture_pq_adjust_AV_vals)
					: mPQActivity.getResources().getString(R.string.picture_pq_adjust_AV1_vals);
			sourcearrayPQ[TvCommonManager.INPUT_SOURCE_HDMI] = count[TYPE_HDMI] == 1
					? mPQActivity.getResources().getString(R.string.picture_pq_adjust_HDMI_vals)
					: mPQActivity.getResources().getString(R.string.picture_pq_adjust_HDMI1_vals);
			for (int i = 0; (i < mSourceListInvisible.length) && (i < SourceListFlag.length); i++) {
				if (TvCommonManager.INPUT_SOURCE_NUM > mSourceListInvisible[i]) {
					SourceListFlag[mSourceListInvisible[i]] = FUNCTION_DISABLED;
					if (mSourceListInvisible[i] < SourceListFlag.length
							&& mSourceListInvisible[i] < sourceList.length) {
						j--;
					}
				}
			}

			sourceType = new int[j];
			j = 0;
			for (int i = 0; (i < SourceListFlag.length) && (i < sourceList.length); i++) {
				if (SourceListFlag[i] > 0) {
					sourceType[j] = i;
					if (sourceType[j] == sourceindexPQ) {
						currentSourceindex = j;
					}
					j++;
				}
			}
		}
	}

	public boolean onCreate() {
		registerListeners();
		return true;
	}

	private void registerListeners() {
		this.linearlayout_pq_colorqmap.setOnClickListener(listener);
		this.linearlayout_pq_qmap.setOnClickListener(listener);
		this.linearlayout_pq_colorsetting.setOnClickListener(listener);
		this.linearlayout_pq_lumaseting.setOnClickListener(listener);
		this.linearlayout_pq_peakingsetting.setOnClickListener(listener);
		this.linearlayout_pq_nrsetting.setOnClickListener(listener);
		this.linearlayout_pq_cti.setOnClickListener(listener);
		this.linearlayout_pq_reload.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			mIsSignalLock = mTvCommonManager.isSignalStable(mTvCommonManager.getCurrentTvInputSource());
			if (!mIsSignalLock && view.getId() != R.id.linearlayout_factory_pq_source) {
				Tools.showToast(mPQActivity.getApplicationContext(),
						mPQActivity.getString(R.string.str_toast_factory_pq_no_signal));
				return;
			}
			switch (view.getId()) {
			case R.id.linearlayout_factory_pq_pqselect:
				mPQActivity.startActivity(new Intent(mPQActivity, PQColorQmapActivity.class));
				break;
			case R.id.linearlayout_factory_pq_qmap:
				mPQActivity.startActivity(new Intent(mPQActivity, QmapActivity.class));
				break;
			case R.id.linearlayout_factory_pq_colorsetting:
				mPQActivity.startActivity(new Intent(mPQActivity, PQColorSettingActivity.class));
				break;
			case R.id.linearlayout_factory_pq_lumasetting:
				mPQActivity.startActivity(new Intent(mPQActivity, LumaSettingActivity.class));
				break;
			case R.id.linearlayout_factory_pq_peakingsetting:
				mPQActivity.startActivity(new Intent(mPQActivity, PeakingSettingActivity.class));
				break;
			case R.id.linearlayout_factory_pq_nrsetting:
				mPQActivity.startActivity(new Intent(mPQActivity, NrSettingActivity.class));
				break;
			case R.id.linearlayout_factory_pq_cti:
				mPQActivity.startActivity(new Intent(mPQActivity, CTIActivity.class));
				break;
			case R.id.linearlayout_factory_pq_reload:
				TvCommonManager.getInstance().setTvosCommonCommand("reload_pq_data");
				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		mIsSignalLock = mTvCommonManager.isSignalStable(mTvCommonManager.getCurrentTvInputSource());
		
		boolean bRet = true;
		int maxValue;
		int curSourceValue;
		int currentId = mPQActivity.getCurrentFocus().getId();

		switch (keyCode) {
		case KeyEvent.KEYCODE_ENTER:
			mIsSignalLock = mTvCommonManager.isSignalStable(mTvCommonManager.getCurrentTvInputSource());
			if (!mIsSignalLock && currentId != R.id.linearlayout_factory_pq_source) {
				Tools.showToast(mPQActivity.getApplicationContext(),
						mPQActivity.getString(R.string.str_toast_factory_pq_no_signal));
				return false;
			}
			String name = "";
			switch (currentId) {
			case R.id.linearlayout_factory_pq_gamma:
				name = mPQActivity.getResources().getString(R.string.str_textview_factory_pq_gamma);
				createDialog(mGammaVal, mGAMMAIndex, name, currentId, Constant.PQ_ADJUST_GAMMA,
						MiniDialog.MODE_REGISTER_OTHER, 0, 0);
				break;
			case R.id.linearlayout_factory_pq_dlc:
				name = mPQActivity.getResources().getString(R.string.str_textview_factory_pq_dlc);
				createDialog(mDlcVal, mDLCIndex, name, currentId, Constant.PQ_ADJUST_DLC,
						MiniDialog.MODE_REGISTER_OTHER, 0, 0);
				break;
			case R.id.linearlayout_factory_pq_vip:
				name = mPQActivity.getResources().getString(R.string.str_textview_factory_pq_vip);
				createDialog(mArrayVip, mVipVal, name, currentId, Constant.PQ_ADJUST_VIP,
						MiniDialog.MODE_QMAP, Constant.MAIN_PQ_IP_VIP_COM_Main, MiniDialog.PQTYPE_BIG);
				break;
			default:
				bRet = false;
				break;
			}
			break;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			switch (currentId) {
			case R.id.linearlayout_factory_pq_source:
				maxValue = sourceType.length;
				currentSourceindex++;
				if (currentSourceindex > maxValue - 1) {
					currentSourceindex = 0;
				}
				setInputSource(EnumInputSource.values()[sourceType[currentSourceindex]]);
				initValue();
				if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_ATV.ordinal()) {
					int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
				} else if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_DTV.ordinal()) {
					TvChannelManager.getInstance().playDtvCurrentProgram();
				}
			default:
				bRet = false;
				break;
			}
			break;

		case KeyEvent.KEYCODE_DPAD_LEFT:
			switch (currentId) {
			case R.id.linearlayout_factory_pq_source:
				maxValue = sourceType.length;
				currentSourceindex--;
				if (currentSourceindex < 0) {
					currentSourceindex = maxValue - 1;
				}
				setInputSource(EnumInputSource.values()[sourceType[currentSourceindex]]);
				initValue();
				if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_ATV.ordinal()) {
					int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
				} else if (sourceType[currentSourceindex] == EnumInputSource.E_INPUT_SOURCE_DTV.ordinal()) {
					TvChannelManager.getInstance().playDtvCurrentProgram();
				}
				break;

			default:
				bRet = false;
				break;
			}

			break;

		default:
			bRet = false;
			break;
		}
		return bRet;
	}

	private void setInputSource(EnumInputSource st) {
		TvCommonManager.getInstance().setInputSource(st);
	}

	public void createDialog(String[] strings, int curIndex, String name, final int currentID, int type,
			 int mode, int ipIndex, int pqtype) {
		final MiniDialog miniDialog;
		if (mode == MiniDialog.MODE_QMAP)
			miniDialog = MiniDialog.createQmapDialog(strings, curIndex, name, type, ipIndex, pqtype);
		else if (mode == MiniDialog.MODE_REGISTER)
			miniDialog = MiniDialog.createRegAdjustDialog(curIndex, name, type);
		else
			miniDialog = MiniDialog.createUserDialog(strings, curIndex, name, type);
		
		miniDialog.show(mPQActivity.getFragmentManager(), "");
		miniDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				int values = miniDialog.currentIndex;
				switch (currentID) {
				case R.id.linearlayout_factory_pq_gamma:
					mGAMMAIndex = values;
					break;
				case R.id.linearlayout_factory_pq_dlc:
					mDLCIndex = values;
					break;
				case R.id.linearlayout_factory_pq_vip:
					mVipVal = values;
					break;
				default:
					break;
				}
				mPQActivity.setActivityVisible();
				initValue();
			}
		});
		miniDialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				mPQActivity.setActivityInvisible();
			}
		});
	}
}
