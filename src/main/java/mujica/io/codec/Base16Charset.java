package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

@CodeHistory(date = "2025/4/19")
public class Base16Charset extends Charset implements Base16Case {

    static final String NAME = "BASE16";
    static final String NAME_LOWER = "BASE16-LOWER";
    static final String NAME_UPPER = "BASE16-UPPER";

    private final int alphabetOffset;

    public Base16Charset(boolean upperCase) {
        super(upperCase ? NAME_UPPER : NAME_LOWER, upperCase ? null : new String[] {NAME});
        this.alphabetOffset = upperCase ? UPPER : LOWER;
    }

    public int alphabetOffset() {
        return alphabetOffset;
    }

    @Override
    public boolean contains(Charset cs) {
        return cs instanceof Base16Charset && this.alphabetOffset == ((Base16Charset) cs).alphabetOffset;
    }

    @NotNull
    @Override
    public CharsetDecoder newDecoder() {
        return new Base16CharsetDecoder(this);
    }

    @NotNull
    @Override
    public CharsetEncoder newEncoder() {
        return new Base16CharsetEncoder(this);
    }
}
