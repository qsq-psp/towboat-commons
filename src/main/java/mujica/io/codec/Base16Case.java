package mujica.io.codec;

import mujica.reflect.modifier.*;

@CodeHistory(date = "2022/3/23", project = "Ultramarine", name = "HexCase")
@CodeHistory(date = "2025/3/2")
@ConstantInterface(composition = ConstantComposition.NEVER)
public interface Base16Case {

    @DataType("u8")
    int UPPER_CONSTANT = 'A' - 0xA;

    @DataType("u8")
    int LOWER_CONSTANT = 'a' - 0xa;
}
