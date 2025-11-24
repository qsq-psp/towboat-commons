package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FlagName;

@CodeHistory(date = "2025/11/6")
final class JsonReaderFlagNames {

    static final FlagName FLAG_NAME = (new FlagName())
            .addFlag(JsonReader.FLAG_LINE_COMMENT, "line-comment")
            .addFlag(JsonReader.FLAG_BLOCK_COMMENT, "block-comment")
            .addFlag(JsonReader.FLAG_SINGLE_QUOTE_STRING, "single-quote-string")
            .addFlag(JsonReader.FLAG_TRAILING_COMMA, "trailing-comma")
            .addFlag(JsonReader.FLAG_INFINITY_NAN_EXTENSION, "infinity-NaN-extension")
            .addFlag(JsonReader.FLAG_SPARSE_ARRAY_EXTENSION, "sparse-array-extension")
            .addFlag(JsonReader.FLAG_INTEGRAL_OVERFLOW_TO_DOUBLE, "integral-overflow-to-double")
            .addFlag(JsonReader.FLAG_INTEGRAL_OVERFLOW_TO_RAW, "integral-overflow-to-raw")
            .addFlag(JsonReader.FLAG_DECIMAL_OVERFLOW_TO_RAW, "decimal-overflow-to-raw")
            .addFlag(JsonReader.FLAG_INTEGRAL_FORCE_TO_DOUBLE, "integral-force-to-double")
            .addFlag(JsonReader.FLAG_INTEGRAL_FORCE_TO_RAW, "integral-force-to-raw")
            .addFlag(JsonReader.FLAG_DECIMAL_FORCE_TO_RAW, "decimal-force-to-raw");

    private JsonReaderFlagNames() {
        super();
    }
}
