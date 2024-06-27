package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created on 2022/10/9.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanEnumValue {

    String falseString() default "false";

    String trueString() default "true";

    boolean caseInsensitive() default false;
}
