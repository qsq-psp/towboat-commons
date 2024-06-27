package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created in webbiton on 2020/12/23, named JsonIntField.
 * Recreated in va on 2021/12/24, named JsonIntValue.
 * Recreated in infrastructure on 2021/12/24.
 * Recreated on 2022/6/12.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntValue {

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;
}
