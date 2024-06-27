package indi.um.json.api;

import java.lang.annotation.*;

/**
 * Created in webbiton on 2020/12/23, named JsonStringToIntField.
 * Recreated in va on 2021/12/27, named JsonIntToStringValue.
 * Recreated in infrastructure on 2021/12/31.
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
public @interface IntEnumValue {

    int[] integers() default {};

    String[] strings() default {};

    boolean caseInsensitive() default false;
}
