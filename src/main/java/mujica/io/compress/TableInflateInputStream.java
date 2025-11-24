package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Created on 2025/11/10.
 */
public class TableInflateInputStream extends ResidueInflateInputStream {

    private int codeLengthMaxLength, literalLengthMaxLength, distanceMaxLength;

    private short[] codeLengthDecodeTable, literalLengthDecodeTable, distanceDecodeTable;

    public TableInflateInputStream(@NotNull InputStream in, @NotNull LookBackMemory runBuffer) {
        super(in, runBuffer);
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
                        int data = in.read();
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
                        int symbol = readSymbol(literalLengthMaxLength, literalLengthDecodeTable);
                        if (symbol > 0x100) {
                            symbol -= 0x101;
                            remainingLength = LENGTH_BASE[symbol];
                            remainingLength += readBits(LENGTH_EXTRA_BITS[symbol]);
                            symbol = readSymbol(distanceMaxLength, distanceDecodeTable);
                            copyDistance = DISTANCE_BASE[symbol];
                            copyDistance += readBits(DISTANCE_EXTRA_BITS[symbol]);
                        } else if (symbol < 0x100) {
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
                    throw new CodecException();
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
        int maxCodeLength = 9;
        literalLengthMaxLength = maxCodeLength;
        literalLengthDecodeTable = clearTable(literalLengthDecodeTable, 1 << maxCodeLength);
        for (int index = 0; index < 144; index++) { // 144
            putSymbol(literalLengthDecodeTable, 0b00110000 + index, 8, maxCodeLength, index);
        }
        for (int index = 144; index < 256; index++) { // 112
            putSymbol(literalLengthDecodeTable, 0b110010000 - 144 + index, 9, maxCodeLength, index);
        }
        for (int index = 256; index < 280; index++) { // 24
            putSymbol(literalLengthDecodeTable, index - 256, 7, maxCodeLength, index);
        }
        for (int index = 280; index < 286; index++) { // 6
            putSymbol(literalLengthDecodeTable, 0b11000000 - 280 + index, 8, maxCodeLength, index);
        }
        maxCodeLength = 5;
        distanceMaxLength = maxCodeLength;
        distanceDecodeTable = clearTable(distanceDecodeTable, 1 << maxCodeLength);
        for (int index = 0; index < 30; index++) {
            putSymbol(distanceDecodeTable, index, 5, maxCodeLength, index);
        }
    }

    private void buildDynamicDecodeMaps() throws IOException {
        final int literalLengthCodeCount = readBits(5) + 257;
        final int distanceCodeCount = readBits(5) + 1;
        final int codeLengthCodeCount = readBits(4) + 4;
        for (int index = 0; index < codeLengthCodeCount; index++) {
            commonAlphabet[REORDER[index]] = readBits(3);
        }
        for (int index = codeLengthCodeCount; index < REORDER.length; index++) {
            commonAlphabet[REORDER[index]] = 0;
        }
        buildDecodeTable(REORDER.length, codeLengthDecodeTable, maxLength -> codeLengthMaxLength = maxLength, decodeTable -> codeLengthDecodeTable = decodeTable);
        readCodeLengths(literalLengthCodeCount);
        buildDecodeTable(literalLengthCodeCount, literalLengthDecodeTable, maxLength -> literalLengthMaxLength = maxLength, decodeTable -> literalLengthDecodeTable = decodeTable);
        readCodeLengths(distanceCodeCount);
        buildDecodeTable(distanceCodeCount, distanceDecodeTable, maxLength -> distanceMaxLength = maxLength, decodeTable -> distanceDecodeTable = decodeTable);
    }

    private void buildDecodeTable(int alphabetSize, @Nullable short[] decodeTable, @NotNull IntConsumer maxLengthReturn, @NotNull Consumer<short[]> decodeTableReturn) {
        int maxLength = 0;
        for (int index = 0; index < alphabetSize; index++) {
            int codeLength = commonAlphabet[index];
            if (codeLength > maxLength) {
                maxLength = codeLength;
            }
        }
        maxLengthReturn.accept(maxLength);
        Arrays.fill(commonLengthCounts, 1, maxLength, 0);
        for (int index = 0; index < alphabetSize; index++) {
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
        decodeTable = clearTable(decodeTable, 1 << maxLength);
        decodeTableReturn.accept(decodeTable);
        for (int symbol = 0; symbol < alphabetSize; symbol++) {
            int codeLength = commonAlphabet[symbol];
            if (codeLength == 0) {
                continue;
            }
            putSymbol(decodeTable, commonNextCode[codeLength]++, codeLength, maxLength, symbol);
        }
        // System.out.println("alphabet = " + Arrays.toString(Arrays.copyOf(commonAlphabet, alphabetSize)));
        // System.out.println("decodeTable = " + Arrays.toString(Arrays.copyOf(decodeTable, 1 << maxLength)));
    }

    @NotNull
    private short[] clearTable(@Nullable short[] decodeTable, int tableSize) {
        if (decodeTable == null || decodeTable.length < tableSize) {
            decodeTable = new short[tableSize];
        } else {
            Arrays.fill(decodeTable, 0, tableSize, (short) 0);
        }
        return decodeTable;
    }

    private void putSymbol(@NotNull short[] table, int code, int codeLength, int maxLength, int symbol) {
        final short value = (short) ((symbol << 4) | codeLength);
        final int shift = maxLength - codeLength;
        final int reverseShift = Integer.SIZE - maxLength;
        final int startCode = code << shift;
        final int endCode = (code + 1) << shift;
        for (code = startCode; code < endCode; code++) {
            table[Integer.reverse(code) >>> reverseShift] = value;
        }
    }

    private void readCodeLengths(int targetCount) throws IOException {
        int lastSymbol = -1;
        int index = 0;
        while (index < targetCount) {
            int thisSymbol = readSymbol(codeLengthMaxLength, codeLengthDecodeTable);
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
                    for (int times = readBits(2) + 3; times > 0; times--) {
                        commonAlphabet[index++] = lastSymbol;
                    }
                    break;
                case 17:
                    for (int times = readBits(3) + 3; times > 0; times--) {
                        commonAlphabet[index++] = 0;
                    }
                    lastSymbol = 0;
                    break;
                case 18:
                    for (int times = readBits(7) + 11; times > 0; times--) {
                        commonAlphabet[index++] = 0;
                    }
                    lastSymbol = 0;
                    break;
                default:
                    throw new CodecException("code = " + thisSymbol);
            }
        }
        if (index != targetCount) {
            throw new CodecException();
        }
    }

    private int readSymbol(int maxLength, short[] decodeTable) throws IOException {
        try {
            int symbol = decodeTable[readBitsAhead(maxLength)];
            if (symbol == 0) {
                throw new CodecException();
            }
            skipBits(0xf & symbol);
            return symbol >> 4;
        } catch (EOFException e) {
            int codeLength = 0;
            int code = 0;
            while (codeLength < maxLength) {
                if (readBit()) {
                    code |= 1 << codeLength;
                }
                codeLength++;
                int symbol = decodeTable[code];
                if (codeLength == (0xf & symbol)) {
                    return symbol >> 4;
                }
            }
            throw e;
        }
    }
}
