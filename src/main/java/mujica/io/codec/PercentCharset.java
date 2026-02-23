package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

@CodeHistory(date = "2026/2/14")
abstract class PercentCharset extends Charset implements Base16Case {

    final int alphabetOffset;

    protected PercentCharset(boolean upperCase, @NotNull String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
        this.alphabetOffset = upperCase ? UPPER : LOWER;
    }

    public int getAlphabetOffset() {
        return alphabetOffset;
    }

    @Override
    public boolean contains(Charset that) {
        return this.getClass() == that.getClass() && this.alphabetOffset == ((PercentCharset) that).alphabetOffset;
    }

    @Override
    @NotNull
    public CharsetDecoder newDecoder() {
        return new PercentDecoder(this);
    }
}
