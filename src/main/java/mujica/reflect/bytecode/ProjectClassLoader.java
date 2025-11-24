package mujica.reflect.bytecode;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.function.Predicate;

@CodeHistory(date = "2025/9/27")
public class ProjectClassLoader extends ClassLoader {

    @NotNull
    protected static Predicate<String> rootPackagePredicate() {
        return new Predicate<>() {
            @NotNull
            final String prefix; // should be "mujica."
            {
                String name = ProjectClassLoader.class.getName();
                int dot = name.indexOf('.');
                if (dot == -1) {
                    name = "";
                } else {
                    name = name.substring(0, dot + 1);
                }
                prefix = name;
            }
            @Override
            public boolean test(String string) {
                return string.startsWith(prefix);
            }
        };
    }

    @NotNull
    protected final Predicate<String> predicate;

    protected final HashMap<String, Class<?>> loaded = new HashMap<>();

    public ProjectClassLoader(@NotNull Predicate<String> predicate) {
        super();
        this.predicate = predicate;
    }

    public ProjectClassLoader() {
        this(rootPackagePredicate());
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (predicate.test(name)) {
            Class<?> clazz = loaded.get(name);
            if (clazz == null) {
                clazz = loadProjectClass(name);
            }
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        } else {
            return super.loadClass(name, resolve);
        }
    }

    @NotNull
    protected Class<?> loadProjectClass(@NotNull String name) throws ClassNotFoundException {
        try (InputStream is = getClassStream(name)) {
            byte[] data = is.readAllBytes();
            return defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }
    }

    @NotNull
    protected InputStream getClassStream(@NotNull String name) throws ClassNotFoundException {
        final String resourceName = '/' + name.replace('.', '/') + ".class";
        final InputStream is = ProjectClassLoader.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new ClassNotFoundException();
        }
        return is;
    }
}
