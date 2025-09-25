package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.junit.Test;

import static org.junit.Assert.*;

@CodeHistory(date = "2022/6/29", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class BoundTest extends G2dTest {

    @Test
    public void checkSerializable() {
        for (int boundIndex = 0; boundIndex < BOUND; boundIndex++) {
            Bound bound = nextBound();
            assertEquals(bound, tryCopy(bound));
        }
    }

    @Test
    public void checkCutLine() {
        final Line<Point> line2 = Line.points();
        final Point point = new Point();
        for (int boundIndex = 0; boundIndex < BOUND; boundIndex++) {
            Bound bound = nextBound();
            Line<Point> line1 = nextLine();
            if (bound.cutLine(line1, line2)) {
                line2.areaRandom(rc, point);
                assertTrue(bound.test(point));
                assertEquals(0.0, line1.minimumDistance(point), Geometry.EPSILON);
            } else {
                assertNotEquals(GeometryOperationResult.FAIL, line1.areaRandom(rc, point));
                assertFalse(bound.test(point));
            }
        }
    }

    @Test
    public void checkCutHalfPlane() {
        final Line<Point> line = Line.points();
        final Point point = new Point();
        for (int boundIndex = 0; boundIndex < BOUND; boundIndex++) {
            Bound bound = nextBound();
            HalfPlane halfPlane = nextHalfPlane();
            if (bound.cutHalfPlane(halfPlane, line)) {
                line.areaRandom(rc, point);
                assertTrue(bound.test(point));
                point.setToLerp(line.p1, line.p2, 1.0 + rc.nextDouble());
                assertFalse(bound.test(point));
                point.setToLerp(line.p2, line.p1, 1.0 + rc.nextDouble());
                assertFalse(bound.test(point));
            } else {
                assertNotEquals(GeometryOperationResult.FAIL, bound.areaRandom(rc, point));
                boolean side = halfPlane.test(point);
                for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                    assertNotEquals(GeometryOperationResult.FAIL, bound.areaRandom(rc, point));
                    assertEquals(side, halfPlane.test(point));
                }
            }
        }
    }

    @Test
    public void checkIncludePoint1() {
        final Point[] array = new Point[POINT];
        final Point point1 = new Point();
        for (int boundIndex = 0; boundIndex < BOUND; boundIndex++) {
            Bound bound = new Bound();
            bound.invalidate();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                array[pointIndex] = point0;
                bound.includePoint(point0);
            }
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                point1.setToLerp(rc.select(array), rc.select(array), rc.nextDouble());
                assertTrue(bound.test(point1));
            }
        }
    }

    @Test
    public void checkIncludePoint2() {
        final Point[] array = new Point[POINT];
        final Point point1 = new Point();
        for (int boundIndex = 0; boundIndex < BOUND; boundIndex++) {
            Bound bound = new Bound();
            bound.invalidate();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                array[pointIndex] = point0;
                bound.includePoint(point0);
            }
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                (new Triangle<>(rc.select(array), rc.select(array), rc.select(array))).areaRandom(rc, point1);
                assertTrue(bound.test(point1));
            }
        }
    }

    @Test
    public void checkIncludeCircle1() {
        final Circle<?>[] array = new Circle[POINT];
        for (int boundIndex = 0; boundIndex < BOUND; boundIndex++) {
            Bound bound = new Bound();
            bound.invalidate();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Circle<Point> circle = nextCircle();
                array[pointIndex] = circle;
                bound.includeCircle(circle);
            }
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                assertTrue(bound.test(array[pointIndex].o));
            }
        }
    }

    @Test
    public void checkIncludeCircle2() {
        final Circle<?>[] array = new Circle[POINT];
        for (int boundIndex = 0; boundIndex < BOUND; boundIndex++) {
            Bound bound = new Bound();
            bound.invalidate();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Circle<Point> circle = nextCircle();
                array[pointIndex] = circle;
                circle.includedInto(bound);
            }
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                assertTrue(bound.test(array[pointIndex].o));
            }
        }
    }
}
