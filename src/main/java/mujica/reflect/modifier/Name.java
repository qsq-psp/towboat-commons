package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2021/12/24", project = "infrastructure", name = "KeyName")
@CodeHistory(date = "2022/6/11", project = "Ultramarine", name = "JsonName")
@CodeHistory(date = "2025/3/3")
@Stable(date = "2025/8/1")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Repeatable(NameCollection.class)
public @interface Name {

    String value();

    /**
     * @return "reflect", "json", "xml", "en", "zh", etc
     */
    String language() default "reflect";
}
