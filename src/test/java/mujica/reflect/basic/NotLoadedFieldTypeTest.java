package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.annotation.Target;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.TypeDescriptor;
import java.lang.invoke.VarHandle;
import java.lang.reflect.TypeVariable;
import java.math.MathContext;
import java.nio.file.WatchEvent;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

@CodeHistory(date = "2025/9/15")
public class NotLoadedFieldTypeTest {

    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();

    @Test
    public void testPrimitive() {
        final SourceFieldType sourceFieldType = SourceFieldType.of(byte.class);
        final BytecodeFieldType bytecodeFieldType = BytecodeFieldType.of(Byte.TYPE); // also the primitive
        sourceFieldType.checkHealth(errorCollector::addError);
        bytecodeFieldType.checkHealth(errorCollector::addError);
        Assert.assertTrue(sourceFieldType.isPrimitive());
        Assert.assertTrue(bytecodeFieldType.isPrimitive());
        Assert.assertFalse(sourceFieldType.isArray());
        Assert.assertFalse(bytecodeFieldType.isArray());
        Assert.assertNull(sourceFieldType.componentType());
        Assert.assertNull(bytecodeFieldType.componentType());
        Assert.assertEquals("byte", sourceFieldType.toSourceString());
        Assert.assertEquals("byte", bytecodeFieldType.toSourceString());
        Assert.assertEquals("B", sourceFieldType.toBytecodeString());
        Assert.assertEquals("B", bytecodeFieldType.toBytecodeString());
        Assert.assertEquals(sourceFieldType, bytecodeFieldType.toSourceFieldType());
        Assert.assertEquals(bytecodeFieldType, sourceFieldType.toBytecodeFieldType());
    }

    private static final double[] ARRAY1 = new double[0];

    @Test
    public void testPrimitiveArray1() {
        final SourceFieldType sourceFieldType = SourceFieldType.of(double[].class);
        final BytecodeFieldType bytecodeFieldType = BytecodeFieldType.of(ARRAY1.getClass());
        sourceFieldType.checkHealth(errorCollector::addError);
        bytecodeFieldType.checkHealth(errorCollector::addError);
        Assert.assertFalse(sourceFieldType.isPrimitive());
        Assert.assertFalse(bytecodeFieldType.isPrimitive());
        Assert.assertTrue(sourceFieldType.isArray());
        Assert.assertTrue(bytecodeFieldType.isArray());
        Assert.assertEquals(SourceFieldType.of(double.class), sourceFieldType.componentType());
        Assert.assertEquals(BytecodeFieldType.of(Double.TYPE), bytecodeFieldType.componentType());
        Assert.assertEquals("double[]", sourceFieldType.toSourceString());
        Assert.assertEquals("double[]", bytecodeFieldType.toSourceString());
        Assert.assertEquals("[D", sourceFieldType.toBytecodeString());
        Assert.assertEquals("[D", bytecodeFieldType.toBytecodeString());
        Assert.assertEquals(sourceFieldType, bytecodeFieldType.toSourceFieldType());
        Assert.assertEquals(bytecodeFieldType, sourceFieldType.toBytecodeFieldType());
    }

    private static final int[][] ARRAY2 = new int[1][1];

    @Test
    public void testPrimitiveArray2() {
        final SourceFieldType sourceFieldType = SourceFieldType.of(int[][].class);
        final BytecodeFieldType bytecodeFieldType = BytecodeFieldType.of(ARRAY2.getClass());
        sourceFieldType.checkHealth(errorCollector::addError);
        bytecodeFieldType.checkHealth(errorCollector::addError);
        Assert.assertFalse(sourceFieldType.isPrimitive());
        Assert.assertFalse(bytecodeFieldType.isPrimitive());
        Assert.assertTrue(sourceFieldType.isArray());
        Assert.assertTrue(bytecodeFieldType.isArray());
        Assert.assertEquals(SourceFieldType.of(int[].class), sourceFieldType.componentType());
        Assert.assertEquals(BytecodeFieldType.of(Integer.TYPE).arrayType(), bytecodeFieldType.componentType());
        Assert.assertEquals("int[][]", sourceFieldType.toSourceString());
        Assert.assertEquals("int[][]", bytecodeFieldType.toSourceString());
        Assert.assertEquals("[[I", sourceFieldType.toBytecodeString());
        Assert.assertEquals("[[I", bytecodeFieldType.toBytecodeString());
        Assert.assertEquals(sourceFieldType, bytecodeFieldType.toSourceFieldType());
        Assert.assertEquals(bytecodeFieldType, sourceFieldType.toBytecodeFieldType());
    }

    private static final boolean[][][] ARRAY3 = new boolean[2][2][2];

    @Test
    public void testPrimitiveArray3() {
        final SourceFieldType sourceFieldType = SourceFieldType.of(boolean[][][].class);
        final BytecodeFieldType bytecodeFieldType = BytecodeFieldType.of(ARRAY3.getClass());
        sourceFieldType.checkHealth(errorCollector::addError);
        bytecodeFieldType.checkHealth(errorCollector::addError);
        Assert.assertFalse(sourceFieldType.isPrimitive());
        Assert.assertFalse(bytecodeFieldType.isPrimitive());
        Assert.assertTrue(sourceFieldType.isArray());
        Assert.assertTrue(bytecodeFieldType.isArray());
        Assert.assertEquals(SourceFieldType.of(boolean[][].class), sourceFieldType.componentType());
        Assert.assertEquals(BytecodeFieldType.of(Boolean.TYPE).arrayType().arrayType(), bytecodeFieldType.componentType());
        Assert.assertEquals("boolean[][][]", sourceFieldType.toSourceString());
        Assert.assertEquals("boolean[][][]", bytecodeFieldType.toSourceString());
        Assert.assertEquals("[[[Z", sourceFieldType.toBytecodeString());
        Assert.assertEquals("[[[Z", bytecodeFieldType.toBytecodeString());
        Assert.assertEquals(sourceFieldType, bytecodeFieldType.toSourceFieldType());
        Assert.assertEquals(bytecodeFieldType, sourceFieldType.toBytecodeFieldType());
    }

    @Test
    public void testVoid() {
        final SourceFieldType sourceFieldType = SourceFieldType.of(void.class);
        final BytecodeFieldType bytecodeFieldType = BytecodeFieldType.of(Void.TYPE);
        sourceFieldType.checkReturnTypeHealth(errorCollector::addError);
        bytecodeFieldType.checkReturnTypeHealth(errorCollector::addError);
        Assert.assertTrue(sourceFieldType.isPrimitive()); // void is also primitive
        Assert.assertTrue(bytecodeFieldType.isPrimitive()); // void is also primitive
        Assert.assertFalse(sourceFieldType.isArray());
        Assert.assertFalse(bytecodeFieldType.isArray());
        Assert.assertNull(sourceFieldType.componentType());
        Assert.assertNull(bytecodeFieldType.componentType());
        Assert.assertEquals("void", sourceFieldType.toSourceString());
        Assert.assertEquals("void", bytecodeFieldType.toSourceString());
        Assert.assertEquals("V", sourceFieldType.toBytecodeString());
        Assert.assertEquals("V", bytecodeFieldType.toBytecodeString());
        Assert.assertEquals(sourceFieldType, bytecodeFieldType.toSourceFieldType());
        Assert.assertEquals(bytecodeFieldType, sourceFieldType.toBytecodeFieldType());
    }

    private static final Class<?>[] OUTER = {
            InputStreamReader.class,
            Target.class,
            VarHandle.class,
            TypeVariable[].class,
            Character[].class,
            Process.class,
            MathContext.class,
            Comparator.class,
            Locale.class
    };

    @Test
    public void testOuter() {
        for (Class<?> clazz : OUTER) {
            SourceFieldType sourceFieldType = SourceFieldType.of(clazz);
            BytecodeFieldType bytecodeFieldType = BytecodeFieldType.of(clazz);
            sourceFieldType.checkHealth(errorCollector::addError);
            bytecodeFieldType.checkHealth(errorCollector::addError);
            Assert.assertFalse(sourceFieldType.isPrimitive());
            Assert.assertFalse(bytecodeFieldType.isPrimitive());
            Assert.assertEquals(clazz.getName(), sourceFieldType.toClassGetName());
            Assert.assertEquals(clazz.getName(), bytecodeFieldType.toClassGetName());
            Assert.assertEquals(sourceFieldType, bytecodeFieldType.toSourceFieldType());
            Assert.assertEquals(bytecodeFieldType, sourceFieldType.toBytecodeFieldType());
        }
    }

    private static final Class<?>[] INNER = {
            Character.Subset.class,
            Thread.State.class,
            Map.Entry[].class,
            MethodHandles.Lookup.class,
            WatchEvent.Kind.class,
            ProcessBuilder.Redirect.Type[].class,
            TypeDescriptor.OfMethod.class,
            ObjectOutputStream.PutField.class
    };

    @Test
    public void testInner() {
        for (Class<?> clazz : INNER) {
            SourceFieldType sourceFieldType = SourceFieldType.of(clazz);
            BytecodeFieldType bytecodeFieldType = BytecodeFieldType.of(clazz);
            sourceFieldType.checkHealth(errorCollector::addError);
            bytecodeFieldType.checkHealth(errorCollector::addError);
            Assert.assertFalse(sourceFieldType.isPrimitive());
            Assert.assertFalse(bytecodeFieldType.isPrimitive());
            Assert.assertEquals(clazz.getName(), sourceFieldType.toClassGetName());
            Assert.assertEquals(clazz.getName(), bytecodeFieldType.toClassGetName());
            Assert.assertEquals(-1, sourceFieldType.toRealSourceString().indexOf("$"));
            Assert.assertEquals(-1, bytecodeFieldType.toRealSourceString().indexOf("$"));
            Assert.assertNotEquals(-1, sourceFieldType.toBytecodeString().indexOf("$"));
            Assert.assertNotEquals(-1, bytecodeFieldType.toBytecodeString().indexOf("$"));
            Assert.assertEquals(sourceFieldType, bytecodeFieldType.toSourceFieldType());
            Assert.assertEquals(bytecodeFieldType, sourceFieldType.toBytecodeFieldType());
        }
    }
}
