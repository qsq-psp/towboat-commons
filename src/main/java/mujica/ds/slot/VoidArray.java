package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;

@CodeHistory(date = "2026/8/14")
public class VoidArray implements Serializable {

    private static final long serialVersionUID = 0x436f35bb74f1d713L;

    public static final VoidArray EMPTY_ARRAY = new VoidArray(0);

    final int length;

    public VoidArray(int length) {
        super();
        this.length = length;
    }
}
