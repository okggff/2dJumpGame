package com.parkour.core.math;

/**
 * Result of a swept AABB collision test.
 *
 * <ul>
 *   <li>{@code t} — normalised time of impact in [0, 1].
 *       {@code t = 0} means already overlapping at start.
 *       {@code t = 1} means no collision occurred this step.</li>
 *   <li>{@code normal} — collision surface normal pointing away from the obstacle,
 *       or (0, 0) when there is no collision ({@code t == 1}).</li>
 * </ul>
 */
public final class SweptResult {

    /** Sentinel indicating no collision this step. */
    public static final SweptResult NONE = new SweptResult(1f, new Vec2(0f, 0f));

    public final float t;
    public final Vec2  normal;

    public SweptResult(float t, Vec2 normal) {
        this.t      = t;
        this.normal = normal;
    }

    public boolean hasCollision() {
        return t < 1f;
    }

    @Override
    public String toString() {
        return "SweptResult{t=" + t + ", normal=" + normal + "}";
    }
}
