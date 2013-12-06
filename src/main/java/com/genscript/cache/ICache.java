package com.genscript.cache;


public interface ICache <K, V> {

	public void put(K key, V value);
	
	public V get(K key);
	
	public V remove(K key);
	
}
