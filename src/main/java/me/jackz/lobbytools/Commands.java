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

class Commands implements CommandExecutor {
    private final Main plugin;
    private final LanguageManager lm;
    Commands(Main plugin) {
        this.plugin = plugin;
        this.lm = plugin.getLanguageManager();
    }
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("lobbytools.command")) {
            lm.sendCommand(sender,"nopermission");
            return true;
        }
        if(args.length == 0) {
            lm.sendCommand(sender,"noarguments");
            return true;
        }
        switch(args[0].toLowerCase()) {
            case "pk":
            case "parkour":
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    if(p.hasPermission("lobbytools.command.parkour")) {
                        if(!plugin.isPluginEnabled("WorldGuard")) {
                            lm.sendCommand(p,"parkour.worldguard_not_enabled");
                            return true;
                        }
                        if(args.length < 2) {
                            lm.sendCommand(sender,"parkour.usage");
                            return true;
                        }
                        ParkourRegionManager parkourRegionManager = plugin.getParkourRegionManager();
                        switch(args[1].toLowerCase()) {
                            case "create":
                                if(args.length < 3) {
                                    lm.sendCommand(p,"parkour.invalid","§e/lt parkour create <name> [min y]");
                                }else{
                                    //todo: check for existing regions
                                    ParkourRegion region = new ParkourRegion(args[2],p.getLocation());

                                    if(args.length >= 4 ) {
                                        try {
                                            int y = Integer.parseInt(args[3]);
                                            region.setMinY(y);
                                        }catch(NumberFormatException e) {
                                            lm.sendCommand(p,"parkour.create.invalidnumber");
                                            return true;
                                        }
                                    }
                                    List<String> regions = Util.getRegionsAtLocation(p.getLocation());
                                    for (String s : regions) {
                                        if(s.equals(region.getName())) {
                                            parkourRegionManager.addRegion(region);
                                            parkourRegionManager.saveRegions();
                                            lm.sendCommand(p,"parkour.create.success",region.getName());
                                            return true;
                                        }
                                    }
                                    lm.sendCommand(p,"parkour.create.noregionfound");
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
                                    int yaw = Math.round(spawnpoint.getYaw());
                                    p.sendMessage(String.format("§a%d. §e%s §7: X:%d Y:%d Z:%d Yaw:%d",i+1,region.getName(),x,y,z,yaw));
                                }
                                break;
                            case "delete":
                            case "del":
                            case "remove":
                                if(args.length >= 3) {
                                    boolean success = parkourRegionManager.removeRegion(args[2]);
                                    if(success) {
                                        lm.sendCommand(p,"parkour.delete.success");
                                    }else{
                                        lm.sendCommand(p,"parkour.delete.failed");
                                    }
                                }else{
                                    lm.sendCommand(p,"parkour.invalid","§cUsage: /lt parkour delete <name>");
                                }
                                break;
                            case "help":
                                p.sendMessage("§6LobbyTools Parkour Regions" + lm.getCommand("parkour.help"));
                                break;
                            case "checkpoint":
                                if (args[2].toLowerCase().equalsIgnoreCase("help")) {
                                    lm.send(p,"notimplemented");
                                    return true;
                                }
                                if(args.length < 4) {
                                    lm.sendCommand(p,"parkour.invalid","§e/lt parkour checkpoint help");
                                }else{
                                    ParkourRegion parkourRegion;
                                    if(args[2].toLowerCase().equalsIgnoreCase("here")) {
                                        parkourRegion = parkourRegionManager.getRegion(p.getLocation());
                                    }else{
                                        parkourRegion = parkourRegionManager.findRegion(args[2].toLowerCase());
                                    }
                                    if(parkourRegion == null) {
                                        p.sendMessage("§cNo Parkour Regions were found.");
                                        return true;
                                    }

                                    switch(args[3].toLowerCase()) {
                                        case "create":
                                        case "new":
                                        case "add": {
                                            lm.send(p, "notimplemented");
                                            int id = parkourRegion.addCheckpoint(p.getLocation());
                                            id++;
                                            p.sendMessage("§aAdded checkpoint §e#" + id);
                                            break;
                                        } case "getmine":
                                            int region = parkourRegion.getPlayerCheckpoint(p);
                                            p.sendMessage("you are on checkpoint #" + region);
                                            break;
                                        case "list":
                                            List<Location> checkpoints = parkourRegion.getCheckpoints();
                                            //HashMap<Integer,Location> checkpoints = parkourRegion.getCheckpoints();
                                            p.sendMessage("§6LobbyTools Parkour Regions");
                                            if(checkpoints.size() == 0) {
                                                p.sendMessage("§cThere are no checkpoints for this region.");
                                                return true;
                                            }
                                            for (int i = 0; i < checkpoints.size(); i++) {
                                                Location loc = checkpoints.get(i);
                                                int x = (int) Math.round(loc.getX());
                                                int y = (int) Math.round(loc.getY());
                                                int z = (int) Math.round(loc.getZ());
                                                int yaw = Math.round(loc.getYaw());
                                                p.sendMessage(String.format("§e%d. §7X:%d Y:%d Z:%d Yaw:%d",i+1,x,y,z,yaw));
                                            }
                                            break;
                                        case "del":
                                        case "remove":
                                        case "delete": {
                                            try {
                                                int id = Integer.parseInt(args[4]);
                                                parkourRegion.removeCheckpoint(id);
                                                p.sendMessage("§aRemoved checkpoint §e#" + id);
                                            }catch(NumberFormatException e) {
                                                p.sendMessage("§cInvalid id");
                                                return true;
                                            }
                                            break;
                                        } default:
                                            lm.sendCommand(p,"unknownargument");
                                    }
                                }
                                break;
                            default:
                                lm.sendCommand(p,"unknownargument");
                        }
                    }else{
                        lm.sendCommand(p,"nopermission");
                    }
                }else{
                    lm.sendCommand(sender,"playeronly");
                }
                break;
            case "reload":
                if(sender.hasPermission("lobbytools.reload")) {
                    try {
                        plugin.getConfig().load(Main.CONFIG_FILE);
                        plugin.reloadPlugin();
                        lm.sendCommand(sender,"reload.success");
                    } catch (IOException e) {
                        lm.sendCommand(sender,"reload.failed_general");
                    } catch (InvalidConfigurationException e) {
                        lm.sendCommand(sender,"reload.failed_invalid");
                    }

                }else{
                    lm.sendCommand(sender,"nopermission");
                }
                break;
            case "save":
                plugin.getParkourRegionManager().saveRegions();
                plugin.saveConfig();
                //plugin.saveData();
                sender.sendMessage("§aSuccessfully saved data");
            case "restart":
                //THIS IS A DEV FEATURE ONLY, REMOVE ON PROD
                Bukkit.dispatchCommand(sender, "plugman reload LobbyTools");
                break;
            case "help":
                PluginDescriptionFile pdf = plugin.getDescription();
                sender.sendMessage("§6LobbyTools §eVersion " +  pdf.getVersion());
                lm.sendCommand(sender,"help");
                break;
            default:
                lm.sendCommand(sender,"unknownargument");
        }
        return true;
    }
}