package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

@CodeHistory(date = "2026/9/23")
@ReferencePage(title = "", href = "https://github.com/electron/asar")
public class AsarIntegrity {

    public String algorithm;

    public String hash;

    public long blockSize;

    public String[] blocks;

    public AsarIntegrity() {
        super();
    }
}
