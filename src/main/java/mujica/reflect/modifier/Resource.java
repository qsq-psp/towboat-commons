package mujica.reflect.modifier;

import java.lang.annotation.*;

@CodeHistory(date = "2026/10/2")
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE, ElementType.MODULE})
@Repeatable(ResourceCollection.class)
public @interface Resource {

    String name();

    String mimeType() default "";

    CodeHistory[] codeHistoryTimeline() default {};

    ReferencePage[] referenceDocument() default {};

    ReferenceCode[] referenceSources() default {};
}
