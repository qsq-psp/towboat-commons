package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created in infrastructure on 2021/12/24, named KeyOrder.
 * Created on 2022/6/11.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldOrder {

    String[] value();

    int ordered() default ReflectOperations.JSON_SERIALIZE;

    int random() default 0;
}
