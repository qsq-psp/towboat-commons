package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/3/29")
public interface JsonAsyncStringReader extends JsonReader {

    @Override
    void setFlags(int flags);

    @Override
    int getFlags();

    void read(@NotNull CharSequence string, @Index(of = "string") int startIndex, @Index(of = "string", inclusive = false) int endIndex);
}
