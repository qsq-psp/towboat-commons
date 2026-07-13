package mujica.ds.i8.view;

import mujica.ds.bit.ReadOnlyBitArray;
import mujica.ds.i8.ReadOnlyI8Array;
import mujica.ds.i32.list.IntSequence;
import mujica.ds.i64.ReadOnlyI64Array;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/5/9")
@ReferencePage(title = "DataView", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/DataView")
public interface DataView extends ReadOnlyBitArray, ReadOnlyI8Array, IntSequence, ReadOnlyI64Array { // multiple data type; endian fixed; sign variable; read only

    Runnable NOP_GUARD = () -> {};

    @Override
    int booleanLength();

    @Override
    boolean getBoolean(int index);

    boolean getBitExact();

    @Override
    int byteLength();

    @Override
    byte getByte(int index);

    byte getByteAll();

    byte getByteExact();

    short getUnsignedByte(int index);

    short getUnsignedByteAll();

    short getUnsignedByteExact();

    int shortLength();

    short getShort(int index);

    short shortAt(int byteOffset);

    short getShortAll();

    short getShortExact();

    int getUnsignedShort(int index);

    int unsignedShortAt(int byteOffset);

    int getUnsignedShortAll();

    int getUnsignedShortExact();

    @Override
    int intLength();

    @Override
    int getInt(int index);

    int intAt(int byteOffset);

    int getIntAll();

    int getIntExact();

    long getUnsignedInt(int index);

    long unsignedIntAt(int byteOffset);

    long getUnsignedIntAll();

    long getUnsignedIntExact();

    @Override
    int longLength();

    @Override
    long getLong(int index);

    long longAt(int byteOffset);

    long getLongAll();

    long getLongExact();

    @NotNull
    String toBinaryString();

    @NotNull
    String toHexString(boolean upperCase);
}
