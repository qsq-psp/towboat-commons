package indi.um.json.io;

import indi.um.json.api.JsonConsumer;
import indi.um.util.ds.Index;
import indi.um.util.text.Quote;
import indi.um.util.text.StringUtility;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ByteProcessor;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Created on 2022/8/22.
 */
public class UriFormDataReader implements RecursiveReader {

    private final String string;

    private int pos;

    private Consumer<RecursiveReader> callback;

    public UriFormDataReader(String string) {
        super();
        this.string = string;
    }

    public UriFormDataReader(ByteBuf byteBuf) {
        this(byteBuf.toString(StandardCharsets.UTF_8));
    }

    public String getString(String key) {
        final int keyLength = key.length();
        int current = 0;
        boolean hasNext = true;
        while (hasNext) {
            int next = string.indexOf('&', current);
            if (next == -1) {
                next = string.length();
                hasNext = false;
            }
            int equal = string.indexOf('=', current);
            if (equal < next && equal - current == keyLength && string.substring(current, equal).equals(key)) {
                return string.substring(equal + 1, next);
            }
            current = next + 1;
        }
        return null;
    }

    @Override
    public void config(int config) {
        // pass
    }

    @Override
    public void read(@NotNull JsonConsumer jc) {
        callback = null;
        final int length = string.length();
        int current = pos;
        jc.openObject();
        while (current < length) {
            int next = string.indexOf('&', current);
            if (next == -1) {
                next = length;
            }
            int equal = string.indexOf('=', current);
            if (equal != -1 && equal < next) {
                pos = equal + 1;
                jc.key(StringUtility.decodeUri(string.substring(current, equal)).toString());
                if (callback == null) {
                    jc.stringValue(StringUtility.decodeUri(string.substring(equal + 1, next)).toString());
                } else {
                    pos = next;
                    callback.accept(this);
                    callback = null;
                }
            } else {
                pos = next;
                jc.key(StringUtility.decodeUri(string.substring(current, next)).toString());
                if (callback == null) {
                    jc.booleanValue(true);
                } else {
                    callback.accept(this);
                    callback = null;
                }
            }
            current = next + 1;
        }
        jc.closeObject();
    }

    @Override
    public void skip(@NotNull Consumer<RecursiveReader> callback) {
        this.callback = callback;
    }

    @Override
    @SuppressWarnings("DefaultAnnotationParam")
    public String raw(@Index(inclusive = true) int fromIndex, @Index(inclusive = false) int toIndex) {
        return string.substring(fromIndex, toIndex);
    }

    @Override
    public void setPosition(int pos) {
        this.pos = pos;
    }

    @Override
    public int getPosition() {
        return pos;
    }

    @Override
    public void stringifyNeighbors(StringBuilder sb, int before, int after) {
        before = Math.max(0, pos - before);
        after = Math.min(pos + after, string.length());
        Quote.DEFAULT.append(sb, string, before, after);
    }
}
