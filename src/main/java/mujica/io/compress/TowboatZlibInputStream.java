package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.ds.of_boolean.BitSequence;
import mujica.ds.of_int.map.IntMap;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.IntFunction;
import java.util.function.Supplier;

@CodeHistory(date = "2025/10/27", name = "TowboatZlibInputStream")
@ReferencePage(title = "ZLIB Compressed Data Format Specification version 3.3", href = "https://www.rfc-editor.org/rfc/rfc1950.html")
public class TowboatZlibInputStream extends FilterInputStream implements BitSequence {

    private int bitIndex; // from 8 to 0

    private int readNoCompressionLength() throws IOException {
        bitIndex = 0; // align
        int length = in.read() | (in.read() << Byte.SIZE);
        if (length < 0) {
            throw new EOFException();
        }
        int notLength = in.read() | (in.read() << Byte.SIZE);
        if (notLength < 0) {
            throw new EOFException();
        }
        if ((length ^ notLength) != 0xffff) {
            throw new CodecException();
        }
        return length;
    }

    @Override
    public int bitLength() {
        return bitIndex;
    }

    private int buffer; // contains bitIndex bits

    /**
     * trailing bits can be read after EOF, or after EOF and close
     * steganography information contained inside
     */
    @Override
    public boolean getBit(int index) {
        if (index < 0 || index >= bitIndex) {
            throw new IndexOutOfBoundsException();
        }
        return (buffer & (0x100 >> (bitIndex - index))) != 0;
    }

    private boolean readBit() throws IOException {
        if (bitIndex == 0) {
            buffer = in.read();
            if (buffer == -1) {
                throw new EOFException();
            }
            bitIndex = Byte.SIZE;
        }
        return (buffer & (0x100 >> bitIndex--)) != 0;
    }

    private int readBits(int bitCount) throws IOException {
        int value = 0;
        for (int shift = 0; shift < bitCount; shift++) {
            if (readBit()) {
                value |= 1 << shift;
            }
        }
        return value;
    }

    private int readCode(@NotNull IntMap decodeMap) throws IOException {
        int length = 0;
        int value = 0;
        while (true) {
            if (length == Integer.SIZE - Byte.SIZE) {
                throw new CodecException("length = " + length + ", value = " + Integer.toBinaryString(value >>> Byte.SIZE));
            }
            length++;
            value <<= 1;
            if (readBit()) {
                value |= 1 << Byte.SIZE;
            }
            int code = decodeMap.getInt(value | length);
            if (code != 0) {
                return code;
            }
        }
    }

    private static final int MAX_ALPHABET_SIZE = 287;

    private final int[] commonAlphabet = new int[MAX_ALPHABET_SIZE];

    private void buildDecodeMap(int alphabetSize, @NotNull IntMap decodeMap) {
        int maxLength = 0;
        for (int index = 1; index <= alphabetSize; index++) {
            int codeLength = commonAlphabet[index];
            if (codeLength > maxLength) {
                maxLength = codeLength;
            }
        }
        final int[] lengthCount = new int[maxLength + 1];
        for (int index = 1; index <= alphabetSize; index++) {
            lengthCount[commonAlphabet[index]]++;
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
        for (int index = 1; index <= alphabetSize; index++) {
            int codeLength = commonAlphabet[index];
            if (codeLength == 0) {
                continue;
            }
            commonAlphabet[index] = (nextCode[codeLength]++ << Byte.SIZE) | codeLength;
        }
        decodeMap.clear();
        for (int index = 1; index <= alphabetSize; index++) {
            int code = commonAlphabet[index];
            if (code == 0) { // code of length 0 is never used
                continue;
            }
            if (decodeMap.putInt(code, index) != 0) {
                throw new CodecException(); // code should not overlap
            }
        }
    }

    private final IntMap codeLengthDecodeMap;

    private void readCodeLengths(int targetCount) throws IOException {
        int lastCode = -1;
        int index = 0;
        while (index < targetCount) {
            int thisCode = readCode(codeLengthDecodeMap);
            switch (thisCode) {
                case  1: case  2: case  3: case  4:
                case  5: case  6: case  7: case  8:
                case  9: case 10: case 11: case 12:
                case 13: case 14: case 15: case 16:
                    thisCode--;
                    commonAlphabet[++index] = thisCode;
                    lastCode = thisCode;
                    break;
                case 17:
                    if (lastCode == -1) {
                        throw new CodecException();
                    }
                    for (int times = readBits(2) + 3; times > 0; times--) {
                        commonAlphabet[++index] = lastCode;
                    }
                    break;
                case 18:
                    for (int times = readBits(3) + 3; times > 0; times--) {
                        commonAlphabet[++index] = 0;
                    }
                    lastCode = 0;
                    break;
                case 19:
                    for (int times = readBits(7) + 11; times > 0; times--) {
                        commonAlphabet[++index] = 0;
                    }
                    lastCode = 0;
                    break;
                default:
                    throw new CodecException("code = " + thisCode);
            }
        }
        if (index != targetCount) {
            throw new CodecException();
        }
    }

    private final IntMap literalLengthDecodeMap;

    private final IntMap distanceDecodeMap;

    private void buildFixedDecodeMaps() {
        literalLengthDecodeMap.clear();
        for (int index = 0; index < 144; index++) {
            literalLengthDecodeMap.putInt(((0b00110000 + index) << Byte.SIZE) | 8, index + 1);
        }
        for (int index = 144; index < 256; index++) {
            literalLengthDecodeMap.putInt(((0b110010000 - 144 + index) << Byte.SIZE) | 9, index + 1);
        }
        for (int index = 256; index < 280; index++) {
            literalLengthDecodeMap.putInt(((index - 256) << Byte.SIZE) | 7, index + 1);
        }
        for (int index = 280; index < 286; index++) {
            literalLengthDecodeMap.putInt(((0b11000000 - 280 + index) << Byte.SIZE) | 8, index + 1);
        }
        distanceDecodeMap.clear();
        for (int index = 0; index < 30; index++) {
            distanceDecodeMap.putInt((index << Byte.SIZE) | 5, index + 1);
        }
    }

    static final int[] REORDER = {
            17, 18, 19, 1, 9, 8, 10, 7, 11, 6, 12, 5, 13, 4, 14, 3, 15, 2, 16
    }; // 19

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
        buildDecodeMap(REORDER.length, codeLengthDecodeMap);
        readCodeLengths(literalLengthCodeCount);
        buildDecodeMap(literalLengthCodeCount, literalLengthDecodeMap);
        readCodeLengths(distanceCodeCount);
        buildDecodeMap(distanceCodeCount, distanceDecodeMap);
    }

    private static final int STATE_NO_COMPRESSION = 0;
    private static final int STATE_FIXED_HUFFMAN = 1;
    private static final int STATE_DYNAMIC_HUFFMAN = 2;
    private static final int STATE_START = 3;
    private static final int MASK_LAST_BLOCK = 4;

    private int state = STATE_START;

    private int copyDistance, remainingLength;

    private void readBlock() throws IOException {
        final boolean lastBlock = readBit();
        switch (readBits(2)) {
            case STATE_NO_COMPRESSION:
                state = STATE_NO_COMPRESSION;
                remainingLength = readNoCompressionLength();
                break;
            case STATE_FIXED_HUFFMAN:
                state = STATE_FIXED_HUFFMAN;
                buildFixedDecodeMaps();
                break;
            case STATE_DYNAMIC_HUFFMAN:
                state = STATE_DYNAMIC_HUFFMAN;
                buildDynamicDecodeMaps();
                break;
            default:
                throw new CodecException();
        }
        if (lastBlock) {
            state |= MASK_LAST_BLOCK;
        }
        // System.out.println("TowboatZlibInputStream.readBlock " + state);
    }

    @NotNull
    private final LookBackMemory memory;

    public TowboatZlibInputStream(@NotNull InputStream in, @NotNull Supplier<IntMap> decodeMapSupplier, @NotNull IntFunction<LookBackMemory> runBufferAllocator) throws IOException {
        super(in);
        codeLengthDecodeMap = decodeMapSupplier.get();
        literalLengthDecodeMap = decodeMapSupplier.get();
        distanceDecodeMap = decodeMapSupplier.get();
        final int compressionMethod = readBits(4);
        if (compressionMethod != 8) {
            throw new CodecException("compressionMethod = " + compressionMethod);
        }
        final int compressionInfo = readBits(4);
        if (compressionInfo >= 8) {
            throw new CodecException("compressionInfo = " + compressionInfo);
        }
        memory = runBufferAllocator.apply(1 << (compressionInfo + 8));
        final int headerCheck = readBits(5);
        final boolean presetDictionary = readBit();
        if (presetDictionary) {
            throw new CodecException("presetDictionary");
        }
        final int compressionLevel = readBits(2);
        if (((compressionInfo << 12) | (compressionMethod << 8) | (compressionLevel << 6) | headerCheck) % 31 != 0) {
            throw new CodecException("headerCheck");
        }
        readBlock();
    }

    /*
    private static final int[] LENGTH_EXTRA_BITS = {
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1,
            1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            4, 4, 4, 4, 5, 5, 5, 5, 0
    }; // 29

    private static final int[] LENGTH_BASE = new int[LENGTH_EXTRA_BITS.length];

    private static final int[] DISTANCE_EXTRA_BITS = {
            0, 0, 0, 0, 1, 1, 2, 2, 3, 3,
            4, 4, 5, 5, 6, 6, 7, 7, 8, 8,
            9, 9, 10, 10, 11, 11, 12, 12, 13, 13
    }; // 30

    private static final int[] DISTANCE_BASE = new int[DISTANCE_EXTRA_BITS.length];

    static {
        int length = LENGTH_EXTRA_BITS.length;
        int value = 3;
        for (int index = 0; index < length; index++) {
            LENGTH_BASE[index] = value;
            value += 1 << LENGTH_EXTRA_BITS[index];
        }
        assert value == 259;
        length = DISTANCE_EXTRA_BITS.length;
        value = 1;
        for (int index = 0; index < length; index++) {
            DISTANCE_BASE[index] = value;
            value += 1 << DISTANCE_EXTRA_BITS[index];
        }
        assert value == 32769;
    }
    //*/

    private static final int[] LENGTH_EXTRA_BITS = {
            0, 0, 0, 0, 0, 0, 0, 0, 1, 1,
            1, 1, 2, 2, 2, 2, 3, 3, 3, 3,
            4, 4, 4, 4, 5, 5, 5, 5, 0
    };

    private static final int[] LENGTH_BASE = {
            3, 4, 5, 6, 7, 8, 9, 10, 11, 13,
            15, 17, 19, 23, 27, 31, 35, 43, 51, 59,
            67, 83, 99, 115, 131, 163, 195, 227, 258
    };

    private static final int[] DISTANCE_EXTRA_BITS = {
            0, 0, 0, 0, 1, 1, 2, 2, 3, 3,
            4, 4, 5, 5, 6, 6, 7, 7, 8, 8,
            9, 9, 10, 10, 11, 11, 12, 12, 13, 13
    };

    private static final int[] DISTANCE_BASE = {
            1, 2, 3, 4, 5, 7, 9, 13, 17, 25,
            33, 49, 65, 97, 129, 193, 257, 385, 513, 769,
            1025, 1537, 2049, 3073, 4097, 6145, 8193, 12289, 16385, 24577
    };

    @Override
    public int read() throws IOException {
        while (true) {
            switch (state) {
                case STATE_NO_COMPRESSION:
                case MASK_LAST_BLOCK | STATE_NO_COMPRESSION:
                    if (remainingLength > 0) {
                        int data = in.read();
                        if (data == -1) {
                            throw new EOFException();
                        }
                        remainingLength--;
                        memory.write(data);
                        return data;
                    } else {
                        state = (state & MASK_LAST_BLOCK) | STATE_START;
                    }
                    break;
                case STATE_FIXED_HUFFMAN:
                case MASK_LAST_BLOCK | STATE_FIXED_HUFFMAN:
                case STATE_DYNAMIC_HUFFMAN:
                case MASK_LAST_BLOCK | STATE_DYNAMIC_HUFFMAN:
                    if (remainingLength > 0) {
                        remainingLength--;
                        return 0xff & memory.copyAndWrite(copyDistance);
                    } else {
                        int code = readCode(literalLengthDecodeMap);
                        if (code > 0x101) {
                            code -= 0x102;
                            remainingLength = LENGTH_BASE[code];
                            remainingLength += readBits(LENGTH_EXTRA_BITS[code]);
                            code = readCode(distanceDecodeMap) - 1;
                            copyDistance = DISTANCE_BASE[code];
                            copyDistance += readBits(DISTANCE_EXTRA_BITS[code]);
                        } else if (code < 0x101) {
                            code--;
                            memory.write(code);
                            return code;
                        } else {
                            state = (state & MASK_LAST_BLOCK) | STATE_START;
                        }
                    }
                    break;
                case STATE_START:
                    readBlock();
                    break;
                case MASK_LAST_BLOCK | STATE_START:
                    return -1;
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
}
