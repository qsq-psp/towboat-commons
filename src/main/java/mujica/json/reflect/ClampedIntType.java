package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/4/7")
class ClampedIntType extends IntType {

    protected int min;

    protected int max;

    ClampedIntType(long flags) {
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
    public void setInt(int newValue) {
        if ((flags & JsonHint.UNSIGNED) == 0L) {
            value = Math.max(min, Math.min(newValue, max));
        } else {
            value = Math.max(min + Integer.MIN_VALUE, Math.min(newValue + Integer.MIN_VALUE, max + Integer.MIN_VALUE)) - Integer.MIN_VALUE;
        }
        state = CollectionConstant.PRESENT;
    }
}
