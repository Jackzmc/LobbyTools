package me.jackz.lobbytools;

import me.jackz.lobbytools.lib.LanguageManager;
import me.jackz.lobbytools.lib.ParkourRegionManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private static InventoryEvents inventoryEvents;
    private static PlayerEvents playerEvents;
    private static ParkourRegionManager parkourRegionManager;
    private static LanguageManager lm;


    private static Economy ECONOMY = null;

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
        Setup.setupConfig(this);
        db = Setup.setupData(this);
        //Only set to replace for now, while in development:
        //Save the messages.yml into the plugin file
        saveResource("messages.yml",true);

        //get the main plugin world
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        if(world == null) world = Bukkit.getWorld("world");

        //load managers & register events & commands
        loadManagersAndEvents();
        getCommand("lobbytools").setExecutor(new Commands(this));

        //noinspection ConstantConditions
        //register for bungeecoord channel, for server changing
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        //check for plugins w/ warnings on fail
        if(!isPluginEnabled("TTA")) getLogger().warning("TTA not found, join action bar messages will be disabled");
        if(!isPluginEnabled("WorldGuard")) getLogger().warning("WorldGuard not found, parkour regions feature will be disabled");
        if (!setupEconomy() ) getLogger().warning("Vault not found, economy features will be disabled");
    }
    void reloadPlugin() {
        String config_world = getConfig().getString("lobby_world");
        if(config_world != null) world = Bukkit.getWorld(config_world);
        inventoryEvents.updateInventories();
        //playerEvents.updateRegions();
        lm.loadMessages();
        parkourRegionManager.loadRegions();
    }
    @Override
    public void onDisable() {
        //save data in classes
        parkourRegionManager.saveRegions();
		playerEvents.reloadVariables();
        //unload variables, probably not needed
        lm = null;
        inventoryEvents = null;
        playerEvents = null;
        world = null;
        db = null;
        parkourRegionManager = null;
        // Plugin shutdown logic
    }

    private boolean setupEconomy()
    {
        try {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                ECONOMY = economyProvider.getProvider();
            }
            return (ECONOMY != null);
        }catch(NoClassDefFoundError ignored) {
            return false;
        }
    }
    private static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
    private void loadManagersAndEvents() {
        lm = new LanguageManager(this);
        parkourRegionManager = new ParkourRegionManager(this);
        inventoryEvents = new InventoryEvents(this);
        playerEvents = new PlayerEvents(this);
        registerEvents(this,
                new JoinEvent(this),
                new EntityDamage(this),
                inventoryEvents,
                playerEvents
        );
    }
    boolean isPluginEnabled(String plugin_name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(plugin_name);
        return plugin != null && plugin.isEnabled();
    }


    //getters
    public FileConfiguration getData() {
        return db;
    }
    ParkourRegionManager getParkourRegionManager() {
        return parkourRegionManager;
    }
    LanguageManager getLanguageManager() {
        return lm;
    }
    static Economy getECONOMY() { return ECONOMY; }


}
