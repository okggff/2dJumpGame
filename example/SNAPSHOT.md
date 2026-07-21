# Example Snapshot — Tasks 1–3

This directory is a complete snapshot of the working project state after
Tasks 1–3 were completed. It mirrors the full directory structure of the
live project root.

## Purpose

- Reference when starting fresh or after a destructive reset
- Shows the exact file content that produces a working `./gradlew :app:test` (36/36 passing)
- Documents the resolved toolchain: **Java 25 + Gradle 9.6.1 + LibGDX 1.12.1**

## Contents

```
example/
├── .gitattributes               ← line-ending rules (gradlew = lf, *.bat = crlf)
├── .gitignore                   ← full Java/Gradle/IDE ignore rules
├── README.md                    ← full project README with issues log
├── SNAPSHOT.md                  ← this file
├── settings.gradle              ← root project name + app subproject include
├── gradlew                      ← POSIX wrapper script (eol=lf)
├── gradlew.bat                  ← Windows wrapper script
├── gradle/
│   ├── libs.versions.toml       ← version catalog: junit-jupiter, libgdx, ashley
│   └── wrapper/
│       ├── gradle-wrapper.jar   ← wrapper bootstrap JAR (binary)
│       └── gradle-wrapper.properties  ← points to Gradle 9.6.1
└── app/
    ├── build.gradle             ← LibGDX + Ashley + JUnit 5 deps, mainClass
    └── src/
        ├── main/java/com/parkour/
        │   ├── ParkourGame.java          ← ApplicationAdapter, dark background
        │   ├── DesktopLauncher.java      ← LWJGL3 1280×720 window config
        │   └── core/math/
        │       ├── Vec2.java             ← 2D float vector, immutable-style
        │       ├── AABB.java             ← axis-aligned bounding box
        │       ├── SweptResult.java      ← time-of-impact + normal value object
        │       └── Sweep.java            ← swept AABB vs AABB, slab method
        └── test/java/com/parkour/math/
            ├── Vec2Test.java             ← 12 tests, 100% method coverage
            ├── AABBTest.java             ← 13 tests, all overlap/contain/translate cases
            └── SweepTest.java            ← 9 tests incl. tunneling, zero-vel, adjacent

```

## Known Issues (see README.md for full details)

| # | Issue | Fix |
|---|-------|-----|
| 1 | PowerShell garbles agent terminal input | Switch Kiro terminal to bash |
| 2 | `gradle init --overwrite` flag does not exist | Remove the flag |
| 3 | Gradle 8.5 / 8.11.1 cannot run on Java 25 | Use Gradle 9.6.1+ |
| 4 | Can't zip `ext/gradle-8.5/` — daemon holds file locks | Not needed; use Gradle CDN |
| 5 | `SweepTest` diagonal case asserted wrong normal | Replace with axis-aligned test |
| 6 | Incremental test cache doesn't pick up edits | Use `./gradlew :app:clean :app:test` |

## Note on `arua-tuff-ultra-tuff/`

A folder named `arua-tuff-ultra-tuff/` exists in the project root. It is a dead
artifact from the first `gradle init` attempt when the terminal was garbling input
and produced a mangled project name. It is safe to delete.
