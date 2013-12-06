package com.genscript.cache;

public interface IMemCachedCache extends ICache<String, Object>{

	public void put(String key, Object value);

	public Object get(String key);

	public Object remove(String key);
}
