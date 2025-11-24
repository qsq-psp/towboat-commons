package mujica.reflect.bytecode;

import mujica.ds.generic.list.TruncateList;
import mujica.io.codec.IndentWriter;
import mujica.io.nest.LimitedDataInput;
import mujica.math.algebra.random.RandomContext;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/9/13")
@ReferencePage(title = "JVMS12 The Constant Pool", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4")
public class ConstantPool implements ClassFileNode.Independent, IntUnaryOperator {

    private static final long serialVersionUID = 0x3E9BA0CFA68B44ADL;

    /**
     * Occupy two slots
     */
    interface Large {}

    private final TruncateList<ConstantInfo> list = new TruncateList<>();

    private final HashMap<ConstantInfo, Integer> map = new HashMap<>();

    public ConstantPool() {
        super();
    }

    @Override
    public int groupCount() {
        return 1;
    }

    @NotNull
    @Override
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        if (groupIndex == 0) {
            return ConstantInfo.class;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == ConstantInfo.class) {
            return list.size();
        }  else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == ConstantInfo.class) {
            return list.get(nodeIndex);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static final int CONSTANT_UTF8 = 1;

    public static final int CONSTANT_INTEGER = 3;

    public static final int CONSTANT_FLOAT = 4;

    public static final int CONSTANT_LONG = 5;

    public static final int CONSTANT_DOUBLE = 6;

    public static final int CONSTANT_CLASS = 7;

    public static final int CONSTANT_STRING = 8;

    @Name(value = "field reference", language = "en")
    public static final int CONSTANT_FIELDREF = 9;

    @Name(value = "method reference", language = "en")
    public static final int CONSTANT_METHODREF = 10;

    @Name(value = "interface method reference", language = "en")
    public static final int CONSTANT_INTERFACEMETHODREF = 11;

    @Name(value = "name and type", language = "en")
    public static final int CONSTANT_NAMEANDTYPE = 12;

    @Name(value = "method handle", language = "en")
    public static final int CONSTANT_METHODHANDLE = 15;

    @Name(value = "method type", language = "en")
    public static final int CONSTANT_METHODTYPE = 16;

    public static final int CONSTANT_DYNAMIC = 17;

    @Name(value = "invoke dynamic", language = "en")
    public static final int CONSTANT_INVOKEDYNAMIC = 18;

    public static final int CONSTANT_MODULE = 19;

    public static final int CONSTANT_PACKAGE = 20;

    @NotNull
    private static ConstantInfo createByTag(int tag) {
        switch (tag) {
            case CONSTANT_UTF8:
                return new Utf8ConstantInfo();
            case CONSTANT_INTEGER:
                return new IntegerConstantInfo();
            case CONSTANT_FLOAT:
                return new FloatConstantInfo();
            case CONSTANT_LONG:
                return new LongConstantInfo();
            case CONSTANT_DOUBLE:
                return new DoubleConstantInfo();
            case CONSTANT_CLASS:
                return new ClassConstantInfo();
            case CONSTANT_STRING:
                return new StringConstantInfo();
            case CONSTANT_FIELDREF:
            case CONSTANT_METHODREF:
            case CONSTANT_INTERFACEMETHODREF:
                return new MemberReferenceConstantInfo(tag);
            case CONSTANT_NAMEANDTYPE:
                return new NameAndTypeConstantInfo();
            case CONSTANT_METHODHANDLE:
                return new MethodHandleConstantInfo();
            case CONSTANT_METHODTYPE:
                return new MethodTypeConstantInfo();
            case CONSTANT_MODULE:
                return new ModuleConstantInfo();
            case CONSTANT_PACKAGE:
                return new PackageConstantInfo();
            case CONSTANT_DYNAMIC:
                return new DynamicConstantInfo();
            case CONSTANT_INVOKEDYNAMIC:
                return new InvokeDynamicConstantInfo();
            default:
                throw new ClassFormatError("tag = " + tag);
        }
    }

    @Override
    public void read(@NotNull LimitedDataInput in) throws IOException {
        final int occupiedSize = in.readUnsignedShort();
        if (occupiedSize == 0) {
            throw new RuntimeException();
        }
        list.clear();
        list.add(new ConstantSlot());
        map.clear();
        for (int index = 1; index < occupiedSize; index++) {
            ConstantInfo info = createByTag(in.readUnsignedByte());
            assert index == list.size();
            info.newIndex = index;
            info.read(in);
            list.add(info);
            map.putIfAbsent(info, index);
            if (info instanceof Large) {
                list.add(new ConstantSlot());
                index++;
            }
        }
    }

    @Override
    public void read(@NotNull ByteBuffer buffer) {
        final int occupiedSize = 0xffff & buffer.getShort();
        if (occupiedSize == 0) {
            throw new RuntimeException();
        }
        list.clear();
        list.add(new ConstantSlot());
        map.clear();
        for (int index = 1; index < occupiedSize; index++) {
            ConstantInfo info = createByTag(0xff & buffer.get());
            assert index == list.size();
            info.newIndex = index;
            info.read(buffer);
            list.add(info);
            map.putIfAbsent(info, index);
            if (info instanceof Large) {
                list.add(new ConstantSlot());
                index++;
            }
        }
    }

    @Override
    public void write(@NotNull DataOutput out) throws IOException {
        out.writeShort(occupiedSize());
        for (ConstantInfo info : list) {
            int tag = info.tag();
            if (tag != 0) {
                out.writeByte(tag);
                info.write(out);
            }
        }
    }

    @Override
    public void write(@NotNull ByteBuffer buffer) {
        buffer.putShort((short) occupiedSize());
        for (ConstantInfo info : list) {
            int tag = info.tag();
            if (tag != 0) {
                buffer.put((byte) tag);
                info.write(buffer);
            }
        }
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "ConstantPool " + occupiedSize() + " / " + usableSize();
    }

    /**
     * Slot at zero or after long and double included
     */
    public int occupiedSize() {
        return list.size();
    }

    /**
     * Slot at zero or after long and double excluded
     */
    public int usableSize() {
        return map.size();
    }

    private int put(@NotNull ConstantInfo info) {
        Integer index = map.get(info);
        if (index != null) {
            return index;
        }
        index = list.size();
        list.add(info);
        map.put(info, index);
        return index;
    }

    @NotNull
    public String getUtf8(int index) {
        return ((Utf8ConstantInfo) list.get(index)).string;
    }

    public int putUtf8(@NotNull String string) {
        return put(new Utf8ConstantInfo(string));
    }

    @NotNull
    public String getClassName(int index) {
        return getUtf8(((ClassConstantInfo) list.get(index)).utf8Index);
    }

    public int putClassName(@NotNull String className) {
        return put(new ClassConstantInfo(putUtf8(className)));
    }

    @NotNull
    public String getSourceClassName(int index) {
        return getClassName(index).replace('/', '.');
    }

    @NotNull
    public String constantValueToString(@NotNull ClassFile context, int index) {
        return list.get(index).constantValueToString(context);
    }

    @NotNull
    public String getToString(@NotNull ClassFile context, int index) {
        return list.get(index).toString(context);
    }

    public void getAndRetain(@NotNull ClassFile context, int index) {
        list.get(index).retain(context);
    }

    public void writeReference(@NotNull ClassFile context, @NotNull IndentWriter writer) throws IOException {
        for (ConstantInfo info : list) {
            info.referenceCount = 0;
        }
        context.remapConstant(index -> {
            getAndRetain(context, index);
            return index;
        });
        for (ConstantInfo info : list) {
            writer.write(info.toString(context));
            writer.write(" (" + info.referenceCount + ")");
            writer.newLine();
        }
    }

    void sort(@NotNull ConstantOrder order) {
        if (order == ConstantOrder.HASH_SHUFFLE) {
            int index = 1;
            for (ConstantInfo info : map.keySet()) {
                info.newIndex = index;
                if (info instanceof Large) {
                    index += 2;
                } else {
                    index++;
                }
            }
            return;
        }
        final ConstantInfo[] newArray = new ConstantInfo[map.size()];
        {
            int index = 0;
            for (ConstantInfo info : map.keySet()) {
                newArray[index++] = info;
            }
        }
        if (order == ConstantOrder.RANDOM_SHUFFLE) {
            (new RandomContext()).shuffleArray(newArray);
        } else {
            Arrays.sort(newArray, order);
        }
        {
            int index = 1;
            for (ConstantInfo info : newArray) {
                info.newIndex = index;
                if (info instanceof Large) {
                    index += 2;
                } else {
                    index++;
                }
            }
        }
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        for (ConstantInfo info : list) {
            info.remapConstant(remap);
        }
    }

    @Override
    public int applyAsInt(int oldIndex) {
        return list.get(oldIndex).newIndex; // maps old index to new index
    }

    void finishRemap() {
        for (ConstantInfo info : map.keySet()) {
            list.set(info.newIndex, info);
            if (info instanceof Large) {
                list.set(info.newIndex + 1, new ConstantSlot());
            }
        }
    }
}
