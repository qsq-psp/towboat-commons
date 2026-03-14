package mujica.io.codec;

import mujica.io.function.IOIntConsumer;
import mujica.io.function.IOIntSupplier;
import mujica.reflect.function.ByteSupplier;
import mujica.reflect.function.CharConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@CodeHistory(date = "2023/4/30", project = "Ultramarine", name = "Utf8Utility")
@CodeHistory(date = "2026/2/26")
public class UTF8PushPullDecoder extends PushPullDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UTF8PushPullDecoder.class);

    private int buffer;

    public UTF8PushPullDecoder() {
        super();
    }

    @Override
    public void reset() {
        buffer = 0;
    }

    private int codePoint2() {
        return ((buffer & 0x00001f00) >> 2) | (buffer & 0x0000003f);
    }

    private int codePoint3() {
        return ((buffer & 0x000f0000) >> 4) | ((buffer & 0x00003f00) >> 2) | (buffer & 0x0000003f);
    }

    private int codePoint4() {
        return ((buffer & 0x07000000) >> 6) | ((buffer & 0x003f0000) >> 4) | ((buffer & 0x00003f00) >> 2) | (buffer & 0x0000003f);
    }

    @Override
    public void push(byte in, @NotNull CharConsumer out) {
        int shift;
        if (buffer == 0) {
            shift = 24;
        } else if ((buffer & 0xff000000) != 0) {
            if ((buffer & 0x00ff0000) == 0) {
                shift = 16;
            } else if ((buffer & 0x0000ff00) == 0) {
                shift = 8;
            } else if ((buffer & 0x000000ff) == 0) {
                shift = 0;
            } else {
                out.accept(Character.lowSurrogate(codePoint4()));
                buffer = 0;
                shift = 24;
            }
        } else if ((buffer & 0x00ff0000) != 0) {
            if ((buffer & 0x0000ff00) == 0) {
                shift = 8;
            } else {
                shift = 0;
            }
        } else {
            assert (buffer & 0x0000ff00) != 0;
            assert (buffer & 0x000000ff) == 0;
            shift = 0;
        }
        if (shift == 24) {
            if (in >= 0) {
                out.accept((char) in);
            } else if ((in & 0xe0) == 0xc0) {
                buffer = (0xff & in) << 8;
            } else if ((in & 0xf0) == 0xe0) {
                buffer = (0xff & in) << 16;
            } else if ((in & 0xf8) == 0xf0) {
                buffer = (0xff & in) << 24;
            } else {
                LOGGER.warn("push 1st {}", in);
                out.accept(REPLACEMENT);
            }
        } else {
            if ((in & 0xc0) == 0x80) {
                buffer |= (0xff & in) << shift;
            } else {
                LOGGER.warn("push 2nd {} {}", shift, in);
                out.accept(REPLACEMENT);
                buffer = 0;
                shift = 32;
            }
        }
        if (shift == 0) {
            if ((buffer & 0xff000000) != 0) {
                int codePoint = codePoint4();
                out.accept(Character.highSurrogate(codePoint));
                out.accept(Character.lowSurrogate(codePoint));
            } else if ((buffer & 0x00ff0000) != 0) {
                out.accept((char) codePoint3());
            } else {
                assert (buffer & 0x0000ff00) != 0;
                out.accept((char) codePoint2());
            }
            buffer = 0;
        }
    }

    @Override
    public void push(int in, @NotNull IOIntConsumer out) throws IOException {
        int shift;
        if (buffer == 0) {
            shift = 24;
        } else if ((buffer & 0xff000000) != 0) {
            if ((buffer & 0x00ff0000) == 0) {
                shift = 16;
            } else if ((buffer & 0x0000ff00) == 0) {
                shift = 8;
            } else if ((buffer & 0x000000ff) == 0) {
                shift = 0;
            } else {
                out.accept(Character.lowSurrogate(codePoint4()));
                buffer = 0;
                shift = 24;
            }
        } else if ((buffer & 0x00ff0000) != 0) {
            if ((buffer & 0x0000ff00) == 0) {
                shift = 8;
            } else {
                shift = 0;
            }
        } else {
            assert (buffer & 0x0000ff00) != 0;
            assert (buffer & 0x000000ff) == 0;
            shift = 0;
        }
        if (shift == 24) {
            if ((in & 0x80) == 0x00) {
                out.accept((char) in);
            } else if ((in & 0xe0) == 0xc0) {
                buffer = (0xff & in) << 8;
            } else if ((in & 0xf0) == 0xe0) {
                buffer = (0xff & in) << 16;
            } else if ((in & 0xf8) == 0xf0) {
                buffer = (0xff & in) << 24;
            } else {
                LOGGER.warn("push 1st {}", in);
                out.accept(REPLACEMENT);
            }
        } else {
            if ((in & 0xc0) == 0x80) {
                buffer |= (0xff & in) << shift;
            } else {
                LOGGER.warn("push 2nd {} {}", shift, in);
                out.accept(REPLACEMENT);
                buffer = 0;
                shift = 32;
            }
        }
        if (shift == 0) {
            if ((buffer & 0xff000000) != 0) {
                int codePoint = codePoint4();
                out.accept(Character.highSurrogate(codePoint));
                out.accept(Character.lowSurrogate(codePoint));
            } else if ((buffer & 0x00ff0000) != 0) {
                out.accept((char) codePoint3());
            } else {
                assert (buffer & 0x0000ff00) != 0;
                out.accept((char) codePoint2());
            }
            buffer = 0;
        }
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    @Override
    public char pull(@NotNull ByteSupplier in) {
        int value;
        while (true) {
            if (buffer == 0) {
                value = in.getAsByte();
                if ((value & 0x80) == 0x00) {
                    break;
                } else if ((value & 0xe0) == 0xc0) {
                    buffer = (0xff & value) << 8;
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        value = codePoint2();
                    } else {
                        LOGGER.warn("pull 1st {}", value);
                        value = REPLACEMENT;
                    }
                } else if ((value & 0xf0) == 0xe0) {
                    buffer = (0xff & value) << 16;
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        LOGGER.warn("pull 2nd {}", value);
                        value = REPLACEMENT;
                        break;
                    }
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        value = codePoint3();
                    } else {
                        LOGGER.warn("pull 3rd {}", value);
                        value = REPLACEMENT;
                    }
                } else if ((value & 0xf8) == 0xf0) {
                    buffer = (0xff & value) << 24;
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 16;
                    } else {
                        LOGGER.warn("pull 4th {}", value);
                        value = REPLACEMENT;
                        break;
                    }
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        LOGGER.warn("pull 5th {}", value);
                        value = REPLACEMENT;
                        break;
                    }
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        return Character.highSurrogate(codePoint4());
                    } else {
                        LOGGER.warn("pull 6th {}", value);
                        value = REPLACEMENT;
                    }
                } else {
                    LOGGER.warn("pull 7th {}", value);
                    value = REPLACEMENT;
                }
            } else if ((buffer & 0xff000000) != 0) {
                if ((buffer & 0x00ff0000) == 0) {
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 16;
                    } else {
                        LOGGER.warn("pull 8th {}", value);
                        value = REPLACEMENT;
                        break;
                    }
                }
                if ((buffer & 0x0000ff00) == 0) {
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        LOGGER.warn("pull 9th {}", value);
                        value = REPLACEMENT;
                        break;
                    }
                }
                if ((buffer & 0x000000ff) == 0) {
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        return Character.highSurrogate(codePoint4());
                    } else {
                        LOGGER.warn("pull 10th {}", value);
                        value = REPLACEMENT;
                    }
                } else {
                    value = Character.lowSurrogate(codePoint4());
                }
            } else if ((buffer & 0x00ff0000) != 0) {
                if ((buffer & 0x0000ff00) == 0) {
                    if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        LOGGER.warn("pull 11th {}", value);
                        value = REPLACEMENT;
                        break;
                    }
                }
                if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                    buffer |= 0xff & value;
                    value = codePoint3();
                } else {
                    LOGGER.warn("pull 12th {}", value);
                    value = REPLACEMENT;
                }
            } else {
                assert (buffer & 0x0000ff00) != 0;
                assert (buffer & 0x000000ff) == 0;
                if (((value = in.getAsByte()) & 0xc0) == 0x80) {
                    buffer |= 0xff & value;
                    value = codePoint2();
                } else {
                    LOGGER.warn("pull 13th {}", value);
                    value = REPLACEMENT;
                }
            }
            break;
        }
        buffer = 0;
        return (char) value;
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    @Override
    public int pull(@NotNull IOIntSupplier in) throws IOException {
        int value;
        while (true) {
            if (buffer == 0) {
                value = in.getAsInt();
                if ((value & 0x80) == 0x00) {
                    break;
                } else if ((value & 0xe0) == 0xc0) {
                    buffer = (0xff & value) << 8;
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        value = codePoint2();
                    } else {
                        value = REPLACEMENT;
                    }
                } else if ((value & 0xf0) == 0xe0) {
                    buffer = (0xff & value) << 16;
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        value = REPLACEMENT;
                        break;
                    }
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        value = codePoint3();
                    } else {
                        value = REPLACEMENT;
                    }
                } else if ((value & 0xf8) == 0xf0) {
                    buffer = (0xff & value) << 24;
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 16;
                    } else {
                        value = REPLACEMENT;
                        break;
                    }
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        value = REPLACEMENT;
                        break;
                    }
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        return Character.highSurrogate(codePoint4());
                    } else {
                        value = REPLACEMENT;
                    }
                } else {
                    value = REPLACEMENT;
                }
            } else if ((buffer & 0xff000000) != 0) {
                if ((buffer & 0x00ff0000) == 0) {
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 16;
                    } else {
                        value = REPLACEMENT;
                        break;
                    }
                }
                if ((buffer & 0x0000ff00) == 0) {
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        value = REPLACEMENT;
                        break;
                    }
                }
                if ((buffer & 0x000000ff) == 0) {
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= 0xff & value;
                        return Character.highSurrogate(codePoint4());
                    } else {
                        value = REPLACEMENT;
                    }
                } else {
                    value = Character.lowSurrogate(codePoint4());
                }
            } else if ((buffer & 0x00ff0000) != 0) {
                if ((buffer & 0x0000ff00) == 0) {
                    if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                        buffer |= (0xff & value) << 8;
                    } else {
                        value = REPLACEMENT;
                        break;
                    }
                }
                if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                    buffer |= 0xff & value;
                    value = codePoint3();
                } else {
                    value = REPLACEMENT;
                }
            } else {
                assert (buffer & 0x0000ff00) != 0;
                assert (buffer & 0x000000ff) == 0;
                if (((value = in.getAsInt()) & 0xc0) == 0x80) {
                    buffer |= 0xff & value;
                    value = codePoint2();
                } else {
                    value = REPLACEMENT;
                }
            }
            break;
        }
        buffer = 0;
        return (char) value;
    }

    @Override
    public void finishPush(@NotNull CharConsumer out) {
        if (buffer != 0) {
            if ((buffer & 0xf8c0c0c0) == 0xf0808080) {
                out.accept(Character.lowSurrogate(codePoint4()));
            } else {
                LOGGER.warn("finish push {}", buffer);
                out.accept(REPLACEMENT);
            }
            buffer = 0;
        }
    }

    @Override
    public void finishPush(@NotNull IOIntConsumer out) throws IOException {
        if (buffer != 0) {
            if ((buffer & 0xf8c0c0c0) == 0xf0808080) {
                out.accept(Character.lowSurrogate(codePoint4()));
            } else {
                LOGGER.warn("finish push {}", buffer);
                out.accept(REPLACEMENT);
            }
            buffer = 0;
        }
    }

    @Override
    public boolean finishPullHasNext() {
        return buffer != 0;
    }

    @Override
    public char finishPullAsChar() {
        if (buffer != 0) {
            if ((buffer & 0xf8c0c0c0) == 0xf0808080) {
                char ch = Character.lowSurrogate(codePoint4());
                buffer = 0;
                return ch;
            } else {
                LOGGER.warn("finish pull {}", buffer);
                buffer = 0;
                return REPLACEMENT;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public int finishPullAsInt() {
        return finishPullAsChar();
    }
}
