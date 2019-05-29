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
    /** Gives a custom named ItemStack
     * @param material The material type
     * @param name The name of the item
     * @return Custom Named ItemStack of material
     */
    public static ItemStack getNamedItem(Material material, String name) {
        return getNamedItem(material,name,new ArrayList<>());
    }

    /** Gives a custom named ItemStack
     * @param material The material type
     * @param name The name of the item
     * @param lore A single line of lore
     * @return Named ItemStack of material with lore
     */
    public static ItemStack getNamedItem(Material material, String name, String lore) {
        return getNamedItem(material,name,new ArrayList<>(
                Collections.singletonList(lore)
        ));
    }
    /** Gives a custom named ItemStack
     * @param material The material type
     * @param name The name of the item
     * @param lore A string list of lores
     * @return Named ItemStack of material with lore
     */
    public static ItemStack getNamedItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    /** Get worldguard region names at location
     * @param loc The location to test for
     * @return String list of region names
     */
    public static List<String> getRegionsAtLocation(Location loc)  {
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
