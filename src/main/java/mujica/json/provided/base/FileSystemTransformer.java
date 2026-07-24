package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.handler.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@CodeHistory(date = "2022/9/9", project = "Ultramarine", name = "FileSystemValueSerializer")
@CodeHistory(date = "2026/4/27")
public class FileSystemTransformer implements JsonContextTransformer<FileSystem>, JsonStructure {

    public static final FileSystemTransformer INSTANCE = new FileSystemTransformer();

    static final FastString SCHEME = new FastString("scheme");

    static final FastString OPEN = new FastString("open");

    static final FastString READ_ONLY = new FastString("readOnly");

    static final FastString SEPARATOR = new FastString("separator");

    static final FastString ROOTS = new FastString("roots");

    static final FastString STORES = new FastString("stores");

    static final FastString VIEWS = new FastString("views");

    @Override
    public void transform(@NotNull FileSystem fileSystem, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(SCHEME);
            out.stringValue(fileSystem.provider().getScheme());
            out.key(OPEN);
            out.booleanValue(fileSystem.isOpen());
            out.key(READ_ONLY);
            out.booleanValue(fileSystem.isReadOnly());
            out.key(SEPARATOR);
            out.stringValue(fileSystem.getSeparator());
            out.key(ROOTS);
            out.openArray();
            for (Path path : fileSystem.getRootDirectories()) {
                PathTransformer.INSTANCE.transform(path, out, context);
            }
            out.closeArray();
            out.key(STORES);
            out.openArray();
            for (FileStore store : fileSystem.getFileStores()) {
                FileStoreTransformer.INSTANCE.transform(store, out, context);
            }
            out.closeArray();
            out.key(VIEWS);
            out.openArray();
            for (String view : fileSystem.supportedFileAttributeViews()) {
                out.stringValue(view);
            }
            out.closeArray();
        }
        out.closeObject();
    }

    @Override
    public void json(@NotNull JsonHandler jh) {
        transform(FileSystems.getDefault(), jh, null);
    }
}
