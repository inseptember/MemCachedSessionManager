package com.genscript.cache.memcached.config;

public class ClientConfig {
	
	private String name;
	
	private String defaultEncoding = "UTF-8";
	
	private String poolName = "default";
	
	private String transCode = "com.genscript.cache.util.KryoObjectTransCoder";
	
	private String errorHandler = "com.genscript.cache.memcached.MemCachedErrorHandler";

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
	 * @return the defaultEncoding
	 */
	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	/**
	 * @param defaultEncoding the defaultEncoding to set
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * @return the poolName
	 */
	public String getPoolName() {
		return poolName;
	}

	/**
	 * @param poolName the poolName to set
	 */
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	/**
	 * @return the transCode
	 */
	public String getTransCode() {
		return transCode;
	}

	/**
	 * @param transCode the transCode to set
	 */
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	/**
	 * @return the errorHandler
	 */
	public String getErrorHandler() {
		return errorHandler;
	}

	/**
	 * @param errorHandler the errorHandler to set
	 */
	public void setErrorHandler(String errorHandler) {
		this.errorHandler = errorHandler;
	}

}
