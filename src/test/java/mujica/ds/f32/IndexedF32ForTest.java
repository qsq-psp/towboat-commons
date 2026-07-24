package mujica.ds.f32;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/23")
public class IndexedF32ForTest extends F32 {

    private static final long serialVersionUID = 0xBC4BD8766510E1D7L;

    int index;

    public IndexedF32ForTest() {
        super();
    }

    IndexedF32ForTest(float value, int index) {
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
    public IndexedF32ForTest clone() {
        return new IndexedF32ForTest(value, index);
    }
}
