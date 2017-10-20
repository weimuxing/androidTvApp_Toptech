package com.toptech.launcherkorea2.shortcut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.dock.PackageInformation;
import com.toptech.launcherkorea2.logic.MainService;
import com.toptech.launcherkorea2.logic.Task;
import com.toptech.launcherkorea2.logic.TaskType;
import com.toptech.launcherkorea2.utils.Funs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;

public class AppPopupWindow implements OnClickListener {
	private final static String TAG = "ShortCupPopouWindow";

	private Context mContext;
	private LayoutInflater inflater;
	private LinearLayout layout;
	private View _anchor;

	private PopupWindow popupWindow = null;

	private ImageView close_popuwin = null;
	private CheckBox all_check = null;
	private CheckBox rev_check = null;
	private int currck = 0;// 0表示全选选中，1表示反选选中
	private GridView appGrid = null;
	private AppPopupAdapter appPopupAdapter = null;
	private List<PackageInformation> allAppList = null;
	private List<PackageInformation> _selectedApkList = null;// 当前选中apk列表集合

	public AppPopupWindow(Context context, View anchor,List<PackageInformation> selectedApkList) {
		mContext = context;
		_anchor = anchor;
		_selectedApkList = selectedApkList;
		inflater = (LayoutInflater) mContext.getSystemService("layout_inflater");
		layout = (LinearLayout) inflater.inflate(R.layout.shortcut_popup, null,true);
		layout.setBackgroundResource(R.drawable.round_win);

		init();
	}

	/**
	 * 初始化UI
	 */
	private void init() {
		// 增加全选和反选功能
		all_check = (CheckBox) layout.findViewById(R.id.all_check);
		rev_check = (CheckBox) layout.findViewById(R.id.rev_check);

		all_check.setOnClickListener(this);
		rev_check.setOnClickListener(this);
		all_check.setOnKeyListener(keyListener);
		rev_check.setOnKeyListener(keyListener);

		close_popuwin = (ImageView) layout.findViewById(R.id.close_popuwin);
		close_popuwin.setOnClickListener(this);
		appGrid = (GridView) layout.findViewById(R.id.app_grid);

		allAppList = Funs.getAllApks(mContext);
		appPopupAdapter = new AppPopupAdapter(mContext,
				R.layout.shortcut_popup_item, 
				new String[] { "icon", "ckbox","name" }, 
				new int[] { R.id.popu_app_img,R.id.app_check, R.id.popu_app_text }, 
				allAppList,
				_selectedApkList);
		appGrid.setAdapter(appPopupAdapter);
		 appGrid.requestFocus(); //skye 20140702
		appGrid.setOnKeyListener(keyListener);
		// 创建popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.showAtLocation(_anchor, Gravity.CENTER_VERTICAL, 50, 0);

	}

	// 显示弹出窗口
	public void show() {
		if (popupWindow != null && !popupWindow.isShowing()) {
			popupWindow.showAtLocation(_anchor, Gravity.CENTER, 0, 0);
		}
	}

	// 判断窗口是否已经打开
	public boolean isShowing() {
		if (popupWindow == null) {
			return false;
		} else {
			return popupWindow.isShowing();
		}
	}

	// 关闭快捷方式添加窗口提示
	public void saveAndClosePopup() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.tips);
		builder.setMessage(R.string.savemsg);
		// 确定按钮
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						popupWindow.dismiss();
						//Log.d("wangdy", "AppPopupWindow-saveAndClosePopup-click yes");
						_selectedApkList = appPopupAdapter.getApkList();
						// myImageAdapter.DataChanged(selectedApkList);
						HashMap<String, Object> params = new HashMap<String, Object>();
						params.put("data", _selectedApkList);
						MainService.newTask(new Task(TaskType.UPDATE_APP_RECORD, params));

					}
				});
		// 取消按钮
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//popupWindow.dismiss();
						//Log.d("wangdy", "AppPopupWindow-saveAndClosePopup-click no");
						//appPopupAdapter.setSelectedApk(_selectedApkList);

						//HashMap<String, Object> params = new HashMap<String, Object>();
						//params.put("data", _selectedApkList);
						//MainService.newTask(new Task(TaskType.UPDATE_APP_RECORD, params));
					}
				});
		builder.create().show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_popuwin:
			rev_check.setChecked(false);
			all_check.setChecked(false);
			Log.d(TAG, "close the AppPopupWindow....");
			// 保存数据库记录并关闭弹窗
			saveAndClosePopup();
			break;
		case R.id.all_check:
			rev_check.setChecked(false);
			if (all_check.isChecked()) {
				appPopupAdapter.setSelectedApk(allAppList);
				// _selectedApkList = appPopupAdapter.getApkList();
			} else {
				// appPopupAdapter.setSelectedApk(_selectedApkList);
				for (int i = 0; i < allAppList.size(); i++)
					appPopupAdapter.onEnterKeyListener(i);
			}
			break;
		case R.id.rev_check:
			all_check.setChecked(false);
			if (rev_check.isChecked()) {
				List<PackageInformation> temp_list = new ArrayList<PackageInformation>();
				for (PackageInformation p1 : allAppList) {
					boolean flag = false;
					for (PackageInformation p2 : _selectedApkList) {
						if (p2.getPackageName().equals(p1.getPackageName())) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						temp_list.add(p1);
					}
				}

				appPopupAdapter.setSelectedApk(temp_list);
			} else {
				appPopupAdapter.setSelectedApk(_selectedApkList);
			}
			break;
		}

	}

	OnKeyListener keyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int arg1, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_ENTER:
				case KeyEvent.KEYCODE_DPAD_CENTER:
					if (v.getId() == R.id.app_grid) {
						Log.d("zhongbin", "grid enter key press");
						//Log.d("wangdy", "AppPopupWindow-keyListener-KEYCODE_ENTER");
						return appPopupAdapter.onEnterKeyListener(appGrid.getSelectedItemPosition());
					}
					break;
				case KeyEvent.KEYCODE_ESCAPE:
				case KeyEvent.KEYCODE_BACK:
					//Log.d("wangdy", "AppPopupWindow-keyListener-KEYCODE_BACK");
					saveAndClosePopup();
					return true;
				}
			}
			return false;
		}
	};
}
