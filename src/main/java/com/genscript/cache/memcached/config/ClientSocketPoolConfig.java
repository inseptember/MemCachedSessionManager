package com.genscript.cache.memcached.config;

public class ClientSocketPoolConfig {
	
	private String name = "default";
	
	/**
	 * 缓存服务器IP:Port地址，以逗号分割
	 */
	private String[]services;
	
	/**
	 * 权重
	 */
	private String[] weights;
	
	/**
	 * 每台服务器的初始连接数
	 */
	private Integer initConn = 10;
	
	private Integer minConn = 5;
	
	private Integer maxConn = 250;
	
	/**
	 * 每个连接的最大空闲时间
	 */
	private Integer maxIdle = 1000 * 3;
	
	/**
	 * 每maintSleep毫秒检查pool的连接数，并维持
	 */
	private Integer maintSleep = 1000 * 3;
	
	/**
	 * 是否关闭Socket缓存
	 */
	private Boolean nagle = false;
	
	/**
	 * Socket超时时间
	 */
	private Integer socketTO = 1000 * 3;
	
	/**
	 * Socket连接超时时间
	 */
	private Integer socketConnetTO = 0;

	/**
	 * @return the services
	 */
	public String[] getServices() {
		return services;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(String[] services) {
		this.services = services;
	}

	/**
	 * @return the weights
	 */
	public String[] getWeights() {
		return weights;
	}

	/**
	 * @param weights the weights to set
	 */
	public void setWeights(String[] weights) {
		this.weights = weights;
	}

	/**
	 * @return the initConn
	 */
	public Integer getInitConn() {
		return initConn;
	}

	/**
	 * @param initConn the initConn to set
	 */
	public void setInitConn(Integer initConn) {
		this.initConn = initConn;
	}

	/**
	 * @return the minConn
	 */
	public Integer getMinConn() {
		return minConn;
	}

	/**
	 * @param minConn the minConn to set
	 */
	public void setMinConn(Integer minConn) {
		this.minConn = minConn;
	}

	/**
	 * @return the maxConn
	 */
	public Integer getMaxConn() {
		return maxConn;
	}

	/**
	 * @param maxConn the maxConn to set
	 */
	public void setMaxConn(Integer maxConn) {
		this.maxConn = maxConn;
	}

	/**
	 * @return the maxIdle
	 */
	public Integer getMaxIdle() {
		return maxIdle;
	}

	/**
	 * @param maxIdle the maxIdle to set
	 */
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	/**
	 * @return the maIntegerSleep
	 */
	public Integer getMaintSleep() {
		return maintSleep;
	}

	/**
	 * @param maIntegerSleep the maIntegerSleep to set
	 */
	public void setMaintSleep(Integer maintSleep) {
		this.maintSleep = maintSleep;
	}

	/**
	 * @return the nagle
	 */
	public Boolean isNagle() {
		return nagle;
	}

	/**
	 * @param nagle the nagle to set
	 */
	public void setNagle(Boolean nagle) {
		this.nagle = nagle;
	}

	/**
	 * @return the socketTO
	 */
	public Integer getSocketTO() {
		return socketTO;
	}

	/**
	 * @param socketTO the socketTO to set
	 */
	public void setSocketTO(Integer socketTO) {
		this.socketTO = socketTO;
	}

	/**
	 * @return the socketConnetTO
	 */
	public Integer getSocketConnetTO() {
		return socketConnetTO;
	}

	/**
	 * @param socketConnetTO the socketConnetTO to set
	 */
	public void setSocketConnetTO(Integer socketConnetTO) {
		this.socketConnetTO = socketConnetTO;
	}

}
