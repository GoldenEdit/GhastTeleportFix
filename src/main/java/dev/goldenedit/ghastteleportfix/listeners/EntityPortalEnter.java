package dev.goldenedit.ghastteleportfix.listeners;

import dev.goldenedit.ghastteleportfix.GhastTeleportFix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class EntityPortalEnter implements Listener {
    private final HashMap<String, Location> portalCache = new HashMap<>();
    private final HashSet<UUID> processingGhasts = new HashSet<>(); // New HashSet to track processing Ghasts
    private final GhastTeleportFix plugin;

    public EntityPortalEnter(GhastTeleportFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPortalEnterEvent(EntityPortalEnterEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Ghast) {
            UUID ghastUUID = entity.getUniqueId();

            // Skip the event if this Ghast is already being processed
            if (processingGhasts.contains(ghastUUID)) {
                return;
            }

            // Mark this Ghast as being processed
            processingGhasts.add(ghastUUID);
            plugin.debugLog("Ghast UUID: " + ghastUUID);
            if (plugin.cacheEnabled) {
                handleCacheEnabled(event);
            } else if (plugin.convertGhastsToDrops) {
                handleConvertGhastsToDrops(event);
            }

            // Unmark this Ghast, allowing it to be processed again in the future
            processingGhasts.remove(ghastUUID);
        }
    }

    private void handleCacheEnabled(EntityPortalEnterEvent event) {
        Location location = event.getLocation();
        Entity entity = event.getEntity();
        if (location.getWorld().getEnvironment() == World.Environment.NETHER) {
            String portalUUID = generatePortalUUID(location);
            plugin.debugLog("Ghast entered portal. Portal UUID: " + portalUUID);

            if (portalCache.containsKey(portalUUID)) {
                Location cachedLocation = portalCache.get(portalUUID);
                entity.teleport(cachedLocation);
                plugin.debugLog("Teleported Ghast to cached location: " + cachedLocation);
            } else {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    Location newLocation = entity.getLocation();
                    portalCache.put(portalUUID, newLocation);
                    plugin.debugLog("Cached new location for portal UUID: " + portalUUID);

                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        portalCache.remove(portalUUID);
                        plugin.debugLog("Removed cached location for portal UUID: " + portalUUID);
                    }, plugin.cacheDurationTicks);
                }, plugin.ghastTeleportWaitTicks);
            }
        }
    }

    private void handleConvertGhastsToDrops(EntityPortalEnterEvent event) {
        Location location = event.getLocation();
        World world = location.getWorld();
        if (world != null) {
            int ghastTears = (int) (Math.random() * plugin.maxTearDrop);
            world.dropItemNaturally(location, new ItemStack(Material.GHAST_TEAR, ghastTears));

            int gunPowder = (int) (Math.random() * plugin.maxGunpowderDrop);
            world.dropItemNaturally(location, new ItemStack(Material.GUNPOWDER, gunPowder));

            event.getEntity().remove();
            plugin.debugLog("Converted Ghast to drops at location: " + location);
        }
    }

    private String generatePortalUUID(Location location) {
        return location.getWorld().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }
}
