package mujica.io.stream;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2021/1/7", project = "webbiton", name = "ContentLengthInputStream")
@CodeHistory(date = "2022/6/15")
public class LengthInputStream extends FilterInputStream {

    long remaining;

    long markedRemaining;

    public LengthInputStream(@NotNull InputStream in, long length) {
        super(in);
        this.remaining = length;
    }

    public long getRemaining() {
        return remaining;
    }

    public void setRemaining(long remaining) {
        this.remaining = remaining; // do not use it when marked
    }

    @Override
    public int read() throws IOException {
        if (remaining > 0) {
            remaining--;
            return in.read();
        } else {
            return -1;
        }
    }

    @Override
    public int read(@NotNull byte[] bytes, int offset, int length) throws IOException {
        if (remaining > 0) {
            int count = in.read(bytes, offset, (int) Math.min(remaining, length));
            if (count > 0) {
                remaining -= count;
            }
            return count;
        } else {
            return -1;
        }
    }

    @Override
    public long skip(long length) throws IOException {
        if (length > 0) {
            long count = in.skip(Math.min(remaining, length));
            if (count > 0) {
                remaining -= count;
            }
            return count;
        } else {
            return 0;
        }
    }

    @Override
    public int available() throws IOException {
        return (int) Math.min(remaining, in.available()); // the result is always in int range, because in.available() is int type
    }

    @Override
    public synchronized void mark(int readAheadLimit) {
        in.mark(readAheadLimit);
        markedRemaining = remaining;
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
        remaining = markedRemaining;
    }
}
