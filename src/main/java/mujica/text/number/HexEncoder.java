package mujica.text.number;

import io.netty.buffer.ByteBuf;
import mujica.io.codec.Base16Case;
import mujica.reflect.modifier.CodeHistory;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2018/6/8", project = "existence", name = "Hex")
@CodeHistory(date = "2022/8/21", project = "Ultramarine", name = "HexCodec")
@CodeHistory(date = "2025/3/3")
public class HexEncoder implements Base16Case {

    public static final HexEncoder LOWER_ENCODER = new HexEncoder(LOWER);

    public static final HexEncoder UPPER_ENCODER = new HexEncoder(UPPER);

    protected final int alphabetOffset;

    protected HexEncoder(@MagicConstant(valuesFromClass = Base16Case.class) int alphabetOffset) {
        super();
        this.alphabetOffset = alphabetOffset;
    }

    @NotNull
    public char[] hexLookUpTable() {
        final char[] lut = new char[0x10];
        for (int i = 0; i < 0xa; i++) {
            lut[i] = (char) ('0' + i);
        }
        for (int i = 0xa; i < 0x10; i++) {
            lut[i] = (char) (alphabetOffset + i);
        }
        return lut;
    }

    public void hex4(@NotNull StringBuilder out, int value) {
        value &= 0xf;
        if (value < 0xa) {
            out.append((char) ('0' + value));
        } else {
            out.append((char) (alphabetOffset + value));
        }
    }

    public boolean hex4(@NotNull StringBuilder out, int value, boolean first) {
        value &= 0xf;
        if (first && value == 0) {
            return true;
        }
        if (value < 0xa) {
            out.append((char) ('0' + value));
        } else {
            out.append((char) (alphabetOffset + value));
        }
        return false;
    }

    public void hex8(@NotNull StringBuilder out, int value) {
        hex4(out, value >> 4);
        hex4(out, value);
    }

    public void hex16(@NotNull StringBuilder out, int value) {
        hex4(out, value >> 12);
        hex4(out, value >> 8);
        hex4(out, value >> 4);
        hex4(out, value);
    }

    public String hex16(int value) {
        final StringBuilder out = new StringBuilder(4);
        hex16(out, value);
        return out.toString();
    }

    public void hex32(@NotNull StringBuilder out, int value) {
        for (int shift = Integer.SIZE - 4; shift >= 0; shift -= 4) {
            hex4(out, value >> shift);
        }
    }

    public String hex32(int value) {
        final StringBuilder out = new StringBuilder(8);
        hex32(out, value);
        return out.toString();
    }

    public String hex32(float value) {
        final StringBuilder out = new StringBuilder(8);
        hex32(out, Float.floatToRawIntBits(value));
        return out.toString();
    }

    public String hex0x32(int value) {
        final StringBuilder out = new StringBuilder(10);
        out.append("0x");
        hex32(out, value);
        return out.toString();
    }

    public String hex0x32(float value) {
        final StringBuilder out = new StringBuilder(10);
        out.append("0x");
        hex32(out, Float.floatToRawIntBits(value));
        return out.toString();
    }

    public void hex64(@NotNull StringBuilder out, long value) {
        for (int shift = Long.SIZE - 4; shift >= 0; shift -= 4) {
            hex4(out, (int) (value >> shift));
        }
    }

    public String hex64(long value) {
        final StringBuilder out = new StringBuilder(16);
        hex64(out, value);
        return out.toString();
    }

    public String hex64(double value) {
        final StringBuilder out = new StringBuilder(16);
        hex64(out, Double.doubleToRawLongBits(value));
        return out.toString();
    }

    public String hex0x64(long value) {
        final StringBuilder out = new StringBuilder(18);
        out.append("0x");
        hex64(out, value);
        return out.toString();
    }

    public String hex0x64(double value) {
        final StringBuilder out = new StringBuilder(18);
        out.append("0x");
        hex64(out, Double.doubleToRawLongBits(value));
        return out.toString();
    }

    public void hexMax16(@NotNull StringBuilder out, int value) {
        boolean first = true;
        for (int shift = 16 - 4; shift >= 0; shift -= 4) {
            first = hex4(out, value >> shift, first);
        }
        if (first) {
            out.append('0');
        }
    }

    public String hexMax16(int value) {
        final StringBuilder out = new StringBuilder(4); // remain maximum
        hexMax16(out, value);
        return out.toString();
    }

    /**
     * For variable length codepoint
     */
    public void hexMax24(@NotNull StringBuilder out, int value) {
        boolean first = true;
        for (int shift = 24 - 4; shift >= 0; shift -= 4) {
            first = hex4(out, value >> shift, first);
        }
        if (first) {
            out.append('0');
        }
    }

    public void hexMax32(@NotNull StringBuilder out, int value) {
        boolean first = true;
        for (int shift = Integer.SIZE - 4; shift >= 0; shift -= 4) {
            first = hex4(out, value >> shift, first);
        }
        if (first) {
            out.append('0');
        }
    }

    public String hexMax32(int value) {
        final StringBuilder out = new StringBuilder(8); // remain maximum
        hexMax32(out, value);
        return out.toString();
    }

    public void hexMax64(@NotNull StringBuilder out, long value) {
        boolean first = true;
        for (int shift = Long.SIZE - 4; shift >= 0; shift -= 4) {
            first = hex4(out,(int) (value >> shift), first);
        }
        if (first) {
            out.append('0');
        }
    }

    public String hexMax64(long value) {
        final StringBuilder out = new StringBuilder(8); // half the maximum
        hexMax64(out, value);
        return out.toString();
    }

    public void hexDiv8(@NotNull StringBuilder out, @NotNull byte[] array, char div) {
        final int length = array.length;
        if (length == 0) {
            return;
        }
        hex8(out, array[0]);
        for (int index = 1; index < length; index++) {
            out.append(div);
            hex8(out, array[index]);
        }
    }

    public void hexDiv8(@NotNull StringBuilder out, @NotNull ByteBuf byteBuf, char div) {
        final int length = byteBuf.readableBytes();
        if (length == 0) {
            return;
        }
        hex8(out, byteBuf.getByte(0));
        for (int index = 1; index < length; index++) {
            out.append(div);
            hex8(out, byteBuf.getByte(index));
        }
    }

    public void hexDiv32(@NotNull StringBuilder out, @NotNull int[] array, char div) {
        final int length = array.length;
        if (length == 0) {
            return;
        }
        hex32(out, array[0]);
        for (int index = 1; index < length; index++) {
            out.append(div);
            hex32(out, array[index]);
        }
    }

    public void hexDiv64(@NotNull StringBuilder out, @NotNull long[] array, char div) {
        final int length = array.length;
        if (length == 0) {
            return;
        }
        hex64(out, array[0]);
        for (int index = 1; index < length; index++) {
            out.append(div);
            hex64(out, array[index]);
        }
    }

    public void hexARGB(@NotNull StringBuilder out, int value, boolean simplify) {
        if (simplify && ((value ^ (value >> 4)) & 0x0f0f0f0f) == 0) {
            if ((value & 0xff000000) != 0xff000000) {
                hex4(out, value >> 24);
            }
            hex4(out, value >> 16);
            hex4(out, value >> 8);
            hex4(out, value);
        } else {
            if ((value & 0xff000000) != 0xff000000) {
                hex4(out, value >> 28);
                hex4(out, value >> 24);
            }
            hex4(out, value >> 20);
            hex4(out, value >> 16);
            hex4(out, value >> 12);
            hex4(out, value >> 8);
            hex4(out, value >> 4);
            hex4(out, value);
        }
    }

    public String hexARGB(int value, boolean simplify) {
        final StringBuilder out = new StringBuilder(8);
        hexARGB(out, value, simplify);
        return out.toString();
    }

    public void hexRGBA(@NotNull StringBuilder out, int value, boolean simplify) {
        if (simplify && ((value ^ (value >> 4)) & 0x0f0f0f0f) == 0) {
            hex4(out, value >> 24);
            hex4(out, value >> 16);
            hex4(out, value >> 8);
            if ((value & 0x000000ff) != 0x000000ff) {
                hex4(out, value);
            }
        } else {
            hex4(out, value >> 28);
            hex4(out, value >> 24);
            hex4(out, value >> 20);
            hex4(out, value >> 16);
            hex4(out, value >> 12);
            hex4(out, value >> 8);
            if ((value & 0x000000ff) != 0x000000ff) {
                hex4(out, value >> 4);
                hex4(out, value);
            }
        }
    }

    public String hexRGBA(int value, boolean simplify) {
        final StringBuilder out = new StringBuilder(8);
        hexRGBA(out, value, simplify);
        return out.toString();
    }

    public void hexIPv6(@NotNull StringBuilder out, @NotNull byte[] address) {
        int index0 = -1;
        int index1 = -1;
        int position0 = -1;
        for (int position1 = 0; position1 < 16; position1 += 2) {
            if (address[position1] == 0 && address[position1 + 1] == 0) {
                if (position0 == -1) {
                    position0 = position1;
                }
            } else {
                if (position0 != -1 && position1 - position0 > index1 - index0) {
                    index0 = position0;
                    index1 = position1;
                }
                position0 = -1;
            }
        }
        if (position0 != -1 && 16 - position0 > index1 - index0) {
            index0 = position0;
            index1 = 16;
        }
        for (int j1 = 0; j1 < 16; j1 += 2) {
            if (index0 == j1) {
                out.append(':');
            }
            if (index0 <= j1 && j1 < index1) {
                continue;
            }
            if (j1 > 0) {
                out.append(':');
            }
            hexMax16(out, (0xFF00 & (address[j1] << 8)) | (0x00FF & address[j1 + 1]));
        }
        if (index1 == 16) {
            out.append(':');
        }
    }

    public String hexIPv6(@NotNull byte[] address) {
        final StringBuilder out = new StringBuilder(22);
        hexIPv6(out, address);
        return out.toString();
    }

    public void hexIPv6(@NotNull StringBuilder out, long high, long low) {
        final byte[] address = new byte[16];
        for (int index = 16 - 1; index >= 0; index--) {
            address[index] = (byte) low;
            if (index == 8) {
                low = high;
            } else {
                low >>= 8;
            }
        }
        hexIPv6(out, address);
    }

    public String hexIPv6(long high, long low) {
        final StringBuilder out = new StringBuilder(22);
        hexIPv6(out, high, low);
        return out.toString();
    }
}
