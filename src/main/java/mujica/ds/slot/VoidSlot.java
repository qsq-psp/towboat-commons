package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@CodeHistory(date = "2026/8/14")
public class VoidSlot implements Serializable {

    private static final long serialVersionUID = 0x483a9efd70c577d5L;

    public static final VoidSlot INSTANCE = new VoidSlot();

    public VoidSlot() {
        super();
    }

    @NotNull
    private Object readResolve() {
        return INSTANCE;
    }
}
