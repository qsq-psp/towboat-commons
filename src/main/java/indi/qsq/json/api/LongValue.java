package indi.um.json.api;

import java.lang.annotation.*;

/**
 * Created on 2022/8/5.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LongValue {

    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;
}
