package mujica.json.handler;

import mujica.ds.generic.list.TruncateList;
import mujica.ds.of_char.format.AppenderToStringBuilder;
import mujica.json.container.FastString;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.function.Predicate;

@CodeHistory(date = "2026/6/12")
public class EarlyKeyCheckAdapter<H extends JsonHandler> extends JsonHandlerAdapter<H> {

    @NotNull
    protected final TruncateList<HashSet<String>> stack = new TruncateList<>();

    @NotNull
    protected final Predicate<String> predicate;

    public EarlyKeyCheckAdapter(@NotNull H h, @NotNull Predicate<String> predicate) {
        super(h);
        this.predicate = predicate;
    }

    public EarlyKeyCheckAdapter(@NotNull H h) {
        this(h, string -> true); // only checks duplicate keys
    }

    public void reset() {
        stack.clear();
    }

    @Override
    public boolean duplicateKeyChecked() {
        return true;
    }

    public void checkKey(@NotNull String keyString) {
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
    }

    @Override
    public void stringKey(@NotNull String key) {
        checkKey(key);
        h.stringKey(key);
    }

    @Override
    public void stringKey(@NotNull FastString key) {
        checkKey(key.toString());
        h.stringKey(key);
    }
}
