package indi.qsq.io.string;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created on 2024/6/28.
 */
public abstract class IOString {

    @Nullable
    private WeakReference<String> cache;

    @NotNull
    protected abstract String decode();

    @Override
    @NotNull
    public String toString() {
        WeakReference<String> cache = this.cache;
        if (cache != null) {
            String cached = cache.get();
            if (cached != null) {
                return cached;
            }
        }
        final String decoded = decode();
        this.cache = new WeakReference<>(decoded);
        return decoded;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
