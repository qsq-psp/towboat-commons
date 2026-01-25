package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/12/27")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ReferenceSources {

    ReferenceCode[] value();
}
