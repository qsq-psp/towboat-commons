package indi.um.json.value;

import indi.um.json.api.JsonConsumer;
import indi.um.json.reflect.JsonSerializer;
import indi.um.json.api.ValueSerializer;
import indi.um.json.reflect.ConversionConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileStore;

/**
 * Created on 2022/7/12.
 *
 * {
 *     provider: "file",
 *     open: true,
 *     readOnly: false,
 *     separator: "\\",
 *     roots: [
 *         "C:\\",
 *         "D:\\"
 *     ],
 *     stores: [
 *         {
 *             name: "Windows",
 *             type: "NTFS",
 *             readonly: false,
 *             total: 85899341824,
 *             usable: 18826203136,
 *             unallocated: 18826203136,
 *             blockSize: 512
 *         },
 *         {
 *             name: "Data",
 *             type: "NTFS",
 *             readonly: false,
 *             total: 413524815872,
 *             usable: 109440954368,
 *             unallocated: 109440954368,
 *             blockSize: 512
 *         }
 *     ],
 *     views: [
 *         "owner",
 *         "dos",
 *         "acl",
 *         "basic",
 *         "user"
 *     ]
 * }
 */
public class FileStoreValueSerializer implements ValueSerializer<FileStore> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStoreValueSerializer.class);

    public static final FileStoreValueSerializer INSTANCE = new FileStoreValueSerializer();

    @Override
    public void serialize(String key, FileStore value, @NotNull JsonConsumer jc, @NotNull ConversionConfig cc, @NotNull JsonSerializer js) {
        jc.optionalKey(key);
        jc.openObject();
        jc.key("name");
        jc.stringValue(value.name());
        jc.key("type");
        jc.stringValue(value.type());
        jc.key("readonly");
        jc.booleanValue(value.isReadOnly());
        try {
            long space = value.getTotalSpace();
            jc.key("total");
            jc.numberValue(space);
            space = value.getUsableSpace();
            jc.key("usable");
            jc.numberValue(space);
            space = value.getUnallocatedSpace();
            jc.key("unallocated");
            jc.numberValue(space);
            space = value.getBlockSize();
            jc.key("blockSize");
            jc.numberValue(space);
        } catch (Exception e) {
            if (js.logEnabled()) {
                LOGGER.warn("spaces", e);
            }
        }
        try {
            int vsn = (int) value.getAttribute("column:vsn"); // volume serial number
            jc.key("vsn");
            jc.numberValue(vsn);
        } catch (Exception e) {
            if (js.logEnabled()) {
                LOGGER.warn("attributes", e);
            }
        }
        jc.closeObject();
    }
}
