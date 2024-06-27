package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonStructure;
import indi.qsq.json.io.ResultSetReader;
import indi.qsq.util.reflect.ClassUtility;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created on 2022/6/10.
 */
public abstract class JsonConverter {

    public Object passedObject;

    public Object thisObject;

    public JsonConverter() {
        super();
    }

    public boolean logEnabled() {
        return true;
    }

    public int operation() {
        return 0;
    }

    public void stringify(StringBuilder sb) {
        sb.append("passed = ").append(ClassUtility.nearlyFull(passedObject));
        sb.append(", this = ").append(ClassUtility.nearlyFull(thisObject));
    }

    public String stringify() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        stringify(sb);
        sb.append(']');
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append('[');
        stringify(sb);
        sb.append(']');
        return sb.toString();
    }

    public static ResultSetReader resultSetReader(ResultSet resultSet, Class<?> clazz) {
        return new ResultSetReader(resultSet, ReflectClass.get(clazz).indexedColumnList(resultSet));
    }

    public static JsonStructure query(String className) throws ReflectiveOperationException {
        return ReflectClass.query(Class.forName(className));
    }

    public static ArrayList<Class<?>> queryClasses() {
        return ReflectClass.queryClasses();
    }
}
