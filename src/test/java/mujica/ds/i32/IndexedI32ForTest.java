package mujica.ds.i32;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/16")
public class IndexedI32ForTest extends I32 {

    private static final long serialVersionUID = 0x797C68F73DF9E2BDL;

    int index;

    public IndexedI32ForTest() {
        super();
    }

    IndexedI32ForTest(int value, int index) {
        super(value);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @NotNull
    @Override
    public IndexedI32ForTest clone() {
        return new IndexedI32ForTest(value, index);
    }
}
