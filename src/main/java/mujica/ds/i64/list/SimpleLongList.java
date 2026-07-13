package mujica.ds.i64.list;

import mujica.ds.i64.ReadOnlyI64Array;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/5.
 */
@CodeHistory(date = "2026/6/5")
public class SimpleLongList extends AbstractLongList {

    @NotNull
    private final ReadOnlyI64Array longSequence;

    public SimpleLongList(@NotNull ReadOnlyI64Array longSequence) {
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
