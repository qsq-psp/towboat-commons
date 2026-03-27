package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/11/14")
class Getter {

    Getter() { // for finding usage
        super(); // pass
    }

    protected Object invoke(@Nullable Object self) {
        return CollectionConstant.UNDEFINED;
    }

    @NotNull
    protected Getter tryUnreflect(@NotNull MethodHandles.Lookup lookup) {
        return this; // if fail to unreflect
    }
}
