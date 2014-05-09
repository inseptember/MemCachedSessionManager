package com.genscript.cache;


public interface IClientManager <T extends ICache<?, ?>> {
	
	public void setConfigFile(String fileName);
	
	public void loadConfigFile(String fileName);
	
	public void loadConfigFile();
	
	public void startUp();
	
	public void shutDown();
	
	public T getCache(String name);

}
