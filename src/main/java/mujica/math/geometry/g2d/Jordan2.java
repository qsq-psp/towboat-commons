package mujica.math.geometry.g2d;

import mujica.math.algebra.random.RandomContext;
import mujica.math.geometry.Geometry;
import mujica.math.geometry.GeometryOperationResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created in coo on 2020/7/20, named RestrictionArea.
 * Created on 2022/6/5.
 */
public class Jordan2 extends Geometry implements Predicate<Point> {

    private static final long serialVersionUID = 0x727888394fd493bbL;

    public Jordan2() {
        super();
    }

    @Override
    @NotNull
    public Jordan2 duplicate() {
        return new Jordan2();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // always healthy; pass
    }

    @Override
    public boolean isHealthy() {
        return true;
    }

    public double measure1() {
        return 0.0; // length
    }

    public double measure2() {
        return 0.0; // area
    }

    @Override
    public boolean test(@NotNull Point point) {
        return false;
    }

    @NotNull
    public GeometryOperationResult includedInto(@NotNull Bound bound) {
        final Bound additional = new Bound();
        final GeometryOperationResult result = smallestBound(additional);
        if (result != GeometryOperationResult.FAIL) {
            additional.includedInto(bound);
            return GeometryOperationResult.UNKNOWN;
        } else {
            return GeometryOperationResult.FAIL;
        }
    }

    @NotNull
    public GeometryOperationResult smallBound(@NotNull Bound bound) {
        return smallestBound(bound);
    }

    @NotNull
    public GeometryOperationResult smallestBound(@NotNull Bound bound) {
        return GeometryOperationResult.FAIL;
    }

    @NotNull
    public GeometryOperationResult smallCircle(@NotNull Circle<?> circle) {
        return smallestCircle(circle);
    }

    @NotNull
    public GeometryOperationResult smallestCircle(@NotNull Circle<?> circle) {
        return GeometryOperationResult.FAIL;
    }

    @NotNull
    public GeometryOperationResult enterArea(@NotNull Point point) {
        return GeometryOperationResult.FAIL;
    }

    @NotNull
    public GeometryOperationResult leaveArea(@NotNull Point point) {
        return GeometryOperationResult.REMAIN;
    }

    @NotNull
    public GeometryOperationResult areaRandom(@NotNull RandomContext rc, @NotNull Point point) {
        return GeometryOperationResult.FAIL;
    }

    @Override
    @NotNull
    public GeometryOperationResult reset() {
        return GeometryOperationResult.REMAIN;
    }

    @NotNull
    @Override
    public GeometryOperationResult invalidate() {
        return GeometryOperationResult.REMAIN;
    }

    @Override
    @NotNull
    public GeometryOperationResult repair() {
        return GeometryOperationResult.REMAIN;
    }

    @NotNull
    @Override
    public GeometryOperationResult normalize() {
        return GeometryOperationResult.REMAIN;
    }

    @Override
    public int hashCode() {
        return 0x41eb0093;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj != null && Jordan2.class == obj.getClass();
    }
}
