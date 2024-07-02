package indi.qsq.io.string;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Created on 2024/6/29.
 */
public class UTF16String extends IOString {

    @NotNull
    char[] chars;

    public UTF16String(@NotNull String string) {
        super(string);
        chars = string.toCharArray();
    }

    @Override
    public int charLength() {
        return chars.length;
    }

    @Override
    public void writeTo(@NotNull ContentHandler handler) throws SAXException {
        handler.characters(chars, 0, chars.length);
    }
}
