package me.kamey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {

    String pluginName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("language.prefix") + " ");
    private Location location;

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
        loadLoc();
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&bStarting!"));
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&bThis plugin is made by Kamey"));
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("setjoinspawn")) {
            //this is to check if its not console?
            if (!(sender instanceof ConsoleCommandSender)) {
                if (p.hasPermission("spawnonjoin.admin")) {
                    if (args.length == 0) {
                        this.location = p.getLocation();
                        saveLoc(this.location);
                        p.sendMessage(this.pluginName + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.spawn-set"))));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.error"))));
                    return true;
                }
                p.sendMessage(this.pluginName + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.no-permission"))));
                return true;
            } else {
                sender.sendMessage(this.pluginName + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.not-a-player"))));
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("spawn")) {
            //make sure sender is not console
            if (!(sender instanceof ConsoleCommandSender)) {
                if (p.hasPermission("spawnonjoin.command")) {
                    if (args.length == 0) {
                        if (getConfig().getString("location.world") != null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.teleporting"))));
                            p.teleport(this.location);
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.spawn-not-set"))));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.spawn-not-set"))));
                    return true;
                }
                p.sendMessage(this.pluginName + ChatColor.translateAlternateColorCodes('&', "&cNo Permission!"));
                return true;
            } else {
                sender.sendMessage(this.pluginName + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.not-a-player"))));
            }
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
        saveDefaultConfig();
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
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.spawn-not-set"))));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (getConfig().getString("location.world") != null) {
            p.teleport(this.location);
        } else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.spawn-not-set"))));
        }

    }

}

