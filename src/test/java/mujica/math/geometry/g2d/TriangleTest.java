package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2022/10/2.
 */
public class TriangleTest extends G2dTest {

    @Test
    public void checkSerializable() {
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            Assert.assertEquals(triangle, tryCopy(triangle));
        }
    }

    @Test
    public void checkMeasure() {
        final Bound bound = new Bound();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.smallestBound(bound));
            Assert.assertTrue(triangle.measure1() <= bound.measure1());
            Assert.assertTrue(triangle.measure2() <= bound.measure2());
        }
    }

    @Test
    public void checkConvex() {
        final Point p1 = new Point();
        final Point p2 = new Point();
        final Point p3 = new Point();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.areaRandom(rc, p1));
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.areaRandom(rc, p2));
                p3.setToLerp(p1, p2, rc.nextDouble());
                Assert.assertTrue(triangle.test(p3));
            }
        }
    }

    @Test
    public void checkCenter() {
        final Point center = new Point();
        final Point side = new Point();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            Assert.assertTrue(triangle.test(triangle.center(null)));
            triangle.center(center);
            for (int vertexIndex = 0; vertexIndex < 3; vertexIndex++) {
                side.setToLerp(center, triangle.pointAt(vertexIndex), rc.nextDouble());
                Assert.assertTrue(triangle.test(side));
            }
        }
    }

    @Test
    public void checkInnerCenter() {
        final Point innerCenter = new Point();
        final HalfPlane edge = new HalfPlane();
        final Bound range = new Bound();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.innerCenter(innerCenter));
            range.invalidate();
            for (int vertexIndex = 0; vertexIndex < 3; vertexIndex++) {
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.edgeAt(edge, vertexIndex));
                edge.normalize();
                range.includeX(Math.abs(edge.height(innerCenter)));
            }
            Assert.assertEquals(0.0, range.width(), Geometry.EPSILON);
        }
    }

    @Test
    public void checkInnerRadius() {
        final Point innerCenter = new Point();
        final HalfPlane edge = new HalfPlane();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.innerCenter(innerCenter));
            double innerRadius = triangle.innerRadius();
            for (int vertexIndex = 0; vertexIndex < 3; vertexIndex++) {
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.edgeAt(edge, vertexIndex));
                edge.normalize();
                Assert.assertEquals(Math.abs(edge.height(innerCenter)), innerRadius, Geometry.EPSILON);
            }
        }
    }

    @Test
    public void checkOuterCenter() {
        final Point outerCenter = new Point();
        final Bound range = new Bound();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.outerCenter(outerCenter));
            range.invalidate();
            for (int vertexIndex = 0; vertexIndex < 3; vertexIndex++) {
                range.includeX(Point.euclidDistance(triangle.pointAt(vertexIndex), outerCenter));
            }
            Assert.assertEquals(0.0, range.width(), Geometry.EPSILON);
        }
    }

    @Test
    public void checkEnter1() {
        final Point center = new Point();
        final Point side = new Point();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            triangle.center(center);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point enter = nextPoint();
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.enterArea(enter));
                side.setToLerp(center, enter, rc.nextDouble());
                Assert.assertTrue(triangle.test(side));
            }
        }
    }

    @Test
    public void checkEnter2() {
        final Point enter = new Point();
        final Point inside = new Point();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point base = nextPoint();
                enter.setPoint(base);
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.enterArea(enter));
                triangle.areaRandom(rc, inside);
                Assert.assertTrue(Point.euclidDistance(base, enter) <= Point.euclidDistance(base, inside));
            }
        }
    }

    @Test
    public void checkLeave1() {
        final Point center = new Point();
        final Point side = new Point();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            triangle.center(center);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point leave = nextPoint();
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.leaveArea(leave));
                side.setToLerp(center, leave, 1.0 + rc.nextDouble());
                Assert.assertFalse(triangle.test(side));
            }
        }
    }

    @Test
    public void checkLeave2() {
        final Point base = new Point();
        final Point leave = new Point();
        final Point side = new Point();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                triangle.areaRandom(rc, base);
                leave.setPoint(base);
                Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.leaveArea(leave));
                side.setToLerp(triangle.pointOf(pointIndex), triangle.pointOf(pointIndex + 1), rc.nextDouble());
                Assert.assertTrue(Point.euclidDistance(base, leave) <= Point.euclidDistance(base, side));
            }
        }
    }

    @Test
    public void checkRandom() {
        final Point point = new Point();
        for (int triangleIndex = 0; triangleIndex < TRIANGLE; triangleIndex++) {
            Triangle<Point> triangle = nextTriangle();
            Assert.assertNotEquals(GeometryOperationResult.FAIL, triangle.areaRandom(rc, point));
            Assert.assertTrue(triangle.test(point));
        }
    }
}
