package mujica.reflect.bytecode;

import mujica.ds.generic.heap.HeapTest;
import mujica.io.codec.Base16Charset;
import mujica.math.geometry.g2d.Bound;
import mujica.reflect.basic.TypeUtilTest;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.format.FormatValidatorTest;
import mujica.text.number.RomanTest;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.function.UnaryOperator;

/**
 * Created on 2025/9/29.
 */
@CodeHistory(date = "2025/9/29")
public class RewriteClassLoaderTest {

    private void callTest(@NotNull ClassLoader loader, @NotNull Class<?> testClass, @NotNull String methodName) throws Exception {
        testClass = loader.loadClass(testClass.getName());
        Object testInstance = testClass.getConstructor().newInstance();
        testClass.getDeclaredMethod(methodName).invoke(testInstance);
    }

    @SuppressWarnings("JavaReflectionInvocation")
    private void assertLoader(@NotNull ClassLoader loader) throws Exception {
        {
            Class<?> base16CharsetClass = loader.loadClass(Base16Charset.class.getName());
            Object base16CharsetObject = base16CharsetClass.getConstructor(boolean.class).newInstance(false);
            Assert.assertEquals(
                    Boolean.TRUE,
                    base16CharsetClass.getDeclaredMethod("contains", Charset.class).invoke(base16CharsetObject, base16CharsetObject)
            );
            Assert.assertNotNull(
                    base16CharsetClass.getDeclaredMethod("newDecoder").invoke(base16CharsetObject)
            );
        }
        {
            Class<?> boundClass = loader.loadClass(Bound.class.getName());
            Object boundObject = boundClass.getConstructor().newInstance();
            boundClass.getDeclaredMethod("includePoint", double.class, double.class).invoke(boundObject, 1.0, 1.0);
            boundClass.getDeclaredMethod("includeCircle", double.class, double.class, double.class).invoke(boundObject, 4.0, 4.0, 2.0);
            Assert.assertEquals(
                    Boolean.TRUE,
                    boundClass.getDeclaredMethod("intersects", boundClass, boundClass).invoke(null, boundObject, boundObject)
            );
            Assert.assertNotNull(boundObject.toString());
        }
        callTest(loader, HeapTest.class, "checkOrder");
        callTest(loader, RomanTest.class, "case3");
        callTest(loader, FormatValidatorTest.class, "testWindowsFileName1");
        callTest(loader, TypeUtilTest.class, "caseDiamond");
    }

    private void assertOperator(@NotNull UnaryOperator<ClassFile> operator) throws Exception {
        assertLoader(new RewriteClassLoader.UsingStream(operator));
        // assertLoader(new RewriteClassLoader.UsingBuffer(operator)); // constant pool utf8 writer is not ready
    }

    @Test
    public void testIdentity() throws Exception {
        assertOperator(UnaryOperator.identity());
    }

    @Test
    public void testShuffleAttributeInfo() throws Exception {
        assertOperator(classFile -> {
            classFile.shuffleAttributeInfo();
            return classFile;
        });
    }
}
