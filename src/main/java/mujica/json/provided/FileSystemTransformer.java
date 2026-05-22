package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.JsonStructure;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

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
    public void transform(FileSystem in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(SCHEME);
            out.stringValue(in.provider().getScheme());
            out.stringKey(OPEN);
            out.booleanValue(in.isOpen());
            out.stringKey(READ_ONLY);
            out.booleanValue(in.isReadOnly());
            out.stringKey(SEPARATOR);
            out.stringValue(in.getSeparator());
            out.stringKey(ROOTS);
            out.openArray();
            for (Path path : in.getRootDirectories()) {
                PathTransformer.INSTANCE.transform(path, out, context);
            }
            out.closeArray();
            out.stringKey(STORES);
            out.openArray();
            for (FileStore store : in.getFileStores()) {
                FileStoreTransformer.INSTANCE.transform(store, out, context);
            }
            out.closeArray();
            out.stringKey(VIEWS);
            out.openArray();
            for (String view : in.supportedFileAttributeViews()) {
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
