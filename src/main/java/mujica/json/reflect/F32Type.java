package mujica.json.reflect;

import mujica.ds.any.set.CollectionConstant;
import mujica.ds.f32.F32Slot;
import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2022/8/8", project = "Ultramarine", name = "JsonFloatType")
@CodeHistory(date = "2026/6/11")
class F32Type extends JsonType implements F32Slot {

    private static final long serialVersionUID = 0x97c97348b35716f4L;

    transient float value;

    F32Type(long flags) {
        super(flags);
    }

    @Override
    public float getF32() {
        if (state == CollectionConstant.PRESENT) {
            return value;
        }
        if (state == CollectionConstant.EMPTY) {
            throw new NullPointerException();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void setF32(float newValue) {
        value = newValue;
        state = CollectionConstant.PRESENT;
    }
}
