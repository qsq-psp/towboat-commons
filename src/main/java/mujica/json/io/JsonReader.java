package mujica.json.io;

import mujica.reflect.modifier.FlagName;
import mujica.reflect.modifier.CodeHistory;

/**
 * built with input and output, used only once
 */
@CodeHistory(date = "2022/6/12", project = "Ultramarine", name = "ReaderStates")
@CodeHistory(date = "2023/4/29", project = "Ultramarine", name = "ReaderFlags")
@CodeHistory(date = "2025/10/27")
public interface JsonReader {

    int FLAG_LINE_COMMENT            = 0x01;
    int FLAG_BLOCK_COMMENT           = 0x02;
    int FLAG_SINGLE_QUOTE_STRING     = 0x04;
    int FLAG_TRAILING_COMMA          = 0x08;
    int FLAG_RAW_DECIMAL             = 0x10;

    FlagName CONFIG_FLAG = (new FlagName())
            .addFlag(FLAG_LINE_COMMENT, "line-comment")
            .addFlag(FLAG_BLOCK_COMMENT, "block-comment")
            .addFlag(FLAG_SINGLE_QUOTE_STRING, "single-quote-string")
            .addFlag(FLAG_TRAILING_COMMA, "trailing-comma")
            .addFlag(FLAG_RAW_DECIMAL, "raw-decimal");

    void config(int config);

    // skip supported and skip/jump methods
}
