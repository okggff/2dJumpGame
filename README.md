# Parkour — 2D Momentum-Based Platformer

A high-fidelity 2D parkour platformer built with Java and LibGDX. Navigate punishing
obstacle courses using a full suite of traversal abilities: wall jumps, dashes, and
gravity-shifting maneuvers. Every level rewards creative pathfinding and demands
mastery of momentum-based movement.

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