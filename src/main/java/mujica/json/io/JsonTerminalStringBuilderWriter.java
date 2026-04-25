package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/8/18", project = "Ultramarine", name = "TerminalScriptStringWriter")
@CodeHistory(date = "2026/4/24")
public class JsonTerminalStringBuilderWriter extends JsonIndentStringBuilderWriter {

    public JsonTerminalStringBuilderWriter(@NotNull StringBuilder sb) {
        super(sb);
    }

    public JsonTerminalStringBuilderWriter() {
        super();
    }
}
