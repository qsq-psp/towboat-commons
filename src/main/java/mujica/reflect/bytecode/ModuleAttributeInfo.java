package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

/**
 * Created on 2025/10/23.
 */
@CodeHistory(date = "2025/10/23")
@ReferencePage(title = "JVMS12 The Module Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.25")
public class ModuleAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x5CB98421479A38A7L;

    @ConstantType(tags = ConstantPool.CONSTANT_MODULE)
    private int moduleIndex;

    private int flags;

    @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
    private int versionIndex;

    @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
    private int[] usesIndexes; // service interface

    @CodeHistory(date = "2025/10/23")
    private static class Require implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x58DC34A8EADBA0E0L;

        @ConstantType(tags = ConstantPool.CONSTANT_MODULE)
        private int moduleIndex;

        private int flags;

        @ConstantType(tags = ConstantPool.CONSTANT_UTF8)
        private int versionIndex;

        Require() {
            super();
        }

        @Override
        public int groupCount() {
            return 0;
        }

        @NotNull
        @Override
        public Class<?> getGroup(int groupIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int nodeCount(@NotNull Class<?> group) {
            return 0;
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            moduleIndex = in.readUnsignedShort();
            flags = in.readUnsignedShort();
            versionIndex = in.readUnsignedShort();
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            moduleIndex = 0xffff & buffer.getShort();
            flags = 0xffff & buffer.getShort();
            versionIndex = 0xffff & buffer.getShort();
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(moduleIndex);
            out.writeShort(flags);
            out.writeShort(versionIndex);
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) moduleIndex);
            buffer.putShort((short) flags);
            buffer.putShort((short) versionIndex);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "require";
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            moduleIndex = remap.applyAsInt(moduleIndex);
            flags = remap.applyAsInt(flags);
            versionIndex = remap.applyAsInt(versionIndex);
        }
    }

    private Require[] requires;

    @CodeHistory(date = "2025/10/23")
    private static class ExportOrOpen implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0xFC19DA983C170D4DL;

        @ConstantType(tags = ConstantPool.CONSTANT_PACKAGE)
        int packageIndex;

        int flags;

        @ConstantType(tags = ConstantPool.CONSTANT_MODULE)
        int[] toModuleIndexes;

        ExportOrOpen() {
            super();
        }

        @Override
        public int groupCount() {
            return 0;
        }

        @NotNull
        @Override
        public Class<?> getGroup(int groupIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int nodeCount(@NotNull Class<?> group) {
            return 0;
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            packageIndex = in.readUnsignedShort();
            flags = in.readUnsignedShort();
            final int toCount = in.readUnsignedShort();
            toModuleIndexes = new int[toCount];
            for (int index = 0; index < toCount; index++) {
                toModuleIndexes[index] = in.readUnsignedShort();
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            packageIndex = 0xffff & buffer.getShort();
            flags = 0xffff & buffer.getShort();
            final int toCount = 0xffff & buffer.getShort();
            toModuleIndexes = new int[toCount];
            for (int index = 0; index < toCount; index++) {
                toModuleIndexes[index] = 0xffff & buffer.getShort();
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(packageIndex);
            out.writeShort(flags);
            out.writeShort(toModuleIndexes.length);
            for (int moduleIndex : toModuleIndexes) {
                out.writeShort(moduleIndex);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) packageIndex);
            buffer.putShort((short) flags);
            buffer.putShort((short) toModuleIndexes.length);
            for (int moduleIndex : toModuleIndexes) {
                buffer.putShort((short) moduleIndex);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "export/open";
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            packageIndex = remap.applyAsInt(packageIndex);
            final int toCount = toModuleIndexes.length;
            for (int index = 0; index < toCount; index++) {
                toModuleIndexes[index] = remap.applyAsInt(toModuleIndexes[index]);
            }
        }
    }

    private ExportOrOpen[] exports;

    private ExportOrOpen[] opens;

    @CodeHistory(date = "2025/10/24")
    private static class Provide implements ClassFileNode.Independent {

        @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
        int provideIndex; // service interface

        @ConstantType(tags = ConstantPool.CONSTANT_CLASS)
        int[] withIndexes; // service implementation

        Provide() {
            super();
        }

        @Override
        public int groupCount() {
            return 0;
        }

        @NotNull
        @Override
        public Class<?> getGroup(int groupIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int nodeCount(@NotNull Class<?> group) {
            return 0;
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<?> group, int nodeIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            provideIndex = in.readUnsignedShort();
            final int withCount = in.readUnsignedShort();
            withIndexes = new int[withCount];
            for (int index = 0; index < withCount; index++) {
                withIndexes[index] = in.readUnsignedShort();
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            provideIndex = 0xffff & buffer.getShort();
            final int withCount = 0xffff & buffer.getShort();
            withIndexes = new int[withCount];
            for (int index = 0; index < withCount; index++) {
                withIndexes[index] = 0xffff & buffer.getShort();
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(provideIndex);
            out.writeShort(withIndexes.length);
            for (int withIndex : withIndexes) {
                out.writeShort(withIndex);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) provideIndex);
            buffer.putShort((short) withIndexes.length);
            for (int withIndex : withIndexes) {
                buffer.putShort((short) withIndex);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "provide";
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            provideIndex = remap.applyAsInt(provideIndex);
            final int withCount = withIndexes.length;
            for (int index = 0; index < withCount; index++) {
                withIndexes[index] = remap.applyAsInt(withIndexes[index]);
            }
        }
    }

    private Provide[] provides;

    public static final String NAME = "Module";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        return 0;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        moduleIndex = in.readUnsignedShort();
        flags = in.readUnsignedShort();
        versionIndex = in.readUnsignedShort();
        int count = in.readUnsignedShort();
        requires = new Require[count];
        for (int index = 0; index < count; index++) {
            Require require = new Require();
            require.read(in);
            requires[index] = require;
        }
        count = in.readUnsignedShort();
        exports = new ExportOrOpen[count];
        for (int index = 0; index < count; index++) {
            ExportOrOpen export = new ExportOrOpen();
            export.read(in);
            exports[index] = export;
        }
        count = in.readUnsignedShort();
        opens = new ExportOrOpen[count];
        for (int index = 0; index < count; index++) {
            ExportOrOpen open = new ExportOrOpen();
            open.read(in);
            opens[index] = open;
        }
        count = in.readUnsignedShort();
        usesIndexes = new int[count];
        for (int index = 0; index < count; index++) {
            usesIndexes[index] = in.readUnsignedShort();
        }
        count = in.readUnsignedShort();
        provides = new Provide[count];
        for (int index = 0; index < count; index++) {
            Provide provide = new Provide();
            provide.read(in);
            provides[index] = provide;
        }
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        moduleIndex = 0xffff & buffer.getShort();
        flags = 0xffff & buffer.getShort();
        versionIndex = 0xffff & buffer.getShort();
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(moduleIndex);
        out.writeShort(flags);
        out.writeShort(versionIndex);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) moduleIndex);
        buffer.putShort((short) flags);
        buffer.putShort((short) versionIndex);
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        moduleIndex = remap.applyAsInt(moduleIndex);
        flags = remap.applyAsInt(flags);
        versionIndex = remap.applyAsInt(versionIndex);
        remapConstant(remap, usesIndexes);
        for (Require require : requires) {
            require.remapConstant(remap);
        }
        for (ExportOrOpen export : exports) {
            export.remapConstant(remap);
        }
        for (ExportOrOpen open : opens) {
            open.remapConstant(remap);
        }
        for (Provide provide : provides) {
            provide.remapConstant(remap);
        }
    }
}
