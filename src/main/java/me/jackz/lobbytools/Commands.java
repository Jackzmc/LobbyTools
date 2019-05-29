package me.jackz.lobbytools;

import me.jackz.lobbytools.lib.LanguageManager;
import me.jackz.lobbytools.lib.ParkourRegion;
import me.jackz.lobbytools.lib.ParkourRegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

class Commands implements CommandExecutor {
    private final Main plugin;
    private final LanguageManager lm;
    Commands(Main plugin) {
        this.plugin = plugin;
        this.lm = plugin.getLanguageManager();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("lobbytools.command")) {
            sender.sendMessage(lm.getCommand("nopermission"));
            return true;
        }
        if(args.length == 0) {
            return false;
        }
        switch(args[0].toLowerCase()) {
            case "pk":
            case "parkour":
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    if(p.hasPermission("lobbytools.command.parkour")) {
                        //todo: check for worldguard
                        if(args.length < 2) {
                            sender.sendMessage(lm.getCommand("parkour.usage"));
                            return true;
                        }
                        ParkourRegionManager parkourRegionManager = plugin.getParkourRegionManager();
                        switch(args[1].toLowerCase()) {
                            case "create":
                                if(args.length < 3) {
                                    p.sendMessage(lm.getCommand("parkour.invalid") + "§e/lt parkour create <name> [min y]");
                                }else{
                                    //todo: check for existing regions
                                    ParkourRegion region = new ParkourRegion(args[2],p.getLocation());

                                    if(args.length >= 4 ) {
                                        try {
                                            int y = Integer.parseInt(args[3]);
                                            region.setMinY(y);
                                        }catch(NumberFormatException e) {
                                            p.sendMessage(lm.getCommand("parkour.create.invalidnumber"));
                                            return true;
                                        }
                                    }
                                    List<String> regions = Util.getRegionsAtLocation(p.getLocation());
                                    for (String s : regions) {
                                        if(s.equals(region.getName())) {
                                            parkourRegionManager.addRegion(region);
                                            parkourRegionManager.saveRegions();
                                            p.sendMessage(lm.getCommand("parkour.create.success",region.getName()));
                                            return true;
                                        }
                                    }
                                    p.sendMessage(lm.getCommand("parkour.create.noregionfound"));
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
                                    p.sendMessage(String.format("§a%d. §e%s §7- X:%d Y:%d Z:%d Yaw:%d",i+1,region.getName(),x,y,z,yaw));
                                }
                                break;
                            case "delete":
                            case "del":
                            case "remove":
                                if(args.length >= 3) {
                                    boolean success = parkourRegionManager.removeRegion(args[2]);
                                    if(success) {
                                        p.sendMessage(lm.getCommand("parkour.delete.success"));
                                    }else{
                                        p.sendMessage(lm.getCommand("parkour.delete.failed"));
                                    }
                                }else{
                                    p.sendMessage(lm.getCommand("parkour.invalid") + "§cUsage: /lt parkour delete <name>");
                                }
                                break;
                            case "help":
                                p.sendMessage("§6LobbyTools Parkour Regions" + lm.getCommand("parkour.help"));
                                break;
                            default:
                                p.sendMessage(lm.getCommand("unknownargument"));
                        }
                    }else{
                        p.sendMessage(lm.getCommand("nopermission"));
                    }
                }else{
                    sender.sendMessage(lm.getCommand("playeronly"));
                }
                break;
            case "reload":
                if(sender.hasPermission("lobbytools.reload")) {
                    try {
                        plugin.getConfig().load(Main.CONFIG_FILE);
                        plugin.reloadPlugin();
                        sender.sendMessage(lm.getCommand("reload.success"));
                    } catch (IOException e) {
                        sender.sendMessage(lm.getCommand("reload.failed_general"));
                    } catch (InvalidConfigurationException e) {
                        sender.sendMessage(lm.getCommand("reload.failed_invalid"));
                    }

                }else{
                    sender.sendMessage(lm.getCommand("nopermission"));
                }
                break;
            case "logs":
                try {
                    Player p = (Player) sender;
                    String log = plugin.getDataFolder().toPath() + "/../../logs/latest.log";
                    FileInputStream in = new FileInputStream(log);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    List<String> lines = new LinkedList<>();
                    for (String tmp; (tmp = br.readLine()) != null; )
                        lines.add(tmp);
                        if (lines.size() > 5) //this removes older
                            lines.remove(0);
                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                    BookMeta meta = (BookMeta) book.getItemMeta();
                    Date today = new Date();
                    assert meta != null;
                    meta.setAuthor("shittyCode");
                    meta.setTitle("Logs " +  new SimpleDateFormat("yyyy-MM-dd").format(today));
                    for (String line : lines) {
                        meta.addPage(line.trim());
                    }
                    book.setItemMeta(meta);
                    p.getInventory().addItem(book);
                } catch (Exception ex) {
                    sender.sendMessage("Failed to get file: " + ex.toString());
                    plugin.getLogger().log(Level.INFO, "getlogs!", ex);
                }
                break;
            case "restart":
                //THIS IS A DEV FEATURE ONLY, REMOVE ON PROD
                Bukkit.dispatchCommand(sender, "plugman reload LobbyTools");
                break;
            case "help":
                PluginDescriptionFile pdf = plugin.getDescription();
                sender.sendMessage("§6LobbyTools §eVersion " +  pdf.getVersion());
                sender.sendMessage(lm.getCommand("help"));
                break;
            default:
                sender.sendMessage(lm.getCommand("unknownargument"));
        }
        return true;
    }
}