package mujica.io.fs;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2026/9/25")
public class MinecraftAssetEntry {

    public String name; // path separated by slash (not started with slash)

    public String hash; // lower case hex SHA1, char length = 40

    public int size; // bytes
}
