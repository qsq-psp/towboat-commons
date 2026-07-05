package mujica.json.reflect;

import mujica.ds.any.set.CollectionConstant;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/4/7")
class ClampedI32Type extends I32Type {

    protected int min;

    protected int max;

    ClampedI32Type(long flags) {
        super(flags);
        if ((flags & JsonHint.UNSIGNED) == 0L) {
            min = Integer.MIN_VALUE;
            max = Integer.MAX_VALUE;
        } else {
            min = 0;
            max = -1;
        }
    }

    @Override
    public void setI32(int newValue) {
        if ((flags & JsonHint.UNSIGNED) == 0L) {
            value = Math.max(min, Math.min(newValue, max));
        } else {
            value = Math.max(min + Integer.MIN_VALUE, Math.min(newValue + Integer.MIN_VALUE, max + Integer.MIN_VALUE)) - Integer.MIN_VALUE;
        }
        state = CollectionConstant.PRESENT;
    }
}
