package mujica.io.hash;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.LongDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.LongSupplier;

@CodeHistory(date = "2025/4/14")
@ReferencePage(title = "Incremental Updating of the Internet Checksum", href = "https://www.rfc-editor.org/rfc/rfc1141.html")
public class InternetChecksum extends ByteBlockByteHashCore implements LongSupplier {

    private static final long serialVersionUID = 0x7c75b40a939ea604L;

    private long sum;

    public InternetChecksum() {
        super();
    }

    @Override
    public int blockBytes() {
        return 2;
    }

    @Override
    public int resultBytes() {
        return 2;
    }

    @NotNull
    @Override
    public InternetChecksum clone() {
        final InternetChecksum that = new InternetChecksum();
        that.sum = this.sum;
        return that;
    }

    @Override
    public void clear() {
        sum = 0L;
    }

    @Override
    public void start() {
        sum = 0L;
    }

    @Override
    public void step(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        sum += 0xffffL & buffer.getShort();
    }

    @Override
    public void finish(@NotNull ByteBuffer buffer) {
        if (buffer.hasRemaining()) {
            sum += 0xff00L & (buffer.get() << Byte.SIZE);
        }
        while (true) {
            long high = sum >>> Short.SIZE;
            if (high == 0) {
                break;
            }
            sum += high;
        }
    }

    @NotNull
    @Override
    public DataView getDataView(@NotNull Runnable guard) {
        return new LongDataView(this, ByteFillPolicy.MIDDLE_TO_RIGHT, Short.SIZE);
    }

    @Override
    public long getAsLong() {
        return sum;
    }
}
