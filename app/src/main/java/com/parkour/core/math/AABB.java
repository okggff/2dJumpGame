package com.parkour.core.math;

/**
 * Axis-Aligned Bounding Box defined by min and max corners.
 * All methods are non-mutating unless noted.
 */
public final class AABB {

    /** Minimum corner (top-left in screen space, bottom-left in world space). */
    public float minX, minY;
    /** Maximum corner. */
    public float maxX, maxY;

    public AABB(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /** Construct from centre position and half-extents. */
    public static AABB fromCenter(float cx, float cy, float halfW, float halfH) {
        return new AABB(cx - halfW, cy - halfH, cx + halfW, cy + halfH);
    }

    /** Construct from top-left position and full size (pixel-style). */
    public static AABB fromPosition(float x, float y, float width, float height) {
        return new AABB(x, y, x + width, y + height);
    }

    // -----------------------------------------------------------------------
    // Geometric queries
    // -----------------------------------------------------------------------

    public float width()  { return maxX - minX; }
    public float height() { return maxY - minY; }
    public float centerX() { return (minX + maxX) * 0.5f; }
    public float centerY() { return (minY + maxY) * 0.5f; }

    /**
     * Returns true if this AABB overlaps {@code other}.
     * Touching edges (shared boundary) are NOT considered overlapping.
     */
    public boolean overlaps(AABB other) {
        return minX < other.maxX && maxX > other.minX
            && minY < other.maxY && maxY > other.minY;
    }

    /** Returns true if the point is strictly inside this AABB. */
    public boolean contains(float px, float py) {
        return px > minX && px < maxX && py > minY && py < maxY;
    }

    public boolean contains(Vec2 p) {
        return contains(p.x, p.y);
    }

    /**
     * Returns a new AABB expanded by {@code margin} on all sides.
     */
    public AABB expand(float margin) {
        return new AABB(minX - margin, minY - margin, maxX + margin, maxY + margin);
    }

    /**
     * Returns a new AABB translated by the given delta.
     */
    public AABB translate(float dx, float dy) {
        return new AABB(minX + dx, minY + dy, maxX + dx, maxY + dy);
    }

    public AABB translate(Vec2 delta) {
        return translate(delta.x, delta.y);
    }

    /**
     * Returns the Minkowski difference AABB used in swept tests:
     * the "sum" box that {@code other}'s centre must stay outside of for no collision.
     */
    public AABB minkowskiSum(AABB other) {
        return new AABB(
            minX - other.width()  * 0.5f,
            minY - other.height() * 0.5f,
            maxX + other.width()  * 0.5f,
            maxY + other.height() * 0.5f
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AABB o)) return false;
        return Float.compare(minX, o.minX) == 0 && Float.compare(minY, o.minY) == 0
            && Float.compare(maxX, o.maxX) == 0 && Float.compare(maxY, o.maxY) == 0;
    }

    @Override
    public int hashCode() {
        int h = Float.hashCode(minX);
        h = 31 * h + Float.hashCode(minY);
        h = 31 * h + Float.hashCode(maxX);
        h = 31 * h + Float.hashCode(maxY);
        return h;
    }

    @Override
    public String toString() {
        return "AABB[(" + minX + "," + minY + ")→(" + maxX + "," + maxY + ")]";
    }
}
