package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

@CodeHistory(date = "2025/10/23")
@ReferencePage(title = "JVMS12 The Module Attribute", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7.25")
class ModuleAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x5cb98421479a38a7L;

    @ConstantType(tags = ConstantPool.CONSTANT_MODULE)
    private int moduleIndex;

    @NotNull
    public String getModuleName(@NotNull ClassFile context) {
        return context.getConstantPool().getModuleName(moduleIndex);
    }

    private int flags;

    @ConstantType(tags = Utf8ConstantInfo.TAG)
    private int versionIndex;

    @CodeHistory(date = "2025/10/23")
    private static class Require extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0x58dc34a8eadba0e0L;

        @DataType("u16-{0}")
        @ConstantType(tags = ConstantPool.CONSTANT_MODULE)
        private int moduleIndex;

        private int flags;

        @DataType("u16")
        @ConstantType(tags = Utf8ConstantInfo.TAG, zero = true)
        private int versionIndex;

        Require() {
            super();
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
            final StringBuilder sb = new StringBuilder();
            sb.append("requires ");
            if ((flags & 0x0020) != 0) {
                sb.append("transitive ");
            }
            if ((flags & 0x0040) != 0) {
                sb.append("static ");
            }
            if ((flags & 0x1000) != 0) {
                sb.append("synthetic ");
            }
            if ((flags & 0x8000) != 0) {
                sb.append("mandated ");
            }
            sb.append(context.getConstantPool().getModuleName(moduleIndex));
            if (versionIndex != 0) {
                sb.append(" @ ");
                sb.append(context.getConstantPool().getUtf8(versionIndex));
            }
            return sb.append(";").toString();
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
    private static class Export extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0xfc19da983c170d4dL;

        @DataType("u16")
        @ConstantType(tags = ConstantPool.CONSTANT_PACKAGE)
        int packageIndex;

        @DataType("u16")
        int flags;

        @DataType("u16")
        @ConstantType(tags = ConstantPool.CONSTANT_MODULE)
        short[] toModuleIndexes;

        Export() {
            super();
        }

        public int byteSize() {
            return 6 + 2 * toModuleIndexes.length;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            packageIndex = in.readUnsignedShort();
            flags = in.readUnsignedShort();
            final int toCount = in.readUnsignedShort();
            toModuleIndexes = new short[toCount];
            for (int index = 0; index < toCount; index++) {
                toModuleIndexes[index] = in.readShort();
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            packageIndex = 0xffff & buffer.getShort();
            flags = 0xffff & buffer.getShort();
            final int toCount = 0xffff & buffer.getShort();
            toModuleIndexes = new short[toCount];
            for (int index = 0; index < toCount; index++) {
                toModuleIndexes[index] = buffer.getShort();
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(packageIndex);
            out.writeShort(flags);
            out.writeShort(toModuleIndexes.length);
            for (short moduleIndex : toModuleIndexes) {
                out.writeShort(moduleIndex);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) packageIndex);
            buffer.putShort((short) flags);
            buffer.putShort((short) toModuleIndexes.length);
            for (short moduleIndex : toModuleIndexes) {
                buffer.putShort(moduleIndex);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exports ");
            if ((flags & 0x1000) != 0) {
                sb.append("synthetic ");
            }
            if ((flags & 0x8000) != 0) {
                sb.append("mandated ");
            }
            sb.append(context.getConstantPool().getSourcePackageName(packageIndex));
            if (toModuleIndexes.length != 0) {
                sb.append(" to ");
                boolean subsequent = false;
                for (short moduleIndex : toModuleIndexes) {
                    if (subsequent) {
                        sb.append(", ");
                    }
                    sb.append(context.getConstantPool().getModuleName(moduleIndex));
                    subsequent = true;
                }
            }
            return sb.append(";").toString();
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            packageIndex = remap.applyAsInt(packageIndex);
            ModuleAttributeInfo.remapConstant(remap, toModuleIndexes);
        }
    }

    private Export[] exports;

    @CodeHistory(date = "2025/11/13")
    private static class Open extends ClassFileNodeAdapter implements ClassFileNode.Independent {

        private static final long serialVersionUID = 0xfd0ff69094437fccL;

        @DataType("u16")
        @ConstantType(tags = ConstantPool.CONSTANT_PACKAGE)
        int packageIndex;

        @DataType("u16")
        int flags;

        @DataType("u16")
        @ConstantType(tags = ConstantPool.CONSTANT_MODULE)
        short[] toModuleIndexes;

        Open() {
            super();
        }

        public int byteSize() {
            return 6 + 2 * toModuleIndexes.length;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            packageIndex = in.readUnsignedShort();
            flags = in.readUnsignedShort();
            final int toCount = in.readUnsignedShort();
            toModuleIndexes = new short[toCount];
            for (int index = 0; index < toCount; index++) {
                toModuleIndexes[index] = in.readShort();
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            packageIndex = 0xffff & buffer.getShort();
            flags = 0xffff & buffer.getShort();
            final int toCount = 0xffff & buffer.getShort();
            toModuleIndexes = new short[toCount];
            for (int index = 0; index < toCount; index++) {
                toModuleIndexes[index] = buffer.getShort();
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(packageIndex);
            out.writeShort(flags);
            out.writeShort(toModuleIndexes.length);
            for (short moduleIndex : toModuleIndexes) {
                out.writeShort(moduleIndex);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) packageIndex);
            buffer.putShort((short) flags);
            buffer.putShort((short) toModuleIndexes.length);
            for (short moduleIndex : toModuleIndexes) {
                buffer.putShort(moduleIndex);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            final StringBuilder sb = new StringBuilder();
            sb.append("opens ");
            if ((flags & 0x1000) != 0) {
                sb.append("synthetic ");
            }
            if ((flags & 0x8000) != 0) {
                sb.append("mandated ");
            }
            sb.append(context.getConstantPool().getSourcePackageName(packageIndex));
            if (toModuleIndexes.length != 0) {
                sb.append(" to ");
                boolean subsequent = false;
                for (short moduleIndex : toModuleIndexes) {
                    if (subsequent) {
                        sb.append(", ");
                    }
                    sb.append(context.getConstantPool().getModuleName(moduleIndex));
                    subsequent = true;
                }
            }
            return sb.append(";").toString();
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            packageIndex = remap.applyAsInt(packageIndex);
            ModuleAttributeInfo.remapConstant(remap, toModuleIndexes);
        }
    }

    private Open[] opens;

    @CodeHistory(date = "2025/12/12")
    private static class Use extends ConstantReferenceNodeAdapter {

        private static final long serialVersionUID = 0x19F3CD1C0DEC0366L;

        Use(int index) {
            super(index);
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            return "uses " + context.getConstantPool().getSourceClassName(referenceIndex);
        }
    }

    @ConstantType(tags = ClassConstantInfo.TAG)
    private short[] usesIndexes;

    @CodeHistory(date = "2025/10/24")
    private static class Provide implements ClassFileNode.Independent {

        @DataType("u16")
        @ConstantType(tags = ClassConstantInfo.TAG)
        int provideIndex; // service interface

        @DataType("u16")
        @ConstantType(tags = ClassConstantInfo.TAG)
        short[] withIndexes; // service implementation

        Provide() {
            super();
        }

        @Override
        public int groupCount() {
            return 0;
        }

        @NotNull
        @Override
        public Class<? extends ClassFileNode> getGroup(int groupIndex) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
            return 0;
        }

        @NotNull
        @Override
        public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
            throw new IndexOutOfBoundsException();
        }

        public int byteSize() {
            return 4 + 2 * withIndexes.length;
        }

        @Override
        public void read(@NotNull LimitedDataInput in) throws IOException {
            provideIndex = in.readUnsignedShort();
            final int withCount = in.readUnsignedShort();
            withIndexes = new short[withCount];
            for (int index = 0; index < withCount; index++) {
                withIndexes[index] = in.readShort();
            }
        }

        @Override
        public void read(@NotNull ByteBuffer buffer) {
            provideIndex = 0xffff & buffer.getShort();
            final int withCount = 0xffff & buffer.getShort();
            withIndexes = new short[withCount];
            for (int index = 0; index < withCount; index++) {
                withIndexes[index] = buffer.getShort();
            }
        }

        @Override
        public void write(@NotNull DataOutput out) throws IOException {
            out.writeShort(provideIndex);
            out.writeShort(withIndexes.length);
            for (short withIndex : withIndexes) {
                out.writeShort(withIndex);
            }
        }

        @Override
        public void write(@NotNull ByteBuffer buffer) {
            buffer.putShort((short) provideIndex);
            buffer.putShort((short) withIndexes.length);
            for (short withIndex : withIndexes) {
                buffer.putShort(withIndex);
            }
        }

        @NotNull
        @Override
        public String toString(@NotNull ClassFile context) {
            final StringBuilder sb = new StringBuilder();
            sb.append("provides ");
            sb.append(context.getConstantPool().getSourceClassName(provideIndex));
            if (withIndexes.length != 0) {
                sb.append(" with ");
                boolean subsequent = false;
                for (short moduleIndex : withIndexes) {
                    if (subsequent) {
                        sb.append(", ");
                    }
                    sb.append(context.getConstantPool().getSourceClassName(moduleIndex));
                    subsequent = true;
                }
            }
            return sb.append(";").toString();
        }

        @Override
        public void remapConstant(@NotNull IntUnaryOperator remap) {
            provideIndex = remap.applyAsInt(provideIndex);
            ModuleAttributeInfo.remapConstant(remap, withIndexes);
        }
    }

    private Provide[] provides;

    @Override
    public int groupCount() {
        return 5;
    }

    @NotNull
    @Override
    public Class<? extends ClassFileNode> getGroup(int groupIndex) {
        switch (groupIndex) {
            case 0:
                return Require.class;
            case 1:
                return Export.class;
            case 2:
                return Open.class;
            case 3:
                return Use.class;
            case 4:
                return Provide.class;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int nodeCount(@NotNull Class<? extends ClassFileNode> group) {
        if (group == Require.class) {
            return requires.length;
        } else if (group == Export.class) {
            return exports.length;
        } else if (group == Open.class) {
            return opens.length;
        } else if (group == Use.class) {
            return usesIndexes.length;
        } else if (group == Provide.class) {
            return provides.length;
        } else {
            return 0;
        }
    }

    @NotNull
    @Override
    public ClassFileNode getNode(@NotNull Class<? extends ClassFileNode> group, int nodeIndex) {
        if (group == Require.class) {
            return requires[nodeIndex];
        } else if (group == Export.class) {
            return exports[nodeIndex];
        } else if (group == Open.class) {
            return opens[nodeIndex];
        } else if (group == Use.class) {
            return new Use(usesIndexes[nodeIndex]);
        } else if (group == Provide.class) {
            return provides[nodeIndex];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static final String NAME = "Module";

    @NotNull
    @Override
    public String attributeName() {
        return NAME;
    }

    @Override
    public int byteSize() {
        int size = 16 + 6 * requires.length + 2 * usesIndexes.length;
        for (Export export : exports) {
            size += export.byteSize();
        }
        for (Open open : opens) {
            size += open.byteSize();
        }
        for (Provide provide : provides) {
            size += provide.byteSize();
        }
        return size;
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
        exports = new Export[count];
        for (int index = 0; index < count; index++) {
            Export export = new Export();
            export.read(in);
            exports[index] = export;
        }
        count = in.readUnsignedShort();
        opens = new Open[count];
        for (int index = 0; index < count; index++) {
            Open open = new Open();
            open.read(in);
            opens[index] = open;
        }
        count = in.readUnsignedShort();
        usesIndexes = new short[count];
        for (int index = 0; index < count; index++) {
            usesIndexes[index] = in.readShort();
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
        int count = 0xffff & buffer.getShort();
        requires = new Require[count];
        for (int index = 0; index < count; index++) {
            Require require = new Require();
            require.read(buffer);
            requires[index] = require;
        }
        count = 0xffff & buffer.getShort();
        exports = new Export[count];
        for (int index = 0; index < count; index++) {
            Export export = new Export();
            export.read(buffer);
            exports[index] = export;
        }
        count = 0xffff & buffer.getShort();
        opens = new Open[count];
        for (int index = 0; index < count; index++) {
            Open open = new Open();
            open.read(buffer);
            opens[index] = open;
        }
        count = 0xffff & buffer.getShort();
        usesIndexes = new short[count];
        for (int index = 0; index < count; index++) {
            usesIndexes[index] = buffer.getShort();
        }
        count = 0xffff & buffer.getShort();
        provides = new Provide[count];
        for (int index = 0; index < count; index++) {
            Provide provide = new Provide();
            provide.read(buffer);
            provides[index] = provide;
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.writeShort(moduleIndex);
        out.writeShort(flags);
        out.writeShort(versionIndex);
        out.writeShort(requires.length);
        for (Require require : requires) {
            require.write(out);
        }
        out.writeShort(exports.length);
        for (Export export : exports) {
            export.write(out);
        }
        out.writeShort(opens.length);
        for (Open open : opens) {
            open.write(out);
        }
        out.writeShort(usesIndexes.length);
        for (short useIndex : usesIndexes) {
            out.writeShort(useIndex);
        }
        out.writeShort(provides.length);
        for (Provide provide : provides) {
            provide.write(out);
        }
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.putShort((short) moduleIndex);
        buffer.putShort((short) flags);
        buffer.putShort((short) versionIndex);
        buffer.putShort((short) requires.length);
        for (Require require : requires) {
            require.write(buffer);
        }
        buffer.putShort((short) exports.length);
        for (Export export : exports) {
            export.write(buffer);
        }
        buffer.putShort((short) opens.length);
        for (Open open : opens) {
            open.write(buffer);
        }
        buffer.putShort((short) usesIndexes.length);
        for (short useIndex : usesIndexes) {
            buffer.putShort(useIndex);
        }
        buffer.putShort((short) provides.length);
        for (Provide provide : provides) {
            provide.write(buffer);
        }
    }

    @Override
    public void remapConstant(@NotNull IntUnaryOperator remap) {
        moduleIndex = remap.applyAsInt(moduleIndex);
        flags = remap.applyAsInt(flags);
        versionIndex = remap.applyAsInt(versionIndex);
        for (Require require : requires) {
            require.remapConstant(remap);
        }
        for (Export export : exports) {
            export.remapConstant(remap);
        }
        for (Open open : opens) {
            open.remapConstant(remap);
        }
        remapConstant(remap, usesIndexes);
        for (Provide provide : provides) {
            provide.remapConstant(remap);
        }
    }
}
