package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

@CodeHistory(date = "2026/2/16")
@ReferencePage(title = "JVMS12 The CONSTANT_Utf8_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.7")
public class ModifiedUTF8Charset extends Charset {

    public ModifiedUTF8Charset() {
        super("M-UTF-8", new String[] {"modified-utf-8", "MUTF8"});
    }

    @Override
    public boolean contains(Charset that) {
        return that instanceof ModifiedUTF8Charset;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new ModifiedUTF8CharsetDecoder(this);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new ModifiedUTF8CharsetEncoder(this);
    }
}
