package mujica.json.io;

import mujica.reflect.modifier.*;

@CodeHistory(date = "2023/5/13", project = "omnidirectional")
@CodeHistory(date = "2026/6/1")
@ReferencePage(title = "NBT格式", href = "https://minecraft.fandom.com/zh/wiki/NBT%E6%A0%BC%E5%BC%8F")
@ReferencePage(title = "NBT格式", href = "https://zh.minecraft.wiki/w/NBT%E6%A0%BC%E5%BC%8F")
@Name(value = "Named Binary Tag", language = "en")
@Name(value = "二进制命名标签", language = "zh")
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface Nbt {

    int ID_END = 0;
    int ID_BYTE = 1;
    int ID_SHORT = 2;
    int ID_INT = 3;
    int ID_LONG = 4;
    int ID_FLOAT = 5;
    int ID_DOUBLE = 6;
    int ID_BYTE_ARRAY = 7;
    int ID_STRING = 8;
    int ID_LIST = 9;
    int ID_COMPOUND = 10;
    int ID_INT_ARRAY = 11;
    int ID_LONG_ARRAY = 12;
}
