package me.jackz.lobbytools.utils;

import me.jackz.lobbytools.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;

public final class LanguageManager {
    private final Main plugin;
    private final File file;
    private final HashMap<String,String> map = new HashMap<>();

    public LanguageManager(Main plugin) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(),"messages.yml");
        loadMessages();
    }

    //todo: allow string.format()?
    @Deprecated
    public String getCommand(String name, Object... arguments) {
        return String.format(map.get("command." + name),arguments);
    }
    @Deprecated
    public String get(String name,Object... arguments) {
        return String.format(map.get(name),arguments);
    }
    public void send(Player p, String name, Object... arguments) {
        String msg = map.get(name); //todo: add null check
        p.sendMessage(String.format(msg,arguments));
    }
    public void sendCommand(Player p, String name, Object... arguments) {
        String msg = map.get("command." + name); //todo: add null check
        p.sendMessage(String.format(msg,arguments));
    }
    public void sendCommand(CommandSender sender, String name, Object... arguments) {
        String msg = map.get("command." + name); //todo: add null check
        sender.sendMessage(String.format(msg,arguments));
    }
    public void loadMessages() {
        FileConfiguration messages = YamlConfiguration.loadConfiguration(file);
        for (String key : messages.getKeys(true)) {
            String message = null;
            if(messages.isList(key)) {
                message = StringUtils.join(messages.getList(key), '\n');
            }else{
                message = messages.getString(key);
            }
            if(message == null) continue;
            String msg = ChatColor.translateAlternateColorCodes('&',message);
            map.put(key, msg);
        }
    }
}
