package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/7/24")
public abstract class PrimitiveArithmetic<S, A> extends AbstractArithmetic<S> implements SlotArrayComparator<S, A> {

    protected PrimitiveArithmetic() {
        super();
    }
}
