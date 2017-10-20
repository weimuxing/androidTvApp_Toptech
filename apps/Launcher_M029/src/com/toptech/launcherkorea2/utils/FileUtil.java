// 2012-4-18下午07:49:44
 
package com.toptech.launcherkorea2.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FileUtil {
	public static String[] getImagePaths(String folderPath) {
		File file01 = new File(folderPath);
		String[] imagePath = null;
		String[] imageNames = file01.list(new FilenameFilter() {

			@Override
			public boolean accept(File file, String fileName) {
				// TODO Auto-generated method stub
				File name=new File(file+"/"+fileName);//20150828
				if(name.isDirectory())
					return false;
				String fileEnd = fileName.substring(
						fileName.lastIndexOf(".") + 1, fileName.length());
				if (fileEnd.equalsIgnoreCase("jpg")
						|| fileEnd.equalsIgnoreCase("png")
						|| fileEnd.equalsIgnoreCase("bmp")) {
					return true;
				}
				return false;
			}
		});

		if (imageNames.length > 0) {
			imagePath = new String[imageNames.length];
			for (int i = 0; i < imageNames.length; i++) {
				if(folderPath.endsWith(File.separator)){
					imagePath[i] = folderPath + imageNames[i];
				}else{
					imagePath[i] = folderPath +File.separator+ imageNames[i];
				}
				//System.out.println("wangdaoyun----------:::::::::----------->fileNames:"+ imagePath[i]);
			}
		}

		return imagePath;
	}

	public static ArrayList<String> getImagePaths(String folderPath,boolean mb) {
		File file = new File(folderPath);
		ArrayList<String> pathList = new ArrayList<String>();

		String[] imageNames = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File file, String fileName) {
				// TODO Auto-generated method stub
				File name=new File(file+"/"+fileName);//20150828
				if(name.isDirectory())
					return false;
				//end
				String fileEnd = fileName.substring(
						fileName.lastIndexOf(".") + 1, fileName.length());
				if (fileEnd.equalsIgnoreCase("jpg")
						|| fileEnd.equalsIgnoreCase("png")
						|| fileEnd.equalsIgnoreCase("bmp")) {
					return true;
				}
				return false;
			}
		});

		if (imageNames.length > 0) {
			String imagePath = null;
			for (int i = 0; i < imageNames.length; i++) {
				if (folderPath.endsWith(File.separator)) {
					imagePath = folderPath + imageNames[i];
				} else {
					imagePath = folderPath + File.separator + imageNames[i];
				}
				if (imagePath != null) {
					System.out
							.println("wangdaoyun----------:::::::::----------->fileNames:"
									+ imagePath);
					pathList.add(imagePath);
				}

			}
		}

		return pathList;
	}
	
}
