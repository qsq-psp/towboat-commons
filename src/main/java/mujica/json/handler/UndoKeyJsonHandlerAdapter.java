package mujica.json.handler;

import mujica.ds.any.set.CollectionConstant;
import mujica.json.container.FastString;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/4/1")
@DirectSubclass({LateKeyCheckAdapter.class})
public class UndoKeyJsonHandlerAdapter<H extends JsonHandler> extends JsonHandlerAdapter<H> {

    protected Object key;

    public UndoKeyJsonHandlerAdapter(@NotNull H h) {
        super(h);
    }

    @Override
    public boolean supportsUndoKey() {
        return true;
    }

    @Override
    protected void beforeValue() {
        if (key == null) {
            return;
        }
        if (key instanceof FastString) {
            h.key((FastString) key);
        } else {
            h.key(key.toString());
        }
        key = null;
    }

    @Override
    public void key(@NotNull String key) {
        if (this.key != null) {
            throw new IllegalStateException();
        }
        this.key = key;
    }

    @Override
    public void key(@NotNull FastString key) {
        if (this.key != null) {
            throw new IllegalStateException();
        }
        this.key = key;
    }

    @Override
    public void simpleValue(@Nullable Object value) {
        if (value == CollectionConstant.UNDEFINED) {
            key = null; // undo key
        } else {
            super.simpleValue(value);
        }
    }

    @Override
    public void skippedValue() {
        key = null;
    }
}
