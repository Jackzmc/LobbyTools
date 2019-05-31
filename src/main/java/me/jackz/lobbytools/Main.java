package me.jackz.lobbytools;

import me.jackz.lobbytools.lib.LanguageManager;
import me.jackz.lobbytools.lib.ParkourRegionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
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
     gadgets

     */

    @Override
    public void onEnable() {
        // setup config and data yml files
        CONFIG_FILE = new File (this.getDataFolder(), "config.yml");
        setupConfig();
        setupData();
        //Only set to replace for now, while in development:
        //Save the messages.yml into the plugin file
        saveResource("messages.yml",true);

        //get the main plugin world
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        if(world == null) world = Bukkit.getWorld("world");

        //load managers
        loadManagersAndEvents();

        //register events
        getServer().getPluginManager().registerEvents(new JoinEvent(this),this);
        getServer().getPluginManager().registerEvents(inventoryEvents,this);
        getServer().getPluginManager().registerEvents(new EntityDamage(this),this);
        getServer().getPluginManager().registerEvents(playerEvents,this);

        //register commands
        getCommand("lobbytools").setExecutor(new Commands(this));
        //register for bungeecoord channel, for server changing
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        //check for TTA, and warn player if failed
        if(!isTTAEnabled()) {
            getLogger().warning("TTA not found, join action bar messages will be disabled");

        }
    }
    void reloadPlugin() {
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        inventoryEvents.updateServers();
        //playerEvents.updateRegions();
        lm.loadMessages();
        parkourRegionManager.loadRegions();
    }
    @Override
    public void onDisable() {
        //save data in classes
        parkourRegionManager.saveRegions();

        //unload variables, probably not needed
        lm = null;
        inventoryEvents = null;
        playerEvents = null;
        world = null;
        db = null;
        parkourRegionManager = null;
        // Plugin shutdown logic
    }

    private void loadManagersAndEvents() {
        lm = new LanguageManager(this);
        parkourRegionManager = new ParkourRegionManager(this);
        inventoryEvents = new InventoryEvents(this);
        playerEvents = new PlayerEvents(this);
    }

    boolean isTTAEnabled() {
        Plugin TTA = getServer().getPluginManager().getPlugin("TTA");
        return TTA != null && TTA.isEnabled();
    }

    private void setupConfig() {
        File configFile = new File (this.getDataFolder(), "config.yml");
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
    //getters
    public FileConfiguration getData() {
        return db;
    }
    public ParkourRegionManager getParkourRegionManager() {
        return parkourRegionManager;
    }
    public LanguageManager getLanguageManager() {
        return lm;
    }


}
