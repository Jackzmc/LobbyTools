package me.jackz.lobbytools.lib;

import me.jackz.lobbytools.Main;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** A manager to create parkour regions and save/load them with ease
 *
 */
public class ParkourRegionManager {
    private final Main plugin;
    private List<ParkourRegion> regions = new ArrayList<>();

    /**
     * @param plugin The plugin main class
     */
    public ParkourRegionManager(Main plugin) {
        this.plugin = plugin;
        loadRegions();
    }

    /**
     * @return List of ParkourRegions
     */
    public List<ParkourRegion> getRegions() {
        return regions;
    }

    /** Adds a ParkourRegion to the list
     * @param region The ParkourRegion
     */
    public void addRegion(ParkourRegion region) {
        //if(findRegion(region.getName()) != null) //todo:         //only add new regions
        this.regions.add(region);
    }

    /** Removes a region by class
     * @param region The ParkourRegion class
     */
    public void removeRegion(ParkourRegion region) {
        this.regions.remove(region);
    }

    /** Removes a region by name
     * @param name The case-insensitive name of a region
     * @return Returns true if successfully removed
     */
    public boolean removeRegion(String name) {
        for (int i = 0; i < regions.size(); i++) {
            if(regions.get(i).getName().equalsIgnoreCase(name)) {
                plugin.getLogger().info("pre: " + regions.size());
                regions.remove(i);
                plugin.getLogger().info("post: " + regions.size());
                saveRegions(); //for now, save just incase
                return true;
            }
        }
        return false;
    }
    private ParkourRegion findRegion(String name) {
        for (ParkourRegion region : regions) {
            if(region.getName().equalsIgnoreCase(name)) {
                return region;
            }
        }
        return null;
    }

    /** Saves the regions to data.yml file
     *
     */
    public void saveRegions() {
        FileConfiguration db = plugin.getData();
        db.set("parkour_regions",new ArrayList<String>());
        for (ParkourRegion region : regions) {
            String section = "parkour_regions." + region.getName() + ".";
            Location spawnpoint = region.getSpawnPoint();
            db.set(section + "spawnpoint.x",spawnpoint.getX());
            db.set(section + "spawnpoint.y",spawnpoint.getY());
            db.set(section + "spawnpoint.z",spawnpoint.getZ());
            db.set(section + "spawnpoint.yaw",(double)spawnpoint.getYaw());
            db.set(section + "min_y",region.getMinY());
            db.set(section + "fail_message",region.getFailMessage());
        }
        plugin.getLogger().info("Saving " + regions.size() + " regions");
        try {
            db.save(new File(plugin.getDataFolder(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Loads the regions from the data.yml to the list
     *
     */
    public void loadRegions() {
        regions.clear();
        ConfigurationSection parkours = plugin.getData().getConfigurationSection("parkour_regions");
        if(parkours == null) return;
        for (String key : parkours.getKeys(false)) {
            plugin.getLogger().info("f: " + parkours.getDouble(key+".spawnpoint.yaw"));
            Location loc = new Location(Main.world,parkours.getDouble(key + ".spawnpoint.x"),parkours.getDouble(key+".spawnpoint.y"),parkours.getDouble(key+".spawnpoint.z"));
            loc.setYaw((float) parkours.getDouble(key+".spawnpoint.yaw"));
            ParkourRegion region = new ParkourRegion(key,loc);
            region.setMinY(parkours.getInt(key+".min_y"));

            String fail_message = parkours.getString(key+".fail_message");
            region.setFailMessage(fail_message);
            regions.add(region);
        }
    }
}
