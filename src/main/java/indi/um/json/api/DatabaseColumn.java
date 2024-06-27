package indi.um.json.api;

import java.lang.annotation.*;

/**
 * Created on 2022/7/15.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = DatabaseColumnFallBack.class)
public @interface DatabaseColumn {

    DatabaseColumnType type();

    String name() default "";
}
