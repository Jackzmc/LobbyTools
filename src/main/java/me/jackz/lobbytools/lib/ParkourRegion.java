package me.jackz.lobbytools.lib;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class ParkourRegion {
    private Location spawn_point;
    private String name;
    private int y = 0;
    private String fail_message;

    //possibly add new class 'ParkourRegionCheckpoint'
    //private HashMap<UUID,Location>
    /*
        HashMap<UUID,CheckpointName> player -> checkpoint
        HashMap<CheckpointName,Location> (int?)

     */

    private HashMap<UUID, Integer> current_checkpoints = new HashMap<>();
    private HashMap<Integer,Location> checkpoints = new HashMap<>();

    public ParkourRegion(String name, Location spawn_point) {
        this.setName(name);
        this.setSpawnPoint(spawn_point);
    }
    ParkourRegion(String name, Location spawn_point, int min_y) {
        this.setName(name);
        this.setSpawnPoint(spawn_point);
        this.y = min_y;
    }

    public int getMinY() {
        return y;
    }

    public void setMinY(int y) {
        this.y = y;
    }

    public Location getSpawnPoint() {
        return spawn_point;
    }

    public void setSpawnPoint(Location spawn_point) {
        this.spawn_point = spawn_point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFailMessage() {
        return fail_message;
    }

    public void setFailMessage(String custom_message) {
        this.fail_message = custom_message;
    }
    public boolean hasFailMessage() {
        return (this.fail_message != null);
    }

    public HashMap<Integer, Location> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(HashMap<Integer, Location> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public void addCheckpoint(Location loc) {
        int nextid = checkpoints.size()-1;
        checkpoints.put(nextid,loc);
    }
    public void removeCheckpoint(int id) {
        checkpoints.remove(id);
    }
}
