package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2026/4/8")
class BigIntegerType extends JsonType {

    transient BigInteger value;

    BigIntegerType() {
        super();
    }

    BigIntegerType(long flags) {
        super(flags);
    }

    @NotNull
    @Override
    JsonType from() {
        if ((flags & JsonHint.NULLABLE) != 0L) {
            value = null;
            state = CollectionConstant.PRESENT;
        } else {
            throw new NullPointerException();
        }
        return this;
    }
}
