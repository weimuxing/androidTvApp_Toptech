package mstar.tvsetting.factory.ui.picture;

import com.mstar.android.tv.TvFactoryManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemProperties;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.desk.IFactoryDesk;
import mstar.tvsetting.factory.ui.designmenu.DesignMenuActivity;

public class PictureViewHolder {
	private DesignMenuActivity mPictureActivity;
	private TvFactoryManager mTvFactoryManager = null;
	private IFactoryDesk factoryManager;
	private LinearLayout linearlayout_picture_mode;
	private LinearLayout linearlayout_picture_non_linear;
	private LinearLayout linearlayout_picture_adc_adjust;
	private LinearLayout linearlayout_picture_white_balance;
	private LinearLayout linearlayout_picture_overscan;
	private LinearLayout linearlayout_picture_non_standard_adjust;
	private LinearLayout linearlayout_picture_ssc_adjust;
	private LinearLayout linearlayout_factory_test_pattern;
	private LinearLayout linearlayout_picture_xvycc;
	private LinearLayout linearlayout_picture_dtv_av_delay;
	private LinearLayout linearlayout_picture_mfc;
	private LinearLayout linearlayout_picture_pq;
	private TextView textview_picture_test_pattern;
	private TextView textview_picture_mfc;
	private TextView textview_picture_xvycc;
	private TextView textview_picture_dtv_av_delay;
	private TextView textview_picture_3d_adaptive;
	private TextView textview_picture_pq;
	private String[] mTestPattern = null;
	private int mTestPatternIndex = 0;
	private String[] threeDselfadaptivelevel;
	private int threeDselfadaptivelevelindex = 0;
	private String[] mDtv_av_delay;
	private int mDtv_av_delay_index = 0;
	private String[] mfclevel;
	private int mfclevelindex = 0;
	private int mXvyccInputIndex = 0;
	private String inputString;
	private float[] mXvyccValue = new float[8];
	private String[] mXvyccStrings;

	public PictureViewHolder(DesignMenuActivity designMenuActivity1, IFactoryDesk factoryManager) {
		mPictureActivity = designMenuActivity1;
		this.factoryManager = factoryManager;

	}

	public void findView() {
		linearlayout_picture_mode = (LinearLayout) mPictureActivity.findViewById(R.id.linearlayout_picture_mode);
		linearlayout_picture_non_linear = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_Non_linear);
		linearlayout_picture_adc_adjust = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_ADC_Adjust);
		linearlayout_picture_white_balance = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_White_Balance);
		linearlayout_picture_overscan = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_Overscan);
		linearlayout_picture_non_standard_adjust = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_Non_standard_Adjust);
		linearlayout_picture_ssc_adjust = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_SSC_Adjust);
		linearlayout_factory_test_pattern = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_factory_Test_Pattern);
		linearlayout_picture_xvycc = (LinearLayout) mPictureActivity.findViewById(R.id.linearlayout_picture_xvycc);
		linearlayout_picture_dtv_av_delay = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_DTV_AV_delay);
		linearlayout_picture_pq = (LinearLayout) mPictureActivity
				.findViewById(R.id.linearlayout_picture_PQ);
		linearlayout_picture_mfc = (LinearLayout) mPictureActivity.findViewById(R.id.linearlayout_picture_mfc);
		textview_picture_test_pattern = (TextView) mPictureActivity.findViewById(R.id.textview_picture_Test_Pattern2);
		textview_picture_mfc = (TextView) mPictureActivity.findViewById(R.id.textview_picture_mfc_val);
		textview_picture_xvycc = (TextView) mPictureActivity.findViewById(R.id.textview_picture_xvycc2);
		textview_picture_dtv_av_delay = (TextView) mPictureActivity.findViewById(R.id.textview_picture_DTV_AV_delay2);
		textview_picture_3d_adaptive = (TextView) mPictureActivity.findViewById(R.id.textview_picture_3d_adaptive_val);
		initValue();
	}

	private void initValue() {
		// TODO Auto-generated method stub
		mTvFactoryManager = TvFactoryManager.getInstance();
		threeDselfadaptivelevel = mPictureActivity.getResources().getStringArray(R.array.str_arr_3d_self_adaptive);
		threeDselfadaptivelevelindex = mTvFactoryManager.get3DSelfAdaptiveLevel();
		mTestPattern = mPictureActivity.getResources().getStringArray(R.array.str_arr_test_pattern_type);
		mTestPatternIndex = mTvFactoryManager.getVideoTestPattern();
		mfclevel = mPictureActivity.getResources().getStringArray(R.array.str_arr_mfc);
		mfclevelindex = TvPictureManager.getInstance().getMfcLevel();
		if (SystemProperties.getBoolean("mstar.build.for.stb", false)) {
			linearlayout_picture_mfc.setVisibility(View.GONE);
		}
		mDtv_av_delay = mPictureActivity.getResources().getStringArray(R.array.str_arr_picture_dtv_av_delay);
		mDtv_av_delay_index = factoryManager.getDtvAvAbnormalDelay() ? 1 : 0;
		mXvyccStrings = mPictureActivity.getResources().getStringArray(R.array.xvycc_array);
		textview_picture_test_pattern.setText(mTestPattern[mTestPatternIndex]);
		textview_picture_3d_adaptive.setText(threeDselfadaptivelevel[threeDselfadaptivelevelindex]);
		textview_picture_mfc.setText(mfclevel[mfclevelindex]);
		textview_picture_dtv_av_delay.setText(mDtv_av_delay[mDtv_av_delay_index]);
	}

	public boolean onCreate() {
		registerListeners();
		return true;
	}

	private void registerListeners() {
		linearlayout_picture_mode.setOnClickListener(listener);
		linearlayout_picture_non_linear.setOnClickListener(listener);
		linearlayout_picture_adc_adjust.setOnClickListener(listener);
		linearlayout_picture_white_balance.setOnClickListener(listener);
		linearlayout_picture_overscan.setOnClickListener(listener);
		linearlayout_picture_non_standard_adjust.setOnClickListener(listener);
		linearlayout_picture_ssc_adjust.setOnClickListener(listener);
		linearlayout_picture_xvycc.setOnClickListener(listener);
		linearlayout_factory_test_pattern.setOnClickListener(listener);
		this.linearlayout_picture_pq.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.linearlayout_picture_mode:
				mPictureActivity.gotoPage(mPictureActivity.PICTURE_MODE_PAGE);
				break;
			case R.id.linearlayout_picture_Non_linear:
				mPictureActivity.gotoPage(mPictureActivity.NON_LINEAR_PAGE);
				break;
			case R.id.linearlayout_picture_ADC_Adjust:
				mPictureActivity.gotoPage(mPictureActivity.ADC_ADJUST_PAGE);
				break;
			case R.id.linearlayout_picture_White_Balance:
				mPictureActivity.gotoPage(mPictureActivity.WHITE_BALANCE_PAGE);
				break;
			case R.id.linearlayout_picture_Overscan:
				mPictureActivity.gotoPage(mPictureActivity.OVERSCAN_PAGE);
				break;
			case R.id.linearlayout_picture_Non_standard_Adjust:
				mPictureActivity.gotoPage(mPictureActivity.NON_STANDARD_ADJUST_PAGE);
				break;
			case R.id.linearlayout_picture_SSC_Adjust:
				mPictureActivity.gotoPage(mPictureActivity.SSC_ADJUST_PAGE);
				break;
			case R.id.linearlayout_factory_Test_Pattern:
				mPictureActivity.gotoPage(mPictureActivity.TEST_PATTERN_PAGE);
				break;
			case R.id.linearlayout_picture_xvycc:
				mXvyccInputIndex = 0;
				inputString = "";
				showXvyccInput(mXvyccInputIndex);
				break;
			case R.id.linearlayout_picture_PQ:
				mPictureActivity.gotoPage(mPictureActivity.PQ_ADJUST_PAGE);
				break;
			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bRet = true;
		int currentId = mPictureActivity.getCurrentFocus().getId();
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT: {
			switch (currentId) {
			case R.id.linearlayout_picture_Test_Pattern:
				if (mTestPatternIndex != TvFactoryManager.SCREEN_MUTE_BLACK)
					mTestPatternIndex++;
				else
					mTestPatternIndex = TvFactoryManager.SCREEN_MUTE_OFF;

				textview_picture_test_pattern.setText(mTestPattern[mTestPatternIndex]);
				mTvFactoryManager.setVideoTestPattern(mTestPatternIndex);
				onTestPatternChange();
				break;

			case R.id.linearlayout_picture_3d_adaptive:
				if (threeDselfadaptivelevelindex != TvFactoryManager.THREE_DIMENSION_SELFADAPTIVE_LEVEL_HIGH)
					threeDselfadaptivelevelindex++;
				else
					threeDselfadaptivelevelindex = 0;
				textview_picture_3d_adaptive.setText(threeDselfadaptivelevel[threeDselfadaptivelevelindex]);
				TvFactoryManager.getInstance().set3DSelfAdaptiveLevel(threeDselfadaptivelevelindex);
				break;

			case R.id.linearlayout_picture_mfc:
				if (mfclevelindex != TvPictureManager.MFC_LEVEL_BYPASS) {
					mfclevelindex++;
				} else {
					mfclevelindex = 0;
				}
				textview_picture_mfc.setText(mfclevel[mfclevelindex]);
				TvPictureManager.getInstance().setMfcLevel(mfclevelindex);
				break;

			case R.id.linearlayout_picture_DTV_AV_delay:
				if (mDtv_av_delay_index != 1) {
					mDtv_av_delay_index++;
					factoryManager.setDtvAvAbnormalDelay(true);
				} else {
					mDtv_av_delay_index = 0;
					factoryManager.setDtvAvAbnormalDelay(false);
				}
				textview_picture_dtv_av_delay.setText(mDtv_av_delay[mDtv_av_delay_index]);
				break;
			default:
				bRet = false;
				break;
			}
		}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: {
			switch (currentId) {
			case R.id.linearlayout_picture_Test_Pattern:
				if (mTestPatternIndex != TvFactoryManager.SCREEN_MUTE_OFF)
					mTestPatternIndex--;
				else
					mTestPatternIndex = TvFactoryManager.SCREEN_MUTE_BLACK;
				textview_picture_test_pattern.setText(mTestPattern[mTestPatternIndex]);
				mTvFactoryManager.setVideoTestPattern(mTestPatternIndex);
				onTestPatternChange();
				break;
			case R.id.linearlayout_picture_3d_adaptive:
				if (threeDselfadaptivelevelindex != 0)
					threeDselfadaptivelevelindex--;
				else
					threeDselfadaptivelevelindex = TvFactoryManager.THREE_DIMENSION_SELFADAPTIVE_LEVEL_HIGH;
				textview_picture_3d_adaptive.setText(threeDselfadaptivelevel[threeDselfadaptivelevelindex]);
				TvFactoryManager.getInstance().set3DSelfAdaptiveLevel(threeDselfadaptivelevelindex);
				break;
			case R.id.linearlayout_picture_mfc:
				if (mfclevelindex != 0) {
					mfclevelindex--;
				} else {
					mfclevelindex = TvPictureManager.MFC_LEVEL_BYPASS;
				}
				textview_picture_mfc.setText(mfclevel[mfclevelindex]);
				TvPictureManager.getInstance().setMfcLevel(mfclevelindex);
				break;
			case R.id.linearlayout_picture_DTV_AV_delay:
				if (mDtv_av_delay_index != 0) {
					mDtv_av_delay_index--;
					factoryManager.setDtvAvAbnormalDelay(false);
				} else {
					mDtv_av_delay_index = 1;
					factoryManager.setDtvAvAbnormalDelay(true);
				}
				textview_picture_dtv_av_delay.setText(mDtv_av_delay[mDtv_av_delay_index]);
				break;
			default:
				bRet = false;
				break;
			}
		}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER: {
			switch (currentId) {
			case R.id.linearlayout_picture_xvycc:
				mXvyccInputIndex = 0;
				inputString = "";
				showXvyccInput(mXvyccInputIndex);
				break;

			default:
				break;
			}
		}
			break;
		default:
			bRet = false;
			break;
		}
		return bRet;
	}

	private void showXvyccInput(int index) {
		final EditText editTextXy = new EditText(mPictureActivity);
		editTextXy.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		String str1 = mPictureActivity.getString(R.string.str_please_enter);
		String str2 = mPictureActivity.getString(R.string.str_decimal_point);
		new AlertDialog.Builder(mPictureActivity).setTitle(str1 + " " + mXvyccStrings[index] + " " + str2)
				.setIcon(android.R.drawable.ic_dialog_info).setView(editTextXy)
				.setPositiveButton(mPictureActivity.getString(R.string.str_ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (editTextXy.getText().toString().isEmpty() || editTextXy.getText().toString().length() > 9) {
							arg0.dismiss();
							Toast.makeText(mPictureActivity, mPictureActivity.getString(R.string.str_input_invalite),
									Toast.LENGTH_SHORT).show();
						} else {
							String str = editTextXy.getText().toString();
							inputString += (mPictureActivity.getString(R.string.str_zero) + str + ";");
							mXvyccValue[mXvyccInputIndex] = Float
									.parseFloat(mPictureActivity.getString(R.string.str_zero) + str);
							Toast.makeText(mPictureActivity,
									mPictureActivity.getString(R.string.str_input) + mXvyccValue[mXvyccInputIndex],
									Toast.LENGTH_SHORT).show();
							if (mXvyccInputIndex == 7) {
								Toast.makeText(mPictureActivity,
										mPictureActivity.getString(R.string.str_all_input) + inputString,
										Toast.LENGTH_LONG).show();
								try {
									TvManager.getInstance().getFactoryManager().setXvyccDataFromPanel(mXvyccValue[0],
											mXvyccValue[1], mXvyccValue[2], mXvyccValue[3], mXvyccValue[4],
											mXvyccValue[5], mXvyccValue[6], mXvyccValue[7], 0);
								} catch (TvCommonException e) {
									e.printStackTrace();
								}
							} else {
								mXvyccInputIndex++;
								showXvyccInput(mXvyccInputIndex);
							}

						}
					}
				})
				.setNegativeButton(mPictureActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).show();
	}

	private void onTestPatternChange() {
		Intent intent = new Intent();
		if (mTestPatternIndex == TvFactoryManager.SCREEN_MUTE_OFF) {
			intent.setAction("mstar.tvsetting.factory.TEST_PATTERN_OFF");
		} else if (mTestPatternIndex == TvFactoryManager.SCREEN_MUTE_BLACK
				|| mTestPatternIndex == TvFactoryManager.SCREEN_MUTE_RED
				|| mTestPatternIndex == TvFactoryManager.SCREEN_MUTE_GREEN
				|| mTestPatternIndex == TvFactoryManager.SCREEN_MUTE_BLUE
				|| mTestPatternIndex == TvFactoryManager.SCREEN_MUTE_WHITE) {
			intent.setAction("mstar.tvsetting.factory.TEST_PATTERN_ON");
		}
		mPictureActivity.sendBroadcast(intent);
	}

}
