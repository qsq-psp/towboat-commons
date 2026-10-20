package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2025/12/27", name = "ReferenceSources")
@CodeHistory(date = "2026/10/4")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface ReferenceCodeCollection {

    ReferenceCode[] value();
}
