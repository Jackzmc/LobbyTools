package me.jackz.lobbytools;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
            lore.add("§7Server:"+key);
            ItemStack itemstack = Util.getNamedItem(material, ChatColor.GOLD + name,lore);

            SERVER_SELECTOR.setItem(slot,itemstack);
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
            List<String> lores = item.getItemMeta().getLore();
            String last_value = ChatColor.stripColor(lores.get(lores.size()-1));
            if(last_value.toLowerCase().startsWith("server")) {
                String lore_server = last_value.split(":")[1];
                for(String server : server_ids) {
                    plugin.getLogger().info(lore_server + " : " + server);
                    if(lore_server.equals(server)) {
                        p.sendMessage("§aTeleporting you to §e" + server);
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(server);
                        //applies to the player you send it to. aka Kick To Server.
                        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                        return;
                    }
                }
            }
            p.sendMessage("§cSorry, but that server is not configured correctly.");
            view.close();
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
