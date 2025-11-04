package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodType;

@CodeHistory(date = "2019/8/14", project = "bone", name = "JavaConstantMethodType")
@CodeHistory(date = "2025/10/17")
@ReferencePage(title = "JVMS12 The CONSTANT_MethodType_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.9")
class MethodTypeConstantInfo extends IndirectUtf8ConstantInfo {

    private static final long serialVersionUID = 0xC40C7EA10D586134L;

    MethodTypeConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_METHODTYPE;
    }

    @Override
    protected int section() {
        return 9;
    }

    @Override
    protected int sinceVersion() {
        return 51 << Short.SIZE;
    }

    @Nullable
    @Override
    protected Class<?> loadedClass() {
        return MethodType.class;
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " method-type " + context.constantPool.getToString(context, utf8Index);
    }
}
