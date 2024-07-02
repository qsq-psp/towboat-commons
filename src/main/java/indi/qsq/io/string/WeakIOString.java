package indi.qsq.io.string;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created on 2024/7/2.
 */
public abstract class WeakIOString extends AbstractIOString {

    @Nullable
    private WeakReference<String> cache;

    @NotNull
    protected abstract String decode();

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

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
}
