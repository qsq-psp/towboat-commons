package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.attribute.BasicFileAttributes;

@CodeHistory(date = "2026/8/19")
public class BasicFileAttributesTransformer implements JsonContextTransformer<BasicFileAttributes> {

    static final FastString LAST_MODIFIED_TIME = new FastString("lastModifiedTime");

    static final FastString LAST_ACCESS_TIME = new FastString("lastAccessTime");

    static final FastString CREATION_TIME = new FastString("creationTime");

    static final FastString REGULAR_FILE = new FastString("regularFile");

    static final FastString DIRECTORY = new FastString("directory");

    static final FastString SYMBOLIC_LINK = new FastString("symbolicLink");

    static final FastString SIZE = new FastString("size");

    @Override
    public void transform(BasicFileAttributes in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.key(LAST_MODIFIED_TIME);
            out.numberValue(in.lastModifiedTime().toMillis());
            out.key(LAST_ACCESS_TIME);
            out.numberValue(in.lastAccessTime().toMillis());
            out.key(CREATION_TIME);
            out.numberValue(in.creationTime().toMillis());
            out.key(REGULAR_FILE);
            out.booleanValue(in.isRegularFile());
            out.key(DIRECTORY);
            out.booleanValue(in.isDirectory());
            out.key(SYMBOLIC_LINK);
            out.booleanValue(in.isSymbolicLink());
            out.key(SIZE);
            out.numberValue(in.size());
        }
        out.closeObject();
    }
}
