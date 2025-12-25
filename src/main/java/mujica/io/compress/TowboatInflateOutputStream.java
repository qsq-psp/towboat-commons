package mujica.io.compress;

import mujica.ds.of_boolean.BitSequence;
import mujica.ds.of_int.map.IntMap;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

@CodeHistory(date = "2025/10/20")
public class TowboatInflateOutputStream extends FilterOutputStream implements BitSequence {

    private int bitSize, bitPosition, buffer;

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

    @Override
    public void write(int octet) throws IOException {
        assert bitSize <= Integer.SIZE - Byte.SIZE;
        buffer |= octet << bitSize;
        bitSize += Byte.SIZE;
        update();
    }

    private int readBits(int bitCount) {
        assert bitCount <= bitSize;
        final int value = buffer & ((1 << bitCount) - 1);
        bitSize -= bitCount;
        buffer >>>= bitCount;
        return value;
    }

    private void align() {
        final int shift = bitSize & (Byte.SIZE - 1);
        if (shift != 0) {
            bitSize -= shift;
            buffer >>>= shift;
        }
    }

    private int tryGetSymbol(@NotNull IntMap decodeMap) throws IOException {
        int code = 0;
        int length = 0;
        final int limit = Math.min(bitSize, Integer.SIZE - Byte.SIZE);
        while (length < limit) {
            code <<= 1;
            if ((buffer & (1 << length)) != 0) {
                code |= 1 << Byte.SIZE;
            }
            length++;
            int symbol = decodeMap.getInt(code | length);
            if (symbol != 0) {
                return symbol;
            }
        }
        if (length > MAX_CODE_LENGTH) {
            throw new CompressAlgorithmException();
        }
        return 0;
    }

    private void resetAhead() {
        bitPosition = 0;
    }

    private boolean getSymbolAhead(@NotNull IntConsumer symbolOut) {
        return false;
    }

    private boolean getBitsAhead(@NotNull IntConsumer bitsOut) {
        return false;
    }

    public static final int MAX_RUN_BUFFER_DISTANCE = 1 << 16;

    @NotNull
    protected final RunBuffer runBuffer;

    @NotNull
    private final IntMap codeLengthDecodeMap, literalLengthDecodeMap, distanceDecodeMap;

    public TowboatInflateOutputStream(@NotNull OutputStream out, @NotNull RunBuffer runBuffer, @NotNull Supplier<IntMap> decodeMapSupplier) {
        super(out);
        this.runBuffer = runBuffer;
        this.codeLengthDecodeMap = decodeMapSupplier.get();
        this.literalLengthDecodeMap = decodeMapSupplier.get();
        this.distanceDecodeMap = decodeMapSupplier.get();
    }

    private static final int STATE_START = 0;
    private static final int STATE_COMPRESSION_TYPE = 1;
    private static final int STATE_LENGTH = 2;
    private static final int STATE_NOT_LENGTH = 3;
    private static final int STATE_LITERAL_LENGTH = 4;
    private static final int STATE_LENGTH_EXTRA = 5;
    private static final int STATE_DISTANCE = 6;
    private static final int STATE_DISTANCE_EXTRA = 7;
    private static final int STATE_LITERAL_LENGTH_CODE_COUNT = 8;
    private static final int STATE_DISTANCE_CODE_COUNT = 9;
    private static final int STATE_CODE_LENGTH_CODE_COUNT = 10;
    private static final int STATE_CODE_LENGTH_CODE                 = 0x200;
    private static final int STATE_LITERAL_LENGTH_CODE              = 0x400;
    private static final int STATE_LITERAL_LENGTH_CODE_EXTRA        = 0x600;
    private static final int STATE_DISTANCE_CODE                    = 0x800;
    private static final int STATE_DISTANCE_CODE_EXTRA              = 0xa00;

    private int state = STATE_START;

    private int remainingLength, copyDistance;

    private boolean isLastBlock;

    private void update() throws IOException {
        while (true) {
            switch (state) {
                case STATE_START:
                    if (isLastBlock) {
                        return;
                    }
                    if (bitSize >= 1) {
                        isLastBlock = readBits(1) != 0;
                        state = STATE_COMPRESSION_TYPE;
                        break;
                    } else {
                        return;
                    }
                case STATE_COMPRESSION_TYPE:
                    if (bitSize >= 2) {
                        int compressionType = readBits(2);
                        if (compressionType == 0) { // no compression
                            align();
                            state = STATE_LENGTH;
                        } else if (compressionType == 1) { // fixed huffman
                            buildFixedDecodeMaps();
                            state = STATE_LITERAL_LENGTH;
                        } else if (compressionType == 2) { // dynamic huffman
                            state = STATE_LITERAL_LENGTH_CODE_COUNT;
                        } else {
                            throw new CompressAlgorithmException();
                        }
                        break;
                    } else {
                        return;
                    }
                case STATE_LENGTH:
                    if (bitSize >= 16) {
                        remainingLength = readBits(16);
                        state = STATE_NOT_LENGTH;
                        break;
                    } else {
                        return;
                    }
                case STATE_NOT_LENGTH:
                    if (bitSize >= 16) {
                        int notLength = readBits(16);
                        if ((remainingLength ^ notLength) != 0xffff) {
                            throw new CompressAlgorithmException();
                        }
                        state = STATE_NOT_LENGTH;
                        break;
                    } else {
                        return;
                    }
                case STATE_LITERAL_LENGTH: {
                    int symbol = tryGetSymbol(literalLengthDecodeMap);
                    if (symbol == 0) {
                        return;
                    }
                    symbol = ~symbol;
                    if (symbol < 0x100) {
                        runBuffer.put((byte) symbol);
                        out.write(symbol);
                    } else if (symbol > 0x100) {
                        symbol -= 0x101;
                        remainingLength = ResidueInflateInputStream.LENGTH_BASE[symbol];
                        int extraBits = ResidueInflateInputStream.LENGTH_EXTRA_BITS[symbol];
                        if (extraBits != 0) {
                            remainingLength |= extraBits << (Integer.SIZE - Byte.SIZE);
                            state = STATE_LENGTH_EXTRA;
                        } else {
                            state = STATE_DISTANCE;
                        }
                    } else {
                        state = STATE_START;
                    }
                    break;
                }
                case STATE_LENGTH_EXTRA: {
                    int extraBits = remainingLength >>> (Integer.SIZE - Byte.SIZE);
                    if (bitSize >= extraBits) {
                        remainingLength = (0xffff & remainingLength) + readBits(extraBits);
                        state = STATE_DISTANCE;
                        break;
                    } else {
                        return;
                    }
                }
                case STATE_DISTANCE: {
                    int symbol = tryGetSymbol(distanceDecodeMap);
                    if (symbol == 0) {
                        return;
                    }
                    symbol = ~symbol;
                    copyDistance = ResidueInflateInputStream.DISTANCE_BASE[symbol];
                    int extraBits = ResidueInflateInputStream.DISTANCE_EXTRA_BITS[symbol];
                    if (extraBits != 0) {
                        copyDistance |= extraBits << (Integer.SIZE - Byte.SIZE);
                        state = STATE_DISTANCE_EXTRA;
                    } else {
                        writeCopy();
                        state = STATE_LITERAL_LENGTH;
                    }
                    break;
                }
                case STATE_DISTANCE_EXTRA: {
                    int extraBits = copyDistance >>> (Integer.SIZE - Byte.SIZE);
                    if (bitSize >= extraBits) {
                        copyDistance = (0xffff & copyDistance) + readBits(extraBits);
                        writeCopy();
                        state = STATE_LITERAL_LENGTH;
                        break;
                    } else {
                        return;
                    }
                }
                case STATE_LITERAL_LENGTH_CODE_COUNT:
                    if (bitSize >= 5) {
                        literalLengthDecodeMap.putInt(0, readBits(5) + 257);
                        state = STATE_DISTANCE_CODE_COUNT;
                        break;
                    } else {
                        return;
                    }
                case STATE_DISTANCE_CODE_COUNT:
                    if (bitSize >= 5) {
                        distanceDecodeMap.putInt(0, readBits(5) + 1);
                        state = STATE_CODE_LENGTH_CODE_COUNT;
                        break;
                    } else {
                        return;
                    }
                case STATE_CODE_LENGTH_CODE_COUNT:
                    if (bitSize >= 4) {
                        codeLengthDecodeMap.putInt(0, readBits(4) + 4);
                        state = STATE_CODE_LENGTH_CODE;
                        break;
                    } else {
                        return;
                    }
                default:
                    if (readCodes()) {
                        break;
                    } else {
                        return;
                    }
            }
        }
    }

    protected static final int MAX_SYMBOL = 286;

    protected final byte[] commonAlphabet = new byte[MAX_SYMBOL];

    protected static final int MAX_CODE_LENGTH = 15;

    protected final int[] commonLengthCounts = new int[MAX_CODE_LENGTH + 1];

    protected final int[] commonNextCode = new int[MAX_CODE_LENGTH + 1];

    private boolean readCodes() throws IOException {
        switch (state & ~0x1ff) {
            case STATE_CODE_LENGTH_CODE: {
                if (bitSize < 3) {
                    return false;
                }
                int codeLengthCodeCount = codeLengthDecodeMap.getInt(0);
                int index = state - STATE_CODE_LENGTH_CODE;
                assert index < codeLengthCodeCount;
                commonAlphabet[ResidueInflateInputStream.REORDER[index++]] = (byte) readBits(3);
                if (index < codeLengthCodeCount) {
                    state++;
                } else {
                    while (index < ResidueInflateInputStream.REORDER.length) {
                        commonAlphabet[ResidueInflateInputStream.REORDER[index++]] = 0;
                    }
                    buildDecodeMap(ResidueInflateInputStream.REORDER.length, codeLengthDecodeMap);
                    state = STATE_LITERAL_LENGTH_CODE;
                }
                break;
            }
            case STATE_LITERAL_LENGTH_CODE: {
                int symbol = tryGetSymbol(codeLengthDecodeMap);
                if (symbol == 0) {
                    return false;
                }
                int literalLengthCodeCount = literalLengthDecodeMap.getInt(0);
                int index = state - STATE_LITERAL_LENGTH_CODE;
                assert index < literalLengthCodeCount;
                index = fillCodeLength(index, ~symbol);
                if (index >= literalLengthCodeCount) {
                    buildDecodeMap(literalLengthCodeCount, literalLengthDecodeMap);
                    state = STATE_DISTANCE_CODE;
                }
                break;
            }
            case STATE_DISTANCE_CODE: {
                int symbol = tryGetSymbol(codeLengthDecodeMap);
                if (symbol == 0) {
                    return false;
                }
                int distanceCodeCount = distanceDecodeMap.getInt(0);
                int index = state - STATE_DISTANCE_CODE;
                assert index < distanceCodeCount;
                index = fillCodeLength(index, ~symbol);
                if (index >= distanceCodeCount) {
                    buildDecodeMap(distanceCodeCount, distanceDecodeMap);
                    state = STATE_LITERAL_LENGTH;
                }
                break;
            }
        }
        return true;
    }

    private void writeCopy() throws IOException {
        while (remainingLength > 0) {
            out.write(runBuffer.copy(copyDistance));
            remainingLength--;
        }
    }

    private void buildFixedDecodeMaps() {
        //
    }

    private int fillCodeLength(int index, int symbol) {
        switch (symbol) {
            case  0: case  1: case  2: case  3:
            case  4: case  5: case  6: case  7:
            case  8: case  9: case 10: case 11:
            case 12: case 13: case 14: case 15:
                commonAlphabet[index++] = (byte) symbol;
                break;
            case 16:
                //
        }
        return index;
    }

    private void buildDecodeMap(int alphabetSize, @NotNull IntMap decodeMap) {
        //
    }
}
