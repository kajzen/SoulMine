# SoulMine

A 2D RPG game with a custom-built game engine, inspired by Soul Knight and Minecraft.

Built from scratch in Java as a university project at CTU FEL — the engine handles rendering, physics, collision, and I/O, while the game layer implements the actual gameplay on top of it.

## Tech Stack

- **Java 21** + **JavaFX**
- **Maven**
- Custom 2D engine (no third-party game frameworks)

## Architecture

```
cz.cvut.fel.pjv/
├── engine/
│   ├── graphics/     # Rendering, animation
│   ├── physics/      # Collision detection
│   ├── io/           # Input handling, level loading, serialization
│   └── model/        # Base entity and game object classes
└── game/
    ├── controller/   # Game loop, crafting system
    ├── model/        # Heroes, enemies, items, world tiles
    └── view/         # Game UI and scenes
```

## Gameplay

Dungeon-crawler RPG with crafting mechanics. Fight through levels, collect items, craft gear, and reach the portal.

**Enemies:** Witch · Enderman · Ifrit  
**Items:** Weapons, materials, crafting ingredients  
**World:** Procedural levels with lava tiles, crafting tables, and portals

## Controls

| Key | Action |
|-----|--------|
| `WASD` | Move |
| `Left Mouse Button` | Attack |
| `E` | Interact (loot, craft, NPC) |
| `SPACE` | Use item / Confirm |
| `ESC` | Pause |

## Getting Started

Requirements: Java 21+, Maven

```bash
git clone https://github.com/kajzen/SoulMine.git
cd SoulMine
mvn clean javafx:run
```

## Author

Mykhailo Sydorov — [CTU FEL](https://fel.cvut.cz), Software & Information Technology
