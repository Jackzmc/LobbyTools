package me.jackz.lobbytools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {
    private final Main plugin;

    JoinEvent(Main plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String config_world = plugin.getConfig().getString("lobby_world");
        if(config_world != null) {
            World world = Bukkit.getWorld(config_world);
            /*if(!p.hasPermission("lobbytools.bypass.lobby")) {*/
                p.getInventory().clear();
                p.getInventory().setItem(1,Util.getNamedItem(Material.CLOCK,"§6Player Hider","§7Toggle hiding players"));
                p.getInventory().setItem(4,Util.getNamedItem(Material.NETHER_STAR,"§6Server Selector","§7Select a server"));
            /*}*/
        }
    }


}
