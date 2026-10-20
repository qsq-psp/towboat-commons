package mujica.io.fs;

import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

import java.util.HashMap;

@CodeHistory(date = "2026/9/23")
@ReferencePage(title = "", href = "https://github.com/electron/asar")
public class AsarEntry {

    public HashMap<String, AsarEntry> files;

    @JsonHint(JsonHint.CAST_FROM_STRING)
    public long offset;

    public long size;

    public boolean executable;

    public AsarIntegrity integrity;

    public AsarEntry() {
        super();
    }
}
