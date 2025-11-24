package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.ds.of_boolean.BitSequence;
import mujica.ds.of_int.map.CompatibleIntMap;
import mujica.ds.of_int.map.IntMap;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Supplier;

@CodeHistory(date = "2025/11/14")
public class Inflate64InputStream extends FilterInputStream implements BitSequence {

    private int bitSize, buffer;

    @Override
    public int bitLength() {
        return bitSize;
    }

    @Override
    public boolean getBit(int index) {
        if (index < 0 || index >= bitSize) {
            throw new IndexOutOfBoundsException();
        }
        return (buffer & (1 << bitSize)) != 0;
    }

    protected boolean readBit() throws IOException {
        if (bitSize == 0) {
            buffer = in.read();
            if (buffer == -1) {
                throw new EOFException();
            }
            bitSize = Byte.SIZE;
        }
        final boolean bit = (buffer & 1) != 0;
        bitSize--;
        buffer >>>= 1;
        return bit;
    }

    private int readBits(int bitCount) throws IOException {
        assert bitCount <= Integer.SIZE - Byte.SIZE;
        while (bitSize < bitCount) {
            int value = in.read();
            if (value == -1) {
                throw new EOFException();
            }
            buffer |= value << bitSize;
            bitSize += Byte.SIZE;
        }
        final int value = buffer & ((1 << bitCount) - 1);
        bitSize -= bitCount;
        buffer >>>= bitCount;
        return value;
    }

    private int readTrailingBit() {
        if (bitSize > 0) {
            boolean bit = (buffer & 1) != 0;
            bitSize--;
            buffer >>>= 1;
            return bit ? 1 : 0;
        } else {
            return -1;
        }
    }

    private int readTrailingByte() throws IOException {
        if (bitSize >= Byte.SIZE) {
            int alignShift = bitSize & 0x7;
            if (alignShift != 0) {
                bitSize -= alignShift;
                buffer >>>= alignShift;
            }
            int value = 0xff & buffer;
            bitSize -= Byte.SIZE;
            buffer >>>= Byte.SIZE;
            return value;
        } else {
            bitSize = 0;
            buffer = 0;
            return in.read();
        }
    }

    private int readNoCompressionLength() throws IOException {
        int length = readTrailingByte() | (readTrailingByte() << Byte.SIZE);
        if (length < 0) {
            throw new EOFException();
        }
        int notLength = readTrailingByte() | (readTrailingByte() << Byte.SIZE);
        if (notLength < 0) {
            throw new EOFException();
        }
        if ((length ^ notLength) != 0xffff) {
            throw new CompressAlgorithmException();
        }
        return length;
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
                if (symbol < 0) {
                    symbol++;
                }
                return symbol;
            }
        }
    }

    @NotNull
    protected final LookBackMemory runBuffer;

    private static final int MAX_SYMBOL = 286;

    private final byte[] commonAlphabet = new byte[MAX_SYMBOL];

    private static final int MAX_CODE_LENGTH = 15;

    private final int[] commonLengthCounts = new int[MAX_CODE_LENGTH + 1];

    private final int[] commonNextCode = new int[MAX_CODE_LENGTH + 1];

    private void buildDecodeMap(int alphabetSize, @NotNull IntMap decodeMap) {
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
        decodeMap.clear();
        for (int symbol = 0; symbol < alphabetSize; symbol++) {
            int codeLength = commonAlphabet[symbol];
            if (codeLength == 0) {
                continue;
            }
            decodeMap.putInt(commonNextCode[codeLength]++ | (1 << codeLength), symbol == 0 ? -1 : symbol);
        }
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
                    commonAlphabet[index++] = (byte) thisSymbol;
                    lastSymbol = thisSymbol;
                    break;
                case 16:
                    if (lastSymbol == -1) {
                        throw new CodecException();
                    }
                    for (int times = readBits(2) + 3; times > 0; times--) {
                        commonAlphabet[index++] = (byte) lastSymbol;
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

    public static final int MAX_RUN_BUFFER_DISTANCE = 1 << 16;

    @NotNull
    private final IntMap codeLengthDecodeMap, literalLengthDecodeMap, distanceDecodeMap;

    public Inflate64InputStream(@NotNull InputStream in, @NotNull LookBackMemory runBuffer, @NotNull Supplier<IntMap> decodeMapSupplier) {
        super(in);
        this.runBuffer = runBuffer;
        this.codeLengthDecodeMap = decodeMapSupplier.get();
        this.literalLengthDecodeMap = decodeMapSupplier.get();
        this.distanceDecodeMap = decodeMapSupplier.get();
    }

    public Inflate64InputStream(@NotNull InputStream in) {
        this(in, new CyclicLookBackMemory(MAX_RUN_BUFFER_DISTANCE), CompatibleIntMap::new);
    }

    private static final byte[] REORDER = {
            16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15
    }; // 19

    private void buildDynamicDecodeMaps() throws IOException {
        final int literalLengthCodeCount = readBits(5) + 257;
        final int distanceCodeCount = readBits(5) + 1;
        final int codeLengthCodeCount = readBits(4) + 4;
        for (int index = 0; index < codeLengthCodeCount; index++) {
            commonAlphabet[REORDER[index]] = (byte) readBits(3);
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

    private void buildFixedDecodeMaps() {
        literalLengthDecodeMap.clear();
        for (int index = 0; index < 144; index++) {
            literalLengthDecodeMap.putInt((0b00110000 + index) | (1 << 8), index == 0 ? -1 : index);
        }
        for (int index = 144; index < 256; index++) {
            literalLengthDecodeMap.putInt((0b110010000 - 144 + index) | (1 << 9), index);
        }
        for (int index = 256; index < 280; index++) {
            literalLengthDecodeMap.putInt((index - 256) | (1 << 7), index);
        }
        for (int index = 280; index < 286; index++) {
            literalLengthDecodeMap.putInt((0b11000000 - 280 + index) | (1 << 8), index);
        }
        distanceDecodeMap.clear();
        for (int index = 0; index < 32; index++) {
            distanceDecodeMap.putInt((index << Byte.SIZE) | 5, index == 0 ? -1 : index);
        }
    }

    private static final int STATE_FREE                           = 0;
    private static final int STATE_LAST_BLOCK_FREE                = 1;
    private static final int STATE_NO_COMPRESSION                 = 2;
    private static final int STATE_LAST_BLOCK_NO_COMPRESSION      = 3;
    private static final int STATE_FIXED_HUFFMAN                  = 4;
    private static final int STATE_LAST_BLOCK_FIXED_HUFFMAN       = 5;
    private static final int STATE_DYNAMIC_HUFFMAN                = 6;
    private static final int STATE_LAST_BLOCK_DYNAMIC_HUFFMAN     = 7;
    private static final int STATE_TRAILING_BITS                  = 8;
    private static final int STATE_TRAILING_BYTES                 = 9;

    private int state;

    private int copyDistance, remainingLength;

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

    private static final byte[] LENGTH_EXTRA_BITS = {
            0, 0, 0, 0, 0, 0, 0, 0,  1, 1,
            1, 1, 2, 2, 2, 2, 3, 3,  3, 3,
            4, 4, 4, 4, 5, 5, 5, 5, 16
    }; // 29

    private static final short[] LENGTH_BASE = {
            3,  4,  5,   6,   7,   8,   9,  10,  11, 13,
            15, 17, 19,  23,  27,  31,  35,  43,  51, 59,
            67, 83, 99, 115, 131, 163, 195, 227, 258
    }; // 29

    private static final byte[] DISTANCE_EXTRA_BITS = {
            0,  0,  0,  0,  1,  1,  2,  2,  3,  3,
            4,  4,  5,  5,  6,  6,  7,  7,  8,  8,
            9,  9, 10, 10, 11, 11, 12, 12, 13, 13,
            14, 14
    }; // 32

    private static final int[] DISTANCE_BASE = {
            1,     2,    3,    4,    5,    7,    9,    13,    17,    25,
            33,    49,   65,   97,  129,  193,  257,   385,   513,   769,
            1025,  1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577,
            32769, 49153
    }; // 32

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
                        if (symbol > 0x100) {
                            symbol -= 0x101;
                            remainingLength = LENGTH_BASE[symbol];
                            remainingLength += readBits(LENGTH_EXTRA_BITS[symbol]);
                            symbol = readSymbol(distanceDecodeMap);
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

    @Override
    public long skip(long n) throws IOException {
        long m = 0L;
        while (m < n) {
            if (in.read() == -1) {
                break;
            }
            m++;
        }
        return m;
    }

    @Override
    public int available() {
        return remainingLength;
    }

    @Override
    public void close() throws IOException {
        bitSize = 0;
        state = STATE_LAST_BLOCK_FREE;
        remainingLength = 0;
        in.close();
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void mark(int readLimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
