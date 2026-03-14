package mujica.text.number;

import mujica.io.codec.Base16Case;
import mujica.reflect.modifier.CodeHistory;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2018/6/8", project = "existence", name = "Hex")
@CodeHistory(date = "2022/8/21", project = "Ultramarine", name = "HexCodec")
@CodeHistory(date = "2025/3/3")
public class HexEncoder implements Base16Case {

    public static final HexEncoder LOWER_ENCODER = new HexEncoder(LOWER_CONSTANT);

    public static final HexEncoder UPPER_ENCODER = new HexEncoder(UPPER_CONSTANT);

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

    public void hex4(int value, @NotNull StringBuilder out) {
        value &= 0xf;
        if (value < 0xa) {
            out.append((char) ('0' + value));
        } else {
            out.append((char) (alphabetOffset + value));
        }
    }

    public void hex4(int value, @NotNull StringBuffer out) {
        value &= 0xf;
        if (value < 0xa) {
            out.append((char) ('0' + value));
        } else {
            out.append((char) (alphabetOffset + value));
        }
    }

    public boolean hex4(int value, boolean first, @NotNull StringBuilder out) {
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

    public boolean hex4(int value, boolean first, @NotNull StringBuffer out) {
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

    public void hex8(int value, @NotNull StringBuilder out) {
        hex4(value >> 4, out);
        hex4(value, out);
    }

    public void hex8(int value, @NotNull StringBuffer out) {
        hex4(value >> 4, out);
        hex4(value, out);
    }

    public void hex16(int value, @NotNull StringBuilder out) {
        hex4(value >> 12, out);
        hex4(value >> 8, out);
        hex4(value >> 4, out);
        hex4(value, out);
    }

    public void hex16(int value, @NotNull StringBuffer out) {
        hex4(value >> 12, out);
        hex4(value >> 8, out);
        hex4(value >> 4, out);
        hex4(value, out);
    }

    public String hex16(int value) {
        final StringBuilder out = new StringBuilder(4);
        hex16(value, out);
        return out.toString();
    }

    public void hex32(int value, @NotNull StringBuilder out) {
        for (int shift = Integer.SIZE - 4; shift >= 0; shift -= 4) {
            hex4(value >> shift, out);
        }
    }

    public void hex32(int value, @NotNull StringBuffer out) {
        for (int shift = Integer.SIZE - 4; shift >= 0; shift -= 4) {
            hex4(value >> shift, out);
        }
    }

    public String hex32(int value) {
        final StringBuilder out = new StringBuilder(8);
        hex32(value, out);
        return out.toString();
    }

    public String hex32(float value) {
        final StringBuilder out = new StringBuilder(8);
        hex32(Float.floatToRawIntBits(value), out);
        return out.toString();
    }

    public String hex0x32(int value) {
        final StringBuilder out = new StringBuilder(10);
        out.append("0x");
        hex32(value, out);
        return out.toString();
    }

    public String hex0x32(float value) {
        final StringBuilder out = new StringBuilder(10);
        out.append("0x");
        hex32(Float.floatToRawIntBits(value), out);
        return out.toString();
    }

    public void hex64(long value, @NotNull StringBuilder out) {
        for (int shift = Long.SIZE - 4; shift >= 0; shift -= 4) {
            hex4((int) (value >> shift), out);
        }
    }

    public void hex64(long value, @NotNull StringBuffer out) {
        for (int shift = Long.SIZE - 4; shift >= 0; shift -= 4) {
            hex4((int) (value >> shift), out);
        }
    }

    @NotNull
    public String hex64(long value) {
        final StringBuilder out = new StringBuilder(16);
        hex64(value, out);
        return out.toString();
    }

    @NotNull
    public String hex64(double value) {
        final StringBuilder out = new StringBuilder(16);
        hex64(Double.doubleToRawLongBits(value), out);
        return out.toString();
    }

    @NotNull
    public String hex0x64(long value) {
        final StringBuilder out = new StringBuilder(18);
        out.append("0x");
        hex64(value, out);
        return out.toString();
    }

    @NotNull
    public String hex0x64(double value) {
        final StringBuilder out = new StringBuilder(18);
        out.append("0x");
        hex64(Double.doubleToRawLongBits(value), out);
        return out.toString();
    }

    public void hexMax16(int value, @NotNull StringBuilder out) {
        boolean first = true;
        for (int shift = 16 - 4; shift >= 0; shift -= 4) {
            first = hex4(value >> shift, first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    public void hexMax16(int value, @NotNull StringBuffer out) {
        boolean first = true;
        for (int shift = 16 - 4; shift >= 0; shift -= 4) {
            first = hex4(value >> shift, first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    @NotNull
    public String hexMax16(int value) {
        final StringBuilder out = new StringBuilder(4); // remain maximum
        hexMax16(value, out);
        return out.toString();
    }

    public void hexMax24(int value, @NotNull StringBuilder out) {
        boolean first = true;
        for (int shift = 24 - 4; shift >= 0; shift -= 4) {
            first = hex4(value >> shift, first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    public void hexMax24(int value, @NotNull StringBuffer out) {
        boolean first = true;
        for (int shift = 24 - 4; shift >= 0; shift -= 4) {
            first = hex4(value >> shift, first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    public void hexMax32(int value, @NotNull StringBuilder out) {
        boolean first = true;
        for (int shift = Integer.SIZE - 4; shift >= 0; shift -= 4) {
            first = hex4(value >> shift, first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    public void hexMax32(int value, @NotNull StringBuffer out) {
        boolean first = true;
        for (int shift = Integer.SIZE - 4; shift >= 0; shift -= 4) {
            first = hex4(value >> shift, first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    @NotNull
    public String hexMax32(int value) {
        final StringBuilder out = new StringBuilder(8);
        hexMax32(value, out);
        return out.toString();
    }

    public void hexMax64(long value, @NotNull StringBuilder out) {
        boolean first = true;
        for (int shift = Long.SIZE - 4; shift >= 0; shift -= 4) {
            first = hex4((int) (value >> shift), first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    public void hexMax64(long value, @NotNull StringBuffer out) {
        boolean first = true;
        for (int shift = Long.SIZE - 4; shift >= 0; shift -= 4) {
            first = hex4((int) (value >> shift), first, out);
        }
        if (first) {
            out.append('0');
        }
    }

    @NotNull
    public String hexMax64(long value) {
        final StringBuilder out = new StringBuilder(8); // half the maximum
        hexMax64(value, out);
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
            hexMax16((0xff00 & (address[j1] << 8)) | (0x00ff & address[j1 + 1]), out);
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
