package mujica.json.modifier;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2021/12/31", project = "infrastructure", name = "SerializeFrom, SerializeToNull, SerializeToUndefined")
@CodeHistory(date = "2022/6/9", project = "Ultramarine", name = "SerializeFrom, SerializeToNull, SerializeToUndefined")
@CodeHistory(date = "2025/10/29")
public @interface Serialize {

    int FROM_NULL                = 0x0001;
    int FROM_FALSE               = 0x0002;
    int FROM_ZERO_INTEGRAL       = 0x0004;
    int FROM_ZERO_DECIMAL        = 0x0008;
    int FROM_INFINITE            = 0x0010;
    int FROM_NAN                 = 0x0020;
    int FROM_EMPTY_STRING        = 0x0040;
    int FROM_BLANK_STRING        = 0x0080;
    int FROM_EMPTY_ARRAY         = 0x0200;
    int FROM_EMPTY_OBJECT        = 0x0400;
    int FROM_CYCLIC_OBJECT       = 0x0800;
    int FROM_DEFAULT_ENUM        = 0x1000;
    int FROM_OUT_ENUM            = 0x2000;
    int FROM_SPECIFIC            = 0x4000;

    String fromSpecific() default "";

    int toNull() default 0;

    int toUndefined() default 0;
}
