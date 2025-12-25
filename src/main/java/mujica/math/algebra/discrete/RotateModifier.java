package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/12/20")
public class RotateModifier extends VectorExchangeModifier {

    final int move;

    public RotateModifier(@NotNull DimensionCodec codec, int move) {
        super(codec);
        this.move = move;
    }
}
