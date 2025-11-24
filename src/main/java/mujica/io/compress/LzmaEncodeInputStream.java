package mujica.io.compress;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.InputStream;

@CodeHistory(date = "2025/11/19")
@ReferencePage(title = "LZMA specification (DRAFT version)", href = "https://www.7-zip.org/a/lzma-specification.7z")
@Name(value = "Lempel - Ziv - Markov chain - Arithmetic (LZMA) encode input stream", language = "en")
public class LzmaEncodeInputStream extends FilterInputStream {

    @Name(value = "literal context bits")
    private final int lc;
    @Name(value = "literal position state bits")
    private final int lp;
    @Name(value = "position state bits")
    private final int pb;

    LzmaEncodeInputStream(@NotNull InputStream in, int lc, int lp, int pb, int dictionarySize) {
        super(in);
        if (lc < 0 || lc > 8) {
            throw new IllegalArgumentException();
        }
        this.lc = lc;
        if (lp < 0 || lp > 4) {
            throw new IllegalArgumentException();
        }
        this.lp = lp;
        if (pb < 0 || pb > 4) {
            throw new IllegalArgumentException();
        }
        this.pb = pb;
    }
}
