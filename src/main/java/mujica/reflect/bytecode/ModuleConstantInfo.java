package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created on 2025/10/16.
 */
@CodeHistory(date = "2025/10/16")
@ReferencePage(title = "JVMS12 The CONSTANT_Module_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.11")
class ModuleConstantInfo extends IndirectUtf8ConstantInfo {

    private static final long serialVersionUID = 0x923FA73AB1D799DDL;

    ModuleConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_MODULE;
    }

    @Override
    protected int section() {
        return 11;
    }

    @Override
    protected int sinceVersion() {
        return 53 << Short.SIZE;
    }

    @Nullable
    @Override
    protected Class<?> loadedClass() {
        return Module.class;
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " module " + context.constantPool.getToString(context, utf8Index);
    }
}
