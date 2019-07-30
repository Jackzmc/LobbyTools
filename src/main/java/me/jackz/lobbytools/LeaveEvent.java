package me.jackz.lobbytools;

import me.jackz.lobbytools.lib.ParkourRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {
    private final Main plugin;

    LeaveEvent(Main plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        handleDisconnect(p);
    }
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        handleDisconnect(p);
    }

    private void handleDisconnect(Player p) {
        if(p.getWorld().equals(Main.world)) {
            for(ParkourRegion region : plugin.getParkourRegionManager().getRegions()) {
                region.resetCheckpoints(p);
            }
        }
    }
}
