package mujica.io.charset;

import mujica.reflect.modifier.CodeHistory;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Iterator;

@CodeHistory(date = "2025/4/17")
public final class TowboatCharsetProvider extends CharsetProvider {

    @Override
    public Iterator<Charset> charsets() {
        return null;
    }

    @Override
    public Charset charsetForName(String charsetName) {
        switch (charsetName) {
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
            default:
                return null;
        }
    }
}
