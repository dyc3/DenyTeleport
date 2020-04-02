package com.dyc3.DenyTeleport;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {
    private List<String> regions;
    private Logger log;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.regions = new ArrayList<>();
        this.regions.addAll(getConfig().getStringList("regions"));
        getCommand("reload").setExecutor((CommandExecutor)this);
        getServer().getPluginManager().registerEvents(this, (Plugin)this);

        log = PluginLogger.getLogger("DenyTeleport");
        log.info("DenyTeleport has been enabled.");
    }

    @Override
    public void onDisable() {
        //Fired when the server stops and disables all plugins
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("DenyTeleport.Reload")) {
            reload();
            sender.sendMessage(ChatColor.GREEN + "DenyTeleport has been reloaded.");
        }
        return true;
    }

    public final void reload() {
        reloadConfig();
        this.regions.clear();
        this.regions.addAll(getConfig().getStringList("regions"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTP(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.COMMAND) {
            return;
        }
        Player player = event.getPlayer();
        if (player.hasPermission("DenyTeleport.override.all")) {
            return;
        }

        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(event.getTo().getWorld()));
        Location destination = event.getTo();
        ApplicableRegionSet ar = regionManager.getApplicableRegions(BlockVector3.at(destination.getBlockX(), destination.getBlockY(), destination.getBlockZ()));
        Iterator<ProtectedRegion> prs = ar.iterator();
        while (prs.hasNext()) {
            ProtectedRegion pr = prs.next();
            if (this.regions.contains(pr.getId()) && !player.hasPermission("DenyTeleport.override." + pr.getId())) {
                player.sendMessage(getConfig().getString("message").replaceAll("(&([a-f0-9]))", "§$2"));
                event.setCancelled(true);
            }
        }
    }
}
