package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;

/**
 * According to JVMS, 'opcode' is one word
 */
@CodeHistory(date = "2025/8/31")
public interface Opcode {

    @Name(value = "no operation", language = "en")
    int NOP = 0x00;

    int ACONST_NULL = 0x01;
    int ICONST_M1 = 0x02;
    int ICONST_0 = 0x03;
    int ICONST_1 = 0x04;
    int ICONST_2 = 0x05;
    int ICONST_3 = 0x06;
    int ICONST_4 = 0x07;
    int ICONST_5 = 0x08;
    int LCONST_0 = 0x09;
    int LCONST_1 = 0x0a;
    int FCONST_0 = 0x0b;
    int FCONST_1 = 0x0c;
    int FCONST_2 = 0x0d;
    int DCONST_0 = 0x0e;
    int DCONST_1 = 0x0f;

    @Name(value = "byte immediate push", language = "en")
    int BIPUSH = 0x10;
    @Name(value = "short immediate push", language = "en")
    int SIPUSH = 0x11;

    @Name(value = "load constant", language = "en")
    int LDC = 0x12; // constant pool (1)
    @Name(value = "load constant wide", language = "en")
    int LDC_W = 0x13; // constant pool (2)
    @Name(value = "load two-slot constant wide", language = "en")
    int LDC2_W = 0x14; // constant pool (2)

    int ILOAD = 0x15;
    int LLOAD = 0x16;
    int FLOAD = 0x17;
    int DLOAD = 0x18;
    int ALOAD = 0x19;
    int ILOAD_0 = 0x1a;
    int ILOAD_1 = 0x1b;
    int ILOAD_2 = 0x1c;
    int ILOAD_3 = 0x1d;
    int LLOAD_0 = 0x1e;
    int LLOAD_1 = 0x1f;
    int LLOAD_2 = 0x20;
    int LLOAD_3 = 0x21;
    int FLOAD_0 = 0x22;
    int FLOAD_1 = 0x23;
    int FLOAD_2 = 0x24;
    int FLOAD_3 = 0x25;
    int DLOAD_0 = 0x26;
    int DLOAD_1 = 0x27;
    int DLOAD_2 = 0x28;
    int DLOAD_3 = 0x29;
    int ALOAD_0 = 0x2a;
    int ALOAD_1 = 0x2b;
    int ALOAD_2 = 0x2c;
    int ALOAD_3 = 0x2d;
    int IALOAD = 0x2e;
    int LALOAD = 0x2f;
    int FALOAD = 0x30;
    int DALOAD = 0x31;
    int AALOAD = 0x32;
    int BALOAD = 0x33;
    int CALOAD = 0x34;
    int SALOAD = 0x35;

    int ISTORE = 0x36;
    int LSTORE = 0x37;
    int FSTORE = 0x38;
    int DSTORE = 0x39;
    int ASTORE = 0x3a;
    int ISTORE_0 = 0x3b;
    int ISTORE_1 = 0x3c;
    int ISTORE_2 = 0x3d;
    int ISTORE_3 = 0x3e;
    int LSTORE_0 = 0x3f;
    int LSTORE_1 = 0x40;
    int LSTORE_2 = 0x41;
    int LSTORE_3 = 0x42;
    int FSTORE_0 = 0x43;
    int FSTORE_1 = 0x44;
    int FSTORE_2 = 0x45;
    int FSTORE_3 = 0x46;
    int DSTORE_0 = 0x47;
    int DSTORE_1 = 0x48;
    int DSTORE_2 = 0x49;
    int DSTORE_3 = 0x4a;
    int ASTORE_0 = 0x4b;
    int ASTORE_1 = 0x4c;
    int ASTORE_2 = 0x4d;
    int ASTORE_3 = 0x4e;
    int IASTORE = 0x4f;
    int LASTORE = 0x50;
    int FASTORE = 0x51;
    int DASTORE = 0x52;
    int AASTORE = 0x53;
    int BASTORE = 0x54;
    int CASTORE = 0x55;
    int SASTORE = 0x56;

    int POP = 0x57;
    int POP2 = 0x58;
    int DUP = 0x59;
    int DUP_X1 = 0x5a;
    int DUP_X2 = 0x5b;
    int DUP2 = 0x5c;
    int DUP2_X1 = 0x5d;
    int DUP2_X2 = 0x5e;
    int SWAP = 0x5f;

    @Name(value = "int add", language = "en")
    int IADD = 0x60;
    @Name(value = "long add", language = "en")
    int LADD = 0x61;
    @Name(value = "float add", language = "en")
    int FADD = 0x62;
    @Name(value = "double add", language = "en")
    int DADD = 0x63;
    @Name(value = "int subtract", language = "en")
    int ISUB = 0x64;
    @Name(value = "long subtract", language = "en")
    int LSUB = 0x65;
    @Name(value = "float subtract", language = "en")
    int FSUB = 0x66;
    @Name(value = "double subtract", language = "en")
    int DSUB = 0x67;
    @Name(value = "int multiply", language = "en")
    int IMUL = 0x68;
    @Name(value = "long multiply", language = "en")
    int LMUL = 0x69;
    @Name(value = "float multiply", language = "en")
    int FMUL = 0x6a;
    @Name(value = "double multiply", language = "en")
    int DMUL = 0x6b;
    @Name(value = "int divide", language = "en")
    int IDIV = 0x6c;
    @Name(value = "long divide", language = "en")
    int LDIV = 0x6d;
    @Name(value = "float divide", language = "en")
    int FDIV = 0x6e;
    @Name(value = "double divide", language = "en")
    int DDIV = 0x6f;
    @Name(value = "int remainder", language = "en")
    int IREM = 0x70;
    @Name(value = "long remainder", language = "en")
    int LREM = 0x71;
    @Name(value = "float remainder", language = "en")
    int FREM = 0x72;
    @Name(value = "double remainder", language = "en")
    int DREM = 0x73;
    @Name(value = "int negate", language = "en")
    int INEG = 0x74;
    @Name(value = "long negate", language = "en")
    int LNEG = 0x75;
    @Name(value = "float negate", language = "en")
    int FNEG = 0x76;
    @Name(value = "double negate", language = "en")
    int DNEG = 0x77;
    @Name(value = "int shift left", language = "en")
    int ISHL = 0x78;
    @Name(value = "long shift left", language = "en")
    int LSHL = 0x79;
    @Name(value = "int shift right", language = "en")
    int ISHR = 0x7a;
    @Name(value = "long shift right", language = "en")
    int LSHR = 0x7b;
    @Name(value = "int unsigned shift right", language = "en")
    int IUSHR = 0x7c;
    @Name(value = "long unsigned shift right", language = "en")
    int LUSHR = 0x7d;
    @Name(value = "int and", language = "en")
    int IAND = 0x7e;
    @Name(value = "long and", language = "en")
    int LAND = 0x7f;
    @Name(value = "int or", language = "en")
    int IOR = 0x80;
    @Name(value = "long or", language = "en")
    int LOR = 0x81;
    @Name(value = "int exclusive or", language = "en")
    int IXOR = 0x82;
    @Name(value = "long exclusive or", language = "en")
    int LXOR = 0x83;
    @Name(value = "int increase", language = "en")
    int IINC = 0x84;

    @Name(value = "int to long", language = "en")
    int I2L = 0x85;
    @Name(value = "int to float", language = "en")
    int I2F = 0x86;
    @Name(value = "int to double", language = "en")
    int I2D = 0x87;
    @Name(value = "long to int", language = "en")
    int L2I = 0x88;
    @Name(value = "long to float", language = "en")
    int L2F = 0x89;
    @Name(value = "long to double", language = "en")
    int L2D = 0x8a;
    @Name(value = "float to int", language = "en")
    int F2I = 0x8b;
    @Name(value = "float to long", language = "en")
    int F2L = 0x8c;
    @Name(value = "float to double", language = "en")
    int F2D = 0x8d;
    @Name(value = "double to int", language = "en")
    int D2I = 0x8e;
    @Name(value = "double to long", language = "en")
    int D2L = 0x8f;
    @Name(value = "double to float", language = "en")
    int D2F = 0x90;
    @Name(value = "int to byte", language = "en")
    int I2B = 0x91;
    @Name(value = "int to char", language = "en")
    int I2C = 0x92;
    @Name(value = "int to short", language = "en")
    int I2S = 0x93;

    @Name(value = "long compare", language = "en")
    int LCMP = 0x94;
    @Name(value = "float compare less", language = "en")
    int FCMPL = 0x95;
    @Name(value = "float compare greater", language = "en")
    int FCMPG = 0x96;
    @Name(value = "double compare less", language = "en")
    int DCMPL = 0x97;
    @Name(value = "double compare greater", language = "en")
    int DCMPG = 0x98;
    @Name(value = "if equal", language = "en")
    int IFEQ = 0x99;
    @Name(value = "if not equal", language = "en")
    int IFNE = 0x9a;
    @Name(value = "if less", language = "en")
    int IFLT = 0x9b;
    @Name(value = "if greater or equal", language = "en")
    int IFGE = 0x9c;
    @Name(value = "if greater", language = "en")
    int IFGT = 0x9d;
    @Name(value = "if less or equal", language = "en")
    int IFLE = 0x9e;
    int IF_ICMPEQ = 0x9f;
    int IF_ICMPNE = 0xa0;
    int IF_ICMPLT = 0xa1;
    int IF_ICMPGE = 0xa2;
    int IF_ICMPGT = 0xa3;
    int IF_ICMPLE = 0xa4;
    int IF_ACMPEQ = 0xa5;
    int IF_ACMPNE = 0xa6;

    int GOTO = 0xa7;
    @Name(value = "jump subroutine", language = "en")
    int JSR = 0xa8;
    @Name(value = "return from subroutine", language = "en")
    int RET = 0xa9;
    @Name(value = "table switch", language = "en")
    int TABLESWITCH = 0xaa;
    @Name(value = "lookup switch", language = "en")
    int LOOKUPSWITCH = 0xab;
    @Name(value = "int return", language = "en")
    int IRETURN = 0xac;
    @Name(value = "long return", language = "en")
    int LRETURN = 0xad;
    @Name(value = "float return", language = "en")
    int FRETURN = 0xae;
    @Name(value = "double return", language = "en")
    int DRETURN = 0xaf;
    @Name(value = "reference return", language = "en")
    int ARETURN = 0xb0;
    int RETURN = 0xb1;

    @Name(value = "get static", language = "en")
    int GETSTATIC = 0xb2; // constant pool
    @Name(value = "put static", language = "en")
    int PUTSTATIC = 0xb3; // constant pool
    @Name(value = "get field", language = "en")
    int GETFIELD = 0xb4; // constant pool
    @Name(value = "put field", language = "en")
    int PUTFIELD = 0xb5; // constant pool
    @Name(value = "invoke virtual", language = "en")
    int INVOKEVIRTUAL = 0xb6; // constant pool
    @Name(value = "invoke special", language = "en")
    int INVOKESPECIAL = 0xb7; // constant pool
    @Name(value = "invoke static", language = "en")
    int INVOKESTATIC = 0xb8; // constant pool
    @Name(value = "invoke interface", language = "en")
    int INVOKEINTERFACE = 0xb9; // constant pool, count, 0
    @Name(value = "invoke dynamic", language = "en")
    int INVOKEDYNAMIC = 0xba; // constant pool, 0, 0
    int NEW = 0xbb; // constant pool
    @Name(value = "new array", language = "en")
    int NEWARRAY = 0xbc;
    @Name(value = "reference new array", language = "en")
    int ANEWARRAY = 0xbd; // constant pool
    @Name(value = "array length", language = "en")
    int ARRAYLENGTH = 0xbe;
    @Name(value = "reference throw", language = "en")
    int ATHROW = 0xbf;
    @Name(value = "check cast", language = "en")
    int CHECKCAST = 0xc0; // constant pool
    int INSTANCEOF = 0xc1; // constant pool
    @Name(value = "monitor enter", language = "en")
    int MONITORENTER = 0xc2;
    @Name(value = "monitor exit", language = "en")
    int MONITOREXIT = 0xc3;

    int WIDE = 0xc4;
    int MULTIANEWARRAY = 0xc5; // constant pool, dimensions
    @Name(value = "if null", language = "en")
    int IFNULL = 0xc6;
    @Name(value = "if non null", language = "en")
    int IFNONNULL = 0xc7;
    @Name(value = "goto wide", language = "en")
    int GOTO_W = 0xc8;
    @Name(value = "jump subroutine wide", language = "en")
    int JSR_W = 0xc9;

    int BREAKPOINT = 0xca;

    String[] NAMES = {
            "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1", "iconst_2", "iconst_3", "iconst_4", "iconst_5",
            "lconst_0", "lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0", "dconst_1", "bipush", "sipush",
            "ldc", "ldc_w", "ldc2_w", "iload", "lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2",
            "iload_3", "lload_0", "lload_1", "lload_2", "lload_3", "fload_0", "fload_1", "fload_2", "fload_3",
            "dload_0", "dload_1", "dload_2", "dload_3", "aload_0", "aload_1", "aload_2", "aload_3", "iaload", "laload",
            "faload", "daload", "aaload", "baload", "caload", "saload", "istore", "lstore", "fstore", "dstore",
            "astore", "istore_0", "istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1", "lstore_2", "lstore_3",
            "fstore_0", "fstore_1", "fstore_2", "fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3", "astore_0",
            "astore_1", "astore_2", "astore_3", "iastore", "lastore", "fastore", "dastore", "aastore", "bastore",
            "castore", "sastore", "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1", "dup2_x2", "swap",
            "iadd", "ladd", "fadd", "dadd", "isub", "lsub", "fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv",
            "ldiv", "fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg", "fneg", "dneg", "ishl", "lshl",
            "ishr", "lshr", "iushr", "lushr", "iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f", "i2d",
            "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f", "i2b", "i2c", "i2s", "lcmp", "fcmpl",
            "fcmpg", "dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle", "if_icmpeq", "if_icmpne",
            "if_icmplt", "if_icmpge", "if_icmpgt", "if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret",
            "tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn", "dreturn", "areturn", "return", "getstatic",
            "putstatic", "getfield", "putfield", "invokevirtual", "invokespecial", "invokestatic", "invokeinterface",
            "invokedynamic", "new", "newarray", "anewarray", "arraylength", "athrow", "checkcast", "instanceof",
            "monitorenter", "monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull", "goto_w", "jsr_w",
            "breakpoint"
    };
}
