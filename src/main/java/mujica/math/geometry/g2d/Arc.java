package mujica.math.geometry.g2d;

import mujica.math.geometry.GeometryOperationResult;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2022/10/5.
 */
@CodeHistory(date = "2022/10/5", project = "Ultramarine")
@CodeHistory(date = "2025/3/2")
public class Arc<P extends Point, D extends Direction2> extends OriginJordan2<P> implements Curve2 {

    private static final long serialVersionUID = 0xfcfc579604ba9e44L;

    @NotNull
    public final Direction2 d0;

    @NotNull
    public final Direction2 d1;

    public double r;

    public Arc(@NotNull P o, @NotNull D d0, @NotNull D d1) {
        super(o);
        this.d0 = d0;
        this.d1 = d1;
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        o.checkHealth(consumer);
        checkRadianHealthy(d0.getRadian(), d1.getRadian(), consumer);
        checkPositive("r", r, consumer);
    }

    @Override
    public boolean isHealthy() {
        return o.isHealthy() && isRadianHealthy(d0.getRadian(), d1.getRadian()) && r > 0.0;
    }

    private void checkRadianHealthy(double rad0, double rad1, @NotNull Consumer<RuntimeException> consumer) {
        if (!(Math.abs(rad0 - rad1) < Direction2.TAU)) {
            consumer.accept(new RuntimeException("d0 = " + rad0 + " and d1 = " + rad1 + " are too far"));
        }
        if (rad0 == rad1) {
            consumer.accept(new RuntimeException("d0 and d1 = " + rad0 + " are too close"));
        }
    }

    private boolean isRadianHealthy(double rad0, double rad1) {
        return Math.abs(rad0 - rad1) < Direction2.TAU && rad0 != rad1;
    }

    @Override
    public double measure1() {
        return Math.abs(d0.getRadian() - d1.getRadian()) * r;
    }

    @Override
    @NotNull
    public GeometryOperationResult interpolate(double position, @NotNull Point point) {
        double rad = d0.getRadian();
        rad = rad + (d1.getRadian() - rad) * position;
        point.setPoint(
                o.x + r * Math.cos(rad),
                o.y + r * Math.sin(rad)
        );
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult derivative(double position, @NotNull Point point) {
        double rad = d0.getRadian();
        double velocity = d1.getRadian() - d0.getRadian();
        rad = rad + velocity * position;
        velocity *= r;
        point.setPoint(
                o.x - velocity * Math.sin(rad),
                o.y + velocity * Math.cos(rad)
        );
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult includedInto(@NotNull Bound bound) {
        if (bound.testCircle(o.x, o.y, r)) {
            return GeometryOperationResult.REMAIN;
        }
        boundInclude(bound);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallBound(@NotNull Bound bound) {
        bound.reset();
        bound.includeCircle(o.x, o.y, r);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult smallestBound(@NotNull Bound bound) {
        bound.reset();
        boundInclude(bound);
        return GeometryOperationResult.UNKNOWN;
    }

    private void boundInclude(@NotNull Bound bound) {
        double rad0 = d0.getRadian();
        double rad1 = d1.getRadian();
        if (rad0 > rad1) {
            double rad2 = rad0;
            rad0 = rad1;
            rad1 = rad2;
        }
        final int quadrant0 = (int) Math.ceil(Direction2.INV_HPI * rad0);
        final int quadrant1 = (int) Math.floor(Direction2.INV_HPI * rad1);
        boolean cos = true;
        boolean sin = true;
        if (quadrant0 <= quadrant1) {
            if (quadrant0 + 3 <= quadrant1) {
                bound.includeCircle(o.x, o.y, r);
                return;
            } else {
                switch (((quadrant0 & 0x3) << 2) | (quadrant1 & 0x3)) {
                    case 0x0:
                        bound.includeX(o.x + r);
                        break;
                    case 0x1:
                        bound.includeX(o.x + r);
                        bound.includeY(o.y + r);
                        break;
                    case 0x2:
                        bound.includeX(o.x + r);
                        bound.includeY(o.y + r);
                        bound.includeX(o.x - r);
                        cos = false;
                        break;
                    case 0x5:
                        bound.includeY(o.y + r);
                        break;
                    case 0x6:
                        bound.includeY(o.y + r);
                        bound.includeX(o.x - r);
                        break;
                    case 0x7:
                        bound.includeY(o.y + r);
                        bound.includeX(o.x - r);
                        bound.includeY(o.y - r);
                        sin = false;
                        break;
                    case 0x8:
                        bound.includeX(o.x - r);
                        bound.includeY(o.y - r);
                        bound.includeX(o.x + r);
                        cos = false;
                        break;
                    case 0xa:
                        bound.includeX(o.x - r);
                        break;
                    case 0xb:
                        bound.includeX(o.x - r);
                        bound.includeY(o.y - r);
                        break;
                    case 0xc:
                        bound.includeY(o.y - r);
                        bound.includeX(o.x + r);
                        break;
                    case 0xd:
                        bound.includeY(o.y - r);
                        bound.includeX(o.x + r);
                        bound.includeY(o.y + r);
                        sin = false;
                        break;
                    case 0xf:
                        bound.includeY(o.y - r);
                        break;
                }
            }
        }
        if (cos) {
            bound.includeX(o.x + r * Math.cos(rad0));
            bound.includeX(o.x + r * Math.cos(rad1));
        }
        if (sin) {
            bound.includeY(o.y + r * Math.sin(rad0));
            bound.includeY(o.y + r * Math.sin(rad1));
        }
    }

    @Override
    @NotNull
    public GeometryOperationResult areaRandom(@NotNull RandomContext rc, @NotNull Point point) {
        final double rad = rc.nextDouble(d0.getRadian(), d1.getRadian());
        point.setPoint(
                o.x + r * Math.cos(rad),
                o.y + r * Math.sin(rad)
        );
        return GeometryOperationResult.UNKNOWN;
    }

    public void setArc(double cx, double cy, double r, double rad0, double rad1) {
        this.o.setPoint(cx, cy);
        this.r = r;
        this.d0.setRadian(rad0);
        this.d1.setRadian(rad1);
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        setArc(0.0, 0.0, 0.0, 0.0, 0.0);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    @NotNull
    public GeometryOperationResult invalidate() {
        setArc(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        return GeometryOperationResult.UNKNOWN;
    }

    @Override
    public String toString() {
        return String.format("Ellipse[%s, %s, %s, %.3f]", o, d0, d1, r);
    }
}
