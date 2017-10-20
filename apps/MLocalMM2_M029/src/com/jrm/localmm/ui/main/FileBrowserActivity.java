//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package com.jrm.localmm.ui.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager.OnDismissListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageParser.NewPermissionInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jrm.localmm.R;
import com.jrm.localmm.business.adapter.DataAdapter;
import com.jrm.localmm.business.adapter.GridAdapter;
import com.jrm.localmm.business.adapter.ViewModeAdapter;
import com.jrm.localmm.business.data.BaseData;
import com.jrm.localmm.business.data.ViewMode;
import com.jrm.localmm.ui.MediaContainerApplication;
import com.jrm.localmm.ui.video.DatabaseHelper;
import com.jrm.localmm.ui.video.FloatVideoController;
import com.jrm.localmm.ui.video.VideoPlayerActivity;
import com.jrm.localmm.util.Constants;
import com.jrm.localmm.util.CopyFileUtils;
import com.jrm.localmm.util.FileControl;
import com.jrm.localmm.util.FileInfo;
import com.jrm.localmm.util.Tools;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

public class FileBrowserActivity extends Activity {

    private static final String TAG = "FileBrowserActivity";

    // control container class
    private FileBrowserViewHolder holder = null;

    // homepage surface data browsing class
    private TopDataBrowser topDataBrowser = null;

    // local usb disk data browsing class
    private LocalDataBrowser localDataBrowser = null;

    // samba data browsing class
    private SambaDataBrowser sambaDataBrowser = null;

    // dlna data browsing class
    private DlnaDataBrowser dlnaDataBrowser = null;

    // listView adapter
    private DataAdapter adapter = null;

    // the flag means what the view mode now is
    public static int mListOrGridFlag = Constants.LISTVIEW_MODE;
    // make gridview open or not while enter LocalMM first time
    public static int isOrNotGridViewFirst =Constants.LISTVIEW_MODE_FIRST;

    public static int clickBtnswitch=0;

    public static int backAndNextViewFormat=0;

    public static ArrayList<Drawable> fileTypeDrawable = new ArrayList<Drawable>();

    private View.OnClickListener switchPage;

    private ArrayList<BaseData> tmpArray = new ArrayList<BaseData>();

    public static GridAdapter gridAdapter;

    private Resources resourceGrid;

    private static int positionFocusNow =-100;

    private Drawable listviewModeDrawable;

    private Drawable gridviewModeDrawable;

    // listView data source
    private List<BaseData> sourceData = new ArrayList<BaseData>();

    private List<BaseData> desDataList = new ArrayList<BaseData>();

    // currently browsing data sources
    private int m_dataSource = Constants.BROWSER_TOP_DATA;

    private int lastDataSource = Constants.BROWSER_TOP_DATA;

    // temporary save data type
    protected int tmpType = Constants.OPTION_STATE_ALL;

    // currently browsing data types
    protected int dataType = Constants.OPTION_STATE_ALL;

    // Touch events when DOWN the y coordinate
    private int motionY = 0;

    // key shielding identification
    private boolean m_canResponse = true;

    private TvCommonManager appSkin = null;

    private EnumInputSource inputSource;

    //video thumbnail
    private MediaThumbnail mediaThumbnail;

    protected PopupWindow popupWindow;

    private static Toast toast;

    protected ArrayList<ViewMode> viewModeList;
    protected boolean viewModeChange = false;

    private String mPlatform;

  //zb20150613 add
  	private boolean enablePaste = false;
  	private boolean is_cope = false;	
  	private boolean is_move = false;	
  	private boolean is_rename = false;	
  	Dialog mDelDialog = null;	
  	Dialog editorDialog = null;
  	Resources resources = null;
  	public static String currFocusPathString=null;
  	public FileControl mFileControl = null;
  	private CopyFileUtils mCopyFileUtils = null;	
  	Handler mCopyHandler = new Handler();	
  	public static final int DEVICE_MOUNT_CHANGE = 0;
  	public static final int NETWORK_STATUS_CHANGE = DEVICE_MOUNT_CHANGE+1;
  	public static final int STOP_COPY = NETWORK_STATUS_CHANGE+1;
  	public static final int SHOW_COPYDIALOG_GETFILECOUNT = STOP_COPY+1;
  	public static final int MOUNT = 0;
  	public static final int REMOVE = MOUNT+1;
  	public static final int SHOW_DIALOG = 99;
  	public static final int STOP_DIALOG = 100;
  	public static final int SHOW_LOGINFAIL_DIALOG = 101;
  	public static final int CLEAR_CONTENTLIST = 102;
  	private String currSourcePath=null;
  	private ArrayList<FileInfo> multi_path = null;
  	private int curr_path = 0;
  	View myCopyView;
  	AlertDialog copyingDialog;
  	ProgressDialog  GetFileCountDialog = null;
  	public ProgressDialog mWaitDialog;
  	Handler mCopyingHandler = new Handler();	
  	private static final int DIALOG_EDITE = 1;					
  	private static final int DIALOG_DEL = DIALOG_EDITE + 1;		
  	public ProgressDialog delDialog = null;
  	private String source_path = null;	
  	private String target_path = null;
  	private int currFocusPosition=0;
  	String usb_path = new String("/mnt/usb/");
    String sdcard_path = new String("/mnt/sd_card");
    View myView;
    EditText myEditText;
    private final int MSG_BEGIN_DEL = 0;
	private final int MSG_SCAN_DEL = 1;
	private String recoverFileValString = null;
	
    public Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case STOP_COPY:
					boolean disconnect = (msg.arg1==0);
					String mSource = mCopyFileUtils.getSourcePath();
					String mTarget = mCopyFileUtils.getTargetPath();
					if(msg.arg1 == CopyFileUtils.NO_SPACE)
					{
						StopCopy();
						mCopyFileUtils.mCopyResult = CopyFileUtils.COPY_OK;
						showCopyFailDialog(R.string.full);
					}
					break;

				case SHOW_COPYDIALOG_GETFILECOUNT:
					showGetFileCountDialog();
					mCopyingHandler.postDelayed(mCopyingRun, 10);
					break;
				case SHOW_DIALOG:
					showWaitDialog();
					break;
				case STOP_DIALOG:
					if (mWaitDialog != null){
	    				mWaitDialog.dismiss();
	    			}
					break;
					
			}
		}
	};
	Runnable mCopyingRun = new Runnable() {
		public void run() { 
			mCopyFileUtils.getCopyFileCount(multi_path);
			if(GetFileCountDialog != null)
			{
				GetFileCountDialog.dismiss();
				GetFileCountDialog = null;
			}
			mCopyFileUtils.is_copy_finish = false;  
			mCopyFileUtils.is_enable_copy = true;
			
			showCopyDialog();
			mCopyHandler.postDelayed(mCopyRun, 10);
		}
	};
    public void showWaitDialog(){
		mWaitDialog = new ProgressDialog(FileBrowserActivity.this);
		mWaitDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				mFileControl.is_enable_fill = false;
				dialog.dismiss();
			}
			
		});
		mWaitDialog.setMessage(resources.getString(R.string.openning));
		mWaitDialog.setCancelable(false);
		
		mWaitDialog.show();
    	
    }
	private void showGetFileCountDialog()
	{
		GetFileCountDialog = new ProgressDialog(this);
		String msg = this.getResources().getString(R.string.copy_get_file_count);
		GetFileCountDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		GetFileCountDialog.setMessage(msg);
		GetFileCountDialog.setIcon(0);
		GetFileCountDialog.setIndeterminate(false);
		GetFileCountDialog.show();
		GetFileCountDialog.setOnCancelListener(null);
	}
	private void StopCopy()
	{
		CopyFileUtils.is_enable_copy = false;
		CopyFileUtils.is_copy_finish = true;
		FileControl.is_enable_del = false;
		FileControl.is_enable_fill = false;
		
		mCopyFileUtils.setSourcePath("");
		mCopyFileUtils.setTargetPath("");
				
		if(copyingDialog != null)
		{
			copyingDialog.dismiss();
			copyingDialog = null;
		}
	}
	private void showCopyFailDialog(int reson)
	{
		Dialog CopyFailDialog = new AlertDialog.Builder(this)
		    .setTitle(R.string.copy_fail)
			.setMessage(reson)
			.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog,int which) 
				{
					dialog.dismiss();
				}
			}).show();
	}
	
	private void showCopyDialog()
	{
		if(copyingDialog == null)
			{
				LayoutInflater factory=LayoutInflater.from(FileBrowserActivity.this);
				myCopyView=factory.inflate(R.layout.copy_dialog,null);
				copyingDialog = new AlertDialog.Builder(FileBrowserActivity.this).create();
				((Button)myCopyView.findViewById(R.id.but_stop_copy)).setOnClickListener(new View.OnClickListener(){
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(copyingDialog != null){		
							CopyFileUtils.is_copy_finish = true;
							CopyFileUtils.is_enable_copy = false;
	                	}
					}
				});
				copyingDialog.setView(myCopyView);
		        copyingDialog.show();
		        copyingDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}
			else
			{
				copyingDialog.show();
				copyingDialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}
	}
	private void reMedioScan(String path)
	{
		handler.sendEmptyMessage(Constants.REFRESH_DATA);
	}
	
    public String getChangePath(String content){
//	String ret = "";
//        if(content == null)
//                return null;
//        if(content.startsWith(usb_path)){
//                ret = getString(R.string.str_usb1_name) + content.substring(usb_path.length());
//        }else if(content.startsWith(sdcard_path)){
//                ret = getString(R.string.str_sdcard_name) + content.substring(sdcard_path.length());
//        }else{
//                ret = content;
//        }
	return content;
    }
	
	Runnable mCopyRun = new Runnable() {
		public void run() { 
			if(CopyFileUtils.is_copy_finish){
				if ((CopyFileUtils.cope_now_sourceFile != null)
						&& (CopyFileUtils.cope_now_targetFile != null)) {
					int percent = (int) ((((float) CopyFileUtils.mHasCopytargetFileSize) / ((float) CopyFileUtils.cope_now_sourceFile
							.length())) * 100);
				
					((ProgressBar) FileBrowserActivity.this.myCopyView.findViewById(R.id.one_copy_percent)).setProgress((int) percent);
					((TextView) myCopyView.findViewById(R.id.one_percent_Text))
							.setText(percent + " %");

					percent = (int) ((((float) CopyFileUtils.mhascopyfilecount) / ((float) CopyFileUtils.mallcopyfilecount)) * 100);
					((ProgressBar) myCopyView.findViewById(R.id.all_copy_percent)).setProgress((int) percent);
					((TextView) myCopyView.findViewById(R.id.all_percent_Text))
							.setText("" + CopyFileUtils.mhascopyfilecount
									+ " / " + CopyFileUtils.mallcopyfilecount);
				}
				
				
				CopyFileUtils.mHasCopytargetFileSize = 0;
				if(is_move && !CopyFileUtils.is_same_path){	
					is_move = false;
					is_cope = false;
					multi_path = new ArrayList<FileInfo>();	
					
					mFileControl.delFileInfo(mCopyFileUtils.get_has_copy_path());					
				}
				if(!CopyFileUtils.is_enable_copy){		
					mFileControl.DelFile(CopyFileUtils.mInterruptFile);
				}
				for(int i = 0; i < mCopyFileUtils.get_has_copy_path().size(); i ++){
					File tmp_file=new File(mFileControl.get_currently_path()+File.separator+mCopyFileUtils.get_has_copy_path().get(i).file.getName());
					FileInfo tmp_fileinfo = mFileControl.changeFiletoFileInfo(tmp_file);
					if(!mFileControl.is_contain_file(tmp_file)){
						if(!mFileControl.folder_array.contains(tmp_fileinfo)){
							if(tmp_file.isDirectory()){			
								mFileControl.folder_array.add(0, tmp_fileinfo);
							}else{
								mFileControl.folder_array.add(tmp_fileinfo);
							}
						}
					}
				}
				if(copyingDialog != null){				
					copyingDialog.dismiss();
					copyingDialog = null;
				}
					reMedioScan(mFileControl.get_currently_path());		
				CopyFileUtils.is_copy_finish = false;
				if(CopyFileUtils.is_not_free_space){
					CopyFileUtils.is_not_free_space = false;
					if(CopyFileUtils.pathError)
						Toast.makeText(FileBrowserActivity.this, getString(R.string.error_invalid_path), Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(FileBrowserActivity.this, getString(R.string.err_not_free_space), Toast.LENGTH_SHORT).show();
				}
				is_cope = false;
				multi_path = new ArrayList<FileInfo>();	
				
				mCopyHandler.removeCallbacks(mCopyRun);
				try
				{
				    Thread.sleep(1000);
				}catch(Exception ex){
				    Log.e(TAG, "Exception: " + ex.getMessage());
				}
				
				mCopyFileUtils.setSourcePath("");
				mCopyFileUtils.setTargetPath("");
				
				CopyFileUtils.cope_now_sourceFile=null;
				CopyFileUtils.cope_now_targetFile=null;
				
			}else
			{
				if(enablePaste){							
					enablePaste = false;
					Log.d(TAG,"target path=="+currFocusPathString);
					Log.d(TAG,"source path=="+multi_path.get(0).file.getPath());
					Log.d(TAG,"target1 path=="+mFileControl.get_currently_path());
					mCopyFileUtils.CopyFileInfoArray(multi_path, mFileControl.get_currently_path());
				}
				if((CopyFileUtils.cope_now_sourceFile != null) && (CopyFileUtils.cope_now_targetFile != null)){
					((TextView)myCopyView.findViewById(R.id.source_Text)).setText(getChangePath(CopyFileUtils.cope_now_sourceFile.getPath()));
					if (getChangePath(CopyFileUtils.cope_now_targetFile.getPath()).contains("/storage/emulated/0")) {
					((TextView)myCopyView.findViewById(R.id.target_Text)).setText(getChangePath(CopyFileUtils.cope_now_targetFile.getPath()).replace("/storage/emulated/0", "/Internal Storage"));
					}else {
						((TextView)myCopyView.findViewById(R.id.target_Text)).setText(getChangePath(CopyFileUtils.cope_now_targetFile.getPath()));
					}
					int percent = (int)((((float)CopyFileUtils.mHasCopytargetFileSize) / ((float)CopyFileUtils.cope_now_sourceFile.length())) * 100);
					((ProgressBar)myCopyView.findViewById(R.id.one_copy_percent)).setProgress((int)percent);
					((TextView)myCopyView.findViewById(R.id.one_percent_Text)).setText(percent + " %");
					
					percent = (int)((((float)CopyFileUtils.mhascopyfilecount) / ((float)CopyFileUtils.mallcopyfilecount)) * 100);
					((ProgressBar)myCopyView.findViewById(R.id.all_copy_percent)).setProgress((int)percent);
					((TextView)myCopyView.findViewById(R.id.all_percent_Text)).setText(""+CopyFileUtils.mhascopyfilecount+" / "+CopyFileUtils.mallcopyfilecount);
				}
				
				if(CopyFileUtils.mRecoverFile != null){
					if (CopyFileUtils.mRecoverFile.toString().contains("/storage/emulated/0")) {
					 recoverFileValString = CopyFileUtils.mRecoverFile.toString().replace("/storage/emulated/0", "/Internal Storage");
					}else if (CopyFileUtils.mRecoverFile.toString().contains("/mnt/usb")) {
						recoverFileValString = CopyFileUtils.mRecoverFile.toString();
						String[] tmp=recoverFileValString.split("/");
		        		tmp[3]=Constants.UsbName;
		        		recoverFileValString="";
		        		for(int i=1;i<tmp.length;i++)
		        		{
		        			recoverFileValString +="/";
		        			recoverFileValString +=tmp[i];
		        		}
		        		recoverFileValString=recoverFileValString.replace("/mnt/usb/", "/");
					}else{
						recoverFileValString = CopyFileUtils.mRecoverFile.toString();
					}
					new AlertDialog.Builder(FileBrowserActivity.this)
                    .setMessage(recoverFileValString +" "+ getString(R.string.copy_revocer_text))
                    .setPositiveButton(getString(R.string.copy_revocer_yes),new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog, int which)
                      {
                    	  CopyFileUtils.is_recover = true;					
                    	  CopyFileUtils.is_wait_choice_recover = false;		
                    	  dialog.dismiss();
                      }
                    }) 
                    .setNegativeButton(getString(R.string.copy_revocer_no),new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog,int which)
                      {
                    	  CopyFileUtils.is_recover = false;					
                    	  CopyFileUtils.is_wait_choice_recover = false;		
                    	  dialog.dismiss();
                      }
                    }).show();
					CopyFileUtils.mRecoverFile = null;		
				}
				
				if ((CopyFileUtils.cope_now_sourceFile == null)
						&& (CopyFileUtils.cope_now_targetFile == null)) 
				mCopyHandler.postDelayed(mCopyRun, 10);
				else mCopyHandler.postDelayed(mCopyRun, 500);
				
			}
		}
	};
	
	private View.OnClickListener EditorClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub			
			switch(v.getId()){
			case R.id.edit_copy:				
				is_move = false;
				is_cope = true;		
				currSourcePath=currFocusPathString;
				//mFileControl.currently_path=currSourcePath;
				if(multi_path == null || multi_path.size()>0)
				{
					multi_path=new ArrayList<FileInfo>();
				}
				multi_path.add(mFileControl.changeFiletoFileInfo(new File(currSourcePath)));
				break;
				
			case R.id.edit_delete:		
				currSourcePath=currFocusPathString;
				if(multi_path == null || multi_path.size()>0)
				{
					multi_path=new ArrayList<FileInfo>();
				}
				multi_path.add(mFileControl.changeFiletoFileInfo(new File(currSourcePath)));
				mDelDialog.show();				
				is_cope = false;
				break;
				
			case R.id.edit_move:					
				is_move = true; 
				is_cope = true;		
				currSourcePath=currFocusPathString;
				if(multi_path == null || multi_path.size()>0)
				{
					multi_path=new ArrayList<FileInfo>();
				}
				multi_path.add(mFileControl.changeFiletoFileInfo(new File(currSourcePath)));
				break;
				
			case R.id.edit_paste:		
				if(isTargetPahtRight()){	
					if(!PermissionTest()){
						Toast.makeText(FileBrowserActivity.this, resources.getString(R.string.write_error), Toast.LENGTH_SHORT).show();
						break;
					}

					if(is_cope)
						enablePaste = true;
					mHandler.sendEmptyMessage(SHOW_COPYDIALOG_GETFILECOUNT);
				}
				break;
				
			case R.id.edit_rename:		
				currSourcePath=currFocusPathString;
				if(multi_path == null || multi_path.size()>0)
				{
					multi_path=new ArrayList<FileInfo>();
				}
				multi_path.add(mFileControl.changeFiletoFileInfo(new File(currSourcePath)));
				if(multi_path.get(0).file.canWrite()){
					FileRename(multi_path.get(0));
				}
				is_cope = false;
				multi_path = new ArrayList<FileInfo>(); 
				break;
				
            case R.id.edit_cancel_btu:
                if(editorDialog != null)
					editorDialog.dismiss();

				return ;
        
			default:
				break;
			}
			if(editorDialog != null)
				editorDialog.dismiss();
		}
	};
	
	
	public void FileRename(final FileInfo mFileInfo)	
	{
		final File file = mFileInfo.file;
        LayoutInflater factory=LayoutInflater.from(FileBrowserActivity.this);
        myView=factory.inflate(R.layout.rename_alert_dialog,null);
        myEditText=(EditText)myView.findViewById(R.id.mEdit);
        myEditText.setText(file.getName());

        android.content.DialogInterface.OnClickListener listener2=
        new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
            String modName=myEditText.getText().toString();
	    if(modName.contains("\\") || modName.contains("/") || modName.contains(":")
                || modName.contains("*") || modName.contains("?") || modName.contains("\"")
                || modName.contains("<") || modName.contains(">") || modName.contains("|")){
                Toast.makeText(FileBrowserActivity.this, getString(R.string.rename_error), Toast.LENGTH_SHORT).show();
                return;
            }
            final String pFile=file.getParentFile().getPath()+"/";
            final String newPath=pFile+modName;

            if(new File(newPath).exists())
            {
              if(!modName.equals(file.getName()))
              {
                new AlertDialog.Builder(FileBrowserActivity.this)
                    .setTitle(getString(R.string.str_rename_notice))
                    .setMessage(getString(R.string.str_rename_file_exist))
                    .setPositiveButton(getString(R.string.str_OK),new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog, int which)
                      {
                        if(!file.renameTo(new File(newPath))){
                        	Toast.makeText(FileBrowserActivity.this, resources.getString(R.string.rename_fail), Toast.LENGTH_SHORT).show();
                        	is_rename = false;
                        	return;
                        }
                        mFileControl.folder_array.remove(mFileInfo);
                        is_rename = false; 

                        FileInfo tmp_fileinfo = mFileControl.changeFiletoFileInfo(new File(newPath));
                        mFileControl.folder_array.add(currFocusPosition, tmp_fileinfo);
          			    is_rename = false; 
          			    holder.listView.setSelection(currFocusPosition);
          			    	reMedioScan(mFileControl.get_currently_path());		
                        dialog.dismiss();
                      }
                    }) 
                    .setNegativeButton(getString(R.string.str_cancel),new DialogInterface.OnClickListener()
                    {
                      public void onClick(DialogInterface dialog,int which)
                      {
                    	  dialog.dismiss();
                      }
                    }).show();
              }
            }
            else
            {
              if(!file.renameTo(new File(newPath))){
            	  Toast.makeText(FileBrowserActivity.this, resources.getString(R.string.rename_fail), Toast.LENGTH_SHORT).show();
              	is_rename = false;
              	return;
              }
              mFileControl.folder_array.remove(mFileInfo);
              if(mFileInfo.isDir){
              	mFileControl.folder_array.add(0, mFileControl.changeFiletoFileInfo(new File(newPath)));
              }else{
              	mFileControl.folder_array.add(mFileControl.changeFiletoFileInfo(new File(newPath)));
              }
			  is_rename = false; 
			    	reMedioScan(mFileControl.get_currently_path());		
			  dialog.dismiss();
            }
          }
        };

        AlertDialog renameDialog = new AlertDialog.Builder(FileBrowserActivity.this).create();
        renameDialog.setView(myView);

        renameDialog.setButton(getString(R.string.str_OK),listener2);
        renameDialog.setButton2(getString(R.string.str_cancel),new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int which)
          {
          }
        });
        renameDialog.show();
	}
	
	private boolean PermissionTest(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String testfilename = mFileControl.get_currently_path()+"/"+sf.format(new Date());
		try{
		if (!new File(testfilename).createNewFile()){
			return false;
		}else{
			new File(testfilename).delete();
			return true;
		}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
    public boolean isTargetPahtRight(){
    	if(multi_path.get(0).file.getParent().equals(mFileControl.get_currently_path())){
    		Toast.makeText(this, getString(R.string.err_target_same), Toast.LENGTH_SHORT).show();
    		return false;
    	}else{
    		for(int i = 0; i < multi_path.size(); i ++){
    			if(multi_path.get(i).file.isDirectory()){
    				if(mFileControl.get_currently_path().startsWith(multi_path.get(i).file.getPath() + "/") ||
                                mFileControl.get_currently_path().equals(multi_path.get(i).file.getPath()) ){
    					Toast.makeText(this, getString(R.string.err_target_child), Toast.LENGTH_SHORT).show();
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }

	public void showEditorDialog(){
		Log.d(TAG,"showEditorDialog");
		View layout = View.inflate(this, R.layout.editor_layout, null);
		View temp = (View)layout.findViewById(R.id.edit_copy);
        temp.setOnClickListener(EditorClickListener);
        temp = (View)layout.findViewById(R.id.edit_delete);
        temp.setOnClickListener(EditorClickListener);
        temp = (View)layout.findViewById(R.id.edit_move);
        temp.setOnClickListener(EditorClickListener);
        temp = (View)layout.findViewById(R.id.edit_paste);
        temp.setOnClickListener(EditorClickListener);
        temp = (View)layout.findViewById(R.id.edit_rename);
        temp.setOnClickListener(EditorClickListener);
        temp = (View)layout.findViewById(R.id.edit_cancel_btu);
        temp.setOnClickListener(EditorClickListener);
        setEditDialog(layout);
        //editorDialog = new Dialog(FileBrowserActivity.this);
        editorDialog = new Dialog(FileBrowserActivity.this, R.style.dialog_edit);
        editorDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        editorDialog.setContentView(layout);
        editorDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if(KeyEvent.KEYCODE_BACK==arg2.getKeyCode())
				{
					is_cope=false;
					is_move=false;
					is_rename=false;
				}
				return false;
			}
		});
        editorDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				is_cope=false;
				is_move=false;
				is_rename=false;
			}
		});
        editorDialog.show();
	}
	public void setEditDialog(View layout){
		View temp_edit_copy = (View)layout.findViewById(R.id.edit_copy);
		View temp_edit_move = (View)layout.findViewById(R.id.edit_move);
		View temp_edit_delete = (View)layout.findViewById(R.id.edit_delete);
		View temp_edit_rename = (View)layout.findViewById(R.id.edit_rename);
		View temp_edit_paste = (View)layout.findViewById(R.id.edit_paste);
		
		TextView temp_edit_copy_text = (TextView)layout.findViewById(R.id.edit_copy_text);
		TextView temp_edit_move_text = (TextView)layout.findViewById(R.id.edit_move_text);
		TextView temp_edit_delete_text = (TextView)layout.findViewById(R.id.edit_delete_text);
		TextView temp_edit_rename_text = (TextView)layout.findViewById(R.id.edit_rename_text);
		TextView temp_edit_paste_text = (TextView)layout.findViewById(R.id.edit_paste_text);
		
		temp_edit_paste.setEnabled(false);
		setAlphaAndTextColor((TextView)temp_edit_paste_text, false);
		
		if(curr_path == 0 && is_cope==false && is_move==false)
		{		
			temp_edit_copy.setEnabled(false);
			setAlphaAndTextColor(temp_edit_copy_text, false);
			
			temp_edit_move.setEnabled(false);
			setAlphaAndTextColor(temp_edit_move_text, false);
			
			temp_edit_delete.setEnabled(false);
			setAlphaAndTextColor(temp_edit_delete_text, false);
			
			temp_edit_rename.setEnabled(false);
			setAlphaAndTextColor(temp_edit_rename_text, false);
			
		}else
		{						
			if(is_cope){						
				temp_edit_paste.setEnabled(true);
				setAlphaAndTextColor(temp_edit_paste_text, true);
				
				temp_edit_copy.setEnabled(false);
				setAlphaAndTextColor(temp_edit_copy_text, false);
				
				temp_edit_move.setEnabled(false);
				setAlphaAndTextColor(temp_edit_move_text, false);
				
				temp_edit_delete.setEnabled(false);
				setAlphaAndTextColor(temp_edit_delete_text, false);
			}else{									
				temp_edit_paste.setEnabled(false);
				setAlphaAndTextColor(temp_edit_paste_text, false);
				
				temp_edit_copy.setEnabled(true);
				setAlphaAndTextColor(temp_edit_copy_text, true);
				
				if (PermissionTest()){
					temp_edit_move.setEnabled(true);
					setAlphaAndTextColor(temp_edit_move_text, true);
				
					temp_edit_delete.setEnabled(true);
					setAlphaAndTextColor(temp_edit_delete_text, true);
					is_rename = true;
				}else {
					temp_edit_move.setEnabled(false);
					setAlphaAndTextColor(temp_edit_move_text, false);
					
					temp_edit_delete.setEnabled(false);
					setAlphaAndTextColor(temp_edit_delete_text, false);
					is_rename = false;
				}
			}
		}	
		if(is_rename){		
			temp_edit_rename.setEnabled(true);
			setAlphaAndTextColor(temp_edit_rename_text, true);
			is_rename = false;
		}else{				
			temp_edit_rename.setEnabled(false);
			setAlphaAndTextColor(temp_edit_rename_text, false);
		}
	}
	public void setAlphaAndTextColor(TextView temp_Text, boolean visibility){
		if(visibility){
			temp_Text.setTextColor(Color.BLACK);
		}else{
			temp_Text.setTextColor(Color.GRAY);
		}
	}
	
	
	private Handler mDelHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case MSG_BEGIN_DEL:
				if(delDialog == null)
				{
					delDialog = new ProgressDialog(FileBrowserActivity.this);
					delDialog.setTitle(R.string.str_copyingtitle);
					delDialog.setMessage(getString(R.string.str_delcontext));
					delDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
					{
						public void onDismiss(DialogInterface dialog) 
						{
							FileControl.is_enable_del = false;
						}
					});
					
					delDialog.show();
				}
				else
				{
        	        delDialog.show();
	            }	
				mDelHandler.sendEmptyMessageDelayed(MSG_SCAN_DEL, 100);
				break;
				
			case MSG_SCAN_DEL:
				if(FileControl.is_finish_del)
				{
					for(int i = 0; i < FileControl.delFileInfo.size(); i ++)
					{
						mFileControl.folder_array.remove(FileControl.delFileInfo.get(i));
					}
					reMedioScan(mFileControl.get_currently_path());
					
					multi_path = new ArrayList<FileInfo>();
					delDialog.dismiss();
				}
				else
				{
					mDelHandler.sendEmptyMessageDelayed(MSG_SCAN_DEL, 100);
				}
				
				break;
			}
		}
	};
	

	public Dialog DelDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.str_sure_del);
		builder.setMessage(getString(R.string.str_sure_del_ask));
		builder.setPositiveButton(R.string.str_del, new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mFileControl.delFileInfo(multi_path);
    				dissmissDelDialog();
				mDelHandler.sendEmptyMessageDelayed(MSG_BEGIN_DEL, 100);
			}
		});
		builder.setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// do nothing
				multi_path = new ArrayList<FileInfo>(); 	
				dissmissDelDialog();
			}
		});
		return builder.create();
	}

	public void dissmissDelDialog(){
		mDelDialog.dismiss();
	}
    //end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_list);
        showVersion();

        // include file browsing interface all control class
        holder = new FileBrowserViewHolder(this, handler);
        // initialize all file browsing interface control
        holder.findViews();

        topDataBrowser = new TopDataBrowser(this, handler, sourceData);
        adapter = new DataAdapter(this, handler, desDataList);
        holder.listView.setAdapter(adapter);
        holder.listView.setOnItemClickListener(onItemClickListener);
        holder.listView.setOnTouchListener(onTouchListener);
        holder.listView.setOnItemSelectedListener(onItemSelectedListener);
        holder.listView.setOnKeyListener(onKeyListener);
        holder.listView.setOnHoverListener(onHoverListener);

        localDataBrowser = new LocalDataBrowser(this, handler, sourceData);
        dlnaDataBrowser = new DlnaDataBrowser(this, handler, sourceData);
        sambaDataBrowser = new SambaDataBrowser(this, handler, sourceData);

        handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);
        /*if(SystemProperties.getBoolean("mstar.cus.onida", false)){
        	handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);
        	Constants.ExitMM=true;
        }
        else
        	handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);*/

        IntentFilter filter = new IntentFilter();
        filter.addAction(VideoPlayerActivity.ACTION_CHANGE_SOURCE);
        this.registerReceiver(sourceChangeReceiver, filter);

        DatabaseHelper dbHelper = new DatabaseHelper(MediaContainerApplication.getInstance(), Constants.DB_NAME);
        dbHelper.getReadableDatabase();

        if (!Tools.unSupportTVApi()) {
            TvManager.getInstance().getMhlManager().setOnMhlEventListener(new OnMhlEventListener() {
                @Override
                public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onKeyInfo");
                    return false;
                }

                @Override
                public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                    Log.d(TAG, "onAutoSwitch arg0:" + arg0 + " arg1:" + " arg2:" + arg2);
                    TvCommonManager.getInstance().setInputSource(arg1);
                       // ComponentName componentName = new ComponentName("mstar.tvsetting.ui",
                       //      "com.mstar.android.intent.action.START_TV_PLAYER");
                        Intent intent = new Intent("com.mstar.android.intent.action.START_TV_PLAYER");
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        //intent.setComponent(componentName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        intent.putExtra("task_tag", "input_source_changed");
                        FileBrowserActivity.this.startActivity(intent);
                    return false;
                }
            });
        }
        //GridView Adapter
        gridAdapter = new GridAdapter(this, tmpArray,handler);
        holder.gridView.setAdapter(gridAdapter);
        resourceGrid = getResources();
        creatFileIconDrawable();

        //GridView relative code Start---
        switchPage= new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                    createPopWindow();
            }

        };
        holder.btnSwitch.setOnClickListener(switchPage);
        holder.gridView.setOnItemClickListener(onItemClickListener);
        holder.gridView.setOnTouchListener(onTouchListener);
        holder.gridView.setOnItemSelectedListener(onItemSelectedListener);
        holder.gridView.setOnKeyListener(onKeyListener);
        holder.btnSwitch.setOnHoverListener(btnSwitchOnHoverListener);
        holder.viewModeImg.setOnHoverListener(viewModeImgOnHoverListener);
        holder.btnSwitch.setOnFocusChangeListener(btnSwitchOnFocusChangeListener);
        //the first enter time is default logic of mListOrGridFlag,else enter lasted time user's choose
        mPlatform = Tools.getHardwareName();
        if ("muji".equals(mPlatform)) {
            isOrNotGridViewFirst = getSharedPreferences("localmm_sharedPreferences", Context.MODE_PRIVATE).getInt("listOrGridFlag", isOrNotGridViewFirst) ;
        }
        if (isOrNotGridViewFirst==Constants.GRIDVIEW_MODE_FIRST && mListOrGridFlag==Constants.LISTVIEW_MODE) {
            displayGrid();
        }
        //GridView relative code End---
        
        //zb20150613 add
        holder.listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				//showEditorDialog();
				return false;
			}
		});
        mDelDialog = DelDialog();
        resources = this.getResources();
        mCopyFileUtils=new CopyFileUtils();
        multi_path = new ArrayList<FileInfo>();
        curr_path=0;
        mFileControl = new FileControl(this, null, holder.listView);
		mFileControl.last_item = 0;

		mFileControl.mRockExplorer = this;
        source_path = mFileControl.get_currently_path();
		target_path = mFileControl.get_currently_path();
		CopyFileUtils.mFileControl = mFileControl;
		is_cope=false;
		is_move=false;
		is_rename=false;
		//end
    }

     protected void createPopWindow(){
        viewModeList = new ArrayList<ViewMode>();
        Drawable drawable =null;

        drawable = getResources().getDrawable(R.drawable.listview_mode);
        ViewMode viewMode =new ViewMode(drawable, "listview");
        viewModeList.add(viewMode);

        drawable = getResources().getDrawable(R.drawable.gridview_mode);
        viewMode =new ViewMode(drawable, "gridview");
        viewModeList.add(viewMode);

        View myView = getLayoutInflater().inflate(R.layout.viewmode_popwindow, null);
        popupWindow = new PopupWindow(myView, 440, 205, true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popwindow_diaolog_bg));
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(holder.btnSwitch, -5, 0);
        ListView lv = (ListView) myView.findViewById(R.id.lv_pop);
        lv.setAdapter(new ViewModeAdapter(FileBrowserActivity.this, viewModeList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                if (position != FileBrowserActivity.mListOrGridFlag) {
                    viewModeChange = true;
                    displayGrid();

                } else {
                    viewModeChange = false;
                }
                popupWindow.dismiss();
            }
        });

    }
	public static void sendKey(final int KeyCode) {
        new Thread() {     
             public void run() {
                 try {
                     Instrumentation inst = new Instrumentation();
                     inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                     e.printStackTrace();
                 }
              }
   
         }.start();
  }
private OnHoverListener onHoverListener = new OnHoverListener() {

        @Override
        public boolean onHover(View v, MotionEvent event) {
        	if (getCanResponse()) {
                int what = event.getAction();
                switch (what) {
                // The mouse to click the event
                case MotionEvent.ACTION_HOVER_ENTER: {
                     sendKey(KeyEvent.KEYCODE_DPAD_UP);
                    }
                    break;
                }
                }
        	return true ;
        } 
    };
    @Override
    protected void onPause() {
        Log.i(TAG, "---------- onPause ---------");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "---------- onStop ---------");
        int cntDataSource =  getCurrentDataSource();
        if (Constants.BROWSER_LOCAL_DATA == cntDataSource) {
            if (Constants.GRIDVIEW_MODE==FileBrowserActivity.mListOrGridFlag) {
                                new Thread(new Runnable() {
                                   @Override
                                    public void run() {
                                        for (int i=0;i<=Constants.GRID_MODE_DISPLAY_NUM;i++) {
                                            if (gridAdapter.imagePosition2Task[i]!=null
                                                && gridAdapter.isFinish[i]!=Constants.FINISHED) {
                                                gridAdapter.imagePosition2Task[i].cancel(true);
                                                localDataBrowser.hasCancelVideoTaskStateInLocalData=Constants.GRID_TASK_CANCELED;
                                            }
                                        }
                                    }
                                }).start();
           }

        }
        else if (Constants.BROWSER_SAMBA_DATA == cntDataSource) {
            if (Constants.GRIDVIEW_MODE== FileBrowserActivity.mListOrGridFlag) {
                                new Thread(new Runnable() {
                                   @Override
                                    public void run() {
                                        for (int i=0;i<=Constants.GRID_MODE_DISPLAY_NUM;i++) {
                                            if (gridAdapter.imagePosition2Task[i]!=null
                                                &&gridAdapter.isFinish[i]!=Constants.FINISHED) {
                                                gridAdapter.imagePosition2Task[i].cancel(true);
                                                sambaDataBrowser.hasCancelVideoTaskStateInSamba=Constants.GRID_TASK_CANCELED;
                                            }
                                        }
                                    }
                                }).start();
            }

        }
        super.onStop();
    }
    private void creatFileIconDrawable(){
        Drawable drawable =null;
        drawable = resourceGrid.getDrawable(R.drawable.icon_file_file2);
        fileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_pic2);
        fileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_song2);
        fileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_video2);
        fileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_folder2);
        fileTypeDrawable.add(drawable);

        drawable = resourceGrid.getDrawable(R.drawable.icon_file_return2);
        fileTypeDrawable.add(drawable);
    }

    private void displayGrid(){
        // change textview on the button

        if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
            holder.viewModeImg.setBackgroundResource(R.drawable.gridview_mode);
        } else {
            holder.viewModeImg.setBackgroundResource(R.drawable.listview_mode);
        }
        int cntDataSource =  getCurrentDataSource();
        int localState = localDataBrowser.getLocalDataBrowserState();
        int sambaState = sambaDataBrowser.getBrowserSambaDataState();
        if (Constants.BROWSER_TOP_DATA == cntDataSource) {
            if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                mListOrGridFlag=Constants.GRIDVIEW_MODE;
                handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);
                changeFocusWhenL2G();
            } else {
                mListOrGridFlag=Constants.LISTVIEW_MODE;
                handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);
                changeFocusWhenG2L();
            }

        }
        else if (Constants.BROWSER_LOCAL_DATA == cntDataSource && LocalDataManager.STATUS_DEVICE_DISPLAY == localState) {
             if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                 mListOrGridFlag=Constants.GRIDVIEW_MODE;
                 handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);
                 changeFocusWhenL2G();
             } else {
                 mListOrGridFlag=Constants.LISTVIEW_MODE;
                 handler.sendEmptyMessage(Constants.BROWSER_LOCAL_DATA);
                 changeFocusWhenG2L();
             }
        }
        else if (Constants.BROWSER_LOCAL_DATA == cntDataSource && LocalDataManager.STATUS_RESOURCE_DISPLAY == localState) {
             if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                 mListOrGridFlag=Constants.GRIDVIEW_MODE;
                 changeFocusWhenL2G();
                 localDataBrowser.switchViewMode();
             } else {
                 mListOrGridFlag=Constants.LISTVIEW_MODE;
                 localDataBrowser.switchViewMode();
                 changeFocusWhenG2L();
             }
        }
        else if (Constants.BROWSER_SAMBA_DATA == cntDataSource && SambaDataManager.SCAN_HOST == sambaState) {
             if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                 mListOrGridFlag=Constants.GRIDVIEW_MODE;
                 handler.sendEmptyMessage(Constants.BROWSER_SAMBA_DATA);
                 //changeFocusWhenL2G();
             } else {
                 mListOrGridFlag=Constants.LISTVIEW_MODE;
                 handler.sendEmptyMessage(Constants.BROWSER_SAMBA_DATA);
                 //changeFocusWhenG2L();
             }

        }
        else if (Constants.BROWSER_SAMBA_DATA == cntDataSource && SambaDataManager.SCAN_FILE== sambaState) {
             if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                 mListOrGridFlag=Constants.GRIDVIEW_MODE;
                 changeFocusWhenL2G();
                 sambaDataBrowser.switchViewMode();
             } else {
                 mListOrGridFlag=Constants.LISTVIEW_MODE;
                 sambaDataBrowser.switchViewMode();
                 changeFocusWhenG2L();
             }

        }

    }
    private void changeFocusWhenL2G(){
        holder.gridView.setVisibility(View.VISIBLE);
        holder.listView.setVisibility(View.GONE);
        holder.setBtnSwitchFocus(false);
        holder.setGridViewFocus(true,0);
        holder.setListViewFocus(false,0);
    }
    private void changeFocusWhenG2L(){
        holder.gridView.setVisibility(View.GONE);
        holder.listView.setVisibility(View.VISIBLE);
        holder.setBtnSwitchFocus(false);
        holder.setGridViewFocus(false,0);
        holder.setListViewFocus(true,0);
    }
    @Override
    protected void onResume() {

        Log.d(TAG,"onResume");
        if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
            holder.viewModeImg.setBackgroundResource(R.drawable.listview_mode);
        } else {
            holder.viewModeImg.setBackgroundResource(R.drawable.gridview_mode);
        }
        if (Tools.isFloatVideoPlayModeOn()) {
            FloatVideoController.getInstance().hideFloatVideoWindow();
        }
        findViewById(R.id.rootLinearLayout).setVisibility(View.VISIBLE);
        setCanResponse(true);
        String hwName = Tools.getHardwareName();
        if (hwName.equals("madison") || hwName.equals("monet") || hwName.equals("manhattan")
			|| hwName.equals("messi")) {
             Constants.bSupportDualDecode = false;
        }
        // switching source to storage
        Tools.changeSource(true);

        // registration network monitoring radio receiver
        IntentFilter networkFilter = new IntentFilter(
                "com.mstar.localmm.network.disconnect");
        registerReceiver(networkReceiver, networkFilter);

        // registered disk pull plug radio receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        registerReceiver(diskChangeReceiver, filter);
        registerReceiver(homeKeyEventBroadCastReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        IntentFilter finishFileBrowserActivityFilter = new IntentFilter("action_finish_filebrowseractivity");
        registerReceiver(finishFileBrowserActivityReceiver, finishFileBrowserActivityFilter);
        super.onResume();
    }
    @Override
    protected void onRestart() {
        Log.i(TAG, "----------onRestart------------");
        new Thread(new Runnable() {
            @Override
            public void run() {
               if (Constants.GRID_TASK_CANCELED == localDataBrowser.hasCancelVideoTaskStateInLocalData ||
                   Constants.GRID_TASK_CANCELED == sambaDataBrowser.hasCancelVideoTaskStateInSamba) {
                   localDataBrowser.hasCancelVideoTaskStateInLocalData=Constants.GRID_TASK_NOT_CANCELED;
                   sambaDataBrowser.hasCancelVideoTaskStateInSamba    =Constants.GRID_TASK_NOT_CANCELED;
                   for(int i=0;i<=Constants.GRID_MODE_DISPLAY_NUM;i++){
                        if (gridAdapter.imagePosition2Task[i]!=null && gridAdapter.isFinish[i]!=Constants.FINISHED) {
                            // GridAdapter gridAdapter = new GridAdapter();
                            // use this gridview's gridAdapter object without new one
                            int tmpFileType = gridAdapter.mTypeOfPosition[i];
                            GridAdapter.AsyncLoadImageTask task = gridAdapter.new AsyncLoadImageTask(gridAdapter.ivHasCancelTask[i],tmpFileType);
                            gridAdapter.imagePosition2Task[i]=task;
                            task.execute(i);
                         }
                   }
               }
           }
        }).start();
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        for(int i=0;i<=Constants.GRID_MODE_DISPLAY_NUM;i++){
            if (gridAdapter.imagePosition2Task[i]!=null) {
                gridAdapter.imagePosition2Task[i].cancel(true);
                gridAdapter.imagePosition2Task[i]=null;
            }
        }
        sambaDataBrowser.unmount();
        super.onDestroy();
        if ("muji".equals(mPlatform)) {
            getSharedPreferences("localmm_sharedPreferences", Context.MODE_PRIVATE)
            .edit().putInt("listOrGridFlag", mListOrGridFlag).commit();
        }
        // When LMM Destroyed, Launcher can auto-change source,
        // So LMM do not need changeSource again.
        // Tools.changeSource(false);

        // release resources
        release();

        // reverse registering listeners
        unregisterReceiver(diskChangeReceiver);
        unregisterReceiver(networkReceiver);
        unregisterReceiver(sourceChangeReceiver);
        unregisterReceiver(homeKeyEventBroadCastReceiver);
        unregisterReceiver(finishFileBrowserActivityReceiver);
        System.gc();

        finish();

        if (Tools.isFloatVideoPlayModeOn()) {
            FloatVideoController.getInstance().showFloatVideoWindow();
            FloatVideoController.getInstance().getVideoListItem(Constants.DB_NAME, Constants.VIDEO_PLAY_LIST_TABLE_NAME);
        }

        // kill process and recycle resource
        // Process.killProcess(Process.myPid());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp keyCode:" + keyCode + " getCurrentDataSource():" + getCurrentDataSource());
      //lg add
       /* if (keyCode == KeyEvent.KEYCODE_MENU && localDataBrowser.focusPosition != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.exit_title);
            builder.setMessage(R.string.menu_exit_confirm);
            builder.setPositiveButton(this.getString(R.string.exit_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                        	String path=sourceData.get(localDataBrowser.focusPosition).getPath();
                            File file = new File(path);
                            file.setExecutable(true,false);
                            file.setReadable(true,false);
                            file.setWritable(true,false);
                            recurDelete(file);
                            sourceData.remove(localDataBrowser.focusPosition);
                            // update UI
                            desDataList.clear();
                            desDataList.addAll(sourceData);
                            adapter.notifyDataSetChanged();
                            gridAdapter.notifyDataSetChanged();
                            //MediaContainerApplication.allFileList.clear();
                            localDataBrowser.browser(0, dataType);
                            handler.sendEmptyMessage(Constants.BROWSER_TOP_DATA);
                           	handler.sendEmptyMessage(Constants.UPDATE_LOCAL_DATA);
                            handler.sendEmptyMessage(Constants.UPDATE_TOP_DATA);
                            handler.sendEmptyMessage(Constants.UPDATE_MEDIA_TYPE);
                            mDialog.dismiss();
                        }

						
                    });
            builder.setNeutralButton(this.getString(R.string.exit_cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface mDialog, int which) {
                            mDialog.dismiss();
                        }
                    });
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int arg1,
                        KeyEvent arg2) {
                    if (KeyEvent.KEYCODE_MEDIA_PLAY == arg1
                            || KeyEvent.KEYCODE_MEDIA_PAUSE == arg1
                            || KeyEvent.KEYCODE_MEDIA_NEXT == arg1
                            || KeyEvent.KEYCODE_MEDIA_PREVIOUS == arg1) {
                        return true;
                    }
                    return false;
                }
            });
            builder.show();
            }*/
        //end
        // Operation time consuming operation, will not escape key shielding
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getCurrentDataSource() != Constants.BROWSER_TOP_DATA) {
                if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA && (sambaDataBrowser !=  null) && sambaDataBrowser.isUpdating()) {
                    Log.i(TAG, "to stop Samba Updating....");
                    sambaDataBrowser.stopBrowser();
                    setCanResponse(true);
                    Message msg = handler.obtainMessage();
                    msg.what = Constants.UPDATE_TOP_DATA;
                    msg.arg1 = 1;
                    msg.arg2 = 1;
                    handler.sendMessage(msg);
                    setCurrentDataSource(Constants.BROWSER_TOP_DATA);
                    return false;
                }
                //skye add for onida
                if (Constants.ExitMM) {
					this.finish();
					return true;
				}
                if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                    onItemClickListener.onItemClick(holder.listView, holder.listView, 0, 0);
                } else if (Constants.GRIDVIEW_MODE==mListOrGridFlag) {
                           onItemClickListener.onItemClick(holder.gridView, holder.gridView, 0, 0);
                }
                	return true;
            }

            // FileBrowserActivity.this.finish();
            return super.onKeyUp(keyCode, event);
        }

        // Key shielding
        if (!getCanResponse()) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            return processLeftKeyDown();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            return processRightKeyDown();
        }
        if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    
    public static void recurDelete(File f){
    	if(!f.exists())return;
    	if(f.isFile()){
    		f.delete();
    		return;
    	}
    	File[] files=f.listFiles();
    	for(int i= 0;i < files.length; i++){
    		recurDelete(files[i]);
    	}
    	f.delete();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	Log.i(TAG, "keyCode:"+keyCode);
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            return processUpKeyDown();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            return processDownKeyDown();
        }
        if ((KeyEvent.KEYCODE_TV_INPUT == keyCode) && Tools.unSupportTVApi()) {
            return true;
        }
		//zb20150613 add
	    if(keyCode==KeyEvent.KEYCODE_MENU)
	    {
	    	if (Constants.ExitMM) {
	    		return true;
			}
	    	String displayText = holder.displayTip.getText().toString();
	    	if(isShowEditorDialog(displayText)){
	    		showEditorDialog();
	    	}
			return true;
	    }
		//end
        return super.onKeyDown(keyCode, event);
    }
    
    public boolean isShowEditorDialog(String tip){
    	boolean flag = true;
    	if(tip.equals(getResources().getString(R.string.all))
    			||tip.equals(getResources().getString(R.string.picture))
    			||tip.equals(getResources().getString(R.string.song))
    			||tip.equals(getResources().getString(R.string.video))
    			||tip.equals("/Internal Storage")
    			||tip.equals("/USB1")
    			||tip.equals("/USB2")
    			||tip.equals("/USB3")
    			||tip.equals(getResources().getString(R.string.all))){
    		flag = false;
    	}
    	return flag;
    }

    /**
     * send virtual buttons.
     */
    protected void sendKeyEvent(int keycode) {
        
         /** long now = SystemClock.uptimeMillis(); try { KeyEvent downEvent = new
         * KeyEvent(now, now, KeyEvent.ACTION_DOWN, keycode, 0); KeyEvent
         * upEvent = new KeyEvent(now, now, KeyEvent.ACTION_UP, keycode, 0);
         * (IWindowManager
         * .Stub.asInterface(ServiceManager.getService("window")))
         * .injectInputEventNoWait(downEvent);
         * (IWindowManager.Stub.asInterface(ServiceManager
         * .getService("window"))) .injectInputEventNoWait(upEvent); } catch
         * (RemoteException e) { e.printStackTrace(); }
         */
    }

    public void setCanResponse(boolean isCan) {
        Log.i(TAG, "setCanResponse:"+isCan);
        /*
        Exception e = new Exception("this is a log");
        e.printStackTrace();
        */
        m_canResponse = isCan;
    }

    public boolean getCanResponse() {
        return m_canResponse;
    }

    public void setCurrentDataSource(int datasource_) {
        Log.i(TAG, "set current dataSource:"+datasource_);
        m_dataSource = datasource_;
        /*
        Exception e = new Exception("this is a log");
        e.printStackTrace();
        */
    }

    public int getCurrentDataSource() {
        return m_dataSource;
    }

    /************************************************************************
     * ListView event listeners regard highly write area
     ************************************************************************/
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            Log.d(TAG, "list view onItemClick ... dataSource : " + getCurrentDataSource() + " position:" + position);
            // Key shielding
            if (!getCanResponse()) {
                return;
            }
            if(Constants.ExitMM)
            {
            	Constants.UsbName=desDataList.get(position).getName();
            }
            if(Constants.ExitMM&&position==0)
            {	Log.d(TAG, "skk kill it");
            	FileBrowserActivity.this.finish();
            	return;
            }
            // Distribution key event
            dispatchKeyEvent(KeyEvent.KEYCODE_ENTER, position);

        }
    };

    private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                int position, long id) {
            Log.d(TAG, "onItemSelected, position : " + position);
            if(Constants.ExitMM)
            {
            	Constants.UsbName=desDataList.get(position).getName();
            }
            //zb20150613 add
            currFocusPosition=position;
            //end
            showHits(position);
            if (0==positionFocusNow && 0==position)
                positionFocusNow=position;
            else if (0==positionFocusNow && Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM==position)
                positionFocusNow=position;
            else if (Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM==positionFocusNow && 0==position)
                positionFocusNow=position;
            else
                positionFocusNow=position+1;
            localDataBrowser.focusPosition = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };

    private OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int y = (int) event.getY();
            if (getCanResponse()) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    motionY = y;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    // File list page up // 50 difference says at least 50
                    // sliding distance just turn the page, is probably the
                    // height of the item
                    if (y > motionY && y - 50 > motionY) {
                        if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                            localDataBrowser.refresh(Constants.TOUCH_PAGE_UP);
                        } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                            dlnaDataBrowser.refresh(Constants.TOUCH_PAGE_UP);
                        } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                            sambaDataBrowser.refresh(Constants.TOUCH_PAGE_UP);
                        }

                        // File list down turn the page
                    } else if (y < motionY && y + 50 < motionY) {
                        if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                            localDataBrowser.refresh(Constants.TOUCH_PAGE_DOWN);
                        } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                            dlnaDataBrowser.refresh(Constants.TOUCH_PAGE_DOWN);
                        } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                            sambaDataBrowser.refresh(Constants.TOUCH_PAGE_DOWN);
                        }
                    }
                    break;
                }
                }

                return false;

            } else {
                return true;
            }
        }

    };

    private OnKeyListener onKeyListener = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // operation time consuming operation, will not escape key shielding
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return false;
            }

            // if the current key in shielding state, is directly to return to
            if (!getCanResponse()) {
                return true;
            }

            // below is turn the page processing
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                // shortcuts turn the page, the page up
                if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {
                    if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                        localDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                    } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                        dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                    } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                        sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                    }

                    return true;

                    // shortcuts turn the page down, turn the page
                } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
                    if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                        localDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                    } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                        dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                    } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                        sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                    }

                    return true;
                }
				//huzhou add
				else if(keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE){
					dispatchKeyEvent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, localDataBrowser.focusPosition);
					return true;
					}
				//end

            }

            return false;
        }
    };
    private OnHoverListener btnSwitchOnHoverListener = new OnHoverListener(){

        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            if (toast==null)
                toast =Toast.makeText(FileBrowserActivity.this, "More browser mode...", Toast.LENGTH_SHORT);
            else
                toast.setText("More browser mode...");
            toast.setGravity(Gravity.TOP, 0, 8);
            switch (what) {
            case MotionEvent.ACTION_HOVER_ENTER:
                 toast.show();
                 break;
            case MotionEvent.ACTION_HOVER_MOVE:
                 break;
            case MotionEvent.ACTION_HOVER_EXIT:
                 break;
            }
            return false;
        }
    };
    private OnHoverListener viewModeImgOnHoverListener = new OnHoverListener(){
        @Override
        public boolean onHover(View v, MotionEvent event) {
            int what = event.getAction();
            String tmpText ="";
            if (Constants.LISTVIEW_MODE==mListOrGridFlag)
                tmpText = "You are at listview browser mode";
            else if (Constants.GRIDVIEW_MODE==mListOrGridFlag)
                tmpText = "You are at gridview browser mode";
            if (toast==null)
                toast = Toast.makeText(FileBrowserActivity.this, tmpText, Toast.LENGTH_SHORT);
            else
                toast.setText(tmpText);
                toast.setGravity(Gravity.TOP, 0, 8);
            switch (what) {
            case MotionEvent.ACTION_HOVER_ENTER:
                 toast.show();
                 break;
            case MotionEvent.ACTION_HOVER_MOVE:
                 break;
            case MotionEvent.ACTION_HOVER_EXIT:
                 break;
            }
            return false;
        }

    };
    private OnFocusChangeListener btnSwitchOnFocusChangeListener =new OnFocusChangeListener(){
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (toast==null)
                    toast =Toast.makeText(FileBrowserActivity.this, "More browser mode...", Toast.LENGTH_SHORT);
                else
                    toast.setText("More browser mode...");
                toast.setGravity(Gravity.TOP, 0, 8);
                toast.show();
            }
       }

    };

    /************************************************************************
     * Handler event handling area
     ************************************************************************/
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // home page surface
            if (msg.what == Constants.BROWSER_TOP_DATA) {
            	//FileBrowserActivity.this.finish();
                lastDataSource = getCurrentDataSource();
                if (lastDataSource == Constants.BROWSER_DLNA_DATA) {
                    // reset type switch for available state
                    holder.openTypeChange();
                }

                // data
                setCurrentDataSource(Constants.BROWSER_TOP_DATA);
                // loading progress tip
                holder.showScanStatus(true);
                showScanMessage(R.string.loading_top);

                // refresh homepage data
                topDataBrowser.refresh();

                // browsing local usb disk data: level directory
            } else if (msg.what == Constants.BROWSER_LOCAL_DATA) {
                setCurrentDataSource(Constants.BROWSER_LOCAL_DATA);
                // loading progress tip.
                showScanMessage(R.string.loading_usb_device);

                // browsing USB devices.
                localDataBrowser.browser(-1, dataType);

                // browsing samba data: level directory
            } else if (msg.what == Constants.BROWSER_SAMBA_DATA) {
                lastDataSource = getCurrentDataSource();
                setCurrentDataSource(Constants.BROWSER_SAMBA_DATA);
                showScanMessage(R.string.loading_samba_device);

                // loading samba need longer time, need to screen buttons
                setCanResponse(false);
                // browsing samba equipment
                sambaDataBrowser.browser(-1, dataType);

                // browsing dlna data: level directory
            } else if (msg.what == Constants.BROWSER_DLNA_DATA) {
                lastDataSource = getCurrentDataSource();
                setCurrentDataSource(Constants.BROWSER_DLNA_DATA);
                showScanMessage(R.string.loading_dlna_device);

                // browsing dlna equipment
                dlnaDataBrowser.browser(-1, dataType);

                // refresh homepage data
            } else if (msg.what == Constants.UPDATE_TOP_DATA) {
                // Hide loading progress tooltip
                dismissScanMessage();
                int index=-100;
                String text = "";
                if (lastDataSource == Constants.BROWSER_TOP_DATA || lastDataSource == Constants.BROWSER_LOCAL_DATA) {
                    index=0;
                } else if (lastDataSource == Constants.BROWSER_SAMBA_DATA){
                    index=1;
                } else {
                    index=2;
                }
                if (Constants.GRIDVIEW_MODE==mListOrGridFlag) {
                    tmpArray.clear();
                    tmpArray.addAll(sourceData);
                    gridAdapter.notifyDataSetChanged();
                    positionFocusNow=0;
                } else if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                    desDataList.clear();
                    desDataList.addAll(sourceData);
                    adapter.notifyDataSetChanged();
                }
                if (Constants.GRIDVIEW_MODE==mListOrGridFlag) {
                    if (lastDataSource == Constants.BROWSER_TOP_DATA
                        || lastDataSource == Constants.BROWSER_LOCAL_DATA) {
                        holder.gridView.setSelection(0);
                        text = getResources().getStringArray(R.array.data_source)[0];
                    } else if (lastDataSource == Constants.BROWSER_SAMBA_DATA) {
                        holder.gridView.setSelection(1);
                        text = getResources().getStringArray(R.array.data_source)[1];
                    } else if (lastDataSource == Constants.BROWSER_DLNA_DATA) {
                        holder.gridView.setSelection(2);
                        text = getResources().getStringArray(R.array.data_source)[2];
                    }
                } else if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
                     if (lastDataSource == Constants.BROWSER_TOP_DATA
                         || lastDataSource == Constants.BROWSER_LOCAL_DATA) {
                         holder.listView.setSelection(0);
                         text = getResources().getStringArray(R.array.data_source)[0];
                     } else if (lastDataSource == Constants.BROWSER_SAMBA_DATA) {
                         holder.listView.setSelection(1);
                         text = getResources().getStringArray(R.array.data_source)[1];
                     } else if (lastDataSource == Constants.BROWSER_DLNA_DATA) {
                         holder.listView.setSelection(2);
                         text = getResources().getStringArray(R.array.data_source)[2];
                     }
                }
                // ui refresh
                // Refresh hint
                holder.setDisplayTip(text);

                // Update page
                holder.setCurrentPageNum(msg.arg1);
                holder.setTotalPageNum(msg.arg2);

                // refresh local data
            } else if (msg.what == Constants.UPDATE_LOCAL_DATA) {
                // hide loading progress tooltip
                dismissScanMessage();
                Bundle bundle = msg.getData();
                int index = bundle.getInt(Constants.BUNDLE_INDEX);
                // refresh UI.
                if (mListOrGridFlag==Constants.GRIDVIEW_MODE) {
                    tmpArray.clear();
                    tmpArray.addAll(sourceData);
                    gridAdapter.notifyDataSetChanged();
                    positionFocusNow=0;
                } else {
                    desDataList.clear();
                    desDataList.addAll(sourceData);
                    adapter.notifyDataSetChanged();
                }
                if(mListOrGridFlag==Constants.GRIDVIEW_MODE)
                    holder.gridView.setSelection(index);
                else
                    holder.listView.setSelection(index);
                // refresh the page number.
                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
                // display the tooltip
                showHits(index);
                // refresh dlna data
            } else if (msg.what == Constants.UPDATE_DLNA_DATA) {
                // hide loading progress tooltip
                dismissScanMessage();

                // dlna module doesn't allow for types of switching
                holder.setLeftFocus(Constants.OPTION_STATE_ALL, false);
                holder.closeTypeChange();
                tmpType = Constants.OPTION_STATE_ALL;

                // update UI
                desDataList.clear();
                desDataList.addAll(sourceData);
                adapter.notifyDataSetChanged();

                Bundle bundle = msg.getData();
                int index = bundle.getInt(Constants.BUNDLE_INDEX);
                holder.listView.setSelection(index);
                // update the page number
                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));

                // display the tooltip
                showHits(index);

                // refresh samba data
            } else if (msg.what == Constants.UPDATE_ALL_SAMBA_DATA) {
                // let go to key shielding
                setCanResponse(true);
                Bundle bundle = msg.getData();
                int index = bundle.getInt(Constants.BUNDLE_INDEX);
                // refresh UI.
                if (mListOrGridFlag==Constants.GRIDVIEW_MODE) {
                    tmpArray.clear();
                    tmpArray.addAll(sourceData);
                    gridAdapter.notifyDataSetChanged();
                    positionFocusNow=0;
                } else {
                    desDataList.clear();
                    desDataList.addAll(sourceData);
                    adapter.notifyDataSetChanged();
                }
                if(mListOrGridFlag==Constants.GRIDVIEW_MODE)
                    holder.gridView.setSelection(index);
                else
                    holder.listView.setSelection(index);
                // refresh the page number.
                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
                // display the tooltip
                showHits(index);
                dismissScanMessage();
                // view picture data
            }  else if (msg.what == Constants.SAMBA_SCAN_COMPLETED) {
                // hide loading progress tooltip
                if (!m_canResponse && (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA)) {
                    setCanResponse(true);
                    Message msg1 = handler.obtainMessage();
                    msg1.what = Constants.UPDATE_TOP_DATA;
                    msg1.arg1 = 1;
                    msg1.arg2 = 1;
                    handler.sendMessage(msg1);
                    setCurrentDataSource(Constants.BROWSER_TOP_DATA);
                    showToast(R.string.network_connect_failed);
                }
                dismissScanMessage();
            } else if (msg.what == Constants.UPDATE_SAMBA_DATA) {
                // let go to key shielding
                setCanResponse(true);
                Bundle bundle = msg.getData();
                int index = bundle.getInt(Constants.BUNDLE_INDEX);
                // refresh UI.
                if (mListOrGridFlag==Constants.GRIDVIEW_MODE) {
                    tmpArray.clear();
                    tmpArray.addAll(sourceData);
                    gridAdapter.notifyDataSetChanged();
                    positionFocusNow=0;
                    changeFocusWhenL2G();
                } else {
                    desDataList.clear();
                    desDataList.addAll(sourceData);
                    adapter.notifyDataSetChanged();
                    changeFocusWhenG2L();
                }
                if(mListOrGridFlag==Constants.GRIDVIEW_MODE)
                    holder.gridView.setSelection(index);
                else
                    holder.listView.setSelection(index);
                // refresh the page number.
                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));
                // display the tooltip
                showHits(index);
                // view picture data
            } else if (msg.what == Constants.UPDATE_MEDIA_TYPE) {
                switchMediaType(dataType);

            } else if (msg.what == Constants.UPDATE_EXCEPTION_INFO) {
                processExceptions(msg.arg1);
                // Loading tooltip invisible
                holder.showScanStatus(false);
                holder.updateScanStatusText(false, 0);

            } else if (msg.what == Constants.UPDATE_PROGRESS_INFO) {
                if (msg.arg2 == Constants.PROGRESS_TEXT_SHOW) {
                    showScanMessage(msg.arg1);
                } else if (msg.arg2 == Constants.PROGRESS_TEXT_HIDE) {
                    dismissScanMessage();
                }

                // not be used when the network
            } else if (msg.what == Constants.NETWORK_UNCONNECTED) {
                // let go to key shielding
                setCanResponse(true);

                // release resources
                release();

                // if the current are to browse the network data and network,
                // not with is directly turn to Top page
                if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                    holder.openTypeChange();
                }

                lastDataSource = getCurrentDataSource();
                // data
                setCurrentDataSource(Constants.BROWSER_TOP_DATA);
                // loading progress tip
                holder.showScanStatus(true);

                // refresh homepage data
                topDataBrowser.refresh();

                // mouse page up operation
            } else if (msg.what == Constants.MOUSE_PAGE_UP) {
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    localDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                    dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                    sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_UP);
                }

                // The mouse down turn the page operation
            } else if (msg.what == Constants.MOUSE_PAGE_DOWN) {
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    localDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                    dlnaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                    sambaDataBrowser.refresh(Constants.KEYCODE_PAGE_DOWN);
                }

                // The mouse focus with use
            } else if (msg.what == Constants.UPDATE_LISTVIEW_FOCUS) {
                if (getCanResponse()) {
                    int position = msg.getData().getInt(
                            Constants.ADAPTER_POSITION);
                    holder.setListViewFocus(true, position);
                    showHits(position);
                }
            }else if(msg.what == Constants.START_MEDIA_PLAYER){
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    localDataBrowser.startPlayer();
                } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                    dlnaDataBrowser.startPlayer();
                } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                    sambaDataBrowser.startPlayer();
                }

            }
            //zb20150613 add
            else if(msg.what == Constants.REFRESH_DATA)
            {
            	localDataBrowser.browser(-2, dataType);
                desDataList.clear();
                desDataList.addAll(sourceData);
                adapter.notifyDataSetChanged();

                Bundle bundle = msg.getData();
                int index = bundle.getInt(Constants.BUNDLE_INDEX);
                holder.listView.setSelection(index);
                holder.setCurrentPageNum(bundle.getInt(Constants.BUNDLE_PAGE));
                holder.setTotalPageNum(bundle.getInt(Constants.BUNDLE_TPAGE));

                showHits(index);
            }
            //end


        };
    };

    /************************************************************************
     * Broadcast radio event handling area
     ************************************************************************/

    private BroadcastReceiver diskChangeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // disk loading
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                // current for Local data browsing disk device list page
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    // update device list
                    localDataBrowser.updateUSBDevice(null);
                }

                // disk unloading
            } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
                if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                    Uri uri = intent.getData();
                    String devicePath = uri.getPath();
                    localDataBrowser.updateUSBDevice(devicePath);
                }
            }
        }
    };

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "network disconnect");
            // network state changes through the radio to handle processing, if
            // the current in to browse the network data is returned to the Top
            // page
            if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA
                    || getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                handler.sendEmptyMessage(Constants.NETWORK_UNCONNECTED);
                if (sambaDataBrowser != null) {
                    sambaDataBrowser.closeDialogIfneeded();
                }
            }
        }

    };

    BroadcastReceiver sourceChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"*******BroadcastReceiver**********" + intent.getAction());
            findViewById(R.id.rootLinearLayout).setVisibility(View.INVISIBLE);
        }
    };

    private BroadcastReceiver homeKeyEventBroadCastReceiver = new BroadcastReceiver() {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";//home key
        static final String SYSTEM_RECENT_APPS = "recentapps";//long home key
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        Log.i(TAG, "SYSTEM_HOME_KEY");
                        // Before, call this.finish() is for 4k2k to release resource faster when exit lmm.
                        // But Currently Comment out this, because users want lmm remember the current list menu position after exit lmm.
                        // FileBrowserActivity.this.finish();
                        // android.os.Process.killProcess(Process.myPid());
                    } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                        // long home key
                    }
                }
            }
        }
    };

    private BroadcastReceiver finishFileBrowserActivityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "finishFileBrowserActivityReceiver onReceive");
            String action = intent.getAction();

            if (action.equals("action_finish_filebrowseractivity")) {
                Log.i(TAG, "action_finish_filebrowseractivity");
                FileBrowserActivity.this.finish();
            }
        }

    };

    /************************************************************************
     * Key event handling area
     ************************************************************************/
    private boolean processLeftKeyDown() {
        // do nothing when focus on btnSwitch;
        if (holder.btnSwitch.isFocusable()) {
            return true;
        }
        // ListView current gains focus
        if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
            if (holder.listView.isFocusable()) {
                tmpType = dataType;
                holder.setLeftFocus(dataType, true);
                holder.setListViewFocus(false, 0);
                holder.clearFocusFromKey() ;
                return true;
            }
        } else if (Constants.GRIDVIEW_MODE==mListOrGridFlag) {
            if (holder.gridView.isFocusable()) {
                if (0==positionFocusNow||
                    0==(positionFocusNow%Constants.GRID_MODE_ONE_ROW_DISPLAY_NUM)) {
                    tmpType = dataType;
                    holder.setLeftFocus(dataType, true);
                    holder.setGridViewFocus(false, 0);
                }
            }else{
                // do nothing
            }
            // because it add 1 in the onItemSelected
            positionFocusNow--;
        }
        return true;


    }

    private boolean processRightKeyDown() {
        // do nothing when focus on btnSwitch
        if (holder.btnSwitch.isFocusable()) {
            return true;
        }
        if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
            // ListView current gains focus
            if (holder.listView.isFocusable()) {
            } else {
                holder.setLeftFocus(dataType, false);
                if (dataType != tmpType) {
                    holder.changeLeftFocus(tmpType, 0);
                }
                holder.setListViewFocus(true, 0);
                showHits(0);
            }
            return true;
        } else if (Constants.GRIDVIEW_MODE==mListOrGridFlag) {
            if (holder.gridView.isFocusable()) {
            } else {
                holder.setLeftFocus(dataType, false);
                if (dataType != tmpType) {
                    holder.changeLeftFocus(tmpType, 0);
                }
                holder.setGridViewFocus(true, 0);
                positionFocusNow = 0;
                showHits(0);
            }
            return true;
        }
        return true;
    }

    private boolean processUpKeyDown() {
        // do nothing
        if (holder.btnSwitch.isFocusable()) {
            return true;
        }
        String tmp =(String)holder.currentPageNumText.getText();
        // from GridView to btnSwitch
        if ( holder.gridView.isFocusable()&&Integer.parseInt(tmp)==1) {
            holder.setGridViewFocus(false,0);
            holder.setBtnSwitchFocus(true);
            return true;
        }
        // from  listView to btnSwitch
        if (holder.btnSwitchIsOnOrOff&&!holder.btnSwitch.isFocusable()
            &&holder.listView.isFocusable()
            &&holder.listView.getSelectedItemPosition()==0&&Integer.parseInt(tmp)==1) {
            holder.setListViewFocus(false,0);
            holder.setBtnSwitchFocus(true);
            return true;
        }
        if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
            if (holder.listView.isFocusable()) {
                // Turn the page
                dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_UP,holder.listView.getSelectedItemPosition());
            } else {
                int type = 1;
                if (tmpType == Constants.OPTION_STATE_ALL) {
                    type = Constants.OPTION_STATE_VIDEO;
                } else {
                    type = tmpType - 1;
                }
                Log.d(TAG, "old type : " + tmpType + " new type : " + type);
                holder.changeLeftFocus(tmpType, type);
                // Save the current focus data type
                tmpType = type;
            }
            return true;
        } else if (Constants.GRIDVIEW_MODE==mListOrGridFlag) {
            if (holder.gridView.isFocusable()) {
                // Turn the page
                dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_UP,holder.gridView.getSelectedItemPosition());
            } else {
                // According to the up button, switch and the focus of the two icon
                int type = 1;
                if (tmpType == Constants.OPTION_STATE_ALL) {
                    type = Constants.OPTION_STATE_VIDEO;
                } else {
                    type = tmpType - 1;
                }
                Log.d(TAG, "old type : " + tmpType + " new type : " + type);
                holder.changeLeftFocus(tmpType, type);
                // Save the current focus data type
                tmpType = type;
            }
            return true;


        }
        return true;
    }

    private boolean processDownKeyDown() {
       // from btnSwitch to Gridview
        if (holder.btnSwitch.isFocusable()&&Constants.GRIDVIEW_MODE==mListOrGridFlag) {
            holder.setGridViewFocus(true,0);
            holder.setBtnSwitchFocus(false);
            positionFocusNow = 0;
            return true;
        }
        // from btnSwitch to listView
        if (holder.btnSwitch.isFocusable()&&Constants.LISTVIEW_MODE==mListOrGridFlag) {
            holder.setListViewFocus(true,0);
            holder.setBtnSwitchFocus(false);
            return true;
        }
        if (Constants.LISTVIEW_MODE==mListOrGridFlag) {
            // ListView current gains focus
            if (holder.listView.isFocusable()) {
                dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN,holder.listView.getSelectedItemPosition());
            } else {
                // According to the down button, switch and the focus of the two icon
                int type = 1;
                if (tmpType == Constants.OPTION_STATE_VIDEO) {
                    type = Constants.OPTION_STATE_ALL;
                } else {
                    type = tmpType + 1;
                }
                holder.changeLeftFocus(tmpType, type);
                // Save the current focus data type
                tmpType = type;
            }
            return true;
        }
        else if (Constants.GRIDVIEW_MODE==mListOrGridFlag) {
            // gridView current gains focus
             if (holder.gridView.isFocusable()) {
                 dispatchKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN,holder.gridView.getSelectedItemPosition());
             } else {
                 // According to the down button, switch and the focus of the two icon
                 int type = 1;
                 if (tmpType == Constants.OPTION_STATE_VIDEO) {
                     type = Constants.OPTION_STATE_ALL;
                 } else {
                     type = tmpType + 1;
                 }
                 Log.d(TAG, "old type : " + tmpType + " new type : " + type);
                 holder.changeLeftFocus(tmpType, type);
                 // Save the current focus data type
                 tmpType = type;
             }
            return true;

        }

        return true;
    }

    /************************************************************************
     * Private method area
     ************************************************************************/
    /*
     * Exit system call to free resources or reverse registering listeners.
     */
    private void release() {
        dlnaDataBrowser.release();
        sambaDataBrowser.release();
    }

    /*
     * key distributed to each module for processing.
     */
    private void dispatchKeyEvent(int keyCode, int position) {
        if (getCurrentDataSource() == Constants.BROWSER_TOP_DATA) {
            topDataBrowser.processKeyDown(keyCode, position);
        } else if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
            localDataBrowser.processKeyDown(keyCode, position);
        } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
            dlnaDataBrowser.processKeyDown(keyCode, position);
        } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
            sambaDataBrowser.processKeyDown(keyCode, position);
        }
    }

    /*
     * when an exception occurs subsequent processing.
     */
    private void processExceptions(int code) {
        // ping network equipment over time
        if (code == Constants.FAILED_TIME_OUT) {
            // shielding key
            setCanResponse(false);

            if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {
                showToast(R.string.miss_dlna_device);
                // loading progress tip
                showScanMessage(R.string.loading_dlna_device);

                // browsing dlna equipment
                dlnaDataBrowser.browser(-1, 0);
            } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
                showToast(R.string.miss_samba_device);
                // loading progress tip
                showScanMessage(R.string.loading_samba_device);

                // browsing dlna equipment
                sambaDataBrowser.browser(-1, dataType);
            }

            // need to continue shielding keypress, so direct return
            return;

            // landing samba equipment password mistake
        } else if (code == Constants.FAILED_WRONG_PASSWD) {
            showToast(R.string.login_wrong_password);

            // landing samba equipment failure
        } else if (code == Constants.FAILED_LOGIN_FAILED) {
            showToast(R.string.login_failed);

            // other landing samba equipment error
        } else if (code == Constants.FAILED_LOGIN_OTHER_FAILED) {
            showToast(R.string.login_other_failed);

            // do not support media format
        } else if (code == Constants.UNSUPPORT_FORMAT) {
            showToast(R.string.unsupport_format);

            // mount failure
        } else if (code == Constants.MOUNT_FAILED) {
            showToast(R.string.mount_failed);

            // network do not use
        } else if (code == Constants.NETWORK_EXCEPTION) {
            showToast(R.string.network_unconnected);
        }

        // let go to key shielding
        setCanResponse(true);

    }

    /*
     * switching currently browsing media types.
     */
    private void switchMediaType(int type) {

        // local resource switch file type
        if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
            localDataBrowser.refresh(dataType);

            // dlna data default always reveal all types of media files
        } else if (getCurrentDataSource() == Constants.BROWSER_DLNA_DATA) {

            // switching currently browsing the samba resource file type
        } else if (getCurrentDataSource() == Constants.BROWSER_SAMBA_DATA) {
            sambaDataBrowser.refresh(type);
        }

    }

    /*
     * hidden loading progress tooltip.
     */
    private void dismissScanMessage() {
        // loading tooltip invisible
        holder.showScanStatus(false);
        holder.updateScanStatusText(false, 0);
        // let go to key shielding
        setCanResponse(true);
    }

    /*
     * display loading progress tooltip.
     */
    private void showScanMessage(int id) {
        // shielding key
        setCanResponse(false);

        // loading tooltip invisible
        holder.showScanStatus(true);
        if (0 != id) {
            holder.updateScanStatusText(true, id);
        }
    }

    /*
     * display tooltip.
     */
    private void showToast(int id) {
        // Resource id cannot use
        if (id <= 0) {
            return;
        }
        // activity life cycle has ended
        if (isFinishing()) {
            return;
        } else {
            String text = getString(id);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

    }

    /*
     * display file (clamp) name hints or data sources hint, such as local disk
     * data.
     */
    private void showHits(int index) {
        if (index < desDataList.size()&&Constants.LISTVIEW_MODE==mListOrGridFlag) {
            if (index < 0) {
                index = 0;
                Log.i(TAG,"Monkey Test,showHits index < 0,force index 0!");
            }
            BaseData baseData = desDataList.get(index);
            holder.refreshItemFocusState(index) ;
            // local documents show full path
            if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                String path = baseData.getPath();
                Log.d("sch", baseData.getParentPath()+"/"+path+"");
                //zb20150613 add
                if(path==null)
             	   curr_path=0;
                else 
             	   curr_path=1;
                if(path != null)
				{
					currFocusPathString=path;
					mFileControl.currently_path=baseData.getParentPath();
				}else if(baseData.getParentPath() == null){
					mFileControl.currently_path = currFocusPathString;
				}else {
					 currFocusPathString=mFileControl.get_currently_path();
				}
                //end
                if (path != null && !path.equals("")) {
                    holder.setDisplayTip(path);
                } else {
                    holder.setDisplayTip(baseData.getName());
                }
                Log.d("sch", "mFileControl.currently_path:    "+mFileControl.currently_path);
                // network data display file name
            } else {
                holder.setDisplayTip(baseData.getName());
            }
        }else if (index<tmpArray.size()&&Constants.GRIDVIEW_MODE==mListOrGridFlag) {
            BaseData baseData = tmpArray.get(index);
            // local documents show full path
            if (getCurrentDataSource() == Constants.BROWSER_LOCAL_DATA) {
                String path = baseData.getPath();
                if (path != null && !path.equals("")) {
                    holder.setDisplayTip(path);
                } else {
                    holder.setDisplayTip(baseData.getName());
                }
            // network data display file name
            } else {
                holder.setDisplayTip(baseData.getName());
            }
        } else {
            Log.d(TAG, "showHits invalid index : " + index);
        }
    }

    /*
     * Print version.
     */
    private void showVersion() {
        System.out.println(getResources().getString(R.string.mm_version));
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if(requestCode==0&&resultCode==1){
			String name=data.getStringExtra("name");
			localDataBrowser.refresh(name);
		}
	}

}
