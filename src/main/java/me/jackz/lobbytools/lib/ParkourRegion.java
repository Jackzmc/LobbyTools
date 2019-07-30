package me.jackz.lobbytools.lib;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class ParkourRegion {
    private Location spawn_point;
    private String name;
    private int y = 0;
    private String fail_message;
    private int max_tries = -1;

    //possibly add new class 'ParkourRegionCheckpoint'
    //private HashMap<UUID,Location>
    /*
        HashMap<UUID,CheckpointName> player -> checkpoint
        HashMap<CheckpointName,Location> (int?)

     */

    private HashMap<UUID, Integer> current_checkpoints = new HashMap<>();
    private List<Location> checkpoints = new ArrayList<>();
    private HashMap<UUID, Integer> user_tries = new HashMap<>();
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
    //possibly merge spawnpoint with checkpoint - as id #0
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
    public int nextCheckpoint(Player p ) {
    	//i think this triggers multiple times:
	    //todo: fix duplicate method firing
        //Integer player_current = current_checkpoints.get(p.getUniqueId());
        //if(player_current == null) player_current = 0;
        //player_current++;
        int pl_x = p.getLocation().getBlockX();
        int pl_y = p.getLocation().getBlockY();
        int pl_z = p.getLocation().getBlockZ();
        for (int i = 0; i < getCheckpoints().size(); i++) {
            double x = checkpoints.get(i).getBlockX();
            double y = checkpoints.get(i).getBlockY();
            double z = checkpoints.get(i).getBlockZ();
            if(pl_x == x && pl_y == y && pl_z == z) {
                current_checkpoints.put(p.getUniqueId(),i);
                return i;
            }
        }
        //checkpoint 1 -> 0
        //size -> 1
//        if(checkpoints.size() >= player_current) {
//            current_checkpoints.put(p.getUniqueId(),player_current);
//        }else{
//            //todo: end logic
//        }
        return -1;
    }
    public void respawnPlayer(Player p) {
        Integer checkpoint = current_checkpoints.get(p.getUniqueId());
        if(max_tries != -1) {
            Integer tries = user_tries.get(p.getUniqueId());
            if(tries != null) {
                user_tries.put(p.getUniqueId(),++tries);
            }else{
                user_tries.put(p.getUniqueId(),1);
                tries = 1;
            }
            if(tries > max_tries) {
                resetCheckpoints(p);
                if(hasFailMessage()) {
                    p.sendMessage(fail_message);
                }
            }
        }
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
    public void resetCheckpoints(Player p ) {
        current_checkpoints.remove(p.getUniqueId());
    }

    public int getMaxTries() {
        return max_tries;
    }

    public void setMaxTries(int max_tries) {
        this.max_tries = max_tries;
    }
}
