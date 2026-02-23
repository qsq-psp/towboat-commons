package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigInteger;

@CodeHistory(date = "2022/7/23", project = "LeetCode", name = "ObjectArray")
@CodeHistory(date = "2022/10/4", project = "nettyon")
@CodeHistory(date = "2026/2/9")
@SuppressWarnings("ConstantConditions")
public class ArrayCovarianceTest {

    @Test
    public void testByte0() {
        Assert.assertNull(byte.class.getSuperclass());
        Assert.assertNotNull(Byte.class.getSuperclass());
    }

    @Test
    public void testByte1() {
        final Object target = new byte[4];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(byte.class, target.getClass().componentType());
        Assert.assertFalse(target instanceof Object[]);
        Assert.assertFalse(target instanceof Byte[]);
    }

    @Test
    public void testByte2() {
        final Object target = new byte[3][3];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(byte[].class, target.getClass().componentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertFalse(target instanceof Object[][]);
    }

    @Test
    public void testByte3() {
        final Object target = new byte[2][2][2];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(byte[][].class, target.getClass().componentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertFalse(target instanceof Object[][][]);
    }

    @Test
    public void testInt0() {
        Assert.assertNull(int.class.getSuperclass());
        Assert.assertNotNull(Integer.class.getSuperclass());
    }

    @Test
    public void testInt1() {
        final Object target = new int[4];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(int.class, target.getClass().componentType());
        Assert.assertFalse(target instanceof Object[]);
        Assert.assertFalse(target instanceof Integer[]);
    }

    @Test
    public void testInt2() {
        final Object target = new int[3][3];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(int[].class, target.getClass().componentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertFalse(target instanceof Object[][]);
    }

    @Test
    public void testInt3() {
        final Object target = new int[2][2][2];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(int[][].class, target.getClass().componentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertFalse(target instanceof Object[][][]);
    }

    @Test
    public void testVoid1() {
        final Void[] target = new Void[17];
        Assert.assertTrue(target instanceof Object[]);
    }

    @Test
    public void testFile1() {
        final Object target = new File[22];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Serializable[]);
    }

    @Test
    public void testClass1() {
        final Object target = new Class<?>[36];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Type[]);
    }

    @Test
    public void testObject2() {
        final Object target = new Object[3][3];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
    }

    @Test
    public void testBigInteger2() {
        final Object target = new BigInteger[3][3];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertFalse(target instanceof BigInteger[]);
        Assert.assertTrue(target instanceof BigInteger[][]);
    }

    @Test
    public void testObject3() {
        final Object target = new Object[2][2][2];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertTrue(target instanceof Object[][][]);
    }

    @Test
    public void testRunnable3() {
        final Object target = new Runnable[2][2][2];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertTrue(target instanceof Object[][][]);
        Assert.assertFalse(target instanceof Runnable[]);
        Assert.assertFalse(target instanceof Runnable[][]);
        Assert.assertTrue(target instanceof Runnable[][][]);
    }

    @Test
    public void testString2() {
        final Object full = new String[3][3];
        final Object half = new String[3][];
        Assert.assertEquals(full.getClass(), half.getClass());
    }

    @Test
    public void testException3() {
        final Object full = new Exception[2][2][2];
        final Object more = new Exception[2][2][];
        final Object less = new Exception[2][][];
        Assert.assertEquals(full.getClass(), more.getClass());
        Assert.assertEquals(full.getClass(), less.getClass());
    }
}
