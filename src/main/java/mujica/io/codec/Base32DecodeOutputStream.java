package mujica.io.codec;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.LongDataView;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created in UltraIO on 2025/4/29.
 * Moved here on 2025/4/30.
 */
public class Base32DecodeOutputStream extends FilterOutputStream implements Base32StreamingCodec {

    private long buffer;

    private static final int SRC_STEP = CODE_STEP; // 5
    private static final int DST_STEP = MESSAGE_STEP; // 8

    private int srcShift;
    private int dstShift;
    private int stopShift;

    @NotNull
    private BufferingPolicy policy;

    public Base32DecodeOutputStream(@NotNull OutputStream out, @NotNull BufferingPolicy policy) {
        super(out);
        this.policy = policy;
    }

    public Base32DecodeOutputStream(@NotNull OutputStream out) {
        this(out, BufferingPolicy.NORMAL);
    }

    @NotNull
    @Override
    public DataView getDataView() {
        return new LongDataView(this::getBuffer, ByteFillPolicy.MIDDLE_TO_RIGHT, START_SHIFT);
    }

    @Override
    public long getBuffer() {
        return buffer;
    }

    @NotNull
    @Override
    public BufferingPolicy getPolicy() {
        return policy;
    }

    @Override
    public void setPolicyDelayed(@NotNull BufferingPolicy policy) {
        this.policy = policy;
    }

    @Override
    public void setPolicyImmediately(@NotNull BufferingPolicy policy) throws IOException {
        this.policy = policy;
    }

    private void write() throws IOException {
        dstShift -= DST_STEP;
        out.write(0xff & (int) (buffer >> dstShift));
    }

    @Override
    public void write(int digit) throws IOException {
        digit &= 0xff;
        if (digit == '=') {
            if (stopShift == 0 && srcShift / DST_STEP == (srcShift + (SRC_STEP - 1)) / DST_STEP) { // test pass trick
                throw new IOException();
            }
            stopShift += SRC_STEP;
            if (stopShift >= srcShift) {
                while (dstShift - DST_STEP >= srcShift) {
                    write();
                }
                buffer = 0L;
                srcShift = START_SHIFT;
                dstShift = START_SHIFT;
                stopShift = 0;
            }
        } else {
            if (srcShift == 0) {
                while (dstShift > 0) {
                    write();
                }
                buffer = 0L;
                srcShift = START_SHIFT;
                dstShift = START_SHIFT;
            }
            if ('A' <= digit && digit <= 'Z') {
                digit -= 'A';
            } else if ('a' <= digit && digit <= 'z') {
                digit -= 'z';
            } else if ('2' <= digit && digit <= '7') {
                digit += 26 - '2';
            } else {
                throw new BadCodeException(Quote.DEFAULT.apply((byte) digit) + " not in [2-7A-Za-z]");
            }
            srcShift -= SRC_STEP;
            buffer |= ((long) digit) << srcShift;
            if (policy == BufferingPolicy.MINIMAL) {
                while (dstShift - DST_STEP >= srcShift) {
                    write();
                }
            } else if (policy == BufferingPolicy.NORMAL && srcShift == 0) {
                while (dstShift > 0) {
                    write();
                }
                buffer = 0L;
                srcShift = START_SHIFT;
                dstShift = START_SHIFT;
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (stop()) {
            out.flush();
        }
        out.close();
    }

    private boolean stop() throws IOException {
        if (srcShift == START_SHIFT) {
            return false;
        }
        if (srcShift != 0) {
            throw new IOException();
        }
        if (dstShift == 0) {
            return false;
        }
        while (dstShift > 0) {
            write();
        }
        return true;
    }
}
