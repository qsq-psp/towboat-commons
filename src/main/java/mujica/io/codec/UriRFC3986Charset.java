package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.CharsetEncoder;

@CodeHistory(date = "2026/2/15")
@ReferencePage(title = "Encoding for RFC3986", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURI#encoding_for_rfc3986")
public class UriRFC3986Charset extends PercentCharset {

    static final String NAME = "URI-RFC3986"; // the default is upper case
    static final String NAME_LOWER = "URI-RFC3986-LOWER";
    static final String NAME_UPPER = "URI-RFC3986-UPPER";

    public UriRFC3986Charset(boolean upperCase) {
        super(upperCase, upperCase ? NAME_UPPER : NAME_LOWER, upperCase ? null : new String[] {NAME});
    }

    @Override
    @NotNull
    public CharsetEncoder newEncoder() {
        return new UriRFC3986CharsetEncoder(this);
    }
}
