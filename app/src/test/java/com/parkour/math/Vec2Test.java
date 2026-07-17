package com.parkour.math;

import com.parkour.core.math.Vec2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Vec2Test {

    private static final float EPSILON = 1e-5f;

    @Test
    void defaultConstructorIsZero() {
        Vec2 v = new Vec2();
        assertEquals(0f, v.x, EPSILON);
        assertEquals(0f, v.y, EPSILON);
    }

    @Test
    void addReturnsCorrectResult() {
        Vec2 result = new Vec2(1f, 2f).add(new Vec2(3f, 4f));
        assertEquals(4f, result.x, EPSILON);
        assertEquals(6f, result.y, EPSILON);
    }

    @Test
    void subReturnsCorrectResult() {
        Vec2 result = new Vec2(5f, 7f).sub(new Vec2(2f, 3f));
        assertEquals(3f, result.x, EPSILON);
        assertEquals(4f, result.y, EPSILON);
    }

    @Test
    void scaleMultipliesComponents() {
        Vec2 result = new Vec2(3f, 4f).scale(2f);
        assertEquals(6f, result.x, EPSILON);
        assertEquals(8f, result.y, EPSILON);
    }

    @Test
    void dotProductIsCorrect() {
        float dot = new Vec2(1f, 0f).dot(new Vec2(0f, 1f));
        assertEquals(0f, dot, EPSILON);  // perpendicular

        dot = new Vec2(3f, 4f).dot(new Vec2(3f, 4f));
        assertEquals(25f, dot, EPSILON); // self-dot = length²
    }

    @Test
    void lengthIsCorrect() {
        assertEquals(5f, new Vec2(3f, 4f).length(), EPSILON);
        assertEquals(0f, new Vec2(0f, 0f).length(), EPSILON);
    }

    @Test
    void normalizeProducesUnitVector() {
        Vec2 n = new Vec2(3f, 4f).normalize();
        assertEquals(1f, n.length(), EPSILON);
    }

    @Test
    void normalizeOfZeroVectorReturnsZero() {
        Vec2 n = new Vec2(0f, 0f).normalize();
        assertEquals(0f, n.x, EPSILON);
        assertEquals(0f, n.y, EPSILON);
    }

    @Test
    void negateFlipsSign() {
        Vec2 n = new Vec2(3f, -2f).negate();
        assertEquals(-3f, n.x, EPSILON);
        assertEquals(2f, n.y, EPSILON);
    }

    @Test
    void setMutatesInPlace() {
        Vec2 v = new Vec2();
        v.set(7f, 9f);
        assertEquals(7f, v.x, EPSILON);
        assertEquals(9f, v.y, EPSILON);
    }

    @Test
    void equalityIsValueBased() {
        assertEquals(new Vec2(1f, 2f), new Vec2(1f, 2f));
        assertNotEquals(new Vec2(1f, 2f), new Vec2(1f, 3f));
    }

    @Test
    void addDoesNotMutateOriginal() {
        Vec2 original = new Vec2(1f, 1f);
        original.add(new Vec2(5f, 5f));
        // add returns a new Vec2 — original must be unchanged
        assertEquals(1f, original.x, EPSILON);
        assertEquals(1f, original.y, EPSILON);
    }
}
