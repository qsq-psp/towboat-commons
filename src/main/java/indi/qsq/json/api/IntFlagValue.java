package indi.um.json.api;

import java.lang.annotation.*;

/**
 * Created in infrastructure on 2022/1/28.
 * Recreated on 2022/8/5.
 *
 * integer in java object
 * string in json value
 *
 * can be used together with @IdentifierReformat
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntFlagValue {

    String reference() default "";

    int[] integers() default {};

    String[] strings() default {};

    FlagStyle style() default FlagStyle.JSON_ARRAY;
}
