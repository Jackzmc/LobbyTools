package me.jackz.lobbytools;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class Main extends JavaPlugin {
    private static InventoryEvents inventoryEvents;
    static World world;
    static File CONFIG_FILE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupConfig();
        inventoryEvents = new InventoryEvents(this);
        CONFIG_FILE = new File (this.getDataFolder(), "config.yml");
        getServer().getPluginManager().registerEvents(new JoinEvent(this),this);
        getServer().getPluginManager().registerEvents(inventoryEvents,this);
        getServer().getPluginManager().registerEvents(new EntityDamage(this),this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(this),this);
        getCommand("lobbytools").setExecutor(new Commands(this));
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        inventoryEvents = null;
        world = null;
        // Plugin shutdown logic
    }
    void reloadPlugin() {
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        inventoryEvents.updateServers();
    }


    private void setupConfig() {
        File configFile = new File (this.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        config.addDefault("lobby_world", "world");
        config.addDefault("servers",new ArrayList<String>());
        config.addDefault("launchpad_enabled",true);
        config.addDefault("y_spawn_teleport",0);
        config.addDefault("parkour_regions",new ArrayList<String>());

        config.options().copyDefaults(true);
        try {
            config.save(configFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
