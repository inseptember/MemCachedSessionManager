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
			
			List<Element> poolElements = root.elements("pool");
			
			for(Element ele : poolElements){
				ClientSocketPoolConfig config = new ClientSocketPoolConfig();
				
				List<Element> serverElements = ele.elements("server");
				List<String> servers = new ArrayList<String>(), weights = new ArrayList<String>();
				for(Element serverElement : serverElements){
					servers.add(serverElement.getText());
					weights.add(serverElement.attributeValue("weight"));
				}
				config.setServices(servers.toArray(new String[servers.size()]));
				config.setWeights(weights.toArray(new String[weights.size()]));
				
				config.setName((String) getObject(config.getName(), ele.attributeValue("name")));
				config.setInitConn((Integer) getObject(config.getInitConn(), ele.attributeValue("initConn")));
				config.setMinConn((Integer) getObject(config.getMinConn(), ele.attributeValue("minConn")));
				config.setMaxConn((Integer) getObject(config.getMaxConn(), ele.attributeValue("maxConn")));
				config.setMaxIdle((Integer) getObject(config.getMaxIdle(), ele.attributeValue("maxIdle")));
				config.setMaintSleep((Integer) getObject(config.getMaintSleep(), ele.attributeValue("maintSleep")));
				config.setNagle((Boolean)getObject(config.isNagle(), ele.attributeValue("nagle")));
				poolConfigs.add(config);
			}
			
			List<Element> clientElements = root.elements("client");
			for(Element ele : clientElements){
				ClientConfig config = new ClientConfig();
				config.setName((String) getObject(config.getName(), ele.attributeValue("name")));
				config.setPoolName((String) getObject(config.getPoolName(), ele.attributeValue("pool")));
				config.setDefaultEncoding((String) getObject(config.getDefaultEncoding(), ele.attributeValue("defaultEncoding")));
				config.setTransCode((String) getObject(config.getTransCode(), ele.attributeValue("transCode")));
				config.setErrorHandler((String) getObject(config.getErrorHandler(), ele.attributeValue("errorHandler")));
				clientConfigs.add(config);
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
