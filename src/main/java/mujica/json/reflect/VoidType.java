package mujica.json.reflect;

import mujica.ds.any.set.CollectionConstant;
import mujica.json.handler.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/8/15")
class VoidType extends JsonType {

    static final VoidType NOP = new VoidType();

    @NotNull
    protected transient CollectionConstant state = CollectionConstant.UNDEFINED;

    VoidType() {
        super();
    }

    VoidType(long flags) {
        super(flags);
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        state = CollectionConstant.PRESENT;
        return this;
    }

    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
        } else {
            throw new IllegalStateException();
        }
    }

    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
            out.skippedValue();
        } else {
            throw new IllegalStateException();
        }
    }

    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
            return CollectionConstant.UNDEFINED;
        } else {
            throw new IllegalStateException();
        }
    }
}
