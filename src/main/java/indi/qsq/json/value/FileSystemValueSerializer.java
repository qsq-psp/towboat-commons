package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import indi.um.json.reflect.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created on 2022/9/9.
 */
@SuppressWarnings("unused")
public class FileSystemValueSerializer implements ValueSerializer<FileSystem>, JsonStructure {

    public static final FileSystemValueSerializer INSTANCE = new FileSystemValueSerializer();

    @Override
    public void serialize(String key, FileSystem value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("provider");
        jc.stringValue(value.provider().getScheme());
        jc.key("open");
        jc.booleanValue(value.isOpen());
        jc.key("readOnly");
        jc.booleanValue(value.isReadOnly());
        jc.key("separator");
        jc.stringValue(value.getSeparator());
        jc.key("roots");
        {
            jc.openArray();
            for (Path path : value.getRootDirectories()) {
                jc.stringValue(path.toString());
            }
            jc.closeArray();
        }
        jc.key("stores");
        {
            jc.openArray();
            for (FileStore store : value.getFileStores()) {
                FileStoreValueSerializer.INSTANCE.serialize(null, store, jc, cc, js);
            }
            jc.closeArray();
        }
        jc.key("views");
        jc.stringsValue(value.supportedFileAttributeViews());
        jc.closeObject();
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        serialize(null, FileSystems.getDefault(), jc, new ConversionConfig(), new JsonSerializer());
    }
}
