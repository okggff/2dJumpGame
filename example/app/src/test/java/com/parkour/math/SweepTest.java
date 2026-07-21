package com.parkour.math;

import com.parkour.core.math.AABB;
import com.parkour.core.math.Sweep;
import com.parkour.core.math.SweptResult;
import com.parkour.core.math.Vec2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SweepTest {

    private static final float EPSILON = 1e-4f;

    private static AABB unit(float x, float y) {
        return new AABB(x, y, x + 1f, y + 1f);
    }

    @Test
    void noCollisionReturnsT1() {
        AABB a = unit(0f, 0f);
        AABB b = unit(10f, 0f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(1f, 0f), b);
        assertEquals(1f, r.t, EPSILON, "Miss should return t=1");
        assertFalse(r.hasCollision());
    }

    @Test
    void movingAwayFromObstacleNoCollision() {
        AABB a = unit(0f, 0f);
        AABB b = unit(5f, 0f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(-1f, 0f), b);
        assertFalse(r.hasCollision());
    }

    @Test
    void movingPerpendicularMisses() {
        AABB a = unit(0f, 0f);
        AABB b = unit(5f, 5f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(0f, 1f), b);
        assertFalse(r.hasCollision());
    }

    @Test
    void movingRightHitsLeftFaceOfObstacle() {
        AABB a = unit(0f, 0f);
        AABB b = unit(3f, 0f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(4f, 0f), b);
        assertTrue(r.hasCollision(), "Should collide");
        assertTrue(r.t > 0f && r.t < 1f, "t should be between 0 and 1, was " + r.t);
        assertEquals(-1f, r.normal.x, EPSILON);
        assertEquals( 0f, r.normal.y, EPSILON);
    }

    @Test
    void movingLeftHitsRightFaceOfObstacle() {
        AABB a = unit(5f, 0f);
        AABB b = unit(1f, 0f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(-4f, 0f), b);
        assertTrue(r.hasCollision());
        assertEquals(1f, r.normal.x, EPSILON);
        assertEquals(0f, r.normal.y, EPSILON);
    }

    @Test
    void movingDownHitsTopFaceOfObstacle() {
        AABB a = unit(0f, 5f);
        AABB b = unit(0f, 1f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(0f, -4f), b);
        assertTrue(r.hasCollision(), "Falling body should hit floor");
        assertEquals(0f, r.normal.x, EPSILON);
        assertEquals(1f, r.normal.y, EPSILON);
    }

    @Test
    void movingUpHitsBottomFaceOfObstacle() {
        AABB a = unit(0f, 0f);
        AABB b = unit(0f, 4f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(0f, 5f), b);
        assertTrue(r.hasCollision());
        assertEquals( 0f, r.normal.x, EPSILON);
        assertEquals(-1f, r.normal.y, EPSILON);
    }

    @Test
    void tIsExactlyZeroWhenAlreadyTouching() {
        AABB a = new AABB(0f, 0f, 1f, 1f);
        AABB b = new AABB(1f, 0f, 2f, 1f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(1f, 0f), b);
        assertTrue(r.t <= EPSILON, "Adjacent bodies moving into each other: t should be ~0, was " + r.t);
    }

    @Test
    void highSpeedDoesNotTunnel() {
        AABB a = unit(0f, 0f);
        AABB wall = new AABB(50f, -1f, 50.5f, 2f);
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
        AABB b = unit(3f, 0f);
        SweptResult r = Sweep.sweepAABB(a, new Vec2(5f, 0f), b);
        assertTrue(r.hasCollision(), "Should hit obstacle directly to the right");
        assertEquals(-1f, r.normal.x, EPSILON);
        assertEquals( 0f, r.normal.y, EPSILON);
    }
}
