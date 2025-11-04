package mujica.io.codec;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.IntDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@CodeHistory(date = "2025/4/20", project = "UltraIO")
@CodeHistory(date = "2025/4/26")
public class Base64DecodeOutputStream extends FilterOutputStream implements Base64StreamingCodec {

    private int buffer;

    private static final int STATE_SRC_0_DST_0 = 0;
    private static final int STATE_SRC_1_DST_0 = 1;
    private static final int STATE_SRC_2_DST_0 = 2;
    private static final int STATE_SRC_3_DST_0 = 3;
    private static final int STATE_SRC_4_DST_0 = 4;
    private static final int STATE_SRC_2_DST_1 = 5;
    private static final int STATE_SRC_3_DST_1 = 6;
    private static final int STATE_SRC_4_DST_1 = 7;
    private static final int STATE_SRC_3_DST_2 = 8;
    private static final int STATE_SRC_4_DST_2 = 9;
    private static final int STATE_CONSUME_3_SRC_2_DST_0 = 10;
    private static final int STATE_CONSUME_4_SRC_2_DST_0 = 11;
    private static final int STATE_CONSUME_4_SRC_3_DST_0 = 12;
    private static final int STATE_CONSUME_3_SRC_2_DST_1 = 13;
    private static final int STATE_CONSUME_4_SRC_3_DST_1 = 14;

    private int state;

    @NotNull
    private BufferingPolicy policy;

    public Base64DecodeOutputStream(@NotNull OutputStream out, @NotNull BufferingPolicy policy) {
        super(out);
        this.policy = policy;
    }

    public Base64DecodeOutputStream(@NotNull OutputStream out) {
        this(out, BufferingPolicy.NORMAL);
    }

    private int decode(int value) throws IOException {
        if ('A' <= value && value <= 'Z') {
            return value - 'A';
        } else if ('a' <= value && value <= 'z') {
            return value + (26 - 'a');
        } else if ('0' <= value && value <= '9') {
            return value + (52 - '0');
        }
        switch (value) {
            case '-':
            case '+':
                return 62;
            case '_':
            case '/':
                return 63;
            default:
                throw new BadCodeException(Quote.DEFAULT.apply((byte) value) + " not in [0-9A-Za-z\\-+_/]");
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
    @SuppressWarnings("DuplicateBranchesInSwitch")
    public void setPolicyImmediately(@NotNull BufferingPolicy policy) throws IOException {
        this.policy = policy;
        if (policy == BufferingPolicy.GREEDY) {
            return;
        }
        switch (state) {
            case STATE_CONSUME_4_SRC_2_DST_0:
                out.write(buffer >> 16);
                state = STATE_SRC_0_DST_0;
                return; // not break here
            case STATE_CONSUME_4_SRC_3_DST_0:
                out.write(buffer >> 16);
                // no break here
            case STATE_CONSUME_4_SRC_3_DST_1:
                out.write(buffer >> 8);
                state = STATE_SRC_0_DST_0;
                return; // not break here
            case STATE_SRC_4_DST_0:
                out.write(buffer >> 16);
                // no break here
            case STATE_SRC_4_DST_1:
                out.write(buffer >> 8);
                // no break here
            case STATE_SRC_4_DST_2:
                out.write(buffer);
                state = STATE_SRC_0_DST_0;
                // no break here
            case STATE_SRC_0_DST_0:
                return; // not break here
        }
        if (policy == BufferingPolicy.NORMAL) {
            return;
        }
        switch (state) {
            case STATE_CONSUME_3_SRC_2_DST_0:
                out.write(buffer >> 16);
                state = STATE_CONSUME_3_SRC_2_DST_1;
                break;
            case STATE_SRC_2_DST_0:
                out.write(buffer >> 16);
                state = STATE_SRC_2_DST_1;
                break;
            case STATE_SRC_3_DST_0:
                out.write(buffer >> 16);
                // no break here
            case STATE_SRC_3_DST_1:
                out.write(buffer >> 8);
                state = STATE_SRC_3_DST_2;
                break;
        }
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    @Override
    public void write(int octet) throws IOException {
        octet &= 0xff;
        if (octet == '=') {
            switch (state) {
                case STATE_SRC_2_DST_0:
                    state = STATE_CONSUME_3_SRC_2_DST_0;
                    break;
                case STATE_SRC_3_DST_0:
                    state = STATE_CONSUME_4_SRC_3_DST_0;
                    break;
                case STATE_SRC_2_DST_1:
                    state = STATE_CONSUME_3_SRC_2_DST_1;
                    break;
                case STATE_SRC_3_DST_1:
                    state = STATE_CONSUME_4_SRC_3_DST_1;
                    break;
                case STATE_CONSUME_3_SRC_2_DST_0:
                    state = STATE_CONSUME_4_SRC_2_DST_0;
                    break;
                case STATE_SRC_3_DST_2:
                    state = STATE_SRC_0_DST_0;
                    break;
                case STATE_CONSUME_3_SRC_2_DST_1:
                    state = STATE_SRC_0_DST_0;
                    break;
                default:
                    throw new IOException();
            }
        } else {
            switch (state) {
                case STATE_CONSUME_4_SRC_2_DST_0:
                    out.write(buffer >> 16);
                    buffer = decode(octet) << 18; // clear original content
                    state = STATE_SRC_1_DST_0;
                    break;
                case STATE_CONSUME_4_SRC_3_DST_0:
                    out.write(buffer >> 16);
                    // no break here
                case STATE_CONSUME_4_SRC_3_DST_1:
                    out.write(buffer >> 8);
                    buffer = decode(octet) << 18; // clear original content
                    state = STATE_SRC_1_DST_0;
                    break;
                case STATE_SRC_4_DST_0:
                    out.write(buffer >> 16);
                    // no break here
                case STATE_SRC_4_DST_1:
                    out.write(buffer >> 8);
                    // no break here
                case STATE_SRC_4_DST_2:
                    out.write(buffer);
                    // no break here
                case STATE_SRC_0_DST_0:
                    buffer = decode(octet) << 18; // clear original content
                    state = STATE_SRC_1_DST_0;
                    break;
                case STATE_SRC_1_DST_0:
                    buffer |= decode(octet) << 12;
                    if (policy == BufferingPolicy.MINIMAL) {
                        out.write(buffer >> 16);
                        state = STATE_SRC_2_DST_1;
                    } else {
                        state = STATE_SRC_2_DST_0;
                    }
                    break;
                case STATE_SRC_2_DST_0:
                    buffer |= decode(octet) << 6;
                    if (policy == BufferingPolicy.MINIMAL) {
                        out.write(buffer >> 16);
                        out.write(buffer >> 8);
                        state = STATE_SRC_3_DST_2;
                    } else {
                        state = STATE_SRC_3_DST_0;
                    }
                    break;
                case STATE_SRC_3_DST_0:
                    buffer |= decode(octet);
                    if (policy == BufferingPolicy.GREEDY) {
                        state = STATE_SRC_4_DST_0;
                    } else {
                        out.write(buffer >> 16);
                        out.write(buffer >> 8);
                        out.write(buffer);
                        state = STATE_SRC_0_DST_0;
                    }
                    break;
                case STATE_SRC_2_DST_1:
                    buffer |= decode(octet) << 6;
                    if (policy == BufferingPolicy.MINIMAL) {
                        out.write(buffer >> 8);
                        state = STATE_SRC_3_DST_2;
                    } else {
                        state = STATE_SRC_3_DST_1;
                    }
                    break;
                case STATE_SRC_3_DST_1:
                    buffer |= decode(octet);
                    if (policy == BufferingPolicy.GREEDY) {
                        state = STATE_SRC_4_DST_1;
                    } else {
                        out.write(buffer >> 8);
                        out.write(buffer);
                        state = STATE_SRC_0_DST_0;
                    }
                    break;
                case STATE_SRC_3_DST_2:
                    buffer |= decode(octet);
                    if (policy == BufferingPolicy.GREEDY) {
                        state = STATE_SRC_4_DST_2;
                    } else {
                        out.write(buffer);
                        state = STATE_SRC_0_DST_0;
                    }
                    break;
                default:
                    throw new IOException();
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

    @SuppressWarnings("DuplicateBranchesInSwitch")
    private boolean stop() throws IOException {
        switch (state) {
            case STATE_CONSUME_4_SRC_2_DST_0:
                out.write(buffer >> 16);
                break;
            case STATE_CONSUME_4_SRC_3_DST_0:
                out.write(buffer >> 16);
                // no break here
            case STATE_CONSUME_4_SRC_3_DST_1:
                out.write(buffer >> 8);
                break;
            case STATE_SRC_4_DST_0:
                out.write(buffer >> 16);
                // no break here
            case STATE_SRC_4_DST_1:
                out.write(buffer >> 8);
                // no break here
            case STATE_SRC_4_DST_2:
                out.write(buffer);
                break;
            case STATE_SRC_0_DST_0:
                return false;
            default:
                throw new EOFException("state = " + state);
        }
        return true;
    }
}
