package mujica.reflect.bytecode;

/**
 * Created on 2025/10/13.
 */
public @interface ConstantType {

    int[] tags() default {
            ConstantPool.CONSTANT_UTF8,
            ConstantPool.CONSTANT_INTEGER,
            ConstantPool.CONSTANT_FLOAT,
            ConstantPool.CONSTANT_LONG,
            ConstantPool.CONSTANT_DOUBLE,
            ConstantPool.CONSTANT_CLASS,
            ConstantPool.CONSTANT_STRING,
            ConstantPool.CONSTANT_FIELDREF,
            ConstantPool.CONSTANT_METHODREF,
            ConstantPool.CONSTANT_INTERFACEMETHODREF,
            ConstantPool.CONSTANT_NAMEANDTYPE,
            ConstantPool.CONSTANT_METHODHANDLE,
            ConstantPool.CONSTANT_METHODTYPE,
            ConstantPool.CONSTANT_DYNAMIC,
            ConstantPool.CONSTANT_INVOKEDYNAMIC,
            ConstantPool.CONSTANT_MODULE,
            ConstantPool.CONSTANT_PACKAGE
    };

    boolean zero() default false;
}
