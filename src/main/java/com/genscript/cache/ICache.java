package com.genscript.cache;

import java.util.Date;


public interface ICache <K, V> {

	public boolean put(K key, V value);
	
	public boolean put(K key, V value, Date expiry);
	
	public V get(K key);
	
	public V remove(K key);
	
	public boolean clear();
	
	public void destroy();
	
}
