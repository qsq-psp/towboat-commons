package indi.qsq.util.reflect;

import java.lang.annotation.*;

/**
 * Created on 2024/7/4.
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface ActuallyByte {

    boolean unsigned() default false;

    boolean canBeEOF() default false;
}
