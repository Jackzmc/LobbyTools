package me.jackz.lobbytools;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.tr7zw.itemnbtapi.ItemNBTAPI;
import de.tr7zw.itemnbtapi.NBTItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

class InventoryEvents implements Listener {
    private final Main plugin;
    final static Inventory SERVER_SELECTOR = Bukkit.createInventory(null,54,"§6Lobby Selector");
    private Set<String> server_ids;
    InventoryEvents(Main plugin) {
        this.plugin = plugin;
        updateServers();
    }
    void updateServers() {
        SERVER_SELECTOR.clear();
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("servers");
        if(sec == null) {
            plugin.getLogger().warning("No valid servers found in configuration servers");
            return;
        }
        server_ids = sec.getKeys(false);
        int slot = 8;
        for(String key : server_ids){
            String name = sec.getString(key + ".name");
            String item = sec.getString(key + ".item");
            List<String> lore = sec.getStringList(key + ".lore");
            Material material = Material.GLASS_PANE;

            if(name == null) {
                name = WordUtils.capitalize(key);
            }
            if(item != null) material = Material.matchMaterial(item);
            if(material == null) material = Material.GLASS_PANE;
            slot+=2;
            if(slot >= 45) {
                plugin.getLogger().warning("over max limit");
                break;
            }
            if(slot >= SERVER_SELECTOR.getMaxStackSize()) break; //todo: allow new pages

            ItemStack itemstack = Util.getNamedItem(material, ChatColor.GOLD + name,lore);
            NBTItem nbt = ItemNBTAPI.getNBTItem(itemstack);
            nbt.setString("server",key);
            SERVER_SELECTOR.setItem(slot,nbt.getItem());
            if(slot == 24 || slot == 16 || slot == 34) {
                slot+=2;
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if(!p.hasPermission("lobbytools.bypass.inventory")) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onItemClick(InventoryClickEvent e) {
        InventoryView view = e.getView();
        Player p = (Player) e.getWhoClicked();
        if(view.getTitle().equalsIgnoreCase("§6Lobby Selector")) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if(item == null || !item.hasItemMeta()) return;
            NBTItem nbt = ItemNBTAPI.getNBTItem(item);
            if(nbt.hasKey("server")) {
                for(String server : server_ids) {
                    if (nbt.getString("server").equalsIgnoreCase(server)) {
                        p.sendMessage("§aTeleporting you to §e" + server);
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(server);
                        //applies to the player you send it to. aka Kick To Server.
                        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                        return;
                    }
                }
                p.sendMessage("§cSorry, but that server is not configured correctly.");
                view.close();
            }
            return;
        }
        if(!p.hasPermission("lobbytools.bypass.inventory")) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onItemMove(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(!p.hasPermission("lobbytools.bypass.inventory")) {
            e.setCancelled(true);
        }
    }
}
