package mujica.ds.of_long.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/5.
 */
@CodeHistory(date = "2026/6/5")
public class SimpleLongList extends AbstractLongList {

    @NotNull
    private final LongSequence longSequence;

    public SimpleLongList(@NotNull LongSequence longSequence) {
        super();
        this.longSequence = longSequence;
    }

    @Override
    public int longLength() {
        return longSequence.longLength();
    }

    @Override
    public long getLong(int index) {
        return longSequence.getLong(index);
    }
}
