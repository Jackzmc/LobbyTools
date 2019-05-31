package me.jackz.lobbytools;

import de.Herbystar.TTA.TTA_Methods;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class JoinEvent implements Listener {
    private final Main plugin;

    JoinEvent(Main plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        setupPlayer(e.getPlayer());
    }
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        setupPlayer(e.getPlayer());
    }

    private void setupPlayer(Player p) {
        /*if(!p.hasPermission("lobbytools.bypass.lobby")) {*/

        /*}*/
        if(p.getWorld().equals(Main.world)) {
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setItem(1,Util.getNamedItem(Material.CLOCK,"§6Player Hider","§7Toggle hiding players"));
            p.getInventory().setItem(4,Util.getNamedItem(Material.NETHER_STAR,"§6Server Selector","§7Select a server"));
            p.getInventory().setItem(7,Util.getNamedItem(Material.CHEST,"§6Gadgets","§7Fun toys for all"));

            String title = plugin.getConfig().getString("messages.title");
            String subtitle = plugin.getConfig().getString("messages.subtitle");
            p.sendTitle(title,subtitle,0,20,0);
            if(plugin.isTTAEnabled()) {
                String actionbar = plugin.getConfig().getString("messages.actionbar");
                TTA_Methods.sendActionBar(p, actionbar);
            }
            //temp. measure to stop saturation issues:
            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999,1,true));
            if(!plugin.hidden_map.containsKey(p.getUniqueId())) plugin.hidden_map.put(p.getUniqueId(),false);
        }
    }

}
