package mujica.reflect.bytecode;

import mujica.json.entity.JsonArray;
import mujica.math.algebra.random.RandomContext;
import mujica.math.geometry.GeometryOperationResult;
import mujica.math.geometry.g2d.Point;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.number.OrdinalTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * Created on 2025/9/28.
 */
@CodeHistory(date = "2025/9/28")
public class ProjectClassLoaderTest {

    private static final ProjectClassLoader LOADER = new ProjectClassLoader();

    @NotNull
    private Class<?> assertProject(@NotNull Class<?> classA) throws Exception {
        Assert.assertFalse(classA.getClassLoader() instanceof ProjectClassLoader);
        final Class<?> classB = LOADER.loadClass(classA.getName());
        Assert.assertNotSame(classA, classB);
        Assert.assertEquals(classA.getName(), classB.getName());
        Assert.assertSame(LOADER, classB.getClassLoader());
        return classB;
    }

    @Test
    public void caseProject1() throws Exception {
        final Class<?> randomContextClass = assertProject(RandomContext.class);
        final Object randomContextObject = randomContextClass.getConstructor().newInstance();
        final Object bigIntegerObject = randomContextClass.getDeclaredMethod("nextBigInteger", int.class).invoke(randomContextObject, 100);
        Assert.assertTrue(bigIntegerObject instanceof BigInteger);
        Assert.assertFalse(bigIntegerObject.getClass().getClassLoader() instanceof ProjectClassLoader);
        final Object shuffleComparatorObject = randomContextClass.getDeclaredMethod("shuffleComparator").invoke(randomContextObject);
        Assert.assertTrue(shuffleComparatorObject instanceof Comparator<?>);
        Assert.assertSame(LOADER, shuffleComparatorObject.getClass().getClassLoader());
    }

    @Test
    public void caseProject2() throws Exception {
        final Class<?> pointClass = assertProject(Point.class);
        final Object pointObject = pointClass.getConstructor().newInstance();
        final String string = pointObject.toString();
        Assert.assertFalse(string.getClass().getClassLoader() instanceof ProjectClassLoader);
        final Object geometryOperationResultObject = pointClass.getDeclaredMethod("invalidate").invoke(pointObject);
        Assert.assertFalse(geometryOperationResultObject instanceof GeometryOperationResult);
        Assert.assertSame(LOADER, geometryOperationResultObject.getClass().getClassLoader());
    }

    @Test
    public void caseProject3() throws Exception {
        final Class<?> jsonArrayClass = assertProject(JsonArray.class);
        final Class<?> truncateListClass = jsonArrayClass.getSuperclass();
        Assert.assertSame(LOADER, truncateListClass.getClassLoader());
        final Class<?> arrayListClass = truncateListClass.getSuperclass();
        Assert.assertFalse(arrayListClass.getClassLoader() instanceof ProjectClassLoader);
    }

    @Test
    public void caseProject4() throws Exception {
        final Class<?> ordinalTestClass = assertProject(OrdinalTest.class);
        final Object ordinalTestObject = ordinalTestClass.getConstructor().newInstance();
        ordinalTestClass.getDeclaredMethod("caseInt").invoke(ordinalTestObject);
        ordinalTestClass.getDeclaredMethod("caseLong").invoke(ordinalTestObject);
    }

    private void assertLibrary(@NotNull Class<?> classA) throws ClassNotFoundException {
        Assert.assertFalse(classA.getClassLoader() instanceof ProjectClassLoader);
        final Class<?> classB = LOADER.loadClass(classA.getName());
        Assert.assertFalse(classB.getClassLoader() instanceof ProjectClassLoader);
        Assert.assertSame(classA, classB);
    }

    @Test
    public void caseLibrary() throws ClassNotFoundException {
        assertLibrary(Float.class);
        assertLibrary(Math.class);
        assertLibrary(IOException.class);
        assertLibrary(Runtime.Version.class);
        assertLibrary(ByteBuffer.class);
        assertLibrary(NotNull.class);
        assertLibrary(Test.class);
    }
}
