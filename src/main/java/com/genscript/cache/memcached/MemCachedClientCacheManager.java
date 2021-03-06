package com.genscript.cache.memcached;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.genscript.cache.IClientManager;
import com.genscript.cache.IMemCachedCache;
import com.genscript.cache.memcached.config.ClientClusterConfig;
import com.genscript.cache.memcached.config.ClientConfig;
import com.genscript.cache.memcached.config.ClientSocketPoolConfig;
import com.genscript.cache.util.LoadFileUtil;
import com.schooner.MemCached.MemcachedItem;
import com.schooner.MemCached.TransCoder;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

public class MemCachedClientCacheManager implements IClientManager<IMemCachedCache>{
	
	Logger logger = LoggerFactory.getLogger(MemCachedClientCacheManager.class);
	
	private List<ClientSocketPoolConfig> clientSocketPoolConfigs; 
	private List<ClientClusterConfig> clientClusterConfigs; 
	private List<ClientConfig> clientConfigs;
	private ConcurrentHashMap<String, SockIOPool> socketPools;
	private ConcurrentHashMap<String, IMemCachedCache> cachePools;
	
	public static String MEM_CACHED_CONFIG_FILE = "memcached.xml";
	
	private String configFile;

	public void loadConfigFile(String fileName) {
		clientSocketPoolConfigs = new ArrayList<ClientSocketPoolConfig>();
		clientConfigs = new ArrayList<ClientConfig>();
		clientClusterConfigs = new ArrayList<ClientClusterConfig>();
		cachePools = new ConcurrentHashMap<String, IMemCachedCache>();
		socketPools = new ConcurrentHashMap<String, SockIOPool>();
		
		try {
			SAXReader reader = new SAXReader();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(fileName);
			LoadFileUtil.transferConfigByFile(reader, url, clientSocketPoolConfigs, clientClusterConfigs, clientConfigs);
		} catch (Exception e) {
			logger.error("load config file failed", e);
		}
	}

	protected void initialize() {
		for(ClientSocketPoolConfig poolConfig : clientSocketPoolConfigs){
			SockIOPool pool = SockIOPool.getInstance(poolConfig.getName());
			pool.setServers(poolConfig.getServices());
			pool.setWeights(pool.getWeights());
			pool.setInitConn(poolConfig.getInitConn());
			pool.setMinConn(poolConfig.getMinConn());
			pool.setMaxConn(poolConfig.getMaxConn());
			pool.setMaintSleep(poolConfig.getMaintSleep());
			pool.setMaxIdle(poolConfig.getMaxIdle());
			pool.setSocketTO(poolConfig.getSocketTO());
			pool.setSocketConnectTO(poolConfig.getSocketConnetTO());
			pool.initialize();
			
			socketPools.put(poolConfig.getName(), pool);
			logger.info(new StringBuilder().append(" new socket Pool add :").append(poolConfig.getName()).toString());
		}
		
		for(ClientConfig client : clientConfigs){
			MemCachedClient cacheClient = new MemCachedClient(client.getPoolName(),true, false);
			IMemCachedCache cache = new MemCachedClientCache(cacheClient);
			try {
				cacheClient.setDefaultEncoding(client.getDefaultEncoding());
				cacheClient.setTransCoder((TransCoder) Class.forName(client.getTransCode()).newInstance());
				cacheClient.setSanitizeKeys(false);
				cachePools.put(client.getName(), cache);
			} catch (Exception e) {
				logger.error("initail client failed", e);
			}
		}
		
		for(ClientClusterConfig clusterConfig : clientClusterConfigs){
			MemCachedClientCluster cluster = new MemCachedClientCluster();
			cluster.setName(clusterConfig.getName());
			cluster.setCaches(new ArrayList<IMemCachedCache>());
			
			for(String cacheName : clusterConfig.getClients()){
				cluster.getCaches().add(cachePools.get(cacheName));
				//TODO: 用于集群，暂时懒得写了。。。
			}
		}
	}

	public void shutDown() {
		try {
			if(socketPools!=null && socketPools.size()>0){
				Enumeration<String> keys = socketPools.keys();
				while(keys.hasMoreElements()){
					String key = keys.nextElement();
					SockIOPool pool = socketPools.get(key);
					pool.shutDown();
				}
				socketPools.clear();
			}
		} catch (Exception e) {
			logger.error("shut down error", e);
		}finally{
			if(clientSocketPoolConfigs!=null){
				clientSocketPoolConfigs.clear();
			}
			if(clientConfigs!=null){
				clientConfigs.clear();
			}
			if(clientClusterConfigs!=null){
				clientClusterConfigs.clear();
			}
			if(cachePools!=null){
				cachePools.clear();
			}
		}
		
	}

	public IMemCachedCache getCache(String name) {
		return this.getCachePools().get(name);
	}

	public void setClientSocketPoolConfigs(List<ClientSocketPoolConfig> clientSocketPoolConfigs) {
		this.clientSocketPoolConfigs = clientSocketPoolConfigs;
	}

	public List<ClientSocketPoolConfig> getClientSocketPoolConfigs() {
		return clientSocketPoolConfigs;
	}

	public void loadConfigFile() {
		loadConfigFile(MEM_CACHED_CONFIG_FILE);
	}

	public void startUp() {
		this.loadConfigFile(configFile);
		this.initialize();
	}

	public void setSocketPools(ConcurrentHashMap<String, SockIOPool> socketPools) {
		this.socketPools = socketPools;
	}

	public ConcurrentHashMap<String, SockIOPool> getSocketPools() {
		return socketPools;
	}

	public void setClientClusterConfigs(List<ClientClusterConfig> clientClusterConfigs) {
		this.clientClusterConfigs = clientClusterConfigs;
	}

	public List<ClientClusterConfig> getClientClusterConfigs() {
		return clientClusterConfigs;
	}

	public List<ClientConfig> getClientConfigs() {
		return clientConfigs;
	}

	public void setClientConfigs(List<ClientConfig> clientConfigs) {
		this.clientConfigs = clientConfigs;
	}
	
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	public String getConfigFile() {
		return configFile;
	}

	public void setCachePools(ConcurrentHashMap<String, IMemCachedCache> caches) {
		this.cachePools = caches;
	}

	public ConcurrentHashMap<String, IMemCachedCache> getCachePools() {
		return cachePools;
	}

}
