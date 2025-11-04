package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CodeHistory(date = "2019/8/14", project = "bone", name = "JavaConstantClass")
@CodeHistory(date = "2025/9/21")
@ReferencePage(title = "JVMS12 The CONSTANT_Class_info Structure", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.4.1")
class ClassConstantInfo extends IndirectUtf8ConstantInfo {

    private static final long serialVersionUID = 0x8F53B690656E7D58L;

    ClassConstantInfo() {
        super();
    }

    ClassConstantInfo(int utf8Index) {
        super();
        this.utf8Index = utf8Index;
    }

    @Override
    protected int tag() {
        return ConstantPool.CONSTANT_CLASS;
    }

    protected int section() {
        return 1;
    }

    protected int sinceVersion() {
        return (45 << Short.SIZE) | 3;
    }

    @Nullable
    protected Class<?> loadedClass() {
        return Class.class;
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        return "#" + newIndex + " class " + context.constantPool.getToString(context, utf8Index);
    }
}
