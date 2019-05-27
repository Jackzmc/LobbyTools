package me.jackz.lobbytools;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerEvents implements Listener {
    private final static Material[] PRESSURE_PLATES = {
            Material.STONE_PRESSURE_PLATE,
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.OAK_PRESSURE_PLATE,
            Material.DARK_OAK_PRESSURE_PLATE,
            Material.BIRCH_PRESSURE_PLATE,
            Material.SPRUCE_PRESSURE_PLATE,
            Material.ACACIA_PRESSURE_PLATE,
            Material.JUNGLE_PRESSURE_PLATE
    };
    private int y_teleport;
    private final Main plugin;
    PlayerEvents(Main plugin) {
        this.plugin = plugin;
        y_teleport = plugin.getConfig().getInt("y_spawn_teleport");
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(!p.getWorld().equals(Main.world)) return;
        Block b = p.getLocation().getBlock();


        if(plugin.getConfig().getBoolean("launchpad_enabled")) {
            for (Material plate : PRESSURE_PLATES) {
                if (b.getType().equals(plate)) {
                    p.setVelocity(p.getLocation().getDirection().multiply(4));
                    p.setVelocity(new Vector(p.getVelocity().getX(), 1.0D, p.getVelocity().getZ()));
                    break;
                }
            }

        }
        if(p.getLocation().getY() <= y_teleport) {
            p.teleport(Main.world.getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (item == null) {
            if (!p.hasPermission("lobbytools.bypass.inventory")) e.setCancelled(true);
            return;
        }
        switch (item.getType()) {
            case CLOCK:
                p.sendMessage("§cNot Implemented");
                break;
            case NETHER_STAR:
                p.openInventory(InventoryEvents.SERVER_SELECTOR);
                break;
            default:
                if (!p.hasPermission("lobbytools.bypass.inventory")) e.setCancelled(true);
        }
    }
}
