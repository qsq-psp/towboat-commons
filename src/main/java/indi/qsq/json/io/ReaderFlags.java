package indi.qsq.json.io;

import indi.qsq.util.value.EnumMapping;
import indi.qsq.util.value.FlagName;

/**
 * Created on 2022/6/12, named ReaderStates.
 * Renamed on 2023/4/29.
 */
public interface ReaderFlags {

    int FLAG_LINE_COMMENT            = 0x01;
    int FLAG_BLOCK_COMMENT           = 0x02;
    int FLAG_SINGLE_QUOTE_STRING     = 0x04;
    int FLAG_TRAILING_COMMA          = 0x08;
    int FLAG_RAW_DECIMAL             = 0x10;

    FlagName READER_FLAG = (new FlagName())
            .addFlag(FLAG_LINE_COMMENT, "line-comment")
            .addFlag(FLAG_BLOCK_COMMENT, "block-comment")
            .addFlag(FLAG_SINGLE_QUOTE_STRING, "single-quote-string")
            .addFlag(FLAG_TRAILING_COMMA, "trailing-comma")
            .addFlag(FLAG_RAW_DECIMAL, "raw-decimal");

    void config(int config);
}
