package mujica.geometry.g2d;

import mujica.geometry.Geometry;
import mujica.io.stream.OIO;
import mujica.reflect.modifier.CodeHistory;
import org.junit.Test;

import static org.junit.Assert.*;

@CodeHistory(date = "2022/10/10", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class HalfPlaneTest extends G2dTest {

    @Test
    public void checkSerializable() throws Exception {
        for (int halfPlaneIndex = 0; halfPlaneIndex < HALF_PLANE; halfPlaneIndex++) {
            HalfPlane halfPlane = nextHalfPlane();
            assertEquals(halfPlane, OIO.copy(halfPlane));
        }
    }

    @Test
    public void checkToLine1() {
        final Line<Point> line = Line.points();
        for (int halfPlaneIndex = 0; halfPlaneIndex < HALF_PLANE; halfPlaneIndex++) {
            HalfPlane halfPlane = nextHalfPlane();
            halfPlane.toLine(line);
            assertEquals(0.0, halfPlane.height(line.p1), Geometry.EPSILON);
            assertEquals(0.0, halfPlane.height(line.p2), Geometry.EPSILON);
            assertTrue(line.measure1() > Geometry.EPSILON);
        }
    }

    @Test
    public void checkToLine2() {
        final Line<Point> line = Line.points();
        for (int halfPlaneIndex = 0; halfPlaneIndex < HALF_PLANE; halfPlaneIndex++) {
            HalfPlane halfPlane = nextHalfPlane();
            for (int pointIndex = 0; pointIndex < POINT; pointIndex++) {
                halfPlane.toLine(line, nextPoint());
                assertEquals(0.0, halfPlane.height(line.p1), Geometry.EPSILON);
                assertEquals(0.0, halfPlane.height(line.p2), Geometry.EPSILON);
                assertTrue(line.measure1() > Geometry.EPSILON);
            }
        }
    }
}
