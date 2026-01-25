package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;

import java.lang.annotation.*;

@CodeHistory(date = "2025/10/13")
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ConstantType {

    @DataType("u8")
    int[] tags() default {
            Utf8ConstantInfo.TAG,
            IntegerConstantInfo.TAG,
            FloatConstantInfo.TAG,
            LongConstantInfo.TAG,
            DoubleConstantInfo.TAG,
            ClassConstantInfo.TAG,
            StringConstantInfo.TAG,
            MemberReferenceConstantInfo.TAG_FIELDREF,
            MemberReferenceConstantInfo.TAG_METHODREF,
            MemberReferenceConstantInfo.TAG_INTERFACEMETHODREF,
            NameAndTypeConstantInfo.TAG,
            MethodHandleConstantInfo.TAG,
            MethodTypeConstantInfo.TAG,
            DynamicConstantInfo.TAG,
            InvokeDynamicConstantInfo.TAG,
            ConstantPool.CONSTANT_MODULE,
            ConstantPool.CONSTANT_PACKAGE
    };

    boolean zero() default false;
}
