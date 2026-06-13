package mujica.json.handler;

import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_char.format.AppenderToStringBuilder;
import mujica.json.container.FastString;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.function.Predicate;

@CodeHistory(date = "2026/6/6")
public class LateKeyCheckAdapter<H extends JsonHandler> extends UndoKeyJsonHandlerAdapter<H> {

    @NotNull
    protected final TruncateList<HashSet<String>> stack = new TruncateList<>();

    @NotNull
    protected final Predicate<String> predicate;

    public LateKeyCheckAdapter(@NotNull H h, @NotNull Predicate<String> predicate) {
        super(h);
        this.predicate = predicate;
    }

    public LateKeyCheckAdapter(@NotNull H h) {
        this(h, string -> true); // only checks duplicate keys
    }

    public void reset() {
        stack.clear();
    }

    @Override
    public boolean duplicateKeyChecked() {
        return true;
    }

    @Override
    protected void beforeValue() {
        if (key == null) {
            return;
        }
        final String keyString = key.toString();
        if (!predicate.test(keyString)) {
            throw new IllegalArgumentException("denied key " + AppenderToStringBuilder.Json.get().apply(keyString));
        }
        final HashSet<String> set = stack.getLast();
        if (set == null) {
            throw new IllegalStateException("not object");
        }
        if (!set.add(keyString)) {
            throw new IllegalArgumentException("duplicate key " + AppenderToStringBuilder.Json.get().apply(keyString));
        }
        if (key instanceof FastString) {
            h.stringKey((FastString) key);
        } else {
            h.stringKey(key.toString());
        }
        key = null;
    }

    @Override
    public void openArray() {
        beforeValue();
        stack.add(null);
        h.openArray();
    }

    @Override
    public void closeArray() {
        h.closeArray();
        if (stack.removeLast() != null) {
            throw new IllegalStateException("not array");
        }
        afterValue();
    }

    @Override
    public void openObject() {
        beforeValue();
        stack.add(new HashSet<>());
        h.openObject();
    }

    @Override
    public void closeObject() {
        h.closeObject();
        if (stack.removeLast() == null) {
            throw new IllegalStateException("not object");
        }
        afterValue();
    }

    @NotNull
    @Override
    public String toString() {
        return stack.toString();
    }
}
