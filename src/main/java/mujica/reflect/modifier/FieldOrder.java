package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2021/12/24", project = "infrastructure", name = "KeyOrder")
@CodeHistory(date = "2022/6/11", project = "Ultramarine")
@CodeHistory(date = "2025/10/30")
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldOrder {

    String[] value();
}
