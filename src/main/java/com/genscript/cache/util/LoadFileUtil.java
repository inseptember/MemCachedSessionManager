package com.genscript.cache.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.genscript.cache.memcached.config.ClientClusterConfig;
import com.genscript.cache.memcached.config.ClientConfig;
import com.genscript.cache.memcached.config.ClientSocketPoolConfig;

public class LoadFileUtil {
	
	public static void transferConfigByFile(SAXReader reader ,URL url, 
			List<ClientSocketPoolConfig> poolConfigs, List<ClientClusterConfig> clusterConfigs, List<ClientConfig> clientConfigs){
		try {
			Element root = reader.read(url).getRootElement();
			
			List<Element> elements = root.elements();
			
			for(Element ele : elements){
				ClientSocketPoolConfig poolConfig = null;
				ClientConfig clientConfig = null;
				ClientClusterConfig clusterConfig = null;
				if("pool".equals(ele.getName())){
					poolConfig = new ClientSocketPoolConfig();
					poolConfig.setName((String) getObject(poolConfig.getName(), ele.attributeValue("name")));
					poolConfig.setInitConn((Integer) getObject(poolConfig.getInitConn(), ele.attributeValue("initConn")));
					poolConfig.setMinConn((Integer) getObject(poolConfig.getMinConn(), ele.attributeValue("minConn")));
					poolConfig.setMaxConn((Integer) getObject(poolConfig.getMaxConn(), ele.attributeValue("maxConn")));
					poolConfig.setMaxIdle((Integer) getObject(poolConfig.getMaxIdle(), ele.attributeValue("maxIdle")));
					poolConfig.setMaintSleep((Integer) getObject(poolConfig.getMaintSleep(), ele.attributeValue("maintSleep")));
					poolConfig.setNagle((Boolean)getObject(poolConfig.isNagle(), ele.attributeValue("nagle")));
					poolConfigs.add(poolConfig);
				}else if("client".equals(ele.getName())){
					clientConfig = new ClientConfig();
					clientConfig.setName(ele.attributeValue("name"));
					clientConfig.setPoolName((String) getObject(clientConfig.getPoolName(), ele.attributeValue("pool")));
					clientConfig.setDefaultEncoding((String) getObject(clientConfig.getDefaultEncoding(), ele.attributeValue("defaultEncoding")));
					clientConfig.setTransCode((String) getObject(clientConfig.getTransCode(), ele.attributeValue("transCode")));
					clientConfig.setErrorHandler((String) getObject(clientConfig.getErrorHandler(), ele.attributeValue("errorHandler")));
					clientConfigs.add(clientConfig);
				}else if("cluster".equals(ele.getName())){
					clusterConfig = new ClientClusterConfig();
					clusterConfig.setName(ele.attributeValue("name"));
					clusterConfig.setMode((String) getObject(clusterConfig.getMode(), ele.attributeValue("mode")));
					clusterConfigs.add(clusterConfig);
				}
				
				List<Element> children = ele.elements();
				StringBuilder servers = new StringBuilder(), weights = new StringBuilder(), clients = new StringBuilder();
				for(Element child : children){
					if("server".equals(child.getName())){
						servers.append(child.getText()).append(",");
						weights.append(child.attributeValue("weight")).append(",");
					}else if("clients".equals(child.getName())){
						clients.append(child.getText()).append(",");
					}
				}
				poolConfig.setServices(servers.subSequence(0, servers.length() - 1).toString().split(","));
				poolConfig.setWeights(weights.subSequence(0, weights.length() - 1).toString().split(","));
				clusterConfig.setClients(clients.subSequence(0, clients.length() - 1).toString().split(","));
			}
			
		} catch (Exception e) {
			throw new RuntimeException("MemcachedManager loadConfig error !");
		}
	}
	
	public static Object getObject(Object def, String target){
		Object ret = def;
		try {
			if(def instanceof Integer){
				ret = Integer.valueOf(target);
			}else if(def instanceof Boolean){
				ret = Boolean.valueOf(target);
			}else{
				ret = target;
			}
		} catch (Exception e) {
		}
		return ret;
	}
	
}
