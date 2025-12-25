package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/10/17")
@ReferencePage(title = "JVMS12 The CONSTANT_Dynamic_info and CONSTANT_InvokeDynamic_info Structures", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.10")
class InvokeDynamicConstantInfo extends DynamicConstantInfo {

    private static final long serialVersionUID = 0x08361c296dcec3e4L;

    InvokeDynamicConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_INVOKEDYNAMIC;
    }

    @Override
    protected int sinceVersion() {
        return 51 << Short.SIZE;
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " invoke-dynamic #" + bootstrapMethodAttributeIndex + " " + context.constantPool.getToString(context, nameAndTypeIndex);
    }
}
