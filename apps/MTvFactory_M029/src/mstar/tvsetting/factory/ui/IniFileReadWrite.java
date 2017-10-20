package mstar.tvsetting.factory.ui;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.format.DateFormat;
import android.util.Log;


public class IniFileReadWrite {
	public static void main(String[] args) {
		/*
		String file = "D:\\1.ini";
		String section = "panel";
		String variable = "u16MinPWMvalue";
		String defaultValue = "defalut panel";
		
		try {
			String val = getProfileString(file, section, variable, defaultValue);
			System.out.println(val);

			setProfileString(file, section, variable, "new panl");

		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	public static String getProfileString(String file, String section,
			String variable, String defaultValue) throws IOException {
		String strLine, value = "";
		BufferedReader bufferedReader;
		boolean isInSection = false;
		bufferedReader = new BufferedReader(new FileReader(file));
		try {
			while ((strLine = bufferedReader.readLine()) != null) {
				strLine = strLine.trim();
				strLine = strLine.split("[;]")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));

				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");

					m = p.matcher(strLine);
					if (m.matches()) {
						isInSection = true;
					} else {
						isInSection = false;
					}
				}
				if (isInSection == true) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					if (strArray.length == 1) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = "";
							return value;
						}
					} else if (strArray.length == 2) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = strArray[1].trim();
							return value;
						}
					} else if (strArray.length > 2) {
						value = strArray[0].trim();
						if (value.equalsIgnoreCase(variable)) {
							value = strLine.substring(strLine.indexOf("=") + 1)
									.trim();
							return value;
						}
					}
				}
			}
		} finally {
			bufferedReader.close();
		}
		return defaultValue;
	}

	public static boolean setProfileString(String file, String section,
			String variable, String value) throws IOException {
		String fileContent, allLine, strLine, newLine, remarkStr;
		String getValue;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		fileContent = "";
		try {
			while ((allLine = bufferedReader.readLine()) != null) {
				allLine = allLine.trim();
				if (allLine.split("[;]").length > 1)
					remarkStr = ";" + allLine.split(";")[1];
				else
					remarkStr = "";

				strLine = allLine.split(";")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");
					m = p.matcher(strLine);
					if (m.matches()) {
						isInSection = true;
					} else {
						isInSection = false;
					}
				}
				if (isInSection == true) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					if (getValue.equalsIgnoreCase(variable)) {
						newLine = getValue + " = " + value + "; " + remarkStr;
						fileContent += newLine + "\r\n";
						while ((allLine = bufferedReader.readLine()) != null) {
							fileContent += allLine + "\r\n";
						}
						bufferedReader.close();
						BufferedWriter bufferedWriter = new BufferedWriter(
								new FileWriter(file, false));
						bufferedWriter.write(fileContent);
						bufferedWriter.flush();
						bufferedWriter.close();
						return true;
					}
				}
				fileContent += allLine + "\r\n";
			}
		} finally {
			bufferedReader.close();
		}
		return false;
	}
	public static boolean setProfileString(String file, String section,
			List<Map<String, String>> list) throws IOException {
		
		String time= DateFormat.format("yyyy-MM-dd", new Date()).toString();
		
		String fileContent, allLine, strLine, newLine, remarkStr;
		String getValue;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		fileContent = "";
		try {
			while ((allLine = bufferedReader.readLine()) != null) {
				allLine = allLine.trim();
				if (allLine.split("[;]").length > 1)
					remarkStr = ";" + allLine.split(";")[1];
				else
					remarkStr = "";
				strLine = allLine.split(";")[0];
				Pattern p;
				Matcher m;
				p = Pattern.compile("\\[\\s*.*\\s*\\]");
				m = p.matcher((strLine));
				if (m.matches()) {
					p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");
					m = p.matcher(strLine);
					if (m.matches()) {
						isInSection = true;
					} else {
						isInSection = false;
					}
				}
				if (isInSection == true) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					getValue = strArray[0].trim();
					
					if (getValue.equalsIgnoreCase(list.get(0).get("id"))) {
						newLine = getValue + " = " + list.get(0).get("val") + "; " + "#modify@"+time+remarkStr;
						fileContent += newLine + "\r\n";
						int i=1;
						while ((allLine = bufferedReader.readLine()) != null) {
							{
								if(i<list.size())
								{
									if (allLine.split("[;]").length > 1)
									{
										remarkStr = ";" + allLine.split(";")[1];
										
									}
									else
										remarkStr = "";
									
									if(allLine.split(";")!=null && allLine.split(";").length>0)
										strLine = allLine.split(";")[0];
									
									strLine = strLine.trim();
									if(strLine.split("=")!=null && strLine.split("=").length>0)
									{
										String[] strArray2 = strLine.split("=");
										getValue = strArray2[0].trim();
									}
									
									if(getValue.equalsIgnoreCase(list.get(i).get("id")))
									{
										newLine = getValue + " = " + list.get(i).get("val") + "; " + "#modify@"+time+remarkStr;
										fileContent += newLine + "\r\n";
										i++;
									}
									else {
										fileContent += allLine + "\r\n";
									}
									
								}
								else {
									fileContent += allLine + "\r\n";
								}
							}
						}
						bufferedReader.close();
						BufferedWriter bufferedWriter = new BufferedWriter(
								new FileWriter(file, false));
						bufferedWriter.write(fileContent);
						bufferedWriter.flush();
						bufferedWriter.close();
						return true;
					}
				}
				fileContent += allLine + "\r\n";
			}
		} finally {
			bufferedReader.close();
		}
		return false;
	}
}