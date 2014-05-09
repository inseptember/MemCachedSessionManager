package com.genscript.cache.memcached;

import com.whalin.MemCached.ErrorHandler;
import com.whalin.MemCached.MemCachedClient;

public class MemCachedErrorHandler implements ErrorHandler {

	public void handleErrorOnInit(MemCachedClient client, Throwable error) {
		// TODO Auto-generated method stub

	}

	public void handleErrorOnGet(MemCachedClient client, Throwable error,
			String cacheKey) {
		// TODO Auto-generated method stub

	}

	public void handleErrorOnGet(MemCachedClient client, Throwable error,
			String[] cacheKeys) {
		// TODO Auto-generated method stub

	}

	public void handleErrorOnSet(MemCachedClient client, Throwable error,
			String cacheKey) {
		// TODO Auto-generated method stub

	}

	public void handleErrorOnDelete(MemCachedClient client, Throwable error,
			String cacheKey) {
		// TODO Auto-generated method stub

	}

	public void handleErrorOnFlush(MemCachedClient client, Throwable error) {
		// TODO Auto-generated method stub

	}

	public void handleErrorOnStats(MemCachedClient client, Throwable error) {
		// TODO Auto-generated method stub

	}

}
