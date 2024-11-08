# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## Known Issues

#### Food

- Bundles not showing correct stew amount on vanilla clients ([#28](https://github.com/Team-Symphony/Harmony/issues/28))
  - Current workarounds: Install Harmony on the client or disable Stew and Soup Stacking.
- Stews and soups dont properly stack on the player's inventory while on creative mode on vanilla clients. ([#24](https://github.com/Team-Symphony/Harmony/issues/24))
    - Current workarounds: Install Harmony on the client or disable Stew and Soup Stacking (or change gamemode to another different from creative).

#### Combat

- Sometimes using /give command may create a copy of the given item that cant be picked up. This bug hasnt been able to be consistently reproduced and debugged yet.
    - Current workarounds: Disable the "Change items despawn time depending on difficulty" feature.

#### Mobs

- Parrots become invisible for vanilla clients if the player starts flying on creative mode with parrots on their shoulders until client reconnects or the parrots fly off of the player.
    - Current workaround: Install Harmony on the client or disable "Parrots dismount under fewer restrictions"

## [Unreleased]

### Added

#### Trans🏳️‍⚧️portation

- FEATURE: Elytra deactivates when going underwater or inside of lava (and automatically changes to swimming mode)
- FEATURE: Tridents with Riptide gain acceleration underwater (during its animation)
- FEATURE: Longer Riptide animation
- FEATURE: Tridents with riptide cant be used until riptide animation has finished
- FEATURE: Reduced water drag while using Riptide underwater

#### Exploration

- FEATURE: Eye of Ender can locate end cities

#### Redstone

- FEATURE: Removed turning off Redstone Lamp delay

### Deprecated

#### General

- MidnightLib will be replaced by a different config lib

## [0.0.1-alpha+mc1.21.3] - 2024-11-8

### Changed

#### General

- Ported to 1.21.3

## [0.0.1-alpha+mc1.21] - 2024-10-27

### Added

#### General

- Added MidnightLib as an embedded dependency.

#### Trans🏳️‍⚧️portation

- FEATURE: Mobs exit vehicles when taking damage
- FEATURE: Craftable Saddle (crafted with 3 leather, 2 string and 2 iron ingots)
- FEATURE: Horse armor decreases bucking chance on damage
- MELODY INTEGRATION: When Melody is installed and netherite horse armor is enabled, it will fully prevent the horse from bucking

#### Food

- FEATURE: Configurable stew and soup stack size (by default: 16)
- FEATURE: Glow berries give glowing effect for a brief time upon eating (by default: 10s)

#### Building

- FEATURE: Armor Stands can be equipped with arms using a stick, and can be removed with shears

#### Redstone

- FEATURE: Copper bulbs have a 1 tick delay again!

#### Potions

- FEATURE: Beacon effects apply to tamed pets

#### Combat

- FEATURE: Change items despawn time depending on difficulty (By default: 20  minutes on Easy, 10 in Normal and 5 on Hard)

#### Mobs

- FEATURE: Tamed wolves growl at nearby monsters
- FEATURE: Parrots dismount under fewer conditions (sneak and unsneak quickly to get them off of you)
- FEATURE: Mobs can spawn with mismatched armor materials.
- FEATURE: Husks drop sand when converting into zombies.
