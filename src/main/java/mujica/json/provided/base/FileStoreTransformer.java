package mujica.json.provided.base;

import mujica.json.container.FastString;
import mujica.json.handler.JsonHandler;
import mujica.json.reflect.JsonContext;
import mujica.json.reflect.JsonContextTransformer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.FileStore;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "FileStoreValueSerializer")
@CodeHistory(date = "2026/4/23")
public class FileStoreTransformer implements JsonContextTransformer<FileStore> {

    public static final FileStoreTransformer INSTANCE = new FileStoreTransformer();

    static final FastString TYPE = new FastString("type");

    static final FastString READ_ONLY = new FastString("readOnly");

    static final FastString TOTAL = new FastString("total");

    static final FastString USABLE = new FastString("usable");

    static final FastString UNALLOCATED = new FastString("unallocated");

    static final FastString BLOCK_SIZE = new FastString("blockSize");

    @Override
    public void transform(@NotNull FileStore in, @NotNull JsonHandler out, @Nullable JsonContext context) {
        out.openObject();
        {
            out.key(ClassLoaderTransformer.NAME);
            out.stringValue(in.name());
            out.key(TYPE);
            out.stringValue(in.type());
            out.key(READ_ONLY);
            out.booleanValue(in.isReadOnly());
            try {
                long space = in.getTotalSpace(); // throws IOException
                out.key(TOTAL);
                out.numberValue(space);
                space = in.getUsableSpace(); // throws IOException
                out.key(USABLE);
                out.numberValue(space);
                space = in.getUnallocatedSpace(); // throws IOException
                out.key(UNALLOCATED);
                out.numberValue(space);
                space = in.getBlockSize(); // throws IOException
                out.key(BLOCK_SIZE);
                out.numberValue(space);
            } catch (Exception e) {
                if (context != null) {
                    context.getLogger().debug("{}", in, e);
                }
            }
            try { // found in WindowsFileStore
                int vsn = (Integer) in.getAttribute("volume:vsn"); // throws IOException, UnsupportedOperationException
                out.key("VSN");
                out.numberValue(vsn);
                boolean bool = (Boolean) in.getAttribute("volume:isRemovable"); // throws IOException, UnsupportedOperationException
                out.key("removable");
                out.booleanValue(bool);
                bool = (Boolean) in.getAttribute("volume:isCdrom"); // throws IOException, UnsupportedOperationException
                out.key("CDROM");
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
