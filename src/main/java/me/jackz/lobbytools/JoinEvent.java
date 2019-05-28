package me.jackz.lobbytools;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JoinEvent implements Listener {
    private final Main plugin;

    JoinEvent(Main plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(p.getWorld().equals(Main.world)) {
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setItem(1,Util.getNamedItem(Material.CLOCK,"§6Player Hider","§7Toggle hiding players"));
            p.getInventory().setItem(4,Util.getNamedItem(Material.NETHER_STAR,"§6Server Selector","§7Select a server"));
            p.getInventory().setItem(8,Util.getNamedItem(Material.CHEST,"§6Gadgets","§7Fun toys for all"));
            //temp. measure to stop saturation issues:
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999,1,true));
            if(!plugin.hidden_map.containsKey(p.getUniqueId())) plugin.hidden_map.put(p.getUniqueId(),false);
        }
        /*if(!p.hasPermission("lobbytools.bypass.lobby")) {*/

        /*}*/

    }


}
