package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created in infrastructure on 2022/4/9, named UseLogger.
 * Created on 2022/6/11.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectionLog {

    int when() default ReflectOperations.ALL;
}
