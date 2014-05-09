package com.genscript.cache;

import java.util.Date;
import java.util.Map;

import com.schooner.MemCached.MemcachedItem;

public interface IMemCachedCache extends ICache<String, Object>{
	
	/**
	 * 新增记录，前提key不存在
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean add(String key, Object value);
	
	/**
	 * 新增记录，key不存在
	 * @param key
	 * @param value
	 * @param expiry 失效时间，单位s
	 * @return
	 */
	public boolean add(String key, Object value, long expiry);
	
	/**
	 * 替换记录，key存在
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean replace(String key, Object value);
	
	/**
	 * 替换记录，key存在
	 * @param key
	 * @param value
	 * @param expiry 失效时间，单位s
	 * @return
	 */
	public boolean replace(String key, Object value, long expiry);

	public boolean put(String key, Object value);

	/**
	 * 添加记录，存在即被替换
	 * @param key
	 * @param value
	 * @param expiry
	 * @return
	 */
	public boolean put(String key, Object value, Date expiry);

	/**
	 * 添加记录，存在即被替换
	 * @param key
	 * @param value
	 * @param expiry
	 * @return
	 */
	public boolean put(String key, Object value, long expiry);
	
	public Object get(String key);
	
	/**
	 * 从本地+Memcached取出数据，减少与Memcached的交互
	 * @param key
	 * @param localExpiry 本地缓存失效时间
	 * @return
	 */
	public Object get(String key, int localExpiry);
	
	/**
	 * 获取多个keys对应的Entry<String, Object>
	 * @param keys
	 * @return
	 */
	public Map<String, Object> getMulti(String[] keys);
	
	/**
	 * 获取多个keys对应的对象数组
	 * @param keys
	 * @return
	 */
	public Object[] getMultiArray(String[] keys);
	
	public Object remove(String key);
	
	/**
	 * 判断是否存在
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key);
	
	/**
	 * 计数器
	 * @param key
	 * @param counter
	 * @return
	 */
	public boolean storeCounter(String key, long counter);
	
	/**
	 * 得到计数结果
	 * @param key
	 * @return 如果找不到，返回-1
	 */
	public long getCounter(String key);
	
	/**
	 * key对应的计数器结果减1
	 * @param key
	 * @return
	 */
	public long decr(String key);
	
	/**
	 * key对应的计数器结果加1
	 * @param key
	 * @return
	 */
	public long incr(String key);
	
	/**
	 * 新增计数器，并-1
	 * @param key
	 * @return
	 */
	public long addOrDecr(String key);
	
	/**
	 * 新增计数器，并+1
	 * @param key
	 * @return
	 */
	public long addOrIncr(String key);
	
	public MemcachedItem gets(String key);
	
	public boolean cas(String key, Object value, long expiry, long casUnique);
	
	public Map<String, Map<String, String>> stats();
	
	public Map<String, Map<String, String>> statsItems();
	
	public Map<String, Map<String, String>> statsSlabs();
	
}
