package mujica.json.reflect;

import mujica.ds.of_boolean.BooleanSlot;
import mujica.ds.of_byte.ByteSlot;
import mujica.ds.of_char.CharSlot;
import mujica.ds.of_int.IntSlot;
import mujica.ds.of_long.LongSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2026/4/8")
class SlotGetter extends Getter {

    @NotNull
    final Getter getter;

    SlotGetter(@NotNull Getter getter) {
        super();
        this.getter = getter;
    }

    @Override
    protected boolean getBoolean(Object self) throws Throwable {
        return ((BooleanSlot) getter.get(self)).getBoolean();
    }

    @Override
    protected byte getByte(Object self) throws Throwable {
        return ((ByteSlot) getter.get(self)).getByte();
    }

    @Override
    protected char getChar(Object self) throws Throwable {
        return ((CharSlot) getter.get(self)).getChar();
    }

    @Override
    protected int getInt(Object self) throws Throwable {
        return ((IntSlot) getter.get(self)).getInt();
    }

    @Override
    protected long getLong(Object self) throws Throwable {
        return ((LongSlot) getter.get(self)).getLong();
    }
}
