package mujica.io.codec;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.IntDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created in omnidirectional on 2025/4/14, named Base64InputStream.
 * Recreated in UltraIO on 2025/4/21.
 * Moved here on 2025/4/26.
 */
@CodeHistory(date = "2025/4/14", project = "omnidirectional", name = "Base64InputStream")
@CodeHistory(date = "2025/4/21", project = "UltraIO")
@CodeHistory(date = "2025/4/26")
public class Base64DecodeInputStream extends FilterInputStream implements Base64StreamingCodec {

    private int buffer;

    private static final int STATE_SRC_0_DST_0 = 0;
    private static final int STATE_SRC_4_DST_0 = 1;
    private static final int STATE_SRC_2_DST_1 = 2;
    private static final int STATE_SRC_4_DST_1 = 3;
    private static final int STATE_SRC_3_DST_2 = 4;
    private static final int STATE_SRC_4_DST_2 = 5;
    private static final int STATE_SRC_2_DST_0_STOP = 6;
    private static final int STATE_SRC_3_DST_0_STOP = 7;
    private static final int STATE_SRC_3_DST_1_STOP = 8;

    private int state;

    @NotNull
    private BufferingPolicy policy;

    public Base64DecodeInputStream(@NotNull InputStream in, @NotNull BufferingPolicy policy) {
        super(in);
        this.policy = policy;
    }

    public Base64DecodeInputStream(@NotNull InputStream in) {
        this(in, BufferingPolicy.NORMAL);
    }

    private boolean readEOF() throws IOException {
        int value = in.read();
        if ('A' <= value && value <= 'Z') {
            value -= 'A';
        } else if ('a' <= value && value <= 'z') {
           value += 26 - 'a';
        } else if ('0' <= value && value <= '9') {
            value += 52 - '0';
        } else {
            switch (value) {
                case -1:
                    return false;
                case '-':
                case '+':
                    value = 62;
                    break;
                case '_':
                case '/':
                    value = 63;
                    break;
                default:
                    throw new BadCodeException(Quote.DEFAULT.apply((byte) value) + " not in [0-9A-Za-z\\-+_/]");
            }
        }
        buffer = value << 18;
        return true;
    }

    private void readVanilla() throws IOException {
        int value = in.read();
        if ('A' <= value && value <= 'Z') {
            value -= 'A';
        } else if ('a' <= value && value <= 'z') {
            value += 26 - 'a';
        } else if ('0' <= value && value <= '9') {
            value += 52 - '0';
        } else {
            switch (value) {
                case -1:
                    throw new EOFException();
                case '-':
                case '+':
                    value = 62;
                    break;
                case '_':
                case '/':
                    value = 63;
                    break;
                default:
                    throw new BadCodeException(Quote.DEFAULT.apply((byte) value) + " not in [0-9A-Za-z\\-+_/]");
            }
        }
        buffer |= value << 12;
    }

    private boolean readStop(int shift) throws IOException {
        int value = in.read();
        if ('A' <= value && value <= 'Z') {
            value -= 'A';
        } else if ('a' <= value && value <= 'z') {
           value += 26 - 'a';
        } else if ('0' <= value && value <= '9') {
            value += 52 - '0';
        } else {
            switch (value) {
                case -1:
                    throw new EOFException();
                case '=':
                    return false;
                case '-':
                case '+':
                    value = 62;
                    break;
                case '_':
                case '/':
                    value = 63;
                    break;
                default:
                    throw new BadCodeException(Quote.DEFAULT.apply((byte) value) + " not in [0-9A-Za-z\\-+_/=]");
            }
        }
        buffer |= value << shift;
        return true;
    }

    private void readStop() throws IOException {
        final int sign = in.read();
        if (sign != '=') {
            throw new BadCodeException(Quote.DEFAULT.apply((byte) sign) + " != '='");
        }
    }

    private int readGreedy(int pass) throws IOException {
        if (policy == BufferingPolicy.GREEDY && readEOF()) {
            readVanilla();
            if (readStop(6)) {
                if (readStop(0)) {
                    state = STATE_SRC_4_DST_0;
                } else {
                    state = STATE_SRC_3_DST_0_STOP;
                }
            } else {
                readStop();
                state = STATE_SRC_2_DST_0_STOP;
            }
        } else {
            state = STATE_SRC_0_DST_0;
        }
        return pass;
    }

    private int readStart() throws IOException {
        if (readEOF()) {
            readVanilla();
            if (policy == BufferingPolicy.MINIMAL) {
                state = STATE_SRC_2_DST_1;
                return 0xff & (buffer >> 16);
            } else if (readStop(6)) {
                if (readStop(0)) {
                    state = STATE_SRC_4_DST_1;
                } else {
                    state = STATE_SRC_3_DST_1_STOP;
                }
                return 0xff & (buffer >> 16);
            } else {
                readStop();
                return readGreedy(0xff & (buffer >> 16));
            }
        } else {
            return -1;
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
        switch (state) {
            case STATE_SRC_0_DST_0:
                readGreedy(-1);
                break;
            case STATE_SRC_2_DST_1:
                if (policy != BufferingPolicy.MINIMAL) {
                    if (readStop(6)) {
                        if (readStop(0)) {
                            state = STATE_SRC_4_DST_1;
                        } else {
                            state = STATE_SRC_3_DST_1_STOP;
                        }
                    } else {
                        readStop();
                        readGreedy(-1);
                    }
                }
                break;
            case STATE_SRC_3_DST_2:
                if (policy != BufferingPolicy.MINIMAL) {
                    if (readStop(0)) {
                        state = STATE_SRC_4_DST_2;
                    } else {
                        readGreedy(-1);
                    }
                }
                break;
        }
    }

    @Override
    public int read() throws IOException {
        switch (state) {
            case STATE_SRC_0_DST_0:
                return readStart();
            case STATE_SRC_4_DST_0:
                state = STATE_SRC_4_DST_1;
                return 0xff & (buffer >> 16);
            case STATE_SRC_2_DST_1:
                if (readStop(6)) {
                    if (policy == BufferingPolicy.MINIMAL) {
                        state = STATE_SRC_3_DST_2;
                        return 0xff & (buffer >> 8);
                    } else if (readStop(0)) {
                        state = STATE_SRC_4_DST_2;
                        return 0xff & (buffer >> 8);
                    } else {
                        return readGreedy(0xff & (buffer >> 8));
                    }
                } else {
                    readStop();
                    return readStart();
                }
            case STATE_SRC_4_DST_1:
                state = STATE_SRC_4_DST_2;
                return 0xff & (buffer >> 8);
            case STATE_SRC_3_DST_2:
                if (readStop(0)) {
                    return readGreedy(0xff & buffer);
                } else {
                    return readStart();
                }
            case STATE_SRC_4_DST_2:
                return readGreedy(0xff & buffer);
            case STATE_SRC_2_DST_0_STOP:
                return readGreedy(0xff & (buffer >> 16));
            case STATE_SRC_3_DST_0_STOP:
                state = STATE_SRC_3_DST_1_STOP;
                return 0xff & (buffer >> 16);
            case STATE_SRC_3_DST_1_STOP:
                return readGreedy(0xff & (buffer >> 8));
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
                if (count == 0) {
                    count = -1;
                }
                break;
            }
            array[offset] = (byte) value;
            count++;
        }
        return count;
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

    @Override
    public int available() throws IOException {
        final int n = in.available() >> 2;
        switch (state) {
            default:
                return n;
            case STATE_SRC_4_DST_0:
                return n + 3;
            case STATE_SRC_4_DST_1:
            case STATE_SRC_3_DST_0_STOP:
                return n + 2;
            case STATE_SRC_4_DST_2:
            case STATE_SRC_2_DST_0_STOP:
            case STATE_SRC_3_DST_1_STOP:
                return n + 1;
        }
    }
}
