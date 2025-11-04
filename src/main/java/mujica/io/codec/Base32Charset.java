package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

@CodeHistory(date = "2025/4/21")
public class Base32Charset extends Charset implements Base32Case {

    static final String NAME = "BASE32";
    static final String NAME_LOWER = "BASE32-LOWER";
    static final String NAME_UPPER = "BASE32-UPPER";

    private final int alphabetOffset;

    public Base32Charset(boolean upperCase) {
        super(upperCase ? NAME_UPPER : NAME_LOWER, upperCase ? null : new String[] {NAME});
        this.alphabetOffset = upperCase ? UPPER : LOWER;
    }

    public int alphabetOffset() {
        return alphabetOffset;
    }

    @Override
    public boolean contains(Charset cs) {
        return cs instanceof Base32Charset && this.alphabetOffset == ((Base32Charset) cs).alphabetOffset;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new Base32CharsetDecoder(this);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new Base32CharsetEncoder(this);
    }
}
