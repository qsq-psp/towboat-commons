package mujica.reflect.bytecode;

import mujica.io.buffer.ByteBufferUtil;
import mujica.io.nest.BufferedLimitedDataInputStream;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@CodeHistory(date = "2025/9/28")
public abstract class RewriteClassLoader extends ProjectClassLoader {

    @NotNull
    protected final UnaryOperator<ClassFile> operator;

    public RewriteClassLoader(@NotNull Predicate<String> predicate, @NotNull UnaryOperator<ClassFile> operator) {
        super(predicate);
        this.operator = operator;
    }

    @CodeHistory(date = "2025/9/28")
    public static class UsingStream extends RewriteClassLoader {

        public UsingStream(@NotNull Predicate<String> predicate, @NotNull UnaryOperator<ClassFile> operator) {
            super(predicate, operator);
        }

        public UsingStream(@NotNull Predicate<String> predicate) {
            super(predicate, UnaryOperator.identity());
        }

        public UsingStream(@NotNull UnaryOperator<ClassFile> operator) {
            super(rootPackagePredicate(), operator);
        }

        public UsingStream() {
            super(rootPackagePredicate(), UnaryOperator.identity());
        }

        @NotNull
        @Override
        protected Class<?> loadProjectClass(@NotNull String name) throws ClassNotFoundException {
            try (BufferedLimitedDataInputStream in = new BufferedLimitedDataInputStream(getClassStream(name))) {
                ClassFile classFile = new ClassFile();
                classFile.read(in);
                operator.apply(classFile);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                try (DataOutputStream out = new DataOutputStream(os)) {
                    classFile.write(out);
                    out.flush();
                }
                byte[] data = os.toByteArray();
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                throw new ClassNotFoundException(name, e);
            }
        }
    }

    @CodeHistory(date = "2025/9/28")
    public static class UsingBuffer extends RewriteClassLoader {

        public UsingBuffer(@NotNull Predicate<String> predicate, @NotNull UnaryOperator<ClassFile> operator) {
            super(predicate, operator);
        }

        public UsingBuffer(@NotNull Predicate<String> predicate) {
            super(predicate, UnaryOperator.identity());
        }

        public UsingBuffer(@NotNull UnaryOperator<ClassFile> operator) {
            super(rootPackagePredicate(), operator);
        }

        public UsingBuffer() {
            super(rootPackagePredicate(), UnaryOperator.identity());
        }

        @NotNull
        @Override
        protected Class<?> loadProjectClass(@NotNull String name) throws ClassNotFoundException {
            try (InputStream is = getClassStream(name)) {
                ByteBuffer buffer = ByteBufferUtil.read(is);
                ClassFile classFile = new ClassFile();
                classFile.read(buffer);
                operator.apply(classFile);
                buffer = ByteBufferUtil.read((Consumer<ByteBuffer>) classFile::write, buffer.capacity());
                return defineClass(name, buffer, null);
            } catch (Exception e) {
                throw new ClassNotFoundException(name, e);
            }
        }
    }
}
