package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@CodeHistory(date = "2025/10/19", name = "TowboatInflaterInputStream")
public class TowboatInflaterInputStream extends FilterInputStream {

    private int bitIndex = Byte.SIZE;

    private int readLength() throws IOException {
        bitIndex = Byte.SIZE; // align
        int length = in.read() | (in.read() << 8);
        if (length < 0) {
            throw new EOFException();
        }
        int notLength = in.read() | (in.read() << 8);
        if (notLength < 0) {
            throw new EOFException();
        }
        if ((length ^ notLength) != 0xffff) {
            throw new CodecException();
        }
        return length;
    }

    private int byteValue;

    private boolean readBit() throws IOException {
        if (bitIndex == Byte.SIZE) {
            byteValue = in.read();
            if (byteValue == -1) {
                throw new EOFException();
            }
            bitIndex = 0;
        }
        return (byteValue & (1 << bitIndex++)) != 0;
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

    private final HuffmanCode keyCode = new HuffmanCode();

    private int readCode(@NotNull HashMap<HuffmanCode, Integer> decodeMap) throws IOException {
        keyCode.reset();
        while (true) {
            keyCode.addLast(readBit());
            Integer value = decodeMap.get(keyCode);
            if (value != null) {
                return value;
            }
        }
    }

    private static final int MAX_ALPHABET_SIZE = 286;

    private final HuffmanCode[] commonAlphabet = new HuffmanCode[MAX_ALPHABET_SIZE];
    {
        for (int index = 0; index < MAX_ALPHABET_SIZE; index++) {
            commonAlphabet[index] = new HuffmanCode();
        }
    }

    private void buildDecodeMap(int alphabetSize, @NotNull HashMap<HuffmanCode, Integer> decodeMap) {
        decodeMap.clear();
        for (int index = 0; index < alphabetSize; index++) {
            HuffmanCode code = commonAlphabet[index];
            if (code.length == 0) {
                continue;
            }
            if (decodeMap.put(code.duplicate(), index) != null) {
                throw new CodecException();
            }
        }
    }

    private final HashMap<HuffmanCode, Integer> codeLengthDecodeMap = new HashMap<>();

    private void readCodeLengths(int targetCount) throws IOException {
        int lastCode = -1;
        int index = 0;
        while (index < targetCount) {
            int thisCode = readCode(codeLengthDecodeMap);
            if (thisCode <= 15) {
                commonAlphabet[index++].length = thisCode;
                lastCode = thisCode;
            } else if (thisCode == 16) {
                if (lastCode == -1) {
                    throw new CodecException();
                }
                for (int times = readBits(2) + 3; times > 0; times--) {
                    commonAlphabet[index++].length = lastCode;
                }
            } else if (thisCode == 17) {
                for (int times = readBits(3) + 3; times > 0; times--) {
                    commonAlphabet[index++].length = 0;
                }
                lastCode = 0;
            } else if (thisCode == 18) {
                for (int times = readBits(7) + 11; times > 0; times--) {
                    commonAlphabet[index++].length = 0;
                }
                lastCode = 0;
            } else {
                throw new CodecException();
            }
        }
        if (index != targetCount) {
            throw new CodecException();
        }
    }

    private final HashMap<HuffmanCode, Integer> literalLengthDecodeMap = new HashMap<>();

    private final HashMap<HuffmanCode, Integer> distanceDecodeMap = new HashMap<>();

    private void buildFixedDecodeMaps() {
        literalLengthDecodeMap.clear();
        for (int index = 0; index < 144; index++) {
            literalLengthDecodeMap.put(new HuffmanCode(8, 0b00110000 + index), index);
        }
        for (int index = 144; index < 256; index++) {
            literalLengthDecodeMap.put(new HuffmanCode(9, 0b110010000 - 144 + index), index);
        }
        for (int index = 256; index < 280; index++) {
            literalLengthDecodeMap.put(new HuffmanCode(7, index - 256), index);
        }
        for (int index = 280; index < 286; index++) {
            literalLengthDecodeMap.put(new HuffmanCode(8, 0b11000000 - 280 + index), index);
        }
        distanceDecodeMap.clear();
        for (int index = 0; index < 32; index++) {
            distanceDecodeMap.put(new HuffmanCode(5, index), index);
        }
    }

    static final int[] REORDER = {
            16, 17, 18, 0, 8, 7, 9, 6, 10, 5, 11, 4, 12, 3, 13, 2, 14, 1, 15
    }; // 19

    private void buildDynamicDecodeMaps() throws IOException {
        final int literalLengthCodeCount = readBits(5) + 257;
        final int distanceCodeCount = readBits(5) + 1;
        final int codeLengthCodeCount = readBits(4) + 4;
        for (int index = 0; index < codeLengthCodeCount; index++) {
            commonAlphabet[REORDER[index]].length = readBits(3);
        }
        for (int index = codeLengthCodeCount; index < REORDER.length; index++) {
            commonAlphabet[REORDER[index]].length = 0;
        }
        HuffmanCode.canonicalConstruct(commonAlphabet, REORDER.length);
        buildDecodeMap(REORDER.length, codeLengthDecodeMap);
        readCodeLengths(literalLengthCodeCount);
        HuffmanCode.canonicalConstruct(commonAlphabet, literalLengthCodeCount);
        buildDecodeMap(literalLengthCodeCount, literalLengthDecodeMap);
        readCodeLengths(distanceCodeCount);
        HuffmanCode.canonicalConstruct(commonAlphabet, distanceCodeCount);
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
                remainingLength = readLength();
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
        // System.out.println("TowboatInflaterInputStream.readBlock " + state);
    }

    public TowboatInflaterInputStream(@NotNull InputStream in) throws IOException {
        super(in);
        readBlock();
    }

    private static final int MEMORY_CAPACITY = 1 << 15;

    private static final int MEMORY_MODULE = MEMORY_CAPACITY + 1;

    private final byte[] memory = new byte[MEMORY_MODULE];

    private int memoryHead, memoryTail;

    private int directData() throws IOException {
        int data = in.read();
        if (data == -1) {
            throw new EOFException();
        }
        remainingLength--;
        memory[memoryTail++] = (byte) data;
        if (memoryTail == MEMORY_MODULE) {
            memoryTail = 0;
        }
        if (memoryTail == memoryHead && ++memoryHead == MEMORY_MODULE) {
            memoryHead = 0;
        }
        return data;
    }

    private int copyData() {
        int index = memoryTail - copyDistance;
        if (index < 0) {
            index += MEMORY_MODULE;
        }
        final byte data = memory[index];
        remainingLength--;
        memory[memoryTail++] = data;
        if (memoryTail == MEMORY_MODULE) {
            memoryTail = 0;
        }
        if (memoryTail == memoryHead && ++memoryHead == MEMORY_MODULE) {
            memoryHead = 0;
        }
        return 0xff & data;
    }

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

    private int readCode() throws IOException {
        final int literalLengthCode = readCode(literalLengthDecodeMap);
        if (literalLengthCode > 0x100) {
            int tableIndex = literalLengthCode - 0x101;
            remainingLength = LENGTH_BASE[tableIndex];
            remainingLength += readBits(LENGTH_EXTRA_BITS[tableIndex]);
            tableIndex = readCode(distanceDecodeMap);
            copyDistance = DISTANCE_BASE[tableIndex];
            copyDistance += readBits(DISTANCE_EXTRA_BITS[tableIndex]);
        } else if (literalLengthCode < 0x100) {
            memory[memoryTail++] = (byte) literalLengthCode;
            if (memoryTail == MEMORY_MODULE) {
                memoryTail = 0;
            }
            if (memoryTail == memoryHead && ++memoryHead == MEMORY_MODULE) {
                memoryHead = 0;
            }
        }
        return literalLengthCode;
    }

    @Override
    public int read() throws IOException {
        while (true) {
            switch (state) {
                case STATE_NO_COMPRESSION:
                case MASK_LAST_BLOCK | STATE_NO_COMPRESSION:
                    if (remainingLength > 0) {
                        return directData();
                    } else {
                        state = (state & MASK_LAST_BLOCK) | STATE_START;
                    }
                    break;
                case STATE_FIXED_HUFFMAN:
                case MASK_LAST_BLOCK | STATE_FIXED_HUFFMAN:
                case STATE_DYNAMIC_HUFFMAN:
                case MASK_LAST_BLOCK | STATE_DYNAMIC_HUFFMAN:
                    if (remainingLength > 0) {
                        return copyData();
                    } else {
                        int code = readCode();
                        if (code < 0x100) {
                            return code;
                        } else if (code == 0x100) {
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
