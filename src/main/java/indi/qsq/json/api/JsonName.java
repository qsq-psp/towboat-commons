package indi.um.json.api;

import java.lang.annotation.*;

/**
 * Created in infrastructure on 2021/12/24, named KeyName.
 * Recreated on 2022/6/11.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonName {

    String value();
}
