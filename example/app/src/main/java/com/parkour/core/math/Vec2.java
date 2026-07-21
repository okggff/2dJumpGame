package com.parkour.core.math;

/**
 * Immutable-style 2D float vector. Methods return new instances so they
 * compose cleanly without aliasing bugs. Mutable set() is provided for
 * performance-critical paths where object allocation must be avoided.
 */
public final class Vec2 {

    public float x;
    public float y;

    public Vec2() {
        this(0f, 0f);
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /** Copy constructor. */
    public Vec2(Vec2 other) {
        this(other.x, other.y);
    }

    // -----------------------------------------------------------------------
    // Mutating helpers (in-place, for hot paths)
    // -----------------------------------------------------------------------

    public Vec2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vec2 set(Vec2 other) {
        return set(other.x, other.y);
    }

    // -----------------------------------------------------------------------
    // Arithmetic — return new Vec2
    // -----------------------------------------------------------------------

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 add(float dx, float dy) {
        return new Vec2(x + dx, y + dy);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 sub(float dx, float dy) {
        return new Vec2(x - dx, y - dy);
    }

    public Vec2 scale(float s) {
        return new Vec2(x * s, y * s);
    }

    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    // -----------------------------------------------------------------------
    // Geometric queries
    // -----------------------------------------------------------------------

    public float dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public float lengthSq() {
        return x * x + y * y;
    }

    public float length() {
        return (float) Math.sqrt(lengthSq());
    }

    /**
     * Returns a unit vector, or (0,0) if this is the zero vector.
     */
    public Vec2 normalize() {
        float len = length();
        if (len < 1e-6f) return new Vec2(0f, 0f);
        return scale(1f / len);
    }

    public float distanceTo(Vec2 other) {
        return sub(other).length();
    }

    // -----------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------

    public boolean isZero() {
        return x == 0f && y == 0f;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vec2 other)) return false;
        return Float.compare(x, other.x) == 0 && Float.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Float.hashCode(x) + Float.hashCode(y);
    }

    @Override
    public String toString() {
        return "Vec2(" + x + ", " + y + ")";
    }
}
