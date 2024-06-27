package indi.um.json.reflect;

import indi.um.json.api.CallBuilder;
import indi.um.json.api.FrameStructure;
import indi.um.json.api.JsonConsumer;
import indi.um.json.api.UseBuilder;
import indi.um.json.entity.JsonConstant;
import indi.um.util.value.PublicBooleanSlot;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created on 2022/7/23.
 */
class JsonObjectType extends JsonType {

    private static final long serialVersionUID = 0xB0B855E077AD6F57L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonObjectType.class);

    UseBuilder use = UseBuilder.IF_NULL;

    transient Getter build; // Getter is not Serializable, but it is JsonStructure

    JsonObjectType() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public JsonObjectType clone() {
        final JsonObjectType that = new JsonObjectType();
        that.setJsonType(this);
        return that;
    }

    /**
     * @param clazz may be null
     * @return true if need generic info
     */
    @Override
    protected boolean linkCollectClass(Class<?> clazz) {
        if (clazz != null) {
            build = new ClassHeldGetter(clazz);
        }
        return false;
    }

    @Override
    protected void collectAnnotation(@NotNull AnnotatedElement element, int layer) {
        final CallBuilder callBuilder = element.getAnnotation(CallBuilder.class);
        if (callBuilder != null && dereference(callBuilder.value(), layer)) {
            use = callBuilder.when();
        }
        if (build instanceof ClassHeldGetter) {
            Class<?> clazz = ((ClassHeldGetter) build).clazz;
            build = Getter.INSTANCE;
            if (((Modifier.PUBLIC | Modifier.ABSTRACT) & clazz.getModifiers()) == Modifier.PUBLIC) { // public and not abstract class
                try {
                    build = new ConstructorReflectGetter(clazz.getConstructor());
                } catch (ReflectiveOperationException ignore) { }
            }
        }
    }

    boolean dereference(String reference, int layer) {
        if (layer > 0) {
            String expectedSuffix = "[]".repeat(layer);
            if (reference.endsWith(expectedSuffix)) { // test less layers
                reference = reference.substring(0, reference.length() - expectedSuffix.length());
            } else {
                return false;
            }
        }
        if (reference.endsWith("[]")) { // test more layers
            return false;
        }
        int div = reference.lastIndexOf('(');
        if (div == -1) {
            div = reference.lastIndexOf('.');
        } else {
            div = reference.lastIndexOf('.', div);
        }
        if (div == -1) {
            return true;
        }
        try {
            Class<?> clazz = Class.forName(reference.substring(0, div).trim());
            reference = reference.substring(div + 1);
            div = reference.indexOf('(');
            if (div == -1) {
                Field field = clazz.getField(reference.trim());
                if (((Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL) & field.getModifiers()) == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) {
                    build = new FieldReflectGetter(field);
                } else {
                    LOGGER.warn("Field {}", field);
                }
            } else {
                String invocationName = reference.substring(0, div).trim();
                reference = reference.substring(div + 1);
                div = reference.indexOf(')');
                if (div == -1) {
                    LOGGER.warn("Round brackets not paired");
                    return true;
                }
                reference = reference.substring(0, div).trim();
                Class<?> parameterType;
                if (reference.isEmpty()) {
                    parameterType = null;
                } else if (JsonConverter.class.getName().equals(reference) || JsonConverter.class.getSimpleName().equals(reference)) {
                    parameterType = JsonConverter.class;
                } else if (JsonParser.class.getName().equals(reference) || JsonParser.class.getSimpleName().equals(reference)) {
                    parameterType = JsonParser.class;
                } else {
                    LOGGER.warn("Bad parameter type {}", reference);
                    return true;
                }
                if ("new".equals(invocationName) || "<init>".equals(invocationName)) { // constructor
                    if (parameterType == null) {
                        build = new ConstructorReflectGetter(clazz.getConstructor());
                    } else {
                        build = new ConstructorReflectGetter(clazz.getConstructor(parameterType));
                    }
                } else { // ordinary method
                    if (parameterType == null) {
                        build = new MethodReflectGetter(clazz.getMethod(invocationName));
                    } else {
                        build = new MethodReflectGetter.WithParserParameter(clazz.getMethod(invocationName, parameterType));
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            LOGGER.warn("", e);
        }
        return true;
    }

    @Override
    protected JsonType finishCollection() {
        return super.finishCollection();
    }

    @Override
    protected ConverterFrame frame(Object self, Getter read, boolean isObject, JsonConverter jv) {
        final PublicBooleanSlot modified = new PublicBooleanSlot();
        final Object value = useValue(self, read, modified, jv);
        if (value instanceof FrameStructure) {
            return ((FrameStructure) value).frame(isObject, jv);
        } else if (value == null || value instanceof JsonConstant) {
            return ConverterFrame.INSTANCE; // error happens
        } else {
            return ReflectClass.get(value.getClass()).frame(value, jv);
        }
    }

    Object useValue(Object self, Getter read, PublicBooleanSlot modified, JsonConverter jv) {
        Object value;
        if (self != null) {
            switch (use) {
                case NEVER:
                    value = read.invoke(self, null);
                    if (value instanceof JsonConstant) {
                        value = null;
                        modified.value = true;
                    }
                    break;
                case IF_NULL:
                    value = read.invoke(self, null);
                    if (value == null) {
                        value = build.invoke(self, jv);
                        modified.value = true;
                    } else if (value instanceof JsonConstant) {
                        value = null;
                        modified.value = true;
                    }
                    break;
                case IF_NULL_OR_ERROR:
                    value = read.invoke(self, null);
                    if (value == null || value instanceof JsonConstant) {
                        value = build.invoke(self, jv);
                        modified.value = true;
                    }
                    break;
                case IF_ERROR:
                    value = read.invoke(self, null);
                    if (value instanceof JsonConstant) { // read method returns JsonConstant if error happens
                        value = build.invoke(self, jv); // build Getter also returns JsonConstant if error happens
                        modified.value = true;
                    }
                    break;
                case ALWAYS:
                    value = build.invoke(self, jv);
                    modified.value = true;
                    break;
                default:
                    value = null;
                    modified.value = true;
                    break;
            }
        } else {
            switch (use) {
                case IF_NULL:
                case IF_NULL_OR_ERROR:
                case ALWAYS:
                    value = build.invoke(null, jv);
                    break;
                default:
                    value = null;
                    break;
            }
            modified.value = true;
        }
        return value;
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        jc.key("build");
        if (build != null) {
            build.toJson(jc);
        } else {
            jc.nullValue();
        }
    }

    @Override
    public String typeName() {
        return "object";
    }
}
