package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/1", name = "VectorExchangeModifier")
public class VectorExchangeModifier extends DimensionCodecModifier { // VectorOrderModifier

    public VectorExchangeModifier(@NotNull DimensionCodec codec) {
        super(codec);
    }
}
