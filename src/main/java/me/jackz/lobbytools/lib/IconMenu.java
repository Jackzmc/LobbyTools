package me.jackz.lobbytools.lib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** An easy to use icon menu for easy inventory-based navigation
 *
 */
public class IconMenu {
    private Inventory inventory;

    /** Create a new icon menu for easier inventory management
     * @param inventory_name The name of the inventory
     * @param rows The number of rows (will be x9)
     */
    public IconMenu(String inventory_name, int rows) {
        this.inventory = Bukkit.createInventory(null,9*rows,inventory_name);
    }

    /** Get the icon menu inventory
     * @return Inventory object
     */
    public Inventory getInventory() {
        return inventory;
    }

    /** Set the maximum inventory size
     * @param rows amount of rows (will be x9)
     */
    public void setSize(int rows) throws Exception {
        if(rows > 6) {
            throw new Exception("Invalid amount of rows");
        }
        inventory.setMaxStackSize(9*rows);
    }

    /** Get the maximum inventory size
     * @return The amount of rows
     */
    public int getSize() {
        return inventory.getMaxStackSize()/9;
    }

    /** Add a custom item to the inventory
     * @param slot The slot number (0-indexed)
     * @param material The material type of the item
     * @param name The name of the item
     * @param lore The lores of the item
     */
    public void setItem(int slot, Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(slot,item);
    }

    /** Add a custom item to the inventory
     * @param slot The slot number (0-indexed)
     * @param material The unnamed material type of the item
     */
    public void setItem(int slot, Material material) {
        inventory.setItem(slot,new ItemStack(material));
    }

    /** Add a custom item to the inventory
     * @param slot The slot number (0-indexed)
     * @param material The material type of the item
     * @param name The name of the item
     */
    public void setItem(int slot, Material material, String name) {
        setItem(slot,material,name,new ArrayList<>());
    }

    /** Add a custom item to the inventory
     * @param slot The slot number (0-indexed)
     * @param material The material type of the item
     * @param name The name of the item
     * @param lore A single lore of the item
     */
    public void setItem(int slot, Material material, String name, String lore) {
        setItem(slot,material,name,new ArrayList<>(
                Collections.singletonList(lore)
        ));
    }
}
