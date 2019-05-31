package me.jackz.lobbytools.lib;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<Location> checkpoints = new ArrayList<>();
    //private HashMap<Integer,Location> checkpoints = new HashMap<>();

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

    public List<Location> getCheckpoints() {
        return checkpoints;
    }

   public void setCheckpoints(List<Location> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public int addCheckpoint(Location loc) {
        int nextid = checkpoints.size();
        checkpoints.add(nextid,loc);
	    return nextid;
    }
    public void removeCheckpoint(int id) {
        checkpoints.remove(id);
    }
    public void nextCheckpoint(Player p ) {
    	//i think this triggers multiple times:
	    //todo: fix duplicate method firing
        Integer player_current = current_checkpoints.get(p.getUniqueId());
        if(player_current == null) player_current = 0;
        player_current++;
        //checkpoint 1 -> 0
        //size -> 1
        if(checkpoints.size() >= player_current) {
            current_checkpoints.put(p.getUniqueId(),player_current);
        }else{
            //todo: end logic
        }
    }
    public void respawnPlayer(Player p) {
        Integer checkpoint = current_checkpoints.get(p.getUniqueId());
        if(checkpoint == null) {
            p.teleport(spawn_point, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }else{
            Location checkpoint_location = checkpoints.get(checkpoint);
            if(checkpoint_location == null) {
	            p.sendMessage("checkpoint failed, tp to spawn");
	            p.teleport(spawn_point, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }else{
	            p.teleport(checkpoint_location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        }
    }

    public int getPlayerCheckpoint(Player p) {
    	Integer player_current = current_checkpoints.get(p.getUniqueId());
    	p.sendMessage("raw: " + player_current);
    	if(player_current == null) {
    		return 0;
	    }else{
    		return ++player_current;
	    }
    }
}
