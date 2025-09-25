package mujica.math.geometry.g2d;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created on 2022/10/10.
 */
public class LineTest extends G2dTest {

    @Test
    public void testSerializable() {
        for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
            Line<Point> line = nextLine();
            assertEquals(line, tryCopy(line));
        }
    }

    @Test
    public void testSmallestCircle() {
        final Circle<Point> circle = Circle.origin();
        final Point point = new Point();
        for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
            Line<Point> line = nextLine();
            line.smallestCircle(circle);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                line.areaRandom(rc, point);
                assertTrue(circle.test(point));
            }
        }
    }
    
    private void assertIntersection(@NotNull Line<Point> a, @NotNull Line<Point> b, boolean intersects) {
        assertEquals(intersects, Line.intersectsSparse(a, b));
        assertEquals(intersects, Line.intersectsDense(a, b));
    }

    @Test
    public void caseIntersects1() {
        final Line<Point> line1 = Line.points();
        final Line<Point> line2 = Line.points();
        line1.p1.setPoint(2, 1);
        line1.p2.setPoint(5, 1);
        line2.p1.setPoint(7, 1);
        line2.p2.setPoint(8, 1);
        assertIntersection(line1, line2, false);
        line2.p1.setPoint(4, 1);
        line2.p2.setPoint(13, 1);
        assertIntersection(line1, line2, true);
        line2.p1.setPoint(-6, 1);
        line2.p2.setPoint(6, 1);
        assertIntersection(line1, line2, true);
        line2.p1.setPoint(-1, 1);
        line2.p2.setPoint(3, 1);
        assertIntersection(line1, line2, true);
        line2.p1.setPoint(4, 3);
        line2.p2.setPoint(13, 3);
        assertIntersection(line1, line2, false);
        line1.p1.setPoint(0, 0);
        line1.p2.setPoint(-4, 4);
        line2.p1.setPoint(-5, 5);
        line2.p2.setPoint(-11, 11);
        assertIntersection(line1, line2, false);
        line2.p1.setPoint(-3, 3);
        line2.p2.setPoint(-4, 4);
        assertIntersection(line1, line2, true);
        line2.p1.setPoint(3, -3);
        line2.p2.setPoint(4, -4);
        assertIntersection(line1, line2, false);
        line2.p1.setPoint(1, 1);
        line2.p2.setPoint(-3, 5);
        assertIntersection(line1, line2, false);
    }

    @Test
    public void testIntersects2() {
        for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
            Line<Point> a = nextLine();
            Line<Point> b = nextLine();
            assertEquals(Line.intersectsSparse(a, b), Line.intersectsDense(a, b));
        }
    }

    @Test
    public void testMinimumDistance1() {
        final Circle<Point> circle = Circle.origin();
        final Point b = new Point();
        for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
            Line<Point> line = nextLine();
            line.smallestCircle(circle);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point a = nextPoint();
                line.areaRandom(rc, b);
                double min = line.minimumDistance(a);
                assertTrue(min <= Point.euclidDistance(a, b));
                assertTrue(min >= Point.euclidDistance(a, circle.o) - circle.r);
            }
        }
    }

    @Test
    public void testMinimumDistance2() {
        final Circle<Point> c = Circle.origin();
        final Circle<Point> d = Circle.origin();
        for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
            Line<Point> a = nextLine();
            Line<Point> b = nextLine();
            double min = Line.minimumDistance(a, b);
            a.smallestCircle(c);
            b.smallestCircle(d);
            assertTrue(min >= Point.euclidDistance(c.o, d.o) - c.r - d.r);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                a.areaRandom(rc, c.o);
                b.areaRandom(rc, d.o);
                assertTrue(min <= Point.euclidDistance(c.o, d.o));
            }
        }
    }

    @Test
    public void testEnterArea() {
        final Point a = new Point();
        final Point b = new Point();
        for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
            Line<Point> line = nextLine();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point c = nextPoint();
                a.setPoint(c);
                line.enterArea(a);
                line.areaRandom(rc, b);
                assertTrue(Point.euclidDistance(a, c) <= Point.euclidDistance(b, c));
            }
        }
    }
}
