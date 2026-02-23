package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Iterator;
import java.util.List;

@CodeHistory(date = "2025/4/17")
public final class TowboatCharsetProvider extends CharsetProvider {

    public static final ModifiedUTF8Charset MUTF8 = new ModifiedUTF8Charset();

    @Override
    public Iterator<Charset> charsets() {
        return List.of(
                MUTF8,
                new Base16Charset(false),
                new Base16Charset(true),
                new Base32Charset(false),
                new Base32Charset(true),
                new UriCharset(true),
                new UriCharset(false),
                new UriComponentCharset(true),
                new UriComponentCharset(false),
                new UriRFC3986Charset(true),
                new UriRFC3986Charset(false),
                new UriComponentRFC3986Charset(true),
                new UriComponentRFC3986Charset(false)
        ).iterator();
    }

    @Override
    public Charset charsetForName(String charsetName) {
        switch (charsetName) {
            case "M-UTF-8":
            case "modified-utf-8":
            case "MUTF8":
                return MUTF8;
            case Base16Charset.NAME:
            case Base16Charset.NAME_LOWER:
                return new Base16Charset(false);
            case Base16Charset.NAME_UPPER:
                return new Base16Charset(true);
            case Base32Charset.NAME:
            case Base32Charset.NAME_LOWER:
                return new Base32Charset(false);
            case Base32Charset.NAME_UPPER:
                return new Base32Charset(true);
            case UriCharset.NAME:
            case UriCharset.NAME_UPPER:
                return new UriCharset(true);
            case UriCharset.NAME_LOWER:
                return new UriCharset(false);
            case UriComponentCharset.NAME:
            case UriComponentCharset.NAME_UPPER:
                return new UriComponentCharset(true);
            case UriComponentCharset.NAME_LOWER:
                return new UriComponentCharset(false);
            case UriRFC3986Charset.NAME:
            case UriRFC3986Charset.NAME_UPPER:
                return new UriRFC3986Charset(true);
            case UriRFC3986Charset.NAME_LOWER:
                return new UriRFC3986Charset(false);
            case UriComponentRFC3986Charset.NAME:
            case UriComponentRFC3986Charset.NAME_UPPER:
                return new UriComponentRFC3986Charset(true);
            case UriComponentRFC3986Charset.NAME_LOWER:
                return new UriComponentRFC3986Charset(false);
            default:
                return null;
        }
    }
}
