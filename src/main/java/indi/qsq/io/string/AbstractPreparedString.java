package indi.qsq.io.string;

/**
 * Created on 2024/7/13.
 */
public abstract class AbstractPreparedString implements PreparedString {

    final int flags;

    protected AbstractPreparedString(int flags) {
        super();
        this.flags = flags;
    }

    @Override
    public boolean hasCharacteristic(int flag) {
        return (flags & flag) != 0;
    }

    @Override
    public int getCharacteristic() {
        return flags;
    }
}
