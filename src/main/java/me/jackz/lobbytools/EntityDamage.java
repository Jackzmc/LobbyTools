package me.jackz.lobbytools;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {
    private final Main plugin;
    EntityDamage(Main plugin) {this.plugin = plugin;}

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                String config_world = plugin.getConfig().getString("lobby_world");
                if (config_world != null) {
                    World world = Bukkit.getWorld(config_world);
                    if (e.getEntity().getWorld().equals(world)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
