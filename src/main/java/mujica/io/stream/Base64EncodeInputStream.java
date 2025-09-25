package mujica.io.stream;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.IntDataView;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created in UltraIO on 2025/4/22.
 * Moved here on 2025/4/26.
 */
@CodeHistory(date = "2025/4/22", project = "UltraIO")
@CodeHistory(date = "2025/4/26")
public class Base64EncodeInputStream extends FilterInputStream implements Base64StreamingEncoder {

    private int buffer;

    private static final int STATE_SRC_0_DST_0 = 0;
    private static final int STATE_SRC_3_DST_0 = 1;
    private static final int STATE_SRC_1_DST_1 = 2;
    private static final int STATE_SRC_3_DST_1 = 3;
    private static final int STATE_SRC_2_DST_2 = 4;
    private static final int STATE_SRC_3_DST_2 = 5;
    private static final int STATE_SRC_3_DST_3 = 6;
    private static final int STATE_SRC_1_DST_0_STOP = 7;
    private static final int STATE_SRC_1_DST_1_STOP = 8;
    private static final int STATE_SRC_1_DST_2_STOP = 9;
    private static final int STATE_SRC_1_DST_3_STOP = 10;
    private static final int STATE_SRC_2_DST_0_STOP = 11;
    private static final int STATE_SRC_2_DST_1_STOP = 12;
    private static final int STATE_SRC_2_DST_2_STOP = 13;
    private static final int STATE_SRC_2_DST_3_STOP = 14;

    private int state;

    private int flags; // remove it

    @NotNull
    private BufferingPolicy policy;

    public Base64EncodeInputStream(@NotNull InputStream in, @NotNull BufferingPolicy policy, int flags) {
        super(in);
        this.policy = policy;
        setFlags(flags);
    }

    public Base64EncodeInputStream(@NotNull InputStream in, @NotNull BufferingPolicy policy) {
        this(in, policy, FLAG_STOP_ON_FLUSH);
    }

    public Base64EncodeInputStream(@NotNull InputStream in, int flags) {
        this(in, BufferingPolicy.NORMAL, flags);
    }

    public Base64EncodeInputStream(@NotNull InputStream in) {
        this(in, BufferingPolicy.NORMAL, FLAG_STOP_ON_FLUSH);
    }

    private boolean read(int shift) throws IOException {
        final int value = in.read(); // from -1 to 255
        if (value != -1) { // from 0 to 255
            buffer |= value << shift;
            return true;
        } else {
            return false;
        }
    }

    private int readGreedy(int pass) throws IOException {
        buffer = 0;
        if (policy == BufferingPolicy.GREEDY && read(16)) {
            if (read(8)) {
                if (read(0)) {
                    state = STATE_SRC_3_DST_0;
                } else {
                    state = STATE_SRC_2_DST_0_STOP;
                }
            } else {
                state = STATE_SRC_1_DST_0_STOP;
            }
        } else {
            state = STATE_SRC_0_DST_0;
        }
        return pass;
    }

    private int encode(int value) {
        value &= 0x3f;
        if (value < 26) {
            return 'A' + value;
        } else if (value < 52) {
            return 'a' - 26 + value;
        } else if (value < 62) {
            return '0' - 52 + value;
        } else if (value == 62) {
            return 0xff & (flags >> 16);
        } else { // value == 63
            return 0xff & (flags >> 24);
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
        if (policy == BufferingPolicy.MINIMAL) {
            return;
        }
        switch (state) {
            case STATE_SRC_1_DST_1:
                if (read(8)) {
                    if (read(0)) {
                        state = STATE_SRC_3_DST_1;
                    } else {
                        state = STATE_SRC_2_DST_1_STOP;
                    }
                } else {
                    state = STATE_SRC_1_DST_1_STOP;
                }
                break;
            case STATE_SRC_2_DST_2:
                if (read(0)) {
                    state = STATE_SRC_3_DST_2;
                } else {
                    state = STATE_SRC_2_DST_2_STOP;
                }
                break;
        }
        if (policy == BufferingPolicy.NORMAL) {
            return;
        }
        if (state == STATE_SRC_0_DST_0 && read(16)) {
            if (read(8)) {
                if (read(0)) {
                    state = STATE_SRC_3_DST_0;
                } else {
                    state = STATE_SRC_2_DST_0_STOP;
                }
            } else {
                state = STATE_SRC_1_DST_0_STOP;
            }
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
        this.flags = newFlags;
    }

    @Override
    public boolean hasFlag(int testFlag) {
        return (flags & testFlag) != 0;
    }

    @Override
    public int read() throws IOException {
        switch (state) {
            case STATE_SRC_0_DST_0:
                if (read(16)) {
                    if (policy == BufferingPolicy.MINIMAL) {
                        state = STATE_SRC_1_DST_1;
                    } else if (read(8)) {
                        if (read(0)) {
                            state = STATE_SRC_3_DST_1;
                        } else {
                            state = STATE_SRC_2_DST_1_STOP;
                        }
                    } else {
                        state = STATE_SRC_1_DST_1_STOP;
                    }
                    return encode(buffer >> 18);
                } else {
                    return -1;
                }
            case STATE_SRC_3_DST_0:
                state = STATE_SRC_3_DST_1;
                return encode(buffer >> 18);
            case STATE_SRC_1_DST_1:
                if (read(8)) {
                    if (policy == BufferingPolicy.MINIMAL) {
                        state = STATE_SRC_2_DST_2;
                    } else if (read(0)) {
                        state = STATE_SRC_3_DST_2;
                    } else {
                        state = STATE_SRC_2_DST_2_STOP;
                    }
                } else {
                    state = STATE_SRC_1_DST_2_STOP;
                }
                return encode(buffer >> 12);
            case STATE_SRC_3_DST_1:
                state = STATE_SRC_3_DST_2;
                return encode(buffer >> 12);
            case STATE_SRC_2_DST_2:
                if (read(0)) {
                    state = STATE_SRC_3_DST_3;
                } else {
                    state = STATE_SRC_2_DST_3_STOP;
                }
                return encode(buffer >> 6);
            case STATE_SRC_3_DST_2:
                state = STATE_SRC_3_DST_3;
                return encode(buffer >> 6);
            case STATE_SRC_3_DST_3:
                return readGreedy(encode(buffer));
            case STATE_SRC_1_DST_0_STOP:
            case STATE_SRC_2_DST_0_STOP:
                state++;
                return encode(buffer >> 18);
            case STATE_SRC_1_DST_1_STOP:
            case STATE_SRC_2_DST_1_STOP:
                state++;
                return encode(buffer >> 12);
            case STATE_SRC_1_DST_2_STOP:
                state = STATE_SRC_1_DST_3_STOP;
                return '=';
            case STATE_SRC_2_DST_2_STOP:
                state = STATE_SRC_2_DST_3_STOP;
                return encode(buffer >> 6);
            case STATE_SRC_1_DST_3_STOP:
            case STATE_SRC_2_DST_3_STOP:
                return readGreedy('=');
            default:
                throw new IOException();
        }
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        int count = 0;
        for (int limit = Math.addExact(offset, length); offset < limit; offset++) {
            int value = read();
            if (value == -1) {
                break;
            }
            array[offset] = (byte) value;
            count++;
        }
        return count;
    }

    @Override
    public boolean stop() {
        final int oldState = state;
        switch (oldState) {
            case STATE_SRC_1_DST_1:
                state = STATE_SRC_1_DST_1_STOP;
                break;
            case STATE_SRC_2_DST_2:
                state = STATE_SRC_2_DST_2_STOP;
                break;
        }
        return oldState != state;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public long skip(long n) throws IOException {
        long m = 0L;
        while (n > 0L) {
            read();
            n--;
            m++;
        }
        return m;
    }
}
