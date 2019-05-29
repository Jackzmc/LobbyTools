package me.jackz.lobbytools;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

class EntityDamage implements Listener {
    private final Main plugin;
    EntityDamage(Main plugin) {this.plugin = plugin;}

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (e.getEntity().getWorld().equals(Main.world)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
