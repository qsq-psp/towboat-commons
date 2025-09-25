package mujica.text.escape;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ConstantInterface;

/**
 * Meaning of boundary: left (prefix) and right (suffix)
 */
@ConstantInterface
@CodeHistory(date = "2022/5/25", project = "Ultramarine")
@CodeHistory(date = "2025/3/3")
public interface QuoteBoundary {

    int APOSTROPHE          = 0x1; // 'string'
    int QUOTATION           = 0x2; // "string'
    int GRAVE               = 0x4; // `string`
    int ASTERISK            = 0x8; // *string*

    int DOUBLE_ASTERISK     = ASTERISK << 8; // **string**

    int TRIPLE_APOSTROPHE   = APOSTROPHE << 16; // '''string'''
    int TRIPLE_QUOTATION    = QUOTATION << 16; // """string"""
    int TRIPLE_GRAVE        = GRAVE << 16; // ```string```
}
