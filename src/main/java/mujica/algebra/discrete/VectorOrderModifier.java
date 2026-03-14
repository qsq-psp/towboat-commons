package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/3/1", name = "VectorExchangeModifier")
@CodeHistory(date = "2026/2/24")
public class VectorOrderModifier extends DimensionCodecModifier {

    public VectorOrderModifier(@NotNull DimensionCodec codec) {
        super(codec);
    }
}
