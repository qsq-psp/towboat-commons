package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created in va on 2021/9/28, named Affine2Test.
 * Created on 2022/6/26.
 */
@CodeHistory(date = "2021/9/28", name = "Affine2Test")
@CodeHistory(date = "2022/6/26")
public class Transform2Test extends G2dTest {

    public double nextDirection() {
        return 2 * Math.PI * rc.nextDouble();
    }

    public ViewportTransform nextViewportTransform() {
        final ViewportTransform vt = new ViewportTransform();
        final double[] array = rc.nextGaussianArray(2);
        vt.setTranslation(array[0], array[1]);
        do {
            vt.setCoreScale(rc.nextGaussian());
        } while (!vt.isHealthy());
        return vt;
    }

    public Axes nextAxes() {
        final Axes axes = new Axes((Void) null);
        do {
            double[] array = rc.nextGaussianArray(4);
            axes.setTranslation(array[0], array[1]);
            axes.setAxesCore(array[2], array[3]);
        } while (!axes.isHealthy());
        return axes;
    }

    public Affine2 nextAffine2() {
        final Affine2 affine2 = new Affine2((Void) null);
        do {
            double[] array = rc.nextGaussianArray(6);
            affine2.setTranslation(array[0], array[1]);
            affine2.setCore(array[2], array[3], array[4], array[5]);
        } while (!affine2.isHealthy());
        return affine2;
    }

    private static final int TRANSFORM = (4 * 4 + 2 * 2) * LOOP;

    public Transform2 nextPreserveLength() {
        double[] array;
        if (rc.nextBoolean()) {
            array = rc.nextGaussianArray(2);
            return new Translation2(array[0], array[1]);
        } else {
            Axes axes = new Axes((Void) null);
            array = rc.nextGaussianArray(2);
            axes.setTranslation(array[0], array[1]);
            axes.setCoreRotation(nextDirection());
            return axes;
        }
    }

    @Test
    public void testPreserveLength() {
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextPreserveLength();
            Assert.assertTrue(transform.preserveLength());
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                Point point1 = nextPoint();
                double length0 = Point.euclidDistance(point0, point1);
                transform.transform(point0, point0);
                transform.transform(point1, point1);
                double length1 = Point.euclidDistance(point0, point1);
                Assert.assertEquals(length0, length1, Geometry.EPSILON);
            }
        }
    }

    public Translation2 nextLinear() {
        double[] array;
        switch (rc.nextInt(22)) {
            case 0:
                return new Translation2();
            case 1: case 2:
                array = rc.nextGaussianArray(2);
                return new Translation2(array[0], array[1]);
            case 3:
                return new Flip2();
            case 4: case 5: {
                Flip2 flip2 = new Flip2();
                array = rc.nextGaussianArray(2);
                flip2.setTranslation(array[0], array[1]);
                flip2.setFlipOp(rc.nextInt());
                return flip2;
            }
            case 6:
                return new ViewportTransform();
            case 7: case 8:
                return nextViewportTransform();
            case 9:
                return new Axes();
            case 10: {
                Axes axes = new Axes();
                array = rc.nextGaussianArray(2);
                axes.setTranslation(array[0], array[1]);
                return axes;
            }
            case 11: case 12: {
                Axes axes = new Axes((Void) null);
                array = rc.nextGaussianArray(2);
                axes.setTranslation(array[0], array[1]);
                axes.setCoreRotation(nextDirection());
                return axes;
            }
            case 13: case 14:
                return nextAxes();
            case 15:
                return new Affine2();
            case 16: case 17: {
                Affine2 affine2 = new Affine2((Void) null);
                array = rc.nextGaussianArray(2);
                affine2.setTranslation(array[0], array[1]);
                affine2.setCoreRotation(nextDirection());
                return affine2;
            }
            default: // 18, 19, 20, 21
                return nextAffine2();
        }
    }

    @Test
    public void testLinear() {
        final Point point2 = new Point();
        final Point point3 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextLinear();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                Point point1 = nextPoint();
                point2.setToMedian(point0, point1);
                transform.transform(point0, point0);
                transform.transform(point1, point1);
                transform.transform(point2, point2);
                point3.setToMedian(point0, point1);
                Assert.assertTrue(point2.equalPointEpsilon(point3));
            }
        }
    }

    @Test
    public void testTransform0() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextLinear();
            try {
                Assert.assertTrue(transform.isHealthy());
                for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                    Point point0 = nextPoint();
                    point1.setPoint(point0);
                    transform.transform0(point1, point2);
                    Assert.assertTrue(point0.equalPoint(point1));
                    Assert.assertTrue(point2.isHealthy());
                }
            } catch (AssertionError e) {
                System.out.println(transform);
                throw e;
            }
        }
    }

    @Test
    public void testTransform() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextLinear();
            try {
                Assert.assertTrue(transform.isHealthy());
                for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                    Point point0 = nextPoint();
                    point1.setPoint(point0);
                    transform.transform(point1, point2);
                    Assert.assertTrue(point0.equalPoint(point1));
                    Assert.assertTrue(point2.isHealthy());
                }
            } catch (AssertionError e) {
                System.out.println(transform);
                throw e;
            }
        }
    }

    @Test
    public void testTransformLine1() {
        final Point point1 = new Point();
        final HalfPlane halfPlane1 = new HalfPlane();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Translation2 transform = nextLinear();
            try {
                for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                    Point point0 = nextPoint();
                    HalfPlane halfPlane0 = nextHalfPlane();
                    Assert.assertNotEquals(GeometryOperationResult.FAIL, halfPlane0.project(point0, point0));
                    Assert.assertEquals(0.0, halfPlane0.height(point0), Geometry.EPSILON);
                    transform.transform(point0, point1);
                    transform.transform(halfPlane0, halfPlane1);
                    Assert.assertEquals(0.0, halfPlane1.height(point1), Geometry.EPSILON);
                }
            } catch (AssertionError e) {
                System.out.println(transform);
                throw e;
            }
        }
    }

    @Test
    public void testTransformLine2() {
        final HalfPlane halfPlane1 = new HalfPlane();
        final HalfPlane halfPlane2 = new HalfPlane();
        final Line<Point> line = Line.points();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Translation2 transform = nextLinear();
            try {
                for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
                    HalfPlane halfPlane0 = nextHalfPlane();
                    halfPlane1.setHalfPlane(halfPlane0);
                    transform.transform(halfPlane1, halfPlane2);
                    Assert.assertEquals(halfPlane0, halfPlane1);
                    halfPlane0.toLine(line);
                    transform.transform(line.p1, line.p1);
                    transform.transform(line.p2, line.p2);
                    halfPlane1.setHalfPlane(line);
                    Assert.assertTrue(halfPlane1.equalHalfPlaneEpsilon(halfPlane2));
                }
            } catch (AssertionError e) {
                System.out.println(transform);
                throw e;
            }
        }
    }

    @Test
    public void testTransformLine3() {
        final HalfPlane halfPlane1 = new HalfPlane();
        final HalfPlane halfPlane2 = new HalfPlane();
        final Line<Point> line = Line.points();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Translation2 transform = nextLinear();
            try {
                for (int lineIndex = 0; lineIndex < LINE; lineIndex++) {
                    HalfPlane halfPlane0 = nextHalfPlane();
                    halfPlane1.setHalfPlane(halfPlane0);
                    transform.transform(halfPlane1, halfPlane2);
                    Assert.assertEquals(halfPlane0, halfPlane1);
                    halfPlane0.toLine(line, nextPoint());
                    transform.transform(line.p1, line.p1);
                    transform.transform(line.p2, line.p2);
                    halfPlane1.setHalfPlane(line);
                    Assert.assertTrue(halfPlane1.equalHalfPlaneEpsilon(halfPlane2));
                }
            } catch (AssertionError e) {
                System.out.println(transform);
                throw e;
            }
        }
    }

    @Test
    public void testInverseTransform() {
        Point point1 = new Point();
        Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextLinear();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                transform.transform(point0, point1);
                transform.inverseTransform(point1, point2);
                Assert.assertTrue(point0.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testSetToInverse() {
        Point point1 = new Point();
        Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextLinear();
            Transform2 inverse = transform.duplicate();
            Assert.assertEquals(0, transform.compareTo(inverse)); // also test clone()
            inverse.setToInverse();
            Assert.assertNotNull(inverse);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                transform.transform(point0, point1);
                inverse.transform(point1, point2);
                Assert.assertTrue(point0.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testInvert() {
        Point point1 = new Point();
        Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextLinear();
            Transform2 inverse = transform.invert();
            Assert.assertNotNull(inverse);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                transform.transform(point0, point1);
                inverse.transform(point1, point2);
                Assert.assertTrue(point0.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testVectorComponents() {
        Point point1 = new Point();
        Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Transform2 transform = nextLinear();
            Assert.assertEquals(9, transform.vectorLength());
            Assert.assertEquals(0.0, transform.vectorComponent(6), Geometry.EPSILON);
            Assert.assertEquals(0.0, transform.vectorComponent(7), Geometry.EPSILON);
            Assert.assertEquals(1.0, transform.vectorComponent(8), Geometry.EPSILON);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                transform.transform(point0, point1);
                point2.setPoint(
                        transform.vectorComponent(0) * point0.x + transform.vectorComponent(1) * point0.y + transform.vectorComponent(2),
                        transform.vectorComponent(3) * point0.x + transform.vectorComponent(4) * point0.y + transform.vectorComponent(5)
                );
                Assert.assertTrue(point1.equalPointEpsilon(point2));
            }
        }
    }

    @NotNull
    private ViewportTransform nextScale() {
        double[] array;
        switch (rc.nextInt(15)) {
            case 0:
                return new ViewportTransform();
            case 1: case 2:
                return nextViewportTransform();
            case 3:
                return new Axes();
            case 4: {
                Axes axes = new Axes();
                array = rc.nextGaussianArray(2);
                axes.setTranslation(array[0], array[1]);
                return axes;
            }
            case 5: case 6: {
                Axes axes = new Axes((Void) null);
                array = rc.nextGaussianArray(2);
                axes.setTranslation(array[0], array[1]);
                axes.setCoreRotation(nextDirection());
                return axes;
            }
            case 7: case 8:
                return nextAxes();
            case 9:
                return new Affine2();
            case 10: case 11: {
                Affine2 affine2 = new Affine2((Void) null);
                array = rc.nextGaussianArray(2);
                affine2.setTranslation(array[0], array[1]);
                affine2.setCoreRotation(nextDirection());
                return affine2;
            }
            default: // 12, 13, 14
                return nextAffine2();
        }
    }

    @Test
    public void testStationaryPoint() {
        final Point point0 = new Point();
        final Point point1 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            ViewportTransform transform = nextScale();
            try {
                if (transform.stationaryPoint(point0) == GeometryOperationResult.FAIL) {
                    continue;
                }
                transform.transform(point0, point1);
                Assert.assertTrue(point0.equalPointEpsilon(point1));
            } catch (AssertionError e) {
                System.out.println(transform);
                throw e;
            }
        }
    }

    @Test
    public void testScaleBefore() {
        final Point point0 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            double scale = nextViewportTransform().vectorComponent(0); // mxx
            Point point1 = nextPoint();
            point0.setToProduct(scale, point1);
            ViewportTransform transform = nextScale();
            transform.transform(point0, point0);
            transform.scaleBefore(scale);
            transform.transform(point1, point1);
            Assert.assertTrue(point0.equalPointEpsilon(point1));
        }
    }

    @Test
    public void testCenterScaleBefore() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            ViewportTransform transform = nextScale();
            Point center = nextPoint();
            double scale = nextViewportTransform().vectorComponent(0); // mxx
            transform.transform(center, point1);
            transform.scaleBefore(center, scale);
            transform.transform(center, point2);
            Assert.assertTrue(point1.equalPointEpsilon(point2));
        }
    }

    @Test
    public void testScaleAfter() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            ViewportTransform transform = nextViewportTransform();
            double scale = nextViewportTransform().mxx;
            ViewportTransform inverse = transform.invert();
            Assert.assertNotNull(inverse);
            transform.scaleAfter(scale);
            inverse.scaleBefore(1.0 / scale);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                transform.transform(point0, point1);
                inverse.transform(point1, point2);
                Assert.assertTrue(point0.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testCenterScaleAfter() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            ViewportTransform transform = nextViewportTransform();
            Point center = nextPoint();
            double scale = nextViewportTransform().mxx;
            try {
                ViewportTransform inverse = transform.invert();
                Assert.assertNotNull(inverse);
                transform.scaleAfter(center, scale);
                inverse.scaleBefore(center, 1.0 / scale);
                for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                    Point point0 = nextPoint();
                    transform.transform(point0, point1);
                    inverse.transform(point1, point2);
                    Assert.assertTrue(point0.equalPointEpsilon(point2));
                }
            } catch (AssertionError e) {
                System.out.println(transform);
                System.out.println(center);
                System.out.println(scale);
                throw e;
            }
        }
    }

    public Axes nextRotate() {
        if (rc.nextBoolean()) {
            return nextAxes();
        } else {
            return nextAffine2();
        }
    }

    @Test
    public void testRotateBefore() {
        final Point point0 = new Point();
        final Point point1 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            double angle0 = nextDirection();
            double angle1 = nextDirection();
            point0.setPoint(Math.cos(angle0 + angle1), Math.sin(angle0 + angle1));
            point1.setPoint(Math.cos(angle0), Math.sin(angle0));
            Axes transform = nextRotate();
            transform.transform(point0, point0);
            transform.rotateBefore(angle1);
            transform.transform(point1, point1);
            Assert.assertTrue(point0.equalPointEpsilon(point1));
        }
    }

    @Test
    public void testPivotRotateBefore() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Axes transform = nextRotate();
            Point pivot = nextPoint();
            transform.transform(pivot, point1);
            transform.rotateBefore(pivot, nextDirection());
            transform.transform(pivot, point2);
            Assert.assertTrue(point1.equalPointEpsilon(point2));
        }
    }

    @Test
    public void testRotateAfter() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            double angle = nextDirection();
            Axes transform = nextRotate();
            Axes inverse = transform.invert();
            Assert.assertNotNull(inverse);
            transform.rotateAfter(angle);
            inverse.rotateBefore(-angle);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                transform.transform(point0, point1);
                inverse.transform(point1, point2);
                Assert.assertTrue(point0.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testPivotRotateAfter() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            double angle = nextDirection();
            Axes transform = nextRotate();
            Axes inverse = transform.invert();
            Assert.assertNotNull(inverse);
            Point pivot = nextPoint();
            transform.rotateAfter(pivot, angle);
            inverse.rotateBefore(pivot, -angle);
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                transform.transform(point0, point1);
                inverse.transform(point1, point2);
                Assert.assertTrue(point0.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testOrthogonalizeCore() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Affine2 affine2 = nextAffine2();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextNonzeroPoint();
                affine2.transform(point0, point1);
                Assert.assertNotEquals(GeometryOperationResult.FAIL, affine2.orthogonalizeCore(point0));
                affine2.transform(point0, point2);
                Assert.assertTrue(point1.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testAxesProduct() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Axes left = nextAxes();
            Axes right = nextAxes();
            Axes product;
            if (rc.nextBoolean()) {
                product = nextAxes();
                product.setToProduct(left, right);
            } else {
                if (rc.nextBoolean()) {
                    product = left.duplicate();
                    product.setToProduct(product, right);
                } else {
                    product = right.duplicate();
                    product.setToProduct(left, product);
                }
            }
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                right.transform(point0, point1); // right first
                left.transform(point1, point1);
                product.transform(point0, point2);
                Assert.assertTrue(point1.equalPointEpsilon(point2));
            }
        }
    }

    @Test
    public void testAffine2Product() {
        final Point point1 = new Point();
        final Point point2 = new Point();
        for (int transformIndex = 0; transformIndex < TRANSFORM; transformIndex++) {
            Affine2 left = nextAffine2();
            Affine2 right = nextAffine2();
            Affine2 product;
            if (rc.nextBoolean()) {
                product = nextAffine2();
                product.setToProduct(left, right);
            } else {
                if (rc.nextBoolean()) {
                    product = left.duplicate();
                    product.setToProduct(product, right);
                } else {
                    product = right.duplicate();
                    product.setToProduct(left, product);
                }
            }
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                Point point0 = nextPoint();
                right.transform(point0, point1); // right first
                left.transform(point1, point1);
                product.transform(point0, point2);
                Assert.assertTrue(point1.equalPointEpsilon(point2));
            }
        }
    }
}
