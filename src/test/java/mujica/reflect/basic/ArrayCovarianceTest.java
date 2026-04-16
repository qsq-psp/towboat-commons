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
    public void caseByte0() {
        Assert.assertNull(byte.class.getSuperclass());
        Assert.assertNotNull(Byte.class.getSuperclass());
    }

    @Test
    public void caseByte1() {
        final Object target = new byte[4];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(byte.class, target.getClass().getComponentType());
        Assert.assertFalse(target instanceof Object[]);
        Assert.assertFalse(target instanceof Byte[]);
    }

    @Test
    public void caseByte2() {
        final Object target = new byte[3][3];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(byte[].class, target.getClass().getComponentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertFalse(target instanceof Object[][]);
    }

    @Test
    public void caseByte3() {
        final Object target = new byte[2][2][2];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(byte[][].class, target.getClass().getComponentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertFalse(target instanceof Object[][][]);
    }

    @Test
    public void caseInt0() {
        Assert.assertNull(int.class.getSuperclass());
        Assert.assertNotNull(Integer.class.getSuperclass());
    }

    @Test
    public void caseInt1() {
        final Object target = new int[4];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(int.class, target.getClass().getComponentType());
        Assert.assertFalse(target instanceof Object[]);
        Assert.assertFalse(target instanceof Integer[]);
    }

    @Test
    public void caseInt2() {
        final Object target = new int[3][3];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(int[].class, target.getClass().getComponentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertFalse(target instanceof Object[][]);
    }

    @Test
    public void caseInt3() {
        final Object target = new int[2][2][2];
        Assert.assertEquals(Object.class, target.getClass().getSuperclass());
        Assert.assertEquals(int[][].class, target.getClass().getComponentType());
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertFalse(target instanceof Object[][][]);
    }

    @Test
    public void caseVoid1() {
        final Void[] target = new Void[17];
        Assert.assertTrue(target instanceof Object[]);
    }

    @Test
    public void caseFile1() {
        final Object target = new File[22];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Serializable[]);
    }

    @Test
    public void caseClass1() {
        final Object target = new Class<?>[36];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Type[]);
    }

    @Test
    public void caseObject2() {
        final Object target = new Object[3][3];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
    }

    @Test
    public void caseBigInteger2() {
        final Object target = new BigInteger[3][3];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertFalse(target instanceof BigInteger[]);
        Assert.assertTrue(target instanceof BigInteger[][]);
    }

    @Test
    public void caseObject3() {
        final Object target = new Object[2][2][2];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertTrue(target instanceof Object[][][]);
    }

    @Test
    public void caseRunnable3() {
        final Object target = new Runnable[2][2][2];
        Assert.assertTrue(target instanceof Object[]);
        Assert.assertTrue(target instanceof Object[][]);
        Assert.assertTrue(target instanceof Object[][][]);
        Assert.assertFalse(target instanceof Runnable[]);
        Assert.assertFalse(target instanceof Runnable[][]);
        Assert.assertTrue(target instanceof Runnable[][][]);
    }

    @Test
    public void caseString2() {
        final Object full = new String[3][3];
        final Object half = new String[3][];
        Assert.assertEquals(full.getClass(), half.getClass());
    }

    @Test
    public void caseException3() {
        final Object full = new Exception[2][2][2];
        final Object more = new Exception[2][2][];
        final Object less = new Exception[2][][];
        Assert.assertEquals(full.getClass(), more.getClass());
        Assert.assertEquals(full.getClass(), less.getClass());
    }
}
