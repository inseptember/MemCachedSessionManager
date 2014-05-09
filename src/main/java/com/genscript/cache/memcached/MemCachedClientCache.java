package com.genscript.cache.memcached;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genscript.cache.ICache;
import com.genscript.cache.IMemCachedCache;
import com.genscript.cache.local.LocalCacheImpl;
import com.schooner.MemCached.MemcachedItem;
import com.whalin.MemCached.MemCachedClient;

public class MemCachedClientCache implements IMemCachedCache {
	
	Logger logger = LoggerFactory.getLogger(MemCachedClientCache.class);
	
	private ICache<String, Object> localCache;
	
	private MemCachedClient client;
	
	
	MemCachedClientCache(MemCachedClient client) {
		this.client = client;
		this.localCache = new LocalCacheImpl();
	}

	public boolean put(String key, Object value) {
		return this.put(key, value, null);
	}
	
	public boolean put(String key, Object value, long expiry) {
		return this.put(key, value, new Date(expiry));
	}
	
	public boolean put(String key, Object value, Date expiry) {
		boolean result = getClient().set(key, value, expiry);
		if(result){
			this.localCache.remove(key);
		}else{
			logger.error("put key: {0} error.",key);
		}
		return result;
	}

	public Object remove(String key) {
		Object ret = getClient().get(key);
		boolean result = getClient().delete(key);
		if(result){
			this.localCache.remove(key);
		}else{
			logger.error("remove key: {0} error.",key);
		}
		return ret;
	}
	
	public MemCachedClient getClient() {
		return client;
	}

	public boolean containsKey(String key) {
		return getClient().keyExists(key);
	}

	public Map<String, Object> getMulti(String[] keys) {
		return getClient().getMulti(keys);
	}

	public boolean add(String key, Object value) {
		return getClient().add(key, value);
	}

	public boolean add(String key, Object value, long expiry) {
		return getClient().add(key, value, new Date(expiry));
	}

	public boolean replace(String key, Object value) {
		return getClient().replace(key, value);
	}

	public boolean replace(String key, Object value, long expiry) {
		return getClient().replace(key, value, new Date(expiry));
	}

	public Object[] getMultiArray(String[] keys) {
		return getClient().getMultiArray(keys);
	}

	public boolean storeCounter(String key, long counter) {
		return getClient().storeCounter(key, counter);
	}

	public long getCounter(String key) {
		return getClient().getCounter(key);
	}

	public long decr(String key) {
		return getClient().decr(key);
	}

	public long incr(String key) {
		return getClient().incr(key);
	}

	public long addOrDecr(String key) {
		return getClient().addOrDecr(key);
	}

	public long addOrIncr(String key) {
		return getClient().addOrIncr(key);
	}

	public MemcachedItem gets(String key) {
		return this.getClient().gets(key);
	}

	public boolean cas(String key, Object value, long expiry, long casUnique) {
		return getClient().cas(key, value, new Date(expiry), casUnique);
	}

	public Map<String, Map<String, String>> stats() {
		return getClient().stats();
	}

	public Map<String, Map<String, String>> statsItems() {
		return getClient().statsItems();
	}

	public Map<String, Map<String, String>> statsSlabs() {
		return getClient().statsSlabs();
	}

	public boolean clear() {
		this.localCache.clear();
		return this.getClient().flushAll();
	}
	
	public Object get(String key) {
		return getClient().get(key);
	}

	public Object get(String key, int localExpiry) {
		Object result = this.localCache.get(key);
		if(result == null){
			result = this.get(key);
			if(result != null){
				Calendar c = Calendar.getInstance();
				c.set(Calendar.SECOND, localExpiry);
				localCache.put(key, result, c.getTime());
			}
		}
		return result;
	}

	public void destroy() {
		this.localCache.destroy();
	}

}
