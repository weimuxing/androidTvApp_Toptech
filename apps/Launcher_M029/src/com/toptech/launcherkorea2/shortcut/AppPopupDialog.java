package com.toptech.launcherkorea2.shortcut;

import java.util.HashMap;
import java.util.List;
import com.toptech.launcherkorea2.R;
import com.toptech.launcherkorea2.dock.PackageInformation;
import com.toptech.launcherkorea2.logic.MainService;
import com.toptech.launcherkorea2.logic.Task;
import com.toptech.launcherkorea2.logic.TaskType;
import com.toptech.launcherkorea2.utils.Funs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AppPopupDialog extends Dialog implements android.view.View.OnKeyListener{

	private Context mContext;
	private List<PackageInformation> _selectedApkList = null;
	
	private ImageView close_popuwin = null;
	private GridView appGrid = null;
	private AppPopupAdapter appPopupAdapter = null;
	private List<PackageInformation> allAppList = null;
	private View view;
	public AppPopupDialog(Context mContext, List<PackageInformation> selectedApkList) {
		super(mContext);
		this.mContext = mContext;
		this._selectedApkList = selectedApkList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setGravity(Gravity.CENTER);
		window.requestFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view= inflater.inflate(R.layout.shortcut_popup, null);
		setContentView(view);
		
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Point outSize = new Point();
		display.getSize(outSize);
		WindowManager.LayoutParams lParams = getWindow().getAttributes();
		lParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		lParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(lParams);
		setCanceledOnTouchOutside(false);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		view.setBackgroundResource(R.drawable.round_win);
		close_popuwin = (ImageView)view.findViewById(R.id.close_popuwin);
		close_popuwin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveAndClosePopup();
			}
		});

		appGrid = (GridView)view.findViewById(R.id.app_grid);

		allAppList = Funs.getAllApks(mContext);
		appPopupAdapter = new AppPopupAdapter(mContext,
				R.layout.shortcut_popup_item, 
				new String[] { "icon", "ckbox","name" }, 
				new int[] { R.id.popu_app_img,R.id.app_check, R.id.popu_app_text }, 
				allAppList,
				_selectedApkList);
		appGrid.setAdapter(appPopupAdapter);
		appGrid.requestFocus(); 
		appGrid.setOnKeyListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				if (v.getId() == R.id.app_grid) {
					return appPopupAdapter.onEnterKeyListener(appGrid.getSelectedItemPosition());
				}
				break;
			case KeyEvent.KEYCODE_ESCAPE:
			case KeyEvent.KEYCODE_BACK:
				saveAndClosePopup();
				return true;
			}
		}
		return false;
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
							_selectedApkList = appPopupAdapter.getApkList();
							// myImageAdapter.DataChanged(selectedApkList);
							HashMap<String, Object> params = new HashMap<String, Object>();
							params.put("data", _selectedApkList);
							MainService.newTask(new Task(TaskType.UPDATE_APP_RECORD, params));
							AppPopupDialog.this.dismiss();
							dialog.dismiss();
						}
					});
			// 取消按钮
			builder.setNegativeButton(R.string.cancle,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();

		}
}
