package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created on 2022/10/3.
 */
public class CircleTest extends G2dTest {

    @Test
    public void checkSerializable() {
        for (int circleIndex = 0; circleIndex < CIRCLE; circleIndex++) {
            Circle<Point> circle = nextCircle();
            assertEquals(circle, tryCopy(circle));
        }
    }

    @Test
    public void checkConvex() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        final Point point3 = new Point();
        for (int circleIndex = 0; circleIndex < CIRCLE; circleIndex++) {
            Circle<Point> circle = nextCircle();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                assertNotEquals(GeometryOperationResult.FAIL, circle.areaRandom(rc, point1));
                assertNotEquals(GeometryOperationResult.FAIL, circle.areaRandom(rc, point2));
                point3.setToLerp(point1, point2, rc.nextDouble());
                assertTrue(circle.test(point3));
            }
        }
    }

    @Test
    public void checkIntersectsCircle() {
        final Bound bound1 = new Bound();
        final Bound bound2 = new Bound();
        final Point point = new Point();
        for (int circleIndex = 0; circleIndex < CIRCLE; circleIndex++) {
            Circle<Point> circle1 = nextCircle();
            Circle<Point> circle2 = nextCircle();
            if (circle1.intersects(circle2)) {
                assertNotEquals(GeometryOperationResult.FAIL, circle1.smallestBound(bound1));
                assertNotEquals(GeometryOperationResult.FAIL, circle2.smallestBound(bound2));
                assertTrue(bound1.intersects(bound2));
            } else {
                assertNotEquals(GeometryOperationResult.FAIL, circle1.areaRandom(rc, point));
                assertFalse(circle2.test(point));
                assertNotEquals(GeometryOperationResult.FAIL, circle2.areaRandom(rc, point));
                assertFalse(circle1.test(point));
            }
        }
    }

    @Test
    public void checkIntersectsLine() {
        final Bound bound1 = new Bound();
        final Bound bound2 = new Bound();
        final Point point = new Point();
        for (int circleIndex = 0; circleIndex < CIRCLE; circleIndex++) {
            Circle<Point> circle = nextCircle();
            Line<Point> line = nextLine();
            if (circle.intersects(line)) {
                assertNotEquals(GeometryOperationResult.FAIL, circle.smallestBound(bound1));
                assertNotEquals(GeometryOperationResult.FAIL, line.smallestBound(bound2));
                assertTrue(bound1.intersects(bound2));
            } else {
                assertNotEquals(GeometryOperationResult.FAIL, line.areaRandom(rc, point));
                assertFalse(circle.test(point));
            }
        }
    }

    @Test
    public void checkCutLine() {
        final Line<Point> line2 = Line.points();
        final Point point = new Point();
        for (int circleIndex = 0; circleIndex < CIRCLE; circleIndex++) {
            Circle<Point> circle = nextCircle();
            Line<Point> line1 = nextLine();
            if (circle.cutLine(line1, line2)) {
                line2.areaRandom(rc, point);
                assertTrue(circle.test(point));
                assertEquals(0.0, line1.minimumDistance(point), Geometry.EPSILON);
            } else {
                assertNotEquals(GeometryOperationResult.FAIL, line1.areaRandom(rc, point));
                assertFalse(circle.test(point));
            }
        }
    }

    @Test
    public void checkCutHalfPlane() {
        final Line<Point> line = Line.points();
        final Point point = new Point();
        for (int circleIndex = 0; circleIndex < CIRCLE; circleIndex++) {
            Circle<Point> circle = nextCircle();
            HalfPlane halfPlane = nextHalfPlane();
            if (circle.cutHalfPlane(halfPlane, line)) {
                line.areaRandom(rc, point);
                assertTrue(circle.test(point));
                point.setToLerp(line.p1, line.p2, 1.0 + rc.nextDouble());
                assertFalse(circle.test(point));
                point.setToLerp(line.p2, line.p1, 1.0 + rc.nextDouble());
                assertFalse(circle.test(point));
            } else {
                assertNotEquals(GeometryOperationResult.FAIL, circle.areaRandom(rc, point));
                assertEquals(halfPlane.test(circle.o), halfPlane.test(point));
            }
        }
    }
}
