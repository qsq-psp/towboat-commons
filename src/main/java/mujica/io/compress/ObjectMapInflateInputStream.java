package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.ds.generic.list.TruncateList;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

@CodeHistory(date = "2025/11/13")
public class ObjectMapInflateInputStream extends ResidueInflateInputStream {

    @CodeHistory(date = "2025/10/6")
    private static class HuffmanCode extends TruncateList<Boolean> implements Comparable<TruncateList<Boolean>> {

        private static final long serialVersionUID = 0xc274db34f616e2a6L;

        HuffmanCode(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public int compareTo(@NotNull TruncateList<Boolean> that) {
            return 0;
        }
    }

    @NotNull
    private final HashMap<HuffmanCode, Character> codeLengthDecodeMap, literalLengthDecodeMap, distanceDecodeMap;

    public ObjectMapInflateInputStream(@NotNull InputStream in, @NotNull RunBuffer runBuffer) {
        super(in, runBuffer);
        codeLengthDecodeMap = new HashMap<>();
        literalLengthDecodeMap = new HashMap<>();
        distanceDecodeMap = new HashMap<>();
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
                        runBuffer.put((byte) data);
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
                        return 0xff & runBuffer.copy(copyDistance);
                    } else {
                        int symbol = readSymbol(literalLengthDecodeMap);
                        if (symbol > 0x100) {
                            symbol -= 0x101;
                            remainingLength = LENGTH_BASE[symbol];
                            remainingLength += readBits(LENGTH_EXTRA_BITS[symbol]);
                            symbol = readSymbol(distanceDecodeMap);
                            copyDistance = DISTANCE_BASE[symbol];
                            copyDistance += readBits(DISTANCE_EXTRA_BITS[symbol]);
                        } else if (symbol < 0x100) {
                            runBuffer.put((byte) symbol);
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

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        if (length <= 0) {
            return 0;
        }
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
                        if (bitLength() != 0) {
                            int data = readTrailingByte();
                            if (data == -1) {
                                throw new EOFException();
                            }
                            remainingLength--;
                            array[offset] = (byte) data;
                            runBuffer.put((byte) data);
                            return 1;
                        } else {
                            length = Math.min(length, remainingLength);
                            length = in.read(array, offset, length);
                            if (length == -1) {
                                throw new EOFException();
                            }
                            remainingLength -= length;
                            runBuffer.putFully(array, offset, length);
                            return length;
                        }
                    } else {
                        state &= STATE_LAST_BLOCK_FREE;
                    }
                    break;
                case STATE_FIXED_HUFFMAN:
                case STATE_LAST_BLOCK_FIXED_HUFFMAN:
                case STATE_DYNAMIC_HUFFMAN:
                case STATE_LAST_BLOCK_DYNAMIC_HUFFMAN:
                    if (remainingLength > 0) {
                        length = Math.min(length, remainingLength);
                        length = runBuffer.copy(copyDistance, array, offset, length);
                        remainingLength -= length;
                        return length;
                    } else {
                        int symbol = readSymbol(literalLengthDecodeMap);
                        if (symbol > 0x100) {
                            symbol -= 0x101;
                            remainingLength = LENGTH_BASE[symbol];
                            remainingLength += readBits(LENGTH_EXTRA_BITS[symbol]);
                            symbol = readSymbol(distanceDecodeMap);
                            copyDistance = DISTANCE_BASE[symbol];
                            copyDistance += readBits(DISTANCE_EXTRA_BITS[symbol]);
                        } else if (symbol < 0x100) {
                            array[offset] = (byte) symbol;
                            runBuffer.put((byte) symbol);
                            return 1;
                        } else {
                            state &= STATE_LAST_BLOCK_FREE;
                        }
                    }
                    break;
                case STATE_TRAILING_BITS: {
                    int data = readTrailingBit();
                    if (data == -1) {
                        return -1;
                    }
                    array[offset] = (byte) data;
                    return 1;
                }
                case STATE_TRAILING_BYTES:
                    if (bitLength() != 0) {
                        int data = readTrailingByte();
                        if (data == -1) {
                            return -1;
                        }
                        array[offset] = (byte) data;
                        return 1;
                    } else {
                        length = in.read(array, offset, length);
                        return length;
                    }
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
                throw new CodecException();
        }
    }

    private void buildFixedDecodeMaps() {
        literalLengthDecodeMap.clear();
        for (int symbol = 0; symbol < 144; symbol++) { // 144
            putSymbol(literalLengthDecodeMap, 0b00110000 + symbol, 8, symbol);
        }
        for (int symbol = 144; symbol < 256; symbol++) { // 112
            putSymbol(literalLengthDecodeMap, 0b110010000 - 144 + symbol, 9, symbol);
        }
        for (int symbol = 256; symbol < 280; symbol++) { // 24
            putSymbol(literalLengthDecodeMap, symbol - 256, 7, symbol);
        }
        for (int symbol = 280; symbol < 286; symbol++) { // 6
            putSymbol(literalLengthDecodeMap, 0b11000000 - 280 + symbol, 8, symbol);
        }
        distanceDecodeMap.clear();
        for (int symbol = 0; symbol < 30; symbol++) {
            putSymbol(distanceDecodeMap, symbol, 5, symbol);
        }
    }

    private void buildDynamicDecodeMaps() throws IOException {
        final int literalLengthCodeCount = 257 + readBits(5);
        final int distanceCodeCount = 1 + readBits(5);
        final int codeLengthCodeCount = 4 + readBits(4);
        for (int index = 0; index < codeLengthCodeCount; index++) {
            commonAlphabet[REORDER[index]] = readBits(3);
        }
        for (int index = codeLengthCodeCount; index < REORDER.length; index++) {
            commonAlphabet[REORDER[index]] = 0;
        }
        buildDecodeMap(REORDER.length, codeLengthDecodeMap);
        readCodeLengths(literalLengthCodeCount);
        buildDecodeMap(literalLengthCodeCount, literalLengthDecodeMap);
        readCodeLengths(distanceCodeCount);
        buildDecodeMap(distanceCodeCount, distanceDecodeMap);
    }

    private void buildDecodeMap(int alphabetSize, @NotNull HashMap<HuffmanCode, Character> decodeMap) {
        int maxLength = 0;
        for (int symbol = 0; symbol < alphabetSize; symbol++) {
            int codeLength = commonAlphabet[symbol];
            if (codeLength > maxLength) {
                maxLength = codeLength;
            }
        }
        Arrays.fill(commonLengthCounts, 1, maxLength, 0);
        for (int symbol = 0; symbol < alphabetSize; symbol++) {
            commonLengthCounts[commonAlphabet[symbol]]++;
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
        for (int symbol = 0; symbol < alphabetSize; symbol++) {
            int codeLength = commonAlphabet[symbol];
            if (codeLength != 0) {
                putSymbol(decodeMap, commonNextCode[codeLength]++, codeLength, symbol);
            }
        }
    }

    private void putSymbol(@NotNull HashMap<HuffmanCode, Character> decodeMap, int code, int codeLength, int symbol) {
        final HuffmanCode huffmanCode = new HuffmanCode(codeLength);
        while (codeLength > 0) {
            codeLength--;
            huffmanCode.add((code & (1 << codeLength)) != 0);
        }
        decodeMap.put(huffmanCode, (char) symbol);
    }

    private void readCodeLengths(int targetCount) throws IOException {
        int lastSymbol = -1;
        int index = 0;
        while (index < targetCount) {
            int thisSymbol = readSymbol(codeLengthDecodeMap);
            switch (thisSymbol) {
                case  0: case  1: case  2: case  3:
                case  4: case  5: case  6: case  7:
                case  8: case  9: case 10: case 11:
                case 12: case 13: case 14: case 15:
                    commonAlphabet[index++] = thisSymbol;
                    lastSymbol = thisSymbol;
                    break;
                case 16:
                    if (lastSymbol == -1) {
                        throw new CodecException();
                    }
                    for (int repeatIndex = readBits(2) + 3; repeatIndex > 0; repeatIndex--) {
                        commonAlphabet[index++] = lastSymbol;
                    }
                    break;
                case 17:
                    for (int repeatIndex = readBits(3) + 3; repeatIndex > 0; repeatIndex--) {
                        commonAlphabet[index++] = 0;
                    }
                    lastSymbol = 0;
                    break;
                case 18:
                    for (int repeatIndex = readBits(7) + 11; repeatIndex > 0; repeatIndex--) {
                        commonAlphabet[index++] = 0;
                    }
                    lastSymbol = 0;
                    break;
                default:
                    throw new CodecException("symbol = " + thisSymbol);
            }
        }
        if (index != targetCount) {
            throw new CodecException();
        }
    }

    private final HuffmanCode key = new HuffmanCode(MAX_CODE_LENGTH);

    private int readSymbol(@NotNull HashMap<HuffmanCode, Character> decodeMap) throws IOException {
        key.clear();
        while (key.size() < MAX_CODE_LENGTH) {
            key.add(readBit());
            Character symbol = decodeMap.get(key);
            if (symbol != null) {
                return symbol;
            }
        }
        throw new CodecException();
    }
}
