package me.jackz.lobbytools.lib;

import me.jackz.lobbytools.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class LanguageManager {
    private final Main plugin;
    private final File file;
    private final HashMap<String,String> map = new HashMap<>();

    public LanguageManager(Main plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(),"messages.yml");
        loadMessages();
    }
    //todo: allow string.format()?
    public String getMessage(String name, Object... arguments) {
        return String.format(map.get(name),arguments);
    }
    public String getMessage(String name) {
        return map.get(name);
    }
    public String get(String name) {
        return map.get(name);
    }
    public void loadMessages() {
        FileConfiguration messages = YamlConfiguration.loadConfiguration(file);
        for (String key : messages.getKeys(true)) {
            plugin.getLogger().info("KEY: " + key);
            String msg_raw = messages.getString(key);
            if(msg_raw == null) continue;
            String msg = ChatColor.translateAlternateColorCodes('&',msg_raw);
            map.put(key, msg);
        }
    }
}
