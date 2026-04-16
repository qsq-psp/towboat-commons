package mujica.json.reflect;

import mujica.ds.of_boolean.BooleanSlot;
import mujica.ds.of_byte.ByteSlot;
import mujica.ds.of_char.CharSlot;
import mujica.ds.of_int.IntSlot;
import mujica.ds.of_long.LongSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/8")
class SlotSetter extends Setter {

    @NotNull
    final Getter getter;

    SlotSetter(@NotNull Getter getter) {
        super();
        this.getter = getter;
    }

    @Override
    protected void setBoolean(Object self, boolean value) throws Throwable {
        ((BooleanSlot) getter.get(self)).setBoolean(value);
    }

    @Override
    protected void setByte(Object self, byte value) throws Throwable {
        ((ByteSlot) getter.get(self)).setByte(value);
    }

    @Override
    protected void setChar(Object self, char value) throws Throwable {
        ((CharSlot) getter.get(self)).setChar(value);
    }

    @Override
    protected void setInt(Object self, int value) throws Throwable {
        ((IntSlot) getter.get(self)).setInt(value);
    }

    @Override
    protected void setLong(Object self, long value) throws Throwable {
        ((LongSlot) getter.get(self)).setLong(value);
    }
}
