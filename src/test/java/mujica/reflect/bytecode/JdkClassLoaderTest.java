package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created on 2025/10/28.
 */
@CodeHistory(date = "2023/5/14", project = "nettyon", name = "ResourcesUrl")
@CodeHistory(date = "2025/10/28")
@SuppressWarnings("SpellCheckingInspection")
public class JdkClassLoaderTest {

    @Test
    public void testPlatformClassLoader() {
        final ClassLoader classLoader = ClassLoader.getPlatformClassLoader();
        Assert.assertNotNull(classLoader.getResource("java/lang/StringBuilder.class"));
        Assert.assertNotNull(classLoader.getResource("java/io/File.class"));
        Assert.assertNotNull(classLoader.getResource("java/util/JumboEnumSet.class")); // package
        Assert.assertNotNull(classLoader.getResource("jdk/nio/zipfs/ZipFileSystemProvider.class"));
        Assert.assertNotNull(classLoader.getResource("com/sun/crypto/provider/BlowfishCipher.class"));
        Assert.assertNull(classLoader.getResource("mujica/reflect/bytecode/AttributeInfo.class"));
        Assert.assertNull(classLoader.getResource("mujica/reflect/bytecode/ProjectClassLoaderTest.class"));
        Assert.assertNull(classLoader.getResource("mujica/math/algebra/random/RandomSource.class"));
        Assert.assertNull(classLoader.getResource("mujica/math/algebra/random/FuzzyContextTest.class"));
        Assert.assertNull(classLoader.getResource("junit/framework/TestCase.class"));
        Assert.assertNotNull(classLoader.getResource("module-info.class"));
        Assert.assertNull(classLoader.getResource("java/time/chrono/hijrah-config-islamic-umalqura.properties"));
        Assert.assertNull(classLoader.getResource("java/util/currency.data"));
    }

    @Test
    public void testSystemClassLoader() {
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Assert.assertNotNull(classLoader.getResource("java/lang/StringBuilder.class"));
        Assert.assertNotNull(classLoader.getResource("java/io/File.class"));
        Assert.assertNotNull(classLoader.getResource("java/util/JumboEnumSet.class")); // package
        Assert.assertNotNull(classLoader.getResource("javax/annotation/processing/AbstractProcessor.class"));
        Assert.assertNotNull(classLoader.getResource("jdk/nio/zipfs/ZipFileSystemProvider.class"));
        Assert.assertNotNull(classLoader.getResource("com/sun/crypto/provider/BlowfishCipher.class"));
        Assert.assertNotNull(classLoader.getResource("mujica/reflect/bytecode/AttributeInfo.class"));
        Assert.assertNotNull(classLoader.getResource("mujica/reflect/bytecode/ProjectClassLoaderTest.class"));
        Assert.assertNotNull(classLoader.getResource("mujica/math/algebra/random/RandomSource.class"));
        Assert.assertNotNull(classLoader.getResource("mujica/math/algebra/random/FuzzyContextTest.class"));
        Assert.assertNotNull(classLoader.getResource("junit/framework/TestCase.class"));
        Assert.assertNotNull(classLoader.getResource("module-info.class"));
        Assert.assertNull(classLoader.getResource("java/lang/uniName.dat"));
        Assert.assertNull(classLoader.getResource("java/util/currency.data"));
    }
}
