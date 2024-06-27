package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created in infrastructure on 2022/1/2.
 * Recreated on 2022/7/24.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface CallBuilder {

    String value() default "";

    UseBuilder when() default UseBuilder.IF_NULL;
}
