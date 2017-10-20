package com.toptech.launcherkorea2.dao;

import java.util.ArrayList;
import java.util.List;

import com.toptech.launcherkorea2.model.AppBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 业务逻辑
 * @author calvin
 */
public class DaoService {
	DatabaseHelper dbHelper;

	public DaoService(Context context) {
		this.dbHelper = new DatabaseHelper(context);
	}
	
	//新增记录
	public void save(AppBean bean){
		SQLiteDatabase sqlitedb = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("pkg", bean.getPkg());
		sqlitedb.insert("t_app", "aid", values);
		sqlitedb.close();
	}
	
	//修改记录
	public void update(AppBean bean){
		SQLiteDatabase sqlitedb = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("pkg", bean.getPkg());
		sqlitedb.update("t_app", values, "aid=?", new String[]{String.valueOf(bean.getAid())});
		sqlitedb.close();
	}
	
	//根据aid查找某一条记录
	public AppBean find(Integer aid){
		SQLiteDatabase sqlitedb = dbHelper.getReadableDatabase();
		Cursor cursor = sqlitedb.query("t_app", new String[]{"aid","pkg"},
				"aid=?", new String[]{String.valueOf(aid)}, null, null, null);
		if(cursor.moveToNext()){
			AppBean bean = new AppBean();
			bean.setAid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("aid"))));
			bean.setPkg(cursor.getString(cursor.getColumnIndex("pkg")));
			return bean;
		}
		cursor.close();
		sqlitedb.close();
		return null;
	}
	
	//获取最后一条记录ID
	public int getLastId(){
		int lastId = -1;
		SQLiteDatabase sqlitedb = dbHelper.getReadableDatabase();
		Cursor cursor = sqlitedb.query("t_app", new String[]{"aid"},
				null, null, null,null, "aid DESC","0,1");
		if(cursor.moveToNext()){
			lastId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("aid")));
		}
		cursor.close();
		sqlitedb.close();
		return lastId;
	}
	
	//删除一条或多条记录
	public void delete(Integer... ids){
		if(ids.length>0){
			StringBuilder sb = new StringBuilder();
			String[] strids = new String[ids.length];
			for(int i=0;i<ids.length;i++){
				sb.append("?").append(",");
				strids[i] = String.valueOf(ids[i]);
			}
			sb.deleteCharAt(sb.length()-1);			
			SQLiteDatabase sqlitedb = dbHelper.getWritableDatabase();
			sqlitedb.delete("t_app", "aid in("+sb+")",strids);
			sqlitedb.close();
		}
	}
	
	//根据包名删除一条记录
	public void delete(String pkg){
		SQLiteDatabase sqlitedb = dbHelper.getWritableDatabase();
		sqlitedb.delete("t_app", "pkg = ?", new String[]{pkg});
		sqlitedb.close();
	}
	
	//删除所有记录
	public void deleteAll(){
		SQLiteDatabase sqlitedb = dbHelper.getWritableDatabase();
		sqlitedb.delete("t_app", null, null);
		sqlitedb.close();
	}
	
	//查询所有记录
	public List<AppBean> getAllData(){
		List<AppBean> list = new ArrayList<AppBean>();
		SQLiteDatabase sqlitedb = dbHelper.getReadableDatabase();
		Cursor cursor = sqlitedb.query("t_app", new String[]{"aid","pkg"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			AppBean bean = new AppBean();
			bean.setAid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("aid"))));
			bean.setPkg(cursor.getString(cursor.getColumnIndex("pkg")));
			list.add(bean);
		}
		cursor.close();
		sqlitedb.close();
		return list;
	}
	
	//查找分页记录（二级列表）
	public List<AppBean> getScrollData(int startResult,int maxResult,String aid){
		List<AppBean> list = new ArrayList<AppBean>();
		SQLiteDatabase sqlitedb = dbHelper.getReadableDatabase();
		Cursor cursor = sqlitedb.query("t_app", new String[]{"aid","pkg"},
				null, null, null, null, "aid DESC", startResult+","+maxResult);
		while(cursor.moveToNext()){
			AppBean bean = new AppBean();
			bean.setAid(Integer.parseInt(cursor.getString(cursor.getColumnIndex("aid"))));
			bean.setPkg(cursor.getString(cursor.getColumnIndex("pkg")));
			list.add(bean);
		}
		cursor.close();
		sqlitedb.close();
		return list;
	}

	
	//查询记录总数
	public long getCount(){
		long count = 0;
		SQLiteDatabase sqlitedb = dbHelper.getReadableDatabase();
	    Cursor cursor = sqlitedb.query("t_app", new String[]{"count(*)"}, null, null, null, null, null);
	    if(cursor.moveToNext()){
	    	count = cursor.getLong(0);
	    }
	    cursor.close();
	    sqlitedb.close();
		return count;
	}
}
