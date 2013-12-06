package com.genscript.cache.memcached;

import java.util.List;

import com.genscript.cache.IMemCachedCache;

public class MemCachedClientCluster {
	
	private String name;
	
	private List<IMemCachedCache> caches;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the caches
	 */
	public List<IMemCachedCache> getCaches() {
		return caches;
	}

	/**
	 * @param caches the caches to set
	 */
	public void setCaches(List<IMemCachedCache> caches) {
		this.caches = caches;
	}

}
