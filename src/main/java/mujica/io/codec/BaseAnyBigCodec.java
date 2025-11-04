package mujica.io.codec;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/5/4")
public abstract class BaseAnyBigCodec extends BaseAnyCodec {

    private static final long serialVersionUID = 0x712a05cc3d7e1ca3L;

    private BigInteger buffer;

    public BaseAnyBigCodec(@NotNull BaseAnyCodecSpec spec) {
        super(spec);
    }
}
