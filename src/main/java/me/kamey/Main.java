package me.kamey;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class Main extends JavaPlugin implements Listener {

    public Logger log = Bukkit.getLogger();

    String plugin_name = ChatColor.translateAlternateColorCodes('&',"&2[KSpawn] ");

    private Location location;


    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        loadLoc();
        loadConfig();
        log.info(plugin_name + ChatColor.translateAlternateColorCodes('&',"&bIs Starting!"));
        log.info(plugin_name + ChatColor.translateAlternateColorCodes('&',"&bMade by Kamey"));
        log.warning(plugin_name + ChatColor.translateAlternateColorCodes('&',"&6This plugin is intended to be used with java 11 and newer"));
    }


    public void onDisable() {
        log.info(plugin_name + ChatColor.translateAlternateColorCodes('&',"&bStopping!"));
    }


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("setjoinspawn")) {
            if (sender != null) {
                if (p.hasPermission("spawnonjoin.admin")) {
                    if (args.length == 0) {
                        this.location = p.getLocation();
                        saveLoc(this.location);

                        p.sendMessage(String.valueOf(this.plugin_name) + ChatColor.translateAlternateColorCodes('&',"&aSpawn set!"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"   &7World: " + getConfig().getString("location.world")));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"   &7x: " + getConfig().getDouble("location.x")));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"   &7y: " + getConfig().getDouble("location.y")));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"   &7z: " + getConfig().getDouble("location.z")));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"   &7yaw: " + getConfig().getString("location.yaw")));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"   &7pitch: " + getConfig().getString("location.pitch")));
                        return true;

                    }

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cThere was an error, please do: /setjoinspawn"));

                    return true;

                }

                p.sendMessage(String.valueOf(this.plugin_name) + ChatColor.translateAlternateColorCodes('&',"&cNo Permission"));

                return true;

            }

            assert sender != null;
            sender.sendMessage("Only Players can run this commands!");

            return true;

        }

        if (cmd.getName().equalsIgnoreCase("spawn")) {


            if (sender != null) {


                if (p.hasPermission("spawnonjoin.command")) {


                    if (args.length == 0) {


                        if (getConfig().getString("location.world") != null) {


                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&bTeleporting..."));
                            p.teleport(this.location);

                            return true;

                        }

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7The spawn point is not set, to set spawn do: /setjoinspawn"));

                        return true;

                    }

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7The spawn point is not set, to set spawn do: /setjoinspawn"));

                    return true;

                }

                p.sendMessage(String.valueOf(this.plugin_name) + ChatColor.translateAlternateColorCodes('&',"&cNo Permission!"));

                return true;

            }

            assert sender != null;
            sender.sendMessage("Only Players can run this commands!");

            return true;

        }

        return true;

    }


    public void loadLoc() {

        if (getConfig().getString("location.world") != null) {

            this.location = new Location(getServer().getWorld(Objects.requireNonNull(getConfig().getString("location.world"))),
                    getConfig().getDouble("location.x"),
                    getConfig().getDouble("location.y"),
                    getConfig().getDouble("location.z"),
                    Float.parseFloat(Objects.requireNonNull(getConfig().getString("location.yaw"))),
                    Float.parseFloat(Objects.requireNonNull(getConfig().getString("location.pitch"))));

        }

    }


    public void loadConfig() {

        getConfig().options().copyDefaults(true);

        saveConfig();

    }


    public void saveLoc(Location location) {
        getConfig().set("location", location);
        getConfig().set("location.world", Objects.requireNonNull(location.getWorld()).getName());
        getConfig().set("location.x", location.getX());
        getConfig().set("location.y", location.getY());
        getConfig().set("location.z", location.getZ());
        getConfig().set("location.yaw", location.getYaw());
        getConfig().set("location.pitch", location.getPitch());
        saveConfig();

    }


    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {

        Player p = e.getPlayer();

        if (getConfig().getString("location.world") != null) {

            e.setRespawnLocation(this.location);

        } else {

            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7The spawn point is not set, to set spawn do: /setjoinspawn"));

        }

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        if (getConfig().getString("location.world") != null) {

            p.teleport(this.location);

        } else {

            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7The spawn point is not set, to set spawn do: /setjoinspawn"));
        }

    }

}

