package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;

/**
 * built with input and output, used only once
 */
@CodeHistory(date = "2022/6/12", project = "Ultramarine", name = "ReaderStates")
@CodeHistory(date = "2023/4/29", project = "Ultramarine", name = "ReaderFlags")
@CodeHistory(date = "2025/10/27")
public interface JsonReader {

    int FLAG_LINE_COMMENT                       = 0x0001;
    int FLAG_BLOCK_COMMENT                      = 0x0002;
    int FLAG_APOSTROPHE_QUOTE_STRING            = 0x0004;
    int FLAG_GRAVE_ACCENT_QUOTE_STRING          = 0x0008;
    int FLAG_LEADING_COMMA                      = 0x0010;
    int FLAG_TRAILING_COMMA                     = 0x0020;
    int FLAG_PLUS_SIGN_NUMBER                   = 0x0040;
    int FLAG_INFINITY_NAN_EXTENSION             = 0x0080;
    int FLAG_INTEGRAL_OVERFLOW_TO_FRACTIONAL    = 0x0100;
    int FLAG_INTEGRAL_OVERFLOW_TO_RAW           = 0x0200;
    int FLAG_FRACTIONAL_OVERFLOW_TO_RAW         = 0x0400;
    int FLAG_INTEGRAL_FORCE_TO_FRACTIONAL       = 0x0800;
    int FLAG_INTEGRAL_FORCE_TO_RAW              = 0x1000;
    int FLAG_FRACTIONAL_FORCE_TO_RAW            = 0x2000;

    void config(int flags);

    // skip supported and skip/jump methods
}
