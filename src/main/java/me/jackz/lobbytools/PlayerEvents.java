package me.jackz.lobbytools;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.jackz.lobbytools.lib.LanguageManager;
import me.jackz.lobbytools.lib.ParkourRegion;
import me.jackz.lobbytools.lib.ParkourRegionManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.List;

class PlayerEvents implements Listener {
    private Main plugin;
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
    private boolean launchpad_enabled = false;
    private static ParkourRegionManager parkourRegionManager;
    private static LanguageManager lm;
    private int y_teleport = 0;
    PlayerEvents(Main plugin) {
        this.plugin = plugin;
        reloadVariables();
    }
    void reloadVariables() {
        launchpad_enabled = plugin.getConfig().getBoolean("launchpad_enabled");
        y_teleport = plugin.getConfig().getInt("y_spawn_teleport");
        parkourRegionManager = plugin.getParkourRegionManager();
        lm = plugin.getLanguageManager();
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(!p.getWorld().equals(Main.world)) return;
        Block b = p.getLocation().getBlock();

        if(launchpad_enabled) {
            for (Material plate : PRESSURE_PLATES) {
                if (b.getType().equals(plate)) {
                    p.setVelocity(p.getLocation().getDirection().multiply(3));
                    p.setVelocity(new Vector(p.getVelocity().getX(), 1.0D, p.getVelocity().getZ()));
                    break;
                }
            }

        }
        if(p.getLocation().getY() <= y_teleport) {
            p.teleport(Main.world.getSpawnLocation());
            return;
        }
        //probably needs some optimization
        List<ParkourRegion> parkourRegionList = parkourRegionManager.getRegions();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(p.getLocation()));
        for (ProtectedRegion region : set) {
            for (ParkourRegion parkourRegion : parkourRegionList) {
                if(region.getId().equalsIgnoreCase(parkourRegion.getName())) {
                    if(p.isFlying()) {
                        lm.send(p,"parkour_flight_disabled");
                    }
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    //is parkour region
                    if(p.getLocation().getY() < parkourRegion.getMinY()) {
                        parkourRegion.respawnPlayer(p);
                        //p.teleport(parkourRegion.getSpawnPoint(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        if(parkourRegion.hasFailMessage()) p.sendMessage(parkourRegion.getFailMessage());
                    }else{
                        Block beneath = p.getLocation().subtract(0,1,0).getBlock();
                        if(beneath.getType().equals(Material.GOLD_BLOCK)) {
                            parkourRegion.nextCheckpoint(p);
                        }
                    }
                    return;
                }
            }
        }
        //need some check so not called 24/7?
        if(!p.isFlying() && !p.getAllowFlight() && p.hasPermission("lobbytools.lobby.flight")) {
            p.setAllowFlight(true);
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
            case CLOCK: {
                Enchantment enchantment = EnchantmentWrapper.PROTECTION_FALL;
                boolean is_hidden = plugin.hidden_map.getOrDefault(p.getUniqueId(), false);
                if(is_hidden) {
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        if(p.equals(player)) continue;
                        p.showPlayer(plugin,player);
                    }
                    item.removeEnchantment(enchantment);
                    p.sendMessage("§aShowing all players now.");
                    plugin.hidden_map.put(p.getUniqueId(), false);
                }else{
                    for (Player player : plugin.getServer().getOnlinePlayers()) {
                        if(p.equals(player)) continue;
                        if(player.hasPermission("lobbytools.bypass.hide")) continue;
                        p.hidePlayer(plugin,player);
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(meta);
                    item.addUnsafeEnchantment(enchantment,1);
                    p.sendMessage("§aHiding all players now.");
                    plugin.hidden_map.put(p.getUniqueId(), true);
                }
                break;
            }case CHEST: {
                p.openInventory(InventoryEvents.GADGETS_MENU);
                break;
            } case NETHER_STAR:
                p.openInventory(InventoryEvents.SERVER_SELECTOR);
                break;
            default:
                if (!p.hasPermission("lobbytools.bypass.inventory")) e.setCancelled(true);
        }
    }
}
