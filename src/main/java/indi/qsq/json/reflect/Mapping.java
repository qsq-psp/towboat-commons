package indi.qsq.json.reflect;

import indi.qsq.json.api.*;
import indi.qsq.json.io.IndexedColumn;
import indi.qsq.json.io.JsonStringWriter;
import indi.qsq.util.reflect.ClassUtility;
import indi.qsq.util.text.TypedString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created in infrastructure on 2021/12/30.
 * Recreated on 2022/6/3.
 */
class Mapping extends TypedString implements JsonStructure {

    private static final long serialVersionUID = 0xF952640A6A05D352L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Mapping.class);

    JsonType type;

    Setter write;

    Getter read;

    Object extra; // null, DatabaseColumn, CommandLineArgument, or Object[]

    Mapping(String name) {
        super(name);
    }

    String getName() {
        return string;
    }

    void collectExtra(@NotNull AnnotatedElement element) {
        DatabaseColumn databaseColumn1 = element.getAnnotation(DatabaseColumn.class);
        if (databaseColumn1 != null) {
            addExtra(databaseColumn1);
        } else {
            DatabaseColumnFallBack databaseColumnFallBack = element.getAnnotation(DatabaseColumnFallBack.class);
            if (databaseColumnFallBack != null) {
                for (DatabaseColumn databaseColumn2 : databaseColumnFallBack.value()) {
                    addExtra(databaseColumn2);
                }
            }
        }
        CommandLineArgument commandLineArgument = element.getAnnotation(CommandLineArgument.class);
        if (commandLineArgument != null) {
            addExtra(commandLineArgument);
        }
    }

    @SuppressWarnings("unchecked")
    void addExtra(Object item) {
        assert item != null;
        if (extra == null) {
            extra = item;
        } else if (extra instanceof ArrayList) {
            ((ArrayList<Object>) extra).add(item);
        } else {
            ArrayList<Object> list = new ArrayList<>();
            list.add(extra);
            list.add(item);
            extra = list;
        }
    }

    /**
     * @return true if need generic info
     */
    boolean collectAnnotation(@NotNull Class<?> clazz, @NotNull AnnotatedElement element, @NotNull ClosureConfig context) {
        if (type == null) {
            type = JsonTypeBuilder.get(clazz);
            if (type.linkCollectClass(clazz)) {
                return true;
            }
            type.linkSetConversionConfig(context);
        }
        type.linkCollectAnnotation(element, 0);
        return false;
    }

    void collectGenericAnnotation(Type genericType, AnnotatedElement element, ClosureConfig context) {
        type.linkCollectGenericType(genericType);
        type.linkSetConversionConfig(context);
        type.linkCollectAnnotation(element, 0);
    }

    void collectField(Field field, ClosureConfig context) {
        collectExtra(field);
        if (collectAnnotation(field.getType(), field, context)) {
            collectGenericAnnotation(field.getGenericType(), field, context);
        }
        if (write == null) {
            write = new FieldReflectSetter(field);
        }
        if (read == null) {
            read = new FieldReflectGetter(field);
        }
    }

    void collectSetter(Method method, ClosureConfig context) {
        collectExtra(method);
        if (collectAnnotation(method.getParameters()[0].getType(), method, context)) {
            collectGenericAnnotation(method.getGenericParameterTypes()[0], method, context);
        }
        if (write == null) {
            write = new MethodReflectSetter(method);
        }
    }

    void collectGetter(Method method, ClosureConfig context) {
        collectExtra(method);
        if (collectAnnotation(method.getReturnType(), method, context)) {
            collectGenericAnnotation(method.getGenericReturnType(), method, context);
        }
        if (read == null) {
            read = new MethodReflectGetter(method);
        }
    }

    void mergeFrom(Mapping that, Class<?> thatClass) {
        if (this.type == null) {
            LOGGER.debug("Merge type named {} from {}", getName(), thatClass);
            this.type = that.type;
        }
        if (this.write == null) {
            LOGGER.debug("Merge write setter named {} from {}", getName(), thatClass);
            this.write = that.write;
        }
        if (this.read == null) {
            LOGGER.debug("Merge read getter named {} from {}", getName(), thatClass);
            this.read = that.read;
        }
    }

    /**
     * @param lookup can be null if unreflect is not enabled
     */
    void finish(ClosureConfig context, MethodHandles.Lookup lookup) {
        if (lookup != null) {
            if (write != null) {
                write = write.unreflect(lookup);
            }
            if (read != null) {
                read = read.unreflect(lookup);
            }
        }
        if (type != null) {
            type = type.finishCollection();
        } else {
            type = new JsonType();
            type.setConversionConfig(context);
        }
        if (write == null) {
            write = Setter.INSTANCE;
        }
        if (read == null) {
            read = Getter.INSTANCE;
        }
        if (extra instanceof ArrayList) {
            extra = ((ArrayList<?>) extra).toArray();
        }
    }

    void serialize(Object self, JsonConsumer jc, JsonSerializer js) {
        type.serialize(this, read.invoke(self, null), jc, js);
    }

    ConverterFrame frame(Object self, boolean isObject, JsonConverter jv) {
        return type.frame(self, read, isObject, jv);
    }

    @NotNull
    Iterable<DatabaseColumn> getDatabaseColumns() {
        if (extra == null) {
            return List.of();
        } else if (extra instanceof DatabaseColumn) {
            return List.of((DatabaseColumn) extra);
        } else if (extra instanceof Object[]) {
            Object[] array = (Object[]) extra;
            ArrayList<DatabaseColumn> list = new ArrayList<>(array.length);
            for (Object item : array) {
                if (item instanceof DatabaseColumn) {
                    list.add((DatabaseColumn) item);
                }
            }
            return list;
        } else {
            return List.of();
        }
    }

    @Nullable
    CommandLineArgument getCommandLineArgument() {
        if (extra != null) {
            if (extra instanceof CommandLineArgument) {
                return (CommandLineArgument) extra;
            } else if (extra instanceof Object[]) {
                for (Object item : (Object[]) extra) {
                    if (item instanceof CommandLineArgument) {
                        return (CommandLineArgument) item;
                    }
                }
            }
        }
        return null;
    }

    /**
     * JDBC compatible
     */
    @Nullable
    IndexedColumn indexedColumn(ResultSet resultSet) {
        for (DatabaseColumn column : getDatabaseColumns()) {
            String columnLabel = column.name();
            if (columnLabel.isEmpty()) {
                columnLabel = getName();
            }
            try {
                return new IndexedColumn(this, column.type(), resultSet.findColumn(columnLabel));
            } catch (SQLException ignore) {}
        }
        return null;
    }

    void extraItemToJson(Object item, JsonConsumer jc) {
        jc.openObject();
        jc.key("class");
        if (item instanceof DatabaseColumn) {
            jc.stringValue("database-column");
            DatabaseColumn databaseColumn = (DatabaseColumn) item;
            jc.key("type");
            jc.stringValue(databaseColumn.type().name());
            jc.key("name");
            jc.stringValue(databaseColumn.name());
        } else if (item instanceof CommandLineArgument) {
            jc.stringValue("command-line-argument");
            CommandLineArgument commandLineArgument = (CommandLineArgument) item;
            jc.key("index");
            jc.numberValue(commandLineArgument.index());
            jc.key("key");
            jc.stringValue(commandLineArgument.key());
            jc.key("aggregate");
            jc.booleanValue(commandLineArgument.aggregate());
        } else {
            jc.stringValue(ClassUtility.simple(item));
        }
        jc.closeObject();
    }

    @Override
    public void toJson(JsonConsumer jc) {
        jc.openObject();
        jc.key("name");
        jc.stringValue(getName());
        jc.key("type");
        if (type != null) {
            type.toJson(jc);
        } else {
            jc.nullValue();
        }
        jc.key("write");
        if (write != null) {
            write.toJson(jc);
        } else {
            jc.nullValue();
        }
        jc.key("read");
        if (read != null) {
            read.toJson(jc);
        } else {
            jc.nullValue();
        }
        if (extra != null) {
            jc.key("extra");
            if (extra instanceof Object[]) {
                Object[] array = (Object[]) extra;
                jc.openArray();
                for (Object item : array) {
                    extraItemToJson(item, jc);
                }
                jc.closeArray();
            } else {
                extraItemToJson(extra, jc);
            }
        }
        jc.closeObject();
    }

    @Override
    public String toString() {
        return JsonStringWriter.stringify("Mapping", this, null);
    }
}
