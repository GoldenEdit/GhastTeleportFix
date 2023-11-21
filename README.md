# GhastTeleportFix


## About

This is a Minecraft plugin that optimizes Ghast's teleportation through Nether Portals due to their large hitboxes.

Could be used for servers that want to:

- Make it easier to farm Ghasts
- Disable Ghast farms (all configurable)
- Optimize Ghast farms



## Setup & Installation

The plugin is made for `1.19.2+`

1. Download the latest release from the [Releases](https://github.com/VanillaPlusNet/GhastTeleportFix/releases) tab
2. Drag it into your `/plugins` folder
3. Restart/reload your server
4. Work on the config.yml
5. `/reloadghastconfig`

## Config


```yml
cache:
  cache-enabled: false # Enable portal link caching
  cache-duration-seconds: 3600  # How long to hold a link cache for
  ghast-teleport-wait-seconds: 1  # How long to wait in seconds before caching location sent to (time for a Ghast to be successfully sent through to the other side)

drops:
  convert-ghasts-to-drops: false # Converting Ghasts to their drops causes less server lag due to drops having much smaller hitboxes
  max-tear-drop: 1 # Default max number of tears (without looting) For looting set to 4
  max-gunpowder-drop: 2 # Default max number of gunpowder (without looting) For looting set to 5

prevent-ghasts-using-portals: false # Stop Ghast portal farms from working
debug: false  # Toggle debug messages
```

- You can either use the cache feature OR drops feature OR prevent ghasts using portals feature (only one of them):
  - **Cache:** Does not work as well as the drops feature (at improving server performance), but it keeps vanilla behaviour. (`cache-enabled: true`).
    - To put it simply, it creates a link between the nether portal block that the Ghast entered, and where it was sent in the overworld, then uses that link when future Ghasts enter that nether portal block, instead of the performance-expensive search algorithms.
   
  - **Drops:** Massively improves server performance, but completely removing the need for the large hitboxed entity (Ghast) from going through the portal. (`drops-enabled: true`).
    - When the Ghast touches a portal in the nether, it is converted to drops (randomised, decided in the config `max-tear-drop: x` `max-gunpowder-drop: x`). These items are sent through the portal (much smaller hitboxes, less lag).
   
  - **Disable Ghast farms completely:** (`prevent-ghasts-using-portals: true`)

## Commands

`/reloadghastconfig` | `ghastteleportfix.reload` - Reload the config


