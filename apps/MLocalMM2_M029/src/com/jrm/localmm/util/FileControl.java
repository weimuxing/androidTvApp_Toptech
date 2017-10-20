/*
 * Copyright (C) 2009 The Rockchip Android MID Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jrm.localmm.util;

import java.io.File;
import java.util.ArrayList;

import com.jrm.localmm.R;
import com.jrm.localmm.ui.main.FileBrowserActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.widget.ListView;
import android.widget.Toast;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import android.os.Environment;

public class FileControl {	
	final String TAG = "FileControl";
	final boolean DEBUG = true;	//true;
	private void LOG(String str)
	{
		if(DEBUG)
		{
			Log.d(TAG,str);
		}
	}
	public ArrayList<FileInfo> folder_array;	
	Resources resources;	
	Context context_by;
	ListView main_ListView;
	ListView mDeviceList;
	public static String currently_parent = null;	
	public static String currently_path = null;

    public String sdcard_dir = Environment.getExternalStorageDirectory().getPath();
	public String usb_dir0 = Tools.getUSBMountedPath()+"/usb/sda1";
	public String usb_dir1 = Tools.getUSBMountedPath()+"/usb/sdb1";
	public String usb_dir2 = Tools.getUSBMountedPath()+"/usb/sdc1";
	public String usb_dir3 = Tools.getUSBMountedPath()+"/usb/sdd1";
	public String usb_dir4 = Tools.getUSBMountedPath()+"/usb/sde1";
	public String usb_dir5 = Tools.getUSBMountedPath()+"/usb/sdf1";

	
	int currently_state;
	final int AZ_COMPOSITOR = 0;	
	final int TIME_COMPOSITOR = 1;	
	final int SIZE_COMPOSITOR = 2;	
	final int TYPE_COMPOSITOR = 3;	
	
	
	String[] music_postfix = {".mp3", ".ogg", ".wma", ".wav", ".ape", 
								".mid", ".flac", ".mp3PRO", ".au", ".avi"};
	int size_postfix[] = new int[music_postfix.length];
	int pit_postfix[] = new int[music_postfix.length];
	ArrayList<FileInfo> type_compositor_file;
	
	public String str_audio_type = "audio/*";
	public String str_video_type = "video/*";
	public String str_image_type = "image/*";
	public String str_txt_type = "text/plain";
	public String str_pdf_type = "application/pdf";
	public String str_epub_type = "application/epub+zip";
	public String str_apk_type = "application/vnd.android.package-archive";
	
	public static boolean is_enable_fill = true;
	public static boolean is_finish_fill = false;
	
	public static boolean is_first_path = true;	

	public static String str_last_path = null;
	public static int last_item;
	public static FileBrowserActivity mRockExplorer;	

	public static boolean is_enable_del = true;
	public static boolean is_finish_del = true;	
	public static ArrayList<FileInfo> delFileInfo = new ArrayList<FileInfo>();
	
    public FileControl(Context context, String path, ListView tmp_main_listview) {
        
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);     
        
        currently_parent = path;
        currently_path = path;
        currently_state = SIZE_COMPOSITOR;    		
        main_ListView = tmp_main_listview;
        resources = context.getResources();
        context_by = context;
        first_fill();
		
    }
       
    private void fill(File files) {
    	is_first_path = false;
    	folder_array = new ArrayList<FileInfo>();	
    	LOG("in fill, is_enable_fill = " + is_enable_fill);
//    	LOG("in fill, files.length = " + files.list().length);
    	if(mRockExplorer.mWaitDialog != null){
    		mRockExplorer.mWaitDialog.setCancelable(true);
    	}
    	for (File file : files.listFiles()){
		if(!is_enable_fill)
			break;
		if(file.canRead()){
			if(file.isDirectory()){		
				folder_array.add(0, changeFiletoFileInfo(file)); 
			}else{
				folder_array.add(changeFiletoFileInfo(file)); 
			}	
		}
	}
	LOG("in fill, -- fill is over !!!!!!!!!!!!!!");
    }
    
    public void refill(String path){
    	final File files = new File(path); 
    	LOG("in the refill, path = " + path);
//    	is_enable_fill = true;		
    	is_finish_fill = false;
    	fill(files);
	    currently_path = new String(files.getPath());
	    LOG("in the refill, ---- currently_path = " + currently_path);
	    if(currently_path.equals(sdcard_dir))
	    	currently_parent = null;
	    else{
	    	currently_parent = new String(files.getParent());
	    }
	    	
	    LOG("in the refill, ---- currently_path = " + currently_path+" current_parent = "+currently_parent);
	    
	    is_finish_fill = true;
	    is_enable_fill = true;
    	LOG("in the refill, ---- end of refill()----"+"finish_fill:"+is_finish_fill+"enable_fill:"+is_enable_fill);
    }
    
    public void refillwithThread(String path){
    	final File files = new File(path); 
    	LOG("in the refill, path = " + path);
//    	is_enable_fill = true;		
    	is_finish_fill = false;
    	new Thread(){
    		public void run(){
    			LOG("in the refillwithThread, ----begin Thread.start()");
		    	fill(files);
			    currently_path = new String(files.getPath());
			    LOG("in the refillwithThread, ---- currently_path = " + currently_path);
			    if(currently_path.equals("/"))
			    	currently_parent = new String("/");
			    else{
			    	currently_parent = new String(files.getParent());
			    }

			    LOG("in the refillwithThread, ---- currently_path = " + currently_path+" current_parent = "+currently_parent);
			    
			    
			    is_finish_fill = true;
			    is_enable_fill = true;
			    LOG("in the refillwithThread, ----end Thread.start()----"+"finish_fill:"+is_finish_fill+"enable_fill:"+is_enable_fill);
    		}
    	}.start();
    	LOG("in the refillwithThread, ---- after Thread.start()");
    }
    
    /**/
    public ArrayList<FileInfo> get_folder_array(){
    	return folder_array;
    }
    public void set_folder_array(ArrayList<FileInfo> tmp_set_array){
    	folder_array = tmp_set_array;
    }
    
    
    public static String get_currently_parent(){
    	return currently_parent;
    }
    
    public static String get_currently_path(){
    	return currently_path;
    }
    
    public void set_main_ListView(ListView tmp_listview){
    	main_ListView = tmp_listview;
    	if(main_ListView == null){
    		LOG("in set_main_ListView,------------main_ListView = null");
    	}
    }
    

    public void setMainAdapter(){
//    	LOG("in setMainAdapter,----11----folder_array size = " + folder_array.size());
//    	NormalListAdapter tempAdapter = new NormalListAdapter(context_by, folder_array);       	
//    	LOG("in setMainAdapter,----22----folder_array size = " + folder_array.size());
//    	main_ListView.setAdapter(tempAdapter);    	
    }
    
    public String getMIMEType(File f) 
    { 
      String type="";
      String fName=f.getName();
      String end=fName.substring(fName.lastIndexOf(".")+1,
                                 fName.length()).toLowerCase(); 
      
      if(end.equalsIgnoreCase("mp3")||end.equalsIgnoreCase("wma")
         ||end.equalsIgnoreCase("mp1")||end.equalsIgnoreCase("mp2")
    	 ||end.equalsIgnoreCase("ogg")||end.equalsIgnoreCase("oga")
    	 ||end.equalsIgnoreCase("flac")||end.equalsIgnoreCase("ape")
    	 ||end.equalsIgnoreCase("wav")||end.equalsIgnoreCase("aac")
    	 ||end.equalsIgnoreCase("m4a")||end.equalsIgnoreCase("m4r")
	 ||end.equalsIgnoreCase("amr")||end.equalsIgnoreCase("mid")
	 ||end.equalsIgnoreCase("asx")
    	 /*
         ||end.equalsIgnoreCase("mid")||end.equalsIgnoreCase("amr")
    	 ||end.equalsIgnoreCase("awb")||end.equalsIgnoreCase("midi")
    	 ||end.equalsIgnoreCase("xmf")||end.equalsIgnoreCase("rtttl")
    	 ||end.equalsIgnoreCase("smf")||end.equalsIgnoreCase("imy")
    	 ||end.equalsIgnoreCase("rtx")||end.equalsIgnoreCase("ota")*/)
      {
        type = str_audio_type; 
      }
      else if(end.equalsIgnoreCase("3gp")||end.equalsIgnoreCase("mp4")
    		  ||end.equalsIgnoreCase("rmvb")||end.equalsIgnoreCase("3gpp")
    		  ||end.equalsIgnoreCase("avi")||end.equalsIgnoreCase("rm")
    		  ||end.equalsIgnoreCase("mov")||end.equalsIgnoreCase("flv")
    		  ||end.equalsIgnoreCase("mkv")||end.equalsIgnoreCase("wmv")
		  ||end.equalsIgnoreCase("divx")||end.equalsIgnoreCase("bob")
		  ||end.equalsIgnoreCase("mpg") || end.equalsIgnoreCase("mpeg")
		  ||end.equalsIgnoreCase("ts") || end.equalsIgnoreCase("dat")
		  ||end.equalsIgnoreCase("m2ts"))
      {
        type = str_video_type;
        if(end.equalsIgnoreCase("3gpp")){
        	if(isVideoFile(f)){
        		type = str_video_type;
        	}else{
        		type = str_audio_type; 
        	}
        }
      }
      else if(end.equalsIgnoreCase("jpg")||end.equalsIgnoreCase("gif")
    		  ||end.equalsIgnoreCase("png")||end.equalsIgnoreCase("jpeg")
    		  ||end.equalsIgnoreCase("bmp"))
      {
        type = str_image_type;
      }
/*      else if(end.equalsIgnoreCase("txt"))
      {
        type = str_txt_type;
      }
*/      else if(end.equalsIgnoreCase("epub") || end.equalsIgnoreCase("pdb") || end.equalsIgnoreCase("fb2") || end.equalsIgnoreCase("rtf") || end.equalsIgnoreCase("txt"))
      {
        type = str_epub_type;
      }
      else if(end.equalsIgnoreCase("pdf"))
      {
        type = str_pdf_type;
      }
      else if(end.equalsIgnoreCase("apk"))
      {
    	type = str_apk_type;  
      }
      else
      {
        type="*/*";
      }
      
      return type; 
    }
    

    private String fname;
    public void delFileInfo(final ArrayList<FileInfo> file_paths){
    	is_enable_del = true;
	is_finish_del = false;
    	new Thread(){
    		public void run(){
    			for(int file_num = 0; file_num < file_paths.size(); file_num ++){
				boolean del_successful = true;
				if(file_paths.get(file_num).file.canWrite()){
	    				if(file_paths.get(file_num).file.isDirectory()){
    						del_successful = delDir(file_paths.get(file_num).file);
    					}else{
    						if(!file_paths.get(file_num).file.delete()){
							Log.e(TAG, "  ------- :   Delete file " + file_paths.get(file_num).file.getPath() + " fail~~");
							fname = file_paths.get(file_num).file.getName();
							mRockExplorer.mHandler.post(new Runnable(){

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(context_by, context_by.getString(R.string.edit_delete) + fname + context_by.getString(R.string.del_error), Toast.LENGTH_SHORT).show();
								}
								
							});
							is_enable_del = false;
						}
						if(!file_paths.get(file_num).file.canWrite())
							del_successful = false;
    					}
				}
				if(!is_enable_del){
					is_finish_del = true;
					return;
				}
				if(del_successful)
					delFileInfo.add(file_paths.get(file_num));				
    			}
			is_finish_del = true;
    		}
    	}.start();	
    }

    public boolean delDir(File dir){
	boolean ret = true;
	if(!is_enable_del)
		return false;
    	File[] file = dir.listFiles();
    	for (int i = 0; i < file.length; i++) {
    		if (file[i].isFile()){
			if(!is_enable_del)
				return false;
    			if(!file[i].delete()){
				Log.e(TAG, "  ------- :    Delete file " + file[i].getPath() + " fail~~");
			}
			if(!file[i].canWrite())
				ret = false;
    		}else{
			if(!is_enable_del)
				return false;
    			delDir(file[i]);
		}
    	}
    	dir.delete();
	return ret;
    }
    
    public void DelFile(File mInterruptFile){
    	if(mInterruptFile == null)
    		return;
    	LOG(" ____________ DelFile() " + mInterruptFile.getPath() + "   currently_path = " + currently_path);
    	File tmp_file = null;
    	if((tmp_file = new File(currently_path+File.separator+mInterruptFile.getName())).exists()){
    		tmp_file.delete();
    	}
    }
    

    public FileInfo changeFiletoFileInfo(File file){
    	FileInfo temp = new FileInfo();
		temp.file = file;
		//temp.musicType = isMusicFile(temp.name);
		if(file.isDirectory()){		
			temp.icon = resources.getDrawable(R.drawable.folder);
			temp.isDir = true;
		}	
		else {	
			temp.isDir = false;
		
			temp.file_type = getMIMEType(file);	
			temp.icon = getDrawable(temp.file_type);
		}
		return temp;
    }
    

    public Drawable getDrawable(String tmp_type){
    	Drawable d = null;
    	if(tmp_type.equals(str_audio_type)){
    		d = resources.getDrawable(R.drawable.audio);
    	}else if(tmp_type.equals(str_video_type)){
    		d = resources.getDrawable(R.drawable.video);
    	}else if(tmp_type.equals(str_image_type)){
    		d = resources.getDrawable(R.drawable.image);
	}else if(tmp_type.equals(str_txt_type) || tmp_type.equals(str_pdf_type) || tmp_type.equals(str_epub_type)){
                d = resources.getDrawable(R.drawable.storage_list);
    	}else if(tmp_type.equals(str_apk_type)){
    		d = resources.getDrawable(R.drawable.apk);
    	}else {
    		d = resources.getDrawable(R.drawable.blank_page);
    	}
    	return d;
    }
    
    public boolean is_contain_file(File cFile){
    	for(int i = 0; i < folder_array.size(); i ++){
    		if(folder_array.get(i).file.getPath().equals(cFile.getPath())){
    			return true;
    		}
    	}
    	return false;
    }
	
    void first_fill(){
    	is_first_path = true;
    	folder_array = new ArrayList<FileInfo>();
		mRockExplorer = (FileBrowserActivity)context_by;

    	currently_path = null;
    	currently_parent = null;
    }
    
    public boolean isVideoFile(File tmp_file){
    	String path = tmp_file.getPath();
    	ContentResolver resolver = context_by.getContentResolver();
    	String[] audiocols = new String[] {
    			MediaStore.Video.Media._ID,
    			MediaStore.Video.Media.DATA,
    			MediaStore.Video.Media.TITLE
        };  
    	LOG("in getFileUri --- path = " + path);
    	StringBuilder where = new StringBuilder();
    	where.append(MediaStore.Video.Media.DATA + "=" + "'" + path + "'");
      try{
    	Cursor cur = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        		audiocols,
        		where.toString(), null, null);
    	if(cur.moveToFirst()){
    		return true;
    	}
    	return false;
      }catch(Exception e){
        return false;
      }
    }

    private FileInfo createDLNAVirtualFile(String path){
    	FileInfo dlnaItem = new FileInfo();
    	dlnaItem.file = new File(path);
    	dlnaItem.isDir = true;
    	return dlnaItem;
    }
    

    private FileInfo createSMBVirtualFile(String path){
    	FileInfo smbItem = new FileInfo();
    	smbItem.file = new File(path);
    	smbItem.isDir = true;
    	return smbItem;
    }
}
