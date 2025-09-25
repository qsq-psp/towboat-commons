package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/5/8")
@Stable(date = "2025/8/6")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ReferenceDocument {

    ReferencePage[] value();
}
