package mujica.ds.slot;

import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/5.
 */
public class Fraction<S extends Base2Integer> implements Rational {

    @NotNull
    @Name(value = "numerator", language = "en")
    @Name(value = "分子", language = "zh")
    final S p;

    @NotNull
    @Name(value = "denominator", language = "en")
    @Name(value = "分母", language = "zh")
    final S q;

    public Fraction(@NotNull S p, @NotNull S q) {
        super();
        this.p = p;
        this.q = q;
    }

    @NotNull
    @Override
    public RealIterator realIterator() {
        return new RealIterator() {
            @Override
            public boolean next() {
                return false;
            }

            @NotNull
            @Override
            public Rational lowerBound() {
                return Fraction.this;
            }

            @NotNull
            @Override
            public Rational higherBound() {
                return Fraction.this;
            }
        };
    }

    @NotNull
    @Override
    public Fraction<?> getFraction() {
        return this;
    }
}
