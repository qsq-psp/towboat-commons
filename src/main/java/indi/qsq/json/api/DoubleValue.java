package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created in va on 2021/12/24, named JsonDoubleValue.
 * Recreated in infrastructure on 2021/12/24.
 * Recreated on 2022/6/12.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleValue {

    double min() default Double.NaN;

    double max() default Double.NaN;
}
