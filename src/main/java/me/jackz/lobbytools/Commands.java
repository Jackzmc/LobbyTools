package me.jackz.lobbytools;

import me.jackz.lobbytools.lib.LanguageManager;
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
    private final LanguageManager lm;
    Commands(Main plugin) {
        this.plugin = plugin;
        this.lm = plugin.getLanguageManager();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("lobbytools.command")) {
            sender.sendMessage(lm.get("command.nopermission"));
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
                            sender.sendMessage(lm.get("command.parkour.usage"));
                            return true;
                        }
                        ParkourRegionManager parkourRegionManager = plugin.getParkourRegionManager();
                        switch(args[1].toLowerCase()) {
                            case "create":
                                if(args.length < 3) {
                                    p.sendMessage(lm.get("command.parkour.invalid") + "§e/lt parkour create <name> [min y]");
                                }else{
                                    //todo: check for existing regions
                                    ParkourRegion region = new ParkourRegion(args[2],p.getLocation());

                                    if(args.length >= 4 ) {
                                        try {
                                            int y = Integer.parseInt(args[3]);
                                            region.setMinY(y);
                                        }catch(NumberFormatException e) {
                                            p.sendMessage(lm.get("command.parkour.invalidnumber"));
                                            return true;
                                        }
                                    }
                                    List<String> regions = Util.getRegionsAtLocation(p.getLocation());
                                    for (String s : regions) {
                                        if(s.equals(region.getName())) {
                                            parkourRegionManager.addRegion(region);
                                            parkourRegionManager.saveRegions();
                                            p.sendMessage(lm.getMessage("parkour.success",region.getName()));
                                            return true;
                                        }
                                    }
                                    p.sendMessage(lm.get("command.parkour.noregionfound"));
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
                                p.sendMessage(lm.get("command.notimplemented"));
                                break;
                            case "help":
                                p.sendMessage("§6LobbyTools Parkour Regions" + lm.get("command.parkour.help"));
                                break;
                            default:
                                p.sendMessage(lm.get("command.unknownargument"));
                        }
                    }else{
                        p.sendMessage(lm.get("command.nopermission"));
                    }
                }else{
                    sender.sendMessage(lm.get("command.playeronly"));
                }
                break;
            case "reload":
                if(sender.hasPermission("lobbytools.reload")) {
                    try {
                        plugin.getConfig().load(Main.CONFIG_FILE);
                        plugin.reloadPlugin();
                        sender.sendMessage(lm.get("command.reload.success"));
                    } catch (IOException e) {
                        sender.sendMessage(lm.get("command.reload.failed_general"));
                    } catch (InvalidConfigurationException e) {
                        sender.sendMessage(lm.get("command.reload.failed_invalid"));
                    }

                }else{
                    sender.sendMessage(lm.get("command.nopermission"));
                }
                break;
            case "restart":
                //THIS IS A DEV FEATURE ONLY, REMOVE ON PROD
                Bukkit.dispatchCommand(sender, "plugman reload LobbyTools");
                break;
            case "help":
                PluginDescriptionFile pdf = plugin.getDescription();
                sender.sendMessage("§6LobbyTools §eVersion " +  pdf.getVersion());
                sender.sendMessage(lm.get("command.help"));
                break;
            default:
                sender.sendMessage(lm.get("command.unknownargument"));
        }
        return true;
    }
}