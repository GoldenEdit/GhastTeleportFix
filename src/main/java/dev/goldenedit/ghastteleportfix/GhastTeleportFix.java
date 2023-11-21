package dev.goldenedit.ghastteleportfix;

import dev.goldenedit.ghastteleportfix.listeners.EntityPortal;
import dev.goldenedit.ghastteleportfix.listeners.EntityPortalEnter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.text.html.parser.Entity;

public class GhastTeleportFix extends JavaPlugin implements Listener, CommandExecutor {

    public boolean cacheEnabled;
    public long cacheDurationTicks;
    public long ghastTeleportWaitTicks;

    public boolean convertGhastsToDrops;
    public long maxTearDrop;
    public long maxGunpowderDrop;

    public boolean preventGhastsUsingPortals;

    public boolean debugMode;

    @Override
    public void onEnable() {
        getLogger().info("Ghast Teleports fixed by GoldenEdit.dev");

        // Register the command executor
        this.getCommand("reloadghastconfig").setExecutor(this);

        loadConfigAndRegisterEvents();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reloadghastconfig")) {
            if (!sender.hasPermission("ghastteleportfix.reload")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }

            reloadConfig();
            loadConfigAndRegisterEvents();
            sender.sendMessage("Config reloaded.");
            return true;
        }
        return false;
    }

    private void loadConfigAndRegisterEvents() {
        saveDefaultConfig();
        cacheEnabled = getConfig().getBoolean("cache.cache-enabled");
        cacheDurationTicks = getConfig().getLong("cache.cache-duration-seconds") * 20L;
        ghastTeleportWaitTicks = getConfig().getLong("cache.ghast-teleport-wait-seconds") * 20L;

        convertGhastsToDrops = getConfig().getBoolean("drops.convert-ghasts-to-drops");
        maxTearDrop = getConfig().getLong("drops.max-tear-drop") + 1;
        maxGunpowderDrop = getConfig().getLong("drops.max-gunpowder-drop") + 1;

        preventGhastsUsingPortals = getConfig().getBoolean("prevent-ghasts-using-portals");

        debugMode = getConfig().getBoolean("debug");
        debugLog("Debug mode is enabled");

        if (cacheEnabled && convertGhastsToDrops) {
            getLogger().warning("Config values 'cache-enabled' and 'convert-ghasts-to-drops' cannot both be enabled, disabled both modules.");
            cacheEnabled = false;
            convertGhastsToDrops = false;
        }

        if (preventGhastsUsingPortals && (convertGhastsToDrops || cacheEnabled)) {
            getLogger().warning("'preventGhastsUsingPortals' cannot be enabled when config values 'cache-enabled' or 'convert-ghasts-to-drops' are enabled. Disabling all other modules apart from 'preventGhastsUsingPortals'");
            cacheEnabled = false;
            convertGhastsToDrops = false;
        }

        if (cacheEnabled || convertGhastsToDrops) getServer().getPluginManager().registerEvents(new EntityPortalEnter(this), this);
        if (preventGhastsUsingPortals)getServer().getPluginManager().registerEvents(new EntityPortal(this), this);

        getLogger().info("Cache enabled: " + cacheEnabled);
        getLogger().info("Convert Ghasts to drops: " + convertGhastsToDrops);
        getLogger().info("Prevent Ghasts using portals: " + preventGhastsUsingPortals);
    }

    // Utility method for conditional debug logging
    public void debugLog(String message) {
        if (debugMode) {
            getLogger().info(message);
        }
    }
}
