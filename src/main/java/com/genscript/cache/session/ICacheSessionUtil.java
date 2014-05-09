package com.genscript.cache.session;

import java.util.List;
import java.util.Map;

@Deprecated
public interface ICacheSessionUtil {
	
	public Object getRow(String tblName, String primaryKey);

	public Map<String, Object> getRow(String tblName, int number,String sessNo);
	
	public List<Object> getRows(String tblName, List<String> primaryKeyList);
	
	public void updateRow(String tblName, String primaryKey, Object object);
	
	public void deleteRows(String tblName, List<String> primaryKeyList);
	
	public void deleteRow(String tblName, String primaryKey);
	
	public void insertRow(String tblName, String primaryKey, Object object);
	
	public void insertRow(String tblName, int number, String sessNo, Object object);
	
	public Object getOneRow(String tblName, String no, String key);
	
	public void updateOneRow(String tblName, String no, String key, Object updateObject);
	
	public void deleteOneRow(String tblName, String no, String key);
}
