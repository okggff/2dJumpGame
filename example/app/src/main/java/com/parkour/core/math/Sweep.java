package com.parkour.core.math;

/**
 * Swept AABB collision utilities.
 *
 * <p>Algorithm: broad-phase Minkowski sum approach.
 * Expand the static obstacle by the half-extents of the moving body,
 * then cast a ray from the moving body's centre. This reduces the
 * swept AABB vs AABB test to a simple ray vs AABB test.</p>
 */
public final class Sweep {

    private Sweep() { /* static utility class */ }

    /**
     * Tests whether a moving AABB {@code a} travelling along {@code velocity}
     * (in units/step, i.e. velocity * dt) will hit static AABB {@code b}.
     *
     * @param a        the moving body's current AABB
     * @param velocity displacement vector for this step (vel * dt)
     * @param b        the static obstacle AABB
     * @return a {@link SweptResult} with time-of-impact {@code t} ∈ [0, 1]
     *         and the collision normal. Returns {@link SweptResult#NONE} if
     *         no collision occurs within this step.
     */
    public static SweptResult sweepAABB(AABB a, Vec2 velocity, AABB b) {
        // Already overlapping — treat as t=0 push-out (handled by caller)
        if (a.overlaps(b)) {
            return new SweptResult(0f, computePushOutNormal(a, b));
        }

        // Expand b by half-extents of a (Minkowski sum), ray-cast from a's centre
        float halfW = a.width()  * 0.5f;
        float halfH = a.height() * 0.5f;

        AABB expanded = new AABB(
            b.minX - halfW,
            b.minY - halfH,
            b.maxX + halfW,
            b.maxY + halfH
        );

        float cx = a.centerX();
        float cy = a.centerY();

        return rayVsAABB(cx, cy, velocity, expanded);
    }

    // -----------------------------------------------------------------------
    // Ray vs AABB (slab method)
    // -----------------------------------------------------------------------

    static SweptResult rayVsAABB(float ox, float oy, Vec2 dir, AABB box) {
        float tMinX, tMaxX, tMinY, tMaxY;

        if (Math.abs(dir.x) < 1e-6f) {
            if (ox <= box.minX || ox >= box.maxX) return SweptResult.NONE;
            tMinX = Float.NEGATIVE_INFINITY;
            tMaxX = Float.POSITIVE_INFINITY;
        } else {
            float invDx = 1f / dir.x;
            tMinX = (box.minX - ox) * invDx;
            tMaxX = (box.maxX - ox) * invDx;
            if (tMinX > tMaxX) { float tmp = tMinX; tMinX = tMaxX; tMaxX = tmp; }
        }

        if (Math.abs(dir.y) < 1e-6f) {
            if (oy <= box.minY || oy >= box.maxY) return SweptResult.NONE;
            tMinY = Float.NEGATIVE_INFINITY;
            tMaxY = Float.POSITIVE_INFINITY;
        } else {
            float invDy = 1f / dir.y;
            tMinY = (box.minY - oy) * invDy;
            tMaxY = (box.maxY - oy) * invDy;
            if (tMinY > tMaxY) { float tmp = tMinY; tMinY = tMaxY; tMaxY = tmp; }
        }

        float tEnter = Math.max(tMinX, tMinY);
        float tExit  = Math.min(tMaxX, tMaxY);

        if (tExit <= tEnter || tEnter >= 1f || tExit <= 0f) return SweptResult.NONE;

        float t = Math.max(tEnter, 0f);

        Vec2 normal;
        if (tMinX > tMinY) {
            normal = dir.x < 0 ? new Vec2(1f, 0f) : new Vec2(-1f, 0f);
        } else {
            normal = dir.y < 0 ? new Vec2(0f, 1f) : new Vec2(0f, -1f);
        }

        return new SweptResult(t, normal);
    }

    // -----------------------------------------------------------------------
    // Push-out normal for already-overlapping case
    // -----------------------------------------------------------------------

    private static Vec2 computePushOutNormal(AABB a, AABB b) {
        float overlapX = Math.min(a.maxX, b.maxX) - Math.max(a.minX, b.minX);
        float overlapY = Math.min(a.maxY, b.maxY) - Math.max(a.minY, b.minY);

        if (overlapX < overlapY) {
            return a.centerX() < b.centerX() ? new Vec2(-1f, 0f) : new Vec2(1f, 0f);
        } else {
            return a.centerY() < b.centerY() ? new Vec2(0f, -1f) : new Vec2(0f, 1f);
        }
    }
}
