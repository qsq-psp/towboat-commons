package mujica.json.provided;

import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileStore;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "FileStoreValueSerializer")
@CodeHistory(date = "2026/4/23")
public class FileStoreTransformer implements JsonContextTransformer<FileStore> {

    public static final FileStoreTransformer INSTANCE = new FileStoreTransformer();

    static final FastString NAME = new FastString("name");

    static final FastString TYPE = new FastString("type");

    static final FastString READ_ONLY = new FastString("readOnly");

    static final FastString TOTAL = new FastString("total");

    static final FastString USABLE = new FastString("usable");

    static final FastString UNALLOCATED = new FastString("unallocated");

    static final FastString BLOCK_SIZE = new FastString("blockSize");

    @Override
    public void transform(FileStore in, @NotNull JsonHandler out, JsonContext context) {
        out.openObject();
        {
            out.stringKey(NAME);
            out.stringValue(in.name());
            out.stringKey(TYPE);
            out.stringValue(in.type());
            out.stringKey(READ_ONLY);
            out.booleanValue(in.isReadOnly());
            try {
                long space = in.getTotalSpace(); // throws IOException
                out.stringKey(TOTAL);
                out.numberValue(space);
                space = in.getUsableSpace(); // throws IOException
                out.stringKey(USABLE);
                out.numberValue(space);
                space = in.getUnallocatedSpace(); // throws IOException
                out.stringKey(UNALLOCATED);
                out.numberValue(space);
                space = in.getBlockSize(); // throws IOException
                out.stringKey(BLOCK_SIZE);
                out.numberValue(space);
            } catch (Exception e) {
                if (context != null) {
                    context.getLogger().debug("{}", in, e);
                }
            }
            try { // found in WindowsFileStore
                int vsn = (Integer) in.getAttribute("volume:vsn"); // throws IOException, UnsupportedOperationException
                out.stringKey("VSN");
                out.numberValue(vsn);
                boolean bool = (Boolean) in.getAttribute("volume:isRemovable"); // throws IOException, UnsupportedOperationException
                out.stringKey("removable");
                out.booleanValue(bool);
                bool = (Boolean) in.getAttribute("volume:isCdrom"); // throws IOException, UnsupportedOperationException
                out.stringKey("CDROM");
                out.booleanValue(bool);
            } catch (Exception e) {
                if (context != null) {
                    context.getLogger().debug("{}", in, e);
                }
            }
            // nothing new in UnixFileStore or LinuxFileStore
        }
        out.closeObject();
    }
}
