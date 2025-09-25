package mujica.ds.generic;

import mujica.reflect.modifier.CodeHistory;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Comparator;

@CodeHistory(date = "2025/5/24")
public class ComparableComparator<T> implements Comparator<T>, Serializable {

    private static final long serialVersionUID = 0x8f5279a18572c23fL;

    static final MethodHandle COMPARE_TO;

    static {
        try {
            COMPARE_TO = MethodHandles.lookup().findVirtual(Comparable.class, "compareTo", MethodType.methodType(int.class, Object.class));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("JavaLangInvokeHandleSignature")
    @Override
    public int compare(T a, T b) {
        try {
            return (int) COMPARE_TO.invoke(a, b);
        } catch (Throwable e) {
            RuntimeException re;
            if (e instanceof RuntimeException) {
                re = (RuntimeException) e;
            } else {
                re = new RuntimeException(e);
            }
            throw re;
        }
    }
}
