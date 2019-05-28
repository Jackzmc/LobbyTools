package me.jackz.lobbytools.lib;

import me.jackz.lobbytools.Main;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParkourRegionManager {
    private final Main plugin;
    private List<ParkourRegion> regions = new ArrayList<>();
    public ParkourRegionManager(Main plugin) {
        this.plugin = plugin;
        loadRegions();
    }


    public List<ParkourRegion> getRegions() {
        return regions;
    }

    public void addRegion(ParkourRegion region) {
        //if(findRegion(region.getName()) != null) //todo:         //only add new regions
        this.regions.add(region);
    }
    public void removeRegion(ParkourRegion region) {
        this.regions.remove(region);
    }
    public void removeRegion(String name) {
        for (int i = 0; i < regions.size(); i++) {
            if(regions.get(i).getName().equals(name)) {
                regions.remove(i);
                break;
            }
        }
    }
    private ParkourRegion findRegion(String name) {
        for (ParkourRegion region : regions) {
            if(region.getName().equalsIgnoreCase(name)) {
                return region;
            }
        }
        return null;
    }

    public void saveRegions() {
        FileConfiguration db = plugin.getData();
        for (ParkourRegion region : regions) {
            String section = "parkour_regions." + region.getName() + ".";
            db.set(section + "spawnpoint.x",region.getSpawnPoint().getX());
            db.set(section + "spawnpoint.y",region.getSpawnPoint().getY());
            db.set(section + "spawnpoint.z",region.getSpawnPoint().getZ());
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
    public void loadRegions() {
        regions.clear();
        ConfigurationSection parkours = plugin.getData().getConfigurationSection("parkour_regions");
        if(parkours == null) return;
        for (String key : parkours.getKeys(false)) {
            Location loc = new Location(Main.world,parkours.getDouble("spawnpoint.X"),parkours.getDouble("spawnpoint.Y"),parkours.getDouble("spawnpoint.Z"));
            ParkourRegion region = new ParkourRegion(key,loc);
            region.setMinY(parkours.getInt("min_y"));

            String fail_message = parkours.getString("fail_message");
            region.setFailMessage(fail_message);
            regions.add(region);
        }
    }
}
