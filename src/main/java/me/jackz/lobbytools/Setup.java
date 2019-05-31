package me.jackz.lobbytools;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Setup {
	static FileConfiguration setupData(Main plugin) {
		File file = new File(plugin.getDataFolder(), "data.yml");
		YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
		db.addDefault("parkour_regions",new ArrayList<String>());
		try {
			db.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return db;
	}

	 static void setupConfig(Main plugin) {
		File configFile = new File (plugin.getDataFolder(), "config.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		config.addDefault("lobby_world", "world");
		config.addDefault("servers",new ArrayList<String>());
		config.addDefault("launchpad_enabled",true);
		config.addDefault("y_spawn_teleport",0);
		config.addDefault("parkour_regions",new ArrayList<String>());
		config.addDefault("messages.title","Welcome to the Server!");
		config.addDefault("messages.subtitle","");
		config.addDefault("messages.actionbar","");
		config.options().copyDefaults(true);
		try {
			config.save(configFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
