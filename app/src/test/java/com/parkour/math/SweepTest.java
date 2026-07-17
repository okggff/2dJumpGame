package com.parkour.math;

import com.parkour.core.math.AABB;
import com.parkour.core.math.Sweep;
import com.parkour.core.math.SweptResult;
import com.parkour.core.math.Vec2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SweepTest {

    private static final float EPSILON = 1e-4f;

    // Helper: 1x1 unit AABB
    private static AABB unit(float x, float y) {
        return new AABB(x, y, x + 1f, y + 1f);
    }

    // -----------------------------------------------------------------------
    // No collision — NONE sentinel returned
    // -----------------------------------------------------------------------

    @Test
    void noCollisionReturnsT1() {
        AABB a = unit(0f, 0f);
        AABB b = unit(10f, 0f);   // far to the right
        SweptResult r = Sweep.sweepAABB(a, new Vec2(1f, 0f), b);
        assertEquals(1f, r.t, EPSILON, "Miss should return t=1");
        assertFalse(r.hasCollision());
    }

    @Test
    void movingAwayFromObstacleNoCollision() {
        AABB a = unit(0f, 0f);
        AABB b = unit(5f, 0f);
        // Moving left (away from b which is on the right)
        SweptResult r = Sweep.sweepAABB(a, new Vec2(-1f, 0f), b);
        assertFalse(r.hasCollision());
    }

    @Test
    void movingPerpendicularMisses() {
        AABB a = unit(0f, 0f);
        AABB b = unit(5f, 5f);  // diagonal — no axis-aligned hit
        SweptResult r = Sweep.sweepAABB(a, new Vec2(0f, 1f), b);
        assertFalse(r.hasCollision());
    }

    // -----------------------------------------------------------------------
    // Hit — correct t and normal
    // -----------------------------------------------------------------------

    @Test
    void movingRightHitsLeftFaceOfObstacle() {
        // a at x=0..1, b at x=3..4 — moving right by 4 units this step
        AABB a = unit(0f, 0f);
        AABB b = unit(3f, 0f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(4f, 0f), b);

        assertTrue(r.hasCollision(), "Should collide");
        assertTrue(r.t > 0f && r.t < 1f, "t should be between 0 and 1, was " + r.t);
        // Normal should point left (away from b, back at a)
        assertEquals(-1f, r.normal.x, EPSILON);
        assertEquals( 0f, r.normal.y, EPSILON);
    }

    @Test
    void movingLeftHitsRightFaceOfObstacle() {
        AABB a = unit(5f, 0f);
        AABB b = unit(1f, 0f);  // obstacle is to the left
        SweptResult r = Sweep.sweepAABB(a, new Vec2(-4f, 0f), b);

        assertTrue(r.hasCollision());
        assertEquals(1f, r.normal.x, EPSILON);   // normal points right
        assertEquals(0f, r.normal.y, EPSILON);
    }

    @Test
    void movingDownHitsTopFaceOfObstacle() {
        // a above b, falling down
        AABB a = unit(0f, 5f);
        AABB b = unit(0f, 1f);  // floor tile below
        SweptResult r = Sweep.sweepAABB(a, new Vec2(0f, -4f), b);

        assertTrue(r.hasCollision(), "Falling body should hit floor");
        assertEquals(0f, r.normal.x, EPSILON);
        assertEquals(1f, r.normal.y, EPSILON);   // normal points up
    }

    @Test
    void movingUpHitsBottomFaceOfObstacle() {
        AABB a = unit(0f, 0f);
        AABB b = unit(0f, 4f);  // ceiling above
        SweptResult r = Sweep.sweepAABB(a, new Vec2(0f, 5f), b);

        assertTrue(r.hasCollision());
        assertEquals( 0f, r.normal.x, EPSILON);
        assertEquals(-1f, r.normal.y, EPSILON);  // normal points down
    }

    // -----------------------------------------------------------------------
    // Edge cases
    // -----------------------------------------------------------------------

    @Test
    void tIsExactlyZeroWhenAlreadyTouching() {
        // a is exactly adjacent to b (touching, not overlapping)
        AABB a = new AABB(0f, 0f, 1f, 1f);
        AABB b = new AABB(1f, 0f, 2f, 1f);  // share the x=1 edge
        // Moving into b
        SweptResult r = Sweep.sweepAABB(a, new Vec2(1f, 0f), b);
        // Should detect collision at or very near t=0
        assertTrue(r.t <= EPSILON, "Adjacent bodies moving into each other: t should be ~0, was " + r.t);
    }

    @Test
    void highSpeedDoesNotTunnel() {
        // A fast-moving body that would pass completely through a thin wall in one step
        // The swept test must still detect the collision
        AABB a = unit(0f, 0f);                    // 1x1 body at origin
        AABB wall = new AABB(50f, -1f, 50.5f, 2f); // thin wall 0.5 units wide, far away
        SweptResult r = Sweep.sweepAABB(a, new Vec2(100f, 0f), wall);

        assertTrue(r.hasCollision(), "High-speed body must not tunnel through thin wall");
        assertTrue(r.t > 0f && r.t < 1f);
    }

    @Test
    void zeroVelocityNonOverlappingReturnsNone() {
        AABB a = unit(0f, 0f);
        AABB b = unit(5f, 5f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(0f, 0f), b);
        assertFalse(r.hasCollision());
    }

    @Test
    void diagonalMovementHitsCloserAxisFirst() {
        // a: [0,1]x[0,1]   b: [3,4]x[0,1]
        // Pure horizontal gap of 2 units, same Y band — moving right hits the left face
        AABB a = unit(0f, 0f);
        AABB b = unit(3f, 0f);  // aligned on Y, gap of 2 on X
        SweptResult r = Sweep.sweepAABB(a, new Vec2(5f, 0f), b);
        assertTrue(r.hasCollision(), "Should hit obstacle directly to the right");
        assertEquals(-1f, r.normal.x, EPSILON);  // normal points left (away from b)
        assertEquals( 0f, r.normal.y, EPSILON);
    }
}
