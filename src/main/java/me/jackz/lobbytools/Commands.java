package me.jackz.lobbytools;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class Commands implements CommandExecutor {
    private final Main plugin;
    public Commands(Main plugin) {
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
                sender.sendMessage("Help coming soon");
                break;
            default:
                sender.sendMessage("§cUnknown argument, try /lt help");
        }
        return true;
    }
}