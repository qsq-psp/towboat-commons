package mujica.json.io;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/2/28.
 */
@CodeHistory(date = "2026/2/28")
public class JsonCharSequenceReader implements JsonSyncReader {

    @NotNull
    private final CharSequence in;

    @NotNull
    private final StringBuilder sb = new StringBuilder();

    private int flags;

    public JsonCharSequenceReader(@NotNull CharSequence in) {
        super();
        this.in = in;
    }

    @Override
    public void config(int flags) {
        this.flags = flags;
    }

    @Override
    public void read(@NotNull JsonHandler jh) {
        read(jh, 0);
    }

    public void read(@NotNull JsonHandler jh, int startIndex) {

    }
}
