package com.genscript.cache.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.genscript.cache.IClientManager;
import com.genscript.cache.IMemCachedCache;
import com.genscript.cache.memcached.MemCachedClientCacheManager;
import com.genscript.cache.session.ICacheSessionUtil;

@Deprecated
public class CacheSessionUtil implements ICacheSessionUtil {
	
	Logger logger = LoggerFactory.getLogger(CacheSessionUtil.class);
	
	private static IMemCachedCache session;
	
	public static IClientManager<IMemCachedCache> manager;
	
	private static int expire = 30 * 60;
	
	private CacheSessionUtil(){
		
	}
	
	public IMemCachedCache getCache(){
		return session;
	}
	
	public static CacheSessionUtil getInstance(){
		return CacheSessionUtilHolder.instance;
	}

	public Object getRow(String tblName, String primaryKey) {
		Map<String, Object> map = (Map<String, Object>) get(tblName);
		if (map!=null&&!map.isEmpty()) {
			for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
				String key = iter.next();
				if (key.equals(primaryKey)) {
					return map.get(key);
				}
			}
		}
		return null;
	}
	
	public Object get(String name){
		Object map = (Object) session.get(name);
		return map;
	}
	
	public void update(String name, Object value){
		this.update(name, value, new Date(expire));
	}
	
	public void update(String name, Object value, Date expire){
		if(name.length()>250){
			logger.warn("attention:key length is longger than 250");
		}
		session.put(name, value, expire);
	}
	
	public void remove(String name){
		session.remove(name);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getRow(String tblName, int number, String sessNo) {
		Map<String, Object> sessionMap;
			if (number == 0) {
				sessionMap = (HashMap<String, Object>) this.getRow(tblName,
						sessNo);
			} else {
				sessionMap = (HashMap<String, Object>) this.getRow(tblName,
						String.valueOf(number));
			}
			return sessionMap;
		}

	public List<Object> getRows(String tblName, List<String> primaryKeyList) {
		List<Object> list = null;
		for (String val : primaryKeyList) {
			Object object = getRow(tblName, val);
			if(list == null){
				list = new ArrayList<Object>();
			}
			if (object != null) {
				list.add(object);
			}
		}
		return list;
	}

	public void updateRow(String tblName, String primaryKey, Object object) {
		this.insertRow(tblName, primaryKey, object);
	}

	public void deleteRows(String tblName, List<String> primaryKeyList) {
		List<Object> list = getRows(tblName, primaryKeyList);
		if (list != null && list.size() > 0) {
			Map<String, Object> map = (Map<String, Object>) this.get(tblName);
			for (Object key : list) {
				map.remove(key);
			}
			this.update(tblName, map);
		}
	}

	public void deleteRow(String tblName, String primaryKey) {
		Map<String, Object> map = (Map<String, Object>) this.get(tblName);
		if (map!=null&&!map.isEmpty()) {
			for (Iterator<String> iter = map.keySet().iterator(); iter
					.hasNext();) {
				String key = iter.next();
				if (primaryKey.equals(key)) {
					iter.remove();
					map.remove(key);
				}
			}
			this.update(tblName, map);
		}
	}

	public void insertRow(String tblName, String primaryKey, Object object) {
		if (tblName == null || primaryKey == null || object == null) {
			throw new RuntimeException("Missing the arguments");
		}
		Map<String, Object> map = null;
		if (session.containsKey(tblName)) {
			map = (Map<String, Object>) this.get(tblName);
			map.put(primaryKey, object);
		} else {
			map = new Hashtable<String, Object>();
			map.put(primaryKey, object);
		}
		this.update(tblName, object);
	}

	public void insertRow(String tblName, int number, String sessNo,
			Object object) {
		if (number == 0) {
			this.insertRow(tblName, sessNo, object);
		} else {
			this.insertRow(tblName, String.valueOf(number), object);
		}
	}

	@SuppressWarnings("unchecked")
	public Object getOneRow(String tblName, String no, String key) {
		Map<String, Object> map = (Map<String, Object>) this.getRow(tblName, no);
		if (map != null && map.containsKey(key)) {
			return map.get(key);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void updateOneRow(String tblName, String no, String key,
			Object updateObject) {
		Map<String, Object> map = (Map<String, Object>) this.getRow(tblName, no);
		if (map == null) {
			map = new LinkedHashMap<String, Object>();
			this.insertRow(tblName, no, map);
		}
		map.put(key, updateObject);
		this.updateRow(tblName, no, map);
	}

	@SuppressWarnings("unchecked")
	public void deleteOneRow(String tblName, String no, String key) {
		Map<String, Object> map = (Map<String, Object>) this.getRow(
				tblName, no);
		if(map == null){
			map = new LinkedHashMap<String, Object>();
			this.insertRow(tblName, no, map);
		}
		if(key.replaceAll("[0-9]", "").length() == 0){
			map.put(key, null);
		}else if(map.containsKey(key)){
			map.remove(key);
		}
		this.updateRow(tblName, no, map);
	}
	
	private static class CacheSessionUtilHolder{
		static CacheSessionUtil instance = new CacheSessionUtil();
	}
	
	public synchronized static void initailCacheManager(){
		manager = CacheUtil.getCacheManager(MemCachedClientCacheManager.class.getName());
		manager.setConfigFile(MemCachedClientCacheManager.MEM_CACHED_CONFIG_FILE);
		manager.startUp();
		session = manager.getCache("client1");
	}
	
	public static void uninitalCacheMananger(){
		if(manager!=null){
			manager.shutDown();
		}
	}

	/**
	 * @return the expire
	 */
	public int getExpire() {
		return expire;
	}

	/**
	 * @param expire the expire to set
	 */
	public void setExpire(int expire) {
		CacheSessionUtil.expire = expire;
	}
}
