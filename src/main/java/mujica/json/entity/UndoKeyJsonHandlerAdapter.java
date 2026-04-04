package mujica.json.entity;

import mujica.ds.generic.set.CollectionConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2026/4/1.
 */
public class UndoKeyJsonHandlerAdapter<H extends JsonHandler> extends JsonHandlerAdapter<H> {

    Object key;

    public UndoKeyJsonHandlerAdapter(H h) {
        super(h);
    }

    @Override
    protected void beforeValue() {
        if (key != null) {
            if (key instanceof FastString) {
                h.stringKey((FastString) key);
            } else {
                h.stringKey(key.toString());
            }
            key = null;
        }
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
}
