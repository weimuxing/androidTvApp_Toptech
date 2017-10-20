package mstar.tvsetting.factory.ui.pqsetting;

import com.mstar.android.tv.TvCommonManager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import mstar.factorymenu.ui.R;
import mstar.tvsetting.factory.until.Constant;
import com.mstar.android.tv.TvFactoryManager;

public class MiniDialog extends DialogFragment implements OnKeyListener{
	private static final String TAG = "MiniDialog";
	private TextView mTitleView;
	private TextView mValueView;
	private View dialogView;
	private RelativeLayout mRelativeLayout;
	private OnDismissListener onDismissListener;
	private OnShowListener onShowListener;
	private Dialog dialog;

	public static final int MODE_REGISTER = 0;
	public static final int MODE_QMAP = 1;
	public static final int MODE_REGISTER_ONOFF = 2;
	public static final int MODE_REGISTER_OTHER = 3;

	public static final int PQTYPE_BIG = 0;
	public static final int PQTYPE_SMALL = 1;

	private int itemId; // from which item.
	public String name; // item name
	private int mode = 0; // 1-qmap adjust, 0-register adjust.
	// valid when mode is qmap
	private int pqType;// big or small
	public int ipIndex; // valid when it is not -1
	public int currentIndex; // current table index.
	private String[] arr; // table names.
	// valid when mode == 0
	public  int values; // for register adjust, when mode is 0
	public  boolean isSignable = false; // for register value, 1 is minus

	public MiniDialog() {
		super();
	}
	// this is for register adjust.
	@Deprecated
	public MiniDialog(int val,String name,int type ) {
		super();
		mode = MODE_REGISTER;
		values = val;
		this.name = name;
		this.itemId = type;
	}
	@Deprecated
	public MiniDialog(String[] names, int curIndex, String name,int type) {
		super();
		mode = MODE_REGISTER_ONOFF;
		currentIndex = curIndex;
		this.name = name;
		this.itemId = type;
		arr = names;
	}
	// this is for qmap adjust.
	@Deprecated
	public MiniDialog(String[] strings,int curIndex,String name,int type, int ipIndex, int pqType) {
		super();
		arr = strings;
		currentIndex = curIndex;
		this.name = name;
		this.mode = MODE_QMAP;
		this.itemId = type;
		this.ipIndex = ipIndex;
		this.pqType = pqType;
	}
	
	public static MiniDialog createRegAdjustDialog(int val,String name,int id) {
		MiniDialog md = new MiniDialog();
		md.mode = MODE_REGISTER;
		md.values = val;
		md.name = name;
		md.itemId = id;
		if (hasSign(id)) {
			md.isSignable = true;
		}
		return md;
	}
	public static MiniDialog createRegOnoffDialog(String[] names, int curIndex, String name,int id) {
		MiniDialog md = new MiniDialog();
		md.mode = MODE_REGISTER_ONOFF;
		md.currentIndex = curIndex;
		md.name = name;
		md.itemId = id;
		md.arr = names;
		return md;
	}
	public static MiniDialog createQmapDialog(String[] strings,int curIndex,String name,int id, int ipIndex, int pqType) {
		MiniDialog md = new MiniDialog();
		md.arr = strings;
		md.currentIndex = curIndex;
		md.name = name;
		md.mode = MODE_QMAP;
		md.itemId = id;
		md.ipIndex = ipIndex;
		md.pqType = pqType;
		return md;
	}
	public static MiniDialog createUserDialog(String[] names, int curIndex, String name,int id) {
		MiniDialog md = new MiniDialog();
		md.mode = MODE_REGISTER_OTHER;
		md.currentIndex = curIndex;
		md.name = name;
		md.itemId = id;
		md.arr = names;
		return md;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dialog = new Dialog(getActivity(), R.style.TVDialog);
		dialogView = getActivity().getLayoutInflater().inflate(R.layout.minidialog, null);
		dialog.setContentView(dialogView);
		mTitleView = (TextView) dialogView.findViewById(R.id.dialog_title);
		mValueView = (TextView) dialogView.findViewById(R.id.dialog_value);
		mRelativeLayout = (RelativeLayout) dialogView.findViewById(R.id.dialog_rl);
		mRelativeLayout.setOnKeyListener(this);
		mRelativeLayout.requestFocus();
		mRelativeLayout.setFocusable(true);
		mTitleView.setText(name);
		if (mode == MODE_REGISTER) {
			mValueView.setText(toHexString(values));
		} else { // qmap register_onoff
			mValueView.setText(arr[currentIndex]);
		}
		if (itemId == Constant.PQ_ADJUST_GAMMA || itemId == Constant.PQ_ADJUST_DLC || itemId == Constant.PQ_SELECT_IHC
				|| itemId == Constant.PQ_SELECT_ICC || itemId == Constant.PQ_SELECT_IBC) {
			//setTvosCommand();
		}
		dialog.setOnShowListener(onShowListener);
		return dialog;
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		boolean isDown = event.getAction() == KeyEvent.ACTION_DOWN;
		if (!isDown) // we process only down event.
			return true;
		
		if (mode == MODE_QMAP) {
			if (pqType == Constant.PQ_TABLE_TYPE_LITTLE) {
				if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (currentIndex > 0) {
						currentIndex --;
					}else {
						currentIndex = arr.length -1;
					}
					mValueView.setText(arr[currentIndex]);
				}else if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
					if (currentIndex < (arr.length - 1)) {
						currentIndex ++;
					}else{
						currentIndex = 0;
					}
					mValueView.setText(arr[currentIndex]);
				}
				TvFactoryManager.getInstance().loadCustomerPqTable(currentIndex, ipIndex);//
			} else if (pqType == Constant.PQ_TABLE_TYPE_BIG) {
				if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (currentIndex > 0) {
						currentIndex --;
					}else {
						currentIndex = arr.length -1;
					}
					mValueView.setText(arr[currentIndex]);
				}else if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
					if (currentIndex < (arr.length - 1)) {
						currentIndex ++;
					}else{
						currentIndex = 0;
					}
					mValueView.setText(arr[currentIndex]);
				}
				TvFactoryManager.getInstance().loadPqTable(currentIndex, ipIndex);//
			}
		} else if (mode == MODE_REGISTER_ONOFF) {
			if (arr.length != 2)
				Log.e(TAG, "for MODE_REGISTER_ONOFF, arr length must be 2");
			
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
					|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (currentIndex > 0) {
					currentIndex = 0;
				}else {
					currentIndex = 1;
				}
				mValueView.setText(arr[currentIndex]);
				runOnoffCommand();
			}
		} else if (mode == MODE_REGISTER) {
			int[] regdat = getRegInfoById(itemId);
			if (regdat[0] == -1) {
				Log.e(TAG, "not registed!!");
				return true;
			}
			int min = regdat[REGDATA_MIN];
			int max = regdat[REGDATA_MAX];
			int mask = regdat[REGDATA_MASK];

			if (isSignable) {
				min = -max;
				if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (values > min) {
						values--;
					} else {
						values = max;
					}
					runRegCommand();
					mValueView.setText(toHexString(values));
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (values < max) {
						values++;
					} else {
						values = min;
					}
					runRegCommand();
					mValueView.setText(toHexString(values));
				}
			} else {
				if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
					if (values > 0) {
						values--;
					} else {
						values = max;
					}

					if ((values & mask) != values)
						values = 0;

					runRegCommand();
					mValueView.setText("0x"+Integer.toHexString(values).toUpperCase());
				} else if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (values < max) {
						values++;
					} else {
						values = 0;
					}

					if ((values & mask) != values)
						values = min;

					runRegCommand();
					mValueView.setText("0x"+Integer.toHexString(values).toUpperCase());
				}
			}

		} else { // other type, user defined interface.
			if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				if (currentIndex > 0) {
					currentIndex --;
				}else {
					currentIndex = arr.length -1;
				}
				mValueView.setText(arr[currentIndex]);
			}else if (isDown && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
				if (currentIndex < (arr.length - 1)) {
					currentIndex ++;
				}else{
					currentIndex = 0;
				}
				mValueView.setText(arr[currentIndex]);
			}
			runOtherCommand();
		}
		if (isDown && (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)) {
			getDialog().dismiss();
		}
		return true;
	}

	public void setOnDismissListener(OnDismissListener onDismissListener){
		this.onDismissListener = onDismissListener;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (onDismissListener != null) {
			onDismissListener.onDismiss(dialog);
		}
	}
	public void setOnShowListener(OnShowListener onShowListener){
		this.onShowListener = onShowListener;
	}
	@Override
	public void show(FragmentManager manager, String tag) {
		// TODO Auto-generated method stub
		super.show(manager, tag);
	}
	
	public int runRegCommand() {
		if (mode == MODE_REGISTER) {
			int[] regdat = getRegInfoById(itemId);
			int reg = regdat[REGDATA_REG];
			int mask = regdat[REGDATA_MASK];
			int regtype = regdat[REGDATA_TYPE];

			if (regdat[0] != -1) {
				switch (itemId) {
					case Constant.PQ_COLOR_SETTING_ICC_R:
					case Constant.PQ_COLOR_SETTING_ICC_G:
					case Constant.PQ_COLOR_SETTING_ICC_B:
					case Constant.PQ_COLOR_SETTING_ICC_C:
					case Constant.PQ_COLOR_SETTING_ICC_M:
					case Constant.PQ_COLOR_SETTING_ICC_Y:
					case Constant.PQ_COLOR_SETTING_ICC_F:
					case Constant.PQ_COLOR_SETTING_ICC_GY:
					case Constant.PQ_COLOR_SETTING_ICC_BY:
					case Constant.PQ_COLOR_SETTING_ICC_FLESH:
					case Constant.PQ_COLOR_SETTING_ICC_REDDISH:
					case Constant.PQ_COLOR_SETTING_ICC_LIP:
					case Constant.PQ_COLOR_SETTING_ICC_HAIR:
					case Constant.PQ_COLOR_SETTING_ICC_MEAT:
					case Constant.PQ_COLOR_SETTING_ICC_DARK: {
						boolean isPlus = true;
						int tmp = values;
						if (tmp < 0) {
							tmp = -tmp;
							isPlus = false;
						}
						if (regtype == 0) // scaler register
							setScReg8(reg, tmp, mask);
						else if (regtype == 1) // normal register
							setReg8(reg, tmp, mask);
						if (isSignable) {
							setRegValueSignById(itemId, isPlus);
						}
						break;
					}
					case Constant.PQ_COLOR_SETTING_IBC_R:
					case Constant.PQ_COLOR_SETTING_IBC_G:
					case Constant.PQ_COLOR_SETTING_IBC_B:
					case Constant.PQ_COLOR_SETTING_IBC_C:
					case Constant.PQ_COLOR_SETTING_IBC_M:
					case Constant.PQ_COLOR_SETTING_IBC_Y:
					case Constant.PQ_COLOR_SETTING_IBC_F:
					case Constant.PQ_COLOR_SETTING_IBC_GY:
					case Constant.PQ_COLOR_SETTING_IBC_BY:
					case Constant.PQ_COLOR_SETTING_IBC_FLESH:
					case Constant.PQ_COLOR_SETTING_IBC_REDDISH:
					case Constant.PQ_COLOR_SETTING_IBC_LIP:
					case Constant.PQ_COLOR_SETTING_IBC_HAIR:
					case Constant.PQ_COLOR_SETTING_IBC_MEAT:
					case Constant.PQ_COLOR_SETTING_IBC_DARK:
					case Constant.PQ_COLOR_SETTING_IHC_R:
					case Constant.PQ_COLOR_SETTING_IHC_G:
					case Constant.PQ_COLOR_SETTING_IHC_B:
					case Constant.PQ_COLOR_SETTING_IHC_C:
					case Constant.PQ_COLOR_SETTING_IHC_M:
					case Constant.PQ_COLOR_SETTING_IHC_Y:
					case Constant.PQ_COLOR_SETTING_IHC_F:
					case Constant.PQ_COLOR_SETTING_IHC_GY:
					case Constant.PQ_COLOR_SETTING_IHC_BY:
					case Constant.PQ_COLOR_SETTING_IHC_FLESH:
					case Constant.PQ_COLOR_SETTING_IHC_REDDISH:
					case Constant.PQ_COLOR_SETTING_IHC_LIP:
					case Constant.PQ_COLOR_SETTING_IHC_HAIR:
					case Constant.PQ_COLOR_SETTING_IHC_MEAT:
					case Constant.PQ_COLOR_SETTING_IHC_DARK:
					case Constant.PQ_LUMA_SETTING_PRE_YOFFSET:
					case Constant.PQ_LUMA_SETTING_PRE_YGAIN:
					case Constant.PQ_LUMA_SETTING_COMB_CONTRAST:
					case Constant.PQ_LUMA_SETTING_COMB_BRIGHTNESS:
					case Constant.PQ_LUMA_SETTING_COMB_SATURATION:
					case Constant.PQ_CTI_SETTING_COMB_83:
					case Constant.PQ_CTI_SETTING_POST_CTI_COEF:
						if (regtype == 0) // scaler register
							setScReg8(reg, values, mask);
						else if (regtype == 1) // normal register
							setReg8(reg, values, mask);
						break;
					default:
						Log.e(TAG, "MODE_REGISTER not run this id " + itemId);
						break;
				}
			} else {
				Log.e(TAG, "MODE_REGISTER id is not registed!!");
			}
		}
		return 0;
	}
	public int runOnoffCommand() {
		if (mode == MODE_REGISTER_ONOFF) {
			int[] regdat = getRegInfoById(itemId);
			if (regdat[0] == -1) {
				Log.e(TAG, "MODE_REGISTER_ONOFF id not registed!!");
				return 0;
			}
			int reg = regdat[REGDATA_REG];
			int mask = regdat[REGDATA_MASK];
			int regtype = regdat[REGDATA_TYPE];
			switch (itemId ) {
				case Constant.PQ_COLOR_SETTING_ICC_ENABLE:
				case Constant.PQ_COLOR_SETTING_IBC_ENABLE:
				case Constant.PQ_COLOR_SETTING_IHC_ENABLE:
					if (regtype == 0) {//sc reg
						setScReg8(reg, (currentIndex==1)?mask:0, mask);
					} else {
						setReg8(reg, (currentIndex==1)?mask:0, mask);
					}
					break;
				default:
					Log.e(TAG, "MODE_REGISTER_ONOFF not run this id " + itemId);
					break;
			}

		}
		
		return 0;
	}
	public int runOtherCommand() {
		
		switch (itemId ) {
		case Constant.PQ_ADJUST_GAMMA:
			TvCommonManager.getInstance().setTvosCommonCommand("SetGammaTab#" + currentIndex);
			break;
		case Constant.PQ_ADJUST_DLC:
			TvCommonManager.getInstance().setTvosCommonCommand("SetDLCCurve#" + currentIndex);
			break;
		default:
			Log.e(TAG, "MODE_OTHER not run this id " + itemId);
			break;
		}
		return 0;
	}

	static int setScReg8(int reg, int val, int mask) {
		int ret[] = TvCommonManager.getInstance().setTvosCommonCommand("SetScReg8#"+reg+"#"+val+"#"+mask);
		Log.d(TAG, "ret values: " + ret[0] + "," + ret.length);
		return ret[0] & mask;
	}
	static int getScReg8(int reg, int mask) {
		int ret[] = TvCommonManager.getInstance().setTvosCommonCommand("GetScReg8#"+reg+"#"+mask);
		Log.d(TAG, "ret values: " + ret[0] + "," + ret.length);
		return ret[0] & mask;
	}
	static int setReg8(int reg, int val, int mask) {
		int ret[] = TvCommonManager.getInstance().setTvosCommonCommand("SetReg8#"+reg+"#"+val+"#"+mask);
		Log.d(TAG, "ret values: " + ret[0] + "," + ret.length);
		return ret[0] & mask;
	}
	static int getReg8(int reg, int mask) {
		int ret[] = TvCommonManager.getInstance().setTvosCommonCommand("GetReg8#"+reg+"#"+mask);
		Log.d(TAG, "ret values: " + ret[0] + "," + ret.length);
		return ret[0] & mask;
	}

	static int getQmapTableNum(int _mode, int _type, int _ipIndex) {
		if (_mode == MODE_QMAP) {
			if (_type == PQTYPE_BIG) {
				return TvFactoryManager.getInstance().getQmapTableNum(_ipIndex);//
			} else if (_type == PQTYPE_SMALL) {
				return TvFactoryManager.getInstance().getQmapCustomerTableNum(_ipIndex);//
			}
		}
		return 0;
	}

	static int getQmapTableNum(PqDataContent pqc) {
		if (pqc.pqMode == MODE_QMAP) {
			if (pqc.pqType == PQTYPE_BIG) {
				return TvFactoryManager.getInstance().getQmapTableNum(pqc.pqIpIndex);//
			} else if (pqc.pqType == PQTYPE_SMALL) {
				return TvFactoryManager.getInstance().getQmapCustomerTableNum(pqc.pqIpIndex);//
			}
		}
		return 0;
	}

	static String getQmapTableName(int _mode, int _type, int _ipIndex, int _tableIndex) {
		if (_mode == MODE_QMAP) {
			if (_type == PQTYPE_BIG) {
				return TvFactoryManager.getInstance().getQmapTableName(_ipIndex, _tableIndex);//
			} else if (_type == PQTYPE_SMALL) {
				return TvFactoryManager.getInstance().getQmapCustomerTableName(_ipIndex, _tableIndex);//
			}
		}
		return "dummy";
	}

	static String getQmapTableName(PqDataContent pqc, int _tableIndex) {
		if (pqc.pqMode == MODE_QMAP) {
			if (pqc.pqType == PQTYPE_BIG) {
				return TvFactoryManager.getInstance().getQmapTableName(pqc.pqIpIndex, _tableIndex);//
			} else if (pqc.pqType == PQTYPE_SMALL) {
				return TvFactoryManager.getInstance().getQmapCustomerTableName(pqc.pqIpIndex, _tableIndex);//
			}
		}
		return "dummy";
	}

	static public int getQmapCurrentTableIdx(int _mode, int _type, int _ipIndex) {
		if (_mode == MODE_QMAP) {
			if (_type == PQTYPE_BIG) {
				return TvFactoryManager.getInstance().getQmapCurrentTableIdx(_ipIndex);//
			} else if (_type == PQTYPE_SMALL) {
				return TvFactoryManager.getInstance().getQmapCustomerCurrentTableIdx(_ipIndex);//
			}
		}
		return 0;
	}

	static public int getQmapCurrentTableIdx(PqDataContent pqc) {
		if (pqc.pqMode == MODE_QMAP) {
			if (pqc.pqType == PQTYPE_BIG) {
				return TvFactoryManager.getInstance().getQmapCurrentTableIdx(pqc.pqIpIndex);//
			} else if (pqc.pqType == PQTYPE_SMALL) {
				return TvFactoryManager.getInstance().getQmapCustomerCurrentTableIdx(pqc.pqIpIndex);//
			}
		}
		return 0;
	}

	public static class PqDataContent {
		public int pqIpIndex;
		public int pqMode;
		public int pqType;

		PqDataContent(int mode, int pqType, int pqIpIndex) {
			this.pqMode = mode;
			this.pqType = pqType;
			this.pqIpIndex = pqIpIndex;
		}
	}

	static int[] getRegInfoById(int id) {
		int[] ret;

		int index = 0;
		while (true) {
			ret = regData[index];
			if (ret[0] == -1)
				break;
			if (ret[0] == id)
				break;
			index++;
		}

		return ret;
	}

	static public int getRegValueById(int itemId) {
		int ret = 0;
		int[] res = getRegInfoById(itemId);

		if (res[0] == -1) {
			Log.e(TAG, "can not find reginfo for id " + itemId);
			return 0;
		}

		if (res[REGDATA_TYPE] == 0) {//scaler register
			ret = getScReg8(res[REGDATA_REG], res[REGDATA_MASK]);
		} else {
			ret = getReg8(res[REGDATA_REG], res[REGDATA_MASK]);
		}

		if (!getRegValueSignById(itemId)) // minus
			ret = -ret;

		return ret;
	}

	// true - plus,   false  - minus.
	static public boolean getRegValueSignById(int itemId) {
		int ret = 0;
		int[] res;
		int index = 0;

		while (true) {
			res = regSignData[index];
			if (res[0] == -1)
				break;
			if (res[0] == itemId)
				break;
			index++;
		}

		if (res[0] == -1) {
			Log.e(TAG, "can not find reginfo for id " + itemId);
			return true; // default return plus sign if not defined.
		}

		if (res[REGDATA_TYPE] == 0) {//scaler register
			ret = getScReg8(res[REGDATA_REG], res[REGDATA_MASK]);
		} else {
			ret = getReg8(res[REGDATA_REG], res[REGDATA_MASK]);
		}

		// 0 is plus.
		return (ret == 0);
	}

	static public boolean setRegValueSignById(int itemId, boolean setToPlus) {
		int ret = 0;
		int[] res;
		int index = 0;

		while (true) {
			res = regSignData[index];
			if (res[0] == -1)
				break;
			if (res[0] == itemId)
				break;
			index++;
		}

		if (res[0] == -1) {
			Log.e(TAG, "can not find reginfo for id " + itemId);
			return true; // default return plus sign if not defined.
		}

		int dvalue = 0xFF;
		if (setToPlus)
			dvalue = 0;

		if (res[REGDATA_TYPE] == 0) {//scaler register
			ret = setScReg8(res[REGDATA_REG], dvalue, res[REGDATA_MASK]);
		} else {
			ret = setReg8(res[REGDATA_REG], dvalue, res[REGDATA_MASK]);
		}

		// 0 is plus.
		return (ret == 0);
	}

	static public boolean hasSign(int itemId) {
		int[] res;
		int index = 0;

		while (true) {
			res = regSignData[index];
			if (res[0] == -1)
				return false;
			if (res[0] == itemId)
				break;
			index++;
		}
		// 0 is plus.
		return true;
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

	final static int REGDATA_ID = 0;
	final static int REGDATA_TYPE = 1; // 0-scale reg, 1-normal reg.
	final static int REGDATA_REG = 2;
	final static int REGDATA_MASK = 3;
	final static int REGDATA_MIN = 4;
	final static int REGDATA_MAX = 5;
	final static int [][]regData = {
		//     id                        type    reg    mask    min    max
			// icc
		{Constant.PQ_COLOR_SETTING_ICC_R,  0,   	0x2BC3,  0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_G,  0,   	0x2BC4,  0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_B,  0,   	0x2BC5, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_C,  0,   	0x2BC6, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_M,  0,   	0x2BC7, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_Y,  0,   	0x2BC8, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_F,  0,   	0x2BC9, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_GY,  0,   	0x2BCA, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_BY,  0,   	0x2BCB, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_FLESH,  0,   0x2BCC, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_REDDISH,  0, 0x2BCD, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_LIP,  0,   	0x2BCE, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_HAIR,  0,   	0x2BCF, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_MEAT,  0,   	0x2BD0, 0x0F,   0,     15},
		{Constant.PQ_COLOR_SETTING_ICC_DARK,  0,   	0x2BD1, 0x0F,   0,     15},
			// ibc
		{Constant.PQ_COLOR_SETTING_IBC_R,  0,   	0x1C23, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_G,  0,   	0x1C24, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_B,  0,   	0x1C25, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_C,  0,   	0x1C26, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_M,  0,   	0x1C27, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_Y,  0,   	0x1C28, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_F,  0,   	0x1C29, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_GY,  0,   	0x1C2A, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_BY,  0,   	0x1C2B, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_FLESH,  0,   0x1C2C, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_REDDISH,  0, 0x1C2D, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_LIP,  0,   	0x1C2E, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_HAIR,  0,   	0x1C2F, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_MEAT,  0,   	0x1C30, 0x3F,   0,     63},
		{Constant.PQ_COLOR_SETTING_IBC_DARK,  0,   	0x1C31, 0x3F,   0,     63},
			// ihc
		{Constant.PQ_COLOR_SETTING_IHC_R,  0,   	0x1C4B, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_G,  0,   	0x1C4C, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_B,  0,   	0x1C4D, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_C,  0,   	0x1C4E, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_M,  0,   	0x1C4F, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_Y,  0,   	0x1C50, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_F,  0,   	0x1C51, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_GY,  0,   	0x1C52, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_BY,  0,   	0x1C53, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_FLESH,  0,   0x1C54, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_REDDISH,  0, 0x1C55, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_LIP,  0,   	0x1C56, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_HAIR,  0,   	0x1C57, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_MEAT,  0,   	0x1C58, 0x7F,   0,     127},
		{Constant.PQ_COLOR_SETTING_IHC_DARK,  0,   	0x1C59, 0x7F,   0,     127},
		{Constant.PQ_LUMA_SETTING_PRE_YOFFSET,  0,	0x1A1E, 0xFF,   0,     255},
		{Constant.PQ_LUMA_SETTING_PRE_YGAIN,  0,   	0x1A2C, 0x7F,   0,     127},
			// comb
		{Constant.PQ_LUMA_SETTING_COMB_CONTRAST, 1, 0x103673, 0xFF,   0,     255},
		{Constant.PQ_LUMA_SETTING_COMB_BRIGHTNESS,  1,   0x103674, 0xFF,   0,     255},
		{Constant.PQ_LUMA_SETTING_COMB_SATURATION,  1,   0x103675, 0xFF,   0,     255},
		{Constant.PQ_COLOR_SETTING_ICC_ENABLE,      0,  0x2BC0, 1<<6,  1<<6, 1<<6},
		{Constant.PQ_COLOR_SETTING_IBC_ENABLE,      0,  0x1C20, 1<<7,  1<<7, 1<<7},
		{Constant.PQ_COLOR_SETTING_IHC_ENABLE,      0,  0x1C48, 1<<7,  1<<7, 1<<7},
		{Constant.PQ_CTI_SETTING_COMB_83,           1,  0x103683, 0xFF, 0, 0xFF},
		{Constant.PQ_CTI_SETTING_POST_CTI_COEF,     0,  0x2744,   0xFF, 0, 0xFF},
		{-1, -1, -1, -1, -1, -1},
	};

	final static int [][]regSignData = {
		//     id                        type    reg    mask    min    max
		// icc sign bit. 0 is plus, 1 is minus.
		{Constant.PQ_COLOR_SETTING_ICC_R,       0,   	0x2BD2,  0x01,   0x01,     0x01},
		{Constant.PQ_COLOR_SETTING_ICC_G,       0,   	0x2BD2,  0x02,   0x02,     0x02},
		{Constant.PQ_COLOR_SETTING_ICC_B,       0,   	0x2BD2,  0x04,   0x04,     0x04},
		{Constant.PQ_COLOR_SETTING_ICC_C,       0,   	0x2BD2,  0x08,   0x08,     0x08},
		{Constant.PQ_COLOR_SETTING_ICC_M,       0,   	0x2BD2,  0x10,   0x10,     0x10},
		{Constant.PQ_COLOR_SETTING_ICC_Y,       0,   	0x2BD2,  0x20,   0x20,     0x20},
		{Constant.PQ_COLOR_SETTING_ICC_F,       0,   	0x2BD2,  0x40,   0x40,     0x40},
		{Constant.PQ_COLOR_SETTING_ICC_GY,      0,   	0x2BD3,  0x80,   0x80,     0x80},
		{Constant.PQ_COLOR_SETTING_ICC_BY,      0,   	0x2BD3,  0x01,   0x01,     0x01},
		{Constant.PQ_COLOR_SETTING_ICC_FLESH,   0,   	0x2BD3,  0x02,   0x02,     0x02},
		{Constant.PQ_COLOR_SETTING_ICC_REDDISH, 0,   	0x2BD3,  0x04,   0x04,     0x04},
		{Constant.PQ_COLOR_SETTING_ICC_LIP,     0,   	0x2BD3,  0x08,   0x08,     0x08},
		{Constant.PQ_COLOR_SETTING_ICC_HAIR,    0,   	0x2BD3,  0x10,   0x10,     0x10},
		{Constant.PQ_COLOR_SETTING_ICC_MEAT,    0,   	0x2BD3,  0x20,   0x20,     0x20},
		{Constant.PQ_COLOR_SETTING_ICC_DARK,    0,   	0x2BD3,  0x40,   0x40,     0x40},
		{-1, -1, -1, -1, -1, -1},
	};
}
