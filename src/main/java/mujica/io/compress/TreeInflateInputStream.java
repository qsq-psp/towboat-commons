package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@CodeHistory(date = "2025/11/9")
public class TreeInflateInputStream extends ResidueInflateInputStream {

    @CodeHistory(date = "2018/10/17", project = "TubeM", name = "HuffmanTreeNode")
    @CodeHistory(date = "2025/11/9")
    private static class HuffmanNode {

        HuffmanNode left, right;

        int symbol = -1;

        long useCount;
    }

    @NotNull
    private final HuffmanNode codeLengthDecodeRoot, literalLengthDecodeRoot, distanceDecodeRoot;

    @NotNull
    private long[] blockStatistics = new long[8];

    private static final int BS_FIXED_HUFFMAN_BLOCK_COUNT       = 0;
    private static final int BS_FIXED_HUFFMAN_COPY_BYTES        = 1;
    private static final int BS_FIXED_HUFFMAN_DECODE_BYTES      = 2;
    private static final int BS_DYNAMIC_HUFFMAN_BLOCK_COUNT     = 3;
    private static final int BS_DYNAMIC_HUFFMAN_COPY_BYTES      = 4;
    private static final int BS_DYNAMIC_HUFFMAN_DECODE_BYTES    = 5;
    private static final int BS_NO_COMPRESSION_BLOCK_COUNT      = 6;
    private static final int BS_NO_COMPRESSION_COPY_BYTES       = 7;

    private int bsBaseIndex;

    public TreeInflateInputStream(@NotNull InputStream in, @NotNull LookBackMemory runBuffer) {
        super(in, runBuffer);
        codeLengthDecodeRoot = new HuffmanNode();
        literalLengthDecodeRoot = new HuffmanNode();
        distanceDecodeRoot = new HuffmanNode();
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
                        blockStatistics[BS_NO_COMPRESSION_COPY_BYTES]++;
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
                        blockStatistics[bsBaseIndex + BS_FIXED_HUFFMAN_COPY_BYTES]++;
                        return 0xff & runBuffer.copyAndWrite(copyDistance);
                    } else {
                        int symbol = readSymbol(literalLengthDecodeRoot);
                        if (symbol > 0x100) {
                            symbol -= 0x101;
                            remainingLength = LENGTH_BASE[symbol];
                            remainingLength += readBits(LENGTH_EXTRA_BITS[symbol]);
                            symbol = readSymbol(distanceDecodeRoot);
                            copyDistance = DISTANCE_BASE[symbol];
                            copyDistance += readBits(DISTANCE_EXTRA_BITS[symbol]);
                        } else if (symbol < 0x100) {
                            blockStatistics[bsBaseIndex + BS_FIXED_HUFFMAN_DECODE_BYTES]++;
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
                bsBaseIndex = BS_NO_COMPRESSION_BLOCK_COUNT;
                remainingLength = readNoCompressionLength();
                break;
            case STATE_FIXED_HUFFMAN:
            case STATE_LAST_BLOCK_FIXED_HUFFMAN:
                bsBaseIndex = BS_FIXED_HUFFMAN_BLOCK_COUNT;
                buildFixedDecodeMaps();
                break;
            case STATE_DYNAMIC_HUFFMAN:
            case STATE_LAST_BLOCK_DYNAMIC_HUFFMAN:
                bsBaseIndex = BS_DYNAMIC_HUFFMAN_BLOCK_COUNT;
                buildDynamicDecodeMaps();
                break;
            default:
                throw new CodecException();
        }
        blockStatistics[bsBaseIndex]++;
    }

    private void buildFixedDecodeMaps() {
        clearSymbol(literalLengthDecodeRoot);
        for (int index = 0; index < 144; index++) { // 144
            putSymbol(literalLengthDecodeRoot, 0b00110000 + index, 8, index);
        }
        for (int index = 144; index < 256; index++) { // 112
            putSymbol(literalLengthDecodeRoot, 0b110010000 - 144 + index, 9, index);
        }
        for (int index = 256; index < 280; index++) { // 24
            putSymbol(literalLengthDecodeRoot, index - 256, 7, index);
        }
        for (int index = 280; index < 286; index++) { // 6
            putSymbol(literalLengthDecodeRoot, 0b11000000 - 280 + index, 8, index);
        }
        clearSymbol(distanceDecodeRoot);
        for (int index = 0; index < 30; index++) {
            putSymbol(distanceDecodeRoot, index, 5, index);
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
        buildDecodeTree(REORDER.length, codeLengthDecodeRoot);
        readCodeLengths(literalLengthCodeCount);
        buildDecodeTree(literalLengthCodeCount, literalLengthDecodeRoot);
        readCodeLengths(distanceCodeCount);
        buildDecodeTree(distanceCodeCount, distanceDecodeRoot);
    }

    private void buildDecodeTree(int alphabetSize, @NotNull HuffmanNode root) {
        int maxLength = 0;
        for (int index = 0; index < alphabetSize; index++) {
            int codeLength = commonAlphabet[index];
            if (codeLength > maxLength) {
                maxLength = codeLength;
            }
        }
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
        clearSymbol(root);
        for (int symbol = 0; symbol < alphabetSize; symbol++) {
            int codeLength = commonAlphabet[symbol];
            if (codeLength == 0) {
                continue;
            }
            putSymbol(root, commonNextCode[codeLength]++, codeLength, symbol);
        }
    }

    private void clearSymbol(@Nullable HuffmanNode root) {
        if (root == null) {
            return;
        }
        root.symbol = -1;
        // useCount is not reset
        clearSymbol(root.left);
        clearSymbol(root.right);
    }

    private void putSymbol(@NotNull HuffmanNode root, int code, int codeLength, int symbol) {
        while (codeLength-- != 0) {
            if ((code & (1 << codeLength)) != 0) {
                if (root.left == null) {
                    root.left = new HuffmanNode();
                }
                root = root.left;
            } else {
                if (root.right == null) {
                    root.right = new HuffmanNode();
                }
                root = root.right;
            }
        }
        root.symbol = symbol;
    }

    private void readCodeLengths(int targetCount) throws IOException {
        int lastSymbol = -1;
        int index = 0;
        while (index < targetCount) {
            int thisSymbol = readSymbol(codeLengthDecodeRoot);
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
                    throw new CodecException("symbol = " + thisSymbol);
            }
        }
        if (index != targetCount) {
            throw new CodecException();
        }
    }

    private int readSymbol(HuffmanNode node) throws IOException {
        while (node != null) {
            if (node.symbol != -1) {
                node.useCount++;
                return node.symbol;
            }
            if (readBit()) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        throw new CodecException();
    }
}
