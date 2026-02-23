package mujica.io.misc;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.FileStore;

@CodeHistory(date = "2026/2/19")
public class CachedFileStoreAttribute implements CachedDataSize<FileStore> {

    @NotNull
    private final FileStore fileStore;

    @NotNull
    private FileStoreAttributeKey attributeKey;

    private transient long value;

    public CachedFileStoreAttribute(@NotNull FileStore fileStore, @NotNull FileStoreAttributeKey attributeKey) {
        super();
        this.fileStore = fileStore;
        this.attributeKey = attributeKey;
    }

    @NotNull
    public FileStoreAttributeKey getAttributeKey() {
        return attributeKey;
    }

    @NotNull
    public CachedFileStoreAttribute setAttributeKey(@NotNull FileStoreAttributeKey attributeKey) {
        this.attributeKey = attributeKey;
        return this;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public void setLong(long newValue) {
        value = newValue;
    }

    @NotNull
    @Override
    public FileStore getTarget() {
        return fileStore;
    }

    @NotNull
    @Override
    public CachedFileStoreAttribute updateCache() throws IOException {
        switch (attributeKey) {
            case TOTAL_SPACE:
                value = fileStore.getTotalSpace();
                break;
            case USABLE_SPACE:
                value = fileStore.getUsableSpace();
                break;
            case BLOCK_SIZE:
                value = fileStore.getBlockSize();
                break;
            case UNALLOCATED_SPACE:
                value = fileStore.getUnallocatedSpace();
                break;
            case USED_SPACE:
                value = fileStore.getTotalSpace() - fileStore.getUnallocatedSpace();
                break;
        }
        return this;
    }

    @Override
    public int hashCode() {
        return fileStore.hashCode() ^ attributeKey.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CachedFileStoreAttribute)) {
            return false;
        }
        final CachedFileStoreAttribute that = (CachedFileStoreAttribute) obj;
        return this.fileStore.equals(that.fileStore) && this.attributeKey.equals(that.attributeKey);
    }
}
