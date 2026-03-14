package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.FlagName;

@CodeHistory(date = "2025/11/6")
final class JsonReaderFlagNames {

    static final FlagName FLAG_NAME = (new FlagName())
            .addFlag(JsonReader.FLAG_LINE_COMMENT, "line-comment")
            .addFlag(JsonReader.FLAG_BLOCK_COMMENT, "block-comment");

    private JsonReaderFlagNames() {
        super();
    }
}
