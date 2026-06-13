package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/5/29.
 */
@CodeHistory(date = "2026/5/29")
class AnyType extends JsonType {

    private static final long serialVersionUID = 0xD0ACA59DD0AA6B18L;

    static final AnyType ANY = new AnyType();

    protected transient Object value;

    AnyType() {
        super();
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        value = object;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state != CollectionConstant.PRESENT) {
            throw new IllegalStateException();
        }
        state = CollectionConstant.UNDEFINED;
        if (value == null) {
            out.nullValue();
            return;
        }
        final Object self = value;
        value = null;
        if (self instanceof JsonStructure) {
            if (self instanceof JsonStructure.ExposedEntries) {
                out.openObject();
                ((JsonStructure) self).json(out);
                out.closeObject();
            } else {
                ((JsonStructure) self).json(out);
            }
            return;
        }
        context.forClass(self.getClass()).transform(self, out, context);
    }
}
