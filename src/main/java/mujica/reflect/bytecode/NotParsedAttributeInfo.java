package mujica.reflect.bytecode;

import mujica.io.nest.LimitedDataInput;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/9/15")
@ReferencePage(title = "JVMS12 Attributes", href = "https://docs.oracle.com/javase/specs/jvms/se12/html/jvms-4.html#jvms-4.7")
public class NotParsedAttributeInfo extends AttributeInfo {

    private static final long serialVersionUID = 0x1a562ef5c401b03dL;

    @NotNull
    final String name;

    byte[] data;

    public NotParsedAttributeInfo(@NotNull String name) {
        super();
        this.name = name;
    }

    @NotNull
    @Override
    public ImportanceLevel importanceLevel() {
        switch (name) {
            case "ConstantValue":
            case "Code":
            case "StackMapTable":
            case "BootstrapMethods":
            case "NestHost":
            case "NestMembers":
                return ImportanceLevel.CRITICAL;
            case "Exceptions":
            case "InnerClasses":
            case "EnclosingMethod":
            case "Synthetic":
            case "Signature":
            case "SourceFile":
            case "LineNumberTable":
            case "LocalVariableTable":
            case "LocalVariableTypeTable":
                return ImportanceLevel.HIGH;
            case "SourceDebugExtension":
            case "Deprecated":
            case "RuntimeVisibleAnnotations":
            case "RuntimeInvisibleAnnotations":
            case "RuntimeVisibleParameterAnnotations":
            case "RuntimeInvisibleParameterAnnotations":
            case "RuntimeVisibleTypeAnnotations":
            case "RuntimeInvisibleTypeAnnotations":
            case "AnnotationDefault":
            case "MethodParameters":
            case "Module":
            case "ModulePackages":
            case "ModuleMainClass":
                return ImportanceLevel.MEDIUM;
            default:
                return ImportanceLevel.LOW;
        }
    }

    @NotNull
    @Override
    public String attributeName() {
        return name;
    }

    @Override
    public int byteSize() {
        return data.length;
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull LimitedDataInput in) throws IOException {
        data = new byte[(int) in.getRemaining()];
        in.readFully(data);
    }

    @Override
    public void read(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        data = new byte[buffer.remaining()];
        buffer.get(data);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull DataOutput out) throws IOException {
        out.write(data);
    }

    @Override
    public void write(@NotNull ConstantPool context, @NotNull ByteBuffer buffer) {
        buffer.put(data);
    }
}
