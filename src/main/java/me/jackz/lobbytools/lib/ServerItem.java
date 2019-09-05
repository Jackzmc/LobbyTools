package me.jackz.lobbytools.lib;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jackz.lobbytools.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ServerItem {
    private String name;
    private String displayName;
    private ItemStack itemStack;
    private int slot;

    public ServerItem(String name, ItemStack item, int slot) {
        this.name = name;
        this.itemStack = item;
        this.slot = slot;
    }
    public ServerItem(String key, ItemStack item, int slot, String display) {
        this.name = key;
        this.itemStack = item;
        this.slot = slot;
        this.displayName = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemstack) {
        this.itemStack = itemstack;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void updatePlayerCount(Main plugin, Player p) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(name);
        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
