package me.jackz.lobbytools;

import me.jackz.lobbytools.lib.LanguageManager;
import me.jackz.lobbytools.lib.ParkourRegionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private static InventoryEvents inventoryEvents;
    private static PlayerEvents playerEvents;
    private static ParkourRegionManager parkourRegionManager;
    private static LanguageManager lm;

    HashMap<UUID, Boolean> hidden_map = new HashMap<>();
    public static World world;
    static File CONFIG_FILE;

    private FileConfiguration db;

    /*todo:
     parkour regions
     player hider
     gadgets

     */

    @Override
    public void onEnable() {
        // Plugin startup logic
        setupConfig();
        setupData();
        //Only set to replace for now, while in development:
        saveResource("messages.yml",true);
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        if(world == null) world = Bukkit.getWorld("world");


        lm = new LanguageManager(this);
        parkourRegionManager = new ParkourRegionManager(this);
        inventoryEvents = new InventoryEvents(this);
        playerEvents = new PlayerEvents(this);
        CONFIG_FILE = new File (this.getDataFolder(), "config.yml");
        getServer().getPluginManager().registerEvents(new JoinEvent(this),this);
        getServer().getPluginManager().registerEvents(inventoryEvents,this);
        getServer().getPluginManager().registerEvents(new EntityDamage(this),this);
        getServer().getPluginManager().registerEvents(playerEvents,this);
        getCommand("lobbytools").setExecutor(new Commands(this));

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        parkourRegionManager.saveRegions();

        lm = null;
        inventoryEvents = null;
        playerEvents = null;
        world = null;
        db = null;
        parkourRegionManager = null;
        // Plugin shutdown logic
    }
    void reloadPlugin() {
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        inventoryEvents.updateServers();
        //playerEvents.updateRegions();
        lm.loadMessages();
        parkourRegionManager.loadRegions();
    }

    private void setupData() {
        File file = new File(this.getDataFolder(), "data.yml");
        YamlConfiguration db = YamlConfiguration.loadConfiguration(file);
        db.addDefault("parkour_regions",new ArrayList<String>());
        this.db = db;
        try {
            db.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getData() {
        return db;
    }
    public ParkourRegionManager getParkourRegionManager() {
        return parkourRegionManager;
    }
    public LanguageManager getLanguageManager() {
        return lm;
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
