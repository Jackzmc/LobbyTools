package me.jackz.lobbytools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {
    static ItemStack getNamedItem(Material material, String name) {
        return getNamedItem(material,name,new ArrayList<>());
    }
    static ItemStack getNamedItem(Material material, String name, String lore) {
        return getNamedItem(material,name,new ArrayList<>(
                Collections.singletonList(lore)
        ));
    }
    static ItemStack getNamedItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
}
