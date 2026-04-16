package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DirectSubclass;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

@CodeHistory(date = "2026/2/14")
@DirectSubclass({UriCharset.class, UriComponentCharset.class, UriRFC3986Charset.class, UriComponentRFC3986Charset.class})
abstract class PercentCharset extends Charset implements Base16Case {

    final int alphabetOffset;

    protected PercentCharset(boolean upperCase, @NotNull String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
        this.alphabetOffset = upperCase ? UPPER_CONSTANT : LOWER_CONSTANT;
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
