package com.theuniverscraft.PointsManager.Managers;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.configuration.file.FileConfiguration;

import com.theuniverscraft.PointsManager.PluginMain;

public class YamlConfig {
	private FileConfiguration config;
	private HashMap<String, String> values = new HashMap<String, String>();
	
	private static YamlConfig instance;
	public static YamlConfig getInstance() {
		if(instance == null) instance = new YamlConfig();
		return instance;
	}
	
	private YamlConfig() {
		config = PluginMain.getInstance().getConfig();
		
		HashMap<String, String> default_values = new HashMap<String, String>();
		default_values.put("mysql.host", "127.0.0.1");
		default_values.put("mysql.port", "3306");
		default_values.put("mysql.name", "asuername");
		default_values.put("mysql.user", "auser");
		default_values.put("mysql.password", "");
		default_values.put("mysql.table", "points");
		default_values.put("mysql.champ_pseudo", "pseudo");
		default_values.put("mysql.champ_points", "points");
		
		Iterator<String> it = default_values.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			String value = default_values.get(key);
			
			if(config.contains(key)) {
				values.put(key, config.getString(key));
			}
			else {
				values.put(key, value);
				config.set(key, value);
			}
		}
		PluginMain.getInstance().saveConfig();
	}
	
	public String getValue(String key) {
		return values.containsKey(key) ? values.get(key) : null;
	}
}
