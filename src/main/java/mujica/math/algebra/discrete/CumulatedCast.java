package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created in Ultramarine on 2023/12/13, named IntegralCast.Cumulative.
 * Recreated on 2025/3/3.
 */
@CodeHistory(date = "2023/12/13", project = "Ultramarine", name = "IntegralCast.Cumulative")
@CodeHistory(date = "2025/3/3")
public class CumulatedCast implements IntegralCast {

    @NotNull
    final IntegralCast cast;

    double cumulator;

    public CumulatedCast(@NotNull IntegralCast cast) {
        super();
        this.cast = cast;
    }

    @Override
    public int d2i(double v) {
        assert Math.abs(cumulator) < 1.0;
        cumulator += v;
        int integral = cast.d2i(cumulator);
        cumulator -= integral;
        assert Math.abs(cumulator) < 1.0;
        return integral;
    }

    @Override
    public long d2l(double v) {
        assert Math.abs(cumulator) < 1.0;
        cumulator += v;
        long integral = cast.d2l(cumulator);
        cumulator -= integral;
        assert Math.abs(cumulator) < 1.0;
        return integral;
    }
}
