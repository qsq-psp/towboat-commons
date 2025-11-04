package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2019/8/14", project = "bone", name = "JavaConstantString")
@CodeHistory(date = "2025/9/22")
@ReferencePage(title = "JVMS12 The CONSTANT_String_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.3")
class StringConstantInfo extends IndirectUtf8ConstantInfo {

    private static final long serialVersionUID = 0x6AD502A11703BE44L;

    StringConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_STRING;
    }

    protected int section() {
        return 3;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return String.class;
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " string " + context.constantPool.getToString(context, utf8Index);
    }

    @NotNull
    @Override
    public String constantValueToString(@NotNull ClassFile context) {
        return context.constantPool.getToString(context, utf8Index);
    }
}
