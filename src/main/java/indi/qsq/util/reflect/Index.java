package indi.qsq.util.reflect;

import java.lang.annotation.*;

/**
 * Created on 2024/5/1.
 * Moved here on 2024/7/4.
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface Index {

    String of() default "";

    int start() default 0;

    boolean inclusive();
}
