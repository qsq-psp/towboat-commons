package mujica.io.hash;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.IntSupplier;

@CodeHistory(date = "2024/12/11", project = "Ultramarine")
@CodeHistory(date = "2025/4/14")
@ReferencePage(title = "ZLIB Compressed Data Format Specification version 3.3", href = "https://www.rfc-editor.org/rfc/rfc1950.html")
public class Adler32 extends EachByteStreamHash implements IntSupplier, Serializable {

    private static final long serialVersionUID = 0xc4dc856ffe5918fbL;

    private static final int MOD = 65521;

    private int a, b;

    public Adler32() {
        super();
    }

    @NotNull
    @Override
    public Adler32 clone() {
        final Adler32 that = new Adler32();
        that.a = this.a;
        that.b = this.b;
        return that;
    }

    @Override
    public void clear() {
        a = 0; // s1
        b = 0; // s2
    }

    @Override
    public void start() {
        a = 1; // s1
        b = 0; // s2
    }

    @Override
    public void update(byte value) {
        a = (a + (0xff & value)) % MOD;
        b = (a + b) % MOD;
    }

    @NotNull
    @Override
    public DataView finish() {
        return new IntDataView(getAsInt(), ByteFillPolicy.RIGHT_TO_MIDDLE, Integer.SIZE);
    }

    @Override
    public int getAsInt() {
        assert 0 <= a && a < MOD;
        assert 0 <= b && b < MOD;
        return (b << 16) | a;
    }
}
