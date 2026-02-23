package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.CharsetEncoder;

@CodeHistory(date = "2026/2/13")
@ReferencePage(title = "encodeURI()", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURI")
public class UriCharset extends PercentCharset {

    static final String NAME = "URI"; // the default is upper case
    static final String NAME_LOWER = "URI-LOWER";
    static final String NAME_UPPER = "URI-UPPER";

    public UriCharset(boolean upperCase) {
        super(upperCase, upperCase ? NAME_UPPER : NAME_LOWER, upperCase ? null : new String[] {NAME});
    }

    @Override
    @NotNull
    public CharsetEncoder newEncoder() {
        return new UriCharsetEncoder(this);
    }
}
