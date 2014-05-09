package com.genscript.cache.local;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genscript.cache.ICache;

public class LocalCacheImpl implements ICache<String, Object> {

	Logger logger = LoggerFactory.getLogger(LocalCacheImpl.class);
	
	private ConcurrentHashMap<String, Object>[] caches;
	
	private ConcurrentHashMap<String, Long> expCache;
	
	private int cacheSize = 7;
	
	private ScheduledExecutorService schdlExcutor;
	
	private int expInterval = 60 * 10;
	
	public LocalCacheImpl() {
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init(){
		caches = new ConcurrentHashMap[cacheSize];
		for(int i=0; i<cacheSize; i++){
			caches[i] = new ConcurrentHashMap<String, Object>();
		}
		
		expCache = new ConcurrentHashMap<String, Long>();
		schdlExcutor = Executors.newScheduledThreadPool(1);
		schdlExcutor.scheduleAtFixedRate(new InvalidateOutOfDateCacheSchedule(), 0, expInterval, TimeUnit.SECONDS);
	}

	public boolean put(String key, Object value) {
		this.getCache(key).put(key, value);
		expCache.put(key, Long.valueOf(-1));
		return true;
	}

	public Object get(String key) {
		Object ret = this.getCache(key).get(key);
		return ret;
	}

	public Object remove(String key) {
		Object ret = this.getCache(key).remove(key);
		expCache.remove(key);
		return ret;
	}

	public boolean put(String key, Object value, Date expiry) {
		this.getCache(key).put(key, value);
		expCache.put(key, expiry.getTime());
		return true;
	}
	
	public ConcurrentHashMap<String, Object> getCache(String key){
		long hashcode = key.hashCode();
		if(hashcode<0)
			hashcode = -hashcode;
		return this.caches[(int) (hashcode%cacheSize)];
	}

	public boolean clear() {
		if(caches != null){
			for(ConcurrentHashMap<String, Object> c : caches){
				c.clear();
			}
			expCache.clear();
		}
		return true;
	}
	
	private class InvalidateOutOfDateCacheSchedule implements java.lang.Runnable{
		
		public void run() {
			checkAndInvalidate();
			
		}
		
		public InvalidateOutOfDateCacheSchedule() {
		}
		
		private void checkAndInvalidate(){
			try {
				Iterator<String> keys = expCache.keySet().iterator();
				Date curr = new Date();
				
				while(keys.hasNext()){
					String key = keys.next();
					Long expDate = expCache.get(key);
					if(expDate<0)
						continue;
					if(curr.after(new Date(expDate))){
						keys.remove();
						getCache(key).remove(key);
					}
				}
				
			} catch (Exception e) {
				logger.error("invalidate failed", e);
			}
		}
		
	}

	public void destroy() {
		try {
			this.clear();
			if(this.schdlExcutor != null){
				this.schdlExcutor.shutdown();
			}
			this.schdlExcutor = null;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
