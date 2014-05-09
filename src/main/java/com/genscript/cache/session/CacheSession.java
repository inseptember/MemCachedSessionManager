package com.genscript.cache.session;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genscript.cache.IMemCachedCache;
import com.schooner.MemCached.MemcachedItem;

public class CacheSession implements ISession {
	
	Logger logger = LoggerFactory.getLogger(CacheSession.class);
	
	private final IMemCachedCache client;
	
	public static final String SESSION_COOKIE_NAME = "JSESSION";
	
	private final String id;
	
	private int maxInactiveInterval;
	
	private boolean invalid = false;
	
	public CacheSession(IMemCachedCache client, String id){
		this.client = client;
		this.id = id;
	}

	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getId() {
		return this.id;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAttribute(String name) {
		return this.getValue(name);
	}

	public Object getValue(String name) {
		Map<String, String> keysMap = this.getKeysMap();
		Object val = null;
		if(keysMap.containsKey(name)){
			val = client.get(keysMap.get(name), maxInactiveInterval);
		}
		return val;
	}

	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(this.getKeysMap().keySet());
	}

	public  String[] getValueNames() {
		Map<String, String> keysMap = this.getKeysMap();
		return keysMap.keySet().toArray(new String[keysMap.size()]);
	}
	
	private Map<String, String> getKeysMap(){
		MemcachedItem item = this.getKeys();
		Map<String, String> keysMap = this.getKeysMap(item);
		return keysMap;
	}
	
	private Map<String, String> getKeysMap(MemcachedItem item){
		Map<String, String> keysMap = null;
		if(item == null || item.getValue() == null){
		}else{
			try {
				keysMap = (Map<String, String>) item.getValue();
			} catch (Exception e) {
				logger.error("Class Type is not Equal, Please Check the Memcached Manually", e);
			}
		}
		if(keysMap == null){
			keysMap = new HashMap<String, String>();
		}
		return keysMap;
	}
	
	private MemcachedItem getKeys(){
		MemcachedItem item =  this.getClient().gets(SESSION_COOKIE_NAME + this.getId());
		return item;
	}
	
	private void putKeys(long casUnique, Map<String, String> keysMap){
		this.getClient().cas(SESSION_COOKIE_NAME + this.getId(), keysMap, this.getMaxInactiveInterval(), casUnique);
	}
	
	private String putNewNameInKeys(long casUnique, Map<String, String> keysMap, String name){
		String newKey = UUID.randomUUID().toString();
		keysMap.put(name, newKey);
		this.putKeys(casUnique, keysMap);
		return newKey;
	}
	
	public void setAttribute(String name, Object value) {
		this.putValue(name, value);
	}

	public void putValue(String name, Object value) {
		MemcachedItem item = this.getKeys();
		Map<String, String> keysMap = this.getKeysMap(item);
		if(value == null){
			if(keysMap.containsKey(name)){
				this.getClient().remove(keysMap.get(name));
				keysMap.remove(name);
				this.putKeys(item.getCasUnique(), keysMap);
			}
		}else{
			if(keysMap.containsKey(name)){
				
			}else{
				String newKey = UUID.randomUUID().toString();
				keysMap.put(name, newKey);
				if(item == null){
					this.getClient().add(SESSION_COOKIE_NAME + this.getId(), keysMap);
				}else{
					this.putKeys(item.getCasUnique(), keysMap);
				}
			}
			this.getClient().put(keysMap.get(name), value, getMaxInactiveInterval());
		}
	}

	public void removeAttribute(String name) {
		this.removeValue(name);
	}

	public void removeValue(String name) {
		this.putValue(name, null);
	}

	public void invalidate() {
		MemcachedItem item = this.getKeys();
		Map<String, String> keysMap = this.getKeysMap(item);
		Iterator<String> it = keysMap.keySet().iterator();
		while(it.hasNext()){
			this.getClient().remove(keysMap.get(it.next()));
		}
		this.getClient().remove(SESSION_COOKIE_NAME + this.getId());
		this.invalid = true;
	}

	public boolean isNew() {
		return true;
	}

	public IMemCachedCache getClient() {
		return client;
	}

	public boolean isInvalid() {
		return invalid;
	}

}
