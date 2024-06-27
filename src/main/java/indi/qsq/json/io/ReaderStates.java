package indi.qsq.json.io;

import indi.qsq.util.value.EnumMapping;

/**
 * Created on 2023/4/29.
 */
public interface ReaderStates {

    int TOKEN_FREE = 0;
    int TOKEN_WORD = 1; // null, false, true
    int TOKEN_INTEGRAL = 2; // without dot or e
    int TOKEN_DECIMAL = 3; // with dot or e
    int TOKEN_STRING_DOUBLE_QUOTE = 4;
    int TOKEN_STRING_DOUBLE_QUOTE_ESCAPE = 5;
    int TOKEN_STRING_SINGLE_QUOTE = 6;
    int TOKEN_STRING_SINGLE_QUOTE_ESCAPE = 7;
    int TOKEN_COMMENT_SLASH = 9;
    int TOKEN_LINE_COMMENT = 10;
    int TOKEN_BLOCK_COMMENT = 11;
    int TOKEN_BLOCK_COMMENT_STAR = 12;

    EnumMapping TOKEN = (new EnumMapping())
            .addDefault("unknown", -1)
            .add("free", TOKEN_FREE)
            .add("word", TOKEN_WORD)
            .add("integral", TOKEN_INTEGRAL)
            .add("decimal", TOKEN_DECIMAL)
            .add("string-double-quote", TOKEN_STRING_DOUBLE_QUOTE)
            .add("string-double-quote-escape", TOKEN_STRING_DOUBLE_QUOTE_ESCAPE)
            .add("string-single-quote", TOKEN_STRING_SINGLE_QUOTE)
            .add("string-single-quote-escape", TOKEN_STRING_SINGLE_QUOTE_ESCAPE)
            .add("comment-slash", TOKEN_COMMENT_SLASH)
            .add("line-comment", TOKEN_LINE_COMMENT)
            .add("block-comment", TOKEN_BLOCK_COMMENT)
            .add("block-comment-star", TOKEN_BLOCK_COMMENT_STAR);

    int HOLD_NOTHING = 0;
    int HOLD_STRING = 1;
    int HOLD_COMMA = 2;
    int HOLD_COLON = 3;
    int HOLD_UNICODE_0 = 4;
    int HOLD_UNICODE_1 = 5;
    int HOLD_UNICODE_2 = 6;
    int HOLD_UNICODE_3 = 7;
    int HOLD_UTF8_1 = 8;
    int HOLD_UTF8_2 = 9;
    int HOLD_UTF8_3 = 10;

    int HOLD_TYPE_MASK = 0xff;

    EnumMapping HOLD = (new EnumMapping())
            .addDefault("unknown", -1)
            .add("nothing", HOLD_NOTHING)
            .add("string", HOLD_STRING)
            .add("comma", HOLD_COMMA)
            .add("colon", HOLD_COLON)
            .add("unicode-0", HOLD_UNICODE_0)
            .add("unicode-1", HOLD_UNICODE_1)
            .add("unicode-2", HOLD_UNICODE_2)
            .add("unicode-3", HOLD_UNICODE_3)
            .add("utf-8-1", HOLD_UTF8_1)
            .add("utf-8-2", HOLD_UTF8_2)
            .add("utf-8-3", HOLD_UTF8_3);
}
