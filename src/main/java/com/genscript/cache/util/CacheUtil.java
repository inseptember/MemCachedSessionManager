package com.genscript.cache.util;

import com.genscript.cache.ICache;
import com.genscript.cache.IClientManager;

public class CacheUtil {
	
	@SuppressWarnings({ "unchecked" })
	private static <I> I getInstance(String className, ClassLoader loader){
		Class<I> ret;
		try {
			if(loader == null){
				ret = (Class<I>) Class.forName(className);
			}else{
				ret = (Class<I>) loader.getClass();
			}
			return ret.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("get manager instance failed", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <B extends ICache<?, ?>> IClientManager<B> getCacheManager(String className){
		return (IClientManager<B>)getInstance(className, null);
	}

}
