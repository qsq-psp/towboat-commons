package mujica.io.hash;

import mujica.ds.i8.view.ByteFillPolicy;
import mujica.ds.i8.view.DataView;
import mujica.ds.i8.view.LongDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferenceCode;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

@CodeHistory(date = "2026/9/7")
@ReferenceCode(groupId = "com.google.guava", artifactId = "guava", version = "33.6.0-jre", fullyQualifiedName = "com.google.common.hash.SipHashFunction")
public class SipHash extends ByteBlockByteHashCore {

    @Name(value = "number of compression rounds", language = "en")
    private final int c;

    @Name(value = "number of finalization rounds", language = "en")
    private final int d;

    public SipHash(int c, int d) {
        super();
        this.c = c;
        this.d = d;
    }

    public SipHash() {
        this(2, 4);
    }

    @Override
    public int blockBytes() {
        return 8;
    }

    @Override
    public int resultBytes() {
        return 8;
    }

    @NotNull
    @Override
    public ByteBlockByteHashCore clone() {
        return new SipHash(c, d);
    }

    @Override
    public void clear() {

    }

    @Override
    public void start() {

    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {

    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {

    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new LongDataView(() -> {
            guard.run();
            return 0L;
        }, ByteFillPolicy.MIDDLE_TO_RIGHT, Long.SIZE);
    }
}
