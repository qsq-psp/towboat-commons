package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.ds.of_int.map.IntMap;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Created on 2025/11/12.
 */
@CodeHistory(date = "2025/11/12")
public abstract class IntMapInflateInputStream extends ResidueInflateInputStream {

    @NotNull
    private final IntMap codeLengthDecodeMap, literalLengthDecodeMap, distanceDecodeMap;

    protected IntMapInflateInputStream(@NotNull InputStream in, @NotNull LookBackMemory runBuffer, @NotNull Supplier<IntMap> decodeMapSupplier) {
        super(in, runBuffer);
        codeLengthDecodeMap = decodeMapSupplier.get();
        literalLengthDecodeMap = decodeMapSupplier.get();
        distanceDecodeMap = decodeMapSupplier.get();
    }

    @Override
    public int read() throws IOException {
        while (true) {
            switch (state) {
                case STATE_FREE:
                    readBlock();
                    break;
                case STATE_LAST_BLOCK_FREE:
                    return -1;
                case STATE_NO_COMPRESSION:
                case STATE_LAST_BLOCK_NO_COMPRESSION:
                    if (remainingLength > 0) {
                        int data = readTrailingByte();
                        if (data == -1) {
                            throw new EOFException();
                        }
                        remainingLength--;
                        runBuffer.write(data);
                        return data;
                    } else {
                        state &= STATE_LAST_BLOCK_FREE;
                    }
                    break;
                case STATE_FIXED_HUFFMAN:
                case STATE_LAST_BLOCK_FIXED_HUFFMAN:
                case STATE_DYNAMIC_HUFFMAN:
                case STATE_LAST_BLOCK_DYNAMIC_HUFFMAN:
                    if (remainingLength > 0) {
                        remainingLength--;
                        return 0xff & runBuffer.copyAndWrite(copyDistance);
                    } else {
                        int symbol = readSymbol(literalLengthDecodeMap);
                        if (symbol > 0x101) {
                            symbol -= 0x102;
                            remainingLength = LENGTH_BASE[symbol];
                            remainingLength += readBits(LENGTH_EXTRA_BITS[symbol]);
                            symbol = readSymbol(distanceDecodeMap) - 1;
                            copyDistance = DISTANCE_BASE[symbol];
                            copyDistance += readBits(DISTANCE_EXTRA_BITS[symbol]);
                        } else if (symbol < 0x101) {
                            symbol--;
                            runBuffer.write(symbol);
                            return symbol;
                        } else {
                            state &= STATE_LAST_BLOCK_FREE;
                        }
                    }
                    break;
                case STATE_TRAILING_BITS:
                    return readTrailingBit();
                case STATE_TRAILING_BYTES:
                    return readTrailingByte();
                default:
                    throw new CompressAlgorithmException("state = " + state);
            }
        }
    }

    private void readBlock() throws IOException {
        state = STATE_NO_COMPRESSION + readBits(3);
        switch (state) {
            case STATE_NO_COMPRESSION:
            case STATE_LAST_BLOCK_NO_COMPRESSION:
                remainingLength = readNoCompressionLength();
                break;
            case STATE_FIXED_HUFFMAN:
            case STATE_LAST_BLOCK_FIXED_HUFFMAN:
                buildFixedDecodeMaps();
                break;
            case STATE_DYNAMIC_HUFFMAN:
            case STATE_LAST_BLOCK_DYNAMIC_HUFFMAN:
                buildDynamicDecodeMaps();
                break;
            default:
                throw new CompressAlgorithmException("state = " + state);
        }
    }

    private void buildFixedDecodeMaps() throws IOException {
        literalLengthDecodeMap.clear();
        for (int index = 0; index < 144; index++) { // 144
            putSymbol(literalLengthDecodeMap, 0b00110000 + index, 8, index + 1);
        }
        for (int index = 144; index < 256; index++) { // 112
            putSymbol(literalLengthDecodeMap, 0b110010000 - 144 + index, 9, index + 1);
        }
        for (int index = 256; index < 280; index++) { // 24
            putSymbol(literalLengthDecodeMap, index - 256, 7, index + 1);
        }
        // from 280 to 288 is also OK
        for (int index = 280; index < 286; index++) { // 6
            putSymbol(literalLengthDecodeMap, 0b11000000 - 280 + index, 8, index + 1);
        }
        distanceDecodeMap.clear();
        for (int index = 0; index < 30; index++) {
            putSymbol(distanceDecodeMap, index, 5, index + 1);
        }
    }

    private void buildDynamicDecodeMaps() throws IOException {
        final int literalLengthCodeCount = readBits(5) + 257;
        final int distanceCodeCount = readBits(5) + 1;
        final int codeLengthCodeCount = readBits(4) + 4;
        for (int index = 0; index < codeLengthCodeCount; index++) {
            commonAlphabet[REORDER[index] + 1] = readBits(3);
        }
        for (int index = codeLengthCodeCount; index < REORDER.length; index++) {
            commonAlphabet[REORDER[index] + 1] = 0;
        }
        buildDecodeMap(REORDER.length, codeLengthDecodeMap);
        readCodeLengths(literalLengthCodeCount);
        buildDecodeMap(literalLengthCodeCount, literalLengthDecodeMap);
        readCodeLengths(distanceCodeCount);
        buildDecodeMap(distanceCodeCount, distanceDecodeMap);
    }

    private void buildDecodeMap(int alphabetSize, @NotNull IntMap decodeMap) throws IOException {
        int maxLength = 0;
        for (int index = 1; index <= alphabetSize; index++) {
            int codeLength = commonAlphabet[index];
            if (codeLength > maxLength) {
                maxLength = codeLength;
            }
        }
        Arrays.fill(commonLengthCounts, 1, maxLength, 0);
        for (int index = 1; index <= alphabetSize; index++) {
            commonLengthCounts[commonAlphabet[index]]++;
        }
        commonLengthCounts[0] = 0;
        {
            int value = 0;
            for (int length = 0; length < maxLength; length++) {
                value += commonLengthCounts[length];
                value <<= 1;
                commonNextCode[length + 1] = value;
            }
        }
        decodeMap.clear();
        for (int symbol = 1; symbol <= alphabetSize; symbol++) {
            int codeLength = commonAlphabet[symbol];
            if (codeLength == 0) {
                continue;
            }
            putSymbol(decodeMap, commonNextCode[codeLength]++, codeLength, symbol);
        }
    }

    private void readCodeLengths(int targetCount) throws IOException {
        int lastSymbol = -1;
        int index = 0;
        while (index < targetCount) {
            int thisSymbol = readSymbol(codeLengthDecodeMap);
            switch (thisSymbol) {
                case  1: case  2: case  3: case  4:
                case  5: case  6: case  7: case  8:
                case  9: case 10: case 11: case 12:
                case 13: case 14: case 15: case 16:
                    thisSymbol--;
                    commonAlphabet[++index] = thisSymbol;
                    lastSymbol = thisSymbol;
                    break;
                case 17:
                    if (lastSymbol == -1) {
                        throw new CodecException();
                    }
                    for (int times = readBits(2) + 3; times > 0; times--) {
                        commonAlphabet[++index] = lastSymbol;
                    }
                    break;
                case 18:
                    for (int times = readBits(3) + 3; times > 0; times--) {
                        commonAlphabet[++index] = 0;
                    }
                    lastSymbol = 0;
                    break;
                case 19:
                    for (int times = readBits(7) + 11; times > 0; times--) {
                        commonAlphabet[++index] = 0;
                    }
                    lastSymbol = 0;
                    break;
                default:
                    throw new CodecException("code = " + thisSymbol);
            }
        }
        if (index != targetCount) {
            throw new CompressAlgorithmException();
        }
    }

    protected abstract void putSymbol(@NotNull IntMap decodeMap, int code, int codeLength, int symbol) throws IOException;

    protected abstract int readSymbol(@NotNull IntMap decodeMap) throws IOException;

    @CodeHistory(date = "2025/11/8")
    public static class Prefix extends IntMapInflateInputStream {

        public Prefix(@NotNull InputStream in, @NotNull LookBackMemory runBuffer, @NotNull Supplier<IntMap> decodeMapSupplier) {
            super(in, runBuffer, decodeMapSupplier);
        }

        @Override
        protected void putSymbol(@NotNull IntMap decodeMap, int code, int codeLength, int symbol) throws IOException {
            if (decodeMap.putInt(code | (1 << codeLength), symbol) != 0) {
                throw new CompressAlgorithmException();
            }
        }

        protected int readSymbol(@NotNull IntMap decodeMap) throws IOException {
            int code = 1;
            while (true) {
                if (code < 0) {
                    throw new CompressAlgorithmException();
                }
                code <<= 1;
                if (readBit()) {
                    code |= 1;
                }
                int symbol = decodeMap.getInt(code);
                if (symbol != 0) {
                    return symbol;
                }
            }
        }
    }

    @CodeHistory(date = "2025/11/6")
    public static class LengthValue extends IntMapInflateInputStream {

        public LengthValue(@NotNull InputStream in, @NotNull LookBackMemory runBuffer, @NotNull Supplier<IntMap> decodeMapSupplier) {
            super(in, runBuffer, decodeMapSupplier);
        }

        @Override
        protected void putSymbol(@NotNull IntMap decodeMap, int code, int codeLength, int symbol) throws IOException {
            // from LSB to MSB, first 8 bits codeLength, then 24 bits code
            if (decodeMap.putInt((code << Byte.SIZE) | codeLength, symbol) != 0) {
                throw new CompressAlgorithmException();
            }
        }

        protected int readSymbol(@NotNull IntMap decodeMap) throws IOException {
            int length = 0;
            int code = 0;
            while (true) {
                if (length == Integer.SIZE - Byte.SIZE) {
                    throw new CodecException("length = " + length + ", code = " + Integer.toBinaryString(code >>> Byte.SIZE));
                }
                length++;
                code <<= 1;
                if (readBit()) {
                    code |= 1 << Byte.SIZE;
                }
                int symbol = decodeMap.getInt(code | length);
                if (symbol != 0) {
                    return symbol;
                }
            }
        }
    }

    @CodeHistory(date = "2025/11/14")
    public static class ValueLength extends IntMapInflateInputStream {

        public ValueLength(@NotNull InputStream in, @NotNull LookBackMemory runBuffer, @NotNull Supplier<IntMap> decodeMapSupplier) {
            super(in, runBuffer, decodeMapSupplier);
        }

        @Override
        protected void putSymbol(@NotNull IntMap decodeMap, int code, int codeLength, int symbol) throws IOException {
            // from LSB to MSB, first 24 bits code, then 8 bits codeLength
            if (decodeMap.putInt((codeLength << (Integer.SIZE - Byte.SIZE)) | code, symbol) != 0) {
                throw new CompressAlgorithmException();
            }
        }

        @Override
        protected int readSymbol(@NotNull IntMap decodeMap) throws IOException {
            int length = 0;
            int code = 0;
            while (true) {
                if (length == Integer.SIZE - Byte.SIZE) {
                    throw new CodecException("length = " + length + ", code = " + Integer.toBinaryString(code >>> Byte.SIZE));
                }
                length++;
                code <<= 1;
                if (readBit()) {
                    code |= 1;
                }
                int symbol = decodeMap.getInt((length << (Integer.SIZE - Byte.SIZE)) | code);
                if (symbol != 0) {
                    return symbol;
                }
            }
        }
    }
}
