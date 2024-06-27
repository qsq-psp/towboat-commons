package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created on 2022/10/6.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseColumnFallBack {

    DatabaseColumn[] value() default {};
}
