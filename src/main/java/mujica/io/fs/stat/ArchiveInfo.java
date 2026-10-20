package mujica.io.fs.stat;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/5/5")
class ArchiveInfo extends TypedFileInfo {

    private int itemCount;

    private long uncompressedSize;

    private long compressedSize;
}
