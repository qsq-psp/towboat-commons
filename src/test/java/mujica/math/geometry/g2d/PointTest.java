package mujica.math.geometry.g2d;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.Serializable;

import static org.junit.Assert.*;

/**
 * Created on 2022/7/10.
 */
public class PointTest extends G2dTest {

    @Test
    public void testSerializable() {
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point point = nextPoint();
            assertEquals(point, tryCopy(point));
        }
    }

    @Test
    public void testComplexMultiplyAssociative() {
        final Point d = new Point();
        final Point e = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextPoint();
            Point b = nextPoint();
            Point c = nextPoint();
            d.setToComplexProduct(a, b);
            d.setToComplexProduct(d, c);
            e.setToComplexProduct(b, c);
            e.setToComplexProduct(e, a);
            assertTrue(d.equalPointEpsilon(e));
        }
    }

    @Test
    public void testComplexMultiplyDistributive() {
        final Point d = new Point();
        final Point e = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextPoint();
            Point b = nextPoint();
            Point c = nextPoint();
            d.setToComplexProduct(a, c);
            e.setToComplexProduct(b, c);
            d.setToSum(d, e);
            e.setToSum(a, b);
            e.setToComplexProduct(e, c);
            assertTrue(d.equalPointEpsilon(e));
        }
    }

    @Test
    public void testComplexInvert() {
        final Point a = new Point();
        final Point c = new Point(1.0, 0.0);
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point b = nextNonzeroPoint();
            a.setPoint(b);
            b.complexInvert();
            b.complexMultiply(a);
            assertTrue(b.equalPointEpsilon(c));
        }
    }

    @Test
    public void testComplexDivide() {
        final Point d = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextPoint();
            d.setPoint(a);
            Point b = nextNonzeroPoint();
            Point c = nextNonzeroPoint();
            a.complexDivide(b);
            a.complexDivide(c);
            a.complexMultiply(b);
            a.complexMultiply(c);
            assertTrue(a.equalPointEpsilon(d));
        }
    }

    @Test
    public void caseComplexPower1() {
        final Point a = new Point();
        final Point b = new Point();
        a.setPoint(2, 0);
        a.setToComplexPower(a, 8);
        b.setPoint(256, 0);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(2, 0);
        a.setToComplexPower(a, 11);
        b.setPoint(2048, 0);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(1, 1);
        a.setToComplexPower(a, 5);
        b.setPoint(-4, -4);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(3, 1);
        a.setToComplexPower(a, 2);
        b.setPoint(8, 6);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(-3, -4);
        a.setToComplexPower(a, -1);
        b.setPoint(-0.12, 0.16);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(2, 1);
        a.setToComplexPower(a, -2);
        b.setPoint(0.12, -0.16);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(7, 1);
        a.setToComplexPower(a, 1);
        b.setPoint(7, 1);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(0, 1);
        a.setToComplexPower(a, 22);
        b.setPoint(-1, 0);
        assertTrue(a.equalPointEpsilon(b));
        a.setPoint(0, 0);
        a.setToComplexPower(a, 31);
        b.setPoint(0, 0);
        assertTrue(a.equalPointEpsilon(b));
    }

    @Test
    public void testComplexPower2() {
        final Point b = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextPoint();
            b.setToComplexPower(a, 1);
            assertTrue(a.equalPointEpsilon(b));
            b.setToComplexPower(a, 1.0);
            assertTrue(a.equalPointEpsilon(b));
        }
    }

    @Test
    public void testComplexPower3() {
        final Point b = new Point();
        final Point c = new Point();
        final Point d = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextSmallPoint();
            b.setPoint(a);
            int exp = rc.nextInt(-5, 5);
            try {
                c.setToComplexPower(b, exp);
                assertEquals(a, b);
                d.setToComplexPower(b, (double) exp);
                assertEquals(a, b);
                // System.out.println(a + " " + exp + " " + c + " " + d);
                assertTrue(c.equalPointEpsilon(d)); // failed again in 2022/12/6
            } catch (AssertionError e) {
                System.out.println(b);
                System.out.println(exp);
                System.out.println(c);
                System.out.println(d);
                throw e;
            }
        }
    }

    @Test
    public void testComplexPower4() {
        final Point b = new Point();
        final Point c = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextSmallPoint();
            b.setPoint(a);
            c.setPoint(a);
            int exp = rc.nextInt(-5, 5);
            b.complexPower(exp);
            c.complexPower((double) exp);
            assertTrue(b.equalPointEpsilon(c));
        }
    }

    @Test
    public void testComplexExponent() {
        final Point c = new Point();
        final Point d = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextSmallPoint();
            Point b = nextSmallPoint();
            c.setToSum(a, b);
            c.setToComplexExponent(c);
            a.setToComplexExponent(a);
            b.setToComplexExponent(b);
            d.setToComplexProduct(a, b);
            assertTrue(c.equalPointEpsilon(d));
        }
    }

    @Test
    public void testComplexLogarithm() {
        final Point b = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point a = nextSmallPoint();
            b.setPoint(a);
            b.setToComplexNaturalLogarithm(b);
            b.setToComplexExponent(b);
            assertTrue(a.equalPointEpsilon(b));
        }
    }

    @Test
    public void testLerp1() {
        final Point q1 = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point p1 = nextPoint();
            Point p2 = nextPoint();
            double weight = rc.nextDouble();
            q1.setToLerp(p1, p2, weight);
            assertTrue(q1.equalPointEpsilon(Point.lerp(p1, p2, weight)));
            assertTrue(q1.equalPointEpsilon(Point.lerp(p2, p1, 1.0 - weight)));
        }
    }

    @Test
    public void testLerp2() {
        final Point q1 = new Point();
        for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
            Point p1 = nextPoint();
            Point p2 = nextPoint();
            Point p3 = nextPoint();
            double u = rc.nextDouble();
            double v = rc.nextDouble();
            q1.setToLerp(p1, p2, u);
            q1.setToLerp(q1, p3, v);
            assertTrue(q1.equalPointEpsilon(Point.lerp(p1, p2, p3, u * (1.0 - v), v)));
        }
    }
}
