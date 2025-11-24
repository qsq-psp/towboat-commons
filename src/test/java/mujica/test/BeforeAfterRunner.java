package mujica.test;

import mujica.ds.generic.list.TruncateList;
import mujica.reflect.basic.ClassUtil;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runner.Runner;

import java.lang.reflect.Method;

@CodeHistory(date = "2025/11/16")
public abstract class BeforeAfterRunner extends Runner {

    final TruncateList<Method> beforeClass = new TruncateList<>();

    final TruncateList<Method> before = new TruncateList<>();

    final TruncateList<Method> after = new TruncateList<>();

    final TruncateList<Method> afterClass = new TruncateList<>();

    protected BeforeAfterRunner(@NotNull Class<?> clazz) {
        super();
        ClassUtil.allMethods(clazz, method -> {
            if (method.getAnnotation(BeforeClass.class) != null) {
                beforeClass.add(method);
            }
            if (method.getAnnotation(Before.class) != null) {
                before.add(method);
            }
            if (method.getAnnotation(After.class) != null) {
                after.add(method);
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                afterClass.add(method);
            }
        }, false, false);
        beforeClass.reverse();
        before.reverse();
    }

    @NotNull
    protected static ReflectiveCallable reflectiveCallable(@NotNull Method staticRunMethod) {
        return new ReflectiveCallable() {
            @Override
            protected Object runReflectiveCall() throws Throwable {
                return staticRunMethod.invoke(null);
            }
        };
    }

    protected void runBeforeClass() throws Throwable {
        for (Method method : beforeClass) {
            reflectiveCallable(method).run();
        }
    }

    protected void runBefore() throws Throwable {
        for (Method method : before) {
            reflectiveCallable(method).run();
        }
    }

    protected void runAfter() throws Throwable {
        for (Method method : after) {
            reflectiveCallable(method).run();
        }
    }

    protected void runAfterClass() throws Throwable {
        for (Method method : afterClass) {
            reflectiveCallable(method).run();
        }
    }
}
