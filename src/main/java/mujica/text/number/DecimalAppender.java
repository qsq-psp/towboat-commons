package mujica.text.number;

import mujica.ds.of_double.PublicDoubleSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.*;

@CodeHistory(date = "2018/7/9", project = "existence", name = "DoubleToStringConverter")
@CodeHistory(date = "2025/2/25", name = "DecimalToStringFunction")
@CodeHistory(date = "2026/3/18")
public class DecimalAppender {

    public static final List<Class<? extends Number>> NUMBER_FLOAT_CLASSES = List.of(
            Float.class
    );

    public void acceptFloat(float value, @NotNull StringBuilder out) {
        out.append(value);
    }

    public static final List<Class<? extends Number>> NUMBER_DOUBLE_CLASSES = List.of(
            Double.class,
            DoubleAdder.class,
            DoubleAccumulator.class,
            PublicDoubleSlot.class
    );

    public void acceptDouble(double value, @NotNull StringBuilder out) {
        out.append(value);
    }
}
