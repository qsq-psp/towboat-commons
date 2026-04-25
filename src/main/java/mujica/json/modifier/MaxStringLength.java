package mujica.json.modifier;

import java.lang.annotation.*;

/**
 * Created on 2026/4/18.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxStringLength {

    int value();
}
