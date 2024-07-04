package indi.qsq.util.reflect;

import java.lang.annotation.*;

/**
 * Created on 2024/7/4.
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface ActuallyChar {

    boolean canBeZero() default true;

    boolean canBeEOF() default false;

    boolean canContainSurrogate() default true;
}
