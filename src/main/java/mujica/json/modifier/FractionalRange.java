package mujica.json.modifier;

import mujica.reflect.modifier.CodeHistory;

import java.lang.annotation.*;

@CodeHistory(date = "2021/12/24", project = "va", name = "JsonDoubleValue")
@CodeHistory(date = "2021/12/24", project = "infrastructure", name = "DoubleValue")
@CodeHistory(date = "2022/6/12", project = "Ultramarine", name = "DoubleValue")
@CodeHistory(date = "2025/10/31")
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FractionalRange {

    String min() default "";

    String max() default "";

    boolean acceptNaN() default false;
}
