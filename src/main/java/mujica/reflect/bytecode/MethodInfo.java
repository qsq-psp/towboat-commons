package mujica.reflect.bytecode;

import mujica.io.codec.IndentWriter;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

@CodeHistory(date = "2025/9/3")
public class MethodInfo extends MemberInfo implements Consumer<CodeAttributeInfo.Statistics> {

    private static final long serialVersionUID = 0xe90adc507b5f0952L;

    transient MethodParametersAttributeInfo methodParameters;

    transient ExceptionsAttributeInfo exceptions;

    transient CodeAttributeInfo code;

    @Override
    protected void afterRead() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof MethodParametersAttributeInfo) {
                methodParameters = (MethodParametersAttributeInfo) attribute;
            } else if (attribute instanceof CodeAttributeInfo) {
                code = (CodeAttributeInfo) attribute;
            } else if (attribute instanceof SignatureAttributeInfo) {
                signature = (SignatureAttributeInfo) attribute;
            } else if (attribute instanceof ExceptionsAttributeInfo) {
                exceptions = (ExceptionsAttributeInfo) attribute;
            }
        }
    }

    @NotNull
    @Override
    public String toString(@NotNull ClassFile context) {
        final StringBuilder sb = new StringBuilder();
        if ((accessFlags & Modifier.PUBLIC) != 0) {
            sb.append("public ");
        }
        if ((accessFlags & Modifier.PROTECTED) != 0) {
            sb.append("protected ");
        }
        if ((accessFlags & Modifier.PRIVATE) != 0) {
            sb.append("private ");
        }
        if ((accessFlags & Modifier.STATIC) != 0) {
            sb.append("static ");
        }
        if ((accessFlags & Modifier.FINAL) != 0) {
            sb.append("final ");
        }
        if ((accessFlags & Modifier.SYNCHRONIZED) != 0) {
            sb.append("synchronized ");
        }
        if ((accessFlags & 0x0040) != 0) { // Modifier.VOLATILE
            sb.append("bridge ");
        }
        if ((accessFlags & 0x0080) != 0) { // Modifier.TRANSIENT
            sb.append("varargs ");
        }
        if ((accessFlags & Modifier.NATIVE) != 0) {
            sb.append("native ");
        }
        if ((accessFlags & Modifier.ABSTRACT) != 0) {
            sb.append("abstract ");
        }
        if ((accessFlags & Modifier.STRICT) != 0) {
            sb.append("strictfp ");
        }
        if ((accessFlags & 0x1000) != 0) {
            sb.append("synthetic ");
        }
        sb.append(descriptor).append(" ").append(name);
        if (exceptions != null) {
            sb.append(" throws ");
            exceptions.append(context, sb);
        }
        return sb.append(';').toString();
    }

    @Override
    public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
        prefix += "Method.";
        super.accept(statistics, prefix);
    }

    @Override
    public void accept(@NotNull CodeAttributeInfo.Statistics statistics) {
        if (code != null) {
            code.accept(statistics);
        }
    }

    public void writeAssemble(@NotNull ClassFile context, @NotNull IndentWriter writer) throws IOException {
        if (code != null) {
            writer.write(toString(context));
            writer.indentIn();
            code.writeAssemble(context, writer);
            writer.indentOut();
            writer.newLine();
        }
    }
}
