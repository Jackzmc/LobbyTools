package me.jackz.lobbytools;

import me.jackz.lobbytools.lib.ParkourRegion;
import me.jackz.lobbytools.lib.ParkourRegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.IOException;
import java.util.List;

public class Commands implements CommandExecutor {
    private final Main plugin;
    Commands(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("lobbytools.command")) {
            sender.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }
        if(args.length == 0) {
            return false;
        }
        switch(args[0].toLowerCase()) {
            case "parkour":
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    if(p.hasPermission("lobbytools.command.parkour")) {
                        //todo: check for worldguard
                        if(args.length < 2) {
                            sender.sendMessage("§cUsage: /lobbytools parkour help");
                            return true;
                        }
                        ParkourRegionManager parkourRegionManager = plugin.getParkourRegionManager();
                        switch(args[1].toLowerCase()) {
                            case "create":
                                if(args.length < 3) {
                                    p.sendMessage("§cInvalid usage. Usage: §e/lt parkour create <name> [min y]");
                                }else{
                                    //todo: check for existing regions
                                    ParkourRegion region = new ParkourRegion(args[2],p.getLocation());

                                    if(args.length >= 4 ) {
                                        try {
                                            int y = Integer.parseInt(args[3]);
                                            region.setMinY(y);
                                        }catch(NumberFormatException e) {
                                            p.sendMessage("§cInvalid minimum Y-Level. Please use a whole number");
                                            return true;
                                        }
                                    }
                                    List<String> regions = Util.getRegionsAtLocation(p.getLocation());
                                    for (String s : regions) {
                                        if(s.equals(region.getName())) {
                                            parkourRegionManager.addRegion(region);
                                            parkourRegionManager.saveRegions();
                                            p.sendMessage("§aSuccessfully created parkour region named §e" + region.getName());
                                            return true;
                                        }
                                    }
                                    p.sendMessage("§cCould not create parkour region: No WorldGuard regions found");
                                }
                                break;
                            case "list":
                                List<ParkourRegion> regions = parkourRegionManager.getRegions();
                                p.sendMessage("§6LobbyTools Parkour Regions");
                                for (int i = 0; i < regions.size(); i++) {
                                    ParkourRegion region = regions.get(i);
                                    Location spawnpoint = region.getSpawnPoint();
                                    int x = (int) Math.round(spawnpoint.getX());
                                    int y = (int) Math.round(spawnpoint.getY());
                                    int z = (int) Math.round(spawnpoint.getZ());
                                    p.sendMessage(String.format("§a%d. §e%s §7- X:%d Y:%d Z:%d",i+1,region.getName(),x,y,z));
                                }
                                break;
                            case "remove":
                                p.sendMessage("§cNot Implemented");
                                break;
                            case "help":
                                p.sendMessage("§6LobbyTools Parkour Regions" +
                                        "\n§e/lt parkour create <name> [min y] §7- creates a region with spawnpoint at your feet" +
                                        "\n§e/lt parkour list §7- list all parkour regions" +
                                        "\n§e/lt parkour remove <name> §7- remove a certain region");
                                break;
                            default:
                                p.sendMessage("§cUnknown parkour argument.  Try /lt parkour help");
                        }
                    }else{
                        p.sendMessage("§cYou do not have permission to use this command.");
                    }
                }else{
                    sender.sendMessage("§cYou must be a player to use this command.");
                }
                break;
            case "reload":
                if(sender.hasPermission("lobbytools.reload")) {
                    try {
                        plugin.getConfig().load(Main.CONFIG_FILE);
                        plugin.reloadPlugin();
                        sender.sendMessage("§aSuccessfully reloaded configuration.");
                    } catch (IOException e) {
                        sender.sendMessage("§cFailed to load configuration file");
                    } catch (InvalidConfigurationException e) {
                        sender.sendMessage("§cFailed to load configuration file due to invalid configuration.");
                    }

                }else{
                    sender.sendMessage("§cYou do not have permission to use this command");
                }
                break;
            case "restart":
                Bukkit.dispatchCommand(sender, "plugman reload LobbyTools");
                break;
            case "help":
                PluginDescriptionFile pdf = plugin.getDescription();
                sender.sendMessage("§6LobbyTools §eVersion " +  pdf.getVersion());
                sender.sendMessage("§7Sorry, help is currently a work in progress.");
                break;
            default:
                sender.sendMessage("§cUnknown argument, try /lt help");
        }
        return true;
    }
}