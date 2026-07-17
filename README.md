# Parkour — 2D Momentum-Based Platformer

A high-fidelity 2D parkour platformer built with Java and LibGDX. Navigate punishing
obstacle courses using a full suite of traversal abilities: wall jumps, dashes, and
gravity-shifting maneuvers. Every level rewards creative pathfinding and demands
mastery of momentum-based movement.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Architecture Overview](#architecture-overview)
- [Game Mechanics](#game-mechanics)
- [Controls](#controls)
- [Development](#development)
- [Testing](#testing)
- [Implementation Plan](#implementation-plan)

---

## Features

- Fluid, momentum-based movement with full acceleration and deceleration curves
- Variable jump height — tap for a short hop, hold for full height
- Coyote time and jump buffering for forgiving but skill-expressive controls
- Wall slide and wall jump — chain jumps up vertical corridors
- Directional dash with invincibility frames and cooldown
- Checkpoint and instant-respawn system with death counter
- Hand-crafted Tiled TMX levels
- Debug overlay (F3) showing hitboxes, velocity, and player state
- Sound effects and background music wired to game events

---

## Tech Stack

| Layer           | Technology                                      |
|-----------------|-------------------------------------------------|
| Language        | Java 25                                         |
| Build           | Gradle 9.6.1 (wrapper, cached after first run)  |
| Game Framework  | LibGDX 1.12.1                                   |
| ECS             | Ashley 1.7.4 (ships with LibGDX)                |
| Physics         | Custom kinematic AABB controller (no Box2D)     |
| Maps            | Tiled TMX via LibGDX TiledMap API               |
| UI              | LibGDX Scene2D                                  |
| Testing         | JUnit 5 + Mockito                               |

---

## Prerequisites

- **Java 25 JDK** installed and on `PATH`
  - Verify: `java --version`
- **No Gradle installation required** — the Gradle wrapper downloads and caches Gradle 9.6.1 on first run

---

## Getting Started

### 1. Clone the repository

```bash
git clone <repo-url>
cd 2dJumpGame
```

### 2. Build the project

The Gradle wrapper (`gradlew`) is already committed. On first run it downloads Gradle 9.6.1 automatically:

```bash
./gradlew build
```

### 3. Run the game

```bash
./gradlew :app:run
```

A 1280×720 window opens. Use the controls below to play.

### 4. Run tests

```bash
./gradlew :app:test
```

Test report: `app/build/reports/tests/test/index.html`

---

## Project Structure

```
2dJumpGame/
├── ext/
│   └── gradle-8.5/                  ← Gradle binary distribution (local)
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── gradlew / gradlew.bat
├── settings.gradle
├── build.gradle
├── assets/
│   ├── maps/                        ← Tiled .tmx level files
│   ├── textures/                    ← Sprite sheets and tile sets
│   ├── audio/                       ← SFX and music
│   └── skin/                        ← Scene2D UI skin
└── src/
    ├── main/java/com/parkour/
    │   ├── ParkourGame.java          ← LibGDX ApplicationAdapter entry point
    │   ├── DesktopLauncher.java      ← LWJGL3 desktop entry point
    │   ├── core/
    │   │   ├── math/                 ← Vec2, AABB, SweptResult, Sweep
    │   │   ├── events/               ← EventBus, GameEvent
    │   │   ├── assets/               ← AssetManager wrapper
    │   │   └── pool/                 ← ObjectPool helpers
    │   ├── engine/
    │   │   ├── ecs/                  ← Ashley component/system base classes
    │   │   ├── physics/              ← KinematicBody, PhysicsWorld, TileGrid
    │   │   ├── input/                ← InputHandler, GameAction, ActionMap
    │   │   ├── render/               ← RenderSystem, CameraController
    │   │   └── audio/                ← AudioManager
    │   └── game/
    │       ├── screens/              ← GameScreen, MenuScreen, PauseScreen
    │       ├── world/                ← World, LevelLoader (TMX)
    │       ├── entities/             ← PlayerFactory, HazardFactory
    │       ├── components/           ← All ECS components
    │       ├── systems/              ← All ECS systems
    │       ├── player/               ← PlayerStateMachine, all FSM states
    │       └── ui/                   ← HUD, DebugOverlay
    └── test/java/com/parkour/
        ├── math/                     ← Vec2Test, AABBTest, SweepTest
        ├── physics/                  ← KinematicBodyTest, PhysicsWorldTest
        └── player/                   ← StateMachineTest, MechanicsTest
```

---

## Architecture Overview

The project follows a **layered architecture** with three main layers:

```
Game Layer      →  Levels, Player, Hazards, UI
Engine Layer    →  ECS (Ashley), Physics, Rendering, Input, Audio
Core Layer      →  Math, Events, Assets, Config, Pooling
```

### Key Design Patterns

| Pattern                  | Usage                                                                 |
|--------------------------|-----------------------------------------------------------------------|
| **ECS (Ashley)**         | All game objects as component-bearing entities; logic in systems      |
| **Hierarchical FSM**     | Player: Idle/Run/Jump/Fall/WallSlide/WallJump/Dash states            |
| **Command**              | Input actions mapped to rebindable command objects                    |
| **Observer / EventBus**  | Loose coupling between physics, audio, UI, and game systems           |
| **Object Pool**          | Particles and effect objects recycled to avoid GC pressure            |
| **Fixed-timestep Loop**  | Physics at 120 Hz, render interpolated for smooth visuals             |

### Architecture Diagram

```
ParkourGame (ApplicationAdapter)
└── ScreenManager
    ├── MainMenuScreen
    ├── GameScreen
    │   ├── World
    │   │   ├── Ashley Engine (ECS)
    │   │   │   ├── InputSystem
    │   │   │   ├── PhysicsSystem  ──►  PhysicsWorld
    │   │   │   │                           └── KinematicBody × N
    │   │   │   │                               └── Swept AABB vs TileGrid
    │   │   │   └── RenderSystem
    │   │   └── LevelLoader (TMX)
    │   └── CameraController
    └── PauseScreen
```

---

## Game Mechanics

### Movement Physics

The player uses a **custom kinematic AABB controller** — fully hand-rolled, no Box2D.
This gives the tight, responsive feel that a rigid-body solver cannot match.

| Property              | Value        |
|-----------------------|--------------|
| Max run speed         | 320 px/s     |
| Ground acceleration   | 1800 px/s²   |
| Ground deceleration   | 2400 px/s²   |
| Air acceleration      | 1200 px/s²   |
| Gravity               | 2200 px/s²   |
| Max fall speed        | 900 px/s     |
| Physics timestep      | 1/120 s      |

### Jump

| Mechanic          | Detail                                                          |
|-------------------|-----------------------------------------------------------------|
| Variable height   | Hold = full apex (~180 px), tap = short hop (~80 px)           |
| Coyote time       | 100 ms grace window after walking off a ledge                  |
| Jump buffering    | Jump input stored 100 ms, consumed on next landing             |

### Wall Mechanics

| Mechanic    | Detail                                                                 |
|-------------|------------------------------------------------------------------------|
| Wall slide  | Press into wall while airborne — gravity ×0.3, max fall 120 px/s      |
| Wall jump   | Fixed impulse (-wallNormal×280, 520) px/s; 150 ms H-input lock        |

### Dash

- Instant velocity burst in any of 8 directions (600 px/s for 150 ms)
- Gravity suppressed during dash
- Invincibility frames active during dash
- Cooldown: 600 ms (resets on landing)

---

## Controls

| Action         | Keyboard               |
|----------------|------------------------|
| Move           | `A` / `D` or `←` / `→` |
| Jump           | `Space`                |
| Dash           | `Left Shift`           |
| Gravity Shift  | `E`                    |
| Pause          | `Escape`               |
| Debug Overlay  | `F3`                   |

Controls are fully rebindable via `ActionMap`.

---

## Development

### Asset hot-reload

Assets in `assets/` are read at runtime. Texture and map changes take effect on the
next level reload (press `R` in-game) without recompiling.

### Building a fat JAR

```bat
gradlew.bat jar
```

Output: `build/libs/parkour-1.0.jar` — runnable on any Java 25 JVM.

### Editing levels

1. Install [Tiled Map Editor](https://www.mapeditor.org/) (free)
2. Open any `.tmx` file in `assets/maps/`
3. The map uses three layers:
   - `collision` — solid tiles used by the physics engine
   - `visual` — decorative tile layer (not collidable)
   - `objects` — spawn points, hazards, checkpoints, exit triggers
4. Save and reload in-game with `R`

---

## Testing

```bat
# Run all tests
gradlew.bat test

# Run with detailed output
gradlew.bat test --info

# Run a specific class
gradlew.bat test --tests "com.parkour.math.AABBTest"
```

Test report: `build/reports/tests/test/index.html`

The test suite covers:
- `Vec2` and `AABB` math (overlap, sweep, tunneling edge cases)
- Kinematic body physics (gravity, floor, wall collision, no tunneling)
- Player FSM state transitions
- Parkour mechanics (coyote time, jump buffer, variable jump height)
- ECS system update logic

---

## Implementation Plan

### Task 1 — Bootstrap Gradle (local binary, no internet)
Generate wrapper from `ext/gradle-8.5/`, configure `distributionUrl` to local path,
create `settings.gradle` and `build.gradle` with LibGDX 1.12.1 + Ashley + JUnit 5.

### Task 2 — LibGDX Desktop Window
`DesktopLauncher` + `ParkourGame extends ApplicationAdapter`. 1280×720, 60 FPS,
dark background. `gradlew run` opens the window.

### Task 3 — Core Math (Vec2, AABB, Swept Collision)
Pure-Java `Vec2`, `AABB`, `SweptResult`, `Sweep.sweepAABB()`. Full JUnit 5 coverage:
overlap, no-overlap, swept hit at correct `t`, swept miss, tunneling.

### Task 4 — Kinematic Physics Engine
`TileGrid`, `KinematicBody`, `PhysicsWorld.step()`. Iterative swept AABB resolution,
`onGround` / `onWall` / `onCeiling` flags. No tunneling at max fall speed.

### Task 5 — Input System (Command Pattern)
`GameAction` enum, `InputHandler`, `ActionMap`. Rebindable, frame-accurate
`justPressed` / `held` / `justReleased` per action.

### Task 6 — Ashley ECS Skeleton + Fixed-Step Loop
Components: Transform, Velocity, PhysicsBody, Sprite, PlayerTag.
Systems: Input → Physics → Render. Fixed 120 Hz physics, interpolated render.

### Task 7 — Player FSM: Run, Jump, Coyote Time, Jump Buffer
`PlayerStateMachine` with Idle/Run/Jump/Fall states. Variable jump height,
coyote time (100 ms), jump buffering (100 ms).

### Task 8 — Wall Slide and Wall Jump
`WallSlideState` (gravity ×0.3, capped fall), `WallJumpState` (impulse + input lock).
Works under both normal and inverted gravity.

### Task 9 — Dash Ability
`DashState`: 8-direction burst, gravity suppressed, invincibility, afterimage trail.
600 ms cooldown, resets on landing.

### Task 10 — Tiled Level, Hazards, Checkpoints, HUD
TMX loader, hazard death + freeze-frame, checkpoint respawn, Scene2D HUD
(death counter, dash cooldown bar). Full playable loop.
