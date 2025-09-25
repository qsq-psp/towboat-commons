package mujica.math.geometry.g2d;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import mujica.math.algebra.random.RandomContext;
import mujica.math.geometry.Geometry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created on 2022/12/15.
 */
public class G2dTest {

    protected final RandomContext rc = new RandomContext();

    protected static final int LOOP = 12;

    protected static final int POINT = (2 * 2) * LOOP;

    @NotNull
    protected Point nextPoint() {
        final double[] array = rc.nextGaussianArray(2);
        return new Point(array[0], array[1]);
    }

    @NotNull
    protected Point nextNonzeroPoint() {
        while (true) {
            Point point = nextPoint();
            if (point.squareNorm() > Geometry.EPSILON) {
                return point;
            }
        }
    }

    @NotNull
    protected Point nextSmallPoint() {
        final double r = Geometry.EPSILON + Math.E * rc.nextDouble();
        final double a = Direction2.TAU * rc.nextDouble();
        return new Point(
                r * Math.cos(a),
                r * Math.sin(a)
        );
    }

    protected static final int BOUND = (2 * 2 * 2) * LOOP;

    @NotNull
    protected Bound nextBound() {
        final int length = 10;
        final double[] array = rc.nextGaussianArray(length);
        final Bound bound = new Bound();
        for (int index = 0; index < length; index += 2) {
            bound.includePoint(array[index], array[index + 1]);
        }
        return bound;
    }

    protected static final int CIRCLE = (3 * 3) * LOOP;

    @NotNull
    protected Circle<Point> nextCircle() {
        final double[] array = rc.nextGaussianArray(4);
        return new Circle<>(
                new Point(array[0], array[1]),
                0.5 * (Math.abs(array[2]) + Math.abs(array[3]))
        );
    }

    protected static final int LINE = (3 * 3) * LOOP;

    @NotNull
    protected Line<Point> nextLine() {
        final double[] array = rc.nextGaussianArray(4);
        return new Line<>(
                new Point(array[0], array[1]),
                new Point(array[2], array[3])
        );
    }

    protected static final int HALF_PLANE = (3 * 3) * LOOP;

    @NotNull
    protected HalfPlane nextHalfPlane() {
        HalfPlane line = new HalfPlane();
        do {
            double[] array = rc.nextGaussianArray(3);
            line.setHalfPlane(array[0], array[1], array[2]);
        } while (!line.isHealthy());
        return line;
    }

    protected static final int TRIANGLE = (3 * 2 * 2) * LOOP;

    @NotNull
    public Triangle<Point> nextTriangle() {
        final Triangle<Point> triangle = Triangle.points();
        while (true) {
            double[] array = rc.nextGaussianArray(6);
            for (int i = 0; i < 3; i++) {
                triangle.pointAt(i).setPoint(array[i], array[3 + i]);
            }
            if (triangle.isHealthy()) {
                return triangle;
            }
        }
    }

    protected static final int TANGENT = 8 * LOOP;

    @NotNull
    public Tangent nextTangent() {
        return new Tangent(rc.nextInt(), rc.nextGaussian());
    }

    @Nullable
    public static Object copy(@Nullable Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        final ByteBuf buf = Unpooled.buffer();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new ByteBufOutputStream(buf));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteBufInputStream(buf));
            return ois.readObject();
        } finally {
            buf.release();
        }
    }

    @Nullable
    public static Object tryCopy(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        final ByteBuf buf = Unpooled.buffer();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new ByteBufOutputStream(buf));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            ObjectInputStream ois = new ObjectInputStream(new ByteBufInputStream(buf));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            buf.release();
        }
    }
}
