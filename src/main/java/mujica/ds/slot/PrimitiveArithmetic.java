package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/7/24")
public abstract class PrimitiveArithmetic<S, A> extends AbstractArithmetic<S> implements SlotArrayComparator<S, A> {

    private static final long serialVersionUID = 0x4A92C2E95CE5878CL;

    protected PrimitiveArithmetic() {
        super();
    }
}
