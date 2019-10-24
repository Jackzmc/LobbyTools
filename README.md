# LobbyTools [![Build Status](https://ci.jackz.me/job/LobbyTools/badge/icon)](https://ci.jackz.me/job/LobbyTools/)
A group of tools & utilities for a bungeecord lobby server, complete with: 
* Parkour Regions
* Auto Teleporting back to spawn
* No fall damage
* Customizable messages
* Gadgets
* Server Selector
* Player Hiders
* & More to come

### Notice
This is currently in development at the moment, and is prone to errors and missing features. 
Please be patient. If you want a feature, please make a github issue asking for feature.

Feel free to contribute, make issues, or fork if you wish.

If you want to use development builds you can download a build from: 
https://ci.jackz.me/job/LobbyTools/ or [Download the latest directly](https://ci.jackz.me/job/LobbyTools/lastSuccessfulBuild/artifact/target/LobbyTools-1.0-SNAPSHOT.jar)

### Requirements

This plugin will run fine without any other plugins, but some functionality will be missing. This includes:

* Parkour Regions (Requires **WorldGuard**)
* Join ActionBar Messages (Requires **TTA**)

## Commands

Main: /lobbytools, /lt, /lb
* **/lt help** - self-explanatory
* **/lt parkour** <create/list/remove> - creates parkour regions
* **/lt reload** - reloads the config.yml

## Permissions

* lobbytools.command - access to /lobbytools
* lobbytools.command.reload - access to /lt reload
* lobbytools.command.parkour - access to /lt parkour
* lobbytools.lobby.flight - enable flight in lobby
* lobbytools.bypass.lobby - bypass lobby join actions (clearing inventory, etc)
* lobbytools.bypass.inventory - bypass lobby items (anti-item movement, etc)
* lobbytools.bypass.hide - players with this permission cant be hidden


