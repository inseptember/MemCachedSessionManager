package com.genscript.cache.memcached.config;

public class ClientClusterConfig {
	
	private static String MODE_ACTIVE = "active";
	private static String MODE_STANDBY = "standby";
	private static String MODE_NONE = "none";
	
	private String name;
	
	private String mode = MODE_ACTIVE;
	
	private String[] clients;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String[] getClients() {
		return clients;
	}

	public void setClients(String[] clients) {
		this.clients = clients;
	}

}
