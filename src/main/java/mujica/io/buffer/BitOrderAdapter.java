package mujica.io.buffer;

import mujica.io.function.BitSequenceConsumer;
import mujica.io.function.ByteSequenceConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2025/4/12.
 */
public abstract class BitOrderAdapter implements ByteSequenceConsumer {

    @NotNull
    protected final BitSequenceConsumer consumer;

    protected BitOrderAdapter(@NotNull BitSequenceConsumer consumer) {
        super();
        this.consumer = consumer;
    }

    public static class BigEndian extends BitOrderAdapter {

        public BigEndian(@NotNull BitSequenceConsumer consumer) {
            super(consumer);
        }

        @Override
        public void accept(byte value) {
            for (int shift = Byte.SIZE - 1; shift >= 0; shift--) {
                consumer.accept((value & (1 << shift)) != 0);
            }
        }
    }

    public static class LittleEndian extends BitOrderAdapter {

        public LittleEndian(@NotNull BitSequenceConsumer consumer) {
            super(consumer);
        }

        @Override
        public void accept(byte value) {
            for (int shift = 0; shift < Byte.SIZE; shift++) {
                consumer.accept((value & (1 << shift)) != 0);
            }
        }
    }
}
