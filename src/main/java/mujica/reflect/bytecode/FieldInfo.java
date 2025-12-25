package mujica.reflect.bytecode;

import mujica.reflect.basic.BytecodeFieldType;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;

@CodeHistory(date = "2019/8/14", project = "bone", name = "JavaFieldInfo")
@CodeHistory(date = "2025/9/4")
public class FieldInfo extends MemberInfo {

    private static final long serialVersionUID = 0x98925d32e43835a8L;

    transient ConstantValueAttributeInfo constantValue;

    @Override
    protected void afterRead() {
        for (AttributeInfo attribute : attributes) {
            if (attribute instanceof SignatureAttributeInfo) {
                signature = (SignatureAttributeInfo) attribute;
            } else if (attribute instanceof ConstantValueAttributeInfo) {
                constantValue = (ConstantValueAttributeInfo) attribute;
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
        if ((accessFlags & Modifier.VOLATILE) != 0) {
            sb.append("volatile ");
        }
        if ((accessFlags & Modifier.TRANSIENT) != 0) {
            sb.append("transient ");
        }
        if ((accessFlags & 0x1000) != 0) {
            sb.append("synthetic ");
        }
        if ((accessFlags & 0x4000) != 0) {
            sb.append("enum ");
        }
        sb.append((new BytecodeFieldType(descriptor)).toSourceString()).append(' ').append(name);
        if (constantValue != null) {
            sb.append(" = ").append(constantValue.constantValueToString(context));
        }
        return sb.append(';').toString();
    }

    @Override
    public void accept(@NotNull AttributeInfo.Statistics statistics, @NotNull String prefix) {
        prefix += "Field.";
        super.accept(statistics, prefix);
    }
}
