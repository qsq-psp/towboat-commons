package mujica.ds.of_long;

import mujica.math.algebra.discrete.IntegralMath;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@CodeHistory(date = "2023/11/29", project = "OSHI")
@CodeHistory(date = "2023/12/1", project = "Ultramarine", name = "LongQuantity")
@CodeHistory(date = "2023/12/6", project = "Ultramarine", name = "LongBox")
@CodeHistory(date = "2025/3/9")
public class PublicLongSlot extends Number implements LongSlot, Comparable<PublicLongSlot>, Cloneable {

    private static final long serialVersionUID = 0xb0ca00edf2e5e344L;

    public long value;

    public PublicLongSlot() {
        super();
    }

    public PublicLongSlot(long value) {
        super();
        this.value = value;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    @NotNull
    public PublicLongSlot clone() {
        return new PublicLongSlot(value);
    }

    public void absExact() {
        value = IntegralMath.INSTANCE.abs(value);
    }

    /**
     * @param map the counter map
     * @param t the item, used as map key
     * @param delta added quantity
     * @param <T> counted item type
     */
    public static <T> long add(@NotNull Map<T, PublicLongSlot> map, T t, int delta) {
        PublicLongSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicLongSlot(delta);
            map.put(t, slot);
        } else {
            slot.value += delta;
        }
        return slot.value;
    }

    public void addExact(long delta) {
        value = Math.addExact(value, delta);
    }

    public static <T> long addExact(@NotNull Map<T, PublicLongSlot> map, T t, long delta) {
        PublicLongSlot slot = map.get(t);
        if (slot == null) {
            slot = new PublicLongSlot(delta);
            map.put(t, slot);
        } else {
            slot.addExact(delta);
        }
        return slot.value;
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public long setLong(long newValue) {
        final long oldValue = value;
        value = newValue;
        return oldValue;
    }

    @Override
    public int compareTo(@NotNull PublicLongSlot that) {
        return Long.compare(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass() && this.value == ((PublicLongSlot) obj).value;
    }

    @Override
    public String toString() {
        return "(" + value + ")";
    }

    /**
     * Created in OSHI on 2023/11/29, named DataSize.
     * Recreated on 2023/12/6.
     */
    public static class Bytes extends PublicLongSlot {

        private static final long serialVersionUID = 0x6b96e5815ab95636L;

        public Bytes() {
            super();
        }

        public Bytes(long value) {
            super(value);
        }

        @Override
        public String toString() {
            // positive only
            // expecting smaller value
            if (value < (1L << 10)) {
                return value + "B";
            }
            if (value < (1L << 20)) {
                return (value >> 10) + "KiB";
            }
            if (value < (1L << 30)) {
                return (value >> 20) + "MiB";
            }
            if (value < (1L << 40)) {
                return (value >> 30) + "GiB";
            }
            if (value < (1L << 50)) {
                return (value >> 40) + "TiB";
            }
            if (value < (1L << 60)) {
                return (value >> 50) + "PiB";
            }
            return (value >> 60) + "EiB";
        }
    }

    /**
     * Created in OSHI on 2023/11/29, named Duration.
     * Recreated on 2023/12/6.
     */
    public static class Milliseconds extends PublicLongSlot {

        private static final long serialVersionUID = 0x020bf5ea87818fc4L;

        public Milliseconds() {
            super();
        }

        public Milliseconds(long value) {
            super(value);
        }
    }

    /**
     * Created on 2024/2/17.
     */
    public static class MillisecondStopWatch extends Milliseconds {

        private static final long serialVersionUID = 0x2b14af2a6e0e183bL;

        public boolean running;

        public boolean isRunning() {
            return running;
        }

        public void toggle(boolean nextRunning, long timeInMilliseconds) {
            if (running != nextRunning) {
                running = nextRunning;
                value = timeInMilliseconds - value;
            }
        }

        public void toggle(boolean nextRunning) {
            toggle(nextRunning, System.currentTimeMillis());
        }

        @Override
        public String toString() {
            if (running) {
                return "StopWatch[ms, running, " + (new Milliseconds(System.currentTimeMillis() - value)) + "]";
            } else {
                return "StopWatch[ms, stopped, " + super.toString() + "]";
            }
        }
    }

    /**
     * Created on 2024/1/28.
     */
    public static class Nanoseconds extends PublicLongSlot {

        private static final long serialVersionUID = 0x25e524c026507f87L;

        public Nanoseconds() {
            super();
        }

        public Nanoseconds(long value) {
            super(value);
        }
    }

    /**
     * Created on 2024/2/17.
     */
    public static class NanosecondStopWatch extends Nanoseconds {

        private static final long serialVersionUID = 0xa11313ca83a08abdL;

        public boolean running;

        public boolean isRunning() {
            return running;
        }

        public void toggle(boolean nextRunning, long timeInMilliseconds) {
            if (running != nextRunning) {
                running = nextRunning;
                value = timeInMilliseconds - value;
            }
        }

        public void toggle(boolean nextRunning) {
            toggle(nextRunning, System.nanoTime());
        }

        @Override
        public String toString() {
            if (running) {
                return "StopWatch[ns, running, " + (new Nanoseconds(System.nanoTime() - value)) + "]";
            } else {
                return "StopWatch[ns, stopped, " + super.toString() + "]";
            }
        }
    }
}
