package mujica.io.codec;

import mujica.reflect.modifier.*;

@CodeHistory(date = "2022/3/23", project = "Ultramarine", name = "HexCase")
@CodeHistory(date = "2025/3/2")
@Stable(date = "2025/10/9")
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface Base16Case {

    @DataType("u8")
    int UPPER = 'A' - 0xA;

    @DataType("u8")
    int LOWER = 'a' - 0xa;
}
