package mujica.ds.f64;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/17")
public class IndexedF64ForTest extends F64 {

    private static final long serialVersionUID = 0x486417E9682E5A3FL;

    int index;

    public IndexedF64ForTest() {
        super();
    }

    IndexedF64ForTest(double value, int index) {
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
    public IndexedF64ForTest clone() {
        return new IndexedF64ForTest(value, index);
    }
}
