package mujica.io.view;

import mujica.ds.of_boolean.BitSequence;
import mujica.ds.of_int.list.IntSequence;
import mujica.ds.of_long.LongSequence;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

/**
 * Multiple data type; endian fixed; sign variable; read only
 */
@CodeHistory(date = "2025/5/9")
@ReferencePage(title = "DataView", language = "en", href = "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/DataView")
public interface DataView extends BitSequence, ByteSequence, IntSequence, LongSequence {

    @Override
    int bitLength();

    @Override
    boolean getBit(int index);

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
