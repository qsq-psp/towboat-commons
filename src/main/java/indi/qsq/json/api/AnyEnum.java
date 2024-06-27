package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created on 2022/8/12.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyEnum {

    String value();

    boolean copy() default false;
}
