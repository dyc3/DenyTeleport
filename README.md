# DenyTeleport
A Minecraft bukkit server plugin to deny people from teleporting into certain WorldGuard regions.

# Usage

On first run, the plugin will save the default config.

## Config

```yaml
regions:
  - example
message: &bYou are not allowed to teleport to that location.
```

## Commands

`/reload` - Reloads the config.

## Permissions

`DenyTeleport.Reload` - Allows the user to reload the config.

`DenyTeleport.override.all` - Allows the user to teleport to all denied regions anyway.

`DenyTeleport.override.REGION_ID` - Allows the user to teleport to the region REGION_ID.

# Acknowledgements

This plugin was created based on the decompiled source of [ZeeZee's DenyTP](http://www.spigotmc.org/resources/denytp.1660/).

This plugin was commissioned by [Terrapinia MC](http://terrapinia.net).
