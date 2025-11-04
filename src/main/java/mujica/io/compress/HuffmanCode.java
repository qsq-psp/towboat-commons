package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.ds.DataStructure;
import mujica.ds.InvariantException;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

@CodeHistory(date = "2025/10/6")
@ReferencePage(title = "RFC1951", href = "https://www.rfc-editor.org/rfc/rfc1951.html")
public class HuffmanCode implements DataStructure {

    private static final long serialVersionUID = 0xAA0EBBB359BB4E1DL;

    int length;

    int value;

    public HuffmanCode() {
        super();
    }

    public HuffmanCode(int length) {
        super();
        this.length = length;
    }

    public HuffmanCode(int length, int value) {
        super();
        this.length = length;
        this.value = value;
    }

    @NotNull
    @Override
    public HuffmanCode duplicate() {
        return new HuffmanCode(length, value);
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        if (0 <= length && length <= Integer.SIZE) {
            if (length != Integer.SIZE && (value & (-1 << length)) != 0) {
                consumer.accept(new InvariantException(
                        "code length = " + length + ", value = 0b" + Integer.toBinaryString(value)
                ));
            }
        } else {
            consumer.accept(new InvariantException(
                    "code length = " + length + " out of range"
            ));
        }
    }

    public void reset() {
        length = 0;
        value = 0;
    }

    public void set(@NotNull HuffmanCode that) {
        this.length = that.length;
        this.value = that.value;
    }

    public void addLast(boolean bit) {
        if (length++ == Integer.SIZE) {
            throw new CodecException();
        }
        value <<= 1;
        if (bit) {
            value |= 1;
        }
    }

    public void removeLast() {
        if (length-- == 0) {
            throw new CodecException();
        }
        value >>= 1;
    }

    @NotNull
    @Override
    public String summaryToString() {
        return "<length = " + length + ", value = " + value + ">";
    }

    @NotNull
    @Override
    public String detailToString() {
        return "";
    }

    @Override
    public int hashCode() {
        return length * 0x1caf + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HuffmanCode)) {
            return false;
        }
        final HuffmanCode that = (HuffmanCode) obj;
        return this.value == that.value && this.length == that.length;
    }

    @Override
    @NotNull
    public String toString() {
        final char[] chars = new char[length];
        for (int index = 0; index < length; index++) {
            chars[index] = (value & (1 << (length - 1 - index))) != 0 ? '1' : '0';
        }
        return new String(chars);
    }

    public static void fixedLiteralLengthAlphabet(@NotNull HuffmanCode[] alphabet) {
        for (int index = 0; index < 144; index++) {
            alphabet[index] = new HuffmanCode(8, 0b00110000 + index);
        }
        for (int index = 144; index < 256; index++) {
            alphabet[index] = new HuffmanCode(9, 0b110010000 - 144 + index);
        }
        for (int index = 256; index < 280; index++) {
            alphabet[index] = new HuffmanCode(7, index - 256);
        }
        for (int index = 280; index < 286; index++) {
            alphabet[index] = new HuffmanCode(8, 0b11000000 - 280 + index);
        }
    }

    @NotNull
    public static HuffmanCode[] fixedLiteralLengthAlphabet() {
        final HuffmanCode[] alphabet = new HuffmanCode[286];
        fixedLiteralLengthAlphabet(alphabet);
        return alphabet;
    }

    public static void fixedDistanceAlphabet(@NotNull HuffmanCode[] alphabet) {
        for (int index = 0; index < 32; index++) {
            alphabet[index] = new HuffmanCode(5, index);
        }
    }

    @NotNull
    public static HuffmanCode[] fixedDistanceAlphabet() {
        final HuffmanCode[] alphabet = new HuffmanCode[32];
        fixedDistanceAlphabet(alphabet);
        return alphabet;
    }

    public static void canonicalConstruct(@NotNull HuffmanCode[] alphabet, int alphabetSize) {
        int maxLength = 0;
        for (int index = 0; index < alphabetSize; index++) {
            HuffmanCode code = alphabet[index];
            if (code.length > maxLength) {
                maxLength = code.length;
            }
        }
        final int[] lengthCount = new int[maxLength + 1];
        for (int index = 0; index < alphabetSize; index++) {
            lengthCount[alphabet[index].length]++;
        }
        lengthCount[0] = 0;
        final int[] nextCode = new int[maxLength + 1];
        {
            int value = 0;
            for (int length = 0; length < maxLength; length++) {
                value += lengthCount[length];
                value <<= 1;
                nextCode[length + 1] = value;
            }
        }
        for (int index = 0; index < alphabetSize; index++) {
            HuffmanCode code = alphabet[index];
            if (code.length == 0) {
                continue;
            }
            code.value = nextCode[code.length]++;
        }
    }

    @NotNull
    public static HuffmanCode[] canonicalConstruct(@NotNull int[] bitLengths) {
        final int alphabetSize = bitLengths.length;
        final HuffmanCode[] alphabet = new HuffmanCode[alphabetSize];
        for (int index = 0; index < alphabetSize; index++) {
            alphabet[index] = new HuffmanCode(bitLengths[index]);
        }
        canonicalConstruct(alphabet, alphabetSize);
        return alphabet;
    }

    public static void toDecodeMap(@NotNull HuffmanCode[] alphabet, @NotNull HashMap<HuffmanCode, Integer> map) {
        map.clear();
        final int alphabetSize = alphabet.length;
        for (int index = 0; index < alphabetSize; index++) {
            HuffmanCode code = alphabet[index];
            if (code.length == 0) {
                continue;
            }
            map.put(code, index);
        }
    }

    public static void checkHealth(@NotNull HashMap<HuffmanCode, Integer> decodeMap, @NotNull Consumer<RuntimeException> consumer) {
        final HuffmanCode probeKey = new HuffmanCode();
        for (HuffmanCode code : decodeMap.keySet()) {
            code.checkHealth(consumer);
            probeKey.set(code);
            while (true) {
                probeKey.removeLast();
                if (probeKey.length <= 0) {
                    break;
                }
                Integer conflict = decodeMap.get(probeKey);
                if (conflict != null) {
                    consumer.accept(new InvariantException(
                            "code " + code + " at " + decodeMap.get(code) + " conflicts with code " + probeKey + " at " + conflict
                    ));
                }
            }
        }
    }

    public static void checkHealth(@NotNull HuffmanCode[] alphabet, @NotNull Consumer<RuntimeException> consumer) {
        final HashMap<HuffmanCode, Integer> decodeMap = new HashMap<>();
        toDecodeMap(alphabet, decodeMap);
        if (alphabet.length != decodeMap.size()) {
            consumer.accept(new InvariantException("alphabet size = " + alphabet.length + " but decode map size = " + decodeMap.size()));
        }
        checkHealth(decodeMap, consumer);
    }

    public static void checkHealth(@NotNull HashMap<HuffmanCode, Integer> decodeMap) {
        checkHealth(decodeMap, re -> {throw re;});
    }

    public static void checkHealth(@NotNull HuffmanCode[] alphabet) {
        checkHealth(alphabet, re -> {throw re;});
    }
}
