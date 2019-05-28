package me.jackz.lobbytools;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
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

    static List<String> getRegionsAtLocation(Location loc)  {
        List<String> regions = new ArrayList<>();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));
        for(ProtectedRegion region : set) {
            regions.add(region.getId());
        }
        return regions;
    }
}
