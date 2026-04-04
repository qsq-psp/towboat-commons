package mujica.io.codec;

import mujica.io.function.IOByteConsumer;
import mujica.io.function.IOIntSupplier;
import mujica.reflect.function.ByteConsumer;
import mujica.reflect.function.CharSupplier;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@CodeHistory(date = "2026/3/26")
public class UTF8PushPullEncoder extends PushPullEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UTF8PushPullEncoder.class);

    private int buffer;

    public UTF8PushPullEncoder() {
        super();
    }

    @Override
    public void reset() {
        buffer = 0;
    }

    private void utf8b2(int ch) {
        buffer = 0b110_00000_10_000000 | (0b000_11111_00000000 & (ch << 2)) | (0b00_111111 & ch);
    }

    private void utf8b3(int ch) {
        buffer = 0b1110_0000_10_000000_10_000000 | (0b0000_1111_00000000_00000000 & (ch << 4))
                | (0b00_111111_00000000 & (ch << 2)) | (0b00_111111 & ch);
    }

    private void utf8b4(int cp) {
        buffer = 0b11110_000_10_000000_10_000000_10_000000 | (0b00000_111_00000000_00000000_00000000 & (cp << 6))
                | (0b00_111111_00000000_00000000 & (cp << 4)) | (0b00_111111_00000000 & (cp << 2)) | (0b00_111111 & cp);
    }

    @Override
    public void push(char in, @NotNull ByteConsumer out) {
        if (buffer != 0) {
            if ((buffer & 0xffffff00) != 0) {
                if ((buffer & 0xffff0000) != 0) {
                    if ((buffer & 0xff000000) != 0) {
                        if (Character.isLowSurrogate(in)) {
                            utf8b4(Character.toCodePoint((char) (buffer >> 16), in));
                            for (int shift = 24; shift >= 0; shift -= 8) {
                                out.accept((byte) (buffer >>> shift));
                            }
                        } else {
                            LOGGER.warn("push 1st {}", in);
                        }
                        buffer = 0;
                        return;
                    }
                    out.accept((byte) (buffer >> 16));
                }
                out.accept((byte) (buffer >> 8));
            }
            out.accept((byte) buffer);
            buffer = 0;
        }
        if (in < 0x80) {
            out.accept((byte) in);
        } else if (in < 0x800) {
            out.accept((byte) (0b110_00000 | (in >> 6)));
            out.accept((byte) (0b10_000000 | (in & 0b00_111111)));
        } else if (!Character.isSurrogate(in)) {
            out.accept((byte) (0b1110_0000 | (in >> 12)));
            out.accept((byte) (0b10_000000 | ((in >> 6) & 0b00_111111)));
            out.accept((byte) (0b10_000000 | (in & 0b00_111111)));
        } else if (Character.isHighSurrogate(in)) {
            buffer = in << 16;
        } else {
            LOGGER.warn("push 2nd {}", in);
        }
    }

    @Override
    public void push(char in, @NotNull IOByteConsumer out) throws IOException {
        if (buffer != 0) {
            if ((buffer & 0xffffff00) != 0) {
                if ((buffer & 0xffff0000) != 0) {
                    if ((buffer & 0xff000000) != 0) {
                        if (Character.isLowSurrogate(in)) {
                            utf8b4(Character.toCodePoint((char) (buffer >> 16), in));
                            for (int shift = 24; shift >= 0; shift -= 8) {
                                out.accept((byte) (buffer >>> shift));
                            }
                        } else {
                            LOGGER.warn("push 1st {}", in);
                        }
                        buffer = 0;
                        return;
                    }
                    out.accept((byte) (buffer >> 16));
                }
                out.accept((byte) (buffer >> 8));
            }
            out.accept((byte) buffer);
            buffer = 0;
        }
        if (in < 0x80) {
            out.accept((byte) in);
        } else if (in < 0x800) {
            out.accept((byte) (0b110_00000 | (in >> 6)));
            out.accept((byte) (0b10_000000 | (in & 0b00_111111)));
        } else if (!Character.isSurrogate(in)) {
            out.accept((byte) (0b1110_0000 | (in >> 12)));
            out.accept((byte) (0b10_000000 | ((in >> 6) & 0b00_111111)));
            out.accept((byte) (0b10_000000 | (in & 0b00_111111)));
        } else if (Character.isHighSurrogate(in)) {
            buffer = in << 16;
        } else {
            LOGGER.warn("push 2nd {}", in);
        }
    }

    @Override
    public byte pull(@NotNull CharSupplier in) {
        int value = -1;
        do {
            if (buffer != 0) {
                if ((buffer & 0xffffff00) != 0) {
                    if ((buffer & 0xffff0000) != 0) {
                        if ((buffer & 0xff000000) != 0) {
                            char ch = in.getAsChar();
                            if (Character.isLowSurrogate(ch)) {
                                utf8b4(Character.toCodePoint((char) (buffer >> 16), ch));
                                value = buffer >>> 24;
                                buffer &= 0x00ffffff;
                            } else {
                                LOGGER.warn("pull 1st {}", in);
                                buffer = 0;
                            }
                        } else {
                            value = buffer >>> 16;
                            buffer &= 0x0000ffff;
                        }
                    } else {
                        value = buffer >>> 8;
                        buffer &= 0x000000ff;
                    }
                } else {
                    value = buffer;
                    buffer = 0;
                }
            } else {
                char ch = in.getAsChar();
                if (ch < 0x80) {
                    value = ch;
                } else if (ch < 0x800) {
                    utf8b2(ch);
                } else if (!Character.isSurrogate(ch)) {
                    utf8b3(ch);
                } else if (Character.isHighSurrogate(ch)) {
                    buffer = ch << 16;
                } else {
                    LOGGER.warn("pull 2nd {}", in);
                }
            }
        } while (value == -1);
        return (byte) value;
    }

    @Override
    public byte pull(@NotNull IOIntSupplier in) throws IOException {
        int value = -1;
        do {
            if (buffer != 0) {
                if ((buffer & 0xffffff00) != 0) {
                    if ((buffer & 0xffff0000) != 0) {
                        if ((buffer & 0xff000000) != 0) {
                            int ch = in.getAsInt();
                            if (Character.isLowSurrogate((char) ch)) {
                                utf8b4(Character.toCodePoint((char) (buffer >> 16), (char) ch));
                                value = buffer >>> 24;
                                buffer &= 0x00ffffff;
                            } else {
                                LOGGER.warn("pull 1st {}", in);
                                buffer = 0;
                            }
                        } else {
                            value = buffer >>> 16;
                            buffer &= 0x0000ffff;
                        }
                    } else {
                        value = buffer >>> 8;
                        buffer &= 0x000000ff;
                    }
                } else {
                    value = buffer;
                    buffer = 0;
                }
            } else {
                int ch = in.getAsInt();
                if (ch < 0x80) {
                    value = ch;
                } else if (ch < 0x800) {
                    utf8b2(ch);
                } else if (!Character.isSurrogate((char) ch)) {
                    utf8b3(ch);
                } else if (Character.isHighSurrogate((char) ch)) {
                    buffer = ch << 16;
                } else {
                    LOGGER.warn("pull 2nd {}", in);
                }
            }
        } while (value == -1);
        return (byte) value;
    }

    @Override
    public void finishPush(@NotNull ByteConsumer out) {
        if (buffer != 0) {
            if ((buffer & 0xffffff00) != 0) {
                if ((buffer & 0xffff0000) != 0) {
                    if ((buffer & 0xff000000) != 0) {
                        buffer = 0;
                        return;
                    }
                    out.accept((byte) (buffer >> 16));
                }
                out.accept((byte) (buffer >> 8));
            }
            out.accept((byte) buffer);
            buffer = 0;
        }
    }

    @Override
    public boolean finishPullHasNext() {
        return (buffer & 0x0000ffff) != 0;
    }

    @Override
    public byte finishPullAsByte() {
        return (byte) finishPullAsInt();
    }

    @Override
    public int finishPullAsInt() {
        int value;
        if (buffer != 0) {
            if ((buffer & 0xffffff00) != 0) {
                if ((buffer & 0xffff0000) != 0) {
                    if ((buffer & 0xff000000) != 0) {
                        throw new IllegalStateException();
                    } else {
                        value = buffer >>> 16;
                        buffer &= 0x0000ffff;
                    }
                } else {
                    value = buffer >>> 8;
                    buffer &= 0x000000ff;
                }
            } else {
                value = buffer;
                buffer = 0;
            }
        } else {
            throw new IllegalStateException();
        }
        assert (value & 0xff) == value;
        return value;
    }
}
