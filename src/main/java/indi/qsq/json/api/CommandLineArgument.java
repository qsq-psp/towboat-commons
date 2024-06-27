package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created on 2022/9/4.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLineArgument {

    int index() default -1;

    String key() default "";

    boolean aggregate() default false; // for boolean type only
}
