package dev.goldenedit.ghastteleportfix.listeners;

import dev.goldenedit.ghastteleportfix.GhastTeleportFix;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

public class EntityPortal implements Listener {
    private final GhastTeleportFix plugin;
    public EntityPortal(GhastTeleportFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPortalEvent(EntityPortalEvent event){
        if (plugin.preventGhastsUsingPortals) event.setCancelled(true);
    }
}
