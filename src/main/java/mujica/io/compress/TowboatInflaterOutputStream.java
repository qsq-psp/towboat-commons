package mujica.io.compress;

import io.netty.handler.codec.CodecException;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

@CodeHistory(date = "2025/10/20")
public class TowboatInflaterOutputStream extends FilterOutputStream {

    public TowboatInflaterOutputStream(@NotNull OutputStream out) {
        super(out);
    }

    private int buffer; // fill from MSB to LSB

    private int bitIndex = Integer.MAX_VALUE;

    @Override
    public void write(int octet) throws IOException {
        for (int shift = 0; shift < Byte.SIZE; shift++) {
            if ((octet & (1 << shift)) != 0) {
                buffer |= 1 << --bitIndex; // check it again
            }
        }
        assert bitIndex >= 0;
        update();
        assert bitIndex >= Byte.SIZE;
    }

    private final HuffmanCode keyCode = new HuffmanCode();

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

    private static final int STATE_START = 0;
    private static final int STATE_COMPRESSION_TYPE = 1;
    private static final int STATE_LENGTH = 2;
    private static final int STATE_NOT_LENGTH = 3;
    private static final int STATE_DATA = 4;
    private static final int STATE_DISTANCE = 5;
    private static final int STATE_LITERAL_LENGTH_CODE_COUNT = 6;
    private static final int STATE_DISTANCE_CODE_COUNT = 7;
    private static final int STATE_CODE_LENGTH_CODE_COUNT = 8;
    private static final int STATE_CODE_LENGTH_CODE = 0x200;
    private static final int STATE_LITERAL_LENGTH_CODE = 0x400;
    private static final int STATE_DISTANCE_CODE = 0x600;

    private static final int MASK_LAST_BLOCK = 0x1000;

    private int state = STATE_START;

    private void update() throws IOException {
        switch (0xfff & state) {
            case STATE_START:

        }
    }
}
