package me.jackz.lobbytools;

import me.jackz.lobbytools.utils.Gadget;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    static Map<Player, Gadget> activeGadgets = new HashMap<>();
}
