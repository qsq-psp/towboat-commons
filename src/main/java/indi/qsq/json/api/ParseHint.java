package indi.qsq.json.api;

import java.lang.annotation.*;

/**
 * Created in va on 2021/12/28, named ParseCast.
 * Recreated on 2021/12/26.
 */
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParseHint {

    int ACCEPT_NULL             = 0x00000001;
    int ACCEPT_BOOLEAN          = 0x00000002;
    int ACCEPT_NUMBER           = 0x00000004;
    int ACCEPT_STRING           = 0x00000008;
    int ACCEPT_ARRAY            = 0x00000010;

    int ACCEPT_BIN_STRING       = 0x00000020;
    int ACCEPT_DEC_STRING       = 0x00000040;
    int ACCEPT_HEX_STRING       = 0x00000080;
    int ACCEPT_BASE_N_STRING    = 0x00000100;
    int ACCEPT_DOT_STRING       = 0x00000200;
    int ACCEPT_SCI_STRING       = 0x00000400;
    int ACCEPT_INFINITY_STRING  = 0x00000800;
    int ACCEPT_NAN_STRING       = 0x00001000;
    int ACCEPT_DEC_UNITS        = 0x00002000;
    int ACCEPT_BIN_UNITS        = 0x00004000;

    int ACCEPT_ENUM_OUT         = 0x00008000;

    int APPLY_TRUNCATE          = 0x00010000;
    int APPLY_CLAMP             = 0x00020000;
    int APPLY_ROUND_ZERO        = 0x00040000;
    int APPLY_ROUND_FLOOR       = 0x00080000;
    int APPLY_ROUND_CEIL        = 0x00100000;
    int APPLY_ROUND_NEAR        = 0x00200000;

    int WRAP_SINGLE_VALUE       = 0x01000000;
    int APPEND_ARRAY            = 0x02000000;
    int CLEAR_COLLECTION        = 0x04000000;

    int value();
}
