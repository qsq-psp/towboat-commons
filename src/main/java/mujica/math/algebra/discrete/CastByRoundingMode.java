package mujica.math.algebra.discrete;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created on 2025/3/4.
 */
public class CastByRoundingMode implements IntegralCast {

    @NotNull
    public final RoundingMode mode;

    public CastByRoundingMode(@NotNull RoundingMode mode) {
        super();
        this.mode = mode;
    }

    @Override
    public int d2i(double v) {
        switch (mode) {
            default:
            case UNNECESSARY: {
                int iv = (int) v;
                if (iv != v) {
                    throw new ArithmeticException("Cast is necessary");
                }
                return iv;
            }
        }
    }

    @Override
    public long d2l(double v) {
        switch (mode) {
            default:
            case UNNECESSARY: {
                long lv = (long) v;
                if (lv != v) {
                    throw new ArithmeticException("Cast is necessary");
                }
                return lv;
            }
        }
    }
}
