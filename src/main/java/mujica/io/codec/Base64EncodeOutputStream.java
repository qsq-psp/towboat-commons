package mujica.io.codec;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.IntDataView;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created in webbiton on 2021/6/1, named Base64OutputStream.
 * Recreated in omnidirectional on 2025/4/14, named Base64OutputStream.
 * Recreated in UltraIO on 2025/4/16.
 * Moved here on 2025/4/26.
 */
public class Base64EncodeOutputStream extends FilterOutputStream implements Base64StreamingEncoder {

    private int buffer;

    private static final int STATE_SRC_0_DST_0 = 0;
    private static final int STATE_SRC_1_DST_0 = 1;
    private static final int STATE_SRC_2_DST_0 = 2;
    private static final int STATE_SRC_3_DST_0 = 3;
    private static final int STATE_SRC_1_DST_1 = 4;
    private static final int STATE_SRC_2_DST_1 = 5;
    private static final int STATE_SRC_3_DST_1 = 6;
    private static final int STATE_SRC_2_DST_2 = 7;
    private static final int STATE_SRC_3_DST_2 = 8;

    private int state;

    private int flags;

    @NotNull
    private BufferingPolicy policy;

    public Base64EncodeOutputStream(@NotNull OutputStream out, @NotNull BufferingPolicy policy, int flags) {
        super(out);
        this.policy = policy;
        setFlags(flags);
    }

    public Base64EncodeOutputStream(@NotNull OutputStream out, @NotNull BufferingPolicy policy) {
        this(out, policy, FLAG_STOP_ON_CLOSE);
    }

    public Base64EncodeOutputStream(@NotNull OutputStream out, int flags) {
        this(out, BufferingPolicy.NORMAL, flags);
    }

    public Base64EncodeOutputStream(@NotNull OutputStream out) {
        this(out, BufferingPolicy.NORMAL, FLAG_STOP_ON_CLOSE);
    }

    private void encode(int value) throws IOException {
        value &= 0x3f;
        if (value < 26) {
            out.write('A' + value);
        } else if (value < 52) {
            out.write('a' - 26 + value);
        } else if (value < 62) {
            out.write('0' - 52 + value);
        } else if (value == 62) {
            out.write(0xff & (flags >> 16));
        } else { // value == 63
            out.write(0xff & (flags >> 24));
        }
    }

    @NotNull
    @Override
    public DataView getDataView() {
        return new IntDataView(this::getBuffer, ByteFillPolicy.MIDDLE_TO_RIGHT, 24);
    }

    @Override
    public int getBuffer() {
        return buffer;
    }

    @Override
    @NotNull
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
        if (policy == BufferingPolicy.GREEDY) {
            return;
        }
        switch (state) {
            case STATE_SRC_3_DST_0:
                encode(buffer >> 18);
                // no break here
            case STATE_SRC_3_DST_1:
                encode(buffer >> 12);
                // no break here
            case STATE_SRC_3_DST_2:
                encode(buffer >> 6);
                encode(buffer);
                state = STATE_SRC_0_DST_0;
                // no break here
            case STATE_SRC_0_DST_0:
                return; // not break here
        }
        if (policy == BufferingPolicy.NORMAL) {
            return;
        }
        switch (state) {
            case STATE_SRC_1_DST_0:
                encode(buffer >> 18);
                state = STATE_SRC_1_DST_1;
                break;
            case STATE_SRC_2_DST_0:
                encode(buffer >> 18);
                // no break here
            case STATE_SRC_2_DST_1:
                encode(buffer >> 12);
                state = STATE_SRC_2_DST_2;
                break;
        }
    }

    @Override
    public void setFlags(int newFlags) {
        boolean url = (newFlags & FLAG_URL) != 0;
        newFlags &= 0xffff;
        if (url) {
            newFlags |= ('-' << 16) | ('_' << 24); // '-' for 62, '_' for 63
        } else {
            newFlags |= ('+' << 16) | ('/' << 24); // '+' for 62, '/' for 63
        }
        flags = newFlags;
    }

    @Override
    public boolean hasFlag(int testFlag) {
        return (flags & testFlag) != 0;
    }

    @Override
    public void write(int octet) throws IOException {
        octet &= 0xff;
        if (octet == 0x00 && hasFlag(FLAG_CONSUME_NULL)) {
            if (hasFlag(FLAG_STOP_ON_NULL)) {
                stop();
            }
            return;
        }
        if (octet == 0x0a && hasFlag(FLAG_CONSUME_LINE_FEED)) { // line-feed, LF, 0x0a
            if (hasFlag(FLAG_STOP_ON_LINE_FEED)) {
                stop();
            }
            return;
        }
        switch (state) {
            case STATE_SRC_3_DST_0:
                encode(buffer >> 18);
                // no break here
            case STATE_SRC_3_DST_1:
                encode(buffer >> 12);
                // no break here
            case STATE_SRC_3_DST_2:
                encode(buffer >> 6);
                encode(buffer);
                // no break here
            case STATE_SRC_0_DST_0:
                buffer = octet << 16; // clear original content
                if (policy == BufferingPolicy.MINIMAL) {
                    encode(buffer >> 18);
                    state = STATE_SRC_1_DST_1;
                } else {
                    state = STATE_SRC_1_DST_0;
                }
                break;
            case STATE_SRC_1_DST_0:
                buffer |= octet << 8;
                if (policy == BufferingPolicy.MINIMAL) {
                    encode(buffer >> 18);
                    encode(buffer >> 12);
                    state = STATE_SRC_2_DST_2;
                } else {
                    state = STATE_SRC_2_DST_0;
                }
                break;
            case STATE_SRC_2_DST_0:
                buffer |= octet;
                if (policy == BufferingPolicy.GREEDY) {
                    state = STATE_SRC_3_DST_0;
                } else {
                    encode(buffer >> 18);
                    encode(buffer >> 12);
                    encode(buffer >> 6);
                    encode(buffer);
                    state = STATE_SRC_0_DST_0;
                }
                break;
            case STATE_SRC_1_DST_1:
                buffer |= octet << 8;
                if (policy == BufferingPolicy.MINIMAL) {
                    encode(buffer >> 12);
                    state = STATE_SRC_2_DST_2;
                } else {
                    state = STATE_SRC_2_DST_1;
                }
                break;
            case STATE_SRC_2_DST_1:
                buffer |= octet;
                if (policy == BufferingPolicy.GREEDY) {
                    state = STATE_SRC_3_DST_1;
                } else {
                    encode(buffer >> 12);
                    encode(buffer >> 6);
                    encode(buffer);
                    state = STATE_SRC_0_DST_0;
                }
                break;
            case STATE_SRC_2_DST_2:
                buffer |= octet;
                if (policy == BufferingPolicy.GREEDY) {
                    state = STATE_SRC_3_DST_2;
                } else {
                    encode(buffer >> 6);
                    encode(buffer);
                    state = STATE_SRC_0_DST_0;
                }
                break;
            default:
                throw new IOException();
        }
        if (octet == 0x00 && hasFlag(FLAG_STOP_ON_NULL) || octet == 0x0a && hasFlag(FLAG_STOP_ON_LINE_FEED)) { // line-feed, LF, 0x0a
            stop();
        }
    }

    @Override
    public void flush() throws IOException {
        if (hasFlag(FLAG_STOP_ON_FLUSH)) {
            stop();
        }
        if (hasFlag(FLAG_CONSUME_FLUSH)) {
            return;
        }
        out.flush();
    }

    @Override
    public void close() throws IOException {
        boolean needFlush = false;
        if (hasFlag(FLAG_STOP_ON_CLOSE)) {
            needFlush = stop();
        }
        if (hasFlag(FLAG_CONSUME_CLOSE)) {
            return;
        }
        if (needFlush) {
            out.flush();
        }
        out.close();
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    @Override
    public boolean stop() throws IOException {
        switch (state) {
            case STATE_SRC_0_DST_0:
                return false;
            case STATE_SRC_1_DST_0:
                encode(buffer >> 18);
                // no break here
            case STATE_SRC_1_DST_1:
                encode(buffer >> 12);
                out.write('=');
                out.write('=');
                break;
            case STATE_SRC_2_DST_0:
                encode(buffer >> 18);
                // no break here
            case STATE_SRC_2_DST_1:
                encode(buffer >> 12);
                // no break here
            case STATE_SRC_2_DST_2:
                encode(buffer >> 6);
                out.write('=');
                break;
            case STATE_SRC_3_DST_0:
                encode(buffer >> 18);
                // no break here
            case STATE_SRC_3_DST_1:
                encode(buffer >> 12);
                // no break here
            case STATE_SRC_3_DST_2:
                encode(buffer >> 6);
                encode(buffer);
                break;
            default:
                throw new IOException();
        }
        state = STATE_SRC_0_DST_0;
        return true;
    }
}
