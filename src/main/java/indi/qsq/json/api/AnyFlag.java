package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created on 2022/9/11.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyFlag {

    String value();

    boolean copy() default false;

    FlagStyle style() default FlagStyle.JSON_ARRAY;
}
