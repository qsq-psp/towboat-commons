package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2025/10/16")
@ReferencePage(title = "JVMS12 The CONSTANT_Package_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.12")
class PackageConstantInfo extends IndirectUtf8ConstantInfo {

    private static final long serialVersionUID = 0xE9BEA60FADFC2140L;

    PackageConstantInfo() {
        super();
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_PACKAGE;
    }

    @Override
    protected int section() {
        return 12;
    }

    @Override
    protected int sinceVersion() {
        return 53 << Short.SIZE;
    }

    @Nullable
    @Override
    protected Class<?> loadedClass() {
        return Package.class;
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " package " + context.constantPool.getToString(context, utf8Index);
    }
}
