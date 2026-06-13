package mujica.json.handler;

import mujica.ds.generic.set.CollectionConstant;
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
            h.stringKey((FastString) key);
        } else {
            h.stringKey(key.toString());
        }
        key = null;
    }

    @Override
    public void stringKey(@NotNull String key) {
        if (this.key != null) {
            throw new IllegalStateException();
        }
        this.key = key;
    }

    @Override
    public void stringKey(@NotNull FastString key) {
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
