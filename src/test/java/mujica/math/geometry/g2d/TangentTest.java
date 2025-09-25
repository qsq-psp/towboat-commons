package mujica.math.geometry.g2d;

import mujica.math.geometry.Geometry;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created in va on 2021/9/23.
 * Created on 2022/7/10.
 */
public class TangentTest extends G2dTest {

    @Test
    public void testGetRadian() {
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            Tangent tan = nextTangent();
            Radian rad = new Radian(tan.getRadian());
            assertTrue(tan.equalsDirectionEpsilon(rad));
        }
    }

    @Test
    public void testSetRadian() {
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            Radian rad = new Radian();
            rad.setRandom(rc);
            Tangent tan = new Tangent();
            tan.setRadian(rad.getRadian());
            assertTrue(tan.equalsDirectionEpsilon(rad));
        }
    }

    @Test
    public void testCosine() {
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            Tangent tan = nextTangent();
            Point point = new Point(tan.cos(), tan.sin());
            assertEquals(1.0, point.euclidNorm(), Geometry.EPSILON);
        }
    }

    @Test
    public void testCotangent() {
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            Tangent tan = nextTangent();
            Radian rad = new Radian(tan.getRadian());
            assertEquals(rad.tan(), tan.tan(), Geometry.EPSILON);
            assertEquals(rad.cot(), tan.cot(), Geometry.EPSILON);
        }
    }

    @Test
    public void testSum() {
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            Tangent a = nextTangent();
            Tangent b = nextTangent();
            Radian rad = new Radian(a.getRadian() + b.getRadian());
            a.add(b);
            assertTrue(rad.equalsDirectionEpsilon(a));
        }
    }

    @Test
    public void testAddAssociative() {
        Tangent a = new Tangent();
        Tangent b = new Tangent();
        Tangent c = new Tangent();
        Tangent d = new Tangent();
        Tangent e = new Tangent();
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            d.setTangent(a);
            d.add(b);
            d.add(c);
            e.setTangent(b);
            e.add(c);
            e.add(a);
            assertTrue(d.equalsDirectionEpsilon(e));
        }
    }

    @Test
    public void testDifference() {
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            Tangent a = nextTangent();
            Tangent b = nextTangent();
            Radian rad = new Radian(a.getRadian() - b.getRadian());
            a.subtract(b);
            assertTrue(rad.equalsDirectionEpsilon(a));
        }
    }

    @Test
    public void testSubtractAssociative() {
        Tangent a = new Tangent();
        Tangent b = new Tangent();
        Tangent c = new Tangent();
        Tangent d = new Tangent();
        Tangent e = new Tangent();
        for (int directionIndex = 0; directionIndex < TANGENT; directionIndex++) {
            d.setTangent(a);
            d.subtract(b);
            d.subtract(c);
            e.setTangent(a);
            e.subtract(c);
            e.subtract(b);
            assertTrue(d.equalsDirectionEpsilon(e));
        }
    }

    @Test
    public void testVector1() {
        for (int pointIndex = 0; pointIndex < TANGENT; pointIndex++) {
            Point point = nextPoint();
            if (point.euclidNorm() < Geometry.EPSILON) {
                continue;
            }
            Tangent tan = new Tangent();
            tan.setToVector(point);
            Radian rad = new Radian();
            rad.setToVector(point);
            assertTrue(tan.equalsDirectionEpsilon(rad));
        }
    }

    @Test
    public void testVector2() {
        for (int pointIndex = 0; pointIndex < TANGENT; pointIndex++) {
            Point point0 = nextPoint();
            Point point1 = nextPoint();
            if (Point.euclidDistance(point0, point1) < Geometry.EPSILON) {
                continue;
            }
            Tangent tan = new Tangent();
            tan.setToVector(point0, point1);
            Radian rad = new Radian();
            rad.setToVector(point0, point1);
            assertTrue(tan.equalsDirectionEpsilon(rad));
        }
    }
}
