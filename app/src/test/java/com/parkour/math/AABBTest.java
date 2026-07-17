package com.parkour.math;

import com.parkour.core.math.AABB;
import com.parkour.core.math.Vec2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AABBTest {

    private static final float EPSILON = 1e-5f;

    // Helper: 2x2 box at origin
    private static AABB box(float x, float y) {
        return new AABB(x, y, x + 2f, y + 2f);
    }

    // -----------------------------------------------------------------------
    // Construction helpers
    // -----------------------------------------------------------------------

    @Test
    void fromCenterConstructsCorrectly() {
        AABB b = AABB.fromCenter(0f, 0f, 1f, 1f);
        assertEquals(-1f, b.minX, EPSILON);
        assertEquals(-1f, b.minY, EPSILON);
        assertEquals( 1f, b.maxX, EPSILON);
        assertEquals( 1f, b.maxY, EPSILON);
    }

    @Test
    void widthAndHeightAreCorrect() {
        AABB b = new AABB(1f, 2f, 5f, 8f);
        assertEquals(4f, b.width(),  EPSILON);
        assertEquals(6f, b.height(), EPSILON);
    }

    @Test
    void centerIsCorrect() {
        AABB b = new AABB(0f, 0f, 4f, 6f);
        assertEquals(2f, b.centerX(), EPSILON);
        assertEquals(3f, b.centerY(), EPSILON);
    }

    // -----------------------------------------------------------------------
    // overlaps()
    // -----------------------------------------------------------------------

    @Test
    void clearlyOverlappingBoxes() {
        assertTrue(box(0f, 0f).overlaps(box(1f, 1f)));
    }

    @Test
    void nonOverlappingBoxesSeparatedOnX() {
        assertFalse(box(0f, 0f).overlaps(box(3f, 0f)));  // gap between x=2 and x=3
    }

    @Test
    void nonOverlappingBoxesSeparatedOnY() {
        assertFalse(box(0f, 0f).overlaps(box(0f, 3f)));
    }

    @Test
    void touchingEdgesAreNotOverlapping() {
        // box A: [0,2] on x — box B: [2,4] on x — they share the x=2 edge only
        AABB a = new AABB(0f, 0f, 2f, 2f);
        AABB b = new AABB(2f, 0f, 4f, 2f);
        assertFalse(a.overlaps(b), "Touching edges should not count as overlapping");
    }

    @Test
    void containedBoxOverlaps() {
        AABB outer = new AABB(0f, 0f, 10f, 10f);
        AABB inner = new AABB(2f, 2f, 5f, 5f);
        assertTrue(outer.overlaps(inner));
        assertTrue(inner.overlaps(outer));
    }

    // -----------------------------------------------------------------------
    // contains(Vec2)
    // -----------------------------------------------------------------------

    @Test
    void containsPointInsideBox() {
        assertTrue(new AABB(0f, 0f, 4f, 4f).contains(2f, 2f));
    }

    @Test
    void doesNotContainPointOnEdge() {
        assertFalse(new AABB(0f, 0f, 4f, 4f).contains(0f, 2f));
    }

    @Test
    void doesNotContainPointOutside() {
        assertFalse(new AABB(0f, 0f, 4f, 4f).contains(5f, 2f));
    }

    // -----------------------------------------------------------------------
    // translate()
    // -----------------------------------------------------------------------

    @Test
    void translateShiftsAllCorners() {
        AABB original = new AABB(1f, 1f, 3f, 3f);
        AABB moved    = original.translate(2f, -1f);
        assertEquals(3f, moved.minX, EPSILON);
        assertEquals(0f, moved.minY, EPSILON);
        assertEquals(5f, moved.maxX, EPSILON);
        assertEquals(2f, moved.maxY, EPSILON);
    }

    @Test
    void translateDoesNotMutateOriginal() {
        AABB original = new AABB(0f, 0f, 2f, 2f);
        original.translate(5f, 5f);
        assertEquals(0f, original.minX, EPSILON); // must be unchanged
    }
}
