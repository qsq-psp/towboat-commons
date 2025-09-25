package mujica.reflect.bytecode;

import mujica.io.stream.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/18")
public interface ClassFileNode extends Serializable {

    int groupCount();

    @NotNull
    Class<?> getGroup(int groupIndex);

    int nodeCount(@NotNull Class<?> group);

    @NotNull
    ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex);

    @NotNull
    String toString(@NotNull ClassFile context, int position);

    @CodeHistory(date = "2025/9/18")
    interface Independent extends ClassFileNode {

        void read(@NotNull LimitedDataInput in) throws IOException;

        void read(@NotNull ByteBuffer buffer);

        void write(@NotNull DataOutput out) throws IOException;

        void write(@NotNull ByteBuffer buffer);
    }

    @CodeHistory(date = "2025/9/15")
    interface Dependent extends ClassFileNode {

        void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException;

        void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer);

        void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException;

        void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer);
    }
}
