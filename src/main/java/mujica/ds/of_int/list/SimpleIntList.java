package mujica.ds.of_int.list;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/5/31")
public class SimpleIntList extends AbstractIntList {

    private static final long serialVersionUID = 0xe52d308dc20935dcL;

    @NotNull
    private final IntSequence intSequence;

    public SimpleIntList(@NotNull IntSequence intSequence) {
        super();
        this.intSequence = intSequence;
    }

    @Override
    public int intLength() {
        return intSequence.intLength();
    }

    @Override
    public int getInt(int i) {
        return intSequence.getInt(i);
    }
}
