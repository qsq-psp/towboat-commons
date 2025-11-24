package mujica.json.modifier;

import mujica.reflect.modifier.CodeHistory;

import java.lang.annotation.*;

@CodeHistory(date = "2020/12/23", project = "webbiton", name = "JsonIntField")
@CodeHistory(date = "2021/12/24", project = "infrastructure", name = "JsonIntValue")
@CodeHistory(date = "2021/12/24", project = "infrastructure", name = "IntValue")
@CodeHistory(date = "2022/6/12", project = "Ultramarine", name = "IntValue, LongValue")
@CodeHistory(date = "2025/10/29")
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IntegralRange {

    String min() default "";

    String max() default "";
}
